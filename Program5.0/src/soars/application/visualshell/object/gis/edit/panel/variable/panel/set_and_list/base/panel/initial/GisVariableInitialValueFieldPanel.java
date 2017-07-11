/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel;
import soars.application.visualshell.object.gis.object.base.GisInitialValueBase;
import soars.application.visualshell.object.gis.object.variable.GisVariableInitialValue;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;

/**
 * @author kurata
 *
 */
public class GisVariableInitialValueFieldPanel extends GisVariableInitialValuePanel {

	/**
	 * 
	 */
	protected ComboBox _fieldTypeComboBox = null;

	/**
	 * 
	 */
	protected ComboBox _fieldValueComboBox = null;

	/**
	 * @param color
	 * @param gisDataManager
	 * @param gisPropertyPanelBaseMap
	 * @param typeComboBox
	 * @param initialValueTableBase
	 */
	public GisVariableInitialValueFieldPanel(Color color, GisDataManager gisDataManager, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, ComboBox typeComboBox, GisInitialValueTableBase initialValueTableBase) {
		super(color, gisDataManager, gisPropertyPanelBaseMap, typeComboBox, initialValueTableBase);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		_fieldTypeComboBox.setEnabled( enabled);
		_fieldValueComboBox.setEnabled( enabled);
		super.setEnabled(enabled);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel#setup_components(javax.swing.JPanel)
	 */
	@Override
	protected void setup_components(JPanel parent) {
		seup_fieldTypeComboBox( parent);
		insert_vertical_strut( parent);
		setup_fieldValueComboBox( parent);
//		JPanel panel = new JPanel();
//		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));
//
//		panel.add( Box.createHorizontalStrut( 5));
//
//		_label = new JLabel( ResourceManager.get_instance().get( "edit.variable.dialog.initial.value"));
//		_label.setHorizontalAlignment( SwingConstants.RIGHT);
//		_label.setForeground( _color);
//		panel.add( _label);
//
//		panel.add( Box.createHorizontalStrut( 5));
//
//		create( panel);
//
//		panel.add( Box.createHorizontalStrut( 5));
//
//		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void seup_fieldTypeComboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.gis.data.initial.value.field.type"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_fieldTypeComboBox = new ComboBox( _color, false, new CommonComboBoxRenderer( _color, false));
		String[] kinds = new String[] {
			ResourceManager.get_instance().get( "edit.gis.data.initial.value.field.type.string"),
			ResourceManager.get_instance().get( "edit.gis.data.initial.value.field.type.integer"),
			ResourceManager.get_instance().get( "edit.gis.data.initial.value.field.type.real.number")
		};
		CommonTool.update( _fieldTypeComboBox, kinds);
		panel.add( _fieldTypeComboBox);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_fieldValueComboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.gis.data.initial.value.field"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_fieldValueComboBox = new ComboBox( _color, false, new CommonComboBoxRenderer( _color, false));
		String[] fields = _gisDataManager.get_fields( false);
		CommonTool.update( _fieldValueComboBox, fields);
		panel.add( _fieldValueComboBox);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel#create(javax.swing.JPanel)
//	 */
//	@Override
//	protected void create(JPanel parent) {
//		JPanel panel = new JPanel();
//		panel.setLayout( new GridLayout( 1, 2));
//
//		_fieldTypeComboBox = new ComboBox( _color, false, new CommonComboBoxRenderer( _color, false));
//		String[] kinds = new String[] {
//			ResourceManager.get_instance().get( "edit.gis.data.initial.value.field.type.string"),
//			ResourceManager.get_instance().get( "edit.gis.data.initial.value.field.type.integer"),
//			ResourceManager.get_instance().get( "edit.gis.data.initial.value.field.type.real.number")
//		};
//		CommonTool.update( _fieldTypeComboBox, kinds);
//		panel.add( _fieldTypeComboBox);
//
//		_fieldValueComboBox = new ComboBox( _color, false, new CommonComboBoxRenderer( _color, false));
//		String[] fields = _gisDataManager.get_fields( false);
//		CommonTool.update( _fieldValueComboBox, fields);
//		panel.add( _fieldValueComboBox);
//
//		parent.add( panel);
//	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel#set(soars.application.visualshell.object.gis.object.variable.GisVariableInitialValue)
	 */
	@Override
	public void set(GisVariableInitialValue gisVariableInitialValue) {
		_fieldTypeComboBox.setSelectedItem( GisInitialValueBase.__nameMap.get( gisVariableInitialValue._fieldType));
		_fieldValueComboBox.setSelectedItem( gisVariableInitialValue._value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel#on_append(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_append(ActionEvent actionEvent) {
		String fieldType = GisInitialValueBase.__typeMap.get( ( String)_fieldTypeComboBox.getSelectedItem());
		if ( null == fieldType)
			return;

		String fieldValue = ( String)_fieldValueComboBox.getSelectedItem();
		if ( null == fieldValue)
			return;

		GisVariableInitialValue gisVariableInitialValue = new GisVariableInitialValue( "field", fieldType, fieldValue);
		_gisInitialValueTableBase.append( new GisVariableInitialValue[] { gisVariableInitialValue, gisVariableInitialValue});
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel#on_insert(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_insert(ActionEvent actionEvent) {
		if ( 1 != _gisInitialValueTableBase.getSelectedRowCount())
			return;

		String fieldType = GisInitialValueBase.__typeMap.get( ( String)_fieldTypeComboBox.getSelectedItem());
		if ( null == fieldType)
			return;

		String fieldValue = ( String)_fieldValueComboBox.getSelectedItem();
		if ( null == fieldValue)
			return;

		GisVariableInitialValue gisVariableInitialValue = new GisVariableInitialValue( "field", fieldType, fieldValue);
		_gisInitialValueTableBase.insert( new GisVariableInitialValue[] { gisVariableInitialValue, gisVariableInitialValue});
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel#on_update(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_update(ActionEvent actionEvent) {
		if ( 1 != _gisInitialValueTableBase.getSelectedRowCount())
			return;

		String fieldType = GisInitialValueBase.__typeMap.get( ( String)_fieldTypeComboBox.getSelectedItem());
		if ( null == fieldType)
			return;

		String fieldValue = ( String)_fieldValueComboBox.getSelectedItem();
		if ( null == fieldValue)
			return;

		GisVariableInitialValue gisVariableInitialValue = new GisVariableInitialValue( "field", fieldType, fieldValue);
		_gisInitialValueTableBase.update( new GisVariableInitialValue[] { gisVariableInitialValue, gisVariableInitialValue});
	}
}
