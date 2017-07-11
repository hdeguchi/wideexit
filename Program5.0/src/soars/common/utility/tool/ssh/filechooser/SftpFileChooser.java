/*
 * Created on 2007/01/12
 */
package soars.common.utility.tool.ssh.filechooser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.swing.window.Dialog;
import soars.common.utility.tool.ssh.SshTool;

import com.sshtools.j2ssh.sftp.SftpFile;

/**
 * @author kurata
 */
public class SftpFileChooser extends Dialog implements IMessageCallback {

	/**
	 * Type value indicating that the <code>SftpFileChooser</code> supports an 
	 * "Open" file operation.
	 */
	public static final int OPEN_DIALOG = 0;

	/**
	 * Type value indicating that the <code>SftpFileChooser</code> supports a
	 * "Save" file operation.
	 */
	public static final int SAVE_DIALOG = 1;

	/**
	 * Type value indicating that the <code>SftpFileChooser</code> supports a
	 * developer-specified file operation.
	 */
	public static final int CUSTOM_DIALOG = 2;

	/** Instruction to display only files.
	 * 
	 */
	public static final int FILES_ONLY = 0;

	/** Instruction to display only directories.
	 * 
	 */
	public static final int DIRECTORIES_ONLY = 1;

	/** Instruction to display both files and directories.
	 * 
	 */
	public static final int FILES_AND_DIRECTORIES = 2;

	/**
	 * 
	 */
	private int _fileSelectionMode = FILES_ONLY;

	/**
	 * 
	 */
	private boolean _multiSelectionEnabled = false;

	/**
	 * 
	 */
	private SftpDirectoryComboBox _sftpDirectoryComboBox = null;

	/**
	 * 
	 */
	private JButton _upOneLevelButton = null;

	/**
	 * 
	 */
	static private ImageIcon _upOneLevelImageIcon = null;

	/**
	 * 
	 */
	private JButton _updateButton = null;

	/**
	 * 
	 */
	static private ImageIcon _updateImageIcon = null;

	/**
	 * 
	 */
	private JButton _newDirectoryButton = null;

	/**
	 * 
	 */
	static private ImageIcon _newDirectoryImageIcon = null;

	/**
	 * 
	 */
	private SftpFileList _sftpFileList = null;

	/**
	 * 
	 */
	private JScrollPane _sftpFileListScrollPane = null;

	/**
	 * 
	 */
	private JTextField _sftpFileTextField = null;

	/**
	 * 
	 */
	private String _rootDirectory = "/";

	/**
	 * 
	 */
	private String _homeDirectory = "";

	/**
	 * 
	 */
	private String _currentDirectory = _homeDirectory;

	/**
	 * 
	 */
	private String _host = "";

	/**
	 * 
	 */
	private String _username = "";

	/**
	 * 
	 */
	private String _password = "";

	/**
	 * 
	 */
	private boolean _setupCompleted = false;

	/**
	 * 
	 */
	public String _selectedFile = null;

