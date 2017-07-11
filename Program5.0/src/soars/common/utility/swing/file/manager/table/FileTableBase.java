/**
 * 
 */
package soars.common.utility.swing.file.manager.table;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import soars.common.utility.swing.file.manager.IFileManager;
import soars.common.utility.swing.file.manager.IFileManagerCallBack;
import soars.common.utility.swing.file.manager.ResourceManager;
import soars.common.utility.swing.file.manager.common.Message;
import soars.common.utility.swing.file.manager.common.Utility;
import soars.common.utility.swing.file.manager.edit.EditNameDlg;
import soars.common.utility.swing.file.manager.menu.ConvertEncodingAction;
import soars.common.utility.swing.file.manager.menu.CopyAction;
import soars.common.utility.swing.file.manager.menu.ExportAction;
import soars.common.utility.swing.file.manager.menu.NewDirectoryAction;
import soars.common.utility.swing.file.manager.menu.NewFileAction;
import soars.common.utility.swing.file.manager.menu.OpenFileAction;
import soars.common.utility.swing.file.manager.menu.OpenFileWithSpecifiedEncodingAction;
import soars.common.utility.swing.file.manager.menu.PasteAction;
import soars.common.utility.swing.file.manager.menu.RemoveAction;
import soars.common.utility.swing.file.manager.menu.RenameAction;
import soars.common.utility.swing.file.manager.tree.DirectoryNameComparator;
import soars.common.utility.swing.file.manager.tree.DirectoryTreeBase;
import soars.common.utility.swing.progress.IIntProgressCallback;
import soars.common.utility.swing.progress.IntProgressDlg;
import soars.common.utility.swing.table.base.StandardTable;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.clipboard.URISelection;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.file.FileUtility;

/**
 * @author kurata
 *
 */
public class FileTableBase extends StandardTable implements DragGestureListener, DragSourceListener, DropTargetListener, IIntProgressCallback {

	/**
	 * 
	 */
	private File[] _files = null;

	/**
	 * 
	 */
	private File _parent = null;

	/**
	 * 
	 */
	private DirectoryTreeBase _directoryTreeBase = null;

	/**
	 * 
	 */
	protected FileTableSpecificRowRendererBase _fileTableSpecificRowRendererBase = null;

	/**
	 * 
	 */
	protected File _dropTargetItem = null;

	/**
	 * 
	 */
	private IFileManagerCallBack _fileManagerCallBack = null;

	/**
	 * 
	 */
	private IFileManager _fileManager = null;

	/**
	 * 
	 */
	protected JMenuItem _copyMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _pasteMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _exportMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _removeMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _renameMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _newFileMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _newDirectoryMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _openFileMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _openFileWithSpecifiedEncodingMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _convertEncodingMenuItem = null;

	/**
	 * 
	 */
	protected Component _component = null;

	/**
	 * 
	 */
	protected String[] _extensions = null;

	/**
	 * @param fileManagerCallBack
	 * @param fileManager 
	 * @param component
	 * @param owner
	 * @param parent
	 */
	public FileTableBase(IFileManagerCallBack fileManagerCallBack, IFileManager fileManager, Component component, Frame owner, Component parent) {
		super(owner, parent);
		_fileManagerCallBack = fileManagerCallBack;
		_fileManager = fileManager;
		_component = component;
	}

	/**
	 * @param fileManagerCallBack
	 * @param fileManager
	 * @param component
	 * @param extensions
	 * @param owner
	 * @param parent
	 */
	public FileTableBase(IFileManagerCallBack fileManagerCallBack, IFileManager fileManager, Component component, String[] extensions, Frame owner, Component parent) {
		super(owner, parent);
		_fileManagerCallBack = fileManagerCallBack;
		_fileManager = fileManager;
		_component = component;
		_extensions = extensions;
	}

	/**
	 * @param file
	 */
	public void select(File file) {
		for ( int i = 0; i < getRowCount(); ++i) {
			if ( file.equals( ( File)getValueAt( i, 0))) {
				setRowSelectionInterval( i, i);
				break;
			}
		}
	}

	/**
	 * @param files
	 */
	public void select(File[] files) {
		clearSelection();
		for ( int i = 0; i < getRowCount(); ++i) {
			for ( int j = 0; j < files.length; ++j) {
				if ( files[ j].equals( ( File)getValueAt( i, 0))) {
					addRowSelectionInterval( i, i);
					break;
				}
			}
		}
	}

	/**
	 * @param directoryTreeBase
	 * @return
	 */
	public boolean setup(DirectoryTreeBase directoryTreeBase) {
		return setup( new FileTableSpecificRowRendererBase( this), directoryTreeBase);
	}

