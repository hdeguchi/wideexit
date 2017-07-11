/*
 * 2005/01/13
 */
package soars.plugin.modelbuilder.chart.chart.main;

import java.io.IOException;

/**
 * @author kurata
 */
public interface IChart {

	/**
	 * @param name
	 * @return
	 */
	public boolean create(String name);

	/**
	 * @param dataset
	 * @param x
	 * @param y
	 */
	public void append(int dataset, double x, double y);

	/**
	 * @param dataset
	 * @param x
	 * @param y
	 * @param connected
	 */
	public void append(int dataset, double x, double y, boolean connected);

	/**
	 * @param dataset
	 * @param x
	 * @param y
	 */
	public void append(Integer dataset, Double x, Double y);

	/**
	 * @param dataset
	 * @param x
	 * @param y
	 * @param connected
	 */
	public void append(Integer dataset, Double x, Double y, Boolean connected);

	/**
	 * @param dataset
	 * @param label
	 */
	public void addLegend(int dataset, String label);

	/**
	 * @param dataset
	 * @param label
	 */
	public void addLegend(Integer dataset, String label);

	/**
	 * @param label
	 */
	public void setXLabel(String label);

	/**
	 * @param label
	 */
	public void setYLabel(String label);

	/**
	 * @param min
	 * @param max
	 */
	public void setXRange(double min, double max);

	/**
	 * @param min
	 * @param max
	 */
	public void setXRange(Double min, Double max);

	/**
	 * @param min
	 * @param max
	 */
	public void setYRange(double min, double max);

	/**
	 * @param min
	 * @param max
	 */
	public void setYRange(Double min, Double max);

	/**
	 * @param filename
	 */
	public boolean write(String filename) throws IOException;

	/**
	 * @param title
	 */
	public void setTitle(String title);
}
