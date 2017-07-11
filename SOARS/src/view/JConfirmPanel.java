package view;

import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * The JConfirmPanel class represents window with confirmation when closing.
 * @author H. Tanuma / SOARS project
 */
public abstract class JConfirmPanel extends JPanel implements Confirmable {

	private static final long serialVersionUID = -8384892723326351571L;
	
	public JConfirmPanel() {
		super();
	}
	public JConfirmPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}
	public JConfirmPanel(LayoutManager layout) {
		super(layout);
	}
	public JConfirmPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}
}
