/*
 * Created on 2006/02/14
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.command.collection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.base.CollectionAndListCommandPropertyPanelBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.legacy.command.CollectionCommand;
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
public class CollectionCommandPropertyPanel2 extends CollectionAndListCommandPropertyPanelBase {

	/**
	 * 
	 */
	private ObjectSelector _removeSpotSpotSelector = null;

	/**
	 * 
	 */
	private ObjectSelector _removeAgentAgentSelector = null;

	/**
	 * 
	 */
	private TextField _removeStringTextField = null;

	/**
	 * 
	 */
	private ComboBox _removeAllCollectionComboBox = null;

	/**
	 * 
	 */
	private ComboBox _removeAllListComboBox = null;

	/**
	 * 
	 */
	private RadioButton[][] _radioButtons2 = new RadioButton[][] {
		{ null, null, null, null}
	};

	/**
	 * 
	 */
	private ComboBox _removeEquipProbabilityComboBox = null;

	/**
	 * 
	 */
	private ComboBox _removeEquipKeywordComboBox = null;

	/**
	 * 
	 */
	private ComboBox _removeEquipNumberObjectComboBox = null;

	/**
	 * 
	 */
	private TextField _removeEquipObjectTextField = null;

	/**
	 * 
	 */
	private JLabel[][] _dummy3 = new JLabel[][] {
		{ null, null, null}
	};

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
	public CollectionCommandPropertyPanel2(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);

		_radioButtons1 = new RadioButton[] {
			null, null, null, null,
			null, null
		};

		_label = new JLabel[] {
			null, null, null, null,
			null, null
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

		setup_remove_spot( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_remove_agent( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_remove_string( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_remove_all1( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_remove_all2( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_remove_equip( buttonGroup1, northPanel);

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
	private void setup_remove_spot(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.remove.spot"),
			buttonGroup1, true, false);
		_radioButtons1[ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_removeSpotSpotSelector.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 0]);

		_label[ 1] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.label.remove.spot"),
			true);
		panel.add( _label[ 1]);

		_removeSpotSpotSelector = create_spot_selector( true, true, panel);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_remove_agent(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.remove.agent"),
			buttonGroup1, true, false);
		_radioButtons1[ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 2].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_removeAgentAgentSelector.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 1]);

		_label[ 2] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.label.remove.agent"),
			true);
		panel.add( _label[ 2]);

		_removeAgentAgentSelector = create_agent_selector( true, true, panel);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_remove_string(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.remove.string"),
			buttonGroup1, true, false);
		_radioButtons1[ 2].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 3].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_removeStringTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 2]);

		_label[ 3] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.label.remove.string"),
			true);
		panel.add( _label[ 3]);

		_removeStringTextField = create_textField( new TextExcluder( Constant._prohibitedCharacters3), _standardControlWidth, false);
		panel.add( _removeStringTextField);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_remove_all1(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 3] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.remove.all"),
			buttonGroup1, true, false);
		_radioButtons1[ 3].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 4].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_removeAllCollectionComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 3]);

		_label[ 4] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.label.remove.all"),
			true);
		panel.add( _label[ 4]);

		_removeAllCollectionComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _removeAllCollectionComboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_remove_all2(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 4] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.remove.all"),
			buttonGroup1, true, false);
		_radioButtons1[ 4].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 5].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_removeAllListComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 4]);

		_label[ 5] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.label.remove.all"),
			true);
		panel.add( _label[ 5]);

		_removeAllListComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _removeAllListComboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_remove_equip(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 5] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.remove.equip"),
			buttonGroup1, true, false);
		_radioButtons1[ 5].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_radioButtons2[ 0][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_removeEquipProbabilityComboBox.setEnabled(
					_radioButtons2[ 0][ 0].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 0][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_removeEquipKeywordComboBox.setEnabled(
					_radioButtons2[ 0][ 1].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 0][ 2].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_removeEquipNumberObjectComboBox.setEnabled(
					_radioButtons2[ 0][ 2].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 0][ 3].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_removeEquipObjectTextField.setEnabled(
					_radioButtons2[ 0][ 3].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 5]);



		ButtonGroup buttonGroup2 = new ButtonGroup();



		_radioButtons2[ 0][ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.remove.equip.probability"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_removeEquipProbabilityComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 0]);

		_removeEquipProbabilityComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _removeEquipProbabilityComboBox);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 0][ 0] = new JLabel();
		panel.add( _dummy3[ 0][ 0]);

		_radioButtons2[ 0][ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.remove.equip.keyword"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_removeEquipKeywordComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 1]);

		_removeEquipKeywordComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _removeEquipKeywordComboBox);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 0][ 1] = new JLabel();
		panel.add( _dummy3[ 0][ 1]);

		_radioButtons2[ 0][ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.remove.equip.number.object"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 2].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_removeEquipNumberObjectComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 2]);

		_removeEquipNumberObjectComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _removeEquipNumberObjectComboBox);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 0][ 2] = new JLabel();
		panel.add( _dummy3[ 0][ 2]);

		_radioButtons2[ 0][ 3] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.remove.equip.object"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 3].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_removeEquipObjectTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 3]);

		_removeEquipObjectTextField = create_textField( new TextExcluder( Constant._prohibitedCharacters1), _standardControlWidth, false);
		panel.add( _removeEquipObjectTextField);

		parent.add( panel);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.command.base.CollectionAndListCommandPropertyPanelBase#adjust()
	 */
	@Override
	protected void adjust() {
		super.adjust();

		for ( int i = 0; i < _dummy3.length; ++i) {
			for ( int j = 0; j < _dummy3[ i].length; ++j) {
				_dummy3[ i][ j].setPreferredSize( new Dimension(
					_dummy1.getPreferredSize().width,
					_dummy3[ i][ j].getPreferredSize().height));
			}
		}


		int width = _spotCheckBox.getPreferredSize().width;
		for ( int i = 0; i < _radioButtons2.length; ++i) {
			for ( int j = 0; j < _radioButtons2[ i].length; ++j) {
				width = Math.max( width, _radioButtons2[ i][ j].getPreferredSize().width);
			}
		}

		_spotCheckBox.setPreferredSize( new Dimension( width,
			_spotCheckBox.getPreferredSize().height));

		for ( int i = 0; i < _label.length; ++i)
			_label[ i].setPreferredSize( new Dimension( width,
				_label[ i].getPreferredSize().height));

		for ( int i = 0; i < _radioButtons2.length; ++i) {
			for ( int j = 0; j < _radioButtons2[ i].length; ++j) {
				_radioButtons2[ i][ j].setPreferredSize( new Dimension( width,
					_radioButtons2[ i][ j].getPreferredSize().height));
			}
		}
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

		CommonTool.update( _comboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_collection_names( false) : get_spot_collection_names( false));

		CommonTool.update( _removeAllCollectionComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_collection_names( false) : get_spot_collection_names( false));
		CommonTool.update( _removeAllListComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_list_names( false) : get_spot_list_names( false));

		CommonTool.update( _removeEquipProbabilityComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_probability_names( false) : get_spot_probability_names( false));
		CommonTool.update( _removeEquipKeywordComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_keyword_names( false) : get_spot_keyword_names( false));
		CommonTool.update( _removeEquipNumberObjectComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		super.update(objectSelector, spotVariableCheckBox, spotVariableComboBox);

		CommonTool.update( _comboBox, get_spot_collection_names( false));

		CommonTool.update( _removeAllCollectionComboBox, get_spot_collection_names( false));
		CommonTool.update( _removeAllListComboBox, get_spot_list_names( false));

		CommonTool.update( _removeEquipProbabilityComboBox, get_spot_probability_names( false));
		CommonTool.update( _removeEquipKeywordComboBox, get_spot_keyword_names( false));
		CommonTool.update( _removeEquipNumberObjectComboBox, get_spot_number_object_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.object.entiy.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		super.update(spotObject, number, objectSelector, spotVariableCheckBox, spotVariableComboBox);

		CommonTool.update( _comboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "collection", number, false) : get_spot_collection_names( false));

		CommonTool.update( _removeAllCollectionComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "collection", number, false) : get_spot_collection_names( false));
		CommonTool.update( _removeAllListComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));

		CommonTool.update( _removeEquipProbabilityComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "probability", number, false) : get_spot_probability_names( false));
		CommonTool.update( _removeEquipKeywordComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "keyword", number, false) : get_spot_keyword_names( false));
		CommonTool.update( _removeEquipNumberObjectComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));
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
		_radioButtons2[ 0][ 0].setSelected( true);
		update_components( new boolean[] {
			true, false, false, false,
			false, false
		});
	}

	/**
	 * @param enables
	 */
	private void update_components(boolean[] enables) {
		_label[ 1].setEnabled( enables[ 0]);
		_removeSpotSpotSelector.setEnabled( enables[ 0]);

		_label[ 2].setEnabled( enables[ 1]);
		_removeAgentAgentSelector.setEnabled( enables[ 1]);

		_label[ 3].setEnabled( enables[ 2]);
		_removeStringTextField.setEnabled( enables[ 2]);

		_label[ 4].setEnabled( enables[ 3]);
		_removeAllCollectionComboBox.setEnabled( enables[ 3]);

		_label[ 5].setEnabled( enables[ 4]);
		_removeAllListComboBox.setEnabled( enables[ 4]);

		for ( int i = 0; i < _radioButtons2[ 0].length; ++i)
			_radioButtons2[ 0][ i].setEnabled( enables[ 5]);

		_removeEquipProbabilityComboBox.setEnabled( enables[ 5]);
		_removeEquipKeywordComboBox.setEnabled( false);
		_removeEquipNumberObjectComboBox.setEnabled( false);
		_removeEquipObjectTextField.setEnabled( false);
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

		int kind = CollectionCommand.get_kind( rule._value);
		if ( 9 > kind || 17 < kind) {
			set_handler();
			return false;
		} else if ( 9 <= kind && 13 >= kind) {
			_radioButtons1[ kind - 9].setSelected( true);
		} else if ( 14 <= kind && 17 >= kind) {
			_radioButtons1[ 5].setSelected( true);
			_radioButtons2[ 0][ kind - 14].setSelected( true);
		} else
			return false;

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
			case 9:
				set2( _comboBox, _removeSpotSpotSelector, values[ 2], ( 2 > elements.length) ? "" : elements[ 1]);
				break;
			case 10:
				set2( _comboBox, _removeAgentAgentSelector, values[ 2], ( 2 > elements.length) ? "" : elements[ 1]);
				break;
			case 11:
				set3( _comboBox, _removeStringTextField, values[ 2], elements[ 1]);
				break;
			case 12:
				set1( _comboBox, _removeAllCollectionComboBox, values[ 2], elements[ 1]);
				break;
			case 13:
				set1( _comboBox, _removeAllListComboBox, values[ 2], elements[ 1]);
				break;
			case 14:
				set1( _comboBox, _removeEquipProbabilityComboBox, values[ 2], elements[ 1]);
				break;
			case 15:
				set1( _comboBox, _removeEquipKeywordComboBox, values[ 2], elements[ 1]);
				break;
			case 16:
				set1( _comboBox, _removeEquipNumberObjectComboBox, values[ 2], elements[ 1]);
				break;
			case 17:
				set3( _comboBox, _removeEquipObjectTextField, values[ 2], elements[ 1]);
				break;
		}

		set_handler();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get()
	 */
	@Override
	public Rule get() {
		int kind = SwingTool.get_enabled_radioButton( _radioButtons1);

		if ( 5 > kind)
			kind += 9;
		else if ( 5 == kind)
			kind = ( 14 + SwingTool.get_enabled_radioButton( _radioButtons2[ 0]));
		else
			return null;

		String spot = get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);

		String value = null;
		switch ( kind) {
			case 9:
				value = get3( _comboBox, _removeSpotSpotSelector);
				break;
			case 10:
				value = get3( _comboBox, _removeAgentAgentSelector);
				break;
			case 11:
				if ( null == _removeStringTextField.getText()
					|| _removeStringTextField.getText().equals( "")
					|| _removeStringTextField.getText().equals( "$")
					|| 0 < _removeStringTextField.getText().indexOf( '$')
					|| _removeStringTextField.getText().startsWith( " ")
					|| _removeStringTextField.getText().endsWith( " ")
					|| _removeStringTextField.getText().equals( "$Name")
					|| _removeStringTextField.getText().equals( "$Role")
					|| _removeStringTextField.getText().equals( "$Spot")
					|| 0 <= _removeStringTextField.getText().indexOf( Constant._experimentName))
					return null;
	
				if ( _removeStringTextField.getText().startsWith( "$")
					&& ( 0 <= _removeStringTextField.getText().indexOf( " ")
					|| 0 < _removeStringTextField.getText().indexOf( "$", 1)
					|| 0 < _removeStringTextField.getText().indexOf( ")", 1)))
					return null;
	
				value = get4( _comboBox, _removeStringTextField, false);
				break;
			case 12:
				value = get2( _comboBox, _removeAllCollectionComboBox, false);
				break;
			case 13:
				value = get2( _comboBox, _removeAllListComboBox, false);
				break;
			case 14:
				value = get2( _comboBox, _removeEquipProbabilityComboBox, false);
				break;
			case 15:
				value = get2( _comboBox, _removeEquipKeywordComboBox, false);
				break;
			case 16:
				value = get2( _comboBox, _removeEquipNumberObjectComboBox, false);
				break;
			case 17:
				if ( null == _removeEquipObjectTextField.getText()
					|| _removeEquipObjectTextField.getText().equals( "")
					|| _removeEquipObjectTextField.getText().equals( "$")
					|| 0 < _removeEquipObjectTextField.getText().indexOf( '$'))
					return null;

				if ( _removeEquipObjectTextField.getText().startsWith( "$")
					&& 0 < _removeEquipObjectTextField.getText().indexOf( "$", 1))
					return null;

				if ( CommonRuleManipulator.is_object( "probability", _removeEquipObjectTextField.getText(), LayerManager.get_instance())
					|| CommonRuleManipulator.is_object( "keyword", _removeEquipObjectTextField.getText(), LayerManager.get_instance())
					|| CommonRuleManipulator.is_object( "number object", _removeEquipObjectTextField.getText(), LayerManager.get_instance()))
					return null;

				// TODO 要動作確認！
				if ( !is_object( spot, _removeEquipObjectTextField.getText(),
					new String[] {
						"spot variable",
						"role variable",
						"time variable",
						"map",
						"exchange algebra",
						"file",
						"class variable"}))
					return null;

				value = get4( _comboBox, _removeEquipObjectTextField, false);
				break;
			default:
				return null;
		}

		if ( null == value)
			return null;

		return Rule.create( _kind, _type, CollectionCommand._reservedWords[ kind] + spot + value);
	}
}
