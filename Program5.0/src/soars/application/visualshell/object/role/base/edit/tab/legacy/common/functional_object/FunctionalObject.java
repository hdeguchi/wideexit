/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.common.functional_object;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import soars.application.visualshell.layer.ILayerManipulator;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.object.arbitrary.JarFileProperties;
import soars.application.visualshell.object.entity.base.object.class_variable.ClassVariableObject;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;

/**
 * @author kurata
 *
 */
public class FunctionalObject {

	 public String[] _spots = null;					// { null, ""}, { "", ""}, { "spot", ""}, { null, "sv"}, { "", "sv"}, { "spot", "sv"}		old -> null, "", "spot"
	 public String _classVariable = "";
	 public String _method = "";
	 public String[][] _parameters = null;		// { { value, type}, { value, type}, ... }
	 public String[] _returnValue = null;		// null or { value, type}

	/**
	 * @param spots
	 * @param classVariable
	 * @param method
	 * @param parameters
	 * @param returnValue
	 */
	public FunctionalObject(String[] spots, String classVariable, String method, List<String[]> parameters, String[] returnValue) {
		super();
		_spots = spots;
		_classVariable = classVariable;
		_method = method;
		_parameters = ( String[][])parameters.toArray( new String[ 0][]);
		_returnValue = returnValue;
	}

	/**
	 * @param methodObject
	 * @return
	 */
	public boolean equals(MethodObject methodObject) {
		if ( _parameters.length != methodObject._parameterTypes.length)
			return false;

		for ( int i = 0; i < _parameters.length; ++i) {
			if ( !_parameters[ i][ 1].equals( methodObject._parameterTypes[ i]))
				return false;
		}

		return true;
	}

	/**
	 * @param type
	 * @return
	 */
	public String[] get_object_names(String type) {
		List<String> list = new ArrayList<String>();

		// TODO Auto-generated method stub
		get_object_name( type, list);

		get_parameter_names( type, list);
		get_return_value_name( type, list);

		if ( list.isEmpty())
			return null;

		return list.toArray( new String[ 0]);
	}

	/**
	 * @param type
	 * @param list
	 */
	private void get_object_name(String type, List<String> list) {
		// TODO Auto-generated method stub
		if ( type.equals( "class variable")
			&& CommonRuleManipulator.is_object( "class variable", CommonRuleManipulator.get_full_prefix( _spots) + _classVariable)) {
			list.add( get_class_variable_full_name());
			return;
		}

		if ( Environment.get_instance().is_exchange_algebra_enable()
			&& type.equals( "exchange algebra")
			&& CommonRuleManipulator.is_object( "exchange algebra", CommonRuleManipulator.get_full_prefix( _spots) + _classVariable)) {
			list.add( get_class_variable_full_name());
			return;
		}
	}

	/**
	 * @return
	 */
	private String get_class_variable_full_name() {
		return ( CommonRuleManipulator.get_full_prefix( _spots) + _classVariable);
//		return ( ( ( null == _spot) ? "" : "<" + _spot + ">") + _class_variable);
	}

	/**
	 * @param type
	 * @param list
	 */
	private void get_parameter_names(String type, List<String> list) {
		if ( null == _parameters)
			return;

		for ( int i = 0; i < _parameters.length; ++i)
			get_object_name( type, _parameters[ i], list);
	}

	/**
	 * @param type
	 * @param list
	 */
	private void get_return_value_name(String type, List<String> list) {
		if ( null == _returnValue)
			return;

		get_object_name( type, _returnValue, list);
	}

	/**
	 * @param type
	 * @param value
	 * @param list
	 */
	private void get_object_name(String type, String[] value, List<String> list) {
		// TODO Auto-generated method stub
		if ( type.equals( "probability"))
			get_probability_name( value, list);
		else if ( type.equals( "collection"))
			get_collection_name( value, list);
		else if ( type.equals( "list"))
			get_list_name( value, list);
		else if ( type.equals( "map"))
			get_map_name( value, list);
		else if ( type.equals( "keyword"))
			get_keyword_name( value, list);
		else if ( type.equals( "number object"))
			get_number_object_name( value, list);
		else if ( type.equals( "file"))
			get_file_name( value, list);
		else if ( type.equals( "agent"))
			get_agent_name( value, list);
		else if ( type.equals( "spot"))
			get_spot_name( value, list);
		else if ( type.equals( "role variable"))
			get_role_variable_name( value, list);
		else if ( type.equals( "time variable"))
			get_time_variable_name( value, list);
		else if ( type.equals( "spot variable"))
			get_spot_variable_name( value, list);
		else if ( type.equals( "class variable"))
			get_class_variable_name( value, list);
		else if ( type.equals( "exchange algebra"))
			get_exchange_algebra_name( value, list);
	}

	/**
	 * @param value
	 * @param list
	 */
	private void get_probability_name(String[] value, List<String> list) {
		if ( ( value[ 1].equals( "double")
			|| value[ 1].equals( "float"))
			&& CommonRuleManipulator.is_object( "probability", CommonRuleManipulator.get_full_prefix( _spots) + value[ 0], LayerManager.get_instance()))
			//&& LayerManager.get_instance().is_object_name( "probability", value[ 0]))
			append( value[ 0], list);
	}

	/**
	 * @param value
	 * @param list
	 */
	private void get_collection_name(String[] value, List<String> list) {
		if ( ( value[ 1].equals( "java.util.Collection")
			|| value[ 1].equals( "java.util.HashSet")
			|| value[ 1].equals( "java.util.Set"))
			&& CommonRuleManipulator.is_object( "collection", CommonRuleManipulator.get_full_prefix( _spots) + value[ 0], LayerManager.get_instance()))
			//&& LayerManager.get_instance().is_object_name( "collection", value[ 0]))
			append( value[ 0], list);
	}

	/**
	 * @param value
	 * @param list
	 */
	private void get_list_name(String[] value, List<String> list) {
		if ( ( value[ 1].equals( "java.util.Collection")
			|| value[ 1].equals( "java.util.LinkedList")
			|| value[ 1].equals( "java.util.List"))
			&& CommonRuleManipulator.is_object( "list", CommonRuleManipulator.get_full_prefix( _spots) + value[ 0], LayerManager.get_instance()))
			//&& LayerManager.get_instance().is_object_name( "list", value[ 0]))
			append( value[ 0], list);
	}

