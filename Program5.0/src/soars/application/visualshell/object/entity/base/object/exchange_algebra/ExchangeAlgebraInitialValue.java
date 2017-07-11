/**
 * 
 */
package soars.application.visualshell.object.entity.base.object.exchange_algebra;

import java.util.Map;
import java.util.Vector;

import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.file.importer.initial.entity.EntityData;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.object.base.InitialValueBase;
import soars.application.visualshell.object.entity.base.object.base.ObjectBase;
import soars.application.visualshell.object.entity.base.object.number.NumberObject;
import soars.application.visualshell.object.role.base.object.legacy.command.ExchangeAlgebraCommand;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class ExchangeAlgebraInitialValue extends InitialValueBase {

	/**
	 * 
	 */
	public String _value = "";

	/**
	 * 
	 */
	public String _keyword = "";

	/**
	 * 
	 */
	public String _name = "";

	/**
	 * 
	 */
	public String _hat = "";

	/**
	 * 
	 */
	public String _unit = "";

	/**
	 * 
	 */
	public String _time = "";

	/**
	 * 
	 */
	public String _subject = "";

	/**
	 * 
	 */
	public ExchangeAlgebraInitialValue() {
		super();
	}

	/**
	 * @param value
	 * @param keyword
	 */
	public ExchangeAlgebraInitialValue(String value, String keyword) {
		super();
		_value = ( ( null == value) ? "" : value);
		_keyword = ( ( null == keyword) ? "" : keyword);
		_hat = _name = _unit = _time = _subject = "";
	}

	/**
	 * @param value
	 * @param name
	 * @param hat
	 * @param unit
	 * @param time
	 * @param subject
	 */
	public ExchangeAlgebraInitialValue(String value, String name, String hat, String unit, String time, String subject) {
		super();
		_value = ( ( null == value) ? "" : value);
		_keyword = "";
		_name = ( ( null == name) ? "" : name);
		_hat = ( ( null == hat) ? "" : hat);
		_unit = ( ( null == unit) ? "" : unit);
		_time = ( ( null == time) ? "" : time);
		_subject = ( ( null == subject) ? "" : subject);
	}

	/**
	 * @param value
	 * @param keyword
	 * @param name
	 * @param hat
	 * @param unit
	 * @param time
	 * @param subject
	 */
	public ExchangeAlgebraInitialValue(String value, String keyword, String name, String hat, String unit, String time, String subject) {
		super();
		_value = ( ( null == value) ? "" : value);
		_keyword = ( ( null == keyword) ? "" : keyword);
		_name = ( ( null == name) ? "" : name);
		_hat = ( ( null == hat) ? "" : hat);
		_unit = ( ( null == unit) ? "" : unit);
		_time = ( ( null == time) ? "" : time);
		_subject = ( ( null == subject) ? "" : subject);
	}

	/**
	 * @param exchangeAlgebraInitialValue
	 */
	public ExchangeAlgebraInitialValue(ExchangeAlgebraInitialValue exchangeAlgebraInitialValue) {
		super();
		copy( exchangeAlgebraInitialValue);
	}

	/**
	 * For copy, cut and paste only.
	 * @param exchangeAlgebraInitialValue
	 */
	public ExchangeAlgebraInitialValue(ExchangeAlgebraInitialValue exchangeAlgebraInitialValue, int row) {
		super(row);
		copy( exchangeAlgebraInitialValue);
	}

	/**
	 * @param exchangeAlgebraInitialValue
	 */
	public void copy(ExchangeAlgebraInitialValue exchangeAlgebraInitialValue) {
		_value = exchangeAlgebraInitialValue._value;
		_keyword = exchangeAlgebraInitialValue._keyword;
		_name = exchangeAlgebraInitialValue._name;
		_hat = exchangeAlgebraInitialValue._hat;
		_unit = exchangeAlgebraInitialValue._unit;
		_time = exchangeAlgebraInitialValue._time;
		_subject = exchangeAlgebraInitialValue._subject;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if ( !( obj instanceof ExchangeAlgebraInitialValue))
			return false;

		ExchangeAlgebraInitialValue exchangeAlgebraInitialValue = ( ExchangeAlgebraInitialValue)obj;
		return ( _value.equals( exchangeAlgebraInitialValue._value)
			&& _keyword.equals( exchangeAlgebraInitialValue._keyword)
			&& _hat.equals( exchangeAlgebraInitialValue._hat)
			&& _name.equals( exchangeAlgebraInitialValue._name)
			&& _unit.equals( exchangeAlgebraInitialValue._unit)
			&& _time.equals( exchangeAlgebraInitialValue._time)
			&& _subject.equals( exchangeAlgebraInitialValue._subject));
	}

	/**
	 * @return
	 */
	public String get_initial_value() {
		return ( _value + get_base());
	}

	/**
	 * @return
	 */
	public String get_base() {
		if ( null != _keyword && !_keyword.equals( ""))
			return ( "[" + _keyword + "]");

		return ExchangeAlgebraCommand.toExbaseLiteral( _name + "-" + _hat + "-" + _unit + "-" + _time + "-" + _subject);
//		String base = ( _hat.equals( "HAT") ? "^" : "");
//		base += "<";
//		base += _name;
//		base += ",";
//		base += ( _unit.equals( "") ? "#" : _unit);
//		base += ",";
//		base += ( _time.equals( "") ? "#" : _time);
//		base += ",";
//		base += ( _subject.equals( "") ? "#" : _subject);
//		base += ">";
//
//		return base;
	}

	/**
	 * @param exchangeAlgebraInitialValue
	 * @return
	 */
	public boolean is_same_base(ExchangeAlgebraInitialValue exchangeAlgebraInitialValue) {
		return ( _keyword.equals( exchangeAlgebraInitialValue._keyword)
			&& _hat.equals( exchangeAlgebraInitialValue._hat)
			&& _name.equals( exchangeAlgebraInitialValue._name)
			&& _unit.equals( exchangeAlgebraInitialValue._unit)
			&& _time.equals( exchangeAlgebraInitialValue._time)
			&& _subject.equals( exchangeAlgebraInitialValue._subject));
	}

	/**
	 * @param value
	 * @return
	 */
	public static String is_correct(String value) {
		return NumberObject.is_correct( value, "real number");
	}

	/**
	 * @param objectBase
	 * @return
	 */
	public boolean contains(ObjectBase objectBase) {
		return contains( objectBase._kind, objectBase._name);
	}

	/**
	 * @param kind
	 * @param name
	 * @return
	 */
	public boolean contains(String kind, String name) {
		if ( !kind.equals( "keyword"))
			return false;

		return _keyword.equals( name);
	}

	/**
	 * @param type
	 * @param name
	 * @param newName
	 * @return
	 */
	public boolean update_object_name(String type, String name, String newName) {
		if ( !type.equals( "keyword"))
			return false;

		if ( !_keyword.equals( name))
			return false;

		_keyword = newName;
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
	 * @param name
	 * @param hat
	 * @param unit
	 * @param time
	 * @param subject
	 * @return
	 */
	public boolean contains(String name, String hat, String unit, String time, String subject) {
		return ( _name.equals( name)
			&& _hat.equals( hat)
			&& _unit.equals( unit)
			&& _time.equals( time)
			&& _subject.equals( subject));
	}

	/**
	 * @return
	 */
	public String get_initial_data() {
		return ( !_keyword.equals( "")
			? ( "\t" + _value + "-" + _keyword)
			: ( "\t" + _value + "-" + _name + "-" + _hat + "-" + _unit + "-" + _time + "-" + _subject));
	}

	/**
	 * @param prefix
	 * @return
	 */
	public String get_script(String prefix) {
		String script = ( " ; " + prefix + "addParamString " + Constant._exchangeAlgebraFactoryClassVariableName + "=" + _value);

		if ( !_keyword.equals( ""))
			script += ( " ; " + prefix + "equip " + _keyword + " ; " + prefix + "addParam " + Constant._exchangeAlgebraFactoryClassVariableName + "=" + _keyword + "=" + "java.lang.String");
		else {
			script += ( " ; " + prefix + "addParamString " + Constant._exchangeAlgebraFactoryClassVariableName + "=" + _name);
			script += ( " ; " + prefix + "addParamString " + Constant._exchangeAlgebraFactoryClassVariableName + "=" + _hat);
			script += ( " ; " + prefix + "addParamString " + Constant._exchangeAlgebraFactoryClassVariableName + "=" + _unit);
			script += ( " ; " + prefix + "addParamString " + Constant._exchangeAlgebraFactoryClassVariableName + "=" + _time);
			script += ( " ; " + prefix + "addParamString " + Constant._exchangeAlgebraFactoryClassVariableName + "=" + _subject);
		}

		script += ( " ; " + prefix + "invokeClass " + "=" + Constant._exchangeAlgebraFactoryClassVariableName + "=" + "add");

		return script;
	}

	/**
	 * @param index
	 * @param attributesImpl
	 */
	public void write(int index, AttributesImpl attributesImpl) {
		attributesImpl.addAttribute( null, null, "value" + index, "", Writer.escapeAttributeCharData( _value));
		attributesImpl.addAttribute( null, null, "keyword" + index, "", Writer.escapeAttributeCharData( _keyword));
		attributesImpl.addAttribute( null, null, "name" + index, "", Writer.escapeAttributeCharData( _name));
		attributesImpl.addAttribute( null, null, "hat" + index, "", Writer.escapeAttributeCharData( _hat));
		attributesImpl.addAttribute( null, null, "unit" + index, "", Writer.escapeAttributeCharData( _unit));
		attributesImpl.addAttribute( null, null, "time" + index, "", Writer.escapeAttributeCharData( _time));
		attributesImpl.addAttribute( null, null, "subject" + index, "", Writer.escapeAttributeCharData( _subject));
	}

	/**
	 * @param name
	 * @param entityData
	 * @return
	 */
	public boolean verify(String name, EntityData entityData) {
		if ( _keyword.equals( ""))
			return true;

		if ( entityData.has_same_object_name( "keyword", _keyword))
			return true;

		EntityBase entityBase;
		if ( entityData._type.equals( "agent"))
			entityBase = LayerManager.get_instance().get_agent( entityData._name);
		else
			entityBase = LayerManager.get_instance().get_spot( entityData._name);

		if ( null == entityBase || entityBase.is_multi() || !entityBase.has_same_object_name( "keyword", _keyword)) {
			String[] message = new String[] {
				( entityData._type.equals( "agent") ? "Agent" : "Spot"),
				"name = " + entityData._name,
				"exchange algebra name = " + name,
				"keyword " + _keyword + " does not exist!"
			};

			WarningManager.get_instance().add( message);

			return false;
		}

		return true;
	}

	/**
	 * @param name
	 * @param indices
	 * @param entityData
	 * @return
	 */
	public boolean verify(String name, Vector<int[]> indices, EntityData entityData) {
		if ( _keyword.equals( ""))
			return true;

		if ( !verify( "keyword", _keyword, indices, entityData)) {
			String[] message = new String[] {
				( entityData._type.equals( "agent") ? "Agent" : "Spot"),
				"name = " + entityData._name,
				"exchange algebra name = " + name,
				"keyword " + _keyword + " does not exist!"
			};

			WarningManager.get_instance().add( message);

			return false;
		}

		return true;
	}

	/**
	 * @param kind
	 * @param name
	 * @param indices
	 * @param entityData
	 * @return
	 */
	private boolean verify(String kind, String name, Vector<int[]> indices, EntityData entityData) {
		Vector<int[]> objectRanges = entityData.get_object_ranges( kind, name);

		EntityBase entityBase;
		if ( entityData._type.equals( "agent"))
			entityBase = LayerManager.get_instance().get_agent( entityData._name);
		else
			entityBase = LayerManager.get_instance().get_spot( entityData._name);

		if ( null != entityBase && entityBase.is_multi()) {
			Vector<int[]> ranges = entityBase.get_object_ranges( kind, name);
			CommonTool.merge_indices( ranges, objectRanges);
		}

		return CommonTool.contains_ranges( objectRanges, indices);
	}

	/**
	 * @param propertyPanelBaseMap
	 * @return
	 */
	public boolean can_paste(Map<String, PropertyPanelBase> propertyPanelBaseMap) {
		if ( _keyword.equals( ""))
			return true;

		PropertyPanelBase propertyPanelBase = propertyPanelBaseMap.get( "simple variable");
		return ( ( null != propertyPanelBase) && propertyPanelBase.contains( _keyword));
	}
}
