/**
 * 
 */
package soars.application.manager.library.main.tab.directory.tree;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JMenuItem;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import soars.application.manager.library.main.Constant;
import soars.application.manager.library.main.ResourceManager;
import soars.application.manager.library.main.tab.common.menu.DisableModuleAction;
import soars.application.manager.library.main.tab.common.menu.EditModuleAction;
import soars.application.manager.library.main.tab.common.menu.EnableModuleAction;
import soars.application.manager.library.main.tab.common.menu.IContextMenuHandler;
import soars.application.manager.library.main.tab.tab.InternalTabbedPane;
import soars.application.manager.library.module.EditModuleDlg;
import soars.common.soars.module.Module;
import soars.common.utility.swing.file.manager.IFileManager;
import soars.common.utility.swing.file.manager.IFileManagerCallBack;
import soars.common.utility.swing.file.manager.table.FileTableBase;
import soars.common.utility.swing.file.manager.tree.DirectoryTree2;
import soars.common.utility.swing.file.manager.tree.DirectoryTreeCellRendererBase;

/**
 * @author kurata
 *
 */
public class NavigatorDirectoryTree extends DirectoryTree2 implements IContextMenuHandler {

	/**
	 * 
	 */
	protected JMenuItem _editModuleMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _enableModuleMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _disableModuleMenuItem = null;

	/**
	 * 
	 */
	private InternalTabbedPane _internalTabbedPane = null;

	/**
	 * 
	 */
	private JEditorPane _propertyEditorPane = null;

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
	 * @param owner
	 * @param parent
	 */
	public NavigatorDirectoryTree(File[] rootDirectories, IFileManagerCallBack fileManagerCallBack, IFileManager fileManager, Component component, Frame owner, Component parent) {
		super(rootDirectories, fileManagerCallBack, fileManager, component, owner, parent);
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
		if (!super.setup(popupMenu, directoryTreeCellRendererBase, fileTableBase))
			return false;

		_internalTabbedPane = internalTabbedPane;
		_propertyEditorPane = propertyEditorPane;

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

		_popupMenu.addSeparator();

		_enableModuleMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "popup.menu.enable.module.menu"),
			new EnableModuleAction( ResourceManager.get_instance().get( "popup.menu.enable.module.menu"), this),
			ResourceManager.get_instance().get( "popup.menu.enable.module.mnemonic"),
			ResourceManager.get_instance().get( "popup.menu.enable.module.stroke"));
		_disableModuleMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "popup.menu.disable.module.menu"),
			new DisableModuleAction( ResourceManager.get_instance().get( "popup.menu.disable.module.menu"), this),
			ResourceManager.get_instance().get( "popup.menu.disable.module.mnemonic"),
			ResourceManager.get_instance().get( "popup.menu.disable.module.stroke"));
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

		DefaultMutableTreeNode parent = ( DefaultMutableTreeNode)defaultMutableTreeNode.getParent();

		_editModuleMenuItem.setEnabled(
			!defaultMutableTreeNode.isRoot()
			&& !parent.isRoot()
			&& defaultMutableTreeNode.getUserObject() instanceof File
			&& ( null != get_module( ( File)defaultMutableTreeNode.getUserObject(), false)));
		_enableModuleMenuItem.setEnabled(
			!defaultMutableTreeNode.isRoot()
			&& !parent.isRoot()
			&& defaultMutableTreeNode.getUserObject() instanceof File
			&& ( null == get_module( ( File)defaultMutableTreeNode.getUserObject(), true)));
//			&& !is_module( ( File)defaultMutableTreeNode.getUserObject()));
//			&& ( null == Module.get_module( ( File)defaultMutableTreeNode.getUserObject())));
		_disableModuleMenuItem.setEnabled(
			!defaultMutableTreeNode.isRoot()
			&& !parent.isRoot()
			&& defaultMutableTreeNode.getUserObject() instanceof File
			&& ( null != get_module( ( File)defaultMutableTreeNode.getUserObject(), false)));
