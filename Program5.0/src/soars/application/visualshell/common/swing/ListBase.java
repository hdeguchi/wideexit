/*
 * 2005/05/26
 */
package soars.application.visualshell.common.swing;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;

import soars.application.visualshell.common.menu.basic1.AppendAction;
import soars.application.visualshell.common.menu.basic1.EditAction;
import soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1;
import soars.application.visualshell.common.menu.basic1.InsertAction;
import soars.application.visualshell.common.menu.basic1.RemoveAction;
import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.swing.list.StandardList;

/**
 * The common List class for Visual Shell.
 * @author kurata / SOARS project
 */
public class ListBase extends StandardList implements IBasicMenuHandler1 {

	/**
	 * 
	 */
	protected JMenuItem _appendMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _insertMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _editMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _removeMenuItem = null;

	/**
	 * Creates the instance of the common List class with the specified data.
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public ListBase(Frame owner, Component parent) {
		super(owner, parent);
	}

	/**
	 * Returns true if this component is initialized successfully.
	 * @param popupMenu whether to use the context menu
	 * @param singleSelection whether only one list index can be selected at a time
	 * @return true if this component is initialized successfully
	 */
	public boolean setup(boolean popupMenu, boolean singleSelection) {
		if ( !super.setup( popupMenu))
			return false;

		if ( singleSelection)
			setSelectionMode( ListSelectionModel.SINGLE_SELECTION);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.list.StandardList#setup_key_event()
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


		Action deleteAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_remove( e);
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0), "delete");
		getActionMap().put( "delete", deleteAction);


		Action backSpaceAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_remove( e);
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, 0), "backspace");
		getActionMap().put( "backspace", backSpaceAction);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.list.StandardList#setup_popup_menu()
	 */
	protected void setup_popup_menu() {
		super.setup_popup_menu();

		_appendMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.append.menu"),
			new AppendAction( ResourceManager.get_instance().get( "common.popup.menu.append.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.append.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.append.stroke"));
		_insertMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.insert.menu"),
			new InsertAction( ResourceManager.get_instance().get( "common.popup.menu.insert.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.insert.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.insert.stroke"));
		_editMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.edit.menu"),
			new EditAction( ResourceManager.get_instance().get( "common.popup.menu.edit.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.edit.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.edit.stroke"));
		_removeMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.remove.menu"),
			new RemoveAction( ResourceManager.get_instance().get( "common.popup.menu.remove.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.remove.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.remove.stroke"));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.list.StandardList#on_mouse_left_double_click(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_double_click(MouseEvent mouseEvent) {
		int index = getSelectedIndex();
		if ( 0 == _defaultComboBoxModel.getSize() || -1 == index)
			return;

		if ( !getCellBounds( index, index).contains( mouseEvent.getPoint()))
			return;

		on_edit( null);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.list.StandardList#on_mouse_right_up(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
		if ( null == _userInterface)
			return;

		_insertMenuItem.setEnabled( true);
		_editMenuItem.setEnabled( true);
		_removeMenuItem.setEnabled( true);

		//int index = getSelectedIndex();
		if ( 0 == _defaultComboBoxModel.getSize() /*|| -1 == index*/) {
			_insertMenuItem.setEnabled( false);
			_editMenuItem.setEnabled( false);
			_removeMenuItem.setEnabled( false);
		} else {
			int index = locationToIndex( mouseEvent.getPoint());
			if ( 0 <= index && _defaultComboBoxModel.getSize() > index
				&& getCellBounds( index, index).contains( mouseEvent.getPoint())) {
				int[] indices = getSelectedIndices();
				boolean contains = ( 0 <= Arrays.binarySearch( indices, index));
				//setSelectedIndex( index);
				_insertMenuItem.setEnabled( 1 == indices.length && contains);
				_editMenuItem.setEnabled( 1 == indices.length && contains);
				_removeMenuItem.setEnabled( contains);
			} else {
				_insertMenuItem.setEnabled( false);
				_editMenuItem.setEnabled( false);
				_removeMenuItem.setEnabled( false);
			}
		}

		_popupMenu.show( this, mouseEvent.getX(), mouseEvent.getY());
	}

	/**
	 * @param actionEvent
	 */
	protected void on_enter(ActionEvent actionEvent) {
		on_edit( actionEvent);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_edit(java.awt.event.ActionEvent)
	 */
	public void on_edit(ActionEvent actionEvent) {
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_append(java.awt.event.ActionEvent)
	 */
	public void on_append(ActionEvent actionEvent) {
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_insert(java.awt.event.ActionEvent)
	 */
	public void on_insert(ActionEvent actionEvent) {
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_remove(java.awt.event.ActionEvent)
	 */
	public void on_remove(ActionEvent actionEvent) {
	}
}
