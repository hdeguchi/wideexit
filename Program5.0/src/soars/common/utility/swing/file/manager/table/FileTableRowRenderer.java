/**
 * 
 */
package soars.common.utility.swing.file.manager.table;

import java.awt.Component;
import java.io.File;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

import soars.common.utility.swing.file.manager.ResourceManager;

/**
 * @author kurata
 *
 */
public class FileTableRowRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private final Border _emptyBorder = BorderFactory.createEmptyBorder( 1, 1, 1, 1);

	/**
	 * 
	 */
	public FileTableRowRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {

		super.getTableCellRendererComponent(arg0, arg1, false, false, arg4, arg5);

		if ( 1 == arg0.convertColumnIndexToModel( arg5))
			setHorizontalAlignment( JLabel.RIGHT);
		else
			setHorizontalAlignment( JLabel.LEFT);

		String text = "";
		if ( null != arg1) {
			File file = ( File)arg1;
			switch ( arg0.convertColumnIndexToModel( arg5)) {
				case 1:
					text = ( file.isDirectory() ? "" : ( String.valueOf( ( 0l == file.length()) ? 0l : ( long)Math.ceil( ( double)file.length() / 1000.0d)) + "KB "));
					break;
				case 2:
					if ( file.isDirectory())
						text = ( " " + ResourceManager.get_instance().get( "file.table.kind.directory"));
					else {
						FileTableBase fileTableBase = ( FileTableBase)arg0;
						text = ( " " + ( fileTableBase.get_extension( file) + ResourceManager.get_instance().get( "file.table.kind.file")));
					}
					break;
				case 3:
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy/MM/dd HH:mm", getDefaultLocale());
					text = ( " " + simpleDateFormat.format( new Long( file.lastModified())));
					break;
			}
		}

		setOpaque( true);

		setBorder( _emptyBorder);

		setText( text);

		return this;
	}
}
