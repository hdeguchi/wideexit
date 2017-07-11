/**
 * 
 */
package soars.common.utility.swing.file.manager.tree;

import java.io.File;
import java.util.Comparator;

import soars.common.utility.tool.sort.StringNumberComparator;

/**
 * @author kurata
 *
 */
public class DirectoryNameComparator implements Comparator {

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
	public DirectoryNameComparator(boolean ascend, boolean toLower) {
		super();
		_ascend = ascend;
		_toLower = toLower;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {
		File file0 = ( File)arg0;
		File file1 = ( File)arg1;
		if ( _toLower) {
			if ( _ascend)
				return StringNumberComparator.compareTo( file0.getName().toLowerCase(), file1.getName().toLowerCase());
			else
				return StringNumberComparator.compareTo( file1.getName().toLowerCase(), file0.getName().toLowerCase());
		} else {
			if ( _ascend)
				return StringNumberComparator.compareTo( file0.getName(), file1.getName());
			else
				return StringNumberComparator.compareTo( file1.getName(), file0.getName());
		}
	}
}
