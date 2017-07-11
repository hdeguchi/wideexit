/*
 * Created on 2006/03/02
 */
package soars.application.visualshell.executor.monitor;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

import soars.application.visualshell.executor.monitor.menu.CopyToClipboardAction;
import soars.application.visualshell.executor.monitor.menu.RemoveAction;
import soars.application.visualshell.executor.monitor.menu.RemoveAllAction;
import soars.application.visualshell.executor.monitor.menu.TerminateModelBuilderAction;
import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.tool.SwingTool;

/**
 * The container of the monitor components to display the log output of the ModelBuilder.
 * @author kurata / SOARS project
 */
public class MonitorTabbedPane extends JTabbedPane {

	/**
	 * 
	 */
	private UserInterface _userInterface = null;

	/**
	 * 
	 */
	private JPopupMenu _popupMenu = null;

	/**
	 * 
	 */
	private JMenuItem _removeMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _removeAllMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _terminateModelBuilderMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _copyToClipboardMenuItem = null;

	/**
	 * Creates the instance of MonitorTabbedPane with the specified data.
	 * @param arg0 the placement for the tabs relative to the content
	 * @param arg1 the policy for laying out tabs when all tabs will not fit on one run
	 */
	public MonitorTabbedPane(int arg0, int arg1) {
		super(arg0, arg1);
	}

	/**
	 * Initializes this component.
	 */
	public void setup() {
		addMouseListener( new MouseAdapter() {
			public void mouseReleased(MouseEvent arg0) {
				if ( !SwingTool.is_mouse_right_button( arg0))
					return;

				on_mouse_right_up( arg0);
			}
		});

		setup_popup_menu();
	}

	/**
	 * 
	 */
	private void setup_popup_menu() {
		_userInterface = new UserInterface();
		_popupMenu = new JPopupMenu();

		_removeMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "run.monitor.tab.popup.menu.remove.menu"),
			new RemoveAction( ResourceManager.get_instance().get( "run.monitor.tab.popup.menu.remove.menu"), this),
			ResourceManager.get_instance().get( "run.monitor.tab.popup.menu.remove.mnemonic"),
			ResourceManager.get_instance().get( "run.monitor.tab.popup.menu.remove.stroke"));
		_removeAllMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "run.monitor.tab.popup.menu.remove.all.menu"),
			new RemoveAllAction( ResourceManager.get_instance().get( "run.monitor.tab.popup.menu.remove.all.menu"), this),
			ResourceManager.get_instance().get( "run.monitor.tab.popup.menu.remove.all.mnemonic"),
			ResourceManager.get_instance().get( "run.monitor.tab.popup.menu.remove.all.stroke"));
		_terminateModelBuilderMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "run.monitor.tab.popup.menu.terminate.model.builder.menu"),
			new TerminateModelBuilderAction( ResourceManager.get_instance().get( "run.monitor.tab.popup.menu.terminate.model.builder.menu"), this),
			ResourceManager.get_instance().get( "run.monitor.tab.popup.menu.terminate.model.builder.mnemonic"),
			ResourceManager.get_instance().get( "run.monitor.tab.popup.menu.terminate.model.builder.stroke"));
		_copyToClipboardMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "run.monitor.tab.popup.menu.copy.to.clipboard.menu"),
			new CopyToClipboardAction( ResourceManager.get_instance().get( "run.monitor.tab.popup.menu.copy.to.clipboard.menu"), this),
			ResourceManager.get_instance().get( "run.monitor.tab.popup.menu.copy.to.clipboard.mnemonic"),
			ResourceManager.get_instance().get( "run.monitor.tab.popup.menu.copy.to.clipboard.stroke"));
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
		int index = get_index( mouseEvent.getPoint());
		if ( 0 > index || getTabCount() <= index)
			return;

		_removeMenuItem.setEnabled( true);
		_removeAllMenuItem.setEnabled( true);
		_terminateModelBuilderMenuItem.setEnabled( true);
		_copyToClipboardMenuItem.setEnabled( true);

		MonitorPropertyPage monitorPropertyPage = ( MonitorPropertyPage)getComponentAt( index);
		if ( monitorPropertyPage.is_alive()) {
			_removeMenuItem.setEnabled( false);
			_copyToClipboardMenuItem.setEnabled( false);
		} else {
			if ( 2 > getTabCount())
				_removeMenuItem.setEnabled( false);

			_terminateModelBuilderMenuItem.setEnabled( false);
		}

		if ( 2 > getTabCount())
			_removeAllMenuItem.setEnabled( false);

		_terminateModelBuilderMenuItem.setEnabled( null != monitorPropertyPage._process);

		_popupMenu.show( this, mouseEvent.getX(), mouseEvent.getY());
	}

	/**
	 * @param point
	 * @return
	 */
	private int get_index(Point point) {
	  for ( int i = 0; i < getTabCount(); ++i) {
	    Rectangle rectangle = getBoundsAt( i);
	    if ( rectangle.contains( point))
	      return i;
	  }
		return -1;
	}

	/**
	 * Removes the selected monitor component.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_remove(ActionEvent actionEvent) {
		int index = getSelectedIndex();
		if ( 0 > index || getTabCount() <= index)
			return;

		remove( index);
	}

	/**
	 * Removes all monitor components.
	 * @param actionEvent
	 */
	public void on_remove_all(ActionEvent actionEvent) {
		while ( remove_all())
			;
	}

	/**
	 * @return
	 */
	private boolean remove_all() {
		if ( 2 > getTabCount())
			return false;

		for ( int i = 0; i < getTabCount(); ++i) {
			MonitorPropertyPage monitorPropertyPage = ( MonitorPropertyPage)getComponentAt( i);
			if ( !monitorPropertyPage.is_alive()) {
				remove( i);
				return true;
			}
		}
		return false;
	}

	/**
	 * Invoked when the ModelBuilder is terminated.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_terminate_model_builder(ActionEvent actionEvent) {
		int index = getSelectedIndex();
		if ( 0 > index || getTabCount() <= index)
			return;

		MonitorPropertyPage monitorPropertyPage = ( MonitorPropertyPage)getComponentAt( index);
		monitorPropertyPage.terminate();
	}

	/**
	 * Copies the text on the selected monitor component to the clipboard.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_copy_to_clipboard(ActionEvent actionEvent) {
		MonitorPropertyPage monitorPropertyPage = ( MonitorPropertyPage)getSelectedComponent();
		if ( null == monitorPropertyPage)
			return;

		monitorPropertyPage.copy_to_clipboard();
	}
}
