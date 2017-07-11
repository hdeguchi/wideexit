/*
 * 2005/02/07
 */
package soars.application.animator.object.entity.spot;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComponent;

import org.xml.sax.SAXException;

import soars.application.animator.common.image.AnimatorImageManager;
import soars.application.animator.main.internal.ObjectManager;
import soars.application.animator.object.entity.agent.AgentObject;
import soars.application.animator.object.entity.agent.AgentObjectManager;
import soars.application.animator.observer.Observer;
import soars.application.animator.setting.common.CommonProperty;
import soars.common.utility.xml.sax.Writer;

/**
 * The ISpotObjectManipulator hashtable(name(String) - ISpotObjectManipulator).
 * @author kurata / SOARS project
 */
public class SpotObjectManager extends HashMap<String, ISpotObjectManipulator> implements ISpotObjectManipulator {

	/**
	 * 
	 */
	private ObjectManager _objectManager = null;

	/**
	 * 
	 */
	private Vector<ISpotObjectManipulator> _order = new Vector<ISpotObjectManipulator>();

	/**
	 * 
	 */
	private ISpotObjectManipulator _parent = null;

	/**
	 * 
	 */
	private boolean _open = true;

	/**
	 * 
	 */
	private Color _selectedColor = new Color( 255, 0, 0);

	/**
	 * 
	 */
	private boolean _selected = false;

	/**
	 * 
	 */
	private String _name = "";

	/**
	 * 
	 */
	private boolean _visible = true;


	/**
	 * 
	 */
	private BufferedImage _image = null;

	/**
	 * 
	 */
	private String _imageFilename = "";

	/**
	 * 
	 */
	private Point _position = new Point();

	/**
	 * 
	 */
	private Dimension _dimension = new Dimension();

	/**
	 * 
	 */
	private Dimension _nameDimension = null;

	/**
	 * 
	 */
	private Point _imagePosition = new Point();

	/**
	 * 
	 */
	private BufferedImage _bufferedImage = null;

	/**
	 * 
	 */
	private Color _imageColor = new Color( 0,
		( int)( 255 * Math.random()), ( int)( 255 * Math.random()));

	/**
	 * 
	 */
	private Color _textColor = new Color( 0, 0, 255);

	/**
	 * 
	 */
	private Font _font = null;

	/**
	 * Creates the ISpotObjectManipulator hasshtable.
	 * @param name the name of this object
	 * @param parent the parent of this object
	 * @param objectManager
	 * @param graphics2D the graphics object of JAVA
	 */
	public SpotObjectManager(String name, ISpotObjectManipulator parent, ObjectManager objectManager, Graphics2D graphics2D) {
		super();
		_name = name;
		_parent = parent;
		_objectManager = objectManager;
		if ( null != graphics2D) {
			FontMetrics fontMetrics = graphics2D.getFontMetrics();
			_nameDimension = new Dimension(
				fontMetrics.stringWidth( _name) + _name.length(),
				fontMetrics.getHeight());
			_dimension.setSize( CommonProperty.get_instance()._spotWidth,
				CommonProperty.get_instance()._spotHeight + _nameDimension.height);
		}
	}

