/**
 * 
 */
package soars.common.utility.swing.file.manager.tree;

import java.util.Comparator;

import soars.common.utility.tool.sort.StringNumberComparator;

/**
 * @author kurata
 *
 */
public class NodeComparator implements Comparator<Node> {

	/**
	 * 
	 */
	private boolean _ascend = true;

	/**
	 * 
	 */
	private boolean _toLower = true;

	/**
	 * @param ascend 
	 * @param toLower
	 */
	public NodeComparator(boolean ascend, boolean toLower) {
		super();
		_ascend = ascend;
		_toLower = toLower;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Node arg0, Node arg1) {
		if ( _toLower) {
			if ( _ascend)
				return StringNumberComparator.compareTo( arg0._directory.getName().toLowerCase(), arg1._directory.getName().toLowerCase());
			else
				return StringNumberComparator.compareTo( arg1._directory.getName().toLowerCase(), arg0._directory.getName().toLowerCase());
		} else {
			if ( _ascend)
				return StringNumberComparator.compareTo( arg0._directory.getName(), arg1._directory.getName());
			else
				return StringNumberComparator.compareTo( arg1._directory.getName(), arg0._directory.getName());
		}
	}
}
