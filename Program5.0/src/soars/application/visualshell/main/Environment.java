/*
 * 2005/01/31
 */
package soars.application.visualshell.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import soars.common.utility.tool.environment.EnvironmentBase;

/**
 * The local properties maintenance class.
 * @author kurata / SOARS project
 */
public class Environment extends EnvironmentBase {

	/**
	 * Key mapped to the position and size of the main window.
	 */
	static public final String _mainWindowRectangleKey = "MainWindow.window.rectangle.";

	/**
	 * Key mapped to the default directory for the file load.
	 */
	static public final String _openDirectoryKey = "Directory.open";

	/**
	 * Key mapped to the default directory for the image file load.
	 */
	static public final String _openImageDirectoryKey = "Directory.image.open";

	/**
	 * Key mapped to the default directory for the file save.
	 */
	static public final String _saveAsDirectoryKey = "Directory.saveas";

	/**
	 * Key mapped to the default directory for the script file import.
	 */
	static public final String _importDirectoryKey = "Directory.import";

	/**
	 * Key mapped to the default directory for the initial data file import.
	 */
	static public final String _importInitialDataDirectoryKey = "Directory.import.initial.data";

	/**
	 * Key mapped to the flag which indicates whether to import the all initial data.
	 */
	static public final String _importInitialDataAllKey = "Import.initial.data.all";

	/**
	 * Key mapped to the default directory for the initial data file export.
	 */
	static public final String _exportDirectoryKey = "Directory.export";

	/**
	 * Key mapped to the default directory for the experiment data table file export.
	 */
	static public final String _exportTableDirectoryKey = "Directory.export.table";

	/**
	 * Key mapped to the default directory for the experiment data table file import.
	 */
	static public final String _importTableDirectoryKey = "Directory.import.table";

	/**
	 * 
	 */
	static public final String _dockerFilesetCreatorFolderKey = "Docker.fileset.creator.folder";

	/**
	 * 
	 */
	static public final String _dockerMemorySizeKey = "Docker.memory.size";

	/**
	 * 
	 */
	static public final String _dockerLogToFileKey = "Docker.log.to.file";

	/**
	 * 
	 */
	static public final String _dockerFilesetCreatorSpecificDockerImageNameKey = "Experiment.docker.fileset.creator.specific.docker.image.name";

	/**
	 * 
	 */
	static public final String _dockerFilesetCreatorDockerImageNameKey = "Experiment.docker.fileset.creator.docker.image.name";

	/**
	 * 
	 */
	static public final String _dockerFilesetCreatorSpecificUserKey = "Experiment.docker.fileset.creator.specific.user";

	/**
	 * 
	 */
	static public final String _dockerFilesetCreatorUserIdKey = "Experiment.docker.fileset.creator.user.id";

	/**
	 * 
	 */
	static public final String _dockerFilesetCreatorUsernameKey = "Experiment.docker.fileset.creator.username";

	/**
	 * 
	 */
	static public final String _dockerFilesetCreatorPasswordKey = "Experiment.docker.fileset.creator.password";

	/**
	 * Key mapped to the default directory for the GIS data file open.
	 */
	static public final String _openGisDataDirectoryKey = "Directory.open.gis.data";

	/**
	 * Key mapped to the default directory for the GIS data file import.
	 */
	static public final String _saveAsGisDataDirectoryKey = "Directory.saveas.gis.data";

	/**
	 * Key mapped to the default directory for the GIS data file import.
	 */
	static public final String _importGisDataDirectoryKey = "Directory.import.gis.data";

	/**
	 * Key mapped to the position and size of the dialog to edit the directory for the GIS data file import.
	 */
	static public final String _importGisDataDialogRectangleKey = "Import.gis.data.dialog.rectangle.";

	/**
	 * Key mapped to the position and size of the frame window to edit GIS data import conditions.
	 */
	static public final String _gisDataFrameRectangleKey = "Edit.gis.data.frame.rectangle.";

