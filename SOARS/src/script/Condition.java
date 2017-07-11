package script;

import java.io.Serializable;

/**
 * The Condition class represents compiled condition object.
 * @author H. Tanuma / SOARS project
 */
public abstract class Condition<T> implements Serializable {

	private static final long serialVersionUID = -2467773863231431614L;
	/**
	 * Evaluate compiled condition. 
	 * @param obj optional role parameter
	 * @return result of condition
	 * @throws Exception
	 */
	public abstract boolean is(T obj) throws Exception;

	/**
	 * Singleton instance represents true condition.
	 */
	public static final Condition<Object> TRUE =
		new Condition<Object>() {
			private static final long serialVersionUID = 9060077248791724849L;
			public boolean is(Object obj) throws Exception {
				return true;
			}
			public Command<Object> ifthen(Command<Object> rhs) {
				return rhs;
			}
			public Condition<Object> ifelse(Condition<Object> rhst, Condition<Object> rhsf) {
				return rhst;
			}
			public Command<Object> ifelse(Command<Object> rhst, Command<Object> rhsf) {
				return rhst;
			}
			public Condition<Object> oror(Condition<Object> rhs) {
				return TRUE;
			}
			public Condition<Object> andand(Condition<Object> rhs) {
				return rhs;
			}
			public Condition<Object> not() {
				return FALSE;
			}
			public Command<Object> command() {
				return Command.NOP;
			}
		};
	@SuppressWarnings("unchecked")
	public static <T> Condition<T> TRUE() {
		return (Condition<T>) TRUE;
	}
	/**
	 * Singleton instance represents false condition.
	 */
	public static final Condition<Object> FALSE =
		new Condition<Object>() {
			private static final long serialVersionUID = -2615198887022772301L;
			public boolean is(Object obj) throws Exception {
				return false;
			}
			public Command<Object> ifthen(Command<Object> rhs) {
				return Command.NOP;
			}
			public Condition<Object> ifelse(Condition<Object> rhst, Condition<Object> rhsf) {
				return rhsf;
			}
			public Command<Object> ifelse(Command<Object> rhst, Command<Object> rhsf) {
				return rhsf;
			}
			public Condition<Object> oror(Condition<Object> rhs) {
				return rhs;
			}
			public Condition<Object> andand(Condition<Object> rhs) {
				return FALSE;
			}
			public Condition<Object> not() {
				return TRUE;
			}
			public Command<Object> command() {
				return Command.NOP;
			}
		};
	@SuppressWarnings("unchecked")
	public static <T> Condition<T> FALSE() {
		return (Condition<T>) FALSE;
	}
	/**
	 * Compose condition and command.
	 * @param rhs command that executed if condition is satisfied
	 * @return composed command
	 */
	public Command<T> ifthen(final Command<T> rhs) {
		return new Command<T>() {
			private static final long serialVersionUID = 4147894184994345718L;
			public void invoke(T obj) throws Exception {
				if (Condition.this.is(obj)) rhs.invoke(obj);
			}
		};
	}
	/**
	 * Compose conditions to create new condition.
	 * @param rhst condition that evaluated if first condition is satisfied
	 * @param rhsf condition that evaluated if first condition is not satisfied
	 * @return composed condition
	 */
	public Condition<T> ifelse(final Condition<T> rhst, final Condition<T> rhsf) {
		return new Condition<T>() {
			private static final long serialVersionUID = 4912279970586350492L;
			public boolean is(T obj) throws Exception {
				return Condition.this.is(obj) ? rhst.is(obj) : rhsf.is(obj);
			}
		};
	}
	/**
	 * Compose condition and commands.
	 * @param rhst command that executed if first condition is satisfied
	 * @param rhsf command that executed if first condition is not satisfied
	 * @return composed command
	 */
	public Command<T> ifelse(final Command<T> rhst, final Command<T> rhsf) {
		return new Command<T>() {
			private static final long serialVersionUID = -7971253016000431503L;
			public void invoke(T obj) throws Exception {
				if (Condition.this.is(obj)) rhst.invoke(obj);
				else rhsf.invoke(obj);
			}
		};
	}
	/**
	 * Compose logical or'ed conditions in shortcut evaluation.
	 * @param rhs condition that evaluated if first condition is not satisfied
	 * @return composed condition
	 */
	public Condition<T> oror(final Condition<T> rhs) {
		return new Condition<T>() {
			private static final long serialVersionUID = 2514103291851631708L;
			public boolean is(T obj) throws Exception {
				return Condition.this.is(obj) || rhs.is(obj);
			}
		};
	}
	/**
	 * Compose logical or'ed conditions.
	 * @param rhs condition that evaluated later
	 * @return composed condition
	 */
	public Condition<T> or(final Condition<T> rhs) {
		return new Condition<T>() {
			private static final long serialVersionUID = -6866947486184536051L;
			public boolean is(T obj) throws Exception {
				return Condition.this.is(obj) | rhs.is(obj);
			}
		};
	}
	/**
	 * Compose logical and'ed conditions in shortcut evaluation.
	 * @param rhs condition that evaluated if first condition is satisfied
	 * @return composed condition
	 */
	public Condition<T> andand(final Condition<T> rhs) {
		return new Condition<T>() {
			private static final long serialVersionUID = -2947306408265121416L;
			public boolean is(T obj) throws Exception {
				return Condition.this.is(obj) && rhs.is(obj);
			}
		};
	}
	/**
	 * Compose logical and'ed conditions.
	 * @param rhs condition that evaluated later
	 * @return composed condition
	 */
	public Condition<T> and(final Condition<T> rhs) {
		return new Condition<T>() {
			private static final long serialVersionUID = 1077982343468374926L;
			public boolean is(T obj) throws Exception {
				return Condition.this.is(obj) & rhs.is(obj);
			}
		};
	}
	/**
	 * Create logical inverted condition.
	 * @return inverted condition
	 */
	public Condition<T> not() {
		return new Condition<T>() {
			private static final long serialVersionUID = 6192061817119991796L;
			public boolean is(T obj) throws Exception {
				return !Condition.this.is(obj);
			}
		};
	}
	/**
	 * Convert condition to command.
	 * @return converted command
	 */
	public Command<T> command() {
		return new Command<T>() {
			private static final long serialVersionUID = -1173544497378465654L;
			public void invoke(T obj) throws Exception {
				Condition.this.is(obj);
			}
		};
	}
}
