/*
 * 2005/05/26
 */
package soars.application.visualshell.common.swing;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import soars.common.utility.swing.table.base.StandardTable;

/**
 * The common Table class for Visual Shell.
 * @author kurata / SOARS project
 */
public class TableBase extends StandardTable {

	/**
	 * Creates the instance of the common Table class with the specified data.
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public TableBase(Frame owner, Component parent) {
		super(owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#setup(boolean)
	 */
	public boolean setup(boolean popupMenu) {
		if ( !super.setup( popupMenu))
			return false;

		//setAutoResizeMode( AUTO_RESIZE_OFF);
		//getTableHeader().setReorderingAllowed( false);
		//setDefaultEditor( Object.class, null);
		//setSelectionMode( DefaultListSelectionModel.SINGLE_SELECTION);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#setup_key_event()
	 */
	protected void setup_key_event() {
		super.setup_key_event();

		Action enterAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_enter( e);
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0), "enter");
		getActionMap().put( "enter", enterAction);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#on_mouse_right_up(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
		on_mouse_right_up( mouseEvent.getPoint());
	}

	/**
	 * @param point
	 */
	protected void on_mouse_right_up(Point point) {
	}

	/**
	 * @param actionEvent
	 */
	protected void on_enter(ActionEvent actionEvent) {
	}
}
