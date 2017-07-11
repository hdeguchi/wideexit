/*
 * 2005/03/18
 */
package soars.common.utility.swing.file.manager.edit.menu.file;

import java.awt.event.ActionEvent;

/**
 * The file menu handler interface.
 * @author kurata / SOARS project
 */
public interface IFileMenuHandler {

	/**
	 * Invoked when the "New" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_new(ActionEvent actionEvent);

	/**
	 * Invoked when the "Open" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_open(ActionEvent actionEvent);

	/**
	 * Invoked when the "Close" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_close(ActionEvent actionEvent);

	/**
	 * Invoked when the "Save" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_save(ActionEvent actionEvent);

	/**
	 * Invoked when the "Save as" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_save_as(ActionEvent actionEvent);

	/**
	 * Invoked when the "Save with specified encoding" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_save_with_specified_encoding(ActionEvent actionEvent);

	/**
	 * Invoked when the "Exit" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_exit(ActionEvent actionEvent);
}
