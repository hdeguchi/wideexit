/*
 * Created on 2005/11/25
 */
package soars.application.visualshell.file.importer.initial.entity;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.file.importer.initial.InitialDataImporter;
import soars.application.visualshell.file.importer.initial.base.DataBase;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.object.base.ObjectBase;
import soars.application.visualshell.object.entity.base.object.class_variable.ClassVariableObject;
import soars.application.visualshell.object.entity.base.object.exchange_algebra.ExchangeAlgebraInitialValue;
import soars.application.visualshell.object.entity.base.object.exchange_algebra.ExchangeAlgebraObject;
import soars.application.visualshell.object.entity.base.object.file.FileObject;
import soars.application.visualshell.object.entity.base.object.keyword.KeywordObject;
import soars.application.visualshell.object.entity.base.object.map.MapInitialValue;
import soars.application.visualshell.object.entity.base.object.map.MapObject;
import soars.application.visualshell.object.entity.base.object.number.NumberObject;
import soars.application.visualshell.object.entity.base.object.probability.ProbabilityObject;
import soars.application.visualshell.object.entity.base.object.role.RoleVariableObject;
import soars.application.visualshell.object.entity.base.object.spot.SpotVariableObject;
import soars.application.visualshell.object.entity.base.object.time.TimeVariableObject;
import soars.application.visualshell.object.entity.base.object.variable.VariableInitialValue;
import soars.application.visualshell.object.entity.base.object.variable.VariableObject;
import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.file.FileUtility;

/**
 * The class for the initial data of the agent or the spot.
 * @author kurata / SOARS project
 */
public class EntityData extends DataBase {

	/**
	 * The number of this object.
	 */
	public String _number = "";

	/**
	 * Name of the Initial role.
	 */
	public String _initialRole = "";

	/**
	 * Object map hashtable.
	 */
	public Map<String, TreeMap<String, Object>> _objectMapMap = new HashMap<String, TreeMap<String, Object>>();

	/**
	 * True if this object has multi initial data.
	 */
	public boolean _multi = false;

	/**
	 * Creates this object with the specified data. 
	 * @param type the type of this object
	 * @param name the name of this object
	 */
	public EntityData(String type, String name) {
		super(type, name);
		initialize();
	}

	/**
	 * Creates this object with the specified data. 
	 * @param type the type of this object
	 * @param name the name of this object
	 * @param position the position of this object
	 */
	public EntityData(String type, String name, Point position) {
		super(type, name, position);
		initialize();
	}

	/**
	 * 
	 */
	private void initialize() {
		for ( String kind:Constant._kinds)
			_objectMapMap.put( kind, new TreeMap<String, Object>());
	}

	/**
	 * Returns true if this object has multi initial data.
	 * @return true if this object has multi initial data
	 */
	public boolean is_multi() {
		return _multi;
	}

	/**
	 * Sets the specified number to this object.
	 * @param max the specified number
	 */
	public void set_number(int max) {
		if ( _number.equals( ""))
			_number = String.valueOf( max);
		else {
			int number = Integer.parseInt( _number);
			if ( number < max)
				_number = String.valueOf( max);
		}
	}

	/**
	 * Returns true if this object contains the agent or spot which has the specified name.
	 * @param name the specified name
	 * @return true if this object contains the agent or spot which has the specified name
	 */
	public boolean contains(String name) {
		return SoarsCommonTool.has_same_name( _name, _number, name);
	}

	/**
	 * Returns true if this object contains the agent or spot which has the specified name and number.
	 * @param name the specified name
	 * @param number the specified number
	 * @return true if this object contains the agent or spot which has the specified name and number
	 */
	public boolean contains(String name, String number) {
		return SoarsCommonTool.has_same_name( _name, _number, name, number);
	}

	/**
	 * @param name
	 * @param number
	 * @return
	 */
	public boolean contains_as_object_name(String name, String number) {
		// TODO Auto-generated method stub
		for ( String kind:Constant._kinds) {
			if ( contains( _objectMapMap.get( kind), name, number))
				return true;
		}
		return false;
	}

