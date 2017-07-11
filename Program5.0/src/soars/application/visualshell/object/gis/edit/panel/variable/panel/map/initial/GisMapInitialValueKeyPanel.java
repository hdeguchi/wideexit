/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.variable.panel.map.initial;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.map.initial.panel.common.GisFieldTypeSelectorPanel;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.map.initial.panel.common.GisFieldValueSelectorPanel;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.map.initial.panel.key.GisImmediateDataPanel;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.map.initial.panel.key.GisKeywordSelectorPanel;
import soars.application.visualshell.object.gis.object.map.GisMapInitialValue;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;

/**
 * @author kurata
 *
 */
public class GisMapInitialValueKeyPanel extends JPanel {

	/**
	 * 
	 */
	private JLabel _label = null;

	/**
	 * 
	 */
	private Color _color = null;

	/**
	 * 
	 */
	private GisDataManager _gisDataManager = null;

	/**
	 * 
	 */
	private Map<String, GisPropertyPanelBase> _gisPropertyPanelBaseMap = null;

	/**
	 * 
	 */
	private ComboBox _typeComboBox = null;

	/**
	 * 
	 */
	private GisKeywordSelectorPanel _gisKeywordSelectorPanel = null;

	/**
	 * 
	 */
	private GisImmediateDataPanel _gisImediateDataPanel = null;

	/**
	 * 
	 */
	private GisFieldTypeSelectorPanel _gisFieldTypeSelectorPanel = null;

	/**
	 * 
	 */
	private GisFieldValueSelectorPanel _gisFieldValueSelectorPanel = null;

	/**
	 * @param color
	 * @param entityBase
	 * @param gisPropertyPanelBaseMap
	 */
	public GisMapInitialValueKeyPanel(Color color, GisDataManager gisDataManager, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap) {
		super();
		_color = color;
		_gisDataManager = gisDataManager;
		_gisPropertyPanelBaseMap = gisPropertyPanelBaseMap;
	}

	/**
	 * 
	 */
	public void setup() {
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
		setup_top_panel();
		setup_bottom_panel();
	}

