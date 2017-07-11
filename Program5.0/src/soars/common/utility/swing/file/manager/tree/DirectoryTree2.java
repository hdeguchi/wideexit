/**
 * 
 */
package soars.common.utility.swing.file.manager.tree;

import java.awt.Component;
import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import soars.common.utility.swing.file.manager.IFileManager;
import soars.common.utility.swing.file.manager.IFileManagerCallBack;
import soars.common.utility.swing.file.manager.table.FileTableBase;
import soars.common.utility.swing.message.MessageDlg;

/**
 * @author kurata
 *
 */
public class DirectoryTree2 extends DirectoryTreeBase {

	/**
	 * 
	 */
	protected File[] _rootDirectories = null;

	/**
	 * @param rootDirectories
	 * @param fileManagerCallBack
	 * @param fileManager 
	 * @param owner
	 * @param parent
	 */
	public DirectoryTree2(File[] rootDirectories, IFileManagerCallBack fileManagerCallBack, IFileManager fileManager, Component component, Frame owner, Component parent) {
		super(fileManagerCallBack, fileManager, component, owner, parent);
		_rootDirectories = rootDirectories;
	}

	/**
	 * @return
	 */
	public File get_current_directory() {
		TreePath treePath = getSelectionPath();
		if ( null == treePath)
			return null;

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		Object object = defaultMutableTreeNode.getUserObject();

		if ( !( object instanceof File))
			return null;

		return ( File)object;
	}

	/**
	 * @param directory
	 * @param files
	 */
	public void select(File directory, File[] files) {
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();

		if ( null == directory || !directory.exists()) {
			setSelectionPath( new TreePath( ( ( DefaultMutableTreeNode)root.getChildAt( 0)).getPath()));
			return;
		}

		for ( int i = 0; i < root.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)root.getChildAt( i);
			if ( select( child, directory, files))
				return;
		}

		setSelectionPath( new TreePath( ( ( DefaultMutableTreeNode)root.getChildAt( 0)).getPath()));
	}

	/**
	 * @param parent
	 * @param directory
	 * @param files
	 * @return
	 */
	private boolean select(DefaultMutableTreeNode parent, File directory, File[] files) {
		if ( directory.equals( ( File)parent.getUserObject())) {
			setSelectionPath( new TreePath( parent.getPath()));
			if ( null != files && exist( files))
				_fileTableBase.select( files);

			return true;
		}

		for ( int i = 0; i < parent.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)parent.getChildAt( i);
			DefaultMutableTreeNode defaultMutableTreeNode = getNode( directory, child);
			if ( null == defaultMutableTreeNode)
				continue;

			setSelectionPath( new TreePath( defaultMutableTreeNode.getPath()));

			if ( null != files && exist( files))
				_fileTableBase.select( files);

			return true;
		}
		return false;
	}

	/**
	 * @param files
	 * @return
	 */
	private boolean exist(File[] files) {
		for ( int i = 0; i < files.length; ++i) {
			if ( files[ i].exists())
				return true;
		}
		return false;
	}

