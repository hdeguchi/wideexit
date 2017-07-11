/*
 * Created on 2006/04/06
 */
package soars.application.visualshell.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.object.entity.base.object.number.NumberObject;
import soars.common.soars.constant.CommonConstant;
import soars.common.soars.environment.BasicEnvironment;
import soars.common.soars.environment.SoarsCommonEnvironment;

/**
 * Constant definition class.
 * @author kurata / SOARS project
 */
public class Constant extends CommonConstant {

	/**
	 * Application version string.
	 */
	static public final String _applicationVersion = "20170630";

	/**
	 * Resource directory string.
	 */
	static public final String _resourceDirectory = "/soars/application/visualshell/resource";

	/**
	 * Reserved layer name string.
	 */
	static public final String _reservedLayerName = "__new_layer";

	/**
	 * Unavailable characters for general name.
	 */
	static public final String _prohibitedCharacters1 = " \t=<>&|:;,?\"\\/*()";			// General

	/**
	 * Unavailable characters for agent name, spot name, agent role name, chart name and role variable name.
	 */
	static public final String _prohibitedCharacters2 = " \t=<>&|:;,?\"";						// Agent name, Spot name, Agent role name and Role variable name

	/**
	 * Unavailable characters for keyword initial value.
	 */
	//static public final String _prohibitedCharacters3 = "\t=&|;?\"";								// Keyword initial value
	static public final String _prohibitedCharacters3 = "\t=<>&|;,?\"";							// Keyword initial value

	/**
	 * Unavailable characters for numeric variable name and probability name.
	 */
	static public final String _prohibitedCharacters4 = " \t=<>&|:;,?\"+-*/\\()";		// Number variable name and Probability name

	/**
	 * Unavailable characters for numeric variable initial value and probability initial value.
	 */
	static public final String _prohibitedCharacters5 = " \t=<>&|:;,?\"\\*";				// Number variable initial value and Probability initial value

	/**
	 * Unavailable characters for spot role name.
	 */
	static public final String _prohibitedCharacters6 = " \t=&|:;,?\"";							// Spot role name

	/**
	 * Unavailable characters for chart title and axis name.
	 */
	static public final String _prohibitedCharacters7 = "\t=<>&|;,?";								// Chart title and axis name

	/**
	 * Unavailable characters for experiment name.
	 */
	static public final String _prohibitedCharacters8 = "\t\\/:;*?\"'<>|$&#()~`";		// Experiment name

	/**
	 * Unavailable characters for agent role name initial variable.
	 */
	static public final String _prohibitedCharacters9 = " \t=<>&|;,?\"";						// Agent role name initial variable

	/**
	 * Unavailable characters for spot role name initial variable.
	 */
	static public final String _prohibitedCharacters10 = " \t=&|;,?\"";							// Spot role name initial variable

	/**
	 * Unavailable characters for exchange algebra base name.
	 */
	static public final String _prohibitedCharacters11 = " \t=<>&|,?\"-'^%@_";			// Exchange algebra base name

	/**
	 * Unavailable characters for exchange algebra base full name.
	 */
	static public final String _prohibitedCharacters12 = " \t=&|?\"'%@";						// Exchange algebra base full name
	//static public final String _prohibitedCharacters12 = " \t=<>&|,?\"'^%@";				// Exchange algebra base full name

	/**
	 * Unavailable characters for time variable name.
	 */
	static public final String _prohibitedCharacters13 = " \t=<>&|:;,?\"\\/*@!+-";	// Time variable name

	/**
	 * Unavailable characters for time variable initial value.
	 */
	static public final String _prohibitedCharacters14 = " \t=<>&|:;,?\"\\/*@!+-";	// Time variable initial value

	/**
	 * Unavailable characters for others on NumberObject condition and Substitution command.
	 */
	static public final String _prohibitedCharacters15 = " \t=<>&|:;,?\"\\";				// Others on NumberObject condition and Substitution command

	/**
	 * Unavailable characters for file path name.
	 */
	static public final String _prohibitedCharacters16 = "\t=<>&|:;,?\"*\\";				// File path

	/**
	 * Unavailable characters for file path name.
	 */
	static public final String _prohibitedCharacters17 = "\t=<>&|:;,?\"*";					// File path for loading initial data

	/**
	 * Name of temporary spot variabletime.
	 */
	static public final String _spotVariableName = "__spot_variable";

	/**
	 * Name of current time.
	 */
	static public final String _currentTimeName = "__current_time";

