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
import soars.application.visualshell.object.role.base.object.legacy.condition.base.CollectionAndListCondition;
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
public class CollectionAndListConditionPropertyPanel extends CollectionAndListConditionPropertyPanelBase {

	/**
	 * 
	 */
	private ObjectSelector _containsAgentAgentSelector = null;

	/**
	 * 
	 */
	private ObjectSelector _containsSpotSpotSelector = null;

	/**
	 * 
	 */
	private ComboBox _containsEquipNumberObjectNumberObjectComboBox = null;

	/**
	 * 
	 */
	private ComboBox _containsEquipProbabilityProbabilityComboBox = null;

	/**
	 * 
	 */
	private TextField _containsStringTextField = null;

	/**
	 * 
	 */
	private ComboBox _containsAllCollectionComboBox = null;

	/**
	 * 
	 */
	private ComboBox _containsAllListComboBox = null;

	/**
	 * 
	 */
	private TextField _askAllTextField = null;

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
	public CollectionAndListConditionPropertyPanel(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);

		_radioButtons1 = new RadioButton[] {
			null, null, null, null,
			null, null, null, null,
			null
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

		setup_is_empty( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_contains_agent( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_contains_spot( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_contains_equip_number_object( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_contains_equip_probability( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_contains_string( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_contains_all1( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_contains_all2( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_ask_all( buttonGroup1, northPanel);

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
	private void setup_is_empty(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 0] = create_radioButton(
			( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.is.empty")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.is.empty"),
			buttonGroup1, true, false);
		panel.add( _radioButtons1[ 0]);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_contains_agent(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 1] = create_radioButton(
			( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.contains.agent")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.contains.agent"),
			buttonGroup1, true, false);
		_radioButtons1[ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_containsAgentAgentSelector.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 1]);

		_label[ 1] = create_label( "  " +
			( ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.label.contains.agent")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.contains.agent")),
			true);
		panel.add( _label[ 1]);

		_containsAgentAgentSelector = create_agent_selector( true, true, panel);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_contains_spot(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 2] = create_radioButton(
			( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.contains.spot")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.contains.spot"),
			buttonGroup1, true, false);
		_radioButtons1[ 2].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 2].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_containsSpotSpotSelector.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 2]);

		_label[ 2] = create_label( "  " +
			( ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.label.contains.spot")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.contains.spot")),
			true);
		panel.add( _label[ 2]);

		_containsSpotSpotSelector = create_spot_selector( true, true, panel);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_contains_equip_number_object(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 3] = create_radioButton(
			( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.contains.equip.number.object")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.contains.equip.number.object"),
			buttonGroup1, true, false);
		_radioButtons1[ 3].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 3].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_containsEquipNumberObjectNumberObjectComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 3]);

