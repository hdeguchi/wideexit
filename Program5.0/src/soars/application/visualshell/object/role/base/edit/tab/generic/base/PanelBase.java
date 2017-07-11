/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.base;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.edit.tab.generic.base.immediate.ImmediateTimeVariablePanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.common.ObjectPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.common.SubjectPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Variable;
import soars.application.visualshell.object.role.base.object.generic.element.EntityVariableRule;
import soars.application.visualshell.object.role.base.object.generic.element.IObject;
import soars.application.visualshell.object.role.spot.SpotRole;
import soars.application.visualshell.object.stage.StageManager;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.tool.reflection.Reflection;

/**
 * @author kurata
 *
 */
public class PanelBase extends PanelRoot {

	/**
	 * 
	 */
	protected VerbPanel _verbPanel = null;

	/**
	 * 
	 */
	protected ObjectSelector _agentSelector = null;

	/**
	 * 
	 */
	protected ObjectSelector _spotSelector = null;

	/**
	 * Used when only one entity exists.
	 * "agent", "self", "spot" or "currentspot"
	 */
	protected String _entity = null;

	/**
	 * 
	 */
	protected List<RadioButton> _radioButtons = new ArrayList<RadioButton>();

	/**
	 * 
	 */
	/*static */protected Map<RadioButton, String> _radioButtonEntityMap = new HashMap<RadioButton, String>();

	/**
	 * 
	 */
	/*static */protected Map<String, RadioButton> _entityRadioButtonMap = new HashMap<String, RadioButton>();

	/**
	 * 
	 */
	protected JLabel _label = null;

	/**
	 * 
	 */
	protected CheckBox _agentVariableCheckBox = null;

	/**
	 * 
	 */
	protected ComboBox _agentVariableComboBox = null;

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
	protected List<CheckBox> _entityVariableCheckBoxes = new ArrayList<CheckBox>();

	/**
	 * 
	 */
	protected ComboBox _variableTypeComboBox = null;

	/**
	 * 
	 */
	protected Map<String, JPanel> _variablePanelMap = new HashMap<String, JPanel>();

	/**
	 * 
	 */
	protected Map<String, ComboBox>	_variableComboBoxMap = new HashMap<String, ComboBox>();

	/**
	 * 
	 */
	protected Map<String, JComponent>	_variableComponentMap = new HashMap<String, JComponent>();

	/**
	 * 
	 */
	protected int _numberOfPanels = 0;

	/**
	 * @param verbPanel
	 * @param property
	 * @param role
	 * @param buddiesMap
	 * @param rulePropertyPanelBase
	 */
	public PanelBase(VerbPanel verbPanel, Property property, Role role, Map<String, List<PanelRoot>> buddiesMap, RulePropertyPanelBase rulePropertyPanelBase) {
		super(property, role, buddiesMap, rulePropertyPanelBase);
		_verbPanel = verbPanel;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#setup()
	 */
	@Override
	public boolean setup() {
		if ( !super.setup())
			return false;

		List<String> entities = new ArrayList<String>();
		List<String> entityVariables = new ArrayList<String>();
		List<Variable> variables = new ArrayList<Variable>();
		get_all_components( entities, entityVariables, variables);
		if ( entities.isEmpty()) {
			if ( this instanceof SubjectPanel) {
				if ( _role instanceof AgentRole)
					_entity = "self";
				else if ( _role instanceof SpotRole)
					_entity = "currentspot";
				else
					return false;

				if ( entityVariables.isEmpty() && variables.isEmpty()) {
					_visible = false;
					return true;
				}
			} else if ( this instanceof ObjectPanel) {
				if ( entityVariables.isEmpty() && variables.isEmpty())
					return false;
			}
		}

		if ( 1 == entities.size()) {
			if ( _role instanceof AgentRole && entities.get( 0).equals( "self")) {
				_entity = "self";
				if ( this instanceof SubjectPanel) {
					entities.clear();
					if ( entityVariables.isEmpty() && variables.isEmpty()) {
						_visible = false;
						return true;
					}
//				} else if ( this instanceof ObjectPanel) {
//					if ( entityVariables.isEmpty() && variables.isEmpty())
//						return false;
				}
			}

			if ( _role instanceof SpotRole && entities.get( 0).equals( "currentspot")) {
				_entity = "currentspot";
				if ( this instanceof SubjectPanel) {
					entities.clear();
					if ( entityVariables.isEmpty() && variables.isEmpty()) {
						_visible = false;
						return true;
					}
//				} else if ( this instanceof ObjectPanel) {
//					if ( entityVariables.isEmpty() && variables.isEmpty())
//						return false;
				}
			}
		}

		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));

