/**
 * 
 */
package soars.application.visualshell.object.role.base.object.common;

import java.util.Vector;

import soars.application.visualshell.layer.ILayerManipulator;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.role.base.object.generic.element.EntityVariableRule;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 *
 */
public class CommonRuleManipulatorNew {

	/**
	 * @param element
	 * @return
	 */
	public static String[] get_entity_and_value(String element) {
		return get_entity_and_value( element, LayerManager.get_instance());
	}

	/**
	 * @param element
	 * @param layerManipulator
	 * @return
	 */
	public static String[] get_entity_and_value(String element, ILayerManipulator layerManipulator) {
		if ( null == element || element.equals( ""))
			return null;

		element = ( element.startsWith( "!") ? element.substring( "!".length()) : element);
		if ( element.equals( ""))
			return null;

		if ( !element.startsWith( "<"))
			return new String[] { "", null, "", "", element};	// "value"

		String[] words = Tool.split( element, '>');
		if ( null == words || 2 > words.length)
			return null;		// error!

		words[ 0] = words[ 0].substring( "<".length());

		String value = words[ 1];

		words = words[ 0].split( ":");

		if ( 2 == words.length) {
			if ( words[ 0].equals( "")) { 
				if ( layerManipulator.is_spot_object_name( "spot variable", words[ 1]))
					return new String[] { null, "", "", words[ 1], value};	// "<:sv>value"
				else if ( layerManipulator.is_spot_object_name( "agent variable", words[ 1]))
					return new String[] { null, "", words[ 1], "", value};	// "<:av>value"
			} else { 
				if ( null != layerManipulator.get_agent_has_this_name( words[ 0])) {
					if ( layerManipulator.is_agent_object_name( "spot variable", words[ 1]))
						return new String[] { words[ 0], null, "", words[ 1], value};	// "<agent:sv>value"
					else if ( layerManipulator.is_agent_object_name( "agent variable", words[ 1]))
						return new String[] { words[ 0], null, words[ 1], "", value};	// "<agent:av>value"
				} else if ( null != layerManipulator.get_spot_has_this_name( words[ 0])) {
					if ( layerManipulator.is_spot_object_name( "spot variable", words[ 1]))
						return new String[] { null, words[ 0], "", words[ 1], value};	// "<spot:sv>value"
					else if ( layerManipulator.is_spot_object_name( "agent variable", words[ 1]))
						return new String[] { null, words[ 0], words[ 1], "", value};	// "<spot:av>value"
				}
			}
		} else if ( 1 == words.length) {
			if ( words[ 0].equals( ""))
				return new String[] { null, "", "", "", value};	// "<>value"
			else {
				if ( null != layerManipulator.get_agent_has_this_name( words[ 0]))
					return new String[] { words[ 0], null, "", "", value};	// "<agent>value"
				else if ( null != layerManipulator.get_spot_has_this_name( words[ 0]))
					return new String[] { null, words[ 0], "", "", value};	// "<spot>value"
				else if ( layerManipulator.is_agent_object_name( "spot variable", words[ 0]))
					return new String[] { "", null, "", words[ 0], value};	// "<sv>value"
				else if ( layerManipulator.is_agent_object_name( "agent variable", words[ 0]))
					return new String[] { "", null, words[ 0], "", value};	// "<av>value"
			}
		}

		return null;
	}

	/**
	 * @param element
	 * @param variableType
	 * @return
	 */
	public static EntityVariableRule get_entityVariableRule(String element, String variableType) {
		// TODO 2014.2.12
		return get_entityVariableRule( element, variableType, LayerManager.get_instance());
	}

	/**
	 * @param element
	 * @param layerManipulator
	 * @return
	 */

