/**
 * 
 */
package soars.application.animator.common.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.animator.common.font.FontSizeListCellRenderer;
import soars.application.animator.common.tool.CommonTool;
import soars.application.animator.main.ResourceManager;
import soars.common.utility.swing.window.Dialog;

/**
 * The common GUI panel for font selection.
 * @author kurata / SOARS project
 */
public class FontPanel extends JPanel {

	/**
	 * Label for font name.
	 */
	public JLabel _fontFamilyLabel = null;

	/**
	 * Label for font style.
	 */
	public JLabel _fontStyleLabel = null;

	/**
	 * Label for font size.
	 */
	public JLabel _fontSizeLabel = null;

	/**
	 * Combo box for font names.
	 */
	public JComboBox _fontFamilyComboBox = null;

	/**
	 * Combo box for font styles.
	 */
	public JComboBox _fontStyleComboBox = null;

	/**
	 * Combo box for font sizes.
	 */
	public JComboBox _fontSizeComboBox = null;

	/**
	 * Frame of the parent container.
	 */
	private Frame _owner = null;

	/**
	 * Parent container of this component.
	 */
	private Component _parent = null;

	/**
	 * Creates the common GUI panel for font selection.
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public FontPanel(Frame owner, Component parent) {
		super();
		_owner = owner;
		_parent = parent;
	}

	/**
	 * Creates the components to select the font name, style and size.
	 * @param font the specified font object
	 * @param dialog the parent container of this component
	 */
	public void setup(Font font, Dialog dialog) {
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));

		add( Box.createHorizontalStrut( 5));

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		panel.setBorder( BorderFactory.createTitledBorder(
			ResourceManager.get_instance().get( "font.panel.border.title")));

		setup_font_family_combo_box( font, panel);

		dialog.insert_horizontal_glue( panel);

		setup_font_style_combo_box( font, panel);

		dialog.insert_horizontal_glue( panel);

		setup_font_size_combo_box( font, panel);

		dialog.insert_horizontal_glue( panel);

		add( panel);

		add( Box.createHorizontalStrut( 5));
	}

	/**
	 * Creates the components to select the font name.
	 * @param font the specified font object
	 * @param parent the parent container of the component on which the components are displayed
	 */
	private void setup_font_family_combo_box(Font font, JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_fontFamilyLabel = new JLabel(
			ResourceManager.get_instance().get( "font.panel.font.family"));
		_fontFamilyLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _fontFamilyLabel);

		panel.add( Box.createHorizontalStrut( 5));

		_fontFamilyComboBox = CommonTool.get_font_family_combo_box();
		_fontFamilyComboBox.setSelectedItem( font.getFamily());
		panel.add( _fontFamilyComboBox);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * Creates the components to select the font style.
	 * @param font the specified font object
	 * @param parent the parent container of the component on which the components are displayed
	 */
	private void setup_font_style_combo_box(Font font, JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_fontStyleLabel = new JLabel(
			ResourceManager.get_instance().get( "font.panel.font.style"));
		_fontStyleLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _fontStyleLabel);

		panel.add( Box.createHorizontalStrut( 5));

		_fontStyleComboBox = CommonTool.get_font_style_combo_box();
		_fontStyleComboBox.setSelectedItem( CommonTool.get_font_style( font.getStyle()));
		panel.add( _fontStyleComboBox);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * Creates the components to select the font size.
	 * @param font the specified font object
	 * @param parent the parent container of the component on which the components are displayed
	 */
	private void setup_font_size_combo_box(Font font, JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_fontSizeLabel = new JLabel(
			ResourceManager.get_instance().get( "font.panel.font.size"));
		_fontSizeLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _fontSizeLabel);

		panel.add( Box.createHorizontalStrut( 5));

		_fontSizeComboBox = CommonTool.get_font_size_combo_box();
		_fontSizeComboBox.setRenderer( new FontSizeListCellRenderer());
		_fontSizeComboBox.setSelectedItem( String.valueOf( font.getSize()));
		panel.add( _fontSizeComboBox);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * Returns the maximum width of the labels on this component.
	 * @return the maximum width of the labels on this component
	 */
	public int get_max_width() {
		int width = _fontFamilyLabel.getPreferredSize().width;
		width = Math.max( width, _fontStyleLabel.getPreferredSize().width);
		width = Math.max( width, _fontSizeLabel.getPreferredSize().width);
		return width;
	}

	/**
	 * Sets the specified width of the labels on this component.
	 * @param width the specified width
	 */
	public void set_width(int width) {
		_fontFamilyLabel.setPreferredSize( new Dimension( width, _fontFamilyLabel.getPreferredSize().height));
		_fontStyleLabel.setPreferredSize( new Dimension( width, _fontStyleLabel.getPreferredSize().height));
		_fontSizeLabel.setPreferredSize( new Dimension( width, _fontSizeLabel.getPreferredSize().height));
	}
}
