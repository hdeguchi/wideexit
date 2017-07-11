/*
 * 2005/02/07
 */
package soars.application.animator.object.entity.agent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.animator.common.image.AnimatorImageManager;
import soars.application.animator.main.Environment;
import soars.application.animator.main.ResourceManager;
import soars.application.animator.main.internal.ObjectManager;
import soars.application.animator.object.common.tool.CommonObjectTool;
import soars.application.animator.object.entity.base.EntityBase;
import soars.application.animator.object.entity.base.edit.object.EditObjectDlg;
import soars.application.animator.object.entity.spot.ISpotObjectManipulator;
import soars.application.animator.setting.common.CommonProperty;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.xml.sax.Writer;

/**
 * The agent object class.
 * @author kurata / SOARS project
 */
public class AgentObject extends EntityBase {

	/**
	 * Spot for this agent.
	 */
	public ISpotObjectManipulator _spotObjectManipulator = null;

	/** 
	 * Creates the instance of agent object class.
	 * @param name the name of agent
	 * @param objectManager
	 */
	public AgentObject(String name, ObjectManager objectManager) {
		super(name, new Color( ( int)( 255 * Math.random()), 0, ( int)( 255 * Math.random())), new Color( 255, 0, 0), objectManager);
	}

	/** 
	 * Creates the instance of agent object class.
	 * @param name the name of agent
	 * @param objectManager
	 * @param graphics2D the graphics object of JAVA
	 */
	public AgentObject(String name, ObjectManager objectManager, Graphics2D graphics2D) {
		super(name, new Color( ( int)( 255 * Math.random()), 0, ( int)( 255 * Math.random())), new Color( 255, 0, 0), objectManager);
		Font font = graphics2D.getFont();
		_font = new Font( font.getFamily(), font.getStyle(), font.getSize());
		setup_dimension( graphics2D);
	}

	/** Copy constructor(For duplication)
	 * @param agentObject
	 * @param objectManager
	 */
	public AgentObject(AgentObject agentObject, ObjectManager objectManager) {
		super(agentObject, objectManager);
	}

	/**
	 * Sets the new name.
	 * @param name the new name
	 * @param graphics2D the graphics object of JAVA
	 */
	public void rename(String name, Graphics2D graphics2D) {
		_name = name;
		setup_dimension( graphics2D);
	}

	/**
	 * Returns true if this agent is initialized successfully.
	 * @param graphics2D the graphics object of JAVA
	 * @return true if this agent is initialized successfully
	 */
	public boolean setup(Graphics2D graphics2D) {
		Font font = graphics2D.getFont();
		graphics2D.setFont( _font);
		setup_dimension( graphics2D);
		graphics2D.setFont( font);
		return true;
	}

	/**
	 * @param graphics2D
	 */
	private void setup_dimension(Graphics2D graphics2D) {
		FontMetrics fontMetrics = graphics2D.getFontMetrics();
		_nameDimension = new Dimension(
			_visibleName ? ( fontMetrics.stringWidth( _name) + _name.length()) : 0,
			_visibleName ? fontMetrics.getHeight() : 0);
		_dimension.setSize(
			Math.max( CommonProperty.get_instance()._agentWidth, _nameDimension.width),
			CommonProperty.get_instance()._agentHeight + _nameDimension.height);
		_imageDimension.setSize(
			CommonProperty.get_instance()._agentWidth,
			CommonProperty.get_instance()._agentHeight);
	}

	/**
	 * Updates this agent.
	 */
	public void update_dimension() {
		//if ( null != _image)
		if ( !_imageFilename.equals( ""))
			return;

		_dimension.setSize(
			Math.max( CommonProperty.get_instance()._agentWidth, _nameDimension.width),
			CommonProperty.get_instance()._agentHeight + _nameDimension.height);
		_imageDimension.setSize(
			CommonProperty.get_instance()._agentWidth,
			CommonProperty.get_instance()._agentHeight);
	}

