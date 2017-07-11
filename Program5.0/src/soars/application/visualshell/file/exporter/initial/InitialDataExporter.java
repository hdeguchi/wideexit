/*
 * Created on 2005/11/28
 */
package soars.application.visualshell.file.exporter.initial;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Vector;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.base.DrawObject;
import soars.application.visualshell.object.base.DrawObjectIdComparator;
import soars.application.visualshell.object.comment.CommentManager;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.scripts.OtherScriptsManager;
import soars.application.visualshell.object.simulation.SimulationManager;
import soars.application.visualshell.object.stage.StageManager;
import soars.common.utility.tool.sort.QuickSort;

/**
 * Exports the initial data to the file in CSV format.
 * @author kurata / SOARS project
 */
public class InitialDataExporter {

	/**
	 * Returns true for exporting the initial data to the specified file in CSV format.
	 * @param file the specified file
	 * @param all true if all data is exported
	 * @return true for exporting the initial data to the specified file in CSV format
	 */
	public static boolean execute(File file, boolean all) {
		String script = get_script( all);

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
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

//	/**
//	 * @param all
//	 */
//	public static void execute(boolean all) {
//		String script = get_script( all);
//		Clipboard.set( script);
//	}

	/**
	 * @param all 
	 * @return
	 */
	private static String get_script(boolean all) {
		String script = Constant._initialDataIdentifier + Constant._lineSeparator + Constant._lineSeparator;
		if ( all)
			script += StageManager.get_instance().get_initial_data();

		script += get_agent_initial_data();
		script += get_spot_initial_data();

		if ( all) {
			script += get_agent_role_initial_data();
			script += get_spot_role_initial_data();
			script += SimulationManager.get_instance().get_initial_data();
			script += VisualShellExpressionManager.get_instance().get_initial_data();
			script += OtherScriptsManager.get_instance().get_initial_data();
			script += CommentManager.get_instance().get_initial_data();
		}

		return script;
	}

	/**
	 * @return
	 */
	private static String get_agent_initial_data() {
		Vector<DrawObject> agents = new Vector<DrawObject>();
		LayerManager.get_instance().get_agents( agents);
		if ( agents.isEmpty())
			return "";

		return get_initial_data( agents);
	}

	/**
	 * @return
	 */
	private static String get_spot_initial_data() {
		Vector<DrawObject> spots = new Vector<DrawObject>();
		LayerManager.get_instance().get_spots( spots);
		if ( spots.isEmpty())
			return "";

		return get_initial_data( spots);
	}

	/**
	 * @return
	 */
	private static String get_agent_role_initial_data() {
		Vector<DrawObject> agentRoles = new Vector<DrawObject>();
		LayerManager.get_instance().get_agent_roles( agentRoles);
		if ( agentRoles.isEmpty())
			return "";

		return get_initial_data( agentRoles);
	}

	/**
	 * @return
	 */
	private static String get_spot_role_initial_data() {
		Vector<DrawObject> spotRoles = new Vector<DrawObject>();
		LayerManager.get_instance().get_spot_roles( spotRoles);
		if ( spotRoles.isEmpty())
			return "";

		return get_initial_data( spotRoles);
	}

	/**
	 * @param objects
	 * @return
	 */
	private static String get_initial_data(Vector<DrawObject> objects) {
		String script = "";

		DrawObject[] drawObjects = ( DrawObject[])objects.toArray( new DrawObject[ 0]);
		if ( 1 < drawObjects.length)
			QuickSort.sort( drawObjects, new DrawObjectIdComparator());

		for ( int i = 0; i < drawObjects.length; ++i)
			script += drawObjects[ i].get_initial_data();

		return script;
	}
}
