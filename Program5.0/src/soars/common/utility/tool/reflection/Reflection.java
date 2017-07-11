/*
 * 2005/01/14
 */
package soars.common.utility.tool.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author kurata
 */
public class Reflection {

	/**
	 * @param name
	 * @return
	 */
	public static Object get_class_instance(String name) {
		Class cls = null;
		try {
			cls = Class.forName( name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		if ( null == cls)
			return null;

		return get_class_instance( cls);
	}

	/**
	 * @param cls
	 * @return
	 */
	public static Object get_class_instance(Class cls) {
		Object object = null;
		try {
			object = cls.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}

		return object;
	}

	/**
	 * @param object
	 * @param name
	 * @param parameterTypes
	 * @param args
	 * @return
	 */
	public static boolean execute_class_method(Object object, String name, Class[] parameterTypes, Object[] args) {
		Method method = null;
		try {
			method = object.getClass().getMethod( name, parameterTypes);
		} catch (SecurityException e) {
			e.printStackTrace();
			return false;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return false;
		}

		if ( null == method)
			return false;

		try {
			method.invoke( object, args);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param object
	 * @param name
	 * @param parameterTypes
	 * @param args
	 * @param resultList
	 * @return
	 */
	public static boolean execute_class_method(Object object, String name, Class[] parameterTypes, Object[] args, List<Object> resultList) {
		Method method = null;
		try {
			method = object.getClass().getMethod( name, parameterTypes);
		} catch (SecurityException e) {
			e.printStackTrace();
			return false;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return false;
		}

		if ( null == method)
			return false;

		try {
			Object result = method.invoke( object, args);
			resultList.add( result);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param className
	 * @param methodName
	 * @param parameterTypes
	 * @param args
	 * @return
	 */
	public static boolean execute_static_method(String className, String methodName, Class[] parameterTypes, Object[] args) {
		Class cls = null;
		try {
			cls = Class.forName( className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		if ( null == cls)
			return false;

		return execute_static_method( cls, methodName, parameterTypes, args);
	}

	/**
	 * @param cls
	 * @param methodName
	 * @param parameterTypes
	 * @param args
	 * @return
	 */
	public static boolean execute_static_method(Class cls, String methodName, Class[] parameterTypes, Object[] args) {
		Method method = null;
		try {
			method = cls.getMethod( methodName, parameterTypes);
		} catch (SecurityException e) {
			e.printStackTrace();
			return false;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return false;
		}

		if ( null == method)
			return false;

		try {
			method.invoke( null, args);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param className
	 * @param methodName
	 * @param parameterTypes
	 * @param args
	 * @param resultList
	 * @return
	 */
	public static boolean execute_static_method(String className, String methodName, Class[] parameterTypes, Object[] args, List<Object> resultList) {
		Class cls = null;
		try {
			cls = Class.forName( className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		if ( null == cls)
			return false;

		return execute_static_method( cls, methodName, parameterTypes, args, resultList);
	}

	/**
	 * @param cls
	 * @param methodName
	 * @param parameterTypes
	 * @param args
	 * @param resultList
	 * @return
	 */
	public static boolean execute_static_method(Class cls, String methodName, Class[] parameterTypes, Object[] args, List<Object> resultList) {
		Method method = null;
		try {
			method = cls.getMethod( methodName, parameterTypes);
		} catch (SecurityException e) {
			e.printStackTrace();
			return false;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return false;
		}

		if ( null == method)
			return false;

		try {
			Object result = method.invoke( null, args);
			resultList.add( result);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
