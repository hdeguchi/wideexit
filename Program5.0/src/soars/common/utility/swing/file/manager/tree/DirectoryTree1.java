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
import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 *
 */
public class DirectoryTree1 extends DirectoryTreeBase {

	/**
	 * 
	 */
	private File _rootDirectory = null;

	/**
	 * @param rootDirectory
	 * @param fileManagerCallBack
	 * @param fileManager 
	 * @param owner
	 * @param parent
	 */
	public DirectoryTree1(File rootDirectory, IFileManagerCallBack fileManagerCallBack, IFileManager fileManager, Component component, Frame owner, Component parent) {
		super(fileManagerCallBack, fileManager, component, owner, parent);
		_rootDirectory = rootDirectory;
	}

	/**
	 * @return
	 */
	public String get_current_directory() {
		TreePath treePath = getSelectionPath();
		if ( null == treePath)
			return "";

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		File file = ( File)defaultMutableTreeNode.getUserObject();
		if ( null == file || !file.exists())
			return "";

		Object[] objects = ( Object[])treePath.getPath();

		String result = "";
		for ( int i = 1; i < objects.length; ++i) {
			defaultMutableTreeNode = ( DefaultMutableTreeNode)objects[ i];
			File directory = ( File)defaultMutableTreeNode.getUserObject();
			result += ( directory.getName() + ( ( i < objects.length - 1 ? "/" : "")));
		}

		return result;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#set_current_directory(java.lang.String)
	 */
	public void set_current_directory(String path) {
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();
		select( root);

		if ( null == path || path.equals( ""))
			return;

		String[] words = Tool.split( path, '/');
		if ( null == words)
			return;

		List<String> list = new ArrayList<String>();
		for ( int i = 0; i < words.length; ++i) {
			if ( ( 0 == i || words.length - 1 == i) && words[ i].equals( ""))
				continue;

			if ( words[ i].equals( ""))
				return;

			list.add( words[ i]);
		}

		if ( list.isEmpty())
			return;

		words = list.toArray( new String[ 0]);

		File directory = _rootDirectory;
		DefaultMutableTreeNode parent = root;
		for ( int i = 0; i < words.length; ++i) {
			directory = new File( directory, words[ i]);
			if ( !directory.exists() || !directory.isDirectory())
				return;

			parent = getNode( directory, parent);
			if ( null == parent)
				return;

			setSelectionPath( new TreePath( parent.getPath()));
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#select(java.lang.String)
	 */
	public void select(String path) {
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();
		select( root);

		if ( null == path || path.equals( ""))
			return;

		String[] words = Tool.split( path, '/');
		if ( null == words)
			return;

		List<String> list = new ArrayList<String>();
		for ( int i = 0; i < words.length; ++i) {
			if ( ( 0 == i || words.length - 1 == i) && words[ i].equals( ""))
				continue;

			if ( words[ i].equals( ""))
				return;

			list.add( words[ i]);
		}

		if ( list.isEmpty())
			return;

		words = list.toArray( new String[ 0]);

		File directory = _rootDirectory;
		DefaultMutableTreeNode parent = root;
		for ( int i = 0; i < words.length - 1; ++i) {
			directory = new File( directory, words[ i]);
			if ( !directory.exists() || !directory.isDirectory())
				return;

			parent = getNode( directory, parent);
			if ( null == parent)
				return;

			setSelectionPath( new TreePath( parent.getPath()));
		}

		_fileTableBase.select( new File( directory, words[ words.length - 1]));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#setup(boolean, soars.common.utility.swing.file.manager.table.FileTableBase)
	 */
	public boolean setup(boolean popupMenu, FileTableBase fileTableBase) {
		return setup( popupMenu, new DirectoryTreeCellRenderer1(), fileTableBase);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#setup(boolean, soars.common.utility.swing.file.manager.tree.DirectoryTreeCellRendererBase, soars.common.utility.swing.file.manager.table.FileTableBase)
	 */
	public boolean setup(boolean popupMenu, DirectoryTreeCellRendererBase directoryTreeCellRendererBase, FileTableBase fileTableBase) {
		if ( !super.setup( popupMenu, directoryTreeCellRendererBase, fileTableBase))
			return false;

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode( _rootDirectory);
		defaultTreeModel.setRoot( root);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#expand(java.io.File)
	 */
	public void expand(File directory) {
		if ( null == directory || !directory.isDirectory())
			return;

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();
		DefaultMutableTreeNode defaultMutableTreeNode = getNode( directory, root);
		if ( null == defaultMutableTreeNode)
			return;

		setSelectionPath( new TreePath( defaultMutableTreeNode.getPath()));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#on_update(java.io.File)
	 */
	public void on_update(File directory) {
		// ファイルが更新された時
		// 　ファイルが削除された時
		// 　ファイルが貼り付けられた時
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		Node node = new Node();
		if ( !node.create( ( DefaultMutableTreeNode)defaultTreeModel.getRoot(), this, _fileManagerCallBack))
			return;

		DefaultMutableTreeNode root = new DefaultMutableTreeNode( _rootDirectory);
		defaultTreeModel.setRoot( root);

		node.make( root, defaultTreeModel, this);

		expand( directory);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#on_rename(java.io.File, java.io.File, java.io.File)
	 */
	public void on_rename(File directory, File oldPath, File newPath) {
		// ディレクトリ名が変更された時
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		Node node = new Node();
		if ( !node.create( oldPath, newPath, ( DefaultMutableTreeNode)defaultTreeModel.getRoot(), this, _fileManagerCallBack))
			return;

		DefaultMutableTreeNode root = new DefaultMutableTreeNode( _rootDirectory);
		defaultTreeModel.setRoot( root);

		node.make( root, defaultTreeModel, this);

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
		DefaultMutableTreeNode defaultMutableTreeNode = getNode( directory, root);
		if ( null == defaultMutableTreeNode)
			return;

		expand( defaultMutableTreeNode, new TreePath( defaultMutableTreeNode.getPath()), null);
	}

	/**
	 * @param rootDirectory
	 */
	public void update(File rootDirectory) {
		// ルートディレクトリを更新
		_rootDirectory = rootDirectory;

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode( _rootDirectory);
		defaultTreeModel.setRoot( root);

		expand( rootDirectory);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#refresh()
	 */
	public void refresh() {
		String currentDirectory = get_current_directory();

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		Node node = new Node();
		if ( !node.create( ( DefaultMutableTreeNode)defaultTreeModel.getRoot(), this, _fileManagerCallBack))
			return;

		DefaultMutableTreeNode root = new DefaultMutableTreeNode( _rootDirectory);
		defaultTreeModel.setRoot( root);

		node.make( root, defaultTreeModel, this);

		select( currentDirectory);
	}
}
