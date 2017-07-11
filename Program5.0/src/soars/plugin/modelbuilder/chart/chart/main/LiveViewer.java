/*
 * 2004/11/08
 */
package soars.plugin.modelbuilder.chart.chart.main;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.xml.sax.SAXException;

import ptolemy.plot.PlotLive;
import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.xml.sax.Writer;
import soars.plugin.modelbuilder.chart.chart.common.tool.CommonTool;

/**
 * @author kurata
 */
public class LiveViewer extends PlotLive implements IMessageCallback {

	/**
	 * 
	 */
	private double _min_x;

	/**
	 * 
	 */
	private double _max_x;

	/**
	 * 
	 */
	private double _min_y;

	/**
	 * 
	 */
	private double _max_y;

	/**
	 * 
	 */
	private boolean _first = true; 

	/**
	 * 
	 */
	private double _ratio = 0.2;

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
	private Object _Lock = new Object();

	/**
	 * 
	 */
	public LiveViewer() {
		super();
	}

	/* (Non Javadoc)
	 * @see ptolemy.plot.PlotLive#addPoints()
	 */
	public void addPoints() {
	}

	/* (Non Javadoc)
	 * @see ptolemy.plot.Plot#addPoint(int, double, double, boolean)
	 */
	public void addPoint(
		int dataset,
		double x,
		double y,
		boolean connected) {

		synchronized( _Lock) {

			boolean repaint = false;

			if ( _first) {
				if ( !_XRange) {
					_min_x = _max_x = x;
					setXRange( _min_x, _max_x);
				}
				if ( !_YRange) {
					_min_y = _max_y = y;
					setYRange( _min_y, _max_y);
				}
				repaint = true;
				_first = false;
			} else {
				if ( !_XRange) {
					double x_range[] = getXRange();
					if ( x < x_range[ 0]) {
						double play = ( _max_x - x) * _ratio;
						setXRange( x - play, _max_x + play);
						_min_x = x;
						repaint = true;
					} else if ( x > x_range[ 1]) {
						double play = ( x - _min_x) * _ratio;
						setXRange( _min_x - play, x + play);
						_max_x = x;
						repaint = true;
					}
				}

				if ( !_YRange) {
					double y_range[] = getYRange();
					if ( y < y_range[ 0]) {
						double play = ( _max_y - y) * _ratio;
						setYRange( y - play, _max_y + play);
						_min_y = y;
						repaint = true;
					} else if ( y > y_range[ 1]) {
						double play = ( y - _min_y) * _ratio;
						setYRange( _min_y - play, y + play);
						_max_y = y;
						repaint = true;
					}
				}
			}

			if ( repaint)
				repaint();

			super.addPoint(dataset, x, y, connected);
		}
	}

	/**
	 * @return
	 */
	public boolean setup() {
		removeMouseListener();
		removeMouseMotionListener();
		removeMouseWheelListener();
		return true;
	}

	/**
	 * 
	 */
	private void removeMouseListener() {
		MouseListener[] mouseListeners = getMouseListeners();
		for ( int i = 0; i < mouseListeners.length; ++i)
			removeMouseListener( mouseListeners[ i]);
	}

	/**
	 * 
	 */
	private void removeMouseMotionListener() {
		MouseMotionListener[] mouseMotionListeners = getMouseMotionListeners();
		for ( int i = 0; i < mouseMotionListeners.length; ++i)
			removeMouseMotionListener( mouseMotionListeners[ i]);
	}

	/**
	 * 
	 */
	private void removeMouseWheelListener() {
		MouseWheelListener[] mouseWheelListeners = getMouseWheelListeners();
		for ( int i = 0; i < mouseWheelListeners.length; ++i)
			removeMouseWheelListener( mouseWheelListeners[ i]);
	}

	/**
	 * @param min
	 * @param max
	 */
	public void set_x_range(double min, double max) {
		synchronized( _Lock) {
			_XRange = true;
			setXRange(min, max);
		}
	}

	/**
	 * @param min
	 * @param max
	 */
	public synchronized void set_y_range(double min, double max) {
		synchronized( _Lock) {
			_YRange = true;
			setYRange(min, max);
		}
	}

	/**
	 * @param filename
	 * @return
	 */
	public boolean write_data(String filename) throws IOException {
		synchronized( _Lock) {
			LogWriter logWriter = new LogWriter();
			return logWriter.write_data( this, true, true, _points, filename);
		}
	}