	/**
	 * @param fileTableSpecificRowRendererBase
	 * @param directoryTreeBase
	 * @return
	 */
	public boolean setup(FileTableSpecificRowRendererBase fileTableSpecificRowRendererBase, DirectoryTreeBase directoryTreeBase) {
		if ( !super.setup( true))
			return false;


		_fileTableSpecificRowRendererBase = fileTableSpecificRowRendererBase;
		_directoryTreeBase = directoryTreeBase;


		setAutoResizeMode( AUTO_RESIZE_OFF);

		getTableHeader().setReorderingAllowed( false);
		setDefaultEditor( Object.class, null);

		// JTable以外の領域の背景色とJTableのそれを一致させる
		// JTable以外の領域のイベントを取得出来るようにする
		setFillsViewportHeight( true);

		setShowHorizontalLines( false);
		setShowVerticalLines( false);


		setup_key_event();


		JTableHeader tableHeader = getTableHeader();
		FileTableHeaderRenderer fileTableHeaderRenderer = new FileTableHeaderRenderer();

		tableHeader.setDefaultRenderer( fileTableHeaderRenderer);


		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 4);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue(
			ResourceManager.get_instance().get( "file.table.header.name"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue(
			ResourceManager.get_instance().get( "file.table.header.size"));
		defaultTableColumnModel.getColumn( 2).setHeaderValue(
			ResourceManager.get_instance().get( "file.table.header.kind"));
		defaultTableColumnModel.getColumn( 3).setHeaderValue(
			ResourceManager.get_instance().get( "file.table.header.date"));

		defaultTableColumnModel.getColumn( 0).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 1).setPreferredWidth( 100);
		defaultTableColumnModel.getColumn( 2).setPreferredWidth( 100);
		defaultTableColumnModel.getColumn( 3).setPreferredWidth( 2000);

		for ( int i = 0; i < 4; ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			if ( 0 == i)
				tableColumn.setCellRenderer( _fileTableSpecificRowRendererBase);
			else
				tableColumn.setCellRenderer( new FileTableRowRenderer());
		}


		// マウスドラッグによる複数セル選択を解除
		MouseMotionListener[] mouseMotionListeners = getMouseMotionListeners();
		for ( int i = 0; i < mouseMotionListeners.length; ++i)
			removeMouseMotionListener( mouseMotionListeners[ i]);


		new DragSource().createDefaultDragGestureRecognizer( this, DnDConstants.ACTION_COPY_OR_MOVE, this);
		new DropTarget( this, this);


		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#setup_key_event()
	 */
	protected void setup_key_event() {
		super.setup_key_event();

		Action enterAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_enter( null);
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0), "enter");
		getActionMap().put( "enter", enterAction);


