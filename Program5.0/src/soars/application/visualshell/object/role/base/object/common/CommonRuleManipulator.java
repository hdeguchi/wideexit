/*
 * Created on 2006/01/31
 */
package soars.application.visualshell.object.role.base.object.common;

import java.util.Vector;

import soars.application.visualshell.layer.ILayerManipulator;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.common.soars.tool.SoarsCommonTool;

/**
 * @author kurata
 */
public class CommonRuleManipulator {

	/**
	 * @param value
	 * @return
	 */
	public static String[] get_elements(String value) {
		return get_elements( value, 1);
	}

	/**
	 * @param value
	 * @param minimumSize
	 * @return
	 */
	public static String[] get_elements(String value, int minimumSize) {
		String[] elements = split( value);
		if ( null == elements)
			return null;

		elements = elements[ 1].split( "=");
		if ( minimumSize > elements.length)
			return null;

		for ( int i = 0; i < elements.length; ++i) {
			if ( elements[ i].equals( ""))
				return null;
		}

		return elements;
	}

	/**
	 * @param element
	 * @return
	 */
	public static String[] get_prefix_and_object(String element) {
		String[] spot_and_object = separate_into_spot_and_object( element);
		if ( null == spot_and_object)
			return new String[] { "", element};
		else {
			if ( spot_and_object[ 0].equals( ""))
				return new String[] { "<>", spot_and_object[ 1]};
			else
				return new String[] { "<" + spot_and_object[ 0] + ">", spot_and_object[ 1]};
		}
	}

	/**
	 * @param element
	 * @return
	 */
	public static String[] separate_into_spot_and_object(String element) {
		if ( null == element || element.equals( ""))
			return null;

		element = ( element.startsWith( "!") ? element.substring( "!".length()) : element);
		if ( element.equals( ""))
			return null;

		if ( !element.startsWith( "<"))
			return null;

		String[] elements = element.split( ">");
		if ( null == elements || 2 != elements.length)
			return null;

		elements[ 0] = elements[ 0].substring( "<".length());

		return elements;
	}

	/**
	 * @param element
	 * @return
	 */
	public static String[] get_spot_and_object(String element) {
		return get_spot_and_object( element, LayerManager.get_instance());
	}

	/**
	 * @param element
	 * @param layerManipulator
	 * @return
	 */
	public static String[] get_spot_and_object(String element, ILayerManipulator layerManipulator) {
		if ( null == element || element.equals( ""))
			return null;

		element = ( element.startsWith( "!") ? element.substring( "!".length()) : element);
		if ( element.equals( ""))
			return null;

		if ( !element.startsWith( "<"))	// "name"
			return new String[] { null, "", element};

		String[] words = element.split( ">");
		if ( null == words || 2 != words.length)
			return null;		// error!

		words[ 0] = words[ 0].substring( "<".length());

		String name = words[ 1];

		words = words[ 0].split( ":");

		if ( 2 == words.length)
			return new String[] { words[ 0], words[ 1], name};	// "<:sv>name", "<spot:sv>name"
		else if ( 1 == words.length) {
			if ( words[ 0].equals( ""))
				return new String[] { "", "", name};	// "<>name"
			else {
				if ( null != layerManipulator.get_spot_has_this_name( words[ 0]))
					return new String[] { words[ 0], "", name};	// "<spot>name"
				else if ( layerManipulator.is_agent_object_name( "spot variable", words[ 0]))
					return new String[] { null, words[ 0], name};	// "<sv>name"
			}
		}

		return null;
	}

	/**
	 * @param value
	 * @return full name
	 */
	public static String get_full_prefix(String value) {		// get_prefix1
		if ( !value.startsWith( "<") && !value.startsWith( "!<"))
			return "";

		if ( value.startsWith( "<>") || value.startsWith( "!<>"))
			return "<>";

		String[] elements = value.split( ">");
		if ( 1 > elements.length)
			return null;

		return ( ( value.startsWith( "!") ? elements[ 0].substring( "!".length()) : elements[ 0]) + ">");
	}

	/**
	 * @param value
	 * @param layerManipulator
	 * @return "", "<>", "spot"
	 */
	public static String get_semantic_prefix(String value) {	// get_spot_name4
		return get_semantic_prefix( value, LayerManager.get_instance());
	}

