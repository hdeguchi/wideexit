/**
 * 
 */
package soars.application.visualshell.object.gis;

import java.awt.Component;
import java.awt.Point;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.object.keyword.KeywordObject;
import soars.application.visualshell.object.entity.base.object.map.MapInitialValue;
import soars.application.visualshell.object.entity.base.object.map.MapObject;
import soars.application.visualshell.object.entity.base.object.number.NumberObject;
import soars.application.visualshell.object.entity.base.object.spot.SpotVariableObject;
import soars.application.visualshell.object.entity.base.object.time.TimeVariableObject;
import soars.application.visualshell.object.entity.base.object.variable.VariableInitialValue;
import soars.application.visualshell.object.entity.base.object.variable.VariableObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.gis.edit.field.selector.Field;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.application.visualshell.object.gis.object.keyword.GisKeywordObject;
import soars.application.visualshell.object.gis.object.map.GisMapInitialValue;
import soars.application.visualshell.object.gis.object.map.GisMapObject;
import soars.application.visualshell.object.gis.object.number.GisNumberObject;
import soars.application.visualshell.object.gis.object.spot.GisSpotVariableObject;
import soars.application.visualshell.object.gis.object.time.GisTimeVariableObject;
import soars.application.visualshell.object.gis.object.variable.GisVariableInitialValue;
import soars.application.visualshell.object.gis.object.variable.GisVariableObject;

/**
 * @author kurata
 *
 */
public class GisDataRecord extends Vector<String> {

	/**
	 * 
	 */
	public GisDataRecord() {
		super();
	}

	/**
	 * @param line
	 * @return
	 */
	public boolean set(String line) {
		String[] words = line.split( "\t");
		if ( null == words || 2 > words.length)
			return false;

		for ( int i = 1; i < words.length; ++i)
			add( words[ i]);

		return true;
	}

	/**
	 * @param index
	 * @return
	 */
	public boolean can_use_as_spot_name(int index) {
		return !get( index).matches( ".*[" + Constant._prohibitedCharacters2 + "]+.*");
		//return ( !get( index).matches( "[0123456789\\.]+") && !get( index).matches( ".*[" + Constant._prohibitedCharacters2 + "]+.*"));
	}

	/**
	 * @param index
	 * @return
	 */
	public boolean can_use_as_keyword_initial_value(int index) {
		return !get( index).matches( ".*[" + Constant._prohibitedCharacters3 + "]+.*");
	}

