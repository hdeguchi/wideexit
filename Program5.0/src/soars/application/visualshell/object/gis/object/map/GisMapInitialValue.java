/**
 * 
 */
package soars.application.visualshell.object.gis.object.map;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;
import soars.application.visualshell.object.gis.object.base.GisInitialValueBase;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class GisMapInitialValue extends GisInitialValueBase {

	/**
	 * 
	 */
	public String[] _key = new String[] { "", "", ""};		// { type, value, field type}

	/**
	 * 
	 */
	public String[] _value = new String[] { "", "", ""};	// { type, value, field type}

	/**
	 * 
	 */
	public GisMapInitialValue() {
		super();
	}

	/**
	 * @param keyType
	 * @param key
	 * @param keyFieldType
	 * @param valueType
	 * @param value
	 * @param valueFieldType
	 */
	public GisMapInitialValue(String keyType, String key, String keyFieldType, String valueType, String value, String valueFieldType) {
		super();
		_key[ 0] = keyType;
		_key[ 1] = key;
		_key[ 2] = keyFieldType;
		_value[ 0] = valueType;
		_value[ 1] = value;
		_value[ 2] = valueFieldType;
	}

	/**
	 * @param gisMapInitialValue
	 */
	public GisMapInitialValue(GisMapInitialValue gisMapInitialValue) {
		super();
		copy( gisMapInitialValue);
	}

	/**
	 * For copy, cut and paste only.
	 * @param gisMapInitialValue
	 */
	public GisMapInitialValue(GisMapInitialValue gisMapInitialValue, int row) {
		super(row);
		copy( gisMapInitialValue);
	}

	/**
	 * @param gisMapInitialValue
	 */
	public void copy(GisMapInitialValue gisMapInitialValue) {
		_key[ 0] = gisMapInitialValue._key[ 0];
		_key[ 1] = gisMapInitialValue._key[ 1];
		_key[ 2] = gisMapInitialValue._key[ 2];
		_value[ 0] = gisMapInitialValue._value[ 0];
		_value[ 1] = gisMapInitialValue._value[ 1];
		_value[ 2] = gisMapInitialValue._value[ 2];
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if ( !( obj instanceof GisMapInitialValue))
			return false;

		GisMapInitialValue gisMapInitialValue = ( GisMapInitialValue)obj;
		return ( _key[ 0].equals( gisMapInitialValue._key[ 0])
			&& _key[ 1].equals( gisMapInitialValue._key[ 1])
			&& _key[ 2].equals( gisMapInitialValue._key[ 2])
			&& _value[ 0].equals( gisMapInitialValue._value[ 0])
			&& _value[ 1].equals( gisMapInitialValue._value[ 1])
			&& _value[ 2].equals( gisMapInitialValue._value[ 2]));
	}

	/**
	 * @param gisObjectBase
	 * @return
	 */
	public boolean contains(GisObjectBase gisObjectBase) {
		return ( ( _key[ 0].equals( gisObjectBase._kind) && _key[ 1].equals( gisObjectBase._name))
			|| ( _value[ 0].equals( gisObjectBase._kind) && _value[ 1].equals( gisObjectBase._name)));
	}

	/**
	 * @param name
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param entityBase
	 * @return
	 */
	public boolean contains(String name, String type, String headName, Vector<String[]> ranges/*, EntityBase entityBase*/) {
		if ( contains( _key, type, headName, ranges)) {
			String[] message = new String[] {
//				( ( entityBase instanceof AgentObject) ? "Agent" : "Spot"),
//				"name = " + entityBase._name,
				"map " + name,
				"type = " + _key[ 0],
				"value = " + _key[ 1]
			};

			WarningManager.get_instance().add( message);

			return true;
		}

		if ( contains( _value, type, headName, ranges)) {
			String[] message = new String[] {
//				( ( entityBase instanceof AgentObject) ? "Agent" : "Spot"),
//				"name = " + entityBase._name,
				"map " + name,
				"type = " + _value[ 0],
				"value = " + _value[ 1]
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
	public boolean contains(String name, String type, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges/*, EntityBase entityBase*/) {
		if ( contains( _key, type, headName, ranges)
			&& !contains( _key, type, newHeadName, newRanges)) {
			String[] message = new String[] {
//				( ( entityBase instanceof AgentObject) ? "Agent" : "Spot"),
//				"name = " + entityBase._name,
				"map " + name,
				"type = " + _key[ 0],
				"value = " + _key[ 1]
			};

			WarningManager.get_instance().add( message);

			return true;
		}

		if ( contains( _value, type, headName, ranges)
			&& !contains( _value, type, newHeadName, newRanges)) {
			String[] message = new String[] {
//				( ( entityBase instanceof AgentObject) ? "Agent" : "Spot"),
//				"name = " + entityBase._name,
				"map " + name,
				"type = " + _value[ 0],
				"value = " + _value[ 1]
			};

			WarningManager.get_instance().add( message);

			return true;
		}

		return false;
	}

	/**
	 * @param name
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param entityBase
	 * @return
	 */
	public boolean can_adjust_name(String name, String type, String headName, Vector<String[]> ranges/*, EntityBase entityBase*/) {
		if ( contains( _key, type, headName, ranges)) {
			String[] message = new String[] {
//				( ( entityBase instanceof AgentObject) ? "Agent" : "Spot"),
//				"name = " + entityBase._name,
				"map " + name,
				"type = " + _key[ 0],
				"value = " + _key[ 1]
			};

			WarningManager.get_instance().add( message);

			return false;
		}

		if ( contains( _value, type, headName, ranges)) {
			String[] message = new String[] {
//				( ( entityBase instanceof AgentObject) ? "Agent" : "Spot"),
//				"name = " + entityBase._name,
				"map " + name,
				"type = " + _value[ 0],
				"value = " + _value[ 1]
			};

			WarningManager.get_instance().add( message);

			return false;
		}

		return true;
	}

	/**
	 * @param name
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @param entityBase
	 * @return
	 */
	public boolean can_adjust_name(String name, String type, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges/*, EntityBase entityBase*/) {
		if ( contains( _key, type, headName, ranges)
			&& !contains( _key, type, newHeadName, newRanges)) {
			String[] message = new String[] {
//				( ( entityBase instanceof AgentObject) ? "Agent" : "Spot"),
//				"name = " + entityBase._name,
				"map " + name,
				"type = " + _key[ 0],
				"value = " + _key[ 1]
			};

			WarningManager.get_instance().add( message);

			return false;
		}

		if ( contains( _value, type, headName, ranges)
			&& !contains( _value, type, newHeadName, newRanges)) {
			String[] message = new String[] {
//				( ( entityBase instanceof AgentObject) ? "Agent" : "Spot"),
//				"name = " + entityBase._name,
				"map " + name,
				"type = " + _value[ 0],
				"value = " + _value[ 1]
			};

			WarningManager.get_instance().add( message);

			return false;
		}

		return true;
	}

	/**
	 * @param values
	 * @param type
	 * @param headName
	 * @param ranges
	 * @return
	 */
	private boolean contains(String[] values, String type, String headName, Vector<String[]> ranges) {
		return ( values[ 0].equals( type) && SoarsCommonTool.has_same_name( headName, ranges, values[ 1]));
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
		return ( update_name_and_number( _key, type, newName, originalName, headName, ranges, newHeadName, newRanges)
			|| update_name_and_number( _value, type, newName, originalName, headName, ranges, newHeadName, newRanges));
	}

	/**
	 * @param values
	 * @param type
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	private boolean update_name_and_number(String[] values, String type, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		if ( !values[ 0].equals( type))
			return false;

		if ( !SoarsCommonTool.has_same_name( headName, ranges, values[ 1]))
			return false;

		if ( SoarsCommonTool.has_same_name( newHeadName, newRanges, values[ 1]))
			return false;

		values[ 1] = newName + values[ 1].substring( originalName.length());

		return true;
	}

	/**
	 * @param alias
	 * @return
	 */
	public boolean contains_this_alias(String alias) {
		return ( _key[ 1].equals( alias) || _value[ 1].equals( alias));
	}

	/**
	 * @param initialValues
	 */
	public void get_initial_values1(Vector<String> initialValues) {
		String key = ( ( _key[ 0].equals( "immediate") ? "\"" : "") + _key[ 1] + ( _key[ 0].equals( "immediate") ? "\"" : ""));
		if ( !initialValues.contains( key))
			initialValues.add( key);

		if ( !initialValues.contains( _value[ 1]))
			initialValues.add( _value[ 1]);
	}

	/**
	 * @param initialValues
	 */
	public void get_initial_values2(Vector<String> initialValues) {
		String key = ( ( _key[ 0].equals( "immediate") ? "\"" : "") + _key[ 1] + ( _key[ 0].equals( "immediate") ? "\"" : ""));
		String initialValue = ( "[" + key + ", " + _value[ 1] + "]");
		initialValues.add( initialValue);
	}

	/**
	 * @param type
	 * @param name
	 * @param newName
	 * @return
	 */
	public boolean update_object_name(String type, String name, String newName) {
		boolean result = false;
		if ( type.equals( _key[ 0]) && name.equals( _key[ 1])) {
			_key[ 1] = newName;
			result = true;
		}

		if ( type.equals( _value[ 0]) && name.equals( _value[ 1])) {
			_value[ 1] = newName;
			result = true;
		}

		return result;
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	public boolean can_paste(Layer drawObjects) {
		// エージェントまたはスポット以外は全て自らが保持しているのでチェックは必要無い筈！
		if ( _value[ 0].equals( "agent"))
			return ( null != drawObjects.get_agent_has_this_name( _value[ 1]));
		else if ( _value[ 0].equals( "spot"))
			return ( null != drawObjects.get_spot_has_this_name( _value[ 1]));
		else
			return true;
	}

	/**
	 * @param gisMapInitialValue
	 * @return
	 */
	public boolean is_same_key(GisMapInitialValue gisMapInitialValue) {
		return ( _key[ 0].equals( gisMapInitialValue._key[ 0]) && _key[ 1].equals( gisMapInitialValue._key[ 1]));
	}

	/**
	 * @param gisMapInitialValue
	 * @return
	 */
	public boolean is_same_key_and_value(GisMapInitialValue gisMapInitialValue) {
		return ( _key[ 0].equals( gisMapInitialValue._key[ 0])
			&& _key[ 1].equals( gisMapInitialValue._key[ 1])
			&& _value[ 0].equals( gisMapInitialValue._value[ 0])
			&& _value[ 1].equals( gisMapInitialValue._value[ 1]));
	}

	/**
	 * @return
	 */
	public String get_initial_data() {
		return ( "\t" + _key[ 0] + "\t" + _key[ 1] + "\t" + _value[ 0] + "\t" + _value[ 1]);
	}

//	/**
//	 * @param prefix
//	 * @param name
//	 * @param initialValueMap
//	 * @return
//	 */
//	public String get_script(String prefix, String name, InitialValueMap initialValueMap) {
//		String key = ( null == initialValueMap) ? _key[ 1] : initialValueMap.get_initial_value( _key[ 1]);
//		key = ( ( _key[ 0].equals( "immediate") ? "\"" : "") + key + ( _key[ 0].equals( "immediate") ? "\"" : ""));
//
//		String value = ( null == initialValueMap) ? _value[ 1] : initialValueMap.get_initial_value( _value[ 1]);
//		if ( _value[ 0].equals( "agent"))
//			value = ( "{" + value + "}");
//		else if ( _value[ 0].equals( "spot"))
//			value = ( "<" + value + ">");
//		else if ( _value[ 0].equals( "role variable"))
//			value = ( "$Role." + value);
//		else if ( _value[ 0].equals( "time variable"))
//			value = ( "$Time." + value);
//
//		String script = ( _key[ 0].equals( "keyword") ? ( prefix + "equip " + key) : "");
//
//		if ( _value[ 0].equals( "keyword") || _value[ 0].equals( "file"))
//			script += ( ( script.equals( "") ? "" : " ; ") + prefix + "equip " + value);
//
//		script += ( ( script.equals( "") ? "" : " ; ") + prefix + MapCommand._reservedWords[ 1] + name + "=" + key + "=" + value);
//
//		if ( _value[ 0].equals( "keyword") || _value[ 0].equals( "file"))
//			return script;
//		else if ( _value[ 0].equals( "time variable"))
//			return ( script + " ; " + prefix + "cloneEquip " + value);
//		else if ( _value[ 0].equals( "agent") || _value[ 0].equals( "spot") || _value[ 0].equals( "role variable") || _value[ 0].equals( "spot variable"))
//			return script;
//		else
//			return ( script + " ; " + prefix + "cloneEquip " + value);
//	}
//
//	/**
//	 * @param index
//	 * @param attributesImpl
//	 */
//	public void write(int index, AttributesImpl attributesImpl) {
//		attributesImpl.addAttribute( null, null, "key_type" + index, "", Writer.escapeAttributeCharData( _key[ 0]));
//		attributesImpl.addAttribute( null, null, "key" + index, "", Writer.escapeAttributeCharData( _key[ 1]));
//		attributesImpl.addAttribute( null, null, "key_field_type" + index, "", Writer.escapeAttributeCharData( _key[ 2]));
//		attributesImpl.addAttribute( null, null, "value_type" + index, "", Writer.escapeAttributeCharData( _value[ 0]));
//		attributesImpl.addAttribute( null, null, "value" + index, "", Writer.escapeAttributeCharData( _value[ 1]));
//		attributesImpl.addAttribute( null, null, "value_field_type" + index, "", Writer.escapeAttributeCharData( _value[ 2]));
//	}
//
//	/**
//	 * @param msg
//	 * @param agentDataMap
//	 * @param spotDataMap
//	 * @param entityData
//	 * @return
//	 */
//	public boolean verify(String msg, EntityDataMap agentDataMap, EntityDataMap spotDataMap, EntityData entityData) {
//		return ( verify( _key[ 0], _key[ 1], msg, agentDataMap, spotDataMap, entityData)
//			&& verify( _value[ 0], _value[ 1], msg, agentDataMap, spotDataMap, entityData));
//	}
//
//	/**
//	 * @param type
//	 * @param value
//	 * @param msg
//	 * @param agentDataMap
//	 * @param spotDataMap
//	 * @param entityData
//	 * @return
//	 */
//	private boolean verify(String type, String value, String msg, EntityDataMap agentDataMap, EntityDataMap spotDataMap, EntityData entityData) {
//		if ( type.equals( "immediate"))
//			return true;
//		else if ( type.equals( "agent")) {
//			if ( !agentDataMap.contains( value) && null == LayerManager.get_instance().get_agent_has_this_name( value)) {
//				String[] message = new String[] {
//					"Agent",
//					"name = " + entityData._name,
//					msg,
//					"agent " + value + " does not exist!"
//				};
//
//				WarningManager.get_instance().add( message);
//
//				return false;
//			}
//		} else if ( type.equals( "spot")) {
//			if ( !spotDataMap.contains( value) && null == LayerManager.get_instance().get_spot_has_this_name( value)) {
//				String[] message = new String[] {
//					"Spot",
//					"name = " + entityData._name,
//					msg,
//					"spot " + value + " does not exist!"
//				};
//
//				WarningManager.get_instance().add( message);
//
//				return false;
//			}
//		} else {
//			if ( entityData.has_same_object_name( type, value))
//				return true;
//
//			EntityBase entityBase;
//			if ( entityData._type.equals( "agent"))
//				entityBase = LayerManager.get_instance().get_agent( entityData._name);
//			else
//				entityBase = LayerManager.get_instance().get_spot( entityData._name);
//
//			if ( null == entityBase || entityBase.is_multi() || !entityBase.has_same_object_name( type, value)) {
//				String[] message = new String[] {
//					( entityData._type.equals( "agent") ? "Agent" : "Spot"),
//					"name = " + entityData._name,
//					msg,
//					type + " " + value + " does not exist!"
//				};
//
//				WarningManager.get_instance().add( message);
//
//				return false;
//			}
//
//			return true;
//		}
//
//		return true;
//	}
//
//	/**
//	 * @param msg
//	 * @param agentDataMap
//	 * @param spotDataMap
//	 * @param indices
//	 * @param entityData
//	 * @return
//	 */
//	public boolean verify(String msg, EntityDataMap agentDataMap, EntityDataMap spotDataMap, Vector<int[]> indices, EntityData entityData) {
//		return ( verify( _key[ 0], _key[ 1], msg, agentDataMap, spotDataMap, indices, entityData)
//			&& verify( _value[ 0], _value[ 1], msg, agentDataMap, spotDataMap, indices, entityData));
//	}
//
//	/**
//	 * @param type
//	 * @param value
//	 * @param msg
//	 * @param agentDataMap
//	 * @param spotDataMap
//	 * @param indices
//	 * @param entityData
//	 * @return
//	 */
//	private boolean verify(String type, String value, String msg, EntityDataMap agentDataMap, EntityDataMap spotDataMap, Vector<int[]> indices, EntityData entityData) {
//		if ( type.equals( "immediate"))
//			return true;
//		else if ( type.equals( "agent")) {
//			if ( !agentDataMap.contains( value) && null == LayerManager.get_instance().get_agent_has_this_name( value)) {
//				String[] message = new String[] {
//					"Agent",
//					"name = " + entityData._name,
//					msg,
//					"agent " + value + " does not exist!"
//				};
//
//				WarningManager.get_instance().add( message);
//
//				return false;
//			}
//		} else if ( type.equals( "spot")) {
//			if ( !spotDataMap.contains( value) && null == LayerManager.get_instance().get_spot_has_this_name( value)) {
//				String[] message = new String[] {
//					"Spot",
//					"name = " + entityData._name,
//					msg,
//					"spot " + value + " does not exist!"
//				};
//
//				WarningManager.get_instance().add( message);
//
//				return false;
//			}
//		} else {
//			Vector<int[]> objectRanges = entityData.get_object_ranges( type, value);
//
//			EntityBase entityBase;
//			if ( entityData._type.equals( "agent"))
//				entityBase = LayerManager.get_instance().get_agent( entityData._name);
//			else
//				entityBase = LayerManager.get_instance().get_spot( entityData._name);
//
//			if ( null != entityBase && entityBase.is_multi()) {
//				Vector<int[]> ranges = entityBase.get_object_ranges( type, value);
//				CommonTool.merge_indices( ranges, objectRanges);
//			}
//
//			if ( !CommonTool.contains_ranges( objectRanges, indices)) {
//				String[] message = new String[] {
//					( entityData._type.equals( "agent") ? "Agent" : "Spot"),
//					"name = " + entityData._name,
//					msg,
//					type + " " + value + " does not exist!"
//				};
//
//				WarningManager.get_instance().add( message);
//
//				return false;
//			}
//
//			return true;
//		}
//
//		return true;
//	}
//
//	/**
//	 * @param entityBase
//	 * @return
//	 */
//	public boolean transform(EntityBase entityBase) {
//		if ( _key[ 1].equals( "")) {
//			if ( _key[ 0].equals( ""))
//				_key[ 0] = "immediate";
//		} else {
//			if ( !transform( _key, entityBase))
//				_key[ 0] = "immediate";
//		}
//
//		if ( !transform( _value, entityBase))
//			return false;
//
//		return true;
//	}
//
//	/**
//	 * @param values
//	 * @param entityBase
//	 * @return
//	 */
//	private boolean transform(String[] values, EntityBase entityBase) {
//		if ( !values[ 0].equals( ""))
//			return true;
//
//		String type = entityBase.which_object_has_this_name( values[ 1]);
//		if ( null != type) {
//			values[ 0] = type;
//			return true;
//		}
//
//		if ( null != LayerManager.get_instance().get_agent_has_this_name( values[ 1])) {
//			values[ 0] = "agent";
//			return true;
//		}
//			
//		if ( null != LayerManager.get_instance().get_spot_has_this_name( values[ 1])) {
//			values[ 0] = "spot";
//			return true;
//		}
//			
//		return false;
//	}

	/**
	 * @param gisPropertyPanelBaseMap
	 * @param gisObjectBase
	 * @param gisObjectBases
	 * @param entityBase
	 * @return
	 */
	public boolean can_paste(Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, GisObjectBase gisObjectBase, List<GisObjectBase> gisObjectBases/*, EntityBase entityBase*/) {
		return ( can_paste_key( gisPropertyPanelBaseMap/*, entityBase*/) && can_paste_value( gisPropertyPanelBaseMap, gisObjectBase, gisObjectBases/*, entityBase*/));
	}

	/**
	 * @param gisPropertyPanelBaseMap
	 * @param entityBase
	 * @return
	 */
	private boolean can_paste_key(Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap/*, EntityBase entityBase*/) {
		if ( _key[ 0].equals( "field"))
			return true;

		if ( _key[ 0].equals( "immediate"))
			return true;

		if ( _key[ 0].equals( "keyword")) {
			GisPropertyPanelBase gisPropertyPanelBase = gisPropertyPanelBaseMap.get( "simple variable");
			return ( ( null != gisPropertyPanelBase) && gisPropertyPanelBase.contains( _key[ 1]));
		}

		return false;
	}

	/**
	 * @param gisPropertyPanelBaseMap
	 * @param gisObjectBase
	 * @param gisObjectBases
	 * @param entityBase
	 * @return
	 */
	private boolean can_paste_value(Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, GisObjectBase gisObjectBase, List<GisObjectBase> gisObjectBases/*, EntityBase entityBase*/) {
		if ( _value[ 0].equals( "field"))
			return true;

		if ( _value[ 0].equals( "agent") && null != LayerManager.get_instance().get_agent_has_this_name( _value[ 1]))
			return true;

		if ( _value[ 0].equals( "spot") && null != LayerManager.get_instance().get_spot_has_this_name( _value[ 1]))
			return true;

		if ( _value[ 0].equals( "probability") || _value[ 0].equals( "keyword") || _value[ 0].equals( "number object")
			|| _value[ 0].equals( "time variable") || _value[ 0].equals( "spot variable")) {
			GisPropertyPanelBase gisPropertyPanelBase = gisPropertyPanelBaseMap.get( "simple variable");
			return ( ( null != gisPropertyPanelBase) && gisPropertyPanelBase.contains( _value[ 1]));
		}

//		if ( _value[ 0].equals( "role variable")) {
//			GisPropertyPanelBase gisPropertyPanelBase = gisPropertyPanelBaseMap.get( "simple variable");
//			return ( ( entityBase instanceof AgentObject) && ( null != gisPropertyPanelBase) && gisPropertyPanelBase.contains( _value[ 1]));
//		}

		if ( _value[ 0].equals( "collection") || _value[ 0].equals( "list") || _value[ 0].equals( "map") || _value[ 0].equals( "exchange algebra")) {
			GisPropertyPanelBase gisPropertyPanelBase = gisPropertyPanelBaseMap.get( "variable");
			return ( ( null != gisPropertyPanelBase) && ( gisPropertyPanelBase.contains( _value[ 1]) || contains( _value[ 1], gisObjectBase, gisObjectBases)));
//			if ( null == gisPropertyPanelBase)
//				return false;
//
//			if ( gisPropertyPanelBase.contains( _value[ 1]))
//				return true;
//
//			return contains( _value[ 1], gisPropertyPanelBaseMap, gisObjectBase, gisObjectBases, entityBase);
		}

		if ( _value[ 0].equals( "class variable")) {
			GisPropertyPanelBase gisPropertyPanelBase = gisPropertyPanelBaseMap.get( "class variable");
			return ( ( null != gisPropertyPanelBase) && gisPropertyPanelBase.contains( _value[ 1]));
		}

		if ( _value[ 0].equals( "file")) {
			GisPropertyPanelBase gisPropertyPanelBase = gisPropertyPanelBaseMap.get( "file");
			return ( ( null != gisPropertyPanelBase) && gisPropertyPanelBase.contains( _value[ 1]));
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
//	private boolean contains(String name, Map<String, PropertyPanelBase> gisPropertyPanelBaseMap, ObjectBase gisObjectBase, List<ObjectBase> gisObjectBases, EntityBase entityBase) {
//		if ( null == gisObjectBase || null == gisObjectBases)
//			return false;
//
//		for ( gisObjectBase ob:gisObjectBases) {
//			if ( gob == gisObjectBase)
//				continue;
//
//			if ( gob._name.equals( name))
//				return true;
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
		attributesImpl.addAttribute( null, null, "key_type", "", Writer.escapeAttributeCharData( _key[ 0]));
		attributesImpl.addAttribute( null, null, "key", "", Writer.escapeAttributeCharData( _key[ 1]));
		attributesImpl.addAttribute( null, null, "key_field_type", "", Writer.escapeAttributeCharData( _key[ 2]));
		attributesImpl.addAttribute( null, null, "value_type", "", Writer.escapeAttributeCharData( _value[ 0]));
		attributesImpl.addAttribute( null, null, "value", "", Writer.escapeAttributeCharData( _value[ 1]));
		attributesImpl.addAttribute( null, null, "value_field_type", "", Writer.escapeAttributeCharData( _value[ 2]));
		writer.writeElement( null, null, "element", attributesImpl);
		return true;
	}
}
