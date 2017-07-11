/*
 * 2004/10/01
 */
package soars.common.utility.swing.menu;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;

/**
 * @author kurata
 */
public abstract class MenuAction {

	/**
	 * 
	 */
	public String _name = "";

	/**
	 * 
	 */
	public JMenuItem _item = null;

	/**
	 * @param name
	 */
	public MenuAction(String name) {
		super();
		_name = name;
	}

	/**
	 * @param name
	 * @param item
	 */
	public MenuAction(String name, JMenuItem item) {
		super();
		_name = name;
		_item = item;
	}

	/**
	 * @param actionEvent
	 */
	public void selected(ActionEvent actionEvent) {
	}
}