	/**
	 * @param value
	 * @param list
	 */
	private void get_map_name(String[] value, List<String> list) {
		if ( ( value[ 1].equals( "java.util.HashMap")
			|| value[ 1].equals( "java.util.Map"))
			&& CommonRuleManipulator.is_object( "map", CommonRuleManipulator.get_full_prefix( _spots) + value[ 0], LayerManager.get_instance()))
			//&& LayerManager.get_instance().is_object_name( "map", value[ 0]))
			append( value[ 0], list);
	}

	/**
	 * @param value
	 * @param list
	 */
	private void get_keyword_name(String[] value, List<String> list) {
		if ( ( value[ 1].equals( "boolean")
			|| value[ 1].equals( "java.lang.String"))
			&& CommonRuleManipulator.is_object( "keyword", CommonRuleManipulator.get_full_prefix( _spots) + value[ 0], LayerManager.get_instance()))
			//&& LayerManager.get_instance().is_object_name( "keyword", value[ 0]))
			append( value[ 0], list);
	}

	/**
	 * @param value
	 * @param list
	 */
	private void get_number_object_name(String[] value, List<String> list) {
		if ( ( value[ 1].equals( "int")
			|| value[ 1].equals( "byte")
			|| value[ 1].equals( "short")
			|| value[ 1].equals( "long")
			|| value[ 1].equals( "double")
			|| value[ 1].equals( "float"))
			&& CommonRuleManipulator.is_object( "number object", CommonRuleManipulator.get_full_prefix( _spots) + value[ 0], LayerManager.get_instance()))
			//&& LayerManager.get_instance().is_object_name( "number object", value[ 0]))
			append( value[ 0], list);
	}

	/**
	 * @param value
	 * @param list
	 */
	private void get_file_name(String[] value, List<String> list) {
		if ( value[ 1].equals( "java.lang.String")
			&& CommonRuleManipulator.is_object( "file", CommonRuleManipulator.get_full_prefix( _spots) + value[ 0], LayerManager.get_instance()))
			//&& LayerManager.get_instance().is_object_name( "file", value[ 0]))
			append( value[ 0], list);
	}

	/**
	 * @param value
	 * @param list
	 */
	private void get_role_variable_name(String[] value, List<String> list) {
		if ( !value[ 1].equals( "int")
			&& !value[ 1].equals( "byte")
			&& !value[ 1].equals( "short")
			&& !value[ 1].equals( "long")
			&& !value[ 1].equals( "double")
			&& !value[ 1].equals( "float")
			&& !value[ 1].equals( "java.util.Collection")
			&& !value[ 1].equals( "java.util.HashSet")
			&& !value[ 1].equals( "java.util.Set")
			&& !value[ 1].equals( "java.util.LinkedList")
			&& !value[ 1].equals( "java.util.List")
			&& !value[ 1].equals( "java.util.HashMap")
			&& !value[ 1].equals( "java.util.Map")
			&& !value[ 1].equals( "boolean")
			&& !value[ 1].equals( "java.lang.String")
			&& !value[ 1].equals( "env.Agent")
			&& !value[ 1].equals( "env.Spot")
			&& CommonRuleManipulator.is_object( "role variable", CommonRuleManipulator.get_full_prefix( _spots) + value[ 0], LayerManager.get_instance()))
			//&& LayerManager.get_instance().is_object_name( "class variable", value[ 0]))
			append( value[ 0], list);
	}

	/**
	 * @param value
	 * @param list
	 */
	private void get_time_variable_name(String[] value, List<String> list) {
		if ( !value[ 1].equals( "int")
			&& !value[ 1].equals( "byte")
			&& !value[ 1].equals( "short")
			&& !value[ 1].equals( "long")
			&& !value[ 1].equals( "double")
			&& !value[ 1].equals( "float")
			&& !value[ 1].equals( "java.util.Collection")
			&& !value[ 1].equals( "java.util.HashSet")
			&& !value[ 1].equals( "java.util.Set")
			&& !value[ 1].equals( "java.util.LinkedList")
			&& !value[ 1].equals( "java.util.List")
			&& !value[ 1].equals( "java.util.HashMap")
			&& !value[ 1].equals( "java.util.Map")
			&& !value[ 1].equals( "boolean")
			&& !value[ 1].equals( "java.lang.String")
			&& !value[ 1].equals( "env.Agent")
			&& !value[ 1].equals( "env.Spot")
			&& CommonRuleManipulator.is_object( "time variable", CommonRuleManipulator.get_full_prefix( _spots) + value[ 0], LayerManager.get_instance()))
			//&& LayerManager.get_instance().is_object_name( "class variable", value[ 0]))
			append( value[ 0], list);
	}

	/**
	 * @param value
	 * @param list
	 */
	private void get_spot_variable_name(String[] value, List<String> list) {
		if ( !value[ 1].equals( "int")
			&& !value[ 1].equals( "byte")
			&& !value[ 1].equals( "short")
			&& !value[ 1].equals( "long")
			&& !value[ 1].equals( "double")
			&& !value[ 1].equals( "float")
			&& !value[ 1].equals( "java.util.Collection")
			&& !value[ 1].equals( "java.util.HashSet")
			&& !value[ 1].equals( "java.util.Set")
			&& !value[ 1].equals( "java.util.LinkedList")
			&& !value[ 1].equals( "java.util.List")
			&& !value[ 1].equals( "java.util.HashMap")
			&& !value[ 1].equals( "java.util.Map")
			&& !value[ 1].equals( "boolean")
			&& !value[ 1].equals( "java.lang.String")
			&& !value[ 1].equals( "env.Agent")
			&& !value[ 1].equals( "env.Spot")
			&& CommonRuleManipulator.is_object( "spot variable", CommonRuleManipulator.get_full_prefix( _spots) + value[ 0], LayerManager.get_instance()))
			//&& LayerManager.get_instance().is_object_name( "class variable", value[ 0]))
			append( value[ 0], list);
	}

