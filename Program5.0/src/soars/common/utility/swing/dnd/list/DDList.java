/*
 * 2005/05/26
 */
package soars.common.utility.swing.dnd.list;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListModel;

/**
 * @author kurata
 */
public class DDList
	extends JList
	implements DragGestureListener, DragSourceListener, Transferable {

	/**
	 * 
	 */
	private final String _name = "DDList";

	/**
	 * 
	 */
	private final DataFlavor _dataFlavor = new DataFlavor( DataFlavor.javaJVMLocalObjectMimeType, _name);

	/**
	 * 
	 */
	public DDList() {
		super();
	}

	/**
	 * @param arg0
	 */
	public DDList(Object[] arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public DDList(Vector arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public DDList(ListModel arg0) {
		super(arg0);
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.DragGestureEvent)
	 */
	public void dragGestureRecognized(DragGestureEvent dragGestureEvent) {
		try {
			dragGestureEvent.startDrag( DragSource.DefaultMoveDrop, this, this);
		} catch (InvalidDnDOperationException e) {
			e.printStackTrace();
			return;
		}
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragEnter(java.awt.dnd.DragSourceDragEvent)
	 */
	public void dragEnter(DragSourceDragEvent arg0) {
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragOver(java.awt.dnd.DragSourceDragEvent)
	 */
	public void dragOver(DragSourceDragEvent arg0) {
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dropActionChanged(java.awt.dnd.DragSourceDragEvent)
	 */
	public void dropActionChanged(DragSourceDragEvent arg0) {
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragDropEnd(java.awt.dnd.DragSourceDropEvent)
	 */
	public void dragDropEnd(DragSourceDropEvent arg0) {
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragExit(java.awt.dnd.DragSourceEvent)
	 */
	public void dragExit(DragSourceEvent arg0) {
	}

	/* (Non Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor[] dataFlavors = new DataFlavor[ 1];
		dataFlavors[ 0] = _dataFlavor;
		return dataFlavors;
	}

	/* (Non Javadoc)
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
	 */
	public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
		if ( dataFlavor.getHumanPresentableName().equals( _name)) {
			return true;
		} else {
			return false;
		}
	}

	/* (Non Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
	 */
	public Object getTransferData(DataFlavor dataFlavor)
		throws UnsupportedFlavorException, IOException {
		return this;
	}
}
