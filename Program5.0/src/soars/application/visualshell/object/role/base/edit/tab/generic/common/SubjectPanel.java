/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.common;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot;
import soars.application.visualshell.object.role.base.edit.tab.generic.base.GenericPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.base.PanelBase;
import soars.application.visualshell.object.role.base.edit.tab.generic.base.VerbPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Subject;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Variable;
import soars.application.visualshell.object.role.base.object.generic.element.EntityVariableRule;

/**
 * @author kurata
 *
 */
public class SubjectPanel extends PanelBase {

	/**
	 * 
	 */
	public Subject _subject = null;

	/**
	 * @param subject
	 * @param verbPanel
	 * @param property
	 * @param role
	 * @param buddiesMap
	 * @param genericPropertyPanel
	 */
	public SubjectPanel(Subject subject, VerbPanel verbPanel, Property property, Role role, Map<String, List<PanelRoot>> buddiesMap, GenericPropertyPanel genericPropertyPanel) {
		super(verbPanel, property, role, buddiesMap, genericPropertyPanel);
		_subject = subject;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.generic.base.PanelBase#setup()
	 */
	@Override
	public boolean setup() {
		if (!super.setup())
			return false;

		return setup_buddiesMap( _subject);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#get_all_components(java.util.List, java.util.List, java.util.List)
	 */
	@Override
	protected void get_all_components(List<String> entities, List<String> entityVariables, List<Variable> variables) {
		_subject.get_all_components( _role, entities, entityVariables, variables);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#get_variables()
	 */
	@Override
	protected Vector<Variable> get_variables() {
		return _subject;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#on_agent_variable_selected(java.lang.String)
	 */
	@Override
	public void on_agent_variable_selected(String entityVariable) {
		super.on_agent_variable_selected(entityVariable);

		Vector<ObjectPanel> objectPanels = _verbPanel.get_objectPanels();
		if ( null == objectPanels)
			return;

		String entity = ( null != _entity) ? _entity : _radioButtonEntityMap.get( get_selected_radioButton());

		for ( ObjectPanel objectPanel:objectPanels) {
			if ( objectPanel.has_entities())
				continue;

			on_entity_variable_selected( entity, "agentvariable", entityVariable, objectPanel);
//			objectPanel.on_agent_variable_selected(entityVariable);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#on_agent_selected()
	 */
	@Override
	public void on_agent_selected() {
		super.on_agent_selected();

		Vector<ObjectPanel> objectPanels = _verbPanel.get_objectPanels();
		if ( null == objectPanels)
			return;

		String entity = ( null != _entity) ? _entity : _radioButtonEntityMap.get( get_selected_radioButton());

		String entityVariable = "";
		String entityVariableValue = null;
		if ( null != _agentVariableCheckBox && _agentVariableCheckBox.isSelected()) {
			entityVariable = "agentvariable";
			entityVariableValue = ( String)_agentVariableComboBox.getSelectedItem();
		}

		if ( null != _spotVariableCheckBox && _spotVariableCheckBox.isSelected()) {
			entityVariable = "spotvariable";
			entityVariableValue = ( String)_spotVariableComboBox.getSelectedItem();
		}

		for ( ObjectPanel objectPanel:objectPanels) {
			if ( objectPanel.has_entities())
				continue;

			objectPanel.update( entity, entityVariable, entityVariableValue);
//			objectPanel.on_agent_selected();
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#on_spot_variable_selected(java.lang.String)
	 */
	@Override
	public void on_spot_variable_selected(String entityVariable) {
		super.on_spot_variable_selected(entityVariable);

		Vector<ObjectPanel> objectPanels = _verbPanel.get_objectPanels();
		if ( null == objectPanels)
			return;

		String entity = ( null != _entity) ? _entity : _radioButtonEntityMap.get( get_selected_radioButton());

		for ( ObjectPanel objectPanel:objectPanels) {
			if ( objectPanel.has_entities())
				continue;

			on_entity_variable_selected( entity, "spotvariable", entityVariable, objectPanel);
//			objectPanel.on_spot_variable_selected(entityVariable);
		}
	}

	/**
	 * @param entity
	 * @param entityVariable
	 * @param entityVariableValue
	 * @param objectPanel
	 */
	private void on_entity_variable_selected(String entity, String entityVariable, String entityVariableValue, ObjectPanel objectPanel) {
		if ( entity.equals( "agent")) {
			AgentObject agentObject = LayerManager.get_instance().get_agent( _agentSelector.get_name());
			objectPanel.on_objectSelector_selected( agentObject, _agentSelector.get_number(), entity, entityVariable, entityVariableValue);
		} else if ( entity.equals( "spot")) {
			SpotObject spotObject = LayerManager.get_instance().get_spot( _spotSelector.get_name());
			objectPanel.on_objectSelector_selected( spotObject, _spotSelector.get_number(), entity, entityVariable, entityVariableValue);
		} else
			objectPanel.update( entity, entityVariable, entityVariableValue);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#on_spot_selected()
	 */
	@Override
	public void on_spot_selected() {
		super.on_spot_selected();

		Vector<ObjectPanel> objectPanels = _verbPanel.get_objectPanels();
		if ( null == objectPanels)
			return;

		String entity = ( null != _entity) ? _entity : _radioButtonEntityMap.get( get_selected_radioButton());

		String entityVariable = "";
		String entityVariableValue = null;
		if ( null != _agentVariableCheckBox && _agentVariableCheckBox.isSelected()) {
			entityVariable = "agentvariable";
			entityVariableValue = ( String)_agentVariableComboBox.getSelectedItem();
		}

		if ( null != _spotVariableCheckBox && _spotVariableCheckBox.isSelected()) {
			entityVariable = "spotvariable";
			entityVariableValue = ( String)_spotVariableComboBox.getSelectedItem();
		}

		for ( ObjectPanel objectPanel:objectPanels) {
			if ( objectPanel.has_entities())
				continue;

			objectPanel.update( entity, entityVariable, entityVariableValue);
//			objectPanel.on_spot_selected();
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#on_entity_selected(soars.application.visualshell.object.entiy.base.EntityBase, java.lang.String)
	 */
	@Override
	public void on_entity_selected(EntityBase entityBase, String number) {
		super.on_entity_selected(entityBase, number);

		Vector<ObjectPanel> objectPanels = _verbPanel.get_objectPanels();
		if ( null == objectPanels)
			return;

		String entity = ( null != _entity) ? _entity : _radioButtonEntityMap.get( get_selected_radioButton());

		String entityVariable = "";
		String entityVariableValue = null;
		if ( null != _agentVariableCheckBox && _agentVariableCheckBox.isSelected()) {
			entityVariable = "agentvariable";
			entityVariableValue = ( String)_agentVariableComboBox.getSelectedItem();
		}

		if ( null != _spotVariableCheckBox && _spotVariableCheckBox.isSelected()) {
			entityVariable = "spotvariable";
			entityVariableValue = ( String)_spotVariableComboBox.getSelectedItem();
		}

		for ( ObjectPanel objectPanel:objectPanels) {
			if ( objectPanel.has_entities())
				continue;

			objectPanel.on_objectSelector_selected( entityBase, number, entity, entityVariable, entityVariableValue);
//			objectPanel.on_entity_selected(entityBase, number);
		}
	}

	/**
	 * @param objectPanel
	 */
	public void update_objectPanel(ObjectPanel objectPanel) {
		String entity = ( null != _entity) ? _entity : _radioButtonEntityMap.get( get_selected_radioButton());

		if ( null != _agentVariableCheckBox && _agentVariableCheckBox.isSelected()) {
			on_entity_variable_selected( entity, "agentvariable", ( String)_agentVariableComboBox.getSelectedItem(), objectPanel);
			return;
		}

		if ( null != _spotVariableCheckBox && _spotVariableCheckBox.isSelected()) {
			on_entity_variable_selected( entity, "spotvariable", ( String)_spotVariableComboBox.getSelectedItem(), objectPanel);
			return;
		}

		if ( entity.equals( "self") || entity.equals( "currentspot"))
			objectPanel.update( entity);
		else if ( entity.equals( "agent")) {
			AgentObject agentObject = LayerManager.get_instance().get_agent( _agentSelector.get_name());
			objectPanel.on_objectSelector_selected( agentObject, _agentSelector.get_number());
		} else if ( entity.equals( "spot")) {
			SpotObject spotObject = LayerManager.get_instance().get_spot( _spotSelector.get_name());
			objectPanel.on_objectSelector_selected( spotObject, _spotSelector.get_number());
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#contains_empty(java.lang.String)
	 */
	@Override
	protected boolean contains_empty(String type) {
		return _subject.contains_empty(type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#synchronize()
	 */
	@Override
	public void synchronize() {
		synchronize( _subject);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#same_as(soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot)
	 */
	@Override
	protected boolean same_as(PanelRoot panelRoot) {
		if ( !( panelRoot instanceof SubjectPanel))
			return false;

		SubjectPanel subjectPanel = ( SubjectPanel)panelRoot;
		return _subject.same_as( subjectPanel._subject);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.generic.base.PanelBase#get(soars.application.visualshell.object.role.base.object.generic.element.EntityVariableRule, boolean)
	 */
	@Override
	public boolean get(EntityVariableRule entityVariableRule, boolean check) {
		if ( null != _entity)
			entityVariableRule._entity = _entity;
		else {
			if ( _radioButtons.isEmpty())
				return false;

			entityVariableRule._entity = _radioButtonEntityMap.get( get_selected_radioButton());
		}
		return super.get(entityVariableRule, check);
	}
}
