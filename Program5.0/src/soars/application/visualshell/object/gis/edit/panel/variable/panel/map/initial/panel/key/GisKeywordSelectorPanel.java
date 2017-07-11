/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.variable.panel.map.initial.panel.key;

import java.awt.Color;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.table.GisVariableInitialValueTable;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;

/**
 * @author kurata
 *
 */
public class GisKeywordSelectorPanel extends JPanel {

	/**
	 * 
	 */
	public ComboBox _comboBox = null;

	/**
	 * 
	 */
	public GisKeywordSelectorPanel() {
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
	 * @param gisPropertyPanelBaseMap
	 */
	public void update(String item, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap) {
		String type = GisVariableInitialValueTable.get_typeMap().get( item);
		if ( null == type)
			return;

		if ( type.equals( "keyword"))
			CommonTool.update( _comboBox, gisPropertyPanelBaseMap.get( "simple variable").get( type));
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
