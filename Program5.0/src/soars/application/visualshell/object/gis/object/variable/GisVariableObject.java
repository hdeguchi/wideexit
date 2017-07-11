/*
 * Created on 2006/10/13
 */
package soars.application.visualshell.object.gis.object.variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.file.loader.SaxLoader;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 */
public class GisVariableObject extends GisObjectBase {

	/**
	 * 
	 */
	public List<GisVariableInitialValue> _gisVariableInitialValues = new ArrayList<GisVariableInitialValue>();

	/**
	 * @param kind
	 */
	public GisVariableObject(String kind) {
		super(kind);
	}

	/**
	 * @param kind
	 * @param name
	 */
	public GisVariableObject(String kind, String name) {
		super(kind, name);
	}

	/**
	 * @param gisVariableObject
	 */
	public GisVariableObject(GisVariableObject gisVariableObject) {
		super(gisVariableObject);
		for ( GisVariableInitialValue gisVariableInitialValue:gisVariableObject._gisVariableInitialValues)
			_gisVariableInitialValues.add( new GisVariableInitialValue( gisVariableInitialValue));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#copy(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	public void copy(GisObjectBase gisObjectBase) {
		super.copy(gisObjectBase);
		GisVariableObject gisVariableObject = ( GisVariableObject)gisObjectBase;
		for ( GisVariableInitialValue gisVariableInitialValue:gisVariableObject._gisVariableInitialValues)
			_gisVariableInitialValues.add( new GisVariableInitialValue( gisVariableInitialValue));
	}

