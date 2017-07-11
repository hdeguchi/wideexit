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
import soars.application.visualshell.object.gis.edit.panel.variable.panel.map.initial.panel.value.GisAgentSelectorPanel;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.map.initial.panel.value.GisObjectSelectorPanel;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.map.initial.panel.value.GisSpotSelectorPanel;
import soars.application.visualshell.object.gis.object.map.GisMapInitialValue;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;

/**
 * @author kurata
 *
 */
public class GisMapInitialValueValuePanel extends JPanel {

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
	private GisAgentSelectorPanel _gisAgentSelectorPanel = null;

	/**
	 * 
	 */
	private GisSpotSelectorPanel _gisSpotSelectorPanel = null;

	/**
	 * 
	 */
	private GisFieldTypeSelectorPanel _gisFieldTypeSelectorPanel = null;

	/**
	 * 
	 */
	private GisFieldValueSelectorPanel _gisFieldValueSelectorPanel = null;

	/**
	 * 
	 */
	private GisObjectSelectorPanel _gisObjectSelectorPanel = null;

	/**
	 * @param color
	 * @param gisDataManager
	 * @param gisPropertyPanelBaseMap
	 */
	public GisMapInitialValueValuePanel(Color color, GisDataManager gisDataManager, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap) {
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
		_label = new JLabel( ResourceManager.get_instance().get( "edit.map.value.dialog.label.value"));
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
//		items.add( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.agent"));
//		items.add( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.spot"));
		//items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"));
//		if ( _entityBase instanceof AgentObject) {
//			items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable"));
//		}
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.collection"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.list"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.map"));
//		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"));
//		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.file"));
//		if ( Environment.get_instance().is_exchange_algebra_enable()) {
//			items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra"));
//		}
		items.add( ResourceManager.get_instance().get( "edit.gis.data.initial.value.field"));
		_typeComboBox = new ComboBox( items.toArray( new String[ 0]), _color, false, new CommonComboBoxRenderer( _color, false));
		_typeComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String item = ( String)_typeComboBox.getSelectedItem();
				_gisAgentSelectorPanel.setVisible( item.equals( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.agent")));
				_gisSpotSelectorPanel.setVisible( item.equals( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.spot")));
				_gisFieldTypeSelectorPanel.setVisible( item.equals( ResourceManager.get_instance().get( "edit.gis.data.initial.value.field")));
				_gisFieldValueSelectorPanel.setVisible( item.equals( ResourceManager.get_instance().get( "edit.gis.data.initial.value.field")));
				boolean flag = ( !item.equals( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.agent"))
					&& !item.equals( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.spot"))
					&& !item.equals( ResourceManager.get_instance().get( "edit.gis.data.initial.value.field")));
				_gisObjectSelectorPanel.setVisible( flag);
				if ( flag)
					_gisObjectSelectorPanel.update( item, _gisPropertyPanelBaseMap);
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

		_gisAgentSelectorPanel = new GisAgentSelectorPanel();
		_gisAgentSelectorPanel.create( _color);
		panel.add( _gisAgentSelectorPanel);

		_gisSpotSelectorPanel = new GisSpotSelectorPanel();
		_gisSpotSelectorPanel.create( _color);
		panel.add( _gisSpotSelectorPanel);

		_gisFieldTypeSelectorPanel = new GisFieldTypeSelectorPanel();;
		_gisFieldTypeSelectorPanel.create( _color);
		panel.add( _gisFieldTypeSelectorPanel);

		_gisObjectSelectorPanel = new GisObjectSelectorPanel();;
		_gisObjectSelectorPanel.create( _color);
		panel.add( _gisObjectSelectorPanel);

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
		_gisObjectSelectorPanel.update( ResourceManager.get_instance().get( "edit.map.value.dialog.map.key.keyword"), _gisPropertyPanelBaseMap);
		_gisAgentSelectorPanel.setVisible( false);
		_gisSpotSelectorPanel.setVisible( false);
		//_gisObjectSelectorPanel.setVisible( false);
		_gisFieldTypeSelectorPanel.setVisible( false);
		_gisFieldValueSelectorPanel.setVisible( false);
	}

	/**
	 * @param gisMapInitialValue
	 */
	public void set(GisMapInitialValue gisMapInitialValue) {
		if ( gisMapInitialValue._value[ 0].equals( "field")) {
			_typeComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.gis.data.initial.value.field"));
			_gisFieldTypeSelectorPanel.set( gisMapInitialValue._value[ 2]);
			_gisFieldValueSelectorPanel.set( gisMapInitialValue._value[ 1]);
		} else {
			_typeComboBox.setSelectedItem( GisInitialValueTableBase.get_nameMap().get( gisMapInitialValue._value[ 0]));
			if ( gisMapInitialValue._value[ 0].equals( "agent"))
				_gisAgentSelectorPanel.set( gisMapInitialValue._value[ 1]);
			else if ( gisMapInitialValue._value[ 0].equals( "spot"))
				_gisSpotSelectorPanel.set( gisMapInitialValue._value[ 1]);
			else
				_gisObjectSelectorPanel.set( gisMapInitialValue._value[ 1]);
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

		gisMapInitialValue._value[ 0] = type;

		if ( type.equals( "agent")) {
			String value = _gisAgentSelectorPanel.get();
			if ( null == value || value.equals( ""))
				return false;

			gisMapInitialValue._value[ 1] = value;
		} else if ( type.equals( "spot")) {
			String value = _gisSpotSelectorPanel.get();
			if ( null == value || value.equals( ""))
				return false;

			gisMapInitialValue._value[ 1] = value;
		} else if ( type.equals( "field")) {
			String value = _gisFieldTypeSelectorPanel.get();
			if ( null == value || value.equals( ""))
				return false;

			gisMapInitialValue._value[ 2] = value;

			value = _gisFieldValueSelectorPanel.get();
			if ( null == value || value.equals( ""))
				return false;

			gisMapInitialValue._value[ 1] = value;
		} else {
			String value = _gisObjectSelectorPanel.get();
			if ( null == value || value.equals( ""))
				return false;

			gisMapInitialValue._value[ 1] = value;
		}

		return true;
	}

	/**
	 * 
	 */
	public void update() {
		String item = ( String)_typeComboBox.getSelectedItem();
		_gisObjectSelectorPanel.update( item, _gisPropertyPanelBaseMap);
	}
}
