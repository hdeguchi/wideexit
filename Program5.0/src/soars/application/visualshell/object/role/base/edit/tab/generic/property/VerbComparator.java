/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.property;

import java.util.Comparator;

/**
 * @author kurata
 *
 */
public class VerbComparator implements Comparator<Verb> {

	/**
	 * 
	 */
	public VerbComparator() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Verb arg0, Verb arg1) {
		if ( arg0._index > arg1._index)
			return 1;
		else if ( arg0._index == arg1._index)
			return 0;
		else
			return -1;
	}
}
