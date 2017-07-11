/**
 * 
 */
package soars.common.utility.swing.file.manager.edit;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

import soars.common.utility.swing.file.manager.Constant;
import soars.common.utility.swing.file.manager.IFileManager;
import soars.common.utility.swing.file.manager.IFileManagerCallBack;
import soars.common.utility.swing.file.manager.ResourceManager;
import soars.common.utility.swing.file.manager.edit.menu.edit.IEditMenuHandler;
import soars.common.utility.swing.file.manager.edit.menu.edit.RedoAction;
import soars.common.utility.swing.file.manager.edit.menu.edit.UndoAction;
import soars.common.utility.swing.file.manager.edit.menu.file.ExitAction;
import soars.common.utility.swing.file.manager.edit.menu.file.IFileMenuHandler;
import soars.common.utility.swing.file.manager.edit.menu.file.SaveAction;
import soars.common.utility.swing.file.manager.edit.menu.file.SaveWithSpecifiedEncodingAction;
import soars.common.utility.swing.file.manager.table.EncodingSelectorDlg;
import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Frame;
import soars.common.utility.tool.environment.EnvironmentBase;
import soars.common.utility.tool.file.FileUtility;

/**
 * @author kurata
 *
 */
public class FileEditorFrame extends Frame implements IFileMenuHandler, IEditMenuHandler, ITextUndoRedoManagerCallBack {

	/**
	 * Default width.
	 */
	static public final int _minimumWidth = 640;

	/**
	 * Default height.
	 */
	static public final int _minimumHeight = 480;

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
	private JMenuItem _fileSaveWithSpecifiedEncodingMenuItem = null;

//	/**
//	 * 
//	 */
//	private JMenuItem _fileSaveAsMenuItem = null;

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
	private JButton _fileSaveWithSpecifiedEncodingButton = null;

//	/**
//	 * 
//	 */
//	private JButton _fileSaveAsButton = null;

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
	private File _file = null;

	/**
	 * 
	 */
	private EnvironmentBase _environmentBase = null;

	/**
	 * 
	 */
	private String _windowRectangleKey = null;

	/**
	 * 
	 */
	private String _encoding = "";

	/**
	 * 
	 */
	private IFileManager _fileManager = null;

	/**
	 * 
	 */
	private IFileManagerCallBack _fileManagerCallBack = null;

	/**
	 * 
	 */
	private JTextArea _textArea = null;

	/**
	 * 
	 */
	private TextUndoRedoManager _textUndoRedoManager = null;

	/**
	 * 
	 */
	private boolean _modified = false;

	/**
	 * @param file
	 * @param environmentBase
	 * @param windowRectangleKey
	 * @param encoding
	 * @param fileManager
	 * @param fileManagerCallBack
	 * @throws HeadlessException
	 */
	public FileEditorFrame(File file, EnvironmentBase environmentBase, String windowRectangleKey, String encoding, IFileManager fileManager, IFileManagerCallBack fileManagerCallBack) throws HeadlessException {
		super(( null == encoding || encoding.equals( "")) ? file.getName() : ( file.getName() + " [" + encoding + "]"));
		_file = file;
		_environmentBase = environmentBase;
		_windowRectangleKey = windowRectangleKey;
		_encoding = encoding;
		_fileManager = fileManager;
		_fileManagerCallBack = fileManagerCallBack;
	}

