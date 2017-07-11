/*
 * Created on 2006/03/06
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.common.number;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.object.number.NumberObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.legacy.common.expression.ExpressionElements;
import soars.application.visualshell.object.role.base.object.legacy.common.expression.NumberRule;
import soars.application.visualshell.object.role.base.object.legacy.common.expression.SubType;
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
public class NumberObjectPropertyPanelBase extends RulePropertyPanelBase {

	/**
	 * 
	 */
	protected Map<String, Vector<String[]>>[] _variableMaps = new HashMap[] { null, null};

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
	protected ComboBox _numberObjectComboBox = null;

	/**
	 * 
	 */
	protected RadioButton[][] _radioButtons = new RadioButton[][] {
		{ null, null, null, null},
		{ null, null, null, null}
	};

	/**
	 * 
	 */
	protected ComboBox _operatorComboBox1 = null;

	/**
	 * 
	 */
	protected TextField[] _valueTextFields = new TextField[] { null, null};

	/**
	 * 
	 */
	protected ComboBox[] _numberObjectComboBoxes = new ComboBox[] { null, null};

	/**
	 * 
	 */
	protected ComboBox[] _functionComboBoxes = new ComboBox[] { null, null};

	/**
	 * 
	 */
	protected TextField[] _expressionTextFields = new TextField[] { null, null};

	/**
	 * 
	 */
	protected JScrollPane[] _variableValueTableScrollPanes = new JScrollPane[] { null, null};

	/**
	 * 
	 */
	protected VariableValueTable[] _variableValueTables = new VariableValueTable[] { null, null};

	/**
	 * 
	 */
	protected TextField[] _othersTextFields = new TextField[] { null, null};

	/**
	 * 
	 */
	protected JLabel _numberObjectLabel = null;

	/**
	 * 
	 */
	protected JLabel _operatorLabel = null;

	/**
	 * 
	 */
	protected JLabel[] _expressionLabels = new JLabel[] { null, null};

	/**
	 * 
	 */
	protected JLabel _dummy[] = new JLabel[] {
		null, null, null, null
	};

	/**
	 * 
	 */
	private int _row = -1;

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
	public NumberObjectPropertyPanelBase(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
		for ( int i = 0; i < _variableMaps.length; ++i)
			_variableMaps[ i] = VisualShellExpressionManager.get_instance().get_variable_map();
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

		setup_number_object_and_operator( northPanel);

		insert_vertical_strut( northPanel);

		ButtonGroup buttonGroup1[] = new ButtonGroup[] { new ButtonGroup(), new ButtonGroup()};

		setup_value( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_number_object( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_function( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_expression( northPanel);

		insert_vertical_strut( northPanel);

		setup_variable_value_table( northPanel);

		insert_vertical_strut( northPanel);

		setup_others( buttonGroup1, northPanel);

		setup_handler();

		basicPanel.add( northPanel, "North");


		add( basicPanel);


		setup_apply_button();


		adjust();


		return true;
	}

	/**
	 * @param parent
	 */
	protected void setup_spot_checkBox_and_spot_selector(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_spotCheckBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.common.number.object.spot.check.box.name1"),
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


		setup_comparative_operator( panel);


		parent.add( panel);
	}

	/**
	 * @param panel
	 */
	protected void setup_comparative_operator(JPanel panel) {
	}

	/**
	 * @param parent
	 */
	protected void setup_number_object_and_operator(JPanel parent) {
	}

	/**
	 * @param component
	 * @param parent
	 */
	protected void setup_number_object_and_operator(JComponent component, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_numberObjectLabel = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.common.number.object.label.number.object"),
			false);
		_numberObjectLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _numberObjectLabel);

		_numberObjectComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _numberObjectComboBox);

		panel.add( component);

		parent.add( panel);
	}

	/**
	 * @param buttonGroups
	 * @param parent
	 */
	protected void setup_value(ButtonGroup[] buttonGroups, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		for ( int i = 0; i < _radioButtons.length; ++i) {
			_radioButtons[ i][ 0] = create_radioButton(
				ResourceManager.get_instance().get( "edit.rule.dialog.common.number.object.value.radio.button"),
				buttonGroups[ i], true, false);
			_valueTextFields[ i] = create_textField( new TextExcluder( Constant._prohibitedCharacters5), true);
			panel.add( _radioButtons[ i][ 0]);
			panel.add( _valueTextFields[ i]);

			if ( 0 == i) {
				_dummy[ 0] = new JLabel();
				panel.add( _dummy[ 0]);
			}
		}

		parent.add( panel);
	}

	/**
	 * @param buttonGroups
	 * @param parent
	 */
	private void setup_number_object(ButtonGroup[] buttonGroups, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		for ( int i = 0; i < _radioButtons.length; ++i) {
			_radioButtons[ i][ 1] = create_radioButton(
				ResourceManager.get_instance().get( "edit.rule.dialog.common.number.object.number.object.radio.button"),
				buttonGroups[ i], true, false);
			panel.add( _radioButtons[ i][ 1]);

			_numberObjectComboBoxes[ i] = create_comboBox( null, _standardControlWidth, false);
			panel.add( _numberObjectComboBoxes[ i]);

			if ( 0 == i) {
				_dummy[ 1] = new JLabel();
				panel.add( _dummy[ 1]);
			}
		}

		parent.add( panel);
	}

	/**
	 * @param buttonGroups
	 * @param parent
	 */
	private void setup_function(ButtonGroup[] buttonGroups, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		for ( int i = 0; i < _radioButtons.length; ++i) {
			_radioButtons[ i][ 2] = create_radioButton(
				ResourceManager.get_instance().get( "edit.rule.dialog.common.number.object.function.radio.button"),
				buttonGroups[ i], true, false);
			_functionComboBoxes[ i] = create_comboBox(
				VisualShellExpressionManager.get_instance().get_functions(),
				_standardControlWidth, false);
			panel.add( _radioButtons[ i][ 2]);
			panel.add( _functionComboBoxes[ i]);

			if ( 0 == i) {
				_operatorLabel = create_label(
					ResourceManager.get_instance().get( "edit.rule.dialog.common.number.object.label.operator"),
					false);
				panel.add( _operatorLabel);
			}
		}

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	protected void setup_expression(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		for ( int i = 0; i < _radioButtons.length; ++i) {
			_expressionLabels[ i] = create_label(
				ResourceManager.get_instance().get( "edit.rule.dialog.common.number.object.label.expression"),
				false);
			_expressionLabels[ i].setHorizontalAlignment( SwingConstants.RIGHT);
			panel.add( _expressionLabels[ i]);

			_expressionTextFields[ i] = create_textField( false);
			_expressionTextFields[ i].setEditable( false);
			panel.add( _expressionTextFields[ i]);

			if ( 0 == i) {
				_operatorComboBox1 = create_comboBox( new String[] { "", "+", "-", "*", "/", "%"}, 70, true);
				panel.add( _operatorComboBox1);
			}
		}

		parent.add( panel);
	}

	/**
	 * @param parent
	 * @return
	 */
	protected boolean setup_variable_value_table(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		for ( int i = 0; i < _radioButtons.length; ++i) {
			_variableValueTables[ i] = new VariableValueTable( _variableMaps[ i], _color, _owner, _parent);

			if ( !_variableValueTables[ i].setup())
				return false;

			_variableValueTableScrollPanes[ i] = new JScrollPane();
			_variableValueTableScrollPanes[ i].getViewport().setView( _variableValueTables[ i]);

			panel.add( _variableValueTableScrollPanes[ i]);

			if ( 0 == i) {
				_dummy[ 2] = new JLabel();
				panel.add( _dummy[ 2]);
			}
		}

		parent.add( panel);

		return true;
	}

	/**
	 * @param buttonGroups
	 * @param parent
	 */
	private void setup_others(ButtonGroup[] buttonGroups, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		for ( int i = 0; i < _radioButtons.length; ++i) {
			_radioButtons[ i][ 3] = create_radioButton(
				ResourceManager.get_instance().get( "edit.rule.dialog.common.number.object.others.radio.button"),
				buttonGroups[ i], true, false);
			_othersTextFields[ i] = create_textField( new TextExcluder( Constant._prohibitedCharacters15), false);
			panel.add( _radioButtons[ i][ 3]);
			panel.add( _othersTextFields[ i]);

			if ( 0 == i) {
				_dummy[ 3] = new JLabel();
				panel.add( _dummy[ 3]);
			}
		}

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void setup_handler() {
		_operatorComboBox1.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String operator = ( String)_operatorComboBox1.getSelectedItem();
				for ( int i = 0; i < _radioButtons[ 1].length; ++i)
					_radioButtons[ 1][ i].setEnabled( !operator.equals( ""));

				boolean[] enables = new boolean[] {
					false, false, false, false
				};
				if ( !operator.equals( ""))
					enables[ SwingTool.get_enabled_radioButton( _radioButtons[ 1])] = true;

				update_components( enables, 1);
			}
		});

		for ( int i = 0; i < _radioButtons.length; ++i) {
			addItemListener( _radioButtons[ i][ 0], _valueTextFields[ i]);
//			addItemListener( _radioButtons[ i][ 1], _spotCheckBoxes[ i], _spotSelectors[ i], _numberObjectComboBoxes[ i]);
			addItemListener( _radioButtons[ i][ 1], _numberObjectComboBoxes[ i]);
			addItemListener( _radioButtons[ i][ 2], _functionComboBoxes[ i], _expressionLabels[ i], _expressionTextFields[ i], _variableValueTables[ i]);
			addActionListener( _functionComboBoxes[ i], _expressionTextFields[ i], _variableValueTables[ i]);
			addItemListener( _functionComboBoxes[ i], _variableValueTables[ i]);
			addItemListener( _radioButtons[ i][ 3], _othersTextFields[ i]);
		}
	}

	/**
	 * @param radioButton
	 * @param textField
	 */
	private void addItemListener(RadioButton radioButton, final TextField textField) {
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
	}

	/**
	 * @param radioButton
	 * @param comboBox
	 */
	private void addItemListener(RadioButton radioButton, final ComboBox comboBox) {
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				comboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
	}

	/**
	 * @param radioButton
	 * @param functionComboBox
	 * @param expressionLabel
	 * @param expressionTextField
	 * @param variableValueTable
	 */
	private void addItemListener(RadioButton radioButton, final ComboBox functionComboBox, final JLabel expressionLabel, final TextField expressionTextField, final VariableValueTable variableValueTable) {
		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				functionComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				expressionLabel.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				expressionTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());

				if ( ItemEvent.SELECTED == arg0.getStateChange()) {
					if ( 0 <= _row && variableValueTable.getRowCount() > _row)
						variableValueTable.setRowSelectionInterval( _row, _row);
				} else {
					_row = variableValueTable.getSelectedRow();
					if ( 0 < variableValueTable.getRowCount())
						variableValueTable.removeRowSelectionInterval( 0, variableValueTable.getRowCount() - 1);
				}
				variableValueTable.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				variableValueTable.getTableHeader().repaint();
			}
		});
	}

	/**
	 * @param functionComboBox
	 * @param expressionTextField
	 * @param variableValueTable
	 */
	private void addActionListener(final ComboBox functionComboBox, final TextField expressionTextField, final VariableValueTable variableValueTable) {
		functionComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				expressionTextField.setText( VisualShellExpressionManager.get_instance().get_expression(
					( String)functionComboBox.getSelectedItem()));
				variableValueTable.set( ( String)functionComboBox.getSelectedItem());
			}
		});
	}

	/**
	 * @param functionComboBox
	 * @param variableValueTable
	 */
	private void addItemListener(ComboBox functionComboBox, final VariableValueTable variableValueTable) {
		functionComboBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if ( ItemEvent.DESELECTED == arg0.getStateChange())
					variableValueTable.update_variable_map( ( String)arg0.getItem());
			}
		});
	}

	/**
	 * 
	 */
	protected void adjust() {
		int width1 = 0;
		JComponent[] components = new JComponent[] {
			_spotCheckBox,
			_spotVariableCheckBox,
			_numberObjectLabel,
			_radioButtons[ 0][ 0],
			_radioButtons[ 1][ 0],
			_radioButtons[ 0][ 1],
			_radioButtons[ 1][ 1],
			_radioButtons[ 0][ 2],
			_radioButtons[ 1][ 2],
			_expressionLabels[ 0],
			_expressionLabels[ 1],
			_radioButtons[ 0][ 3],
			_radioButtons[ 1][ 3]
		};

		for ( int i = 0; i < components.length; ++i)
			width1 = Math.max( width1, components[ i].getPreferredSize().width);
		
		for ( int i = 0; i < components.length; ++i)
			components[ i].setPreferredSize(
				new Dimension( width1, components[ i].getPreferredSize().height));


		int width2 = _spotSelector._objectNameComboBox.getPreferredSize().width
			+ _spotSelector._numberSpinner.getPreferredSize().width;
		components = new JComponent[] {
			_valueTextFields[ 0],
			_valueTextFields[ 1],
			_numberObjectComboBoxes[ 0],
			_numberObjectComboBoxes[ 1],
			_functionComboBoxes[ 0],
			_functionComboBoxes[ 1],
			_expressionTextFields[ 0],
			_expressionTextFields[ 1],
			_othersTextFields[ 0],
			_othersTextFields[ 1]
		};
		
		for ( int i = 0; i < components.length; ++i)
			components[ i].setPreferredSize(
				new Dimension( width2, components[ i].getPreferredSize().height));


		for ( int i = 0; i < _radioButtons.length; ++i)
			_variableValueTableScrollPanes[ i].setPreferredSize(
				new Dimension( width1 + width2 + 5, 100));


		int width3 = _operatorComboBox1.getPreferredSize().width;
		width3 = Math.max( width3, _operatorLabel.getPreferredSize().width);

		_operatorLabel.setPreferredSize( new Dimension( width3,
			_operatorLabel.getPreferredSize().height));

		_operatorComboBox1.setPreferredSize( new Dimension( width3,
			_operatorComboBox1.getPreferredSize().height));

		for ( int i = 0; i < _dummy.length; ++i)
			_dummy[ i].setPreferredSize( new Dimension( width3,
				_dummy[ i].getPreferredSize().height));
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

		CommonTool.update( _numberObjectComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));
		CommonTool.update( _numberObjectComboBoxes[ 0], ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));
		CommonTool.update( _numberObjectComboBoxes[ 1], ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_number_object_names( false) : get_spot_number_object_names( false));

		for ( int i = 0; i < _variableValueTables.length; ++i)
			_variableValueTables[ i].update( !spotVariableCheckBox.isSelected() ? get_agent_number_object_names( false) : get_spot_number_object_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		super.update(objectSelector, spotVariableCheckBox, spotVariableComboBox);

		CommonTool.update( _numberObjectComboBox, get_spot_number_object_names( false));
		CommonTool.update( _numberObjectComboBoxes[ 0], get_spot_number_object_names( false));
		CommonTool.update( _numberObjectComboBoxes[ 1], get_spot_number_object_names( false));

		for ( int i = 0; i < _variableValueTables.length; ++i)
			_variableValueTables[ i].update( get_spot_number_object_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.object.entiy.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		super.update(spotObject, number, objectSelector, spotVariableCheckBox, spotVariableComboBox);

		CommonTool.update( _numberObjectComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));
		CommonTool.update( _numberObjectComboBoxes[ 0], !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));
		CommonTool.update( _numberObjectComboBoxes[ 1], !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));

		for ( int i = 0; i < _variableValueTables.length; ++i)
			_variableValueTables[ i].update( !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : get_spot_number_object_names( false));
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
		if ( _role instanceof AgentRole) {
			_spotCheckBox.setSelected( false);
			_spotSelector.setEnabled( false);
		} else {
			_spotCheckBox.setSelected( true);
			_spotCheckBox.setEnabled( false);
			_spotSelector.setEnabled( true);
		}

		for ( int i = 0; i < _radioButtons.length; ++i) {
			_radioButtons[ i][ 0].setSelected( true);
		}

		for ( int i = 0; i < _radioButtons[ 1].length; ++i)
			_radioButtons[ 1][ i].setEnabled( false);

		update_components( new boolean[] {
			true, false, false, false
		}, 0);

		update_components( new boolean[] {
			false, false, false, false
		}, 1);
	}

	/**
	 * @param enables
	 * @param index
	 */
	private void update_components(boolean[] enables, int index) {
		_valueTextFields[ index].setEnabled( enables[ 0]);

		_numberObjectComboBoxes[ index].setEnabled( enables[ 1]);

		_functionComboBoxes[ index].setEnabled( enables[ 2]);
		_expressionLabels[ index].setEnabled( enables[ 2]);
		_expressionTextFields[ index].setEnabled( enables[ 2]);
		_variableValueTableScrollPanes[ index].setEnabled( enables[ 2]);
		_variableValueTables[ index].setEnabled( enables[ 2]);

		_othersTextFields[ index].setEnabled( enables[ 3]);
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
//		CommonTool.update( _numberObjectComboBox, get_agent_number_object_names( false));
//
//		for ( int i = 0; i < _variableValueTables.length; ++i)
//			_variableValueTables[ i].update( get_agent_number_object_names( false));
//
//		for ( int i = 0; i < _radioButtons.length; ++i) {
//			CommonTool.update( _numberObjectComboBoxes[ i], get_agent_number_object_names( false));
//			_expressionTextFields[ i].setText( VisualShellExpressionManager.get_instance().get_expression(
//				( String)_functionComboBoxes[ i].getSelectedItem()));
//			_variableValueTables[ i].set( ( String)_functionComboBoxes[ i].getSelectedItem());
//		}

		reset( _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);

		for ( int i = 0; i < _radioButtons.length; ++i) {
			_expressionTextFields[ i].setText( VisualShellExpressionManager.get_instance().get_expression(
				( String)_functionComboBoxes[ i].getSelectedItem()));
			_variableValueTables[ i].set( ( String)_functionComboBoxes[ i].getSelectedItem());
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

	/**
	 * @param value
	 * @param subType
	 * @return
	 */
	protected boolean set(String value, SubType subType) {
		String main_value = subType.get_value( value);
		if ( null == main_value)
			return false;

		if ( subType._operator.equals( "")) {
			if ( !set1( main_value, subType._kind[ 0], 0))
				return false;
		} else {
			String[] values = main_value.split(
				( 0 > "-/".indexOf( subType._operator) ? " \\" : " ")
					+ subType._operator + " ");
			if ( null == values || 2 != values.length)
				return false;

			if ( !set1( values[ 0], subType._kind[ 0], 0))
				return false;

			if ( !set2( values[ 1], subType._kind[ 1], 1))
				return false;
		}

		_operatorComboBox1.setSelectedItem( subType._operator);

		return true;
	}

	/**
	 * @param value
	 * @param kind
	 * @param index
	 * @return
	 */
	private boolean set1(String value, int kind, int index) {
		switch ( kind) {
			case 0:
				if ( !set_value1( value, index))
					return false;
				break;
			case 1:
				if ( !set_number_object1( value, index))
					return false;
				break;
			case 2:
				if ( !set_expression1( value, index))
					return false;
				break;
			case 3:
				if ( !set_others1( value, index))
					return false;
				break;
			default:
				return false;
		}
	
		_radioButtons[ index][ kind].setSelected( true);
		return true;
	}

	/**
	 * @param value
	 * @param index
	 * @return
	 */
	protected boolean set_value1(String value, int index) {
		return false;
	}

	/**
	 * @param value
	 * @param index
	 * @return
	 */
	protected boolean set_number_object1(String value, int index) {
		return false;
	}

	/**
	 * @param value
	 * @param index
	 * @return
	 */
	protected boolean set_expression1(String value, int index) {
		return false;
	}

	/**
	 * @param value
	 * @param index
	 * @return
	 */
	protected boolean set_others1(String value, int index) {
		return false;
	}

	/**
	 * @param value
	 * @param kind
	 * @param index
	 * @return
	 */
	private boolean set2(String value, int kind, int index) {
		switch ( kind) {
			case 0:
				if ( !set_value2( value, index))
					return false;
				break;
			case 1:
				if ( !set_number_object2( value, index))
					return false;
				break;
			case 2:
				if ( !set_expression2( value, index))
					return false;
				break;
			case 3:
				if ( !set_others2( value, index))
					return false;
				break;
			default:
				return false;
		}
	
		_radioButtons[ index][ kind].setSelected( true);
		return true;
	}

	/**
	 * @param value
	 * @param index
	 * @return
	 */
	private boolean set_value2(String value, int index) {
		_valueTextFields[ index].setText( value);
		return true;
	}

	/**
	 * @param value
	 * @param index
	 * @return
	 */
	private boolean set_number_object2(String value, int index) {
		_numberObjectComboBoxes[ index].setSelectedItem( value);
		return true;
	}

	/**
	 * @param value
	 * @param index
	 * @return
	 */
	private boolean set_expression2(String value, int index) {
		ExpressionElements expressionElements = NumberRule.get2( value);
		if ( null == expressionElements)
			return false;

		set_expression( expressionElements, index);

		return true;
	}

	/**
	 * @param value
	 * @param index
	 * @return
	 */
	private boolean set_others2(String value, int index) {
		_othersTextFields[ index].setText( value);
		return true;
	}

	/**
	 * @param numberObject
	 * @param spotCheckBox
	 * @param spotSelector
	 * @param spotVariableCheckBox
	 * @param spotVariableComboBox
	 * @param numberObjectComboBox
	 * @return
	 */
	protected boolean set_number_object(String numberObject, CheckBox spotCheckBox, ObjectSelector spotSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox, ComboBox numberObjectComboBox) {
		String[] values = CommonRuleManipulator.get_spot_and_object( numberObject);
		if ( null == values) {
			set_handler();
			return false;
		}

		if ( null == values[ 0] && _role instanceof SpotRole) {
			set_handler();
			return false;
		}

		if ( !set( values[ 0], values[ 1], spotCheckBox, spotSelector, spotVariableCheckBox, spotVariableComboBox)) {
			set_handler();
			return false;
		}

		numberObjectComboBox.setSelectedItem( values[ 2]);

		return true;
	}

	/**
	 * @param expressionElements
	 * @param index
	 */
	protected void set_expression(ExpressionElements expressionElements, int index) {
		_functionComboBoxes[ index].setSelectedItem( expressionElements._function);
		_expressionTextFields[ index].setText( VisualShellExpressionManager.get_instance().get_expression(
			expressionElements._function));
		_variableMaps[ index].put( expressionElements._function, expressionElements._variables);
		_variableValueTables[ index].set( expressionElements._function);
		_variableValueTables[ index].update();
	}

	/**
	 * @param numberObject
	 * @param subTypes
	 * @param operator
	 * @return
	 */
	protected Rule get(String numberObject, String[] subTypes, String operator) {
		SubType subType = SubType.get( SwingTool.get_enabled_radioButton( _radioButtons[ 0]),
			_operatorComboBox1, SwingTool.get_enabled_radioButton( _radioButtons[ 1]));
		if ( null == subType)
			return null;

		String prefix = subType.get( subTypes);

		String value1 = get( subType._kind[ 0], 0);
		if ( null == value1)
			return null;

		if ( null == subType._operator || subType._operator.equals( ""))
			return Rule.create( _kind, _type, prefix + numberObject + operator + " " + value1);

		String value2 = get( subType._kind[ 1], 1);
		if ( null == value2)
			return null;

		return Rule.create( _kind, _type, prefix + numberObject + operator + " " + value1 + " " + subType._operator + " " + value2);
	}

//	/**
//	 * @param rule
//	 * @param numberObject
//	 * @param subTypes
//	 * @param operator
//	 * @return
//	 */
//	protected boolean get(Rule rule, String numberObject, String[] subTypes, String operator) {
//		SubType subType = SubType.get( SwingTool.get_enabled_radioButton( _radioButtons[ 0]),
//			_operator_comboBox1, SwingTool.get_enabled_radioButton( _radioButtons[ 1]));
//		if ( null == subType)
//			return false;
//
//		String prefix = subType.get( subTypes);
//
//		String value1 = get( subType._kind[ 0], 0);
//		if ( null == value1)
//			return false;
//
//		if ( null == subType._operator || subType._operator.equals( "")) {
//			rule._value = prefix + numberObject + operator + " " + value1;
//			rule._type = _type;
//			return true;
//		}
//
//		String value2 = get( subType._kind[ 1], 1);
//		if ( null == value2)
//			return false;
//
//		rule._value = prefix + numberObject + operator + " " + value1 + " " + subType._operator + " " + value2;
//		rule._type = _type;
//
//		return true;
//	}

	/**
	 * @param kind
	 * @param index
	 * @return
	 */
	private String get(int kind, int index) {
		switch ( kind) {
			case 0:
				return get_value( _valueTextFields[ index]);
			case 1:
				return get_number_object( index);
			case 2:
				return get_expression( _functionComboBoxes[ index], _variableValueTables[ index]);
			case 3:
				return get_others( _othersTextFields[ index]);
				//return _others_textFields[ index].getText();
			default:
				return null;
		}
	}

	/**
	 * @param valueTextField
	 * @return
	 */
	protected String get_value(TextField valueTextField) {
		String value = valueTextField.getText();
		if ( null == value || value.equals( "")
			|| value.equals( "$") || 0 < value.indexOf( '$')
			|| value.equals( "$Name") || value.equals( "$Role") || value.equals( "$Spot")
			|| 0 <= value.indexOf( Constant._experimentName))
			return null;

		if ( value.startsWith( "$")
			&& ( 0 < value.indexOf( "$", 1) || 0 < value.indexOf( ")", 1)))
			return null;

		String result = NumberObject.is_correct( value, "integer");
		if ( null == result || result.equals( "")) {
			result = NumberObject.is_correct( value, "real number");
			if ( null == result || result.equals( ""))
				return null;
		}

		return result;
	}

	/**
	 * @param spotCheckBox
	 * @param spotSelector
	 * @param spotVariableCheckBox
	 * @param spotVariableComboBox
	 * @param numberObjectComboBox
	 * @return
	 */
	protected String get_number_object(CheckBox spotCheckBox, ObjectSelector spotSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox, ComboBox numberObjectComboBox) {
		String numberObject = ( String)numberObjectComboBox.getSelectedItem();
		if ( null == numberObject || numberObject.equals( ""))
			return null;

		String spot = get( spotCheckBox, spotSelector, spotVariableCheckBox, spotVariableComboBox);

		return ( spot + numberObject);
	}

	/**
	 * @param index
	 * @return
	 */
	protected String get_number_object(int index) {
		String numberObject = ( String)_numberObjectComboBoxes[ index].getSelectedItem();
		if ( null == numberObject || numberObject.equals( ""))
			return null;

		return numberObject;
	}

	/**
	 * @param functionComboBox
	 * @param variableValueTable
	 * @return
	 */
	protected String get_expression(ComboBox functionComboBox, VariableValueTable variableValueTable) {
		String function = ( String)functionComboBox.getSelectedItem();
		if ( null == function || function.equals( ""))
			return null;

		Vector<String[]> variables = variableValueTable.get();
		if ( null == variables)
			return null;

		return NumberRule.get( function, variables);
	}

	/**
	 * @param othersTextField
	 * @return
	 */
	protected String get_others(TextField othersTextField) {
		if ( null == othersTextField.getText()
			|| othersTextField.getText().equals( "$")
			|| 0 < othersTextField.getText().indexOf( '$')
			|| othersTextField.getText().equals( "$Name")
			|| othersTextField.getText().equals( "$Role")
			|| othersTextField.getText().equals( "$Spot")
			|| 0 <= othersTextField.getText().indexOf( Constant._experimentName))
			return null;

		if ( othersTextField.getText().startsWith( "$")
			&& ( 0 < othersTextField.getText().indexOf( "$", 1)
			|| 0 < othersTextField.getText().indexOf( ")", 1)))
			return null;

		return othersTextField.getText();
	}
}
