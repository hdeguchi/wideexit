/**
 * 
 */
package soars.library.arbitrary.test001.player;

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
	public static void execute(Agent agent) {
		JOptionPane.showMessageDialog( null,
			"Agent : " + agent.getName(), "SOARS", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * @param spot
	 */
	public static void execute(Spot spot) {
		JOptionPane.showMessageDialog( null,
			"Spot : " + spot.getName(), "SOARS", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * @param agent
	 * @param spot
	 */
	public static void execute(Agent agent, Spot spot) {
		JOptionPane.showMessageDialog( null,
			"Agent : " + agent.getName() + "\nSpot : " + spot.getName(), "SOARS", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * @param agent_name
	 */
	public static void execute1(String agent_name) {
		Agent agent = Agent.forName( agent_name);
		JOptionPane.showMessageDialog( null,
			"Agent : " + agent.getName(), "SOARS", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * @param spot_name
	 */
	public static void execute2(String spot_name) {
		Spot spot = Spot.forName( spot_name);
		JOptionPane.showMessageDialog( null,
			"Spot : " + spot.getName(), "SOARS", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * @param agent_name
	 * @param spot_name
	 */
	public static void execute3(String agent_name, String spot_name) {
		Agent agent = Agent.forName( agent_name);
		Spot spot = Spot.forName( spot_name);
		JOptionPane.showMessageDialog( null,
			"Agent : " + agent.getName() + "\nSpot : " + spot.getName(), "SOARS", JOptionPane.INFORMATION_MESSAGE);
	}
}