		_label[ 3] = create_label( "  " +
			( ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.label.contains.equip.number.object")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.contains.equip.number.object")),
			true);
		panel.add( _label[ 3]);

		_containsEquipNumberObjectNumberObjectComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _containsEquipNumberObjectNumberObjectComboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_contains_equip_probability(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 4] = create_radioButton(
			( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.contains.equip.probability")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.contains.equip.probability"),
			buttonGroup1, true, false);
		_radioButtons1[ 4].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 4].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_containsEquipProbabilityProbabilityComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 4]);

		_label[ 4] = create_label( "  " +
			( ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.label.contains.equip.probability")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.contains.equip.probability")),
			true);
		panel.add( _label[ 4]);

		_containsEquipProbabilityProbabilityComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _containsEquipProbabilityProbabilityComboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_contains_string(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 5] = create_radioButton(
			( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.contains.string")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.contains.string"),
			buttonGroup1, true, false);
		_radioButtons1[ 5].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 5].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_containsStringTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 5]);

		_label[ 5] = create_label( "  " +
			( ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.label.contains.string")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.contains.string")),
			true);
		panel.add( _label[ 5]);

		_containsStringTextField = create_textField( new TextExcluder( Constant._prohibitedCharacters3), _standardControlWidth, false);
		panel.add( _containsStringTextField);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_contains_all1(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 6] = create_radioButton(
			( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.contains.all")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.contains.all"),
			buttonGroup1, true, false);
		_radioButtons1[ 6].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 6].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_containsAllCollectionComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 6]);

		_label[ 6] = create_label( "  " +
			( ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.label.contains.all")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.contains.all")),
			true);
		panel.add( _label[ 6]);

		_containsAllCollectionComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _containsAllCollectionComboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_contains_all2(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 7] = create_radioButton(
			( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.contains.all")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.contains.all"),
			buttonGroup1, true, false);
		_radioButtons1[ 7].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 7].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_containsAllListComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 7]);

		_label[ 7] = create_label( "  " +
			( ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.contains.all")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.label.contains.all")),
			true);
		panel.add( _label[ 7]);

		_containsAllListComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _containsAllListComboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_ask_all(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 8] = create_radioButton(
			( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.ask.all")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.ask.all"),
			buttonGroup1, true, false);
		_radioButtons1[ 8].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 8].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_askAllTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 8]);

		_label[ 8] = create_label( "  " +
			( ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection")))
				? ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection.label.ask.all")
				: ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.label.ask.all")),
			true);
		panel.add( _label[ 8]);

		_askAllTextField = create_textField( new TextExcluder( Constant._prohibitedCharacters3), _standardControlWidth, false);
		panel.add( _askAllTextField);

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

		if ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection"))) {
			CommonTool.update( _comboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_collection_names( false) : get_spot_collection_names( false));

			CommonTool.update( _containsAllCollectionComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_collection_names( false) : get_spot_collection_names( false));
			CommonTool.update( _containsAllListComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_list_names( false) : get_spot_list_names( false));
		} else {
			CommonTool.update( _comboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_list_names( false) : get_spot_list_names( false));

			CommonTool.update( _containsAllCollectionComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_list_names( false) : get_spot_list_names( false));
			CommonTool.update( _containsAllListComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_collection_names( false) : get_spot_collection_names( false));
		}

		CommonTool.update( _containsEquipNumberObjectNumberObjectComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));
		CommonTool.update( _containsEquipProbabilityProbabilityComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_probability_names( false) : get_spot_probability_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		super.update(objectSelector, spotVariableCheckBox, spotVariableComboBox);

		if ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection"))) {
			CommonTool.update( _comboBox, get_spot_collection_names( false));

			CommonTool.update( _containsAllCollectionComboBox, get_spot_collection_names( false));
			CommonTool.update( _containsAllListComboBox, get_spot_list_names( false));
		} else {
			CommonTool.update( _comboBox, get_spot_list_names( false));

			CommonTool.update( _containsAllCollectionComboBox, get_spot_list_names( false));
			CommonTool.update( _containsAllListComboBox, get_spot_collection_names( false));
		}

		CommonTool.update( _containsEquipNumberObjectNumberObjectComboBox, get_spot_number_object_names( false));
		CommonTool.update( _containsEquipProbabilityProbabilityComboBox, get_spot_probability_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.object.entiy.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		super.update(spotObject, number, objectSelector, spotVariableCheckBox, spotVariableComboBox);

		if ( _type.equals( ResourceManager.get_instance().get( "rule.type.condition.collection"))) {
			CommonTool.update( _comboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "collection", number, false) : get_spot_collection_names( false));

			CommonTool.update( _containsAllCollectionComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "collection", number, false) : get_spot_collection_names( false));
			CommonTool.update( _containsAllListComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));
		} else {
			CommonTool.update( _comboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));

			CommonTool.update( _containsAllCollectionComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));
			CommonTool.update( _containsAllListComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "collection", number, false) : get_spot_collection_names( false));
		}

		CommonTool.update( _containsEquipNumberObjectNumberObjectComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));
		CommonTool.update( _containsEquipProbabilityProbabilityComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "probability", number, false) : get_spot_probability_names( false));
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
			false, false, false, false,
			false
		});
	}

	/**
	 * @param enables
	 */
	private void update_components(boolean[] enables) {
		_label[ 1].setEnabled( enables[ 1]);
		_containsAgentAgentSelector.setEnabled( enables[ 1]);

		_label[ 2].setEnabled( enables[ 2]);
		_containsSpotSpotSelector.setEnabled( enables[ 2]);

		_label[ 3].setEnabled( enables[ 3]);
		_containsEquipNumberObjectNumberObjectComboBox.setEnabled( enables[ 3]);

		_label[ 4].setEnabled( enables[ 4]);
		_containsEquipProbabilityProbabilityComboBox.setEnabled( enables[ 4]);

		_label[ 5].setEnabled( enables[ 5]);
		_containsStringTextField.setEnabled( enables[ 5]);

		_label[ 6].setEnabled( enables[ 6]);
		_containsAllCollectionComboBox.setEnabled( enables[ 6]);

		_label[ 7].setEnabled( enables[ 7]);
		_containsAllListComboBox.setEnabled( enables[ 7]);

		_label[ 8].setEnabled( enables[ 8]);
		_askAllTextField.setEnabled( enables[ 8]);
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

		int kind = CollectionAndListCondition.get_kind( rule);
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
				_comboBox.setSelectedItem( values[ 2]);
				break;
			case 1:
				set2( _comboBox, _containsAgentAgentSelector, values[ 2], ( 2 > elements.length) ? "" : elements[ 1]);
				break;
			case 2:
				set2( _comboBox, _containsSpotSpotSelector, values[ 2], ( 2 > elements.length) ? "" : elements[ 1]);
				break;
			case 3:
				set1( _comboBox, _containsEquipNumberObjectNumberObjectComboBox, values[ 2], elements[ 1]);
				break;
			case 4:
				set1( _comboBox, _containsEquipProbabilityProbabilityComboBox, values[ 2], elements[ 1]);
				break;
			case 5:
				set3( _comboBox, _containsStringTextField, values[ 2], elements[ 1]);
				break;
			case 6:
				set1( _comboBox, _containsAllCollectionComboBox, values[ 2], elements[ 1]);
				break;
			case 7:
				set1( _comboBox, _containsAllListComboBox, values[ 2], elements[ 1]);
				break;
			case 8:
				set3( _comboBox, _askAllTextField, values[ 2], elements[ 1]);
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
		String value = null;
		int kind = SwingTool.get_enabled_radioButton( _radioButtons1);
		switch ( kind) {
			case 0:
				value = get1( _comboBox);
				break;
			case 1:
				value = get3( _comboBox, _containsAgentAgentSelector);
				break;
			case 2:
				value = get3( _comboBox, _containsSpotSpotSelector);
				break;
			case 3:
				value = get2( _comboBox, _containsEquipNumberObjectNumberObjectComboBox, false);
				break;
			case 4:
				value = get2( _comboBox, _containsEquipProbabilityProbabilityComboBox, false);
				break;
			case 5:
				if ( null == _containsStringTextField.getText()
					|| _containsStringTextField.getText().equals( "")
					|| _containsStringTextField.getText().equals( "$")
					|| 0 < _containsStringTextField.getText().indexOf( '$')
					|| _containsStringTextField.getText().startsWith( " ")
					|| _containsStringTextField.getText().endsWith( " ")
					|| _containsStringTextField.getText().equals( "$Name")
					|| _containsStringTextField.getText().equals( "$Role")
					|| _containsStringTextField.getText().equals( "$Spot")
					|| 0 <= _containsStringTextField.getText().indexOf( Constant._experimentName))
					return null;

				if ( _containsStringTextField.getText().startsWith( "$")
					&& ( 0 <= _containsStringTextField.getText().indexOf( " ")
					|| 0 < _containsStringTextField.getText().indexOf( "$", 1)
					|| 0 < _containsStringTextField.getText().indexOf( ")", 1)))
					return null;

				value = get4( _comboBox, _containsStringTextField, false);
				break;
			case 6:
				value = get2( _comboBox, _containsAllCollectionComboBox, false);
				break;
			case 7:
				value = get2( _comboBox, _containsAllListComboBox, false);
				break;
			case 8:
				if ( null == _askAllTextField.getText()
					|| _askAllTextField.getText().equals( "")
					|| _askAllTextField.getText().equals( "$")
					|| 0 < _askAllTextField.getText().indexOf( '$')
					|| _askAllTextField.getText().startsWith( " ")
					|| _askAllTextField.getText().endsWith( " ")
					|| _askAllTextField.getText().equals( "$Name")
					|| _askAllTextField.getText().equals( "$Role")
					|| _askAllTextField.getText().equals( "$Spot")
					|| 0 <= _askAllTextField.getText().indexOf( Constant._experimentName))
					return null;

				if ( _askAllTextField.getText().startsWith( "$")
					&& ( 0 <= _askAllTextField.getText().indexOf( " ")
					|| 0 < _askAllTextField.getText().indexOf( "$", 1)
					|| 0 < _askAllTextField.getText().indexOf( ")", 1)))
					return null;

				value = get4( _comboBox, _askAllTextField, false);
				break;
			default:
				return null;
		}

		if ( null == value)
			return null;

		String spot = get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);

		return Rule.create( _kind, _type,
			( _checkBox.isSelected() ? "!" : "") + ( CollectionAndListCondition._reservedWords[ kind] + spot + value));
	}
}
