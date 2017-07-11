/**
 * 
 */
package soars.application.visualshell.object.entity.base.object.exchange_algebra;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.file.importer.initial.entity.EntityData;
import soars.application.visualshell.file.importer.initial.entity.EntityDataMap;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.object.base.ObjectBase;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class ExchangeAlgebraObject extends ObjectBase {

	/**
	 * 
	 */
	public List<ExchangeAlgebraInitialValue> _exchangeAlgebraInitialValues = new ArrayList<ExchangeAlgebraInitialValue>();

	/**
	 * 
	 */
	public ExchangeAlgebraObject() {
		super("exchange algebra");
	}

	/**
	 * @param name
	 */
	public ExchangeAlgebraObject(String name) {
		super("exchange algebra", name);
	}

	/**
	 * @param exchangeAlgebraObject
	 */
	public ExchangeAlgebraObject(ExchangeAlgebraObject exchangeAlgebraObject) {
		super(exchangeAlgebraObject);	
		for ( ExchangeAlgebraInitialValue exchangeAlgebraInitialValue:exchangeAlgebraObject._exchangeAlgebraInitialValues)
			_exchangeAlgebraInitialValues.add( new ExchangeAlgebraInitialValue( exchangeAlgebraInitialValue));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#copy(soars.application.visualshell.object.entiy.base.object.base.ObjectBase)
	 */
	public void copy(ObjectBase objectBase) {
		// TODO Auto-generated method stub
		super.copy(objectBase);
		ExchangeAlgebraObject exchangeAlgebraObject = ( ExchangeAlgebraObject)objectBase;
		for ( ExchangeAlgebraInitialValue exchangeAlgebraInitialValue:exchangeAlgebraObject._exchangeAlgebraInitialValues)
			_exchangeAlgebraInitialValues.add( new ExchangeAlgebraInitialValue( exchangeAlgebraInitialValue));
	}

	/* (non-Javadoc)
	 * @see java.util.Vector#equals(java.lang.Object)
	 */
	public synchronized boolean equals(Object object) {
		if ( !( object instanceof ExchangeAlgebraObject))
			return false;

		if ( !super.equals( object))
			return false;

		ExchangeAlgebraObject exchangeAlgebraObject = ( ExchangeAlgebraObject)object;

		if ( _exchangeAlgebraInitialValues.size() != exchangeAlgebraObject._exchangeAlgebraInitialValues.size())
			return false;

		for ( int i = 0; i < _exchangeAlgebraInitialValues.size(); ++i) {
			if ( !_exchangeAlgebraInitialValues.get( i).equals( exchangeAlgebraObject._exchangeAlgebraInitialValues.get( i)))
				return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#contains(soars.application.visualshell.object.entiy.base.object.base.ObjectBase)
	 */
	public boolean contains(ObjectBase objectBase) {
		for ( ExchangeAlgebraInitialValue exchangeAlgebraInitialValue:_exchangeAlgebraInitialValues) {
			if ( exchangeAlgebraInitialValue.contains( objectBase))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#update_object_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean update_object_name(String type, String name, String newName) {
		for ( ExchangeAlgebraInitialValue exchangeAlgebraInitialValue:_exchangeAlgebraInitialValues) {
			if ( exchangeAlgebraInitialValue.update_object_name( type, name, newName))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#contains_this_alias(java.lang.String)
	 */
	public boolean contains_this_alias(String alias) {
		for ( ExchangeAlgebraInitialValue exchangeAlgebraInitialValue:_exchangeAlgebraInitialValues) {
			if ( exchangeAlgebraInitialValue.contains_this_alias( alias))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_initial_values(java.util.Vector)
	 */
	public void get_initial_values1(Vector<String> initialValues) {
		for ( ExchangeAlgebraInitialValue exchangeAlgebraInitialValue:_exchangeAlgebraInitialValues)
			exchangeAlgebraInitialValue.get_initial_values1( initialValues);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_initial_values2(java.util.Vector)
	 */
	public void get_initial_values2(Vector<String> initialValues) {
		for ( ExchangeAlgebraInitialValue exchangeAlgebraInitialValue:_exchangeAlgebraInitialValues)
			exchangeAlgebraInitialValue.get_initial_values2( initialValues);
	}

	/**
	 * @return
	 */
	public String get_initial_values() {
		String initial_values = "";
		for ( int i = 0; i < _exchangeAlgebraInitialValues.size(); ++i)
			initial_values += ( ( ( 0 == i) ? "" : "+") + _exchangeAlgebraInitialValues.get( i).get_initial_value());

		return initial_values;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_initial_data()
	 */
	public String get_initial_data() {
		String script = ( ResourceManager.get_instance().get( "initial.data.exchange.algebra") + "\t" + _name);
		for ( ExchangeAlgebraInitialValue exchangeAlgebraInitialValue:_exchangeAlgebraInitialValues)
			script += exchangeAlgebraInitialValue.get_initial_data();

		return ( script += Constant._lineSeparator);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_script(java.lang.String, java.nio.IntBuffer, soars.application.visualshell.object.experiment.InitialValueMap)
	 */
	public String get_script(String prefix, IntBuffer counter, InitialValueMap initialValueMap) {
		String script = ( "\t" + prefix + "setClass " + _name + "=" + Constant._exchangeAlgebraClassname);
		script += get_script( prefix);
		script += ( " ; " + prefix + "logEquip " + _name);
		counter.put( 0, counter.get( 0) + 1);
		return script;
	}

	/**
	 * @param prefix
	 * @return
	 */
	private String get_script(String prefix) {
		String script = "";
		for ( ExchangeAlgebraInitialValue exchangeAlgebraInitialValue:_exchangeAlgebraInitialValues)
			script += exchangeAlgebraInitialValue.get_script( prefix);

		script += ( " ; " + prefix + "invokeClass " + _name + "=" + Constant._exchangeAlgebraFactoryClassVariableName+ "=" + "toExalge");

		return script;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#write(soars.common.utility.xml.sax.Writer)
	 */
	public void write(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
		write( writer, attributesImpl);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#write(soars.common.utility.xml.sax.Writer, java.lang.String)
	 */
	public void write(Writer writer, String number) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "number", "", Writer.escapeAttributeCharData( number));
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
		write( writer, attributesImpl);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#write(soars.common.utility.xml.sax.Writer, org.xml.sax.helpers.AttributesImpl)
	 */
	protected void write(Writer writer, AttributesImpl attributesImpl) throws SAXException {
		for ( int i = 0; i < _exchangeAlgebraInitialValues.size(); ++i)
			_exchangeAlgebraInitialValues.get( i).write( i, attributesImpl);

		super.write(writer, attributesImpl);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#verify(soars.application.visualshell.file.importer.initial.entiy.EntityDataMap, soars.application.visualshell.file.importer.initial.entiy.EntityDataMap, soars.application.visualshell.file.importer.initial.entiy.EntityData)
	 */
	public boolean verify(EntityDataMap agentDataMap, EntityDataMap spotDataMap, EntityData entityData) {
		for ( ExchangeAlgebraInitialValue exchangeAlgebraInitialValue:_exchangeAlgebraInitialValues) {
			if ( !exchangeAlgebraInitialValue.verify( _name, entityData))
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#verify(soars.application.visualshell.file.importer.initial.entiy.EntityDataMap, soars.application.visualshell.file.importer.initial.entiy.EntityDataMap, java.util.Vector, soars.application.visualshell.file.importer.initial.entiy.EntityData)
	 */
	public boolean verify(EntityDataMap agentDataMap, EntityDataMap spotDataMap, Vector<int[]> indices, EntityData entityData) {
		for ( ExchangeAlgebraInitialValue exchangeAlgebraInitialValue:_exchangeAlgebraInitialValues) {
			if ( !exchangeAlgebraInitialValue.verify( _name, indices, entityData))
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#print()
	 */
	public void print() {
		if ( _exchangeAlgebraInitialValues.isEmpty())
			System.out.println( "\t" + _name);
		else {
			for ( ExchangeAlgebraInitialValue exchangeAlgebraInitialValue:_exchangeAlgebraInitialValues)
				System.out.println( "\t" + _name + ", " + exchangeAlgebraInitialValue._value + ", " + exchangeAlgebraInitialValue._keyword + ", " + exchangeAlgebraInitialValue._name + ", " + exchangeAlgebraInitialValue._hat + ", " + exchangeAlgebraInitialValue._unit + ", " + exchangeAlgebraInitialValue._time + ", " + exchangeAlgebraInitialValue._subject);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#print(java.util.Vector)
	 */
	public void print(Vector<int[]> indices) {
		String key = "";
		for ( ExchangeAlgebraInitialValue exchangeAlgebraInitialValue:_exchangeAlgebraInitialValues)
			key += ( ( key.equals( "") ? "" : " ") + exchangeAlgebraInitialValue._value + ", " + exchangeAlgebraInitialValue._keyword + ", " + exchangeAlgebraInitialValue._name + ", " + exchangeAlgebraInitialValue._hat + ", " + exchangeAlgebraInitialValue._unit + ", " + exchangeAlgebraInitialValue._time + ", " + exchangeAlgebraInitialValue._subject);

		if ( indices.isEmpty()) {
			System.out.println( "\t" + _name + ", " + key);
			return;
		}

		for ( int i = 0; i < indices.size(); ++i) {
			int[] range = ( int[])indices.get( i);
			System.out.println( "\t" + _name + ", " + key + ", " + range[ 0] + "-" + range[ 1]);
		}
	}

	/**
	 * @param propertyPanelBaseMap
	 * @return
	 */
	public boolean can_paste(Map<String, PropertyPanelBase> propertyPanelBaseMap) {
		for ( ExchangeAlgebraInitialValue exchangeAlgebraInitialValue:_exchangeAlgebraInitialValues) {
			if ( !exchangeAlgebraInitialValue.can_paste( propertyPanelBaseMap))
				return false;
		}
		return true;
	}
}
