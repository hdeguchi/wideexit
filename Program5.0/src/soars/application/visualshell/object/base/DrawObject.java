/*
 * 2005/05/13
 */
package soars.application.visualshell.object.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.Vector;

import javax.swing.JComponent;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.chart.ChartObject;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.spot.SpotRole;
import soars.application.visualshell.observer.Observer;
import soars.common.utility.xml.sax.Writer;

/**
 * The basic class for all objects(agent, spot, role, chart)
 * @author kurata / SOARS project
 */
public class DrawObject {

	/**
	 * This object's id.
	 */
	public int _id;

	/**
	 * This object's name.
	 */
	public String _name = "";

	/**
	 * Comment for this object.
	 */
	public String _comment = "";

	/**
	 * Global object
	 */
	public boolean _global = false;

	/**
	 * This object's position.
	 */
	public Point _position = null;

	/**
	 * Size of rectangle for this object.
	 */
	public Dimension _dimension = null;

	/**
	 * Size of rectangle for the name of this object.
	 */
	public Dimension _nameDimension = null;

	/**
	 * Color for this object.
	 */
	public Color _imageColor = null;

	/**
	 * Color for the name of this object.
	 */
	public Color _textColor = null;

	/**
	 * Image file name.
	 */
	public String _imageFilename = "";

	/**
	 * Selected color for this object.
	 */
	protected Color _selectedColor = null;

	/**
	 * True if this object is selected.
	 */
	protected boolean _selected = false;

	/**
	 * Creates this object with the specified data.
	 * @param id the specified id
	 * @param name the specified name
	 * @param position the specified position
	 * @param graphics2D the graphics object of JAVA
	 */
	public DrawObject(int id, String name, Point position, Graphics2D graphics2D) {
		super();
		_id = id;
		_name = name;
		_position = new Point( position);
		setup_name_dimension( graphics2D);
		_selectedColor = new Color( 255, 0, 0); 
	}

	/**
	 * @param global
	 * @param id
	 * @param name
	 * @param position
	 * @param graphics2d
	 */
	public DrawObject(boolean global, int id, String name, Point position, Graphics2D graphics2D) {
		super();
		_global = global;
		_id = id;
		_name = name;
		_position = new Point( position);
		setup_name_dimension( graphics2D);
		_selectedColor = new Color( 255, 0, 0); 
	}

	/**
	 * Creates this object with the specified data.
	 * @param global
	 * @param id the specified id
	 * @param name the specified name
	 * @param position the specified position
	 * @param imageFilename the image file name
	 * @param graphics2D the graphics object of JAVA
	 */
	public DrawObject(boolean global, int id, String name, Point position, String imageFilename, Graphics2D graphics2D) {
		super();
		_global = global;
		_id = id;
		_name = name;
		_position = new Point( position);
		_imageFilename = imageFilename;
		setup_name_dimension( graphics2D);
		_selectedColor = new Color( 255, 0, 0); 
	}

	/**
	 * Creates this object with the specified data.
	 * @param drawObject the specified object
	 */
	public DrawObject(DrawObject drawObject) {
		super();
		_global = drawObject._global;
		_id = drawObject._id;
		_name = drawObject._name;
		_comment = drawObject._comment;
		_position = new Point( drawObject._position);
		_dimension = new Dimension( drawObject._dimension);
		_nameDimension = new Dimension( drawObject._dimension);

		if ( null != drawObject._imageColor)
			_imageColor = new Color(
				drawObject._imageColor.getRed(),
				drawObject._imageColor.getGreen(),
				drawObject._imageColor.getBlue());

		_textColor = new Color(
			drawObject._textColor.getRed(),
			drawObject._textColor.getGreen(),
			drawObject._textColor.getBlue());

		_imageFilename = drawObject._imageFilename;

		_selectedColor = new Color(
			drawObject._selectedColor.getRed(),
			drawObject._selectedColor.getGreen(),
			drawObject._selectedColor.getBlue());
	}

	/**
	 * for GIS only!
	 */
	public DrawObject() {
		super();
	}

	/**
	 * Creates this object with the specified data.
	 * @param name the specified name
	 */
	public DrawObject(String name) {
		super();
		_id = LayerManager.get_instance().get_unique_id();
		_name = name;
	}

	/**
	 * Creates this object with the specified data.
	 * @param id the specified id
	 * @param name the specified name
	 */
	public DrawObject(int id, String name) {
		super();
		_id = id;
		_name = name;
	}