	/**
	 * 
	 */
	private void get_property_from_environment_file() {
		String value = _environmentBase.get( _windowRectangleKey + "x",
			String.valueOf( SwingTool.get_default_window_position( _minimumWidth, _minimumHeight).x));
		_windowRectangle.x = Integer.parseInt( value);

		value = _environmentBase.get( _windowRectangleKey + "y",
			String.valueOf( SwingTool.get_default_window_position( _minimumWidth, _minimumHeight).y));
		_windowRectangle.y = Integer.parseInt( value);

		value = _environmentBase.get( _windowRectangleKey + "width", String.valueOf( _minimumWidth));
		_windowRectangle.width = Integer.parseInt( value);

		value = _environmentBase.get( _windowRectangleKey + "height", String.valueOf( _minimumHeight));
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
		_environmentBase.set( _windowRectangleKey + "x", String.valueOf( _windowRectangle.x));
		_environmentBase.set( _windowRectangleKey + "y", String.valueOf( _windowRectangle.y));
		_environmentBase.set( _windowRectangleKey + "width", String.valueOf( _windowRectangle.width));
		_environmentBase.set( _windowRectangleKey + "height", String.valueOf( _windowRectangle.height));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		get_property_from_environment_file();

		_userInterface = new UserInterface();

		setup_menu();

		if ( !setup_textArea())
			return false;

		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

//		new DropTarget( this, this);

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

		setup_menu( menuBar, toolBar);

		setJMenuBar( menuBar);

		toolBar.setFloatable( false);

		//toolBar.setEnabled( false);

		northPanel.add( toolBar);


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


//		NewAction newAction = new NewAction( ResourceManager.get_instance().get( "file.new.menu"), this);
//		_fileNewMenuItem = _userInterface.append_menuitem(
//			menu,
//			ResourceManager.get_instance().get( "file.new.menu"),
//			newAction,
//			ResourceManager.get_instance().get( "file.new.mnemonic"),
//			ResourceManager.get_instance().get( "file.new.stroke"),
//			_message_label,
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
//			_message_label,
//			ResourceManager.get_instance().get( "file.open.message"));
//
//
//		CloseAction closeAction = new CloseAction( ResourceManager.get_instance().get( "file.close.menu"), this);
//		_fileCloseMenuItem = _userInterface.append_menuitem(
//			menu,
//			ResourceManager.get_instance().get( "file.close.menu"),
//			closeAction,
//			ResourceManager.get_instance().get( "file.close.mnemonic"),
//			ResourceManager.get_instance().get( "file.close.stroke"),
//			_message_label,
//			ResourceManager.get_instance().get( "file.close.message"));
//
//
//		menu.addSeparator();


		SaveAction saveAction = new SaveAction( ResourceManager.get_instance().get( "file.save.menu"), this);
		_fileSaveMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.save.menu"),
			saveAction,
			ResourceManager.get_instance().get( "file.save.mnemonic"),
			ResourceManager.get_instance().get( "file.save.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.save.message"));


//		SaveAsAction saveAsAction = new SaveAsAction( ResourceManager.get_instance().get( "file.save.as.menu"), this);
//		_fileSaveAsMenuItem = _userInterface.append_menuitem(
//			menu,
//			ResourceManager.get_instance().get( "file.save.as.menu"),
//			saveAsAction,
//			ResourceManager.get_instance().get( "file.save.as.mnemonic"),
//			ResourceManager.get_instance().get( "file.save.as.stroke"),
//			_message_label,
//			ResourceManager.get_instance().get( "file.save.as.message"));


		menu.addSeparator();


		SaveWithSpecifiedEncodingAction saveWithSpecifiedEncodingAction = new SaveWithSpecifiedEncodingAction( ResourceManager.get_instance().get( "file.save.with.specified.encoding.menu"), this);
		_fileSaveWithSpecifiedEncodingMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.save.with.specified.encoding.menu"),
			saveAction,
			ResourceManager.get_instance().get( "file.save.with.specified.encoding.mnemonic"),
			ResourceManager.get_instance().get( "file.save.with.specified.encoding.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.save.with.specified.encoding.message"));


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
			ResourceManager.get_instance().get( "edit.menu"),
			true,
			ResourceManager.get_instance().get( "edit.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "edit.message"));


		UndoAction undoAction = new UndoAction( ResourceManager.get_instance().get( "edit.undo.menu"), this);
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.undo.menu"),
			undoAction,
			ResourceManager.get_instance().get( "edit.undo.mnemonic"),
			ResourceManager.get_instance().get( "edit.undo.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "edit.undo.message"));


		RedoAction redoAction = new RedoAction( ResourceManager.get_instance().get( "edit.redo.menu"), this);
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.redo.menu"),
			redoAction,
			ResourceManager.get_instance().get( "edit.redo.mnemonic"),
			ResourceManager.get_instance().get( "edit.redo.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "edit.redo.message"));



		ImageIcon imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/editor/toolbar/menu/file/app_exit.png"));
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


//		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/editor/toolbar/menu/file/new.png"));
//		_fileNewButton = _userInterface.append_tool_button(
//			toolBar,
//			imageIcon,
//			ResourceManager.get_instance().get( "file.new.menu"),
//			newAction,
//			ResourceManager.get_instance().get( "file.new.tooltip"),
//			_message_label,
//			ResourceManager.get_instance().get( "file.new.message"));
//		_fileNewButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileNewButton.getPreferredSize().height));
//
//
//		toolBar.addSeparator();
//
//
//		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/editor/toolbar/menu/file/open.png"));
//		_fileOpenButton = _userInterface.append_tool_button(
//			toolBar,
//			imageIcon,
//			ResourceManager.get_instance().get( "file.open.menu"),
//			openAction,
//			ResourceManager.get_instance().get( "file.open.tooltip"),
//			_message_label,
//			ResourceManager.get_instance().get( "file.open.message"));
//		_fileOpenButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileOpenButton.getPreferredSize().height));
//
//
//		toolBar.addSeparator();
//
//
//		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/editor/toolbar/menu/file/close.png"));
//		_fileCloseButton = _userInterface.append_tool_button(
//			toolBar,
//			imageIcon,
//			ResourceManager.get_instance().get( "file.close.menu"),
//			closeAction,
//			ResourceManager.get_instance().get( "file.close.tooltip"),
//			_message_label,
//			ResourceManager.get_instance().get( "file.close.message"));
//		_fileCloseButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileCloseButton.getPreferredSize().height));
//
//
//		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/editor/toolbar/menu/file/save.png"));
		_fileSaveButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "file.save.menu"),
			saveAction,
			ResourceManager.get_instance().get( "file.save.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.save.message"));
		_fileSaveButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileSaveButton.getPreferredSize().height));


