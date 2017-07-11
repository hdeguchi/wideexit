/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.command.exchange_algebra.panel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.common.utility.swing.combo.ComboBox;

/**
 * @author kurata
 *
 */
public class HatPanel extends JPanel {

	/**
	 * 
	 */
	private List<JComponent> _components = new ArrayList<JComponent>();

	/**
	 * 
	 */
	private JLabel _label = null;

	/**
	 * 
	 */
	private String _name = "";

	/**
	 * 
	 */
	public ComboBox _comboBox = null;

	/**
	 * 
	 */
	private boolean _margin = false;

	/**
	 * 
	 */
	private RulePropertyPanelBase _rulePropertyPanelBase = null;

	/**
	 * @param margin
	 * @param name
	 * @param rulePropertyPanelBase
	 */
	public HatPanel(boolean margin, String name, RulePropertyPanelBase rulePropertyPanelBase) {
		super();
		_margin = margin;
		_name = name;
		_rulePropertyPanelBase = rulePropertyPanelBase;
	}

	/**
	 * @return
	 */
	public boolean create() {
		int pad = 5;

		setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		if ( _margin) {
			JLabel dummy = new JLabel();
			add( dummy);
			_components.add( dummy);
		}

		_label = _rulePropertyPanelBase.create_label( _name, true);
		add( _label);

		_comboBox = _rulePropertyPanelBase.create_comboBox(
			new String[] {
				ResourceManager.get_instance().get( "edit.exchange.algebra.value.dialog.edit.hat.combo.box.hat"),
				ResourceManager.get_instance().get( "edit.exchange.algebra.value.dialog.edit.hat.combo.box.no_hat")
			}, false);
		add( _comboBox);

		return true;
	}

	/**
	 * @param width
	 * @return
	 */
	public int get_width(int width) {
		return Math.max( width, _label.getPreferredSize().width);
	}

	/**
	 * @param width
	 */
	public void adjust(int width) {
		_label.setPreferredSize( new Dimension( width, _label.getPreferredSize().height));
	}

	/**
	 * @param width
	 * @return
	 */
	public int get_margin_width(int width) {
		for ( JComponent component:_components)
			width = Math.max( width, component.getPreferredSize().width);
		return width;
	}

	/**
	 * @param width
	 */
	public void adjust_margin_width(int width) {
		for ( JComponent component:_components)
			component.setPreferredSize( new Dimension( width, component.getPreferredSize().height));
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		_comboBox.setSelectedIndex( 0);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		_label.setEnabled( enabled);
		_comboBox.setEnabled( enabled);
		super.setEnabled(enabled);
	}

	/**
	 * @param key
	 * @return
	 */
	public boolean set(String key) {
		if ( !key.equals( ResourceManager.get_instance().get( "edit.exchange.algebra.value.dialog.edit.hat.combo.box.hat"))
			&& !key.equals( ResourceManager.get_instance().get( "edit.exchange.algebra.value.dialog.edit.hat.combo.box.no_hat")))
			return false;

		_comboBox.setSelectedItem( key);
		return true;
	}

	/**
	 * @return
	 */
	public String get() {
		return ( String)_comboBox.getSelectedItem();
	}
}