	/**
	 * Sets the size of rectangle for the name of this object.
	 * @param graphics2D the graphics object of JAVA
	 */
	public void setup_name_dimension(Graphics2D graphics2D) {
		FontMetrics fontMetrics = graphics2D.getFontMetrics();
		String name = get_name();
		_nameDimension = new Dimension(
			fontMetrics.stringWidth( name) + name.length(),
			//fontMetrics.stringWidth( _name) + _name.length(),
			fontMetrics.getHeight());
	}

	/**
	 * @return
	 */
	protected String get_name() {
		return _name;
	}

	/**
	 * 
	 */
	protected void setup_graphics() {
		_dimension = get_default_dimension();
		_imageColor = get_default_image_color();
		_textColor = get_default_text_color();
	}

	/**
	 * @return
	 */
	protected Dimension get_default_dimension() {
		return null;
	}

	/**
	 * @return
	 */
	protected Color get_default_image_color() {
		return null;
	}

	/**
	 * 
	 */
	public void set_default_image_color() {
		// TODO Auto-generated method stub
		_imageColor = get_default_image_color();
	}

	/**
	 * @return
	 */
	protected Color get_default_text_color() {
		return null;
	}

	/**
	 * Returns true if this is "Global object".
	 * @return true if this is "Global object"
	 */
	public boolean is_global_object() {
		return _global;
	}

	/**
	 * Creates the instance of this object with the specified data, and returns it.
	 * @param type the specified type
	 * @param id the specified id
	 * @param name the specified name
	 * @param point the specified position
	 * @param graphics2D the graphics object of JAVA
	 * @return the instance of this object
	 */
	public static DrawObject create(String type, int id, String name, Point point, Graphics2D graphics2D) {
		if ( type.equals( "agent"))
			return new AgentObject( id, name, point, graphics2D);
		else if ( type.equals( "spot"))
			return new SpotObject( id, name, point, graphics2D);
		else if ( type.equals( "agent_role"))
			return new AgentRole( id, name, point, graphics2D);
		else if ( type.equals( "spot_role"))
			return new SpotRole( id, name, point, graphics2D);
		else if ( type.equals( "chart"))
			return new ChartObject( id, name, point, graphics2D);

		return null;
	}

	/**
	 * Creates the instance of this object with the specified data, and returns it.
	 * @param type the specified type
	 * @param graphics2D the graphics object of JAVA
	 * @return the instance of this object
	 */
	public static DrawObject create(String type, Graphics2D graphics2D) {
		if ( type.equals( "spot"))
			return new SpotObject( true, LayerManager.get_instance().get_unique_id(), Constant._globalSpotName, new Point( 0, 0), Constant._globalRoleName, graphics2D);
		else if ( type.equals( "spot_role"))
			return new SpotRole( true, LayerManager.get_instance().get_unique_id(), Constant._globalRoleName, new Point( 0, 0), graphics2D);

		return null;
	}

	/**
	 * Creates the instance of this object with the specified data, and returns it.
	 * @param drawObject the specified data
	 * @return the instance of this object
	 */
	public static DrawObject create(DrawObject drawObject) {
		if ( drawObject instanceof AgentObject)
			return new AgentObject( ( AgentObject)drawObject);
		else if ( drawObject instanceof SpotObject)
			return new SpotObject( ( SpotObject)drawObject);
		else if ( drawObject instanceof AgentRole)
			return new AgentRole( ( AgentRole)drawObject);
		else if ( drawObject instanceof SpotRole)
			return new SpotRole( ( SpotRole)drawObject);
		else if ( drawObject instanceof ChartObject)
			return new ChartObject( ( ChartObject)drawObject);

		return null;
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
		_name = "";
		_position = null;
		_dimension = null;
		_nameDimension = null;
		_imageColor = null;
		_textColor = null;
		_selectedColor = null;
	}

	/**
	 * Renames to the specified name.
	 * @param name the specified name.
	 * @param graphics2D the graphics object of JAVA
	 */
	public void rename(String name, Graphics2D graphics2D) {
		_name = name;
		setup_name_dimension( graphics2D);
	}

