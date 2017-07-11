/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.tree;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import soars.application.visualshell.common.swing.TreeBase;
import soars.application.visualshell.object.entity.base.edit.panel.ObjectPanel;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;

/**
 * @author kurata
 *
 */
public class ObjectTree extends TreeBase {

	/**
	 * 
	 */
	public ObjectPanel _objectPanel = null;

	/**
	 * 
	 */
	public Map<PropertyPanelBase, DefaultMutableTreeNode> _map = new HashMap<PropertyPanelBase, DefaultMutableTreeNode>();

	/**
	 * @param owner
	 * @param parent
	 */
	public ObjectTree(Frame owner, Component parent) {
		super(owner, parent);
	}

	/**
	 * @param objectPanel
	 * @param title
	 * @return
	 */
	public boolean setup(ObjectPanel objectPanel, String title) {
		_objectPanel = objectPanel;

		if ( !super.setup( false))
			return false;

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode( title);
		defaultTreeModel.setRoot( root);

		addTreeSelectionListener( new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				on_valueChanged( e);
			}
		});

		setCellRenderer( new ObjectTreeCellRenderer());

		return true;
	}

	/**
	 * @param treeSelectionEvent
	 */
	protected void on_valueChanged(TreeSelectionEvent treeSelectionEvent) {
		TreePath treePath = treeSelectionEvent.getOldLeadSelectionPath();
		if ( null == treePath)
			return;

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		Object object = defaultMutableTreeNode.getUserObject();

		if ( object instanceof Component && !_objectPanel.confirm( ( Component)object)) {
			setSelectionPath( treePath);
			return;
		}

		treePath = treeSelectionEvent.getPath();
		if ( null == treePath)
			return;

		defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		object = defaultMutableTreeNode.getUserObject();

		if ( object instanceof Component)
			_objectPanel.select( ( Component)object);
		else
			expandPath( treePath);
//		TreePath treePath = treeSelectionEvent.getPath();
//		if ( null == treePath)
//			return;
//
//		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
//		Object object = defaultMutableTreeNode.getUserObject();
//
//		if ( object instanceof Component)
//			_objectPanel.select( ( Component)object);
//		else
//			expandPath( treePath);
	}

	/**
	 * @param propertyPanelBase
	 */
	public void append(PropertyPanelBase propertyPanelBase) {
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();
		DefaultMutableTreeNode child = new DefaultMutableTreeNode( propertyPanelBase);
		root.add( child);
		_map.put( propertyPanelBase, child);
	}

	/**
	 * 
	 */
	public void expand() {
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();
		if ( 0 < root.getChildCount()) {
			TreePath treePath = new TreePath( root.getPath());
			expandPath( treePath);
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.tree.StandardTree#on_mouse_pressed(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_pressed(MouseEvent mouseEvent) {
		TreePath treePath = getPathForLocation( mouseEvent.getX(), mouseEvent.getY());
		if ( null == treePath)
			return;

		setSelectionPath( treePath);

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		if ( 0 < defaultMutableTreeNode.getChildCount() && !isExpanded( treePath))
			expandPath( treePath);

		requestFocus();
	}

	/**
	 * @param propertyPanelBase
	 */
	public void select(PropertyPanelBase propertyPanelBase) {
		DefaultMutableTreeNode defaultMutableTreeNode = _map.get( propertyPanelBase);
		if ( null == defaultMutableTreeNode)
			return;

		TreePath treePath = new TreePath( defaultMutableTreeNode.getPath());
		setSelectionPath( treePath);
		expandPath( treePath);
	}
}
