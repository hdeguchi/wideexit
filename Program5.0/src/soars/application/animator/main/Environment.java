/*
 * 2005/01/31
 */
package soars.application.animator.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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
	 * Key mapped to the default directory for the file save.
	 */
	static public final String _saveAsDirectoryKey = "Directory.saveas";

	/**
	 * Key mapped to the default directory for the log files import.
	 */
	static public final String _importDirectoryKey = "Directory.import";

	/**
	 * Key mapped to the default directory for the graphics data file import.
	 */
	static public final String _graphicPropertyDirectoryKey = "Directory.graphic.property";

	/**
	 * Key mapped to the default directory for the image file set to the selected agents.
	 */
	static public final String _openAgentImageDirectoryKey = "Directory.agent.select.imagefile";

	/**
	 * Key mapped to the default directory for the image file set to the selected spots.
	 */
	static public final String _openSpotImageDirectoryKey = "Directory.spot.select.imagefile";

	/**
	 * Key mapped to the default directory for the image file set to the selected image objects.
	 */
	static public final String _openImageObjectImageDirectoryKey = "Directory.image.objet.select.imagefile";

	/**
	 * Key mapped to the position and size of the dialog box to edit the object.
	 */
	static public final String _editObjectDialogRectangleKey = "Edit.object.dialog.rectangle.";

	/**
	 * Key mapped to the position and size of the dialog box to edit the image object.
	 */
	static public final String _editImageObjectDialogRectangleKey = "Edit.image.object.dialog.rectangle.";

	/**
	 * Key mapped to the position and size of the dialog box to retrieve the agent property.
	 */
	static public final String _retrieveAgentPropertyDialogLocationKey = "Retrieve.agent.property.dialog.location.";

	/**
	 * Key mapped to the position and size of the dialog box to retrieve the spot property.
	 */
	static public final String _retrieveSpotPropertyDialogLocationKey = "Retrieve.spot.property.dialog.location.";

	/**
	 * Key mapped to the position and size of the dialog box which has the slider to set the position played.
	 */
	static public final String _animationSliderDialogLocationKey = "Animation.slider.dialog.location.";

	/**
	 * Key mapped to the position and size of the dialog box to edit the objects.
	 */
	static public final String _editObjectsDialogRectangleKey = "Edit.objects.dialog.rectangle.";

	/**
	 * Key mapped to the position and size of the dialog box to edit the object properties.
	 */
	static public final String _editPropertiesDialogRectangleKey = "Edit.properties.dialog.rectangle.";

	/**
	 * Key mapped to the flag on how to show the agents.
	 */
	public static final String _packAgentsKey = "Pack.agents";

	/**
	 * Key mapped to the flag whether the animation repeats.
	 */
	public static final String _repeatKey = "Repeat";

	/**
	 * Key mapped to the position and size of the dialog box to set the chart setting.
	 */
	public static String _chartDisplaySettingDialogRectangleKey = "Chart.display.setting.dialog.rectangle.";

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
	 * The startup routine.
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
				+ "animator" + File.separator
				+ "environment" + File.separator,
			"environment.properties",
			"SOARS Animator properties");
	}
}
