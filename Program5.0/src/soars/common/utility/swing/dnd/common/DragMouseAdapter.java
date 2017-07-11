/*
 * 2005/04/15
 */
package soars.common.utility.swing.dnd.common;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 * @author kurata
 */
public class DragMouseAdapter extends MouseAdapter {

	/**
	 * 
	 */
	public DragMouseAdapter() {
		super();
	}

	/* (Non Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent arg0) {
		JComponent component = ( JComponent)arg0.getSource();
		TransferHandler transferHandler = component.getTransferHandler();
		transferHandler.exportAsDrag( component, arg0, TransferHandler.COPY);
	}
}
