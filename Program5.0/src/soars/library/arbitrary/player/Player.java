/**
 * 
 */
package soars.library.arbitrary.player;

import javax.swing.JOptionPane;

import env.Agent;
import env.Spot;

/**
 * @author kurata
 *
 */
public class Player {

	/**
	 * @param agent
	 */
	public static void execute1(Agent agent) {
		JOptionPane.showMessageDialog( null,
			"Agent : " + agent.getName(), "SOARS", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * @param spot
	 */
	public static void execute2(Spot spot) {
		JOptionPane.showMessageDialog( null,
			"Spot : " + spot.getName(), "SOARS", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * @param agent
	 * @param spot
	 */
	public static void execute3(Agent agent, Spot spot) {
		JOptionPane.showMessageDialog( null,
			"Agent : " + agent.getName() + "\nSpot : " + spot.getName(), "SOARS", JOptionPane.INFORMATION_MESSAGE);
	}
}
