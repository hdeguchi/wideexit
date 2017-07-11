/**
 * 
 */
package soars.application.visualshell.object.gis.object.base;

import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.object.gis.object.keyword.GisKeywordObject;
import soars.application.visualshell.object.gis.object.map.GisMapObject;
import soars.application.visualshell.object.gis.object.number.GisNumberObject;
import soars.application.visualshell.object.gis.object.spot.GisSpotVariableObject;
import soars.application.visualshell.object.gis.object.time.GisTimeVariableObject;
import soars.application.visualshell.object.gis.object.variable.GisVariableObject;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class GisObjectBase {

	/**
	 * This object's kind.
	 */
	public String _kind = "";

	/**
	 * This object's name.
	 */
	public String _name = "";

	/**
	 * Comment for this object.
	 */
	public String _comment = "";

	/**
	 * @param kind
	 */
	public GisObjectBase(String kind) {
		super();
		_kind = kind;
	}

	/**
	 * @param kind
	 * @param name
	 */
	public GisObjectBase(String kind, String name) {
		super();
		_kind = kind;
		_name = name;
	}

	/**
	 * @param kind
	 * @param name
	 * @param comment
	 */
	public GisObjectBase(String kind, String name, String comment) {
		super();
		_kind = kind;
		_name = name;
		_comment = comment;
	}

	/**
	 * @param gisObjectBase
	 */
	public GisObjectBase(GisObjectBase gisObjectBase) {
		super();
		_kind = gisObjectBase._kind;
		_name = gisObjectBase._name;
		_comment = gisObjectBase._comment;
	}

	/**
	 * @param gisObjectBase
	 */
	public void copy(GisObjectBase gisObjectBase) {
		_kind = gisObjectBase._kind;
		_name = gisObjectBase._name;
		_comment = gisObjectBase._comment;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if ( !( obj instanceof GisObjectBase))
			return false;

		GisObjectBase gisObjectBase = ( GisObjectBase)obj;
		return ( _kind.equals( gisObjectBase._kind)
			&& _name.equals( gisObjectBase._name)
			&& _comment.equals( gisObjectBase._comment));
	}

	/**
	 * @param gisObjectBase
	 * @return
	 */
	public static GisObjectBase create( GisObjectBase gisObjectBase) {
//		if ( gisObjectBase instanceof GisProbabilityObject)
//			return new GisProbabilityObject( ( GisProbabilityObject)gisObjectBase);
		if ( gisObjectBase instanceof GisKeywordObject)
			return new GisKeywordObject( ( GisKeywordObject)gisObjectBase);
		else if ( gisObjectBase instanceof GisNumberObject)
			return new GisNumberObject( ( GisNumberObject)gisObjectBase);
//		else if ( gisObjectBase instanceof GisRoleVariableObject)
//			return new GisRoleVariableObject( ( GisRoleVariableObject)gisObjectBase);
		else if ( gisObjectBase instanceof GisTimeVariableObject)
			return new GisTimeVariableObject( ( GisTimeVariableObject)gisObjectBase);
		else if ( gisObjectBase instanceof GisSpotVariableObject)
			return new GisSpotVariableObject( ( GisSpotVariableObject)gisObjectBase);
		else if ( gisObjectBase instanceof GisVariableObject)
			return new GisVariableObject( ( GisVariableObject)gisObjectBase);
		else if ( gisObjectBase instanceof GisMapObject)
			return new GisMapObject( ( GisMapObject)gisObjectBase);
//		else if ( gisObjectBase instanceof GisExchangeAlgebraObject)
//			return new GisExchangeAlgebraObject( ( GisExchangeAlgebraObject)gisObjectBase);
//		else if ( gisObjectBase instanceof GisClassVariableObject)
//			return new GisClassVariableObject( ( GisClassVariableObject)gisObjectBase);
//		else if ( gisObjectBase instanceof GisFileObject)
//			return new GisFileObject( ( GisFileObject)gisObjectBase);
//		else if ( gisObjectBase instanceof GisInitialDataFileObject)
//			return new GisInitialDataFileObject( ( GisInitialDataFileObject)gisObjectBase);
//		else if ( gisObjectBase instanceof GisExTransferObject)
//			return new GisExTransferObject( ( GisExTransferObject)gisObjectBase);
		else
			return null;
	}

	/**
	 * @param kind
	 * @return
	 */
	public static GisObjectBase create(String kind) {
//		if ( kind.equals( "probability"))
//			return new GisProbabilityObject();
		if ( kind.equals( "keyword"))
			return new GisKeywordObject();
		else if ( kind.equals( "number object"))
			return new GisNumberObject();
//		else if ( kind.equals( "role variable"))
//			return new GisRoleVariableObject();
		else if ( kind.equals( "time variable"))
			return new GisTimeVariableObject();
		else if ( kind.equals( "spot variable"))
			return new GisSpotVariableObject();
		else if ( kind.equals( "collection") || kind.equals( "list"))
			return new GisVariableObject( kind);
		else if ( kind.equals( "map"))
			return new GisMapObject();
//		else if (kind.equals( "exchange algebra"))
//			return new GisExchangeAlgebraObject();
//		else if ( kind.equals( "class variable"))
//			return new GisClassVariableObject();
//		else if ( kind.equals( "file"))
//			return new GisFileObject();
//		else if ( kind.equals( "initial data file"))
//			return new GisInitialDataFileObject();
//		else if ( kind.equals( "extransfer"))
//			return new GisExTransferObject();
		else
			return null;
	}

	/**
	 * @param kind
	 * @param gisObjectBase
	 * @return
	 */
	public static boolean is_target(String kind, GisObjectBase gisObjectBase) {
		return ( ( kind.equals( "keyword") && gisObjectBase instanceof GisKeywordObject)
			|| ( kind.equals( "number object") && gisObjectBase instanceof GisNumberObject)
			|| ( kind.equals( "time variable") && gisObjectBase instanceof GisTimeVariableObject)
			|| ( kind.equals( "spot variable") && gisObjectBase instanceof GisSpotVariableObject)
			|| ( kind.equals( "collection") && gisObjectBase instanceof GisVariableObject && ( ( GisVariableObject)gisObjectBase)._kind.equals( "collection"))
			|| ( kind.equals( "list") && gisObjectBase instanceof GisVariableObject && ( ( GisVariableObject)gisObjectBase)._kind.equals( "list"))
			|| ( kind.equals( "map") && gisObjectBase instanceof GisMapObject));
//		return ( ( kind.equals( "probability") && objectBase instanceof GisProbabilityObject)
//			|| ( kind.equals( "keyword") && objectBase instanceof GisKeywordObject)
//			|| ( kind.equals( "number object") && objectBase instanceof GisNumberObject)
//			|| ( kind.equals( "role variable") && objectBase instanceof GisRoleVariableObject)
//			|| ( kind.equals( "time variable") && objectBase instanceof GisTimeVariableObject)
//			|| ( kind.equals( "spot variable") && objectBase instanceof GisSpotVariableObject)
//			|| ( kind.equals( "collection") && objectBase instanceof GisVariableObject && ( ( GisVariableObject)objectBase)._kind.equals( "collection"))
//			|| ( kind.equals( "list") && objectBase instanceof GisVariableObject && ( ( GisVariableObject)objectBase)._kind.equals( "list"))
//			|| ( kind.equals( "map") && objectBase instanceof GisMapObject)
//			|| ( kind.equals( "exchange algebra") && objectBase instanceof GisExchangeAlgebraObject)
//			|| ( kind.equals( "class variable") && objectBase instanceof GisClassVariableObject)
//			|| ( kind.equals( "file") && objectBase instanceof GisFileObject)
//			|| ( kind.equals( "initial data file") && objectBase instanceof GisInitialDataFileObject)
//			|| ( kind.equals( "extransfer") && objectBase instanceof GisExTransferObject));
	}

//	/**
//	 * Returns the integer array to which this object is mapped in the integer hashtable.
//	 * @param indicesMap the integer hashtable
//	 * @return the integer array to which this object is mapped in the integer hashtable
//	 */
//	public Vector<int[]> get_indices(HashMap<GisObjectBase, Vector<int[]>> indicesMap) {
//		Iterator iterator = indicesMap.entrySet().iterator();
//		while ( iterator.hasNext()) {
//			Object object = iterator.next();
//			Map.Entry entry = ( Map.Entry)object;
//			GisObjectBase gisObjectBase = ( GisObjectBase)entry.getKey();
//			if ( equals( gisObjectBase))
//				return ( Vector<int[]>)entry.getValue();
//		}
//		return null;
//	}
//
//	/**
//	 * Update the comment with the specified one.
//	 * @param indicesMap the integer hashtable
//	 * @param comment the specified comment
//	 */
//	public void update_comment(HashMap indicesMap, String comment) {
//		Iterator iterator = indicesMap.entrySet().iterator();
//		while ( iterator.hasNext()) {
//			Object object = iterator.next();
//			Map.Entry entry = ( Map.Entry)object;
//			GisObjectBase gisObjectBase = ( GisObjectBase)entry.getKey();
//			if ( equals( gisObjectBase)) {
//				gisObjectBase._comment = comment;
//				break;
//			}
//		}
//	}

	/**
	 * @param gisObjectBase
	 * @return
	 */
	public boolean contains(GisObjectBase gisObjectBase) {
		return false;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(Writer writer) throws SAXException {
		return write( writer, new AttributesImpl());
	}

	/**
	 * @param writer
	 * @param attributesImpl
	 * @return
	 * @throws SAXException 
	 */
	protected boolean write(Writer writer, AttributesImpl attributesImpl) throws SAXException {
		return true;
	}

	/**
	 * @param attributesImpl
	 * @return
	 */
	protected boolean write(AttributesImpl attributesImpl) {
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
		return true;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException 
	 */
	protected boolean write_commment(Writer writer) throws SAXException {
		if ( _comment.equals( ""))
			return true;

		writer.startElement( null, null, "comment", new AttributesImpl());
		writer.characters( _comment.toCharArray(), 0, _comment.length());
		writer.endElement( null, null, "comment");
		return true;
	}

//	/**
//	 * @param writer
//	 * @param attributesImpl
//	 * @throws SAXException
//	 */
//	protected void write(Writer writer, AttributesImpl attributesImpl) throws SAXException {
//		if ( _comment.equals( "")) {
//			if ( is_fields_empty())
//				writer.writeElement( null, null, SaxLoader._kindTypeMap.get( _kind), attributesImpl);
//			else {
//				writer.startElement( null, null, SaxLoader._kindTypeMap.get( _kind), attributesImpl);
//				writer.characters( _comment.toCharArray(), 0, _comment.length());
//				writer.endElement( null, null, SaxLoader._kindTypeMap.get( _kind));
//			}
//		} else {
//			writer.startElement( null, null, SaxLoader._kindTypeMap.get( _kind), attributesImpl);
//			writer.characters( _comment.toCharArray(), 0, _comment.length());
//			writer.endElement( null, null, SaxLoader._kindTypeMap.get( _kind));
//		}
//	}

	/**
	 * @param kind
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param name
	 * @param entityBase
	 * @return
	 */
	public boolean contains(String kind, String type, String headName, Vector<String[]> ranges, String name/*, EntityBase entityBase*/) {
		return false;
	}

	/**
	 * @param kind
	 * @param name
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @param entityBase
	 * @return
	 */
	public boolean contains(String kind, String name, String type, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges/*, EntityBase entityBase*/) {
		return false;
	}

	/**
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param entityBase
	 * @return
	 */
	public boolean can_adjust_name(String type, String headName, Vector<String[]> ranges/*, EntityBase entityBase*/) {
		return true;
	}

	/**
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @param entityBase
	 * @return
	 */
	public boolean can_adjust_name(String type, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges/*, EntityBase entityBase*/) {
		return true;
	}

	/**
	 * @param type
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	public boolean update_name_and_number(String type, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		return false;
	}

	/**
	 * @param type
	 * @param name
	 * @param newName
	 * @return
	 */
	public boolean update_object_name(String type, String name, String newName) {
		return false;
	}

	/**
	 * @param alias
	 * @return
	 */
	public boolean contains_this_alias(String alias) {
		return false;
	}

	/**
	 * @param initialValues
	 */
	public void get_initial_values1(Vector<String> initialValues) {
	}

	/**
	 * @param initialValues
	 */
	public void get_initial_values2(Vector<String> initialValues) {
	}

//	/**
//	 * Returns true if the specified object can be paste.
//	 * @param entityBase the specified object
//	 * @param drawObjects the specified objects array
//	 * @return true if this objecct can be paste
//	 */
//	public boolean can_paste(EntityBase entityBase, Layer drawObjects) {
//		return true;
//	}
//
//	/**
//	 * @param originalName
//	 * @param name
//	 * @return
//	 */
//	public boolean update_role_name(String originalName, String name) {
//		return false;
//	}
//
//	/**
//	 * @param roleNames
//	 */
//	public void on_remove_role_name(Vector<String> roleNames) {
//	}

	/**
	 * @return
	 */
	public String get_initial_data() {
		return "";
	}

//	/**
//	 * @param prefix
//	 * @param counter
//	 * @param initialValueMap
//	 * @return
//	 */
//	public String get_script(String prefix, IntBuffer counter, InitialValueMap initialValueMap) {
//		return "";
//	}
//
//	/**
//	 * @param prefix
//	 * @param initialValueMap
//	 * @return
//	 */
//	public String get_initial_values_script(String prefix, InitialValueMap initialValueMap) {
//		return "";
//	}
//
//	/**
//	 * 
//	 */
//	public void print() {
//	}
//
//	/**
//	 * @param indices
//	 */
//	public void print(Vector<int[]> indices) {
//	}

//	/**
//	 * @param writer
//	 */
//	public void write(Writer writer) throws SAXException {
//	}

//	/**
//	 * @param writer
//	 * @param number
//	 */
//	public void write(Writer writer, String number) throws SAXException {
//	}

//	/**
//	 * @param writer
//	 * @param attributesImpl
//	 * @throws SAXException
//	 */
//	protected void write(Writer writer, AttributesImpl attributesImpl) throws SAXException {
//		if ( _comment.equals( ""))
//			writer.writeElement( null, null, SaxLoader._kindTypeMap.get( _kind), attributesImpl);
//		else {
//			writer.startElement( null, null, SaxLoader._kindTypeMap.get( _kind), attributesImpl);
//			writer.characters( _comment.toCharArray(), 0, _comment.length());
//			writer.endElement( null, null, SaxLoader._kindTypeMap.get( _kind));
//		}
//	}

//	/**
//	 * @param agentObjectMap
//	 * @param spotObjectMap
//	 * @param entityData
//	 * @return
//	 */
//	public boolean verify(EntityDataMap agentObjectMap, EntityDataMap spotObjectMap, EntityData entityData) {
//		return true;
//	}
//
//	/**
//	 * @param agentDataMap
//	 * @param spotDataMap
//	 * @param indices
//	 * @param entityData
//	 * @return
//	 */
//	public boolean verify(EntityDataMap agentDataMap, EntityDataMap spotDataMap, Vector<int[]> indices, EntityData entityData) {
//		return true;
//	}
}
