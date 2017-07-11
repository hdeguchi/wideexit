/**
 * 
 */
package soars.application.visualshell.object.role.base.object.generic.element;

import java.util.List;
import java.util.Vector;

import org.xml.sax.SAXException;

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
public interface IObject {

	/**
	 * @param object
	 * @return
	 */
	boolean same_as(IObject object);

	/**
	 * @param initialValues
	 */
	void get_initial_values(Vector<String> initialValues);

	/**
	 * @param name
	 * @param number
	 * @return
	 */
	boolean has_same_agent_name(String name, String number);

	/**
	 * @param alias
	 * @return
	 */
	boolean contains_this_alias(String alias);

	/**
	 * @param names
	 */
	void get_used_agent_names(List<String> names);

	/**
	 * @param names
	 */
	void get_used_spot_names(List<String> names);

	/**
	 * @param type
	 * @param names
	 */
	void get_used_independent_variable_names(String type, List<String> names);

	/**
	 * @param names
	 */
	void get_used_agent_variable_names(List<String> names);

	/**
	 * @param names
	 * @param subject
	 */
	void get_used_agent_variable_names(List<String> names, EntityVariableRule subject);

	/**
	 * @param names
	 */
	void get_used_spot_variable_names(List<String> names);

	/**
	 * @param names
	 * @param subject
	 */
	void get_used_spot_variable_names(List<String> names, EntityVariableRule subject);

	/**
	 * @param type
	 * @param names
	 */
	void get_used_variable_names(String type, List<String> names);

	/**
	 * @param type
	 * @param names
	 * @param subject
	 */
	void get_used_variable_names(String type, List<String> names, EntityVariableRule subject);

	/**
	 * @param names
	 */
	void get_used_expressions(List<String> names);

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
	boolean update_agent_or_spot_name_and_number(String type, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges);

	/**
	 * @param originalName
	 * @param newName
	 * @return
	 */
	boolean update_role_name(String originalName, String newName);

	/**
	 * @param string
	 * @param originalName
	 * @param newName
	 * @return
	 */
	boolean update_independent_variable_name(String type, String originalName, String newName);

	/**
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @return
	 */
	public boolean update_agent_variable_name(String originalName, String newName, String entityType);

	/**
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @param subject
	 * @return
	 */
	boolean update_agent_variable_name(String originalName, String newName, String entityType, EntityVariableRule subject);

	/**
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @return
	 */
	public boolean update_spot_variable_name(String originalName, String newName, String entityType);

	/**
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @param subject
	 * @return
	 */
	boolean update_spot_variable_name(String originalName, String newName, String entityType, EntityVariableRule subject);

	/**
	 * @param type
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @return
	 */
	boolean update_variable_name(String type, String originalName, String newName, String entityType);

	/**
	 * @param type
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @param subject
	 * @return
	 */
	boolean update_variable_name(String type, String originalName, String newName, String entityType, EntityVariableRule subject);

	/**
	 * @param originalFunctionName
	 * @param newFunctionName
	 */
	boolean update_function(String originalFunctionName, String newFunctionName);

	/**
	 * @param visualShellExpressionManager
	 * @return
	 */
	boolean update_expression(VisualShellExpressionManager visualShellExpressionManager);

	/**
	 * @param newExpression
	 * @param newVariableCount
	 * @param originalExpression
	 * @return
	 */
	boolean update_expression(Expression newExpression, int newVariableCount, Expression originalExpression);

	/**
	 * @param drawObjects
	 * @return
	 */
	boolean can_paste(Layer drawObjects);

	/**
	 * @param drawObjects
	 * @param subject
	 * @return
	 */
	boolean can_paste(Layer drawObjects, EntityVariableRule subject);

	/**
	 * @param entityType
	 * @param numberObjectName
	 * @param newType
	 * @return
	 */
	boolean is_number_object_type_correct(String entityType, String numberObjectName, String numberObjectNewType);

	/**
	 * @param entityType
	 * @param numberObjectName
	 * @param newType
	 * @param subject
	 * @return
	 */
	boolean is_number_object_type_correct(String entityType, String numberObjectName, String numberObjectNewType, EntityVariableRule subject);

	/**
	 * @return
	 */
	boolean update_stage_manager();
	/**
	 * @param initialValueMap
	 * @param role
	 * @param isSubject
	 * @return
	 */
	String get_script(InitialValueMap initialValueMap, Role role, boolean isSubject);

	/**
	 * @param initialValueMap
	 * @param subject
	 * @param role
	 * @return
	 */
	String get_script(InitialValueMap initialValueMap, EntityVariableRule subject, Role role);

	/**
	 * @param initialValueMap
	 * @param subject
	 * @param role
	 * @param separator
	 * @return
	 */
	String get_script_to_set_variables_into_expression_spot(InitialValueMap initialValueMap, EntityVariableRule subject, Role role, String separator);

	/**
	 * @param role
	 * @param isSubject
	 * @return
	 */
	String get_cell_text(Role role, boolean isSubject);

	/**
	 * @param subject
	 * @param role
	 * @return
	 */
	String get_cell_text(EntityVariableRule subject, Role role);

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	boolean write(Writer writer) throws SAXException;

	/**
	 * @param name
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	boolean write(String name, Writer writer) throws SAXException;

	/**
	 * 
	 */
	void print();
}
