/*
 * 2005/05/01
 */
package soars.application.animator.common.image.menu;

import java.awt.event.ActionEvent;

import soars.application.animator.common.image.ThumbnailList;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler to add the thumbnail into the thumbnail list.
 * @author kurata / SOARS project
 */
public class AppendAction extends MenuAction {

	/**
	 * The thumbnail list.
	 */
	private ThumbnailList _thumbnailList = null;

	/**
	 * Creates the menu handler to add the thumbnail into the thumbnail list.
	 * @param name the Menu name
	 * @param thumbnailList the Thumbnail list
	 */
	public AppendAction(String name, ThumbnailList thumbnailList) {
		super(name);
		_thumbnailList = thumbnailList;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_thumbnailList.on_append( actionEvent);
	}
}
