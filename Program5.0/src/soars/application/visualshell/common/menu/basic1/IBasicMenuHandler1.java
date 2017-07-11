/*
 * 2005/03/18
 */
package soars.application.visualshell.common.menu.basic1;

import java.awt.event.ActionEvent;

/**
 * The common menu handler interface.
 * @author kurata / SOARS project
 */
public interface IBasicMenuHandler1 {

	/**
	 * Invoked when the "Append" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_append(ActionEvent actionEvent);

	/**
	 * Invoked when the "Insert" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_insert(ActionEvent actionEvent);

	/**
	 * Invoked when the "Edit" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit(ActionEvent actionEvent);

	/**
	 * Invoked when the "Remove" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_remove(ActionEvent actionEvent);
}
