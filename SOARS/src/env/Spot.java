package env;

import java.util.List;

/**
 * The Spot class represents spot in SOARS.
 * @author H. Tanuma / SOARS project
 */
public class Spot extends EquippedObject {

	private static final long serialVersionUID = 7884147124493874361L;

	/**
	 * Get list of spots.
	 * @return list of spots
	 */
	public static List<Spot> getList() {
		return Environment.getCurrent().getSpots().getList();
	}
	/**
	 * Get spot by name.
	 * @param name name string of a spot
	 * @return spot with specified name or null
	 */
	public static Spot forName(String name) {
		return Environment.getCurrent().getSpots().forName(name);
	}
	/**
	 * Get spot by ID number.
	 * @param idNumber ID number in integer
	 * @return spot with specified ID or null
	 */
	public static Spot forId(int idNumber) {
		return Environment.getCurrent().getSpots().forId(idNumber);
	}
	/**
	 * Default constructor for the Spot class.
	 */
	public Spot() {
		super(Environment.getCurrent().getSpots());
	}
	/**
	 * Get spot self.
	 */
	public Spot getSpot() {
		return this;
	}
}
