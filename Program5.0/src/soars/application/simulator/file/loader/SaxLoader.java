/*
 * 2005/04/22
 */
package soars.application.simulator.file.loader;

import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import soars.application.simulator.data.ChartData;
import soars.application.simulator.data.Dataset;
import soars.application.simulator.data.FileManagerData;
import soars.application.simulator.data.LogData;
import soars.application.simulator.main.MainFrame;

/**
 * @author kurata
 */
public class SaxLoader extends DefaultHandler {

	/**
	 * 
	 */
	static private boolean _result;

	/**
	 * 
	 */
	private String _filename = "";

//	/**
//	 * 
//	 */
//	private String _simulatorWindowTitle = "";

	/**
	 * 
	 */
	private String _simulatorWindowTime = "";

	/**
	 * 
	 */
	private String _logViewerWindowTitle = "";

	/**
	 * 
	 */
	private Rectangle _logViewerWindowRectangle = new Rectangle();

	/**
	 * 
	 */
	private String _type = "";

	/**
	 * 
	 */
	private List<LogData> _agents = new ArrayList<LogData>();

	/**
	 * 
	 */
	private List<LogData> _spots = new ArrayList<LogData>();

	/**
	 * 
	 */
	private ChartData _chartData = null;

	/**
	 * 
	 */
	private Map<String, ChartData> _chartDataMap = new HashMap<String, ChartData>();

	/**
	 * 
	 */
	private Dataset _dataset = null;

	/**
	 * 
	 */
	private FileManagerData _fileManagerData = null;

