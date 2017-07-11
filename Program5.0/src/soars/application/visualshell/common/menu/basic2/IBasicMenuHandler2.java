/*
 * 2005/03/18
 */
package soars.application.visualshell.common.menu.basic2;

import java.awt.event.ActionEvent;

/**
 * The common menu handler interface.
 * @author kurata / SOARS project
 */
public interface IBasicMenuHandler2 {

	/**
	 * Invoked when the "Undo" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_undo(ActionEvent actionEvent);

	/**
	 * Invoked when the "Redo" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_redo(ActionEvent actionEvent);

	/**
	 * Invoked when the "Copy" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_copy(ActionEvent actionEvent);

	/**
	 * Invoked when the "Cut" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_cut(ActionEvent actionEvent);

	/**
	 * Invoked when the "Paste" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_paste(ActionEvent actionEvent);

	/**
	 * Invoked when the "Select all" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_select_all(ActionEvent actionEvent);

	/**
	 * Invoked when the "Deselect all" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_deselect_all(ActionEvent actionEvent);
}
