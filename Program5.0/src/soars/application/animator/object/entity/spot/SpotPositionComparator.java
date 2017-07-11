/*
 * Created on 2005/11/14
 */
package soars.application.animator.object.entity.spot;

import java.util.Comparator;

/**
 * The comparison function for the position of spots.
 * @author kurata / SOARS project
 */
public class SpotPositionComparator implements Comparator {

	/**
	 * 
	 */
	private boolean _vertical = true;

	/**
	 * Creates the comparison function for the position of spots.
	 * @param vertical true for the comparison of the vertical position
	 */
	public SpotPositionComparator(boolean vertical) {
		super();
		_vertical = vertical;
	}

	/* (Non Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public final int compare(Object a, Object b) {
		ISpotObjectManipulator spotObjectManipulatorA = ( ISpotObjectManipulator)a;
		ISpotObjectManipulator spotObjectManipulatorB = ( ISpotObjectManipulator)b;
		if ( _vertical) {
			if ( spotObjectManipulatorA.get_position().y < spotObjectManipulatorB.get_position().y)
				return -1;
			else if ( spotObjectManipulatorA.get_position().y > spotObjectManipulatorB.get_position().y)
				return 1;
			else
				return 0;
		} else {
			if ( spotObjectManipulatorA.get_position().x < spotObjectManipulatorB.get_position().x)
				return -1;
			else if ( spotObjectManipulatorA.get_position().x > spotObjectManipulatorB.get_position().x)
				return 1;
			else
				return 0;
		}
	}

	/* (Non Javadoc)
	 * @see java.util.Comparator#equals(java.lang.Object, java.lang.Object)
	 */
	public final boolean equals(Object a, Object b) {
		return( a == b );
	}
}
