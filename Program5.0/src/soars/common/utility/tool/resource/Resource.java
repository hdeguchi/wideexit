/*
 * 2004/12/22
 */
package soars.common.utility.tool.resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * @author kurata
 */
public class Resource {

	/**
	 * 
	 */
	public Resource() {
		super();
	}

	/**
	 * @param filename
	 * @param cls
	 * @return
	 */
	public static BufferedImage load_image_from_resource(String filename, Class cls) {
		return load_image_from_resource( cls.getResource( filename));
	}

	/**
	 * @param url
	 * @return
	 */
	public static BufferedImage load_image_from_resource(URL url) {
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read( url);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Throwable e) {
			return null;
		}
		return bufferedImage;
	}

	/**
	 * @param filename
	 * @return
	 */
	public static BufferedImage load_image(String filename) {
		return load_image( new File( filename));
	}

	/**
	 * @param file
	 * @return
	 */
	public static BufferedImage load_image(File file) {
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read( file);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Throwable e) {
			return null;
		}
		return bufferedImage;
	}

	/**
	 * @param bufferedImage
	 * @param formatName
	 * @param filename
	 * @return
	 */
	public static boolean write_image(BufferedImage bufferedImage, String formatName, String filename) {
		return write_image( bufferedImage, formatName, new File( filename));
	}

	/**
	 * @param bufferedImage
	 * @param formatName
	 * @param file
	 * @return
	 */
	public static boolean write_image(BufferedImage bufferedImage, String formatName, File file) {
		try {
			ImageIO.write( bufferedImage, formatName, file);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (Throwable e) {
			return false;
		}
		return true;
	}

//	/**
//	 * @param filename
//	 * @return
//	 */
//	public static Image load_image(String filename) {
//		byte[] imageData = load_imageData( filename);
//		if ( null == imageData)
//			return null;
//
//		return Toolkit.getDefaultToolkit().createImage( imageData);
//	}
//
//	/**
//	 * @param filename
//	 * @return
//	 */
//	public static ImageIcon load_imageIcon(String filename) {
//		byte[] imageData = load_imageData( filename);
//		if ( null == imageData)
//			return null;
//
//		return new ImageIcon( imageData);
//	}
//
//	/**
//	 * @param filename
//	 * @return
//	 */
//	public static byte[] load_imageData(String filename) {
//		InputStream inputStream = Resource.class.getResourceAsStream( filename);
//		if ( null == inputStream)
//			return null;
//
//		byte[] imageData;
//		try {
//			int size = inputStream.available();
//			imageData = new byte[ size];
//			inputStream.read( imageData);
//			inputStream.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		}
//		return imageData;
//	}
}
