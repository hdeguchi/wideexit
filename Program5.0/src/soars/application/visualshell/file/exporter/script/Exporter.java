/*
 * 2005/06/11
 */
package soars.application.visualshell.file.exporter.script;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.MainFrame;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.base.DrawObject;
import soars.application.visualshell.object.base.DrawObjectIdComparator;
import soars.application.visualshell.object.chart.ChartObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.object.class_variable.ClassVariableObject;
import soars.application.visualshell.object.entity.base.object.number.NumberObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.log.LogManager;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.spot.SpotRole;
import soars.application.visualshell.object.scripts.OtherScriptsManager;
import soars.application.visualshell.object.simulation.SimulationManager;
import soars.application.visualshell.object.stage.StageManager;
import soars.application.visualshell.observer.WarningDlg2;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.sort.QuickSort;

/**
 * Exports the ModelBuilder script to the file in CSV format.
 * @author kurata / SOARS project
 */
public class Exporter {

	/**
	 * Returns true for exporting the ModelBuilder script to the specified file in CSV format successfully.
	 * @param file the specified file
	 * @param initialValueMap the initial value hashtable(alias(String) - value(String))
	 * @param workDirectoryName the name of the directory for the log files
	 * @param experimentName the name of the experiment
	 * @param toDisplay whether to display the logs
	 * @param toFile whether to make the log files
	 * @param ga
	 * @return true for exporting the ModelBuilder script to the specified file in CSV format successfully
	 */
	public static boolean execute_on_model_builder(File file, InitialValueMap initialValueMap, String workDirectoryName, String experimentName, boolean toDisplay, boolean toFile, boolean ga) {
		String script = get_script_on_model_builder( initialValueMap, workDirectoryName, experimentName, toDisplay, toFile, ga);
		if ( null == script)
			return false;

		try {
			OutputStreamWriter outputStreamWriter;
			if ( 0 <= System.getProperty( "os.name").indexOf( "Mac")
				&& !System.getProperty( Constant._systemDefaultFileEncoding, "").equals( ""))
				outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file),
					System.getProperty( Constant._systemDefaultFileEncoding, ""));
			else
				outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file));

			outputStreamWriter.write( script);
			outputStreamWriter.flush();
			outputStreamWriter.close();
//			FileWriter writer = new FileWriter( file);
//			writer.write( script);
//			writer.flush();
//			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Exports the ModelBuilder script to the clipboard in CSV format.
	 * @param initialValueMap the initial value hashtable(alias(String) - value(String))
	 * @param experimentName the name of the experiment
	 * @param toDisplay whether to display the logs
	 * @param toFile whether to make the log files
	 */
	public static void execute(InitialValueMap initialValueMap, String experimentName, boolean toDisplay, boolean toFile) {
		String script = get_script_on_model_builder( initialValueMap, null, experimentName, toDisplay, toFile, false);
		if ( null == script)
			return;

		Clipboard.set( script);
		//System.out.println( Clipboard.get());
	}

	/**
	 * @param initialValueMap
	 * @param workDirectoryName
	 * @param experimentName
	 * @param toDisplay
	 * @param toFile
	 * @param ga
	 * @return
	 */
	private static String get_script_on_model_builder(InitialValueMap initialValueMap, String workDirectoryName, String experimentName, boolean toDisplay, boolean toFile, boolean ga) {
		WarningManager.get_instance().cleanup();

//		String script = CommentManager.get_instance().get_script();
//		script += OtherScriptsManager.get_instance().get_script( "", false);
		String script = OtherScriptsManager.get_instance().get_script( "", "", false, ga);
		script += get_role_script( false, ga);
		script += get_spot_script( initialValueMap, false, ga);
		script += get_agent_script( initialValueMap, false, ga);
		script += get_spot_variable_script( initialValueMap);
		script += get_agent_variable_script( initialValueMap);
		script += StageManager.get_instance().get_script( false, ga);
		script += get_rule_script( initialValueMap, experimentName, false, ga, false);
		script += LogManager.get_instance().get_script_on_model_builder( workDirectoryName, toDisplay, toFile);
		script += SimulationManager.get_instance().get_script( initialValueMap);

		if ( !WarningManager.get_instance().isEmpty()) {
			WarningDlg2 warningDlg2 = new WarningDlg2(
				MainFrame.get_instance(),
				ResourceManager.get_instance().get( "warning.dialog2.title"),
				ResourceManager.get_instance().get( "warning.dialog2.message1"),
				MainFrame.get_instance());
			warningDlg2.do_modal();
			if ( !warningDlg2._continue)
				return null;
		}

		return script;
	}

	/**
	 * Returns true for exporting the ModelBuilder script to the specified file in CSV format successfully.
	 * @param file the specified file
	 * @param initialValueMap the initial value hashtable(alias(String) - value(String))
	 * @param workDirectoryName the name of the directory for the log files
	 * @param experimentName the name of the experiment
	 * @param toFile whether to make the log files
	 * @return true for exporting the ModelBuilder script to the specified file in CSV format successfully
	 */
	public static boolean execute_on_docker(File file, InitialValueMap initialValueMap, String workDirectoryName, String experimentName, boolean toFile) {
		String script = get_script_on_docker( initialValueMap, workDirectoryName, experimentName, toFile);
		if ( null == script)
			return false;

		// スクリプト内のユーザディレクトリを置き換える
		while ( 0 <= script.indexOf( Constant._reservedUserDataDirectory))
			script = script.replace( Constant._reservedUserDataDirectory, Constant._userDataDirectoryForDocker);

		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file), "UTF-8");
			outputStreamWriter.write( script);
			outputStreamWriter.flush();
			outputStreamWriter.close();
