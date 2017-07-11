/*
 * 2005/04/22
 */
package soars.application.animator.file.loader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import soars.application.animator.main.Administrator;
import soars.application.animator.main.Constant;
import soars.application.animator.main.internal.AnimatorViewFrame;
import soars.application.animator.main.internal.WindowProperty;
import soars.application.animator.object.chart.AnimatorInternalChartFrame;
import soars.application.animator.object.chart.ChartData;
import soars.application.animator.object.chart.ChartDataPair;
import soars.application.animator.object.chart.ChartObject;
import soars.application.animator.object.chart.ChartObjectMap;
import soars.application.animator.setting.common.CommonProperty;
import soars.common.utility.swing.image.ImageProperty;
import soars.common.utility.swing.image.ImagePropertyManager;
import soars.common.utility.tool.resource.Resource;

/**
 * The XML SAX loader for Animator data.
 * @author kurata / SOARS project
 */
public class CommonSaxLoader extends DefaultHandler {

	/**
	 * Root directory for Animator data file.
	 */
	private String _directory = "";

	/**
	 * True while reading the informations of SOARS log files.
	 */
	private boolean _header = false;

	/**
	 * True while reading the data of the properties for SOARS.
	 */
	private boolean _property = false;

	/**
	 * True while reading the informations of image files.
	 */
	private boolean _image = false;

	/**
	 * String in which XML string is stored temporarily.
	 */
	private String _value = null;

	/**
	 * 
	 */
	private Vector<FileProperty> _fileProperties = null;

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
	 * @return true if the loading the specified file is completed successfully
	 */
	public static boolean execute(File file, Vector<FileProperty> fileProperties) {
		_result = false;
		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			CommonSaxLoader commonSaxLoader = new CommonSaxLoader( file.getAbsoluteFile().getParentFile().getAbsolutePath(), fileProperties);
			saxParser.parse( file, commonSaxLoader);
			commonSaxLoader.at_end_of_load();
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

		if ( !setup_imagePropertyManager()) {
			_result = false;
			return;
		}

		if ( !_chartObjectMap.isEmpty() && !ChartObjectMap.get_instance().set( _chartObjectMap, new File( _directory + "/" + Constant._chartLogDirectory))) {
			_result = false;
			return;
		}
	}

	/**
	 * @return
	 */
	private boolean setup_imagePropertyManager() {
		if ( !Administrator.get_instance().exist_image_directory())
			return true;

		File imageDirectory = Administrator.get_instance().get_image_directory();
		if ( null == imageDirectory)
			return false;

		File[] files = imageDirectory.listFiles();
		if ( null == files)
			return false;

		Map<String, File> fileMap = new HashMap<String, File>();

		// ファイルが存在していてImagePropertyが存在していない場合は新たにImagePropertyを作成して_imagePropertyMapへ追加
		for ( File file:files) {
			if ( file.getName().startsWith( "."))
				continue;

			fileMap.put( file.getName(), file);

			ImageProperty imageProperty = _imagePropertyMap.get( file.getName());
			if ( null != imageProperty)
				continue;

			BufferedImage bufferedImage = Resource.load_image( file);
			if ( null == bufferedImage)
				continue;

			_imagePropertyMap.put( file.getName(), new ImageProperty( bufferedImage.getWidth(), bufferedImage.getHeight()));
		}

		// ImagePropertyが存在しているのにファイルが存在していない場合はImagePropertyを_imagePropertyMapから削除
		Set<String> keys = _imagePropertyMap.keySet();
		String[] filenames = keys.toArray( new String[ 0]);
		for ( String filename:filenames) {
			File file = fileMap.get( filename);
			if ( null != file)
				continue;

			_imagePropertyMap.remove( filename);
		}

		ImagePropertyManager.get_instance().putAll( _imagePropertyMap);

		return true;
	}

	/**
	 * Creates the XML SAX loader for Animator data.
	 * @param directory the root directory for Animator data
	 * @param fileProperties
	 */
	public CommonSaxLoader(String directory, Vector<FileProperty> fileProperties) {
		super();
		_directory = directory;
		_fileProperties = fileProperties;
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

		if ( arg2.equals( "animation_common_data")) {
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

		_fileProperties.add( new FileProperty( filename, type, name, header));
	}

	/**
	 * Invoked at the head of "property" tag.
	 * @param attributes the interface for a list of XML attributes
	 */
	private void on_property(Attributes attributes) {
		_property = true;
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
		if ( _image)
			get_image_property( attributes);
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

		if ( _fileProperties.isEmpty()) {
			_result = false;
			return;
		}
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
	 * Invoked at the end of "image" tag.
	 */
	private void on_image() {
//		ImagePropertyManager.get_instance().putAll( _imagePropertyMap);
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
