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

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.map.initial.panel.value.AgentSelectorPanel;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.map.initial.panel.value.ObjectSelectorPanel;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.map.initial.panel.value.SpotSelectorPanel;
import soars.application.visualshell.object.entity.base.object.map.MapInitialValue;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;

/**
 * @author kurata
 *
 */
public class MapInitialValueValuePanel extends JPanel {

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
	private AgentSelectorPanel _agentSelectorPanel = null;

	/**
	 * 
	 */
	private SpotSelectorPanel _spotSelectorPanel = null;

	/**
	 * 
	 */
	private ObjectSelectorPanel _objectSelectorPanel = null;

	/**
	 * @param color
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 */
	public MapInitialValueValuePanel(Color color, EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap) {
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

		_label = new JLabel( ResourceManager.get_instance().get( "edit.map.value.dialog.label.value"));
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
		items.add( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.agent"));
		items.add( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.spot"));
		//items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"));
		if ( _entityBase instanceof AgentObject) {
			items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable"));
		}
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.collection"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.list"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.map"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.file"));
		if ( Environment.get_instance().is_exchange_algebra_enable()) {
			items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra"));
		}
		_typeComboBox = new ComboBox( items.toArray( new String[ 0]), _color, false, new CommonComboBoxRenderer( _color, false));
		_typeComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String item = ( String)_typeComboBox.getSelectedItem();
				_agentSelectorPanel.setVisible( item.equals( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.agent")));
				_spotSelectorPanel.setVisible( item.equals( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.spot")));
				boolean flag = ( !item.equals( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.agent"))
					&& !item.equals( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.spot")));
				_objectSelectorPanel.setVisible( flag);
				if ( flag)
					_objectSelectorPanel.update( item, _entityBase, _propertyPanelBaseMap);
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

		_agentSelectorPanel = new AgentSelectorPanel();
		_agentSelectorPanel.create( _color);
		panel.add( _agentSelectorPanel);

		_spotSelectorPanel = new SpotSelectorPanel();
		_spotSelectorPanel.create( _color);
		panel.add( _spotSelectorPanel);

		_objectSelectorPanel = new ObjectSelectorPanel();;
		_objectSelectorPanel.create( _color);
		panel.add( _objectSelectorPanel);

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
		_spotSelectorPanel.setVisible( false);
		_objectSelectorPanel.setVisible( false);
	}

	/**
	 * @param mapInitialValue
	 */
	public void set(MapInitialValue mapInitialValue) {
		_typeComboBox.setSelectedItem( InitialValueTableBase.__nameMap.get( mapInitialValue._value[ 0]));
		if ( mapInitialValue._value[ 0].equals( "agent"))
			_agentSelectorPanel.set( mapInitialValue._value[ 1]);
		else if ( mapInitialValue._value[ 0].equals( "spot"))
			_spotSelectorPanel.set( mapInitialValue._value[ 1]);
		else
			_objectSelectorPanel.set( mapInitialValue._value[ 1]);
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

		mapInitialValue._value[ 0] = type;

		if ( type.equals( "agent")) {
			String value = _agentSelectorPanel.get();
			if ( null == value || value.equals( ""))
				return false;

			mapInitialValue._value[ 1] = value;
		} else if ( type.equals( "spot")) {
			String value = _spotSelectorPanel.get();
			if ( null == value || value.equals( ""))
				return false;

			mapInitialValue._value[ 1] = value;
		} else {
			String value = _objectSelectorPanel.get();
			if ( null == value || value.equals( ""))
				return false;

			mapInitialValue._value[ 1] = value;
		}

		return true;
	}

	/**
	 * 
	 */
	public void update() {
		String item = ( String)_typeComboBox.getSelectedItem();
		_objectSelectorPanel.update( item, _entityBase, _propertyPanelBaseMap);
	}
}
