/**
 * 
 */
package soars.application.visualshell.object.arbitrary;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.plugin.Plugin;

/**
 * The Plugin class for the functional object.
 * @author kurata / SOARS project
 */
public class FunctionalObjectPlugin extends Plugin {

	/**
	 * Flag which indicates whether to enable the functional objects.
	 */
	public boolean _originalEnable = Environment._enableFunctionalObjectDefaultValue.equals( "true");

	/**
	 * Creates the instance of this class.
	 */
	public FunctionalObjectPlugin() {
		super();
		_enable = _originalEnable
			= Environment.get_instance().get( Environment._enableFunctionalObjectKey, Environment._enableFunctionalObjectDefaultValue).equals( "true");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.plugin.Plugin#getName()
	 */
	public String getName() {
		return ResourceManager.get_instance().get( "edit.plugin.dialog.plugin.table.functional.object.name");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.plugin.Plugin#getVersion()
	 */
	public String getVersion() {
		return Constant._applicationVersion;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.plugin.Plugin#get_comment()
	 */
	public String get_comment() {
		return ResourceManager.get_instance().get( "edit.plugin.dialog.plugin.table.functional.object.comment");
	}
}
