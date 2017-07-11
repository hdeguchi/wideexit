/**
 * 
 */
package soars.application.visualshell.object.gis.object.time;

import java.util.List;

import soars.application.visualshell.object.gis.edit.field.selector.Field;
import soars.application.visualshell.object.gis.object.base.GisSimpleVariableObject;

/**
 * @author kurata
 *
 */
public class GisTimeVariableObject extends GisSimpleVariableObject {

	/**
	 * 
	 */
	public GisTimeVariableObject() {
		super("time variable");
	}

	/**
	 * @param name
	 */
	public GisTimeVariableObject(String name) {
		super("time variable", name);
	}

	/**
	 * @param name
	 * @param fields
	 */
	public GisTimeVariableObject(String name, List<Field> fields) {
		super("time variable", name, fields);
	}

	/**
	 * @param name
	 * @param fields
	 * @param comment
	 */
	public GisTimeVariableObject(String name, List<Field> fields, String comment) {
		super("time variable", name, fields, comment);
	}

	/**
	 * @param gisTimeVariableObject
	 */
	public GisTimeVariableObject(GisTimeVariableObject gisTimeVariableObject) {
		super(gisTimeVariableObject);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if ( !( object instanceof GisTimeVariableObject))
			return false;

		return super.equals(object);
	}
}
