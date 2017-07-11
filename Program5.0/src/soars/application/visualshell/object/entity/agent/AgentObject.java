/*
 * 2005/04/20
 */
package soars.application.visualshell.object.entity.agent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 */
public class AgentObject extends EntityBase {

	/**
	 * 
	 */
	public String _initialSpot = "";

	/**
	 * @param id
	 * @param name
	 * @param position
	 * @param graphics2D
	 */
	public AgentObject(int id, String name, Point position, Graphics2D graphics2D) {
		super(id, name, position, graphics2D);
		setup_graphics();
	}

	/**
	 * @param global
	 * @param id
	 * @param name
	 * @param position
	 * @param number
	 * @param initialRole
	 * @param initialSpot
	 * @param imageFilename
	 * @param gis
	 * @param gisCoordinates
	 * @param graphics2D
	 */
	public AgentObject(boolean global, int id, String name, Point position, String number, String initialRole, String initialSpot, String imageFilename, String gis, String[] gisCoordinates, Graphics2D graphics2D) {
		super(global, id, name, position, number, initialRole, imageFilename, gis, gisCoordinates, graphics2D);
		_initialSpot = initialSpot;
		setup_graphics();
	}

	/**
	 * @param agentObject
	 */
	public AgentObject(AgentObject agentObject) {
		super(agentObject);
		_initialSpot = agentObject._initialSpot;
	}

