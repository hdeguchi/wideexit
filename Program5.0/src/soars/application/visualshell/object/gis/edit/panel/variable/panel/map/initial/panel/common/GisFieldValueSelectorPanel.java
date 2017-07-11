/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.variable.panel.map.initial.panel.common;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class GisFieldValueSelectorPanel extends JPanel {

	/**
	 * 
	 */
	protected GisDataManager _gisDataManager = null;

	/**
	 * 
	 */
	private JLabel _label = null;

	/**
	 * 
	 */
	protected ComboBox _fieldValueComboBox = null;

	/**
	 * @param gisDataManager
	 */
	public GisFieldValueSelectorPanel(GisDataManager gisDataManager) {
		super();
		_gisDataManager = gisDataManager;
	}

	/**
	 * @param text
	 * @param color
	 */
	public void create(String text, Color color) {
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
		SwingTool.insert_vertical_strut( this, 5);
		setup_fieldValueComboBox( text, color);
	}

	/**
	 * @param text
	 * @param color
	 */
	private void setup_fieldValueComboBox(String text, Color color) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_label = new JLabel( text);
		_label.setHorizontalAlignment( SwingConstants.RIGHT);
		_label.setForeground( color);
		panel.add( _label);

		panel.add( Box.createHorizontalStrut( 5));

		_fieldValueComboBox = new ComboBox( color, false, new CommonComboBoxRenderer( color, false));
		String[] fields = _gisDataManager.get_fields( false);
		CommonTool.update( _fieldValueComboBox, fields);
		panel.add( _fieldValueComboBox);

		panel.add( Box.createHorizontalStrut( 5));

		add( panel);
	}

	/**
	 * @param width
	 * @return
	 */
	public int get_label_width(int width) {
		return Math.max( width, _label.getPreferredSize().width);
	}

	/**
	 * @param width
	 */
	public void adjust(int width) {
		_label.setPreferredSize( new Dimension( width, _label.getPreferredSize().height));
	}

	/**
	 * @return
	 */
	public String get() {
		return ( String)_fieldValueComboBox.getSelectedItem();
	}

	/**
	 * @param value
	 */
	public void set(String value) {
		_fieldValueComboBox.setSelectedItem( value);
	}
}
