/**
 * 
 */
package soars.common.utility.tool.ssh2.filechooser;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.event.MouseInputAdapter;

import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.tool.ssh.filechooser.ResourceManager;
import soars.common.utility.tool.ssh2.SshTool2;
import soars.common.utility.tool.ssh2.filechooser.menu.DeleteAction;
import soars.common.utility.tool.ssh2.filechooser.menu.NewAction;
import soars.common.utility.tool.ssh2.filechooser.menu.RenameAction;

import com.sshtools.j2ssh.sftp.SftpFile;

/**
 * @author kurata
 *
 */
public class SftpFileList extends JList {

	/**
	 * 
	 */
	private boolean _multiSelectionEnabled = false;

	/**
	 * 
	 */
	private DefaultComboBoxModel _defaultComboBoxModel = null;

	/**
	 * 
	 */
	private Frame _owner = null;

	/**
	 * 
	 */
	private SftpFileChooser _sftpFileChooser = null;

	/**
	 * 
	 */
	protected UserInterface _userInterface = null;

	/**
	 * 
	 */
	protected JPopupMenu _popupMenu = null;

	/**
	 * 
	 */
	protected JMenuItem _newMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _renameMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _deleteMenuItem = null;

	/**
	 * @param selectionEnabled
	 * @param owner
	 * @param sftpFileChooser
	 * 
	 */
	public SftpFileList(boolean selectionEnabled, Frame owner, SftpFileChooser sftpFileChooser) {
		super();
		_multiSelectionEnabled = selectionEnabled;
		_owner = owner;
		_sftpFileChooser = sftpFileChooser;
	}

	/**
	 * @return
	 */
	public boolean setup() {
		setSelectionMode( ListSelectionModel.SINGLE_SELECTION);

		_defaultComboBoxModel = new DefaultComboBoxModel();
		setModel( _defaultComboBoxModel);

		addMouseListener( new MouseInputAdapter() {
			public void mouseClicked( MouseEvent arg0) {
				if ( SwingTool.is_mouse_left_button( arg0)) {
					if ( 1 == arg0.getClickCount())
						on_mouse_left_down( arg0);
					else if ( 2 == arg0.getClickCount())
						on_mouse_left_double_click( arg0);
				}
			}
			public void mouseReleased(MouseEvent arg0) {
				if ( SwingTool.is_mouse_right_button( arg0))
					on_mouse_right_up( arg0);
			}
		});

		addKeyListener( new KeyListener() {
			public void keyPressed(KeyEvent arg0) {
				on_key_pressed( arg0);
			}
			public void keyReleased(KeyEvent arg0) {
			}
			public void keyTyped(KeyEvent arg0) {
			}
		});

		setup_popup_menu();

		setCellRenderer( new SftpFileListCellRenderer());

		return true;
	}