	/**
	 * @param id
	 * @param name
	 */
	public AgentObject(int id, String name) {
		super(id, name);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#get_default_dimension()
	 */
	protected Dimension get_default_dimension() {
		return new Dimension( Constant._visualShellAgentIconWidth, Constant._visualShellAgentIconHeight);
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

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#setup_name_dimension(java.awt.Graphics2D)
	 */
	public void setup_name_dimension(Graphics2D graphics2D) {
		FontMetrics fontMetrics = graphics2D.getFontMetrics();
		String name = "Name : " + get_name();
		String spot = "Spot : " + _initialSpot;
		String role = "Role : " + _initialRole;
		int width = fontMetrics.stringWidth( name) + name.length();
		width = Math.max( width, fontMetrics.stringWidth( spot) + spot.length());
		width = Math.max( width, fontMetrics.stringWidth( role) + role.length());
		_nameDimension = new Dimension( width, ( fontMetrics.getHeight() + 1) * 3);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#draw_name(java.awt.Graphics2D)
	 */
	protected void draw_name(Graphics2D graphics2D) {
		graphics2D.drawString( "Name : " + get_name(),
			_position.x + ( _dimension.width - _nameDimension.width) / 2,
			_position.y + _dimension.height + ( _nameDimension.height / 3) - 2);
		graphics2D.drawString( "Spot : " + _initialSpot,
			_position.x + ( _dimension.width - _nameDimension.width) / 2,
			_position.y + _dimension.height + ( 2 * _nameDimension.height / 3) - 2);
		graphics2D.drawString( "Role : " + _initialRole,
			_position.x + ( _dimension.width - _nameDimension.width) / 2,
			_position.y + _dimension.height + _nameDimension.height - 2);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#draw(java.awt.Graphics2D, java.awt.image.ImageObserver)
	 */
	public void draw(Graphics2D graphics2D, ImageObserver imageObserver) {
		super.draw(graphics2D, imageObserver);
		if ( !draw_image( graphics2D, imageObserver)) {
			graphics2D.setColor( _imageColor);
			graphics2D.drawOval( _position.x, _position.y, _dimension.width, _dimension.height);
		}
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_adjust_spot_name(java.lang.String, java.util.Vector)
	 */
	public boolean can_adjust_spot_name(String headName, Vector<String[]> ranges) {
		boolean result1 = true;
		if ( SoarsCommonTool.has_same_name( headName, ranges, _initialSpot)) {
			String[] message = new String[] {
				"Agent",
				"name = " + _name,
				"initial spot = " + _initialSpot
			};

			WarningManager.get_instance().add( message);

			result1 = false;
		}

		boolean result2 = super.can_adjust_spot_name( headName, ranges);

		return ( result1 && result2);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_adjust_spot_name(java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean can_adjust_spot_name(String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result1 = true;
		if ( SoarsCommonTool.has_same_name( headName, ranges, _initialSpot)
			&& !SoarsCommonTool.has_same_name( newHeadName, newRanges, _initialSpot)) {
			String[] message = new String[] {
				"Agent",
				"name = " + _name,
				"initial spot = " + _initialSpot
			};

			WarningManager.get_instance().add( message);

			result1 = false;
		}

		boolean result2 = super.can_adjust_spot_name( headName, ranges, newHeadName, newRanges);

		return ( result1 && result2);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_remove()
	 */
	public boolean can_remove() {
		String headName = SoarsCommonTool.separate( _name);
		String headNumber = _name.substring( headName.length());
		Vector<String[]> ranges = SoarsCommonTool.get_ranges( headNumber, _number);
		boolean result1 = LayerManager.get_instance().can_adjust_agent_name( headName, ranges, true);
		boolean result2 = can_remove_objects_names();
		return ( result1 && result2);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_paste(soars.application.visualshell.layer.Layer)
	 */
	public boolean can_paste(Layer drawObjects) {
		if ( drawObjects.contains( this, "agent", _name, _number)
			|| drawObjects.chartObject_has_same_name( _name, _number)) {
			String[] message = new String[] {
				"Agent",
				"name = " + _name
			};

			WarningManager.get_instance().add( message);
			return false;
		}

		boolean result1 = super.can_paste(drawObjects);
		boolean result2 = can_paste_initial_role( drawObjects);
		boolean result3 = can_paste_initial_spot( drawObjects);
		return ( result1 && result2 && result3);
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_initial_role(Layer drawObjects) {
		if ( !_initialRole.equals( "")) {
			if ( null == drawObjects.get_agent_role( _initialRole)) {
				String[] message = new String[] {
					"Agent",
					"name = " + _name,
					"initial role = " + _initialRole
				};

				WarningManager.get_instance().add( message);
				return false;
			}
		}
		return true;
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_initial_spot(Layer drawObjects) {
		if ( !_initialSpot.equals( "")) {
			if ( null == drawObjects.get_spot_has_this_name( _initialSpot)) {
				String[] message = new String[] {
					"Agent",
					"name = " + _name,
					"initial spot = " + _initialSpot
				};

				WarningManager.get_instance().add( message);
				return false;
			}
		}
		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = false;
		if ( SoarsCommonTool.has_same_name( headName, ranges, _initialSpot)
			&& !SoarsCommonTool.has_same_name( newHeadName, newRanges, _initialSpot)) {
			_initialSpot = newName + _initialSpot.substring( originalName.length());
			result = true;
		}

		return ( result || super.update_spot_name_and_number( newName, originalName, headName, ranges, newHeadName, newRanges));
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#on_remove_spot_name_and_number(java.lang.String, java.util.Vector)
	 */
	public void on_remove_spot_name_and_number(String headName, Vector<String[]> ranges) {
		if ( !SoarsCommonTool.has_same_name( headName, ranges, _initialSpot))
			return;

		_initialSpot = "";
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.entity.base.EntityBase#on_get_script()
	 */
	protected String on_get_script() {
		if ( _initialSpot.equals( ""))
			return "";

		return ( "\t<" + _initialSpot + ">moveTo");
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#write(soars.common.utility.xml.sax.Writer)
	 */
	public boolean write(Writer writer) throws SAXException {
		return super.write( "agent", writer);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.entity.base.EntityBase#on_write(org.xml.sax.helpers.AttributesImpl)
	 */
	protected void on_write(AttributesImpl attributesImpl) {
		attributesImpl.addAttribute( null, null, "initial_spot", "", Writer.escapeAttributeCharData( _initialSpot));
	}
}
