/*
 * 2005/02/21
 */
package soars.application.animator.object.property.spot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import soars.application.animator.object.property.base.PropertyBase;

/**
 * The spot property class.
 * @author kurata / SOARS project
 */
public class SpotProperty extends PropertyBase {

	/**
	 * Creates the instance of spot property class.
	 */
	public SpotProperty() {
		super();
	}
	
	/**
	 * Creates the instance of spot property class with the specified spot property.
	 * @param spotProperty the specified spot property
	 */
	public SpotProperty(SpotProperty spotProperty) {
		super(spotProperty);
	}

//	// From Loader
//	/* (Non Javadoc)
//	 * @see soars.application.animator.object.property.base.PropertyBase#setup(java.lang.String, java.awt.Graphics2D)
//	 */
//	public void setup(String value, Graphics2D graphics2D) {
//		super.setup( value, CommonProperty.get_instance()._spot_width, CommonProperty.get_instance()._spot_height, graphics2D);
//		draw();
//	}
//
//	// From SpotPropertyImporter
//	/* (Non Javadoc)
//	 * @see soars.application.animator.object.property.base.PropertyBase#setup(int[], java.lang.String, java.awt.Graphics2D)
//	 */
//	public void setup(int[] color, String value, Graphics2D graphics2D) {
//		super.setup( color, value, CommonProperty.get_instance()._spot_width, CommonProperty.get_instance()._spot_height, graphics2D);
//		draw();
//	}
//
//	/* (Non Javadoc)
//	 * @see soars.application.animator.object.property.base.PropertyBase#update(boolean, int, int, int, int, int, int, java.lang.String, int, int, java.lang.String, java.awt.Graphics2D)
//	 */
//	public void update(
//		boolean visible,
//		int image_r,
//		int image_g,
//		int image_b,
//		int text_r,
//		int text_g,
//		int text_b,
//		String font_family,
//		int font_style,
//		int font_size,
//		String image_filename,
//		Graphics2D graphics2D) {
//		super.update(
//			visible,
//			image_r,
//			image_g,
//			image_b,
//			text_r,
//			text_g,
//			text_b,
//			font_family,
//			font_style,
//			font_size,
//			image_filename,
//			CommonProperty.get_instance()._spot_width,
//			CommonProperty.get_instance()._spot_height,
//			graphics2D);
//		draw();
//	}
//
//	/* (Non Javadoc)
//	 * @see soars.application.animator.object.property.base.PropertyBase#update(soars.application.animator.scenario.property.base.PropertyBase, java.awt.Graphics2D)
//	 */
//	public void update(PropertyBase propertyBase, Graphics2D graphics2D) {
//		super.update( propertyBase, CommonProperty.get_instance()._spot_width, CommonProperty.get_instance()._spot_height, graphics2D);
//		draw();
//	}
//
//	/* (Non Javadoc)
//	 * @see soars.application.animator.object.property.base.PropertyBase#draw()
//	 */
//	public void draw() {
//		super.draw();
//		draw_default_image( CommonProperty.get_instance()._spot_width, CommonProperty.get_instance()._spot_height);
//	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.property.base.PropertyBase#draw_default_image(java.awt.Graphics2D, java.awt.Point, int, int)
	 */
	public void draw_default_image(Graphics2D graphics2D, Point position, int width, int height) {
		graphics2D.setColor( new Color( _imageR, _imageG, _imageB));
		graphics2D.fill3DRect( position.x, position.y, width, height, true);
	}

//	/* (Non Javadoc)
//	 * @see soars.application.animator.object.property.base.PropertyBase#draw_default_image(int, int)
//	 */
//	protected void draw_default_image(int width, int height) {
//		Graphics2D graphics2D = ( Graphics2D)_default_image.getGraphics();
//		graphics2D.setColor( new Color( _image_r, _image_g, _image_b));
//		graphics2D.fill3DRect( 0, 0, width, height, true);
//		graphics2D.dispose();
//	}
}