	public static EntityVariableRule get_entityVariableRule(String element, String variableType, ILayerManipulator layerManipulator) {
		// TODO 2014.2.12
		if ( null == element || element.equals( ""))
			return null;

		element = ( element.startsWith( "!") ? element.substring( "!".length()) : element);
		if ( element.equals( ""))
			return null;

		if ( !element.startsWith( "<"))
			return new EntityVariableRule( "self", "", "", "", variableType, element);	// "value"

		String[] words = Tool.split( element, '>');
		if ( null == words || 2 > words.length)
			return null;		// error!

		words[ 0] = words[ 0].substring( "<".length());

		String value = words[ 1];

		words = words[ 0].split( ":");

		if ( 2 == words.length) {
			if ( words[ 0].equals( "")) { 
				if ( layerManipulator.is_spot_object_name( "spot variable", words[ 1]))
					return new EntityVariableRule( "currentspot", "", "", words[ 1], variableType, value);	// "<:sv>value"
				else if ( layerManipulator.is_spot_object_name( "agent variable", words[ 1]))
					return new EntityVariableRule( "currentspot", "", words[ 1], "", variableType, value);	// "<:av>value"
			} else { 
				if ( null != layerManipulator.get_agent_has_this_name( words[ 0])) {
					if ( layerManipulator.is_agent_object_name( "spot variable", words[ 1]))
						return new EntityVariableRule( "agent", words[ 0], "", words[ 1], variableType, value);	// "<agent:sv>value"
					else if ( layerManipulator.is_agent_object_name( "agent variable", words[ 1]))
						return new EntityVariableRule( "agent", words[ 0], words[ 1], "", variableType, value);	// "<agent:av>value"
				} else if ( null != layerManipulator.get_spot_has_this_name( words[ 0])) {
					if ( layerManipulator.is_spot_object_name( "spot variable", words[ 1]))
						return new EntityVariableRule( "spot", words[ 0], "", words[ 1], variableType, value);	// "<spot:sv>value"
					else if ( layerManipulator.is_spot_object_name( "agent variable", words[ 1]))
						return new EntityVariableRule( "spot", words[ 0], words[ 1], "", variableType, value);	// "<spot:av>value"
				}
			}
		} else if ( 1 == words.length) {
			if ( words[ 0].equals( ""))
				return new EntityVariableRule( "currentspot", "", "", "", variableType, value);	// "<>value"
			else {
				if ( null != layerManipulator.get_agent_has_this_name( words[ 0]))
					return new EntityVariableRule( "agent", words[ 0], "", "", variableType, value);	// "<agent>value"
				else if ( null != layerManipulator.get_spot_has_this_name( words[ 0]))
					return new EntityVariableRule( "spot", words[ 0], "", "", variableType, value);	// "<spot>value"
				else if ( layerManipulator.is_agent_object_name( "spot variable", words[ 0]))
					return new EntityVariableRule( "self", "", "", words[ 0], variableType, value);	// "<sv>value"
				else if ( layerManipulator.is_agent_object_name( "agent variable", words[ 0]))
					return new EntityVariableRule( "self", "", words[ 0], "", variableType, value);	// "<av>value"
			}
		}

		return null;
	}

	/**
	 * @param kind
	 * @param values
	 * @param object
	 * @return
	 */
	public static boolean is_object(String kind, String[] values, String object) {
		return is_object( kind, values, object, LayerManager.get_instance());
	}

	/**
	 * @param kind
	 * @param values
	 * @param object
	 * @param layerManipulator
	 * @return
	 */
	public static boolean is_object(String kind, String[] values, String object, ILayerManipulator layerManipulator) {
		if ( null == values || null == object || object.equals( ""))
			return false;

		if ( null != values[ 0]) {
			if ( null != values[ 1])
				return false;

			if ( values[ 0].equals( "")) {
				if ( !values[ 3].equals( ""))
					return layerManipulator.is_spot_object_name( kind, object);
				else
					return layerManipulator.is_agent_object_name( kind, object);
			} else {
				if ( !values[ 2].equals( ""))
					return layerManipulator.is_agent_object_name( kind, object);
				else {
					if ( !values[ 3].equals( ""))
						return layerManipulator.is_spot_object_name( kind, object);
					else
						return layerManipulator.is_agent_object_name( kind, values[ 0], object);
				}
			}
		} else if ( null != values[ 1]) {
			if ( null != values[ 0])
				return false;

			if ( values[ 1].equals( "")) {
				if ( !values[ 2].equals( ""))
					return layerManipulator.is_agent_object_name( kind, object);
				else
					return layerManipulator.is_spot_object_name( kind, object);
			} else {
				if ( !values[ 3].equals( ""))
					return layerManipulator.is_spot_object_name( kind, object);
				else {
					if ( !values[ 2].equals( ""))
						return layerManipulator.is_agent_object_name( kind, object);
					else
						return layerManipulator.is_spot_object_name( kind, values[ 1], object);
				}
			}
		}

		return false;
	}

