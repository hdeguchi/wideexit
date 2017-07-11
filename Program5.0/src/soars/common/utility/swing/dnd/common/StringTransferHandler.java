/*
 * 2005/04/18
 */
package soars.common.utility.swing.dnd.common;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 * @author kurata
 */
public abstract class StringTransferHandler extends TransferHandler {

	protected abstract String exportString( JComponent c);
	protected abstract void importString( JComponent c, String str);
	protected abstract void cleanup( JComponent c, boolean remove);

	/**
	 * 
	 */
	public StringTransferHandler() {
		super();
	}

	/**
	 * @param arg0
	 */
	public StringTransferHandler(String arg0) {
		super(arg0);
	}

	/* (Non Javadoc)
	 * @see javax.swing.TransferHandler#createTransferable(javax.swing.JComponent)
	 */
	protected Transferable createTransferable(JComponent arg0) {
		return new StringSelection( exportString( arg0));
	}

	/* (Non Javadoc)
	 * @see javax.swing.TransferHandler#getSourceActions(javax.swing.JComponent)
	 */
	public int getSourceActions(JComponent arg0) {
		return COPY_OR_MOVE;
	}

	/* (Non Javadoc)
	 * @see javax.swing.TransferHandler#importData(javax.swing.JComponent, java.awt.datatransfer.Transferable)
	 */
	public boolean importData(JComponent arg0, Transferable arg1) {
		if ( canImport( arg0, arg1.getTransferDataFlavors())) {
			try {
				String str = ( String)arg1.getTransferData( DataFlavor.stringFlavor);
				importString( arg0, str);
				return true;
			} catch (UnsupportedFlavorException ufe) {
			} catch (IOException ioe) {
			}
		}

		return false;
	}

	/* (Non Javadoc)
	 * @see javax.swing.TransferHandler#exportDone(javax.swing.JComponent, java.awt.datatransfer.Transferable, int)
	 */
	protected void exportDone(JComponent arg0, Transferable arg1, int arg2) {
		cleanup( arg0, arg2 == MOVE);
	}

	/* (Non Javadoc)
	 * @see javax.swing.TransferHandler#canImport(javax.swing.JComponent, java.awt.datatransfer.DataFlavor[])
	 */
	public boolean canImport(JComponent arg0, DataFlavor[] arg1) {
		for ( int i = 0; i < arg1.length; i++) {
			if ( DataFlavor.stringFlavor.equals( arg1[ i])) {
				return true;
			}
		}
		return false;
	}
}
