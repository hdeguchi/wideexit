package util;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * The Invoker class represents class variable.
 * @author H. Tanuma / SOARS project
 */
public class Invoker implements Cloneable, Serializable {

	private static final long serialVersionUID = 5603522760734389822L;
	protected Class<?> type = null;
	protected Object inst = null;
	protected ArrayList<Object> args = new ArrayList<Object>();
	protected ArrayList<Class<?>> types = new ArrayList<Class<?>>();

	/**
	 * Constructor for Invoker class.
	 * @param type type of class variable
	 */
	public Invoker(Class<?> type) {
		this.type = type;
		inst = null;
	}
	/**
	 * Constructor for Invoker class.
	 * @param instance instance for class variable
	 */
	public Invoker(Object instance) {
		setInstance(instance);
	}
	/**
	 * Set instance for class variable.
	 * @param instance instance for class variable
	 */
	public void setInstance(Object instance) {
		inst = instance;
		if (instance != null) {
			type = instance.getClass();
		}
	}
	/**
	 * Get instance for class variable.
	 * @return instance for class variable
	 */
	public Object getInstance() {
		return inst;
	}
	/**
	 * Add parameter for class variable.
	 * @param arg parameter
	 * @param type type of parameter
	 */
	public void addParameter(Object arg, Class<?> type) {
		args.add(arg);
		types.add(type);
	}
	/**
	 * Set type for last parameter.
	 * @param type type for last parameter
	 */
	public void setLastParameterType(Class<?> type) {
		types.set(types.size() - 1, type);
	}
	/**
	 * Create new instance for class variable.
	 * @return new instance for class variable
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public Object newInstance() throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		setInstance(type.getConstructor((Class[]) types.toArray(new Class[types.size()])).newInstance(args.toArray()));
		args.clear();
		types.clear();
		return inst;
	}
	/**
	 * Invoke method for class variable.
	 * @param name method name
	 * @return result of method invocation
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public Object invoke(String name) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Object ret = type.getMethod(name, (Class[]) types.toArray(new Class[types.size()])).invoke(inst, args.toArray());
		args.clear();
		types.clear();
		return ret;
	}
	/**
	 * Get field value of class variable.
	 * @param name field name
	 * @return field value
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	public Object getField(String name) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
		return type.getField(name).get(inst);
	}
	/**
	 * Set field value of class variable.
	 * @param name field name
	 * @param value field value
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	public void setField(String name, Object value) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
		type.getField(name).set(inst, value);
	}
	/**
	 * Set field value of class variable as last parameter.
	 * @param name field name
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	public void setField(String name) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
		type.getField(name).set(inst, args.get(args.size() - 1));
		args.remove(args.size() - 1);
		types.remove(types.size() - 1);
	}
	/**
	 * Create clone of the class variable.
	 * @return clone of the class variable
	 * @throws CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	/**
	 * Check if instance of class variables agree.
	 * @param obj other class variable
	 * @return true if instance of class variables agree
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Invoker) {
			obj = ((Invoker) obj).getInstance();
		}
		if (inst instanceof Invoker) {
			return false; // To avoid infinite recursion
		}
		return inst != null ? inst.equals(obj) : obj == null;
	}
	/**
	 * Get string expression for instance in class variable.
	 * @return string expression for instance in class variable
	 */
	public String toString() {
		return inst != null && inst != this ? inst.toString() : String.valueOf(type);
	}
}
