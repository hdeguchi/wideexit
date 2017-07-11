/*
 * 2005/08/03
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.condition;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.legacy.condition.base.CollectionAndListConditionPropertyPanelBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.legacy.condition.ListCondition;
import soars.application.visualshell.object.role.spot.SpotRole;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 */
public class ListConditionPropertyPanel extends CollectionAndListConditionPropertyPanelBase {

	/**
	 * 
	 */
	private ObjectSelector _isFirstAgentAgentSelector = null;

	/**
	 * 
	 */
	private ObjectSelector _isFirstSpotSpotSelector = null;

	/**
	 * 
	 */
	private TextField _isFirstEquipTextField = null;

	/**
	 * 
	 */
	private TextField _isFirstStringTextField = null;

	/**
	 * 
	 */
	private ObjectSelector _isLastAgentAgentSelector = null;

	/**
	 * 
	 */
	private ObjectSelector _isLastSpotSpotSelector = null;

	/**
	 * 
	 */
	private TextField _isLastEquipTextField = null;

	/**
	 * 
	 */
	private TextField _isLastStringTextField = null;

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
	public ListConditionPropertyPanel(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);

		_radioButtons1 = new RadioButton[] {
			null, null, null, null,
			null, null, null, null
		};

		_label = new JLabel[] {
			null, null, null, null,
			null, null, null, null,
			null
		};
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

		setup_spot_checkBox_and_spot_selector( northPanel);

		insert_vertical_strut( northPanel);

		setup_header( northPanel);

		insert_vertical_strut( northPanel);

		ButtonGroup buttonGroup1 = new ButtonGroup();

		setup_is_first_agent( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_is_first_spot( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_is_first_equip( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_is_first_string( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_is_last_agent( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_is_last_spot( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_is_last_equip( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_is_last_string( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		basicPanel.add( northPanel, "North");


		add( basicPanel);


		setup_apply_button();


		adjust();


		return true;
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_is_first_agent(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.is.first.agent"),
			buttonGroup1, true, false);
		_radioButtons1[ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_isFirstAgentAgentSelector.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 0]);

		_label[ 1] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.is.first.agent"),
			true);
		panel.add( _label[ 1]);

		_isFirstAgentAgentSelector = create_agent_selector( true, true, panel);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_is_first_spot(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.is.first.spot"),
			buttonGroup1, true, false);
		_radioButtons1[ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 2].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_isFirstSpotSpotSelector.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 1]);

		_label[ 2] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.is.first.spot"),
			true);
		panel.add( _label[ 2]);

		_isFirstSpotSpotSelector = create_spot_selector( true, true, panel);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_is_first_equip(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.is.first.equip"),
			buttonGroup1, true, false);
		_radioButtons1[ 2].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 3].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_isFirstEquipTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 2]);