	/**
	 * Key mapped to the divider position of the frame window to edit GIS data import conditions.
	 */
	static public final String _gisDataFrameDividerLocationKey1 = "Edit.gis.data.frame.divider.location1";

	/**
	 * Key mapped to the divider position of the frame window to edit GIS data import conditions.
	 */
	static public final String _gisDataFrameDividerLocationKey2 = "Edit.gis.data.frame.divider.location2";

	/**
	 * Key mapped to the position and size of the frame window to select GIS data fields.
	 */
	static public final String _selectGisDataFieldsDialogRectangleKey = "Select.gis.data.fields.dialog.rectangle.";

	/**
	 * Key mapped to the default directory for the files export.
	 */
	static public final String _exportFilesDirectoryKey = "Directory.export.files";

	/**
	 * Key mapped to the position and size of the dialog box to edit the stages.
	 */
	static public final String _editStageDialogRectangleKey = "Edit.stage.dialog.rectangle.";

	/**
	 * Key mapped to the position and size of the dialog box to edit the agent or spot object.
	 */
	static public final String _editObjectDialogRectangleKey = "Edit.object.dialog.rectangle.";

	/**
	 * Key mapped to the divider position of the dialog box to edit the agent or spot object.
	 */
	static public final String _editObjectDialogDividerLocationKey = "Edit.object.dialog.divider.location";

	/**
	 * Key mapped to the divider position of the property page to edit the class variables.
	 */
	static public String _editObjectDialogClassVariablePropertyPanelDividerLocationKey= "Edit.object.dialog.class.property.page.divider.location";

	/**
	 * Key mapped to the divider position of the class manager.
	 */
	static public String _editObjectDialogClassManagerDividerLocationKey = "Edit.object.dialog.class.manager.divider.location";

	/**
	 * Key mapped to the divider position of the property page to edit the file variables.
	 */
	static public final String _editObjectDialogFilePropertyPanelDividerLocationKey = "Edit.object.dialog.file.property.page.divider.location";

	/**
	 * Key mapped to the divider position of the file manager.
	 */
	static public final String _editObjectDialogFileManagerDividerLocationKey = "Edit.object.dialog.file.manager.divider.location";

	/**
	 * Key mapped to the divider position of the property page to edit the file variables.
	 */
	static public final String _editObjectDialogInitialDataFilePropertyPanelDividerLocationKey = "Edit.object.dialog.initial.data.file.property.page.divider.location";

	/**
	 * Key mapped to the divider position of the file manager.
	 */
	static public final String _editObjectDialogInitialDataFileManagerDividerLocationKey = "Edit.object.dialog.initial.data.file.manager.divider.location";

	/**
	 * Key mapped to the divider position of the property page to edit the file variables.
	 */
	static public final String _editObjectDialogExTransferPropertyPanelDividerLocationKey = "Edit.object.dialog.extransfer.property.page.divider.location";

	/**
	 * Key mapped to the divider position of the file manager.
	 */
	static public final String _editObjectDialogExTransferFileManagerDividerLocationKey = "Edit.object.dialog.extransfer.file.manager.divider.location";

	/**
	 * Key mapped to the position and size of the file editor window.
	 */
	static public final String _fileEditorWindowRectangleKey = "File.editor.rectangle.";

	/**
	 * Key mapped to the position and size of the dialog box to edit the role object.
	 */
	static public final String _editRoleDialogRectangleKey = "Edit.role.dialog.rectangle.";

	/**
	 * Key mapped to the position and size of the status frame to show the SOARS objects.
	 */
	public static String _statusFrameRectangleKey = "Status.frame.rectangle.";

	/**
	 * Key mapped to the minimum size of the rule table column.
	 */
	static public final String _editRuleTableColumnMinimumSizeKey = "Edit.role.table.column.minimum.size";

	/**
	 * Key mapped to the position and size of the dialog box to edit the rules.
	 */
	static public final String _editRuleDialogRectangleKey = "Edit.rule.dialog.rectangle.";

