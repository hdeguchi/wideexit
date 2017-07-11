package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import script.ScriptReader;

/**
 * The JScriptTable class represents spread sheet editor.
 * @author H. Tanuma / SOARS project
 */
public class JScriptTable extends JTable implements Confirmable {

	private static final long serialVersionUID = 8474011630477845537L;

	static final int COLUMN_COUNT_AUTO_RESIZE_OFF = 27;

	AbstractUndo undo = null;
	AbstractUndo redo = null;

	abstract class AbstractUndo {
		AbstractUndo next;
		AbstractUndo() {
			next = undo;
			undo = this;
			undoButton.setEnabled(true);
		}
		void back() {
			undo = next;
			undo();
			next = redo;
			redo = this;
			redoButton.setEnabled(true);
			if (undo != null) {
				undo.back();
			}
			else {
				undoButton.setEnabled(false);
			}
		}
		abstract void undo();
		void restore() {
			redo = next;
			redo();
			next = undo;
			undo = this;
			undoButton.setEnabled(true);
			if (redo != null) {
				redo.restore();
			}
			else {
				redoButton.setEnabled(false);
			}
		}
		abstract void redo();
	}
	abstract class Undo extends AbstractUndo {
		Undo()
		{
			redo = null;
			redoButton.setEnabled(false);
		}
	}
	class UndoSelection extends AbstractUndo {
		private int[] rows = getSelectedRows();
		private int column = columnModel.getSelectionModel().getAnchorSelectionIndex();
		private int row = selectionModel.getAnchorSelectionIndex();
		void restoreSelection() {
			clearSelection();
			for (int i = 0; i < rows.length; ++i) {
				addRowSelectionInterval(rows[i], rows[i]);
			}
			columnModel.getSelectionModel().setAnchorSelectionIndex(column);
			selectionModel.setAnchorSelectionIndex(row);
		}
		void undo() {
		}
		void redo() {
		}
	}
	class UndoBreak extends UndoSelection {
		UndoBreak()
		{
			redo = null;
			redoButton.setEnabled(false);
		}
		void back() {
			undo = next;
			undoButton.setEnabled(undo != null);
			undo();
			next = redo;
			redo = this;
			redoButton.setEnabled(true);
			restoreSelection();
		}
	}
	class UndoExecute extends UndoSelection {
		UndoExecute() {
			back();
		}
		void restore() {
			redo = next;
			redoButton.setEnabled(redo != null);
			restoreSelection();
		}
	}

