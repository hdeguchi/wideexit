/**
 * 
 */
package soars.application.visualshell.common.image.menu;

import java.awt.event.ActionEvent;

import soars.application.visualshell.common.image.ThumbnailList;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler to rename the thumbnail.
 * @author kurata / SOARS project
 */
public class EditAction extends MenuAction {

	/**
	 * The thumbnail list.
	 */
	private ThumbnailList _thumbnailList = null;

	/**
	 * Creates the menu handler to rename the thumbnail. 
	 * @param name the Menu name
	 * @param thumbnailList the Thumbnail list
	 */
	public EditAction(String name, ThumbnailList thumbnailList) {
		super(name);
		_thumbnailList = thumbnailList;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_thumbnailList.on_edit( actionEvent);
	}
}
