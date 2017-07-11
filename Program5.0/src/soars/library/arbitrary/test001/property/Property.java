/**
 * 
 */
package soars.library.arbitrary.test001.property;

import javax.swing.JOptionPane;

/**
 * @author kurata
 *
 */
public class Property {

	/**
	 * 
	 */
	public static void execute1() {
		String property = System.getProperty( "SOARS_USER_DATA_DIRECTORY");
		JOptionPane.showMessageDialog( null,
			( null == property) ? "Could not get!" : ( "SOARS_USER_DATA_DIRECTORY = " + property), "SOARS", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * 
	 */
	public static void execute2() {
		String property = System.getProperty( "SOARS_MEMORY_SIZE");
		JOptionPane.showMessageDialog( null,
			( null == property) ? "Could not get!" : ( "SOARS_MEMORY_SIZE = " + property), "SOARS", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * 
	 */
	public static void execute3() {
		String property = System.getProperty( "SOARS_USER_DATA_DIRECTORY");
		System.out.println( ( null == property) ? "Could not get!" : ( "SOARS_USER_DATA_DIRECTORY = " + property));
	}

	/**
	 * 
	 */
	public static void execute4() {
		String property = System.getProperty( "SOARS_MEMORY_SIZE");
		System.out.println( ( null == property) ? "Could not get!" : ( "SOARS_MEMORY_SIZE = " + property));
	}
}