	/**
	 * Gets the size of rectangle for this object.
	 * @param preferredSize the size of this object
	 */
	public void get_preferred_size(Dimension preferredSize) {
		int x = Math.max( _position.x + _dimension.width + 1,
			_position.x + ( _dimension.width + _nameDimension.width) / 2);
		int y = _position.y + _dimension.height + _nameDimension.height;
		if ( preferredSize.width < x)
			preferredSize.width = x;
		if ( preferredSize.height < y)
			preferredSize.height = y;
	}

	/**
	 * Returns true if this object doesn't have the specified id.
	 * @param id the specified id
	 * @return true if this object doesn't have the specified id
	 */
	public boolean is_unique(int id) {
		return ( _id != id);
	}

	/**
	 * Returns true if this object has the specified type and name.
	 * @param type the specified type
	 * @param name the specified name
	 * @return true if this object has the specified type and name
	 */
	public boolean is_same_name(String type, String name) {
		if ( this instanceof ChartObject)
			return _name.equals( name);

//		if ( ( type.equals( "agent") && !( this instanceof AgentObject))
//			|| ( type.equals( "spot") && !( this instanceof SpotObject))
		if ( ( ( type.equals( "agent") || type.equals( "spot")) && !( this instanceof EntityBase))
			|| ( type.equals( "agent_role") && !( this instanceof Role))
			|| ( type.equals( "spot_role") && !( this instanceof Role)))
			return false;

		return _name.equals( name);
	}

	/**
	 * Returns true if this object has the specified name and number.
	 * @param name the specified name
	 * @param number the specified number
	 * @return true if this object has the specified name and number
	 */
	public boolean has_same_name(String name, String number) {
		return false;
	}

	/**
	 * Returns true if this object has the specified full name.
	 * @param fullName the specified full name
	 * @return true if this object has the specified full name
	 */
	public boolean has_same_name(String fullName) {
		return false;
	}

	/**
	 * Sets the selection of this object.
	 * @param selected whether or not all objects on this layer are selected
	 */
	public void select(boolean selected) {
		_selected = selected;
	}

	/**
	 * Sets the selection of this object, true if this object's rectangle and the specified rectangle intersect.
	 * @param rectangle the specified rectangle
	 */
	public void select(Rectangle rectangle) {
		_selected = rectangle.intersects( new Rectangle( _position, _dimension));
	}

	/**
	 * Returns the selection of this object.
	 * @return the selection of this object
	 */
	public boolean selected() {
		return _selected;
	}

	/**
	 * Returns true if this object's rectangle contains the specified point.
	 * @param point the specified point
	 * @return true if this object's rectangle contains the specified point
	 */
	public boolean contains(Point point) {
		if ( get_rectangle().contains( point))
			return true;

		return false;
	}

	/**
	 * Returns this object's rectangle.
	 * @return this object's rectangle
	 */
	public Rectangle get_rectangle() {
		return new Rectangle(
			Math.min( _position.x,
				_position.x + ( _dimension.width - _nameDimension.width) / 2),
			_position.y,
			Math.max( _dimension.width, _nameDimension.width),
			_dimension.height + _nameDimension.height);
	}

	/**
	 * Moves this object.
	 * @param deltaX the x-coordinates of the moving
	 * @param deltaY the y-coordinates for moving
	 */
	public void move(int deltaX, int deltaY) {
		Point position = new Point( _position);

		position.x += deltaX;
		if ( 0 > position.x) {
			position.x = 0;
			deltaX = position.x - _position.x;
		}

		position.y += deltaY;
		if ( 0 > position.y) {
			position.y = 0;
			deltaY = position.y - _position.y;
		}

		_position.x += deltaX;
		_position.y += deltaY;

		Observer.get_instance().modified();
	}

	/**
	 * Returns true if the coordinates and the coordinates of this object are more than 0.
	 * @param deltaX the x-coordinates of the moving
	 * @param deltaY the y-coordinates for moving
	 * @return true if the coordinates and the coordinates of this object are more than 0
	 */
	public boolean test(int deltaX, int deltaY) {
		return ( 0 <= ( _position.x + deltaX) && ( 0 <= _position.y + deltaY));
	}

	/**
	 * Draws this object.
	 * @param graphics2D the graphics object of JAVA
	 * @param imageObserver an asynchronous update interface for receiving notifications about Image information as the Image is constructed
	 */
	public void draw(Graphics2D graphics2D, ImageObserver imageObserver) {
		graphics2D.setColor( _textColor);
		draw_name( graphics2D);

		if ( _selected)
			draw_frame( graphics2D);
	}

