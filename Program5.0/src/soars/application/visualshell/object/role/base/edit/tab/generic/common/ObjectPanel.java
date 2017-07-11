/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.common;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot;
import soars.application.visualshell.object.role.base.edit.tab.generic.base.GenericPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.base.PanelBase;
import soars.application.visualshell.object.role.base.edit.tab.generic.base.VerbPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Subject;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Variable;
import soars.application.visualshell.object.role.base.object.generic.element.EntityVariableRule;
import soars.application.visualshell.object.role.base.object.generic.element.IObject;

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
	 * @param verbPanel
	 * @param property
	 * @param role
	 * @param buddiesMap 
	 * @param genericPropertyPanel
	 */
	public ObjectPanel(Subject object, VerbPanel verbPanel, Property property, Role role, Map<String, List<PanelRoot>> buddiesMap, GenericPropertyPanel genericPropertyPanel) {
		super(verbPanel, property, role, buddiesMap, genericPropertyPanel);
		_object = object;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.generic.base.PanelBase#setup()
	 */
	@Override
	public boolean setup() {
		if ( !super.setup())
			return false;

		return setup_buddiesMap( _object);
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
				CommonTool.update( _agentVariableComboBox, GenericPropertyPanel.get_agent_agent_variable_names( false));
			else if ( entityVariable.equals( "spotvariable"))
				CommonTool.update( _agentVariableComboBox, GenericPropertyPanel.get_spot_agent_variable_names( false));
		}

		if ( null != _spotVariableCheckBox) {
			if ( entityVariable.equals( "agentvariable"))
				CommonTool.update( _spotVariableComboBox, GenericPropertyPanel.get_agent_spot_variable_names( false));
			else if ( entityVariable.equals( "spotvariable"))
				CommonTool.update( _spotVariableComboBox, GenericPropertyPanel.get_spot_spot_variable_names( false));
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

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#update(java.lang.String)
	 */
	@Override
	public void update(String entity) {
		if ( null == entity) {
			// ObjectPanelの内部からしか呼ばれない筈！
			// entityが無く、_agentVariableCheckBoxまたは_spotVariableCheckBoxが押された時だけ呼ばれる
			SubjectPanel subjectPanel = _verbPanel.get_current_subjectPanel();
			if ( null == subjectPanel)
				return;

			subjectPanel.update_objectPanel( this);

			return;
		}

		super.update(entity);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#contains_empty(java.lang.String)
	 */
	@Override
	protected boolean contains_empty(String type) {
		return _object.contains_empty(type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#synchronize()
	 */
	@Override
	public void synchronize() {
		synchronize( _object);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#same_as(soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot)
	 */
	@Override
	protected boolean same_as(PanelRoot panelRoot) {
		if ( !( panelRoot instanceof ObjectPanel))
			return false;

		ObjectPanel objectPanel = ( ObjectPanel)panelRoot;
		return _object.same_as( objectPanel._object);
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
