/*
 * 2005/05/31
 */
package soars.application.visualshell.object.log;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.file.loader.SaxLoader;
import soars.application.visualshell.main.Constant;
import soars.common.utility.xml.sax.Writer;

/**
 * Manages the all log option managers for Visual Shell.
 * @author kurata / SOARS project
 */
public class LogManager extends HashMap<String, Map<String, LogOptionManager>> {

	/**
	 * Names of all object types.
	 */
	static public final String[] _kinds = new String[] {
		"keyword",
		"number object",
		"probability",
		"time variable",
		"role variable",
		"collection",
		"list",
		"map",
		"spot variable",
		//"class variable",
		//"file",
		"exchange algebra"
	};

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private LogManager _logManager = null;

	/**
	 * 
	 */
	static {
		startup();
	}

	/**
	 * 
	 */
	private static void startup() {
		synchronized( _lock) {
			if ( null == _logManager) {
				_logManager = new LogManager();
				if ( !_logManager.initialize())
					System.exit( 1);
			}
		}
	}

	/**
	 * Returns the instance of this object.
	 * @return the instance of this object
	 */
	public static LogManager get_instance() {
		if ( null == _logManager) {
			System.exit( 0);
		}

		return _logManager;
	}

	/**
	 * Creates this object.
	 */
	private LogManager() {
		super();
	}

	/**
	 * @return
	 */
	private boolean initialize() {
		String[] types = new String[] { "agent", "spot"};
		for ( String type:types) {
			Map<String, LogOptionManager> map = new HashMap<String, LogOptionManager>();
			for ( String kind:_kinds)
				map.put( kind, new LogOptionManager( type, kind));
			put( type, map);
		}

		get( "agent").get( "keyword").add( new LogOption( "$Name", false));
		get( "agent").get( "keyword").add( new LogOption( "$Role", false));
		get( "agent").get( "keyword").add( new LogOption( "$Spot", true));

		return true;
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
		clear();
		initialize();
	}

	/**
	 * Returns the script for ModelBuilder script.
	 * @param workDirectoryName the name of the directory for the log files
	 * @param toDisplay whether to display the logs
	 * @param toFile whether to make the log files
	 * @return the script for ModelBuilder script
	 */
	public String get_script_on_model_builder(String workDirectoryName, boolean toDisplay, boolean toFile) {
		if ( !toDisplay && !toFile)
			return "";

		String script = "";
		script += get_script_on_model_builder( get( "agent"), "logAgents", "logAgentsToFile", "agents/", workDirectoryName, toDisplay, toFile);
		script += get_script_on_model_builder( get( "spot"), "logSpots", "logSpotsToFile", "spots/", workDirectoryName, toDisplay, toFile);

		return script;
	}

	/**
	 * @param map
	 * @param word0
	 * @param word1
	 * @param word2
	 * @param workDirectoryName
	 * @param toDisplay
	 * @param toFile
	 * @return
	 */
	private String get_script_on_model_builder(Map<String, LogOptionManager> map, String word0, String word1, String word2, String workDirectoryName, boolean toDisplay, boolean toFile) {
		String[] unit = new String[] { "", ""};
		for ( String kind:_kinds) {
			LogOptionManager logOptionManager = map.get( kind);
			if ( null == logOptionManager)
				continue;

			logOptionManager.get_script( unit, word0);
		}

		if ( unit[ 0].equals( "") || unit[ 1].equals( ""))
			return "";

		String script = "";
		if ( toDisplay && !toFile) {
			script += "itemData" + Constant._lineSeparator;
			script += ( unit[ 0] + Constant._lineSeparator);
			script += ( unit[ 1] + Constant._lineSeparator);
		} else if ( !toDisplay && toFile) {
			script += "itemData" + Constant._lineSeparator;
			script += ( "logMkdirs\t" + word1 + "\t" + unit[ 0] + Constant._lineSeparator);
			script += ( get_work_directory_name( workDirectoryName) + word2 + "\t" + get_work_directory_name( workDirectoryName) + word2 + "\t" + unit[ 1] + Constant._lineSeparator);
		} else {
			script += "itemData" + Constant._lineSeparator;
			script += ( unit[ 0] + "\t" + "logMkdirs\t" + word1 + "\t" + unit[ 0] + Constant._lineSeparator);
			script += ( unit[ 1] + "\t" + get_work_directory_name( workDirectoryName) + word2 + "\t" + get_work_directory_name( workDirectoryName) + word2 + "\t" + unit[ 1] + Constant._lineSeparator);
		}

		return ( script + Constant._lineSeparator);
	}

	/**
	 * @param work_directory
	 * @return
	 */
	private String get_work_directory_name(String workDirectoryName) {
		return ( ( null == workDirectoryName) ? "" : ( workDirectoryName + "/"));
	}

	/**
	 * Returns the script for ModelBuilder script.
	 * @param work_directory the directory for the log files
	 * @return the script for ModelBuilder script
	 */
	public String get_script_on_animator(File work_directory) {
		boolean flag = get( "agent").get( "keyword").get_flag( "$Spot");
		get( "agent").get( "keyword").set_flag( "$Spot", true);

		String script = "";
		script += get_log_script_on_animator( get( "agent"), "logAgents", "logAgentsToFile", "agents/", work_directory);
		script += get_log_script_on_animator( get( "spot"), "logSpots", "logSpotsToFile", "spots/", work_directory);

		get( "agent").get( "keyword").set_flag( "$Spot", flag);

		return script;
	}

