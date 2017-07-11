/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.inheritance;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.util.Vector;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.base.DrawObject;
import soars.application.visualshell.object.role.base.Role;


/**
 * The connection object for role object.
 * @author kurata / SOARS project
 */
public class ConnectInObject extends ConnectObject {

	/**
	 * @param parent
	 */
	public ConnectInObject(Role parent) {
		super(parent);
	}

	/**
	 * @param connectInObject
	 * @param parent
	 */
	public ConnectInObject(ConnectInObject connectInObject, Role parent) {
		super(connectInObject, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.inheritance.ConnectObject#update_name_dimension(java.awt.Graphics2D)
	 */
	public void update_name_dimension(Graphics2D graphics2D) {
		_parent.setup_name_dimension( graphics2D);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.inheritance.ConnectObject#draw(java.awt.Point, java.awt.Dimension, java.awt.Graphics2D, java.awt.image.ImageObserver)
	 */
	public void draw(Point position, Dimension dimension, Graphics2D graphics2D, ImageObserver imageObserver) {
		graphics2D.drawLine( position.x + ( dimension.width / 2), position.y, position.x + ( dimension.width / 2), position.y - Constant._visualShellRoleConnectionLength);
		graphics2D.drawOval( position.x + ( dimension.width / 2) - Constant._visualShellRoleConnectionRadius, position.y - Constant._visualShellRoleConnectionLength - ( Constant._visualShellRoleConnectionRadius * 2), Constant._visualShellRoleConnectionRadius * 2, Constant._visualShellRoleConnectionRadius * 2);
	}

	/**
	 * @param point
	 * @param position
	 * @param dimension
	 * @return
	 */
	public boolean contains(Point point, Point position, Dimension dimension) {
		return contains( point, position.x + ( dimension.width / 2), position.y - Constant._visualShellRoleConnectionLength - Constant._visualShellRoleConnectionRadius);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.inheritance.ConnectObject#get_center()
	 */
	public Point get_center() {
		return new Point( _parent._position.x + ( _parent._dimension.width / 2), _parent._position.y - Constant._visualShellRoleConnectionLength - Constant._visualShellRoleConnectionRadius);
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	public boolean is_closure(Vector<DrawObject> drawObjects) {
		for ( int i = 0; i < _connectObjects.size(); ++i) {
			ConnectOutObject connectOutObject = ( ConnectOutObject)_connectObjects.get( i);
			if ( !drawObjects.contains( connectOutObject._parent))
				return false;

			if ( !connectOutObject._parent.is_closure( drawObjects))
				return false;
		}

		return true;
	}
}
