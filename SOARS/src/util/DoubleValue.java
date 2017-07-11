package util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import role.Role;

import env.Environment;
import env.EquippedObject;

/**
 * The DoubleValue class represents double variable.
 * @author H. Tanuma / SOARS project
 */
public class DoubleValue extends MutableNumber implements Comparable<Object>, Ownable, Cloneable, Askable<Role> {

	private static final long serialVersionUID = -298267574755513345L;
	protected EquippedObject owner = null;
	protected double value = 0;
	transient protected HashMap<String, Value> cache = null;
	static protected ThreadLocal<HashMap<String, Value>> current = new ThreadLocal<HashMap<String, Value>>() {
		protected HashMap<String, Value> initialValue() {
			return new HashMap<String, Value>();
		}
	};

	/**
	 * Set number value.
	 * @param value number value
	 */
	public void setNumber(Number value) {
		this.value = value.doubleValue();
	}
	/**
	 * Set double value.
	 * @param value double value
	 */
	public void setDouble(double value) {
		this.value = value;
	}
	/**
	 * Get number value.
	 * @return number value
	 */
	public Number numberValue() {
		return new Double(doubleValue());
	}
	/**
	 * Get double value.
	 * @return double value
	 */
	public double doubleValue() {
		return value;
	}
	/**
	 * Get float value.
	 * @return float value
	 */
	public float floatValue() {
		return (float) doubleValue();
	}
	/**
	 * Get int value.
	 * @return int value
	 */
	public int intValue() {
		return (int) doubleValue();
	}
	/**
	 * Get long value.
	 * @return long value
	 */
	public long longValue() {
		return (long) doubleValue();
	}
	/**
	 * Compare to other value.
	 * @param o other value
	 * @return result of comparation
	 */
	public int compareTo(Object o) {
		return Double.compare(value, ((DoubleValue) o).value);
	}
	/**
	 * Check equality of values.
	 * @param o other value
	 * @return true if two values agree
	 */
	public boolean equals(Object o) {
		return o instanceof DoubleValue ? compareTo(o) == 0 : false;
	}
	/**
	 * Get string expression of the value.
	 * @return string expression
	 */
	public String toString() {
		return Double.toString(value);
	}
	/**
	 * Set owner of the variable.
	 * @param owner of the variable
	 */
	public void setOwner(Object owner) {
		if (this.owner == null || owner == null) {
			this.owner = (EquippedObject) owner;
		}
	}
	/**
	 * Create clone of the variable.
	 * @return clone of the variable
	 * @throws CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	/**
	 * Evaluate assignment formula.
	 * @param caller role context of caller
	 * @param message assignment formula
	 * @return result of inequality
	 * @throws Exception
	 */
	public boolean ask(Role caller, Object message) throws Exception {
		if (cache == null) {
			cache = current.get();
		}
		String formula = message.toString();
		if (formula.charAt(0) == '=') {
			return doubleValue() == new Parser(formula.substring(1), owner).parse(cache);
		}
		if (formula.charAt(0) == '<') {
			return doubleValue() <= new Parser(formula.substring(1), owner).parse(cache);
		}
		if (formula.charAt(0) == '>') {
			return doubleValue() >= new Parser(formula.substring(1), owner).parse(cache);
		}
		value = new Parser(formula, owner).parse(cache);
		return true;
	}

	static abstract class Value {
		abstract double value(EquippedObject owner) throws Exception;
	}

	static class Parser {

		String formula;
		EquippedObject owner;
		StringTokenizer tokens;
		String token = null;

