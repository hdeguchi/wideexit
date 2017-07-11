/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.set_and_list.base.panel.initial;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.JPanel;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.set_and_list.base.panel.initial.base.VariableInitialValuePanel;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.set_and_list.base.panel.table.VariableInitialValueTable;
import soars.application.visualshell.object.entity.base.object.variable.VariableInitialValue;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;

/**
 * @author kurata
 *
 */
public class VariableInitialValueObjectPanel extends VariableInitialValuePanel {

	/**
	 * 
	 */
	private ComboBox _comboBox = null;

	/**
	 * @param color
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 * @param typeComboBox
	 * @param variableInitialValueTable
	 */
	public VariableInitialValueObjectPanel(Color color, EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, ComboBox typeComboBox, VariableInitialValueTable variableInitialValueTable) {
		super(color, entityBase, propertyPanelBaseMap, typeComboBox, variableInitialValueTable);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.initial.base.VariableInitialValuePanel#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		_comboBox.setEnabled( enabled);
		super.setEnabled(enabled);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.initial.base.VariableInitialValuePanel#create(javax.swing.JPanel)
	 */
	@Override
	protected void create(JPanel parent) {
		_comboBox = new ComboBox( _color, false, new CommonComboBoxRenderer( _color, false));
		parent.add( _comboBox);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.initial.base.VariableInitialValuePanel#update(java.lang.String)
	 */
	@Override
	public void update(String item) {
		String type = InitialValueTableBase.__typeMap.get( item);
		if ( null == type)
			return;

		if ( type.equals( "collection") || type.equals( "list") || type.equals( "map")
			|| ( type.equals( "exchange algebra") && Environment.get_instance().is_exchange_algebra_enable()))
			CommonTool.update( _comboBox, ( String[])_propertyPanelBaseMap.get( "variable").get( type));
		else if ( type.equals( "class variable"))
			CommonTool.update( _comboBox, _propertyPanelBaseMap.get( "class variable").get());
		else if ( type.equals( "file"))
			CommonTool.update( _comboBox, _propertyPanelBaseMap.get( "file").get());
		else {
			if ( type.equals( "role variable") && !( _entityBase instanceof AgentObject))
				return;

			CommonTool.update( _comboBox, _propertyPanelBaseMap.get( "simple variable").get( type));
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.initial.base.VariableInitialValuePanel#set(java.lang.String)
	 */
	@Override
	public void set(String value) {
		_comboBox.setSelectedItem( value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.initial.base.VariableInitialValuePanel#on_append(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_append(ActionEvent actionEvent) {
		String item = ( String)_typeComboBox.getSelectedItem();
		if ( null == item)
			return;

		String type = VariableInitialValueTable.__typeMap.get( item);
		if ( null == type)
			return;

		String value = ( String)_comboBox.getSelectedItem();
		if ( null == value)
			return;

		VariableInitialValue variableInitialValue = new VariableInitialValue( type, value);
		_initialValueTableBase.append( new VariableInitialValue[] { variableInitialValue, variableInitialValue});
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.initial.base.VariableInitialValuePanel#on_insert(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_insert(ActionEvent actionEvent) {
		if ( 1 != _initialValueTableBase.getSelectedRowCount())
			return;

		String item = ( String)_typeComboBox.getSelectedItem();
		if ( null == item)
			return;

		String type = VariableInitialValueTable.__typeMap.get( item);
		if ( null == type)
			return;

		String value = ( String)_comboBox.getSelectedItem();
		if ( null == value)
			return;

		VariableInitialValue variableInitialValue = new VariableInitialValue( type, value);
		_initialValueTableBase.insert( new VariableInitialValue[] { variableInitialValue, variableInitialValue});
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.initial.base.VariableInitialValuePanel#on_update(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_update(ActionEvent actionEvent) {
		if ( 1 != _initialValueTableBase.getSelectedRowCount())
			return;

		String item = ( String)_typeComboBox.getSelectedItem();
		if ( null == item)
			return;

		String type = VariableInitialValueTable.__typeMap.get( item);
		if ( null == type)
			return;

		String value = ( String)_comboBox.getSelectedItem();
		if ( null == value)
			return;

		VariableInitialValue variableInitialValue = new VariableInitialValue( type, value);
		_initialValueTableBase.update( new VariableInitialValue[] { variableInitialValue, variableInitialValue});
	}
}
