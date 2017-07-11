/**
 * 
 */
package soars.application.manager.library.main.tab.common.table;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JMenuItem;

import soars.application.manager.library.main.Constant;
import soars.application.manager.library.main.ResourceManager;
import soars.application.manager.library.main.tab.common.menu.IContextMenuHandler;
import soars.application.manager.library.main.tab.common.menu.UpdateAnnotationFileAction;
import soars.application.manager.library.main.tab.tab.InternalTabbedPane;
import soars.common.utility.swing.file.manager.IFileManager;
import soars.common.utility.swing.file.manager.IFileManagerCallBack;
import soars.common.utility.swing.file.manager.table.FileTableBase;
import soars.common.utility.swing.file.manager.table.FileTableSpecificRowRendererBase;
import soars.common.utility.swing.file.manager.tree.DirectoryTreeBase;

/**
 * @author kurata
 *
 */
public class FileTable extends FileTableBase implements IContextMenuHandler {

	/**
	 * 
	 */
	protected JMenuItem _updateAnnotationFileMenuItem = null;

	/**
	 * 
	 */
	private InternalTabbedPane _internalTabbedPane = null;

	/**
	 * @param fileManagerCallBack
	 * @param fileManager 
	 * @param component
	 * @param owner
	 * @param parent
	 */
	public FileTable(IFileManagerCallBack fileManagerCallBack, IFileManager fileManager, Component component, Frame owner, Component parent) {
		super(fileManagerCallBack, fileManager, component, new String[] { ".mdf", ".madf"}, owner, parent);
	}

	/**
	 * @param fileTableSpecificRowRendererBase
	 * @param directoryTreeBase
	 * @param internalTabbedPane
	 * @return
	 */
	public boolean setup(FileTableSpecificRowRendererBase fileTableSpecificRowRendererBase, DirectoryTreeBase directoryTreeBase, InternalTabbedPane internalTabbedPane) {
		if (!super.setup(fileTableSpecificRowRendererBase, directoryTreeBase))
			return false;

		_internalTabbedPane = internalTabbedPane;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.table.FileTableBase#setup_popup_menu()
	 */
	protected void setup_popup_menu() {
		super.setup_popup_menu();

		_popupMenu.addSeparator();

		_updateAnnotationFileMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "popup.menu.update.annotation.file.menu"),
			new UpdateAnnotationFileAction( ResourceManager.get_instance().get( "popup.menu.update.annotation.file.menu"), this),
			ResourceManager.get_instance().get( "popup.menu.update.annotation.file.mnemonic"),
			ResourceManager.get_instance().get( "popup.menu.update.annotation.file.stroke"));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.table.FileTableBase#on_mouse_pressed(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_pressed(MouseEvent mouseEvent) {
		super.on_mouse_pressed(mouseEvent);
		update_internalTabbedPane();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.table.FileTableBase#on_mouse_right_up(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
		update_internalTabbedPane();
		_updateAnnotationFileMenuItem.setEnabled( false);
		if ( 0 < getRowCount()) {
			Point point = mouseEvent.getPoint();
			int row = rowAtPoint( point);
			if ( ( 0 <= row && getRowCount() > row)) {
				int[] rows = getSelectedRows();
				File file = ( File)getValueAt( row, 0);
				boolean isOnLabel = _fileTableSpecificRowRendererBase.is_on_label( file, point, this);
				if ( isOnLabel && 0 > Arrays.binarySearch( rows, row))
					setRowSelectionInterval( row, row);
				_updateAnnotationFileMenuItem.setEnabled( isOnLabel);
			}
		}

		super.on_mouse_right_up(mouseEvent);
	}

	/**
	 * 
	 */
	private void update_internalTabbedPane() {
		// もし選択されているファイルの数が複数またはjarファイルではない場合は_internalTabbedPaneを空にする必要がある
		int[] rows = getSelectedRows();
		if ( null != rows && 1 != rows.length)
			_internalTabbedPane.update( ( File)null, true);
		else {
			try {
				_internalTabbedPane.update( ( File)getValueAt( rows[ 0], 0), true);
			} catch (ArrayIndexOutOfBoundsException e) {
				_internalTabbedPane.update( ( File)null, true);
				return;
			}
		}
		_internalTabbedPane.repaint();
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.library.main.tab.common.menu.IContextMenuHandler#on_edit_module(java.awt.event.ActionEvent)
	 */
	public void on_edit_module(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.library.main.tab.common.menu.IContextMenuHandler#on_enable_module(java.awt.event.ActionEvent)
	 */
	public void on_enable_module(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.library.main.tab.common.menu.IContextMenuHandler#on_disable_module(java.awt.event.ActionEvent)
	 */
	public void on_disable_module(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.library.main.tab.common.menu.IContextMenuHandler#on_update_annotation_file(java.awt.event.ActionEvent)
	 */
	public void on_update_annotation_file(ActionEvent actionEvent) {
		// TODO Auto-generated method stub
		int[] rows = getSelectedRows();
		if ( null == rows || 0 == rows.length)
			return;

		File[] files = new File[ rows.length];
		for ( int i = 0; i < rows.length; ++i)
			files[ i] = ( File)getValueAt( rows[ i], 0);

		_internalTabbedPane.update( files);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.table.FileTableBase#get_files(java.io.File[])
	 */
	protected File[] get_files(File[] files) {
		// TODO Auto-generated method stub
		if ( null == files || 0 == files.length)
			return files;

		List<File> list = new ArrayList<File>( Arrays.asList( files));
		for ( File file:files) {
			if ( file.isFile() && file.getName().toLowerCase().endsWith( ".jar")) {
				File annotationFile = new File( file.getParent(), file.getName().substring( 0, file.getName().length() - ".jar".length()) + Constant._moduleAnnotationFileExtension);
				if ( file.exists() && file.isFile() && !list.contains( annotationFile))
					list.add( annotationFile);
			}
		}
		return list.toArray( new File[ 0]);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#changeSelection(int, int, boolean, boolean)
	 */
	public void changeSelection(int arg0, int arg1, boolean arg2, boolean arg3) {
		super.changeSelection(arg0, arg1, arg2, arg3);

		update_internalTabbedPane();
//		int[] rows = getSelectedRows();
//		if ( null == rows)
//			return;
//
////		int[] columns = getSelectedColumns();
////		if ( null == columns || 1 != columns.length || 0 != columns[ 0])
////			return;
//
//		if ( 1 != rows.length)
//			_internalTabbedPane.update( ( File)null, true);
//		else {
//			try {
//				File file = ( File)getValueAt( rows[ 0], 0);
//				if ( null == file)
//					return;
//
//				_internalTabbedPane.update( file, true);
//
//			} catch (ArrayIndexOutOfBoundsException e) {
//				return;
//			}
//		}
	}

//	/* (non-Javadoc)
//	 * @see soars.common.utility.swing.file.manager.table.FileTableBase#select(java.io.File)
//	 */
//	public void select(File file) {
//		super.select(file);
//		_internalTabbedPane.update( file);
//	}
//
//	/* (non-Javadoc)
//	 * @see soars.common.utility.swing.file.manager.table.FileTableBase#select(java.io.File[])
//	 */
//	public void select(File[] files) {
//		super.select(files);
//		_internalTabbedPane.update( ( File)null);
//	}
}
