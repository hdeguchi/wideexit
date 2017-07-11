/*
 * 2005/01/13
 */
package soars.plugin.modelbuilder.chart.chart.main;

import java.io.IOException;

/**
 * @author kurata
 */
public class ChartLogger implements IChart {

	/**
	 * 
	 */
	public String _name = "";

	/**
	 * 
	 */
	private Logger _logger = new Logger();

	/**
	 * 
	 */
	private Object _Lock = new Object();

	/**
	 * 
	 */
	public ChartLogger() {
		super();
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#create(java.lang.String)
	 */
	public boolean create(String name) {
		_name = name;

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#append(int, double, double)
	 */
	public void append(int dataset, double x, double y) {
		append( dataset, x, y, true);
	}

	/* (Non Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#append(int, double, double, boolean)
	 */
	public void append(int dataset, double x, double y, boolean connected) {
		synchronized( _Lock) {
			if ( !connected && !_logger.getMarksStyle().equals( "dots"))
				_logger.setMarksStyle( "dots", dataset);

			_logger.addPoint( dataset, x, y, connected);
		}
	}

	/* (Non Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#append(java.lang.Integer, java.lang.Double, java.lang.Double)
	 */
	public void append(Integer dataset, Double x, Double y) {
		append( dataset.intValue(), x.doubleValue(), y.doubleValue(), true);
	}

	/* (Non Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#append(java.lang.Integer, java.lang.Double, java.lang.Double, java.lang.Boolean)
	 */
	public void append(Integer dataset, Double x, Double y, Boolean connected) {
		append( dataset.intValue(), x.doubleValue(), y.doubleValue(), connected.booleanValue());
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#addLegend(int, java.lang.String)
	 */
	public void addLegend(int dataset, String label) {
		if ( null == _logger)
			return;

		_logger.addLegend( dataset, label);
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#addLegend(java.lang.Integer, java.lang.String)
	 */
	public void addLegend(Integer dataset, String label) {
		addLegend( dataset.intValue(), label);
	}

	/* (Non Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#setXLabel(java.lang.String)
	 */
	public void setXLabel(String label) {
		_logger.setXLabel( label);
	}

	/* (Non Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#setYLabel(java.lang.String)
	 */
	public void setYLabel(String label) {
		_logger.setYLabel( label);
	}

	/* (Non Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#setXRange(double, double)
	 */
	public void setXRange(double min, double max) {
		synchronized( _Lock) {
			_logger.set_x_range( min, max);
		}
	}

	/* (Non Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#setXRange(java.lang.Double, java.lang.Double)
	 */
	public void setXRange(Double min, Double max) {
		setXRange( min.doubleValue(), max.doubleValue());
	}

	/* (Non Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#setYRange(double, double)
	 */
	public void setYRange(double min, double max) {
		synchronized( _Lock) {
			_logger.set_y_range( min, max);
		}
	}

	/* (Non Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#setYRange(java.lang.Double, java.lang.Double)
	 */
	public void setYRange(Double min, Double max) {
		setYRange( min.doubleValue(), max.doubleValue());
	}

	/* (Non Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#write(java.lang.String)
	 */
	public boolean write(String filename) throws IOException {
		synchronized( _Lock) {
			if ( !_logger.write_data( filename))
				return false;

			return true;

//			File file = new File( filename);
//
//			FileWriter fileWriter;
//			try {
//				fileWriter = new FileWriter( file);
//			} catch (IOException e) {
//				e.printStackTrace();
//				throw new Exception( getClass().getName() + "write()");
//			}
//
//			PrintWriter printWriter = new PrintWriter( fileWriter);
//
//			_plot.writeFormat( printWriter);
//			_plot.writeData( printWriter);
//
//			printWriter.close();
//
//			return true;


//			try {
//				_plot.writeOldSyntax( new FileOutputStream( file));
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//				throw new Exception( getClass().getName() + "write()");
//			}
		}
	}

	/* (Non Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		_logger.setTitle( title);
	}
}
