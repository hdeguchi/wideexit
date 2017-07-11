/*
 * 2005/06/16
 */
package soars.application.visualshell.menu.file;

import java.awt.event.ActionEvent;

import soars.application.visualshell.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Export script to clipboard" menu.
 * @author kurata / SOARS project
 */
public class ExportToClipboardAction extends MenuAction {

	/**
	 * Creates the menu handler of "Export script to clipboard" menu
	 * @param name the Menu name
	 */
	public ExportToClipboardAction(String name) {
		super(name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_file_export_to_clipboard(actionEvent);
	}
}
