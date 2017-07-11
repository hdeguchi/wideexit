/**
 * 
 */
package soars.common.soars.constant;

import java.io.File;

import soars.common.soars.module.Module;

/**
 * @author kurata
 *
 */
public class CommonConstant {

	/**
	 * 
	 */
	static public final String _lineSeparator = "\n";
	//static public final String _lineSeparator = System.getProperty( "line.separator");

	/**
	 * 
	 */
	static public final String _soarsHome = "SOARS_HOME";

	/**
	 * 
	 */
	static public final String _soarsProperty = "SOARS_PROPERTY";

	/**
	 * 
	 */
	static public final String _soarsProperties = "SOARS_PROPERTIES";

	/**
	 * 
	 */
	static public final String _soarsMemorySize = "SOARS_MEMORY_SIZE";

	/**
	 * 
	 */
	static public final String _soarsUserDataDirectory = "SOARS_USER_DATA_DIRECTORY";

	/**
	 * 
	 */
	static public final String _systemDefaultFileEncoding = "system.default.file.encoding";

	/**
	 * 
	 */
	static public final String _defaultMemorySize = "1024";

	/**
	 * 
	 */
	static public final String _windowsJava = "java";

	/**
	 * 
	 */
	static public final String _macJava = "java";

	/**
	 * 
	 */
	static public final String _javaHome = "JAVA_HOME";

	/**
	 * 
	 */
	static public final String _soarsLauncherJarFilename = "soarslauncher.jar";

	/**
	 * 
	 */
	static public final String _visualShellJarFilename = "visualshell.jar";

	/**
	 * 
	 */
	static public final String _visualShell4JarFilename = "visualshell4.jar";

	/**
	 * 
	 */
	static public final String _animatorJarFilename = "animator.jar";

	/**
	 * 
	 */
	static public final String _simulatorJarFilename = "simulator.jar";

	/**
	 * 
	 */
	static public final String _simulatorMainClassname = "soars.application.simulator.main.Application";

	/**
	 * 
	 */
	static public final String _soarsEngineJarFilename = "ModelBuilder.jar";

	/**
	 * 
	 */
	static public final String _soarsEngineConsoleMainClassname = "main.MainConsole";

	/**
	 * 
	 */
	static public final String _soarsEngineGuiMainClassname = "main.MainGUI";

	/**
	 * 
	 */
	static public final String _logExplicatorJarFilename = "function/org.soars.log/module/org.soars.log.jar";

	/**
	 * 
	 */
	static public final String _gamingBuilderJarFilename = "function/org.soars.gaming/org.soars.gaming.jar";
	//static public final String _gamingBuilderJarFilename = "org.soars.gaming.Plugin";

	/**
	 * 
	 */
	static public final String _gamingRunnerJarFilename = "function/org.soars.gaming/org.soars.gaming.runner.jar";

	/**
	 * 
	 */
	static public final String _aadlEditorJarFilename = "function/aadl.editor/AADLEditor.jar";

	/**
	 * 
	 */
	static public final String _applicationBuilderJarFilename = "function/org.soars.pack/org.soars.pack.jar";

	/**
	 * 
	 */
	static public final String _applicationBuilder4JarFilename = "function/org.soars.pack4/org.soars.pack.jar";

	/**
	 * 
	 */
	static public final String _applicationBuilderMainClassname = "org.soars.pack.app.SimpleBuilder";

	/**
	 * 
	 */
	static public final String _animationBuilderJarFilename = "animationbuilder.jar";

	/**
	 * 
	 */
	static public final String _animatorRunnerJarFilename = "run.jar";

	/**
	 * 
	 */
	static public final String _animatorRunnerHomeDirectory = "lib";

	/**
	 * 
	 */
	static public final String _animatorRunnerLauncherJarFilename = "launcher.jar";

	/**
	 * 
	 */
	static public final String _animatorRunnerInternalLauncherJarFilename = "internal.launcher.jar";

	/**
	 * 
	 */
	static public final String _animatorRunnerDataDirectory = "data";

	/**
	 * 
	 */
	static public final String _animatorRunnerDataFilename = "data.anm";

	/**
	 * 
	 */
	static public final String _animatorRunnerParameterFilename = "parameter.js";

	/**
	 * 
	 */
	static public final String _agentAccessExplicatorJarFilename = "function/org.soars.log/module/org.soars.card.jar";

