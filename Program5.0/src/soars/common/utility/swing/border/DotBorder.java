/**
 * 
 */
package soars.common.utility.swing.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.SystemColor;

import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicGraphicsUtils;

/**
 * The customized empty border class.
 * @author kurata / SOARS project
 */
public class DotBorder extends EmptyBorder {

	/**
	 * Creates an customized empty border with the specified insets.
	 * @param insets the insets of the border
	 */
	public DotBorder(Insets insets) {
		super(insets);
	}

	/**
	 * Creates an customized empty border with the specified insets.
	 * @param top the top inset of the border
	 * @param left the left inset of the border
	 * @param bottom the bottom inset of the border
	 * @param right the right inset of the border
	 */
	public DotBorder(int top, int left, int bottom, int right) {
		super(top, left, bottom, right);
	}

	/* (non-Javadoc)
	 * @see javax.swing.border.EmptyBorder#isBorderOpaque()
	 */
	public boolean isBorderOpaque() {
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.swing.border.EmptyBorder#paintBorder(java.awt.Component, java.awt.Graphics, int, int, int, int)
	 */
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    Graphics2D graphics2D = ( Graphics2D)g;
    graphics2D.translate( x, y);
    graphics2D.setPaint( new Color( ~SystemColor.activeCaption.getRGB()));
    BasicGraphicsUtils.drawDashedRect( graphics2D, 0, 0, width, height);
    graphics2D.translate( -x, -y);
	}
}