	/**
	 * @param value
	 * @param list
	 */
	private void get_class_variable_name(String[] value, List<String> list) {
		if ( !value[ 1].equals( "int")
			&& !value[ 1].equals( "byte")
			&& !value[ 1].equals( "short")
			&& !value[ 1].equals( "long")
			&& !value[ 1].equals( "double")
			&& !value[ 1].equals( "float")
			&& !value[ 1].equals( "java.util.Collection")
			&& !value[ 1].equals( "java.util.HashSet")
			&& !value[ 1].equals( "java.util.Set")
			&& !value[ 1].equals( "java.util.LinkedList")
			&& !value[ 1].equals( "java.util.List")
			&& !value[ 1].equals( "java.util.HashMap")
			&& !value[ 1].equals( "java.util.Map")
			&& !value[ 1].equals( "boolean")
			&& !value[ 1].equals( "java.lang.String")
			&& !value[ 1].equals( "env.Agent")
			&& !value[ 1].equals( "env.Spot")
			&& CommonRuleManipulator.is_object( "class variable", CommonRuleManipulator.get_full_prefix( _spots) + value[ 0], LayerManager.get_instance()))
			//&& LayerManager.get_instance().is_object_name( "class variable", value[ 0]))
			append( value[ 0], list);
	}

	/**
	 * @param value
	 * @param list
	 */
	private void get_exchange_algebra_name(String[] value, List<String> list) {
		if ( !value[ 1].equals( "int")
			&& !value[ 1].equals( "byte")
			&& !value[ 1].equals( "short")
			&& !value[ 1].equals( "long")
			&& !value[ 1].equals( "double")
			&& !value[ 1].equals( "float")
			&& !value[ 1].equals( "java.util.Collection")
			&& !value[ 1].equals( "java.util.HashSet")
			&& !value[ 1].equals( "java.util.Set")
			&& !value[ 1].equals( "java.util.LinkedList")
			&& !value[ 1].equals( "java.util.List")
			&& !value[ 1].equals( "java.util.HashMap")
			&& !value[ 1].equals( "java.util.Map")
			&& !value[ 1].equals( "boolean")
			&& !value[ 1].equals( "java.lang.String")
			&& !value[ 1].equals( "env.Agent")
			&& !value[ 1].equals( "env.Spot")
			&& CommonRuleManipulator.is_object( "exchange algebra", CommonRuleManipulator.get_full_prefix( _spots) + value[ 0], LayerManager.get_instance()))
			//&& LayerManager.get_instance().is_object_name( "exchange algebra", value[ 0]))
			append( value[ 0], list);
	}

	/**
	 * @param value
	 * @param list
	 */
	private void append(String value, List<String> list) {
		list.add( CommonRuleManipulator.get_full_prefix( _spots) + value);
	}

	/**
	 * @param value
	 * @param list
	 */
	private void get_agent_name(String[] value, List<String> list) {
		// TODO Auto-generated method stub
		if ( value[ 1].equals( "env.Agent") && !value[ 0].equals( ""))
			list.add( value[ 0]);
	}

	/**
	 * @param value
	 * @param list
	 */
	private void get_spot_name(String[] value, List<String> list) {
		// TODO Auto-generated method stub
		if ( value[ 1].equals( "env.Spot") && !value[ 0].equals( "") /*&& !value[ 0].equals( "<>")*/)
			list.add( value[ 0]);
	}

	/**
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @param rule
	 * @return
	 */
	public boolean update_agent_name(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges, Rule rule) {
		// TODO Auto-generated method stub
		boolean result1 = update_parameter_name( "env.Agent", newName, originalName, headName, ranges, newHeadName, newRanges);
		boolean result2 = update_return_value_name( "env.Agent", newName, originalName, headName, ranges, newHeadName, newRanges);
		if ( !result1 && !result2)
			return false;

		rule._value = get();

		return true;
	}

	/**
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @param rule
	 * @return
	 */
	public boolean update_spot_name(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges, Rule rule) {
		// TODO Auto-generated method stub
		boolean result1 = update_parameter_name( "env.Spot", newName, originalName, headName, ranges, newHeadName, newRanges);
		boolean result2 = update_return_value_name( "env.Spot", newName, originalName, headName, ranges, newHeadName, newRanges);
		boolean result3 = CommonRuleManipulator.update_spot_name3( _spots, newName, originalName, headName, ranges, newHeadName, newRanges);
		if ( !result1 && !result2 && !result3)
			return false;

		rule._value = get();

		return true;
	}

