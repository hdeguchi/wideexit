/**
 * 
 */
package soars.application.visualshell.object.role.base.object.generic.element;

import java.util.List;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Variable;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.spot.SpotRole;
import soars.application.visualshell.object.stage.StageManager;
import soars.common.utility.tool.expression.Expression;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class EntityVariableRule implements IObject {

	/**
	 * 
	 */
	public static String _null = "null";

	/**
	 * 
	 */
	public String _entity = "";

	/**
	 * 
	 */
	public String _entityName = "";

	/**
	 * 
	 */
	public String _agentVariable = "";

	/**
	 * 
	 */
	public String _spotVariable = "";

	/**
	 * 
	 */
	public String _variableType = "";

	/**
	 * 
	 */
	public String _variableValue = "";

	/**
	 * 
	 */
	public EntityVariableRule() {
		super();
	}

	/**
	 * @param entityVariableRule
	 */
	public EntityVariableRule(EntityVariableRule entityVariableRule) {
		super();
		copy( entityVariableRule);
	}

	/**
	 * @param entity
	 * @param entityName
	 * @param agentVariable
	 * @param spotVariable
	 * @param variableType
	 * @param variableValue
	 */
	public EntityVariableRule(String entity, String entityName, String agentVariable, String spotVariable, String variableType, String variableValue) {
		super();
		set( entity, entityName, agentVariable, spotVariable, variableType, variableValue);
	}

	/**
	 * @param entity
	 * @param entityName
	 * @param agentVariable
	 * @param spotVariable
	 * @param variableType
	 * @param variableValue
	 */
	public void set(String entity, String entityName, String agentVariable, String spotVariable, String variableType, String variableValue) {
		_entity = entity;
		_entityName = entityName;
		_agentVariable = agentVariable;
		_spotVariable = spotVariable;
		_variableType = variableType;
		_variableValue = variableValue;
	}

	/**
	 * @param entityVariableRule
	 */
	public void copy(EntityVariableRule entityVariableRule) {
		_entity = entityVariableRule._entity;
		_entityName = entityVariableRule._entityName;
		_agentVariable = entityVariableRule._agentVariable;
		_spotVariable = entityVariableRule._spotVariable;
		_variableType = entityVariableRule._variableType;
		_variableValue = entityVariableRule._variableValue;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#same_as(soars.application.visualshell.object.role.base.object.generic.element.IObject)
	 */
	@Override
	public boolean same_as(IObject object) {
		if ( !( object instanceof EntityVariableRule))
			return false;

		EntityVariableRule entityVariableRule = ( EntityVariableRule)object;
		return ( _entity.equals( entityVariableRule._entity)
			&& _entityName.equals( entityVariableRule._entityName)
			&& _agentVariable.equals( entityVariableRule._agentVariable)
			&& _spotVariable.equals( entityVariableRule._spotVariable)
			&& _variableType.equals( entityVariableRule._variableType)
			&& _variableValue.equals( entityVariableRule._variableValue));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_initial_values(java.util.Vector)
	 */
	@Override
	public void get_initial_values(Vector<String> initialValues) {
		if ( !Variable._immediateTypes.contains( _variableType))
			return;

		if ( !_variableValue.startsWith( "$"))
			return;

		initialValues.add( _variableValue);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#has_same_agent_name(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean has_same_agent_name(String name, String number) {
		return ( ( _entity.equals( "agent") && _entityName.equals( name + number))
			|| ( _variableType.equals( "agent") && _variableValue.equals( name + number)));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#contains_this_alias(java.lang.String)
	 */
	@Override
	public boolean contains_this_alias(String alias) {
		if ( !Variable._immediateTypes.contains( _variableType))
			return false;

		return _variableValue.equals( alias);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_agent_names(java.util.List)
	 */
	@Override
	public void get_used_agent_names(List<String> names) {
		if ( _entity.equals( "agent") && !_entityName.equals( ""))
			names.add( _entityName);

		if ( _variableType.equals( "agent") && !_variableValue.equals( ""))
			names.add( _variableValue);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_spot_names(java.util.List)
	 */
	@Override
	public void get_used_spot_names(List<String> names) {
		if ( _entity.equals( "spot") && !_entityName.equals( ""))
			names.add( _entityName);

		if ( _variableType.equals( "spot") && !_variableValue.equals( ""))
			names.add( _variableValue);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_independent_variable_names(java.lang.String, java.util.List)
	 */
	@Override
	public void get_used_independent_variable_names(String type, List<String> names) {
		if ( _variableType.equals( type) && !_variableValue.equals( ""))
			names.add( _variableValue);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_agent_variable_names(java.util.List)
	 */
	@Override
	public void get_used_agent_variable_names(List<String> names) {
		// TODO いずれ必要になるだろう
		get_used_entityVariable_names( _agentVariable, names);
		get_used_variable_names( "agent variable", names);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_agent_variable_names(java.util.List, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public void get_used_agent_variable_names(List<String> names, EntityVariableRule subject) {
		// TODO いずれ必要になるだろう
		get_used_entityVariable_names( _agentVariable, names, subject);
		get_used_variable_names( "agent variable", names, subject);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_spot_variable_names(java.util.List)
	 */
	@Override
	public void get_used_spot_variable_names(List<String> names) {
		get_used_entityVariable_names( _spotVariable, names);
		get_used_variable_names( "spot variable", names);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_spot_variable_names(java.util.List, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public void get_used_spot_variable_names(List<String> names, EntityVariableRule subject) {
		get_used_entityVariable_names( _spotVariable, names, subject);
		get_used_variable_names( "spot variable", names, subject);
	}

	/**
	 * @param entityVariable
	 * @param names
	 */
	private void get_used_entityVariable_names(String entityVariable, List<String> names) {
		if ( !entityVariable.equals( "")) {
			String prefix = get_prefix( _entity, _entityName);
			if ( null != prefix)
				names.add( prefix + entityVariable);
		}
	}

	/**
	 * @param entityVariable
	 * @param names
	 * @param subject
	 */
	private void get_used_entityVariable_names(String entityVariable, List<String> names, EntityVariableRule subject) {
		if ( !entityVariable.equals( "")) {
			String prefix = !_entity.equals( _null)
				? get_prefix( _entity, _entityName)
				: get_prefix( subject._entity, subject._entityName, subject._agentVariable, subject._spotVariable);
			if ( null != prefix)
				names.add( prefix + entityVariable);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_variable_names(java.lang.String, java.util.List)
	 */
	@Override
	public void get_used_variable_names(String type, List<String> names) {
		if ( ( _variableType.equals( type) || ( type.equals( "number object") && ( _variableType.equals( "integer number object") || _variableType.equals( "real number object")))) && !_variableValue.equals( "")) {
			String prefix = get_prefix( _entity, _entityName, _agentVariable, _spotVariable);
			if ( null != prefix)
				names.add( prefix + _variableValue);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_variable_names(java.lang.String, java.util.List, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public void get_used_variable_names(String type, List<String> names, EntityVariableRule subject) {
		if ( ( _variableType.equals( type) || ( type.equals( "number object") && ( _variableType.equals( "integer number object") || _variableType.equals( "real number object")))) && !_variableValue.equals( "")) {
			String prefix = null;
			if ( !_entity.equals( _null))
				prefix = get_prefix( _entity, _entityName, _agentVariable, _spotVariable);
			else
				prefix = ( !_agentVariable.equals( "") || !_spotVariable.equals( ""))
					? get_prefix( subject._entity, subject._entityName, _agentVariable, _spotVariable)
					: get_prefix( subject._entity, subject._entityName, subject._agentVariable, subject._spotVariable);
			if ( null != prefix)
				names.add( prefix + _variableValue);
		}
	}

	/**
	 * @param entity
	 * @param entityName
	 * @return
	 */
	private String get_prefix(String entity, String entityName) {
		if ( entity.equals( "self"))
			return "";
		else if ( entity.equals( "agent") || entity.equals( "spot"))
			return ( "<" + entityName + ">");
		else if ( entity.equals( "currentspot"))
			return ( "<>");
		return null;
	}

	/**
	 * @return
	 */
	public String get_prefix() {
		return get_prefix( _entity, _entityName, _agentVariable, _spotVariable);
	}

	/**
	 * @param entity
	 * @param entityName
	 * @param agentVariable
	 * @param spotVariable
	 * @return
	 */
	private String get_prefix(String entity, String entityName, String agentVariable, String spotVariable) {
		if ( entity.equals( "self")) {
			if ( agentVariable.equals( "") && spotVariable.equals( ""))
				return "";
			else {
				if ( !agentVariable.equals( ""))
					return ( "<" + agentVariable + ">");
				else if ( !spotVariable.equals( ""))
					return ( "<" + spotVariable + ">");
			}
		} else if ( entity.equals( "agent") || entity.equals( "spot")) {
			if ( agentVariable.equals( "") && spotVariable.equals( ""))
				return ( "<" + entityName + ">");
			else {
				if ( !agentVariable.equals( ""))
					return ( "<" + entityName + ":" + agentVariable + ">");
				else if ( !spotVariable.equals( ""))
					return ( "<" + entityName + ":" + spotVariable + ">");
			}
		} else if ( entity.equals( "currentspot")) {
			if ( agentVariable.equals( "") && spotVariable.equals( ""))
				return ( "<>");
			else {
				if ( !agentVariable.equals( ""))
					return ( "<" + ":" + agentVariable + ">");
				else if ( !spotVariable.equals( ""))
					return ( "<" + ":" + spotVariable + ">");
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_expressions(java.util.List)
	 */
	@Override
	public void get_used_expressions(List<String> names) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_agent_or_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_agent_or_spot_name_and_number(String type, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result1 = update_entityName( type, newName, originalName, headName, ranges, newHeadName, newRanges);
		boolean result2 = update_entityName_in_variableValue( type, newName, originalName, headName, ranges, newHeadName, newRanges);
		return ( result1 || result2);
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
	private boolean update_entityName(String type, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		if ( !_entity.equals( type))
			return false;

		String newEntityName = CommonRuleManipulator.get_new_agent_or_spot_name( _entityName, newName, originalName, headName, ranges, newHeadName, newRanges);
		if ( null == newEntityName)
			return false;

		_entityName = newEntityName;
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
	private boolean update_entityName_in_variableValue(String type, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		if ( !_variableType.equals( type))
			return false;

		String newEntityName = CommonRuleManipulator.get_new_agent_or_spot_name( _variableValue, newName, originalName, headName, ranges, newHeadName, newRanges);
		if ( null == newEntityName)
			return false;

		_variableValue = newEntityName;
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_role_name(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_role_name(String originalName, String newName) {
		boolean result = false;
		if ( update_independent_variable_name( "role", originalName, newName))
			result = true;
		if ( update_independent_variable_name( "agent role", originalName, newName))
			result = true;
		if ( update_independent_variable_name( "spot role", originalName, newName))
			result = true;
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_independent_variable_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_independent_variable_name(String type, String originalName, String newName) {
		if ( _variableType.equals( type) && _variableValue.equals( originalName)) {
			_variableValue = newName;
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_agent_variable_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_agent_variable_name(String originalName, String newName, String entityType) {
		// TODO いずれ必要になるだろう
		boolean result = false;
		if ( !_agentVariable.equals( "") && correspond( _entity, _agentVariable, originalName, entityType)) {
			_agentVariable = newName;
			return true;
		}

		if ( update_variable_name( "agent variable", originalName, newName, entityType))
			result = true;

		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_agent_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public boolean update_agent_variable_name(String originalName, String newName, String entityType, EntityVariableRule subject) {
		// TODO いずれ必要になるだろう
		boolean result = false;
		if ( !_agentVariable.equals( "")) {
			if ( !_entity.equals( _null)) {
				if ( correspond( _entity, _agentVariable, originalName, entityType)) {
					_agentVariable = newName;
					result = true;
				}
			} else {
				if ( correspond( subject._entity, subject._agentVariable, subject._spotVariable, _agentVariable, originalName, entityType)) {
					_agentVariable = newName;
					result = true;
				}						
			}
		}

		if ( update_variable_name( "agent variable", originalName, newName, entityType, subject))
			result = true;

		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_spot_variable_name(String originalName, String newName, String entityType) {
		boolean result = false;
		if ( !_spotVariable.equals( "") && correspond( _entity, _spotVariable, originalName, entityType)) {
			_spotVariable = newName;
			result = true;
		}

		if ( update_variable_name( "spot variable", originalName, newName, entityType))
			result = true;

		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public boolean update_spot_variable_name(String originalName, String newName, String entityType, EntityVariableRule subject) {
		boolean result = false;
		if ( !_spotVariable.equals( "")) {
			if ( !_entity.equals( _null)) {
				if ( correspond( _entity, _spotVariable, originalName, entityType)) {
					_spotVariable = newName;
					result = true;
				}
			} else {
				if ( correspond( subject._entity, subject._agentVariable, subject._spotVariable, _spotVariable, originalName, entityType)) {
					_spotVariable = newName;
					result = true;
				}						
			}
		}

		if ( update_variable_name( "spot variable", originalName, newName, entityType, subject))
			result = true;

		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_variable_name(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_variable_name(String type, String originalName, String newName, String entityType) {
		boolean result = false;
		if ( ( _variableType.equals( type) || ( type.equals( "number object") && ( _variableType.equals( "integer number object") || _variableType.equals( "real number object")))) && !_variableValue.equals( "")) {
			if ( correspond( _entity, _agentVariable, _spotVariable, _variableValue, originalName, entityType)) {
				_variableValue = newName;
				result = true;
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_variable_name(java.lang.String, java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public boolean update_variable_name(String type, String originalName, String newName, String entityType, EntityVariableRule subject) {
		if ( ( _variableType.equals( type) || ( type.equals( "number object") && ( _variableType.equals( "integer number object") || _variableType.equals( "real number object")))) && !_variableValue.equals( "")) {
			if ( !_entity.equals( _null)) {
				if ( correspond( _entity, _agentVariable, _spotVariable, _variableValue, originalName, entityType)) {
					_variableValue = newName;
					return true;
				}
			} else
				if ( !_agentVariable.equals( "") || !_spotVariable.equals( "")) {
					if ( correspond( subject._entity, _agentVariable, _spotVariable, _variableValue, originalName, entityType)) {
						_variableValue = newName;
						return true;
					}						
				} else {
					if ( correspond( subject._entity, subject._agentVariable, subject._spotVariable, _variableValue, originalName, entityType)) {
						_variableValue = newName;
						return true;
					}						
				}
		}
		return false;
	}

	/**
	 * @param entity
	 * @param entityVariable
	 * @param originalName
	 * @param entityType
	 * @return
	 */
	private boolean correspond(String entity, String entityVariable, String originalName, String entityType) {
		if ( entityType.equals( "agent")) {
			if ( entity.equals( "self") || entity.equals( "agent"))
				return entityVariable.equals( originalName);
		} else if ( entityType.equals( "spot")) {
			if ( entity.equals( "currentspot") || entity.equals( "spot"))
				return entityVariable.equals( originalName);
		}
		return false;
	}

	/**
	 * @param entity
	 * @param agentVariable
	 * @param spotVariable
	 * @param variableValue
	 * @param originalName
	 * @param entityType
	 * @return
	 */
	private boolean correspond(String entity, String agentVariable, String spotVariable, String variableValue, String originalName, String entityType) {
		if ( entityType.equals( "agent")) {
			if ( entity.equals( "self") || entity.equals( "agent") || !agentVariable.equals( ""))
				return variableValue.equals( originalName);
		} else if ( entityType.equals( "spot")) {
			if ( entity.equals( "currentspot") || entity.equals( "spot") || !spotVariable.equals( ""))
				return variableValue.equals( originalName);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_expression(soars.common.utility.tool.expression.Expression, int, soars.common.utility.tool.expression.Expression)
	 */
	@Override
	public boolean update_expression(Expression newExpression, int newVariableCount, Expression originalExpression) {
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#can_paste(soars.application.visualshell.layer.Layer)
	 */
	@Override
	public boolean can_paste(Layer drawObjects) {
		if ( !can_paste_entity( drawObjects))
			return false;

		if ( !can_paste_entityVariable( _entity, _entityName, drawObjects))
			return false;

		if ( !can_paste_variable( drawObjects))
			return false;

		return true;
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_variable(Layer drawObjects) {
		if ( _variableType.equals( "") || !Variable._variableTypes.contains( _variableType) || _variableValue.equals( ""))
			return true;

		if ( _variableType.equals( "integer number object"))
			return can_paste_number_object( _entity, _entityName, _agentVariable, _spotVariable, "integer", drawObjects);
		else if ( _variableType.equals( "real number object"))
			return can_paste_number_object( _entity, _entityName, _agentVariable, _spotVariable, "real number", drawObjects);

		return can_paste_variable( _entity, _entityName, _agentVariable, _spotVariable, drawObjects);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#can_paste(soars.application.visualshell.layer.Layer, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public boolean can_paste(Layer drawObjects, EntityVariableRule subject) {
		if ( !_entity.equals( _null)) {
			if ( !can_paste_entity( drawObjects))
				return false;
		}

		if ( !can_paste_entityVariable( drawObjects, subject))
			return false;

		if ( !can_paste_variable( drawObjects, subject))
			return false;

		return true;
	}

	/**
	 * @param drawObjects
	 * @param subject
	 * @return
	 */
	private boolean can_paste_entityVariable(Layer drawObjects, EntityVariableRule subject) {
		if ( !_entity.equals( _null))
			return can_paste_entityVariable( _entity, _entityName, drawObjects);
		else {
			if ( !_agentVariable.equals( ""))
				return can_paste_variable( subject._entity, subject._entityName, subject._agentVariable, subject._spotVariable, "agent variable", _agentVariable, drawObjects);
			else if ( !_spotVariable.equals( ""))
				return can_paste_variable( subject._entity, subject._entityName, subject._agentVariable, subject._spotVariable, "spot variable", _spotVariable, drawObjects);
			else
				return true;
		}
	}

	/**
	 * @param drawObjects
	 * @param subject
	 * @return
	 */
	private boolean can_paste_variable(Layer drawObjects, EntityVariableRule subject) {
		if ( _variableType.equals( "") || !Variable._variableTypes.contains( _variableType) || _variableValue.equals( ""))
			return true;

		if ( _variableType.equals( "integer number object")) {
			if ( !_entity.equals( _null))
				return can_paste_number_object( _entity, _entityName, _agentVariable, _spotVariable, "integer", drawObjects);
			else
				return ( !_agentVariable.equals( "") || !_spotVariable.equals( ""))
					? can_paste_number_object( subject._entity, subject._entityName, _agentVariable, _spotVariable, "integer", drawObjects)
					: can_paste_number_object( subject._entity, subject._entityName, subject._agentVariable, subject._spotVariable, "integer", drawObjects);
		} else if ( _variableType.equals( "real number object")) {
			if ( !_entity.equals( _null))
				return can_paste_number_object( _entity, _entityName, _agentVariable, _spotVariable, "real number", drawObjects);
			else
				return ( !_agentVariable.equals( "") || !_spotVariable.equals( ""))
					? can_paste_number_object( subject._entity, subject._entityName, _agentVariable, _spotVariable, "real number", drawObjects)
					: can_paste_number_object( subject._entity, subject._entityName, subject._agentVariable, subject._spotVariable, "real number", drawObjects);
		}

		if ( !_entity.equals( _null))
			return can_paste_variable( _entity, _entityName, _agentVariable, _spotVariable, drawObjects);
		else {
			if ( !_agentVariable.equals( "") || !_spotVariable.equals( ""))
				return can_paste_variable( subject._entity, subject._entityName, _agentVariable, _spotVariable, drawObjects);
			else
				return can_paste_variable( subject._entity, subject._entityName, subject._agentVariable, subject._spotVariable, drawObjects);
		}
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_entity(Layer drawObjects) {
		if ( _entity.equals( "agent") && !_entityName.equals( ""))
			return ( null != drawObjects.get_agent_has_this_name( _entityName));
		if ( _entity.equals( "spot") && !_entityName.equals( ""))
			return ( null != drawObjects.get_spot_has_this_name( _entityName));
		return true;
	}

	/**
	 * @param entity
	 * @param entityName
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_entityVariable(String entity, String entityName, Layer drawObjects) {
		if ( !_agentVariable.equals( "") && !can_paste_variable( entity, entityName, "", "", "agent variable", _agentVariable, drawObjects))
			return false;

		if ( !_spotVariable.equals( "") && !can_paste_variable( entity, entityName, "", "", "spot variable", _spotVariable, drawObjects))
			return false;

		return true;
	}

	/**
	 * @param entity
	 * @param entityName
	 * @param agentVariable
	 * @param spotVariable
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_variable(String entity, String entityName, String agentVariable, String spotVariable, Layer drawObjects) {
		if ( _variableType.equals( "agent"))
			return ( null != drawObjects.get_agent_has_this_name( _variableValue));
		else if ( _variableType.equals( "spot"))
			return ( null != drawObjects.get_spot_has_this_name( _variableValue));
		else if ( _variableType.equals( "role"))
			return drawObjects.is_role_name( _variableValue);
		else if ( _variableType.equals( "agent role"))
			return drawObjects.is_agent_role_name( _variableValue);
		else if ( _variableType.equals( "spot role"))
			return drawObjects.is_spot_role_name( _variableValue);
		else if ( _variableType.equals( "stage"))
			// 不足しているステージは自動的に追加されるのでチェックする必要は無い
			return true;
		else if ( Variable._variableTypes.contains( _variableType))
			return can_paste_variable( entity, entityName, agentVariable, spotVariable, _variableType, _variableValue, drawObjects);
		else
			return true;
	}

	/**
	 * @param entity
	 * @param entityName
	 * @param agentVariable
	 * @param spotVariable
	 * @param variableType
	 * @param variableValue
	 * @param drawObjects
	 * @return
	 */
	public static boolean can_paste_variable(String entity, String entityName, String agentVariable, String spotVariable, String variableType, String variableValue, Layer drawObjects) {
		if ( !agentVariable.equals( ""))
			return drawObjects.is_agent_object_name( variableType, variableValue);
		else if ( !spotVariable.equals( ""))
			return drawObjects.is_spot_object_name( variableType, variableValue);
		else {
			if ( entity.equals( "self"))
				return drawObjects.is_agent_object_name( variableType, variableValue);
			else if ( entity.equals( "currentspot"))
				return drawObjects.is_spot_object_name( variableType, variableValue);
			else if ( entity.equals( "agent")) {
				AgentObject agentObject = drawObjects.get_agent_has_this_name( entityName);
				if ( null == agentObject)
					return false;

				return agentObject.has_same_object_name( variableType, variableValue);
			} else if ( entity.equals( "spot")) {
				SpotObject spotObject = drawObjects.get_spot_has_this_name( entityName);
				if ( null == spotObject)
					return false;

				return spotObject.has_same_object_name( variableType, variableValue);
			}
		}
		return false;
	}

	/**
	 * @param entity
	 * @param entityName
	 * @param agentVariable
	 * @param spotVariable
	 * @param numberObjectType
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_number_object(String entity, String entityName, String agentVariable, String spotVariable, String numberObjectType, Layer drawObjects) {
		if ( entity.equals( "self")) {
			if ( agentVariable.equals( "") && spotVariable.equals( ""))
				return drawObjects.is_number_object_correct( "agent", _variableValue, numberObjectType, null);
			else {
				if ( !agentVariable.equals( ""))
					return drawObjects.is_number_object_correct( "agent", _variableValue, numberObjectType, null);
				else if ( !spotVariable.equals( ""))
					return drawObjects.is_number_object_correct( "spot", _variableValue, numberObjectType, null);
			}
		} else if ( entity.equals( "agent")) {
			if ( agentVariable.equals( "") && spotVariable.equals( "")) {
				AgentObject agentObject = drawObjects.get_agent_has_this_name( entityName);
				if ( null == agentObject)
					return false;

				return agentObject.is_number_object_correct( _variableValue, numberObjectType);
			} else {
				if ( !agentVariable.equals( ""))
					return drawObjects.is_number_object_correct( "agent", _variableValue, numberObjectType, null);
				else if ( !spotVariable.equals( ""))
					return drawObjects.is_number_object_correct( "spot", _variableValue, numberObjectType, null);
			}
		} else if ( entity.equals( "spot")) {
			if ( agentVariable.equals( "") && spotVariable.equals( "")) {
				SpotObject spotObject = drawObjects.get_spot_has_this_name( entityName);
				if ( null == spotObject)
					return false;

				return spotObject.is_number_object_correct( _variableValue, numberObjectType);
			} else {
				if ( !agentVariable.equals( ""))
					return drawObjects.is_number_object_correct( "agent", _variableValue, numberObjectType, null);
				else if ( !spotVariable.equals( ""))
					return drawObjects.is_number_object_correct( "spot", _variableValue, numberObjectType, null);
			}
		} else if ( entity.equals( "currentspot")) {
			if ( agentVariable.equals( "") && spotVariable.equals( ""))
				return drawObjects.is_number_object_correct( "spot", _variableValue, numberObjectType, null);
			else {
				if ( !agentVariable.equals( ""))
					return drawObjects.is_number_object_correct( "agent", _variableValue, numberObjectType, null);
				else if ( !spotVariable.equals( ""))
					return drawObjects.is_number_object_correct( "spot", _variableValue, numberObjectType, null);
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_function(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_function(String originalFunctionName, String newFunctionName) {
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_expression(soars.application.visualshell.object.expression.VisualShellExpressionManager)
	 */
	@Override
	public boolean update_expression(VisualShellExpressionManager visualShellExpressionManager) {
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#is_number_object_type_correct(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean is_number_object_type_correct(String entityType, String numberObjectName, String numberObjectNewType) {
		if ( _variableType.equals( "") || ( !_variableType.equals( "integer number object") && !_variableType.equals( "real number object")) || !_variableValue.equals( numberObjectName))
			return true;

		return is_number_object_type_correct( entityType, _entity, _agentVariable, _spotVariable, numberObjectName, numberObjectNewType);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#is_number_object_type_correct(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public boolean is_number_object_type_correct(String entityType, String numberObjectName, String numberObjectNewType, EntityVariableRule subject) {
		if ( _variableType.equals( "") || ( !_variableType.equals( "integer number object") && !_variableType.equals( "real number object")) || !_variableValue.equals( numberObjectName))
			return true;

		if ( !_entity.equals( _null))
			return is_number_object_type_correct( entityType, _entity, _agentVariable, _spotVariable, numberObjectName, numberObjectNewType);
		else
			return ( !_agentVariable.equals( "") || !_spotVariable.equals( ""))
				? is_number_object_type_correct( entityType, subject._entity, _agentVariable, _spotVariable, numberObjectName, numberObjectNewType)
				: is_number_object_type_correct( entityType, subject._entity, subject._agentVariable, subject._spotVariable, numberObjectName, numberObjectNewType);
	}

	/**
	 * @param entityType
	 * @param entity
	 * @param agentVariable
	 * @param spotVariable
	 * @param numberObjectName
	 * @param numberObjectNewType
	 * @return
	 */
	private boolean is_number_object_type_correct(String entityType, String entity, String agentVariable, String spotVariable, String numberObjectName, String numberObjectNewType) {
		if ( entity.equals( "self") || entity.equals( "agent")) {
			if ( spotVariable.equals( "")) {
				if ( entityType.equals( "agent") && !_variableType.startsWith( numberObjectNewType))
					return false;
			} else {
				if ( entityType.equals( "spot") && !_variableType.startsWith( numberObjectNewType))
					return false;
			}
		} else if ( entity.equals( "spot") || entity.equals( "currentspot")) {
			if ( agentVariable.equals( "")) {
				if ( entityType.equals( "spot") && !_variableType.startsWith( numberObjectNewType))
					return false;
			} else {
				if ( entityType.equals( "agent") && !_variableType.startsWith( numberObjectNewType))
					return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_stage_manager()
	 */
	@Override
	public boolean update_stage_manager() {
		if ( !_variableType.equals( "stage") || _variableValue.equals( ""))
			return false;

		StageManager.get_instance().append_main_stage( _variableValue);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_script(soars.application.visualshell.object.experiment.InitialValueMap, soars.application.visualshell.object.role.base.Role, boolean)
	 */
	@Override
	public String get_script(InitialValueMap initialValueMap, Role role, boolean isSubject) {
		if ( Variable._independentVariableTypes.contains( _variableType))
			return _variableValue;

		if ( Variable._immediateTypes.contains( _variableType)) {
			if ( null == initialValueMap)
				return ( "\"" + _variableValue + "\"");
			else {
				String value = initialValueMap.get( _variableValue);
				if ( null == value)
					return ( "\"" + _variableValue + "\"");
				else
					return ( "\"" + value + "\"");
			}
		}

		// TODO 2013.9.4
		//String text = _entityName;
		String text;
//		if ( !_agentVariable.equals( "") || !_spotVariable.equals( ""))
//			text = _entityName;
		// TODO 2013.8.27
		//else
		//	text = _entity.equals( "currentspot") ? "__currentspot" : _entityName;
//		else {
			if ( _entity.equals( "self"))
				// TODO 2013.9.3
				//text = "__self";
				text = isSubject ? "" : "__self";
			// TODO 2013.9.3
			//else if ( _entity.equals( "currentspot"))
			//	text = ( role instanceof SpotRole) ? "__self" : "__currentspot";
			//else
			else if ( _entity.equals( "currentspot")) {
				if ( role instanceof SpotRole)
					text = isSubject ? "" : "__self";
				else
					text = "__currentspot";
			} else
				text = _entityName;
//		}

		return get_script( text);
	}

	/**
	 * @param text
	 * @return
	 */
	private String get_script(String text) {
		// TODO 2013.9.4
		text += ( _agentVariable.equals( "") ? "" : ( /*( _entity.equals( "self") ? "" : ":")*/ ":" + _agentVariable));
		text += ( _spotVariable.equals( "") ? "" : ( /*( _entity.equals( "self") ? "" : ":")*/ ":" + _spotVariable));
		if ( !_variableType.equals( "") && !_variableValue.equals( ""))
			// TODO 2013.8.27
			text += ( ( text.equals( "") ? "" : ".") + _variableValue);
			//text += ( "." + _variableValue);
		return text;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_script(soars.application.visualshell.object.experiment.InitialValueMap, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public String get_script(InitialValueMap initialValueMap, EntityVariableRule subject, Role role) {
		if ( !_entity.equals( _null))
			return get_script( initialValueMap, role, false);
		else {
			if ( Variable._independentVariableTypes.contains( _variableType))
				return _variableValue;

			if ( Variable._immediateTypes.contains( _variableType)) {
				if ( null == initialValueMap)
					return ( "\"" + _variableValue + "\"");
				else {
					String value = initialValueMap.get( _variableValue);
					if ( null == value)
						return ( "\"" + _variableValue + "\"");
					else
						return ( "\"" + value + "\"");
				}
			}

			// TODO 2013.9.4
			//String text = subject._entityName;
			String text;
//			if ( !subject._agentVariable.equals( "") || !subject._spotVariable.equals( ""))
//				text = subject._entityName;
			// TODO 2013.8.27
			//else
			//	text = subject._entity.equals( "currentspot") ? "__currentspot" : subject._entityName;
//			else {
				if ( subject._entity.equals( "self"))
					text = "__self";
				else if ( subject._entity.equals( "currentspot"))
					text = ( role instanceof SpotRole) ? "__self" : "__currentspot";
				else
					text = subject._entityName;
//			}

			if ( !subject._agentVariable.equals( "")) {
				text +=  ( /*( subject._entity.equals( "self") ? "" : ":")*/ ":" + subject._agentVariable);
				return get_script( text, subject._entity, true);
			} else if ( !subject._spotVariable.equals( "")) {
				text += ( /*( subject._entity.equals( "self") ? "" : ":")*/ ":" + subject._spotVariable);
				return get_script( text, subject._entity, true);
			}
			return get_script( text, subject._entity, false);
		}
	}

	/**
	 * @param text
	 * @param entity
	 * @param afterEntityVariable
	 * @return
	 */
	private String get_script(String text, String entity, boolean afterEntityVariable) {
		// TODO 2013.9.4
		if ( !_agentVariable.equals( "")) {
			if ( afterEntityVariable)
				text += ( ":" + _agentVariable);
			else
				text +=  ( /*( entity.equals( "self") ? "" : ":")*/ ":" + _agentVariable);
		} else if ( !_spotVariable.equals( "")) {
			if ( afterEntityVariable)
				text += ( ":" + _spotVariable);
			else
				text +=  ( /*( entity.equals( "self") ? "" : ":")*/ ":" + _spotVariable);
		}

		if ( !_variableType.equals( "") && !_variableValue.equals( ""))
			// TODO 2013.8.27
			text += ( ( text.equals( "") ? "" : ".") + _variableValue);
			//text += ( "." + _variableValue);

		return text;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_script_to_set_variables_into_expression_spot(soars.application.visualshell.object.experiment.InitialValueMap, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule, soars.application.visualshell.object.role.base.Role, java.lang.String)
	 */
	@Override
	public String get_script_to_set_variables_into_expression_spot(InitialValueMap initialValueMap, EntityVariableRule subject, Role role, String separator) {
		return "";
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_cell_text(soars.application.visualshell.object.role.base.Role, boolean)
	 */
	@Override
	public String get_cell_text(Role role, boolean isSubject) {
		if ( Variable._independentVariableTypes.contains( _variableType))
			return _variableValue;

		if ( Variable._immediateTypes.contains( _variableType))
			return ( "\"" + _variableValue + "\"");

		// TODO 2013.9.4
		//String text = _entityName;
		String text;
//		if ( !_agentVariable.equals( "") || !_spotVariable.equals( ""))
//			text = _entityName;
		// TODO 2013.8.27
		//else
		//text = _entity.equals( "currentspot") ? "CurrentSpot" : _entityName;
//		else {
			if ( _entity.equals( "self"))
				// TODO 2013.9.3
				//text = "Self";
				text = isSubject ? "" : "Self";
			// TODO 2013.9.3
			//else if ( _entity.equals( "currentspot"))
			//	text = ( role instanceof SpotRole) ? "Self" : "CurrentSpot";
			//else
			else if ( _entity.equals( "currentspot")) {
				if ( role instanceof SpotRole)
					text = isSubject ? "" : "Self";
				else
					text = "CurrentSpot";
			} else
				text = _entityName;
//		}

		return get_cell_text( text);
	}

	/**
	 * @param text
	 * @return
	 */
	private String get_cell_text(String text) {
		// TODO 2013.9.4
		text += ( _agentVariable.equals( "") ? "" : ( /*( _entity.equals( "self") ? "" : ":")*/ ":" + _agentVariable));
		text += ( _spotVariable.equals( "") ? "" : ( /*( _entity.equals( "self") ? "" : ":")*/ ":" + _spotVariable));
		if ( !_variableType.equals( "") && !_variableValue.equals( ""))
			// TODO 2013.8.27
			text += ( ( text.equals( "") ? "" : ".") + _variableValue);
			//text += ( "." + _variableValue);
		return text;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_cell_text(soars.application.visualshell.object.role.base.object.generic.EntityVariableRule, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public String get_cell_text(EntityVariableRule subject, Role role) {
		if ( !_entity.equals( _null))
			// TODO 2013.9.3
			return get_cell_text( role, false);
		else {
			if ( Variable._independentVariableTypes.contains( _variableType))
				return _variableValue;

			if ( Variable._immediateTypes.contains( _variableType))
				return ( "\"" + _variableValue + "\"");

			// TODO 2013.9.4
			//String text = subject._entityName;
			String text;
//			if ( !subject._agentVariable.equals( "") || !subject._spotVariable.equals( ""))
//				text = subject._entityName;
			// TODO 2013.8.27
			//else
			//text = subject._entity.equals( "currentspot") ? "CurrentSpot" : subject._entityName;
//			else {
				if ( subject._entity.equals( "self"))
					text = "Self";
				else if ( subject._entity.equals( "currentspot"))
					text = ( role instanceof SpotRole) ? "Self" : "CurrentSpot";
				else
					text = subject._entityName;
//			}

			if ( !subject._agentVariable.equals( "")) {
				text +=  ( /*( subject._entity.equals( "self") ? "" : ":")*/ ":" + subject._agentVariable);
				return get_cell_text( text, subject._entity, true);
			} else if ( !subject._spotVariable.equals( "")) {
				text += ( /*( subject._entity.equals( "self") ? "" : ":")*/ ":" + subject._spotVariable);
				return get_cell_text( text, subject._entity, true);
			}
			return get_cell_text( text, subject._entity, false);
		}
	}

	/**
	 * @param text
	 * @param entity
	 * @param afterEntityVariable
	 * @return
	 */
	private String get_cell_text(String text, String entity, boolean afterEntityVariable) {
		// TODO 2013.9.4
		if ( !_agentVariable.equals( "")) {
			if ( afterEntityVariable)
				text += ( ":" + _agentVariable);
			else
				text +=  ( /*( entity.equals( "self") ? "" : ":")*/ ":" + _agentVariable);
		} else if ( !_spotVariable.equals( "")) {
			if ( afterEntityVariable)
				text += ( ":" + _spotVariable);
			else
				text +=  ( /*( entity.equals( "self") ? "" : ":")*/ ":" + _spotVariable);
		}

		if ( !_variableType.equals( "") && !_variableValue.equals( ""))
			// TODO 2013.8.27
			text += ( ( text.equals( "") ? "" : ".") + _variableValue);
			//text += ( "." + _variableValue);

		return text;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#write(soars.common.utility.xml.sax.Writer)
	 */
	@Override
	public boolean write(Writer writer) throws SAXException {
		return write( "object", writer);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#write(java.lang.String, soars.common.utility.xml.sax.Writer)
	 */
	@Override
	public boolean write(String name, Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "entity", "", Writer.escapeAttributeCharData( _entity));
		attributesImpl.addAttribute( null, null, "entityName", "", Writer.escapeAttributeCharData( _entityName));
		attributesImpl.addAttribute( null, null, "agentVariable", "", Writer.escapeAttributeCharData( _agentVariable));
		attributesImpl.addAttribute( null, null, "spotVariable", "", Writer.escapeAttributeCharData( _spotVariable));
		attributesImpl.addAttribute( null, null, "variableType", "", Writer.escapeAttributeCharData( _variableType));
		attributesImpl.addAttribute( null, null, "variableValue", "", Writer.escapeAttributeCharData( _variableValue));
		writer.writeElement( null, null, name, attributesImpl);
		return true;
	}

	/**
	 * @return
	 */
	public String get_number_object_type() {
		// TODO 2014.2.19 数値変数のタイプを取得
		// 数式旧データを新データに更新する際に呼ばれる
		if ( _entity.equals( "self")) {
			if ( _agentVariable.equals( "") && _spotVariable.equals( ""))
				return LayerManager.get_instance().get_agent_number_object_type( _variableValue);
			else {
				if ( !_agentVariable.equals( ""))
					return LayerManager.get_instance().get_agent_number_object_type( _variableValue);
				else if ( !_spotVariable.equals( ""))
					return LayerManager.get_instance().get_spot_number_object_type( _variableValue);
			}
		} else if ( _entity.equals( "agent")) {
			if ( _agentVariable.equals( "") && _spotVariable.equals( "")) {
				AgentObject agentObject = LayerManager.get_instance().get_agent_has_this_name( _entityName);
				if ( null == agentObject)
					return null;

				return agentObject.get_number_object_type( _variableValue);
			} else {
				if ( !_agentVariable.equals( ""))
					return LayerManager.get_instance().get_agent_number_object_type( _variableValue);
				else if ( !_spotVariable.equals( ""))
					return LayerManager.get_instance().get_spot_number_object_type( _variableValue);
			}
		} else if ( _entity.equals( "spot")) {
			if ( _agentVariable.equals( "") && _spotVariable.equals( "")) {
				SpotObject spotObject = LayerManager.get_instance().get_spot_has_this_name( _entityName);
				if ( null == spotObject)
					return null;

				return spotObject.get_number_object_type( _variableValue);
			} else {
				if ( !_agentVariable.equals( ""))
					return LayerManager.get_instance().get_agent_number_object_type( _variableValue);
				else if ( !_spotVariable.equals( ""))
					return LayerManager.get_instance().get_spot_number_object_type( _variableValue);
			}
		} else if ( _entity.equals( "currentspot")) {
			if ( _agentVariable.equals( "") && _spotVariable.equals( ""))
				return LayerManager.get_instance().get_spot_number_object_type( _variableValue);
			else {
				if ( !_agentVariable.equals( ""))
					return LayerManager.get_instance().get_agent_number_object_type( _variableValue);
				else if ( !_spotVariable.equals( ""))
					return LayerManager.get_instance().get_spot_number_object_type( _variableValue);
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#print()
	 */
	@Override
	public void print() {
		System.out.println( "Entity=" + _entity + " : " + "Entity name=" + _entityName + " : " + "Agent variable=" + _agentVariable + " : " + "Spot variable=" + _spotVariable + " : " + "Variable type=" + _variableType + " : " + "Variable value=" + _variableValue);
	}
}
