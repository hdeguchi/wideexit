/*
 * Created on 2006/10/18
 */
package soars.common.utility.tool.sort;

import java.util.Comparator;

/**
 * @author kurata
 */
public class IntegerComparator implements Comparator {

	/**
	 * 
	 */
	private boolean _ascend = true;

	/**
	 * @param ascend
	 */
	public IntegerComparator(boolean ascend) {
		super();
		_ascend = ascend;
	}

	/* (Non Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {
		if ( _ascend)
			return( ( Integer)arg0).compareTo( ( Integer)arg1);
		else
			return( ( Integer)arg1).compareTo( ( Integer)arg0);
	}

	/* (Non Javadoc)
	 * @see java.util.Comparator#equals(java.lang.Object, java.lang.Object)
	 */
	public final boolean equals(Object arg0, Object arg1) {
		return ( arg0 == arg1);
	}
}
