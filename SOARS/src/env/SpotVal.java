package env;

import java.io.Serializable;

/**
 * The SpotVal class represents spot value reference.
 * @author H. Tanuma / SOARS project
 */
public abstract class SpotVal implements Serializable {

	private static final long serialVersionUID = -1653640032377104547L;

	/**
	 * Abstract method to retrieve entity value.
	 * @param obj optional role parameter
	 * @return entity value
	 */
	public abstract EquippedObject getEntity(Context context);

	/**
	 * method to retrieve spot value.
	 * @param obj optional role parameter
	 * @return spot value
	 */
	public Spot getSpot(Context context) {
		return (Spot) getEntity(context);
	}

	/**
	 * Resolve immediate entity name.
	 * @param name entity name
	 * @return entity value
	 */
	public static EquippedObject getEntity(String name) {
		EquippedObject self = Spot.forName(name);
		return (self != null) ? self : Agent.forName(name);
	}

	/**
	 * Resolve spot reference string.
	 * @param name spot reference string
	 * @return spot value reference
	 */
	public static SpotVal forName(String name) {
		final EquippedObject self = getEntity(name);
		if (self != null) {
			return new SpotVal() {
				private static final long serialVersionUID = 980835550261110498L;
				public EquippedObject getEntity(Context context) {
					return self;
				}
			};
		}
		if (name.equals("")) {
			return new SpotVal() {
				private static final long serialVersionUID = -3155825875165596211L;
				public EquippedObject getEntity(Context context) {
					return context.getSpot();
				}
			};
		}
		final String[] keys = name.split("\\:", -1);
		final EquippedObject top = getEntity(keys[0]);
		if (top != null) {
			return new SpotVal() {
				private static final long serialVersionUID = 6364460363145574186L;
				public EquippedObject getEntity(Context context) {
					EquippedObject self = top;
					for (int i = 1; i < keys.length; ++i) {
						self = self.<EquippedObject>getEquip(keys[i]);
						if (self == null) return null;
					}
					return self;
				}
			};
		}
		if (keys[0].equals("")) {
			return new SpotVal() {
				private static final long serialVersionUID = 6364460363145574186L;
				public EquippedObject getEntity(Context context) {
					EquippedObject self = context.getSpot();
					for (int i = 1; i < keys.length; ++i) {
						self = self.<EquippedObject>getEquip(keys[i]);
						if (self == null) return null;
					}
					return self;
				}
			};
		}
		return new SpotVal() {
			private static final long serialVersionUID = -7353255318762439535L;
			public EquippedObject getEntity(Context context) {
				EquippedObject self = context.getSelf();
				for (int i = 0; i < keys.length; ++i) {
					self = self.<EquippedObject>getEquip(keys[i]);
					if (self == null) return null;
				}
				return self;
			}
		};
	}
}
