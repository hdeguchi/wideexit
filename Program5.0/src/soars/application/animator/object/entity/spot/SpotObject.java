/*
 * 2005/02/07
 */
package soars.application.animator.object.entity.spot;

import java.awt.Color;
import java.awt.Cursor;
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
import java.util.Map;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.animator.common.image.AnimatorImageManager;
import soars.application.animator.main.Environment;
import soars.application.animator.main.ResourceManager;
import soars.application.animator.main.internal.ObjectManager;
import soars.application.animator.object.common.tool.CommonObjectTool;
import soars.application.animator.object.entity.agent.AgentObject;
import soars.application.animator.object.entity.agent.AgentObjectManager;
import soars.application.animator.object.entity.base.EntityBase;
import soars.application.animator.object.entity.base.edit.object.EditImageObjectDlg;
import soars.application.animator.object.entity.base.edit.object.EditObjectDlg;
import soars.application.animator.observer.Observer;
import soars.application.animator.setting.common.CommonProperty;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.xml.sax.Writer;

/**
 * The spot object class.
 * @author kurata / SOARS project
 */
public class SpotObject extends EntityBase implements ISpotObjectManipulator {

	/**
	 * True if this object has only image.
	 */
	public boolean _imageObject = false;

	/**
	 * Parent of this Spot object.
	 */
	public ISpotObjectManipulator _parent = null;

	/**
	 * Position of image.
	 */
	public Point _imagePosition = new Point();

	/**
	 * Array of agents on this spot.
	 */
	public Vector<AgentObject> _agents = new Vector<AgentObject>();

	/**
	 * 縮小時の一辺最小長
	 */
	private int _imageMinimumSide = 2;

	/** 
	 * Creates the instance of spot object class.
	 * @param name the name of spot
	 * @param parent the parent of this spot
	 * @param objectManager
	 */
	public SpotObject(String name, ISpotObjectManipulator parent, ObjectManager objectManager) {
		super(name, new Color( 0, ( int)( 255 * Math.random()), ( int)( 255 * Math.random())), new Color( 0, 0, 255), objectManager);
		_parent = parent;
	}

	/** 
	 * Creates the instance of spot object class.
	 * @param name the name of spot
	 * @param parent the parent of this spot
	 * @param objectManager
	 * @param graphics2D the graphics object of JAVA
	 */
	public SpotObject(String name, ISpotObjectManipulator parent, ObjectManager objectManager, Graphics2D graphics2D) {
		super(name, new Color( 0, ( int)( 255 * Math.random()), ( int)( 255 * Math.random())), new Color( 0, 0, 255), objectManager);
		_parent = parent;
		Font font = graphics2D.getFont();
		_font = new Font( font.getFamily(), font.getStyle(), font.getSize());
		setup_dimension( graphics2D);
	}

	/** 
	 * Creates the instance of spot object class.
	 * @param name the name of spot
	 * @param visibleName true if the name string is visible
	 * @param position the position of this spot
	 * @param imageObject true if this object has only image
	 * @param parent the parent of this spot
	 * @param objectManager
	 * @param graphics2D the graphics object of JAVA
	 */
	public SpotObject(String name, boolean visibleName, Point position, boolean imageObject, ISpotObjectManipulator parent, ObjectManager objectManager, Graphics2D graphics2D) {
		super(name, new Color( 0, ( int)( 255 * Math.random()), ( int)( 255 * Math.random())), new Color( 0, 0, 255), objectManager);
		_visibleName = visibleName;
		_imageObject = imageObject;
		_parent = parent;
		Font font = graphics2D.getFont();
		_font = new Font( font.getFamily(), font.getStyle(), font.getSize());
		setup_dimension( graphics2D);
		set_position( position.x, position.y);
	}

	/** Copy constructor(For duplication)
	 * @param spotObject
	 * @param parent
	 * @param objectManager
	 */
	public SpotObject(SpotObject spotObject, ISpotObjectManipulator parent, ObjectManager objectManager) {
		super(spotObject, objectManager);
		_imageObject = spotObject._imageObject;
		_parent = parent;
		if ( null != spotObject._imagePosition)
			_imagePosition = new Point( spotObject._imagePosition);
		_imageMinimumSide = spotObject._imageMinimumSide;
	}

