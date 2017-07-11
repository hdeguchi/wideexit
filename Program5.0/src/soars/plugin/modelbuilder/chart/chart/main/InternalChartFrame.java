/**
 * 
 */
package soars.plugin.modelbuilder.chart.chart.main;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.InternalFrameEvent;

import org.xml.sax.SAXException;

import ptolemy.plot.PlotPoint;

import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.window.MDIChildFrame;
import soars.common.utility.tool.resource.Resource;
import soars.common.utility.xml.sax.Writer;
import soars.plugin.modelbuilder.chart.chart.menu.file.IFileMenuHandler;
import soars.plugin.modelbuilder.chart.chart.menu.file.SaveAsAction;
import soars.plugin.modelbuilder.chart.chart.menu.file.SaveImageAsAction;

/**
 * @author kurata
 *
 */
public class InternalChartFrame extends MDIChildFrame implements IChart, IFileMenuHandler {

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
	public InternalChartFrame() {
		super();
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

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		//setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		setResizable( true);
		setMaximizable( true);
		setIconifiable( true);

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
			setFrameIcon( new ImageIcon( image));


		pack();


		setVisible( true);


		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_internal_frame_closing(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_closing(InternalFrameEvent internalFrameEvent) {
		super.on_internal_frame_closing(internalFrameEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_internal_frame_activated(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_activated(InternalFrameEvent internalFrameEvent) {
		super.on_internal_frame_activated(internalFrameEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_internal_frame_deactivated(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_deactivated(InternalFrameEvent internalFrameEvent) {
		super.on_internal_frame_deactivated(internalFrameEvent);
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#append(int, double, double)
	 */
	public void append(int dataset, double x, double y) {
		append( dataset, x, y, true);
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#append(int, double, double, boolean)
	 */
	public void append(int dataset, double x, double y, boolean connected) {
		if ( null == _liveViewer)
			return;

		if ( !connected && !_liveViewer.getMarksStyle().equals( "dots"))
			_liveViewer.setMarksStyle( "dots", dataset);

		_liveViewer.addPoint( dataset, x, y, connected);
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#append(java.lang.Integer, java.lang.Double, java.lang.Double)
	 */
	public void append(Integer dataset, Double x, Double y) {
		append( dataset.intValue(), x.doubleValue(), y.doubleValue(), true);
	}

	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#setXLabel(java.lang.String)
	 */
	public void setXLabel(String label) {
		if ( null == _liveViewer)
			return;

		_liveViewer.setXLabel( label);
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#setYLabel(java.lang.String)
	 */
	public void setYLabel(String label) {
		if ( null == _liveViewer)
			return;

		_liveViewer.setYLabel( label);
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#setXRange(double, double)
	 */
	public void setXRange(double min, double max) {
		if ( null == _liveViewer)
			return;

		_liveViewer.set_x_range( min, max);
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#setXRange(java.lang.Double, java.lang.Double)
	 */
	public void setXRange(Double min, Double max) {
		setXRange( min.doubleValue(), max.doubleValue());
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#setYRange(double, double)
	 */
	public void setYRange(double min, double max) {
		if ( null == _liveViewer)
			return;

		_liveViewer.set_y_range( min, max);
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.IChart#setYRange(java.lang.Double, java.lang.Double)
	 */
	public void setYRange(Double min, Double max) {
		setYRange( min.doubleValue(), max.doubleValue());
	}

	/* (non-Javadoc)
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

		_liveViewer.on_save_as( this, null);
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.menu.file.IFileMenuHandler#on_save_image_as(java.awt.event.ActionEvent)
	 */
	public void on_save_image_as(ActionEvent actionEvent) {
		if ( null == _liveViewer)
			return;

		_liveViewer.on_save_image_as( this, null);
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(Writer writer) throws SAXException {
		if ( null == _liveViewer)
			return false;

		return _liveViewer.write( writer, _name, getBounds());
	}

	/**
	 * @param dataset
	 * @param file
	 * @param connect
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(int dataset, File file, boolean connect, Writer writer) throws SAXException {
		if ( null == _liveViewer)
			return false;

		return _liveViewer.write( dataset, file, connect, writer, _name, getBounds());
	}

	/**
	 * @param dataset
	 * @param file
	 * @return
	 */
	public boolean write(int dataset, File file) {
		if ( null == _liveViewer)
			return false;

		return _liveViewer.write( dataset, file);
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