	/** Copy constructor(For duplication)
	 * @param spotObjectManager
	 * @param objectManager
	 */
	public SpotObjectManager(SpotObjectManager spotObjectManager, ISpotObjectManipulator parent, ObjectManager objectManager) {
		_name = spotObjectManager._name;
		_parent = parent;
		_objectManager = objectManager;
		_open = spotObjectManager._open;
		if ( null != spotObjectManager._selectedColor)
			_selectedColor = new Color( spotObjectManager._selectedColor.getRGB());
		_selected = spotObjectManager._selected;
		_visible = spotObjectManager._visible;
		_imageFilename = spotObjectManager._imageFilename;
		if ( null != spotObjectManager._position)
			_position = new Point( spotObjectManager._position);
		if ( null != spotObjectManager._dimension)
			_dimension = new Dimension( spotObjectManager._dimension);
		if ( null != spotObjectManager._nameDimension)
			_nameDimension = new Dimension( spotObjectManager._nameDimension);
		if ( null != spotObjectManager._imagePosition)
			_imagePosition = new Point( spotObjectManager._imagePosition);
		if ( null != spotObjectManager._imageColor)
			_imageColor = new Color( spotObjectManager._imageColor.getRGB());
		if ( null != spotObjectManager._textColor)
			_textColor = new Color( spotObjectManager._textColor.getRGB());
		if ( null != spotObjectManager._font)
			_font = new Font( spotObjectManager._font.getName(), spotObjectManager._font.getStyle(), spotObjectManager._font.getSize());
		for ( ISpotObjectManipulator spotObjectManipulator:spotObjectManager._order) {
			if ( spotObjectManipulator instanceof SpotObjectManager) {
				SpotObjectManager som = new SpotObjectManager( ( SpotObjectManager)spotObjectManipulator, this, objectManager);
				_order.add( som);
				put( som._name, som);
			} else if ( spotObjectManipulator instanceof SpotObject) {
				SpotObject so = new SpotObject( ( SpotObject)spotObjectManipulator, this, objectManager);
				_order.add( so);
				put( so._name, so);
			}
		}
	}

	/**
	 * @param agentObjectManager new object of duplication
	 * @param srcSpotObjectManager source of duplication
	 */
	public void adjust_for_duplication(AgentObjectManager agentObjectManager, SpotObjectManager srcSpotObjectManager) {
		for ( int i = 0; i < _order.size(); ++i) {
			if ( _order.get( i) instanceof SpotObjectManager) {
				SpotObjectManager som = ( SpotObjectManager)_order.get( i);
				som.adjust_for_duplication( agentObjectManager, ( SpotObjectManager)srcSpotObjectManager._order.get( i));
			} else if ( _order.get( i) instanceof SpotObject) {
				SpotObject so = ( SpotObject)_order.get( i);
				so.adjust_for_duplication( agentObjectManager, ( SpotObject)srcSpotObjectManager._order.get( i));
			}
		}
	}

