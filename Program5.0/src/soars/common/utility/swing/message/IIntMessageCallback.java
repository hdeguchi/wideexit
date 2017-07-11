/**
 * 
 */
package soars.common.utility.swing.message;

/**
 * @author kurata
 *
 */
public interface IIntMessageCallback {

	/**
	 * @param id
	 * @param objects
	 * @param intMessageDlg
	 * @return
	 */
	int int_message_callback(String id, Object[] objects, IntMessageDlg intMessageDlg);
}
