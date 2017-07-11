/*
 * 2004/12/15
 */
package soars.plugin.modelbuilder.chart.live_viewer.menu;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;

import soars.plugin.modelbuilder.chart.live_viewer.main.LiveViewerView;

import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 */
public class WriteAction extends MenuAction {


	/**
	 * 
	 */
	private LiveViewerView _liveViewerView = null;

	/**
	 * @param name
	 * @param liveViewerView
	 */
	public WriteAction(String name, LiveViewerView liveViewerView) {
		super(name);
		_liveViewerView = liveViewerView;
	}

	/**
	 * @param name
	 * @param item
	 */
	public WriteAction(String name, JMenuItem item) {
		super(name, item);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_liveViewerView.on_write( actionEvent);
	}
}
