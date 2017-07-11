/**
 * 
 */
package soars.application.animator.main.internal;

import java.util.Comparator;

import soars.application.animator.object.chart.AnimatorInternalChartFrame;

/**
 * @author kurata
 *
 */
public class InternalFrameComparator implements Comparator {

	/**
	 * 
	 */
	public InternalFrameComparator() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {
		int order0 = -1;

		if ( arg0 instanceof AnimatorViewFrame)
			order0 = ( ( AnimatorViewFrame)arg0)._order;
		else if ( arg0 instanceof AnimatorInternalChartFrame)
			order0 = ( ( AnimatorInternalChartFrame)arg0)._order;

		int order1 = -1;
		if ( arg1 instanceof AnimatorViewFrame)
			order1 = ( ( AnimatorViewFrame)arg1)._order;
		else if ( arg1 instanceof AnimatorInternalChartFrame)
			order1 = ( ( AnimatorInternalChartFrame)arg1)._order;

		if ( order0 == order1)
			return 0;
		else if ( order0 > order1)
			return -1;
		else
			return 1;
	}
}
