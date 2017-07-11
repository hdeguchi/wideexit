/*
 * 2004/10/07
 */
package soars.common.utility.swing.tool;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import soars.common.utility.swing.button.RadioButton;

/**
 * @author kurata
 */
public class SwingTool {

	/**
	 * @param lookAndFeel
	 * @return
	 */
	public static boolean setup_look_and_feel(String lookAndFeel) {
		try {
			UIManager.setLookAndFeel( lookAndFeel);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (InstantiationException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param width
	 * @param height
	 * @return
	 */
	public static Point get_default_window_position(int width, int height) {
		Rectangle rectangle = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		int x = ( ( rectangle.width - width) / 2);
		int y = ( ( rectangle.height - height) / 2);
		return new Point( ( ( x < 0) ? 0 : x), ( ( y < 0) ? 0 : y));
	}

	/**
	 * @param component
	 * @param width
	 * @param height
	 * @return
	 */
	public static Point get_default_window_position(Component component, int width, int height) {
		Rectangle rectangle = component.getBounds();
		int x = ( rectangle.x + ( ( rectangle.width - width) / 2));
		int y = ( rectangle.y + ( ( rectangle.height - height) / 2));
		return new Point( ( ( x < 0) ? 0 : x), ( ( y < 0) ? 0 : y));
	}

	/**
	 * @param width1
	 * @param height1
	 * @param width2
	 * @param height2
	 * @return
	 */
	public static Point get_default_window_position(int width1, int height1, int width2, int height2) {
		int x = ( ( width1 - width2) / 2);
		int y = ( ( height1 - height2) / 2);
		return new Point( ( ( x < 0) ? 0 : x), ( ( y < 0) ? 0 : y));
	}

	/**
	 * @param mouseEvent
	 * @return
	 */
	public static boolean is_mouse_left_button(MouseEvent mouseEvent) {
		return ( 1 == mouseEvent.getButton() && 0 == ( InputEvent.CTRL_DOWN_MASK & mouseEvent.getModifiersEx()));
	}

	/**
	 * @param mouseEvent
	 * @return
	 */
	public static boolean is_mouse_left_button_double_click(MouseEvent mouseEvent) {
		return ( 1 == mouseEvent.getButton() && 2 == mouseEvent.getClickCount() && 0 == ( InputEvent.CTRL_DOWN_MASK & mouseEvent.getModifiersEx()));
	}

	/**
	 * @param mouseEvent
	 * @return
	 */
	public static boolean is_mouse_right_button(MouseEvent mouseEvent) {
		return ( 3 == mouseEvent.getButton()
			|| ( 0 <= System.getProperty( "os.name").indexOf( "Mac") && 1 == mouseEvent.getButton() && 0 != ( InputEvent.CTRL_DOWN_MASK & mouseEvent.getModifiersEx())));
	}

	/**
	 * @param radioButtons
	 * @return
	 */
	public static int get_enabled_radioButton(JRadioButton[] radioButtons) {
		for ( int i = 0; i < radioButtons.length; ++i) {
			if ( radioButtons[ i].isSelected())
				return i;
		}
		return 0;
	}

	/**
	 * @param radioButtons
	 * @return
	 */
	public static int get_enabled_radioButton(List<RadioButton> radioButtons) {
		for ( int i = 0; i < radioButtons.size(); ++i) {
			if ( radioButtons.get( i).isSelected())
				return i;
		}
		return 0;
	}

	/**
	 * @param radioButtons
	 * @return
	 */
	public static int get_enabled_radioButton(RadioButton[] radioButtons) {
		for ( int i = 0; i < radioButtons.length; ++i) {
			if ( radioButtons[ i].isSelected())
				return i;
		}
		return 0;
	}

	/**
	 * @param comboBox
	 * @param object
	 * @return
	 */
	public static boolean contains(JComboBox comboBox, Object object) {
		DefaultComboBoxModel defaultComboBoxModel = ( DefaultComboBoxModel)comboBox.getModel();
		int index = defaultComboBoxModel.getIndexOf( object);
		return ( 0 <= index && comboBox.getItemCount() > index);
	}

	/**
	 * @param container
	 */
	public static void insert_horizontal_glue(Container container) {
		JPanel gluePanel = new JPanel();
		gluePanel.add( Box.createHorizontalGlue());
		container.add( gluePanel);
	}

	/**
	 * @param container
	 * @param height
	 */
	public static void insert_vertical_strut(Container container, int height) {
		container.add( Box.createVerticalStrut( height));
	}

	/**
	 * @param scrollPane
	 */
	public static void set_table_left_top_corner_column(JScrollPane scrollPane) {
		JPanel borderPanel = new JPanel();
		borderPanel.setBorder( UIManager.getBorder( "TableHeader.cellBorder"));
		scrollPane.setCorner( JScrollPane.UPPER_LEFT_CORNER, borderPanel);
	}

	/**
	 * @param parentComponent
	 * @param message
	 */
	public static void showInformationMessageDialog(Component parentComponent, String message) {
		JOptionPane.showMessageDialog( parentComponent,new JLabel( message),
			"Information...", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * @param parentComponent
	 * @param message
	 */
	public static void showWarningMessageDialog(Component parentComponent, String message) {
		JOptionPane.showMessageDialog( parentComponent,new JLabel( message),
			"Warning...", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * @param parentComponent
	 * @param message
	 */
	public static void showErrorMessageDialog(Component parentComponent, String message) {
		JOptionPane.showMessageDialog( parentComponent,new JLabel( message),
			"Error...", JOptionPane.ERROR_MESSAGE);
	}
}
