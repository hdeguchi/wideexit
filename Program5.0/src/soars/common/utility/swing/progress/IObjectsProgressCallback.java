/**
 * 
 */
package soars.common.utility.swing.progress;

/**
 * @author kurata
 *
 */
public interface IObjectsProgressCallback {

	/**
	 * @param id
	 * @param objects
	 * @param objectsProgressDlg
	 * @return
	 */
	Object[] objects_message_callback(String id, Object[] objects, ObjectsProgressDlg objectsProgressDlg);
}