	/**
	 * Key mapped to the divider position of the dialog box to edit the rules.
	 */
	static public final String _editRuleDialogDividerLocationKey1 = "Edit.rule.dialog.divider.location1";

	/**
	 * Key mapped to the divider position of the dialog box to edit the rules.
	 */
	static public final String _editRuleDialogDividerLocationKey2 = "Edit.rule.dialog.divider.location2";

	/**
	 * Key mapped to the position and size of the dialog box to edit the chart object.
	 */
	static public final String _editChartObjectDialogRectangleKey = "Edit.chart.object.dialog.rectangle.";

	/**
	 * Key mapped to the position and size of the dialog box to edit the simulation settings.
	 */
	static public final String _editSettingDialogRectangleKey = "Edit.setting.dialog.rectangle.";

	/**
	 *  Key mapped to the position and size of the dialog box to edit the expression data.
	 */
	static public final String _editExpressionDialogRectangleKey= "Edit.expression.dialog.rectangle.";

	/**
	 * Key mapped to the position and size of the dialog box to edit the experiment data.
	 */
	static public final String _editExperimentDialogRectangleKey = "Edit.experiment.dialog.rectangle.";

	/**
	 * Key mapped to the position and size of the dialog box to edit the application settings.
	 */
	static public final String _editApplicationSettingDialogRectangleKey = "Edit.application.setting.dialog.rectangle.";

	/**
	 * Key mapped to the position and size of the dialog box to show the warning messages.
	 */
	static public final String _warningDialogRectangleKey1 = "Warning.dialog1.rectangle.";

	/**
	 * Key mapped to the position and size of the dialog box to show the warning messages.
	 */
	static public final String _warningDialogRectangleKey2 = "Warning.dialog2.rectangle.";

	/**
	 * Key mapped to the position and size of the dialog box to show the warning messages.
	 */
	static public final String _warningDialogRectangleKey3 = "Warning.dialog3.rectangle.";

	/**
	 * Key mapped to the position and size of the dialog box to show the ModelBuilder logs.
	 */
	static public final String _monitorWindowRectangleKey = "MonitorWindow.window.rectangle.";

	/**
	 * 
	 */
	static public final String _editExportSettingDialogToFileKey = "Edit.export.setting.dialog.to.file";

	/**
	 * 
	 */
	static public final String _editExportSettingDialogKeepUserDataFileKey = "Edit.export.setting.dialog.keep.user.data.file";

	/**
	 * Key mapped to the flag which indicates whether to enable the plugin.
	 */
	static public final String _enablePluginsKey = "Enable.plugins";

	/**
	 * Key mapped to the flag which indicates whether to enable the functional objects.
	 */
	static public final String _enableFunctionalObjectKey = "Enable.functional.object";

	/**
	 * Key mapped to the flag which indicates whether to enable the use of exchange algebra.
	 */
	static public final String _enableExchangeAlgebraKey = "Enable.exchange.algebra";

	/**
	 * Key mapped to the flag which indicates whether to enable the use of initial data importer/exporter.
	 */
	static public final String _enableInitialDataKey = "Enable.initial.data";

	/**
	 * Key mapped to the flag which indicates whether to enable the use of gis data importer.
	 */
	static public final String _enableGisDataKey = "Enable.gis.data";

	/**
	 * Key mapped to the character code of GIS shape file.
	 */
	static public final String _gisShapefileAnalyzerCharacterCode = "GIS.shapefile.analyzer.character.code";

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private Environment _environment = null;

	/**
	 * Key mapped to the default flag which indicates whether to enable the functional objects.
	 */
	static public final String _enableFunctionalObjectDefaultValue = "true";

	/**
	 * 
	 */
	private boolean _enableFunctionalObject = _enableFunctionalObjectDefaultValue.equals( "true");

	/**
	 * Key mapped to the default flag which indicates whether to enable the use of exchange algebra.
	 */
	static public final String _enableExchangeAlgebraDefaultValue = "false";

