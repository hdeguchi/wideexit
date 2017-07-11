/**
 * 
 */
package soars.common.utility.tool.http;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * @author kurata
 *
 */
public class HttpAuthenticator extends Authenticator {

	/**
	 * 
	 */
	private String _username;

	/**
	 * 
	 */
	private String _password;

	/**
	 * 
	 */
	public HttpAuthenticator(String username, String password){
		_username = username;
		_password = password;
	}

	/**
	 * 
	 */
	protected PasswordAuthentication getPasswordAuthentication(){
		return new PasswordAuthentication( _username, _password.toCharArray());
	}

	/**
	 * 
	 */
	public String myGetRequestingPrompt(){
		return super.getRequestingPrompt();
	}
}
