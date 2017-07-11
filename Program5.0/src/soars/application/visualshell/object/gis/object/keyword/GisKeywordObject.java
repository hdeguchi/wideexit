/**
 * 
 */
package soars.application.visualshell.object.gis.object.keyword;

import java.util.List;

import soars.application.visualshell.object.gis.edit.field.selector.Field;
import soars.application.visualshell.object.gis.object.base.GisSimpleVariableObject;

/**
 * The keyword object class
 * @author kurata / SOARS project
 */
public class GisKeywordObject extends GisSimpleVariableObject {

	/**
	 * Creates this object.
	 */
	public GisKeywordObject() {
		super("keyword");
	}

	/**
	 * Creates this object.
	 * @param name the specified name
	 */
	public GisKeywordObject(String name) {
		super("keyword", name);
	}

	/**
	 * Creates this object with the spcified name and initial value.
	 * @param name the specified name
	 * @param fields the specified initial value
	 */
	public GisKeywordObject(String name, List<Field> fields) {
		super("keyword", name, fields);
	}

	/**
	 * Creates this object with the spcified data.
	 * @param name the specified name
	 * @param fields the specified initial value
	 * @param comment the specified comment
	 */
	public GisKeywordObject(String name, List<Field> fields, String comment) {
		super("keyword", name, fields, comment);
	}

	/**
	 * Creates this object with the spcified data.
	 * @param gisKeywordObject the spcified data
	 */
	public GisKeywordObject(GisKeywordObject gisKeywordObject) {
		super(gisKeywordObject);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if ( !( object instanceof GisKeywordObject))
			return false;

		return super.equals( object);
	}
}
