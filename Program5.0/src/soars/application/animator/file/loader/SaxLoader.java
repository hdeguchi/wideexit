/*
 * 2005/04/22
 */
package soars.application.animator.file.loader;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.File;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import soars.application.animator.main.Constant;
import soars.application.animator.main.internal.AnimatorViewFrame;
import soars.application.animator.main.internal.ObjectManager;
import soars.application.animator.main.internal.WindowProperty;
import soars.application.animator.object.chart.AnimatorInternalChartFrame;
import soars.application.animator.object.chart.ChartData;
import soars.application.animator.object.chart.ChartDataPair;
import soars.application.animator.object.chart.ChartObject;
import soars.application.animator.object.chart.ChartObjectMap;
import soars.application.animator.object.entity.agent.AgentObject;
import soars.application.animator.object.entity.spot.SpotObject;
import soars.application.animator.object.file.FileObject;
import soars.application.animator.object.file.HeaderObject;
import soars.application.animator.object.property.agent.AgentProperty;
import soars.application.animator.object.property.base.PropertyBase;
import soars.application.animator.object.property.spot.SpotProperty;
import soars.application.animator.setting.common.CommonProperty;
import soars.common.utility.swing.image.ImageProperty;
import soars.common.utility.swing.image.ImagePropertyManager;

/**
 * The XML SAX loader for Animator data.
 * @author kurata / SOARS project
 */
public class SaxLoader extends DefaultHandler {

	/**
	 * Root directory for Animator data file.
	 */
	private String _directory = "";

	/**
	 * 
	 */
	private ObjectManager _objectManager = null;

	/**
	 * Graphics object of JAVA.
	 */
	private Graphics2D _graphics2D = null;

	/**
	 * True while reading the informations of SOARS log files.
	 */
	private boolean _header = false;

	/**
	 * True while reading the data of the properties for SOARS.
	 */
	private boolean _property = false;

	/**
	 * True while reading the data of the SOARS objects.
	 */
	private boolean _object = false;

	/**
	 * True while reading the informations of image files.
	 */
	private boolean _image = false;

	/**
	 * String in which XML string is stored temporarily.
	 */
	private String _value = null;

	/**
	 * Array of SOARS log filenames.
	 */
	private Vector<String> _filenames = new Vector<String>();

	/**
	 * String in which the name of the property for SOARS is stored temporarily.
	 */
	private String _name = null;

	/**
	 * SpotProperty hashtable(String[name] - SpotProperty)
	 */
	private TreeMap<String, PropertyBase> _spotPropertyMap = null;

	/**
	 * Array of the names of visible spot properties.
	 */
	private Vector<String> _selectedSpotProperties = null;

	/**
	 * AgentProperty hashtable(String[name] - AgentProperty)
	 */
	private TreeMap<String, PropertyBase> _agentPropertyMap = null;

	/**
	 * Array of the names of visible agent properties.
	 */
	private Vector<String> _selectedAgentProperties = null;

	/**
	 * Temporary SpotObject.
	 */
	private SpotObject _spotObject = null;

	/**
	 * Temporary AgentObject.
	 */
	private AgentObject _agentObject = null;

	/**
	 * Informations of SOARS log files.
	 */
	public HeaderObject _headerObject = new HeaderObject(); 

	/**
	 * ImageProperty hashtable(String[filename] - ImageProperty)
	 */
	private TreeMap<String, ImageProperty> _imagePropertyMap = new TreeMap<String, ImageProperty>();

	/**
	 * ChartObject hashtable(String[name] - ChartObject)
	 */
	private ChartObjectMap _chartObjectMap = new ChartObjectMap();

	/**
	 * 
	 */
	private ChartObject _chartObject = null;

	/**
	 * False if error occurred.
	 */
	static private boolean _result;

	/**
	 * Returns true if the loading the specified file is completed successfully.
	 * @param file the specified XML file of Animator data
	 * @param fileProperties
	 * @param objectManager
	 * @param graphics2D the graphics object of JAVA
	 * @return true if the loading the specified file is completed successfully
	 */
	public static boolean execute(File file, Vector<FileProperty> fileProperties, ObjectManager objectManager, Graphics2D graphics2D) {
		_result = false;
		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			SaxLoader saxLoader = new SaxLoader( file.getAbsoluteFile().getParentFile().getAbsolutePath(), fileProperties, objectManager, graphics2D);
			saxParser.parse( file, saxLoader);
			saxLoader.at_end_of_load();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return _result;
	}

