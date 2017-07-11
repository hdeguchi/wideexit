/**
 * 
 */
package soars.application.visualshell.object.gis;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.plugin.Plugin;

/**
 * @author kurata
 *
 */
public class GisDataPlugin extends Plugin {

	/**
	 * 
	 */
	public boolean _originalEnable = Environment._enableGisDataDefaultValue.equals( "true");

	/**
	 * 
	 */
	public GisDataPlugin() {
		super();
		_enable = _originalEnable
			= Environment.get_instance().get( Environment._enableGisDataKey, Environment._enableGisDataDefaultValue).equals( "true");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.plugin.Plugin#getName()
	 */
	public String getName() {
		return ResourceManager.get_instance().get( "edit.plugin.dialog.plugin.table.gis.data.name");
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
		return ResourceManager.get_instance().get( "edit.plugin.dialog.plugin.table.gis.data.comment");
	}
}