	/**
	 * Unavailable characters for expression function name.
	 */
	static public final String _prohibitedExpressionCharacters1 = " !\"#$%&'()-=^~\\|\t@`[{;+:*]}<.>/?";	// Expression function1

	/**
	 * Unavailable characters for expression augument name.
	 */
	static public final String _prohibitedExpressionCharacters2 = " !\"#$%&'()-=^~\\|\t@`[{;+:*]}<.>/?";	// Expression function2

	/**
	 * Unavailable characters for expression name.
	 */
	static public final String _prohibitedExpressionCharacters3 = " !\"#$&'=^~\\|\t@`[{;:]}<>?";					// Expression expression	TODO 2015.9.20
	//static public final String _prohibitedExpressionCharacters3 = " !\"#$%&'=^~\\|\t@`[{;:]}<>?";					// Expression expression	TODO 2015.9.20

	/**
	 * Expression spot name.
	 */
	static public final String _expressionSpotName = "__expression_spot";

	/**
	 * Expression numeric variable prefix.
	 */
	static public final String _expressionNemericVariablePrefix = "__expression_numeric_variable";

	/**
	 * Expression numeric variable to receive result.
	 */
	static public final String _expressionResultNemericVariable1 = "__expression_result_numeric_variable1";

	/**
	 * Expression numeric variable to receive result.
	 */
	static public final String _expressionResultNemericVariable2 = "__expression_result_numeric_variable2";

	/**
	 * Identifier string of initial data file.
	 */
	static public final String _initialDataIdentifier = "SOARS initial data";

	/**
	 * Identifier string of Experiment table data file.
	 */
	static public final String _experimentTableDataIdentifier = "SOARS experiment table data";

	/**
	 * Name of stage to initialize chart.
	 */
	static public final String _initializeChartStageName = "__initialize_chart";

	/**
	 * Name of stage to update chart.
	 */
	static public final String _updateChartStageName = "__update_chart";

	/**
	 * SOARS classURL script for chart.
	 */
	static public final String[] _chartManagerJarScript = {
		"file:../function/chart/module/chartmanager.jar" + _lineSeparator,
		"file:../function/chart/module/chart.jar" + _lineSeparator,
		"file:../library/chart/plot.jar" + _lineSeparator,
		"file:../function/chart/module/get_value.jar" + _lineSeparator
	};

	/**
	 * Name of chart class.
	 */
	static public final String _chartManagerMainClassname = "soars.plugin.modelbuilder.chart.chartmanager.ChartManager";

	/**
	 * Name of class which gets the value of numeric variable for agent of spot.
	 */
	static public final String _chartGetValueMainClassname = "basic_module.GetValue";

	/**
	 * Name of method which gets the value of numeric variable for agent.
	 */
	static public final String _chartGetAgentValueMethodname = "get_agent_value";

	/**
	 * Name of method which gets the value of numeric variable for spot.
	 */
	static public final String _chartGetSpotValueMethodname = "get_spot_value";

	/**
	 * Name of variablemethod which gets the value of numeric variable for spot.
	 */
	static public final String _chartGetValueClassVariableName = "__get_value_class";

	/**
	 * Name of stage for initial data files.
	 */
	static public final String _initialDataFileStageName = "__initial_data_file_stage";

	/**
	 * Name of spot for initial data files.
	 */
	static public final String _initialDataFileSpotName = "__initial_data_file_spot";

	/**
	 * Name of role for initial data files.
	 */
	static public final String _initialDataFileRoleName = "__initial_data_file_role";

	/**
	 * Name of class for initial data files.
	 */
	static public final String _initialDataFileClassName = "csv_reader.CSVReader";

	/**
	 * Name of jar file for initial data files.
	 */
	static public final String _initialDataFileJarFileName = "csv_reader.jar";

	/**
	 * Name of method for initial data files.
	 */
	static public final String _initialDataFileClassMethodName = "ReadCSV";

	/**
	 * Name of class variable for initial data files.
	 */
	static public final String _initialDataFileClassVariableName = "__initial_data_file_class_variable";

	/**
	 * Name of file variable for initial data files.
	 */
	static public final String _initialDataFileVariableName = "__initial_data_file_variable";

	/**
	 * Name of class for initial data files.
	 */
	static public final String _exchangeAlgebraInitialDataFileClassName = "soars.library.auxiliary.CSVReader";

	/**
	 * Name of jar file for initial data files.
	 */
	static public final String _exchangeAlgebraInitialDataFileJarFileName = "auxiliary.jar";

