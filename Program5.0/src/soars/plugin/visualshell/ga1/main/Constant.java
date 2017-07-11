/*
 * Created on 2006/06/19
 */
package soars.plugin.visualshell.ga1.main;

import soars.common.soars.constant.CommonConstant;

/**
 * @author kurata
 */
public class Constant extends CommonConstant {

	/**
	 * 
	 */
	static public final String _resource_directory = "/soars/plugin/visualshell/ga1/resource";

	/**
	 * 
	 */
	static public final int _max_number_of_times = 10000;

	/**
	 * 
	 */
	static public final String _base_executor_jar_filename = "/soars/program/soars/bin/utility/grid/executor/executor.jar";

	/**
	 * 
	 */
	static public final String _decompression_name = "decompression";

	/**
	 * 
	 */
	static public final String _termination_name = "termination";

	/**
	 * 
	 */
	static public final String _simulation_shell_script_file_name = "script.sh";

	/**
	 * 
	 */
	static public final String _analysis_condition_script_file_name = "condition.txt";

	/**
	 * 
	 */
	static public final String _analysis_shell_script_file_name = "analysis.sh";

	/**
	 * 
	 */
	static public final String _compression_shell_script_file_name = "compression.sh";

	/**
	 * 
	 */
	static public final String _base_submit_shell_script_file_name = "/soars/program/submit.sh";

	/**
	 * 
	 */
	static public final String _base_delete_shell_script_file_name = "/soars/program/delete.sh";

	/**
	 * 
	 */
	static public final String _job_name = "soars";

	/**
	 * 
	 */
	static public final String _default_advanced_memory_setting = "false";

	/**
	 * 
	 */
	public static String[] _memory_sizes = {
		"64",
		"128",
		"256",
		"512",
		"1024",
		"2048",
		"4096",
		"8192",
		"16384",
		"32768",
		"65536",
		"131072",
		"262144",
		"524288",
		"600000",
		"700000",
		"800000",
		"900000",
		"1000000"
	};

	/**
	 * @param memory_size
	 * @return
	 */
	public static boolean contained(String memory_size) {
		for ( int i = 0; i < _memory_sizes.length; ++i) {
			if ( _memory_sizes[ i].equals( memory_size))
				return true;
		}
		return false;
	}
}
