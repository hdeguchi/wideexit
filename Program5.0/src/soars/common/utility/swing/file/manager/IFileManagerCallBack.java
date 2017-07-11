/**
 * 
 */
package soars.common.utility.swing.file.manager;

import java.io.File;

/**
 * @author kurata
 *
 */
public interface IFileManagerCallBack {

	/**
	 * @param fileManager
	 */
	void modified(IFileManager fileManager);

	/**
	 * @param fileManager
	 */
	void select_changed(IFileManager fileManager);

	/**
	 * @param file
	 * @return
	 */
	boolean can_remove(File file);

	/**
	 * @param srcPath
	 * @param destPath
	 */
	void on_move(File srcPath, File destPath);

	/**
	 * 
	 */
	void on_start_paste();

	/**
	 * 
	 */
	void on_start_paste_and_remove();

	/**
	 * @param file
	 * @return
	 */
	boolean can_copy(File file);

	/**
	 * @param file
	 * @return
	 */
	boolean can_paste(File file);

	/**
	 * 
	 */
	void on_paste_completed();

	/**
	 * 
	 */
	void on_paste_and_remove_completed();

	/**
	 * @param file
	 * @param encoding
	 */
	void on_select(File file, String encoding);

	/**
	 * @param originalFile
	 * @param newFile
	 */
	void on_rename(File originalFile, File newFile);

	/**
	 * 
	 */
	File get_export_directory();

	/**
	 * @param file
	 * @return
	 */
	boolean visible(File file);
}
