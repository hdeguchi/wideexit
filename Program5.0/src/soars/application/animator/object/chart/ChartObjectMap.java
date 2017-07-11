/**
 * 
 */
package soars.application.animator.object.chart;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JDesktopPane;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.animator.main.internal.InternalFrameRectangleMap;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class ChartObjectMap extends HashMap<String, ChartObject> {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private ChartObjectMap _chartObjectMap = null;

	/**
	 * 
	 */
	public String[] _actualEndTime = new String[] { "0", "00", "00"};

	/**
	 * 
	 */
	public String[] _startTime = new String[] { "0", "00", "00"};

	/**
	 * 
	 */
	public String[] _stepTime = new String[] { "0", "00", "00"};

	/**
	 * 
	 */
	public String[] _endTime = new String[] { "0", "00", "00"};

	/**
	 * 
	 */
	public String[] _logStepTime = new String[] { "0", "00", "00"};

	/**
	 * 
	 */
	public boolean _exportEndTime = true;

	/**
	 * 
	 */
	public boolean _exportLogStepTime = false;

	/**
	 * Returns the instance of this class.
	 * @return the instance of this class
	 */
	public static ChartObjectMap get_instance() {
		synchronized( _lock) {
			if ( null == _chartObjectMap) {
				_chartObjectMap = new ChartObjectMap();
			}
		}
		return _chartObjectMap;
	}

	/**
	 * 
	 */
	public ChartObjectMap() {
		super();
	}

	/**
	 * @return
	 */
	public ChartObjectMap(ChartObjectMap chartObjectMap) {
		super();
		System.arraycopy( chartObjectMap._actualEndTime, 0, _actualEndTime, 0, _actualEndTime.length);
		System.arraycopy( chartObjectMap._startTime, 0, _startTime, 0, _startTime.length);
		System.arraycopy( chartObjectMap._stepTime, 0, _stepTime, 0, _stepTime.length);
		System.arraycopy( chartObjectMap._endTime, 0, _endTime, 0, _endTime.length);
		System.arraycopy( chartObjectMap._logStepTime, 0, _logStepTime, 0, _logStepTime.length);
		_exportEndTime = chartObjectMap._exportEndTime;
		_exportLogStepTime = chartObjectMap._exportLogStepTime;
		Iterator iterator = chartObjectMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ChartObject chartObject = ( ChartObject)entry.getValue();
			put( chartObject._name, new ChartObject( chartObject));
		}
	}

	/**
	 * @param chartPropertiesFile
	 * @param chartLogDirectory
	 * @return
	 */
	public boolean create_chartFrames(File chartPropertiesFile, File chartLogDirectory) {
		if ( !chartPropertiesFile.exists() || !chartPropertiesFile.canRead()
			|| !chartLogDirectory.exists() || !chartLogDirectory.isDirectory())
			return true;

		String chartProperties = FileUtility.read_text_from_file( chartPropertiesFile, "UTF-8");
		if ( null == chartProperties)
			return false;

		String[] lines = chartProperties.split( "\n");
		if ( null == lines)
			return false;

		if ( 8 > lines.length)
			return true;

		_actualEndTime = lines[ 0].split( ",");
		_startTime = lines[ 1].split( ",");
		_stepTime = lines[ 2].split( ",");
		_endTime = lines[ 3].split( ",");
		_logStepTime = lines[ 4].split( ",");
		_exportEndTime = lines[ 5].equals( "true");
		_exportLogStepTime = lines[ 6].equals( "true");

		for ( int i = 7; i < lines.length; ++i) {
			if ( !create_chartFrames( lines[ i], chartLogDirectory))
				return false;
		}

		return true;
	}

	/**
	 * @param line
	 * @param chartLogDirectory
	 * @return
	 */
	private boolean create_chartFrames(String line, File chartLogDirectory) {
		String[] words = line.split( "\t");
		if ( null == words || 13 != words.length)
			return false;

		ChartObject chartObject = ( ChartObject)get( words[ 0]);
		if ( null != chartObject) {
			if ( !chartObject.append( words, chartLogDirectory))
				return false;
		} else {
			chartObject = new ChartObject( words);
			if ( !chartObject.setup( words, chartLogDirectory))
				return false;

			put( words[ 0], chartObject);
		}

		return true;
	}

	/**
	 * 
	 */
	public void bring_chartFrames_to_top() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ChartObject chartObject = ( ChartObject)entry.getValue();
			chartObject._animatorInternalChartFrame.toFront();
		}
	}

	/**
	 * 
	 */
	public void cleanup() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ChartObject chartObject = ( ChartObject)entry.getValue();
			chartObject.cleanup();
		}
		clear();
	}

	/**
	 * @param chartObjectMap
	 * @param chartLogDirectory
	 * @return
	 */
	public boolean set(ChartObjectMap chartObjectMap, File chartLogDirectory) {
		_actualEndTime = chartObjectMap._actualEndTime;
		_startTime = chartObjectMap._startTime;
		_stepTime = chartObjectMap._stepTime;
		_endTime = chartObjectMap._endTime;
		_logStepTime = chartObjectMap._logStepTime;
		_exportEndTime = chartObjectMap._exportEndTime;
		_exportLogStepTime = chartObjectMap._exportLogStepTime;

		Iterator iterator = chartObjectMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ChartObject chartObject = ( ChartObject)entry.getValue();
			if ( !chartObject.setup( chartObject._name, chartLogDirectory))
				return false;

			put( chartObject._name, chartObject);
		}

		return true;
	}

	/**
	 * @param internalFrameRectangleMap
	 * @param desktopPane
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(InternalFrameRectangleMap internalFrameRectangleMap, JDesktopPane desktopPane, Writer writer) throws SAXException {
		if ( isEmpty())
			return true;

		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "actual_end_time", "", _actualEndTime[ 0] + "," + _actualEndTime[ 1] + "," + _actualEndTime[ 2]);
		attributesImpl.addAttribute( null, null, "start_time", "", _startTime[ 0] + "," + _startTime[ 1] + "," + _startTime[ 2]);
		attributesImpl.addAttribute( null, null, "step_time", "", _stepTime[ 0] + "," + _stepTime[ 1] + "," + _stepTime[ 2]);
		attributesImpl.addAttribute( null, null, "end_time", "", _endTime[ 0] + "," + _endTime[ 1] + "," + _endTime[ 2]);
		attributesImpl.addAttribute( null, null, "log_step_time", "", _logStepTime[ 0] + "," + _logStepTime[ 1] + "," + _logStepTime[ 2]);
		attributesImpl.addAttribute( null, null, "export_end_time", "", _exportEndTime ? "true" : "false");
		attributesImpl.addAttribute( null, null, "export_log_step_time", "", _exportLogStepTime ? "true" : "false");

		writer.startElement( null, null, "chart", attributesImpl);

		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ChartObject chartObject = ( ChartObject)entry.getValue();
			if ( !chartObject.write( internalFrameRectangleMap, desktopPane, writer))
				return false;
		}

		writer.endElement( null, null, "chart");

		return true;
	}

	/**
	 * @param currentTime
	 */
	public void indicate(double currentTime) {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ChartObject chartObject = ( ChartObject)entry.getValue();
			chartObject.indicate( ( currentTime - get_time( _startTime)) / ( get_time( _actualEndTime) - get_time( _startTime)));
		}
	}

	/**
	 * @param times
	 * @return
	 */
	private double get_time(String[] times) {
		return ( 24.0f * 60.0f * Double.parseDouble( times[ 0]) + 60.0f * Double.parseDouble( times[ 1]) + Double.parseDouble( times[ 2]));
	}

	/**
	 * 
	 */
	public void clear_indication() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ChartObject chartObject = ( ChartObject)entry.getValue();
			chartObject.clear_indication();
		}
	}

	/**
	 * @param chartObjectMap
	 * @return
	 */
	public boolean update(ChartObjectMap chartObjectMap) {
		Iterator iterator = chartObjectMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ChartObject co = ( ChartObject)entry.getValue();
			ChartObject chartObject = /*( ChartObject)*/get( co._name);
			if ( null == chartObject)
				continue;

			if ( !chartObject.update( co))
				return false;
		}
		return true;
	}

	/**
	 * 
	 */
	public void debug() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ChartObject chartObject = ( ChartObject)entry.getValue();
			chartObject.debug();
		}
	}
}
