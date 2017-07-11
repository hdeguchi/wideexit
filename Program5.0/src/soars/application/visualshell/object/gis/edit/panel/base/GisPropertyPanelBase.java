/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.base;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

import org.xml.sax.SAXException;

import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.common.utility.swing.panel.StandardPanel;
import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class GisPropertyPanelBase extends StandardPanel implements ITextUndoRedoManagerCallBack {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	public String _title = "";

	/**
	 * 
	 */
	protected GisDataManager _gisDataManager = null;

	/**
	 * 
	 */
	protected Map<String, GisPropertyPanelBase> _gisPropertyPanelBaseMap = null;

	/**
	 * 
	 */
	public int _index = 0;

	/**
	 * 
	 */
	protected GisVariableTableBase _gisVariableTableBase = null; 

	/**
	 * 
	 */
	protected List<TextUndoRedoManager> _textUndoRedoManagers = new ArrayList<TextUndoRedoManager>();

	/**
	 * 
	 */
	protected Frame _owner = null;

	/**
	 * 
	 */
	protected Component _parent = null;

	/**
	 * 
	 */
	public Map<String, GisPanelBase> _gisPanelBaseMap = new HashMap<String, GisPanelBase>();

//	/**
//	 * 
//	 */
//	static public Map<String, String> _nameMap = null;
//
//	/**
//	 * 
//	 */
//	static public Map<String, Color> _colorMap = null;
//
//	/**
//	 * 
//	 */
//	static {
//		startup();
//	}
//
//	/**
//	 * 
//	 */
//	private static void startup() {
//		synchronized( _lock) {
//			if ( null == _nameMap) {
//				_nameMap = new HashMap<String, String>();
////				_nameMap.put( "probability", ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"));
//				_nameMap.put( "keyword", ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"));
//				_nameMap.put( "number object", ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"));
//				_nameMap.put( "role variable", ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable"));
//				_nameMap.put( "time variable", ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"));
//				_nameMap.put( "spot variable", ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"));
//				_nameMap.put( "collection", ResourceManager.get_instance().get( "edit.object.dialog.tree.collection"));
//				_nameMap.put( "list", ResourceManager.get_instance().get( "edit.object.dialog.tree.list"));
//				_nameMap.put( "map", ResourceManager.get_instance().get( "edit.object.dialog.tree.map"));
//				_nameMap.put( "exchange algebra", ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra"));
//				_nameMap.put( "extransfer", ResourceManager.get_instance().get( "edit.object.dialog.tree.extransfer"));
//				_nameMap.put( "class variable", ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"));
//				_nameMap.put( "file", ResourceManager.get_instance().get( "edit.object.dialog.tree.file"));
//				_nameMap.put( "initial data file", ResourceManager.get_instance().get( "edit.object.dialog.tree.initial.data.file"));
//			}
//
//			if ( null == _colorMap) {
//				_colorMap = new HashMap<String, Color>();
////				_colorMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"), new Color( 200, 0, 200));
//				_colorMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"), new Color( 0, 128, 128));
//				_colorMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"), new Color( 0, 0, 128));
//				_colorMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable"), new Color( 0, 64, 128));
//				_colorMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"), new Color( 128, 0, 0));
//				_colorMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"), new Color( 240, 0, 0));
//				_colorMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.collection"), new Color( 128, 128, 0));
//				_colorMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.list"), new Color( 64, 128, 0));
//				_colorMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.map"), new Color( 0, 160, 0));
//				_colorMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra"), new Color( 0, 0, 200));
//			}
//		}
//	}

	/**
	 * 
	 */
	static public Map<String, String> get_nameMap() {
		return PropertyPanelBase._nameMap;
	}

	/**
	 * 
	 */
	static public Map<String, Color> get_colorMap() {
		return PropertyPanelBase._colorMap;
	}

	/**
	 * @param title
	 * @param gisDataManager
	 * @param gisPropertyPanelBaseMap
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public GisPropertyPanelBase(String title, GisDataManager gisDataManager, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, int index, Frame owner, Component parent) {
		super();
		_title = title;
		_gisDataManager = gisDataManager;
		_gisPropertyPanelBaseMap = gisPropertyPanelBaseMap;
		_index = index;
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @param name
	 * @return
	 */
	public boolean contains(String name) {
		return _gisVariableTableBase.contains( name);
	}

	/**
	 * @param name
	 * @param number
	 * @return
	 */
	public boolean contains(String name, String number) {
		return _gisVariableTableBase.contains(name, number);
	}

	/**
	 * @param gisObjectBase
	 * @return
	 */
	public boolean contains(GisObjectBase gisObjectBase) {
		boolean result = false;
		if ( _gisVariableTableBase.contains( gisObjectBase))
			result = true;

		for ( GisPanelBase gisPanelBase:_gisPanelBaseMap.values()) {
			if ( gisPanelBase.contains( gisObjectBase))
				result = true;
		}

		return result;
	}

	/**
	 * @param type
	 * @param headName
	 * @param ranges
	 * @return
	 */
	public boolean can_adjust_name(String type, String headName, Vector<String[]> ranges) {
		return _gisVariableTableBase.can_adjust_name( type, headName, ranges);
	}

	/**
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	public boolean can_adjust_name(String type, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		return _gisVariableTableBase.can_adjust_name( type, headName, ranges, newHeadName, newRanges);
	}

	/**
	 * @param type
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 */
	public void update_name_and_number(String type, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		_gisVariableTableBase.update_name_and_number( type, newName, originalName, headName, ranges, newHeadName, newRanges);
	}

	/**
	 * @param type
	 * @param name
	 * @param newName
	 */
	public boolean update_object_name(String type, String name, String newName) {
		return false;
	}

	/**
	 * @return
	 */
	public String[] get() {
		return null;
	}

	/**
	 * @param kind
	 * @return
	 */
	public String[] get(String kind) {
		return _gisVariableTableBase.get( kind);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_append(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	protected void on_update(ActionEvent actionEvent) {
	}

	/**
	 * 
	 */
	public void update() {
	}

	/**
	 * @param actionEvent
	 */
	protected void on_clear(ActionEvent actionEvent) {
	}

	/**
	 * @param gisObjectBase
	 */
	public void changeSelection(GisObjectBase gisObjectBase) {
	}

	/**
	 * 
	 */
	protected void adjust() {
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
	}

	/**
	 * @param gisObjectBase
	 */
	public void append(GisObjectBase gisObjectBase) {
		_gisVariableTableBase.append( gisObjectBase);
	}

	/**
	 * @param gisObjectBases
	 */
	public void get(List<GisObjectBase> gisObjectBases) {
		_gisVariableTableBase.get(gisObjectBases);
	}

	/**
	 * @return
	 */
	public boolean on_ok() {
		return true;
	}

	/**
	 * 
	 */
	public void on_cancel() {
	}

	/**
	 * @param fromTree
	 * @return
	 */
	public boolean confirm(boolean fromTree) {
		return true;
	}

	/**
	 * row
	 * targetObjectBase
	 * selectedObjectBase
	 * @return
	 */
	public GisObjectBase confirm(int row, GisObjectBase targetGisObjectBase, GisObjectBase selectedGisObjectBase) {
		return null;
	}

	/**
	 * @param file
	 * @return
	 */
	public boolean uses_this_file(File file) {
		return false;
	}

	/**
	 * @param srcPath
	 * @param destPath
	 */
	public void move_file(File srcPath, File destPath) {
	}

	/**
	 * @param srcPath
	 * @param destPath
	 */
	public void update_file(File srcPath, File destPath) {
	}

	/**
	 * 
	 */
	public void refresh() {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.undo.UndoManager)
	 */
	@Override
	public void on_changed(UndoManager undoManager) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.text.AbstractDocument.DefaultDocumentEvent, javax.swing.undo.UndoManager)
	 */
	@Override
	public void on_changed(DefaultDocumentEvent defaultDocumentEvent,UndoManager undoManager) {
	}

	/**
	 * 
	 */
	public void select_at_first() {
		_gisVariableTableBase.select_at_first();
	}

	/**
	 * @return
	 */
	public boolean is_empty() {
		return _gisVariableTableBase.is_empty();
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(Writer writer) throws SAXException {
		return _gisVariableTableBase.write(writer);
	}
}
