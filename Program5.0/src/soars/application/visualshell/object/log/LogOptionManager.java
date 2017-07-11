/*
 * 2005/06/01
 */
package soars.application.visualshell.object.log;

import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.file.loader.SaxLoader;
import soars.application.visualshell.layer.LayerManager;
import soars.common.utility.xml.sax.Writer;

/**
 * The log option manager for Visual Shell.
 * @author kurata / SOARS project
 */
public class LogOptionManager extends Vector<LogOption> {

	/**
	 * "agent" or "spot"
	 */
	public String _type = "";

	/**
	 * the object kind.
	 */
	public String _kind = "";

	/**
	 * Creates this object.
	 * @param type
	 * @param kind
	 */
	public LogOptionManager(String type, String kind) {
		super();
		_type = type;
		_kind = kind;
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
		for ( LogOption logOption:this)
			logOption.cleanup();

		clear();
	}

	/**
	 * Returns the LogOption which has the specified name.
	 * @param name the specified name
	 * @return the LogOption which has the specified name
	 */
	public LogOption get(String name) {
		for ( LogOption logOption:this) {
			if ( logOption._name.equals( name))
				return logOption;
		}
		return null;
	}

	/**
	 * Returns the flag of the LogOption which has the specified name.
	 * @param name the specified name
	 * @return the flag of the LogOption which has the specified name
	 */
	public boolean get_flag(String name) {
		LogOption logOption = get( name);
		if ( null == logOption)
			return false;

		return logOption._flag;
	}
	/**
	 * Sets the specified flag to the LogOption which has the specified name.
	 * @param name the specified name
	 * @param flag the specified flag
	 */
	public void set_flag(String name, boolean flag) {
		LogOption logOption = get( name);
		if ( null == logOption)
			return;

		logOption._flag = flag;
	}

	/**
	 * @param unit
	 * @param word
	 */
	public void get_script(String[] unit, String word) {
		for ( LogOption logOption:this) {
			if ( logOption._flag) {
				unit[ 0] += ( ( unit[ 0].equals( "") ? "" : "\t")) + word;
				unit[ 1] += ( ( unit[ 1].equals( "") ? "" : "\t")) + logOption._name;
			}
		}
	}

	/**
	 * @param logOption
	 */
	public void append(LogOption logOption) {
		if ( _type.equals( "agent") && _kind.equals( "keyword")) {
			LogOption lo = get( logOption._name);
			if ( null == lo)
				add( logOption);
			else
				lo._flag = logOption._flag;
		} else
			add( logOption);
	}

	/**
	 * Updates this object data.
	 */
	public void update() {
		String[] names = null;
		if ( _type.equals( "agent"))
			names = LayerManager.get_instance().get_agent_object_names( _kind, false);
		else if ( _type.equals( "spot"))
			names = LayerManager.get_instance().get_spot_object_names( _kind, false);

		Vector<LogOption> logOptions;
		if ( _type.equals( "agent") && _kind.equals( "keyword"))
			logOptions = get_current_default_agent_keyword();
		else
			logOptions = new Vector<LogOption>();

		if ( null != names) {
			for ( String name:names)
				logOptions.add( new LogOption( name));
		}

		for ( LogOption logOption:logOptions) {
			for ( LogOption lo:this) {
				if ( lo._name.equals( logOption._name)) {
					logOption._flag = lo._flag;
					break;
				}
			}
		}

		cleanup();

		if ( !logOptions.isEmpty())
			addAll( logOptions);
	}

	/**
	 * Updates this object data.
	 * @param name the old name
	 * @param newName the new name
	 */
	public void update(String name, String newName) {
		for ( LogOption logOption:this) {
			if ( logOption._name.equals( name)) {
				logOption._name = newName;
				break;
			}
		}
	}

	/**
	 * @return
	 */
	private Vector<LogOption> get_current_default_agent_keyword() {
		Vector<LogOption> logOptions = new Vector<LogOption>();

		LogOption nameLogOption = new LogOption( "$Name");
		LogOption roleLogOption = new LogOption( "$Role");
		LogOption spotLogOption = new LogOption( "$Spot");

		logOptions.add( nameLogOption);
		logOptions.add( roleLogOption);
		logOptions.add( spotLogOption);

		for ( LogOption logOption:this) {
			if ( logOption._name.equals( "$Name"))
				nameLogOption._flag = logOption._flag;
			if ( logOption._name.equals( "$Role"))
				roleLogOption._flag = logOption._flag;
			if ( logOption._name.equals( "$Spot"))
				spotLogOption._flag = logOption._flag;
		}

		return logOptions;
	}

	/**
	 * @param writer
	 * @throws SAXException
	 */
	public void write(Writer writer) throws SAXException {
		if ( isEmpty())
			return;

		writer.startElement( null, null, _type + "_" + SaxLoader._kindTagMap.get( _kind), new AttributesImpl());

		for ( LogOption logOption:this)
			logOption.write( _type + "_" + SaxLoader._kindTypeMap.get( _kind), writer);

		writer.endElement( null, null, _type + "_" + SaxLoader._kindTagMap.get( _kind));
	}
}
