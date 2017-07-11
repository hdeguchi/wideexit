/*
 * Created on 2005/10/31
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.command;

import java.awt.Color;
import java.awt.Frame;

import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.legacy.common.number.NumberObjectPropertyPanelBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.legacy.command.SubstitutionCommand;
import soars.application.visualshell.object.role.base.object.legacy.common.expression.ExpressionElements;
import soars.application.visualshell.object.role.base.object.legacy.common.expression.SubType;

/**
 * @author kurata
 */
public class SubstitutionCommandPropertyPanel extends NumberObjectPropertyPanelBase {

	/**
	 * 
	 */
	protected JLabel _equalLabel = null;

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
	public SubstitutionCommandPropertyPanel(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.number.NumberObjectPropertyPanelBase#setup_number_object_and_operator(javax.swing.JPanel)
	 */
	@Override
	protected void setup_number_object_and_operator(JPanel parent) {
		_equalLabel = create_label( " = ", false);
		setup_number_object_and_operator( _equalLabel, parent);
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

		SubType subType = SubType.get( rule._value, SubstitutionCommand._subTypes);
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
		if ( null == elements || 2 > elements.length)
			return false;

		if ( !set_number_object( elements[ 0], _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox, _numberObjectComboBox))
			return false;

		_valueTextFields[ index].setText( elements[ 1]);

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.number.NumberObjectPropertyPanelBase#set_number_object1(java.lang.String, int)
	 */
	@Override
	protected boolean set_number_object1(String value, int index) {
		String[] elements = value.split( " ");
		if ( null == elements || 2 > elements.length)
			return false;

		if ( !set_number_object( elements[ 0], _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox, _numberObjectComboBox))
			return false;

		_numberObjectComboBoxes[ index].setSelectedItem( elements[ 1]);

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.number.NumberObjectPropertyPanelBase#set_expression1(java.lang.String, int)
	 */
	@Override
	protected boolean set_expression1(String value, int index) {
		String[] elements = value.split( " ");
		if ( null == elements || 2 > elements.length)
			return false;

		ExpressionElements expressionElements = SubstitutionCommand.get1( elements);
		if ( null == expressionElements)
			return false;

		if ( !set_number_object( elements[ 0], _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox, _numberObjectComboBox))
			return false;

		set_expression( expressionElements, index);

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.number.NumberObjectPropertyPanelBase#set_others1(java.lang.String, int)
	 */
	@Override
	protected boolean set_others1(String value, int index) {
		String[] elements = value.split( " ");
		if ( null == elements || 2 > elements.length)
			return false;

		if ( !set_number_object( elements[ 0], _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox, _numberObjectComboBox))
			return false;
	
		_othersTextFields[ index].setText( value.substring( ( elements[ 0] + " ").length()));

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get()
	 */
	@Override
	public Rule get() {
		String number_object1 = get_number_object( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox, _numberObjectComboBox);
		if ( null == number_object1)
			return null;

		return get( number_object1, SubstitutionCommand._subTypes, "");
	}
}
