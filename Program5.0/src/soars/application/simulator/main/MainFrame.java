/**
 * 
 */
package soars.application.simulator.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Label;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.xml.sax.SAXException;

import soars.application.simulator.data.ChartData;
import soars.application.simulator.data.FileManagerData;
import soars.application.simulator.data.LogData;
import soars.application.simulator.file.loader.SaxLoader;
import soars.application.simulator.file.writer.SaxWriter;
import soars.application.simulator.main.chart.ChartFrame;
import soars.application.simulator.main.filemanager.FileManagerFrame;
import soars.application.simulator.main.log.LiveLogViewerFrame;
import soars.application.simulator.main.log.LogViewerFrame;
import soars.application.simulator.main.log.StaticLogViewerFrame;
import soars.application.simulator.menu.file.ExitAction;
import soars.application.simulator.menu.file.IFileMenuHandler;
import soars.application.simulator.menu.file.SaveAction;
import soars.application.simulator.menu.file.SaveAsAction;
import soars.application.simulator.menu.help.AboutAction;
import soars.application.simulator.menu.help.ContentsAction;
import soars.application.simulator.menu.help.ForumAction;
import soars.application.simulator.menu.help.IHelpMenuHandler;
import soars.application.simulator.menu.run.AnimatorAction;
import soars.application.simulator.menu.run.FileManagerAction;
import soars.application.simulator.menu.run.IRunMenuHandler;
import soars.application.simulator.menu.simulation.ISimulationMenuHandler;
import soars.application.simulator.menu.simulation.StopAction;
import soars.application.simulator.stream.StdErrOutStreamPumper;
import soars.application.simulator.stream.StdOutStreamPumper;
import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.soars.property.EditPropertyDlg;
import soars.common.soars.property.Property;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.mac.IMacScreenMenuHandler;
import soars.common.utility.swing.mac.MacUtility;
import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.MDIChildFrame;
import soars.common.utility.swing.window.MDIFrame;
import soars.common.utility.tool.file.Entry;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.file.ZipDecompressHandler;
import soars.common.utility.tool.file.ZipUtility;
import soars.common.utility.tool.network.BrowserLauncher;
import soars.common.utility.tool.resource.Resource;
import soars.common.utility.tool.timer.ITimerTaskImplementCallback;
import soars.common.utility.tool.timer.TimerTaskImplement;
import soars.common.utility.xml.sax.Writer;
import soars.plugin.modelbuilder.chart.chart.main.InternalChartFrame;
import time.TimeCounter;

/**
 * @author kurata
 *
 */
public class MainFrame extends MDIFrame implements IMacScreenMenuHandler, ITimerTaskImplementCallback, IFileMenuHandler, IHelpMenuHandler, IRunMenuHandler, ISimulationMenuHandler, IMessageCallback, ZipDecompressHandler/*, DropTargetListener*/ {

	/**
	 * 
	 */
	static public final int _minimumWidth = 800;

	/**
	 * 
	 */
	static public final int _minimumHeight = 600;

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private MainFrame _mainFrame = null;

	/**
	 * 
	 */
	private long _id = -1;

	/**
	 * 
	 */
	private String _title = "";

	/**
	 * 
	 */
	private String _visualShellTitle = "";

	/**
	 * 
	 */
	private Rectangle _windowRectangle = new Rectangle();

	/**
	 * 
	 */
	private UserInterface _userInterface = null;

//	/**
//	 * 
//	 */
//	private JMenuItem _fileNewMenuItem = null;
//
//	/**
//	 * 
//	 */
//	private JMenuItem _fileOpenMenuItem = null;
//
//	/**
//	 * 
//	 */
//	private JMenuItem _fileCloseMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _fileSaveMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _fileSaveAsMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _simulationStopMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _runAnimatorMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _runFileManagerMenuItem = null;

//	/**
//	 * 
//	 */
//	private JButton _fileNewButton = null;
//
//	/**
//	 * 
//	 */
//	private JButton _fileOpenButton = null;
//
//	/**
//	 * 
//	 */
//	private JButton _fileCloseButton = null;

	/**
	 * 
	 */
	private JButton _fileSaveButton = null;

	/**
	 * 
	 */
	private JButton _fileSaveAsButton = null;

	/**
	 * 
	 */
	private JButton _simulationStopButton = null;

	/**
	 * 
	 */
	private JButton _runAnimatorButton = null;

	/**
	 * 
	 */
	private JButton _runFileManagerButton = null;

	/**
	 * 
	 */
	private JLabel _messageLabel = null;

	/**
	 * 
	 */
	private JLabel _informationLabel = null;

	/**
	 * 
	 */
	private final Color _backgroundColor = new Color( 128, 128, 128);

	/**
	 * 
	 */
	private String _graphicProperties = "";

	/**
	 * 
	 */
	private String _chartProperties = "";

//	/**
//	 * 
//	 */
//	private String _simulatorWindowTitle = "";

	/**
	 * 
	 */
	private String _simulatorWindowTime = "";

	/**
	 * 
	 */
	private String _logFolderPath = "";

	/**
	 * 
	 */
	private LogViewerFrame _logViewerFrame = null;

	/**
	 * 
	 */
	private env.Environment _environment = null;

	/**
	 * 
	 */
	private Timer _timer = null;

	/**
	 * 
	 */
	private TimerTaskImplement _timerTaskImplement = null;

	/**
	 * 
	 */
	private int _timerID = 0;

	/**
	 * 
	 */
	private final long _delay = 0;

	/**
	 * 
	 */
	private final long _period = 1000;

	/**
	 * 
	 */
	private boolean _running = false;

	/**
	 * 
	 */
	public StdOutStreamPumper _stdOutStreamPumper = null;

	/**
	 * 
	 */
	public StdErrOutStreamPumper _stdErrOutStreamPumper = null;

	/**
	 * 
	 */
	private File _currentFile = null;

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
	private InternalFrameRectangleMap _internalFrameRectangleMap = new InternalFrameRectangleMap();

	/**
	 * 
	 */
	static private final int _success = 1;

	/**
	 * 
	 */
	static private final int _error = -1;

	/**
	 * 
	 */
	static private final int _cancel = 0;

	/**
	 * @return
	 */
	public static MainFrame get_instance() {
		synchronized( _lock) {
			if ( null == _mainFrame) {
				_mainFrame = new MainFrame( ResourceManager.get_instance().get( "application.title"));
			}
		}
		return _mainFrame;
	}

	/**
	 * @param mdiChildFrame
	 * @return
	 */
	public static boolean append(MDIChildFrame mdiChildFrame) {
		synchronized( _lock) {
			if ( null == _mainFrame)
				return false;
		}

		_mainFrame._desktopPane.add( mdiChildFrame);

		Point position = SwingTool.get_default_window_position(
			_mainFrame._desktopPane.getBounds().width,
			_mainFrame._desktopPane.getBounds().height,
			mdiChildFrame.getBounds().width,
			mdiChildFrame.getBounds().height);
		mdiChildFrame.setLocation( position);
		mdiChildFrame.setSize( mdiChildFrame.getBounds().width, mdiChildFrame.getBounds().height);

		mdiChildFrame.toFront();

		_mainFrame._desktopPane.setSelectedFrame( mdiChildFrame);

		return true;
	}

	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public MainFrame(String arg0) throws HeadlessException {
		super(arg0);
	}

