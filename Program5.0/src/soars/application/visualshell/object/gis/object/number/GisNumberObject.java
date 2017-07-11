/*
 * 2005/05/20
 */
package soars.application.visualshell.object.gis.object.number;

import java.util.List;

import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.edit.field.selector.Field;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.application.visualshell.object.gis.object.base.GisSimpleVariableObject;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 */
public class GisNumberObject extends GisSimpleVariableObject {

	/**
	 * 
	 */
	public String _type = "";	// "integer" or "real number"

	/**
	 * 
	 */
	public GisNumberObject() {
		super("number object");
	}

	/**
	 * @param name
	 * @param type
	 */
	public GisNumberObject(String name, String type) {
		super("number object", name);
		_type = type;
	}

	/**
	 * @param name
	 * @param type
	 * @param fields
	 */
	public GisNumberObject(String name, String type, List<Field> fields) {
		super("number object", name, fields);
		_type = type;
	}

	/**
	 * @param name
	 * @param type
	 * @param fields
	 * @param comment
	 */
	public GisNumberObject(String name, String type, List<Field> fields, String comment) {
		super("number object", name, fields, comment);
		_type = type;
	}

	/**
	 * @param gisNumberObject
	 */
	public GisNumberObject(GisNumberObject gisNumberObject) {
		super(gisNumberObject);
		_type = gisNumberObject._type;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#copy(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	public void copy(GisObjectBase gisObjectBase) {
		super.copy(gisObjectBase);
		GisNumberObject gisNumberObject = ( GisNumberObject)gisObjectBase;
		_type = gisNumberObject._type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if ( !( object instanceof GisNumberObject))
			return false;

		if ( !super.equals( object))
			return false;

		GisNumberObject gisNumberObject = ( GisNumberObject)object;

		return _type.equals( gisNumberObject._type);
	}

	/**
	 * @param type
	 * @return
	 */
	public static String get_type_name(String type) {
		if ( type.equals( "integer"))
			return ResourceManager.get_instance().get( "number.object.integer");
		else if ( type.equals( "real number"))
			return ResourceManager.get_instance().get( "number.object.real.number");

		return "";
	}

	/**
	 * @param typeName
	 * @return
	 */
	public static String get_type(String typeName) {
		if ( typeName.equals( ResourceManager.get_instance().get( "number.object.integer")))
			return "integer";
		else if ( typeName.equals( ResourceManager.get_instance().get( "number.object.real.number")))
			return "real number";

		return "";
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#write(org.xml.sax.helpers.AttributesImpl)
	 */
	@Override
	protected boolean write(AttributesImpl attributesImpl) {
		if (!super.write(attributesImpl))
			return false;

		attributesImpl.addAttribute( null, null, "type", "", Writer.escapeAttributeCharData( _type));
		return true;
	}

//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#write(soars.common.utility.xml.sax.Writer)
//	 */
//	@Override
//	public void write(Writer writer) throws SAXException {
//		AttributesImpl attributesImpl = new AttributesImpl();
//		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
//		attributesImpl.addAttribute( null, null, "type", "", Writer.escapeAttributeCharData( _type));
//		attributesImpl.addAttribute( null, null, "initial_value", "", Writer.escapeAttributeCharData( _initialValue));
//		write( writer, attributesImpl);
//	}
//
//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#write(soars.common.utility.xml.sax.Writer, java.lang.String)
//	 */
//	@Override
//	public void write(Writer writer, String number) throws SAXException {
//		AttributesImpl attributesImpl = new AttributesImpl();
//		attributesImpl.addAttribute( null, null, "number", "", Writer.escapeAttributeCharData( number));
//		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
//		attributesImpl.addAttribute( null, null, "type", "", Writer.escapeAttributeCharData( _type));
//		attributesImpl.addAttribute( null, null, "initial_value", "", Writer.escapeAttributeCharData( _initialValue));
//		write( writer, attributesImpl);
//	}
}
