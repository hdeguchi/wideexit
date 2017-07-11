/**
 * 
 */
package soars.library.jarfile_analyzer;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * @author kurata
 *
 */
public class JarfileParser {

	/**
	 * @param file
	 * @param urlClassLoader
	 * @param printStream
	 * @return
	 */
	public static boolean execute(File file, URLClassLoader urlClassLoader, PrintStream printStream) {
		JarFile jarFile = null;
		try {
			jarFile = new JarFile( file.getAbsolutePath());
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		}

		Enumeration enumeration = jarFile.entries();
		if ( null == enumeration)
			return false;

		printStream.println( "jarfile:" + file.getPath().replaceAll( "\\\\", "/"));

		while ( enumeration.hasMoreElements()) {
			ZipEntry zipEntry = ( ZipEntry)enumeration.nextElement();
			String name = zipEntry.getName();
			if ( !name.endsWith( ".class") || 0 <= name.indexOf( '$'))
				continue;

			name = name.substring( 0, name.length() - ".class".length());
			name = name.replaceAll( "/", ".");
			printStream.println( "class:" + name);
			if ( !parse_class( name, urlClassLoader, printStream))
				continue;
		}
		return true;
	}

	/**
	 * @param name
	 * @param urlClassLoader
	 * @param printStream
	 * @return
	 */
	private static boolean parse_class(String name, URLClassLoader urlClassLoader, PrintStream printStream) {
		Class cls = null;
		try {
			cls = urlClassLoader.loadClass( name);
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			//printStream.println( "\t- ClassNotFoundException!");
			//printStream.println( "");
			return false;
		} catch (Throwable e) {
			//e.printStackTrace();
			//printStream.println( "\t- Class error!");
			//printStream.println( "");
			return false;
		}

		if ( null == cls) {
			//printStream.println( "\t- Class error!");
			//printStream.println( "");
			return false;
		}

		if ( cls.isInterface())
			return false;

		Member[] constructors = null;
		try {
			constructors = cls.getConstructors();
		} catch (Throwable ex) {
			//printStream.println( "\t- Method error!");
			//printStream.println( "");
			constructors = null;
		}

		Member[] methods = null;
		try {
			methods = cls.getMethods();
		} catch (Throwable ex) {
			//printStream.println( "\t- Method error!");
			//printStream.println( "");
			methods = null;
		}

		List memberList = new ArrayList();

		if ( null != constructors && 0 < constructors.length) {
			for ( int i = 0; i < constructors.length; ++i) {
				if ( 0 <= constructors[ i].getName().indexOf( '$'))
					continue;

				memberList.add( constructors[ i]);
			}
		}

		if ( null != methods && 0 < methods.length) {
			for ( int i = 0; i < methods.length; ++i) {
				if ( 0 <= methods[ i].getName().indexOf( '$'))
					continue;

				memberList.add( methods[ i]);
			}
		}

		if ( memberList.isEmpty()) {
			//printStream.println( "\t- No method!");
			//printStream.println( "");
			return false;
		}

		for ( int i = 0; i < memberList.size(); ++i) {
			if ( memberList.get( i) instanceof Constructor) {
				Constructor constructor = ( Constructor)memberList.get( i);
				parse_method( constructor.getName(), null, constructor.getParameterTypes(), printStream);
			} else if ( memberList.get( i) instanceof Method) {
				Method method = ( Method)memberList.get( i);
				parse_method( method.getName(), method.getReturnType(), method.getParameterTypes(), printStream);
			}
		}

		return true;
	}

	/**
	 * @param name
	 * @param returnType
	 * @param parameterTypes
	 * @param printStream
	 * @return
	 */
	private static boolean parse_method(String name, Class returnType, Class[] parameterTypes, PrintStream printStream) {
		String text = ( "method:" + name);
		text += ( "return_type:" + ( ( null == returnType) ? "" : returnType.getName()));
		text += "parameter_type:";
		for ( int i = 0; i < parameterTypes.length; ++i)
			text += ( ( 0 == i ? "" : "\t") + parameterTypes[ i].getName());

		printStream.println( text);

		return true;
	}
}
