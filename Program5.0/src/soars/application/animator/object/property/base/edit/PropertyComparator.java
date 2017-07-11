/**
 * 
 */
package soars.application.animator.object.property.base.edit;

import java.util.Comparator;

import soars.application.animator.object.property.base.PropertyBase;
import soars.common.utility.tool.sort.StringNumberComparator;

/**
 * @author kurata
 *
 */
public class PropertyComparator implements Comparator {

	/**
	 * 
	 */
	private boolean _ascend = true;

	/**
	 * 
	 */
	private boolean _to_lower = true;

	/**
	 * @param ascend 
	 * @param to_lower
	 * 
	 */
	public PropertyComparator(boolean ascend, boolean to_lower) {
		super();
		_ascend = ascend;
		_to_lower = to_lower;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {
		PropertyBase a = ( PropertyBase)arg0;
		PropertyBase b = ( PropertyBase)arg1;
		if ( is_number( a._value) && is_number( b._value)) {
			double d1 = Double.parseDouble( a._value);
			double d2 = Double.parseDouble( b._value);
			if ( _ascend)
				return compareTo( d1, d2);
			else
				return compareTo( d2, d1);
		} else {
			if ( _to_lower) {
				if ( _ascend)
					return StringNumberComparator.compareTo( a._value.toLowerCase(), b._value.toLowerCase());
				else
					return StringNumberComparator.compareTo( b._value.toLowerCase(), a._value.toLowerCase());
			} else {
				if ( _ascend)
					return StringNumberComparator.compareTo( a._value, b._value);
				else
					return StringNumberComparator.compareTo( b._value, a._value);
			}
		}
	}

	/**
	 * @param value
	 * @return
	 */
	private boolean is_number(String value) {
		try {
			double d = Double.parseDouble( value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param d1
	 * @param d2
	 * @return
	 */
	private int compareTo(double d1, double d2) {
		if ( d1 > d2)
			return 1;
		else if ( d1 == d2)
			return 0;
		else
			return -1;
	}
}
