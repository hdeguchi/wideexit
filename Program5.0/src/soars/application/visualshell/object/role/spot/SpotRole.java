/*
 * 2005/05/20
 */
package soars.application.visualshell.object.role.spot;

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
public class SpotRole extends Role {

	/**
	 * @param id
	 * @param name
	 * @param position
	 * @param graphics2D
	 */
	public SpotRole(int id, String name, Point position, Graphics2D graphics2D) {
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
	public SpotRole(boolean global, int id, String name, Point position, Graphics2D graphics2D) {
		super(global, id, name, position, graphics2D);
		setup_graphics();
	}

	/**
	 * @param spotRole
	 */
	public SpotRole(SpotRole spotRole) {
		super(spotRole);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#get_default_dimension()
	 */
	@Override
	protected Dimension get_default_dimension() {
		return new Dimension( Constant._visualShellSpotRoleIconWidth, Constant._visualShellSpotRoleIconHeight);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#get_default_image_color()
	 */
	@Override
	protected Color get_default_image_color() {
		return _global ? new Color( 0, 100, 0) : new Color( 0, 0, 255);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#get_default_text_color()
	 */
	@Override
	protected Color get_default_text_color() {
		return new Color( 0, 0, 0);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_paste(soars.application.visualshell.layer.Layer)
	 */
	@Override
	public boolean can_paste(Layer drawObjects) {
		if ( drawObjects.contains( this, "spot_role", _name)
			|| drawObjects.chartObject_has_same_name( _name, "")) {
			String[] message = new String[] {
				"Spot role",
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
	@Override
	public boolean write(Writer writer) throws SAXException {
		return super.write( "spot_role", writer);
	}
}
