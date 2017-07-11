/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.common.functional_object;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import soars.application.visualshell.layer.ILayerManipulator;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.object.entity.base.object.class_variable.ClassVariableObject;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.legacy.common.functional_object.FunctionalObject;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;

/**
 * @author kurata
 *
 */
public class FunctionalObjectRule extends Rule {

	/**
	 * @param value
	 * @return
	 */
	public static FunctionalObject get_functionalObject(String value) {
		return get_functionalObject( value, LayerManager.get_instance());
	}

	/**
	 * @param value
	 * @param layerManipulator
	 * @return
	 */
	private static FunctionalObject get_functionalObject(String value, ILayerManipulator layerManipulator) {
		String[] parts = value.split( " return: ");
		if ( null == parts || 0 == parts.length)
			return null;

		String[] elements = parts[ 0].split( " ");
		if ( null == elements || 2 > elements.length)
			return null;

		String spot, classVariable;
		String[] values = CommonRuleManipulator.get_spot_and_object( elements[ 0], layerManipulator);
		if ( null == values)
			return null;

		String[] spots = new String[] { values[ 0], values[ 1]};
		classVariable = values[ 2];

		String method = elements[ 1];

		List<String[]> parameters = new ArrayList<String[]>();
		if ( 2 < elements.length) {
			parts[ 0] = ( parts[ 0].substring( ( elements[ 0] + " " + elements[ 1] + " ").length()) + " ");
			String[] parameter = new String[] { "", ""};
			String word = "";
			boolean literal = false;
			for ( int i = 0; i < parts[ 0].length(); ++i) {
				char c = parts[ 0].charAt( i);
				if ( '\"' == c) {
					word += c;
					literal = !literal;
				} else if ( ' ' == c) {
					if ( literal)
						word += c;
					else {
						parameter[ 1] = word;
						parameters.add( new String[] { parameter[ 0], parameter[ 1]});
						word = parameter[ 0] = parameter[ 1] = "";
					}
				} else if ( '=' == c) {
					if ( literal)
						word += c;
					else {
						parameter[ 0] = word;
						word = "";
					}
				} else
					word += c;
			}
		}

//		List<String[]> parameters = new ArrayList<String[]>();
//		for ( int i = 2; i < elements.length; ++i) {
//			String[] parameter = elements[ i].split( "=");
//			if ( null == parameter || 2 > parameter.length)
//				return null;
//
//			parameters.add( parameter);
//		}

		String[] returnValue = null;
		if ( 2 <= parts.length) {
			returnValue = parts[ 1].split( "=");
			if ( null == returnValue || 2 > returnValue.length)
				return null;
		}

		return new FunctionalObject( spots, classVariable, method, parameters, returnValue);
	}

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public FunctionalObjectRule(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_agent_names()
	 */
	@Override
	protected String[] get_used_agent_names() {
		// TODO Auto-generated method stub
		if ( !Environment.get_instance().is_functional_object_enable())
			return null;

		FunctionalObject functionalObject = get_functionalObject( _value);
		if ( null == functionalObject)
			return null;

		return functionalObject.get_object_names( "agent");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_names()
	 */
	@Override
	protected String[] get_used_spot_names() {
		// TODO Auto-generated method stub
		if ( !Environment.get_instance().is_functional_object_enable())
			return null;

		FunctionalObject functionalObject = get_functionalObject( _value);
		if ( null == functionalObject)
			return null;

		String spot = ( ( null != functionalObject._spots && null != functionalObject._spots[ 0] && !functionalObject._spots[ 0].equals( "")) ? functionalObject._spots[ 0] : null);

		String[] spots = functionalObject.get_object_names( "spot");

		if ( null == spot)
			return spots;
		else {
			if ( null == spots)
				return new String[] { spot};
			else {
				String[] result = new String[ 1 + spots.length];
				result[ 0] = spot;
				System.arraycopy( spots, 0, result, 1, spots.length);
				return result;
			}
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_spot_variable_names(Role role) {
		// TODO Auto-generated method stub
		if ( !Environment.get_instance().is_functional_object_enable())
			return null;

		FunctionalObject functionalObject = get_functionalObject( _value);
		if ( null == functionalObject)
			return null;

		String spotVariable = CommonRuleManipulator.get_spot_variable_name3( functionalObject._spots);

		String[] spotVariables = functionalObject.get_object_names( "spot variable");

		if ( null == spotVariable)
			return spotVariables;
		else {
			if ( null == spotVariables)
				return new String[] { spotVariable};
			else {
				String[] result = new String[ 1 + spotVariables.length];
				result[ 0] = spotVariable;
				System.arraycopy( spotVariables, 0, result, 1, spotVariables.length);
				return result;
			}
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_probability_names()
	 */
	@Override
	protected String[] get_used_probability_names() {
		return get_used_object_names( "probability");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_collection_names()
	 */
	@Override
	protected String[] get_used_collection_names() {
		return get_used_object_names( "collection");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_list_names()
	 */
	@Override
	protected String[] get_used_list_names() {
		return get_used_object_names( "list");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_map_names()
	 */
	@Override
	protected String[] get_used_map_names() {
		return get_used_object_names( "map");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_keyword_names()
	 */
	@Override
	protected String[] get_used_keyword_names() {
		return get_used_object_names( "keyword");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_number_object_names()
	 */
	@Override
	protected String[] get_used_number_object_names() {
		return get_used_object_names( "number object");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_file_names()
	 */
	@Override
	protected String[] get_used_file_names() {
		return get_used_object_names( "file");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_role_variable_names()
	 */
	@Override
	protected String[] get_used_role_variable_names() {
		return get_used_object_names( "role variable");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_time_variable_names()
	 */
	@Override
	protected String[] get_used_time_variable_names() {
		return get_used_object_names( "time variable");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_class_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_class_variable_names(Role role) {
		return get_used_object_names( "class variable");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_exchange_algebra_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_exchange_algebra_names(Role role) {
		return get_used_object_names( "exchange algebra");
	}

	/**
	 * @param type
	 * @return
	 */
	private String[] get_used_object_names(String type) {
		if ( !Environment.get_instance().is_functional_object_enable())
			return null;

		FunctionalObject functionalObject = get_functionalObject( _value);
		if ( null == functionalObject)
			return null;

		return functionalObject.get_object_names( type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_agent_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_agent_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		// TODO Auto-generated method stub
		if ( !Environment.get_instance().is_functional_object_enable())
			return false;

		FunctionalObject functionalObject = get_functionalObject( _value);
		if ( null == functionalObject)
			return false;

		return functionalObject.update_agent_name( newName, originalName, headName, ranges, newHeadName, newRanges, this);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		// TODO Auto-generated method stub
		if ( !Environment.get_instance().is_functional_object_enable())
			return false;

		FunctionalObject functionalObject = get_functionalObject( _value);
		if ( null == functionalObject)
			return false;

		return functionalObject.update_spot_name( newName, originalName, headName, ranges, newHeadName, newRanges, this);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_spot_variable_name(String name, String newName, String type, Role role) {
		if ( !Environment.get_instance().is_functional_object_enable())
			return false;

		FunctionalObject functionalObject = get_functionalObject( _value);
		if ( null == functionalObject)
			return false;

		return functionalObject.update_spot_variable_name( name, newName, type, this);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_probability_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_probability_name(String name, String newName, String type, Role role) {
		return update_object_name( "probability", name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_collection_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_collection_name(String name, String newName, String type, Role role) {
		return update_object_name( "collection", name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_list_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_list_name(String name, String newName, String type, Role role) {
		return update_object_name( "list", name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_map_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_map_name(String name, String newName, String type, Role role) {
		return update_object_name( "map", name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_keyword_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_keyword_name(String name, String newName, String type, Role role) {
		return update_object_name( "keyword", name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_number_object_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_number_object_name(String name, String newName, String type, Role role) {
		return update_object_name( "number object", name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_file_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_file_name(String name, String newName, String type, Role role) {
		return update_object_name( "file", name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_class_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_role_variable_name(String name, String newName, String type, Role role) {
		return update_object_name( "role variable", name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_class_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_time_variable_name(String name, String newName, String type, Role role) {
		return update_object_name( "time variable", name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_class_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_class_variable_name(String name, String newName, String type, Role role) {
		return update_object_name( "class variable", name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_exchange_algebra_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_exchange_algebra_name(String name, String newName, String type, Role role) {
		return update_object_name( "exchange algebra", name, newName, type);
	}

	/**
	 * @param kind
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private boolean update_object_name(String kind, String name, String newName, String type) {
		if ( !Environment.get_instance().is_functional_object_enable())
			return false;

		FunctionalObject functionalObject = get_functionalObject( _value);
		if ( null == functionalObject)
			return false;

		return functionalObject.update_object_name( kind, name, newName, type, this);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	@Override
	protected boolean can_paste(Role role, Layer drawObjects) {
		// TODO Auto-generated method stub
		if ( !Environment.get_instance().is_functional_object_enable())
			return false;

		FunctionalObject functionalObject = get_functionalObject( _value, drawObjects);
		if ( null == functionalObject)
			return false;

		if ( !CommonRuleManipulator.can_paste_spot_and_spot_variable_name( functionalObject._spots, drawObjects))
			return false;

		if ( !functionalObject.is_correct( drawObjects))
			return false;

		return functionalObject.can_paste_object_names( drawObjects);
	}

	/**
	 * @param value
	 * @param role
	 * @param separator
	 * @return
	 */
	protected String get_script(String value, Role role, String separator) {
		if ( !Environment.get_instance().is_functional_object_enable())
			return "";

		FunctionalObject functionalObject = get_functionalObject( value);
		if ( null == functionalObject)
			return "";

		ClassVariableObject classVariableObject = LayerManager.get_instance().get_class_variable( functionalObject);
		if ( null == classVariableObject) {
			if ( CommonRuleManipulator.is_object( "exchange algebra", CommonRuleManipulator.get_full_prefix( functionalObject._spots) + functionalObject._classVariable))
				// TODO もし交換代数なら、、、
				classVariableObject = new ClassVariableObject( functionalObject._classVariable, Constant._exchangeAlgebraJarFilename, Constant._exchangeAlgebraClassname);
			else
				return "";
		}

		String[] words = classVariableObject._classname.split( "\\.");
		if ( null == words || 1 > words.length)
			return "";

		//functionalObject.print();

		//String spot1 = CommonRuleManipulator.get_spot_name4( functionalObject._spots);

		String spot = CommonRuleManipulator.get_full_prefix( functionalObject._spots);

		String script = "";

		for ( int i = 0; i < functionalObject._parameters.length; ++i) {
			script += ( script.equals( "") ? "" : separator);
			script += ( ( CommonRuleManipulator.is_object( "keyword", spot + functionalObject._parameters[ i][ 0], LayerManager.get_instance())
					|| CommonRuleManipulator.is_object( "file", spot + functionalObject._parameters[ i][ 0], LayerManager.get_instance()))
				? ( spot + "equip " + functionalObject._parameters[ i][ 0] + separator)
				: "");
			script += spot;
			if ( functionalObject._parameters[ i][ 1].equals( "boolean"))
				script += ( "addParamBoolean "+ functionalObject._classVariable + "=" + functionalObject._parameters[ i][ 0]);
			else if ( functionalObject._parameters[ i][ 1].equals( "java.lang.String") && functionalObject._parameters[ i][ 0].startsWith( "\"") && functionalObject._parameters[ i][ 0].endsWith( "\""))
				script += ( "addParamString "+ functionalObject._classVariable + "=" + functionalObject._parameters[ i][ 0].substring( 1, functionalObject._parameters[ i][ 0].length() - 1));
			else if ( functionalObject._parameters[ i][ 1].equals( "env.Agent"))
				// TODO Auto-generated method stub
				script += ( "addParamAgent "+ functionalObject._classVariable
					+ ( functionalObject._parameters[ i][ 0].equals( "") ? "" : ( "=" + functionalObject._parameters[ i][ 0])));
			else if ( functionalObject._parameters[ i][ 1].equals( "env.Spot"))
				// TODO Auto-generated method stub
				script += ( "addParamSpot "+ functionalObject._classVariable
					+ ( functionalObject._parameters[ i][ 0].equals( "") ? "" : ( "=" + functionalObject._parameters[ i][ 0])));
			else {
				// TODO Auto-generated method stub
				if ( CommonRuleManipulator.is_object( "time variable", spot + functionalObject._parameters[ i][ 0], LayerManager.get_instance()))
					script += ( "addParam " + functionalObject._classVariable + "=$Time." + functionalObject._parameters[ i][ 0] + "=" + functionalObject._parameters[ i][ 1]);
				else if ( CommonRuleManipulator.is_object( "role variable", spot + functionalObject._parameters[ i][ 0], LayerManager.get_instance()))
					script += ( "addParam " + functionalObject._classVariable + "=$Role." + functionalObject._parameters[ i][ 0] + "=" + functionalObject._parameters[ i][ 1]);
				else
					script += ( "addParam " + functionalObject._classVariable + "=" + functionalObject._parameters[ i][ 0] + "=" + functionalObject._parameters[ i][ 1]);
			}
		}

		script += ( script.equals( "") ? "" : separator);

		if ( functionalObject._method.equals( words[ words.length - 1]))
			script += ( spot + "newInstance "
				+ functionalObject._classVariable + "=" + functionalObject._classVariable);
		else {
//			script += ( spot + "invokeClass "
//				+ ( ( null == functionalObject._returnValue || functionalObject._returnValue[ 0].equals( "")) ? "" : ( functionalObject._returnValue[ 0] + "="))
//				+ functionalObject._classVariable + "=" + functionalObject._method);
			script += ( spot + "invokeClass ");
			if ( null != functionalObject._returnValue && !functionalObject._returnValue[ 0].equals( "")) {
				if ( CommonRuleManipulator.is_object( "time variable", spot + functionalObject._returnValue[ 0], LayerManager.get_instance()))
					script += "$Time.";
				else if ( CommonRuleManipulator.is_object( "role variable", spot + functionalObject._returnValue[ 0], LayerManager.get_instance()))
					script += "$Role.";

				script += ( functionalObject._returnValue[ 0] + "=");
			}
			script += ( functionalObject._classVariable + "=" + functionalObject._method);
			script += ( ( ( null != functionalObject._returnValue)
				&& ( CommonRuleManipulator.is_object( "keyword", spot + functionalObject._returnValue[ 0], LayerManager.get_instance())
					|| CommonRuleManipulator.is_object( "file", spot + functionalObject._returnValue[ 0], LayerManager.get_instance())))
				? (  separator + spot + "printEquip " + functionalObject._returnValue[ 0])
				: "");
		}

		return script;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_cell_text(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public String get_cell_text(Role role) {
		String[] parts = _value.split( " return: ");
		if ( null == parts || 0 == parts.length)
			return "";

		String[] elements = parts[ 0].split( " ");
		if ( null == elements || 2 > elements.length)
			return "";

		String result = ( elements[ 0] + "." + elements[ 1] + "(");

		if ( 2 < elements.length) {
			parts[ 0] = parts[ 0].substring( ( elements[ 0] + " " + elements[ 1] + " ").length());
			String text = "";
			String word = "";
			boolean literal = false;
			for ( int i = 0; i < parts[ 0].length(); ++i) {
				char c = parts[ 0].charAt( i);
				if ( '\"' == c) {
					word += c;
					literal = !literal;
				} else if ( ' ' == c) {
					if ( literal)
						word += c;
					else
						word = "";
				} else if ( '=' == c) {
					if ( literal)
						word += c;
					else {
						text += ( text.equals( "") ? "" : ", ");
						text += ( word.equals( "") ? "\"\"" : word);
						word = "";
					}
				} else
					word += c;
			}
			result += text;
		}

//		for ( int i = 2; i < elements.length; ++i) {
//			result += ( ( 2 == i) ? "" : ", ");
//			String[] words = elements[ i].split( "=");
//			if ( null == words || 2 > words.length)
//				return "";
//
//			result += ( words[ 0].equals( "") ? "\"\"" : words[ 0]);
//		}

		result += ")";

		if ( 2 <= parts.length) {
			String[] words = parts[ 1].split( "=");
			if ( null == words || 2 > words.length)
				return "";

			result = ( words[ 0].equals( "") ? result : ( words[ 0] + "=" + result));
		}

		return result;
	}
}