	/**
	 * @param treeMap
	 * @param name
	 * @param number
	 * @return
	 */
	private boolean contains(TreeMap<String, Object> treeMap, String name, String number) {
		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			if ( !is_multi()) {
				ObjectBase objectBase = ( ObjectBase)entry.getValue();
				if ( SoarsCommonTool.has_same_name( name, number, objectBase._name))
					return true;
			} else {
				if ( contains( ( HashMap<ObjectBase, Vector<int[]>>)entry.getValue(), name, number))
					return true;
			}
		}
		return false;
	}

	/**
	 * @param indicesMap
	 * @param name
	 * @param number
	 * @return
	 */
	private boolean contains(HashMap<ObjectBase, Vector<int[]>> indicesMap, String name, String number) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			if ( SoarsCommonTool.has_same_name( name, number, objectBase._name))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if this object contains the object which has the specified name.
	 * @param kind the kind of the object
	 * @param name the specified object name
	 * @return true if this object contains the object which has the specified name
	 */
	public boolean has_same_object_name(String kind, String name) {
		TreeMap<String, Object> treeMap = _objectMapMap.get( kind);
		if ( null == treeMap)
			return false;

		return treeMap.containsKey( name);
	}

	/**
	 * Returns true if the specified name is correct as the number variable.
	 * @param name the specified name
	 * @param type the type of the number varibale
	 * @return true if the specified name is correct as the number variable
	 */
	public boolean is_number_object_correct(String name, String type) {
		return EntityBase.is_number_object_correct( _type, _name, name, type, _objectMapMap.get( "number object"), is_multi());
	}

	/**
	 * Returns true for appending the collection variable to this object successfully.
	 * @param name the collection variable name
	 * @param words the array of words extracted from the line
	 * @param number the line number
	 * @param warning whether to display the warning messages
	 * @return true for appending the collection variable to this object successfully
	 */
	public boolean append_collection(String name, String[] words, int number, boolean warning) {
		return append( words, _objectMapMap.get( "collection"), new VariableObject( "collection", name), number, warning);
	}

	/**
	 * Returns true for appending the list variable to this object successfully.
	 * @param name the list variable name
	 * @param words the array of words extracted from the line
	 * @param number the line number
	 * @param warning whether to display the warning messages
	 * @return true for appending the list variable to this object successfully
	 */
	public boolean append_list(String name, String[] words, int number, boolean warning) {
		return append( words, _objectMapMap.get( "list"), new VariableObject( "list", name), number, warning);
	}

	/**
	 * @param words
	 * @param treeMap
	 * @param variableObject
	 * @param number
	 * @param warning
	 * @return
	 */
	private boolean append(String[] words, TreeMap<String, Object> treeMap, VariableObject variableObject, int number, boolean warning) {
		if ( 0 != words.length % 2) {
			InitialDataImporter.on_invalid_value_error( number, warning);
			return false;
		}

		for ( int i = 4; i < words.length; i += 2) {
			if ( !is_valid_variable_element( variableObject._kind, variableObject._name, words[ i], words[ i + 1], this, number, warning))
				return false;

			variableObject._variableInitialValues.add( new VariableInitialValue( words[ i], words[ i + 1]));
		}

		treeMap.put( variableObject._name, variableObject);

		return true;
	}

	/**
	 * Returns true for appending the map variable to this object successfully.
	 * @param name the map variable name
	 * @param words the array of words extracted from the line
	 * @param number the line number
	 * @param warning whether to display the warning messages
	 * @return true for appending the map variable to this object successfully
	 */
	public boolean append_map(String name, String[] words, int number, boolean warning) {
		if ( 0 != words.length % 4) {
			InitialDataImporter.on_invalid_value_error( number, warning);
			return false;
		}

		MapObject mapObject = new MapObject( name);
		for ( int i = 4; i < words.length; i += 4) {
			if ( !words[ i].equals( "immediate") && !words[ i].equals( "keyword")) {
				InitialDataImporter.on_invalid_value_error( number, warning);
				return false;
			}
			if ( words[ i].equals( "immediate")) {
				if ( !words[ i + 1].equals( "") && !is_valid_value3( words[ i + 1], number, warning))
					return false;
			} else {
				if ( !is_valid_name( words[ i + 1], Constant._prohibitedCharacters1, number, warning))
					return false;
			}

			if ( !is_valid_variable_element( "map", name, words[ i + 2], words[ i + 3], this, number, warning))
				return false;

			mapObject._mapInitialValues.add( new MapInitialValue( words[ i], words[ i + 1], words[ i + 2], words[ i + 3]));
		}

		_objectMapMap.get( "map").put( name, mapObject);

		return true;
	}

	/**
	 * Returns true for appending the claass variable to this object successfully.
	 * @param name the claass variable name
	 * @param jarFilename the jar file name
	 * @param classname the class name
	 * @param number the line number
	 * @return true for appending the claass variable to this object successfully
	 */
	public boolean append_class_variable(String name, String jarFilename, String classname, int number) {
		ClassVariableObject classVariableObject = new ClassVariableObject( name, jarFilename, classname);
		_objectMapMap.get( "class variable").put( name, classVariableObject);
		return true;
	}

	/**
	 * Returns true for appending the exchange algebra variable to this object successfully.
	 * @param name the exchange algebra variable name
	 * @param words the array of words extracted from the line
	 * @param number the line number
	 * @param warning whether to display the warning messages
	 * @return true for appending the exchange algebra variable to this object successfully
	 */
	public boolean append_exchange_algebra(String name, String[] words, int number, boolean warning) {
		ExchangeAlgebraObject exchangeAlgebraObject = new ExchangeAlgebraObject( name);
		for ( int i = 4; i < words.length; ++i) {
			String[] elements = Tool.split( words[ i], '-');
			if ( !is_correct_exchange_algebra_initial_value( elements, number, warning))
				return false;

			if ( 2 == elements.length)
				exchangeAlgebraObject._exchangeAlgebraInitialValues.add( new ExchangeAlgebraInitialValue( elements[ 0], elements[ 1]));
			else
				exchangeAlgebraObject._exchangeAlgebraInitialValues.add( new ExchangeAlgebraInitialValue(
					elements[ 0], elements[ 1], elements[ 2],
					( ( 4 <= elements.length) ? elements[ 3] : ""),
					( ( 5 <= elements.length) ? elements[ 4] : ""),
					( ( 6 <= elements.length) ? elements[ 5] : "")));
		}

		_objectMapMap.get( "exchange algebra").put( name, exchangeAlgebraObject);
		return true;
	}

	/**
	 * @param elements
	 * @param number
	 * @param warning
	 * @return
	 */
	private boolean is_correct_exchange_algebra_initial_value(String[] elements, int number, boolean warning) {
		if ( 2 > elements.length || 6 < elements.length) {
			InitialDataImporter.on_invalid_value_error( number, warning);
			return false;
		}


		for ( int i = 0; i < elements.length; ++i) {
			if ( null == elements[ i]) {
				InitialDataImporter.on_invalid_value_error( number, warning);
				return false;
			}
		}


		// value
		if ( elements[ 0].equals( "")
			|| elements[ 0].equals( "$") || 0 < elements[ 0].indexOf( '$')) {
			InitialDataImporter.on_invalid_value_error( number, warning);
			return false;
		}

		if ( elements[ 0].startsWith( "$") && 0 < elements[ 0].indexOf( "$", 1)) {
			InitialDataImporter.on_invalid_value_error( number, warning);
			return false;
		}

		elements[ 0] = ExchangeAlgebraInitialValue.is_correct( elements[ 0]);
		if ( null == elements[ 0]) {
			InitialDataImporter.on_invalid_value_error( number, warning);
			return false;
		}


		if ( 2 == elements.length) {
			// keyword
			if ( elements[ 1].equals( "")) {
				InitialDataImporter.on_invalid_value_error( number, warning);
				return false;
			}

			if ( !is_valid_name( elements[ 1], Constant._prohibitedCharacters1, number, warning)) {
				return false;
			}
		} else {
			// name
			if ( elements[ 1].equals( "")) {
				InitialDataImporter.on_invalid_value_error( number, warning);
				return false;
			}

			if ( !is_valid_name( elements[ 1], Constant._prohibitedCharacters11, number, warning)) {
				return false;
			}


			// hat
			if ( !elements[ 2].equals( "NO_HAT") && !elements[ 2].equals( "HAT")) {
				InitialDataImporter.on_invalid_value_error( number, warning);
				return false;
			}


			// other
			for ( int i = 3; i < elements.length; ++i) {
				if ( !is_valid_name( elements[ i], Constant._prohibitedCharacters11, number, warning))
					return false;
			}
		}

		return true;
	}

	/**
	 * Returns true for appending the probability variable to this object successfully.
	 * @param words the array of words extracted from the line
	 * @param indices the integer array
	 * @param number the line number
	 * @param warning whether to display the warning messages
	 * @return true for appending the probability variable to this object successfully
	 */
	public boolean append_probability(String[] words, Vector<int[]> indices, int number, boolean warning) {
		if ( 6 < words.length
			&& ( !is_valid_value5( words[ 6], number, warning)
			|| !CommonTool.is_probability_correct( words[ 6]))) {
			InitialDataImporter.on_invalid_value_error( number, warning);
			return false;
		}

		_multi = true;
		append_object( _objectMapMap.get( "probability"), new ProbabilityObject( words[ 5], ( ( 6 < words.length) ? words[ 6] : "")), indices);
		return true;
	}

	/**
	 * Returns true for appending the collection variable to this object successfully.
	 * @param words the array of words extracted from the line
	 * @param indices the integer array
	 * @param number the line number
	 * @param warning whether to display the warning messages
	 * @return true for appending the collection variable to this object successfully
	 */
	public boolean append_collection(String[] words, Vector<int[]> indices, int number, boolean warning) {
		_multi = true;
		return append_variable( words, _objectMapMap.get( "collection"), indices, new VariableObject( "collection", words[ 5]), number, warning);
	}

	/**
	 * Returns true for appending the list variable to this object successfully.
	 * @param words the array of words extracted from the line
	 * @param indices the integer array
	 * @param number the line number
	 * @param warning whether to display the warning messages
	 * @return true for appending the list variable to this object successfully
	 */
	public boolean append_list(String[] words, Vector<int[]> indices, int number, boolean warning) {
		_multi = true;
		return append_variable( words, _objectMapMap.get( "list"), indices, new VariableObject( "list", words[ 5]), number, warning);
	}

	/**
	 * @param words
	 * @param treeMap
	 * @param indices
	 * @param variableObject
	 * @param number
	 * @param warning
	 * @return
	 */
	private boolean append_variable(String[] words, TreeMap<String, Object> treeMap, Vector<int[]> indices, VariableObject variableObject, int number, boolean warning) {
		if ( 0 != words.length % 2) {
			InitialDataImporter.on_invalid_value_error( number, warning);
			return false;
		}

		for ( int i = 6; i < words.length; i += 2) {
			if ( !is_valid_variable_element( variableObject._kind, variableObject._name, words[ i], words[ i + 1], this, number, warning))
				return false;

			variableObject._variableInitialValues.add( new VariableInitialValue( words[ i], words[ i + 1]));
		}

		append_object( treeMap, variableObject, indices);

		return true;
	}

	/**
	 * Returns true for appending the map variable to this object successfully.
	 * @param words the array of words extracted from the line
	 * @param indices the integer array
	 * @param number the line number
	 * @param warning whether to display the warning messages
	 * @return true for appending the map variable to this object successfully
	 */
	public boolean append_map(String[] words, Vector<int[]> indices, int number, boolean warning) {
		_multi = true;

		if ( 0 != ( words.length - 6 )% 4) {
			InitialDataImporter.on_invalid_value_error( number, warning);
			return false;
		}

		MapObject mapObject = new MapObject( words[ 5]);

		for ( int i = 6; i < words.length; i += 4) {
			if ( !words[ i].equals( "immediate") && !words[ i].equals( "keyword")) {
				InitialDataImporter.on_invalid_value_error( number, warning);
				return false;
			}
			if ( words[ i].equals( "immediate")) {
				if ( !words[ i + 1].equals( "") && !is_valid_value3( words[ i + 1], number, warning))
					return false;
			} else {
				if ( !is_valid_name( words[ i + 1], Constant._prohibitedCharacters1, number, warning))
					return false;
			}

			if ( !is_valid_variable_element( "map", words[ 5], words[ i + 2], words[ i + 3], this, number, warning))
				return false;

			mapObject._mapInitialValues.add( new MapInitialValue( words[ i], words[ i + 1], words[ i + 2], words[ i + 3]));
		}

		append_object( _objectMapMap.get( "map"), mapObject, indices);

		return true;
	}

	/**
	 * Returns true for appending the keyword to this object successfully.
	 * @param words the array of words extracted from the line
	 * @param indices the integer array
	 * @param number the line number
	 * @param warning whether to display the warning messages
	 * @return true for appending the keyword to this object successfully
	 */
	public boolean append_keyword(String[] words, Vector<int[]> indices, int number, boolean warning) {
		if ( 6 < words.length && !is_valid_value3( words[ 6], number, warning))
			return false;

		_multi = true;
		append_object( _objectMapMap.get( "keyword"), new KeywordObject( words[ 5], ( ( 6 < words.length) ? words[ 6] : "")), indices);
		return true;
	}

	/**
	 * Appends the number variable to this object.
	 * @param name the number variable name
	 * @param type the number variable type
	 * @param value the initial value for the number variable
	 * @param indices the integer array
	 */
	public void append_number_object(String name, String type, String value, Vector<int[]> indices) {
		_multi = true;
		append_object( _objectMapMap.get( "number object"), new NumberObject( name, type, value), indices);
	}

	/**
	 * Returns true for appending the role variable to this object successfully.
	 * @param words the array of words extracted from the line
	 * @param indices the integer array
	 * @param number the line number
	 * @param warning whether to display the warning messages
	 * @return true for appending the role variable to this object successfully
	 */
	public boolean append_role_variable(String[] words, Vector<int[]> indices, int number, boolean warning) {
		if ( 6 < words.length && !is_valid_value6( words[ 6], number, warning))
			return false;

		_multi = true;
		append_object( _objectMapMap.get( "role variable"), new RoleVariableObject( words[ 5], ( ( 6 < words.length) ? words[ 6] : "")), indices);
		return true;
	}

	/**
	 * Returns true for appending the time variable to this object successfully.
	 * @param words the array of words extracted from the line
	 * @param indices the integer array
	 * @param number the line number
	 * @param warning whether to display the warning messages
	 * @return true for appending the time variable to this object successfully
	 */
	public boolean append_time_variable(String[] words, Vector<int[]> indices, int number, boolean warning) {
		if ( 6 < words.length) {
			String value = transform_time_variable_initial_value( words[ 6], number, warning);
			if ( null == value)
				return false;

			words[ 6] = value;
		}

		_multi = true;
		append_object( _objectMapMap.get( "time variable"), new TimeVariableObject( words[ 5], ( ( 6 < words.length) ? words[ 6] : "0:00")), indices);
		return true;
	}

	/**
	 * Returns the transformed string.
	 * @param value the specified value of the object
	 * @param number the line number
	 * @param warning whether to display the warning messages
	 * @return the transformed string
	 */
	public static String transform_time_variable_initial_value(String value, int number, boolean warning) {
		if ( value.equals( "$") || 0 < value.indexOf( '$'))
			return null;

		if ( value.startsWith( "$")) {
			if ( !is_valid_value( value, Constant._prohibitedCharacters14, number, warning)
				|| value.equals( "$")
				|| value.equals( "$Name")
				|| value.equals( "$Role")
				|| value.equals( "$Spot")
				|| 0 <= value.indexOf( Constant._experimentName)
				|| 0 <= value.indexOf( Constant._currentTimeName))
				return null;

			if ( 0 < value.indexOf( "$", 1)
				|| 0 < value.indexOf( ")", 1))
				return null;

			return value;
		}

		String[] words = value.split( "/");
		if ( null == words || 0 == words.length || 2 < words.length) {
			InitialDataImporter.on_invalid_value_error( number, warning);
			return null;
		} else if ( 1 == words.length) {
			String newValue = transform_time_variable_initial_value( words[ 0]);
			if ( null == newValue) {
				InitialDataImporter.on_invalid_value_error( number, warning);
				return null;
			}
			return newValue;
		} else {
			int day;
			try {
				day = Integer.parseInt( words[ 0]);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				InitialDataImporter.on_invalid_value_error( number, warning);
				return null;
			}
			String newValue = transform_time_variable_initial_value( words[ 1]);
			if ( null == newValue) {
				InitialDataImporter.on_invalid_value_error( number, warning);
				return null;
			}
			return String.valueOf( day) + "/" + newValue;
		}
	}

	/**
	 * @param value
	 * @return
	 */
	private static String transform_time_variable_initial_value(String value) {
		String[] words = value.split( ":");
		if ( null == words || 2 != words.length)
			return null;

		int hour;
		try {
			hour = Integer.parseInt( words[ 0]);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return null;
		}

		if ( 0 > hour || 23 < hour)
			return null;

		int minute;
		try {
			minute = Integer.parseInt( words[ 1]);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return null;
		}

		if ( 0 > minute || 59 < minute)
			return null;

		return String.format( "%d:%02d", hour, minute);
	}

	/**
	 * @param words
	 * @param indices
	 * @param number
	 * @param warning
	 * @return
	 */
	public boolean append_spot_variable(String[] words, Vector<int[]> indices, int number, boolean warning) {
		if ( 6 < words.length && !is_valid_value1( words[ 6], number, warning))
			return false;

		_multi = true;
		append_object( _objectMapMap.get( "spot variable"), new SpotVariableObject( words[ 5], ( ( 6 < words.length) ? words[ 6] : "")), indices);
		return true;
	}

	/**
	 * Returns true for appending the claass variable to this object successfully.
	 * @param name the claass variable name
	 * @param jarFilename the jar file name
	 * @param classname the class name
	 * @param indices the integer array
	 * @return true for appending the claass variable to this object successfully
	 */
	public boolean append_class_variable(String name, String jarFilename, String classname, Vector<int[]> indices) {
		_multi = true;
		append_object( _objectMapMap.get( "class variable"), new ClassVariableObject( name, jarFilename, classname), indices);
		return true;
	}

	/**
	 * @param words
	 * @param indices
	 * @param number
	 * @param warning
	 * @return
	 */
	public boolean append_file(String[] words, Vector<int[]> indices, int number, boolean warning) {
		if ( 6 < words.length) {
			words[ 6] = normalize_file_path( words[ 6]);
			if ( !words[ 6].equals( "")
				&& ( !is_valid_value17( words[ 6], number, warning) || !is_valid_file( words[ 6], number, warning)))
				return false;
		}

		_multi = true;
		append_object( _objectMapMap.get( "file"), new FileObject( words[ 5], ( ( 6 < words.length) ? words[ 6] : "")), indices);
		return true;
	}

	/**
	 * Returns true for appending the exchange algebra to this object successfully.
	 * @param name the exchange algebra name
	 * @param words the array of words extracted from the line
	 * @param indices the integer array
	 * @param number the line number
	 * @param warning whether to display the warning messages
	 * @return true for appending the exchange algebra to this object successfully
	 */
	public boolean append_exchange_algebra(String name, String[] words, Vector<int[]> indices, int number, boolean warning) {
		_multi = true;
		ExchangeAlgebraObject exchangeAlgebraObject = new ExchangeAlgebraObject( name);
		for ( int i = 6; i < words.length; ++i) {
			String[] elements = Tool.split( words[ i], '-');
			if ( !is_correct_exchange_algebra_initial_value( elements, number, warning))
				return false;

			if ( 2 == elements.length)
				exchangeAlgebraObject._exchangeAlgebraInitialValues.add( new ExchangeAlgebraInitialValue( elements[ 0], elements[ 1]));
			else
				exchangeAlgebraObject._exchangeAlgebraInitialValues.add( new ExchangeAlgebraInitialValue(
					elements[ 0], elements[ 1], elements[ 2],
					( ( 4 <= elements.length) ? elements[ 3] : ""),
					( ( 5 <= elements.length) ? elements[ 4] : ""),
					( ( 6 <= elements.length) ? elements[ 5] : "")));
		}

		append_object( _objectMapMap.get( "exchange algebra"), exchangeAlgebraObject, indices);
		return true;
	}

	/**
	 * Appends the probability variable to this object.
	 * @param treeMap the object hashtable
	 * @param objectBase the specified object
	 * @param indices the integer array
	 */
	public static void append_object(TreeMap<String, Object> treeMap, ObjectBase objectBase, Vector<int[]> indices) {
		Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)treeMap.get( objectBase._name);
		if ( null == indicesMap) {
			indicesMap = new HashMap<ObjectBase, Vector<int[]>>();
			treeMap.put( objectBase._name, indicesMap);
		}

		Vector<int[]> vector = objectBase.get_indices( indicesMap);
		if ( null == vector) {
			vector = new Vector<int[]>();
			indicesMap.put( objectBase, vector);
		}

		arrange( indicesMap, indices, vector);
	}

	/**
	 * Arranges the speified integer array hashtable.
	 * @param indicesMap the speified integer array hashtable
	 * @param indices the source integer array
	 * @param vector the destination integer array
	 */
	public static void arrange(Map<ObjectBase, Vector<int[]>> indicesMap, Vector<int[]> indices, Vector<int[]> vector) {
		CommonTool.append_indices( indices, vector);

		trim( indicesMap, vector);

		while ( remove_empty( indicesMap))
			;

		sort( indicesMap);
	}

	/**
	 * @param indicesMap
	 * @param indices
	 */
	private static void trim(Map<ObjectBase, Vector<int[]>> indicesMap, Vector<int[]> indices) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Vector<int[]> vector = ( Vector<int[]>)entry.getValue();
			if ( vector.equals( indices))
				continue;

			CommonTool.trim_indices( indices, vector);
		}
	}

	/**
	 * @param indicesMap
	 * @return
	 */
	private static boolean remove_empty(Map<ObjectBase, Vector<int[]>> indicesMap) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Object key = entry.getKey();
			Vector<int[]> vector = ( Vector<int[]>)entry.getValue();
			if ( vector.isEmpty()) {
				indicesMap.remove( key);
				return true;
			}
		}
		return false;
	}

	/**
	 * @param indicesMap
	 */
	private static void sort(Map<ObjectBase, Vector<int[]>> indicesMap) {
		Vector<ObjectBase> objectBases = new Vector<ObjectBase>( indicesMap.keySet());
		for ( ObjectBase objectBase:objectBases) {
			//String value = ( String)values.get( i);
			//Object value = values.get( i);
			Vector<int[]> indices = ( Vector<int[]>)indicesMap.get( objectBase);
			indicesMap.put( objectBase, CommonTool.sort_indices( indices));
		}
	}

	/**
	 * Returns true if the specified name of the object is valid.
	 * @param name the specified name of the object
	 * @param prohibitedCharacters set of the prohibited characters
	 * @param number the line number
	 * @param warning whether to display the warning messages
	 * @return true if the specified name of the object is valid
	 */
	public static boolean is_valid_name(String name, String prohibitedCharacters, int number, boolean warning) {
		if ( 0 <= name.indexOf( '$')) {
			InitialDataImporter.on_invalid_value_error( number, warning);
			return false;
		}

		return is_valid_value( name, prohibitedCharacters, number, warning);
	}

	/**
	 * Returns true if the specified value of the object is valid.
	 * @param value the specified value of the object
	 * @param number the line number
	 * @param warning whether to display the warning messages
	 * @return true if the specified value of the object is valid
	 */
	public static boolean is_valid_value1(String value, int number, boolean warning) {
		if ( value.equals( "$") || 0 < value.indexOf( '$'))
			return false;

		if ( value.startsWith( "$") && 0 < value.indexOf( "$", 1))
			return false;

		return is_valid_value( value, Constant._prohibitedCharacters1, number, warning);
	}

	/**
	 * Returns true if the specified value of the object is valid.
	 * @param value the specified value of the object
	 * @param number the line number
	 * @param warning whether to display the warning messages
	 * @return true if the specified value of the object is valid
	 */
	public static boolean is_valid_value3(String value, int number, boolean warning) {
		if ( value.equals( "$") || 0 < value.indexOf( '$')
			|| value.startsWith( " ") || value.endsWith( " "))
			return false;

		if ( value.startsWith( "$") && ( 0 < value.indexOf( "$", 1) || 0 <= value.indexOf( " ")))
			return false;

		return is_valid_value( value, Constant._prohibitedCharacters3, number, warning);
	}

	/**
	 * Returns true if the specified value of the object is valid.
	 * @param value the specified value of the object
	 * @param number the line number
	 * @param warning whether to display the warning messages
	 * @return true if the specified value of the object is valid
	 */
	public static boolean is_valid_value5(String value, int number, boolean warning) {
		if ( value.equals( "$") || 0 < value.indexOf( '$'))
			return false;

		if ( value.startsWith( "$") && 0 < value.indexOf( "$", 1))
			return false;

		return is_valid_value( value, Constant._prohibitedCharacters5, number, warning);
	}

	/**
	 * Returns true if the specified value of the object is valid.
	 * @param value the specified value of the object
	 * @param number the line number
	 * @param warning whether to display the warning messages
	 * @return true if the specified value of the object is valid
	 */
	public static boolean is_valid_value6(String value, int number, boolean warning) {
		if ( value.equals( "$") || 0 < value.indexOf( '$'))
			return false;

		if ( value.startsWith( "$") && 0 < value.indexOf( "$", 1))
			return false;

		return is_valid_value( value, Constant._prohibitedCharacters6, number, warning);
	}

	/**
	 * @param value
	 * @param number
	 * @param warning
	 * @return
	 */
	public static boolean is_valid_value17(String value, int number, boolean warning) {
		if ( value.equals( "$") || 0 < value.indexOf( '$')
			|| value.startsWith( " ") || value.endsWith( " "))
			return false;

		if ( value.startsWith( "$") && ( 0 < value.indexOf( "$", 1) || 0 <= value.indexOf( " ") || 0 <= value.indexOf( "/")))
			return false;

		return is_valid_value( value, Constant._prohibitedCharacters17, number, warning);
	}

	/**
	 * @param name
	 * @param prohibitedCharacters
	 * @param number the line number
	 * @param warning whether to display the warning messages
	 * @return
	 */
	private static boolean is_valid_value(String value, String prohibitedCharacters, int number, boolean warning) {
		for ( int i = 0; i < prohibitedCharacters.length(); ++i) {
			if ( 0 > value.indexOf( prohibitedCharacters.charAt( i)))
				continue;

			InitialDataImporter.on_invalid_value_error( number, warning);
			return false;
		}
		return true;
	}

	/**
	 * @param kind
	 * @param name
	 * @param type
	 * @param value
	 * @param entityData
	 * @param number
	 * @param warning
	 * @return
	 */
	private static boolean is_valid_variable_element(String kind, String name, String type, String value, EntityData entityData, int number, boolean warning) {
		if ( type.equals( "agent") || type.equals( "spot"))
			return ( is_valid_name( value, Constant._prohibitedCharacters2, number, warning) && Constant.is_correct_agent_or_spot_name( value));
		else if ( type.equals( "collection"))
			return ( !( type.equals( kind) && value.equals( name))
				&& is_valid_name( value, Constant._prohibitedCharacters2, number, warning) && Constant.is_correct_name( value));
		else if ( type.equals( "list"))
			return ( !( type.equals( kind) && value.equals( name))
				&& is_valid_name( value, Constant._prohibitedCharacters2, number, warning) && Constant.is_correct_name( value));
		else if ( type.equals( "map"))
			return ( !( type.equals( kind) && value.equals( name))
				&& is_valid_name( value, Constant._prohibitedCharacters1, number, warning) && Constant.is_correct_name( value));
		else if ( type.equals( "probability"))
			return ( is_valid_name( value, Constant._prohibitedCharacters4, number, warning) && Constant.is_correct_name( value));
		else if ( type.equals( "keyword"))
			return ( is_valid_name( value, Constant._prohibitedCharacters1, number, warning) && Constant.is_correct_name( value));
		else if ( type.equals( "number object"))
			return ( is_valid_name( value, Constant._prohibitedCharacters4, number, warning) && Constant.is_correct_name( value));
		else if ( type.equals( "role variable"))
			return ( is_valid_name( value, Constant._prohibitedCharacters2, number, warning) && Constant.is_correct_name( value)
				&& !LayerManager.get_instance().is_role_name( value) && ( null == LayerManager.get_instance().get_chart( value)));
		else if ( type.equals( "time variable"))
			return ( is_valid_name( value, Constant._prohibitedCharacters13, number, warning) && Constant.is_correct_name( value)
				&& ( null == LayerManager.get_instance().get_chart( value)));
		else if ( type.equals( "spot variable"))
			return ( is_valid_name( value, Constant._prohibitedCharacters1, number, warning) && Constant.is_correct_name( value)
				&& !( entityData._type.equals( "agent") && ( value.equals( "$Name") || value.equals( "$Role") || value.equals( "$Spot"))));
		else if ( type.equals( "class variable"))
			return ( is_valid_name( value, Constant._prohibitedCharacters1, number, warning) && Constant.is_correct_name( value)
				&& !( entityData._type.equals( "agent") && ( value.equals( "$Name") || value.equals( "$Role") || value.equals( "$Spot"))));
		else if ( type.equals( "file"))
			return ( is_valid_name( value, Constant._prohibitedCharacters1, number, warning) && Constant.is_correct_name( value)
				&& !( entityData._type.equals( "agent") && ( value.equals( "$Name") || value.equals( "$Role") || value.equals( "$Spot"))));
		else if ( type.equals( "exchange algebra"))
			return ( is_valid_name( value, Constant._prohibitedCharacters1, number, warning) && Constant.is_correct_name( value));

		InitialDataImporter.on_invalid_value_error( number, warning);
		return false;
	}

	/**
	 * @param value
	 * @return
	 */
	public static String normalize_file_path(String value) {
		// Trims " " from the beginning and end of a string.
		value = value.trim();

		if ( value.equals( ""))
			return value;

		// Replaces each substring of this string that matches "\" with "/".
		value = value.replaceAll( "\\\\", "/");

		// Trims "/" from the beginning of a string.
		int index = 0;
		for ( int i = 0; i < value.length(); ++i) {
			if ( '/' == value.charAt( i))
				++index;
			else
				break;
		}
		return value.substring( index);
	}

	/**
	 * @param value "file", "dir0/dir1/dir2/file", "dir0/dir1/dir2/dir3/" ...
	 * @param number
	 * @param warning
	 * @return
	 */
	public static boolean is_valid_file(String value, int number, boolean warning) {
		if ( value.startsWith( "$"))
			return true;

		String[] values = FileUtility.get_directory_and_filename( value);
		if ( null == values || 2 != values.length) {
			InitialDataImporter.on_invalid_value_error( number, warning);
			return false;
		}

		if ( !is_valid_file( values)) {
			InitialDataImporter.on_invalid_value_error( number, warning);
			return false;
		}

		if ( !LayerManager.get_instance().is_valid_file( values)) {
			InitialDataImporter.on_invalid_value_error( number, warning);
			return false;
		}

		return true;
	}

	/**
	 * @param values
	 * @return
	 */
	private static boolean is_valid_file(String[] values) {
		if ( !values[ 0].equals( "")) {
			String[] words = values[ 0].split( "/");
			if ( null == words || 0 == words.length)
				return false;

			for ( int i = 0; i < words.length; ++i) {
				if ( words[ i].equals( "") || words[ i].matches( "[\\.]+.*"))
					return false;
			}
		}

		if ( !values[ 1].equals( "") && values[ 1].matches( "[\\.]+.*"))
			return false;

		String homeDirectoryName = System.getProperty( Constant._soarsHome);
		String temporaryDirectoryName = SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._tmpKey, "");
		File rootDirectory = new File( homeDirectoryName + "/" + temporaryDirectoryName + Constant._testDirectory );

		if ( values[ 1].equals( "")) {
			if ( values[ 0].equals( ""))
				return true;
			
			File directory = new File( homeDirectoryName + "/" + temporaryDirectoryName + Constant._testDirectory + "/" + values[ 0]);
			if ( !directory.exists() && !directory.mkdirs())
				return false;

		} else {
			File directory = new File( homeDirectoryName + "/" + temporaryDirectoryName + Constant._testDirectory + ( values[ 0].equals( "") ? "" : ( "/" + values[ 0])));
			if ( !directory.exists() && !directory.mkdirs())
				return false;

			File file = new File( homeDirectoryName + "/" + temporaryDirectoryName + Constant._testDirectory + ( values[ 0].equals( "") ? "" : ( "/" + values[ 0])) + "/" + values[ 1]);
			if ( file.exists()) {
				if ( file.isFile())
					return true;

				return false;
			}

			try {
				if ( !file.createNewFile())
					return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns the integer array for the object.
	 * @param kind the object type
	 * @param name the object name
	 * @return the integer array for the object
	 */
	public Vector<int[]> get_object_ranges(String kind, String name) {
		if ( !is_multi())
			return new Vector();

		TreeMap<String, Object> treeMap = _objectMapMap.get( kind);
		if ( null == treeMap)
			return new Vector();

		return get_object_ranges( treeMap, name);
	}

	/**
	 * Returns the integer array for the object.
	 * @param treeMap the object hashtable
	 * @param name the object name
	 * @return the integer array for the object
	 */
	public static Vector<int[]> get_object_ranges(TreeMap<String, Object> treeMap, String name) {
		Vector<int[]> ranges = new Vector<int[]>();
		Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)treeMap.get( name);
		if ( null == indicesMap)
			return ranges;

		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Vector<int[]> indices = ( Vector<int[]>)entry.getValue();
			CommonTool.merge_indices( indices, ranges);
		}

		return ranges;
	}

	/**
	 * Returns true if the specified class variable name is in use for a different class.
	 * @param class_variable the specified class variable name
	 * @param jarFilename the jar file name
	 * @param classname the class name
	 * @param number the line number
	 * @param warning whether to display the warning messages
	 * @return true if the specified class variable name is in use for a different class
	 */
	public boolean uses_this_class_variable_as_different_class(String class_variable, String jarFilename, String classname, int number, boolean warning) {
		Iterator iterator = _objectMapMap.get( "class variable").entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			if ( !name.equals( class_variable))
				continue;

			if ( !is_multi()) {
				ClassVariableObject classVariableObject = ( ClassVariableObject)entry.getValue();
				if ( !classVariableObject._jarFilename.equals( jarFilename) || !classVariableObject._classname.equals( classname)) {
					InitialDataImporter.on_invalid_line_error( number, warning);
					return true;
				}
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				if ( uses_this_class_variable_as_different_class( indicesMap, class_variable, jarFilename, classname, number, warning))
					return true;
			}
		}
		return false;
	}

	/**
	 * @param indicesMap
	 * @param classVariable
	 * @param jarFilename
	 * @param classname
	 * @param number
	 * @param warning
	 * @return
	 */
	private boolean uses_this_class_variable_as_different_class(Map<ObjectBase, Vector<int[]>> indicesMap, String classVariable, String jarFilename, String classname, int number, boolean warning) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ClassVariableObject classVariableObject = ( ClassVariableObject)entry.getKey();
			if ( !classVariableObject._jarFilename.equals( jarFilename) || !classVariableObject._classname.equals( classname)) {
				InitialDataImporter.on_invalid_line_error( number, warning);
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if the loaded data are valid.
	 * @param agentDataMap the EntityData(agent) hashtable
	 * @param spotDataMap the EntityData(spot) hashtable
	 * @return true if the loaded data are valid
	 */
	public boolean verify(EntityDataMap agentDataMap, EntityDataMap spotDataMap) {
		for ( String kind:Constant._kinds) {
			if ( !verify( _objectMapMap.get( kind), agentDataMap, spotDataMap))
				return false;
		}
		return true;
	}

	/**
	 * @param treeMap
	 * @param agentDataMap
	 * @param spotDataMap
	 * @return
	 */
	private boolean verify(TreeMap<String, Object> treeMap, EntityDataMap agentDataMap, EntityDataMap spotDataMap) {
		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			if ( !is_multi()) {
				ObjectBase objectBase = ( ObjectBase)entry.getValue();
				if ( !objectBase.verify( agentDataMap, spotDataMap, this))
					return false;
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				if ( !verify( indicesMap, agentDataMap, spotDataMap))
					return false;
			}
		}
		return true;
	}

	/**
	 * @param indicesMap
	 * @param agentDataMap
	 * @param spotDataMap
	 * @return
	 */
	private boolean verify(Map<ObjectBase, Vector<int[]>> indicesMap, EntityDataMap agentDataMap, EntityDataMap spotDataMap) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			if ( !objectBase.verify( agentDataMap, spotDataMap, ( Vector)entry.getValue(), this))
				return false;
		}
		return true;
	}

	/**
	 * For debug print.
	 */
	public void print() {
		System.out.println( _name);
		System.out.println( _number);

		for ( String kind:Constant._kinds)
			print_object( _objectMapMap.get( kind), is_multi());
	}

	/**
	 * @param treeMap
	 * @param multi
	 */
	public static void print_object(TreeMap<String, Object> treeMap, boolean multi) {
		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			if ( !multi) {
				ObjectBase objectBase = ( ObjectBase)entry.getValue();
				objectBase.print();
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				print_object( indicesMap);
			}
		}
	}

	/**
	 * @param indicesMap
	 */
	private static void print_object(Map<ObjectBase, Vector<int[]>> indicesMap) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			Vector<int[]> indices = ( Vector<int[]>)entry.getValue();
			objectBase.print( indices);
		}
	}
}
