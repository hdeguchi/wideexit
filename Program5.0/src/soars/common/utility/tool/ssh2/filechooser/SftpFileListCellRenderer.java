/**
 * 
 */
package soars.common.utility.tool.ssh2.filechooser;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.sshtools.j2ssh.sftp.SftpFile;

/**
 * @author kurata
 *
 */
public class SftpFileListCellRenderer extends JLabel implements ListCellRenderer {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private ImageIcon _directoryImageIcon = null;

	/**
	 * 
	 */
	static private ImageIcon _fileImageIcon = null;

	/**
	 * 
	 */
	public SftpFileListCellRenderer() {
		super();
		synchronized( _lock) {
			if ( null == _directoryImageIcon)
				_directoryImageIcon = new ImageIcon( SftpFileListCellRenderer.class.getResource(
					"/soars/common/utility/tool/ssh/filechooser/resource/image/icon/closed_directory.png"));
			if ( null == _fileImageIcon)
				_fileImageIcon = new ImageIcon( SftpFileListCellRenderer.class.getResource(
					"/soars/common/utility/tool/ssh/filechooser/resource/image/icon/file.png"));
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

		SftpFile sftpFile = ( SftpFile)arg1;

		setOpaque( true);

		if ( sftpFile.isDirectory()) {
			if ( null != _directoryImageIcon)
				setIcon( _directoryImageIcon);
		} else {
			if ( null != _fileImageIcon)
				setIcon( _fileImageIcon);
		}

		setForeground( arg3 ? arg0.getSelectionForeground() : arg0.getForeground());
		setBackground( arg3 ? arg0.getSelectionBackground() : arg0.getBackground());
		setText( sftpFile.getFilename());
		return this;
	}
}
