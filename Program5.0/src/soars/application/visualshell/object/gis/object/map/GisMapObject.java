/**
 * 
 */
package soars.application.visualshell.object.gis.object.map;

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
 *
 */
public class GisMapObject extends GisObjectBase {

	/**
	 * 
	 */
	public List<GisMapInitialValue> _gisMapInitialValues = new ArrayList<GisMapInitialValue>();

	/**
	 * 
	 */
	public GisMapObject() {
		super("map");
	}

	/**
	 * @param name
	 */
	public GisMapObject(String name) {
		super("map", name);
	}

	/**
	 * @param gisMapObject
	 */
	public GisMapObject(GisMapObject gisMapObject) {
		super(gisMapObject);
		for ( GisMapInitialValue gisMapInitialValue:gisMapObject._gisMapInitialValues)
			_gisMapInitialValues.add( new GisMapInitialValue( gisMapInitialValue));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#copy(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	public void copy(GisObjectBase gisObjectBase) {
		super.copy(gisObjectBase);
		GisMapObject mapObject = ( GisMapObject)gisObjectBase;
		for ( GisMapInitialValue gisMapInitialValue:mapObject._gisMapInitialValues)
			_gisMapInitialValues.add( new GisMapInitialValue( gisMapInitialValue));
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractMap#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if ( !( object instanceof GisMapObject))
			return false;

		if ( !super.equals( object))
			return false;

		GisMapObject gisMapObject = ( GisMapObject)object;

		if ( gisMapObject._gisMapInitialValues.size() != _gisMapInitialValues.size())
			return false;

		for ( int i = 0; i < gisMapObject._gisMapInitialValues.size(); ++i) {
			if ( !gisMapObject._gisMapInitialValues.get( i).equals( _gisMapInitialValues.get( i)))
				return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#contains(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	public boolean contains(GisObjectBase gisObjectBase) {
		for ( GisMapInitialValue mapInitialValue:_gisMapInitialValues) {
			if ( mapInitialValue.contains( gisObjectBase))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#contains(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, soars.application.visualshell.object.entiy.base.EntityBase)
	 */
	@Override
	public boolean contains(String kind, String type, String headName, Vector<String[]> ranges, String name/*, EntityBase entityBase*/) {
		for ( GisMapInitialValue gisMapInitialValue:_gisMapInitialValues) {
			if ( gisMapInitialValue.contains( name, type, headName, ranges/*, entityBase*/))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#contains(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector, soars.application.visualshell.object.entiy.base.EntityBase)
	 */
	@Override
	public boolean contains(String kind, String name, String type, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges/*, EntityBase entityBase*/) {
		for ( GisMapInitialValue gisMapInitialValue:_gisMapInitialValues) {
			if ( gisMapInitialValue.contains( name, type, headName, ranges, newHeadName, newRanges/*, entityBase*/))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#can_adjust_name(java.lang.String, java.lang.String, java.util.Vector, soars.application.visualshell.object.entiy.base.EntityBase)
	 */
	@Override
	public boolean can_adjust_name(String type, String headName, Vector<String[]> ranges/*, EntityBase entityBase*/) {
		for ( GisMapInitialValue gisMapInitialValue:_gisMapInitialValues) {
			if ( !gisMapInitialValue.can_adjust_name( _name, type, headName, ranges/*, entityBase*/))
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#can_adjust_name(java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector, soars.application.visualshell.object.entiy.base.EntityBase)
	 */
	@Override
	public boolean can_adjust_name(String type, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges/*, EntityBase entityBase*/) {
		for ( GisMapInitialValue gisMapInitialValue:_gisMapInitialValues) {
			if ( !gisMapInitialValue.can_adjust_name( _name, type, headName, ranges, newHeadName, newRanges/*, entityBase*/))
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
		for ( GisMapInitialValue gisMapInitialValue:_gisMapInitialValues) {
			if ( gisMapInitialValue.update_name_and_number( type, newName, originalName, headName, ranges, newHeadName, newRanges))
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
		for ( GisMapInitialValue gisMapInitialValue:_gisMapInitialValues) {
			if ( gisMapInitialValue.update_object_name( type, name, newName))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#contains_this_alias(java.lang.String)
	 */
	@Override
	public boolean contains_this_alias(String alias) {
		for ( GisMapInitialValue gisMapInitialValue:_gisMapInitialValues) {
			if ( gisMapInitialValue.contains_this_alias( alias))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#get_initial_values(java.util.Vector)
	 */
	@Override
	public void get_initial_values1(Vector<String> initialValues) {
		for ( GisMapInitialValue gisMapInitialValue:_gisMapInitialValues)
			gisMapInitialValue.get_initial_values1( initialValues);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#get_initial_values2(java.util.Vector)
	 */
	@Override
	public void get_initial_values2(Vector<String> initialValues) {
		for ( GisMapInitialValue gisMapInitialValue:_gisMapInitialValues)
			gisMapInitialValue.get_initial_values2( initialValues);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#get_initial_data()
	 */
	@Override
	public String get_initial_data() {
		String script = ( ResourceManager.get_instance().get( "initial.data.map") + "\t" + _name);
		for ( GisMapInitialValue gisMapInitialValue:_gisMapInitialValues)
			script += gisMapInitialValue.get_initial_data();
		return ( script + Constant._lineSeparator);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#write(soars.common.utility.xml.sax.Writer, org.xml.sax.helpers.AttributesImpl)
	 */
	@Override
	protected boolean write(Writer writer, AttributesImpl attributesImpl) throws SAXException {
		if ( !write( attributesImpl))
			return false;

		if ( _comment.equals( "") && _gisMapInitialValues.isEmpty())
			writer.writeElement( null, null, SaxLoader._kindTypeMap.get( _kind), attributesImpl);
		else {
			writer.startElement( null, null, SaxLoader._kindTypeMap.get( _kind), attributesImpl);
			for ( GisMapInitialValue gisMapInitialValue:_gisMapInitialValues)
				if ( !gisMapInitialValue.write( writer))
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
//	protected void write(Writer writer, AttributesImpl attributesImpl) throws SAXException {
//		for ( int i = 0; i < _mapInitialValues.size(); ++i)
//			_mapInitialValues.get( i).write( i ,attributesImpl);
//
//		super.write(writer, attributesImpl);
//	}
//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.gis.object.base.GisObjectBase#print(java.util.Vector)
//	 */
//	@Override
//	public void print(Vector<int[]> indices) {
//		String text = "";
//		for ( GisMapInitialValue mapInitialValue:_mapInitialValues)
//			text += ( ( text.equals( "") ? "" : " ") + "[" + mapInitialValue._key[ 0] + "]"+ mapInitialValue._key[ 1] + " - [" + mapInitialValue._value[ 0] + "]" + mapInitialValue._value[ 1]);
//
//		if ( indices.isEmpty()) {
//			System.out.println( "\t" + _name + ", " + text);
//			return;
//		}
//
//		for ( int i = 0; i < indices.size(); ++i) {
//			int[] range = ( int[])indices.get( i);
//			System.out.println( "\t" + _name + ", " + text + ", " + range[ 0] + "-" + range[ 1]);
//		}
//	}
}
