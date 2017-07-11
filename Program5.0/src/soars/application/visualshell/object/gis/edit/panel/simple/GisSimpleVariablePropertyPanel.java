/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.simple;

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
import soars.application.visualshell.object.gis.edit.panel.simple.panel.keyword.GisKeywordPanel;
import soars.application.visualshell.object.gis.edit.panel.simple.panel.number.GisNumberVariablePanel;
import soars.application.visualshell.object.gis.edit.panel.simple.panel.spot.GisSpotVariablePanel;
import soars.application.visualshell.object.gis.edit.panel.simple.panel.time.GisTimeVariablePanel;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.application.visualshell.object.gis.object.keyword.GisKeywordObject;
import soars.application.visualshell.object.gis.object.number.GisNumberObject;
import soars.application.visualshell.object.gis.object.spot.GisSpotVariableObject;
import soars.application.visualshell.object.gis.object.time.GisTimeVariableObject;

/**
 * @author kurata
 *
 */
public class GisSimpleVariablePropertyPanel extends GisPropertyPanelBase {

	/**
	 * 
	 */
	private JLabel _kindLabel = null;

	/**
	 * 
	 */
	private JComboBox _kindComboBox = null;

//	/**
//	 * 
//	 */
//	private GisProbabilityVariablePanel _gisProbabilityVariablePanel = null;

	/**
	 * 
	 */
	private GisKeywordPanel _gisKeywordPanel = null;

	/**
	 * 
	 */
	private GisNumberVariablePanel _gisNumberVariablePanel = null;

	/**
	 * 
	 */
	private GisTimeVariablePanel _gisTimeVariablePanel = null;

	/**
	 * 
	 */
	private GisSpotVariablePanel _gisSpotVariablePanel = null;

