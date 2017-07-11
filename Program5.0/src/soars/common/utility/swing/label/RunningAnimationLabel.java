/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  Copyright 2007-2009  SSAC(Systems of Social Accounting Consortium)
 *  <author> Yasunari Ishizuka (PieCake,Inc.)
 *  <author> Hiroshi Deguchi (TOKYO INSTITUTE OF TECHNOLOGY)
 *  <author> Yuji Onuki (Statistics Bureau)
 *  <author> Shungo Sakaki (Tokyo University of Technology)
 *  <author> Akira Sasaki (HOSEI UNIVERSITY)
 *  <author> Hideki Tanuma (TOKYO INSTITUTE OF TECHNOLOGY)
 */
/*
 * @(#)RunningAnimationLabel.java	1.00	2008/03/24
 *     - created by Y.Ishizuka(PieCake.inc,)
 */
//package ssac.util.swing;
package soars.common.utility.swing.label;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * 実行状態を表示するアニメーション・ラベル・コンポーネント。
 * 
 * @version 1.00 2008/03/24
 */
public final class RunningAnimationLabel extends JLabel
{
	//------------------------------------------------------------
	// Definitions
	//------------------------------------------------------------
	
	/*
	static private final String[] STATE = new String[]{
		"　", "｜", "／", "－", "＼",
	};
	*/
	
	//------------------------------------------------------------
	// Fields
	//------------------------------------------------------------
	
	private final AnimeIcon icon = new AnimeIcon();

	private boolean	isRunning;
	//private int		istate;

	//------------------------------------------------------------
	// Constructions
	//------------------------------------------------------------
	
	public RunningAnimationLabel() {
		super();
		//super(" "+STATE[0]+" ");
		this.isRunning = false;
		this.setIcon(this.icon);
		//this.istate = 0;
		//this.setHorizontalAlignment(JLabel.CENTER);
		//Dimension dm = this.getPreferredSize();
		//this.setPreferredSize(dm);
		//this.setMinimumSize(dm);
		//this.setMaximumSize(dm);
	}

	//------------------------------------------------------------
	// Public interfaces
	//------------------------------------------------------------
	
	public boolean isRunning() {
		return this.isRunning;
	}
	
	public void setRunning(boolean running) {
		if (this.isRunning && !running) {
			// stop
			this.isRunning = false;
			//this.istate = 0;
			//this.setText(STATE[0]);
			this.icon.setRunning(this.isRunning);
			this.repaint();
		}
		else if (!this.isRunning && running) {
			// start
			this.isRunning = true;
			//this.istate = 1;
			//this.setText(STATE[1]);
			this.icon.setRunning(this.isRunning);
			this.repaint();
		}
	}
	
	public void next() {
		/*
		if (this.isRunning) {
			this.istate++;
			if (this.istate >= STATE.length) {
				this.istate = 1;
			}
			this.setText(STATE[this.istate]);
		}
		*/
		this.icon.next();
		this.repaint();
	}

	//------------------------------------------------------------
	// Internal methods
	//------------------------------------------------------------

	//------------------------------------------------------------
	// implement AnimeIcon class
	//------------------------------------------------------------
	
	static class AnimeIcon implements Icon
	{
		//------------------------------------------------------------
		// Definitions
		//------------------------------------------------------------
		
		static private final Color cColor = new Color(0.5f,0.5f,0.5f);
		
		//------------------------------------------------------------
		// Fields
		//------------------------------------------------------------

		private final Vector<Shape> list = new Vector<Shape>();
		private final Dimension dim;
		private boolean isRunning = false;

		//------------------------------------------------------------
		// Constructions
		//------------------------------------------------------------
		
		public AnimeIcon() {
			super();
			double r  = 2.0d;
			double sx = 1.0d;
			double sy = 1.0d;
			list.addElement(new Ellipse2D.Double(sx+3*r, sy+0*r, 2*r, 2*r));
			list.addElement(new Ellipse2D.Double(sx+5*r, sy+1*r, 2*r, 2*r));
			list.addElement(new Ellipse2D.Double(sx+6*r, sy+3*r, 2*r, 2*r));
			list.addElement(new Ellipse2D.Double(sx+5*r, sy+5*r, 2*r, 2*r));
			list.addElement(new Ellipse2D.Double(sx+3*r, sy+6*r, 2*r, 2*r));
			list.addElement(new Ellipse2D.Double(sx+1*r, sy+5*r, 2*r, 2*r));
			list.addElement(new Ellipse2D.Double(sx+0*r, sy+3*r, 2*r, 2*r));
			list.addElement(new Ellipse2D.Double(sx+1*r, sy+1*r, 2*r, 2*r));
			dim = new Dimension((int)(r*8+sx*2), (int)(r*8+sy*2));
		}

		//------------------------------------------------------------
		// Public interfaces
		//------------------------------------------------------------
		
		public int getIconWidth() {
			return dim.width;
		}
		
		public int getIconHeight() {
			return dim.height;
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setPaint((c!=null)?c.getBackground():Color.WHITE);
			g2d.fillRect(x, y, getIconWidth(), getIconHeight());
			if (this.isRunning) {
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(cColor);
				float alpha = 0.0f;
				g2d.translate(x, y);
				for(Shape s: list) {
					alpha = isRunning?alpha+0.1f:0.5f;
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
					g2d.fill(s);
				}
				g2d.translate(-x, -y);
			}
		}
		
		public void next() {
			if(isRunning) list.addElement(list.remove(0));
		}
		
		public void setRunning(boolean isRunning) {
			this.isRunning = isRunning;
		}
	}
}
