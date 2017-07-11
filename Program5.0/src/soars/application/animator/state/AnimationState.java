/*
 * 2004/05/14
 */
package soars.application.animator.state;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import soars.application.animator.main.Administrator;
import soars.application.animator.main.ResourceManager;
import soars.application.animator.main.internal.ObjectManager;
import soars.application.animator.object.entity.agent.AgentObject;
import soars.application.animator.object.entity.spot.ISpotObjectManipulator;
import soars.application.animator.setting.common.CommonProperty;
import soars.application.animator.state.menu.animation.AnimationSliderAction;
import soars.application.animator.state.menu.animation.RetrieveAgentPropertyAction;
import soars.application.animator.state.menu.animation.RetrieveSpotPropertyAction;
import soars.common.utility.swing.gui.UserInterface;

/**
 * The "State pattern" class for animation.
 * @author kurata / SOARS project
 */
public class AnimationState extends StateBase {

	/**
	 * 
	 */
	private boolean _loading = false;

	/**
	 * 
	 */
	private boolean _pausing = false;

	/**
	 * 
	 */
	private boolean _drawing = false;

	/**
	 * 
	 */
	private UserInterface _userInterface = null;

	/**
	 * 
	 */
	private JPopupMenu _popupMenu = null;

	/**
	 * 
	 */
	private JMenuItem _retrieveAgentPropertyMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _retrieveSpotPropertyMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _animationSliderMenuItem = null;

	/**
	 * Creates the instance of the "State pattern" class for animation.
	 * @param objectManager 
	 */
	public AnimationState(ObjectManager objectManager) {
		super(objectManager);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#setup()
	 */
	public boolean setup() {
		setup_popup_menu();
		return super.setup();
	}

	/**
	 * 
	 */
	private void setup_popup_menu() {
		_userInterface = new UserInterface();
		_popupMenu = new JPopupMenu();

		_retrieveAgentPropertyMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "animation.retrieve.agent.property.menu"),
			new RetrieveAgentPropertyAction( ResourceManager.get_instance().get( "animation.retrieve.agent.property.menu"), this),
			ResourceManager.get_instance().get( "animation.retrieve.agent.property.mnemonic"),
			ResourceManager.get_instance().get( "animation.retrieve.agent.property.stroke"));


