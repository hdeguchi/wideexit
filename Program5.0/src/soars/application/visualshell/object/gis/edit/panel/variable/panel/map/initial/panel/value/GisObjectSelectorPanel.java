/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.variable.panel.map.initial.panel.value;

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
public class GisObjectSelectorPanel extends JPanel {

	/**
	 * 
	 */
	public ComboBox _comboBox = null;

	/**
	 * 
	 */
	public GisObjectSelectorPanel() {
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

		if ( type.equals( "collection") || type.equals( "list") || type.equals( "map"))
			CommonTool.update( _comboBox, gisPropertyPanelBaseMap.get( "variable").get( type));
		else
			CommonTool.update( _comboBox, gisPropertyPanelBaseMap.get( "simple variable").get( type));
//		if ( type.equals( "collection") || type.equals( "list") || type.equals( "map")
//			|| ( type.equals( "exchange algebra") && Environment.get_instance().is_exchange_algebra_enable()))
//			CommonTool.update( _comboBox, gisPropertyPanelBaseMap.get( "variable").get( type));
//		else if ( type.equals( "class variable"))
//			CommonTool.update( _comboBox, gisPropertyPanelBaseMap.get( "class variable").get());
//		else if ( type.equals( "file"))
//			CommonTool.update( _comboBox, gisPropertyPanelBaseMap.get( "file").get());
//		else {
//			if ( type.equals( "role variable") && !( entityBase instanceof AgentObject))
//				return;
//
//			CommonTool.update( _comboBox, gisPropertyPanelBaseMap.get( "simple variable").get( type));
//		}
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
