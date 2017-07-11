/*
 * 2005/03/18
 */
package soars.application.animator.common.menu.basic1;

import java.awt.event.ActionEvent;

/**
 * The common menu handler interface.
 * @author kurata / SOARS project
 */
public interface IBasicMenuHandler1 {

	/**
	 * Invoked when the "Visible" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_visible(ActionEvent actionEvent);

	/**
	 * Invoked when the "Invisible" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_invisible(ActionEvent actionEvent);

	/**
	 * Invoked when the "Visible name" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_visible_name(ActionEvent actionEvent);

	/**
	 * Invoked when the "Invisible name" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_invisible_name(ActionEvent actionEvent);

	/**
	 * Invoked when the "Change image color" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_change_image_color(ActionEvent actionEvent);

	/**
	 * Invoked when the "Change text color" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_change_text_color(ActionEvent actionEvent);

	/**
	 * Invoked when the "Change font name" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_change_font_family(ActionEvent actionEvent);

	/**
	 * Invoked when the "Change font style" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_change_font_style(ActionEvent actionEvent);

	/**
	 * Invoked when the "Change font size" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_change_font_size(ActionEvent actionEvent);

	/**
	 * Invoked when the "Change imagefile" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_change_imagefile(ActionEvent actionEvent);

	/**
	 * Invoked when the "Remove imagefile" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_remove_imagefile(ActionEvent actionEvent);

	/**
	 * Invoked when the "Arrange spots" menu item is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_arrange_spots(ActionEvent actionEvent);
}
