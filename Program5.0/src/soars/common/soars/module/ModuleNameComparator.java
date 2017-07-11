/**
 * 
 */
package soars.common.soars.module;

import java.util.Comparator;

import soars.common.utility.tool.sort.StringNumberComparator;

/**
 * @author kurata
 *
 */
public class ModuleNameComparator implements Comparator<Module> {

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
	public ModuleNameComparator(boolean ascend, boolean toLower) {
		super();
		_ascend = ascend;
		_toLower = toLower;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Module module0, Module module1) {
		if ( _toLower) {
			if ( _ascend)
				return StringNumberComparator.compareTo( module0.getName().toLowerCase(), module1.getName().toLowerCase());
			else
				return StringNumberComparator.compareTo( module1.getName().toLowerCase(), module0.getName().toLowerCase());
		} else {
			if ( _ascend)
				return StringNumberComparator.compareTo( module0.getName(), module1.getName());
			else
				return StringNumberComparator.compareTo( module1.getName(), module0.getName());
		}
	}
}
