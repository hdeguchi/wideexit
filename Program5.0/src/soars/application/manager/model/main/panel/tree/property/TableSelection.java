/**
 * 
 */
package soars.application.manager.model.main.panel.tree.property;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

/**
 * @author kurata
 *
 */
public class TableSelection extends ArrayList<Cell> {

	/**
	 * 
	 */
	public TableSelection() {
		super();
	}

	/**
	 * @param_tableSelection
	 */
	public TableSelection(TableSelection tableSelection) {
		for ( Cell cell:tableSelection)
			add( new Cell( cell));
	}

	/**
	 * @param soarsContentsTable
	 * @return
	 */
	public List<Cell> get_available_cells(JTable table) {
		List<Cell> cells = new ArrayList<Cell>();
		for ( Cell cell:this) {
			if ( table.getRowCount() > cell._row && table.getColumnCount() > cell._column)
				cells.add( cell);
		}
		return cells;
	}
}