	/**
	 * @param agentObjectManager
	 * @param srcSpotObject
	 */
	public void adjust_for_duplication(AgentObjectManager agentObjectManager, SpotObject srcSpotObject) {
		for ( AgentObject agentObject:srcSpotObject._agents)
			_agents.add( agentObjectManager.get( agentObject._name));
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#is_image_object()
	 */
	public boolean is_image_object() {
		return _imageObject;
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#setup(java.awt.Graphics2D)
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
			Math.max( CommonProperty.get_instance()._spotWidth, _nameDimension.width),
			CommonProperty.get_instance()._spotHeight + _nameDimension.height);
		_imageDimension.setSize(
			CommonProperty.get_instance()._spotWidth,
			CommonProperty.get_instance()._spotHeight);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#cleanup()
	 */
	public void cleanup() {
		super.cleanup();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#get_name()
	 */
	public String get_name() {
		return super.get_name();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#is_visible()
	 */
	public boolean is_visible() {
		return super._visible;
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#update_dimension()
	 */
	public void update_dimension() {
		//if ( null != _image)
		if ( !_imageFilename.equals( ""))
			return;

		_dimension.setSize(
			Math.max( CommonProperty.get_instance()._spotWidth, _nameDimension.width),
			CommonProperty.get_instance()._spotHeight + _nameDimension.height);
		_imageDimension.setSize(
			CommonProperty.get_instance()._spotWidth,
			CommonProperty.get_instance()._spotHeight);
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#append(soars.application.animator.object.entity.agent.AgentObject)
	 */
	public void append(AgentObject agentObject) {
		_agents.add( agentObject);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#get_position()
	 */
	public Point get_position() {
		return _position;
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#set_position(int, int)
	 */
	public void set_position(int x, int y) {
		_position.setLocation( x, y);
		_imagePosition.setLocation( x, y + _nameDimension.height);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#get_image_position()
	 */
	public Point get_image_position() {
		return _imagePosition;
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#get_dimension()
	 */
	public Dimension get_dimension() {
		return _dimension;
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#update_dimension(int, int, int)
	 */
	public void update_dimension(int cursorType, int deltaX, int deltaY) {
		if ( Cursor.NW_RESIZE_CURSOR == cursorType) {
			int width = adjust( _dimension.width - deltaX);
			int imageWidth = adjust( _imageDimension.width - deltaX);
			int height = adjust( _dimension.height - deltaY);
			int imageHeight = adjust( _imageDimension.height - deltaY);
			int x = _position.x + ( _imageMinimumSide >= width ? 0 : deltaX);
			int y = _position.y + ( _imageMinimumSide >= height ? 0 : deltaY);
			int imageX = _imagePosition.x + ( _imageMinimumSide >= imageWidth ? 0 : deltaX);
			int imageY = _imagePosition.y + ( _imageMinimumSide >= imageHeight ? 0 : deltaY);
			_position = new Point( 0 > x ? 0 : x, 0 > y ? 0 : y);
			_imagePosition = new Point( 0 > imageX ? 0 : imageX, 0 > imageY ? 0 : imageY);
			_dimension = new Dimension( 0 > x ? _dimension.width : width, 0 > y ? _dimension.height : height);
			_imageDimension = new Dimension( 0 > imageX ? _imageDimension.width : imageWidth, 0 > imageY ? _imageDimension.height : imageHeight);
		} else if ( Cursor.W_RESIZE_CURSOR == cursorType) {
			int width = adjust( _dimension.width - deltaX);
			int imageWidth = adjust( _imageDimension.width - deltaX);
			int x = _position.x + ( _imageMinimumSide >= width ? 0 : deltaX);
			int imageX = _imagePosition.x + ( _imageMinimumSide >= imageWidth ? 0 : deltaX);
			_position = new Point( 0 > x ? 0 : x, _position.y);
			_imagePosition = new Point( 0 > imageX ? 0 : imageX, _imagePosition.y);
			_dimension = new Dimension( 0 > x ? _dimension.width : width, _dimension.height);
			_imageDimension = new Dimension( 0 > imageX ? _imageDimension.width : imageWidth, _imageDimension.height);
		} else if (  Cursor.SW_RESIZE_CURSOR == cursorType) {
			int width = adjust( _dimension.width - deltaX);
			int imageWidth = adjust( _imageDimension.width - deltaX);
			int x = _position.x + ( _imageMinimumSide >= width ? 0 : deltaX);
			int imageX = _imagePosition.x + ( _imageMinimumSide >= imageWidth ? 0 : deltaX);
			_position = new Point( 0 > x ? 0 : x, _position.y);
			_imagePosition = new Point( 0 > imageX ? 0 : imageX, _imagePosition.y);
			_dimension = new Dimension( 0 > x ? _dimension.width : width, adjust( _dimension.height + deltaY));
			_imageDimension = new Dimension( 0 > imageX ? _imageDimension.width : imageWidth, adjust( _imageDimension.height + deltaY));
		} else if (  Cursor.N_RESIZE_CURSOR == cursorType) {
			int height = adjust( _dimension.height - deltaY);
			int imageHeight = adjust( _imageDimension.height - deltaY);
			int y = _position.y + ( _imageMinimumSide >= height ? 0 : deltaY);
			int imageY = _imagePosition.y + ( _imageMinimumSide >= imageHeight ? 0 : deltaY);
			_position = new Point( _position.x, 0 > y ? 0 : y);
			_imagePosition = new Point( _imagePosition.x, 0 > imageY ? 0 : imageY);
			_dimension = new Dimension( _dimension.width, 0 > y ? _dimension.height : height);
			_imageDimension = new Dimension( _imageDimension.width, 0 > imageY ? _imageDimension.height : imageHeight);
		} else if (  Cursor.S_RESIZE_CURSOR == cursorType) {
			_dimension = new Dimension( _dimension.width, adjust( _dimension.height + deltaY));
			_imageDimension = new Dimension( _imageDimension.width, adjust( _imageDimension.height + deltaY));
		} else if (  Cursor.NE_RESIZE_CURSOR == cursorType) {
			int height = adjust( _dimension.height - deltaY);
			int imageHeight = adjust( _imageDimension.height - deltaY);
			int y = _position.y + ( _imageMinimumSide >= height ? 0 : deltaY);
			int imageY = _imagePosition.y + ( _imageMinimumSide >= imageHeight ? 0 : deltaY);
			_position = new Point( _position.x, 0 > y ? 0 : y);
			_imagePosition = new Point( _imagePosition.x, 0 > imageY ? 0 : imageY);
			_dimension = new Dimension( adjust( _dimension.width + deltaX), 0 > y ? _dimension.height : height);
			_imageDimension = new Dimension( adjust( _imageDimension.width + deltaX), 0 > imageY ? _imageDimension.height : imageHeight);
		} else if (  Cursor.E_RESIZE_CURSOR == cursorType) {
			_dimension = new Dimension( adjust( _dimension.width + deltaX), _dimension.height);
			_imageDimension = new Dimension( adjust( _imageDimension.width + deltaX), _imageDimension.height);
		} else if (  Cursor.SE_RESIZE_CURSOR == cursorType) {
			_dimension = new Dimension( adjust( _dimension.width + deltaX), adjust( _dimension.height + deltaY));
			_imageDimension = new Dimension( adjust( _imageDimension.width + deltaX), adjust( _imageDimension.height + deltaY));
		}
		Observer.get_instance().modified();
	}

	/**
	 * @param value
	 * @return
	 */
	private int adjust(int value) {
		return ( ( _imageMinimumSide >= value) ? _imageMinimumSide : value);
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#restore_image_size()
	 */
	public void restore_image_size() {
		if ( !is_image_object() || _imageFilename.equals( ""))
			return;

		BufferedImage bufferedImage = AnimatorImageManager.get_instance().load_image( _imageFilename);
		if ( null == bufferedImage)
			return;

		_dimension.setSize( Math.max( bufferedImage.getWidth(), _nameDimension.width), _nameDimension.height + bufferedImage.getHeight());
		_imageDimension.setSize( bufferedImage.getWidth(), bufferedImage.getHeight());
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#update_preferred_size(java.awt.Dimension, java.awt.Dimension, java.awt.Dimension, int)
	 */
	public void update_preferred_size(Dimension preferredSize, Dimension spotPropertyMaxDimension, Dimension agentMaxDimension, int max) {
		get_size( preferredSize, spotPropertyMaxDimension, agentMaxDimension, max);
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#get_size(java.awt.Dimension, java.awt.Dimension, java.awt.Dimension, int)
	 */
	public void get_size(Dimension dimension, Dimension spotPropertyMaxDimension, Dimension agentMaxDimension, int max) {
		//if ( !_visible)
		//	return;

		int x = _position.x + Math.max(
			_dimension.width, spotPropertyMaxDimension.width);
		int y = _position.y + Math.max( _dimension.height,
			_nameDimension.height + spotPropertyMaxDimension.height);
		if ( dimension.width < x)
			dimension.width = x;
		if ( dimension.height < y)
			dimension.height = y;

		if ( 0 == max)
			return;

		int width = ( _imageDimension.width / max);
		if ( width < CommonProperty.get_instance()._agentWidth)
			width = ( ( 10 < CommonProperty.get_instance()._agentWidth)
				? 10 : CommonProperty.get_instance()._agentWidth);
		else if ( width > CommonProperty.get_instance()._agentWidth)
			width = CommonProperty.get_instance()._agentWidth;
//		if ( width < CommonProperty.get_instance()._minimumWidth)
//			width = CommonProperty.get_instance()._minimumWidth;

		x = _imagePosition.x + ( width * max) + agentMaxDimension.width;
		if ( x > dimension.width)
			dimension.width = x;
		y = _imagePosition.y + agentMaxDimension.height;
		if ( y > dimension.height)
			dimension.height = y;
	}

	/**
	 * Returns the maximum size of the area for drawing.
	 * @param propertyMaxDimension the maximum size for spot property
	 * @return the maximum size of the area for drawing
	 */
	public Dimension get_size(Dimension propertyMaxDimension) {
		//if ( !_visible)
		//	return new Dimension();

		return new Dimension(
			_position.x + Math.max( _dimension.width, propertyMaxDimension.width),
			_position.y + Math.max( _dimension.height, _nameDimension.height + propertyMaxDimension.height));
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#load_image(java.lang.String)
	 */
	public boolean load_image(String filename) {
		if ( null == filename || filename.equals( "")) {
			_dimension.setSize(
				Math.max( CommonProperty.get_instance()._spotWidth, _nameDimension.width),
				CommonProperty.get_instance()._spotHeight + _nameDimension.height);
			_imageDimension.setSize(
				CommonProperty.get_instance()._spotWidth,
				CommonProperty.get_instance()._spotHeight);
			_imageFilename = "";
			return false;
		}

		BufferedImage bufferedImage = AnimatorImageManager.get_instance().load_image( filename);
		if ( null == bufferedImage)
			return false;

		_dimension.setSize( Math.max( bufferedImage.getWidth(), _nameDimension.width), _nameDimension.height + bufferedImage.getHeight());
		_imageDimension.setSize( bufferedImage.getWidth(), bufferedImage.getHeight());
		_imageFilename = filename;
		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#load_image_from_resource(java.lang.String)
	 */
	public boolean load_image_from_resource(String filename) {
		if ( null == filename || filename.equals( "")) {
			_dimension.setSize(
				Math.max( CommonProperty.get_instance()._spotWidth, _nameDimension.width),
				CommonProperty.get_instance()._spotHeight + _nameDimension.height);
			_imageDimension.setSize(
				CommonProperty.get_instance()._spotWidth,
				CommonProperty.get_instance()._spotHeight);
			_imageFilename = "";
			return false;
		}

		BufferedImage bufferedImage = AnimatorImageManager.get_instance().load_image_from_resource( filename, getClass());
		if ( null == bufferedImage)
			return false;

		_dimension.setSize(
			Math.max( bufferedImage.getWidth(), _nameDimension.width),
			_nameDimension.height + bufferedImage.getHeight());
		_imageDimension.setSize(
			bufferedImage.getWidth(),
			bufferedImage.getHeight());
		_imageFilename = filename;
		return true;
	}

	/**
	 * @param imageX
	 * @param imageY
	 * @param imageWidth
	 * @param imageHeight
	 */
	public void set_image_position_and_image_size(int imageX, int imageY, int imageWidth, int imageHeight) {
		_imagePosition.setLocation( imageX, imageY);
		_dimension.setSize( Math.max( imageWidth, _nameDimension.width), _nameDimension.height + imageHeight);
		_imageDimension.setSize( imageWidth, imageHeight);
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#uses_this_image(java.lang.String)
	 */
	public boolean uses_this_image(String filename) {
		return filename.equals( _imageFilename);
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#update_image(java.lang.String, java.lang.String)
	 */
	public void update_image(String original_filename, String new_filename) {
		if ( original_filename.equals( _imageFilename))
			_imageFilename = new_filename;
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#set_image(java.lang.String, java.lang.String, java.lang.String)
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
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#update(java.lang.String, java.lang.String, int, int, java.util.Map)
	 */
	public boolean update(String name, String number, int x, int y, Map<String, Integer> counterMap) {
		if ( number.equals( "")) {
			if ( _name.equals( name))
				move_to( x, y, false);
		} else {
			if ( SoarsCommonTool.has_same_name( name, String.valueOf( number), _name)) {
				int counter = 0;
				Integer integer = counterMap.get( name);
				if ( null != integer) {
					counter = ( integer.intValue() + 1);
					integer = new Integer( counter);
				}

				counterMap.put( name, new Integer( counter));

				move_to( x + ( 5 * counter), y + ( 5 * counter), false);
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#draw(java.awt.Graphics2D, java.awt.Rectangle, java.awt.image.ImageObserver)
	 */
	public void draw(Graphics2D graphics2D, Rectangle rectangle, ImageObserver imageObserver) {
		if ( !_visible)
			return;

		if ( _visibleName)
			draw_name( rectangle, graphics2D, _position.x, _position.y, _position.x, _position.y + _nameDimension.height - 2);

		BufferedImage bufferedImage = AnimatorImageManager.get_instance().load_image( _imageFilename);
		if ( null != bufferedImage) {
			draw_image( rectangle, graphics2D, bufferedImage, _imagePosition, _imageDimension, imageObserver);
			if ( !_imageObject)
				_objectManager._scenarioManager._spotPropertyManager.draw_current_property_image( graphics2D, _imagePosition, bufferedImage.getHeight(), CommonProperty.get_instance()._spotWidth, CommonProperty.get_instance()._spotHeight, _properties, rectangle, imageObserver, true);
		} else {
			if ( _imageObject)
				draw_default_image( rectangle, graphics2D, _imagePosition, _imageDimension.width/*CommonProperty.get_instance()._spotWidth*/, _imageDimension.height/*CommonProperty.get_instance()._spotHeight*/);
			else {
				if ( !_objectManager._scenarioManager._spotPropertyManager.draw_current_property_image( graphics2D, _imagePosition, CommonProperty.get_instance()._spotHeight, CommonProperty.get_instance()._spotWidth, CommonProperty.get_instance()._spotHeight, _properties, rectangle, imageObserver, false))
					draw_default_image( rectangle, graphics2D, _imagePosition, CommonProperty.get_instance()._spotWidth, CommonProperty.get_instance()._spotHeight);
			}
		}

		if ( _selected && rectangle.intersects( new Rectangle( _position, _dimension)))
			CommonObjectTool.draw_frame( graphics2D, _position, _dimension, _selectedColor);
	}

//	/**
//	 * @param spotPropertyMaxDimension
//	 * @param graphics2D
//	 * @param imageObserver
//	 */
//	public void prepare_for_animation(Dimension spotPropertyMaxDimension, Graphics2D graphics2D, ImageObserver imageObserver) {
//		if ( !_visible)
//			return;
//
//		_bufferedImage = new BufferedImage(
//			Math.max( _dimension.width, spotPropertyMaxDimension.width),
//			Math.max( _dimension.height, spotPropertyMaxDimension.height),
//			BufferedImage.TYPE_INT_ARGB);
//		Graphics2D g2D = ( Graphics2D)_bufferedImage.getGraphics();
//
//		if ( _visible_name)
//			draw_name( graphics2D, 0, _nameDimension.height - 2);
//
//		Image image = AnimatorImageManager.get_instance().load_image( _imageFilename);
//		if ( null != image)
//			g2D.drawImage( image, _imagePosition.x - _position.x, _imagePosition.y - _position.y, imageObserver);
//		else {
//			if ( _imageObject || !SpotPropertyManager.get_instance().exist_selected_property()) {
//				draw_default_image( g2D, _imagePosition.x -_position.x,
//					_imagePosition.y - _position.y, CommonProperty.get_instance()._spotWidth, CommonProperty.get_instance()._spotHeight);
//			}
//		}
//
//		graphics2D.drawImage( _bufferedImage, _position.x, _position.y, imageObserver);
//	}

	/**
	 * Animates this spot.
	 * @param graphics2D the graphics object of JAVA
	 * @param rectangle the visible rectangle
	 * @param imageObserver an asynchronous update interface for receiving notifications about Image information as the Image is constructed
	 */
	public void animate(Graphics2D graphics2D, Rectangle rectangle, ImageObserver imageObserver) {
		if ( !_visible)
			return;

//		if ( null != _bufferedImage) {
//			graphics2D.drawImage( _bufferedImage, _position.x, _position.y, imageObserver);
//
//			BufferedImage bufferedImage = AnimatorImageManager.get_instance().load_image( _imageFilename);
//			if ( null != bufferedImage) {
//				if ( _image_object)
//					graphics2D.drawImage( bufferedImage, _imagePosition.x, _imagePosition.y, imageObserver);
//				else
//					ScenarioManager.get_instance().draw_current_spot_property_image( this, graphics2D, _imagePosition, bufferedImage.getHeight(), CommonProperty.get_instance()._spotWidth, CommonProperty.get_instance()._spotHeight, rectangle, imageObserver, true);
//			} else {
//				if ( _image_object)
//					draw_default_image( graphics2D, _imagePosition.x, _imagePosition.y, CommonProperty.get_instance()._spotWidth, CommonProperty.get_instance()._spotHeight);
//				else {
//					if ( !ScenarioManager.get_instance().draw_current_spot_property_image( this, graphics2D, _imagePosition, CommonProperty.get_instance()._spot_height, CommonProperty.get_instance()._spotWidth, CommonProperty.get_instance()._spotHeight, rectangle, imageObserver, false))
//						draw_default_image( graphics2D, _imagePosition.x, _imagePosition.y, CommonProperty.get_instance()._spotWidth, CommonProperty.get_instance()._spotHeight);
//				}
//			}
//		} else {
			if ( _visibleName)
				draw_name( rectangle, graphics2D, _position.x, _position.y, _position.x, _position.y + _nameDimension.height - 2);

			BufferedImage bufferedImage = AnimatorImageManager.get_instance().load_image( _imageFilename);
			if ( null != bufferedImage) {
				draw_image( rectangle, graphics2D, bufferedImage, _imagePosition, _imageDimension, imageObserver);
				if ( !_imageObject)
					_objectManager._scenarioManager.draw_current_spot_property_image( this, graphics2D, _imagePosition, bufferedImage.getHeight(), CommonProperty.get_instance()._spotWidth, CommonProperty.get_instance()._spotHeight, rectangle, imageObserver, true);
			} else {
				if ( _imageObject)
					draw_default_image( rectangle, graphics2D, _imagePosition, _imageDimension.width/*CommonProperty.get_instance()._spotWidth*/, _imageDimension.height/*CommonProperty.get_instance()._spotHeight*/);
				else {
					if ( !_objectManager._scenarioManager.draw_current_spot_property_image( this, graphics2D, _imagePosition, CommonProperty.get_instance()._spotHeight, CommonProperty.get_instance()._spotWidth, CommonProperty.get_instance()._spotHeight, rectangle, imageObserver, false))
						draw_default_image( rectangle, graphics2D, _imagePosition, CommonProperty.get_instance()._spotWidth, CommonProperty.get_instance()._spotHeight);
				}
			}
//		}
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.base.EntityBase#draw_default_image(java.awt.Graphics2D, int, int, int, int)
	 */
	protected void draw_default_image(Graphics2D graphics2D, int x, int y,int width, int height) {
		graphics2D.setColor( _imageColor);
		graphics2D.fill3DRect( x, y, width, height, true);
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#get_agent_position(soars.application.animator.object.entity.agent.AgentObject, boolean)
	 */
	public Point get_agent_position(AgentObject agentObject, boolean pack) {
		if ( !_visible)
			return null;

		if ( _agents.isEmpty())
			return null;

		return get_agent_position( agentObject, _agents.indexOf( agentObject), _agents.size(), pack);
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#get_agent_position(soars.application.animator.object.entity.agent.AgentObject, int, int, boolean)
	 */
	public Point get_agent_position(AgentObject agentObject,int index, int size, boolean pack) {
		if ( !_visible)
			return null;

		int width = ( _imageDimension.width / size);
		if ( width < CommonProperty.get_instance()._agentWidth)
			width = ( ( 10 < CommonProperty.get_instance()._agentWidth)
				? 10 : CommonProperty.get_instance()._agentWidth);
		else if ( width > CommonProperty.get_instance()._agentWidth)
			width = CommonProperty.get_instance()._agentWidth;
//		if ( width < CommonProperty.get_instance()._minimumWidth)
//			width = CommonProperty.get_instance()._minimumWidth;

		Point point = new Point( _imagePosition);
		point.x += ( width * index);

		// Pack agents
		if ( pack) {
			if ( point.x + CommonProperty.get_instance()._agentWidth > _imagePosition.x + _imageDimension.width/*CommonProperty.get_instance()._spotWidth*/)
				point.x = _imagePosition.x
					+ Math.max( 0, _imageDimension.width/*CommonProperty.get_instance()._spotWidth*/ - agentObject._imageDimension.width/*CommonProperty.get_instance()._agentWidth*/);
			if ( point.x < _imagePosition.x)
				point.x = _imagePosition.x;
		}

		return point;
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#get_spot(java.awt.Point)
	 */
	public ISpotObjectManipulator get_spot(Point point) {
		Rectangle rect = new Rectangle( _position.x, _position.y, _imageDimension.width, _dimension.height);
		if ( _visible && rect.contains( point))
			return this;
//		Rectangle rect1 = new Rectangle( _position.x, _position.y + _nameDimension.height, _imageDimension.width, _imageDimension.height);
//		Rectangle rect2 = new Rectangle( _position, _nameDimension);
//		if ( _visible && ( rect1.contains( point) || rect2.contains( point)))
//			return this;

		return null;
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#select(boolean)
	 */
	public void select(boolean selected) {
		super.select( selected);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#select_all_objects(boolean)
	 */
	public void select_all_objects(boolean selected) {
		select( selected);
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#select_all_spot_objects(boolean)
	 */
	public void select_all_spot_objects(boolean selected) {
		if ( !is_image_object())
			select( selected);
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#select_all_image_objects(boolean)
	 */
	public void select_all_image_objects(boolean selected) {
		if ( is_image_object())
			select( selected);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#select(java.awt.Rectangle)
	 */
	public void select(Rectangle rectangle) {
		if ( _visible)
			_selected = rectangle.intersects( new Rectangle( _position.x, _position.y, _imageDimension.width, _dimension.height));
//		if ( _visible)
//			_selected = ( rectangle.intersects( new Rectangle( _position.x, _position.y + _nameDimension.height, _imageDimension.width, _imageDimension.height))
//				|| rectangle.intersects( new Rectangle( _position, _nameDimension)));
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#move_to_top()
	 */
	public void move_to_top() {
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#move_to_bottom()
	 */
	public void move_to_bottom() {
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#move_to_front()
	 */
	public void move_to_front() {
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#move_to_back()
	 */
	public void move_to_back() {
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#test(int, int)
	 */
	public boolean test(int deltaX, int deltaY) {
		return ( 0 <= ( _position.x + deltaX) && ( 0 <= _position.y + deltaY));
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#move(int, int, boolean)
	 */
	public void move(int deltaX, int deltaY, boolean modify) {
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

		_imagePosition.x += deltaX;
		_imagePosition.y += deltaY;

		for ( AgentObject agentObject:_agents)
			agentObject.move( deltaX, deltaY);

		if ( modify)
			Observer.get_instance().modified();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#move_to(int, int, boolean)
	 */
	public void move_to(int x, int y, boolean modify) {
		move( x - _position.x, y - _position.y, modify);
	}

	/**
	 * Updates this spot with the specified spot.
	 * @param spotObject the specified spot
	 */
	public void update(SpotObject spotObject) {
		super.update( spotObject);
		_imagePosition = spotObject._imagePosition;
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#edit(java.awt.Frame)
	 */
	public boolean edit(Frame frame) {
		if ( !is_image_object()) {
			EditObjectDlg editObjectDlg = new EditObjectDlg( frame,
				ResourceManager.get_instance().get( "edit.spot.dialog.title"),
				true, Environment._openSpotImageDirectoryKey, this);
			if ( !editObjectDlg.do_modal())
				return false;
		} else {
			EditImageObjectDlg editImageObjectDlg = new EditImageObjectDlg( frame,
				ResourceManager.get_instance().get( "edit.image.object.dialog.title"),
				true, Environment._openImageObjectImageDirectoryKey, this);
			if ( !editImageObjectDlg.do_modal())
				return false;
		}

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

		_imagePosition.setLocation( _position.x, _position.y + _nameDimension.height);

		BufferedImage bufferedImage = AnimatorImageManager.get_instance().load_image( _imageFilename);
		if ( null == bufferedImage) {
			_dimension.setSize(
				Math.max( CommonProperty.get_instance()._spotWidth, _nameDimension.width),
				CommonProperty.get_instance()._spotHeight + _nameDimension.height);
			_imageDimension.setSize(
				CommonProperty.get_instance()._spotWidth,
				CommonProperty.get_instance()._spotHeight);
		} else {
			_dimension.setSize(
				Math.max( bufferedImage.getWidth(), _nameDimension.width),
				_nameDimension.height + bufferedImage.getHeight());
			_imageDimension.setSize(
				bufferedImage.getWidth(),
				bufferedImage.getHeight());
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.base.EntityBase#update(int, int, int, java.lang.String, java.awt.Graphics2D)
	 */
	public boolean update(int imageR, int imageG, int imageB, String imageFilename, Graphics2D graphics2D) {
		if ( !super.update(imageR, imageG, imageB, imageFilename, graphics2D))
			return true;

		_imagePosition.setLocation( _position.x, _position.y + _nameDimension.height);

		BufferedImage bufferedImage = AnimatorImageManager.get_instance().load_image( _imageFilename);
		if ( null == bufferedImage) {
			_dimension.setSize(
				Math.max( CommonProperty.get_instance()._spotWidth, _nameDimension.width),
				CommonProperty.get_instance()._spotHeight + _nameDimension.height);
			_imageDimension.setSize(
				CommonProperty.get_instance()._spotWidth,
				CommonProperty.get_instance()._spotHeight);
		} else {
			_dimension.setSize(
				Math.max( bufferedImage.getWidth(), _nameDimension.width),
				_nameDimension.height + bufferedImage.getHeight());
			_imageDimension.setSize(
				bufferedImage.getWidth(),
				bufferedImage.getHeight());
		}

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.base.EntityBase#update_image(java.lang.String)
	 */
	protected boolean update_image(String imageFilename) {
		return load_image( imageFilename);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#get_tooltip_text(java.lang.String)
	 */
	public String get_tooltip_text(String state) {
		// 画像情報を表示
		if ( is_image_object())
			return "Image object : x=" + _imagePosition.x + " y=" + _imagePosition.y + " width=" + _dimension.width + " height=" + _dimension.height;

		if ( state.equals( "animation"))
			return _name + _objectManager._scenarioManager.get_tooltip_text( this, _objectManager._scenarioManager._spotTransitionManager, _objectManager._scenarioManager._spotPropertyManager.get_selected_properties());
		else if ( state.equals( "edit"))
			return get_tooltip_text( _objectManager._scenarioManager._spotPropertyManager.get_selected_properties());
		return null;
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#is_unique_name(java.lang.String)
	 */
	public boolean is_unique_name(String name) {
		return !_name.equals( name);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#write(soars.common.utility.xml.sax.Writer)
	 */
	public boolean write(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		if ( !super.write( attributesImpl))
			return false;

		attributesImpl.addAttribute( null, null, "x", "", String.valueOf( _position.x));
		attributesImpl.addAttribute( null, null, "y", "", String.valueOf( _position.y));
		attributesImpl.addAttribute( null, null, "image_object", "", _imageObject ? "true" : "false");

		if ( _imageObject) {
			attributesImpl.addAttribute( null, null, "image_x", "", String.valueOf( _imagePosition.x));
			attributesImpl.addAttribute( null, null, "image_y", "", String.valueOf( _imagePosition.y));
			attributesImpl.addAttribute( null, null, "image_width", "", String.valueOf( _imageDimension.width));
			attributesImpl.addAttribute( null, null, "image_height", "", String.valueOf( _imageDimension.height));
		}

		writer.writeElement( null, null, "spot", attributesImpl);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#write_graphic_data(java.io.File, soars.common.utility.xml.sax.Writer)
	 */
	public boolean write_graphic_data(File root_directory, Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		if ( !super.write_graphic_data( root_directory, attributesImpl))
			return false;

		attributesImpl.addAttribute( null, null, "x", "", String.valueOf( _position.x));
		attributesImpl.addAttribute( null, null, "y", "", String.valueOf( _position.y));
		attributesImpl.addAttribute( null, null, "image_object", "", _imageObject ? "true" : "false");

		if ( _imageObject) {
			attributesImpl.addAttribute( null, null, "image_x", "", String.valueOf( _imagePosition.x));
			attributesImpl.addAttribute( null, null, "image_y", "", String.valueOf( _imagePosition.y));
			attributesImpl.addAttribute( null, null, "image_width", "", String.valueOf( _imageDimension.width));
			attributesImpl.addAttribute( null, null, "image_height", "", String.valueOf( _imageDimension.height));
		}

		writer.writeElement( null, null, "spot", attributesImpl);

		return true;
	}
}
