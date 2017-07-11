/*
 * 2004/12/17
 */
package soars.common.utility.swing.window;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 * @author kurata
 */
public class MDIChildFrame extends JInternalFrame {

	/**
	 * 
	 */
	public MDIChildFrame() {
		super();
	}

	/**
	 * @param arg0
	 */
	public MDIChildFrame(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public MDIChildFrame(String arg0, boolean arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public MDIChildFrame(String arg0, boolean arg1, boolean arg2) {
		super(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public MDIChildFrame(
		String arg0,
		boolean arg1,
		boolean arg2,
		boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 */
	public MDIChildFrame(
		String arg0,
		boolean arg1,
		boolean arg2,
		boolean arg3,
		boolean arg4) {
		super(arg0, arg1, arg2, arg3, arg4);
	}

	/**
	 * @return
	 */
	public boolean create() {
		addInternalFrameListener( new InternalFrameListener() {
			public void internalFrameActivated(InternalFrameEvent arg0) {
				on_internal_frame_activated( arg0);
			}
			public void internalFrameClosed(InternalFrameEvent arg0) {
				on_internal_frame_closed( arg0);
			}
			public void internalFrameClosing(InternalFrameEvent arg0) {
				on_internal_frame_closing( arg0);
			}
			public void internalFrameDeactivated(InternalFrameEvent arg0) {
				on_internal_frame_deactivated( arg0);
			}
			public void internalFrameDeiconified(InternalFrameEvent arg0) {
				on_internal_frame_deiconified( arg0);
			}
			public void internalFrameIconified(InternalFrameEvent arg0) {
				on_internal_frame_iconified( arg0);
			}
			public void internalFrameOpened(InternalFrameEvent arg0) {
				on_internal_frame_opened( arg0);
			}
		});

		if ( !on_create())
			return false;

		return true;
	}

	/**
	 * @return
	 */
	protected boolean on_create() {
		return true;
	}

	/**
	 * @param internalFrameEvent
	 */
	protected void on_internal_frame_activated(InternalFrameEvent internalFrameEvent) {
	}

	/**
	 * @param internalFrameEvent
	 */
	protected void on_internal_frame_closed(InternalFrameEvent internalFrameEvent) {
	}

	/**
	 * @param internalFrameEvent
	 */
	protected void on_internal_frame_closing(InternalFrameEvent internalFrameEvent) {
	}

	/**
	 * @param internalFrameEvent
	 */
	protected void on_internal_frame_deactivated(InternalFrameEvent internalFrameEvent) {
	}

	/**
	 * @param internalFrameEvent
	 */
	protected void on_internal_frame_deiconified(InternalFrameEvent internalFrameEvent) {
	}

	/**
	 * @param internalFrameEvent
	 */
	protected void on_internal_frame_iconified(InternalFrameEvent internalFrameEvent) {
	}

	/**
	 * @param internalFrameEvent
	 */
	protected void on_internal_frame_opened(InternalFrameEvent internalFrameEvent) {
	}
}
