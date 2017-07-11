/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.spot.SpotRole;
import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 *
 */
public class Subject extends Vector<Variable> {

	/**
	 * 
	 */
	public boolean _self = false;

	/**
	 * 
	 */
	public boolean _agent = false;

	/**
	 * 
	 */
	public boolean _spot = false;

	/**
	 * 
	 */
	public boolean _currentSpot = false;

	/**
	 * 
	 */
	public boolean _agentVariable = false;

	/**
	 * 
	 */
	public boolean _spotVariable = false;

	/**
	 * 
	 */
	public String _sync = "";

	/**
	 * For constant
	 */
	public boolean _constant = false;

	/**
	 * For constant
	 */
	public String _name = "";

	/**
	 * For constant
	 */
	public List<String> _constants = new ArrayList<String>();

	/**
	 * For constant
	 */
	public Map<String, String> _constantNameMap = new HashMap<String, String>();

	/**
	 * For constant
	 */
	public Map<String, String> _nameConstantMap = new HashMap<String, String>();

	/**
	 * For expression
	 */
	public boolean _expression = false;

	/**
	 * 
	 */
	public Subject() {
		super();
	}

	/**
	 * @param subject
	 */
	public Subject(Subject subject) {
		super();
		copy( subject);
	}

	/**
	 * @param subject
	 */
	private void copy(Subject subject) {
		_self = subject._self;
		_agent = subject._agent;
		_spot = subject._spot;
		_currentSpot = subject._currentSpot;
		_agentVariable = subject._agentVariable;
		_spotVariable = subject._spotVariable;

		_sync = subject._sync;

		_constant = subject._constant;
		_name = subject._name;
		_constants = new ArrayList<String>( subject._constants);
		_constantNameMap = new HashMap<String, String>( subject._constantNameMap);
		_nameConstantMap = new HashMap<String, String>( subject._nameConstantMap);

		_expression = subject._expression;

		clear();
		for ( Variable variable:subject)
			add( new Variable( variable));
	}

	/**
	 * @param line
	 * @return
	 */
	public boolean set(String line) {
		if ( line.equals( ""))
			return true;

		String[] words = Tool.split( line, ',');
		if ( null == words)
			return false;

		for ( String word:words) {
			if ( word.equals( "self"))
				_self = true;
			else if ( word.equals( "agent"))
				_agent = true;
			else if ( word.equals( "spot"))
				_spot = true;
			else if ( word.equals( "currentspot"))
				_currentSpot = true;
			else if ( word.equals( "agentvariable"))
				_agentVariable = true;
			else if ( word.equals( "spotvariable"))
				_spotVariable = true;
		}

		return true;
	}

	/**
	 * @param line
	 * @return
	 */
	public boolean set_variable(String line) {
		if ( line.equals( ""))
			return false;

		String[] words = Tool.split( line, ',');
		if ( null == words || 2 > words.length)
			return false;

		// ここでタイプをチェック
		if ( !Variable._allTypes.contains( words[ 0]))
			return false;

		if ( words[ 1].equals( ""))
			return false;

		Variable variable = ( 2 == words.length) ? new Variable( words[ 0], words[ 1]) : new Variable( words[ 0], words[ 1], words[ 2].equals( "empty"));
		if ( contains_same_variable( variable))
			return false;

		add( variable);

		return true;
	}

	/**
	 * @param variable
	 * @return
	 */
	private boolean contains_same_variable(Variable variable) {
		for ( Variable v:this) {
			if ( variable._type.equals( v._type) || variable._name.equals( v._name))
				return true;
		}
		return false;
	}

	/**
	 * @param line
	 * @return
	 */
	public boolean set_constant(String line) {
		if ( line.equals( ""))
			return false;

		_name = line;

		_constant = true;

		return true;
	}

	/**
	 * @param line
	 * @return
	 */
	public boolean set_value(String line) {
		if ( line.equals( ""))
			return false;

		String[] words = Tool.split( line, ',');
		if ( null == words || 2 > words.length)
			return false;

		if ( words[ 0].equals( "") || words[ 1].equals( ""))
			return false;

		if ( null != _constantNameMap.get( words[ 0])
			|| null != _nameConstantMap.get( words[ 1]))
			return false;

		_constantNameMap.put( words[ 0], words[ 1]);
		_nameConstantMap.put( words[ 1], words[ 0]);
		_constants.add( words[ 0]);
		return true;
	}

