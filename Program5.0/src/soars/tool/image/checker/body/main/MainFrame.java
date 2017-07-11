/**
 * 
 */
package soars.tool.image.checker.body.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.mac.IMacScreenMenuHandler;
import soars.common.utility.swing.mac.MacUtility;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Frame;
import soars.common.utility.tool.network.BrowserLauncher;
import soars.tool.image.checker.body.menu.file.CopyToClipboardAction;
import soars.tool.image.checker.body.menu.file.ExitAction;
import soars.tool.image.checker.body.menu.file.InitializeListAction;
import soars.tool.image.checker.body.menu.help.AboutAction;
import soars.tool.image.checker.body.menu.help.ForumAction;

/**
 * @author kurata
 *
 */
public class MainFrame extends Frame implements IMacScreenMenuHandler {

	/**
	 * Default width.
	 */
	static public final int _minimum_width = 800;

	/**
	 * Default height.
	 */
	static public final int _minimum_height = 600;

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
	private Rectangle _window_rectangle = new Rectangle();

	/**
	 * 
	 */
	private UserInterface _userInterface = null;

	/**
	 * 
	 */
	private JMenuItem _file_initialize_list_menuItem = null;

	/**
	 * 
	 */
	private JMenuItem _file_copy_to_clipboard_menuItem = null;

	/**
	 * 
	 */
	private JLabel _message_label = null;

	/**
	 * 
	 */
	private JLabel _information_label = null;

	/**
	 * Returns the instance of this class.
	 * @return the instance of this class
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
	 * @param arg0
	 * @throws HeadlessException
	 */
	public MainFrame(String arg0) throws HeadlessException {
		super(arg0);
	}