	/**
	 * @param mdiChildFrame
	 * @return
	 */
	public boolean appendChildFrame( MDIChildFrame mdiChildFrame) {
		_desktopPane.add( mdiChildFrame);

//		Point position = SwingTool.get_default_window_position(
//			_mainFrame._desktopPane.getBounds().width,
//			_mainFrame._desktopPane.getBounds().height,
//			mdiChildFrame.getBounds().width,
//			mdiChildFrame.getBounds().height);
//		mdiChildFrame.setLocation( position);
//		mdiChildFrame.setSize( mdiChildFrame.getBounds().width, mdiChildFrame.getBounds().height);

		mdiChildFrame.toFront();

		_desktopPane.setSelectedFrame( mdiChildFrame);

		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_work_directory() {
		if ( null != _parentDirectory)
			return true;

		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return false;

		File rootDirectory = new File( parentDirectory, Constant._simulatorRootDirectoryName);
		if ( !rootDirectory.mkdirs()) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		_parentDirectory = parentDirectory;
		_rootDirectory = rootDirectory;

		return true;
	}

	/**
	 * @param parentDirectory
	 * @return
	 */
	private boolean setup_work_directory(File parentDirectory) {
		File rootDirectory = new File( parentDirectory, Constant._simulatorRootDirectoryName);
		if ( !rootDirectory.exists()) {
			cleanup();
			return false;
		}

		_parentDirectory = parentDirectory;
		_rootDirectory = rootDirectory;

		return true;
	}

	/**
	 * Returns true if the agent log directory exists.
	 * @return true if the agent log directory exists
	 */
	public boolean exist_agent_log_directory() {
		if ( null == _parentDirectory || null == _rootDirectory)
			return false;

		File directory = new File( _rootDirectory, "agents");
		return ( directory.exists() && directory.isDirectory());
	}

	/**
	 * Returns the agent log directory.
	 * @return the agent log directory
	 */
	public File get_agent_log_directory() {
		if ( !setup_work_directory())
			return null;

		File directory = new File( _rootDirectory, "agents");
		if ( !directory.exists() && !directory.mkdir())
			return null;

		return directory;
	}

	/**
	 * Returns true if the spot log directory exists.
	 * @return true if the spot log directory exists
	 */
	public boolean exist_spot_log_directory() {
		if ( null == _parentDirectory || null == _rootDirectory)
			return false;

		File directory = new File( _rootDirectory, "spots");
		return ( directory.exists() && directory.isDirectory());
	}

	/**
	 * Returns the spot log directory.
	 * @return the spot log directory
	 */
	public File get_spot_log_directory() {
		if ( !setup_work_directory())
			return null;

		File directory = new File( _rootDirectory, "spots");
		if ( !directory.exists() && !directory.mkdir())
			return null;

		return directory;
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
		if ( !setup_work_directory())
			return null;

		File directory = new File( _rootDirectory, Constant._imageDirectory);
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
		if ( !setup_work_directory())
			return null;

		File directory = new File( _rootDirectory, Constant._thumbnailImageDirectory);
		if ( !directory.exists() && !directory.mkdir())
			return null;

		return directory;
	}

	/**
	 * Returns true if the user's data directory exists.
	 * @return true if the user's data directory exists
	 */
	public boolean exist_user_data_directory() {
		if ( null == _parentDirectory || null == _rootDirectory)
			return false;

		File directory = new File( _rootDirectory, Constant._userDataDirectory);
		return ( directory.exists() && directory.isDirectory());
	}

	/**
	 * Returns the user's data directory.
	 * @return the user's data directory
	 */
	public File get_user_data_directory() {
		if ( !setup_work_directory())
			return null;

		File directory = new File( _rootDirectory, Constant._userDataDirectory);
		if ( !directory.exists() && !directory.mkdir())
			return null;

		return directory;
	}

	/**
	 * 
	 */
	private void cleanup() {
		if ( null != _parentDirectory)
			FileUtility.delete( _parentDirectory, true);

		_rootDirectory = null;
		_parentDirectory = null;
	}

//	/**
//	 * @return
//	 */
//	public String get_simulator_window_title() {
//		return _simulatorWindowTitle;
//	}

	/**
	 * 
	 */
	private void get_property_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._mainWindowRectangleKey + "x",
			String.valueOf( SwingTool.get_default_window_position( _minimumWidth, _minimumHeight).x));
		_windowRectangle.x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._mainWindowRectangleKey + "y",
			String.valueOf( SwingTool.get_default_window_position( _minimumWidth, _minimumHeight).y));
		_windowRectangle.y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._mainWindowRectangleKey + "width",
			String.valueOf( _minimumWidth));
		_windowRectangle.width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._mainWindowRectangleKey + "height",
			String.valueOf( _minimumHeight));
		_windowRectangle.height = Integer.parseInt( value);
	}

	/**
	 * @return
	 */
	private void optimize_window_rectangle() {
		if ( !GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersects( _windowRectangle)
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( _windowRectangle).width <= 10
			|| _windowRectangle.y <= -getInsets().top
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( _windowRectangle).height <= getInsets().top)
			_windowRectangle.setBounds(
				SwingTool.get_default_window_position( _minimumWidth, _minimumHeight).x,
				SwingTool.get_default_window_position( _minimumWidth, _minimumHeight).y,
				_minimumWidth, _minimumHeight);
	}

	/**
	 * 
	 */
	private void set_property_to_environment_file() throws IOException {
		if ( null != _logViewerFrame)
			_logViewerFrame.set_property_to_environment_file();

		Environment.get_instance().set(
			Environment._mainWindowRectangleKey + "x", String.valueOf( _windowRectangle.x));
		Environment.get_instance().set(
			Environment._mainWindowRectangleKey + "y", String.valueOf( _windowRectangle.y));
		Environment.get_instance().set(
			Environment._mainWindowRectangleKey + "width", String.valueOf( _windowRectangle.width));
		Environment.get_instance().set(
			Environment._mainWindowRectangleKey + "height", String.valueOf( _windowRectangle.height));

		Environment.get_instance().store();
	}

	/**
	 * 
	 */
	private void setup_menu() {
		JToolBar statusBar = new JToolBar();

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		_messageLabel = new JLabel( "");
		//statusBar.add( _message_label);
		panel.add( _messageLabel);
		statusBar.add( panel);

		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));
		//panel.setLayout( new BorderLayout());

		//_information_label = new JLabel( "");
		_informationLabel = new JLabel( "                                             ");
		_informationLabel.setHorizontalAlignment( Label.RIGHT);
		//statusBar.add( _information_label);
		panel.add( _informationLabel);
		statusBar.add( panel);

		statusBar.setFloatable( false);

		//statusBar.setEnabled( false);

		getContentPane().add( statusBar, BorderLayout.SOUTH);




		JMenuBar menuBar = new JMenuBar();

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		JToolBar toolBar = new JToolBar();
		toolBar.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0));

		if ( !Application._demo)
			setup_menu( menuBar, toolBar);
		else
			setup_menu_for_demo( menuBar, toolBar);

		setJMenuBar( menuBar);

		toolBar.setFloatable( false);

		//toolBar.setEnabled( false);

		northPanel.add( toolBar);


		if ( !_logFolderPath.equals( "")) {
			panel = new JPanel();
			panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

			panel.add( new JLabel( ResourceManager.get_instance().get( "application.toolbar.log.folder.path") + _logFolderPath));
			northPanel.add( panel);

			insert_horizontal_glue( northPanel);
		}


		getContentPane().add( northPanel, BorderLayout.NORTH);
	}

	/**
	 * @param menuBar
	 * @param toolBar
	 */
	private void setup_menu(JMenuBar menuBar, JToolBar toolBar) {
		JMenu menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "file.menu"),
			true,
			ResourceManager.get_instance().get( "file.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.message"));


//		NewAction newAction = new NewAction( ResourceManager.get_instance().get( "file.new.menu"));
//		_fileNewMenuItem = _userInterface.append_menuitem(
//			menu,
//			ResourceManager.get_instance().get( "file.new.menu"),
//			newAction,
//			ResourceManager.get_instance().get( "file.new.mnemonic"),
//			ResourceManager.get_instance().get( "file.new.stroke"),
//			_messageLabel,
//			ResourceManager.get_instance().get( "file.new.message"));
//
//
//		OpenAction openAction = new OpenAction( ResourceManager.get_instance().get( "file.open.menu"), this);
//		_fileOpenMenuItem = _userInterface.append_menuitem(
//			menu,
//			ResourceManager.get_instance().get( "file.open.menu"),
//			openAction,
//			ResourceManager.get_instance().get( "file.open.mnemonic"),
//			ResourceManager.get_instance().get( "file.open.stroke"),
//			_messageLabel,
//			ResourceManager.get_instance().get( "file.open.message"));
//
//
//		CloseAction closeAction = new CloseAction(
//			ResourceManager.get_instance().get( "file.close.menu"));
//		_fileCloseMenuItem = _userInterface.append_menuitem(
//			menu,
//			ResourceManager.get_instance().get( "file.close.menu"),
//			closeAction,
//			ResourceManager.get_instance().get( "file.close.mnemonic"),
//			ResourceManager.get_instance().get( "file.close.stroke"),
//			_messageLabel,
//			ResourceManager.get_instance().get( "file.close.message"));
//
//
//		menu.addSeparator();


		SaveAction saveAction = new SaveAction(
			ResourceManager.get_instance().get( "file.save.menu"));
		_fileSaveMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.save.menu"),
			saveAction,
			ResourceManager.get_instance().get( "file.save.mnemonic"),
			ResourceManager.get_instance().get( "file.save.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.save.message"));


		SaveAsAction saveAsAction = new SaveAsAction( ResourceManager.get_instance().get( "file.save.as.menu"), this);
		_fileSaveAsMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.save.as.menu"),
			saveAsAction,
			ResourceManager.get_instance().get( "file.save.as.mnemonic"),
			ResourceManager.get_instance().get( "file.save.as.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.save.as.message"));


		menu.addSeparator();


		ExitAction exitAction = new ExitAction( ResourceManager.get_instance().get( "file.exit.menu"), this);
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.exit.menu"),
			exitAction,
			ResourceManager.get_instance().get( "file.exit.mnemonic"),
			ResourceManager.get_instance().get( "file.exit.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.exit.message"));




		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "simulation.menu"),
			true,
			ResourceManager.get_instance().get( "simulation.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "simulation.message"));


		StopAction stopAction = new StopAction( ResourceManager.get_instance().get( "simulation.stop.menu"), this);
		_simulationStopMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "simulation.stop.menu"),
			stopAction,
			ResourceManager.get_instance().get( "simulation.stop.mnemonic"),
			ResourceManager.get_instance().get( "simulation.stop.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "simulation.stop.message"));




		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "run.menu"),
			true,
			ResourceManager.get_instance().get( "run.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.message"));


		AnimatorAction animatorAction = new AnimatorAction( ResourceManager.get_instance().get( "run.animator.menu"), this);
		_runAnimatorMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "run.animator.menu"),
			animatorAction,
			ResourceManager.get_instance().get( "run.animator.mnemonic"),
			ResourceManager.get_instance().get( "run.animator.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.animator.message"));


		menu.addSeparator();


		FileManagerAction fileManagerAction = new FileManagerAction( ResourceManager.get_instance().get( "run.file.manager.menu"), this);
		_runFileManagerMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "run.file.manager.menu"),
			fileManagerAction,
			ResourceManager.get_instance().get( "run.file.manager.mnemonic"),
			ResourceManager.get_instance().get( "run.file.manager.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.file.manager.message"));




		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "help.menu"),
			true,
			ResourceManager.get_instance().get( "help.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "help.message"));


		ContentsAction contentsAction = new ContentsAction( ResourceManager.get_instance().get( "help.contents.menu"), this);
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "help.contents.menu"),
			contentsAction,
			ResourceManager.get_instance().get( "help.contents.mnemonic"),
			ResourceManager.get_instance().get( "help.contents.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "help.contents.message"));


		menu.addSeparator();


		ForumAction forumAction = new ForumAction( ResourceManager.get_instance().get( "help.forum.menu"), this);
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "help.forum.menu"),
			forumAction,
			ResourceManager.get_instance().get( "help.forum.mnemonic"),
			ResourceManager.get_instance().get( "help.forum.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "help.forum.message"));


		menu.addSeparator();


		AboutAction aboutAction = new AboutAction( ResourceManager.get_instance().get( "help.about.menu"), this);
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "help.about.menu"),
			aboutAction,
			ResourceManager.get_instance().get( "help.about.mnemonic"),
			ResourceManager.get_instance().get( "help.about.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "help.about.message"));


		//menuBar.setEnabled( false);



		ImageIcon imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/app_exit.png"));
		JButton button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "file.exit.menu"),
			exitAction,
			ResourceManager.get_instance().get( "file.exit.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.exit.message"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));


		toolBar.addSeparator();