//		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/editor/toolbar/menu/file/save_as.png"));
//		_fileSaveAsButton = _userInterface.append_tool_button(
//			toolBar,
//			imageIcon,
//			ResourceManager.get_instance().get( "file.save.as.menu"),
//			saveAsAction,
//			ResourceManager.get_instance().get( "file.save.as.tooltip"),
//			_message_label,
//			ResourceManager.get_instance().get( "file.save.as.message"));
//		_fileSaveAsButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileSaveAsButton.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/editor/toolbar/menu/file/save_with_specified_encoding.png"));
		_fileSaveWithSpecifiedEncodingButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "file.save.with.specified.encoding.menu"),
			saveWithSpecifiedEncodingAction,
			ResourceManager.get_instance().get( "file.save.with.specified.encoding.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.save.with.specified.encoding.message"));
		_fileSaveWithSpecifiedEncodingButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileSaveButton.getPreferredSize().height));
	}

	/**
	 * @return
	 */
	private boolean setup_textArea() {
		String text = "";
		if ( 0l < _file.length()) {
			text = ( ( null == _encoding || _encoding.equals( "")) ? FileUtility.read_text_from_file( _file) : FileUtility.read_text_from_file( _file, _encoding));
			if ( null == text)
				return false;
		}

		_textArea = new JTextArea( text);
		_textUndoRedoManager = new TextUndoRedoManager( _textArea, this);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportView( _textArea);
		getContentPane().add( scrollPane);

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
				on_file_save( null);
				break;
			case JOptionPane.CANCEL_OPTION:
			case JOptionPane.CLOSED_OPTION:
				requestFocus();
				break;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_window_closing(java.awt.event.WindowEvent)
	 */
	protected void on_window_closing(WindowEvent windowEvent) {
		if ( _modified) {
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
		dispose();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.edit.menu.file.IFileMenuHandler#on_file_new(java.awt.event.ActionEvent)
	 */
	public void on_file_new(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.edit.menu.file.IFileMenuHandler#on_file_open(java.awt.event.ActionEvent)
	 */
	public void on_file_open(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.edit.menu.file.IFileMenuHandler#on_file_close(java.awt.event.ActionEvent)
	 */
	public void on_file_close(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.edit.menu.file.IFileMenuHandler#on_file_save(java.awt.event.ActionEvent)
	 */
	public void on_file_save(ActionEvent actionEvent) {
		if ( null == _file || !_file.exists() || !_file.isFile() || !_file.canWrite())
			return;

		byte[] data = null;
		if ( null == _encoding || _encoding.equals( ""))
			data = _textArea.getText().getBytes();
		else {
			try {
				data = _textArea.getText().getBytes( _encoding);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return;
			}
		}

		if ( !FileUtility.write( data, _file.getAbsolutePath()))
			return;

		if ( null != _fileManagerCallBack)
			_fileManagerCallBack.modified( _fileManager);

		setTitle( _file.getName() + ( ( null == _encoding || _encoding.equals( "")) ? "" : ( " [" + _encoding + "]")));
		_textUndoRedoManager.clear();
		_modified = false;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.edit.menu.file.IFileMenuHandler#on_file_save_as(java.awt.event.ActionEvent)
	 */
	public void on_file_save_as(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.edit.menu.file.IFileMenuHandler#on_file_save_with_specified_encoding(java.awt.event.ActionEvent)
	 */
	public void on_file_save_with_specified_encoding(ActionEvent actionEvent) {
		EncodingSelectorDlg encodingSelectorDlg = new EncodingSelectorDlg( this, ResourceManager.get_instance().get( "encoding.selector.dialog.title"), true, _encoding);
		if ( !encodingSelectorDlg.do_modal( this))
			return;

		_encoding = encodingSelectorDlg._encoding;
		on_file_save( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.edit.menu.file.IFileMenuHandler#on_file_exit(java.awt.event.ActionEvent)
	 */
	public void on_file_exit(ActionEvent actionEvent) {
		if ( _modified) {
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
		dispose();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.edit.menu.edit.IEditMenuHandler#on_edit_undo(java.awt.event.ActionEvent)
	 */
	public void on_edit_undo(ActionEvent actionEvent) {
		_textUndoRedoManager.on_undo();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.edit.menu.edit.IEditMenuHandler#on_edit_redo(java.awt.event.ActionEvent)
	 */
	public void on_edit_redo(ActionEvent actionEvent) {
		_textUndoRedoManager.on_redo();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.undo.UndoManager)
	 */
	public void on_changed(UndoManager undoManager) {
		_modified = undoManager.canUndo();
		String title = _file.getName() + ( ( null == _encoding || _encoding.equals( "")) ? "" : ( " [" + _encoding + "]"));
		setTitle( title + ( !_modified ? "" : ( " " + ResourceManager.get_instance().get( "state.edit.modified"))));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.text.AbstractDocument.DefaultDocumentEvent, javax.swing.undo.UndoManager)
	 */
	public void on_changed(DefaultDocumentEvent defaultDocumentEvent, UndoManager undoManager) {
		on_changed( undoManager);
	}
}
