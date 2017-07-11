/*
 * Created on 2006/08/24
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.command;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.object.class_variable.ClassVariableObject;
import soars.application.visualshell.object.entity.base.object.number.NumberObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.legacy.command.CreateObjectCommand;
import soars.application.visualshell.object.role.base.object.legacy.common.time.TimeRule;
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
public class CreateObjectCommandPropertyPanel extends RulePropertyPanelBase {

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
	private JLabel _dummy1 = null;

	/**
	 * 
	 */
	private RadioButton[] _radioButtons1 = new RadioButton[] {
		null, null, null, null,
		null, null, null, null,
		null, null
	};

	/**
	 * 
	 */
	private ComboBox _probabilityComboBox = null;

	/**
	 * 
	 */
	private ComboBox _collectionComboBox = null;

	/**
	 * 
	 */
	private ComboBox _listComboBox = null;

	/**
	 * 
	 */
	private ComboBox _mapComboBox = null;

	/**
	 * 
	 */
	private ComboBox _keywordComboBox = null;

	/**
	 * 
	 */
	private ComboBox _numberObjectComboBox = null;

	/**
	 * 
	 */
	private ComboBox _roleVariableComboBox = null;

	/**
	 * 
	 */
	private ComboBox _timeVariableComboBox = null;

	/**
	 * 
	 */
	private ComboBox _spotVariableComboBox2 = null;

	/**
	 * 
	 */
	private ComboBox _classVariableComboBox = null;

	/**
	 * 
	 */
	private TextField _probabilityInitialValueTextField = null;

	/**
	 * 
	 */
	private TextField _keywordInitialValueTextField = null;

	/**
	 * 
	 */
	private TextField _numberObjectTypeTextField = null;

	/**
	 * 
	 */
	private TextField _numberObjectInitialValueTextField = null;

	/**
	 * 
	 */
	private ComboBox _roleComboBox = null;

	/**
	 * 
	 */
	private TextField _timeTextField = null;

	/**
	 * 
	 */
	private ComboBox[] _timeComboBoxes = new ComboBox[] { null, null};

	/**
	 * 
	 */
	private JLabel[] _timeLabels = new JLabel[] { null, null};

	/**
	 * 
	 */
	private ObjectSelector _spotSelector2 = null;

	/**
	 * 
	 */
	private TextField _classnameTextField = null;

	/**
	 * 
	 */
	private TextField _jarFilenameTextField = null;

	/**
	 * 
	 */
	private JLabel[][] _labels = new JLabel[][] {
		{ null},
		{ null},
		{ null, null},
		{ null},
		{ null},
		{ null},
		{ null, null}
	};

