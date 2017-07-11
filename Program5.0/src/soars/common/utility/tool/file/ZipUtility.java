/**
 * 
 */
package soars.common.utility.tool.file;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 *
 */
public class ZipUtility {

	/**
	 * 
	 */
	static public final int _bufferSize = 1024000;

	/**
	 * @param path
	 * @param filename
	 * @return
	 */
	public static boolean find(File path, String filename) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream( new FileInputStream( path));
			ZipEntry zipEntry = null;
			try {
				while ( null != ( zipEntry = zipInputStream.getNextEntry())) {
					if ( zipEntry.getName().equals( filename)) {
						zipInputStream.closeEntry();
						return true;
					}
					zipInputStream.closeEntry();
				}
			} finally {
				zipInputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * @param path
	 * @param rootDirectory
	 * @param parentDirectory
	 * @param exclusionFilenames
	 * @return
	 */
	public static boolean compress(File path, File rootDirectory, File parentDirectory, List<String> exclusionFilenames) {
		return compress( path, rootDirectory, parentDirectory, exclusionFilenames, _bufferSize);
	}

	/**
	 * @param path
	 * @param rootDirectory
	 * @param parentDirectory
	 * @param exclusionFilenames
	 * @param bufferSize
	 * @return
	 */
	private static boolean compress(File path, File rootDirectory, File parentDirectory, List<String> exclusionFilenames, int bufferSize) {
		List<File> list = new ArrayList<File>();
		get( rootDirectory, list, parentDirectory, exclusionFilenames);
		if ( list.isEmpty())
			return false;

		try {
			ZipOutputStream zipOutputStream = new ZipOutputStream( new FileOutputStream( path));
			try {
				for ( int i = 0; i < list.size(); ++i) {
		    	if ( !compress( zipOutputStream, ( File)list.get( i), parentDirectory, bufferSize))
		    		return false;
		    }
			} finally {
				zipOutputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param file
	 * @param list
	 * @param parentDirectory
	 * @param exclusionFilenames
	 */
	private static void get(File file, List<File> list, File parentDirectory, List<String> exclusionFilenames) {
		String filename = ( ( null == parentDirectory) ? file.getPath().replaceAll( "\\\\", "/") : file.getPath().replaceAll( "\\\\", "/").substring( parentDirectory.getAbsolutePath().replaceAll( "\\\\", "/").length() + 1));
		if ( contains( filename, exclusionFilenames))
			return;

		list.add( file);
		if ( file.isDirectory()) {
			File[] files = file.listFiles();
			for ( int i = 0; i < files.length; ++i)
				get( files[ i], list, parentDirectory, exclusionFilenames);
		}
	}

	/**
	 * @param filename
	 * @param exclusionFilenames
	 * @return
	 */
	private static boolean contains(String filename, List<String> exclusionFilenames) {
		for ( String exclusionFilename:exclusionFilenames) {
			if ( filename.startsWith( exclusionFilename))
				return true;
		}
		return false;
	}

	/**
	 * @param path
	 * @param rootDirectory
	 * @param parentDirectory
	 * @return
	 */
	public static boolean compress(File path, File rootDirectory, File parentDirectory) {
		return compress( path, rootDirectory, parentDirectory, _bufferSize);
	}

	/**
	 * @param path
	 * @param rootDirectory
	 * @param parentDirectory
	 * @param bufferSize
	 * @return
	 */
	public static boolean compress(File path, File rootDirectory, File parentDirectory, int bufferSize) {
		List<File> list = new ArrayList<File>();
		get( rootDirectory, list);
		if ( list.isEmpty())
			return false;

		try {
			ZipOutputStream zipOutputStream = new ZipOutputStream( new FileOutputStream( path));
			try {
				for ( int i = 0; i < list.size(); ++i) {
		    	if ( !compress( zipOutputStream, ( File)list.get( i), parentDirectory, bufferSize))
		    		return false;
		    }
			} finally {
				zipOutputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param file
	 * @param list
	 * @return
	 */
	private static void get(File file, List<File> list) {
		list.add( file);
		if ( file.isDirectory()) {
			File[] files = file.listFiles();
			for ( int i = 0; i < files.length; ++i)
				get( files[ i], list);
		}
	}

	/**
	 * @param zipOutputStream
	 * @param file
	 * @param parentDirectory
	 * @param bufferSize
	 * @return
	 */
	private static boolean compress(ZipOutputStream zipOutputStream, File file, File parentDirectory, int bufferSize) {
		ZipEntry zipEntry = new ZipEntry(
			( ( null == parentDirectory) ? file.getPath().replaceAll( "\\\\", "/") : file.getPath().replaceAll( "\\\\", "/").substring( parentDirectory.getAbsolutePath().replaceAll( "\\\\", "/").length() + 1))
			+ ( file.isDirectory() ? "/" : ""));
		zipEntry.setTime( file.lastModified());

		try {
			zipOutputStream.putNextEntry( zipEntry);

			if ( file.isFile()) {
				BufferedInputStream bufferedInputStream = new BufferedInputStream( new FileInputStream( file));
				byte buf[] = new byte[ bufferSize];
				int count;
				while ( -1 != ( count = bufferedInputStream.read( buf, 0, bufferSize)))
					zipOutputStream.write( buf, 0, count);

				bufferedInputStream.close();
			}

			zipOutputStream.closeEntry(); 

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param file
	 * @param entryMap Entries to be appended
	 * @return
	 */
	public static File append(File file, Map<String, List<Entry>> entryMap) {
		return append( file, entryMap, _bufferSize);
	}

	/**
	 * @param file
	 * @param entryMap Entries to be appended
	 * @param bufferSize
	 * @return
	 */
	private static File append(File file, Map<String, List<Entry>> entryMap, int bufferSize) {
		String name = file.getName();
		File tempFile = new File( file.getParentFile(), file.getName() + ".tmp");
		if ( !append( tempFile, file, entryMap, bufferSize)) {
			tempFile.delete();
			return null;
		}

		while ( true) {
			file.delete();
			if ( !file.exists())
				break;

			try {
				Thread.sleep( 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		while ( true) {
			if ( tempFile.renameTo( file))
				break;

			try {
				Thread.sleep( 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return tempFile;
	}

	/**
	 * @param outputFile
	 * @param inputFile
	 * @param entryMap Entries to be appended
	 * @return
	 */
	public static boolean append(File outputFile, File inputFile, Map<String, List<Entry>> entryMap) {
		return append( outputFile, inputFile, entryMap, _bufferSize);
	}

	/**
	 * @param outputFile
	 * @param inputFile
	 * @param entryMap Entries to be appended
	 * @param bufferSize
	 * @return
	 */
	private static boolean append(File outputFile, File inputFile, Map<String, List<Entry>> entryMap, int bufferSize) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream( new FileInputStream( inputFile));
			ZipOutputStream zipOutputStream = new ZipOutputStream( new FileOutputStream( outputFile));
			ZipEntry inputZipEntry = null;
			try {
				while ( null != ( inputZipEntry = zipInputStream.getNextEntry())) {
					//System.out.println( inputZipEntry.getName());
					if ( inputZipEntry.isDirectory()) {
						zipOutputStream.putNextEntry( new ZipEntry( inputZipEntry));
						List<Entry> entryList = entryMap.get( inputZipEntry.getName());
						zipOutputStream.closeEntry();
						zipInputStream.closeEntry();
						if ( null != entryList) {
							for ( Entry entry:entryList) {
								zipOutputStream.putNextEntry( new ZipEntry( entry._path));
								if ( null != entry._file) {
									BufferedInputStream bufferedInputStream = new BufferedInputStream( new FileInputStream( entry._file));
									byte buf[] = new byte[ bufferSize];
									int count;
									while ( -1 != ( count = bufferedInputStream.read( buf, 0, bufferSize)))
										zipOutputStream.write( buf, 0, count);
								}
								zipOutputStream.closeEntry();
							}
						}
						continue;
					} else {
						zipOutputStream.putNextEntry( new ZipEntry( inputZipEntry));
						BufferedInputStream bufferedInputStream = new BufferedInputStream( zipInputStream);
						byte buf[] = new byte[ bufferSize];
						int count;
						while ( -1 != ( count = bufferedInputStream.read( buf, 0, bufferSize)))
							zipOutputStream.write( buf, 0, count);
					}
					zipOutputStream.closeEntry();
					zipInputStream.closeEntry();
				}
			} finally {
				zipInputStream.close();
				zipOutputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param file
	 * @param fileMap Entries to be replaced
	 * @return
	 */
	public static File update(File file, Map<String, File> fileMap) {
		return update( file, fileMap, _bufferSize);
	}

	/**
	 * @param file
	 * @param fileMap Entries to be replaced
	 * @param bufferSize
	 * @return
	 */
	public static File update(File file, Map<String, File> fileMap, int bufferSize) {
		String name = file.getName();
		File tempFile = new File( file.getParentFile(), file.getName() + ".tmp");
		if ( !update( tempFile, file, fileMap, bufferSize)) {
			tempFile.delete();
			return null;
		}

		while ( true) {
			file.delete();
			if ( !file.exists())
				break;

			try {
				Thread.sleep( 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		while ( true) {
			if ( tempFile.renameTo( file))
				break;

			try {
				Thread.sleep( 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return tempFile;
	}

	/**
	 * @param outputFile
	 * @param inputFile
	 * @param fileMap Entries to be replaced
	 * @return
	 */
	public static boolean update(File outputFile, File inputFile, Map<String, File> fileMap) {
		return update( outputFile, inputFile, fileMap, _bufferSize);
	}

	/**
	 * @param outputFile
	 * @param inputFile
	 * @param fileMap Entries to be replaced
	 * @param bufferSize
	 * @return
	 */
	public static boolean update(File outputFile, File inputFile, Map<String, File> fileMap, int bufferSize) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream( new FileInputStream( inputFile));
			ZipOutputStream zipOutputStream = new ZipOutputStream( new FileOutputStream( outputFile));
			ZipEntry inputZipEntry = null;
			try {
				while ( null != ( inputZipEntry = zipInputStream.getNextEntry())) {
					//System.out.println( inputZipEntry.getName());
					if ( inputZipEntry.isDirectory())
						zipOutputStream.putNextEntry( new ZipEntry( inputZipEntry));
					else {
						File file = fileMap.get( inputZipEntry.getName());
						if ( null == file) {
							zipOutputStream.putNextEntry( new ZipEntry( inputZipEntry));
							BufferedInputStream bufferedInputStream = new BufferedInputStream( zipInputStream);
							byte buf[] = new byte[ bufferSize];
							int count;
							while ( -1 != ( count = bufferedInputStream.read( buf, 0, bufferSize)))
								zipOutputStream.write( buf, 0, count);
						} else {
							zipOutputStream.putNextEntry( new ZipEntry( inputZipEntry.getName()));
							BufferedInputStream bufferedInputStream = new BufferedInputStream( new FileInputStream( file));
							byte buf[] = new byte[ bufferSize];
							int count;
							while ( -1 != ( count = bufferedInputStream.read( buf, 0, bufferSize)))
								zipOutputStream.write( buf, 0, count);
						}
					}
					zipOutputStream.closeEntry();
					zipInputStream.closeEntry();
				}
			} finally {
				zipInputStream.close();
				zipOutputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param file
	 * @param exclusionFilenames
	 * @return
	 */
	public static File update(File file, List<String> exclusionFilenames) {
		return update( file, exclusionFilenames, _bufferSize);
	}

	/**
	 * @param file
	 * @param exclusionFilenames
	 * @param bufferSize
	 * @return
	 */
	public static File update(File file, List<String> exclusionFilenames, int bufferSize) {
		String name = file.getName();
		File tempFile = new File( file.getParentFile(), file.getName() + ".tmp");
		if ( !update( tempFile, file, exclusionFilenames, bufferSize)) {
			tempFile.delete();
			return null;
		}

		while ( true) {
			file.delete();
			if ( !file.exists())
				break;

			try {
				Thread.sleep( 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		while ( true) {
			if ( tempFile.renameTo( file))
				break;

			try {
				Thread.sleep( 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return tempFile;
	}

	/**
	 * @param outputFile
	 * @param inputFile
	 * @param exclusionFilenames
	 * @return
	 */
	public static boolean update(File outputFile, File inputFile, List<String> exclusionFilenames) {
		return update( outputFile, inputFile, exclusionFilenames, _bufferSize);
	}

	/**
	 * @param outputFile
	 * @param inputFile
	 * @param exclusionFilenames
	 * @param bufferSize
	 * @return
	 */
	public static boolean update(File outputFile, File inputFile, List<String> exclusionFilenames, int bufferSize) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream( new FileInputStream( inputFile));
			ZipOutputStream zipOutputStream = new ZipOutputStream( new FileOutputStream( outputFile));
			ZipEntry inputZipEntry = null;
			try {
				while ( null != ( inputZipEntry = zipInputStream.getNextEntry())) {
					//System.out.println( inputZipEntry.getName());
					if ( contains( inputZipEntry.getName(), exclusionFilenames)) {
						zipInputStream.closeEntry();
						continue;
					}

					if ( inputZipEntry.isDirectory())
						zipOutputStream.putNextEntry( new ZipEntry( inputZipEntry));
					else {
						zipOutputStream.putNextEntry( new ZipEntry( inputZipEntry));
						BufferedInputStream bufferedInputStream = new BufferedInputStream( zipInputStream);
						byte buf[] = new byte[ bufferSize];
						int count;
						while ( -1 != ( count = bufferedInputStream.read( buf, 0, bufferSize)))
							zipOutputStream.write( buf, 0, count);
					}
					zipOutputStream.closeEntry();
					zipInputStream.closeEntry();
				}
			} finally {
				zipInputStream.close();
				zipOutputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param file
	 * @param fileMap
	 * @param exclusionFilenames
	 * @return
	 */
	public static File update(File file, Map<String, File> fileMap, List<String> exclusionFilenames) {
		return update( file, fileMap, exclusionFilenames, _bufferSize);
	}

	/**
	 * @param file
	 * @param fileMap
	 * @param exclusionFilenames
	 * @param bufferSize
	 * @return
	 */
	public static File update(File file, Map<String, File> fileMap, List<String> exclusionFilenames, int bufferSize) {
		String name = file.getName();
		File tempFile = new File( file.getParentFile(), file.getName() + ".tmp");
		if ( !update( tempFile, file, fileMap, exclusionFilenames, bufferSize)) {
			tempFile.delete();
			return null;
		}

		while ( true) {
			file.delete();
			if ( !file.exists())
				break;

			try {
				Thread.sleep( 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		while ( true) {
			if ( tempFile.renameTo( file))
				break;

			try {
				Thread.sleep( 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return tempFile;
	}

	/**
	 * @param outputFile
	 * @param inputFile
	 * @param fileMap
	 * @param exclusionFilenames
	 * @return
	 */
	public static boolean update(File outputFile, File inputFile, Map<String, File> fileMap, List<String> exclusionFilenames) {
		return update( outputFile, inputFile, fileMap, exclusionFilenames, _bufferSize);
	}

	/**
	 * @param outputFile
	 * @param inputFile
	 * @param fileMap
	 * @param exclusionFilenames
	 * @param bufferSize
	 * @return
	 */
	public static boolean update(File outputFile, File inputFile, Map<String, File> fileMap, List<String> exclusionFilenames, int bufferSize) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream( new FileInputStream( inputFile));
			ZipOutputStream zipOutputStream = new ZipOutputStream( new FileOutputStream( outputFile));
			ZipEntry inputZipEntry = null;
			try {
				while ( null != ( inputZipEntry = zipInputStream.getNextEntry())) {
					//System.out.println( inputZipEntry.getName());
					if ( contains( inputZipEntry.getName(), exclusionFilenames)) {
						zipInputStream.closeEntry();
						continue;
					}

					if ( inputZipEntry.isDirectory())
						zipOutputStream.putNextEntry( new ZipEntry( inputZipEntry));
					else {
						File file = fileMap.get( inputZipEntry.getName());
						if ( null == file) {
							zipOutputStream.putNextEntry( new ZipEntry( inputZipEntry));
							BufferedInputStream bufferedInputStream = new BufferedInputStream( zipInputStream);
							byte buf[] = new byte[ bufferSize];
							int count;
							while ( -1 != ( count = bufferedInputStream.read( buf, 0, bufferSize)))
								zipOutputStream.write( buf, 0, count);
						} else {
							zipOutputStream.putNextEntry( new ZipEntry( inputZipEntry.getName()));
							BufferedInputStream bufferedInputStream = new BufferedInputStream( new FileInputStream( file));
							byte buf[] = new byte[ bufferSize];
							int count;
							while ( -1 != ( count = bufferedInputStream.read( buf, 0, bufferSize)))
								zipOutputStream.write( buf, 0, count);
						}
					}
					zipOutputStream.closeEntry();
					zipInputStream.closeEntry();
				}
			} finally {
				zipInputStream.close();
				zipOutputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param file
	 * @param entryMap Entries to be appended
	 * @param fileMap Entries to be replaced
	 * @return
	 */
	public static File update(File file, Map<String, List<Entry>> entryMap, Map<String, File> fileMap) {
		return update( file, entryMap, fileMap, _bufferSize);
	}

	/**
	 * @param file
	 * @param entryMap Entries to be appended
	 * @param fileMap Entries to be replaced
	 * @param bufferSize
	 * @return
	 */
	public static File update(File file, Map<String, List<Entry>> entryMap, Map<String, File> fileMap, int bufferSize) {
		String name = file.getName();
		File tempFile = new File( file.getParentFile(), file.getName() + ".tmp");
		if ( !update( tempFile, file, entryMap, fileMap, bufferSize)) {
			tempFile.delete();
			return null;
		}

		while ( true) {
			file.delete();
			if ( !file.exists())
				break;

			try {
				Thread.sleep( 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		while ( true) {
			if ( tempFile.renameTo( file))
				break;

			try {
				Thread.sleep( 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return tempFile;
	}

	/**
	 * @param outputFile
	 * @param inputFile
	 * @param entryMap Entries to be appended
	 * @param fileMap Entries to be replaced
	 * @return
	 */
	public static boolean update(File outputFile, File inputFile, Map<String, List<Entry>> entryMap, Map<String, File> fileMap) {
		return update( outputFile, inputFile, entryMap, fileMap, _bufferSize);
	}

	/**
	 * @param outputFile
	 * @param inputFile
	 * @param entryMap Entries to be appended
	 * @param fileMap Entries to be replaced
	 * @param bufferSize
	 * @return
	 */
	public static boolean update(File outputFile, File inputFile, Map<String, List<Entry>> entryMap, Map<String, File> fileMap, int bufferSize) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream( new FileInputStream( inputFile));
			ZipOutputStream zipOutputStream = new ZipOutputStream( new FileOutputStream( outputFile));
			ZipEntry inputZipEntry = null;
			try {
				while ( null != ( inputZipEntry = zipInputStream.getNextEntry())) {
					//System.out.println( inputZipEntry.getName());
					if ( inputZipEntry.isDirectory()) {
						zipOutputStream.putNextEntry( new ZipEntry( inputZipEntry));
						List<Entry> entryList = entryMap.get( inputZipEntry.getName());
						zipOutputStream.closeEntry();
						zipInputStream.closeEntry();
						if ( null != entryList) {
							for ( Entry entry:entryList) {
								zipOutputStream.putNextEntry( new ZipEntry( entry._path));
								if ( null != entry._file) {
									BufferedInputStream bufferedInputStream = new BufferedInputStream( new FileInputStream( entry._file));
									byte buf[] = new byte[ bufferSize];
									int count;
									while ( -1 != ( count = bufferedInputStream.read( buf, 0, bufferSize)))
										zipOutputStream.write( buf, 0, count);
								}
								zipOutputStream.closeEntry();
							}
						}
						continue;
					} else {
						File file = fileMap.get( inputZipEntry.getName());
						if ( null == file) {
							zipOutputStream.putNextEntry( new ZipEntry( inputZipEntry));
							BufferedInputStream bufferedInputStream = new BufferedInputStream( zipInputStream);
							byte buf[] = new byte[ bufferSize];
							int count;
							while ( -1 != ( count = bufferedInputStream.read( buf, 0, bufferSize)))
								zipOutputStream.write( buf, 0, count);
						} else {
							zipOutputStream.putNextEntry( new ZipEntry( inputZipEntry.getName()));
							BufferedInputStream bufferedInputStream = new BufferedInputStream( new FileInputStream( file));
							byte buf[] = new byte[ bufferSize];
							int count;
							while ( -1 != ( count = bufferedInputStream.read( buf, 0, bufferSize)))
								zipOutputStream.write( buf, 0, count);
						}
					}
					zipOutputStream.closeEntry();
					zipInputStream.closeEntry();
				}
			} finally {
				zipInputStream.close();
				zipOutputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param path
	 * @param parentDirectory
	 * @return
	 */
	public static boolean decompress(File path, File parentDirectory) {
		return decompress( path, parentDirectory, _bufferSize);
	}

	/**
	 * @param path
	 * @param parentDirectory
	 * @param bufferSize
	 * @return
	 */
	public static boolean decompress(File path, File parentDirectory, int bufferSize) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream( new FileInputStream( path));
			return decompress( zipInputStream, parentDirectory, bufferSize);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @param zip
	 * @param parentDirectory
	 * @return
	 */
	public static boolean decompress(byte[] zip, File parentDirectory) {
		return decompress( zip, parentDirectory, _bufferSize);
	}

	/**
	 * @param zip
	 * @param parentDirectory
	 * @param bufferSize
	 * @return
	 */
	public static boolean decompress(byte[] zip, File parentDirectory, int bufferSize) {
		return decompress( new ZipInputStream( new ByteArrayInputStream( zip)), parentDirectory, bufferSize);
	}

	/**
	 * @param inputStream
	 * @param parentDirectory
	 * @return
	 */
	public static boolean decompress(InputStream inputStream, File parentDirectory) {
		return decompress( new ZipInputStream( inputStream), parentDirectory, _bufferSize);
	}

	/**
	 * @param inputStream
	 * @param parentDirectory
	 * @param bufferSize
	 * @return
	 */
	public static boolean decompress(InputStream inputStream, File parentDirectory, int bufferSize) {
		return decompress( new ZipInputStream( inputStream), parentDirectory, bufferSize);
	}

	/**
	 * @param zipInputStream
	 * @param parentDirectory
	 * @param bufferSize
	 * @return
	 */
	private static boolean decompress(ZipInputStream zipInputStream, File parentDirectory, int bufferSize) {
		int[] range = new int[] { -1, -1};
		Map<String, Map<File, Long>> timeMap = new HashMap<String, Map<File, Long>>();
		try {
			try {
				ZipEntry zipEntry = null;
				while ( null != ( zipEntry = zipInputStream.getNextEntry())) {
					if ( zipEntry.isDirectory()) {
						File directory = new File( parentDirectory, zipEntry.getName());
						if ( !directory.mkdirs()) {
							zipInputStream.closeEntry();
							return false;
						}
						make( directory, zipEntry.getTime(), timeMap, range);
					} else { 
						File file = new File( parentDirectory, zipEntry.getName());
						File directory = new File( file.getParent());
						if ( !directory.exists() && !directory.mkdirs()) {
							zipInputStream.closeEntry();
							return false;
						}

						FileOutputStream fileOutputStream = new FileOutputStream( file);

						byte[] buf = new byte[ bufferSize];
						BufferedInputStream bufferedInputStream = new BufferedInputStream( zipInputStream);

						int count;
						while ( -1 != ( count = bufferedInputStream.read( buf, 0, bufferSize)))
							fileOutputStream.write( buf, 0, count);

						fileOutputStream.close();

						make( file, zipEntry.getTime(), timeMap, range);
					}

					zipInputStream.closeEntry();
				}
			} finally {
				zipInputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		update( timeMap, range);

		return true;
	}

	/**
	 * @param path
	 * @param filenames
	 * @param parentDirectory
	 * @return
	 */
	public static boolean decompress(File path, String[] filenames, File parentDirectory) {
		return decompress( path, filenames, parentDirectory, _bufferSize);
	}

	/**
	 * @param path
	 * @param filenames
	 * @param parentDirectory
	 * @param bufferSize
	 * @return
	 */
	public static boolean decompress(File path, String[] filenames, File parentDirectory, int bufferSize) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream( new FileInputStream( path));
			return decompress( zipInputStream, filenames, parentDirectory, bufferSize);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @param zip
	 * @param filenames
	 * @param parentDirectory
	 * @return
	 */
	public static boolean decompress(byte[] zip, String[] filenames, File parentDirectory) {
		return decompress( zip, filenames, parentDirectory, _bufferSize);
	}

	/**
	 * @param zip
	 * @param filenames
	 * @param parentDirectory
	 * @param bufferSize
	 * @return
	 */
	public static boolean decompress(byte[] zip, String[] filenames, File parentDirectory, int bufferSize) {
		return decompress( new ZipInputStream( new ByteArrayInputStream( zip)), filenames, parentDirectory, bufferSize);
	}

	/**
	 * @param inputStream
	 * @param filenames
	 * @param parentDirectory
	 * @return
	 */
	public static boolean decompress(InputStream inputStream, String[] filenames, File parentDirectory) {
		return decompress( new ZipInputStream( inputStream), filenames, parentDirectory, _bufferSize);
	}

	/**
	 * @param inputStream
	 * @param filenames
	 * @param parentDirectory
	 * @param bufferSize
	 * @return
	 */
	public static boolean decompress(InputStream inputStream, String[] filenames, File parentDirectory, int bufferSize) {
		return decompress( new ZipInputStream( inputStream), filenames, parentDirectory, bufferSize);
	}

	/**
	 * @param zipInputStream
	 * @param filenames
	 * @param parentDirectory
	 * @param bufferSize
	 * @return
	 */
	private static boolean decompress(ZipInputStream zipInputStream, String[] filenames, File parentDirectory, int bufferSize) {
		int[] range = new int[] { -1, -1};
		Map<String, Map<File, Long>> timeMap = new HashMap<String, Map<File, Long>>();
		try {
			try {
				ZipEntry zipEntry = null;
				while ( null != ( zipEntry = zipInputStream.getNextEntry())) {
					String filename = zipEntry.getName();
					if ( !contains( filename, filenames)) {
						zipInputStream.closeEntry();
						continue;
					}

					if ( zipEntry.isDirectory()) {
						File directory = new File( parentDirectory, filename);
						if ( !directory.mkdirs()) {
							zipInputStream.closeEntry();
							return false;
						}
						make( directory, zipEntry.getTime(), timeMap, range);
					} else { 
						File file = new File( parentDirectory, filename);
						File directory = new File( file.getParent());
						if ( !directory.exists() && !directory.mkdirs()) {
							zipInputStream.closeEntry();
							return false;
						}

						FileOutputStream fileOutputStream = new FileOutputStream( file);

						byte[] buf = new byte[ bufferSize];
						BufferedInputStream bufferedInputStream = new BufferedInputStream( zipInputStream);

						int count;
						while ( -1 != ( count = bufferedInputStream.read( buf, 0, bufferSize)))
							fileOutputStream.write( buf, 0, count);

						fileOutputStream.close();

						make( file, zipEntry.getTime(), timeMap, range);
					}
					zipInputStream.closeEntry();
				}
			} finally {
				zipInputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		update( timeMap, range);

		return true;
	}

	/**
	 * @param path
	 * @param filenames
	 * @param exclusionPathName
	 * @param parentDirectory
	 * @return
	 */
	public static boolean decompress(File path, String[] filenames, String exclusionPathName, File parentDirectory) {
		return decompress( path, filenames, exclusionPathName, parentDirectory, _bufferSize);
	}

	/**
	 * @param path
	 * @param filenames
	 * @param exclusionPathName
	 * @param parentDirectory
	 * @param bufferSize
	 * @return
	 */
	public static boolean decompress(File path, String[] filenames, String exclusionPathName, File parentDirectory, int bufferSize) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream( new FileInputStream( path));
			return decompress( zipInputStream, filenames, exclusionPathName, parentDirectory, bufferSize);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @param zip
	 * @param filenames
	 * @param exclusionPathName
	 * @param parentDirectory
	 * @return
	 */
	public static boolean decompress(byte[] zip, String[] filenames, String exclusionPathName, File parentDirectory) {
		return decompress( zip, filenames, exclusionPathName, parentDirectory, _bufferSize);
	}

	/**
	 * @param zip
	 * @param filenames
	 * @param exclusionPathName
	 * @param parentDirectory
	 * @param bufferSize
	 * @return
	 */
	public static boolean decompress(byte[] zip, String[] filenames, String exclusionPathName, File parentDirectory, int bufferSize) {
		return decompress( new ZipInputStream( new ByteArrayInputStream( zip)), filenames, exclusionPathName, parentDirectory, bufferSize);
	}

	/**
	 * @param inputStream
	 * @param filenames
	 * @param exclusionPathName
	 * @param parentDirectory
	 * @return
	 */
	public static boolean decompress(InputStream inputStream, String[] filenames, String exclusionPathName, File parentDirectory) {
		return decompress( new ZipInputStream( inputStream), filenames, exclusionPathName, parentDirectory, _bufferSize);
	}

	/**
	 * @param inputStream
	 * @param filenames
	 * @param exclusionPathName
	 * @param parentDirectory
	 * @param bufferSize
	 * @return
	 */
	public static boolean decompress(InputStream inputStream, String[] filenames, String exclusionPathName, File parentDirectory, int bufferSize) {
		return decompress( new ZipInputStream( inputStream), filenames, exclusionPathName, parentDirectory, bufferSize);
	}

	/**
	 * @param zipInputStream
	 * @param filenames
	 * @param exclusionPathName
	 * @param parentDirectory
	 * @param bufferSize
	 * @return
	 */
	private static boolean decompress(ZipInputStream zipInputStream, String[] filenames, String exclusionPathName, File parentDirectory, int bufferSize) {
		int[] range = new int[] { -1, -1};
		Map<String, Map<File, Long>> timeMap = new HashMap<String, Map<File, Long>>();
		try {
			try {
				ZipEntry zipEntry = null;
				while ( null != ( zipEntry = zipInputStream.getNextEntry())) {
					String filename = zipEntry.getName();
					if ( !contains( filename, filenames) || !filename.startsWith( exclusionPathName) || filename.equals( exclusionPathName)) {
						zipInputStream.closeEntry();
						continue;
					}

					if ( zipEntry.isDirectory()) {
						File directory = new File( parentDirectory, filename.substring( exclusionPathName.length()));
						if ( !directory.mkdirs()) {
							zipInputStream.closeEntry();
							return false;
						}
						make( directory, zipEntry.getTime(), timeMap, range);
					} else { 
						File file = new File( parentDirectory, filename.substring( exclusionPathName.length()));
						File directory = new File( file.getParent());
						if ( !directory.exists() && !directory.mkdirs()) {
							zipInputStream.closeEntry();
							return false;
						}

						FileOutputStream fileOutputStream = new FileOutputStream( file);

						byte[] buf = new byte[ bufferSize];
						BufferedInputStream bufferedInputStream = new BufferedInputStream( zipInputStream);

						int count;
						while ( -1 != ( count = bufferedInputStream.read( buf, 0, bufferSize)))
							fileOutputStream.write( buf, 0, count);

						fileOutputStream.close();

						make( file, zipEntry.getTime(), timeMap, range);
					}
					zipInputStream.closeEntry();
				}
			} finally {
				zipInputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		update( timeMap, range);

		return true;
	}

	/**
	 * @param filename
	 * @param filenames
	 * @return
	 */
	private static boolean contains(String filename, String[] filenames) {
		for ( int i = 0; i < filenames.length; ++i) {
			if ( filenames[ i].startsWith( filename) || filename.startsWith( filenames[ i]))
				return true;
		}
		return false;
	}

	/**
	 * @param inputStream
	 * @param parentDirectory
	 * @param zipDecompressHandler
	 * @param id
	 * @return
	 */
	public static boolean decompress(InputStream inputStream, File parentDirectory, ZipDecompressHandler zipDecompressHandler, int id) {
		return decompress( new ZipInputStream( inputStream), parentDirectory, zipDecompressHandler, id, _bufferSize);
	}

	/**
	 * @param inputStream
	 * @param parentDirectory
	 * @param zipDecompressHandler
	 * @param id
	 * @param bufferSize
	 * @return
	 */
	public static boolean decompress(InputStream inputStream, File parentDirectory, ZipDecompressHandler zipDecompressHandler, int id, int bufferSize) {
		return decompress( new ZipInputStream( inputStream), parentDirectory, zipDecompressHandler, id, bufferSize);
	}

	/**
	 * @param zipInputStream
	 * @param parentDirectory
	 * @param id
	 * @param bufferSize
	 * @return
	 */
	private static boolean decompress(ZipInputStream zipInputStream, File parentDirectory, ZipDecompressHandler zipDecompressHandler, int id, int bufferSize) {
		if ( null == zipDecompressHandler)
			return false;

		int[] range = new int[] { -1, -1};
		Map<String, Map<File, Long>> timeMap = new HashMap<String, Map<File, Long>>();
		try {
			try {
				ZipEntry zipEntry = null;
				while ( null != ( zipEntry = zipInputStream.getNextEntry())) {
					String filename = zipEntry.getName();
					filename = zipDecompressHandler.get_new_filename( id, filename);
					if ( null == filename) {
						zipInputStream.closeEntry();
						continue;
					}

					if ( zipEntry.isDirectory()) {
						File directory = new File( parentDirectory, filename);
						if ( !directory.mkdirs()) {
							zipInputStream.closeEntry();
							return false;
						}
						ZipUtility.make( directory, zipEntry.getTime(), timeMap, range);
					} else { 
						File file = new File( parentDirectory, filename);
						File directory = new File( file.getParent());
						if ( !directory.exists() && !directory.mkdirs()) {
							zipInputStream.closeEntry();
							return false;
						}

						FileOutputStream fileOutputStream = new FileOutputStream( file);

						byte[] buf = new byte[ bufferSize];
						BufferedInputStream bufferedInputStream = new BufferedInputStream( zipInputStream);

						int count;
						while ( -1 != ( count = bufferedInputStream.read( buf, 0, bufferSize)))
							fileOutputStream.write( buf, 0, count);

						fileOutputStream.close();

						ZipUtility.make( file, zipEntry.getTime(), timeMap, range);
					}
					zipInputStream.closeEntry();
				}
			} finally {
				//zipInputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		ZipUtility.update( timeMap, range);

		return true;
	}

	/**
	 * @param file
	 * @param time
	 * @param timeMap
	 * @param range
	 * @return
	 */
	public static boolean make(File file, long time, Map<String, Map<File, Long>> timeMap, int[] range) {
		String[] words = Tool.split( file.getAbsolutePath(), File.separatorChar);
		if ( null == words || 0 == words.length)
			return false;

		int number = words.length;
		if ( 0 > range[ 0] && 0 > range[ 1])
			range[ 0] = range[ 1] = number;
		else {
			range[ 0] = Math.min( number, range[ 0]);
			range[ 1] = Math.max( number, range[ 1]);
		}

		String key = String.valueOf( number);
		Map<File, Long> map = timeMap.get( key);
		if ( null == map) {
			map = new HashMap<File, Long>();
			timeMap.put( key, map);
		}

		map.put( file, new Long( time));

		return true;
	}

	/**
	 * @param timeMap
	 * @param range
	 */
	public static void update(Map<String, Map<File, Long>> timeMap, int[] range) {
		for ( int i = range[ 1]; range[ 0] <= i; --i) {
			Map<File, Long> map = timeMap.get( String.valueOf( i));
			if ( null == map)
				continue;

			File[] files = map.keySet().toArray( new File[ 0]);
			for ( File file:files) {
				Long time = map.get( file);
				if ( null == time)
					continue;

				file.setLastModified( time.longValue());
			}
		}
	}

	/**
	 * @param path
	 * @param filename
	 * @param encoding
	 * @return
	 */
	public static String get_text(File path, String filename, String encoding) {
		return get_text( path, filename, encoding, _bufferSize);
	}

	/**
	 * @param path
	 * @param filename
	 * @param encoding
	 * @param bufferSize
	 * @return
	 */
	public static String get_text(File path, String filename, String encoding, int bufferSize) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream( new FileInputStream( path));
			return get_text( zipInputStream, filename, encoding, bufferSize);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param zip
	 * @param filename
	 * @param encoding
	 * @return
	 */
	public static String get_text(byte[] zip, String filename, String encoding) {
		return get_text( zip, filename, encoding, _bufferSize);
	}

	/**
	 * @param zip
	 * @param filename
	 * @param encoding
	 * @param bufferSize
	 * @return
	 */
	public static String get_text(byte[] zip, String filename, String encoding, int bufferSize) {
		return get_text( new ZipInputStream( new ByteArrayInputStream( zip)), filename, encoding, bufferSize);
	}

	/**
	 * @param zipInputStream
	 * @param filename
	 * @param encoding
	 * @param bufferSize
	 * @return
	 */
	private static String get_text(ZipInputStream zipInputStream, String filename, String encoding, int bufferSize) {
		try {
			try {
				ZipEntry zipEntry = null;
				while ( null != ( zipEntry = zipInputStream.getNextEntry())) {
					if ( !filename.equals( zipEntry.getName())) {
						zipInputStream.closeEntry();
						continue;
					}

					if ( zipEntry.isDirectory()) {
						zipInputStream.closeEntry();
						return null;
					}

					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

					byte[] buf = new byte[ bufferSize];
					BufferedInputStream bufferedInputStream = new BufferedInputStream( zipInputStream);

					int count;
					while ( -1 != ( count = bufferedInputStream.read( buf, 0, bufferSize)))
						byteArrayOutputStream.write( buf, 0, count);

					byte[] data = byteArrayOutputStream.toByteArray();
					if ( null == data) {
						byteArrayOutputStream.close();
						zipInputStream.closeEntry();
						return null;
					}

					String text = new String( data, encoding);
					byteArrayOutputStream.close();
					zipInputStream.closeEntry();
					return text;
				}
			} finally {
				zipInputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	/**
	 * @param path
	 * @param filename
	 * @return
	 */
	public static byte[] get_binary(File path, String filename) {
		return get_binary( path, filename, _bufferSize);
	}

	/**
	 * @param path
	 * @param filename
	 * @param bufferSize
	 * @return
	 */
	public static byte[] get_binary(File path, String filename, int bufferSize) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream( new FileInputStream( path));
			return get_binary( zipInputStream, filename, bufferSize);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param zip
	 * @param filename
	 * @return
	 */
	public static byte[] get_binary(byte[] zip, String filename) {
		return get_binary( zip, filename, _bufferSize);
	}

	/**
	 * @param zip
	 * @param filename
	 * @param bufferSize
	 * @return
	 */
	public static byte[] get_binary(byte[] zip, String filename, int bufferSize) {
		return get_binary( new ZipInputStream( new ByteArrayInputStream( zip)), filename, bufferSize);
	}

	/**
	 * @param zipInputStream
	 * @param filename
	 * @param bufferSize
	 * @return
	 */
	private static byte[] get_binary(ZipInputStream zipInputStream, String filename, int bufferSize) {
		try {
			try {
				ZipEntry zipEntry = null;
				while ( null != ( zipEntry = zipInputStream.getNextEntry())) {
					if ( !filename.equals( zipEntry.getName())) {
						zipInputStream.closeEntry();
						continue;
					}

					if ( zipEntry.isDirectory()) {
						zipInputStream.closeEntry();
						return null;
					}

					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

					byte[] buf = new byte[ bufferSize];
					BufferedInputStream bufferedInputStream = new BufferedInputStream( zipInputStream);

					int count;
					while ( -1 != ( count = bufferedInputStream.read( buf, 0, bufferSize)))
						byteArrayOutputStream.write( buf, 0, count);

					byte[] data = byteArrayOutputStream.toByteArray();
					if ( null == data) {
						byteArrayOutputStream.close();
						zipInputStream.closeEntry();
						return null;
					}

					byteArrayOutputStream.close();
					zipInputStream.closeEntry();
					return data;
				}
			} finally {
				zipInputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	/**
	 * @param path
	 * @param filename
	 * @return
	 */
	public static BufferedImage get_image(File path, String filename) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream( new FileInputStream( path));
			return get_image( zipInputStream, filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param zip
	 * @param filename
	 * @return
	 */
	public static BufferedImage get_image(byte[] zip, String filename) {
		return get_image( new ZipInputStream( new ByteArrayInputStream( zip)), filename);
	}

	/**
	 * @param inputStream
	 * @param filename
	 * @return
	 */
	public static BufferedImage get_image(InputStream inputStream, String filename) {
		return get_image( new ZipInputStream( inputStream), filename);
	}

	/**
	 * @param zipInputStream
	 * @param filename
	 * @return
	 */
	private static BufferedImage get_image(ZipInputStream zipInputStream, String filename) {
		try {
			try {
				ZipEntry zipEntry = null;
				while ( null != ( zipEntry = zipInputStream.getNextEntry())) {
					if ( !filename.equals( zipEntry.getName())) {
						zipInputStream.closeEntry();
						continue;
					}

					if ( zipEntry.isDirectory()) {
						zipInputStream.closeEntry();
						return null;
					}

					try {
						BufferedImage bufferedImage = ImageIO.read( zipInputStream);
						zipInputStream.closeEntry();
						return bufferedImage;
					} catch (IOException e) {
						e.printStackTrace();
						zipInputStream.closeEntry();
						return null;
					} catch (Throwable e) {
						zipInputStream.closeEntry();
						return null;
					}
				}
			} finally {
				zipInputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	/**
	 * @param path
	 * @param filename
	 * @return
	 */
	public static boolean contains(File path, String filename) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream( new FileInputStream( path));
			return contains( zipInputStream, filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @param zip
	 * @param filename
	 * @return
	 */
	public static boolean contains(byte[] zip, String filename) {
		return contains( new ZipInputStream( new ByteArrayInputStream( zip)), filename);
	}

	/**
	 * @param inputStream
	 * @param filename
	 * @return
	 */
	public static boolean contains(InputStream inputStream, String filename) {
		return contains( new ZipInputStream( inputStream), filename);
	}

	/**
	 * @param zipInputStream
	 * @param string
	 * @return
	 */
	public static boolean contains(ZipInputStream zipInputStream, String filename) {
		try {
			try {
				ZipEntry zipEntry = null;
				while ( null != ( zipEntry = zipInputStream.getNextEntry())) {
					if ( filename.equals( zipEntry.getName())) {
						zipInputStream.closeEntry();
						return true;
					}
				}
			} finally {
				zipInputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}
}
