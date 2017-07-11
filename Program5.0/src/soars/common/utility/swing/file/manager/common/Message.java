/**
 * 
 */
package soars.common.utility.swing.file.manager.common;

import java.awt.Component;
import java.awt.dnd.DnDConstants;

import javax.swing.JOptionPane;

import soars.common.utility.swing.file.manager.ResourceManager;

/**
 * @author kurata
 *
 */
public class Message {

	/**
	 * @param component
	 */
	public static void on_error_from_parent_to_child(Component component) {
		JOptionPane.showMessageDialog( component,
			ResourceManager.get_instance().get( "file.manager.error.from.parent.to.child.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param manager
	 */
	public static void on_error_copy(Component component) {
		JOptionPane.showMessageDialog( component,
			ResourceManager.get_instance().get( "file.manager.error.copy.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param manager
	 */
	public static void on_error_move(Component component) {
		JOptionPane.showMessageDialog( component,
			ResourceManager.get_instance().get( "file.manager.error.move.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param component
	 * @param action
	 */
	public static void on_error_copy_or_move(Component component, int action) {
		JOptionPane.showMessageDialog( component,
			( DnDConstants.ACTION_MOVE == action)
				? ResourceManager.get_instance().get( "file.manager.error.move.message")
				: ResourceManager.get_instance().get( "file.manager.error.copy.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param manager
	 */
	public static void on_error_paste(Component component) {
		JOptionPane.showMessageDialog( component,
			ResourceManager.get_instance().get( "file.manager.error.paste.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param manager
	 */
	public static void on_error_remove(Component component) {
		JOptionPane.showMessageDialog( component,
			ResourceManager.get_instance().get( "file.manager.error.remove.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.ERROR_MESSAGE);
	}
}