//	/* (non-Javadoc)
//	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#set_current_directory(java.lang.String)
//	 */
//	public void set_current_directory(String path) {
//		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
//		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();
//		select( root);
//
//		if ( null == path || path.equals( ""))
//			return;
//
//		String[] words = Tool.split( path, '/');
//		if ( null == words)
//			return;
//
//		List<String> list = new ArrayList<String>();
//		for ( int i = 0; i < words.length; ++i) {
//			if ( ( 0 == i || words.length - 1 == i) && words[ i].equals( ""))
//				continue;
//
//			if ( words[ i].equals( ""))
//				return;
//
//			list.add( words[ i]);
//		}
//
//		if ( list.isEmpty())
//			return;
//
//		words = list.toArray( new String[ 0]);
//
//		boolean select = false;
//		for ( int i = 0; i < _root_directories.length; ++i) {
//			File directory = _root_directories[ i];
//			DefaultMutableTreeNode parent = getNode( directory, root);
//			for ( int j = 0; j < words.length; ++j) {
//				directory = new File( directory, words[ j]);
//				if ( !directory.exists() || !directory.isDirectory())
//					break;
//
//				parent = getNode( directory, parent);
//				if ( null == parent)
//					break;
//
//				setSelectionPath( new TreePath( parent.getPath()));
//				select = true;
//			}
//
//			if ( select)
//				break;
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#select(java.lang.String)
//	 */
//	public void select(String path) {
//		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
//		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();
//		select( root);
//
//		if ( null == path || path.equals( ""))
//			return;
//
//		String[] words = Tool.split( path, '/');
//		if ( null == words)
//			return;
//
//		List<String> list = new ArrayList<String>();
//		for ( int i = 0; i < words.length; ++i) {
//			if ( ( 0 == i || words.length - 1 == i) && words[ i].equals( ""))
//				continue;
//
//			if ( words[ i].equals( ""))
//				return;
//
//			list.add( words[ i]);
//		}
//
//		if ( list.isEmpty())
//			return;
//
//		words = list.toArray( new String[ 0]);
//
//		boolean select = false;
//		for ( int i = 0; i < _root_directories.length; ++i) {
//			File directory = _root_directories[ i];
//			DefaultMutableTreeNode parent = getNode( directory, root);
//			for ( int j = 0; j < words.length - 1; ++j) {
//				directory = new File( directory, words[ j]);
//				if ( !directory.exists() || !directory.isDirectory())
//					break;
//
//				parent = getNode( directory, parent);
//				if ( null == parent)
//					break;
//
//				setSelectionPath( new TreePath( parent.getPath()));
//				select = true;
//			}
//
//			if ( select) {
//				_fileTableBase.select( new File( directory, words[ words.length - 1]));
//				break;
//			}
//		}
//	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#setup(boolean, soars.common.utility.swing.file.manager.table.FileTableBase)
	 */
	public boolean setup(boolean popupMenu, FileTableBase fileTableBase) {
		return setup( popupMenu, new DirectoryTreeCellRenderer2(), fileTableBase);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#setup(boolean, soars.common.utility.swing.file.manager.tree.DirectoryTreeCellRendererBase, soars.common.utility.swing.file.manager.table.FileTableBase)
	 */
	public boolean setup(boolean popupMenu, DirectoryTreeCellRendererBase directoryTreeCellRendererBase, FileTableBase fileTableBase) {
		if ( !super.setup(popupMenu, directoryTreeCellRendererBase, fileTableBase))
			return false;

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode( "");
		defaultTreeModel.setRoot( root);

		for ( int i = 0; i < _rootDirectories.length; ++i) {
			DefaultMutableTreeNode child = new DefaultMutableTreeNode( _rootDirectories[ i]);
			root.add( child);
		}

		expandPath( new TreePath( root.getPath()));

		setRootVisible( false);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#on_setup_completed()
	 */
	public void on_setup_completed() {
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();
		DefaultMutableTreeNode child = ( DefaultMutableTreeNode)root.getChildAt( 0);
		setSelectionPath( new TreePath( child.getPath()));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#expand(java.io.File)
	 */
	public void expand(File directory) {
		if ( null == directory || !directory.isDirectory())
			return;

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();
		for ( int i = 0; i < root.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)root.getChildAt( i);
			DefaultMutableTreeNode defaultMutableTreeNode = getNode( directory, child);
			if ( null == defaultMutableTreeNode)
				continue;

			setSelectionPath( new TreePath( defaultMutableTreeNode.getPath()));
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#on_update(java.io.File)
	 */
	public void on_update(File directory) {
		// ファイルが更新された時
		// 　ファイルが削除された時
		// 　ファイルが貼り付けられた時
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();
		List<Node> nodes = new ArrayList<Node>();
		for ( int i = 0; i < root.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)root.getChildAt( i);
			Node node = new Node();
			if ( !node.create( child, this, _fileManagerCallBack))
				continue;

			nodes.add( node);
		}

		root = new DefaultMutableTreeNode( "");
		defaultTreeModel.setRoot( root);
		expandPath( new TreePath( root.getPath()));

		for ( int i = 0; i < nodes.size(); ++i) {
			DefaultMutableTreeNode child = new DefaultMutableTreeNode( nodes.get( i)._directory);
			defaultTreeModel.insertNodeInto( child, root, root.getChildCount());
			nodes.get( i).make( child, defaultTreeModel, this);
		}

		expand( directory);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#on_rename(java.io.File, java.io.File, java.io.File)
	 */
	public void on_rename(File directory, File oldPath, File newPath) {
		// ディレクトリ名が変更された時
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();
		List<Node> nodes = new ArrayList<Node>();
		for ( int i = 0; i < root.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)root.getChildAt( i);
			Node node = new Node();
			if ( !node.create( oldPath, newPath, child, this, _fileManagerCallBack))
				return;

			nodes.add( node);
		}

		root = new DefaultMutableTreeNode( "");
		defaultTreeModel.setRoot( root);
		expandPath( new TreePath( root.getPath()));

		for ( int i = 0; i < nodes.size(); ++i) {
			DefaultMutableTreeNode child = new DefaultMutableTreeNode( nodes.get( i)._directory);
			defaultTreeModel.insertNodeInto( child, root, root.getChildCount());
			nodes.get( i).make( child, defaultTreeModel, this);
		}

		expand( directory);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#on_new_directory(java.io.File)
	 */
	public void on_new_directory(File directory) {
		// 新しいディレクトリが作成された時
		if ( null == directory || !directory.isDirectory())
			return;

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();
		for ( int i = 0; i < root.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)root.getChildAt( i);
			DefaultMutableTreeNode defaultMutableTreeNode = getNode( directory, child);
			if ( null == defaultMutableTreeNode)
				continue;

			expand( defaultMutableTreeNode, new TreePath( defaultMutableTreeNode.getPath()), null);
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#expand(javax.swing.tree.DefaultMutableTreeNode, javax.swing.tree.TreePath, soars.common.utility.swing.message.MessageDlg)
	 */
	protected boolean expand(DefaultMutableTreeNode parent, TreePath treePath, MessageDlg messageDlg) {
		Object object = parent.getUserObject();
		if ( object instanceof File)
			return super.expand(parent, treePath, messageDlg);

		expandPath( treePath);
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#refresh()
	 */
	public void refresh() {
		File currentDirectory = get_current_directory();
		File[] files = _fileTableBase.get_selected_files();

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();

		List<Node> nodes = new ArrayList<Node>();

		for ( int i = 0; i < root.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)root.getChildAt( i);
			Node node = new Node();
			if ( !node.create( child, this, _fileManagerCallBack))
				continue;

			nodes.add( node);
		}

		root = new DefaultMutableTreeNode( "");
		defaultTreeModel.setRoot( root);
		expandPath( new TreePath( root.getPath()));

		for ( int i = 0; i < nodes.size(); ++i) {
			DefaultMutableTreeNode child = new DefaultMutableTreeNode( nodes.get( i)._directory);
			defaultTreeModel.insertNodeInto( child, root, root.getChildCount());
			nodes.get( i).make( child, defaultTreeModel, this);
		}

		select( currentDirectory, files);
	}
}
