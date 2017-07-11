/**
 * 
 */
package soars.application.simulator.data;

/**
 * @author kurata
 *
 */
public class LogData {

	/**
	 * 
	 */
	public String _name = "";

	/**
	 * 
	 */
	public String _value = "";

	/**
	 * @param name
	 */
	public LogData(String name) {
		super();
		_name = name;
	}

	/**
	 * @param name
	 * @param value
	 */
	public LogData(String name, String value) {
		super();
		_name = name;
		_value = value;
	}
}