//			&& is_module( ( File)defaultMutableTreeNode.getUserObject()));
//			&& ( null != Module.get_module( ( File)defaultMutableTreeNode.getUserObject())));

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
		Module module = get_module( directory, false);
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
		TreePath treePath = getSelectionPath();
		if ( null == treePath)
			return;

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		Object object = defaultMutableTreeNode.getUserObject();
		if ( object instanceof String)
			return;

		File directory = ( File)object;
		Module module = get_module( directory);
		if ( null == module) {
			module = new Module();
			module.setFilePath( new File( directory, Constant._moduleSpringFilename));
		}

		EditModuleDlg editModuleDlg = new EditModuleDlg( _owner, ResourceManager.get_instance().get( "edit.module.dialog.title"), true, module);
		if ( !editModuleDlg.do_modal())
			return;

		File[] files = directory.listFiles();
		for ( int i = 0; i < files.length; ++i) {
			if ( !files[ i].isDirectory())
				continue;

			disable_all( files[ i]);
		}

		repaint();
	}

	/**
	 * @param directory
	 * @return
	 */
	private Module get_module(File directory) {
		if ( !_fileManagerCallBack.visible( directory))
			return null;

		File[] files = directory.listFiles();
		for ( int i = 0; i < files.length; ++i) {
			if ( !files[ i].isFile() || !files[ i].getName().equals( Constant._moduleSpringFilename))
				continue;

			Module module = Module.get_module( files[ i]);
			if ( null == module)
				continue;

			return module;
		}

		return null;
	}

	/**
	 * @param directory
	 */
	private void disable_all(File directory) {
		if ( !_fileManagerCallBack.visible( directory))
			return;

		File[] files = directory.listFiles();
		for ( int i = 0; i < files.length; ++i) {
			if ( files[ i].isDirectory()) {
				disable_all( files[ i]);
				continue;
			}

			if ( !files[ i].isFile() || !files[ i].getName().equals( Constant._moduleSpringFilename))
				continue;

			Module module = Module.get_module( files[ i]);
			if ( null == module)
				continue;

			module.setEnable( "false");
			module.write();
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.modulemanager.main.tab.common.menu.IContextMenuHandler#on_disable_module(java.awt.event.ActionEvent)
	 */
	public void on_disable_module(ActionEvent actionEvent) {
		TreePath treePath = getSelectionPath();
		if ( null == treePath)
			return;

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		Object object = defaultMutableTreeNode.getUserObject();
		if ( object instanceof String)
			return;

		File directory = ( File)object;
		Module module = get_module( directory, false);
		if ( null == module)
			return;

		module.setEnable( "false");
		module.write();

		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.library.main.tab.common.menu.IContextMenuHandler#on_update_annotation_file(java.awt.event.ActionEvent)
	 */
	public void on_update_annotation_file(ActionEvent actionEvent) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	public void update() {
		_internalTabbedPane.refresh( _flag);

		TreePath treePath = getSelectionPath();
		if ( null == treePath) {
			_propertyEditorPane.setText( "");
			_previousText = "";
			return;
		}

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		if ( defaultMutableTreeNode.isRoot()) {
			_propertyEditorPane.setText( "");
			_previousText = "";
			return;
		}

		File directory = ( File)defaultMutableTreeNode.getUserObject();

		if ( is_root_directory( directory)) {
			_propertyEditorPane.setText( "");
			_previousText = "";
			return;
		}

		Module module = get_module( directory, true);
		if ( null == module) {
			_propertyEditorPane.setText( "");
			_previousText = "";
			return;
		}

		String newText = module.get_html();
		if ( newText.equals( _previousText))
			return;

		_propertyEditorPane.setText( newText);
		_propertyEditorPane.setCaretPosition( 0);
		_previousText = newText;
	}

	/**
	 * @param directory
	 * @param trace
	 * @return
	 */
	public Module get_module(File directory, boolean trace) {
		if ( !_fileManagerCallBack.visible( directory))
			return null;

		File[] files = directory.listFiles();
		for ( int i = 0; i < files.length; ++i) {
			if ( !files[ i].isFile() || !files[ i].getName().equals( Constant._moduleSpringFilename))
				continue;

			Module module = Module.get_module( files[ i]);
			if ( null == module || !module.isEnabled())
				continue;

			return module;
		}

		if ( !trace)
			return null;

		File parent = directory.getParentFile();
		if ( is_root_directory( parent))
			return null;

		return get_module( parent, trace);
	}

	/**
	 * @param directory
	 * @return
	 */
	public boolean is_root_directory(File directory) {
		for ( int i = 0; i < _rootDirectories.length; ++i) {
			if ( directory.equals( _rootDirectories[ i]))
				return true;
		}
		return false;
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
}
