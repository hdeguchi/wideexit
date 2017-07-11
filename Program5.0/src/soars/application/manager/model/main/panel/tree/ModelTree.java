/**
 * 
 */
package soars.application.manager.model.main.panel.tree;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import soars.application.manager.model.common.tool.CommonTool;
import soars.application.manager.model.executor.ApplicationBuilder;
import soars.application.manager.model.executor.Executor;
import soars.application.manager.model.executor.SimulationRunner;
import soars.application.manager.model.executor.VisualShell;
import soars.application.manager.model.main.Constant;
import soars.application.manager.model.main.Environment;
import soars.application.manager.model.main.MainFrame;
import soars.application.manager.model.main.ResourceManager;
import soars.application.manager.model.main.panel.image.ImagePanel;
import soars.application.manager.model.main.panel.tab.contents.SoarsContentsPage;
import soars.application.manager.model.main.panel.tab.doc.SoarsDocEditorPane;
import soars.application.manager.model.main.panel.tab.doc.edit.ModelInformationDlg;
import soars.application.manager.model.main.panel.tree.property.NodeProperty;
import soars.application.manager.model.main.panel.tree.property.TableSelection;
import soars.application.manager.model.menu.edit.CopyAction;
import soars.application.manager.model.menu.edit.DuplicateAction;
import soars.application.manager.model.menu.edit.ExportAction;
import soars.application.manager.model.menu.edit.ExportUserDefinedRuleAction;
import soars.application.manager.model.menu.edit.IEditMenuHandler;
import soars.application.manager.model.menu.edit.ImportUserDefinedRuleAction;
import soars.application.manager.model.menu.edit.ModelInformationAction;
import soars.application.manager.model.menu.edit.NewDirectoryAction;
import soars.application.manager.model.menu.edit.NewSimulationModelAction;
import soars.application.manager.model.menu.edit.PasteAction;
import soars.application.manager.model.menu.edit.RemoveAction;
import soars.application.manager.model.menu.edit.RemoveUserDefinedRuleAction;
import soars.application.manager.model.menu.edit.RenameAction;
import soars.application.manager.model.menu.edit.UpdateUserDefinedRuleAction;
import soars.application.manager.model.menu.run.IRunMenuHandler;
import soars.application.manager.model.menu.run.StartApplicationBuilderAction;
import soars.application.manager.model.menu.run.StartSimulatorAction;
import soars.application.manager.model.menu.run.StartVisualShellAction;
import soars.common.soars.environment.BasicEnvironment;
import soars.common.soars.property.UserDefinedRule;
import soars.common.soars.tool.Converter;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.file.manager.IFileManager;
import soars.common.utility.swing.file.manager.IFileManagerCallBack;
import soars.common.utility.swing.file.manager.common.Message;
import soars.common.utility.swing.file.manager.common.Utility;
import soars.common.utility.swing.file.manager.edit.EditNameDlg;
import soars.common.utility.swing.file.manager.tree.DirectoryNodeTransferable;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.swing.progress.IIntProgressCallback;
import soars.common.utility.swing.progress.IntProgressDlg;
import soars.common.utility.swing.tree.StandardTree;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.clipboard.URISelection;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.file.ZipUtility;

/**
 * @author kurata
 *
 */
public class ModelTree extends StandardTree implements DragGestureListener, DragSourceListener, DropTargetListener, IIntProgressCallback, IFileManager, IFileManagerCallBack, IEditMenuHandler, IRunMenuHandler {

	/**
	 * 
	 */
	private List<File> _rootDirectories = new ArrayList<File>();

	/**
	 * 
	 */
	private Map<File, String> _rootDirectoryNameMap = new HashMap<File, String>();

	/**
	 * 
	 */
	protected Map<File, NodeProperty> _nodePropertyMap = new HashMap<File, NodeProperty>();

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
	private ImagePanel _imagePanel = null;

	/**
	 * 
	 */
	private SoarsDocEditorPane _soarsDocEditorPane = null;

	/**
	 * 
	 */
	private SoarsContentsPage _soarsContentsPage = null;

	/**
	 * 
	 */
	private JMenuItem _editCopyMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editPasteMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editDuplicateMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editExportMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editRemoveMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editRenameMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editNewDirectoryMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editNewSimulationModelMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editModelInformationMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editImportUserDefinedRuleMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editUpdateUserDefinedRuleMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editExportUserDefinedRuleMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editRemoveUserDefinedRuleMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _runStartVisualShellMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _runStartSimulatorMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _runStartApplicationBuilderMenuItem = null;

	/**
	 * 
	 */
	private Map<String, JButton> _buttonMap = null;

	/**
	 * 
	 */
	public Component _mainPanel = null;

	/**
	 * @param buttonMap
	 * @param mainPanel
	 * @param owner
	 * @param parent
	 */
	public ModelTree(Map<String, JButton> buttonMap, Component mainPanel, Frame owner, Component parent) {
		super(owner, parent);
		setup_rootDirectories();
		_buttonMap = buttonMap;
		_mainPanel = mainPanel;
	}

	/**
	 * 
	 */
	private void setup_rootDirectories() {
		File directory = new File( Constant._systemModelDirectory);
		_rootDirectories.add( directory);
		_rootDirectoryNameMap.put( directory, ResourceManager.get_instance().get( "model.tree.system.root"));
	}

	/**
	 * @return
	 */
	public File get_selected_file() {
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
	 * @param file
	 */
	public void select(File file) {
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();

		if ( null == file || !file.exists()) {
			setSelectionPath( new TreePath( ( ( DefaultMutableTreeNode)root.getChildAt( 0)).getPath()));
			return;
		}

		if ( select( root, file))
			return;

		setSelectionPath( new TreePath( ( ( DefaultMutableTreeNode)root.getChildAt( 0)).getPath()));
	}

	/**
	 * @param parent
	 * @param file
	 * @return
	 */
	private boolean select(DefaultMutableTreeNode parent, File file) {
		for ( int i = 0; i < parent.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)parent.getChildAt( i);
			File target = ( File)child.getUserObject();
			if ( file.equals( target)) {
				setSelectionPath( new TreePath( child.getPath()));
				return true;
			}

			if ( target.isDirectory() && select( child, file))
				return true;
		}
		return false;
	}

