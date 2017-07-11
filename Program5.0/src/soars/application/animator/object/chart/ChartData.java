/**
 * 
 */
package soars.application.animator.object.chart;

import org.xml.sax.helpers.AttributesImpl;

import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class ChartData {

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
	 * @param type
	 * @param objectName
	 * @param numberVariable
	 */
	public ChartData(String type, String objectName, String numberVariable) {
		super();
		_type = type;
		_objectName = objectName;
		_numberVariable = numberVariable;
	}

	/**
	 * @param chartData
	 */
	public ChartData(ChartData chartData) {
		super();
		_type = chartData._type;
		_objectName = chartData._objectName;
		_numberVariable = chartData._numberVariable;
	}

	/**
	 * @return
	 */
	public String getLegendWord() {
		return ( _numberVariable.equals( "") ? _type : _numberVariable);
	}

	/**
	 * @param attributesImpl
	 * @param index
	 */
	public void write(AttributesImpl attributesImpl, int index) {
		attributesImpl.addAttribute( null, null, "type" + String.valueOf( index), "", Writer.escapeAttributeCharData( _type));
		attributesImpl.addAttribute( null, null, "object_name" + String.valueOf( index), "", Writer.escapeAttributeCharData( _objectName));
		attributesImpl.addAttribute( null, null, "number_variable" + String.valueOf( index), "", Writer.escapeAttributeCharData( _numberVariable));
	}
}
