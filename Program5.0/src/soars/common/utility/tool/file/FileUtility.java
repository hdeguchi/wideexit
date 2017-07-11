/*
 * Created on 2006/05/12
 */
package soars.common.utility.tool.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.Vector;

import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 */
public class FileUtility {

	/**
	 * @param filename
	 * @return
	 */
	public static boolean exists(String filename) {
		File file = new File( filename);
		return file.exists();
	}

	/**
	 * @param names
	 * @return
	 */
	public static String make_path(String[] names) {
		if ( 2 > names.length)
			return "";

		String path = "";
		for ( int i = 0; i < names.length - 1; ++i)
			path += ( names[ i] + File.separator);

		path += names[ names.length - 1];
		return path;
	}

	/**
	 * @param file
	 * @param directory
	 * @return
	 */
	public static boolean is_parent(File file, File directory) {
		if ( null == directory)
			return false;

		File parent = file.getParentFile();
		if ( null == parent)
			return false;

		while ( true) {
			if ( parent.equals( directory))
				return true;

			parent = parent.getParentFile();
			if ( null == parent)
				return false;
		}
	}

	/**
	 * @param directory
	 * @param self
	 */
	public static boolean delete(File directory, boolean self) {
		if ( !directory.exists())
			return true;

		if ( !delete( directory))
			return false;

		if ( self)
			directory.delete();

		return true;
	}

	/**
	 * @param file
	 * @return
	 */
	private static boolean delete(File file) {
		File[] files = file.listFiles();
		for ( int i = 0; i < files.length; ++i) {
			if ( files[ i].isDirectory()) {
				if ( !delete( files[ i]))
					return false;
			}

			if ( !files[ i].delete())
				return false;
		}

		return true;
	}

	/**
	 * @param srcPath
	 * @param destPath
	 * @return
	 */
	public static boolean copy_all(String srcPath, String destPath) {
		return copy_all( new File( srcPath), new File( destPath));
	}

	/**
	 * @param srcPath
	 * @param destPath
	 * @return
	 */
	public static boolean copy_all(File srcPath, File destPath) {
		if ( !srcPath.exists() || !srcPath.isDirectory())
			return false;

		if ( !destPath.exists() && !destPath.mkdirs())
			return false;

		File[] files = srcPath.listFiles();
		if ( null == files)
			return false;

		for ( int i = 0; i < files.length; ++i) {
			if ( files[ i].isDirectory()) {
				if ( !copy_all( files[ i], new File( destPath, files[ i].getName())))
					return false;
			} else {
				if ( !copy( files[ i], new File( destPath, files[ i].getName())))
					return false;
			}
		}

		return true;
	}

	/**
	 * @param srcPath
	 * @param destPath
	 * @return
	 */
	public static boolean copy(String srcPath, String destPath) {
		return copy( new File( srcPath), new File( destPath));
	}

