/**
 * 
 */
package soars.application.manager.library.main.tab.common.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soars.common.soars.module.Module;
import soars.common.utility.swing.file.manager.tree.Node;

/**
 * @author kurata
 *
 */
public class NodeData {

	/**
	 * 
	 */
	public List<Node> _nodes = new ArrayList<Node>();

	/**
	 * 
	 */
	public Map<Module, File> _directories = new HashMap<Module, File>();
}
