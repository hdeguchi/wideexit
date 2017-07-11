/*
 * 2004/12/16
 */
package soars.plugin.modelbuilder.chart.chart.main;

import java.io.IOException;

import ptolemy.plot.Plot;

/**
 * @author kurata
 */
public class Logger extends Plot {


	/**
	 * 
	 */
	private boolean _XRange = false;

	/**
	 * 
	 */
	private boolean _YRange = false;

	/**
	 * 
	 */
	public Logger() {
		super();
	}

	/**
	 * @param min
	 * @param max
	 */
	public void set_x_range(double min, double max) {
		setXRange( min, max);
		_XRange = true;
	}

	/**
	 * @param min
	 * @param max
	 */
	public void set_y_range(double min, double max) {
		setYRange( min, max);
		_YRange = true;
	}

	/**
	 * @param filename
	 * @return
	 */
	public boolean write_data(String filename) throws IOException {
		LogWriter logWriter = new LogWriter();
		return logWriter.write_data( this, _XRange, _YRange, _points, filename);
	}
}