	/**
	 * Returns true if the loading the specified file is completed successfully.
	 * @param file the specified XML file of Animator data
	 * @param objectManager
	 * @param graphics2D the graphics object of JAVA
	 * @return true if the loading the specified file is completed successfully
	 */
	public static boolean execute(File file, ObjectManager objectManager, Graphics2D graphics2D) {
		_result = false;
		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			SaxLoader saxLoader = new SaxLoader( file.getAbsoluteFile().getParentFile().getAbsolutePath(), objectManager, graphics2D);
			saxParser.parse( file, saxLoader);
			saxLoader.at_end_of_load();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return _result;
	}

	/**
	 * Invoked at the end of the loading.
	 */
	private void at_end_of_load() {
		if ( !_result)
			return;

		_headerObject._indicesFile = new File( _directory + "/" + HeaderObject._indicesFilenamePrefix + HeaderObject._indicesFilenameExtension);
		for ( int i = 0; i < _headerObject._filenames.length; ++i) {
			FileObject fileObject = _headerObject._fileObjectMap.get( _headerObject._filenames[ i]);
			fileObject._file = new File( _directory + "/" + _headerObject._filenames[ i]);
			if ( !fileObject.setup()) {
				_result = false;
				return;
			}
		}

		if ( !_objectManager._scenarioManager.load( _headerObject)) {
			_result = false;
			return;
		}

		_objectManager._agentObjectManager.arrange();

		if ( !_chartObjectMap.isEmpty() && !ChartObjectMap.get_instance().set( _chartObjectMap, new File( _directory + "/" + Constant._chartLogDirectory))) {
			_result = false;
			return;
		}
	}

	/**
	 * Creates the XML SAX loader for Animator data.
	 * @param directory the root directory for Animator data
	 * @param fileProperties
	 * @param objectManager
	 * @param graphics2D the graphics object of JAVA
	 */
	public SaxLoader(String directory, Vector<FileProperty> fileProperties, ObjectManager objectManager, Graphics2D graphics2D) {
		super();
		_directory = directory;
		_objectManager = objectManager;
		_graphics2D = graphics2D;
		_headerObject._directory = new File( _directory);
		_headerObject._filenames = new String[ fileProperties.size()];
		for ( int i = 0; i < fileProperties.size(); ++i) {
			_filenames.add( fileProperties.get( i)._filename);
			_headerObject._filenames[ i] = fileProperties.get( i)._filename;
			_headerObject._fileObjectMap.put( fileProperties.get( i)._filename, new FileObject( fileProperties.get( i)._type, fileProperties.get( i)._name, fileProperties.get( i)._header, _objectManager));
		}
	}

