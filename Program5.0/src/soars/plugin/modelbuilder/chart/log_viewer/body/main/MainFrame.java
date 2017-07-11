/*
 * 2004/11/08
 */
package soars.plugin.modelbuilder.chart.log_viewer.body.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.mac.IMacScreenMenuHandler;
import soars.common.utility.swing.mac.MacUtility;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.MDIFrame;
import soars.common.utility.tool.network.BrowserLauncher;
import soars.plugin.modelbuilder.chart.log_viewer.body.common.tool.CommonTool;
import soars.plugin.modelbuilder.chart.log_viewer.body.menu.file.ExitAction;
import soars.plugin.modelbuilder.chart.log_viewer.body.menu.file.IFileMenuHandler;
import soars.plugin.modelbuilder.chart.log_viewer.body.menu.file.OpenAction;
import soars.plugin.modelbuilder.chart.log_viewer.body.menu.help.AboutAction;
import soars.plugin.modelbuilder.chart.log_viewer.body.menu.help.ForumAction;
import soars.plugin.modelbuilder.chart.log_viewer.body.menu.help.IHelpMenuHandler;

/**
 * @author kurata
 */
public class MainFrame extends MDIFrame implements IMacScreenMenuHandler, DropTargetListener, IFileMenuHandler, IHelpMenuHandler {

	/**
	 * 
	 */
	static public final int _minimum_width = 800;

	/**
	 * 
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
	private static Rectangle _window_rectangle = new Rectangle();

	/**
	 * 
	 */
	public UserInterface _userInterface = null;

	/**
	 * 
	 */
	private JMenuItem _file_open_menuItem = null;

	/**
	 * 
	 */
	private JButton _file_open_button = null;

	/**
	 * 
	 */
	private JLabel _message_label = null;

	/**
	 * 
	 */
	private JLabel _information_label = null;

	/**
	 * 
	 */
	private final Color _background_color = new Color( 128, 128, 128);

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
		getContentPane().add( statusBar, BorderLayout.SOUTH);



