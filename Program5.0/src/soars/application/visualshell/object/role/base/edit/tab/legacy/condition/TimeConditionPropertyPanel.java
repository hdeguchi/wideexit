/*
 * 2005/07/27
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.condition;

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
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.legacy.common.time.TimePropertyPanel;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.legacy.condition.TimeCondition;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;

/**
 * @author kurata
 */
public class TimeConditionPropertyPanel extends TimePropertyPanel {

	/**
	 * 
	 */
	private JLabel _typeLabel = null;

	/**
	 * 
	 */
	private ComboBox _typeComboBox = null;

	/**
	 * 
	 */
	private CheckBox _denialCheckBox = null;

	/**
	 * 
	 */
	private RadioButton[][] _radioButtons = new RadioButton[][] {
		{ null, null},
		{ null, null}
	};

	/**
	 * 
	 */
	private TextField[] _timeTextFields = new TextField[] {
		null, null
	};

	/**
	 * 
	 */
	private ComboBox[][] _timeComboBoxes = new ComboBox[][] {
		{ null, null},
		{ null, null}
	};

	/**
	 * 
	 */
	private JLabel[][] _timeLabels = new JLabel[][] {
		{ null, null},
		{ null, null}
	};

	/**
	 * 
	 */
	private ComboBox[] _timeVariableComboBoxes = new ComboBox[] {
		null, null
	};

