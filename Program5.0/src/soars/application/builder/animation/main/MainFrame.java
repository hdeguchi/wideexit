/**
 * 
 */
package soars.application.builder.animation.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.builder.animation.common.tool.CommonTool;
import soars.application.builder.animation.document.DocumentManager;
import soars.application.builder.animation.main.panel.MainPanel;
import soars.application.builder.animation.main.text.AnimatorFileTextField;
import soars.application.builder.animation.main.tree.LanguageTree;
import soars.application.builder.animation.menu.file.CloseAction;
import soars.application.builder.animation.menu.file.ExitAction;
import soars.application.builder.animation.menu.file.ExportArchiveAction;
import soars.application.builder.animation.menu.file.NewAction;
import soars.application.builder.animation.menu.file.OpenAction;
import soars.application.builder.animation.menu.file.SaveAction;
import soars.application.builder.animation.menu.file.SaveAsAction;
import soars.application.builder.animation.menu.help.AboutAction;
import soars.application.builder.animation.menu.help.ForumAction;
import soars.application.builder.animation.menu.run.ApplicationAction;
import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.mac.IMacScreenMenuHandler;
import soars.common.utility.swing.mac.MacUtility;
import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Frame;
import soars.common.utility.tool.network.BrowserLauncher;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class MainFrame extends Frame implements IMacScreenMenuHandler, IMessageCallback {

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
	static public final String _animatorBuilderExtension = "abml";

	/**
	 * 
	 */
	static public final String _exportArchiveExtension = "zip";

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
	private Rectangle _windowRectangle = new Rectangle();

	/**
	 * 
	 */
	private UserInterface _userInterface = null;

	/**
	 * 
	 */
	private JMenuItem _fileNewMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _fileOpenMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _fileCloseMenuItem = null;

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
	private JMenuItem _fileExportArchiveMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _runApplicationMenuItem = null;

	/**
	 * 
	 */
	private JButton _fileNewButton = null;

	/**
	 * 
	 */
	private JButton _fileOpenButton = null;

	/**
	 * 
	 */
	private JButton _fileCloseButton = null;

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
	private JButton _fileExportArchiveButton = null;

	/**
	 * 
	 */
	private JButton _runApplicationButton = null;

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
	private JLabel _animatorFileLabel = null;

	/**
	 * 
	 */
	private AnimatorFileTextField _animatorFileTextField = null;

	/**
	 * 
	 */
	private JLabel _languageLabel = null;

	/**
	 * 
	 */
	private JComboBox _languageComboBox = null;

	/**
	 * 
	 */
	private JSplitPane _splitPane = null;

	/**
	 * 
	 */
	private LanguageTree _languageTree = null;

	/**
	 * 
	 */
	private MainPanel _mainPanel = null;

	/**
	 * 
	 */
	private boolean _ignore = false;

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
			String.valueOf( SwingTool.get_default_window_position( _minimumWidth, _minimumHeight).x));
		_windowRectangle.x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._main_window_rectangle_key + "y",
			String.valueOf( SwingTool.get_default_window_position( _minimumWidth, _minimumHeight).y));
		_windowRectangle.y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._main_window_rectangle_key + "width",
			String.valueOf( _minimumWidth));
		_windowRectangle.width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._main_window_rectangle_key + "height",
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
	private void set_property_to_environment_file() {
		Environment.get_instance().set(
			Environment._main_window_rectangle_key + "x", String.valueOf( _windowRectangle.x));
		Environment.get_instance().set(
			Environment._main_window_rectangle_key + "y", String.valueOf( _windowRectangle.y));
		Environment.get_instance().set(
			Environment._main_window_rectangle_key + "width", String.valueOf( _windowRectangle.width));
		Environment.get_instance().set(
			Environment._main_window_rectangle_key + "height", String.valueOf( _windowRectangle.height));

		Environment.get_instance().set(
			Environment._main_window_divider_location_key, String.valueOf( _splitPane.getDividerLocation()));

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

		JToolBar toolBar = new JToolBar();
		toolBar.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0));

		setup_menu( menuBar, toolBar);

		setJMenuBar( menuBar);

		toolBar.setFloatable( false);

		getContentPane().add( toolBar, BorderLayout.NORTH);
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


		NewAction newAction = new NewAction( ResourceManager.get_instance().get( "file.new.menu"));
		_fileNewMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.new.menu"),
			newAction,
			ResourceManager.get_instance().get( "file.new.mnemonic"),
			ResourceManager.get_instance().get( "file.new.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.new.message"));


		OpenAction openAction = new OpenAction(
			ResourceManager.get_instance().get( "file.open.menu"));
		_fileOpenMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.open.menu"),
			openAction,
			ResourceManager.get_instance().get( "file.open.mnemonic"),
			ResourceManager.get_instance().get( "file.open.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.open.message"));


		menu.addSeparator();


		CloseAction closeAction = new CloseAction(
			ResourceManager.get_instance().get( "file.close.menu"));
		_fileCloseMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.close.menu"),
			closeAction,
			ResourceManager.get_instance().get( "file.close.mnemonic"),
			ResourceManager.get_instance().get( "file.close.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.close.message"));
		_fileCloseMenuItem.setEnabled( false);


		menu.addSeparator();


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
		_fileSaveMenuItem.setEnabled( false);


		SaveAsAction saveAsAction = new SaveAsAction(
			ResourceManager.get_instance().get( "file.saveas.menu"));
		_fileSaveAsMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.saveas.menu"),
			saveAsAction,
			ResourceManager.get_instance().get( "file.saveas.mnemonic"),
			ResourceManager.get_instance().get( "file.saveas.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.saveas.message"));
		//_file_save_as_menuItem.setEnabled( false);


		menu.addSeparator();


		ExportArchiveAction exportArchiveAction = new ExportArchiveAction(
			ResourceManager.get_instance().get( "file.export.archive.menu"));
		_fileExportArchiveMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.export.archive.menu"),
			exportArchiveAction,
			ResourceManager.get_instance().get( "file.export.archive.mnemonic"),
			ResourceManager.get_instance().get( "file.export.archive.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.export.archive.message"));
		_fileExportArchiveMenuItem.setEnabled( false);


		menu.addSeparator();


		ExitAction exitAction = new ExitAction(
			ResourceManager.get_instance().get( "file.exit.menu"));
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
			ResourceManager.get_instance().get( "run.menu"),
			true,
			ResourceManager.get_instance().get( "run.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.message"));


		ApplicationAction applicationAction = new ApplicationAction(
			ResourceManager.get_instance().get( "run.application.menu"));
		_runApplicationMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "run.application.menu"),
			applicationAction,
			ResourceManager.get_instance().get( "run.application.mnemonic"),
			ResourceManager.get_instance().get( "run.application.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.application.message"));
		_runApplicationMenuItem.setEnabled( false);



		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "help.menu"),
			true,
			ResourceManager.get_instance().get( "help.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "help.message"));


//		ContentsAction contentsAction = new ContentsAction(
//			ResourceManager.get_instance().get( "help.contents.menu"));
//		_userInterface.append_menuitem(
//			menu,
//			ResourceManager.get_instance().get( "help.contents.menu"),
//			contentsAction,
//			ResourceManager.get_instance().get( "help.contents.mnemonic"),
//			ResourceManager.get_instance().get( "help.contents.stroke"),
//			_message_label,
//			ResourceManager.get_instance().get( "help.contents.message"));
//
//
//		menu.addSeparator();


		ForumAction forumAction = new ForumAction(
			ResourceManager.get_instance().get( "help.forum.menu"));
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "help.forum.menu"),
			forumAction,
			ResourceManager.get_instance().get( "help.forum.mnemonic"),
			ResourceManager.get_instance().get( "help.forum.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "help.forum.message"));


		menu.addSeparator();


		AboutAction aboutAction = new AboutAction(
			ResourceManager.get_instance().get( "help.about.menu"));
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


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/new.png"));
		_fileNewButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "file.new.menu"),
			newAction,
			ResourceManager.get_instance().get( "file.new.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.new.message"));
		_fileNewButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileNewButton.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/open.png"));
		_fileOpenButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "file.open.menu"),
			openAction,
			ResourceManager.get_instance().get( "file.open.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.open.message"));
		_fileOpenButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileOpenButton.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/close.png"));
		_fileCloseButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "file.close.menu"),
			closeAction,
			ResourceManager.get_instance().get( "file.close.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.close.message"));
		_fileCloseButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileCloseButton.getPreferredSize().height));
		_fileCloseButton.setEnabled( false);


		toolBar.addSeparator();


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
		_fileSaveButton.setEnabled( false);


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/save_as.png"));
		_fileSaveAsButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "file.saveas.menu"),
			saveAsAction,
			ResourceManager.get_instance().get( "file.saveas.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.saveas.message"));
		_fileSaveAsButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileSaveAsButton.getPreferredSize().height));
		//_file_save_as_button.setEnabled( false);


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/export_archive.png"));
		_fileExportArchiveButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "file.export.archive.menu"),
			exportArchiveAction,
			ResourceManager.get_instance().get( "file.export.archive.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.export.archive.message"));
		_fileExportArchiveButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileExportArchiveButton.getPreferredSize().height));
		_fileExportArchiveButton.setEnabled( false);


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/run/application.png"));
		_runApplicationButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "run.application.menu"),
			applicationAction,
			ResourceManager.get_instance().get( "run.application.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.application.message"));
		_runApplicationButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _runApplicationButton.getPreferredSize().height));
		_runApplicationButton.setEnabled( false);
	}

	/**
	 * 
	 */
	private void initialize_user_interface() {
		setTitle( ResourceManager.get_instance().get( "application.title"));
		update_user_interface( true, true, false, false, true, false, false);
		update( "                                             ");
	}

	/**
	 * @param file_new
	 * @param file_open
	 * @param file_close
	 * @param file_save
	 * @param file_save_as
	 * @param file_export_archive
	 * @param run_application
	 */
	private void update_user_interface(boolean file_new, boolean file_open,
		boolean file_close, boolean file_save, boolean file_save_as,
		boolean file_export_archive, boolean run_application) {
		enable_menuItem( _fileNewMenuItem, file_new);
		enable_menuItem( _fileOpenMenuItem, file_open);
		enable_menuItem( _fileCloseMenuItem, file_close);
		enable_menuItem( _fileSaveMenuItem, file_save);
		enable_menuItem( _fileSaveAsMenuItem, file_save_as);
		enable_menuItem( _fileExportArchiveMenuItem, file_export_archive);

		enable_menuItem( _runApplicationMenuItem, run_application);

		enable_button( _fileNewButton, file_new);
		enable_button( _fileOpenButton, file_open);
		enable_button( _fileCloseButton, file_close);
		enable_button( _fileSaveButton, file_save);
		enable_button( _fileSaveAsButton, file_save_as);
		enable_button( _fileExportArchiveButton, file_export_archive);

		enable_button( _runApplicationButton, run_application);
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
	 * 
	 */
	private void reset() {
		_mainPanel.reset();
		set_animator_file( "");
		DocumentManager.get_instance().reset();
		initialize_user_interface();
	}

	/**
	 * @param language
	 */
	public void set_selected_language(String language) {
		_ignore = true;
		_languageComboBox.setSelectedItem( DocumentManager.get_instance().get_language_name( language));
		_ignore = false;
	}

	/**
	 * @param filename
	 */
	public void set_animator_file(String filename) {
		_ignore = true;
		_animatorFileTextField.setText( filename);
		_ignore = false;
	}

	/**
	 * @return
	 */
	private boolean animator_file_exists() {
		String filename = _animatorFileTextField.getText();
		if ( null == filename || filename.equals( ""))
			return false;

		File file = new File( _animatorFileTextField.getText());
		return ( file.exists() && file.isFile() && file.canRead());
	}

	/**
	 * @param file
	 */
	public void open(File file) {
		if ( !MessageDlg.execute( this, ResourceManager.get_instance().get( "application.title"), true,
			"on_file_open", ResourceManager.get_instance().get( "file.open.show.message"), new Object[] { file}, this, this)) {
			reset();
			return;
		}

		_languageTree.load();

		setTitle( ResourceManager.get_instance().get( "application.title") + " - " + file.getName());

		update_user_interface( true, true,
			DocumentManager.get_instance().exist_datafile(),
			DocumentManager.get_instance().exist_datafile(),
			true, animator_file_exists(), animator_file_exists());
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IMessageCallback#message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.MessageDlg)
	 */
	public boolean message_callback(String id, Object[] objects, MessageDlg messageDlg) {
		if ( id.equals( "on_file_open"))
			return DocumentManager.get_instance().load( ( File)objects[ 0]);
		else if ( id.equals( "on_file_save"))
			return DocumentManager.get_instance().save();
		else if ( id.equals( "on_file_save_as"))
			return DocumentManager.get_instance().save_as( ( File)objects[ 0]);
		else if ( id.equals( "on_file_export_archive"))
			return DocumentManager.get_instance().export_archive( ( File)objects[ 0], ( String)objects[ 1], ( String)objects[ 2]);
		else if ( id.equals( "on_run_application"))
			return DocumentManager.get_instance().run_application( ( String)objects[ 0], ( String)objects[ 1]);

		return true;
	}

	/**
	 * @return
	 */
	private int confirm() {
		int result = JOptionPane.showConfirmDialog(
			this,
			ResourceManager.get_instance().get( "file.close.confirm.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_CANCEL_OPTION);
		switch ( result) {
			case JOptionPane.YES_OPTION:
				if ( DocumentManager.get_instance().exist_datafile())
					on_file_save( null);
				else {
					if ( !on_file_save_as( null))
						result = JOptionPane.CANCEL_OPTION;
				}
				break;
			case JOptionPane.CANCEL_OPTION:
			case JOptionPane.CLOSED_OPTION:
				requestFocus();
				break;
		}
		return result;
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

		if ( !setup())
			return false;

		setup_menu();

		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

		pack();

		optimize_window_rectangle();
		setLocation( _windowRectangle.x, _windowRectangle.y);
		setSize( _windowRectangle.width, _windowRectangle.height);

		Toolkit.getDefaultToolkit().setDynamicLayout( true);

		setVisible( true);

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

	/**
	 * @return
	 */
	private boolean setup() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		setup_animatorFileTextField( northPanel);

		insert_horizontal_glue( northPanel);

		setup_languageComboBox( northPanel);

		insert_horizontal_glue( northPanel);

		panel.add( northPanel, "North");


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		if ( !setup_center_panel( centerPanel))
			return false;

		panel.add( centerPanel);


		adjust();


		getContentPane().add( panel);


		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_animatorFileTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_animatorFileLabel = new JLabel( ResourceManager.get_instance().get( "main.frame.animator.file.label"));
		_animatorFileLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _animatorFileLabel);

		panel.add( Box.createHorizontalStrut( 5));

		_animatorFileTextField = new AnimatorFileTextField();
		_animatorFileTextField.getDocument().addDocumentListener( new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				on_update();
			}
			public void insertUpdate(DocumentEvent e) {
				on_update();
			}
			public void removeUpdate(DocumentEvent e) {
				on_update();
			}
		});
		_animatorFileTextField.setEditable( false);
		panel.add( _animatorFileTextField);

		panel.add( Box.createHorizontalStrut( 5));

		JButton button = new JButton( ResourceManager.get_instance().get( "main.frame.animator.file.reference.button"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_animator_file_selector_button_actionPerformed();
			}
		});
		panel.add( button);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_languageComboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_languageLabel = new JLabel( ResourceManager.get_instance().get( "main.frame.language.label"));
		_languageLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _languageLabel);

		_languageComboBox = new JComboBox( DocumentManager.get_instance().get_language_names());
		_languageComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if ( _ignore || !DocumentManager.get_instance().exist_datafile())
					return;

				DocumentManager.get_instance().modified();
			}
		});
		_languageComboBox.setPreferredSize( new Dimension( 150, _languageComboBox.getPreferredSize().height));
		panel.add( _languageComboBox);

		parent.add( panel);
	}

	/**
	 * 
	 */
	protected void on_update() {
		enable_menuItem( _fileExportArchiveMenuItem, animator_file_exists());
		enable_menuItem( _runApplicationMenuItem, animator_file_exists());
		enable_button( _fileExportArchiveButton, animator_file_exists());
		enable_button( _runApplicationButton, animator_file_exists());

		if ( _ignore)
			return;

		DocumentManager.get_instance().modified();
	}

	/**
	 * 
	 */
	protected void on_animator_file_selector_button_actionPerformed() {
		File file = CommonTool.get_animator_file(
			Environment._select_animator_file_directory_key,
			ResourceManager.get_instance().get( "select.animator.file.dialog.title"),
			this);
		if ( null == file)
			return;

		_animatorFileTextField.setText( file.getAbsolutePath().replaceAll(  "\\\\", "/"));
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_center_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));


		_mainPanel = new MainPanel();
		_languageTree = new LanguageTree( _mainPanel, this, this);

		if ( !_mainPanel.setup())
				return false;

		if ( !_languageTree.setup())
			return false;

		_splitPane = new JSplitPane();

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportView( _languageTree);
		_splitPane.setLeftComponent( scrollPane);

		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportView( _mainPanel);
		_splitPane.setRightComponent( scrollPane);
		
		String value = Environment.get_instance().get(
			Environment._main_window_divider_location_key, "100");
		_splitPane.setDividerLocation( Integer.parseInt( value));


		panel.add( _splitPane);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);


		return true;
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = _animatorFileLabel.getPreferredSize().width;
		width = Math.max( width, _languageLabel.getPreferredSize().width);

		_animatorFileLabel.setPreferredSize( new Dimension( width, _animatorFileLabel.getPreferredSize().height));
		_languageLabel.setPreferredSize( new Dimension( width, _languageLabel.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_window_closing(java.awt.event.WindowEvent)
	 */
	protected void on_window_closing(WindowEvent windowEvent) {
		if ( DocumentManager.get_instance().isModified()) {
			int result = confirm();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return;
		} else {
			if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
				this,
				ResourceManager.get_instance().get( "file.exit.confirm.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.YES_NO_OPTION)) {
				requestFocus();
				return;
			}
		}

		_windowRectangle = getBounds();
		set_property_to_environment_file();
		System.exit( 0);
	}

	/**
	 * @param actionEvent
	 */
	public void on_file_new(ActionEvent actionEvent) {
		on_file_close( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_file_open(ActionEvent actionEvent) {
		if ( DocumentManager.get_instance().isModified()) {
			int result = confirm();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return;
		}

		File file = CommonTool.get_open_file(
			Environment._open_directory_key,
			ResourceManager.get_instance().get( "file.open.dialog"),
			new String[] { _animatorBuilderExtension},
			"animator builder data",
			this);

		requestFocus();

		if ( null == file)
			return;

		open( file);
	}

	/**
	 * @param actionEvent
	 */
	public void on_file_close(ActionEvent actionEvent) {
		if ( DocumentManager.get_instance().isModified()) {
			int result = confirm();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return;
		}

		reset();

		requestFocus();
	}

	/**
	 * @param actionEvent
	 */
	public void on_file_save(ActionEvent actionEvent) {
		MessageDlg.execute( this, ResourceManager.get_instance().get( "application.title"), true,
			"on_file_save", ResourceManager.get_instance().get( "file.save.show.message"), this, this);

		setTitle( ResourceManager.get_instance().get( "application.title")
			+ " - " + DocumentManager.get_instance().get_current_file().getName());

		requestFocus();
	}

	/**
	 * @param actionEvent
	 * @return
	 */
	public boolean on_file_save_as(ActionEvent actionEvent) {
		File file = CommonTool.get_save_file(
			Environment._save_as_directory_key,
			ResourceManager.get_instance().get( "file.saveas.dialog"),
			new String[] { _animatorBuilderExtension},
			"animator builder data",
			this);

		requestFocus();

		if ( null == file)
			return false;

		String absolute_name = file.getAbsolutePath();
		String name = file.getName();
		int index = name.lastIndexOf( '.');
		if ( -1 == index)
			file = new File( absolute_name + "." + _animatorBuilderExtension);
		else if ( name.length() - 1 == index)
			file = new File( absolute_name + _animatorBuilderExtension);

		if ( !MessageDlg.execute( this, ResourceManager.get_instance().get( "application.title"), true,
			"on_file_save_as", ResourceManager.get_instance().get( "file.saveas.show.message"),
			new Object[] { file}, this, this))
			return false;

		_fileCloseMenuItem.setEnabled( true);
		_fileCloseButton.setEnabled( true);
		_fileSaveMenuItem.setEnabled( true);
		_fileSaveButton.setEnabled( true);

		setTitle( ResourceManager.get_instance().get( "application.title") + " - " + file.getName());

		return true;
	}

	/**
	 * @param actionEvent
	 */
	public void on_file_export_archive(ActionEvent actionEvent) {
		_languageTree.store();

		File file = CommonTool.get_save_file(
			Environment._export_archive_directory_key,
			ResourceManager.get_instance().get( "file.export.archive.dialog"),
			new String[] { _exportArchiveExtension},
			"animation archive data",
			this);

		requestFocus();

		if ( null == file)
			return;

		String absolute_name = file.getAbsolutePath();
		String name = file.getName();
		int index = name.lastIndexOf( '.');
		if ( -1 == index)
			file = new File( absolute_name + "." + _exportArchiveExtension);
		else if ( name.length() - 1 == index)
			file = new File( absolute_name + _exportArchiveExtension);

		if ( !MessageDlg.execute( this, ResourceManager.get_instance().get( "application.title"), true,
			"on_file_export_archive", ResourceManager.get_instance().get( "file.export.archive.show.message"),
			new Object[] { file, _animatorFileTextField.getText().replaceAll(  "\\\\", "/"),
				( String)_languageComboBox.getSelectedItem()},
			this, this))
			JOptionPane.showMessageDialog(
				this,
				ResourceManager.get_instance().get( "file.export.archive.could.not.export.archive.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param actionEvent
	 */
	public void on_file_exit(ActionEvent actionEvent) {
		if ( DocumentManager.get_instance().isModified()) {
			int result = confirm();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return;
		} else {
			if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
				this,
				ResourceManager.get_instance().get( "file.exit.confirm.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.YES_NO_OPTION)) {
				requestFocus();
				return;
			}
		}

		_windowRectangle = getBounds();
		set_property_to_environment_file();
		System.exit( 0);
	}

	/**
	 * @param actionEvent
	 */
	public void on_run_application(ActionEvent actionEvent) {
		_languageTree.store();

		if ( !MessageDlg.execute( this, ResourceManager.get_instance().get( "application.title"), true,
			"on_run_application", ResourceManager.get_instance().get( "run.application.show.message"),
			new Object[] { _animatorFileTextField.getText().replaceAll(  "\\\\", "/"),
				( String)_languageComboBox.getSelectedItem()},
			this, this))
			JOptionPane.showMessageDialog(
				this,
				ResourceManager.get_instance().get( "run.application.could.not.launch.application.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param actionEvent
	 */
	public void on_help_contents(ActionEvent actionEvent) {
		String current_directory_name = System.getProperty( Constant._soarsHome);
		if ( null == current_directory_name)
			return;

		File file = new File( current_directory_name + "/"
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

	/**
	 * @param information
	 */
	public void update(String information) {
		_informationLabel.setText( information);
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException 
	 */
	public boolean write(Writer writer) throws SAXException {
		_languageTree.store();


		if ( !DocumentManager.get_instance().write( writer))
			return false;


		String language_name = ( String)_languageComboBox.getSelectedItem();
		if ( null == language_name)
			return false;

		String langage = DocumentManager.get_instance().get_language( language_name);
		if ( null == langage)
			return false;

		writer.startElement( null, null, "selected_language", new AttributesImpl());
		writer.characters( langage.toCharArray(), 0, langage.length());
		writer.endElement( null, null, "selected_language");


		String filename = _animatorFileTextField.getText();
		if ( null == filename)
			return false;

		writer.startElement( null, null, "animator_file", new AttributesImpl());
		writer.characters( filename.toCharArray(), 0, filename.length());
		writer.endElement( null, null, "animator_file");


		return true;
	}
}
