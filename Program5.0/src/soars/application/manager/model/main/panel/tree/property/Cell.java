/**
 * 
 */
package soars.application.manager.model.main.panel.tree.property;

/**
 * @author kurata
 *
 */
public class Cell {

	/**
	 * 
	 */
	public int _row = 0;

	/**
	 * 
	 */
	public int _column = 0;

	/**
	 * @param row
	 * @param column
	 */
	public Cell(int row, int column) {
		super();
		_row = row;
		_column = column;
	}

	/**
	 * @param cell
	 */
	public Cell(Cell cell) {
		super();
		_row = cell._row;
		_column = cell._column;
	}
}