		add( Box.createHorizontalStrut( 2));

		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		SwingTool.insert_vertical_strut( northPanel, 5);

		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 1, 3));
		//panel.setLayout( new GridLayout( 1, ( ( entities.isEmpty() ? 0 : 1) + ( entityVariables.isEmpty() ? 0 : 1) + ( variables.isEmpty() ? 0 : 1))));

		List<RadioButton> radioButtons = new ArrayList<RadioButton>();

		setup_entities_panel( entities, radioButtons, panel);

		setup_entity_variables_panel( entityVariables, panel);

		setup_variables_panel( variables, panel);

		//int num = ( ( entities.isEmpty() ? 0 : 1) + ( entityVariables.isEmpty() ? 0 : 1) + ( variables.isEmpty() ? 0 : 1));
		for ( int i = 0; i < 3 - _numberOfPanels; ++i)
			panel.add( new JPanel());

		northPanel.add( panel);

		basePanel.add( northPanel, "North");

		basePanel.setBorder( BorderFactory.createLineBorder( this instanceof ObjectPanel ? Color.blue : getForeground(), 1));

		add( basePanel);

		add( Box.createHorizontalStrut( 2));

		adjust( radioButtons);

		//print( entities, entityVariables, variables);

		return true;
	}

	/**
	 * @param entities
	 * @param entityVariables
	 * @param variables
	 */
	protected void get_all_components(List<String> entities, List<String> entityVariables, List<Variable> variables) {
	}

	/**
	 * @param entities
	 * @param radioButtons
	 * @param parent
	 */
	protected void setup_entities_panel(List<String> entities, List<RadioButton> radioButtons, JPanel parent) {
		if ( entities.isEmpty())
			return;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		if ( 0 == _numberOfPanels)
			panel.add( Box.createHorizontalStrut( 5));

		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		ButtonGroup buttonGroup = ( ( 1 < entities.size()) ? new ButtonGroup() : null);

		if ( _role instanceof AgentRole) {
			setup_self( entities, buttonGroup, northPanel);
			setup_agentSelector( entities, buttonGroup, radioButtons, northPanel);
			setup_spotSelector( entities, buttonGroup, radioButtons, northPanel);
			setup_currentSpot( entities, buttonGroup, northPanel);
		} else if ( _role instanceof SpotRole) {
			setup_currentSpot( entities, buttonGroup, northPanel);
			setup_spotSelector( entities, buttonGroup, radioButtons, northPanel);
			setup_agentSelector( entities, buttonGroup, radioButtons, northPanel);
		}

		basePanel.add( northPanel, "North");

		panel.add( basePanel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		++_numberOfPanels;
	}

	/**
	 * @param entities 
	 * @param buttonGroup
	 * @param parent
	 */
	private void setup_self(List<String> entities, ButtonGroup buttonGroup, JPanel parent) {
		if ( !entities.contains( "self"))
			return;

		if ( null != buttonGroup) {
			JPanel panel = new JPanel();
			panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

			RadioButton radioButton = _rulePropertyPanelBase.create_radioButton( ResourceManager.get_instance().get( "generic.gui.rule.self"), buttonGroup, true, false);
			radioButton.addItemListener( new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					if ( ItemEvent.SELECTED == arg0.getStateChange())
						on_self_selected();
				}
			});
			_radioButtons.add( radioButton);
			_radioButtonEntityMap.put( radioButton, "self");
			_entityRadioButtonMap.put( "self", radioButton);
			panel.add( radioButton);

			parent.add( panel);
		} else {
			JPanel panel = new JPanel();
			panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

			_label = _rulePropertyPanelBase.create_label( ResourceManager.get_instance().get( "generic.gui.rule.self") + " ", true);
			panel.add( _label);
			_entity = "self";

			parent.add( panel);
		}

		SwingTool.insert_vertical_strut( parent, 5);
	}

	/**
	 * 
	 */
	protected void on_self_selected() {
		if ( null != _agentVariableCheckBox)
			CommonTool.update( _agentVariableComboBox, GenericPropertyPanel.get_agent_agent_variable_names( false));

		if ( null != _spotVariableCheckBox)
			CommonTool.update( _spotVariableComboBox, GenericPropertyPanel.get_agent_spot_variable_names( false));

		if ( null != _agentVariableCheckBox && _agentVariableCheckBox.isSelected()) {
			on_agent_variable_selected( ( String)_agentVariableComboBox.getSelectedItem());
			return;
		}

		if ( null != _spotVariableCheckBox && _spotVariableCheckBox.isSelected()) {
			on_spot_variable_selected( ( String)_spotVariableComboBox.getSelectedItem());
			return;
		}

		on_agent_selected();
	}

	/**
	 * @param entities
	 * @param buttonGroup
	 * @param radioButtons
	 * @param parent
	 */
	private void setup_agentSelector(List<String> entities, ButtonGroup buttonGroup, List<RadioButton> radioButtons, JPanel parent) {
		if ( !entities.contains( "agent"))
			return;

		JPanel panel = new JPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();
		panel.setLayout( gridBagLayout);

		GridBagConstraints gridBagConstraints = new GridBagConstraints();

		if ( null != buttonGroup) {
			RadioButton radioButton = _rulePropertyPanelBase.create_radioButton( ResourceManager.get_instance().get( "generic.gui.rule.agent") + " ", buttonGroup, true, false);
			radioButton.addItemListener( new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					_agentSelector.setEnabled( ItemEvent.SELECTED == arg0.getStateChange());
					if ( ItemEvent.SELECTED == arg0.getStateChange())
						on_agentSelector_selected();
				}
			});
			_radioButtons.add( radioButton);
			_radioButtonEntityMap.put( radioButton, "agent");
			_entityRadioButtonMap.put( "agent", radioButton);
			gridBagConstraints.fill = GridBagConstraints.NONE;
			gridBagConstraints.weightx = 0.0;
			gridBagConstraints.weighty = 0.0;
			gridBagLayout.setConstraints( radioButton, gridBagConstraints);
			panel.add( radioButton);
			radioButtons.add( radioButton);
		} else {
			_label = _rulePropertyPanelBase.create_label( ResourceManager.get_instance().get( "generic.gui.rule.agent") + " ", true);
			panel.add( _label);
			_entity = "agent";
		}

		//JPanel partialPanel = new JPanel();
		//partialPanel.setLayout( new BorderLayout());
		//partialPanel.setLayout( new BoxLayout( partialPanel, BoxLayout.X_AXIS));
		//partialPanel.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0));

		_agentSelector = new ObjectSelector( "agent", GenericPropertyPanel.get_agent_names( false), _standardNameWidth, _standardNumberSpinnerWidth, _property._color, true, this, null);
		_agentSelector.selectFirstItem();
		//partialPanel.add( _agentSelector);
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0.0;
		gridBagLayout.setConstraints( _agentSelector, gridBagConstraints);
		//gridBagLayout.setConstraints( partialPanel, gridBagConstraints);

		panel.add( _agentSelector);
		//panel.add( partialPanel);

		parent.add( panel);

		SwingTool.insert_vertical_strut( parent, 5);
	}

	/**
	 * 
	 */
	protected void on_agentSelector_selected() {
		AgentObject agentObject = LayerManager.get_instance().get_agent( _agentSelector.get_name());
		on_objectSelector_selected( agentObject, _agentSelector.get_number());
	}

	/**
	 * @param entities
	 * @param buttonGroup
	 * @param radioButtons
	 * @param parent
	 */
	private void setup_spotSelector(List<String> entities, ButtonGroup buttonGroup, List<RadioButton> radioButtons, JPanel parent) {
		if ( !entities.contains( "spot"))
			return;

		JPanel panel = new JPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();
		panel.setLayout( gridBagLayout);

		GridBagConstraints gridBagConstraints = new GridBagConstraints();

		if ( null != buttonGroup) {
			RadioButton radioButton = _rulePropertyPanelBase.create_radioButton( ResourceManager.get_instance().get( "generic.gui.rule.spot") + " ", buttonGroup, true, false);
			radioButton.addItemListener( new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					_spotSelector.setEnabled( ItemEvent.SELECTED == arg0.getStateChange());
					if ( ItemEvent.SELECTED == arg0.getStateChange())
						on_spotSelector_selected();
				}
			});
			_radioButtons.add( radioButton);
			_radioButtonEntityMap.put( radioButton, "spot");
			_entityRadioButtonMap.put( "spot", radioButton);
			gridBagConstraints.fill = GridBagConstraints.NONE;
			gridBagConstraints.weightx = 0.0;
			gridBagConstraints.weighty = 0.0;
			gridBagLayout.setConstraints( radioButton, gridBagConstraints);
			panel.add( radioButton);
			radioButtons.add( radioButton);
		} else {
			_label = _rulePropertyPanelBase.create_label( ResourceManager.get_instance().get( "generic.gui.rule.spot") + " ", true);
			panel.add( _label);
			_entity = "spot";
		}

		//JPanel partialPanel = new JPanel();
		//partialPanel.setLayout( new BorderLayout());
		//partialPanel.setLayout( new BoxLayout( partialPanel, BoxLayout.X_AXIS));
		//partialPanel.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0));

		_spotSelector = new ObjectSelector( "spot", GenericPropertyPanel.get_spot_names( false), _standardNameWidth, _standardNumberSpinnerWidth, _property._color, true, this, null);
		_spotSelector.selectFirstItem();
		//partialPanel.add( _spotSelector);
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0.0;
		gridBagLayout.setConstraints( _spotSelector, gridBagConstraints);
		//gridBagLayout.setConstraints( partialPanel, gridBagConstraints);

		panel.add( _spotSelector);
		//panel.add( partialPanel);

		parent.add( panel);

		SwingTool.insert_vertical_strut( parent, 5);
	}

	/**
	 * 
	 */
	protected void on_spotSelector_selected() {
		SpotObject spotObject = LayerManager.get_instance().get_spot( _spotSelector.get_name());
		on_objectSelector_selected( spotObject, _spotSelector.get_number());
	}

	/**
	 * 
	 */
	public void on_objectSelector_selected(EntityBase entityBase, String number) {
		if ( null != _agentVariableCheckBox)
			update_comboBox( _agentVariableComboBox, "agent variable", entityBase, number);

		if ( null != _spotVariableCheckBox)
			update_comboBox( _spotVariableComboBox, "spot variable", entityBase, number);

		if ( null != _agentVariableCheckBox && _agentVariableCheckBox.isSelected()) {
			on_agent_variable_selected( ( String)_agentVariableComboBox.getSelectedItem());
			return;
		}

		if ( null != _spotVariableCheckBox && _spotVariableCheckBox.isSelected()) {
			on_spot_variable_selected( ( String)_spotVariableComboBox.getSelectedItem());
			return;
		}

		on_entity_selected( entityBase, number);
	}

	/**
	 * @param entities 
	 * @param buttonGroup
	 * @param parent
	 */
	private void setup_currentSpot(List<String> entities, ButtonGroup buttonGroup, JPanel parent) {
		if ( !entities.contains( "currentspot"))
			return;

		String text = ( ( _role instanceof SpotRole) ? ResourceManager.get_instance().get( "generic.gui.rule.self") : ResourceManager.get_instance().get( "generic.gui.rule.current.spot"));

		if ( null != buttonGroup) {
			JPanel panel = new JPanel();
			panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

			RadioButton radioButton = _rulePropertyPanelBase.create_radioButton( text, buttonGroup, true, false);
			radioButton.addItemListener( new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					if ( ItemEvent.SELECTED == arg0.getStateChange())
						on_currentSpot_selected();
				}
			});
			_radioButtons.add( radioButton);
			_radioButtonEntityMap.put( radioButton, "currentspot");
			_entityRadioButtonMap.put( "currentspot", radioButton);
			panel.add( radioButton);

			parent.add( panel);
		} else {
			JPanel panel = new JPanel();
			panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

			_label = _rulePropertyPanelBase.create_label( text + " ", true);
			panel.add( _label);
			_entity = "currentspot";

			parent.add( panel);
		}

		SwingTool.insert_vertical_strut( parent, 5);
	}

	/**
	 * 
	 */
	protected void on_currentSpot_selected() {
		if ( null != _agentVariableCheckBox)
			CommonTool.update( _agentVariableComboBox, GenericPropertyPanel.get_spot_agent_variable_names( false));

		if ( null != _spotVariableCheckBox)
			CommonTool.update( _spotVariableComboBox, GenericPropertyPanel.get_spot_spot_variable_names( false));

		if ( null != _agentVariableCheckBox && _agentVariableCheckBox.isSelected()) {
			on_agent_variable_selected( ( String)_agentVariableComboBox.getSelectedItem());
			return;
		}

		if ( null != _spotVariableCheckBox && _spotVariableCheckBox.isSelected()) {
			on_spot_variable_selected( ( String)_spotVariableComboBox.getSelectedItem());
			return;
		}

		on_spot_selected();
	}

	/**
	 * @param entityVariables
	 * @param parent
	 */
	protected void setup_entity_variables_panel(List<String> entityVariables, JPanel parent) {
		if ( entityVariables.isEmpty())
			return;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		if ( 0 == _numberOfPanels)
			panel.add( Box.createHorizontalStrut( 5));

		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		setup_agentVariableComboBox( entityVariables, northPanel);

		setup_spotVariableComboBox( entityVariables, northPanel);

		basePanel.add( northPanel, "North");

		panel.add( basePanel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		++_numberOfPanels;
	}

	/**
	 * @param entityVariables
	 * @param parent
	 */
	private void setup_agentVariableComboBox(List<String> entityVariables, JPanel parent) {
		if ( !entityVariables.contains( "agentvariable"))
			return;

		JPanel panel = new JPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();
		panel.setLayout( gridBagLayout);

		GridBagConstraints gridBagConstraints = new GridBagConstraints();

		_agentVariableCheckBox = _rulePropertyPanelBase.create_checkBox( ResourceManager.get_instance().get( "generic.gui.rule.agent.variable") + " ", true, false);
		_agentVariableCheckBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_agentVariableComboBox.setEnabled( ItemEvent.SELECTED == arg0.getStateChange());
				if ( ItemEvent.SELECTED == arg0.getStateChange()) {
					if ( null != _spotVariableCheckBox)
						_spotVariableCheckBox.setSelected( false);
				}
				update();
			}
		});
		_entityVariableCheckBoxes.add( _agentVariableCheckBox);
		gridBagConstraints.fill = GridBagConstraints.NONE;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.weighty = 0.0;
		gridBagLayout.setConstraints( _agentVariableCheckBox, gridBagConstraints);
		panel.add( _agentVariableCheckBox);

		_agentVariableComboBox = _rulePropertyPanelBase.create_comboBox( null, _standardControlWidth, false);
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0.0;
		gridBagLayout.setConstraints( _agentVariableComboBox, gridBagConstraints);
		panel.add( _agentVariableComboBox);

		parent.add( panel);

		SwingTool.insert_vertical_strut( parent, 5);
	}

	/**
	 * @param entityVariables
	 * @param parent
	 */
	private void setup_spotVariableComboBox(List<String> entityVariables, JPanel parent) {
		if ( !entityVariables.contains( "spotvariable"))
			return;

		JPanel panel = new JPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();
		panel.setLayout( gridBagLayout);

		GridBagConstraints gridBagConstraints = new GridBagConstraints();

		_spotVariableCheckBox = _rulePropertyPanelBase.create_checkBox( ResourceManager.get_instance().get( "generic.gui.rule.spot.variable") + " ", true, false);
		_spotVariableCheckBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_spotVariableComboBox.setEnabled( ItemEvent.SELECTED == arg0.getStateChange());
				if ( ItemEvent.SELECTED == arg0.getStateChange()) {
					if ( null != _agentVariableCheckBox)
						_agentVariableCheckBox.setSelected( false);
				}
				update();
			}
		});
		_entityVariableCheckBoxes.add( _spotVariableCheckBox);
		gridBagConstraints.fill = GridBagConstraints.NONE;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.weighty = 0.0;
		gridBagLayout.setConstraints( _spotVariableCheckBox, gridBagConstraints);
		panel.add( _spotVariableCheckBox);

		_spotVariableComboBox = _rulePropertyPanelBase.create_comboBox( null, _standardControlWidth, false);
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0.0;
		gridBagLayout.setConstraints( _spotVariableComboBox, gridBagConstraints);
		panel.add( _spotVariableComboBox);

		parent.add( panel);

		SwingTool.insert_vertical_strut( parent, 5);
	}

	/**
	 * 
	 */
	private void update() {
		// RadioButtonが無い場合があるので要注意！
		update( ( null != _entity) ? _entity : _radioButtonEntityMap.get( get_selected_radioButton()));
	}

	/**
	 * @return
	 */
	protected RadioButton get_selected_radioButton() {
		for ( RadioButton radioButton:_radioButtons) {
			if ( radioButton.isSelected())
				return radioButton;
		}
		return null;
	}

	/**
	 * @param entity
	 */
	public void update(String entity) {
		if ( null != entity) {
			if ( entity.equals( "agent"))
				on_agentSelector_selected();
			else if ( entity.equals( "self"))
				on_self_selected();
			else if ( entity.equals( "spot"))
				on_spotSelector_selected();
			else if ( entity.equals( "currentspot"))
				on_currentSpot_selected();
		}
	}

	/**
	 * @param variables
	 * @param parent
	 */
	protected void setup_variables_panel(List<Variable> variables, JPanel parent) {
		if ( variables.isEmpty())
			return;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		if ( 0 == _numberOfPanels)
			panel.add( Box.createHorizontalStrut( 5));

		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		setup_variable_type_panel( northPanel);

		SwingTool.insert_vertical_strut( northPanel, 5);

		setup_variable_component_panel( variables, northPanel);

		basePanel.add( northPanel, "North");

		panel.add( basePanel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		++_numberOfPanels;
	}

	/**
	 * @param parent
	 * @return
	 */
	private void setup_variable_type_panel(JPanel parent) {
		List<Variable> variables = get_variables();
		if ( variables.isEmpty())
			return;

		List<String> variableTypeNames = new ArrayList<String>();
		for ( Variable variable:variables)
			variableTypeNames.add( variable._name);

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		_variableTypeComboBox = _rulePropertyPanelBase.create_comboBox( variableTypeNames.toArray( new String[ 0]), _standardControlWidth, false);
		_variableTypeComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update_variable_panel();
			}
		});
		panel.add( _variableTypeComboBox);

		parent.add( panel);
	}

	/**
	 * @return
	 */
	protected Vector<Variable> get_variables() {
		return null;
	}

	/**
	 * @param variables
	 * @param parent
	 */
	private void setup_variable_component_panel(List<Variable> variables, JPanel parent) {
		if ( variables.isEmpty())
			return;

		for ( Variable variable:variables)
			setup_variable_component_panel( variable, parent);

		SwingTool.insert_vertical_strut( parent, 5);
	}

	/**
	 * @param variable
	 * @param parent
	 */
	private void setup_variable_component_panel(Variable variable, JPanel parent) {
		if ( null != _variablePanelMap.get( variable._type))
			return;

		if ( Constant.contains( variable._type)
			|| variable._type.equals( "agent variable")	// TODO いずれはConstant._kindsへ
			|| variable._type.equals( "integer number object")
			|| variable._type.equals( "real number object")) {
			// entity及びentiry variableの選択状態により内容が変化するコンボボックス
			JPanel panel = new JPanel();
			panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

			ComboBox comboBox = _rulePropertyPanelBase.create_comboBox( null, _standardControlWidth, false);
			panel.add( comboBox);

			_variablePanelMap.put( variable._type, panel);
			_variableComponentMap.put( variable._type, comboBox);
			_variableComboBoxMap.put( variable._type, comboBox);

			parent.add( panel);
		} else if ( variable._type.equals( "agent")) {
			JPanel panel = new JPanel();
			panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

			ObjectSelector agentSelector = new ObjectSelector( "agent", GenericPropertyPanel.get_agent_names( variable._empty), _standardNameWidth, _standardNumberSpinnerWidth, _property._color, true/*, this*/, null);
			agentSelector.selectFirstItem();
			panel.add( agentSelector);

			_variablePanelMap.put( variable._type, panel);
			_variableComponentMap.put( variable._type, agentSelector);

			parent.add( panel);
		} else if ( variable._type.equals( "spot")) {
			JPanel panel = new JPanel();
			panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

			ObjectSelector spotSelector = new ObjectSelector( "spot", GenericPropertyPanel.get_spot_names( variable._empty), _standardNameWidth, _standardNumberSpinnerWidth, _property._color, true/*, this*/, null);
			spotSelector.selectFirstItem();
			panel.add( spotSelector);

			_variablePanelMap.put( variable._type, panel);
			_variableComponentMap.put( variable._type, spotSelector);

			parent.add( panel);
		} else if ( variable._type.equals( "role") || variable._type.equals( "agent role") || variable._type.equals( "spot role")) {
			List<Object> resultList = new ArrayList<Object>();
			if ( !Reflection.execute_static_method( GenericPropertyPanel.class, "get_" + _typeNameMap.get( variable._type) + "_names", new Class[] { boolean.class}, new Object[] { variable._empty}, resultList))
				return;

			JPanel panel = new JPanel();
			panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

			ComboBox comboBox = _rulePropertyPanelBase.create_comboBox( ( String[])resultList.get( 0), _standardControlWidth, false);
			panel.add( comboBox);

			_variablePanelMap.put( variable._type, panel);
			_variableComponentMap.put( variable._type, comboBox);

			parent.add( panel);
		} else if ( variable._type.equals( "stage")) {
			JPanel panel = new JPanel();
			panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

			ComboBox comboBox = _rulePropertyPanelBase.create_comboBox( StageManager.get_instance().get_names( variable._empty), _standardControlWidth, false);
			panel.add( comboBox);

			_variablePanelMap.put( variable._type, panel);
			_variableComponentMap.put( variable._type, comboBox);

			parent.add( panel);
		} else {
			if ( variable._type.equals( "immediate probability"))
				append( variable._type, new TextExcluder( Constant._prohibitedCharacters5), parent);
			else if ( variable._type.equals( "immediate keyword"))
				append( variable._type, new TextExcluder( Constant._prohibitedCharacters3), parent);
			else if ( variable._type.equals( "immediate integer"))
				append( variable._type, new TextExcluder( Constant._prohibitedCharacters5), parent);
			else if ( variable._type.equals( "immediate real number"))
				append( variable._type, new TextExcluder( Constant._prohibitedCharacters5), parent);
			else if ( variable._type.equals( "immediate time variable"))
				append_immediateTimeVariablePanel( parent);
			else if ( variable._type.equals( "immediate exchange algebra base"))
				append( variable._type, new TextExcluder( Constant._prohibitedCharacters12), parent);
			else if ( variable._type.equals( "immediate data"))
				append( variable._type, null, parent);
		}
	}

	/**
	 * @param type
	 * @param textExcluder
	 * @param parent
	 */
	private void append(String type, TextExcluder textExcluder, JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		TextField textField = ( null == textExcluder)
			? _rulePropertyPanelBase.create_textField( _standardControlWidth, false)
			: _rulePropertyPanelBase.create_textField( textExcluder, _standardControlWidth, false);
		_textUndoRedoManagers.add( new TextUndoRedoManager( textField, this));
		panel.add( textField);

		_variablePanelMap.put( type, panel);
		_variableComponentMap.put( type, textField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_variableTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		TextField textField = _rulePropertyPanelBase.create_textField( _standardControlWidth, false);
		_textUndoRedoManagers.add( new TextUndoRedoManager( textField, this));
		panel.add( textField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void append_immediateTimeVariablePanel(JPanel parent) {
		ImmediateTimeVariablePanel ImmediateTimeVariablePanel = new ImmediateTimeVariablePanel( _rulePropertyPanelBase, _standardControlWidth, _textUndoRedoManagers);
		if ( !ImmediateTimeVariablePanel.setup())
			return;

		_variablePanelMap.put( "immediate time variable", ImmediateTimeVariablePanel);
		_variableComponentMap.put( "immediate time variable", ImmediateTimeVariablePanel);

		parent.add( ImmediateTimeVariablePanel);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.selector.IObjectSelectorHandler#changed(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector)
	 */
	@Override
	public void changed(String name, String number, String fullName, ObjectSelector objectSelector) {
		if ( objectSelector == _agentSelector)
			on_agentSelector_selected();
		else if ( objectSelector == _spotSelector)
			on_spotSelector_selected();
	}

	/**
	 * @param entityVariable
	 */
	public void on_agent_variable_selected(String entityVariable) {
		Collection<String> types = _variableComboBoxMap.keySet();
		for ( String type:types) {
			if ( null == entityVariable)
				_variableComboBoxMap.get( type).removeAllItems();
			else
				update_comboBox( type, "get_agent_");
		}
	}

	/**
	 * 
	 */
	public void on_agent_selected() {
		Collection<String> types = _variableComboBoxMap.keySet();
		for ( String type:types)
			update_comboBox( type, "get_agent_");
	}

	/**
	 * @param entityVariable
	 */
	public void on_spot_variable_selected(String entityVariable) {
		Collection<String> types = _variableComboBoxMap.keySet();
		for ( String type:types) {
			if ( null == entityVariable)
				_variableComboBoxMap.get( type).removeAllItems();
			else
				update_comboBox( type, "get_spot_");
		}
	}

	/**
	 * 
	 */
	public void on_spot_selected() {
		Collection<String> types = _variableComboBoxMap.keySet();
		for ( String type:types)
			update_comboBox( type, "get_spot_");
	}

	/**
	 * @param type
	 * @param prefix
	 */
	private void update_comboBox(String type, String prefix) {
		List<Object> resultList = new ArrayList<Object>();
		if ( !Reflection.execute_static_method( GenericPropertyPanel.class, prefix + _typeNameMap.get( type) + "_names", new Class[] { boolean.class}, new Object[] { contains_empty( type)}, resultList))
			return;

		// TODO 2015.7.13
		if ( null == resultList)
			return;

		CommonTool.update( _variableComboBoxMap.get( type), ( String[])resultList.get( 0));
	}

	/**
	 * @param entityBase
	 * @param number
	 */
	public void on_entity_selected(EntityBase entityBase, String number) {
		Collection<String> types = _variableComboBoxMap.keySet();
		for ( String type:types)
			update_comboBox( _variableComboBoxMap.get( type), type, entityBase, number);
	}

	/**
	 * @param comboBox
	 * @param type
	 * @param entityBase
	 * @param number
	 */
	protected void update_comboBox(ComboBox comboBox, String type, EntityBase entityBase, String number) {
		if ( null == entityBase)
			comboBox.removeAllItems();
		else {
			if ( type.equals( "integer number object"))
				CommonTool.update( comboBox, entityBase.get_number_object_names( "integer", number, contains_empty( type)));
			else if ( type.equals( "real number object"))
				CommonTool.update( comboBox, entityBase.get_number_object_names( "real number", number, contains_empty( type)));
			else
				CommonTool.update( comboBox, entityBase.get_object_names( type, number, contains_empty( type)));
		}
	}

	/**
	 * @param radioButtons 
	 */
	protected void adjust(List<RadioButton> radioButtons) {
		if ( 1 < radioButtons.size()) {
			int width = 0;
			for ( RadioButton radioButton:radioButtons)
				width = Math.max( width, radioButton.getPreferredSize().width);

			for ( RadioButton radioButton:radioButtons)
				radioButton.setPreferredSize( new Dimension( width, radioButton.getPreferredSize().height));
		}

		if ( 1 < _entityVariableCheckBoxes.size()) {
			int width = 0;
			for ( CheckBox checkBox:_entityVariableCheckBoxes)
				width = Math.max( width, checkBox.getPreferredSize().width);

			for ( CheckBox checkBox:_entityVariableCheckBoxes)
				checkBox.setPreferredSize( new Dimension( width, checkBox.getPreferredSize().height));
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#initialize()
	 */
	@Override
	public void initialize() {
		if ( _radioButtons.isEmpty()) {
			// RadioButtonが無い場合があるので要注意！
			if ( null != _entity) {
				if ( _entity.equals( "agent"))
					on_agentSelector_selected();
				else if ( _entity.equals( "self"))
					on_self_selected();
				else if ( _entity.equals( "spot"))
					on_spotSelector_selected();
				else if ( _entity.equals( "currentspot"))
					on_currentSpot_selected();
			}
		} else {
			for (RadioButton radioButton:_radioButtons) {
				radioButton.setSelected( true);
				radioButton.setSelected( false);
			}
			_radioButtons.get( 0).setSelected( true);
		}

		for ( CheckBox checkBox:_entityVariableCheckBoxes) {
			checkBox.setSelected( true);
			checkBox.setSelected( false);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#get_max_width(int)
	 */
	@Override
	public int get_max_width(int width) {
		if ( null != _label)
			width = Math.max( width, _label.getPreferredSize().width);
		for ( RadioButton radioButton:_radioButtons)
			width = Math.max( width, radioButton.getPreferredSize().width);
		return width;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#set_max_width(int)
	 */
	@Override
	public void set_max_width(int width) {
		if ( null != _label)
			_label.setPreferredSize( new Dimension( width, _label.getPreferredSize().height));
		for ( RadioButton radioButton:_radioButtons)
			radioButton.setPreferredSize( new Dimension( width, radioButton.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#update_variable_panel()
	 */
	@Override
	public void update_variable_panel() {
		if ( null == _variableTypeComboBox)
			return;

		String selectedItem = ( String)_variableTypeComboBox.getSelectedItem();
		if ( null == selectedItem)
			return;

		Vector<Variable> variables = get_variables();
		if ( null == variables)
			return;

		Collection<JPanel> panels = _variablePanelMap.values();
		for ( JPanel panel:panels)
			panel.setVisible( false);

		for ( Variable variable:variables) {
			if ( !variable._name.equals( selectedItem))
				continue;

			JPanel panel = _variablePanelMap.get( variable._type);
			if ( null == panel)
				continue;

			panel.setVisible( true);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#set(soars.application.visualshell.object.role.base.object.generic.element.IObject, boolean)
	 */
	@Override
	public boolean set(IObject object, boolean check) {
		if ( !( object instanceof EntityVariableRule))
			return false;

		EntityVariableRule entityVariableRule = ( EntityVariableRule)object;
		if ( _radioButtons.isEmpty()) {
			if ( entityVariableRule._entity.equals( EntityVariableRule._null)) {
				if ( null != _entity)
					return false;
			} else {
				if ( !entityVariableRule._entity.equals( _entity))
					return false;
			}
		} else {
			if ( !_radioButtons.isEmpty()) {
				RadioButton radioButton = _entityRadioButtonMap.get( entityVariableRule._entity);
				if ( null == radioButton)
					return false;

				for (RadioButton rb:_radioButtons) {
					if ( !rb._label.getText().equals( radioButton._label.getText()))
						continue;

					rb.setSelected( true);
					break;
				}
			}
		}

		if ( entityVariableRule._entity.equals( "agent")) {
			if ( null == _agentSelector || ( check && _agentSelector.is_empty()) || ( check && entityVariableRule._entityName.equals( "")))
				return false;

			if ( null == LayerManager.get_instance().get_agent_has_this_name( entityVariableRule._entityName))
				return false;

			_agentSelector.set( entityVariableRule._entityName);
		} else if ( entityVariableRule._entity.equals( "spot")) {
			if ( null == _spotSelector || ( check && _spotSelector.is_empty()) || ( check && entityVariableRule._entityName.equals( "")))
				return false;

			if ( null == LayerManager.get_instance().get_spot_has_this_name( entityVariableRule._entityName))
				return false;

			_spotSelector.set( entityVariableRule._entityName);
		}

		if ( !check && null == entityVariableRule._agentVariable) {
			if ( null != _agentVariableCheckBox)
				_agentVariableCheckBox.setSelected( true);
		} else {
			if ( entityVariableRule._agentVariable.equals( "")) {
				if ( null != _agentVariableCheckBox)
					_agentVariableCheckBox.setSelected( false);
			} else {
				if ( null == _agentVariableCheckBox)
					return false;

				// コンボボックスにentityVariableRule._agentVariableが含まれているか？をチェック
				_agentVariableCheckBox.setSelected( true);
				if ( null != entityVariableRule._agentVariable && SwingTool.contains( _agentVariableComboBox, entityVariableRule._agentVariable))
					_agentVariableComboBox.setSelectedItem( entityVariableRule._agentVariable);
			}
		}

		if ( !check && null == entityVariableRule._spotVariable) {
			if ( null != _spotVariableCheckBox)
				_spotVariableCheckBox.setSelected( true);
		} else {
			if ( entityVariableRule._spotVariable.equals( "")) {
				if ( null != _spotVariableCheckBox)
					_spotVariableCheckBox.setSelected( false);
			} else {
				if ( null == _spotVariableCheckBox)
					return false;

				// コンボボックスにentityVariableRule._spotVariableが含まれているか？をチェック
				_spotVariableCheckBox.setSelected( true);
				if ( null != entityVariableRule._spotVariable && SwingTool.contains( _spotVariableComboBox, entityVariableRule._spotVariable))
					_spotVariableComboBox.setSelectedItem( entityVariableRule._spotVariable);
			}
		}

		if ( entityVariableRule._variableType.equals( "")) {
			if ( null != _variableTypeComboBox)
				return false;

			return true;
		} else {
			if ( null == _variableTypeComboBox)
				return false;

			Vector<Variable> variables = get_variables();
			if ( null == variables)
				return false;

			for ( Variable variable:variables) {
				if ( !variable._type.equals( entityVariableRule._variableType))
					continue;

				_variableTypeComboBox.setSelectedItem( variable._name);

				JComponent component = _variableComponentMap.get( variable._type);
				if ( null == component)
					return false;

				if ( check && !variable._empty && entityVariableRule._variableValue.equals( ""))
					return false;

				if ( component instanceof ComboBox) {
					ComboBox comboBox = ( ComboBox)component;
					// コンボボックスにentityVariableRule._variableValueが含まれているか？をチェック
					if ( null != entityVariableRule._variableValue && SwingTool.contains( comboBox, entityVariableRule._variableValue));
						comboBox.setSelectedItem( entityVariableRule._variableValue);
				} else if ( component instanceof TextField) {
					TextField textField = ( TextField)component;
					textField.setText( entityVariableRule._variableValue);
				} else if ( component instanceof ObjectSelector) {
					ObjectSelector objectSelector = ( ObjectSelector)component;
					// TODO ObjectSelectorにentityVariableRule._variableValueが含まれているか？をチェック(未実装)
					objectSelector.set( entityVariableRule._variableValue);
				} else if ( component instanceof ImmediateTimeVariablePanel) {
					ImmediateTimeVariablePanel immediateTimeVariablePanel = ( ImmediateTimeVariablePanel)component;
					immediateTimeVariablePanel.set( entityVariableRule._variableValue);
				} else
					return false;

				return true;
			}
		}

		return false;
	}

	/**
	 * @param entityVariableRule
	 * @param check
	 * @return
	 */
	public boolean get(EntityVariableRule entityVariableRule, boolean check) {
		if ( entityVariableRule._entity.equals( "agent")) {
			if ( null == _agentSelector || ( check && _agentSelector.is_empty()))
				return false;

			String entityName = _agentSelector.get();
			if ( null == entityName || ( check && entityName.equals( "")))
				return false;

			entityVariableRule._entityName = entityName;
		} else if ( entityVariableRule._entity.equals( "spot")) {
			if ( null == _spotSelector || ( check && _spotSelector.is_empty()))
				return false;

			String entityName = _spotSelector.get();
			if ( null == entityName || ( check && entityName.equals( "")))
				return false;

			entityVariableRule._entityName = entityName;
		} else
			entityVariableRule._entityName = "";

		if ( null != _agentVariableCheckBox && _agentVariableCheckBox.isSelected()) {
			String agentVariable = ( String)_agentVariableComboBox.getSelectedItem();
			if ( check) {
				if ( null == agentVariable || agentVariable.equals( ""))
					return false;
			}
			entityVariableRule._agentVariable = agentVariable;
		}

		if ( null != _spotVariableCheckBox && _spotVariableCheckBox.isSelected()) {
			String spotVariable = ( String)_spotVariableComboBox.getSelectedItem();
			if ( check) {
				if ( null == spotVariable || spotVariable.equals( ""))
					return false;
			}
			entityVariableRule._spotVariable = spotVariable;
		}

		if ( null == _variableTypeComboBox)
			return true;

		String selectedItem = ( String)_variableTypeComboBox.getSelectedItem();
		if ( null == selectedItem || ( check && selectedItem.equals( "")))
			return false;

		Vector<Variable> variables = get_variables();
		if ( null == variables)
			return false;

		for ( Variable variable:variables) {
			if ( !variable._name.equals( selectedItem))
				continue;

			entityVariableRule._variableType = variable._type;

			JComponent component = _variableComponentMap.get( variable._type);
			if ( null == component)
				return false;

			String value = null;
			if ( component instanceof ComboBox) {
				ComboBox comboBox = ( ComboBox)component;
				value = ( String)comboBox.getSelectedItem();
				if ( check) {
					if ( null == value || ( !variable._empty && value.equals( "")))
						return false;
				}
			} else if ( component instanceof TextField) {
				TextField textField = ( TextField)component;
				value = textField.getText();
				if ( null == value)
					return false;

				if ( check) {
					if ( null == value || ( !variable._empty && value.equals( "")))
						return false;

					value = is_correct( variable._type, value);
					if ( null == value)
						return false;

					textField.setText( value);
				}
			} else if ( component instanceof ObjectSelector) {
				ObjectSelector objectSelector = ( ObjectSelector)component;
				value = objectSelector.get();
				if ( null == value || ( check && !variable._empty && value.equals( "")))
					return false;

			} else if ( component instanceof ImmediateTimeVariablePanel) {
				ImmediateTimeVariablePanel immediateTimeVariablePanel = ( ImmediateTimeVariablePanel)component;
				value = immediateTimeVariablePanel.get();
				if ( null == value)
					return false;

			} else
				return false;

			entityVariableRule._variableValue = value;
			return true;
		}

		return false;
	}

	/**
	 * @param type
	 * @param value
	 * @return
	 */
	private String is_correct(String type, String value) {
		if ( type.equals( "immediate probability"))
			return Constant.is_correct_probability_initial_value( value);
		else if ( type.equals( "immediate keyword"))
			return Constant.is_correct_keyword_initial_value( value) ? value : null;
		else if ( type.equals( "immediate integer"))
			return Constant.is_correct_number_variable_initial_value( "integer", value);
		else if ( type.equals( "immediate real number"))
			return Constant.is_correct_number_variable_initial_value( "real number", value);
		else if ( type.equals( "immediate data"))
			return value;
		else
			return value;
	}

	/**
	 * @param type
	 * @return
	 */
	protected boolean contains_empty(String type) {
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#synchronize(soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot)
	 */
	@Override
	public void synchronize(PanelRoot panelRoot) {
		if ( !( panelRoot instanceof PanelBase))
			return;

		if ( !same_as( panelRoot))
			return;

		set( panelRoot.get( false), false);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.undo.UndoManager)
	 */
	@Override
	public void on_changed(UndoManager undoManager) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.text.AbstractDocument.DefaultDocumentEvent, javax.swing.undo.UndoManager)
	 */
	@Override
	public void on_changed(DefaultDocumentEvent defaultDocumentEvent, UndoManager undoManager) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#update_stage_name(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_stage_name(String newName, String originalName) {
		ComboBox comboBox = ( ComboBox)_variableComponentMap.get( "stage");
		if ( null == comboBox)
			return false;

		String selectedItem = ( String)comboBox.getSelectedItem();
		if ( null != selectedItem && selectedItem.equals( originalName))
			selectedItem = newName;

		CommonTool.update( comboBox, StageManager.get_instance().get_names( contains_empty( "stage")));

		comboBox.setSelectedItem( selectedItem);

		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#on_update_stage()
	 */
	@Override
	public void on_update_stage() {
		ComboBox comboBox = ( ComboBox)_variableComponentMap.get( "stage");
		if ( null == comboBox)
			return;

		String selectedItem = ( String)comboBox.getSelectedItem();
		CommonTool.update( comboBox, StageManager.get_instance().get_names(  contains_empty( "stage")));
		if ( null != selectedItem)
			comboBox.setSelectedItem( selectedItem);
	}

	/**
	 * @param entities
	 * @param entityVariables
	 * @param variables
	 */
	private void print(List<String> entities, List<String> entityVariables, List<Variable> variables) {
		String text = "Entities :";
		for ( String entity:entities)
			text += ( " " + entity);
		System.out.println( text);

		text = "Entity variables : ";
		for ( String entityVariable:entityVariables)
			text += ( " " + entityVariable);
		System.out.println( text);

		text = "Variables : ";
		for ( Variable variable:variables)
			text += ( " " + variable._type + ", " + variable._name);
		System.out.println( text);
	}
}
