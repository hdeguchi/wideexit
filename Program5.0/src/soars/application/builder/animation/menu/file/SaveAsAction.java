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
public class SaveAsAction extends MenuAction {

	/**
	 * @param name
	 */
	public SaveAsAction(String name) {
		super(name);
	}

	/**
	 * @param name
	 * @param item
	 */
	public SaveAsAction(String name, JMenuItem item) {
		super(name, item);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_file_save_as(actionEvent);
	}
}