	/**
	 * 
	 */
	private Dimension _minimumSize = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param host
	 * @param username
	 * @param password
	 * @param currentDirectory
	 * @param homeDirectory
	 * @throws HeadlessException
	*/
	public SftpFileChooser(Frame arg0, String arg1, String host, String username, String password, String currentDirectory, String homeDirectory) throws HeadlessException {
		super(arg0, arg1, true);
		_host = host;
		_username = username;
		_password = password;
		_currentDirectory = currentDirectory;
		_homeDirectory = homeDirectory;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param host
	 * @param username
	 * @param password
	 * @param currentDirectory
	 * @param homeDirectory
	 * @param rootDirectory
	 * @throws HeadlessException
	*/
	public SftpFileChooser(Frame arg0, String arg1, String host, String username, String password, String currentDirectory, String homeDirectory, String rootDirectory) throws HeadlessException {
		super(arg0, arg1, true);
		_host = host;
		_username = username;
		_password = password;
		_currentDirectory = currentDirectory;
		_homeDirectory = homeDirectory;
		_rootDirectory = rootDirectory;
	}

	/**
	 * @return
	*/
	public int getFileSelectionMode() {
		return _fileSelectionMode;
	}

	/**
	 * @param mode
	*/
	public void setFileSelectionMode(int mode) {
		if ( _fileSelectionMode == mode)
			return;

		if ( ( FILES_ONLY == mode) || (DIRECTORIES_ONLY == mode) || (FILES_AND_DIRECTORIES == mode))
			_fileSelectionMode = mode;
		else
			throw new IllegalArgumentException( "Incorrect Mode for Dialog: " + mode);
	}

	/**
	 * @return
	*/
	public boolean isFileSelectionEnabled() {
		return ( ( FILES_ONLY == _fileSelectionMode) || ( FILES_AND_DIRECTORIES == _fileSelectionMode));
	}

	/**
	 * @return
	*/
	public boolean isDirectorySelectionEnabled() {
		return ( ( DIRECTORIES_ONLY == _fileSelectionMode) || ( FILES_AND_DIRECTORIES == _fileSelectionMode));
	}

	/**
	 * @param name
	 * @return
	 */
	public boolean mkdir(String name) {
		String new_directory = ( ( String)_sftpDirectoryComboBox.getSelectedItem() + "/" + name);

		if ( !MessageDlg.execute( ( Frame)getOwner(), getTitle(), true,
			"mkdir", ResourceManager.get_instance().get( "sftp.file.chooser.message.new.folder"),
			new Object[] { new_directory}, this, this))
			return false;

		update( ( String)_sftpDirectoryComboBox.getSelectedItem());

		setEnabled( false);

		if ( _sftpFileList.select( new_directory))
			_sftpFileTextField.setText( new_directory);

		setEnabled( true);

		return true;
	}

	/**
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public boolean rename(String oldPath, String newPath) {
		if ( !MessageDlg.execute( ( Frame)getOwner(), getTitle(), true,
			"rename", ResourceManager.get_instance().get( "sftp.file.chooser.message.rename"),
			new Object[] { oldPath, newPath}, this, this))
			return false;

		update( ( String)_sftpDirectoryComboBox.getSelectedItem());

		setEnabled( false);

		if ( _sftpFileList.select( newPath))
			_sftpFileTextField.setText( newPath);

		setEnabled( true);

		return true;
	}

	/**
	 * @param path
	 * @param index
	 * @return
	 */
	public boolean delete(String path, int index) {
		if ( !MessageDlg.execute( ( Frame)getOwner(), getTitle(), true,
			"delete", ResourceManager.get_instance().get( "sftp.file.chooser.message.delete"),
			new Object[] { path}, this, this))
			return false;

		update( ( String)_sftpDirectoryComboBox.getSelectedItem());

		setEnabled( false);

		if ( 0 < _sftpFileList.getModel().getSize()) {
			index = ( ( _sftpFileList.getModel().getSize() > index) ? index : ( _sftpFileList.getModel().getSize() - 1));
			_sftpFileList.setSelectedIndex( index);
			SftpFile sftpFile = ( SftpFile)_sftpFileList.getModel().getElementAt( index);
			_sftpFileTextField.setText( sftpFile.getAbsolutePath());
		}

		setEnabled( true);

		return true;
	}

	/**
	 * @param sftpFile
	 * @param move
	 */
	public void update(SftpFile sftpFile, boolean move) {
		if ( !_setupCompleted)
			return;

		String directory = sftpFile.getAbsolutePath().replaceAll( "//", "/");
		if ( move)
			update( directory);
		else
			_sftpFileTextField.setText( directory);
	}

	/**
	 * @param directory
	 */
	public void update(String directory) {
		if ( !_setupCompleted)
			return;

		setEnabled( false);

		_upOneLevelButton.setEnabled( !directory.equals( _rootDirectory));

		if ( _sftpDirectoryComboBox.update( directory, _rootDirectory)
			&& _sftpFileList.update( _host, _username, _password, directory,
				isFileSelectionEnabled(), isDirectorySelectionEnabled()))
			_currentDirectory = directory;
		else {
			if ( !_sftpDirectoryComboBox.update( _currentDirectory, _rootDirectory)
				|| !_sftpFileList.update( _host, _username, _password, _currentDirectory,
					isFileSelectionEnabled(), isDirectorySelectionEnabled())) {
				_sftpDirectoryComboBox.update( _homeDirectory, _rootDirectory);
				_sftpFileList.update( _host, _username, _password, _homeDirectory,
					isFileSelectionEnabled(), isDirectorySelectionEnabled());
				_currentDirectory = _homeDirectory;
			}
		}

		SftpFile sftpFile = ( SftpFile)_sftpFileList.getSelectedValue();
		if ( null == sftpFile) {
			_sftpFileTextField.setText( directory);
			setEnabled( true);
			return;
		}

		_sftpFileTextField.setText( sftpFile.getAbsolutePath());

		setEnabled( true);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IMessageCallback#message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.MessageDlg)
	 */
	public boolean message_callback(String id, Object[] objects, MessageDlg messageDlg) {
		if ( id.equals( "mkdir"))
			return SshTool.mkdirs( _host, _username, _password, ( String)objects[ 0]);
		else if ( id.equals( "rename"))
			return SshTool.rename( _host, _username, _password, ( String)objects[ 0], ( String)objects[ 1]);
		else if ( id.equals( "delete"))
			return SshTool.delete( _host, _username, _password, ( String)objects[ 0], true);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				if ( null == _minimumSize)
					return;

				int width = getSize().width;
				int height = getSize().height;
				setSize( ( _minimumSize.width > width) ? _minimumSize.width : width,
					( _minimumSize.height > height) ? _minimumSize.height : height);
			}
		});


		getContentPane().setLayout( new BorderLayout());



		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		if ( !setup_sftpDirectoryComboBox( northPanel))
			return false;

		insert_horizontal_glue( northPanel);

		getContentPane().add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		if ( !setup_sftpFileList( centerPanel))
			return false;

		getContentPane().add( centerPanel);



		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( southPanel);

		setup_sftpFile_textField( southPanel);

