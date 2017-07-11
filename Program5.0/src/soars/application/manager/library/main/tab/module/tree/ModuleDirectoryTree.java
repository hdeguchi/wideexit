/**
 * 
 */
package soars.application.manager.library.main.tab.module.tree;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JEditorPane;
import javax.swing.JMenuItem;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import soars.application.manager.library.main.Constant;
import soars.application.manager.library.main.ResourceManager;
import soars.application.manager.library.main.tab.common.data.NodeData;
import soars.application.manager.library.main.tab.common.menu.EditModuleAction;
import soars.application.manager.library.main.tab.common.menu.IContextMenuHandler;
import soars.application.manager.library.main.tab.tab.InternalTabbedPane;
import soars.application.manager.library.module.EditModuleDlg;
import soars.common.soars.module.Module;
import soars.common.soars.module.ModuleNameComparator;
import soars.common.utility.swing.file.manager.IFileManager;
import soars.common.utility.swing.file.manager.IFileManagerCallBack;
import soars.common.utility.swing.file.manager.table.FileTableBase;
import soars.common.utility.swing.file.manager.tree.DirectoryTreeBase;
import soars.common.utility.swing.file.manager.tree.DirectoryTreeCellRendererBase;
import soars.common.utility.swing.file.manager.tree.Node;
import soars.common.utility.swing.message.MessageDlg;

/**
 * @author kurata
 *
 */
public class ModuleDirectoryTree extends DirectoryTreeBase implements IContextMenuHandler {

	/**
	 * 
	 */
	protected String[] _rootDirectories = null;

	/**
	 * 
	 */
	protected JMenuItem _editModuleMenuItem = null;

	/**
	 * 
	 */
	private InternalTabbedPane _internalTabbedPane = null;

	/**
	 * 
	 */
	private JEditorPane _propertEditorPane = null;

	/**
	 * 
	 */
	private String _previousText = "";

	/**
	 * 
	 */
	public boolean _flag = true;	// 苦肉の策、美しくない

	/**
	 * @param rootDirectories
	 * @param fileManagerCallBack
	 * @param fileManager
	 * @param component
	 * @param propertyEditorPane
	 * @param owner
	 * @param parent
	 */
	public ModuleDirectoryTree(String[] rootDirectories, IFileManagerCallBack fileManagerCallBack, IFileManager fileManager, Component component, Frame owner, Component parent) {
		super(fileManagerCallBack, fileManager, component, owner, parent);
		_rootDirectories = rootDirectories;
	}

	/**
	 * @param popupMenu
	 * @param directoryTreeCellRendererBase
	 * @param fileTableBase
	 * @param internalTabbedPane
	 * @param propertyEditorPane
	 * @return
	 */
	public boolean setup(boolean popupMenu, DirectoryTreeCellRendererBase directoryTreeCellRendererBase, FileTableBase fileTableBase, InternalTabbedPane internalTabbedPane, JEditorPane propertyEditorPane) {
		if ( !super.setup(popupMenu, directoryTreeCellRendererBase, fileTableBase))
			return false;

		_internalTabbedPane = internalTabbedPane;
		_propertEditorPane = propertyEditorPane;

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
	 * @see soars.common.utility.swing.tree.StandardTree#setup_popup_menu()
	 */
	protected void setup_popup_menu() {
		super.setup_popup_menu();

		_editModuleMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "popup.menu.edit.module.menu"),
			new EditModuleAction( ResourceManager.get_instance().get( "popup.menu.edit.module.menu"), this),
			ResourceManager.get_instance().get( "popup.menu.edit.module.mnemonic"),
			ResourceManager.get_instance().get( "popup.menu.edit.module.stroke"));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.tree.StandardTree#on_mouse_right_up(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
		if ( null == _userInterface)
			return;

		Point point = mouseEvent.getPoint();

		TreePath treePath = getPathForLocation( point.x, point.y);
		if ( null == treePath)
			return;

		setSelectionPath( treePath);

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();

		_editModuleMenuItem.setEnabled(
			defaultMutableTreeNode.getUserObject() instanceof File
			&& ( null != get_module( ( File)defaultMutableTreeNode.getUserObject())));

