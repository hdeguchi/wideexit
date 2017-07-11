/**
 * 
 */
package soars.application.visualshell.object.common.arbitrary;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
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
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import soars.application.visualshell.common.swing.TreeBase;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.arbitrary.JarFileProperties;
import soars.common.soars.module.Module;
import soars.common.utility.swing.dnd.dom.DomNodeTransferable;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.xml.dom.XmlTool;

/**
 * @author kurata
 *
 */
public class ClassTree extends TreeBase implements DragGestureListener, DragSourceListener, DropTargetListener {

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
	private IClassTreeCallback _classTreeCallback = null;

	/**
	 * @param owner
	 * @param parent
	 * @param classTreeCallback
	 */
	public ClassTree(Frame owner, Component parent, IClassTreeCallback classTreeCallback) {
		super(owner, parent);
		_classTreeCallback = classTreeCallback;
	}

	/**
	 * @param names
	 * @return
	 */
	public boolean select(String[][] names) {
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();

		for ( int i = 0; i < root.getChildCount(); ++i) {
			DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)root.getChildAt( i);
			Object object = ( Node)defaultMutableTreeNode.getUserObject();
			if ( null == object || !( object instanceof Node))
				return false;

			Node node = ( Node)object;
			String name = XmlTool.get_attribute( node, "name");
			if ( null == name)
				return false;

			if ( !names[ 0][ 1].startsWith( name))
				continue;

			expand( defaultMutableTreeNode, node);
			return select( names, defaultMutableTreeNode);
		}

