/*
 * 2005/05/31
 */
package soars.application.visualshell.object.simulation;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.xml.sax.Writer;


/**
 * @author kurata
 */
public class SimulationManager {

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
	public String _randomSeed = "";

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private SimulationManager _simulationManager = null;

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
			if ( null == _simulationManager) {
				_simulationManager = new SimulationManager();
			}
		}
	}

	/**
	 * @return
	 */
	public static SimulationManager get_instance() {
		if ( null == _simulationManager) {
			System.exit( 0);
		}

		return _simulationManager;
	}

	/**
	 * 
	 */
	public SimulationManager() {
		super();
	}

	/**
	 * 
	 */
	public void cleanup() {
		for ( int i = 0; i < _startTime.length; ++i) {
			if ( 0 == i) {
				_startTime[ i] = "0";
				_stepTime[ i] = "0";
				_endTime[ i] = "0";
				_logStepTime[ i] = "0";
			} else {
				_startTime[ i] = "00";
				_stepTime[ i] = "00";
				_endTime[ i] = "00";
				_logStepTime[ i] = "00";
			}
		}

		_randomSeed = "";
	}

	public boolean update(Map<String, String[]> simulationDataMap) {
		Iterator iterator = simulationDataMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String key = ( String)entry.getKey();
			String[] values = ( String[])entry.getValue();
			if ( key.equals( ResourceManager.get_instance().get( "initial.data.simulation.start.time")))
				_startTime = values;
			else if ( key.equals( ResourceManager.get_instance().get( "initial.data.simulation.step.time")))
				_stepTime = values;
			else if ( key.equals( ResourceManager.get_instance().get( "initial.data.simulation.end.time")))
				_endTime = values;
			else if ( key.equals( ResourceManager.get_instance().get( "initial.data.simulation.log.step.time")))
				_logStepTime = values;
			else if ( key.equals( ResourceManager.get_instance().get( "initial.data.simulation.export.end.time")))
				_exportEndTime = ( values[ 0].equals( "true") ? true : false);
//			else if ( key.equals( ResourceManager.get_instance().get( "initial.data.simulation.export.log.step.time")))
//				_export_log_step_time = ( values[ 0].equals( "true") ? true : false);
			else if ( key.equals( ResourceManager.get_instance().get( "initial.data.simulation.random.seed")))
				_randomSeed = values[ 0];
		}

		if ( log_step_time_equals_zero())
			substitute_step_time_into_log_step_time();

		return true;
	}

	/**
	 * @param value
	 * @return
	 */
	public boolean is_correct_random_seed(String value) {
		// TODO Auto-generated method stub
		if ( value.equals( "$") || value.startsWith( "0"))
			return false;

		if ( value.startsWith( "$")) {
			if ( 1 < value.length() && 0 < value.indexOf( "$", 1))
				return false;
		} else {
			double d;
			try {
				d = Double.parseDouble( value);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	/**
	 * @param alias
	 * @return
	 */
	public boolean contains_this_alias(String alias) {
		return _randomSeed.equals( alias);
	}

	/**
	 * @return
	 */
	public String[] get_initial_values() {
		Vector<String> initialValues = new Vector<String>();
		if ( !initialValues.contains( _randomSeed))
			initialValues.add( _randomSeed);

		return Tool.quick_sort_string( initialValues, true, false);
	}

	/**
	 * @return
	 */
	public String get_initial_data() {
		String script = "";
		script += ( ResourceManager.get_instance().get( "initial.data.simulation")
			+ "\t" + ResourceManager.get_instance().get( "initial.data.simulation.start.time")
			+ "\t" + _startTime[ 0] + "\t" + _startTime[ 1] + "\t" + _startTime[ 2] + Constant._lineSeparator);
		script += ( ResourceManager.get_instance().get( "initial.data.simulation")
			+ "\t" + ResourceManager.get_instance().get( "initial.data.simulation.step.time")
			+ "\t" + _stepTime[ 0] + "\t" + _stepTime[ 1] + "\t" + _stepTime[ 2] + Constant._lineSeparator);
		script += ( ResourceManager.get_instance().get( "initial.data.simulation")
			+ "\t" + ResourceManager.get_instance().get( "initial.data.simulation.end.time")
			+ "\t" + _endTime[ 0] + "\t" + _endTime[ 1] + "\t" + _endTime[ 2] + Constant._lineSeparator);
		script += ( ResourceManager.get_instance().get( "initial.data.simulation")
			+ "\t" + ResourceManager.get_instance().get( "initial.data.simulation.log.step.time")
			+ "\t" + _logStepTime[ 0] + "\t" + _logStepTime[ 1] + "\t" + _logStepTime[ 2] + Constant._lineSeparator);
		script += ( ResourceManager.get_instance().get( "initial.data.simulation")
			+ "\t" + ResourceManager.get_instance().get( "initial.data.simulation.export.end.time")
			+ "\t" + String.valueOf( _exportEndTime) + Constant._lineSeparator);
//		script += ( ResourceManager.get_instance().get( "initial.data.simulation")
//			+ "\t" + ResourceManager.get_instance().get( "initial.data.simulation.export.log.step.time")
//			+ "\t" + String.valueOf( _export_log_step_time) + Constant._line_separator);
		script += ( _randomSeed.equals( "") ? ""
			: ( ResourceManager.get_instance().get( "initial.data.simulation")
			+ "\t" + ResourceManager.get_instance().get( "initial.data.simulation.random.seed")
			+ "\t" + _randomSeed + Constant._lineSeparator));
		return ( script + Constant._lineSeparator);
	}

	/**
	 * @param initialValueMap
	 * @return
	 */
	public String get_script(InitialValueMap initialValueMap) {
		String randomSeed = get_random_seed( initialValueMap);

		String script = "itemData" + Constant._lineSeparator;
		script += "logStepTime\tenvStartTime\tenvStepTime";
		script += ( randomSeed.equals( "") ? "" : "\tenvRandomSeed");
		script += ( "\texecUntil" + Constant._lineSeparator);
//		script += ( _export_log_step_time ? get_time( _log_step_time) : "");
		script += get_time( _logStepTime);
		script += "\t";
		script += get_time( _startTime);
		script += "\t";
		script += get_time( _stepTime);
		script += ( randomSeed.equals( "") ? "" : ( "\t" + randomSeed));
		script += "\t";
		script += ( _exportEndTime ? get_time( _endTime) : "");
		script += Constant._lineSeparator;
		return ( script + Constant._lineSeparator);
	}

	/**
	 * @param initialValueMap
	 * @return
	 */
	private String get_random_seed(InitialValueMap initialValueMap) {
		String randomSeed = ( ( null == initialValueMap) ? _randomSeed : initialValueMap.get_initial_value( _randomSeed));
		return ( ( null == randomSeed) ? "" : randomSeed);
	}

	/**
	 * @param time
	 * @return
	 */
	private String get_time(String[] time) {
		return ( time[ 0] + "/" + time[ 1] + ":" + time[ 2]);
//		if ( !time[ 0].equals( "0"))
//			return ( time[ 0] + "/" + time[ 1] + ":" + time[ 2]);
//		else {
//			if ( !time[ 1].equals( "00"))
//				return ( time[ 1] + ":" + time[ 2]);
//			else
//				return time[ 2];
//		}
	}

	/**
	 * @param writer
	 * @return
	 */
	public boolean write(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "export_end_time", "", _exportEndTime ? "true" : "false");
//		attributesImpl.addAttribute( null, null, "export_log_step_time", "", _export_log_step_time ? "true" : "false");
		attributesImpl.addAttribute( null, null, "random_seed", "", _randomSeed);
		writer.startElement( null, null, "simulation_data", attributesImpl);

		write( "start", _startTime, writer);
		write( "step", _stepTime, writer);
		write( "end", _endTime, writer);
		write( "log_step", _logStepTime, writer);

		writer.endElement( null, null, "simulation_data");

		return true;
	}

	/**
	 * @param name
	 * @param values
	 * @param writer
	 */
	private void write(String name, String[] values, Writer writer) throws SAXException {
		String[] names = new String[] { "day", "hour", "minute"};
		AttributesImpl attributesImpl = new AttributesImpl();

		for ( int i = 0; i < values.length; ++i)
			attributesImpl.addAttribute( null, null, names[ i], "", Writer.escapeAttributeCharData( values[ i]));

		writer.writeElement( null, null, name, attributesImpl);
		//writer.startElement( null, null, name, attributesImpl);
		//writer.endElement( null, null, name);
	}

	/**
	 * @return
	 */
	public String get_properties() {
		// TODO Auto-generated method stub
		return ( _startTime[ 0] + "," + _startTime[ 1] + "," + _startTime[ 2] + "\n"
			+ _stepTime[ 0] + "," + _stepTime[ 1] + "," + _stepTime[ 2] + "\n"
			+ _endTime[ 0] + "," + _endTime[ 1] + "," + _endTime[ 2] + "\n"
			+ _logStepTime[ 0] + "," + _logStepTime[ 1] + "," + _logStepTime[ 2] + "\n"
			+ ( _exportEndTime ? "true" : "false") + "\n"
			+ "true\n");
//			+ ( _export_log_step_time ? "true" : "false") + "\n");
	}

	/**
	 * @return
	 */
	public boolean log_step_time_equals_step_time() {
		// TODO Auto-generated method stub
		return get_time( _logStepTime).equals( get_time( _stepTime));
	}

	/**
	 * @return
	 */
	public boolean log_step_time_equals_zero() {
		// TODO Auto-generated method stub
		return get_time( _logStepTime).equals( "0/00:00");
	}

	/**
	 * 
	 */
	public void substitute_step_time_into_log_step_time() {
		// TODO Auto-generated method stub
		System.arraycopy( _stepTime, 0, _logStepTime, 0, _stepTime.length);
	}
}
