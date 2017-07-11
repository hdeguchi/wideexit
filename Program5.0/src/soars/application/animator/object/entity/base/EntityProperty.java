/**
 * 
 */
package soars.application.animator.object.entity.base;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soars.application.animator.object.entity.agent.AgentObjectManager;
import soars.application.animator.object.entity.spot.ISpotObjectManipulator;
import soars.application.animator.object.entity.spot.SpotObjectManager;

/**
 * @author kurata
 *
 */
public class EntityProperty {

	/**
	 * Name of this object.
	 */
	public String _name = "";

	/**
	 * True if this object is visible.
	 */
	public boolean _visible = true;

	/**
	 * True if the name string is visible.
	 */
	public boolean _visibleName = true;

	/**
	 * Name of image file.
	 */
	public String _imageFilename = "";

	/**
	 * Color for this object.
	 */
	public Color _imageColor = null;

	/**
	 * Color for the name of this object.
	 */
	public Color _textColor = null; 

	/**
	 * Font object for the name string of this object.
	 */
	public Font _font = null;

	/**
	 * 
	 */
	public int _number = 0;

	/**
	 * @param entityBase
	 */
	public EntityProperty(EntityBase entityBase) {
		super();
		_name = entityBase._name;
		_visible = entityBase._visible;
		_visibleName = entityBase._visibleName;
		_imageFilename = entityBase._imageFilename;
		_imageColor = new Color( entityBase._imageColor.getRed(), entityBase._imageColor.getGreen(), entityBase._imageColor.getBlue());
		_textColor = new Color( entityBase._textColor.getRed(), entityBase._textColor.getGreen(), entityBase._textColor.getBlue());
		_font = new Font( entityBase._font.getFamily(), entityBase._font.getStyle(), entityBase._font.getSize());
	}

	/**
	 * @param name
	 * @param number
	 * @param entityBase
	 */
	public EntityProperty(String name, int number, EntityBase entityBase) {
		super();
		_name = name;
		_number = number;
		_visible = entityBase._visible;
		_visibleName = entityBase._visibleName;
		_imageFilename = entityBase._imageFilename;
		_imageColor = new Color( entityBase._imageColor.getRed(), entityBase._imageColor.getGreen(), entityBase._imageColor.getBlue());
		_textColor = new Color( entityBase._textColor.getRed(), entityBase._textColor.getGreen(), entityBase._textColor.getBlue());
		_font = new Font( entityBase._font.getFamily(), entityBase._font.getStyle(), entityBase._font.getSize());
	}

	/**
	 * Returns true if this object is updated successfully.
	 * @param spot true if objects are spots.
	 * @param spotObjectManager 
	 * @param agentObjectManager
	 * @param graphics2D the graphics object of JAVA
	 * @return true if objects are updated successfully
	 */
	public boolean update(boolean spot, SpotObjectManager spotObjectManager, AgentObjectManager agentObjectManager, Graphics2D graphics2D) {
		Map map = ( spot ? spotObjectManager : agentObjectManager);
		if ( 0 == _number) {
			EntityBase entityBase = ( EntityBase)map.get( _name);
			if ( null == entityBase)
				return false;

			return entityBase.update( _visible, _visibleName, _imageColor.getRed(), _imageColor.getGreen(), _imageColor.getBlue(),
				_textColor.getRed(), _textColor.getGreen(), _textColor.getBlue(), _font.getFamily(), _font.getStyle(), _font.getSize(), _imageFilename, graphics2D);
		} else {
			for ( int i = 1; i <= _number; ++i) {
				EntityBase entityBase = ( EntityBase)map.get( _name + String.valueOf( i));
				if ( null == entityBase)
					return false;

				if ( !entityBase.update( _visible, _visibleName, _imageColor.getRed(), _imageColor.getGreen(), _imageColor.getBlue(),
					_textColor.getRed(), _textColor.getGreen(), _textColor.getBlue(), _font.getFamily(), _font.getStyle(), _font.getSize(), _imageFilename, graphics2D))
					return false;
			}
		}
		return true;
	}

	/**
	 * @param list
	 * @param spotObjectManager
	 * @return
	 */
	public boolean get_entityBases(List list, SpotObjectManager spotObjectManager) {
		if ( 0 == _number) {
			EntityBase entityBase = ( EntityBase)spotObjectManager.get( _name);
			if ( null == entityBase)
				return false;

			list.add( entityBase);
		} else {
			for ( int i = 1; i <= _number; ++i) {
				EntityBase entityBase = ( EntityBase)spotObjectManager.get( _name + String.valueOf( i));
				if ( null == entityBase)
					return false;

				list.add( entityBase);
			}
		}
		return true;
	}

	/**
	 * @param spotPositionMap
	 * @param spotObjectManager 
	 */
	public void restore(HashMap spotPositionMap, SpotObjectManager spotObjectManager) {
		if ( 0 == _number) {
			EntityBase entityBase = ( EntityBase)spotObjectManager.get( _name);
			if ( null == entityBase)
				return;

			restore( entityBase, spotPositionMap);
		} else {
			for ( int i = 1; i <= _number; ++i) {
				EntityBase entityBase = ( EntityBase)spotObjectManager.get( _name + String.valueOf( i));
				if ( null == entityBase)
					return;

				restore( entityBase, spotPositionMap);
			}
		}
	}

	/**
	 * @param entityBase
	 * @param spotPositionMap
	 */
	private void restore(EntityBase entityBase, HashMap spotPositionMap) {
		Point position = ( Point)spotPositionMap.get( entityBase._name);
		if ( null == position)
			return;

		ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)entityBase;
		spotObjectManipulator.move_to( position.x, position.y, false);
	}

	/**
	 * 
	 */
	public void debug() {
		System.out.println( _name + " : " + String.valueOf( _number));
	}
}
