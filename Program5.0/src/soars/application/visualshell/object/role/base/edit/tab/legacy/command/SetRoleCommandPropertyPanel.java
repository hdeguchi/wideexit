/*
 * Created on 2005/12/01
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.command;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.legacy.command.SetRoleCommand;
import soars.application.visualshell.object.role.base.object.legacy.condition.RoleCondition;
import soars.common.utility.swing.combo.ComboBox;

/**
 * @author kurata
 */
public class SetRoleCommandPropertyPanel extends RulePropertyPanelBase {

	/**
	 * 
	 */
	private ComboBox _roleVariableComboBox = null;

	/**
	 * 
	 */
	private ComboBox _roleComboBox = null;

//	private String _originalRoleVariable = "";

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
	public SetRoleCommandPropertyPanel(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
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

		JLabel label = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.set.role.variable.label"),
			true);
		panel.add( label);

		_roleVariableComboBox = create_comboBox(
			get_agent_role_variable_names( false), _standardControlWidth, false);
		panel.add( _roleVariableComboBox);

		label = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.set.role.label"),
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
//		_original_role_variable = "";

		if ( null == rule || !_type.equals( rule._type) || rule._value.equals( ""))
			return false;

		String[] elements = RoleCondition.get_elements( rule._value);
		if ( null == elements)
			return false;
		else {
			switch ( elements.length) {
				case 1:
					_roleVariableComboBox.setSelectedItem( elements[ 0]);
					_roleComboBox.setSelectedItem( "");
//					_original_role_variable = elements[ 0];
					break;
				case 2:
					_roleVariableComboBox.setSelectedItem( elements[ 0]);
					_roleComboBox.setSelectedItem( elements[ 1]);
//					_original_role_variable = elements[ 0];
					break;
				default:
					return false;
			}
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get()
	 */
	@Override
	public Rule get() {
		String roleVariable = ( String)_roleVariableComboBox.getSelectedItem();
		if ( null == roleVariable || roleVariable.equals( "")
			//|| roleVariable.equals( Constant._spot_chart_role_name)
			|| LayerManager.get_instance().is_role_name( roleVariable))
			return null;

//		if ( !_original_role_variable.equals( "")
//			&& !role_variable.equals( _original_role_variable)
//			&& !_parent.contains_this_agent_role_variable( _original_role_variable)) {
//			WarningManager.get_instance().cleanup();
//			if ( _parent.use_this_role_variable( _original_role_variable)) {
//				WarningDlg1 warningDlg1 = new WarningDlg1(
//					_owner,
//					ResourceManager.get_instance().get( "warning.dialog1.title"),
//					ResourceManager.get_instance().get( "warning.dialog1.message1"));
//				warningDlg1.do_modal();
//				return false;
//			}
//		}

		String role = ( String)_roleComboBox.getSelectedItem();

		String value = roleVariable;
		if ( null != role && !role.equals( ""))
			value += ( "=" + role);

//		_original_role_variable = role_variable;

		return Rule.create( _kind, _type, SetRoleCommand._reservedWord + value);
	}
}
