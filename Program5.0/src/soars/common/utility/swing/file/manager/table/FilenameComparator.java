/**
 * 
 */
package soars.common.utility.swing.file.manager.table;

import java.io.File;
import java.util.Comparator;

import soars.common.utility.tool.sort.StringNumberComparator;

/**
 * @author kurata
 *
 */
public class FilenameComparator implements Comparator {

	/**
	 * 
	 */
	private String _extension = "";

	/**
	 * 
	 */
	private boolean _ascend = true;

	/**
	 * 
	 */
	private boolean _toLower = true;

	/**
	 * @param extension 
	 * @param ascend 
	 * @param toLower
	 */
	public FilenameComparator(String extension, boolean ascend, boolean toLower) {
		super();
		_extension = extension;
		_ascend = ascend;
		_toLower = toLower;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {
		File file0 = ( File)arg0;
		File file1 = ( File)arg1;
		String[] filenames = _extension.equals( "")
			? new String[] { file0.getName(), file1.getName()}
			: new String[] { file0.getName().substring( 0, file0.getName().length() - _extension.length() - 1), file1.getName().substring( 0, file1.getName().length() - _extension.length() - 1)};
		if ( is_number( filenames[ 0]) && is_number( filenames[ 1])) {
			double d0 = Double.parseDouble( filenames[ 0]);
			double d1 = Double.parseDouble( filenames[ 1]);
			if ( _ascend)
				return compareTo( d0, d1);
			else
				return compareTo( d1, d0);
		} else {
			if ( _toLower) {
				if ( _ascend)
					return StringNumberComparator.compareTo( filenames[ 0].toLowerCase(), filenames[ 1].toLowerCase());
				else
					return StringNumberComparator.compareTo( filenames[ 1].toLowerCase(), filenames[ 0].toLowerCase());
			} else {
				if ( _ascend)
					return StringNumberComparator.compareTo( filenames[ 0], filenames[ 1]);
				else
					return StringNumberComparator.compareTo( filenames[ 1], filenames[ 0]);
			}
		}
	}

	/**
	 * @param value
	 * @return
	 */
	private boolean is_number(String value) {
		try {
			double d = Double.parseDouble( value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param d0
	 * @param d1
	 * @return
	 */
	private int compareTo(double d0, double d1) {
		if ( d0 > d1)
			return 1;
		else if ( d0 == d1)
			return 0;
		else
			return -1;
	}
}
