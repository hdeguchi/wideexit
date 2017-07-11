/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.command;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.JPanel;

import soars.application.visualshell.object.role.base.edit.tab.generic.base.GenericPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.base.VerbPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;

/**
 * @author kurata
 *
 */
public class CommandVerbPanel extends VerbPanel {

	/**
	 * @param property
	 * @param genericPropertyPanel
	 * @param owner
	 * @param parent
	 */
	public CommandVerbPanel(Property property, GenericPropertyPanel genericPropertyPanel, Frame owner, Component parent) {
		super(property, genericPropertyPanel, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.generic.base.VerbPanel#setup_denial_panel(javax.swing.JPanel)
	 */
	@Override
	protected void setup_denial_panel(JPanel parent) {
		parent.add( new JPanel());
	}
}
