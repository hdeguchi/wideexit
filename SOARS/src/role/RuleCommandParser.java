package role;

import java.util.ArrayList;
import java.util.List;

import env.SpotVal;

import script.Command;
import script.CommandParser;
import script.Condition;
import util.SerializableMethod;

/**
 * The RuleCommandParser class implements rule command parser.
 * @author H. Tanuma / SOARS project
 */
public class RuleCommandParser<T> extends CommandParser<T> {

	Class<?> roleClass;

	/**
	 * Constructor for RuleCommandParser class.
	 * @param roleClass role class related to rule
	 */
	public RuleCommandParser(Class<?> roleClass) {
		this.roleClass = roleClass;
	}
	SerializableMethod parseMethod(String term, List<Object> args) throws NoSuchMethodException {
		List<Class<?>> types = new ArrayList<Class<?>>();
		String[] pair;
		// Parse Spot Prefix
		if (term.charAt(0) == '<') {
			pair = term.split(">", 2);
			if (pair.length != 2) {
				throw new RuntimeException("Spot Prefix Parse Error - " + term);
			}
			args.add(SpotVal.forName(pair[0].substring(1).trim()));
			types.add(SpotVal.class);
			term = pair[1].trim();
		}
		// Split Parameter String
		pair = term.split(" ", 2);
		if (pair.length == 2) {
			args.add(pair[1].trim());
			types.add(String.class);
			term = pair[0];
		}
		// Parse Method
		Class<?>[] argTypes = (Class[]) types.toArray(new Class[0]);
		return new SerializableMethod(roleClass, term, argTypes);
	}
	protected Command<T> parseCommandTerm(String term) throws Exception {
		List<Object> listArgs = new ArrayList<Object>();
		SerializableMethod method = parseMethod(term, listArgs);
		Class<?> returnType = method.getMethod().getReturnType();
		if (Command.class.isAssignableFrom(returnType)) {
			return method.<Command<T>>invoke(null, listArgs.toArray());
		}
		if (Condition.class.isAssignableFrom(returnType)) {
			return method.<Condition<T>>invoke(null, listArgs.toArray()).command();
		}
		return makeCommand(method, listArgs);
	}
	static <T> Command<T> makeCommand(final SerializableMethod method, List<?> listArgs) {
		final Object[] args = listArgs.toArray();
		return new Command<T>() {
			private static final long serialVersionUID = 8945243387252919424L;
			public void invoke(T obj) throws Exception {
				method.getMethod().invoke(obj, args);
			}
		};
	}
	protected Condition<T> parseConditionTerm(String term) throws Exception {
		List<Object> listArgs = new ArrayList<Object>();
		SerializableMethod method = parseMethod(term, listArgs);
		Class<?> returnType = method.getMethod().getReturnType();
		if (returnType == boolean.class || returnType == Boolean.class) {
			return makeCondition(method, listArgs);
		}
		if (Condition.class.isAssignableFrom(returnType)) {
			return method.<Condition<T>>invoke(null, listArgs.toArray());
		}
		throw new RuntimeException("Illegal Condition Method - " + term);
	}
	static <T> Condition<T> makeCondition(final SerializableMethod method, List<?> listArgs) {
		final Object[] args = listArgs.toArray();
		return new Condition<T>() {
			private static final long serialVersionUID = 8770795527206874458L;
			public boolean is(T obj) throws Exception {
				return method.<Boolean>invoke(obj, args).booleanValue();
			}
		};
	}
}
