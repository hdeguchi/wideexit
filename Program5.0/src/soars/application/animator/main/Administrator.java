/**
 * 
 */
package soars.application.animator.main;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import soars.application.animator.common.image.AnimatorImageManager;
import soars.application.animator.file.exporter.CommonGraphicDataSaxWriter;
import soars.application.animator.file.importer.CommonGraphicDataSaxLoader;
import soars.application.animator.file.importer.Importer;
import soars.application.animator.file.loader.CommonSaxLoader;
import soars.application.animator.file.loader.FileProperty;
import soars.application.animator.file.writer.CommonSaxWriter;
import soars.application.animator.main.internal.AnimatorView;
import soars.application.animator.main.internal.AnimatorViewFrame;
import soars.application.animator.main.internal.InternalFrameRectangleMap;
import soars.application.animator.main.internal.ObjectManager;
import soars.application.animator.main.internal.WindowProperty;
import soars.application.animator.object.chart.ChartObjectMap;
import soars.application.animator.object.file.FileObject;
import soars.application.animator.object.file.HeaderObject;
import soars.application.animator.object.scenario.TimeKeeper;
import soars.application.animator.setting.common.CommonProperty;
import soars.common.soars.property.EditPropertyDlg;
import soars.common.soars.property.Property;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.swing.image.ImagePropertyManager;
import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.IObjectsMessageCallback;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.swing.message.ObjectsMessageDlg;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.file.Entry;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.file.ZipDecompressHandler;
import soars.common.utility.tool.file.ZipUtility;
import soars.common.utility.tool.timer.ITimerTaskImplementCallback;
import soars.common.utility.tool.timer.Timer2;
import soars.common.utility.tool.timer.TimerTaskImplement;

/**
 * @author kurata
 *
 */
public class Administrator extends Vector<ObjectManager> implements ITimerTaskImplementCallback, IMessageCallback, IObjectsMessageCallback, ZipDecompressHandler {

	/**
	 * 
	 */
	static public final String _commonDataFilenamePrefix = "common";

	/**
	 * 
	 */
	static public final String _dataFilenamePrefix = "data";

	/**
	 * 
	 */
	static public final String _dataFilenameExtension = ".aml";

	/**
	 * 
	 */
	static public final String _spotLogPropertiesFilename = "spot_log_properties.txt";

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private Administrator _administrator = null;

	/**
	 * 
	 */
	private Map<ObjectManager, AnimatorViewFrame> _animatorViewFrameMap = new HashMap<ObjectManager, AnimatorViewFrame>();

	/**
	 * 
	 */
	private File _currentFile = null;

	/**
	 * 
	 */
	private String _id = null;

	/**
	 * 
	 */
	private long _index = -1;

	/**
	 * 
	 */
	private String _title = "";

	/**
	 * 
	 */
	private String _simulatorTitle = "";

	/**
	 * 
	 */
	private String _visualShellTitle = "";

	/**
	 * 
	 */
	private File _parentDirectory = null;

	/**
	 * 
	 */
	private File _rootDirectory = null;

	/**
	 * 
	 */
	private boolean _modified = false;

	/**
	 * 
	 */
	private TimeKeeper _timeKeeper = null;

	/**
	 * 
	 */
	private Timer2[] _timer = new Timer2[ 2];

	/**
	 * 
	 */
	private TimerTaskImplement[] _timerTaskImplement = new TimerTaskImplement[ 2];

	/**
	 * 
	 */
	private int[] _timerID = new int[] { 0, 1};

	/**
	 * 
	 */
	private final long[] _delay = new long[] { 0l, 0l};

	/**
	 * 
	 */
	private final long[] _period = new long[] { 10l, 33l};

	/**
	 * 
	 */
	public int _tick = 0;

	/**
	 * 
	 */
	static private Object _scenarioLock = new Object();

	/**
	 * 
	 */
	private boolean _pausing = false;

	/**
	 * 
	 */
	private boolean _update = true;

	/**
	 * 
	 */
	private double _currentTime = -1.0f;

	/**
	 * 
	 */
	static public final int _success = 1;

	/**
	 * 
	 */
	static public final int _error = -1;

	/**
	 * 
	 */
	static public final int _cancel = 0;

	/**
	 * 
	 */
	static {
		startup();
	}

	/**
	 * 
	 */
	private static void startup() {
		synchronized( _lock) {
			if ( null == _administrator) {
				_administrator = new Administrator();
			}
		}
	}

	/**
	 * Returns the instance of the all object manager class.
	 * @return the instance of the all object manager class
	 */
	public static Administrator get_instance() {
		if ( null == _administrator)
			System.exit( 0);

		return _administrator;
	}

	/**
	 * 
	 */
	public Administrator() {
		super();
	}

	/**
	 * @param animatorView
	 * @param animatorViewFrame
	 * @return
	 */
	public ObjectManager create_ObjectManager(AnimatorView animatorView, AnimatorViewFrame animatorViewFrame) {
		ObjectManager objectManager = new ObjectManager( animatorView);
		add( objectManager);
		_animatorViewFrameMap.put( objectManager, animatorViewFrame);
		return objectManager;
	}

	/**
	 * @param srcObjectManager
	 * @param animatorViewFrame
	 * @param animatorView
	 * @return
	 */
	public ObjectManager duplicate_ObjectManager(ObjectManager srcObjectManager, AnimatorViewFrame animatorViewFrame, AnimatorView animatorView) {
		Object[] objects = ObjectsMessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"duplicate", ResourceManager.get_instance().get( "duplicate.animator.view.frame.show.message"), new Object[] { srcObjectManager, animatorView}, ( IObjectsMessageCallback)this, MainFrame.get_instance());
		if ( null == objects)
			return null;

