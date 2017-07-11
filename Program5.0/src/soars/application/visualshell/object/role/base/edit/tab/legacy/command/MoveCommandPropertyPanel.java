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
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulatorNew;
import soars.application.visualshell.object.role.base.object.legacy.command.MoveCommand;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 *
 */
public class MoveCommandPropertyPanel extends RulePropertyPanelBase {

	/**
	 * 
	 */
	private ButtonGroup _buttonGroup1 = new ButtonGroup();

	/**
	 * 
	 */
	private ButtonGroup _buttonGroup2 = new ButtonGroup();

	/**
	 * 
	 */
	private ButtonGroup _buttonGroup3 = new ButtonGroup();

	/**
	 * 
	 */
	private ButtonGroup _buttonGroup4 = new ButtonGroup();

	/**
	 * 
	 */
	private ButtonGroup _buttonGroup5 = new ButtonGroup();

	/**
	 * 
	 */
	private ButtonGroup _buttonGroup6 = new ButtonGroup();

	/**
	 * 
	 */
	private List<RadioButton> _radioButtons1 = new ArrayList<RadioButton>();

	/**
	 * 
	 */
	private List<RadioButton> _radioButtons2 = new ArrayList<RadioButton>();

	/**
	 * 
	 */
	private List<RadioButton> _radioButtons3 = new ArrayList<RadioButton>();

	/**
	 * 
	 */
	private List<RadioButton> _radioButtons4 = new ArrayList<RadioButton>();

	/**
	 * 
	 */
	private List<RadioButton> _radioButtons5 = new ArrayList<RadioButton>();

	/**
	 * 
	 */
	private List<RadioButton> _radioButtons6 = new ArrayList<RadioButton>();

	/**
	 * 
	 */
	private ObjectSelector _agentSelector = null;

	/**
	 * 
	 */
	private ObjectSelector _spotSelector = null;

//	/**
//	 * 
//	 */
//	private CheckBox _agentVariableCheckBox = null;
//
//	/**
//	 * 
//	 */
//	private ComboBox _agentVariableComboBox = null;

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
	private ComboBox _collectionComboBox = null;

	/**
	 * 
	 */
	private ComboBox _listComboBox = null;

	/**
	 * 
	 */
	private ComboBox _numberVariableComboBox = null;

	/**
	 * 
	 */
	private TextField _numberTextField = null;

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
	private TextField _immediateTextField = null;

	/**
	 * 
	 */
	private List<JLabel> _dummy = new ArrayList<JLabel>();

	/**
	 * 
	 */
	private boolean _spotVariable = false;