	/**
	 * Moves this agent.
	 * @param deltaX the x-coordinates of the moving
	 * @param deltaY the y-coordinates of the moving
	 */
	public void move(int deltaX, int deltaY) {
		_position.x += deltaX;
		_position.y += deltaY;
	}

	/**
	 * Sets the spot for this agent.
	 * @param spotObjectManipulator the spot for this agent
	 */
	public void set(ISpotObjectManipulator spotObjectManipulator) {
		_spotObjectManipulator = spotObjectManipulator;
	}

	/**
	 * Appends this agent to the spot.
	 */
	public void append() {
		if ( null == _spotObjectManipulator)
			return;

		_spotObjectManipulator.append( this);
	}

	/**
	 * Updates the position of this agent.
	 */
	public void arrange() {
		if ( null == _spotObjectManipulator)
			return;

		Point position = _spotObjectManipulator.get_agent_position( this, false);
		if ( null == position)
			return;

		_position.setLocation( position);
	}

	/**
	 * Gets the maximum size of the area for drawing.
	 * @param dimension the maximum size of the area for drawing
	 * @param agentPropertyMaxDimension the maximum size for agent property
	 */
	public void get_size(Dimension dimension, Dimension agentPropertyMaxDimension) {
		//if ( !_visible)
		//	return new Dimension();

		dimension.width = Math.max( _dimension.width, agentPropertyMaxDimension.width);
		dimension.height = Math.max( _dimension.height, agentPropertyMaxDimension.height);
	}

	/**
	 * Returns true if the specified image is set to this agent successfully.
	 * @param filename the specified image file name
	 * @return true if the specified image is set to this agent successfully
	 */
	public boolean load_image(String filename) {
		if ( null == filename || filename.equals( "")) {
			_dimension.setSize(
				Math.max( CommonProperty.get_instance()._agentWidth, _nameDimension.width),
				CommonProperty.get_instance()._agentHeight + _nameDimension.height);
			_imageDimension.setSize(
				CommonProperty.get_instance()._agentWidth,
				CommonProperty.get_instance()._agentHeight);
			_imageFilename = "";
			return false;
		}

		BufferedImage bufferedImage = AnimatorImageManager.get_instance().load_image( filename);
		if ( null == bufferedImage)
			return false;

		_dimension.setSize(
			Math.max( bufferedImage.getWidth(), _nameDimension.width),
			bufferedImage.getHeight() + _nameDimension.height);
		_imageDimension.setSize(
			bufferedImage.getWidth(),
			bufferedImage.getHeight());
		_imageFilename = filename;
		return true;
	}

	/**
	 * Returns true if the specified resource image is set to this agent successfully.
	 * @param filename the specified resource image file name
	 * @return true if the specified resource image is set to this agent successfully
	 */
	public boolean load_image_from_resource(String filename) {
		if ( null == filename || filename.equals( "")) {
			_dimension.setSize(
				Math.max( CommonProperty.get_instance()._agentWidth, _nameDimension.width),
				CommonProperty.get_instance()._agentHeight + _nameDimension.height);
			_imageDimension.setSize(
				CommonProperty.get_instance()._agentWidth,
				CommonProperty.get_instance()._agentHeight);
			_imageFilename = "";
			return false;
		}

		BufferedImage bufferedImage = AnimatorImageManager.get_instance().load_image_from_resource( filename, getClass());
		if ( null == bufferedImage)
			return false;

		_dimension.setSize(
			Math.max( bufferedImage.getWidth(), _nameDimension.width),
			bufferedImage.getHeight() + _nameDimension.height);
		_imageDimension.setSize(
			bufferedImage.getWidth(),
			bufferedImage.getHeight());
		_imageFilename = filename;
		return true;
	}

	/**
	 * Returns true if this agent uses the specified image file.
	 * @param filename the specified image file name
	 * @return true if this agent uses the specified image file
	 */
	public boolean uses_this_image(String filename) {
		return filename.equals( _imageFilename);
	}

