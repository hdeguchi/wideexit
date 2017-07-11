/*
 * 2005/07/27
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

import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.common.utility.swing.combo.ComboBox;

/**
 * @author kurata
 */
public class SelectRuleValuePropertyPanel1 extends RulePropertyPanelBase {

	/**
	 * 
	 */
	private boolean _canEmpty = true;

	/**
	 * 
	 */
	private String _label = "";

	/**
	 * 
	 */
	private ComboBox _valueComboBox = null;

	/**
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
	public SelectRuleValuePropertyPanel1(String title, String kind, String type, Color color, Role role, int index, String label, boolean canEmpty, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
		_label = label;
		_canEmpty = canEmpty;
	}

	/**
	 * @param items
	 */
	public void setup(String[] items) {
		if ( null == items || 0 == items.length)
			return;

		for ( int i = 0; i < items.length; ++i)
			_valueComboBox.addItem( items[ i]);

		_valueComboBox.setSelectedIndex( 0);
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

		basicPanel.add( northPanel, "North");


		add( basicPanel);


		setup_apply_button();


		return true;
	}

	/**
	 * @param parent
	 */
	private void setup(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));
		//panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		//panel.add( Box.createHorizontalStrut( 5));

		JLabel label = create_label( _label, false);
		panel.add( label);

		//panel.add( Box.createHorizontalStrut( 5));

		_valueComboBox = create_comboBox( null, 	_standardControlWidth/*500*/, false);

		panel.add( _valueComboBox);
		//panel.add( Box.createHorizontalStrut( 5));
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
		if ( null == rule || !_type.equals( rule._type))
			return false;

		_valueComboBox.setSelectedItem( rule._value);
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get()
	 */
	@Override
	public Rule get() {
		String value = ( String)_valueComboBox.getSelectedItem();
		if ( !_canEmpty && ( null == value || value.equals( "")))
			return null;

		return Rule.create( _kind, _type, value);
	}

//	/* (Non Javadoc)
//	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get(soars.application.visualshell.object.role.base.rule.base.Rule)
//	 */
//	@Override
//	public boolean get(Rule rule) {
//		if ( !super.get(rule))
//			return false;
//
//		String value = ( String)_valueComboBox.getSelectedItem();
//		if ( !_canEmpty && ( null == value || value.equals( "")))
//			return false;
//
//		rule._value = value;
//		rule._type = _type;
//
//		return true;
//	}
}
