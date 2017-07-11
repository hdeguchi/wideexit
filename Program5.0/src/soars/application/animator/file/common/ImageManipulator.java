/**
 * 
 */
package soars.application.animator.file.common;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.TreeMap;

import javax.swing.ImageIcon;

import soars.application.animator.common.image.AnimatorImageManager;
import soars.application.animator.main.Administrator;
import soars.application.animator.main.Constant;
import soars.application.animator.main.MainFrame;
import soars.application.animator.object.entity.agent.AgentObject;
import soars.application.animator.object.entity.spot.SpotObject;
import soars.application.animator.object.property.agent.AgentProperty;
import soars.application.animator.object.property.base.PropertyBase;
import soars.application.animator.object.property.spot.SpotProperty;
import soars.common.utility.swing.image.ImageProperty;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.resource.Resource;

/**
 * The common image manipulator for Animator.
 * @author kurata / SOARS project
 */
public class ImageManipulator {

	/**
	 * Returns true if the specified image is set to the specified object.
	 * @param object the specified object(AgentObject, SpotObject, AgentProperty or SpotProperty)
	 * @param src the specified image file
	 * @param imagePropertyMap the image property hashtable(String[filename] - ImageProperty)
	 * @return true if the specified image is set to the specified object
	 */
	public static boolean get_image(Object object, File src, TreeMap imagePropertyMap) {
		File dest = new File( Administrator.get_instance().get_image_directory(), src.getName());
		if ( !dest.exists()) {
			if ( !FileUtility.copy( src, dest))
				return get_default_image( object, src.getName(), imagePropertyMap);

			BufferedImage bufferedImage = AnimatorImageManager.get_instance().load_image( dest.getName());
			if ( null == bufferedImage) {
				dest.delete();
				return get_default_image( object, src.getName(), imagePropertyMap);
			} else {
				if ( AnimatorImageManager._thumbnailSize >= bufferedImage.getWidth()
					&& AnimatorImageManager._thumbnailSize >= bufferedImage.getHeight()) {
					if ( !FileUtility.copy( dest, new File( Administrator.get_instance().get_thumbnail_image_directory(), dest.getName()))) {
						dest.delete();
						return get_default_image( object, src.getName(), imagePropertyMap);
					}
				} else {
					if ( !make_thumbnail_imagefile( dest, bufferedImage)) {
						dest.delete();
						return get_default_image( object, src.getName(), imagePropertyMap);
					}
				}

				imagePropertyMap.put( dest.getName(), new ImageProperty( bufferedImage.getWidth(), bufferedImage.getHeight()));
			}
		}

		return load_image( object, dest.getName());
	}

	/**
	 * Returns true if the specified image is set to the specified object.
	 * @param object the specified object(AgentObject, SpotObject, AgentProperty or SpotProperty)
	 * @param imageFilename th image filename
	 * @return true if the specified image is set to the specified object
	 */
	private static boolean load_image(Object object, String imageFilename) {
		if ( object instanceof AgentObject) {
			AgentObject agentObject = ( AgentObject)object;
			return agentObject.load_image( imageFilename);
		} else if ( object instanceof SpotObject) {
			SpotObject spotObject = ( SpotObject)object;
			return spotObject.load_image( imageFilename);
		} else if ( object instanceof PropertyBase) {
			PropertyBase propertyBase = ( PropertyBase)object;
			return propertyBase.load_image( imageFilename);
		}
		return false;
	}

	/**
	 * Returns true if the specified thumbnail image file is created successfully.
	 * @param file the specified thumbnail image file
	 * @param bufferedImage the image from which the specified thumbnail one is created
	 * @return true if the specified thumbnail image file is created successfully
	 */
	private static boolean make_thumbnail_imagefile(File file, BufferedImage bufferedImage) {
		MediaTracker mediaTracker = new MediaTracker( MainFrame.get_instance());
	
		double ratio = ( ( double)AnimatorImageManager._thumbnailSize / ( double)Math.max( bufferedImage.getWidth(), bufferedImage.getHeight()));
		Image image = bufferedImage.getScaledInstance( ( int)( ( double)bufferedImage.getWidth() * ratio),
			( int)( ( double)bufferedImage.getHeight() * ratio), Image.SCALE_SMOOTH);
		mediaTracker.addImage( image, 0);
		ImageIcon imageIcon = new ImageIcon( image);
		try {
			mediaTracker.waitForID( 0);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}

		return make_thumbnail_imagefile( file, imageIcon);
	}

	/**
	 * Returns true if the specified thumbnail image file is created successfully.
	 * @param file the specified thumbnail image file
	 * @param imageIcon the specified thumbnail image
	 * @return true if the specified thumbnail image file is created successfully
	 */
	private static boolean make_thumbnail_imagefile(File file, ImageIcon imageIcon) {
		BufferedImage thumbnail_bufferedImage = new BufferedImage( imageIcon.getIconWidth(), imageIcon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics2D = ( Graphics2D)thumbnail_bufferedImage.getGraphics();
		graphics2D.drawImage( imageIcon.getImage(), 0, 0, null);
		return Resource.write_image( thumbnail_bufferedImage, "png", new File( Administrator.get_instance().get_thumbnail_image_directory(), file.getName()));
	}

	/**
	 * Returns true if the default image is set to the specified object.
	 * @param object the specified object(AgentObject, SpotObject, AgentProperty or SpotProperty)
	 * @param imageFilename the default image file
	 * @param imagePropertyMap the image property hashtable(String[filename] - ImageProperty)
	 * @return true if the default image is set to the specified object
	 */
	public static boolean get_default_image(Object object, String imageFilename, TreeMap imagePropertyMap) {
		File dest = new File( Administrator.get_instance().get_image_directory(), imageFilename);
		if ( !dest.exists()) {
			BufferedImage bufferedImage = Resource.load_image_from_resource(
				Constant._resourceDirectory + "/image/object/images/" + get_resource_filename( object), object.getClass());
			if ( null == bufferedImage)
				return false;

			if ( !Resource.write_image( bufferedImage, "png", dest))
				return false;

			bufferedImage = Resource.load_image_from_resource(
				Constant._resourceDirectory + "/image/object/thumbnails/" + get_resource_filename( object), object.getClass());
			if ( null == bufferedImage) {
				dest.delete();
				return false;
			}

			if ( !Resource.write_image( bufferedImage, "png", new File( Administrator.get_instance().get_thumbnail_image_directory(), imageFilename))) {
				dest.delete();
				return false;
			}

			imagePropertyMap.put( dest.getName(), new ImageProperty( bufferedImage.getWidth(), bufferedImage.getHeight()));
		}

		return load_image( object, dest.getName());
	}

	/**
	 * Returns the default image filename.
	 * @param object the specified object(AgentObject, SpotObject, AgentProperty or SpotProperty)
	 * @return the default image filename
	 */
	private static String get_resource_filename(Object object) {
		if ( object instanceof AgentObject) {
			return "agent.png";
		} else if ( object instanceof SpotObject) {
			return "spot.png";
		} else if ( object instanceof AgentProperty) {
			return "agent_property.png";
		} else if ( object instanceof SpotProperty) {
			return "spot_property.png";
		}
		return null;
	}
}