	/**
	 * Name of method for initial data files.
	 */
	static public final String _exchangeAlgebraInitialDataFileClassMethodName = "read";

	/**
	 * Name of class variable for initial data files.
	 */
	static public final String _exchangeAlgebraInitialDataFileClassVariableName = "__exchange_algebra_initial_data_file_class_variable";

	/**
	 * Name of file variable for initial data files.
	 */
	static public final String _exchangeAlgebraInitialDataFileVariableName = "__exchange_algebra_initial_data_file_variable";

	/**
	 * Identifier string of GamingRunner data file.
	 */
	static public final String _soarsGamingRunnerFile = "VML_FILE";

	/**
	 * Identifier string of ModelBuilder data file.
	 */
	static public final String _soarsSorFile = "SOR_FILE";

	/**
	 * Root path of plugin file.
	 */
	static public final String _pluginRelativePath = "../function";

	/**
	 * Name of plugin definition file.
	 */
	static public final String _pluginSpringFilename = "beans.xml";

	/**
	 * Plugin identifier.
	 */
	static public final String _pluginSpringID = "plugin";

	/**
	 * Root path of Functional object files.
	 */
	static public String[] _functionalObjectDirectories = null;

	/**
	 * @return
	 */
	static public boolean initialize_functionalObjectDirectories() {
		// TODO Auto-generated method stub
		File userModuleDirectory = BasicEnvironment.get_instance().get_projectSubFoler( _userModuleDirectoryName);
		if ( null == userModuleDirectory)
			return false;

		_functionalObjectDirectories = new String[] {
			// この順序を変えてはいけない！グリッド上へjarファイルを転送出来なくなる
			_systemModuleDirectory,
			userModuleDirectory.getAbsolutePath().replaceAll( "\\\\", "/"),
			"../library/exalge",
			"../library/auxiliary"
		};

		return true;
	}

	/**
	 * User module directory name in dara.vml
	 */
	static public String _userModuleDirectoryNameSymbol = "|UserModuleDirectory|";

	/**
	 * Root of Functional object files path on Grid.
	 */
	static public final String _gridFunctionalObjectRootDirectory = "___library";

	/**
	 * Path for Functional object files on Grid.
	 */
	static public final String[] _gridFunctionalObjectDirectories = {
		// この順序を変えてはいけない！グリッド上へjarファイルを転送出来なくなる
		"/" + _gridFunctionalObjectRootDirectory + "/arbitrary",
		"/" + _gridFunctionalObjectRootDirectory + "/user",
		// TODO Auto-generated method stub
		"/" + _gridFunctionalObjectRootDirectory + "/exalge",
		"/" + _gridFunctionalObjectRootDirectory + "/auxiliary"
	};

	/**
	 * @param jarFilename
	 * @return
	 */
	public static int get_index_of_functionalObjectDirectories(String jarFilename) {
		// TODO Auto-generated method stub
		return get_index_of_functionalObjectDirectories( "", jarFilename);
	}

	/**
	 * @param prefix
	 * @param jarFilename
	 * @return
	 */
	public static int get_index_of_functionalObjectDirectories(String prefix, String jarFilename) {
		// TODO Auto-generated method stub
		for ( int i = 0; i < _functionalObjectDirectories.length; ++i) {
			if ( jarFilename.startsWith( prefix + _functionalObjectDirectories[ i]))
				return i;
		}
		return -1;
	}

	/**
	 * Path of program file which analyzes jar file.
	 */
	static public final String _jarfilesAnalyzerJarFilename = "../library/jarfile_analyzer/jarfiles_analyzer.jar";

	/**
	 * Tag name for java classes.
	 */
	static public final String _javaClasses = "Java classes";

	/**
	 * Maximum number of experiment.
	 */
	static public final int _maxNumberOfTimes = 10000;

	/**
	 * Reserved experiment name string.
	 */
	static public final String _experimentName = "__experiment_name";

	/**
	 * 
	 */
	static public final String _testDirectory = "/.soars/program/visualshell/test";

	/**
	 * Name of exchange algebra jar file name.
	 */
	static public final String _exchangeAlgebraJarFilename = "../library/exalge/Exalge2.jar";

	/**
	 * SOARS classURL script for exchange algebra.
	 */
	static public final String[] _exchangeAlgebraJar = {
		"../library/exalge/ExalgeUtil.jar",
		_exchangeAlgebraJarFilename,
		"../library/exalge/Dtalge.jar",
		"../library/exalge/aadlrt.jar",
		"../library/auxiliary/auxiliary.jar"
	};