	/**
	 * @param value
	 * @return "", "<>", "spot"
	 */
	private static String get_semantic_prefix(String value, ILayerManipulator layerManipulator) {		// get_spot_name4
		String[] spots = get_spot( value, layerManipulator);
		if ( null == spots)
			return null;		// error!

		if ( null == spots[ 0] && spots[ 1].equals( ""))
			return "";
		else if ( null != spots[ 0] && !spots[ 0].equals( "") && spots[ 1].equals( ""))
			return spots[ 0];	// "spot"
		else
			return "<>";		// "<>", "sv", ":sv", "spot:sv"
	}

	/**
	 * @param value
	 * @return
	 */
	public static String[] get_spot(String value) {	// get_prefix_spot_name5
		return get_spot( value, LayerManager.get_instance());
	}

	/**
	 * @param value
	 * @param layerManipulator
	 * @return
	 */
	public static String[] get_spot(String value, ILayerManipulator layerManipulator) {	// get_prefix_spot_name5
		String spot = get_prefix( value);
		if ( null == spot)
			return null;		// error!

		if ( spot.equals( ""))
			return new String[] { null, ""};
		else if ( spot.equals( "<>"))
			return new String[] { "", ""};
		else {
			String[] words = spot.split( ":");
			if ( 2 == words.length)
				return new String[] { words[ 0], words[ 1]};	// ":sv", "spot:sv"
			else if ( 1 == words.length) {
				if ( words[ 0].equals( ""))
					return null;		// error!
				else {
					if ( null != layerManipulator.get_spot_has_this_name( words[ 0]))
						return new String[] { words[ 0], ""};		// "spot"
					else if ( layerManipulator.is_agent_object_name( "spot variable", words[ 0]))
						return new String[] { null, words[ 0]};	// "sv"
				}
			}
		}

		return null;		// error!
	}

	/**
	 * @param value
	 * @return
	 */
	private static String get_prefix(String value) {
		if ( !value.startsWith( "<") && !value.startsWith( "!<"))
			return "";

		if ( value.startsWith( "<>") || value.startsWith( "!<>"))
			return "<>";

		String[] elements = value.substring( value.startsWith( "!") ? "!<".length() : "<".length()).split( ">");
		if ( 1 > elements.length)
			return null;

		return elements[ 0];
	}

	/**
	 * @param values
	 * @return full name
	 */
	public static String get_full_prefix(String[] values) {	// get_spot_name1
		if ( null == values[ 0])
			return ( values[ 1].equals( "") ? "" : ( "<" + values[ 1] + ">"));		// "" or "<sv>"
		else if ( values[ 0].equals( ""))
			return ( values[ 1].equals( "") ? "<>" : ( "<:" + values[ 1] + ">"));		// "<>" or "<:sv>"
		else
			return ( values[ 1].equals( "") ? ( "<" + values[ 0] + ">") : ( "<" + values[ 0] + ":" + values[ 1] + ">"));		// "<spot>" or "<spot:sv>"
	}

	/**
	 * @param values
	 * @return "", "<>", "spot"
	 */
	public static String get_semantic_prefix(String[] values) {	//get_spot_name4
		if ( null == values[ 0])
			return ( values[ 1].equals( "") ? "" : "<>");	// "" or "<>"
		else if ( values[ 0].equals( ""))
			return "<>";		// "<>"
		else
			return ( values[ 1].equals( "") ? values[ 0] : ( "<>"));		// "spot" or "<>"
	}

	/**
	 * @param value
	 * @return
	 */
	public static String get_reserved_word(String value) {
		String[] elements = split( value);
		if ( null == elements)
			return null;

		elements[ 0] = ( elements[ 0].startsWith( "!") ? elements[ 0].substring( "!".length()) : elements[ 0]);
		if ( elements[ 0].equals( ""))
			return null;

		elements[ 0] += " ";

		if ( !elements[ 0].startsWith( "<"))
			return elements[ 0];

		elements = elements[ 0].split( ">");
		if ( 2 != elements.length)
			return null;

		return elements[ 1];
	}

	/**
	 * @param value
	 * @return
	 */
	public static String extract_spot_name1(String value) {
		return extract_spot_name1( value, LayerManager.get_instance());
	}

