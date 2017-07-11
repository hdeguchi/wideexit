/*
 * 2005/02/18
 */
package soars.application.animator.object.transition.spot;

import soars.application.animator.object.transition.base.TransitionBase;

/**
 * The scenario data for the spot.
 * @author kurata / SOARS project
 */
public class SpotTransition extends TransitionBase {

	/**
	 * The number of the agents on the spot for this scenario data. 
	 */
	public int _size = 0;

	/**
	 * Creates the scenario data for the spot.
	 */
	public SpotTransition() {
		super();
	}

	/**
	 * Creates the scenario data for the spot with the specified one.
	 * @param spotTransition the specified scenario data for the spot
	 */
	public SpotTransition(SpotTransition spotTransition) {
		super(spotTransition);
		_size = spotTransition._size;
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.transition.base.TransitionBase#cleanup()
	 */
	public void cleanup() {
		super.cleanup();
		_size = 0;
	}

	/**
	 * Returns the number of agents on the spot for this scenario data.
	 * @return the number of agents on the spot for this scenario data
	 */
	public int get_index() {
		return _size++;
	}
}
