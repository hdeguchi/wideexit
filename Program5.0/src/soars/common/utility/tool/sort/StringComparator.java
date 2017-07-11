/*
 * Created on 2006/06/27
 */
package soars.common.utility.tool.sort;

import java.util.Comparator;

/**
 * @author kurata
 */
public class StringComparator implements Comparator {

	/**
	 * 
	 */
	private boolean _ascend = true;

	/**
	 * 
	 */
	private boolean _toLower = true;

	/**
	 * @param ascend
	 * @param toLower
	 */
	public StringComparator(boolean ascend, boolean toLower) {
		super();
		_ascend = ascend;
		_toLower = toLower;
	}

	/* (Non Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 * 
	 * Compare two Strings. Callback for sort.
	 * effectively returns arg0-arg1;
	 * e.g. +1 (or any +ve number) if arg0 > arg1
	 *       0                     if arg0 == arg1
	 *      -1 (or any -ve number) if arg0 < arg1
	 */
	public final int compare(Object arg0, Object arg1) {
		if ( _toLower) {
			if ( _ascend)
				return( ( ( String)arg0).toLowerCase()).compareTo( ( ( String)arg1).toLowerCase());
			else
				return( ( ( String)arg1).toLowerCase()).compareTo( ( ( String)arg0).toLowerCase());
		} else {
			if ( _ascend)
				return( ( String)arg0).compareTo( ( String)arg1);
			else
				return( ( String)arg1).compareTo( ( String)arg0);
		}
	}

	/* (Non Javadoc)
	 * @see java.util.Comparator#equals(java.lang.Object, java.lang.Object)
	 */
	public final boolean equals(Object arg0, Object arg1) {
		return ( arg0 == arg1);
	}
}
