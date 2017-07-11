package soars.application.visualshell.file.importer.initial.simulation;

import java.util.HashMap;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.simulation.SimulationManager;

/**
 * The simulation data hashtable(type[String] - the simulation data[String]).
 * @author kurata / SOARS project
 */
public class SimulationDataMap extends HashMap<String, String[]> {

	/**
	 * Creates this object.
	 */
	public SimulationDataMap() {
		super();
	}

	/**
	 * Returns true for appending the simulation data successfully.
	 * @param words the array of words extracted from the specified line
	 * @param line the specified line
	 * @param number the line number
	 * @return true for appending the simulation data successfully
	 */
	public boolean append(String[] words, String line, int number) {
		if ( words[ 1].equals( ResourceManager.get_instance().get( "initial.data.simulation.export.end.time")))
			put( words[ 1], new String[] { words[ 2].equals( "true") ? "true" : "false"});
//		else if ( words[ 1].equals( ResourceManager.get_instance().get( "initial.data.simulation.export.log.step.time")))
//			put( words[ 1], new String[] { words[ 2].equals( "true") ? "true" : "false"});
		else if ( words[ 1].equals( ResourceManager.get_instance().get( "initial.data.simulation.start.time"))
			|| words[ 1].equals( ResourceManager.get_instance().get( "initial.data.simulation.step.time"))
			|| words[ 1].equals( ResourceManager.get_instance().get( "initial.data.simulation.end.time"))
			|| words[ 1].equals( ResourceManager.get_instance().get( "initial.data.simulation.log.step.time")))
			return get_time( words, line, number);
		else if ( words[ 1].equals( ResourceManager.get_instance().get( "initial.data.simulation.random.seed")))
			return get_random_seed( words, line, number);
		else
			return false;

		return true;
	}

	/**
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean get_time(String[] words, String line, int number) {
		if ( 5 > words.length)
			return false;

		try {
			int day = Integer.parseInt( words[ 2]);
			words[ 2] = String.valueOf( day);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}

		try {
			int value = Integer.parseInt( words[ 3]);
			if ( 0 > value || 23 < value)
				return false;

			words[ 3] = ( ( ( 10 > value) ? "0" : "") + String.valueOf( value));
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}

		try {
			int value = Integer.parseInt( words[ 4]);
			if ( 0 > value || 59 < value)
				return false;

			words[ 4] = ( ( ( 10 > value) ? "0" : "") + String.valueOf( value));
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}

		put( words[ 1], new String[] { words[ 2], words[ 3], words[ 4]});

		return true;
	}

	/**
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean get_random_seed(String[] words, String line, int number) {
		if ( !SimulationManager.get_instance().is_correct_random_seed( words[ 2]))
			return false;

		put( words[ 1], new String[] { words[ 2]});
		return true;
	}
}
