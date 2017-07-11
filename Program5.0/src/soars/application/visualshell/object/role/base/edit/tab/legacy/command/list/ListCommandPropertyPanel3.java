/**
 * 
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
 *
 */
public class ListCommandPropertyPanel3 extends CollectionAndListCommandPropertyPanelBase {

	/**
	 * 
	 */
	private RadioButton[][] _radioButtons2 = new RadioButton[][] {
		{ null, null, null, null},
		{ null, null, null, null}
	};

	/**
	 * 
	 */
	private ComboBox _equipFirstProbabilityComboBox = null;

	/**
	 * 
	 */
	private ComboBox _equipFirstKeywordComboBox = null;

	/**
	 * 
	 */
	private ComboBox _equipFirstNumberObjectComboBox = null;

	/**
	 * 
	 */
	private TextField _equipFirstObjectTextField = null;

	/**
	 * 
	 */
	private ComboBox _equipLastProbabilityComboBox = null;

	/**
	 * 
	 */
	private ComboBox _equipLastKeywordComboBox = null;

	/**
	 * 
	 */
	private ComboBox _equipLastNumberObjectComboBox = null;

	/**
	 * 
	 */
	private TextField _equipLastObjectTextField = null;

	/**
	 * 
	 */
	private JLabel[][] _dummy3 = new JLabel[][] {
		{ null, null, null},
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
	public ListCommandPropertyPanel3(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);

		_radioButtons1 = new RadioButton[] {
			null, null
		};

		_label = new JLabel[] {
			null
		};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#on_create()
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

		setup_equip_first( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_equip_last( buttonGroup1, northPanel);

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
	private void setup_equip_first(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.equip.first"),
			buttonGroup1, true, false);
		_radioButtons1[ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_radioButtons2[ 0][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_equipFirstProbabilityComboBox.setEnabled(
					_radioButtons2[ 0][ 0].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 0][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_equipFirstKeywordComboBox.setEnabled(
					_radioButtons2[ 0][ 1].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 0][ 2].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_equipFirstNumberObjectComboBox.setEnabled(
					_radioButtons2[ 0][ 2].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 0][ 3].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_equipFirstObjectTextField.setEnabled(
					_radioButtons2[ 0][ 3].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 0]);



		ButtonGroup buttonGroup2 = new ButtonGroup();



		_radioButtons2[ 0][ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.equip.first.probability"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_equipFirstProbabilityComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 0]);

		_equipFirstProbabilityComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _equipFirstProbabilityComboBox);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 0][ 0] = new JLabel();
		panel.add( _dummy3[ 0][ 0]);

		_radioButtons2[ 0][ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.equip.first.keyword"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_equipFirstKeywordComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 1]);

		_equipFirstKeywordComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _equipFirstKeywordComboBox);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 0][ 1] = new JLabel();
		panel.add( _dummy3[ 0][ 1]);

		_radioButtons2[ 0][ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.equip.first.number.object"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 2].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_equipFirstNumberObjectComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 2]);

		_equipFirstNumberObjectComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _equipFirstNumberObjectComboBox);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 0][ 2] = new JLabel();
		panel.add( _dummy3[ 0][ 2]);

		_radioButtons2[ 0][ 3] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.equip.first.object"),
			buttonGroup2, true, false);
		_radioButtons2[ 0][ 3].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_equipFirstObjectTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 0][ 3]);

		_equipFirstObjectTextField = create_textField( new TextExcluder( Constant._prohibitedCharacters1), _standardControlWidth, false);
		panel.add( _equipFirstObjectTextField);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_equip_last(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.equip.last"),
			buttonGroup1, true, false);
		_radioButtons1[ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_radioButtons2[ 1][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_equipLastProbabilityComboBox.setEnabled(
					_radioButtons2[ 1][ 0].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 1][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_equipLastKeywordComboBox.setEnabled(
					_radioButtons2[ 1][ 1].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 1][ 2].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_equipLastNumberObjectComboBox.setEnabled(
					_radioButtons2[ 1][ 2].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
				_radioButtons2[ 1][ 3].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_equipLastObjectTextField.setEnabled(
					_radioButtons2[ 1][ 3].isSelected() && ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 1]);



		ButtonGroup buttonGroup2 = new ButtonGroup();



		_radioButtons2[ 1][ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.equip.last.probability"),
			buttonGroup2, true, false);
		_radioButtons2[ 1][ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_equipLastProbabilityComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 1][ 0]);

		_equipLastProbabilityComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _equipLastProbabilityComboBox);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 1][ 0] = new JLabel();
		panel.add( _dummy3[ 1][ 0]);

		_radioButtons2[ 1][ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.equip.last.keyword"),
			buttonGroup2, true, false);
		_radioButtons2[ 1][ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_equipLastKeywordComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 1][ 1]);

		_equipLastKeywordComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _equipLastKeywordComboBox);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 1][ 1] = new JLabel();
		panel.add( _dummy3[ 1][ 1]);

		_radioButtons2[ 1][ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.equip.last.number.object"),
			buttonGroup2, true, false);
		_radioButtons2[ 1][ 2].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_equipLastNumberObjectComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 1][ 2]);

		_equipLastNumberObjectComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _equipLastNumberObjectComboBox);

		parent.add( panel);



		insert_vertical_strut( parent);



		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy3[ 1][ 2] = new JLabel();
		panel.add( _dummy3[ 1][ 2]);

		_radioButtons2[ 1][ 3] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.list.equip.last.object"),
			buttonGroup2, true, false);
		_radioButtons2[ 1][ 3].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_equipLastObjectTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons2[ 1][ 3]);

		_equipLastObjectTextField = create_textField( new TextExcluder( Constant._prohibitedCharacters1), _standardControlWidth, false);
		panel.add( _equipLastObjectTextField);

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

		CommonTool.update( _equipFirstProbabilityComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_probability_names( false) : get_spot_probability_names( false));
		CommonTool.update( _equipFirstKeywordComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_keyword_names( false) : get_spot_keyword_names( false));
		CommonTool.update( _equipFirstNumberObjectComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));

		CommonTool.update( _equipLastProbabilityComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_probability_names( false) : get_spot_probability_names( false));
		CommonTool.update( _equipLastKeywordComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_keyword_names( false) : get_spot_keyword_names( false));
		CommonTool.update( _equipLastNumberObjectComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));
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

		CommonTool.update( _equipFirstProbabilityComboBox, get_spot_probability_names( false));
		CommonTool.update( _equipFirstKeywordComboBox, get_spot_keyword_names( false));
		CommonTool.update( _equipFirstNumberObjectComboBox, get_spot_number_object_names( false));

		CommonTool.update( _equipLastProbabilityComboBox, get_spot_probability_names( false));
		CommonTool.update( _equipLastKeywordComboBox, get_spot_keyword_names( false));
		CommonTool.update( _equipLastNumberObjectComboBox, get_spot_number_object_names( false));
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

		CommonTool.update( _equipFirstProbabilityComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "probability", number, false) : get_spot_probability_names( false));
		CommonTool.update( _equipFirstKeywordComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "keyword", number, false) : get_spot_keyword_names( false));
		CommonTool.update( _equipFirstNumberObjectComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));

		CommonTool.update( _equipLastProbabilityComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "probability", number, false) : get_spot_probability_names( false));
		CommonTool.update( _equipLastKeywordComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "keyword", number, false) : get_spot_keyword_names( false));
		CommonTool.update( _equipLastNumberObjectComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));
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
		_radioButtons2[ 1][ 0].setSelected( true);
		update_components( new boolean[] {
			true, false
		});
	}

	/**
	 * @param enables
	 */
	private void update_components(boolean[] enables) {
		for ( int i = 0; i < _radioButtons2[ 0].length; ++i)
			_radioButtons2[ 0][ i].setEnabled( enables[ 0]);

		_equipFirstProbabilityComboBox.setEnabled( enables[ 0]);
		_equipFirstKeywordComboBox.setEnabled( false);
		_equipFirstNumberObjectComboBox.setEnabled( false);
		_equipFirstObjectTextField.setEnabled( false);

		for ( int i = 0; i < _radioButtons2[ 1].length; ++i)
			_radioButtons2[ 1][ i].setEnabled( enables[ 1]);

		_equipLastProbabilityComboBox.setEnabled( enables[ 1]);
		_equipLastKeywordComboBox.setEnabled( enables[ 1]);
		_equipLastNumberObjectComboBox.setEnabled( enables[ 1]);
		_equipLastObjectTextField.setEnabled( enables[ 1]);
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
		if ( 14 > kind || 21 < kind) {
			set_handler();
			return false;
		} else if ( 14 <= kind && 17 >= kind) {
			_radioButtons1[ 0].setSelected( true);
			_radioButtons2[ 0][ kind - 14].setSelected( true);
		} else {
			_radioButtons1[ 1].setSelected( true);
			_radioButtons2[ 1][ kind - 18].setSelected( true);
		}

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
			case 14:
				set1( _equipFirstProbabilityComboBox, _comboBox, values[ 2], elements[ 1]);
				break;
			case 15:
				set1( _equipFirstKeywordComboBox, _comboBox, values[ 2], elements[ 1]);
				break;
			case 16:
				set1( _equipFirstNumberObjectComboBox, _comboBox, values[ 2], elements[ 1]);
				break;
			case 17:
				set3( _comboBox, _equipFirstObjectTextField, elements[ 1], values[ 2]);
				break;
			case 18:
				set1( _equipLastProbabilityComboBox, _comboBox, values[ 2], elements[ 1]);
				break;
			case 19:
				set1( _equipLastKeywordComboBox, _comboBox, values[ 2], elements[ 1]);
				break;
			case 20:
				set1( _equipLastNumberObjectComboBox, _comboBox, values[ 2], elements[ 1]);
				break;
			case 21:
				set3( _comboBox, _equipLastObjectTextField, elements[ 1], values[ 2]);
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
		if ( 0 == kind)
			kind = ( 14 + SwingTool.get_enabled_radioButton( _radioButtons2[ 0]));
		else if ( 1 == kind)
			kind = ( 18 + SwingTool.get_enabled_radioButton( _radioButtons2[ 1]));
		else
			return null;

		String spot = get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);

		String value = null;
		switch ( kind) {
			case 14:
				value = get2( _equipFirstProbabilityComboBox, _comboBox);
				break;
			case 15:
				value = get2( _equipFirstKeywordComboBox, _comboBox);
				break;
			case 16:
				value = get2( _equipFirstNumberObjectComboBox, _comboBox);
				break;
			case 17:
				if ( null == _equipFirstObjectTextField.getText()
					|| _equipFirstObjectTextField.getText().equals( "")
					|| _equipFirstObjectTextField.getText().equals( "$")
					|| 0 < _equipFirstObjectTextField.getText().indexOf( '$')
					|| _equipFirstObjectTextField.getText().equals( "$Name")
					|| _equipFirstObjectTextField.getText().equals( "$Role")
					|| _equipFirstObjectTextField.getText().equals( "$Spot")
					|| 0 <= _equipFirstObjectTextField.getText().indexOf( Constant._experimentName))
					return null;

				if ( _equipFirstObjectTextField.getText().startsWith( "$")
					&& ( 0 < _equipFirstObjectTextField.getText().indexOf( "$", 1)
					|| 0 < _equipFirstObjectTextField.getText().indexOf( ")", 1)))
					return null;

				// TODO 要動作確認！
				if ( !is_object( spot, _equipFirstObjectTextField.getText(),
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

				value = get4( _equipFirstObjectTextField, _comboBox, false);
				break;
			case 18:
				value = get2( _equipLastProbabilityComboBox, _comboBox);
				break;
			case 19:
				value = get2( _equipLastKeywordComboBox, _comboBox);
				break;
			case 20:
				value = get2( _equipLastNumberObjectComboBox, _comboBox);
				break;
			case 21:
				if ( null == _equipLastObjectTextField.getText()
					|| _equipLastObjectTextField.getText().equals( "")
					|| _equipLastObjectTextField.getText().equals( "$")
					|| 0 < _equipLastObjectTextField.getText().indexOf( '$')
					|| _equipLastObjectTextField.getText().equals( "$Name")
					|| _equipLastObjectTextField.getText().equals( "$Role")
					|| _equipLastObjectTextField.getText().equals( "$Spot")
					|| 0 <= _equipLastObjectTextField.getText().indexOf( Constant._experimentName))
					return null;

				if ( _equipLastObjectTextField.getText().startsWith( "$")
					&& ( 0 < _equipLastObjectTextField.getText().indexOf( "$", 1)
					|| 0 < _equipLastObjectTextField.getText().indexOf( ")", 1)))
					return null;

				// TODO 要動作確認！
				if ( !is_object( spot, _equipLastObjectTextField.getText(),
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

				value = get4( _equipLastObjectTextField, _comboBox, false);
				break;
			default:
				return null;
		}

		if ( null == value)
			return null;

		return Rule.create( _kind, _type, ListCommand._reservedWords[ kind] + spot + value);
	}
}