	/**
	 * @param indices
	 * @param spotFields
	 * @param gisObjectBases
	 * @param component
	 * @return
	 */
	public boolean can_append_spots(int[] indices, List<Field> spotFields, List<GisObjectBase> gisObjectBases, Component component) {
		String name = make_name( indices, spotFields);
		if ( !Constant.is_correct_agent_or_spot_name( name)) {
			JOptionPane.showMessageDialog( component,
				ResourceManager.get_instance().get( "edit.gis.data.dialog.invalid.spot.name.error.message") + " - \"" + name + "\"",
				ResourceManager.get_instance().get( "edit.gis.data.dialog.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}
		for ( GisObjectBase gisObjectBase:gisObjectBases) {
			// TODO 仕様が決まっているのは現時点でキーワードと数値変数だけ
			if ( gisObjectBase instanceof GisKeywordObject) {
				GisKeywordObject gisKeywordObject = ( GisKeywordObject)gisObjectBase;
				String initialValue = make_name( gisKeywordObject._indices, gisKeywordObject._fields);
				if ( !Constant.is_correct_keyword_initial_value( initialValue)) {
					JOptionPane.showMessageDialog( component,
						ResourceManager.get_instance().get( "edit.gis.data.dialog.invalid.keyword.initial.value.error.message") + " - \"" + initialValue + "\"",
						ResourceManager.get_instance().get( "edit.gis.data.dialog.title"),
						JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @param indices
	 * @param spotFields
	 * @param gisObjectBases
	 * @param role
	 * @param gis
	 * @param range
	 * @param ratio
	 * @param component
	 * @return
	 */
	public boolean append_spots(int[] indices, List<Field> spotFields, List<GisObjectBase> gisObjectBases, String role, String gis, double[] range, double[] ratio, JComponent component) {
		String name = make_name( indices, spotFields);
		name = get_unique_name( name);
		SpotObject spotObject = LayerManager.get_instance().get_spot_has_this_name( name);
		if ( null != spotObject)
			spotObject._position = get_spot_position( range, ratio);
		else {
			spotObject = ( SpotObject)LayerManager.get_instance().append_object2( "spot", name, get_spot_position( range, ratio), component);
			if ( null == spotObject)
				return false;
		}

		if ( null != spotObject) {
			spotObject._initialRole = role;

			// TODO 仕様が決まらないと完全な実装は出来ない
			for ( GisObjectBase gisObjectBase:gisObjectBases) {
				if ( gisObjectBase instanceof GisKeywordObject) {
					GisKeywordObject gisKeywordObject = ( GisKeywordObject)gisObjectBase;
					spotObject.append_object( new KeywordObject( gisKeywordObject._name, make_name( gisKeywordObject._indices, gisKeywordObject._fields), gisKeywordObject._comment));
				} else if ( gisObjectBase instanceof GisNumberObject) {
					GisNumberObject gisNumberObject = ( GisNumberObject)gisObjectBase;
					spotObject.append_object( new NumberObject( gisNumberObject._name, gisNumberObject._type, make_name( gisNumberObject._indices, gisNumberObject._fields), gisNumberObject._comment));
				} else if ( gisObjectBase instanceof GisTimeVariableObject) {
					GisTimeVariableObject gisTimeVariableObject = ( GisTimeVariableObject)gisObjectBase;
					spotObject.append_object( new TimeVariableObject( gisTimeVariableObject._name, make_name( gisTimeVariableObject._indices, gisTimeVariableObject._fields), gisTimeVariableObject._comment));
				} else if ( gisObjectBase instanceof GisSpotVariableObject) {
					GisSpotVariableObject gisSpotVariableObject = ( GisSpotVariableObject)gisObjectBase;
					spotObject.append_object( new SpotVariableObject( gisSpotVariableObject._name, make_name( gisSpotVariableObject._indices, gisSpotVariableObject._fields), gisSpotVariableObject._comment));
				} else if ( gisObjectBase instanceof GisVariableObject) {
					GisVariableObject gisVariableObject = ( GisVariableObject)gisObjectBase;
					VariableObject variableObject = new VariableObject( gisVariableObject._kind, gisVariableObject._name, gisVariableObject._comment);
					for ( GisVariableInitialValue gisVariableInitialValue:gisVariableObject._gisVariableInitialValues) {
						// TODO 現時点ではFieldデータを追加しない
						if ( gisVariableInitialValue._type.equals( "field"))
							continue;

						variableObject._variableInitialValues.add( new VariableInitialValue( gisVariableInitialValue._type, gisVariableInitialValue._value));
					}
					spotObject.append_object( variableObject);
				} else if ( gisObjectBase instanceof GisMapObject) {
					GisMapObject gisMapObject = ( GisMapObject)gisObjectBase;
					MapObject mapObject = new MapObject( gisMapObject._name, gisMapObject._comment);
					for ( GisMapInitialValue gisMapInitialValue:gisMapObject._gisMapInitialValues) {
						// TODO 現時点ではFieldデータを追加しない
						if ( gisMapInitialValue._key[ 0].equals( "field") || gisMapInitialValue._value[ 0].equals( "field"))
							continue;

						mapObject._mapInitialValues.add( new MapInitialValue( gisMapInitialValue._key[ 0], gisMapInitialValue._key[ 1], gisMapInitialValue._value[ 0],  gisMapInitialValue._value[ 1]));
					}
					spotObject.append_object( mapObject);
				}
			}

			spotObject._gis = gis;

			// GISスポットに絶対座標を設定する
			spotObject._gisCoordinates[ 0] = get( size() - 2);
			spotObject._gisCoordinates[ 1] = get( size() - 1);
		}

		return true;
	}

	/**
	 * @param indices
	 * @param spotFields
	 * @return
	 */
	private String make_name(int[] indices, List<Field> spotFields) {
		String name = "";
		for ( int i = 0; i < indices.length; ++i)
			name += ( 0 > indices[ i]) ? spotFields.get( i)._value : get( indices[ i]);
		return name;
	}

	/**
	 * @param name
	 * @return
	 */
	private String get_unique_name(String name) {
		if ( ( null == LayerManager.get_instance().get_agent_has_this_name( name))
			/*&& ( null == LayerManager.get_instance().get_spot_has_this_name( name))*/)
			return name;

		int index = 2;
		while ( true) {
			String suffix = ( "(" + String.valueOf( index) + ")");
			if ( ( null == LayerManager.get_instance().get_agent_has_this_name( name + suffix))
				/*&& ( null == LayerManager.get_instance().get_spot_has_this_name( name + suffix))*/)
				return ( name + suffix);

			++index;
		}
	}

	/**
	 * @param range
	 * @param ratio
	 * @return
	 */
	private Point get_spot_position(double[] range, double[] ratio) {
		return new Point(
			( int)( ( Double.parseDouble( get( size() - 2)) - range[ 0]) * ratio[ 0]),
			( int)( ( range[ 3] - Double.parseDouble( get( size() - 1))) * ratio[ 1]));
	}

	/**
	 * @param row
	 * @param table
	 * @return
	 */
	public boolean create(int row, JTable table) {
		for ( int column = 0; column < table.getColumnCount(); ++column)
			add( ( String)table.getValueAt( row, column));
		return true;
	}

	/**
	 * @return
	 */
	public String get() {
		String text = "";

		for ( int i = 0; i < size(); ++i)
			text += ( ( ( 0 == i) ? "" : "\t") + get( i));

		return text;
	}

	/**
	 * 
	 */
	public void print() {
		String record = "record";
		for ( String element:this)
			record += ( "\t" + element);
		System.out.println( record);
	}
}
