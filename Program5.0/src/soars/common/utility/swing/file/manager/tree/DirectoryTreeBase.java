/**
 * 
 */
package soars.common.utility.swing.file.manager.tree;

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
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import soars.common.utility.swing.file.manager.IFileManager;
import soars.common.utility.swing.file.manager.IFileManagerCallBack;
import soars.common.utility.swing.file.manager.ResourceManager;
import soars.common.utility.swing.file.manager.common.Message;
import soars.common.utility.swing.file.manager.common.Utility;
import soars.common.utility.swing.file.manager.table.FileItemTransferable;
import soars.common.utility.swing.file.manager.table.FileTableBase;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.swing.progress.IIntProgressCallback;
import soars.common.utility.swing.progress.IntProgressDlg;
import soars.common.utility.swing.tree.StandardTree;
import soars.common.utility.tool.clipboard.URISelection;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.sort.StringNumberComparator;

/**
 * @author kurata
 *
 */
public class DirectoryTreeBase extends StandardTree implements DragGestureListener, DragSourceListener, DropTargetListener, IIntProgressCallback {

	/**
	 * 
	 */
	protected FileTableBase _fileTableBase = null;

	/**
	 * 
	 */
	protected TreeNode _draggedTreeNode = null;

	/**
	 * 
	 */
	public TreeNode _dropTargetTreeNode = null;

	/**
	 * 
	 */
	protected IFileManagerCallBack _fileManagerCallBack = null;

	/**
	 * 
	 */
	protected IFileManager _fileManager = null;

	/**
	 * 
	 */
	protected Component _component = null;

	/**
	 * @param fileManagerCallBack
	 * @param fileManager
	 * @param component
	 * @param owner
	 * @param parent
	 */
	public DirectoryTreeBase(IFileManagerCallBack fileManagerCallBack, IFileManager fileManager, Component component, Frame owner, Component parent) {
		super(owner, parent);
		_fileManagerCallBack = fileManagerCallBack;
		_fileManager = fileManager;
		_component = component;
	}

	/**
	 * @param path
	 */
	public void set_current_directory(String path) {
	}

	/**
	 * @param path
	 */
	public void select(String path) {
	}

	/**
	 * @param defaultMutableTreeNode
	 */
	protected void select(DefaultMutableTreeNode defaultMutableTreeNode) {
		TreePath treePath = new TreePath( defaultMutableTreeNode.getPath());
		expand( defaultMutableTreeNode, treePath, null);
//		MessageDlg.execute( _owner, ResourceManager.get_instance().get( "application.title"), true, "expand",
//			ResourceManager.get_instance().get( "directory.tree.expanding.message"), new Object[] { defaultMutableTreeNode, treePath}, this, _parent);
		setSelectionPath( treePath);
	}

	/**
	 * @param popupMenu
	 * @param fileTableBase
	 * @return
	 */
	public boolean setup(boolean popupMenu, FileTableBase fileTableBase) {
		return setup( popupMenu, new DirectoryTreeCellRendererBase(), fileTableBase);
	}

	/**
	 * @param popupMenu
	 * @param fileTableBase
	 * @return
	 */
	public boolean setup(boolean popupMenu, DirectoryTreeCellRendererBase directoryTreeCellRendererBase, FileTableBase fileTableBase) {
		if ( !super.setup( popupMenu))
			return false;


		_fileTableBase = fileTableBase;


		getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION);


