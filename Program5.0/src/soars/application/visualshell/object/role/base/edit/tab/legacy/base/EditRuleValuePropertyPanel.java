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
import soars.common.utility.swing.text.TextField;

/**
 * @author kurata
 */
public class EditRuleValuePropertyPanel extends RulePropertyPanelBase {

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
	private TextField _valueTextField = null;

	/**
	 * 
	 */
	private int _valueTextFieldWidth = _standardControlWidth;

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
	public EditRuleValuePropertyPanel(String title, String kind, String type, Color color, Role role, int index, String label, boolean canEmpty, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
		_label = label;
		_canEmpty = canEmpty;
	}

	/**
	 * @param title
	 * @param kind
	 * @param type
	 * @param color
	 * @param role
	 * @param index
	 * @param label
	 * @param valueTextFieldWidth
	 * @param canEmpty
	 * @param owner
	 * @param parent
	 */
	public EditRuleValuePropertyPanel(String title, String kind, String type, Color color, Role role, int index, String label, int valueTextFieldWidth, boolean canEmpty, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
		_label = label;
		_valueTextFieldWidth = valueTextFieldWidth;
		_canEmpty = canEmpty;
	}

	/**
	 * @param value
	 */
	public void set(String value) {
		if ( null == _valueTextField)
			return;

		_valueTextField.setText( value);
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

		setup_value_text_field( northPanel);

		basicPanel.add( northPanel, "North");


		add( basicPanel);


		setup_apply_button();


		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_value_text_field(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));
		//panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		//panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( _label);
		label.setForeground( _color);
		panel.add( label);

		//panel.add( Box.createHorizontalStrut( 5));

		_valueTextField = create_textField( _valueTextFieldWidth/*500*/, false);
		panel.add( _valueTextField);

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

		_valueTextField.setText( rule._value);
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get()
	 */
	@Override
	public Rule get() {
		String value = _valueTextField.getText();
		if ( !_canEmpty && ( null == value || value.equals( "")))
			return null;

		return Rule.create( _kind, _type, value);
	}

//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get(soars.application.visualshell.object.role.base.rule.base.Rule)
//	 */
//	@Override
//	public boolean get(Rule rule) {
//		if ( !super.get(rule))
//			return false;
//
//		String value = _valueTextField.getText();
//		if ( !_canEmpty && ( null == value || value.equals( "")))
//			return false;
//
//		rule._value = value;
//		rule._type = _type;
//
//		return true;
//	}
}
