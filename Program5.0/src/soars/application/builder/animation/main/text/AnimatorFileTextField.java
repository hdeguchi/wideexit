/**
 * 
 */
package soars.application.builder.animation.main.text;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import soars.common.utility.swing.dnd.text.DDTextField;

/**
 * @author kurata
 *
 */
public class AnimatorFileTextField extends DDTextField {

	/**
	 * 
	 */
	public AnimatorFileTextField() {
		super();
	}

	/**
	 * @param arg0
	 */
	public AnimatorFileTextField(String arg0) {
		super(arg0);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.dnd.text.DDTextField#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent arg0) {
		arg0.acceptDrag( DnDConstants.ACTION_COPY);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.dnd.text.DDTextField#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent arg0) {
		arg0.acceptDrag( DnDConstants.ACTION_COPY_OR_MOVE);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.dnd.text.DDTextField#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent arg0) {
		arg0.acceptDrag( DnDConstants.ACTION_COPY);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.dnd.text.DDTextField#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent arg0) {
		try {
			Transferable transferable = arg0.getTransferable();
			if ( transferable.isDataFlavorSupported( DataFlavor.javaFileListFlavor)) {
				arg0.acceptDrop( DnDConstants.ACTION_COPY_OR_MOVE);
				List list = ( List)transferable.getTransferData( DataFlavor.javaFileListFlavor);
				if ( list.isEmpty())
					return;

				File file =( File)list.get( 0);
				if ( !file.exists() || !file.isFile() || !file.canRead())
					return;

				setText( file.getAbsolutePath().replaceAll( "\\\\", "/"));

				arg0.getDropTargetContext().dropComplete( true);
			} else if ( transferable.isDataFlavorSupported( DataFlavor.stringFlavor)) {
				arg0.acceptDrop( DnDConstants.ACTION_COPY_OR_MOVE);
				String string = ( String)transferable.getTransferData( DataFlavor.stringFlavor);
				arg0.getDropTargetContext().dropComplete( true);
				String[] files = string.split( System.getProperty( "line.separator"));
				if ( files.length <= 0)
					arg0.rejectDrop();
				else {
					File file = new File( new URI( files[ 0].replaceAll( "[\r\n]", "")));
					if ( !file.exists() || !file.isFile() || !file.canRead())
						return;

					setText( file.getAbsolutePath().replaceAll( "\\\\", "/"));

					arg0.getDropTargetContext().dropComplete( true);
				}
			}
		} catch (UnsupportedFlavorException e) {
			arg0.rejectDrop();
		} catch (IOException e) {
			arg0.rejectDrop();
		} catch (URISyntaxException e) {
			arg0.rejectDrop();
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.dnd.text.DDTextField#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent arg0) {
		super.dragExit(arg0);
	}
}
