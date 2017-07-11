/**
 * 
 */
package soars.common.utility.swing.dnd.dom;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * @author kurata
 *
 */
public class DomNodeTransferable implements Transferable {

	/**
	 * 
	 */
	public static final String _name = "DomNode";

	/**
	 * 
	 */
	public static final DataFlavor _localObjectFlavor = new DataFlavor( DataFlavor.javaJVMLocalObjectMimeType, _name);

	/**
	 * 
	 */
	private static final DataFlavor[] _supportedFlavors = { _localObjectFlavor };

	/**
	 * 
	 */
	private Object _object = null;

	/**
	 * @param object
	 */
	public DomNodeTransferable(Object object) {
		super();
		_object = object;
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
	 */
	public Object getTransferData(DataFlavor arg0) throws UnsupportedFlavorException, IOException {
		return isDataFlavorSupported( arg0) ? _object : null;
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	public DataFlavor[] getTransferDataFlavors() {
		return _supportedFlavors;
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
	 */
	public boolean isDataFlavorSupported(DataFlavor arg0) {
		return arg0.getHumanPresentableName().equals( _name);
	}
}
