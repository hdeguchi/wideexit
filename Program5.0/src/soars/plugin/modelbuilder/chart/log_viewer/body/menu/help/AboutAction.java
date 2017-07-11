/*
 * 2004/12/22
 */
package soars.plugin.modelbuilder.chart.log_viewer.body.menu.help;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;

import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 */
public class AboutAction extends MenuAction {


	/**
	 * 
	 */
	private IHelpMenuHandler _helpMenuHandler = null;

	/**
	 * @param name
	 * @param helpMenuHandler
	 */
	public AboutAction(String name, IHelpMenuHandler helpMenuHandler) {
		super(name);
		_helpMenuHandler = helpMenuHandler;
	}

	/**
	 * @param name
	 * @param item
	 */
	public AboutAction(String name, JMenuItem item) {
		super(name, item);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_helpMenuHandler.on_help_about( actionEvent);
	}
}
