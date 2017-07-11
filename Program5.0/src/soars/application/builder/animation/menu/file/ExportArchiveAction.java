/*
 * 2005/07/01
 */
package soars.application.builder.animation.menu.file;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;

import soars.application.builder.animation.main.MainFrame;

import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 */
public class ExportArchiveAction extends MenuAction {

	/**
	 * @param name
	 */
	public ExportArchiveAction(String name) {
		super(name);
	}

	/**
	 * @param name
	 * @param item
	 */
	public ExportArchiveAction(String name, JMenuItem item) {
		super(name, item);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		MainFrame.get_instance().on_file_export_archive(actionEvent);
	}
}