	/**
	 * 
	 */
	static public final String _csvCutterJarFilename = "function/org.soars.log/module/org.soars.csv.jar";

	/**
	 * 
	 */
	static public final String _libraryManagerJarFilename = "librarymanager.jar";

	/**
	 * 
	 */
	static public final String _soarsManagerJarFilename = "modelmanager.jar";

	/**
	 * 
	 */
	static public final String _documentJarFilename = "function/document/doc_maker.jar";

	/**
	 * 
	 */
	static public final String _documentHtmlFilename = "doc/document.html";

	/**
	 * 
	 */
	static public final String _logViewerJarFilename = "logviewer.jar";

	/**
	 * Default extension of the SOARS data file.
	 */
	static public final String _soarsExtension = "soars";

	/**
	 * Name of the root directory for SOARS data.
	 */
	static public final String _soarsRootDirectoryName = "soars";

	/**
	 * Name of the VisualShell4 identify file.
	 */
	static public final String _visualShell4IdentifyFileName = "visualshell4";

	/**
	 * Name of the VisualShell zip file.
	 */
	static public final String _visualShellZipFileName = "visualshell.zip";

	/**
	 * Name of the root directory for VisualShell data.
	 */
	static public final String _visualShellRootDirectoryName = "visualshell";

	/**
	 * Name of the VisualShell data file.
	 */
	static public final String _visualShellDataFilename = "data.vml";

	/**
	 * Name of the root directory for Simulator data.
	 */
	static public final String _simulatorRootDirectoryName = "simulator";

	/**
	 * Name of the Simulator data file.
	 */
	static public final String _simulatorDataFilename = "data.sml";

	/**
	 * Name of the root directory for Animator data.
	 */
	static public final String _animatorRootDirectoryName = "animator";

	/**
	 * 
	 */
	static public final String _propertyFileName = "property.xml";

	/**
	 * 
	 */
	static public final boolean _enableSplashWindow = false;

	/**
	 * Name of the graphic properties file.
	 */
	static public final String _graphicPropertiesFilename = "graphic_properties.txt";

	/**
	 * Name of the chart properties file.
	 */
	static public final String _chartPropertiesFilename = "chart_properties.txt";

	/**
	 * Name of the chart log directory.
	 */
	static public final String _chartLogDirectory = "charts";

	/**
	 * Name of the image directory.
	 */
	static public final String _imageDirectory = "images";

	/**
	 * Name of the thumbnail image directory.
	 */
	static public final String _thumbnailImageDirectory = "thumbnails";

	/**
	 * Name of the user's data directory.
	 */
	static public final String _userDataDirectory = "userdata";

	/**
	 * Name of the gaming data directory.
	 */
	static public final String _gamingDataDirectory = "gaming";

	/**
	 * Name of the gaming runner data file.
	 */
	static public final String _gamingRunnerFile = "gamingrunner.xml";

	/**
	 * Temporary name of user data directory.
	 */
	static public final String _reservedUserDataDirectory = "__&|&|__";

	/**
	 * Default name of ModelBuilder data file.
	 */
	static public final String _soarsScriptFilename = "script.sor";

	/**
	 * Default name of user data zip file.
	 */
	static public final String _userDataZipFilename = "userdata.zip";

	/**
	 * System module directory name.
	 */
	static public final String _systemModuleDirectoryName = "arbitrary";

	/**
	 * User module directory name.
	 */
	static public final String _userModuleDirectoryName = "library";

	/**
	 * System module directory string.
	 */
	static public final String _systemModuleDirectory = "../library/" + _systemModuleDirectoryName;

	/**
	 * Module filename.
	 */
	static public final String _moduleSpringFilename = "module.mdf";

	/**
	 * Module ID.
	 */
	static public final String _moduleSpringID = "module";

	/**
	 * Module class.
	 */
	static public final String _moduleClass = Module.class.getName()/*"soars.common.soars.module.Module"*/;

	/**
	 * 
	 */
	public static final String _noDefinedModule = "No defined module";

	/**
	 * Model directory name.
	 */
	static public final String _modelDirectoryName = "model";

	/**
	 * System model directory string.
	 */
	static public final String _systemModelDirectory = "../resource/" + _modelDirectoryName;

	/**
	 * Module annotation file extension.
	 */
	static public final String _moduleAnnotationFileExtension = ".madf";

	/**
	 * 
	 */
	static public final String _defaultAdvancedMemorySetting = "false";

