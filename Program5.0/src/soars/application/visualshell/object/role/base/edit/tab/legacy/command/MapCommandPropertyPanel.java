/*
 * Created on 2006/09/25
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
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.legacy.command.MapCommand;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 */
public class MapCommandPropertyPanel extends RulePropertyPanelBase {

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
	private JLabel _mapLabel = null;

	/**
	 * 
	 */
	private ComboBox _mapComboBox = null;

	/**
	 * 
	 */
	private RadioButton[] _radioButtons1 = new RadioButton[] {
		null, null
	};

	/**
	 * 
	 */
	private JLabel[][] _labels = new JLabel[][] {
		{ null, null},
		{ null, null}
	};

	/**
	 * 
	 */
	private RadioButton[][] _radioButtons2 = new RadioButton[][] {
		{ null, null},
		{ null, null}
	};

	/**
	 * 
	 */
	private ComboBox[] _keywordKeyComboBoxes = new ComboBox[] {
		null, null
	};

	/**
	 * 
	 */
	private TextField[] _keyTextFields = new TextField[] {
		null, null
	};

	/**
	 * 
	 */
	private RadioButton[][] _radioButtons3 = new RadioButton[][] {
		{ null, null, null},
		{ null, null, null}
	};

	/**
	 * 
	 */
	private ComboBox[] _probabilityValueComboBoxes = new ComboBox[] {
		null, null
	};

	/**
	 * 
	 */
	private ComboBox[] _keywordValueComboBoxes = new ComboBox[] {
		null, null
	};

	/**
	 * 
	 */
	private ComboBox[] _numberObjectValueComboBoxes = new ComboBox[] {
		null, null
	};

	/**
	 * 
	 */
	private JLabel[][] _dummies1 = new JLabel[][] {
		{ null, null, null},
		{ null, null, null}
	};

