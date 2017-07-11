package role;

import env.Agent;
import env.Context;
import env.EquippedObject;
import env.Spot;
import env.SpotVal;

/**
 * The RoleFacade abstact class represents role facade interface.
 * @author H. Tanuma / SOARS project
 */
public abstract class RoleFacade implements Context {

	/**
	 * Get agent for the name.
	 * @return agent
	 */
	public static Agent getAgent(String name) {
		return Agent.forName(name);
	}
	/**
	 * Get spot for the name.
	 * @return spot
	 */
	public Spot getSpot(String name) {
		return SpotVal.forName(name).getSpot(this);
	}
	/**
	 * Get entity by name.
	 * @param name name string of a entity
	 * @return entity with specified name or null
	 */
	public static EquippedObject getEntity(Context self, String name) {
		return SpotVal.forName(name).getEntity(self);
	}
	/**
	 * Get keyword value of agent or spot.
	 * @return keyword value
	 */
	public static String getKeyword(EquippedObject self, String key) {
		return self.getKeyword(key);
	}
	/**
	 * Get keyword value of agent or spot.
	 * @return keyword value
	 */
	public String getKeyword(String key) {
		return getKeyword(getSelf(), key);
	}
	/**
	 * Set keyword value of agent or spot.
	 */
	public static void setKeyword(EquippedObject self, String key, String value) {
		self.setKeyword(key, value);
	}
	/**
	 * Set keyword value of agent or spot.
	 */
	public void setKeyword(String key, String value) {
		setKeyword(getSelf(), key, value);
	}
}
