/**
 * 
 */
package soars.common.utility.swing.message;

/**
 * @author kurata
 *
 */
public interface IObjectsMessageCallback {

	/**
	 * @param id
	 * @param objects
	 * @param objectsMessageDlg
	 * @return
	 */
	Object[] objects_message_callback(String id, Object[] objects, ObjectsMessageDlg objectsMessageDlg);
}
