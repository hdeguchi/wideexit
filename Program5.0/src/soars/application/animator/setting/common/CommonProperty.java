/*
 * 2005/03/02
 */
package soars.application.animator.setting.common;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.common.utility.xml.sax.Writer;

/**
 * The common properties.
 * @author kurata / SOARS project
 */
public class CommonProperty {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private CommonProperty _commonProperty = null;

	/**
	 * Value for the velocity of the animation.
	 */
	public int _divide = 30;

	/**
	 * Minimum width of object(AgentObject or SpotObject).
	 */
	public int _minimumWidth = 1;

	/**
	 * Maximum width of object(AgentObject or SpotObject).
	 */
//	public int _maximumWidth = 500;

	/**
	 * Default agent width.
	 */
	public int _agentWidth = 20;

	/**
	 * Default agent height.
	 */
	public int _agentHeight = 20;

	/**
	 * Default spot width.
	 */
	public int _spotWidth = 30;

	/**
	 * Default spot height.
	 */
	public int _spotHeight = 30;

	/**
	 * Whether the agents on the spot are packed.
	 */
	public boolean _pack = false;

	/**
	 * Whether or not the animation is replayable.
	 */
	public boolean _repeat = false;

	/**
	 * 
	 */
	static {
		startup();
	}

	/**
	 * 
	 */
	private static void startup() {
		synchronized( _lock) {
			if ( null == _commonProperty) {
				_commonProperty = new CommonProperty();
			}
		}
	}

	/**
	 * Returns the instance of the common properties.
	 * @return the instance of the common properties
	 */
	public static CommonProperty get_instance() {
		if ( null == _commonProperty) {
			System.exit( 0);
		}

		return _commonProperty;
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
		_divide = 30;
		_minimumWidth = 1;
//		_maximumWidth = 500;
		_agentWidth = 20;
		_agentHeight = 20;
		_spotWidth = 30;
		_spotHeight = 30;
	}

	/**
	 * Returns true for writing all common property data successfully.
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing all common property data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(Writer writer) throws SAXException {
		writer.startElement( null, null, "agent_width", new AttributesImpl());
		String value = String.valueOf( _agentWidth);
		writer.characters( value.toCharArray(), 0, value.length());
		writer.endElement( null, null, "agent_width");

		writer.startElement( null, null, "agent_height", new AttributesImpl());
		value = String.valueOf( _agentHeight);
		writer.characters( value.toCharArray(), 0, value.length());
		writer.endElement( null, null, "agent_height");

		writer.startElement( null, null, "spot_width", new AttributesImpl());
		value = String.valueOf( _spotWidth);
		writer.characters( value.toCharArray(), 0, value.length());
		writer.endElement( null, null, "spot_width");

		writer.startElement( null, null, "spot_height", new AttributesImpl());
		value = String.valueOf( _spotHeight);
		writer.characters( value.toCharArray(), 0, value.length());
		writer.endElement( null, null, "spot_height");

//		writer.startElement( null, null, "minimum_width", new AttributesImpl());
//		value = String.valueOf( _minimum_width);
//		writer.characters( value.toCharArray(), 0, value.length());
//		writer.endElement( null, null, "minimum_width");

		writer.startElement( null, null, "velocity", new AttributesImpl());
		value = String.valueOf( _divide);
		writer.characters( value.toCharArray(), 0, value.length());
		writer.endElement( null, null, "velocity");

		return true;
	}

	/**
	 * Returns true for writing all common property graphic data successfully.
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing all common property graphic data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write_graphic_data(Writer writer) throws SAXException {
		writer.startElement( null, null, "agent_width", new AttributesImpl());
		String value = String.valueOf( _agentWidth);
		writer.characters( value.toCharArray(), 0, value.length());
		writer.endElement( null, null, "agent_width");

		writer.startElement( null, null, "agent_height", new AttributesImpl());
		value = String.valueOf( _agentHeight);
		writer.characters( value.toCharArray(), 0, value.length());
		writer.endElement( null, null, "agent_height");

		writer.startElement( null, null, "spot_width", new AttributesImpl());
		value = String.valueOf( _spotWidth);
		writer.characters( value.toCharArray(), 0, value.length());
		writer.endElement( null, null, "spot_width");

		writer.startElement( null, null, "spot_height", new AttributesImpl());
		value = String.valueOf( _spotHeight);
		writer.characters( value.toCharArray(), 0, value.length());
		writer.endElement( null, null, "spot_height");

//		writer.startElement( null, null, "minimum_width", new AttributesImpl());
//		value = String.valueOf( _minimum_width);
//		writer.characters( value.toCharArray(), 0, value.length());
//		writer.endElement( null, null, "minimum_width");

//		writer.startElement( null, null, "velocity", new AttributesImpl());
//		value = String.valueOf( _divide);
//		writer.characters( value.toCharArray(), 0, value.length());
//		writer.endElement( null, null, "velocity");

		return true;
	}
}
