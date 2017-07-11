/**
 * 
 */
package soars.application.visualshell.file.docker;

import java.awt.Component;

import javax.swing.JOptionPane;

import soars.application.visualshell.main.ResourceManager;

/**
 * @author kurata
 *
 */
public class DockerFilesetProperty {

	/**
	 * 
	 */
	public boolean _specificDockerImageName;

	/**
	 * 
	 */
	public String _dockerImageName;

	/**
	 * 
	 */
	public boolean _specificUser;

	/**
	 * 
	 */
	public String _userId;

	/**
	 * 
	 */
	public String _username; 

	/**
	 * 
	 */
	public String _password;

	/**
	 * @param specificDockerImageName 
	 * @param dockerImageName 
	 * @param specificUser 
	 * @param userId 
	 * @param username 
	 * @param password 
	 */
	public DockerFilesetProperty(boolean specificDockerImageName, String dockerImageName, boolean specificUser, String userId, String username, String password) {
		_specificDockerImageName = specificDockerImageName;
		_dockerImageName = dockerImageName;
		_specificUser = specificUser;
		_userId = userId;
		_username = username; 
		_password = password;
	}

	/**
	 * @param component
	 * @return
	 */
	public boolean check(Component component) {
		if ( _specificDockerImageName) {
			// TODO ドッカーイメージ名長及びドッカーイメージ名文字列についての制限を調べる必要がある！
			if ( _dockerImageName.equals( "")
				|| 32 <= _dockerImageName.length()) {
				JOptionPane.showMessageDialog( component,
					ResourceManager.get_instance().get( "create.docker.fileset.error.invalid.docker.image.name.length.message"),
					ResourceManager.get_instance().get( "application.title"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
			if ( !_dockerImageName.matches( "[a-z_][a-z0-9_]{0,30}")) {
				JOptionPane.showMessageDialog( component,
					ResourceManager.get_instance().get( "create.docker.fileset.error.invalid.docker.image.name.message"),
					ResourceManager.get_instance().get( "application.title"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		if ( _specificUser) {
			if ( _username.equals( "")
				|| 32 <= _username.length()) {
				JOptionPane.showMessageDialog( component,
					ResourceManager.get_instance().get( "create.docker.fileset.error.invalid.username.length.message"),
					ResourceManager.get_instance().get( "application.title"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
			if ( !_username.matches( "[a-z_][a-z0-9_]{0,30}")) {
				JOptionPane.showMessageDialog( component,
					ResourceManager.get_instance().get( "create.docker.fileset.error.invalid.username.message"),
					ResourceManager.get_instance().get( "application.title"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
			if ( _password.equals( "")
				|| 256 < _password.length()) {
				JOptionPane.showMessageDialog( component,
					ResourceManager.get_instance().get( "create.docker.fileset.error.invalid.password.length.message"),
					ResourceManager.get_instance().get( "application.title"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
			if ( !_password.matches( "[ -~]+")) {
				JOptionPane.showMessageDialog( component,
					ResourceManager.get_instance().get( "create.docker.fileset.error.invalid.password.message"),
					ResourceManager.get_instance().get( "application.title"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return true;
	}
}
