/**
 * 
 */
package soars.application.visualshell.file.importer.initial.role;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import soars.application.visualshell.file.importer.initial.stage.StageDataMap;
import soars.application.visualshell.layer.LayerManager;

/**
 * The RoleData hashtable(name[String] - RoleData).
 * @author kurata / SOARS project
 */
public class RoleDataMap extends HashMap<String, RoleData> {

	/**
	 * 
	 */
	private String _type = "";

	/**
	 * Creates this object with the specified data.
	 * @param type the type of this object("agent_role" or "spot_role")
	 */
	public RoleDataMap(String type) {
		super();
		_type = type;
	}

	/**
	 * Returns true if the specified role name is correct.
	 * @param name the specified role name
	 * @return true if the specified role name is correct
	 */
	public boolean is_correct(String name) {
		if ( 0 <= name.indexOf( '$'))
			return false;
//		if ( name.equals( Constant._spot_chart_role_name)
//			|| 0 <= name.indexOf( '$'))
//			return false;

		if ( _type.equals( "spot_role")) {
			if ( name.equals( ""))
				return false;
		}

		if ( LayerManager.get_instance().chartObject_has_same_name( name, ""))
			return false;

		return true;
	}

	/**
	 * Returns true if the specified name is in use for a same type role or a chart object.
	 * @param name the specified neme
	 * @return true if the specified name is in use for a same type role or a chart object
	 */
	public boolean contains(String name) {
		if ( null != get( name))
			return true;

		if ( LayerManager.get_instance().contains_this_role_name( _type, name)
			|| LayerManager.get_instance().chartObject_has_same_name( name, ""))
			return true;

		return false;
	}

	/**
	 * Returns true if the loaded data are valid.
	 * @param stageDataMaps the stage data hashtables
	 * @return true if the loaded data are valid
	 */
	public boolean verify(StageDataMap[] stageDataMaps) {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			RoleData roleData = ( RoleData)entry.getValue();
			if ( !roleData.verify( stageDataMaps))
				return false;
		}
		return true;
	}
}