	/**
	 * @param graphics2D
	 */
	protected void draw_name(Graphics2D graphics2D) {
		graphics2D.drawString( get_name(),
			_position.x + ( _dimension.width - _nameDimension.width) / 2,
			_position.y + _dimension.height + _nameDimension.height - 2);
	}

	/**
	 * Draws the frame of this object.
	 * @param graphics2D the graphics object of JAVA
	 */
	public void draw_frame(Graphics2D graphics2D) {
		int side = 6;
		int[] x = {
			Math.min( _position.x,
				_position.x + ( _dimension.width - _nameDimension.width) / 2) - side,
			_position.x + ( ( _dimension.width - side) / 2),
			Math.max( _position.x + _dimension.width + 1,
				_position.x + ( _dimension.width + _nameDimension.width) / 2),
		};
		int[] y = {
			_position.y - side,
			_position.y + ( ( _dimension.height + _nameDimension.height - side) / 2),
			_position.y + _dimension.height + _nameDimension.height
		};

		graphics2D.setColor( _selectedColor);

		graphics2D.fill3DRect( x[ 0], y[ 0], side, side, false);
		graphics2D.fill3DRect( x[ 0], y[ 1], side, side, false);
		graphics2D.fill3DRect( x[ 0], y[ 2], side, side, false);
		graphics2D.fill3DRect( x[ 1], y[ 0], side, side, false);
		graphics2D.fill3DRect( x[ 1], y[ 2], side, side, false);
		graphics2D.fill3DRect( x[ 2], y[ 0], side, side, false);
		graphics2D.fill3DRect( x[ 2], y[ 1], side, side, false);
		graphics2D.fill3DRect( x[ 2], y[ 2], side, side, false);
	}

	/**
	 * Returns true if this object can be removed.
	 * @return true if this object can be removed
	 */
	public boolean can_remove() {
		return true;
	}

	/**
	 * Invoked when this object is pasted.
	 */
	public void on_paste() {
	}

	/**
	 * Returns true if the specified object's name can be adjusted.
	 * @param headName the prefix of the specified object's name
	 * @param ranges the ranges for the specified object's number
	 * @return true if the specified object's name can be adjusted
	 */
	public boolean can_adjust_agent_name(String headName, Vector<String[]> ranges) {
		return true;
	}

	/**
	 * Returns true if it is possible to update the specified agent name with the new one.
	 * @param headName the prefix of the specified agent name
	 * @param ranges the ranges for the specified agent number
	 * @param newHeadName the prefix of the new agent name
	 * @param newRanges the ranges for the new agent number
	 * @return true if it is possible to update the specified agent name with the new one
	 */
	public boolean can_adjust_agent_name(String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		return true;
	}

	/**
	 * Returns true if the specified object's name can be adjusted.
	 * @param headName the prefix of the specified object's name
	 * @param ranges the ranges for the specified object's number
	 * @return true if the specified object's name can be adjusted
	 */
	public boolean can_adjust_spot_name(String headName, Vector<String[]> ranges) {
		return true;
	}

	/**
	 * Returns true if it is possible to update the specified spot name with the new one.
	 * @param headName the prefix of the specified spot name
	 * @param ranges the ranges for the specified spot number
	 * @param newHeadName the prefix of the new spot name
	 * @param newRanges the ranges for the new spot number
	 * @return true if it is possible to update the specified spot name with the new one
	 */
	public boolean can_adjust_spot_name(String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		return true;
	}

	/**
	 * Returns true if the specified object can be removed.
	 * @param kind the kind of the specified object
	 * @param name the name of the specified object
	 * @param otherSpotsHaveThisObjectName true if other spot objects have the object which has the specified data
	 * @param headName the prefix of the agent or spot name
	 * @param ranges the ranges for the agent or spot number
	 * @return true if this object can be removed
	 */
	public boolean can_remove(String kind, String name, boolean otherSpotsHaveThisObjectName, String headName, Vector<String[]> ranges) {
		// TODO 従来のもの
		return true;
	}

	/**
	 * @param entityType
	 * @param kind
	 * @param objectName
	 * @param otherEntitiesHaveThisObjectName
	 * @param headName
	 * @param ranges
	 * @return
	 */
	public boolean can_remove(String entityType, String kind, String objectName, boolean otherEntitiesHaveThisObjectName, String headName, Vector<String[]> ranges) {
		// TODO これからはこちらに移行してゆく
		return true;
	}

