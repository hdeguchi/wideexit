/**
 * 
 */
package soars.application.visualshell.object.common.arbitrary;

import java.io.File;
import java.util.Comparator;

import org.w3c.dom.Node;

import soars.application.visualshell.main.Constant;
import soars.common.soars.module.Module;
import soars.common.utility.xml.dom.XmlTool;

/**
 * @author kurata
 *
 */
public class NodeComparator implements Comparator<Node> {

	/**
	 * 
	 */
	public NodeComparator() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Node node0, Node node1) {
		if ( node0.getNodeName().equals( "module") && node1.getNodeName().equals( "module")) {
			String name0 = XmlTool.get_attribute( node0, "name");
			if ( null == name0)
				return 0;

			String name1 = XmlTool.get_attribute( node1, "name");
			if ( null == name1)
				return 0;

			if ( name0.equals( Constant._noDefinedModule))
				return 1;

			if ( name1.equals( Constant._noDefinedModule))
				return -1;

			Module module0 = Module.get_module( new File( name0));
			if ( null == module0)
				return 0;

			Module module1 = Module.get_module( new File( name1));
			if ( null == module1)
				return 0;

			return module0.getName().compareTo( module1.getName());
		} else if ( ( node0.getNodeName().equals( "folder") && node1.getNodeName().equals( "folder"))
			|| ( node0.getNodeName().equals( "jarfile") && node1.getNodeName().equals( "jarfile"))) {
			String name0 = XmlTool.get_attribute( node0, "name");
			if ( null == name0)
				return 0;

			String name1 = XmlTool.get_attribute( node1, "name");
			if ( null == name1)
				return 0;

			File file0 = new File( name0);
			if ( null == file0 || !file0.exists())
				return 0;

			File file1 = new File( name1);
			if ( null == file1 || !file1.exists())
				return 0;

			return file0.getName().compareTo( file1.getName());

//			String words0[] = name0.split( "/");
//			if ( null == words0 || 0 == words0.length)
//				return 0;
//
//			String words1[] = name1.split( "/");
//			if ( null == words1 || 0 == words1.length)
//				return 0;
//
//			return words0[ words0.length - 1].compareTo( words1[ words1.length - 1]);
		} else if ( node0.getNodeName().equals( "folder")) {
			return -1;
		} else if ( node1.getNodeName().equals( "folder")) {
			return 1;
		} else if ( node0.getNodeName().equals( "class") && node1.getNodeName().equals( "class")) {
			String name0 = XmlTool.get_attribute( node0, "name");
			if ( null == name0)
				return 0;

			String name1 = XmlTool.get_attribute( node1, "name");
			if ( null == name1)
				return 0;

			String words0[] = name0.split( "\\.");
			if ( null == words0 || 0 == words0.length)
				return 0;

			String words1[] = name1.split( "\\.");
			if ( null == words1 || 0 == words1.length)
				return 0;

			return words0[ words0.length - 1].compareTo( words1[ words1.length - 1]);
		}

		return 0;
	}
}
