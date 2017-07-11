/**
 * 
 */
package soars.application.visualshell.object.gis;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JTable;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.edit.field.selector.Field;
import soars.application.visualshell.object.gis.file.loader.FileData;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class GisData extends Vector<GisDataRecord> {

	/**
	 * 
	 */
	public String _name = "";

	/**
	 * 
	 */
	public String _role = "";

	/**
	 * 
	 */
	public List<String> _names = new ArrayList<String>();

	/**
	 * 
	 */
	public List<String> _types = new ArrayList<String>();

	/**
	 * 
	 */
	public List<Class> _classes = new ArrayList<Class>();

	/**
	 * @param name
	 */
	public GisData(String name) {
		super();
		_name = name;
	}

	/**
	 * @param gisData
	 */
	public GisData(GisData gisData) {
		super();
		_name = gisData._name;
		_role = gisData._role;
		_names.addAll( gisData._names);
		_types.addAll( gisData._types);
		_classes.addAll( gisData._classes);
	}

	/**
	 * @param fileData
	 */
	public GisData(FileData fileData) {
		super();
		_name = fileData._name;
		_role = fileData._role;
	}

	/**
	 * @param line
	 * @return
	 */
	public boolean set_name(String line) {
		String[] words = line.split( "\t");
		if ( null == words || 2 > words.length)
			return false;

		for ( int i = 1; i < words.length; ++i)
			_names.add( words[ i]);

		return true;
	}

	/**
	 * @param line
	 * @return
	 */
	public boolean set_type(String line) {
		String[] words = line.split( "\t");
		if ( null == words || 2 > words.length)
			return false;

		for ( int i = 1; i < words.length; ++i)
			_types.add( words[ i]);

		return true;
	}

	/**
	 * @param line
	 * @return
	 */
	public boolean set_class(String line) {
		String[] words = line.split( "\t");
		if ( null == words || 2 > words.length)
			return false;

		for ( int i = 1; i < words.length; ++i) {
			try {
				_classes.add( Class.forName( words[ i]));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	/**
	 * @param gisData
	 * @return
	 */
	public boolean compare(GisData gisData) {
		if ( _names.size() != gisData._names.size())
			return false;

		if ( _types.size() != gisData._types.size())
			return false;

		if ( _classes.size() != gisData._classes.size())
			return false;

		for ( int i = 0; i < _names.size(); ++i) {
			if ( !_names.get( i).equals( gisData._names.get( i)))
				return false;
		}

		for ( int i = 0; i < _types.size(); ++i) {
			if ( !_types.get( i).equals( gisData._types.get( i)))
				return false;
		}

		for ( int i = 0; i < _classes.size(); ++i) {
			if ( !_classes.get( i).equals( gisData._classes.get( i)))
				return false;
		}

		return true;
	}

	/**
	 * @param fields
	 * @return
	 */
	public List<String> get_available_spot_fields(List<String> fields) {
		List<String> availableFields = new ArrayList<String>();
		for ( String field:fields) {
			int index = _names.indexOf( field);
			if ( 0 > index)
				continue;

			boolean result = true;
			for ( GisDataRecord gisDataRecord:this) {
				if ( !gisDataRecord.can_use_as_spot_name( index)) {
					result = false;
					break;
				}
			}

			if ( !result)
				continue;

			availableFields.add( field);
		}
		return availableFields;
	}

	/**
	 * @param fields
	 * @return
	 */
	public List<String> get_available_keyword_fields(List<String> fields) {
		List<String> availableFields = new ArrayList<String>();
		for ( String field:fields) {
			int index = _names.indexOf( field);
			if ( 0 > index)
				continue;

			boolean result = true;
			for ( GisDataRecord gisDataRecord:this) {
				if ( !gisDataRecord.can_use_as_keyword_initial_value( index)) {
					result = false;
					break;
				}
			}

			if ( !result)
				continue;

			availableFields.add( field);
		}
		return availableFields;
	}

	/**
	 * @return
	 */
	public List<String> get_string_fields() {
		List<String> fields = new ArrayList<String>();
		for ( int i = 0; i < _types.size(); ++i) {
			if ( _types.get( i).equals( "C"))
				fields.add( _names.get( i));
		}
		return fields;
	}

	/**
	 * @param type
	 * @param fields
	 * @return
	 */
	public List<String> get_numeric_fields(String type, List<String> fields) {
		for ( int i = 0; i < _types.size(); ++i) {
			if ( !_types.get( i).equals( "N"))
				continue;

			if ( type.equals( ResourceManager.get_instance().get( "number.object.real.number"))
				|| ( type.equals( ResourceManager.get_instance().get( "number.object.integer"))
					&& ( _classes.get( i).equals( Integer.class) || _classes.get( i).equals( Long.class) || _classes.get( i).equals( Short.class) || _classes.get( i).equals( Byte.class))))
				fields.add( _names.get( i));
		}
		return fields;
	}

	/**
	 * @param fields
	 * @return
	 */
	public List<String> get_time_fields(List<String> fields) {
		// TODO 仕様が決まっていないので現時点では全てのフィールドを選択可能としている
		for ( int i = 0; i < _names.size(); ++i) {
			fields.add( _names.get( i));
		}
		return fields;
	}

	/**
	 * @param indices
	 * @param spotFields
	 * @param gisObjectBases
	 * @param component
	 * @return
	 */
	public boolean can_append_spots(int[] indices, List<Field> spotFields, List<GisObjectBase> gisObjectBases, Component component) {
		for ( GisDataRecord gisDataRecord:this) {
			if ( !gisDataRecord.can_append_spots( indices, spotFields, gisObjectBases, component))
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

		boolean first = true;
		for ( int i = 0; i < size(); ++i) {
			if ( 0 == i) {
				range[ 0] = range[ 2] = Double.parseDouble( get( i).get( get( i).size() - 2));
				range[ 1] = range[ 3] = Double.parseDouble( get( i).get( get( i).size() - 1));
			} else {
				range[ 0] = Math.min( range[ 0], Double.parseDouble( get( i).get( get( i).size() - 2)));
				range[ 1] = Math.min( range[ 1], Double.parseDouble( get( i).get( get( i).size() - 1)));
				range[ 2] = Math.max( range[ 2], Double.parseDouble( get( i).get( get( i).size() - 2)));
				range[ 3] = Math.max( range[ 3], Double.parseDouble( get( i).get( get( i).size() - 1)));
			}
		}

		return range;
	}

	/**
	 * @param indices
	 * @param spotFields
	 * @param gisObjectBases
	 * @param gis
	 * @param range
	 * @param ratio
	 * @param component
	 * @return
	 */
	public boolean append_spots(int[] indices, List<Field> spotFields, List<GisObjectBase> gisObjectBases, String gis, double[] range, double[] ratio, JComponent component) {
		for ( GisDataRecord gisDataRecord:this) {
			if ( !gisDataRecord.append_spots( indices, spotFields, gisObjectBases, _role, gis, range, ratio, component))
				return false;
		}
		return true;
	}

	/**
	 * @param table
	 * @return
	 */
	public boolean cerate(JTable table) {
		if ( _names.size() != table.getColumnCount() - 2)
			return false;

		for ( int row = 0; row < table.getRowCount(); ++row) {
			GisDataRecord gisDataRecord = new GisDataRecord();
			if ( !gisDataRecord.create( row, table))
				return false;

			add( gisDataRecord);
		}

		return true;
	}

	/**
	 * @param file
	 * @return
	 */
	public boolean create(File file) {
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader( new InputStreamReader( new FileInputStream( file), "UTF8"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}

		String state = "name";
		while ( true) {
			String line = null;
			try {
				line = bufferedReader.readLine();
			} catch (IOException e1) {
				try {
					bufferedReader.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				e1.printStackTrace();
				return false;
			}

			if ( null == line)
				break;

			if ( line.equals( ""))
				continue;

			if ( state.equals( "name")) {
				if ( !read_name( line)) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					return false;
				}
				state = "type";
			} else if ( state.equals( "type")) {
				if ( !read_type( line)) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					return false;
				}
				state = "class";
			} else if ( state.equals( "class")) {
				if ( !read_class( line)) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					return false;
				}
				state = "";
			} else {
				if ( !read( line)) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * @param line
	 * @return
	 */
	private boolean read_name(String line) {
		String[] words = Tool.split( line, '\t');
		for ( String word:words)
			_names.add( word);
		return true;
	}

	/**
	 * @param line
	 * @return
	 */
	private boolean read_type(String line) {
		String[] words = Tool.split( line, '\t');
		for ( String word:words)
			_types.add( word);
		return true;
	}

	/**
	 * @param line
	 * @return
	 */
	private boolean read_class(String line) {
		String[] words = Tool.split( line, '\t');
		for ( String word:words) {
			try {
				_classes.add( Class.forName( word));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/**
	 * @param line
	 * @return
	 */
	private boolean read(String line) {
		GisDataRecord gisDataRecord = new GisDataRecord();
		String[] words = Tool.split( line, '\t');
		for ( String word:words)
			gisDataRecord.add( word);

		add( gisDataRecord);

		return true;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
		attributesImpl.addAttribute( null, null, "role", "", Writer.escapeAttributeCharData( _role));
		writer.writeElement( null, null, "file", attributesImpl);
		return true;
	}

	/**
	 * @param file
	 * @return
	 */
	public boolean write(File file) {
		String text = "";

		for ( int i = 0; i < _names.size(); ++i)
			text += ( ( ( 0 == i) ? "" : "\t") + _names.get( i));

		text += "\n";

		for ( int i = 0; i < _types.size(); ++i)
			text += ( ( ( 0 == i) ? "" : "\t") + _types.get( i));

		text += "\n";

		for ( int i = 0; i < _classes.size(); ++i)
			text += ( ( ( 0 == i) ? "" : "\t") + _classes.get( i).getName());

		text += "\n";

		for ( GisDataRecord gisDataRecord:this)
			text += ( gisDataRecord.get() + "\n");

		return FileUtility.write_text_to_file( file, text, "UTF8");
	}

	/**
	 * 
	 */
	public void print() {
		String names = "name";
		for ( String name:_names)
			names += ( "\t" + name);
		System.out.println( names);

		String types = "type";
		for ( String type:_types)
			types += ( "\t" + type);
		System.out.println( types);

		String classes = "class";
		for ( Class cls:_classes)
			classes += ( "\t" + cls.getName());
		System.out.println( classes);

		for ( GisDataRecord gisDataRecord:this)
			gisDataRecord.print();
	}
}
