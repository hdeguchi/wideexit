/*
 * 2005/02/18
 */
package soars.application.animator.object.transition.agent;

import java.awt.Point;

import soars.application.animator.object.entity.agent.AgentObject;
import soars.application.animator.object.entity.spot.ISpotObjectManipulator;
import soars.application.animator.object.transition.base.TransitionBase;
import soars.application.animator.object.transition.spot.SpotTransition;
import soars.application.animator.object.transition.spot.SpotTransitionManager;

/**
 * The scenario data for the agent.
 * @author kurata / SOARS project
 */
public class AgentTransition extends TransitionBase {

	/**
	 * Instance of the SpotTransitionManager class.
	 */
	public SpotTransitionManager _spotTransitionManager = null;

	/**
	 * Spot which contains the agent for this scenario data.
	 */
	public ISpotObjectManipulator _spotObjectManipulator = null;

	/**
	 * Position of the scenario.
	 */
	public int _index = 0;

	/**
	 * Creates the scenario data for the agent.
	 * @param spotTransitionManager
	 */
	public AgentTransition(SpotTransitionManager spotTransitionManager) {
		super();
		_spotTransitionManager = spotTransitionManager;
	}

//	/**
//	 * Creates the scenario data for the agent with the specified spot.
//	 * @param spotObjectManipulator the specified spot
//	 */
//	public AgentTransition(ISpotObjectManipulator spotObjectManipulator) {
//		super();
//		_spotObjectManipulator = spotObjectManipulator;
//	}
//
//	/**
//	 * Creates the scenario data for the agent with the specified one.
//	 * @param agentTransition the specified scenario data for the agent
//	 */
//	public AgentTransition(AgentTransition agentTransition) {
//		super(agentTransition);
//		_spotObjectManipulator = agentTransition._spotObjectManipulator;
//		_index = agentTransition._index;
//	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.transition.base.TransitionBase#cleanup()
	 */
	public void cleanup() {
		super.cleanup();
		_spotObjectManipulator = null;
	}

	/**
	 * Sets the specified spot.
	 * @param spotObjectManipulator the specified spot
	 */
	public void set(ISpotObjectManipulator spotObjectManipulator) {
		_spotObjectManipulator = spotObjectManipulator;
	}

	/**
	 * Sets the specified data.
	 * @param index the specified position of the scenario
	 * @param spotTransitionManager the specified scenario data for spot
	 */
	public void set(int index, SpotTransitionManager spotTransitionManager) {
		if ( null == _spotObjectManipulator)
			return;

		_index = spotTransitionManager.get_index( _spotObjectManipulator, index);
	}

	/**
	 * Returns the position of the specified agent.
	 * @param agentObject the specified agent
	 * @param index the specified position of the scenario
	 * @param pack whether the agents on the spot are packed
	 * @return the position of the specified agent
	 */
	public Point get_position(AgentObject agentObject, int index, boolean pack) {
		if ( null == _spotObjectManipulator || !_spotObjectManipulator.is_visible())
			return null;

		SpotTransition spotTransition = ( SpotTransition)_spotTransitionManager.get( _spotObjectManipulator, index);
		if ( null == spotTransition || 0 == spotTransition._size)
			return null;

		return _spotObjectManipulator.get_agent_position( agentObject, _index, spotTransition._size, pack);
	}
}
