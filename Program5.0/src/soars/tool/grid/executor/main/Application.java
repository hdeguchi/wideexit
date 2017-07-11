/**
 * 
 */
package soars.tool.grid.executor.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.stream.StreamPumper;

/**
 * @author kurata
 *
 */
public class Application {

	/**
	 * @param args
	 * @return
	 */
	private static String[] get_cmdarray(String[] args) {
		List< String>list = new ArrayList< String>();
		if ( args[ 0].equals( "simulation") && 7 <= args.length) {
			list.add( args[ 4] + "/bin/java");
			list.add( "-Dfile.encoding=UTF-8");
			if ( 8 <= args.length) {
				list.add( "-D" + Constant._soarsUserDataDirectory + "=" + args[ 7]);
			}
			if ( !args[ 5].equals( "0")) {
				list.add( "-D" + Constant._soarsMemorySize + "=" + args[ 5]);
				list.add( "-Xmx" + args[ 5] + "m");
			}
			list.add( "-cp");
			list.add( args[ 1] + Constant._grid_model_builder_jar_filename);
			list.add( Constant._soarsEngineConsoleMainClassname);
			list.add( args[ 6]);

			System.out.println( "type : " + args[ 0]);
			System.out.println( "id : " + args[ 3]);
			System.out.println( "script : " + args[ 6] + "\n");

		} else if ( args[ 0].equals( "analysis") && 8 <= args.length) {
			list.add( args[ 4] + "/bin/java");
			if ( !args[ 5].equals( "0")) {
				list.add( "-Xmx" + args[ 5] + "m");
			}
			list.add( "-jar");
			list.add( args[ 1] + Constant._grid_log_analyzer_jar_filename);
			list.add( args[ 6]);
			list.add( args[ 7]);

			System.out.println( "type : " + args[ 0]);
			System.out.println( "id : " + args[ 3]);
			System.out.println( "log directory : " + args[ 7] + "\n");

		} else if ( args[ 0].equals( "compression") && 9 <= args.length) {
			list.add( args[ 4] + "/bin/java");
			list.add( "-Dfile.encoding=UTF-8");
			if ( !args[ 5].equals( "0")) {
				list.add( "-Xmx" + args[ 5] + "m");
			}
			list.add( "-jar");
			list.add( args[ 1] + Constant._grid_compressor_jar_filename);
			list.add( args[ 6]);
			list.add( args[ 7]);
			list.add( args[ 8]);

			System.out.println( "type : " + args[ 0]);
			System.out.println( "id : " + args[ 3]);
			System.out.println( "compression\n");

		} else if ( args[ 0].equals( "decompression") && 8 <= args.length) {
			list.add( args[ 4] + "/bin/java");
			if ( !args[ 5].equals( "0")) {
				list.add( "-Xmx" + args[ 5] + "m");
			}
			list.add( "-jar");
			list.add( args[ 1] + Constant._grid_decompressor_jar_filename);
			list.add( args[ 6]);
			list.add( args[ 7]);

			System.out.println( "type : " + args[ 0]);
			System.out.println( "id : " + args[ 3]);
			System.out.println( "decompression\n");

		} else
			return null;

		return list.toArray( new String[ 0]);
	}

	/**
	 * @param cmdarray
	 * @param filename
	 * @param id 
	 */
	private static void execute(String[] cmdarray, String filename, String id) {
		try {
			Process process = ( Process)Runtime.getRuntime().exec( cmdarray);
			new StreamPumper( process.getErrorStream(), System.err).start();
			new StreamPumper( process.getInputStream(), System.out).start();
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
				output( filename, id, "error:3");
				return;
			}
			output( filename, id, "success!");
		} catch (IOException e) {
			e.printStackTrace();
			for ( int i = 0; i < cmdarray.length; ++i)
				System.out.println( cmdarray[ i]);
			System.out.println( "error:2");
			output( filename, id, "error:2");
		}
	}

	/**
	 * @param filename
	 * @param id
	 * @param string
	 */
	private static void output(String filename, String id, String string) {
		FileUtility.write_text_to_file( new File( filename + "." + id), string);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if ( 4 > args.length)
			return;

		String[] cmdarray = get_cmdarray( args);
		if ( null == cmdarray) {
			System.out.println( args[ 2] + " : " + args[ 3] + " : error:1");
			output( args[ 2], args[ 3], "error:1");
			return;
		}

		execute( cmdarray, args[ 2], args[ 3]);
	}
}
