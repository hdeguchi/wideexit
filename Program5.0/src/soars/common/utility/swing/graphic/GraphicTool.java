/**
 * 
 */
package soars.common.utility.swing.graphic;

import java.awt.Graphics2D;
import java.awt.Polygon;

/**
 * @author kurata
 *
 */
public class GraphicTool {

	/**
	 * @param graphics2D
	 * @param x
	 * @param y
	 * @param r
	 */
	public static void drawCircle(Graphics2D graphics2D, int x, int y, int r) {
		graphics2D.drawOval( x - r, y - r, r * 2, r * 2);
	}
  
	/**
	 * @param graphics2D
	 * @param x
	 * @param y
	 * @param r
	 */
	public static void fillCircle(Graphics2D graphics2D, int x, int y, int r) {
		graphics2D.fillOval( x - r, y - r, r * 2, r * 2);
	}

  /**
	 * @param graphics2D
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param l
	 */
	public static void drawArrow(Graphics2D graphics2D, int x0, int y0, int x1, int y1, int l) {
		drawArrow( graphics2D, x0, y0, x1, y1, l, Math.PI / 6.0);
	}

  /**
	 * @param graphics2D
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param l
	 * @param dt
	 */
	public static void drawArrow(Graphics2D graphics2D, int x0, int y0, int x1, int y1, int l, double dt) {
		double theta;
		int x, y;
		theta = Math.atan2( ( double)( y1 - y0), ( double)( x1 - x0));
		graphics2D.drawLine( x0, y0, x1, y1);
		x = x1 - ( int)( l * Math.cos( theta - dt));
		y = y1 - ( int)( l * Math.sin( theta - dt));
		graphics2D.drawLine( x1, y1, x, y);
		x = x1 - ( int)( l * Math.cos( theta + dt));
		y = y1 - ( int)( l * Math.sin( theta + dt));
		graphics2D.drawLine( x1, y1, x, y);
	}

  /**
	 * @param graphics2D
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param l
	 */
	public static void drawFillArrow(Graphics2D graphics2D, int x0, int y0, int x1, int y1, int l) {
		drawFillArrow( graphics2D, x0, y0, x1, y1, l, Math.PI / 6.0);
	}

  /**
	 * @param graphics2D
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param l
	 * @param dt
	 */
	public static void drawFillArrow(Graphics2D graphics2D, int x0, int y0, int x1, int y1, int l, double dt) {
		Polygon polygon = new Polygon();
		double theta;
		int x, y;
		theta = Math.atan2( ( double)( y1 - y0), ( double)( x1 - x0));
		graphics2D.drawLine( x0, y0, x1, y1);
		polygon.addPoint( x1, y1);
		x = x1 - ( int)( l * Math.cos( theta - dt));
		y = y1 - ( int)( l * Math.sin( theta - dt));
		polygon.addPoint( x, y);
		x = x1 - ( int)( l * Math.cos( theta + dt));
		y = y1 - ( int)( l * Math.sin( theta + dt));
		polygon.addPoint( x, y);
		graphics2D.fillPolygon( polygon);
	}

  /**
	 * @param graphics2D
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param l
	 */
	public static void drawFillRhombusArrow(Graphics2D graphics2D, int x0, int y0, int x1, int y1, int l) {
		drawFillRhombusArrow( graphics2D, x0, y0, x1, y1, l, Math.PI / 6.0);
	}

  /**
	 * @param graphics2D
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param l
	 * @param dt
	 */
	public static void drawFillRhombusArrow(Graphics2D graphics2D, int x0, int y0, int x1, int y1, int l, double dt) {
		Polygon polygon = new Polygon();
		double theta;
		int x, y;
		theta = Math.atan2( ( double)( y1 - y0), ( double)( x1 - x0));
		graphics2D.drawLine( x0, y0, x1, y1);
		polygon.addPoint( x1, y1);
		x = x1 - ( int)( l * Math.cos( theta - dt));
		y = y1 - ( int)( l * Math.sin( theta - dt));
		polygon.addPoint( x, y);
		x = x1 - ( int)( 2 * l * Math.cos( theta));
		y = y1 - ( int)( 2 * l * Math.sin( theta));
		polygon.addPoint( x, y);
		x = x1 - ( int)( l * Math.cos( theta + dt));
		y = y1 - ( int)( l * Math.sin( theta + dt));
		polygon.addPoint( x, y);
		graphics2D.fillPolygon( polygon);
	}

	/**
	 * @param graphics2D
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param w
	 */
	public static void drawWline(Graphics2D graphics2D, int x0, int y0, int x1, int y1, int w) {
		int dx, dy, nxi, nyi;
		double d, nx, ny;
		int i;
		int[] x = new int[ 4];
		int[] y = new int[ 4];

		dx = x1 - x0;
		dy = y1 - y0;
		d = Math.sqrt( dx * dx + dy * dy);
		nx = ( double)dy / d;
		ny = -( double)dx / d;
		nxi = ( int)( Math.rint( nx * w / 2.0));
		nyi = ( int)( Math.rint( ny * w / 2.0));
		x[ 0] = x0 - nxi;
		y[ 0] = y0 - nyi;
		x[ 1] = x1 - nxi;
		y[ 1] = y1 - nyi;
		x[ 2] = x1 + nxi;
		y[ 2] = y1 + nyi;
		x[ 3] = x0 + nxi;
		y[ 3] = y0 + nyi;
		graphics2D.fillPolygon( x, y, 4);
	}

	/**
	 * @param graphics2D
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param l
	 * @param w
	 */
	public static void drawWarrow(Graphics2D graphics2D, int x0, int y0, int x1, int y1, int l, int w) {
		double theta;
		int x, y;
		double dt = Math.PI / 6.0;
		theta = Math.atan2( ( double)( y1 - y0),( double)( x1 - x0));
		drawWline( graphics2D, x0, y0, x1, y1, w);
		x = x1 - ( int)( l * Math.cos( theta - dt));
		y = y1 - ( int)( l * Math.sin( theta - dt));
		drawWline( graphics2D, x1, y1, x, y, w);
		x = x1 - ( int)( l * Math.cos( theta + dt));
		y = y1 - ( int)( l * Math.sin( theta + dt));
		drawWline( graphics2D, x1, y1, x, y, w);
	}

	/**
	 * @param graphics2D
	 * @param x
	 * @param y
	 * @param r
	 * @param w
	 */
	public static void drawWcircle(Graphics2D graphics2D, int x, int y, int r, int w) {
		for ( int i = r - ( int)( w / 2); i <= r + ( int)( w / 2); i++)
			drawCircle( graphics2D, x, y, i);
	}
}
