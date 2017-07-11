/*
 * Created on 2006/03/28
 */
package soars.common.soars.environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import soars.common.soars.constant.CommonConstant;
import soars.common.utility.tool.environment.EnvironmentBase;

/**
 * @author kurata
 */
public class SoarsCommonEnvironment extends EnvironmentBase {

	/**
	 * 
	 */
	static public final String _versionKey = "version";

	/**
	 * 
	 */
	static public final String _copyrightKey = "copyright";

	/**
	 * 
	 */
	static public final String _urlKey = "url";

	/**
	 * 
	 */
	static public final String _forumEnUrlKey = "forum.en.url";

	/**
	 * 
	 */
	static public final String _forumJaUrlKey = "forum.ja.url";

	/**
	 * 
	 */
	static public final String _configKey = "config.soars";

	/**
	 * 
	 */
	static public final String _tmpKey = "tmp.soars";

	/**
	 * 
	 */
	static public final String _initialMemorySizeKey = "initial.memory.size";

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private SoarsCommonEnvironment _soarsCommonEnvironment = null;

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
			if ( null == _soarsCommonEnvironment) {
				_soarsCommonEnvironment = new SoarsCommonEnvironment();
				if ( !_soarsCommonEnvironment.initialize())
					System.exit( 0);
			}
		}
	}

	/**
	 * @return
	 */
	public static SoarsCommonEnvironment get_instance() {
		if ( null == _soarsCommonEnvironment) {
			System.exit( 0);
		}

		return _soarsCommonEnvironment;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.tool.environment.EnvironmentBase#initialize()
	 */
	public boolean initialize() throws FileNotFoundException, IOException {
		File path = new File( _directory + _filename);
		if ( path.exists())
			load( new FileInputStream( path));

		return true;
	}

	/**
	 * 
	 */
	public SoarsCommonEnvironment() {
		super(
			System.getProperty( CommonConstant._soarsHome) + File.separator
				+ ".." + File.separator
				+ "resource" + File.separator
				+ "common" + File.separator,
			"soars.properties");
	}
}
