/*
 * Created on 2006/06/19
 */
package soars.plugin.visualshell.tsubame2.main;

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
	static public final String _host_key = "Hostname";

	/**
	 * 
	 */
	static public final String _username_key = "Username";

	/**
	 * 
	 */
	static public final String _password_key = "Password";

	/**
	 * 
	 */
	static public final String _local_username_key = "Local.username";


	/**
	 * 
	 */
	public static final String _store_information = "Store.information";


	/**
	 * 
	 */
	static public final String _script_directory_key = "Script.directory";


	/**
	 * 
	 */
	static public final String _log_directory_key = "Log.directory";


	/**
	 * 
	 */
	static public final String _model_builder_memory_size_key = "Model.builder.memory.size";


	/**
	 * 
	 */
	static public final String _simulation_key = "Simulation";

	/**
	 * 
	 */
	static public final String _number_of_times_for_grid_key = "Number.of.times.for.experiment";


	/**
	 * 
	 */
	static public final String _log_analysis_key = "Log.analysis";

	/**
	 * 
	 */
	static public final String _log_analysis_condition_filename_key = "Log.analysis.condition.filename";


	/**
	 * 
	 */
	static public final String _log_transfer_key = "Log.transfer";

	/**
	 * 
	 */
	static public final String _local_log_directory_key = "Local.log.directory";



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
				+ "tsubame2" + File.separator
				+ "environment" + File.separator,
			"environment.properties",
			"SOARS Visual Shell Plugin TSUBAME2 properties");
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
