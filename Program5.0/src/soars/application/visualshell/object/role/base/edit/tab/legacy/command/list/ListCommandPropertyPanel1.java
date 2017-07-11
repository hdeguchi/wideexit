/*
 * 2005/08/03
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.command.list;

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
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.base.CollectionAndListCommandPropertyPanelBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.legacy.command.ListCommand;
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
public class ListCommandPropertyPanel1 extends CollectionAndListCommandPropertyPanelBase {

	/**
	 * 
	 */
	private RadioButton[][] _radioButtons2 = new RadioButton[][] {
		{ null, null, null, null}
	};

	/**
	 * 
	 */
	private ObjectSelector _addFirstAgentAgentSelector = null;

	/**
	 * 
	 */
	private ObjectSelector _addFirstSpotSpotSelector = null;

	/**
	 * 
	 */
	private ComboBox _addFirstEquipProbabilityComboBox = null;

	/**
	 * 
	 */
	private ComboBox _addFirstEquipKeywordComboBox = null;

	/**
	 * 
	 */
	private ComboBox _addFirstEquipNumberObjectComboBox = null;

	/**
	 * 
	 */
	private TextField _addFirstEquipObjectTextField = null;

	/**
	 * 
	 */
	private TextField _addFirstStringTextField = null;

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
	public ListCommandPropertyPanel1(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);

		_radioButtons1 = new RadioButton[] {
			null, null, null, null
		};

		_label = new JLabel[] {
			null, null, null, null
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

		setup_add_first_agent( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_add_first_spot( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_add_first_equip( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_add_first_string( buttonGroup1, northPanel);

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
	private void setup_add_first_agent(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.add.first.agent"),
			buttonGroup1, true, false);
		_radioButtons1[ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_addFirstAgentAgentSelector.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 0]);

		_label[ 1] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.label.add.first.agent"),
			true);
		panel.add( _label[ 1]);

		_addFirstAgentAgentSelector = create_agent_selector( true, true, panel);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_add_first_spot(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.add.first.spot"),
			buttonGroup1, true, false);
		_radioButtons1[ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 2].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_addFirstSpotSpotSelector.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 1]);

		_label[ 2] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.label.add.first.spot"),
			true);
		panel.add( _label[ 2]);

		_addFirstSpotSpotSelector = create_spot_selector( true, true, panel);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_add_first_equip(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.add.first.equip"),
			buttonGroup1, true, false);
		_radioButtons1[ 2].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_radioButtons2[ 0][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_addFirstEquipProbabilityComboBox.setEnabled(
					_radioButtons2[ 0][ 0].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 0][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_addFirstEquipKeywordComboBox.setEnabled(
					_radioButtons2[ 0][ 1].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 0][ 2].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_addFirstEquipNumberObjectComboBox.setEnabled(
					_radioButtons2[ 0][ 2].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 0][ 3].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_addFirstEquipObjectTextField.setEnabled(
					_radioButtons2[ 0][ 3].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 2]);



		ButtonGroup buttonGroup2 = new ButtonGroup();



		_radioButtons2[ 0][ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.add.first.equip.probability"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_addFirstEquipProbabilityComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 0]);

		_addFirstEquipProbabilityComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _addFirstEquipProbabilityComboBox);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 0][ 0] = new JLabel();
		panel.add( _dummy3[ 0][ 0]);

		_radioButtons2[ 0][ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.add.first.equip.keyword"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_addFirstEquipKeywordComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 1]);

		_addFirstEquipKeywordComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _addFirstEquipKeywordComboBox);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 0][ 1] = new JLabel();
		panel.add( _dummy3[ 0][ 1]);

		_radioButtons2[ 0][ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.add.first.equip.number.object"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 2].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_addFirstEquipNumberObjectComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 2]);

		_addFirstEquipNumberObjectComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _addFirstEquipNumberObjectComboBox);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 0][ 2] = new JLabel();
		panel.add( _dummy3[ 0][ 2]);

		_radioButtons2[ 0][ 3] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.add.first.equip.object"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 3].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_addFirstEquipObjectTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 3]);

		_addFirstEquipObjectTextField = create_textField( new TextExcluder( Constant._prohibitedCharacters1), _standardControlWidth, false);
		panel.add( _addFirstEquipObjectTextField);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_add_first_string(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 3] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.add.first.string"),
			buttonGroup1, true, false);
		_radioButtons1[ 3].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_label[ 3].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_addFirstStringTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 3]);

		_label[ 3] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.label.add.first.string"),
			true);
		panel.add( _label[ 3]);

		_addFirstStringTextField = create_textField( new TextExcluder( Constant._prohibitedCharacters3), _standardControlWidth, false);
		panel.add( _addFirstStringTextField);

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

		CommonTool.update( _comboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_list_names( false) : get_spot_list_names( false));

		CommonTool.update( _addFirstEquipProbabilityComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_probability_names( false) : get_spot_probability_names( false));
		CommonTool.update( _addFirstEquipKeywordComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_keyword_names( false) : get_spot_keyword_names( false));
		CommonTool.update( _addFirstEquipNumberObjectComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));
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

		CommonTool.update( _addFirstEquipProbabilityComboBox, get_spot_probability_names( false));
		CommonTool.update( _addFirstEquipKeywordComboBox, get_spot_keyword_names( false));
		CommonTool.update( _addFirstEquipNumberObjectComboBox, get_spot_number_object_names( false));
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

		CommonTool.update( _addFirstEquipProbabilityComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "probability", number, false) : get_spot_probability_names( false));
		CommonTool.update( _addFirstEquipKeywordComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "keyword", number, false) : get_spot_keyword_names( false));
		CommonTool.update( _addFirstEquipNumberObjectComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));
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
			true, false, false, false
		});
	}

	/**
	 * @param enables
	 */
	private void update_components(boolean[] enables) {
		_label[ 1].setEnabled( enables[ 0]);
		_addFirstAgentAgentSelector.setEnabled( enables[ 0]);

		_label[ 2].setEnabled( enables[ 1]);
		_addFirstSpotSpotSelector.setEnabled( enables[ 1]);

		for ( int i = 0; i < _radioButtons2[ 0].length; ++i)
			_radioButtons2[ 0][ i].setEnabled( enables[ 2]);

		_addFirstEquipProbabilityComboBox.setEnabled( enables[ 2]);
		_addFirstEquipKeywordComboBox.setEnabled( enables[ 2]);
		_addFirstEquipNumberObjectComboBox.setEnabled( enables[ 2]);
		_addFirstEquipObjectTextField.setEnabled( enables[ 2]);

		_label[ 3].setEnabled( enables[ 3]);
		_addFirstStringTextField.setEnabled( enables[ 3]);
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

		int kind = ListCommand.get_kind( rule._value);
		if ( 0 > kind || 6 < kind) {
			set_handler();
			return false;
		} else if ( 1 >= kind)
			_radioButtons1[ kind].setSelected( true);
		else if ( 2 <= kind && 5 >= kind) {
			_radioButtons1[ 2].setSelected( true);
			_radioButtons2[ 0][ kind - 2].setSelected( true);
		} else if ( 6 == kind)
			_radioButtons1[ 3].setSelected( true);
		else
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
			case 0:
				set2( _comboBox, _addFirstAgentAgentSelector, values[ 2], ( 2 > elements.length) ? "" : elements[ 1]);
				break;
			case 1:
				set2( _comboBox, _addFirstSpotSpotSelector, values[ 2], ( 2 > elements.length) ? "" : elements[ 1]);
				break;
			case 2:
				set1( _comboBox, _addFirstEquipProbabilityComboBox, values[ 2], elements[ 1]);
				break;
			case 3:
				set1( _comboBox, _addFirstEquipKeywordComboBox, values[ 2], elements[ 1]);
				break;
			case 4:
				set1( _comboBox, _addFirstEquipNumberObjectComboBox, values[ 2], elements[ 1]);
				break;
			case 5:
				set3( _comboBox, _addFirstEquipObjectTextField, values[ 2], elements[ 1]);
				break;
			case 6:
				set3( _comboBox, _addFirstStringTextField, values[ 2], elements[ 1]);
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
		if ( 2 == kind)
			kind = ( 2 + SwingTool.get_enabled_radioButton( _radioButtons2[ 0]));
		else if ( 3 == kind)
			kind = 6;
		else if ( 0 > kind || 6 < kind)
			return null;

		String spot = get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);

		String value = null;
		switch ( kind) {
			case 0:
				value = get3( _comboBox, _addFirstAgentAgentSelector);
				break;
			case 1:
				value = get3( _comboBox, _addFirstSpotSpotSelector);
				break;
			case 2:
				value = get2( _comboBox, _addFirstEquipProbabilityComboBox, false);
				break;
			case 3:
				value = get2( _comboBox, _addFirstEquipKeywordComboBox, false);
				break;
			case 4:
				value = get2( _comboBox, _addFirstEquipNumberObjectComboBox, false);
				break;
			case 5:
				if ( null == _addFirstEquipObjectTextField.getText()
					|| _addFirstEquipObjectTextField.getText().equals( "")
					|| _addFirstEquipObjectTextField.getText().equals( "$")
					|| 0 < _addFirstEquipObjectTextField.getText().indexOf( '$')
					|| _addFirstEquipObjectTextField.getText().equals( "$Name")
					|| _addFirstEquipObjectTextField.getText().equals( "$Role")
					|| _addFirstEquipObjectTextField.getText().equals( "$Spot")
					|| 0 <= _addFirstEquipObjectTextField.getText().indexOf( Constant._experimentName))
					return null;

				if ( _addFirstEquipObjectTextField.getText().startsWith( "$")
					&& ( 0 < _addFirstEquipObjectTextField.getText().indexOf( "$", 1)
					|| 0 < _addFirstEquipObjectTextField.getText().indexOf( ")", 1)))
					return null;

				// TODO 要動作確認！
				if ( !is_object( spot, _addFirstEquipObjectTextField.getText(),
					new String[] {
						"collection",
						"list",
						"spot variable",
						"role variable",
						"time variable",
						"map",
						"exchange algebra",
						"file",
						"class variable"}))
					return null;

				value = get4( _comboBox, _addFirstEquipObjectTextField, false);
				break;
			case 6:
				if ( null == _addFirstStringTextField.getText()
					|| _addFirstStringTextField.getText().equals( "")
					|| _addFirstStringTextField.getText().equals( "$")
					|| 0 < _addFirstStringTextField.getText().indexOf( '$')
					|| _addFirstStringTextField.getText().startsWith( " ")
					|| _addFirstStringTextField.getText().endsWith( " ")
					|| _addFirstStringTextField.getText().equals( "$Name")
					|| _addFirstStringTextField.getText().equals( "$Role")
					|| _addFirstStringTextField.getText().equals( "$Spot")
					|| 0 <= _addFirstStringTextField.getText().indexOf( Constant._experimentName))
					return null;

				if ( _addFirstStringTextField.getText().startsWith( "$")
					&& ( 0 <= _addFirstStringTextField.getText().indexOf( " ")
					|| 0 < _addFirstStringTextField.getText().indexOf( "$", 1)
					|| 0 < _addFirstStringTextField.getText().indexOf( ")", 1)))
					return null;

				value = get4( _comboBox, _addFirstStringTextField, false);
				break;
			default:
				return null;
		}

		if ( null == value)
			return null;

		return Rule.create( _kind, _type, ListCommand._reservedWords[ kind] + spot + value);
	}
}
