/*
 * 2004/05/14
 */
package soars.application.animator.state;

import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The "State pattern" manager class.
 * @author kurata / SOARS project
 */
public class StateManager extends HashMap<String, StateBase> {

	/**
	 * 
	 */
	private String _currentStateName = "";

	/**
	 * 
	 */
	public StateManager() {
		super();
	}

	/**
	 * Returns true if the current state is the specified state.
	 * @param currentStateName the specified state
	 * @return true if the current state is the specified state
	 */
	public boolean is_current_state(String currentStateName) {
		return _currentStateName.equals( currentStateName);
	}

	/**
	 * Returns true if this object is initialized successfully.
	 * @param defaultState the default state
	 * @return true if this object is initialized successfully
	 */
	public boolean setup(String defaultState) {
		_currentStateName = defaultState;
		return true;
	}

	/**
	 * Clears all.
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
	 * Resets all.
	 */
	public void reset() {
		_currentStateName = "";
	}

	/**
	 * Returns true for changing the current state into the specified state successfully.
	 * @param newStateName the specified state
	 * @return true for changing the current state into the specified state successfully
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
	 * Cancels all selections of objects.
	 */
	public void cancel() {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.cancel();
	}

	/**
	 * Invoked when the position or size of the main window is changed.
	 * @param componentEvent a low-level event which indicates that a component moved, changed size, or changed visibility (also, the root class for the other component-level events)
	 */
	public void on_resized(ComponentEvent componentEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_resized( componentEvent);
	}

	/**
	 * Invoked when the mouse button is double clicked.
	 * @param mouseEvent an event which indicates that a mouse action occurred in a component
	 */
	public void on_mouse_left_double_click(MouseEvent mouseEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_mouse_left_double_click( mouseEvent);
	}

	/**
	 * Invoked when the mouse left button is pressed.
	 * @param mouseEvent an event which indicates that a mouse action occurred in a component
	 */
	public void on_mouse_left_down(MouseEvent mouseEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_mouse_left_down( mouseEvent);
	}

	/**
	 * Invoked when the mouse left button is released.
	 * @param mouseEvent an event which indicates that a mouse action occurred in a component
	 */
	public void on_mouse_left_up(MouseEvent mouseEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_mouse_left_up( mouseEvent);
	}

	/**
	 * Invoked when the mouse is moved.
	 * @param mouseEvent an event which indicates that a mouse action occurred in a component
	 */
	public void on_mouse_moved(MouseEvent mouseEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_mouse_moved( mouseEvent);
	}

	/**
	 * Invoked when the mouse right button is pressed.
	 * @param mouseEvent an event which indicates that a mouse action occurred in a component
	 */
	public void on_mouse_right_down(MouseEvent mouseEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_mouse_right_down( mouseEvent);
	}

	/**
	 * Invoked when the mouse right button is released.
	 * @param mouseEvent an event which indicates that a mouse action occurred in a component
	 */
	public void on_mouse_right_up(MouseEvent mouseEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_mouse_right_up( mouseEvent);
	}

	/**
	 * Invoked when the mouse is dragged.
	 * @param mouseEvent an event which indicates that a mouse action occurred in a component
	 */
	public void on_mouse_dragged(MouseEvent mouseEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_mouse_dragged( mouseEvent);
	}

	/**
	 * Returns the appropriate tool tip text.
	 * @param mouseEvent an event which indicates that a mouse action occurred in a component
	 * @return the appropriate tool tip text
	 */
	public String get_tooltip_text(MouseEvent mouseEvent) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return null;

		return currentState.get_tooltip_text( mouseEvent);
	}

	/**
	 * On drawing. 
	 * @param graphics the graphics object of JAVA
	 */
	public void paint(Graphics graphics) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.paint( graphics);
	}

	/**
	 * Invoked when the key is pressed.
	 * @param key
	 */
	public void on_key_pressed(String key) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_key_pressed( key);
	}

	/**
	 * Goes backward head.
	 */
	public void on_backward_head() {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_backward_head();
	}

	/**
	 * Goes backward.
	 */
	public void on_backward() {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_backward();
	}

	/**
	 * Goes backward step.
	 */
	public void on_backward_step() {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_backward_step();
	}

	/**
	 * Starts or Restarts the animation.
	 * @param bStart
	 */
	public void on_play(boolean bStart) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_play( bStart);
	}

	/**
	 * Pauses the animation.
	 */
	public void on_pause() {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_pause();
	}

	/**
	 * Goes forward step.
	 */
	public void on_forward_step() {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_forward_step();
	}

	/**
	 * Goes forward.
	 */
	public void on_forward() {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_forward();
	}

	/**
	 * Goes forward tail.
	 */
	public void on_forward_tail() {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_forward_tail();
	}

	/**
	 * Sets the current position of the animation with the specified position.
	 * @param delta the specified position delta
	 * @param slider whether the position slider is updated.
	 */
	public void update_current_position(int delta, boolean slider) {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.update_current_position( delta, slider);
	}

	/**
	 * 
	 */
	public void on_start_timer() {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_start_timer();
	}

	/**
	 * 
	 */
	public void on_stop_timer() {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.on_stop_timer();
	}

	/**
	 * 
	 */
	public void load_on_timer_task() {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.load_on_timer_task();
	}

	/**
	 * 
	 */
	public void animate_on_timer_task() {
		StateBase currentState = get( _currentStateName);
		if ( null == currentState)
			return;

		currentState.animate_on_timer_task();
	}
}
