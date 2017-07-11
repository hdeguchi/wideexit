/**
 * 
 */
package soars.application.manager.library.main.tab.module.table;

import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;

import soars.application.manager.library.main.Constant;
import soars.common.utility.swing.file.manager.table.FileTableSpecificRowRendererBase;

/**
 * @author kurata
 *
 */
public class ModuleFileTableSpecificRowRenderer extends FileTableSpecificRowRendererBase {

	/**
	 * 
	 */
	protected Icon _directoryCloseIcon = null;

	/**
	 * 
	 */
	protected Icon _htmlFileIcon = null;

	/**
	 * @param table
	 */
	public ModuleFileTableSpecificRowRenderer(JTable table) {
		super(table);
		_directoryCloseIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/directory_close.png"));
		_htmlFileIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/html_file.png"));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.table.FileTableSpecificRowRendererBase#on_setIcon(javax.swing.JLabel, java.io.File)
	 */
	protected void on_setIcon(JLabel iconLabel, File file) {
		if ( file.isDirectory())
			iconLabel.setIcon( _directoryCloseIcon);
		else
			iconLabel.setIcon( ( file.getName().endsWith( "html") || file.getName().endsWith( "htm")) ? _htmlFileIcon : _leafIcon);
	}
}
