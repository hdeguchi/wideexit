/**
 * 
 */
package soars.application.manager.library.main.tab;

import java.awt.Component;
import java.awt.Frame;
import java.io.File;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import soars.application.manager.library.main.Constant;
import soars.application.manager.library.main.ResourceManager;
import soars.application.manager.library.main.tab.directory.NavigatorFileManager;
import soars.application.manager.library.main.tab.module.ModuleFileManager;
import soars.common.soars.environment.BasicEnvironment;

/**
 * @author kurata
 *
 */
public class MainTabbedPane extends JTabbedPane {

	/**
	 * 
	 */
	private ModuleFileManager _moduleFileManager = null;

	/**
	 * 
	 */
	private NavigatorFileManager _navigatorFileManager = null;

	/**
	 * 
	 */
	private Frame _owner = null;

	/**
	 * 
	 */
	private Component _parent = null;

	/**
	 * @param owner
	 * @param parent
	 */
	public MainTabbedPane(Frame owner, Component parent) {
		super();
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @return
	 */
	public boolean setup() {
		if ( !setup_moduleFileManager())
			return false;

		if ( !setup_navigatorFileManager())
			return false;

		add( _moduleFileManager, ResourceManager.get_instance().get( "main.tab.module.explorer"));
		add( _navigatorFileManager, ResourceManager.get_instance().get( "main.tab.module.navigator"));

		addChangeListener( new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Component component = getSelectedComponent();
				if ( component instanceof ModuleFileManager) {
					_navigatorFileManager.store();
					_moduleFileManager.refresh( false);
				} else if ( component instanceof NavigatorFileManager) {
					_moduleFileManager.store();
					_navigatorFileManager.refresh( false);
				}
			}
		});

		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_moduleFileManager() {
		File userModuleDirectory = BasicEnvironment.get_instance().get_projectSubFoler( Constant._userModuleDirectoryName);
		if ( null == userModuleDirectory)
			return false;

		_moduleFileManager = new ModuleFileManager( _owner, _parent);
		return _moduleFileManager.setup( new String[] { Constant._systemModuleDirectory, userModuleDirectory.getAbsolutePath()});
	}

	/**
	 * @return
	 */
	private boolean setup_navigatorFileManager() {
		File userModuleDirectory = BasicEnvironment.get_instance().get_projectSubFoler( Constant._userModuleDirectoryName);
		if ( null == userModuleDirectory)
			return false;

		_navigatorFileManager = new NavigatorFileManager( _owner, _parent);
		return _navigatorFileManager.setup( new File[] { new File( Constant._systemModuleDirectory), userModuleDirectory});
	}

	/**
	 * 
	 */
	public void optimize_divider_location() {
		_moduleFileManager.optimize_divider_location();
		_navigatorFileManager.optimize_divider_location();
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		_moduleFileManager.on_setup_completed();
		_navigatorFileManager.on_setup_completed();
	}

	/**
	 * 
	 */
	public void refresh() {
		_moduleFileManager.refresh( true);
		_navigatorFileManager.refresh( true);
	}

	/**
	 * 
	 */
	public void set_property_to_environment_file() {
		_moduleFileManager.set_property_to_environment_file();
		_navigatorFileManager.set_property_to_environment_file();
	}

	/**
	 * 
	 */
	public void store() {
		Component component = getSelectedComponent();
		if ( component instanceof ModuleFileManager)
			_moduleFileManager.store();
		else if ( component instanceof NavigatorFileManager)
			_navigatorFileManager.store();
	}
}
