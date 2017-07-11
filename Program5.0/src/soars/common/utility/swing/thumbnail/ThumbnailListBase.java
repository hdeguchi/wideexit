/**
 * 
 */
package soars.common.utility.swing.thumbnail;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.SystemColor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;

import soars.common.utility.swing.image.ImageProperty;
import soars.common.utility.swing.list.StandardList;
import soars.common.utility.swing.progress.IObjectsProgressCallback;
import soars.common.utility.swing.progress.IProgressCallback;
import soars.common.utility.swing.progress.ObjectsProgressDlg;
import soars.common.utility.swing.progress.ProgressDlg;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.resource.Resource;

/**
 * The common thumbnail list class.
 * @author kurata / SOARS project
 */
public class ThumbnailListBase extends StandardList implements DropTargetListener, IProgressCallback, IObjectsProgressCallback {

	/**
	 * Default maximum number of thumbnails on the thumbnail list.
	 */
	static public final int _defaultMax = 30;

	/**
	 * Default size of ths thumbnail's maximum side.
	 */
	static public final int _defaultSide = 100;

	/**
	 * Maximum number of thumbnails on the thumbnail list.
	 */
	public int _max = _defaultMax;

	/**
	 * Size of ths thumbnail's maximum side.
	 */
	public int _side = _defaultSide;

	/**
	 * The number of current page.
	 */
	public int _page = 0;

	/**
	 * Image directory.
	 */
	protected File _imageDirectory = null;

	/**
	 * Thumbnail directory.
	 */
	protected File _thumbnailImageDirectory = null;

	/**
	 * Image hashtable(String[absolute path] - BufferedImage).
	 */
	protected TreeMap<String, ImageProperty> _imagePropertyManager = null;

	/**
	 * Title string for the dialog box.
	 */
	protected String _title = "";

	/**
	 * Message strings for the dialog box.
	 */
	protected String[] _messages = null;

	/**
	 * Option strings of overwrite for the dialog box.
	 */
	protected String[] _overwriteOptions = null;

	/**
	 * Cancel string for the dialog box.
	 */
	protected String _cancel = "";

	/**
	 * Start position of the rubber band.
	 */
	private Point _rubberbandStartPoint = null;

	/**
	 * End position of the rubber band.
	 */
	private Point _rubberbandEndPoint = new Point();

	/**
	 * Current rubber band rectangle.
	 */
	private Rectangle _currentRectangle = new Rectangle();

	/**
	 * Previous rubber band rectangle.
	 */
	private Rectangle _previousRectangle = new Rectangle();

	/**
	 * Visible rectangle of this component.
	 */
	private Rectangle _clientRectangle = new Rectangle();

	/**
	 * Object which makes the rubber band visible.
	 */
	private Polygon _polygon = new Polygon();

	/**
	 * Border line for the rubber band.
	 */
	private Line2D _line = new Line2D.Double();

	/**
	 * Color of the border line for the rubber band.
	 */
	private final Color _rcolor = SystemColor.activeCaption;

	/**
	 * Color of the rubber band.
	 */
	private final Color _pcolor = make_color( _rcolor);