	/**
	 * Name of exchange algebra class.
	 */
	static public final String _exchangeAlgebraClassname = "exalge2.Exalge";

	/**
	 * Name of exchange algebra instance factory class.
	 */
	static public final String _exchangeAlgebraFactoryClassname = "org.soars.exalge.util.ExalgeFactory";

	/**
	 * Name of exchange algebra instance factory class variable.
	 */
	static public final String _exchangeAlgebraFactoryClassVariableName = "__exalgeFactory";

	/**
	 * Name of exchange algebra utility class.
	 */
	static public final String _exchangeAlgebraMathClassname = "org.soars.exalge.util.ExalgeMath";

	/**
	 * Name of exchange algebra utility class variable.
	 */
	static public final String _exchangeAlgebraMathClassVariableName = "__exalgeMath";

	/**
	 * Name of exchange algebra utility class.
	 */
	static public final String _exchangeAlgebraUtilityClassname = "soars.library.auxiliary.ExchangeAlgebraUtility";

	/**
	 * Name of exchange algebra utility class variable.
	 */
	static public final String _exchangeAlgebraUtilityClassVariableName = "__exchangeAlgebraUtility";

	/**
	 * Name of class for AADLFunctions.
	 */
	static public final String _aadlFunctionsClassname = "ssac.aadl.runtime.AADLFunctions";

	/**
	 * Name of class variable for AADLFunctions.
	 */
	static public final String _aadlFunctionsClassVariableName = "__aadlFunctions";

	/**
	 * Name of csv file encoding for AADLFunctions.
	 */
	static public final String _aadlFunctionsDefaultCsvEncoding = "MS932";

	/**
	 * Name of method to set default csv file encoding for AADLFunctions.
	 */
	static public final String _aadlFunctionsSetDefaultCsvEncodingMethodName = "setDefaultCsvEncoding";

	/**
	 * Name of method to create instance of ExTransfer from the specified csv file.
	 */
	static public final String _aadlFunctionsNewExTransferFromCsvFileMethodName = "newExTransferFromCsvFile";

	/**
	 * Name of class for ExTransfer.
	 */
	static public final String _exTransferClassname = "exalge2.ExTransfer";

	/**
	 * Name of method to transfer.
	 */
	static public final String _exTransferTransferMethodName = "transfer";

	/**
	 * Path of program file which analyzes dbase file.
	 */
	static public final String _dbasefileAnalyzerJarFilename = "../library/gis/dbasefile_analyser.jar";

	/**
	 * Path of program file which analyzes shape file.
	 */
	static public final String _shapefileAnalyzerJarFilename = "../library/gis/shapefile_analyser.jar";

	/**
	 * Name of csv file encoding to analyze shape file.
	 */
	//static public final String _shapefileAnalyzerDefaultCsvEncoding = "MS932";

	/**
	 * Name of csv file encoding to analyze shape file.
	 */
	static public final String _gisRootDirectoryName = "gis";

	/**
	 * Name of csv file encoding to analyze shape file.
	 */
	static public final String _gisDataFilename = "data.xml";

	/**
	 * Name of csv file encoding to analyze shape file.
	 */
	static public final String _gisDataDirectoryName = "data";

	/**
	 * SOARS classURL script for rules.jar.
	 */
	static public final String _rulesJar = "../function/adapter/rules.jar";

	/**
	 * System rule script files relative path name.
	 */
	static public final String _systemRuleScriptsRelativePathName = "../resource/gui/program/visualshell/rule";

	/**
	 * SOARS classURL script for userrules.jar.
	 */
	static public final String _userrulesJar = "../library/adapter/userrules.jar";

	/**
	 * User rule spot name.
	 */
	static public final String _userRuleSpotName = "__userRuleSpot";

	/**
	 * User rule class variable name.
	 */
	static public final String _userRuleClassVariableName = "__userRuleReflection";

	/**
	 * User rule class name.
	 */
	static public final String _userRuleClassName = "soars.library.adapter.userrules.UserRuleReflection";

	/**
	 * User rule condition method name.
	 */
	static public final String _userRuleConditionMethodName = "execute_condition";

	/**
	 * User rule command method name.
	 */
	static public final String _userRuleCommandMethodName = "execute_command";

	/**
	 * User rule arguments variable name.
	 */
	static public final String _userRuleArgumentsListName = "__userRuleList";