	/**
	 * Creates the XML SAX loader for Animator data.
	 * @param directory the root directory for Animator data
	 * @param objectManager
	 * @param graphics2D the graphics object of JAVA
	 */
	public SaxLoader(String directory, ObjectManager objectManager, Graphics2D graphics2D) {
		super();
		_directory = directory;
		_objectManager = objectManager;
		_graphics2D = graphics2D;
		_headerObject._directory = new File( _directory);
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(
		String arg0,
		String arg1,
		String arg2,
		Attributes arg3)
		throws SAXException {

		super.startElement(arg0, arg1, arg2, arg3);

		if ( arg2.equals( "animation_data")) {
			_result = true;
			return;
		}

		if ( !_result)
			return;

		if ( arg2.equals( "header")) {
			on_header( arg3);
		} else if ( arg2.equals( "file")) {
			on_file( arg3);
		} else if ( arg2.equals( "property")) {
			on_property( arg3);
		} else if ( arg2.equals( "object")) {
			on_object( arg3);
		} else if ( arg2.equals( "agent_width")) {
			on_agent_width( arg3);
		} else if ( arg2.equals( "agent_height")) {
			on_agent_height( arg3);
		} else if ( arg2.equals( "spot_width")) {
			on_spot_width( arg3);
		} else if ( arg2.equals( "spot_height")) {
			on_spot_height( arg3);
		} else if ( arg2.equals( "minimum_width")) {
			on_minimum_width( arg3);
		} else if ( arg2.equals( "velocity")) {
			on_velocity( arg3);
		} else if ( arg2.equals( "window")) {
			on_window( arg3);
		} else if ( arg2.equals( "spot_log")) {
			on_spot_log( arg3);
		} else if ( arg2.equals( "selected_spot_property")) {
			on_selected_spot_property( arg3);
		} else if ( arg2.equals( "selected_agent_property")) {
			on_selected_agent_property( arg3);
		} else if ( arg2.equals( "spot")) {
			on_spot( arg3);
		} else if ( arg2.equals( "agent")) {
			on_agent( arg3);
		} else if ( arg2.equals( "image")) {
			on_image( arg3);
		} else if ( arg2.equals( "data")) {
			on_data( arg3);
		} else if ( arg2.equals( "chart")) {
			on_chart( arg3);
		} else if ( arg2.equals( "chart_frame")) {
			on_chart_frame( arg3);
		} else if ( arg2.equals( "chart_data")) {
			on_chart_data( arg3);
		}
	}

	/**
	 * Invoked at the head of the "header" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_header(Attributes attributes) {
		String value = attributes.getValue( "size");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}
		try {
			_headerObject._size = Integer.parseInt( value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}

		value = attributes.getValue( "length");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}
		try {
			_headerObject._length = Integer.parseInt( value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}

		value = attributes.getValue( "max");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}
		try {
			_headerObject._max = Integer.parseInt( value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}

		value = attributes.getValue( "last");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}
		_headerObject.set_last_time( value);
//		_headerObject._last = attributes.getValue( "last");
//		if ( null == _headerObject._last || _headerObject._last.equals( "")) {
//			_result = false;
//			return;
//		}

		_header = true;
	}

	/**
	 * Invoked at the head of "file" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_file(Attributes attributes) {
		String filename = attributes.getValue( "filename");
		if ( null == filename || filename.equals( "")) {
			_result = false;
			return;
		}

		String type = attributes.getValue( "type");
		if ( null == type || type.equals( "")) {
			_result = false;
			return;
		}

		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "header");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}

		long header;
		try {
			header = Long.parseLong( value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}

		_filenames.add( filename);
		_headerObject._fileObjectMap.put( filename, new FileObject( type, name, header, _objectManager));
	}

	/**
	 * Invoked at the head of "property" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_property(Attributes attributes) {
		_property = true;
	}

	/**
	 * Invoked at the head of "object" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_object(Attributes attributes) {
		_object = true;
	}

	/**
	 * Invoked at the head of "agent_width" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_agent_width(Attributes attributes) {
		_value = "";
	}

	/**
	 * Invoked at the head of "agent_height" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_agent_height(Attributes attributes) {
		_value = "";
	}

	/**
	 * Invoked at the head of "spot_width" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_spot_width(Attributes attributes) {
		_value = "";
	}

	/**
	 * Invoked at the head of "spot_height" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_spot_height(Attributes attributes) {
		_value = "";
	}

	/**
	 * Invoked at the head of "minimum_width" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_minimum_width(Attributes attributes) {
		_value = "";
	}

	/**
	 * Invoked at the head of "velocity" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_velocity(Attributes attributes) {
		_value = "";
	}

	/**
	 * @param attributes
	 */
	private void on_window(Attributes attributes) {
		int x = 0;
		String attribute = attributes.getValue( "x");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				x = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		int y = 0;
		attribute = attributes.getValue( "y");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				y = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		int width = AnimatorViewFrame._minimumWidth;
		attribute = attributes.getValue( "width");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				width = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		int height = AnimatorViewFrame._minimumHeight;
		attribute = attributes.getValue( "height");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				height = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		_objectManager._windowProperty._rectangle.x = x;
		_objectManager._windowProperty._rectangle.y = y;
		_objectManager._windowProperty._rectangle.width = ( ( AnimatorViewFrame._minimumWidth > width) ? AnimatorViewFrame._minimumWidth : width);
		_objectManager._windowProperty._rectangle.height = ( ( AnimatorViewFrame._minimumHeight > height) ? AnimatorViewFrame._minimumHeight : height);

		attribute = attributes.getValue( "maximum");
		if ( null != attribute && !attribute.equals( ""))
			_objectManager._windowProperty._maximum = ( attribute.equals( "true")) ? true : false;

		attribute = attributes.getValue( "icon");
		if ( null != attribute && !attribute.equals( ""))
			_objectManager._windowProperty._icon = ( attribute.equals( "true")) ? true : false;

		int order = -1;
		attribute = attributes.getValue( "order");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				order = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		_objectManager._windowProperty._order = order;
	}

	/**
	 * Invoked at the head of "spot_log" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_spot_log(Attributes attributes) {
		String type = attributes.getValue( "type");
		if ( null == type || type.equals( "")) {
			_result = false;
			return;
		}

		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		_objectManager._spotLog[ 0] = type;
		_objectManager._spotLog[ 1] = name;
	}

	/**
	 * Invoked at the head of "selected_spot_property" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_selected_spot_property(Attributes attributes) {
		_selectedSpotProperties = new Vector<String>();
	}

	/**
	 * Invoked at the head of "selected_agent_property" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_selected_agent_property(Attributes attributes) {
		_selectedAgentProperties = new Vector<String>();
	}

	/**
	 * Invoked at the head of "spot" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_spot(Attributes attributes) {
		if ( _property)
			on_spot_property( attributes);
		else if ( _object)
			on_spot_object( attributes);
	}

	/**
	 * Invoked at the head of "agent" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_agent(Attributes attributes) {
		if ( _property)
			on_agent_property( attributes);
		else if ( _object)
			on_agent_object( attributes);
	}

	/**
	 * Prepares for reading the new spot properties.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_spot_property(Attributes attributes) {
		_name = attributes.getValue( "name");
		if ( null == _name || _name.equals( "")) {
			_result = false;
			return;
		}

		_spotPropertyMap = new TreeMap<String, PropertyBase>();
	}

	/**
	 * Prepares for reading the new agent properties.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_agent_property(Attributes attributes) {
		_name = attributes.getValue( "name");
		if ( null == _name || _name.equals( "")) {
			_result = false;
			return;
		}

		_agentPropertyMap = new TreeMap<String, PropertyBase>();
	}

	/**
	 * Reads the spot object data.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_spot_object(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name/* || name.equals( "")*/) {
			_result = false;
			return;
		}

