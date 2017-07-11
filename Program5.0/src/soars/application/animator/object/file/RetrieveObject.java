/**
 * 
 */
package soars.application.animator.object.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * The object for retrieving.
 * @author kurata / SOARS project
 */
public class RetrieveObject {

	/**
	 * Index of the target log file.
	 */
	public int _index;

	/**
	 * FileObject of the target log file.
	 */
	public FileObject _fileObject = null;

	/**
	 * Index of the compared value in the specified line.
	 */
	public int _offset;

	/**
	 * Informations of SOARS log files.
	 */
	private HeaderObject _headerObject = null;

	/**
	 * Creates the object for retrieving.
	 * @param index the index of the target log file
	 * @param fileObject the FileObject of the target log file
	 * @param offset the index of the compared value in the specified line
	 * @param headerObject the informations of SOARS log files
	 */
	public RetrieveObject(int index, FileObject fileObject, int offset, HeaderObject headerObject) {
		super();
		_index = index;
		_fileObject = fileObject;
		_offset = offset;
		_headerObject = headerObject;
	}

	/**
	 * Returns true if the specified value is found backward.
	 * @param value the specified value
	 * @param start_position the start position
	 * @return true if the specified value is found backward
	 */
	public int retrieve_backward(String value, int start_position) {
		if ( !_fileObject.open())
			return -1;

		RandomAccessFile randomAccessFile;
		try {
			randomAccessFile = new RandomAccessFile( _headerObject._indicesFile, "r");
			FileChannel fileChannel = randomAccessFile.getChannel();

			long previous_position = -1l;
			for ( int i = start_position; i >= 0; --i) {
				fileChannel.position( ( ( long)i * _headerObject._length) + 8l + 4l + 4l + ( 8l * ( long)_index));
				long position = randomAccessFile.readLong();
				if ( position == previous_position)
					continue;

				if ( _fileObject.retrieve( position, _offset, value)) {
					randomAccessFile.close();
					_fileObject.close();
					return i;
				}

				previous_position = position;
			}

			randomAccessFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			_fileObject.close();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			_fileObject.close();
			return -1;
		}

		_fileObject.close();

		return -1;
	}

	/**
	 * Returns true if the value, which is not less than the specified value, is found backward.
	 * @param value the specified value
	 * @param startPosition the start position
	 * @return true if the value, which is not less than the specified value, is found backward
	 */
	public int retrieve_backward_more_than(double value, int startPosition) {
		if ( !_fileObject.open())
			return -1;

		RandomAccessFile randomAccessFile;
		try {
			randomAccessFile = new RandomAccessFile( _headerObject._indicesFile, "r");
			FileChannel fileChannel = randomAccessFile.getChannel();

			long previous_position = -1l;
			for ( int i = startPosition; i >= 0; --i) {
				fileChannel.position( ( ( long)i * _headerObject._length) + 8l + 4l + 4l + ( 8l * ( long)_index));
				long position = randomAccessFile.readLong();
				if ( position == previous_position)
					continue;

				if ( _fileObject.retrieve_more_than( position, _offset, value)) {
					randomAccessFile.close();
					_fileObject.close();
					return i;
				}

				previous_position = position;
			}

			randomAccessFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			_fileObject.close();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			_fileObject.close();
			return -1;
		}

		_fileObject.close();

		return -1;
	}

	/**
	 * Returns true if the value, which is not more than the specified value, is found backward.
	 * @param value the specified value
	 * @param startPosition the start position
	 * @return true if the value, which is not more than the specified value, is found backward
	 */
	public int retrieve_backward_less_than(double value, int startPosition) {
		if ( !_fileObject.open())
			return -1;

		RandomAccessFile randomAccessFile;
		try {
			randomAccessFile = new RandomAccessFile( _headerObject._indicesFile, "r");
			FileChannel fileChannel = randomAccessFile.getChannel();

			long previous_position = -1l;
			for ( int i = startPosition; i >= 0; --i) {
				fileChannel.position( ( ( long)i * _headerObject._length) + 8l + 4l + 4l + ( 8l * ( long)_index));
				long position = randomAccessFile.readLong();
				if ( position == previous_position)
					continue;

				if ( _fileObject.retrieve_less_than( position, _offset, value)) {
					randomAccessFile.close();
					_fileObject.close();
					return i;
				}

				previous_position = position;
			}

			randomAccessFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			_fileObject.close();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			_fileObject.close();
			return -1;
		}

		_fileObject.close();

		return -1;
	}

	/**
	 * Returns true if the value, which is not less than the value0 and not more than value1, is found backward.
	 * @param value0 the specified value
	 * @param value1 the specified value
	 * @param startPosition the start position
	 * @return true if the value, which is not less than the value0 and not more than value1, is found backward
	 */
	public int retrieve_backward_more_than_less_than(double value0, double value1, int startPosition) {
		if ( !_fileObject.open())
			return -1;

		RandomAccessFile randomAccessFile;
		try {
			randomAccessFile = new RandomAccessFile( _headerObject._indicesFile, "r");
			FileChannel fileChannel = randomAccessFile.getChannel();

			long previous_position = -1l;
			for ( int i = startPosition; i >= 0; --i) {
				fileChannel.position( ( ( long)i * _headerObject._length) + 8l + 4l + 4l + ( 8l * ( long)_index));
				long position = randomAccessFile.readLong();
				if ( position == previous_position)
					continue;

				if ( _fileObject.retrieve_more_than_less_than( position, _offset, value0, value1)) {
					randomAccessFile.close();
					_fileObject.close();
					return i;
				}

				previous_position = position;
			}

			randomAccessFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			_fileObject.close();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			_fileObject.close();
			return -1;
		}

		_fileObject.close();

		return -1;
	}