		JMenuBar menuBar = new JMenuBar();
		JMenu menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "file.menu"),
			true,
			ResourceManager.get_instance().get( "file.mnemonic"),
			_message_label,
			ResourceManager.get_instance().get( "file.message"));


		OpenAction openAction = new OpenAction( ResourceManager.get_instance().get( "file.open.menu"), this);
		_file_open_menuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.open.menu"),
			openAction,
			ResourceManager.get_instance().get( "file.open.mnemonic"),
			ResourceManager.get_instance().get( "file.open.stroke"),
			_message_label,
			ResourceManager.get_instance().get( "file.open.message"));


		menu.addSeparator();


		ExitAction exitAction = new ExitAction( ResourceManager.get_instance().get( "file.exit.menu"), this);
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


		ForumAction forumAction = new ForumAction( ResourceManager.get_instance().get( "help.forum.menu"), this);
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "help.forum.menu"),
			forumAction,
			ResourceManager.get_instance().get( "help.forum.mnemonic"),
			ResourceManager.get_instance().get( "help.forum.stroke"),
			_message_label,
			ResourceManager.get_instance().get( "help.forum.message"));


		menu.addSeparator();


		AboutAction aboutAction = new AboutAction( ResourceManager.get_instance().get( "help.about.menu"), this);
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "help.about.menu"),
			aboutAction,
			ResourceManager.get_instance().get( "help.about.mnemonic"),
			ResourceManager.get_instance().get( "help.about.stroke"),
			_message_label,
			ResourceManager.get_instance().get( "help.about.message"));


		setJMenuBar( menuBar);




		JToolBar toolBar = new JToolBar();
		toolBar.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0));

		ImageIcon imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/toolbar/menu/file/app_exit.png"));
		JButton button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "file.exit.menu"),
			exitAction,
			ResourceManager.get_instance().get( "file.exit.tooltip"),
			_message_label,
			ResourceManager.get_instance().get( "file.exit.message"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/toolbar/menu/file/open.png"));
		_file_open_button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "file.open.menu"),
			openAction,
			ResourceManager.get_instance().get( "file.open.tooltip"),
			_message_label,
			ResourceManager.get_instance().get( "file.open.message"));
		_file_open_button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _file_open_button.getPreferredSize().height));


		toolBar.setFloatable( false);
		getContentPane().add( toolBar, BorderLayout.NORTH);
	}

	/**
	 * @param file
	 * @return
	 */
	private boolean create_new_childFrame(File file) {
		ChartFrame chartFrame = new ChartFrame( "", true, true, true, true, this);
		if ( !chartFrame.create())
			return false;

		_desktopPane.add( chartFrame);

		chartFrame.toFront();

		if ( null == file)
			chartFrame.setTitle( "New chart");
		else {
			if ( !chartFrame.read( file))
				return false;

			chartFrame.set_title();
		}

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		if ( !MacUtility.setup_screen_menu_handler( this, this, ResourceManager.get_instance().get( "application.title")))
			return false;

		_desktopPane.setBackground( _background_color);

		get_property_from_environment_file();

		_userInterface = new UserInterface();

		setup_menu();

		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

		new DropTarget( this, this);

		pack();

		optimize_window_rectangle();
		setLocation( _window_rectangle.x, _window_rectangle.y);
		setSize( _window_rectangle.width, _window_rectangle.height);

		Toolkit.getDefaultToolkit().setDynamicLayout( true);

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

	/* (Non Javadoc)
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

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.log_viewer.menu.file.IFileMenuHandler#on_file_open(java.awt.event.ActionEvent)
	 */
	public void on_file_open(ActionEvent actionEvent) {
		File file = CommonTool.get_open_file(
			Environment._open_directory_key,
			ResourceManager.get_instance().get( "file.open.dialog"),
			new String[] { "pml"},
			"soars chart data",
			this);

		if ( null == file)
			return;

		create_new_childFrame( file);
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.log_viewer.menu.file.IFileMenuHandler#on_file_save_image_as(java.awt.event.ActionEvent)
	 */
	public void on_file_save_image_as(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.log_viewer.menu.file.IFileMenuHandler#on_file_exit(java.awt.event.ActionEvent)
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

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.log_viewer.menu.help.IHelpMenuHandler#on_help_forum(java.awt.event.ActionEvent)
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
	 * @see soars.plugin.modelbuilder.chart.log_viewer.menu.help.IHelpMenuHandler#on_help_about(java.awt.event.ActionEvent)
	 */
	public void on_help_about(ActionEvent actionEvent) {
		JOptionPane.showMessageDialog( this,
			Constant.get_version_message(),
			ResourceManager.get_instance().get( "application.title"),
//			JOptionPane.INFORMATION_MESSAGE,
//			new ImageIcon( Resource.load_image_from_resource( Constant._resource_directory + "/image/picture/picture.jpg", getClass())));
			JOptionPane.INFORMATION_MESSAGE);

		requestFocus();
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent arg0) {
		arg0.acceptDrag( DnDConstants.ACTION_COPY);
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent arg0) {
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent arg0) {
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent arg0) {
		try {
			Transferable transferable = arg0.getTransferable();
			if ( transferable.isDataFlavorSupported( DataFlavor.javaFileListFlavor)) {
				arg0.acceptDrop( DnDConstants.ACTION_COPY_OR_MOVE);
				List list = ( List)transferable.getTransferData( DataFlavor.javaFileListFlavor);
				for ( int i = 0; i < list.size(); ++i) {
					File file =( File)list.get( i);
					create_new_childFrame( file);
				}
				arg0.getDropTargetContext().dropComplete( true);
			} else if ( transferable.isDataFlavorSupported( DataFlavor.stringFlavor)) {
				arg0.acceptDrop( DnDConstants.ACTION_COPY_OR_MOVE);
				String string = ( String)transferable.getTransferData( DataFlavor.stringFlavor);
				arg0.getDropTargetContext().dropComplete( true);
				String[] files = string.split( System.getProperty( "line.separator"));
				if ( files.length <= 0)
					arg0.rejectDrop();
				else {
					for ( int i = 0; i < files.length; ++i)
						create_new_childFrame( new File( new URI( files[ 0].replaceAll( "[\r\n]", ""))));
				}
			} else {
				arg0.rejectDrop();
			}
		} catch (IOException ioe) {
			arg0.rejectDrop();
		} catch (UnsupportedFlavorException ufe) {
			arg0.rejectDrop();
		} catch (URISyntaxException e) {
			arg0.rejectDrop();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent arg0) {
	}
}
