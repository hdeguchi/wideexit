/*
 * Created on 2005/12/01
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.condition;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.legacy.condition.RoleCondition;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.combo.ComboBox;

/**
 * @author kurata
 */
public class RoleConditionPropertyPanel extends RulePropertyPanelBase {

	/**
	 * 
	 */
	private CheckBox _checkBox = null;

	/**
	 * 
	 */
	private ComboBox _roleVariableComboBox = null;

	/**
	 * 
	 */
	private ComboBox _roleComboBox = null;

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
	public RoleConditionPropertyPanel(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.panel.StandardPanel#on_create()
	 */
	@Override
	protected boolean on_create() {
		if ( !super.on_create())
			return false;


		setLayout( new BorderLayout());


		JPanel basicPanel = new JPanel();
		basicPanel.setLayout( new BorderLayout());


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup( northPanel);

		insert_vertical_strut( northPanel);

		basicPanel.add( northPanel, "North");


		add( basicPanel);


		setup_apply_button();


		return true;
	}

	/**
	 * @param parent
	 */
	private void setup(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_checkBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.check.box.denial"),
			true, false);
		panel.add( _checkBox);

		JLabel label = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.role.variable.label"),
			true);
		panel.add( label);

		_roleVariableComboBox = create_comboBox(
			get_agent_role_variable_names( true),
//			_parent.get_agent_role_variables( true, true),
			_standardControlWidth, false);
		panel.add( _roleVariableComboBox);

		label = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.role.label"),
			true);
		panel.add( label);

		_roleComboBox = create_comboBox(
			get_agent_role_names( true), _standardControlWidth, false);
		panel.add( _roleComboBox);

		parent.add( panel);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#on_setup_completed()
	 */
	@Override
	public void on_setup_completed() {
		super.on_setup_completed();
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.panel.StandardPanel#on_ok(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_ok(ActionEvent actionEvent) {
		_parent.on_apply( this, actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#set(soars.application.visualshell.object.role.base.rule.base.Rule)
	 */
	@Override
	public boolean set(Rule rule) {
		if ( null == rule || !_type.equals( rule._type) || rule._value.equals( ""))
			return false;

		String[] elements = RoleCondition.get_elements( rule._value);
		if ( null == elements) {
			_roleVariableComboBox.setSelectedItem( "");
			_roleComboBox.setSelectedItem( "");
		} else {
			switch ( elements.length) {
				case 1:
					_roleVariableComboBox.setSelectedItem( "");
					_roleComboBox.setSelectedItem( elements[ 0]);
					break;
				case 2:
					_roleVariableComboBox.setSelectedItem( elements[ 0]);
					_roleComboBox.setSelectedItem( elements[ 1]);
					break;
				default:
					return false;
			}
		}

		_checkBox.setSelected( rule._value.startsWith( "!"));

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get()
	 */
	@Override
	public Rule get() {
		String roleVariable = ( String)_roleVariableComboBox.getSelectedItem();
		String role = ( String)_roleComboBox.getSelectedItem();

		String value = "";
		if ( null == roleVariable || roleVariable.equals( "")) {
			if ( null != role && !role.equals( ""))
				value = role;
		} else {
			if ( null == role || role.equals( ""))
				return null;
			else {
				value = ( roleVariable + "=" + role);
			}
		}

		return Rule.create( _kind, _type,
			( _checkBox.isSelected() ? "!" : "") + RoleCondition._reservedWord + value);
	}
}