//	/**
//	 * 
//	 */
//	private JLabel[] _dummy2 = new JLabel[] {
//		null, null
//	};

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
	public CreateObjectCommandPropertyPanel(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
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

		setup_spot_checkBox_and_spot_selector( northPanel);

		insert_vertical_strut( northPanel);

		setup_spot_variable_checkBox_and_spot_variable_comboBox( northPanel);

		insert_vertical_strut( northPanel);

		ButtonGroup buttonGroup1 = new ButtonGroup();

		setup_probability( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_collection( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_list( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_map( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_keyword( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_number_object( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_role_variable( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_time_variable( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_spot_variable( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_class_variable( buttonGroup1, northPanel);

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
	private void setup_spot_checkBox_and_spot_selector(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy1 = new JLabel();
		panel.add( _dummy1);

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
				_radioButtons1[ 6].setEnabled( !_spotVariableCheckBox.isSelected() && _role instanceof AgentRole && ItemEvent.SELECTED != arg0.getStateChange());
				if ( _spotVariableCheckBox.isSelected() || _role instanceof SpotRole || ItemEvent.SELECTED == arg0.getStateChange()) {
					_roleVariableComboBox.setEnabled( false);
					_labels[ 3][ 0].setEnabled( false);
					_roleComboBox.setEnabled( false);
				}
			}
		});
		panel.add( _spotCheckBox);

		_spotSelector = create_spot_selector( true, true, panel);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_spot_variable_checkBox_and_spot_variable_comboBox( JPanel parent) {
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
				_radioButtons1[ 6].setEnabled( !_spotCheckBox.isSelected() && _role instanceof AgentRole && ItemEvent.SELECTED != arg0.getStateChange());
				if ( _spotCheckBox.isSelected() || _role instanceof SpotRole || ItemEvent.SELECTED == arg0.getStateChange()) {
					_roleVariableComboBox.setEnabled( false);
					_labels[ 3][ 0].setEnabled( false);
					_roleComboBox.setEnabled( false);
				}
			}
		});
		panel.add( _spotVariableCheckBox);

		_spotVariableComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _spotVariableComboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_probability(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.probability"),
			buttonGroup1, true, false);
		_radioButtons1[ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_probabilityComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_labels[ 0][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_probabilityInitialValueTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 0]);

		_probabilityComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _probabilityComboBox);

		_labels[ 0][ 0] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.probability.initial.value"),
			true);
		panel.add( _labels[ 0][ 0]);

		_probabilityInitialValueTextField = create_textField( new TextExcluder( Constant._prohibitedCharacters5), _standardControlWidth, true);
		panel.add( _probabilityInitialValueTextField);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_collection(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.collection"),
			buttonGroup1, true, false);
		_radioButtons1[ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_collectionComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 1]);

		_collectionComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _collectionComboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_list(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.list"),
			buttonGroup1, true, false);
		_radioButtons1[ 2].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_listComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 2]);

		_listComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _listComboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_map(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 3] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.map"),
			buttonGroup1, true, false);
		_radioButtons1[ 3].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_mapComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 3]);

		_mapComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _mapComboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_keyword(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 4] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.keyword"),
			buttonGroup1, true, false);
		_radioButtons1[ 4].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_keywordComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_labels[ 1][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_keywordInitialValueTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 4]);

		_keywordComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _keywordComboBox);

		_labels[ 1][ 0] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.keyword.initial.value"),
			true);
		panel.add( _labels[ 1][ 0]);

		_keywordInitialValueTextField = create_textField( new TextExcluder( Constant._prohibitedCharacters3), _standardControlWidth, false);
		panel.add( _keywordInitialValueTextField);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_number_object(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 5] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.number.object"),
			buttonGroup1, true, false);
		_radioButtons1[ 5].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_numberObjectComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_labels[ 2][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_numberObjectTypeTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_labels[ 2][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_numberObjectInitialValueTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 5]);

		_numberObjectComboBox = create_comboBox( null, _standardControlWidth, false);
		_numberObjectComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_update_number_object_comboBox();
			}
		});
		panel.add( _numberObjectComboBox);

		_labels[ 2][ 0] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.number.object.type"),
			true);
		panel.add( _labels[ 2][ 0]);

		_numberObjectTypeTextField = create_textField( _standardControlWidth, false);
		_numberObjectTypeTextField.setEditable( false);
		panel.add( _numberObjectTypeTextField);

		parent.add( panel);


//		insert_vertical_strut( parent);
//
//
//		panel = new JPanel();
//		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));
//
//		_dummy2[ 0] = new JLabel();
//		panel.add( _dummy2[ 0]);

		_labels[ 2][ 1] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.number.object.initial.value"),
			true);
		panel.add( _labels[ 2][ 1]);

		_numberObjectInitialValueTextField = create_textField( new TextExcluder( Constant._prohibitedCharacters5), _standardControlWidth, true);
		panel.add( _numberObjectInitialValueTextField);

		parent.add( panel);
	}

	/**
	 * 
	 */
	protected void on_update_number_object_comboBox() {
		String number_object_name = ( String)_numberObjectComboBox.getSelectedItem();
		if ( null == number_object_name || number_object_name.equals( "")) {
			_numberObjectTypeTextField.setText( "");
			return;
		}

		String spot = get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);
		spot = CommonRuleManipulator.get_semantic_prefix( spot);
		if ( null == spot) {
			_numberObjectTypeTextField.setText( "");
			return;
		}

		String number_object_type = CreateObjectCommand.get_number_object_type( spot, number_object_name);
