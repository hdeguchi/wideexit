/**
 * 
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
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.legacy.condition.SpotCondition;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.combo.ComboBox;

/**
 * @author kurata
 *
 */
public class SpotConditionPropertyPanel extends RulePropertyPanelBase {

	/**
	 * 
	 */
	private CheckBox _denyCheckBox = null;

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
	private JLabel _dummy = null;

	/**
	 * 
	 */
	private CheckBox[] _spotCheckBoxes = new CheckBox[ 2];

	/**
	 * 
	 */
	private ObjectSelector[] _spotSelectors = new ObjectSelector[ 2];

	/**
	 * 
	 */
	private CheckBox[] _spotVariableCheckBoxes = new CheckBox[ 2];

	/**
	 * 
	 */
	private ComboBox[] _spotVariableComboBoxes = new ComboBox[ 2];

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
	public SpotConditionPropertyPanel(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
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

		setup_spot_selector0( northPanel);

		insert_vertical_strut( northPanel);

		setup_spot_selector1( northPanel);

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
	private void setup_spot_selector0(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_denyCheckBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.check.box.denial"),
			true, true);
		panel.add( _denyCheckBox);

		setup_spot_selector( 0, panel);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_spot_selector1(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_typeLabel = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot.condition.type"),
			true);
		panel.add( _typeLabel);

		_typeComboBox = create_comboBox(
			new String[] {
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot.condition.spot.condition"),
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot.condition.spot.comparison")
			}, false);
		_typeComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				update_components();
			}
		});
		panel.add( _typeComboBox);

		parent.add( panel);


		insert_vertical_strut( parent);


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy = new JLabel();
		panel.add( _dummy);

		setup_spot_selector( 1, panel);

		parent.add( panel);
	}

	/**
	 * @param index
	 * @param panel
	 */
	private void setup_spot_selector(final int index, JPanel panel) {
		_spotCheckBoxes[ index] = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot.condition.spot.check.box.name"),
			true, true);
		_spotCheckBoxes[ index].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_spotSelectors[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				on_update( ItemEvent.SELECTED == arg0.getStateChange(),
					_spotSelectors[ index],
					_spotVariableCheckBoxes[ index],
					_spotVariableComboBoxes[ index]);
			}
		});
		panel.add( _spotCheckBoxes[ index]);

		_spotSelectors[ index] = create_spot_selector( true, true, panel);


		_spotVariableCheckBoxes[ index] = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.spot.variable.check.box.name"),
			true, true);
		_spotVariableCheckBoxes[ index].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				on_update( _spotCheckBoxes[ index].isSelected(),
					_spotSelectors[ index],
					_spotVariableCheckBoxes[ index],
					_spotVariableComboBoxes[ index]);
			}
		});
		panel.add( _spotVariableCheckBoxes[ index]);

		_spotVariableComboBoxes[ index] = create_comboBox( null, _standardControlWidth, false);
		panel.add( _spotVariableComboBoxes[ index]);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width1 = ( _denyCheckBox.getPreferredSize().width + _spotCheckBoxes[ 0].getPreferredSize().width + 5);
		int width2 = _typeLabel.getPreferredSize().width;
		if ( width1 > width2)
			_typeLabel.setPreferredSize( new Dimension( width1, _typeLabel.getPreferredSize().height));
		else if ( width1 < width2)
			_denyCheckBox.setPreferredSize(
				new Dimension( width2 - _spotCheckBoxes[ 0].getPreferredSize().width - 5,
					_denyCheckBox.getPreferredSize().height));

		_dummy.setPreferredSize( new Dimension( _denyCheckBox.getPreferredSize().width, _dummy.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#reset(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void reset(ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( objectSelector.equals( _spotSelectors[ 0]))
			CommonTool.update( spotVariableComboBox, !_spotCheckBoxes[ 0].isSelected() ? get_agent_spot_variable_names( false) : get_spot_spot_variable_names( false));
		else if ( objectSelector.equals( _spotSelectors[ 1]))
			CommonTool.update( spotVariableComboBox, !_spotCheckBoxes[ 1].isSelected() ? get_agent_spot_variable_names( false) : get_spot_spot_variable_names( false));

		super.reset(objectSelector, spotVariableCheckBox, spotVariableComboBox);

		if ( objectSelector.equals( _spotSelectors[ 0]))
			objectSelector.setEnabled( _spotCheckBoxes[ 0].isSelected());
		else
			objectSelector.setEnabled( _spotCheckBoxes[ 1].isSelected());
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelectors[ 0]) && !objectSelector.equals( _spotSelectors[ 1]))
			return;

		super.update(objectSelector, spotVariableCheckBox, spotVariableComboBox);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.object.entiy.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelectors[ 0]) && !objectSelector.equals( _spotSelectors[ 1]))
			return;

		super.update(spotObject, number, objectSelector, spotVariableCheckBox, spotVariableComboBox);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.common.selector.ObjectSelector)
	 */
	@Override
	protected void update(ObjectSelector objectSelector) {
		if ( objectSelector.equals( _spotSelectors[ 0]))
			update( objectSelector, _spotVariableCheckBoxes[ 0], _spotVariableComboBoxes[ 0]);
		else if ( objectSelector.equals( _spotSelectors[ 1]))
			update( objectSelector, _spotVariableCheckBoxes[ 1], _spotVariableComboBoxes[ 1]);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.object.entiy.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector)
	 */
	@Override
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector) {
		if ( objectSelector.equals( _spotSelectors[ 0]))
			update( spotObject, number, objectSelector, _spotVariableCheckBoxes[ 0], _spotVariableComboBoxes[ 0]);
		else if ( objectSelector.equals( _spotSelectors[ 1]))
			update( spotObject, number, objectSelector, _spotVariableCheckBoxes[ 1], _spotVariableComboBoxes[ 1]);
	}

	/**
	 * 
	 */
	private void initialize() {
		for ( int i = 0; i < 2; ++i) {
			if ( _role instanceof AgentRole) {
				_spotCheckBoxes[ i].setSelected( false);
				_spotSelectors[ i].setEnabled( false);
			} else {
				_spotCheckBoxes[ i].setSelected( true);
				_spotCheckBoxes[ i].setEnabled( false);
				_spotSelectors[ i].setEnabled( true);
			}
		}

		_typeComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot.condition.spot.condition"));
	}

	/**
	 * 
	 */
	private void update_components() {
		String type = ( String)_typeComboBox.getSelectedItem();
		if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot.condition.spot.comparison"))) {
			if ( _role instanceof AgentRole)
				_spotCheckBoxes[ 1].setEnabled( true);

			_spotSelectors[ 1].setEnabled( true);
			on_update( _spotCheckBoxes[ 1].isSelected(),
				_spotSelectors[ 1],
				_spotVariableCheckBoxes[ 1],
				_spotVariableComboBoxes[ 1]);
		} else {
			_spotCheckBoxes[ 1].setEnabled( false);
			_spotSelectors[ 1].setEnabled( false);
			_spotVariableCheckBoxes[ 1].setEnabled( false);
			_spotVariableComboBoxes[ 1].setEnabled( false);
		}
	}

	/**
	 * 
	 */
	protected void set_handler() {
		for ( int i = 0; i < 2; ++i)
			_spotSelectors[ i].set_handler( this);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#on_setup_completed()
	 */
	@Override
	public void on_setup_completed() {
		for ( int i = 0; i < 2; ++i)
			reset( _spotSelectors[ i], _spotVariableCheckBoxes[ i], _spotVariableComboBoxes[ i]);

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

		if ( null == rule || !_type.equals( rule._type) /*|| rule._value.equals( "")*/) {
			set_handler();
			return false;
		}

		String[][] values = SpotCondition.get_values( rule._value);
		if ( null == values) {
			set_handler();
			return false;
		}

		if ( !set( values[ 0][ 0], values[ 0][ 1], _spotCheckBoxes[ 0], _spotSelectors[ 0], _spotVariableCheckBoxes[ 0], _spotVariableComboBoxes[ 0])) {
			set_handler();
			return false;
		}

		if ( null != values[ 1]) {
			if ( !set( values[ 1][ 0], values[ 1][ 1], _spotCheckBoxes[ 1], _spotSelectors[ 1], _spotVariableCheckBoxes[ 1], _spotVariableComboBoxes[ 1])) {
				set_handler();
				return false;
			}

			_typeComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot.condition.spot.comparison"));
		}

		_denyCheckBox.setSelected( rule._value.startsWith( "!"));

		set_handler();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get()
	 */
	@Override
	public Rule get() {
		String[] values = new String[] { null, null};

		values[ 0] = get( _spotCheckBoxes[ 0], _spotSelectors[ 0], _spotVariableCheckBoxes[ 0], _spotVariableComboBoxes[ 0]);
		if ( values[ 0].equals( ""))
			return null;

		String type = ( String)_typeComboBox.getSelectedItem();
		if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot.condition.spot.comparison"))) {
			values[ 1] = get( _spotCheckBoxes[ 1], _spotSelectors[ 1], _spotVariableCheckBoxes[ 1], _spotVariableComboBoxes[ 1]);
			if ( values[ 1].equals( ""))
				return null;
		}

		return Rule.create( _kind, _type,
			( _denyCheckBox.isSelected() ? "!" : "") + SpotCondition.get_rule_value( values));
	}
}
