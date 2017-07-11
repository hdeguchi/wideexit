/**
 * 
 */
package soars.common.utility.swing.label;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import soars.common.utility.tool.network.BrowserLauncher;

/**
 * @author kurata
 *
 */
public class URLLabel extends JPanel {

	/**
	 * 
	 */
	private final String url;

	/**
	 * 
	 */
	private final String text;

	/**
	 * 
	 */
	private final Point pos = new Point();

	/**
	 * 
	 */
	private final Color orgColor = getForeground();

	/**
	 * 
	 */
	private final Color textColor = new Color( 0, 0, 200);

	/**
	 * 
	 */
	private final Cursor cc = getCursor();

	/**
	 * 
	 */
	private final Cursor hc = new Cursor(Cursor.HAND_CURSOR);

	/**
	 * 
	 */
	private boolean flg = false;

	/**
	 * 
	 */
	private int width;

	/**
	 * 
	 */
	private int height;

	/**
	 * 
	 */
	public URLLabel(String str) {
		super();
		url  = str;
		text = str;
		iniitialize();
	}

	/**
	 * 
	 */
	public URLLabel(String str1, String str2) {
		super();
		url  = str1;
		text = str2;
		iniitialize();
	}

	/**
	 * 
	 */
	private void iniitialize() {
		Font font = getFont();
		width  = SwingUtilities.computeStringWidth(getFontMetrics(font), text);
		height = font.getSize();
		setPreferredSize( new Dimension( width * 3 / 2, height + 4));
		addComponentListener(new ComponentAdapter() {

	/**
	 * 
	 */
			public void componentResized(ComponentEvent e) {
				JComponent c = (JComponent)e.getSource();
				pos.setLocation(c.getInsets().left, c.getSize().height - 4);
				//pos.setLocation(c.getInsets().left, (c.getSize().height+height)/2);
				c.repaint();
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {

	/**
	 * 
	 */
			public void mouseMoved(MouseEvent e) {
				JComponent c = (JComponent)e.getSource();
				flg = isOnURL(e.getPoint());
				if(flg) {
					c.setCursor(hc);
				}else{
					c.setCursor(cc);
				}
				c.repaint();
			}
		});
		addMouseListener(new MouseAdapter() {

	/**
	 * 
	 */
			public void mouseExited(MouseEvent e) {
				JComponent c = (JComponent)e.getSource();
				c.setCursor(cc);
				flg = false;
			}

	/**
	 * 
	 */
			public void mousePressed(MouseEvent e) {
				BrowserLauncher.openURL(url);
			}
		});
	}

	/**
	 * 
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
//		if(flg) {
//			g2.setPaint(SystemColor.activeCaption);
			g2.setPaint(textColor);
			g2.drawString(text, pos.x, pos.y);
			g2.drawLine(pos.x, pos.y+1, pos.x+width, pos.y+1);
//			flg = false;
//		}else{
//			g2.setPaint(orgColor);
//			g2.drawString(url, pos.x, pos.y);
//		}
	}

	/**
	 * 
	 */
	private boolean isOnURL(Point p) {
		Rectangle2D rect = new Rectangle2D.Float(getInsets().left, pos.y-height/2, width, height);
		return rect.contains(p.x, p.y);
	}
}