		Action enterAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_enter();
			}
		};

		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0), "enter");
		getActionMap().put( "enter", enterAction);


		addTreeSelectionListener( new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				on_valueChanged( e);
			}
		});


		setCellRenderer( directoryTreeCellRendererBase);


		new DragSource().createDefaultDragGestureRecognizer( this, DnDConstants.ACTION_COPY_OR_MOVE, this);
		new DropTarget( this, this);


		return true;
	}

	/**
	 * 
	 */
	protected void on_enter() {
		TreePath treePath = getSelectionPath();
		if ( null == treePath)
			return;

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		expand( defaultMutableTreeNode, treePath, null);
//		MessageDlg.execute( _owner, ResourceManager.get_instance().get( "application.title"), true, "expand",
//			ResourceManager.get_instance().get( "directory.tree.expanding.message"), new Object[] { defaultMutableTreeNode, treePath}, this, _parent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.tree.StandardTree#on_mouse_left_double_click(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_double_click(MouseEvent mouseEvent) {
		on_enter();
	}

	/**
	 * @param treeSelectionEvent
	 */
	protected void on_valueChanged(TreeSelectionEvent treeSelectionEvent) {
		TreePath treePath = treeSelectionEvent.getPath();
		if ( null == treePath)
			return;

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		expand( defaultMutableTreeNode, treePath, null);
//		MessageDlg.execute( _owner, ResourceManager.get_instance().get( "application.title"), true, "expand",
//			ResourceManager.get_instance().get( "directory.tree.expanding.message"), new Object[] { defaultMutableTreeNode, treePath}, this, _parent);

		_fileManagerCallBack.select_changed( _fileManager);
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();
		setSelectionPath( new TreePath( root.getPath()));
	}

	/**
	 * @param directory
	 */
	public void expand(File directory) {
	}

	/**
	 * @param directory
	 */
	public void on_update(File directory) {
		// ファイルが更新された時
		// 　ファイルが削除された時
		// 　ファイルが貼り付けられた時
	}

	/**
	 * @param directory
	 * @param oldPath
	 * @param newPath
	 */
	public void on_rename(File directory, File oldPath, File newPath) {
		// ディレクトリ名が変更された時
	}

	/**
	 * @param directory
	 */
	public void on_new_directory(File directory) {
		// 新しいディレクトリが作成された時
	}

	/**
	 * @param directory
	 * @param parent
	 * @return
	 */
	protected DefaultMutableTreeNode getNode(File directory, DefaultMutableTreeNode parent) {
		if ( null == directory || !directory.isDirectory())
			return null;

		File file = ( File)parent.getUserObject();
		if ( directory.equals( file))
			return parent;

		for ( int i = 0; i < parent.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)parent.getChildAt( i);
			child = getNode( directory, child);
			if ( null != child)
				return child;
		}

		return null;
	}