	public final JButton undoButton = new JButton("Undo");
	{
		undoButton.setEnabled(false);
		undoButton.setToolTipText("Undo Last Operation (Ctrl+Z)");
		undoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (undo != null) {
					stopCellEditing();
					new UndoExecute();
					requestFocus();
				}
			}
		});
	}

	public final JButton redoButton = new JButton("Redo");
	{
		redoButton.setEnabled(false);
		redoButton.setToolTipText("Redo Undone Operation (Ctrl+Y)");
		redoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (redo != null) {
					removeEditor();
					redo.restore();
					requestFocus();
				}
			}
		});
	}

	public final JButton addColumnButton = new JButton(" + Col ");
	{
		addColumnButton.setToolTipText("Add a Column (Ctrl+W)");
		addColumnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addColumn();
				requestFocus();
			}
		});
	}
	void addColumn() {
		stopCellEditing();
		final DefaultTableModel tableModel = (DefaultTableModel) getModel();
		new UndoBreak() {
			void undo() {
				tableModel.setColumnCount(getColumnCount() - 1);
			}
			void redo() {
				addColumn(null, null);
				restoreSelection();
			}
		}.redo();
	}
	void addColumn(Object columnName, Object[] columnData) {
		((DefaultTableModel) getModel()).addColumn(columnName, columnData);
		if (getColumnCount() == COLUMN_COUNT_AUTO_RESIZE_OFF && getAutoResizeMode() != AUTO_RESIZE_OFF) {
			setAutoResizeMode(AUTO_RESIZE_OFF);
			adjustRowHeight();
		}
	}

	public final JButton removeColumnButton = new JButton(" - Col ");
	{
		removeColumnButton.setToolTipText("Remove Rightmost Column (Ctrl+Q)");
		removeColumnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeColumn();
				requestFocus();
			}
		});
	}
	void removeColumn() {
		stopCellEditing();
		final DefaultTableModel tableModel = (DefaultTableModel) getModel();
		int column = getColumnCount() - 1;
		if (column > 0) {
			int rowCount = getRowCount();
			final Object[] columnData = new Object[rowCount];
			for (int row = 0; row < rowCount; ++row) {
				columnData[row] = getValueAt(row, column);
			}
			new UndoBreak() {
				void undo() {
					addColumn(null, columnData);
				}
				void redo() {
					tableModel.setColumnCount(getColumnCount() - 1);
					adjustRowHeight();
					restoreSelection();
				}
			}.redo();
		}
	}

	public final JButton insertButton = new JButton(" Ins ");
	{
		insertButton.setToolTipText("Insert Selected Rows (Insert)");
		insertButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insertSel();
				requestFocus();
			}
		});
	}
	void insertSel() {
		stopCellEditing();
		new UndoBreak();
		int focusRow = selectionModel.getAnchorSelectionIndex();
		int[] rows = getSelectedRows();
		for (int i = rows.length - 1; i >= 0; ) {
			int j = i - 1;
			while (j >= 0 && rows[j + 1] == rows[j] + 1) --j;
			for (int k = rows[i] - rows[j + 1]; k >= 0; --k) {
				insertRow(rows[j + 1], new Object[0]);
				removeRowSelectionInterval(rows[i] + 1, rows[i] + 1);
			}
			i = j;
		}
		selectionModel.setAnchorSelectionIndex(focusRow);
	}
	void insertRow(final int row, final Object[] rowData) {
		final DefaultTableModel tableModel = (DefaultTableModel) getModel();
		new Undo() {
			void undo() {
				tableModel.removeRow(row);
			}
			void redo() {
				tableModel.insertRow(row, rowData);
			}
		}.redo();
	}

	public final JButton deleteButton = new JButton(" Del ");
	{
		deleteButton.setToolTipText("Delete Selected Rows (Delete)");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteSel();
				requestFocus();
			}
		});
	}
	void deleteSel() {
		stopCellEditing();
		new UndoBreak();
		int[] rows = getSelectedRows();
		for (int i = rows.length - 1; i >= 0; --i) {
			removeRow(rows[i]);
		}
		ensureBottomRow();
		if (rows.length != 0 && rows[0] < getRowCount()) {
			addRowSelectionInterval(rows[0], rows[0]);
		}
	}
	void removeRow(final int row) {
		final DefaultTableModel tableModel = (DefaultTableModel) getModel();
		final Object[] rowData = ((Vector<?>) tableModel.getDataVector().elementAt(row)).toArray();
		new Undo() {
			void undo() {
				tableModel.insertRow(row, rowData);
			}
			void redo() {
				tableModel.removeRow(row);
			}
		}.redo();
	}

	public final JButton dupButton = new JButton(" Dup ");
	{
		dupButton.setToolTipText("Duplicate Selected Rows (Ctrl+D)");
		dupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dupSel();
				requestFocus();
			}
		});
	}
	void dupSel() {
		stopCellEditing();
		new UndoBreak();
		int[] rows = getSelectedRows();
		DefaultTableModel tableModel = (DefaultTableModel) getModel();
		for (int i = rows.length - 1; i >= 0; ) {
			int j = i - 1;
			while (j >= 0 && rows[j + 1] == rows[j] + 1) --j;
			for (int k = rows[i]; k >= rows[j + 1]; --k) {
				Object[] rowData = ((Vector<?>) tableModel.getDataVector().elementAt(k)).toArray();
				insertRow(rows[i] + 1, rowData);
				setRowHeight(rows[i] + 1, getRowHeight(k));
			}
			i = j;
		}
	}

	public final JButton upButton = new JButton("  Up  ");
	{
		upButton.setToolTipText("Move Selected Rows Up (Ctrl+Up)");
		upButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				upSel();
				requestFocus();
			}
		});
	}
	void upSel() {
		stopCellEditing();
		if (isRowSelected(0)) {
			return;
		}
		new UndoBreak();
		int[] rows = getSelectedRows();
		for (int i = 0; i < rows.length; ++i) {
			moveRow(rows[i], rows[i], rows[i] - 1);
			int rowHeight = getRowHeight(rows[i]);
			setRowHeight(rows[i], getRowHeight(rows[i] - 1));
			setRowHeight(rows[i] - 1, rowHeight);
			removeRowSelectionInterval(rows[i], rows[i]);
			addRowSelectionInterval(rows[i] - 1, rows[i] - 1);
		}
		ensureBottomRow();
	}
	void moveRow(final int start, final int end, final int to) {
		final DefaultTableModel tableModel = (DefaultTableModel) getModel();
		new Undo() {
			void undo() {
				tableModel.moveRow(to, to + end - start, start);
			}
			void redo() {
				tableModel.moveRow(start, end, to);
			}
		}.redo();
	}

	public final JButton downButton = new JButton("Down");
	{
		downButton.setToolTipText("Move Selected Rows Down (Ctrl+Down)");
		downButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				downSel();
				requestFocus();
			}
		});
	}
	void downSel() {
		stopCellEditing();
		if (isRowSelected(getRowCount() - 1)) {
			return;
		}
		new UndoBreak();
		int[] rows = getSelectedRows();
		for (int i = rows.length - 1; i >= 0; --i) {
			moveRow(rows[i], rows[i], rows[i] + 1);
			int rowHeight = getRowHeight(rows[i]);
			setRowHeight(rows[i], getRowHeight(rows[i] + 1));
			setRowHeight(rows[i] + 1, rowHeight);
			removeRowSelectionInterval(rows[i], rows[i]);
			addRowSelectionInterval(rows[i] + 1, rows[i] + 1);
		}
		ensureBottomRow();
	}

	public final JButton leftButton = new JButton("Left");
	{
		leftButton.setToolTipText("Move Selected Cells Left (Ctrl+Left)");
		leftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				leftSel();
				requestFocus();
			}
		});
	}
	void leftSel() {
		stopCellEditing();
		new UndoBreak();
		int[] rows = getSelectedRows();
		int focusColumn = columnModel.getSelectionModel().getAnchorSelectionIndex();
		for (int i = 0; i < rows.length; ++i) {
			for (int column = focusColumn; column < getColumnCount() - 1; ++column) {
				setValue(getValueAt(rows[i], column + 1), rows[i], column);
			}
			setValue(null, rows[i], getColumnCount() - 1);
		}
	}
	void setValue(final Object aValue, final int row, final int column) {
		final Object oldValue = getValueAt(row, column);
		if (aValue != null ? aValue.equals(oldValue) : oldValue == null) {
			return;
		}
		new Undo() {
			void undo() {
				setValueAt(oldValue, row, column);
				setRowHeight(row, rowHeight);
			}
			void redo() {
				setValueAt(aValue, row, column);
				setRowHeight(row, rowHeight);
			}
		}.redo();
	}

	public final JButton rightButton = new JButton("Right");
	{
		rightButton.setToolTipText("Move Selected Cells Right (Ctrl+Right)");
		rightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rightSel();
				requestFocus();
			}
		});
	}
	void rightSel() {
		stopCellEditing();
		new UndoBreak();
		int[] rows = getSelectedRows();
		int focusColumn = columnModel.getSelectionModel().getAnchorSelectionIndex();
		for (int i = 0; i < rows.length; ++i) {
			for (int column = getColumnCount() - 1; column > focusColumn; --column) {
				setValue(getValueAt(rows[i], column - 1), rows[i], column);
			}
			setValue(null, rows[i], focusColumn);
		}
	}

	public final JButton copyButton = new JButton("Copy Script");
	{
		copyButton.setToolTipText("Copy Editing Script to Clipboard");
		copyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopCellEditing();
				Clipboard.setString(JScriptTable.this.toString().replaceAll("\r\n","\n")); // Dirty Hack for Excel
				requestFocus();
			}
		});
	}

	public static final String untitled = "(untitled)";

	public final JButton saveButton = new JButton("Save");
	{
		saveButton.setToolTipText("Save Editing Script (Ctrl+S)");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveAs(getTitle());
				requestFocus();
			}
		});
	}

	public final JButton saveAsButton = new JButton("Save As...");
	{
		saveAsButton.setToolTipText("Save Editing Script as New File");
		saveAsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveAs(untitled);
				requestFocus();
			}
		});
	}

	void saveAs(String fileName) {
		stopCellEditing();
		if (fileName == untitled) {
			JFileChooser chooser = new JFileChooser(getTitle() == untitled ? "." : getTitle());
			if (chooser.showSaveDialog(panel) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			fileName = chooser.getSelectedFile().getPath();
		}
		try {
			write(new PrintWriter(new FileWriter(fileName)));
			setTitle(fileName);
			undo = null;
			undoButton.setEnabled(false);
		} catch (IOException ex) {
			PrintWriter out = new PrintWriter(new JTabbedWriter(frame, "error").setSelected());
			ex.printStackTrace(out);
			ex.printStackTrace();
		}
	}

	public final JButton runButton = new JButton("Run Script");
	{
		runButton.setToolTipText("Run Editing Script");
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopCellEditing();
				frame.runScript(new ScriptReader(new StringReader(JScriptTable.this.toString()), getTitle() + "  (editing)"), false);
			}
		});
	}

	public final JButton enqueButton = new JButton("Enque Script");
	{
		enqueButton.setToolTipText("Enqueue Editing Script");
		enqueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopCellEditing();
				frame.runScript(new ScriptReader(new StringReader(JScriptTable.this.toString()), getTitle() + "  (editing)"), true);
			}
		});
	}

