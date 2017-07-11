/*
 * Created on 2005/11/15
 */
package soars.application.visualshell.object.base;

import java.util.Comparator;

/**
 * The comparison function for the object's ids.
 * @author kurata / SOARS project
 */
public class DrawObjectIdComparator implements Comparator {

	/**
	 * Ceates the comparison function for the object's ids.
	 */
	public DrawObjectIdComparator() {
		super();
	}

	/* (Non Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object a, Object b) {
		DrawObject drawObjectA = ( DrawObject)a;
		DrawObject drawObjectB = ( DrawObject)b;
		if ( drawObjectA._id < drawObjectB._id)
			return -1;
		else if ( drawObjectA._id > drawObjectB._id)
			return 1;
		else
			return 0;
	}

	/* (Non Javadoc)
	 * @see java.util.Comparator#equals(java.lang.Object, java.lang.Object)
	 */
	public final boolean equals(Object a, Object b) {
		return( a == b );
	}
}