	/**
	 * @param values
	 * @param numberObjectName
	 * @param numberObjectType
	 * @return
	 */
	public static boolean is_number_object(String[] values, String numberObjectName, String numberObjectType) {
		return is_number_object( values, numberObjectName, numberObjectType, LayerManager.get_instance());
	}

	/**
	 * @param values
	 * @param numberObjectName
	 * @param numberObjectType
	 * @param layerManipulator
	 * @return
	 */
	public static boolean is_number_object(String[] values, String numberObjectName, String numberObjectType, ILayerManipulator layerManipulator) {
		if ( null != values[ 0]) {
			if ( null != values[ 1])
				return false;

			if ( values[ 0].equals( "")) {
				if ( !values[ 3].equals( ""))
					return layerManipulator.is_spot_number_object_name( numberObjectName, numberObjectType);
				else
					return layerManipulator.is_agent_number_object_name( numberObjectName, numberObjectType);
			} else {
				if ( !values[ 2].equals( ""))
					return layerManipulator.is_agent_number_object_name( numberObjectName, numberObjectType);
				else {
					if ( !values[ 3].equals( ""))
						return layerManipulator.is_spot_number_object_name( numberObjectName, numberObjectType);
					else
						return layerManipulator.is_agent_number_object_name( values[ 0], numberObjectName, numberObjectType);
				}
			}
		} else if ( null != values[ 1]) {
			if ( null != values[ 0])
				return false;

			if ( values[ 1].equals( "")) {
				if ( !values[ 2].equals( ""))
					return layerManipulator.is_agent_number_object_name( numberObjectName, numberObjectType);
				else
					return layerManipulator.is_spot_number_object_name( numberObjectName, numberObjectType);
			} else {
				if ( !values[ 3].equals( ""))
					return layerManipulator.is_spot_number_object_name( numberObjectName, numberObjectType);
				else {
					if ( !values[ 2].equals( ""))
						return layerManipulator.is_agent_number_object_name( numberObjectName, numberObjectType);
					else
						return layerManipulator.is_spot_number_object_name( values[ 1], numberObjectName, numberObjectType);
				}
			}
		}

		return false;
	}

	/**
	 * @param values
	 * @return
	 */
	public static String get_spot_variable_name(String[] values) {
		return get_spot_variable_name( values, LayerManager.get_instance());
	}

	/**
	 * @param values
	 * @param getInstance
	 * @return
	 */
	public static String get_spot_variable_name(String[] values, ILayerManipulator layerManipulator) {
		if ( values[ 3].equals( ""))
			return null;

		if ( null != values[ 0]) {
			if ( values[ 0].equals( ""))
				return values[ 3];	// "sv"
			else
				return "<" + values[ 0] + ">" + values[ 3];	// "<agent>sv"
		} else if ( null != values[ 1]) {
			return "<" + values[ 1] + ">" + values[ 3];	// "<>sv"、"<spot>sv"
		}

		return null;
	}