	/**
	 * @param value
	 * @param layerManipulator
	 * @return
	 */
	private static String extract_spot_name1(String value, ILayerManipulator layerManipulator) {
		String[] elements = split( value);
		if ( null == elements)
			return null;

		return extract_spot_name2( elements[ 0], layerManipulator);
	}

	/**
	 * @param element
	 * @return
	 */
	public static String extract_spot_name2(String element) {
		return extract_spot_name2( element, LayerManager.get_instance());
	}

	/**
	 * @param element
	 * @param layerManipulator
	 * @return
	 */
	private static String extract_spot_name2(String element, ILayerManipulator layerManipulator) {
		String[] values = get_spot_and_object( element, layerManipulator);
		if ( null == values)
			return null;

		if ( null == values[ 0] || values[ 0].equals( ""))
			return null;

		return values[ 0];
	}

	/**
	 * @param element
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	public static String update_spot_name1(String element, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		return update_spot_name1( element, newName, originalName, headName, ranges, newHeadName, newRanges, LayerManager.get_instance());
	}

	/**
	 * @param element
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @param layerManipulator
	 * @return
	 */
	private static String update_spot_name1(String element, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges, ILayerManipulator layerManipulator) {
		if ( null == element)
			return null;

		element = ( element.startsWith( "!") ? element.substring( "!".length()) : element);

		String[] words = element.split( ":");
		if ( null == words)
			return null;

		if ( 2 == words.length) {
			if ( words[ 0].equals( ""))
				return null;
		} else if ( 1 == words.length) {
			if ( null == layerManipulator.get_spot_has_this_name( words[ 0]))
				return null;
		}

		String newSpotName = get_new_agent_or_spot_name( words[ 0], newName, originalName, headName, ranges, newHeadName, newRanges);
		if ( null == newSpotName)
			return null;

		return ( newSpotName + ( ( 1 < words.length) ? ( ":" + words[ 1]) : ""));
	}

