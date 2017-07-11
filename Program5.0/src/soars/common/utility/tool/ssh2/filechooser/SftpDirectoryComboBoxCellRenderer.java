/**
 * 
 */
package soars.common.utility.tool.ssh2.filechooser;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * @author kurata
 *
 */
public class SftpDirectoryComboBoxCellRenderer extends JLabel implements ListCellRenderer {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private ImageIcon _openedDirectoryImageIcon = null;

	/**
	 * 
	 */
	public SftpDirectoryComboBoxCellRenderer() {
		super();
		synchronized( _lock) {
			if ( null == _openedDirectoryImageIcon)
				_openedDirectoryImageIcon = new ImageIcon( SftpDirectoryComboBoxCellRenderer.class.getResource(
					"/soars/common/utility/tool/ssh/filechooser/resource/image/icon/opened_directory.png"));
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3, boolean arg4) {
		if ( null == arg1) {
			setText( "");
			return this;
		}

		setOpaque( true);

		if ( null != _openedDirectoryImageIcon)
			setIcon( _openedDirectoryImageIcon);

		setForeground( arg3 ? arg0.getSelectionForeground() : arg0.getForeground());
		setBackground( arg3 ? arg0.getSelectionBackground() : arg0.getBackground());
		setText( ( String)arg1);
		return this;
	}
}
