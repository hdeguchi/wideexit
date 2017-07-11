package util;

import java.util.ArrayList;
import java.util.List;

import script.Command;
import script.CommandParser;
import script.Condition;

/**
 * The AskCommandParser class implements string message handler.
 * @author H. Tanuma / SOARS project
 */
public class AskCommandParser<T> extends CommandParser<T> {

	Object target;
	Object caller;
	Class<?> callerClass;

	/**
	 * Constructor for AskCommandParser class.
	 * @param target target object implements command methods.
	 * @param caller role instance
	 * @param callerClass role class
	 */
	public AskCommandParser(Object target, Object caller, Class<?> callerClass) {
		this.target = target;
		this.caller = caller;
		this.callerClass = callerClass;
	}
	SerializableMethod parseMethod(String term, List<Object> args) throws NoSuchMethodException {
		args.add(caller);
		List<Class<?>> types = new ArrayList<Class<?>>();
		types.add(callerClass);
		String[] pair;
		// Split Parameter String
		pair = term.split(" ", 2);
		if (pair.length == 2) {
			args.add(pair[1].trim());
			types.add(String.class);
			term = pair[0];
		}
		// Parse Method
		Class<?>[] argTypes = (Class[]) types.toArray(new Class[0]);
		return new SerializableMethod(target.getClass(), term, argTypes);
	}
	protected Command<T> parseCommandTerm(String term) throws Exception {
		List<Object> listArgs = new ArrayList<Object>();
		return makeCommand(parseMethod(term, listArgs), listArgs);
	}
	static <T> Command<T> makeCommand(final SerializableMethod method, List<?> listArgs) {
		final Object[] args = listArgs.toArray();
		return new Command<T>() {
			private static final long serialVersionUID = -6679912091119465288L;
			public void invoke(T obj) throws Exception {
				method.invoke(obj, args);
			}
		};
	}
	protected Condition<T> parseConditionTerm(String term) throws Exception {
		List<Object> listArgs = new ArrayList<Object>();
		return makeCondition(parseMethod(term, listArgs), listArgs);
	}
	static <T> Condition<T> makeCondition(final SerializableMethod method, List<?> listArgs) {
		final Object[] args = listArgs.toArray();
		return new Condition<T>() {
			private static final long serialVersionUID = -260017938693823707L;
			public boolean is(T obj) throws Exception {
				return method.<Boolean>invoke(obj, args).booleanValue();
			}
		};
	}
}
