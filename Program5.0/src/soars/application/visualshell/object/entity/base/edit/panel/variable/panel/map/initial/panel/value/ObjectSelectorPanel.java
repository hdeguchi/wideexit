/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.map.initial.panel.value;

import java.awt.Color;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.set_and_list.base.panel.table.VariableInitialValueTable;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;

/**
 * @author kurata
 *
 */
public class ObjectSelectorPanel extends JPanel {

	/**
	 * 
	 */
	public ComboBox _comboBox = null;

	/**
	 * 
	 */
	public ObjectSelectorPanel() {
		super();
	}

	/**
	 * @param color
	 */
	public void create(Color color) {
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));

		_comboBox = new ComboBox( color, false, new CommonComboBoxRenderer( color, false));
		add( _comboBox);

		add( Box.createHorizontalStrut( 5));
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		_comboBox.setEnabled( enabled);
		super.setEnabled(enabled);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean aFlag) {
		_comboBox.setVisible( aFlag);
		super.setVisible(aFlag);
	}

	/**
	 * @param item
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 */
	public void update(String item, EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap) {
		String type = VariableInitialValueTable.__typeMap.get( item);
		if ( null == type)
			return;

		if ( type.equals( "collection") || type.equals( "list") || type.equals( "map")
			|| ( type.equals( "exchange algebra") && Environment.get_instance().is_exchange_algebra_enable()))
			CommonTool.update( _comboBox, propertyPanelBaseMap.get( "variable").get( type));
		else if ( type.equals( "class variable"))
			CommonTool.update( _comboBox, propertyPanelBaseMap.get( "class variable").get());
		else if ( type.equals( "file"))
			CommonTool.update( _comboBox, propertyPanelBaseMap.get( "file").get());
		else {
			if ( type.equals( "role variable") && !( entityBase instanceof AgentObject))
				return;

			CommonTool.update( _comboBox, propertyPanelBaseMap.get( "simple variable").get( type));
		}
	}

	/**
	 * @param value
	 */
	public void set(String value) {
		_comboBox.setSelectedItem( value);
	}

	/**
	 * @return
	 */
	public String get() {
		Object object = _comboBox.getSelectedItem();
		if ( null == object)
			return null;

		return ( String)object;
	}
}