	/**
	 * @param map
	 * @param word0
	 * @param word1
	 * @param word2
	 * @param workDirectory
	 * @return
	 */
	private String get_log_script_on_animator(Map<String, LogOptionManager> map, String word0, String word1, String word2, File workDirectory) {
		String[] unit = new String[] { "", ""};
		for ( String kind:_kinds) {
			LogOptionManager logOptionManager = map.get( kind);
			if ( null == logOptionManager)
				continue;

			logOptionManager.get_script( unit, word0);
		}

		if ( unit[ 0].equals( "") || unit[ 1].equals( ""))
			return "";

		String script = "";
		script += "itemData" + Constant._lineSeparator;
		script += ( "logMkdirs\t" + word1 + "\t" + unit[ 0] + Constant._lineSeparator);
		script += ( get_work_directory( workDirectory) + word2 + "\t" + get_work_directory( workDirectory) + word2 + "\t" + unit[ 1] + Constant._lineSeparator);

		return ( script + Constant._lineSeparator);
	}

	/**
	 * @param work_directory
	 * @return
	 */
	private String get_work_directory(File work_directory) {
		return ( ( null == work_directory) ? "" : ( work_directory.getAbsolutePath() + "/"));
	}

	/**
	 * Returns the script for ModelBuilder script.
	 * @param subDirectory the name of directory for the script file on Grid
	 * @param logDirectory the name of directory for the log files on Grid
	 * @return the script for ModelBuilder script
	 */
	public String get_script_on_grid(String subDirectory, String logDirectory) {
		String script = "";
		script += get_log_script_on_grid( get( "agent"), "logAgents", "logAgentsToFile", "agents/", subDirectory, logDirectory);
		script += get_log_script_on_grid( get( "spot"), "logSpots", "logSpotsToFile", "spots/", subDirectory, logDirectory);
		return script;
	}

	/**
	 * @param map
	 * @param word0
	 * @param word1
	 * @param word2
	 * @param subDirectory
	 * @param logDirectory
	 * @return
	 */
	private String get_log_script_on_grid(Map<String, LogOptionManager> map, String word0, String word1, String word2, String subDirectory, String logDirectory) {
		String[] unit = new String[] { "", ""};
		for ( String kind:_kinds) {
			LogOptionManager logOptionManager = map.get( kind);
			if ( null == logOptionManager)
				continue;

			logOptionManager.get_script( unit, word0);
		}

		if ( unit[ 0].equals( "") || unit[ 1].equals( ""))
			return "";

		String script = "";
		script += "itemData" + Constant._lineSeparator;
		script += ( "logMkdirs\t" + word1 + "\t" + unit[ 0] + Constant._lineSeparator);
		script += ( logDirectory + "/" + subDirectory + "/" + word2 + "\t" + logDirectory + "/" + subDirectory + "/" + word2 + "\t" + unit[ 1] + Constant._lineSeparator);

		return ( script + Constant._lineSeparator);
	}

	/**
	 * Appends the specified LogOption to the appropriate LogManager.
	 * @param logOption the specified LogOption
	 * @param tag the tag
	 */
	public void append(LogOption logOption, String tag) {
		String[] kinds = SaxLoader._tagKindMap.get( tag);
		if ( null == kinds)
			return;

		LogOptionManager logOptionManager = get( kinds[ 0]).get( kinds[ 1]);
		if ( null == logOptionManager)
			return;

		logOptionManager.append( logOption);
	}

	/**
	 * @param type
	 * @param kind
	 */
	public void update(String type, String kind) {
		LogOptionManager logOptionManager = get( type).get( kind);
		if ( null == logOptionManager)
			return;

		logOptionManager.update();
	}

	/**
	 * Updates all log managers.
	 */
	public void update_all() {
		Collection<Map<String, LogOptionManager>> maps = values();
		for ( Map<String, LogOptionManager> map:maps) {
			Collection<LogOptionManager> logOptionManagers = map.values();
			for ( LogOptionManager logOptionManager:logOptionManagers)
				logOptionManager.update();
		}
	}

	/**
	 * @param type
	 * @param kind
	 * @param name
	 * @param newName
	 */
	public void update(String type, String kind, String name, String newName) {
		LogOptionManager logOptionManager = get( type).get( kind);
		if ( null == logOptionManager)
			return;

		logOptionManager.update( name, newName);
	}

	/**
	 * Returns true for writing this object data successfully.
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing this object data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(Writer writer) throws SAXException {
		writer.startElement( null, null, "log_data", new AttributesImpl());

		Collection<Map<String, LogOptionManager>> maps = values();
		for ( Map<String, LogOptionManager> map:maps) {
			for ( String kind:_kinds) {
				LogOptionManager logOptionManager = map.get( kind);
				if ( null == logOptionManager)
					continue;

				logOptionManager.write( writer);
			}
		}

		writer.endElement( null, null, "log_data");

		return true;
	}
}
