/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.command;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.spot.SpotRole;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.combo.ComboBox;

/**
 * @author kurata
 *
 */
public class ExTransferCommandPropertyPanel extends RulePropertyPanelBase {

	/**
	 * 
	 */
	private CheckBox _spotCheckBox = null;

	/**
	 * 
	 */
	private ObjectSelector _spotSelector = null;

	/**
	 * 
	 */
	private CheckBox _spotVariableCheckBox = null;

	/**
	 * 
	 */
	private ComboBox _spotVariableComboBox = null;

	/**
	 * 
	 */
	private List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * 
	 */
	private ComboBox _exTransferComboBox = null;

	/**
	 * 
	 */
	private ComboBox _exchangeAlgebraInComboBox = null;

	/**
	 * 
	 */
	private ComboBox _exchangeAlgebraOutComboBox = null;

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
	public ExTransferCommandPropertyPanel(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase#on_create()
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

		setup_spotCheckBox_and_spotSelector( northPanel);

		insert_vertical_strut( northPanel);

		setup_spotVariableCheckBox_and_spotVariableComboBox( northPanel);

		insert_vertical_strut( northPanel);

		setup_exTransferComboBox( northPanel);

		insert_vertical_strut( northPanel);

		setup_exchangeAlgebraInComboBox( northPanel);

		insert_vertical_strut( northPanel);

		setup_exchangeAlgebraOutComboBox( northPanel);

		insert_vertical_strut( northPanel);

		basicPanel.add( northPanel, "North");


		add( basicPanel);


		setup_apply_button();


		adjust();


		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_spotCheckBox_and_spotSelector(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_spotCheckBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.extransfer.spot.check.box.name"),
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
	private void setup_spotVariableCheckBox_and_spotVariableComboBox(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

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
	private void setup_exTransferComboBox(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.extransfer.label"),
			true);
		_labels.add( label);
		panel.add( label);

		_exTransferComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _exTransferComboBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_exchangeAlgebraInComboBox(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.in.label"),
			true);
		_labels.add( label);
		panel.add( label);

		_exchangeAlgebraInComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _exchangeAlgebraInComboBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_exchangeAlgebraOutComboBox(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.out.label"),
			true);
		_labels.add( label);
		panel.add( label);

		_exchangeAlgebraOutComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _exchangeAlgebraOutComboBox);

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = _spotCheckBox.getPreferredSize().width;
		width = Math.max( width, _spotVariableCheckBox.getPreferredSize().width);
		for ( JLabel label:_labels)
			width = Math.max( width, label.getPreferredSize().width);

		_spotCheckBox.setPreferredSize( new Dimension( width, _spotCheckBox.getPreferredSize().height));
		_spotVariableCheckBox.setPreferredSize( new Dimension( width, _spotVariableCheckBox.getPreferredSize().height));
		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#reset(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void reset(ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		CommonTool.update( spotVariableComboBox, !_spotCheckBox.isSelected() ? get_agent_spot_variable_names( false) : get_spot_spot_variable_names( false));

		super.reset(objectSelector, spotVariableCheckBox, spotVariableComboBox);

		objectSelector.setEnabled( _spotCheckBox.isSelected());

		CommonTool.update( _exTransferComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_extransfer_names( false) : get_spot_extransfer_names( false));
		CommonTool.update( _exchangeAlgebraInComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_exchange_algebra_names2( false) : get_spot_exchange_algebra_names2( false));
		CommonTool.update( _exchangeAlgebraOutComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_exchange_algebra_names2( false) : get_spot_exchange_algebra_names2( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		super.update(objectSelector, spotVariableCheckBox, spotVariableComboBox);

		CommonTool.update( _exTransferComboBox, get_spot_extransfer_names( false));
		CommonTool.update( _exchangeAlgebraInComboBox, get_spot_exchange_algebra_names2( false));
		CommonTool.update( _exchangeAlgebraOutComboBox, get_spot_exchange_algebra_names2( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.object.entiy.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		super.update(spotObject, number, objectSelector, spotVariableCheckBox, spotVariableComboBox);

		CommonTool.update( _exTransferComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "extransfer", number, false) : get_spot_extransfer_names( false));
		CommonTool.update( _exchangeAlgebraInComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_exchange_algebra_names( number, false) : get_spot_exchange_algebra_names2( false));
		CommonTool.update( _exchangeAlgebraOutComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_exchange_algebra_names( number, false) : get_spot_exchange_algebra_names2( false));
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.common.selector.ObjectSelector)
	 */
	@Override
	protected void update(ObjectSelector objectSelector) {
		update( objectSelector, _spotVariableCheckBox, _spotVariableComboBox);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.object.entiy.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector)
	 */
	@Override
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector) {
		update( spotObject, number, objectSelector, _spotVariableCheckBox, _spotVariableComboBox);
	}

	/**
	 * 
	 */
	private void initialize() {
		if ( _role instanceof AgentRole) {
			_spotCheckBox.setSelected( false);
			_spotSelector.setEnabled( false);
		} else {
			_spotCheckBox.setSelected( true);
			_spotCheckBox.setEnabled( false);
			_spotSelector.setEnabled( true);
		}
	}

	/**
	 * 
	 */
	private void set_handler() {
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

		String[] elements = CommonRuleManipulator.get_elements( "dummy " + rule._value, 3);	// 苦肉の策
		if ( null == elements) {
			set_handler();
			return false;
		}

		String[] values = CommonRuleManipulator.get_spot_and_object( elements[ 0]);
		if ( null == values) {
			set_handler();
			return false;
		}

		if ( null == values[ 0] && _role instanceof SpotRole) {
			set_handler();
			return false;
		}

		String spot = CommonRuleManipulator.get_semantic_prefix( values);

		if ( !set( values[ 0], values[ 1], _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox)) {
			set_handler();
			return false;
		}

		_exTransferComboBox.setSelectedItem( values[ 2]);
		_exchangeAlgebraInComboBox.setSelectedItem( elements[ 1]);
		_exchangeAlgebraOutComboBox.setSelectedItem( elements[ 2]);

		set_handler();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get()
	 */
	@Override
	public Rule get() {
		String spot = get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);

		String exTransfer = ( String)_exTransferComboBox.getSelectedItem();
		if ( null == exTransfer || exTransfer.equals( ""))
			return null;

		String exchangeAlgebraIn = ( String)_exchangeAlgebraInComboBox.getSelectedItem();
		if ( null == exchangeAlgebraIn || exchangeAlgebraIn.equals( ""))
			return null;

		String _exchangeAlgebraOut = ( String)_exchangeAlgebraOutComboBox.getSelectedItem();
		if ( null == _exchangeAlgebraOut || _exchangeAlgebraOut.equals( ""))
			return null;

		return Rule.create( _kind, _type, spot + exTransfer + "=" + exchangeAlgebraIn + "=" + _exchangeAlgebraOut);
	}
}
