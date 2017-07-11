/**
 * 
 */
package soars.application.animator.object.file;

import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.TreeMap;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.animator.main.internal.ObjectManager;
import soars.application.animator.object.entity.base.EntityBase;
import soars.application.animator.object.transition.agent.AgentTransitionManager;
import soars.application.animator.object.transition.spot.SpotTransitionManager;
import soars.common.utility.xml.sax.Writer;

/**
 * The informations of SOARS log files.
 * @author kurata / SOARS project
 */
public class HeaderObject {

	/**
	 * Indices file name prefix.
	 */
	static public final String _indicesFilenamePrefix = "indices";

	/**
	 * Indices file name extension.
	 */
	static public final String _indicesFilenameExtension = ".ndx";

	/**
	 * Root directory for Animator data.
	 */
	public File _directory = null;

	/**
	 * Indices file.
	 */
	public File _indicesFile = null;

	/**
	 * Log file names.
	 */
	public String[] _filenames = null;

	/**
	 * FileObject hashtable(file name - FileObject)
	 */
	public TreeMap<String, FileObject> _fileObjectMap = new TreeMap<String, FileObject>();

	/**
	 * Array for the step strings of SOARS log.
	 */
	public String[] _steps = null;

	/**
	 * The number of steps.
	 */
	public int _size = 0;

	/**
	 * Length of a record.
	 */
	public int _length = 0;

	/**
	 * Max number of agents.
	 */
	public int _max = 1;

	/**
	 * Last step string.
	 */
	public String _last = "";

	/**
	 * 
	 */
	public HeaderObject() {
		super();
	}

	/** Copy constructor(For duplication)
	 * @param headerObject
	 * @param objectManager
	 */
	public HeaderObject(HeaderObject headerObject, ObjectManager objectManager) {
		super();
		if ( null != headerObject._indicesFile)
			_indicesFile = new File( headerObject._indicesFile.getAbsolutePath());
		if ( null != headerObject._filenames) {
			_filenames = new String[ headerObject._filenames.length];
			System.arraycopy( headerObject._filenames, 0, _filenames, 0, headerObject._filenames.length);
		}
		for ( String filename:headerObject._filenames)
			_fileObjectMap.put( filename, new FileObject( headerObject._fileObjectMap.get( filename), objectManager));
		if ( null != headerObject._steps) {
			_steps = new String[ headerObject._steps.length];
			System.arraycopy( headerObject._steps, 0, _steps, 0, headerObject._steps.length);
		}
		_size = headerObject._size;
		_length = headerObject._length;
		_max = headerObject._max;
		_last = headerObject._last;
	}

	/**
	 * Returns true if the log files are opened successfully.
	 * @return true if the log files are opened successfully
	 */
	private boolean open() {
		for ( int i = 0; i < _filenames.length; ++i) {
			FileObject fileObject = _fileObjectMap.get( _filenames[ i]);
			if ( null == fileObject)
				return false;

			if ( !fileObject.open())
				return false;
		}
		return true;
	}

	/**
	 * Closes the log files.
	 */
	private void close() {
		for ( int i = 0; i < _filenames.length; ++i) {
			FileObject fileObject = _fileObjectMap.get( _filenames[ i]);
			if ( null == fileObject)
				continue;

			fileObject.close();
		}
	}

	/**
	 * @param last
	 */
	public void set_last_time(String last) {
		_last = last;
//		_lastTime = CommonTool.time_to_double( _last);
	}

	/**
	 * Sets the number of steps and the length of a record
	 * @param steps the array for the step strings of the log file
	 */
	public void set(String[] steps) {
		_steps = steps;
		_size = steps.length;
		_length = ( 8 + 4 + 4 + ( 8 * _filenames.length));
	}

	/**
	 * Returns true for loading the values successfully.
	 * @param graphics2D
	 * @return true for loading the values successfully
	 */
	public synchronized boolean load(Graphics2D graphics2D) {
		if ( !open()) {
			close();
			return false;
		}

		try {
			RandomAccessFile randomAccessFile = new RandomAccessFile( _indicesFile, "r");
			FileChannel fileChannel = randomAccessFile.getChannel();

			fileChannel.position( 0l);

			long day = randomAccessFile.readLong();
			int hour = randomAccessFile.readInt();
			int min = randomAccessFile.readInt();
			if ( -1l != day) {
				randomAccessFile.close();
				close();
				return false;
			}

			for ( int i = 0; i < _filenames.length; ++i) {
				FileObject fileObject = _fileObjectMap.get( _filenames[ i]);
				if ( !fileObject.load( randomAccessFile.readLong(), graphics2D)) {
					randomAccessFile.close();
					close();
					return false;
				}
			}

			randomAccessFile.close();
		} catch (FileNotFoundException e) {
			close();
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			close();
			e.printStackTrace();
			return false;
		}

		close();

		return true;
	}

