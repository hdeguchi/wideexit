/**
 * 
 */
package soars.application.animator.common.tool;

import java.util.Comparator;

/**
 * The comparison function for the step strings of SOARS log file.
 * @author kurata / SOARS project
 */
public class TimeComparator implements Comparator {

	/**
	 * Creates the comparison function for the step strings of SOARS log file.
	 */
	public TimeComparator() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {
		double time0 = get_double( ( String)arg0);
		double time1 = get_double( ( String)arg1);
		if ( time0 < time1)
			return -1;
		else if ( time0 > time1)
			return 1;
		else
			return 0;
	}

	/**
	 * Returns the double value from the specified step string of SOARS.
	 * @param time the specified step string of SOARS
	 * @return the double value from the specified step string of SOARS
	 */
	public static double get_double(String time) {
		String[] words = time.split( "[/:]");
		String word = "";
		for ( int i = 0; i < words.length; ++i)
			word += words[ i];

		double result;
		try {
			result = Double.parseDouble( word);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return -1.0;
		}
		return result;
	}
}
