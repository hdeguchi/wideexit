/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tree;

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
import soars.application.visualshell.object.role.base.edit.tab.RuleTabbedPane;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;

/**
 * @author kurata
 *
 */
public class RuleTree extends TreeBase {

	/**
	 * 
	 */
	public RuleTabbedPane _ruleTabbedPane = null;

	/**
	 * 
	 */
	public Map<RulePropertyPanelBase, DefaultMutableTreeNode> _map = new HashMap<RulePropertyPanelBase, DefaultMutableTreeNode>();

	/**
	 * 
	 */
	public String _kind = "";

	/**
	 * @param kind
	 * @param owner
	 * @param parent
	 */
	public RuleTree(String kind, Frame owner, Component parent) {
		super(owner, parent);
		_kind = kind;
	}

	/**
	 * @param ruleTabbedPane 
	 * @param rootText
	 * @return
	 */
	public boolean setup(RuleTabbedPane ruleTabbedPane, String rootText) {
		_ruleTabbedPane = ruleTabbedPane;

		if ( !super.setup( false))
			return false;

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode( rootText);
		defaultTreeModel.setRoot( root);

		addTreeSelectionListener( new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				on_valueChanged( e);
			}
		});

		setCellRenderer( new RuleTreeCellRenderer());

		return true;
	}

	/**
	 * @param treeSelectionEvent
	 */
	protected void on_valueChanged(TreeSelectionEvent treeSelectionEvent) {
		//JOptionPane.showMessageDialog( getParent(), "changed", "RuleTree", JOptionPane.INFORMATION_MESSAGE);
		TreePath treePath = treeSelectionEvent.getPath();
		if ( null == treePath)
			return;

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		Object object = defaultMutableTreeNode.getUserObject();

		if ( object instanceof RulePropertyPanelBase)
			_ruleTabbedPane.select( ( RulePropertyPanelBase)object);
		else
			expandPath( treePath);
	}

	/**
	 * @param rulePropertyPanelBase
	 */
	public void append(RulePropertyPanelBase rulePropertyPanelBase) {
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();
		DefaultMutableTreeNode child = new DefaultMutableTreeNode( rulePropertyPanelBase);
		root.add( child);
		_map.put( rulePropertyPanelBase, child);
	}

	/**
	 * @param nodeTexts
	 * @param rulePropertyPanelBase
	 */
	public void append(String[] nodeTexts, RulePropertyPanelBase rulePropertyPanelBase) {
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode parent = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();
		for ( String nodeText:nodeTexts) {
			DefaultMutableTreeNode defaultMutableTreeNode = get_child( parent, nodeText);
			if ( null == defaultMutableTreeNode) {
				defaultMutableTreeNode = new DefaultMutableTreeNode( nodeText);
				parent.add( defaultMutableTreeNode);
			}
			parent = defaultMutableTreeNode;
		}
		DefaultMutableTreeNode child = new DefaultMutableTreeNode( rulePropertyPanelBase); 
		parent.add( child);
		_map.put( rulePropertyPanelBase, child);
	}

	/**
	 * @param parent
	 * @param nodeText
	 * @return
	 */
	private DefaultMutableTreeNode get_child(DefaultMutableTreeNode parent, String nodeText) {
		for ( int i = 0; i < parent.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)parent.getChildAt( i);
			if ( null == child)
				continue;

			Object object = child.getUserObject();
			if ( null == object)
				continue;

			if ( !( object instanceof String))
				continue;

			String text = ( String)object;
			if ( text.equals( nodeText))
				return child;
		}

		return null;
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
	@Override
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
	 * @param rulePropertyPanelBase
	 */
	public void select(RulePropertyPanelBase rulePropertyPanelBase) {
		DefaultMutableTreeNode defaultMutableTreeNode = _map.get( rulePropertyPanelBase);
		if ( null == defaultMutableTreeNode)
			return;

		TreePath treePath = new TreePath( defaultMutableTreeNode.getPath());
		setSelectionPath( treePath);
		expandPath( treePath);
	}

	/**
	 * @return
	 */
	public RulePropertyPanelBase get_selected_page() {
		TreePath treePath = getSelectionPath();
		if ( null == treePath)
			return null;

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		if ( defaultMutableTreeNode.isRoot())
			return null;

		Object object = defaultMutableTreeNode.getUserObject();
		if ( null == object)
			return null;

		if ( !( object instanceof RulePropertyPanelBase))
			return null;

		return ( RulePropertyPanelBase)object;
	}
}
