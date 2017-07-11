/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.variable.panel.map.initial.panel.common;

import java.awt.Color;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.object.base.GisInitialValueBase;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;

/**
 * @author kurata
 *
 */
public class GisFieldTypeSelectorPanel extends JPanel {

	/**
	 * 
	 */
	protected ComboBox _fieldTypeComboBox = null;

	/**
	 * 
	 */
	public GisFieldTypeSelectorPanel() {
		super();
	}

	/**
	 * @param color
	 */
	public void create(Color color) {
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));

		_fieldTypeComboBox = new ComboBox( color, false, new CommonComboBoxRenderer( color, false));
		String[] kinds = new String[] {
			ResourceManager.get_instance().get( "edit.gis.data.initial.value.field.type.string"),
			ResourceManager.get_instance().get( "edit.gis.data.initial.value.field.type.integer"),
			ResourceManager.get_instance().get( "edit.gis.data.initial.value.field.type.real.number")
		};
		CommonTool.update( _fieldTypeComboBox, kinds);
		add( _fieldTypeComboBox);

		add( Box.createHorizontalStrut( 5));
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		_fieldTypeComboBox.setEnabled( enabled);
		super.setEnabled(enabled);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean aFlag) {
		_fieldTypeComboBox.setVisible( aFlag);
		super.setVisible(aFlag);
	}

	/**
	 * @return
	 */
	public String get() {
		return GisInitialValueBase.__typeMap.get( _fieldTypeComboBox.getSelectedItem());
	}

	/**
	 * @param type
	 */
	public void set(String type) {
		_fieldTypeComboBox.setSelectedItem( GisInitialValueBase.__nameMap.get( type));
	}
}
