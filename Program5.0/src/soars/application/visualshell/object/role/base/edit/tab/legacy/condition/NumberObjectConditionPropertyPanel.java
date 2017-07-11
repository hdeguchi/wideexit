/*
 * Created on 2006/03/06
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.condition;

import java.awt.Color;
import java.awt.Frame;

import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.legacy.common.number.NumberObjectPropertyPanelBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.legacy.common.expression.ExpressionElements;
import soars.application.visualshell.object.role.base.object.legacy.common.expression.SubType;
import soars.application.visualshell.object.role.base.object.legacy.condition.NumberObjectCondition;
import soars.common.utility.swing.combo.ComboBox;

/**
 * @author kurata
 */
public class NumberObjectConditionPropertyPanel extends NumberObjectPropertyPanelBase {

	/**
	 * 
	 */
	protected ComboBox _operatorComboBox2 = null;

	/**
	 * @param title
	 * @param kind
	 * @param type
	 * @param color
	 * @param role
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public NumberObjectConditionPropertyPanel(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.number.NumberObjectPropertyPanelBase#setup_comparative_operator(javax.swing.JPanel)
	 */
	@Override
	protected void setup_comparative_operator(JPanel panel) {
		JLabel label = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.number.object.label.comparative.operator"),
			false);
		panel.add( label);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.number.NumberObjectPropertyPanelBase#setup_number_object_and_operator(javax.swing.JPanel)
	 */
	@Override
	protected void setup_number_object_and_operator(JPanel parent) {
		_operatorComboBox2 = create_comboBox( new String[] { ">", ">=", "==", "!=", "<=", "<"}, 70, true);
		setup_number_object_and_operator( _operatorComboBox2, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#set(soars.application.visualshell.object.role.base.rule.base.Rule)
	 */
	@Override
	public boolean set(Rule rule) {
		initialize();

		if ( null == rule || !_type.equals( rule._type) || rule._value.equals( "")) {
			set_handler();
			return false;
		}

		SubType subType = SubType.get( rule._value, NumberObjectCondition._subTypes);
		if ( null == subType) {
			set_handler();
			return false;
		}

		if ( !set( rule._value, subType)) {
			set_handler();
			return false;
		}

		set_handler();

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.number.NumberObjectPropertyPanelBase#set_value1(java.lang.String, int)
	 */
	@Override
	protected boolean set_value1(String value, int index) {
		String[] elements = value.split( " ");
		if ( null == elements || 3 > elements.length)
			return false;

		if ( !set_number_object( elements[ 0], _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox, _numberObjectComboBox))
			return false;

		_operatorComboBox2.setSelectedItem( elements[ 1]);

		_valueTextFields[ index].setText( elements[ 2]);

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.number.NumberObjectPropertyPanelBase#set_number_object1(java.lang.String, int)
	 */
	@Override
	protected boolean set_number_object1(String value, int index) {
		String[] elements = value.split( " ");
		if ( null == elements || 3 > elements.length)
			return false;

		if ( !set_number_object( elements[ 0], _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox, _numberObjectComboBox))
			return false;

		_operatorComboBox2.setSelectedItem( elements[ 1]);

		_numberObjectComboBoxes[ index].setSelectedItem( elements[ 2]);

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.number.NumberObjectPropertyPanelBase#set_expression1(java.lang.String, int)
	 */
	@Override
	protected boolean set_expression1(String value, int index) {
		String[] elements = value.split( " ");
		if ( null == elements || 3 > elements.length)
			return false;

		ExpressionElements expressionElements = NumberObjectCondition.get1( elements);
		if ( null == expressionElements)
			return false;

		if ( !set_number_object( elements[ 0], _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox, _numberObjectComboBox))
			return false;

		_operatorComboBox2.setSelectedItem( elements[ 1]);

		set_expression( expressionElements, index);

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.number.NumberObjectPropertyPanelBase#set_others1(java.lang.String, int)
	 */
	@Override
	protected boolean set_others1(String value, int index) {
		String[] elements = value.split( " ");
		if ( null == elements || 3 > elements.length)
			return false;

		if ( !set_number_object( elements[ 0], _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox, _numberObjectComboBox))
			return false;

		_operatorComboBox2.setSelectedItem( elements[ 1]);

		_othersTextFields[ index].setText( value.substring( ( elements[ 0] + " " + elements[ 1] + " ").length()));

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get()
	 */
	@Override
	public Rule get() {
		String numberObject1 = get_number_object( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox, _numberObjectComboBox);
		if ( null == numberObject1)
			return null;

		String operator2 = ( String)_operatorComboBox2.getSelectedItem();
		if ( null == operator2 || operator2.equals( ""))
			return null;

		return get( numberObject1, NumberObjectCondition._subTypes, " " + operator2);
	}
}
