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
import java.util.List;

import soars.common.soars.constant.CommonConstant;
import soars.common.soars.module.Module;

/**
 * @author kurata
 *
 */
public class JarfileAnalyzer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if ( 2 > args.length) {
			System.out.println( "error!");
			return;
		}

		execute( args[ 0], args, System.out);
//		try {
//			PrintStream printStream = new PrintStream( new File( "test.txt"));
//			execute( args[ 0], printStream);
//			printStream.flush();
//			printStream.close();
//		} catch (FileNotFoundException e) {
//			//e.printStackTrace();
//			return;
//		}
	}

	/**
	 * @param filename
	 * @param directories
	 * @param printStream
	 */
	private static void execute(String filename, String[] directories, PrintStream printStream) {
		File file = new File( filename);
		if ( file.isDirectory() || !file.isFile() || !file.getName().toLowerCase().endsWith( ".jar")) {
			printStream.println( "error!");
			return;
		}

		List<File> rootDirectories = get_rootDirectories( directories);
		if ( rootDirectories.isEmpty()) {
			printStream.println( "error!");
			return;
		}

		File baseDirectory = get_baseDirectory( file.getParentFile(), rootDirectories);

		URLClassLoader urlClassLoader = load( file, baseDirectory);
		if ( null == urlClassLoader) {
			printStream.println( "error!");
			return;
		}

		if ( !JarfileParser.execute( file, urlClassLoader, printStream)) {
			printStream.println( "error!");
			return;
		}
	}

	/**
	 * @param directories
	 * @return
	 */
	private static List<File> get_rootDirectories(String[] directories) {
		List<File> rootDirectories = new ArrayList<File>();
		for ( int i = 1; i < directories.length; ++i) {
			File rootDirectory = new File( directories[ i]);
			if ( null == rootDirectory || !rootDirectory.isDirectory())
				continue;

			rootDirectories.add( rootDirectory);
		}
		return rootDirectories;
	}

	/**
	 * @param directory
	 * @param rootDirectories
	 * @return
	 */
	private static File get_baseDirectory(File directory, List<File> rootDirectories) {
		if ( module_exists( directory))
			return directory;

		if ( rootDirectories.contains( directory))
			return null;

		return get_baseDirectory( directory.getParentFile(), rootDirectories);
	}

	/**
	 * @param directory
	 * @return
	 */
	private static boolean module_exists(File directory) {
		File[] files = directory.listFiles();
		for ( int i = 0; i < files.length; ++i) {
			if ( files[ i].isDirectory())
				continue;

			if ( !files[ i].isFile() || !files[ i].getName().equals( CommonConstant._moduleSpringFilename))
				continue;

			Module module = Module.get_module( files[ i]);
			if ( null == module || !module.isEnabled())
				continue;

			return true;
		}
		return false;
	}

	/**
	 * @param file
	 * @param baseDirectory 
	 * @return
	 */
	private static URLClassLoader load(File file, File baseDirectory) {
		List<URL> urlList = new ArrayList<URL>();

		if ( null == baseDirectory) {
			if ( !append_url( file, urlList))
				return null;
		} else {
			if ( !append_urls( baseDirectory, urlList))
				return null;
		}

		URL[] urls = urlList.toArray( new URL[ 0]);

		return new URLClassLoader( urls, ClassLoader.getSystemClassLoader().getParent());
	}

	/**
	 * @param parentDirectory
	 * @param urlList
	 * @return
	 */
	private static boolean append_urls(File parentDirectory, List<URL> urlList) {
		File[] files = parentDirectory.listFiles();
		for ( int i = 0; i < files.length; ++i) {
			if ( files[ i].isDirectory()) {
				if ( files[ i].getName().startsWith( "."))
					continue;

				if ( !append_urls( files[ i], urlList))
					return false;
			} else {
				if ( !files[ i].isFile() || !files[ i].getName().toLowerCase().endsWith( ".jar"))
					continue;

				if ( !append_url( files[ i], urlList))
					return false;
			}
		}
		return true;
	}

	/**
	 * @param file
	 * @param urlList
	 * @return
	 */
	private static boolean append_url(File file, List<URL> urlList) {
		try {
			URL url = new URL( "jar:file:" + file.getPath() + "!/");
			urlList.add( url);
		} catch (MalformedURLException e) {
			//e.printStackTrace();
			return false;
		}
		return true;
	}
}
