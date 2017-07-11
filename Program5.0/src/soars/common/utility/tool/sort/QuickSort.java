/*
 * Created on 2006/06/27
 */
package soars.common.utility.tool.sort;

import java.util.Comparator;

/*
 * Source code for Hoare's QuickSort
 * 
 * copyright (c) 1996-2004
 * 
 *   Roedy Green
 *   Canadian Mind Products
 *   #327 - 964 Heywood Avenue
 *   Victoria, BC Canada V8V 2Y5
 *   tel: (250) 361-9093
 *   mailto:roedyg@mindprod.com
 *   http://mindprod.com
 * 
 * May be freely distributed for any purpose but military.
 * 
 * Version 1.0
 *         1.1 1998 November 10 - add name and address.
 *         1.2 1998 December 28 - JDK 1.2 style Comparator
 *         1.3 2002 February 19 - java.util.Comparator by default.
 *         1.4 2002 March 30 - tidy code.
 *         1.5 2003 May 30 - add dummy private constructor
 * 
 * Eric van Bezooijen <eric@logrus.berkeley.edu> was the
 * original author of this version of QuickSort.  I modified
 * the version he posted to comp.lang.java to use a callback
 * delegate object. I also made a few optimisations.
 * 
 * I have also posted source for HeapSort and RadixSort both
 * of which run faster than QuickSort.
*/

/**
 * @author Eric van Bezooijen <eric@logrus.berkeley.edu>
 * QuickSort for objects
 * modifed by Roedy Green <roedyg@mindprod.com>
 * to a use a Comparator callback delegate
 */
public class QuickSort {

	/**
	 * dummy constructor to prevent its use.
	 * Use static method sort.
	 */
	private QuickSort() {
	}

	private static final String EmbeddedCopyright =
		"copyright (c) 1996-2004 Roedy Green, Canadian Mind Products, http://mindprod.com";

	private static final boolean DEBUGGING  = false;

	// callback object we are passed that has
	// a Comparator(Object a, Object b) method.
	private Comparator comparator;

	// pointer to the array of user's objects we are sorting
	private Object [] userArray;

	/**
	 * sort the user's array
	 * 
	 * @param userArray  Array of Objects to be sorted.
	 * @param comparator Comparator delegate that can compare two
	 *                    Objects and tell which should come first.
	 */
	public static void sort(Object[] userArray, Comparator<Object> comparator) {
		QuickSort h = new QuickSort();
		h.comparator = comparator;
		h.userArray = userArray;
		if ( h.isAlreadySorted())
			return;

		h.quicksort( 0, userArray.length-1 );
		if ( h.isAlreadySorted())
			return;

		if ( DEBUGGING) {
			// debug ensure sort is working
			if ( !h.isAlreadySorted())
				System.out.println( "Sort failed");
		}
		return;
	}

	/**
	 * recursive quicksort that breaks array up into sub
	 * arrays and sorts each of them.
	 * 
	 * @param p
	 * @param r
	 */
	private void quicksort(int p, int r) {
		if ( p < r) {
			int q = partition( p, r);
			if ( q == r) {
				q--;
			}
			quicksort( p, q);
			quicksort( q + 1, r);
		}
	}

	/**
	 * Partition by splitting this chunk to sort in two and
	 * get all big elements on one side of the pivot and all
	 * the small elements on the other.
	 * 
	 * @param to
	 * @param hi
	 * @return
	 */
	private int partition ( int lo, int hi) {
		Object pivot = userArray[lo];
		while ( true) {
			while ( comparator.compare( userArray[ hi], pivot) >= 0 && lo < hi) {
				hi--;
			}
			while ( comparator.compare( userArray[ lo], pivot) < 0 && lo < hi) {
				lo++;
			}
			if ( lo < hi) {
				// exchange objects on either side of the pivot
				Object T = userArray[ lo];
				userArray[ lo] = userArray[ hi];
				userArray[ hi] = T;
			} else
				return hi;
		}
	}

	/**
	 * check if user's array is already sorted
	 * 
	 * @return
	 */
	private boolean isAlreadySorted() {
		for ( int i = 1; i < userArray.length; i++) {
			if ( comparator.compare( userArray[ i],userArray[ i - 1]) < 0)
				return false;
		}
		return true;
	}
}
