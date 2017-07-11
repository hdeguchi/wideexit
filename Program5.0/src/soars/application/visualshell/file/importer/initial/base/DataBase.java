/**
 * 
 */
package soars.application.visualshell.file.importer.initial.base;

import java.awt.Point;

/**
 * The base class for EntityData and RoleData.
 * @author kurata / SOARS project
 */
public class DataBase {

	/**
	 * Type of this object("agent", "spot", "agent_role" or "spot_role").
	 */
	public String _type = "";

	/**
	 * Name of this object.
	 */
	public String _name = "";

	/**
	 * Position of this object.
	 */
	public Point _position = null;

	/**
	 * Creates this object with the specified data.
	 * @param type the type of this object
	 * @param name the name of this object
	 */
	public DataBase(String type, String name) {
		super();
		_type = type;
		_name = name;
		_position = new Point();
	}

	/**
	 * Creates this object with the specified data.
	 * @param type the type of this object
	 * @param name the name of this object
	 * @param position the position of this object
	 */
	public DataBase(String type, String name, Point position) {
		super();
		_type = type;
		_name = name;
		_position = new Point( position);
	}
}
