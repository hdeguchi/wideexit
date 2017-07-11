/**
 * 
 */
package soars.application.visualshell.object.entity.base.object.base;

import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.file.importer.initial.entity.EntityData;
import soars.application.visualshell.file.importer.initial.entity.EntityDataMap;
import soars.application.visualshell.file.loader.SaxLoader;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.object.class_variable.ClassVariableObject;
import soars.application.visualshell.object.entity.base.object.exchange_algebra.ExchangeAlgebraObject;
import soars.application.visualshell.object.entity.base.object.extransfer.ExTransferObject;
import soars.application.visualshell.object.entity.base.object.file.FileObject;
import soars.application.visualshell.object.entity.base.object.initial_data_file.InitialDataFileObject;
import soars.application.visualshell.object.entity.base.object.keyword.KeywordObject;
import soars.application.visualshell.object.entity.base.object.map.MapObject;
import soars.application.visualshell.object.entity.base.object.number.NumberObject;
import soars.application.visualshell.object.entity.base.object.probability.ProbabilityObject;
import soars.application.visualshell.object.entity.base.object.role.RoleVariableObject;
import soars.application.visualshell.object.entity.base.object.spot.SpotVariableObject;
import soars.application.visualshell.object.entity.base.object.time.TimeVariableObject;
import soars.application.visualshell.object.entity.base.object.variable.VariableObject;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class ObjectBase {

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
	public ObjectBase(String kind) {
		super();
		_kind = kind;
	}

	/**
	 * @param kind
	 * @param name
	 */
	public ObjectBase(String kind, String name) {
		super();
		_kind = kind;
		_name = name;
	}

	/**
	 * @param kind
	 * @param name
	 * @param comment
	 */
	public ObjectBase(String kind, String name, String comment) {
		super();
		_kind = kind;
		_name = name;
		_comment = comment;
	}

	/**
	 * @param objectBase
	 */
	public ObjectBase(ObjectBase objectBase) {
		super();
		_kind = objectBase._kind;
		_name = objectBase._name;
		_comment = objectBase._comment;
	}

	/**
	 * @param objectBase
	 */
	public void copy(ObjectBase objectBase) {
		_kind = objectBase._kind;
		_name = objectBase._name;
		_comment = objectBase._comment;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if ( !( obj instanceof ObjectBase))
			return false;

		ObjectBase objectBase = ( ObjectBase)obj;
		return ( _kind.equals( objectBase._kind)
			&& _name.equals( objectBase._name)
			&& _comment.equals( objectBase._comment));
	}

	/**
	 * @param objectBase
	 * @return
	 */
	public static ObjectBase create( ObjectBase objectBase) {
		if ( objectBase instanceof ProbabilityObject)
			return new ProbabilityObject( ( ProbabilityObject)objectBase);
		else if ( objectBase instanceof KeywordObject)
			return new KeywordObject( ( KeywordObject)objectBase);
		else if ( objectBase instanceof NumberObject)
			return new NumberObject( ( NumberObject)objectBase);
		else if ( objectBase instanceof RoleVariableObject)
			return new RoleVariableObject( ( RoleVariableObject)objectBase);
		else if ( objectBase instanceof TimeVariableObject)
			return new TimeVariableObject( ( TimeVariableObject)objectBase);
		else if ( objectBase instanceof SpotVariableObject)
			return new SpotVariableObject( ( SpotVariableObject)objectBase);
		else if ( objectBase instanceof VariableObject)
			return new VariableObject( ( VariableObject)objectBase);
		else if ( objectBase instanceof MapObject)
			return new MapObject( ( MapObject)objectBase);
		else if ( objectBase instanceof ExchangeAlgebraObject)
			return new ExchangeAlgebraObject( ( ExchangeAlgebraObject)objectBase);
		else if ( objectBase instanceof ClassVariableObject)
			return new ClassVariableObject( ( ClassVariableObject)objectBase);
		else if ( objectBase instanceof FileObject)
			return new FileObject( ( FileObject)objectBase);
		else if ( objectBase instanceof InitialDataFileObject)
			return new InitialDataFileObject( ( InitialDataFileObject)objectBase);
		else if ( objectBase instanceof ExTransferObject)
			return new ExTransferObject( ( ExTransferObject)objectBase);
		else
			return null;
	}

	/**
	 * @param kind
	 * @return
	 */
	public static ObjectBase create(String kind) {
		if ( kind.equals( "probability"))
			return new ProbabilityObject();
		else if ( kind.equals( "keyword"))
			return new KeywordObject();
		else if ( kind.equals( "number object"))
			return new NumberObject();
		else if ( kind.equals( "role variable"))
			return new RoleVariableObject();
		else if ( kind.equals( "time variable"))
			return new TimeVariableObject();
		else if ( kind.equals( "spot variable"))
			return new SpotVariableObject();
		else if ( kind.equals( "collection") || kind.equals( "list"))
			return new VariableObject( kind);
		else if ( kind.equals( "map"))
			return new MapObject();
		else if (kind.equals( "exchange algebra"))
			return new ExchangeAlgebraObject();
		else if ( kind.equals( "class variable"))
			return new ClassVariableObject();
		else if ( kind.equals( "file"))
			return new FileObject();
		else if ( kind.equals( "initial data file"))
			return new InitialDataFileObject();
		else if ( kind.equals( "extransfer"))
			return new ExTransferObject();
		else
			return null;
	}

	/**
	 * @param kind
	 * @param objectBase
	 * @return
	 */
	public static boolean is_target(String kind, ObjectBase objectBase) {
		return ( ( kind.equals( "probability") && objectBase instanceof ProbabilityObject)
			|| ( kind.equals( "keyword") && objectBase instanceof KeywordObject)
			|| ( kind.equals( "number object") && objectBase instanceof NumberObject)
			|| ( kind.equals( "role variable") && objectBase instanceof RoleVariableObject)
			|| ( kind.equals( "time variable") && objectBase instanceof TimeVariableObject)
			|| ( kind.equals( "spot variable") && objectBase instanceof SpotVariableObject)
			|| ( kind.equals( "collection") && objectBase instanceof VariableObject && ( ( VariableObject)objectBase)._kind.equals( "collection"))
			|| ( kind.equals( "list") && objectBase instanceof VariableObject && ( ( VariableObject)objectBase)._kind.equals( "list"))
			|| ( kind.equals( "map") && objectBase instanceof MapObject)
			|| ( kind.equals( "exchange algebra") && objectBase instanceof ExchangeAlgebraObject)
			|| ( kind.equals( "class variable") && objectBase instanceof ClassVariableObject)
			|| ( kind.equals( "file") && objectBase instanceof FileObject)
			|| ( kind.equals( "initial data file") && objectBase instanceof InitialDataFileObject)
			|| ( kind.equals( "extransfer") && objectBase instanceof ExTransferObject));
	}
	/**
	 * Returns the integer array to which this object is mapped in the integer hashtable.
	 * @param indicesMap the integer hashtable
	 * @return the integer array to which this object is mapped in the integer hashtable
	 */
	public Vector<int[]> get_indices(Map<ObjectBase, Vector<int[]>> indicesMap) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			if ( equals( objectBase))
				return ( Vector<int[]>)entry.getValue();
		}
		return null;
	}

	/**
	 * Update the comment with the specified one.
	 * @param indicesMap the integer hashtable
	 * @param comment the specified comment
	 */
	public void update_comment(Map<ObjectBase, Vector<int[]>> indicesMap, String comment) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			if ( equals( objectBase)) {
				objectBase._comment = comment;
				break;
			}
		}
	}

	/**
	 * @param objectBase
	 * @return
	 */
	public boolean contains(ObjectBase objectBase) {
		return false;
	}

	/**
	 * @param kind
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param name
	 * @param entityBase
	 * @return
	 */
	public boolean contains(String kind, String type, String headName, Vector<String[]> ranges, String name, EntityBase entityBase) {
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
	public boolean contains(String kind, String name, String type, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges, EntityBase entityBase) {
		return false;
	}

	/**
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param entityBase
	 * @return
	 */
	public boolean can_adjust_name(String type, String headName, Vector<String[]> ranges, EntityBase entityBase) {
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
	public boolean can_adjust_name(String type, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges, EntityBase entityBase) {
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

	/**
	 * Returns true if the specified object can be paste.
	 * @param entityBase the specified object
	 * @param drawObjects the specified objects array
	 * @return true if this objecct can be paste
	 */
	public boolean can_paste(EntityBase entityBase, Layer drawObjects) {
		return true;
	}

	/**
	 * @param originalName
	 * @param name
	 * @return
	 */
	public boolean update_role_name(String originalName, String name) {
		return false;
	}

	/**
	 * @param roleNames
	 */
	public void on_remove_role_name(Vector<String> roleNames) {
	}

	/**
	 * @return
	 */
	public String get_initial_data() {
		return "";
	}

	/**
	 * @param prefix
	 * @param counter
	 * @param initialValueMap
	 * @return
	 */
	public String get_script(String prefix, IntBuffer counter, InitialValueMap initialValueMap) {
		return "";
	}

	/**
	 * @param prefix
	 * @param initialValueMap
	 * @return
	 */
	public String get_initial_values_script(String prefix, InitialValueMap initialValueMap) {
		return "";
	}

	/**
	 * 
	 */
	public void print() {
	}

	/**
	 * @param indices
	 */
	public void print(Vector<int[]> indices) {
	}

	/**
	 * @param writer
	 */
	public void write(Writer writer) throws SAXException {
	}

	/**
	 * @param writer
	 * @param number
	 */
	public void write(Writer writer, String number) throws SAXException {
	}

	/**
	 * @param writer
	 * @param attributesImpl
	 * @throws SAXException
	 */
	protected void write(Writer writer, AttributesImpl attributesImpl) throws SAXException {
		if ( _comment.equals( ""))
			writer.writeElement( null, null, SaxLoader._kindTypeMap.get( _kind), attributesImpl);
		else {
			writer.startElement( null, null, SaxLoader._kindTypeMap.get( _kind), attributesImpl);
			writer.characters( _comment.toCharArray(), 0, _comment.length());
			writer.endElement( null, null, SaxLoader._kindTypeMap.get( _kind));
		}
	}

	/**
	 * @param agentObjectMap
	 * @param spotObjectMap
	 * @param entityData
	 * @return
	 */
	public boolean verify(EntityDataMap agentObjectMap, EntityDataMap spotObjectMap, EntityData entityData) {
		return true;
	}

	/**
	 * @param agentDataMap
	 * @param spotDataMap
	 * @param indices
	 * @param entityData
	 * @return
	 */
	public boolean verify(EntityDataMap agentDataMap, EntityDataMap spotDataMap, Vector<int[]> indices, EntityData entityData) {
		return true;
	}
}
