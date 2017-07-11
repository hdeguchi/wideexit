/**
 * 
 */
package soars.common.utility.swing.file.manager.tree;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import soars.common.utility.swing.file.manager.IFileManagerCallBack;

/**
 * @author kurata
 *
 */
public class Node {

	/**
	 * 
	 */
	public File _directory = null;

	/**
	 * 
	 */
	public boolean _expand = false;

	/**
	 * 
	 */
	public Node[] _childs = null;

	/**
	 * @param defaultMutableTreeNode
	 * @param tree
	 */
	public Node() {
		super();
	}

	/**
	 * @param directory
	 */
	public Node(File directory) {
		super();
		_directory = directory;
		_childs = new Node[ 0];
	}

	/**
	 * @param defaultMutableTreeNode
	 * @param tree
	 * @param fileManagerCallBack
	 * @return
	 */
	public boolean create(DefaultMutableTreeNode defaultMutableTreeNode, JTree tree, IFileManagerCallBack fileManagerCallBack) {
		// 木の写しを作成する
		// 存在しないディレクトリを指しているノードは自動的に省かれる
		// 展開しているノードに存在しているディレクトリが含まれていない場合、
		// これに対応するノードを新たに作成して追加する(但し、新たに追加されたノードは展開しない)
		_directory = ( File)defaultMutableTreeNode.getUserObject();
		if ( null == _directory || !_directory.exists() || !_directory.isDirectory())
			return false;

		_expand = tree.isExpanded( new TreePath( defaultMutableTreeNode.getPath()));

		List<Node> list = new ArrayList<Node>();
		for ( int i = 0; i < defaultMutableTreeNode.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)defaultMutableTreeNode.getChildAt( i);
			File directory = ( File)child.getUserObject();
			if ( !directory.exists() || !directory.isDirectory())
				continue;

			Node node = new Node();
			if ( !node.create( child, tree, fileManagerCallBack))
				return false;

			list.add( node);
		}

		if ( _expand) {
			File[] files = _directory.listFiles();
			for ( int i = 0; i < files.length; ++i) {
				if ( !files[ i].isDirectory())
					continue;

				if ( !fileManagerCallBack.can_paste( files[ i]))
					continue;

				if ( exists( files[ i], list))
					continue;

				list.add( new Node( files[ i]));
			}
		}

		_childs = list.toArray( new Node[ 0]);
		Arrays.sort( _childs, new NodeComparator( true, false));

		return true;
	}

	/**
	 * @param oldPath
	 * @param newPath
	 * @param defaultMutableTreeNode
	 * @param tree
	 * @param fileManagerCallBack
	 * @return
	 */
	public boolean create(File oldPath, File newPath, DefaultMutableTreeNode defaultMutableTreeNode, JTree tree, IFileManagerCallBack fileManagerCallBack) {
		// 木の写しを作成する
		// 名前が変わるところは新しい名前を保持する
		// 存在しないディレクトリを指しているノードは自動的に省かれる
		// 但し、これは名前が変わっても親が変わらない場合のみ有効 → 親も変わる場合は別途メソッドが必要
		// 展開しているノードに存在しているディレクトリが含まれていない場合、
		// これに対応するノードを新たに作成して追加する(但し、新たに追加されたノードは展開しない)
		_directory = ( File)defaultMutableTreeNode.getUserObject();
		if ( _directory.equals( oldPath))
			_directory = newPath;

		_expand = tree.isExpanded( new TreePath( defaultMutableTreeNode.getPath()));

		List<Node> list = new ArrayList<Node>();
		for ( int i = 0; i < defaultMutableTreeNode.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)defaultMutableTreeNode.getChildAt( i);
			File directory = ( File)child.getUserObject();
			if ( !directory.exists() || !directory.isDirectory())
				continue;

			Node node = new Node();
			if ( !node.create( oldPath, newPath, child, tree, fileManagerCallBack))
				return false;

			list.add( node);
		}

		if ( _expand) {
			File[] files = _directory.listFiles();
			for ( int i = 0; i < files.length; ++i) {
				if ( !files[ i].isDirectory())
					continue;

				if ( !fileManagerCallBack.can_paste( files[ i]))
					continue;

				if ( exists( files[ i], list))
					continue;

				list.add( new Node( files[ i]));
			}
		}

		_childs = list.toArray( new Node[ 0]);
		Arrays.sort( _childs, new NodeComparator( true, false));

		return true;
	}

	/**
	 * @param directory
	 * @param list
	 * @return
	 */
	private boolean exists(File directory, List<Node> list) {
		for ( int i = 0; i < list.size(); ++i) {
			if ( list.get( i)._directory.equals( directory))
				return true;
		}
		return false;
	}

	/**
	 * @param root
	 * @param defaultTreeModel
	 * @param tree
	 * @return
	 */
	public boolean make(DefaultMutableTreeNode defaultMutableTreeNode, DefaultTreeModel defaultTreeModel, JTree tree) {
		// 写しから木を復元
		DefaultMutableTreeNode[] childs = new DefaultMutableTreeNode[ _childs.length];
		for ( int i = 0; i < _childs.length; ++i) {
			childs[ i] = new DefaultMutableTreeNode( _childs[ i]._directory);
			defaultTreeModel.insertNodeInto( childs[ i], defaultMutableTreeNode, defaultMutableTreeNode.getChildCount());
		}

		if ( _expand)
			tree.expandPath( new TreePath( defaultMutableTreeNode.getPath()));

		for ( int i = 0; i < _childs.length; ++i) {
			if ( !_childs[ i].make( childs[ i], defaultTreeModel, tree))
				return false;
		}

		return true;
	}
}
