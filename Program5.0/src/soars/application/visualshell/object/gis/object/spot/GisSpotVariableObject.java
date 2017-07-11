/**
 * 
 */
package soars.application.visualshell.object.gis.object.spot;

import java.util.List;

import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.object.gis.edit.field.selector.Field;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.application.visualshell.object.gis.object.base.GisSimpleVariableObject;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class GisSpotVariableObject extends GisSimpleVariableObject {

	/**
	 * This object's initial value.
	 */
	public String _initialValue = "";

	/**
	 * 
	 */
	public GisSpotVariableObject() {
		super("spot variable");
	}

	/**
	 * @param name
	 */
	public GisSpotVariableObject(String name) {
		super("spot variable", name);
	}

	/**
	 * @param name
	 * @param initialValue
	 */
	public GisSpotVariableObject(String name, String initialValue) {
		super("spot variable", name);
		_initialValue = initialValue;
	}

	/**
	 * @param name
	 * @param fields
	 */
	public GisSpotVariableObject(String name, List<Field> fields) {
		super("spot variable", name, fields);
	}

	/**
	 * @param name
	 * @param fields
	 * @param comment
	 */
	public GisSpotVariableObject(String name, List<Field> fields, String comment) {
		super("spot variable", name, fields, comment);
	}

	/**
	 * @param gisSpotVariableObject
	 */
	public GisSpotVariableObject(GisSpotVariableObject gisSpotVariableObject) {
		super(gisSpotVariableObject);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisSimpleVariableObject#copy(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	public void copy(GisObjectBase gisObjectBase) {
		super.copy(gisObjectBase);
		GisSpotVariableObject gisSpotVariableObject = ( GisSpotVariableObject)gisObjectBase;
		_initialValue = gisSpotVariableObject._initialValue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if ( !( object instanceof GisSpotVariableObject))
			return false;

		if ( !super.equals(object))
			return false;

		GisSpotVariableObject gisSpotVariableObject = ( GisSpotVariableObject)object;

		return _initialValue.equals( gisSpotVariableObject._initialValue);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#write(org.xml.sax.helpers.AttributesImpl)
	 */
	@Override
	protected boolean write(AttributesImpl attributesImpl) {
		if (!super.write(attributesImpl))
			return false;

		attributesImpl.addAttribute( null, null, "value", "", Writer.escapeAttributeCharData( _initialValue));
		return true;
	}
}
