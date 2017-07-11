/**
 * 
 */
package soars.application.visualshell.object.common.arbitrary;

import java.awt.Component;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.w3c.dom.Node;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.common.soars.module.Module;
import soars.common.utility.xml.dom.XmlTool;

/**
 * @author kurata
 *
 */
public class ClassTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	protected Map<String, String> _nameMap = new HashMap<String, String>();

	/**
	 * 
	 */
	protected Icon _directoryOpenIcon = null;

	/**
	 * 
	 */
	protected Icon _directoryCloseIcon = null;

	/**
	 * 
	 */
	protected Icon _moduleDirectoryOpenIcon = null;

	/**
	 * 
	 */
	protected Icon _moduleDirectoryCloseIcon = null;

	/**
	 * 
	 */
	protected Icon _jarFileOpenIcon = null;

	/**
	 * 
	 */
	protected Icon _jarFileCloseIcon = null;

	/**
	 * 
	 */
	protected Icon _fileIcon = null;

	/**
	 * 
	 */
	public ClassTreeCellRenderer() {
		super();
		_directoryOpenIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/tree/directory_open.png"));
		_directoryCloseIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/tree/directory_close.png"));
		_moduleDirectoryOpenIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/tree/module_directory_open.png"));
		_moduleDirectoryCloseIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/tree/module_directory_close.png"));
		_jarFileOpenIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/tree/jar_file_open.png"));
		_jarFileCloseIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/tree/jar_file_close.png"));
		_fileIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/tree/file.png"));
		_nameMap.put( Constant._javaClasses, ResourceManager.get_instance().get( "directory.tree.java.classes.root"));
		_nameMap.put( Constant._functionalObjectDirectories[ 0], ResourceManager.get_instance().get( "directory.tree.system.root"));
		_nameMap.put( Constant._functionalObjectDirectories[ 1], ResourceManager.get_instance().get( "directory.tree.user.root"));
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	public Component getTreeCellRendererComponent(JTree arg0, Object arg1, boolean arg2, boolean arg3, boolean arg4, int arg5, boolean arg6) {

		super.getTreeCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5, arg6);

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)arg1;
		if ( defaultMutableTreeNode.isRoot()) {
			if ( !arg3)
				setIcon( getClosedIcon());

			setText( "");
		} else {
			Object object = defaultMutableTreeNode.getUserObject();
			if ( null == object)
				setText( "unknown");
			else {
				if ( object instanceof String)
					setText( ( String)object);
				else if ( object instanceof Node) {
					Node node = ( Node)object;
					String name = XmlTool.get_attribute( node, "name");
					if ( null == name)
						setText( "unknown");
					else {
						if ( node.getNodeName().equals( "root")) {
							setIcon( arg3 ? _directoryOpenIcon : _directoryCloseIcon);
							String value = _nameMap.get( name);
							setText( ( null != value) ? value : "unknown");
						} else if ( node.getNodeName().equals( "module")) {
							if ( name.equals( Constant._noDefinedModule)) {
								setIcon( arg3 ? _directoryOpenIcon : _directoryCloseIcon);
								setText( ResourceManager.get_instance().get( "no.defined.module"));
							} else {
								setIcon( arg3 ? _moduleDirectoryOpenIcon : _moduleDirectoryCloseIcon);
								Module module = Module.get_module( new File( name));
								setText( ( null != module) ? module.getName() : "unknown");
							}
						} else if ( node.getNodeName().equals( "folder")) {
							setIcon( arg3 ? _directoryOpenIcon : _directoryCloseIcon);
							File file = new File( name);
							setText( ( null != file && file.exists()) ? file.getName() : "unknown");
						} else if ( node.getNodeName().equals( "jarfile")) {
							setIcon( arg3 ? _jarFileOpenIcon : _jarFileCloseIcon);
							File file = new File( name);
							setText( ( null != file && file.exists()) ? file.getName() : "unknown");
						} else if ( node.getNodeName().equals( "class")) {
							setIcon( _fileIcon);
							String[] words = name.split( "\\.");
							if ( null != words && 0 < words.length)
								setText( words[ words.length - 1]);
						}
					}
				}
			}
		}

		return this;
	}
}
