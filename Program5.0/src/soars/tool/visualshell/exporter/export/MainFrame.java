/**
 * 
 */
package soars.tool.visualshell.exporter.export;

import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.io.File;

import javax.swing.JPanel;

import soars.application.visualshell.executor.common.Parameters;
import soars.application.visualshell.layer.LayerManager;
import soars.common.utility.swing.window.Frame;

/**
 * @author kurata
 *
 */
public class MainFrame extends Frame {

	/**
	 * 
	 */
	static private final String _zipFilename = "parameters.zip";

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private MainFrame _mainFrame = null;

	/**
	 * @return
	 */
	public static MainFrame get_instance() {
		synchronized( _lock) {
			if ( null == _mainFrame) {
				_mainFrame = new MainFrame();
			}
		}
		return _mainFrame;
	}

	/**
	 * @throws HeadlessException
	 */
	public MainFrame() throws HeadlessException {
		super();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		pack();

		return true;
	}

	/**
	 * @param file
	 * @param folder
	 * @return
	 */
	public boolean export(File file, File folder) {
		JPanel view = new JPanel();
		getContentPane().add( view);

		if ( !LayerManager.get_instance().load( file, ( Graphics2D)view.getGraphics(), view))
			return false;

		String script = "";
		if ( file.getName().endsWith( ".soars"))
			script = ( file.getName().substring( 0, file.getName().length() - ".soars".length()) + ".sor");
//		else if ( file.getName().endsWith( ".vsl"))
//			script = ( file.getName().substring( 0, file.getName().length() - ".vsl".length()) + ".sor");
//		else if ( file.getName().endsWith( ".vml"))
//			script = ( file.getName().substring( 0, file.getName().length() - ".vml".length()) + ".sor");
//		else if ( file.getName().endsWith( ".gvml"))
//			script = ( file.getName().substring( 0, file.getName().length() - ".gvml".length()) + ".sor");
		else
			script = ( file.getName() + ".sor");

		if ( !LayerManager.get_instance().export_data_on_demo( new File( folder, script), true, false)) {
			LayerManager.get_instance().cleanup();
			return false;
		}

		File zipFile = new File( folder, _zipFilename);

		String graphicProperties = LayerManager.get_instance().get_graphic_properties();

		String chartProperties = LayerManager.get_instance().get_chart_properties(); 

		Parameters parameters = new Parameters();
		if ( !parameters.compress( zipFile, graphicProperties, chartProperties)) {
			LayerManager.get_instance().cleanup();
			return false;
		}

		LayerManager.get_instance().cleanup();

		return true;
	}
}
