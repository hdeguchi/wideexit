/*
 * Created on 2006/06/06
 */
package soars.application.visualshell.plugin;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.window.Frame;
import soars.common.utility.tool.sort.QuickSort;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;

/**
 * @author kurata
 */
public class PluginManager extends ArrayList<Plugin> {


	/**
	 * 
	 */
	static private Object _lock = new Object();


	/**
	 * 
	 */
	static private PluginManager _pluginManager = null;


	/**
	 * 
	 */
	static public URLClassLoader _urlClassLoader = null;


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
			if ( null == _pluginManager) {
				_pluginManager = new PluginManager();
				_pluginManager.read();
			}
		}
	}

	/**
	 * @return
	 */
	public static PluginManager get_instance() {
		if ( null == _pluginManager) {
			System.exit( 0);
		}

		return _pluginManager;
	}

	/**
	 * 
	 */
	public PluginManager() {
		super();
	}

	/**
	 * @return
	 */
	private List<String> get_enable_plugin_names() {
		String enablePlugins = Environment.get_instance().get( Environment._enablePluginsKey, "");

		if ( enablePlugins.equals( ""))
			return new ArrayList();

		String[] enablePluginNames = enablePlugins.split( ";");
		if ( null == enablePluginNames || 0 == enablePluginNames.length)
			return new ArrayList<String>();

		return new ArrayList<String>( Arrays.asList( enablePluginNames));
	}

	/**
	 * 
	 */
	private void read() {
		List<String> enablePluginNames = get_enable_plugin_names();

		File directory = new File( Constant._pluginRelativePath);
		if ( !directory.exists()) {
			Environment.get_instance().set( Environment._enablePluginsKey, "");
			return;
		}

		File[] files = directory.listFiles( new FileFilter() {
			public boolean accept(File arg0) {
				return arg0.isDirectory();
			}
		});

		List<Plugin> plugins = new ArrayList<Plugin>();
		for ( int i = 0; i < files.length; ++i) {
			directory = files[ i];
			File file = new File( directory, Constant._pluginSpringFilename);
			if ( !file.isFile())
				continue;

			if ( !file.canRead())
				continue;

			Resource resource = new FileSystemResource( file);
			XmlBeanFactory xmlBeanFactory = new XmlBeanFactory( resource);
			Plugin plugin = ( Plugin)xmlBeanFactory.getBean( Constant._pluginSpringID, Plugin.class);
			if ( !plugin.is_correct())
				continue;

			plugin._enable = enablePluginNames.contains( plugin.getName());

			plugin._directory = ( Constant._pluginRelativePath + "/" + directory.getName());

			plugins.add( plugin);
		}

		if ( plugins.isEmpty()) {
			Environment.get_instance().set( Environment._enablePluginsKey, "");
			return;
		}

		append( plugins);

		reset_enable_plugin_names();

		setup_urlClassLoader();
	}

	/**
	 * @param plugins
	 */
	private void append(List<Plugin> plugins) {
		if ( 1 == plugins.size()) {
			add( plugins.get( 0));
			return;
		}

		Plugin[] array = plugins.toArray( new Plugin[ 0]);
		QuickSort.sort( array, new PluginComparator());
		addAll( new ArrayList<Plugin>( Arrays.asList( array)));
	}

	/**
	 * 
	 */
	private void reset_enable_plugin_names() {
		String enablePlugins = "";
		for ( Plugin plugin:this) {
			if ( !plugin._enable)
				continue;

			enablePlugins += ( ( enablePlugins.equals( "") ? "" : ";") + plugin.getName());
		}
		Environment.get_instance().set( Environment._enablePluginsKey, enablePlugins);
	}

	/**
	 * 
	 */
	public void setup_urlClassLoader() {
		List<URL> urlList = new ArrayList<URL>();
		get_file_module_names( urlList);
		get_url_module_names( urlList);
		if ( urlList.isEmpty())
			return;

		URL[] urls = urlList.toArray( new URL[ 0]);
		_urlClassLoader = new URLClassLoader( urls);
	}

	/**
	 * @param urlList
	 */
	public void get_file_module_names(List<URL> urlList) {
		for ( Plugin plugin:this) {
			if ( !plugin._enable)
				continue;

			plugin.get_file_modules( urlList);
		}
	}

	/**
	 * @param urlList
	 */
	public void get_url_module_names(List<URL> urlList) {
		for ( Plugin plugin:this) {
			if ( !plugin._enable)
				continue;

			plugin.get_url_modules( urlList);
		}
	}

	/**
	 * @param userInterface
	 * @param message_label
	 * @param frame
	 * @param menuTextMap
	 * @return
	 */
	public boolean setup_menu(UserInterface userInterface, JLabel message_label, Frame frame, Map<String, Boolean> menuTextMap) {
		for ( Plugin plugin:this) {
			if ( !plugin._enable)
				continue;

			if ( !plugin.setup_menu( userInterface, message_label, frame, menuTextMap))
				return false;
		}
		return true;
	}

	/**
	 * @param name
	 * @param frame
	 */
	public void execute(String name, Frame frame) {
		for ( Plugin plugin:this) {
			if ( !plugin.getName().equals( name))
				continue;

			plugin.on_selected( frame, null);
		}
	}
}
