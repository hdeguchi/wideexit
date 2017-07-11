/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;
import soars.application.visualshell.object.gis.edit.panel.simple.GisSimpleVariablePropertyPanel;
import soars.application.visualshell.object.gis.edit.panel.variable.GisVariablePropertyPanel;
import soars.application.visualshell.object.gis.edit.tree.GisObjectTree;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class GisObjectPanel extends JPanel {

	/**
	 * 
	 */
	public GisDataManager _gisDataManager = null;

	/**
	 * 
	 */
	public GisObjectTree _gisObjectTree = null;

	/**
	 * 
	 */
	private List<GisPropertyPanelBase> _gisPropertyPanelBases = new ArrayList<GisPropertyPanelBase>();

	/**
	 * 
	 */
	private Map<String, GisPropertyPanelBase> _gisPropertyPanelBaseMap = new HashMap<String, GisPropertyPanelBase>();

	/**
	 * 
	 */
	static private int _selectedIndex = 0;

	/**
	 * @param gisDataManager 
	 * 
	 */
	public GisObjectPanel(GisDataManager gisDataManager) {
		super();
		_gisDataManager = gisDataManager;
	}

	/**
	 * 
	 */
	protected void get_selected_index() {
		for ( int i = 0; i < _gisPropertyPanelBases.size(); ++i) {
			if ( _gisPropertyPanelBases.get( i).isVisible()) {
				_selectedIndex = i;
				break;
			}
		}
	}

	/**
	 * 
	 */
	protected void set_selected_index() {
		for ( GisPropertyPanelBase gisPropertyPanelBase:_gisPropertyPanelBases)
			gisPropertyPanelBase.setVisible( gisPropertyPanelBase.equals( _gisPropertyPanelBases.get( _selectedIndex)));
	}

	/**
	 * 
	 */
	private void select() {
		for ( GisPropertyPanelBase gisPropertyPanelBase:_gisPropertyPanelBases) {
			if ( gisPropertyPanelBase.isVisible())
				_gisObjectTree.select( gisPropertyPanelBase);
		}
	}

	/**
	 * @param component
	 */
	public void select(Component component) {
		for ( GisPropertyPanelBase gisPropertyPanelBase:_gisPropertyPanelBases)
			gisPropertyPanelBase.setVisible( component.equals( gisPropertyPanelBase));
	}

	/**
	 * @param component
	 * @return
	 */
	public boolean confirm(Component component) {
		GisPropertyPanelBase gisPropertyPanelBase = ( GisPropertyPanelBase)component;
		return gisPropertyPanelBase.confirm( true);
	}

	/**
	 * @return
	 */
	public boolean confirm() {
		for ( GisPropertyPanelBase gisPropertyPanelBase:_gisPropertyPanelBases) {
			if ( !gisPropertyPanelBase.confirm( false))
				return false;
		}
		return true;
	}

	/**
	 * @param gisObjectTree
	 * @param owner
	 * @param parent
	 * @return
	 */
	public boolean setup(GisObjectTree gisObjectTree, Frame owner, Component parent) {
		_gisObjectTree = gisObjectTree;

		// これ超重要
		setLayout( new BorderLayout());

		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));

		GisSimpleVariablePropertyPanel gisSimpleVariablePropertyPanel = new GisSimpleVariablePropertyPanel(
			ResourceManager.get_instance().get( "edit.object.dialog.tree.simple.variable"),
			_gisDataManager, _gisPropertyPanelBaseMap, _gisPropertyPanelBases.size(), owner, parent);
		if ( !create( gisSimpleVariablePropertyPanel, "simple variable"))
			return false;

		GisVariablePropertyPanel gisVariablePropertyPanel = new GisVariablePropertyPanel(
			ResourceManager.get_instance().get( "edit.object.dialog.tree.variable"),
			_gisDataManager, _gisPropertyPanelBaseMap, _gisPropertyPanelBases.size(), owner, parent);
		if ( !create( gisVariablePropertyPanel, "variable"))
			return false;

		_gisObjectTree.expand();

		return true;
	}

	/**
	 * @param gisPropertyPanelBase
	 * @param key
	 * @return
	 */
	private boolean create(GisPropertyPanelBase gisPropertyPanelBase, String key) {
		if ( !gisPropertyPanelBase.create())
			return false;

		add( gisPropertyPanelBase);
		_gisObjectTree.append( gisPropertyPanelBase);
		_gisPropertyPanelBaseMap.put( key, gisPropertyPanelBase);
		_gisPropertyPanelBases.add( gisPropertyPanelBase);
		return true;
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		for ( GisPropertyPanelBase gisPropertyPanelBase:_gisPropertyPanelBases) {
			gisPropertyPanelBase.on_setup_completed();
			gisPropertyPanelBase.setVisible( gisPropertyPanelBase.equals( _gisPropertyPanelBases.get( 0)));
		}

		set_selected_index();
		select();
	}

	/**
	 * @param gisObjectBases
	 */
	public void set(List<GisObjectBase> gisObjectBases) {
		for ( GisObjectBase gisObjectBase:gisObjectBases) {
			if ( gisObjectBase._kind.equals( "keyword")
				|| gisObjectBase._kind.equals( "number object")
				|| gisObjectBase._kind.equals( "time variable")
				|| gisObjectBase._kind.equals( "spot variable"))
				_gisPropertyPanelBaseMap.get( "simple variable").append( gisObjectBase);
			else if ( gisObjectBase._kind.equals( "collection")
				|| gisObjectBase._kind.equals( "list")
				|| gisObjectBase._kind.equals( "map"))
				_gisPropertyPanelBaseMap.get( "variable").append( gisObjectBase);
		}
		for ( GisPropertyPanelBase gisPropertyPanelBase:_gisPropertyPanelBases)
			gisPropertyPanelBase.select_at_first();
	}

	/**
	 * @return
	 */
	public List<GisObjectBase> get() {
		List<GisObjectBase> gisObjectBases = new ArrayList<GisObjectBase>();
		for ( GisPropertyPanelBase gisPropertyPanelBase:_gisPropertyPanelBases)
			gisPropertyPanelBase.get( gisObjectBases);
		return gisObjectBases;
	}

	/**
	 * @return
	 */
	public boolean on_ok() {
		//for ( GisPropertyPanelBase gisPropertyPanelBase:_gisPropertyPanelBases) {
		// 参照されているものを後から保存しないとダメ！
		for ( int i = _gisPropertyPanelBases.size() - 1; 0 <= i; --i) {
			GisPropertyPanelBase gisPropertyPanelBase = _gisPropertyPanelBases.get( i);
			if ( !gisPropertyPanelBase.on_ok())
				return false;
		}

		get_selected_index();

		return true;
	}

	/**
	 * 
	 */
	public void on_cancel() {
		get_selected_index();
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(Writer writer) throws SAXException {
		if ( _gisPropertyPanelBaseMap.get( "simple variable").is_empty()
			&& _gisPropertyPanelBaseMap.get( "variable").is_empty())
			return true;

		writer.startElement( null, null, "variable_data", new AttributesImpl());

		if ( !_gisPropertyPanelBaseMap.get( "simple variable").write( writer))
			return false;

		if ( !_gisPropertyPanelBaseMap.get( "variable").write( writer))
			return false;

		writer.endElement( null, null, "variable_data");

		return true;
	}
}
