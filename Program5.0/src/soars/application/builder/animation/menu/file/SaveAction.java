/*
 * 2005/02/09
 */
package soars.application.builder.animation.menu.file;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;

import soars.application.builder.animation.main.MainFrame;

import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 */
public class SaveAction extends MenuAction {

	/**
	 * @param name
	 */
	public SaveAction(String name) {
		super(name);
	}

	/**
	 * @param name
	 * @param item
	 */
	public SaveAction(String name, JMenuItem item) {
		super(name, item);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_file_save(actionEvent);
	}
}