	/* (Non Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public synchronized boolean equals(Object object) {
		if ( !( object instanceof GisVariableObject))
			return false;

		if ( !super.equals( object))
			return false;

		GisVariableObject gisVariableObject = ( GisVariableObject)object;

		if ( _gisVariableInitialValues.size() != gisVariableObject._gisVariableInitialValues.size())
			return false;

		for ( int i = 0; i < _gisVariableInitialValues.size(); ++i) {
			if ( !_gisVariableInitialValues.get( i).equals( gisVariableObject._gisVariableInitialValues.get( i)))
				return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#contains(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	public boolean contains(GisObjectBase gisObjectBase) {
		for ( GisVariableInitialValue gisVariableInitialValue:_gisVariableInitialValues) {
			if ( gisVariableInitialValue.contains( gisObjectBase))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#contains(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, soars.application.visualshell.object.entiy.base.EntityBase)
	 */
	@Override
	public boolean contains(String kind, String type, String headName, Vector<String[]> ranges, String name/*, EntityBase entityBase*/) {
		for ( GisVariableInitialValue gisVariableInitialValue:_gisVariableInitialValues) {
			if ( gisVariableInitialValue.contains( kind, name, type, headName, ranges/*, entityBase*/))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#contains(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector, soars.application.visualshell.object.entiy.base.EntityBase)
	 */
	@Override
	public boolean contains(String kind, String name, String type, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges/*, EntityBase entityBase*/) {
		for ( GisVariableInitialValue gisVariableInitialValue:_gisVariableInitialValues) {
			if ( gisVariableInitialValue.contains( kind, name, type, headName, ranges, newHeadName, newRanges/*, entityBase*/))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#can_adjust_name(java.lang.String, java.lang.String, java.util.Vector, soars.application.visualshell.object.entiy.base.EntityBase)
	 */
	@Override
	public boolean can_adjust_name(String type, String headName, Vector<String[]> ranges/*, EntityBase entityBase*/) {
		for ( GisVariableInitialValue gisVariableInitialValue:_gisVariableInitialValues) {
			if ( !gisVariableInitialValue.can_adjust_name( _kind, _name, type, headName, ranges/*, entityBase*/))
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#can_adjust_name(java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector, soars.application.visualshell.object.entiy.base.EntityBase)
	 */
	@Override
	public boolean can_adjust_name(String type, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges/*, EntityBase entityBase*/) {
		for ( GisVariableInitialValue gisVariableInitialValue:_gisVariableInitialValues) {
			if ( !gisVariableInitialValue.can_adjust_name( _kind, _name, type, headName, ranges, newHeadName, newRanges/*, entityBase*/))
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#update_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_name_and_number(String type, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = false;
		for ( GisVariableInitialValue gisVariableInitialValue:_gisVariableInitialValues) {
			if ( gisVariableInitialValue.update_name_and_number( type, newName, originalName, headName, ranges, newHeadName, newRanges))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#update_object_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_object_name(String type, String name, String newName) {
		boolean result = false;
		for ( GisVariableInitialValue gisVariableInitialValue:_gisVariableInitialValues) {
			if ( gisVariableInitialValue.update_object_name( type, name, newName))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#contains_this_alias(java.lang.String)
	 */
	@Override
	public boolean contains_this_alias(String alias) {
		for ( GisVariableInitialValue gisVariableInitialValue:_gisVariableInitialValues) {
			if ( gisVariableInitialValue.contains_this_alias( alias))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#get_initial_values1(java.util.Vector)
	 */
	@Override
	public void get_initial_values1(Vector<String> initialValues) {
		for ( GisVariableInitialValue gisVariableInitialValue:_gisVariableInitialValues)
			gisVariableInitialValue.get_initial_values1( initialValues);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#get_initial_values2(java.util.Vector)
	 */
	@Override
	public void get_initial_values2(Vector<String> initialValues) {
		for ( GisVariableInitialValue gisVariableInitialValue:_gisVariableInitialValues)
			gisVariableInitialValue.get_initial_values2( initialValues);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#get_initial_data()
	 */
	@Override
	public String get_initial_data() {
		String script = ( ResourceManager.get_instance().get( _kind.equals( "collection") ? "initial.data.collection" : "initial.data.list") + "\t" + _name);
		for ( GisVariableInitialValue gisVariableInitialValue:_gisVariableInitialValues)
			script += gisVariableInitialValue.get_initial_data();
		return ( script + Constant._lineSeparator);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#write(soars.common.utility.xml.sax.Writer, org.xml.sax.helpers.AttributesImpl)
	 */
	@Override
	protected boolean write(Writer writer, AttributesImpl attributesImpl) throws SAXException {
		if ( !write( attributesImpl))
			return false;

		if ( _comment.equals( "") && _gisVariableInitialValues.isEmpty())
			writer.writeElement( null, null, SaxLoader._kindTypeMap.get( _kind), attributesImpl);
		else {
			writer.startElement( null, null, SaxLoader._kindTypeMap.get( _kind), attributesImpl);
			for ( GisVariableInitialValue gisVariableInitialValue:_gisVariableInitialValues)
				if ( !gisVariableInitialValue.write( writer))
					return false;

			if ( !write_commment( writer))
				return false;

			writer.endElement( null, null, SaxLoader._kindTypeMap.get( _kind));
		}

		return true;
	}

//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#write(soars.common.utility.xml.sax.Writer)
//	 */
//	@Override
//	public void write(Writer writer) throws SAXException {
//		AttributesImpl attributesImpl = new AttributesImpl();
//		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
//		write( writer, attributesImpl);
//	}

//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#write(soars.common.utility.xml.sax.Writer, java.lang.String)
//	 */
//	@Override
//	public void write(Writer writer, String number) throws SAXException {
//		AttributesImpl attributesImpl = new AttributesImpl();
//		attributesImpl.addAttribute( null, null, "number", "", Writer.escapeAttributeCharData( number));
//		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
//		write( writer, attributesImpl);
//	}

//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#write(soars.common.utility.xml.sax.Writer, org.xml.sax.helpers.AttributesImpl)
//	 */
//	@Override
//	protected void write(Writer writer, AttributesImpl attributesImpl) throws SAXException {
//		for ( int i = 0; i < _variableInitialValues.size(); ++i)
//			_gisVariableInitialValues.get( i).write( i, attributesImpl);
//
//		super.write(writer, attributesImpl);
//	}
}