	/**
	 * 
	 */
	private void setup_top_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));
		
		panel.add( Box.createHorizontalStrut( 5));

		setup_label( panel);

		panel.add( Box.createHorizontalStrut( 5));

		setup_top_panel( panel);

		add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_label(JPanel parent) {
		_label = new JLabel( ResourceManager.get_instance().get( "edit.map.value.dialog.label.key"));
		_label.setHorizontalAlignment( SwingConstants.RIGHT);
		_label.setForeground( _color);
		parent.add( _label);
	}

	/**
	 * @param parent
	 */
	private void setup_top_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 1, 1));

		setup_left_panel( panel);

		setup_right_panel( panel);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_left_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		Vector<String> items = new Vector<String>();
		items.add( ResourceManager.get_instance().get( "edit.map.value.dialog.map.key.keyword"));
		items.add( ResourceManager.get_instance().get( "edit.map.value.dialog.map.key.immediate.data"));
		items.add( ResourceManager.get_instance().get( "edit.gis.data.initial.value.field"));
		_typeComboBox = new ComboBox( items.toArray( new String[ 0]), _color, false, new CommonComboBoxRenderer( _color, false));
		_typeComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String item = ( String)_typeComboBox.getSelectedItem();
				_gisKeywordSelectorPanel.setVisible( item.equals( ResourceManager.get_instance().get( "edit.map.value.dialog.map.key.keyword")));
				_gisImediateDataPanel.setVisible( item.equals( ResourceManager.get_instance().get( "edit.map.value.dialog.map.key.immediate.data")));
				_gisFieldTypeSelectorPanel.setVisible( item.equals( ResourceManager.get_instance().get( "edit.gis.data.initial.value.field")));
				_gisFieldValueSelectorPanel.setVisible( item.equals( ResourceManager.get_instance().get( "edit.gis.data.initial.value.field")));
				if ( item.equals( ResourceManager.get_instance().get( "edit.map.value.dialog.map.key.keyword")))
					_gisKeywordSelectorPanel.update( item, _gisPropertyPanelBaseMap);
				revalidate();
				repaint();
			}
		});
		panel.add( _typeComboBox);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_right_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		_gisKeywordSelectorPanel = new GisKeywordSelectorPanel();
		_gisKeywordSelectorPanel.create( _color);
		panel.add( _gisKeywordSelectorPanel);

		_gisImediateDataPanel = new GisImmediateDataPanel();
		_gisImediateDataPanel.create( _color);
		panel.add( _gisImediateDataPanel);

		_gisFieldTypeSelectorPanel = new GisFieldTypeSelectorPanel();;
		_gisFieldTypeSelectorPanel.create( _color);
		panel.add( _gisFieldTypeSelectorPanel);

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void setup_bottom_panel() {
		_gisFieldValueSelectorPanel = new GisFieldValueSelectorPanel( _gisDataManager);
		_gisFieldValueSelectorPanel.create( ResourceManager.get_instance().get( "edit.gis.data.initial.value.field"), _color);
		add( _gisFieldValueSelectorPanel);
	}

	/**
	 * @param width
	 * @return
	 */
	public int get_label_width(int width) {
		width =  Math.max( width, _label.getPreferredSize().width);
		return _gisFieldValueSelectorPanel.get_label_width( width);
	}

	/**
	 * @param width
	 */
	public void adjust(int width) {
		_label.setPreferredSize( new Dimension( width, _label.getPreferredSize().height));
		_gisFieldValueSelectorPanel.adjust( width);
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		_gisKeywordSelectorPanel.update( ResourceManager.get_instance().get( "edit.map.value.dialog.map.key.keyword"), _gisPropertyPanelBaseMap);
		_gisImediateDataPanel._textField.setPreferredSize(
			new Dimension( _gisImediateDataPanel._textField.getPreferredSize().width,
				_gisKeywordSelectorPanel._comboBox.getPreferredSize().height));
		//_gisKeywordSelectorPanel.setVisible( false);
		_gisImediateDataPanel.setVisible( false);
		_gisFieldTypeSelectorPanel.setVisible( false);
		_gisFieldValueSelectorPanel.setVisible( false);
	}

	/**
	 * @param gisMapInitialValue
	 */
	public void set(GisMapInitialValue gisMapInitialValue) {
		if ( gisMapInitialValue._key[ 0].equals( "field")) {
			_typeComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.gis.data.initial.value.field"));
			_gisFieldTypeSelectorPanel.set( gisMapInitialValue._key[ 2]);
			_gisFieldValueSelectorPanel.set( gisMapInitialValue._key[ 1]);
		} else {
			_typeComboBox.setSelectedItem( GisInitialValueTableBase.get_nameMap().get( gisMapInitialValue._key[ 0]));
			if ( gisMapInitialValue._key[ 0].equals( "immediate"))
				_gisImediateDataPanel.set( gisMapInitialValue._key[ 1]);
			else if ( gisMapInitialValue._key[ 0].equals( "keyword"))
				_gisKeywordSelectorPanel.set( gisMapInitialValue._key[ 1]);
		}
	}

	/**
	 * @param gisMapInitialValue
	 * @return
	 */
	public boolean get(GisMapInitialValue gisMapInitialValue) {
		String item = ( String)_typeComboBox.getSelectedItem();
		if ( null == item)
			return false;

		String type = null;
		if ( item.equals( ResourceManager.get_instance().get( "edit.gis.data.initial.value.field")))
			type = "field";
		else {
			type = GisInitialValueTableBase.get_typeMap().get( item);
			if ( null == type)
				return false;
		}

		gisMapInitialValue._key[ 0] = type;

		if ( type.equals( "immediate")) {
			String value = _gisImediateDataPanel.get();
			if ( null == value)
				return false;

			gisMapInitialValue._key[ 1] = value;
		} else if ( type.equals( "keyword")) {
			String value = _gisKeywordSelectorPanel.get();
			if ( null == value || value.equals( ""))
				return false;

			gisMapInitialValue._key[ 1] = value;
		} else if ( type.equals( "field")) {
			String value = _gisFieldTypeSelectorPanel.get();
			if ( null == value || value.equals( ""))
				return false;

			gisMapInitialValue._key[ 2] = value;

			value = _gisFieldValueSelectorPanel.get();
			if ( null == value || value.equals( ""))
				return false;

			gisMapInitialValue._key[ 1] = value;
		} else
			return false;

		return true;
	}

	/**
	 * 
	 */
	public void update() {
		_gisKeywordSelectorPanel.update( ResourceManager.get_instance().get( "edit.map.value.dialog.map.key.keyword"), _gisPropertyPanelBaseMap);
	}

	/**
	 * 
	 */
	public void clear_textUndoRedoManagers() {
		_gisImediateDataPanel.clear_textUndoRedoManagers();
	}

	/**
	 * 
	 */
	public void setup_textUndoRedoManagers() {
		_gisImediateDataPanel.setup_textUndoRedoManagers();
	}
}
