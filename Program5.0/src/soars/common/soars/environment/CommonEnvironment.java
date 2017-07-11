/*
 * Created on 2006/03/28
 */
package soars.common.soars.environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import soars.common.soars.constant.CommonConstant;
import soars.common.utility.tool.environment.EnvironmentBase;

/**
 * @author kurata
 */
public class CommonEnvironment extends EnvironmentBase {

	/**
	 * 
	 */
	static public final String _localeKey = "Locale";

	/**
	 * 
	 */
	static private final String _32bitMemorySizeKey = "32bit.memory.size";

	/**
	 * 
	 */
	static private final String _64bitMemorySizeKey = "64bit.memory.size";

	/**
	 * 
	 */
	static public final String _advancedMemorySettingKey = "Advanced.memory.setting";

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private CommonEnvironment _commonEnvironment = null;

	/**
	 * 
	 */
	static {
		try {
			startup();
		} catch (FileNotFoundException e) {
			throw new RuntimeException( e);
		} catch (IOException e) {
			throw new RuntimeException( e);
		}
	}

	/**
	 * 	
	 */
	public static void startup() throws FileNotFoundException, IOException {
		synchronized( _lock) {
			if ( null == _commonEnvironment) {
				_commonEnvironment = new CommonEnvironment();
				if ( !_commonEnvironment.initialize())
					System.exit( 0);
			}
		}
	}

	/**
	 * @return
	 */
	public static CommonEnvironment get_instance() {
		if ( null == _commonEnvironment) {
			System.exit( 0);
		}

		return _commonEnvironment;
	}

	/**
	 * 
	 */
	public CommonEnvironment() {
		super(
			System.getProperty( CommonConstant._soarsProperty) + File.separator
				+ "common" + File.separator
				+ "environment" + File.separator,
			"environment.properties",
			"SOARS common properties");
	}

	/**
	 * 
	 */
	public void update() {
		_directory = System.getProperty( CommonConstant._soarsProperty) + File.separator
			+ "common" + File.separator
			+ "environment" + File.separator;
		File directory = new File( _directory);
		if ( !directory.exists())
			directory.mkdirs();
	}

	/**
	 * @return
	 */
	public String get_memory_size() {
		String defaultMemorySize = System.getProperty( CommonConstant._soarsMemorySize, CommonConstant._defaultMemorySize);
		return get( get_memory_size_key(), defaultMemorySize);
	}

	/**
	 * @param memorySize
	 */
	public void set_memory_size(String memorySize) {
		set( get_memory_size_key(), memorySize);
	}

	/**
	 * @return
	 */
	private String get_memory_size_key() {
		String value = System.getProperty( "sun.arch.data.model", "32");
		return ( value.equals( "64") ? _64bitMemorySizeKey : _32bitMemorySizeKey);
	}
}