	/**
	 * @param file
	 * @return
	 */
	public static boolean execute(File file) {
		_result = false;

		SaxLoader saxLoader = new SaxLoader( file.getName());

		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			saxParser.parse( file, saxLoader);
			saxLoader.at_end_of_load();
		} catch (Exception e) {
			e.printStackTrace();
			saxLoader.at_end_of_load();
			return false;
		}
		return _result;
	}

	/**
	 * 
	 */
	private void at_end_of_load() {
		if ( !_result)
			return;
	}

	/**
	 * @param filename 
	 * 
	 */
	public SaxLoader(String filename) {
		super();
		_filename = filename;
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement( String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {

		super.startElement(arg0, arg1, arg2, arg3);

		if ( arg2.equals( "simulator_data")) {
			on_simulator_data( arg3);
			_result = true;
			return;
		}

		if ( !_result)
			return;

		if ( arg2.equals( "log_viewer_data"))
			on_log_viewer_data( arg3);
		else if ( arg2.equals( "agents"))
			on_agents( arg3);
		else if ( arg2.equals( "spots"))
			on_spots( arg3);
		else if ( arg2.startsWith( "log"))
			on_log( arg2, arg3);
		else if ( arg2.equals( "chart_data"))
			on_chart_data( arg3);
		else if ( arg2.equals( "xRange"))
			on_xRange( arg3);
		else if ( arg2.equals( "yRange"))
			on_yRange( arg3);
		else if ( arg2.equals( "dataset"))
			on_dataset( arg3);
		else if ( arg2.equals( "file_manager_data"))
			on_file_manager( arg3);
	}

	/**
	 * @param attributes
	 */
	private void on_simulator_data(Attributes attributes) {
//		String value = attributes.getValue( "title");
//		if ( null == value || value.equals( "")) {
//			_result = false;
//			return;
//		}
//
//		_simulatorWindowTitle = value;


		String value = attributes.getValue( "time");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}

		_simulatorWindowTime = value;
	}

	/**
	 * @param attributes
	 */
	private void on_log_viewer_data(Attributes attributes) {
		String value = attributes.getValue( "title");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}

		_logViewerWindowTitle = value;

		value = attributes.getValue( "x");
		_logViewerWindowRectangle.x = ( ( null == value || value.equals( "")) ? 0 : Integer.parseInt( value));

		value = attributes.getValue( "y");
		_logViewerWindowRectangle.y = ( ( null == value || value.equals( "")) ? 0 : Integer.parseInt( value));

		value = attributes.getValue( "width");
		_logViewerWindowRectangle.width = ( ( null == value || value.equals( "")) ? 0 : Integer.parseInt( value));

		value = attributes.getValue( "height");
		_logViewerWindowRectangle.height = ( ( null == value || value.equals( "")) ? 0 : Integer.parseInt( value));
	}

	/**
	 * @param attributes
	 */
	private void on_agents(Attributes attributes) {
		_type = "agents";
	}

	/**
	 * @param attributes
	 */
	private void on_spots(Attributes attributes) {
		_type = "spots";
	}

	/**
	 * @param qName
	 * @param attributes
	 */
	private void on_log(String qName, Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		if ( _type.equals( "agents"))
			_agents.add( new LogData( name));
		else if ( _type.equals( "spots"))
			_spots.add( new LogData( name));
		else {
			_result = false;
			return;
		}
	}

	/**
	 * @param attributes
	 */
	private void on_chart_data(Attributes attributes) {
		if ( null != _chartData) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "name");
		if ( null == value)
			value = "";

		_chartData = _chartDataMap.get( value);
		if ( null != _chartData)
			return;

		_chartData = new ChartData();
		_chartDataMap.put( value, _chartData);

		_chartData._name = value;

		value = attributes.getValue( "title");
		if ( null == value/* || value.equals( "")*/)
			return;

		_chartData._title = value;

		value = attributes.getValue( "xLabel");
		if ( null == value/* || value.equals( "")*/)
			return;

		_chartData._XLabel = value;

		value = attributes.getValue( "yLabel");
		if ( null == value/* || value.equals( "")*/)
			return;

		_chartData._YLabel = value;

		value = attributes.getValue( "x");
		_chartData._windowRectangle.x = ( ( null == value || value.equals( "")) ? 0 : Integer.parseInt( value));

		value = attributes.getValue( "y");
		_chartData._windowRectangle.y = ( ( null == value || value.equals( "")) ? 0 : Integer.parseInt( value));

		value = attributes.getValue( "width");
		_chartData._windowRectangle.width = ( ( null == value || value.equals( "")) ? 0 : Integer.parseInt( value));

		value = attributes.getValue( "height");
		_chartData._windowRectangle.height = ( ( null == value || value.equals( "")) ? 0 : Integer.parseInt( value));
	}

	/**
	 * @param attributes
	 */
	private void on_xRange(Attributes attributes) {
		if ( null == _chartData) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "min");
		if ( null == value || value.equals( ""))
			return;

		double min = new Double( value).doubleValue();

		value = attributes.getValue( "max");
		if ( null == value || value.equals( ""))
			return;

		double max = new Double( value).doubleValue();

		_chartData._XRange = true;
		_chartData._XRangeMin = min;
		_chartData._XRangeMax = max;
	}

	/**
	 * @param attributes
	 */
	private void on_yRange(Attributes attributes) {
		if ( null == _chartData) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "min");
		if ( null == value || value.equals( ""))
			return;

		double min = new Double( value).doubleValue();

		value = attributes.getValue( "max");
		if ( null == value || value.equals( ""))
			return;

		double max = new Double( value).doubleValue();

		_chartData._YRange = true;
		_chartData._YRangeMin = min;
		_chartData._YRangeMax = max;
	}

	/**
	 * @param attributes
	 */
	private void on_dataset(Attributes attributes) {
		if ( null == _chartData) {
			_result = false;
			return;
		}

		if ( null != _dataset) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "id");
		if ( null == value || value.equals( ""))
			return;

		int id = Integer.parseInt( value);

		String legend = attributes.getValue( "legend");
		if ( null == legend /*|| legend.equals( "")*/)
			return;

		value = attributes.getValue( "connect");
		if ( null == value || value.equals( ""))
			return;

		boolean connect = value.equals( "true");

		_dataset = new Dataset( id, legend, connect);
	}

	/**
	 * @param attributes
	 */
	private void on_file_manager(Attributes attributes) {
		if ( null != _fileManagerData) {
			_result = false;
			return;
		}

		_fileManagerData = new FileManagerData();

		String value = attributes.getValue( "x");
		_fileManagerData._windowRectangle.x = ( ( null == value || value.equals( "")) ? 0 : Integer.parseInt( value));

		value = attributes.getValue( "y");
		_fileManagerData._windowRectangle.y = ( ( null == value || value.equals( "")) ? 0 : Integer.parseInt( value));

		value = attributes.getValue( "width");
		_fileManagerData._windowRectangle.width = ( ( null == value || value.equals( "")) ? 0 : Integer.parseInt( value));

		value = attributes.getValue( "height");
		_fileManagerData._windowRectangle.height = ( ( null == value || value.equals( "")) ? 0 : Integer.parseInt( value));

		value = attributes.getValue( "divider_location");
		_fileManagerData._dividerLocation = ( ( null == value || value.equals( "")) ? 100 : Integer.parseInt( value));

		value = attributes.getValue( "current_directory");
		if ( null == value)
			value = "";

		_fileManagerData._currentDirectory = value;

		value = attributes.getValue( "visible");
		if ( null == value)
			value = "true";

		_fileManagerData._visible = value.equals( "true");
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String arg0, String arg1, String arg2) throws SAXException {

		if ( !_result) {
			super.endElement(arg0, arg1, arg2);
			return;
		}

		if ( arg2.equals( "simulator_data"))
			on_simulator_data();
		else if ( arg2.equals( "log_viewer_data"))
			on_log_viewer_data();
		else if ( arg2.equals( "agents"))
			on_agents();
		else if ( arg2.equals( "spots"))
			on_spots();
		else if ( arg2.startsWith( "log"))
			on_log();
		else if ( arg2.equals( "chart_data"))
			on_chart_data();
		else if ( arg2.equals( "xRange"))
			on_xRange();
		else if ( arg2.equals( "yRange"))
			on_yRange();
		else if ( arg2.equals( "dataset"))
			on_dataset();
		else if ( arg2.equals( "file_manager_data"))
			on_file_manager();

		super.endElement(arg0, arg1, arg2);
	}

	/**
	 * 
	 */
	private void on_simulator_data() {
		if ( !MainFrame.get_instance().load(
			_filename,
//			_simulatorWindowTitle,
			_simulatorWindowTime,
			_logViewerWindowTitle,
			_logViewerWindowRectangle,
			_agents, _spots, _chartDataMap,
			_fileManagerData))
			_result = false;
	}

	/**
	 * 
	 */
	private void on_log_viewer_data() {
		_type = "";
	}

	/**
	 * 
	 */
	private void on_agents() {
		if ( !_type.equals( "agents")) {
			_result = false;
			return;
		}

		_type = "";
	}

	/**
	 * 
	 */
	private void on_spots() {
		if ( !_type.equals( "spots")) {
			_result = false;
			return;
		}

		_type = "";
	}

	/**
	 * 
	 */
	private void on_log() {
	}

	/**
	 * 
	 */
	private void on_chart_data() {
		if ( null == _chartData) {
			_result = false;
			return;
		}

		_chartData = null;
	}

	/**
	 * 
	 */
	private void on_xRange() {
	}

	/**
	 * 
	 */
	private void on_yRange() {
	}

	/**
	 * 
	 */
	private void on_dataset() {
		if ( null == _chartData) {
			_result = false;
			return;
		}

		if ( null == _dataset) {
			_result = false;
			return;
		}

		_chartData._datasets.add( _dataset);

		_dataset = null;
	}

	/**
	 * 
	 */
	private void on_file_manager() {
		if ( null == _fileManagerData) {
			_result = false;
			return;
		}
	}
}
