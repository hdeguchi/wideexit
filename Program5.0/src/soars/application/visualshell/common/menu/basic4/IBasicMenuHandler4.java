/*
 * 2005/07/13
 */
package soars.application.visualshell.common.menu.basic4;

import java.awt.event.ActionEvent;

/**
 * The common menu handler interface.
 * @author kurata / SOARS project
 */
public interface IBasicMenuHandler4 {

	/**
	 * Invoked when the "New column" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_append_column(ActionEvent actionEvent);

	/**
	 * Invoked when the "Insert column" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_insert_column(ActionEvent actionEvent);

	/**
	 * Invoked when the "Remove column" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_remove_column(ActionEvent actionEvent);

	/**
	 * Invoked when the "Copy column" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_copy_column(ActionEvent actionEvent);

	/**
	 * Invoked when the "Cut column" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_cut_column(ActionEvent actionEvent);

	/**
	 * Invoked when the "Paste column" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_paste_column(ActionEvent actionEvent);
}
