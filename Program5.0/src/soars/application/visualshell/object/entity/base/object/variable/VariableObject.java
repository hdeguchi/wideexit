/*
 * Created on 2006/10/13
 */
package soars.application.visualshell.object.entity.base.object.variable;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.file.importer.initial.entity.EntityData;
import soars.application.visualshell.file.importer.initial.entity.EntityDataMap;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.object.base.ObjectBase;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 */
public class VariableObject extends ObjectBase {

	/**
	 * 
	 */
	public List<VariableInitialValue> _variableInitialValues = new ArrayList<VariableInitialValue>();

	/**
	 * @param kind
	 */
	public VariableObject(String kind) {
		super(kind);
	}

	/**
	 * @param kind
	 * @param name
	 */
	public VariableObject(String kind, String name) {
		super(kind, name);
	}

	/**
	 * @param kind
	 * @param name
	 * @param comment
	 */
	public VariableObject(String kind, String name, String comment) {
		super(kind, name, comment);
	}

	/**
	 * @param variableObject
	 */
	public VariableObject(VariableObject variableObject) {
		super(variableObject);
		for ( VariableInitialValue variableInitialValue:variableObject._variableInitialValues)
			_variableInitialValues.add( new VariableInitialValue( variableInitialValue));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#copy(soars.application.visualshell.object.entiy.base.object.base.ObjectBase)
	 */
	public void copy(ObjectBase objectBase) {
		super.copy(objectBase);
		VariableObject variableObject = ( VariableObject)objectBase;
		for ( VariableInitialValue variableInitialValue:variableObject._variableInitialValues)
			_variableInitialValues.add( new VariableInitialValue( variableInitialValue));
	}

	/* (Non Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public synchronized boolean equals(Object object) {
		if ( !( object instanceof VariableObject))
			return false;

		if ( !super.equals( object))
			return false;

		VariableObject variableObject = ( VariableObject)object;

		if ( _variableInitialValues.size() != variableObject._variableInitialValues.size())
			return false;

		for ( int i = 0; i < _variableInitialValues.size(); ++i) {
			if ( !_variableInitialValues.get( i).equals( variableObject._variableInitialValues.get( i)))
				return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#contains(soars.application.visualshell.object.entiy.base.object.base.ObjectBase)
	 */
	public boolean contains(ObjectBase objectBase) {
		for ( VariableInitialValue variableInitialValue:_variableInitialValues) {
			if ( variableInitialValue.contains( objectBase))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#contains(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, soars.application.visualshell.object.entiy.base.EntityBase)
	 */
	public boolean contains(String kind, String type, String headName, Vector<String[]> ranges, String name, EntityBase entityBase) {
		for ( VariableInitialValue variableInitialValue:_variableInitialValues) {
			if ( variableInitialValue.contains( kind, name, type, headName, ranges, entityBase))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#contains(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector, soars.application.visualshell.object.entiy.base.EntityBase)
	 */
	public boolean contains(String kind, String name, String type, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges, EntityBase entityBase) {
		for ( VariableInitialValue variableInitialValue:_variableInitialValues) {
			if ( variableInitialValue.contains( kind, name, type, headName, ranges, newHeadName, newRanges, entityBase))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#can_adjust_name(java.lang.String, java.lang.String, java.util.Vector, soars.application.visualshell.object.entiy.base.EntityBase)
	 */
	public boolean can_adjust_name(String type, String headName, Vector<String[]> ranges, EntityBase entityBase) {
		for ( VariableInitialValue variableInitialValue:_variableInitialValues) {
			if ( !variableInitialValue.can_adjust_name( _kind, _name, type, headName, ranges, entityBase))
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#can_adjust_name(java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector, soars.application.visualshell.object.entiy.base.EntityBase)
	 */
	public boolean can_adjust_name(String type, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges, EntityBase entityBase) {
		for ( VariableInitialValue variableInitialValue:_variableInitialValues) {
			if ( !variableInitialValue.can_adjust_name( _kind, _name, type, headName, ranges, newHeadName, newRanges, entityBase))
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#update_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean update_name_and_number(String type, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = false;
		for ( VariableInitialValue variableInitialValue:_variableInitialValues) {
			if ( variableInitialValue.update_name_and_number( type, newName, originalName, headName, ranges, newHeadName, newRanges))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#update_object_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean update_object_name(String type, String name, String newName) {
		boolean result = false;
		for ( VariableInitialValue variableInitialValue:_variableInitialValues) {
			if ( variableInitialValue.update_object_name( type, name, newName))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#contains_this_alias(java.lang.String)
	 */
	public boolean contains_this_alias(String alias) {
		for ( VariableInitialValue variableInitialValue:_variableInitialValues) {
			if ( variableInitialValue.contains_this_alias( alias))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_initial_values1(java.util.Vector)
	 */
	public void get_initial_values1(Vector<String> initialValues) {
		for ( VariableInitialValue variableInitialValue:_variableInitialValues)
			variableInitialValue.get_initial_values1( initialValues);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_initial_values2(java.util.Vector)
	 */
	public void get_initial_values2(Vector<String> initialValues) {
		for ( VariableInitialValue variableInitialValue:_variableInitialValues)
			variableInitialValue.get_initial_values2( initialValues);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#can_paste(soars.application.visualshell.object.entiy.base.EntityBase, soars.application.visualshell.layer.Layer)
	 */
	public boolean can_paste(EntityBase entityBase, Layer drawObjects) {
		boolean result = true;
		for ( VariableInitialValue variableInitialValue:_variableInitialValues) {
			if ( !variableInitialValue.can_paste( drawObjects)) {
				result = false;
				String[] message = new String[] {
					( ( entityBase instanceof AgentObject) ? "Agent" : "Spot"),
					"name = " + entityBase._name,
					_kind + " " + _name + " has " + variableInitialValue._value
				};

				WarningManager.get_instance().add( message);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_initial_data()
	 */
	public String get_initial_data() {
		String script = ( ResourceManager.get_instance().get( _kind.equals( "collection") ? "initial.data.collection" : "initial.data.list") + "\t" + _name);
		for ( VariableInitialValue variableInitialValue:_variableInitialValues)
			script += variableInitialValue.get_initial_data();
		return ( script + Constant._lineSeparator);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_script(java.lang.String, java.nio.IntBuffer, soars.application.visualshell.object.experiment.InitialValueMap)
	 */
	public String get_script(String prefix, IntBuffer counter, InitialValueMap initialValueMap) {
		String script = ( "\t" + prefix + "setEquip " + _name
			+ ( _kind.equals( "collection") ? "=java.util.HashSet ; " : "=java.util.LinkedList ; ")
			+ prefix + "logEquip " + _name);
		counter.put( 0, counter.get( 0) + 1);
		return script;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_initial_values_script(java.lang.String, soars.application.visualshell.object.experiment.InitialValueMap)
	 */
	public String get_initial_values_script(String prefix, InitialValueMap initialValueMap) {
		String script = "";
		for ( VariableInitialValue variableInitialValue:_variableInitialValues)
			script += ( ( script.equals( "") ? "" : " ; ") + variableInitialValue.get_script( _kind, prefix, _name, initialValueMap));
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
		for ( int i = 0; i < _variableInitialValues.size(); ++i)
			_variableInitialValues.get( i).write( i, attributesImpl);

		super.write(writer, attributesImpl);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#verify(soars.application.visualshell.file.importer.initial.entiy.EntityDataMap, soars.application.visualshell.file.importer.initial.entiy.EntityDataMap, soars.application.visualshell.file.importer.initial.entiy.EntityData)
	 */
	public boolean verify(EntityDataMap agentDataMap, EntityDataMap spotDataMap, EntityData entityData) {
		for ( VariableInitialValue variableInitialValue:_variableInitialValues) {
			if ( !variableInitialValue.verify( _kind + " name = " + _name, agentDataMap, spotDataMap, entityData))
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#verify(soars.application.visualshell.file.importer.initial.entiy.EntityDataMap, soars.application.visualshell.file.importer.initial.entiy.EntityDataMap, java.util.Vector, soars.application.visualshell.file.importer.initial.entiy.EntityData)
	 */
	public boolean verify(EntityDataMap agentDataMap, EntityDataMap spotDataMap, Vector<int[]> indices, EntityData entityData) {
		for ( VariableInitialValue variableInitialValue:_variableInitialValues) {
			if ( !variableInitialValue.verify( _kind + " name = " + _name, agentDataMap, spotDataMap, indices, entityData))
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#print()
	 */
	public void print() {
		if ( _variableInitialValues.isEmpty()) {
			System.out.println( "\t" + _name);
			return;
		}

		for ( VariableInitialValue variableInitialValue:_variableInitialValues)
			System.out.println( "\t" + _name + ", " + variableInitialValue._type + ", " + variableInitialValue._value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#print(java.util.Vector)
	 */
	public void print(Vector<int[]> indices) {
		String key = "";
		for ( VariableInitialValue variableInitialValue:_variableInitialValues)
			key += ( ( key.equals( "") ? "" : " ") + variableInitialValue._type + ":" + variableInitialValue._value);

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
	 * @param objectBase
	 * @param objectBases
	 * @param entityBase
	 * @return
	 */
	public boolean can_paste(Map<String, PropertyPanelBase> propertyPanelBaseMap, ObjectBase objectBase, List<ObjectBase> objectBases, EntityBase entityBase) {
		for ( VariableInitialValue variableInitialValue:_variableInitialValues) {
			if ( !variableInitialValue.can_paste( propertyPanelBaseMap, objectBase, objectBases, entityBase))
				return false;
		}
		return true;
	}
}