	/**
	 * @param nodeProperty
	 * @return
	 */
	public File get_datafile(NodeProperty nodeProperty) {
		Iterator iterator = _nodePropertyMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			NodeProperty value = ( NodeProperty)entry.getValue();
			if ( nodeProperty.equals( value))
				return ( File)entry.getKey();
		}
		return null;
	}

	/**
	 * 
	 */
	public void update() {
		File newProjectFolder = new File( BasicEnvironment.get_instance().get( BasicEnvironment._projectFolderKey + "0", "") + "/" + Constant._modelDirectoryName);

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();

		DefaultMutableTreeNode child = get_project_folder_node( root);
		if ( null != child) {
			File folder = ( File)child.getUserObject();
			if ( folder.equals( newProjectFolder))
				return;

			_rootDirectoryNameMap.remove( folder);
			_rootDirectories.remove( folder);
			root.remove( child);
			//System.out.println( _rootDirectoryNameMap.size() + ", " + _rootDirectories.size());
		}

		//System.out.println( _rootDirectoryNameMap.size() + ", " + _rootDirectories.size());

		child = new DefaultMutableTreeNode( newProjectFolder);
		defaultTreeModel.insertNodeInto( child, root, root.getChildCount());
		setSelectionPath( new TreePath( child.getPath()));
		_rootDirectories.add( newProjectFolder);
		_rootDirectoryNameMap.put( newProjectFolder, ResourceManager.get_instance().get( "model.tree.user.root"));
		refresh();

		//System.out.println( _rootDirectoryNameMap.size() + ", " + _rootDirectories.size());
	}

	/**
	 * @param root
	 * @return
	 */
	private DefaultMutableTreeNode get_project_folder_node(DefaultMutableTreeNode root) {
		for ( int i = 0; i < root.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)root.getChildAt( i);
			if ( null == child)
				continue;

			File folder = ( File)child.getUserObject();
			if ( null == folder)
				continue;

			String name = _rootDirectoryNameMap.get( folder);
			if ( null == name)
				continue;

			if ( name.equals( ResourceManager.get_instance().get( "model.tree.user.root")))
				return child;
		}
		return null;
	}

	/**
	 * @param imagePanel
	 * @param soarsDocEditorPane
	 * @param soarsContentsPage 
	 * @return
	 */
	public boolean setup(ImagePanel imagePanel, SoarsDocEditorPane soarsDocEditorPane, SoarsContentsPage soarsContentsPage) {
		if ( !super.setup( true))
			return false;

		_imagePanel = imagePanel;
		_soarsDocEditorPane = soarsDocEditorPane;
		_soarsContentsPage = soarsContentsPage;


		getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION);


		Action enterAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_enter();
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0), "enter");
		getActionMap().put( "enter", enterAction);


		Action deleteAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_edit_remove( e);
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0), "delete");
		getActionMap().put( "delete", deleteAction);


		Action backSpaceAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_edit_remove( e);
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, 0), "backspace");
		getActionMap().put( "backspace", backSpaceAction);


		Action copyAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_edit_copy( e);
			}
		};
		//getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "copy");
		getActionMap().put( "copy", copyAction);


		Action pasteAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_edit_paste( e);
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


		addTreeSelectionListener( new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				on_valueChanged( e);
			}
		});


		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode( "");
		defaultTreeModel.setRoot( root);

		for ( File rootDirectory:_rootDirectories) {
			DefaultMutableTreeNode child = new DefaultMutableTreeNode( rootDirectory);
			root.add( child);
		}

		expandPath( new TreePath( root.getPath()));

		setRootVisible( false);


		setCellRenderer( new ModelTreeCellRenderer( _nodePropertyMap, _rootDirectoryNameMap));


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
		Object object = ( Object)defaultMutableTreeNode.getUserObject();
		if ( null == object)
			return;

		if ( !( object instanceof File))
			return;

		File file = ( File)object;
		if ( !file.exists())
			return;

		if ( file.isDirectory())
			expand( defaultMutableTreeNode, treePath, null);
		else if ( file.isFile()) {
			MainFrame.get_instance().store();
			VisualShell.start( file);
		}
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
		TreePath oldTreePath = treeSelectionEvent.getOldLeadSelectionPath();
		if ( null != oldTreePath) {
			DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)oldTreePath.getLastPathComponent();
			File file = ( File)defaultMutableTreeNode.getUserObject();
			if ( file.isFile()) {
				NodeProperty nodeProperty = _nodePropertyMap.get( file);
				if ( null != nodeProperty) {
					_soarsContentsPage.get( nodeProperty._tableSelection);
					//System.out.println( file.getName() + ", " + nodeProperty._title + ", " + nodeProperty._directory.getName());
				}
			}
		}