	/**
	 * 
	 */
	private JLabel[][] _dummies2 = new JLabel[][] {
		{ null, null}
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
	public MapCommandPropertyPanel(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
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


		create1();
		create2();
		create3();


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_spot_checkBox_and_spot_selector( northPanel);

		insert_vertical_strut( northPanel);

		setup_spot_variable_checkBox_and_spot_variable_comboBox( northPanel);

		insert_vertical_strut( northPanel);

		setup_map_comboBox( northPanel);

		insert_vertical_strut( northPanel);

		setup_get( northPanel);

		insert_vertical_strut( northPanel);

		setup_set( northPanel);

		insert_vertical_strut( northPanel);

		basicPanel.add( northPanel, "North");


		add( basicPanel);


		setup_apply_button();


		adjust();


		return true;
	}

	/**
	 * 
	 */
	private void create1() {
		ButtonGroup buttonGroup = new ButtonGroup();

		String[][] texts = new String[][] {
			{ ResourceManager.get_instance().get( "edit.rule.dialog.command.map.get"),
				ResourceManager.get_instance().get( "edit.rule.dialog.command.map.label.value"),
				ResourceManager.get_instance().get( "edit.rule.dialog.command.map.label.key")
			},
			{ ResourceManager.get_instance().get( "edit.rule.dialog.command.map.put"),
				ResourceManager.get_instance().get( "edit.rule.dialog.command.map.label.key"),
				ResourceManager.get_instance().get( "edit.rule.dialog.command.map.label.value")
			}
		};

		for ( int i = 0; i < 2; ++i)
			create1( i, texts, buttonGroup);
	}

	/**
	 * @param index
	 * @param texts
	 * @param buttonGroup
	 */
	private void create1(final int index, String[][] texts, ButtonGroup buttonGroup) {
		_radioButtons1[ index] = create_radioButton( texts[ index][ 0], buttonGroup, true, false);
		_radioButtons1[ index].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_labels[ index][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_labels[ index][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());

				_radioButtons2[ index][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ index][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_keywordKeyComboBoxes[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange()
					&& _radioButtons2[ index][ 0].isSelected());
				_keyTextFields[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange()
					&& _radioButtons2[ index][ 1].isSelected());

				_radioButtons3[ index][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons3[ index][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons3[ index][ 2].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_probabilityValueComboBoxes[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange()
					&& _radioButtons3[ index][ 0].isSelected());
				_keywordValueComboBoxes[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange()
					&& _radioButtons3[ index][ 1].isSelected());
				_numberObjectValueComboBoxes[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange()
					&& _radioButtons3[ index][ 2].isSelected());
			}
		});
		_labels[ index][ 0] = create_label( texts[ index][ 1], false);
		_labels[ index][ 1] = create_label( texts[ index][ 2], false);
	}

	/**
	 * 
	 */
	private void create2() {
		for ( int i = 0; i < 2; ++i)
			create2( i);
	}

	/**
	 * @param index
	 */
	private void create2(final int index) {
		ButtonGroup buttonGroup = new ButtonGroup();
		_radioButtons2[ index][ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.map.key.keyword"),
			buttonGroup, true, false);
		_radioButtons2[ index][ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_keywordKeyComboBoxes[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		_radioButtons2[ index][ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.map.key.immediate.data"),
			buttonGroup, true, false);
		_radioButtons2[ index][ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_keyTextFields[ index].setEnabled(
				ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		_keywordKeyComboBoxes[ index] = create_comboBox( null, _standardControlWidth, false);
		_keyTextFields[ index] = create_textField( new TextExcluder( Constant._prohibitedCharacters3), _standardControlWidth, false);
	}

	/**
	 * 
	 */
	private void create3() {
		for ( int i = 0; i < 2; ++i)
			create3( i);
	}

	/**
	 * @param index
	 */
	private void create3(final int index) {
		ButtonGroup buttonGroup = new ButtonGroup();
		_radioButtons3[ index][ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.map.value.probability"),
			buttonGroup, true, false);
		_radioButtons3[ index][ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_probabilityValueComboBoxes[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		_radioButtons3[ index][ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.map.value.keyword"),
			buttonGroup, true, false);
		_radioButtons3[ index][ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_keywordValueComboBoxes[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		_radioButtons3[ index][ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.map.value.number.object"),
			buttonGroup, true, false);
		_radioButtons3[ index][ 2].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_numberObjectValueComboBoxes[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		_probabilityValueComboBoxes[ index] = create_comboBox( null, _standardControlWidth, false);
		_keywordValueComboBoxes[ index] = create_comboBox( null, _standardControlWidth, false);
		_numberObjectValueComboBoxes[ index] = create_comboBox( null, _standardControlWidth, false);
	}

	/**
	 * @param parent
	 */
	private void setup_spot_checkBox_and_spot_selector(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_spotCheckBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.map.spot.check.box.name"),
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
	private void setup_map_comboBox(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_mapLabel = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.map.label.map"),
			true);
		panel.add( _mapLabel);

		_mapComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _mapComboBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_get(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		panel.add( _radioButtons1[ 0]);
		panel.add( _labels[ 0][ 0]);
		panel.add( _labels[ 0][ 1]);
		parent.add( panel);


		insert_vertical_strut( parent);


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummies1[ 0][ 0] = new JLabel();
		panel.add( _dummies1[ 0][ 0]);
		panel.add( _radioButtons3[ 0][ 0]);
		panel.add( _probabilityValueComboBoxes[ 0]);
		panel.add( _radioButtons2[ 0][ 0]);
		panel.add( _keywordKeyComboBoxes[ 0]);
		parent.add( panel);


		insert_vertical_strut( parent);


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummies1[ 0][ 1] = new JLabel();
		panel.add( _dummies1[ 0][ 1]);
		panel.add( _radioButtons3[ 0][ 1]);
		panel.add( _keywordValueComboBoxes[ 0]);
		panel.add( _radioButtons2[ 0][ 1]);
		panel.add( _keyTextFields[ 0]);
		parent.add( panel);


		insert_vertical_strut( parent);


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummies1[ 0][ 2] = new JLabel();
		panel.add( _dummies1[ 0][ 2]);
		panel.add( _radioButtons3[ 0][ 2]);
		panel.add( _numberObjectValueComboBoxes[ 0]);
		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_set(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		panel.add( _radioButtons1[ 1]);
		panel.add( _labels[ 1][ 0]);
		panel.add( _labels[ 1][ 1]);
		parent.add( panel);


		insert_vertical_strut( parent);


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummies1[ 1][ 0] = new JLabel();
		panel.add( _dummies1[ 1][ 0]);
		panel.add( _radioButtons2[ 1][ 0]);
		panel.add( _keywordKeyComboBoxes[ 1]);
		panel.add( _radioButtons3[ 1][ 0]);
		panel.add( _probabilityValueComboBoxes[ 1]);
		parent.add( panel);


		insert_vertical_strut( parent);


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummies1[ 1][ 1] = new JLabel();
		panel.add( _dummies1[ 1][ 1]);
		panel.add( _radioButtons2[ 1][ 1]);
		panel.add( _keyTextFields[ 1]);
		panel.add( _radioButtons3[ 1][ 1]);
		panel.add( _keywordValueComboBoxes[ 1]);
		parent.add( panel);


		insert_vertical_strut( parent);


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummies1[ 1][ 2] = new JLabel();
		panel.add( _dummies1[ 1][ 2]);
		_dummies2[ 0][ 0] = new JLabel();
		panel.add( _dummies2[ 0][ 0]);
		_dummies2[ 0][ 1] = new JLabel();
		panel.add( _dummies2[ 0][ 1]);
		panel.add( _radioButtons3[ 1][ 2]);
		panel.add( _numberObjectValueComboBoxes[ 1]);
		parent.add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width1 = 0;
		for ( int i = 0; i < _radioButtons1.length; ++i)
			width1 = Math.max( width1, _radioButtons1[ i].getPreferredSize().width);

		for ( int i = 0; i < _radioButtons1.length; ++i)
			_radioButtons1[ i].setPreferredSize( new Dimension( width1,
				_radioButtons1[ i].getPreferredSize().height));
		for ( int i = 0; i < _dummies1.length; ++i) {
			for ( int j = 0; j < _dummies1[ i].length; ++j)
				_dummies1[ i][ j].setPreferredSize( new Dimension( width1,
					_dummies1[ i][ j].getPreferredSize().height));
		}


		int width2 = 0;
		for ( int i = 0; i < _radioButtons2.length; ++i) {
			for ( int j = 0; j < _radioButtons2[ i].length; ++j) {
				width2 = Math.max( width2, _radioButtons2[ i][ j].getPreferredSize().width);
			}
		}
		for ( int i = 0; i < _radioButtons3.length; ++i) {
			for ( int j = 0; j < _radioButtons3[ i].length; ++j) {
				width2 = Math.max( width2, _radioButtons3[ i][ j].getPreferredSize().width);
			}
		}

		for ( int i = 0; i < _labels.length; ++i)
			_labels[ i][ 0].setPreferredSize( new Dimension(
				width2 + _keyTextFields[ 1].getPreferredSize().width + 5,
				//width2 + _standard_control_width + 5,
				_labels[ i][ 0].getPreferredSize().height));
		for ( int i = 0; i < _radioButtons2.length; ++i) {
			for ( int j = 0; j < _radioButtons2[ i].length; ++j)
				_radioButtons2[ i][ j].setPreferredSize( new Dimension( width2,
					_radioButtons2[ i][ j].getPreferredSize().height));
		}
		for ( int i = 0; i < _radioButtons3.length; ++i) {
			for ( int j = 0; j < _radioButtons3[ i].length; ++j)
				_radioButtons3[ i][ j].setPreferredSize( new Dimension( width2,
					_radioButtons3[ i][ j].getPreferredSize().height));
		}
		for ( int i = 0; i < _dummies2.length; ++i)
			_dummies2[ i][ 0].setPreferredSize( new Dimension( width2,
				_dummies2[ i][ 0].getPreferredSize().height));


		_spotCheckBox.setPreferredSize( new Dimension( width1 + width2 + 5,
			_spotCheckBox.getPreferredSize().height));
		_spotVariableCheckBox.setPreferredSize( new Dimension( width1 + width2 + 5,
			_spotVariableCheckBox.getPreferredSize().height));
		_mapLabel.setPreferredSize( new Dimension( width1 + width2 + 5,
			_mapLabel.getPreferredSize().height));


		for ( int i = 0; i < _dummies2.length; ++i)
			_dummies2[ i][ 1].setPreferredSize(
				new Dimension( _keyTextFields[ 1].getPreferredSize().width,
				//new Dimension( _standard_control_width,
					_dummies2[ i][ 1].getPreferredSize().height));
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

		CommonTool.update( _mapComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_map_names( false) : get_spot_map_names( false));

		for ( int i = 0; i < _radioButtons1.length; ++i) {
			CommonTool.update( _keywordKeyComboBoxes[ i], ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_keyword_names( false) : get_spot_keyword_names( false));
			CommonTool.update( _probabilityValueComboBoxes[ i], ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_probability_names( false) : get_spot_probability_names( false));
			CommonTool.update( _keywordValueComboBoxes[ i], ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_keyword_names( false) : get_spot_keyword_names( false));
			CommonTool.update( _numberObjectValueComboBoxes[ i], ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		super.update(objectSelector, spotVariableCheckBox, spotVariableComboBox);

		CommonTool.update( _mapComboBox, get_spot_map_names( false));

		for ( int i = 0; i < _radioButtons1.length; ++i) {
			CommonTool.update( _keywordKeyComboBoxes[ i], get_spot_keyword_names( false));
			CommonTool.update( _probabilityValueComboBoxes[ i], get_spot_probability_names( false));
			CommonTool.update( _keywordValueComboBoxes[ i], get_spot_keyword_names( false));
			CommonTool.update( _numberObjectValueComboBoxes[ i], get_spot_number_object_names( false));
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.object.entiy.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		super.update(spotObject, number, objectSelector, spotVariableCheckBox, spotVariableComboBox);

		CommonTool.update( _mapComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "map", number, false) : get_spot_map_names( false));

		for ( int i = 0; i < _radioButtons1.length; ++i) {
			CommonTool.update( _keywordKeyComboBoxes[ i], !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "keyword", number, false) : get_spot_keyword_names( false));
			CommonTool.update( _probabilityValueComboBoxes[ i], !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "probability", number, false) : get_spot_probability_names( false));
			CommonTool.update( _keywordValueComboBoxes[ i], !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "keyword", number, false) : get_spot_keyword_names( false));
			CommonTool.update( _numberObjectValueComboBoxes[ i], !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));
		}
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

		for ( int i = _radioButtons1.length - 1; i >= 0; --i) {
			_radioButtons2[ i][ 0].setSelected( true);
			_radioButtons3[ i][ 0].setSelected( true);
			_radioButtons1[ i].setSelected( true);
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


		String[] spots = CommonRuleManipulator.get_spot( rule._value);
		if ( null == spots) {
			set_handler();
			return false;
		}

		int kind = MapCommand.get_kind( rule._value);
		if ( 0 > kind) {
			set_handler();
			return false;
		}

		String[] elements = CommonRuleManipulator.get_elements( rule._value, 3);
		if ( null == elements) {
			set_handler();
			return false;
		}

		if ( !set( spots[ 0], spots[ 1], _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox)) {
			set_handler();
			return false;
		}

		_radioButtons1[ kind].setSelected( true);

		if ( 0 == kind) {
			if ( !set( kind, CommonRuleManipulator.get_full_prefix( spots), elements[ 1], elements[ 2], elements[ 0])) {
				set_handler();
				return false;
			}
		} else if ( 1 == kind) {
			if ( !set( kind, CommonRuleManipulator.get_full_prefix( spots), elements[ 0], elements[ 1], elements[ 2])) {
				set_handler();
				return false;
			}
		} else {
			set_handler();
			return false;
		}

		set_handler();

		return true;
	}

	/**
	 * @param kind
	 * @param spot
	 * @param map
	 * @param key
	 * @param value
	 */
	private boolean set(int kind, String spot, String map, String key, String value) {
		if ( !CommonRuleManipulator.is_object( "map", spot + map, LayerManager.get_instance()))
			return false;

		_mapComboBox.setSelectedItem( map);


		if ( key.startsWith( "\"") && key.endsWith( "\"")) {
			_radioButtons2[ kind][ 1].setSelected( true);
			_keyTextFields[ kind].setText( key.substring( 1, key.length() - 1));
		} else {
			if ( !CommonRuleManipulator.is_object( "keyword", spot + key, LayerManager.get_instance()))
				return false;

			_radioButtons2[ kind][ 0].setSelected( true);
			_keywordKeyComboBoxes[ kind].setSelectedItem( key);
		}


		if ( CommonRuleManipulator.is_object( "probability", spot + value, LayerManager.get_instance())) {
			_radioButtons3[ kind][ 0].setSelected( true);
			_probabilityValueComboBoxes[ kind].setSelectedItem( value);
		} else if ( CommonRuleManipulator.is_object( "keyword", spot + value, LayerManager.get_instance())) {
			_radioButtons3[ kind][ 1].setSelected( true);
			_keywordValueComboBoxes[ kind].setSelectedItem( value);
		} else if ( CommonRuleManipulator.is_object( "number object", spot + value, LayerManager.get_instance())) {
			_radioButtons3[ kind][ 2].setSelected( true);
			_numberObjectValueComboBoxes[ kind].setSelectedItem( value);
		} else
			return false;


		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get()
	 */
	@Override
	public Rule get() {
		String map = get1( _mapComboBox);
		if ( null == map)
			return null;

		int kind1 = SwingTool.get_enabled_radioButton( _radioButtons1);
		int kind2 = SwingTool.get_enabled_radioButton( _radioButtons2[ kind1]);
		int kind3 = SwingTool.get_enabled_radioButton( _radioButtons3[ kind1]);

		String key = null;
		switch ( kind2) {
			case 0:
				key = get1( _keywordKeyComboBoxes[ kind1]);
				break;
			case 1:
				if ( null == _keyTextFields[ kind1].getText()
					|| _keyTextFields[ kind1].getText().equals( "$")
					|| 0 < _keyTextFields[ kind1].getText().indexOf( '$')
					|| _keyTextFields[ kind1].getText().startsWith( " ")
					|| _keyTextFields[ kind1].getText().endsWith( " ")
					|| _keyTextFields[ kind1].getText().equals( "$Name")
					|| _keyTextFields[ kind1].getText().equals( "$Role")
					|| _keyTextFields[ kind1].getText().equals( "$Spot")
					|| 0 <= _keyTextFields[ kind1].getText().indexOf( Constant._experimentName))
					return null;

				if ( _keyTextFields[ kind1].getText().startsWith( "$")
					&& ( 0 <= _keyTextFields[ kind1].getText().indexOf( " ")
					|| 0 < _keyTextFields[ kind1].getText().indexOf( "$", 1)
					|| 0 < _keyTextFields[ kind1].getText().indexOf( ")", 1)))
					return null;

				key = get1( _keyTextFields[ kind1], true);
				key = "\"" + key + "\"";
				break;
		}

		if ( null == key)
			return null;

		String val = null;
		switch ( kind3) {
			case 0:
				val = get1( _probabilityValueComboBoxes[ kind1]);
				break;
			case 1:
				val = get1( _keywordValueComboBoxes[ kind1]);
				break;
			case 2:
				val = get1( _numberObjectValueComboBoxes[ kind1]);
				break;
		}

		if ( null == val)
			return null;

		String value = null;
		switch ( kind1) {
			case 0:
				value = val + "=" + map + "=" + key;
				break;
			case 1:
				value = map + "=" + key + "=" + val;
				break;
		}

		if ( null == value)
			return null;

		String spot = get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);

		return Rule.create( _kind, _type, spot + MapCommand._reservedWords[ kind1] + value);
	}
}
