/*
 * Created on 2006/06/22
 */
package soars.application.visualshell.plugin;

import java.util.Comparator;

/**
 * @author kurata
 */
public class PluginComparator implements Comparator {

	/**
	 * 
	 */
	public PluginComparator() {
		super();
	}

	/* (Non Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {
		Plugin plugin0 = ( Plugin)arg0;
		Plugin plugin1 = ( Plugin)arg1;
		return plugin0.getName().compareTo( plugin1.getName());
	}

	/* (Non Javadoc)
	 * @see java.util.Comparator#equals(java.lang.Object, java.lang.Object)
	 */
	public final boolean equals(Object a, Object b) {
		return ( a == b);
	}
}
