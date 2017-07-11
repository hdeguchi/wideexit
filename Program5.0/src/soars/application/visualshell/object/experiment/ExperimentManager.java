/*
 * 2005/07/12
 */
package soars.application.visualshell.object.experiment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JProgressBar;
import javax.swing.JTextField;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.common.file.ScriptFile;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.file.exporter.experiment.ExperimentTableExporter;
import soars.application.visualshell.file.exporter.script.Exporter;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.simulation.SimulationManager;
import soars.common.utility.tool.sort.QuickSort;
import soars.common.utility.tool.sort.StringNumberComparator;
import soars.common.utility.tool.ssh.SshTool;
import soars.common.utility.xml.sax.Writer;

import com.sshtools.j2ssh.SftpClient;

/**
 * The experiment support manager.
 * @author kurata / SOARS project
 */
public class ExperimentManager extends TreeMap<String, InitialValueMap> {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private ExperimentManager _experimentManager = null;

	/**
	 * Column width of the check box which indicates whether to export the ModelBuilder script.
	 */
	public static int _defaultExperimentTableCheckBoxColumnWidth = 50;

	/**
	 * Column width.
	 */
	public static int _defaultExperimentTableColumnWidth = 150;

	/**
	 * Array of delimiters to detect the alias.
	 */
	public static String[] _suffixes = new String[]{ " ", "\t", "=", ">", "\""};

	/**
	 * Array of column width.
	 */
	public Vector<Integer> _columnWidths = new Vector<Integer>();

	/**
	 * Times of experiments.
	 */
	public int _numberOfTimes = 1;

	/**
	 * Flag for parallel execution.
	 */
	public boolean _parallel = false;

	/**
	 * Hashtable for comments of experiment.
	 */
	public TreeMap<String, String> _commentMap = new TreeMap<String, String>();

	/**
	 * 
	 */
	static private final String[] _kinds = new String[] {
		"probability",
		"collection",
		"list",
		"map",
		"keyword",
		"number object",
		"time variable",
		"file",
		"exchange algebra",
		"extransfer",
		"role",
		"simulation"
	};

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
			if ( null == _experimentManager) {
				_experimentManager = new ExperimentManager();
			}
		}
	}

	/**
	 * Returns the instance of this object.
	 * @return the instance of this object
	 */
	public static ExperimentManager get_instance() {
		if ( null == _experimentManager) {
			System.exit( 0);
		}

		return _experimentManager;
	}

	/**
	 * Creates this object.
	 */
	public ExperimentManager() {
		super();
	}

	/**
	 * Creates this object with the specified data.
	 * @param experimentManager the specified data
	 */
	public ExperimentManager(ExperimentManager experimentManager) {
		super();
		copy( experimentManager);
	}

	/**
	 * Copy the specified data to this object.
	 * @param experimentManager the specified data
	 */
	public void copy(ExperimentManager experimentManager) {
		cleanup();

		Iterator iterator = experimentManager.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String experiment_name = ( String)entry.getKey();
			InitialValueMap initialValueMap = new InitialValueMap( ( InitialValueMap)entry.getValue());
			put( experiment_name, initialValueMap);
		}

		for ( Integer integer:experimentManager._columnWidths)
			_columnWidths.add( new Integer( integer.intValue()));

		_numberOfTimes = experimentManager._numberOfTimes;

		_parallel = experimentManager._parallel;

		_commentMap.putAll( experimentManager._commentMap);
	}

	/**
	 * @param experimentManager
	 * @return
	 */
	public boolean sama_as(ExperimentManager experimentManager) {
		// TODO Auto-generated method stub
		if ( size() !=experimentManager.size())
			return false;

		Iterator iterator = experimentManager.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String experimentName = ( String)entry.getKey();
			if ( null == get( experimentName) || null == entry.getValue())
				return false;

			if ( !( ( InitialValueMap)get( experimentName)).same_as( ( InitialValueMap)entry.getValue()))
				return false;
		}

//		if ( _columnWidths.size() != experimentManager._columnWidths.size())
//			return false;

