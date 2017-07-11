/**
 * 
 */
package soars.application.animator.file.loader;

/**
 * @author kurata
 *
 */
public class FileProperty {

	/**
	 * 
	 */
	public String _filename = null;

	/**
	 * 
	 */
	public String _type = null;

	/**
	 * 
	 */
	public String _name = null;

	/**
	 * 
	 */
	public long _header = 0l;

	/**
	 * @param filename
	 * @param type
	 * @param name
	 * @param header
	 */
	public FileProperty(String filename, String type, String name, long header) {
		super();
		_filename = filename;
		_type = type;
		_name = name;
		_header = header;
	}
}
