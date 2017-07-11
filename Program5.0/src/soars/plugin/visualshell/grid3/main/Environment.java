/*
 * Created on 2006/06/19
 */
package soars.plugin.visualshell.grid3.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import soars.common.utility.tool.environment.EnvironmentBase;

/**
 * @author kurata
 */
public class Environment extends EnvironmentBase {


	/**
	 * 
	 */
	static public final String _gridPortalIpAddressKey = "Grid.portal.ip.address";


	/**
	 * 
	 */
	static public final String _localUsernameKey = "Local.username";


	/**
	 * 
	 */
	static public final String _scriptDirectoryKey = "Script.directory";


	/**
	 * 
	 */
	static public final String _logDirectoryKey = "Log.directory";


	/**
	 * 
	 */
	static public final String _modelBuilderMemorySizeKey = "Model.builder.memory.size";


	/**
	 * 
	 */
	static public final String _simulationKey = "Simulation";

	/**
	 * 
	 */
	static public final String _numberOfTimesForGridKey = "Number.of.times.for.experiment";


	/**
	 * 
	 */
	static public final String _logAnalysisKey = "Log.analysis";

	/**
	 * 
	 */
	static public final String _logAnalysisConditionFilenameKey = "Log.analysis.condition.filename";


	/**
	 * 
	 */
	static public final String _logTransferKey = "Log.transfer";

	/**
	 * 
	 */
	static public final String _localLogDirectoryKey = "Local.log.directory";



	/**
	 * 
	 */
	static private Object _lock = new Object();


	/**
	 * 
	 */
	static private Environment _environment = null;


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
			if ( null == _environment) {
				_environment = new Environment();
				if ( !_environment.initialize())
					System.exit( 0);
			}
		}
	}

	/**
	 * @return
	 */
	public static Environment get_instance() {
		if ( null == _environment) {
			System.exit( 0);
		}

		return _environment;
	}

	/**
	 * 
	 */
	public Environment() {
		super(
			System.getProperty( Constant._soarsProperty) + File.separator
				+ "function" + File.separator
				+ "grid3" + File.separator
				+ "environment" + File.separator,
			"environment.properties",
			"SOARS Visual Shell Plugin Grid3 properties");
	}

	/**
	 * @return
	 */
	public Environment get_clone() {
		Environment environment = new Environment();
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			environment.put( entry.getKey(), entry.getValue());
		}
		return environment;
	}
}
