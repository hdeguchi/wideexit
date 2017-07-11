/*
 * Created on 2006/07/24
 */
package soars.application.visualshell.layer;

import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.spot.SpotObject;

/**
 * The interface to manipulate the layer.
 * @author kurata / SOARS project
 */
public interface ILayerManipulator {

	/**
	 * Returns the agent object which has the specified name.
	 * @param fullName the specified name
	 * @return the agent object which has the specified name
	 */
	public AgentObject get_agent_has_this_name(String fullName);

	/**
	 * Returns the spot object which has the specified name.
	 * @param fullName the specified name
	 * @return the spot object which has the specified name
	 */
	public SpotObject get_spot_has_this_name(String fullName);

	/**
	 * Returns true if the object which has the specified name exists.
	 * @param kind the type of the object
	 * @param name the specified name
	 * @return true if the object which has the specified name exists
	 */
	public boolean is_object_name(String kind, String name);

	/**
	 * Returns true if the agent's object which has the specified name exists.
	 * @param kind the type of the object
	 * @param name the specified name
	 * @return true if the agent's object which has the specified name exists
	 */
	public boolean is_agent_object_name(String kind, String name);

	/**
	 * Returns true if the agent's object which has the specified name exists.
	 * @param kind the type of the object
	 * @param fullName the specified name
	 * @param name the specified name
	 * @return true if the agent's object which has the specified name exists
	 */
	public boolean is_agent_object_name(String kind, String fullName, String name);

	/**
	 * Returns true if the spot's object which has the specified name exists.
	 * @param kind the type of the object
	 * @param name the specified name
	 * @return true if the spot's object which has the specified name exists
	 */
	public boolean is_spot_object_name(String kind, String name);

	/**
	 * Returns true if the spot's object which has the specified name exists.
	 * @param kind the type of the object
	 * @param fullName the specified name
	 * @param name the specified name
	 * @return true if the spot's object which has the specified name exists
	 */
	public boolean is_spot_object_name(String kind, String fullName, String name);

	/**
	 * @param numberObjectName
	 * @param numberObjectType
	 * @return
	 */
	public boolean is_agent_number_object_name(String numberObjectName, String numberObjectType);

	/**
	 * @param fullName
	 * @param numberObjectName
	 * @param numberObjectType
	 * @return
	 */
	public boolean is_agent_number_object_name(String fullName, String numberObjectName, String numberObjectType);

	/**
	 * @param numberObjectName
	 * @param numberObjectType
	 * @return
	 */
	public boolean is_spot_number_object_name(String numberObjectName, String numberObjectType);

	/**
	 * @param fullName
	 * @param numberObjectName
	 * @param numberObjectType
	 * @return
	 */
	public boolean is_spot_number_object_name(String fullName, String numberObjectName, String numberObjectType);

	/**
	 * Returns the type of the specified number variable.
	 * @param numberObjectName the specified number variable
	 * @return the type of the specified number variable
	 */
	public String get_agent_number_object_type(String numberObjectName);

	/**
	 * Returns the type of the specified number variable.
	 * @param fullName the specified number variable
	 * @param numberObjectName the specified number variable
	 * @return the type of the specified number variable
	 */
	public String get_agent_number_object_type(String fullName, String numberObjectName);

	/**
	 * Returns the type of the specified number variable.
	 * @param numberObjectName the specified number variable
	 * @return the type of the specified number variable
	 */
	public String get_spot_number_object_type(String numberObjectName);

	/**
	 * Returns the type of the specified number variable.
	 * @param fullName the specified number variable
	 * @param numberOobjectName the specified number variable
	 * @return the type of the specified number variable
	 */
	public String get_spot_number_object_type(String fullName, String numberOobjectName);

	/**
	 * Returns true if the role which has the specified name exists.
	 * @param name the specified name
	 * @return true if the role which has the specified name exists
	 */
	public boolean is_role_name(String name);

	/**
	 * Returns true if the agent role which has the specified name exists.
	 * @param name the specified name
	 * @return true if the agent role which has the specified name exists
	 */
	public boolean is_agent_role_name(String name);

	/**
	 * Returns true if the spot role which has the specified name exists.
	 * @param name the specified name
	 * @return true if the spot role which has the specified name exists
	 */
	public boolean is_spot_role_name(String name);
}
