/*
 * 2004/10/01
 */
package soars.plugin.modelbuilder.chart.live_viewer.menu;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;

import soars.plugin.modelbuilder.chart.live_viewer.main.MainFrame;
import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 */
public class ExitAction extends MenuAction {


	/**
	 * 
	 */
	private MainFrame _mainFrame = null;

	/**
	 * @param name
	 * @param mainFrame
	 */
	public ExitAction(String name, MainFrame mainFrame) {
		super(name);
		_mainFrame = mainFrame;
	}

	/**
	 * @param name
	 * @param item
	 */
	public ExitAction(String name, JMenuItem item) {
		super(name, item);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_mainFrame.on_exit( actionEvent);
	}
}
