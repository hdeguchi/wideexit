/*
 * 2005/05/13
 */
package soars.application.visualshell.object.entity.base;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JComponent;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.common.image.VisualShellImageManager;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.file.importer.initial.entity.EntityData;
import soars.application.visualshell.file.loader.SaxLoader;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.base.DrawObject;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.edit.EditObjectDlg;
import soars.application.visualshell.object.entity.base.object.base.ObjectBase;
import soars.application.visualshell.object.entity.base.object.class_variable.ClassVariableObject;
import soars.application.visualshell.object.entity.base.object.exchange_algebra.ExchangeAlgebraObject;
import soars.application.visualshell.object.entity.base.object.extransfer.ExTransferObject;
import soars.application.visualshell.object.entity.base.object.file.FileObject;
import soars.application.visualshell.object.entity.base.object.initial_data_file.InitialDataFileChecker;
import soars.application.visualshell.object.entity.base.object.initial_data_file.InitialDataFileObject;
import soars.application.visualshell.object.entity.base.object.map.MapObject;
import soars.application.visualshell.object.entity.base.object.number.NumberObject;
import soars.application.visualshell.object.entity.base.object.role.RoleVariableObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.application.visualshell.object.role.base.Role;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.image.ImageProperty;
import soars.common.utility.swing.image.ImagePropertyManager;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.xml.sax.Writer;

/**
 * The basic class for agent and spot object.
 * @author kurata / SOARS project
 */
public class EntityBase extends DrawObject {

	/**
	 * The number of this object.
	 */
	public String _number = "";

	/**
	 * Initial role name.
	 */
	public String _initialRole = "";

	/**
	 * Any script.
	 */
	public String _others = "";

	/**
	 * Object map hashtable.
	 */
	public Map<String, TreeMap<String, Object>> _objectMapMap = new HashMap<String, TreeMap<String, Object>>();

	/**
	 * Initial data file list.
	 */
	public Map<String, List<ObjectBase>> _objectListMap = new HashMap<String, List<ObjectBase>>();

	/**
	 * True if this object has multiple initial data.
	 */
	public boolean _multi = false;

	/**
	 * GIS's ID.
	 */
	public String _gis = "";

	/**
	 * GIS's coordinates.
	 */
	public String[] _gisCoordinates = new String[] { "", ""};

	/**
	 * Creates this object with the specified data.
	 * @param id the specified id
	 * @param name the specified name
	 * @param position the specified position
	 * @param graphics2D the graphics object of JAVA
	 */
	public EntityBase(int id, String name, Point position, Graphics2D graphics2D) {
		super(id, name, position, graphics2D);
		initialize();
	}

	/**
	 * @param global
	 * @param id
	 * @param name
	 * @param position
	 * @param initialRole
	 * @param graphics2D
	 */
	public EntityBase(boolean global, int id, String name, Point position, String initialRole, Graphics2D graphics2D) {
		super(global, id, name, position, graphics2D);
		_initialRole = initialRole;
		initialize();
	}

	/**
	 * Creates this object with the specified data.
	 * @param global
	 * @param id the specified id
	 * @param name the specified name
	 * @param position the specified position
	 * @param number the number of this object
	 * @param initialRole the initial role name
	 * @param imageFilename the image file name
	 * @param gis
	 * @param gisCoordinates
	 * @param graphics2D the graphics object of JAVA
	 */
	public EntityBase(boolean global, int id, String name, Point position, String number, String initialRole, String imageFilename, String gis, String[] gisCoordinates, Graphics2D graphics2D) {
		super(global, id, name, position, imageFilename, graphics2D);
		_number = number;
		_initialRole = initialRole;
		_gis = gis;
		_gisCoordinates = gisCoordinates;
		initialize();
	}

	/**
	 * Creates this object with the specified data.
	 * @param entityBase the specified data
	 */
	public EntityBase(EntityBase entityBase) {
		super(entityBase);
		initialize();
		copy( entityBase);
	}

	/**
	 * for GIS only!
	 */
	public EntityBase() {
		super();
	}

	/**
	 * Creates this object with the specified data.
	 * @param name the specified name
	 */
	public EntityBase(String name) {
		super(name);
		initialize();
	}

	/**
	 * Creates this object with the specified data.
	 * @param id the specified id
	 * @param name the specified name
	 */
	public EntityBase(int id, String name) {
		super(id, name);
		initialize();
	}

	/**
	 * 
	 */
	private void initialize() {
		for ( String kind:Constant._kinds)
			_objectMapMap.put( kind, new TreeMap<String, Object>());
		for ( String kind:Constant._exceptionalKinds)
			_objectListMap.put( kind, new ArrayList<ObjectBase>());
	}

	/**
	 * @param entityBase
	 */
	private void copy(EntityBase entityBase) {
		_multi = entityBase._multi;
		_number = entityBase._number;
		_initialRole = entityBase._initialRole;
		_others = entityBase._others;
		_gis = entityBase._gis;
		_gisCoordinates[ 0] = entityBase._gisCoordinates[ 0];
		_gisCoordinates[ 1] = entityBase._gisCoordinates[ 1];
		for ( String kind:Constant._kinds)
			copy( _objectMapMap.get( kind), entityBase._objectMapMap.get( kind));
		for ( String kind:Constant._exceptionalKinds)
			copy( _objectListMap.get( kind), entityBase._objectListMap.get( kind));
	}

	/**
	 * @param destinationMap
	 * @param sourceMap
	 */
	private void copy(TreeMap<String, Object> destinationMap, TreeMap<String, Object> sourceMap) {
		Iterator iterator = sourceMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			if ( !is_multi())
				destinationMap.put( name, ObjectBase.create( ( ObjectBase)entry.getValue()));
			else {
				Map<ObjectBase, Vector<int[]>> srcIndicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();

				Map<ObjectBase, Vector<int[]>> dstIndicesMap = new HashMap<ObjectBase, Vector<int[]>>();
				destinationMap.put( name, dstIndicesMap);

				copy( dstIndicesMap, srcIndicesMap);
			}
		}
	}

	/**
	 * @param dstIndicesMap
	 * @param srcIndicesMap
	 */
	private void copy(Map<ObjectBase, Vector<int[]>> dstIndicesMap, Map<ObjectBase, Vector<int[]>> srcIndicesMap) {
		Iterator iterator = srcIndicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			Vector<int[]> srcIndices = ( Vector<int[]>)entry.getValue();

			Vector<int[]> dstIndices = new Vector<int[]>();
			dstIndicesMap.put( ObjectBase.create( objectBase), dstIndices);

			for ( int i = 0; i < srcIndices.size(); ++i) {
				int[] range = srcIndices.get( i);
				dstIndices.add( new int[] { range[ 0], range[ 1]});
			}
		}
	}

	/**
	 * @param dstList
	 * @param srcList
	 */
	private void copy(List<ObjectBase> dstList, List<ObjectBase> srcList) {
		if ( is_multi())
			return;

		for ( ObjectBase objectBase:srcList)
			dstList.add( ObjectBase.create( objectBase));
	}

	/**
	 * Returns true if the number variable is correct.
	 * @param numberObjectName the number variable name
	 * @param numberObjectType the number variable type
	 * @return true if the number variable is correct
	 */
	public boolean is_number_object_correct(String numberObjectName, String numberObjectType) {
		if ( !is_multi()) {
			NumberObject numberObject = ( NumberObject)_objectMapMap.get( "number object").get( numberObjectName);
			if ( null == numberObject)
				return false;

			if ( !numberObject._type.equals( numberObjectType))
				return false;
		} else {
			Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)_objectMapMap.get( "number object").get( numberObjectName);
			if ( null == indicesMap)
				return false;

			Vector<ObjectBase> keys = new Vector<ObjectBase>( indicesMap.keySet());
			if ( keys.isEmpty())
				return false;

			NumberObject numberObject = ( NumberObject)keys.get( 0);
			if ( !numberObject._type.equals( numberObjectType))
				return false;
		}
		return true;
	}

	/**
	 * Returns true if the number variable is correct.
	 * @param type the object type("agent" or "spot")
	 * @param numberObjectName the number variable name
	 * @param numberObjectType the number variable type
	 * @return true if the number variable is correct
	 */
	public boolean is_number_object_correct(String type, String numberObjectName, String numberObjectType) {
		return is_number_object_correct( type, _name, numberObjectName, numberObjectType, _objectMapMap.get( "number object"), is_multi());
	}

	/**
	 * Returns true if the number variable is correct.
	 * @param type the object type("agent" or "spot")
	 * @param name the object name
	 * @param numberObjectName the number variable name
	 * @param numberObjectType the number variable type
	 * @param treeMap the number variable hashtable
	 * @param multi true if the object has multi initial data
	 * @return true if the number variable is correct
	 */
	public static boolean is_number_object_correct(String type, String name, String numberObjectName, String numberObjectType, TreeMap<String, Object> treeMap, boolean multi) {
		if ( !multi) {
			NumberObject numberObject = ( NumberObject)treeMap.get( numberObjectName);
			if ( null == numberObject)
				return true;

			if ( numberObject._type.equals( numberObjectType))
				return true;

			String[] message = new String[] {
				( type.equals( "agent") ? "Agent" : "Spot"),
				"name = " + name,
				"number object = " + numberObjectName,
				"type = " + numberObject._type
			};

			WarningManager.get_instance().add( message);

			return false;
		} else {
			Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)treeMap.get( numberObjectName);
			if ( null == indicesMap)
				return true;

			Vector<ObjectBase> keys = new Vector<ObjectBase>( indicesMap.keySet());
			if ( keys.isEmpty())
				return true;

			NumberObject numberObject = ( NumberObject)keys.get( 0);
			if ( numberObject._type.equals( numberObjectType))
				return true;

			String[] message = new String[] {
				( type.equals( "agent") ? "Agent" : "Spot"),
				"name = " + name,
				"number object = " + numberObjectName,
				"type = " + ( numberObjectType.equals( "integer") ? "real number" : "integer")
			};

			WarningManager.get_instance().add( message);

			return false;
		}
	}

	/**
	 * Returns true for updating the number variable type successfully.
	 * @param type the object type("agent" or "spot")
	 * @param numberObjectName the number variable name
	 * @param numberObjectType the number variable type
	 * @return true for updating the number variable type successfully
	 */
	public boolean update_number_object_type(String type, String numberObjectName, String numberObjectType) {
		return update_number_object_type( type, _name, numberObjectName, numberObjectType, _objectMapMap.get( "number object"), is_multi());
	}

	/**
	 * Returns true for updating the number variable type successfully.
	 * @param type the object type("agent" or "spot")
	 * @param name the object name
	 * @param numberObjectName the number variable name
	 * @param numberObjectType the number variable type
	 * @param treeMap the number variable hashtable
	 * @param multi true if the object has multi initial data
	 * @return true for updating the number variable type successfully
	 */
	public static boolean update_number_object_type(String type, String name, String numberObjectName, String numberObjectType, TreeMap<String, Object> treeMap, boolean multi) {
		if ( !multi) {
			NumberObject numberObject = ( NumberObject)treeMap.get( numberObjectName);
			if ( null == numberObject)
				return false;

			if ( numberObject._type.equals( numberObjectType))
				return false;

			numberObject._type = numberObjectType;
			return true;
		} else {
			Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)treeMap.get( numberObjectName);
			if ( null == indicesMap)
				return false;

			boolean result = false;
			Iterator iterator = indicesMap.entrySet().iterator();
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				NumberObject numberObject = ( NumberObject)entry.getKey();

				if ( numberObject._type.equals( numberObjectType))
					continue;

				numberObject._type = numberObjectType;
				result = true;
			}

			return result;
		}
	}

	/**
	 * Returns the specified number variable's type. 
	 * @param numberObjectName the name of the specified number variable
	 * @return the specified number variable's type
	 */
	public String get_number_object_type(String numberObjectName) {
		return get_number_object_type( numberObjectName, _objectMapMap.get( "number object"), is_multi());
	}

	/**
	 * @param numberObjectName
	 * @param treeMap
	 * @param multi
	 * @return
	 */
	private String get_number_object_type(String numberObjectName, TreeMap<String, Object> treeMap, boolean multi) {
		if ( !multi) {
			NumberObject numberObject = ( NumberObject)treeMap.get( numberObjectName);
			if ( null == numberObject)
				return null;

			return numberObject._type;
		} else {
			Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)treeMap.get( numberObjectName);
			if ( null == indicesMap)
				return null;

			Vector<ObjectBase> keys = new Vector<ObjectBase>( indicesMap.keySet());
			if ( keys.isEmpty())
				return null;

			NumberObject numberObject = ( NumberObject)keys.get( 0);
			if ( numberObject._type.equals( "integer"))
				return "integer";
			else if ( numberObject._type.equals( "real number"))
				return "real number";
			else
				return null;
		}
	}

	/**
	 * Appends the ClassVariable objects to the specified hashtable.
	 * @param classVariableMap the specified hashtable
	 */
	public void get_class_variable_map(Map<String, ClassVariableObject> classVariableMap) {
		Iterator iterator = _objectMapMap.get( "class variable").entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			if ( classVariableMap.containsKey( name))
				continue;

			if ( !is_multi()) {
				ClassVariableObject classVariableObject = ( ClassVariableObject)entry.getValue();
				classVariableMap.put( name, classVariableObject);
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				Vector<ObjectBase> keys = new Vector<ObjectBase>( indicesMap.keySet());
				if ( keys.isEmpty())
					continue;

				ClassVariableObject classVariableObject = ( ClassVariableObject)keys.get( 0);
				classVariableMap.put( name, new ClassVariableObject( classVariableObject));
			}
		}
	}

	/**
	 * Returns the ClassVariable object which has the specified name.
	 * @param name the specified name
	 * @return the ClassVariable object which has the specified name
	 */
	public ClassVariableObject get_class_variable(String name) {
		if ( !is_multi())
			return ( ClassVariableObject)_objectMapMap.get( "class variable").get( name);
		else {
			Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)_objectMapMap.get( "class variable").get( name);
			if ( null == indicesMap)
				return null;

			Vector<ObjectBase> keys = new Vector<ObjectBase>( indicesMap.keySet());
			if ( keys.isEmpty())
				return null;

			return new ClassVariableObject( ( ClassVariableObject)keys.get( 0));
		}
	}

	/**
	 * Returns true for updating this object with the specified data.
	 * @param entityData the specified data
	 * @return true for updating this object with the specified data
	 */
	public boolean update(EntityData entityData) {
		update_initial_role( entityData);
		update_number( entityData);
		for ( String kind:Constant._kinds)
			update( _objectMapMap.get( kind), entityData._objectMapMap.get( kind));
		return true;
	}

	/**
	 * @param entityData
	 */
	private void update_initial_role(EntityData entityData) {
		if ( !entityData._initialRole.equals( ""))
			_initialRole = entityData._initialRole;
	}

	/**
	 * @param entityData
	 */
	private void update_number(EntityData entityData) {
		if ( entityData._number.equals( ""))
			return;

		if ( !entityData.is_multi())
			_number = entityData._number;
		else {
			if ( _number.equals( ""))
				_number = entityData._number;
			else {
				int number = Integer.parseInt( _number);
				if ( Integer.parseInt( _number) < Integer.parseInt( entityData._number))
					_number = entityData._number;
			}
		}
	}

	/**
	 * @param destinationMap
	 * @param sourceMap
	 */
	private void update(TreeMap<String, Object> destinationMap, TreeMap<String, Object> sourceMap) {
		Iterator iterator = sourceMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			if ( !is_multi()) {
				ObjectBase objectBase = ( ObjectBase)entry.getValue();
				destinationMap.put( name, objectBase);
				if ( objectBase instanceof FileObject)
					( ( FileObject)objectBase).make();
			} else {
				Map<ObjectBase, Vector<int[]>> srcIndicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();

				Map<ObjectBase, Vector<int[]>> dstIndicesMap = ( Map<ObjectBase, Vector<int[]>>)destinationMap.get( name);
				if ( null == dstIndicesMap) {
					destinationMap.put( name, srcIndicesMap);
					if ( destinationMap == _objectMapMap.get( "file"))
						make_file( srcIndicesMap);

					continue;
				}

				update( dstIndicesMap, srcIndicesMap);
			}
		}
	}

	/**
	 * @param dstIndicesMap
	 * @param srcIndicesMap
	 */
	private void update(Map<ObjectBase, Vector<int[]>> dstIndicesMap, Map<ObjectBase, Vector<int[]>> srcIndicesMap) {
		Iterator iterator = srcIndicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			Vector<int[]> srcIndices = ( Vector<int[]>)entry.getValue();

			Vector<int[]> dstIndices = objectBase.get_indices( dstIndicesMap);
			if ( null == dstIndices) {
				if ( objectBase instanceof ClassVariableObject
					|| objectBase instanceof ExchangeAlgebraObject)
					dstIndicesMap.clear();	// dstIndicesMap can have only one pair.

				dstIndices = new Vector<int[]>();
				dstIndicesMap.put( objectBase, dstIndices);
			}

			if ( objectBase instanceof FileObject)
				( ( FileObject)objectBase).make();

			EntityData.arrange( dstIndicesMap, srcIndices, dstIndices);
		}
	}

	/**
	 * @param srcIndicesMap
	 * @return
	 */
	private boolean make_file(Map<ObjectBase, Vector<int[]>> srcIndicesMap) {
		boolean result = true;
		Iterator iterator = srcIndicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			FileObject fileObject = ( FileObject)entry.getKey();
			Vector<int[]> srcIndices = ( Vector<int[]>)entry.getValue();
			if ( !fileObject.make())
				result = false;
		}
		return result;
	}

	/**
	 * Returns true if this object has multiple initial data.
	 * @return true if this object has multiple initial data
	 */
	public boolean is_multi() {
		return _multi;
	}

