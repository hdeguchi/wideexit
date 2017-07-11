/*
 * 2005/02/04
 */
package soars.application.animator.object.common.tool;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import soars.application.animator.object.entity.base.EntityBase;

/**
 * The common tools class.
 * @author kurata / SOARS project
 */
public class CommonObjectTool {

	/**
	 * 
	 */
	public static final int _side = 6;

	/**
	 * Draws the frame of selected object.
	 * @param graphics2D the graphics object of JAVA
	 * @param position the position of the selected object
	 * @param dimension the size of the selected object
	 * @param color the color of the frame
	 */
	public static void draw_frame(Graphics2D graphics2D, Point position, Dimension dimension, Color color) {
		graphics2D.setColor( color);
		graphics2D.fill3DRect( position.x - _side, position.y - _side, _side, _side, false);
		graphics2D.fill3DRect( position.x + dimension.width, position.y - _side, _side, _side, false);
		graphics2D.fill3DRect( position.x - _side, position.y + dimension.height, _side, _side, false);
		graphics2D.fill3DRect( position.x + dimension.width, position.y + dimension.height, _side, _side, false);
		graphics2D.fill3DRect( position.x + ( ( dimension.width - _side) / 2), position.y - _side, _side, _side, false);
		graphics2D.fill3DRect( position.x - _side, position.y + ( ( dimension.height - _side) / 2), _side, _side, false);
		graphics2D.fill3DRect( position.x + dimension.width, position.y + ( ( dimension.height - _side) / 2), _side, _side, false);
		graphics2D.fill3DRect( position.x + ( ( dimension.width - _side) / 2), position.y + dimension.height, _side, _side, false);
	}

	/**
	 * @param position
	 * @param dimension
	 * @param mousePoint
	 * @return
	 */
	public static Cursor get_cursor(Point position, Dimension dimension, Point mousePoint) {
		int cursorType = get_cursor_type( position, dimension, mousePoint);
		return ( ( Cursor.DEFAULT_CURSOR == cursorType) ? Cursor.getDefaultCursor() : new Cursor( cursorType));
	}

	/**
	 * @param position
	 * @param dimension
	 * @param mousePoint
	 * @return
	 */
	public static int get_cursor_type(Point position, Dimension dimension, Point mousePoint) {
		if ( mousePoint.x >= position.x - _side && mousePoint.x <= position.x) {
			if ( mousePoint.y >= position.y - _side && mousePoint.y <= position.y)
				return Cursor.NW_RESIZE_CURSOR;
			else if ( mousePoint.y >= position.y + ( ( dimension.height - _side) / 2) && mousePoint.y <= position.y + ( ( dimension.height + _side) / 2))
				return Cursor.W_RESIZE_CURSOR;
			else if ( mousePoint.y >= position.y + dimension.height && mousePoint.y <= position.y + dimension.height + _side)
				return Cursor.SW_RESIZE_CURSOR;
		} else if ( mousePoint.x >= position.x + ( ( dimension.width - _side) / 2) && mousePoint.x <= position.x + ( ( dimension.width + _side) / 2)) {
			if ( mousePoint.y >= position.y - _side && mousePoint.y <= position.y)
				return Cursor.N_RESIZE_CURSOR;
			else if ( mousePoint.y >= position.y + dimension.height && mousePoint.y <= position.y + dimension.height + _side)
				return Cursor.S_RESIZE_CURSOR;
		} else if ( mousePoint.x >= position.x + dimension.width && mousePoint.x <= position.x + dimension.width + _side) {
			if ( mousePoint.y >= position.y - _side && mousePoint.y <= position.y)
				return Cursor.NE_RESIZE_CURSOR;
			else if ( mousePoint.y >= position.y + ( ( dimension.height - _side) / 2) && mousePoint.y <= position.y + ( ( dimension.height + _side) / 2))
				return Cursor.E_RESIZE_CURSOR;
			else if ( mousePoint.y >= position.y + dimension.height && mousePoint.y <= position.y + dimension.height + _side)
				return Cursor.SE_RESIZE_CURSOR;
		}
		return Cursor.DEFAULT_CURSOR;
	}

	/**
	 * Returs the array of the name of the image file set to the objects.
	 * @param entityBases the objects
	 * @return the array of the name of the image file set to the objects
	 */
	public static String[] get_image_filenames(Vector entityBases) {
		List list = new ArrayList();
		for ( int i = 0; i < entityBases.size(); ++i) {
			EntityBase entityBase = ( EntityBase)entityBases.get( i);
			if ( entityBase._imageFilename.equals( ""))
				continue;

			if ( list.contains( entityBase._imageFilename))
				continue;

			list.add( entityBase._imageFilename);
		}
		return ( String[])list.toArray( new String[ 0]);
	}
}