	/**
	 * Returns true for reading the values successfully.
	 * @param position the start position
	 * @param size the size of record
	 * @param times the array for the step strings
	 * @param agentTransitionManager the scenario data manager of agents
	 * @param spotTransitionManager the scenario data manager of spots
	 * @param graphics2D
	 * @return true for reading the values successfully
	 */
	public synchronized boolean read(int position, int size, Vector<String> times, AgentTransitionManager agentTransitionManager, SpotTransitionManager spotTransitionManager, Graphics2D graphics2D) {
		if ( !open()) {
			close();
			return false;
		}

		try {
			RandomAccessFile randomAccessFile = new RandomAccessFile( _indicesFile, "r");
			FileChannel fileChannel = randomAccessFile.getChannel();

			fileChannel.position( ( long)position * _length);

			for ( int i = 0; i < size; ++i) {
				agentTransitionManager.append();
				spotTransitionManager.append();
				long day = randomAccessFile.readLong();
				int hour = randomAccessFile.readInt();
				int min = randomAccessFile.readInt();
				times.add( ( -1l == day) ? "" : ( String.valueOf( day) + "/" + String.format( "%02d", hour) + ":" + String.format( "%02d", min)));
				for ( int j = 0; j < _filenames.length; ++j) {
					FileObject fileObject = _fileObjectMap.get( _filenames[ j]);
					if ( !fileObject.read( i, randomAccessFile.readLong(), agentTransitionManager, spotTransitionManager, graphics2D)) {
						randomAccessFile.close();
						close();
						return false;
					}
				}
			}

			randomAccessFile.close();
		} catch (FileNotFoundException e) {
			close();
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			close();
			e.printStackTrace();
			return false;
		}

		close();

		return true;
	}

	/**
	 * Returns true if the specified value is found backward.
	 * @param entityBase the specified object
	 * @param directory
	 * @param name the specified name
	 * @param value the specified value
	 * @param startPosition the start position
	 * @return true if the specified value is found backward
	 */
	public int retrieve_backward(EntityBase entityBase, String directory, String name, String value, int startPosition) {
		if ( 0 > startPosition)
			return -1;

		RetrieveObject retrieveObject = create_retrieveObject( entityBase, directory, name);
		if ( null == retrieveObject)
			return -1;

		return retrieveObject.retrieve_backward( value, startPosition);
	}

	/**
	 * Returns true if the value, which is not less than the specified value, is found backward.
	 * @param entityBase the specified object
	 * @param directory
	 * @param name the specified name
	 * @param value the specified value
	 * @param startPosition the start position
	 * @return true if the value, which is not less than the specified value, is found backward
	 */
	public int retrieve_backward_more_than(EntityBase entityBase, String directory, String name, double value, int startPosition) {
		if ( 0 > startPosition)
			return -1;

		RetrieveObject retrieveObject = create_retrieveObject( entityBase, directory, name);
		if ( null == retrieveObject)
			return -1;

		return retrieveObject.retrieve_backward_more_than( value, startPosition);
	}

	/**
	 * Returns true if the value, which is not more than the specified value, is found backward.
	 * @param entityBase the specified object
	 * @param directory
	 * @param name the specified name
	 * @param value the specified value
	 * @param startPosition the start position
	 * @return true if the value, which is not more than the specified value, is found backward
	 */
	public int retrieve_backward_less_than(EntityBase entityBase, String directory, String name, double value, int startPosition) {
		if ( 0 > startPosition)
			return -1;

		RetrieveObject retrieveObject = create_retrieveObject( entityBase, directory, name);
		if ( null == retrieveObject)
			return -1;

		return retrieveObject.retrieve_backward_less_than( value, startPosition);
	}

	/**
	 * Returns true if the value, which is not less than the value0 and not more than value1, is found backward.
	 * @param entityBase the specified object
	 * @param directory
	 * @param name the specified name
	 * @param value0 the specified value
	 * @param value1 the specified value
	 * @param startPosition the start position
	 * @return true if the value, which is not less than the value0 and not more than value1, is found backward
	 */
	public int retrieve_backward_more_than_less_than(EntityBase entityBase, String directory, String name, double value0, double value1, int startPosition) {
		if ( 0 > startPosition)
			return -1;

		RetrieveObject retrieveObject = create_retrieveObject( entityBase, directory, name);
		if ( null == retrieveObject)
			return -1;

		return retrieveObject.retrieve_backward_more_than_less_than( value0, value1, startPosition);
	}

