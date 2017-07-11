/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.map.initial.panel.key;

import java.awt.Color;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.set_and_list.base.panel.table.VariableInitialValueTable;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;

/**
 * @author kurata
 *
 */
public class KeywordSelectorPanel extends JPanel {

	/**
	 * 
	 */
	public ComboBox _comboBox = null;

	/**
	 * 
	 */
	public KeywordSelectorPanel() {
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

		if ( type.equals( "keyword"))
			CommonTool.update( _comboBox, propertyPanelBaseMap.get( "simple variable").get( type));
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