		_retrieveSpotPropertyMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "animation.retrieve.spot.property.menu"),
			new RetrieveSpotPropertyAction( ResourceManager.get_instance().get( "animation.retrieve.spot.property.menu"), this),
			ResourceManager.get_instance().get( "animation.retrieve.spot.property.mnemonic"),
			ResourceManager.get_instance().get( "animation.retrieve.spot.property.stroke"));

		_popupMenu.addSeparator();

		_animationSliderMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "animation.slider.menu"),
			new AnimationSliderAction( ResourceManager.get_instance().get( "animation.slider.menu"), this),
			ResourceManager.get_instance().get( "animation.slider.mnemonic"),
			ResourceManager.get_instance().get( "animation.slider.stroke"));
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#cleanup()
	 */
	public void cleanup() {
		super.cleanup();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_enter()
	 */
	public boolean on_enter(String newStateName) {
		_loading = false;
		_drawing = false;
		_pausing = false;
		_objectManager.prepare_for_animation();
		Administrator.get_instance().on_enter_animation_state();
		return super.on_enter(newStateName);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_leave()
	 */
	public boolean on_leave(String newStateName) {
		_objectManager._scenarioManager.set_current_position( 0);
		Administrator.get_instance().on_leave_animation_state();
		return super.on_leave(newStateName);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_resized(java.awt.event.ComponentEvent)
	 */
	public void on_resized(ComponentEvent componentEvent) {
		super.on_resized(componentEvent);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#paint(java.awt.Graphics)
	 */
	public void paint(Graphics graphics) {
		_objectManager.animate( graphics);
		super.paint(graphics);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_backward_head()
	 */
	public void on_backward_head() {
		// 現在は使用していない→update_current_positionを使用している
		_pausing = true;
		_objectManager._scenarioManager.set_current_position( 0);
		_objectManager._scenarioManager.enable_retrieve_property_dialog( true);
		_objectManager._scenarioManager.enable_animation_slider_dialog( true);
		animate( false, true);
		super.on_backward_head();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_backward()
	 */
	public void on_backward() {
		// 現在は使用していない→update_current_positionを使用している
		_pausing = true;
		_objectManager._scenarioManager.backward();
		_objectManager._scenarioManager.enable_retrieve_property_dialog( true);
		_objectManager._scenarioManager.enable_animation_slider_dialog( true);
		animate( false, true);
		super.on_backward();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_backward_step()
	 */
	public void on_backward_step() {
		// 現在は使用していない→update_current_positionを使用している
		_pausing = true;
		_objectManager._scenarioManager.backward_step();
		_objectManager._scenarioManager.enable_retrieve_property_dialog( true);
		_objectManager._scenarioManager.enable_animation_slider_dialog( true);
		animate( false, true);
		super.on_backward_step();
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.state.StateBase#on_play(boolean)
	 */
	public void on_play(boolean bStart) {
		_objectManager._scenarioManager.enable_retrieve_property_dialog( false);
		_objectManager._scenarioManager.enable_animation_slider_dialog( false);
		_drawing = false;
		if ( bStart) {
			if ( !_pausing)
				_objectManager._scenarioManager.set_current_position( 0);

			_pausing = false;
		}
		super.on_play( bStart);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_pause()
	 */
	public void on_pause() {
		_pausing = true;
		_objectManager._scenarioManager.enable_retrieve_property_dialog( true);
		_objectManager._scenarioManager.enable_animation_slider_dialog( true);
		animate( false, true);
		super.on_pause();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_forward_step()
	 */
	public void on_forward_step() {
		// 現在は使用していない→update_current_positionを使用している
		_pausing = true;
		_objectManager._scenarioManager.forward_step();
		_objectManager._scenarioManager.enable_retrieve_property_dialog( true);
		_objectManager._scenarioManager.enable_animation_slider_dialog( true);
		animate( false, true);
		super.on_forward_step();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_forward()
	 */
	public void on_forward() {
		// 現在は使用していない→update_current_positionを使用している
		_pausing = true;
		_objectManager._scenarioManager.forward();
		_objectManager._scenarioManager.enable_retrieve_property_dialog( true);
		_objectManager._scenarioManager.enable_animation_slider_dialog( true);
		animate( false, true);
		super.on_forward();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.state.StateBase#on_forward_tail()
	 */
	public void on_forward_tail() {
		// 現在は使用していない→update_current_positionを使用している
		_pausing = true;
		_objectManager._scenarioManager.forward_tail();
		_objectManager._scenarioManager.enable_retrieve_property_dialog( true);
		_objectManager._scenarioManager.enable_animation_slider_dialog( true);
		animate( false, true);
		super.on_forward_tail();
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.state.StateBase#update_current_position(int, boolean)
	 */
	public void update_current_position(int delta, boolean slider) {
		_pausing = true;
		_objectManager._scenarioManager.set_current_position( _objectManager._scenarioManager.get_current_position() + delta);
		_objectManager._scenarioManager.enable_retrieve_property_dialog( true);
		_objectManager._scenarioManager.enable_animation_slider_dialog( true);
		animate( false, slider);
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.state.StateBase#on_stop_timer()
	 */
	public void on_stop_timer() {
		_loading = false;
		_drawing = false;
		_pausing = false;
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.state.StateBase#load_on_timer_task()
	 */
	public void load_on_timer_task() {
		if ( _loading)
			return;

		_loading = true;

		_objectManager._scenarioManager.read();

		_loading = false;
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.state.StateBase#animate_on_timer_task()
	 */
	public void animate_on_timer_task() {
		if ( _drawing)
			return;

		if ( _objectManager._scenarioManager.is_tail()) {
			if ( CommonProperty.get_instance()._repeat)
				_objectManager._scenarioManager.rewind();
			else {
				_drawing = true;
				animate( false, true);
				_drawing = false;
				return;
			}
		}

		_drawing = true;
		animate( true, true);
		_drawing = false;
	}

	/**
	 * @param next
	 * @param slider
	 */
	private void animate(boolean next, boolean slider) {
		_objectManager._animatorView.set_title();
		_objectManager._animatorView.getParent().repaint();
		if ( slider)
			_objectManager._scenarioManager.update_animation_slider_dialog();
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.state.StateBase#get_tooltip_text(java.awt.event.MouseEvent)
	 */
	public String get_tooltip_text(MouseEvent mouseEvent) {
		if ( !_pausing)
			return null;

		AgentObject agentObject = get_agent( mouseEvent.getPoint());
		if ( null != agentObject)
			return agentObject.get_tooltip_text( "animation");
		else {
			ISpotObjectManipulator spotObjectManipulator = _objectManager._spotObjectManager.get_spot( mouseEvent.getPoint());
			return ( ( null == spotObjectManipulator)
				? null : spotObjectManipulator.get_tooltip_text( "animation"));
		}
	}

	/**
	 * @param mouse_position
	 * @return
	 */
	private AgentObject get_agent(Point mouse_position) {
		AgentObject agentObject = _objectManager._agentObjectManager.get_agent( mouse_position, "animation");
		if ( null == agentObject)
			return null;

		ISpotObjectManipulator spotObjectManipulator = _objectManager._scenarioManager.get_spot( agentObject);
		if ( null == spotObjectManipulator)
			return null;

		return agentObject;
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.state.StateBase#on_mouse_right_up(java.awt.event.MouseEvent)
	 */
	public void on_mouse_right_up(MouseEvent mouseEvent) {
		_retrieveAgentPropertyMenuItem.setEnabled( _pausing);
		_retrieveSpotPropertyMenuItem.setEnabled( _pausing && _objectManager._spotLog[ 0].equals( "agents") && _objectManager._spotLog[ 1].equals( "$Spot"));
		_animationSliderMenuItem.setEnabled( _pausing);
		_popupMenu.show( _objectManager._animatorView, mouseEvent.getX(), mouseEvent.getY());
	}

	/**
	 * @param actionEvent
	 */
	public void on_animation_slider(ActionEvent actionEvent) {
		_objectManager._animatorView.on_animation_slider();
	}

	/**
	 * @param actionEvent
	 */
	public void on_retrieve_agent_property(ActionEvent actionEvent) {
		_objectManager._animatorView.on_retrieve_agent_property();
	}

	/**
	 * @param actionEvent
	 */
	public void on_retrieve_spot_property(ActionEvent actionEvent) {
		_objectManager._animatorView.on_retrieve_spot_property();
	}
}
