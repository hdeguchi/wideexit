/**
 * 
 */
package soars.common.utility.tool.ssh2.filechooser;

import java.util.Comparator;

import com.sshtools.j2ssh.sftp.SftpFile;

/**
 * @author kurata
 *
 */
public class SftpFilenameComparer implements Comparator {

	/**
	 * 
	 */
	public SftpFilenameComparer() {
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {
		SftpFile sftpFile0 = ( SftpFile)arg0;
		SftpFile sftpFile1 = ( SftpFile)arg1;
		return sftpFile0.getFilename().compareTo( sftpFile1.getFilename());
	}
}
