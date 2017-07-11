/*
 * Created on 2006/03/29
 */
package soars.application.visualshell.object.gis.edit.panel.variable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase;
import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.map.GisMapPanel;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.list.GisListPanel;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.set.GisSetPanel;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.application.visualshell.object.gis.object.map.GisMapObject;
import soars.application.visualshell.object.gis.object.variable.GisVariableObject;

/**
 * @author kurata
 */
public class GisVariablePropertyPanel extends GisPropertyPanelBase {

	/**
	 * 
	 */
	private JLabel _kindLabel = null;

	/**
	 * 
	 */
	private JComboBox _kindComboBox = null;

	/**
	 * 
	 */
	private GisSetPanel _gisSetPanel = null;

	/**
	 * 
	 */
	private GisListPanel _gisListPanel = null;

	/**
	 * 
	 */
	private GisMapPanel _gisMapPanel = null;

//	/**
//	 * 
//	 */
//	private GisExchangeAlgebraPanel _gisExchangeAlgebraPanel = null;

	/**
	 * @param title
	 * @param entityBase
	 * @param gisPropertyPanelBaseMap
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public GisVariablePropertyPanel(String title, GisDataManager gisDataManager, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, int index, Frame owner, Component parent) {
		super(title, gisDataManager, gisPropertyPanelBaseMap, index, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase#update_object_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean update_object_name(String type, String name, String newName) {
		boolean result = false;
		if ( _gisVariableTableBase.update_object_name( type, name, newName))
			result = true;

		for ( GisPanelBase gisPanelBase:_gisPanelBaseMap.values()) {
			if ( gisPanelBase.update_object_name( type, name, newName))
				result = true;
		}

		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase#update()
	 */
	@Override
	public void update() {
		for ( GisPanelBase gisPanelBase:_gisPanelBaseMap.values())
			gisPanelBase.update();
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase#on_create()
	 */
	@Override
	protected boolean on_create() {
		if ( !super.on_create())
			return false;


		setLayout( new BorderLayout());


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( centerPanel);

		if ( !setup( centerPanel))
			return false;

		insert_vertical_strut( centerPanel);

		add( centerPanel);


		adjust();


		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup(JPanel parent) {
		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BorderLayout());


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		if ( !setup_center_panel( centerPanel))
			return false;

		basePanel.add( centerPanel);


		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		if ( !setup_components( southPanel))
			return false;

		basePanel.add( southPanel, "South");
		

		parent.add( basePanel);


		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_center_panel(JPanel parent) {
		parent.setLayout( new BorderLayout());

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		create_variableTable();

		if ( !setup_variableTable( centerPanel))
			return false;

		parent.add( centerPanel);


		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		insert_vertical_strut( southPanel);

		if ( !setup_kind_components( southPanel))
			return false;

		parent.add( southPanel, "South");

		return true;
	}

	/**
	 * 
	 */
	private void create_variableTable() {
		_gisVariableTableBase = new GisVariableTable( _gisPropertyPanelBaseMap, this, _owner, _parent);
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_variableTable(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		if ( !_gisVariableTableBase.setup())
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _gisVariableTableBase);
		scrollPane.getViewport().setOpaque( true);
		scrollPane.getViewport().setBackground( SystemColor.text);
		scrollPane.setPreferredSize( new Dimension( scrollPane.getPreferredSize().width, 200));

		panel.add( scrollPane);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_kind_components(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		setup_kindLabel( panel);

		panel.add( Box.createHorizontalStrut( 5));

		setup_kindComboBox( panel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_kindLabel(JPanel parent) {
		_kindLabel = new JLabel( ResourceManager.get_instance().get( "edit.object.dialog.variable.kind.label"));
		_kindLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		parent.add( _kindLabel);
	}

	/**
	 * @param parent
	 */
	private void setup_kindComboBox(JPanel parent) {
		Vector<String> items = new Vector<String>();
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.collection"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.list"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.map"));
//		if ( Environment.get_instance().is_exchange_algebra_enable())
//			items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra"));
		_kindComboBox = new JComboBox( items);
		_kindComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String item = ( String)_kindComboBox.getSelectedItem();
				Set<String> kinds = _gisPanelBaseMap.keySet();
				for ( String kind:kinds) {
					GisPanelBase gisPanelBase = _gisPanelBaseMap.get( kind);
					gisPanelBase.clear();
					gisPanelBase.activate( kind.equals( item));
					//gisPanelBase.setVisible( kind.equals( item));
				}
				revalidate();
				repaint();
			}
		});
		parent.add( _kindComboBox);
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_components(JPanel parent) {
		_gisSetPanel = new GisSetPanel( _gisDataManager, _gisPropertyPanelBaseMap, _gisVariableTableBase,
			get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.collection")), _owner, _parent);
		if ( !_gisSetPanel.setup())
			return false;

		_gisPanelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.collection"), _gisSetPanel);
		parent.add( _gisSetPanel);


		_gisListPanel = new GisListPanel( _gisDataManager, _gisPropertyPanelBaseMap, _gisVariableTableBase,
			get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.list")), _owner, _parent);
		if ( !_gisListPanel.setup())
			return false;

		_gisPanelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.list"), _gisListPanel);
		parent.add( _gisListPanel);


		_gisMapPanel = new GisMapPanel( _gisDataManager, _gisPropertyPanelBaseMap, _gisVariableTableBase,
			get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.map")), _owner, _parent);
		if ( !_gisMapPanel.setup())
			return false;

		_gisPanelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.map"), _gisMapPanel);
		parent.add( _gisMapPanel);


//		if ( Environment.get_instance().is_exchange_algebra_enable()) {
//			_exchangeAlgebraPanel = new ExchangeAlgebraPanel( _entityBase, _propertyPanelBaseMap, _variableTable,
//				get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra")), _owner, _parent);
//			if ( !_exchangeAlgebraPanel.setup())
//				return false;
//
//			_panelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra"), _exchangeAlgebraPanel);
//			parent.add( _exchangeAlgebraPanel);
//		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase#changeSelection(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	public void changeSelection(GisObjectBase gisObjectBase) {
		if ( null != gisObjectBase) {
			if ( gisObjectBase instanceof GisVariableObject && ( ( GisVariableObject)gisObjectBase)._kind.equals( "collection"))
				_kindComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.object.dialog.tree.collection"));
			else if ( gisObjectBase instanceof GisVariableObject && ( ( GisVariableObject)gisObjectBase)._kind.equals( "list"))
				_kindComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.object.dialog.tree.list"));
			else if ( gisObjectBase instanceof GisMapObject)
				_kindComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.object.dialog.tree.map"));
//			else if ( objectBase instanceof GisExchangeAlgebraObject)
//				_kindComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra"));
			else
				return;
		}

		GisPanelBase gisPanelBase = _gisPanelBaseMap.get( _kindComboBox.getSelectedItem());
		if ( null == gisPanelBase)
			return;

		gisPanelBase.update( gisObjectBase);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase#adjust()
	 */
	@Override
	protected void adjust() {
		int width = _kindLabel.getPreferredSize().width;
		Collection<GisPanelBase> gisPanelBases = _gisPanelBaseMap.values();
		for ( GisPanelBase gisPanelBase:gisPanelBases)
			width = gisPanelBase.get_label_width( width);

		int button_width = 0;
		for ( GisPanelBase gisPanelBase:gisPanelBases)
			button_width = gisPanelBase.adjust( width);

		_kindLabel.setPreferredSize( new Dimension( width, _kindLabel.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase#on_setup_completed()
	 */
	@Override
	public void on_setup_completed() {
//		if ( _entityBase.is_multi()) {
//			_variableTable.setEnabled( false);
//			_kindLabel.setEnabled( false);
//			_kindComboBox.setEnabled( false);
//			Collection<GisPanelBase> gisPanelBases = _panelBaseMap.values();
//			for ( GisPanelBase gisPanelBase:gisPanelBases)
//				gisPanelBase.setEnabled( false);
//		}

		Collection<GisPanelBase> panelBases = _gisPanelBaseMap.values();
		for ( GisPanelBase panelBase:panelBases)
			panelBase.on_setup_completed();

		_gisListPanel.setVisible( false);
		_gisMapPanel.setVisible( false);
//		if ( Environment.get_instance().is_exchange_algebra_enable())
//			_gisExchangeAlgebraPanel.setVisible( false);

		_gisVariableTableBase.select_at_first();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase#on_ok()
	 */
	@Override
	public boolean on_ok() {
		if ( isVisible() && !confirm( false))
			return false;

//		_variableTable.on_ok();
//
//		if ( _entityBase instanceof AgentObject) {
//			Observer.get_instance().on_update_agent_object( "collection");
//			Observer.get_instance().on_update_agent_object( "list");
//			Observer.get_instance().on_update_agent_object( "map");
//			Observer.get_instance().on_update_agent_object( "exchange algebra");
//		} else if ( _entityBase instanceof SpotObject) {
//			Observer.get_instance().on_update_spot_object( "collection");
//			Observer.get_instance().on_update_spot_object( "list");
//			Observer.get_instance().on_update_spot_object( "map");
//			Observer.get_instance().on_update_spot_object( "exchange algebra");
//		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase#confirm(boolean)
	 */
	@Override
	public boolean confirm(boolean fromTree) {
		if ( !isVisible())
			return true;

		//if ( 0 == _gisVariableTable.getRowCount())
		//	return true;

		if ( !_gisSetPanel.confirm( fromTree))
			return false;

		if ( !_gisListPanel.confirm( fromTree))
			return false;

		if ( !_gisMapPanel.confirm( fromTree))
			return false;

//		if ( Environment.get_instance().is_exchange_algebra_enable()) {
//			if ( !_gisExchangeAlgebraPanel.confirm( fromTree))
//				return false;
//		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase#confirm(int, soars.application.visualshell.object.gis.object.base.GisObjectBase, soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	public GisObjectBase confirm(int row, GisObjectBase targetGisObjectBase, GisObjectBase selectedGisObjectBase) {
		GisObjectBase gisObjectBase = _gisSetPanel.confirm( row, targetGisObjectBase, selectedGisObjectBase);
		if ( null != gisObjectBase)
			return gisObjectBase;

		gisObjectBase = _gisListPanel.confirm( row, targetGisObjectBase, selectedGisObjectBase);
		if ( null != gisObjectBase)
			return gisObjectBase;

		gisObjectBase = _gisMapPanel.confirm( row, targetGisObjectBase, selectedGisObjectBase);
		if ( null != gisObjectBase)
			return gisObjectBase;

//		if ( Environment.get_instance().is_exchange_algebra_enable()) {
//			gisObjectBase = _gisExchangeAlgebraPanel.confirm( row, targetGisObjectBase, selectedGisObjectBase);
//			if ( null != gisObjectBase)
//				return gisObjectBase;
//		}

		return null;
	}
}
