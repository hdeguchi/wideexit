/**
 * 
 */
package soars.application.animator.file.importer;

import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import soars.application.animator.common.tool.TimeComparator;
import soars.application.animator.main.Constant;
import soars.application.animator.main.internal.ObjectManager;
import soars.application.animator.object.file.FileObject;
import soars.application.animator.object.file.HeaderObject;
import soars.common.utility.tool.file.FileUtility;

/**
 * Makes Animator data from SOARS log files.
 * @author kurata / SOARS project
 */
public class Importer {

	/**
	 * Names of directories for SOARS log files.
	 */
	static public final String[] _subDirectoryNames = new String[] { "agents", "spots"};

	/**
	 * Informations of SOARS log files.
	 */
	private HeaderObject _headerObject = null; 

	/**
	 * 
	 */
	private ObjectManager _objectManager = null; 

	/**
	 * Array for the step strings of SOARS log.
	 */
	private String[] _steps = null;

	/**
	 * Returns the root directory for Animator data if it is created and all of SOARS log files are copied to it successfully.
	 * @param importDirectory the source directory which contains all of SOARS log files
	 * @param parentDirectory the directory in which the root directory is created
	 * @param graphicPropertiesFilename
	 * @param chartPropertiesFilename
	 * @param chartLogDirectory
	 * @return the root directory for Animator data if it is created and all of SOARS log files are copied to it successfully
	 */
	public static File copy(File importDirectory, File parentDirectory, String graphicPropertiesFilename, String chartPropertiesFilename, String chartLogDirectory) {
		File rootDirectory = new File( parentDirectory, Constant._animatorRootDirectoryName);
		if ( null == rootDirectory)
			return null;

		if ( !rootDirectory.mkdir())
			return null;

		if ( !copy( importDirectory, graphicPropertiesFilename, rootDirectory))
			return null;

		if ( !copy( importDirectory, chartPropertiesFilename, rootDirectory))
			return null;

		boolean empty = true;

		for ( int i = 0; i < _subDirectoryNames.length; ++i) {
			File srcDirectory = new File( importDirectory, _subDirectoryNames[ i]);
			if ( null == srcDirectory || !srcDirectory.exists() || !srcDirectory.isDirectory())
				continue;

			File[] files = srcDirectory.listFiles();
			if ( null == files || 0 == files.length)
				continue;

			File destDirectory = new File( rootDirectory, _subDirectoryNames[ i]);
			if ( !destDirectory.mkdir())
				return null;

			for ( int j = 0; j < files.length; ++j) {
				if ( !files[ j].isFile() || !files[ j].canRead() || !files[ j].getName().endsWith( FileObject._extension))
					continue;

				if ( !FileUtility.copy( files[ j], new File( destDirectory, files[ j].getName())))
					return null;

				empty = false;
			}
		}

		if ( !empty && !copy( new File( importDirectory, chartLogDirectory), new File( rootDirectory, chartLogDirectory)))
			return null;

		return ( empty ? null : rootDirectory);
	}

	/**
	 * @param importDirectory
	 * @param propertiesFilename
	 * @param rootDirectory
	 * @return
	 */
	private static boolean copy(File importDirectory, String propertiesFilename, File rootDirectory) {
		File file = new File( importDirectory, propertiesFilename);
		if ( null != file && file.exists() && file.canRead()) {
			if ( !FileUtility.copy( file, new File( rootDirectory, propertiesFilename)))
				return false;
		}
		return true;
	}

	/**
	 * @param srcDirectory
	 * @param destDirectory
	 * @return
	 */
	private static boolean copy(File srcDirectory, File destDirectory) {
		if ( null == srcDirectory || !srcDirectory.exists() || !srcDirectory.isDirectory())
			return true;

		File[] files = srcDirectory.listFiles();
		if ( null == files || 0 == files.length)
			return true;

		if ( !destDirectory.mkdir())
			return false;

		for ( int j = 0; j < files.length; ++j) {
			if ( !files[ j].isFile() || !files[ j].canRead() || !files[ j].getName().endsWith( "log"))
				continue;

			if ( !FileUtility.copy( files[ j], new File( destDirectory, files[ j].getName())))
				return false;
		}

		return true;
	}

	/**
	 * Constructs a Importer that is initialized with the root directory for Animator data.
	 * @param directory the root directory for Animator data
	 * @param objectManager
	 */
	public Importer(File directory, ObjectManager objectManager) {
		super();
		_headerObject = new HeaderObject();
		_headerObject._directory = directory;
		_objectManager = objectManager;
	}

	/**
	 * Returns true if Animator data is created from SOARS log files successfully.
	 * @param graphics2D the graphics object of JAVA
	 * @return true if Animator data is created from SOARS log files successfully
	 */
	public boolean execute(Graphics2D graphics2D) {
		if ( !read( graphics2D))
			return false;

		_headerObject._filenames = ( String[])_headerObject._fileObjectMap.keySet().toArray( new String[ 0]);
		_headerObject.set( _steps);
		_headerObject._indicesFile = new File( _headerObject._directory.getAbsolutePath() + "/" + HeaderObject._indicesFilenamePrefix + HeaderObject._indicesFilenameExtension);
		_objectManager._scenarioManager._headerObject = _headerObject;

		return true;
	}

	/**
	 * Returns true if the array for the step strings of SOARS log scraped successfully.
	 * @param graphics2D the graphics object of JAVA
	 * @return true if the array for the step strings of SOARS log scraped successfully
	 */
	private boolean read(Graphics2D graphics2D) {
		List<String> list = new ArrayList<String>();

		for ( int i = 0; i < _subDirectoryNames.length; ++i) {
			File file = new File( _headerObject._directory, _subDirectoryNames[ i]);
			if ( null != file) {
				File[] files = file.listFiles();
				if ( null != files) {
					for ( int j = 0; j < files.length; ++j) {
						if ( !files[ j].isFile() || !files[ j].canRead())
							continue;

						FileObject fileObject = new FileObject( _subDirectoryNames[ i], files[ j], _objectManager);
						if ( !fileObject.read( list, graphics2D))
							return false;

//						if ( fileObject._type.equals( "agents") && fileObject._name.equals( "$Spot"))
						if ( fileObject._type.equals( _objectManager._spotLog[ 0]) && fileObject._name.equals( _objectManager._spotLog[ 1]))
							_headerObject._max = fileObject._max;

						_headerObject._fileObjectMap.put( _subDirectoryNames[ i] + "/" + files[ j].getName(), fileObject);
					}
				}
			}
		}

		if ( list.isEmpty())
			_steps = new String[] { ""};
		else {
			String[] temporarySteps = ( String[])list.toArray( new String[ 0]);
			Arrays.sort( temporarySteps, new TimeComparator());
			_steps = new String[ temporarySteps.length + 1];
			_steps[ 0] = "";
			System.arraycopy( temporarySteps, 0, _steps, 1, temporarySteps.length);
		}

		_headerObject.set_last_time( _steps[ _steps.length - 1]);
//		_headerObject._last = _steps[ _steps.length - 1];

		return true;
	}
}
