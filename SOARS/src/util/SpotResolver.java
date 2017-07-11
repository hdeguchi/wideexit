package util;

import env.Spot;

/**
 * The SpotResolver class represents resolver assigned to spot.
 * @author H. Tanuma / SOARS project
 */
public abstract class SpotResolver extends Resolver implements Ownable {

	private static final long serialVersionUID = 7190101423840069882L;
	Spot spot = null;

	public void setOwner(Object owner) {
		if (spot == null || owner == null) {
			spot = (Spot) owner;
		}
	}
	public Spot getSpot() {
		return spot;
	}
}