//			FileWriter writer = new FileWriter( file);
//			writer.write( script);
//			writer.flush();
//			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param initialValueMap
	 * @param workDirectoryName
	 * @param experimentName
	 * @param toFile
	 * @return
	 */
	private static String get_script_on_docker(InitialValueMap initialValueMap, String workDirectoryName, String experimentName, boolean toFile) {
		WarningManager.get_instance().cleanup();

		String script = OtherScriptsManager.get_instance().get_script_on_docker();
		script += get_role_script( true, false);
		script += get_spot_script( initialValueMap, true, false);
		script += get_agent_script( initialValueMap, true, false);
		script += get_spot_variable_script( initialValueMap);
		script += get_agent_variable_script( initialValueMap);
		script += StageManager.get_instance().get_script( true, false);
		script += get_rule_script( initialValueMap, experimentName, true, false, false);
		script += LogManager.get_instance().get_script_on_model_builder( workDirectoryName, false, toFile);
		script += SimulationManager.get_instance().get_script( initialValueMap);

		if ( !WarningManager.get_instance().isEmpty()) {
			WarningDlg2 warningDlg2 = new WarningDlg2(
				MainFrame.get_instance(),
				ResourceManager.get_instance().get( "warning.dialog2.title"),
				ResourceManager.get_instance().get( "warning.dialog2.message1"),
				MainFrame.get_instance());
			warningDlg2.do_modal();
			if ( !warningDlg2._continue)
				return null;
		}

		return script;
	}

