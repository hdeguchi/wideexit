/*
 * 2005/08/19
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.base;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.common.utility.swing.button.CheckBox;

/**
 * @author kurata
 */
public class SelectRuleValuePropertyPanel4 extends RulePropertyPanelBase {

	/**
	 * 
	 */
	private boolean _canEmpty = true;

	/**
	 * 
	 */
	private String _agentOrSpot = "";

	/**
	 * 
	 */
	private String _label = "";

	/**
	 * 
	 */
	private CheckBox _denialCheckBox = null;

	/**
	 * 
	 */
	private ObjectSelector _objectSelector = null;

	/**
	 * @param agentOrSpot
	 * @param title
	 * @param kind
	 * @param type
	 * @param color
	 * @param role
	 * @param index
	 * @param label
	 * @param canEmpty
	 * @param owner
	 * @param parent
	 */
	public SelectRuleValuePropertyPanel4(String agentOrSpot, String title, String kind, String type, Color color, Role role, int index, String label, boolean canEmpty, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
		_agentOrSpot = agentOrSpot;
		_label = label;
		_canEmpty = canEmpty;
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

		if ( !setup( northPanel))
			return false;

		basicPanel.add( northPanel, "North");


		add( basicPanel);


		setup_apply_button();


		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_denialCheckBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.check.box.denial"),
			true, false);
		panel.add( _denialCheckBox);

		JLabel label = create_label( _label, false);
		panel.add( label);

		if ( _agentOrSpot.equals( "agent"))
			_objectSelector = create_agent_selector( _canEmpty, /*400,*/ true, panel);
		else if ( _agentOrSpot.equals( "spot"))
			_objectSelector = create_spot_selector( _canEmpty, /*400,*/ true, panel);
		else
			return false;

		parent.add( panel);

		return true;
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
		if ( null == rule || !_type.equals( rule._type))
			return false;

		_denialCheckBox.setSelected( rule._value.startsWith( "!"));

		_objectSelector.set( rule._value.substring( ( rule._value.startsWith( "!")) ? ( "!".length()) : 0));

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get()
	 */
	@Override
	public Rule get() {
		String value = _objectSelector.get();
		if ( !_canEmpty && ( null == value || value.equals( "")))
			return null;

		return Rule.create( _kind, _type, ( _denialCheckBox.isSelected() ? "!" : "") + value);
	}

//	/* (Non Javadoc)
//	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get(soars.application.visualshell.object.role.base.rule.base.Rule)
//	 */
//	@Override
//	public boolean get(Rule rule) {
//		if ( !super.get(rule))
//			return false;
//
//		String value = _objectSelector.get();
//		if ( !_canEmpty && ( null == value || value.equals( "")))
//			return false;
//
//		rule._value = ( ( _denialCheckBox.isSelected() ? "!" : "") + value);
//		rule._type = _type;
//
//		return true;
//	}
}
