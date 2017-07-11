/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.common.expression;

import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.generic.element.EntityVariableRule;
import soars.application.visualshell.object.role.base.object.generic.element.ExpressionRule;

/**
 * @author kurata
 *
 */
public class Variable {

	/**
	 * 
	 */
	public String _name = "";

	/**
	 * 
	 */
	public EntityVariableRule _entityVariableRule = null;

	/**
	 * @param name
	 * @param role
	 */
	public Variable(String name, Role role) {
		super();
		_name = name;
		_entityVariableRule = ExpressionRule.get_default_entityVariableRule( role);
	}

	/**
	 * @param variable
	 */
	public Variable(Variable variable) {
		super();
		_name = variable._name;
		_entityVariableRule = new EntityVariableRule( variable._entityVariableRule);
	}

	/**
	 * @param entityVariableRule
	 */
	public void set(EntityVariableRule entityVariableRule) {
		_entityVariableRule.copy( entityVariableRule);
	}
}
