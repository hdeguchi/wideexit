/*
 * 2005/03/28
 */
package soars.application.animator.state.menu.animation;

import java.awt.event.ActionEvent;

import soars.application.animator.state.AnimationState;
import soars.common.utility.swing.menu.MenuAction;

/**
 * The menu handler of "Retrieve spot property" menu.
 * @author kurata / SOARS project
 */
public class RetrieveSpotPropertyAction extends MenuAction {

	/**
	 * 
	 */
	private AnimationState _animationState = null;

	/**
	 * Creates the menu handler of "Retrieve spot property" menu
	 * @param name the Menu name
	 * @param animationState the instance of the AnimationState class
	 */
	public RetrieveSpotPropertyAction(String name, AnimationState animationState) {
		super(name);
		_animationState = animationState;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.menu.MenuAction#selected(java.awt.event.ActionEvent)
	 */
	public void selected(ActionEvent actionEvent) {
		_animationState.on_retrieve_spot_property(actionEvent);
	}
}
