package script;

/**
 * The CommandParser class implements command script parser.
 * @author H. Tanuma / SOARS project
 */
public abstract class CommandParser<T> {

	protected abstract Command<T> parseCommandTerm(String term) throws Exception;
	protected abstract Condition<T> parseConditionTerm(String term) throws Exception;

	/**
	 * Parse command script.
	 * @param script command script
	 * @return compiled command
	 * @throws Exception
	 */
	public Command<T> parseCommand(String script) throws Exception {
		String[] terms = script.split("; ", 2);
		if (terms.length == 2) {
			return parseCommand(terms[0]).pair(parseCommand(terms[1]));
		}
		terms = script.split(" \\? ", -1);
		if (terms.length > 1) {
			for (int i = 1; i < terms.length - 1; ++i) {
				terms[0] += (" ? " + terms[i]);
			}
			Condition<T> lhs = parseCondition(terms[0]);
			terms = terms[terms.length - 1].split(" : ", 2);
			if (terms.length == 2) {
				return lhs.ifelse(parseCommand(terms[0]), parseCommand(terms[1]));
			}
			return lhs.ifthen(parseCommand(terms[0]));
		}
		terms = script.split(", ", 2);
		if (terms.length == 2) {
			return parseCommand(terms[0]).pair(parseCommand(terms[1]));
		}
		return parseCommandTerm(script.trim());
	}

	/**
	 * Parse condition script.
	 * @param script condition script
	 * @return compiled condition
	 * @throws Exception
	 */
	public Condition<T> parseCondition(String script) throws Exception {
		String[] terms = script.split("; ", 2);
		if (terms.length == 2) {
			return parseCommand(terms[0]).pair(parseCondition(terms[1]));
		}
		terms = script.split(" \\? ", -1);
		if (terms.length > 1) {
			for (int i = 1; i < terms.length - 1; ++i) {
				terms[0] += (" ? " + terms[i]);
			}
			Condition<T> lhs = parseCondition(terms[0]);
			terms = terms[terms.length - 1].split(" : ", 2);
			if (terms.length == 2) {
				return lhs.ifelse(parseCondition(terms[0]), parseCondition(terms[1]));
			}
			return lhs.andand(parseCondition(terms[0]));
		}
		terms = script.split(" \\|\\| ", 2);
		if (terms.length == 2) {
			return parseCondition(terms[0]).oror(parseCondition(terms[1]));
		}
		terms = script.split(" && ", 2);
		if (terms.length == 2) {
			return parseCondition(terms[0]).andand(parseCondition(terms[1]));
		}
		terms = script.split(" \\| ", 2);
		if (terms.length == 2) {
			return parseCondition(terms[0]).or(parseCondition(terms[1]));
		}
		terms = script.split(" & ", 2);
		if (terms.length == 2) {
			return parseCondition(terms[0]).and(parseCondition(terms[1]));
		}
		terms = script.split(", ", 2);
		if (terms.length == 2) {
			return parseCommand(terms[0]).pair(parseCondition(terms[1]));
		}
		script = script.trim();
		if (script.charAt(0) == '!') {
			return parseCondition(script.substring(1)).not();
		}
		if (script.charAt(0) == '~') {
			return parseCommandTerm(script.substring(1).trim()).condition();
		}
		return parseConditionTerm(script);
	}
}