	/**
	 * Names of all object types.
	 */
	static public final String[] _kinds = new String[] {
		"probability",
		"keyword",
		"number object",
		"role variable",
		"time variable",
		"spot variable",
		"class variable",
		"file",
		"exchange algebra",
		"extransfer",
		"map",
		"collection",
		"list"
	};

	/**
	 * Returns true if _kinds contains kind.
	 * @param kind object type
	 * @return true if _kinds contains kind
	 */
	static public boolean contains(String kind) {
		String[] kinds = new String[ _kinds.length];
		System.arraycopy( _kinds, 0, kinds, 0, _kinds.length);
		Arrays.sort( kinds);
		return ( 0 <= Arrays.binarySearch( kinds, kind));
	}

	/**
	 * Names of all exceptional object types.
	 */
	static public final String[] _exceptionalKinds = new String[] {
		"initial data file"
	};

	/**
	 * Names of all property panels.
	 */
	static public final String[] _propertyPanelBases = new String[] {
		"simple variable",
		"variable",
		"class variable",
		"file",
		"extransfer"
	};

	/**
	 * Names of all exceptional property panels.
	 */
	static public final String[] _exceptionalPropertyPanelBases = new String[] {
		"initial data file"
	};

	/**
	 * Returns the version message string.
	 * @return the version message string
	 */
	public static String get_version_message() {
		return ( SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._versionKey, "SOARS") + _lineSeparator
			+ "- VisualShell (" + _applicationVersion  + ")" + _lineSeparator + _lineSeparator
			+ SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._copyrightKey, "Copyright (c) 2003-???? SOARS Project."));
	}

	/**
	 * Returns true if the name is correct.
	 * @param name variable name
	 * @return true if the name is correct
	 */
	public static boolean is_correct_agent_or_spot_name(String name) {
		return ( !name.equals( "")
			&& !name.startsWith( "$")
			&& !name.equals( "__t")
			&& !name.matches( "__object_[0-9]+")
			&& !name.matches( "__variable_[0-9]+")
			&& !name.matches( "__value_[0-9]+")
			&& !name.equals( "__animator_image_")
			&& !name.equals( _spotVariableName)
			&& !name.equals( _currentTimeName)
			&& !name.equals( _chartGetValueClassVariableName)
			&& !name.equals( _exchangeAlgebraFactoryClassVariableName)
			&& !name.equals( _exchangeAlgebraMathClassVariableName)
			&& !name.equals( _aadlFunctionsClassVariableName)
			&& ( 0 > name.indexOf( _experimentName))
			&& !name.equals( _initialDataFileSpotName)
			&& !name.equals( _initialDataFileRoleName)
			&& !name.equals( _initialDataFileClassName)
			&& !name.equals( _initialDataFileClassVariableName)
			&& ( 0 > name.indexOf( _initialDataFileVariableName))
			&& !name.equals( _expressionSpotName)
			&& !name.equals( _userRuleSpotName)
			&& !name.equals( _userRuleClassVariableName)
			&& !name.equals( _userRuleArgumentsListName)
			&& ( 0 > name.indexOf( _expressionNemericVariablePrefix))
			&& !name.equals( _expressionResultNemericVariable1)
			&& !name.equals( _expressionResultNemericVariable2));
	}

	/**
	 * Returns true if the name is correct.
	 * @param name variable name
	 * @return true if the name is correct
	 */
	public static boolean is_correct_name(String name) {
		return ( !name.equals( "")
			&& ( 0 > name.indexOf( '$'))
			&& !name.equals( "__t")
			&& !name.matches( "__object_[0-9]+")
			&& !name.matches( "__variable_[0-9]+")
			&& !name.matches( "__value_[0-9]+")
			&& !name.equals( "__animator_image_")
			&& !name.equals( _spotVariableName)
			&& !name.equals( _currentTimeName)
			&& !name.equals( _chartGetValueClassVariableName)
			&& !name.equals( _exchangeAlgebraFactoryClassVariableName)
			&& !name.equals( _exchangeAlgebraMathClassVariableName)
			&& !name.equals( _aadlFunctionsClassVariableName)
			&& ( 0 > name.indexOf( _experimentName))
			&& !name.equals( _initialDataFileSpotName)
			&& !name.equals( _initialDataFileRoleName)
			&& !name.equals( _initialDataFileClassName)
			&& !name.equals( _initialDataFileClassVariableName)
			&& ( 0 > name.indexOf( _initialDataFileVariableName))
			&& !name.equals( _expressionSpotName)
			&& !name.equals( _userRuleSpotName)
			&& !name.equals( _userRuleClassVariableName)
			&& !name.equals( _userRuleArgumentsListName)
			&& ( 0 > name.indexOf( _expressionNemericVariablePrefix))
			&& !name.equals( _expressionResultNemericVariable1)
			&& !name.equals( _expressionResultNemericVariable2));
	}

	/**
	 * Returns the array of object kinds except the specified one.
	 * @param kind the specified kind
	 * @return the array of object kinds except the specified one.
	 */
	public static String[] get_kinds(String kind) {
		List<String> list = new ArrayList<String>();
		for ( int i = 0; i < _kinds.length; ++i) {
			if ( _kinds[ i].equals( kind))
				continue;

			list.add( _kinds[ i]);
		}

		return list.toArray( new String[ 0]);
	}

	/**
	 * Returns the array of property panels except the specified one.
	 * @param kind the specified property panel
	 * @return the array of property panels except the specified one.
	 */
	public static String[] get_propertyPanelBases(String kind) {
		List<String> list = new ArrayList<String>();
		for ( int i = 0; i < _propertyPanelBases.length; ++i) {
			if ( _propertyPanelBases[ i].equals( kind))
				continue;

			list.add( _propertyPanelBases[ i]);
		}

		return list.toArray( new String[ 0]);
	}

	/**
	 * Returns true if the keyword initial value is correct.
	 * @param value keyword initial value
	 * @return true if the value is correct
	 */
	public static boolean is_correct_keyword_initial_value(String value) {
		if ( null == value
			|| 0 < value.indexOf( '$')
			|| value.equals( "$")
			|| value.startsWith( " ")
			|| value.endsWith( " ")
			|| value.equals( "$Name")
			|| value.equals( "$Role")
			|| value.equals( "$Spot")
			|| 0 <= value.indexOf( Constant._experimentName))
			return false;

		if ( value.startsWith( "$")
			&& ( 0 <= value.indexOf( " ")
			|| 0 < value.indexOf( "$", 1)
			|| 0 < value.indexOf( ")", 1)))
			return false;

		return true;
	}

	/**
	 * Returns true if the number variable initial value is correct.
	 * @param type "integer" or "real number"
	 * @param value number variable initial value
	 * @return correct value
	 */
	public static String is_correct_number_variable_initial_value(String type, String value) {
		if ( value.equals( ""))
			return value;

		if ( null == value
			|| 0 < value.indexOf( '$')
			|| value.equals( "$")
			|| value.equals( "$Name")
			|| value.equals( "$Role")
			|| value.equals( "$Spot")
			|| 0 <= value.indexOf( Constant._experimentName))
			return null;

		if ( value.startsWith( "$")
			&& ( 0 <= value.indexOf( " ")
			|| 0 < value.indexOf( "$", 1)
			|| 0 < value.indexOf( ")", 1)))
			return null;

		return NumberObject.is_correct( value, type);
	}

	/**
	 * Returns true if the probability initial value is correct.
	 * @param value probability initial value
	 * @return correct value
	 */
	public static String is_correct_probability_initial_value(String value) {
		if ( value.equals( "$")
			|| 0 < value.indexOf( '$')
			|| value.equals( "$Name")
			|| value.equals( "$Role")
			|| value.equals( "$Spot")
			|| 0 <= value.indexOf( Constant._experimentName))
			return null;

		if ( value.startsWith( "$")
			&& 0 < value.indexOf( "$", 1)
			|| 0 < value.indexOf( ")", 1))
			return null;

		if ( !CommonTool.is_probability_correct( value))
			return null;

		return NumberObject.is_correct( value, "real number");
	}

	/**
	 * Returns true if the probability initial value is correct.
	 * @param value probability initial value
	 * @return correct value
	 */
	public static String is_correct_time_variable_initial_value(String value) {
		if ( value.startsWith( "$")) {
			if ( value.equals( "$")
				|| value.equals( "$Name")
				|| value.equals( "$Role")
				|| value.equals( "$Spot")
				|| 0 <= value.indexOf( Constant._experimentName)
				|| 0 <= value.indexOf( Constant._currentTimeName))
				return null;

			if ( 0 < value.indexOf( "$", 1)
				|| 0 < value.indexOf( ")", 1))
				return null;

			return value;
		} else {
			try {
				int number = Integer.parseInt( value);
				return String.valueOf( number);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				return null;
			}
		}
	}
}
