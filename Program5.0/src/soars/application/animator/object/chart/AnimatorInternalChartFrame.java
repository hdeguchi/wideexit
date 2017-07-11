/**
 * 
 */
package soars.application.animator.object.chart;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.event.InternalFrameEvent;

import soars.application.animator.main.MainFrame;
import soars.application.animator.observer.Observer;
import soars.plugin.modelbuilder.chart.chart.main.InternalChartFrame;

/**
 * @author kurata
 *
 */
public class AnimatorInternalChartFrame extends InternalChartFrame {

	/**
	 * Default width.
	 */
	static public final int _minimumWidth = 400;

	/**
	 * Default height.
	 */
	static public final int _minimumHeight = 300;

	/**
	 * 
	 */
	public int _order = -1;

	/**
	 * @param order
	 * 
	 */
	public AnimatorInternalChartFrame(int order) {
		super();
		_order = order;
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.InternalChartFrame#on_create()
	 */
	protected boolean on_create() {
		if (!super.on_create())
			return false;

		addComponentListener( new ComponentAdapter() {
			public void componentMoved(ComponentEvent e){
				int x = getLocation().x;
				int y = getLocation().y;
				x = ( 100 - getSize().width > x) ? 100 - getSize().width : x;
				y = ( -10 > y) ? -10 : y;
				x = ( MainFrame.get_instance().getContentPane().getSize().width - 50 < x) ? MainFrame.get_instance().getContentPane().getSize().width - 50 : x;
				y = ( MainFrame.get_instance().getContentPane().getSize().height - 60 < y) ? MainFrame.get_instance().getContentPane().getSize().height - 60 : y;
				setLocation( x, y);
				MainFrame.get_instance().repaint();
			}
			public void componentResized(ComponentEvent e){
				int width = getSize().width;
				int height = getSize().height;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width, ( _minimumHeight > height) ? _minimumHeight : height);
			}
		});

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_internal_frame_deiconified(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_deiconified(InternalFrameEvent internalFrameEvent) {
		Observer.get_instance().modified();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_internal_frame_iconified(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_iconified(InternalFrameEvent internalFrameEvent) {
		Observer.get_instance().modified();
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.InternalChartFrame#on_internal_frame_activated(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_activated(InternalFrameEvent internalFrameEvent) {
		MainFrame.get_instance().on_animatorInternalChartFrame_activated();
	}

	/* (non-Javadoc)
	 * @see soars.plugin.modelbuilder.chart.chart.main.InternalChartFrame#on_internal_frame_deactivated(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_deactivated(InternalFrameEvent internalFrameEvent) {
		MainFrame.get_instance().on_animatorInternalChartFrame_deactivated();
	}
}
