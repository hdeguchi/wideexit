/**
 * 
 */
package soars.common.utility.tool.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.MissingResourceException;
import java.util.Properties;

/**
 * @author kurata
 *
 */
public class ResourceManagerBase extends Properties {

	/**
	 * 
	 */
	public ResourceManagerBase() {
	}

	/**
	 * @param key
	 * @return
	 */
	public String get(String key) {
		try {
			return getProperty( key);
		} catch (NullPointerException e) {
			return "Key is null!";
		} catch (MissingResourceException e) {
			return "Unknown resource!";
		} catch (ClassCastException e) {
			return "Value is not String!";
		}
	}

	/**
	 * @param filePath
	 * @param defaultFilePath
	 * @return
	 */
	public boolean initialize(String filePath, String defaultFilePath) {
		File file = new File( filePath);
		try {
			Reader reader = new InputStreamReader( new FileInputStream( file.exists() ? file : new File( defaultFilePath)), "UTF-8");
			load( reader);
			reader.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/** ApplicationBuilderに対応する為の苦肉の策
	 * @param filePath
	 * @return
	 */
	protected static String get_filePath(String filePath) {
		File file = new File( filePath);
		return file.exists() ? filePath : filePath.substring( "../".length());
	}
}