		Parser(String formula, EquippedObject owner) {
			this.formula = formula;
			this.owner = owner;
		}
		double parse() throws Exception {
			tokens = new StringTokenizer(formula, "+-*/%()", true);
			return sum(null).value(owner);
		}
		double parse(HashMap<String, Value> cache) throws Exception {
			Value value = (Value) cache.get(formula);
			if (value == null) {
				tokens = new StringTokenizer(formula, "+-*/%()", true);
				value = sum(null);
				cache.put(formula, value);
			}
			return value.value(owner);
		}
		void error() {
			String message = "Illegal Formula \"" + formula + '\"';			if (token != null) {
				message += " at \'" + token + '\'';
				while (tokens.hasMoreTokens()) {
					message += tokens.nextToken();
				}
			}
			throw new RuntimeException(message);
		}
		String nextToken() {
			if (token == null && tokens.hasMoreTokens()) {
				token = tokens.nextToken().trim();
				if (token.equals("")) {
					token = null;
					nextToken();
				}
			}
			return token;
		}
		Value sum(String check) throws Exception {
			Value value;
			if (nextToken().equals("-")) {
				token = null;
				final Value v = term();
				value = new Value() {
					double value(EquippedObject owner) throws Exception {
						return -v.value(owner);
					}
				};
			}
			else {
				value = term();
			}
			while (nextToken() != null) {
				if (token.equals("+")) {
					token = null;
					final Value lhs = value;
					final Value rhs = term();
					value = new Value() {
						double value(EquippedObject owner) throws Exception {
							return lhs.value(owner) + rhs.value(owner);
						}
					};
				}
				else if (token.equals("-")) {
					token = null;
					final Value lhs = value;
					final Value rhs = term();
					value = new Value() {
						double value(EquippedObject owner) throws Exception {
							return lhs.value(owner) - rhs.value(owner);
						}
					};
				}
				else if (token.equals(check)) {
					token = null;
					return value;
				}
				else {
					error();
				}
			}
			if (check != null) {
				error();
			}
			return value;
		}
		Value term() throws Exception {
			Value value = element();
			while (nextToken() != null) {
				if (token.equals("*")) {
					token = null;
					final Value lhs = value;
					final Value rhs = element();
					value = new Value() {
						double value(EquippedObject owner) throws Exception {
							return lhs.value(owner) * rhs.value(owner);
						}
					};
				}
				else if (token.equals("/")) {
					token = null;
					final Value lhs = value;
					final Value rhs = element();
					value = new Value() {
						double value(EquippedObject owner) throws Exception {
							return lhs.value(owner) / rhs.value(owner);
						}
					};
				}
				else if (token.equals("%")) {
					token = null;
					final Value lhs = value;
					final Value rhs = element();
					value = new Value() {
						double value(EquippedObject owner) throws Exception {
							return lhs.value(owner) % rhs.value(owner);
						}
					};
				}
				else {
					break;
				}
			}
			return value;
		}
		Value element() throws Exception {
			if (nextToken() == null) {
				error();
			}
			if (token.equals("(")) {
				token = null;
				return sum(")");
			}
			String name = token;
			Class<?> math = Math.class;
			token = null;
			if (nextToken() != null && token.equals("(")) {
				ArrayList<Value> params = new ArrayList<Value>();
				ArrayList<Class<?>> types = new ArrayList<Class<?>>();
				token = null;
				if (nextToken() == null) {
					error();
				}
				else if (token.equals(")")) {
					token = null;
					math = getClass();
				}
				else {
					params.add(sum(")"));
					types.add(double.class);
					while (nextToken() != null && token.equals("(")) {
						token = null;
						params.add(sum(")"));
						types.add(double.class);
					}
				}
				final Method m = math.getMethod(name, (Class[]) types.toArray(new Class[types.size()]));
				final Value[] v = (Value[]) params.toArray(new Value[params.size()]);
				final Object[] p = new Object[v.length];
				return new Value() {
					double value(EquippedObject owner) throws Exception {
						for (int i = 0; i < v.length; ++i) {
							p[i] = new Double(v[i].value(owner));
						}
						return ((Number) m.invoke(null, p)).doubleValue();
					}
				};
			}
			if (owner.getEquip(name) instanceof Number) {
				final String n = name;
				return new Value() {
					double value(EquippedObject owner) {
						return ((Number) owner.getEquip(n)).doubleValue();
					}
				};
			}
			try {
				final double v = Double.parseDouble(name);
				return new Value() {
					double value(EquippedObject owner) {
						return v;
					}
				};
			}
			catch (NumberFormatException e) {
				token = token == null ? name : name + token;
				error();
				return null;
			}
		}
		public static double E() {
			return Math.E;
		}
		public static double PI() {
			return Math.PI;
		}
		public static double random() {
			return Environment.getCurrent().getRandom().nextDouble();
		}
	}
}
