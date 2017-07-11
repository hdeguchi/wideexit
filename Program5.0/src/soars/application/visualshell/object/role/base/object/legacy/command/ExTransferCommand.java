/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.Rules;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;

/**
 * @author kurata
 *
 */
public class ExTransferCommand extends Rule {

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public ExTransferCommand(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_spot_names()
	 */
	@Override
	protected String[] get_used_spot_names() {
		String[] elements = CommonRuleManipulator.get_elements( "dummy " + _value, 3);	// 苦肉の策
		if ( null == elements)
			return null;

		return new String[] { CommonRuleManipulator.extract_spot_name2( elements[ 0])};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_spot_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_spot_variable_names(Role role) {
		String[] elements = CommonRuleManipulator.get_elements( "dummy " + _value, 3);	// 苦肉の策
		if ( null == elements)
			return null;

		return new String[] { CommonRuleManipulator.get_spot_variable_name2( elements[ 0])};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_extransfer_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_extransfer_names(Role role) {
		String[] elements = CommonRuleManipulator.get_elements( "dummy " + _value, 3);	// 苦肉の策
		if ( null == elements)
			return null;

		return new String[] { elements[ 0]};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_exchange_algebra_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_exchange_algebra_names(Role role) {
		return get_object_names( "exchange algebra");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_used_class_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_class_variable_names(Role role) {
		return get_object_names( "class variable");
	}

	/**
	 * @param kind
	 * @return
	 */
	private String[] get_object_names(String kind) {
		String[] elements = CommonRuleManipulator.get_elements( "dummy " + _value, 3);	// 苦肉の策
		if ( null == elements)
			return null;

		List<String> objectNames = new ArrayList<String>();

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return null;

		if ( CommonRuleManipulator.is_object( kind, prefix + elements[ 1], LayerManager.get_instance()))
			objectNames.add( prefix + elements[ 1]);

		if ( CommonRuleManipulator.is_object( kind, prefix + elements[ 2], LayerManager.get_instance()))
			objectNames.add( prefix + elements[ 2]);

		return objectNames.toArray( new String[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		String[] elements = CommonRuleManipulator.get_elements( "dummy " + _value, 3);	// 苦肉の策
		if ( null == elements)
			return false;

		elements[ 0] = CommonRuleManipulator.update_spot_name2( elements[ 0], newName, originalName, headName, ranges, newHeadName, newRanges);
		if ( null == elements[ 0])
			return false;

		_value = ( elements[ 0] + "=" + elements[ 1] + "=" + elements[ 2]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_spot_variable_name(String originalName, String newName, String entityType, Role role) {
		String[] elements = CommonRuleManipulator.get_elements( "dummy " + _value, 3);	// 苦肉の策
		if ( null == elements)
			return false;

		elements[ 0] = CommonRuleManipulator.update_spot_variable_name2( elements[ 0], originalName, newName, entityType);
		if ( null == elements[ 0])
			return false;

		_value = ( elements[ 0] + "=" + elements[ 1] + "=" + elements[ 2]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_extransfer_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_extransfer_name(String originalName, String newName, String entityType, Role role) {
		String[] elements = CommonRuleManipulator.get_elements( "dummy " + _value, 3);	// 苦肉の策
		if ( null == elements)
			return false;

		String element = CommonRuleManipulator.update_object_name( elements[ 0], originalName, newName, entityType);
		if ( null == element)
			return false;

		elements[ 0] = element;

		_value = ( elements[ 0] + "=" + elements[ 1] + "=" + elements[ 2]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_exchange_algebra_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_exchange_algebra_name(String originalName, String newName, String entityType, Role role) {
		return update_object_name( originalName, newName, entityType, role);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#update_class_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_class_variable_name(String originalName, String newName, String entityType, Role role) {
		return update_object_name(originalName, newName, entityType, role);
	}

	/**
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @param role
	 * @return
	 */
	private boolean update_object_name(String originalName, String newName, String entityType, Role role) {
		String[] elements = CommonRuleManipulator.get_elements( "dummy " + _value, 3);	// 苦肉の策
		if ( null == elements)
			return false;

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return false;

		boolean result = false;

		if ( CommonRuleManipulator.correspond( prefix, elements[ 1], originalName, entityType)) {
			elements[ 1] = newName;
			result = true;
		}

		if ( CommonRuleManipulator.correspond( prefix, elements[ 2], originalName, entityType)) {
			elements[ 2] = newName;
			result = true;
		}

		if ( result)
			_value = ( elements[ 0] + "=" + elements[ 1] + "=" + elements[ 2]);

		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#can_paste(int, soars.application.visualshell.object.role.base.object.Rules, soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	@Override
	public boolean can_paste(int row, Rules rules, Role role, Layer drawObjects) {
		String[] elements = CommonRuleManipulator.get_elements( "dummy " + _value, 3);	// 苦肉の策
		if ( null == elements)
			return false;

		boolean result1 = CommonRuleManipulator.can_paste_object( "extransfer", elements[ 0], drawObjects);

		String prefix = CommonRuleManipulator.get_full_prefix( elements[ 0]);
		if ( null == prefix)
			return false;
		
		boolean result2 = ( CommonRuleManipulator.is_object( "exchange algebra", prefix + elements[ 1], drawObjects)
			|| CommonRuleManipulator.is_object( "class variable", prefix + elements[ 1], drawObjects));

		boolean result3 = ( CommonRuleManipulator.is_object( "exchange algebra", prefix + elements[ 2], drawObjects)
			|| CommonRuleManipulator.is_object( "class variable", prefix + elements[ 2], drawObjects));

		return ( result1 && result2 && result3);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String get_script(String value, Role role) {
		// TODO Auto-generated method stub
		String[] elements = CommonRuleManipulator.get_elements( "dummy " + _value, 3);	// 苦肉の策
		if ( null == elements)
			return "";

		String[] prefixAndObject = CommonRuleManipulator.get_prefix_and_object( elements[ 0]);

		String script = ( prefixAndObject[ 0] + "addParam " + Constant._aadlFunctionsClassVariableName + "=" + elements[ 1] + "=" + Constant._exchangeAlgebraClassname);
		script += ( " ; " + prefixAndObject[ 0] + "addParam " + Constant._aadlFunctionsClassVariableName + "=" + prefixAndObject[ 1] + "=" + Constant._exTransferClassname);
		script += ( " ; " + prefixAndObject[ 0] + "invokeClass " + elements[ 2] + "=" + Constant._aadlFunctionsClassVariableName + "=" + Constant._exTransferTransferMethodName);

		return script;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#get_cell_text(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public String get_cell_text(Role role) {
		String[] elements = CommonRuleManipulator.get_elements( "dummy " + _value, 3);	// 苦肉の策
		if ( null == elements)
			return _value;

		return ( elements[ 0] + "(" + elements[ 1] + "->" + elements[ 2] + ")");
	}
}