//	/* (non-Javadoc)
//	 * @see soars.common.utility.swing.message.IMessageCallback#message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.MessageDlg)
//	 */
//	public boolean message_callback(String id, Object[] objects, MessageDlg messageDlg) {
//		if ( id.equals( "expand"))
//			expand( ( DefaultMutableTreeNode)objects[ 0], ( TreePath)objects[ 1], messageDlg);
//
//		return true;
//	}

	/**
	 * @param parent
	 * @param treePath
	 * @param messageDlg
	 * @return
	 */
	protected boolean expand(DefaultMutableTreeNode parent, TreePath treePath, MessageDlg messageDlg) {
		File directory = ( File)parent.getUserObject();
		File[] files = directory.listFiles();
		if ( null == files)
			return false;

		List<File> list = new ArrayList<File>();
		for ( int i = 0; i < files.length; ++i) {
			if ( !files[ i].isDirectory())
				continue;

			list.add( files[ i]);
		}

		files = list.toArray( new File[ 0]);
		Arrays.sort( files, new DirectoryNameComparator( true, false));

		for ( int i = 0; i < files.length; ++i) {
			if ( exist( parent, files[ i]))
				continue;

			if ( !_fileManagerCallBack.visible( files[ i]))
				continue;

			insert( parent, files[ i]);
		}

		expandPath( treePath);

		return _fileTableBase.update( directory);
	}

	/**
	 * @param parent
	 * @param file
	 * @return
	 */
	protected boolean exist(DefaultMutableTreeNode parent, File file) {
		for ( int i = 0; i < parent.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)parent.getChildAt( i);
			if ( file.equals( ( File)child.getUserObject()))
				return true;
		}
		return false;
	}

	/**
	 * @param parent
	 * @param file
	 */
	private void insert(DefaultMutableTreeNode parent, File file) {
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		for ( int i = 0; i < parent.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)parent.getChildAt( i);
			String filename = ( ( File)child.getUserObject()).getName();
			if ( file.getName().equals( filename))
				return;

			if ( 0 > StringNumberComparator.compareTo( file.getName(), filename)) {
				defaultTreeModel.insertNodeInto( new DefaultMutableTreeNode( file), parent, i);
				return;
			}
		}
		defaultTreeModel.insertNodeInto( new DefaultMutableTreeNode( file), parent, parent.getChildCount());
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.DragGestureEvent)
	 */
	public void dragGestureRecognized(DragGestureEvent arg0) {
		Point point = arg0.getDragOrigin();
		TreePath treePath = getPathForLocation( point.x, point.y);
		if ( null == treePath || null == treePath.getParentPath())
			return;

		_draggedTreeNode = ( TreeNode)treePath.getLastPathComponent();
		Transferable transferable = new DirectoryNodeTransferable( _draggedTreeNode);
		new DragSource().startDrag( arg0, Cursor.getDefaultCursor(), transferable, this);
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragDropEnd(java.awt.dnd.DragSourceDropEvent)
	 */
	public void dragDropEnd(DragSourceDropEvent arg0) {
		_draggedTreeNode = null;
		_dropTargetTreeNode = null;
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
		_dropTargetTreeNode = null;
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent arg0) {
		_dropTargetTreeNode = null;
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent arg0) {
		Transferable transferable = arg0.getTransferable();
    DataFlavor[] dataFlavors = arg0.getCurrentDataFlavors();
		if ( !transferable.isDataFlavorSupported( DataFlavor.javaFileListFlavor)
			&& !transferable.isDataFlavorSupported( URISelection._uriFlavor)
			&& !dataFlavors[ 0].getHumanPresentableName().equals( DirectoryNodeTransferable._name)
			&& !dataFlavors[ 0].getHumanPresentableName().equals( FileItemTransferable._name)) {
			arg0.rejectDrag();
			return;
		}

		Point position = getMousePosition();
		if ( null == position) {
			arg0.rejectDrag();
			return;
		}

		TreePath treePath = getPathForLocation( position.x, position.y);
		if ( null == treePath) {
			arg0.rejectDrag();
			return;
		}

		// target must be file or directory
		DefaultMutableTreeNode targetDefaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		if ( !( targetDefaultMutableTreeNode.getUserObject() instanceof File)) {
			_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
			repaint();
			arg0.rejectDrag();
			return;
		}

		if ( dataFlavors[ 0].getHumanPresentableName().equals( DirectoryNodeTransferable._name)) {
			try {
				DefaultMutableTreeNode draggedDefaultMutableTreeNode = ( DefaultMutableTreeNode)arg0.getTransferable().getTransferData( DirectoryNodeTransferable._localObjectFlavor);

				// source must be directory
				if ( !( draggedDefaultMutableTreeNode.getUserObject() instanceof File)) {
					_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
					repaint();
					arg0.rejectDrag();
					return;
				}

				// source's parent must be directory
				DefaultMutableTreeNode parentDefaultMutableTreeNode = ( DefaultMutableTreeNode)draggedDefaultMutableTreeNode.getParent();
				if ( !( parentDefaultMutableTreeNode.getUserObject() instanceof File)) {
					_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
					repaint();
					arg0.rejectDrag();
					return;
				}

				// self
				if ( draggedDefaultMutableTreeNode.equals( targetDefaultMutableTreeNode)) {
					_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
					repaint();
					arg0.rejectDrag();
					return;
				}

				// same place
				TreeNode parentTreeNode = ( TreeNode)draggedDefaultMutableTreeNode.getParent();
				if ( null != parentTreeNode && parentTreeNode.equals( targetDefaultMutableTreeNode)) {
					_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
					repaint();
					arg0.rejectDrag();
					return;
				}

				// parent -> child
				parentTreeNode = ( TreeNode)targetDefaultMutableTreeNode.getParent();
				while ( null != parentTreeNode) {
					if ( draggedDefaultMutableTreeNode.equals( parentTreeNode)) {
						_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
						repaint();
						arg0.rejectDrag();
						return;
					}
					parentTreeNode = ( TreeNode)parentTreeNode.getParent();
				}

				arg0.acceptDrag( arg0.getDropAction());

			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
				_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
				repaint();
				arg0.rejectDrag();
				return;
			} catch (IOException e) {
				e.printStackTrace();
				_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
				repaint();
				arg0.rejectDrag();
				return;
			}
		} else if ( dataFlavors[ 0].getHumanPresentableName().equals( FileItemTransferable._name)) {
			try {
				File[] files = ( File[])arg0.getTransferable().getTransferData( FileItemTransferable._localObjectFlavor);
				if ( null == files) {
					_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
					repaint();
					arg0.rejectDrag();
					return;
				}

				// self
				File file = ( File)targetDefaultMutableTreeNode.getUserObject();
				for ( int i = 0; i < files.length; ++i) {
					if ( files[ i].equals( file)) {
						_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
						repaint();
						arg0.rejectDrag();
						return;
					}
				}

				// same place
				for ( int i = 0; i < files.length; ++i) {
					if ( files[ i].getParentFile().equals( file)) {
						_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
						repaint();
						arg0.rejectDrag();
						return;
					}
				}

				// parent -> child
				for ( int i = 0; i < files.length; ++i) {
					DefaultMutableTreeNode parentDefaultMutableTreeNode = ( DefaultMutableTreeNode)targetDefaultMutableTreeNode.getParent();
					while ( null != parentDefaultMutableTreeNode) {
						Object object = parentDefaultMutableTreeNode.getUserObject();
						if ( object instanceof File) {
							file = ( File)object;
							if ( files[ i].equals( file)) {
								_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
								repaint();
								arg0.rejectDrag();
								return;
							}
						}
//						file = ( File)parentDefaultMutableTreeNode.getUserObject();
//						if ( files[ i].equals( file)) {
//							_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
//							repaint();
//							arg0.rejectDrag();
//							return;
//						}
						parentDefaultMutableTreeNode = ( DefaultMutableTreeNode)parentDefaultMutableTreeNode.getParent();
					}
				}

				arg0.acceptDrag( arg0.getDropAction());

			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
				_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
				repaint();
				arg0.rejectDrag();
				return;
			} catch (IOException e) {
				e.printStackTrace();
				_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
				repaint();
				arg0.rejectDrag();
				return;
			}
			TreeNode targetTreeNode = ( TreeNode)treePath.getLastPathComponent();

		} else {
			arg0.acceptDrag( DnDConstants.ACTION_COPY);
		}

		_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent arg0) {
		if ( null == _dropTargetTreeNode) {
			arg0.rejectDrop();
			repaint();
			return;
		}

		try {
			Transferable transferable = arg0.getTransferable();
			if ( transferable.isDataFlavorSupported( DataFlavor.javaFileListFlavor)) {
				arg0.acceptDrop( arg0.getDropAction());
				List list = ( List)transferable.getTransferData( DataFlavor.javaFileListFlavor);
				if ( null == list || list.isEmpty()) {
					arg0.getDropTargetContext().dropComplete( true);
					_dropTargetTreeNode = null;
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
					_dropTargetTreeNode = null;
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
					_dropTargetTreeNode = null;
					return;
				}

				DataFlavor[] dataFlavors = arg0.getCurrentDataFlavors();
				if ( dataFlavors[ 0].getHumanPresentableName().equals( DirectoryNodeTransferable._name)) {
					if ( null == _dropTargetTreeNode) {
						arg0.getDropTargetContext().dropComplete( true);
						arg0.rejectDrop();
						repaint();
						return;
					}

					arg0.acceptDrop( arg0.getDropAction());
					TreeNode draggedTreeNode = ( TreeNode)arg0.getTransferable().getTransferData( DirectoryNodeTransferable._localObjectFlavor);
					if ( null == draggedTreeNode) {
						arg0.getDropTargetContext().dropComplete( true);
						_dropTargetTreeNode = null;
						return;
					}
					File[] files = new File[] { ( File)( ( DefaultMutableTreeNode)draggedTreeNode).getUserObject()};
					arg0.getDropTargetContext().dropComplete( true);
					if ( DnDConstants.ACTION_COPY == arg0.getDropAction())
						on_paste( files, arg0.getDropAction());
					else if ( DnDConstants.ACTION_MOVE == arg0.getDropAction()) {
						_fileManagerCallBack.on_start_paste_and_remove();
						paste_and_remove( files, arg0.getDropAction(), true);
						_fileManagerCallBack.on_paste_and_remove_completed();
					} else {
						arg0.rejectDrop();
						repaint();
						return;
					}
				} else if ( dataFlavors[ 0].getHumanPresentableName().equals( FileItemTransferable._name)) {
					arg0.acceptDrop( arg0.getDropAction());
					File[] files = ( File[])arg0.getTransferable().getTransferData( FileItemTransferable._localObjectFlavor);
					if ( null == files) {
						arg0.getDropTargetContext().dropComplete( true);
						_dropTargetTreeNode = null;
						return;
					}
					arg0.getDropTargetContext().dropComplete( true);
					if ( DnDConstants.ACTION_COPY == arg0.getDropAction())
						on_paste( files, arg0.getDropAction());
					else if ( DnDConstants.ACTION_MOVE == arg0.getDropAction()) {
						int[] rows = _fileTableBase.getSelectedRows();
						_fileManagerCallBack.on_start_paste_and_remove();
						paste_and_remove( files, arg0.getDropAction(), false);
						_fileTableBase.optimize_selection( rows);
						_fileManagerCallBack.on_paste_and_remove_completed();
					} else {
						arg0.rejectDrop();
						repaint();
						return;
					}
				} else {
					arg0.getDropTargetContext().dropComplete( true);
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

		_dropTargetTreeNode = null;
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent arg0) {
		_dropTargetTreeNode = null;
		repaint();
	}

	/**
	 * @param files
	 * @param action
	 */
	protected void on_paste(File[] files, int action) {
		File[] selected_files = _fileTableBase.get_selected_files();
		_fileManagerCallBack.on_start_paste();
		paste( files, action);
		_fileTableBase.restore_selection( selected_files);
		_fileManagerCallBack.on_paste_completed();
	}

	/**
	 * @param files
	 * @param action
	 * @return
	 */
	private boolean paste(File[] files, int action) {
		File srcDirectory = null;
		TreePath treePath = getSelectionPath();
		if ( null == treePath) {
			Message.on_error_copy_or_move( _component, action);
			return false;
		}

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		srcDirectory = ( File)defaultMutableTreeNode.getUserObject();

		DefaultMutableTreeNode targetNode = ( DefaultMutableTreeNode)_dropTargetTreeNode;

		File targetDirectory = ( File)targetNode.getUserObject();
		if ( null == targetDirectory) {
			Message.on_error_copy_or_move( _component, action);
			return false;
		}

		int result = IntProgressDlg.execute(
			_owner,
			ResourceManager.get_instance().get( "file.manager.copy.message"),
			true,
			"copy",
			ResourceManager.get_instance().get( "dialog.cancel"),
			new Object[] { files, targetDirectory, new Integer( action), new Boolean( false)},
			this, _component);
		switch ( result) {
			case -1:
				Message.on_error_from_parent_to_child( _component);
				break;
			case -2:
				Message.on_error_copy_or_move( _component, action);
				break;
		}

		treePath = new TreePath( targetNode.getPath());
		if ( null == treePath) {
			Message.on_error_copy_or_move( _component, action);
			return false;
		}

		if ( !isExpanded( treePath) && !treePath.equals( getSelectionPath()))
			return true;

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		for ( int i = 0; i < files.length; ++i) {
			File newDirectory = new File( targetDirectory, files[ i].getName());
			if ( newDirectory.exists() && newDirectory.isDirectory() && !has_this_child( targetNode, newDirectory)) {
				defaultTreeModel.insertNodeInto( new DefaultMutableTreeNode( newDirectory), targetNode, targetNode.getChildCount());
			}
		}

		on_update( srcDirectory);

		return true;
	}

	/**
	 * @param files
	 * @param action
	 * @param insideTree
	 * @return
	 */
	protected boolean paste_and_remove(File[] files, int action, boolean insideTree) {
		File srcDirectory = null;
		TreePath treePath = getSelectionPath();
		if ( null == treePath) {
			Message.on_error_copy_or_move( _component, action);
			return false;
		}

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		srcDirectory = ( File)defaultMutableTreeNode.getUserObject();
		if ( insideTree)
			// 移動元の親を選択する
			srcDirectory = srcDirectory.getParentFile();

		DefaultMutableTreeNode targetNode = ( DefaultMutableTreeNode)_dropTargetTreeNode;

		File targetDirectory = ( File)targetNode.getUserObject();
		if ( null == targetDirectory) {
			Message.on_error_copy_or_move( _component, action);
			return false;
		}

		int result = IntProgressDlg.execute(
			_owner,
			ResourceManager.get_instance().get( "file.manager.move.message"),
			true,
			"move",
			ResourceManager.get_instance().get( "dialog.cancel"),
			new Object[] { files, targetDirectory, new Integer( action), new Boolean( false)},
			this, _component);
		switch ( result) {
			case -1:
				Message.on_error_from_parent_to_child( _component);
				break;
			case -2:
				Message.on_error_copy_or_move( _component, action);
				break;
//			case -3:
//				Message.on_error_move( _fileManager);
//				break;
		}

		treePath = new TreePath( targetNode.getPath());
		if ( null == treePath) {
			Message.on_error_copy_or_move( _component, action);
			return false;
		}

		if ( isExpanded( treePath)) {
			DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
			for ( int i = 0; i < files.length; ++i) {
				File newDirectory = new File( targetDirectory, files[ i].getName());
				if ( newDirectory.exists() && newDirectory.isDirectory() && !has_this_child( targetNode, newDirectory)) {
					defaultTreeModel.insertNodeInto( new DefaultMutableTreeNode( newDirectory), targetNode, targetNode.getChildCount());
				}
			}
		}

		if ( 0 < result) {
			result = IntProgressDlg.execute(
				_owner,
				ResourceManager.get_instance().get( "file.manager.move.message"),
				true,
				"remove",
				ResourceManager.get_instance().get( "dialog.cancel"),
				new Object[] { files, targetDirectory, new Integer( action)},
				this, _component);
			switch ( result) {
				case -3:
					Message.on_error_move( _component);
					break;
			}
		}

		on_update( srcDirectory);

		return true;
	}

	/**
	 * @param defaultMutableTreeNode
	 * @param file
	 * @return
	 */
	private boolean has_this_child(DefaultMutableTreeNode defaultMutableTreeNode, File file) {
		for ( int i = 0; i < defaultMutableTreeNode.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)defaultMutableTreeNode.getChildAt( i);
			File f = ( File)child.getUserObject();
			if ( file.equals( ( File)child.getUserObject()))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.progress.IIntProgressCallback#int_message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.progress.IntProgressDlg)
	 */
	public int int_message_callback(String id, Object[] objects, IntProgressDlg intProgressDlg) {
		if ( id.equals( "copy"))
			return copy( ( File[])objects[ 0], ( File)objects[ 1], ( ( Integer)objects[ 2]).intValue(), ( ( Boolean)objects[ 3]).booleanValue(), intProgressDlg);
		else if ( id.equals( "move"))
			return move( ( File[])objects[ 0], ( File)objects[ 1], ( ( Integer)objects[ 2]).intValue(), ( ( Boolean)objects[ 3]).booleanValue(), intProgressDlg);
		else if ( id.equals( "remove"))
			return remove( ( File[])objects[ 0], intProgressDlg);

		return 0;
	}

	/**
	 * @param files
	 * @param targetDirectory
	 * @param action
	 * @param auto
	 * @param fileManager
	 * @param fileManagerCallBack
	 * @param intProgressDlg
	 * @return
	 */
	private int copy(File[] files, File targetDirectory, int action, boolean auto, IntProgressDlg intProgressDlg) {
		return Utility.paste( get_files( files), targetDirectory, action, auto, true, _fileManager, _fileManagerCallBack, _component, intProgressDlg);
	}

	/**
	 * @param files
	 * @param targetDirectory
	 * @param action
	 * @param auto
	 * @param intProgressDlg
	 * @return
	 */
	private int move(File[] files, File targetDirectory, int action, boolean auto, IntProgressDlg intProgressDlg) {
		return Utility.paste( get_files( files), targetDirectory, action, auto, true, _fileManager, _fileManagerCallBack, _component, intProgressDlg);
//		int result = Utility.paste( files, targetDirectory, action, auto, _fileManager, _fileManagerCallBack, intProgressDlg);
//		if ( 0 < result) {
//			for ( int i = 0; i < files.length; ++i) {
//				if ( files[ i].isFile()) {
//					if ( !files[ i].delete()) {
//						return -3;
//					}
//					if ( null != _fileManagerCallBack)
//						_fileManagerCallBack.modified( _fileManager);
//				} else if ( files[ i].isDirectory()) {
//					if ( !FileUtility.delete( files[ i], true)) {
//						return -3;
//					}
//					if ( null != _fileManagerCallBack)
//						_fileManagerCallBack.modified( _fileManager);
//				} else {
//					return -3;
//				}
//			}
//		}
//		return result;
	}

	/**
	 * @param files
	 * @param intProgressDlg
	 * @return
	 */
	private int remove(File[] files, IntProgressDlg intProgressDlg) {
		intProgressDlg.set( 100);
		files = get_files( files);
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
		return 1;
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
	public void refresh() {
	}
}
