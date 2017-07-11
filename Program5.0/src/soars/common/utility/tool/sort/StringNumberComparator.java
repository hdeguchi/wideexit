/*
 * Created on 2006/06/27
 */
package soars.common.utility.tool.sort;

import java.util.Comparator;

/**
 * @author kurata
 */
public class StringNumberComparator implements Comparator {

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
	public StringNumberComparator(boolean ascend, boolean toLower) {
		super();
		_ascend = ascend;
		_toLower = toLower;
	}

	/* (Non Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public final int compare(Object arg0, Object arg1) {
		if ( _toLower) {
			if ( _ascend)
				return compareTo( ( ( String)arg0).toLowerCase(), ( ( String)arg1).toLowerCase());
			else
				return compareTo( ( ( String)arg1).toLowerCase(), ( ( String)arg0).toLowerCase());
		} else {
			if ( _ascend)
				return compareTo( ( String)arg0, ( String)arg1);
			else
				return compareTo( ( String)arg1, ( String)arg0);
		}
	}

	/**
	 * @param value0
	 * @param value1
	 * @return
	 */
	public static int compareTo(String value0, String value1) {
		String head0 = separate( value0);
		String head1 = separate( value1);
		if ( head0.equals( value0) || head1.equals( value1) || !head0.equals( head1))
			return value0.compareTo( value1);

		String number0 = value0.substring( head0.length());
		String number1 = value1.substring( head1.length());

		int n0;
		try {
			n0 = Integer.parseInt( number0);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return value0.compareTo( value1);
		}

		int n1;
		try {
			n1 = Integer.parseInt( number1);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return value0.compareTo( value1);
		}

		if ( n0 > n1)
			return 1;
		else if ( n0 == n1)
			return 0;
		else
			return -1;
	}

	/**
	 * @param value
	 * @return
	 */
	public static String separate(String value) {
		if ( value.equals( ""))
			return value;

		int index = value.length() - 1;
		while ( index >= 0) {
			char c = value.charAt( index);
			if ( 0 > "0123456789".indexOf( c))
				break;

			--index;
		}

		if ( value.length() - 1 == index)
			return value;

		index += 1;
		while ( index < value.length()) {
			char c = value.charAt( index);
			if ( '0' != c)
				break;

			++index;
		}

		return value.substring( 0, index);
	}

	/* (Non Javadoc)
	 * @see java.util.Comparator#equals(java.lang.Object, java.lang.Object)
	 */
	public final boolean equals(Object arg0, Object arg1) {
		return ( arg0 == arg1);
	}
}
