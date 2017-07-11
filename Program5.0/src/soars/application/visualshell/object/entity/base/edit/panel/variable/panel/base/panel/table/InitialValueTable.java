/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table;

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
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.VariableTableBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.VariablePanelBase;
import soars.application.visualshell.object.entity.base.object.base.InitialValueBase;
import soars.common.utility.swing.table.base.StandardTableHeaderRenderer;

/**
 * @author kurata
 *
 */
public class InitialValueTable extends InitialValueTableBase {

	/**
	 * 
	 */
	protected String _kind = "";

	/**
	 * 
	 */
	protected Map<String, PropertyPanelBase> _propertyPanelBaseMap = null;

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static protected Map<String, List<InitialValueBase>> __initialValueBasesMap = null;

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
		if ( null != __initialValueBasesMap)
			return;

		__initialValueBasesMap = new HashMap<String, List<InitialValueBase>>();
		__initialValueBasesMap.put( "variable", new ArrayList<InitialValueBase>());
		__initialValueBasesMap.put( "map", new ArrayList<InitialValueBase>());
		__initialValueBasesMap.put( "exchange algebra", new ArrayList<InitialValueBase>());
	}

	/**
	 * 
	 */
	static public void clear() {
		Collection<List<InitialValueBase>> initialValuesBasesList = __initialValueBasesMap.values();
		for ( List<InitialValueBase> InitialValueBases:initialValuesBasesList)
			InitialValueBases.clear();
	}

	/**
	 * @param kind
	 * @param color
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 * @param variablePanelBase
	 * @param owner
	 * @param parent
	 */
	public InitialValueTable(String kind, Color color, EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, VariablePanelBase variablePanelBase, Frame owner, Component parent) {
		super(color, entityBase, variablePanelBase, owner, parent);
		_kind = kind;
		_propertyPanelBaseMap = propertyPanelBaseMap;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#setup(soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase)
	 */
	@Override
	public boolean setup(InitialValueTableBase initialValueTableBase) {
		if ( !super.setup(initialValueTableBase))
			return false;

		setAutoResizeMode( AUTO_RESIZE_OFF);

		JTableHeader tableHeader = getTableHeader();
		StandardTableHeaderRenderer standardTableHeaderRenderer = new StandardTableHeaderRenderer();
		if ( _entityBase.is_multi())
			standardTableHeaderRenderer.setEnabled( false);

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
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#changeSelection(int[], int)
	 */
	@Override
	public void changeSelection(int[] rows, int rowIndex) {
		_variablePanelBase.changeSelection( ( null == rows || 1 != rows.length) ? null : getValueAt( rowIndex, 0));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#on_remove()
	 */
	@Override
	public void on_remove() {
		_initialValueTableBase.on_remove();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#on_remove(java.awt.event.ActionEvent)
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
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#remove(int[])
	 */
	@Override
	public void remove(int[] rows) {
		Arrays.sort( rows);
		for ( int i = rows.length - 1; i >= 0; --i) {
			removeRow( rows[ i]);
			_initialValueTableBase.removeRow( rows[ i]);
		}

		if ( 0 < getRowCount()) {
			int row = ( ( rows[ 0] < getRowCount()) ? rows[ 0] : getRowCount() - 1);
			select( row);
			changeSelection();
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#changeSelection()
	 */
	@Override
	public void changeSelection() {
		int[] rows = getSelectedRows();
		_variablePanelBase.changeSelection( ( null == rows || 1 != rows.length) ? null : getValueAt( rows[ 0], 0));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#on_copy()
	 */
	@Override
	public void on_copy() {
		_initialValueTableBase.on_copy();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#copy(int[])
	 */
	@Override
	public void copy(int[] rows) {
		VariableTableBase.clear();
		/*__initialValueBasesMap.get( kind).*/clear();
		Arrays.sort( rows);
		for ( int i = 0; i < rows.length; ++i)
			__initialValueBasesMap.get( _kind).add( InitialValueBase.create( ( InitialValueBase)getValueAt( rows[ i], 0), rows[ i] - rows[ 0]));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#on_cut()
	 */
	@Override
	public void on_cut() {
		_initialValueTableBase.on_cut();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#cut(int[])
	 */
	@Override
	public void cut(int[] rows) {
		copy( rows);
		_initialValueTableBase.remove( rows);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#can_paste(java.awt.Point)
	 */
	@Override
	protected boolean can_paste(Point point) {
		if ( 0 == getRowCount())
			return !__initialValueBasesMap.get( _kind).isEmpty();

		int[] rows = getSelectedRows();
		if ( null == rows || 1 != rows.length)
			return false;

		int row = rowAtPoint( point);
		int column = columnAtPoint( point);
		if ( ( 0 <= row && getRowCount() > row)
			&& ( 0 <= column && getColumnCount() > column))
			return ( 0 <= Arrays.binarySearch( rows, row)
				&& !__initialValueBasesMap.get( _kind).isEmpty());
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#on_paste()
	 */
	@Override
	public void on_paste() {
		_initialValueTableBase.on_paste();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#on_select_all(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_select_all(ActionEvent actionEvent) {
		_initialValueTableBase.selectAll();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#append(java.lang.Object[])
	 */
	@Override
	public void append(Object[] objects) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.addRow( objects);
		_initialValueTableBase.append();
		select( getRowCount() - 1);
		scroll( getRowCount() - 1);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#insert(java.lang.Object[])
	 */
	@Override
	public void insert(Object[] objects) {
		int[] rows = _initialValueTableBase.getSelectedRows();
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
		_initialValueTableBase.insert( row);
		defaultTableModel.insertRow( row, objects);
		select( row);
		scroll( row);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#update(java.lang.Object[])
	 */
	@Override
	public void update(Object[] objects) {
		int[] rows = _initialValueTableBase.getSelectedRows();
		if ( 1 != rows.length)
			return;

		for ( int i = 0; i < objects.length; ++i)
			setValueAt( objects[ i], rows[ 0], i);

		clearSelection();
		addRowSelectionInterval( rows[ 0], rows[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#up()
	 */
	@Override
	public void up() {
		_initialValueTableBase.up();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#down()
	 */
	@Override
	public void down() {
		_initialValueTableBase.down();
	}
}
