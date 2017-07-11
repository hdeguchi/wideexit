/**
 * 
 */
package soars.library.jarfile_analyzer;

import java.io.File;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import soars.common.soars.constant.CommonConstant;
import soars.common.soars.module.Module;

/**
 * @author kurata
 *
 */
public class JarfilesAnalyzer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		execute( args, System.out);
//		try {
//			PrintStream printStream = new PrintStream( new File( "test.txt"));
//			execute( args, printStream);
//			printStream.flush();
//			printStream.close();
//		} catch (FileNotFoundException e) {
//			//e.printStackTrace();
//			return;
//		}
	}

	/**
	 * @param directories
	 * @param printStream
	 */
	private static void execute(String[] directories, PrintStream printStream) {
		if ( 0 == directories.length) {
			printStream.println( "error!");
			return;
		}

		Map<String, Map<String, List<File>>> map = make( directories/*, printStream*/);
		if ( null == map) {
			printStream.println( "error!");
			return;
		}

		URLClassLoader urlClassLoader = load( directories, map);
		if ( null == urlClassLoader) {
			printStream.println( "error!");
			return;
		}

		if ( !parse( directories, map, urlClassLoader, printStream)) {
			printStream.println( "error!");
			return;
		}
	}

	/**
	 * @param directories
	 * @return
	 */
	private static Map<String, Map<String, List<File>>> make(String[] directories/*, PrintStream printStream*/) {
		Map<String, Map<String, List<File>>> map = new HashMap<String, Map<String, List<File>>>();
		for ( int i = 0; i < directories.length; ++i) {
			File directory = new File( directories[ i]);
			if ( null == directory || !directory.isDirectory())
				continue;

			Map<String, List<File>> filesMap = new HashMap<String, List<File>>();
			make( directory, null, filesMap/*, printStream*/);
			map.put( directories[ i], filesMap);
		}
		return map;
	}

	/**
	 * @param directory
	 * @param module
	 * @param filesMap
	 * @return
	 */
	private static boolean make(File directory, String module, Map<String, List<File>> filesMap/*, PrintStream printStream*/) {
		if ( null == module)
			module = get_module( directory);
		else {
			List<File> list = filesMap.get( module);
			if ( null == list) {
				list = new ArrayList<File>();
				filesMap.put( module, list);
			}

			list.add( directory);
		}

		File[] files = directory.listFiles();
		for ( int i = 0; i < files.length; ++i) {
			if ( files[ i].isDirectory()) {
				if ( files[ i].getName().startsWith( "."))
					continue;

				if ( !make( files[ i], module, filesMap/*, printStream*/))
					return true;
			} else {
				if ( !files[ i].isFile() || !files[ i].getName().toLowerCase().endsWith( ".jar"))
					continue;

				List<File> list = filesMap.get( ( null == module) ? CommonConstant._noDefinedModule : module);
				if ( null == list) {
					list = new ArrayList<File>();
					filesMap.put( ( null == module) ? CommonConstant._noDefinedModule : module, list);
				}

				//printStream.println( "module: " + ( ( null == module) ? CommonConstant._no_defined_module : module));
				//printStream.println( "jarfile: " + files[ i].getPath() + "\n");
				list.add( files[ i]);
			}
		}
		return true;
	}

	/**
	 * @param directory
	 * @return
	 */
	private static String get_module(File directory) {
		File[] files = directory.listFiles();
		for ( int i = 0; i < files.length; ++i) {
			if ( files[ i].isDirectory())
				continue;

			if ( !files[ i].isFile() || !files[ i].getName().equals( CommonConstant._moduleSpringFilename))
				continue;

			Module module = Module.get_module( files[ i]);
			if ( null == module || !module.isEnabled())
				continue;

			return files[ i].getPath().replaceAll( "\\\\", "/");
		}
		return null;
	}

	/**
	 * @param directories
	 * @param map
	 * @return
	 */
	private static URLClassLoader load(String[] directories, Map<String, Map<String, List<File>>> map) {
		List<URL> urlList = new ArrayList<URL>();

		for ( int i = 0; i < directories.length; ++i) {
			Map<String, List<File>> filesMap = map.get( directories[ i]);
			if ( !load( urlList, filesMap))
				return null;
		}

		URL[] urls = urlList.toArray( new URL[ 0]);

		return new URLClassLoader( urls, ClassLoader.getSystemClassLoader().getParent());
	}

	/**
	 * @param filesMap
	 * @return
	 */
	private static boolean load(List<URL> urlList, Map<String, List<File>> filesMap) {
		Collection<List<File>> list = filesMap.values();
		Iterator<List<File>> iterator = list.iterator();
		while ( iterator.hasNext()) {
			List<File> files = iterator.next();
			for ( int i = 0; i < files.size(); ++i) {
				File file = files.get( i);
				if ( file.isDirectory())
					continue;

				try {
					URL url = new URL( "jar:file:" + file.getPath() + "!/");
					urlList.add( url);
				} catch (MalformedURLException e) {
					//e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @param directories
	 * @param map
	 * @param urlClassLoader
	 * @param printStream
	 * @return
	 */
	private static boolean parse(String[] directories, Map<String, Map<String, List<File>>> map, URLClassLoader urlClassLoader, PrintStream printStream) {
		for ( int i = 0; i < directories.length; ++i) {
			printStream.println( "root:" + directories[ i]);
			Map<String, List<File>> filesMap = map.get( directories[ i]);
			if ( !parse( filesMap, urlClassLoader, printStream))
				return false;
		}
		return true;
	}

	/**
	 * @param filesMap
	 * @param urlClassLoader
	 * @param printStream
	 * @return
	 */
	private static boolean parse(Map<String, List<File>> filesMap, URLClassLoader urlClassLoader, PrintStream printStream) {
		Set<String> modules = filesMap.keySet();
		Iterator<String> iterator = modules.iterator();
		while ( iterator.hasNext()) {
			String module = iterator.next();
			printStream.println( "module:" + module);
			List<File> files = filesMap.get( module);
			for ( int i = 0; i < files.size(); ++i) {
				File file = files.get( i);
				if ( file.isDirectory())
					printStream.println( "folder:" + file.getPath().replaceAll( "\\\\", "/"));
				else {
					if ( !JarfileParser.execute( file, urlClassLoader, printStream))
						return false;
				}
			}
		}
		return true;
	}
}
