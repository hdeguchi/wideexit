/**
 * 
 */
package soars.application.simulator.data;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kurata
 *
 */
public class ChartData {

	/**
	 * 
	 */
	public String _name = "";

	/**
	 * 
	 */
	public String _title = "";

	/**
	 * 
	 */
	public String _XLabel = "";

	/**
	 * 
	 */
	public String _YLabel = "";

	/**
	 * 
	 */
	public Rectangle _windowRectangle = new Rectangle();

	/**
	 * 
	 */
	public boolean _XRange = false;

	/**
	 * 
	 */
	public double _XRangeMin = 0;

	/**
	 * 
	 */
	public double _XRangeMax = 0;

	/**
	 * 
	 */
	public boolean _YRange = false;

	/**
	 * 
	 */
	public double _YRangeMin = 0;

	/**
	 * 
	 */
	public double _YRangeMax = 0;

	/**
	 * 
	 */
	public List _datasets = new ArrayList();
}
