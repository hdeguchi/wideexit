/**
 * 
 */
package soars.application.animator.object.chart;

import java.util.Comparator;

/**
 * @author kurata
 *
 */
public class ChartObjectComparator implements Comparator {

	/**
	 * 
	 */
	public ChartObjectComparator() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {
		ChartObject chartObject0 = ( ChartObject)arg0;
		ChartObject chartObject1 = ( ChartObject)arg1;
		return chartObject0._title.compareTo( chartObject1._title);
	}
}
