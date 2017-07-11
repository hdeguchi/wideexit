/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.table.data;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.object.base.Rule;

/**
 * @author kurata
 *
 */
public class CommonRuleData {

	/**
	 * 
	 */
	static public HashMap _conditionNameMap = null;

	/**
	 * 
	 */
	static public HashMap _commandNameMap = null;

	/**
	 * 
	 */
	static public HashMap _conditionColorMap = null;

	/**
	 * 
	 */
	static public HashMap _commandColorMap = null;

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
		create_name_map();
		create_color_map();
	}

	/**
	 * 
	 */
	private static void create_name_map() {
		_conditionNameMap = new HashMap();

		_conditionNameMap.put( ResourceManager.get_instance().get( "rule.type.condition.stage"),
			ResourceManager.get_instance().get( "rule.type.condition.stage.name"));
		_conditionNameMap.put( ResourceManager.get_instance().get( "rule.type.condition.spot"),
			ResourceManager.get_instance().get( "rule.type.condition.spot.name0"));
		_conditionNameMap.put( ResourceManager.get_instance().get( "rule.type.condition.keyword"),
			ResourceManager.get_instance().get( "rule.type.condition.keyword.name"));
		_conditionNameMap.put( ResourceManager.get_instance().get( "rule.type.condition.time"),
			ResourceManager.get_instance().get( "rule.type.condition.time.name"));
		_conditionNameMap.put( ResourceManager.get_instance().get( "rule.type.condition.role"),
			ResourceManager.get_instance().get( "rule.type.condition.role.name"));
		_conditionNameMap.put( ResourceManager.get_instance().get( "rule.type.condition.probability"),
			ResourceManager.get_instance().get( "rule.type.condition.probability.name"));
		_conditionNameMap.put( ResourceManager.get_instance().get( "rule.type.condition.agent.name"),
			ResourceManager.get_instance().get( "rule.type.condition.agent.name.name"));
		_conditionNameMap.put( ResourceManager.get_instance().get( "rule.type.condition.spot.name"),
			ResourceManager.get_instance().get( "rule.type.condition.spot.name.name"));
		_conditionNameMap.put( ResourceManager.get_instance().get( "rule.type.condition.number.object"),
			ResourceManager.get_instance().get( "rule.type.condition.number.object.name"));
		_conditionNameMap.put( ResourceManager.get_instance().get( "rule.type.condition.collection"),
			ResourceManager.get_instance().get( "rule.type.condition.collection.name"));
		_conditionNameMap.put( ResourceManager.get_instance().get( "rule.type.condition.list"),
			ResourceManager.get_instance().get( "rule.type.condition.list.name"));
		_conditionNameMap.put( ResourceManager.get_instance().get( "rule.type.condition.functional.object"),
			ResourceManager.get_instance().get( "rule.type.condition.functional.object.name"));
		_conditionNameMap.put( ResourceManager.get_instance().get( "rule.type.condition.others"),
			ResourceManager.get_instance().get( "rule.type.condition.others.name"));

		_commandNameMap = new HashMap();

		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.keyword"),
			ResourceManager.get_instance().get( "rule.type.command.keyword.name"));
		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.move"),
			ResourceManager.get_instance().get( "rule.type.command.move.name"));
		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.spot.variable"),
			ResourceManager.get_instance().get( "rule.type.command.spot.variable.name"));
		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.time"),
			ResourceManager.get_instance().get( "rule.type.command.time.name"));
		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.role"),
			ResourceManager.get_instance().get( "rule.type.command.role.name"));
		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.set.role"),
			ResourceManager.get_instance().get( "rule.type.command.set.role.name"));
		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.substitution"),
			ResourceManager.get_instance().get( "rule.type.command.substitution.name"));
		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.collection"),
			ResourceManager.get_instance().get( "rule.type.command.collection.name"));
		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.list"),
			ResourceManager.get_instance().get( "rule.type.command.list.name"));
		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.map"),
			ResourceManager.get_instance().get( "rule.type.command.map.name"));
		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.create.agent"),
			ResourceManager.get_instance().get( "rule.type.command.create.agent.name"));
		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.create.object"),
			ResourceManager.get_instance().get( "rule.type.command.create.object.name"));
		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.get.equip"),
			ResourceManager.get_instance().get( "rule.type.command.get.equip.name"));
		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.put.equip"),
			ResourceManager.get_instance().get( "rule.type.command.put.equip.name"));
		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.attach"),
			ResourceManager.get_instance().get( "rule.type.command.attach.name"));
		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.detach"),
			ResourceManager.get_instance().get( "rule.type.command.detach.name"));
		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.functional.object"),
			ResourceManager.get_instance().get( "rule.type.command.functional.object.name"));
		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"),
			ResourceManager.get_instance().get( "rule.type.command.exchange.algebra.name"));
		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.extransfer"),
			ResourceManager.get_instance().get( "rule.type.command.extransfer.name"));
		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.next.stage"),
			ResourceManager.get_instance().get( "rule.type.command.next.stage.name"));
		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.terminate"),
			ResourceManager.get_instance().get( "rule.type.command.terminate.name"));
		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.trace"),
			ResourceManager.get_instance().get( "rule.type.command.trace.name"));
		_commandNameMap.put( ResourceManager.get_instance().get( "rule.type.command.others"),
			ResourceManager.get_instance().get( "rule.type.command.others.name"));
	}

	/**
	 * 
	 */
	private static void create_color_map() {
		_conditionColorMap = new HashMap();

		_conditionColorMap.put( ResourceManager.get_instance().get( "rule.type.condition.stage"),
			new Color( 0, 0, 200));
		_conditionColorMap.put( ResourceManager.get_instance().get( "rule.type.condition.spot"),
			new Color( 240, 0, 0));
		_conditionColorMap.put( ResourceManager.get_instance().get( "rule.type.condition.keyword"),
			new Color( 0, 128, 128));
		_conditionColorMap.put( ResourceManager.get_instance().get( "rule.type.condition.time"),
			new Color( 128, 0, 0));
		_conditionColorMap.put( ResourceManager.get_instance().get( "rule.type.condition.role"),
			new Color( 0, 64, 128));
		_conditionColorMap.put( ResourceManager.get_instance().get( "rule.type.condition.probability"),
			new Color( 200, 0, 200));
		_conditionColorMap.put( ResourceManager.get_instance().get( "rule.type.condition.agent.name"),
			new Color( 128, 0, 128));
		_conditionColorMap.put( ResourceManager.get_instance().get( "rule.type.condition.spot.name"),
			new Color( 128, 0, 128));
		_conditionColorMap.put( ResourceManager.get_instance().get( "rule.type.condition.number.object"),
			new Color( 0, 0, 128));
		_conditionColorMap.put( ResourceManager.get_instance().get( "rule.type.condition.collection"),
			new Color( 128, 128, 0));
		_conditionColorMap.put( ResourceManager.get_instance().get( "rule.type.condition.list"),
			new Color( 64, 128, 0));
		_conditionColorMap.put( ResourceManager.get_instance().get( "rule.type.condition.functional.object"),
			( Environment.get_instance().is_functional_object_enable()
				? new Color( 0, 128, 64) : new Color( 0, 0, 0)));
		_conditionColorMap.put( ResourceManager.get_instance().get( "rule.type.condition.others"),
			new Color( 0, 0, 0));

		_commandColorMap = new HashMap();

		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.keyword"),
			new Color( 0, 128, 128));
		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.move"),
			new Color( 240, 0, 0));
		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.spot.variable"),
			new Color( 220, 0, 0));
		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.time"),
			new Color( 128, 0, 0));
		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.role"),
			new Color( 0, 64, 128));
		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.set.role"),
			new Color( 0, 64, 128));
		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.substitution"),
			new Color( 0, 0, 128));
		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.collection"),
			new Color( 128, 128, 0));
		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.list"),
			new Color( 64, 128, 0));
		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.map"),
			new Color( 0, 160, 0));
		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.create.agent"),
			new Color( 128, 0, 128));
		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.create.object"),
			new Color( 128, 0, 128));
		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.get.equip"),
			new Color( 128, 128, 128));
		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.put.equip"),
			new Color( 128, 128, 128));
		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.attach"),
			new Color( 0, 128, 128));
		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.detach"),
			new Color( 0, 128, 128));
		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.functional.object"),
			( Environment.get_instance().is_functional_object_enable()
				? new Color( 0, 128, 64) : new Color( 0, 0, 0)));
		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"),
			( Environment.get_instance().is_exchange_algebra_enable()
				? new Color( 0, 0, 200) : new Color( 0, 0, 0)));
		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.extransfer"),
			( Environment.get_instance().is_extransfer_enable()
				? new Color( 0, 0, 200) : new Color( 0, 0, 0)));
		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.next.stage"),
			new Color( 0, 0, 200));
		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.terminate"),
			new Color( 128, 0, 0));
		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.trace"),
			new Color( 128, 128, 255));
		_commandColorMap.put( ResourceManager.get_instance().get( "rule.type.command.others"),
			new Color( 0, 0, 0));
	}

	/**
	 * @param type
	 * @param kind
	 * @return
	 */
	public static String get_name(String type, String kind) {
		if ( kind.equals( "condition"))
			return ( String)_conditionNameMap.get( type);
		else if ( kind.equals( "command"))
			return ( String)_commandNameMap.get( type);

		return "";
	}

	/**
	 * @param rule
	 * @return
	 */
	public static String get_name(Rule rule) {
		if ( rule._kind.equals( "condition"))
			return ( String)_conditionNameMap.get( rule._type);
		else if ( rule._kind.equals( "command"))
			return ( String)_commandNameMap.get( rule._type);

		return "";
	}

	/**
	 * @param kind
	 * @param name
	 * @return
	 */
	public static String get_type(String kind, String name) {
		if ( kind.equals( "condition"))
			return get_type( name, _conditionNameMap);
		else if ( kind.equals( "command"))
			return get_type( name, _commandNameMap);

		return "";
	}

	/**
	 * @param name
	 * @param name_map
	 * @return
	 */
	private static String get_type(String name, HashMap name_map) {
		Iterator iterator = name_map.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String value = ( String)entry.getValue();
			if ( value.equals( name))
				return ( String)entry.getKey();
		}
		return "";
	}

	/**
	 * @param rule
	 * @return
	 */
	public static Color get_color(Rule rule) {
		if ( rule._kind.equals( "condition"))
			return ( Color)_conditionColorMap.get( rule._type);
		else if ( rule._kind.equals( "command"))
			return ( Color)_commandColorMap.get( rule._type);

		return null;
	}

	/**
	 * @param type
	 * @param kind
	 * @return
	 */
	public static Color get_color(String type, String kind) {
		if ( kind.equals( "condition"))
			return ( Color)_conditionColorMap.get( type);
		else if ( kind.equals( "command"))
			return ( Color)_commandColorMap.get( type);

		return null;
	}
}
