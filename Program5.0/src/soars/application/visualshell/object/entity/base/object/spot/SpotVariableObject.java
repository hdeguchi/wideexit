/**
 * 
 */
package soars.application.visualshell.object.entity.base.object.spot;

import java.nio.IntBuffer;
import java.util.Vector;

import soars.application.visualshell.file.importer.initial.entity.EntityData;
import soars.application.visualshell.file.importer.initial.entity.EntityDataMap;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.object.base.SimpleVariableObject;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.soars.warning.WarningManager;

/**
 * The spot variable object class
 * @author kurata / SOARS project
 */
public class SpotVariableObject extends SimpleVariableObject {

	/**
	 * Creates this object.
	 */
	public SpotVariableObject() {
		super("spot variable");
	}

	/**
	 * Creates this object with the spcified name and initial value.
	 * @param name the specified name
	 * @param initialValue the specified initial value
	 */
	public SpotVariableObject(String name, String initialValue) {
		super("spot variable", name, initialValue);
	}

	/**
	 * Creates this object with the spcified data.
	 * @param name the specified name
	 * @param initialValue the specified initial value
	 * @param comment the specified comment
	 */
	public SpotVariableObject(String name, String initialValue, String comment) {
		super("spot variable", name, initialValue, comment);
	}

	/**
	 * Creates this object with the spcified data.
	 * @param spotVariableObject the spcified data
	 */
	public SpotVariableObject(SpotVariableObject spotVariableObject) {
		super(spotVariableObject);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if ( !( object instanceof SpotVariableObject))
			return false;

		return super.equals( object);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#can_adjust_name(java.lang.String, java.lang.String, java.util.Vector, soars.application.visualshell.object.entiy.base.EntityBase)
	 */
	public boolean can_adjust_name(String type, String headName, Vector<String[]> ranges, EntityBase entityBase) {
		if ( !type.equals( "spot"))
			return true;

		if ( contains( headName, ranges)) {
			String[] message = new String[] {
				( ( entityBase instanceof AgentObject) ? "Agent" : "Spot"),
				"name = " + entityBase._name,
				"Spot variable " + _name,
				"value = " + _initialValue
			};

			WarningManager.get_instance().add( message);

			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#can_adjust_name(java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector, soars.application.visualshell.object.entiy.base.EntityBase)
	 */
	public boolean can_adjust_name(String type, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges, EntityBase entityBase) {
		if ( !type.equals( "spot"))
			return true;

		if ( contains( headName, ranges) && !contains( newHeadName, newRanges)) {
			String[] message = new String[] {
				( ( entityBase instanceof AgentObject) ? "Agent" : "Spot"),
				"name = " + entityBase._name,
				"Spot variable " + _name,
				"value = " + _initialValue
			};

			WarningManager.get_instance().add( message);

			return false;
		}
		return true;
	}

	/**
	 * @param headName
	 * @param ranges
	 * @return
	 */
	private boolean contains(String headName, Vector<String[]> ranges) {
		if ( _initialValue.equals( ""))
			return false;

		return SoarsCommonTool.has_same_name( headName, ranges, _initialValue);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#update_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean update_name_and_number(String type, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		if ( !type.equals( "spot"))
			return false;

		if ( _initialValue.equals( ""))
			return false;

		if ( !SoarsCommonTool.has_same_name( headName, ranges, _initialValue))
			return false;

		if ( SoarsCommonTool.has_same_name( newHeadName, newRanges, _initialValue))
			return false;

		_initialValue = newName + _initialValue.substring( originalName.length());

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#can_paste(soars.application.visualshell.object.entiy.base.EntityBase, soars.application.visualshell.layer.Layer)
	 */
	public boolean can_paste(EntityBase entityBase, Layer drawObjects) {
		if ( _initialValue.equals( ""))
			return true;

		if ( null != drawObjects.get_spot_has_this_name( _initialValue))
			return true;

		String[] message = new String[] {
			( ( entityBase instanceof AgentObject) ? "Agent" : "Spot"),
			"name = " + entityBase._name,
			_name + " has " + _initialValue
		};

		WarningManager.get_instance().add( message);

		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_initial_data()
	 */
	public String get_initial_data() {
		return get_initial_data( ResourceManager.get_instance().get( "initial.data.spot.variable"));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_script(java.lang.String, java.nio.IntBuffer, soars.application.visualshell.object.experiment.InitialValueMap)
	 */
	public String get_script(String prefix, IntBuffer counter, InitialValueMap initialValueMap) {
		String script = ( "\t<>setSpot " + _name + " ; " + prefix + "logEquip " + _name);
		counter.put( 0, counter.get( 0) + 1);
		return script;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_initial_values_script(java.lang.String, soars.application.visualshell.object.experiment.InitialValueMap)
	 */
	public String get_initial_values_script(String prefix, InitialValueMap initialValueMap) {
		return ( _initialValue.equals( "") ? "" : ( "<" + _initialValue + ">setSpot " + _name));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#verify(soars.application.visualshell.file.importer.initial.entiy.EntityDataMap, soars.application.visualshell.file.importer.initial.entiy.EntityDataMap, soars.application.visualshell.file.importer.initial.entiy.EntityData)
	 */
	public boolean verify(EntityDataMap agentDataMap, EntityDataMap spotDataMap, EntityData entityData) {
		if ( _initialValue.equals( ""))
			return true;

		if ( !spotDataMap.contains( _initialValue) && null == LayerManager.get_instance().get_spot_has_this_name( _initialValue)) {
			String[] message = new String[] {
				( entityData._type.equals( "agent") ? "Agent" : "Spot"),
				"name = " + entityData._name,
				"spot variable name = " + _name,
				"spot " + _initialValue + " does not exist!"
			};

			WarningManager.get_instance().add( message);

			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#verify(soars.application.visualshell.file.importer.initial.entiy.EntityDataMap, soars.application.visualshell.file.importer.initial.entiy.EntityDataMap, java.util.Vector, soars.application.visualshell.file.importer.initial.entiy.EntityData)
	 */
	public boolean verify(EntityDataMap agentDataMap, EntityDataMap spotDataMap, Vector<int[]> indices, EntityData entityData) {
		return verify( agentDataMap, spotDataMap, entityData);
	}
}
