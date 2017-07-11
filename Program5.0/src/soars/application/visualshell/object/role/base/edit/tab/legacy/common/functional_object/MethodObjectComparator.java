/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.common.functional_object;

import java.util.Comparator;

/**
 * @author kurata
 *
 */
public class MethodObjectComparator implements Comparator {

	/**
	 * 
	 */
	public MethodObjectComparator() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Object arg0, Object arg1) {
		MethodObject methodObject0 = ( MethodObject)arg0;
		MethodObject methodObject1 = ( MethodObject)arg1;
		return methodObject0._name.compareTo( methodObject1._name);
	}
}
