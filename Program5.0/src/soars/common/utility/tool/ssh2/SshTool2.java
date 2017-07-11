/**
 * 
 */
package soars.common.utility.tool.ssh2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import soars.common.utility.tool.common.Tool;
import ch.ethz.ssh2.Connection;

import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PublicKeyAuthenticationClient;
import com.sshtools.j2ssh.io.IOStreamConnector;
import com.sshtools.j2ssh.io.IOStreamConnectorState;
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.sftp.FileAttributes;
import com.sshtools.j2ssh.sftp.SftpFile;
import com.sshtools.j2ssh.transport.IgnoreHostKeyVerification;
import com.sshtools.j2ssh.transport.publickey.InvalidSshKeyException;
import com.sshtools.j2ssh.transport.publickey.SshPrivateKeyFile;
import com.sshtools.j2ssh.util.InvalidStateException;

/**
 * @author kurata
 *
 */
public class SshTool2 {

	/**
	 * @param host
	 * @param username
	 * @param keyFile
	 * @return
	 */
	public static SshClient getSshClient(String host, String username, File keyFile) {
		SshClient sshClient = new SshClient();

		try {
			sshClient.connect( host, new IgnoreHostKeyVerification());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		PublicKeyAuthenticationClient publicKeyAuthenticationClient = new PublicKeyAuthenticationClient();
		publicKeyAuthenticationClient.setUsername( username);
		try {
			SshPrivateKeyFile sshPrivateKeyFile = SshPrivateKeyFile.parse( keyFile);
			publicKeyAuthenticationClient.setKey( sshPrivateKeyFile.toPrivateKey( null));
		} catch (InvalidSshKeyException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		try {
			int result = sshClient.authenticate( publicKeyAuthenticationClient);
			if ( AuthenticationProtocolState.COMPLETE != result) {
				sshClient.disconnect();
				return null;
			}
		} catch (IOException e) {
			sshClient.disconnect();
			e.printStackTrace();
			return null;
		}

		return sshClient;
	}

	/**
	 * @param sshClient
	 * @return
	 */
	public static SessionChannelClient getSessionChannelClient(SshClient sshClient) {
		SessionChannelClient sessionChannelClient = null;
		try {
			sessionChannelClient = sshClient.openSessionChannel();
		} catch (IOException e) {
			return null;
		}
		return sessionChannelClient;
	}

	/**
	 * @param sshClient
	 */
	public static void disconnect(SshClient sshClient) {
		try {
			sshClient.disconnect();
		} catch (Throwable ex) {
			return;
		}
	}

	/**
	 * @param sessionChannelClient
	 */
	public static void close(SessionChannelClient sessionChannelClient) {
		try {
			sessionChannelClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param connection
	 */
	public static void close(Connection connection) {
		try {
			connection.close();
		} catch (Throwable ex) {
			return;
		}
	}

	/**
	 * @param sshClient
	 * @return
	 */
	public static SftpClient getSftpClient(SshClient sshClient) {
		SftpClient sftpClient = null;
		try {
			sftpClient = sshClient.openSftpClient();
		} catch (IOException e) {
			return null;
		}
		return sftpClient;
	}

	/**
	 * @param sftpClient
	 */
	public static void close(SftpClient sftpClient) {
		try {
			sftpClient.quit();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}

	/**
	 * @param sftpClient
	 */
	public static void close_and_catch(SftpClient sftpClient) {
		try {
			close( sftpClient);
		} catch (Throwable ex) {
			return;
		}
	}

	/**
	 * @param sftpClient
	 * @param path
	 * @return
	 */
	public static FileAttributes getFileAttributes(SftpClient sftpClient, String path) {
		FileAttributes fileAttributes;
		try {
			fileAttributes = sftpClient.stat( path);
		} catch (IOException e) {
			return null;
		}
		return fileAttributes;
	}

	/**
	 * @param host 
	 * @param username 
	 * @param keyFile 
	 * @param directory
	 * @return
	 */
	public static boolean directory_exists(String host, String username, File keyFile, String directory) {
		SshClient sshClient = getSshClient( host, username, keyFile);
		if ( null == sshClient)
			return false;

		SftpClient sftpClient = getSftpClient( sshClient);
		if ( null == sftpClient) {
			sshClient.disconnect();
			return false;
		}

		boolean result = directory_exists( sftpClient, directory);

		close( sftpClient);
		sshClient.disconnect();

		return result;
	}

	/**
	 * @param sftpClient
	 * @param directory
	 * @return
	 */
	public static boolean directory_exists(SftpClient sftpClient, String directory) {
		FileAttributes fileAttributes = getFileAttributes( sftpClient, directory);
		if ( null == fileAttributes)
			return false;

		return fileAttributes.isDirectory();
	}

	/**
	 * @param host 
	 * @param username 
	 * @param keyFile 
	 * @param file
	 */
	public static boolean file_exists(String host, String username, File keyFile, String file) {
		SshClient sshClient = getSshClient( host, username, keyFile);
		if ( null == sshClient)
			return false;

		SftpClient sftpClient = getSftpClient( sshClient);
		if ( null == sftpClient) {
			sshClient.disconnect();
			return false;
		}

		boolean result = file_exists( sftpClient, file);

		close( sftpClient);
		sshClient.disconnect();

		return result;
	}

	/**
	 * @param sftpClient
	 * @param file
	 */
	public static boolean file_exists(SftpClient sftpClient, String file) {
		FileAttributes fileAttributes = getFileAttributes( sftpClient, file);
		if ( null == fileAttributes)
			return false;

		return fileAttributes.isFile();
	}

	/**
	 * @param host 
	 * @param username 
	 * @param keyFile 
	 * @return
	 */
	public static List ls(String host, String username, File keyFile) {
		SshClient sshClient = getSshClient( host, username, keyFile);
		if ( null == sshClient)
			return null;

		SftpClient sftpClient = getSftpClient( sshClient);
		if ( null == sftpClient) {
			sshClient.disconnect();
			return null;
		}

		List files = ls( sftpClient);

		close( sftpClient);
		sshClient.disconnect();

		return files;
	}

	/**
	 * @param sftpClient
	 * @return
	 */
	public static List ls(SftpClient sftpClient) {
		List files;
		try {
			files = sftpClient.ls();
		} catch (IOException e) {
			return null;
		}
		return files;
	}

	/**
	 * @param host 
	 * @param username 
	 * @param keyFile 
	 * @param directory
	 * @return
	 */
	public static List ls(String host, String username, File keyFile, String directory) {
		SshClient sshClient = getSshClient( host, username, keyFile);
		if ( null == sshClient)
			return null;

		SftpClient sftpClient = getSftpClient( sshClient);
		if ( null == sftpClient) {
			sshClient.disconnect();
			return null;
		}

		List files = ls( sftpClient, directory);

		close( sftpClient);
		sshClient.disconnect();

		return files;
	}

	/**
	 * @param sftpClient
	 * @param directory
	 * @return
	 */
	public static List ls(SftpClient sftpClient, String directory) {
		List files;
		try {
			files = sftpClient.ls( directory);
		} catch (IOException e) {
			return null;
		}
		return files;
	}

	/**
	 * @param host
	 * @param username
	 * @param keyFile
	 * @param path
	 * @return
	 */
	public static boolean mkdirs(String host, String username, File keyFile, String path) {
		SshClient sshClient = getSshClient( host, username, keyFile);
		if ( null == sshClient)
			return false;

		SftpClient sftpClient = getSftpClient( sshClient);
		if ( null == sftpClient) {
			sshClient.disconnect();
			return false;
		}

		sftpClient.mkdirs( path);

		boolean result = directory_exists( sftpClient, path);

		close( sftpClient);
		sshClient.disconnect();

		return result;
	}

	/**
	 * @param host
	 * @param username
	 * @param keyFile
	 * @param oldpath
	 * @param newpath
	 * @return
	 */
	public static boolean rename(String host, String username, File keyFile, String oldpath, String newpath) {
		SshClient sshClient = getSshClient( host, username, keyFile);
		if ( null == sshClient)
			return false;

		SftpClient sftpClient = getSftpClient( sshClient);
		if ( null == sftpClient) {
			sshClient.disconnect();
			return false;
		}

		boolean result = rename( sftpClient, oldpath, newpath);

		close( sftpClient);
		sshClient.disconnect();

		return result;
	}

	/**
	 * @param sftpClient
	 * @param oldpath
	 * @param newpath
	 * @return
	 */
	public static boolean rename(SftpClient sftpClient, String oldpath, String newpath) {
		try {
			sftpClient.rename( oldpath, newpath);
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param host 
	 * @param username 
	 * @param keyFile 
	 * @param path
	 * @param self
	 * @return
	 */
	public static boolean delete(String host, String username, File keyFile, String path, boolean self) {
		SshClient sshClient = getSshClient( host, username, keyFile);
		if ( null == sshClient)
			return false;

		SftpClient sftpClient = getSftpClient( sshClient);
		if ( null == sftpClient) {
			sshClient.disconnect();
			return false;
		}

		boolean result = delete( sftpClient, path, self);

		close( sftpClient);
		sshClient.disconnect();

		return result;
	}

	/**
	 * @param sftpClient
	 * @param path
	 * @param self
	 * @return
	 */
	public static boolean delete(SftpClient sftpClient, String path, boolean self) {
		if ( !delete( sftpClient, path))
			return false;

		if ( self)
			return rm( sftpClient, path);

		return true;
	}

	/**
	 * @param sftpClient
	 * @param path
	 * @return
	 */
	private static boolean delete(SftpClient sftpClient, String path) {
		List files = ls( sftpClient, path);
		if ( null == files)
			return false;

		for ( int i = 0; i < files.size(); ++i) {
			SftpFile sftpFile = ( SftpFile)files.get( i);
			if ( sftpFile.getAbsolutePath().endsWith( "."))
				continue;

			if ( sftpFile.isDirectory()) {
				if ( !delete( sftpClient, sftpFile.getAbsolutePath()))
					return false;
			}

			try {
				sftpFile.delete();
			} catch (IOException e) {
				//e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/**
	 * @param host 
	 * @param username 
	 * @param keyFile 
	 * @param path
	 * @return
	 */
	public static boolean rm(String host, String username, File keyFile, String path) {
		SshClient sshClient = getSshClient( host, username, keyFile);
		if ( null == sshClient)
			return false;

		SftpClient sftpClient = getSftpClient( sshClient);
		if ( null == sftpClient) {
			sshClient.disconnect();
			return false;
		}

		boolean result = rm( sftpClient, path);

		close( sftpClient);
		sshClient.disconnect();

		return result;
	}

	/**
	 * @param sftpClient
	 * @param path
	 * @return
	 */
	public static boolean rm(SftpClient sftpClient, String path) {
		try {
			sftpClient.rm( path);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * @param host 
	 * @param username 
	 * @param keyFile 
	 * @param command
	 * @return
	 */
	public static String execute1(String host, String username, File keyFile, String command) {
		SshClient sshClient = getSshClient( host, username, keyFile);
		if ( null == sshClient)
			return null;

		return execute1( sshClient, command);
	}

	/**
	 * @param sshClient
	 * @param command
	 * @return
	 */
	public static String execute1(SshClient sshClient, String command) {
		SessionChannelClient sessionChannelClient = getSessionChannelClient( sshClient);
		if ( null == sessionChannelClient)
			return null;

		try {
			if ( !sessionChannelClient.executeCommand( command)) {
				close( sessionChannelClient);
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			close( sessionChannelClient);
			return null;
		}

		String result = Tool.read( sessionChannelClient.getInputStream());
		if ( null == result) {
			close( sessionChannelClient);
			return null;
		}

		try {
			String error = Tool.read( sessionChannelClient.getStderrInputStream());
			if ( result.equals( "") && null != error)
				result = error;
		} catch (IOException e) {
			e.printStackTrace();
		}

		close( sessionChannelClient);

		return result;
	}

	/**
	 * @param host 
	 * @param username 
	 * @param keyFile 
	 * @param command
	 * @return
	 */
	public static String execute2(String host, String username, File keyFile, String command) {
		SshClient sshClient = getSshClient( host, username, keyFile);
		if ( null == sshClient)
			return null;

		return execute2( sshClient, command);
	}

	/**
	 * @param sshClient
	 * @param command
	 * @return
	 */
	public static String execute2(SshClient sshClient, String command) {
		SessionChannelClient sessionChannelClient = getSessionChannelClient( sshClient);
		if ( null == sessionChannelClient)
			return null;

		try {
			if ( !sessionChannelClient.requestPseudoTerminal( "vt100", 80, 24, 0, 0, "")) {
				close( sessionChannelClient);
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			close( sessionChannelClient);
			return null;
		}

		try {
			if ( !sessionChannelClient.startShell()) {
				close( sessionChannelClient);
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			close( sessionChannelClient);
			return null;
		}

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		IOStreamConnector output = new IOStreamConnector( sessionChannelClient.getInputStream(), byteArrayOutputStream);

		try {
			sessionChannelClient.getOutputStream().write( command.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			close( sessionChannelClient);
			return null;
		}

		try {
			sessionChannelClient.getOutputStream().write( "exit\n".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			close( sessionChannelClient);
			return null;
		}

		try {
			output.getState().waitForState( IOStreamConnectorState.CLOSED);
		} catch (InvalidStateException e) {
			close( sessionChannelClient);
			e.printStackTrace();
			return null;
		} catch (InterruptedException e) {
			close( sessionChannelClient);
			e.printStackTrace();
			return null;
		}

		try {
			byteArrayOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		close( sessionChannelClient);

		return byteArrayOutputStream.toString();
	}

//	/**
//	 * @param inputStream
//	 * @return
//	 */
//	public static String read(ChannelInputStream inputStream) {
//		String text = "";
//		try {
//			while ( true) {
//				int c = inputStream.read();
//				if ( -1 == c)
//					break;
//
//				text += ( char)c;
//			}
//		} catch (IOException e) {
//			//e.printStackTrace();
//			return null;
//		}
//
//		return text;
//	}

	/**
	 * @param host 
	 * @param username 
	 * @param keyFile 
	 * @param srcDirectory
	 * @param destDirectory
	 * @param inclusiveFilenameRegularExpressions
	 * @param exclusiveFilenameRegularExpressions
	 * @return
	 */
	public static boolean transfer(String host, String username, File keyFile, SftpFile srcDirectory, File destDirectory,
		String[] inclusiveFilenameRegularExpressions, String[] exclusiveFilenameRegularExpressions) {
		SshClient sshClient = getSshClient( host, username, keyFile);
		if ( null == sshClient)
			return false;

		SftpClient sftpClient = getSftpClient( sshClient);
		if ( null == sftpClient) {
			sshClient.disconnect();
			return false;
		}

		boolean result = transfer( sftpClient, srcDirectory, destDirectory,
			inclusiveFilenameRegularExpressions, exclusiveFilenameRegularExpressions);

		close( sftpClient);
		sshClient.disconnect();

		return result;
	}

	/**
	 * @param sftpClient
	 * @param srcDirectory
	 * @param destDirectory
	 * @param inclusiveFilenameRegularExpressions
	 * @param exclusiveFilenameRegularExpressions
	 * @return
	 */
	public static boolean transfer(SftpClient sftpClient, SftpFile srcDirectory, File destDirectory,
		String[] inclusiveFilenameRegularExpressions, String[] exclusiveFilenameRegularExpressions) {
		//System.out.println( srcDirectory.getAbsolutePath() + " -> " + destDirectory.getAbsolutePath());
		if ( !directory_exists( sftpClient, srcDirectory.getAbsolutePath()) || !destDirectory.isDirectory())
			return false;

		List files = ls( sftpClient, srcDirectory.getAbsolutePath());
		if ( null == files)
			return false;

		for ( int i = 0; i < files.size(); ++i) {
			SftpFile sftpFile = ( SftpFile)files.get( i);
			if ( sftpFile.getAbsolutePath().endsWith( "."))
				continue;

			if ( sftpFile.isDirectory()) {
				File newDirectory = new File( destDirectory, sftpFile.getFilename());
				if ( newDirectory.exists()) {
					if ( !newDirectory.isDirectory()) {
						if ( !newDirectory.delete())
							return false;

						if ( !newDirectory.mkdirs())
							return false;
					}
				} else {
					if ( !newDirectory.mkdirs())
						return false;
				}

				if ( !transfer( sftpClient, new SftpFile( srcDirectory.getAbsolutePath() + "/" + sftpFile.getFilename()), newDirectory,
					inclusiveFilenameRegularExpressions, exclusiveFilenameRegularExpressions))
					return false;
			} else if ( sftpFile.isFile()) {
				if ( null != exclusiveFilenameRegularExpressions
					&& matches( sftpFile.getFilename(), exclusiveFilenameRegularExpressions))
					continue;

				if ( null != inclusiveFilenameRegularExpressions
					&& !matches( sftpFile.getFilename(), inclusiveFilenameRegularExpressions))
					continue;

				try {
					sftpClient.get( srcDirectory.getAbsolutePath() + "/" + sftpFile.getFilename(),
						destDirectory.getAbsolutePath() + "/" + sftpFile.getFilename());
				} catch (IOException e) {
					//e.printStackTrace();
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * @param filename
	 * @param filenameRegularExpressions
	 * @return
	 */
	public static boolean matches(String filename, String[] filenameRegularExpressions) {
		for ( int i = 0; i < filenameRegularExpressions.length; ++i) {
			if ( filename.matches( filenameRegularExpressions[ i]))
				return true;
		}
		return false;
	}
}
