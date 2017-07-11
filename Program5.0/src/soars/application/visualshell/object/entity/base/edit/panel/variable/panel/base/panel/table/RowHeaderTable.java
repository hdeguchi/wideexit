/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table;

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
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.VariablePanelBase;

/**
 * @author kurata
 *
 */
public class RowHeaderTable extends InitialValueTableBase {

	/**
	 * 
	 */
	private final int _defaultColumnWidth = 40;

	/**
	 * @param color
	 * @param entityBase
	 * @param variablePanelBase
	 * @param owner
	 * @param parent
	 */
	public RowHeaderTable(Color color, EntityBase entityBase, VariablePanelBase variablePanelBase, Frame owner, Component parent) {
		super(color, entityBase, variablePanelBase, owner, parent);
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
	 * @param initialValueTableBase
	 * @param graphics2D
	 * @return
	 */
	public boolean setup(InitialValueTableBase initialValueTableBase, Graphics2D graphics2D) {
		if ( !super.setup(initialValueTableBase))
			return false;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 1);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		int column_width = get_column_width( 10000, graphics2D);
		defaultTableColumnModel.getColumn( 0).setMinWidth( column_width);
		defaultTableColumnModel.getColumn( 0).setMaxWidth( column_width);

		defaultTableColumnModel.getColumn( 0).setCellRenderer( new RowHeaderTableCellRenderer( _color));

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
		_initialValueTableBase.selectAll();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#select(int[])
	 */
	@Override
	public boolean select(int[] rows) {
		clearSelection();
		_initialValueTableBase.clearSelection();
		for ( int row:rows) {
			if ( 0 > row || getRowCount() <= row)
				continue;

			addRowSelectionInterval( row, row);
			_initialValueTableBase.addRowSelectionInterval( row, row);
		}

		repaint();
		_initialValueTableBase.repaint();
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#changeSelection(int, int, boolean, boolean)
	 */
	@Override
	public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
		super.changeSelection(rowIndex, columnIndex, toggle, extend);
		int[] rows = getSelectedRows();
		_initialValueTableBase.changeSelection( rows, rowIndex);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#on_remove()
	 */
	@Override
	public void on_remove() {
		on_remove( null);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.InitialValueTableBase#on_remove(java.awt.event.ActionEvent)
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
			_initialValueTableBase.changeSelection();
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#on_copy()
	 */
	@Override
	public void on_copy() {
		int[] rows = getSelectedRows();
		if ( 1 > rows.length)
			return;

		_initialValueTableBase.copy( rows);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#on_cut()
	 */
	@Override
	public void on_cut() {
		int[] rows = getSelectedRows();
		if ( 1 > rows.length)
			return;

		_initialValueTableBase.cut( rows);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#can_paste(java.awt.Point)
	 */
	@Override
	protected boolean can_paste(Point point) {
		return _initialValueTableBase.can_paste(point);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#on_paste()
	 */
	@Override
	public void on_paste() {
		int[] rows = getSelectedRows();
		_initialValueTableBase.paste( rows);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#on_select_all(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_select_all(ActionEvent actionEvent) {
		selectAll();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.InitialValueTableBase#append()
	 */
	@Override
	public void append() {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.addRow( new Object[] { new RowData()});
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.InitialValueTableBase#insert(int)
	 */
	@Override
	public void insert(int row) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.insertRow( row, new Object[] { new RowData()});
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.InitialValueTableBase#up()
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
		_initialValueTableBase.exchange_on_up( rows);

		clearSelection();
		_initialValueTableBase.clearSelection();
		for ( int i = 0; i < rows.length; ++i) {
			addRowSelectionInterval( rows[ i] - 1, rows[ i] - 1);
			_initialValueTableBase.addRowSelectionInterval( rows[ i] - 1, rows[ i] - 1);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.InitialValueTableBase#down()
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
		_initialValueTableBase.exchange_on_down( rows);

		clearSelection();
		_initialValueTableBase.clearSelection();
		for ( int i = 0; i < rows.length; ++i) {
			addRowSelectionInterval( rows[ i] + 1, rows[ i] + 1);
			_initialValueTableBase.addRowSelectionInterval( rows[ i] + 1, rows[ i] + 1);
		}
	}
}
