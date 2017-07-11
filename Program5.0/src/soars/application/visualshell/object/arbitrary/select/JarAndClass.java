/**
 * 
 */
package soars.application.visualshell.object.arbitrary.select;

/**
 * @author kurata
 *
 */
public class JarAndClass {

	/**
	 * 
	 */
	public String _jarFilename = "";

	/**
	 * 
	 */
	public String _classname = "";

	/**
	 * @param jarFilename
	 * @param classname
	 */
	public JarAndClass(String jarFilename, String classname) {
		super();
		_jarFilename = jarFilename;
		_classname = classname;
	}

	/**
	 * @param jarFilename
	 * @param classname
	 * @return
	 */
	public boolean equals(String jarFilename, String classname) {
		return ( _jarFilename.equals( jarFilename) && _classname.equals( classname));
	}
}