	/**
	 * 
	 */
	private JLabel[] _labels = new JLabel[] {
		null, null
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
	public TimeConditionPropertyPanel(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
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

		setup_type_comboBox( northPanel);

		insert_vertical_strut( northPanel);

		ButtonGroup[] buttonGroups = new ButtonGroup[] { new ButtonGroup(), new ButtonGroup()};

		setup_time_components( buttonGroups, northPanel);

		insert_vertical_strut( northPanel);

		setup_time_variable_components( buttonGroups, northPanel);

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


		_spotCheckBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.spot.check.box.name"),
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
	private void setup_type_comboBox(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));


		_typeLabel = create_label( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.condition.type"), true);
		panel.add( _typeLabel);


		_typeComboBox = create_comboBox(
			new String[] {
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.at.the.specified.time"),
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.after.the.specified.time"),
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.before.the.specified.time"),
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.between.start.time.and.stop.time")
			}, false);
		_typeComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				update_components();
			}
		});
		panel.add( _typeComboBox);


		_denialCheckBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.check.box.denial"),
			true, false);
		panel.add( _denialCheckBox);


		parent.add( panel);
	}

	/**
	 * @param buttonGroups
	 * @param parent
	 */
	private void setup_time_components(ButtonGroup[] buttonGroups, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		for ( int i = 0; i < _radioButtons.length; ++i) {
			setup_time_radioButtons( i, buttonGroups, panel);
			setup_time_components( i, panel);
			if ( 0 == i) {
				_labels[ 0] = create_label( " - ", false);
				panel.add( _labels[ 0]);
			}
		}

		parent.add( panel);
	}

	/**
	 * @param index
	 * @param buttonGroups
	 * @param panel
	 */
	private void setup_time_radioButtons(final int index, ButtonGroup[] buttonGroups, JPanel panel) {
		_radioButtons[ index][ 0] = create_radioButton(
			( ( 0 == index ) ? ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.time.radio.button") : null),
			buttonGroups[ index], true, false);
		_radioButtons[ index][ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_timeTextFields[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_timeLabels[ index][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_timeComboBoxes[ index][ 0].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_timeLabels[ index][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_timeComboBoxes[ index][ 1].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons[ index][ 0]);
	}

	/**
	 * @param index
	 * @param panel
	 */
	private void setup_time_components(int index, JPanel panel) {
		_timeTextFields[ index] = create_textField( new TextExcluder( Constant._prohibitedCharacters14), "", 60, true);
		panel.add( _timeTextFields[ index]);

		_timeLabels[ index][ 0] = create_label( " / ", false);
		panel.add( _timeLabels[ index][ 0]);

		_timeComboBoxes[ index][ 0] = create_comboBox( CommonTool.get_hours(), 60, true);
		panel.add( _timeComboBoxes[ index][ 0]);

		_timeLabels[ index][ 1] = create_label( " : ", false);
		panel.add( _timeLabels[ index][ 1]);

		_timeComboBoxes[ index][ 1] = create_comboBox( CommonTool.get_minutes00(), 60, true);
		panel.add( _timeComboBoxes[ index][ 1]);
	}

	/**
	 * @param buttonGroups
	 * @param parent
	 */
	private void setup_time_variable_components(ButtonGroup[] buttonGroups, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		for ( int i = 0; i < _radioButtons.length; ++i) {
			setup_time_variable_radioButtons( i, buttonGroups, panel);
			setup_time_variable_comboBox( i, panel);
			if ( 0 == i) {
				_labels[ 1] = create_label( " - ", false);
				panel.add( _labels[ 1]);
			}
		}

		parent.add( panel);
	}

	/**
	 * @param index
	 * @param buttonGroups
	 * @param panel
	 */
	private void setup_time_variable_radioButtons(final int index, ButtonGroup[] buttonGroups, JPanel panel) {
		_radioButtons[ index][ 1] = create_radioButton(
			( ( 0 == index ) ? ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.time.variable.radio.button") : null),
			buttonGroups[ index], true, false);
		_radioButtons[ index][ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_timeVariableComboBoxes[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons[ index][ 1]);
	}

	/**
	 * @param index
	 * @param panel
	 */
	private void setup_time_variable_comboBox(int index, JPanel panel) {
		_timeVariableComboBoxes[ index] = create_comboBox( null, false);
		panel.add( _timeVariableComboBoxes[ index]);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = _spotCheckBox.getPreferredSize().width;
		width = Math.max( width, _spotVariableCheckBox.getPreferredSize().width);
		width = Math.max( width, _typeLabel.getPreferredSize().width);
		for ( int i = 0; i < _radioButtons[ 0].length; ++i)
			width = Math.max( width, _radioButtons[ 0][ i].getPreferredSize().width);

		_spotCheckBox.setPreferredSize( new Dimension( width, _spotCheckBox.getPreferredSize().height));
		_spotVariableCheckBox.setPreferredSize( new Dimension( width, _spotVariableCheckBox.getPreferredSize().height));
		_typeLabel.setPreferredSize( new Dimension( width, _typeLabel.getPreferredSize().height));
		for ( int i = 0; i < _radioButtons[ 0].length; ++i)
			_radioButtons[ 0][ i].setPreferredSize( new Dimension( width, _radioButtons[ 0][ i].getPreferredSize().height));


		width = _timeTextFields[ 0].getPreferredSize().width
			+ _timeLabels[ 0][ 0].getPreferredSize().width
			+ _timeComboBoxes[ 0][ 0].getPreferredSize().width
			+ _timeLabels[ 0][ 1].getPreferredSize().width
			+ _timeComboBoxes[ 0][ 1].getPreferredSize().width
			+ 20;

		_spotSelector.setWidth( width);
		_spotVariableComboBox.setPreferredSize( new Dimension( width, _spotVariableComboBox.getPreferredSize().height));
		_typeComboBox.setPreferredSize( new Dimension( width, _typeComboBox.getPreferredSize().height));
		for ( int i = 0; i < _timeVariableComboBoxes.length; ++i)
			_timeVariableComboBoxes[ i].setPreferredSize( new Dimension( width, _timeVariableComboBoxes[ i].getPreferredSize().height));
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

		for ( int i = 0; i < _timeVariableComboBoxes.length; ++i)
			CommonTool.update( _timeVariableComboBoxes[ i], ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_time_variable_names( false) : get_spot_time_variable_names( false));

		update_components();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		super.update(objectSelector, spotVariableCheckBox, spotVariableComboBox);

		for ( int i = 0; i < _timeVariableComboBoxes.length; ++i)
			CommonTool.update( _timeVariableComboBoxes[ i], get_spot_time_variable_names( false));

		update_components();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.object.entiy.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		super.update(spotObject, number, objectSelector, spotVariableCheckBox, spotVariableComboBox);

		for ( int i = 0; i < _timeVariableComboBoxes.length; ++i)
			CommonTool.update( _timeVariableComboBoxes[ i], !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "time variable", number, false) : get_spot_time_variable_names( false));

		update_components();
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

		for ( int i = 0; i < _radioButtons.length; ++i)
			_radioButtons[ i][ 0].setSelected( true);

		_typeComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.at.the.specified.time"));
	}

	/**
	 * 
	 */
	private void update_components() {
		String type = ( String)_typeComboBox.getSelectedItem();

		_denialCheckBox.setEnabled(
			type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.at.the.specified.time")));

		if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.at.the.specified.time")))
			update_components( new boolean[] { true, false, false});
		else if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.after.the.specified.time")))
			update_components( new boolean[] { true, false, true});
		else if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.before.the.specified.time")))
			update_components( new boolean[] { false, true, true});
		else if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.between.start.time.and.stop.time")))
			update_components( new boolean[] { true, true, true});
	}

	/**
	 * @param enables
	 */
	protected void update_components(boolean[] enables) {
		for ( int i = 0; i < _radioButtons.length; ++i) {
			_radioButtons[ i][ 0].setEnabled( enables[ i]);
			_timeTextFields[ i].setEnabled( enables[ i] && _radioButtons[ i][ 0].isSelected());
			_timeLabels[ i][ 0].setEnabled( enables[ i] && _radioButtons[ i][ 0].isSelected());
			_timeComboBoxes[ i][ 0].setEnabled( enables[ i] && _radioButtons[ i][ 0].isSelected());
			_timeLabels[ i][ 1].setEnabled( enables[ i] && _radioButtons[ i][ 0].isSelected());
			_timeComboBoxes[ i][ 1].setEnabled( enables[ i] && _radioButtons[ i][ 0].isSelected());
			_radioButtons[ i][ 1].setEnabled( enables[ i] && 0 < _timeVariableComboBoxes[ 0].getItemCount());
			_timeVariableComboBoxes[ i].setEnabled( enables[ i] && _radioButtons[ i][ 1].isSelected() && 0 < _timeVariableComboBoxes[ 0].getItemCount());
		}

		if ( 0 >= _timeVariableComboBoxes[ 0].getItemCount()) {
			for ( int i = 0; i < _radioButtons.length; ++i)
				_radioButtons[ i][ 0].setSelected( true);
		}

		_labels[ 0].setEnabled( enables[ 2]);
		_labels[ 1].setEnabled( enables[ 2] && 0 < _timeVariableComboBoxes[ 0].getItemCount());
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

		int kind = TimeCondition.get_kind( rule);
		if ( 0 > kind || _typeComboBox.getItemCount() <= kind) {
			set_handler();
			return false;
		}

		_typeComboBox.setSelectedIndex( kind);

		switch ( kind) {
			case 0:
				if ( !set( kind, rule._value, _timeTextFields[ 0], _timeComboBoxes[ 0], _timeVariableComboBoxes[ 0], _radioButtons[ 0])) {
					set_handler();
					return false;
				}
				_denialCheckBox.setSelected( rule._value.startsWith( "!"));
				break;
			case 1:
				if ( !set( kind, rule._value, _timeTextFields[ 0], _timeComboBoxes[ 0], _timeVariableComboBoxes[ 0], _radioButtons[ 0])) {
					set_handler();
					return false;
				}
				break;
			case 2:
				if ( !set( kind, rule._value, _timeTextFields[ 1], _timeComboBoxes[ 1], _timeVariableComboBoxes[ 1], _radioButtons[ 1])) {
					set_handler();
					return false;
				}
				break;
			case 3:
				if ( !set( kind, rule._value)) {
					set_handler();
					return false;
				}
				break;
		}

		set_handler();

		return true;
	}

	/**
	 * @param kind
	 * @param value
	 * @return
	 */
	private boolean set(int kind, String value) {
		String[] elements = value.split( " && ");
		if ( null == elements || 2 != elements.length
			|| null == elements[ 0] || elements[ 0].equals( "")
			|| null == elements[ 1] || elements[ 1].equals( ""))
			return false;

		if ( !set( kind, elements[ 0], _timeTextFields[ 0], _timeComboBoxes[ 0], _timeVariableComboBoxes[ 0], _radioButtons[ 0]))
			return false;

		if ( !set( kind, elements[ 1], _timeTextFields[ 1], _timeComboBoxes[ 1], _timeVariableComboBoxes[ 1], _radioButtons[ 1]))
			return false;

		return true;
	}

	/**
	 * @param kind
	 * @param value
	 * @param time_textField
	 * @param time_comboBoxes
	 * @param time_variable_comboBox
	 * @param radioButtons
	 * @return
	 */
	private boolean set(int kind, String value, TextField time_textField, ComboBox[] time_comboBoxes, ComboBox time_variable_comboBox, RadioButton[] radioButtons) {
		String[] spots = CommonRuleManipulator.get_spot( value);
		if ( null == spots)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 1 != elements.length)
			return false;

		if ( !set( spots[ 0], spots[ 1], _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox))
			return false;

		set( ( ( 0 == kind) ? elements[ 0].substring( "@".length()) : elements[ 0]), time_textField, time_comboBoxes, time_variable_comboBox, radioButtons);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get()
	 */
	@Override
	public Rule get() {
		String spot = get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);

		String type = ( String)_typeComboBox.getSelectedItem();

		if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.at.the.specified.time"))) {
			String value = get( _timeTextFields[ 0], _timeComboBoxes[ 0], _timeVariableComboBoxes[ 0], _radioButtons[ 0]);
			if ( null == value)
				return null;

			return Rule.create( _kind, _type, ( _denialCheckBox.isSelected() ? "!" : "") + spot + "isTime @" + value);
		} else if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.after.the.specified.time"))) {
			String value = get( _timeTextFields[ 0], _timeComboBoxes[ 0], _timeVariableComboBoxes[ 0], _radioButtons[ 0]);
			if ( null == value)
				return null;

			return Rule.create( _kind, _type, spot + "isTime " + value);
		} else if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.before.the.specified.time"))) {
			String value = get( _timeTextFields[ 1], _timeComboBoxes[ 1], _timeVariableComboBoxes[ 1], _radioButtons[ 1]);
			if ( null == value)
				return null;

			return Rule.create( _kind, _type, "!" + spot + "isTime " + value);
		} else if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.time.between.start.time.and.stop.time"))) {
			String value1 = get( _timeTextFields[ 0], _timeComboBoxes[ 0], _timeVariableComboBoxes[ 0], _radioButtons[ 0]);
			if ( null == value1)
				return null;

			String value2 = get( _timeTextFields[ 1], _timeComboBoxes[ 1], _timeVariableComboBoxes[ 1], _radioButtons[ 1]);
			if ( null == value1)
				return null;

			return Rule.create( _kind, _type, spot + "isTime " + value1 + " && !" + spot + "isTime " + value2);
		} else
			return null;
	}
}
