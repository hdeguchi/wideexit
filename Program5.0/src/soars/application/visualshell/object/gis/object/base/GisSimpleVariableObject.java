/**
 * 
 */
package soars.application.visualshell.object.gis.object.base;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.file.loader.SaxLoader;
import soars.application.visualshell.object.gis.edit.field.selector.Field;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class GisSimpleVariableObject extends GisObjectBase {

	/**
	 * 
	 */
	public List<Field> _fields = new ArrayList<Field>();

	/**
	 * 
	 */
	public int[] _indices = null;

	/**
	 * Creates this object.
	 * @param kind the specified kind
	 */
	public GisSimpleVariableObject(String kind) {
		super(kind);
	}

	/**
	 * Creates this object.
	 * @param kind the specified kind
	 * @param name the specified name
	 */
	public GisSimpleVariableObject(String kind, String name) {
		super(kind, name);
	}

	/**
	 * Creates this object with the spcified name and initial value.
	 * @param kind the specified kind
	 * @param name the specified name
	 * @param fields the specified initial value
	 */
	public GisSimpleVariableObject(String kind, String name, List<Field> fields) {
		super(kind, name);
		_fields = fields;
	}

	/**
	 * Creates this object with the spcified data.
	 * @param kind the specified kind
	 * @param name the specified name
	 * @param fields the specified initial value
	 * @param comment the specified comment
	 */
	public GisSimpleVariableObject(String kind, String name, List<Field> fields, String comment) {
		super(kind, name, comment);
		_fields = fields;
	}

	/**
	 * Creates this object.
	 * @param kind the specified kind
	 */
	public GisSimpleVariableObject(GisSimpleVariableObject gisSimpleVariableObject) {
		super(gisSimpleVariableObject);
		copy( gisSimpleVariableObject._fields);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#copy(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	public void copy(GisObjectBase gisObjectBase) {
		super.copy(gisObjectBase);
		GisSimpleVariableObject simpleVariableObject = ( GisSimpleVariableObject)gisObjectBase;
		copy( simpleVariableObject._fields);
	}

	/**
	 * @param fields
	 */
	public void copy(List<Field> fields) {
		_fields.clear();
		for ( Field field:fields)
			_fields.add( field);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if ( !( obj instanceof GisSimpleVariableObject))
			return false;

		if ( !super.equals( obj))
			return false;

		GisSimpleVariableObject gisSimpleVariableObject = ( GisSimpleVariableObject)obj;
		if ( _fields.size() != gisSimpleVariableObject._fields.size())
			return false;

		for ( int i = 0; i < _fields.size(); ++i) {
			if ( !_fields.get( i).equals( gisSimpleVariableObject._fields.get( i)))
				return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#write(soars.common.utility.xml.sax.Writer, org.xml.sax.helpers.AttributesImpl)
	 */
	protected boolean write(Writer writer, AttributesImpl attributesImpl) throws SAXException {
		if ( !write( attributesImpl))
			return false;

		if ( _comment.equals( "") && _fields.isEmpty())
			writer.writeElement( null, null, SaxLoader._kindTypeMap.get( _kind), attributesImpl);
		else {
			writer.startElement( null, null, SaxLoader._kindTypeMap.get( _kind), attributesImpl);
			if ( !write_fields( writer))
				return false;

			if ( !write_commment( writer))
				return false;

			writer.endElement( null, null, SaxLoader._kindTypeMap.get( _kind));
		}

		return true;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	protected boolean write_fields(Writer writer) throws SAXException {
		for ( Field field:_fields) {
			if ( !field.write( writer))
				return false;
		}
		return true;
	}

//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#contains_this_alias(java.lang.String)
//	 */
//	public boolean contains_this_alias(String alias) {
//		return _initialValue.equals( alias);
//	}
//
//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#get_initial_values1(java.util.Vector)
//	 */
//	public void get_initial_values1(Vector<String> initialValues) {
//		if ( !initialValues.contains( _initialValue))
//			initialValues.add( _initialValue);
//	}
//
//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#get_initial_values2(java.util.Vector)
//	 */
//	public void get_initial_values2(Vector<String> initialValues) {
//		initialValues.add( _initialValue);
//	}
//
//	/**
//	 * @param resource
//	 * @return
//	 */
//	protected String get_initial_data(String resource) {
//		String script = ( resource + "\t" + _name);
//		if ( null != _initialValue && !_initialValue.equals( ""))
//			script +=  ( "\t" + _initialValue);
//
//		return ( script += Constant._lineSeparator);
//	}

//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#write(soars.common.utility.xml.sax.Writer)
//	 */
//	public void write(Writer writer) throws SAXException {
//		AttributesImpl attributesImpl = new AttributesImpl();
//		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
//		attributesImpl.addAttribute( null, null, "value", "", Writer.escapeAttributeCharData( _initialValue));
//		write( writer, attributesImpl);
//	}
//
//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#write(soars.common.utility.xml.sax.Writer, java.lang.String)
//	 */
//	public void write(Writer writer, String number) throws SAXException {
//		AttributesImpl attributesImpl = new AttributesImpl();
//		attributesImpl.addAttribute( null, null, "number", "", Writer.escapeAttributeCharData( number));
//		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
//		attributesImpl.addAttribute( null, null, "value", "", Writer.escapeAttributeCharData( _initialValue));
//		write( writer, attributesImpl);
//	}
//
//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#print()
//	 */
//	public void print() {
//		System.out.println( "\t" + _name + ", " + _initialValue + ", " + _comment);
//	}
//
//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#print(java.util.Vector)
//	 */
//	public void print(Vector<int[]> indices) {
//		if ( indices.isEmpty()) {
//			System.out.println( "\t" + _name + ", " + _initialValue + ", " + _comment);
//			return;
//		}
//
//		for ( int i = 0; i < indices.size(); ++i) {
//			int[] range = indices.get( i);
//			System.out.println( "\t" + _name + ", " + _initialValue + ", " + range[ 0] + "-" + range[ 1] + ", " + _comment);
//		}
//	}
}
