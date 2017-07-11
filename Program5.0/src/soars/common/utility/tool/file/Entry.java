/**
 * 
 */
package soars.common.utility.tool.file;

import java.io.File;

/**
 * @author kurata
 *
 */
public class Entry {

	/**
	 *  
	 */
	public String _path = "";

	/**
	 *  
	 */
	public File _file = null;

	/**
	 * @param path 
	 * @param file 
	 */
	public Entry(String path, File file) {
		super();
		_path = path;
		_file = file;
	}
}
