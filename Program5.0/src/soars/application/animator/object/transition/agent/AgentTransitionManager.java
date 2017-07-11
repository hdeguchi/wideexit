/*
 * 2005/02/25
 */
package soars.application.animator.object.transition.agent;

import java.awt.Point;
import java.util.Map;

import soars.application.animator.object.entity.agent.AgentObject;
import soars.application.animator.object.entity.spot.ISpotObjectManipulator;
import soars.application.animator.object.transition.base.TransitionBase;
import soars.application.animator.object.transition.base.TransitionManager;
import soars.application.animator.object.transition.spot.SpotTransitionManager;

/**
 * The scenario data manager for agents.
 * @author kurata / SOARS project
 */
public class AgentTransitionManager extends TransitionManager {

	/**
	 * Instance of the SpotTransitionManager class.
	 */
	public SpotTransitionManager _spotTransitionManager = null;

	/**
	 * Creates the scenario data manager for agents.
	 * @param spotTransitionManager
	 */
	public AgentTransitionManager(SpotTransitionManager spotTransitionManager) {
		super();
		_spotTransitionManager = spotTransitionManager;
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.transition.base.TransitionManager#createTransition()
	 */
	protected TransitionBase createTransition() {
		return new AgentTransition( _spotTransitionManager);
	}

	/**
	 * Sets the specified spot to the scenario data for the specified agent.
	 * @param index the positon of the scenario
	 * @param agentObject the specified agent
	 * @param spotObjectManipulator the specified spot
	 */
	public void set(int index, AgentObject agentObject, ISpotObjectManipulator spotObjectManipulator) {
		Map agentTransitionMap = ( Map)get( index);
		AgentTransition agentTransition = ( AgentTransition)agentTransitionMap.get( agentObject);
		if ( null == agentTransition) {
			agentTransition = new AgentTransition( _spotTransitionManager);
			agentTransitionMap.put( agentObject, agentTransition);
		}
		agentTransition.set( spotObjectManipulator);
	}

	/**
	 * Sets the specified data to the scenario data for the specified agent.
	 * @param index the positon of the scenario
	 * @param agentObject the specified agent
	 * @param spotTransitionManager the specified scenario data for spot
	 */
	public void set(int index, AgentObject agentObject, SpotTransitionManager spotTransitionManager) {
		Map agentTransitionMap = ( Map)get( index);
		AgentTransition agentTransition = ( AgentTransition)agentTransitionMap.get( agentObject);
		if ( null == agentTransition) {
			agentTransition = new AgentTransition( _spotTransitionManager);
			agentTransitionMap.put( agentObject, agentTransition);
		}
		agentTransition.set( index, spotTransitionManager);
	}

	/**
	 * Returns the spot which contains the specified agent.
	 * @param agentObject the specified agent
	 * @param index the position of the scenario
	 * @return the spot which contains the specified agent
	 */
	public ISpotObjectManipulator get_spot(AgentObject agentObject, int index) {
		if ( size() <= index)
			return null;

		Map agentTransitionMap = ( Map)get( index);
		AgentTransition agentTransition = ( AgentTransition)agentTransitionMap.get( agentObject);
		if ( null == agentTransition)
			return null;

		if ( null == agentTransition._spotObjectManipulator)
			return null;

		return ( !agentTransition._spotObjectManipulator.is_visible())
			? null : agentTransition._spotObjectManipulator;
	}

	/**
	 * Returns the position of the specified agent.
	 * @param agentObject specified agent
	 * @param index the position of the scenario
	 * @param pack whether the agents on the spot are packed
	 * @return the position of the specified agent
	 */
	public Point get_position(AgentObject agentObject, int index, boolean pack) {
		if ( size() <= index) {
			//System.out.println( index + "/" + size());
			return null;
		}

		Map agentTransitionMap = ( Map)get( index);
		AgentTransition agentTransition = ( AgentTransition)agentTransitionMap.get( agentObject);
		if ( null == agentTransition)
			return null;

		return agentTransition.get_position( agentObject, index, pack);
	}
}
