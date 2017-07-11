/**
 * 
 */
package soars.plugin.modelbuilder.chart.log_viewer.body.main;

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
import java.io.File;
import java.io.IOException;
import java.util.List;

import soars.common.utility.swing.message.MessageDlg;
import soars.plugin.modelbuilder.chart.chart.main.LogViewer;

/**
 * @author kurata
 *
 */
public class CustomLogViewer extends LogViewer implements DropTargetListener {

	/**
	 * 
	 */
	private Component _parent = null;

	/**
	 * 
	 */
	private Frame _owner = null;

	/**
	 * @param parent
	 * @param owner
	 */
	public CustomLogViewer(Component parent, Frame owner) {
		super();
		_parent = parent;
		_owner = owner;
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.LogViewer#setup()
	 */
	public boolean setup() {
		if ( !super.setup())
			return false;

		new DropTarget( this, this);
		//new DropTarget( routePanel, this);

		return true;
	}

	/**
	 * @param file
	 * @return
	 */
	public boolean read(File file) {
		clear( true);

		if ( !MessageDlg.execute( _owner, ResourceManager.get_instance().get( "application.title"), true,
			"on_file_open", ResourceManager.get_instance().get( "file.open.show.message"),
			new Object[] { file, this}, this, _parent))
			return false;

		repaint();

		return true;
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
				if ( 1 == list.size()) {
					File file =( File)list.get( 0);
					//showInformationMessageDialog( file.getAbsolutePath());
					arg0.getDropTargetContext().dropComplete( true);

					read( file);

				} else {
					//showWarningMessageDialog( "Too much elements.");
					arg0.getDropTargetContext().dropComplete( false);
				}
			} else {
				//showWarningMessageDialog( "Unsupported.");
				arg0.rejectDrop();
			}
		} catch (IOException ioe) {
			//showErrorMessageDialog( "I/O exception.");
			arg0.rejectDrop();
		} catch (UnsupportedFlavorException ufe) {
			//showErrorMessageDialog( "Unsupported");
			arg0.rejectDrop();
		}
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent arg0) {
	}
}
