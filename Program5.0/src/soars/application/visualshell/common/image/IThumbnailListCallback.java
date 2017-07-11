/**
 * 
 */
package soars.application.visualshell.common.image;

import soars.common.utility.swing.thumbnail.ThumbnailItem;

/**
 * The thumbnail selection handler for the thumbnail list.
 * @author kurata / SOARS project
 */
public interface IThumbnailListCallback {

	/**
	 * Invoked when a thumbnail is clicked.
	 * @param thumbnailItem the thumbnail panel
	 */
	void selected(ThumbnailItem thumbnailItem);

	/**
	 * Invoked when a thumbnail is selected.
	 */
	void update();
}