//	/**
//	 * Returns true if this object has the specified data.
//	 * @param type the specified type
//	 * @param name the specified name
//	 * @param number the specified number
//	 * @return true if this object has the specified data
//	 */
//	public boolean has_same_name_with_type(String type, String name, String number) {
//		if ( ( type.equals( "agent") && ( this instanceof SpotObject))
//			|| ( type.equals( "spot") && ( this instanceof AgentObject)))
//			return false;
//
//		return has_same_name( name, number);
//	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#has_same_name(java.lang.String, java.lang.String)
	 */
	public boolean has_same_name(String name, String number) {
		return SoarsCommonTool.has_same_name( _name, _number, name, number);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#has_same_name(java.lang.String)
	 */
	public boolean has_same_name(String fullName) {
		return SoarsCommonTool.has_same_name( _name, _number, fullName);
	}

	/**
	 * Returns true if this object has the object which has the specified name.
	 * @param kind the type of the object
	 * @param name the specified name
	 * @return true if this object has the object which has the specified name
	 */
	public boolean has_same_object_name(String kind, String name) {
		TreeMap<String, Object> treeMap = _objectMapMap.get( kind);
		if ( null == treeMap)
			return false;

		return treeMap.containsKey( name);
	}

	/**
	 * Returns true if this object has the object which has the specified name.
	 * @param kind the type of the object
	 * @param name the specified name
	 * @param gis the specified GIS's ID
	 * @return true if this object has the object which has the specified name
	 */
	public boolean has_same_object_name(String kind, String name, String gis) {
		// TODO gisは""ではない
		//if ( gis.equals( _gis))
		if ( !_gis.equals( ""))
			// TODO GISで追加されたスポットは全て同じ対象とすることに変更した
			return false;

		TreeMap<String, Object> treeMap = _objectMapMap.get( kind);
		if ( null == treeMap)
			return false;

		return treeMap.containsKey( name);
	}

	/**
	 * @param name the specified name
	 * @return 
	 */
	public String which_object_has_this_name(String name) {
		for ( String kind:Constant._kinds) {
			if ( _objectMapMap.get( kind).containsKey( name))
				return kind;
		}
		return null;
	}

	/**
	 * Returns the integer array for the object.
	 * @param kind the object kind
	 * @param name the object name
	 * @return the integer array for the object
	 */
	public Vector<int[]> get_object_ranges(String kind, String name) {
		if ( !is_multi())
			return new Vector();

		TreeMap<String, Object> treeMap = _objectMapMap.get( kind);
		if ( null == treeMap)
			return new Vector();

		return EntityData.get_object_ranges( treeMap, name);
	}

	/**
	 * Returns the array of the object names.
	 * @param kinds the array of the object kinds
	 * @param number the specified number
	 * @param containsEmpty true for containing ""
	 * @return the array of the object names
	 */
	public String[] get_object_names(String[] kinds, String number, boolean containsEmpty) {
		Vector<String> objectNames = new Vector<String>();
		for ( int i = 0; i < kinds.length; ++i) {
			TreeMap<String, Object> treeMap = _objectMapMap.get( kinds[ i]);
			if ( null == treeMap)
				continue;

			get_object_names( objectNames, treeMap, number, containsEmpty);
		}

		if ( containsEmpty)
			objectNames.insertElementAt( "", 0);

		return Tool.quick_sort_string( objectNames, true, false);
	}

	/**
	 * @param objectNames
	 * @param treeMap
	 * @param number
	 * @param containsEmpty
	 */
	private void get_object_names(Vector<String> objectNames, TreeMap<String, Object> treeMap, String number, boolean containsEmpty) {
		objectNames.addAll( !is_multi() ? new Vector<String>( treeMap.keySet()) : get_object_names( treeMap, Integer.parseInt( number)));
	}

	/**
	 * Returns the array of the object names.
	 * @param kind the object kind
	 * @param number the specified number
	 * @param containsEmpty true for containing ""
	 * @return the array of the object names
	 */
	public String[] get_object_names(String kind, String number, boolean containsEmpty) {
		TreeMap<String, Object> treeMap = _objectMapMap.get( kind);
		if ( null == treeMap)
			return null;

		return get_object_names( treeMap, number, containsEmpty);
	}

	/**
	 * @param treeMap
	 * @param number
	 * @param containsEmpty
	 * @return
	 */
	private String[] get_object_names(TreeMap<String, Object> treeMap, String number, boolean containsEmpty) {
		Vector<String> objectNames;
		if ( !is_multi())
			objectNames = new Vector<String>( treeMap.keySet());
		else
			objectNames = get_object_names( treeMap, Integer.parseInt( number));

		if ( containsEmpty)
			objectNames.insertElementAt( "", 0);

		return Tool.quick_sort_string( objectNames, true, false);
	}

	/**
	 * @param treeMap
	 * @param number
	 * @return
	 */
	private Vector<String> get_object_names(TreeMap<String, Object> treeMap, int number) {
		Vector<String> objectNames = new Vector<String>();
		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
			if ( !contains( indicesMap, number))
				continue;

			objectNames.add( name);
		}
		return objectNames;
	}

	/**
	 * @param indicesMap
	 * @param number
	 * @return
	 */
	private boolean contains(Map<ObjectBase, Vector<int[]>> indicesMap, int number) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Vector<int[]> indices = ( Vector<int[]>)entry.getValue();
			for ( int i = 0; i < indices.size(); ++i) {
				int[] range = ( int[])indices.get( i);
				if ( range[ 0] <= number && number <= range[ 1])
					return true;
			}
		}
		return false;
	}

	/**
	 * @param number
	 * @param containsEmpty
	 * @return
	 */
	public String[] get_exchange_algebra_names(String number, boolean containsEmpty) {
		String[] names = get_object_names( "exchange algebra", number, containsEmpty);
		Vector<String> exchangeAlgebraNames;
		if ( null != names)
			exchangeAlgebraNames = new Vector<String>( Arrays.asList( names));
		else {
			exchangeAlgebraNames = new Vector<String>();
			if ( containsEmpty)
				exchangeAlgebraNames.insertElementAt( "", 0);
		}

		TreeMap<String, Object> treeMap = _objectMapMap.get( "class variable");
		if ( null == treeMap)
			return Tool.quick_sort_string( exchangeAlgebraNames, true, false);

		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			if ( exchangeAlgebraNames.contains( name))
				continue;

			if ( !is_multi()) {
				ClassVariableObject classVariableObject = ( ClassVariableObject)entry.getValue();
				if ( !classVariableObject.is_exchange_algebra())
					continue;

				exchangeAlgebraNames.add( name);
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				if ( null == indicesMap)
					continue;

				get_exchange_algebra_names( indicesMap, name, Integer.parseInt( number), exchangeAlgebraNames);
			}
		}

		return Tool.quick_sort_string( exchangeAlgebraNames, true, false);
	}

	/**
	 * @param indicesMap
	 * @param name
	 * @param number
	 * @param exchangeAlgebraNames
	 */
	private void get_exchange_algebra_names(Map<ObjectBase, Vector<int[]>> indicesMap, String name, int number, Vector<String> exchangeAlgebraNames) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ClassVariableObject classVariableObject = ( ClassVariableObject)entry.getKey();
			if ( !classVariableObject.is_exchange_algebra())
				continue;

			Vector<int[]> indices = ( Vector<int[]>)entry.getValue();
			for ( int i = 0; i < indices.size(); ++i) {
				int[] range = ( int[])indices.get( i);
				if ( range[ 0] <= number && number <= range[ 1]) {
					exchangeAlgebraNames.add( name);
					return;
				}
			}
		}
	}

	/**
	 * Returns the array of the object names.
	 * @param type the number variable type
	 * @param number the specified number
	 * @param containsEmpty true for containing ""
	 * @return the array of the object names
	 */
	public String[] get_number_object_names(String type, String number, boolean containsEmpty) {
		Vector<String> numberObjectNames = new Vector<String>();
		Iterator iterator = _objectMapMap.get( "number object").entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			if ( numberObjectNames.contains( name))
				continue;

			if ( !is_multi()) {
				NumberObject numberObject = ( NumberObject)entry.getValue();
				if ( numberObject._type.equals( type))
					numberObjectNames.add( name);
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				if ( contains( indicesMap, type, Integer.parseInt( number)))
					numberObjectNames.add( name);
			}
		}

		if ( containsEmpty)
			numberObjectNames.insertElementAt( "", 0);

		return Tool.quick_sort_string( numberObjectNames, true, false);
	}

	/**
	 * @param indicesMap
	 * @param type
	 * @param number
	 * @return
	 */
	private boolean contains(Map<ObjectBase, Vector<int[]>> indicesMap, String type, int number) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			NumberObject numberObject = ( NumberObject)entry.getKey();
			if ( !numberObject._type.equals( type))
				continue;

			Vector<int[]> indices = ( Vector<int[]>)entry.getValue();
			for ( int i = 0; i < indices.size(); ++i) {
				int[] range = ( int[])indices.get( i);
				if ( range[ 0] <= number && number <= range[ 1])
					return true;
			}
		}
		return false;
	}

	/**
	 * Gets the object names.
	 * @param kind the object kind
	 * @param objectNames the array for the object names
	 */
	public void get_object_names(String kind, Vector<String> objectNames) {
		TreeMap<String, Object> treeMap = _objectMapMap.get( kind);
		if ( null == treeMap)
			return;

		get_object_names( treeMap, objectNames);
	}

	/**
	 * @param treeMap
	 * @param objectNames
	 */
	private void get_object_names(TreeMap<String, Object> treeMap, Vector<String> objectNames) {
		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			if ( objectNames.contains( name))
				continue;

			objectNames.add( name);
		}
	}

	/**
	 * @param exchangeAlgebraNames
	 */
	public void get_exchange_algebra_names(Vector<String> exchangeAlgebraNames) {
		get_object_names( "exchange algebra", exchangeAlgebraNames);

		TreeMap<String, Object> treeMap = _objectMapMap.get( "class variable");
		if ( null == treeMap)
			return;

		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			if ( exchangeAlgebraNames.contains( name))
				continue;

			if ( !is_multi()) {
				ClassVariableObject classVariableObject = ( ClassVariableObject)entry.getValue();
				if ( !classVariableObject.is_exchange_algebra())
					continue;

				exchangeAlgebraNames.add( name);
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				if ( null == indicesMap)
					continue;

				get_exchange_algebra_names( indicesMap, name, exchangeAlgebraNames);
			}
		}
	}

	/**
	 * @param indicesMap
	 * @param name
	 * @param exchangeAlgebraNames
	 */
	private void get_exchange_algebra_names(Map<ObjectBase, Vector<int[]>> indicesMap, String name, Vector<String> exchangeAlgebraNames) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ClassVariableObject classVariableObject = ( ClassVariableObject)entry.getKey();
			if ( !classVariableObject.is_exchange_algebra())
				continue;

			exchangeAlgebraNames.add( name);
			return;
		}
	}

	/**
	 * @param name
	 * @return
	 */
	public boolean is_exchange_algebra(String name) {
		TreeMap<String, Object> treeMap = _objectMapMap.get( "class variable");
		if ( null == treeMap)
			return false;

		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			if ( !name.equals( ( String)entry.getKey()))
				continue;

			if ( !is_multi()) {
				ClassVariableObject classVariableObject = ( ClassVariableObject)entry.getValue();
				return classVariableObject.is_exchange_algebra();
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				if ( null == indicesMap)
					return false;

				return is_exchange_algebra( indicesMap, name);
			}
		}

		return false;
	}

	/**
	 * @param indicesMap
	 * @param name
	 * @return
	 */
	private boolean is_exchange_algebra(Map<ObjectBase, Vector<int[]>> indicesMap, String name) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ClassVariableObject classVariableObject = ( ClassVariableObject)entry.getKey();
			return classVariableObject.is_exchange_algebra();
		}
		return false;
	}

	/**
	 * Gets the number variable names.
	 * @param type the number variable type
	 * @param numberObjectNames the array for the number variable names
	 */
	public void get_number_object_names(String type, Vector<String> numberObjectNames) {
		Iterator iterator = _objectMapMap.get( "number object").entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			if ( numberObjectNames.contains( name))
				continue;

			if ( !is_multi()) {
				NumberObject numberObject = ( NumberObject)entry.getValue();
				if ( numberObject._type.equals( type))
					numberObjectNames.add( name);
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				if ( contains( indicesMap, type))
					numberObjectNames.add( name);
			}
		}
	}

	/**
	 * @param indicesMap
	 * @param type
	 * @return
	 */
	private boolean contains(Map<ObjectBase, Vector<int[]>> indicesMap, String type) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			NumberObject numberObject = ( NumberObject)entry.getKey();
			return numberObject._type.equals( type);
		}
		return false;
	}

	/**
	 * Returns true if this object contains the object which has the specified alias as the initial value.
	 * @param kind the object kind
	 * @param alias the specified alias
	 * @return true if this object contains the object which has the specified alias as the initial value
	 */
	public boolean object_contains_this_alias(String kind, String alias) {
		TreeMap<String, Object> treeMap = _objectMapMap.get( kind);
		if ( null == treeMap)
			return false;

		return object_contains_this_alias( treeMap, alias);
	}

	/**
	 * @param treeMap
	 * @param alias
	 * @return
	 */
	private boolean object_contains_this_alias(TreeMap<String, Object> treeMap, String alias) {
		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			if ( !is_multi()) {
				ObjectBase objectBase = ( ObjectBase)entry.getValue();
				if ( objectBase.contains_this_alias( alias))
					return true;
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				if ( object_contains_this_alias( indicesMap, alias))
					return true;
			}
		}
		return false;
	}

	/**
	 * @param indicesMap
	 * @param alias
	 * @return
	 */
	private boolean object_contains_this_alias(Map<ObjectBase, Vector<int[]>> indicesMap, String alias) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			if ( objectBase.contains_this_alias( alias))
				return true;
		}
		return false;
	}

	/**
	 * Gets the initial values.
	 * @param kind the object kind
	 * @param initialValues the array for the initial values
	 */
	public void get_object_initial_values(String kind, Vector<String> initialValues) {
		TreeMap<String, Object> treeMap = _objectMapMap.get( kind);
		if ( null == treeMap)
			return;

		get_object_initial_values1( treeMap, initialValues);
	}

	/**
	 * @param treeMap
	 * @param initialValues
	 */
	private void get_object_initial_values1(TreeMap<String, Object> treeMap, Vector<String> initialValues) {
		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			if ( !is_multi()) {
				ObjectBase objectBase = ( ObjectBase)entry.getValue();
				objectBase.get_initial_values1( initialValues);
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				get_object_initial_values1( indicesMap, initialValues);
			}
		}
	}

	/**
	 * @param indicesMap
	 * @param initialValues
	 */
	private void get_object_initial_values1(Map<ObjectBase, Vector<int[]>> indicesMap, Vector<String> initialValues) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			objectBase.get_initial_values1( initialValues);
		}
	}

	/**
	 * Gets the initial values.
	 * @param kind the object kind
	 * @param name the object name
	 * @param initialValues the array for the initial values
	 */
	public void get_object_initial_values(String kind, String name, Vector<String> initialValues) {
		if ( kind.equals( "integer") || kind.equals( "real number"))
			get_object_initial_values2( kind, name, initialValues);
		else {
			TreeMap<String, Object> treeMap = _objectMapMap.get( kind);
			if ( null == treeMap)
				return;

			get_object_initial_values2( treeMap, name, initialValues);
		}
	}

	/**
	 * @param treeMap
	 * @param name
	 * @param initialValues
	 */
	private void get_object_initial_values2(TreeMap<String, Object> treeMap, String name, Vector<String> initialValues) {
		if ( !is_multi()) {
			ObjectBase objectBase = ( ObjectBase)treeMap.get( name);
			objectBase.get_initial_values2( initialValues);
		} else {
			Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)treeMap.get( name);
			get_object_initial_values2( indicesMap, initialValues);
		}
	}

	/**
	 * @param indicesMap
	 * @param initialValues
	 */
	private void get_object_initial_values2(Map<ObjectBase, Vector<int[]>> indicesMap, Vector<String> initialValues) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			objectBase.get_initial_values2( initialValues);
		}
	}

	/**
	 * @param type
	 * @param name
	 * @param initialValues
	 */
	private void get_object_initial_values2(String type, String name, Vector<String> initialValues) {
		if ( !is_multi()) {
			NumberObject numberObject = ( NumberObject)_objectMapMap.get( "number object").get( name);
			if ( numberObject._type.equals( type))
				numberObject.get_initial_values2( initialValues);
		} else {
			Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)_objectMapMap.get( "number object").get( name);
			get_number_object_initial_values( type, indicesMap, initialValues);
		}
	}

	/**
	 * @param type
	 * @param indicesMap
	 * @param initial_values
	 */
	private void get_number_object_initial_values(String type, Map<ObjectBase, Vector<int[]>> indicesMap, Vector<String> initialValues) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			NumberObject numberObject = ( NumberObject)entry.getKey();
			if ( numberObject._type.equals( type))
				numberObject.get_initial_values2( initialValues);
		}
	}

	/**
	 * Returns true if the specified class variable name is in use for a different class on other objects except the specified one.
	 * @param classVariable the specified class variable
	 * @param jarFilename the jar file name
	 * @param classname the class name
	 * @return true if the specified class variable name is in use for a different class on other objects except the specified one
	 */
	public boolean uses_this_class_variable_as_different_class(String classVariable, String jarFilename, String classname) {
		Iterator iterator = _objectMapMap.get( "class variable").entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			if ( !name.equals( classVariable))
				continue;

			if ( !is_multi()) {
				ClassVariableObject classVariableObject = ( ClassVariableObject)entry.getValue();
				if ( !classVariableObject._jarFilename.equals( jarFilename) || !classVariableObject._classname.equals( classname)) {
					String[] message = new String[] {
						( ( this instanceof AgentObject) ? "Agent" : "Spot"),
						"name = " + _name,
						"Class variable : " + classVariable + " : " + "[" + classname + ", " + jarFilename + "] " + "[" + classVariableObject._classname + ", " + classVariableObject._jarFilename + "]"
					};

					WarningManager.get_instance().add( message);

					return true;
				}
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				if ( uses_this_class_variable_as_different_class( indicesMap, classVariable, jarFilename, classname))
					return true;
			}
		}
		return false;
	}

	/**
	 * @param indicesMap
	 * @param classVariable
	 * @param jarFilename
	 * @param classname
	 * @return
	 */
	private boolean uses_this_class_variable_as_different_class(Map<ObjectBase, Vector<int[]>> indicesMap, String classVariable, String jarFilename, String classname) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ClassVariableObject classVariableObject = ( ClassVariableObject)entry.getKey();
			if ( !classVariableObject._jarFilename.equals( jarFilename) || !classVariableObject._classname.equals( classname)) {
				String[] message = new String[] {
					( ( this instanceof AgentObject) ? "Agent" : "Spot"),
					"name = " + _name,
					"Class variable : " + classVariable + " : " + "[" + classname + ", " + jarFilename + "] " + "[" + classVariableObject._classname + ", " + classVariableObject._jarFilename + "]"
				};

				WarningManager.get_instance().add( message);

				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true the specified class is in use.
	 * @param jarFilename the specified jar file name
	 * @param classname the specified class name
	 * @return true the specified class is in use
	 */
	public boolean uses_this_class(String jarFilename, String classname) {
		Iterator iterator = _objectMapMap.get( "class variable").entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			if ( !is_multi()) {
				ClassVariableObject classVariableObject = ( ClassVariableObject)entry.getValue();
				if ( classVariableObject._jarFilename.equals( jarFilename)
					&& classVariableObject._classname.equals( classname))
					return true;
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				if ( uses_this_class( indicesMap, jarFilename, classname))
					return true;
			}
		}
		return false;
	}

	/**
	 * @param indicesMap
	 * @param jarFilename
	 * @param classname
	 * @return
	 */
	private boolean uses_this_class(Map<ObjectBase, Vector<int[]>> indicesMap, String jarFilename, String classname) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ClassVariableObject classVariableObject = ( ClassVariableObject)entry.getKey();
			if ( classVariableObject._jarFilename.equals( jarFilename)
				&& classVariableObject._classname.equals( classname))
				return true;
		}
		return false;
	}

