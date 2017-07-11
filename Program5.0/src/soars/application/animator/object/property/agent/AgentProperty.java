/*
 * 2005/02/21
 */
package soars.application.animator.object.property.agent;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import soars.application.animator.object.property.base.PropertyBase;

/**
 * The agent property class.
 * @author kurata / SOARS project
 */
public class AgentProperty extends PropertyBase {

	/**
	 * Creates the instance of agent property class.
	 */
	public AgentProperty() {
		super();
	}
	
	/**
	 * Creates the instance of agent property class with the specified agent property.
	 * @param agentProperty the specified agent property
	 */
	public AgentProperty(AgentProperty agentProperty) {
		super(agentProperty);
	}

//	// From Loader
//	/* (Non Javadoc)
//	 * @see soars.application.animator.object.property.base.PropertyBase#setup(java.lang.String, java.awt.Graphics2D)
//	 */
//	public void setup(String value, Graphics2D graphics2D) {
//		super.setup( value, CommonProperty.get_instance()._agent_width, CommonProperty.get_instance()._agent_height, graphics2D);
//		draw();
//	}
//
//	// From AgentPropertyImporter
//	/* (Non Javadoc)
//	 * @see soars.application.animator.object.property.base.PropertyBase#setup(int[], java.lang.String, java.awt.Graphics2D)
//	 */
//	public void setup(int[] color, String value, Graphics2D graphics2D) {
//		super.setup( color, value, CommonProperty.get_instance()._agent_width, CommonProperty.get_instance()._agent_height, graphics2D);
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
//			CommonProperty.get_instance()._agent_width,
//			CommonProperty.get_instance()._agent_height,
//			graphics2D);
//		draw();
//	}
//
//	/* (Non Javadoc)
//	 * @see soars.application.animator.object.property.base.PropertyBase#update(soars.application.animator.scenario.property.base.PropertyBase, java.awt.Graphics2D)
//	 */
//	public void update(PropertyBase PropertyBase, Graphics2D graphics2D) {
//		super.update( PropertyBase, CommonProperty.get_instance()._agent_width, CommonProperty.get_instance()._agent_height, graphics2D);
//		draw();
//	}
//
//	/* (Non Javadoc)
//	 * @see soars.application.animator.object.property.base.PropertyBase#draw()
//	 */
//	public void draw() {
//		super.draw();
//		draw_default_image( CommonProperty.get_instance()._agent_width, CommonProperty.get_instance()._agent_height);
//	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.property.base.PropertyBase#draw_default_image(java.awt.Graphics2D, java.awt.Point, int, int)
	 */
	public void draw_default_image(Graphics2D graphics2D, Point position, int width, int height) {
		graphics2D.setColor( new Color( _imageR, _imageG, _imageB));
		graphics2D.fillOval( position.x, position.y, width, height);
	}

//	/* (Non Javadoc)
//	 * @see soars.application.animator.object.property.base.PropertyBase#draw_default_image(int, int)
//	 */
//	protected void draw_default_image(int width, int height) {
//		Graphics2D graphics2D = ( Graphics2D)_default_image.getGraphics();
//		graphics2D.setColor( new Color( _image_r, _image_g, _image_b));
//		graphics2D.fillOval( 0, 0, width, height);
//		graphics2D.dispose();
//	}
}
