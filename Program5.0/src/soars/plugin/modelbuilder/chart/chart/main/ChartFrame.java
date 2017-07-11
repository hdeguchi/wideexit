/*
 * 2005/01/13
 */
package soars.plugin.modelbuilder.chart.chart.main;

import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import ptolemy.plot.PlotPoint;
import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.window.Frame;
import soars.common.utility.tool.resource.Resource;
import soars.plugin.modelbuilder.chart.chart.menu.file.IFileMenuHandler;
import soars.plugin.modelbuilder.chart.chart.menu.file.SaveAsAction;
import soars.plugin.modelbuilder.chart.chart.menu.file.SaveImageAsAction;

/**
 * @author kurata
 */
public class ChartFrame extends Frame implements IChart, IFileMenuHandler {

	/**
	 * 
	 */
	public String _name = "";

	/**
	 * 
	 */
	private LiveViewer _liveViewer = null;

	/**
	 * 
	 */
	private UserInterface _userInterface = null;

	/**
	 * 
	 */
	private JMenuItem _file_save_as_menuItem = null;

	/**
	 * 
	 */
	private JMenuItem _file_save_image_as_menuItem = null;

	/**
	 * 
	 */
	private Frame _parent = null;

	/**
	 * 
	 */
	private int _default_close_operation = DISPOSE_ON_CLOSE;

	/**
	 * @throws java.awt.HeadlessException
	 */
	public ChartFrame() throws HeadlessException {
		super();
	}

	/**
	 * @param parent
	 * @throws java.awt.HeadlessException
	 */
	public ChartFrame(Frame parent, int default_close_operation) throws HeadlessException {
		super();
		_parent = parent;
		_default_close_operation = default_close_operation;
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#create(java.lang.String)
	 */
	public boolean create(String name) {
		_name = name;

		if ( null != _liveViewer)
			return false;

		if ( !super.create())
			return false;

		return true;
	}

	/**
	 * 
	 */
	private void setup_menu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "file.menu"),
			true,
			ResourceManager.get_instance().get( "file.mnemonic"));

		_file_save_as_menuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.save.as.menu"),
			new SaveAsAction( ResourceManager.get_instance().get( "file.save.as.menu"), this),
			ResourceManager.get_instance().get( "file.save.as.mnemonic"),
			ResourceManager.get_instance().get( "file.save.as.stroke"));

		_file_save_image_as_menuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.save.image.as.menu"),
			new SaveImageAsAction( ResourceManager.get_instance().get( "file.save.image.as.menu"), this),
			ResourceManager.get_instance().get( "file.save.image.as.mnemonic"),
			ResourceManager.get_instance().get( "file.save.image.as.stroke"));

		setJMenuBar( menuBar);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		setDefaultCloseOperation( _default_close_operation);

		_userInterface = new UserInterface();

		_liveViewer = new LiveViewer();
		if ( !_liveViewer.setup())
			return false;

		getContentPane().add( "Center", _liveViewer);
		//_liveViewer.setButtons( true);
		_liveViewer.setButtons( false);


		setup_menu();


		Image image = Resource.load_image_from_resource(
			Constant._resource_directory + "/image/icon/icon.png", getClass());
		if ( null != image)
			setIconImage( image);


		pack();


		setLocationRelativeTo( _parent);


		setVisible( true);


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
		if ( null == _liveViewer)
			return;

		if ( !connected && !_liveViewer.getMarksStyle().equals( "dots"))
			_liveViewer.setMarksStyle( "dots", dataset);

		_liveViewer.addPoint( dataset, x, y, connected);
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
		if ( null == _liveViewer)
			return;

		_liveViewer.addLegend( dataset, label);
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
		if ( null == _liveViewer)
			return;

		_liveViewer.setXLabel( label);
	}

	/* (Non Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#setYLabel(java.lang.String)
	 */
	public void setYLabel(String label) {
		if ( null == _liveViewer)
			return;

		_liveViewer.setYLabel( label);
	}

	/* (Non Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#setXRange(double, double)
	 */
	public void setXRange(double min, double max) {
		if ( null == _liveViewer)
			return;

		_liveViewer.set_x_range( min, max);
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
		if ( null == _liveViewer)
			return;

		_liveViewer.set_y_range( min, max);
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
		if ( null == _liveViewer)
			return false;

		_liveViewer.write_data( filename);
		return true;
	}

	/* (Non Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		super.setTitle(title);

		if ( null == _liveViewer)
			return;

		_liveViewer.setTitle( title);
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.menu.file.IFileMenuHandler#on_save_as(java.awt.event.ActionEvent)
	 */
	public void on_save_as(ActionEvent actionEvent) {
		if ( null == _liveViewer)
			return;

		_liveViewer.on_save_as( this, this);
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.menu.file.IFileMenuHandler#on_save_image_as(java.awt.event.ActionEvent)
	 */
	public void on_save_image_as(ActionEvent actionEvent) {
		if ( null == _liveViewer)
			return;

		_liveViewer.on_save_image_as( this, this);
	}

	/**
	 * @return
	 */
	public Vector get_points() {
		return _liveViewer.get_points();
	}

	/**
	 * @param dataset
	 * @param indication_dataset
	 * @param ratio
	 */
	public void indicate(int dataset, int indication_dataset, double ratio) {
		Vector plotPoints = ( Vector)get_points().get( dataset);
		int offset = ( int)( ( double)plotPoints.size() * ratio);
		if ( 0 > offset)
			offset = 0;

		if ( plotPoints.size() <= offset)
			offset = plotPoints.size() - 1;

		PlotPoint plotPoint = ( PlotPoint)plotPoints.get( offset);
		_liveViewer.setMarksStyle( "dots", indication_dataset);
		_liveViewer.addPoint( indication_dataset, plotPoint.x, plotPoint.y, false);
	}

	/**
	 * @param dataset
	 */
	public void clear(int dataset) {
		_liveViewer.clear( dataset);
	}

	/**
	 * @param dataset
	 */
	public void removeLegend(int dataset) {
		_liveViewer.removeLegend( dataset);
	}

	/**
	 * 
	 */
	public void clearLegends() {
		_liveViewer.clearLegends();
	}
}
