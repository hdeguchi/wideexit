/*
 * Created on 2006/02/15
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.command.exchange_algebra;

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
import soars.application.visualshell.object.role.base.object.legacy.command.ExchangeAlgebraCommand;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.combo.ComboBox;

/**
 * @author kurata
 */
public class ExchangeAlgebraCommandPropertyPanel5 extends RulePropertyPanelBase {

	/**
	 * 
	 */
	protected CheckBox _spotCheckBox = null;

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
	protected List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * 
	 */
	private List<JLabel> _dummies = new ArrayList<JLabel>();

	/**
	 * 
	 */
	protected ComboBox _directionComboBox = null;

	/**
	 * 
	 */
	protected List<ComboBox> _comboBoxes = new ArrayList<ComboBox>();

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
	public ExchangeAlgebraCommandPropertyPanel5(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
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

		setup_spotCheckBox( northPanel);

		setup_spotSelector( northPanel);

		insert_vertical_strut( northPanel);

		setup( northPanel);

		basicPanel.add( northPanel, "North");


		add( basicPanel);


		setup_apply_button();


		adjust();


		return true;
	}

	/**
	 * @param parent
	 */
	protected void setup_spotCheckBox(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel();
		panel.add( label);
		_dummies.add( label);

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
		_spotCheckBox.setEnabled( _role instanceof AgentRole);
		panel.add( _spotCheckBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_spotSelector(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel();
		panel.add( label);
		_dummies.add( label);

		label = create_label(
			_role instanceof AgentRole
				? ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.equip.label.agent")
				: ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.equip.label.current.spot"),
			false);
		panel.add( label);
		_labels.add( label);

		_directionComboBox = create_comboBox( new String[] { "<-", "->"}, 40, true);
		panel.add( _directionComboBox);

		_spotSelector = create_spot_selector(
			true, //_role instanceof AgentRole,
			true, panel);


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
	private void setup(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.equip.label.exchange.algebra"),
			false);
		panel.add( label);
		_labels.add( label);

		for ( int i = 0; i < 2; ++i) {
			ComboBox comboBox = create_comboBox( null, _standardControlWidth, false);
			panel.add( comboBox);
			_comboBoxes.add( comboBox);
		}

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = _labels.get( 1).getPreferredSize().width;
		_dummies.get( 1).setPreferredSize( new Dimension( width,
			_dummies.get( 1).getPreferredSize().height));


		_dummies.get( 0).setPreferredSize( new Dimension( width + _comboBoxes.get( 0).getPreferredSize().width + 5,
			_dummies.get( 0).getPreferredSize().height));


		_labels.get( 0).setPreferredSize( new Dimension(
			_comboBoxes.get( 0).getPreferredSize().width - _directionComboBox.getPreferredSize().width - 5,
			_labels.get( 0).getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#reset(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void reset(ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		spotVariableCheckBox.setSelected( true);
		CommonTool.update( spotVariableComboBox, get_agent_spot_variable_names( false));
		//CommonTool.update( spotVariableComboBox, get_spot_spot_variable_names( false));

		//super.reset(objectSelector, spotVariableCheckBox, spotVariableComboBox);
		spotVariableComboBox.setEnabled( 0 < spotVariableComboBox.getItemCount());

		spotVariableCheckBox.setEnabled( false);

		if ( _role instanceof AgentRole) {
			CommonTool.update( _comboBoxes.get( 0), get_agent_exchange_algebra_names2( false));

			//CommonTool.update( _comboBoxes.get( 1), get_spot_exchange_algebra_names2( false));
			String[] agentSpotVariableNames = get_agent_spot_variable_names( false);
			CommonTool.update( _comboBoxes.get( 1), ( null == agentSpotVariableNames || 0 == agentSpotVariableNames.length) ? null : get_spot_exchange_algebra_names2( false));
		} else {
			CommonTool.update( _comboBoxes.get( 0), get_spot_exchange_algebra_names2( false));

			if ( !objectSelector.is_empty())
				update( 0, objectSelector);
			else
				CommonTool.update( _comboBoxes.get( 1), null);
		}
	}


	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		spotVariableCheckBox.setEnabled( true);

		super.update(objectSelector, spotVariableCheckBox, spotVariableComboBox);

//		if ( _role instanceof AgentRole) {
			CommonTool.update( _comboBoxes.get( 1), get_spot_exchange_algebra_names2( false));
//		} else {
//			CommonTool.update( _comboBoxes.get( 1), null);
//		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.object.entiy.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		spotVariableCheckBox.setEnabled( true);

		super.update(spotObject, number, objectSelector, spotVariableCheckBox, spotVariableComboBox);

		CommonTool.update( _comboBoxes.get( 1), !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "exchange algebra", number, false) : get_spot_exchange_algebra_names2( false));
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
	}

	/**
	 * @param enables
	 */
	private void update_components(boolean[] enables) {
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
		CommonTool.update( _comboBoxes.get( 0), ( _role instanceof AgentRole) ? get_agent_exchange_algebra_names2( false) : get_spot_exchange_algebra_names2( false));
		_spotCheckBox.setSelected( true);
		update( _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);
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

		int kind = ExchangeAlgebraCommand.get_kind( rule._value);
		if ( 20 > kind || 21 < kind) {
			set_handler();
			return false;
		}

		String[] spots = CommonRuleManipulator.get_spot( rule._value);
		if ( null == spots) {
			set_handler();
			return false;
		}

		String[] elements = CommonRuleManipulator.get_elements( rule._value, 2);
		if ( null == elements) {
			set_handler();
			return false;
		}

		if ( !set( spots[ 0], spots[ 1], _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox)) {
			set_handler();
			return false;
		}

		_directionComboBox.setSelectedIndex( kind - 20);

		_comboBoxes.get( 0).setSelectedItem( elements[ ( 20 == kind) ? 0 : 1]);

		_comboBoxes.get( 1).setSelectedItem( elements[ ( 20 == kind) ? 1 : 0]);

		set_handler();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get()
	 */
	@Override
	public Rule get() {
		String value = get( get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox),
			ExchangeAlgebraCommand._reservedWords[ 20 + _directionComboBox.getSelectedIndex()],
			_comboBoxes.get( ( 0 == _directionComboBox.getSelectedIndex()) ? 0 : 1),
			_comboBoxes.get( ( 0 == _directionComboBox.getSelectedIndex()) ? 1 : 0));
		if ( null == value)
			return null;

		return Rule.create( _kind, _type, value);
	}

	/**
	 * @param spot
	 * @param command
	 * @param comboBox0
	 * @param comboBox1
	 * @return
	 */
	private String get(String spot, String command, ComboBox comboBox0, ComboBox comboBox1) {
		String text0 = ( String)comboBox0.getSelectedItem();
		if ( null == text0 || text0.equals( ""))
			return null;

		String text1 = ( String)comboBox1.getSelectedItem();
		if ( null == text1 || text1.equals( ""))
			return null;

		return ( spot + command + text0 + "=" + text1);
	}
}
