/**
 * 
 */
package soars.application.manager.model.main.panel.tab.contents;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics2D;

import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableColumnModel;

import soars.common.utility.swing.table.spread_sheet.SpreadSheetRowHeaderTable;
import soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase;

/**
 * @author kurata
 *
 */
public class SoarsContentsRowHeaderTable extends SpreadSheetRowHeaderTable {

	/**
	 * 
	 */
	private final int _defaultColumnWidth = 40;

	/**
	 * @param owner
	 * @param parent
	 */
	public SoarsContentsRowHeaderTable(Frame owner, Component parent) {
		super(owner, parent);
	}

	/**
	 * @param spreadSheetTableBase
	 * @param popupMenu
	 * @param graphics2D
	 * @return
	 */
	public boolean setup(SpreadSheetTableBase spreadSheetTableBase, boolean popupMenu, Graphics2D graphics2D) {
		if ( !super.setup(spreadSheetTableBase, popupMenu))
			return false;

		getTableHeader().setReorderingAllowed( false);
		setFillsViewportHeight( true);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setCellRenderer( new SoarsContentsRowHeaderTableCellRenderer());

		int columnWidth = get_column_width( 100, graphics2D);
		defaultTableColumnModel.getColumn( 0).setMinWidth( columnWidth);
		defaultTableColumnModel.getColumn( 0).setMaxWidth( columnWidth);

		//setToolTipText( "SoarsContentsRowHeaderTable");

		return true;
	}

	/**
	 * @param rows
	 * @param graphics2D
	 * @return
	 */
	private int get_column_width(int rows, Graphics2D graphics2D) {
		if ( 0 == rows || null == graphics2D)
			return _defaultColumnWidth;

		String maxNumber = String.valueOf( rows);
		FontMetrics fontMetrics = graphics2D.getFontMetrics();
		return ( Math.max( _defaultColumnWidth, fontMetrics.stringWidth( maxNumber) * 3 / 2));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		super.valueChanged(e);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.spread_sheet.SpreadSheetTableBase#changeSelection(int, int, boolean, boolean)
	 */
	public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
		super.changeSelection(rowIndex, columnIndex, toggle, extend);
	}

	/**
	 * 
	 */
	public void refresh() {
		updateUI();
		repaint();
	}
}
