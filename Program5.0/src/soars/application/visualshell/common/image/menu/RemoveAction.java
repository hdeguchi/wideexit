/*
 * 2005/05/01
 */
package soars.application.visualshell.common.image.menu;

import java.awt.event.ActionEvent;

import soars.application.visualshell.common.image.ThumbnailList;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler to remove the thumbnail.
 * @author kurata / SOARS project
 */
public class RemoveAction extends MenuAction {

	/**
	 * The thumbnail list.
	 */
	private ThumbnailList _thumbnailList = null;

	/**
	 * Creates the menu handler to remove the thumbnail. 
	 * @param name the Menu name
	 * @param thumbnailList the Thumbnail list
	 */
	public RemoveAction(String name, ThumbnailList thumbnailList) {
		super(name);
		_thumbnailList = thumbnailList;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_thumbnailList.on_remove( actionEvent);
	}
}