//	/**
//	 * @param file
//	 * @param initialValueMap
//	 * @param work_directory
//	 * @param experiment_name
//	 * @return
//	 */
//	public static boolean execute_on_animator(File file, InitialValueMap initialValueMap, File work_directory, String experiment_name) {
//		String script = get_script_on_animator( initialValueMap, work_directory, experiment_name);
//		if ( null == script)
//			return false;
//
//		try {
//			OutputStreamWriter outputStreamWriter;
//			if ( 0 <= System.getProperty( "os.name").indexOf( "Mac")
//				&& !System.getProperty( Constant._system_default_file_encoding, "").equals( ""))
//				outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file),
//					System.getProperty( Constant._system_default_file_encoding, ""));
//			else
//				outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file));
//
//			outputStreamWriter.write( script);
//			outputStreamWriter.flush();
//			outputStreamWriter.close();
////			FileWriter writer = new FileWriter( file);
////			writer.write( script);
////			writer.flush();
////			writer.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
//
//		return true;
//	}
//
//	/**
//	 * @param initialValueMap
//	 * @param work_directory
//	 * @param experiment_name
//	 * @return
//	 */
//	private static String get_script_on_animator(InitialValueMap initialValueMap, File work_directory, String experiment_name) {
//		WarningManager.get_instance().cleanup();
//
////		String script = CommentManager.get_instance().get_script();
////		script += OtherScriptsManager.get_instance().get_script( "", false);
//		String script = OtherScriptsManager.get_instance().get_script( "", false);
//		script += get_role_script( false);
//		script += get_spot_script( initialValueMap, false);
//		script += get_agent_script( initialValueMap, false);
//		script += get_spot_variable_script( initialValueMap);
//		script += get_agent_variable_script( initialValueMap);
//		script += StageManager.get_instance().get_script( false);
//		script += get_rule_script( initialValueMap, experiment_name, false, false);
//		script += LogManager.get_instance().get_script_on_animator( work_directory);
//		script += SimulationManager.get_instance().get_script( initialValueMap);
//
//		if ( !WarningManager.get_instance().isEmpty()) {
//			WarningDlg2 warningDlg2 = new WarningDlg2(
//				MainFrame.get_instance(),
//				ResourceManager.get_instance().get( "warning.dialog2.title"),
//				ResourceManager.get_instance().get( "warning.dialog2.message1"),
//				MainFrame.get_instance());
//			warningDlg2.do_modal();
//			if ( !warningDlg2._continue)
//				return null;
//		}
//
//		return script;
//	}

	/**
	 * Returns true for exporting the ModelBuilder script to the specified file in CSV format for Grid successfully.
	 * @param file the specified file
	 * @param initialValueMap the initial value hashtable(alias(String) - value(String))
	 * @param subDirectory the name of sub directory for the script and log files on Grid
	 * @param scriptDirectory the name of directory for the script files on Grid
	 * @param logDirectory the name of directory for the log files on Grid
	 * @param programDirectory the name of directory for the program file on Grid
	 * @param experimentName the name of the experiment
	 * @param conversion whether to convert the character code
	 * @return true for exporting the ModelBuilder script to the specified file in CSV format for Grid successfully
	 */
	public static boolean execute_on_grid(File file, InitialValueMap initialValueMap, String subDirectory, String scriptDirectory, String logDirectory, String programDirectory, String experimentName, boolean conversion) {
		String script = get_script_on_grid( initialValueMap, subDirectory, scriptDirectory, logDirectory, programDirectory, experimentName);
		if ( null == script)
			return false;

		// スクリプト内のユーザディレクトリをGrid上のスクリプトディレクトリに置き換える
		while ( 0 <= script.indexOf( Constant._reservedUserDataDirectory))
			script = script.replace( Constant._reservedUserDataDirectory, logDirectory + "/" + subDirectory + "/" + Constant._userDataDirectory + "/");

		try {
			OutputStreamWriter outputStreamWriter;
			if ( conversion)
				outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file), "UTF-8");
			else {
				if ( 0 <= System.getProperty( "os.name").indexOf( "Mac")
					&& !System.getProperty( Constant._systemDefaultFileEncoding, "").equals( ""))
					outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file),
						System.getProperty( Constant._systemDefaultFileEncoding, ""));
				else
					outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file));
			}

			outputStreamWriter.write( script);
			outputStreamWriter.flush();
			outputStreamWriter.close();
