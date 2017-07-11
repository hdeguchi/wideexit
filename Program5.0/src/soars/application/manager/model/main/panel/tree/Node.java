/**
 * 
 */
package soars.application.manager.model.main.panel.tree;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import soars.application.manager.model.main.panel.tree.property.NodeProperty;
import soars.application.manager.model.main.panel.tree.property.TableSelection;
import soars.common.utility.swing.file.manager.IFileManagerCallBack;

/**
 * @author kurata
 *
 */
public class Node {

	/**
	 * 
	 */
	public File _file = null;

	/**
	 * 
	 */
	public boolean _expand = false;

	/**
	 * 
	 */
	public Node[] _childs = null;

	/**
	 * 
	 */
	private Map<File, NodeProperty> _nodePropertyMap = new HashMap<File, NodeProperty>();

	/**
	 * @param nodePropertyMap
	 */
	public Node(Map<File, NodeProperty> nodePropertyMap) {
		super();
		_nodePropertyMap = nodePropertyMap;
	}

	/**
	 * @param file
	 * @param nodePropertyMap
	 */
	private Node(File file, Map<File, NodeProperty> nodePropertyMap) {
		super();
		_file = file;
		_nodePropertyMap = nodePropertyMap;
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
		_file = ( File)defaultMutableTreeNode.getUserObject();
		if ( null == _file || !_file.exists() || !_file.isDirectory())
			return false;

		_expand = tree.isExpanded( new TreePath( defaultMutableTreeNode.getPath()));

		List<Node> list = new ArrayList<Node>();
		for ( int i = 0; i < defaultMutableTreeNode.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)defaultMutableTreeNode.getChildAt( i);
			File file = ( File)child.getUserObject();
			if ( !file.exists())
				continue;

			if ( file.isFile()) {
				list.add( new Node( file, _nodePropertyMap));
				continue;
			}

			if ( !file.isDirectory())
				continue;

			Node node = new Node( _nodePropertyMap);
			if ( !node.create( child, tree, fileManagerCallBack))
				return false;

			list.add( node);
		}

		if ( _expand) {
			File[] files = _file.listFiles();
			for ( int i = 0; i < files.length; ++i) {
				if ( !fileManagerCallBack.visible( files[ i]))
					continue;

				if ( exists( files[ i], list))
					continue;

				list.add( new Node( files[ i], _nodePropertyMap));
			}
		}

		_childs = list.toArray( new Node[ 0]);
		Arrays.sort( _childs, new NodeComparator( _nodePropertyMap, true, false));

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
		_file = ( File)defaultMutableTreeNode.getUserObject();
		if ( _file.equals( oldPath))
			_file = newPath;

		_expand = tree.isExpanded( new TreePath( defaultMutableTreeNode.getPath()));

		List<Node> list = new ArrayList<Node>();
		for ( int i = 0; i < defaultMutableTreeNode.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)defaultMutableTreeNode.getChildAt( i);
			File file = ( File)child.getUserObject();
			if ( !file.exists())
				continue;

			if ( file.isFile()) {
				list.add( new Node( file.equals( oldPath) ? newPath : file, _nodePropertyMap));
				continue;
			}

			if ( !file.isDirectory())
				continue;

			Node node = new Node( _nodePropertyMap);
			if ( !node.create( oldPath, newPath, child, tree, fileManagerCallBack))
				return false;

			list.add( node);
		}

		if ( _expand) {
			File[] files = _file.listFiles();
			for ( int i = 0; i < files.length; ++i) {
				if ( !fileManagerCallBack.visible( files[ i]))
					continue;

				if ( exists( files[ i], list))
					continue;

				list.add( new Node( files[ i], _nodePropertyMap));
			}
		}

		_childs = list.toArray( new Node[ 0]);
		Arrays.sort( _childs, new NodeComparator( _nodePropertyMap, true, false));

		return true;
	}

	/**
	 * @param file
	 * @param list
	 * @return
	 */
	private boolean exists(File file, List<Node> list) {
		for ( int i = 0; i < list.size(); ++i) {
			if ( list.get( i)._file.equals( file))
				return true;
		}
		return false;
	}

	/**
	 * @param defaultMutableTreeNode
	 * @param defaultTreeModel
	 * @param tree
	 * @return
	 */
	public boolean make(DefaultMutableTreeNode defaultMutableTreeNode, DefaultTreeModel defaultTreeModel, JTree tree) {
		// 写しから木を復元
		DefaultMutableTreeNode[] childs = new DefaultMutableTreeNode[ _childs.length];
		for ( int i = 0; i < _childs.length; ++i) {
			childs[ i] = new DefaultMutableTreeNode( _childs[ i]._file);
			NodeProperty.update_nodePropertyMap( _childs[ i]._file, _nodePropertyMap);
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

	/**
	 * @param defaultMutableTreeNode
	 * @param defaultTreeModel
	 * @param tree
	 * @param tableSelectionMap
	 * @return
	 */
	public boolean make(DefaultMutableTreeNode defaultMutableTreeNode, DefaultTreeModel defaultTreeModel, JTree tree, Map<File, TableSelection> tableSelectionMap) {
		// 写しから木を復元
		DefaultMutableTreeNode[] childs = new DefaultMutableTreeNode[ _childs.length];
		for ( int i = 0; i < _childs.length; ++i) {
			childs[ i] = new DefaultMutableTreeNode( _childs[ i]._file);
			NodeProperty.update_nodePropertyMap( _childs[ i]._file, _nodePropertyMap, tableSelectionMap);
			defaultTreeModel.insertNodeInto( childs[ i], defaultMutableTreeNode, defaultMutableTreeNode.getChildCount());
		}

		if ( _expand)
			tree.expandPath( new TreePath( defaultMutableTreeNode.getPath()));

		for ( int i = 0; i < _childs.length; ++i) {
			if ( !_childs[ i].make( childs[ i], defaultTreeModel, tree, tableSelectionMap))
				return false;
		}

		return true;
	}
}
