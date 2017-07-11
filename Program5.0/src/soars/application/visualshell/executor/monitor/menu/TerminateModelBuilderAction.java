/*
 * Created on 2006/09/13
 */
package soars.application.visualshell.executor.monitor.menu;

import java.awt.event.ActionEvent;

import soars.application.visualshell.executor.monitor.MonitorTabbedPane;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Forced termination" menu.
 * @author kurata / SOARS project
 */
public class TerminateModelBuilderAction extends MenuAction {

	/**
	 * 
	 */
	private MonitorTabbedPane _monitorTabbedPane = null;

	/**
	 * Creates the menu handler of "Forced termination" menu
	 * @param name the Menu name
	 * @param monitorTabbedPane the he container of the monitor components to display the log output of the ModelBuilder
	 */
	public TerminateModelBuilderAction(String name, MonitorTabbedPane monitorTabbedPane) {
		super(name);
		_monitorTabbedPane = monitorTabbedPane;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_monitorTabbedPane.on_terminate_model_builder(actionEvent);
	}
}