//			( !_spot_checkBox.isSelected() ? null : _spot_selector.get()), number_object_name);
		if ( null == number_object_type) {
			_numberObjectTypeTextField.setText( "");
			return;
		}

		String number_object_type_name = NumberObject.get_type_name( number_object_type);
		if ( number_object_type_name.equals( "")) {
			_numberObjectTypeTextField.setText( "");
			return;
		}

		_numberObjectTypeTextField.setText( number_object_type_name);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_role_variable(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 6] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.role.variable"),
			buttonGroup1, true, false);
		_radioButtons1[ 6].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_roleVariableComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_labels[ 3][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_roleComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 6]);

		_roleVariableComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _roleVariableComboBox);

		_labels[ 3][ 0] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.role.variable.role"),
			true);
		panel.add( _labels[ 3][ 0]);

		_roleComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _roleComboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_time_variable(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 7] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.time.variable"),
			buttonGroup1, true, false);
		_radioButtons1[ 7].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_timeVariableComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_labels[ 4][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_timeTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_timeLabels[ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_timeComboBoxes[ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_timeLabels[ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_timeComboBoxes[ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 7]);

		_timeVariableComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _timeVariableComboBox);

		_labels[ 4][ 0] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.time.variable.initial.value"),
			true);
		panel.add( _labels[ 4][ 0]);

		_timeTextField = create_textField( new TextExcluder( Constant._prohibitedCharacters14), 52, true);
		panel.add( _timeTextField);

		_timeLabels[ 0] = new JLabel( " / ");
		panel.add( _timeLabels[ 0]);

		_timeComboBoxes[ 0] = create_comboBox( CommonTool.get_hours(), 52, true);
		panel.add( _timeComboBoxes[ 0]);

		_timeLabels[ 1] = new JLabel( " : ");
		panel.add( _timeLabels[ 1]);

		_timeComboBoxes[ 1] = create_comboBox( CommonTool.get_minutes00(), 52, true);
		panel.add( _timeComboBoxes[ 1]);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_spot_variable(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 8] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.spot.variable"),
			buttonGroup1, true, false);
		_radioButtons1[ 8].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_spotVariableComboBox2.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_labels[ 5][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_spotSelector2.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 8]);

		_spotVariableComboBox2 = create_comboBox( null, _standardControlWidth, false);
		panel.add( _spotVariableComboBox2);

		_labels[ 5][ 0] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.spot.variable.spot"),
			true);
		panel.add( _labels[ 5][ 0]);

		_spotSelector2 = create_spot_selector( true, true, panel);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_class_variable(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 9] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.class.variable"),
			buttonGroup1, true, false);
		_radioButtons1[ 9].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if ( !Environment.get_instance().is_functional_object_enable())
					return;

				_classVariableComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_labels[ 6][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_classnameTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_labels[ 6][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_jarFilenameTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 9]);

		_classVariableComboBox = create_comboBox( null, _standardControlWidth, false);
		_classVariableComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_update_class_variable_comboBox();
			}
		});
		panel.add( _classVariableComboBox);

		_labels[ 6][ 0] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.class.variable.class.name"),
			true);
		panel.add( _labels[ 6][ 0]);

		_classnameTextField = create_textField( _standardControlWidth, false);
		_classnameTextField.setEditable( false);
		panel.add( _classnameTextField);

		parent.add( panel);


//		insert_vertical_strut( parent);
//
//
//		panel = new JPanel();
//		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));
//
//		_dummy2[ 1] = new JLabel();
//		panel.add( _dummy2[ 1]);

		_labels[ 6][ 1] = create_label( "  " +
			ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object.class.variable.jar.filename"),
			true);
		panel.add( _labels[ 6][ 1]);

		_jarFilenameTextField = create_textField( _standardControlWidth, false);
		_jarFilenameTextField.setEditable( false);
		panel.add( _jarFilenameTextField);

		parent.add( panel);
	}

	/**
	 * 
	 */
	protected void on_update_class_variable_comboBox() {
		String class_variable_name = ( String)_classVariableComboBox.getSelectedItem();
		if ( null == class_variable_name || class_variable_name.equals( "")) {
			_classnameTextField.setText( "");
			_jarFilenameTextField.setText( "");
			return;
		}

		String spot = get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);
		spot = CommonRuleManipulator.get_semantic_prefix( spot);
		if ( null == spot) {
			_classnameTextField.setText( "");
			_jarFilenameTextField.setText( "");
			return;
		}

		ClassVariableObject classVariableObject;
