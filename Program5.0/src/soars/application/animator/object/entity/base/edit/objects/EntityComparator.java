/*
 * 2005/03/15
 */
package soars.application.animator.object.entity.base.edit.objects;

import java.util.Comparator;

import soars.application.animator.object.entity.base.EntityBase;
import soars.common.utility.tool.sort.StringNumberComparator;

/**
 * The comparison function for the object.
 * @author kurata / SOARS project
 */
public class EntityComparator implements Comparator {

	/**
	 * 
	 */
	private boolean _ascend = true;

	/**
	 * 
	 */
	private boolean _toLower = true;

	/**
	 * Creates the comparison function for the object.
	 */
	public EntityComparator(boolean ascend, boolean toLower) {
		super();
		_ascend = ascend;
		_toLower = toLower;
	}

	/* (Non Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public final int compare(Object arg0, Object arg1) {
		EntityBase a = ( EntityBase)arg0;
		EntityBase b = ( EntityBase)arg1;
		if ( _toLower) {
			if ( _ascend)
				return StringNumberComparator.compareTo( a._name.toLowerCase(), b._name.toLowerCase());
			else
				return StringNumberComparator.compareTo( b._name.toLowerCase(), a._name.toLowerCase());
		} else {
			if ( _ascend)
				return StringNumberComparator.compareTo( a._name, b._name);
			else
				return StringNumberComparator.compareTo( b._name, a._name);
		}
	}

	/* (Non Javadoc)
	 * @see java.util.Comparator#equals(java.lang.Object, java.lang.Object)
	 */
	public final boolean equals(Object arg0, Object arg1) {
		EntityBase a = ( EntityBase)arg0;
		EntityBase b = ( EntityBase)arg1;
		return( a._name.equals( b._name));
	}
}
