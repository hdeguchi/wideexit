/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.condition.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;

/**
 * @author kurata
 *
 */
public class CollectionAndListConditionPropertyPanelBase extends RulePropertyPanelBase {

	/**
	 * 
	 */
	protected RadioButton[] _radioButtons1 = null;

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
	protected CheckBox _checkBox = null;

	/**
	 * 
	 */
	protected ComboBox _comboBox = null;

	/**
	 * 
	 */
	protected JLabel[] _label = null;

	/**
	 * 
	 */
	protected JLabel _dummy1 = null;

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
	public CollectionAndListConditionPropertyPanelBase(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
	}

	/**
	 * @param parent
	 */
	protected void setup_spot_checkBox_and_spot_selector(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy1 = new JLabel();
		panel.add( _dummy1);

		_spotCheckBox = create_checkBox(
			( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.spot.check.box.name")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.spot.check.box.name"),
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
	protected void setup_header(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_checkBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.check.box.denial"),
			true, true);
		panel.add( _checkBox);

		_label[ 0] = create_label(
			( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.header")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.header"),
			true);
		panel.add( _label[ 0]);

		_comboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _comboBox);

		parent.add( panel);
	}

	/**
	 * 
	 */
	protected void adjust() {
		int width = _checkBox.getPreferredSize().width;
		for ( int i = 0; i < _radioButtons1.length; ++i)
			width = Math.max( width, _radioButtons1[ i].getPreferredSize().width);

		_dummy1.setPreferredSize( new Dimension( width, _dummy1.getPreferredSize().height));
		_checkBox.setPreferredSize( new Dimension( width, _checkBox.getPreferredSize().height));

		Dimension dimension = new Dimension( width,
			_radioButtons1[ 0].getPreferredSize().height);
		for ( int i = 0; i < _radioButtons1.length; ++i)
			_radioButtons1[ i].setPreferredSize( dimension);


		width = _spotCheckBox.getPreferredSize().width;
		for ( int i = 0; i < _label.length; ++i)
			width = Math.max( width, _label[ i].getPreferredSize().width);

		_spotCheckBox.setPreferredSize( new Dimension( width, _spotCheckBox.getPreferredSize().height));

		dimension = new Dimension( width,
			_label[ 0].getPreferredSize().height);
		for ( int i = 0; i < _label.length; ++i)
			_label[ i].setPreferredSize( dimension);
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
}
