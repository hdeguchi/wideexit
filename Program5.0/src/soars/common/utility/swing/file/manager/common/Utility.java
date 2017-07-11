/**
 * 
 */
package soars.common.utility.swing.file.manager.common;

import java.awt.Component;
import java.io.File;
import java.nio.IntBuffer;

import soars.common.utility.swing.file.manager.IFileManager;
import soars.common.utility.swing.file.manager.IFileManagerCallBack;
import soars.common.utility.swing.progress.IntProgressDlg;

/**
 * @author kurata
 *
 */
public class Utility {

	/**
	 * @param files
	 * @param directory
	 * @param action
	 * @param auto
	 * @param checkModified
	 * @param fileManager
	 * @param fileManagerCallBack
	 * @param component
	 * @param intProgressDlg
	 * @return
	 */
	public static int paste(File[] files, File directory, int action, boolean auto, boolean checkModified, IFileManager fileManager, IFileManagerCallBack fileManagerCallBack, Component component, IntProgressDlg intProgressDlg) {
		if ( null == files || 0 == files.length)
			return -2;

		if ( !can_paste( files, directory, auto))
			return -1;

		if ( all_the_parents_are_same( files))
			return paste( files, files[ 0].getParentFile(), directory, action, auto, checkModified, fileManager, fileManagerCallBack, component, intProgressDlg);
		else {
			for ( int i = 0; i < files.length; ++i) {
				int result = paste( new File[] { files[ i]}, files[ i].getParentFile(), directory, action, auto, checkModified, fileManager, fileManagerCallBack, component, intProgressDlg);
				if ( 0 >= result)
					return result;
			}
			return 1;
		}
	}

	/**
	 * @param files
	 * @param parentFile
	 * @param directory
	 * @param action
	 * @param auto
	 * @param checkModified
	 * @param fileManager
	 * @param fileManagerCallBack
	 * @param component
	 * @param intProgressDlg
	 * @return
	 */
	private static int paste(File[] files, File parent, File directory, int action, boolean auto, boolean checkModified, IFileManager fileManager, IFileManagerCallBack fileManagerCallBack, Component component, IntProgressDlg intProgressDlg) {
		IntBuffer counter = IntBuffer.allocate( 1);
		counter.put( 0, 0);

		Node node = new Node( parent);
		if ( !node.create( files, counter))
			return -2;

		return node.paste( directory, action, auto, checkModified, fileManager, fileManagerCallBack, component, intProgressDlg, counter.get( 0));
	}

	/**
	 * @param files
	 * @return
	 */
	private static boolean all_the_parents_are_same(File[] files) {
		for ( int i = 1; i < files.length; ++i) {
			if ( !files[ i].getParentFile().equals( files[ 0].getParentFile()))
				return false;
		}
		return true;
	}

	/**
	 * @param files
	 * @param directory
	 * @param auto
	 * @return
	 */
	public static boolean can_paste(File[] files, File directory, boolean auto) {
		for ( int i = 0; i < files.length; ++i) {
			if ( files[ i].equals( directory)
				|| ( !auto && files[ i].getParentFile().equals( directory))
				|| from_parent_to_child( files[ i], directory))
				return false;
		}
		return true;
	}

	/**
	 * @param file
	 * @param directory
	 * @return
	 */
	public static boolean from_parent_to_child(File file, File directory) {
		File parent = directory.getParentFile();
		while ( null != parent) {
			if ( parent.equals( file))
				return true;

			parent = parent.getParentFile();
		}
		return false;
	}
}
