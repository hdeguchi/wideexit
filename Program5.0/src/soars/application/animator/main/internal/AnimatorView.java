/*
 * 2005/01/28
 */
package soars.application.animator.main.internal;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import soars.application.animator.file.exporter.GraphicDataSaxWriter;
import soars.application.animator.file.loader.FileProperty;
import soars.application.animator.file.writer.SaxWriter;
import soars.application.animator.main.Administrator;
import soars.application.animator.main.MainFrame;
import soars.application.animator.main.ResourceManager;
import soars.application.animator.state.AnimationState;
import soars.application.animator.state.EditState;
import soars.application.animator.state.StateManager;
import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.swing.window.View;

/**
 * The Animator view class.
 * @author kurata / SOARS project
 */
public class AnimatorView extends View implements IMessageCallback {

	/**
	 * 
	 */
	private AnimatorViewFrame _animatorViewFrame = null;

	/**
	 * 
	 */
	private ObjectManager _objectManager = null;

	/**
	 * 
	 */
	private StateManager _stateManager = null;

	/**
	 * Background color of this view.
	 */
	private final Color _backgroundColor = new Color( 255, 255, 255);

	/**
	 * Creates the instance of the Animator view class.
	 * @param arg0 a boolean, true for double-buffering, which uses additional memory space to achieve fast, flicker-free updates
	 * @param animatorViewFrame 
	 * @param mainFrame the instance of the main window.
	 */
	public AnimatorView(boolean arg0, AnimatorViewFrame animatorViewFrame) {
		super(arg0);
		_animatorViewFrame = animatorViewFrame;
		setBackground( _backgroundColor);
	}

	/**
	 * Returns true if the current state is "edit".
	 * @return true if the current state is "edit"
	 */
	public boolean is_state_edit() {
		return _stateManager.is_current_state( ResourceManager.get_instance().get( "state.edit"));
	}

	/**
	 * Returns true if the current state is "animation".
	 * @return true if the current state is "animation"
	 */
	public boolean is_state_animation() {
		return _stateManager.is_current_state( ResourceManager.get_instance().get( "state.animation"));
	}

	/**
	 * Cancels all selections of objects.
	 */
	public void cancel() {
		_stateManager.cancel();
	}

	/**
	 * 
	 */
	public void on_internal_frame_closing() {
		MainFrame.get_instance().remove_AnimatorViewFrame( _objectManager);
	}

	/**
	 * @return
	 */
	public boolean is_closable() {
		return Administrator.get_instance().can_remove( _objectManager);
	}

	/**
	 * Returns true if this component is initialized successfully.
	 * @return true if this component is initialized successfully
	 */
	private boolean setup() {
		_objectManager = ( null == _animatorViewFrame._srcObjectManager)
			? Administrator.get_instance().create_ObjectManager( this, _animatorViewFrame)		// For Loading, Importing
			: Administrator.get_instance().duplicate_ObjectManager( _animatorViewFrame._srcObjectManager, _animatorViewFrame, this);		// For Duplication

		if ( null == _objectManager)
			return false;

		_stateManager = new StateManager();

		if ( !setup_state_manager())
			return false;

		setup_key_event();

		return true;
	}

	/**
	 * 
	 */
	private void setup_key_event() {
		Action selectAllAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				_stateManager.on_key_pressed( "selectAll");
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "selectAll");
		getActionMap().put( "selectAll", selectAllAction);