	/**
	 * 
	 */
	private void setup_popup_menu() {
		_userInterface = new UserInterface();
		_popupMenu = new JPopupMenu();

		_newMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "sftp.file.list.popup.menu.new.folder.menu"),
			new NewAction( ResourceManager.get_instance().get( "sftp.file.list.popup.menu.new.folder.menu"), this),
			ResourceManager.get_instance().get( "sftp.file.list.popup.menu.new.folder.mnemonic"),
			ResourceManager.get_instance().get( "sftp.file.list.popup.menu.new.folder.stroke"));
		_renameMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "sftp.file.list.popup.menu.rename.menu"),
			new RenameAction( ResourceManager.get_instance().get( "sftp.file.list.popup.menu.rename.menu"), this),
			ResourceManager.get_instance().get( "sftp.file.list.popup.menu.rename.mnemonic"),
			ResourceManager.get_instance().get( "sftp.file.list.popup.menu.rename.stroke"));
		_deleteMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "sftp.file.list.popup.menu.delete.menu"),
			new DeleteAction( ResourceManager.get_instance().get( "sftp.file.list.popup.menu.delete.menu"), this),
			ResourceManager.get_instance().get( "sftp.file.list.popup.menu.delete.mnemonic"),
			ResourceManager.get_instance().get( "sftp.file.list.popup.menu.delete.stroke"));
	}

	/**
	 * @param actionEvent
	 */
	public void on_new(ActionEvent actionEvent) {
		EditSftpFileDlg editSftpFileDlg = new EditSftpFileDlg(
			_owner,
			ResourceManager.get_instance().get( "sftp.file.dialog.title.new.folder"),
			true,
			ResourceManager.get_instance().get( "sftp.file.dialog.label.new.folder"));
		if ( !editSftpFileDlg.do_modal( _sftpFileChooser))
			return;

		if ( !_sftpFileChooser.mkdir( editSftpFileDlg._name)) {
			JOptionPane.showMessageDialog(
				_owner,
				ResourceManager.get_instance().get( "sftp.file.list.error.message.new.folder"),
				ResourceManager.get_instance().get( "sftp.file.dialog.title.new.folder"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	/**
	 * @param actionEvent
	 */
	public void on_rename(ActionEvent actionEvent) {
		int[] indices = getSelectedIndices();
		if ( 1 != indices.length)
			return;

		SftpFile sftpFile = ( SftpFile)_defaultComboBoxModel.getElementAt( indices[ 0]);
		if ( null == sftpFile)
			return;

		String name = sftpFile.getFilename();
		if ( null == name)
			return;

		EditSftpFileDlg editSftpFileDlg = new EditSftpFileDlg(
			_owner,
			ResourceManager.get_instance().get( "sftp.file.dialog.title.rename"),
			true,
			name,
			ResourceManager.get_instance().get( "sftp.file.dialog.label.rename"));
		if ( !editSftpFileDlg.do_modal( _sftpFileChooser))
			return;

		String oldPath = sftpFile.getAbsolutePath();
		String newPath = ( sftpFile.getAbsolutePath().substring( 0, sftpFile.getAbsolutePath().length() - sftpFile.getFilename().length()) + editSftpFileDlg._name);
		if ( !_sftpFileChooser.rename( oldPath, newPath)) {
			JOptionPane.showMessageDialog(
				_owner,
				ResourceManager.get_instance().get( "sftp.file.list.error.message.rename"),
				ResourceManager.get_instance().get( "sftp.file.dialog.title.rename"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	/**
	 * @param actionEvent
	 */
	public void on_delete(ActionEvent actionEvent) {
		int[] indices = getSelectedIndices();
		if ( 1 != indices.length)
			return;

		SftpFile sftpFile = ( SftpFile)_defaultComboBoxModel.getElementAt( indices[ 0]);
		if ( null == sftpFile)
			return;

		if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
			_owner,
			ResourceManager.get_instance().get( "sftp.file.list.confirm.delete.message"),
			ResourceManager.get_instance().get( "sftp.file.list.confirm.delete.dialog.title"),
			JOptionPane.YES_NO_OPTION)) {
			if ( !_sftpFileChooser.delete( sftpFile.getAbsolutePath(), indices[ 0])) {
				JOptionPane.showMessageDialog(
					_owner,
					ResourceManager.get_instance().get( "sftp.file.list.error.message.delete"),
					ResourceManager.get_instance().get( "sftp.file.list.confirm.delete.dialog.title"),
					JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_left_down(MouseEvent mouseEvent) {
		int index = getSelectedIndex();
		if ( 0 == _defaultComboBoxModel.getSize() || -1 == index)
			return;

		if ( !getCellBounds( index, index).contains( mouseEvent.getPoint()))
			return;

		on_selected( null, false);
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_left_double_click(MouseEvent mouseEvent) {
		int index = getSelectedIndex();
		if ( 0 == _defaultComboBoxModel.getSize() || -1 == index)
			return;

		if ( !getCellBounds( index, index).contains( mouseEvent.getPoint()))
			return;

		on_selected( null, true);
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
		if ( null == _userInterface)
			return;

		_newMenuItem.setEnabled( true);
		_renameMenuItem.setEnabled( true);
		_deleteMenuItem.setEnabled( true);

		//int index = getSelectedIndex();
		if ( 0 == _defaultComboBoxModel.getSize() /*|| -1 == index*/) {
			_renameMenuItem.setEnabled( false);
			_deleteMenuItem.setEnabled( false);
		} else {
			int index = locationToIndex( mouseEvent.getPoint());
			if ( 0 <= index && _defaultComboBoxModel.getSize() > index
				&& getCellBounds( index, index).contains( mouseEvent.getPoint())) {
				//int[] indices = getSelectedIndices();
				//boolean contains = ( 0 <= Arrays.binarySearch( indices, index));
				setSelectedIndex( index);
				//_rename_menuItem.setEnabled( 1 == indices.length && contains);
				//_delete_menuItem.setEnabled( contains);
			} else {
				_renameMenuItem.setEnabled( false);
				_deleteMenuItem.setEnabled( false);
			}
		}

		on_selected( null, false);

		_popupMenu.show( this, mouseEvent.getX(), mouseEvent.getY());
	}

	/**
	 * @param keyEvent
	 */
	protected void on_key_pressed(KeyEvent keyEvent) {
		int index = getSelectedIndex();
		if ( 0 == _defaultComboBoxModel.getSize() || -1 == index)
			return;

		switch ( keyEvent.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				on_selected( null, true);
				break;
			case KeyEvent.VK_DELETE:
			case KeyEvent.VK_BACK_SPACE:
				on_delete( null);
				break;
		}
	}

	/**
	 * @param actionEvent
	 * @param move
	 */
	private void on_selected(ActionEvent actionEvent, boolean move) {
		int[] indices = getSelectedIndices();
		if ( 1 != indices.length)
			return;

		SftpFile sftpFile = ( SftpFile)_defaultComboBoxModel.getElementAt( indices[ 0]);
		if ( !sftpFile.isDirectory())
			return;

		_sftpFileChooser.update( sftpFile, move);
	}

	/**
	 * @param host
	 * @param username
	 * @param keyFilename
	 * @param directory
	 * @param fileSelectionEnabled
	 * @param directorySelectionEnabled
	 * @return
	 */
	public boolean update(String host, String username, String keyFilename, String directory,
		boolean fileSelectionEnabled, boolean directorySelectionEnabled) {
		List sftpFileList = SshTool2.ls( host, username, new File( keyFilename), directory);
		if ( null == sftpFileList)
			return false;

		SftpFile[] sftpFiles = ( SftpFile[])sftpFileList.toArray( new SftpFile[ 0]);
		Arrays.sort( sftpFiles, new SftpFilenameComparer());

		_defaultComboBoxModel.removeAllElements();

		if ( directorySelectionEnabled)
			update( sftpFiles, "directory");

		if ( fileSelectionEnabled)
			update( sftpFiles, "file");

		return true;
	}

	/**
	 * @param sftpFiles
	 * @param type
	 */
	private void update(SftpFile[] sftpFiles, String type) {
		for ( int i = 0; i < sftpFiles.length; ++i) {
			if ( sftpFiles[ i].getFilename().startsWith( "."))
				continue;

			if ( type.equals( "directory") && !sftpFiles[ i].isDirectory())
				continue;

			if ( type.equals( "file") && !sftpFiles[ i].isFile())
				continue;

			_defaultComboBoxModel.addElement( sftpFiles[ i]);
		}
	}

	/**
	 * @param directory
	 * @return
	 */
	public boolean select(String directory) {
		for ( int i = 0; i < _defaultComboBoxModel.getSize(); ++i) {
			SftpFile sftpFile = ( SftpFile)_defaultComboBoxModel.getElementAt( i);
			String path = sftpFile.getAbsolutePath().replaceAll( "//", "/");
			if ( directory.equals( path)) {
				setSelectedIndex( i);
				return true;
			}
		}
		return false;
	}
}
