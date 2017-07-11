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
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author kurata
 *
 */
public class CheckBox extends JPanel {

	/**
	 * 
	 */
	public JCheckBox _checkBox = null;

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
	public CheckBox( boolean synchronize, boolean right) {
		super();
		_checkBox = new JCheckBox();
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
	public CheckBox(Icon arg0, boolean synchronize, boolean right) {
		super();
		_checkBox = new JCheckBox( arg0);
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
	public CheckBox(String arg0, boolean synchronize, boolean right) {
		super();
		_checkBox = new JCheckBox();
		_label = new JLabel( arg0);
		_synchronize = synchronize;
		_right = right;
		setup();
	}

	/**
	 * @param arg0
	 * @param synchronize
	 * @param right
	 */
	public CheckBox(Action arg0, boolean synchronize, boolean right) {
		super();
		_checkBox = new JCheckBox( arg0);
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
	public CheckBox(Icon arg0, boolean arg1, boolean synchronize, boolean right) {
		super();
		_checkBox = new JCheckBox( arg0, arg1);
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
	public CheckBox(String arg0, boolean arg1, boolean synchronize, boolean right) {
		super();
		_checkBox = new JCheckBox( "", arg1);
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
	public CheckBox(String arg0, Icon arg1, boolean synchronize, boolean right) {
		super();
		_checkBox = new JCheckBox( "", arg1);
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
	public CheckBox(String arg0, Icon arg1, boolean arg2, boolean synchronize, boolean right) {
		super();
		_checkBox = new JCheckBox( "", arg1, arg2);
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
		add( _checkBox);
		add( _label);
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
				if ( null != _checkBox && _checkBox.isEnabled() && 1 == arg0.getButton())
					setSelected( !isSelected());
			}
		});
	}

	/**
	 * @param itemListener
	 */
	public void addItemListener(ItemListener itemListener) {
		if ( null == _checkBox || null == _label)
			return;

		_checkBox.addItemListener( itemListener);
	}

	/**
	 * @return
	 */
	public boolean isSelected() {
		if ( null == _checkBox || null == _label)
			return false;

		return _checkBox.isSelected();
	}

	/**
	 * @param b
	 */
	public void setSelected(boolean b) {
		if ( null == _checkBox || null == _label)
			return;

		_checkBox.setSelected( b);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	public void setEnabled(boolean arg0) {
		if ( null == _checkBox || null == _label)
			return;

		_checkBox.setEnabled( arg0);
		_label.setEnabled( _synchronize ? arg0 : true);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setForeground(java.awt.Color)
	 */
	public void setForeground(Color arg0) {
		if ( null == _checkBox || null == _label)
			return;

		_checkBox.setForeground( arg0);
		_label.setForeground( arg0);
	}

	/**
	 * @param synchronize
	 */
	public void setSynchronize(boolean synchronize) {
		_synchronize = synchronize;
	}
}
