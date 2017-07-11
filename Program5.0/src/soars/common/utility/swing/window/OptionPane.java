/**
 * 
 */
package soars.common.utility.swing.window;

import java.awt.Component;
import java.awt.HeadlessException;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * @author kurata
 *
 */
public class OptionPane {

	/**
	 * @param message
	 * @return
	 */
	public static String showInputDialog(Object message) throws HeadlessException {
		return showInputDialog( null, message);
	}

	/**
	 * @param message
	 * @param initialSelectionValue
	 * @return
	 */
	public static String showInputDialog(Object message, Object initialSelectionValue) {
		return showInputDialog( null, message, initialSelectionValue);
	}

	/**
	 * @param parentComponent
	 * @param message
	 * @return
	 */
	public static String showInputDialog(Component parentComponent, Object message) throws HeadlessException {
		return showInputDialog( parentComponent, message,
			UIManager.getString( "OptionPane.inputDialogTitle",
				( ( null == parentComponent) ? Locale.getDefault() : parentComponent.getLocale())),
			JOptionPane.QUESTION_MESSAGE);
	}

	/**
	 * @param parentComponent
	 * @param message
	 * @param initialSelectionValue
	 * @return
	 */
	public static String showInputDialog(Component parentComponent, Object message, Object initialSelectionValue) {
		return ( String)showInputDialog( parentComponent, message,
			UIManager.getString("OptionPane.inputDialogTitle",
				( ( null == parentComponent) ? Locale.getDefault() : parentComponent.getLocale())),
			JOptionPane.QUESTION_MESSAGE, null, null, initialSelectionValue);
	}

	/**
	 * @param parentComponent
	 * @param message
	 * @param title
	 * @param messageType
	 * @return
	 */
	public static String showInputDialog(Component parentComponent, Object message, String title, int messageType) throws HeadlessException {
		return ( String)showInputDialog( parentComponent, message, title, messageType, null, null, null);
	}

	/**
	 * @param parentComponent
	 * @param message
	 * @param title
	 * @param messageType
	 * @param icon
	 * @param selectionValues
	 * @param initialSelectionValue
	 * @return
	 */
	public static Object showInputDialog(Component parentComponent, Object message, String title, int messageType, Icon icon, Object[] selectionValues, Object initialSelectionValue) throws HeadlessException {
		JOptionPane optionPane = new JOptionPane( message, messageType, JOptionPane.OK_CANCEL_OPTION, icon, null, null);

		optionPane.setWantsInput( true);
		optionPane.setSelectionValues( selectionValues);
		optionPane.setInitialSelectionValue( initialSelectionValue);
		optionPane.setComponentOrientation( ( ( null == parentComponent) ?
			JOptionPane.getRootFrame() : parentComponent).getComponentOrientation());

		//int style = styleFromMessageType( messageType);
		//JDialog dialog = optionPane.createDialog( parentComponent, title, style);
		JDialog dialog = optionPane.createDialog( parentComponent, title);

		optionPane.selectInitialValue();
		dialog.setAlwaysOnTop( true);
		//dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible( true);
		dialog.dispose();

		Object value = optionPane.getInputValue();

		if ( JOptionPane.UNINITIALIZED_VALUE == value)
      return null;

		return value;
	}

