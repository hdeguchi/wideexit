/*
 * Created on 2005/11/11
 */
package soars.common.utility.swing.image;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import soars.common.utility.tool.resource.Resource;

/**
 * The image hashtable(String[absolute path] - BufferedImage).
 * @author kurata / SOARS project
 */
public class ImageManager extends HashMap<String, Image> {

	/**
	 * Creates the image hashtable.
	 */
	public ImageManager() {
		super();
	}

	/**
	 * Returns the BufferedImage to which the specified filename is mapped in this hashtable. if it does not exist, creates it from filename, and maps it to which the specified filename is mapped in this hashtable.
	 * @param filename the specified String[absolute path]
	 * @return the BufferedImage to which the specified filename is mapped in this hashtable. if it does not exist, creates it from filename, and maps it to which the specified filename is mapped in this hashtable
	 */
	public BufferedImage load_image(String filename) {
		BufferedImage bufferedImage = ( BufferedImage)get( filename);
		if ( null != bufferedImage)
			return bufferedImage;

		bufferedImage = Resource.load_image( filename);
		if ( null == bufferedImage)
				return null;

		put( filename, bufferedImage);

		return bufferedImage;
	}

	/**
	 * Returns the BufferedImage to which the specified filename is mapped in this hashtable. if it does not exist, creates it from filename and cls, and maps it to which the specified filename is mapped in this hashtable.
	 * @param filename the specified String[absolute path]
	 * @param cls the specified Class
	 * @return the BufferedImage to which the specified filename is mapped in this hashtable. if it does not exist, creates it from filename and cls, and maps it to which the specified filename is mapped in this hashtable
	 */
	public BufferedImage load_image_from_resource(String filename, Class cls) {
		BufferedImage bufferedImage = ( BufferedImage)get( filename);
		if ( null != bufferedImage)
			return bufferedImage;

		bufferedImage = Resource.load_image_from_resource( filename, cls);
		if ( null == bufferedImage)
				return null;

		put( filename, bufferedImage);

		return bufferedImage;
	}

	/**
	 * Clears hashtable.
	 */
	public void cleanup() {
		clear();
	}
}
