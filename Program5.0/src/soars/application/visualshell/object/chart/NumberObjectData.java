/**
 * 
 */
package soars.application.visualshell.object.chart;

import java.util.Vector;

import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.xml.sax.Writer;

/**
 * The numeric data class for the chart object
 * @author kurata / SOARS project
 */
public class NumberObjectData {

	/**
	 * Type of the object which contains the number variable for this numeric data.
	 */
	public String _type = "step";	// step, agent, spot

	/**
	 * Name of the object which contains the number variable for this numeric data.
	 */
	public String _objectName = "";

	/**
	 * Name of the number variable for this numeric data.
	 */
	public String _numberVariable = "";

	/**
	 * Creates this object.
	 */
	public NumberObjectData() {
		super();
	}

	/**
	 * Creates this object with the specified data.
	 * @param numberObjectData the specified data
	 */
	public NumberObjectData(NumberObjectData numberObjectData) {
		super();
		_type = numberObjectData._type;
		_objectName = numberObjectData._objectName;
		_numberVariable = numberObjectData._numberVariable;
	}

	/**
	 * Creates this object with the specified data.
	 * @param type the type of the object which contains the number variable for this numeric data
	 * @param objectName the name of the object which contains the number variable for this numeric data
	 * @param numberVariable the name of the number variable for this numeric data
	 */
	public NumberObjectData(String type, String objectName, String numberVariable) {
		super();
		_type = type;
		_objectName = objectName;
		_numberVariable = numberVariable;
	}

	/**
	 * Returns true if the agent or spot name can be adjusted.
	 * @param type the type of the object which contains the number variable for this numeric data
	 * @param headName the prefix of the agent or spot name
	 * @param ranges the ranges for the agent or spot number
	 * @param chartObject the chart object which has this object
	 * @return true if the agent or spot name can be adjusted
	 */
	public boolean can_adjust_name(String type, String headName, Vector<String[]> ranges, ChartObject chartObject) {
		if ( !_type.equals( type))
			return true;

		if ( !SoarsCommonTool.has_same_name( headName, ranges, _objectName))
			return true;

		String[] message = new String[] {
			"Chart",
			"name = " + chartObject._name,
			_type + " = " + _objectName
		};

		WarningManager.get_instance().add( message);

		return false;
	}

	/**
	 * Returns true if it is possible to update the specified agent or spot name with the new one.
	 * @param type the type of the object which contains the number variable for this numeric data
	 * @param headName the prefix of the specified agent or spot name
	 * @param ranges the ranges for the specified agent or spot number
	 * @param newHeadName the prefix of the new agent or spot name
	 * @param newRanges the ranges for the new agent or spot number
	 * @param chartObject the chart object which has this object
	 * @return true if it is possible to update the specified agent or spot name with the new one
	 */
	public boolean can_adjust_name(String type, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges, ChartObject chartObject) {
		if ( !_type.equals( type))
			return true;

		if ( !SoarsCommonTool.has_same_name( headName, ranges, _objectName))
			return true;

		if ( SoarsCommonTool.has_same_name( newHeadName, newRanges, _objectName))
			return true;

		String[] message = new String[] {
			"Chart",
			"name = " + chartObject._name,
			_type + " = " + _objectName
		};

		WarningManager.get_instance().add( message);

		return false;
	}

	/**
	 * Invoked when the agent or spot object has been changed.
	 * @param type the type of the object which contains the number variable for this numeric data
	 * @param newName the new agent name
	 * @param originalName the original agent or spot name
	 * @param headName the prefix of the agent or spot name
	 * @param ranges the ranges for the agent or spot number
	 * @param newHeadName the prefix of the new agent or spot name
	 * @param newRanges the ranges for the new agent or spot number
	 * @return
	 */
	public boolean update_name_and_number(String type, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		if ( !_type.equals( type))
			return false;

		if ( !SoarsCommonTool.has_same_name( headName, ranges, _objectName))
			return false;

		if ( SoarsCommonTool.has_same_name( newHeadName, newRanges, _objectName))
			return false;

		_objectName = newName + _objectName.substring( originalName.length());

		return true;
	}

	/**
	 * Returns true if the specified number variable can be removed.
	 * @param type the type of the object which contains the number variable for this numeric data
	 * @param numberVariable the specified number variable name
	 * @param headName the prefix of the agent or spot name
	 * @param ranges the ranges for the agent or spot number
	 * @param chartObject the chart object which has this object
	 * @return true if the specified number variable can be removed
	 */
	public boolean can_remove(String type, String numberVariable, String headName, Vector<String[]> ranges, ChartObject chartObject) {
		if ( !_type.equals( "agent") && !_type.equals( "spot"))
			return true;

		if ( !SoarsCommonTool.has_same_name( headName, ranges, _objectName))
			return true;

		if ( !_numberVariable.equals( numberVariable))
			return true;

		String[] message = new String[] {
			"Chart",
			"name = " + chartObject._name,
			_type + " = " + _objectName,
			"number object = " + _numberVariable
		};

		WarningManager.get_instance().add( message);

		return false;
	}

	/**
	 * Returns true for updating the specified number variable name with the new one successfully.
	 * @param entity "agent" or "spot"
	 * @param name the specified name of the number variable
	 * @param newName the new name of the number variable
	 * @return true for updating the specified number variable name with the new one successfully
	 */
	public boolean update_object_name(String entity, String name, String newName) {
		if ( !_type.equals( entity))
			return false;

		if ( !_numberVariable.equals( name))
			return false;

		_numberVariable = newName;

		return true;
	}

	/**
	 * Returns true if any agent or spot in the specified objects has the number variable for this numeric data.
	 * @param drawObjects the specified objects
	 * @param chartObject the chart object which has this object
	 * @return true if any agent or spot in the specified objects has the number variable for this numeric data
	 */
	public boolean can_paste(Layer drawObjects, ChartObject chartObject) {
		if ( _objectName.equals( ""))
			return true;

		EntityBase entityBase = null;
		if ( _type.equals( "agent"))
			entityBase = drawObjects.get_agent_has_this_name( _objectName);
		else if ( _type.equals( "spot"))
			entityBase = drawObjects.get_spot_has_this_name( _objectName);

		if ( null == entityBase) {
			String[] message = new String[] {
				"Chart",
				"name = " + chartObject._name,
				_type + " name = " + _objectName
			};

			WarningManager.get_instance().add( message);
			return false;
		} else {
			if ( !entityBase.has_same_object_name( "number object", _numberVariable)) {
				String[] message = new String[] {
					"Chart",
					"name = " + chartObject._name,
					_type + " name = " + _objectName,
					"number object name = " + _numberVariable
				};

				WarningManager.get_instance().add( message);
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns true for writing this object data successfully.
	 * @param prifix the horizontal axis of vertical axis
	 * @param attributesImpl the default implementation of the SAX2 Attributes interface
	 * @param writer the abstract class for writing to character streams
	 */
	public void write(String prifix, AttributesImpl attributesImpl, Writer writer) {
		attributesImpl.addAttribute( null, null, prifix + "type", "", Writer.escapeAttributeCharData( _type));
		attributesImpl.addAttribute( null, null, prifix + "object_name", "", Writer.escapeAttributeCharData( _objectName));
		attributesImpl.addAttribute( null, null, prifix + "number_variable", "", Writer.escapeAttributeCharData( _numberVariable));
	}
}
