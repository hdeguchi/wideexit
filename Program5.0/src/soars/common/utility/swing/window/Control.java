/*
 * Created on 2006/01/17
 */
package soars.common.utility.swing.window;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

/**
 * @author kurata
 */
public class Control extends JComponent {

	/**
	 * 
	 */
	public Control() {
		super();

		addComponentListener( new ComponentListener() {
			public void componentHidden(ComponentEvent arg0) {
				on_component_hidden( arg0);
			}
			public void componentMoved(ComponentEvent arg0) {
				on_component_moved( arg0);
			}
			public void componentResized(ComponentEvent arg0) {
				on_component_resized( arg0);
			}
			public void componentShown(ComponentEvent arg0) {
				on_component_shown( arg0);
			}
		});

		addMouseListener( new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
				on_mouse_clicked( arg0);
			}
			public void mouseEntered(MouseEvent arg0) {
				on_mouse_enterd( arg0);
			}
			public void mouseExited(MouseEvent arg0) {
				on_mouse_exited( arg0);
			}
			public void mousePressed(MouseEvent arg0) {
				on_mouse_pressed( arg0);
			}
			public void mouseReleased(MouseEvent arg0) {
				on_mouse_released( arg0);
			}
		});

		addMouseMotionListener( new MouseMotionListener() {
			public void mouseDragged(MouseEvent arg0) {
				on_mouse_dragged( arg0);
			}
			public void mouseMoved(MouseEvent arg0) {
				on_mouse_moved( arg0);
			}
		});
	}

	/**
	 * @param componentEvent
	 */
	protected void on_component_hidden(ComponentEvent componentEvent) {
	}

	/**
	 * @param componentEvent
	 */
	protected void on_component_moved(ComponentEvent componentEvent) {
	}

	/**
	 * @param componentEvent
	 */
	protected void on_component_resized(ComponentEvent componentEvent) {
	}

	/**
	 * @param componentEvent
	 */
	protected void on_component_shown(ComponentEvent componentEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_clicked(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_enterd(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_exited(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_pressed(MouseEvent mouseEvent) {
		switch ( mouseEvent.getButton()) {
			case 1:
				switch ( mouseEvent.getClickCount()) {
					case 1:
						on_mouse_left_down( mouseEvent);
						break;
					case 2:
						on_mouse_left_double_click( mouseEvent);
						break;
				}
				break;
			case 2:
				switch ( mouseEvent.getClickCount()) {
					case 1:
						on_mouse_middle_down( mouseEvent);
						break;
					case 2:
						on_mouse_middle_double_click( mouseEvent);
						break;
				}
				break;
			case 3:
				switch ( mouseEvent.getClickCount()) {
					case 1:
						on_mouse_right_down( mouseEvent);
						break;
					case 2:
						on_mouse_right_double_click( mouseEvent);
						break;
				}
				break;
		}
//		if ( 1 != mouseEvent.getClickCount())
//			return;
//
//		switch ( mouseEvent.getButton()) {
//			case 1:
//				on_mouse_left_down( mouseEvent);
//				break;
//			case 2:
//				on_mouse_middle_down( mouseEvent);
//				break;
//			case 3:
//				on_mouse_right_down( mouseEvent);
//				break;
//		}
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_left_down(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_middle_down(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_right_down(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_released(MouseEvent mouseEvent) {
		switch ( mouseEvent.getButton()) {
			case 1:
				on_mouse_left_up( mouseEvent);
				break;
			case 2:
				on_mouse_middle_up( mouseEvent);
				break;
			case 3:
				on_mouse_right_up( mouseEvent);
				break;
		}
//		switch ( mouseEvent.getButton()) {
//			case 1:
//				switch ( mouseEvent.getClickCount()) {
//					case 1:
//						on_mouse_left_up( mouseEvent);
//						break;
//					case 2:
//						on_mouse_left_double_click( mouseEvent);
//						break;
//				}
//				break;
//			case 2:
//				switch ( mouseEvent.getClickCount()) {
//					case 1:
//						on_mouse_middle_up( mouseEvent);
//						break;
//					case 2:
//						on_mouse_middle_double_click( mouseEvent);
//						break;
//				}
//				break;
//			case 3:
//				switch ( mouseEvent.getClickCount()) {
//					case 1:
//						on_mouse_right_up( mouseEvent);
//						break;
//					case 2:
//						on_mouse_right_double_click( mouseEvent);
//						break;
//				}
//				break;
//		}
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_left_up(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_middle_up(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_left_double_click(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_middle_double_click(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_right_double_click(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_dragged(MouseEvent mouseEvent) {
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_moved(MouseEvent mouseEvent) {
	}
}
