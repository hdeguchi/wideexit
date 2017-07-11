/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.inheritance;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.role.base.Role;

/**
 * @author kurata
 *
 */
public class ConnectObject {

	/**
	 * 
	 */
	public Role _parent = null;

	/**
	 * 
	 */
	public List<ConnectObject> _connectObjects = new ArrayList<ConnectObject>();

	/**
	 * @param parent
	 */
	public ConnectObject(Role parent) {
		super();
		_parent = parent;
	}

	/**
	 * @param connectObject
	 * @param parent
	 */
	public ConnectObject(ConnectObject connectObject, Role parent) {
		super();
		_parent = parent;
	}

	/**
	 * 
	 */
	public void cleanup() {
		_connectObjects.clear();
	}

	/**
	 * @return
	 */
	public String get_name() {
		String name = _parent._name;
		for ( int i = 0; i < _connectObjects.size(); ++i) {
			ConnectObject connectObject = ( ConnectObject)_connectObjects.get( i);
			name += ( ":" + connectObject._parent._name);
		}
		return name;
	}

	/**
	 * @param graphics2D
	 */
	public void update_name_dimension(Graphics2D graphics2D) {
	}

	/**
	 * @param position
	 * @param dimension
	 * @param graphics2D
	 * @param imageObserver
	 */
	public void draw(Point position, Dimension dimension, Graphics2D graphics2D, ImageObserver imageObserver) {
	}

	/**
	 * @param graphics2D
	 */
	public void draw_connection(Graphics2D graphics2D) {
		for ( int i = 0; i < _connectObjects.size(); ++i) {
			ConnectObject connectObject = ( ConnectObject)_connectObjects.get( i);
			graphics2D.setColor( _parent._imageColor);
			graphics2D.drawLine( get_center().x, get_center().y, connectObject.get_center().x, connectObject.get_center().y);
			//GraphicTool.drawFillRhombusArrow( graphics2D, get_center().x, get_center().y, connectObject.get_center().x, connectObject.get_center().y, 8);
			//GraphicTool.drawArrow( graphics2D, get_center().x, get_center().y, connectObject.get_center().x, connectObject.get_center().y, 12);
			//GraphicTool.drawWarrow( graphics2D, get_center().x, get_center().y, connectObject.get_center().x, connectObject.get_center().y, 15, 2);
			//graphics2D.drawLine( get_center().x, get_center().y, connectObject.get_center().x, connectObject.get_center().y);
		}
	}

	/**
	 * @param point
	 * @param x
	 * @param y
	 * @return
	 */
	protected boolean contains(Point point, int x, int y) {
		return ( ( Math.pow( point.x - x, 2.0) + Math.pow( point.y - y, 2.0)) < Math.pow( Constant._visualShellRoleConnectionRadius, 2.0));
	}

	/**
	 * @return
	 */
	public Point get_center() {
		return null;
	}

	/**
	 * @param connectObject
	 */
	public void connect(ConnectObject connectObject) {
		connect( this, connectObject);
		connect( connectObject, this);
	}

	/**
	 * @param connectObject1
	 * @param connectObject2
	 */
	private void connect(ConnectObject connectObject1, ConnectObject connectObject2) {
		if ( connectObject1._connectObjects.contains( connectObject2))
			return;

		connectObject1._connectObjects.add( connectObject2);
	}

	/**
	 * @param connectObject
	 */
	public void disconnect(ConnectObject connectObject) {
		disconnect( this, connectObject);
		disconnect( connectObject, this);
	}

	/**
	 * @param connectObject1
	 * @param connectObject2
	 */
	private void disconnect(ConnectObject connectObject1, ConnectObject connectObject2) {
		if ( !connectObject1._connectObjects.contains( connectObject2))
			return;

		connectObject1._connectObjects.remove( connectObject2);
	}

	/**
	 * @return
	 */
	public boolean can_disconnect() {
		return !_connectObjects.isEmpty();
	}
}