	/**
	 * @param srcPath
	 * @param destPath
	 * @return
	 */
	public static boolean copy(File srcPath, File destPath) {
		try {
			FileChannel srcChannel = new FileInputStream( srcPath).getChannel();
			FileChannel destChannel = new FileOutputStream( destPath).getChannel();
			srcChannel.transferTo( 0, srcChannel.size(), destChannel);
			srcChannel.close();
			destChannel.close();
			destPath.setLastModified( srcPath.lastModified());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
//		try {
//			FileReader fileReader = new FileReader( src_path);
//			FileWriter fileWriter = new FileWriter( dest_path);
//			int data;
//			while ( -1 != ( data = fileReader.read()))
//				fileWriter.write( data);
//
//			fileReader.close();
//			fileWriter.close();
//		} catch (IOException e) {
//			//e.printStackTrace();
//			return false;
//		}
//		return true;
	}

	/**
	 * @param filename
	 * @return
	 */
	public static byte[] read(String filename) {
		InputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream( new FileInputStream( filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		Vector<Byte> data = new Vector<Byte>();
		int c;
		try {
			while ( -1 != ( c = inputStream.read()))
				data.add( new Byte( ( byte)c));
		} catch (IOException e) {
			try {
				inputStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				return null;
			}
			e.printStackTrace();
			return null;
		}

		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		if ( data.isEmpty())
			return null;

		byte[] result = new byte[ data.size()];
		for ( int i = 0; i < data.size(); ++i) {
			Byte b = data.get( i);
			result[ i] = b.byteValue();
		}

		return result;
	}

	/**
	 * @param data
	 * @param filename
	 * @return
	 */
	public static boolean write(byte[] data, String filename) {
		OutputStream outputStream = null;
		try {
			outputStream = new BufferedOutputStream( new FileOutputStream( filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		try {
			outputStream.write( data);
		} catch (IOException e) {
			try {
				outputStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				return false;
			}
			e.printStackTrace();
			return false;
		}

		try {
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

//	/**
//	 * @param file
//	 * @param encodingFrom
//	 * @param encodingTo
//	 * @return
//	 */
//	public static boolean convert_encoding(File file, String encodingFrom, String encodingTo) {
//		String text = read_text_from_file( file, encodingFrom);
//		if ( null == text)
//			return false;
//
//		return write_text_to_file( file, text, encodingTo);
//	}
//
//	/**
//	 * @param filename
//	 * @return
//	 */
//	public static String read_text_from_file(String filename) {
//		File file = new File( filename);
//		if ( !file.exists() || !file.isFile() || !file.canRead())
//			return null;
//
//		return read_text_from_file( file);
//	}
//
//	/**
//	 * @param file
//	 * @return
//	 */
//	public static String read_text_from_file(File file) {
//		BufferedReader bufferedReader;
//		try {
//			bufferedReader = new BufferedReader( new InputStreamReader( new FileInputStream( file)));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			return null;
//		}
//
//		String text = read_text_from_file( bufferedReader);
//
//		try {
//			bufferedReader.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return text;
//	}

	/**
	 * @param inputStream
	 * @return
	 */
	public static String read_text_from_file(InputStream inputStream) {
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( inputStream));

		String text = read_text_from_file( bufferedReader);

		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return text;
	}

//	/**
//	 * @param filename
//	 * @param encoding
//	 * @return
//	 */
//	public static String read_text_from_file(String filename, String encoding) {
//		File file = new File( filename);
//		if ( !file.exists() || !file.isFile() || !file.canRead())
//			return null;
//
//		return read_text_from_file( file, encoding);
//
//	}
//
//	/**
//	 * @param file
//	 * @param encoding
//	 * @return
//	 */
//	public static String read_text_from_file(File file, String encoding) {
//		BufferedReader bufferedReader;
//		try {
//			bufferedReader = new BufferedReader( new InputStreamReader( new FileInputStream( file), encoding));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			return null;
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//			return null;
//		}
//
//		String text = read_text_from_file( bufferedReader);
//
//		try {
//			bufferedReader.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return text;
//	}

	/**
	 * @param bufferedReader
	 * @return
	 */
	public static String read_text_from_file(BufferedReader bufferedReader) {
		String text = "";

		while ( true) {
			String line = null;
			try {
				line = bufferedReader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

			if ( null == line)
				break;

			text += ( line + "\n");
		}

		return text;
	}

	/**
	 * @param file
	 * @param encodingFrom
	 * @param encodingTo
	 * @return
	 */
	public static boolean convert_encoding(File file, String encodingFrom, String encodingTo) {
		String text = read_text_from_file( file, encodingFrom);
		if ( null == text)
			return false;

		return write_text_to_file( file, text, encodingTo);
	}

	/**
	 * @param filename
	 * @return
	 */
	public static String read_text_from_file(String filename) {
		File file = new File( filename);
		if ( !file.exists() || !file.isFile() || !file.canRead())
			return null;

		return read_text_from_file( file);
	}

	/**
	 * @param filename
	 * @param encoding
	 * @return
	 */
	public static String read_text_from_file(String filename, String encoding) {
		File file = new File( filename);
		if ( !file.exists() || !file.isFile() || !file.canRead())
			return null;

		return read_text_from_file( file, encoding);

	}

	/**
	 * @param file
	 * @return
	 */
	public static String read_text_from_file(File file) {
		long length = file.length();
		if ( Integer.MAX_VALUE < length)
			return null;

		byte[] buffer;
		try {
			buffer = read_text_from_file( new BufferedInputStream( new FileInputStream( file)), ( int)length);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		if ( null == buffer)
			return null;

		return new String( buffer);
	}

	/**
	 * @param file
	 * @param encoding
	 * @return
	 */
	public static String read_text_from_file(File file, String encoding) {
		long length = file.length();
		if ( Integer.MAX_VALUE < length)
			return null;

		byte[] buffer;
		try {
			buffer = read_text_from_file( new BufferedInputStream( new FileInputStream( file)), ( int)length);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return null;
		}
		if ( null == buffer)
			return null;

		try {
			return new String( buffer, encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param inputStream
	 * @param length
	 * @return
	 */
	public static byte[] read_text_from_file(InputStream inputStream, int length) {
		byte[] buffer = new byte[ length];
		int result;
		try {
			result = inputStream.read( buffer);
		} catch (IOException e) {
			try {
				inputStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				return null;
			}
			e.printStackTrace();
			return null;
		}

		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		if ( 0 > result)
			return null;

		return buffer;
	}

	/**
	 * @param filename
	 * @param text
	 * @return
	 */
	public static boolean write_text_to_file(String filename, String text) {
		return write_text_to_file( new File( filename), text);
	}

	/**
	 * @param file
	 * @param text
	 * @return
	 */
	public static boolean write_text_to_file(File file, String text) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file));
			return write_text_to_file( outputStreamWriter, text);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @param filename
	 * @param text
	 * @param encoding
	 * @return
	 */
	public static boolean write_text_to_file(String filename, String text, String encoding) {
		return write_text_to_file( new File( filename), text, encoding);
	}

	/**
	 * @param file
	 * @param text
	 * @param encoding
	 * @return
	 */
	public static boolean write_text_to_file(File file, String text, String encoding) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file), encoding);
			return write_text_to_file( outputStreamWriter, text);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @param outputStreamWriter
	 * @param text
	 * @return
	 */
	public static boolean write_text_to_file(OutputStreamWriter outputStreamWriter, String text) {
		try {
			outputStreamWriter.write( text);
			outputStreamWriter.flush();
			outputStreamWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param directory
	 * @param relativePath
	 * @return
	 */
	public static String get_absolute_path(String directory, String relativePath) {
		return get_absolute_path( directory, relativePath, File.separator);
	}

	/**
	 * @param directory
	 * @param relativePath
	 * @param separator
	 * @return
	 */
	public static String get_absolute_path(String directory, String relativePath, String separator) {
		if ( relativePath.startsWith( "." + separator))
			return ( directory + relativePath.substring( ( "." + separator).length()));

		if ( relativePath.startsWith( separator))
			return ( directory + relativePath.substring( separator.length()));


		String regex = separator;
		if ( regex.equals( "\\"))
			regex += "\\";

		String[] directories = directory.split( regex);

		int counter = 0;
		String temp = relativePath;
		while ( temp.startsWith( ".." + separator)) {
			++counter;
			temp = temp.substring( ( ".." + separator).length());
		}

		if ( -1 == directories[ 0].indexOf( ':')) {
			if ( directories.length < counter)
				return null;
		} else {
			if ( directories.length - 1< counter)
				return null;
		}

		String absolutePath = "";
		for ( int i = 0; i < directories.length - counter; ++i)
			absolutePath += ( directories[ i] + separator);

		absolutePath += temp;

		return absolutePath;
	}

	/**
	 * @param directory
	 * @param absolutePath
	 * @return
	 */
	public static String get_relative_path(String directory, String absolutePath) {
		return get_relative_path( directory, absolutePath, File.separator);
	}

	/**
	 * @param directory
	 * @param absolutePath
	 * @param separator
	 * @return
	 */
	public static String get_relative_path(String directory, String absolutePath, String separator) {
		if ( -1 != directory.indexOf( ':') && -1 != absolutePath.indexOf( ':')) {
			if ( directory.toLowerCase().charAt( 0) != absolutePath.toLowerCase().charAt( 0))
				return absolutePath;
		} else if ( ( -1 == directory.indexOf( ':') && -1 != absolutePath.indexOf( ':'))
			|| ( -1 != directory.indexOf( ':') && -1 == absolutePath.indexOf( ':')))
			return null;

		String regex = separator;
		if ( regex.equals( "\\"))
			regex += "\\";

		String[] directories1 = directory.split( regex);
		String[] directories2 = absolutePath.split( regex);

		String relativePath = "";

		for ( int i = 0; i < directories1.length; ++i) {

			if ( directories2.length == i)
				return null;

			if ( !directories1[ i].equals( directories2[ i])
				&& ( -1 == directories1[ i].indexOf( ':') && -1 == directories2[ i].indexOf( ':'))) {

				for ( int j = 0; j < directories1.length - i; ++j)
					relativePath += ( ".." + separator);

				for ( int j = i; j < directories2.length - 1; ++j)
					relativePath += ( directories2[ j] + separator);

				relativePath += directories2[ directories2.length - 1];
				return relativePath;
			}
		}

		if ( directories2.length <= directories1.length)
			return null;

		for ( int i = directories1.length; i < directories2.length - 1; ++i)
			relativePath += ( directories2[ i] + separator);

		relativePath += directories2[ directories2.length - 1];
		return relativePath;
	}

	/**
	 * @param path
	 * @return
	 */
	public static String[] get_directory_and_filename(String path) {
		String[] words = Tool.split( path, '/');
		if ( null == words || 0 == words.length)
			return null;

		if ( 1 == words.length)
			return new String[] { "", words[ 0]};
		else {
			if ( words[ words.length - 1].equals( ""))
				return new String[] { path.substring( 0, path.length() - 1), ""};
			else
				return new String[] { path.substring( 0, path.length() - words[ words.length - 1].length() - 1), words[ words.length - 1]};
		}
	}

	/**
	 * @param filename
	 * @return
	 */
	public static String regularize_filename(String filename) {

		String result = new String();

		for ( int i = 0; i < filename.length(); ++i) {
			if ( ' ' == filename.charAt( i))
				result += "%20";
			else
				result += filename.charAt( i);
		}

		return result;
	}

	/**
	 * @param string
	 * @return
	 */
	public static String encode_url(String string){
		if ( null == string)
			return null;

		StringBuffer result = new StringBuffer();
		for ( int i = 0; i < string.length(); ++i) {
			char c = string.charAt( i);
			if ( ( 'a' <= c && c <= 'z') || ( 'A' <= c && c <= 'Z') || ( '0' <= c && c <= '9')) {
				result.append( c);
			} else if ( c == ' ') {
				result.append( "+");
			} else {
				String hex = Integer.toHexString( c).toUpperCase();	
				hex = "0" + hex;
				hex = hex.substring( hex.length() - 2 , hex.length());
				result.append( "%");
				result.append( hex);
			}
		}

		return result.toString();
	}

	/**
	 * @param string
	 * @return
	 */
	public static String decode_url(String string) {
		if ( null == string)
			return null;

		String result = "";
		for ( int i = 0; i < string.length(); ++i) {
			char c = string.charAt( i);
			if ( ( 'a' <= c && c <= 'z') || ( 'A' <= c && c <= 'Z') || ( '0' <= c && c <= '9')) {
				result += c;
			} else if ( c == '+') {
				result += " ";
			} else if( c=='%' ) {
				if ( i + 2 < string.length()) {
					String cc = "";
					cc += string.charAt( i + 1);
					cc += string.charAt( i + 2);
					result += ( char)Integer.parseInt( cc, 16);
					i += 2;
				} else {
					break;
				}
			}
		}

		return result;
	}

	/**
	 * @param randomAccessFile
	 * @param encoding
	 * @return
	 */
	public static String readLine(RandomAccessFile randomAccessFile, String encoding) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		int c = -1;
		boolean eol = false;

		while ( !eol) {
			try {
				switch ( c = randomAccessFile.read()) {
					case -1:
					case '\n':
						eol = true;
						break;
					case '\r':
						eol = true;
						long cur = randomAccessFile.getFilePointer();
						if ( '\n' != randomAccessFile.read())
							randomAccessFile.seek( cur);
						break;
					default:
						byteArrayOutputStream.write( c);
						break;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		if ( ( -1 == c) && ( 0 == byteArrayOutputStream.size()))
			return null;

		try {
			return byteArrayOutputStream.toString( encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
