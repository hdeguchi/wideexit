/*
 * Created on 2006/06/19
 */
package soars.plugin.visualshell.ga1.main;

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
	static public final String _ga_dialog_rectangle_key = "GA.dialog.rectangle.";

	/**
	 * 
	 */
	static public final String _ga_local_frame_rectangle_key = "GA.local.frame.rectangle.";

	/**
	 * 
	 */
	static public final String _ga_grid_frame_rectangle_key = "GA.grid.frame.rectangle.";

	/**
	 * 
	 */
	static public final String _number_of_crossovers_key = "Number.of.crossovers";

	/**
	 * 
	 */
	static public final String _number_of_generation_alternations_key = "Number.of.generation.alternations";

	/**
	 * 
	 */
	static public final String _minimization_key = "Minimization";

	/**
	 * 
	 */
	static public final String _roulette_selection_key = "Roulette.selection";

	/**
	 * 
	 */
	static public final String _local_log_directory_key = "Local.log.directory";

	/**
	 * 
	 */
	static public final String _grid_key = "Grid";

	/**
	 * 
	 */
	static public final String _grid_portal_ip_address_key = "Grid.portal.ip.address";

	/**
	 * 
	 */
	static public final String _local_username_key = "Local.username";

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
	static public final String _advanced_memory_setting_key = "Advanced.memory.setting";

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
				+ "ga1" + File.separator
				+ "environment" + File.separator,
			"environment.properties",
			"SOARS Visual Shell Plugin GA1 properties");
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
