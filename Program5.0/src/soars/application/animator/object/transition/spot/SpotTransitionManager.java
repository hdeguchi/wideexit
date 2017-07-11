/*
 * 2005/02/25
 */
package soars.application.animator.object.transition.spot;

import java.util.Map;

import soars.application.animator.object.entity.spot.ISpotObjectManipulator;
import soars.application.animator.object.transition.base.TransitionBase;
import soars.application.animator.object.transition.base.TransitionManager;

/**
 * The scenario data manager for spots.
 * @author kurata / SOARS project
 */
public class SpotTransitionManager extends TransitionManager {

	/**
	 * Creates the scenario data manager for spots.
	 */
	public SpotTransitionManager() {
		super();
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.transition.base.TransitionManager#createTransition()
	 */
	protected TransitionBase createTransition() {
		return new SpotTransition();
	}

	/**
	 * Returns the number of agents on the specified spot.
	 * @param spotObjectManipulator the specified spot
	 * @param index the specified position of the scenario
	 * @return the number of agents on the specified spot
	 */
	public int get_index(ISpotObjectManipulator spotObjectManipulator, int index) {
		Map spotTransitionMap = ( Map)get( index);
		SpotTransition spotTransition = ( SpotTransition)spotTransitionMap.get( spotObjectManipulator);
		if ( null == spotTransition) {
			spotTransition = new SpotTransition();
			spotTransitionMap.put( spotObjectManipulator, spotTransition);
		}
		return spotTransition.get_index();
	}
}
