/*
 * 2005/07/05
 */
package soars.plugin.modelbuilder.chart.chart.main;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author kurata
 */
public class Loader extends DefaultHandler {


	/**
	 * 
	 */
	private LogViewer _logViewer = null;


	/**
	 * 
	 */
	private String _value = null;


	/**
	 * 
	 */
	private int _dataset = 0;


	/**
	 * 
	 */
	static private boolean _result;

	/**
	 * @param file
	 * @param logViewer
	 * @return
	 */
	public static boolean load(File file, LogViewer logViewer) {
		_result = true;
		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			saxParser.parse( file, new Loader( logViewer));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return _result;
	}

	/**
	 * @param logViewer
	 * 
	 */
	public Loader(LogViewer logViewer) {
		super();
		_logViewer = logViewer;
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {

		super.startElement(arg0, arg1, arg2, arg3);

		if ( arg2.equals( "chart_data")) {
			on_chart_data( arg3);
			_result = true;
			return;
		}

		if ( !_result)
			return;

		if ( arg2.equals( "xRange"))
			on_xRange( arg3);
		else if ( arg2.equals( "yRange"))
			on_yRange( arg3);
		else if ( arg2.equals( "dataset"))
			on_dataset( arg3);
	}

	/**
	 * @param attributes
	 */
	private void on_chart_data(Attributes attributes) {
		String value = attributes.getValue( "title");
		if ( null == value || value.equals( ""))
			return;

		_logViewer.setTitle( value);

		value = attributes.getValue( "xLabel");
		if ( null == value || value.equals( ""))
			return;

		_logViewer.setXLabel( value);

		value = attributes.getValue( "yLabel");
		if ( null == value || value.equals( ""))
			return;

		_logViewer.setYLabel( value);
	}

	/**
	 * @param attributes
	 */
	private void on_xRange(Attributes attributes) {
		String value = attributes.getValue( "min");
		if ( null == value || value.equals( ""))
			return;

		double min = new Double( value).doubleValue();

		value = attributes.getValue( "max");
		if ( null == value || value.equals( ""))
			return;

		double max = new Double( value).doubleValue();

		_logViewer.setXRange( min, max);
	}

	/**
	 * @param attributes
	 */
	private void on_yRange(Attributes attributes) {
		String value = attributes.getValue( "min");
		if ( null == value || value.equals( ""))
			return;

		double min = new Double( value).doubleValue();

		value = attributes.getValue( "max");
		if ( null == value || value.equals( ""))
			return;

		double max = new Double( value).doubleValue();

		_logViewer.setYRange( min, max);
	}

	/**
	 * @param attributes
	 */
	private void on_dataset(Attributes attributes) {
		String value = attributes.getValue( "legend");
		if ( null == value || value.equals( ""))
			return;

		_logViewer.addLegend( _dataset, value);

		_value = "";
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
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

		if ( arg2.equals( "chart_data"))
			on_chart_data();
		else if ( arg2.equals( "xRange"))
			on_xRange();
		else if ( arg2.equals( "yRange"))
			on_yRange();
		else if ( arg2.equals( "dataset"))
			on_dataset();
	}

	/**
	 * 
	 */
	private void on_chart_data() {
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
		if ( !_value.equals( "")) {
			String[] values = _value.split( " ");
			if ( null == values || ( 0 != values.length % 3)) {
				_result = false;
				return;
			}

			for ( int i = 0; i < values.length; i += 3) {
				if ( null == values[ i] || null == values[ i + 1] || null == values[ i + 2]
					|| ( !values[ i].equals( "m") && !values[ i].equals( "p"))) {
					_result = false;
					return;
				}

				try {
					double x = Double.parseDouble( values[ i + 1]);
					double y = new Double( values[ i + 2]).doubleValue();
					_logViewer.addPoint( _dataset, x, y, values[ i].equals( "p"));
				} catch (NumberFormatException e) {
					e.printStackTrace();
					_result = false;
					return;
				}
			}
		}

		_value = null;

		++_dataset;
	}
}
