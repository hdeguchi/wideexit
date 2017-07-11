/**
 * 
 */
package soars.common.utility.swing.dnd.text;

import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.JTextField;
import javax.swing.text.Document;

/**
 * @author kurata
 *
 */
public class DDTextField extends JTextField implements DropTargetListener {

	/**
	 * 
	 */
	public DDTextField() {
		super();
		new DropTarget( this, this);
	}

	/**
	 * @param arg0
	 */
	public DDTextField(String arg0) {
		super(arg0);
		new DropTarget( this, this);
	}

	/**
	 * @param arg0
	 */
	public DDTextField(int arg0) {
		super(arg0);
		new DropTarget( this, this);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public DDTextField(String arg0, int arg1) {
		super(arg0, arg1);
		new DropTarget( this, this);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public DDTextField(Document arg0, String arg1, int arg2) {
		super(arg0, arg1, arg2);
		new DropTarget( this, this);
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent arg0) {
	}
}