		Action escapeAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				_stateManager.on_key_pressed( "escape");
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0, true), "escape");
		getActionMap().put( "escape", escapeAction);

		Action leftAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				_stateManager.on_key_pressed( "left");
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_LEFT, 0), "left");
		getActionMap().put( "left", leftAction);

		Action upAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				_stateManager.on_key_pressed( "up");
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_UP, 0), "up");
		getActionMap().put( "up", upAction);

		Action rightAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				_stateManager.on_key_pressed( "right");
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_RIGHT, 0), "right");
		getActionMap().put( "right", rightAction);

		Action downAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				_stateManager.on_key_pressed( "down");
			}
		};
		getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_DOWN, 0), "down");
		getActionMap().put( "down", downAction);

	}

	/**
	 * Returns true if the "State pattern" manager is initialized successfully.
	 * @return true if the "State pattern" manager is initialized successfully
	 */
	private boolean setup_state_manager() {
		if ( !_stateManager.setup( ""))
			return false;

		EditState editState = new EditState( _objectManager);
		if ( !editState.setup())
			return false;

		_stateManager.put( ResourceManager.get_instance().get( "state.edit"), editState);

		AnimationState animationState = new AnimationState( _objectManager);
		if ( !animationState.setup())
			return false;

		_stateManager.put( ResourceManager.get_instance().get( "state.animation"), animationState);

		return true;
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
		_stateManager.cleanup();
		_objectManager.cleanup();
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		if ( !setup())
			return false;

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_component_resized(java.awt.event.ComponentEvent)
	 */
	protected void on_component_resized(ComponentEvent componentEvent) {
		_stateManager.on_resized( componentEvent);
		super.on_component_resized(componentEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_mouse_left_double_click(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_double_click(MouseEvent mouseEvent) {
		_stateManager.on_mouse_left_double_click(mouseEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_mouse_left_down(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_down(MouseEvent mouseEvent) {
		_stateManager.on_mouse_left_down(mouseEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_mouse_left_up(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_up(MouseEvent mouseEvent) {
		_stateManager.on_mouse_left_up(mouseEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_mouse_dragged(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_dragged(MouseEvent mouseEvent) {
		_stateManager.on_mouse_dragged(mouseEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_mouse_right_down(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_right_down(MouseEvent mouseEvent) {
		_stateManager.on_mouse_right_down(mouseEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_mouse_right_up(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
		_stateManager.on_mouse_right_up(mouseEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_mouse_moved(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_moved(MouseEvent mouseEvent) {
		_stateManager.on_mouse_moved(mouseEvent);
	}

	/* (Non Javadoc)
	 * @see javax.swing.JComponent#getToolTipText(java.awt.event.MouseEvent)
	 */
	public String getToolTipText(MouseEvent arg0) {
		return _stateManager.get_tooltip_text( arg0);
	}

	/* (Non Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics arg0) {
		super.paintComponent( arg0);
		_stateManager.paint( arg0);
	}

	/**
	 * @param id 
	 * @param file
	 * @param fileProperties
	 * @return
	 */
	public boolean load(int id, File file, Vector<FileProperty> fileProperties) {
		_objectManager._id = id;

		Graphics2D graphics2D = ( Graphics2D)getGraphics();

		if ( !MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"load", ResourceManager.get_instance().get( "file.open.show.message"), new Object[] { file, fileProperties, graphics2D}, this, MainFrame.get_instance()))
			return false;

		//_stateManager.reset();
		_stateManager.change( ResourceManager.get_instance().get( "state.edit"));

		set_title();

		return true;
	}

	/**
	 * Close.
	 */
	public void close() {
		cleanup();
	}

	/**
	 * Returns true if the current data are stored to the specified file successfully.
	 * @param windowProperty
	 * @param file the specified file
	 * @return true if the current data are stored to the specified file successfully
	 */
	public boolean save_as(WindowProperty windowProperty, File file) {
		return MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"save_as", ResourceManager.get_instance().get( "file.saveas.show.message"),
			new Object[] { windowProperty, file}, this, MainFrame.get_instance());
	}

	/**
	 * @param id
	 * @param spotLog
	 * @return
	 */
	public boolean import_from_directory(int id, String[] spotLog) {
		_objectManager._id = id;
		_objectManager._spotLog = spotLog;

		Graphics2D graphics2D = ( Graphics2D)getGraphics();

		if ( !MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"file_import", ResourceManager.get_instance().get( "file.import.show.message"), new Object[] { graphics2D}, this, MainFrame.get_instance()))
			return false;

		//_stateManager.reset();
		_stateManager.change( ResourceManager.get_instance().get( "state.edit"));

		set_title();

		return true;
	}

	/**
	 * @return
	 */
	public boolean adjust_for_duplication() {
		_stateManager.change( ResourceManager.get_instance().get( "state.edit"));
		set_title();
		return true;
	}

	/**
	 * Returns true if the graphics data are loaded from the specified graphics data file successfully.
	 * @param file the specified file
	 * @param rootDirectory
	 * @return true if the graphics data are loaded from the specified graphics data file successfully
	 */
	public boolean import_graphic_data(File file, File rootDirectory) {
		Graphics2D graphics2D = ( Graphics2D)getGraphics();

		if ( !MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"import_graphic_data", ResourceManager.get_instance().get( "file.import.graphic.data.show.message"),
			new Object[] { file, rootDirectory, graphics2D}, this, MainFrame.get_instance()))
			return false;

		//_stateManager.reset();
		_stateManager.change( ResourceManager.get_instance().get( "state.edit"));

		return true;
	}

	/**
	 * Returns true if the graphics data are stored to the specified graphics data file successfully.
	 * @param file the specified file
	 * @param rootDirectory
	 * @return true if the graphics data are stored to the specified graphics data file successfully
	 */
	public boolean export_graphic_data(File file, File rootDirectory) {
		return MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"export_graphic_data", ResourceManager.get_instance().get( "file.export.graphic.data.show.message"),
			new Object[] { file, rootDirectory}, this, MainFrame.get_instance());
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IMessageCallback#message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.MessageDlg)
	 */
	public boolean message_callback(String id, Object[] objects, MessageDlg messageDlg) {
		if ( id.equals( "load"))
			return _objectManager.load( ( File)objects[ 0], ( Vector<FileProperty>)objects[ 1], ( Graphics2D)objects[ 2]);
		else if ( id.equals( "save_as"))
			return SaxWriter.execute( ( WindowProperty)objects[ 0], ( File)objects[ 1], _objectManager);
		else if ( id.equals( "file_import"))
			return _objectManager.import_data( ( Graphics2D)objects[ 0]);
		else if ( id.endsWith( "import_graphic_data"))
			return _objectManager.import_graphic_data( ( File)objects[ 0], ( File)objects[ 1], ( Graphics2D)objects[ 2]);
		else if ( id.equals( "export_graphic_data"))
			return GraphicDataSaxWriter.execute( ( File)objects[ 0], ( File)objects[ 1], _objectManager);

		return true;
	}

	/**
	 * Edits Animator common properties.
	 */
	public void on_edit_common_property() {
		_objectManager.on_edit_common_property( this);
		repaint();
	}

	/**
	 * Edits the data of the specified agent.
	 */
	public void on_edit_agent() {
		_stateManager.cancel();
		if ( !_objectManager.edit_agent( this, MainFrame.get_instance()))
			return;

		repaint();
	}

	/**
	 * Edits the data of the specified spot.
	 */
	public void on_edit_spot() {
		_stateManager.cancel();
		if ( !_objectManager.edit_spot( this, MainFrame.get_instance()))
			return;

		repaint();
	}

	/**
	 * Edits the properties of the specified agent.
	 */
	public void on_edit_agent_property() {
		_stateManager.cancel();
		if ( !_objectManager.edit_agent_property( this, MainFrame.get_instance()))
			return;

		repaint();
	}

	/**
	 * Edits the properties of the specified spot.
	 */
	public void on_edit_spot_property() {
		_stateManager.cancel();
		if ( !_objectManager.edit_spot_property( this, MainFrame.get_instance()))
			return;

		repaint();
	}

//	/**
//	 * Invoked when the "About SOARS Animator" menu is selected.
//	 */
//	public void on_about() {
//		_stateManager.cancel();
//	}

	/**
	 * Goes backward head.
	 */
	public void on_backward_head() {
		_stateManager.change( ResourceManager.get_instance().get( "state.animation"));
		_stateManager.on_backward_head();
		set_title();
	}

	/**
	 * Goes backward.
	 */
	public void on_backward() {
		_stateManager.change( ResourceManager.get_instance().get( "state.animation"));
		_stateManager.on_backward();
		set_title();
	}

	/**
	 * Goes backward step.
	 */
	public void on_backward_step() {
		_stateManager.change( ResourceManager.get_instance().get( "state.animation"));
		_stateManager.on_backward_step();
		set_title();
	}

	/**
	 * Starts or Restarts the animation.
	 * @param bStart
	 */
	public void on_play(boolean bStart) {
		_stateManager.change( ResourceManager.get_instance().get( "state.animation"));
		_stateManager.on_play( bStart);
	}

	/**
	 * Pauses the animation.
	 */
	public void on_pause() {
		_stateManager.on_pause();
	}

	/**
	 * Stop the animation.
	 */
	public void on_stop() {
		_stateManager.change( ResourceManager.get_instance().get( "state.edit"));
		_objectManager._scenarioManager.dispose_retrieve_property_dialog();
		_objectManager._scenarioManager.dispose_animation_slider_dialog();
		set_title();
		repaint();
	}

	/**
	 * Goes forward step.
	 */
	public void on_forward_step() {
		_stateManager.change( ResourceManager.get_instance().get( "state.animation"));
		_stateManager.on_forward_step();
		set_title();
	}

	/**
	 * Goes forward.
	 */
	public void on_forward() {
		_stateManager.change( ResourceManager.get_instance().get( "state.animation"));
		_stateManager.on_forward();
		set_title();
	}

	/**
	 * Goes forward tail.
	 */
	public void on_forward_tail() {
		_stateManager.change( ResourceManager.get_instance().get( "state.animation"));
		_stateManager.on_forward_tail();
		set_title();
	}

	/**
	 * Returns true if the specified agent property is found.
	 * @return true if the specified agent property is found
	 */
	public boolean on_retrieve_agent_property() {
		_stateManager.change( ResourceManager.get_instance().get( "state.animation"));
		_stateManager.on_pause();

		if ( !_objectManager.retrieve_agent_property( MainFrame.get_instance()))
			return false;

		return true;
	}

	/**
	 * Returns true if the specified spot property is found.
	 * @return true if the specified spot property is found
	 */
	public boolean on_retrieve_spot_property() {
		_stateManager.change( ResourceManager.get_instance().get( "state.animation"));
		_stateManager.on_pause();

		if ( !_objectManager.retrieve_spot_property( MainFrame.get_instance()))
			return false;

		return true;
	}

	/**
	 * Shows the animation slider.
	 */
	public void on_animation_slider() {
		_stateManager.change( ResourceManager.get_instance().get( "state.animation"));
		_objectManager._scenarioManager.show_animation_slider( _objectManager.get_title_prefix(), MainFrame.get_instance(), _animatorViewFrame);
		_stateManager.on_pause();
	}

	/**
	 * 
	 */
	public void on_main_animation_slider() {
		_stateManager.change( ResourceManager.get_instance().get( "state.animation"));
		_stateManager.on_pause();
	}

	/**
	 * @param delta
	 * @param slider
	 */
	public void update_current_position(int delta, boolean slider) {
		_stateManager.change( ResourceManager.get_instance().get( "state.animation"));
		_stateManager.update_current_position( delta, slider);
		set_title();
	}

	/**
	 * @return
	 */
	public boolean exist_agent_property() {
		return _objectManager._scenarioManager._agentPropertyManager.exist_property();
	}

	/**
	 * @return
	 */
	public boolean exist_selected_agent_property() {
		return _objectManager._scenarioManager._agentPropertyManager.exist_selected_property();
	}

	/**
	 * @return
	 */
	public boolean exist_spot_property() {
		return _objectManager._scenarioManager._spotPropertyManager.exist_property();
	}

	/**
	 * @return
	 */
	public boolean exist_selected_spot_property() {
		return _objectManager._scenarioManager._spotPropertyManager.exist_selected_property();
	}

	/**
	 * 
	 */
	public void on_start_timer() {
		_stateManager.on_start_timer();
	}

	/**
	 * 
	 */
	public void on_stop_timer() {
		_stateManager.on_stop_timer();
	}

	/**
	 * 
	 */
	public void load_on_timer_task() {
		_stateManager.load_on_timer_task();
	}

	/**
	 * 
	 */
	public void animate_on_timer_task() {
		_stateManager.animate_on_timer_task();
	}

	/**
	 * 
	 */
	public void set_title() {
		 _animatorViewFrame.setTitle( _objectManager.get_title_prefix() + _objectManager._scenarioManager.get_information());
	}
}
