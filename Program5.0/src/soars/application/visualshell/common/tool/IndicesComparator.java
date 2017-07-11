/*
 * Created on 2006/02/07
 */
package soars.application.visualshell.common.tool;

import java.util.Comparator;

/**
 * The comparison function for the set of numbers.
 * @author kurata / SOARS project
 */
public class IndicesComparator implements Comparator {

	/**
	 * Creates the comparison function for the set of numbers.
	 */
	public IndicesComparator() {
		super();
	}

	/* (Non Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {
		int[] range0 = ( int[])arg0;
		int[] range1 = ( int[])arg1;
		if ( range0[ 0] < range1[ 0])
			return -1;
		else if ( range0[ 0] > range1[ 0])
			return 1;
		else
			return 0;
	}

	/* (Non Javadoc)
	 * @see java.util.Comparator#equals(java.lang.Object, java.lang.Object)
	 */
	public final boolean equals(Object a, Object b) {
		return ( a == b);
	}
}