//		if ( !_spot_checkBox.isSelected())
		if ( spot.equals( ""))
			classVariableObject = get_agent_class_variable( class_variable_name);
		else
			classVariableObject = get_spot_class_variable( class_variable_name);
		if ( null == classVariableObject) {
			return;
		}

		if ( null == classVariableObject._classname || classVariableObject._classname.equals( ""))
			_classnameTextField.setText( "");
		else {
			String[] words = classVariableObject._classname.split( "\\.");
			_classnameTextField.setText( ( null != words && 0 < words.length) ? words[ words.length - 1] : "");
		}

		_classnameTextField.setToolTipText( classVariableObject._classname);

		if ( null == classVariableObject._jarFilename || classVariableObject._jarFilename.equals( ""))
			_jarFilenameTextField.setText( "");
		else {
			String[] words = classVariableObject._jarFilename.split( "/");
			_jarFilenameTextField.setText( ( null != words && 0 < words.length) ? words[ words.length - 1] : "");
		}

		_jarFilenameTextField.setToolTipText( classVariableObject._jarFilename);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = _spotCheckBox.getPreferredSize().width;
		width = Math.max( width, _spotVariableCheckBox.getPreferredSize().width);
		for ( int i = 0; i < _radioButtons1.length; ++i)
			width = Math.max( width, _radioButtons1[ i].getPreferredSize().width);

		_spotCheckBox.setPreferredSize( new Dimension( width,
			_spotCheckBox.getPreferredSize().height));

		_spotVariableCheckBox.setPreferredSize( new Dimension( width,
			_spotVariableCheckBox.getPreferredSize().height));

		Dimension dimension = new Dimension( width,
			_radioButtons1[ 0].getPreferredSize().height);
		for ( int i = 0; i < _radioButtons1.length; ++i)
			_radioButtons1[ i].setPreferredSize( dimension);


		_dummy1.setPreferredSize( new Dimension(
			_radioButtons1[ 0].getPreferredSize().width
				- _spotCheckBox.getPreferredSize().width - 5,
			_dummy1.getPreferredSize().height));


