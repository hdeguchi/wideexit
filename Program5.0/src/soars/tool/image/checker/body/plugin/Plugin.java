/**
 * 
 */
package soars.tool.image.checker.body.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import soars.tool.image.checker.body.main.Constant;

/**
 * @author kurata
 *
 */
public class Plugin {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private Plugin _plugin = null;

	/**
	 * 
	 */
	private List _extentionList;

	/**
	 * 
	 */
	static {
		startup();
	}

	/**
	 * 
	 */
	private static void startup() {
		synchronized( _lock) {
			if ( null == _plugin) {
				File current_directory = new File( "");
				if ( null == current_directory)
					System.exit( 1);

				File file = new File( current_directory.getAbsoluteFile(), Constant._plugin_spring_filename);
				if ( !file.isFile() || !file.canRead())
					System.exit( 1);

				Resource resource = new FileSystemResource( file);
				XmlBeanFactory xmlBeanFactory = new XmlBeanFactory( resource);
				_plugin = ( Plugin)xmlBeanFactory.getBean( Constant._plugin_spring_id, Plugin.class);
			}
		}
	}

	/**
	 * Returns the instance of the Plugin class.
	 * @return the instance of the Plugin class
	 */
	public static Plugin get_instance() {
		return _plugin;
	}

	/**
	 * @return
	 */
	public List getExtentionList() {
		if ( null == _extentionList)
			_extentionList = new ArrayList();

		return _extentionList;
	}

	/**
	 * @param extentionList
	 */
	public void setExtentionList(List extentionList) {
		_extentionList = extentionList;
	}

	/**
	 * @param file
	 * @return
	 */
	public boolean contains(File file) {
		for ( int i = 0; i < _extentionList.size(); ++i) {
			String extention = ( String)_extentionList.get( i);
			if ( file.getName().toLowerCase().endsWith( extention.toLowerCase()))
				return true;
		}
		return false;
	}
}
