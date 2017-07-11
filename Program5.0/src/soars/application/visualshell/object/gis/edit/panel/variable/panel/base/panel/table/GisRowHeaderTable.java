/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.GisVariablePanelBase;

/**
 * @author kurata
 *
 */
public class GisRowHeaderTable extends GisInitialValueTableBase {

	/**
	 * 
	 */
	private final int _defaultColumnWidth = 40;

	/**
	 * @param color
	 * @param gisDataManager
	 * @param gisVariablePanelBase
	 * @param owner
	 * @param parent
	 */
	public GisRowHeaderTable(Color color, GisDataManager gisDataManager, GisVariablePanelBase gisVariablePanelBase, Frame owner, Component parent) {
		super(color, gisDataManager, gisVariablePanelBase, owner, parent);
	}

	/**
	 * @param rows
	 * @param graphics2D
	 * @return
	 */
	private int get_column_width(int rows, Graphics2D graphics2D) {
		if ( 0 == rows || null == graphics2D)
			return _defaultColumnWidth;

		String max_number = String.valueOf( rows);
		FontMetrics fontMetrics = graphics2D.getFontMetrics();
		return ( Math.max( _defaultColumnWidth,
			fontMetrics.stringWidth( max_number) * 3 / 2));
	}

	/**
	 * @param gisInitialValueTableBase
	 * @param graphics2D
	 * @return
	 */
	public boolean setup(GisInitialValueTableBase gisInitialValueTableBase, Graphics2D graphics2D) {
		if ( !super.setup(gisInitialValueTableBase))
			return false;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 1);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		int column_width = get_column_width( 10000, graphics2D);
		defaultTableColumnModel.getColumn( 0).setMinWidth( column_width);
		defaultTableColumnModel.getColumn( 0).setMaxWidth( column_width);

		defaultTableColumnModel.getColumn( 0).setCellRenderer( new GisRowHeaderTableCellRenderer( _color));

		if ( 0 < defaultTableModel.getRowCount()) {
			addRowSelectionInterval( 0, 0);
			setColumnSelectionInterval( 0, 0);
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#selectAll()
	 */
	@Override
	public void selectAll() {
		super.selectAll();
		_gisInitialValueTableBase.selectAll();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#select(int[])
	 */
	@Override
	public boolean select(int[] rows) {
		clearSelection();
		_gisInitialValueTableBase.clearSelection();
		for ( int row:rows) {
			if ( 0 > row || getRowCount() <= row)
				continue;

			addRowSelectionInterval( row, row);
			_gisInitialValueTableBase.addRowSelectionInterval( row, row);
		}

		repaint();
		_gisInitialValueTableBase.repaint();
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#changeSelection(int, int, boolean, boolean)
	 */
	@Override
	public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
		super.changeSelection(rowIndex, columnIndex, toggle, extend);
		int[] rows = getSelectedRows();
		_gisInitialValueTableBase.changeSelection( rows, rowIndex);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#on_remove()
	 */
	@Override
	public void on_remove() {
		on_remove( null);
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
			_gisInitialValueTableBase.changeSelection();
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#on_copy()
	 */
	@Override
	public void on_copy() {
		int[] rows = getSelectedRows();
		if ( 1 > rows.length)
			return;

		_gisInitialValueTableBase.copy( rows);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#on_cut()
	 */
	@Override
	public void on_cut() {
		int[] rows = getSelectedRows();
		if ( 1 > rows.length)
			return;

		_gisInitialValueTableBase.cut( rows);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#can_paste(java.awt.Point)
	 */
	@Override
	protected boolean can_paste(Point point) {
		return _gisInitialValueTableBase.can_paste(point);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#on_paste()
	 */
	@Override
	public void on_paste() {
		int[] rows = getSelectedRows();
		_gisInitialValueTableBase.paste( rows);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#on_select_all(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_select_all(ActionEvent actionEvent) {
		selectAll();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#append()
	 */
	@Override
	public void append() {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.addRow( new Object[] { new GisRowData()});
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#insert(int)
	 */
	@Override
	public void insert(int row) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.insertRow( row, new Object[] { new GisRowData()});
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#up()
	 */
	@Override
	public void up() {
		int[] rows = getSelectedRows();
		if ( 1 > rows.length)
			return;

		Arrays.sort( rows);
		if ( 0 == rows[ 0])
			return;

		exchange_on_up( rows);
		_gisInitialValueTableBase.exchange_on_up( rows);

		clearSelection();
		_gisInitialValueTableBase.clearSelection();
		for ( int i = 0; i < rows.length; ++i) {
			addRowSelectionInterval( rows[ i] - 1, rows[ i] - 1);
			_gisInitialValueTableBase.addRowSelectionInterval( rows[ i] - 1, rows[ i] - 1);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#down()
	 */
	@Override
	public void down() {
		int[] rows = getSelectedRows();
		if ( 1 > rows.length)
			return;

		Arrays.sort( rows);
		if ( getRowCount() - 1 == rows[ rows.length - 1])
			return;

		exchange_on_down( rows);
		_gisInitialValueTableBase.exchange_on_down( rows);

		clearSelection();
		_gisInitialValueTableBase.clearSelection();
		for ( int i = 0; i < rows.length; ++i) {
			addRowSelectionInterval( rows[ i] + 1, rows[ i] + 1);
			_gisInitialValueTableBase.addRowSelectionInterval( rows[ i] + 1, rows[ i] + 1);
		}
	}
}
