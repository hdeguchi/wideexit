/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.common.expression;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.edit.tab.generic.base.PanelBase;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Subject;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Variable;
import soars.application.visualshell.object.role.base.object.generic.element.EntityVariableRule;
import soars.application.visualshell.object.role.base.object.generic.element.IObject;
import soars.application.visualshell.object.role.spot.SpotRole;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class ObjectPanel extends PanelBase {

	/**
	 * 
	 */
	private Subject _object = null;

	/**
	 * @param object
	 * @param property
	 * @param role
	 * @param rulePropertyPanelBase
	 */
	public ObjectPanel(Subject object, Property property, Role role, RulePropertyPanelBase rulePropertyPanelBase) {
		super(null, property, role, null, rulePropertyPanelBase);
		_object = object;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.generic.base.PanelBase#setup()
	 */
	@Override
	public boolean setup() {
		// 親クラスのsetup()を呼んではいけない
		List<String> entities = new ArrayList<String>();
		List<String> entityVariables = new ArrayList<String>();
		List<Variable> variables = new ArrayList<Variable>();
		get_all_components( entities, entityVariables, variables);
		if ( entities.isEmpty()) {
//			if ( this instanceof SubjectPanel) {
//				if ( _role instanceof AgentRole)
//					_entity = "self";
//				else if ( _role instanceof SpotRole)
//					_entity = "currentspot";
//				else
//					return false;
//
//				if ( entityVariables.isEmpty() && variables.isEmpty()) {
//					_visible = false;
//					return true;
//				}
//			} else if ( this instanceof ObjectPanel) {
				if ( entityVariables.isEmpty() && variables.isEmpty())
					return false;
//			}
		}

		if ( 1 == entities.size()) {
			if ( _role instanceof AgentRole && entities.get( 0).equals( "self")) {
				_entity = "self";
//				if ( this instanceof SubjectPanel) {
//					entities.clear();
//					if ( entityVariables.isEmpty() && variables.isEmpty()) {
//						_visible = false;
//						return true;
//					}
////				} else if ( this instanceof ObjectPanel) {
////					if ( entityVariables.isEmpty() && variables.isEmpty())
////						return false;
//				}
			}

			if ( _role instanceof SpotRole && entities.get( 0).equals( "currentspot")) {
				_entity = "currentspot";
//				if ( this instanceof SubjectPanel) {
//					entities.clear();
//					if ( entityVariables.isEmpty() && variables.isEmpty()) {
//						_visible = false;
//						return true;
//					}
////				} else if ( this instanceof ObjectPanel) {
////					if ( entityVariables.isEmpty() && variables.isEmpty())
////						return false;
//				}
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

//		basePanel.setBorder( BorderFactory.createLineBorder( this instanceof ObjectPanel ? Color.blue : getForeground(), 1));
		basePanel.setBorder( BorderFactory.createLineBorder( Color.blue));

		add( basePanel);

		add( Box.createHorizontalStrut( 2));

		adjust( radioButtons);

		//print( entities, entityVariables, variables);

		initialize();

		update_variable_panel();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#get_all_components(java.util.List, java.util.List, java.util.List)
	 */
	@Override
	protected void get_all_components(List<String> entities, List<String> entityVariables, List<Variable> variables) {
		_object.get_all_components( _role, entities, entityVariables, variables);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#get_variables()
	 */
	@Override
	protected Vector<Variable> get_variables() {
		return _object;
	}

	/**
	 * @return
	 */
	public boolean has_entities() {
		return ( !_radioButtons.isEmpty() || ( null != _entity));
	}

	/**
	 * @param entityBase
	 * @param number
	 * @param entity
	 * @param entityVariable
	 * @param entityVariableValue
	 */
	public void on_objectSelector_selected(EntityBase entityBase, String number, String entity, String entityVariable, String entityVariableValue) {
		if ( entityVariable.equals( ""))
			super.on_objectSelector_selected(entityBase, number);
		else
			on_subject_entityVariable_selected( entityVariable, entityVariableValue);
//			on_entity_selected( entityBase, number);
	}

	/**
	 * @param entity
	 * @param entityVariable
	 * @param entityVariableValue
	 */
	public void update(String entity, String entityVariable, String entityVariableValue) {
		if ( entityVariable.equals( ""))
			update( entity);
		else
			on_subject_entityVariable_selected( entityVariable, entityVariableValue);
	}

	/**
	 * @param entityVariable
	 * @param entityVariableValue
	 */
	private void on_subject_entityVariable_selected(String entityVariable, String entityVariableValue) {
		if ( null != _agentVariableCheckBox) {
			if ( entityVariable.equals( "agentvariable"))
				CommonTool.update( _agentVariableComboBox, RulePropertyPanelBase.get_agent_agent_variable_names( false));
			else if ( entityVariable.equals( "spotvariable"))
				CommonTool.update( _agentVariableComboBox, RulePropertyPanelBase.get_spot_agent_variable_names( false));
		}

		if ( null != _spotVariableCheckBox) {
			if ( entityVariable.equals( "agentvariable"))
				CommonTool.update( _spotVariableComboBox, RulePropertyPanelBase.get_agent_spot_variable_names( false));
			else if ( entityVariable.equals( "spotvariable"))
				CommonTool.update( _spotVariableComboBox, RulePropertyPanelBase.get_spot_spot_variable_names( false));
		}

		if ( null != _agentVariableCheckBox && _agentVariableCheckBox.isSelected()) {
			on_agent_variable_selected( ( String)_agentVariableComboBox.getSelectedItem());
			return;
		}

		if ( null != _spotVariableCheckBox && _spotVariableCheckBox.isSelected()) {
			on_spot_variable_selected( ( String)_spotVariableComboBox.getSelectedItem());
			return;
		}

		if ( entityVariable.equals( "agentvariable"))
			on_agent_variable_selected( entityVariableValue);
		else if ( entityVariable.equals( "spotvariable"))
			on_spot_variable_selected( entityVariableValue);
	}

//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#update(java.lang.String)
//	 */
//	@Override
//	public void update(String entity) {
//		if ( null == entity) {
//			// ObjectPanelの内部からしか呼ばれない筈！
//			// entityが無く、_agentVariableCheckBoxまたは_spotVariableCheckBoxが押された時だけ呼ばれる
//			SubjectPanel subjectPanel = _verbPanel.get_current_subjectPanel();
//			if ( null == subjectPanel)
//				return;
//
//			subjectPanel.update_objectPanel( this);
//
//			return;
//		}
//
//		super.update(entity);
//	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#contains_empty(java.lang.String)
	 */
	@Override
	protected boolean contains_empty(String type) {
		return _object.contains_empty(type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#get(boolean)
	 */
	@Override
	public IObject get(boolean check) {
		EntityVariableRule entityVariableRule = new EntityVariableRule();

		if ( null != _entity)
			entityVariableRule._entity = _entity;
		else {
			if ( _radioButtons.isEmpty())
				entityVariableRule._entity = EntityVariableRule._null;
			else
				entityVariableRule._entity = _radioButtonEntityMap.get( get_selected_radioButton());
		}

		return get( entityVariableRule, check) ? entityVariableRule : null;
	}
}