	/**
	 * Returns the array of all spots.
	 * @return the array of all spots
	 */
	public Vector get_order() {
		return _order;
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#is_image_object()
	 */
	public boolean is_image_object() {
		return false;
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.spot.ISpotObjectManipulator#setup(java.awt.Graphics2D)
	 */
	public boolean setup(Graphics2D graphics2D) {
		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#cleanup()
	 */
	public void cleanup() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)entry.getValue();
			spotObjectManipulator.cleanup();
		}
		clear();

		_order.clear();
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#set_property(java.lang.String, java.lang.String)
	 */
	public boolean set_property(String name, String value) {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)entry.getValue();
			spotObjectManipulator.set_property( name, value);
		}
		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#get_name()
	 */
	public String get_name() {
		return _name;
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#is_visible()
	 */
	public boolean is_visible() {
		return _visible;
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#update_dimension()
	 */
	public void update_dimension() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)entry.getValue();
			spotObjectManipulator.update_dimension();
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#append(soars.application.animator.object.entity.agent.AgentObject)
	 */
	public void append(AgentObject agentObject) {
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
		
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#restore_image_size()
	 */
	public void restore_image_size() {
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#update_preferred_size(java.awt.Dimension, java.awt.Dimension, java.awt.Dimension, int)
	 */
	public void update_preferred_size(Dimension preferredSize, Dimension spotPropertyMaxDimension, Dimension agentMaxDimension, int max) {
		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			spotObjectManipulator.update_preferred_size( preferredSize, spotPropertyMaxDimension, agentMaxDimension, max);
		}
//		for ( int i = 0; i < _order.size(); ++i) {
//			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
//			spotObjectManipulator.update_preferred_size( preferred_size, spotPropertyMaxDimension, agentMaxDimension, max);
//			if ( spotObjectManipulator instanceof SpotObjectManager) {
//				SpotObjectManager spotObjectManager = ( SpotObjectManager)spotObjectManipulator;
//				if ( spotObjectManager._open)
//					spotObjectManager.update_preferred_size( preferred_size, spotPropertyMaxDimension, agentMaxDimension, max);
//				else {
//					int x = _position.x + Math.max( 
//						Math.max( _dimension.width, _nameDimension.width),
//						spotPropertyMaxDimension.width);
//					int y = _position.y + Math.max( _dimension.height, spotPropertyMaxDimension.height);
//					if ( preferredSize.width < x)
//						preferredSize.width = x;
//					if ( preferredSize.height < y)
//						preferredSize.height = y;
//				}
//			} else
//				spotObjectManipulator.update_preferred_size( preferredSize, spotPropertyMaxDimension, agentMaxDimension, max);
//		}
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#get_size(java.awt.Dimension, java.awt.Dimension, java.awt.Dimension, int)
	 */
	public void get_size(Dimension dimension, Dimension spotPropertyMaxDimension, Dimension agentMaxDimension, int max) {
		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			spotObjectManipulator.get_size( dimension, spotPropertyMaxDimension, agentMaxDimension, max);
		}
//		for ( int i = 0; i < _order.size(); ++i) {
//			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
//			spotObjectManipulator.get_size( dimension, spotPropertyMaxDimension, agentMaxDimension, max);
//			if ( spotObjectManipulator instanceof SpotObjectManager) {
//				SpotObjectManager spotObjectManager = ( SpotObjectManager)spotObjectManipulator;
//				if ( spotObjectManager._open)
//					spotObjectManager.get_size( dimension, spotPropertyMaxDimension, agentMaxDimension, max);
//				else {
//					int x = _position.x + Math.max(
//						Math.max( _dimension.width, _nameDimension.width),
//						spotPropertyMaxDimension.width);
//					int y = _position.y + Math.max( _dimension.height, spotPropertyMaxDimension.height);
//					if ( dimension.width < x)
//						dimension.width = x;
//					if ( dimension.height < y)
//						dimension.height = y;
//				}
//			} else
//				spotObjectManipulator.get_size( dimension, spotPropertyMaxDimension, agentMaxDimension, max);
//		}
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#load_image(java.lang.String)
	 */
	public boolean load_image(String filename) {
		if ( null == filename || 0 == filename.length()) {
			_dimension.setSize( CommonProperty.get_instance()._spotWidth,
				CommonProperty.get_instance()._spotHeight + _nameDimension.height);
			_imageFilename = "";
			_image = null;
			return false;
		}

		BufferedImage image = AnimatorImageManager.get_instance().load_image( filename);
		//BufferedImage image = Resource.load_image( filename);
		if ( null == image)
			return false;

		_image = image;
		_dimension.setSize( _image.getWidth(), _image.getHeight());
		_imageFilename = filename;
		return true;
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#load_image_from_resource(java.lang.String)
	 */
	public boolean load_image_from_resource(String filename) {
		if ( null == filename || 0 == filename.length()) {
			_dimension.setSize( CommonProperty.get_instance()._spotWidth,
				CommonProperty.get_instance()._spotHeight + _nameDimension.height);
			_imageFilename = "";
			_image = null;
			return false;
		}

		BufferedImage image = AnimatorImageManager.get_instance().load_image_from_resource( filename, getClass());
		//BufferedImage image = Resource.load_image_from_resource( filename, getClass());
		if ( null == image)
			return false;

		_image = image;
		_dimension.setSize( _image.getWidth(), _image.getHeight());
		_imageFilename = filename;
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#uses_this_image(java.lang.String)
	 */
	public boolean uses_this_image(String filename) {
		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			if ( spotObjectManipulator.uses_this_image( filename))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#update_image(java.lang.String, java.lang.String)
	 */
	public void update_image(String originalFilename, String newFilename) {
		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			spotObjectManipulator.update_image( originalFilename, newFilename);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#set_image(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void set_image(String name, String number, String filename) {
		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			spotObjectManipulator.set_image( name, number, filename);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#update(java.lang.String, java.lang.String, int, int, java.util.Map)
	 */
	public boolean update(String name, String number, int x, int y, Map<String, Integer> counterMap) {
		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			spotObjectManipulator.update( name, number, x, y, counterMap);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#draw(java.awt.Graphics2D, java.awt.Rectangle, java.awt.image.ImageObserver)
	 */
	public void draw(Graphics2D graphics2D, Rectangle rectangle, ImageObserver imageObserver) {
		for ( int i = _order.size() - 1; i >= 0; --i)
			_order.get( i).draw( graphics2D, rectangle, imageObserver);
//		for ( int i = _order.size() - 1; i >= 0; --i) {
//			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
//			spotObjectManipulator.draw( graphics2D, rectangle, imageObserver);
//			if ( spotObjectManipulator instanceof SpotObjectManager) {
//				SpotObjectManager spotObjectManager = ( SpotObjectManager)spotObjectManipulator;
//				if ( spotObjectManager._open)
//					spotObjectManager.draw( graphics2D, imageObserver);
//				else {
//					graphics2D.setColor( _textColor);
//					graphics2D.drawString( _name, _position.x, _position.y + _nameDimension.height - 2);
//
//					if ( null != _image) {
//						graphics2D.drawImage( _image, _imagePosition.x, _imagePosition.y, imageObserver);
//						ScenarioManager.get_instance().draw_current_spot_property_image( this, graphics2D, _imagePosition, _image.getHeight(), imageObserver, true);
//					} else {
//						if ( SpotPropertyManager.get_instance().exist_selected_property())
//							if ( !ScenarioManager.get_instance().draw_current_spot_property_image( this, graphics2D, _imagePosition, _dimension.height - _nameDimension.height, imageObserver, false)) {
//								draw_default_image( graphics2D, _imagePosition.x, _imagePosition.y, _dimension.width, _dimension.height - _nameDimension.height);
//							}
//						else {
//							draw_default_image( graphics2D, _imagePosition.x, _imagePosition.y, _dimension.width, _dimension.height - _nameDimension.height);
//						}
//					}
//					if ( _selected)
//						CommonObjectTool.draw_frame( graphics2D, _position, _dimension, _selectedColor);
//				}
//			} else
//				spotObjectManipulator.draw( graphics2D, imageObserver);
//		}
	}

//	/**
//	 * @param dimension
//	 * @param spot_property_max_dimension
//	 * @param imageObserver
//	 */
//	public void prepare_for_animation(Dimension dimension, Dimension spot_property_max_dimension, ImageObserver imageObserver) {
//		_bufferedImage = new BufferedImage( dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
//		Graphics2D graphics2D = ( Graphics2D)_bufferedImage.getGraphics();
//
//		for ( int i = _order.size() - 1; i >= 0; --i) {
//			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
//			if ( spotObjectManipulator instanceof SpotObjectManager) {
//				SpotObjectManager spotObjectManager = ( SpotObjectManager)spotObjectManipulator;
//				if ( spotObjectManager._open)
//					spotObjectManager.prepare_for_animation( dimension, spot_property_max_dimension, imageObserver);
//				else {
//					graphics2D.setColor( _textColor);
//					graphics2D.drawString( _name, _position.x, _position.y + _nameDimension.height - 2);
//
//					if ( null != _image)
//						graphics2D.drawImage( _image, _imagePosition.x - _position.x,
//							_imagePosition.y - _position.y, imageObserver);
//					else {
//						if ( !SpotPropertyManager.get_instance().exist_selected_property()) {
//							draw_default_image( graphics2D, _imagePosition.x -_position.x,
//								_imagePosition.y - _position.y, _dimension.width, _dimension.height - _nameDimension.height);
//						}
//					}
//					if ( _selected)
//						CommonObjectTool.draw_frame( graphics2D, _position, _dimension, _selectedColor);
//				}
//			} else {
//				SpotObject spotObject = ( SpotObject)spotObjectManipulator;
//				spotObject.prepare_for_animation( spot_property_max_dimension, graphics2D, imageObserver);
//			}
//		}
//
//		graphics2D.dispose();
//	}

	/**
	 * Animates the all spots.
	 * @param graphics2D the graphics object of JAVA
	 * @param rectangle the visible rectangle
	 * @param imageObserver an asynchronous update interface for receiving notifications about Image information as the Image is constructed
	 */
	public void animate(Graphics2D graphics2D, Rectangle rectangle, ImageObserver imageObserver) {
		if ( null != _bufferedImage)
			graphics2D.drawImage( _bufferedImage, 0, 0, imageObserver);

		for ( int i = _order.size() - 1; i >= 0; --i) {
			SpotObject spotObject = ( SpotObject)_order.get( i);
			spotObject.animate( graphics2D, rectangle, imageObserver);
		}
	}

	/**
	 * @param graphics2D
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	private void draw_default_image(Graphics2D graphics2D, int x, int y, int width, int height) {
		graphics2D.setColor( _imageColor);
		graphics2D.fill3DRect( x, y, width, height, true);
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#get_agent_position(soars.application.animator.object.entity.agent.AgentObject, boolean)
	 */
	public Point get_agent_position(AgentObject agentObject, boolean pack) {
		return null;
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#get_agent_position(soars.application.animator.object.entity.agent.AgentObject, int, int, boolean)
	 */
	public Point get_agent_position(AgentObject agentObject, int index, int size, boolean pack) {
		return null;
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#get_spot(java.awt.Point)
	 */
	public ISpotObjectManipulator get_spot(Point point) {
		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			if ( spotObjectManipulator instanceof SpotObjectManager) {
				SpotObjectManager spotObjectManager = ( SpotObjectManager)spotObjectManipulator;
				if ( spotObjectManager._open)
					return spotObjectManager.get_spot( point);
				else {
					Rectangle rect = new Rectangle( spotObjectManager._position, spotObjectManager._dimension);
					if ( !_visible || !rect.contains( point))
						continue;

//					move_to_top( spotObjectManager);
					return spotObjectManager;
				}
			} else {
				SpotObject spotObject = ( SpotObject)spotObjectManipulator.get_spot( point);
				if ( null == spotObject)
					continue;

//				move_to_top( spotObject);
				return spotObject;
			}
		}
		return null;
	}

	/**
	 * Moves the specified spot to the top.
	 * @param spotObjectManipulator the specified spot
	 */
	public void move_to_top(ISpotObjectManipulator spotObjectManipulator) {
		int index = _order.indexOf( spotObjectManipulator);
		_order.removeElementAt( index);
		_order.insertElementAt( spotObjectManipulator, 0);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#select(boolean)
	 */
	public void select(boolean selected) {
		_selected = selected;
		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			spotObjectManipulator.select( selected);
		}
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#select_all_objects(boolean)
	 */
	public void select_all_objects(boolean selected) {
		_selected = selected;
		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			spotObjectManipulator.select_all_objects( selected);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#select_all_spot_objects(boolean)
	 */
	public void select_all_spot_objects(boolean selected) {
		if ( !is_image_object())
			_selected = selected;

		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			spotObjectManipulator.select_all_spot_objects( selected);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#select_all_image_objects(boolean)
	 */
	public void select_all_image_objects(boolean selected) {
		if ( is_image_object())
			_selected = selected;

		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			spotObjectManipulator.select_all_image_objects( selected);
		}
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#select(java.awt.Rectangle)
	 */
	public void select(Rectangle rectangle) {
		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			spotObjectManipulator.select( rectangle);
		}
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#selected()
	 */
	public boolean selected() {
		return _selected;
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#move_to_top()
	 */
	public void move_to_top() {
		boolean modified = false;
		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			if ( spotObjectManipulator instanceof SpotObjectManager)
				spotObjectManipulator.move_to_top();
			else {
				if ( spotObjectManipulator.selected()) {
					int index = _order.indexOf( spotObjectManipulator);
					if ( 0 > index || index >= _order.size())
						continue;

					_order.removeElementAt( index);
					_order.insertElementAt( spotObjectManipulator, 0);

					modified = true;
				}
			}
		}

		if ( modified)
			Observer.get_instance().modified();
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#move_to_bottom()
	 */
	public void move_to_bottom() {
		boolean modified = false;
		for ( int i = _order.size() - 1; i >= 0; --i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			if ( spotObjectManipulator instanceof SpotObjectManager)
				spotObjectManipulator.move_to_bottom();
			else {
				if ( spotObjectManipulator.selected()) {
					int index = _order.indexOf( spotObjectManipulator);
					if ( 0 > index || index >= _order.size())
						continue;

					_order.removeElementAt( index);
					_order.add( spotObjectManipulator);

					modified = true;
				}
			}
		}

		if ( modified)
			Observer.get_instance().modified();
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#move_to_front()
	 */
	public void move_to_front() {
		boolean modified = false;
		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			if ( spotObjectManipulator instanceof SpotObjectManager)
				spotObjectManipulator.move_to_front();
			else {
				if ( spotObjectManipulator.selected()) {
					int index = _order.indexOf( spotObjectManipulator);
					if ( 1 > index || index >= _order.size())
						continue;

					_order.removeElementAt( index);
					_order.insertElementAt( spotObjectManipulator, index - 1);

					modified = true;
				}
			}
		}

		if ( modified)
			Observer.get_instance().modified();
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#move_to_back()
	 */
	public void move_to_back() {
		boolean modified = false;
		for ( int i = _order.size() - 1; i >= 0; --i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			if ( spotObjectManipulator instanceof SpotObjectManager)
				spotObjectManipulator.move_to_back();
			else {
				if ( spotObjectManipulator.selected()) {
					int index = _order.indexOf( spotObjectManipulator);
					if ( 0 > index || index >= _order.size() - 1)
						continue;

					_order.removeElementAt( index);
					_order.insertElementAt( spotObjectManipulator, index + 1);

					modified = true;
				}
			}
		}

		if ( modified)
			Observer.get_instance().modified();
	}

	/**
	 * Returns true if a selected spot exists.
	 * @return true if a selected spot exists
	 */
	public boolean exist_selected_spot() {
		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			if ( spotObjectManipulator instanceof SpotObjectManager) {
				SpotObjectManager spotObjectManager = ( SpotObjectManager)spotObjectManipulator;
				if ( spotObjectManager.exist_selected_spot())
					return true;
			} else {
				SpotObject spotObject = ( SpotObject)spotObjectManipulator;
				if ( spotObject._visible && spotObject._selected)
					return true;
			}
		}
		return false;
	}

	/**
	 * Gets the selected spots.
	 * @param spots the array of the selected spots
	 */
	public void get_selected_spot(Vector<SpotObject> spots) {
		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			if ( spotObjectManipulator.is_image_object())
				continue;

			if ( spotObjectManipulator instanceof SpotObjectManager) {
				SpotObjectManager spotObjectManager = ( SpotObjectManager)spotObjectManipulator;
				spotObjectManager.get_selected_spot( spots);
			} else {
				SpotObject spotObject = ( SpotObject)spotObjectManipulator;
				if ( spotObject._visible && spotObject._selected)
					spots.add( spotObject);
			}
		}
	}

	/**
	 * Gets the selected image objects.
	 * @param images the array of the selected image objects
	 */
	public void get_selected_image(Vector<SpotObject> images) {
		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			if ( spotObjectManipulator instanceof SpotObjectManager) {
				SpotObjectManager spotObjectManager = ( SpotObjectManager)spotObjectManipulator;
				spotObjectManager.get_selected_image( images);
			} else {
				if ( !spotObjectManipulator.is_image_object())
					continue;

				SpotObject spotObject = ( SpotObject)spotObjectManipulator;
				if ( spotObject._visible && spotObject._selected)
					images.add( spotObject);
			}
		}
	}

	/**
	 * Gets the selected spots and image objects.
	 * @param objects the array of the selected spots and image objects
	 */
	public void get_selected_spot_and_image(Vector<SpotObject> objects) {
		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			if ( spotObjectManipulator instanceof SpotObjectManager) {
				SpotObjectManager spotObjectManager = ( SpotObjectManager)spotObjectManipulator;
				spotObjectManager.get_selected_spot( objects);
			} else {
				SpotObject spotObject = ( SpotObject)spotObjectManipulator;
				if ( spotObject._visible && spotObject._selected)
					objects.add( spotObject);
			}
		}
	}

	/**
	 * Returns true if a visible spot exists.
	 * @return true if a visible spot exists
	 */
	public boolean exist_visible_spot() {
		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			if ( spotObjectManipulator instanceof SpotObjectManager) {
				SpotObjectManager spotObjectManager = ( SpotObjectManager)spotObjectManipulator;
				if ( spotObjectManager.exist_visible_spot())
					return true;
			} else {
				SpotObject spotObject = ( SpotObject)spotObjectManipulator;
				if ( spotObject._visible)
					return true;
			}
		}
		return false;
	}

	/**
	 * Gets the visible spots.
	 * @param spots the array of the visible spots
	 */
	public void get_visible_spot(Vector<ISpotObjectManipulator> spots) {
		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			if ( spotObjectManipulator instanceof SpotObjectManager) {
				SpotObjectManager spotObjectManager = ( SpotObjectManager)spotObjectManipulator;
				spotObjectManager.get_visible_spot( spots);
			} else {
				SpotObject spotObject = ( SpotObject)spotObjectManipulator;
				if ( spotObject._visible && !spotObject.is_image_object())
					spots.add( spotObject);
			}
		}
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
		_position.x += deltaX;
		_position.y += deltaY;

		if ( modify)
			Observer.get_instance().modified();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#move_to(int, int, boolean)
	 */
	public void move_to(int x, int y, boolean modify) {
		_position.x = x;
		_position.y = y;

		if ( modify)
			Observer.get_instance().modified();
	}

	/**
	 * Appends a new spot object with the specified data.
	 * @param name the name of the new spot
	 * @param position the position of the new spot
	 * @param component the base class for all Swing components
	 */
	public SpotObject append(String name, Point position, JComponent component) {
		SpotObject spotObject = new SpotObject( name, false, position, true, this, _objectManager, ( Graphics2D)component.getGraphics());
		put( name, spotObject);
		_order.insertElementAt( spotObject, 0);
		Observer.get_instance().modified();
		return spotObject;
	}

	/**
	 * Appends the specified spot object
	 * @param name the name of the specified spot
	 * @param spotObjectManipulator the specified spot
	 */
	public void append(String name, ISpotObjectManipulator spotObjectManipulator) {
		put( name, spotObjectManipulator);
		_order.add( spotObjectManipulator);
	}

	/**
	 * Updates the spot, which has the specified name, with the specified spot.
	 * @param name the specified name
	 * @param spotObjectManipulator the specified spot
	 */
	public void update(String name, ISpotObjectManipulator spotObjectManipulator) {
		SpotObject so = ( SpotObject)get( name);
		if ( null == so)
			return;

		so.update( ( SpotObject)spotObjectManipulator);
	}

	/**
	 * Returns the coordinates which has the maximum x-coordinates and y-coordinates.
	 * @return the coordinates which has the maximum x-coordinates and y-coordinates
	 */
	public Point get_max_image_point() {
		Point point = new Point();
		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			Point p;
			if ( spotObjectManipulator instanceof SpotObjectManager) {
				SpotObjectManager spotObjectManager = ( SpotObjectManager)spotObjectManipulator;
				if ( spotObjectManager._open)
					p = spotObjectManager.get_max_image_point();
				else
					p = _imagePosition;
			} else
				p = spotObjectManipulator.get_image_position();

			if ( point.x < p.x)
				point.x = p.x;
			if ( point.y < p.y)
				point.y = p.y;
		}

		return point;
	}

	/**
	 * Arranges the all spots appropriately.
	 */
	public void arrange() {
		int line = 0;
		int row = 0;
		int width = ( ( int)Math.sqrt( size()) + 1);

		if ( size() < width)
			width = size();

		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)entry.getValue();
			spotObjectManipulator.set_position(
				CommonProperty.get_instance()._spotWidth * 2 * row++,
				CommonProperty.get_instance()._spotHeight * 2 * line);

			if ( row == width) {
				row = 0;
				++line;
			}
		}
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#edit(java.awt.Frame)
	 */
	public boolean edit(Frame frame) {
		return false;
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#get_tooltip_text(java.lang.String)
	 */
	public String get_tooltip_text(String state) {
		return null;
	}

	/**
	 * Returns the unique name of image objects.
	 * @return the unique name of image objects
	 */
	public String get_unique_name() {
		String base_name = "__animator_image_";
		int index = 0;
		while ( true) {
			String name = ( base_name + index);
			if ( is_unique_name( name))
				return name;

			++index;
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#is_unique_name(java.lang.String)
	 */
	public boolean is_unique_name(String name) {
		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			if ( !spotObjectManipulator.is_unique_name( name))
				return false;
		}
		return true;
	}

	/**
	 * Removes the specified image objects.
	 * @param images the specified image objects
	 */
	public void remove(Vector<SpotObject> images) {
		if ( images.isEmpty())
			return;

		for ( SpotObject imageObject:images) {
			remove( imageObject._name);
			_order.removeElement( imageObject);
		}

		Observer.get_instance().modified();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#write(soars.common.utility.xml.sax.Writer)
	 */
	public boolean write(Writer writer) throws SAXException {
		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			if ( !spotObjectManipulator.write( writer))
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.entity.spot.ISpotObjectManipulator#write_graphic_data(java.io.File, soars.common.utility.xml.sax.Writer)
	 */
	public boolean write_graphic_data(File rootDirectory, Writer writer)	throws SAXException {
		for ( int i = 0; i < _order.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)_order.get( i);
			if ( !spotObjectManipulator.write_graphic_data( rootDirectory, writer))
				return false;
		}
		return true;
	}

	/**
	 * Returns true if the specified image file is loaded successfully.
	 * @param filename the name of the specified image file
	 * @return true if the specified image file is loaded successfully
	 */
	public boolean load_image_all(String filename) {
		if ( !load_image( filename))
			return false;

		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)entry.getValue();
			if ( !spotObjectManipulator.load_image( filename))
				return false;
		}

		return true;
	}

	/**
	 * Returns true if the specified image file is loaded from resource successfully.
	 * @param filename the name of the specified image file
	 * @return true if the specified image file is loaded from resource successfully
	 */
	public boolean load_image_from_resource_all(String filename) {
		if ( !load_image_from_resource( filename))
			return false;

		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)entry.getValue();
			if ( !spotObjectManipulator.load_image_from_resource( filename))
				return false;
		}

		return true;
	}
}
