/*
 * 2005/02/09
 */
package soars.application.animator.menu.file;

import java.awt.event.ActionEvent;

import soars.application.animator.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Save" menu.
 * @author kurata / SOARS project
 */
public class SaveAction extends MenuAction {

	/**
	 * Creates the menu handler of "Save" menu
	 * @param name the Menu name
	 */
	public SaveAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_file_save(actionEvent);
	}
}
