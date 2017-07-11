/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;
import soars.application.visualshell.object.gis.edit.panel.base.GisVariableTableBase;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.GisVariablePanelBase;
import soars.application.visualshell.object.gis.object.base.GisInitialValueBase;
import soars.common.utility.swing.table.base.StandardTableHeaderRenderer;

/**
 * @author kurata
 *
 */
public class GisInitialValueTable extends GisInitialValueTableBase {

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
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static protected Map<String, List<GisInitialValueBase>> __gisInitialValueBasesMap = null;

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
			create__initialValueBasesMap();
		}
	}

	/**
	 * 
	 */
	private static void create__initialValueBasesMap() {
		if ( null != __gisInitialValueBasesMap)
			return;

		__gisInitialValueBasesMap = new HashMap<String, List<GisInitialValueBase>>();
		__gisInitialValueBasesMap.put( "variable", new ArrayList<GisInitialValueBase>());
		__gisInitialValueBasesMap.put( "map", new ArrayList<GisInitialValueBase>());
		__gisInitialValueBasesMap.put( "exchange algebra", new ArrayList<GisInitialValueBase>());
	}

	/**
	 * 
	 */
	static public void clear() {
		Collection<List<GisInitialValueBase>> initialValuesBasesList = __gisInitialValueBasesMap.values();
		for ( List<GisInitialValueBase> gisInitialValueBases:initialValuesBasesList)
			gisInitialValueBases.clear();
	}

	/**
	 * @param kind
	 * @param color
	 * @param gisDataManager
	 * @param gisPropertyPanelBaseMap
	 * @param gisVariablePanelBase
	 * @param owner
	 * @param parent
	 */
	public GisInitialValueTable(String kind, Color color, GisDataManager gisDataManager, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, GisVariablePanelBase gisVariablePanelBase, Frame owner, Component parent) {
		super(color, gisDataManager, gisVariablePanelBase, owner, parent);
		_kind = kind;
		_gisPropertyPanelBaseMap = gisPropertyPanelBaseMap;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#setup(soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase)
	 */
	@Override
	public boolean setup(GisInitialValueTableBase gisInitialValueTableBase) {
		if ( !super.setup(gisInitialValueTableBase))
			return false;

		setAutoResizeMode( AUTO_RESIZE_OFF);

		JTableHeader tableHeader = getTableHeader();
		StandardTableHeaderRenderer standardTableHeaderRenderer = new StandardTableHeaderRenderer();

		tableHeader.setDefaultRenderer( standardTableHeaderRenderer);

		return true;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#changeSelection(int, int, boolean, boolean)
	 */
	@Override
	public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
		super.changeSelection(rowIndex, columnIndex, toggle, extend);
		int[] rows = getSelectedRows();
		changeSelection( rows, rowIndex);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#changeSelection(int[], int)
	 */
	@Override
	public void changeSelection(int[] rows, int rowIndex) {
		_gisVariablePanelBase.changeSelection( ( null == rows || 1 != rows.length) ? null : getValueAt( rowIndex, 0));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#on_remove()
	 */
	@Override
	public void on_remove() {
		_gisInitialValueTableBase.on_remove();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#on_remove(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_remove(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		if ( 1 > rows.length)
			return;

		if ( 0 == getRowCount())
			return;

		if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
			_parent,
			ResourceManager.get_instance().get( "edit.object.dialog.variable.initial.value.table.confirm.remove.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION))
			remove( rows);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#remove(int[])
	 */
	@Override
	public void remove(int[] rows) {
		Arrays.sort( rows);
		for ( int i = rows.length - 1; i >= 0; --i) {
			removeRow( rows[ i]);
			_gisInitialValueTableBase.removeRow( rows[ i]);
		}

		if ( 0 < getRowCount()) {
			int row = ( ( rows[ 0] < getRowCount()) ? rows[ 0] : getRowCount() - 1);
			select( row);
			changeSelection();
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#changeSelection()
	 */
	@Override
	public void changeSelection() {
		int[] rows = getSelectedRows();
		_gisVariablePanelBase.changeSelection( ( null == rows || 1 != rows.length) ? null : getValueAt( rows[ 0], 0));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#on_copy()
	 */
	@Override
	public void on_copy() {
		_gisInitialValueTableBase.on_copy();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#copy(int[])
	 */
	@Override
	public void copy(int[] rows) {
		GisVariableTableBase.clear();
		/*__initialValueBasesMap.get( kind).*/clear();
		Arrays.sort( rows);
		for ( int i = 0; i < rows.length; ++i)
			__gisInitialValueBasesMap.get( _kind).add( GisInitialValueBase.create( ( GisInitialValueBase)getValueAt( rows[ i], 0), rows[ i] - rows[ 0]));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#on_cut()
	 */
	@Override
	public void on_cut() {
		_gisInitialValueTableBase.on_cut();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#cut(int[])
	 */
	@Override
	public void cut(int[] rows) {
		copy( rows);
		_gisInitialValueTableBase.remove( rows);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#can_paste(java.awt.Point)
	 */
	@Override
	protected boolean can_paste(Point point) {
		if ( 0 == getRowCount())
			return !__gisInitialValueBasesMap.get( _kind).isEmpty();

		int[] rows = getSelectedRows();
		if ( null == rows || 1 != rows.length)
			return false;

		int row = rowAtPoint( point);
		int column = columnAtPoint( point);
		if ( ( 0 <= row && getRowCount() > row)
			&& ( 0 <= column && getColumnCount() > column))
			return ( 0 <= Arrays.binarySearch( rows, row)
				&& !__gisInitialValueBasesMap.get( _kind).isEmpty());
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#on_paste()
	 */
	@Override
	public void on_paste() {
		_gisInitialValueTableBase.on_paste();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#on_select_all(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_select_all(ActionEvent actionEvent) {
		_gisInitialValueTableBase.selectAll();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#append(java.lang.Object[])
	 */
	@Override
	public void append(Object[] objects) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.addRow( objects);
		_gisInitialValueTableBase.append();
		select( getRowCount() - 1);
		scroll( getRowCount() - 1);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#insert(java.lang.Object[])
	 */
	@Override
	public void insert(Object[] objects) {
		int[] rows = _gisInitialValueTableBase.getSelectedRows();
		if ( 1 != rows.length)
			return;

		insert( objects, rows[ 0]);
	}

	/**
	 * @param objects
	 * @param row
	 */
	protected void insert(Object[] objects, int row) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		_gisInitialValueTableBase.insert( row);
		defaultTableModel.insertRow( row, objects);
		select( row);
		scroll( row);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#update(java.lang.Object[])
	 */
	@Override
	public void update(Object[] objects) {
		int[] rows = _gisInitialValueTableBase.getSelectedRows();
		if ( 1 != rows.length)
			return;

		for ( int i = 0; i < objects.length; ++i)
			setValueAt( objects[ i], rows[ 0], i);

		clearSelection();
		addRowSelectionInterval( rows[ 0], rows[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#up()
	 */
	@Override
	public void up() {
		_gisInitialValueTableBase.up();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#down()
	 */
	@Override
	public void down() {
		_gisInitialValueTableBase.down();
	}
}