	/**
	 * Returns true if the specified value is found forward.
	 * @param value the specified value
	 * @param startPosition the start position
	 * @return true if the specified value is found forward
	 */
	public int retrieve_forward(String value, int startPosition) {
		if ( !_fileObject.open())
			return -1;

		RandomAccessFile randomAccessFile;
		try {
			randomAccessFile = new RandomAccessFile( _headerObject._indicesFile, "r");
			FileChannel fileChannel = randomAccessFile.getChannel();

			long previous_position = -1l;
			for ( int i = startPosition; i < _headerObject._size; ++i) {
				fileChannel.position( ( ( long)i * _headerObject._length) + 8l + 4l + 4l + ( 8l * ( long)_index));
				long position = randomAccessFile.readLong();
				if ( position == previous_position)
					continue;

				if ( _fileObject.retrieve( position, _offset, value)) {
					randomAccessFile.close();
					_fileObject.close();
					return i;
				}

				previous_position = position;
			}

			randomAccessFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			_fileObject.close();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			_fileObject.close();
			return -1;
		}

		_fileObject.close();

		return -1;
	}

	/**
	 * Returns true if the value, which is not less than the specified value, is found forward.
	 * @param value the specified value
	 * @param startPosition the start position
	 * @return true if the value, which is not less than the specified value, is found forward
	 */
	public int retrieve_forward_more_than(double value, int startPosition) {
		if ( !_fileObject.open())
			return -1;

		RandomAccessFile randomAccessFile;
		try {
			randomAccessFile = new RandomAccessFile( _headerObject._indicesFile, "r");
			FileChannel fileChannel = randomAccessFile.getChannel();

			long previous_position = -1l;
			for ( int i = startPosition; i < _headerObject._size; ++i) {
				fileChannel.position( ( ( long)i * _headerObject._length) + 8l + 4l + 4l + ( 8l * ( long)_index));
				long position = randomAccessFile.readLong();
				if ( position == previous_position)
					continue;

				if ( _fileObject.retrieve_more_than( position, _offset, value)) {
					randomAccessFile.close();
					_fileObject.close();
					return i;
				}

				previous_position = position;
			}

			randomAccessFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			_fileObject.close();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			_fileObject.close();
			return -1;
		}

		_fileObject.close();

		return -1;
	}

	/**
	 * Returns true if the value, which is not more than the specified value, is found forward.
	 * @param value the specified value
	 * @param startPosition the start position
	 * @return true if the value, which is not more than the specified value, is found forward
	 */
	public int retrieve_forward_less_than(double value, int startPosition) {
		if ( !_fileObject.open())
			return -1;

		RandomAccessFile randomAccessFile;
		try {
			randomAccessFile = new RandomAccessFile( _headerObject._indicesFile, "r");
			FileChannel fileChannel = randomAccessFile.getChannel();

			long previous_position = -1l;
			for ( int i = startPosition; i < _headerObject._size; ++i) {
				fileChannel.position( ( ( long)i * _headerObject._length) + 8l + 4l + 4l + ( 8l * ( long)_index));
				long position = randomAccessFile.readLong();
				if ( position == previous_position)
					continue;

				if ( _fileObject.retrieve_less_than( position, _offset, value)) {
					randomAccessFile.close();
					_fileObject.close();
					return i;
				}

				previous_position = position;
			}

			randomAccessFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			_fileObject.close();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			_fileObject.close();
			return -1;
		}

		_fileObject.close();

		return -1;
	}

	/**
	 * Returns true if the value, which is not less than the value0 and not more than value1, is found forward.
	 * @param value0 the specified value
	 * @param value1 the specified value
	 * @param startPosition the start position
	 * @return true if the value, which is not less than the value0 and not more than value1, is found forward
	 */
	public int retrieve_forward_more_than_less_than(double value0, double value1, int startPosition) {
		if ( !_fileObject.open())
			return -1;

		RandomAccessFile randomAccessFile;
		try {
			randomAccessFile = new RandomAccessFile( _headerObject._indicesFile, "r");
			FileChannel fileChannel = randomAccessFile.getChannel();

			long previous_position = -1l;
			for ( int i = startPosition; i < _headerObject._size; ++i) {
				fileChannel.position( ( ( long)i * _headerObject._length) + 8l + 4l + 4l + ( 8l * ( long)_index));
				long position = randomAccessFile.readLong();
				if ( position == previous_position)
					continue;

				if ( _fileObject.retrieve_more_than_less_than( position, _offset, value0, value1)) {
					randomAccessFile.close();
					_fileObject.close();
					return i;
				}

				previous_position = position;
			}

			randomAccessFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			_fileObject.close();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			_fileObject.close();
			return -1;
		}

		_fileObject.close();

		return -1;
	}
}
