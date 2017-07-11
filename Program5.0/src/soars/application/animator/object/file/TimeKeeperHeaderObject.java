/**
 * 
 */
package soars.application.animator.object.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Vector;

/**
 * @author kurata
 *
 */
public class TimeKeeperHeaderObject {

	/**
	 * Indices file.
	 */
	public File _indicesFile = null;

	/**
	 * The number of steps.
	 */
	public int _size = 0;

	/**
	 * Length of a record.
	 */
	public int _length = 0;

	/**
	 * Last step string.
	 */
	public String _last = "";

	/**
	 * @param headerObject
	 * 
	 */
	public TimeKeeperHeaderObject(HeaderObject headerObject) {
		super();
		_indicesFile = new File( headerObject._indicesFile.getAbsolutePath());
		_size = headerObject._size;
		_length = headerObject._length;
		_last = headerObject._last;
	}

	/**
	 * @param position
	 * @param size
	 * @param times
	 * @return
	 */
	public synchronized boolean read(int position, int size, Vector<String> times) {
		try {
			RandomAccessFile randomAccessFile = new RandomAccessFile( _indicesFile, "r");
			FileChannel fileChannel = randomAccessFile.getChannel();

			for ( long i = 0; i < size; ++i) {
				fileChannel.position( ( ( long)( position) + i) * _length);
				long day = randomAccessFile.readLong();
				int hour = randomAccessFile.readInt();
				int min = randomAccessFile.readInt();
				times.add( ( -1l == day) ? "" : ( String.valueOf( day) + "/" + String.format( "%02d", hour) + ":" + String.format( "%02d", min)));
			}

			randomAccessFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
