/*
 * Created on 2006/04/05
 */
package soars.application.visualshell.object.gis.object.variable;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;
import soars.application.visualshell.object.gis.object.base.GisInitialValueBase;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 */
public class GisVariableInitialValue extends GisInitialValueBase {

	/**
	 * 
	 */
	public String _type = "";

	/**
	 * 
	 */
	public String _fieldType = "";	// _typeが"field"の場合のみ使用される

	/**
	 * 
	 */
	public String _value = "";

	/**
	 * 
	 */
	public GisVariableInitialValue() {
		super();
	}

	/**
	 * @param type
	 * @param fieldType
	 * @param value
	 */
	public GisVariableInitialValue(String type, String fieldType, String value) {
		super();
		_type = type;
		_fieldType = fieldType;
		_value = value;
	}

	/**
	 * @param gisVariableInitialValue
	 */
	public GisVariableInitialValue(GisVariableInitialValue gisVariableInitialValue) {
		super();
		copy( gisVariableInitialValue);
	}

	/**
	 * For copy, cut and paste only.
	 * @param row
	 * @param gisVariableInitialValue
	 */
	public GisVariableInitialValue(GisVariableInitialValue gisVariableInitialValue, int row) {
		super(row);
		copy( gisVariableInitialValue);
	}

	/**
	 * @param gisVariableInitialValue
	 */
	public void copy(GisVariableInitialValue gisVariableInitialValue) {
		_type = gisVariableInitialValue._type;
		_fieldType = gisVariableInitialValue._fieldType;
		_value = gisVariableInitialValue._value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if ( !( obj instanceof GisVariableInitialValue))
			return false;

		GisVariableInitialValue gisVariableInitialValue = ( GisVariableInitialValue)obj;
		return ( _type.equals( gisVariableInitialValue._type)
			&& _fieldType.equals( gisVariableInitialValue._fieldType)
			&& _value.equals( gisVariableInitialValue._value));
	}

	/**
	 * @param gisObjectBase
	 * @return
	 */
	public boolean contains(GisObjectBase gisObjectBase) {
		return ( _type.equals( gisObjectBase._kind) && _value.equals( gisObjectBase._name));
	}

	/**
	 * @param kind
	 * @param name
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param entityBase
	 * @return
	 */
	public boolean contains(String kind, String name, String type, String headName, Vector<String[]> ranges/*, EntityBase entityBase*/) {
		if ( contains( type, headName, ranges)) {
			String[] message = new String[] {
//				( ( entityBase instanceof AgentObject) ? "Agent" : "Spot"),
//				"name = " + entityBase._name,
				kind + " " + name,
				"type = " + _type,
				"value = " + _value
			};

			WarningManager.get_instance().add( message);

			return true;
		}
		return false;
	}

	/**
	 * @param kind
	 * @param name
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @param entityBase
	 * @return
	 */
	public boolean contains(String kind, String name, String type, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges/*, EntityBase entityBase*/) {
		if ( contains( type, headName, ranges)
			&& !contains( type, newHeadName, newRanges)) {
			String[] message = new String[] {
//				( ( entityBase instanceof AgentObject) ? "Agent" : "Spot"),
//				"name = " + entityBase._name,
				kind + " " + name,
				"type = " + _type,
				"value = " + _value
			};

			WarningManager.get_instance().add( message);

			return true;
		}
		return false;
	}

	/**
	 * @param kind
	 * @param name
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param entityBase
	 * @return
	 */
	public boolean can_adjust_name(String kind, String name, String type, String headName, Vector<String[]> ranges/*, EntityBase entityBase*/) {
		if ( contains( type, headName, ranges)) {
			String[] message = new String[] {
//				( ( entityBase instanceof AgentObject) ? "Agent" : "Spot"),
//				"name = " + entityBase._name,
				kind + " " + name,
				"type = " + _type,
				"value = " + _value
			};

			WarningManager.get_instance().add( message);

			return false;
		}
		return true;
	}

	/**
	 * @param kind
	 * @param name
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param entityBase
	 * @return
	 */
	public boolean can_adjust_name(String kind, String name, String type, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges/*, EntityBase entityBase*/) {
		if ( contains( type, headName, ranges) && !contains( type, newHeadName, newRanges)) {
			String[] message = new String[] {
//				( ( entityBase instanceof AgentObject) ? "Agent" : "Spot"),
//				"name = " + entityBase._name,
				kind + " " + name,
				"type = " + _type,
				"value = " + _value
			};

			WarningManager.get_instance().add( message);

			return false;
		}
		return true;
	}

	/**
	 * @param type
	 * @param headName
	 * @param ranges
	 * @return
	 */
	private boolean contains(String type, String headName, Vector<String[]> ranges) {
		return ( _type.equals( type) && SoarsCommonTool.has_same_name( headName, ranges, _value));
	}

	/**
	 * @param type
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	public boolean update_name_and_number(String type, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		if ( !_type.equals( type))
			return false;

		if ( !SoarsCommonTool.has_same_name( headName, ranges, _value))
			return false;

		if ( SoarsCommonTool.has_same_name( newHeadName, newRanges, _value))
			return false;

		_value = newName + _value.substring( originalName.length());

		return true;
	}

	/**
	 * @param type
	 * @param name
	 * @param newName
	 * @return
	 */
	public boolean update_object_name(String type, String name, String newName) {
		if ( !type.equals( _type) || !name.equals( _value))
			return false;

		_value = newName;
		return true;
	}