//		_dummy2[ 0].setPreferredSize( new Dimension(
//			_radioButtons1[ 4].getPreferredSize().width
//				+ 5
//				+ _number_object_comboBox.getPreferredSize().width,
//			_dummy2[ 0].getPreferredSize().height));
//
//
//		_dummy2[ 1].setPreferredSize( new Dimension(
//			_radioButtons1[ 5].getPreferredSize().width
//				+ 5
//				+ _class_variable_comboBox.getPreferredSize().width,
//			_dummy2[ 1].getPreferredSize().height));


		width = 0;
		for ( int i = 0; i < _labels.length; ++i)
			width = Math.max( width, _labels[ i][ 0].getPreferredSize().width);

		for ( int i = 0; i < _labels.length; ++i)
			_labels[ i][ 0].setPreferredSize( new Dimension( width, _labels[ i][ 0].getPreferredSize().height));


		width = 0;
		for ( int i = 0; i < _labels.length; ++i) {
			if ( 1 < _labels[ i].length)
				width = Math.max( width, _labels[ i][ 1].getPreferredSize().width);
		}

		for ( int i = 0; i < _labels.length; ++i) {
			if ( 1 < _labels[ i].length)
				_labels[ i][ 1].setPreferredSize( new Dimension( width, _labels[ i][ 1].getPreferredSize().height));
		}


		width = ( _timeTextField.getPreferredSize().width
			+ _timeLabels[ 0].getPreferredSize().width
			+ _timeComboBoxes[ 0].getPreferredSize().width
			+ _timeLabels[ 1].getPreferredSize().width
			+ _timeComboBoxes[ 1].getPreferredSize().width
			+ 20);

		_probabilityInitialValueTextField.setPreferredSize(
			new Dimension( width, _probabilityInitialValueTextField.getPreferredSize().height));
		_keywordInitialValueTextField.setPreferredSize(
			new Dimension( width, _keywordInitialValueTextField.getPreferredSize().height));
		_numberObjectTypeTextField.setPreferredSize(
			new Dimension( width, _numberObjectTypeTextField.getPreferredSize().height));
		_numberObjectInitialValueTextField.setPreferredSize(
			new Dimension( width, _numberObjectInitialValueTextField.getPreferredSize().height));
		_roleComboBox.setPreferredSize(
			new Dimension( width, _roleComboBox.getPreferredSize().height));
		_spotSelector2.setWidth( width);
		_classnameTextField.setPreferredSize(
			new Dimension( width, _classnameTextField.getPreferredSize().height));
		_jarFilenameTextField.setPreferredSize(
			new Dimension( width, _jarFilenameTextField.getPreferredSize().height));
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

		objectSelector.setEnabled( false);

		CommonTool.update( _probabilityComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_probability_names( false) : get_spot_probability_names( false));
		CommonTool.update( _collectionComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_collection_names( false) : get_spot_collection_names( false));
		CommonTool.update( _listComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_list_names( false) : get_spot_list_names( false));
		CommonTool.update( _mapComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_map_names( false) : get_spot_map_names( false));
		CommonTool.update( _keywordComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_keyword_names( false) : get_spot_keyword_names( false));
		CommonTool.update( _numberObjectComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));

		if ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) {
			CommonTool.update( _roleVariableComboBox, get_agent_role_variable_names( false));
			CommonTool.update( _roleComboBox, get_agent_role_names( true));
		}

		CommonTool.update( _roleVariableComboBox, get_agent_role_variable_names( false));
		CommonTool.update( _roleComboBox, get_agent_role_names( true));

		_radioButtons1[ 6].setEnabled( true);
		if ( 6 == SwingTool.get_enabled_radioButton( _radioButtons1)) {
			_roleVariableComboBox.setEnabled( true);
			_labels[ 3][ 0].setEnabled( true);
			_roleComboBox.setEnabled( true);
		}

		CommonTool.update( _timeVariableComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_time_variable_names( false) : get_spot_time_variable_names( false));
		CommonTool.update( _spotVariableComboBox2, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_spot_variable_names( false) : get_spot_spot_variable_names( false));
		CommonTool.update( _classVariableComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_class_variable_names( false) : get_spot_class_variable_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		super.update(objectSelector, spotVariableCheckBox, spotVariableComboBox);

		CommonTool.update( _probabilityComboBox, get_spot_probability_names( false));
		CommonTool.update( _collectionComboBox, get_spot_collection_names( false));
		CommonTool.update( _listComboBox, get_spot_list_names( false));
		CommonTool.update( _mapComboBox, get_spot_map_names( false));
		CommonTool.update( _keywordComboBox, get_spot_keyword_names( false));
		CommonTool.update( _numberObjectComboBox, get_spot_number_object_names( false));

		_radioButtons1[ 6].setEnabled( false);
		_roleVariableComboBox.setEnabled( false);
		_labels[ 3][ 0].setEnabled( false);
		_roleComboBox.setEnabled( false);

		CommonTool.update( _timeVariableComboBox, get_spot_time_variable_names( false));
		CommonTool.update( _spotVariableComboBox2, get_spot_spot_variable_names( false));
		CommonTool.update( _classVariableComboBox, get_spot_class_variable_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.object.entiy.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		super.update(spotObject, number, objectSelector, spotVariableCheckBox, spotVariableComboBox);

		CommonTool.update( _probabilityComboBox, get_spot_probability_names( false));
		CommonTool.update( _collectionComboBox, get_spot_collection_names( false));
		CommonTool.update( _listComboBox, get_spot_list_names( false));
		CommonTool.update( _mapComboBox, get_spot_map_names( false));
		CommonTool.update( _keywordComboBox, get_spot_keyword_names( false));
		CommonTool.update( _numberObjectComboBox, get_spot_number_object_names( false));
//		CommonTool.update( _probability_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "probability", number, false) : get_spot_probability_names( false));
//		CommonTool.update( _collection_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "collection", number, false) : get_spot_collection_names( false));
//		CommonTool.update( _list_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));
//		CommonTool.update( _map_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "map", number, false) : get_spot_map_names( false));
//		CommonTool.update( _keyword_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "keyword", number, false) : get_spot_keyword_names( false));
//		CommonTool.update( _number_object_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));

		_radioButtons1[ 6].setEnabled( false);
		_roleVariableComboBox.setEnabled( false);
		_labels[ 3][ 0].setEnabled( false);
		_roleComboBox.setEnabled( false);

		CommonTool.update( _timeVariableComboBox, get_spot_time_variable_names( false));
		CommonTool.update( _spotVariableComboBox2, get_spot_spot_variable_names( false));
		CommonTool.update( _classVariableComboBox, get_spot_class_variable_names( false));
//		CommonTool.update( _time_variable_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "time variable", number, false) : get_spot_time_variable_names( false));
//		CommonTool.update( _spot_variable_comboBox2, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "spot variable", number, false) : get_spot_spot_variable_names( false));
//		CommonTool.update( _class_variable_comboBox, !spot_variable_checkBox.isSelected() ? spotObject.get_object_names( "class variable", number, false) : get_spot_class_variable_names( false));
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
			false, false
		});
	}

	/**
	 * @param enables
	 */
	private void update_components(boolean[] enables) {
		_probabilityComboBox.setEnabled( enables[ 0]);
		_labels[ 0][ 0].setEnabled( enables[ 0]);
		_probabilityInitialValueTextField.setEnabled( enables[ 0]);

		_collectionComboBox.setEnabled( enables[ 1]);

		_listComboBox.setEnabled( enables[ 2]);

		_mapComboBox.setEnabled( enables[ 3]);

		_keywordComboBox.setEnabled( enables[ 4]);
		_labels[ 1][ 0].setEnabled( enables[ 4]);
		_keywordInitialValueTextField.setEnabled( enables[ 4]);

		_numberObjectComboBox.setEnabled( enables[ 5]);
		_labels[ 2][ 0].setEnabled( enables[ 5]);
		_numberObjectTypeTextField.setEnabled( enables[ 5]);
		_labels[ 2][ 1].setEnabled( enables[ 5]);
		_numberObjectInitialValueTextField.setEnabled( enables[ 5]);

		_roleVariableComboBox.setEnabled( enables[ 6]);
		_labels[ 3][ 0].setEnabled( enables[ 6]);
		_roleComboBox.setEnabled( enables[ 6]);

		_timeVariableComboBox.setEnabled( enables[ 7]);
		_labels[ 4][ 0].setEnabled( enables[ 7]);
		_timeTextField.setEnabled( enables[ 7]);
		_timeLabels[ 0].setEnabled( enables[ 7]);
		_timeComboBoxes[ 0].setEnabled( enables[ 7]);
		_timeLabels[ 1].setEnabled( enables[ 7]);
		_timeComboBoxes[ 1].setEnabled( enables[ 7]);

		_spotVariableComboBox2.setEnabled( enables[ 8]);
		_labels[ 5][ 0].setEnabled( enables[ 8]);
		_spotSelector2.setEnabled( enables[ 8]);

		_classVariableComboBox.setEnabled( enables[ 9]);
		_labels[ 6][ 0].setEnabled( enables[ 9]);
		_classnameTextField.setEnabled( enables[ 9]);
		_labels[ 6][ 1].setEnabled( enables[ 9]);
		_jarFilenameTextField.setEnabled( enables[ 9]);
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

		if ( !Environment.get_instance().is_functional_object_enable()) {
			_radioButtons1[ 9].setEnabled( false);
			_labels[ 6][ 0].setEnabled( false);
			_classnameTextField.setEnabled( false);
			_labels[ 6][ 1].setEnabled( false);
			_jarFilenameTextField.setEnabled( false);
		}

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

		int kind = CreateObjectCommand.get_kind( rule._value);
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

		if ( !set( values[ 0], values[ 1], _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox)) {
			set_handler();
			return false;
		}

		switch ( kind) {
			case 0:
				_probabilityComboBox.setSelectedItem( values[ 2]);
				_probabilityInitialValueTextField.setText( ( 1 < elements.length) ? elements[ 1] : "");
				break;
			case 1:
				_collectionComboBox.setSelectedItem( values[ 2]);
				break;
			case 2:
				_listComboBox.setSelectedItem( values[ 2]);
				break;
			case 3:
				_mapComboBox.setSelectedItem( values[ 2]);
				break;
			case 4:
				_keywordComboBox.setSelectedItem( values[ 2]);
				_keywordInitialValueTextField.setText( ( 1 < elements.length) ? elements[ 1] : "");
				break;
			case 5:
				String number_object_type = CreateObjectCommand.get_number_object_type(
					CommonRuleManipulator.get_semantic_prefix( values), values[ 2]);
				if ( null == number_object_type) {
					set_handler();
					return false;
				}

				String number_object_type_name = NumberObject.get_type_name( number_object_type);
				if ( number_object_type_name.equals( "")) {
					set_handler();
					return false;
				}

				_numberObjectTypeTextField.setText( number_object_type_name);

				_numberObjectComboBox.setSelectedItem( values[ 2]);

				_numberObjectInitialValueTextField.setText( ( 1 < elements.length) ? elements[ 1] : "");

				break;
			case 6:
				_roleVariableComboBox.setSelectedItem( values[ 2]);
				_roleComboBox.setSelectedItem( ( 1 < elements.length) ? elements[ 1] : "");
				break;
			case 7:
				_timeVariableComboBox.setSelectedItem( values[ 2]);
				set_time_variable_initial_values( ( 1 < elements.length) ? elements[ 1] : "");
				break;
			case 8:
				_spotVariableComboBox2.setSelectedItem( values[ 2]);
				_spotSelector2.set( ( 1 < elements.length) ? elements[ 1] : "");
				break;
			case 9:
				_classVariableComboBox.setSelectedItem( values[ 2]);
				break;
		}

		set_handler();

		return true;
	}

	/**
	 * @param initial_value
	 */
	private void set_time_variable_initial_values(String initial_value) {
		if ( initial_value.startsWith( "$"))
			_timeTextField.setText( initial_value);
		else {
			String[] words = TimeRule.get_time_elements( initial_value);
			if ( null == words)
				return;

			_timeTextField.setText( words[ 0]);
			_timeComboBoxes[ 0].setSelectedItem( words[ 1]);
			_timeComboBoxes[ 1].setSelectedItem( words[ 2]);
		}
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
				value = get_probability();
				break;
			case 1:
				value = get1( _collectionComboBox);
				break;
			case 2:
				value = get1( _listComboBox);
				break;
			case 3:
				value = get1( _mapComboBox);
				break;
			case 4:
				value = get_keyword();
				break;
			case 5:
				value = get_number_object();
				break;
			case 6:
				if ( _spotCheckBox.isSelected() || _spotVariableCheckBox.isSelected())
					return null;

				value = get2( _roleVariableComboBox, _roleComboBox, true);
				break;
			case 7:
				value = get_time_variable();
				break;
			case 8:
				value = get_spot_variable();
				break;
			case 9:
				if ( !Environment.get_instance().is_functional_object_enable())
					return null;

				value = get1( _classVariableComboBox);
				break;
		}

		if ( null == value)
			return null;

		String spot = get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);

		return Rule.create( _kind, _type, CreateObjectCommand._reservedWords[ kind] + spot + value);
	}

	/**
	 * @return
	 */
	private String get_probability() {
		if ( null == _probabilityInitialValueTextField.getText()
			|| _probabilityInitialValueTextField.getText().equals( "$")
			|| 0 < _probabilityInitialValueTextField.getText().indexOf( '$')
			|| _probabilityInitialValueTextField.getText().equals( "$Name")
			|| _probabilityInitialValueTextField.getText().equals( "$Role")
			|| _probabilityInitialValueTextField.getText().equals( "$Spot")
			|| 0 <= _probabilityInitialValueTextField.getText().indexOf( Constant._experimentName))
			return null;

		if ( _probabilityInitialValueTextField.getText().startsWith( "$")
			&& ( 0 < _probabilityInitialValueTextField.getText().indexOf( "$", 1)
			|| 0 < _probabilityInitialValueTextField.getText().indexOf( ")", 1)))
			return null;

		String probability = ( String)_probabilityComboBox.getSelectedItem();
		if ( null == probability || probability.equals( ""))
			return null;

		String initialValue = _probabilityInitialValueTextField.getText();
		if ( null == initialValue || initialValue.equals( ""))
			initialValue = "";
		else {
			if ( !CommonTool.is_probability_correct( initialValue))
				return null;

			initialValue = NumberObject.is_correct( initialValue, "real number");
		}

		return ( probability + "=" + initialValue);
	}

	/**
	 * @return
	 */
	private String get_keyword() {
		if ( null == _keywordInitialValueTextField.getText()
			|| _keywordInitialValueTextField.getText().equals( "$")
			|| 0 < _keywordInitialValueTextField.getText().indexOf( '$')
			||_keywordInitialValueTextField.getText().startsWith( " ")
			|| _keywordInitialValueTextField.getText().endsWith( " ")
			|| _keywordInitialValueTextField.getText().equals( "$Name")
			|| _keywordInitialValueTextField.getText().equals( "$Role")
			|| _keywordInitialValueTextField.getText().equals( "$Spot")
			|| 0 <= _keywordInitialValueTextField.getText().indexOf( Constant._experimentName))
			return null;

		if ( _keywordInitialValueTextField.getText().startsWith( "$")
			&& ( 0 <= _keywordInitialValueTextField.getText().indexOf( " ")
			|| 0 < _keywordInitialValueTextField.getText().indexOf( "$", 1)
			|| 0 < _keywordInitialValueTextField.getText().indexOf( ")", 1)))
			return null;

		return get4( _keywordComboBox, _keywordInitialValueTextField, true);
	}

	/**
	 * @return
	 */
	private String get_number_object() {
		if ( null == _numberObjectInitialValueTextField.getText()
			|| _numberObjectInitialValueTextField.getText().equals( "$")
			|| 0 < _numberObjectInitialValueTextField.getText().indexOf( '$')
			|| _numberObjectInitialValueTextField.getText().equals( "$Name")
			|| _numberObjectInitialValueTextField.getText().equals( "$Role")
			|| _numberObjectInitialValueTextField.getText().equals( "$Spot")
			|| 0 <= _numberObjectInitialValueTextField.getText().indexOf( Constant._experimentName))
			return null;

		if ( _numberObjectInitialValueTextField.getText().startsWith( "$")
			&& ( 0 < _numberObjectInitialValueTextField.getText().indexOf( "$", 1)
			|| 0 < _numberObjectInitialValueTextField.getText().indexOf( ")", 1)))
			return null;

		String numberObject = ( String)_numberObjectComboBox.getSelectedItem();
		if ( null == numberObject || numberObject.equals( ""))
			return null;

		String typeName = _numberObjectTypeTextField.getText();
		if ( null == typeName)
			return null;

		String type = NumberObject.get_type( typeName);
		if ( type.equals( ""))
			return null;

		String initialValue = _numberObjectInitialValueTextField.getText();
		if ( null == initialValue || initialValue.equals( ""))
			initialValue = "";
		else {
			initialValue = NumberObject.is_correct( initialValue, type);
			if ( null == initialValue)
				return null;
		}

		return ( numberObject + "=" + initialValue);
	}

	/**
	 * @return
	 */
	private String get_time_variable() {
		String timeVariable = ( String)_timeVariableComboBox.getSelectedItem();
		if ( null == timeVariable || timeVariable.equals( ""))
			return null;

		String initialValue = "";

		if ( _timeTextField.getText().startsWith( "$")) {
			if ( _timeTextField.getText().equals( "$")
				|| _timeTextField.getText().equals( "$Name")
				|| _timeTextField.getText().equals( "$Role")
				|| _timeTextField.getText().equals( "$Spot")
				|| 0 <= _timeTextField.getText().indexOf( Constant._experimentName)
				|| 0 <= _timeTextField.getText().indexOf( Constant._currentTimeName))
				return null;

			if ( 0 < _timeTextField.getText().indexOf( "$", 1)
				|| 0 < _timeTextField.getText().indexOf( ")", 1))
				return null;

			initialValue = _timeTextField.getText();

		} else {
			if ( !_timeTextField.getText().equals( "")) {
				int day;
				try {
					day = Integer.parseInt( _timeTextField.getText());
				} catch (NumberFormatException e) {
					//e.printStackTrace();
					return null;
				}
				initialValue = ( String.valueOf( day) + "/");
			}
			initialValue += ( ( String)_timeComboBoxes[ 0].getSelectedItem() + ":" + ( String)_timeComboBoxes[ 1].getSelectedItem());
		}

		return ( timeVariable + "=" + initialValue);
	}

	/**
	 * @return
	 */
	private String get_spot_variable() {
		String spotVariable = ( String)_spotVariableComboBox2.getSelectedItem();
		if ( null == spotVariable || spotVariable.equals( ""))
			return null;

		return ( spotVariable + "=" + _spotSelector2.get());
	}
}