	/**
	 * 
	 */
	private boolean _enableExchangeAlgebra = _enableExchangeAlgebraDefaultValue.equals( "true");

	/**
	 * Key mapped to the default flag which indicates whether to enable the use of initial data importer/exporter.
	 */
	static public final String _enableInitialDataDefaultValue = "false";

	/**
	 * 
	 */
	private boolean _enableInitialData = _enableInitialDataDefaultValue.equals( "true");

	/**
	 * Key mapped to the default flag which indicates whether to enable the use of gis data importer.
	 */
	static public final String _enableGisDataDefaultValue = "false";

	/**
	 * 
	 */
	private boolean _enableGisData = _enableGisDataDefaultValue.equals( "true");

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
	private static void startup() throws FileNotFoundException, IOException {
		synchronized( _lock) {
			if ( null == _environment) {
				_environment = new Environment();
				if ( !_environment.initialize())
					System.exit( 0);
			}
		}
	}

	/**
	 * Returns the instance of this class.
	 * @return the instance of this class
	 */
	public static Environment get_instance() {
		if ( null == _environment) {
			System.exit( 0);
		}

		return _environment;
	}

	/**
	 * Creates the local properties maintenance class.
	 */
	public Environment() {
		super(
			System.getProperty( Constant._soarsProperty) + File.separator
				+ "program" + File.separator
				+ "visualshell" + File.separator
				+ "environment" + File.separator,
			"environment.properties",
			"SOARS Visual Shell properties");
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.tool.environment.EnvironmentBase#initialize()
	 */
	public boolean initialize() throws FileNotFoundException, IOException {
		if ( !super.initialize())
			return false;

		_enableFunctionalObject = get( _enableFunctionalObjectKey, _enableFunctionalObjectDefaultValue).equals( "true");
		_enableExchangeAlgebra = get( _enableExchangeAlgebraKey, _enableExchangeAlgebraDefaultValue).equals( "true");
		_enableInitialData = get( _enableInitialDataKey, _enableInitialDataDefaultValue).equals( "true");
		_enableGisData = get( _enableGisDataKey, _enableGisDataDefaultValue).equals( "true");

		return true;
	}

	/**
	 * Returns whether or not the functional objects are enabled.
	 * @return whether or not the functional objects are enabled
	 */
	public boolean is_functional_object_enable() {
		return _enableFunctionalObject;
	}

	/**
	 * Enables the functional objects.
	 */
	public void enable_functional_object() {
		set( _enableFunctionalObjectKey, "true");
		_enableFunctionalObject = true;
	}

	/**
	 * Returns whether or not the exchange algebra are enabled.
	 * @return whether or not the exchange algebra are enabled
	 */
	public boolean is_exchange_algebra_enable() {
		return _enableExchangeAlgebra;
	}

	/**
	 * @return
	 */
	public boolean is_extransfer_enable() {
		// TODO とりあえず
		return is_exchange_algebra_enable();
		//return false;
	}

	/**
	 * Returns whether or not the initial data are enabled.
	 * @return whether or not the initial data are enabled
	 */
	public boolean is_initial_data_enable() {
		return _enableInitialData;
	}

	/**
	 * @return
	 */
	public boolean is_gis_enable() {
		// TODO とりあえず
		return _enableGisData;
	}

	/**
	 * Appends the specified plugin.
	 * @param pluginName the name of the specified plugin
	 */
	public void append_enable_plugin(String pluginName) {
		String value = get( _enablePluginsKey, "");

		if ( value.equals( "")) {
			set( _enablePluginsKey, pluginName);
			return;
		}

		String[] enablePluginNames = value.split( ";");
		if ( null == enablePluginNames || 0 == enablePluginNames.length) {
			set( _enablePluginsKey, pluginName);
			return;
		}

		List<String> enablePlugins = new ArrayList( Arrays.asList( enablePluginNames));
		if ( enablePlugins.contains( pluginName))
			return;

		value += ( ";" + pluginName);
		set( _enablePluginsKey, value);
	}
}
