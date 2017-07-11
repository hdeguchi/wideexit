/**
 * 
 */
package soars.application.manager.library.main.tab.tab.annotaion.tree;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import soars.application.manager.library.main.Constant;
import soars.application.manager.library.main.ResourceManager;

/**
 * @author kurata
 *
 */
public class ClassTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	protected Icon _rootOpenIcon = null;

	/**
	 * 
	 */
	protected Icon _rootCloseIcon = null;

	/**
	 * 
	 */
	protected Icon _classOpenIcon = null;

	/**
	 * 
	 */
	protected Icon _classCloseIcon = null;

	/**
	 * 
	 */
	protected Icon _methodOpenIcon = null;

	/**
	 * 
	 */
	protected Icon _methodCloseIcon = null;

	/**
	 * 
	 */
	protected Icon _returnIcon = null;

	/**
	 * 
	 */
	protected Icon _parameterIcon = null;

	/**
	 * 
	 */
	public ClassTreeCellRenderer() {
		super();
		_rootOpenIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/directory_open.png"));
		_rootCloseIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/directory_close.png"));
		_classOpenIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/class_open.png"));
		_classCloseIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/class_close.png"));
		_methodOpenIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/method_open.png"));
		_methodCloseIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/method_close.png"));
		_returnIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/return.png"));
		_parameterIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/parameter.png"));
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	public Component getTreeCellRendererComponent(JTree arg0, Object arg1, boolean arg2, boolean arg3, boolean arg4, int arg5, boolean arg6) {

		super.getTreeCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5, arg6);

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)arg1;
		Object object = defaultMutableTreeNode.getUserObject();
		if ( null == object) {
			setText( defaultMutableTreeNode.isRoot() ? "" : "unknown");
			setIcon( getClosedIcon());
		} else {
			if ( !( object instanceof AnnotationData))
				setText( "unknown");
			else {
				AnnotationData annotationData = ( AnnotationData)object;
				if ( annotationData._kind.equals( "jarfile_properties")) {
					setText( annotationData._name);
					setIcon( arg3 ? _rootOpenIcon : _rootCloseIcon);
					//setIcon( arg3 ? getOpenIcon() : getClosedIcon());
				} else if ( annotationData._kind.equals( "class")) {
					setText( get_name( annotationData._name));
					setIcon( arg3 ? _classOpenIcon : _classCloseIcon);
					//setIcon( arg3 ? getOpenIcon() : getClosedIcon());
				} else if ( annotationData._kind.equals( "method")) {
					setText( get_name( annotationData._name));
					setIcon( arg3 ? _methodOpenIcon : _methodCloseIcon);
					//setIcon( arg3 ? getOpenIcon() : getClosedIcon());
				} else if ( annotationData._kind.equals( "return")) {
					setText( get_prefix( annotationData._kind) + get_name( annotationData._type));
					setIcon( _returnIcon);
				} else if ( annotationData._kind.startsWith( "parameter")) {
					setText( get_prefix( annotationData._kind) + get_name( annotationData._type));
					setIcon( _parameterIcon);
				} else
					setText( "unknown");
			}
		}

		return this;
	}

	/**
	 * @param value
	 * @return
	 */
	private String get_name(String value) {
		String[] words = value.split( "\\.");
		if ( null == words || 1 > words.length)
			return value;

		return words[ words.length - 1];
	}

	/**
	 * @param kind
	 * @return
	 */
	private String get_prefix(String kind) {
		if ( kind.equals( "return"))
			return ( ResourceManager.get_instance().get( "annotation.type.return") + " : ");
		else if ( kind.startsWith( "parameter"))
			return ( ResourceManager.get_instance().get( "annotation.type.parameter") + String.valueOf( Integer.parseInt( kind.substring( "parameter".length())) + 1) + " : ");
		return "";
	}
}