	/**
	 * Sets the specified new image if this agent uses the specified image.
	 * @param originalFilename the specified image file name
	 * @param newFilename the specified new image file name
	 */
	public void update_image(String originalFilename, String newFilename) {
		if ( originalFilename.equals( _imageFilename))
			_imageFilename = newFilename;
	}

	/**
	 * Draws this agent.
	 * @param graphics2D the graphics object of JAVA
	 * @param rectangle the visible rectangle
	 * @param imageObserver an asynchronous update interface for receiving notifications about Image information as the Image is constructed
	 */
	public void draw(Graphics2D graphics2D, Rectangle rectangle, ImageObserver imageObserver) {
		if ( !_visible)
			return;

		if ( null == _spotObjectManipulator || !_spotObjectManipulator.is_visible())
			return;

		BufferedImage bufferedImage = AnimatorImageManager.get_instance().load_image( _imageFilename);
		if ( null != bufferedImage) {
			draw_image( rectangle, graphics2D, bufferedImage, _position, imageObserver);
			_objectManager._scenarioManager._agentPropertyManager.draw_current_property_image( graphics2D, _position, CommonProperty.get_instance()._agentWidth, CommonProperty.get_instance()._agentHeight, _properties, rectangle, imageObserver, true);
		} else {
			if ( !_objectManager._scenarioManager._agentPropertyManager.draw_current_property_image( graphics2D, _position, CommonProperty.get_instance()._agentWidth, CommonProperty.get_instance()._agentHeight, _properties, rectangle, imageObserver, false))
				draw_default_image( rectangle, graphics2D, _position, CommonProperty.get_instance()._agentWidth, CommonProperty.get_instance()._agentHeight);
		}

		if ( _visibleName)
			draw_name( rectangle, graphics2D, _position.x, _position.y + _imageDimension.height, _position.x, _position.y + _dimension.height - 2);

		if ( _selected && rectangle.intersects( new Rectangle( _position, _dimension)))
			CommonObjectTool.draw_frame( graphics2D, _position, _dimension, _selectedColor);
	}

//	/**
//	 * @param agentPropertyMaxDimension
//	 * @param imageObserver
//	 */
//	public void prepare_for_animation(Dimension agentPropertyMaxDimension, ImageObserver imageObserver) {
//		if ( !_visible)
//			return;
//
//		// Pack agents
//		Point position = ScenarioManager.get_instance().get_position( this, 0, false);
//		if ( null != position)
//			_position.setLocation( position);
//
//		_bufferedImage = new BufferedImage(
//			Math.max( _dimension.width, agentPropertyMaxDimension.width),
//			Math.max( _dimension.height, agentPropertyMaxDimension.height),
//			BufferedImage.TYPE_INT_ARGB);
//		Graphics2D graphics2D = ( Graphics2D)_bufferedImage.getGraphics();
//		Image image = AnimatorImageManager.get_instance().load_image( _imageFilename);
//		if ( null != image)
//			graphics2D.drawImage( image, 0, 0, imageObserver);
//		else {
//			if ( !AgentPropertyManager.get_instance().exist_selected_property()) {
//				draw_default_image( graphics2D, 0, 0, CommonProperty.get_instance()._agentWidth, CommonProperty.get_instance()._agentHeight);
//			}
//		}
//
//		if ( _visibleName)
//			draw_name( graphics2D, 0, _dimension.height - 2);
//
//		graphics2D.dispose();
//	}

