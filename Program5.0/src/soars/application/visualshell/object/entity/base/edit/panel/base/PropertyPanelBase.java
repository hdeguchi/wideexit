/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.base;

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

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.object.base.ObjectBase;
import soars.common.utility.swing.panel.StandardPanel;
import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextUndoRedoManager;

/**
 * @author kurata
 *
 */
public class PropertyPanelBase extends StandardPanel implements ITextUndoRedoManagerCallBack {

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
	protected EntityBase _entityBase = null;

	/**
	 * 
	 */
	protected Map<String, PropertyPanelBase> _propertyPanelBaseMap = null;

	/**
	 * 
	 */
	public int _index = 0;

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
	public Map<String, PanelBase> _panelBaseMap = new HashMap<String, PanelBase>();

	/**
	 * 
	 */
	static public Map<String, String> _nameMap = null;

	/**
	 * 
	 */
	static public Map<String, Color> _colorMap = null;

	/**
	 * 
	 */
	static {
		startup();
	}

	/**
	 * 
	 */
	private static void startup() {
		synchronized( _lock) {
			if ( null == _nameMap) {
				_nameMap = new HashMap<String, String>();
//				_nameMap.put( "probability", ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"));
				_nameMap.put( "keyword", ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"));
				_nameMap.put( "number object", ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"));
				_nameMap.put( "role variable", ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable"));
				_nameMap.put( "time variable", ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"));
				_nameMap.put( "spot variable", ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"));
				_nameMap.put( "collection", ResourceManager.get_instance().get( "edit.object.dialog.tree.collection"));
				_nameMap.put( "list", ResourceManager.get_instance().get( "edit.object.dialog.tree.list"));
				_nameMap.put( "map", ResourceManager.get_instance().get( "edit.object.dialog.tree.map"));
				_nameMap.put( "exchange algebra", ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra"));
				_nameMap.put( "extransfer", ResourceManager.get_instance().get( "edit.object.dialog.tree.extransfer"));
				_nameMap.put( "class variable", ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"));
				_nameMap.put( "file", ResourceManager.get_instance().get( "edit.object.dialog.tree.file"));
				_nameMap.put( "initial data file", ResourceManager.get_instance().get( "edit.object.dialog.tree.initial.data.file"));
			}

			if ( null == _colorMap) {
				_colorMap = new HashMap<String, Color>();
//				_colorMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"), new Color( 200, 0, 200));
				_colorMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"), new Color( 0, 128, 128));
				_colorMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"), new Color( 0, 0, 128));
				_colorMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable"), new Color( 0, 64, 128));
				_colorMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"), new Color( 128, 0, 0));
				_colorMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"), new Color( 240, 0, 0));
				_colorMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.collection"), new Color( 128, 128, 0));
				_colorMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.list"), new Color( 64, 128, 0));
				_colorMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.map"), new Color( 0, 160, 0));
				_colorMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra"), new Color( 0, 0, 200));
			}
		}
	}

	/**
	 * @param title
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public PropertyPanelBase(String title, EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, int index, Frame owner, Component parent) {
		super();
		_title = title;
		_entityBase = entityBase;
		_propertyPanelBaseMap = propertyPanelBaseMap;
		_index = index;
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @param name
	 * @return
	 */
	public boolean contains(String name) {
		return false;
	}

	/**
	 * @param name
	 * @param number
	 * @return
	 */
	public boolean contains(String name, String number) {
		return false;
	}

	/**
	 * @param objectBase
	 * @return
	 */
	public boolean contains(ObjectBase objectBase) {
		return false;
	}

	/**
	 * @param type
	 * @param headName
	 * @param ranges
	 * @return
	 */
	public boolean can_adjust_name(String type, String headName, Vector<String[]> ranges) {
		return true;
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
		return true;
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
		return null;
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
	 * @param objectBase
	 */
	public void changeSelection(ObjectBase objectBase) {
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
	public ObjectBase confirm(int row, ObjectBase targetObjectBase, ObjectBase selectedObjectBase) {
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
}