	/**
	 * 
	 */
	private void get_property_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._main_window_rectangle_key + "x",
			String.valueOf( SwingTool.get_default_window_position( _minimum_width, _minimum_height).x));
		_window_rectangle.x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._main_window_rectangle_key + "y",
			String.valueOf( SwingTool.get_default_window_position( _minimum_width, _minimum_height).y));
		_window_rectangle.y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._main_window_rectangle_key + "width",
			String.valueOf( _minimum_width));
		_window_rectangle.width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._main_window_rectangle_key + "height",
			String.valueOf( _minimum_height));
		_window_rectangle.height = Integer.parseInt( value);
	}

	/**
	 * @return
	 */
	private void optimize_window_rectangle() {
		if ( !GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersects( _window_rectangle)
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( _window_rectangle).width <= 10
			|| _window_rectangle.y <= -getInsets().top
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( _window_rectangle).height <= getInsets().top)
			_window_rectangle.setBounds(
				SwingTool.get_default_window_position( _minimum_width, _minimum_height).x,
				SwingTool.get_default_window_position( _minimum_width, _minimum_height).y,
				_minimum_width, _minimum_height);
	}

	/**
	 * 
	 */
	private void set_property_to_environment_file() throws IOException {
		Environment.get_instance().set(
			Environment._main_window_rectangle_key + "x", String.valueOf( _window_rectangle.x));
		Environment.get_instance().set(
			Environment._main_window_rectangle_key + "y", String.valueOf( _window_rectangle.y));
		Environment.get_instance().set(
			Environment._main_window_rectangle_key + "width", String.valueOf( _window_rectangle.width));
		Environment.get_instance().set(
			Environment._main_window_rectangle_key + "height", String.valueOf( _window_rectangle.height));

		Environment.get_instance().store();
	}

	/**
	 * 
	 */
	private void setup_menu() {
		JToolBar statusBar = new JToolBar();

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		_message_label = new JLabel( "");
		//statusBar.add( _message_label);
		panel.add( _message_label);
		statusBar.add( panel);

		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));
		//panel.setLayout( new BorderLayout());

		//_information_label = new JLabel( "");
		_information_label = new JLabel( "                                             ");
		_information_label.setHorizontalAlignment( Label.RIGHT);
		//statusBar.add( _information_label);
		panel.add( _information_label);
		statusBar.add( panel);

		statusBar.setFloatable( false);

		//statusBar.setEnabled( false);

		getContentPane().add( statusBar, BorderLayout.SOUTH);




		JMenuBar menuBar = new JMenuBar();


		JMenu menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "file.menu"),
			true,
			ResourceManager.get_instance().get( "file.mnemonic"),
			_message_label,
			ResourceManager.get_instance().get( "file.message"));


		InitializeListAction initializeListAction = new InitializeListAction( ResourceManager.get_instance().get( "file.initialize.list.menu"));
		_file_initialize_list_menuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.initialize.list.menu"),
			initializeListAction,
			ResourceManager.get_instance().get( "file.initialize.list.mnemonic"),
			ResourceManager.get_instance().get( "file.initialize.list.stroke"),
			_message_label,
			ResourceManager.get_instance().get( "file.initialize.list.message"));


		CopyToClipboardAction copyToClipboardAction = new CopyToClipboardAction( ResourceManager.get_instance().get( "file.copy.to.clipboard.menu"));
		_file_copy_to_clipboard_menuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.copy.to.clipboard.menu"),
			copyToClipboardAction,
			ResourceManager.get_instance().get( "file.copy.to.clipboard.mnemonic"),
			ResourceManager.get_instance().get( "file.copy.to.clipboard.stroke"),
			_message_label,
			ResourceManager.get_instance().get( "file.copy.to.clipboard.message"));


		menu.addSeparator();


		ExitAction exitAction = new ExitAction( ResourceManager.get_instance().get( "file.exit.menu"));
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.exit.menu"),
			exitAction,
			ResourceManager.get_instance().get( "file.exit.mnemonic"),
			ResourceManager.get_instance().get( "file.exit.stroke"),
			_message_label,
			ResourceManager.get_instance().get( "file.exit.message"));


		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "help.menu"),
			true,
			ResourceManager.get_instance().get( "help.mnemonic"),
			_message_label,
			ResourceManager.get_instance().get( "help.message"));


		ForumAction forumAction = new ForumAction( ResourceManager.get_instance().get( "help.forum.menu"));
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "help.forum.menu"),
			forumAction,
			ResourceManager.get_instance().get( "help.forum.mnemonic"),
			ResourceManager.get_instance().get( "help.forum.stroke"),
			_message_label,
			ResourceManager.get_instance().get( "help.forum.message"));


		menu.addSeparator();


		AboutAction aboutAction = new AboutAction( ResourceManager.get_instance().get( "help.about.menu"));
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "help.about.menu"),
			aboutAction,
			ResourceManager.get_instance().get( "help.about.mnemonic"),
			ResourceManager.get_instance().get( "help.about.stroke"),
			_message_label,
			ResourceManager.get_instance().get( "help.about.message"));

		setJMenuBar( menuBar);
	}

	/**
	 * 
	 */
	public void initialize() {
		_file_initialize_list_menuItem.setEnabled( false);
		_file_copy_to_clipboard_menuItem.setEnabled( false);
	}

	/**
	 * 
	 */
	public void reset() {
		_file_initialize_list_menuItem.setEnabled( 0 < MainList.get_instance().getModel().getSize());
		_file_copy_to_clipboard_menuItem.setEnabled( 0 < MainList.get_instance().getModel().getSize());
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		if ( !MacUtility.setup_screen_menu_handler( this, this, ResourceManager.get_instance().get( "application.title")))
			return false;

		get_property_from_environment_file();

		_userInterface = new UserInterface();

		MainList mainList = new MainList( this, this);
		if ( !mainList.setup())
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportView( mainList);
		scrollPane.setBackground( new Color( 255, 255, 255));
		getContentPane().setLayout( new BorderLayout());
		getContentPane().add( scrollPane);

		setup_menu();

		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

		pack();

		optimize_window_rectangle();
		setLocation( _window_rectangle.x, _window_rectangle.y);
		setSize( _window_rectangle.width, _window_rectangle.height);

		Toolkit.getDefaultToolkit().setDynamicLayout( true);

		initialize();

		setVisible( true);

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				int width = getSize().width;
				int height = getSize().height;
				setSize( ( _minimum_width > width) ? _minimum_width : width,
					( _minimum_height > height) ? _minimum_height : height);
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
		if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
			this,
			ResourceManager.get_instance().get( "file.exit.confirm.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION)) {
			requestFocus();
			return;
		}

		_window_rectangle = getBounds();
		try {
			set_property_to_environment_file();
		}
		catch (IOException e) {
			throw new RuntimeException( e);
		}

		System.exit( 0);
	}

	/**
	 * @param actionEvent
	 */
	public void on_file_initialize_list(ActionEvent actionEvent) {
		MainList.get_instance().initialize();
	}

	/**
	 * @param actionEvent
	 */
	public void on_file_copy_to_clipboard(ActionEvent actionEvent) {
		MainList.get_instance().copy_to_clipboard();
	}

	/**
	 * @param actionEvent
	 */
	public void on_file_exit(ActionEvent actionEvent) {
		if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
			this,
			ResourceManager.get_instance().get( "file.exit.confirm.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION)) {
			requestFocus();
			return;
		}

		_window_rectangle = getBounds();
		try {
			set_property_to_environment_file();
		}
		catch (IOException e) {
			throw new RuntimeException( e);
		}

		System.exit( 0);
	}

	/**
	 * @param actionEvent
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

	/**
	 * @param actionEvent
	 */
	public void on_help_about(ActionEvent actionEvent) {
		JOptionPane.showMessageDialog( this,
			Constant.get_version_message(),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.INFORMATION_MESSAGE);

		requestFocus();
	}
}
