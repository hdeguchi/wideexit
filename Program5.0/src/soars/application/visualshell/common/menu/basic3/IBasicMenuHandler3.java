/*
 * 2005/07/13
 */
package soars.application.visualshell.common.menu.basic3;

import java.awt.event.ActionEvent;

/**
 * The common menu handler interface.
 * @author kurata / SOARS project
 */
public interface IBasicMenuHandler3 {

	/**
	 * Invoked when the "New row" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_append_row(ActionEvent actionEvent);

	/**
	 * Invoked when the "Insert row" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_insert_row(ActionEvent actionEvent);

	/**
	 * Invoked when the "Remove row" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_remove_row(ActionEvent actionEvent);

	/**
	 * Invoked when the "Copy row" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_copy_row(ActionEvent actionEvent);

	/**
	 * Invoked when the "Cut row" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_cut_row(ActionEvent actionEvent);

	/**
	 * Invoked when the "Paste row" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_paste_row(ActionEvent actionEvent);
}