//			FileWriter writer = new FileWriter( file);
//			writer.write( script);
//			writer.flush();
//			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param initialValueMap
	 * @param subDirectory
	 * @param scriptDirectory
	 * @param logDirectory
	 * @param programDirectory
	 * @param experimentName
	 * @return
	 */
	private static String get_script_on_grid(InitialValueMap initialValueMap, String subDirectory, String scriptDirectory, String logDirectory, String programDirectory, String experimentName) {
		WarningManager.get_instance().cleanup();

//		String script = CommentManager.get_instance().get_script();
//		script += OtherScriptsManager.get_instance().get_script( program_directory, true);
		String script = OtherScriptsManager.get_instance().get_script( scriptDirectory, programDirectory, true, false);
		script += get_role_script( true, false);
		script += get_spot_script( initialValueMap, true, false);
		script += get_agent_script( initialValueMap, true, false);
		script += get_spot_variable_script( initialValueMap);
		script += get_agent_variable_script( initialValueMap);
		script += StageManager.get_instance().get_script( true, false);
		script += get_rule_script( initialValueMap, experimentName, true, false, false);
		script += LogManager.get_instance().get_script_on_grid( subDirectory, logDirectory);
		script += SimulationManager.get_instance().get_script( initialValueMap);

		if ( !WarningManager.get_instance().isEmpty()) {
			WarningDlg2 warningDlg2 = new WarningDlg2(
				MainFrame.get_instance(),
				ResourceManager.get_instance().get( "warning.dialog2.title"),
				ResourceManager.get_instance().get( "warning.dialog2.message1"),
				MainFrame.get_instance());
			warningDlg2.do_modal();
			if ( !warningDlg2._continue)
				return null;
		}

		return script;
	}

	/**
	 * Returns true for exporting the ModelBuilder script to the specified file in CSV format for Application Builder successfully.
	 * @param file the specified file
	 * @param initialValueMap the initial value hashtable(alias(String) - value(String))
	 * @param workDirectoryName the name of the directory for the log files
	 * @param experimentName the name of the experiment
	 * @param toDisplay whether to display the logs
	 * @param toFile whether to make the log files
	 * @return true for exporting the ModelBuilder script to the specified file in CSV format for Application Builder successfully
	 */
	public static boolean execute_on_demo(File file, InitialValueMap initialValueMap, String workDirectoryName, String experimentName, boolean toDisplay, boolean toFile) {
		String script = get_script_on_demo( initialValueMap, workDirectoryName, experimentName, toDisplay, toFile);
		if ( null == script)
			return false;

		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file), "UTF-8");
			outputStreamWriter.write( script);
			outputStreamWriter.flush();
			outputStreamWriter.close();
