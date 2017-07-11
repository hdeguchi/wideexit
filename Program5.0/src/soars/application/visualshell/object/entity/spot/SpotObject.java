/*
 * 2005/04/20
 */
package soars.application.visualshell.object.entity.spot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.util.Vector;

import org.xml.sax.SAXException;

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
public class SpotObject extends EntityBase {

	/**
	 * @param id
	 * @param name
	 * @param position
	 * @param graphics2D
	 */
	public SpotObject(int id, String name, Point position, Graphics2D graphics2D) {
		super(id, name, position, graphics2D);
		setup_graphics();
	}

	/**
	 * @param global
	 * @param id
	 * @param name
	 * @param position
	 * @param initialRole
	 * @param graphics2D
	 */
	public SpotObject(boolean global, int id, String name, Point position, String initialRole, Graphics2D graphics2D) {
		super(global, id, name, position, initialRole, graphics2D);
		setup_graphics();
	}

	/**
	 * @param global
	 * @param id
	 * @param name
	 * @param position
	 * @param number
	 * @param initialRole
	 * @param imageFilename
	 * @param gis
	 * @param gisCoordinates
	 * @param graphics2D
	 */
	public SpotObject(boolean global, int id, String name, Point position, String number, String initialRole, String imageFilename, String gis, String[] gisCoordinates, Graphics2D graphics2D) {
		super(global, id, name, position, number, initialRole, imageFilename, gis, gisCoordinates, graphics2D);
		setup_graphics();
	}

	/**
	 * @param spotObject
	 */
	public SpotObject(SpotObject spotObject) {
		super(spotObject);
	}

	/**
	 * @param name
	 */
	public SpotObject(String name) {
		super(name);
	}

	/**
	 * @param id
	 * @param name
	 */
	public SpotObject(int id, String name) {
		super(id, name);
	}

	/**
	 * for GIS only!
	 */
	public SpotObject() {
		super();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#get_default_dimension()
	 */
	protected Dimension get_default_dimension() {
		return new Dimension( Constant._visualShellSpotIconWidth, Constant._visualShellSpotIconHeight);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#get_default_image_color()
	 */
	protected Color get_default_image_color() {
		return _global ? new Color( 0, 100, 0) : new Color( 0, 0, 255);
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
		String role = "Role : " + _initialRole;
		int width = fontMetrics.stringWidth( name) + name.length();
		width = Math.max( width, fontMetrics.stringWidth( role) + role.length());
		_nameDimension = new Dimension( width, ( fontMetrics.getHeight() + 1) * 2);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#draw_name(java.awt.Graphics2D)
	 */
	protected void draw_name(Graphics2D graphics2D) {
		graphics2D.drawString( "Name : " + get_name(),
			_position.x + ( _dimension.width - _nameDimension.width) / 2,
			_position.y + _dimension.height + ( _nameDimension.height / 2) - 2);
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
			graphics2D.draw3DRect( _position.x, _position.y, _dimension.width, _dimension.height, true);
		}
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_remove()
	 */
	public boolean can_remove() {
		String headName = SoarsCommonTool.separate( _name);
		String headNumber = _name.substring( headName.length());
		Vector<String[]> ranges = SoarsCommonTool.get_ranges( headNumber, _number);
		boolean result1 = LayerManager.get_instance().can_adjust_spot_name( headName, ranges, true);
		boolean result2 = can_remove_objects_names();
		return ( result1 && result2);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_paste(soars.application.visualshell.layer.Layer)
	 */
	public boolean can_paste(Layer drawObjects) {
		if ( drawObjects.contains( this, "spot", _name, _number)
			|| drawObjects.chartObject_has_same_name( _name, _number)) {
			String[] message = new String[] {
				"Spot",
				"name = " + _name
			};

			WarningManager.get_instance().add( message);
			return false;
		}

		boolean result1 = super.can_paste(drawObjects);
		boolean result2 = can_paste_initial_role( drawObjects);
		return ( result1 && result2);
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_initial_role(Layer drawObjects) {
		if ( !_initialRole.equals( "")) {
			if ( null == drawObjects.get_spot_role( _initialRole)) {
				String[] message = new String[] {
					"Spot",
					"name = " + _name,
					"initial role = " + _initialRole
				};

				WarningManager.get_instance().add( message);
				return false;
			}
		}
		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#write(soars.common.utility.xml.sax.Writer)
	 */
	public boolean write(Writer writer) throws SAXException {
		return super.write( "spot", writer);
	}
}
