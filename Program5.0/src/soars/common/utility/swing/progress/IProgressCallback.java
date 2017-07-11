/**
 * 
 */
package soars.common.utility.swing.progress;

/**
 * @author kurata
 *
 */
public interface IProgressCallback {

	/**
	 * @param id
	 * @param objects
	 * @param progressDlg
	 */
	boolean progress_callback(String id, Object[] objects, ProgressDlg progressDlg);
}