//		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/new.png"));
//		_fileNewButton = _userInterface.append_tool_button(
//			toolBar,
//			imageIcon,
//			ResourceManager.get_instance().get( "file.new.menu"),
//			newAction,
//			ResourceManager.get_instance().get( "file.new.tooltip"),
//			_messageLabel,
//			ResourceManager.get_instance().get( "file.new.message"));
//		_fileNewButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileNewButton.getPreferredSize().height));
//
//
//		toolBar.addSeparator();
//
//
//		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/open.png"));
//		_fileOpenButton = _userInterface.append_tool_button(
//			toolBar,
//			imageIcon,
//			ResourceManager.get_instance().get( "file.open.menu"),
//			openAction,
//			ResourceManager.get_instance().get( "file.open.tooltip"),
//			_messageLabel,
//			ResourceManager.get_instance().get( "file.open.message"));
//		_fileOpenButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileOpenButton.getPreferredSize().height));
//
//
//		toolBar.addSeparator();
//
//
//		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/close.png"));
//		_fileCloseButton = _userInterface.append_tool_button(
//			toolBar,
//			imageIcon,
//			ResourceManager.get_instance().get( "file.close.menu"),
//			closeAction,
//			ResourceManager.get_instance().get( "file.close.tooltip"),
//			_messageLabel,
//			ResourceManager.get_instance().get( "file.close.message"));
//		_fileCloseButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileCloseButton.getPreferredSize().height));
//
//
//		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/save.png"));
		_fileSaveButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "file.save.menu"),
			saveAction,
			ResourceManager.get_instance().get( "file.save.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.save.message"));
		_fileSaveButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileSaveButton.getPreferredSize().height));


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/save_as.png"));
		_fileSaveAsButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "file.save.as.menu"),
			saveAsAction,
			ResourceManager.get_instance().get( "file.save.as.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.save.as.message"));
		_fileSaveAsButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileSaveAsButton.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/simulation/stop.png"));
		_simulationStopButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "simulation.stop.menu"),
			stopAction,
			ResourceManager.get_instance().get( "simulation.stop.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "simulation.stop.message"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/run/animator.png"));
		_runAnimatorButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "run.animator.menu"),
			animatorAction,
			ResourceManager.get_instance().get( "run.animator.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.animator.message"));
		_runAnimatorButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _runAnimatorButton.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/run/file_manager.png"));
		_runFileManagerButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "run.file.manager.menu"),
			fileManagerAction,
			ResourceManager.get_instance().get( "run.file.manager.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.file.manager.message"));
		_runFileManagerButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _runAnimatorButton.getPreferredSize().height));


		initialize_user_interface();
	}

	/**
	 * @param menuBar
	 * @param toolBar
	 */
	private void setup_menu_for_demo(JMenuBar menuBar, JToolBar toolBar) {
		JMenu menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "file.menu"),
			true,
			ResourceManager.get_instance().get( "file.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.message"));


		ExitAction exitAction = new ExitAction( ResourceManager.get_instance().get( "file.exit.menu"), this);
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.exit.menu"),
			exitAction,
			ResourceManager.get_instance().get( "file.exit.mnemonic"),
			ResourceManager.get_instance().get( "file.exit.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.exit.message"));




		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "simulation.menu"),
			true,
			ResourceManager.get_instance().get( "simulation.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "simulation.message"));


		StopAction stopAction = new StopAction( ResourceManager.get_instance().get( "simulation.stop.menu"), this);
		_simulationStopMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "simulation.stop.menu"),
			stopAction,
			ResourceManager.get_instance().get( "simulation.stop.mnemonic"),
			ResourceManager.get_instance().get( "simulation.stop.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "simulation.stop.message"));




		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "run.menu"),
			true,
			ResourceManager.get_instance().get( "run.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.message"));


		AnimatorAction animatorAction = new AnimatorAction( ResourceManager.get_instance().get( "run.animator.menu"), this);
		_runAnimatorMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "run.animator.menu"),
			animatorAction,
			ResourceManager.get_instance().get( "run.animator.mnemonic"),
			ResourceManager.get_instance().get( "run.animator.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.animator.message"));


		menu.addSeparator();


		FileManagerAction fileManagerAction = new FileManagerAction( ResourceManager.get_instance().get( "run.file.manager.menu"), this);
		_runFileManagerMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "run.file.manager.menu"),
			fileManagerAction,
			ResourceManager.get_instance().get( "run.file.manager.mnemonic"),
			ResourceManager.get_instance().get( "run.file.manager.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.file.manager.message"));




		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "help.menu"),
			true,
			ResourceManager.get_instance().get( "help.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "help.message"));


		AboutAction aboutAction = new AboutAction( ResourceManager.get_instance().get( "help.about.menu"), this);
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "help.about.menu"),
			aboutAction,
			ResourceManager.get_instance().get( "help.about.mnemonic"),
			ResourceManager.get_instance().get( "help.about.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "help.about.message"));


		//menuBar.setEnabled( false);



		ImageIcon imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/app_exit.png"));
		JButton button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "file.exit.menu"),
			exitAction,
			ResourceManager.get_instance().get( "file.exit.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.exit.message"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/simulation/stop.png"));
		_simulationStopButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "simulation.stop.menu"),
			stopAction,
			ResourceManager.get_instance().get( "simulation.stop.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "simulation.stop.message"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/run/animator.png"));
		_runAnimatorButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "run.animator.menu"),
			animatorAction,
			ResourceManager.get_instance().get( "run.animator.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.animator.message"));
		_runAnimatorButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _runAnimatorButton.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/run/file_manager.png"));
		_runFileManagerButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "run.file.manager.menu"),
			fileManagerAction,
			ResourceManager.get_instance().get( "run.file.manager.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.file.manager.message"));
		_runFileManagerButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _runAnimatorButton.getPreferredSize().height));


		initialize_user_interface();
	}

	/**
	 * 
	 */
	private void initialize_user_interface() {
		setTitle( ResourceManager.get_instance().get( "application.title"));
		update_user_interface( true, true, false, false, false, false, false, false);
		update( "                                             ");
		_currentFile = null;
	}

	/**
	 * @param fileNew
	 * @param fileOpen
	 * @param fileClose
	 * @param fileSave
	 * @param fileSaveAs
	 * @param stopSimulation
	 * @param runAnimator
	 * @param runFileManager
	 */
	private void update_user_interface(boolean fileNew, boolean fileOpen,
		boolean fileClose, boolean fileSave, boolean fileSaveAs,
		boolean stopSimulation, boolean runAnimator, boolean runFileManager) {
//		enable_menuItem( _fileNewMenuItem, fileNew);
//		enable_menuItem( _fileOpenMenuItem, fileOpen);
//		enable_menuItem( _fileCloseMenuItem, fileClose);
		enable_menuItem( _fileSaveMenuItem, fileSave);
		enable_menuItem( _fileSaveAsMenuItem, fileSaveAs);

		enable_menuItem( _simulationStopMenuItem, stopSimulation);

		enable_menuItem( _runAnimatorMenuItem, runAnimator);
		enable_menuItem( _runFileManagerMenuItem, runFileManager);

//		enable_button( _fileNewButton, fileNew);
//		enable_button( _fileOpenButton, fileOpen);
//		enable_button( _fileCloseButton, fileClose);
		enable_button( _fileSaveButton, fileSave);
		enable_button( _fileSaveAsButton, fileSaveAs);

		enable_button( _simulationStopButton, stopSimulation);

		enable_button( _runAnimatorButton, runAnimator);
		enable_button( _runFileManagerButton, runFileManager);
	}

	/**
	 * @param menuItem
	 * @param enable
	 */
	private void enable_menuItem(JMenuItem menuItem, boolean enable) {
		if ( null == menuItem)
			return;

		menuItem.setEnabled( enable);
	}

	/**
	 * @param button
	 * @param enable
	 */
	private void enable_button(JButton button, boolean enable) {
		if ( null == button)
			return;

		button.setEnabled( enable);
	}

	/**
	 * @param modified
	 */
	public void modified(boolean modified) {
		if ( Application._demo)
			return;

		MainFrame.get_instance().setTitle(
			ResourceManager.get_instance().get( "application.title")
			+ " - [" + ( _title.equals( "") ? ResourceManager.get_instance().get( "application.no.title") : _title) + "]"
			+ " - [" + ( _visualShellTitle.equals( "") ? ResourceManager.get_instance().get( "application.no.title") : _visualShellTitle) + "]"
			+ ( ( null == _currentFile) ? "" : " <" + _currentFile.getName() + ">")
			+ ( !modified ? "" : ( " " + ResourceManager.get_instance().get( "state.edit.modified"))));
//		String title = "SOARS Simulator";
//		if ( null != _currentFile)
//			title += ( " - " + _currentFile.getName());
//
////		if ( !_simulatorWindowTitle.equals( ""))
////			title += ( " - " + _simulatorWindowTitle);
//
//		if ( modified)
//			title += ResourceManager.get_instance().get( "state.edit.modified");
//
//		setTitle( title);
		_modified = modified;
	}

	/**
	 * @param parentDirectory
	 * @param logFolderPath 
	 * @return
	 */
	public boolean create(File parentDirectory, String logFolderPath) {
		if ( !setup_work_directory( parentDirectory))
			return false;

		_graphicProperties = FileUtility.read_text_from_file( new File( _rootDirectory, Constant._graphicPropertiesFilename), "UTF-8");
		if ( null == _graphicProperties)
			_graphicProperties = "";

		_chartProperties = FileUtility.read_text_from_file( new File( _rootDirectory, Constant._chartPropertiesFilename), "UTF-8");
		if ( null == _chartProperties)
			_chartProperties = "";

		_logFolderPath = logFolderPath;

		return super.create();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		if ( !MacUtility.setup_screen_menu_handler( this, this, ResourceManager.get_instance().get( "application.title")))
			return false;

		_desktopPane.setBackground( _backgroundColor);

		get_property_from_environment_file();

		_userInterface = new UserInterface();

		setup_menu();

		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

		pack();

		optimize_window_rectangle();
		setLocation( _windowRectangle.x, _windowRectangle.y);
		setSize( _windowRectangle.width, _windowRectangle.height);

		Toolkit.getDefaultToolkit().setDynamicLayout( true);

		setVisible( true);

//		new DropTarget( this, this);

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				int width = getSize().width;
				int height = getSize().height;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width,
					( _minimumHeight > height) ? _minimumHeight : height);
			}
		});

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.mac.IMacScreenMenuHandler#on_mac_about()
	 */
	public void on_mac_about() {
		on_help_about( null);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.mac.IMacScreenMenuHandler#on_mac_quit()
	 */
	public void on_mac_quit() {
		on_file_exit( null);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_window_closing(java.awt.event.WindowEvent)
	 */
	protected void on_window_closing(WindowEvent windowEvent) {
		if ( _modified) {
			int result = confirm1();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return;
		} else {
			int result = confirm2();
			if ( JOptionPane.YES_OPTION != result/* && JOptionPane.NO_OPTION != result*/)
				return;
//			if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
//				this,
//				ResourceManager.get_instance().get( "file.exit.confirm.message"),
//				ResourceManager.get_instance().get( "application.title"),
//				JOptionPane.YES_NO_OPTION)) {
//				requestFocus();
//				return;
//			}
		}

		_windowRectangle = getBounds();
		try {
			set_property_to_environment_file();
		}
		catch (IOException e) {
			throw new RuntimeException( e);
		}

		stop_timer();

		if ( null != _logViewerFrame)
			_logViewerFrame.stop_simulation();

		cleanup();

		Application.get_instance().exit_instance();
	}

	/**
	 * @return
	 */
	public boolean createStaticLogViewerFrame() {
		_logViewerFrame = new StaticLogViewerFrame(
			ResourceManager.get_instance().get( "log.viewer.window.title")/* + ( experiment.equals( "") ? "" : ( " - " + experiment))*/,
			true, false, true, true);
		if ( !_logViewerFrame.create())
			return false;

		Image image = Resource.load_image_from_resource( Constant._resourceDirectory + "/image/icon/icon.png", getClass());
		if ( null != image)
			_logViewerFrame.setFrameIcon( new ImageIcon( image));

		_desktopPane.add( _logViewerFrame);

		_logViewerFrame.toFront();

		_desktopPane.setSelectedFrame( _logViewerFrame);

		return true;
	}

	/**
	 * @param reader
	 * @param experiment
	 * @param visualShellTitle
	 * @param soarsFilename
	 * @return
	 */
	public boolean start(Reader reader, String experiment, String visualShellTitle, String soarsFilename) {
//		if ( !experiment.equals( ""))
//			_simulatorWindowTitle = experiment;
//
//		if ( soarsFilename.equals( ""))
//			_simulatorWindowTitle += ( _simulatorWindowTitle.equals( "") ? "" : " - ") + ResourceManager.get_instance().get( "main.title.no.name");
//		else {
//			File soarsFile = new File( soarsFilename);
//			if ( soarsFile.exists() && soarsFile.canRead() && soarsFile.canWrite()) {
//				_currentFile = soarsFile;
//				_simulatorWindowTitle += ( _simulatorWindowTitle.equals( "") ? " [" : " - [") + soarsFile.getName() + "]";
//			} else
//				_simulatorWindowTitle += ( _simulatorWindowTitle.equals( "") ? "" : " - ") + ResourceManager.get_instance().get( "main.title.no.name");
//		}
//
//		if ( !_simulatorWindowTitle.equals( ""))
//			setTitle( getTitle() + " - " + _simulatorWindowTitle);
//
//		if ( !Application._demo) {
//			setTitle( getTitle() + " " + ResourceManager.get_instance().get( "state.edit.modified"));
//			_modified = true;
//		}

		if ( !soarsFilename.equals( "")) {
			File soarsFile = new File( soarsFilename);
			if ( soarsFile.exists() && soarsFile.canRead() && soarsFile.canWrite())
				_currentFile = soarsFile;
		}

		setTitle( ResourceManager.get_instance().get( "application.title")
			+ " - [" + ( experiment.equals( "") ? ResourceManager.get_instance().get( "application.no.title") : experiment) + "]"
			+ " - [" + ( visualShellTitle.equals( "") ? ResourceManager.get_instance().get( "application.no.title") : visualShellTitle) + "]"
			+ ( ( null == _currentFile) ? "" : " <" + _currentFile.getName() + ">"));

		if ( !Application._demo) {
			setTitle( getTitle() + " " + ResourceManager.get_instance().get( "state.edit.modified"));
			_modified = true;
		}

		_logViewerFrame = new LiveLogViewerFrame(
			ResourceManager.get_instance().get( "log.viewer.window.title")/* + ( experiment.equals( "") ? "" : ( " - " + experiment))*/,
			true, false, true, true);
		if ( !_logViewerFrame.create())
			return false;

		Image image = Resource.load_image_from_resource( Constant._resourceDirectory + "/image/icon/icon.png", getClass());
		if ( null != image)
			_logViewerFrame.setFrameIcon( new ImageIcon( image));

		_desktopPane.add( _logViewerFrame);

		_logViewerFrame.toFront();

		_desktopPane.setSelectedFrame( _logViewerFrame);

		_stdOutStreamPumper = new StdOutStreamPumper();
		_stdOutStreamPumper.start();

		_stdErrOutStreamPumper = new StdErrOutStreamPumper();
		_stdErrOutStreamPumper.start();

		if ( !_logViewerFrame.start( reader, _stdOutStreamPumper, _stdErrOutStreamPumper))
			return false;

		_logViewerFrame.optimize_window_rectangle();

		start_timer();

		update_user_interface( false, false, false, false, true, true, false, false);

		_title = experiment;
		_visualShellTitle = visualShellTitle;

		return true;
	}

	/**
	 * 
	 */
	public void on_start_simulation() {
		_environment = env.Environment.getCurrent();
	}

//	/**
//	 * 
//	 */
//	public void on_terminate_simulation() {
//		stop_timer();
//		_logViewerFrame.flush();
//		set_sensor( true);
//		update( "[ " + get_time() + " ]");
//		update_user_interface( true, true, false, false, true, false, _logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists());
//	}

	/**
	 * 
	 */
	private void start_timer() {
		if ( null == _timer) {
			_timer = new Timer();
			_timerTaskImplement = new TimerTaskImplement( _timerID, this);
			_timer.schedule( _timerTaskImplement, _delay, _period);
		}
	}

	/**
	 * 
	 */
	private void stop_timer() {
		if ( null != _timer) {
			_timer.cancel();
			_timer = null;
		}

		if ( null != _stdOutStreamPumper)
			_stdOutStreamPumper.cleanup();

		if ( null != _stdErrOutStreamPumper)
			_stdErrOutStreamPumper.cleanup();

		_running = false;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.tool.timer.ITimerTaskImplementCallback#execute_timer_task(int)
	 */
	public void execute_timer_task(int id) {
		if ( _timerID != id)
			return;

		if ( _running)
			return;

		if ( null == _environment)
			return;

		_running = true;

		update( "[ " + get_time() + " ]");

		if ( _logViewerFrame._terminated) {
			stop_timer();
			_logViewerFrame.flush();
			set_sensor( true);
			update_user_interface( true, true, false, false, true, false, _logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists(), exist_user_data_directory());
		}
//		if ( _logViewerFrame.terminated_normally()) {
//			update_user_interface( true, true, false, false, true, false, _logViewerFrame.dollar_spot_log_exists());
//			_stdOutStreamPumper.cleanup();
//			_stdErrOutStreamPumper.cleanup();
//			_logViewerFrame.flush();
//		}

		_running = false;
	}

	/**
	 * @return
	 */
	public String get_time() {
		if ( null == _environment)
			return _simulatorWindowTime;

		TimeCounter timeCounter = ( TimeCounter)_environment.getStepCounter();
		return timeCounter.getTime().toString();
	}

	/**
	 * @return
	 */
	private String get_actual_end_time() {
		String[] times = new String[] { "0", "00", "00"};
		String[] words = get_time().split( "/");
		times[ 0] = words[ 0];
		words = words[ 1].split( ":");
		times[ 1] = words[ 0];
		times[ 2] = words[ 1];
		return ( times[ 0] + "," + times[ 1] + "," + times[ 2]);
	}

	/**
	 * @param information
	 */
	public void update(String information) {
		_informationLabel.setText( information);
	}

	/**
	 * @param file
	 * @param id
	 * @param title
	 * @param visualShellTitle
	 */
	public boolean open(File file, String id, String title, String visualShellTitle) {
		if ( null == _logViewerFrame) {
			if ( !createStaticLogViewerFrame())
				return false;
		}

		_logViewerFrame.stop_simulation();
		stop_timer();

		cleanup();

		if ( !MessageDlg.execute( this, ResourceManager.get_instance().get( "application.title"), true, "on_file_open",
			ResourceManager.get_instance().get( "file.open.show.message"), new Object[] { file, id}, this, this)) {
			JOptionPane.showMessageDialog( this, ResourceManager.get_instance().get( "file.open.error.message"), ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
			close_all();
			initialize_user_interface();
			return false;
		}

		_logViewerFrame.toBack();

		update_user_interface( true, true,
			null != _currentFile,
			null != _currentFile,
			false/*true*/, false,
			_logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists(),
			exist_user_data_directory());

		_id = Long.parseLong( id);

		boolean modified = false;
		if ( _logViewerFrame.optimize_window_rectangle())
			modified = true;

		JInternalFrame[] internalFrames = _desktopPane.getAllFrames();
		for ( int i = 0; i < internalFrames.length; ++i) {
			if ( internalFrames[ i] instanceof LogViewerFrame)
				continue;

			if ( !( internalFrames[ i] instanceof ChartFrame))
				continue;

			ChartFrame chartFrame = ( ChartFrame)internalFrames[ i];
			if ( chartFrame.optimize_window_rectangle())
				modified = true;
		}

		FileManagerFrame fileManagerFrame = getFileManagerFrame();
		if ( null != fileManagerFrame && fileManagerFrame.optimize_window_rectangle())
			modified = true;
	
		_title = title;
		_visualShellTitle = visualShellTitle;

		modified( modified);

		set_sensor( true);

		return true;
	}

	/**
	 * @param enable
	 */
	private void set_sensor(boolean enable) {
		JInternalFrame[] internalFrames = _desktopPane.getAllFrames();
		for ( int i = 0; i < internalFrames.length; ++i) {
			if ( enable)
				enable_sensor( internalFrames[ i]);
			else {
				disable_sensor( internalFrames[ i]);
			}
		}
	}

	/**
	 * @param internalFrame
	 */
	private void enable_sensor(final JInternalFrame internalFrame) {
		internalFrame.addComponentListener( new ComponentListener() {
			public void componentHidden(ComponentEvent arg0) {
			}
			public void componentMoved(ComponentEvent arg0) {
				on_changed( internalFrame);
			}
			public void componentResized(ComponentEvent arg0) {
				on_changed( internalFrame);
			}
			public void componentShown(ComponentEvent arg0) {
			}
		});
	}

	/**
	 * @param internalFrame
	 */
	private void on_changed(JInternalFrame internalFrame) {
		if ( internalFrame.isMaximum() || internalFrame.isIcon()) {
			return;
		}

		Rectangle rectangle = ( Rectangle)_internalFrameRectangleMap.get( internalFrame);
		if ( null != rectangle && internalFrame.getBounds().equals( rectangle))
			return;

		modified( true);
	}

	/**
	 * @param internalFrame
	 */
	private void disable_sensor(JInternalFrame internalFrame) {
		ComponentListener[] componentListeners = internalFrame.getComponentListeners();
		for ( int j = 0; j < componentListeners.length; ++j)
			internalFrame.removeComponentListener( componentListeners[ j]);
	}

	/**
	 * SaxLoader
	 * @param filename
	 * @param simulatorWindowTime
	 * @param logViewerWindowTitle
	 * @param logViewerWindowRectangle
	 * @param agents
	 * @param spots
	 * @param chartDataMap
	 * @param fileManagerData
	 * @return
	 */
	public boolean load(String filename/*, String simulatorWindowTitle*/, String simulatorWindowTime, String logViewerWindowTitle, Rectangle logViewerWindowRectangle, List<LogData> agents, List<LogData> spots, Map<String, ChartData> chartDataMap, FileManagerData fileManagerData) {
		if ( null == logViewerWindowRectangle || null == agents || null == spots)
			return false;


		if ( !read( agents, new File( _rootDirectory, "agents")))
			return false;

		if ( !read( spots, new File( _rootDirectory, "spots")))
			return false;


		String console = read( new File( _rootDirectory, Constant._consoleFilename));
		String stdout = read( new File( _rootDirectory, Constant._standardOutFilename));
		String stderr = read( new File( _rootDirectory, Constant._standardErrorFilename));
		String graphic_properties = read( new File( _rootDirectory, Constant._graphicPropertiesFilename));
		String chart_properties = read( new File( _rootDirectory, Constant._chartPropertiesFilename));


		while ( close())
			;

		_internalFrameRectangleMap.clear();

		update( "[ " + simulatorWindowTime + " ]");

		if ( !_logViewerFrame.update( logViewerWindowTitle, logViewerWindowRectangle, console, agents, spots, stdout, stderr))
			return false;

		Collection<ChartData> chartDataList = chartDataMap.values();
			for ( ChartData chartData:chartDataList) {
				ChartFrame chartFrame = new ChartFrame( chartData._name, chartData._title, true, false, true, true);
				if ( !chartFrame.create( chartData, new File( _rootDirectory, Constant._chartLogDirectory))) {
//					_run_animator_menuItem.setEnabled( _logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists());
//					_run_animator_button.setEnabled( _logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists());
					return false;
				}

			_desktopPane.add( chartFrame);
		}

		if ( !createFileManagerFrame( fileManagerData))
			return false;

		_graphicProperties = graphic_properties;

		_chartProperties = chart_properties;

//		_run_animator_menuItem.setEnabled( _logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists());
//		_run_animator_button.setEnabled( _logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists());

//		_simulatorWindowTitle = simulatorWindowTitle;
//		setTitle( "SOARS Simulator - " + filename + " - " + _simulator_window_title);

		_simulatorWindowTime = simulatorWindowTime;

		_internalFrameRectangleMap.update( _desktopPane);

		return true;
	}

	/**
	 * @param list
	 * @param directory
	 * @return
	 */
	private boolean read(List<LogData> list, File directory) {
		for ( LogData logData:list) {
			String value = FileUtility.read_text_from_file( new File( directory, logData._name + ".log"), "UTF-8");
			if ( null == value)
				return false;

			logData._value = value;
		}
		return true;
	}

	/**
	 * @param file
	 * @return
	 */
	private String read(File file) {
		if ( !file.exists() || !file.isFile() || !file.canRead())
			return "";

		String value = FileUtility.read_text_from_file( file, "UTF-8");
		if ( null == value)
			return "";

		return value;
	}

	/**
	 * @param fileManagerData
	 * @return
	 */
	private boolean createFileManagerFrame(FileManagerData fileManagerData) {
		if ( null == fileManagerData)
			return true;

		if ( null != fileManagerData && !exist_user_data_directory())
			return false;

		FileManagerFrame fileManagerFrame = getFileManagerFrame();
		if ( null != fileManagerFrame)
			return false;

		fileManagerFrame = new FileManagerFrame( ResourceManager.get_instance().get( "file.manager.title"), true, true, true, true);
		if ( !fileManagerFrame.create( fileManagerData))
			return false;

		_desktopPane.add( fileManagerFrame);

		return true;
	}

	/**
	 * @return
	 */
	private FileManagerFrame getFileManagerFrame() {
		JInternalFrame[] internalFrames = _desktopPane.getAllFrames();
		for ( int i = 0; i < internalFrames.length; ++i) {
			if ( internalFrames[ i] instanceof FileManagerFrame)
				return ( FileManagerFrame)internalFrames[ i];
		}
		return null;
	}

	/**
	 * @return
	 */
	private boolean close() {
		JInternalFrame[] internalFrames = _desktopPane.getAllFrames();
		for ( int i = 0; i < internalFrames.length; ++i) {
			if ( internalFrames[ i] instanceof LogViewerFrame) {
				disable_sensor( internalFrames[ i]);
				continue;
			}

			internalFrames[ i].dispose();
			return true;
		}

		return false;
	}

	/**
	 * 
	 */
	private void close_all() {
		if ( null != _logViewerFrame)
			_logViewerFrame.stop_simulation();

		stop_timer();

		JInternalFrame[] internalFrames = _desktopPane.getAllFrames();
		for ( int i = 0; i < internalFrames.length; ++i)
			internalFrames[ i].dispose();

		_logViewerFrame = null;

//		_simulatorWindowTitle = "";
		_internalFrameRectangleMap.clear();
		modified( false);

		cleanup();
	}

	/**
	 * @return
	 */
	private int confirm1() {
		int result = JOptionPane.showConfirmDialog(
			this,
			ResourceManager.get_instance().get( "file.close.confirm.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_CANCEL_OPTION);
		if ( Application._demo)
			return result;

		switch ( result) {
			case JOptionPane.YES_OPTION:
				if ( 0 <= _id) {
					int state = on_file_save();
					switch ( state) {
						case _error:
							if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
								this,
								ResourceManager.get_instance().get( "file.exit.confirm.message"),
								ResourceManager.get_instance().get( "application.title"),
								JOptionPane.YES_NO_OPTION))
								result = JOptionPane.CANCEL_OPTION;
							break;
						case _cancel:
							result = JOptionPane.CANCEL_OPTION;
							break;
					}
				} else {
					int state = on_file_save_as();
					switch ( state) {
					case _error:
						if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
							this,
							ResourceManager.get_instance().get( "file.exit.confirm.message"),
							ResourceManager.get_instance().get( "application.title"),
							JOptionPane.YES_NO_OPTION))
							result = JOptionPane.CANCEL_OPTION;
						break;
					case _cancel:
						result = JOptionPane.CANCEL_OPTION;
						break;
					}
				}
				break;
			case JOptionPane.CANCEL_OPTION:
			case JOptionPane.CLOSED_OPTION:
				requestFocus();
				break;
		}
		return result;
	}

	/**
	 * @return
	 */
	private int confirm2() {
		int result = JOptionPane.showConfirmDialog(
			this,
			ResourceManager.get_instance().get( "file.exit.confirm.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION);
		if ( Application._demo)
			return result;

		switch ( result) {
			case JOptionPane.YES_OPTION:
				int state = on_file_save_property();
				switch ( state) {
					case _error:
						return JOptionPane.showConfirmDialog(
							this,
							ResourceManager.get_instance().get( "file.exit.confirm.message"),
							ResourceManager.get_instance().get( "application.title"),
							JOptionPane.YES_NO_OPTION);
					case _cancel:
						result = JOptionPane.CANCEL_OPTION;
						break;
				}
				break;
			case JOptionPane.CLOSED_OPTION:
				requestFocus();
				break;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.file.IFileMenuHandler#on_file_exit(java.awt.event.ActionEvent)
	 */
	public void on_file_exit(ActionEvent actionEvent) {
		if ( _modified) {
			int result = confirm1();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return;
		} else {
			int result = confirm2();
			if ( JOptionPane.YES_OPTION != result/* && JOptionPane.NO_OPTION != result*/)
				return;
//			if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
//				this,
//				ResourceManager.get_instance().get( "file.exit.confirm.message"),
//				ResourceManager.get_instance().get( "application.title"),
//				JOptionPane.YES_NO_OPTION)) {
//				requestFocus();
//				return;
//			}
		}

		_windowRectangle = getBounds();
		try {
			set_property_to_environment_file();
		}
		catch (IOException e) {
			throw new RuntimeException( e);
		}

		stop_timer();

		if ( null != _logViewerFrame)
			_logViewerFrame.stop_simulation();

		cleanup();

		Application.get_instance().exit_instance();
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.file.IFileMenuHandler#on_file_new(java.awt.event.ActionEvent)
	 */
	public void on_file_new(ActionEvent actionEvent) {
//		if ( _modified) {
//			int result = confirm();
//			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
//				return;
//		}
//
//		close_all();
//		initialize_user_interface();
		requestFocus();
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.file.IFileMenuHandler#on_file_open(java.awt.event.ActionEvent)
	 */
	public void on_file_open(ActionEvent actionEvent) {
//		if ( _modified) {
//			int result = confirm();
//			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
//				return;
//		}
//
//		File file = CommonTool.get_open_file(
//			Environment._openDirectoryKey,
//			ResourceManager.get_instance().get( "file.open.dialog"),
//			new String[] { Constant._soarsExtension/*"sim", "sml"*/},
//			"soars data"/*"soars simulator data"*/,
//			this);
//
		requestFocus();
//
//		if ( null == file)
//			return;
//
//		open( file);
	}

	/**
	 * @param file
	 */
	private void on_file_open_by_drag_and_drop(File file) {
//		if ( _modified) {
//			int result = confirm();
//			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
//				return;
//		}
//
		requestFocus();
//
//		if ( null == file)
//			return;
//
//		open( file);
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.file.IFileMenuHandler#on_file_close(java.awt.event.ActionEvent)
	 */
	public void on_file_close(ActionEvent actionEvent) {
//		if ( _modified) {
//			int result = confirm();
//			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
//				return;
//		}
//
//		close_all();
//		initialize_user_interface();
		requestFocus();
	}

	/**
	 * @return
	 */
	private int on_file_save_property() {
		if ( null == _currentFile) {
			JOptionPane.showMessageDialog( this, ResourceManager.get_instance().get( "file.save.property.error.message"), ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
			return _error;
		}

		TreeMap<String, Property> propertyMap = new TreeMap<String, Property>();
		int[] error = new int[ 1];
		Property property = get_property( propertyMap, error);
		if ( null == property) {
			if ( 0 <= error[ 0])
				return _cancel;
			else {
				JOptionPane.showMessageDialog( this, ResourceManager.get_instance().get( "file.save.property.error.message"), ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
				return _error;
			}
		}

		if ( 0 < error[ 0]) {
			modified( false);
			return _success;
		}

		if ( !MessageDlg.execute( this, ResourceManager.get_instance().get( "application.title"), true,
			"on_save_property", ResourceManager.get_instance().get( "file.save.property.show.message"),
			new Object[] { _currentFile, property, propertyMap}, this, this)) {
			JOptionPane.showMessageDialog( this, ResourceManager.get_instance().get( "file.save.property.error.message"), ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
			return _error;
		}

		_internalFrameRectangleMap.update( _desktopPane);

		_title = property._title;

		modified( false);

		return _success;
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.file.IFileMenuHandler#on_file_save(java.awt.event.ActionEvent)
	 */
	public void on_file_save(ActionEvent actionEvent) {
		on_file_save();
		requestFocus();
	}
	
	/**
	 * @return
	 */
	public int on_file_save() {
		if ( null == _currentFile) {
			JOptionPane.showMessageDialog( this, ResourceManager.get_instance().get( "file.save.error.message"), ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
			return _error;
		}

		TreeMap<String, Property> propertyMap = new TreeMap<String, Property>();
		int[] error = new int[ 1];
		Property property = get_property( propertyMap, error);
		if ( null == property) {
			if ( 0 <= error[ 0])
				return _cancel;
			else {
				JOptionPane.showMessageDialog( this, ResourceManager.get_instance().get( "file.save.error.message"), ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
				return _error;
			}
		}

		if ( !MessageDlg.execute( this, ResourceManager.get_instance().get( "application.title"), true,
			"on_file_save_as", ResourceManager.get_instance().get( "file.save.show.message"),
			new Object[] { _currentFile, property, propertyMap}, this, this)) {
			JOptionPane.showMessageDialog( this, ResourceManager.get_instance().get( "file.save.error.message"), ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
			return _error;
		}

		_internalFrameRectangleMap.update( _desktopPane);

		_title = property._title;

		modified( false);

		return _success;
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.file.IFileMenuHandler#on_file_save_as(java.awt.event.ActionEvent)
	 */
	public void on_file_save_as(ActionEvent actionEvent) {
		on_file_save_as();
	}

	/**
	 * @return
	 */
	private int on_file_save_as() {
		if ( null == _currentFile) {
			JOptionPane.showMessageDialog( this, ResourceManager.get_instance().get( "file.save.as.error.message"), ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
			return _error;
		}

		TreeMap<String, Property> propertyMap = new TreeMap<String, Property>();
		int[] error = new int[ 1];
		Property property = get_property( propertyMap, error);
		if ( null == property) {
			if ( 0 <= error[ 0])
				return _cancel;
			else {
				JOptionPane.showMessageDialog( this, ResourceManager.get_instance().get( "file.save.as.error.message"), ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
				return _error;
			}
		}

		if ( !MessageDlg.execute( this, ResourceManager.get_instance().get( "application.title"), true,
			"on_file_save_as", ResourceManager.get_instance().get( "file.save.as.show.message"),
			new Object[] { _currentFile, property, propertyMap}, this, this)) {
			JOptionPane.showMessageDialog( this, ResourceManager.get_instance().get( "file.save.as.error.message"), ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
			return _error;
		}

		_internalFrameRectangleMap.update( _desktopPane);

		_title = property._title;

		modified( false);

		// メニュー更新
		update_user_interface( false, false, false, true, false, false, _logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists(), exist_user_data_directory());

		return _success;
//		File file = CommonTool.get_save_file(
//			Environment._saveAsDirectoryKey,
//			ResourceManager.get_instance().get( "file.save.as.dialog"),
//			new String[] { Constant._soarsExtension/*"sim"*/},
//			"soars data"/*"soars simulator data"*/,
//			this);
//
//		requestFocus();
//
//		if ( null == file)
//			return false;
//
//		String absolute_name = file.getAbsolutePath();
//		String name = file.getName();
//		int index = name.lastIndexOf( '.');
//		if ( -1 == index)
//			file = new File( absolute_name + "." + Constant._soarsExtension/*".sim"*/);
//		else if ( name.length() - 1 == index)
//			file = new File( absolute_name + Constant._soarsExtension/*"sim"*/);
//
//		if ( !MessageDlg.execute( this, ResourceManager.get_instance().get( "application.title"), true,
//			"on_file_save_as", ResourceManager.get_instance().get( "file.save.as.show.message"),
//			new Object[] { file}, this, this)) {
//			JOptionPane.showMessageDialog( this, ResourceManager.get_instance().get( "file.save.as.error.message"), ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
//			return false;
//		}
//
//		_internalFrameRectangleMap.update( _desktopPane);
//
//		update_user_interface( true, true, true, true, true, false, _logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists(), exist_user_data_directory());
//
//		return true;
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

		Property property = null;
		if ( 0 > _id) {
			long id = System.currentTimeMillis();
			property = new Property( _currentFile, "simulation", String.valueOf( id), _title, "");
		} else {
			property = propertyMap.get( String.valueOf( _id));
			if ( null == property) {
				error[ 0] = -1;
				return null;
			}
		}

		// TODO 2014.3.12 ApplicationBuilderからの起動なら、Animator起動時にタイトルとコメントを入力させない
		if ( Application._demo)
			error[ 0] = 1;
		else {
			// ここでダイアログボックスを開いてタイトルとコメントを入力させる
			EditPropertyDlg editPropertyDlg = new EditPropertyDlg( this, true, property);
			if ( !editPropertyDlg.do_modal( this))
				return null;

			if ( !editPropertyDlg._changed)
				error[ 0] = 1;
		}
//		// ここでダイアログボックスを開いてタイトルとコメントを入力させる
//		EditPropertyDlg editPropertyDlg = new EditPropertyDlg( this, true, property);
//		if ( !editPropertyDlg.do_modal( this))
//			return null;
//
//		if ( !editPropertyDlg._changed)
//			error[ 0] = 1;

		propertyMap.put( property._id, property);

		return property;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(Writer writer) throws SAXException {
		if ( null == _logViewerFrame)
			return false;

		if ( !_logViewerFrame.write( _rootDirectory, writer))
			return false;

		if ( !write_chart_data( writer))
			return false;

		if ( !write_file_manager_data( writer))
			return false;

		if ( !write_graphic_properties( writer))
			return false;

		if ( !write_chart_properties( writer))
			return false;

		return true;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 * @throws NumberFormatException
	 */
	private boolean write_chart_data(Writer writer) throws NumberFormatException, SAXException {
		String[] lines = _chartProperties.split( "\n");
		if ( null == lines)
			return false;

		if ( 7 > lines.length)
			return true;

		for ( int i = 6; i < lines.length; ++i) {
			if ( !write_chart_data( lines[ i], writer))
				return false;
		}

		return true;
	}

	/**
	 * @param line
	 * @param writer
	 * @return
	 * @throws SAXException
	 * @throws NumberFormatException
	 */
	private boolean write_chart_data(String line, Writer writer) throws NumberFormatException, SAXException {
		String[] words = line.split( "\t");
		if ( null == words || 2 > words.length)
			return false;

		JInternalFrame[] internalFrames = _desktopPane.getAllFrames();
		for ( int i = 0; i < internalFrames.length; ++i) {
			if ( internalFrames[ i] instanceof LogViewerFrame)
				continue;

			if ( internalFrames[ i] instanceof InternalChartFrame) {
				InternalChartFrame internalChartFrame = ( InternalChartFrame)internalFrames[ i];
				if ( !internalChartFrame._name.equals( words[ 0]))
					continue;

				File chart_log_directory = new File( _rootDirectory, Constant._chartLogDirectory);
				if ( !chart_log_directory.exists() && !chart_log_directory.mkdir())
					return false;

				if ( !internalChartFrame.write( Integer.parseInt( words[ 1]),
					new File( chart_log_directory, words[ 0] + "_" + words[ 1] + ".log"),
					words[ 11].equals( "true"), writer))
					return false;
			} else if ( internalFrames[ i] instanceof ChartFrame) {
				ChartFrame chartFrame = ( ChartFrame)internalFrames[ i];
				if ( !chartFrame._name.equals( words[ 0]))
					continue;

				File chart_log_directory = new File( _rootDirectory, Constant._chartLogDirectory);
				if ( !chart_log_directory.exists() && !chart_log_directory.mkdir())
					return false;

				if ( !chartFrame.write( Integer.parseInt( words[ 1]),
					new File( chart_log_directory, words[ 0] + "_" + words[ 1] + ".log"),
					words[ 11].equals( "true"), writer))
					return false;
			}
		}

		return true;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_file_manager_data(Writer writer) throws SAXException {
		if ( !exist_user_data_directory())
			return true;

		FileManagerFrame fileManagerFrame = getFileManagerFrame();
		if ( null == fileManagerFrame)
			return true;

		return fileManagerFrame.write( writer);
	}

	/**
	 * @param writer
	 * @return
	 */
	private boolean write_graphic_properties(Writer writer) {
		if ( _graphicProperties.equals( ""))
			return true;

		File file = new File( _rootDirectory, Constant._graphicPropertiesFilename);
		if ( file.exists())
			return true;

		return FileUtility.write_text_to_file( file, _graphicProperties, "UTF-8");
	}

	/**
	 * @param writer
	 * @return
	 */
	private boolean write_chart_properties(Writer writer) {
		if ( _chartProperties.equals( ""))
			return true;

		File file = new File( _rootDirectory, Constant._chartPropertiesFilename);
		if ( file.exists())
			return true;

		return FileUtility.write_text_to_file( file, _chartProperties, "UTF-8");
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.file.IFileMenuHandler#on_file_save_image_as(java.awt.event.ActionEvent)
	 */
	public void on_file_save_image_as(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.simulation.ISimulationMenuHandler#on_simulation_stop(java.awt.event.ActionEvent)
	 */
	public void on_simulation_stop(ActionEvent actionEvent) {
		if ( null == _logViewerFrame)
			return;

		_logViewerFrame.stop_simulation();
		stop_timer();

		update_user_interface( true, true, false, false, true, false, _logViewerFrame.terminated_normally() && _logViewerFrame.dollar_spot_log_exists(), exist_user_data_directory());
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.run.IRunMenuHandler#on_run_animator(java.awt.event.ActionEvent)
	 */
	public void on_run_animator(ActionEvent actionEvent) {
		if ( null == _logViewerFrame) {
			JOptionPane.showMessageDialog( this, ResourceManager.get_instance().get( "run.animator.error.message"), ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		if ( null == _currentFile) {
			JOptionPane.showMessageDialog( this, ResourceManager.get_instance().get( "run.animator.error.message"), ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		TreeMap<String, Property> propertyMap = new TreeMap<String, Property>();
		int[] error = new int[ 1];
		Property property = get_property( propertyMap, error);
		if ( null == property) {
			if ( 0 > error[ 0])
				JOptionPane.showMessageDialog( this, ResourceManager.get_instance().get( "run.animator.error.message"), ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		if ( !MessageDlg.execute( this, ResourceManager.get_instance().get( "application.title"), true, "on_run_animator",
			ResourceManager.get_instance().get( "run.animator.show.message"), new Object[] { _currentFile, property, propertyMap}, this, this)) {
			JOptionPane.showMessageDialog( this, ResourceManager.get_instance().get( "run.animator.error.message"), ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		// TODO 2014.3.12 ApplicationBuilderからの起動なら、Animator起動後に最初の状態に戻してなにもしない
		if ( Application._demo)
			_id = -1;
		else {
			_title = property._title;

			modified( _modified);

			// メニュー更新
			enable_menuItem( _fileSaveMenuItem, true);
			enable_button( _fileSaveButton, true);

			enable_menuItem( _fileSaveAsMenuItem, false);
			enable_button( _fileSaveAsButton, false);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.run.IRunMenuHandler#on_run_file_manager(java.awt.event.ActionEvent)
	 */
	public void on_run_file_manager(ActionEvent actionEvent) {
		if ( !exist_user_data_directory())
			return;

		FileManagerFrame fileManagerFrame = getFileManagerFrame();
		if ( null != fileManagerFrame) {
			if ( fileManagerFrame.isVisible())
				fileManagerFrame.setVisible( false);
			else {
				fileManagerFrame.setVisible( true);
				fileManagerFrame.toFront();
			}
		} else {
			fileManagerFrame = new FileManagerFrame( ResourceManager.get_instance().get( "file.manager.title"), true, true, true, true);
			if ( !fileManagerFrame.create())
				return;

			if ( !append( fileManagerFrame))
					return;

//			_desktopPane.add( fileManagerFrame);
//
//			fileManagerFrame.toFront();
		}

		modified( true);
	}

	/**
	 * @param root_directory
	 * @return
	 */
	public boolean setup_chart_data_files(File root_directory) {
		String[] lines = _chartProperties.split( "\n");
		if ( null == lines)
			return false;

		if ( 7 > lines.length)
			return true;

		for ( int i = 6; i < lines.length; ++i) {
			if ( !setup_chart_data_file( lines[ i], root_directory))
				return false;
		}

		return true;
	}

	/**
	 * @param line
	 * @param root_directory
	 * @return
	 */
	private boolean setup_chart_data_file(String line, File root_directory) {
		String[] words = line.split( "\t");
		if ( null == words || 2 > words.length)
			return false;

		JInternalFrame[] internalFrames = _desktopPane.getAllFrames();
		for ( int i = 0; i < internalFrames.length; ++i) {
			if ( internalFrames[ i] instanceof LogViewerFrame)
				continue;

			if ( internalFrames[ i] instanceof InternalChartFrame) {
				InternalChartFrame internalChartFrame = ( InternalChartFrame)internalFrames[ i];
				if ( !internalChartFrame._name.equals( words[ 0]))
					continue;

				File chart_log_directory = new File( root_directory, Constant._chartLogDirectory);
				if ( !chart_log_directory.exists() && !chart_log_directory.mkdir())
					return false;

				if ( !internalChartFrame.write( Integer.parseInt( words[ 1]),
					new File( chart_log_directory, words[ 0] + "_" + words[ 1] + ".log")))
					return false;
			} else if ( internalFrames[ i] instanceof ChartFrame) {
				ChartFrame chartFrame = ( ChartFrame)internalFrames[ i];
				if ( !chartFrame._name.equals( words[ 0]))
					continue;

				File chart_log_directory = new File( root_directory, Constant._chartLogDirectory);
				if ( !chart_log_directory.exists() && !chart_log_directory.mkdir())
					return false;

				if ( !chartFrame.write( Integer.parseInt( words[ 1]),
					new File( chart_log_directory, words[ 0] + "_" + words[ 1] + ".log")))
					return false;
			}
		}

		return true;
	}

	/**
	 * @param root_directory
	 * @return
	 */
	public boolean setup_image_files(File root_directory) {
		if ( !exist_image_directory())
			return true;

		return FileUtility.copy_all( get_image_directory(), new File( root_directory, Constant._imageDirectory));
	}

	/**
	 * @param root_directory
	 * @return
	 */
	public boolean setup_thumbnail_image_files(File root_directory) {
		if ( !exist_thumbnail_image_directory())
			return true;

		return FileUtility.copy_all( get_thumbnail_image_directory(), new File( root_directory, Constant._thumbnailImageDirectory));
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.help.IHelpMenuHandler#on_help_contents(java.awt.event.ActionEvent)
	 */
	public void on_help_contents(ActionEvent actionEvent) {
		String currentDirectoryName = System.getProperty( Constant._soarsHome);
		if ( null == currentDirectoryName)
			return;

		File file = new File( currentDirectoryName + "/"
			+ ResourceManager.get_instance().get( "help.contents.url"));
		if ( !file.exists() || !file.canRead())
			return;

		try {
			BrowserLauncher.openURL( file.toURI().toURL().toString().replaceAll( "\\\\", "/"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		//BrowserLauncher.openURL( "file:///" + file.getAbsolutePath().replaceAll( "\\\\", "/"));
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.help.IHelpMenuHandler#on_help_forum(java.awt.event.ActionEvent)
	 */
	public void on_help_forum(ActionEvent actionEvent) {
		String language = Locale.getDefault().getLanguage();
		if ( null == language)
			return;

		String url = SoarsCommonEnvironment.get_instance().getProperty(
			language.equals( "ja") ? SoarsCommonEnvironment._forumJaUrlKey : SoarsCommonEnvironment._forumEnUrlKey);
		if ( null == url)
			return;

		BrowserLauncher.openURL( url);
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.menu.help.IHelpMenuHandler#on_help_about(java.awt.event.ActionEvent)
	 */
	public void on_help_about(ActionEvent actionEvent) {
		JOptionPane.showMessageDialog( this,
			Constant.get_version_message(),
			ResourceManager.get_instance().get( "application.title"),
//				JOptionPane.INFORMATION_MESSAGE,
//				new ImageIcon( Resource.load_image_from_resource( Constant._resource_directory + "/image/picture/picture.jpg", getClass())));
			JOptionPane.INFORMATION_MESSAGE);

		requestFocus();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IMessageCallback#message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.MessageDlg)
	 */
	public boolean message_callback(String id, Object[] objects, MessageDlg messageDlg) {
		if ( id.equals( "on_file_open"))
			return load( ( File)objects[ 0], ( String)objects[ 1]);
		else if ( id.equals( "on_file_save_as"))
			return write1( ( File)objects[ 0], ( ( Property)objects[ 1])._id, ( TreeMap<String, Property>)objects[ 2]);
		else if ( id.equals( "on_save_property"))
			return write2( ( File)objects[ 0], ( ( Property)objects[ 1])._id, ( TreeMap<String, Property>)objects[ 2]);
		else if ( id.equals( "on_run_animator"))
			return run_animator( ( File)objects[ 0], ( Property)objects[ 1], ( TreeMap<String, Property>)objects[ 2]);

		return true;
	}

	/**
	 * Returns true if loading the data from the specified file successfully.
	 * @param file the specified file
	 * @param id the simulation ID
	 * @return true if loading the data from the specified file successfully
	 */
	private boolean load(File file, String id) {
		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return false;

		if ( !decompress( file, id, parentDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		// ここで画像ファイルディレクトリを解凍
		File rootDirectory = new File( parentDirectory, Constant._simulatorRootDirectoryName);
		if ( !rootDirectory.exists() || !rootDirectory.isDirectory()) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		if ( !decompress_imagefiles( rootDirectory, file)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		cleanup();

		_parentDirectory = parentDirectory;
		_rootDirectory = rootDirectory;

		if ( !SaxLoader.execute( new File( _rootDirectory, Constant._simulatorDataFilename))) {
			cleanup();
			return false;
		}

		_currentFile = file;

		return true;
	}

	/**
	 * @param file
	 * @param id
	 * @param parentDirectory
	 * @return
	 */
	private boolean decompress(File file, String id, File parentDirectory) {
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
					if ( !zipEntry.getName().equals( Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/" + id + ".zip")) {
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
	 * @return
	 */
	private boolean decompress_imagefiles(File rootDirectory, File file) {
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
					if ( !ZipUtility.decompress( zipInputStream, rootDirectory, this, 0)) {
						return false;
					}
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

	/* (non-Javadoc)
	 * @see soars.common.utility.tool.file.ZipDecompressHandler#get_new_filename(int, java.lang.String)
	 */
	public String get_new_filename(int id, String filename) {
		String[][] filenames = new String[][] {
			{ Constant._visualShellRootDirectoryName + "/" + Constant._imageDirectory, Constant._imageDirectory},
			{ Constant._visualShellRootDirectoryName + "/" + Constant._thumbnailImageDirectory, Constant._thumbnailImageDirectory}
		};
		for ( int i = 0; i < filenames.length; ++i) {
			if ( filename.startsWith( filenames[ i][ 0]))
				return filenames[ i][ 1] + filename.substring( filenames[ i][ 0].length());
		}
		return null;
	}

	/**
	 * @param file
	 * @param property
	 * @param propertyMap
	 * @return
	 */
	private boolean run_animator(File file, Property property, TreeMap<String, Property> propertyMap) {
		// TODO 2014.3.12 ApplicationBuilderからの起動なら、Animator起動時にSimulatorのデータを保存しない
		if ( 0 <= _id) {
			if ( !Application._demo) {
				if ( !property.update_simulation_properties( propertyMap))
					return false;
			}
		} else {
			if ( Application._demo) {
				if ( 0 > _id)
					_id = Long.parseLong( property._id);
			} else {
				if ( !write1( file, property._id, propertyMap))
					return false;
			}
		}
//		if ( 0 <= _id) {
//			if ( !property.update_simulation_properties( propertyMap))
//				return false;
//		} else {
//			if ( !write1( file, property._id, propertyMap))
//				return false;
//		}

		return _logViewerFrame.run_animator( _graphicProperties, get_actual_end_time() + "\n" + _chartProperties, _currentFile, _id, property._title, _visualShellTitle);
	}

	/**
	 * @param file
	 * @param id
	 * @param propertyMap
	 * @return
	 */
	private boolean write1(File file, String id, TreeMap<String, Property> propertyMap) {
		if ( file.exists() && ( !file.isFile() || !file.canRead() || !file.canWrite()))
			return false;

		if ( !setup_work_directory())
			return false;

		if ( !SaxWriter.execute( new File( _rootDirectory, Constant._simulatorDataFilename)))
			return false;

		// images、thumbnailsとguiは保存しない
		List<String> exclusionFolderNames = new ArrayList<String>();
		exclusionFolderNames.add( Constant._simulatorRootDirectoryName + "/" + Constant._imageDirectory);
		exclusionFolderNames.add( Constant._simulatorRootDirectoryName + "/" + Constant._thumbnailImageDirectory);
		exclusionFolderNames.add( Constant._simulatorRootDirectoryName + Constant._userGuiInternalRelativePathName);

		if ( !file.exists() || !ZipUtility.find( file, Constant._soarsRootDirectoryName + "/")) {
			// ファイルが存在しないかまたは存在していてもSOARSデータファイルでない場合
			return false;
//			if ( null == _currentFile) {
//				// 新たに作成する
//				File parentDirectory = SoarsCommonTool.make_parent_directory();
//				if ( null == parentDirectory)
//					return false;
//
//				File rootDirectory = new File( parentDirectory, Constant._soarsRootDirectoryName);
//				if ( !rootDirectory.mkdirs()) {
//					FileUtility.delete( parentDirectory, true);
//					return false;
//				}
//
//				File simulatorDirectory = new File( rootDirectory, Constant._simulatorRootDirectoryName);
//				if ( !simulatorDirectory.mkdirs()) {
//					FileUtility.delete( parentDirectory, true);
//					return false;
//				}
//
//				// imagesとthumbnailsは保存しない
//				if ( !ZipUtility.compress( new File( simulatorDirectory, id + ".zip"), _rootDirectory, _parentDirectory, exclusionFolderNames)) {
//					FileUtility.delete( parentDirectory, true);
//					return false;
//				}
//
//				if ( !ZipUtility.compress( file, rootDirectory, parentDirectory)) {
//					FileUtility.delete( parentDirectory, true);
//					return false;
//				}
//
//				FileUtility.delete( parentDirectory, true);
//			} else {
//				// もし_currentFileが存在していたら_currentFileからコピーして更新する
//				FileUtility.copy( _currentFile, file);
//				if ( null == update( file, id, exclusionFolderNames))
//					return false;
//			}
		} else {
			// ファイルが存在していれば更新する
			if ( null == update( file, id, propertyMap, exclusionFolderNames))
				return false;
		}

		_currentFile = new File( file.getAbsolutePath());
		modified( false);

		if ( 0 > _id)
			_id = Long.parseLong( id);

		return true;
	}

	/**
	 * @param file
	 * @param id
	 * @param propertyMap
	 * @param exclusionFolderNames
	 * @return
	 */
	private File update(File file, String id, TreeMap<String, Property> propertyMap, List<String> exclusionFolderNames) {
		File tempZipFile;
		try {
			tempZipFile = File.createTempFile( id, ".zip");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		tempZipFile.deleteOnExit();

		// imagesとthumbnailsは保存しない
		if ( !ZipUtility.compress( tempZipFile, _rootDirectory, _parentDirectory, exclusionFolderNames)) {
			tempZipFile.delete();
			return null;
		}

		File tempPropertyFile;
		try {
			tempPropertyFile = File.createTempFile( "simulation", ".xml");
		} catch (IOException e) {
			e.printStackTrace();
			tempZipFile.delete();
			return null;
		}

		tempPropertyFile.deleteOnExit();

		if ( !Property.write_simulation_properties( propertyMap, tempPropertyFile)) {
			tempZipFile.delete();
			tempPropertyFile.delete();
			return null;
		}

		Map<String, List<Entry>> entryMap = new HashMap<String, List<Entry>>();
		List<Entry> entryList = new ArrayList<Entry>();

		Map<String, File> fileMap = new HashMap<String, File>();

		if ( !ZipUtility.find( file, Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/")) {
			// soars/simulatorが含まれていない場合
			// soars/simulatorを追加
			entryList.add( new Entry( Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/", null));

			// soars/simulatorにproperty.xmlを追加
			entryList.add( new Entry( Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/" + Constant._propertyFileName, tempPropertyFile));

			// soars/simulatorに圧縮ファイルを追加
			entryList.add( new Entry( Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/" + id + ".zip", tempZipFile));

			entryMap.put( Constant._soarsRootDirectoryName + "/", entryList);

//			file = ZipUtility.append( file, entryMap);
		} else {
			// soars/simulatorが含まれている場合
//			List<Entry> entryList = new ArrayList<Entry>();
//			Map<String, File> fileMap = new HashMap<String, File>();

			if ( !ZipUtility.find( file, Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/" + Constant._propertyFileName)) {
				// soars/simulator/property.xmlが含まれていなければ追加
				entryList.add( new Entry( Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/" + Constant._propertyFileName, tempPropertyFile));
			} else {
				// soars/simulator/property.xmlが含まれていれば更新
				fileMap.put( Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/" + Constant._propertyFileName, tempPropertyFile);
			}

			if ( !ZipUtility.find( file, Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/" + id + ".zip")) {
				// soars/simulatorに圧縮ファイルが含まれていなければ追加
				entryList.add( new Entry( Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/" + id + ".zip", tempZipFile));
			} else {
				// soars/simulatorに圧縮ファイルが含まれていれば更新
				fileMap.put( Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/" + id + ".zip", tempZipFile);
			}

//			Map<String, List<Entry>> entryMap = new HashMap<String, List<Entry>>();
			entryMap.put( Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/", entryList);
		}

		file = ZipUtility.update( file, entryMap, fileMap);

		tempZipFile.delete();
		tempPropertyFile.delete();

		return file;
	}

	/**
	 * @param file
	 * @param id
	 * @param propertyMap
	 * @return
	 */
	private boolean write2(File file, String id, TreeMap<String, Property> propertyMap) {
		if ( file.exists() && ( !file.isFile() || !file.canRead() || !file.canWrite()))
			return false;

		if ( !file.exists() || !ZipUtility.find( file, Constant._soarsRootDirectoryName + "/"))
			// ファイルが存在しないかまたは存在していてもSOARSデータファイルでない場合
			return false;
		else {
			// ファイルが存在していれば更新する
			if ( null == update( file, id, propertyMap))
				return false;
		}

		_currentFile = new File( file.getAbsolutePath());
		modified( false);

		return true;
	}

	/**
	 * @param file
	 * @param id
	 * @param propertyMap
	 * @return
	 */
	private File update(File file, String id, TreeMap<String, Property> propertyMap) {
		File tempPropertyFile;
		try {
			tempPropertyFile = File.createTempFile( "simulation", ".xml");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		tempPropertyFile.deleteOnExit();

		if ( !Property.write_simulation_properties( propertyMap, tempPropertyFile)) {
			tempPropertyFile.delete();
			return null;
		}

		Map<String, List<Entry>> entryMap = new HashMap<String, List<Entry>>();
		List<Entry> entryList = new ArrayList<Entry>();

		Map<String, File> fileMap = new HashMap<String, File>();

		if ( !ZipUtility.find( file, Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/")) {
			// soars/simulatorが含まれていない場合→これは起こり得ない筈だが念の為
			// soars/simulatorを追加
			entryList.add( new Entry( Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/", null));

			// soars/simulatorにproperty.xmlを追加
			entryList.add( new Entry( Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/" + Constant._propertyFileName, tempPropertyFile));

			entryMap.put( Constant._soarsRootDirectoryName + "/", entryList);
		} else {
			// soars/simulatorが含まれている場合
			if ( !ZipUtility.find( file, Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/" + Constant._propertyFileName)) {
				// soars/simulator/property.xmlが含まれていなければ追加
				entryList.add( new Entry( Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/" + Constant._propertyFileName, tempPropertyFile));
			} else {
				// soars/simulator/property.xmlが含まれていれば更新
				fileMap.put( Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/" + Constant._propertyFileName, tempPropertyFile);
			}

			entryMap.put( Constant._soarsRootDirectoryName + "/" + Constant._simulatorRootDirectoryName + "/", entryList);
		}

		file = ZipUtility.update( file, entryMap, fileMap);

		tempPropertyFile.delete();

		return file;
	}

//	/* (non-Javadoc)
//	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
//	 */
//	public void dragEnter(DropTargetDragEvent arg0) {
//		arg0.acceptDrag( DnDConstants.ACTION_COPY);
//	}
//
//	/* (non-Javadoc)
//	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
//	 */
//	public void dragOver(DropTargetDragEvent arg0) {
//		arg0.acceptDrag( DnDConstants.ACTION_COPY_OR_MOVE);
//	}
//
//	/* (non-Javadoc)
//	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
//	 */
//	public void dropActionChanged(DropTargetDragEvent arg0) {
//		arg0.acceptDrag( DnDConstants.ACTION_COPY);
//	}
//
//	/* (non-Javadoc)
//	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
//	 */
//	public void drop(DropTargetDropEvent arg0) {
//		if ( !_fileOpenMenuItem.isEnabled() || !_fileOpenButton.isEnabled()) {
//			arg0.rejectDrop();
//			return;
//		}
//
//		try {
//			Transferable transferable = arg0.getTransferable();
//			if ( transferable.isDataFlavorSupported( DataFlavor.javaFileListFlavor)) {
//				arg0.acceptDrop( DnDConstants.ACTION_COPY_OR_MOVE);
//				List list = ( List)transferable.getTransferData( DataFlavor.javaFileListFlavor);
//				if ( list.isEmpty()) {
//					arg0.getDropTargetContext().dropComplete( true);
//					return;
//				}
//
//				File file =( File)list.get( 0);
//				arg0.getDropTargetContext().dropComplete( true);
//				requestFocus();
//				on_file_open_by_drag_and_drop( file);
//			} else if ( transferable.isDataFlavorSupported( DataFlavor.stringFlavor)) {
//				arg0.acceptDrop( DnDConstants.ACTION_COPY_OR_MOVE);
//				String string = ( String)transferable.getTransferData( DataFlavor.stringFlavor);
//				arg0.getDropTargetContext().dropComplete( true);
//				String[] files = string.split( System.getProperty( "line.separator"));
//				if ( files.length <= 0)
//					arg0.rejectDrop();
//				else
//					on_file_open_by_drag_and_drop( new File( new URI( files[ 0].replaceAll( "[\r\n]", ""))));
//			} else {
//				arg0.rejectDrop();
//			}
//		} catch (IOException ioe) {
//			arg0.rejectDrop();
//		} catch (UnsupportedFlavorException ufe) {
//			arg0.rejectDrop();
//		} catch (URISyntaxException e) {
//			arg0.rejectDrop();
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
//	 */
//	public void dragExit(DropTargetEvent arg0) {
//	}
}
