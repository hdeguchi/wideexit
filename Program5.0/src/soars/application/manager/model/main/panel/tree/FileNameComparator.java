/**
 * 
 */
package soars.application.manager.model.main.panel.tree;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import soars.application.manager.model.main.ResourceManager;
import soars.application.manager.model.main.panel.tree.property.NodeProperty;
import soars.common.utility.tool.sort.StringNumberComparator;

/**
 * @author kurata
 *
 */
public class FileNameComparator implements Comparator<File> {

	/**
	 * 
	 */
	private Map<File, NodeProperty> _nodePropertyMap = new HashMap<File, NodeProperty>();

	/**
	 * 
	 */
	private boolean _ascend = true;

	/**
	 * 
	 */
	private boolean _toLower = true;

	/**
	 * @param nodePropertyMap
	 * @param ascend
	 * @param toLower
	 */
	public FileNameComparator(Map<File, NodeProperty> nodePropertyMap, boolean ascend, boolean toLower) {
		super();
		_nodePropertyMap = nodePropertyMap;
		_ascend = ascend;
		_toLower = toLower;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(File file0, File file1) {
		return compare( file0, file1, _nodePropertyMap, _ascend, _toLower);
	}

	/**
	 * @param file0
	 * @param file1
	 * @param nodePropertyMap
	 * @param ascend
	 * @param toLower
	 * @return
	 */
	static public int compare(File file0, File file1, Map<File, NodeProperty> nodePropertyMap, boolean ascend, boolean toLower) {
//		if ( file0.isDirectory() && file1.isDirectory()) {
			if ( toLower) {
				if ( ascend)
					return StringNumberComparator.compareTo( file0.getName().toLowerCase(), file1.getName().toLowerCase());
				else
					return StringNumberComparator.compareTo( file1.getName().toLowerCase(), file0.getName().toLowerCase());
			} else {
				if ( ascend)
					return StringNumberComparator.compareTo( file0.getName(), file1.getName());
				else
					return StringNumberComparator.compareTo( file1.getName(), file0.getName());
			}
//		} else if ( file0.isFile() && file1.isFile()) {
//			String title0 = get_title( file0, nodePropertyMap);
//			String title1 = get_title( file1, nodePropertyMap);
//			if ( toLower) {
//				if ( ascend)
//					return StringNumberComparator.compareTo( title0.toLowerCase(), title1.toLowerCase());
//				else
//					return StringNumberComparator.compareTo( title1.toLowerCase(), title0.toLowerCase());
//			} else {
//				if ( ascend)
//					return StringNumberComparator.compareTo( title0, title1);
//				else
//					return StringNumberComparator.compareTo( title1, title0);
//			}
//		} else
//			return ( file0.isDirectory() ? -1 : 1);
	}

	/**
	 * @param file
	 * @param nodePropertyMap
	 * @return
	 */
	static private String get_title(File file, Map<File, NodeProperty> nodePropertyMap) {
		NodeProperty nodeProperty = nodePropertyMap.get( file);
		if ( null == nodeProperty || null == nodeProperty._title)
			return ResourceManager.get_instance().get( "model.tree.no.title");

		return nodeProperty._title;
	}
}
