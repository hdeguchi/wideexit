package env;

public abstract class ObjectFacade extends NamedObject implements Context {

	private static final long serialVersionUID = 7604762210439526542L;

	abstract String getProp(String key);
	abstract String setProp(String key, String value);
	abstract Object setEquip(String key, Object obj);

	/**
	 * Constructor for ObjectFacade class.
	 * @param database database object
	 */
	public ObjectFacade(NamedObjectDB database) {
		super(database);
	}
	/**
	 * Get keyword value of agent or spot.
	 * @return keyword value
	 */
	public String getKeyword(String key) {
		return getProp(key);
	}
	/**
	 * Set keyword value of agent or spot.
	 */
	public void setKeyword(String key, String value) {
		setProp(key, value);
	}
	/**
	 * Get spot by name.
	 * @param name name string of a spot
	 * @return spot with specified name or null
	 */
	public static Spot getSpot(String name) {
		return SpotVal.forName(name).getSpot(null);
	}
	/**
	 * Get spot value of agent or spot.
	 * @return spot value
	 */
	public Spot getSpotVariable(String key) {
		return SpotVal.forName(key).getSpot(this);
	}
	/**
	 * Set spot value of agent or spot.
	 */
	public void setSpotVariable(String key, Spot value) {
		setEquip(key, value);
	}
}
