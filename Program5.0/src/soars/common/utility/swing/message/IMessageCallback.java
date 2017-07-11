/**
 * 
 */
package soars.common.utility.swing.message;

/**
 * @author kurata
 *
 */
public interface IMessageCallback {

	/**
	 * @param id
	 * @param objects
	 * @param messageDlg
	 */
	boolean message_callback(String id, Object[] objects, MessageDlg messageDlg);
}