//		TreePath treePath = treeSelectionEvent.getPath();
		TreePath treePath = treeSelectionEvent.getNewLeadSelectionPath();
		if ( null == treePath) {
			_imagePanel.set_empty_image();
			MainFrame.get_instance().update_title( "", "");
			//_filenameTextField.setText( "");
			_soarsDocEditorPane.set_empty_page();
			_buttonMap.get( ResourceManager.get_instance().get( "edit.copy.menu")).setEnabled( false);
			_buttonMap.get( ResourceManager.get_instance().get( "edit.paste.menu")).setEnabled( false);
			_buttonMap.get( ResourceManager.get_instance().get( "edit.remove.menu")).setEnabled( false);
			_buttonMap.get( ResourceManager.get_instance().get( "run.start.visual.shell.menu")).setEnabled( false);
			_buttonMap.get( ResourceManager.get_instance().get( "run.start.simulator.menu")).setEnabled( false);
			_soarsContentsPage.update( null, null);
			_soarsContentsPage.refresh();
			return;
		}

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();

		DefaultMutableTreeNode parent = ( DefaultMutableTreeNode)defaultMutableTreeNode.getParent();

		File file = ( File)defaultMutableTreeNode.getUserObject();
		if ( !file.isFile()) {
			_imagePanel.set_empty_image();
			MainFrame.get_instance().update_title( "", "");
			//_filenameTextField.setText( "");
			_soarsDocEditorPane.set_empty_page();
			_buttonMap.get( ResourceManager.get_instance().get( "edit.copy.menu")).setEnabled( true);
			_buttonMap.get( ResourceManager.get_instance().get( "edit.paste.menu")).setEnabled( file.isDirectory() && exists_files_to_paste());
			_buttonMap.get( ResourceManager.get_instance().get( "edit.remove.menu")).setEnabled( !parent.isRoot());
			_buttonMap.get( ResourceManager.get_instance().get( "run.start.visual.shell.menu")).setEnabled( false);
			_buttonMap.get( ResourceManager.get_instance().get( "run.start.simulator.menu")).setEnabled( false);
			expand( defaultMutableTreeNode, treePath, null);
			_soarsContentsPage.update( null, null);
			_soarsContentsPage.refresh();
			return;
		}

		_buttonMap.get( ResourceManager.get_instance().get( "edit.copy.menu")).setEnabled( true);
		_buttonMap.get( ResourceManager.get_instance().get( "edit.paste.menu")).setEnabled( false);
		_buttonMap.get( ResourceManager.get_instance().get( "edit.remove.menu")).setEnabled( !parent.isRoot());
		_buttonMap.get( ResourceManager.get_instance().get( "run.start.visual.shell.menu")).setEnabled( true);
		_buttonMap.get( ResourceManager.get_instance().get( "run.start.simulator.menu")).setEnabled( true);

		NodeProperty nodeProperty = _nodePropertyMap.get( file);
		if ( null == nodeProperty) {
			MainFrame.get_instance().update_title( "", "");
			nodeProperty = new NodeProperty();
			if ( !nodeProperty.setup( file)) {
				_imagePanel.set_no_image();
				_soarsDocEditorPane.set_no_document_page();
				_soarsContentsPage.update( null, null);
				_soarsContentsPage.refresh();
				return;
			}

			_nodePropertyMap.put( file, nodeProperty);
		}


		MainFrame.get_instance().update_title( nodeProperty._title, file.getName());
		//_filenameTextField.setText( file.getName());

		_soarsDocEditorPane.update( nodeProperty._directory);
		_imagePanel.update( nodeProperty);
		_soarsContentsPage.update( file, nodeProperty._tableSelection);
		_soarsContentsPage.refresh();
	}

	/**
	 * @param menuItemMap 
	 */
	public void editMenuSelected(Map<String, JMenuItem> menuItemMap) {
		TreePath treePath = getSelectionPath();
		if ( null == treePath) {
			menuItemMap.get( ResourceManager.get_instance().get( "edit.copy.menu")).setEnabled( false);
			menuItemMap.get( ResourceManager.get_instance().get( "edit.paste.menu")).setEnabled( false);
			menuItemMap.get( ResourceManager.get_instance().get( "edit.duplicate.menu")).setEnabled( false);
			menuItemMap.get( ResourceManager.get_instance().get( "edit.export.menu")).setEnabled( false);
			menuItemMap.get( ResourceManager.get_instance().get( "edit.remove.menu")).setEnabled( false);
			menuItemMap.get( ResourceManager.get_instance().get( "edit.rename.menu")).setEnabled( false);
			menuItemMap.get( ResourceManager.get_instance().get( "edit.new.directory.menu")).setEnabled( false);
			menuItemMap.get( ResourceManager.get_instance().get( "edit.new.simulation.model.menu")).setEnabled( false);
			menuItemMap.get( ResourceManager.get_instance().get( "edit.model.information.menu")).setEnabled( false);
			menuItemMap.get( ResourceManager.get_instance().get( "edit.import.user.defined.rule.menu")).setEnabled( false);
			menuItemMap.get( ResourceManager.get_instance().get( "edit.update.user.defined.rule.menu")).setEnabled( false);
			menuItemMap.get( ResourceManager.get_instance().get( "edit.export.user.defined.rule.menu")).setEnabled( false);
			menuItemMap.get( ResourceManager.get_instance().get( "edit.remove.user.defined.rule.menu")).setEnabled( false);
			return;
		}

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();

		DefaultMutableTreeNode parent = ( DefaultMutableTreeNode)defaultMutableTreeNode.getParent();

		File file = ( File)defaultMutableTreeNode.getUserObject();

		menuItemMap.get( ResourceManager.get_instance().get( "edit.copy.menu")).setEnabled( true);
		menuItemMap.get( ResourceManager.get_instance().get( "edit.paste.menu")).setEnabled( file.isDirectory() && exists_files_to_paste());
		menuItemMap.get( ResourceManager.get_instance().get( "edit.duplicate.menu")).setEnabled( file.isFile());
		menuItemMap.get( ResourceManager.get_instance().get( "edit.export.menu")).setEnabled( true);
		menuItemMap.get( ResourceManager.get_instance().get( "edit.remove.menu")).setEnabled( !parent.isRoot());
		menuItemMap.get( ResourceManager.get_instance().get( "edit.rename.menu")).setEnabled( !parent.isRoot());
		menuItemMap.get( ResourceManager.get_instance().get( "edit.new.directory.menu")).setEnabled( file.isDirectory());
		menuItemMap.get( ResourceManager.get_instance().get( "edit.new.simulation.model.menu")).setEnabled( file.isDirectory());
		menuItemMap.get( ResourceManager.get_instance().get( "edit.model.information.menu")).setEnabled( file.isFile());
		menuItemMap.get( ResourceManager.get_instance().get( "edit.import.user.defined.rule.menu")).setEnabled( file.isFile() && !has_user_defined_rule( file));
		menuItemMap.get( ResourceManager.get_instance().get( "edit.update.user.defined.rule.menu")).setEnabled( file.isFile() && has_user_defined_rule( file));
		menuItemMap.get( ResourceManager.get_instance().get( "edit.export.user.defined.rule.menu")).setEnabled( file.isFile() && has_user_defined_rule( file));
		menuItemMap.get( ResourceManager.get_instance().get( "edit.remove.user.defined.rule.menu")).setEnabled( file.isFile() && has_user_defined_rule( file));
	}

	/**
	 * @param menuItemMap
	 */
	public void runMenuSelected(Map<String, JMenuItem> menuItemMap) {
		TreePath treePath = getSelectionPath();
		if ( null == treePath) {
			menuItemMap.get( ResourceManager.get_instance().get( "run.start.visual.shell.menu")).setEnabled( false);
			_buttonMap.get( ResourceManager.get_instance().get( "run.start.visual.shell.menu")).setEnabled( false);
			menuItemMap.get( ResourceManager.get_instance().get( "run.start.simulator.menu")).setEnabled( false);
			_buttonMap.get( ResourceManager.get_instance().get( "run.start.simulator.menu")).setEnabled( false);
			menuItemMap.get( ResourceManager.get_instance().get( "run.start.application.builder.menu")).setEnabled( false);
			return;
		}

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();

		File file = ( File)defaultMutableTreeNode.getUserObject();

		menuItemMap.get( ResourceManager.get_instance().get( "run.start.visual.shell.menu")).setEnabled( file.isFile());
		_buttonMap.get( ResourceManager.get_instance().get( "run.start.visual.shell.menu")).setEnabled( file.isFile());
		menuItemMap.get( ResourceManager.get_instance().get( "run.start.simulator.menu")).setEnabled( file.isFile());
		_buttonMap.get( ResourceManager.get_instance().get( "run.start.simulator.menu")).setEnabled( file.isFile());
		menuItemMap.get( ResourceManager.get_instance().get( "run.start.application.builder.menu")).setEnabled( file.isFile());
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.tree.StandardTree#setup_popup_menu()
	 */
	protected void setup_popup_menu() {
		super.setup_popup_menu();

		_editCopyMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.copy.menu"),
			new CopyAction( ResourceManager.get_instance().get( "edit.copy.menu"), this),
			ResourceManager.get_instance().get( "edit.copy.mnemonic"),
			ResourceManager.get_instance().get( "edit.copy.stroke"));
		_editPasteMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.paste.menu"),
			new PasteAction( ResourceManager.get_instance().get( "edit.paste.menu"), this),
			ResourceManager.get_instance().get( "edit.paste.mnemonic"),
			ResourceManager.get_instance().get( "edit.paste.stroke"));
		_editDuplicateMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.duplicate.menu"),
			new DuplicateAction( ResourceManager.get_instance().get( "edit.duplicate.menu"), this),
			ResourceManager.get_instance().get( "edit.duplicate.mnemonic"),
			ResourceManager.get_instance().get( "edit.duplicate.stroke"));
		_editExportMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.export.menu"),
			new ExportAction( ResourceManager.get_instance().get( "edit.export.menu"), this),
			ResourceManager.get_instance().get( "edit.export.mnemonic"),
			ResourceManager.get_instance().get( "edit.export.stroke"));

		_popupMenu.addSeparator();

		_editRemoveMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.remove.menu"),
			new RemoveAction( ResourceManager.get_instance().get( "edit.remove.menu"), this),
			ResourceManager.get_instance().get( "edit.remove.mnemonic"),
			ResourceManager.get_instance().get( "edit.remove.stroke"));

		_popupMenu.addSeparator();

		_editRenameMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.rename.menu"),
			new RenameAction( ResourceManager.get_instance().get( "edit.rename.menu"), this),
			ResourceManager.get_instance().get( "edit.rename.mnemonic"),
			ResourceManager.get_instance().get( "edit.rename.stroke"));

		_popupMenu.addSeparator();

		_editNewDirectoryMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.new.directory.menu"),
			new NewDirectoryAction( ResourceManager.get_instance().get( "edit.new.directory.menu"), this),
			ResourceManager.get_instance().get( "edit.new.directory.mnemonic"),
			ResourceManager.get_instance().get( "edit.new.directory.stroke"));

		_popupMenu.addSeparator();

		_editNewSimulationModelMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.new.simulation.model.menu"),
			new NewSimulationModelAction( ResourceManager.get_instance().get( "edit.new.simulation.model.menu"), this),
			ResourceManager.get_instance().get( "edit.new.simulation.model.mnemonic"),
			ResourceManager.get_instance().get( "edit.new.simulation.model.stroke"));

		_editModelInformationMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.model.information.menu"),
			new ModelInformationAction( ResourceManager.get_instance().get( "edit.model.information.menu"), this),
			ResourceManager.get_instance().get( "edit.model.information.mnemonic"),
			ResourceManager.get_instance().get( "edit.model.information.stroke"));

		_popupMenu.addSeparator();

		_editImportUserDefinedRuleMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.import.user.defined.rule.menu"),
			new ImportUserDefinedRuleAction( ResourceManager.get_instance().get( "edit.import.user.defined.rule.menu"), this),
			ResourceManager.get_instance().get( "edit.import.user.defined.rule.mnemonic"),
			ResourceManager.get_instance().get( "edit.import.user.defined.rule.stroke"));

		_editUpdateUserDefinedRuleMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.update.user.defined.rule.menu"),
			new UpdateUserDefinedRuleAction( ResourceManager.get_instance().get( "edit.update.user.defined.rule.menu"), this),
			ResourceManager.get_instance().get( "edit.update.user.defined.rule.mnemonic"),
			ResourceManager.get_instance().get( "edit.update.user.defined.rule.stroke"));

		_editExportUserDefinedRuleMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.export.user.defined.rule.menu"),
			new ExportUserDefinedRuleAction( ResourceManager.get_instance().get( "edit.export.user.defined.rule.menu"), this),
			ResourceManager.get_instance().get( "edit.export.user.defined.rule.mnemonic"),
			ResourceManager.get_instance().get( "edit.export.user.defined.rule.stroke"));

		_editRemoveUserDefinedRuleMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.remove.user.defined.rule.menu"),
			new RemoveUserDefinedRuleAction( ResourceManager.get_instance().get( "edit.remove.user.defined.rule.menu"), this),
			ResourceManager.get_instance().get( "edit.remove.user.defined.rule.mnemonic"),
			ResourceManager.get_instance().get( "edit.remove.user.defined.rule.stroke"));

		_popupMenu.addSeparator();

		_runStartVisualShellMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "run.start.visual.shell.menu"),
			new StartVisualShellAction( ResourceManager.get_instance().get( "run.start.visual.shell.menu"), this),
			ResourceManager.get_instance().get( "run.start.visual.shell.mnemonic"),
			ResourceManager.get_instance().get( "run.start.visual.shell.stroke"));

		_popupMenu.addSeparator();

		_runStartSimulatorMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "run.start.simulator.menu"),
			new StartSimulatorAction( ResourceManager.get_instance().get( "run.start.simulator.menu"), this),
			ResourceManager.get_instance().get( "run.start.simulator.mnemonic"),
			ResourceManager.get_instance().get( "run.start.simulator.stroke"));

		_popupMenu.addSeparator();

		_runStartApplicationBuilderMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "run.start.application.builder.menu"),
			new StartApplicationBuilderAction( ResourceManager.get_instance().get( "run.start.application.builder.menu"), this),
			ResourceManager.get_instance().get( "run.start.application.builder.mnemonic"),
			ResourceManager.get_instance().get( "run.start.application.builder.stroke"));
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

		File file = ( File)defaultMutableTreeNode.getUserObject();

		_editCopyMenuItem.setEnabled( true);
		_editPasteMenuItem.setEnabled( file.isDirectory() && exists_files_to_paste());
		_editDuplicateMenuItem.setEnabled( file.isFile());
		_editExportMenuItem.setEnabled( true);
		_editRemoveMenuItem.setEnabled( !parent.isRoot());
		_editRenameMenuItem.setEnabled( !parent.isRoot());
		_editNewDirectoryMenuItem.setEnabled( file.isDirectory());
		_editNewSimulationModelMenuItem.setEnabled( file.isDirectory());
		_editModelInformationMenuItem.setEnabled( file.isFile());
		_editImportUserDefinedRuleMenuItem.setEnabled( file.isFile() && !Executor.is_visualShell4( file) && !has_user_defined_rule( file));
		_editUpdateUserDefinedRuleMenuItem.setEnabled( file.isFile() && !Executor.is_visualShell4( file) && has_user_defined_rule( file));
		_editExportUserDefinedRuleMenuItem.setEnabled( file.isFile() && !Executor.is_visualShell4( file) && has_user_defined_rule( file));
		_editRemoveUserDefinedRuleMenuItem.setEnabled( file.isFile() && !Executor.is_visualShell4( file) && has_user_defined_rule( file));
		_runStartVisualShellMenuItem.setEnabled( file.isFile());
		_runStartSimulatorMenuItem.setEnabled( file.isFile());
		_runStartApplicationBuilderMenuItem.setEnabled( file.isFile());

		_popupMenu.show( this, point.x, point.y);
	}

	/**
	 * @param file
	 * @return
	 */
	private boolean has_user_defined_rule(File file) {
		// TODO Auto-generated method stub
		UserDefinedRule userDefinedRule = new UserDefinedRule( file);
		return userDefinedRule.exists();
	}

	/**
	 * @return
	 */
	private boolean exists_files_to_paste() {
		File[] files = Clipboard.get_files();
		return ( null != files && 0 < files.length);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_copy(java.awt.event.ActionEvent)
	 */
	public void on_edit_copy(ActionEvent actionEvent) {
		TreePath treePath = getSelectionPath();
		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		File file = ( File)defaultMutableTreeNode.getUserObject();
		if ( null == file)
			return;

		Clipboard.set( new File[] { file});
		_buttonMap.get( ResourceManager.get_instance().get( "edit.paste.menu")).setEnabled( file.isDirectory() && exists_files_to_paste());
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_paste(java.awt.event.ActionEvent)
	 */
	public void on_edit_paste(ActionEvent actionEvent) {
		File[] files = Clipboard.get_files();
		if ( null == files || 0 == files.length)
			return;

		TreePath treePath = getSelectionPath();
		DefaultMutableTreeNode targetNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		File parent = ( File)targetNode.getUserObject();
		if ( null == parent || !parent.isDirectory())
			return;

		on_start_paste();

		int result = IntProgressDlg.execute(
			_owner,
			ResourceManager.get_instance().get( "model.tree.copy.message"),
			true,
			"copy",
			ResourceManager.get_instance().get( "dialog.cancel"),
			new Object[] { files, parent, new Integer( DnDConstants.ACTION_COPY), new Boolean( false)},
			this, _parent);
		switch ( result) {
			case -1:
				Message.on_error_from_parent_to_child( _owner);
				break;
			case -2:
				Message.on_error_copy_or_move( _owner, DnDConstants.ACTION_COPY);
				break;
		}

		treePath = new TreePath( parent.getPath());
		if ( null == treePath) {
			Message.on_error_copy_or_move( _owner, DnDConstants.ACTION_COPY);
			return;
		}

//			if ( !isExpanded( treePath) && !treePath.equals( getSelectionPath()))
//				return true;

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		for ( int i = 0; i < files.length; ++i) {
			File newDirectory = new File( parent, files[ i].getName());
			if ( newDirectory.exists() && newDirectory.isDirectory() && !has_this_child( targetNode, newDirectory)) {
				defaultTreeModel.insertNodeInto( new DefaultMutableTreeNode( newDirectory), targetNode, targetNode.getChildCount());
			}
		}

		on_update( parent);

		select( parent);
		//select( new File( parent, files[ 0].getName()));

		on_paste_completed();
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_duplicate(java.awt.event.ActionEvent)
	 */
	public void on_edit_duplicate(ActionEvent actionEvent) {
		TreePath treePath = getSelectionPath();
		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		File file = ( File)defaultMutableTreeNode.getUserObject();
		if ( null == file || !file.isFile() || !file.getName().endsWith( ".soars"))
			return;

		File parent = file.getParentFile();
		if ( null == parent)
			return;

		File newFile = null;
		int index = 1;
		while ( true) {
			newFile = new File( parent, file.getName().substring( 0, file.getName().length() - ".soars".length()) + "_copy" + String.valueOf( index) + ".soars");
			if ( !newFile.exists())
				break;

			++index;
		}

		FileUtility.copy( file, newFile);

		on_update( parent);

		select( parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_export(java.awt.event.ActionEvent)
	 */
	public void on_edit_export(ActionEvent actionEvent) {
		TreePath treePath = getSelectionPath();
		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		File file = ( File)defaultMutableTreeNode.getUserObject();
		if ( null == file)
			return;

		File directory = get_export_directory();
		if ( null == directory)
			return;

		int result = IntProgressDlg.execute(
			_owner,
			ResourceManager.get_instance().get( "model.tree.copy.message"),
			true,
			"copy",
			ResourceManager.get_instance().get( "dialog.cancel"),
			new Object[] { new File[] { file}, directory, new Integer( DnDConstants.ACTION_COPY), new Boolean( false), new Boolean( false)},
			this, _parent);
		switch ( result) {
			case -1:
				Message.on_error_from_parent_to_child( _owner);
				break;
			case -2:
				Message.on_error_copy_or_move( _owner, DnDConstants.ACTION_COPY);
				break;
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_remove(java.awt.event.ActionEvent)
	 */
	public void on_edit_remove(ActionEvent actionEvent) {
		TreePath treePath = getSelectionPath();
		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		DefaultMutableTreeNode parentNode = ( DefaultMutableTreeNode)defaultMutableTreeNode.getParent();
		if ( parentNode.isRoot())
			return;

		if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
			_owner,
			ResourceManager.get_instance().get( "model.tree.confirm.remove.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION))
		return;

		File target = ( File)defaultMutableTreeNode.getUserObject();
		if ( null == target)
			return;

		File parent = target.getParentFile();

		if ( target.isFile()) {
			target.delete();
			NodeProperty.cleanup_nodePropertyMap( target, _nodePropertyMap);
		} else if ( target.isDirectory()) {
			FileUtility.delete( target, true);
			NodeProperty.cleanup_nodePropertyMap( _nodePropertyMap);
		}

		on_update( parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_rename(java.awt.event.ActionEvent)
	 */
	public void on_edit_rename(ActionEvent actionEvent) {
		TreePath treePath = getSelectionPath();
		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		DefaultMutableTreeNode parentNode = ( DefaultMutableTreeNode)defaultMutableTreeNode.getParent();
		if ( parentNode.isRoot())
			return;

		File file = ( File)defaultMutableTreeNode.getUserObject();
		if ( null == file)
			return;

		File originalFile = new File( file.getAbsolutePath());

		EditNameDlg editNameDlg = new EditNameDlg(
			_owner,
			ResourceManager.get_instance().get( file.isDirectory() ? "model.tree.edit.directory.name.dialog.title" : "model.tree.edit.file.name.dialog.title"),
			true,
			file.isDirectory() ? "directory" : "file",
			file,
			file.getParentFile(),
			".soars");
		if ( !editNameDlg.do_modal( _parent))
			return;

		if ( !editNameDlg._modified)
			return;

		on_rename( file.getParentFile(), originalFile, editNameDlg._file);
		select( editNameDlg._file);

		on_move( originalFile, editNameDlg._file);
		modified( this);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_new_directory(java.awt.event.ActionEvent)
	 */
	public void on_edit_new_directory(ActionEvent actionEvent) {
		TreePath treePath = getSelectionPath();
		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		File parent = ( File)defaultMutableTreeNode.getUserObject();
		if ( null == parent || !parent.isDirectory())
			return;

		EditNameDlg editNameDlg = new EditNameDlg(
			_owner,
			ResourceManager.get_instance().get( "model.tree.append.directory.name.dialog.title"),
			true,
			"directory",
			parent);
		if ( !editNameDlg.do_modal( _parent))
			return;

		if ( !editNameDlg._modified)
			return;

		on_new_directory( parent);
		select( editNameDlg._file);

		modified( this);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_new_simulation_model(java.awt.event.ActionEvent)
	 */
	public void on_edit_new_simulation_model(ActionEvent actionEvent) {
		TreePath treePath = getSelectionPath();
		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		DefaultMutableTreeNode parent = ( DefaultMutableTreeNode)defaultMutableTreeNode.getParent();
		File folder = ( File)defaultMutableTreeNode.getUserObject();
		if ( !folder.isDirectory())
			return;

		ModelInformationDlg modelInformationDlg = new ModelInformationDlg( _owner, ResourceManager.get_instance().get( "new.simulation.model.dialog.title"), true, folder, _mainPanel);
		if ( !modelInformationDlg.do_modal( _parent))
			return;

		refresh();
		select( modelInformationDlg._file);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_model_information(java.awt.event.ActionEvent)
	 */
	public void on_edit_model_information(ActionEvent actionEvent) {
		TreePath treePath = getSelectionPath();
		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		DefaultMutableTreeNode parent = ( DefaultMutableTreeNode)defaultMutableTreeNode.getParent();
		File file = ( File)defaultMutableTreeNode.getUserObject();
		if ( !file.isFile())
			return;

		NodeProperty nodeProperty = _nodePropertyMap.get( file);
		if ( null == nodeProperty)
			return;

		ModelInformationDlg modelInformationDlg = new ModelInformationDlg( _owner, ResourceManager.get_instance().get( "model.information.dialog.title"), true, nodeProperty, file, _mainPanel);
		if ( !modelInformationDlg.do_modal( _parent))
			return;

		refresh();
		select( modelInformationDlg._file);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_import_user_defined_rule(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_edit_import_user_defined_rule(ActionEvent actionEvent) {
		// TODO Auto-generated method stub
		update_user_defined_rule( "import");
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_update_user_defined_rule(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_edit_update_user_defined_rule(ActionEvent actionEvent) {
		// TODO Auto-generated method stub
		update_user_defined_rule( "update");
	}
	/**
	 * @param string
	 */
	private void update_user_defined_rule(String kind) {
		// TODO Auto-generated method stub
		TreePath treePath = getSelectionPath();
		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		DefaultMutableTreeNode parent = ( DefaultMutableTreeNode)defaultMutableTreeNode.getParent();
		File file = ( File)defaultMutableTreeNode.getUserObject();
		if ( !file.isFile())
			return;

		String projectFoldername = BasicEnvironment.get_instance().get( BasicEnvironment._projectFolderDirectoryKey, "");
		if ( projectFoldername.equals( ""))
			return;

		File srcFolder = new File( projectFoldername + Constant._userRuleExternalRelativePathName);
		if ( !srcFolder.exists() || !srcFolder.isDirectory())
			return;

		srcFolder = CommonTool.get_directory( Environment.get_instance(),
			Environment._userRuleImportDirectoryKey,
			srcFolder,
			ResourceManager.get_instance().get( kind + ".user.defined.rule.dialog.title"),
			MainFrame.get_instance());
		if ( null == srcFolder)
			return;

		File scriptFolder = new File( srcFolder, "script");
		File libraryFolder = new File( srcFolder, "library");
		if ( !scriptFolder.exists() || !scriptFolder.isDirectory() || !libraryFolder.exists() || !libraryFolder.isDirectory()) {
			JOptionPane.showMessageDialog( MainFrame.get_instance(),
				ResourceManager.get_instance().get( "invalid.user.defined.rule.error.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}

		if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
			MainFrame.get_instance(),
			ResourceManager.get_instance().get( kind + ".user.defined.rule.confirm.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION))
			return;

		UserDefinedRule userDefinedRule = new UserDefinedRule( file);
		if ( !userDefinedRule.update( srcFolder)) {
			JOptionPane.showMessageDialog( MainFrame.get_instance(),
				ResourceManager.get_instance().get( "could.not." + kind + ".user.defined.rule.error.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}
	}


	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_export_user_defined_rule(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_edit_export_user_defined_rule(ActionEvent actionEvent) {
		// TODO Auto-generated method stub
		TreePath treePath = getSelectionPath();
		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		DefaultMutableTreeNode parent = ( DefaultMutableTreeNode)defaultMutableTreeNode.getParent();
		File file = ( File)defaultMutableTreeNode.getUserObject();
		if ( !file.isFile())
			return;

		File destFolder = CommonTool.get_directory( Environment.get_instance(),
			Environment._userRuleExportDirectoryKey,
			ResourceManager.get_instance().get( "export.user.defined.rult.dialog.title"),
			MainFrame.get_instance());
		if ( null == destFolder)
			return;

		UserDefinedRule userDefinedRule = new UserDefinedRule( file);
		if ( !userDefinedRule.export( destFolder)) {
			JOptionPane.showMessageDialog( MainFrame.get_instance(),
				ResourceManager.get_instance().get( "could.not.export.user.defined.rule.error.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_remove_user_defined_rule(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_edit_remove_user_defined_rule(ActionEvent actionEvent) {
		// TODO Auto-generated method stub
		TreePath treePath = getSelectionPath();
		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		DefaultMutableTreeNode parent = ( DefaultMutableTreeNode)defaultMutableTreeNode.getParent();
		File file = ( File)defaultMutableTreeNode.getUserObject();
		if ( !file.isFile())
			return;

		if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
			MainFrame.get_instance(),
			ResourceManager.get_instance().get( "remove.user.defined.rule.confirm.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION))
			return;

		UserDefinedRule userDefinedRule = new UserDefinedRule( file);
		if ( !userDefinedRule.remove()) {
			JOptionPane.showMessageDialog( MainFrame.get_instance(),
				ResourceManager.get_instance().get( "could.not.remove.user.defined.rule.error.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.run.IRunMenuHandler#on_run_start_visual_shell(java.awt.event.ActionEvent)
	 */
	public void on_run_start_visual_shell(ActionEvent actionEvent) {
		TreePath treePath = getSelectionPath();
		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		File file = ( File)defaultMutableTreeNode.getUserObject();
		if ( null == file || !file.exists() || !file.isFile())
			return;

		MainFrame.get_instance().store();
		VisualShell.start( file);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.run.IRunMenuHandler#on_run_start_simulator(java.awt.event.ActionEvent)
	 */
	public void on_run_start_simulator(ActionEvent actionEvent) {
		TreePath treePath = getSelectionPath();
		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		File file = ( File)defaultMutableTreeNode.getUserObject();
		if ( null == file || !file.exists() || !file.isFile())
			return;

		MainFrame.get_instance().store();
		SimulationRunner.start( file);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.run.IRunMenuHandler#on_run_start_library_manager(java.awt.event.ActionEvent)
	 */
	public void on_run_start_library_manager(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.run.IRunMenuHandler#on_run_start_application_builder(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_run_start_application_builder(ActionEvent actionEvent) {
		TreePath treePath = getSelectionPath();
		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		File file = ( File)defaultMutableTreeNode.getUserObject();
		if ( null == file || !file.exists() || !file.isFile())
			return;

		MainFrame.get_instance().store();
		ApplicationBuilder.start( file);
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();
		DefaultMutableTreeNode child = ( DefaultMutableTreeNode)root.getChildAt( 0);
		setSelectionPath( new TreePath( child.getPath()));
	}

	/**
	 * 
	 */
	public void on_exit() {
		List<NodeProperty> properties = new ArrayList<NodeProperty>( _nodePropertyMap.values());
		for ( int i = 0; i < properties.size(); ++i)
			properties.get( i).cleanup();
	}

	/**
	 * @param directory
	 */
	public void expand(File directory) {
		if ( null == directory /*|| !directory.isDirectory()*/)
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

	/**
	 * @param directory
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
			Node node = new Node( _nodePropertyMap);
			if ( !node.create( child, this, this))
				continue;

			nodes.add( node);
		}

		root = new DefaultMutableTreeNode( "");
		defaultTreeModel.setRoot( root);
		expandPath( new TreePath( root.getPath()));

		for ( int i = 0; i < nodes.size(); ++i) {
			DefaultMutableTreeNode child = new DefaultMutableTreeNode( nodes.get( i)._file);
			NodeProperty.update_nodePropertyMap( nodes.get( i)._file, _nodePropertyMap);
			defaultTreeModel.insertNodeInto( child, root, root.getChildCount());
			nodes.get( i).make( child, defaultTreeModel, this);
		}

		expand( directory);
	}

	/**
	 * @param directory
	 * @param oldPath
	 * @param newPath
	 */
	public void on_rename(File directory, File oldPath, File newPath) {
		// ディレクトリ名が変更された時
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();
		List<Node> nodes = new ArrayList<Node>();
		for ( int i = 0; i < root.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)root.getChildAt( i);
			Node node = new Node( _nodePropertyMap);
			if ( !node.create( oldPath, newPath, child, this, this))
				return;

			nodes.add( node);
		}

		root = new DefaultMutableTreeNode( "");
		defaultTreeModel.setRoot( root);
		expandPath( new TreePath( root.getPath()));

		for ( int i = 0; i < nodes.size(); ++i) {
			DefaultMutableTreeNode child = new DefaultMutableTreeNode( nodes.get( i)._file);
			NodeProperty.update_nodePropertyMap( nodes.get( i)._file, _nodePropertyMap);
			defaultTreeModel.insertNodeInto( child, root, root.getChildCount());
			nodes.get( i).make( child, defaultTreeModel, this);
		}

		expand( directory);
	}

	/**
	 * @param directory
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

		Arrays.sort( files, new FileNameComparator( _nodePropertyMap, true, false));

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();

		for ( int i = 0; i < files.length; ++i) {
			if ( exist( parent, files[ i]))
				continue;

			if ( !visible( files[ i]))
				continue;

			NodeProperty.update_nodePropertyMap( files[ i], _nodePropertyMap);

			defaultTreeModel.insertNodeInto( new DefaultMutableTreeNode( files[ i]), parent, parent.getChildCount());
		}

		expandPath( treePath);

		return true;
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
	 * 
	 */
	public void refresh() {
		File selectedFile = get_selected_file();
		NodeProperty nodeProperty = ( null != selectedFile) ? _nodePropertyMap.get( selectedFile) : null;
		if ( null != nodeProperty)
			_soarsContentsPage.get( nodeProperty._tableSelection);

		Map<File, TableSelection> tableSelectionMap = new HashMap<File, TableSelection>();
		NodeProperty.copy( _nodePropertyMap, tableSelectionMap);
		NodeProperty.cleanup_nodePropertyMap_all( _nodePropertyMap);

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();

		List<Node> nodes = new ArrayList<Node>();

		for ( int i = 0; i < root.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)root.getChildAt( i);
			Node node = new Node( _nodePropertyMap);
			if ( !node.create( child, this, this))
				continue;

			nodes.add( node);
		}

		root = new DefaultMutableTreeNode( "");
		defaultTreeModel.setRoot( root);
		expandPath( new TreePath( root.getPath()));

		for ( int i = 0; i < nodes.size(); ++i) {
			DefaultMutableTreeNode child = new DefaultMutableTreeNode( nodes.get( i)._file);
			NodeProperty.update_nodePropertyMap( nodes.get( i)._file, _nodePropertyMap, tableSelectionMap);
			defaultTreeModel.insertNodeInto( child, root, root.getChildCount());
			nodes.get( i).make( child, defaultTreeModel, this, tableSelectionMap);
		}

		select( selectedFile);

		_imagePanel.refresh();
		_soarsDocEditorPane.refresh();
		_soarsContentsPage.refresh();
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
			&& !dataFlavors[ 0].getHumanPresentableName().equals( DirectoryNodeTransferable._name)) {
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

		File file = ( File)targetDefaultMutableTreeNode.getUserObject();
		if ( !file.isDirectory()) {
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
				List<File> list = ( List<File>)transferable.getTransferData( DataFlavor.javaFileListFlavor);
				if ( null == list || list.isEmpty()) {
					arg0.getDropTargetContext().dropComplete( true);
					_dropTargetTreeNode = null;
					return;
				}

				File[] files = list.toArray( new File[ 0]);
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
				List<File> list = new ArrayList<File>();
				while( stringTokenizer.hasMoreElements()) {
					URI uri = new URI( ( String)stringTokenizer.nextElement());
					if ( uri.getScheme().equals( "file"))
						list.add( new File( uri.getPath()));
				}
				File[] files = list.toArray( new File[ 0]);
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
						on_start_paste_and_remove();
						paste_and_remove( files, arg0.getDropAction(), true);
						on_paste_and_remove_completed();
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
	private void on_paste(File[] files, int action) {
		on_start_paste();
		paste( files, action);
		on_paste_completed();
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
			Message.on_error_copy_or_move( _owner, action);
			return false;
		}

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		srcDirectory = ( File)defaultMutableTreeNode.getUserObject();

		DefaultMutableTreeNode targetNode = ( DefaultMutableTreeNode)_dropTargetTreeNode;

		File targetDirectory = ( File)targetNode.getUserObject();
		if ( null == targetDirectory) {
			Message.on_error_copy_or_move( _owner, action);
			return false;
		}

		int result = IntProgressDlg.execute(
			_owner,
			ResourceManager.get_instance().get( "model.tree.copy.message"),
			true,
			"copy",
			ResourceManager.get_instance().get( "dialog.cancel"),
			new Object[] { files, targetDirectory, new Integer( action), new Boolean( false)},
			this, _parent);
		switch ( result) {
			case -1:
				Message.on_error_from_parent_to_child( _owner);
				break;
			case -2:
				Message.on_error_copy_or_move( _owner, action);
				break;
		}

		treePath = new TreePath( targetNode.getPath());
		if ( null == treePath) {
			Message.on_error_copy_or_move( _owner, action);
			return false;
		}

//		if ( !isExpanded( treePath) && !treePath.equals( getSelectionPath()))
//			return true;

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		for ( int i = 0; i < files.length; ++i) {
			File newDirectory = new File( targetDirectory, files[ i].getName());
			if ( newDirectory.exists() && newDirectory.isDirectory() && !has_this_child( targetNode, newDirectory)) {
				defaultTreeModel.insertNodeInto( new DefaultMutableTreeNode( newDirectory), targetNode, targetNode.getChildCount());
			}
		}

		on_update( srcDirectory);

		select( new File( targetDirectory, files[ 0].getName()));

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
			Message.on_error_copy_or_move( _owner, action);
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
			Message.on_error_copy_or_move( _owner, action);
			return false;
		}

		int result = IntProgressDlg.execute(
			_owner,
			ResourceManager.get_instance().get( "model.tree.move.message"),
			true,
			"move",
			ResourceManager.get_instance().get( "dialog.cancel"),
			new Object[] { files, targetDirectory, new Integer( action), new Boolean( false)},
			this, _parent);
		switch ( result) {
			case -1:
				Message.on_error_from_parent_to_child( _owner);
				break;
			case -2:
				Message.on_error_copy_or_move( _owner, action);
				break;
//			case -3:
//				Message.on_error_move( _fileManager);
//				break;
		}

		treePath = new TreePath( targetNode.getPath());
		if ( null == treePath) {
			Message.on_error_copy_or_move( _owner, action);
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
				ResourceManager.get_instance().get( "model.tree.move.message"),
				true,
				"remove",
				ResourceManager.get_instance().get( "dialog.cancel"),
				new Object[] { files, targetDirectory, new Integer( action)},
				this, _parent);
			switch ( result) {
				case -3:
					Message.on_error_move( _owner);
					break;
			}
		}

		on_update( srcDirectory);

		select( new File( targetDirectory, files[ 0].getName()));

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
		if ( !vsl2soars( files))
			return -2;

		return Utility.paste( files, targetDirectory, action, auto, true, this, this, _owner, intProgressDlg);
	}

	/**
	 * @param files
	 * @return
	 */
	private boolean vsl2soars(File[] files) {
		for ( int i = 0; i < files.length; ++i) {
			if ( files[ i].isDirectory()) {
				if ( !vsl2soars( files[ i].listFiles()))
					return false;
			} else {
				if ( !files[ i].getName().endsWith( ".vsl"))
					continue;

				File file = Converter.vsl2soars( files[ i]);
				if ( null == file)
					return false;

				files[ i] = file;
			}
		}
		return true;
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
		return Utility.paste( files, targetDirectory, action, auto, true, this, this, _owner, intProgressDlg);
	}

	/**
	 * @param files
	 * @param intProgressDlg
	 * @return
	 */
	private int remove(File[] files, IntProgressDlg intProgressDlg) {
		intProgressDlg.set( 100);
		for ( int i = 0; i < files.length; ++i) {
			if ( files[ i].isFile()) {
				if ( !files[ i].delete()) {
					return -3;
				}
				NodeProperty.cleanup_nodePropertyMap( files[ i], _nodePropertyMap);
				modified( this);
			} else if ( files[ i].isDirectory()) {
				if ( !FileUtility.delete( files[ i], true)) {
					return -3;
				}
				NodeProperty.cleanup_nodePropertyMap( _nodePropertyMap);
				modified( this);
			} else {
				return -3;
			}
		}
		return 1;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_start_paste()
	 */
	public void on_start_paste() {
		WarningManager.get_instance().cleanup();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_start_paste_and_remove()
	 */
	public void on_start_paste_and_remove() {
		WarningManager.get_instance().cleanup();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#can_copy(java.io.File)
	 */
	public boolean can_copy(File file) {
		if ( file.getName().startsWith( "."))
			return false;

		if ( file.isDirectory())
			return true;

		if ( !file.isFile())
			return false;

//		String data = ZipUtility.get_text( file, Constant._rootDirectoryName + "/" + Constant._dataFilename, "UTF8");
//		String data = get_text( file, Constant._rootDirectoryName + "/" + Constant._visualShellZipFileName, Constant._visualShellRootDirectoryName + "/" + Constant._visualShellDataFilename, "UTF8");
//		if ( null == data)
//			return false;
		byte[] data = ZipUtility.get_binary( file, Constant._soarsRootDirectoryName + "/" + Constant._visualShellZipFileName);
		if ( null == data)
			return false;

		String text = ZipUtility.get_text( data, Constant._visualShellRootDirectoryName + "/" + Constant._visualShellDataFilename, "UTF8");
		if ( null == text)
			return false;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#can_paste(java.io.File)
	 */
	public boolean can_paste(File file) {
		return !file.getName().startsWith( ".");
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_paste_completed()
	 */
	public void on_paste_completed() {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_paste_and_remove_completed()
	 */
	public void on_paste_and_remove_completed() {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#modified(soars.common.utility.swing.file.manager.IFileManager)
	 */
	public void modified(IFileManager fileManager) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#select_changed(soars.common.utility.swing.file.manager.IFileManager)
	 */
	public void select_changed(IFileManager fileManager) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#can_remove(java.io.File)
	 */
	public boolean can_remove(File file) {
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_move(java.io.File, java.io.File)
	 */
	public void on_move(File srcPath, File destPath) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_select(java.io.File, java.lang.String)
	 */
	public void on_select(File file, String encoding) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_rename(java.io.File, java.io.File)
	 */
	public void on_rename(File originalFile, File newFile) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#get_export_directory()
	 */
	public File get_export_directory() {
		String exportDirectory = "";
		File directory = null;
		
		String value = Environment.get_instance().get( Environment._exportFilesDirectoryKey, "");
		if ( null != value && !value.equals( "")) {
			directory = new File( value);
			if ( directory.exists())
				exportDirectory = value;
		}
	
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle( ResourceManager.get_instance().get( "model.tree.export.files.title"));
		fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);

		if ( !exportDirectory.equals( "")) {
			fileChooser.setCurrentDirectory( new File( exportDirectory + "/../"));
			fileChooser.setSelectedFile( directory);
		}

		if ( JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog( _owner)) {
			directory = fileChooser.getSelectedFile();
			exportDirectory = directory.getAbsolutePath();
			Environment.get_instance().set( Environment._exportFilesDirectoryKey, exportDirectory);
			return directory;
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#visible(java.io.File)
	 */
	public boolean visible(File file) {
		return ( !file.getName().startsWith( ".") && ( !file.isFile() || file.getName().endsWith( ".soars")));
		//return ( !file.getName().startsWith( ".") && ( !file.isFile() || file.getName().endsWith( ".vsl")));
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_clear_image(java.awt.event.ActionEvent)
	 */
	public void on_edit_clear_image(ActionEvent actionEvent) {
	}

	/**
	 * @param file
	 * @return
	 */
	public String get_title(File file) {
		NodeProperty nodeProperty = _nodePropertyMap.get( file);
		if ( null == nodeProperty)
			return null;

		return nodeProperty._title;
	}
}