//	/**
//	 * @param file
//	 * @param kind
//	 * @return
//	 */
//	public boolean uses_this_file(File file, String kind) {
//		if ( !kind.equals( "file")) {
//			Iterator iterator = _objectMapMap.get( "file").entrySet().iterator();
//			while ( iterator.hasNext()) {
//				Object object = iterator.next();
//				Map.Entry entry = ( Map.Entry)object;
//				String name = ( String)entry.getKey();
//				if ( !is_multi()) {
//					FileObject fileObject = ( FileObject)entry.getValue();
//					if ( fileObject.uses_this_file( file))
//						return true;
//				} else {
//					Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
//					if ( uses_this_file( indicesMap, file))
//						return true;
//				}
//			}
//		}
//		if ( !is_multi()) {
//			if ( !kind.equals( "initial data file")) {
//				List<ObjectBase> objectBases = _objectListMap.get( "initial data file");
//				for ( ObjectBase objectBase:objectBases) {
//					InitialDataFileObject initialDataFileObject = ( InitialDataFileObject)objectBase;
//					if ( initialDataFileObject.uses_this_file( file))
//						return true;
//				}
//			}
//			if ( !kind.equals( "extransfer")) {
//				Iterator iterator = _objectMapMap.get( "extransfer").entrySet().iterator();
//				while ( iterator.hasNext()) {
//					Object object = iterator.next();
//					Map.Entry entry = ( Map.Entry)object;
//					ExTransferObject exTransferObject = ( ExTransferObject)entry.getValue();
//					if ( exTransferObject.uses_this_file( file))
//						return true;
//				}
//			}
//		}
//		return false;
//	}

	/**
	 * @param indicesMap
	 * @param file
	 * @return
	 */
	private boolean uses_this_file(Map<ObjectBase, Vector<int[]>> indicesMap, File file) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			FileObject fileObject = ( FileObject)entry.getKey();
			if ( fileObject.uses_this_file( file))
				return true;
		}
		return false;
	}

	/**
	 * @param srcPath
	 * @param destPath
	 * @return
	 */
	public boolean move_file(File srcPath, File destPath) {
		boolean result = false;
		Iterator iterator = _objectMapMap.get( "file").entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			if ( !is_multi()) {
				FileObject fileObject = ( FileObject)entry.getValue();
				if ( fileObject.move_file( srcPath, destPath))
					result = true;
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				if ( move_file( indicesMap, srcPath, destPath))
					result = true;
			}
		}
		if ( !is_multi()) {
			List<ObjectBase> objectBases = _objectListMap.get( "initial data file");
			for ( ObjectBase objectBase:objectBases) {
				InitialDataFileObject initialDataFileObject = ( InitialDataFileObject)objectBase;
				if ( initialDataFileObject.move_file( srcPath, destPath))
					result = true;
			}
			iterator = _objectMapMap.get( "extransfer").entrySet().iterator();
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				ExTransferObject exTransferObject = ( ExTransferObject)entry.getValue();
				if ( exTransferObject.move_file( srcPath, destPath))
					return true;
			}
		}
		return result;
	}

	/**
	 * @param indicesMap
	 * @param srcPath
	 * @param destPath
	 * @return
	 */
	private boolean move_file(Map<ObjectBase, Vector<int[]>> indicesMap, File srcPath, File destPath) {
		boolean result = false;
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			FileObject fileObject = ( FileObject)entry.getKey();
			if ( fileObject.move_file( srcPath, destPath))
				return true;
		}
		return result;
	}

	/**
	 * @param files
	 * @return
	 */
	public boolean get_user_data_files(List<File> files) {
		Iterator iterator = _objectMapMap.get( "file").entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			if ( !is_multi()) {
				FileObject fileObject = ( FileObject)entry.getValue();
				if ( !fileObject.get( files))
					return false;
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				if ( !get_user_data_files( indicesMap, files))
					return false;
			}
		}
		if ( !is_multi()) {
			List<ObjectBase> objectBases = _objectListMap.get( "initial data file");
			for ( ObjectBase objectBase:objectBases) {
				InitialDataFileObject initialDataFileObject = ( InitialDataFileObject)objectBase;
				if ( !initialDataFileObject.get( files))
					return false;
			}
			iterator = _objectMapMap.get( "extransfer").entrySet().iterator();
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				ExTransferObject exTransferObject = ( ExTransferObject)entry.getValue();
				if ( exTransferObject.get( files))
					return true;
			}
		}
		return true;
	}

	/**
	 * @param indicesMap
	 * @param files
	 * @return
	 */
	private boolean get_user_data_files(Map<ObjectBase, Vector<int[]>> indicesMap, List<File> files) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			FileObject fileObject = ( FileObject)entry.getKey();
			if ( !fileObject.get( files))
				return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	public boolean has_initial_data_file() {
		return ( !is_multi() && !_objectListMap.get( "initial data file").isEmpty());
	}

	/**
	 * @param initialDataFiles
	 */
	public void get_initial_data_files(List<String> initialDataFiles) {
		if ( !is_multi()) {
			List<ObjectBase> objectBases = _objectListMap.get( "initial data file");
			for ( ObjectBase objectBase:objectBases) {
				if ( initialDataFiles.contains( objectBase._name))
					continue;

				String type = InitialDataFileChecker.get_initial_data_file_type( objectBase._name);
				if ( null != type && type.equals( "common"))
					initialDataFiles.add( objectBase._name);
			}
		}
	}

	/**
	 * @param exchangeAlgebraInitialDataFiles
	 */
	public void get_exchange_algebra_initial_data_files(List<String> exchangeAlgebraInitialDataFiles) {
		if ( !is_multi()) {
			List<ObjectBase> objectBases = _objectListMap.get( "initial data file");
			for ( ObjectBase objectBase:objectBases) {
				if ( exchangeAlgebraInitialDataFiles.contains( objectBase._name))
					continue;

				String type = InitialDataFileChecker.get_initial_data_file_type( objectBase._name);
				if ( null != type && type.equals( "exchange algebra"))
					exchangeAlgebraInitialDataFiles.add( objectBase._name);
			}
		}
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_adjust_agent_name(java.lang.String, java.util.Vector)
	 */
	public boolean can_adjust_agent_name(String headName, Vector<String[]> ranges) {
		return can_adjust_name( "agent", headName, ranges);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_adjust_spot_name(java.lang.String, java.util.Vector)
	 */
	public boolean can_adjust_spot_name(String headName, Vector<String[]> ranges) {
		return can_adjust_name( "spot", headName, ranges);
	}

	/**
	 * @param type
	 * @param headName
	 * @param ranges
	 * @return
	 */
	private boolean can_adjust_name(String type, String headName, Vector<String[]> ranges) {
		boolean result = true;
		for ( String kind:Constant._kinds) {
			if ( !can_adjust_name( _objectMapMap.get( kind), type, headName, ranges))
				result = false;
		}
		return result;
	}

	/**
	 * @param treeMap
	 * @param type
	 * @param headName
	 * @param ranges
	 * @return
	 */
	private boolean can_adjust_name(TreeMap<String, Object> treeMap, String type, String headName, Vector<String[]> ranges) {
		boolean result = true;
		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			if ( !is_multi()) {
				ObjectBase objectBase = ( ObjectBase)entry.getValue();
				if ( !objectBase.can_adjust_name( type, headName, ranges, this))
					result = false;
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				if ( !can_adjust_name( indicesMap, type, headName, ranges))
					result = false;
			}
		}
		return result;
	}

	/**
	 * @param indicesMap
	 * @param type
	 * @param headName
	 * @param ranges
	 * @return
	 */
	private boolean can_adjust_name(Map<ObjectBase, Vector<int[]>> indicesMap, String type, String headName, Vector<String[]> ranges) {
		boolean result = true;
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			if ( !objectBase.can_adjust_name( type, headName, ranges, this))
				result = false;
		}
		return result;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_adjust_agent_name(java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean can_adjust_agent_name(String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		return can_adjust_name( "agent", headName, ranges, newHeadName, newRanges);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_adjust_spot_name(java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean can_adjust_spot_name(String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		return can_adjust_name( "spot", headName, ranges, newHeadName, newRanges);
	}

	/**
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	private boolean can_adjust_name(String type, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = true;
		for ( String kind:Constant._kinds) {
			if ( !can_adjust_name( _objectMapMap.get( kind), type, headName, ranges, newHeadName, newRanges))
				result = false;
		}
		return result;
	}

	/**
	 * @param treeMap
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	private boolean can_adjust_name(TreeMap<String, Object> treeMap, String type, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = true;
		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			if ( !is_multi()) {
				ObjectBase objectBase = ( ObjectBase)entry.getValue();
				if ( !objectBase.can_adjust_name( type, headName, ranges, newHeadName, newRanges, this))
					result = false;
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				if ( !can_adjust_name( indicesMap, type, headName, ranges, newHeadName, newRanges))
					result = false;
			}
		}
		return result;
	}

	/**
	 * @param indicesMap
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	private boolean can_adjust_name(Map<ObjectBase, Vector<int[]>> indicesMap, String type, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = true;
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			if ( !objectBase.can_adjust_name( type, headName, ranges, newHeadName, newRanges, this))
				result = false;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#update_agent_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean update_agent_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		return update_name_and_number( "agent", newName, originalName, headName, ranges, newHeadName, newRanges);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		return update_name_and_number( "spot", newName, originalName, headName, ranges, newHeadName, newRanges);
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
	private boolean update_name_and_number(String type, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = false;
		for ( String kind:Constant._kinds) {
			if ( update_name_and_number( _objectMapMap.get( kind), type, newName, originalName, headName, ranges, newHeadName, newRanges))
				result = true;
		}
		return result;
	}

/**
	 * @param treeMap
	 * @param type
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	private boolean update_name_and_number(TreeMap<String, Object> treeMap, String type, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = false;
		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			if ( !is_multi()) {
				ObjectBase objectBase = ( ObjectBase)entry.getValue();
				if ( objectBase.update_name_and_number( type, newName, originalName, headName, ranges, newHeadName, newRanges))
					result = true;
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				if ( update_name_and_number( indicesMap, type, newName, originalName, headName, ranges, newHeadName, newRanges))
					result = true;
			}
		}
		return result;
	}

	/**
	 * @param indicesMap
	 * @param type
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	private boolean update_name_and_number(Map<ObjectBase, Vector<int[]>> indicesMap, String type, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = false;
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			if ( objectBase.update_name_and_number( type, newName, originalName, headName, ranges, newHeadName, newRanges))
				result = true;
		}
		return result;
	}

	/**
	 * @return
	 */
	protected boolean can_remove_objects_names() {
		for ( String kind:Constant._kinds) {
			if ( kind.equals( "number object")) {
				if ( !can_remove_number_object_names())
					return false;
			} else if ( kind.equals( "role variable")) {
				if ( !can_remove_role_variable_names())
					return false;
			} else {
			if ( !can_remove_object_names( kind, _objectMapMap.get( kind)))
				return false;
			}
		}
		return true;
	}

	/**
	 * @param kind
	 * @param treeMap
	 * @return
	 */
	protected boolean can_remove_object_names(String kind, TreeMap<String, Object> treeMap) {
		boolean result1 = can_remove_object_names_old( kind, treeMap);
		boolean result2 = can_remove_object_names_new( kind, treeMap);
		return ( result1 && result2);
	}

	/**
	 * @param kind
	 * @param treeMap
	 * @return
	 */
	private boolean can_remove_object_names_old(String kind, TreeMap<String, Object> treeMap) {
		// TODO 従来のもの
		Vector<String> objectNames = new Vector<String>( treeMap.keySet());

		if ( this instanceof AgentObject)
			objectNames = LayerManager.get_instance().get_unremovable_object_names( kind, objectNames);

		String headName = null;
		Vector<String[]> ranges = null;
		if ( this instanceof SpotObject) {
			headName = SoarsCommonTool.separate( _name);
			String headNumber = _name.substring( headName.length());
			ranges = SoarsCommonTool.get_ranges( headNumber, _number);
		}

		boolean result = true;
		for ( String objectName:objectNames) {
			if ( !LayerManager.get_instance().can_remove(
				kind,
				objectName,
				( ( this instanceof SpotObject)
					? LayerManager.get_instance().other_spots_have_same_object_name( kind, objectName)
					: false),
				headName,
				ranges,
				true))
				result = false;
		}

		return result;
	}

	/**
	 * @param kind
	 * @param treeMap
	 * @return
	 */
	private boolean can_remove_object_names_new(String kind, TreeMap<String, Object> treeMap) {
		// TODO これからはこちらに移行してゆく
		Vector<String> objectNames = new Vector<String>( treeMap.keySet());

		String headName = SoarsCommonTool.separate( _name);
		String headNumber = _name.substring( headName.length());
		Vector<String[]> ranges = SoarsCommonTool.get_ranges( headNumber, _number);

		String entityType = null;
		if ( this instanceof AgentObject)
			entityType = "agent";
		else if ( this instanceof SpotObject)
			entityType = "spot";
		else
			return false;

		boolean result = true;
		for ( String objectName:objectNames) {
			if ( !LayerManager.get_instance().can_remove(
					entityType,
				kind,
				objectName,
				( entityType.equals( "agent")
					? LayerManager.get_instance().other_agents_have_same_object_name( kind, objectName)
					: LayerManager.get_instance().other_spots_have_same_object_name( kind, objectName)),
				headName,
				ranges,
				true))
				result = false;
		}

		return result;
	}

	/**
	 * @return
	 */
	protected boolean can_remove_number_object_names() {
		boolean result1 = can_remove_number_object_names_old();
		boolean result2 = can_remove_number_object_names_new();
		return ( result1 && result2);
	}

	/**
	 * @return
	 */
	protected boolean can_remove_number_object_names_old() {
		// TODO 従来のもの
		Vector<String> numberObjectNames = new Vector<String>( _objectMapMap.get( "number object").keySet());

		Vector<String> unremovableNumberObjectNames = ( ( this instanceof AgentObject)
			? LayerManager.get_instance().get_unremovable_object_names( "number object", numberObjectNames)
			: numberObjectNames);

		String headName = null;
		Vector<String[]> ranges = null;
		if ( this instanceof SpotObject) {
			headName = SoarsCommonTool.separate( _name);
			String headNumber = _name.substring( headName.length());
			ranges = SoarsCommonTool.get_ranges( headNumber, _number);
		}

		boolean result1 = true;
		for ( String unremovableNumberObjectName:unremovableNumberObjectNames) {
			if ( !LayerManager.get_instance().can_remove(
				"number object",
				unremovableNumberObjectName,
				( ( this instanceof SpotObject)
					? LayerManager.get_instance().other_spots_have_same_object_name( "number object", unremovableNumberObjectName)
					: false),
				headName,
				ranges,
				true))
				result1 = false;
		}

		boolean result2 = true;
		headName = SoarsCommonTool.separate( _name);
		String headNumber = _name.substring( headName.length());
		ranges = SoarsCommonTool.get_ranges( headNumber, _number);

		for ( String numberObjectName:numberObjectNames) {
			if ( !LayerManager.get_instance().can_remove(
				"number object",
				( this instanceof AgentObject) ? "agent" : "spot",
				numberObjectName,
				headName,
				ranges,
				true))
				result2 = false;
		}

		return ( result1 && result2);
	}

	/**
	 * @return
	 */
	protected boolean can_remove_number_object_names_new() {
		// TODO これからはこちらに移行してゆく
		Vector<String> numberObjectNames = new Vector<String>( _objectMapMap.get( "number object").keySet());

		String headName = SoarsCommonTool.separate( _name);
		String headNumber = _name.substring( headName.length());
		Vector<String[]> ranges = SoarsCommonTool.get_ranges( headNumber, _number);

		String entityType = null;
		if ( this instanceof AgentObject)
			entityType = "agent";
		else if ( this instanceof SpotObject)
			entityType = "spot";
		else
			return false;

		// Roleをチェック
		boolean result1 = true;
		for ( String numberObjectName:numberObjectNames) {
			if ( !LayerManager.get_instance().can_remove(
					entityType,
				"number object",
				numberObjectName,
				( entityType.equals( "agent")
					? LayerManager.get_instance().other_agents_have_same_object_name( "number object", numberObjectName)
					: LayerManager.get_instance().other_spots_have_same_object_name( "number object", numberObjectName)),
				headName,
				ranges,
				true))
				result1 = false;
		}

		// ChartObjectをチェック
		boolean result2 = true;
		for ( String numberObjectName:numberObjectNames) {
			if ( !LayerManager.get_instance().can_remove(
				"number object",
				entityType,
				numberObjectName,
				headName,
				ranges,
				true))
				result2 = false;
		}

		return ( result1 && result2);
	}

	/**
	 * @return
	 */
	protected boolean can_remove_role_variable_names() {
		boolean result1 = can_remove_role_variable_names_old();
		boolean result2 = can_remove_role_variable_names_new();
		return ( result1 && result2);
	}

	/**
	 * @return
	 */
	protected boolean can_remove_role_variable_names_old() {
		// TODO 従来のもの
		Vector<String> roleVariableNames = new Vector<String>( _objectMapMap.get( "role variable").keySet());

		if ( this instanceof AgentObject)
			roleVariableNames = LayerManager.get_instance().get_unremovable_object_names( "role variable", roleVariableNames);

		String headName = null;
		Vector<String[]> ranges = null;
//		if ( this instanceof SpotObject) {
//			headName = EntityBase.separate( _name);
//			String headNumber = _name.substring( headName.length());
//			ranges = EntityBase.get_ranges( headNumber, _number);
//		}

		boolean result = true;
		for ( String roleVariableName:roleVariableNames) {
			if ( !LayerManager.get_instance().can_remove(
				"role variable",
				roleVariableName,
//				( ( this instanceof SpotObject)
//					? LayerManager.get_instance().other_spots_have_same_object_name( "role variable", roleVariableName)
//					: false),
				false,
				headName,
				ranges,
				this,
				true))
				result = false;
		}

		return result;
	}

	/**
	 * @return
	 */
	protected boolean can_remove_role_variable_names_new() {
		// TODO これからはこちらに移行してゆく
		if ( this instanceof SpotObject)
			return true;

		Vector<String> roleVariableNames = new Vector<String>( _objectMapMap.get( "role variable").keySet());

		String headName = SoarsCommonTool.separate( _name);
		String headNumber = _name.substring( headName.length());
		Vector<String[]> ranges = SoarsCommonTool.get_ranges( headNumber, _number);

		boolean result = true;
		for ( String roleVariableName:roleVariableNames) {
			if ( !LayerManager.get_instance().can_remove(
				"agent",
				"role variable",
				roleVariableName,
				LayerManager.get_instance().other_agents_have_same_object_name( "role variable", roleVariableName),
				headName,
				ranges,
				this,
				true))
				result = false;
		}

		return result;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_remove_role_name(java.lang.String)
	 */
	public boolean can_remove_role_name(String roleName) {
		if ( roleName.equals( ""))
			return true;

		boolean result = true;
		if ( !is_multi()) {
			if ( !_initialRole.equals( "") && _initialRole.equals( roleName)) {
				String[] message = new String[] {
					( ( this instanceof AgentObject) ? "Agent" : "Spot"),
					"name = " + _name,
					"initial role = " + _initialRole
				};

				WarningManager.get_instance().add( message);

				result = false;
			}

			Iterator iterator = _objectMapMap.get( "role variable").entrySet().iterator();
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				RoleVariableObject roleVariableObject = ( RoleVariableObject)entry.getValue();
				if ( roleVariableObject._initialValue.equals( roleName)) {
					String[] message = new String[] {
						( ( this instanceof AgentObject) ? "Agent" : "Spot"),
						"name = " + _name,
						"Role variable uses " + roleName + "."
					};
	
					WarningManager.get_instance().add( message);
	
					result = false;
				}
			}
		} else {
			Iterator iterator = _objectMapMap.get( "role variable").entrySet().iterator();
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				if ( indicesMap.containsKey( roleName)) {
					String[] message = new String[] {
						( ( this instanceof AgentObject) ? "Agent" : "Spot"),
						"name = " + _name,
						"Role variable uses " + roleName + "."
					};

					WarningManager.get_instance().add( message);

					result = false;

					break;
				}
			}
		}

		return result;
	}

	/**
	 * Returns true for updating the specified number variable name with the new one successfully, except the specified agent or spot object.
	 * @param kind the specified kind of the object
	 * @param name the specified name of the number variable
	 * @param newName the new name of the number variable
	 * @param type the specified type of the number variable
	 * @return true for updating the specified number variable name with the new one successfully, except the specified agent or spot object
	 */
	public boolean update_object_name(String kind, String name, String newName, String entity) {
		if ( name.equals( newName))
			return false;

		if ( ( entity.equals( "agent") && this instanceof AgentObject)
			|| ( entity.equals( "spot") && this instanceof SpotObject))
			return update_object_name( kind, name, newName);

		return false;
	}

	/**
	 * Updates the specified object name with the new one.
	 * @param kind the object kind
	 * @param name the specified name
	 * @param newName the new name
	 * @return
	 */
	public boolean update_object_name(String kind, String name, String newName) {
		TreeMap<String, Object> treeMap = _objectMapMap.get( kind);
		if ( null == treeMap)
			return false;

		return update_object_name( treeMap, kind, name, newName);
	}

	/**
	 * @param treeMap
	 * @param kind
	 * @param name
	 * @param newName
	 * @return
	 */
	private boolean update_object_name(TreeMap<String, Object> treeMap, String kind, String name, String newName) {
		if ( name.equals( newName))
			return false;

		boolean result = false;

		if ( !is_multi()) {
			ObjectBase objectBase = ( ObjectBase)treeMap.get( name);
			if ( null != objectBase) {
				objectBase._name = newName;
				treeMap.put( newName, objectBase);
				treeMap.remove( name);
				result = true;
			}
		} else {
			Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)treeMap.get( name);
			if ( null != indicesMap) {
				update_object_name( newName, indicesMap);
				treeMap.put( newName, indicesMap);
				treeMap.remove( name);
				result = true;
			}
		}

		if ( update_variable_initial_value( kind, name, newName))
			result = true;

		return result;
	}

	/**
	 * @param newName
	 * @param indicesMap
	 */
	private void update_object_name(String newName, Map<ObjectBase, Vector<int[]>> indicesMap) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			objectBase._name = newName;
		}
	}

	/**
	 * @param type
	 * @param name
	 * @param newName
	 * @return
	 */
	private boolean update_variable_initial_value(String type, String name, String newName) {
		boolean result = false;
		for ( String kind:Constant._kinds) {
			if ( update_variable_initial_value( _objectMapMap.get( kind), type, name, newName))
				result = true;
		}
		return result;
	}

	/**
	 * @param treeMap
	 * @param type
	 * @param name
	 * @param newName
	 * @return
	 */
	private boolean update_variable_initial_value(TreeMap<String, Object> treeMap, String type, String name, String newName) {
		boolean result = false;
		if ( !is_multi()) {
			Iterator iterator = treeMap.entrySet().iterator();
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				ObjectBase objectBase = ( ObjectBase)entry.getValue();
				if ( objectBase.update_object_name( type, name, newName))
					result = true;
			}
		} else {
			Iterator iterator = treeMap.entrySet().iterator();
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				if ( update_variable_initial_value( type, name, newName, indicesMap))
					result = true;
			}
		}
		return result;
	}

	/**
	 * @param type
	 * @param name
	 * @param newName
	 * @param indicesMap
	 * @return
	 */
	private boolean update_variable_initial_value(String type, String name, String newName, Map<ObjectBase, Vector<int[]>> indicesMap) {
		boolean result = false;
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			if ( objectBase.update_object_name( type, name, newName))
				result = true;
		}
		return result;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_paste(soars.application.visualshell.layer.Layer)
	 */
	public boolean can_paste(Layer drawObjects) {
		for ( String kind:Constant._kinds) {
			if ( !can_paste( kind, _objectMapMap.get( kind), drawObjects))
				return false;
		}
		for ( String exceptionalKind:Constant._exceptionalKinds) {
			if ( !can_paste( exceptionalKind, _objectListMap.get( exceptionalKind), drawObjects))
				return false;
		}
		return true;
	}

	/**
	 * @param exceptionalKind
	 * @param objectBases
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste(String exceptionalKind, List<ObjectBase> objectBases, Layer drawObjects) {
		if ( is_multi())
			return true;

		boolean result = true;
		for ( ObjectBase objectBase:objectBases) {
			if ( !objectBase.can_paste( this, drawObjects))
				result = false;
		}
		return result;
	}

	/**
	 * @param kind
	 * @param treeMap
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste(String kind, TreeMap<String, Object> treeMap, Layer drawObjects) {
		boolean result = true;
		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			if ( null != drawObjects.get_chart( name)
				|| is_object_name( drawObjects, kind, name)) {
				String[] message = new String[] {
					( ( this instanceof AgentObject) ? "Agent" : "Spot"),
					"name = " + _name,
					"Duplicate name : " + kind + " = " + name
				};

				WarningManager.get_instance().add( message);

				result = false;
			}

			if ( !is_multi()) {
				ObjectBase objectBase = ( ObjectBase)entry.getValue();
				if ( !objectBase.can_paste( this, drawObjects))
					result = false;
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				if ( !can_paste( indicesMap, drawObjects))
					result = false;
			}
		}

		return result;
	}

	/**
	 * @param indicesMap
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste(Map<ObjectBase, Vector<int[]>> indicesMap, Layer drawObjects) {
		boolean result = true;
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			if ( !objectBase.can_paste( this, drawObjects))
				result = false;
		}
		return result;
	}

	/**
	 * @param drawObjects
	 * @param kind
	 * @param name
	 * @return
	 */
	private boolean is_object_name(Layer drawObjects, String kind, String name) {
		for ( int i = 0; i < Constant._kinds.length; ++i) {
			if ( Constant._kinds[ i].equals( kind))
				continue;

			if ( drawObjects.is_object_name( Constant._kinds[ i], name))
				return true;
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#update_role_name(java.lang.String, java.lang.String)
	 */
	public boolean update_role_name(String originalName, String name) {
		boolean result = false;
		if ( !is_multi()) {
			if ( _initialRole.equals( originalName)) {
				_initialRole = name;
				result = true;
			}
		}

		for ( String kind:Constant._kinds) {
			if ( update_role_name( _objectMapMap.get( kind), originalName, name))
				result = true;
		}

		return result;
	}

	/**
	 * @param treeMap
	 * @param originalName
	 * @param name
	 * @return
	 */
	private boolean update_role_name(TreeMap<String, Object> treeMap, String originalName, String name) {
		boolean result = false;
		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			if ( !is_multi()) {
				ObjectBase objectBase = ( ObjectBase)entry.getValue();
				if ( objectBase.update_role_name( originalName, name))
					result = true;
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				if ( update_role_name( originalName, name, indicesMap))
					result = true;
			}
		}
		return result;
	}

	/**
	 * @param originalName
	 * @param name
	 * @param indicesMap
	 * @return
	 */
	private boolean update_role_name(String originalName, String name, Map<ObjectBase, Vector<int[]>> indicesMap) {
		boolean result = false;
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			if ( objectBase.update_role_name( originalName, name))
				result = true;
		}
		return result;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#on_remove_role_name(java.util.Vector)
	 */
	public void on_remove_role_name(Vector<String> roleNames) {
		if ( !is_multi()) {
			if ( !roleNames.contains( _initialRole))
				_initialRole = "";
		}

		for ( String kind:Constant._kinds)
			on_remove_role_name( _objectMapMap.get( kind), roleNames);
	}

	/**
	 * @param treeMap
	 * @param roleNames
	 */
	private void on_remove_role_name(TreeMap<String, Object> treeMap, Vector<String> roleNames) {
		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			if ( !is_multi()) {
				ObjectBase objectBase = ( ObjectBase)entry.getValue();
				objectBase.on_remove_role_name( roleNames);
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				on_remove_role_name( roleNames, indicesMap);
			}
		}
	}

	/**
	 * @param roleNames
	 * @param indicesMap
	 */
	private void on_remove_role_name(Vector<String> roleNames, Map<ObjectBase, Vector<int[]>> indicesMap) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			objectBase.on_remove_role_name( roleNames);
		}
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#edit(javax.swing.JComponent, java.awt.Frame)
	 */
	public void edit(JComponent component, Frame frame) {
		String title;
		if ( this instanceof AgentObject)
			title = ResourceManager.get_instance().get( "edit.agent.dialog.title");
		else if ( this instanceof SpotObject)
			title = ResourceManager.get_instance().get( "edit.spot.dialog.title");
		else
			return;

		EditObjectDlg editObjectDlg = new EditObjectDlg( frame, title + " - " + _name, true, this);
		editObjectDlg.do_modal();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#get_initial_data()
	 */
	public String get_initial_data() {
		String entityName = get_entity();
		if ( null == entityName)
			return "";

		String script = "";

		script += get_number();

		for ( String kind:Constant._kinds)
			script += get_initial_data( _objectMapMap.get( kind), entityName + "\t" + _name + "\t");

		if ( !script.equals( ""))
			script += Constant._lineSeparator;

		return script;
	}

	/**
	 * @return
	 */
	private String get_number() {
		if ( _multi || _number.equals( ""))
			return "";

		if ( this instanceof AgentObject)
			return ( ResourceManager.get_instance().get( "initial.data.agent.number") + "\t" + _name + "\t" + _number + Constant._lineSeparator);
		else if ( this instanceof SpotObject)
			return ( ResourceManager.get_instance().get( "initial.data.spot.number") + "\t" + _name + "\t" + _number + Constant._lineSeparator);
		else
			return "";
	}

	/**
	 * @return
	 */
	private String get_entity() {
		if ( this instanceof AgentObject)
			return ResourceManager.get_instance().get( "initial.data.agent");
		else if ( this instanceof SpotObject)
			return ResourceManager.get_instance().get( "initial.data.spot");

		return null;
	}

	/**
	 * @param treeMap
	 * @param prefix
	 * @return
	 */
	private String get_initial_data(TreeMap<String, Object> treeMap, String prefix) {
		String script = "";
		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			if ( !is_multi()) {
				ObjectBase objectBase = ( ObjectBase)entry.getValue();
				script += ( prefix + objectBase.get_initial_data());
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				script += get_initial_data( prefix + "Number\t", indicesMap);
			}
		}
		return script;
	}

	/**
	 * @param prefix
	 * @param indicesMap
	 * @return
	 */
	private String get_initial_data(String prefix, Map<ObjectBase, Vector<int[]>> indicesMap) {
		String script = "";
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			Vector<int[]> indices = ( Vector<int[]>)entry.getValue();
			script += ( prefix + CommonTool.get_indices( indices, ":", "_") + "\t" + objectBase.get_initial_data());
		}
		return script;
	}

	/**
	 * Returns the ModelBuilder script.
	 * @param command the word counter
	 * @param initialValueMap the initial value hashtable
	 * @return the ModelBuilder script
	 */
	public String get_script(IntBuffer command, InitialValueMap initialValueMap/*, boolean grid*/) {
		if ( is_multi()) {
			String script = "";
			int number = Integer.parseInt( _number);
			for ( int i = 1; i <= number; ++i)
				script += get_script( i, command, initialValueMap);

			return script;
		}

		String script = ( _number + "\t" + _name);

		IntBuffer counter = IntBuffer.allocate( 1);
		counter.put( 0, 0);

		String text = on_get_script();
		if ( !text.equals( "")) {
			script += text;
			counter.put( 0, counter.get( 0) + 1);
		}

		if ( !_initialRole.equals( "")) {
			if ( this instanceof AgentObject) {
				Role role = LayerManager.get_instance().get_agent_role( _initialRole);
				if ( null != role)
					script += ( "\tactivateRole " + role.get_name());
			} else if ( this instanceof SpotObject) {
				Role role = LayerManager.get_instance().get_spot_role( _initialRole);
				if ( null != role)
					script += ( "\t<>startRule " + role.get_name());
			} else
				return "";

			counter.put( 0, counter.get( 0) + 1);
		}

		for ( String kind:Constant._kinds)
			script += get_script( kind, _objectMapMap.get( kind), counter, initialValueMap);

		String others = get_others_script();
		if ( !others.equals( "")) {
			script += ( "\t" + others);
			counter.put( 0, counter.get( 0) + 1);
		}

		script += Constant._lineSeparator;

		if ( command.get( 0) < counter.get( 0))
			command.put( 0, counter.get( 0));

		return script;
	}

	/**
	 * @param index
	 * @param command
	 * @param initialValueMap
	 * @return
	 */
	private String get_script(int index, IntBuffer command, InitialValueMap initialValueMap) {
		String script = ( "\t" + ( _name + index));

		IntBuffer counter = IntBuffer.allocate( 1);
		counter.put( 0, 0);

		for ( String kind:Constant._kinds)
			script += get_script( kind, _objectMapMap.get( kind), index, counter, initialValueMap);

		script += Constant._lineSeparator;

		if ( command.get( 0) < counter.get( 0))
			command.put( 0, counter.get( 0));

		return script;
	}

	/**
	 * @param kind
	 * @param treeMap
	 * @param index
	 * @param counter
	 * @param initialValueMap
	 * @return
	 */
	private String get_script(String kind, TreeMap<String, Object> treeMap, int index, IntBuffer counter, InitialValueMap initialValueMap) {
		String script = "";

		if ( kind.equals( "spot variable")) {
			script += "\t<>setSpot " + Constant._spotVariableName;
			counter.put( 0, counter.get( 0) + 1);
		}

		if ( kind.equals( "class variable") && !Environment.get_instance().is_functional_object_enable())
			return "";

		if ( kind.equals( "exchange algebra")) {
			if ( !Environment.get_instance().is_exchange_algebra_enable())
				return "";

			if ( _objectMapMap.get( "exchange algebra").isEmpty())
				return "";

			script += ( "\t" + get_prefix() + "setClass " + Constant._exchangeAlgebraFactoryClassVariableName + "=" + Constant._exchangeAlgebraFactoryClassname);
			//script += ( " ; " + get_prefix() + "logEquip " + Constant._exchangeAlgebraFactoryClassVariableName);
			counter.put( 0, counter.get( 0) + 1);

			script += ( "\t" + get_prefix() + "setClass " + Constant._exchangeAlgebraMathClassVariableName + "=" + Constant._exchangeAlgebraMathClassname);
			//script += ( " ; " + get_prefix() + "logEquip " + Constant._exchangeAlgebraMathClassVariableName);
			counter.put( 0, counter.get( 0) + 1);

			script += ( "\t" + get_prefix() + "setClass " + Constant._exchangeAlgebraUtilityClassVariableName + "=" + Constant._exchangeAlgebraUtilityClassname);
			//script += ( " ; " + get_prefix() + "logEquip " + Constant._exchangeAlgebraMathClassVariableName);
			counter.put( 0, counter.get( 0) + 1);
		}

		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
			script += get_script( index, indicesMap, counter, initialValueMap);
		}

		return script;
	}

	/**
	 * @param index
	 * @param indicesMap
	 * @param counter
	 * @param initialValueMap
	 * @return
	 */
	private String get_script(int index, Map<ObjectBase, Vector<int[]>> indicesMap, IntBuffer counter, InitialValueMap initialValueMap) {
		String script = "";
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			Vector<int[]> indices = ( Vector<int[]>)entry.getValue();
			if ( !contains( indices, index))
				continue;

			script += objectBase.get_script( get_prefix(), counter, initialValueMap);
		}
		return script;
	}

	/**
	 * @return
	 */
	protected String on_get_script() {
		return "";
	}

	/**
	 * @param kind
	 * @param treeMap
	 * @param counter
	 * @param initialValueMap
	 */
	private String get_script(String kind, TreeMap<String, Object> treeMap, IntBuffer counter, InitialValueMap initialValueMap) {
		String script = "";

		if ( kind.equals( "spot variable")) {
			script += "\t<>setSpot " + Constant._spotVariableName;
			counter.put( 0, counter.get( 0) + 1);
		}

		if ( kind.equals( "class variable") && !Environment.get_instance().is_functional_object_enable())
			return "";

		if ( kind.equals( "exchange algebra")) {
			if ( !Environment.get_instance().is_exchange_algebra_enable())
				return "";

			if ( treeMap.isEmpty())
				return "";

			script += ( "\t" + get_prefix() + "setClass " + Constant._exchangeAlgebraFactoryClassVariableName + "=" + Constant._exchangeAlgebraFactoryClassname);
			//script += ( " ; " + get_prefix() + "logEquip " + Constant._exchangeAlgebraFactoryClassVariableName);
			counter.put( 0, counter.get( 0) + 1);

			script += ( "\t" + get_prefix() + "setClass " + Constant._exchangeAlgebraMathClassVariableName + "=" + Constant._exchangeAlgebraMathClassname);
			//script += ( " ; " + get_prefix() + "logEquip " + Constant._exchangeAlgebraMathClassVariableName);
			counter.put( 0, counter.get( 0) + 1);

			script += ( "\t" + get_prefix() + "setClass " + Constant._exchangeAlgebraUtilityClassVariableName + "=" + Constant._exchangeAlgebraUtilityClassname);
			//script += ( " ; " + get_prefix() + "logEquip " + Constant._exchangeAlgebraUtilityClassVariableName);
			counter.put( 0, counter.get( 0) + 1);
		}

		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getValue();
			script += objectBase.get_script( get_prefix(), counter, initialValueMap);
		}

		return script;
	}

	/**
	 * @return
	 */
	private String get_others_script() {
		if ( _others.equals( ""))
			return "";

		String script = "";
		String others[] = _others.split( Constant._lineSeparator);
		for ( int i = 0; i < others.length; ++i) {
			if ( others[ i].equals( "") || others[ i].matches( "[ ]+"))
				continue;

			script += ( ( script.equals( "") ? "" : " ; ") + others[ i].trim());
		}

		return script;
	}

	/**
	 * Returns the ModelBuilder script.
	 * @param command the word counter
	 * @param initialValueMap the initial value hashtable
	 * @return the ModelBuilder script
	 */
	public String get_variable_initial_values_script(IntBuffer command, InitialValueMap initialValueMap) {
		command.put( 0, 1);

		if ( is_multi()) {
			String script = "";
			int number = Integer.parseInt( _number);
			for ( int i = 1; i <= number; ++i)
				script += get_variable_initial_values_script( i, initialValueMap);

			return script;
		}

		String script = "";

		for ( String kind:Constant._kinds) {
			String text = get_variable_initial_values_script( kind, _objectMapMap.get( kind), initialValueMap);
			if ( !text.equals( ""))
				script += ( ( script.equals( "") ? "" : " ; ") + text);
		}

		if ( script.equals( ""))
			return "";

		script = ( _number + "\t" + _name + "\t" + script);

		script += Constant._lineSeparator;

		return script;
	}

	/**
	 * @param index
	 * @param initialValueMap
	 * @return
	 */
	private String get_variable_initial_values_script(int index, InitialValueMap initialValueMap) {
		String script = "";

		for ( String kind:Constant._kinds) {
			String text = get_variable_initial_values_script( kind, _objectMapMap.get( kind), index, initialValueMap);
			if ( !text.equals( ""))
				script += ( ( script.equals( "") ? "" : " ; ") + text);
		}

		if ( script.equals( ""))
			return "";

		script = ( "\t" + ( _name + index) + "\t" + script);
		
		script += Constant._lineSeparator;
	
		return script;
	}

	/**
	 * @param kind
	 * @param treeMap
	 * @param index
	 * @param initialValueMap
	 * @return
	 */
	private String get_variable_initial_values_script(String kind, TreeMap<String, Object> treeMap, int index, InitialValueMap initialValueMap) {
		String script = "";
		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
			String text = get_variable_initial_values_script( index, indicesMap, initialValueMap);
			if ( text.equals( ""))
				continue;

			script += ( ( script.equals( "") ? "" : " ; ") + text);
		}
		return script;
	}

	/**
	 * @param index
	 * @param indicesMap
	 * @param initialValueMap
	 * @return
	 */
	private String get_variable_initial_values_script(int index, Map<ObjectBase, Vector<int[]>> indicesMap, InitialValueMap initialValueMap) {
		String script = "";
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Vector<int[]> indices = ( Vector<int[]>)entry.getValue();
			if ( !contains( indices, index))
				continue;

			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			String text = objectBase.get_initial_values_script( get_prefix(), initialValueMap);
			if ( text.equals( ""))
				continue;

			script += ( ( script.equals( "") ? "" : " ; ") + text);
		}
		return script;
	}

	/**
	 * @param kind
	 * @param treeMap
	 * @param initialValueMap
	 * @return
	 */
	private String get_variable_initial_values_script(String kind, TreeMap<String, Object> treeMap, InitialValueMap initialValueMap) {
		String script = "";
		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getValue();
			String text = objectBase.get_initial_values_script( get_prefix(), initialValueMap);
			if ( text.equals( ""))
				continue;

			script += ( ( script.equals( "") ? "" : " ; ") + text);
		}
		return script;
	}

	/**
	 * @return
	 */
	private String get_prefix() {
		return ( ( this instanceof SpotObject) ? "<>" : "");
	}

	/**
	 * @param indices
	 * @param index
	 * @return
	 */
	private boolean contains(Vector<int[]> indices, int index) {
		for ( int[] range:indices) {
			if ( range[ 0] <= index && index <= range[ 1])
				return true;
		}
		return false;
	}

	/**
	 * Appends the specified object.
	 * @param objectBase the specified object
	 */
	public void append_object(ObjectBase objectBase) {
		if ( null != _objectMapMap.get( objectBase._kind))
			_objectMapMap.get( objectBase._kind).put( objectBase._name, objectBase);
		else if ( null != _objectListMap.get( objectBase._kind))
			_objectListMap.get( objectBase._kind).add( objectBase);
	}

	/**
	 * Appends the specified object.
	 * @param objectBase the specified object
	 * @param number the specified number
	 */
	public void append_object(ObjectBase objectBase, String number) {
		if ( null == _objectMapMap.get( objectBase._kind))
			return;

		_multi = true;

		Vector<int[]> indices = CommonTool.get_indices( number, ",", "-");
		undate_number( indices);

		EntityData.append_object( _objectMapMap.get( objectBase._kind), objectBase, indices);
	}

	/**
	 * Appends the specified object.
	 * @param objectBase the specified object
	 */
	public void remove_object(ObjectBase objectBase) {
		if ( null != _objectMapMap.get( objectBase._kind))
			_objectMapMap.get( objectBase._kind).remove( objectBase._name);
		else if ( null != _objectListMap.get( objectBase._kind)) {
			List<ObjectBase> objectBases = _objectListMap.get( objectBase._kind);
			for ( int i = 0; i < objectBases.size(); ++i) {
				if ( objectBases.get( i)._name.equals( objectBase._name)) {
					objectBases.remove( i);
					break;
				}
			}
		}
	}

	/**
	 * @param indices
	 */
	private void undate_number(Vector<int[]> indices) {
		int max = CommonTool.get_max( indices);
		if ( _number.equals( ""))
			_number = String.valueOf( max);
		else {
			int number = Integer.parseInt( _number);
			if ( number < max)
				_number = String.valueOf( max);
		}
	}

	/**
	 * Returns true for setting the comment.
	 * @param kind the object kind
	 * @param name the object name
	 * @param comment the comment
	 * @return true for setting the comment
	 */
	public boolean set_comment(String kind, String name, String comment) {
		if ( is_multi())
			return false;

		if ( null != _objectMapMap.get( kind)) {
			TreeMap<String, Object> treeMap = _objectMapMap.get( kind);
			if ( null == treeMap)
				return false;

			return update_object_comment( treeMap, name, comment);
		} else if ( null != _objectListMap.get( kind)) {
			List<ObjectBase> objectBases = _objectListMap.get( kind);
			if ( null == objectBases)
				return false;

			return update_object_comment( objectBases, name, comment);
		}

		return false;
	}

	/**
	 * @param treeMap
	 * @param name
	 * @param comment
	 * @return
	 */
	private boolean update_object_comment(TreeMap<String, Object> treeMap, String name, String comment) {
		ObjectBase objectBase = ( ObjectBase)treeMap.get( name);
		if ( null == objectBase)
			return false;

		objectBase._comment = comment;

		return true;
	}

	/**
	 * @param objectBases
	 * @param name
	 * @param comment
	 * @return
	 */
	private boolean update_object_comment(List<ObjectBase> objectBases, String name, String comment) {
		for ( ObjectBase objectBase:objectBases) {
			if ( null != objectBase && objectBase._name.equals( name)) {
				objectBase._comment = comment;
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true for setting the comment.
	 * @param kind the object kind
	 * @param name the object name
	 * @param initial_value_object the initial value
	 * @param comment the comment
	 * @return true for setting the comment
	 */
	public boolean set_comment(String kind, String name, ObjectBase objectBase, String comment) {
		if ( !is_multi())
			return false;

		if ( null == _objectMapMap.get( kind))
			return false;

		if ( !objectBase._kind.equals( kind))
			return false;

		TreeMap<String, Object> treeMap = _objectMapMap.get( kind);
		if ( null == treeMap)
			return false;

		return update_object_comment( treeMap, name, objectBase, comment);
	}

	/**
	 * @param treeMap
	 * @param name
	 * @param objectBase
	 * @param comment
	 * @return
	 */
	private boolean update_object_comment(TreeMap<String, Object> treeMap, String name, ObjectBase objectBase, String comment) {
		Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)treeMap.get( name);
		if ( null == indicesMap)
			return false;

		objectBase.update_comment( indicesMap, comment);
		return true;
	}

	/**
	 * Returns true for writing this object data successfully.
	 * @param name this object type("agent" or "spot")
	 * @param writer the abstract class for writing to character streams
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(String name, Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		write( _name, attributesImpl);
		attributesImpl.addAttribute( null, null, "image", "", Writer.escapeAttributeCharData( _imageFilename));
		attributesImpl.addAttribute( null, null, "gis", "", Writer.escapeAttributeCharData( _gis));
		attributesImpl.addAttribute( null, null, "gis_coordinates_x", "", Writer.escapeAttributeCharData( _gisCoordinates[ 0]));
		attributesImpl.addAttribute( null, null, "gis_coordinates_y", "", Writer.escapeAttributeCharData( _gisCoordinates[ 1]));

		if ( !is_multi()) {
			attributesImpl.addAttribute( null, null, "number", "", String.valueOf( _number));
			attributesImpl.addAttribute( null, null, "initial_role", "", Writer.escapeAttributeCharData( _initialRole));
			on_write( attributesImpl);
			return write( name, attributesImpl, writer);
		} else {
			if ( is_empty()) {
				writer.writeElement( null, null, name, attributesImpl);
				return true;
			}

			writer.startElement( null, null, name, attributesImpl);

			writer.startElement( null, null, "initial_data", new AttributesImpl());

			for ( String kind:Constant._kinds)
				write( kind, _objectMapMap.get( kind), writer);

			writer.endElement( null, null, "initial_data");

			write_others( writer);
			write_comment( writer);

			writer.endElement( null, null, name);

			return true;
		}
	}

	/**
	 * @param name
	 * @param attributesImpl
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write(String name, AttributesImpl attributesImpl, Writer writer) throws SAXException {
		if ( is_empty()) {
			writer.startElement( null, null, name, attributesImpl);
			write_others( writer);
			write_comment( writer);
			writer.endElement( null, null, name);
			return true;
		}

		writer.startElement( null, null, name, attributesImpl);

		for ( String kind:Constant._kinds)
			write( kind, _objectMapMap.get( kind), writer);

		for ( String kind:Constant._exceptionalKinds)
			write( kind, _objectListMap.get( kind), writer);

		write_others( writer);
		write_comment( writer);

		writer.endElement( null, null, name);
		return true;
	}

	/**
	 * @return
	 */
	private boolean is_empty() {
		for ( String kind:Constant._kinds) {
			if ( !_objectMapMap.get( kind).isEmpty())
				return false;
		}
		for ( String kind:Constant._exceptionalKinds) {
			if ( !_objectListMap.get( kind).isEmpty())
				return false;
		}
		return true;
	}

	/**
	 * @param writer
	 * @throws SAXException
	 */
	private void write_others(Writer writer) throws SAXException {
		if ( _others.equals( ""))
			return;

		writer.startElement( null, null, "others", new AttributesImpl());
		writer.characters( _others.toCharArray(), 0, _others.length());
		writer.endElement( null, null, "others");
	}

	/**
	 * @param attributesImpl
	 */
	protected void on_write(AttributesImpl attributesImpl) {
	}

	/**
	 * @param kind
	 * @param treeMap
	 * @param writer
	 * @throws SAXException
	 */
	private void write(String kind, TreeMap<String, Object> treeMap, Writer writer) throws SAXException {
		if ( treeMap.isEmpty())
			return;

		if ( !is_multi()) {
			writer.startElement( null, null, SaxLoader._kindTagMap.get( kind), new AttributesImpl());

			Iterator iterator = treeMap.entrySet().iterator();
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				ObjectBase objectBase = ( ObjectBase)entry.getValue();
				objectBase.write( writer);
			}

			writer.endElement( null, null, SaxLoader._kindTagMap.get( kind));
		} else {
			Iterator iterator = treeMap.entrySet().iterator();
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)entry.getValue();
				write( indicesMap, writer);
			}
		}
	}

	/**
	 * @param indicesMap
	 * @param writer
	 * @throws SAXException
	 */
	private void write(Map<ObjectBase, Vector<int[]>> indicesMap, Writer writer) throws SAXException {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			Vector<int[]> indices = ( Vector<int[]>)entry.getValue();
			String number = CommonTool.get_indices( indices, ",", "-");
			objectBase.write( writer, number);
		}
	}

	/**
	 * @param kind
	 * @param list
	 * @param writer
	 * @throws SAXException
	 */
	private void write(String kind, List<ObjectBase> list, Writer writer) throws SAXException {
		if ( list.isEmpty())
			return;

		if ( is_multi())
			return;

		writer.startElement( null, null, SaxLoader._kindTagMap.get( kind), new AttributesImpl());

		for ( ObjectBase objectBase:list)
			objectBase.write( writer);

		writer.endElement( null, null, SaxLoader._kindTagMap.get( kind));
	}

	/**
	 * Gets the jar file names.
	 * @param list the list for the jar file names
	 */
	public void get_jar_filenames(List<String> list) {
		Iterator iterator = _objectMapMap.get( "class variable").entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			if ( !is_multi()) {
				ClassVariableObject classVariableObject = ( ClassVariableObject)entry.getValue();
				if ( classVariableObject._jarFilename.equals( Constant._javaClasses))
					continue;

				if ( !list.contains( classVariableObject._jarFilename))
					list.add( classVariableObject._jarFilename);
			} else {
				Map<ObjectBase, Vector<int[]>> indicesMap = ( Map<ObjectBase, Vector<int[]>>)_objectMapMap.get( "class variable").get( name);
				if ( null == indicesMap)
					continue;

				Vector<ObjectBase> keys = new Vector<ObjectBase>( indicesMap.keySet());
				if ( keys.isEmpty())
					continue;

				get_jar_filenames( list, keys);
			}
		}
	}

	/**
	 * @param list
	 * @param keys
	 */
	private void get_jar_filenames(List<String> list, Vector<ObjectBase> keys) {
		for ( int i = 0; i < keys.size(); ++i) {
			ClassVariableObject classVariableObject = ( ClassVariableObject)keys.get( i);
			if ( null == classVariableObject)
				continue;

			if ( !list.contains( classVariableObject._jarFilename))
				list.add( classVariableObject._jarFilename);
		}
	}

	/**
	 * @param graphics2D
	 * @param imageObserver
	 * @return
	 */
	protected boolean draw_image(Graphics2D graphics2D, ImageObserver imageObserver) {
		if ( _imageFilename.equals( ""))
			return false;

		BufferedImage bufferedImage = VisualShellImageManager.get_instance().load_image( _imageFilename);
		if ( null == bufferedImage)
			return false;

		graphics2D.drawImage( bufferedImage, _position.x, _position.y, imageObserver);

		return true;
}

	/**
	 * @param imageFilename
	 * @return
	 */
	public boolean uses_this_image(String imageFilename) {
		return _imageFilename.equals( imageFilename);
	}

	/**
	 * 
	 */
	public void update_image() {
		if ( _imageFilename.equals( ""))
			return;

		ImageProperty imageProperty = ImagePropertyManager.get_instance().get( _imageFilename);
		if ( null == imageProperty)
			return;

		_dimension = new Dimension( imageProperty._width, imageProperty._height);
		_textColor = get_default_image_color();
	}

	/**
	 * @param newImageFilename
	 */
	public void update_image(String newImageFilename) {
		if ( _imageFilename.equals( newImageFilename))
			return;

		if ( newImageFilename.equals( "")) {
			_dimension = get_default_dimension();
			_textColor = get_default_text_color();
			_imageFilename = newImageFilename;
		} else {
			ImageProperty imageProperty = ImagePropertyManager.get_instance().get( newImageFilename);
			if ( null != imageProperty) {
				_dimension = new Dimension( imageProperty._width, imageProperty._height);
				_textColor = get_default_image_color();
				_imageFilename = newImageFilename;
			}
		}
	}

	/**
	 * @param originalImageFilename
	 * @param newImageFilename
	 */
	public void update_image(String originalImageFilename, String newImageFilename) {
		if ( !_imageFilename.equals( originalImageFilename))
			return;

		ImageProperty imageProperty = ImagePropertyManager.get_instance().get( newImageFilename);
		if ( null != imageProperty) {
			_dimension = new Dimension( imageProperty._width, imageProperty._height);
			_textColor = get_default_image_color();
			_imageFilename = newImageFilename;
		}
	}

	/**
	 * @return
	 */
	public boolean transform_map_objects() {
		Iterator iterator = _objectMapMap.get( "map").entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			if ( !is_multi()) {
				MapObject mapObject = ( MapObject)entry.getValue();
				if ( !mapObject.transform( this))
					return false;
			} else {
				if ( !transform_map_objects( ( HashMap<ObjectBase, Vector<int[]>>)entry.getValue()))
					return false;
			}
		}
		return true;
	}

	/**
	 * @param indicesMap
	 */
	private boolean transform_map_objects(HashMap<ObjectBase, Vector<int[]>> indicesMap) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			MapObject mapObject = ( MapObject)entry.getKey();
			if ( !mapObject.transform( this))
				return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	public boolean is_initial_data_file_correct() {
		if ( !is_multi()) {
			List<ObjectBase> objectBases = _objectListMap.get( "initial data file");
			for ( ObjectBase objectBase:objectBases) {
				InitialDataFileObject initialDataFileObject = ( InitialDataFileObject)objectBase;
				if ( !initialDataFileObject.is_correct())
					return false;
			}
		}
		return true;
	}

	/**
	 * @param name
	 * @param number
	 * @return
	 */
	public boolean contains(String name, String number) {
		for ( String kind:Constant._kinds) {
			if ( contains( _objectMapMap.get( kind), name, number))
				return true;
		}
		return false;
	}

	/**
	 * @param treeMap
	 * @param name
	 * @param number
	 * @return
	 */
	private boolean contains(TreeMap<String, Object> treeMap, String name, String number) {
		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			if ( !is_multi()) {
				ObjectBase objectBase = ( ObjectBase)entry.getValue();
				if ( SoarsCommonTool.has_same_name( name, number, objectBase._name))
					return true;
			} else {
				if ( contains( ( HashMap<ObjectBase, Vector<int[]>>)entry.getValue(), name, number))
					return true;
			}
		}
		return false;
	}

	/**
	 * @param indicesMap
	 * @param name
	 * @param number
	 * @return
	 */
	private boolean contains(HashMap<ObjectBase, Vector<int[]>> indicesMap, String name, String number) {
		Iterator iterator = indicesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ObjectBase objectBase = ( ObjectBase)entry.getKey();
			if ( SoarsCommonTool.has_same_name( name, number, objectBase._name))
				return true;
		}
		return false;
	}

	/**
	 * @param objectBase
	 * @return
	 */
	public boolean contains_as_initial_value(ObjectBase objectBase) {
		for ( String kind:Constant._kinds) {
			if ( !kind.equals( "collection") && !kind.equals( "list") && !kind.equals( "map") && !kind.equals( "exchange algebra"))
				continue;

			TreeMap<String, Object> objectMap = _objectMapMap.get( kind);
			if ( null == objectMap)
				continue;

			Iterator iterator = objectMap.entrySet().iterator();
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				String name = ( String)entry.getKey();
				Object value = entry.getValue();
				if ( !( value instanceof ObjectBase))
					continue;

				ObjectBase ob = ( ObjectBase)value;
				if ( ob.contains( objectBase)) {
					String[] message = new String[] {
						this instanceof SpotObject ? "Spot" : "Agent",
						"name = " + _name,
						"variable type = " + kind,
						"variable name = " + ob._name,
						"contained variable type = " + objectBase._kind,
						"contained variable name = " + objectBase._name,
					};
					if ( !WarningManager.get_instance().has( message))
						WarningManager.get_instance().add( message);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param entityBase
	 */
	public void update_on_gis(EntityBase entityBase) {
		if ( this == entityBase)
			return;

		_comment = entityBase._comment;
		_others = entityBase._others;
	}

	/**
	 * @param range
	 */
	public void get_gis_range(double[] range) {
		// TODO Auto-generated method stub
		if ( _gisCoordinates[ 0].equals( "") || _gisCoordinates[ 1].equals( ""))
			return;

		range[ 0] = Math.min( range[ 0], Double.parseDouble( _gisCoordinates[ 0]));
		range[ 1] = Math.min( range[ 1], Double.parseDouble( _gisCoordinates[ 1]));
		range[ 2] = Math.max( range[ 2], Double.parseDouble( _gisCoordinates[ 0]));
		range[ 3] = Math.max( range[ 3], Double.parseDouble( _gisCoordinates[ 1]));
	}

	/**
	 * @param range
	 * @param ratio
	 */
	public void update_gis_coordinates(double[] range, double[] ratio) {
		// TODO Auto-generated method stub
		if ( _gisCoordinates[ 0].equals( "") || _gisCoordinates[ 1].equals( ""))
			return;

		_position.x = ( int)( ( Double.parseDouble( _gisCoordinates[ 0]) - range[ 0]) * ratio[ 0]);
		_position.y = ( int)( ( range[ 3] - Double.parseDouble( _gisCoordinates[ 1])) * ratio[ 1]);
	}

	/**
	 * For debug.
	 */
	public void print() {
		System.out.println( _name);
		System.out.println( _number);

		for ( String kind:Constant._kinds)
			EntityData.print_object( _objectMapMap.get( kind), is_multi());
	}
}
