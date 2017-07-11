/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.condition;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot;
import soars.application.visualshell.object.role.base.edit.tab.generic.base.GenericPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.base.VerbPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Verb;
import soars.application.visualshell.object.role.base.object.generic.GenericRule;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class ConditionVerbPanel extends VerbPanel {

	/**
	 * 
	 */
	private CheckBox _denialCheckBox = null;

	/**
	 * 
	 */
	private JPanel _denialPanel = null;

	/**
	 * @param property
	 * @param genericPropertyPanel
	 * @param owner
	 * @param parent
	 */
	public ConditionVerbPanel(Property property, GenericPropertyPanel genericPropertyPanel, Frame owner, Component parent) {
		super(property, genericPropertyPanel, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.generic.base.VerbPanel#setup(soars.application.visualshell.object.role.base.Role, java.util.Map, javax.swing.JPanel)
	 */
	@Override
	public boolean setup(Role role, Map<String, List<PanelRoot>> buddiesMap, JPanel parent) {
		if (!super.setup(role, buddiesMap, parent))
			return false;

		adjust_component_height();

		return true;
	}

	/**
	 * 
	 */
	private void adjust_component_height() {
		if ( null != _verbLabel)
			_verbLabel.setPreferredSize( new Dimension( _verbLabel.getPreferredSize().width, _denialCheckBox.getPreferredSize().height));

		if ( null != _verbComboBox)
			_verbComboBox.setPreferredSize( new Dimension( _verbComboBox.getPreferredSize().width, _denialCheckBox.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.generic.base.VerbPanel#setup_denial_panel(javax.swing.JPanel)
	 */
	@Override
	protected void setup_denial_panel(JPanel parent) {
		_denialPanel = new JPanel();
		_denialPanel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		SwingTool.insert_vertical_strut( northPanel, 5);

		setup_denialCheckBox( northPanel);

		SwingTool.insert_vertical_strut( northPanel, 5);

		_denialPanel.add( northPanel, "North");

		_denialPanel.setVisible( _property._denial);

		parent.add( _denialPanel);
	}

	/**
	 * @param parent
	 */
	private void setup_denialCheckBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		_denialCheckBox = _genericPropertyPanel.create_checkBox( ResourceManager.get_instance().get( "generic.gui.rule.denial") + " ", _property._color, false, false);
		panel.add( _denialCheckBox);

		parent.add( panel);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.generic.base.VerbPanel#show_denial_panel(soars.application.visualshell.object.role.base.edit.tab.generic.property.Verb)
	 */
	@Override
	protected void show_denial_panel(Verb verb) {
		_denialPanel.setVisible( _property._denial && verb._denial);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.generic.base.VerbPanel#set(soars.application.visualshell.object.role.base.object.base.GenericRule)
	 */
	@Override
	public boolean set(GenericRule genericRule) {
		if (!super.set(genericRule))
			return false;

		_denialCheckBox.setSelected( genericRule._denial);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.generic.base.VerbPanel#get(soars.application.visualshell.object.role.base.object.base.GenericRule)
	 */
	@Override
	public boolean get(GenericRule genericRule) {
		if (!super.get(genericRule))
			return false;

		genericRule._denial = _denialPanel.isVisible() ? _denialCheckBox.isSelected() : false;

		return true;
	}
}
