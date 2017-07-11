/*
 * 2005/07/28
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
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.legacy.common.time.TimePropertyPanel;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.legacy.command.TimeCommand;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 */
public class TimeCommandPropertyPanel extends TimePropertyPanel {

	/**
	 * 
	 */
	private JLabel _timeVariableLabel = null;

	/**
	 * 
	 */
	private ComboBox _timeVariableComboBox = null;

	/**
	 * 
	 */
	private RadioButton[][] _radioButtons = new RadioButton[][] {
		{ null, null, null},
		{ null, null}
	};

	/**
	 * 
	 */
	private JLabel _operatorLabel = null;

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
	private ComboBox _operatorComboBox = null;

	/**
	 * 
	 */
	private ComboBox[] _timeVariableComboBoxes = new ComboBox[] {
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
	public TimeCommandPropertyPanel(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
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

		setup_time_variable_comboBox( northPanel);

		insert_vertical_strut( northPanel);

		ButtonGroup buttonGroups[] = new ButtonGroup[] { new ButtonGroup(), new ButtonGroup()};

		setup_time_components( buttonGroups, northPanel);

		insert_vertical_strut( northPanel);

		setup_time_variable_comboBoxes( buttonGroups, northPanel);

		insert_vertical_strut( northPanel);

		setup_current_time_radioButton( buttonGroups[ 0], northPanel);

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
			ResourceManager.get_instance().get( "edit.rule.dialog.command.time.spot.check.box.name"),
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
	private void setup_time_variable_comboBox(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));


		_timeVariableLabel = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.time.time.variable"),
			true);
		panel.add( _timeVariableLabel);


		_timeVariableComboBox = create_comboBox( null, false);
		panel.add( _timeVariableComboBox);


		JLabel label = create_label( " = ", false);
		panel.add( label);


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
				_operatorLabel = create_label(
					ResourceManager.get_instance().get( "edit.rule.dialog.command.time.operator"),
					false);
				panel.add( _operatorLabel);
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
			( ( 0 == index ) ? ResourceManager.get_instance().get( "edit.rule.dialog.command.time.time.radio.button") : null),
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
	private void setup_time_variable_comboBoxes(ButtonGroup[] buttonGroups, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		for ( int i = 0; i < _radioButtons.length; ++i) {
			setup_time_variable_radioButtons( i, buttonGroups, panel);
			setup_time_variable_comboBoxes( i, panel);
			if ( 0 == i)
				setup_operator_comboBox( panel);
		}

		parent.add( panel);
	}

