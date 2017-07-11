/**
 * 
 */
package soars.common.utility.tool.ssh.filechooser.menu;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;

import soars.common.utility.swing.menu.MenuAction;
import soars.common.utility.tool.ssh.filechooser.SftpFileList;

/**
 * @author kurata
 *
 */
public class RenameAction extends MenuAction {

	/**
	 * 
	 */
	private SftpFileList _sftpFileList = null;

	/**
	 * @param name
	 * @param sftpFileList
	 */
	public RenameAction(String name, SftpFileList sftpFileList) {
		super(name);
		_sftpFileList = sftpFileList;
	}

	/**
	 * @param name
	 * @param item
	 */
	public RenameAction(String name, JMenuItem item) {
		super(name, item);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_sftpFileList.on_rename(actionEvent);
	}
}
