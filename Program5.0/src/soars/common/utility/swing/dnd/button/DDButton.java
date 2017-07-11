/*
 * 2005/04/19
 */
package soars.common.utility.swing.dnd.button;

import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * @author kurata
 */
public class DDButton extends JButton implements DropTargetListener {

	/**
	 * 
	 */
	public DDButton() {
		super();
		new DropTarget( this, this);
	}

	/**
	 * @param arg0
	 */
	public DDButton(String arg0) {
		super(arg0);
		new DropTarget( this, this);
	}

	/**
	 * @param arg0
	 */
	public DDButton(Action arg0) {
		super(arg0);
		new DropTarget( this, this);
	}

	/**
	 * @param arg0
	 */
	public DDButton(Icon arg0) {
		super(arg0);
		new DropTarget( this, this);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public DDButton(String arg0, Icon arg1) {
		super(arg0, arg1);
		new DropTarget( this, this);
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent arg0) {
		arg0.rejectDrag();
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent arg0) {
		arg0.rejectDrag();
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent arg0) {
		arg0.rejectDrag();
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent arg0) {
		arg0.rejectDrop();
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent arg0) {
	}
}
