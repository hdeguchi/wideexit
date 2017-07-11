/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.base;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

import org.xml.sax.SAXException;

import soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1;
import soars.application.visualshell.common.menu.basic1.RemoveAction;
import soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2;
import soars.application.visualshell.common.menu.basic2.SelectAllAction;
import soars.application.visualshell.common.swing.TableBase;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTable;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class GisVariableTableBase extends TableBase implements IBasicMenuHandler1, IBasicMenuHandler2 {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private Map<String, List<GisObjectBase>> __gisObjectBasesMap = null;

	/**
	 * 
	 */
	protected String _kind = "";

	/**
	 * 
	 */
	protected Map<String, GisPropertyPanelBase> _gisPropertyPanelBaseMap = null;

	/**
	 * 
	 */
	protected GisPropertyPanelBase _gisPropertyPanelBase = null;

	/**
	 * 
	 */
	private JMenuItem _removeMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _selectAllMenuItem = null;

	/**
	 * 
	 */
	protected int _columns = 0;

	/**
	 * 
	 */
	protected int _priviousRow = 0;

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
			if ( null != __gisObjectBasesMap)
				return;

			__gisObjectBasesMap = new HashMap<String, List<GisObjectBase>>();
			for ( String name:Constant._propertyPanelBases)
				__gisObjectBasesMap.put( name, new ArrayList<GisObjectBase>());
			for ( String name:Constant._exceptionalPropertyPanelBases)
				__gisObjectBasesMap.put( name, new ArrayList<GisObjectBase>());
		}
	}

	/**
	 * 
	 */
	static public void clear() {
		Collection<List<GisObjectBase>> gisObjectBasesList = __gisObjectBasesMap.values();
		for ( List<GisObjectBase> gisObjectBases:gisObjectBasesList)
			gisObjectBases.clear();
	}

	/**
	 * @param kind
	 * @param gisPropertyPanelBaseMap
	 * @param gisPropertyPanelBase
	 * @param columns
	 * @param owner
	 * @param parent
	 */
	public GisVariableTableBase(String kind, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, GisPropertyPanelBase gisPropertyPanelBase, int columns, Frame owner, Component parent) {
		super(owner, parent);
		_kind = kind;
		_gisPropertyPanelBaseMap = gisPropertyPanelBaseMap;
		_gisPropertyPanelBase = gisPropertyPanelBase;
		_columns = columns;
	}

	/**
	 * @return
	 */
	public boolean setup() {
		return setup( true);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.swing.TableBase#setup(boolean)
	 */
	public boolean setup(boolean popupMenu) {
		if ( !super.setup(popupMenu))
			return false;

		getTableHeader().setReorderingAllowed( false);
		setDefaultEditor( Object.class, null);
		setFillsViewportHeight( true);

		return true;
	}

	/**
	 * 
	 */
	public void select_at_first() {
		if ( 0 < getRowCount()) {
			setRowSelectionInterval( 0, 0);
			_gisPropertyPanelBase.changeSelection( ( GisObjectBase)getValueAt( 0, 0));
		}
	}

	/**
	 * @param name
	 * @return
	 */
	public boolean contains(String name) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			GisObjectBase gisObjectBase = ( GisObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == gisObjectBase)
				continue;

			if ( name.equals( gisObjectBase._name))
				return true;
		}
		return false;
	}

	/**
	 * @param kind
	 * @param name
	 * @return
	 */
	public boolean other_objectBase_has_this_name(String kind, String name) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			GisObjectBase gisObjectBase = ( GisObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == gisObjectBase || gisObjectBase._kind.equals( kind))
				continue;

			if ( name.equals( gisObjectBase._name))
				return true;
		}
		return false;
	}

	/**
	 * @param name
	 * @param number
	 * @return
	 */
	public boolean contains(String name, String number) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			GisObjectBase gisObjectBase = ( GisObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == gisObjectBase)
				continue;

			if ( SoarsCommonTool.has_same_name( name, number, gisObjectBase._name))
				return true;
		}
		return false;
	}

	/**
	 * @param gisObjectBase
	 * @return
	 */
	public boolean contains(GisObjectBase gisObjectBase) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			GisObjectBase ob = ( GisObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( ob.contains( gisObjectBase)) {
				String[] message = new String[] {
//					( ( _entityBase instanceof AgentObject) ? "Agent" : "Spot"),
//					"name = " + _entityBase._name,
					gisObjectBase._kind + " " + ob._name + " uses " + gisObjectBase._name
				};

				WarningManager.get_instance().add( message);

				return true;
			}
		}
		return false;
	}

	/**
	 * @param type
	 * @param headName
	 * @param ranges
	 * @return
	 */
	public boolean can_adjust_name(String type, String headName, Vector<String[]> ranges) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			GisObjectBase gisObjectBase = ( GisObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == gisObjectBase)
				continue;

			if ( !gisObjectBase.can_adjust_name( type, headName, ranges/*, _entityBase*/))
				return false;
		}
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
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			GisObjectBase gisObjectBase = ( GisObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == gisObjectBase)
				continue;

			if ( !gisObjectBase.can_adjust_name( type, headName, ranges, newHeadName, newRanges/*, _entityBase*/))
				return false;
		}
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
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			GisObjectBase gisObjectBase = ( GisObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == gisObjectBase)
				continue;

			gisObjectBase.update_name_and_number( type, newName, originalName, headName, ranges, newHeadName, newRanges);
		}
	}

	/**
	 * @param type
	 * @param name
	 * @param newName
	 * @return
	 */
	public boolean update_object_name(String type, String name, String newName) {
		boolean result = false;
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			GisObjectBase gisObjectBase = ( GisObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == gisObjectBase)
				continue;

			if ( gisObjectBase.update_object_name( type, name, newName))
				result = true;
		}
		return result;
	}

	/**
	 * @return
	 */
	public String[] get() {
		List<String> names = new ArrayList<String>();
		for ( int i = 0; i < getRowCount(); ++i) {
			GisObjectBase gisObjectBase = ( GisObjectBase)getValueAt( i, 0);
			if ( null == gisObjectBase)
				continue;

			names.add( gisObjectBase._name);
		}
		return names.toArray( new String[ 0]);
	}

	/**
	 * @param kind
	 * @return
	 */
	public String[] get(String kind) {
		List<String> names = new ArrayList<String>();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			GisObjectBase gisObjectBase = ( GisObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == gisObjectBase)
				continue;

			if ( GisObjectBase.is_target( kind, gisObjectBase))
				names.add( gisObjectBase._name);
		}
		return names.toArray( new String[ 0]);
	}

	/**
	 * @param kind
	 * @param name
	 * @return
	 */
	public GisObjectBase get(String kind, String name) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			GisObjectBase gisObjectBase = ( GisObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == gisObjectBase)
				continue;

			if ( GisObjectBase.is_target( kind, gisObjectBase) && gisObjectBase._name.equals( name))
				return gisObjectBase;
		}
		return null;
	}

	/**
	 * @param gisObjectBase
	 * @return
	 */
	public int get(GisObjectBase gisObjectBase) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			GisObjectBase gob = ( GisObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == gob)
				continue;

			if ( gob.equals( gisObjectBase))
				return i;
		}
		return -1;
	}

	/**
	 * @param gisObjectBases
	 */
	public void get(List<GisObjectBase> gisObjectBases) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i)
			gisObjectBases.add( ( GisObjectBase)defaultTableModel.getValueAt( i, 0));
	}

	/**
	 * @param objectMap
	 * @param defaultTableModel
	 * @return
	 */
	protected boolean setup(TreeMap<String, Object> objectMap, DefaultTableModel defaultTableModel) {
		Collection<Object> objects = objectMap.values();
		for ( Object object:objects) {
			GisObjectBase gob = GisObjectBase.create( ( GisObjectBase)object);
			if ( null == gob)
				return false;

			insert( gob, defaultTableModel, false);
		}
		return true;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#setup_popup_menu()
	 */
	@Override
	protected void setup_popup_menu() {
		_userInterface = new UserInterface();
		_popupMenu = new JPopupMenu();

		_removeMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.remove.menu"),
			new RemoveAction( ResourceManager.get_instance().get( "common.popup.menu.remove.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.remove.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.remove.stroke"));

//		_popupMenu.addSeparator();
//
//		_copyMenuItem = _userInterface.append_popup_menuitem(
//			_popupMenu,
//			ResourceManager.get_instance().get( "common.popup.menu.copy.menu"),
//			new CopyAction( ResourceManager.get_instance().get( "common.popup.menu.copy.menu"), this),
//			ResourceManager.get_instance().get( "common.popup.menu.copy.mnemonic"),
//			ResourceManager.get_instance().get( "common.popup.menu.copy.stroke"));
//		_pasteMenuItem = _userInterface.append_popup_menuitem(
//			_popupMenu,
//			ResourceManager.get_instance().get( "common.popup.menu.paste.menu"),
//			new PasteAction( ResourceManager.get_instance().get( "common.popup.menu.paste.menu"), this),
//			ResourceManager.get_instance().get( "common.popup.menu.paste.mnemonic"),
//			ResourceManager.get_instance().get( "common.popup.menu.paste.stroke"));

		_popupMenu.addSeparator();

		_selectAllMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.select.all.menu"),
			new SelectAllAction( ResourceManager.get_instance().get( "common.popup.menu.select.all.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.select.all.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.select.all.stroke"));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.swing.TableBase#setup_key_event()
	 */
	@Override
	protected void setup_key_event() {
		super.setup_key_event();

		Action deleteAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_remove( null);
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0), "delete");
		getActionMap().put( "delete", deleteAction);


		Action backSpaceAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_remove( null);
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, 0), "backspace");
		getActionMap().put( "backspace", backSpaceAction);


		Action copyAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_copy();
			}
		};
		//getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "copy");
		getActionMap().put( "copy", copyAction);


		Action cutAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_cut();
			}
		};
		//getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "cut");
		getActionMap().put( "cut", cutAction);


		Action pasteAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_paste();
			}
		};
		//getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "paste");
		getActionMap().put( "paste", pasteAction);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.swing.TableBase#on_mouse_right_up(java.awt.Point)
	 */
	@Override
	public void on_mouse_right_up(Point point) {
		if ( null == _userInterface)
			return;

		if ( 0 == getRowCount()) {
			_removeMenuItem.setEnabled( false);
//			_copyMenuItem.setEnabled( false);
			_selectAllMenuItem.setEnabled( false);
		} else {
			int row = rowAtPoint( point);
			int column = columnAtPoint( point);
			if ( ( 0 <= row && getRowCount() > row)
				&& ( 0 <= column && getColumnCount() > column)) {
				int[] rows = getSelectedRows();
				boolean contains = ( 0 <= Arrays.binarySearch( rows, row));
				_removeMenuItem.setEnabled( contains);
//				_copyMenuItem.setEnabled( contains);
			} else {
				_removeMenuItem.setEnabled( false);
//				_copyMenuItem.setEnabled( false);
			}

			_selectAllMenuItem.setEnabled( true);
		}

//		_pasteMenuItem.setEnabled( !__objectBasesMap.get( _kind).isEmpty());

		_popupMenu.show( this, point.x, point.y);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_redo(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_redo(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_undo(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_undo(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_copy(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_copy(ActionEvent actionEvent) {
		on_copy();
	}

	/**
	 * 
	 */
	protected void on_copy() {
		int[] rows = getSelectedRows();
		if ( 1 > rows.length)
			return;

		GisInitialValueTable.clear();
		/*__gisObjectBasesMap.get( _kind).*/clear();
		Arrays.sort( rows);
		for ( int i = 0; i < rows.length; ++i)
			__gisObjectBasesMap.get( _kind).add( GisObjectBase.create( ( GisObjectBase)getValueAt( rows[ i], 0)));
	}

	/**
	 * 
	 */
	protected void on_cut() {
		// これは無理！
		// 削除出来ないものが含まれているから
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_paste(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_paste(ActionEvent actionEvent) {
		on_paste();
	}

	/**
	 * 
	 */
	protected void on_paste() {
		WarningManager.get_instance().cleanup();

		// 存在しない変数を参照しているコレクション変数を省く
		List<GisObjectBase> gisObjectBases = new ArrayList<GisObjectBase>();
		for ( GisObjectBase gisObjectBase:__gisObjectBasesMap.get( _kind)) {
			GisPanelBase gisPanelBase = _gisPropertyPanelBase._gisPanelBaseMap.get( GisPropertyPanelBase.get_nameMap().get( gisObjectBase._kind));
			if ( null == gisPanelBase)
				continue;

			if ( !gisPanelBase.can_paste( gisObjectBase, __gisObjectBasesMap.get( _kind))) {
				String[] message = new String[] {
					GisPropertyPanelBase.get_nameMap().get( gisObjectBase._kind),
					"name = " + gisObjectBase._name
				};

				WarningManager.get_instance().add( message);
				continue;
			}

			gisObjectBases.add( gisObjectBase);
		}

		// １回目で張り付けられると判断されたものだけを対象にして再度行う
		// １回目で省かれたコレクション変数を参照しているコレクション変数が省かれる
		int option = -1;
		List<GisObjectBase> gobs = new ArrayList<GisObjectBase>();
		for ( GisObjectBase gisObjectBase:gisObjectBases) {
			GisPanelBase gisPanelBase = _gisPropertyPanelBase._gisPanelBaseMap.get( GisPropertyPanelBase.get_nameMap().get( gisObjectBase._kind));
			if ( null == gisPanelBase)
				continue;

			if ( !gisPanelBase.can_paste( gisObjectBase, gisObjectBases)) {
				String[] message = new String[] {
					GisPropertyPanelBase.get_nameMap().get( gisObjectBase._kind),
					"name = " + gisObjectBase._name
				};

				WarningManager.get_instance().add( message);
				continue;
			}

			GisObjectBase gob = get( gisObjectBase._kind, gisObjectBase._name);
			if ( null != gob)	// update
				if ( gob.equals( gisObjectBase)) {
					gobs.add( gob);
					continue;
				} else {
					if ( 3 == option)		// 全ていいえ
						continue;
					else if ( 2 == option) {		// 全てはい
						gob.copy( gisObjectBase);
						gobs.add( gob);
						continue;
					} else {
						option = showOptionDialog( gisObjectBase);
						if ( 0 == option || 2 == option) {
							gob.copy( gisObjectBase);
							gobs.add( gob);
						}
						continue;
					}
				}
			else {	// append
				gob = GisObjectBase.create( gisObjectBase);
				append( gob);
				gobs.add( gob);
			}
		}

		List<Integer> list = new ArrayList<Integer>();
		for ( GisObjectBase gisObjectBase:gobs) {
			for ( int i = 0; i < getRowCount(); ++i) {
				if ( gisObjectBase == getValueAt( i, 0)) {
					list.add( i);
					break;
				}
			}
		}
		select( Tool.get_array( list));

		if ( !WarningManager.get_instance().isEmpty()) {
			WarningDlg1 warningDlg1 = new WarningDlg1(
				_owner,
				ResourceManager.get_instance().get( "warning.dialog1.title"),
				ResourceManager.get_instance().get( "warning.dialog1.message6"),
				_parent);
			warningDlg1.do_modal();
		}
	}

	/**
	 * @param indices
	 */
	private void select(int[] indices) {
		if ( 0 == indices.length)
			return;

		clearSelection();
		for ( int index:indices) {
			addRowSelectionInterval( index, index);
			Rectangle rect = getCellRect( index, 0, true);
			scrollRectToVisible( rect);
		}

		if ( 1 == indices.length) {
			_priviousRow = indices[ 0];
			_gisPropertyPanelBase.changeSelection( ( GisObjectBase)getValueAt( indices[ 0], 0));
		} else {
			_priviousRow = -1;
			_gisPropertyPanelBase.changeSelection( null);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_select_all(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_select_all(ActionEvent actionEvent) {
		selectAll();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_deselect_all(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_deselect_all(ActionEvent actionEvent) {
	}

	/**
	 * @param gisObjectBase
	 * @return
	 */
	private int showOptionDialog(GisObjectBase gisObjectBase) {
		String[] overwrite_options = new String[] {
			ResourceManager.get_instance().get( "dialog.yes"),
			ResourceManager.get_instance().get( "dialog.no"),
			ResourceManager.get_instance().get( "dialog.yes.to.all"),
			ResourceManager.get_instance().get( "dialog.no.to.all")};
		return JOptionPane.showOptionDialog(
			getParent(),
			GisPropertyPanelBase.get_nameMap().get( gisObjectBase._kind) + " : " + gisObjectBase._name + "\n"
				+ ResourceManager.get_instance().get( "edit.object.dialog.variable.table.confirm.overwrite.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.INFORMATION_MESSAGE,
			null,
			overwrite_options,
			overwrite_options[ 0]);
	}

	/**
	 * @param gisObjectBase
	 */
	public void append(GisObjectBase gisObjectBase) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		insert( gisObjectBase, defaultTableModel, true);
	}

	/**
	 * @param row
	 * @param originalGisObjectBase
	 * @param selection
	 */
	public void update(int row, GisObjectBase originalGisObjectBase, boolean selection) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		GisObjectBase gisObjectBase = ( GisObjectBase)defaultTableModel.getValueAt( row, 0);
		if ( null == gisObjectBase)
			return;

		if ( !gisObjectBase._name.equals( originalGisObjectBase._name)) {
			defaultTableModel.removeRow( row);
			insert( gisObjectBase, defaultTableModel, selection);
			clear();	// 名前が変わったからキャッシュをクリア
			GisInitialValueTable.clear();
		}

		repaint();
	}

	/**
	 * @param gisObjectBase
	 * @param defaultTableModel
	 * @param selection
	 */
	private void insert(GisObjectBase gisObjectBase, DefaultTableModel defaultTableModel, boolean selection) {
		Object[] objects = new Object[ _columns];
		for ( int i = 0; i < objects.length; ++i)
			objects[ i] = gisObjectBase;

		int index = 0;
		boolean insert = false;
		if ( 0 == defaultTableModel.getRowCount()) {
			defaultTableModel.addRow( objects);
			insert = true;
		} else {
			for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
				GisObjectBase gob = ( GisObjectBase)defaultTableModel.getValueAt( i, 0);
				if ( null == gob)
					return;

				if ( 0 > compare( gisObjectBase, gob)) {
					defaultTableModel.insertRow( i, objects);
					index = i;
					insert = true;
					break;
				}
			}
		}

		if ( !insert) {
			defaultTableModel.addRow( objects);
			index = defaultTableModel.getRowCount() - 1;
		}

		if ( selection) {
			setRowSelectionInterval( index, index);
			Rectangle rect = getCellRect( index, 0, true);
			scrollRectToVisible( rect);
			_gisPropertyPanelBase.changeSelection( gisObjectBase);
			_priviousRow = index;
		}
	}

	/**
	 * @param gisObjectBase0
	 * @param gisObjectBase1
	 * @return
	 */
	protected int compare(GisObjectBase gisObjectBase0, GisObjectBase gisObjectBase1) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTable#on_remove(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_remove(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		if ( 1 > rows.length)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount())
			return;

		WarningManager.get_instance().cleanup();

		boolean result = true;
		for ( int i = 0; i < rows.length; ++i) {
			GisObjectBase gisObjectBase = ( GisObjectBase)defaultTableModel.getValueAt( rows[ i], 0);
			if ( !can_remove_selected_objects( gisObjectBase))
				result = false;
		}

		if ( !result) {
			if ( !WarningManager.get_instance().isEmpty()) {
				WarningDlg1 warningDlg1 = new WarningDlg1(
					_owner,
					ResourceManager.get_instance().get( "warning.dialog1.title"),
					ResourceManager.get_instance().get( "warning.dialog1.message2"),
					_parent);
				warningDlg1.do_modal();
			}
			return;
		}

		if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
			_parent,
			ResourceManager.get_instance().get( "edit.object.dialog.variable.table.confirm.remove.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION)) {

			Arrays.sort( rows);
			for ( int i = rows.length - 1; i >= 0; --i)
				defaultTableModel.removeRow( rows[ i]);

			if ( 0 == getRowCount()) {
				_gisPropertyPanelBase.changeSelection( null);
				_priviousRow = 0;
			} else {
				int select = ( rows[ 0] < defaultTableModel.getRowCount()) ? rows[ 0] : defaultTableModel.getRowCount() - 1;
				setRowSelectionInterval( select, select);
				_priviousRow = select;
				_gisPropertyPanelBase.changeSelection( ( GisObjectBase)getValueAt( select, 0));
			}

			on_remove();

			clear();	// 削除されたからキャッシュをクリア
			GisInitialValueTable.clear();
		}
	}

	/**
	 * @param gisObjectBase
	 * @return
	 */
	protected boolean can_remove_selected_objects(GisObjectBase gisObjectBase) {
		return true;
//		boolean result1 = can_remove_selected_objects_old( gisObjectBase);
//		boolean result2 = can_remove_selected_objects_new( gisObjectBase);
//		return ( result1 && result2);
	}

//	/**
//	 * @param gisObjectBase
//	 * @return
//	 */
//	protected boolean can_remove_selected_objects_old(GisObjectBase gisObjectBase) {
//		// TODO 従来のもの
//		if ( ( _entityBase instanceof SpotObject) || ( ( _entityBase instanceof AgentObject)
//			&& !LayerManager.get_instance().other_agents_have_same_object_name( gisObjectBase._kind, gisObjectBase._name, ( AgentObject)_entityBase))) {
//			if ( _entityBase._gis.equals( "")) {
//				boolean otherSpotsHaveSameObjectName
//					= ( _entityBase instanceof SpotObject)
//							? LayerManager.get_instance().other_spots_have_same_object_name( gisObjectBase._kind, gisObjectBase._name, ( SpotObject)_entityBase)
//							: false;
//
//				String headName = null;
//				Vector<String[]> ranges = null;
//				if ( _entityBase instanceof SpotObject) {
//					headName = SoarsCommonTool.separate( _entityBase._name);
//					String headNumber = _entityBase._name.substring( headName.length());
//					ranges = SoarsCommonTool.get_ranges( headNumber, _entityBase._number);
//				}
//
//				if ( !LayerManager.get_instance().can_remove(
//					gisObjectBase._kind, gisObjectBase._name, otherSpotsHaveSameObjectName, headName, ranges, false))
//					return false;
//			} else {
//				// GISスポットの場合
//				List<EntityBase> entityBases = new ArrayList<EntityBase>();
//				LayerManager.get_instance().get_gis_spots( entityBases, _entityBase._gis);
//				for ( EntityBase entityBase:entityBases) {
//					if ( entityBase.contains_as_initial_value( gisObjectBase))
//						return false;
//				}
//
//				boolean otherSpotsHaveSameObjectName
//					= ( _entityBase instanceof SpotObject)
//							? LayerManager.get_instance().other_spots_have_same_object_name( gisObjectBase._kind, gisObjectBase._name, _entityBase._gis, ( SpotObject)_entityBase)
//							: false;
//
//				for ( EntityBase entityBase:entityBases) {
//					String headName = null;
//					Vector<String[]> ranges = null;
//					if ( entityBase instanceof SpotObject) {
//						headName = SoarsCommonTool.separate( entityBase._name);
//						String headNumber = entityBase._name.substring( headName.length());
//						ranges = SoarsCommonTool.get_ranges( headNumber, entityBase._number);
//					}
//					if ( !LayerManager.get_instance().can_remove(
//						gisObjectBase._kind, gisObjectBase._name, otherSpotsHaveSameObjectName, headName, ranges, false))
//						return false;
//				}
//			}
//		}
//
//		return true;
//	}
//
//	/**
//	 * @param gisObjectBase
//	 * @return
//	 */
//	protected boolean can_remove_selected_objects_new(GisObjectBase gisObjectBase) {
//		// TODO これからはこちらに移行してゆく
//		if ( ( _entityBase instanceof SpotObject) || ( _entityBase instanceof AgentObject)) {
//			String entity = null;
//			if ( _entityBase instanceof AgentObject)
//				entity = "agent";
//			else if ( _entityBase instanceof SpotObject)
//				entity = "spot";
//			else
//				return false;
//
//			if ( _entityBase._gis.equals( "")) {
//				boolean otherGisObjectBasesHaveSameObjectName
//					= ( entity.equals( "agent")
//							? LayerManager.get_instance().other_agents_have_same_object_name( gisObjectBase._kind, gisObjectBase._name, ( AgentObject)_entityBase)
//							: LayerManager.get_instance().other_spots_have_same_object_name( gisObjectBase._kind, gisObjectBase._name, ( SpotObject)_entityBase));
//
//				String headName = SoarsCommonTool.separate( _entityBase._name);
//				String headNumber = _entityBase._name.substring( headName.length());
//				Vector<String[]> ranges = SoarsCommonTool.get_ranges( headNumber, _entityBase._number);
//
//				if ( !LayerManager.get_instance().can_remove(
//					entity,
//					gisObjectBase._kind,
//					objectBase._name,
//					otherGisObjectBasesHaveSameObjectName,
//					headName,
//					ranges,
//					false))
//					return false;
//			} else {
//				// GISスポットの場合
//				List<EntityBase> entityBases = new ArrayList<EntityBase>();
//				LayerManager.get_instance().get_gis_spots( entityBases, _entityBase._gis);
//				for ( EntityBase entityBase:entityBases) {
//					if ( entityBase.contains_as_initial_value( gisObjectBase))
//						return false;
//				}
//
//				boolean otherGisObjectBasesHaveSameObjectName
//					= ( entity.equals( "agent")
//							? LayerManager.get_instance().other_agents_have_same_object_name( gisObjectBase._kind, gisObjectBase._name, ( AgentObject)_entityBase)
//							: LayerManager.get_instance().other_spots_have_same_object_name( gisObjectBase._kind, gisObjectBase._name, _entityBase._gis, ( SpotObject)_entityBase));
//
//				for ( EntityBase entityBase:entityBases) {
//					String headName = SoarsCommonTool.separate( entityBase._name);
//					String headNumber = entityBase._name.substring( headName.length());
//					Vector<String[]> ranges = SoarsCommonTool.get_ranges( headNumber, entityBase._number);
//					
//					if ( !LayerManager.get_instance().can_remove(
//						entity,
//						gisObjectBase._kind,
//						gisObjectBase._name,
//						otherGisObjectBasesHaveSameObjectName,
//						headName,
//						ranges,
//						false))
//						return false;
//				}
//			}
//		}
//
//		return true;
//	}

	/**
	 * 
	 */
	protected void on_remove() {
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		super.valueChanged(e);
		int[] rows = getSelectedRows();
		if ( 1 != rows.length) {
			_gisPropertyPanelBase.changeSelection( null);
			_priviousRow = -1;
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#changeSelection(int, int, boolean, boolean)
	 */
	@Override
	public void changeSelection(int arg0, int arg1, boolean arg2, boolean arg3) {
		// ここがキモ！
		//System.out.println( _priviousRow + ", " + arg0);
		if ( arg3) {
			// 選択が増加する場合
			int[] rows = getSelectedRows();
			if ( 1 == rows.length && rows[ 0] == arg0) {	// 実は選択は１つ
				_gisPropertyPanelBase.changeSelection( ( GisObjectBase)getValueAt( arg0, 0));
				_priviousRow = arg0;
			} else {	// これから選択が２つ以上になる
				_gisPropertyPanelBase.changeSelection( null);
				_priviousRow = -1;
			}
			super.changeSelection(arg0, arg1, arg2, arg3);
		} else if ( arg2) {
			// 選択が減少する場合
			int[] rows = getSelectedRows();
			if ( 2 == rows.length) {	// これから選択が１つになる
				_gisPropertyPanelBase.changeSelection( ( GisObjectBase)getValueAt( ( arg0 == rows[ 0]) ? rows[ 1] : arg0, 0));
				_priviousRow = ( arg0 == rows[ 0]) ? rows[ 1] : arg0;
			} else {	// この選択が解除されても２つ以上が選択された状態となる
				_gisPropertyPanelBase.changeSelection( null);
				_priviousRow = -1;
			}
			super.changeSelection(arg0, arg1, arg2, arg3);
		} else {
			GisObjectBase gisObjectBase = ( 0 > _priviousRow) ? null : ( GisObjectBase)getValueAt( _priviousRow, 0);
			gisObjectBase = ( GisObjectBase)_gisPropertyPanelBase.confirm( _priviousRow, gisObjectBase, ( GisObjectBase)getValueAt( arg0, 0));
			if ( null == gisObjectBase) {
				// 選択状態を変えない=super.changeSelection( ... )を呼ばない
				//System.out.println( "debug3 : " + _priviousRow + ", " + arg0 + ", " + arg2 + ", " + arg3);
				return;
			} else {
				int index = getIndex( gisObjectBase);
				if ( 0 > index) {
					// これは起こり得ない筈だが念の為
					_gisPropertyPanelBase.changeSelection( ( GisObjectBase)getValueAt( arg0, 0));
					_priviousRow = arg0;
					super.changeSelection(arg0, arg1, arg2, arg3);
					//System.out.println( "debug4 : " + _priviousRow + ", " + arg0 + ", " + arg2 + ", " + arg3);
				} else {
					// 返されたオブジェクトを選択する
					_gisPropertyPanelBase.changeSelection( gisObjectBase);
					_priviousRow = index;
					super.changeSelection(index, arg1, arg2, arg3);
					//System.out.println( "debug5 : " + _priviousRow + ", " + arg0 + ", " + arg2 + ", " + arg3);
				}
			}
		}
//		super.changeSelection(arg0, arg1, arg2, arg3);
//		int[] rows = getSelectedRows();
//		_variablePropertyPanelBase.changeSelection( ( null == rows || 1 != rows.length) ? null : ( ObjectBase)getValueAt( arg0, 0));
	}

	/**
	 * @param gisObjectBase
	 */
	public void select(GisObjectBase gisObjectBase) {
		int index = getIndex( gisObjectBase);
		if ( 0 > index)
			return;	// これは起こり得ない筈だが念の為

		setRowSelectionInterval( index, index);
		_priviousRow = index;
		_gisPropertyPanelBase.changeSelection( gisObjectBase);
	}

	/**
	 * @param gisObjectBase
	 * @return
	 */
	protected int getIndex(GisObjectBase gisObjectBase) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			GisObjectBase gob = ( GisObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == gob)
				continue;

			if ( gob.equals( gisObjectBase))
				return i;
		}
		return -1;
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		if ( 0 < getRowCount()) {
			setRowSelectionInterval( 0, 0);
			_gisPropertyPanelBase.changeSelection( ( GisObjectBase)getValueAt( 0, 0));
		}
	}

//	/**
//	 * 
//	 */
//	public void on_ok() {
//	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_append(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_append(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_edit(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_edit(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_insert(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_insert(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_cut(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_cut(ActionEvent actionEvent) {
	}

	/**
	 * @return
	 */
	public boolean is_empty() {
		return ( 0 == getRowCount());
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(Writer writer) throws SAXException {
		for ( int row = 0; row < getRowCount(); ++row) {
			GisObjectBase gisObjectBase = ( GisObjectBase)getValueAt( row, 0);
			if ( !gisObjectBase.write( writer))
				return false;
		}

		return true;
	}
}
