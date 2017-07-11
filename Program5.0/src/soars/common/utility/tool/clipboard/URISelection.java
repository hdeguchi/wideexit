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

/**
 * @author kurata
 *
 */
public class URISelection implements Transferable, ClipboardOwner {

	/**
	 * 
	 */
	static public DataFlavor _uriFlavor = null;

	/**
	 * 
	 */
	private File[] _files = null;

	/**
	 * 
	 */
	static {
		startup();
	}

	/**
	 * 
	 */
	private static void startup() {
		try {
			_uriFlavor = new DataFlavor( "text/uri-list;class=java.lang.String");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param files 
	 */
	public URISelection(File[] files) {
		super();
		_files = files;
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
	 */
	public Object getTransferData(DataFlavor arg0) throws UnsupportedFlavorException, IOException {
		if ( !isDataFlavorSupported( arg0))
			return null;

		String object = "";
		for ( int i = 0; i < _files.length; ++i)
			object += ( ( ( 0 == i) ? "" : System.getProperty( "line.separator")) + _files[ i].toURI().toString());

		return object;
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { _uriFlavor};
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
	 */
	public boolean isDataFlavorSupported(DataFlavor arg0) {
		return arg0.equals( _uriFlavor);
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.ClipboardOwner#lostOwnership(java.awt.datatransfer.Clipboard, java.awt.datatransfer.Transferable)
	 */
	public void lostOwnership(Clipboard arg0, Transferable arg1) {
		_files = null;
	}
}
