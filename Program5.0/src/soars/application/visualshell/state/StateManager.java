/*
 * 2004/05/14
 */
package soars.application.visualshell.state;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import soars.application.visualshell.object.base.DrawObject;

/**
 * @author kurata
 */
public class StateManager extends HashMap<String, StateBase> {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private StateManager _stateManager = null;

	/**
	 * 
	 */
	private String _currentStateName = "";

	/**
	 * 
	 */
	static {
		startup();
	}

	/**
	 * 
	 */
	private static void startup() {
		synchronized( _lock) {
			if ( null == _stateManager) {
				_stateManager = new StateManager();
			}
		}
	}

	/**
	 * @return
	 */
	public static StateManager get_instance() {
		if ( null == _stateManager) {
			System.exit( 0);
		}

		return _stateManager;
	}

	/**
	 * 
	 */
	public StateManager() {
		super();
	}

	/**
	 * @param arg0
	 */
	public StateManager(int arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public StateManager(int arg0, float arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public StateManager(Map<String, StateBase> arg0) {
		super(arg0);
	}

	/**
	 * @param currentStateName
	 * @return
	 */
	public boolean is_current_state(String currentStateName) {
		return _currentStateName.equals( currentStateName);
	}

	/**
	 * @param defaultState
	 * @return
	 */
	public boolean setup(String defaultState) {
		_currentStateName = defaultState;
		return true;
	}

	/**
	 * 
	 */
	public void cleanup() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String stateName = ( String)entry.getKey();
			StateBase stateBase = ( StateBase)entry.getValue();
			stateBase.cleanup();
		}
		clear();
	}

	/**
	 * 
	 */
	public void reset() {
		_currentStateName = "";
	}

	/**
	 * 
	 */
	public void refresh() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String stateName = ( String)entry.getKey();
			StateBase stateBase = ( StateBase)entry.getValue();
			stateBase.refresh();
		}
	}

	/**
	 * @param newStateName
	 * @return
	 */
	public boolean change(String newStateName) {
		if ( newStateName.equals( _currentStateName))
			return true;

		StateBase currentState = get( _currentStateName);
		if ( null != currentState) {
			if ( !currentState.on_leave( newStateName))
				return false;
		}

		_currentStateName = newStateName;

		StateBase newState = get( newStateName);
		if ( null != newState) {
			if ( !newState.on_enter( newStateName))
				return false;
		}

		return true;
	}

	/**
	 * 
	 */
	public void cancel() {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.cancel();
	}

	/**
	 * @param componentEvent
	 */
	public void on_resized(ComponentEvent componentEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_resized( componentEvent);
	}

	/**
	 * @param mouseEvent
	 */
	public void on_mouse_left_double_click(MouseEvent mouseEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_mouse_left_double_click( mouseEvent);
	}

	/**
	 * @param mouseEvent
	 */
	public void on_mouse_left_down(MouseEvent mouseEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_mouse_left_down( mouseEvent);
	}

	/**
	 * @param mouseEvent
	 */
	public void on_mouse_left_up(MouseEvent mouseEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_mouse_left_up( mouseEvent);
	}

	/**
	 * @param mouseEvent
	 */
	public void on_mouse_moved(MouseEvent mouseEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_mouse_moved( mouseEvent);
	}

	/**
	 * @param mouseEvent
	 */
	public void on_mouse_right_down(MouseEvent mouseEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_mouse_right_down( mouseEvent);
	}

	/**
	 * @param mouseEvent
	 */
	public void on_mouse_right_up(MouseEvent mouseEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_mouse_right_up( mouseEvent);
	}

	/**
	 * @param mouseEvent
	 */
	public void on_mouse_dragged(MouseEvent mouseEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_mouse_dragged( mouseEvent);
	}

	/**
	 * @param mouseEvent
	 * @return
	 */
	public String get_tooltip_text(MouseEvent mouseEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return null;

		return currentState.get_tooltip_text( mouseEvent);
	}

	/**
	 * @param graphics
	 */
	public void paint(Graphics graphics) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.paint( graphics);
	}

	/**
	 * @param keyEvent
	 */
	public void on_key_pressed(KeyEvent keyEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_key_pressed( keyEvent);
	}

	/**
	 * @param keyEvent
	 */
	public void on_key_released(KeyEvent keyEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_key_released( keyEvent);
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	public boolean is_closure_connection(Vector<DrawObject> drawObjects) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return false;

		return currentState.is_closure_connection( drawObjects);
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	public boolean can_swap_names(Vector<DrawObject> drawObjects) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return false;

		return currentState.can_swap_names( drawObjects);
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	public boolean can_copy_objects(Vector<DrawObject> drawObjects) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return false;

		return currentState.can_copy_objects( drawObjects);
	}

	/**
	 * @return
	 */
	public boolean can_paste_objects() {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return false;

		return currentState.can_paste_objects();
	}

	/**
	 * @return
	 */
	public Vector<DrawObject> get_selected_drawobjects() {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return new Vector<DrawObject>();

		return currentState.get_selected_drawobjects();
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit(ActionEvent actionEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_edit( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_remove(ActionEvent actionEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_remove( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_move(ActionEvent actionEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_move( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_copy(ActionEvent actionEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_copy( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_paste(ActionEvent actionEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_paste( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_select_all(ActionEvent actionEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_select_all( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_deselect_all(ActionEvent actionEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_deselect_all( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_flush_top(ActionEvent actionEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_flush_top( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_flush_bottom(ActionEvent actionEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_flush_bottom( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_flush_left(ActionEvent actionEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_flush_left( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_flush_right(ActionEvent actionEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_flush_right( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_vertical_equal_layout(ActionEvent actionEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_vertical_equal_layout( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_horizontal_equal_layout(ActionEvent actionEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_horizontal_equal_layout( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_swap_names(ActionEvent actionEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_swap_names( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_copy_objects(ActionEvent actionEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_copy_objects( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_paste_objects(ActionEvent actionEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_paste_objects( actionEvent);
	}
}