	/**
	 * @param value
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	public static String update_spot_name2(String value, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		return update_spot_name2( value, newName, originalName, headName, ranges, newHeadName, newRanges, LayerManager.get_instance());
	}

	/**
	 * @param value
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @param layerManipulator
	 * @return
	 */
	private static String update_spot_name2(String value, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges, ILayerManipulator layerManipulator) {
		if ( null == value || value.equals( ""))
			return null;

		String prefix = ( value.startsWith( "!") ? "!" : "");

		value = value.substring( prefix.length());
		if ( value.equals( ""))
			return null;

		if ( !value.startsWith( "<"))	// "name", "!name"
			return null;

		String[] words = value.split( ">");
		if ( null == words || 1 > words.length)
			return null;		// error!

		String spot = update_spot_name1( words[ 0].substring( "<".length()), newName, originalName, headName, ranges, newHeadName, newRanges, layerManipulator);
		if ( null == spot)
			return null;

		return ( prefix + "<" + spot + ">" + ( ( 1 < words.length) ? words[ 1] : ""));
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
	public static boolean update_spot_name3(String[] values, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		return update_spot_name3( values, newName, originalName, headName, ranges, newHeadName, newRanges, LayerManager.get_instance());
	}

	/**
	 * @param values
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @param layerManipulator
	 * @return
	 */
	private static boolean update_spot_name3(String[] values, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges, ILayerManipulator layerManipulator) {
		if ( null == values)
			return false;

		if ( null == values[ 0] || values[ 0].equals( ""))
			return false;

		if ( null == layerManipulator.get_spot_has_this_name( values[ 0]))
			return false;

		String newSpotName = get_new_agent_or_spot_name( values[ 0], newName, originalName, headName, ranges, newHeadName, newRanges);
		if ( null == newSpotName)
			return false;

		values[ 0] = newSpotName;

		return true;
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
	 * @param element
	 * @return
	 */
	public static String get_spot_variable_name1(String element) {
		return get_spot_variable_name1( element, LayerManager.get_instance());
	}

	/**
	 * @param element
	 * @param layerManipulator
	 * @return
	 */
	public static String get_spot_variable_name1(String element, ILayerManipulator layerManipulator) {
		if ( null == element)
			return null;

		element = ( element.startsWith( "!") ? element.substring( "!".length()) : element);

		String[] words = element.split( ":");
		if ( null == words)
			return null;

		if ( 2 == words.length)
			return ( "<" + words[ 0] + ">" + words[ 1]);	// ":sv", "!:sv", "spot:sv", "!spot:sv" -> "<>sv", "<spot>sv"
		else if ( 1 == words.length) {
			if ( !words[ 0].equals( "") && layerManipulator.is_agent_object_name( "spot variable", words[ 0]))
				return words[ 0];	// "sv", "!sv" -> "sv"
		}

		return null;
	}

	/**
	 * @param value
	 * @return
	 */
	public static String get_spot_variable_name2(String value) {
		return get_spot_variable_name2( value, LayerManager.get_instance());
	}

	/**
	 * @param value
	 * @param layerManipulator
	 * @return
	 */
	public static String get_spot_variable_name2(String value, ILayerManipulator layerManipulator) {
		if ( null == value || value.equals( ""))
			return null;

		value = ( value.startsWith( "!") ? value.substring( "!".length()) : value);
		if ( value.equals( ""))
			return null;

		if ( !value.startsWith( "<"))	// "name", "!name"
			return null;

		String[] words = value.split( ">");
		if ( null == words || 1 > words.length)
			return null;		// error!

		return get_spot_variable_name1( words[ 0].substring( "<".length()));
	}

	/**
	 * @param values
	 * @return
	 */
	public static String get_spot_variable_name3(String[] values) {
		if ( null == values)
			return null;

		if ( null == values[ 0])
			return ( values[ 1].equals( "") ? "" : values[ 1]);		// null or "sv"
		else if ( values[ 0].equals( ""))
			return ( values[ 1].equals( "") ? null : ( "<>" + values[ 1]));		// null or "<>sv"
		else
			return ( values[ 1].equals( "") ? null : ( "<" + values[ 0] + ">" + values[ 1]));		// null or "<spot>sv"
	}

	/**
	 * @param element
	 * @param spotVariableName
	 * @param newSpotVariableName
	 * @param type
	 * @return
	 */
	public static String update_spot_variable_name1(String element, String spotVariableName, String newSpotVariableName, String type) {
		if ( null == element)
			return null;

		element = ( element.startsWith( "!") ? element.substring( "!".length()) : element);

		String[] words = element.split( ":");
		if ( null == words)
			return null;

		if ( 2 == words.length) {
			if ( type.equals( "spot") && spotVariableName.equals( words[ 1]))
				return ( words[ 0] + ":" + newSpotVariableName);	// ":sv", "!:sv", "spot:sv", "!spot:sv" -> ":new_sv", "spot:new_sv"
		} else if ( 1 == words.length) {
			if ( type.equals( "agent") && spotVariableName.equals( words[ 0]))
				return newSpotVariableName;	// "sv", "!sv" -> "new_sv"
		}

		return null;
	}

	/**
	 * @param value
	 * @param spotVariableName
	 * @param newSpotVariableName
	 * @param type
	 * @return
	 */
	public static String update_spot_variable_name2(String value, String spotVariableName, String newSpotVariableName, String type) {
		if ( null == value || value.equals( ""))
			return null;

		String prefix = ( value.startsWith( "!") ? "!" : "");

		value = value.substring( prefix.length());
		if ( value.equals( ""))
			return null;

		if ( !value.startsWith( "<"))	// "name", "!name"
			return null;

		String[] words = value.split( ">");
		if ( null == words || 1 > words.length)
			return null;		// error!

		String spot = update_spot_variable_name1( words[ 0].substring( "<".length()), spotVariableName, newSpotVariableName, type);
		if ( null == spot)
			return null;

		return ( prefix + "<" + spot + ">" + ( ( 1 < words.length) ? words[ 1] : ""));
	}

	/**
	 * @param values
	 * @param spotVariableName
	 * @param newSpotVariableName
	 * @param type
	 * @return
	 */
	public static boolean update_spot_variable_name3(String[] values, String spotVariableName, String newSpotVariableName, String type) {
		if ( null == values)
			return false;

		if ( null == values[ 0]) {
			if ( type.equals( "agent") && spotVariableName.equals( values[ 1])) {
				values[ 1] = newSpotVariableName;	// { null, sv} -> { null, new_sv}
				return true;
			}
		} else {
			if ( type.equals( "spot") && spotVariableName.equals( values[ 1])) {
				values[ 1] = newSpotVariableName;	// { "", sv}, { "spot", sv} -> { "", new_sv}, { "spot", new_sv}
				return true;
			}
		}

		return false;
	}

	/**
	 * @param kind
	 * @param element
	 * @return
	 */
	public static boolean is_object(String kind, String element) {
		return is_object( kind, element, LayerManager.get_instance());
	}

	/**
	 * @param kind
	 * @param element
	 * @param layerManipulator
	 * @return
	 */
	public static boolean is_object(String kind, String element, ILayerManipulator layerManipulator) {
		String[] values = get_spot_and_object( element, layerManipulator);
		if ( null == values)
			return false;

		String spot = get_semantic_prefix( values);

		if ( spot.equals( ""))
			return layerManipulator.is_agent_object_name( kind, values[ 2]);
		else {
			if ( spot.equals( "<>"))
				return layerManipulator.is_spot_object_name( kind, values[ 2]);
			else
				return layerManipulator.is_spot_object_name( kind, spot, values[ 2]);
		}
	}

	/**
	 * @param kind
	 * @param element
	 * @param layerManipulator
	 * @return
	 */
	public static boolean can_paste_object(String kind, String element, ILayerManipulator layerManipulator) {
		String[] values = get_spot_and_object( element, layerManipulator);
		if ( null == values)
			return false;

		if ( null == values[ 0]) {
			if ( values[ 1].equals( ""))
				return layerManipulator.is_agent_object_name( kind, values[ 2]);
			else
				return ( layerManipulator.is_agent_object_name( "spot variable", values[ 1])
					&& layerManipulator.is_spot_object_name( kind, values[ 2]));
		} else if ( values[ 0].equals( "")) {
			if ( values[ 1].equals( ""))
				return layerManipulator.is_spot_object_name( kind, values[ 2]);
			else
				return ( layerManipulator.is_spot_object_name( "spot variable", values[ 1])
					&& layerManipulator.is_spot_object_name( kind, values[ 2]));
		} else {
			SpotObject spotObject = layerManipulator.get_spot_has_this_name( values[ 0]);
			if ( null == spotObject)
				return false;

			if ( values[ 1].equals( ""))
				return spotObject.has_same_object_name( kind, values[ 2]);
			else
				return ( spotObject.has_same_object_name( "spot variable", values[ 1])
					&& layerManipulator.is_spot_object_name( kind, values[ 2]));
		}
	}

	/**
	 * @param value
	 * @param layerManipulator
	 * @return
	 */
	public static boolean can_paste_spot_and_spot_variable_name1(String value, ILayerManipulator layerManipulator) {
		String[] spots = get_spot( value, layerManipulator);
		if ( null == spots)
			return false;

		return can_paste_spot_and_spot_variable_name( spots, layerManipulator);
	}

	/**
	 * @param element
	 * @param layerManipulator
	 * @return
	 */
	public static boolean can_paste_spot_and_spot_variable_name2(String element, ILayerManipulator layerManipulator) {
		String[] spots = get_spot_and_object( element, layerManipulator);
		if ( null == spots)
			return false;

		return can_paste_spot_and_spot_variable_name( spots, layerManipulator);
	}

	/**
	 * @param spots
	 * @param layerManipulator
	 * @return
	 */
	public static boolean can_paste_spot_and_spot_variable_name(String spots[], ILayerManipulator layerManipulator) {
		if ( null == spots[ 0]) {
			if ( spots[ 1].equals( ""))
				return true;
			else
				return ( layerManipulator.is_agent_object_name( "spot variable", spots[ 1]));
		} else if ( spots[ 0].equals( "")) {
			if ( spots[ 1].equals( ""))
				return true;
			else
				return ( layerManipulator.is_spot_object_name( "spot variable", spots[ 1]));
		} else {
			SpotObject spotObject = layerManipulator.get_spot_has_this_name( spots[ 0]);
			if ( null == spotObject)
				return false;

			if ( spots[ 1].equals( ""))
				return true;
			else
				return ( spotObject.has_same_object_name( "spot variable", spots[ 1]));
		}
	}

	/**
	 * @param value
	 * @return
	 */
	public static String[] split(String value) {
		int position = value.indexOf( " ");
		if ( 1 > position || value.length() - 1 == position)
			return null;

		String[] elements = new String[ 2];
		elements[ 0] = value.substring( 0, position);
		elements[ 1] = value.substring( position + 1);
		return elements;
	}

	/**
	 * Returns true if the full name of the object contains the specified object name.
	 * @param target the full name of the object
	 * @param objectName the specified object name
	 * @param otherSpotsHaveThisObjectName true if other spots have the specified text
	 * @param headName the parameter for comparison
	 * @param ranges the array of the parameters for comparison
	 * @return true if the full name of the object contains the specified object name
	 */
	public static boolean correspond(String target, String objectName, boolean otherSpotsHaveThisObjectName, String headName, Vector<String[]> ranges) {
		target = get_object_name( target, LayerManager.get_instance());
		// "name" -> "name"
		// "<>name", "<sv>name", "<:sv>name", "<spot:sv>name" -> "<>name"
		// "<spot>name" -> "<spot>name"
		if ( null == target)
			return false;

		if ( null == headName)
			return objectName.equals( target);

		String[] elements = target.split( ">");
		if ( 2 != elements.length)
				return false;

		if ( elements[ 0].equals( "<")) {
			if ( otherSpotsHaveThisObjectName)
				return false;

			return objectName.equals( elements[ 1]);
		}

		String spotName = elements[ 0].substring( "<".length());
		if ( null == spotName)
			return false;

		if ( !SoarsCommonTool.has_same_name( headName, ranges, spotName))
			return false;

		return objectName.equals( elements[ 1]);
	}

	/**
	 * @param element "name", "<>name", "<spot>name", "<sv>name", "<:sv>name", "<spot:sv>name"
	 * @param layerManipulator
	 * @return "name", "<>name", "<spot>name"
	 */
	private static String get_object_name(String element, ILayerManipulator layerManipulator) {
		String[] values = get_spot_and_object( element, layerManipulator);
		if ( null == values)
			return null;

		return ( get_object_name( values) + values[ 2]);
	}

	/**
	 * @param values
	 * @return
	 */
	private static String get_object_name(String[] values) {
		if ( null == values[ 0])
			return ( values[ 1].equals( "") ? "" : "<>");	// "" or "<>"
		else if ( values[ 0].equals( ""))
			return "<>";		// "<>"
		else
			return ( values[ 1].equals( "") ? ( "<" + values[ 0] + ">") : ( "<>"));		// "<spot>" or "<>"
	}

	/**
	 * Returns true if the full name of the object contains the specified object name.
	 * @param prefix the name of the spot
	 * @param target the target string
	 * @param objectName the specified object name
	 * @param type "agent" or "spot"
	 * @return true if the full name of the object contains the specified object name
	 */
	public static boolean correspond(String prefix, String target, String objectName, String type) {
		if ( null == prefix)
			return false;

		if ( type.equals( "agent")) {
			if ( !prefix.equals( ""))
				return false;

			return objectName.equals( target);
		} else if ( type.equals( "spot")) {
			if ( prefix.equals( ""))
				return false;

			return objectName.equals( target);
		}

		return false;
	}

	/**
	 * Replaces the specified text with the new specified text, and returns the new text.
	 * @param target the text which contains the specified text
	 * @param objectName the specified text
	 * @param newObjectName the new specified text
	 * @param type "agent" or "spot"
	 * @return the new text
	 */
	public static String update_object_name(String target, String objectName, String newObjectName, String type) {
		if ( type.equals( "agent")) {
			if ( target.startsWith( "<"))
				return null;

			return ( objectName.equals( target) ? newObjectName : null);
		} else if ( type.equals( "spot")) {
			if ( !target.startsWith( "<"))
				return null;

			String[] elements = target.split( ">");
			if ( 2 != elements.length)
				return null;

			if ( elements[ 0].equals( "<"))
				return ( objectName.equals( elements[ 1]) ? ( "<>" + newObjectName) : null);
			else {
				String spotName = elements[ 0].substring( "<".length());
				if ( null == spotName)
					return null;

				return ( objectName.equals( elements[ 1]) ? ( "<" + spotName + ">" + newObjectName) : null);
			}
		}

		return null;
	}
}
