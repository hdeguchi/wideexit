/**
 * 
 */
package soars.application.visualshell.object.entity.base.object.base;

import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.main.Constant;
import soars.common.utility.xml.sax.Writer;


/**
 * @author kurata
 *
 */
public class SimpleVariableObject extends ObjectBase {

	/**
	 * This object's initial value.
	 */
	public String _initialValue = "";

	/**
	 * Creates this object.
	 * @param kind the specified kind
	 */
	public SimpleVariableObject(String kind) {
		super(kind);
	}

	/**
	 * Creates this object with the spcified name and initial value.
	 * @param kind the specified kind
	 * @param name the specified name
	 * @param initialValue the specified initial value
	 */
	public SimpleVariableObject(String kind, String name, String initialValue) {
		super(kind, name);
		_initialValue = initialValue;
	}

	/**
	 * Creates this object with the spcified data.
	 * @param kind the specified kind
	 * @param name the specified name
	 * @param initialValue the specified initial value
	 * @param comment the specified comment
	 */
	public SimpleVariableObject(String kind, String name, String initialValue, String comment) {
		super(kind, name, comment);
		_initialValue = initialValue;
	}

	/**
	 * Creates this object.
	 * @param kind the specified kind
	 */
	public SimpleVariableObject(SimpleVariableObject simpleVariableObject) {
		super(simpleVariableObject);
		_initialValue = simpleVariableObject._initialValue;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#copy(soars.application.visualshell.object.entiy.base.object.base.ObjectBase)
	 */
	public void copy(ObjectBase objectBase) {
		super.copy(objectBase);
		SimpleVariableObject simpleVariableObject = ( SimpleVariableObject)objectBase;
		_initialValue = simpleVariableObject._initialValue;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if ( !( obj instanceof SimpleVariableObject))
			return false;

		if ( !super.equals( obj))
			return false;

		SimpleVariableObject simpleVariableObject = ( SimpleVariableObject)obj;
		return _initialValue.equals( simpleVariableObject._initialValue);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#contains_this_alias(java.lang.String)
	 */
	public boolean contains_this_alias(String alias) {
		return _initialValue.equals( alias);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_initial_values1(java.util.Vector)
	 */
	public void get_initial_values1(Vector<String> initialValues) {
		if ( !initialValues.contains( _initialValue))
			initialValues.add( _initialValue);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_initial_values2(java.util.Vector)
	 */
	public void get_initial_values2(Vector<String> initialValues) {
		initialValues.add( _initialValue);
	}

	/**
	 * @param resource
	 * @return
	 */
	protected String get_initial_data(String resource) {
		String script = ( resource + "\t" + _name);
		if ( null != _initialValue && !_initialValue.equals( ""))
			script +=  ( "\t" + _initialValue);

		return ( script += Constant._lineSeparator);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#write(soars.common.utility.xml.sax.Writer)
	 */
	public void write(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
		attributesImpl.addAttribute( null, null, "value", "", Writer.escapeAttributeCharData( _initialValue));
		write( writer, attributesImpl);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#write(soars.common.utility.xml.sax.Writer, java.lang.String)
	 */
	public void write(Writer writer, String number) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "number", "", Writer.escapeAttributeCharData( number));
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
		attributesImpl.addAttribute( null, null, "value", "", Writer.escapeAttributeCharData( _initialValue));
		write( writer, attributesImpl);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#print()
	 */
	public void print() {
		System.out.println( "\t" + _name + ", " + _initialValue + ", " + _comment);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#print(java.util.Vector)
	 */
	public void print(Vector<int[]> indices) {
		if ( indices.isEmpty()) {
			System.out.println( "\t" + _name + ", " + _initialValue + ", " + _comment);
			return;
		}

		for ( int i = 0; i < indices.size(); ++i) {
			int[] range = ( int[])indices.get( i);
			System.out.println( "\t" + _name + ", " + _initialValue + ", " + range[ 0] + "-" + range[ 1] + ", " + _comment);
		}
	}
}
