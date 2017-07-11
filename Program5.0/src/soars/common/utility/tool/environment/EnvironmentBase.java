/*
 * 2005/01/31
 */
package soars.common.utility.tool.environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author kurata
 */
public abstract class EnvironmentBase extends Properties {

	/**
	 * 
	 */
	protected String _directory;

	/**
	 * 
	 */
	protected String _filename;

	/**
	 * 
	 */
	protected String _header;

	/**
	 * @param directory
	 * @param filename
	 * @param header
	 * 
	 */
	public EnvironmentBase(String directory, String filename, String header) {
		super();
		_directory = directory;
		_filename = filename;
		_header = header;
	}

	/**
	 * @param directory
	 * @param filename
	 * 
	 */
	public EnvironmentBase(String directory, String filename) {
		super();
		_directory = directory;
		_filename = filename;
		_header = "";
	}

	/**
	 * @return
	 */
	public boolean initialize() throws FileNotFoundException, IOException {
		File directory = new File( _directory);
		if ( !directory.exists())
			directory.mkdirs();

		File path = get();
		if ( path.exists())
			load( new FileInputStream( path));

		return true;
	}

	/**
	 * @return
	 */
	public File get() {
		return new File( _directory + _filename);
	}

//	/**
//	 * @param key
//	 * @return
//	 */
//	public String get(String key) {
//		return getProperty( key);
//	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String get(String key, String defaultValue) {
		return getProperty( key, defaultValue);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		setProperty( key, value);
	}

	/**
	 * 
	 */
	public void store() {
		try {
			store_properties();
		} catch (FileNotFoundException e) {
			throw new RuntimeException( e);
		} catch (IOException e) {
			throw new RuntimeException( e);
		}
	}

	/**
	 * 
	 */
	public void store_properties() throws FileNotFoundException, IOException {
		File path = new File( _directory + _filename);
		store( new FileOutputStream( path), _header);
	}

	/**
	 * 
	 */
	public void debug() {
		System.out.println( _directory);
		System.out.println( _filename);
		System.out.println( _header);
	}
}