//	public final JComboBox selectComboBox = new JComboBox(new String[] {"Select Rows", "Select Columns", "Select Box"});
//	{
//		selectComboBox.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				switch (selectComboBox.getSelectedIndex()) {
//					case 0:
//						setRowSelectionAllowed(true);
//						setColumnSelectionAllowed(false);
//						break;
//					case 1:
//						setRowSelectionAllowed(false);
//						setColumnSelectionAllowed(true);
//						break;
//					default:
//						setRowSelectionAllowed(true);
//						setColumnSelectionAllowed(true);
//						break;
//				}
//			}
//		});
//	}

	public final JLabel lineLabel = new JLabel();
	{
		lineLabel.setText("Line:  ");
	}

	public int confirm(Object message) {
		return JOptionPane.showConfirmDialog(panel, "Are you sure to close this script?", "SOARS", JOptionPane.YES_NO_OPTION);
	}

	public final JToolBar toolBar = new JToolBar();
	public final JPanel panel = new JConfirmPanel(new BorderLayout()) {
		private static final long serialVersionUID = 9021058640684428556L;
		public int confirm(Object message) {
			return JScriptTable.this.confirm(null);
		}};
	public final JMainFrame frame;

	public JScriptTable(JMainFrame frame)
	{
		this.frame = frame;
		toolBar.add(undoButton);
		toolBar.add(redoButton);
		toolBar.addSeparator();
		toolBar.add(addColumnButton);
		toolBar.add(removeColumnButton);
		toolBar.addSeparator();
		toolBar.add(insertButton);
		toolBar.add(deleteButton);
		toolBar.addSeparator();
		toolBar.add(dupButton);
		toolBar.addSeparator();
		toolBar.add(upButton);
		toolBar.add(downButton);
		toolBar.addSeparator();
		toolBar.add(leftButton);
		toolBar.add(rightButton);
		toolBar.addSeparator();
		toolBar.add(copyButton);
		toolBar.addSeparator();
		toolBar.add(saveButton);
		toolBar.add(saveAsButton);
		toolBar.addSeparator();
		toolBar.add(runButton);
		toolBar.add(enqueButton);
		toolBar.addSeparator();
		toolBar.addSeparator();
		toolBar.add(lineLabel);
		panel.add(toolBar, BorderLayout.NORTH);
		JScrollPane scrollPane = new JScrollPane(this);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(scrollPane);
		scrollPane.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				adjustRowHeight();
			}
		});
		getTableHeader().addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				stopCellEditing();
			}
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() != 2) {
					return;
				}
				if (getAutoResizeMode() == AUTO_RESIZE_SUBSEQUENT_COLUMNS) {
					setAutoResizeMode(AUTO_RESIZE_OFF);
				}
				else {
					setAutoResizeMode(AUTO_RESIZE_SUBSEQUENT_COLUMNS);
				}
				adjustRowHeight();
			}
			public void mouseReleased(MouseEvent e) {
				adjustRowHeight();
			}
		});
		getTableHeader().setReorderingAllowed(false);
		addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				switch ((int) e.getKeyChar()) {
					case 3: // Ctrl+C
						Clipboard.setString(toStringSel().replaceAll("\r\n","\n")); // Dirty Hack for Excel
						e.consume();
						break;
					case 4: // Ctrl+D
						dupSel();
						e.consume();
						break;
					case 17: // Ctrl+Q
						removeColumn();
						e.consume();
						break;
					case 19: // Ctrl+S
						saveAs(getTitle());
						e.consume();
						break;
					case 22: // Ctrl+V
						try {
							readSel(new BufferedReader(new StringReader(Clipboard.getString())));
						}
						catch (Exception ex) {
						}
						e.consume();
						break;
					case 23: // Ctrl+W
						addColumn();
						e.consume();
						break;
					case 24: // Ctrl+X
						Clipboard.setString(toStringSel().replaceAll("\r\n","\n")); // Dirty Hack for Excel
						deleteSel();
						e.consume();
						break;
					case 25: // Ctrl+Y
						JScriptTable.super.removeEditor();
						if (redo != null) {
							redo.restore();
						}
						e.consume();
						break;
					case 26: // Ctrl+Z
						JScriptTable.super.removeEditor();
						if (undo != null) {
							new UndoExecute();
						}
						e.consume();
						break;
				}
			}
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
					case 10: // Enter
						if (e.getModifiers() == KeyEvent.CTRL_MASK) {
							if (editorComp != null) {
								stopCellEditing();
							}
							else {
								int column = columnModel.getSelectionModel().getAnchorSelectionIndex();
								int row = selectionModel.getAnchorSelectionIndex();
								editCellAt(row, column);
								editor.textArea.requestFocus();
							}
							e.consume();
						}
						break;
					case 37: // Left
						if (e.getModifiers() == KeyEvent.CTRL_MASK) {
							leftSel();
							e.consume();
						}
						break;
					case 38: // Up
						if (e.getModifiers() == KeyEvent.CTRL_MASK) {
							upSel();
							e.consume();
						}
						break;
					case 39: // Right
						if (e.getModifiers() == KeyEvent.CTRL_MASK) {
							rightSel();
							e.consume();
						}
						break;
					case 40: // Down
						if (e.getModifiers() == KeyEvent.CTRL_MASK) {
							downSel();
							e.consume();
						}
						break;
					case 155: // Insert
						insertSel();
						e.consume();
						break;
					default:
						if ((int) e.getKeyChar() == 127) { // Delete
							deleteSel();
							e.consume();
						}
						break;
				}
			}
		});
	}

	int rowDrag = -1;
	int columnDrag;
	{
		addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				if (rowDrag == -1) {
					rowDrag = rowAtPoint(e.getPoint());
					columnDrag = columnAtPoint(e.getPoint());
					if (columnDrag == -1) {
						rowDrag = -1;
					}
					if (rowDrag == -1) {
						return;
					}
				}
				switch (e.getModifiersEx() & (MouseEvent.SHIFT_DOWN_MASK | MouseEvent.CTRL_DOWN_MASK)) {
					case MouseEvent.SHIFT_DOWN_MASK:
						setCursor(DragSource.DefaultMoveDrop);
						break;
					case MouseEvent.CTRL_DOWN_MASK:
						setCursor(DragSource.DefaultCopyDrop);
						break;
					case MouseEvent.SHIFT_DOWN_MASK | MouseEvent.CTRL_DOWN_MASK:
						setCursor(DragSource.DefaultLinkDrop);
						break;
					default:
						setCursor(Cursor.getDefaultCursor());
						rowDrag = -1;
						return;
				}
				clearSelection();
				columnModel.getSelectionModel().setAnchorSelectionIndex(columnDrag);
				selectionModel.setAnchorSelectionIndex(rowDrag);
			}
			public void mouseMoved(MouseEvent e) {
			}
		});
		addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (rowDrag == -1) {
					return;
				}
				int rowFrom = rowDrag;
				int columnFrom = columnDrag;
				rowDrag = -1;
				int rowTo = rowAtPoint(e.getPoint());
				int columnTo = columnAtPoint(e.getPoint());
				setCursor(Cursor.getDefaultCursor());
				if (rowTo == -1 || columnTo == -1) {
					return;
				}
				if (rowTo == rowFrom && columnTo == columnFrom) {
					return;
				}
				new UndoBreak();
				Object valueFrom = getValueAt(rowFrom, columnFrom);
				switch (e.getModifiersEx() & (MouseEvent.SHIFT_DOWN_MASK | MouseEvent.CTRL_DOWN_MASK)) {
					case MouseEvent.SHIFT_DOWN_MASK:
						setValue(null, rowFrom, columnFrom);
						setRowHeight(rowFrom, rowHeight);
						break;
					case MouseEvent.CTRL_DOWN_MASK:
						break;
					case MouseEvent.SHIFT_DOWN_MASK | MouseEvent.CTRL_DOWN_MASK:
						setValue(getValueAt(rowTo, columnTo), rowFrom, columnFrom);
						setRowHeight(rowFrom, rowHeight);
						break;
					default:
						return;
				}
				setValue(valueFrom, rowTo, columnTo);
				setRowHeight(rowTo, rowHeight);
				clearSelection();
				columnModel.getSelectionModel().setAnchorSelectionIndex(columnTo);
				selectionModel.setAnchorSelectionIndex(rowTo);
			}
		});
	}

	public String getTitle() {
		Component parent = panel.getParent();
		if (parent instanceof JTabbedPane) {
			JTabbedPane tab = (JTabbedPane) parent;
			return tab.getTitleAt(tab.indexOfComponent(panel));
		}
		return "";
	}
	public void setTitle(String title) {
		Component parent = panel.getParent();
		if (parent instanceof JTabbedPane) {
			JTabbedPane tab = (JTabbedPane) parent;
			tab.setTitleAt(tab.indexOfComponent(panel), title);
		}
	}
	public void adjustRowHeight() {
		setRowHeight(rowHeight);
	}
	public void readSel(BufferedReader reader) throws IOException {
		stopCellEditing();
		new UndoBreak();
		int focusRow = selectionModel.getAnchorSelectionIndex();
		int row = focusRow;
		final DefaultTableModel tableModel = (DefaultTableModel) getModel();
		String line;
		while ((line = reader.readLine()) != null) {
			String[] split = line.split("\t");
			while (tableModel.getColumnCount() < split.length) {
				new Undo() {
					void undo() {
						tableModel.setColumnCount(getColumnCount() - 1);
					}
					void redo() {
						addColumn(null, null);
					}
				}.redo();
			}
			insertRow(row, split);
			addRowSelectionInterval(row, row);
			++row;
		}
		ensureBottomRow();
		selectionModel.setAnchorSelectionIndex(focusRow);
	}
	public void read(BufferedReader reader) throws IOException {
		DefaultTableModel tableModel = (DefaultTableModel) getModel();
		tableModel.addColumn(null);
		String line;
		while ((line = reader.readLine()) != null) {
			String[] split = line.split("\t");
			while (tableModel.getColumnCount() < split.length) {
				addColumn(null, null);
			}
			tableModel.addRow(split);
		}
		ensureBottomRow();
		addRowSelectionInterval(0, 0);
		undo = null;
		undoButton.setEnabled(false);
		redo = null;
		redoButton.setEnabled(false);
	}
	public void ensureBottomRow() {
		int row = getRowCount();
		if (row == 0 || !isRowEmpty(row - 1)) {
			new Undo() {
				void undo() {
					((DefaultTableModel) getModel()).removeRow(getRowCount() - 1);
				}
				void redo() {
					((DefaultTableModel) getModel()).addRow(new Object[0]);
				}
			}.redo();
		}
	}
	public void writeSel(PrintWriter writer) {
		int[] rows = getSelectedRows();
		int columnCount = getColumnCount();
		for (int i = 0; i < rows.length; ++i) {
			for (int column = 0; column < columnCount; ++column) {
				if (column != 0) {
					writer.print('\t');
				}
				Object value = getValueAt(rows[i], column);
				if (value != null) {
					writer.print(value);
				}
			}
			writer.println();
		}
		writer.flush();
	}
	public String toStringSel() {
		StringWriter writer = new StringWriter();
		writeSel(new PrintWriter(writer));
		return writer.toString();
	}
	public void write(PrintWriter writer) {
		int rowCount = getRowCount();
		int columnCount = getColumnCount();
		for (int row = 0; row < rowCount; ++row) {
			for (int column = 0; column < columnCount; ++column) {
				if (column != 0) {
					writer.print('\t');
				}
				Object value = getValueAt(row, column);
				if (value != null) {
					writer.print(value);
				}
			}
			writer.println();
		}
		writer.flush();
	}
	public String toString() {
		StringWriter writer = new StringWriter();
		write(new PrintWriter(writer));
		return writer.toString();
	}

	TableCellRenderer renderer = new ScriptTableCellRenderer();

	public TableCellRenderer getCellRenderer(int row, int column) {
		return renderer;
	}

	public static Color focusForeground = new Color(0, 0, 0);
	public static Color focusBackground = new Color(240, 240, 240);
	public static Color paragraphBackground = new Color(255, 255, 216);
	public static Color warningBackground = new Color(255, 216, 216);

	public boolean isRowEmpty(int row) {
		int columnCount = getColumnCount();
		for (int i = 0; i < columnCount; ++i) {
			Object value = getValueAt(row, i);
			if (value != null && !value.toString().trim().equals("")) {
				return false; 
			}
		}
		return true;
	}

	public class ScriptTableCellRenderer extends JTextArea implements TableCellRenderer {

		private static final long serialVersionUID = -937812959081588124L;

		{
			setLineWrap(true);
			setWrapStyleWord(true);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (value == null) {
				value = "";
			}
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			}
			else {
				setForeground(table.getForeground());
				setBackground(!isRowEmpty(row) ? paragraphBackground : table.getBackground());
			}
			if (value.toString().trim() != value) {
				setBackground(warningBackground);
			}
			setFont(table.getFont());
			String line = "Line:  " + (row + 1);
			setToolTipText(line);
			if (hasFocus) {
				lineLabel.setText(line);
				if (table.isCellEditable(row, column)) {
					setForeground(focusForeground);
					setBackground(focusBackground);
				}
			}
			setText(value.toString());
			setSize(table.getColumnModel().getColumn(column).getWidth() - table.getRowMargin(), table.getRowHeight(row) - table.getRowMargin());
			int height = getPreferredSize().height;
			if (table.getRowHeight(row) < height) {
				table.setRowHeight(row, height);
			}
			return this;
		}
	}

	ScriptTableCellEditor editor = new ScriptTableCellEditor();

	public TableCellEditor getCellEditor(int row, int column) {
		return editor;
	}

	void stopCellEditing() {
		if (editorComp != null) {
			editor.stopCellEditing();
		}
	}
	public void editingStopped(ChangeEvent e) {
		final int row = editingRow;
		final int column = editingColumn;
		final Object oldValue = getValueAt(row, column);
		super.editingStopped(e);
		final Object aValue = getValueAt(row, column);
		if (!aValue.equals(oldValue)) {
			new UndoBreak() {
				void undo() {
					setValueAt(oldValue, row, column);
					setRowHeight(row, rowHeight);
				}
				void redo() {
					setValueAt(aValue, row, column);
					setRowHeight(row, rowHeight);
				}
			};
		}
		ensureBottomRow();
	}
	public void removeEditor() {
		super.removeEditor();
		setRowHeight(selectionModel.getAnchorSelectionIndex(), rowHeight);
	}

	public class ScriptTableCellEditor extends AbstractCellEditor implements TableCellEditor {

		private static final long serialVersionUID = 8081608380414968035L;

		public JTextArea textArea = new JTextArea();

		{
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textArea.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyChar() == '\t' || e.getKeyChar() == '\n') {
						JScriptTable.this.dispatchEvent(e);
						e.consume();
					}
				}
			});
			textArea.addCaretListener(new CaretListener() {
				public void caretUpdate(CaretEvent e) {
					int row = selectionModel.getAnchorSelectionIndex();
					int height = textArea.getPreferredSize().height;
					if (getRowHeight(row) < height) {
						setRowHeight(row, height);
					}
					textArea.requestFocus();
				}
			});
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			textArea.setFont(table.getFont());
			textArea.setText(value != null ? value.toString() : "");
			textArea.setRows(1);
			return textArea;
		}
		public Object getCellEditorValue() {
			return textArea.getText().replaceAll("\\s"," ");
		}
		public boolean isCellEditable(EventObject e) {
			return !(e instanceof MouseEvent) || ((MouseEvent) e).getClickCount() >= 2;
		}
	}
}
