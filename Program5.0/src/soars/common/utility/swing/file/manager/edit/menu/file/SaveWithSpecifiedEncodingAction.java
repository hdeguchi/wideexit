/**
 * 
 */
package soars.common.utility.swing.file.manager.edit.menu.file;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Save with specified encoding" menu.
 * @author kurata / SOARS project
 */
public class SaveWithSpecifiedEncodingAction extends MenuAction {

	/**
	 * 
	 */
	private IFileMenuHandler _fileMenuHandler = null;

	/**
	 * Creates the menu handler of "Save with specified encoding" menu
	 * @param name the Menu name
	 * @param name fileMenuHandler the instance of IFileMenuHandler
	 */
	public SaveWithSpecifiedEncodingAction(String name, IFileMenuHandler fileMenuHandler) {
		super(name);
		_fileMenuHandler = fileMenuHandler;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_fileMenuHandler.on_file_save_with_specified_encoding(actionEvent);
	}
}