		if ( null != _objectManager._spotObjectManager.get( name)) {
			_result = false;
			return;
		}

		_spotObject = new SpotObject( name, null, _objectManager, _graphics2D);
		_objectManager._spotObjectManager.append( name, _spotObject);


		String attribute = attributes.getValue( "visible");
		if ( null != attribute && !attribute.equals( ""))
			_spotObject._visible = ( attribute.equals( "true")) ? true : false;


		attribute = attributes.getValue( "visible_name");
		if ( null != attribute && !attribute.equals( ""))
			_spotObject._visibleName = ( attribute.equals( "true")) ? true : false;


		String imageR = attributes.getValue( "image_r");
		if ( null != imageR && !imageR.equals( "")) {
			String imageG = attributes.getValue( "image_g");
			if ( null != imageG && !imageG.equals( "")) {
				String imageB = attributes.getValue( "image_b");
				if ( null != imageB && !imageB.equals( "")) {
					try {
						_spotObject._imageColor = new Color( Integer.parseInt( imageR), Integer.parseInt( imageG), Integer.parseInt( imageB));
					} catch (NumberFormatException e) {
						//e.printStackTrace();
						_result = false;
						return;
					}
				}
			}
		}


		String textR = attributes.getValue( "text_r");
		if ( null != textR && !textR.equals( "")) {
			String textG = attributes.getValue( "text_g");
			if ( null != textG && !textG.equals( "")) {
				String textB = attributes.getValue( "text_b");
				if ( null != textB && !textB.equals( "")) {
					try {
						_spotObject._textColor = new Color( Integer.parseInt( textR), Integer.parseInt( textG), Integer.parseInt( textB));
					} catch (NumberFormatException e) {
						//e.printStackTrace();
						_result = false;
						return;
					}
				}
			}
		}


		String fontName = attributes.getValue( "font_name");
		if ( null != fontName && !fontName.equals( "")) {
			String fontStyle = attributes.getValue( "font_style");
			if ( null != fontStyle && !fontStyle.equals( "")) {
				String fontSize = attributes.getValue( "font_size");
				if ( null != fontSize && !fontSize.equals( "")) {
					try {
						_spotObject._font = new Font( fontName, Integer.parseInt( fontStyle), Integer.parseInt( fontSize));
					} catch (NumberFormatException e) {
						//e.printStackTrace();
						_result = false;
						return;
					}
				}
			}
		}

		if ( null == _spotObject._font) {
			Font font = _graphics2D.getFont();
			_spotObject._font = new Font( font.getFontName(), font.getStyle(), font.getSize());
		}


		if ( !_spotObject.setup( _graphics2D)) {
			_result = false;
			return;
		}


		String imageFilename = attributes.getValue( "image");
		if ( null != imageFilename && !imageFilename.equals( ""))
			_spotObject.load_image( imageFilename);


		int x, y;

		attribute = attributes.getValue( "x");
		if ( null == attribute || attribute.equals( "")) {
			_result = false;
			return;
		}

		try {
			x = Integer.parseInt( attribute);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}

		attribute = attributes.getValue( "y");
		if ( null == attribute || attribute.equals( "")) {
			_result = false;
			return;
		}

		try {
			y = Integer.parseInt( attribute);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}

		_spotObject.set_position( x, y);


		attribute = attributes.getValue( "image_object");
		if ( null != attribute && !attribute.equals( ""))
			_spotObject._imageObject = ( attribute.equals( "true")) ? true : false;

