/**
 * 
 */
package soars.common.utility.swing.button;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * @author kurata
 *
 */
public class RadioButton extends JPanel {

	/**
	 * 
	 */
	public JRadioButton _radioButton = null;

	/**
	 * 
	 */
	public JLabel _label = null;

	/**
	 * 
	 */
	public boolean _synchronize = true;

	/**
	 * 
	 */
	public boolean _right = false;

	/**
	 * @param synchronize
	 * @param right
	 */
	public RadioButton( boolean synchronize, boolean right) {
		super();
		_radioButton = new JRadioButton();
		_label = new JLabel();
		_synchronize = synchronize;
		_right = right;
		setup();
	}

	/**
	 * @param arg0
	 * @param synchronize
	 * @param right
	 */
	public RadioButton(Icon arg0, boolean synchronize, boolean right) {
		super();
		_radioButton = new JRadioButton( arg0);
		_label = new JLabel();
		_synchronize = synchronize;
		_right = right;
		setup();
	}

	/**
	 * @param arg0
	 * @param synchronize
	 * @param right
	 */
	public RadioButton(Action arg0, boolean synchronize, boolean right) {
		super();
		_radioButton = new JRadioButton( arg0);
		_label = new JLabel();
		_synchronize = synchronize;
		_right = right;
		setup();
	}

	/**
	 * @param arg0
	 * @param synchronize
	 * @param right
	 */
	public RadioButton(String arg0, boolean synchronize, boolean right) {
		super();
		_radioButton = new JRadioButton();
		_label = new JLabel( arg0);
		_synchronize = synchronize;
		_right = right;
		setup();
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param synchronize
	 * @param right
	 */
	public RadioButton(Icon arg0, boolean arg1, boolean synchronize, boolean right) {
		super();
		_radioButton = new JRadioButton( arg0, arg1);
		_label = new JLabel();
		_synchronize = synchronize;
		_right = right;
		setup();
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param synchronize
	 * @param right
	 */
	public RadioButton(String arg0, boolean arg1, boolean synchronize, boolean right) {
		super();
		_radioButton = new JRadioButton( "", arg1);
		_label = new JLabel( arg0);
		_synchronize = synchronize;
		_right = right;
		setup();
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param synchronize
	 * @param right
	 */
	public RadioButton(String arg0, Icon arg1, boolean synchronize, boolean right) {
		super();
		_radioButton = new JRadioButton( "", arg1);
		_label = new JLabel( arg0);
		_synchronize = synchronize;
		_right = right;
		setup();
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param synchronize
	 * @param right
	 */
	public RadioButton(String arg0, Icon arg1, boolean arg2, boolean synchronize, boolean right) {
		super();
		_radioButton = new JRadioButton( "", arg1, arg2);
		_label = new JLabel( arg0);
		_synchronize = synchronize;
		_right = right;
		setup();
	}

	/**
	 * 
	 */
	private void setup() {
		setLayout( new FlowLayout( _right ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 0));
		add( _radioButton);
		add( _label);
		_label.addMouseListener( new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
			}
			public void mouseEntered(MouseEvent arg0) {
			}
			public void mouseExited(MouseEvent arg0) {
			}
			public void mousePressed(MouseEvent arg0) {
			}
			public void mouseReleased(MouseEvent arg0) {
				if ( null != _radioButton && _radioButton.isEnabled() && 1 == arg0.getButton())
					setSelected( true);
			}
		});
	}

	/**
	 * @param itemListener
	 */
	public void addItemListener(ItemListener itemListener) {
		if ( null == _radioButton || null == _label)
			return;

		_radioButton.addItemListener( itemListener);
	}

	/**
	 * @return
	 */
	public boolean isSelected() {
		if ( null == _radioButton || null == _label)
			return false;

		return _radioButton.isSelected();
	}

	/**
	 * @param b
	 */
	public void setSelected(boolean b) {
		if ( null == _radioButton || null == _label)
			return;

		_radioButton.setSelected( b);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	public void setEnabled(boolean arg0) {
		if ( null == _radioButton || null == _label)
			return;

		_radioButton.setEnabled( arg0);
		_label.setEnabled( _synchronize ? arg0 : true);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setForeground(java.awt.Color)
	 */
	public void setForeground(Color arg0) {
		if ( null == _radioButton || null == _label)
			return;

		_radioButton.setForeground( arg0);
		_label.setForeground( arg0);
	}
}
