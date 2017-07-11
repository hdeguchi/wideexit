/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.map.initial;

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
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.map.initial.panel.key.ImmediateDataPanel;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.map.initial.panel.key.KeywordSelectorPanel;
import soars.application.visualshell.object.entity.base.object.map.MapInitialValue;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;

/**
 * @author kurata
 *
 */
public class MapInitialValueKeyPanel extends JPanel {

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
	private EntityBase _entityBase = null;

	/**
	 * 
	 */
	private Map<String, PropertyPanelBase> _propertyPanelBaseMap = null;

	/**
	 * 
	 */
	private ComboBox _typeComboBox = null;

	/**
	 * 
	 */
	private KeywordSelectorPanel _keywordSelectorPanel = null;

	/**
	 * 
	 */
	private ImmediateDataPanel _immediateDataPanel = null;

	/**
	 * @param color
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 */
	public MapInitialValueKeyPanel(Color color, EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap) {
		super();
		_color = color;
		_entityBase = entityBase;
		_propertyPanelBaseMap = propertyPanelBaseMap;
	}

	/**
	 * 
	 */
	public void setup() {
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));

		setup_label();

		setup_components();
	}

	/**
	 * 
	 */
	private void setup_label() {
		add( Box.createHorizontalStrut( 5));

		_label = new JLabel( ResourceManager.get_instance().get( "edit.map.value.dialog.label.key"));
		_label.setHorizontalAlignment( SwingConstants.RIGHT);
		_label.setForeground( _color);
		add( _label);

		add( Box.createHorizontalStrut( 5));
	}

	/**
	 * 
	 */
	private void setup_components() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 1, 1));

		setup_left_panel( panel);

		setup_right_panel( panel);

		add( panel);
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
		_typeComboBox = new ComboBox( items.toArray( new String[ 0]), _color, false, new CommonComboBoxRenderer( _color, false));
		_typeComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String item = ( String)_typeComboBox.getSelectedItem();
				_keywordSelectorPanel.setVisible( item.equals( ResourceManager.get_instance().get( "edit.map.value.dialog.map.key.keyword")));
				_immediateDataPanel.setVisible( item.equals( ResourceManager.get_instance().get( "edit.map.value.dialog.map.key.immediate.data")));
				if ( item.equals( ResourceManager.get_instance().get( "edit.map.value.dialog.map.key.keyword")))
					_keywordSelectorPanel.update( item, _entityBase, _propertyPanelBaseMap);
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

		_keywordSelectorPanel = new KeywordSelectorPanel();
		_keywordSelectorPanel.create( _color);
		panel.add( _keywordSelectorPanel);

		_immediateDataPanel = new ImmediateDataPanel();
		_immediateDataPanel.create( _color);
		panel.add( _immediateDataPanel);

		parent.add( panel);
	}

	/**
	 * @param width
	 * @return
	 */
	public int get_label_width(int width) {
		return Math.max( width, _label.getPreferredSize().width);
	}

	/**
	 * @param width
	 */
	public void adjust(int width) {
		_label.setPreferredSize( new Dimension( width, _label.getPreferredSize().height));
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		_keywordSelectorPanel.update( ResourceManager.get_instance().get( "edit.map.value.dialog.map.key.keyword"), _entityBase, _propertyPanelBaseMap);
		_immediateDataPanel._textField.setPreferredSize(
			new Dimension( _immediateDataPanel._textField.getPreferredSize().width,
				_keywordSelectorPanel._comboBox.getPreferredSize().height));
		_immediateDataPanel.setVisible( false);
	}

	/**
	 * @param mapInitialValue
	 */
	public void set(MapInitialValue mapInitialValue) {
		_typeComboBox.setSelectedItem( InitialValueTableBase.__nameMap.get( mapInitialValue._key[ 0]));
		if ( mapInitialValue._key[ 0].equals( "immediate"))
			_immediateDataPanel.set( mapInitialValue._key[ 1]);
		else if ( mapInitialValue._key[ 0].equals( "keyword"))
			_keywordSelectorPanel.set( mapInitialValue._key[ 1]);
	}

	/**
	 * @param mapInitialValue
	 * @return
	 */
	public boolean get(MapInitialValue mapInitialValue) {
		String item = ( String)_typeComboBox.getSelectedItem();
		if ( null == item)
			return false;

		String type = InitialValueTableBase.__typeMap.get( item);
		if ( null == type)
			return false;

		mapInitialValue._key[ 0] = type;

		if ( type.equals( "immediate")) {
			String value = _immediateDataPanel.get();
			if ( null == value)
				return false;

			mapInitialValue._key[ 1] = value;
		} else if ( type.equals( "keyword")) {
			String value = _keywordSelectorPanel.get();
			if ( null == value || value.equals( ""))
				return false;

			mapInitialValue._key[ 1] = value;
		} else
			return false;

		return true;
	}

	/**
	 * 
	 */
	public void update() {
		_keywordSelectorPanel.update( ResourceManager.get_instance().get( "edit.map.value.dialog.map.key.keyword"), _entityBase, _propertyPanelBaseMap);
	}

	/**
	 * 
	 */
	public void clear_textUndoRedoManagers() {
		_immediateDataPanel.clear_textUndoRedoManagers();
	}

	/**
	 * 
	 */
	public void setup_textUndoRedoManagers() {
		_immediateDataPanel.setup_textUndoRedoManagers();
	}
}