	/**
	 * Alpha compositing rules for the rubber band.
	 */
	private final AlphaComposite _alphaComposite = AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.1f);

	/**
	 * Returns the color of the rubber band.
	 * @param color the color of the border line for the rubber band
	 * @return the color of the rubber band
	 */
	static private Color make_color(Color color) {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		return ( r > g )
			? ( r > b) ? new Color( r, 0, 0) : new Color( 0, 0, b)
			: ( g > b) ? new Color( 0, g, 0) : new Color( 0, 0, b);
	}

	/**
	 * Creates the common thumbnail list.
	 * @param imageDirectory the image directory
	 * @param thumbnailImageDirectory the thumbnail directory
	 * @param imagePropertyManager the image hashtable(String[absolute path] - BufferedImage)
	 * @param title the title string for the dialog box
	 * @param messages the message strings for the dialog box
	 * @param overwriteOptions the option strings of overwrite for the dialog box
	 * @param cancel the cancel string for the dialog box
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public ThumbnailListBase(File imageDirectory, File thumbnailImageDirectory, TreeMap<String, ImageProperty> imagePropertyManager, String title, String[] messages, String[] overwriteOptions, String cancel, Frame owner, Component parent) {
		super(owner, parent);
		_imageDirectory = imageDirectory;
		_thumbnailImageDirectory = thumbnailImageDirectory;
		_imagePropertyManager = imagePropertyManager;
		_title = title;
		_messages = messages;
		_overwriteOptions = overwriteOptions;
		_cancel = cancel;
	}

	/**
	 * Returns true if this component is initialized successfully.
	 * @param filename the image filename to be selected
	 * @param popupMenu true if the context menu is used
	 * @return true if this component is initialized successfully
	 */
	public boolean setup(String filename, boolean popupMenu) {
		if ( !super.setup( popupMenu))
			return false;

		new DropTarget( this, this);

		setLayoutOrientation( JList.HORIZONTAL_WRAP);

		setFixedCellWidth( _side + 10);

		setCellRenderer( new ThumbnailListCellRenderer());

		if ( !load( filename))
			return false;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.list.StandardList#on_mouse_left_down(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_down(MouseEvent mouseEvent) {
		_rubberbandStartPoint = mouseEvent.getPoint();
		_rubberbandEndPoint.setLocation( mouseEvent.getPoint());
		_previousRectangle.setBounds( mouseEvent.getPoint().x, mouseEvent.getPoint().y, 0, 0);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.list.StandardList#on_mouse_left_double_click(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_double_click(MouseEvent mouseEvent) {
		_rubberbandStartPoint = null;
		_polygon.reset();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.list.StandardList#on_mouse_left_up(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_up(MouseEvent mouseEvent) {
		_rubberbandStartPoint = null;
		_polygon.reset();
		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.list.StandardList#on_mouse_dragged(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_dragged(MouseEvent mouseEvent) {
		if ( null == _rubberbandStartPoint)
			return;

		_rubberbandEndPoint.setLocation( mouseEvent.getPoint());

		if ( 0 > _rubberbandEndPoint.x)
			_rubberbandEndPoint.x = 0;
		if ( 0 > _rubberbandEndPoint.y)
			_rubberbandEndPoint.y = 0;

		_currentRectangle.setBounds(
			Math.min( _rubberbandStartPoint.x, _rubberbandEndPoint.x),
			Math.min( _rubberbandStartPoint.y, _rubberbandEndPoint.y),
			Math.abs( _rubberbandStartPoint.x - _rubberbandEndPoint.x + 1),
			Math.abs( _rubberbandStartPoint.y - _rubberbandEndPoint.y + 1));

		if ( getVisibleRect().contains( _currentRectangle))
			scrollRectToVisible( _currentRectangle);
		else {
			_clientRectangle.setBounds( getVisibleRect());

			if (  _currentRectangle.x < getVisibleRect().x
				|| _currentRectangle.x + _currentRectangle.width > getVisibleRect().x + getVisibleRect().width) {
				if ( _currentRectangle.x == _previousRectangle.x) {
					if ( 0 != _currentRectangle.x)
						_clientRectangle.x = _currentRectangle.x + _currentRectangle.width - getVisibleRect().width;
				} else
					_clientRectangle.x = _currentRectangle.x;
			}

			if ( _currentRectangle.y < getVisibleRect().y
				|| _currentRectangle.y + _currentRectangle.height > getVisibleRect().y + getVisibleRect().height) {
				if ( _currentRectangle.y == _previousRectangle.y) {
					if ( 0 != _currentRectangle.y)
						_clientRectangle.y = _currentRectangle.y + _currentRectangle.height - getVisibleRect().height;
				} else
					_clientRectangle.y = _currentRectangle.y;
			}

			scrollRectToVisible( _clientRectangle);
		}

		_previousRectangle.setBounds( _currentRectangle);

		_polygon.reset();
		_polygon.addPoint( _rubberbandStartPoint.x, _rubberbandStartPoint.y);
		_polygon.addPoint( _rubberbandEndPoint.x, _rubberbandStartPoint.y);
		_polygon.addPoint( _rubberbandEndPoint.x, _rubberbandEndPoint.y);
		_polygon.addPoint( _rubberbandStartPoint.x, _rubberbandEndPoint.y);
		if ( _rubberbandStartPoint.x == _rubberbandEndPoint.x
			|| _rubberbandStartPoint.y == _rubberbandEndPoint.y) {
			_line.setLine( _rubberbandStartPoint.x, _rubberbandStartPoint.y, _rubberbandEndPoint.x, _rubberbandEndPoint.y);
			setSelectedIndices( get_intersects_indices( _line));
		} else {
			setSelectedIndices( get_intersects_indices( _polygon));
		}

		repaint();
	}

	/**
	 * Returns the indices of selected thumbnails.
	 * @param shape the object which makes the rubber band visible
	 * @return the indices of selected thumbnails
	 */
	private int[] get_intersects_indices(Shape shape) {
		List<Integer> list = new ArrayList<Integer>();

		for ( int i = 0; i < _defaultComboBoxModel.getSize(); ++i) {
			Rectangle rectangle = getCellBounds( i, i);
			if ( shape.intersects( rectangle)) {
				list.add( i);
			}
		}

		int[] indices = new int[ list.size()];
		for ( int i = 0; i < list.size(); ++i)
			indices[ i] = list.get( i);

		return indices;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(Graphics graphics) {
		super.paint(graphics);

		if ( null == _rubberbandStartPoint
			|| _polygon.getBounds().isEmpty())
			return;

		Graphics2D graphics2D = ( Graphics2D)graphics;
		graphics2D.setPaint( _rcolor);
		graphics2D.drawPolygon( _polygon);
		graphics2D.setComposite( _alphaComposite);
		graphics2D.setPaint( _pcolor);
		graphics2D.fillPolygon( _polygon);
	}

	/**
	 * Returns the number of the maximum page.
	 * @return
	 */
	public int get_max_page() {
		return ( ( ( _imagePropertyManager.size() - 1) / _max) + 1);
	}

	/**
	 * Returns true if we can go to the previous page.
	 * @return true if we can go to the previous page
	 */
	public boolean can_move_to_previous_page() {
		return ( 0 < _page);
	}

	/**
	 * Returns true if we can go to the next page.
	 * @return true if we can go to the next page
	 */
	public boolean can_move_to_next_page() {
		return ( _imagePropertyManager.size() > _max * ( _page + 1));
	}

	/**
	 * Goes to the previous page.
	 */
	public void move_to_previous_page() {
		if ( !can_move_to_previous_page())
			return;

		--_page;
		setup_current_page();
	}

	/**
	 * Goes to the next page.
	 */
	public void move_to_next_page() {
		if ( !can_move_to_next_page())
			return;

		++_page;
		setup_current_page();
	}

	/**
	 * Returns true if we could make thumbnail list and select the specified thumbnail.
	 * @param filename the image filename to be selected
	 * @return true if we could make thumbnail list and select the specified thumbnail
	 */
	protected boolean load(String filename) {
		if ( _imagePropertyManager.isEmpty())
			return true;

		String[] filenames = _imagePropertyManager.keySet().toArray( new String[ 0]);
		int index = select_page( filenames, filename);

		List<File> list = new ArrayList<File>();
		for ( int i = 0, j = _max * _page; i < _max && j < filenames.length; ++i, ++j)
			list.add( new File( _thumbnailImageDirectory, filenames[ j]));

		Data[] data = ( Data[])ObjectsProgressDlg.execute( _owner, _title + " - " + _messages[ 0], true, "load", ( File[])list.toArray( new File[ 0]), ( IObjectsProgressCallback)this, _owner);
		if ( null == data)
			return false;

		if ( 0 == data.length)
			return true;

		setup_current_page();

		if ( null != filename && !filename.equals( "") && 0 <= index && _defaultComboBoxModel.getSize() > index) {
			setSelectedIndex( index);
			on_select();
		} else
			setSelectedIndex( 0);

		return true;
	}

	/**
	 * Returns the number of page to show the specified thumbnail.
	 * @param filename the image filename of the specified thumbnail
	 * @return the number of page to show the specified thumbnail
	 */
	private int select_page(String filename) {
		return select_page( _imagePropertyManager.keySet().toArray( new String[ 0]), filename);
	}

	/**
	 * Returns the number of page to show the specified thumbnail.
	 * @param filenames all image filenames
	 * @param filename the image filename of the specified thumbnail
	 * @return the number of page to show the specified thumbnail
	 */
	private int select_page(String[] filenames, String filename) {
		int index = -1;
		if ( null == filename || filename.equals( ""))
			_page = 0;
		else {
			index = Arrays.binarySearch( filenames, filename);
			if ( 0 > index)
				_page = 0;
			else {
				_page = ( index / _max);
				index -= ( _max * _page);
			}
		}
		return index;
	}

	/**
	 * Invoked when a thumbnail is selected.
	 */
	protected void on_select() {
	}

	/**
	 * Returns true if all specified image files are appended successfully.
	 * @param files the specified image files
	 * @return true if all specified image files are appended successfully
	 */
	protected boolean append(File[] files) {
		List<File> list = new ArrayList<File>();
		List<String> overwrites = new ArrayList<String>();
		for ( int i = 0; i < files.length; ++i) {
			if ( !files[ i].isFile() || !files[ i].canRead())
				continue;

			File file = new File( _imageDirectory, files[ i].getName());
			if ( file.exists()) {
				// 上書きしないものは省く
				// "Yes", "No", "Yes to all", "No to all", "Cancel"
				int result = JOptionPane.showOptionDialog( _parent,
					files[ i].getName() + " - " + _messages[ 2],
					_title, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, _overwriteOptions, _overwriteOptions[ 0]);
				if ( 1 == result)	// No
					continue;
				else if ( 4 == result || JOptionPane.CLOSED_OPTION == result)	// Cancel or Close
					return false;
				else if ( 2 == result) {	// Yes to all
					list.clear();
					overwrites.clear();
					on_yes_to_all( files, list, overwrites);
					break;
				} else if ( 3 == result) {	// No to all
					list.clear();
					overwrites.clear();
					on_no_to_all( files, list);
					break;
				}

				overwrites.add( files[ i].getName());
			}

			list.add( files[ i]);
		}

		if ( list.isEmpty())
			return false;

		Data[] data = ( Data[])ObjectsProgressDlg.execute( _owner, _title + " - " + _messages[ 0], true, "make", _cancel, ( File[])list.toArray( new File[ 0]), ( IObjectsProgressCallback)this, _parent);
		if ( null == data)
			return false;

		if ( 0 == data.length)
			return true;

		if ( !ProgressDlg.execute( _owner, _title + " - " + _messages[ 1], true, "make", data, ( IProgressCallback)this, _parent))
			return false;

		for ( int i = 0; i < data.length; ++i)
			_imagePropertyManager.put( data[ i]._file.getName(), new ImageProperty( data[ i]._width, data[ i]._height));

		setup_current_page();

		changed( overwrites.toArray( new String[ 0]));

		return true;
	}

	/**
	 * Appends all specified image files, however if a file already exists, it will be overwritten with new one.
	 * @param files the specified image files
	 * @param list all image files which are appended
	 * @param overwrites all image files which are overwritten with new ones
	 */
	private void on_yes_to_all(File[] files, List<File> list, List<String> overwrites) {
		for ( int i = 0; i < files.length; ++i) {
			if ( !files[ i].isFile() || !files[ i].canRead())
				continue;

			File file = new File( _imageDirectory, files[ i].getName());
			if ( file.exists())
				overwrites.add( files[ i].getName());

			list.add( files[ i]);
		}
	}

	/**
	 * Appends all specified image files, however if a file already exists, it will not be overwritten with new one.
	 * @param files the specified image files
	 * @param list all image files which are appended
	 */
	private void on_no_to_all(File[] files, List<File> list) {
		for ( int i = 0; i < files.length; ++i) {
			if ( !files[ i].isFile() || !files[ i].canRead())
				continue;

			File file = new File( _imageDirectory, files[ i].getName());
			if ( file.exists())
				continue;

			list.add( files[ i]);
		}
	}

//	/**
//	 * Returns true if all specified image files are appended successfully.
//	 * @param files the specified image files
//	 * @return true if all specified image files are appended successfully
//	 */
//	protected boolean appends(File[] files) {
//		boolean yesToAll = false;
//		boolean noToAll = false;
//		List<File> list = new ArrayList<File>();
//		List<String> overwrites = new ArrayList<String>();
//		for ( int i = 0; i < files.length; ++i) {
//			if ( !files[ i].isFile() || !files[ i].canRead())
//				continue;
//
//			File file = new File( _imageDirectory, files[ i].getName());
//			if ( file.exists()) {
//				// 上書きしないものは省く
//				if ( noToAll)
//					continue;
//
//				if ( !yesToAll) {
//					// "Yes", "No", "Yes to all", "No to all", "Cancel"
//					int result = JOptionPane.showOptionDialog( _parent,
//						files[ i].getName() + " - " + _messages[ 2],
//						_title, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, _overwriteOptions, _overwriteOptions[ 0]);
//					switch ( result) {
//						case 0:
//							break;
//						case 1:	// No
//							continue;
//						case 2:	// Yes to all
//							yesToAll = true;
//							break;
//						case 3:	// No to all
//							noToAll = true;
//							continue;
//						case 4:	// Cancel
//						case JOptionPane.CLOSED_OPTION:	// Close
//							return false;
//						default:
//							break;
//					}
//				}
//
//				overwrites.add( files[ i].getName());
//			}
//
//			list.add( files[ i]);
//		}
//
//		if ( list.isEmpty())
//			return false;
//
//		Data[] data = ( Data[])ObjectsProgressDlg.execute( _owner, _title + " - " + _messages[ 0], true, "make", _cancel, ( File[])list.toArray( new File[ 0]), ( IObjectsProgressCallback)this, _parent);
//		if ( null == data)
//			return false;
//
//		if ( 0 == data.length)
//			return true;
//
//		if ( !ProgressDlg.execute( _owner, _title + " - " + _messages[ 1], true, "make", data, ( IProgressCallback)this, _parent))
//			return false;
//
//		for ( int i = 0; i < data.length; ++i)
//			_imagePropertyManager.put( data[ i]._file.getName(), new ImageProperty( data[ i]._width, data[ i]._height));
//
//		setup_current_page();
//
//		changed( overwrites.toArray( new String[ 0]));
//
//		return true;
//	}

	/**
	 * Goes to the current page.
	 */
	private void setup_current_page() {
		_defaultComboBoxModel.removeAllElements();

		if ( _imagePropertyManager.isEmpty()) {
			_page = 0;
			return;
		}

		if ( _imagePropertyManager.size() <= _max * _page)
			_page = ( _imagePropertyManager.size() - 1) / _max;

		String[] filenames = _imagePropertyManager.keySet().toArray( new String[ 0]);
		for ( int i = 0, j = _max * _page; i < _max && j < filenames.length; ++i, ++j) {
			ImageProperty imageProperty = _imagePropertyManager.get( filenames[ j]);
			if ( null == imageProperty)
				continue;

			File file = new File( _thumbnailImageDirectory, filenames[ j]);
			if ( !file.isFile() || !file.canRead())
				continue;

			BufferedImage bufferedImage = Resource.load_image( file);
			if ( null == bufferedImage)
				continue;

			_defaultComboBoxModel.addElement( new ThumbnailItem( new ImageIcon( bufferedImage),
				imageProperty, new File( _imageDirectory, filenames[ j])));
		}
	}

	/**
	 * Invoked when new image files are appended
	 * @param filenames all names of image files which are overwritten with new ones
	 */
	protected void changed(String[] filenames) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.progress.IObjectsProgressCallback#objects_message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.progress.ObjectsProgressDlg)
	 */
	public Object[] objects_message_callback(String id, Object[] objects, ObjectsProgressDlg objectsProgressDlg) {
		if ( id.equals( "load"))
			return load( ( File[])objects, objectsProgressDlg);
		else if ( id.equals( "make"))
			return make( ( File[])objects, objectsProgressDlg);
		return null;
	}

	/**
	 * Returns thumbnail data array if we could load specified thumbnail files successfully.
	 * @param files the specified thumbnail files
	 * @param objectsProgressDlg the dialog box which can show the progress of the specified task
	 * @return thumbnail data array if we could load specified thumbnail files successfully
	 */
	private Data[] load(File[] files, ObjectsProgressDlg objectsProgressDlg) {
		List<Data> list = new ArrayList<Data>();

		int max = Math.min( files.length, _max);

		for ( int i = 0; i < max; ++i) {
			objectsProgressDlg.set( ( int)( 100.0 * ( double)( i + 1) / ( double)files.length));

			if ( !files[ i].isFile() || !files[ i].canRead())
				continue;

			BufferedImage bufferedImage = Resource.load_image( files[ i]);
			if ( null == bufferedImage)
				continue;

			File file = new File( _imageDirectory, files[ i].getName());

			ImageProperty imageProperty = _imagePropertyManager.get( file.getName());
			if ( null == imageProperty) {
				BufferedImage bi = Resource.load_image( file);
				if ( null == bi)
					continue;

				imageProperty = new ImageProperty( bi.getWidth(), bi.getHeight());
			}

			list.add( new Data( new ImageIcon( bufferedImage), imageProperty, file));
		}

		return ( ( Data[])list.toArray( new Data[ 0]));
	}

	/**
	 * Returns thumbnail data array if we could make thumbnail images from specified image files successfully.
	 * @param files the specified image files
	 * @param objectsProgressDlg the dialog box which can show the progress of the specified task
	 * @return thumbnail data array if we could make thumbnail images from specified image files successfully
	 */
	private Data[] make(File[] files, ObjectsProgressDlg objectsProgressDlg) {
		List<Data> list = new ArrayList<Data>();

		MediaTracker mediaTracker = new MediaTracker( this);

		for ( int i = 0; i < files.length; ++i) {
			if ( objectsProgressDlg._canceled)
				break;

			objectsProgressDlg.set( ( int)( 100.0 * ( double)( i + 1) / ( double)files.length));

			if ( !files[ i].isFile() || !files[ i].canRead())
				continue;

			BufferedImage bufferedImage = Resource.load_image( files[ i]);
			if ( null == bufferedImage)
				continue;

			if ( _side >= bufferedImage.getWidth() && _side >= bufferedImage.getHeight())
				list.add( new Data( new ImageIcon( bufferedImage), bufferedImage.getWidth(), bufferedImage.getHeight(), files[ i]));
			else {
				double ratio = ( ( double)_side / ( double)Math.max( bufferedImage.getWidth(), bufferedImage.getHeight()));
				Image image = bufferedImage.getScaledInstance( ( int)( ( double)bufferedImage.getWidth() * ratio),
					( int)( ( double)bufferedImage.getHeight() * ratio), Image.SCALE_SMOOTH);
				mediaTracker.addImage( image, i);
				list.add( new Data( new ImageIcon( image), bufferedImage.getWidth(), bufferedImage.getHeight(), files[ i]));
				try {
					mediaTracker.waitForID( i);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return null;
				}
			}
		}

//		try {
//			mediaTracker.waitForAll();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//			return null;
//		}

		if ( objectsProgressDlg._canceled)
			return null;

		return ( ( Data[])list.toArray( new Data[ 0]));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.progress.IProgressCallback#progress_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.progress.ProgressDlg)
	 */
	public boolean progress_callback(String id, Object[] objects, ProgressDlg progressDlg) {
		if ( id.equals( "make"))
			return make( ( Data[])objects, progressDlg);
		else if ( id.equals( "update"))
			return update( ( String[])objects, progressDlg);
		return false;
	}

	/**
	 * Returns true if the image hashtable is updated successfully.
	 * @param filenames all names of image files which are overwritten
	 * @param progressDlg the dialog box which can show the progress of the specified task
	 * @return true if the image hashtable is updated successfully
	 */
	protected boolean update(String[] filenames, ProgressDlg progressDlg) {
		return false;
	}

	/**
	 * Returns true if we could make thumbnail files from thumbnail data array successfully.
	 * @param data thumbnail data array
	 * @param objectsProgressDlg the dialog box which can show the progress of the specified task
	 * @return true if we could make thumbnail files from thumbnail data array successfully
	 */
	private boolean make(Data[] data, ProgressDlg progressDlg) {
		boolean result = true;
		boolean modified = false;
		for ( int i = 0; i < data.length; ++i) {
			progressDlg.set( ( int)( 100.0 * ( double)( i + 1) / ( double)data.length));

			File file = new File( _imageDirectory, data[ i]._file.getName());
			if ( !FileUtility.copy( data[ i]._file, file)) {
				result = false;
				break;
			}

			data[ i]._file = file;

			modified = true;

			if ( _side >= data[ i]._width && _side >= data[ i]._height) {
				if ( !FileUtility.copy( data[ i]._file, new File( _thumbnailImageDirectory, data[ i]._file.getName()))) {
					result = false;
					break;
				}
			} else {
				if ( !make_thumbnail_imagefile( data[ i])) {
					result = false;
					break;
				}
			}
		}

		if ( modified)
			modified();

		return result;
	}

	/**
	 * Returns true if we could make thumbnail file from specified thumbnail data successfully.
	 * @param data the specified thumbnail data
	 * @return true if we could make thumbnail file from specified thumbnail data successfully
	 */
	private boolean make_thumbnail_imagefile(Data data) {
		// BufferedImage.TYPE_4BYTE_ABGRで良いのか？
		BufferedImage bufferedImage = new BufferedImage( data._imageIcon.getIconWidth(), data._imageIcon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics2D = ( Graphics2D)bufferedImage.getGraphics();
		graphics2D.drawImage( data._imageIcon.getImage(), 0, 0, null);
		return Resource.write_image( bufferedImage, "png", new File( _thumbnailImageDirectory, data._file.getName()));
	}

	/**
	 * Invoked when image files are appended, renamed, overwritten or removed.
	 */
	protected void modified() {
	}

	/**
	 * Returns true if we could rename the image file of the specified thumbnail panel successfully.
	 * @param thumbnailItem the specified thumbnail panel
	 * @param newFilename the new image filename
	 * @return true if we could rename the image file of the specified thumbnail panel successfully
	 */
	protected boolean rename(ThumbnailItem thumbnailItem, String newFilename) {
		String originalFilename = thumbnailItem._file.getName();
		ImageProperty imageProperty = _imagePropertyManager.get( originalFilename);
		if ( null == imageProperty)
			return false;

		if ( !thumbnailItem._file.renameTo( new File( _imageDirectory, newFilename)))
			return false;

		File file = new File( _thumbnailImageDirectory, originalFilename);
		if ( !file.renameTo( new File( _thumbnailImageDirectory, newFilename)))
			return false;

		_imagePropertyManager.remove( originalFilename);
		_imagePropertyManager.put( newFilename, imageProperty);

		int index = select_page( newFilename);

		setup_current_page();

		if ( null != newFilename && !newFilename.equals( "") && 0 <= index && _defaultComboBoxModel.getSize() > index) {
			setSelectedIndex( index);
			on_select();
		}

		return true;
	}

	/**
	 * Returns true if we could remove the image files of the selected thumbnail panels successfully.
	 * @return true if we could remove the image files of the selected thumbnail panels successfully
	 */
	protected boolean remove() {
		int[] indices = getSelectedIndices();
		if ( 1 > indices.length)
			return false;

		if ( 1 < indices.length)
			Arrays.sort( indices);

		if ( !can_remove( indices))
			return false;

		if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
			_parent, _messages[ 3], _title, JOptionPane.YES_NO_OPTION))
			return false;

		boolean result = false;
		for ( int i = indices.length - 1; i >= 0; --i) {
			ThumbnailItem thumbnailItem = ( ThumbnailItem)_defaultComboBoxModel.getElementAt( indices[ i]);

			_imagePropertyManager.remove( thumbnailItem._file.getName());

			File file = new File( _thumbnailImageDirectory, thumbnailItem._file.getName());
			if ( file.exists())
				file.delete();

			if ( thumbnailItem._file.exists())
				thumbnailItem._file.delete();

			_defaultComboBoxModel.removeElementAt( indices[ i]);

			result = true;
		}

		if ( !result)
			return false;

		setup_current_page();

		if ( 0 < _defaultComboBoxModel.getSize()) {
			if ( indices[ 0] < _defaultComboBoxModel.getSize())
				setSelectedIndex( indices[ 0]);
			else
				setSelectedIndex( _defaultComboBoxModel.getSize() - 1);
		}

		changed();
		modified();
		return true;
	}

	/**
	 * Invoked when image files are removed
	 */
	protected void changed() {
	}

	/**
	 * Returns true if the selected thumbnails are not used.
	 * @param indices the indices of the selected thumbnails
	 * @return true if the selected thumbnails are not used
	 */
	protected boolean can_remove(int[] indices) {
		return false;
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent arg0) {
		arg0.acceptDrag( DnDConstants.ACTION_COPY);
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent arg0) {
		arg0.acceptDrag( DnDConstants.ACTION_COPY_OR_MOVE);
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent arg0) {
		arg0.acceptDrag( DnDConstants.ACTION_COPY);
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent arg0) {
		try {
			Transferable transferable = arg0.getTransferable();
			if ( transferable.isDataFlavorSupported( DataFlavor.javaFileListFlavor)) {
				arg0.acceptDrop( DnDConstants.ACTION_COPY_OR_MOVE);
				List list = ( List)transferable.getTransferData( DataFlavor.javaFileListFlavor);
				if ( list.isEmpty()) {
					arg0.getDropTargetContext().dropComplete( true);
					return;
				}

				File[] files = ( File[])list.toArray( new File[ 0]);
				arg0.getDropTargetContext().dropComplete( true);
				if ( !append( files))
					return;

				repaint();
			} else {
				arg0.rejectDrop();
			}
		} catch (IOException ioe) {
			arg0.rejectDrop();
		} catch (UnsupportedFlavorException ufe) {
			arg0.rejectDrop();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent arg0) {
	}
}