		return false;
	}

	/**
	 * @param names
	 * @param defaultMutableTreeNode
	 * @return
	 */
	private boolean select(String[][] names, DefaultMutableTreeNode defaultMutableTreeNode) {
		if ( names[ 0][ 1].equals( Constant._javaClasses))
			return select( names[ 1][ 1], defaultMutableTreeNode);
		else {
			defaultMutableTreeNode = get_module( names[ 0][ 1], defaultMutableTreeNode);
			if ( null == defaultMutableTreeNode)
				return false;

			File jarfile = new File( names[ 0][ 1]);
			if ( null == jarfile || !jarfile.exists() || !jarfile.isFile())
				return false;

			defaultMutableTreeNode = get_jarfile( jarfile, defaultMutableTreeNode);
			if ( null == defaultMutableTreeNode)
				return false;

			return select( names[ 1][ 1], defaultMutableTreeNode);
		}
	}

	/**
	 * @param jarfilename
	 * @param parent
	 * @return
	 */
	private DefaultMutableTreeNode get_module(String jarfilename, DefaultMutableTreeNode parent) {
		DefaultMutableTreeNode default_module = null;
		Node default_node = null;
		for ( int i = 0; i < parent.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)parent.getChildAt( i);
			Object object = child.getUserObject();
			if ( null == object || !( object instanceof Node))
				continue;

			Node node = ( Node)object;
			String name = XmlTool.get_attribute( node, "name");
			if ( null == name)
				continue;

			if ( name.equals( Constant._noDefinedModule)) {
				default_module = child;
				default_node = node;
				continue;
			}

			File modulefile = new File( name);
			if ( null == modulefile || !modulefile.exists() || !modulefile.isFile())
				continue;

			File jarfile = new File( jarfilename);
			if ( null == jarfile || !jarfile.exists() || !jarfile.isFile())
				continue;

			if ( !FileUtility.is_parent( jarfile, modulefile.getParentFile()))
				continue;

			expand( child, node);
			return child;
		}

		if ( null == default_module || null == default_node)
			return null;

		expand( default_module, default_node);
		return default_module;
	}

	/**
	 * @param jarfile
	 * @param parent
	 * @return
	 */
	private DefaultMutableTreeNode get_jarfile(File jarfile, DefaultMutableTreeNode parent) {
		for ( int i = 0; i < parent.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)parent.getChildAt( i);
			Object object = child.getUserObject();
			if ( null == object || !( object instanceof Node))
				continue;

			Node node = ( Node)object;
			String type = node.getNodeName();
			String name = XmlTool.get_attribute( node, "name");
			if ( null == name)
				continue;

			if ( type.equals( "folder")) {
				File folder = new File( name);
				if ( null == folder || !folder.exists() || !folder.isDirectory())
					continue;

				if ( !FileUtility.is_parent( jarfile, folder))
					continue;

				expand( child, node);
				return get_jarfile( jarfile, child);
			} else if ( type.equals( "jarfile")) {
				File file = new File( name);
				if ( null == file || !file.exists() || !file.isFile())
					continue;

				if ( !jarfile.equals( file))
					continue;

				expand( child, node);
				return child;
			}
		}
		return null;
	}

	/**
	 * @param classname
	 * @param parent
	 * @return
	 */
	private boolean select(String classname, DefaultMutableTreeNode parent) {
		for ( int i = 0; i < parent.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)parent.getChildAt( i);
			Object object = child.getUserObject();
			if ( null == object || !( object instanceof Node))
				continue;

			Node node = ( Node)object;
			String name = XmlTool.get_attribute( node, "name");
			if ( null == name)
				continue;

			if ( !classname.equals( name))
				continue;

			TreePath treePath = new TreePath( child.getPath());
			setSelectionPath( treePath);
			scrollPathToVisible( treePath);

			if ( node.getNodeName().equals( "module")
				|| node.getNodeName().equals( "folder")
				|| node.getNodeName().equals( "jarfile")
				|| node.getNodeName().equals( "class"))
				_classTreeCallback.selected( node);

			return true;
		}
		return false;
	}

	/**
	 * @param dnd
	 * @return
	 */
	public boolean setup(boolean dnd) {
		if ( !super.setup( false))
			return false;

		Node node = JarFileProperties.get_instance().get_root_node();
		if ( null == node)
			return false;

		getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION);

		setCellRenderer( new ClassTreeCellRenderer());

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode( node);

		defaultTreeModel.setRoot( root);

		node = XmlTool.get_node( JarFileProperties.get_instance().get_root_node(), "root[@name=\"" + Constant._javaClasses + "\"]");
		DefaultMutableTreeNode child = new DefaultMutableTreeNode( node);
		root.add( child);

		String[] rootDirectories = new String[] { Constant._functionalObjectDirectories[ 0], Constant._functionalObjectDirectories[ 1]};

		for ( int i = 0; i < rootDirectories.length; ++i) {
			node = XmlTool.get_node( JarFileProperties.get_instance().get_root_node(), "root[@name=\"" + rootDirectories[ i] + "\"]");
			child = new DefaultMutableTreeNode( node);
			root.add( child);
		}

		expandPath( new TreePath( root.getPath()));

		setRootVisible( false);

		addTreeSelectionListener( new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				on_valueChanged( e);
			}
		});

		setToolTipText( "ClassTree");

		if ( dnd) {
			new DragSource().createDefaultDragGestureRecognizer( this, DnDConstants.ACTION_COPY_OR_MOVE, this);
			new DropTarget( this, this);
		}

		return true;
	}

	/**
	 * @param treeSelectionEvent
	 */
	protected void on_valueChanged(TreeSelectionEvent treeSelectionEvent) {
		TreePath treePath = treeSelectionEvent.getPath();
		if ( null == treePath) {
			_classTreeCallback.selected( null);
			return;
		}

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		Object object = defaultMutableTreeNode.getUserObject();
		if ( null == object || !( object instanceof Node)) {
			_classTreeCallback.selected( null);
			return;
		}

//		Node node = ( Node)object;
//		if ( !node.getNodeName().equals( "class")) {
//			_classTreeCallback.selected( null);
//			return;
//		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTree#getToolTipText(java.awt.event.MouseEvent)
	 */
	public String getToolTipText(MouseEvent arg0) {
		TreePath treePath = getPathForLocation( arg0.getX(), arg0.getY());
		if ( null == treePath)
			return null;

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		Object object = defaultMutableTreeNode.getUserObject();
		if ( null == object || !( object instanceof Node))
			return null;

		Node node = ( Node)object;
		if ( node.getNodeName().equals( "jarfile") || node.getNodeName().equals( "class"))
			return XmlTool.get_attribute( node, "name");

		return null;
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
		Object object = defaultMutableTreeNode.getUserObject();
		if ( null == object || !( object instanceof Node))
			return;

		Node node = ( Node)object;
		if ( node.getNodeName().equals( "module")
			|| node.getNodeName().equals( "folder")
			|| node.getNodeName().equals( "jarfile")
			|| node.getNodeName().equals( "class"))
			_classTreeCallback.selected( node);

		if ( !node.getNodeName().equals( "class")) {
			if ( node.getNodeName().equals( "root") && isExpanded( treePath)) {
				collapsePath( treePath);
				return;
			}

			if ( 0 < defaultMutableTreeNode.getChildCount()) {
				if ( !isExpanded( treePath))
					expandPath( treePath);
				return;
			}

			expand( defaultMutableTreeNode, node);
		}
	}

	/**
	 * @param defaultMutableTreeNode
	 * @param node
	 */
	private void expand(DefaultMutableTreeNode defaultMutableTreeNode, Node node) {
		TreePath treePath = new TreePath( defaultMutableTreeNode.getPath());
		if ( isExpanded( treePath))
			return;

		Node[] nodes = get_child_nodes( node);
		if ( null == nodes)
			return;

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		for ( int i = 0; i < nodes.length; ++i) {
			if ( null == nodes[ i])
				continue;

			DefaultMutableTreeNode childDefaultMutableTreeNode = new DefaultMutableTreeNode( nodes[ i]);
			defaultTreeModel.insertNodeInto( childDefaultMutableTreeNode, defaultMutableTreeNode, defaultMutableTreeNode.getChildCount());
		}

		expandPath( treePath);
	}

	/**
	 * @param parent
	 * @return
	 */
	private Node[] get_child_nodes(Node parent) {
		NodeList nodeList = parent.getChildNodes();
		List<Node> list = new ArrayList<Node>();
		for ( int i = 0; i < nodeList.getLength(); ++i) {
			Node node = nodeList.item( i);
			if ( ( node.getNodeName().equals( "module") || node.getNodeName().equals( "folder")) && !contains( node))
				continue;

			list.add( node);
		}

		Node[] nodes = list.toArray( new Node[ 0]);
		Arrays.sort( nodes, new NodeComparator());

		return nodes;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean contains(Node parent) {
		NodeList nodeList = parent.getChildNodes();
		for ( int i = 0; i < nodeList.getLength(); ++i) {
			Node node = nodeList.item( i);
			if ( null == node)
				continue;

			if ( node.getNodeName().equals( "jarfile")) {
				String name = XmlTool.get_attribute( node, "name");
				if ( null == name)
					continue;

				File jarfile = new File( name);
				if ( null == jarfile || !jarfile.exists() || !jarfile.isFile())
					continue;

				return true;
			}

			if ( node.getNodeName().equals( "folder")) {
				String name = XmlTool.get_attribute( node, "name");
				if ( null == name)
					continue;

				File folder = new File( name);
				if ( null == folder || !folder.exists() || !folder.isDirectory())
					continue;

				if ( contains( node))
					return true;

				continue;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.tree.StandardTree#on_mouse_left_double_click(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_double_click(MouseEvent mouseEvent) {
		TreePath treePath = getPathForLocation( mouseEvent.getX(), mouseEvent.getY());
		if ( null == treePath)
			return;

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		Object object = defaultMutableTreeNode.getUserObject();
		if ( null == object || !( object instanceof Node))
			return;

		if ( defaultMutableTreeNode.isRoot()) {
			if ( isExpanded( treePath))
				collapsePath( treePath);

			return;
		}

		Node node = ( Node)object;
		if ( node.getNodeName().equals( "module")
			|| node.getNodeName().equals( "folder")
			|| node.getNodeName().equals( "jarfile")
			|| node.getNodeName().equals( "class")) {
			_classTreeCallback.selected( node);
			expandPath( treePath);
		}
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
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
		Transferable transferable = new DomNodeTransferable( _draggedTreeNode);
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
		arg0.rejectDrag();
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent arg0) {
		arg0.rejectDrop();
		_dropTargetTreeNode = null;
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent arg0) {
		_dropTargetTreeNode = null;
		repaint();
	}

	/**
	 * @param node
	 * @return
	 */
	public Module get_module(Node node) {
		if ( null == node)
			return null;

		if ( !node.getNodeName().equals( "module"))
			return get_module( node.getParentNode());
		else {
			String name = XmlTool.get_attribute( node, "name");
			if ( null == name)
				return null;

			if ( name.equals( Constant._noDefinedModule))
				return null;

			return Module.get_module( new File( name));
		}
	}
}
