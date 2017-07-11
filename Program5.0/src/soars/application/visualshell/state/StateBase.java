/*
 * 2004/05/14
 */
package soars.application.visualshell.state;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Vector;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.VisualShellView;
import soars.application.visualshell.object.base.DrawObject;

/**
 * @author kurata
 */
public class StateBase {

	/**
	 * 
	 */
	public StateBase() {
		super();
	}

	/**
	 * @return
	 */
	public boolean setup() {
		return true;
	}

	/**
	 * 
	 */
	public void cleanup() {
	}

	/**
	 * 
	 */
	public void refresh() {
	}

	/**
	 * @param newStateName
	 * @return
	 */
	public boolean change(String newStateName) {
		return true;
	}

	/**
	 * @param newStateName
	 * @return
	 */
	public boolean on_enter(String newStateName) {
		VisualShellView.get_instance().setToolTipText( "");
		return true;
	}

	/**
	 * @param newStateName
	 * @return
	 */
	public boolean on_leave(String newStateName) {
		return true;
	}

	/**
	 * 
	 */
	public void cancel() {
	}

	/**
	 * @param componentEvent
	 */
	public void on_resized(ComponentEvent componentEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	public void on_mouse_left_double_click(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	public void on_mouse_left_down(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	public void on_mouse_left_up(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	public void on_mouse_moved(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	public void on_mouse_right_down(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	public void on_mouse_right_up(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	public void on_mouse_dragged(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 * @return
	 */
	public String get_tooltip_text(MouseEvent mouseEvent) {
		DrawObject drawObject = LayerManager.get_instance().get( mouseEvent.getPoint());
		if ( null == drawObject || drawObject._comment.equals( ""))
			return null;

		return drawObject._comment;
	}

	/**
	 * @param graphics
	 */
	public void paint(Graphics graphics) {
	}

	/**
	 * @param keyEvent
	 */
	public void on_key_pressed(KeyEvent keyEvent) {
	}

	/**
	 * @param keyEvent
	 */
	public void on_key_released(KeyEvent keyEvent) {
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	public boolean is_closure_connection(Vector<DrawObject> drawObjects) {
		return false;
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	public boolean can_swap_names(Vector<DrawObject> drawObjects) {
		return false;
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	public boolean can_copy_objects(Vector<DrawObject> drawObjects) {
		return false;
	}

	/**
	 * @return
	 */
	public boolean can_paste_objects() {
		return false;
	}

	/**
	 * @return
	 */
	public Vector<DrawObject> get_selected_drawobjects() {
		return new Vector();
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	public void on_remove(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	public void on_move(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	public void on_copy(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	public void on_paste(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	public void on_select_all(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	public void on_deselect_all(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	public void on_flush_top(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	public void on_flush_bottom(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	public void on_flush_left(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	public void on_flush_right(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	public void on_vertical_equal_layout(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	public void on_horizontal_equal_layout(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	public void on_swap_names(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	public void on_copy_objects(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	public void on_paste_objects(ActionEvent actionEvent) {
	}
}