	/**
	 * @param parent
	 * @param owner
	 * @return
	 */
	public boolean on_save_as(Component parent, Frame owner) {
		File file = CommonTool.get_save_file(
			Environment._save_as_directory_key,
			ResourceManager.get_instance().get( "file.save.as.dialog"),
			new String[] { "pml"},
			"soars chart data",
			parent);

		if ( null == file)
			return false;

		String absolute_name = file.getAbsolutePath();
		String name = file.getName();
		int index = name.lastIndexOf( '.');
		if ( -1 == index)
			file = new File( absolute_name + ".pml");
		else if ( name.length() - 1 == index)
			file = new File( absolute_name + "pml");

		if ( !MessageDlg.execute( owner, getTitle(), true,
			"on_file_save_as", ResourceManager.get_instance().get( "file.save.as.show.message"),
			new Object[] { file.getAbsolutePath()}, this, parent)) {
			JOptionPane.showMessageDialog( owner,
				ResourceManager.get_instance().get( "file.save.as.error.message"),
				ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
			return false;
		}

		Environment.get_instance().store();

		return true;
	}

	/**
	 * @param parent
	 * @param owner
	 * @return
	 */
	public boolean on_save_image_as(Component parent, Frame owner) {
		String[] formatNames = ImageIO.getWriterFormatNames();
		if ( null == formatNames || 0 == formatNames.length)
			return false;

		Arrays.sort( formatNames);
		if ( 0 > Arrays.binarySearch( formatNames, "png"))
			return false;

		BufferedImage bufferedImage = exportImage();
		if ( null == bufferedImage)
			return false;

		File file = CommonTool.get_save_file(
			Environment._save_image_as_directory_key,
			ResourceManager.get_instance().get( "file.save.image.as.dialog"),
			new String[] { "png"},
			"soars chart image data",
			parent);

		if ( null == file)
			return false;

		String absolute_name = file.getAbsolutePath();
		String name = file.getName();
		int index = name.lastIndexOf( '.');
		if ( -1 == index)
			file = new File( absolute_name + ".png");
		else if ( name.length() - 1 == index)
			file = new File( absolute_name + "png");

		if ( !MessageDlg.execute( owner, getTitle(), true,
			"on_file_save_image_as", ResourceManager.get_instance().get( "file.save.image.as.show.message"),
			new Object[] { bufferedImage, "png", file}, this, parent)) {
			JOptionPane.showMessageDialog( owner,
				ResourceManager.get_instance().get( "file.save.image.as.error.message"),
				ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
			return false;
		}

		Environment.get_instance().store();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IMessageCallback#message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.MessageDlg)
	 */
	public boolean message_callback(String id, Object[] objects, MessageDlg messageDlg) {
		if ( id.equals( "on_file_save_as")) {
			try {
				return write_data( ( String)objects[ 0]);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else if ( id.equals( "on_file_save_image_as")) {
			try {
				return ImageIO.write( ( BufferedImage)objects[ 0],
					( String)objects[ 1],
					( File)objects[ 2]);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	/**
	 * @param writer
	 * @param name
	 * @param rectangle
	 * @return
	 * @throws SAXException
	 */
	public boolean write(Writer writer, String name, Rectangle rectangle) throws SAXException {
		synchronized( _Lock) {
			LogWriter logWriter = new LogWriter();
			return logWriter.write( writer, this, true, true, name, _points, rectangle);
		}
	}

	/**
	 * @param dataset
	 * @param file
	 * @param connect
	 * @param writer
	 * @param name
	 * @param rectangle
	 * @return
	 * @throws SAXException
	 */
	public boolean write(int dataset, File file, boolean connect, Writer writer, String name, Rectangle rectangle) throws SAXException {
		synchronized( _Lock) {
			LogWriter logWriter = new LogWriter();
			return logWriter.write( dataset, file, connect, writer, this, true, true, name, _points, rectangle);
		}
	}

	/**
	 * @param dataset
	 * @param file
	 * @return
	 */
	public boolean write(int dataset, File file) {
		synchronized( _Lock) {
			LogWriter logWriter = new LogWriter();
			return logWriter.write_data( this, _points, dataset, file);
		}
	}

	/**
	 * @return
	 */
	public Vector get_points() {
		return _points;
	}
}