	/**
	 * @param parentComponent
	 * @param message
	 */
	public static void showMessageDialog(Component parentComponent, Object message) throws HeadlessException {
		showMessageDialog( parentComponent, message,
			UIManager.getString( "OptionPane.messageDialogTitle",
				( ( null == parentComponent) ? Locale.getDefault() : parentComponent.getLocale())),
			JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * @param parentComponent
	 * @param message
	 * @param title
	 * @param messageType
	 */
	public static void showMessageDialog(Component parentComponent, Object message, String title, int messageType) throws HeadlessException {
  	showMessageDialog( parentComponent, message, title, messageType, null);
	}

	/**
	 * @param parentComponent
	 * @param message
	 * @param title
	 * @param messageType
	 * @param icon
	 */
	public static void showMessageDialog(Component parentComponent, Object message, String title, int messageType, Icon icon) throws HeadlessException {
		showOptionDialog( parentComponent, message, title, JOptionPane.DEFAULT_OPTION, messageType, icon, null, null);
	}

	/**
	 * @param parentComponent
	 * @param message
	 * @return
	 */
	public static int showConfirmDialog(Component parentComponent, Object message) throws HeadlessException {
		return showConfirmDialog( parentComponent, message, 	UIManager.getString("OptionPane.titleText"), JOptionPane.YES_NO_CANCEL_OPTION);
	}

	/**
	 * @param parentComponent
	 * @param message
	 * @param title
	 * @param optionType
	 * @return
	 */
	public static int showConfirmDialog(Component parentComponent, Object message, String title, int optionType) throws HeadlessException {
		return showConfirmDialog( parentComponent, message, title, optionType, JOptionPane.QUESTION_MESSAGE);
	}

	/**
	 * @param parentComponent
	 * @param message
	 * @param title
	 * @param optionType
	 * @param messageType
	 * @return
	 */
	public static int showConfirmDialog(Component parentComponent, Object message, String title, int optionType, int messageType) throws HeadlessException {
		return showConfirmDialog( parentComponent, message, title, optionType, messageType, null);
	}

	/**
	 * @param parentComponent
	 * @param message
	 * @param title
	 * @param optionType
	 * @param messageType
	 * @param icon
	 * @return
	 */
	public static int showConfirmDialog(Component parentComponent, Object message, String title, int optionType, int messageType, Icon icon) throws HeadlessException {
		return showOptionDialog( parentComponent, message, title, optionType, messageType, icon, null, null);
	}

	/**
	 * @param parentComponent
	 * @param message
	 * @param title
	 * @param optionType
	 * @param messageType
	 * @param icon
	 * @param options
	 * @param initialValue
	 * @return
	 */
	public static int showOptionDialog(Component parentComponent, Object message, String title, int optionType, int messageType, Icon icon, Object[] options, Object initialValue) {
		JOptionPane optionPane = new JOptionPane( message, messageType, optionType, icon, options, initialValue);

		optionPane.setInitialValue( initialValue);
		optionPane.setComponentOrientation( ( ( null == parentComponent)
			? JOptionPane.getRootFrame()
			: parentComponent).getComponentOrientation());

		//int style = styleFromMessageType(messageType);
		//JDialog dialog = optionPane.createDialog(parentComponent, title, style);
		JDialog dialog = optionPane.createDialog( parentComponent, title);

		optionPane.selectInitialValue();
		dialog.setAlwaysOnTop( true);
		//dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible( true);
		dialog.dispose();

		Object selectedValue = optionPane.getValue();

		if ( null == selectedValue)
			return JOptionPane.CLOSED_OPTION;

		if ( null == options) {
			if ( selectedValue instanceof Integer)
				return ( ( Integer)selectedValue).intValue();

			return JOptionPane.CLOSED_OPTION;
	  }

		for ( int counter = 0, maxCounter = options.length; counter < maxCounter; counter++) {
			if ( options[ counter].equals( selectedValue))
				return counter;
		}

		return JOptionPane.CLOSED_OPTION;
	}

	/**
	 * @param parentComponent
	 * @param message
	 * @param title
	 * @param optionType
	 * @param messageType
	 * @param icon
	 * @param options
	 * @param initialValue
	 * @return
	 */
	public static Object executeOptionDialog(Object message, int messageType, int optionType, Icon icon, Object[] options, Object initialValue, Component parentComponent, String title) {
		JOptionPane optionPane = new JOptionPane( message, messageType, optionType, icon, options, initialValue);
		JDialog dialog = optionPane.createDialog( parentComponent, title);
		dialog.setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setVisible( true);
		return optionPane.getValue();
	}

//	/**
//	 * @param messageType
//	 * @return
//	 */
//	private static int styleFromMessageType(int messageType) {
//		switch ( messageType) {
//			case JOptionPane.ERROR_MESSAGE:
//				return JRootPane.ERROR_DIALOG;
//			case JOptionPane.QUESTION_MESSAGE:
//				return JRootPane.QUESTION_DIALOG;
//			case JOptionPane.WARNING_MESSAGE:
//				return JRootPane.WARNING_DIALOG;
//			case JOptionPane.INFORMATION_MESSAGE:
//				return JRootPane.INFORMATION_DIALOG;
//			case JOptionPane.PLAIN_MESSAGE:
//			default:
//				return JRootPane.PLAIN_DIALOG;
//		}
//	}
}
