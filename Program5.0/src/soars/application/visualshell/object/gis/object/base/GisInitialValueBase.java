/**
 * 
 */
package soars.application.visualshell.object.gis.object.base;

import java.util.HashMap;
import java.util.Map;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.object.map.GisMapInitialValue;
import soars.application.visualshell.object.gis.object.variable.GisVariableInitialValue;


/**
 * @author kurata
 *
 */
public class GisInitialValueBase {

	/**
	 * 
	 */
	public int _row = 0;	// for copy, cut and paste only.

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	* 
	*/
	static public Map<String, String> __typeMap = null;
	
	/**
	* 
	*/
	static public Map<String, String> __nameMap = null;

	/**
	* 
	*/
	static {
		startup();
	}

	/**
	* 
	*/
	private static void startup() {
		synchronized( _lock) {
			create__typeMap();
			create__nameMap();
		}
	}

	/**
	* 
	*/
	private static void create__typeMap() {
		if ( null != __typeMap)
			return;

		__typeMap = new HashMap<String, String>();
		__typeMap.put( ResourceManager.get_instance().get( "edit.gis.data.initial.value.field.type.string"), "string");
		__typeMap.put( ResourceManager.get_instance().get( "edit.gis.data.initial.value.field.type.integer"), "integer");
		__typeMap.put( ResourceManager.get_instance().get( "edit.gis.data.initial.value.field.type.real.number"), "real number");
	}

	/**
	* 
	*/
	private static void create__nameMap() {
		if ( null != __nameMap)
			return;

		__nameMap = new HashMap<String, String>();
		__nameMap.put( "string", ResourceManager.get_instance().get( "edit.gis.data.initial.value.field.type.string"));
		__nameMap.put( "integer", ResourceManager.get_instance().get( "edit.gis.data.initial.value.field.type.integer"));
		__nameMap.put( "real number", ResourceManager.get_instance().get( "edit.gis.data.initial.value.field.type.real.number"));
	}

	/**
	 * 
	 */
	public GisInitialValueBase() {
		super();
	}

	/**
	 * @param row
	 */
	public GisInitialValueBase(int row) {
		super();
		_row = row;
	}

	/**
	 * @param gisInitialValueBase
	 * @return
	 */
	public static GisInitialValueBase create(GisInitialValueBase gisInitialValueBase) {
		if ( gisInitialValueBase instanceof GisVariableInitialValue)
			return new GisVariableInitialValue( ( GisVariableInitialValue)gisInitialValueBase);
		else if ( gisInitialValueBase instanceof GisMapInitialValue)
			return new GisMapInitialValue( ( GisMapInitialValue)gisInitialValueBase);
//		else if ( gisInitialValueBase instanceof GisExchangeAlgebraInitialValue)
//			return new GisExchangeAlgebraInitialValue( ( GisExchangeAlgebraInitialValue)gisInitialValueBase);
		else
			return null;
	}

	/**
	 * @param gisInitialValueBase
	 * @param row
	 * @return
	 */
	public static GisInitialValueBase create(GisInitialValueBase gisInitialValueBase, int row) {
		if ( gisInitialValueBase instanceof GisVariableInitialValue)
			return new GisVariableInitialValue( ( GisVariableInitialValue)gisInitialValueBase, row);
		else if ( gisInitialValueBase instanceof GisMapInitialValue)
			return new GisMapInitialValue( ( GisMapInitialValue)gisInitialValueBase, row);
//		else if ( gisInitialValueBase instanceof GisExchangeAlgebraInitialValue)
//			return new GisExchangeAlgebraInitialValue( ( GisExchangeAlgebraInitialValue)gisInitialValueBase, row);
		else
			return null;
	}
}
