/**
 * 
 */
package soars.application.builder.animation.main.tree;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import soars.application.builder.animation.document.Document;
import soars.application.builder.animation.document.DocumentManager;
import soars.application.builder.animation.main.ResourceManager;
import soars.application.builder.animation.main.panel.MainPanel;
import soars.common.utility.swing.tree.StandardTree;

/**
 * @author kurata
 *
 */
public class LanguageTree extends StandardTree {


	/**
	 * 
	 */
	private MainPanel _mainPanel = null;

	/**
	 * @param mainPanel
	 * @param owner
	 * @param parent
	 */
	public LanguageTree(MainPanel mainPanel, Frame owner, Component parent) {
		super(owner, parent);
		_mainPanel  = mainPanel;
	}

	/**
	 * @return
	 */
	public boolean setup() {
		if ( !super.setup(false))
			return false;


		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode( ResourceManager.get_instance().get( "language.tree.root"));
		defaultTreeModel.setRoot( root);


		for ( int i = 0; i < DocumentManager._languages.length; ++i) {
			Document document = ( Document)DocumentManager.get_instance().get( DocumentManager._languages[ i][ 1]);
			if ( null == document)
				return false;

			root.add( new DefaultMutableTreeNode( document));
		}
		

		setCellRenderer( new LanguageTreeCellRenderer());


		addTreeSelectionListener( new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				on_valueChanged( e);
			}
		});


		TreePath treePath = new TreePath( root.getPath());
		expandPath( treePath);


		DefaultMutableTreeNode child = ( DefaultMutableTreeNode)root.getFirstChild();
		setSelectionPath( new TreePath( child.getPath()));


		return true;
	}

	/**
	 * @param treeSelectionEvent
	 */
	protected void on_valueChanged(TreeSelectionEvent treeSelectionEvent) {
		TreePath treePath = treeSelectionEvent.getOldLeadSelectionPath();
		Document previous = get_document( treePath);

		treePath = treeSelectionEvent.getNewLeadSelectionPath();
		Document next = get_document( treePath);

		if ( null != previous && null != next && previous.equals( next))
			return;

		_mainPanel.update( previous, next);
	}

	/**
	 * @param treePath
	 * @return
	 */
	private Document get_document(TreePath treePath) {
		if ( null == treePath)
			return null;

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		Object object = defaultMutableTreeNode.getUserObject();
		return ( ( object instanceof Document) ? ( Document)object : null);
	}

	/**
	 * 
	 */
	public void store() {
		TreePath treePath = getSelectionPath();
		_mainPanel.store( get_document( treePath));
	}

	/**
	 * 
	 */
	public void load() {
		TreePath treePath = getSelectionPath();
		_mainPanel.load( get_document( treePath));
	}
}
