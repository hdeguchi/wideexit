/**
 * 
 */
package soars.application.visualshell.object.role.base.object.generic.element;

import java.util.List;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.role.base.Role;
import soars.common.utility.tool.expression.Expression;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class ConstantRule implements IObject {

	/**
	 * 
	 */
	public String _value = "";

	/**
	 * 
	 */
	public ConstantRule() {
		super();
	}

	/**
	 * @param value
	 */
	public ConstantRule(String value) {
		super();
		_value = value;
	}

	/**
	 * @param constant
	 */
	public ConstantRule(ConstantRule constant) {
		super();
		_value = constant._value;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#same_as(soars.application.visualshell.object.role.base.object.generic.element.IObject)
	 */
	@Override
	public boolean same_as(IObject object) {
		if ( !( object instanceof ConstantRule))
			return false;

		ConstantRule constant = ( ConstantRule)object;
		return _value.equals( constant._value);
	}
 
	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_initial_values(java.util.Vector)
	 */
	@Override
	public void get_initial_values(Vector<String> initialValues) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#has_same_agent_name(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean has_same_agent_name(String name, String number) {
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#contains_this_alias(java.lang.String)
	 */
	@Override
	public boolean contains_this_alias(String alias) {
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_agent_names(java.util.List)
	 */
	@Override
	public void get_used_agent_names(List<String> names) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_spot_names(java.util.List)
	 */
	@Override
	public void get_used_spot_names(List<String> names) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_independent_variable_names(java.lang.String, java.util.List)
	 */
	@Override
	public void get_used_independent_variable_names(String type, List<String> names) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_agent_variable_names(java.util.List)
	 */
	@Override
	public void get_used_agent_variable_names(List<String> names) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_agent_variable_names(java.util.List, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public void get_used_agent_variable_names(List<String> names, EntityVariableRule subject) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_spot_variable_names(java.util.List)
	 */
	@Override
	public void get_used_spot_variable_names(List<String> names) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_spot_variable_names(java.util.List, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public void get_used_spot_variable_names(List<String> names, EntityVariableRule subject) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_variable_names(java.lang.String, java.util.List)
	 */
	@Override
	public void get_used_variable_names(String type, List<String> names) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_used_variable_names(java.lang.String, java.util.List, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public void get_used_variable_names(String type, List<String> names, EntityVariableRule subject) {
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
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_role_name(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_role_name(String originalName, String newName) {
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_independent_variable_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_independent_variable_name(String type, String originalName, String newName) {
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_agent_variable_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_agent_variable_name(String originalName, String newName, String entityType) {
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_agent_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public boolean update_agent_variable_name(String originalName, String newName, String entityType, EntityVariableRule subject) {
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_spot_variable_name(String originalName, String newName, String entityType) {
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public boolean update_spot_variable_name(String originalName, String newName, String entityType, EntityVariableRule subject) {
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_variable_name(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_variable_name(String type, String originalName, String newName, String entityType) {
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_variable_name(java.lang.String, java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public boolean update_variable_name(String type, String originalName, String newName, String entityType, EntityVariableRule subject) {
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
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#can_paste(soars.application.visualshell.layer.Layer, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public boolean can_paste(Layer drawObjects, EntityVariableRule subject) {
		return true;
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
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#is_number_object_type_correct(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule)
	 */
	@Override
	public boolean is_number_object_type_correct(String entityType, String numberObjectName, String numberObjectNewType, EntityVariableRule subject) {
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#update_stage_manager()
	 */
	@Override
	public boolean update_stage_manager() {
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_script(soars.application.visualshell.object.experiment.InitialValueMap, soars.application.visualshell.object.role.base.Role, boolean)
	 */
	@Override
	public String get_script(InitialValueMap initialValueMap, Role role, boolean isSubject) {
		return _value;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_script(soars.application.visualshell.object.experiment.InitialValueMap, soars.application.visualshell.object.role.base.object.generic.EntityVariableRule, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public String get_script(InitialValueMap initialValueMap, EntityVariableRule subject, Role role) {
		return _value;
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
		return _value;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#get_cell_text(soars.application.visualshell.object.role.base.object.generic.EntityVariableRule, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public String get_cell_text(EntityVariableRule subject, Role role) {
		return _value;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#write(soars.common.utility.xml.sax.Writer)
	 */
	@Override
	public boolean write(Writer writer) throws SAXException {
		return write( "constant", writer);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#write(java.lang.String, soars.common.utility.xml.sax.Writer)
	 */
	@Override
	public boolean write(String name, Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "value", "", Writer.escapeAttributeCharData( _value));
		writer.writeElement( null, null, name, attributesImpl);
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.generic.element.IObject#print()
	 */
	@Override
	public void print() {
		System.out.println( "Value=" + _value);
	}
}