		if ( !set_image_position_and_image_size( attributes)) {
			_result = false;
			return;
		}
	}

	/**
	 * @param attributes
	 * @return
	 */
	private boolean set_image_position_and_image_size(Attributes attributes) {
		int imageX, imageY, imageWidth, imageHeight;

		String attribute = attributes.getValue( "image_x");
		if ( null == attribute || attribute.equals( ""))
			return true;

		try {
			imageX = Integer.parseInt( attribute);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}

		attribute = attributes.getValue( "image_y");
		if ( null == attribute || attribute.equals( ""))
			return true;

		try {
			imageY = Integer.parseInt( attribute);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}

		attribute = attributes.getValue( "image_width");
		if ( null == attribute || attribute.equals( ""))
			return true;

		try {
			imageWidth = Integer.parseInt( attribute);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}

		attribute = attributes.getValue( "image_height");
		if ( null == attribute || attribute.equals( ""))
			return true;

		try {
			imageHeight = Integer.parseInt( attribute);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}

		_spotObject.set_image_position_and_image_size( imageX, imageY, imageWidth, imageHeight);

		return true;
	}

	/**
	 * Reads the agent object data.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_agent_object(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		_agentObject = new AgentObject( name,_objectManager, _graphics2D);
		_objectManager._agentObjectManager.append( name, _agentObject);


		String attribute = attributes.getValue( "visible");
		if ( null != attribute && !attribute.equals( ""))
			_agentObject._visible = ( attribute.equals( "true")) ? true : false;


		attribute = attributes.getValue( "visible_name");
		if ( null != attribute && !attribute.equals( ""))
			_agentObject._visibleName = ( attribute.equals( "true")) ? true : false;


		String imageR = attributes.getValue( "image_r");
		if ( null != imageR && !imageR.equals( "")) {
			String imageG = attributes.getValue( "image_g");
			if ( null != imageG && !imageG.equals( "")) {
				String imageB = attributes.getValue( "image_b");
				if ( null != imageB && !imageB.equals( "")) {
					try {
						_agentObject._imageColor = new Color( Integer.parseInt( imageR), Integer.parseInt( imageG), Integer.parseInt( imageB));
					} catch (NumberFormatException e) {
						//e.printStackTrace();
						_result = false;
						return;
					}
				}
			}
		}


		String textR = attributes.getValue( "text_r");
		if ( null != textR && !textR.equals( "")) {
			String textG = attributes.getValue( "text_g");
			if ( null != textG && !textG.equals( "")) {
				String textB = attributes.getValue( "text_b");
				if ( null != textB && !textB.equals( "")) {
					try {
						_agentObject._textColor = new Color( Integer.parseInt( textR), Integer.parseInt( textG), Integer.parseInt( textB));
					} catch (NumberFormatException e) {
						//e.printStackTrace();
						_result = false;
						return;
					}
				}
			}
		}


		String fontName = attributes.getValue( "font_name");
		if ( null != fontName && !fontName.equals( "")) {
			String fontStyle = attributes.getValue( "font_style");
			if ( null != fontStyle && !fontStyle.equals( "")) {
				String fontSize = attributes.getValue( "font_size");
				if ( null != fontSize && !fontSize.equals( "")) {
					try {
						_agentObject._font = new Font( fontName, Integer.parseInt( fontStyle), Integer.parseInt( fontSize));
					} catch (NumberFormatException e) {
						//e.printStackTrace();
						_result = false;
						return;
					}
				}
			}
		}

		if ( null == _agentObject._font) {
			Font font = _graphics2D.getFont();
			_agentObject._font = new Font( font.getFontName(), font.getStyle(), font.getSize());
		}


		if ( !_agentObject.setup( _graphics2D)) {
			_result = false;
			return;
		}


		String imageFilename = attributes.getValue( "image");
		if ( null != imageFilename && !imageFilename.equals( ""))
			_agentObject.load_image( imageFilename);
	}

	/**
	 * Invoked at the head of "image" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_image(Attributes attributes) {
		_image = true;
	}

	/**
	 * Invoked at the head of "data" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_data(Attributes attributes) {
		if ( _property) {
			if ( null != _spotPropertyMap) {
				get_spot_property( attributes);
				return;
			}
			if ( null != _agentPropertyMap) {
				get_agent_property( attributes);
				return;
			}
			if ( null != _selectedSpotProperties) {
				get_selected_spot_property( attributes);
				return;
			}
			if ( null != _selectedAgentProperties) {
				get_selected_agent_property( attributes);
				return;
			}
		} else if ( _image) {
			get_image_property( attributes);
		}
	}

	/**
	 * Reads the spot property data.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void get_spot_property(Attributes attributes) {
		String value = attributes.getValue( "value");
		if ( null == value) {
			_result = false;
			return;
		}

		SpotProperty spotProperty = new SpotProperty();
		get_property( spotProperty, attributes);
		_spotPropertyMap.put( value, spotProperty);
	}

	/**
	 * Reads the agent property data.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void get_agent_property(Attributes attributes) {
		String value = attributes.getValue( "value");
		if ( null == value) {
			_result = false;
			return;
		}

		AgentProperty agentProperty = new AgentProperty();
		get_property( agentProperty, attributes);
		_agentPropertyMap.put( value, agentProperty);
	}

	/**
	 * Reads the property data into the specified property data.
	 * @param propertyBase the specified property data.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void get_property(PropertyBase propertyBase, Attributes attributes) {
		String attribute = attributes.getValue( "visible");
		if ( null != attribute && !attribute.equals( ""))
			propertyBase._visible = ( attribute.equals( "true")) ? true : false;

		attribute = attributes.getValue( "image_r");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				propertyBase._imageR = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		attribute = attributes.getValue( "image_g");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				propertyBase._imageG = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		attribute = attributes.getValue( "image_b");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				propertyBase._imageB = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}


		attribute = attributes.getValue( "text_r");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				propertyBase._textR = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		attribute = attributes.getValue( "text_g");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				propertyBase._textG = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		attribute = attributes.getValue( "text_b");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				propertyBase._textB = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}


		String fontName = attributes.getValue( "font_name");
		if ( null != fontName && !fontName.equals( "")) {
			String fontStyle = attributes.getValue( "font_style");
			if ( null != fontStyle && !fontStyle.equals( "")) {
				String fontSize = attributes.getValue( "font_size");
				if ( null != fontSize && !fontSize.equals( "")) {
					try {
						propertyBase._font = new Font( fontName, Integer.parseInt( fontStyle), Integer.parseInt( fontSize));
					} catch (NumberFormatException e) {
						//e.printStackTrace();
						_result = false;
						return;
					}
				}
			}
		}

		if ( null == propertyBase._font) {
			Font font = _graphics2D.getFont();
			propertyBase._font = new Font( font.getFontName(), font.getStyle(), font.getSize());
		}



		String imageFilename = attributes.getValue( "image");
		if ( null != imageFilename && !imageFilename.equals( ""))
			propertyBase.load_image( imageFilename);
	}

	/**
	 * Reads the name of the visible spot property.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void get_selected_spot_property(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		_selectedSpotProperties.add( name);
	}

	/**
	 * Reads the name of the visible agent property.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void get_selected_agent_property(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		_selectedAgentProperties.add( name);
	}

	/**
	 * Reads the image property data.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void get_image_property(Attributes attributes) {
		String filename = attributes.getValue( "filename");
		if ( null == filename || filename.equals( "")) {
			_result = false;
			return;
		}

		int width = 0;
		String attribute = attributes.getValue( "width");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				width = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		int height = 0;
		attribute = attributes.getValue( "height");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				height = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		_imagePropertyMap.put( filename, new ImageProperty( width, height));
	}

	/**
	 * @param attributes
	 */
	private void on_chart(Attributes attributes) {
		String value = attributes.getValue( "actual_end_time");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}
		_chartObjectMap._actualEndTime = value.split( ",");


		value = attributes.getValue( "start_time");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}
		_chartObjectMap._startTime = value.split( ",");


		value = attributes.getValue( "step_time");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}
		_chartObjectMap._stepTime = value.split( ",");


		value = attributes.getValue( "end_time");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}
		_chartObjectMap._endTime = value.split( ",");


		value = attributes.getValue( "log_step_time");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}
		_chartObjectMap._logStepTime = value.split( ",");


		value = attributes.getValue( "export_end_time");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}
		_chartObjectMap._exportEndTime = value.equals( "true");

		
		value = attributes.getValue( "export_log_step_time");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}
		_chartObjectMap._exportLogStepTime = value.equals( "true");
	}

	/**
	 * @param attributes
	 */
	private void on_chart_frame(Attributes attributes) {
		if ( null != _chartObject) {
			_result = false;
			return;
		}

		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		String title = attributes.getValue( "title");
		if ( null == title)
			title = "";

		String horizontalAxis = attributes.getValue( "horizontal_axis");
		if ( null == horizontalAxis)
			horizontalAxis = "";

		String verticalAxis = attributes.getValue( "log_step_time");
		if ( null == verticalAxis)
			verticalAxis = "";

		int x = 0;
		String attribute = attributes.getValue( "x");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				x = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		int y = 0;
		attribute = attributes.getValue( "y");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				y = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		int width = AnimatorViewFrame._minimumWidth;
		attribute = attributes.getValue( "width");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				width = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		int height = AnimatorViewFrame._minimumHeight;
		attribute = attributes.getValue( "height");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				height = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		WindowProperty windowProperty = new WindowProperty( AnimatorInternalChartFrame._minimumWidth, AnimatorInternalChartFrame._minimumHeight);
		windowProperty._rectangle.x = x;
		windowProperty._rectangle.y = y;
		windowProperty._rectangle.width = ( ( AnimatorInternalChartFrame._minimumWidth > width) ? AnimatorInternalChartFrame._minimumWidth : width);
		windowProperty._rectangle.height = ( ( AnimatorInternalChartFrame._minimumHeight > height) ? AnimatorInternalChartFrame._minimumHeight : height);

		attribute = attributes.getValue( "maximum");
		if ( null != attribute && !attribute.equals( ""))
			windowProperty._maximum = ( attribute.equals( "true")) ? true : false;

		attribute = attributes.getValue( "icon");
		if ( null != attribute && !attribute.equals( ""))
			windowProperty._icon = ( attribute.equals( "true")) ? true : false;

		int order = -1;
		attribute = attributes.getValue( "order");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				order = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		windowProperty._order = order;

		_chartObject = new ChartObject( name, title, horizontalAxis, verticalAxis, windowProperty);

		_chartObjectMap.put( name, _chartObject);
	}

	/**
	 * @param attributes
	 */
	private void on_chart_data(Attributes attributes) {
		if ( null == _chartObject) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "dataset");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}
		int dataset = Integer.parseInt( value);


		value = attributes.getValue( "connect");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}
		boolean connect = value.equals( "true");


		value = attributes.getValue( "show");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}
		boolean show = value.equals( "true");


		ChartData[] chartData = new ChartData[ 2];
		for ( int i = 0; i < 2; ++i) {
			String type = attributes.getValue( "type" + String.valueOf( i));
			if ( null == type || ( !type.equals( "step") && !type.equals( "agent") && !type.equals( "spot"))) {
				_result = false;
				return;
			}

			String objectName = attributes.getValue( "object_name" + String.valueOf( i));
			if ( null == objectName)
				objectName = "";

			String numberVariable = attributes.getValue( "number_variable" + String.valueOf( i));
			if ( null == numberVariable)
				numberVariable = "";

			chartData[ i] = new ChartData( type, objectName, numberVariable);
		}

		_chartObject._chartDataPairs.add( new ChartDataPair( dataset, chartData, connect, show, _chartObject));
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters(char[] arg0, int arg1, int arg2)
		throws SAXException {
		if ( !_result)
			return;

		if ( null != _value) {
			String value = new String( arg0, arg1, arg2);
			_value += value;
		}
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String arg0, String arg1, String arg2) throws SAXException {

		if ( !_result) {
			super.endElement(arg0, arg1, arg2);
			return;
		}

		if ( arg2.equals( "header")) {
			on_header();
		} else if ( arg2.equals( "file")) {
			on_file();
		} else if ( arg2.equals( "property")) {
			on_property();
		} else if ( arg2.equals( "object")) {
			on_object();
		} else if ( arg2.equals( "agent_width")) {
			on_agent_width();
		} else if ( arg2.equals( "agent_height")) {
			on_agent_height();
		} else if ( arg2.equals( "spot_width")) {
			on_spot_width();
		} else if ( arg2.equals( "spot_height")) {
			on_spot_height();
		} else if ( arg2.equals( "minimum_width")) {
			on_minimum_width();
		} else if ( arg2.equals( "velocity")) {
			on_velocity();
		} else if ( arg2.equals( "window")) {
			on_window();
		} else if ( arg2.equals( "spot_log")) {
			on_spot_log();
		} else if ( arg2.equals( "selected_spot_property")) {
			on_selected_spot_property();
		} else if ( arg2.equals( "selected_agent_property")) {
			on_selected_agent_property();
		} else if ( arg2.equals( "spot")) {
			on_spot();
		} else if ( arg2.equals( "agent")) {
			on_agent();
		} else if ( arg2.equals( "image")) {
			on_image();
		} else if ( arg2.equals( "chart")) {
			on_chart();
		} else if ( arg2.equals( "chart_frame")) {
			on_chart_frame();
		} else if ( arg2.equals( "chart_data")) {
			on_chart_data();
		}
	}

	/**
	 * Invoked at the end of the "header" tag.
	 */
	private void on_header() {
		_header = false;

		if ( _filenames.isEmpty()) {
			_result = false;
			return;
		}

		_headerObject._filenames = _filenames.toArray( new String[ 0]);
		if ( null == _headerObject._filenames) {
			_result = false;
			return;
		}

		_filenames.clear();
	}

	/**
	 * Invoked at the end of the "file" tag.
	 */
	private void on_file() {
	}

	/**
	 * Invoked at the end of the "property" tag.
	 */
	private void on_property() {
		_property = false;
	}

	/**
	 * Invoked at the end of the "object" tag.
	 */
	private void on_object() {
		_object = false;
	}

	/**
	 * Invoked at the end of the "agent_width" tag.
	 */
	private void on_agent_width() {
		try {
			CommonProperty.get_instance()._agentWidth = Integer.parseInt( _value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}
		_value = null;
	}

	/**
	 * Invoked at the end of the "agent_height" tag.
	 */
	private void on_agent_height() {
		try {
			CommonProperty.get_instance()._agentHeight = Integer.parseInt( _value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}
		_value = null;
	}

	/**
	 * Invoked at the end of the "spot_width" tag.
	 */
	private void on_spot_width() {
		try {
			CommonProperty.get_instance()._spotWidth = Integer.parseInt( _value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}
		_value = null;
	}

	/**
	 * Invoked at the end of the "spot_height" tag.
	 */
	private void on_spot_height() {
		try {
			CommonProperty.get_instance()._spotHeight = Integer.parseInt( _value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}
		_value = null;
	}

	/**
	 * Invoked at the end of the "minimum_width" tag.
	 */
	private void on_minimum_width() {
//		try {
//			CommonProperty.get_instance()._minimum_width = Integer.parseInt( _value);
//		} catch (NumberFormatException e) {
//			//e.printStackTrace();
//			_result = false;
//			return;
//		}
		_value = null;
	}

	/**
	 * Invoked at the end of the "velocity" tag.
	 */
	private void on_velocity() {
		try {
			CommonProperty.get_instance()._divide = Integer.parseInt( _value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}
		_value = null;
	}

	/**
	 * 
	 */
	private void on_window() {
	}

	/**
	 * 
	 */
	private void on_spot_log() {
	}

	/**
	 * Invoked at the end of "selected_spot_property" tag.
	 */
	private void on_selected_spot_property() {
		if ( _selectedSpotProperties.isEmpty())
			return;

		_objectManager._scenarioManager._spotPropertyManager.set_selected_properties( _selectedSpotProperties);
		_selectedSpotProperties = null;
	}

	/**
	 * Invoked at the end of "selected_agent_property" tag.
	 */
	private void on_selected_agent_property() {
		if ( _selectedAgentProperties.isEmpty())
			return;

		_objectManager._scenarioManager._agentPropertyManager.set_selected_properties( _selectedAgentProperties);
		_selectedAgentProperties = null;
	}

	/**
	 * Invoked at the end of "spot" tag.
	 */
	private void on_spot() {
		if ( _property)
			on_spot_property();
		else if ( _object)
			on_spot_object();
	}

	/**
	 * Invoked at the end of "agent" tag.
	 */
	private void on_agent() {
		if ( _property)
			on_agent_property();
		else if ( _object)
			on_agent_object();
	}

	/**
	 * Invoked after reading the new spot properties.
	 */
	private void on_spot_property() {
		_result = _objectManager._scenarioManager._spotPropertyManager.append_properties( _name, _spotPropertyMap, _graphics2D);
		_name = null;
		_spotPropertyMap = null;
	}

	/**
	 * Invoked after reading the new agent properties.
	 */
	private void on_agent_property() {
		_result = _objectManager._scenarioManager._agentPropertyManager.append_properties( _name, _agentPropertyMap, _graphics2D);
		_name = null;
		_agentPropertyMap = null;
	}

	/**
	 * Invoked after reading the spot object data.
	 */
	private void on_spot_object() {
		_spotObject = null;
	}

	/**
	 * Invoked after reading the agent object data.
	 */
	private void on_agent_object() {
		_agentObject = null;
	}

	/**
	 * Invoked at the end of "image" tag.
	 */
	private void on_image() {
		ImagePropertyManager.get_instance().putAll( _imagePropertyMap);
		_image = false;
	}

	/**
	 * 
	 */
	private void on_chart() {
	}

	/**
	 * 
	 */
	private void on_chart_frame() {
		_chartObject = null;
	}

	/**
	 * 
	 */
	private void on_chart_data() {
	}
}
