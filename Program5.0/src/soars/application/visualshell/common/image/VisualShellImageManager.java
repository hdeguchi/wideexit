/*
 * Created on 2005/11/11
 */
package soars.application.visualshell.common.image;

import java.awt.image.BufferedImage;
import java.io.File;

import soars.application.visualshell.layer.LayerManager;
import soars.common.utility.swing.image.ImageManager;

/**
 * The image hashtable(String[absolute path] - BufferedImage) for Visual Shell.
 * @author kurata / SOARS project
 */
public class VisualShellImageManager extends ImageManager {

	/**
	 * Maximum number of thumbnails on the thumbnail list.
	 */
	static public final int _thumbnailMax = 30;

	/**
	 * Size of ths thumbnail's maximum side.
	 */
	static public final int _thumbnailSize = 100;

	/**
	 * Synchronized object.
	 */
	static private Object _lock = new Object();

	/**
	 * The instance of VisualShellImageManager.
	 */
	static private VisualShellImageManager _visualShellImageManager = null;

	/**
	 * The startup routine.
	 */
	static {
		startup();
	}

	/**
	 * Creates the instance of VisualShellImageManager, if it is null.
	 */
	private static void startup() {
		synchronized( _lock) {
			if ( null == _visualShellImageManager) {
				_visualShellImageManager = new VisualShellImageManager();
			}
		}
	}

	/**
	 * Returns the instance of VisualShellImageManager.
	 * @return the instance of VisualShellImageManager
	 */
	public static VisualShellImageManager get_instance() {
		if ( null == _visualShellImageManager) {
			System.exit( 0);
		}

		return _visualShellImageManager;
	}

	/**
	 * Creates the image hashtable(String[absolute path] - BufferedImage) for Visual Shell.
	 */
	public VisualShellImageManager() {
		super();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.image.ImageManager#load_image(java.lang.String)
	 */
	public BufferedImage load_image(String filename) {
		if ( null == filename || filename.equals( ""))
			return null;

		File file = new File( LayerManager.get_instance().get_image_directory(), filename);
		return super.load_image(file.getAbsolutePath());
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.image.ImageManager#load_image_from_resource(java.lang.String, java.lang.Class)
	 */
	public BufferedImage load_image_from_resource(String filename, Class cls) {
		if ( null == filename || filename.equals( ""))
			return null;

		File file = new File( LayerManager.get_instance().get_image_directory(), filename);
		return super.load_image_from_resource(file.getAbsolutePath(), cls);
	}

	/**
	 * Removes the images form the hashtable, if they are not used.
	 */
	public void update() {
		if ( isEmpty())
			return;

		String[] filenames = ( String[])keySet().toArray( new String[ 0]);
		for ( int i = 0; i < filenames.length; ++i) {
			File file = new File( filenames[ i]);
			if ( !LayerManager.get_instance().uses_this_image( file.getName()))
				remove( filenames[ i]);
		}
	}
}
