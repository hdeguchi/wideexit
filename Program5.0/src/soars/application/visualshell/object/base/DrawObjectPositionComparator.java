/*
 * Created on 2005/11/14
 */
package soars.application.visualshell.object.base;

import java.util.Comparator;

/**
 * The comparison function for the object's positions.
 * @author kurata / SOARS project
 */
public class DrawObjectPositionComparator implements Comparator {

	/**
	 * 
	 */
	private boolean _vertical = true;

	/**
	 * Creates the comparison function for the object's positions.
	 * @param vertical true for making vertical gaps between the selected objects equal
	 */
	public DrawObjectPositionComparator(boolean vertical) {
		super();
		_vertical = vertical;
	}

	/* (Non Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public final int compare(Object a, Object b) {
		DrawObject drawObjectA = ( DrawObject)a;
		DrawObject drawObjectB = ( DrawObject)b;
		if ( _vertical) {
			if ( drawObjectA._position.y < drawObjectB._position.y)
				return -1;
			else if ( drawObjectA._position.y > drawObjectB._position.y)
				return 1;
			else
				return 0;
		} else {
			if ( drawObjectA._position.x < drawObjectB._position.x)
				return -1;
			else if ( drawObjectA._position.x > drawObjectB._position.x)
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
