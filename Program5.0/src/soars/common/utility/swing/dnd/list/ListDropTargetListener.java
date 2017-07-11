/*
 * 2005/05/26
 */
package soars.common.utility.swing.dnd.list;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;

/**
 * @author kurata
 */
public class ListDropTargetListener implements DropTargetListener {

	/**
	 * 
	 */
	private DDList _list = null;

	/**
	 * 
	 */
	private int _index = -1;

	/**
	 * 
	 */
	private Rectangle2D _line = new Rectangle2D.Float();

	/**
	 * @param list
	 */
	public ListDropTargetListener(DDList list) {
		super();
		_list = list;
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
		if ( isDragAcceptable( dropTargetDragEvent))
			dropTargetDragEvent.acceptDrag( dropTargetDragEvent.getDropAction());
		else
			dropTargetDragEvent.rejectDrag();
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
		Graphics2D graphics2D = ( Graphics2D)_list.getGraphics();
		_index = _list.locationToIndex( dropTargetDragEvent.getLocation());
		int cellHeight = ( int)_list.getCellBounds( 0, 0).getHeight();
		int cellWidht  = ( int)_list.getCellBounds( 0, 0).getWidth();
		for ( int i = 0; i <= _list.getModel().getSize(); ++i) {
			_line.setRect( 0, cellHeight * i - ( int)( cellHeight / 2), cellWidht, cellHeight);
			if ( _line.contains( dropTargetDragEvent.getLocation())) {
				graphics2D.setColor( SystemColor.activeCaption);
				_index = i;
			} else {
				graphics2D.setColor( _list.getBackground());
			}
			_line.setRect( 0, i * cellHeight, cellWidht, 2);
			graphics2D.fill( _line);
		}

		if ( isDragAcceptable( dropTargetDragEvent))
			dropTargetDragEvent.acceptDrag( dropTargetDragEvent.getDropAction());
		else
			dropTargetDragEvent.rejectDrag();
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent dropTargetDragEvent) {
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent dropTargetDropEvent) {
		Point point = dropTargetDropEvent.getLocation();
		Transferable transferable = dropTargetDropEvent.getTransferable();
		DataFlavor[] dataFlavors = transferable.getTransferDataFlavors();
		try{
			if ( isDropAcceptable( dropTargetDropEvent)) {
				Component component = ( Component)transferable.getTransferData( dataFlavors[ 0]);
				int si = _list.getSelectedIndex();
				DefaultComboBoxModel defaultComboBoxModel = ( DefaultComboBoxModel)_list.getModel();
				Object object = defaultComboBoxModel.getElementAt( si);
				if ( _index >= _list.getModel().getSize()) {
					defaultComboBoxModel.removeElementAt( si);
					defaultComboBoxModel.addElement( object);
				} else if ( _index == si) {
				} else if ( _index < si) {
					defaultComboBoxModel.removeElementAt( si);
					defaultComboBoxModel.insertElementAt( object, _index);
				} else {
					defaultComboBoxModel.insertElementAt( object, _index);
					defaultComboBoxModel.removeElementAt( si);
				}
				dropTargetDropEvent.dropComplete( true);
			}else{
				dropTargetDropEvent.dropComplete( false);
			}
		} catch (UnsupportedFlavorException e) {
			dropTargetDropEvent.dropComplete( false);
		} catch (IOException ie) {
			dropTargetDropEvent.dropComplete( false);
		}
		dropTargetDropEvent.dropComplete(false);
		_index = -1;
		_list.repaint();
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent dropTargetEvent) {
		_list.repaint();
	}

	/**
	 * @param dropTargetDragEvent
	 * @return
	 */
	public boolean isDragAcceptable(DropTargetDragEvent dropTargetDragEvent) {
		DataFlavor[] dataFlavors = dropTargetDragEvent.getCurrentDataFlavors();
		if ( _list.isDataFlavorSupported( dataFlavors[ 0]))
			return true;
		else
			return false;
	}

	/**
	 * @param dropTargetDropEvent
	 * @return
	 */
	public boolean isDropAcceptable(DropTargetDropEvent dropTargetDropEvent) {
		Transferable transferable = dropTargetDropEvent.getTransferable();
		DataFlavor[] dataFlavors = transferable.getTransferDataFlavors();
		if ( _list.isDataFlavorSupported( dataFlavors[ 0]))
			return true;
		else
			return false;
	}
}
