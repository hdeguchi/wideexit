/*
 * Created on 2005/11/11
 */
package soars.application.animator.common.image;

import java.awt.image.BufferedImage;
import java.io.File;

import soars.application.animator.main.Administrator;
import soars.common.utility.swing.image.ImageManager;

/**
 * The image hashtable(String[absolute path] - BufferedImage) for Animator.
 * @author kurata / SOARS project
 */
public class AnimatorImageManager extends ImageManager {

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
	 * The instance of AnimatorImageManager.
	 */
	static private AnimatorImageManager _animatorImageManager = null;

	/**
	 * The startup routine.
	 */
	static {
		startup();
	}

	/**
	 * Creates the instance of AnimatorImageManager, if it is null.
	 */
	private static void startup() {
		synchronized( _lock) {
			if ( null == _animatorImageManager) {
				_animatorImageManager = new AnimatorImageManager();
			}
		}
	}

	/**
	 * Returns the instance of AnimatorImageManager.
	 * @return the instance of AnimatorImageManager
	 */
	public static AnimatorImageManager get_instance() {
		if ( null == _animatorImageManager) {
			System.exit( 0);
		}

		return _animatorImageManager;
	}

	/**
	 * Creates the image hashtable(String[absolute path] - BufferedImage) for Animator.
	 */
	public AnimatorImageManager() {
		super();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.image.ImageManager#load_image(java.lang.String)
	 */
	public BufferedImage load_image(String filename) {
		if ( null == filename || filename.equals( ""))
			return null;

		File file = new File( Administrator.get_instance().get_image_directory(), filename);
		return super.load_image(file.getAbsolutePath());
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.image.ImageManager#load_image_from_resource(java.lang.String, java.lang.Class)
	 */
	public BufferedImage load_image_from_resource(String filename, Class cls) {
		if ( null == filename || filename.equals( ""))
			return null;

		File file = new File( Administrator.get_instance().get_image_directory(), filename);
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
			if ( !Administrator.get_instance().uses_this_image( file.getName()))
				remove( filenames[ i]);
		}
	}
}
