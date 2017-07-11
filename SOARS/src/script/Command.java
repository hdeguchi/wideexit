package script;

import java.io.Serializable;

/**
 * The Command class represents compiled command object.
 * @author H. Tanuma / SOARS project
 */
public abstract class Command<T> implements Serializable {

	private static final long serialVersionUID = -8580763560458960015L;
	/**
	 * Invoke compiled command.
	 * @param obj optional role parameter
	 * @throws Exception
	 */
	public abstract void invoke(T obj) throws Exception;

	/**
	 * Singleton instance represents no operation.
	 */
	public static final Command<Object> NOP =
		new Command<Object>() {
			private static final long serialVersionUID = -716759997296757070L;
			public void invoke(Object obj) throws Exception {
			}
			public Command<Object> pair(Command<Object> rhs) {
				return rhs;
			}
			public Condition<Object> pair(Condition<Object> rhs) {
				return rhs;
			}
			public Condition<Object> condition() {
				return Condition.TRUE;
			}
		};
	/**
	 * Compose two commands.
	 * @param rhs command that executed later
	 * @return composed command
	 */
	public Command<T> pair(final Command<T> rhs) {
		return new Command<T>() {
			private static final long serialVersionUID = 5487681710496562122L;
			public void invoke(T obj) throws Exception {
				Command.this.invoke(obj);
				rhs.invoke(obj);
			}
		};
	}
	/**
	 * Compose command and condition.
	 * @param rhs condition that checked later
	 * @return composed condition
	 */
	public Condition<T> pair(final Condition<T> rhs) {
		return new Condition<T>() {
			private static final long serialVersionUID = -4543486335478388675L;
			public boolean is(T obj) throws Exception {
				Command.this.invoke(obj);
				return rhs.is(obj);
			}
		};
	}
	/**
	 * Convert command to condition which always returns true.
	 * @return converted condition
	 */
	public Condition<T> condition() {
		return new Condition<T>() {
			private static final long serialVersionUID = -490044329013138693L;
			public boolean is(T obj) throws Exception {
				Command.this.invoke(obj);
				return true;
			}
		};
	}
}
