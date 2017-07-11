/**
 * 
 */
package soars.application.manager.model.main.panel.tree;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import soars.application.manager.model.main.panel.tree.property.NodeProperty;

/**
 * @author kurata
 *
 */
public class NodeComparator implements Comparator<Node> {

	/**
	 * 
	 */
	private Map<File, NodeProperty> _nodePropertyMap = new HashMap<File, NodeProperty>();

	/**
	 * 
	 */
	private boolean _ascend = true;

	/**
	 * 
	 */
	private boolean _toLower = true;

	/**
	 * @param nodePropertyMap
	 * @param ascend
	 * @param toLower
	 */
	public NodeComparator(Map<File, NodeProperty> nodePropertyMap, boolean ascend, boolean toLower) {
		super();
		_nodePropertyMap = nodePropertyMap;
		_ascend = ascend;
		_toLower = toLower;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Node arg0, Node arg1) {
		return FileNameComparator.compare( arg0._file, arg1._file, _nodePropertyMap, _ascend, _toLower);
	}
}
