/*
 * Created on 2005/11/29
 */
package soars.application.visualshell.common.menu.basic5;

import java.awt.event.ActionEvent;

/**
 * The common menu handler interface.
 * @author kurata / SOARS project
 */
public interface IBasicMenuHandler5 {

	/**
	 * Invoked when the "Insert and shift right" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_insert_right_shift(ActionEvent actionEvent);

	/**
	 * Invoked when the "Insert and shift downward" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_insert_downward_shift(ActionEvent actionEvent);

	/**
	 * Invoked when the "Remove and shift left" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_remove_left_shift(ActionEvent actionEvent);

	/**
	 * Invoked when the "Remove and shift upward" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_remove_upward_shift(ActionEvent actionEvent);

	/**
	 * Invoked when the "Or" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_or(ActionEvent actionEvent);
}
