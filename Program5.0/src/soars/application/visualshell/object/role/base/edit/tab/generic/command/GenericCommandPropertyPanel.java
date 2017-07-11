/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.command;

import java.awt.Frame;
import java.util.List;
import java.util.Map;

import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot;
import soars.application.visualshell.object.role.base.edit.tab.generic.base.GenericPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.base.VerbPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;

/**
 * @author kurata
 *
 */
public class GenericCommandPropertyPanel extends GenericPropertyPanel {

	/**
	 * @param kind
	 * @param property
	 * @param role
	 * @param buddiesMap
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public GenericCommandPropertyPanel(String kind, Property property, Role role, Map<String, List<PanelRoot>> buddiesMap, int index, Frame owner, EditRoleFrame parent) {
		super(kind, property, role, buddiesMap, index, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.generic.base.GenericPropertyPanel#create_VerbPanel()
	 */
	@Override
	protected VerbPanel create_VerbPanel() {
		return new CommandVerbPanel( _property, this, _owner, _parent);
	}
}