		_label[ 3] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.is.first.equip"),
			true);
		panel.add( _label[ 3]);

		_isFirstEquipTextField = create_textField( new TextExcluder( Constant._prohibitedCharacters1), _standardControlWidth, false);
		panel.add( _isFirstEquipTextField);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_is_first_string(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 3] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.is.first.string"),
			buttonGroup1, true, false);
		_radioButtons1[ 3].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 4].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_isFirstStringTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 3]);

		_label[ 4] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.is.first.string"),
			true);
		panel.add( _label[ 4]);

		_isFirstStringTextField = create_textField( new TextExcluder( Constant._prohibitedCharacters3), _standardControlWidth, false);
		panel.add( _isFirstStringTextField);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_is_last_agent(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 4] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.is.last.agent"),
			buttonGroup1, true, false);
		_radioButtons1[ 4].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 5].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_isLastAgentAgentSelector.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 4]);

		_label[ 5] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.is.last.agent"),
			true);
		panel.add( _label[ 5]);

		_isLastAgentAgentSelector = create_agent_selector( true, true, panel);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_is_last_spot(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 5] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.is.last.spot"),
			buttonGroup1, true, false);
		_radioButtons1[ 5].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 6].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_isLastSpotSpotSelector.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 5]);

		_label[ 6] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.is.last.spot"),
			true);
		panel.add( _label[ 6]);

		_isLastSpotSpotSelector = create_spot_selector( true, true, panel);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_is_last_equip(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 6] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.is.last.equip"),
			buttonGroup1, true, false);
		_radioButtons1[ 6].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 7].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_isLastEquipTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 6]);

		_label[ 7] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.is.last.equip"),
			true);
		panel.add( _label[ 7]);

		_isLastEquipTextField = create_textField( new TextExcluder( Constant._prohibitedCharacters1), _standardControlWidth, false);
		panel.add( _isLastEquipTextField);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_is_last_string(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 7] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.is.last.string"),
			buttonGroup1, true, false);
		_radioButtons1[ 7].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 8].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_isLastStringTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 7]);

		_label[ 8] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.is.last.string"),
			true);
		panel.add( _label[ 8]);

		_isLastStringTextField = create_textField( new TextExcluder( Constant._prohibitedCharacters3), _standardControlWidth, false);
		panel.add( _isLastStringTextField);

		parent.add( panel);
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

		CommonTool.update( _comboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_list_names( false) : get_spot_list_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		super.update(objectSelector, spotVariableCheckBox, spotVariableComboBox);

		CommonTool.update( _comboBox, get_spot_list_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.object.entiy.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		super.update(spotObject, number, objectSelector, spotVariableCheckBox, spotVariableComboBox);

		CommonTool.update( _comboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));
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

		_radioButtons1[ 0].setSelected( true);
		update_components( new boolean[] {
			true, false, false, false,
			false, false, false, false
		});
	}

	/**
	 * @param enables
	 */
	private void update_components(boolean[] enables) {
		_label[ 1].setEnabled( enables[ 0]);
		_isFirstAgentAgentSelector.setEnabled( enables[ 0]);

		_label[ 2].setEnabled( enables[ 1]);
		_isFirstSpotSpotSelector.setEnabled( enables[ 1]);

		_label[ 3].setEnabled( enables[ 2]);
		_isFirstEquipTextField.setEnabled( enables[ 2]);

		_label[ 4].setEnabled( enables[ 3]);
		_isFirstStringTextField.setEnabled( enables[ 3]);

		_label[ 5].setEnabled( enables[ 4]);
		_isLastAgentAgentSelector.setEnabled( enables[ 4]);

		_label[ 6].setEnabled( enables[ 5]);
		_isLastSpotSpotSelector.setEnabled( enables[ 5]);

		_label[ 7].setEnabled( enables[ 6]);
		_isLastEquipTextField.setEnabled( enables[ 6]);

		_label[ 8].setEnabled( enables[ 7]);
		_isLastStringTextField.setEnabled( enables[ 7]);
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

		int kind = ListCondition.get_kind( rule._value);
		if ( 0 > kind) {
			set_handler();
			return false;
		}

		_radioButtons1[ kind].setSelected( true);

		String[] elements = CommonRuleManipulator.get_elements( rule._value);
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

		if ( !set( values[ 0], values[ 1], _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox)) {
			set_handler();
			return false;
		}

		switch ( kind) {
			case 0:
				set2( _comboBox, _isFirstAgentAgentSelector, values[ 2], ( 2 > elements.length) ? "" : elements[ 1]);
				break;
			case 1:
				set2( _comboBox, _isFirstSpotSpotSelector, values[ 2], ( 2 > elements.length) ? "" : elements[ 1]);
				break;
			case 2:
				set3( _comboBox, _isFirstEquipTextField, values[ 2], elements[ 1]);
				break;
			case 3:
				set3( _comboBox, _isFirstStringTextField, values[ 2], elements[ 1]);
				break;
			case 4:
				set2( _comboBox, _isLastAgentAgentSelector, values[ 2], ( 2 > elements.length) ? "" : elements[ 1]);
				break;
			case 5:
				set2( _comboBox, _isLastSpotSpotSelector, values[ 2], ( 2 > elements.length) ? "" : elements[ 1]);
				break;
			case 6:
				set3( _comboBox, _isLastEquipTextField, values[ 2], elements[ 1]);
				break;
			case 7:
				set3( _comboBox, _isLastStringTextField, values[ 2], elements[ 1]);
				break;
			default:
				set_handler();
				return false;
		}

		_checkBox.setSelected( rule._value.startsWith( "!"));

		set_handler();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get()
	 */
	@Override
	public Rule get() {
		String spot = get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);

		String value = null;
		int kind = SwingTool.get_enabled_radioButton( _radioButtons1);
		switch ( kind) {
			case 0:
				value = get3( _comboBox, _isFirstAgentAgentSelector);
				break;
			case 1:
				value = get3( _comboBox, _isFirstSpotSpotSelector);
				break;
			case 2:
				if ( null == _isFirstEquipTextField.getText()
					|| _isFirstEquipTextField.getText().equals( "")
					|| _isFirstEquipTextField.getText().equals( "$")
					|| 0 < _isFirstEquipTextField.getText().indexOf( '$')
					|| _isFirstEquipTextField.getText().equals( "$Name")
					|| _isFirstEquipTextField.getText().equals( "$Role")
					|| _isFirstEquipTextField.getText().equals( "$Spot")
					|| 0 <= _isFirstEquipTextField.getText().indexOf( Constant._experimentName))
					return null;

				if ( _isFirstEquipTextField.getText().startsWith( "$")
					&& ( 0 < _isFirstEquipTextField.getText().indexOf( "$", 1)
					|| 0 < _isFirstEquipTextField.getText().indexOf( ")", 1)))
					return null;

				// TODO 要動作確認！
				if ( !is_object( spot, _isFirstEquipTextField.getText()))
					return null;

				value = get4( _comboBox, _isFirstEquipTextField, false);
				break;
			case 3:
				if ( null == _isFirstStringTextField.getText()
					|| _isFirstStringTextField.getText().equals( "")
					|| _isFirstStringTextField.getText().equals( "$")
					|| 0 < _isFirstStringTextField.getText().indexOf( '$')
					|| _isFirstStringTextField.getText().startsWith( " ")
					|| _isFirstStringTextField.getText().endsWith( " ")
					|| _isFirstStringTextField.getText().equals( "$Name")
					|| _isFirstStringTextField.getText().equals( "$Role")
					|| _isFirstStringTextField.getText().equals( "$Spot")
					|| 0 <= _isFirstStringTextField.getText().indexOf( Constant._experimentName))
					return null;

				if ( _isFirstStringTextField.getText().startsWith( "$")
					&& ( 0 <= _isFirstStringTextField.getText().indexOf( " ")
					|| 0 < _isFirstStringTextField.getText().indexOf( "$", 1)
					|| 0 < _isFirstStringTextField.getText().indexOf( ")", 1)))
					return null;

				value = get4( _comboBox, _isFirstStringTextField, false);
				break;
			case 4:
				value = get3( _comboBox, _isLastAgentAgentSelector);
				break;
			case 5:
				value = get3( _comboBox, _isLastSpotSpotSelector);
				break;
			case 6:
				if ( null == _isLastEquipTextField.getText()
					|| _isLastEquipTextField.getText().equals( "")
					|| _isLastEquipTextField.getText().equals( "$")
					|| 0 < _isLastEquipTextField.getText().indexOf( '$')
					|| _isLastEquipTextField.getText().equals( "$Name")
					|| _isLastEquipTextField.getText().equals( "$Role")
					|| _isLastEquipTextField.getText().equals( "$Spot")
					|| 0 <= _isLastEquipTextField.getText().indexOf( Constant._experimentName))
					return null;

				if ( _isLastEquipTextField.getText().startsWith( "$")
					&& ( 0 < _isLastEquipTextField.getText().indexOf( "$", 1)
					|| 0 < _isLastEquipTextField.getText().indexOf( ")", 1)))
					return null;

				// TODO 要動作確認！
				if ( !is_object( spot, _isLastEquipTextField.getText()))
					return null;

				value = get4( _comboBox, _isLastEquipTextField, false);
				break;
			case 7:
				if ( null == _isLastStringTextField.getText()
					|| _isLastStringTextField.getText().equals( "")
					|| _isLastStringTextField.getText().equals( "$")
					|| 0 < _isLastStringTextField.getText().indexOf( '$')
					|| _isLastStringTextField.getText().startsWith( " ")
					|| _isLastStringTextField.getText().endsWith( " ")
					|| _isLastStringTextField.getText().equals( "$Name")
					|| _isLastStringTextField.getText().equals( "$Role")
					|| _isLastStringTextField.getText().equals( "$Spot")
					|| 0 <= _isLastStringTextField.getText().indexOf( Constant._experimentName))
					return null;

				if ( _isLastStringTextField.getText().startsWith( "$")
					&& ( 0 <= _isLastStringTextField.getText().indexOf( " ")
					|| 0 < _isLastStringTextField.getText().indexOf( "$", 1)
					|| 0 < _isLastStringTextField.getText().indexOf( ")", 1)))
					return null;

				value = get4( _comboBox, _isLastStringTextField, false);
				break;
			default:
				return null;
		}

		if ( null == value)
			return null;

		return Rule.create( _kind, _type, ( _checkBox.isSelected() ? "!" : "") + ( ListCondition._reservedWords[ kind] + spot + value));
	}
}