	/**
	 * @param line
	 * @return
	 */
	public boolean set_expression(String line) {
		if ( !set( line))
			return false;

		_expression = true;

		return true;
	}

	/**
	 * @param role
	 * @param entities
	 * @param entityVariables
	 * @param variables
	 */
	public void get_all_components(Role role, List<String> entities, List<String> entityVariables, List<Variable> variables) {
		if ( _self) {
			if ( role instanceof AgentRole)
				entities.add( "self");
			else if ( role instanceof SpotRole)
				entities.add( "currentspot");
		}
		if ( _agent)
			entities.add( "agent");
		if ( _spot)
			entities.add( "spot");
		if ( _currentSpot && role instanceof AgentRole)
			entities.add( "currentspot");
		
		if ( _agentVariable)
			entityVariables.add( "agentvariable");
		if ( _spotVariable)
			entityVariables.add( "spotvariable");

		get_variables( variables);
	}

	/**
	 * @param variables
	 */
	public void get_variables(List<Variable> variables) {
		for ( Variable variable:this) {
			if ( contains( variables, variable))
				continue;

			variables.add( new Variable( variable));
		}
	}

	/**
	 * @param variables
	 * @param variable
	 * @return
	 */
	private boolean contains(List<Variable> variables, Variable variable) {
		for ( Variable v:variables) {
			if ( variable._type.equals( v._type))
				return true;
		}
		return false;
	}

	/**
	 * @param subject
	 * @return
	 */
	public boolean same_as(Subject subject) {
		return ( _self == subject._self
			&& _agent == subject._agent
			&& _spot == subject._spot
			&& _currentSpot == subject._currentSpot
			&& _agentVariable == subject._agentVariable
			&& _spotVariable == subject._spotVariable
			&& _constant == subject._constant
			&& _name.equals( subject._name)
			&& same_constants( subject)
			&& same_map( _constantNameMap, subject._constantNameMap)
			&& same_map( _nameConstantMap, subject._nameConstantMap)
			&& _expression == subject._expression
			&& same_variables( subject));
	}

	/**
	 * @param subject
	 * @return
	 */
	private boolean same_constants(Subject subject) {
		if ( _constants.size() != subject._constants.size())
			return false;

		for ( int i = 0; i < _constants.size(); ++i) {
			if ( !_constants.get( i).equals( subject._constants.get( i)))
				return false;
		}

		return true;
	}

	/**
	 * @param map1
	 * @param map2
	 * @return
	 */
	private boolean same_map(Map<String, String> map1, Map<String, String> map2) {
		if ( map1.size() != map2.size())
			return false;

		Iterator iterator = map1.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String key = ( String)entry.getKey();
			String value = ( String)entry.getValue();
			if ( !value.equals( map2.get( key)))
				return false;
		}
		return true;
	}

	/**
	 * @param variables
	 * @return
	 */
	private boolean same_variables(Vector<Variable> variables) {
		if ( size() != variables.size())
			return false;

		for ( int i = 0; i < size(); ++i) {
			if ( !get( i).same_as( variables.get( i)))
				return false;
		}

		return true;
	}

	/**
	 * @param type
	 * @return
	 */
	public boolean contains_empty(String type) {
		for ( Variable variable:this) {
			if ( variable._type.equals( type))
				return variable._empty;
		}
		return false;
	}

	/**
	 * @param kind
	 */
	public void print(String kind) {
		System.out.println( kind);

		String text = "\t";
		if ( _self)
			text += ( ( text.equals( "\t") ? "" : ", ") + "Self");
		if ( _agent)
			text += ( ( text.equals( "\t") ? "" : ", ") + "Agent");
		if ( _spot)
			text += ( ( text.equals( "\t") ? "" : ", ") + "Spot");
		if ( _currentSpot)
			text += ( ( text.equals( "\t") ? "" : ", ") + "Current spot");
		if ( _agentVariable)
			text += ( ( text.equals( "\t") ? "" : ", ") + "Agent variable");
		if ( _spotVariable)
			text += ( ( text.equals( "\t") ? "" : ", ") + "Spot variable");
		if ( !text.equals( "\t"))
			System.out.println( text);

		for ( Variable variable:this)
			System.out.println( "\tVariable : " + variable._type + ", " + variable._name);
	}
}
