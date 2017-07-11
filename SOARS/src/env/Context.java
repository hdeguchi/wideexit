package env;

/**
 * The Context interface represents context assignment getter.
 * @author H. Tanuma / SOARS project
 */
public interface Context {

	/**
	 * Get self of the context.
	 */
	public EquippedObject getSelf();
	/**
	 * Get spot of the context.
	 */
	public Spot getSpot();
}
