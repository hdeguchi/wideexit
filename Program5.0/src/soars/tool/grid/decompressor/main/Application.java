/**
 * 
 */
package soars.tool.grid.decompressor.main;

import java.io.File;

import soars.common.utility.tool.file.ZipUtility;

/**
 * @author kurata
 *
 */
public class Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if ( 2 != args.length) {
			System.out.println( "error!");
			System.exit( 1);
		}

		File file = new File( args[ 0]);
		if ( !file.exists() || !file.isFile()) {
			System.out.println( "error!");
			System.exit( 1);
		}

		File parent_directory = new File( args[ 1]);
		if ( parent_directory.exists() && !parent_directory.isDirectory()) {
			System.out.println( "error!");
			System.exit( 1);
		}

		if ( !parent_directory.exists() && !parent_directory.mkdirs()) {
			System.out.println( "error!");
			System.exit( 1);
		}

		if ( !ZipUtility.decompress( file, parent_directory)) {
			System.out.println( "error!");
			System.exit( 1);
		}

		System.out.println( "success!");
	}
}