	/**
	 * @param type
	 * @param value
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	private boolean update_parameter_name(String type, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		// TODO Auto-generated method stub
		if ( null == _parameters)
			return false;

		boolean result = false;
		for ( int i = 0; i < _parameters.length; ++i) {
			if ( update_agent_or_spot_name( type, _parameters[ i], newName, originalName, headName, ranges, newHeadName, newRanges))
				result = true;
		}

		return result;
	}

	/**
	 * @param type
	 * @param value
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	private boolean update_return_value_name(String type, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		// TODO Auto-generated method stub
		if ( null == _returnValue)
			return false;

		return update_agent_or_spot_name( type, _returnValue, newName, originalName, headName, ranges, newHeadName, newRanges);
	}

	/**
	 * @param type
	 * @param value
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	private boolean update_agent_or_spot_name(String type, String[] value, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		// TODO Auto-generated method stub
		if ( !value[ 1].equals( type) || value[ 0].equals( "") /*|| value[ 0].equals( "<>")*/)
			return false;

		String new_agent_or_spot_name = CommonRuleManipulator.get_new_agent_or_spot_name( value[ 0], newName, originalName, headName, ranges, newHeadName, newRanges);
		if ( null == new_agent_or_spot_name)
			return false;

		value[ 0] = new_agent_or_spot_name;
		return true;
	}

	/**
	 * @param name
	 * @param newName
	 * @param type
	 * @param rule
	 * @return
	 */
	public boolean update_spot_variable_name(String name, String newName, String type, Rule rule) {
		String spot = CommonRuleManipulator.get_full_prefix( _spots);

		boolean result1 = CommonRuleManipulator.update_spot_variable_name3( _spots, name, newName, type);
		boolean result2 = update_parameter_name( "spot variable", spot, name, newName, type);
		boolean result3 = update_return_value_name( "spot variable", spot, name, newName, type);
		if ( !result1 && !result2 && !result3)
			return false;

		rule._value = get();

		return true;
	}

	/**
	 * @param kind
	 * @param name
	 * @param newName
	 * @param type
	 * @param rule
	 * @return
	 */
	public boolean update_object_name(String kind, String name, String newName, String type, Rule rule) {
		String spot = CommonRuleManipulator.get_full_prefix( _spots);

		boolean result1 = false;
		if ( kind.equals( "class variable"))
			result1 = update_class_variable_name( spot, name, newName, type);
			
		boolean result2 = update_parameter_name( kind, spot, name, newName, type);
		boolean result3 = update_return_value_name( kind, spot, name, newName, type);
		if ( !result1 && !result2 && !result3)
			return false;

		rule._value = get();

		return true;
	}

	/**
	 * @param spot
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private boolean update_class_variable_name(String spot, String name, String newName, String type) {
		if ( !CommonRuleManipulator.is_object( "class variable", spot + _classVariable, LayerManager.get_instance()))
		//if ( !LayerManager.get_instance().is_object_name( "class variable", _classVariable))
			return false;

		if ( !CommonRuleManipulator.correspond( spot, _classVariable, name, type))
			return false;

		_classVariable = newName;

		return true;
	}

	/**
	 * @param kind
	 * @param spot
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private boolean update_parameter_name(String kind, String spot, String name, String newName, String type) {
		if ( null == _parameters)
			return false;

		boolean result = false;
		for ( int i = 0; i < _parameters.length; ++i) {
			if ( update_object_name( kind, spot, _parameters[ i], name, newName, type))
				result = true;
		}

		return result;
	}

	/**
	 * @param kind
	 * @param spot
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private boolean update_return_value_name(String kind, String spot, String name, String newName, String type) {
		if ( null == _returnValue)
			return false;

		return update_object_name( kind, spot, _returnValue, name, newName, type);
	}

	/**
	 * @param kind
	 * @param spot
	 * @param value
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private boolean update_object_name(String kind, String spot, String[] value, String name, String newName, String type) {
		if ( kind.equals( "probability"))
			return update_probability_name( spot, value, name, newName, type);
		else if ( kind.equals( "collection"))
			return update_collection_name( spot, value, name, newName, type);
		else if ( kind.equals( "list"))
			return update_list_name( spot, value, name, newName, type);
		else if ( kind.equals( "map"))
			return update_map_name( spot, value, name, newName, type);
		else if ( kind.equals( "keyword"))
			return update_keyword_name( spot, value, name, newName, type);
		else if ( kind.equals( "number object"))
			return update_number_object_name( spot, value, name, newName, type);
		else if ( kind.equals( "file"))
			return update_file_name( spot, value, name, newName, type);
		else if ( kind.equals( "role variable"))
			return update_role_variable_name( spot, value, name, newName, type);
		else if ( kind.equals( "time variable"))
			return update_time_variable_name( spot, value, name, newName, type);
		else if ( kind.equals( "spot variable"))
			return update_spot_variable_name( spot, value, name, newName, type);
		else if ( kind.equals( "class variable"))
			return update_class_variable_name( spot, value, name, newName, type);
		else if ( kind.equals( "exchange algebra"))
			return update_exhange_algebra_name( spot, value, name, newName, type);

		return false;
	}

	/**
	 * @param spot
	 * @param value
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private boolean update_probability_name(String spot, String[] value, String name, String newName, String type) {
		if ( ( value[ 1].equals( "double")
			|| value[ 1].equals( "float"))
			&& CommonRuleManipulator.is_object( "probability", spot + value[ 0], LayerManager.get_instance())
			//&& LayerManager.get_instance().is_object_name( "probability", value[ 0])
			&& CommonRuleManipulator.correspond( spot, value[ 0], name, type)) {
			value[ 0] = newName;
			return true;
		}
		return false;
	}

	/**
	 * @param spot
	 * @param value
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private boolean update_collection_name(String spot, String[] value, String name, String newName, String type) {
		if ( ( value[ 1].equals( "java.util.Collection")
			|| value[ 1].equals( "java.util.HashSet")
			|| value[ 1].equals( "java.util.Set"))
			&& CommonRuleManipulator.is_object( "collection", spot + value[ 0], LayerManager.get_instance())
			//&& LayerManager.get_instance().is_object_name( "collection", value[ 0])
			&& CommonRuleManipulator.correspond( spot, value[ 0], name, type)) {
			value[ 0] = newName;
			return true;
		}
		return false;
	}

	/**
	 * @param spot
	 * @param value
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private boolean update_list_name(String spot, String[] value, String name, String newName, String type) {
		if ( ( value[ 1].equals( "java.util.Collection")
			|| value[ 1].equals( "java.util.LinkedList")
			|| value[ 1].equals( "java.util.List"))
			&& CommonRuleManipulator.is_object( "list", spot + value[ 0], LayerManager.get_instance())
			//&& LayerManager.get_instance().is_object_name( "list", value[ 0])
			&& CommonRuleManipulator.correspond( spot, value[ 0], name, type)) {
			value[ 0] = newName;
			return true;
		}
		return false;
	}

	/**
	 * @param spot
	 * @param value
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private boolean update_map_name(String spot, String[] value, String name, String newName, String type) {
		if ( ( value[ 1].equals( "java.util.HashMap")
			|| value[ 1].equals( "java.util.Map"))
			&& CommonRuleManipulator.is_object( "map", spot + value[ 0], LayerManager.get_instance())
			//&& LayerManager.get_instance().is_object_name( "map", value[ 0])
			&& CommonRuleManipulator.correspond( spot, value[ 0], name, type)) {
			value[ 0] = newName;
			return true;
		}
		return false;
	}

	/**
	 * @param spot
	 * @param value
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private boolean update_keyword_name(String spot, String[] value, String name, String newName, String type) {
		if ( ( value[ 1].equals( "boolean")
			|| value[ 1].equals( "java.lang.String"))
			&& CommonRuleManipulator.is_object( "keyword", spot + value[ 0], LayerManager.get_instance())
			//&& LayerManager.get_instance().is_object_name( "keyword", value[ 0])
			&& CommonRuleManipulator.correspond( spot, value[ 0], name, type)) {
			value[ 0] = newName;
			return true;
		}
		return false;
	}

	/**
	 * @param spot
	 * @param value
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private boolean update_number_object_name(String spot, String[] value, String name, String newName, String type) {
		if ( ( value[ 1].equals( "int")
			|| value[ 1].equals( "byte")
			|| value[ 1].equals( "short")
			|| value[ 1].equals( "long")
			|| value[ 1].equals( "double")
			|| value[ 1].equals( "float"))
			&& CommonRuleManipulator.is_object( "number object", spot + value[ 0], LayerManager.get_instance())
			//&& LayerManager.get_instance().is_object_name( "number object", value[ 0])
			&& CommonRuleManipulator.correspond( spot, value[ 0], name, type)) {
			value[ 0] = newName;
			return true;
		}
		return false;
	}

	/**
	 * @param spot
	 * @param value
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private boolean update_file_name(String spot, String[] value, String name, String newName, String type) {
		if ( value[ 1].equals( "java.lang.String")
			&& CommonRuleManipulator.is_object( "file", spot + value[ 0], LayerManager.get_instance())
			//&& LayerManager.get_instance().is_object_name( "file", value[ 0])
			&& CommonRuleManipulator.correspond( spot, value[ 0], name, type)) {
			value[ 0] = newName;
			return true;
		}
		return false;
	}

	/**
	 * @param spot
	 * @param value
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private boolean update_role_variable_name(String spot, String[] value, String name, String newName, String type) {
		if ( !value[ 1].equals( "int")
			&& !value[ 1].equals( "byte")
			&& !value[ 1].equals( "short")
			&& !value[ 1].equals( "long")
			&& !value[ 1].equals( "double")
			&& !value[ 1].equals( "float")
			&& !value[ 1].equals( "java.util.Collection")
			&& !value[ 1].equals( "java.util.HashSet")
			&& !value[ 1].equals( "java.util.Set")
			&& !value[ 1].equals( "java.util.LinkedList")
			&& !value[ 1].equals( "java.util.List")
			&& !value[ 1].equals( "java.util.HashMap")
			&& !value[ 1].equals( "java.util.Map")
			&& !value[ 1].equals( "boolean")
			&& !value[ 1].equals( "java.lang.String")
			&& !value[ 1].equals( "env.Agent")
			&& !value[ 1].equals( "env.Spot")
			&& CommonRuleManipulator.is_object( "role variable", spot + value[ 0], LayerManager.get_instance())
			//&& LayerManager.get_instance().is_object_name( "role variable", value[ 0])
			&& CommonRuleManipulator.correspond( spot, value[ 0], name, type)) {
			value[ 0] = newName;
			return true;
		}
		return false;
	}

	/**
	 * @param spot
	 * @param value
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private boolean update_time_variable_name(String spot, String[] value, String name, String newName, String type) {
		if ( !value[ 1].equals( "int")
			&& !value[ 1].equals( "byte")
			&& !value[ 1].equals( "short")
			&& !value[ 1].equals( "long")
			&& !value[ 1].equals( "double")
			&& !value[ 1].equals( "float")
			&& !value[ 1].equals( "java.util.Collection")
			&& !value[ 1].equals( "java.util.HashSet")
			&& !value[ 1].equals( "java.util.Set")
			&& !value[ 1].equals( "java.util.LinkedList")
			&& !value[ 1].equals( "java.util.List")
			&& !value[ 1].equals( "java.util.HashMap")
			&& !value[ 1].equals( "java.util.Map")
			&& !value[ 1].equals( "boolean")
			&& !value[ 1].equals( "java.lang.String")
			&& !value[ 1].equals( "env.Agent")
			&& !value[ 1].equals( "env.Spot")
			&& CommonRuleManipulator.is_object( "time variable", spot + value[ 0], LayerManager.get_instance())
			//&& LayerManager.get_instance().is_object_name( "time variable", value[ 0])
			&& CommonRuleManipulator.correspond( spot, value[ 0], name, type)) {
			value[ 0] = newName;
			return true;
		}
		return false;
	}

	/**
	 * @param spot
	 * @param value
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private boolean update_spot_variable_name(String spot, String[] value, String name, String newName, String type) {
		if ( !value[ 1].equals( "int")
			&& !value[ 1].equals( "byte")
			&& !value[ 1].equals( "short")
			&& !value[ 1].equals( "long")
			&& !value[ 1].equals( "double")
			&& !value[ 1].equals( "float")
			&& !value[ 1].equals( "java.util.Collection")
			&& !value[ 1].equals( "java.util.HashSet")
			&& !value[ 1].equals( "java.util.Set")
			&& !value[ 1].equals( "java.util.LinkedList")
			&& !value[ 1].equals( "java.util.List")
			&& !value[ 1].equals( "java.util.HashMap")
			&& !value[ 1].equals( "java.util.Map")
			&& !value[ 1].equals( "boolean")
			&& !value[ 1].equals( "java.lang.String")
			&& !value[ 1].equals( "env.Agent")
			&& !value[ 1].equals( "env.Spot")
			&& CommonRuleManipulator.is_object( "spot variable", spot + value[ 0], LayerManager.get_instance())
			//&& LayerManager.get_instance().is_object_name( "spot variable", value[ 0])
			&& CommonRuleManipulator.correspond( spot, value[ 0], name, type)) {
			value[ 0] = newName;
			return true;
		}
		return false;
	}

	/**
	 * @param spot
	 * @param value
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private boolean update_class_variable_name(String spot, String[] value, String name, String newName, String type) {
		if ( !value[ 1].equals( "int")
			&& !value[ 1].equals( "byte")
			&& !value[ 1].equals( "short")
			&& !value[ 1].equals( "long")
			&& !value[ 1].equals( "double")
			&& !value[ 1].equals( "float")
			&& !value[ 1].equals( "java.util.Collection")
			&& !value[ 1].equals( "java.util.HashSet")
			&& !value[ 1].equals( "java.util.Set")
			&& !value[ 1].equals( "java.util.LinkedList")
			&& !value[ 1].equals( "java.util.List")
			&& !value[ 1].equals( "java.util.HashMap")
			&& !value[ 1].equals( "java.util.Map")
			&& !value[ 1].equals( "boolean")
			&& !value[ 1].equals( "java.lang.String")
			&& !value[ 1].equals( "env.Agent")
			&& !value[ 1].equals( "env.Spot")
			&& CommonRuleManipulator.is_object( "class variable", spot + value[ 0], LayerManager.get_instance())
			//&& LayerManager.get_instance().is_object_name( "class variable", value[ 0])
			&& CommonRuleManipulator.correspond( spot, value[ 0], name, type)) {
			value[ 0] = newName;
			return true;
		}
		return false;
	}

	/**
	 * @param spot
	 * @param value
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private boolean update_exhange_algebra_name(String spot, String[] value, String name, String newName, String type) {
		// TODO Auto-generated method stub
		boolean result = false;
		if ( CommonRuleManipulator.is_object( "exchange algebra", spot + _classVariable)
			&& CommonRuleManipulator.correspond( spot, _classVariable, name, type)) {
			_classVariable = newName;
			result = true;
		}

		if ( !value[ 1].equals( "int")
			&& !value[ 1].equals( "byte")
			&& !value[ 1].equals( "short")
			&& !value[ 1].equals( "long")
			&& !value[ 1].equals( "double")
			&& !value[ 1].equals( "float")
			&& !value[ 1].equals( "java.util.Collection")
			&& !value[ 1].equals( "java.util.HashSet")
			&& !value[ 1].equals( "java.util.Set")
			&& !value[ 1].equals( "java.util.LinkedList")
			&& !value[ 1].equals( "java.util.List")
			&& !value[ 1].equals( "java.util.HashMap")
			&& !value[ 1].equals( "java.util.Map")
			&& !value[ 1].equals( "boolean")
			&& !value[ 1].equals( "java.lang.String")
			&& !value[ 1].equals( "env.Agent")
			&& !value[ 1].equals( "env.Spot")
			&& CommonRuleManipulator.is_object( "exchange algebra", spot + value[ 0], LayerManager.get_instance())
			//&& LayerManager.get_instance().is_object_name( "exchange algebra", value[ 0])
			&& CommonRuleManipulator.correspond( spot, value[ 0], name, type)) {
			value[ 0] = newName;
			result = true;
		}

		return result;
	}

	/**
	 * @return
	 */
	private String get() {
		String value = CommonRuleManipulator.get_full_prefix( _spots);

		value += _classVariable;

		value += ( " " + _method);

		if ( null != _parameters) {
			for ( int i = 0; i < _parameters.length; ++i)
				value += ( " " + _parameters[ i][ 0] + "=" + _parameters[ i][ 1]);
		}

		if ( null != _returnValue)
			value += ( " return: " + _returnValue[ 0] + "=" + _returnValue[ 1]);

		return value;
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	public boolean is_correct(Layer drawObjects) {
		ClassVariableObject classVariableObject = drawObjects.get_class_variable( this);
		if ( null == classVariableObject) {
			if ( !Environment.get_instance().is_exchange_algebra_enable())
				return false;
			else {
				if ( !CommonRuleManipulator.is_object( "exchange algebra", CommonRuleManipulator.get_full_prefix( _spots) + _classVariable, drawObjects))
					return false;

				// TODO もし交換代数なら、、、
				classVariableObject = new ClassVariableObject( _classVariable, Constant._exchangeAlgebraJarFilename, Constant._exchangeAlgebraClassname);
			}
		}

		return JarFileProperties.get_instance().contains( classVariableObject._jarFilename,
			classVariableObject._classname, _method, _parameters, _returnValue);
	}

	/**
	 * @param layerManipulator
	 * @return
	 */
	public boolean can_paste_object_names(ILayerManipulator layerManipulator) {
		// TODO Auto-generated method stub
		String spot = CommonRuleManipulator.get_full_prefix( _spots);
		return ( ( CommonRuleManipulator.can_paste_object( "class variable", spot + _classVariable, layerManipulator)
				|| ( Environment.get_instance().is_exchange_algebra_enable() && CommonRuleManipulator.can_paste_object( "exchange algebra", spot + _classVariable, layerManipulator)))
			&& can_paste_parameter_names( spot, layerManipulator)
			&& can_paste_return_value_name( spot, layerManipulator));
	}

	/**
	 * @param spot
	 * @param layerManipulator
	 * @return
	 */
	private boolean can_paste_parameter_names(String spot, ILayerManipulator layerManipulator) {
		// TODO Auto-generated method stub
		if ( null == _parameters)
			return true;

		for ( int i = 0; i < _parameters.length; ++i) {
			if ( null == _parameters[ i] || _parameters[ i][ 0].equals( ""))
				continue;

			if ( !can_paste_object_name( spot, _parameters[ i], layerManipulator))
				return false;
		}

		return true;
	}

	/**
	 * @param spot
	 * @param layerManipulator
	 * @return
	 */
	private boolean can_paste_return_value_name(String spot, ILayerManipulator layerManipulator) {
		// TODO Auto-generated method stub
		if ( null == _returnValue || _returnValue[ 0].equals( ""))
			return true;

		return can_paste_object_name( spot, _returnValue, layerManipulator);
	}

	/**
	 * @param spot
	 * @param value
	 * @param layerManipulator
	 * @return
	 */
	private boolean can_paste_object_name(String spot, String[] value, ILayerManipulator layerManipulator) {
		// TODO Auto-generated method stub
		return ( can_paste_probability_name( spot, value, layerManipulator)
			|| can_paste_collection_name( spot, value, layerManipulator)
			|| can_paste_list_name( spot, value, layerManipulator)
			|| can_paste_map_name( spot, value, layerManipulator)
			|| can_paste_keyword_name( spot, value, layerManipulator)
			|| can_paste_number_object_name( spot, value, layerManipulator)
			|| can_paste_file_name( spot, value, layerManipulator)
			|| can_paste_role_variable_name( spot, value, layerManipulator)
			|| can_paste_time_variable_name( spot, value, layerManipulator)
			|| can_paste_spot_variable_name( spot, value, layerManipulator)
			|| can_paste_class_variable_name( spot, value, layerManipulator)
			|| can_paste_exchange_algebra_name( spot, value, layerManipulator)
//			|| can_paste_any_object_name( spot, value, layerManipulator)	// contains class variables and exchange algebras
			|| can_paste_agent_name( value, layerManipulator)
			|| can_paste_spot_name( value, layerManipulator));
	}

	/**
	 * @param spot
	 * @param value
	 * @param layerManipulator
	 */
	private boolean can_paste_probability_name(String spot, String[] value, ILayerManipulator layerManipulator) {
		return ( ( value[ 1].equals( "double")
			|| value[ 1].equals( "float"))
			&& CommonRuleManipulator.can_paste_object( "probability", spot + value[ 0], layerManipulator));
	}

	/**
	 * @param spot
	 * @param value
	 * @param layerManipulator
	 */
	private boolean can_paste_collection_name(String spot, String[] value, ILayerManipulator layerManipulator) {
		return ( ( value[ 1].equals( "java.util.Collection")
			|| value[ 1].equals( "java.util.HashSet")
			|| value[ 1].equals( "java.util.Set"))
			&& CommonRuleManipulator.can_paste_object( "collection", spot + value[ 0], layerManipulator));
	}

	/**
	 * @param spot
	 * @param value
	 * @param layerManipulator
	 */
	private boolean can_paste_list_name(String spot, String[] value, ILayerManipulator layerManipulator) {
		return ( ( value[ 1].equals( "java.util.Collection")
			|| value[ 1].equals( "java.util.LinkedList")
			|| value[ 1].equals( "java.util.List"))
			&& CommonRuleManipulator.can_paste_object( "list", spot + value[ 0], layerManipulator));
	}

	/**
	 * @param spot
	 * @param value
	 * @param layerManipulator
	 */
	private boolean can_paste_map_name(String spot, String[] value, ILayerManipulator layerManipulator) {
		return ( ( value[ 1].equals( "java.util.HashMap")
			|| value[ 1].equals( "java.util.Map"))
			&& CommonRuleManipulator.can_paste_object( "map", spot + value[ 0], layerManipulator));
	}

	/**
	 * @param spot
	 * @param value
	 * @param layerManipulator
	 */
	private boolean can_paste_keyword_name(String spot, String[] value, ILayerManipulator layerManipulator) {
		return ( ( value[ 1].equals( "boolean")
			|| value[ 1].equals( "java.lang.String"))
			&& ( ( value[ 0].startsWith( "\"") && value[ 0].endsWith( "\""))
			|| CommonRuleManipulator.can_paste_object( "keyword", spot + value[ 0], layerManipulator)));
	}

	/**
	 * @param spot
	 * @param value
	 * @param layerManipulator
	 */
	private boolean can_paste_number_object_name(String spot, String[] value, ILayerManipulator layerManipulator) {
		return ( ( value[ 1].equals( "int")
			|| value[ 1].equals( "byte")
			|| value[ 1].equals( "short")
			|| value[ 1].equals( "long")
			|| value[ 1].equals( "double")
			|| value[ 1].equals( "float"))
			&& CommonRuleManipulator.can_paste_object( "number object", spot + value[ 0], layerManipulator));
	}

	/**
	 * @param spot
	 * @param value
	 * @param layerManipulator
	 * @return
	 */
	private boolean can_paste_file_name(String spot, String[] value, ILayerManipulator layerManipulator) {
		return ( value[ 1].equals( "java.lang.String")
			&& CommonRuleManipulator.can_paste_object( "file", spot + value[ 0], layerManipulator));
	}

	/**
	 * @param spot
	 * @param value
	 * @param layerManipulator
	 * @return
	 */
	private boolean can_paste_role_variable_name(String spot, String[] value, ILayerManipulator layerManipulator) {
		// TODO Auto-generated method stub
		return ( !value[ 1].equals( "int")
			&& !value[ 1].equals( "byte")
			&& !value[ 1].equals( "short")
			&& !value[ 1].equals( "long")
			&& !value[ 1].equals( "double")
			&& !value[ 1].equals( "float")
			&& !value[ 1].equals( "java.util.Collection")
			&& !value[ 1].equals( "java.util.HashSet")
			&& !value[ 1].equals( "java.util.Set")
			&& !value[ 1].equals( "java.util.LinkedList")
			&& !value[ 1].equals( "java.util.List")
			&& !value[ 1].equals( "java.util.HashMap")
			&& !value[ 1].equals( "java.util.Map")
			&& !value[ 1].equals( "boolean")
			&& !value[ 1].equals( "java.lang.String")
			&& !value[ 1].equals( "env.Agent")
			&& !value[ 1].equals( "env.Spot")
			&& CommonRuleManipulator.can_paste_object( "role variable", spot + value[ 0], layerManipulator));
	}

	/**
	 * @param spot
	 * @param value
	 * @param layerManipulator
	 * @return
	 */
	private boolean can_paste_time_variable_name(String spot, String[] value, ILayerManipulator layerManipulator) {
		// TODO Auto-generated method stub
		return ( !value[ 1].equals( "int")
			&& !value[ 1].equals( "byte")
			&& !value[ 1].equals( "short")
			&& !value[ 1].equals( "long")
			&& !value[ 1].equals( "double")
			&& !value[ 1].equals( "float")
			&& !value[ 1].equals( "java.util.Collection")
			&& !value[ 1].equals( "java.util.HashSet")
			&& !value[ 1].equals( "java.util.Set")
			&& !value[ 1].equals( "java.util.LinkedList")
			&& !value[ 1].equals( "java.util.List")
			&& !value[ 1].equals( "java.util.HashMap")
			&& !value[ 1].equals( "java.util.Map")
			&& !value[ 1].equals( "boolean")
			&& !value[ 1].equals( "java.lang.String")
			&& !value[ 1].equals( "env.Agent")
			&& !value[ 1].equals( "env.Spot")
			&& CommonRuleManipulator.can_paste_object( "time variable", spot + value[ 0], layerManipulator));
	}

	/**
	 * @param spot
	 * @param value
	 * @param layerManipulator
	 * @return
	 */
	private boolean can_paste_spot_variable_name(String spot, String[] value, ILayerManipulator layerManipulator) {
		// TODO Auto-generated method stub
		return ( !value[ 1].equals( "int")
			&& !value[ 1].equals( "byte")
			&& !value[ 1].equals( "short")
			&& !value[ 1].equals( "long")
			&& !value[ 1].equals( "double")
			&& !value[ 1].equals( "float")
			&& !value[ 1].equals( "java.util.Collection")
			&& !value[ 1].equals( "java.util.HashSet")
			&& !value[ 1].equals( "java.util.Set")
			&& !value[ 1].equals( "java.util.LinkedList")
			&& !value[ 1].equals( "java.util.List")
			&& !value[ 1].equals( "java.util.HashMap")
			&& !value[ 1].equals( "java.util.Map")
			&& !value[ 1].equals( "boolean")
			&& !value[ 1].equals( "java.lang.String")
			&& !value[ 1].equals( "env.Agent")
			&& !value[ 1].equals( "env.Spot")
			&& CommonRuleManipulator.can_paste_object( "spot variable", spot + value[ 0], layerManipulator));
	}

	/**
	 * @param spot
	 * @param value
	 * @param layerManipulator
	 */
	private boolean can_paste_class_variable_name(String spot, String[] value, ILayerManipulator layerManipulator) {
		return ( !value[ 1].equals( "int")
			&& !value[ 1].equals( "byte")
			&& !value[ 1].equals( "short")
			&& !value[ 1].equals( "long")
			&& !value[ 1].equals( "double")
			&& !value[ 1].equals( "float")
			&& !value[ 1].equals( "java.util.Collection")
			&& !value[ 1].equals( "java.util.HashSet")
			&& !value[ 1].equals( "java.util.Set")
			&& !value[ 1].equals( "java.util.LinkedList")
			&& !value[ 1].equals( "java.util.List")
			&& !value[ 1].equals( "java.util.HashMap")
			&& !value[ 1].equals( "java.util.Map")
			&& !value[ 1].equals( "boolean")
			&& !value[ 1].equals( "java.lang.String")
			&& !value[ 1].equals( "env.Agent")
			&& !value[ 1].equals( "env.Spot")
			&& CommonRuleManipulator.can_paste_object( "class variable", spot + value[ 0], layerManipulator));
	}

	/**
	 * @param spot
	 * @param value
	 * @param layerManipulator
	 */
	private boolean can_paste_exchange_algebra_name(String spot, String[] value, ILayerManipulator layerManipulator) {
		return ( !value[ 1].equals( "int")
			&& !value[ 1].equals( "byte")
			&& !value[ 1].equals( "short")
			&& !value[ 1].equals( "long")
			&& !value[ 1].equals( "double")
			&& !value[ 1].equals( "float")
			&& !value[ 1].equals( "java.util.Collection")
			&& !value[ 1].equals( "java.util.HashSet")
			&& !value[ 1].equals( "java.util.Set")
			&& !value[ 1].equals( "java.util.LinkedList")
			&& !value[ 1].equals( "java.util.List")
			&& !value[ 1].equals( "java.util.HashMap")
			&& !value[ 1].equals( "java.util.Map")
			&& !value[ 1].equals( "boolean")
			&& !value[ 1].equals( "java.lang.String")
			&& !value[ 1].equals( "env.Agent")
			&& !value[ 1].equals( "env.Spot")
			&& CommonRuleManipulator.can_paste_object( "exchange algebra", spot + value[ 0], layerManipulator));
	}

//	/**
//	 * @param spot
//	 * @param value
//	 * @param layerManipulator
//	 * @return
//	 */
//	private boolean can_paste_any_object_name(String spot, String[] value, ILayerManipulator layerManipulator) {
//		return ( !value[ 1].equals( "int")
//			&& !value[ 1].equals( "byte")
//			&& !value[ 1].equals( "short")
//			&& !value[ 1].equals( "long")
//			&& !value[ 1].equals( "double")
//			&& !value[ 1].equals( "float")
//			&& !value[ 1].equals( "java.util.Collection")
//			&& !value[ 1].equals( "java.util.HashSet")
//			&& !value[ 1].equals( "java.util.Set")
//			&& !value[ 1].equals( "java.util.LinkedList")
//			&& !value[ 1].equals( "java.util.List")
//			&& !value[ 1].equals( "java.util.HashMap")
//			&& !value[ 1].equals( "java.util.Map")
//			&& !value[ 1].equals( "boolean")
//			&& !value[ 1].equals( "java.lang.String")
//			&& !value[ 1].equals( "env.Agent")
//			&& !value[ 1].equals( "env.Spot"));
//	}

	/**
	 * @param value
	 * @param layerManipulator
	 * @return
	 */
	private boolean can_paste_agent_name(String[] value, ILayerManipulator layerManipulator) {
		// TODO Auto-generated method stub
		return ( value[ 1].equals( "env.Agent")
			&& ( value[ 0].equals( "") || ( null != layerManipulator.get_agent_has_this_name( value[ 0]))));
	}

	/**
	 * @param value
	 * @param layerManipulator
	 * @return
	 */
	private boolean can_paste_spot_name(String[] value, ILayerManipulator layerManipulator) {
		// TODO Auto-generated method stub
		return ( value[ 1].equals( "env.Spot")
			&& ( value[ 0].equals( "") || ( null != layerManipulator.get_spot_has_this_name( value[ 0]))));
	}

	/**
	 * 
	 */
	public void print() {
		System.out.println( CommonRuleManipulator.get_full_prefix( _spots));
		System.out.println( _classVariable);
		System.out.println( _method);
		if ( null != _returnValue)
			System.out.println( _returnValue[ 1] + " - " + _returnValue[ 0]);
		for ( int i = 0; i < _parameters.length; ++i)
			System.out.println( _parameters[ i][ 1] + " - " + _parameters[ i][ 0]);
	}
}