//		for ( int i = 0; i < _columnWidths.size(); ++i) {
//			if ( _columnWidths.get( i).intValue() != experimentManager._columnWidths.get( i).intValue())
//				return false;
//		}

		if ( _numberOfTimes != experimentManager._numberOfTimes)
			return false;

		if ( _parallel != experimentManager._parallel)
			return false;

		iterator = experimentManager._commentMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String alias = ( String)entry.getKey();
			if ( null == _commentMap.get( alias) || null == entry.getValue())
				return false;

			if ( !( ( String)_commentMap.get( alias)).equals( ( String)entry.getValue()))
				return false;
		}

		return true;
	}

	/**
	 * @return
	 */
	public boolean initial_value_exists() {
		// TODO Auto-generated method stub
		String[] initialValues = SimulationManager.get_instance().get_initial_values();
		if ( null != initialValues) {
			for ( String initialValue:initialValues) {
				if ( !initialValue.equals( "") && initialValue.matches( "\\$.+"))
					return true;
			}
		}

		initialValues = LayerManager.get_instance().get_role_initial_values();
		if ( null != initialValues) {
			for ( String initialValue:initialValues) {
				if ( !initialValue.equals( "") && initialValue.matches( "\\$.+"))
					return true;
			}
		}

		String[] kinds = new String[] {
			"probability",
			"collection",
			"list",
			"map",
			"keyword",
			"number object",
			"time variable",
			"file",
			"exchange_algebra",
			"extransfer"
		};
		for ( String kind:kinds) {
			initialValues = LayerManager.get_instance().get_object_initial_values( kind);
			if ( null != initialValues) {
				for ( String initialValue:initialValues) {
					if ( !initialValue.equals( "") && initialValue.matches( "\\$.+"))
						return true;
				}
			}
		}

		return false;
	}

	/**
	 * Extract the specified data to this object.
	 * @return the specified data object
	 */
	public ExperimentManager extract_for_genetic_algorithm() {
		ExperimentManager experimentManager = new ExperimentManager();

		int index = 1;
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
//			String experiment_name = ( String)entry.getKey();
			InitialValueMap initialValueMap = ( InitialValueMap)entry.getValue();
			if ( !initialValueMap._export)
				continue;

			experimentManager.put( "name" + String.valueOf( index++), new InitialValueMap( initialValueMap));
		}

//		for ( int i = 0; i < experimentManager._column_widths.size(); ++i) {
//			Integer integer = ( Integer)experimentManager._column_widths.get( i);
//			_column_widths.add( new Integer( integer.intValue()));
//		}

