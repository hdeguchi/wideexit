/*
 * Created on 2006/06/06
 */
package soars.application.visualshell.plugin;

import java.awt.event.ActionEvent;

import soars.common.utility.swing.menu.MenuAction;
import soars.common.utility.swing.window.Frame;

/**
 * @author kurata
 */
public class PluginMenuAction extends MenuAction {


	/**
	 * 
	 */
	private Frame _frame = null;

	/**
	 * 
	 */
	private Plugin _plugin = null;

	/**
	 * @param name
	 * @param frame
	 * @param plugin
	 */
	public PluginMenuAction(String name, Frame frame, Plugin plugin) {
		super(name);
		_frame = frame;
		_plugin = plugin;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_plugin.on_selected(_frame, actionEvent);
	}
}
