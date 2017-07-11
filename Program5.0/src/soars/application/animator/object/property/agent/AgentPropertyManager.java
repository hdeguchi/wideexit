/*
 * 2005/02/25
 */
package soars.application.animator.object.property.agent;

import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.xml.sax.SAXException;

import soars.application.animator.main.Environment;
import soars.application.animator.main.ResourceManager;
import soars.application.animator.object.property.base.PropertyBase;
import soars.application.animator.object.property.base.PropertyManager;
import soars.application.animator.object.transition.base.TransitionBase;
import soars.common.utility.xml.sax.Writer;

/**
 * The AgentProperty hashtable(name(String) - value(String) - AgentProperty).
 * @author kurata / SOARS project
 */
public class AgentPropertyManager extends PropertyManager {

	/**
	 * Creates the instance of the AgentProperty hasshtable.
	 */
	public AgentPropertyManager() {
		super();
	}

	/** Copy constructor(For duplication)
	 * @param agentPropertyManager
	 */
	public AgentPropertyManager(AgentPropertyManager agentPropertyManager) {
		super(agentPropertyManager);
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.property.base.PropertyManager#create(soars.application.animator.object.property.base.PropertyBase)
	 */
	public PropertyBase create(PropertyBase propertyBase) {
		return new AgentProperty( ( AgentProperty)propertyBase);
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.property.base.PropertyManager#append(java.util.TreeMap, java.lang.String, java.awt.Graphics2D)
	 */
	protected void append(TreeMap<String, PropertyBase> properties, String value, Graphics2D graphics2D) {
		AgentProperty agentProperty = new AgentProperty();
		properties.put( value, agentProperty);
		agentProperty.setup(
			new int[] {
				200 + ( int)( 55 * Math.random()),
				( int)( 255 * Math.random()),
				0},
			value, graphics2D);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.property.base.PropertyManager#setup_default_property_image(java.util.TreeMap, java.awt.Graphics2D)
	 */
	protected void setup_default_property_image(TreeMap<String, PropertyBase> properties, Graphics2D graphics2D) {
		Iterator iterator = properties.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String value = ( String)entry.getKey();
			PropertyBase propertyBase = ( PropertyBase)entry.getValue();
			propertyBase.setup(
				new int[] {
					200 + ( int)( 55 * Math.random()),
					( int)( 255 * Math.random()),
					0},
				value, graphics2D);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.property.base.PropertyManager#draw_current_property_image(java.awt.Graphics2D, java.awt.Point, int, int, java.util.Map, java.awt.Rectangle, java.awt.image.ImageObserver, boolean)
	 */
	public boolean draw_current_property_image(Graphics2D graphics2D, Point position, int width, int height, Map<String, String> properties, Rectangle rectangle, ImageObserver imageObserver, boolean imageExists) {
		boolean result = super.draw_current_property_image( graphics2D, position, width, height, properties, rectangle, imageObserver, imageExists);

		int y = position.y;
		for ( int i = 0; i < _selectedProperties.size(); ++i) {
			String value = properties.get( _selectedProperties.get( i));
			if ( null == value)
				value = "";

			AgentProperty agentProperty = ( AgentProperty)get( _selectedProperties.get( i), value);
			if ( null == agentProperty)
				continue;

			if ( agentProperty.draw_text( rectangle, graphics2D, position.x, y, imageObserver))
				result = true;

			y += agentProperty._textDimension.height;
//			graphics2D.drawImage( agentProperty._text_image, position.x, y, imageObserver);
//			y += agentProperty._text_image.getHeight();
		}

		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.property.base.PropertyManager#draw_current_property_image(java.awt.Graphics2D, java.awt.Point, int, int, soars.application.animator.object.transition.base.TransitionBase, java.awt.Rectangle, java.awt.image.ImageObserver, boolean)
	 */
	public boolean draw_current_property_image(Graphics2D graphics2D, Point position, int width, int height, TransitionBase transitionBase, Rectangle rectangle, ImageObserver imageObserver, boolean imageExists) {
		boolean result = super.draw_current_property_image( graphics2D, position, width, height, transitionBase, rectangle, imageObserver, imageExists);

		int y = position.y;
		for ( int i = 0; i < _selectedProperties.size(); ++i) {
			String value = ( null == transitionBase) 	? "" : transitionBase._properties.get( _selectedProperties.get( i));
			if ( null == value)
				value = "";

			AgentProperty agentProperty = ( AgentProperty)get( _selectedProperties.get( i), value);
			if ( null == agentProperty)
				continue;

			if ( agentProperty.draw_text( rectangle, graphics2D, position.x, y, imageObserver))
				result = true;

			y += agentProperty._textDimension.height;
//			graphics2D.drawImage( agentProperty._text_image, position.x, y, imageObserver);
//			y += agentProperty._text_image.getHeight();
		}

		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.property.base.PropertyManager#edit(java.lang.String, java.awt.Frame)
	 */
	public boolean edit(String titlePrefix, Frame frame) {
		return edit( titlePrefix + ResourceManager.get_instance().get( "edit.agent.properties.dialog.title"),
			Environment._openAgentImageDirectoryKey,
			frame);
	}

	/**
	 * Returns true for writing all agent property data successfully.
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing all agent property data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(Writer writer) throws SAXException {
		return write( writer, "agent");
	}

	/**
	 * Returns true for writing all agent property graphic data successfully.
	 * @param rootDirectory the root directory for Animator data
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing all agent property graphic data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write_graphic_data(File rootDirectory, Writer writer) throws SAXException {
		return write_graphic_data( rootDirectory, writer, "agent");
	}
}