//		experimentManager._number_of_times = _number_of_times;
//
//		experimentManager._parallel = _parallel;
//
//		experimentManager._commentMap.putAll( _commentMap);

		return experimentManager;
	}

	/**
	 * @param population 
	 * @return 
	 */
	public boolean update_for_genetic_algorithm(List<List<String>> population) {
		for ( int i = 0; i < population.size(); ++i) {
			String name = ( "name" + String.valueOf( i + 1));
			InitialValueMap initialValueMap = ( InitialValueMap)get( name);
			if ( null != initialValueMap) {
				if ( !initialValueMap.update( population.get( i)))
					return false;
			} else {
				String[] aliases = get_aliases();
				if ( null == aliases || aliases.length < population.get( i).size())
					return false;

				initialValueMap = new InitialValueMap( true);
				int index = 0;
				for ( int j = 0; j < aliases.length; ++j)
					initialValueMap.put( aliases[ j], aliases[ j].startsWith( "$__val") ? population.get( i).get( index++) : "");

				put( name, initialValueMap);
			}
		}
		return true;
	}

	/**
	 * @param population 
	 * @return 
	 */
	public ExperimentManager get_for_genetic_algorithm(List<List<String>> population) {
		ExperimentManager experimentManager = new ExperimentManager( this);

		int index = 0;
		Iterator iterator = experimentManager.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String experiment_name = ( String)entry.getKey();
			InitialValueMap initialValueMap = ( InitialValueMap)entry.getValue();
			if ( !initialValueMap._export)
				continue;

			if ( !initialValueMap.update( population.get( index++)))
				return null;
		}
		return experimentManager;
	}

	/**
	 * @param file 
	 * @return 
	 */
	public boolean export_table(File file) {
		String[] aliases = get_aliases();
		if ( null == aliases)
			return false;

		String[] comments = get_comments();
		if ( null == comments)
			return false;

		String[][] tableData = get_table_data( aliases.length);
		if ( null == tableData || 0 == tableData.length)
			return false;

		if ( !ExperimentTableExporter.execute( file, aliases, comments, tableData))
			return false;

		return true;
	}

	/**
	 * @return
	 */
	private String[] get_comments() {
		List<String> comments = new ArrayList<String>();
		comments.add( "");
		comments.add( "");
		comments.add( "");
		Iterator iterator = _commentMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			comments.add( ( String)entry.getValue());
		}
		return comments.toArray( new String[ 0]);
	}

	/**
	 * @param length
	 * @return
	 */
	private String[][] get_table_data(int length) {
		String[][] tableData = new String[ size()][];
		int index = 0;
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			InitialValueMap initialValueMap = ( InitialValueMap)entry.getValue();
			tableData[ index++] = initialValueMap.get_data_for_table( ( String)entry.getKey());
		}
		return tableData;
	}

	/**
	 * @return
	 */
	public int get_export_count() {
		int counter = 0;
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			InitialValueMap initialValueMap = ( InitialValueMap)entry.getValue();
			if ( !initialValueMap._export)
				continue;

			++counter;
		}
		return counter;
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			InitialValueMap initialValueMap = ( InitialValueMap)entry.getValue();
			initialValueMap.cleanup();
		}
		clear();

		_columnWidths.clear();

		_numberOfTimes = 1;

		_parallel = false;

		_commentMap.clear();
	}

	/**
	 * Returns true for exporting the ModelBuilder scripts.
	 * @return true for exporting the ModelBuilder scripts
	 */
	public boolean can_export() {
		if ( !isEmpty() && 0 < get_initial_value_count()) {
			Iterator iterator = entrySet().iterator();
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				InitialValueMap initialValueMap = ( InitialValueMap)entry.getValue();
				if ( initialValueMap._export)
					return true;
			}
			return false;
		}
		return true;
	}

	/**
	 * Returns true for exporting a ModelBuilder script to clipboard.
	 * @return true for exporting a ModelBuilder script to clipboard
	 */
	public boolean can_export_to_clipboard() {
		if ( !isEmpty() && 0 < get_initial_value_count()) {
			int counter = 0;
			Iterator iterator = entrySet().iterator();
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				InitialValueMap initialValueMap = ( InitialValueMap)entry.getValue();
				if ( initialValueMap._export)
					++counter;
			}
			return ( 1 == counter);
		}
		return true;
	}

	/**
	 * Returns the number of aliases.
	 * @return the number of aliases
	 */
	public int get_initial_value_count() {
		// TODO
		int counter = 0;

		String[] initialValues = SimulationManager.get_instance().get_initial_values();
		if ( null != initialValues) {
			for ( String initialValue:initialValues) {
				if ( !initialValue.equals( "") && initialValue.matches( "\\$.+"))
					++counter;
			}
		}

		initialValues = LayerManager.get_instance().get_role_initial_values();
		if ( null != initialValues) {
			for ( String initialValue:initialValues) {
				if ( !initialValue.equals( "") && initialValue.matches( "\\$.+"))
					++counter;
			}
		}

		String[] kinds = new String[] {
			"probability",
			"collection",
			"list",
			"map",
			"keyword",
			"number object",
			"time variable",
			"file",
			"exchange_algebra",
			"extransfer"
		};
		for ( String kind:kinds) {
			initialValues = LayerManager.get_instance().get_object_initial_values( kind);
			if ( null != initialValues) {
				for ( String initialValue:initialValues) {
					if ( !initialValue.equals( "") && initialValue.matches( "\\$.+"))
						++counter;
				}
			}
		}

		return counter;
	}

	/**
	 * @return 
	 */
	public int get_initial_value_count_for_genetic_algorithm() {
		String[] aliases = get_aliases();
		if ( null == aliases)
			return 0;

		int counter = 0;
		for ( int i = 0; i < aliases.length; ++i) {
			if ( !aliases[ i].startsWith( "$__val"))
				continue;

			++counter;
		}
		return counter;
	}

	/**
	 * Returns the number of columns for the table.
	 * @return the number of columns for the table
	 */
	public int get_column_count() {
		return ( get_initial_value_count() + 2);
	}

	/**
	 * Returns the array of the aliases.
	 * @return the array of the aliases
	 */
	public String[] get_aliases() {
		// TODO
		List<String> initialValueList = new ArrayList<>();

		String[] initialValues = SimulationManager.get_instance().get_initial_values();
		if ( null != initialValues) {
			for ( String initialValue:initialValues) {
				if ( !initialValue.equals( "") && initialValue.matches( "\\$.+"))
					initialValueList.add( initialValue);
			}
		}

		initialValues = LayerManager.get_instance().get_role_initial_values();
		if ( null != initialValues) {
			for ( String initialValue:initialValues) {
				if ( !initialValue.equals( "") && initialValue.matches( "\\$.+"))
					initialValueList.add( initialValue);
			}
		}

		String[] kinds = new String[] {
			"probability",
			"collection",
			"list",
			"map",
			"keyword",
			"number object",
			"time variable",
			"file",
			"exchange_algebra",
			"extransfer"
		};
		for ( String kind:kinds) {
			initialValues = LayerManager.get_instance().get_object_initial_values( kind);
			if ( null != initialValues) {
				for ( String initialValue:initialValues) {
					if ( !initialValue.equals( "") && initialValue.matches( "\\$.+"))
						initialValueList.add( initialValue);
				}
			}
		}

		if ( initialValueList.isEmpty())
			return null;

		Collections.sort( initialValueList);

		return ( String[])initialValueList.toArray( new String[ initialValueList.size()]);
//		InitialValueMap initialValueMap = get_top_initialValueMap();
//		if ( null == initialValueMap)
//			return null;
//
//		return initialValueMap.get_aliases();
	}

	/**
	 * @return
	 */
	private InitialValueMap get_top_initialValueMap() {
		if ( isEmpty())
			return null;

		Iterator iterator = entrySet().iterator();
		if ( !iterator.hasNext())
			return null;

		Object object = iterator.next();
		Map.Entry entry = ( Map.Entry)object;
		return ( InitialValueMap)entry.getValue();
	}

	/**
	 * Invoked when the probability variable's initial value is changed.
	 */
	public void on_update_object(String kind) {
		String[] initialValues = LayerManager.get_instance().get_object_initial_values( kind);
		if ( null != initialValues && 0 != initialValues.length)
			update( initialValues);

		remove_not_used_aliases( kind,
			( null == initialValues) ? null : new Vector( Arrays.asList( initialValues)));

		if ( isEmpty())
			_parallel = false;
	}

	/**
	 * Invoked when the role's initial value is changed.
	 */
	public void on_update_role() {
		String[] initialValues = LayerManager.get_instance().get_role_initial_values();
		if ( null != initialValues && 0 != initialValues.length)
			update( initialValues);

		remove_not_used_aliases( "role",
			( null == initialValues) ? null : new Vector( Arrays.asList( initialValues)));

		if ( isEmpty())
			_parallel = false;
	}

	/**
	 * Invoked when the simulation condition's initial value is changed.
	 */
	public void on_update_simulation() {
		String[] initialValues = SimulationManager.get_instance().get_initial_values();
		if ( null != initialValues && 0 != initialValues.length)
			update( initialValues);

		remove_not_used_aliases( "simulation",
			( null == initialValues) ? null : new Vector( Arrays.asList( initialValues)));

		if ( isEmpty())
			_parallel = false;
	}

	/**
	 * @param initialValues
	 */
	private void update(String[] initialValues) {
		for ( int i = 0; i < initialValues.length; ++i) {
			if ( initialValues[ i].matches( "\\$.+"))
				append( initialValues[ i]);
		}
	}

	/**
	 * @param alias
	 */
	private void append(String alias) {
		//TODO
//		if ( isEmpty())
//			put( "name1", new InitialValueMap());

		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			InitialValueMap initialValueMap = ( InitialValueMap)entry.getValue();
			if ( !initialValueMap.containsKey( alias))
				initialValueMap.put( alias, "");
		}

		update_column_widths( alias);

		if ( !_commentMap.containsKey( alias))
			_commentMap.put( alias, "");
	}

	/**
	 * @param alias
	 */
	private void update_column_widths(String alias) {
		int index = get_index( alias);
		if ( 3 > index)
			return;

		_columnWidths.insertElementAt( new Integer( _defaultExperimentTableColumnWidth), index);
	}

	/**
	 * @param kind
	 * @param initialValues
	 */
	private void remove_not_used_aliases(String kind, Vector<String> initialValues) {
		InitialValueMap initialValueMap = get_top_initialValueMap();
		if ( null == initialValueMap)
			return;

		Vector<String> aliasesToBeRemoved = new Vector<String>();

		Iterator iterator = initialValueMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String alias = ( String)entry.getKey();
			if ( used( kind, alias))
				continue;

			if ( null == initialValues) {
				aliasesToBeRemoved.add( alias);
				continue;
			}

			if ( !initialValues.contains( alias))
				aliasesToBeRemoved.add( alias);
		}

		remove_aliases( aliasesToBeRemoved);
	}

	/**
	 * @param kind
	 * @param alias
	 * @return
	 */
	private boolean used(String kind, String alias) {
		for ( int i = 0; i < _kinds.length; ++i) {
			if ( _kinds[ i].equals( kind))
				continue;

			if ( _kinds[ i].equals( "role")) {
				if ( LayerManager.get_instance().role_contains_this_alias( alias))
					return true;
			} else if ( _kinds[ i].equals( "simulation")) {
				if ( SimulationManager.get_instance().contains_this_alias( alias))
					return true;
			} else {
				if ( LayerManager.get_instance().object_contains_this_alias( _kinds[ i], alias))
					return true;
			}
		}
		return false;
	}

	/**
	 * @param aliasesToBeRemoved
	 */
	private void remove_aliases(Vector<String> aliasesToBeRemoved) {
		for ( int i = 0; i < aliasesToBeRemoved.size(); ++i) {
			int index = get_index( ( String)aliasesToBeRemoved.get( i));
			if ( 1 < index)
				_columnWidths.removeElementAt( index);

			Iterator iterator = entrySet().iterator();
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				InitialValueMap initialValueMap = ( InitialValueMap)entry.getValue();
				initialValueMap.remove( ( String)aliasesToBeRemoved.get( i));
			}

			_commentMap.remove( ( String)aliasesToBeRemoved.get( i));
		}
	}

	/**
	 * @param alias
	 * @return
	 */
	private int get_index(String alias) {
		if ( _columnWidths.isEmpty())
			return -1;

		InitialValueMap initialValueMap = get_top_initialValueMap();
		if ( null == initialValueMap)
			return -1;

		Vector<String> initialValues = new Vector<String>( initialValueMap.keySet());
		return ( initialValues.indexOf( alias) + 3);
	}

	/**
	 * Returns true for exporting the ModelBuilder script to the specified file in CSV format successfully.
	 * @param file the specified file
	 * @param toDisplay whether to display the logs
	 * @param toFile whether to make the log files
	 * @return true for exporting the ModelBuilder script to the specified file in CSV format successfully
	 */
	public boolean export(File file, boolean toDisplay, boolean toFile) {
		String[] words = file.getName().split( "\\.");
		if ( 1 > words.length)
			return false;

		String filename = "";
		String extension = "";
		if ( 1 == words.length)
			filename = ( words[ 0] + '.');
		else {
			for ( int i = 0; i < words.length - 1; ++i)
				filename = ( words[ i] + '.');

			extension = ( '.' + words[ words.length - 1]);
		}

		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String experimentName = ( String)entry.getKey();
			InitialValueMap initialValueMap = ( InitialValueMap)entry.getValue();
			if ( !initialValueMap._export)
				continue;

			File newFile = new File( file.getParent() + File.separator + filename + experimentName + extension);
			if ( !Exporter.execute_on_model_builder( newFile, initialValueMap, null, experimentName, toDisplay, toFile, false))
				return false;
		}
		return true;
	}

	/**
	 * Exports the ModelBuilder scripts to the specified files in CSV format, and returns them.
	 * @param toDisplay whether to display the logs
	 * @param toFile whether to make the log files
	 * @return the ModelBuilder scripts 
	 */
	public ScriptFile[] export(/*boolean animator, */boolean toDisplay, boolean toFile) {
//		if ( !animator) {
			Vector<ScriptFile> scriptFiles = new Vector<ScriptFile>();
			for ( int i = 0; i < _numberOfTimes; ++i) {
				Iterator iterator = entrySet().iterator();
				while ( iterator.hasNext()) {
					Object object = iterator.next();
					Map.Entry entry = ( Map.Entry)object;
					String experimentName = ( String)entry.getKey();
					InitialValueMap initialValueMap = ( InitialValueMap)entry.getValue();
					if ( !initialValueMap._export)
						continue;

					String workDirectoryName = CommonTool.get_work_directory_name(
						( null == LayerManager.get_instance().get_current_file())
							? ( System.getProperty( Constant._soarsHome) + "/../log/" + experimentName)
							: ( LayerManager.get_instance().get_current_file().getParent() + "/" + experimentName),
						i);
					if ( null == workDirectoryName)
						return null;

					File file = null;
					try {
						file = File.createTempFile( "soars_", ".sor");
					} catch (IOException e) {
						//e.printStackTrace();
						return null;
					}

					String name = experimentName + ( ( 2 > _numberOfTimes) ? "" : ( "(" + String.valueOf( i + 1) + ")"));
					if ( !Exporter.execute_on_model_builder( file, initialValueMap, workDirectoryName, name, toDisplay, toFile, false)) {
						file.delete();
						break;
					}

					file.deleteOnExit();

					scriptFiles.add( new ScriptFile( name, file, workDirectoryName));
				}
			}
			return ( scriptFiles.isEmpty() ? null : ( ScriptFile[])scriptFiles.toArray( new ScriptFile[ 0]));
//		} else {
//			Vector<ScriptFile> workDirectories = new Vector<ScriptFile>();
//			Iterator iterator = entrySet().iterator();
//			while ( iterator.hasNext()) {
//				Object object = iterator.next();
//				Map.Entry entry = ( Map.Entry)object;
//				String experimentName = ( String)entry.getKey();
//				InitialValueMap initialValueMap = ( InitialValueMap)entry.getValue();
//				if ( !initialValueMap._export)
//					continue;
//
//				File workDirectory = CommonTool.make_work_directory();
//				File scriptFile = new File( workDirectory.getAbsolutePath() + "/" + Constant._soarsScriptFilename);
//				if ( !Exporter.execute_on_animator( scriptFile, initialValueMap, workDirectory, experimentName)) {
//					FileUtility.delete( workDirectory, true);
//					break;
//				}
//
//				workDirectories.add( new ScriptFile( workDirectory));
//			}
//
//			return ( workDirectories.isEmpty() ? null : workDirectories.toArray( new ScriptFile[ 0]));
//		}
	}

	/**
	 * For Genetic Algorithm on Local, exports the ModelBuilder scripts to the specified files in CSV format, and returns them.
	 * @param localLogDirectory
	 * @param numberOfScriptsTextField
	 * @param progressBar
	 * @return the ModelBuilder scripts 
	 */
	public List<ScriptFile> export(File localLogDirectory, JTextField numberOfScriptsTextField, JProgressBar progressBar) {
		progressBar.setMinimum( 0);
		progressBar.setMaximum( size());
		progressBar.setValue( 0);
		progressBar.update( progressBar.getGraphics());

		List<ScriptFile> scriptFiles = new ArrayList<ScriptFile>();
		int index = 0;
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String experimentName = ( String)entry.getKey();
			InitialValueMap initialValueMap = ( InitialValueMap)entry.getValue();
			if ( !initialValueMap._export)
				continue;

			File file = null;
			try {
				file = File.createTempFile( "soars_", ".sor");
			} catch (IOException e) {
				//e.printStackTrace();
				return null;
			}

			String localLogDirectoryName = ( localLogDirectory.getAbsolutePath() + "/" + experimentName +"/1");
			if ( !Exporter.execute_on_model_builder( file, initialValueMap, localLogDirectoryName, experimentName, false, true, true)) {
				file.delete();
				break;
			}

			file.deleteOnExit();

			scriptFiles.add( new ScriptFile( experimentName, file, localLogDirectoryName));

			numberOfScriptsTextField.setText( String.valueOf( scriptFiles.size()));
			numberOfScriptsTextField.update( numberOfScriptsTextField.getGraphics());

			progressBar.setValue( ++index);
			progressBar.update( progressBar.getGraphics());
		}
		return scriptFiles;
	}

	/**
	 * Exports the ModelBuilder script to the clipboard in CSV format.
	 * @param toDisplay whether to display the logs
	 * @param toFile whether to make the log files
	 */
	public void export_to_clipboard(boolean toDisplay, boolean toFile) {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String experimentName = ( String)entry.getKey();
			InitialValueMap initialValueMap = ( InitialValueMap)entry.getValue();
			if ( !initialValueMap._export)
				continue;

			Exporter.execute( initialValueMap, experimentName, toDisplay, toFile);
			break;
		}
	}

	/**
	 * Exports the ModelBuilder script to the specified file in CSV format for Grid.
	 * @param script_directory the name of directory for the script file on Grid
	 * @param log_directory the name of directory for the log files on Grid
	 * @param programDirectory the name of directory for the program file on Grid
	 * @param numberOfTimes the number of the experiment times
	 */
	public void export(String scriptDirectoryName, String logDirectoryName, String programDirectory, int numberOfTimes) {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String experimentName = ( String)entry.getKey();
			InitialValueMap initialValueMap = ( InitialValueMap)entry.getValue();
			if ( !initialValueMap._export)
				continue;

			for ( int i = 1; i <= numberOfTimes; ++i) {
				File scriptDirectory = new File( scriptDirectoryName + "/" + experimentName + "/" + i);
				//if ( scriptDirectory.exists())
				//	Tool.delete( scriptDirectory);

				scriptDirectory.mkdirs();
				if ( !scriptDirectory.exists())
					return;

				File scriptFile = new File( scriptDirectoryName + "/" + experimentName + "/" + i + "/" + Constant._soarsScriptFilename);
				Exporter.execute_on_grid( scriptFile, initialValueMap, experimentName + "/" + i, scriptDirectoryName, logDirectoryName, programDirectory,
					experimentName + ( ( 2 > _numberOfTimes) ? "" : ( "(" + String.valueOf( i) + ")")),
					false);
			}
		}
	}

	/**
	 * Exports the ModelBuilder script to the specified file in CSV format for Grid.
	 * @param script_directory the name of directory for the script file on Grid
	 * @param log_directory the name of directory for the log files on Grid
	 * @param programDirectory the name of directory for the program file on Grid
	 * @param numberOfTimes the number of the experiment times
	 * @param sftpClient the client for SSH File Transfer Protocol
	 * @param intBuffer the couter for progress
	 * @param textField the text field to display progress
	 */
	public void export(String scriptDirectoryName, String logDirectoryName, String programDirectory, int numberOfTimes, SftpClient sftpClient, IntBuffer intBuffer, JTextField textField) {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String experimentName = ( String)entry.getKey();
			InitialValueMap initialValueMap = ( InitialValueMap)entry.getValue();
			if ( !initialValueMap._export)
				continue;

			for ( int i = 1; i <= numberOfTimes; ++i) {
				if ( !SshTool.directory_exists( sftpClient, scriptDirectoryName + "/" + experimentName + "/" + String.valueOf( i)))
					sftpClient.mkdirs( scriptDirectoryName + "/" + experimentName + "/" + String.valueOf( i));

				if ( !SshTool.directory_exists( sftpClient, scriptDirectoryName + "/" + experimentName + "/" + String.valueOf( i)))
					return;

				File scriptFile; 
				try {
					scriptFile = File.createTempFile( "soars_", ".sor");
				} catch (IOException e) {
					return;
				}

				Exporter.execute_on_grid( scriptFile, initialValueMap, experimentName + "/" + String.valueOf( i), scriptDirectoryName, logDirectoryName, programDirectory,
					experimentName + ( ( 2 > _numberOfTimes) ? "" : ( "(" + String.valueOf( i) + ")")),
					true);

				try {
					sftpClient.put( scriptFile.getAbsolutePath(), scriptDirectoryName + "/" + experimentName + "/" + String.valueOf( i) + "/" + Constant._soarsScriptFilename);
				} catch (IOException e) {
					scriptFile.delete();
					return;
//				} catch (Throwable ex) {
//					script_file.delete();
//					return;
				}

				scriptFile.delete();

				intBuffer.put( 0, intBuffer.get( 0) + 1);
				textField.setText( String.valueOf( intBuffer.get( 0)));
				textField.update( textField.getGraphics());
			}
		}
	}

	/**
	 * Returns true for exporting the ModelBuilder script to the specified file in CSV format for Application Builder successfully.
	 * @param file the specified file
	 * @param toDisplay whether to display the logs
	 * @param toFile whether to make the log files
	 * @return true for exporting the ModelBuilder script to the specified file in CSV format for Application Builder successfully
	 */
	public boolean export_on_demo(File file, boolean toDisplay, boolean toFile) {
		if ( isEmpty())
			return false;

		InitialValueMap ivm = get_top_initialValueMap();
		if ( ivm.isEmpty())
			return false;

		InitialValueMap initialValueMap = new InitialValueMap();
		initialValueMap._export = true;
		Iterator iterator = ivm.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String alias = ( String)entry.getKey();
			initialValueMap.put( alias, "(" + alias + ")");
		}

		return Exporter.execute_on_demo( file, initialValueMap, null, Constant._experimentName, toDisplay, toFile);
	}

	/**
	 * Returns true for exporting the experiment table data to the specified file in CSV format for Application Builder successfully.
	 * @param file the specified file
	 * @return true for exporting the experiment table data to the specified file in CSV format for Application Builder successfully
	 */
	public boolean export_table_on_demo(File file) {
		InitialValueMap initialValueMap = get_top_initialValueMap();
		if ( initialValueMap.isEmpty())
			return false;

		String[] aliases = ( String[])initialValueMap.keySet().toArray( new String[ 0]);

		String text = "\t";

		for ( int i = 0; i < aliases.length; ++i)
			text += ( "\t" + aliases[ i]);

		text += Constant._lineSeparator;

		text += "\t";

		for ( int i = 0; i < aliases.length; ++i) {
			String comment = ( String)_commentMap.get( aliases[ i]);
			text += ( "\t" + ( ( null == comment) ? "" : comment));
		}

		text += Constant._lineSeparator;

		String[] experimentNames = ( String[])( new Vector( keySet())).toArray( new String[ 0]);
		QuickSort.sort( experimentNames, new StringNumberComparator( true, false));

		for ( int i = 0; i < experimentNames.length; ++i) {
			initialValueMap = ( InitialValueMap)get( experimentNames[ i]);
			if ( !initialValueMap._export)
				continue;

			String[] values = ( String[])initialValueMap.values().toArray( new String[ 0]);
			if ( values.length != aliases.length)
				return false;

			text += ( experimentNames[ i] + "\t" + initialValueMap._comment);

			for ( int j = 0; j < values.length; ++j)
				text += ( "\t" + values[ j]);

			text += Constant._lineSeparator;
		}

		String table = "";
		if ( file.getName().endsWith( ".sor"))
			table = ( file.getName().substring( 0, file.getName().length() - ".sor".length()) + ".tbl");
		else
			table = ( file.getName() + ".tbl");

		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( new File( file.getParent() + "/" + table)), "UTF-8");
			outputStreamWriter.write( text);
			outputStreamWriter.flush();
			outputStreamWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Updates the array of column width with the specified one.
	 * @param columnWidths the specified array of column width
	 */
	public void setup_column_widths(Vector<Integer> columnWidths) {
		_columnWidths.clear();
		_columnWidths.addAll( columnWidths);
	}

	/**
	 * Updates all data.
	 */
	public void update_all() {
		on_update_object( "probability");
		on_update_object( "collection");
		on_update_object( "list");
		on_update_object( "map");
		on_update_object( "keyword");
		on_update_object( "number object");
		on_update_object( "time variable");
		on_update_object( "file");
		on_update_object( "exchange_algebra");
		on_update_object( "extransfer");
		on_update_role();
		on_update_simulation();
	}

	/**
	 * Returns true for writing this object data successfully.
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing this object data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(Writer writer) throws SAXException {
		if ( 0 == get_initial_value_count())
			return true;

		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "column", "", String.valueOf( get_column_count()));
		attributesImpl.addAttribute( null, null, "number_of_times", "", String.valueOf( _numberOfTimes));
		attributesImpl.addAttribute( null, null, "parallel", "", _parallel ? "true" : "false");
		for ( int i = 0; i < _columnWidths.size(); ++i) {
			if ( 0 == i) {
				if ( _columnWidths.get( i).intValue() != _defaultExperimentTableCheckBoxColumnWidth)
					attributesImpl.addAttribute( null, null,
						"width" + i, "", String.valueOf( _columnWidths.get( i).intValue()));
			} else {
				if ( _columnWidths.get( i).intValue() != _defaultExperimentTableColumnWidth)
					attributesImpl.addAttribute( null, null,
						"width" + i, "", String.valueOf( _columnWidths.get( i).intValue()));
			}
		}

		writer.startElement( null, null, "experiment_data", attributesImpl);

		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			InitialValueMap initialValueMap = ( InitialValueMap)entry.getValue();

			attributesImpl = new AttributesImpl();
			attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( name));
			attributesImpl.addAttribute( null, null, "export", "", initialValueMap._export ? "true" : "false");

			writer.startElement( null, null, "experiment", attributesImpl);

			initialValueMap.write( writer);

			if ( !initialValueMap._comment.equals( "")) {
				writer.startElement( null, null, "comment", new AttributesImpl());
				writer.characters( initialValueMap._comment.toCharArray(), 0, initialValueMap._comment.length());
				writer.endElement( null, null, "comment");
			}

			writer.endElement( null, null, "experiment");
		}

		iterator = _commentMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			String comment = ( String)entry.getValue();
			if ( comment.equals( ""))
				continue;

			attributesImpl = new AttributesImpl();
			attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( name));

			writer.startElement( null, null, "initial_value_comment", attributesImpl);
			writer.characters( comment.toCharArray(), 0, comment.length());
			writer.endElement( null, null, "initial_value_comment");
		}

		writer.endElement( null, null, "experiment_data");

		return true;
	}
}
