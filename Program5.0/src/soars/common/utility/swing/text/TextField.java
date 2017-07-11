/*
 * Created on 2006/01/28
 */
package soars.common.utility.swing.text;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

import soars.common.utility.swing.panel.StandardPanel;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 */
public class TextField extends JTextField {

	/**
	 * @param width
	 * @param color
	 * @param right
	 * @param standardPanel
	 * @return
	 */
	public static TextField create(int width, Color color, boolean right, StandardPanel standardPanel) {
		TextField textField = new TextField();
		textField.setPreferredSize( new Dimension( width, textField.getPreferredSize().height));
		textField.setup( color, right, standardPanel);
		return textField;
	}

	/**
	 * @param textLimiter
	 * @param width
	 * @param color
	 * @param right
	 * @param standardPanel
	 * @return
	 */
	public static TextField create(TextLimiter textLimiter, int width, Color color, boolean right, StandardPanel standardPanel) {
		TextField textField = new TextField();
		textField.setDocument( textLimiter);
		textField.setPreferredSize( new Dimension( width, textField.getPreferredSize().height));
		textField.setup( color, right, standardPanel);
		return textField;
	}

	/**
	 * @param textLimiter
	 * @param text
	 * @param width
	 * @param color
	 * @param right
	 * @param standardPanel
	 * @return
	 */
	public static TextField create(TextLimiter textLimiter, String text, int width, Color color, boolean right, StandardPanel standardPanel) {
		TextField textField = new TextField();
		textField.setDocument( textLimiter);
		textField.setText( text);
		textField.setPreferredSize( new Dimension( width, textField.getPreferredSize().height));
		textField.setup( color, right, standardPanel);
		return textField;
	}

	/**
	 * @param textExcluder
	 * @param width
	 * @param color
	 * @param right
	 * @param standardPanel
	 * @return
	 */
	public static TextField create(TextExcluder textExcluder, int width, Color color, boolean right, StandardPanel standardPanel) {
		TextField textField = new TextField();
		textField.setDocument( textExcluder);
		textField.setPreferredSize( new Dimension( width, textField.getPreferredSize().height));
		textField.setup( color, right, standardPanel);
		return textField;
	}

	/**
	 * @param textExcluder
	 * @param text
	 * @param width
	 * @param color
	 * @param right
	 * @param standardPanel
	 * @return
	 */
	public static TextField create(TextExcluder textExcluder, String text, int width, Color color, boolean right, StandardPanel standardPanel) {
		TextField textField = new TextField();
		textField.setDocument( textExcluder);
		textField.setText( text);
		textField.setPreferredSize( new Dimension( width, textField.getPreferredSize().height));
		textField.setup( color, right, standardPanel);
		return textField;
	}

	/**
	 * @param width
	 * @param color
	 * @param right
	 * @param dialog
	 * @return
	 */
	public static TextField create(int width, Color color, boolean right, Dialog dialog) {
		TextField textField = new TextField();
		textField.setPreferredSize( new Dimension( width, textField.getPreferredSize().height));
		textField.setup( color, right, dialog);
		return textField;
	}

	/**
	 * @param textLimiter
	 * @param width
	 * @param color
	 * @param right
	 * @param dialog
	 * @return
	 */
	public static TextField create(TextLimiter textLimiter, int width, Color color, boolean right, Dialog dialog) {
		TextField textField = new TextField();
		textField.setDocument( textLimiter);
		textField.setPreferredSize( new Dimension( width, textField.getPreferredSize().height));
		textField.setup( color, right, dialog);
		return textField;
	}

	/**
	 * @param textLimiter
	 * @param text
	 * @param width
	 * @param color
	 * @param right
	 * @param dialog
	 * @return
	 */
	public static TextField create(TextLimiter textLimiter, String text, int width, Color color, boolean right, Dialog dialog) {
		TextField textField = new TextField();
		textField.setDocument( textLimiter);
		textField.setText( text);
		textField.setPreferredSize( new Dimension( width, textField.getPreferredSize().height));
		textField.setup( color, right, dialog);
		return textField;
	}

	/**
	 * @param textExcluder
	 * @param width
	 * @param color
	 * @param right
	 * @param dialog
	 * @return
	 */
	public static TextField create(TextExcluder textExcluder, int width, Color color, boolean right, Dialog dialog) {
		TextField textField = new TextField();
		textField.setDocument( textExcluder);
		textField.setPreferredSize( new Dimension( width, textField.getPreferredSize().height));
		textField.setup( color, right, dialog);
		return textField;
	}

	/**
	 * @param textExcluder
	 * @param text
	 * @param width
	 * @param color
	 * @param right
	 * @param dialog
	 * @return
	 */
	public static TextField create(TextExcluder textExcluder, String text, int width, Color color, boolean right, Dialog dialog) {
		TextField textField = new TextField();
		textField.setDocument( textExcluder);
		textField.setText( text);
		textField.setPreferredSize( new Dimension( width, textField.getPreferredSize().height));
		textField.setup( color, right, dialog);
		return textField;
	}

	/**
	 * 
	 */
	public TextField() {
		super();
	}

	/**
	 * @param color
	 * @param right
	 * @return
	 */
	public TextField(Color color, boolean right) {
		TextField textField = new TextField();
		textField.setup( color, right);
	}

	/**
	 * @param textLimiter
	 * @param color
	 * @param right
	 * @return
	 */
	public TextField(TextLimiter textLimiter, Color color, boolean right) {
		TextField textField = new TextField();
		textField.setDocument( textLimiter);
		textField.setup( color, right);
	}

	/**
	 * @param textLimiter
	 * @param text
	 * @param color
	 * @param right
	 * @return
	 */
	public TextField(TextLimiter textLimiter, String text, Color color, boolean right) {
		TextField textField = new TextField();
		textField.setDocument( textLimiter);
		textField.setText( text);
		textField.setup( color, right);
	}

	/**
	 * @param textExcluder
	 * @param color
	 * @param right
	 * @return
	 */
	public TextField(TextExcluder textExcluder, Color color, boolean right) {
		TextField textField = new TextField();
		textField.setDocument( textExcluder);
		textField.setup( color, right);
	}

	/**
	 * @param textExcluder
	 * @param text
	 * @param color
	 * @param right
	 * @return
	 */
	public TextField(TextExcluder textExcluder, String text, Color color, boolean right) {
		TextField textField = new TextField();
		textField.setDocument( textExcluder);
		textField.setText( text);
		textField.setup( color, right);
	}

	/**
	 * @param color
	 * @param right
	 * @param standardPanel
	 */
	private void setup(Color color, boolean right, StandardPanel standardPanel) {
		setup( color, right);

		standardPanel.link_to_ok( this);
		standardPanel.link_to_cancel( this);
	}

	/**
	 * @param color
	 * @param right
	 * @param dialog
	 */
	private void setup(Color color, boolean right, Dialog dialog) {
		setup( color, right);

		dialog.link_to_cancel( this);
	}

	/**
	 * @param color
	 * @param right
	 */
	private void setup(Color color, boolean right) {
		setSelectionColor( color);
		setForeground( color);

		if ( right)
			setHorizontalAlignment( SwingConstants.RIGHT);
	}
}