	/**
	 * @param element "value", "<av>value", "<sv>value", "<agent>value", "<agent:av>value", "<agent:sv>value", "<>value", "<:av>value", "<:sv>value", "<spot>value", "<spot:av>value", "<spot:sv>value"
	 * @param entityType
	 * @param objectName
	 * @param otherEntitiesHaveThisObjectName
	 * @param headName
	 * @param ranges
	 * @return
	 */
	public static boolean correspond(String element, String entityType, String objectName, boolean otherEntitiesHaveThisObjectName, String headName, Vector<String[]> ranges) {
		element = get_semantic_element( element);
		if ( null == element)
			return false;

		if ( !element.startsWith( "<")) {	// "value"
			if ( !entityType.equals( "agent"))
				return false;

			if ( otherEntitiesHaveThisObjectName)
				return false;

			return objectName.equals( element);
		} else {
			String[] elements = element.split( ">");
			if ( 2 != elements.length)
					return false;

			if ( elements[ 0].equals( "<")) {	// "<>value"
				if ( !entityType.equals( "spot"))
					return false;

				if ( otherEntitiesHaveThisObjectName)
					return false;

				return objectName.equals( elements[ 1]);
			} else {	// "<agent>value"、"<spot>value"
				String entityTypeName = elements[ 0].substring( "<".length());
				if ( null == entityTypeName)
					return false;

				if ( !SoarsCommonTool.has_same_name( headName, ranges, entityTypeName))
					return false;

				return objectName.equals( elements[ 1]);
			}
		}
	}

	/**
	 * @param element
	 * @return
	 */
	public static String get_semantic_element(String element) {
		// "value", "<av>value", "<:av>value", "<agent:av>value", "<spot:av>value" -> "value"
		// "<>value", "<sv>value", "<:sv>value", "<agent:sv>value", "<spot:sv>value" -> "<>value"
		// "<agent>value" -> "<agent>value"
		// "<spot>value" -> "<spot>value"
		String[] values = get_entity_and_value( element);
		if ( null == values)
			return null;

		if ( !values[ 2].equals( ""))
			return values[ 4];

		if ( !values[ 3].equals( ""))
			return ( "<>" + values[ 4]);

		return element;
	}

	/**
	 * @param values
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	public static String update_agent_name(String[] values, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		if ( null == values[ 0] || values[ 0].equals( ""))
			return null;

		String newAgentName = get_new_agent_or_spot_name( values[ 0], newName, originalName, headName, ranges, newHeadName, newRanges);
		if ( null == newAgentName)
			return null;

		values[ 0] = newAgentName;

		return make( values);
	}

	/**
	 * @param values
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	public static String update_spot_name(String[] values, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		if ( null == values[ 1] || values[ 1].equals( ""))
			return null;

		String newSpotName = get_new_agent_or_spot_name( values[ 1], newName, originalName, headName, ranges, newHeadName, newRanges);
		if ( null == newSpotName)
			return null;

		values[ 1] = newSpotName;

		return make( values);
	}

	/**
	 * @param name
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	public static String get_new_agent_or_spot_name(String name, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		if ( null == name || name.equals( ""))
			return null;

		if ( !SoarsCommonTool.has_same_name( headName, ranges, name))
			return null;

		if ( SoarsCommonTool.has_same_name( newHeadName, newRanges, name))
			return null;

		return ( newName + name.substring( originalName.length()));
	}

	/**
	 * @param values
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @return
	 */
	public static String update_spot_variable_name(String[] values, String originalName, String newName, String entityType) {
		if ( ( entityType.equals( "agent") && ( null != values[ 0]))
			|| ( entityType.equals( "spot") && ( null != values[ 1]))) {
			if ( !values[ 3].equals( originalName))
				return null;

			values[ 3] = newName;
			return make( values);
		}

		return null;
	}

	/**
	 * @param values
	 * @param object
	 * @param newName
	 * @param entityType
	 * @return
	 */
	public static boolean can_update_object_name(String[] values, String object, String originalName, String entityType) {
		if ( entityType.equals( "agent")) {
			if ( !values[ 3].equals( ""))
				return false;	// "<sv>value"、"<agent:sv>value"、"<:sv>value"、"<spot:sv>value"

			if ( null == values[ 0] && values[ 2].equals( ""))
				return false;	// "<>value"、"<spot>value"

		} else if ( entityType.equals( "spot")){
			if ( !values[ 2].equals( ""))
				return false;	// "<av>value"、"<agent:av>value"、"<:av>value"、"<spot:av>value"、

			if ( null == values[ 1] && values[ 3].equals( ""))
				return false;	// "value"、"<agent>value"

		} else
			return false;

		return object.equals( originalName);
	}