//			FileWriter writer = new FileWriter( file);
//			writer.write( script);
//			writer.flush();
//			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param initialValueMap
	 * @param workDirectoryName
	 * @param experimentName
	 * @param toDisplay
	 * @param toFile
	 * @return
	 */
	private static String get_script_on_demo(InitialValueMap initialValueMap, String workDirectoryName, String experimentName, boolean toDisplay, boolean toFile) {
		WarningManager.get_instance().cleanup();

//		String script = CommentManager.get_instance().get_script();
//		script += OtherScriptsManager.get_instance().get_script( "", false);
		String script = OtherScriptsManager.get_instance().get_script( "", "", false, false);
		script += get_role_script( false, false);
		script += get_spot_script( null, false, false);
		script += get_agent_script( null, false, false);
		script += get_spot_variable_script( null);
		script += get_agent_variable_script( null);
		script += StageManager.get_instance().get_script( false, false);
		script += get_rule_script( initialValueMap, experimentName, false, false, true);
		script += LogManager.get_instance().get_script_on_model_builder( workDirectoryName, toDisplay, toFile);
		script += SimulationManager.get_instance().get_script( initialValueMap);

		if ( !WarningManager.get_instance().isEmpty()) {
			WarningManager.get_instance().print();
			return null;
		}

		return script;
	}

	/**
	 * @param grid
	 * @param ga
	 * @return
	 */
	private static String get_role_script(boolean grid, boolean ga) {
		Vector<DrawObject> rs = new Vector<DrawObject>();
		LayerManager.get_instance().get_roles( rs);

		// for chart roles and initial data file role
		String chartRoleNames = get_chart_role_names( grid || ga);
		String initialDataFileRoleName = get_initial_data_file_role_name();
		if ( rs.isEmpty()) {
			if ( chartRoleNames.equals( "") && initialDataFileRoleName.equals( ""))
				return "";

			String script = ( "role" + Constant._lineSeparator);
			if ( !chartRoleNames.equals( ""))
				script += ( chartRoleNames + Constant._lineSeparator);
			if ( !initialDataFileRoleName.equals( ""))
				script += ( initialDataFileRoleName + Constant._lineSeparator);
			return script;
		} else if ( 1 == rs.size()) {
			Role role = ( Role)rs.get( 0);
			if ( role._name.equals( "")) {
				if ( chartRoleNames.equals( "") && initialDataFileRoleName.equals( ""))
					return "";

				String script = ( "role" + Constant._lineSeparator);
				if ( !chartRoleNames.equals( ""))
					script += ( chartRoleNames + Constant._lineSeparator);
				if ( !initialDataFileRoleName.equals( ""))
					script += ( initialDataFileRoleName + Constant._lineSeparator);
				return script;
			}
		}

		Role[] roles = rs.toArray( new Role[ 0]);
		if ( 1 < roles.length)
			QuickSort.sort( roles, new DrawObjectIdComparator());

		String script = "role" + Constant._lineSeparator;
		for ( int i = 0; i < roles.length; ++i) {
			if ( roles[ i] instanceof SpotRole || roles[ i]._name.equals( ""))
				continue;

			// for Mr.Ichikawa
			script += ( roles[ i].get_name() + "\t" + "adapter.Rules" + Constant._lineSeparator);
			//script += ( roles[ i].get_name() + Constant._line_separator);
		}

		for ( int i = 0; i < roles.length; ++i) {
			if ( roles[ i] instanceof AgentRole || roles[ i]._name.equals( ""))
				continue;

			// for Mr.Ichikawa
			script += ( roles[ i].get_name() + "\t" + "adapter.Rules" + Constant._lineSeparator);
			//script += ( roles[ i].get_name() + Constant._line_separator);
		}

		if ( !chartRoleNames.equals( ""))
			script += chartRoleNames;

		// for initial data file role
		if ( !initialDataFileRoleName.equals( ""))
			script += initialDataFileRoleName;

		return script + Constant._lineSeparator;
	}

	/**
	 * @param gridOrGA
	 * @return
	 */
	private static String get_chart_role_names(boolean gridOrGA) {
		if ( gridOrGA)
			return "";

		String script = "";

		Vector<DrawObject> charts = new Vector<DrawObject>();
		LayerManager.get_instance().get_charts( charts);

		for ( int i = 0; i < charts.size(); ++i) {
			ChartObject chartObject = ( ChartObject)charts.get( i);
			if ( !chartObject.is_available())
				continue;

			// for Mr.Ichikawa
			script += ( chartObject._name + "\t" + "adapter.Rules" + Constant._lineSeparator);
			//script += ( chartObject._name + Constant._line_separator);
		}

		return script;
	}

	/**
	 * @return
	 */
	private static String get_initial_data_file_role_name() {
		return LayerManager.get_instance().initial_data_file_exists() ? ( Constant._initialDataFileRoleName + "\t" + "adapter.Rules" + Constant._lineSeparator) : "";
	}

	/**
	 * @param initialValueMap
	 * @param grid
	 * @param ga
	 * @return
	 */
	private static String get_spot_script(InitialValueMap initialValueMap, boolean grid, boolean ga) {
		Vector<DrawObject> spots = new Vector<DrawObject>();
		LayerManager.get_instance().get_spots( spots);
		spots.add( get_expressionSpot());	// TODO シミュレーション実行時にだけ使われる数式スポットを作成して追加する
		spots.add( get_userRuleSpot());		// TODO シミュレーション実行時にだけ使われるユーザ定義ルールスポットを作成して追加する
		return get_object_base_script( "spot", spots, "spotNumber\tspotName\t", "spotCommand\t", "spotCreate", initialValueMap, grid, ga);
	}

	/**
	 * @return
	 */
	private static DrawObject get_expressionSpot() {
		// TODO Auto-generated method stub
		SpotObject spotObject = new SpotObject( Constant._expressionSpotName);
		spotObject._objectMapMap.get( "number object").put( Constant._expressionResultNemericVariable1, new NumberObject( Constant._expressionResultNemericVariable1, "real number", "0.0"));
		spotObject._objectMapMap.get( "number object").put( Constant._expressionResultNemericVariable2, new NumberObject( Constant._expressionResultNemericVariable2, "real number", "0.0"));
		int size = VisualShellExpressionManager.get_instance().get_max_variables_count();
		for ( int i = 0; i < size; ++i) {
			String name = Constant._expressionNemericVariablePrefix + String.valueOf( i);
			spotObject._objectMapMap.get( "number object").put( name, new NumberObject( name, "real number", "0.0"));
		}
		return spotObject;
	}

	/**
	 * @return
	 */
	private static DrawObject get_userRuleSpot() {
		// TODO Auto-generated method stub
		SpotObject spotObject = new SpotObject( Constant._userRuleSpotName);
		spotObject._objectMapMap.get( "class variable").put( Constant._userRuleClassVariableName, new ClassVariableObject( Constant._userRuleClassVariableName, Constant._userrulesJar, Constant._userRuleClassName));
		return spotObject;
	}

	/**
	 * @param initialValueMap
	 * @param grid
	 * @param ga
	 * @return
	 */
	private static String get_agent_script(InitialValueMap initialValueMap, boolean grid, boolean ga) {
		Vector<DrawObject> agents = new Vector<DrawObject>();
		LayerManager.get_instance().get_agents( agents);
		return get_object_base_script( "agent", agents, "agentNumber\tagentName\t", "agentCommand\t", "agentCreate", initialValueMap, grid, ga);
	}

	/**
	 * @param type
	 * @param drawObjects
	 * @param string1
	 * @param string2
	 * @param string3
	 * @param initialValueMap
	 * @param grid
	 * @param ga
	 * @return
	 */
	private static String get_object_base_script(String type, Vector<DrawObject> drawObjects, String string1, String string2, String string3, InitialValueMap initialValueMap, boolean grid, boolean ga) {
		String script1 = "";

		EntityBase[] entityBases = drawObjects.toArray( new EntityBase[ 0]);
		if ( 1 < entityBases.length)
			QuickSort.sort( entityBases, new DrawObjectIdComparator());

		IntBuffer command = IntBuffer.allocate( 1);
		command.put( 0, 0);
		for ( int i = 0; i < entityBases.length; ++i)
			script1 += entityBases[ i].get_script( command, initialValueMap/*, grid*/);

		if ( !grid && !ga && type.equals( "spot"))
			script1 += get_chart_spot_script( command);

		if ( type.equals( "spot"))
			script1 += get_initial_data_file_spot_script( command);

		if ( script1.equals( ""))
			return "";

		String script = "itemData" + Constant._lineSeparator;
		script += string1;

		for ( int i = 0; i < command.get( 0); ++i)
			script += string2;

		script += ( string3 + Constant._lineSeparator);

		script += ( script1 + Constant._lineSeparator);

		return script;
	}

	/**
	 * @param command
	 * @return
	 */
	private static String get_chart_spot_script(IntBuffer command) {
		String script = "";

		Vector<DrawObject> charts = new Vector<DrawObject>();
		LayerManager.get_instance().get_charts( charts);

		for ( int i = 0; i < charts.size(); ++i) {
			ChartObject chartObject = ( ChartObject)charts.get( i);
			if ( !chartObject.is_available())
				continue;

			script += chartObject.get_chart_spot_script( command);
		}

		return script;
	}

	/**
	 * @param command
	 * @return
	 */
	private static String get_initial_data_file_spot_script(IntBuffer command) {
		// TODO Auto-generated method stub
		List<String> initialDataFiles = new ArrayList<String>();
		LayerManager.get_instance().get_initial_data_files( initialDataFiles);

		List<String> exchangeAlgebraInitialDataFiles = new ArrayList<String>();
		LayerManager.get_instance().get_exchange_algebra_initial_data_files( exchangeAlgebraInitialDataFiles);

		if ( initialDataFiles.isEmpty() && exchangeAlgebraInitialDataFiles.isEmpty())
			return "";

		String script = ( "\t" + Constant._initialDataFileSpotName
			+ "\t<>startRule " + Constant._initialDataFileRoleName
			+ "\t<>setSpot __spot_variable"
			+ ( initialDataFiles.isEmpty() ? "" : ( "\t<>setClass " + Constant._initialDataFileClassVariableName + "=" + Constant._initialDataFileClassName + " ; <>logEquip " + Constant._initialDataFileClassVariableName))
			+ ( exchangeAlgebraInitialDataFiles.isEmpty() ? "" : ( "\t<>setClass " + Constant._exchangeAlgebraInitialDataFileClassVariableName + "=" + Constant._exchangeAlgebraInitialDataFileClassName + " ; <>logEquip " + Constant._exchangeAlgebraInitialDataFileClassVariableName)));

		IntBuffer counter = IntBuffer.allocate( 1);
		counter.put( 0, 2 + ( initialDataFiles.isEmpty() ? 0 : 1) + ( exchangeAlgebraInitialDataFiles.isEmpty() ? 0 : 1));

		for ( int i = 0; i < initialDataFiles.size(); ++i) {
			script += ( "\t<>keyword " + Constant._initialDataFileVariableName + String.valueOf( i) + "=" + Constant._reservedUserDataDirectory + initialDataFiles.get( i));
			counter.put( 0, counter.get( 0) + 1);
		}

		for ( int i = 0; i < exchangeAlgebraInitialDataFiles.size(); ++i) {
			script += ( "\t<>keyword " + Constant._exchangeAlgebraInitialDataFileVariableName + String.valueOf( i) + "=" + Constant._reservedUserDataDirectory + exchangeAlgebraInitialDataFiles.get( i));
			counter.put( 0, counter.get( 0) + 1);
		}

		if ( command.get( 0) < counter.get( 0))
			command.put( 0, counter.get( 0));

		return ( script + Constant._lineSeparator);
	}

	/**
	 * @param initialValueMap
	 * @return
	 */
	private static String get_spot_variable_script(InitialValueMap initialValueMap) {
		Vector<DrawObject> spots = new Vector<DrawObject>();
		LayerManager.get_instance().get_spots( spots);
		if ( spots.isEmpty())
			return "";

		return get_entity_base_variable_initial_values_script( spots, "spotNumber\tspotName\t", "spotCommand\t", "spotInitialize", initialValueMap);
	}

	/**
	 * @param initialValueMap
	 * @return
	 */
	private static String get_agent_variable_script(InitialValueMap initialValueMap) {
		Vector<DrawObject> agents = new Vector<DrawObject>();
		LayerManager.get_instance().get_agents( agents);
		if ( agents.isEmpty())
			return "";

		return get_entity_base_variable_initial_values_script( agents, "agentNumber\tagentName\t", "agentCommand\t", "agentInitialize", initialValueMap);
	}

	/**
	 * @param drawObjects
	 * @param string1
	 * @param string2
	 * @param string3
	 * @param initialValueMap
	 * @return
	 */
	private static String get_entity_base_variable_initial_values_script(Vector<DrawObject> drawObjects, String string1, String string2, String string3, InitialValueMap initialValueMap) {
		String script1 = "";

		EntityBase[] entityBases = drawObjects.toArray( new EntityBase[ 0]);
		if ( 1 < entityBases.length)
			QuickSort.sort( entityBases, new DrawObjectIdComparator());

		IntBuffer command = IntBuffer.allocate( 1);
		command.put( 0, 0);
		for ( int i = 0; i < entityBases.length; ++i)
			script1 += entityBases[ i].get_variable_initial_values_script( command, initialValueMap);

		if ( script1.equals( ""))
			return "";

		String script = "itemData" + Constant._lineSeparator;
		script += string1;

		for ( int i = 0; i < command.get( 0); ++i)
			script += string2;

		script += ( string3 + Constant._lineSeparator);

		script += ( script1 + Constant._lineSeparator);

		return script;
	}

	/**
	 * @param initialValueMap
	 * @param experimentName
	 * @param grid
	 * @param ga
	 * @param demo
	 * @return
	 */
	private static String get_rule_script(InitialValueMap initialValueMap, String experimentName, boolean grid, boolean ga, boolean demo) {
		Vector<DrawObject> rs = new Vector<DrawObject>();
		LayerManager.get_instance().get_roles( rs);
		if ( rs.isEmpty() && ( grid || ga || !LayerManager.get_instance().contains_available_chartObject())
			&& !LayerManager.get_instance().initial_data_file_exists())
			return "";

		Role[] roles = rs.toArray( new Role[ 0]);
		if ( 1 < roles.length)
			QuickSort.sort( roles, new DrawObjectIdComparator());

		IntBuffer ruleCount = IntBuffer.allocate( 1);
		ruleCount.put( 0, 0);
		for ( int i = 0; i < roles.length; ++i)
			roles[ i].how_many_rules( ruleCount);
			 
		if ( !grid && !ga && LayerManager.get_instance().contains_available_chartObject() && ruleCount.get( 0) < 2)
			ruleCount.put( 0, 2);

		if ( LayerManager.get_instance().initial_data_file_exists() && ruleCount.get( 0) < 2)
			ruleCount.put( 0, 2);

		String script1 = "itemData" + Constant._lineSeparator;
		String script2 = "ruleRole\truleStage\t";
		String script3 = "";

		for ( int i = 0; i < ruleCount.get( 0) - 1; ++i)
			script2 += "ruleCondition\t";

		for ( int i = 0; i < roles.length; ++i) {
			if ( roles[ i] instanceof SpotRole)
				continue;

			script3 += roles[ i].get_script( ruleCount.get( 0), initialValueMap, demo);
		}

		for ( int i = 0; i < roles.length; ++i) {
			if ( roles[ i] instanceof AgentRole)
				continue;

			script3 += roles[ i].get_script( ruleCount.get( 0), initialValueMap, demo);
		}


		if ( LayerManager.get_instance().initial_data_file_exists())
			script3 += get_initial_data_file_rule_script( ruleCount.get( 0));


		// ルール最終列に従来通り " ; " 区切りで記述し、最後に " ; TRUE" を追加する？
		if ( !grid && !ga && LayerManager.get_instance().contains_available_chartObject()) {
			Vector<DrawObject> cos = new Vector<DrawObject>();
			LayerManager.get_instance().get_charts( cos);
			if ( !cos.isEmpty()) {
				ChartObject[] chartObjects = cos.toArray( new ChartObject[ 0]);
				if ( 1 < chartObjects.length)
					QuickSort.sort( chartObjects, new DrawObjectIdComparator());

				for ( int i = 0; i < chartObjects.length; ++i) {
					if ( !chartObjects[ i].is_available())
						continue;

					script3 += chartObjects[ i].get_role_initialize_command( ruleCount.get( 0), experimentName);
					script3 += chartObjects[ i].get_role_update_command( ruleCount.get( 0));
				}
			}
		}


		String script = "";
		script += script1;
		script += ( script2 + "ruleCreate" + Constant._lineSeparator);
		script += ( script3 + Constant._lineSeparator);

		return script;
	}

	/**
	 * @param ruleCount
	 * @return
	 */
	private static String get_initial_data_file_rule_script(int ruleCount) {
		// TODO Auto-generated method stub
		// ルール最終列に従来通り " ; " 区切りで記述し、最後に " ; TRUE" を追加する？
		List<String> initialDataFiles = new ArrayList<String>();
		LayerManager.get_instance().get_initial_data_files( initialDataFiles);

		List<String> exchangeAlgebraInitialDataFiles = new ArrayList<String>();
		LayerManager.get_instance().get_exchange_algebra_initial_data_files( exchangeAlgebraInitialDataFiles);

		if ( initialDataFiles.isEmpty() && exchangeAlgebraInitialDataFiles.isEmpty())
			return "";

		String script = ( Constant._initialDataFileRoleName + "\t" + Constant._initialDataFileStageName);

		for ( int i = 0; i < ruleCount - 1; ++i)
			script += "\t";

		String command = "";
		for ( int i = 0; i < initialDataFiles.size(); ++i)
			command += ( ( command.equals( "") ? "" : " ; ") + "<>equip " + Constant._initialDataFileVariableName + String.valueOf( i) + " ; "
				+ "<>addParam " + Constant._initialDataFileClassVariableName + "=" + Constant._initialDataFileVariableName + String.valueOf( i) + "=java.lang.String ; "
				+ "<>invokeClass " + Constant._initialDataFileClassVariableName + "=" + Constant._initialDataFileClassMethodName);

		for ( int i = 0; i < exchangeAlgebraInitialDataFiles.size(); ++i)
			command += ( ( command.equals( "") ? "" : " ; ") + "<>equip " + Constant._exchangeAlgebraInitialDataFileVariableName + String.valueOf( i) + " ; "
				+ "<>addParam " + Constant._exchangeAlgebraInitialDataFileClassVariableName + "=" + Constant._exchangeAlgebraInitialDataFileVariableName + String.valueOf( i) + "=java.lang.String ; "
				+ "<>invokeClass " + Constant._exchangeAlgebraInitialDataFileClassVariableName + "=" + Constant._exchangeAlgebraInitialDataFileClassMethodName);

		script += command;

		return ( script + " ; TRUE\tInitial data file" + Constant._lineSeparator);
	}
}
