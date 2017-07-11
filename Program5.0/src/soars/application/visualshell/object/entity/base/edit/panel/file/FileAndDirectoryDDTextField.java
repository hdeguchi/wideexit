/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.file;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import soars.application.visualshell.layer.LayerManager;
import soars.common.utility.swing.dnd.text.DDTextField;
import soars.common.utility.swing.file.manager.table.FileItemTransferable;
import soars.common.utility.swing.file.manager.tree.DirectoryNodeTransferable;

/**
 * @author kurata
 *
 */
public class FileAndDirectoryDDTextField extends DDTextField {

	/**
	 * 
	 */
	public FileAndDirectoryDDTextField() {
		super();
	}

	/**
	 * @param file
	 */
	private void setText(File file) {
		File user_data_directory = LayerManager.get_instance().get_user_data_directory();
		if ( null == user_data_directory)
			return;

		if ( !file.getAbsolutePath().replaceAll( "\\\\", "/").startsWith( user_data_directory.getAbsolutePath().replaceAll( "\\\\", "/")))
			return;

		setText( file.getAbsolutePath().replaceAll( "\\\\", "/").substring( user_data_directory.getAbsolutePath().replaceAll( "\\\\", "/").length() + 1)
			+ ( file.isDirectory() ? "/" : ""));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.dnd.text.DDTextField#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	@Override
	public void dragEnter(DropTargetDragEvent arg0) {
		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.dnd.text.DDTextField#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragExit(DropTargetEvent arg0) {
		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.dnd.text.DDTextField#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	@Override
	public void dragOver(DropTargetDragEvent arg0) {
    DataFlavor[] dataFlavors = arg0.getCurrentDataFlavors();
		if ( dataFlavors[ 0].getHumanPresentableName().equals( DirectoryNodeTransferable._name))
			arg0.acceptDrag( DnDConstants.ACTION_COPY);
		else if ( dataFlavors[ 0].getHumanPresentableName().equals( FileItemTransferable._name)) {
			try {
				File[] files = ( File[])arg0.getTransferable().getTransferData( FileItemTransferable._localObjectFlavor);
				if ( null == files || 1 != files.length || !files[ 0].exists()) {
					arg0.rejectDrag();
					repaint();
					return;
				}

				arg0.acceptDrag( DnDConstants.ACTION_COPY);
			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
				arg0.rejectDrag();
			} catch (IOException e) {
				e.printStackTrace();
				arg0.rejectDrag();
			}
		} else
			arg0.rejectDrag();

		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.dnd.text.DDTextField#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	@Override
	public void drop(DropTargetDropEvent arg0) {
		try {
			DataFlavor[] dataFlavors = arg0.getCurrentDataFlavors();
			if ( dataFlavors[ 0].getHumanPresentableName().equals( DirectoryNodeTransferable._name)) {
				TreeNode draggedTreeNode = ( TreeNode)arg0.getTransferable().getTransferData( DirectoryNodeTransferable._localObjectFlavor);
				arg0.getDropTargetContext().dropComplete( true);
				if ( null == draggedTreeNode) {
					arg0.rejectDrop();
					repaint();
					return;
				}

				File file = ( File)( ( DefaultMutableTreeNode)draggedTreeNode).getUserObject();
				if ( null == file || !file.exists()) {
					arg0.rejectDrop();
					repaint();
					return;
				}

				arg0.acceptDrop( DnDConstants.ACTION_COPY);
				setText( file);
			} else if ( dataFlavors[ 0].getHumanPresentableName().equals( FileItemTransferable._name)) {
				File[] files = ( File[])arg0.getTransferable().getTransferData( FileItemTransferable._localObjectFlavor);
				arg0.getDropTargetContext().dropComplete( true);
				if ( null == files || 1 != files.length || !files[ 0].exists()) {
					arg0.rejectDrop();
					repaint();
					return;
				}

				arg0.acceptDrop( DnDConstants.ACTION_COPY);
				setText( files[ 0]);
			} else {
				arg0.rejectDrop();
			}
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
			arg0.rejectDrop();
		} catch (IOException e) {
			e.printStackTrace();
			arg0.rejectDrop();
		}

		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.dnd.text.DDTextField#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	@Override
	public void dropActionChanged(DropTargetDragEvent arg0) {
		repaint();
	}
}
