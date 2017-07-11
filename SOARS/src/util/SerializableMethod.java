package util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The SerializableMethod class represents serializable method object.
 * @author H. Tanuma / SOARS project
 */
public class SerializableMethod implements Serializable {

	private static final long serialVersionUID = -8423931841589469573L;
	transient Method method;
	Class<?> target;
	String name;
	Class<?>[] argTypes;

	public SerializableMethod(Class<?> target, String name, Class<?>[] argTypes) throws SecurityException, NoSuchMethodException {
		this.target = target;
		this.name = name;
		this.argTypes = argTypes;
		setMethod();
	}
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException, SecurityException, NoSuchMethodException {
		in.defaultReadObject();
		setMethod();
	}
	void setMethod() throws SecurityException, NoSuchMethodException{
		method = target.getMethod(name, argTypes);
		try {
			method.setAccessible(true);
		}
		catch (SecurityException e) {
		}
	}
	public Method getMethod() {
		return method;
	}
	@SuppressWarnings("unchecked")
	public <T> T invoke(Object obj, Object... args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		return (T) method.invoke(obj, args);
	}
}