	/**
	 * Animates this agent.
	 * @param graphics2D the graphics object of JAVA
	 * @param rectangle the visible rectangle
	 * @param imageObserver an asynchronous update interface for receiving notifications about Image information as the Image is constructed
	 */
	public void animate(Graphics2D graphics2D, Rectangle rectangle, ImageObserver imageObserver) {
		if ( !_visible)
			return;

		Point position = _objectManager._scenarioManager.get_current_position( this);
		if ( null == position)
			return;

//		if ( null != _bufferedImage) {
//			graphics2D.drawImage( _bufferedImage, position.x, position.y, imageObserver);
//
//			Image image = AnimatorImageManager.get_instance().load_image( _imageFilename);
//			if ( null != image) {
//				ScenarioManager.get_instance().draw_current_agent_property_image( this, graphics2D, position, CommonProperty.get_instance()._agentWidth, CommonProperty.get_instance()._agentHeight, rectangle, imageObserver, true);
//			} else {
//				if ( !ScenarioManager.get_instance().draw_current_agent_property_image( this, graphics2D, position, CommonProperty.get_instance()._agentWidth, CommonProperty.get_instance()._agentHeight, rectangle, imageObserver, false))
//					draw_default_image( graphics2D, position.x, position.y, CommonProperty.get_instance()._agentWidth, CommonProperty.get_instance()._agentHeight);
//			}
//		} else {
			BufferedImage bufferedImage = AnimatorImageManager.get_instance().load_image( _imageFilename);
			if ( null != bufferedImage) {
				draw_image( rectangle, graphics2D, bufferedImage, position, imageObserver);
				_objectManager._scenarioManager.draw_current_agent_property_image( this, graphics2D, position, CommonProperty.get_instance()._agentWidth, CommonProperty.get_instance()._agentHeight, rectangle, imageObserver, true);
			} else {
				if ( !_objectManager._scenarioManager.draw_current_agent_property_image( this, graphics2D, position, CommonProperty.get_instance()._agentWidth, CommonProperty.get_instance()._agentHeight, rectangle, imageObserver, false))
					draw_default_image( rectangle, graphics2D, position, CommonProperty.get_instance()._agentWidth, CommonProperty.get_instance()._agentHeight);
			}

			if ( _visibleName)
				draw_name( rectangle, graphics2D, position.x, position.y + _imageDimension.height, position.x, position.y + _dimension.height - 2);
//		}
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.base.EntityBase#draw_default_image(java.awt.Graphics2D, int, int, int, int)
	 */
	protected void draw_default_image(Graphics2D graphics2D, int x, int y,int width, int height) {
		graphics2D.setColor( _imageColor);
		graphics2D.fillOval( x, y, width, height);
	}

	/**
	 * Returns true if the specified point is on this agent.
	 * @param point the specified point
	 * @param state the current state
	 * @return true if the specified point is on this agent
	 */
	public boolean contains(Point point, String state) {
		Point position = null;
		if ( state.equals( "animation"))
			position = _objectManager._scenarioManager.get_current_position( this);
		else if ( state.equals( "edit")) {
			if ( null == _spotObjectManipulator)
				return false;

			position = _spotObjectManipulator.get_agent_position( this, false);
		}

		if ( null == position)
			return false;

		Rectangle rect = new Rectangle( position, _imageDimension);
		return rect.contains( point);
//		Rectangle rect1 = new Rectangle( position, _imageDimension);
//		Rectangle rect2 = new Rectangle( position.x, position.y + _imageDimension.height, _nameDimension.width, _nameDimension.height);
//		return ( rect1.contains( point) || rect2.contains( point));
	}

	/**
	 * Returns true if this agent is modified.
	 * @param frame the Frame from which the dialog is displayed
	 * @return true if this agent is modified
	 */
	public boolean edit(Frame frame) {
		EditObjectDlg editObjectDlg = new EditObjectDlg( frame,
			ResourceManager.get_instance().get( "edit.agent.dialog.title"),
			true, Environment._openAgentImageDirectoryKey, this);
		if ( !editObjectDlg.do_modal())
			return false;

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.base.EntityBase#update(boolean, boolean, int, int, int, int, int, int, java.lang.String, int, int, java.lang.String, java.awt.Graphics2D)
	 */
	public boolean update(boolean visible, boolean visibleName, int imageR,
		int imageG, int imageB, int textR, int textG, int textB,
		String fontFamily, int fontStyle, int fontSize,
		String imageFilename, Graphics2D graphics2D) {
		if ( !super.update( visible, visibleName, imageR, imageG, imageB,
			textR, textG, textB, fontFamily, fontStyle, fontSize, imageFilename, graphics2D))
			return true;

		BufferedImage bufferedImage = AnimatorImageManager.get_instance().load_image( _imageFilename);
		if ( null == bufferedImage) {
			_dimension.setSize(
				Math.max( CommonProperty.get_instance()._agentWidth, _nameDimension.width),
				CommonProperty.get_instance()._agentHeight + _nameDimension.height);
			_imageDimension.setSize(
				CommonProperty.get_instance()._agentWidth,
				CommonProperty.get_instance()._agentHeight);
		} else {
			_dimension.setSize(
				Math.max( bufferedImage.getWidth(), _nameDimension.width),
				bufferedImage.getHeight() + _nameDimension.height);
			_imageDimension.setSize(
				bufferedImage.getWidth(),
				bufferedImage.getHeight());
		}

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.base.EntityBase#select(boolean)
	 */
	public void select(boolean selected) {
		if ( selected) {
			if ( null == _spotObjectManipulator)
				return;

			if ( !_spotObjectManipulator.is_visible())
				return;
		}

		super.select(selected);
	}

	/**
	 * Selects if this agents is contained the specified rectangle.
	 * @param rectangle the specified rectangle
	 */
	public void select(Rectangle rectangle) {
		if ( null == _spotObjectManipulator)
			return;

		if ( !_spotObjectManipulator.is_visible()) {
			_selected = false;
			return;
		}

		_selected = rectangle.intersects( new Rectangle( _position, _imageDimension));
//		_selected = ( rectangle.intersects( new Rectangle( _position, _imageDimension))
//			|| rectangle.intersects( new Rectangle( _position.x, _position.y + _imageDimension.height, _nameDimension.width, _nameDimension.height)));
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.base.EntityBase#update_image(java.lang.String)
	 */
	protected boolean update_image(String imageFilename) {
		return load_image( imageFilename);
	}

	/**
	 * @param name
	 * @param number 
	 * @param filename 
	 */
	public void set_image(String name, String number, String filename) {
		// TODO これは根深い問題
		// 果たしてこれで良いのか？
		if ( number.equals( "")) {
			if ( !_name.equals( name)) {
				String headName1 = SoarsCommonTool.separate( _name);
				String headName2 = SoarsCommonTool.separate( name);
				if ( !headName1.equals( headName2))
					return;
			}
//			if ( !_name.equals( name))
//				return;
		} else {
			if ( !SoarsCommonTool.has_same_name( name, String.valueOf( number), _name))
				return;
		}
		load_image( filename);
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.base.EntityBase#get_tooltip_text(java.lang.String)
	 */
	public String get_tooltip_text(String state) {
		if ( state.equals( "animation"))
			return _name + _objectManager._scenarioManager.get_tooltip_text( this, _objectManager._scenarioManager._agentTransitionManager, _objectManager._scenarioManager._agentPropertyManager.get_selected_properties());
		else if ( state.equals( "edit"))
			return get_tooltip_text( _objectManager._scenarioManager._agentPropertyManager.get_selected_properties());
		else
			return null;
	}

	/**
	 * Returns true for writing this agent data successfully.
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing this agent data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		if ( !super.write( attributesImpl))
			return false;

		writer.writeElement( null, null, "agent", attributesImpl);

		return true;
	}

	/**
	 * Returns true for writing this agent graphic data successfully.
	 * @param rootDirectory the root directory for Animator data
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing this agent data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write_graphic_data(File rootDirectory, Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		if ( !super.write_graphic_data( rootDirectory, attributesImpl))
			return false;

		writer.writeElement( null, null, "agent", attributesImpl);

		return true;
	}
}