	/**
	 * Global spot name.
	 */
	static public final String _globalSpotName = "GlobalSpot";

	/**
	 * Global role name.
	 */
	static public final String _globalRoleName = "GlobalRole";

	/**
	 * VisualShell agent icon width.
	 */
	static public final int _visualShellAgentIconWidth = 20;

	/**
	 * VisualShell agent icon height.
	 */
	static public final int _visualShellAgentIconHeight = 20;

	/**
	 * VisualShell spot icon width.
	 */
	static public final int _visualShellSpotIconWidth = 30;

	/**
	 * VisualShell spot icon height.
	 */
	static public final int _visualShellSpotIconHeight = 30;

	/**
	 * VisualShell agent role icon width.
	 */
	static public final int _visualShellAgentRoleIconWidth = 20;

	/**
	 * VisualShell agent role icon height.
	 */
	static public final int _visualShellAgentRoleIconHeight = 10;

	/**
	 * VisualShell spot role icon width.
	 */
	static public final int _visualShellSpotRoleIconWidth = 20;

	/**
	 * VisualShell spot role icon height.
	 */
	static public final int _visualShellSpotRoleIconHeight = 10;

	/**
	 * VisualShell role connection length.
	 */
	static public final int _visualShellRoleConnectionLength = 15;

	/**
	 * VisualShell role connection radius.
	 */
	static public final int _visualShellRoleConnectionRadius = 4;

	/**
	 * User GUI internal relative path name.
	 */
	static public final String _userGuiInternalRelativePathName = "/gui";

	/**
	 * User rule internal relative path name.
	 */
	static public final String _userRuleInternalRelativePathName = "/gui/rule";

	/**
	 * VisualShell user rule script files internal relative path name.
	 */
	static public final String _userRuleScriptsInternalRelativePathName = "/gui/rule/script";

	/**
	 * VisualShell user rule jar files internal relative path name.
	 */
	static public final String _userRuleJarFilesInternalRelativePathName = "/gui/rule/library";

	/**
	 * VisualShell user rule external relative path name.
	 */
	static public final String _userRuleExternalRelativePathName = "/gui/program/visualshell/rule";

	/**
	 * VisualShell user rule script files external relative path name.
	 */
	static public final String _userRuleScriptsExternalRelativePathName = "/gui/program/visualshell/rule/script";

	/**
	 * VisualShell user rule jar files external relative path name.
	 */
	static public final String _userRuleJarFilesExternalRelativePathName = "/gui/program/visualshell/rule/library";

	/**
	 * Temporary name of user data directory.
	 */
	static public final String _reservedUserRuleJarFilesInternalDirectory = "__|&|&|__";

	/**
	 * User module directory name for docker.
	 */
	static public final String _userModuleDirectoryNameForDocker = "../library/user";

	/**
	 * Name of the user's data directory for docker.
	 */
	static public final String _userDataDirectoryForDocker = "../data/";

	/**
	 * 
	 */
	public static String[] _memorySizes = {
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
	 * Filter directory name.
	 */
	static public final String _filterDirectoryName = "filter";

	/**
	 * @param memorySize
	 * @return
	 */
	public static boolean contained(String memorySize) {
		for ( int i = 0; i < _memorySizes.length; ++i) {
			if ( _memorySizes[ i].equals( memorySize))
				return true;
		}
		return false;
	}

	/**
	 * 
	 */
	static public final String _macScreenMenuName = "dock:name";
	//static public final String _macScreenMenuName = "com.apple.mrj.application.apple.menu.about.name";

	/**
	 * 
	 */
	static public final String _macIconFilename = "dock:icon";

	/**
	 * 
	 */
	static public final String _macScreenMenu = "apple.laf.useScreenMenuBar";
	//static public final String _macScreenMenu = "com.apple.macos.useScreenMenuBar";

	/**
	 * 
	 */
	static public final String _xercesDirectory = "library/xerces";

	/**
	 * 
	 */
	static public String get_mac_java_command() {
		String macJava = null;
		File javahome = new File( System.getProperty( "java.home"));
		String osname = System.getProperty( "os.name");
		File command = new File( javahome, "bin/java");
		if ( command.exists())
			macJava = command.getAbsolutePath();
		else {
			command = new File( javahome, "Commands/java");
			if ( command.exists()) {
				macJava = command.getAbsolutePath();
			} else {
				macJava = _macJava;
			}
		}
		return macJava;
	}
}