		_popupMenu.show( this, point.x, point.y);
	}

	/* (non-Javadoc)
	 * @see soars.application.modulemanager.main.tab.common.menu.IContextMenuHandler#on_edit_module(java.awt.event.ActionEvent)
	 */
	public void on_edit_module(ActionEvent actionEvent) {
		TreePath treePath = getSelectionPath();
		if ( null == treePath)
			return;

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		Object object = defaultMutableTreeNode.getUserObject();
		if ( object instanceof String)
			return;

		File directory = ( File)object;
		Module module = get_module( directory);
		if ( null == module)
			return;

		String originalName = module.getName();
		EditModuleDlg editModuleDlg = new EditModuleDlg( _owner, ResourceManager.get_instance().get( "edit.module.dialog.title"), true, module);
		if ( !editModuleDlg.do_modal())
			return;

		if ( !module.getName().equals( originalName))
			on_update(directory);

		update();
	}

	/* (non-Javadoc)
	 * @see soars.application.modulemanager.main.tab.common.menu.IContextMenuHandler#on_enable_module(java.awt.event.ActionEvent)
	 */
	public void on_enable_module(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.modulemanager.main.tab.common.menu.IContextMenuHandler#on_disable_module(java.awt.event.ActionEvent)
	 */
	public void on_disable_module(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.library.main.tab.common.menu.IContextMenuHandler#on_update_annotation_file(java.awt.event.ActionEvent)
	 */
	public void on_update_annotation_file(ActionEvent actionEvent) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return
	 */
	public Object get_selected_object() {
		TreePath treePath = getSelectionPath();
		if ( null == treePath)
			return null;

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		Object object = defaultMutableTreeNode.getUserObject();

		if ( !( object instanceof File) && !( object instanceof String))
			return null;

		return object;
	}

	/**
	 * @param object
	 * @param files
	 */
	public void select(Object object, File[] files) {
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();

		if ( object instanceof String) {
			String rootDirectory = ( String)object;
			for ( int i = 0; i < root.getChildCount(); ++i) {
				DefaultMutableTreeNode child = ( DefaultMutableTreeNode)root.getChildAt( i);
				if ( rootDirectory.equals( ( String)child.getUserObject())) {
					setSelectionPath( new TreePath( child.getPath()));
					return;
				}
			}
		} else if ( object instanceof File) {
			File directory = ( File)object;
			if ( null == directory || !directory.exists()) {
				setSelectionPath( new TreePath( ( ( DefaultMutableTreeNode)root.getChildAt( 0)).getPath()));
				return;
			}

			for ( int i = 0; i < root.getChildCount(); ++i) {
				DefaultMutableTreeNode child = ( DefaultMutableTreeNode)root.getChildAt( i);
				if ( select( child, directory, files))
					return;
			}
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
//		List list = new ArrayList();
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
//		words = ( String[])list.toArray( new String[ 0]);
//
//		for ( int i = 0; i < root.getChildCount(); ++i) {
//			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)root.getChildAt( i);
//			String rootDirectory = ( String)child.getUserObject();
//			if ( set_current_directory( child, words))
//				break;
//		}
//	}
//
//	/**
//	 * @param parent
//	 * @param words
//	 * @return
//	 */
//	private boolean set_current_directory(DefaultMutableTreeNode parent, String[] words) {
//		boolean select = false;
//		for ( int i = 0; i < parent.getChildCount(); ++i) {
//			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)parent.getChildAt( i);
//			File directory = ( File)child.getUserObject();
//			DefaultMutableTreeNode defaultMutableTreeNode = getNode( directory, child);
//			for ( int j = 0; j < words.length; ++j) {
//				directory = new File( directory, words[ j]);
//				if ( !directory.exists() || !directory.isDirectory())
//					break;
//
//				defaultMutableTreeNode = getNode( directory, defaultMutableTreeNode);
//				if ( null == defaultMutableTreeNode)
//					break;
//
//				setSelectionPath( new TreePath( defaultMutableTreeNode.getPath()));
//				select = true;
//			}
//
//			if ( select)
//				break;
//		}
//		return select;
//	}

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
			expand( child, directory);
		}
	}

	/**
	 * @param parent
	 * @param directory
	 */
	private void expand(DefaultMutableTreeNode parent, File directory) {
		for ( int i = 0; i < parent.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)parent.getChildAt( i);
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

		Map<String, List<Node>> map = new HashMap<String, List<Node>>();

		for ( int i = 0; i < root.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)root.getChildAt( i);
			String rootDirectory = ( String)child.getUserObject();
			List<Node> nodes = new ArrayList<Node>();
			map.put( rootDirectory, nodes);
			get( child, nodes);
		}

		root = new DefaultMutableTreeNode( "");
		defaultTreeModel.setRoot( root);
		expandPath( new TreePath( root.getPath()));

		for ( int i = 0; i < _rootDirectories.length; ++i) {
			DefaultMutableTreeNode child = new DefaultMutableTreeNode( _rootDirectories[ i]);
			root.add( child);
			List<Node> nodes = map.get( _rootDirectories[ i]);
			make( child, nodes, defaultTreeModel);
		}

		expand( directory);
	}

	/**
	 * @param parent
	 * @param nodes
	 */
	private void get(DefaultMutableTreeNode parent, List<Node> nodes) {
		for ( int i = 0; i < parent.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)parent.getChildAt( i);
			Node node = new Node();
			if ( !node.create( child, this, _fileManagerCallBack))
				continue;

			nodes.add( node);
		}
	}

	/**
	 * @param parent
	 * @param nodes
	 * @param defaultTreeModel
	 */
	private void make(DefaultMutableTreeNode parent, List<Node> nodes, DefaultTreeModel defaultTreeModel) {
		for ( int i = 0; i < nodes.size(); ++i) {
			DefaultMutableTreeNode child = new DefaultMutableTreeNode( nodes.get( i)._directory);
			defaultTreeModel.insertNodeInto( child, parent, parent.getChildCount());
			nodes.get( i).make( child, defaultTreeModel, this);
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#on_rename(java.io.File, java.io.File, java.io.File)
	 */
	public void on_rename(File directory, File oldPath, File newPath) {
		// ディレクトリ名が変更された時
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();

		Map<String, List<Node>> map = new HashMap<String, List<Node>>();

		for ( int i = 0; i < root.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)root.getChildAt( i);
			String rootDirectory = ( String)child.getUserObject();
			List<Node> nodes = new ArrayList<Node>();
			map.put( rootDirectory, nodes);
			get( oldPath, newPath, child, nodes);
		}

		root = new DefaultMutableTreeNode( "");
		defaultTreeModel.setRoot( root);
		expandPath( new TreePath( root.getPath()));

		for ( int i = 0; i < _rootDirectories.length; ++i) {
			DefaultMutableTreeNode child = new DefaultMutableTreeNode( _rootDirectories[ i]);
			root.add( child);
			List<Node> nodes = map.get( _rootDirectories[ i]);
			make( child, nodes, defaultTreeModel);
		}

		expand( directory);
	}

	/**
	 * @param oldPath
	 * @param newPath
	 * @param parent
	 * @param nodes
	 */
	private void get(File oldPath, File newPath, DefaultMutableTreeNode parent, List<Node> nodes) {
		for ( int i = 0; i < parent.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)parent.getChildAt( i);
			Node node = new Node();
			if ( !node.create( oldPath, newPath, child, this, _fileManagerCallBack))
				return;

			nodes.add( node);
		}
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
			on_new_directory( child, directory);
		}
	}

	/**
	 * @param parent
	 * @param directory
	 */
	private void on_new_directory(DefaultMutableTreeNode parent, File directory) {
		for ( int i = 0; i < parent.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)parent.getChildAt( i);
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
		else if ( object instanceof String) {
			String rootDirectory = ( String)object;
			for ( int i = 0; i < _rootDirectories.length; ++i) {
				if ( !rootDirectory.equals( _rootDirectories[ i]))
					continue;

				append( parent, new File( rootDirectory), treePath);
				return true;
			}
		}
		expandPath( treePath);
		return true;
	}

	/**
	 * @param parent
	 * @param rootDirectory
	 * @return
	 */
	private boolean append(DefaultMutableTreeNode parent, File rootDirectory, TreePath treePath) {
		Map<Module, File> map = new HashMap<Module, File>();
		if ( !get( parent, map, rootDirectory, false))
			return false;

		Module[] modules = map.keySet().toArray( new Module[ 0]);
		Arrays.sort( modules, new ModuleNameComparator( true, false));

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();

		for ( int i = 0; i < modules.length; ++i)
			defaultTreeModel.insertNodeInto( new DefaultMutableTreeNode( map.get( modules[ i])), parent, parent.getChildCount());

		expandPath( treePath);

		return _fileTableBase.update( ( File)null);
	}

	/**
	 * @param parent
	 * @param map
	 * @param directory
	 * @param ignore
	 * @return
	 */
	private boolean get(DefaultMutableTreeNode parent, Map<Module, File> map, File directory, boolean ignore) {
		if ( !_fileManagerCallBack.visible( directory))
			return true;

		Module module = get_module( directory);
		if ( null != module) {
			if ( ignore) {
				map.put( module, directory);
				return true;
			} else {
				if ( !exist( parent, directory)) {
					map.put( module, directory);
					return true;
				}
			}
		}

		File[] files = directory.listFiles();
		if ( null == files)
			return false;

		for ( int i = 0; i < files.length; ++i) {
			if ( !files[ i].isDirectory())
				continue;

			if ( !get( parent, map, files[ i], ignore))
				return false;
		}

		return true;
	}

	/**
	 * 
	 */
	public void update() {
		_internalTabbedPane.refresh( _flag);

		TreePath treePath = getSelectionPath();
		if ( null == treePath) {
			_propertEditorPane.setText( "");
			_previousText = "";
			return;
		}

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		Object object = defaultMutableTreeNode.getUserObject();
		if ( object instanceof String) {
			_propertEditorPane.setText( "");
			_previousText = "";
			return;
		}

		File directory = ( File)object;
		while ( true) {
			DefaultMutableTreeNode parent = ( DefaultMutableTreeNode)defaultMutableTreeNode.getParent();
			if ( parent.getUserObject() instanceof String)
				break;

			defaultMutableTreeNode = parent;
			directory = ( File)defaultMutableTreeNode.getUserObject();
		}

		Module module = get_module( directory);
		if ( null == module) {
			_propertEditorPane.setText( "");
			_previousText = "";
			return;
		}

		String newText = module.get_html();
		if ( newText.equals( _previousText))
			return;

		_propertEditorPane.setText( newText);
		_propertEditorPane.setCaretPosition( 0);
		_previousText = newText;
	}

	/**
	 * @param directory
	 * @return
	 */
	public Module get_module(File directory) {
		File[] files = directory.listFiles();
		if ( null == files)
			return null;

		for ( int i = 0; i < files.length; ++i) {
			if ( !files[ i].isFile() || !files[ i].getName().equals( Constant._moduleSpringFilename))
				continue;

			Module module = Module.get_module( files[ i]);
			if ( null == module || !module.isEnabled())
				continue;

			return module;
		}

		return null;
	}

	/**
	 * 
	 */
	public void refresh() {
		Object object = get_selected_object();
		File[] files = _fileTableBase.get_selected_files();

		Map<String, NodeData> map = new HashMap<String, NodeData>();

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();
		for ( int i = 0; i < root.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)root.getChildAt( i);
			String rootDirectory = ( String)child.getUserObject();

			NodeData nodeData = new NodeData();

			// Tree情報リスト(=nodeData._nodes)を生成
			//   実在するディレクトリを保持しているnodeのみが含まれている
			//   実在しないディレクトリを保持しているnodeはふくまれていない
			get( child, nodeData._nodes);

			// 実ディレクトリ情報マップ(=nodeData._directories)を生成
			//   実在するディレクトリのみ含まれている
			//   こちらには含まれていてTree情報リストに含まれていないものが存在しうることに注意！
			if ( !get( child, nodeData._directories, new File( rootDirectory), true))
				continue;

			map.put( rootDirectory, nodeData);
		}

		// rootの作り直し
		root = new DefaultMutableTreeNode( "");
		defaultTreeModel.setRoot( root);

		for ( int i = 0; i < _rootDirectories.length; ++i) {
			DefaultMutableTreeNode child = new DefaultMutableTreeNode( _rootDirectories[ i]);
			root.add( child);
		}

		expandPath( new TreePath( root.getPath()));

		// 各Tree情報が実ディレクトリ情報を参照しながらTreeを再構築する
		for ( int i = 0; i < root.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)root.getChildAt( i);
			NodeData nodeData = map.get( ( String)child.getUserObject());
			if ( nodeData._nodes.isEmpty())
				continue;

			make( child, nodeData, defaultTreeModel);
			expandPath( new TreePath( child.getPath()));
		}

		select( object, files);
	}

	/**
	 * @param parent
	 * @param nodeData
	 * @param defaultTreeModel
	 */
	private void make(DefaultMutableTreeNode parent, NodeData nodeData, DefaultTreeModel defaultTreeModel) {
		// 実ディレクトリ情報マップのkeyをModuleNameComparatorでソート
		Module[] modules = nodeData._directories.keySet().toArray( new Module[ 0]);
		Arrays.sort( modules, new ModuleNameComparator( true, false));

		for ( int i = 0; i < modules.length; ++i) {
			Node node = get( modules[ i], nodeData);
			if ( null == node) {
				// 実ディレクトリ情報マップには含まれていてTree情報リストには含まれていないnodeは新たに追加
				DefaultMutableTreeNode child = new DefaultMutableTreeNode( nodeData._directories.get( modules[ i]));
				defaultTreeModel.insertNodeInto( child, parent, parent.getChildCount());
			} else {
				DefaultMutableTreeNode child = new DefaultMutableTreeNode( node._directory);
				defaultTreeModel.insertNodeInto( child, parent, parent.getChildCount());
				// nodeを再構築
				node.make( child, defaultTreeModel, this);
			}
		}
	}

	/**
	 * @param module
	 * @param nodeData
	 * @return
	 */
	private Node get(Module module, NodeData nodeData) {
		for ( int i = 0; i < nodeData._nodes.size(); ++i) {
			if ( nodeData._nodes.get( i)._directory.equals( nodeData._directories.get( module)))
				return nodeData._nodes.get( i);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#get_files(java.io.File[])
	 */
	protected File[] get_files(File[] files) {
		// TODO Auto-generated method stub
		if ( null == files || 0 == files.length)
			return files;

		List<File> list = new ArrayList<File>( Arrays.asList( files));
		for ( File file:files) {
			if ( file.isFile() && file.getName().toLowerCase().endsWith( ".jar")) {
				File annotationFile = new File( file.getParent(), file.getName().substring( 0, file.getName().length() - ".jar".length()) + Constant._moduleAnnotationFileExtension);
				if ( file.exists() && file.isFile() && !list.contains( annotationFile))
					list.add( annotationFile);
			}
		}
		return list.toArray( new File[ 0]);
	}

//	/* (non-Javadoc)
//	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#dragOver(java.awt.dnd.DropTargetDragEvent)
//	 */
//	public void dragOver(DropTargetDragEvent arg0) {
//		Transferable transferable = arg0.getTransferable();
//    DataFlavor[] dataFlavors = arg0.getCurrentDataFlavors();
//		if ( !transferable.isDataFlavorSupported( DataFlavor.javaFileListFlavor)
//			&& !transferable.isDataFlavorSupported( URISelection._uriFlavor)
//			&& !dataFlavors[ 0].getHumanPresentableName().equals( FileItemTransferable._name)) {
//			arg0.rejectDrag();
//			return;
//		}
//
//		Point position = getMousePosition();
//		if ( null == position) {
//			arg0.rejectDrag();
//			return;
//		}
//
//		TreePath treePath = getPathForLocation( position.x, position.y);
//		if ( null == treePath) {
//			arg0.rejectDrag();
//			return;
//		}
//
//		DefaultMutableTreeNode targetDefaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
//		Object object = targetDefaultMutableTreeNode.getUserObject();
//		if ( !( object instanceof File)) {
//			_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
//			repaint();
//			arg0.rejectDrag();
//			return;
//		}
//
//		if ( dataFlavors[ 0].getHumanPresentableName().equals( FileItemTransferable._name)) {
//			try {
//				File[] files = ( File[])arg0.getTransferable().getTransferData( FileItemTransferable._localObjectFlavor);
//				if ( null == files) {
//					_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
//					repaint();
//					arg0.rejectDrag();
//					return;
//				}
//
//				// self
//				File file = ( File)object;
//				for ( int i = 0; i < files.length; ++i) {
//					if ( files[ i].equals( file)) {
//						_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
//						repaint();
//						arg0.rejectDrag();
//						return;
//					}
//				}
//
//				// same place
//				for ( int i = 0; i < files.length; ++i) {
//					if ( files[ i].getParentFile().equals( file)) {
//						_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
//						repaint();
//						arg0.rejectDrag();
//						return;
//					}
//				}
//
//				// parent -> child
//				for ( int i = 0; i < files.length; ++i) {
//					DefaultMutableTreeNode parentDefaultMutableTreeNode = ( DefaultMutableTreeNode)targetDefaultMutableTreeNode.getParent();
//					while ( null != parentDefaultMutableTreeNode) {
//						object = parentDefaultMutableTreeNode.getUserObject();
//						if ( object instanceof File) {
//							file = ( File)object;
//							if ( files[ i].equals( file)) {
//								_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
//								repaint();
//								arg0.rejectDrag();
//								return;
//							}
//						}
//						parentDefaultMutableTreeNode = ( DefaultMutableTreeNode)parentDefaultMutableTreeNode.getParent();
//					}
//				}
//
//				arg0.acceptDrag( arg0.getDropAction());
//
//			} catch (UnsupportedFlavorException e) {
//				e.printStackTrace();
//				_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
//				repaint();
//				arg0.rejectDrag();
//				return;
//			} catch (IOException e) {
//				e.printStackTrace();
//				_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
//				repaint();
//				arg0.rejectDrag();
//				return;
//			}
//			TreeNode targetTreeNode = ( TreeNode)treePath.getLastPathComponent();
//
//		} else {
//			arg0.acceptDrag( DnDConstants.ACTION_COPY);
//		}
//
//		_dropTargetTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
//		repaint();
//	}
//
//	/* (non-Javadoc)
//	 * @see soars.common.utility.swing.file.manager.tree.DirectoryTreeBase#drop(java.awt.dnd.DropTargetDropEvent)
//	 */
//	public void drop(DropTargetDropEvent arg0) {
//		if ( null == _dropTargetTreeNode) {
//			arg0.rejectDrop();
//			repaint();
//			return;
//		}
//
//		try {
//			Transferable transferable = arg0.getTransferable();
//			if ( transferable.isDataFlavorSupported( DataFlavor.javaFileListFlavor)) {
//				arg0.acceptDrop( arg0.getDropAction());
//				List list = ( List)transferable.getTransferData( DataFlavor.javaFileListFlavor);
//				if ( null == list || list.isEmpty()) {
//					arg0.getDropTargetContext().dropComplete( true);
//					_dropTargetTreeNode = null;
//					return;
//				}
//
//				File[] files =( File[])list.toArray( new File[ 0]);
//				arg0.getDropTargetContext().dropComplete( true);
//				on_paste( files, DnDConstants.ACTION_COPY);
//			} else if ( transferable.isDataFlavorSupported( URISelection._uriFlavor)) {
//				arg0.acceptDrop( arg0.getDropAction());
//				String string = ( String)transferable.getTransferData( URISelection._uriFlavor);
//				arg0.getDropTargetContext().dropComplete( true);
//				if ( null == string) {
//					_dropTargetTreeNode = null;
//					return;
//				}
//
//				StringTokenizer stringTokenizer = new StringTokenizer( string, "\r\n");
//				List list = new ArrayList();
//				while( stringTokenizer.hasMoreElements()) {
//					URI uri = new URI( ( String)stringTokenizer.nextElement());
//					if ( uri.getScheme().equals( "file"))
//						list.add( new File( uri.getPath()));
//				}
//				File[] files =( File[])list.toArray( new File[ 0]);
//				on_paste( files, DnDConstants.ACTION_COPY);
//			} else {
//				if ( DnDConstants.ACTION_COPY != arg0.getDropAction()
//					&& DnDConstants.ACTION_MOVE != arg0.getDropAction()) {
//					arg0.getDropTargetContext().dropComplete( true);
//					_dropTargetTreeNode = null;
//					return;
//				}
//
//				DataFlavor[] dataFlavors = arg0.getCurrentDataFlavors();
//				if ( dataFlavors[ 0].getHumanPresentableName().equals( FileItemTransferable._name)) {
//					arg0.acceptDrop( arg0.getDropAction());
//					File[] files = ( File[])arg0.getTransferable().getTransferData( FileItemTransferable._localObjectFlavor);
//					if ( null == files) {
//						arg0.getDropTargetContext().dropComplete( true);
//						_dropTargetTreeNode = null;
//						return;
//					}
//					arg0.getDropTargetContext().dropComplete( true);
//					if ( DnDConstants.ACTION_COPY == arg0.getDropAction())
//						on_paste( files, arg0.getDropAction());
//					else if ( DnDConstants.ACTION_MOVE == arg0.getDropAction()) {
//						int[] rows = _fileTableBase.getSelectedRows();
//						_fileManagerCallBack.on_start_paste_and_remove();
//						paste_and_remove( files, arg0.getDropAction(), false);
//						_fileTableBase.optimize_selection( rows);
//						_fileManagerCallBack.on_paste_and_remove_completed();
//					} else {
//						arg0.rejectDrop();
//						repaint();
//						return;
//					}
//				} else {
//					arg0.getDropTargetContext().dropComplete( true);
//					arg0.rejectDrop();
//				}
//			}
//		} catch (IOException ioe) {
//			arg0.rejectDrop();
//		} catch (UnsupportedFlavorException ufe) {
//			arg0.rejectDrop();
//		} catch (InvalidDnDOperationException idoe) {
//			arg0.rejectDrop();
//		} catch (URISyntaxException e) {
//			arg0.rejectDrop();
//		}
//
//		_dropTargetTreeNode = null;
//		repaint();
//	}
}