	/**
	 * @param entityType
	 * @param numberObjectName
	 * @param newType
	 * @return
	 */
	public boolean is_number_object_type_correct(String entityType, String numberObjectName, String newType) {
		return true;
	}

	/**
	 * Returns true if the specified object can be removed.
	 * @param kind the kind of the specified object
	 * @param type the type of the number variable
	 * @param name the name of the specified object
	 * @param headName the prefix of the agent or spot name
	 * @param ranges the ranges for the agent or spot number
	 * @return true if the specified object can be removed
	 */
	public boolean can_remove(String kind, String type, String name, String headName, Vector<String[]> ranges) {
		return true;
	}

	/**
	 * Returns true if the specified role object can be removed.
	 * @param roleName the name of the specified role object
	 * @return true true if the specified role object can be removed
	 */
	public boolean can_remove_role_name(String roleName) {
		return true;
	}

	/**
	 * Returns true if it is possible to paste the specified objects.
	 * @param drawObjects the specified objects
	 * @return true if it is possible to paste the specified objects
	 */
	public boolean can_paste(Layer drawObjects) {
		return true;
	}

	/**
	 * Invoked when the specified agent name is updated.
	 * @param newName the new agent name
	 * @param originalName the original agent name
	 * @param headName the prefix of the specified agent name
	 * @param ranges the ranges for the specified agent number
	 * @param newHeadName the prefix of the new agent name
	 * @param newRanges the ranges for the new agent number
	 * @return
	 */
	public boolean update_agent_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		return false;
	}

	/**
	 * Invoked when the specified spot name is updated.
	 * @param newName the new spot name
	 * @param originalName the original spot name
	 * @param headName the prefix of the specified spot name
	 * @param ranges the ranges for the specified spot number
	 * @param newHeadName the prefix of the new spot name
	 * @param newRanges the ranges for the new spot number
	 * @return
	 */
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		return false;
	}

	/**
	 * Invoked when the specified role name is updated.
	 * @param originalName the new role name
	 * @param name the original role name
	 * @return
	 */
	public boolean update_role_name(String originalName, String name) {
		return false;
	}

	/**
	 * Invoked when the specified agent is removed.
	 * @param headName the prefix of the specified agent name
	 * @param ranges the ranges for the specified agent number
	 */
	public void on_remove_agent_name_and_number(String headName, Vector<String[]> ranges) {
	}

	/**
	 * Invoked when the specified spot is removed.
	 * @param headName the prefix of the specified spot name
	 * @param ranges the ranges for the specified spot number
	 */
	public void on_remove_spot_name_and_number(String headName, Vector<String[]> ranges) {
	}

	/**
	 * Invoked when the specified roles are removed.
	 * @param roleNames the names of the specified roles
	 */
	public void on_remove_role_name(Vector<String> roleNames) {
	}

	/**
	 * Invoked when the specified stages are removed.
	 * @param stageNames the names of the specified stages
	 */
	public void on_remove_stage_name(Vector<String> stageNames) {
	}

	/**
	 * Edits this object.
	 * @param component the base class for all Swing components
	 * @param frame the Frame from which the dialog is displayed
	 */
	public void edit(JComponent component, Frame frame) {
	}

	/**
	 * Returns the text of the initial data.
	 * @return the text of the initial data
	 */
	public String get_initial_data() {
		return "";
	}

	/**
	 * Returns true for writing this object data successfully.
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing this object data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(Writer writer) throws SAXException {
		return true;
	}

	/**
	 * @param name
	 * @param attributesImpl
	 */
	protected void write(String name, AttributesImpl attributesImpl) {
		attributesImpl.addAttribute( null, null, "id", "", String.valueOf( _id));
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( name));
		attributesImpl.addAttribute( null, null, "global", "", _global ? "true" : "false");
		attributesImpl.addAttribute( null, null, "x", "", String.valueOf( _position.x));
		attributesImpl.addAttribute( null, null, "y", "", String.valueOf( _position.y));
	}

	/**
	 * @param writer
	 * @throws SAXException
	 */
	protected void write_comment(Writer writer) throws SAXException {
		if ( _comment.equals( ""))
			return;

		writer.startElement( null, null, "comment", new AttributesImpl());
		writer.characters( _comment.toCharArray(), 0, _comment.length());
		writer.endElement( null, null, "comment");
	}
}