		insert_horizontal_glue( southPanel);

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));
		setup_ok_and_cancel_button(
			panel,
			ResourceManager.get_instance().get( "dialog.open"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			false, false);
		southPanel.add( panel);

		insert_horizontal_glue( southPanel);

		getContentPane().add( southPanel, "South");



		setDefaultCloseOperation( DISPOSE_ON_CLOSE);


		Toolkit.getDefaultToolkit().setDynamicLayout( true);


		adjust();


		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_sftpDirectoryComboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_sftpDirectoryComboBox = new SftpDirectoryComboBox( this);
		if ( !_sftpDirectoryComboBox.setup())
			return false;

		panel.add( _sftpDirectoryComboBox);


		panel.add( Box.createHorizontalStrut( 5));


		if ( null == _upOneLevelImageIcon)
			_upOneLevelImageIcon = new ImageIcon( SftpFileChooser.class.getResource(
				"/soars/common/utility/tool/ssh/filechooser/resource/image/icon/up_one_level.png"));

		_upOneLevelButton = new JButton( _upOneLevelImageIcon);
		_upOneLevelButton.setPreferredSize(
			new Dimension( _upOneLevelImageIcon.getIconWidth() + 8,
				_upOneLevelButton.getPreferredSize().height));
		_upOneLevelButton.setToolTipText( ResourceManager.get_instance().get( "sftp.file.chooser.up.one.level.button.tool.tip.text"));
		_upOneLevelButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_sftpDirectoryComboBox.up_one_level();
			}
		});
		panel.add( _upOneLevelButton);


		panel.add( Box.createHorizontalStrut( 5));


		if ( null == _updateImageIcon)
			_updateImageIcon = new ImageIcon( SftpFileChooser.class.getResource(
				"/soars/common/utility/tool/ssh/filechooser/resource/image/icon/update.png"));

		_updateButton = new JButton( _updateImageIcon);
		_updateButton.setPreferredSize(
			new Dimension( _updateImageIcon.getIconWidth() + 8,
				_updateButton.getPreferredSize().height));
		_updateButton.setToolTipText( ResourceManager.get_instance().get( "sftp.file.chooser.update.button.tool.tip.text"));
		_updateButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_update( e);
			}
		});
		panel.add( _updateButton);


		panel.add( Box.createHorizontalStrut( 5));


		if ( null == _newDirectoryImageIcon)
			_newDirectoryImageIcon = new ImageIcon( SftpFileChooser.class.getResource(
				"/soars/common/utility/tool/ssh/filechooser/resource/image/icon/new_directory.png"));

		_newDirectoryButton = new JButton( _newDirectoryImageIcon);
		_newDirectoryButton.setPreferredSize(
			new Dimension( _newDirectoryImageIcon.getIconWidth() + 8,
				_newDirectoryButton.getPreferredSize().height));
		_newDirectoryButton.setToolTipText( ResourceManager.get_instance().get( "sftp.file.chooser.new.folder.button.tool.tip.text"));
		_newDirectoryButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_sftpFileList.on_new( null);
			}
		});
		panel.add( _newDirectoryButton);


		panel.add( Box.createHorizontalStrut( 5));


		parent.add( panel);

		return true;
	}

	/**
	 * @param actionEvent
	 */
	protected void on_update(ActionEvent actionEvent) {
		String directory = _sftpFileTextField.getText();

		update( ( String)_sftpDirectoryComboBox.getSelectedItem());

		if ( null != directory) {
			if ( _sftpFileList.select( directory))
				_sftpFileTextField.setText( directory);
		}
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_sftpFileList(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_sftpFileList = new SftpFileList( _multiSelectionEnabled, ( Frame)getOwner(), this);
		if ( !_sftpFileList.setup())
			return false;

		panel.add( _sftpFileList);

		_sftpFileListScrollPane = new JScrollPane();
		_sftpFileListScrollPane.getViewport().setView( _sftpFileList);
		panel.add( _sftpFileListScrollPane);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 * 
	 */
	private void setup_sftpFile_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_sftpFileTextField = new JTextField();
		_sftpFileTextField.setEditable( false);
		panel.add( _sftpFileTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		_sftpFileListScrollPane.setPreferredSize( new Dimension( 600, 200));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		String parentDirectory = get_parent_directory( _currentDirectory);
		update( parentDirectory);

		_sftpDirectoryComboBox.update( parentDirectory, _rootDirectory);
		_sftpFileList.update( _host, _username, _password, parentDirectory,
			isFileSelectionEnabled(), isDirectorySelectionEnabled());

		_sftpFileList.select( _currentDirectory);

		_sftpFileTextField.setText( _currentDirectory);

		_upOneLevelButton.setEnabled( !parentDirectory.equals( _rootDirectory));

		_setupCompleted = true;

		_minimumSize = getSize();
	}

	/**
	 * @param directory
	 * @return
	 */
	private String get_parent_directory(String directory) {
		if ( directory.equals( _rootDirectory))
			return directory;

		String[] names = directory.split( "/");
		if ( 2 >= names.length)
			return "/";

		String result = "";
		for ( int i = 1; i < names.length - 1; ++i)
			result += ( "/" + names[ i]);

		return result;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		_selectedFile = _sftpFileTextField.getText();
		super.on_ok(actionEvent);
	}
}
