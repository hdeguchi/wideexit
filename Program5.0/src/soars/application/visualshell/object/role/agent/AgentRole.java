/*
 * 2005/05/20
 */
package soars.application.visualshell.object.role.agent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import org.xml.sax.SAXException;

import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.role.base.Role;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 */
public class AgentRole extends Role {

	/**
	 * @param id
	 * @param name
	 * @param position
	 * @param graphics2D
	 */
	public AgentRole(int id, String name, Point position, Graphics2D graphics2D) {
		super(id, name, position, graphics2D);
		setup_graphics();
	}

	/**
	 * @param global
	 * @param id
	 * @param name
	 * @param position
	 * @param graphics2D
	 */
	public AgentRole(boolean global, int id, String name, Point position, Graphics2D graphics2D) {
		super(global, id, name, position, graphics2D);
		setup_graphics();
	}

	/**
	 * @param agentRole
	 */
	public AgentRole(AgentRole agentRole) {
		super(agentRole);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#get_default_dimension()
	 */
	protected Dimension get_default_dimension() {
		return new Dimension( Constant._visualShellAgentRoleIconWidth, Constant._visualShellAgentRoleIconHeight);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#get_default_image_color()
	 */
	protected Color get_default_image_color() {
		return new Color( 255, 0, 0);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#get_default_text_color()
	 */
	protected Color get_default_text_color() {
		return new Color( 0, 0, 0);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_paste(soars.application.visualshell.layer.Layer)
	 */
	public boolean can_paste(Layer drawObjects) {
		if ( drawObjects.contains( this, "agent_role", _name)
			|| drawObjects.chartObject_has_same_name( _name, "")) {
			String[] message = new String[] {
				"Agent role",
				"name = " + _name
			};

			WarningManager.get_instance().add( message);
			return false;
		}
		return _ruleManager.can_paste( this, drawObjects);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#write(soars.common.utility.xml.sax.Writer)
	 */
	public boolean write(Writer writer) throws SAXException {
		return super.write( "agent_role", writer);
	}
}
