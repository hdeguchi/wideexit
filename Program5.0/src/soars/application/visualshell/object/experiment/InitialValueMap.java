/*
 * 2005/07/12
 */
package soars.application.visualshell.object.experiment;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.common.utility.tool.common.Tool;
import soars.common.utility.xml.sax.Writer;

/**
 * The initial value hashtable.
 * @author kurata / SOARS project
 */
public class InitialValueMap extends TreeMap<String, String> {

	/**
	 * Flag which indicates whether to export the ModelBuilder script.
	 */
	public boolean _export = false;

	/**
	 * Comment for this object.
	 */
	public String _comment = "";

	/**
	 * Cerates this object.
	 */
	public InitialValueMap() {
		super();
	}

	/**
	 * Cerates this object with the specified data.
	 * @param export
	 */
	public InitialValueMap(boolean export) {
		super();
		_export = export;
	}

	/**
	 * Cerates this object with the specified data.
	 * @param initialValueMap the specified data
	 */
	public InitialValueMap(InitialValueMap initialValueMap) {
		super();
		copy( initialValueMap);
	}

	/**
	 * @param initialValueMap
	 */
	private void copy(InitialValueMap initialValueMap) {
		cleanup();

		Iterator iterator = initialValueMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String alias = ( String)entry.getKey();
			String initial_value = ( String)entry.getValue();
			put( alias, initial_value);
		}

		_export = initialValueMap._export;
		_comment = initialValueMap._comment;
	}

	/**
	 * @param initialValueMap
	 * @return
	 */
	public boolean same_as(InitialValueMap initialValueMap) {
		// TODO Auto-generated method stub
		Iterator iterator = initialValueMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String alias = ( String)entry.getKey();
			if ( null == get( alias) || null == entry.getValue())
				return false;

			if ( !( ( String)get( alias)).equals( ( String)entry.getValue()))
				return false;
		}

		if ( _export != initialValueMap._export)
			return false;

		if ( !_comment.equals( initialValueMap._comment))
			return false;

		return true;
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
		clear();
		_export = false;
		_comment = "";
	}

	/**
	 * @param values
	 * @return 
	 */
	public boolean update(List<String> values) {
		// TODO Auto-generated method stub
		if ( size() < values.size())
			return false;

		int index = 0;
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String key = ( String)entry.getKey();
			if ( !key.startsWith( "$__val"))
				continue;

			put( key, values.get( index++));
		}

		return true;
	}

	/**
	 * @param name
	 * @return
	 */
	public String[] get_data_for_table(String name) {
		// TODO Auto-generated method stub
		String[] data = new String[ size() + 3];

		data[ 0] = _export ? "true" : "false";
		data[ 1] = name;
		data[ 2] = _comment;
		int index = 3;
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			data[ index++] = ( String)entry.getValue();
		}

		return data;
	}

	/**
	 * Returns the array of the aliases.
	 * @return the array of the aliases
	 */
	public String[] get_aliases() {
		Vector<String> aliases = new Vector<String>();

		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			aliases.add( ( String)entry.getKey());
		}

		if ( aliases.isEmpty())
			return null;

		return ( String[])aliases.toArray( new String[ 0]);
	}

	/**
	 * Returns the initial value to which this map maps the specified key.
	 * @param value the specified key
	 * @return the initial value to which this map maps the specified key
	 */
	public String get_initial_value(String value) {
		if ( value.equals( "") || !value.matches( "\\$.+"))
			return value;

		return ( String)get(value);
	}

	/**
	 * Returns the ModelBuilder script in which all aliases are replaced with the initial values.
	 * @param value the original ModelBuilder script
	 * @return the ModelBuilder script
	 */
	public String get_script(String value) {
		if ( value.equals( ""))
			return value;

		String result = value;
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String alias = ( String)entry.getKey();
			String initialValue = ( String)entry.getValue();
			result = replaces( result, alias, initialValue);
		}
		return result;
	}

	/**
	 * @param result
	 * @param alias
	 * @param initialValue
	 * @return
	 */
	private String replaces(String result, String alias, String initialValue) {
		if ( result.endsWith( alias)) {
			int index = result.lastIndexOf( alias);
			if ( 0 <= index)
				result = result.substring( 0, index) + initialValue;
		}

		for ( int i = 0; i < ExperimentManager._suffixes.length; ++i)
			result = Tool.replace_all( result, alias + ExperimentManager._suffixes[ i],
				initialValue + ExperimentManager._suffixes[ i]);

		return result;
	}

	/**
	 * Returns true for writing this object data successfully.
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing this object data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public void write(Writer writer) throws SAXException {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String alias = ( String)entry.getKey();
			String initialValue = ( String)entry.getValue();

			AttributesImpl attributesImpl = new AttributesImpl();
			attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( alias));
			attributesImpl.addAttribute( null, null, "value", "", Writer.escapeAttributeCharData( initialValue));
			writer.writeElement( null, null, "initial_value", attributesImpl);
		}
	}
}
