/**
 * 
 */
package soars.tool.image.checker.body.menu.file;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.menu.MenuAction;
import soars.tool.image.checker.body.main.MainFrame;

/**
 * The menu handler of "Copy to clipboard" menu.
 * @author kurata / SOARS project
 */
public class CopyToClipboardAction extends MenuAction {

	/**
	 * Creates the menu handler of "Copy to clipboard" menu
	 * @param name the Menu name
	 */
	public CopyToClipboardAction(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_file_copy_to_clipboard(actionEvent);
	}
}