	/**
	 * @param alias
	 * @return
	 */
	public boolean contains_this_alias(String alias) {
		return _value.equals( alias);
	}

	/**
	 * @param initialValues
	 */
	public void get_initial_values1(Vector<String> initialValues) {
		if ( !initialValues.contains( _value))
			initialValues.add( _value);
	}

	/**
	 * @param initialValues
	 */
	public void get_initial_values2(Vector<String> initialValues) {
		initialValues.add( _value);
	}

	/**
	 * @return
	 */
	public String get_initial_data() {
		return ( "\t" + _type + "\t" + _value);
	}

	/**
	 * @param gisPropertyPanelBaseMap
	 * @param gisObjectBase
	 * @param gisObjectBases
	 * @param entityBase
	 * @return
	 */
	public boolean can_paste(Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, GisObjectBase gisObjectBase, List<GisObjectBase> gisObjectBases/*, EntityBase entityBase*/) {
		if ( _type.equals( "field"))
			return true;

		if ( _type.equals( "agent") && null != LayerManager.get_instance().get_agent_has_this_name( _value))
			return true;

		if ( _type.equals( "spot") && null != LayerManager.get_instance().get_spot_has_this_name( _value))
			return true;

		if ( _type.equals( "probability") || _type.equals( "keyword") || _type.equals( "number object")
			|| _type.equals( "time variable") || _type.equals( "spot variable")) {
			GisPropertyPanelBase gisPropertyPanelBase = gisPropertyPanelBaseMap.get( "simple variable");
			return ( ( null != gisPropertyPanelBase) && gisPropertyPanelBase.contains( _value));
		}

//		if ( _type.equals( "role variable")) {
//			GisPropertyPanelBase gisPropertyPanelBase = gisPropertyPanelBaseMap.get( "simple variable");
//			return ( ( entityBase instanceof AgentObject) && ( null != gisPropertyPanelBase) && gisPropertyPanelBase.contains( _value));
//		}

		if ( _type.equals( "collection") || _type.equals( "list") || _type.equals( "map") || _type.equals( "exchange algebra")) {
			GisPropertyPanelBase gisPropertyPanelBase = gisPropertyPanelBaseMap.get( "variable");
			return ( ( null != gisPropertyPanelBase) && ( gisPropertyPanelBase.contains( _value) || contains( _value, gisObjectBase, gisObjectBases)));
//			if ( null == gisPropertyPanelBase)
//				return false;
//
//			if ( gisPropertyPanelBase.contains( _value))
//				return true;
//
//			return contains( _value, gisPropertyPanelBaseMap, gisObjectBase, gisObjectBases, entityBase);
		}

		if ( _type.equals( "class variable")) {
			GisPropertyPanelBase gisPropertyPanelBase = gisPropertyPanelBaseMap.get( "class variable");
			return ( ( null != gisPropertyPanelBase) && gisPropertyPanelBase.contains( _value));
		}

		if ( _type.equals( "file")) {
			GisPropertyPanelBase gisPropertyPanelBase = gisPropertyPanelBaseMap.get( "file");
			return ( ( null != gisPropertyPanelBase) && gisPropertyPanelBase.contains( _value));
		}

		return false;
	}

	/**
	 * @param name
	 * @param gisObjectBase
	 * @param gisObjectBases
	 * @return
	 */
	private boolean contains(String name, GisObjectBase gisObjectBase, List<GisObjectBase> gisObjectBases) {
		if ( null == gisObjectBase || null == gisObjectBases)
			return false;

		for ( GisObjectBase gob:gisObjectBases) {
			if ( gob == gisObjectBase)
				continue;

			if ( gob._name.equals( name))
				return true;
		}
		return false;
	}

//	/**
//	 * @param name
//	 * @param gisPropertyPanelBaseMap
//	 * @param gisObjectBase
//	 * @param gisObjectBases
//	 * @param entityBase
//	 * @return
//	 */
//	private boolean contains(String name, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, GisObjectBase gisObjectBase, List<GisObjectBase> gisObjectBases, EntityBase entityBase) {
//		if ( null == gisObjectBase || null == gisObjectBases)
//			return false;
//
//		for ( GisObjectBase gob:gisObjectBases) {
//			if ( gob == gisObjectBase)
//				continue;
//
//			if ( !gob._name.equals( name))
//				continue;
//
//			if ( gob instanceof GisVariableObject) {
//				GisVariableObject gisVariableObject = ( GisVariableObject)gob;
//				return gisVariableObject.can_paste( gisPropertyPanelBaseMap, gisObjectBase, gisObjectBases, entityBase);
//			} else if ( gob instanceof GisMapObject) {
//				GisMapObject gisMapObject = ( GisMapObject)gob;
//				return gisMapObject.can_paste( gisPropertyPanelBaseMap, gisObjectBase, gisObjectBases, entityBase);
//			} else if ( gob instanceof GisExchangeAlgebraObject) {
//				GisExchangeAlgebraObject gisExchangeAlgebraObject = ( GisExchangeAlgebraObject)gob;
//				return gisExchangeAlgebraObject.can_paste( gisPropertyPanelBaseMap);
//			}
//			return false;
//		}
//		return false;
//	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "type", "", Writer.escapeAttributeCharData( _type));
		attributesImpl.addAttribute( null, null, "field_type", "", Writer.escapeAttributeCharData( _fieldType));
		attributesImpl.addAttribute( null, null, "value", "", Writer.escapeAttributeCharData( _value));
		writer.writeElement( null, null, "element", attributesImpl);
		return true;
	}
}
