/*
 * Created on 2005/10/18
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.command.equip;

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
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.legacy.command.GetEquipCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.PutEquipCommand;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;

/**
 * @author kurata
 */
public class EquipCommandPropertyPanelBase extends RulePropertyPanelBase {

	/**
	 * 
	 */
	private static final String[] _resourceKeys = {
		"edit.rule.dialog.command.equip.probability",
		"edit.rule.dialog.command.equip.collection",
		"edit.rule.dialog.command.equip.list",
		"edit.rule.dialog.command.equip.map",
		"edit.rule.dialog.command.equip.keyword",
		"edit.rule.dialog.command.equip.number.object",
		"edit.rule.dialog.command.equip.time.variable",
		"edit.rule.dialog.command.equip.spot.variable"
	};

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
	protected List<RadioButton> _radioButtons1 = new ArrayList<RadioButton>();

	/**
	 * 
	 */
	private List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * 
	 */
	private List<JLabel> _dummy = new ArrayList<JLabel>();

	/**
	 * 
	 */
	protected ComboBox _directionComboBox = null;

	/**
	 * 
	 */
	protected List<List<ComboBox>> _comboBoxes = new ArrayList<List<ComboBox>>();

	/**
	 * @param title
	 * @param kind
	 * @param color
	 * @param role
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public EquipCommandPropertyPanelBase(String title, String kind, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, color, role, index, owner, parent);
		for ( int i = 0; i < _resourceKeys.length; ++i)
			_comboBoxes.add( new ArrayList<ComboBox>());
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

		setup_spot_selector( northPanel);

		insert_vertical_strut( northPanel);

		ButtonGroup buttonGroup = new ButtonGroup();

		for ( int i = 0; i < _resourceKeys.length; ++i) {
			setup( i, buttonGroup, northPanel);
			insert_vertical_strut( northPanel);
		}

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
		_dummy.add( label);

		_spotCheckBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.spot.check.box.name"),
			false, true);
		_spotCheckBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_spotSelector.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				on_update( ItemEvent.SELECTED == arg0.getStateChange(),
					_spotSelector,
					_spotVariableCheckBox,
					_spotVariableComboBox);
//				_radioButtons1[ 6].setEnabled( !_spot_variable_checkBox.isSelected() && _role instanceof AgentRole && ItemEvent.SELECTED != arg0.getStateChange());
//				if ( _spot_variable_checkBox.isSelected() || _role instanceof SpotRole || ItemEvent.SELECTED == arg0.getStateChange()) {
//					_role_variable_comboBox.setEnabled( false);
//					_labels[ 3][ 0].setEnabled( false);
//					_role_comboBox.setEnabled( false);
//				}
			}
		});
		_spotCheckBox.setEnabled( this instanceof AgentEquipCommandPropertyPanel);
		panel.add( _spotCheckBox);

//		label = create_label(
//			ResourceManager.get_instance().get( "edit.rule.dialog.command.equip.label.spot"),
//			false);
//		panel.add( label);
//		_labels.add( label);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_spot_selector(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel();
		panel.add( label);
		_dummy.add( label);

		label = create_label(
			this instanceof AgentEquipCommandPropertyPanel
				? ResourceManager.get_instance().get( "edit.rule.dialog.command.equip.label.agent")
				: ResourceManager.get_instance().get( "edit.rule.dialog.command.equip.label.current.spot"),
			false);
		panel.add( label);
		_labels.add( label);

		_directionComboBox = create_comboBox( new String[] { "<-", "->"}, 40, true);
		panel.add( _directionComboBox);

		_spotSelector = create_spot_selector(
			true,	//this instanceof AgentEquipCommandPropertyPanel,
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
//				on_update( _spotSelector,
//					_spotVariableCheckBox,
//					_spotVariableComboBox);
			}
		});
		panel.add( _spotVariableCheckBox);

		_spotVariableComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _spotVariableComboBox);

		parent.add( panel);
	}

	/**
	 * @param kind
	 * @param buttonGroup
	 * @param parent
	 */
	private void setup(final int kind, ButtonGroup buttonGroup, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		RadioButton radioButton = create_radioButton(
			ResourceManager.get_instance().get( _resourceKeys[ kind]),
			buttonGroup, true, false);
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_comboBoxes.get( kind).get( 0).setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_comboBoxes.get( kind).get( 1).setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( radioButton);
		_radioButtons1.add( radioButton);

		for ( int i = 0; i < 2/*_comboBoxes.get( kind).size()*/; ++i) {
			ComboBox comboBox = create_comboBox( null, _standardControlWidth, false);
			_comboBoxes.get( kind).add( comboBox);
			panel.add( comboBox);
		}

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( RadioButton radioButton:_radioButtons1)
			width = Math.max( width, radioButton.getPreferredSize().width);

		Dimension dimension = new Dimension( width,
			_radioButtons1.get( 0).getPreferredSize().height);
		_dummy.get( 1).setPreferredSize( dimension);
		for ( RadioButton radioButton:_radioButtons1)
			radioButton.setPreferredSize( dimension);


		_dummy.get( 0).setPreferredSize( new Dimension( width + _comboBoxes.get( 0).get( 0).getPreferredSize().width /*+ 5*/,
			_dummy.get( 0).getPreferredSize().height));


		_labels.get( 0).setPreferredSize( new Dimension(
			_comboBoxes.get( 0).get( 0).getPreferredSize().width - _directionComboBox.getPreferredSize().width - 5,
				_labels.get( 0).getPreferredSize().height));
//		_labels.get( 1).setPreferredSize( new Dimension(
//			_comboBoxes.get( 0).get( 0).getPreferredSize().width - _directionComboBox.getPreferredSize().width - 5,
//				_labels.get( 1).getPreferredSize().height));
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

		//super.reset(objectSelector, spotVariableCheckBox, spotVariableComboBox);
		//spotVariableCheckBox.setEnabled( 0 < spotVariableComboBox.getItemCount());
		//spotVariableCheckBox.setSelected( spotVariableCheckBox.isSelected() && 0 < spotVariableComboBox.getItemCount());
		spotVariableComboBox.setEnabled( 0 < spotVariableComboBox.getItemCount());

		//objectSelector.setEnabled( false);
		spotVariableCheckBox.setEnabled( false);

		if ( this instanceof AgentEquipCommandPropertyPanel) {
			CommonTool.update( _comboBoxes.get( 0).get( 0), get_agent_probability_names( false));
			CommonTool.update( _comboBoxes.get( 1).get( 0), get_agent_collection_names( false));
			CommonTool.update( _comboBoxes.get( 2).get( 0), get_agent_list_names( false));
			CommonTool.update( _comboBoxes.get( 3).get( 0), get_agent_map_names( false));
			CommonTool.update( _comboBoxes.get( 4).get( 0), get_agent_keyword_names( false));
			CommonTool.update( _comboBoxes.get( 5).get( 0), get_agent_number_object_names( false));
			CommonTool.update( _comboBoxes.get( 6).get( 0), get_agent_time_variable_names( false));
			CommonTool.update( _comboBoxes.get( 7).get( 0), get_agent_spot_variable_names( false));

			String[] agentSpotVariableNames = get_agent_spot_variable_names( false);
			CommonTool.update( _comboBoxes.get( 0).get( 1), ( null == agentSpotVariableNames || 0 == agentSpotVariableNames.length) ? null : get_spot_probability_names( false));
			CommonTool.update( _comboBoxes.get( 1).get( 1), ( null == agentSpotVariableNames || 0 == agentSpotVariableNames.length) ? null : get_spot_collection_names( false));
			CommonTool.update( _comboBoxes.get( 2).get( 1), ( null == agentSpotVariableNames || 0 == agentSpotVariableNames.length) ? null : get_spot_list_names( false));
			CommonTool.update( _comboBoxes.get( 3).get( 1), ( null == agentSpotVariableNames || 0 == agentSpotVariableNames.length) ? null : get_spot_map_names( false));
			CommonTool.update( _comboBoxes.get( 4).get( 1), ( null == agentSpotVariableNames || 0 == agentSpotVariableNames.length) ? null : get_spot_keyword_names( false));
			CommonTool.update( _comboBoxes.get( 5).get( 1), ( null == agentSpotVariableNames || 0 == agentSpotVariableNames.length) ? null : get_spot_number_object_names( false));
			CommonTool.update( _comboBoxes.get( 6).get( 1), ( null == agentSpotVariableNames || 0 == agentSpotVariableNames.length) ? null : get_spot_time_variable_names( false));
			CommonTool.update( _comboBoxes.get( 7).get( 1), ( null == agentSpotVariableNames || 0 == agentSpotVariableNames.length) ? null : get_spot_spot_variable_names( false));
		} else {
			CommonTool.update( _comboBoxes.get( 0).get( 0), get_spot_probability_names( false));
			CommonTool.update( _comboBoxes.get( 1).get( 0), get_spot_collection_names( false));
			CommonTool.update( _comboBoxes.get( 2).get( 0), get_spot_list_names( false));
			CommonTool.update( _comboBoxes.get( 3).get( 0), get_spot_map_names( false));
			CommonTool.update( _comboBoxes.get( 4).get( 0), get_spot_keyword_names( false));
			CommonTool.update( _comboBoxes.get( 5).get( 0), get_spot_number_object_names( false));
			CommonTool.update( _comboBoxes.get( 6).get( 0), get_spot_time_variable_names( false));
			CommonTool.update( _comboBoxes.get( 7).get( 0), get_spot_spot_variable_names( false));

			if ( !objectSelector.is_empty())
				update( 0, objectSelector);
			else {
				CommonTool.update( _comboBoxes.get( 0).get( 1), null);
				CommonTool.update( _comboBoxes.get( 1).get( 1), null);
				CommonTool.update( _comboBoxes.get( 2).get( 1), null);
				CommonTool.update( _comboBoxes.get( 3).get( 1), null);
				CommonTool.update( _comboBoxes.get( 4).get( 1), null);
				CommonTool.update( _comboBoxes.get( 5).get( 1), null);
				CommonTool.update( _comboBoxes.get( 6).get( 1), null);
				CommonTool.update( _comboBoxes.get( 7).get( 1), null);
			}
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

//		if ( this instanceof AgentEquipCommandPropertyPanel) {
			CommonTool.update( _comboBoxes.get( 0).get( 1), get_spot_probability_names( false));
			CommonTool.update( _comboBoxes.get( 1).get( 1), get_spot_collection_names( false));
			CommonTool.update( _comboBoxes.get( 2).get( 1), get_spot_list_names( false));
			CommonTool.update( _comboBoxes.get( 3).get( 1), get_spot_map_names( false));
			CommonTool.update( _comboBoxes.get( 4).get( 1), get_spot_keyword_names( false));
			CommonTool.update( _comboBoxes.get( 5).get( 1), get_spot_number_object_names( false));
			CommonTool.update( _comboBoxes.get( 6).get( 1), get_spot_time_variable_names( false));
			CommonTool.update( _comboBoxes.get( 7).get( 1), get_spot_spot_variable_names( false));
//		} else {
//			CommonTool.update( _comboBoxes.get( 0).get( 1), null);
//			CommonTool.update( _comboBoxes.get( 1).get( 1), null);
//			CommonTool.update( _comboBoxes.get( 2).get( 1), null);
//			CommonTool.update( _comboBoxes.get( 3).get( 1), null);
//			CommonTool.update( _comboBoxes.get( 4).get( 1), null);
//			CommonTool.update( _comboBoxes.get( 5).get( 1), null);
//			CommonTool.update( _comboBoxes.get( 6).get( 1), null);
//			CommonTool.update( _comboBoxes.get( 7).get( 1), null);
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

		CommonTool.update( _comboBoxes.get( 0).get( 1), !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "probability", number, false) : get_spot_probability_names( false));
		CommonTool.update( _comboBoxes.get( 1).get( 1), !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "collection", number, false) : get_spot_collection_names( false));
		CommonTool.update( _comboBoxes.get( 2).get( 1), !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));
		CommonTool.update( _comboBoxes.get( 3).get( 1), !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "map", number, false) : get_spot_map_names( false));
		CommonTool.update( _comboBoxes.get( 4).get( 1), !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "keyword", number, false) : get_spot_keyword_names( false));
		CommonTool.update( _comboBoxes.get( 5).get( 1), !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));
		CommonTool.update( _comboBoxes.get( 6).get( 1), !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "time variable", number, false) : get_spot_time_variable_names( false));
		CommonTool.update( _comboBoxes.get( 7).get( 1), !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "spot variable", number, false) : get_spot_spot_variable_names( false));
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
	protected void initialize() {
		_radioButtons1.get( 0).setSelected( true);
		update_components( new boolean[] {
			true, false, false, false,
			false, false, false, false
		});
	}

	/**
	 * @param enables
	 */
	protected void update_components(boolean[] enables) {
		for ( int i = 0; i < _comboBoxes.size(); ++i) {
			_comboBoxes.get( i).get( 0).setEnabled( enables[ i]);
			_comboBoxes.get( i).get( 1).setEnabled( enables[ i]);
		}
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
		CommonTool.update( _comboBoxes.get( 0).get( 0), ( this instanceof AgentEquipCommandPropertyPanel) ? get_agent_probability_names( false) : get_spot_probability_names( false));
		CommonTool.update( _comboBoxes.get( 1).get( 0), ( this instanceof AgentEquipCommandPropertyPanel) ? get_agent_collection_names( false) : get_spot_collection_names( false));
		CommonTool.update( _comboBoxes.get( 2).get( 0), ( this instanceof AgentEquipCommandPropertyPanel) ? get_agent_list_names( false) : get_spot_list_names( false));
		CommonTool.update( _comboBoxes.get( 3).get( 0), ( this instanceof AgentEquipCommandPropertyPanel) ? get_agent_map_names( false) : get_spot_map_names( false));
		CommonTool.update( _comboBoxes.get( 4).get( 0), ( this instanceof AgentEquipCommandPropertyPanel) ? get_agent_keyword_names( false) : get_spot_keyword_names( false));
		CommonTool.update( _comboBoxes.get( 5).get( 0), ( this instanceof AgentEquipCommandPropertyPanel) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));
		CommonTool.update( _comboBoxes.get( 6).get( 0), ( this instanceof AgentEquipCommandPropertyPanel) ? get_agent_time_variable_names( false) : get_spot_time_variable_names( false));
		CommonTool.update( _comboBoxes.get( 7).get( 0), ( this instanceof AgentEquipCommandPropertyPanel) ? get_agent_spot_variable_names( false) : get_spot_spot_variable_names( false));
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

		if ( null == rule || rule._value.equals( "")) {
			set_handler();
			return false;
		}

		int kind;
		if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.command.get.equip")))
			kind = GetEquipCommand.get_kind( rule._value);
		else if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.command.put.equip")))
			kind = PutEquipCommand.get_kind( rule._value);
		else
			return false;
				
		if ( 0 > kind) {
			set_handler();
			return false;
		}

		_radioButtons1.get( kind).setSelected( true);

		if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.command.get.equip"))) {
			if ( !set( rule._value, kind, 0, 1)) {
				set_handler();
				return false;
			}
		} else {
			if ( !set( rule._value, kind, 1, 0)) {
				set_handler();
				return false;
			}
		}

		set_handler();

		return true;
	}

	/**
	 * @param value
	 * @param kind
	 * @param index0
	 * @param index1
	 * @return
	 */
	protected boolean set(String value, int kind, int index0, int index1) {
		return false;
	}
}
