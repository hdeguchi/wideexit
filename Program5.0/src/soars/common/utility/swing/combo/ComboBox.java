/*
 * Created on 2006/01/28
 */
package soars.common.utility.swing.combo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import soars.common.utility.swing.panel.StandardPanel;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 */
public class ComboBox extends JComboBox {

	/**
	 * 
	 */
	static protected Color[] _textFieldDefaultColors = new Color[ 3];

	/**
	 * 
	 */
	private Color _color = null;

	/**
	 * 
	 */
	private JTextField _textField = null;

	/**
	 * @param items
	 * @param right
	 * @param basicComboBoxRenderer
	 * @return
	 */
	public static ComboBox create(Object[] items, boolean right, BasicComboBoxRenderer basicComboBoxRenderer) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( right, basicComboBoxRenderer);
		else
			comboBox = new ComboBox( items, right, basicComboBoxRenderer);

		return comboBox;
	}

	/**
	 * @param items
	 * @param color
	 * @param right
	 * @param basicComboBoxRenderer
	 * @return
	 */
	public static ComboBox create(Object[] items, Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( color, right, basicComboBoxRenderer);
		else
			comboBox = new ComboBox( items, color, right, basicComboBoxRenderer);

		return comboBox;
	}

	/**
	 * @param items
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param standardPanel
	 * @return
	 */
	public static ComboBox create(Object[] items, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, StandardPanel standardPanel) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( right, basicComboBoxRenderer, standardPanel);
		else
			comboBox = new ComboBox( items, right, basicComboBoxRenderer, standardPanel);

		return comboBox;
	}

	/**
	 * @param items
	 * @param color
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param standardPanel
	 * @return
	 */
	public static ComboBox create(Object[] items, Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, StandardPanel standardPanel) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( color, right, basicComboBoxRenderer, standardPanel);
		else
			comboBox = new ComboBox( items, color, right, basicComboBoxRenderer, standardPanel);

		return comboBox;
	}

	/**
	 * @param items
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param dialog
	 * @return
	 */
	public static ComboBox create(Object[] items, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, Dialog dialog) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( right, basicComboBoxRenderer, dialog);
		else
			comboBox = new ComboBox( items, right, basicComboBoxRenderer, dialog);

		return comboBox;
	}

	/**
	 * @param items
	 * @param color
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param dialog
	 * @return
	 */
	public static ComboBox create(Object[] items, Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, Dialog dialog) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( color, right, basicComboBoxRenderer, dialog);
		else
			comboBox = new ComboBox( items, color, right, basicComboBoxRenderer, dialog);

		return comboBox;
	}

	/**
	 * @param items
	 * @param width
	 * @param right
	 * @param basicComboBoxRenderer
	 * @return
	 */
	public static ComboBox create(Object[] items, int width, boolean right, BasicComboBoxRenderer basicComboBoxRenderer) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( right, basicComboBoxRenderer);
		else
			comboBox = new ComboBox( items, right, basicComboBoxRenderer);

		comboBox.setPreferredSize( new Dimension( width, comboBox.getPreferredSize().height));

		return comboBox;
	}

	/**
	 * @param items
	 * @param width
	 * @param color
	 * @param right
	 * @param basicComboBoxRenderer
	 * @return
	 */
	public static ComboBox create(Object[] items, int width, Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( color, right, basicComboBoxRenderer);
		else
			comboBox = new ComboBox( items, color, right, basicComboBoxRenderer);

		comboBox.setPreferredSize( new Dimension( width, comboBox.getPreferredSize().height));

		return comboBox;
	}

	/**
	 * @param items
	 * @param width
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param standardPanel
	 * @return
	 */
	public static ComboBox create(Object[] items, int width, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, StandardPanel standardPanel) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( right, basicComboBoxRenderer, standardPanel);
		else
			comboBox = new ComboBox( items, right, basicComboBoxRenderer, standardPanel);

		comboBox.setPreferredSize( new Dimension( width, comboBox.getPreferredSize().height));

		return comboBox;
	}

	/**
	 * @param items
	 * @param width
	 * @param right
	 * @param color
	 * @param basicComboBoxRenderer
	 * @param standardPanel
	 * @return
	 */
	public static ComboBox create(Object[] items, int width, Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, StandardPanel standardPanel) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( color, right, basicComboBoxRenderer, standardPanel);
		else
			comboBox = new ComboBox( items, color, right, basicComboBoxRenderer, standardPanel);

		comboBox.setPreferredSize( new Dimension( width, comboBox.getPreferredSize().height));

		return comboBox;
	}

	/**
	 * @param items
	 * @param width
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param dialog
	 * @return
	 */
	public static ComboBox create(Object[] items, int width, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, Dialog dialog) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( right, basicComboBoxRenderer, dialog);
		else
			comboBox = new ComboBox( items, right, basicComboBoxRenderer, dialog);

		comboBox.setPreferredSize( new Dimension( width, comboBox.getPreferredSize().height));

		return comboBox;
	}

	/**
	 * @param items
	 * @param width
	 * @param color
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param dialog
	 * @return
	 */
	public static ComboBox create(Object[] items, int width, Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, Dialog dialog) {
		ComboBox comboBox = null;

		if ( null == items)
			comboBox = new ComboBox( color, right, basicComboBoxRenderer, dialog);
		else
			comboBox = new ComboBox( items, color, right, basicComboBoxRenderer, dialog);

		comboBox.setPreferredSize( new Dimension( width, comboBox.getPreferredSize().height));

		return comboBox;
	}

	/**
	 * 
	 */
	public ComboBox() {
		super();
	}

	/**
	 * @param right
	 * @param basicComboBoxRenderer
	 */
	public ComboBox(boolean right, BasicComboBoxRenderer basicComboBoxRenderer) {
		super();
		setup( right, basicComboBoxRenderer);
	}

	/**
	 * @param items
	 * @param right
	 * @param basicComboBoxRenderer
	 */
	public ComboBox(Object[] items, boolean right, BasicComboBoxRenderer basicComboBoxRenderer) {
		super(items);
		setup( right, basicComboBoxRenderer);
	}

	/**
	 * @param color
	 * @param right
	 * @param basicComboBoxRenderer
	 */
	public ComboBox(Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer) {
		super();
		_color = color;
		setup( right, basicComboBoxRenderer);
	}

	/**
	 * @param items
	 * @param color
	 * @param right
	 * @param basicComboBoxRenderer
	 */
	public ComboBox(Object[] items, Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer) {
		super(items);
		_color = color;
		setup( right, basicComboBoxRenderer);
	}

	/**
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param standardPanel
	 */
	public ComboBox(boolean right, BasicComboBoxRenderer basicComboBoxRenderer, StandardPanel standardPanel) {
		super();
		setup( right, basicComboBoxRenderer, standardPanel);
	}

	/**
	 * @param items
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param standardPanel
	 */
	public ComboBox(Object[] items, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, StandardPanel standardPanel) {
		super(items);
		setup( right, basicComboBoxRenderer, standardPanel);
	}

	/**
	 * @param color
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param standardPanel
	 */
	public ComboBox(Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, StandardPanel standardPanel) {
		super();
		_color = color;
		setup( right, basicComboBoxRenderer, standardPanel);
	}

	/**
	 * @param items
	 * @param color
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param standardPanel
	 */
	public ComboBox(Object[] items, Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, StandardPanel standardPanel) {
		super(items);
		_color = color;
		setup( right, basicComboBoxRenderer, standardPanel);
	}

	/**
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param dialog
	 */
	public ComboBox(boolean right, BasicComboBoxRenderer basicComboBoxRenderer, Dialog dialog) {
		super();
		setup( right, basicComboBoxRenderer, dialog);
	}

	/**
	 * @param items
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param dialog
	 */
	public ComboBox(Object[] items, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, Dialog dialog) {
		super(items);
		setup( right, basicComboBoxRenderer, dialog);
	}

	/**
	 * @param color
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param dialog
	 */
	public ComboBox(Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, Dialog dialog) {
		super();
		_color = color;
		setup( right, basicComboBoxRenderer, dialog);
	}

	/**
	 * @param items
	 * @param color
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param dialog
	 */
	public ComboBox(Object[] items, Color color, boolean right, BasicComboBoxRenderer basicComboBoxRenderer, Dialog dialog) {
		super(items);
		_color = color;
		setup( right, basicComboBoxRenderer, dialog);
	}

	/**
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param standardPanel
	 */
	private void setup(boolean right, BasicComboBoxRenderer basicComboBoxRenderer, StandardPanel standardPanel) {
		setup(right, basicComboBoxRenderer);

		standardPanel.link_to_ok( _textField);
		standardPanel.link_to_cancel( _textField);

		standardPanel.link_to_ok( this);
		standardPanel.link_to_cancel( this);
	}

	/**
	 * @param right
	 * @param basicComboBoxRenderer
	 * @param dialog
	 */
	private void setup(boolean right, BasicComboBoxRenderer basicComboBoxRenderer, Dialog dialog) {
		setup(right, basicComboBoxRenderer);

		dialog.link_to_cancel( _textField);

		dialog.link_to_cancel( this);
	}

	/**
	 * @param right
	 * @param basicComboBoxRenderer
	 */
	private void setup(boolean right, BasicComboBoxRenderer basicComboBoxRenderer) {
		setRenderer( basicComboBoxRenderer);

		BasicComboBoxEditor basicComboBoxEditor = new BasicComboBoxEditor();
		_textField = ( JTextField)basicComboBoxEditor.getEditorComponent();
		_textField.setOpaque( true);

		if ( null != _color) {
			if ( 0 <= System.getProperty( "os.name").indexOf( "Mac")) {
				addItemListener( new ItemListener() {
					public void itemStateChanged(ItemEvent arg0) {
						if ( arg0.getStateChange() == ItemEvent.SELECTED) {
							_textField.setSelectionColor( _color);
							_textField.setForeground( Color.white);
							_textField.setBackground( _color);
							setForeground( Color.white);
							setBackground( _color);
						} else {
							_textField.setSelectionColor( Color.white);
							_textField.setForeground( _color);
							_textField.setBackground( Color.white);
							setForeground( _color);
							setBackground( Color.white);
						}
					}
				});
				_textField.setSelectionColor( _color);
				_textField.setForeground( Color.white);
				_textField.setBackground( _color);
				setForeground( Color.white);
				setBackground( _color);
			} else {
				if ( null == _textFieldDefaultColors[ 0])
					_textFieldDefaultColors[ 0] = _textField.getSelectionColor();
				if ( null == _textFieldDefaultColors[ 1])
					_textFieldDefaultColors[ 1] = _textField.getForeground();
				_textField.setEditable( false);
				if ( null == _textFieldDefaultColors[ 2])
					_textFieldDefaultColors[ 2] = _textField.getBackground();
				setEditor( basicComboBoxEditor);
				setEditable( true);
		
				_textField.setSelectionColor( _color);
				_textField.setForeground( Color.white);
				_textField.setBackground( _color);
			}
		}

		if ( right)
			_textField.setHorizontalAlignment( JTextField.RIGHT);
	}

	/* (Non Javadoc)
	 * @see java.awt.Component#setEnabled(boolean)
	 */
	public void setEnabled(boolean arg0) {
		super.setEnabled(arg0);

		if ( null == _color)
			return;

		if ( 0 <= System.getProperty( "os.name").indexOf( "Mac")) {
			_textField.setSelectionColor( arg0 ? _color : Color.white);
			_textField.setForeground( arg0 ? Color.white : _color);
			_textField.setBackground( arg0 ? _color : Color.white);
			setForeground( arg0 ? Color.white : _color);
			setBackground( arg0 ? _color : Color.white);
		} else {
			_textField.setSelectionColor( arg0 ? _color : _textFieldDefaultColors[ 0]);
			_textField.setForeground( arg0 ? Color.white : _textFieldDefaultColors[ 1]);
			_textField.setBackground( arg0 ? _color : _textFieldDefaultColors[ 2]);
		}
	}
}
