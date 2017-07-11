/**
 * 
 */
package soars.tool.grid.compressor.main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author kurata
 *
 */
public class Application {

	/**
	 * 
	 */
	public Application() {
		super();
	}

	/**
	 * @param filename
	 * @param files
	 * @return
	 */
	private boolean compress(String filename, File[] files) {
		List<File> list= new ArrayList<File>();
		for ( int i = 0; i < files.length; ++i)
			get( files[ i], list);

    ZipOutputStream zipOutputStream;
		try {
			zipOutputStream = new ZipOutputStream( new FileOutputStream( new File( filename)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		boolean result = true;
		for ( int i = 0; i < list.size(); ++i) {
    	if ( !compress( zipOutputStream, list.get( i), files[ 0])) {
    		result = false;
    		break;
    	}
    }

		try {
			zipOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
  		result = false;
		}

		return result;
	}

	/**
	 * @param file
	 * @param list
	 */
	private void get(File file, List<File> list) {
		if ( file.isFile())
			list.add( file);
		else if ( file.isDirectory()) {
			File[] files = file.listFiles();
			for ( int i = 0; i < files.length; ++i)
				get( files[ i], list);
		}
	}

	/**
	 * @param zipOutputStream
	 * @param file
	 * @param log_directory
	 * @return
	 */
	private boolean compress(ZipOutputStream zipOutputStream, File file, File log_directory) {
		BufferedInputStream bufferedInputStream;
		try {
			bufferedInputStream = new BufferedInputStream( new FileInputStream( file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		ZipEntry zipEntry = new ZipEntry( getPath( file, log_directory));
		//ZipEntry zipEntry = new ZipEntry( file.getPath());

		try {
			zipOutputStream.putNextEntry( zipEntry);

			byte buf[] = new byte[ 1024000];
			int count;
			while ( -1 != ( count = bufferedInputStream.read( buf, 0, 1024000)))
				zipOutputStream.write( buf, 0, count);

			bufferedInputStream.close();

			zipOutputStream.closeEntry(); 

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param file
	 * @param log_directory
	 * @return
	 */
	private String getPath(File file, File log_directory) {
		if ( !file.isFile())
			return file.getPath();

		if ( !file.getParentFile().getName().equals( "agents")
			&& !file.getParentFile().getName().equals( "spots"))
			return file.getPath();

		if ( file.getParentFile().getParentFile().getParentFile().equals( log_directory))
			return getPath( file);

		if ( file.getParentFile().getParentFile().getParentFile().getParentFile().equals( log_directory)
			&& !file.getParentFile().getParentFile().getName().endsWith( "userdata"))
			return getPath( file);

		return file.getPath();
	}

	/**
	 * @param file
	 * @return
	 */
	private String getPath(File file) {
		if ( !file.getName().endsWith( ".log"))
			return file.getPath();

		if ( file.getName().equals( "$Name.log")
			|| file.getName().equals( "$Role.log")
			|| file.getName().equals( "$Spot.log"))
			return file.getPath();

		return getName( file);
	}

	/**
	 * @param file
	 * @return
	 */
	private String getName(File file) {
		if ( !file.exists() || !file.isFile() || !file.canRead())
			return file.getPath();

		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader( new InputStreamReader( new FileInputStream( file), "UTF-8"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return file.getPath();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return file.getPath();
		}

		String name = null;
		int index = 0;
		while ( true) {
			String line = null;
			try {
				line = bufferedReader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}

			if ( null == line)
				break;

			if ( 3 > ++index)
				continue;

			String[] words = line.split( "\t");
			if ( null == words || 2 > words.length || null == words[ 1] || words[ 1].equals( ""))
				break;

			name = ( file.getParentFile().getPath() + "/" + words[ 1] + ".log");
			//System.out.println( name);
			break;
		}

		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ( ( null != name) ? name : file.getPath());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if ( 2 > args.length) {
			System.out.println( "error!");
			System.exit( 1);
		}

		File[] files = new File[ args.length - 1];
		for ( int i = 0; i < files.length; ++i)
			files[ i] = new File( args[ i + 1]);

		Application application = new Application();
		if ( !application.compress( args[ 0], files)) {
			System.out.println( "error!");
			System.exit( 1);
		}

		System.out.println( "success!");
	}
}
