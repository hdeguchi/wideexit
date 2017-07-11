/**
 * 
 */
package soars.application.manager.library.main.tab.tab.annotaion.tree;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import soars.application.manager.library.main.tab.tab.annotaion.AnnotationPane;
import soars.common.utility.swing.tree.StandardTree;
import soars.common.utility.xml.dom.DomUtility;
import soars.common.utility.xml.dom.XmlTool;

/**
 * @author kurata
 *
 */
public class ClassTree extends StandardTree {

	/**
	 * 
	 */
	private AnnotationPane _annotationPane = null;

	/**
	 * @param annotationPane
	 * @param owner
	 * @param parent
	 */
	public ClassTree(AnnotationPane annotationPane, Frame owner, Component parent) {
		super(owner, parent);
		_annotationPane = annotationPane;
	}

	/**
	 * @return
	 */
	public boolean setup() {
		if (!super.setup(false))
			return false;

		getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION);

		setCellRenderer( new ClassTreeCellRenderer());

		setToolTipText( "ClassTree");

		clear();

		addTreeSelectionListener( new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				on_valueChanged( e);
			}
		});

		return true;
	}

	/**
	 * @param treeSelectionEvent
	 */
	protected void on_valueChanged(TreeSelectionEvent treeSelectionEvent) {
		TreePath treePath = treeSelectionEvent.getOldLeadSelectionPath();
		if ( null != treePath) {
			DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
			Object object = defaultMutableTreeNode.getUserObject();
			if ( null != object && object instanceof AnnotationData)
				_annotationPane.writeTo( ( AnnotationData)object);
		}

		treePath = treeSelectionEvent.getPath();
		if ( null != treePath) {
			DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
			Object object = defaultMutableTreeNode.getUserObject();
			if ( null != object && object instanceof AnnotationData)
				_annotationPane.readFrom( ( AnnotationData)object);
		}
	}

	/**
	 * @param annotationFile
	 * @return
	 */
	public boolean writeTo(File annotationFile) {
		if ( null == annotationFile)
			return false;

		// 現在の状態をこのTreeへ取得した後ファイルannotationFileへ保存
		TreePath treePath = getSelectionPath();
		if ( null == treePath)
			return false;

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		Object object = defaultMutableTreeNode.getUserObject();
		if ( null != object && object instanceof AnnotationData)
			_annotationPane.writeTo( ( AnnotationData)object);

		return saveTo( annotationFile);
	}

	/**
	 * @param annotationFile
	 */
	private boolean saveTo(File annotationFile) {
		Document document = XmlTool.create_document( null, "jarfile_properties", null);
		if ( null == document)
			return false;

		Element rootElement = document.getDocumentElement();
		if ( null == rootElement)
			return false;

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode rootTreeNode = ( DefaultMutableTreeNode)defaultTreeModel.getRoot();

		Object object = rootTreeNode.getUserObject();
		if ( null != object && object instanceof AnnotationData) {
			AnnotationData annotationData = ( AnnotationData)object;
			if ( !annotationData.writeTo( document, rootElement))
				return false;
		}

		if ( !writeTo( document, rootElement, rootTreeNode))
			return false;

		return DomUtility.write( document, annotationFile, "UTF-8", null);
	}

	/**
	 * @param document
	 * @param parentNode
	 * @param parentTreeNode
	 * @return
	 */
	private boolean writeTo(Document document, Node parentNode, DefaultMutableTreeNode parentTreeNode) {
		for ( int i = 0; i < parentTreeNode.getChildCount(); ++i) {
			DefaultMutableTreeNode childTreeNode = ( DefaultMutableTreeNode)parentTreeNode.getChildAt( i);
			Object object = childTreeNode.getUserObject();
			if ( null == object || !( object instanceof AnnotationData))
				return false;

			AnnotationData annotationData = ( AnnotationData)object;
			Element childNode = XmlTool.create_and_append_node( annotationData._kind, document, ( Element)parentNode);
			if ( !annotationData.writeTo( document, childNode))
				return false;

			if ( !writeTo( document, childNode, childTreeNode))
				return false;
		}

		return true;
	}

	/**
	 * @param document
	 * @param file
	 * @return
	 */
	public boolean update(Document document, File file) {
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode rootTreeNode = new DefaultMutableTreeNode();
		defaultTreeModel.setRoot( rootTreeNode);

		Element rootElement = document.getDocumentElement();
		if ( null == rootElement)
			return false;

		if ( !make( rootElement, rootTreeNode, file))
			return false;

		expandPath( new TreePath( rootTreeNode.getPath()));

		setSelectionPath( new TreePath( rootTreeNode.getPath()));

		return true;
	}

	/**
	 * @param parentNode
	 * @param parentTreeNode
	 * @param file
	 * @return
	 */
	private boolean make(Node parentNode, DefaultMutableTreeNode parentTreeNode, File file) {
		AnnotationData annotationData = new AnnotationData( parentNode);
		if ( parentTreeNode.isRoot())
			annotationData._name = file.getName();

		parentTreeNode.setUserObject( annotationData);

		NodeList nodeList = parentNode.getChildNodes();
		for ( int i = 0; i < nodeList.getLength(); ++i) {
			Node childNode = nodeList.item( i);
			if ( null == childNode)
				continue;

			if ( childNode.getNodeName().equals( "en"))
				annotationData._enComment = XmlTool.get_node_value( childNode);
			else if ( childNode.getNodeName().equals( "ja"))
				annotationData._jaComment = XmlTool.get_node_value( childNode);
			else {
				DefaultMutableTreeNode childTreeNode = new DefaultMutableTreeNode();
				parentTreeNode.add( childTreeNode);
				if ( !make( childNode, childTreeNode, file))
					return false;
			}
		}
		return true;
	}

	/**
	 * 
	 */
	public void clear() {
		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode( null);
		defaultTreeModel.setRoot( root);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTree#getToolTipText(java.awt.event.MouseEvent)
	 */
	public String getToolTipText(MouseEvent event) {
		TreePath treePath = getPathForLocation( event.getX(), event.getY());
		if ( null == treePath)
			return null;

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		Object object = defaultMutableTreeNode.getUserObject();
		if ( null == object || !( object instanceof AnnotationData))
			return null;

		AnnotationData annotationData = ( AnnotationData)object;
		if ( annotationData._kind.equals( "jarfile_properties"))
			return null;
		else if ( annotationData._kind.equals( "class"))
			return annotationData._name;
		else if ( annotationData._kind.equals( "method"))
			return null;
		else if ( annotationData._kind.equals( "return"))
			return ( /*annotationData._kind + " : " +*/ annotationData._type);
		else if ( annotationData._kind.startsWith( "parameter"))
			return(  /*annotationData._kind + " : " +*/ annotationData._type);

		return null;
	}
}