	/**
	 * 
	 */
	private int _selected = 0;

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
	public MoveCommandPropertyPanel(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#on_create()
	 */
	@Override
	protected boolean on_create() {
		if ( !super.on_create())
			return false;


		setup();


		setLayout( new BorderLayout());


		JPanel basicPanel = new JPanel();
		basicPanel.setLayout( new BorderLayout());


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		arrange( northPanel);

		basicPanel.add( northPanel, "North");


		adjust();


		add( basicPanel);


		setup_apply_button();


		return true;
	}

	/**
	 * 
	 */
	private void setup() {
		setup_buttonGroup1();
		setup_buttonGroup2();
		setup_buttonGroup3();
		setup_buttonGroup4();
		setup_buttonGroup5();
		setup_buttonGroup6();
		setup_components();
	}

	/**
	 * 
	 */
	private void setup_buttonGroup1() {
		RadioButton radioButton = create_radioButton( ResourceManager.get_instance().get( "edit.rule.dialog.command.move.spot"), _color, _buttonGroup1, true, false);
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if ( ItemEvent.SELECTED == e.getStateChange()) {
					_radioButtons2.get( 0).setEnabled( false);
					_radioButtons2.get( 1).setEnabled( true);
					_agentSelector.setEnabled( false);
					if ( _radioButtons2.get( 0).isSelected())
						_radioButtons2.get( 1).setSelected( true);
					_spotSelector.setEnabled( _radioButtons2.get( 1).isSelected());
					_spotVariableCheckBox.setSynchronize( true);
					// TODO 記憶していたスポット変数の選択状態を設定
					_spotVariableCheckBox.setSelected( _spotVariable);
					_spotVariableCheckBox.setEnabled( false);
					_spotVariableComboBox.setEnabled( false);
					for ( RadioButton radioButton:_radioButtons3)
						radioButton.setEnabled( false);
					for ( JLabel label:_labels)
						label.setEnabled( false);
					_collectionComboBox.setEnabled( false);
					_listComboBox.setEnabled( false);
					_mapComboBox.setEnabled( false);
					for ( RadioButton radioButton:_radioButtons4)
						radioButton.setEnabled( false);
					_numberVariableComboBox.setEnabled( false);
					_numberTextField.setEnabled( false);
					for ( RadioButton radioButton:_radioButtons5)
						radioButton.setEnabled( false);
					for ( RadioButton radioButton:_radioButtons6)
						radioButton.setEnabled( false);
					_keywordComboBox.setEnabled( false);
					_immediateTextField.setEnabled( false);
				}
			}
		});
		_radioButtons1.add( radioButton);

		radioButton = create_radioButton( ResourceManager.get_instance().get( "edit.rule.dialog.command.move.spot.variable"), _color, _buttonGroup1, true, false);
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if ( ItemEvent.SELECTED == e.getStateChange()) {
					for ( RadioButton radioButton:_radioButtons2)
						radioButton.setEnabled( true);
					_agentSelector.setEnabled( _radioButtons2.get( 0).isSelected());
					_spotSelector.setEnabled( _radioButtons2.get( 1).isSelected());
					_spotVariableCheckBox.setSynchronize( false);
					// TODO スポット変数の選択状態を記憶
					_spotVariable = _spotVariableCheckBox.isSelected();
					_spotVariableCheckBox.setSelected( true);
					_spotVariableCheckBox.setEnabled( false);
					_spotVariableComboBox.setEnabled( true);
					for ( RadioButton radioButton:_radioButtons3)
						radioButton.setEnabled( false);
					for ( JLabel label:_labels)
						label.setEnabled( false);
					_collectionComboBox.setEnabled( false);
					_listComboBox.setEnabled( false);
					_mapComboBox.setEnabled( false);
					for ( RadioButton radioButton:_radioButtons4)
						radioButton.setEnabled( false);
					_numberVariableComboBox.setEnabled( false);
					_numberTextField.setEnabled( false);
					for ( RadioButton radioButton:_radioButtons5)
						radioButton.setEnabled( false);
					for ( RadioButton radioButton:_radioButtons6)
						radioButton.setEnabled( false);
					_keywordComboBox.setEnabled( false);
					_immediateTextField.setEnabled( false);
				}
			}
		});
		_radioButtons1.add( radioButton);

		radioButton = create_radioButton( ResourceManager.get_instance().get( "edit.rule.dialog.command.move.element.of.collection.variable"), _color, _buttonGroup1, true, false);
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if ( ItemEvent.SELECTED == e.getStateChange()) {
					for ( RadioButton radioButton:_radioButtons2)
						radioButton.setEnabled( true);
					_agentSelector.setEnabled( _radioButtons2.get( 0).isSelected());
					_spotSelector.setEnabled( _radioButtons2.get( 1).isSelected());
					_spotVariableCheckBox.setSynchronize( true);
					// TODO 記憶していたスポット変数の選択状態を設定
					_spotVariableCheckBox.setSelected( _spotVariable);
					_spotVariableCheckBox.setEnabled( true);
					_spotVariableComboBox.setEnabled( _spotVariableCheckBox.isSelected());
					// TODO 選択されているラジオボタンを記憶
					_selected = SwingTool.get_enabled_radioButton( _radioButtons3);
					_radioButtons3.get( 0).setEnabled( true);
					_radioButtons3.get( 0).setSelected( true);
					_radioButtons3.get( 1).setEnabled( false);
					_radioButtons3.get( 2).setEnabled( false);
					_radioButtons3.get( 3).setEnabled( false);
					_labels.get( 0).setEnabled( true);
					_labels.get( 1).setEnabled( false);
					_labels.get( 2).setEnabled( false);
					_collectionComboBox.setEnabled( true);
					_listComboBox.setEnabled( false);
					_mapComboBox.setEnabled( false);
					for ( RadioButton radioButton:_radioButtons4)
						radioButton.setEnabled( false);
					_numberVariableComboBox.setEnabled( false);
					_numberTextField.setEnabled( false);
					for ( RadioButton radioButton:_radioButtons5)
						radioButton.setEnabled( false);
					for ( RadioButton radioButton:_radioButtons6)
						radioButton.setEnabled( false);
					_keywordComboBox.setEnabled( false);
					_immediateTextField.setEnabled( false);
				}
			}
		});
		_radioButtons1.add( radioButton);

		radioButton = create_radioButton( ResourceManager.get_instance().get( "edit.rule.dialog.command.move.element.of.list.variable"), _color, _buttonGroup1, true, false);
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if ( ItemEvent.SELECTED == e.getStateChange()) {
					for ( RadioButton radioButton:_radioButtons2)
						radioButton.setEnabled( true);
					_agentSelector.setEnabled( _radioButtons2.get( 0).isSelected());
					_spotSelector.setEnabled( _radioButtons2.get( 1).isSelected());
					_spotVariableCheckBox.setSynchronize( true);
					// TODO 記憶していたスポット変数の選択状態を設定
					_spotVariableCheckBox.setSelected( _spotVariable);
					_spotVariableCheckBox.setEnabled( true);
					_spotVariableComboBox.setEnabled( _spotVariableCheckBox.isSelected());
					for ( RadioButton radioButton:_radioButtons3)
						radioButton.setEnabled( true);
					// TODO 記憶していたラジオボタンを選択
					_radioButtons3.get( _selected).setSelected( true);
					_labels.get( 0).setEnabled( false);
					_labels.get( 1).setEnabled( true);
					_labels.get( 2).setEnabled( false);
					_collectionComboBox.setEnabled( false);
					_listComboBox.setEnabled( true);
					_mapComboBox.setEnabled( false);
					for ( RadioButton radioButton:_radioButtons4)
						radioButton.setEnabled( _radioButtons3.get( 3).isSelected());
					_numberVariableComboBox.setEnabled( _radioButtons3.get( 3).isSelected() && _radioButtons4.get( 0).isSelected());
					_numberTextField.setEnabled( _radioButtons3.get( 3).isSelected() && _radioButtons4.get( 1).isSelected());
					for ( RadioButton radioButton:_radioButtons5)
						radioButton.setEnabled( false);
					for ( RadioButton radioButton:_radioButtons6)
						radioButton.setEnabled( false);
					_keywordComboBox.setEnabled( false);
					_immediateTextField.setEnabled( false);
				}
			}
		});
		_radioButtons1.add( radioButton);

		radioButton = create_radioButton( ResourceManager.get_instance().get( "edit.rule.dialog.command.move.key.of.map.variable"), _color, _buttonGroup1, true, false);
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if ( ItemEvent.SELECTED == e.getStateChange()) {
					for ( RadioButton radioButton:_radioButtons2)
						radioButton.setEnabled( true);
					_agentSelector.setEnabled( _radioButtons2.get( 0).isSelected());
					_spotSelector.setEnabled( _radioButtons2.get( 1).isSelected());
					_spotVariableCheckBox.setSynchronize( false);
					// TODO 記憶していたスポット変数の選択状態を設定
					_spotVariableCheckBox.setSelected( _spotVariable);
					_spotVariableCheckBox.setEnabled( true);
					_spotVariableComboBox.setEnabled( _spotVariableCheckBox.isSelected());
					for ( RadioButton radioButton:_radioButtons3)
						radioButton.setEnabled( false);
					_labels.get( 0).setEnabled( false);
					_labels.get( 1).setEnabled( false);
					_labels.get( 2).setEnabled( true);
					_collectionComboBox.setEnabled( false);
					_listComboBox.setEnabled( false);
					_mapComboBox.setEnabled( true);
					for ( RadioButton radioButton:_radioButtons4)
						radioButton.setEnabled( false);
					_numberVariableComboBox.setEnabled( false);
					_numberTextField.setEnabled( false);
					for ( RadioButton radioButton:_radioButtons5)
						radioButton.setEnabled( true);
					for ( RadioButton radioButton:_radioButtons6)
						radioButton.setEnabled( _radioButtons5.get( 1).isSelected());
					_keywordComboBox.setEnabled( _radioButtons5.get( 1).isSelected() && _radioButtons6.get( 0).isSelected());
					_immediateTextField.setEnabled( _radioButtons5.get( 1).isSelected() && _radioButtons6.get( 1).isSelected());
				}
			}
		});
		_radioButtons1.add( radioButton);
	}

	/**
	 * 
	 */
	private void setup_buttonGroup2() {
		RadioButton radioButton = create_radioButton( ResourceManager.get_instance().get( "edit.rule.dialog.command.move.agent"), _color, _buttonGroup2, true, false);
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				_agentSelector.setEnabled( true);
				_spotSelector.setEnabled( false);
				update( _agentSelector.get_name(), _agentSelector.get_number(), _agentSelector);
			}
		});
		_radioButtons2.add( radioButton);

		radioButton = create_radioButton( ResourceManager.get_instance().get( "edit.rule.dialog.command.move.spot"), _color, _buttonGroup2, true, false);
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				_agentSelector.setEnabled( false);
				_spotSelector.setEnabled( true);
				update( _spotSelector.get_name(), _spotSelector.get_number(), _spotSelector);
			}
		});
		_radioButtons2.add( radioButton);
	}

	/**
	 * 
	 */
	private void setup_buttonGroup3() {
		RadioButton radioButton = create_radioButton( ResourceManager.get_instance().get( "edit.rule.dialog.command.move.random"), _color, _buttonGroup3, true, false);
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
			}
		});
		_radioButtons3.add( radioButton);

		radioButton = create_radioButton( ResourceManager.get_instance().get( "edit.rule.dialog.command.move.first"), _color, _buttonGroup3, true, false);
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
			}
		});
		_radioButtons3.add( radioButton);

		radioButton = create_radioButton( ResourceManager.get_instance().get( "edit.rule.dialog.command.move.last"), _color, _buttonGroup3, true, false);
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
			}
		});
		_radioButtons3.add( radioButton);

		radioButton = create_radioButton( ResourceManager.get_instance().get( "edit.rule.dialog.command.move.certain"), _color, _buttonGroup3, true, false);
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				for ( RadioButton radioButton:_radioButtons4)
					radioButton.setEnabled( ItemEvent.SELECTED == e.getStateChange());
				_numberVariableComboBox.setEnabled( _radioButtons3.get( 3).isSelected() && _radioButtons4.get( 0).isSelected());
				_numberTextField.setEnabled( _radioButtons3.get( 3).isSelected() && _radioButtons4.get( 1).isSelected());
			}
		});
		_radioButtons3.add( radioButton);
	}

	/**
	 * 
	 */
	private void setup_buttonGroup4() {
		RadioButton radioButton = create_radioButton( ResourceManager.get_instance().get( "edit.rule.dialog.command.move.number"), _color, _buttonGroup4, true, true);
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				_numberVariableComboBox.setEnabled( ItemEvent.SELECTED == e.getStateChange());
			}
		});
		_radioButtons4.add( radioButton);

		radioButton = create_radioButton( ResourceManager.get_instance().get( "edit.rule.dialog.command.move.value"), _color, _buttonGroup4, true, true);
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				_numberTextField.setEnabled( ItemEvent.SELECTED == e.getStateChange());
			}
		});
		_radioButtons4.add( radioButton);
	}

	/**
	 * 
	 */
	private void setup_buttonGroup5() {
		RadioButton radioButton = create_radioButton( ResourceManager.get_instance().get( "edit.rule.dialog.command.move.random"), _color, _buttonGroup5, true, false);
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
			}
		});
		_radioButtons5.add( radioButton);

		radioButton = create_radioButton( ResourceManager.get_instance().get( "edit.rule.dialog.command.move.certain"), _color, _buttonGroup5, true, false);
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				for ( RadioButton radioButton:_radioButtons6)
					radioButton.setEnabled( ItemEvent.SELECTED == e.getStateChange());
				_keywordComboBox.setEnabled( _radioButtons5.get( 1).isSelected() && _radioButtons6.get( 0).isSelected());
				_immediateTextField.setEnabled( _radioButtons5.get( 1).isSelected() && _radioButtons6.get( 1).isSelected());
			}
		});
		_radioButtons5.add( radioButton);
	}

	/**
	 * 
	 */
	private void setup_buttonGroup6() {
		RadioButton radioButton = create_radioButton( ResourceManager.get_instance().get( "edit.rule.dialog.command.move.keyword"), _color, _buttonGroup6, true, true);
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				_keywordComboBox.setEnabled( ItemEvent.SELECTED == e.getStateChange());
			}
		});
		_radioButtons6.add( radioButton);

		radioButton = create_radioButton( ResourceManager.get_instance().get( "edit.rule.dialog.command.move.immediate"), _color, _buttonGroup6, true, true);
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				_immediateTextField.setEnabled( ItemEvent.SELECTED == e.getStateChange());
			}
		});
		_radioButtons6.add( radioButton);
	}

	/**
	 * 
	 */
	private void setup_components() {
		_spotVariableCheckBox = create_checkBox( ResourceManager.get_instance().get( "edit.rule.dialog.spot.variable.check.box.name"), true, false);
		_spotVariableCheckBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				_spotVariableComboBox.setEnabled( ItemEvent.SELECTED == e.getStateChange());
				// TODO スポット変数の選択状態を記憶
				if ( !_radioButtons1.get( 1).isSelected())
					_spotVariable = ( ItemEvent.SELECTED == e.getStateChange());
				if ( ItemEvent.SELECTED == e.getStateChange()) {
					CommonTool.update( _collectionComboBox, get_spot_collection_names( false));
					CommonTool.update( _listComboBox, get_spot_list_names( false));
					CommonTool.update( _numberVariableComboBox, get_spot_integer_number_object_names( false));
					CommonTool.update( _mapComboBox, get_spot_map_names( false));
					CommonTool.update( _keywordComboBox, get_spot_keyword_names( false));
				} else {
					if ( _radioButtons2.get( 0).isSelected())
						update( _agentSelector.get_name(), _agentSelector.get_number(), _agentSelector);
					else if ( _radioButtons2.get( 1).isSelected())
						update( _spotSelector.get_name(), _spotSelector.get_number(), _spotSelector);
				}
			}
		});

		_spotVariableComboBox = create_comboBox( null, _standardControlWidth, false);


		JLabel label = create_label( ResourceManager.get_instance().get( "edit.rule.dialog.command.move.collection"), true);
		_labels.add( label);

		_collectionComboBox = create_comboBox( null, _standardControlWidth, false);


		label = create_label( ResourceManager.get_instance().get( "edit.rule.dialog.command.move.list"), true);
		_labels.add( label);

		_listComboBox = create_comboBox( null, _standardControlWidth, false);

		_numberVariableComboBox = create_comboBox( null, _standardControlWidth, false);

		_numberTextField = create_textField( new TextExcluder( Constant._prohibitedCharacters1), _standardControlWidth, true);


		label = create_label( ResourceManager.get_instance().get( "edit.rule.dialog.command.move.map"), true);
		_labels.add( label);

		_mapComboBox = create_comboBox( null, _standardControlWidth, false);

		_keywordComboBox = create_comboBox( null, _standardControlWidth, false);

		_immediateTextField = create_textField( new TextExcluder( Constant._prohibitedCharacters3), _standardControlWidth, false);
	}

	/**
	 * @param parent
	 */
	private void arrange(JPanel parent) {
		setup1( parent);
		insert_vertical_strut( parent);
		setup2( parent);
		insert_vertical_strut( parent);
		setup3( parent);
		insert_vertical_strut( parent);
		setup4( parent);
		insert_vertical_strut( parent);
		setup5( parent);
		insert_vertical_strut( parent);
		setup6( parent);
		insert_vertical_strut( parent);
		setup7( parent);
		insert_vertical_strut( parent);
		setup8( parent);
		insert_vertical_strut( parent);
		setup9( parent);
		insert_vertical_strut( parent);
		setup10( parent);
		insert_vertical_strut( parent);
		setup11( parent);
		insert_vertical_strut( parent);
	}

	/**
	 * @param parent
	 */
	private void setup1(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JLabel label = create_label( ResourceManager.get_instance().get( "edit.rule.dialog.command.move.move.to"), true);
		panel.add( label);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup2(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		panel.add( _radioButtons1.get( 0));

		panel.add( _radioButtons2.get( 0));

		_agentSelector = create_agent_selector( true, _color, true, panel);
//		_agentSelector.selectFirstItem();
		panel.add( _agentSelector);

		panel.add( _spotVariableCheckBox);

		panel.add( _spotVariableComboBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup3(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		panel.add( _radioButtons1.get( 1));

		panel.add( _radioButtons2.get( 1));

		_spotSelector = create_spot_selector( true, _color, true, panel);
//		_spotSelector.selectFirstItem();
		panel.add( _spotSelector);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup4(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		panel.add( _radioButtons1.get( 2));

		panel.add( _radioButtons3.get( 0));

		panel.add( _labels.get( 0));
		panel.add( _collectionComboBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup5(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		panel.add( _radioButtons1.get( 3));

		panel.add( _radioButtons3.get( 1));

		panel.add( _labels.get( 1));
		panel.add( _listComboBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup6(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		append_dummy( panel);

		panel.add( _radioButtons3.get( 2));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup7(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		append_dummy( panel);

		panel.add( _radioButtons3.get( 3));

		panel.add( _radioButtons4.get( 0));
		panel.add( _numberVariableComboBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup8(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		append_dummy( panel);

		append_dummy( panel);

		panel.add( _radioButtons4.get( 1));
		panel.add( _numberTextField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup9(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		panel.add( _radioButtons1.get( 4));

		panel.add( _radioButtons5.get( 0));
		//append_dummy( panel);

		panel.add( _labels.get( 2));
		panel.add( _mapComboBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup10(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		append_dummy( panel);

		panel.add( _radioButtons5.get( 1));
		//append_dummy( panel);

		panel.add( _radioButtons6.get( 0));
		panel.add( _keywordComboBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup11(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		append_dummy( panel);

		append_dummy( panel);

		panel.add( _radioButtons6.get( 1));
		panel.add( _immediateTextField);

		parent.add( panel);
	}

	/**
	 * @param panel
	 */
	private void append_dummy(JPanel panel) {
		JLabel label = create_label( "", false);
		panel.add( label);
		_dummy.add( label);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase#update(java.lang.String, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector)
	 */
	@Override
	protected void update(String name, String number, ObjectSelector objectSelector) {
		if ( objectSelector.equals( _agentSelector)) {
			if ( name.equals( ""))
				update( "agent");
			else {
				AgentObject agentObject = LayerManager.get_instance().get_agent( name);
				if ( null == agentObject)
					return;

				update( agentObject, number);
			}
		} else if ( objectSelector.equals( _spotSelector)) {
			if ( name.equals( ""))
				update( "spot");
			else {
				SpotObject spotObject = LayerManager.get_instance().get_spot( name);
				if ( null == spotObject)
					return;

				update( spotObject, number);
			}
		}
	}

	/**
	 * @param which
	 */
	private void update(String which) {
		if ( which.equals( "agent")) {
			CommonTool.update( _spotVariableComboBox, get_agent_spot_variable_names( false));
			CommonTool.update( _collectionComboBox, !_spotVariableCheckBox.isSelected() ? get_agent_collection_names( false) : get_spot_collection_names( false));
			CommonTool.update( _listComboBox, !_spotVariableCheckBox.isSelected() ? get_agent_list_names( false) : get_spot_list_names( false));
			CommonTool.update( _numberVariableComboBox, !_spotVariableCheckBox.isSelected() ? get_agent_integer_number_object_names( false) : get_spot_integer_number_object_names( false));
			CommonTool.update( _mapComboBox, !_spotVariableCheckBox.isSelected() ? get_agent_map_names( false) : get_spot_map_names( false));
			CommonTool.update( _keywordComboBox, !_spotVariableCheckBox.isSelected() ? get_agent_keyword_names( false) : get_spot_keyword_names( false));
		} else if ( which.equals( "spot")) {
			CommonTool.update( _spotVariableComboBox, get_spot_spot_variable_names( false));
			CommonTool.update( _collectionComboBox, get_spot_collection_names( false));
			CommonTool.update( _listComboBox, get_spot_list_names( false));
			CommonTool.update( _numberVariableComboBox, get_spot_integer_number_object_names( false));
			CommonTool.update( _mapComboBox, get_spot_map_names( false));
			CommonTool.update( _keywordComboBox, get_spot_keyword_names( false));
		}
	}

	/**
	 * @param agentObject
	 * @param number
	 */
	private void update(AgentObject agentObject, String number) {
		CommonTool.update( _spotVariableComboBox, agentObject.get_object_names( "spot variable", number, false));
		CommonTool.update( _collectionComboBox, !_spotVariableCheckBox.isSelected() ? agentObject.get_object_names( "collection", number, false) : get_spot_collection_names( false));
		CommonTool.update( _listComboBox, !_spotVariableCheckBox.isSelected() ? agentObject.get_object_names( "list", number, false) : get_spot_list_names( false));
		CommonTool.update( _numberVariableComboBox, !_spotVariableCheckBox.isSelected() ? agentObject.get_number_object_names( "integer", number, false) : get_spot_integer_number_object_names( false));
		CommonTool.update( _mapComboBox, !_spotVariableCheckBox.isSelected() ? agentObject.get_object_names( "map", number, false) : get_spot_map_names( false));
		CommonTool.update( _keywordComboBox, !_spotVariableCheckBox.isSelected() ? agentObject.get_object_names( "keyword", number, false) : get_spot_keyword_names( false));
	}

	/**
	 * @param spotObject
	 * @param number
	 */
	private void update(SpotObject spotObject, String number) {
		CommonTool.update( _spotVariableComboBox, spotObject.get_object_names( "spot variable", number, false));
		CommonTool.update( _collectionComboBox, !_spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "collection", number, false) : get_spot_collection_names( false));
		CommonTool.update( _listComboBox, !_spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "list", number, false) : get_spot_list_names( false));
		CommonTool.update( _numberVariableComboBox, !_spotVariableCheckBox.isSelected() ? spotObject.get_number_object_names( "integer", number, false) : get_spot_integer_number_object_names( false));
		CommonTool.update( _mapComboBox, !_spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "map", number, false) : get_spot_map_names( false));
		CommonTool.update( _keywordComboBox, !_spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "keyword", number, false) : get_spot_keyword_names( false));
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( RadioButton radioButton:_radioButtons1)
			width = Math.max( width, radioButton.getPreferredSize().width);
		for ( RadioButton radioButton:_radioButtons1)
			radioButton.setPreferredSize( new Dimension( width, radioButton.getPreferredSize().height));

		_dummy.get( 0).setPreferredSize( new Dimension( width, _dummy.get( 0).getPreferredSize().height));
		_dummy.get( 1).setPreferredSize( new Dimension( width, _dummy.get( 1).getPreferredSize().height));
		_dummy.get( 2).setPreferredSize( new Dimension( width, _dummy.get( 2).getPreferredSize().height));
		_dummy.get( 4).setPreferredSize( new Dimension( width, _dummy.get( 4).getPreferredSize().height));
		_dummy.get( 5).setPreferredSize( new Dimension( width, _dummy.get( 5).getPreferredSize().height));

		width = 0;
		for ( RadioButton radioButton:_radioButtons2)
			width = Math.max( width, radioButton.getPreferredSize().width);
		for ( RadioButton radioButton:_radioButtons2)
			radioButton.setPreferredSize( new Dimension( width, radioButton.getPreferredSize().height));
		width += ( _standardSpotNameWidth + _standardNumberSpinnerWidth + 5);
		for ( RadioButton radioButton:_radioButtons3)
			radioButton.setPreferredSize( new Dimension( width, radioButton.getPreferredSize().height));
		for ( RadioButton radioButton:_radioButtons5)
			radioButton.setPreferredSize( new Dimension( width, radioButton.getPreferredSize().height));

		_dummy.get( 3).setPreferredSize( new Dimension( width, _dummy.get( 3).getPreferredSize().height));
		_dummy.get( 6).setPreferredSize( new Dimension( width, _dummy.get( 6).getPreferredSize().height));

		width = 0;
		for ( RadioButton radioButton:_radioButtons4)
			width = Math.max( width, radioButton._label.getPreferredSize().width);
		for ( RadioButton radioButton:_radioButtons4)
			radioButton._label.setPreferredSize( new Dimension( width, radioButton._label.getPreferredSize().height));

		width = 0;
		for ( RadioButton radioButton:_radioButtons6)
			width = Math.max( width, radioButton._label.getPreferredSize().width);
		for ( RadioButton radioButton:_radioButtons6)
			radioButton._label.setPreferredSize( new Dimension( width, radioButton._label.getPreferredSize().height));

		width = _spotVariableCheckBox.getPreferredSize().width;
		for ( JLabel label:_labels)
			width = Math.max( width, label.getPreferredSize().width);
		for ( RadioButton radioButton:_radioButtons4)
			width = Math.max( width, radioButton.getPreferredSize().width);
		for ( RadioButton radioButton:_radioButtons6)
			width = Math.max( width, radioButton.getPreferredSize().width);
		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));
		for ( RadioButton radioButton:_radioButtons4)
			radioButton.setPreferredSize( new Dimension( width, radioButton.getPreferredSize().height));
		for ( RadioButton radioButton:_radioButtons6)
			radioButton.setPreferredSize( new Dimension( width, radioButton.getPreferredSize().height));
	}

	/**
	 * 
	 */
	protected void set_handler() {
		_agentSelector.set_handler( this);
		_spotSelector.set_handler( this);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#on_setup_completed()
	 */
	@Override
	public void on_setup_completed() {
		_radioButtons6.get( 0).setSelected( true);
		_radioButtons5.get( 0).setSelected( true);
		_radioButtons4.get( 0).setSelected( true);
		_radioButtons3.get( 0).setSelected( true);
		_radioButtons2.get( 0).setSelected( true);
		_radioButtons1.get( 0).setSelected( true);
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
		if ( null == rule || !_type.equals( rule._type) || rule._value.equals( "")) {
			set_handler();
			return false;
		}

		String[] values = MoveCommand.get_values( rule._value);
		if ( null == values) {
			set_handler();
			return false;
		}

		if ( null != values[ 0]) {
			_agentSelector.set( values[ 0]);
			_radioButtons2.get( 0).setSelected( true);
		} else if ( null != values[ 1]) {
			_spotSelector.set( values[ 1]);
			_radioButtons2.get( 1).setSelected( true);
		} else {
			set_handler();
			return false;
		}

		// TODO _spotVariableを設定する必要がある？
		if ( values[ 3].equals( ""))
			_spotVariableCheckBox.setSelected( false);
		else {
			_spotVariableCheckBox.setSelected( true);
			_spotVariableComboBox.setSelectedItem( values[ 3]);
		}

		if ( rule._value.startsWith( MoveCommand._reservedWords[ 0])) {
			if ( null != values[ 1] && values[ 3].equals( ""))
				_radioButtons1.get( 0).setSelected( true);
			else
				_radioButtons1.get( 1).setSelected( true);
		} else {
			String[] words = Tool.split( values[ 4], '=');
			if ( null == words || 0 == words.length) {
				set_handler();
				return false;
			}

			if ( rule._value.startsWith( MoveCommand._reservedWords[ 1])) {
				if ( CommonRuleManipulatorNew.is_object( "collection", values, words[ 0])) {
					_collectionComboBox.setSelectedItem( words[ 0]);
					_radioButtons3.get( 0).setSelected( true);
					_radioButtons1.get( 2).setSelected( true);
				} else if ( CommonRuleManipulatorNew.is_object( "list", values, words[ 0])) {
					_listComboBox.setSelectedItem( words[ 0]);
					_selected = 0;
					_radioButtons3.get( 0).setSelected( true);
					_radioButtons1.get( 3).setSelected( true);
				} else if ( CommonRuleManipulatorNew.is_object( "map", values, words[ 0])) {
					_mapComboBox.setSelectedItem( words[ 0]);
					_radioButtons5.get( 0).setSelected( true);
					_radioButtons1.get( 4).setSelected( true);
				} else {
					set_handler();
					return false;
				}
			} else if ( rule._value.startsWith( MoveCommand._reservedWords[ 2])) {
				if ( CommonRuleManipulatorNew.is_object( "list", values, words[ 0])) {
					_listComboBox.setSelectedItem( words[ 0]);
					_selected = 1;
					_radioButtons3.get( 1).setSelected( true);
					_radioButtons1.get( 3).setSelected( true);
				} else {
					set_handler();
					return false;
				}
			} else if ( rule._value.startsWith( MoveCommand._reservedWords[ 3])) {
				if ( CommonRuleManipulatorNew.is_object( "list", values, words[ 0])) {
					_listComboBox.setSelectedItem( words[ 0]);
					_selected = 2;
					_radioButtons3.get( 2).setSelected( true);
					_radioButtons1.get( 3).setSelected( true);
				} else {
					set_handler();
					return false;
				}
			} else {
				if ( 2 > words.length) {
					set_handler();
					return false;
				}

				if ( rule._value.startsWith( MoveCommand._reservedWords[ 4])) {
					if ( CommonRuleManipulatorNew.is_object( "list", values, words[ 0])) {
						_listComboBox.setSelectedItem( words[ 0]);
						if ( CommonRuleManipulatorNew.is_number_object( values, words[ 1], "integer")) {
							_numberVariableComboBox.setSelectedItem( words[ 1]);
							_radioButtons4.get( 0).setSelected( true);
						} else {
							if ( !words[ 1].startsWith( "\"") || !words[ 1].endsWith( "\"")) {
								set_handler();
								return false;
							}

							_numberTextField.setText( words[ 1].substring( 1, words[ 1].length() - 1));
							_radioButtons4.get( 1).setSelected( true);
						}
						_selected = 3;
						_radioButtons3.get( 3).setSelected( true);
						_radioButtons1.get( 3).setSelected( true);
					} else {
						set_handler();
						return false;
					}
				} else if ( rule._value.startsWith( MoveCommand._reservedWords[ 5])) {
					if ( CommonRuleManipulatorNew.is_object( "map", values, words[ 0])) {
						_mapComboBox.setSelectedItem( words[ 0]);
						if ( CommonRuleManipulatorNew.is_object( "keyword", values, words[ 1])) {
							_keywordComboBox.setSelectedItem( words[ 1]);
							_radioButtons6.get( 0).setSelected( true);
						} else {
							if ( !words[ 1].startsWith( "\"") || !words[ 1].endsWith( "\"")) {
								set_handler();
								return false;
							}

							_immediateTextField.setText( words[ 1].substring( 1, words[ 1].length() - 1));
							_radioButtons6.get( 1).setSelected( true);
						}
						_radioButtons5.get( 1).setSelected( true);
						_radioButtons1.get( 4).setSelected( true);
					} else {
						set_handler();
						return false;
					}
				} else {
					set_handler();
					return false;
				}
			}
		}

		set_handler();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get()
	 */
	@Override
	public Rule get() {
		String prefix = get_prifix();
		if ( null == prefix)
			return null;

		String value = get_value( prefix);
		if ( null == value)
			return null;

		//System.out.println( value);

		return Rule.create( _kind, _type, value);
	}

	/**
	 * @return
	 */
	private String get_prifix() {
		int selected1 = SwingTool.get_enabled_radioButton( _radioButtons1);
		int selected2 = SwingTool.get_enabled_radioButton( _radioButtons2);
		switch ( selected1) {
			case 0:
				String spot = _spotSelector.get();
				if ( null == spot || spot.equals( ""))
					return null;

				return ( "<" + spot + ">");
			case 1:
			case 2:
			case 3:
			case 4:
				switch ( selected2) {
					case 0:
						String agent = _agentSelector.get();
						if ( null == agent)
							return null;

						if ( !_spotVariableCheckBox.isSelected())
							return ( agent.equals( "") ? "" : ( "<" + agent + ">"));
						else {
							String spotVariable = ( String)_spotVariableComboBox.getSelectedItem();
							if ( null == spotVariable || spotVariable.equals( ""))
								return null;

							return ( agent.equals( "") ? ( "<" + spotVariable + ">") : ( "<" + agent + ":" + spotVariable + ">"));
						}
					case 1:
						spot = _spotSelector.get();
						if ( null == spot)
							return null;

						if ( !_spotVariableCheckBox.isSelected())
							return ( spot.equals( "") ? "<>" : ( "<" + spot + ">"));
						else {
							String spotVariable = ( String)_spotVariableComboBox.getSelectedItem();
							if ( null == spotVariable || spotVariable.equals( ""))
								return null;

							return ( spot.equals( "") ? ( "<:" + spotVariable + ">") : ( "<" + spot + ":" + spotVariable + ">"));
						}
				}
		}
		return null;
	}

	/**
	 * @param prefix
	 * @return
	 */
	private String get_value(String prefix) {
		int selected1 = SwingTool.get_enabled_radioButton( _radioButtons1);
		switch ( selected1) {
			case 0:
			case 1:
				return ( MoveCommand._reservedWords[ 0] + prefix);
			case 2:
				String collection = ( String)_collectionComboBox.getSelectedItem();
				if ( null == collection || collection.equals( ""))
					return null;

				return ( MoveCommand._reservedWords[ 1] + prefix + collection);
			case 3:
				String list = ( String)_listComboBox.getSelectedItem();
				if ( null == list || list.equals( ""))
					return null;

				int selected3 = SwingTool.get_enabled_radioButton( _radioButtons3);
				switch ( selected3) {
					case 0:
						return ( MoveCommand._reservedWords[ 1] + prefix + list);
					case 1:
						return ( MoveCommand._reservedWords[ 2] + prefix + list);
					case 2:
						return ( MoveCommand._reservedWords[ 3] + prefix + list);
					case 3:
						int selected4 = SwingTool.get_enabled_radioButton( _radioButtons4);
						switch ( selected4) {
							case 0:
								String numberVariable = ( String)_numberVariableComboBox.getSelectedItem();
								if ( null == numberVariable || numberVariable.equals( ""))
									return null;

								return ( MoveCommand._reservedWords[ 4] + prefix + list + "=" + numberVariable);
							case 1:
								String number = _numberTextField.getText();
								if ( null == number || number.equals( "")
									|| number.equals( "$") || 0 < number.indexOf( '$')
									|| number.equals( "$Name") || number.equals( "$Role") || number.equals( "$Spot")
									|| 0 <= number.indexOf( Constant._experimentName))
									return null;

								if ( number.startsWith( "$")
									&& ( 0 < number.indexOf( "$", 1) || 0 < number.indexOf( ")", 1)))
									return null;

								int n;
								try {
									n = Integer.parseInt( number);
									if ( 1 > n)
										return null;

									number = String.valueOf( n);
								} catch (NumberFormatException e) {
									//e.printStackTrace();
									if ( !number.matches( "\\$.+"))
										return null;
								}

								return ( MoveCommand._reservedWords[ 4] + prefix + list + "=\"" + number + "\"");
						}
				}
				break;
			case 4:
				String map = ( String)_mapComboBox.getSelectedItem();
				if ( null == map || map.equals( ""))
					return null;

				int selected5 = SwingTool.get_enabled_radioButton( _radioButtons5);
				switch ( selected5) {
					case 0:
						return ( MoveCommand._reservedWords[ 1] + prefix + map);
					case 1:
						int selected6 = SwingTool.get_enabled_radioButton( _radioButtons6);
						switch ( selected6) {
							case 0:
								String keyword = ( String)_keywordComboBox.getSelectedItem();
								if ( null == keyword || keyword.equals( ""))
									return null;

								return ( MoveCommand._reservedWords[ 5] + prefix + map + "=" + keyword);
							case 1:
								String immediate = _immediateTextField.getText();
								if ( null == immediate)
									return null;

								return ( MoveCommand._reservedWords[ 5] + prefix + map + "=\"" + immediate + "\"");
						}
				}
				break;
		}
		return null;
	}
}
