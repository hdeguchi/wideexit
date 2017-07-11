/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.map.initial.panel.value;

import java.awt.Color;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import soars.application.visualshell.common.selector.ObjectSelector;

/**
 * @author kurata
 *
 */
public class AgentSelectorPanel extends JPanel {

	/**
	 * 
	 */
	public ObjectSelector _agentSelector = null;

	/**
	 * 
	 */
	public AgentSelectorPanel() {
		super();
	}

	/**
	 * @param color
	 */
	public void create(Color color) {
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));

		_agentSelector = new ObjectSelector( "agent", false, 80, 20, color, true, null);
		_agentSelector.selectFirstItem();
		add( _agentSelector);

		add( Box.createHorizontalStrut( 5));
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		_agentSelector.setEnabled( enabled);
		super.setEnabled(enabled);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean aFlag) {
		_agentSelector.setVisible( aFlag);
		super.setVisible(aFlag);
	}

	/**
	 * @param value
	 */
	public void set(String value) {
		_agentSelector.set( value);
	}

	/**
	 * @return
	 */
	public String get() {
		return _agentSelector.get();
	}
}
