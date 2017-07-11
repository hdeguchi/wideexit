/**
 * 
 */
package soars.common.utility.swing.progress;

/**
 * @author kurata
 *
 */
public interface IIntProgressCallback {

	/**
	 * @param id
	 * @param objects
	 * @param intProgressDlg
	 * @return
	 */
	int int_message_callback(String id, Object[] objects, IntProgressDlg intProgressDlg);
}