		ObjectManager objectManager = ( ObjectManager)objects[ 0];
		add( objectManager);
		_animatorViewFrameMap.put( objectManager, animatorViewFrame);
		return objectManager;
	}

	/**
	 * @param objectManager
	 * @return
	 */
	public boolean can_remove(ObjectManager objectManager) {
		for ( ObjectManager om:this) {
			if ( om == objectManager)
				continue;

			if ( om._spotLog[ 0].equals( objectManager._spotLog[ 0]) && om._spotLog[ 1].equals( objectManager._spotLog[ 1]))
				return true;
		}
		return false;
	}

	/**
	 * @param objectManager
	 * @param internalFrameRectangleMap
	 * @return
	 */
	public boolean on_remove(ObjectManager objectManager, InternalFrameRectangleMap internalFrameRectangleMap) {
		AnimatorViewFrame animatorViewFrame = _animatorViewFrameMap.get( objectManager);
		if ( null == animatorViewFrame)
			return false;

		if ( JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(
			animatorViewFrame,
			ResourceManager.get_instance().get( "remove.animator.view.frame.confirm.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION))
			return false;

		internalFrameRectangleMap.remove( animatorViewFrame);
		MainFrame.get_instance().disable_sensor( animatorViewFrame);
		animatorViewFrame.dispose();
		objectManager.cleanup();
		remove( objectManager);
		modified( true);

		return true;
	}

	/* (non-Javadoc)
	 * @see java.util.Vector#remove(java.lang.Object)
	 */
	public boolean remove(Object object) {
		if ( !( object instanceof ObjectManager))
			return false;

		_animatorViewFrameMap.remove( object);
		return super.remove(object);
	}

	/**
	 * Returns true if the data file exists.
	 * @return true if the data file exists
	 */
	public boolean exist_datafile() {
		return ( null == _currentFile || 0 > _index) ? false : true;
	}

	/**
	 * Returns the current data file.
	 * @return the current data file
	 */
	public File get_current_file() {
		return _currentFile;
	}

	/**
	 * @return
	 */
	public File get_root_directory() {
		return _rootDirectory;
	}

	/**
	 * Returns true if the image directory exists.
	 * @return true if the image directory exists
	 */
	public boolean exist_image_directory() {
		if ( null == _parentDirectory || null == _rootDirectory)
			return false;

		File directory = new File( _rootDirectory, Constant._imageDirectory);
		return ( directory.exists() && directory.isDirectory());
	}

	/**
	 * Returns the image directory.
	 * @return the image directory
	 */
	public File get_image_directory() {
		return get_image_directory( _rootDirectory);
	}

	/**
	 * Returns the image directory.
	 * @param rootDirectory the root directory
	 * @return the image directory
	 */
	public File get_image_directory(File rootDirectory) {
		File directory = new File( rootDirectory, Constant._imageDirectory);
		if ( !directory.exists() && !directory.mkdir())
			return null;

		return directory;
	}

	/**
	 * Returns true if the thumbnail directory exists.
	 * @return true if the thumbnail directory exists
	 */
	public boolean exist_thumbnail_image_directory() {
		if ( null == _parentDirectory || null == _rootDirectory)
			return false;

		File directory = new File( _rootDirectory, Constant._thumbnailImageDirectory);
		return ( directory.exists() && directory.isDirectory());
	}

	/**
	 * Returns the thumbnail directory.
	 * @return the thumbnail directory
	 */
	public File get_thumbnail_image_directory() {
		return get_thumbnail_image_directory( _rootDirectory);
	}

	/**
	 * Returns the thumbnail directory.
	 * @param rootDirectory the root directory
	 * @return the thumbnail directory
	 */
	public File get_thumbnail_image_directory(File rootDirectory) {
		File directory = new File( rootDirectory, Constant._thumbnailImageDirectory);
		if ( !directory.exists() && !directory.mkdir())
			return null;

		return directory;
	}

	/**
	 * Returns the chart directory.
	 * @return the chart directory
	 */
	public File get_chart_directory() {
		return get_chart_directory( _rootDirectory);
	}

	/**
	 * Returns the chart directory.
	 * @param rootDirectory the root directory
	 * @return the chart directory
	 */
	public File get_chart_directory(File rootDirectory) {
		File directory = new File( rootDirectory, Constant._chartLogDirectory);
		if ( !directory.exists() && !directory.mkdir())
			return null;

		return directory;
	}

	/**
	 * Invoked when the data is changed.
	 * @param modified
	 */
	public void modified(boolean modified) {
		if ( Application._demo)
			return;

		_modified = modified;

		MainFrame.get_instance().setTitle(
			ResourceManager.get_instance().get( "application.title")
			+ " - [" + ( _title.equals( "") ? ResourceManager.get_instance().get( "application.no.title") : _title) + "]"
			+ " - [" + ( _simulatorTitle.equals( "") ? ResourceManager.get_instance().get( "application.no.title") : _simulatorTitle) + "]"
			+ " - [" + ( _visualShellTitle.equals( ResourceManager.get_instance().get( "application.no.title")) ? "" : _visualShellTitle) + "]"
			+ ( ( null == _currentFile) ? "" : " <" + _currentFile.getName() + ">")
			+ ( !modified ? "" : ( " " + ResourceManager.get_instance().get( "state.edit.modified"))));
//		if ( !MainFrame.get_instance().getTitle().equals( title))
//			MainFrame.get_instance().setTitle( title);
	}

	/**
	 * Returns true if the data is changed.
	 * @return true if the data is changed
	 */
	public boolean isModified() {
		return ( Application._demo ? false : _modified);
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
		stop_timer();
		CommonProperty.get_instance().cleanup();
		AnimatorImageManager.get_instance().cleanup();
		ImagePropertyManager.get_instance().cleanup();
		ChartObjectMap.get_instance().cleanup();
		_timeKeeper = null;
		_currentFile = null;
		_modified = false;
		_pausing = false;
		_update = true;

		if ( null != _parentDirectory)
			FileUtility.delete( _parentDirectory, true);

		_rootDirectory = null;
		_parentDirectory = null;

		_animatorViewFrameMap.clear();
		clear();
	}

	/**
	 * @param internalFrames
	 */
	public void resetScrollRectToVisible(JInternalFrame[] internalFrames) {
		for ( JInternalFrame internalFrame:internalFrames) {
			if ( !( internalFrame instanceof AnimatorViewFrame))
				continue;

			( ( AnimatorViewFrame)internalFrame).resetScrollRectToVisible();
		}
	}

	/**
	 * @param internalFrames
	 */
	public void cancel(JInternalFrame[] internalFrames) {
		for ( JInternalFrame internalFrame:internalFrames) {
			if ( !( internalFrame instanceof AnimatorViewFrame))
				continue;

			( ( AnimatorViewFrame)internalFrame).cancel();
		}
	}

	/**
	 * Returns true if loading the data from the specified file successfully.
	 * @param file the specified file
	 * @param index
	 * @param id
	 * @param title
	 * @param simulatorTitle
	 * @param visualShellTitle
	 * @return true if loading the data from the specified file successfully
	 */
	public boolean load(File file, String id, String index, String title, String simulatorTitle, String visualShellTitle) {
		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return false;

		if ( !MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"decompress_on_load", ResourceManager.get_instance().get( "file.open.show.message"), new Object[] { file, id, index, parentDirectory}, ( IMessageCallback)this, MainFrame.get_instance())) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		if ( !load()) {
			cleanup();
			return false;
		}

		_id = id;
		_index = Long.parseLong( index);
		_title = title;
		_simulatorTitle = simulatorTitle;
		_visualShellTitle = visualShellTitle;

		_currentFile = file;

		MainFrame.get_instance().update_all_AnimatorViewFrames();

		System.gc();

		return true;
	}

	/**
	 * @return
	 */
	private boolean load() {
		File dataFile = new File( _rootDirectory, _dataFilenamePrefix + _dataFilenameExtension);
		if ( dataFile.exists()) {
			if ( !prepare_to_load( _rootDirectory, dataFile))
				return false;
		} else {
			dataFile = new File( _rootDirectory, _dataFilenamePrefix + ".xml");
			if ( dataFile.exists()) {
				if ( !prepare_to_load( _rootDirectory, dataFile))
					return false;
			}
		}

		File commonDataFile = new File( _rootDirectory, _commonDataFilenamePrefix + _dataFilenameExtension);
		Vector<FileProperty> fileProperties = new Vector<FileProperty>();
		if ( commonDataFile.exists()) {
			if ( !commonDataFile.isFile() || !commonDataFile.canRead())
				return false;

			if ( !MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
				"load", ResourceManager.get_instance().get( "file.open.show.message"), new Object[] { commonDataFile, fileProperties}, ( IMessageCallback)this, MainFrame.get_instance()))
				return false;
		}

		int index = 1;
		List<File> files = new ArrayList<File>();
		while ( true) {
			File file = new File( _rootDirectory, _dataFilenamePrefix + String.valueOf( index++) + _dataFilenameExtension);
			if ( !file.exists() || !file.isFile() || !file.canRead())
				break;

			files.add( file);
		}

		if ( files.isEmpty())
			return false;

		for ( int i = 0; i < files.size(); ++i) {
			if ( !MainFrame.get_instance().create_AnimatorViewFrame( i + 1, files.get( i), fileProperties))
				return false;
		}

		_timeKeeper = new TimeKeeper( get( 0)._scenarioManager._headerObject);

		for ( ObjectManager objectManager:this) {
			AnimatorViewFrame animatorViewFrame = _animatorViewFrameMap.get( objectManager);
			if ( null == animatorViewFrame)
				return false;

			animatorViewFrame._order = objectManager._windowProperty._order;

			if ( !GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersects( objectManager._windowProperty._rectangle)
				|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( objectManager._windowProperty._rectangle).width <= 10
				|| objectManager._windowProperty._rectangle.y <= -animatorViewFrame.getInsets().top
				|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( objectManager._windowProperty._rectangle).height <= animatorViewFrame.getInsets().top)
				objectManager._windowProperty._rectangle = new Rectangle( 0, 0, AnimatorViewFrame._minimumWidth, AnimatorViewFrame._minimumHeight);

			animatorViewFrame.setBounds( objectManager._windowProperty._rectangle);

			if ( objectManager._windowProperty._maximum) {
				try {
					animatorViewFrame.setMaximum( true);
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				}
				continue;
			}

			if ( objectManager._windowProperty._icon) {
				try {
					animatorViewFrame.setIcon( true);
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				}
				continue;
			}
		}

		return true;
	}

	/**
	 * @param rootDirectory
	 * @param dataFile
	 * @return
	 */
	private boolean prepare_to_load(File rootDirectory, File dataFile) {
		if ( !dataFile.isFile() || !dataFile.canRead())
			return false;

		if ( !dataFile.renameTo( new File( rootDirectory, _dataFilenamePrefix + "1" + _dataFilenameExtension)))
			return false;

		return true;
	}

	/**
	 * @param internalFrames
	 */
	public void close(JInternalFrame[] internalFrames) {
		for ( JInternalFrame internalFrame:internalFrames) {
			MainFrame.get_instance().disable_sensor( internalFrame);
			if ( internalFrame instanceof AnimatorViewFrame) {
				AnimatorViewFrame animatorViewFrame = ( AnimatorViewFrame)internalFrame;
				animatorViewFrame.close();
				animatorViewFrame.dispose();
			} else
				internalFrame.dispose();
		}
		cleanup();
		System.gc();
	}

	/**
	 * @return
	 */
	public int save_property() {
		if ( null == _currentFile)
			return _error;

		TreeMap<String, Property> propertyMap = new TreeMap<String, Property>();
		int[] error = new int[ 1];
		Property property = get_property( propertyMap, error);
		if ( null == property) {
			if ( 0 <= error[ 0])
				return _cancel;
			else {
				JOptionPane.showMessageDialog( MainFrame.get_instance(), ResourceManager.get_instance().get( "file.save.property.error.message"), ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
				return _error;
			}
		}

		if ( 0 < error[ 0]) {
			modified( false);
			return _success;
		}

		if ( !MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"compress2", ResourceManager.get_instance().get( "file.save.property.show.message"), new Object[] { _currentFile, property, propertyMap}, ( IMessageCallback)this, MainFrame.get_instance())) {
			JOptionPane.showMessageDialog( MainFrame.get_instance(), ResourceManager.get_instance().get( "file.save.property.error.message"), ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
			return _error;
		}

		_title = property._title;

		modified( false);

		return _success;
	}

	/**
	 * Returns true if saving the data to the current file successfully.
	 * @param desktopPane
	 * @param internalFrameRectangleMap
	 * @return true if saving the data to the current file successfully
	 */
	public int save(JDesktopPane desktopPane, InternalFrameRectangleMap internalFrameRectangleMap) {
		if ( null == _currentFile)
			return _error;

		return save_as( desktopPane, internalFrameRectangleMap, _currentFile, ResourceManager.get_instance().get( "file.save.show.message"), ResourceManager.get_instance().get( "file.save.error.message"), true);
	}

	/**
	 * Returns true if saving the data to the specified file successfully.
	 * @param desktopPane
	 * @param internalFrameRectangleMap
	 * @return true if saving the data to the specified file successfully
	 */
	public int save_as(JDesktopPane desktopPane, InternalFrameRectangleMap internalFrameRectangleMap) {
		if ( null == _currentFile)
			return _error;

		return save_as( desktopPane, internalFrameRectangleMap, _currentFile, ResourceManager.get_instance().get( "file.saveas.show.message"), ResourceManager.get_instance().get( "file.saveas.error.message"), false);
	}

	/**
	 * Returns true if saving the data to the specified file successfully.
	 * @param desktopPane
	 * @param internalFrameRectangleMap
	 * @param file the specified file
	 * @param showMessage
	 * @param errorMessage
	 * @param overwrite
	 * @return true if saving the data to the specified file successfully
	 */
	public int save_as(JDesktopPane desktopPane, InternalFrameRectangleMap internalFrameRectangleMap, File file, String showMessage, String errorMessage, boolean overwrite) {
		TreeMap<String, Property> propertyMap = new TreeMap<String, Property>();
		int[] error = new int[ 1];
		Property property = get_property( propertyMap, error);
		if ( null == property) {
			if ( 0 <= error[ 0])
				return _cancel;
			else {
				JOptionPane.showMessageDialog( MainFrame.get_instance(), errorMessage, ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
				return _error;
			}
		}

		if ( !MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true, "save_as", showMessage, new Object[] { internalFrameRectangleMap, desktopPane}, ( IMessageCallback)this, MainFrame.get_instance())) {
			JOptionPane.showMessageDialog( MainFrame.get_instance(), errorMessage, ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
			return _error;
		}

		remove_all_datafiles();

		String text = "";
		for ( int i = 0; i < size(); ++i) {
			AnimatorViewFrame animatorViewFrame = _animatorViewFrameMap.get( get( i));
			if ( null == animatorViewFrame) {
				JOptionPane.showMessageDialog( MainFrame.get_instance(), errorMessage, ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
				return _error;
			}

			get( i)._id = ( i + 1);
			if ( !get( i).save_as( new WindowProperty( internalFrameRectangleMap.get( animatorViewFrame), animatorViewFrame.isMaximum(), animatorViewFrame.isIcon(), desktopPane.getComponentZOrder( animatorViewFrame)), new File( _rootDirectory, _dataFilenamePrefix + String.valueOf( get( i)._id) + _dataFilenameExtension))) {
				JOptionPane.showMessageDialog( MainFrame.get_instance(), errorMessage, ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
				return _error;
			}

			text += ( get( i)._spotLog[ 0] + "," + get( i)._spotLog[ 1] + "\n");
		}

		FileUtility.write_text_to_file( new File( _rootDirectory, _spotLogPropertiesFilename), text, "UTF8");

		remove_old_datafile( _rootDirectory);

		if ( !MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"compress1", showMessage, new Object[] { file, property, propertyMap, Boolean.valueOf( overwrite)}, ( IMessageCallback)this, MainFrame.get_instance())) {
			JOptionPane.showMessageDialog( MainFrame.get_instance(), errorMessage, ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
			return _error;
		}

		_currentFile = file;

		_title = property._title;

		modified( false);

		return _success;
	}

	/**
	 * @param propertyMap
	 * @param error
	 * @return
	 */
	private Property get_property(TreeMap<String, Property> propertyMap, int[] error) {
		error[ 0] = 0;

		if ( !Property.get_simulation_properties( propertyMap, _currentFile)) {
			error[ 0] = -1;
			return null;
		}

		if ( !Property.get_animation_properties( propertyMap, _currentFile)) {
			error[ 0] = -1;
			return null;
		}

		// 該当するシミュレーションプロパティが存在する筈！
		if ( null == propertyMap.get( _id)) {
			error[ 0] = -1;
			return null;
		}

		// この時点では_indexが-1である可能性がある
		// 従ってpropertyをpropertyMapへ追加することは出来ない
		// compress( ...)で_indexを決定した後追加を行う
		Property property = null;
		if ( 0 > _index)
			property = new Property( _currentFile, "animation", String.valueOf( _index), "", "", _id);
		else {
			Property p = propertyMap.get( _id)._propertyMap.get( String.valueOf( _index));
			if ( null == p) {
				error[ 0] = -1;
				return null;
			}

			property = new Property( _currentFile, "animation", String.valueOf( _index), p._title, p._comment, _id);
		}

		// ここでダイアログボックスを開いてタイトルとコメントを入力させる
		EditPropertyDlg editPropertyDlg = new EditPropertyDlg( MainFrame.get_instance(), true, property);
		if ( !editPropertyDlg.do_modal( MainFrame.get_instance()))
			return null;

		if ( !editPropertyDlg._changed)
			error[ 0] = 1;

		return property;
	}

	/**
	 * 
	 */
	private void remove_all_datafiles() {
		File[] files = _rootDirectory.listFiles();
		for ( File file:files) {
			if ( file.getName().startsWith( _dataFilenamePrefix) && file.getName().endsWith( _dataFilenameExtension))
				file.delete();
		}
	}

	/**
	 * @param rootDirectory
	 */
	private void remove_old_datafile(File rootDirectory) {
		File datafile = new File( rootDirectory, "data.xml");
		if ( datafile.exists() && datafile.isFile() && datafile.canRead())
			datafile.delete();

		datafile = new File( rootDirectory, "data.aml");
		if ( datafile.exists() && datafile.isFile() && datafile.canRead())
			datafile.delete();
	}

	/**
	 * @param directory
	 * @return
	 */
	public boolean import_from_directory(File directory) {
		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return false;

		File rootDirectory = Importer.copy( directory, parentDirectory,
			Constant._graphicPropertiesFilename, Constant._chartPropertiesFilename, Constant._chartLogDirectory);
		if ( null == rootDirectory) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		return import_from_directory( parentDirectory, rootDirectory, null, null, "", "");
	}

	/**
	 * @param parentDirectory
	 * @param rootDirectory
	 * @param soarsFile
	 * @param dataID
	 * @param simulatorTitle
	 * @param visualShellTitle
	 * @return
	 */
	public boolean import_from_directory(File parentDirectory, File rootDirectory, File soarsFile, String dataID, String simulatorTitle, String visualShellTitle) {
		cleanup();

		_parentDirectory = parentDirectory;
		_rootDirectory = rootDirectory;

		Map<String, List<String>> map = get_spotLogs();
		if ( map.isEmpty()) {
			cleanup();
			return false;
		}

		// 子ウィンドウとObjectManagerを生成
		int id = 1;
		for ( String type:Importer._subDirectoryNames) {
			List<String> names = map.get( type);
			if ( null == names)
				continue;

			for ( String name:names) {
				if ( name.equals( "$Name"))
					continue;

				if ( !MainFrame.get_instance().create_AnimatorViewFrame( id++, new String[] { type, name})) {
					cleanup();
					return false;
				}
			}
		}

		if ( !MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"import_from_directory", ResourceManager.get_instance().get( "file.import.show.message"), ( IMessageCallback)this, MainFrame.get_instance())) {
			cleanup();
			return false;
		}

		_simulatorTitle = simulatorTitle;
		_visualShellTitle = visualShellTitle;

		modified( true);

		MainFrame.get_instance().update_all_AnimatorViewFrames();

		if ( null != soarsFile) {
			_currentFile = soarsFile;

			if ( null != dataID)
				_id = dataID;

			modified( true);
		}

		System.gc();

		return true;
	}

	/**
	 * @return
	 */
	private Map<String, List<String>> get_spotLogs() {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for ( String subDirectoryName:Importer._subDirectoryNames) {
			File directory = new File( _rootDirectory, subDirectoryName);
			if ( !directory.exists() || !directory.isDirectory())
				continue;

			File[] files = directory.listFiles();
			if ( null == files)
				continue;

			List<String> names = new ArrayList<String>();

			for ( File file:files) {
				if ( !file.isFile() || !file.canRead())
					continue;

				names.add( file.getName().substring( 0, file.getName().length() - ".log".length()));
			}

			map.put( subDirectoryName, names);
		}

		return map;
	}

	/**
	 * Returns true if importing the graphic data from the specified file successfully.
	 * @param file the specified file
	 * @param id 
	 * @param index 
	 * @return positive value if importing the graphic data from the specified file successfully
	 */
	public int import_graphic_data(File file, String id, String index) {
		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return 0;

		if ( !MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"decompress_on_import_graphic_data", ResourceManager.get_instance().get( "file.import.graphic.data.show.message"), new Object[] { file, id, index, parentDirectory}, ( IMessageCallback)this, MainFrame.get_instance())) {
			FileUtility.delete( parentDirectory, true);
			return 0;
		}

		File rootDirectory = new File( parentDirectory, Constant._animatorRootDirectoryName);
		if ( !rootDirectory.exists() || !rootDirectory.isDirectory()) {
			FileUtility.delete( parentDirectory, true);
			return 0;
		}

		int result = import_graphic_data_internal( rootDirectory);
		if ( 0 >= result) {
			FileUtility.delete( parentDirectory, true);
			return result;
		}

		FileUtility.delete( parentDirectory, true);

		modified( true);

		System.gc();

		return result;
	}

	/**
	 * @param rootDirectory
	 * @return
	 */
	private int import_graphic_data_internal(File rootDirectory) {
		File dataFile = new File( rootDirectory, _dataFilenamePrefix + _dataFilenameExtension);
		if ( dataFile.exists()) {
			if ( !prepare_to_load( rootDirectory, dataFile))
				return 0;
		} else {
			dataFile = new File( rootDirectory, _dataFilenamePrefix + ".xml");
			if ( dataFile.exists()) {
				if ( !prepare_to_load( rootDirectory, dataFile))
					return 0;
			}
		}

		File commonDataFile = new File( rootDirectory, _commonDataFilenamePrefix + _dataFilenameExtension);
		if ( !commonDataFile.exists() || !commonDataFile.isFile() || !commonDataFile.canRead())
			return 0;

		File spotLogPropertiesFile = new File( rootDirectory, _spotLogPropertiesFilename);
		if ( !spotLogPropertiesFile.exists() || !spotLogPropertiesFile.isFile() || !spotLogPropertiesFile.canRead())
			return 0;

		if ( !MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"import_graphic_data", ResourceManager.get_instance().get( "file.open.show.message"), new Object[] { commonDataFile}, ( IMessageCallback)this, MainFrame.get_instance()))
			return -1;

		Vector<ObjectManager> objectManagers = new Vector<ObjectManager>();
		objectManagers.addAll( this);

		while ( !objectManagers.isEmpty()) {
			int size = objectManagers.size();
			int result = import_graphic_data_internal( spotLogPropertiesFile, rootDirectory, objectManagers);
			if ( 0 > result)
				return result;

			if ( size == objectManagers.size())
				break;
		}

		return 1;
	}
	
	/**
	 * @param spotLogPropertiesFile
	 * @param rootDirectory
	 * @param objectManagers
	 * @return
	 */
	private int import_graphic_data_internal(File spotLogPropertiesFile, File rootDirectory, Vector<ObjectManager> objectManagers) {
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader( new InputStreamReader( new FileInputStream( spotLogPropertiesFile), "UTF8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return -1;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		}

		int index = 1;
		while ( true) {
			String line = null;
			try {
				line = bufferedReader.readLine();
			} catch (IOException e1) {
				try {
					bufferedReader.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				e1.printStackTrace();
				return -1;
			}

			if ( null == line)
				break;

			String[] spotLog = Tool.split( line, ',');
			if ( null == spotLog || 2 != spotLog.length)
				continue;

			ObjectManager objectManager = get_corresponding_objectManager( spotLog, objectManagers);
			if ( null == objectManager)
				continue;

			File file = new File( rootDirectory, _dataFilenamePrefix + String.valueOf( index++) + _dataFilenameExtension);
			if ( !file.exists() || !file.isFile() || !file.canRead())
				continue;

			if ( !objectManager._animatorView.import_graphic_data( file, rootDirectory)) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return -1;
			}

			objectManagers.remove( objectManager);
			if ( objectManagers.isEmpty())
				break;
		}

		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return 1;
	}

	/**
	 * @param spotLog
	 * @param objectManagers
	 * @return
	 */
	private ObjectManager get_corresponding_objectManager(String[] spotLog, Vector<ObjectManager> objectManagers) {
		for ( ObjectManager objectManager:objectManagers) {
			if ( objectManager._spotLog[ 0].equals( spotLog[ 0]) && objectManager._spotLog[ 1].equals( spotLog[ 1]))
				return objectManager;
		}
		return null;
	}

	/**
	 * @param file
	 * @return
	 */
	public boolean export_graphic_data(File file) {
		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return false;

		File rootDirectory = new File( parentDirectory, Constant._animatorRootDirectoryName);
		if ( !rootDirectory.mkdirs()) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		if ( !MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true, "export_graphic_data", ResourceManager.get_instance().get( "file.export.graphic.data.show.message"), new Object[] { rootDirectory}, ( IMessageCallback)this, MainFrame.get_instance()))
			return false;

		String text = "";
		for ( int i = 0; i < size(); ++i) {
			get( i)._id = ( i + 1);
			if ( !get( i).export_graphic_data( new File( rootDirectory, _dataFilenamePrefix + String.valueOf( get( i)._id) + _dataFilenameExtension), rootDirectory))
				return false;

			text += ( get( i)._spotLog[ 0] + "," + get( i)._spotLog[ 1] + "\n");
		}

		FileUtility.write_text_to_file( new File( rootDirectory, _spotLogPropertiesFilename), text, "UTF8");

		remove_old_datafile( rootDirectory);

		if ( !MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"compress3", ResourceManager.get_instance().get( "file.export.graphic.data.show.message"), new Object[] { file, rootDirectory, parentDirectory}, ( IMessageCallback)this, MainFrame.get_instance()))
			return false;

		FileUtility.delete( parentDirectory, true);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IMessageCallback#message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.MessageDlg)
	 */
	public boolean message_callback(String id, Object[] objects, MessageDlg messageDlg) {
		if ( id.equals( "decompress_on_load"))
			return decompress_on_load( ( File)objects[ 0], ( String)objects[ 1], ( String)objects[ 2], ( File)objects[ 3]);
		else if ( id.equals( "load"))
			return load( ( File)objects[ 0], ( Vector<FileProperty>)objects[ 1]);
		else if ( id.equals( "save_as"))
			return save_as( ( InternalFrameRectangleMap)objects[ 0], ( JDesktopPane)objects[ 1]);
		else if ( id.equals( "compress1"))
			return compress( ( File)objects[ 0], ( Property)objects[ 1], ( TreeMap<String, Property>)objects[ 2], ( ( Boolean)objects[ 3]).booleanValue());
		else if ( id.equals( "compress2"))
			return compress( ( File)objects[ 0], ( Property)objects[ 1], ( TreeMap<String, Property>)objects[ 2]);
		else if ( id.equals( "compress3"))
			return compress( ( File)objects[ 0], ( File)objects[ 1], ( File)objects[ 2]);
		else if ( id.equals( "import_from_directory"))
			return import_from_directory();
		else if ( id.equals( "decompress_on_import_graphic_data"))
			return decompress_on_import_graphic_data( ( File)objects[ 0], ( String)objects[ 1], ( String)objects[ 2], ( File)objects[ 3]);
		else if ( id.equals( "import_graphic_data"))
			return import_common_graphic_data( ( File)objects[ 0]);
		else if ( id.equals( "export_graphic_data"))
			return export_common_graphic_data( ( File)objects[ 0]);
		return true;
	}

	/**
	 * @param file
	 * @param id
	 * @param index
	 * @param parentDirectory
	 * @return
	 */
	private boolean decompress_on_load(File file, String id, String index, File parentDirectory) {
		if ( !decompress( file, id, index, parentDirectory))
			return false;

		File rootDirectory = new File( parentDirectory, Constant._animatorRootDirectoryName);
		if ( !rootDirectory.exists() || !rootDirectory.isDirectory())
			return false;

		if ( !decompress_imagefiles_and_logfiles( rootDirectory, file, id))
			return false;

		cleanup();

		_parentDirectory = parentDirectory;
		_rootDirectory = rootDirectory;

		return true;
	}

	/**
	 * @param file
	 * @param id
	 * @param index
	 * @param parentDirectory
	 * @return
	 */
	private boolean decompress(File file, String id, String index, File parentDirectory) {
		boolean exist = false;
		try {
			ZipInputStream zipInputStream = new ZipInputStream( new FileInputStream( file));
			ZipEntry zipEntry = null;
			try {
				while ( null != ( zipEntry = zipInputStream.getNextEntry())) {
					if ( zipEntry.isDirectory()) {
						zipInputStream.closeEntry();
						continue;
					}
					if ( !zipEntry.getName().equals( Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + id + "/" + index + ".zip")) {
						zipInputStream.closeEntry();
						continue;
					}
					if ( !ZipUtility.decompress( zipInputStream, parentDirectory)) {
						//zipInputStream.closeEntry();
						return false;
					}
					//zipInputStream.closeEntry();
					exist = true;
					break;
				}
			} finally {
				zipInputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return exist;
	}

	/**
	 * @param propertyMap
	 * @param file
	 * @param id
	 * @return
	 */
	private boolean decompress_imagefiles_and_logfiles(File rootDirectory, File file, String id) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream( new FileInputStream( file));
			ZipEntry zipEntry = null;
			try {
				while ( null != ( zipEntry = zipInputStream.getNextEntry())) {
					if ( zipEntry.isDirectory()) {
						zipInputStream.closeEntry();
						continue;
					}
					if ( !zipEntry.getName().equals( Constant._soarsRootDirectoryName + "/" + Constant._visualShellZipFileName)
						&& !zipEntry.getName().equals( Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/" + id + ".zip")) {
						zipInputStream.closeEntry();
						continue;
					}
					if ( !ZipUtility.decompress( zipInputStream, rootDirectory, this, 0)) {
						zipInputStream.closeEntry();
						return false;
					}
					zipInputStream.closeEntry();
				}
			} finally {
				zipInputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.tool.file.ZipDecompressHandler#get_new_filename(int, java.lang.String)
	 */
	public String get_new_filename(int id, String filename) {
		switch ( id) {
			case 0:
				return get_new_filename0( filename);
			case 1:
				return get_new_filename1( filename);
		}
		return null;
	}

	/**
	 * @param filename
	 * @return
	 */
	private String get_new_filename0(String filename) {
		String[][] filenames = new String[][] {
			{ Constant._visualShellRootDirectoryName + "/" + Constant._imageDirectory, Constant._imageDirectory},
			{ Constant._visualShellRootDirectoryName + "/" + Constant._thumbnailImageDirectory, Constant._thumbnailImageDirectory},
			{ Constant._simulatorRootDirectoryName + "/agents", "agents"},
			{ Constant._simulatorRootDirectoryName + "/spots", "spots"},
			{ Constant._simulatorRootDirectoryName + "/" + Constant._chartLogDirectory, Constant._chartLogDirectory}
		};
		for ( int i = 0; i < filenames.length; ++i) {
			if ( filename.startsWith( filenames[ i][ 0]))
				return filenames[ i][ 1] + filename.substring( filenames[ i][ 0].length());
		}
		return null;
	}

	/**
	 * @param commonDataFile
	 * @param fileProperties
	 * @return
	 */
	private boolean load(File commonDataFile, Vector<FileProperty> fileProperties) {
		return CommonSaxLoader.execute( commonDataFile, fileProperties);
	}

	/**
	 * @param internalFrameRectangleMap
	 * @param desktopPane
	 * @return
	 */
	private boolean save_as(InternalFrameRectangleMap internalFrameRectangleMap, JDesktopPane desktopPane) {
		return CommonSaxWriter.execute( internalFrameRectangleMap, desktopPane, new File( _rootDirectory, _commonDataFilenamePrefix + _dataFilenameExtension), get( 0));
	}

	/**
	 * @param file
	 * @param property
	 * @param propertyMap
	 * @param overwrite
	 * @return
	 */
	private boolean compress(File file, Property property, TreeMap<String, Property> propertyMap, boolean overwrite) {
		if ( null == file || null == _id)
			return false;

		if ( !file.exists() || !ZipUtility.find( file, Constant._soarsRootDirectoryName + "/"))
			// ファイルが存在しないかまたは存在していてもSOARSデータファイルでない場合
			return false;

		// imagesとthumbnailsは保存せずにsoars/visualshell.zipへ反映させる
		File tempVisualShellZipFile = get_tempVisualShellZipFile( file);
		if ( null != tempVisualShellZipFile)
			tempVisualShellZipFile.deleteOnExit();

		if ( overwrite) {
			if ( 0 > _index) {
				if ( null != tempVisualShellZipFile)
					tempVisualShellZipFile.delete();
				return false;
			}

			if ( !ZipUtility.find( file, Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + _id + "/" + String.valueOf( _index) + ".zip")) {
				// 上書き保存であるにもかかわらず該当するファイルが存在しない場合
				if ( null != tempVisualShellZipFile)
					tempVisualShellZipFile.delete();
				return false;
			}
		} else {
			if ( !ZipUtility.find( file, Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/" + _id + ".zip")) {
				// 新規保存の際にシミュレーションデータファイルが存在しない場合
				if ( null != tempVisualShellZipFile)
					tempVisualShellZipFile.delete();
				return false;
			}

			if ( !ZipUtility.find( file, Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + _id + "/")) {
				_index = System.currentTimeMillis();
			} else {
				long index = System.currentTimeMillis();
				while ( ZipUtility.find( file, Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + _id + "/" + String.valueOf( index) + ".zip"))
					++index;

				_index = index;
			}
		}

		// agents、spots、charts、imagesとthumbnailsは保存しない
		List<String> exclusionFolderNames = new ArrayList<String>();
		exclusionFolderNames.add( Constant._animatorRootDirectoryName + "/agents");
		exclusionFolderNames.add( Constant._animatorRootDirectoryName + "/spots");
		exclusionFolderNames.add( Constant._animatorRootDirectoryName + "/" + Constant._chartLogDirectory);
		exclusionFolderNames.add( Constant._animatorRootDirectoryName + "/" + Constant._imageDirectory);
		exclusionFolderNames.add( Constant._animatorRootDirectoryName + "/" + Constant._thumbnailImageDirectory);

		File tempAnimatorZipFile;
		try {
			tempAnimatorZipFile = File.createTempFile( _id, ".zip");
		} catch (IOException e) {
			e.printStackTrace();
			if ( null != tempVisualShellZipFile)
				tempVisualShellZipFile.delete();
			return false;
		}

		tempAnimatorZipFile.deleteOnExit();

		// agents、spots、imagesとthumbnailsは保存しない
		if ( !ZipUtility.compress( tempAnimatorZipFile, _rootDirectory, _parentDirectory, exclusionFolderNames)) {
			tempAnimatorZipFile.delete();
			if ( null != tempVisualShellZipFile)
				tempVisualShellZipFile.delete();
			return false;
		}

		property._id = String.valueOf( _index);
		propertyMap.get( _id)._propertyMap.put( property._id, property);

		File tempAnimationPropertyFile;
		try {
			tempAnimationPropertyFile = File.createTempFile( "animation", ".xml");
		} catch (IOException e) {
			e.printStackTrace();
			tempAnimatorZipFile.delete();
			if ( null != tempVisualShellZipFile)
				tempVisualShellZipFile.delete();
			return false;
		}

		tempAnimationPropertyFile.deleteOnExit();

		if ( !Property.write_animation_properties( propertyMap, tempAnimationPropertyFile)) {
			tempAnimatorZipFile.delete();
			tempAnimationPropertyFile.delete();
			if ( null != tempVisualShellZipFile)
				tempVisualShellZipFile.delete();
			return false;
		}

		Map<String, List<Entry>> entryMap = new HashMap<String, List<Entry>>();
		List<Entry> entryList = new ArrayList<Entry>();

		Map<String, File> fileMap = new HashMap<String, File>();

		if ( null != tempVisualShellZipFile)
			// 画像が含まれていればsoars/visualshell.zipを更新
			fileMap.put( Constant._soarsRootDirectoryName + "/" + Constant._visualShellZipFileName, tempVisualShellZipFile);

		if ( !ZipUtility.find( file, Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/")) {
			// soars/animatorが含まれていない場合
			// soars/animatorを追加
			entryList.add( new Entry( Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/", null));

			// soars/animator/property.xmlを追加
			entryList.add( new Entry( Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + Constant._propertyFileName, tempAnimationPropertyFile));

			// soars/animator/[_id]を追加
			entryList.add( new Entry( Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + _id + "/", null));

			// soars/animator/[_id]に圧縮ファイルを追加
			entryList.add( new Entry( Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + _id + "/" + String.valueOf( _index) + ".zip", tempAnimatorZipFile));

			entryMap.put( Constant._soarsRootDirectoryName + "/", entryList);

//			file = ZipUtility.append( file, entryMap);
		} else {
			// soars/animatorが含まれている場合
//			List<Entry> entryList = new ArrayList<Entry>();
//			Map<String, File> fileMap = new HashMap<String, File>();

			if ( !ZipUtility.find( file, Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + Constant._propertyFileName)) {
				// soars/animator/property.xmlが含まれていなければ追加
				entryList.add( new Entry( Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + Constant._propertyFileName, tempAnimationPropertyFile));
			} else {
				// soars/animator/property.xmlが含まれていれば更新
				fileMap.put( Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + Constant._propertyFileName, tempAnimationPropertyFile);
			}

			if ( !ZipUtility.find( file, Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + _id + "/")) {
				// soars/animator/[_id]が含まれていない場合

				// soars/animator/[_id]を追加
				entryList.add( new Entry( Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + _id + "/", null));

				// soars/animator/[_id]に圧縮ファイルを追加
				entryList.add( new Entry( Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + _id + "/" + String.valueOf( _index) + ".zip", tempAnimatorZipFile));
			} else {
				// soars/animator/[_id]が含まれている場合
				if ( !ZipUtility.find( file, Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + _id + "/" + String.valueOf( _index) + ".zip")) {
					// soars/animator/[_id]/[_index].zipが含まれていなければ追加
					entryList.add( new Entry( Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + _id + "/" + String.valueOf( _index) + ".zip", tempAnimatorZipFile));
				} else {
					// soars/animator/[_id]/[_index].zipが含まれていれば更新
					fileMap.put( Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + _id + "/" + String.valueOf( _index) + ".zip", tempAnimatorZipFile);
				}
			}

//			Map<String, List<Entry>> entryMap = new HashMap<String, List<Entry>>();
			entryMap.put( Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/", entryList);

//			file = ZipUtility.update( file, entryMap, fileMap);
		}

		file = ZipUtility.update( file, entryMap, fileMap);

		if ( null != tempVisualShellZipFile)
			tempVisualShellZipFile.delete();

		tempAnimatorZipFile.delete();
		tempAnimationPropertyFile.delete();

		return ( null != file);
	}

	/**
	 * @param file
	 * @return
	 */
	private File get_tempVisualShellZipFile(File file) {
		if ( !exist_image_directory() || !exist_thumbnail_image_directory())
			return null;

		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return null;

		if ( !decompress( parentDirectory, file)) {
			FileUtility.delete( parentDirectory, true);
			return null;
		}

		File rootDirectory = new File( parentDirectory, Constant._visualShellRootDirectoryName);
		if ( !rootDirectory.exists() || !rootDirectory.isDirectory()) {
			FileUtility.delete( parentDirectory, true);
			return null;
		}

		if ( !FileUtility.copy_all( get_image_directory(), new File( rootDirectory, Constant._imageDirectory))) {
			FileUtility.delete( parentDirectory, true);
			return null;
		}

		if ( !FileUtility.copy_all( get_thumbnail_image_directory(), new File( rootDirectory, Constant._thumbnailImageDirectory))) {
			FileUtility.delete( parentDirectory, true);
			return null;
		}

		long now = System.currentTimeMillis();
		String filename = String.valueOf( now);

		File tempVisualShellZipFile;
		try {
			tempVisualShellZipFile = File.createTempFile( filename, null);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		if ( !ZipUtility.compress( tempVisualShellZipFile, rootDirectory, parentDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return null;
		}

		FileUtility.delete( parentDirectory, true);

		return tempVisualShellZipFile;
	}

	/**
	 * @param parentDirectory
	 * @param file
	 * @return
	 */
	private boolean decompress(File parentDirectory, File file) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream( new FileInputStream( file));
			ZipEntry zipEntry = null;
			try {
				while ( null != ( zipEntry = zipInputStream.getNextEntry())) {
					if ( zipEntry.isDirectory()) {
						zipInputStream.closeEntry();
						continue;
					}
					if ( !zipEntry.getName().equals( Constant._soarsRootDirectoryName + "/" + Constant._visualShellZipFileName)) {
						zipInputStream.closeEntry();
						continue;
					}
					if ( !ZipUtility.decompress( zipInputStream, parentDirectory, this, 1)) {
						zipInputStream.closeEntry();
						return false;
					}
					zipInputStream.closeEntry();
					break;
				}
			} finally {
				zipInputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param filename
	 * @return
	 */
	private String get_new_filename1(String filename) {
		return ( filename.startsWith( Constant._visualShellRootDirectoryName + "/" + Constant._imageDirectory)
			|| filename.startsWith( Constant._visualShellRootDirectoryName + "/" + Constant._thumbnailImageDirectory))
			? null : filename;
	}

	/**
	 * @param file
	 * @param property
	 * @param propertyMap
	 * @return
	 */
	private boolean compress(File file, Property property, TreeMap<String, Property> propertyMap) {
		property._id = String.valueOf( _index);
		propertyMap.get( _id)._propertyMap.put( property._id, property);

		File tempAnimationPropertyFile;
		try {
			tempAnimationPropertyFile = File.createTempFile( "animation", ".xml");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		if ( !Property.write_animation_properties( propertyMap, tempAnimationPropertyFile)) {
			tempAnimationPropertyFile.delete();
			return false;
		}

		Map<String, List<Entry>> entryMap = new HashMap<String, List<Entry>>();
		List<Entry> entryList = new ArrayList<Entry>();

		Map<String, File> fileMap = new HashMap<String, File>();

		if ( !ZipUtility.find( file, Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/")) {
			// soars/animatorが含まれていない場合→これは起こり得ない筈だが念の為
			// soars/animatorを追加
			entryList.add( new Entry( Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/", null));

			// soars/animator/property.xmlを追加
			entryList.add( new Entry( Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + Constant._propertyFileName, tempAnimationPropertyFile));

			entryMap.put( Constant._soarsRootDirectoryName + "/", entryList);
		} else {
			// soars/animatorが含まれている場合
			if ( !ZipUtility.find( file, Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + Constant._propertyFileName)) {
				// soars/animator/property.xmlが含まれていなければ追加
				entryList.add( new Entry( Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + Constant._propertyFileName, tempAnimationPropertyFile));
			} else {
				// soars/animator/property.xmlが含まれていれば更新
				fileMap.put( Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/" + Constant._propertyFileName, tempAnimationPropertyFile);
			}

			entryMap.put( Constant._soarsRootDirectoryName + "/" + Constant._animatorRootDirectoryName + "/", entryList);
		}

		file = ZipUtility.update( file, entryMap, fileMap);

		tempAnimationPropertyFile.delete();

		return ( null != file);
	}

	/**
	 * @param file
	 * @param rootDirectory
	 * @param parentDirectory
	 * @return
	 */
	private boolean compress(File file, File rootDirectory, File parentDirectory) {
		// agentsとspotsは保存しない
		List<String> exclusionFolderNames = new ArrayList<String>();
		exclusionFolderNames.add( Constant._animatorRootDirectoryName + "/agents");
		exclusionFolderNames.add( Constant._animatorRootDirectoryName + "/spots");

		return ZipUtility.compress( file, rootDirectory, parentDirectory, exclusionFolderNames);
	}

	/**
	 * @return
	 */
	private boolean import_from_directory() {
		if ( !make_indices_file( get( 0)._scenarioManager._headerObject))
			return false;

		for ( ObjectManager objectManager:this) {
			if ( !objectManager._scenarioManager.load()) {
				cleanup();
				return false;
			}

			objectManager._spotObjectManager.arrange();
			objectManager._agentObjectManager.arrange();

			objectManager.update_graphic_properties();
			objectManager.update_preferred_size();
		}

		_timeKeeper = new TimeKeeper( get( 0)._scenarioManager._headerObject);

		ChartObjectMap.get_instance().create_chartFrames(
			new File( _rootDirectory, Constant._chartPropertiesFilename),
			new File( _rootDirectory, Constant._chartLogDirectory));

		return true;
	}

	/**
	 * Returns true if the file that consists of the indices of SOARS log files is created successfully.
	 * @param headerObject
	 * @return true if the file that consists of the indices of SOARS log files is created successfully
	 */
	private boolean make_indices_file(HeaderObject headerObject) {
		if ( !open( headerObject)) {
			close(  headerObject);
			return false;
		}

		try {
			DataOutputStream dataOutputStream = new DataOutputStream( new FileOutputStream( headerObject._indicesFile));
			for ( int i = 0; i < headerObject._steps.length; ++i) {
				if ( !write( headerObject._steps[ i], dataOutputStream, headerObject)) {
					dataOutputStream.flush();
					dataOutputStream.close();
					close(  headerObject);
					return false;
				}
			}

			dataOutputStream.flush();
			dataOutputStream.close();
		} catch (IOException e) {
			close(  headerObject);
			e.printStackTrace();
			return false;
		}

		close(  headerObject);

		return true;
	}

	/**
	 * Returns true if all of SOARS log files are opened successfully.
	 * @param headerObject
	 * @return true if all of SOARS log files are opened successfully
	 */
	private boolean open(HeaderObject headerObject) {
		for ( int i = 0; i < headerObject._filenames.length; ++i) {
			FileObject fileObject = headerObject._fileObjectMap.get( headerObject._filenames[ i]);
			if ( null == fileObject)
				return false;

			if ( !fileObject.open())
				return false;
		}
		return true;
	}

	/**
	 * Closes all of SOARS log files.
	 * @param headerObject
	 */
	private void close(HeaderObject headerObject) {
		for ( int i = 0; i < headerObject._filenames.length; ++i) {
			FileObject fileObject = headerObject._fileObjectMap.get( headerObject._filenames[ i]);
			if ( null == fileObject)
				continue;

			fileObject.close();
		}
	}

	/**
	 * Returns true if the indices of SOARS log files, which correspond to the the specified step string, is written to the specified stream successfully.
	 * @param stepthe specified step string
	 * @param dataOutputStream the specified stream
	 * @return true if the indices of SOARS log files, which correspond to the the specified step string, is written to the specified stream successfully
	 */
	private boolean write(String step, DataOutputStream dataOutputStream, HeaderObject headerObject) {
		try {
			if ( step.equals( "")) {
				dataOutputStream.writeLong( -1l);
				dataOutputStream.writeInt( -1);
				dataOutputStream.writeInt( -1);
			} else {
				String[] words = step.split( "[/:]");
				if ( 3 > words.length)
					return false;

				dataOutputStream.writeLong( Long.parseLong( words[ 0]));
				dataOutputStream.writeInt( Integer.parseInt( words[ 1]));
				dataOutputStream.writeInt( Integer.parseInt( words[ 2]));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		String line = step;
		for ( int i = 0; i < headerObject._filenames.length; ++i) {
			FileObject fileObject = headerObject._fileObjectMap.get( headerObject._filenames[ i]);
			if ( null == fileObject)
				return false;

			if ( !fileObject.write( step, dataOutputStream))
				return false;
		}
		return true;
	}

	/**
	 * @param file
	 * @param id
	 * @param index
	 * @param parentDirectory
	 * @return
	 */
	private boolean decompress_on_import_graphic_data(File file, String id, String index, File parentDirectory) {
		if ( !decompress( file, id, index, parentDirectory))
			return false;

		File rootDirectory = new File( parentDirectory, Constant._animatorRootDirectoryName);
		if ( !rootDirectory.exists() || !rootDirectory.isDirectory())
			return false;

		if ( !decompress_imagefiles_and_logfiles( rootDirectory, file, id))
			return false;

		return true;
	}

	/**
	 * @param file
	 * @return
	 */
	private boolean import_common_graphic_data(File file) {
		return CommonGraphicDataSaxLoader.execute( file);
	}

	/**
	 * @param rootDirectory
	 * @return
	 */
	private boolean export_common_graphic_data(File rootDirectory) {
		return CommonGraphicDataSaxWriter.execute( new File( rootDirectory, _commonDataFilenamePrefix + _dataFilenameExtension));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IObjectsMessageCallback#objects_message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.ObjectsMessageDlg)
	 */
	public Object[] objects_message_callback(String id, Object[] objects, ObjectsMessageDlg objectsMessageDlg) {
		if ( id.equals( "duplicate"))
			return duplicate( ( ObjectManager)objects[ 0], ( AnimatorView)objects[ 1]);
		return null;
	}

	/**
	 * @param srcObjectManager
	 * @param animatorView
	 * @return
	 */
	private Object[] duplicate(ObjectManager srcObjectManager, AnimatorView animatorView) {
		return new Object[] { new ObjectManager( get_unique_id(), srcObjectManager, animatorView)};
	}

	/**
	 * @return
	 */
	private int get_unique_id() {
		int id = 1;
		while ( !is_unique_id( id))
			++id;
		return id;
	}

	/**
	 * @param id
	 * @return
	 */
	private boolean is_unique_id(int id) {
		for ( ObjectManager objectManager:this) {
			if ( objectManager._id == id)
				return false;
		}
		return true;
	}

	/**
	 * @param allFrames
	 */
	public void on_edit_common_property(JInternalFrame[] internalFrames) {
		for ( JInternalFrame internalFrame:internalFrames) {
			if ( !( internalFrame instanceof AnimatorViewFrame))
				continue;

			( ( AnimatorViewFrame)internalFrame).on_edit_common_property();
		}
	}

	/**
	 * @param internalFrames
	 */
	public void on_backward_head(JInternalFrame[] internalFrames) {
		synchronized ( _scenarioLock) {
			stop_timer();
			int previousPosition = _timeKeeper.get_current_position();
			_timeKeeper.set_current_position( 0);
			for ( JInternalFrame internalFrame:internalFrames) {
				if ( !( internalFrame instanceof AnimatorViewFrame))
					continue;

				( ( AnimatorViewFrame)internalFrame).update_current_position( _timeKeeper.get_current_position() - previousPosition, true);
				//( ( AnimatorViewFrame)internalFrame).on_backward_head();
			}
			_pausing = true;
			_timeKeeper.enable_animation_slider_dialog( true);
			update_charts();
			MainFrame.get_instance().update( _timeKeeper.get_information());
			_update = false;
			_timeKeeper.update_animation_slider_dialog();
			_update = true;
		}
	}

	/**
	 * @param animatorViewFrame
	 */
	public void on_edit_agent(AnimatorViewFrame animatorViewFrame) {
		animatorViewFrame.on_edit_agent();
	}

	/**
	 * @param animatorViewFrame
	 */
	public void on_edit_spot(AnimatorViewFrame animatorViewFrame) {
		animatorViewFrame.on_edit_spot();
	}

	/**
	 * @param animatorViewFrame
	 */
	public void on_edit_agent_property(AnimatorViewFrame animatorViewFrame) {
		animatorViewFrame.on_edit_agent_property();
	}

	/**
	 * @param animatorViewFrame
	 */
	public void on_edit_spot_property(AnimatorViewFrame animatorViewFrame) {
		animatorViewFrame.on_edit_spot_property();
	}

	/**
	 * @param internalFrames
	 */
	public void on_backward(JInternalFrame[] internalFrames) {
		synchronized ( _scenarioLock) {
			stop_timer();
			int previousPosition = _timeKeeper.get_current_position();
			_timeKeeper.backward();
			for ( JInternalFrame internalFrame:internalFrames) {
				if ( !( internalFrame instanceof AnimatorViewFrame))
					continue;

				( ( AnimatorViewFrame)internalFrame).update_current_position( _timeKeeper.get_current_position() - previousPosition, true);
				//( ( AnimatorViewFrame)internalFrame).on_backward();
			}
			_pausing = true;
			_timeKeeper.enable_animation_slider_dialog( true);
			update_charts();
			MainFrame.get_instance().update( _timeKeeper.get_information());
			_update = false;
			_timeKeeper.update_animation_slider_dialog();
			_update = true;
		}
	}

	/**
	 * @param internalFrames
	 */
	public void on_backward_step(JInternalFrame[] internalFrames) {
		synchronized ( _scenarioLock) {
			stop_timer();
			int previousPosition = _timeKeeper.get_current_position();
			_timeKeeper.backward_step();
			for ( JInternalFrame internalFrame:internalFrames) {
				if ( !( internalFrame instanceof AnimatorViewFrame))
					continue;

				( ( AnimatorViewFrame)internalFrame).update_current_position( _timeKeeper.get_current_position() - previousPosition, true);
				//( ( AnimatorViewFrame)internalFrame).on_backward_step();
			}
			_pausing = true;
			_timeKeeper.enable_animation_slider_dialog( true);
			update_charts();
			MainFrame.get_instance().update( _timeKeeper.get_information());
			_update = false;
			_timeKeeper.update_animation_slider_dialog();
			_update = true;
		}
	}

	/**
	 * @param internalFrames
	 */
	public void on_play(JInternalFrame[] internalFrames) {
		synchronized ( _scenarioLock) {
			for ( JInternalFrame internalFrame:internalFrames) {
				if ( !( internalFrame instanceof AnimatorViewFrame))
					continue;

				( ( AnimatorViewFrame)internalFrame).on_play( null == _timer[ 0] && null == _timer[ 1]);
			}
			if ( null == _timer[ 0] && null == _timer[ 1]) {
				if ( !_pausing)
					_timeKeeper.set_current_position( 0);

				_pausing = false;

				start_timer();
			}
			_timeKeeper.enable_animation_slider_dialog( false);
		}
	}

	/**
	 * @param internalFrames
	 */
	public void on_pause(JInternalFrame[] internalFrames) {
//		synchronized ( _scenarioLock) {
			stop_timer();
			for ( JInternalFrame internalFrame:internalFrames) {
				if ( !( internalFrame instanceof AnimatorViewFrame))
					continue;

				( ( AnimatorViewFrame)internalFrame).on_pause();
			}
			_pausing = true;
			_timeKeeper.enable_animation_slider_dialog( true);
			update_charts();
//		}
	}

	/**
	 * @param internalFrames
	 */
	public void on_stop(JInternalFrame[] internalFrames) {
//		synchronized ( _scenarioLock) {
			stop_timer();
			_timeKeeper.set_current_position( 0);
			for ( JInternalFrame internalFrame:internalFrames) {
				if ( !( internalFrame instanceof AnimatorViewFrame))
					continue;

				( ( AnimatorViewFrame)internalFrame).on_stop();
			}
			_pausing = false;
			_timeKeeper.dispose_animation_slider_dialog();
			ChartObjectMap.get_instance().clear_indication();
			MainFrame.get_instance().update( _timeKeeper.get_information());
//		}
	}

	/**
	 * @param internalFrames
	 */
	public void on_forward_step(JInternalFrame[] internalFrames) {
		synchronized ( _scenarioLock) {
			stop_timer();
			int previousPosition = _timeKeeper.get_current_position();
			_timeKeeper.forward_step();
			for ( JInternalFrame internalFrame:internalFrames) {
				if ( !( internalFrame instanceof AnimatorViewFrame))
					continue;

				( ( AnimatorViewFrame)internalFrame).update_current_position( _timeKeeper.get_current_position() - previousPosition, true);
//				( ( AnimatorViewFrame)internalFrame).on_forward_step();
			}
			_pausing = true;
			_timeKeeper.enable_animation_slider_dialog( true);
			update_charts();
			MainFrame.get_instance().update( _timeKeeper.get_information());
			_update = false;
			_timeKeeper.update_animation_slider_dialog();
			_update = true;
		}
	}

	/**
	 * @param internalFrames
	 */
	public void on_forward(JInternalFrame[] internalFrames) {
		synchronized ( _scenarioLock) {
			stop_timer();
			int previousPosition = _timeKeeper.get_current_position();
			_timeKeeper.forward();
			for ( JInternalFrame internalFrame:internalFrames) {
				if ( !( internalFrame instanceof AnimatorViewFrame))
					continue;

				( ( AnimatorViewFrame)internalFrame).update_current_position( _timeKeeper.get_current_position() - previousPosition, true);
				//( ( AnimatorViewFrame)internalFrame).on_forward();
			}
			_pausing = true;
			_timeKeeper.enable_animation_slider_dialog( true);
			update_charts();
			MainFrame.get_instance().update( _timeKeeper.get_information());
			_update = false;
			_timeKeeper.update_animation_slider_dialog();
			_update = true;
		}
	}

	/**
	 * @param internalFrames
	 */
	public void on_forward_tail(JInternalFrame[] internalFrames) {
		synchronized ( _scenarioLock) {
			stop_timer();
			int previousPosition = _timeKeeper.get_current_position();
			_timeKeeper.forward_tail();
			for ( JInternalFrame internalFrame:internalFrames) {
				if ( !( internalFrame instanceof AnimatorViewFrame))
					continue;

				( ( AnimatorViewFrame)internalFrame).update_current_position( _timeKeeper.get_current_position() - previousPosition, true);
				//( ( AnimatorViewFrame)internalFrame).on_forward_tail();
			}
			_pausing = true;
			_timeKeeper.enable_animation_slider_dialog( true);
			update_charts();
			MainFrame.get_instance().update( _timeKeeper.get_information());
			_update = false;
			_timeKeeper.update_animation_slider_dialog();
			_update = true;
		}
	}

	/**
	 * @param animatorViewFrame
	 */
	public void on_retrieve_agent_property(AnimatorViewFrame animatorViewFrame) {
		synchronized ( _scenarioLock) {
			stop_timer();
			animatorViewFrame.on_retrieve_agent_property();
			_pausing = true;
			MainFrame.get_instance().update( _timeKeeper.get_information());
		}
	}

	/**
	 * @param animatorViewFrame
	 */
	public void on_retrieve_spot_property(AnimatorViewFrame animatorViewFrame) {
		synchronized ( _scenarioLock) {
			stop_timer();
			animatorViewFrame.on_retrieve_spot_property();
			_pausing = true;
			MainFrame.get_instance().update( _timeKeeper.get_information());
		}
	}

	/**
	 * @param internalFrames
	 */
	public void on_animation_slider(JInternalFrame[] internalFrames) {
		synchronized ( _scenarioLock) {
			stop_timer();
			for ( JInternalFrame internalFrame:internalFrames) {
				if ( !( internalFrame instanceof AnimatorViewFrame))
					continue;

				( ( AnimatorViewFrame)internalFrame).on_main_animation_slider();
			}
			_pausing = true;
			_timeKeeper.show_animation_slider( MainFrame.get_instance(), MainFrame.get_instance());
			MainFrame.get_instance().update( _timeKeeper.get_information());
		}
	}

	/**
	 * _timeKeeperのSliderの位置が変更された時に呼ばれる
	 * ユーザによるSlider操作の場合は実行するが、それ以外の場合は実行しないようにする必要がある
	 * @param position
	 * @param slider
	 * @param internalFrames
	 */
	public void update_current_position(int position, boolean slider, JInternalFrame[] internalFrames) {
		if ( !_pausing)
			// 再生中は実行しない
			return;

		if ( !_update)
			// ツールバーボタン操作の場合も実行しない
			return;

		synchronized ( _scenarioLock) {
			stop_timer();
			for ( JInternalFrame internalFrame:internalFrames) {
				if ( !( internalFrame instanceof AnimatorViewFrame))
					continue;

				( ( AnimatorViewFrame)internalFrame).update_current_position( position - _timeKeeper.get_current_position(), slider);
			}
			_pausing = true;
			_timeKeeper.enable_animation_slider_dialog( true);
			_timeKeeper.set_current_position( position);
			update_charts();
			MainFrame.get_instance().update_user_interface();
			MainFrame.get_instance().update( _timeKeeper.get_information());
		}
	}

	/**
	 * @param internalFrames
	 * @return
	 */
	public boolean is_state_animation(JInternalFrame[] internalFrames) {
		if ( 0 == internalFrames.length)
			return false;

		for ( JInternalFrame internalFrame:internalFrames) {
			if ( !( internalFrame instanceof AnimatorViewFrame))
				continue;

			return ( ( AnimatorViewFrame)internalFrame).is_state_animation();
		}

		return false;
	}

	/**
	 * @param originalFilename
	 * @param newFilename
	 */
	public void update_image(String originalFilename, String newFilename) {
		for ( ObjectManager objectManager:this)
			objectManager.update_image( originalFilename, newFilename);
	}

	/**
	 * @param filename
	 * @return
	 */
	public boolean uses_this_image(String filename) {
		for ( ObjectManager objectManager:this) {
			if ( objectManager.uses_this_image( filename))
				return true;
		}
		return false;
	}

	/**
	 * 
	 */
	public void repaint() {
		for ( ObjectManager objectManager:this)
			objectManager._animatorView.repaint();
	}

	/**
	 * @return
	 */
	public String get_information() {
		if ( isEmpty())
			return "";

		return _timeKeeper.get_information();
	}

	/**
	 * @return
	 */
	public boolean is_head() {
		synchronized ( _scenarioLock) {
			if ( isEmpty())
				return true;

			return _timeKeeper.is_head();
		}
	}

	/**
	 * @return
	 */
	public boolean is_tail() {
		synchronized ( _scenarioLock) {
			if ( isEmpty())
				return true;

			return _timeKeeper.is_tail();
		}
	}

	/**
	 * @param selectedInternalFrame
	 * @return
	 */
	public boolean exist_agent_property(JInternalFrame selectedInternalFrame) {
		if ( null == selectedInternalFrame)
			return false;

		if ( !( selectedInternalFrame instanceof AnimatorViewFrame))
			return false;

		return ( ( AnimatorViewFrame)selectedInternalFrame).exist_agent_property();
	}

	/**
	 * @param selectedInternalFrame
	 * @return
	 */
	public boolean exist_selected_agent_property(JInternalFrame selectedInternalFrame) {
		if ( null == selectedInternalFrame)
			return false;

		if ( !( selectedInternalFrame instanceof AnimatorViewFrame))
			return false;

		return ( ( AnimatorViewFrame)selectedInternalFrame).exist_selected_agent_property();
	}

	/**
	 * @param selectedInternalFrame
	 * @return
	 */
	public boolean exist_spot_property(JInternalFrame selectedInternalFrame) {
		if ( null == selectedInternalFrame)
			return false;

		if ( !( selectedInternalFrame instanceof AnimatorViewFrame))
			return false;

		return ( ( AnimatorViewFrame)selectedInternalFrame).exist_spot_property();
	}

	/**
	 * @param selectedInternalFrame
	 * @return
	 */
	public boolean exist_selected_spot_property(JInternalFrame selectedInternalFrame) {
		if ( null == selectedInternalFrame)
			return false;

		if ( !( selectedInternalFrame instanceof AnimatorViewFrame))
			return false;

		return ( ( AnimatorViewFrame)selectedInternalFrame).exist_selected_spot_property();
	}

	/**
	 * 
	 */
	public void on_enter_animation_state() {
		_pausing = false;
		_update = true;
		_currentTime = -1.0f;
		_timeKeeper.reset();
		_timeKeeper.read();
	}

	/**
	 * 
	 */
	public void on_leave_animation_state() {
		_pausing = false;
		_update = true;
		_currentTime = -1.0f;
	}

	/**
	 * 
	 */
	private void start_timer() {
		if ( isEmpty())
			return;

		for ( int i = 0; i < _timer.length; ++i) {
			if ( null == _timer[ i]) {
				_timer[ i] = new Timer2();
				_timerTaskImplement[ i] = new TimerTaskImplement( _timerID[ i], this);
				_timer[ i].schedule( _timerTaskImplement[ i], _delay[ i], _period[ i]);
			}
		}

		for ( ObjectManager objectManager:this)
			objectManager._animatorView.on_start_timer();
	}

	/**
	 * 
	 */
	private void stop_timer() {
		if ( isEmpty())
			return;

		_pausing = false;
		_currentTime = -1.0f;

		for ( int i = 0; i < _timer.length; ++i) {
			if ( null != _timer[ i]) {
				_timer[ i].cancel();
				_timer[ i] = null;
			}
		}

		for ( ObjectManager objectManager:this)
			objectManager._animatorView.on_stop_timer();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.tool.timer.ITimerTaskImplementCallback#execute_timer_task(int)
	 */
	public void execute_timer_task(int id) {
		synchronized ( _scenarioLock) {
			if ( isEmpty())
				return;

			if ( 0 == id) {
				_timeKeeper.load_on_timer_task();
				for ( ObjectManager objectManager:this)
					objectManager._animatorView.load_on_timer_task();
			} else if ( 1 == id) {
				if ( _tick < CommonProperty.get_instance()._divide - 1)
					++_tick;
				else {
					_timeKeeper.next();
					for ( ObjectManager objectManager:this)
						objectManager._scenarioManager.next();

					_tick = 0;
				}

				_timeKeeper.animate_on_timer_task();
				for ( ObjectManager objectManager:this)
					objectManager._animatorView.animate_on_timer_task();

				update_charts();

				MainFrame.get_instance().update( _timeKeeper.get_information());
			}
		}
	}

	/**
	 * 
	 */
	private void update_charts() {
		double currentTime = _timeKeeper.get_current_time();
		if ( currentTime != _currentTime) {
			_currentTime = currentTime;
			ChartObjectMap.get_instance().indicate( _currentTime);
		}
	}
}