	/**
	 * @param title
	 * @param gisDataManager
	 * @param gisPropertyPanelBaseMap
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public GisSimpleVariablePropertyPanel(String title, GisDataManager gisDataManager, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, int index, Frame owner, Component parent) {
		super(title, gisDataManager, gisPropertyPanelBaseMap, index, owner, parent);
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

		create_simple_variable_table();

		if ( !setup_simple_variable_table( centerPanel))
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
	private void create_simple_variable_table() {
		_gisVariableTableBase = new GisSimpleVariableTable( _gisPropertyPanelBaseMap, this, _owner, _parent);
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_simple_variable_table(JPanel parent) {
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
		_kindLabel = new JLabel( ResourceManager.get_instance().get( "edit.object.dialog.simple.variable.kind.label"));
		_kindLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		parent.add( _kindLabel);
	}

	/**
	 * @param parent
	 */
	private void setup_kindComboBox(JPanel parent) {
		Vector<String> items = new Vector<String>();
		//items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"));
//		if ( _entityBase instanceof AgentObject) {
//			items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable"));
//		}
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"));
		_kindComboBox = new JComboBox( items);
		_kindComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String item = ( String)_kindComboBox.getSelectedItem();
				Set<String> kinds = _gisPanelBaseMap.keySet();
				for ( String kind:kinds) {
					GisPanelBase gisPanelBase = _gisPanelBaseMap.get( kind);
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
//		_gisProbabilityVariablePanel = new GisProbabilityVariablePanel( _entityBase, _gisPropertyPanelBaseMap, _gisSimpleVariableTable,
//			get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.probability")), _owner, _parent);
//		if ( !_gisProbabilityVariablePanel.setup())
//			return false;
//
//		_gisPanelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"), _gisProbabilityVariablePanel);
//		parent.add( _gisProbabilityVariablePanel);


		_gisKeywordPanel = new GisKeywordPanel( _gisDataManager, _gisPropertyPanelBaseMap, _gisVariableTableBase,
			get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword")), _owner, _parent);
		if ( !_gisKeywordPanel.setup())
			return false;

		_gisPanelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"), _gisKeywordPanel);
		parent.add( _gisKeywordPanel);


		_gisNumberVariablePanel = new GisNumberVariablePanel( _gisDataManager, _gisPropertyPanelBaseMap, _gisVariableTableBase,
			get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object")), _owner, _parent);
		if ( !_gisNumberVariablePanel.setup())
			return false;

		_gisPanelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"), _gisNumberVariablePanel);
		parent.add( _gisNumberVariablePanel);


		_gisTimeVariablePanel = new GisTimeVariablePanel( _gisDataManager, _gisPropertyPanelBaseMap, _gisVariableTableBase,
			get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable")), _owner, _parent);
		if ( !_gisTimeVariablePanel.setup())
			return false;

		_gisPanelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"), _gisTimeVariablePanel);
		parent.add( _gisTimeVariablePanel);


		_gisSpotVariablePanel = new GisSpotVariablePanel( _gisDataManager, _gisPropertyPanelBaseMap, _gisVariableTableBase,
			get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable")), _owner, _parent);
		if ( !_gisSpotVariablePanel.setup())
			return false;

		_gisPanelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"), _gisSpotVariablePanel);
		parent.add( _gisSpotVariablePanel);


		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase#changeSelection(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	public void changeSelection(GisObjectBase gisObjectBase) {
		if ( null != gisObjectBase) {
//			if ( gisObjectBase instanceof GisProbabilityObject)
//				_kindComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"));
			if ( gisObjectBase instanceof GisKeywordObject)
				_kindComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"));
			else if ( gisObjectBase instanceof GisNumberObject)
				_kindComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"));
			else if ( gisObjectBase instanceof GisTimeVariableObject)
				_kindComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"));
			else if ( gisObjectBase instanceof GisSpotVariableObject)
				_kindComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"));
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

		int buttonWidth = 0;
		for ( GisPanelBase gisPanelBase:gisPanelBases)
			buttonWidth = gisPanelBase.adjust( width);

		_kindLabel.setPreferredSize( new Dimension( width, _kindLabel.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase#on_setup_completed()
	 */
	@Override
	public void on_setup_completed() {

//		_gisKeywordPanel.setVisible( false);
		_gisNumberVariablePanel.setVisible( false);
		_gisTimeVariablePanel.setVisible( false);
		_gisSpotVariablePanel.setVisible( false);
		_gisVariableTableBase.on_setup_completed();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase#on_ok()
	 */
	@Override
	public boolean on_ok() {
		if ( isVisible() && !confirm( false))
			return false;

//		_simpleVariableTable.on_ok();
//
//		if ( _entityBase instanceof AgentObject) {
////			Observer.get_instance().on_update_agent_object( "probability");
//			Observer.get_instance().on_update_agent_object( "keyword");
//			Observer.get_instance().on_update_agent_object( "number object");
////			Observer.get_instance().on_update_agent_object( "role variable");
//			Observer.get_instance().on_update_agent_object( "time variable");
//			Observer.get_instance().on_update_agent_object( "spot variable");
//		} else if ( _entityBase instanceof SpotObject) {
////			Observer.get_instance().on_update_spot_object( "probability");
//			Observer.get_instance().on_update_spot_object( "keyword");
//			Observer.get_instance().on_update_spot_object( "number object");
////			Observer.get_instance().on_update_spot_object( "role variable");
//			Observer.get_instance().on_update_spot_object( "time variable");
//			Observer.get_instance().on_update_spot_object( "spot variable");
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

		//if ( 0 == _simpleVariableTable.getRowCount())
		//	return true;

//		if ( !_gisProbabilityVariablePanel.confirm( fromTree))
//			return false;

		if ( !_gisKeywordPanel.confirm( fromTree))
			return false;

		if ( !_gisNumberVariablePanel.confirm( fromTree))
			return false;


		if ( !_gisTimeVariablePanel.confirm( fromTree))
			return false;

		if ( !_gisSpotVariablePanel.confirm( fromTree))
			return false;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase#confirm(int, soars.application.visualshell.object.gis.object.base.GisObjectBase, soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	public GisObjectBase confirm(int row, GisObjectBase targetGisObjectBase, GisObjectBase selectedGisObjectBase) {
//		GisObjectBase gisObjectBase = _gisProbabilityVariablePanel.confirm( row, targetGisObjectBase, selectedGisObjectBase);
//		if ( null != gisObjectBase)
//			return gisObjectBase;

		GisObjectBase gisObjectBase = _gisKeywordPanel.confirm( row, targetGisObjectBase, selectedGisObjectBase);
		if ( null != gisObjectBase)
			return gisObjectBase;

		gisObjectBase = _gisNumberVariablePanel.confirm( row, targetGisObjectBase, selectedGisObjectBase);
		if ( null != gisObjectBase)
			return gisObjectBase;

//		if ( _entityBase instanceof AgentObject) {
//			gisObjectBase = _gisRoleVariablePanel.confirm( row, targetGisObjectBase, selectedGisObjectBase);
//			if ( null != gisObjectBase)
//				return gisObjectBase;
//		}

		gisObjectBase = _gisTimeVariablePanel.confirm( row, targetGisObjectBase, selectedGisObjectBase);
		if ( null != gisObjectBase)
			return gisObjectBase;

		gisObjectBase = _gisSpotVariablePanel.confirm( row, targetGisObjectBase, selectedGisObjectBase);
		if ( null != gisObjectBase)
			return gisObjectBase;

		return null;
	}
}
