/*
 * Created on 2005/10/14
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.command.exchange_algebra;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.object.legacy.command.ExchangeAlgebraCommand;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextField;

/**
 * @author kurata
 */
public class ExchangeAlgebraCommandPropertyPanelBase extends RulePropertyPanelBase {

	/**
	 * 
	 */
	protected List<RadioButton> _radioButtons1 = new ArrayList<RadioButton>();

	/**
	 * 
	 */
	protected CheckBox _spotCheckBox = null;

	/**
	 * 
	 */
	protected ObjectSelector _spotSelector = null;

	/**
	 * 
	 */
	protected CheckBox _spotVariableCheckBox = null;

	/**
	 * 
	 */
	protected ComboBox _spotVariableComboBox = null;

	/**
	 * 
	 */
	protected ComboBox _numberObjectComboBox = null;

	/**
	 * 
	 */
	protected ComboBox _exchangeAlgebraComboBox = null;

	/**
	 * 
	 */
	protected List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * 
	 */
	protected JLabel _dummy1 = null;

	/**
	 * 
	 */
	protected JLabel _dummy2 = null;

	/**
	 * 
	 */
	protected JLabel _dummy3 = null;

	/**
	 * 
	 */
	protected JLabel _dummy4 = null;

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
	public ExchangeAlgebraCommandPropertyPanelBase(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
	}

	/**
	 * @param parent
	 */
	protected void setup_spotCheckBox_and_spotSelector(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy1 = new JLabel();
		panel.add( _dummy1);

		_spotCheckBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.spot.check.box.name"),
			false, true);
		_spotCheckBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_spotSelector.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				on_update( ItemEvent.SELECTED == arg0.getStateChange(),
					_spotSelector,
					_spotVariableCheckBox,
					_spotVariableComboBox);
			}
		});
		panel.add( _spotCheckBox);

		_spotSelector = create_spot_selector( true, true, panel);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	protected void setup_spotVariableCheckBox_and_spotVariableComboBox( JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy2 = new JLabel();
		panel.add( _dummy2);

		_spotVariableCheckBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.spot.variable.check.box.name"),
			true, true);
		_spotVariableCheckBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				on_update( _spotCheckBox.isSelected(),
					_spotSelector,
					_spotVariableCheckBox,
					_spotVariableComboBox);
			}
		});
		panel.add( _spotVariableCheckBox);

		_spotVariableComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _spotVariableComboBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	protected void setup_header1(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3 = new JLabel();
		panel.add( _dummy3);

		JLabel label = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.header.exchange.algebra"),
			true);
		panel.add( label);
		_labels.add( label);

		_exchangeAlgebraComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _exchangeAlgebraComboBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	protected void setup_header2(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3 = new JLabel();
		panel.add( _dummy3);

		JLabel label = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.header.norm"),
			true);
		panel.add( label);
		_labels.add( label);

		_numberObjectComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _numberObjectComboBox);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy4 = new JLabel();
		panel.add( _dummy4);

		label = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.header.exchange.algebra"),
			true);
		panel.add( label);
		_labels.add( label);

		_exchangeAlgebraComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _exchangeAlgebraComboBox);

		parent.add( panel);
	}

	/**
	 * 
	 */
	protected void set_handler() {
		_spotSelector.set_handler( this);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#on_setup_completed()
	 */
	@Override
	public void on_setup_completed() {
		reset( _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);
		super.on_setup_completed();
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.panel.StandardPanel#on_ok(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_ok(ActionEvent actionEvent) {
		_parent.on_apply( this, actionEvent);
	}

	/**
	 * @param textField
	 * @param element
	 * @return
	 */
	public static boolean set_base(TextField textField, String element) {
		String exbaseLiteral = ExchangeAlgebraCommand.toExbaseLiteral( element);
		if ( null == exbaseLiteral)
			return false;

		textField.setText( exbaseLiteral);
		return true;
	}

	/**
	 * @param textField
	 * @return
	 */
	public static String get_base(TextField textField) {
		return ExchangeAlgebraCommand.toBaseOneKeyString( textField.getText());
	}
}
