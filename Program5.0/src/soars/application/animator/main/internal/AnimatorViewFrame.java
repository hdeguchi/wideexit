/*
 * 2005/01/27
 */
package soars.application.animator.main.internal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.event.InternalFrameEvent;

import soars.application.animator.file.loader.FileProperty;
import soars.application.animator.main.Constant;
import soars.application.animator.main.MainFrame;
import soars.application.animator.observer.Observer;
import soars.common.utility.swing.window.MDIChildFrame;
import soars.common.utility.tool.resource.Resource;

/**
 * The Animator main window class.
 * @author kurata / SOARS project
 */
public class AnimatorViewFrame extends MDIChildFrame {

	/**
	 * Default width.
	 */
	static public final int _minimumWidth = 640;

	/**
	 * Default height.
	 */
	static public final int _minimumHeight = 480;

	/**
	 * 
	 */
	private AnimatorView _animatorView = null;

	/**
	 * 
	 */
	private JLabel _messageLabel = null;

	/**
	 * 
	 */
	private JLabel _informationLabel = null;

	/**
	 * 
	 */
	public ObjectManager _srcObjectManager = null;

	/**
	 * 
	 */
	public int _order = -1;

	/**
	 * @param arg0
	 */
	public AnimatorViewFrame(String arg0) {
		super(arg0, true, true, true, true);
	}

	/** For duplication
	 * @param arg0
	 * @param srcObjectManager
	 */
	public AnimatorViewFrame(String arg0, ObjectManager srcObjectManager) {
		super(arg0, true, true, true, true);
		_srcObjectManager = srcObjectManager;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		_animatorView = new AnimatorView( true, this);
		if ( !_animatorView.create())
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportView( _animatorView);
		scrollPane.setBackground( new Color( 255, 255, 255));
		getContentPane().setLayout( new BorderLayout());
		getContentPane().add( scrollPane);

		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

		setSize( _minimumWidth, _minimumHeight);

		Toolkit.getDefaultToolkit().setDynamicLayout( true);

		Image image = Resource.load_image_from_resource( Constant._resourceDirectory + "/image/icon/icon.png", getClass());
		if ( null != image)
			setFrameIcon( new ImageIcon( image));

		getRootPane().setDoubleBuffered( true);
		( ( JComponent)getContentPane()).setDoubleBuffered( true);

		setVisible( true);

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
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_internal_frame_closing(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_closing(InternalFrameEvent internalFrameEvent) {
		_animatorView.on_internal_frame_closing();
	}

	/**
	 * 
	 */
	public void set_closable() {
		setClosable( _animatorView.is_closable());
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
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_internal_frame_activated(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_activated(InternalFrameEvent internalFrameEvent) {
		MainFrame.get_instance().on_animatorViewFrame_activated();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_internal_frame_deactivated(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_deactivated(InternalFrameEvent internalFrameEvent) {
		MainFrame.get_instance().on_animatorViewFrame_deactivated();
	}

	/**
	 * 
	 */
	public void cancel() {
		_animatorView.cancel();
	}

	/**
	 * @param id
	 * @param file
	 * @param fileProperties
	 * @return
	 */
	public boolean load(int id, File file, Vector<FileProperty> fileProperties) {
		return _animatorView.load( id, file, fileProperties);
	}

	/**
	 * @param id
	 * @param spotLog
	 * @return
	 */
	public boolean import_from_directory(int id, String[] spotLog) {
		return _animatorView.import_from_directory( id, spotLog);
	}

	/**
	 * @return
	 */
	public boolean adjust_for_duplication() {
		return _animatorView.adjust_for_duplication();
	}

	/**
	 * 
	 */
	public void resetScrollRectToVisible() {
		_animatorView.scrollRectToVisible( new Rectangle( 0, 0, 1, 1));
	}

	/**
	 * 
	 */
	public void close() {
		_animatorView.close();
	}

	/**
	 * 
	 */
	public void on_edit_common_property() {
		_animatorView.on_edit_common_property();
	}

	/**
	 * 
	 */
	public void on_edit_agent() {
		_animatorView.on_edit_agent();
	}

	/**
	 * 
	 */
	public void on_edit_spot() {
		_animatorView.on_edit_spot();
	}

	/**
	 * 
	 */
	public void on_edit_agent_property() {
		_animatorView.on_edit_agent_property();
	}

	/**
	 * 
	 */
	public void on_edit_spot_property() {
		_animatorView.on_edit_spot_property();
	}

	/**
	 * 
	 */
	public void on_backward_head() {
		_animatorView.on_backward_head();
	}

	/**
	 * 
	 */
	public void on_backward() {
		_animatorView.on_backward();
	}

	/**
	 * 
	 */
	public void on_backward_step() {
		_animatorView.on_backward_step();
	}

	/**
	 * @param bStart
	 */
	public void on_play(boolean bStart) {
		_animatorView.on_play( bStart);
	}

	/**
	 * 
	 */
	public void on_pause() {
		_animatorView.on_pause();
	}

	/**
	 * 
	 */
	public void on_stop() {
		_animatorView.on_stop();
	}

	/**
	 * 
	 */
	public void on_forward_step() {
		_animatorView.on_forward_step();
	}

	/**
	 * 
	 */
	public void on_forward() {
		_animatorView.on_forward();
	}

	/**
	 * 
	 */
	public void on_forward_tail() {
		_animatorView.on_forward_tail();
	}

	/**
	 * @return
	 */
	public boolean on_retrieve_agent_property() {
		return _animatorView.on_retrieve_agent_property();
	}

	/**
	 * @return
	 */
	public boolean on_retrieve_spot_property() {
		return _animatorView.on_retrieve_spot_property();
	}

	/**
	 * 
	 */
	public void on_animation_slider() {
		_animatorView.on_animation_slider();
	}

	/**
	 * 
	 */
	public void on_main_animation_slider() {
		_animatorView.on_main_animation_slider();
	}

	/**
	 * @return
	 */
	public boolean is_state_animation() {
		return _animatorView.is_state_animation();
	}

	/**
	 * @param delta
	 * @param slider
	 */
	public void update_current_position(int delta, boolean slider) {
		_animatorView.update_current_position( delta, slider);
	}

	/**
	 * @return
	 */
	public boolean exist_agent_property() {
		return _animatorView.exist_agent_property();
	}

	/**
	 * @return
	 */
	public boolean exist_selected_agent_property() {
		return _animatorView.exist_selected_agent_property();
	}

	/**
	 * @return
	 */
	public boolean exist_spot_property() {
		return _animatorView.exist_spot_property();
	}

	/**
	 * @return
	 */
	public boolean exist_selected_spot_property() {
		return _animatorView.exist_selected_spot_property();
	}
}
