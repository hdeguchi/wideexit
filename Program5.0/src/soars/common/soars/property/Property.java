/**
 * 
 */
package soars.common.soars.property;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.common.soars.constant.CommonConstant;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.file.Entry;
import soars.common.utility.tool.file.ZipUtility;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class Property {

	/**
	 * 
	 */
	public File _file = null;

	/**
	 * 
	 */
	public String _type = "";

	/**
	 * 
	 */
	public String _id = "";

	/**
	 * 
	 */
	public String _title = "";

	/**
	 * 
	 */
	public String _comment = "";

	/**
	 * 
	 */
	public TreeMap<String, Property> _propertyMap = new TreeMap<String, Property>();

	/**
	 * 
	 */
	public String _parentID = "";

	/**
	 * @param file
	 * @param type
	 * @param id
	 * @param title
	 * @param comment
	 */
	public Property(File file, String type, String id, String title, String comment) {
		super();
		_file = file;
		_type = type;
		_id = id;
		_title = title;
		_comment = comment;
	}

	/**
	 * @param file
	 * @param type
	 * @param id
	 * @param title
	 * @param comment
	 * @param parentID
	 */
	public Property(File file, String type, String id, String title, String comment, String parentID) {
		super();
		_file = file;
		_type = type;
		_id = id;
		_title = title;
		_comment = comment;
		_parentID = parentID;
	}

	/**
	 * @param propertyMap
	 * @param file
	 * @return
	 */
	public static boolean get_simulation_properties(TreeMap<String, Property> propertyMap, File file) {
		if ( !ZipUtility.find( file, CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._simulatorRootDirectoryName + "/" + CommonConstant._propertyFileName)) {
			// file内のシミュレーションデータからpropertyMapへデータを設定
			if ( !create_simulation_properties( propertyMap, file))
				return false;

			// ここでxmlファイル(シミュレーション)を作成してfileへ挿入
			if ( !append_simulation_properties( propertyMap, file))
				return false;
		} else {
			// file内xmlファイル(シミュレーション)のデータをpropertyMapへ設定
			if ( !SimulationSaxLoader.execute( file, propertyMap))
				return false;
		}
		return true;
	}

	/**
	 * @param propertyMap
	 * @param file
	 * @return
	 */
	private static boolean create_simulation_properties(TreeMap<String, Property> propertyMap, File file) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream( new FileInputStream( file));
			ZipEntry zipEntry = null;
			try {
				while ( null != ( zipEntry = zipInputStream.getNextEntry())) {
					if ( zipEntry.isDirectory()) {
						zipInputStream.closeEntry();
						continue;
					}
					if ( !zipEntry.getName().startsWith( CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._simulatorRootDirectoryName + "/")) {
						zipInputStream.closeEntry();
						continue;
					}
					if ( zipEntry.getName().equals( CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._simulatorRootDirectoryName + "/")) {
						zipInputStream.closeEntry();
						continue;
					}
					if ( !zipEntry.getName().endsWith( ".zip")) {
						zipInputStream.closeEntry();
						continue;
					}
					String[] words = Tool.split( zipEntry.getName(), '/');
					if ( null == words || 0 == words.length) {
						zipInputStream.closeEntry();
						continue;
					}
					zipInputStream.closeEntry();
					String id = words[ words.length - 1].substring( 0, words[ words.length - 1].length() - ".zip".length());
					propertyMap.put( id, new Property( file, "simulation", id, "", ""));
				}
			} finally {
				zipInputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param propertyMap
	 * @param file
	 * @return
	 */
	public static boolean append_simulation_properties(TreeMap<String, Property> propertyMap, File file) {
		File tempFile;
		try {
			tempFile = File.createTempFile( "simulation", ".xml");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		tempFile.deleteOnExit();

		if ( !write_simulation_properties( propertyMap, tempFile)) {
			tempFile.delete();
			return false;
		}

		Map<String, List<Entry>> entryMap = new HashMap<String, List<Entry>>();
		List<Entry> entryList = new ArrayList<Entry>();

		if ( !ZipUtility.find( file, CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._simulatorRootDirectoryName + "/")) {
			entryList.add( new Entry( CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._simulatorRootDirectoryName + "/", null));
			entryList.add( new Entry( CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._simulatorRootDirectoryName + "/" + CommonConstant._propertyFileName, tempFile));
			entryMap.put( CommonConstant._soarsRootDirectoryName + "/", entryList);
		} else {
			entryList.add( new Entry( CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._simulatorRootDirectoryName + "/" + CommonConstant._propertyFileName, tempFile));
			entryMap.put( CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._simulatorRootDirectoryName + "/", entryList);
		}

		/*file = */ZipUtility.append( file, entryMap);

		tempFile.delete();

		return true;
	}

	/**
	 * @param propertyMap
	 * @param tempFile
	 * @return
	 */
	public static boolean write_simulation_properties(TreeMap<String, Property> propertyMap, File tempFile) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( tempFile), "UTF-8");
			if ( null == outputStreamWriter)
				return false;

			Writer writer = new Writer( outputStreamWriter);

			outputStreamWriter.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

			writer.startElement( null, null, "simulation_properties", new AttributesImpl());

			Set<String> ids = propertyMap.keySet();
			for ( String id:ids) {
				Property property = propertyMap.get( id);
				if ( null == property)
					return false;

				if ( !property.write( writer))
					return false;
			}

			writer.endElement( null, null, "simulation_properties");

			outputStreamWriter.flush();
			outputStreamWriter.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (SAXException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param propertyMap
	 * @param file
	 * @return
	 */
	public static boolean get_animation_properties(TreeMap<String, Property> propertyMap, File file) {
		if ( !ZipUtility.find( file, CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._animatorRootDirectoryName + "/" + CommonConstant._propertyFileName)) {
			// file内のアニメーションデータからpropertyMapへデータを設定
			// 但し、シミュレーションデータだけは予めpropertyMapに設定されていなくてはならない
			// 従って、Animatorから呼ぶ時は、予めsoars/animation/property.xmlが存在していることを確認してから使用するべき！
			if ( !create_animation_properties( propertyMap, file))
				return false;

			// ここでxmlファイル(アニメーション)を作成してfileへ挿入
			if ( !append_animation_properties( propertyMap, file))
				return false;
		} else {
			// file内xmlファイル(アニメーション)のデータを_propertyMapへ設定
			if ( !AnimationSaxLoader.execute( file, propertyMap))
				return false;
		}
		return true;
	}

	/**
	 * @param propertyMap
	 * @param file
	 * @return
	 */
	private static boolean create_animation_properties(TreeMap<String, Property> propertyMap, File file) {
		Set<String> keys = propertyMap.keySet();
		for ( String key:keys) {
			if ( !get_animation_properties( propertyMap, key, file))
				return false;
		}
		return true;
	}

	/**
	 * @param propertyMap
	 * @param key
	 * @param file
	 * @return
	 */
	private static boolean get_animation_properties(TreeMap<String, Property> propertyMap, String key, File file) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream( new FileInputStream( file));
			ZipEntry zipEntry = null;
			try {
				while ( null != ( zipEntry = zipInputStream.getNextEntry())) {
					if ( zipEntry.isDirectory()) {
						zipInputStream.closeEntry();
						continue;
					}
					if ( !zipEntry.getName().startsWith( CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._animatorRootDirectoryName + "/" + key + "/")) {
						zipInputStream.closeEntry();
						continue;
					}
					if ( zipEntry.getName().equals( CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._simulatorRootDirectoryName + "/" + key + "/")) {
						zipInputStream.closeEntry();
						continue;
					}
					if ( !zipEntry.getName().endsWith( ".zip")) {
						zipInputStream.closeEntry();
						continue;
					}
					String[] words = Tool.split( zipEntry.getName(), '/');
					if ( null == words || 0 == words.length) {
						zipInputStream.closeEntry();
						continue;
					}
					zipInputStream.closeEntry();
					String id = words[ words.length - 1].substring( 0, words[ words.length - 1].length() - ".zip".length());
					propertyMap.get( key)._propertyMap.put( id, new Property( file, "animation", id, "", "", key));
				}
			} finally {
				zipInputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param propertyMap
	 * @param file
	 * @return
	 */
	public static boolean append_animation_properties( TreeMap<String, Property> propertyMap, File file) {
		File tempFile;
		try {
			tempFile = File.createTempFile( "animation", ".xml");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		tempFile.deleteOnExit();

		if ( !write_animation_properties( propertyMap, tempFile)) {
			tempFile.delete();
			return false;
		}

		Map<String, List<Entry>> entryMap = new HashMap<String, List<Entry>>();
		List<Entry> entryList = new ArrayList<Entry>();

		if ( !ZipUtility.find( file, CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._animatorRootDirectoryName + "/")) {
			entryList.add( new Entry( CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._animatorRootDirectoryName + "/", null));
			entryList.add( new Entry( CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._animatorRootDirectoryName + "/" + CommonConstant._propertyFileName, tempFile));
			entryMap.put( CommonConstant._soarsRootDirectoryName + "/", entryList);
		} else {
			entryList.add( new Entry( CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._animatorRootDirectoryName + "/" + CommonConstant._propertyFileName, tempFile));
			entryMap.put( CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._animatorRootDirectoryName + "/", entryList);
		}

		/*file = */ZipUtility.append( file, entryMap);

		tempFile.delete();

		return true;
	}

	/**
	 * @param propertyMap
	 * @param tempFile
	 * @return
	 */
	public static boolean write_animation_properties(TreeMap<String, Property> propertyMap, File tempFile) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( tempFile), "UTF-8");
			if ( null == outputStreamWriter)
				return false;

			Writer writer = new Writer( outputStreamWriter);

			outputStreamWriter.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

			writer.startElement( null, null, "animation_properties", new AttributesImpl());

			Set<String> ids = propertyMap.keySet();
			for ( String id:ids) {
				AttributesImpl attributesImpl = new AttributesImpl();
				attributesImpl.addAttribute( null, null, "id", "", Writer.escapeAttributeCharData( id));

				writer.startElement( null, null, "simulation", attributesImpl);

				Property property = propertyMap.get( id);
				if ( null == property)
					return false;

				if ( !property.write_animation_properties( writer))
					return false;

				writer.endElement( null, null, "simulation");
			}

			writer.endElement( null, null, "animation_properties");

			outputStreamWriter.flush();
			outputStreamWriter.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (SAXException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_animation_properties(Writer writer) throws SAXException {
		Set<String> ids = _propertyMap.keySet();
		for ( String id:ids) {
			Property property = _propertyMap.get( id);
			if ( null == property)
				return false;

			if ( !property.write( writer))
				return false;
		}
		return true;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "id", "", Writer.escapeAttributeCharData( _id));
		attributesImpl.addAttribute( null, null, "title", "", Writer.escapeAttributeCharData( _title));

		writer.startElement( null, null, "property", attributesImpl);

		writer.characters( _comment.toCharArray(), 0, _comment.length());

		writer.endElement( null, null, "property");

		return true;
	}

	/**
	 * @param data
	 * @param reverse
	 * @return
	 */
	public boolean get(List<Property[]> data, boolean reverse) {
		Property[] properties = new Property[ 2];
		properties[ 0] = this;
		properties[ 1] = null;
		data.add( properties);
		if ( _propertyMap.isEmpty())
			return true;

		Set<String> set = _propertyMap.keySet();
		String[] ids = set.toArray( new String[ 0]);
		int index = 0;
		if ( reverse) {
			for ( int i = ids.length - 1; i >= 0; --i) {
				if ( !get( ids[ i], index, properties, data))
					return false;

				++index;
			}
		} else {
			for ( String id:ids) {
				if ( !get( id, index, properties, data))
					return false;

				++index;
			}
		}

		return true;
	}

	/**
	 * @param id
	 * @param index
	 * @param properties
	 * @param data
	 * @return
	 */
	private boolean get(String id, int index, Property[] properties, List<Property[]> data) {
		Property property = _propertyMap.get( id);
		if ( null == property)
			return false;

		if ( 0 == index)
			properties[ 1] = property;
		else {
			properties = new Property[ 2];
			properties[ 0] = null;
			properties[ 1] = property;
			data.add( properties);
		}

		return true;
	}

	/**
	 * @param propertyMap
	 * @return
	 */
	public boolean update_properties(TreeMap<String, Property> propertyMap) {
		if ( _type.equals( "simulation"))
			return update_simulation_properties( propertyMap);
		else if ( _type.equals( "animation"))
			return update_animation_properties( propertyMap);
		return false;
	}

	/**
	 * @param propertyMap
	 * @return
	 */
	public boolean update_simulation_properties(TreeMap<String, Property> propertyMap) {
		if ( !ZipUtility.find( _file, CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._simulatorRootDirectoryName + "/" + CommonConstant._propertyFileName))
			return append_simulation_properties( propertyMap, _file);

		File tempFile = get_simulation_property_temporary_file( propertyMap);
		if ( null == tempFile)
			return false;

		tempFile.deleteOnExit();

		Map<String, File> fileMap = new HashMap<String, File>();
		fileMap.put( CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._simulatorRootDirectoryName + "/" + CommonConstant._propertyFileName, tempFile);
		/*_file = */ZipUtility.update( _file, fileMap);

		tempFile.delete();

		return true;
	}

	/**
	 * @param propertyMap
	 * @return
	 */
	public File get_simulation_property_temporary_file(TreeMap<String, Property> propertyMap) {
		File tempFile;
		try {
			tempFile = File.createTempFile( "simulation", ".xml");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		tempFile.deleteOnExit();

		if ( !write_simulation_properties( propertyMap, tempFile)) {
			tempFile.delete();
			return null;
		}

		return tempFile;
	}

	/**
	 * @param propertyMap
	 * @return
	 */
	public boolean update_animation_properties(TreeMap<String, Property> propertyMap) {
		// 但し、propertyMapにはシミュレーションデータも設定されていなくてはならない
		if ( !ZipUtility.find( _file, CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._animatorRootDirectoryName + "/" + CommonConstant._propertyFileName))
			return append_animation_properties( propertyMap, _file);

		File tempFile = get_animation_property_temporary_file( propertyMap);
		if ( null == tempFile)
			return false;

		tempFile.deleteOnExit();

		Map<String, File> fileMap = new HashMap<String, File>();
		fileMap.put( CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._animatorRootDirectoryName + "/" + CommonConstant._propertyFileName, tempFile);
		/*_file = */ZipUtility.update( _file, fileMap);

		tempFile.delete();

		return true;
	}

	/**
	 * @param propertyMap
	 * @return
	 */
	public File get_animation_property_temporary_file(TreeMap<String, Property> propertyMap) {
		File tempFile;
		try {
			tempFile = File.createTempFile( "animation", ".xml");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		tempFile.deleteOnExit();

		if ( !write_animation_properties( propertyMap, tempFile)) {
			tempFile.delete();
			return null;
		}

		return tempFile;
	}
}