	/**
	 * @param values
	 * @return
	 */
	public static String make(String[] values) {
		if ( null != values[ 0]) {
			if ( null != values[ 1])
				return null;

			if ( values[ 0].equals( "")) {
				if ( !values[ 2].equals( ""))
					return ( "<" + values[ 2] + ">" + values[ 4]);	// "<av>value"

				if ( !values[ 3].equals( ""))
					return ( "<" + values[ 3] + ">" + values[ 4]);	// "<sv>value"

				return values[ 4];	// "value"
			} else {
				if ( !values[ 2].equals( ""))
					return ( "<" + values[ 0] + ":" + values[ 2] + ">" + values[ 4]);	// "<agent:av>value"

				if ( !values[ 3].equals( ""))
					return ( "<" + values[ 0] + ":" + values[ 3] + ">" + values[ 4]);	// "<agent:sv>value"

				return ( "<" + values[ 0] + ">" + values[ 4]);	// "<agent>value"
			}
		} else if ( null != values[ 1]) {
			if ( null != values[ 0])
				return null;

			if ( values[ 1].equals( "")) {
				if ( !values[ 2].equals( ""))
					return ( "<:" + values[ 2] + ">" + values[ 4]);	// "<:av>value"

				if ( !values[ 3].equals( ""))
					return ( "<:" + values[ 3] + ">" + values[ 4]);	// "<:sv>value"

				return ( "<>" + values[ 4]);	// "<>value"
			} else {
				if ( !values[ 2].equals( ""))
					return ( "<" + values[ 1] + ":" + values[ 2] + ">" + values[ 4]);	// "<spot:av>value"

				if ( !values[ 3].equals( ""))
					return ( "<" + values[ 1] + ":" + values[ 3] + ">" + values[ 4]);	// "<spot:sv>value"

				return ( "<" + values[ 1] + ">" + values[ 4]);	// "<spot>value"
			}
		}

		return null;
	}

	/**
	 * @param elements
	 * @param delimiter
	 * @return
	 */
	public static String concatenate(String[] elements, String delimiter) {
		if ( null == elements)
			return null;

		String text = "";
		for ( int i = 0; i < elements.length; ++i)
			text += ( ( ( 0 == i) ? "" : delimiter) + elements[ i]);

		return text;
	}

	/**
	 * @param values
	 * @param drawObjects
	 * @return
	 */
	public static boolean can_paste_agent_name(String[] values, Layer drawObjects) {
		if ( null == values[ 0] || values[ 0].equals( ""))
			return true;

		return ( null != drawObjects.get_agent_has_this_name( values[ 0]));
	}

	/**
	 * @param values
	 * @param drawObjects
	 * @return
	 */
	public static boolean can_paste_spot_name(String[] values, Layer drawObjects) {
		if ( null == values[ 1] || values[ 1].equals( ""))
			return true;

		return ( null != drawObjects.get_spot_has_this_name( values[ 1]));
	}

	/**
	 * @param values
	 * @param drawObjects
	 * @return
	 */
	public static boolean can_paste_spot_variable_name(String[] values, Layer drawObjects) {
		if ( null == values[ 3] || values[ 3].equals( ""))
			return true;

		if ( null != values[ 0]) {
			if ( null != values[ 1])
				return false;

			if ( values[ 0].equals( ""))
				return drawObjects.is_agent_object_name( "spot variable", values[ 3]);	// "<sv>value"
			else
				return drawObjects.is_agent_object_name( "spot variable", values[ 0], values[ 3]);	// "<agent:sv>value"
		} else if ( null != values[ 1]) {
			if ( null != values[ 0])
				return false;

			if ( values[ 1].equals( ""))
				return drawObjects.is_spot_object_name( "spot variable", values[ 3]);	// "<:sv>value"
			else
				return drawObjects.is_spot_object_name( "spot variable", values[ 1], values[ 3]);	// "<spot:sv>value"
		}

		return false;
	}
}
