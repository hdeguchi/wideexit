/**
 * 
 */
package soars.application.simulator.data;

/**
 * @author kurata
 *
 */
public class Dataset {

	/**
	 * 
	 */
	public int _id = 0;

	/**
	 * 
	 */
	public String _legend = "";

	/**
	 * 
	 */
	public boolean _connect = true;

	/**
	 * 
	 */
	public String _value = "";

	/**
	 * @param id
	 * @param legend
	 */
	public Dataset(int id, String legend) {
		super();
		_id = id;
		_legend = legend;
	}

	/**
	 * @param id
	 * @param legend
	 * @param connect
	 */
	public Dataset(int id, String legend, boolean connect) {
		super();
		_id = id;
		_legend = legend;
		_connect = connect;
	}
}