	/**
	 * Returns true if the specified value is found forward.
	 * @param entityBase the specified object
	 * @param directory
	 * @param name the specified name
	 * @param value the specified value
	 * @param startPosition the start position
	 * @return true if the specified value is found forward
	 */
	public int retrieve_forward(EntityBase entityBase, String directory, String name, String value, int startPosition) {
		if ( _size <= startPosition)
			return -1;

		RetrieveObject retrieveObject = create_retrieveObject( entityBase, directory, name);
		if ( null == retrieveObject)
			return -1;

		return retrieveObject.retrieve_forward( value, startPosition);
	}

	/**
	 * Returns true if the value, which is not less than the specified value, is found forward.
	 * @param entityBase the specified object
	 * @param directory
	 * @param name the specified name
	 * @param value the specified value
	 * @param startPosition the start position
	 * @return true if the value, which is not less than the specified value, is found forward
	 */
	public int retrieve_forward_more_than(EntityBase entityBase, String directory, String name, double value, int startPosition) {
		if ( _size <= startPosition)
			return -1;

		RetrieveObject retrieveObject = create_retrieveObject( entityBase, directory, name);
		if ( null == retrieveObject)
			return -1;

		return retrieveObject.retrieve_forward_more_than( value, startPosition);
	}

	/**
	 * Returns true if the value, which is not more than the specified value, is found forward.
	 * @param entityBase the specified object
	 * @param directory
	 * @param name the specified name
	 * @param value the specified value
	 * @param startPosition the start position
	 * @return true if the value, which is not more than the specified value, is found forward
	 */
	public int retrieve_forward_less_than(EntityBase entityBase, String directory, String name, double value, int startPosition) {
		if ( _size <= startPosition)
			return -1;

		RetrieveObject retrieveObject = create_retrieveObject( entityBase, directory, name);
		if ( null == retrieveObject)
			return -1;

		return retrieveObject.retrieve_forward_less_than( value, startPosition);
	}

	/**
	 * Returns true if the value, which is not less than the value0 and not more than value1, is found forward.
	 * @param entityBase the specified object
	 * @param directory
	 * @param name the specified name
	 * @param value0 the specified value
	 * @param value1 the specified value
	 * @param startPosition the start position
	 * @return true if the value, which is not less than the value0 and not more than value1, is found forward
	 */
	public int retrieve_forward_more_than_less_than(EntityBase entityBase, String directory, String name, double value0, double value1, int startPosition) {
		if ( _size <= startPosition)
			return -1;

		RetrieveObject retrieveObject = create_retrieveObject( entityBase, directory, name);
		if ( null == retrieveObject)
			return -1;

		return retrieveObject.retrieve_forward_more_than_less_than( value0, value1, startPosition);
	}

	/**
	 * @param entityBase
	 * @param directory
	 * @param name
	 * @return
	 */
	private RetrieveObject create_retrieveObject(EntityBase entityBase, String directory, String name) {
		String filename = directory + name + "." + FileObject._extension;
		FileObject fileObject = _fileObjectMap.get( filename);
		if ( null == fileObject)
			return null;

		int index = get( filename);
		if ( 0 > index)
			return null;

		int offset = fileObject.get( entityBase._name);
		if ( 0 > offset)
			return null;

		return new RetrieveObject( index, fileObject, offset, this);
	}

	/**
	 * @param filename
	 * @return
	 */
	private int get(String filename) {
		for ( int i = 0; i < _filenames.length; ++i) {
			if ( _filenames[ i].equals( filename))
				return i;
		}
		return -1;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write_common_header(Writer writer) throws SAXException {
		writer.startElement( null, null, "header", new AttributesImpl());

		for ( int i = 0; i < _filenames.length; ++i) {
			FileObject fileObject = _fileObjectMap.get( _filenames[ i]);
			if ( null == fileObject)
				return false;

			if ( !fileObject.write( _filenames[ i], writer))
				return false;
		}

		writer.endElement( null, null, "header");

		return true;
	}

	/**
	 * Returns true for writing the informations successfully.
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing the informations successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "size", "", String.valueOf( _size));
		attributesImpl.addAttribute( null, null, "length", "", String.valueOf( _length));
		attributesImpl.addAttribute( null, null, "max", "", String.valueOf( _max));
		attributesImpl.addAttribute( null, null, "last", "", Writer.escapeAttributeCharData( _last));
		writer.writeElement( null, null, "header", attributesImpl);
		return true;
	}
}
