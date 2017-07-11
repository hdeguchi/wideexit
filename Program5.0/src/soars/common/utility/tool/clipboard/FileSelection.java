/**
 * 
 */
package soars.common.utility.tool.clipboard;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author kurata
 *
 */
public class FileSelection implements Transferable, ClipboardOwner {

	/**
	 * 
	 */
	private File[] _files = null;

	/**
	 * @param files
	 */
	public FileSelection(File[] files) {
		super();
		_files = files;
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
	 */
	public Object getTransferData(DataFlavor arg0) throws UnsupportedFlavorException, IOException {
		return isDataFlavorSupported( arg0) ? Arrays.asList( _files) : null;
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { DataFlavor.javaFileListFlavor};
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
	 */
	public boolean isDataFlavorSupported(DataFlavor arg0) {
		return arg0.equals( DataFlavor.javaFileListFlavor);
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.ClipboardOwner#lostOwnership(java.awt.datatransfer.Clipboard, java.awt.datatransfer.Transferable)
	 */
	public void lostOwnership(Clipboard arg0, Transferable arg1) {
		_files = null;
	}
}
