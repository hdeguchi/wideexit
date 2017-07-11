/**
 * 
 */
package soars.application.visualshell.file.importer.initial.stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import soars.application.visualshell.object.stage.Stage;
import soars.application.visualshell.object.stage.StageManager;

/**
 * The stage data hashtable(the stage name[String] - the instance of Stage class).
 * @author kurata / SOARS project
 */
public class StageDataMap extends HashMap<String, Stage> {

	/**
	 * 
	 */
	private String _type = "";

	/**
	 * Array of the Stages.
	 */
	public List<Stage> _stageDataList = new ArrayList<Stage>();

	/**
	 * Creates this object with the specified data. 
	 * @param type the type of stage
	 */
	public StageDataMap(String type) {
		super();
		_type = type;
	}

	/**
	 * Returns true if the Stage which has ths specified name exists.
	 * @param name ths specified name
	 * @return true if the Stage which has ths specified name exists
	 */
	public boolean contains(String name) {
		if ( null != get( name))
			return true;

		if ( StageManager.get_instance().contains( _type, name))
			return true;

		return false;
	}

	/**
	 * Returns true for appending the stage data successfully.
	 * @param words the array of words extracted from the specified line
	 * @param line the specified line
	 * @param number the line number
	 * @return true for appending the stage data successfully
	 */
	public boolean append(String[] words, String line, int number) {
		if ( null != get( words[ 1]))
			return true;

		Stage stage = new Stage( words[ 1], ( ( 2 < words.length) && ( null != words[ 2]) && words[ 2].equals( "true")));
		put( words[ 1], stage);
		_stageDataList.add( stage);
		return true;
	}
}
