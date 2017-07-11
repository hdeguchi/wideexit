/**
 * 
 */
package soars.application.visualshell.object.gis.file.importer;

import java.awt.Component;
import java.awt.Frame;
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
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import soars.application.visualshell.common.menu.basic1.AppendAction;
import soars.application.visualshell.common.menu.basic1.RemoveAction;
import soars.application.visualshell.common.swing.ListBase;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.swing.gui.UserInterface;

/**
 * @author kurata
 *
 */
public class DirectoryList extends ListBase implements DropTargetListener {

	/**
	 * @param owner
	 * @param parent
	 */
	public DirectoryList(Frame owner, Component parent) {
		super(owner, parent);
	}

	/**
	 * @return
	 */
	public File[] get() {
		List<File> directories = new ArrayList<File>();
		for ( int i = 0; i < _defaultComboBoxModel.getSize(); ++i)
			directories.add( ( File)_defaultComboBoxModel.getElementAt( i));
		return directories.toArray( new File[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.swing.ListBase#setup(boolean, boolean)
	 */
	public boolean setup(boolean popup_menu, boolean single_selection) {
		if (!super.setup(popup_menu, single_selection))
			return false;

		new DropTarget( this, this);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.swing.ListBase#setup_popup_menu()
	 */
	protected void setup_popup_menu() {
		_userInterface = new UserInterface();
		_popupMenu = new JPopupMenu();

		_appendMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.append.menu"),
			new AppendAction( ResourceManager.get_instance().get( "common.popup.menu.append.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.append.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.append.stroke"));
		_removeMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.remove.menu"),
			new RemoveAction( ResourceManager.get_instance().get( "common.popup.menu.remove.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.remove.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.remove.stroke"));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.swing.ListBase#on_mouse_right_up(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
		if ( null == _userInterface)
			return;

		_removeMenuItem.setEnabled( true);

		if ( 0 == _defaultComboBoxModel.getSize()) {
			_removeMenuItem.setEnabled( false);
		} else {
			int index = locationToIndex( mouseEvent.getPoint());
			if ( 0 <= index && _defaultComboBoxModel.getSize() > index
				&& getCellBounds( index, index).contains( mouseEvent.getPoint())) {
				int[] indices = getSelectedIndices();
				boolean contains = ( 0 <= Arrays.binarySearch( indices, index));
				_removeMenuItem.setEnabled( contains);
			} else {
				_removeMenuItem.setEnabled( false);
			}
		}

		_popupMenu.show( this, mouseEvent.getX(), mouseEvent.getY());
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.swing.ListBase#on_append(java.awt.event.ActionEvent)
	 */
	public void on_append(ActionEvent actionEvent) {
		File[] directories = CommonTool.get_directories( Environment._importGisDataDirectoryKey, ResourceManager.get_instance().get( "import.gis.data.dialog.disrectory.list.directories.chooser.title"), _parent);
		if ( null == directories)
			return;

		append( directories);
	}

	/**
	 * @param directories
	 */
	private void append(File[] directories) {
		for ( File directory:directories) {
			if ( !directory.isDirectory()) {
				// ディレクトリではない場合のエラーメッセージ
				JOptionPane.showMessageDialog( this,
					ResourceManager.get_instance().get( "import.gis.data.dialog.not.directory.error.message") + "\n" + directory.getName(),
					ResourceManager.get_instance().get( "import.gis.data.dialog.title"),
					JOptionPane.ERROR_MESSAGE);
				return;
			}
			if ( 0 <= _defaultComboBoxModel.getIndexOf( directory)) {
				// 重複エラーメッセージ
				JOptionPane.showMessageDialog( this,
					ResourceManager.get_instance().get( "import.gis.data.dialog.duplicated.directory.error.message") + "\n" + directory.getName(),
					ResourceManager.get_instance().get( "import.gis.data.dialog.title"),
					JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		for ( File directory:directories)
			_defaultComboBoxModel.addElement( directory);

		int[] indices = new int[ directories.length];
		for ( int i = 0; i < indices.length; ++i)
			indices[ i] = _defaultComboBoxModel.getSize() - indices.length + i;
		clearSelection();
		setSelectedIndices( indices);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.swing.ListBase#on_remove(java.awt.event.ActionEvent)
	 */
	public void on_remove(ActionEvent actionEvent) {
		int[] indices = getSelectedIndices();
		if ( 1 > indices.length)
			return;

		Arrays.sort( indices);
		for ( int i = indices.length - 1; 0 <= i; --i)
			_defaultComboBoxModel.removeElementAt( indices[ i]);

		if ( 0 < _defaultComboBoxModel.getSize()) {
			int index = ( ( indices[ 0] < _defaultComboBoxModel.getSize()) ? indices[ 0] : _defaultComboBoxModel.getSize() - 1);
			setSelectedIndex( index);
		}
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
				List<File> list = ( List<File>)transferable.getTransferData( DataFlavor.javaFileListFlavor);
				if ( list.isEmpty()) {
					arg0.getDropTargetContext().dropComplete( true);
					return;
				}

				File[] directories =list.toArray( new File[ 0]);
				arg0.getDropTargetContext().dropComplete( true);
				append( directories);
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
