/**
 * 
 */
package soars.application.visualshell.object.gis.edit.table.menu;

import java.awt.event.ActionEvent;

import soars.application.visualshell.object.gis.edit.table.GisDataTable;
import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 *
 */
public class CopyToClipboardAction extends MenuAction {

	/**
	 * 
	 */
	private GisDataTable _gisDataTable = null;

	/**
	 * @param name
	 * @param gisDataTable
	 */
	public CopyToClipboardAction(String name, GisDataTable gisDataTable) {
		super(name);
		_gisDataTable = gisDataTable;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_gisDataTable.on_copy_to_clipboard(actionEvent);
	}
}
