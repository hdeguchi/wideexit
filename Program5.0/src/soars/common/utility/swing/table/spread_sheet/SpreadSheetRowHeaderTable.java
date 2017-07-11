/**
 * 
 */
package soars.common.utility.swing.table.spread_sheet;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.util.Arrays;

/**
 * @author kurata
 *
 */
public class SpreadSheetRowHeaderTable extends SpreadSheetTableBase {

	/**
	 * @param owner
	 * @param parent
	 */
	public SpreadSheetRowHeaderTable(Frame owner, Component parent) {
		super(owner, parent);
	}

	/**
	 * @param spreadSheetTableBase
	 * @param popupMenu
	 * @return
	 */
	public boolean setup(SpreadSheetTableBase spreadSheetTableBase, boolean popupMenu) {
		if ( !super.setup(popupMenu))
			return false;

		setDefaultEditor( Object.class, null);

		_spreadSheetTableBase = spreadSheetTableBase;

		setColumnCount( 1);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.base.StandardTable#on_mouse_pressed(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_pressed(MouseEvent mouseEvent) {
		_spreadSheetTableBase.stop_cell_editing();
		requestFocus();
		internal_synchronize();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#on_mouse_released(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_released(MouseEvent mouseEvent) {
		requestFocus();
		internal_synchronize();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#on_mouse_right_up(int, int, java.awt.event.MouseEvent)
	 */
	protected boolean on_mouse_right_up(int row, int column, MouseEvent mouseEvent) {
		requestFocus();

		int[] rows = getSelectedRows();
		if ( null == rows || 0 == rows.length)
			return false;

		Arrays.sort( rows);
		if ( 0 > Arrays.binarySearch( rows, row)) {
			boolean ctrl = ( 0 <= System.getProperty( "os.name").indexOf( "Mac"))
			? mouseEvent.isMetaDown() : mouseEvent.isControlDown();
			select_row( row, row);
		} else {
			if (_spreadSheetTableBase.getColumnCount() != _spreadSheetTableBase.getSelectedColumnCount())
				select_row( row, row);
		}

		requestFocus();
		internal_synchronize();

		return true;
	}

	/**
	 * @param from
	 * @param to
	 */
	private void select_row(int from, int to) {
		clearSelection();
		addRowSelectionInterval( from, to);
	}
}