	/**
	 * @param panel
	 */
	private void setup_operator_comboBox(JPanel panel) {
		_operatorComboBox = create_comboBox( new String[] { "", "+", "-"}, 60, true);
		_operatorComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				update_right_side();
			}
		});
		panel.add( _operatorComboBox);
	}

	/**
	 * @param index
	 * @param buttonGroups
	 * @param panel
	 */
	private void setup_time_variable_radioButtons(final int index, ButtonGroup[] buttonGroups, JPanel panel) {
		_radioButtons[ index][ 1] = create_radioButton(
			( ( 0 == index ) ? ResourceManager.get_instance().get( "edit.rule.dialog.command.time.time.variable.radio.button") : null),
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
	private void setup_time_variable_comboBoxes(int index, JPanel panel) {
		_timeVariableComboBoxes[ index] = create_comboBox( null, false);
		panel.add( _timeVariableComboBoxes[ index]);
	}

	/**
	 * @param buttonGroup
	 * @param parent
	 */
	private void setup_current_time_radioButton(ButtonGroup buttonGroup, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons[ 0][ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.time.current.time.radio.button"),
			buttonGroup, true, false);
		panel.add( _radioButtons[ 0][ 2]);

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = _spotCheckBox.getPreferredSize().width;
		width = Math.max( width, _spotVariableCheckBox.getPreferredSize().width);
		width = Math.max( width, _timeVariableLabel.getPreferredSize().width);
		for ( int i = 0; i < _radioButtons[ 0].length; ++i)
			width = Math.max( width, _radioButtons[ 0][ i].getPreferredSize().width);

		_spotCheckBox.setPreferredSize( new Dimension( width, _spotCheckBox.getPreferredSize().height));
		_spotVariableCheckBox.setPreferredSize( new Dimension( width, _spotVariableCheckBox.getPreferredSize().height));
		_timeVariableLabel.setPreferredSize( new Dimension( width, _timeVariableLabel.getPreferredSize().height));
		for ( int i = 0; i < _radioButtons[ 0].length; ++i)
			_radioButtons[ 0][ i].setPreferredSize( new Dimension( width, _radioButtons[ 0][ i].getPreferredSize().height));


		width = _operatorLabel.getPreferredSize().width;
		width = Math.max( width, _operatorComboBox.getPreferredSize().width);
		_operatorLabel.setPreferredSize( new Dimension( width, _operatorLabel.getPreferredSize().height));
		_operatorComboBox.setPreferredSize( new Dimension( width, _operatorComboBox.getPreferredSize().height));


		width = _timeTextFields[ 0].getPreferredSize().width
			+ _timeLabels[ 0][ 0].getPreferredSize().width
			+ _timeComboBoxes[ 0][ 0].getPreferredSize().width
			+ _timeLabels[ 0][ 1].getPreferredSize().width
			+ _timeComboBoxes[ 0][ 1].getPreferredSize().width
			+ 20;

		_spotSelector.setWidth( width);
		_spotVariableComboBox.setPreferredSize( new Dimension( width, _spotVariableComboBox.getPreferredSize().height));
		_timeVariableComboBox.setPreferredSize( new Dimension( width, _timeVariableComboBox.getPreferredSize().height));
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

		CommonTool.update( _timeVariableComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_time_variable_names( false) : get_spot_time_variable_names( false));

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

		CommonTool.update( _timeVariableComboBox, get_spot_time_variable_names( false));

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

		CommonTool.update( _timeVariableComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "time variable", number, false) : get_spot_time_variable_names( false));

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

		for ( int i = 0; i < _radioButtons[ 1].length; ++i)
			_radioButtons[ 1][ i].setEnabled( false);

		update_components();
	}

	/**
	 * 
	 */
	private void update_components() {
		if ( 0 < _timeVariableComboBox.getItemCount()) {
			for ( int i = 0; i < _radioButtons[ 0].length; ++i)
				_radioButtons[ 0][ i].setEnabled( true);

			update_left_side();
			update_right_side();

			_operatorLabel.setEnabled( true);
			_operatorComboBox.setEnabled( true);
		} else {
			for ( int i = 0; i < _radioButtons.length; ++i) {
				for ( int j = 0; j < _radioButtons[ i].length; ++j)
				_radioButtons[ i][ j].setEnabled( false);
			}

			update_components( new boolean[] { false, false}, 0);
			update_components( new boolean[] { false, false}, 1);

			_operatorLabel.setEnabled( false);
			_operatorComboBox.setEnabled( false);
		}
	}

	/**
	 * 
	 */
	private void update_left_side() {
		boolean[] enables = new boolean[] {
			false, false, false
		};
		enables[ SwingTool.get_enabled_radioButton( _radioButtons[ 0])] = true;
		update_components( enables, 0);
	}

	/**
	 * 
	 */
	private void update_right_side() {
		String operator = ( String)_operatorComboBox.getSelectedItem();
		for ( int i = 0; i < _radioButtons[ 1].length; ++i)
			_radioButtons[ 1][ i].setEnabled( !operator.equals( ""));

		boolean[] enables = new boolean[] {
			false, false
		};
		if ( !operator.equals( ""))
			enables[ SwingTool.get_enabled_radioButton( _radioButtons[ 1])] = true;

		update_components( enables, 1);
	}

	/**
	 * @param enables
	 * @param index
	 */
	private void update_components(boolean[] enables, int index) {
		_timeTextFields[ index].setEnabled( enables[ 0]);

		for ( int i = 0; i < _timeComboBoxes[ index].length; ++i)
			_timeComboBoxes[ index][ i].setEnabled( enables[ 0]);

		for ( int i = 0; i < _timeLabels[ index].length; ++i)
			_timeLabels[ index][ i].setEnabled( enables[ 0]);

		_timeVariableComboBoxes[ index].setEnabled( enables[ 1]);
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

		String[] values = TimeCommand.get_values( rule._value);
		if ( null == values) {
			set_handler();
			return false;
		}

		if ( !set( values[ 0], values[ 1], _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox)) {
			set_handler();
			return false;
		}

		_timeVariableComboBox.setSelectedItem( values[ 2]);

		set( values[ 3], _timeTextFields[ 0], _timeComboBoxes[ 0], _timeVariableComboBoxes[ 0], _radioButtons[ 0]);

		_operatorComboBox.setSelectedItem( values[ 4]);

		if ( !values[ 4].equals( "") && null != values[ 5])
			set( values[ 5], _timeTextFields[ 1], _timeComboBoxes[ 1], _timeVariableComboBoxes[ 1], _radioButtons[ 1]);

		set_handler();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get()
	 */
	@Override
	public Rule get() {
		String timeVariable = ( String)_timeVariableComboBox.getSelectedItem();
		if ( null == timeVariable || timeVariable.equals( ""))
			return null;

		String spot = get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);

		String value = ( spot + "setTime " + timeVariable + "=");

		String time = get( _timeTextFields[ 0], _timeComboBoxes[ 0], _timeVariableComboBoxes[ 0], _radioButtons[ 0]);
		if ( null == time)
			return null;

		value += time;

		String operator = ( String)_operatorComboBox.getSelectedItem();
		if ( !operator.equals( "")) {
			time = get( _timeTextFields[ 1], _timeComboBoxes[ 1], _timeVariableComboBoxes[ 1], _radioButtons[ 1]);
			if ( null == time)
				return null;

			value += ( " " + operator + " " + time);
		}

		return Rule.create( _kind, _type, value);
	}
}