		Action deleteAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_remove( e);
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0), "delete");
		getActionMap().put( "delete", deleteAction);


		Action backSpaceAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_remove( e);
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, 0), "backspace");
		getActionMap().put( "backspace", backSpaceAction);


		Action copyAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_copy( e);
			}
		};
		//getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "copy");
		getActionMap().put( "copy", copyAction);


		Action pasteAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_paste( e);
			}
		};
		//getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "paste");
		getActionMap().put( "paste", pasteAction);


		Action cutAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
			}
		};
		//getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "cut");
		getActionMap().put( "cut", cutAction);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#setup_popup_menu()
	 */
	protected void setup_popup_menu() {
		super.setup_popup_menu();

		_copyMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "popup.menu.copy.menu"),
			new CopyAction( ResourceManager.get_instance().get( "popup.menu.copy.menu"), this),
			ResourceManager.get_instance().get( "popup.menu.copy.mnemonic"),
			ResourceManager.get_instance().get( "popup.menu.copy.stroke"));
		_pasteMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "popup.menu.paste.menu"),
			new PasteAction( ResourceManager.get_instance().get( "popup.menu.paste.menu"), this),
			ResourceManager.get_instance().get( "popup.menu.paste.mnemonic"),
			ResourceManager.get_instance().get( "popup.menu.paste.stroke"));
		_exportMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "popup.menu.export.menu"),
			new ExportAction( ResourceManager.get_instance().get( "popup.menu.export.menu"), this),
			ResourceManager.get_instance().get( "popup.menu.export.mnemonic"),
			ResourceManager.get_instance().get( "popup.menu.export.stroke"));

		_popupMenu.addSeparator();

		_removeMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "popup.menu.remove.menu"),
			new RemoveAction( ResourceManager.get_instance().get( "popup.menu.remove.menu"), this),
			ResourceManager.get_instance().get( "popup.menu.remove.mnemonic"),
			ResourceManager.get_instance().get( "popup.menu.remove.stroke"));

		_popupMenu.addSeparator();

		_renameMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "popup.menu.rename.menu"),
			new RenameAction( ResourceManager.get_instance().get( "popup.menu.rename.menu"), this),
			ResourceManager.get_instance().get( "popup.menu.rename.mnemonic"),
			ResourceManager.get_instance().get( "popup.menu.rename.stroke"));

		_popupMenu.addSeparator();

		_newFileMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "popup.menu.new.file.menu"),
			new NewFileAction( ResourceManager.get_instance().get( "popup.menu.new.file.menu"), this),
			ResourceManager.get_instance().get( "popup.menu.new.file.mnemonic"),
			ResourceManager.get_instance().get( "popup.menu.new.file.stroke"));
		_newDirectoryMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "popup.menu.new.directory.menu"),
			new NewDirectoryAction( ResourceManager.get_instance().get( "popup.menu.new.directory.menu"), this),
			ResourceManager.get_instance().get( "popup.menu.new.directory.mnemonic"),
			ResourceManager.get_instance().get( "popup.menu.new.directory.stroke"));

		_popupMenu.addSeparator();

		_openFileMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "popup.menu.open.file.menu"),
			new OpenFileAction( ResourceManager.get_instance().get( "popup.menu.open.file.menu"), this),
			ResourceManager.get_instance().get( "popup.menu.open.file.mnemonic"),
			ResourceManager.get_instance().get( "popup.menu.open.file.stroke"));
		_openFileWithSpecifiedEncodingMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "popup.menu.open.file.with.specified.encoding.menu"),
			new OpenFileWithSpecifiedEncodingAction( ResourceManager.get_instance().get( "popup.menu.open.file.with.specified.encoding.menu"), this),
			ResourceManager.get_instance().get( "popup.menu.open.file.with.specified.encoding.mnemonic"),
			ResourceManager.get_instance().get( "popup.menu.open.file.with.specified.encoding.stroke"));

		_popupMenu.addSeparator();

		_convertEncodingMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "popup.menu.convert.encoding.menu"),
			new ConvertEncodingAction( ResourceManager.get_instance().get( "popup.menu.convert.encoding.menu"), this),
			ResourceManager.get_instance().get( "popup.menu.convert.encoding.mnemonic"),
			ResourceManager.get_instance().get( "popup.menu.convert.encoding.stroke"));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#on_mouse_right_up(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
		if ( null == _userInterface)
			return;

		Point point = mouseEvent.getPoint();

		_copyMenuItem.setEnabled( true);
		_pasteMenuItem.setEnabled( exists_files_to_paste());
		_exportMenuItem.setEnabled( true);
		_removeMenuItem.setEnabled( true);
		_renameMenuItem.setEnabled( true);
		_newFileMenuItem.setEnabled( null != _parent);
		_newDirectoryMenuItem.setEnabled( null != _parent);
		_openFileMenuItem.setEnabled( true);
		_openFileWithSpecifiedEncodingMenuItem.setEnabled( true);
		_convertEncodingMenuItem.setEnabled( true);

		if ( 0 == getRowCount()) {
			_copyMenuItem.setEnabled( false);
			_exportMenuItem.setEnabled( false);
			_removeMenuItem.setEnabled( false);
			_renameMenuItem.setEnabled( false);
			_openFileMenuItem.setEnabled( false);
			_openFileWithSpecifiedEncodingMenuItem.setEnabled( false);
			_convertEncodingMenuItem.setEnabled( false);
		} else {
			int row = rowAtPoint( point);
			if ( ( 0 <= row && getRowCount() > row)) {
				int[] rows = getSelectedRows();
				boolean contains = ( 0 <= Arrays.binarySearch( rows, row));
				File file = ( File)getValueAt( row, 0);
				boolean is_on_label = _fileTableSpecificRowRendererBase.is_on_label( file, point, this);
				if ( is_on_label && !contains) {
					setRowSelectionInterval( row, row);
					setColumnSelectionInterval( 0, 0);
					file = ( File)getValueAt( row, 0);
				}

				_copyMenuItem.setEnabled( is_on_label);
				_exportMenuItem.setEnabled( is_on_label);
				_removeMenuItem.setEnabled( is_on_label);

				rows = getSelectedRows();
				_renameMenuItem.setEnabled( is_on_label && 1 == rows.length);
				_openFileMenuItem.setEnabled( is_on_label && 1 == rows.length && file.isFile());
				_openFileWithSpecifiedEncodingMenuItem.setEnabled( is_on_label && 1 == rows.length && file.isFile());
				_convertEncodingMenuItem.setEnabled( is_on_label && 0 < rows.length);
			} else {
				_copyMenuItem.setEnabled( false);
				_exportMenuItem.setEnabled( false);
				_removeMenuItem.setEnabled( false);
				_renameMenuItem.setEnabled( false);
				_openFileMenuItem.setEnabled( false);
				_openFileWithSpecifiedEncodingMenuItem.setEnabled( false);
				_convertEncodingMenuItem.setEnabled( false);
			}
		}

		_popupMenu.show( this, point.x, point.y);
	}

	/**
	 * @return
	 */
	private boolean exists_files_to_paste() {
		File[] files = Clipboard.get_files();
		return ( null != files && 0 < files.length);
	}

	/**
	 * @param encoding 
	 */
	protected void on_enter(String encoding) {
		if ( null == _parent)
			return;

		int[] rows = getSelectedRows();
		if ( null == rows || 1 != rows.length)
			return;

		int[] columns = getSelectedColumns();
		if ( null == columns || 1 != columns.length || 0 != columns[ 0])
			return;

		try {
			File file = ( File)getValueAt( rows[ 0], 0);
			if ( null == file)
				return;

			if ( file.isDirectory())
				_directoryTreeBase.expand( file);
			else
				_fileManagerCallBack.on_select( file, encoding);

		} catch (ArrayIndexOutOfBoundsException e) {
			return;
		}
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
	}

	/**
	 * 
	 */
	private void clear() {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		while ( 0 < getRowCount())
			defaultTableModel.removeRow( 0);
	}

	/**
	 * @param directory
	 * @return
	 */
	public boolean update(File directory) {
		clear();

		_parent = directory;
		if ( null == directory)
			return true;
//		if ( null != directory)
//			_parent = directory;

		File[] files = _parent.listFiles();
		if ( null == files)
			return false;

		List[] lists = new List[] { new ArrayList(), new ArrayList()};
		for ( int i = 0; i < files.length; ++i) {
			if ( !_fileManagerCallBack.visible( files[ i]))
				continue;

			if ( files[ i].isDirectory())
				lists[ 0].add( files[ i]);
			else
				lists[ 1].add( files[ i]);
		}

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();

		update_directories( ( File[])lists[ 0].toArray( new File[ 0]), defaultTableModel);
		update_files( ( File[])lists[ 1].toArray( new File[ 0]), defaultTableModel);

		if ( 0 < defaultTableModel.getRowCount())
			setRowSelectionInterval( 0, 0);

		return true;
	}

	/**
	 * @param directories
	 * @param defaultTableModel
	 */
	private void update_directories(File[] directories, DefaultTableModel defaultTableModel) {
		Arrays.sort( directories, new DirectoryNameComparator( true, false));
		for ( int i = 0; i < directories.length; ++i) {
			Object[] objects = new Object[ 4];
			objects[ 0] = directories[ i];
			objects[ 1] = directories[ i];
			objects[ 2] = directories[ i];
			objects[ 3] = directories[ i];
			defaultTableModel.addRow( objects);
		}
	}

	/**
	 * @param files
	 * @param defaultTableModel
	 */
	private void update_files(File[] files, DefaultTableModel defaultTableModel) {
		TreeMap<String, List<File>> treeMap = get_extensions( files);
		Iterator iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String extension = ( String)entry.getKey();
			List<File> list = ( List<File>)entry.getValue();
			update_files( extension, list.toArray( new File[ 0]), defaultTableModel);
		}
	}

	/**
	 * @param extension
	 * @param files
	 * @param defaultTableModel
	 */
	private void update_files(String extension, File[] files, DefaultTableModel defaultTableModel) {
		Arrays.sort( files, new FilenameComparator( extension, true, false));
		for ( int i = 0; i < files.length; ++i) {
			Object[] objects = new Object[ 4];
			objects[ 0] = files[ i];
			objects[ 1] = files[ i];
			objects[ 2] = files[ i];
			objects[ 3] = files[ i];
			defaultTableModel.addRow( objects);
		}
	}

	/**
	 * @param files
	 * @return
	 */
	private TreeMap<String, List<File>> get_extensions(File[] files) {
		TreeMap<String, List<File>> treeMap = new TreeMap<String, List<File>>();
		for ( int i = 0; i < files.length; ++i) {
			String extension = get_extension( files[ i]);
			List<File> list = treeMap.get( extension);
			if ( null == list) {
				list = new ArrayList<File>();
				treeMap.put( extension, list);
			}

			list.add( files[ i]);
		}
		return treeMap;
	}

	/**
	 * @param file
	 * @return
	 */
	protected String get_extension(File file) {
		String[] words = Tool.split( file.getName(), '.');
		if ( null == words
			|| 1 == words.length
			|| ( 2 == words.length && words[ 0].equals( "")))
			return "";

		return words[ words.length - 1];
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#on_mouse_pressed(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_pressed(MouseEvent mouseEvent) {
		Point position = mouseEvent.getPoint();
		if ( null == position)
			return;

		if ( 0 != columnAtPoint( position)) {
			clearSelection();
			return;
		}

		try {
			File file = ( File)getValueAt( rowAtPoint( position), columnAtPoint( position));
			if ( null == file)
				return;

			if ( !_fileTableSpecificRowRendererBase.is_on_label( file, position, this)) {
				clearSelection();
				return;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return;
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#on_mouse_left_double_click(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_double_click(MouseEvent mouseEvent) {
		Point position = mouseEvent.getPoint();
		if ( null == position)
			return;

		if ( 0 != columnAtPoint( position))
			return;

		try {
			File file = ( File)getValueAt( rowAtPoint( position), columnAtPoint( position));
			if ( null == file)
				return;

			if ( !_fileTableSpecificRowRendererBase.is_on_label( file, position, this))
				return;

			if ( file.isDirectory())
				_directoryTreeBase.expand( file);
			else
				_fileManagerCallBack.on_select( file, null);

		} catch (ArrayIndexOutOfBoundsException e) {
			return;
		}
	}

	/**
	 * @param actionEvent
	 */
	public void on_copy(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		if ( null == rows || 0 == rows.length)
			return;

		File[] files = new File[ rows.length];
		for ( int i = 0; i < rows.length; ++i)
			files[ i] = ( File)getValueAt( rows[ i], 0);

		Clipboard.set( files);
	}

	/**
	 * @param actionEvent
	 */
	public void on_paste(ActionEvent actionEvent) {
		if ( null == _parent)
			return;

		File[] files = Clipboard.get_files();
		if ( null == files || 0 == files.length)
			return;

		_fileManagerCallBack.on_start_paste();

		int result = IntProgressDlg.execute(
			_owner,
			ResourceManager.get_instance().get( "file.manager.copy.message"),
			true,
			"copy",
			ResourceManager.get_instance().get( "dialog.cancel"),
			new Object[] { files, _parent, new Integer( DnDConstants.ACTION_COPY), new Boolean( files[ 0].getParentFile().equals( _parent)), new Boolean( true)},
			this, _component);
		switch ( result) {
			case -1:
				Message.on_error_from_parent_to_child( _component);
				break;
			case -2:
				Message.on_error_copy_or_move( _component, DnDConstants.ACTION_COPY);
				break;
		}

		_directoryTreeBase.on_update( _parent);

		for ( int i = 0; i < files.length; ++i)
			files[ i] = new File( _parent, files[ i].getName());

		select( files);

		_fileManagerCallBack.on_paste_completed();
	}

	/**
	 * @param actionEvent
	 */
	public void on_export(ActionEvent actionEvent) {
		if ( null == _parent)
			return;

		int[] rows = getSelectedRows();
		if ( null == rows || 0 == rows.length)
			return;

		File[] files = new File[ rows.length];
		for ( int i = 0; i < rows.length; ++i)
			files[ i] = ( File)getValueAt( rows[ i], 0);

		File directory = _fileManagerCallBack.get_export_directory();
		if ( null == directory)
			return;

		int result = IntProgressDlg.execute(
			_owner,
			ResourceManager.get_instance().get( "file.manager.copy.message"),
			true,
			"copy",
			ResourceManager.get_instance().get( "dialog.cancel"),
			new Object[] { files, directory, new Integer( DnDConstants.ACTION_COPY), new Boolean( false), new Boolean( false)},
			this, _component);
		switch ( result) {
			case -1:
				Message.on_error_from_parent_to_child( _component);
				break;
			case -2:
				Message.on_error_copy_or_move( _component, DnDConstants.ACTION_COPY);
				break;
		}
	}

	/**
	 * @param actionEvent
	 */
	public void on_remove(ActionEvent actionEvent) {
		if ( null == _parent)
			return;

		if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
			_component,
			ResourceManager.get_instance().get( "file.manager.confirm.remove.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION))
		return;

		int[] rows = getSelectedRows();
		List<File> list = new ArrayList<File>();
		for ( int row:rows)
			list.add( ( File)getValueAt( row, 0));

		File[] files = list.toArray( new File[ 0]);

		files = get_files( files);

		for ( File file:files) {
			if ( null == file || !file.exists())
				continue;

			if ( file.isDirectory()) {
				if ( !remove( file)) {
					Message.on_error_remove( _component);
					break;
				}
			}

			if ( null != _fileManagerCallBack && !_fileManagerCallBack.can_remove( file)) {
				Message.on_error_remove( _component);
				break;
			}

			if ( !file.delete()) {
				Message.on_error_remove( _component);
				break;
			}

			if ( null != _fileManagerCallBack)
				_fileManagerCallBack.modified( _fileManager);
		}

		_directoryTreeBase.on_update( _parent);

		if ( 0 < getRowCount()) {
			if ( 0 == rows.length || 0 > rows[ 0] || getRowCount() <= rows[ 0])
				setRowSelectionInterval( 0, 0);
			else
				setRowSelectionInterval( rows[ 0], rows[ 0]);
		}
	}

	/**
	 * @param directory
	 * @return
	 */
	private boolean remove(File directory) {
		File[] files = directory.listFiles();
		for ( int i = 0; i < files.length; ++i) {
			if ( null == files[ i] || !files[ i].exists())
				continue;

			if ( files[ i].isDirectory()) {
				if ( !remove( files[ i]))
					return false;
			}

			if ( null != _fileManagerCallBack && !_fileManagerCallBack.can_remove( files[ i]))
				return false;

			if ( !files[ i].delete())
				return false;

			if ( null != _fileManagerCallBack)
				_fileManagerCallBack.modified( _fileManager);
		}

		return true;
	}

	/**
	 * @param actionEvent
	 */
	public void on_rename(ActionEvent actionEvent) {
		if ( null == _parent)
			return;

		int[] rows = getSelectedRows();
		if ( null == rows || 1 != rows.length)
			return;

		File file = ( File)getValueAt( rows[ 0], 0);
		File originalFile = new File( file.getAbsolutePath());
		EditNameDlg editNameDlg = new EditNameDlg(
			_owner,
			ResourceManager.get_instance().get( file.isDirectory() ? "edit.directory.name.dialog.title" : "edit.file.name.dialog.title"),
			true,
			file.isDirectory() ? "directory" : "file",
			file,
			_parent);
		if ( !editNameDlg.do_modal( _component))
			return;

		if ( !editNameDlg._modified)
			return;

		if ( null != _fileManagerCallBack) {
			_fileManagerCallBack.on_move( originalFile, editNameDlg._file);
			_fileManagerCallBack.on_rename( originalFile, editNameDlg._file);
			_fileManagerCallBack.modified( _fileManager);
		}

		_directoryTreeBase.on_rename( _parent, originalFile, editNameDlg._file);
		select( editNameDlg._file);
	}

	/**
	 * @param actionEvent
	 */
	public void on_new_file(ActionEvent actionEvent) {
		if ( null == _parent)
			return;

		EditNameDlg editNameDlg = new EditNameDlg(
			_owner,
			ResourceManager.get_instance().get( "append.file.name.dialog.title"),
			true,
			"file",
			_parent);
		if ( !editNameDlg.do_modal( _component))
			return;

		if ( !editNameDlg._modified)
			return;

		update( _parent);
		select( editNameDlg._file);

		if ( null != _fileManagerCallBack)
			_fileManagerCallBack.modified( _fileManager);
	}

	/**
	 * @param actionEvent
	 */
	public void on_new_directory(ActionEvent actionEvent) {
		if ( null == _parent)
			return;

		EditNameDlg editNameDlg = new EditNameDlg(
			_owner,
			ResourceManager.get_instance().get( "append.directory.name.dialog.title"),
			true,
			"directory",
			_parent);
		if ( !editNameDlg.do_modal( _component))
			return;

		if ( !editNameDlg._modified)
			return;

		_directoryTreeBase.on_new_directory( _parent);
		select( editNameDlg._file);

		if ( null != _fileManagerCallBack)
			_fileManagerCallBack.modified( _fileManager);
	}

	/**
	 * @param actionEvent
	 */
	public void on_open_file(ActionEvent actionEvent) {
		setColumnSelectionInterval( 0, 0);
		on_enter( "");
	}

	/**
	 * @param actionEvent
	 */
	public void on_open_file_with_specified_encoding(ActionEvent actionEvent) {
		EncodingSelectorDlg encodingSelectorDlg = new EncodingSelectorDlg( _owner, ResourceManager.get_instance().get( "encoding.selector.dialog.title"), true);
		if ( !encodingSelectorDlg.do_modal( _component))
			return;

		setColumnSelectionInterval( 0, 0);
		on_enter( encodingSelectorDlg._encoding);
	}

	/**
	 * @param actionEvent
	 */
	public void on_convert_encoding(ActionEvent actionEvent) {
		ConvertEncodingDlg convertEncodingDlg = new ConvertEncodingDlg( _owner, ResourceManager.get_instance().get( "convert.encoding.dialog.title"), true);
		if ( !convertEncodingDlg.do_modal( _component))
			return;

		if ( 0 > IntProgressDlg.execute( _owner,
			ResourceManager.get_instance().get( "convert.encoding.dialog.title") + "[" + ConvertEncodingDlg._defaultEncodings[ 0] + "->" + ConvertEncodingDlg._defaultEncodings[ 1] + "]", true,
			"convert", ResourceManager.get_instance().get( "dialog.cancel"), new Object[] { ConvertEncodingDlg._defaultEncodings[ 0], ConvertEncodingDlg._defaultEncodings[ 1]}, this, _component))
			JOptionPane.showMessageDialog( _component,
				ResourceManager.get_instance().get( "file.manager.error.convert.encoding.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param encodingFrom
	 * @param encodingTo
	 * @param intProgressDlg
	 * @return
	 */
	private int convert(String encodingFrom, String encodingTo, IntProgressDlg intProgressDlg) {
		List<File> fileList = new ArrayList<File>();
		get_files( fileList);
		if ( fileList.isEmpty())
			return 0;

		boolean modified = false;
		for ( int i = 0; i < fileList.size(); ++i) {
			if ( intProgressDlg._canceled)
				break;

			if ( !FileUtility.convert_encoding( fileList.get( i), encodingFrom, encodingTo)) {
				_fileManagerCallBack.modified( _fileManager);
				return -1;
			}

			modified = true;

			intProgressDlg.set( ( int)( 100.0 * ( double)( i + 1) / ( double)fileList.size()));
		}

		if ( modified)
			_fileManagerCallBack.modified( _fileManager);

		return 0;
	}

	/**
	 * @param fileList
	 */
	private void get_files(List<File> fileList) {
		int[] rows = getSelectedRows();
		if ( null == rows || 0 == rows.length)
			return;

		for ( int row:rows) {
			File file = ( File)getValueAt( row, 0);
			if ( file.isDirectory())
				get_files( file, fileList);
			else if ( file.isFile() && can_convert( file)) {
				fileList.add( file);
			}
		}
	}

	/**
	 * @param directory
	 * @param fileList
	 */
	private void get_files(File directory, List<File> fileList) {
		File[] files = directory.listFiles();
		if ( null == files)
			return;

		for ( File file:files) {
			if ( file.isDirectory())
				get_files( file, fileList);
			else if ( file.isFile() && can_convert( file)) {
				fileList.add( file);
			}
		}
	}

	/**
	 * @param file
	 * @return
	 */
	private boolean can_convert(File file) {
		if ( null == _extensions)
			return true;

		for ( String extension:_extensions) {
			if ( file.getName().toLowerCase().endsWith( extension))
				return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.DragGestureEvent)
	 */
	public void dragGestureRecognized(DragGestureEvent arg0) {
		int[] rows = getSelectedRows();
		if ( null == rows || 0 == rows.length)
			return;

		int[] columns = getSelectedColumns();
		if ( null == columns || 1 != columns.length || 0 != columns[ 0])
			return;

		Point position = getMousePosition();
		if ( null == position)
			return;

		try {
			File file = ( File)getValueAt( rowAtPoint( position), columnAtPoint( position));
			if ( null == file)
				return;

			if ( !_fileTableSpecificRowRendererBase.is_on_label( file, position, this))
				return;

			_files = new File[ rows.length];
			for ( int i = 0; i < rows.length; ++i)
				_files[ i] = ( File)getValueAt( rows[ i], 0);

			Transferable transferable = new FileItemTransferable( _files);
			new DragSource().startDrag( arg0, Cursor.getDefaultCursor(), transferable, this);
		} catch (ArrayIndexOutOfBoundsException e) {
			return;
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragDropEnd(java.awt.dnd.DragSourceDropEvent)
	 */
	public void dragDropEnd(DragSourceDropEvent arg0) {
		_files = null;
		_dropTargetItem = null;
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragEnter(java.awt.dnd.DragSourceDragEvent)
	 */
	public void dragEnter(DragSourceDragEvent arg0) {
		arg0.getDragSourceContext().setCursor( DragSource.DefaultMoveDrop);
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragExit(java.awt.dnd.DragSourceEvent)
	 */
	public void dragExit(DragSourceEvent arg0) {
		arg0.getDragSourceContext().setCursor( DragSource.DefaultMoveNoDrop);
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragOver(java.awt.dnd.DragSourceDragEvent)
	 */
	public void dragOver(DragSourceDragEvent arg0) {
		if ( DnDConstants.ACTION_COPY == arg0.getDropAction())
			arg0.getDragSourceContext().setCursor( DragSource.DefaultCopyDrop);
		else
			arg0.getDragSourceContext().setCursor( DragSource.DefaultMoveDrop);
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dropActionChanged(java.awt.dnd.DragSourceDragEvent)
	 */
	public void dropActionChanged(DragSourceDragEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent arg0) {
		_dropTargetItem = null;
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent arg0) {
		_dropTargetItem = null;
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent arg0) {
		if ( null == _parent) {
			_dropTargetItem = null;
			arg0.rejectDrag();
			repaint();
			return;
		}

		Transferable transferable = arg0.getTransferable();
    DataFlavor[] dataFlavors = arg0.getCurrentDataFlavors();
		if ( !transferable.isDataFlavorSupported( DataFlavor.javaFileListFlavor)
			&& !transferable.isDataFlavorSupported( URISelection._uriFlavor)
			&& !dataFlavors[ 0].getHumanPresentableName().equals( FileItemTransferable._name)) {
			_dropTargetItem = null;
			arg0.rejectDrag();
			repaint();
			return;
		}

		Point position = getMousePosition();
		if ( null == position) {
			_dropTargetItem = null;
			arg0.rejectDrag();
			repaint();
			return;
		}

		if ( dataFlavors[ 0].getHumanPresentableName().equals( FileItemTransferable._name)) {
			try {
				_dropTargetItem = ( File)getValueAt( rowAtPoint( position), columnAtPoint( position));
			} catch (ArrayIndexOutOfBoundsException e) {
				_dropTargetItem = null;
				arg0.rejectDrag();
				repaint();
				return;
			}	

			if ( null == _dropTargetItem) {
				arg0.rejectDrag();
				repaint();
				return;
			}

			if ( 0 != columnAtPoint( position) || !_dropTargetItem.isDirectory()) {
				arg0.rejectDrag();
				repaint();
				return;
			}

			for ( int i = 0; i < _files.length; ++i) {
				if ( _files[ i].equals( _dropTargetItem)) {
					arg0.rejectDrag();
					repaint();
					return;
				}
			}

			repaint();

			arg0.acceptDrag( arg0.getDropAction());
		} else {
			try {
				_dropTargetItem = ( File)getValueAt( rowAtPoint( position), columnAtPoint( position));
				if ( null == _dropTargetItem)
					arg0.acceptDrag( DnDConstants.ACTION_COPY);
				else {
					if ( 0 == columnAtPoint( position) && _dropTargetItem.isDirectory())
						arg0.acceptDrag( DnDConstants.ACTION_COPY);
					else {
						arg0.rejectDrag();
						repaint();
						return;
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				_dropTargetItem = null;
				arg0.acceptDrag( DnDConstants.ACTION_COPY);
				repaint();
				return;
			}	
		}

		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent arg0) {
		if ( null == _parent) {
			arg0.getDropTargetContext().dropComplete( true);
			_dropTargetItem = null;
			return;
		}

		try {
			Transferable transferable = arg0.getTransferable();
			if ( transferable.isDataFlavorSupported( DataFlavor.javaFileListFlavor)) {
				arg0.acceptDrop( arg0.getDropAction());
				List list = ( List)transferable.getTransferData( DataFlavor.javaFileListFlavor);
				if ( null == list || list.isEmpty()) {
					arg0.getDropTargetContext().dropComplete( true);
					_dropTargetItem = null;
					return;
				}

				File[] files =( File[])list.toArray( new File[ 0]);
				arg0.getDropTargetContext().dropComplete( true);
				on_paste( files, DnDConstants.ACTION_COPY);
			} else if ( transferable.isDataFlavorSupported( URISelection._uriFlavor)) {
				arg0.acceptDrop( arg0.getDropAction());
				String string = ( String)transferable.getTransferData( URISelection._uriFlavor);
				arg0.getDropTargetContext().dropComplete( true);
				if ( null == string) {
					_dropTargetItem = null;
					return;
				}

				StringTokenizer stringTokenizer = new StringTokenizer( string, "\r\n");
				List list = new ArrayList();
				while( stringTokenizer.hasMoreElements()) {
					URI uri = new URI( ( String)stringTokenizer.nextElement());
					if ( uri.getScheme().equals( "file"))
						list.add( new File( uri.getPath()));
				}
				File[] files =( File[])list.toArray( new File[ 0]);
				on_paste( files, DnDConstants.ACTION_COPY);
			} else {
				if ( DnDConstants.ACTION_COPY != arg0.getDropAction()
					&& DnDConstants.ACTION_MOVE != arg0.getDropAction()) {
					arg0.getDropTargetContext().dropComplete( true);
					_dropTargetItem = null;
					return;
				}

				if ( null == _dropTargetItem || null == _files) {
					arg0.getDropTargetContext().dropComplete( true);
					arg0.rejectDrop();
					repaint();
					return;
				}

				DataFlavor[] dataFlavors = arg0.getCurrentDataFlavors();
				if ( dataFlavors[ 0].getHumanPresentableName().equals( FileItemTransferable._name)) {
					arg0.acceptDrop( arg0.getDropAction());
					arg0.getDropTargetContext().dropComplete( true);
					if ( DnDConstants.ACTION_COPY == arg0.getDropAction()) {
						on_paste( _files, arg0.getDropAction());
					} else if ( DnDConstants.ACTION_MOVE == arg0.getDropAction()) {
						int[] rows = getSelectedRows();
						_fileManagerCallBack.on_start_paste_and_remove();
						paste_and_remove( _files, arg0.getDropAction());
						optimize_selection( rows);
						_fileManagerCallBack.on_paste_and_remove_completed();
					} else {
						arg0.rejectDrop();
						repaint();
						return;
					}
				} else {
					arg0.rejectDrop();
				}
			}
		} catch (IOException ioe) {
			arg0.rejectDrop();
		} catch (UnsupportedFlavorException ufe) {
			arg0.rejectDrop();
		} catch (InvalidDnDOperationException idoe) {
			arg0.rejectDrop();
		} catch (URISyntaxException e) {
			arg0.rejectDrop();
		}

		_dropTargetItem = null;
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent arg0) {
		_dropTargetItem = null;
		repaint();
	}

	/**
	 * @return
	 */
	public File[] get_selected_files() {
		// 選択されているファイルを返す
		int[] rows = getSelectedRows();
		if ( null == rows || 0 == rows.length)
			return null;

		File[] files = new File[ rows.length];
		for ( int i = 0; i < rows.length; ++i)
			files[ i] = ( File)getValueAt( rows[ i], 0);

		return files;
	}

	/**
	 * @param files
	 */
	public void restore_selection(File[] files) {
		// コピー元の選択状態を復元
		if ( 0 >= getRowCount())
			return;

		if ( null == files || 0 == files.length) {
			setRowSelectionInterval( 0, 0);
			return;
		}

		select( files);
	}

	/**
	 * @param rows
	 */
	public void optimize_selection(int[] rows) {
		// コピー元の選択状態を最適化
		clearSelection();
		if ( 0 < getRowCount()) {
			if ( 0 == rows.length || 0 > rows[ 0] || getRowCount() <= rows[ 0])
				setRowSelectionInterval( 0, 0);
			else
				setRowSelectionInterval( rows[ 0], rows[ 0]);
		}
	}

	/**
	 * @param files
	 * @param action
	 */
	private void on_paste(File[] files, int action) {
		if ( null == _parent)
			return;

		File[] selected_files = get_selected_files();
		_fileManagerCallBack.on_start_paste();
		paste( files, DnDConstants.ACTION_COPY);
		restore_selection( selected_files);
		_fileManagerCallBack.on_paste_completed();
	}

	/**
	 * @param files
	 * @param action
	 * @return
	 */
	private boolean paste(File[] files, int action) {
		File target_directory = ( null != _dropTargetItem) ? _dropTargetItem : _parent;

		int result = IntProgressDlg.execute(
			_owner,
			ResourceManager.get_instance().get( "file.manager.copy.message"),
			true,
			"copy",
			ResourceManager.get_instance().get( "dialog.cancel"),
			new Object[] { files, target_directory, new Integer( action), new Boolean( false), new Boolean( true)},
			this, _component);
		switch ( result) {
			case -1:
				Message.on_error_from_parent_to_child( _component);
				break;
			case -2:
				Message.on_error_copy_or_move( _component, action);
				break;
		}

		_directoryTreeBase.on_update( _parent);

		File[] files_to_be_selected = new File[ files.length];
		for ( int i = 0; i < files.length; ++i)
			files_to_be_selected[ i] = new File( _parent, files[ i].getName());

		select( files_to_be_selected);

		return true;
	}

	/**
	 * @param files
	 * @param action
	 * @return
	 */
	private boolean paste_and_remove(File[] files, int action) {
		File target_directory = _dropTargetItem;
		int result = IntProgressDlg.execute(
			_owner,
			ResourceManager.get_instance().get( "file.manager.move.message"),
			true,
			"move",
			ResourceManager.get_instance().get( "dialog.cancel"),
			new Object[] { files, target_directory, new Integer( action), new Boolean( false)},
			this, _component);
		switch ( result) {
			case -1:
				Message.on_error_from_parent_to_child( _component);
				break;
			case -2:
				Message.on_error_copy_or_move( _component, action);
				break;
			case -3:
				Message.on_error_move( _component);
				break;
		}

		_directoryTreeBase.on_update( _parent);

		File[] files_to_be_selected = new File[ files.length];
		for ( int i = 0; i < files.length; ++i)
			files_to_be_selected[ i] = new File( _parent, files[ i].getName());

		select( files_to_be_selected);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.progress.IIntProgressCallback#int_message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.progress.IntProgressDlg)
	 */
	public int int_message_callback(String id, Object[] objects, IntProgressDlg intProgressDlg) {
		if ( id.equals( "copy"))
			return copy( ( File[])objects[ 0], ( File)objects[ 1], ( ( Integer)objects[ 2]).intValue(), ( ( Boolean)objects[ 3]).booleanValue(), ( ( Boolean)objects[ 4]).booleanValue(), intProgressDlg);
		else if ( id.equals( "move"))
			return move( ( File[])objects[ 0], ( File)objects[ 1], ( ( Integer)objects[ 2]).intValue(), ( ( Boolean)objects[ 3]).booleanValue(), intProgressDlg);
		else if ( id.equals( "convert"))
			return convert( ( String)objects[ 0], ( String)objects[ 1], intProgressDlg);

		return 0;
	}

	/**
	 * @param files
	 * @param target_directory
	 * @param action
	 * @param auto
	 * @param check_modified
	 * @param fileManager
	 * @param fileManagerCallBack
	 * @param intProgressDlg
	 * @return
	 */
	private int copy(File[] files, File target_directory, int action, boolean auto, boolean check_modified, IntProgressDlg intProgressDlg) {
		return Utility.paste( get_files( files), target_directory, action, auto, check_modified, _fileManager, _fileManagerCallBack, _component, intProgressDlg);
	}

	/**
	 * @param files
	 * @param target_directory
	 * @param action
	 * @param auto
	 * @param intProgressDlg
	 * @return
	 */
	private int move(File[] files, File target_directory, int action, boolean auto, IntProgressDlg intProgressDlg) {
		int result = Utility.paste( get_files( files), target_directory, action, auto, true, _fileManager, _fileManagerCallBack, _component, intProgressDlg);
		if ( 0 < result) {
			for ( int i = 0; i < files.length; ++i) {
				if ( files[ i].isFile()) {
					if ( !files[ i].delete()) {
						return -3;
					}
					if ( null != _fileManagerCallBack)
						_fileManagerCallBack.modified( _fileManager);
				} else if ( files[ i].isDirectory()) {
					if ( !FileUtility.delete( files[ i], true)) {
						return -3;
					}
					if ( null != _fileManagerCallBack)
						_fileManagerCallBack.modified( _fileManager);
				} else {
					return -3;
				}
			}
		}
		return result;
	}

	/**
	 * @param files
	 * @return
	 */
	protected File[] get_files(File[] files) {
		return files;
	}

	/**
	 * 
	 */
	public void cleanup() {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		while ( 0 < getRowCount())
			defaultTableModel.removeRow( 0);
	}
}
