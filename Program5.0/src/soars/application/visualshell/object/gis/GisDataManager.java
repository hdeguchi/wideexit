/**
 * 
 */
package soars.application.visualshell.object.gis;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JTable;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.object.gis.edit.field.selector.Field;
import soars.application.visualshell.object.gis.file.loader.FileData;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.application.visualshell.object.gis.object.base.GisSimpleVariableObject;
import soars.application.visualshell.object.gis.object.map.GisMapObject;
import soars.application.visualshell.object.gis.object.variable.GisVariableObject;
import soars.application.visualshell.observer.Observer;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.utility.swing.file.manager.table.FilenameComparator;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.stream.StreamPumper;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class GisDataManager extends Vector<GisData> {

	/**
	 * 
	 */
	public GisDataManager() {
		super();
	}

	/**
	 * @param directories
	 * @return
	 */
	public List<File> read(File[] directories) {
		List<File> shapeFiles = get_shapeFiles( directories);
		if ( shapeFiles.isEmpty())
			return shapeFiles;

		shapeFiles = sort( shapeFiles.toArray( new File[ 0]));

		for ( File shapeFile:shapeFiles) {
			GisData gisData = read( shapeFile);
			if ( null == gisData)
				return null;

			add( gisData);
		}

		if ( !examine())
			return null;

		return shapeFiles;
	}

	/**
	 * @param directories
	 * @return
	 */
	private List<File> get_shapeFiles(File[] directories) {
		List<File> shapeFiles = new ArrayList<File>();
		for ( File directory:directories) {
			if ( !directory.exists() || !directory.isDirectory())
				continue;

			File[] files = directory.listFiles();
			for ( File file:files) {
				if ( file.isFile() && file.getName().endsWith( ".shp"))
					shapeFiles.add( file);
			}
		}
		return shapeFiles;
	}

	/**
	 * @param files
	 * @return
	 */
	private List<File> sort(File[] files) {
		Arrays.sort( files, new FilenameComparator( "shp", true, false));
		return Arrays.asList( files);
	}

	/**
	 * @param shapeFile
	 * @return
	 */
	private GisData read(File shapeFile) {
		String[] cmdarray = get_cmdarray( Constant._dbasefileAnalyzerJarFilename, shapeFile);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec( cmdarray);
			new StreamPumper( process.getErrorStream(), false).start();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		GisData gisData = new GisData( shapeFile.getName().substring( 0, shapeFile.getName().length() - ".shp".length()));

		boolean result = true;
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( process.getInputStream()));
		try {
			String line = null;
			while ( true) {
				line = bufferedReader.readLine();
				if ( null == line)
					break;

				if ( line.startsWith( "name")) {
					if ( !gisData.set_name( line)) {
						result = false;
						break;
					}
				} else if ( line.startsWith( "type")) {
					if ( !gisData.set_type( line)) {
						result = false;
						break;
					}
				} else if ( line.startsWith( "class")) {
					if ( !gisData.set_class( line)) {
						result = false;
						break;
					}
				} else {
					System.out.println( line);
					result = false;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}

		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ( result ? gisData : null);
	}

	/**
	 * @return
	 */
	private boolean examine() {
		for ( int i = 1; i < size(); ++i) {
			if ( !get( i).compare( get( i - 1)))
				return false;
		}
		return true;
	}

	/**
	 * @param shapeFiles
	 * @return
	 */
	public boolean load(List<File> shapeFiles) {
		if ( null == shapeFiles || shapeFiles.isEmpty() || size() != shapeFiles.size())
			return false;

		for ( int i = 0; i < size(); ++i) {
			if ( !load( get( i), shapeFiles.get( i)))
				return false;
		}

		//print();

		return true;
	}

	/**
	 * @param gisData
	 * @param shapeFile
	 * @return
	 */
	private boolean load(GisData gisData, File shapeFile) {
		String[] cmdarray = get_cmdarray( Constant._shapefileAnalyzerJarFilename, shapeFile);

		debug( "GIS import", System.getProperty( "os.name"), System.getProperty( "os.version"), cmdarray);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec( cmdarray);
			new StreamPumper( process.getErrorStream(), false).start();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		boolean result = true;
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( process.getInputStream()));
		try {
			String line = null;
			while ( true) {
				line = bufferedReader.readLine();
				if ( null == line)
					break;

				if ( line.startsWith( "record")) {
					GisDataRecord gisDataRecord = new GisDataRecord();
					if ( !gisDataRecord.set( line)) {
						result = false;
						break;
					}
					gisData.add( gisDataRecord);
				} else if ( line.startsWith( "range")) {
					// Do nothing!
				} else {
					System.out.println( line);
					result = false;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}

		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * @param program
	 * @param shapeFile
	 * @return
	 */
	private String[] get_cmdarray(String program, File shapeFile) {
		String memorySize = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");
		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant.get_mac_java_command());
			list.add( "-Dfile.encoding=UTF-8");
		} else {
			list.add( Tool.get_java_command());
		}

		if ( !memorySize.equals( "0"))
			list.add( "-Xmx" + memorySize + "m");
		list.add( "-jar");
		list.add( program);
		list.add( shapeFile.getAbsolutePath());
		list.add( Environment.get_instance().get( Environment._gisShapefileAnalyzerCharacterCode, "MS932"));

		return list.toArray( new String[ 0]);
	}

	/**
	 * @param type
	 * @param osname
	 * @param osversion
	 * @param cmdarray
	 */
	private static void debug(String type, String osname, String osversion, String[] cmdarray) {
		String text = ""; 
		text += ( "Type : " + type + Constant._lineSeparator);
		text += ( "OS : " + osname + " [" + osversion + "]" + Constant._lineSeparator);
		for ( int i = 0; i < cmdarray.length; ++i)
			text += ( "Parameter : " + cmdarray[ i] + Constant._lineSeparator);

		Clipboard.set( text);
	}

	/**
	 * @param containsEmpty 
	 * @return
	 */
	public String[] get_fields(boolean containsEmpty) {
		List<String> fields = new ArrayList<String>();
		if ( containsEmpty)
			fields.add( "");

		if ( isEmpty())
			return fields.toArray( new String[ 0]);
		else {
			fields.addAll( get( 0)._names);
			return fields.toArray( new String[ 0]);
		}
	}

	/**
	 * @param type
	 * @param containsEmpty
	 * @return
	 */
	public List<String> get_numeric_fields(String type, boolean containsEmpty) {
		List<String> fields = new ArrayList<String>();
		if ( containsEmpty)
			fields.add( "");

		if ( isEmpty())
			return fields;
		else
			return get( 0).get_numeric_fields( type, fields);
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public List<String> get_time_fields(boolean containsEmpty) {
		// TODO Auto-generated method stub
		List<String> fields = new ArrayList<String>();
		if ( containsEmpty)
			fields.add( "");

		if ( isEmpty())
			return fields;
		else
			return get( 0).get_time_fields( fields);
	}

	/**
	 * @param spotFields
	 * @param gisObjectBases
	 * @param component
	 * @return
	 */
	public boolean can_append_spots(List<Field> spotFields, List<GisObjectBase> gisObjectBases, Component component) {
		int[] indices = get_indices( spotFields);
		if ( null == indices)
			return false;

		if ( !set_indices( gisObjectBases))
			return false;

		for ( GisData gisData:this) {
			if ( !gisData.can_append_spots( indices, spotFields, gisObjectBases, component))
				return false;
		}

		return true;
	}

	/**
	 * @return
	 */
	public double[] get_range() {
		if ( isEmpty())
			return null;

		double[] range = new double[ 4];	// { left, top, right, bottom}

		for ( int i = 0; i < size(); ++i) {
			double[] temp = get( i).get_range();
			if ( null == temp)
				return null;

			if ( 0 == i) {
				for ( int j = 0; j < temp.length; ++j)
					range[ j] = temp[ j];
			} else {
				range[ 0] = Math.min( range[ 0], temp[ 0]);
				range[ 1] = Math.min( range[ 1], temp[ 1]);
				range[ 2] = Math.max( range[ 2], temp[ 2]);
				range[ 3] = Math.max( range[ 3], temp[ 3]);
			}
		}

		return range;
	}

	/**
	 * @param spotFields
	 * @param gisObjectBases
	 * @param range
	 * @param ratio
	 * @param component
	 * @return
	 */
	public boolean append_spots(List<Field> spotFields, List<GisObjectBase> gisObjectBases, double[] range, double[] ratio, JComponent component) {
		int[] indices = get_indices( spotFields);
		if ( null == indices)
			return false;

		String gis = LayerManager.get_instance().get_unique_gis_id();

		boolean result = true;
		for ( GisData gisData:this) {
			if ( !gisData.append_spots( indices, spotFields, gisObjectBases, gis, range, ratio, component)) {
				result = false;
				break;
			}
		}

		LayerManager.get_instance().update_preferred_size( component);

		Observer.get_instance().modified();

		return result;
	}

	/**
	 * @param fields
	 * @return
	 */
	private int[] get_indices(List<Field> fields) {
		int[] indices = new int[ fields.size()];
		for ( int i = 0; i < fields.size(); ++i) {
			if ( !fields.get( i)._flag)
				indices[ i] = -1;
			else {
				int index = get_index( fields.get( i)._value);
				if ( 0 > index)
					return null;

				indices[ i] = index;
			}
		}
		return indices;
	}

	/**
	 * @param value
	 * @return
	 */
	private int get_index(String value) {
		if ( isEmpty())
			return -1;

		return get( 0)._names.indexOf( value);
	}

	/**
	 * @param gisObjectBases
	 * @return
	 */
	private boolean set_indices(List<GisObjectBase> gisObjectBases) {
		// TODO 仕様が決まらないと実装出来ない
		for ( GisObjectBase gisObjectBase:gisObjectBases) {
			if ( gisObjectBase instanceof GisSimpleVariableObject) {
				GisSimpleVariableObject gisSimpleVariableObject = ( GisSimpleVariableObject)gisObjectBase;
				gisSimpleVariableObject._indices = get_indices( gisSimpleVariableObject._fields);
				if ( null == gisSimpleVariableObject._indices)
					return false;
			} else if ( gisObjectBase instanceof GisVariableObject) {
				// 未実装
			} else if ( gisObjectBase instanceof GisMapObject) {
				// 未実装
			}
		}
		return true;
	}

	/**
	 * @param gisDataManager
	 * @param gisDataTables
	 * @return
	 */
	public boolean cerate(GisDataManager gisDataManager, List<JTable> gisDataTables) {
		if ( gisDataManager.size() != gisDataTables.size())
			return false;

		for ( int i = 0; i < gisDataManager.size(); ++i) {
			GisData gisData = new GisData( gisDataManager.get( i));
			if ( !gisData.cerate( gisDataTables.get( i)))
				return false;

			add( gisData);
		}

		return true;
	}

	/**
	 * @param fileDataList
	 * @param gisDataDirectory
	 * @return
	 */
	public boolean load(List<FileData> fileDataList, File gisDataDirectory) {
		clear();

		for ( int i = 0; i < fileDataList.size(); ++i) {
			GisData gisData = new GisData( fileDataList.get( i));
			if ( !gisData.create( new File( gisDataDirectory, String.valueOf( i) + ".csv")))
				return false;

			add( gisData);
		}

		return true;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(Writer writer) throws SAXException {
		if ( isEmpty())
			return true;

		writer.startElement( null, null, "file_data", new AttributesImpl());

		for ( GisData gisData:this) {
			if ( !gisData.write( writer))
				return false;
		}

		writer.endElement( null, null, "file_data");

		return true;
	}

	/**
	 * @param gisDataDirectory
	 * @return
	 */
	public boolean write(File gisDataDirectory) {
		for ( int i = 0; i < size(); ++i) {
			if ( !get( i).write( new File( gisDataDirectory, String.valueOf( i) + ".csv")))
				return false;
		}
		return true;
	}

	/**
	 * 
	 */
	public void print() {
		for ( GisData gisData:this)
			gisData.print();
	}
}
