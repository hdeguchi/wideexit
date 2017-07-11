/*
 * 2005/02/08
 */
package soars.application.animator.object.scenario;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.Vector;

import org.xml.sax.SAXException;

import soars.application.animator.common.tool.CommonTool;
import soars.application.animator.main.Administrator;
import soars.application.animator.main.Constant;
import soars.application.animator.main.Environment;
import soars.application.animator.main.MainFrame;
import soars.application.animator.main.ResourceManager;
import soars.application.animator.main.internal.AnimatorView;
import soars.application.animator.main.internal.ObjectManager;
import soars.application.animator.object.entity.agent.AgentObject;
import soars.application.animator.object.entity.base.EntityBase;
import soars.application.animator.object.entity.spot.ISpotObjectManipulator;
import soars.application.animator.object.file.HeaderObject;
import soars.application.animator.object.property.agent.AgentPropertyManager;
import soars.application.animator.object.property.spot.SpotPropertyManager;
import soars.application.animator.object.scenario.retrieve.RetrievePropertyDlg;
import soars.application.animator.object.scenario.slider.AnimationSliderDlg;
import soars.application.animator.object.transition.agent.AgentTransitionManager;
import soars.application.animator.object.transition.base.TransitionManager;
import soars.application.animator.object.transition.spot.SpotTransitionManager;
import soars.application.animator.setting.common.CommonProperty;
import soars.common.utility.swing.message.IIntMessageCallback;
import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.IntMessageDlg;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.xml.sax.Writer;

/**
 * The animation scenario class.
 * @author kurata / SOARS project
 */
public class ScenarioManager implements IScenarioManipulator, IMessageCallback, IIntMessageCallback {

	/**
	 * 
	 */
	private Object _lock = new Object();

	/**
	 * Instance of the Animator view class.
	 */
	public AnimatorView _animatorView = null;

	/**
	 * Informations of SOARS log files.
	 */
	public HeaderObject _headerObject = null;

	/**
	 * 
	 */
	private Vector<String> _times = new Vector<String>();

	/**
	 * Instance of the AgentTransitionManager class.
	 */
	public AgentTransitionManager _agentTransitionManager = null;

	/**
	 * Instance of the SpotTransitionManager class.
	 */
	public SpotTransitionManager _spotTransitionManager = null;

	/**
	 * Instance of the AgentPropertyManager class.
	 */
	public AgentPropertyManager _agentPropertyManager = null;

	/**
	 * Instance of the SpotPropertyManager class.
	 */
	public SpotPropertyManager _spotPropertyManager = null;

	/**
	 * 
	 */
	private RetrievePropertyDlg _retrieveAgentPropertyDlg = null;

	/**
	 * 
	 */
	private RetrievePropertyDlg _retrieveSpotPropertyDlg = null;

	/**
	 * 
	 */
	private AnimationSliderDlg _animationSliderDlg = null;

	/**
	 * 
	 */
	private int _counter = 0;

	/**
	 * 
	 */
	private int _index = 0;

	/**
	 * 
	 */
	private int _divide = 10;

	/**
	 * Creates the instance of the animation scenario class.
	 * @param animatorView 
	 */
	public ScenarioManager(AnimatorView animatorView) {
		super();
		_animatorView = animatorView;
		_spotTransitionManager = new SpotTransitionManager();
		_agentTransitionManager = new AgentTransitionManager( _spotTransitionManager);
		_agentPropertyManager = new AgentPropertyManager();
		_spotPropertyManager = new SpotPropertyManager();
	}

	/** Copy constructor(For duplication)
	 * @param scenarioManager
	 * @param animatorView
	 * @param objectManager
	 */
	public ScenarioManager(ScenarioManager scenarioManager, AnimatorView animatorView, ObjectManager objectManager) {
		super();
		_animatorView = animatorView;
		for ( String time:scenarioManager._times)
			_times.add( time);
		_headerObject = new HeaderObject( scenarioManager._headerObject, objectManager);
		_spotTransitionManager = new SpotTransitionManager();
		_agentTransitionManager = new AgentTransitionManager( _spotTransitionManager);
		_agentPropertyManager = new AgentPropertyManager( scenarioManager._agentPropertyManager);
		_spotPropertyManager = new SpotPropertyManager( scenarioManager._spotPropertyManager);
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.scenario.IScenarioManipulator#get_size()
	 */
	public int get_size() {
		synchronized( _lock) {
			return _headerObject._size;
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.scenario.IScenarioManipulator#get_current_position()
	 */
	public int get_current_position() {
		synchronized( _lock) {
			return get_current_position_internal();
		}
	}

	/**
	 * @return
	 */
	private int get_current_position_internal() {
		return ( _counter - _times.size());
	}

	/**
	 * Sets the current position of the animation with the specified position.
	 * @param position the specified position
	 */
	public void set_current_position(int position) {
		synchronized( _lock) {
			set_current_position_internal( position);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.scenario.IScenarioManipulator#on_animationSlider_state_changed(int)
	 */
	public void on_animationSlider_state_changed(int position) {
		set_current_position( position/*, false*/);
		_animatorView.set_title();
		_animatorView.getParent().repaint();
	}

	/**
	 * Returns true if the current position is the head of the animation.
	 * @return true if the current position is the head of the animation
	 */
	public boolean is_head() {
		synchronized( _lock) {
			return ( 0 == get_current_position_internal());
		}
	}

	/**
	 * Returns true if the current position is the tail of the animation.
	 * @return true if the current position is the tail of the animation
	 */
	public boolean is_tail() {
		synchronized( _lock) {
			return ( ( _headerObject._size == _counter) && ( 1 == _times.size()));
		}
	}

	/**
	 * @return
	 */
	private boolean is_waiting() {
		synchronized( _lock) {
			return ( 2 > _times.size());
		}
	}

	/**
	 * Returns true for loading the scenario from a line of each log file successfully.
	 * @param headerObject the informations of SOARS log files
	 * @return true for loading the scenario from a line of each log file successfully
	 */
	public boolean load(HeaderObject headerObject) {
		_headerObject = headerObject;
		return _headerObject.load( ( Graphics2D)_animatorView.getGraphics());
	}

	/**
	 * Returns true for loading the scenario from a line of each log file successfully.
	 * @return true for loading the scenario from a line of each log file successfully
	 */
	public boolean load() {
		return _headerObject.load( ( Graphics2D)_animatorView.getGraphics());
	}

	/**
	 * Returns true for reading the scenario from the lines of each log file successfully.
	 * @return true for reading the scenario from the lines of each log file successfully
	 */
	public boolean read() {
		if ( _headerObject._size == _counter)
			return true;

		if ( Constant._bufferingSize <= _times.size())
			return true;

		return read( Math.min( Constant._bufferingSize - _times.size(), _headerObject._size - _counter));
	}

	/**
	 * @param size
	 * @return
	 */
	private boolean read(int size) {
		Vector<String> times = new Vector<String>();
		AgentTransitionManager agentTransitionManager = new AgentTransitionManager( _spotTransitionManager);
		SpotTransitionManager spotTransitionManager = new SpotTransitionManager();

		if ( !_headerObject.read( _counter, size, times, agentTransitionManager, spotTransitionManager, ( Graphics2D)_animatorView.getGraphics()))
			return false;

		synchronized( _lock) {
			_times.addAll( times);
			_agentTransitionManager.addAll( agentTransitionManager);
			_spotTransitionManager.addAll( spotTransitionManager);

			_counter += size;
		}

		return true;
	}

	/**
	 * @param position
	 */
	private void set_current_position_internal(int position) {
		MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"set_current_position_internal", ResourceManager.get_instance().get( "file.open.show.message"),
			new Object[] { new Integer( position)}, this, MainFrame.get_instance());
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IMessageCallback#message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.MessageDlg)
	 */
	public boolean message_callback(String id, Object[] objects, MessageDlg messageDlg) {
		if ( id.equals( "set_current_position_internal"))
			on_set_current_position_internal( ( ( Integer)objects[ 0]).intValue());

		return true;
	}

	/**
	 * Rewind.
	 */
	public void rewind() {
		synchronized( _lock) {
			on_set_current_position_internal( 0);
		}
	}

	/**
	 * @param position
	 */
	private void on_set_current_position_internal(int position) {
		// 現在読み込んでいるものを再利用する
		if ( 0 > position)
			position = 0;

		if ( _headerObject._size <= position)
			position = _headerObject._size - 1;

		int currentPosition = get_current_position_internal();
		if ( currentPosition == position)
			return;

		Vector<String> times = new Vector<String>();
		AgentTransitionManager agentTransitionManager = new AgentTransitionManager( _spotTransitionManager);
		SpotTransitionManager spotTransitionManager = new SpotTransitionManager();

//		int type = -1;

		int size = Math.abs( position - currentPosition);
		if ( _times.size() > size) {
			if ( position < currentPosition) {
//				type = 1;
				if ( !_headerObject.read( position, size, times, agentTransitionManager, spotTransitionManager, ( Graphics2D)_animatorView.getGraphics()))
					return;

				_times.addAll( 0, times);
				_agentTransitionManager.addAll( 0, agentTransitionManager);
				_spotTransitionManager.addAll( 0, spotTransitionManager);

				while ( Constant._bufferingSize < _times.size()) {
					_times.removeElementAt( _times.size() - 1);
					_agentTransitionManager.removeElementAt( _agentTransitionManager.size() - 1);
					_spotTransitionManager.removeElementAt( _spotTransitionManager.size() - 1);
				}
			} else {
//				type = 2;
				if ( _headerObject._size > _counter) {
					size = Math.min( size, _headerObject._size - _counter);
					if ( !_headerObject.read( _counter, size, times, agentTransitionManager, spotTransitionManager, ( Graphics2D)_animatorView.getGraphics()))
						return;

					_times.addAll( times);
					_agentTransitionManager.addAll( agentTransitionManager);
					_spotTransitionManager.addAll( spotTransitionManager);
				}

				for ( int i = 0; i < size; ++i) {
					_times.removeElementAt( 0);
					_agentTransitionManager.removeElementAt( 0);
					_spotTransitionManager.removeElementAt( 0);
				}
			}
		} else {
//			type = 3;
			size = Math.min( Constant._bufferingSize, _headerObject._size - position);

			if ( !_headerObject.read( position, size, times, agentTransitionManager, spotTransitionManager, ( Graphics2D)_animatorView.getGraphics()))
				return;

			_times.clear();
			_agentTransitionManager.cleanup();
			_spotTransitionManager.cleanup();

			_times.addAll( times);
			_agentTransitionManager.addAll( agentTransitionManager);
			_spotTransitionManager.addAll( spotTransitionManager);
		}

		_counter = ( position + _times.size());

		reset();

//		System.out.println( type + ", " + position + ", " + get_current_position() + ", " + _times.size() + ", " + _counter);
	}

	/**
	 * Resets all.
	 */
	public void reset() {
		_index = 0;
		Administrator.get_instance()._tick = 0;
//		_tick = 0;
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
		dispose_retrieve_property_dialog();

		dispose_animation_slider_dialog();

		_headerObject = null;

		_times.clear();

		_agentTransitionManager.cleanup();
		_spotTransitionManager.cleanup();

		_agentPropertyManager.cleanup();
		_spotPropertyManager.cleanup();

		_counter = 0;

		reset();
	}

	/**
	 * Disposes the instances of RetrievePropertyDlg.
	 */
	public void dispose_retrieve_property_dialog() {
		if ( null != _retrieveAgentPropertyDlg) {
			Point point = _retrieveAgentPropertyDlg.getLocation();
			Environment.get_instance().set(
				Environment._retrieveAgentPropertyDialogLocationKey + "x", String.valueOf( point.x));
			Environment.get_instance().set(
				Environment._retrieveAgentPropertyDialogLocationKey + "y", String.valueOf( point.y));
			_retrieveAgentPropertyDlg.dispose();
			_retrieveAgentPropertyDlg = null;
		}

		if ( null != _retrieveSpotPropertyDlg) {
			Point point = _retrieveSpotPropertyDlg.getLocation();
			Environment.get_instance().set(
				Environment._retrieveSpotPropertyDialogLocationKey + "x", String.valueOf( point.x));
			Environment.get_instance().set(
				Environment._retrieveSpotPropertyDialogLocationKey + "y", String.valueOf( point.y));
			_retrieveSpotPropertyDlg.dispose();
			_retrieveSpotPropertyDlg = null;
		}
	}

	/**
	 * Disposes the instance of AnimationSliderDlg.
	 */
	public void dispose_animation_slider_dialog() {
		if ( null != _animationSliderDlg) {
			Point point = _animationSliderDlg.getLocation();
			Environment.get_instance().set(
				Environment._animationSliderDialogLocationKey + "x", String.valueOf( point.x));
			Environment.get_instance().set(
				Environment._animationSliderDialogLocationKey + "y", String.valueOf( point.y));
			_animationSliderDlg.dispose();
			_animationSliderDlg = null;
		}
	}

	/**
	 * Sets whether or not the instances of RetrievePropertyDlg is enabled.
	 * @param enable true if the instances of RetrievePropertyDlg should be enabled, false otherwise
	 */
	public void enable_retrieve_property_dialog(boolean enable) {
		if ( null != _retrieveAgentPropertyDlg)
			_retrieveAgentPropertyDlg.enable_user_interface( enable);

		if ( null != _retrieveSpotPropertyDlg)
			_retrieveSpotPropertyDlg.enable_user_interface( enable);
	}

	/**
	 * Sets whether or not the instance of AnimationSliderDlg is enabled.
	 * @param enable true if the instance of AnimationSliderDlg should be enabled, false otherwise
	 */
	public void enable_animation_slider_dialog(boolean enable) {
		if ( null != _animationSliderDlg)
			_animationSliderDlg.enable_user_interface( enable);
	}

	/**
	 * Updates the instance of AnimationSliderDlg.
	 */
	public void update_animation_slider_dialog() {
		synchronized( _lock) {
			if ( null != _animationSliderDlg)
				_animationSliderDlg.update( get_current_position_internal());
		}
	}

	/**
	 * Returns the position of the specified agent.
	 * @param agentObject the specified agent
	 * @param pack whether the agents on the spot are packed
	 * @return the position of the specified agent
	 */
	public Point get_position(AgentObject agentObject, boolean pack) {
		return get_position( agentObject, _index, pack);
	}

	/**
	 * Returns the position of the specified agent.
	 * @param agentObject the specified agent
	 * @param index the position of the scenario
	 * @param pack whether the agents on the spot are packed
	 * @return the position of the specified agent
	 */
	public Point get_position(AgentObject agentObject, int index, boolean pack) {
		if ( 0 > index)
			return null;

		return _agentTransitionManager.get_position( agentObject, index, pack);
	}

	/**
	 * Returns the spot which contains the specified agent.
	 * @param agentObject the specified agent
	 * @return the spot which contains the specified agent
	 */
	public ISpotObjectManipulator get_spot(AgentObject agentObject) {
		return get_spot( agentObject, _index);
	}

	/**
	 * Returns the spot which contains the specified agent.
	 * @param agentObject the specified agent
	 * @param index the position of the scenario
	 * @return the spot which contains the specified agent
	 */
	public ISpotObjectManipulator get_spot(AgentObject agentObject, int index) {
		return _agentTransitionManager.get_spot( agentObject, index);
	}

	/**
	 * Goes to the next position in the scenario.
	 */
	public void next() {
//		if ( _tick < CommonProperty.get_instance()._divide - 1)
//			++_tick;
//		else {
			if ( !is_tail() && !is_waiting()) {
				synchronized( _lock) {
					_times.removeElementAt( 0);
					_agentTransitionManager.removeElementAt( 0);
					_spotTransitionManager.removeElementAt( 0);
				}
			}

//			_tick = 0;
//		}
	}

	/**
	 * Returns the current position of the specified agent.
	 * @param agentObject the specified agent
	 * @return the current position of the specified agent
	 */
	public Point get_current_position(AgentObject agentObject) {
		// Pack agents
		if ( is_tail() || is_waiting())
			return get_position( agentObject, CommonProperty.get_instance()._pack);

		Point position_from = get_position( agentObject, CommonProperty.get_instance()._pack);
		if ( null == position_from)
			return null;

		Point position_to = get_position( agentObject, _index + 1, CommonProperty.get_instance()._pack);
		if ( null == position_to)
			return null;

		return new Point(
			( int)( ( ( CommonProperty.get_instance()._divide - Administrator.get_instance()._tick) * position_from.x + Administrator.get_instance()._tick * position_to.x) / CommonProperty.get_instance()._divide),
			( int)( ( ( CommonProperty.get_instance()._divide - Administrator.get_instance()._tick) * position_from.y + Administrator.get_instance()._tick * position_to.y) / CommonProperty.get_instance()._divide));
//			( int)( ( ( CommonProperty.get_instance()._divide - _tick) * position_from.x + _tick * position_to.x) / CommonProperty.get_instance()._divide),
//			( int)( ( ( CommonProperty.get_instance()._divide - _tick) * position_from.y + _tick * position_to.y) / CommonProperty.get_instance()._divide));
	}

	/**
	 * Returns true for drawing the image of the specified agent property successfully.
	 * @param agentObject the specified agent
	 * @param graphics2D the graphics object of JAVA
	 * @param position the position of the image
	 * @param width the default width of the image
	 * @param height the default height of the image
	 * @param rectangle the visible rectangle
	 * @param imageObserver an asynchronous update interface for receiving notifications about Image information as the Image is constructed
	 * @param image true if the object has the image
	 * @return true for drawing the image of the specified agent property successfully
	 */
	public boolean draw_current_agent_property_image(AgentObject agentObject, Graphics2D graphics2D, Point position, int width, int height, Rectangle rectangle, ImageObserver imageObserver, boolean image_exists) {
		synchronized( _lock) {
			return _agentPropertyManager.draw_current_property_image( graphics2D, position, width, height, _agentTransitionManager.get( agentObject, _index), rectangle, imageObserver, image_exists);
		}
	}

	/**
	 * Returns true for drawing the image of the specified spot property successfully.
	 * @param spotObjectManipulator the specified spot
	 * @param graphics2D the graphics object of JAVA
	 * @param position the position of the image
	 * @param deltaY the y-coordinates of the text
	 * @param width the default width of the image
	 * @param height the default height of the image
	 * @param rectangle the visible rectangle
	 * @param imageObserver an asynchronous update interface for receiving notifications about Image information as the Image is constructed
	 * @param image true if the object has the image
	 * @return true for drawing the image of the specified spot property successfully
	 */
	public boolean draw_current_spot_property_image(ISpotObjectManipulator spotObjectManipulator, Graphics2D graphics2D, Point position, int deltaY, int width, int height, Rectangle rectangle, ImageObserver imageObserver, boolean image_exists) {
		synchronized( _lock) {
			return _spotPropertyManager.draw_current_property_image( graphics2D, position, deltaY,
				width, height, _spotTransitionManager.get( spotObjectManipulator, _index), rectangle, imageObserver, image_exists);
		}
	}

	/**
	 * Goes backward.
	 */
	public void backward() {
		synchronized( _lock) {
			if ( _headerObject._size < _divide) {
				set_current_position_internal( 0);
				return;
			}

			for ( int i = _divide - 1; i >= 0; --i) {
				int position = ( int)Math.floor( i * _headerObject._size / _divide);
				if ( get_current_position_internal() > position) {
					set_current_position_internal( position);
					return;
				}
			}

			set_current_position_internal( 0);
		}
	}

	/**
	 * Goes backward step.
	 */
	public void backward_step() {
		synchronized( _lock) {
			if ( is_head())
				return;

			int position = get_current_position_internal() - 1;
			set_current_position_internal( ( 0 <= position) ? position : 0);
		}
	}

	/**
	 * Goes forward step.
	 */
	public void forward_step() {
		synchronized( _lock) {
			if ( is_tail())
				return;

			int position = get_current_position_internal() + 1;
			set_current_position_internal( ( 0 <= position) ? position : 0);
		}
	}

	/**
	 * Goes forward.
	 */
	public void forward() {
		synchronized( _lock) {
			if ( _headerObject._size < _divide) {
				forward_tail();
				return;
			}

			for ( int i = 0; i < _divide; ++i) {
				int position = ( int)Math.ceil( i * _headerObject._size / _divide);
				if ( get_current_position_internal() < position) {
					set_current_position_internal( position);
					return;
				}
			}

			forward_tail();
		}
	}

	/**
	 * Goes forward tail.
	 */
	public void forward_tail() {
		synchronized( _lock) {
			set_current_position_internal( _headerObject._size - 1);
		}
	}

	/**
	 * Returns the information of the current position in the scenario.
	 * @return the information of the current position in the scenario
	 */
	public String get_information() {
		synchronized( _lock) {
			String time = "";

			if ( _times.size() > _index)
				time = "[ " + ( String)_times.get( _index) + " / " + _headerObject._last + " ]";

			return ( time.equals( "") ? "" : ( time + " ")) + "[ "
				+ ( get_current_position_internal() + 1) + " / " + _headerObject._size + " ]";
		}
	}

	/**
	 * Returns the the current position in the scenario.
	 * @return the the current position in the scenario
	 */
	public double get_current_time() {
		synchronized( _lock) {
			if ( _times.size() <= _index)
				return 0.0f;

			String time = ( String)_times.get( _index);
			if ( time.equals( "") || time.equals( "0/00:00"))
				return 0.0f;

			return CommonTool.time_to_double( time);
		}
	}

	/**
	 * Returns true for displaying the instance of RetrievePropertyDlg.
	 * @param entityBases the array of the all agents
	 * @param directory
	 * @param titlePrefix
	 * @param frame the Frame from which the dialog is displayed
	 * @param component
	 * @return true for displaying the instance of RetrievePropertyDlg
	 */
	public boolean retrieve_agent_property(EntityBase[] entityBases, String directory, String titlePrefix, Frame frame, Component component) {
		if ( !_agentPropertyManager.exist_selected_property())
			return false;

		if ( null == _retrieveAgentPropertyDlg) {
			_retrieveAgentPropertyDlg = new RetrievePropertyDlg(
				frame,
				titlePrefix + ResourceManager.get_instance().get( "retrieve.agent.property.dialog.title"),
				false,
				entityBases,
				directory,
				_agentPropertyManager,
				_agentTransitionManager,
				this);

			if ( !_retrieveAgentPropertyDlg.create())
				return false;

			_retrieveAgentPropertyDlg.setLocationRelativeTo( component);
//			String x = Environment.get_instance().get(
//				Environment._retrieveAgentPropertyDialogLocationKey + "x", "");
//			String y = Environment.get_instance().get(
//				Environment._retrieveAgentPropertyDialogLocationKey + "y", "");
//			if ( ( null == x || x.equals( ""))
//				|| ( null == y || y.equals( "")))
//				_retrieveAgentPropertyDlg.setLocationRelativeTo( component);
//			else {
//				_retrieveAgentPropertyDlg.setLocationRelativeTo( null);
//				_retrieveAgentPropertyDlg.setLocation(
//					Integer.parseInt( x),
//					Integer.parseInt( y));
//			}
		}

		_retrieveAgentPropertyDlg.setVisible( true);

		return true;
	}

	/**
	 * Returns true for displaying the instance of RetrievePropertyDlg.
	 * @param entityBases the array of the all spots
	 * @param directory
	 * @param titlePrefix
	 * @param frame the Frame from which the dialog is displayed
	 * @param component
	 * @return true for displaying the instance of RetrievePropertyDlg
	 */
	public boolean retrieve_spot_property(EntityBase[] entityBases, String directory, String titlePrefix, Frame frame, Component component) {
		if ( !_spotPropertyManager.exist_selected_property())
			return false;

		if ( null == _retrieveSpotPropertyDlg) {
			_retrieveSpotPropertyDlg = new RetrievePropertyDlg(
				frame,
				titlePrefix + ResourceManager.get_instance().get( "retrieve.spot.property.dialog.title"),
				false,
				entityBases,
				directory,
				_spotPropertyManager,
				_spotTransitionManager,
				this);

			if ( !_retrieveSpotPropertyDlg.create())
				return false;

			_retrieveSpotPropertyDlg.setLocationRelativeTo( component);
//			String x = Environment.get_instance().get(
//				Environment._retrieveSpotPropertyDialogLocationKey + "x", "");
//			String y = Environment.get_instance().get(
//				Environment._retrieveSpotPropertyDialogLocationKey + "y", "");
//			if ( ( null == x || x.equals( ""))
//				|| ( null == y || y.equals( "")))
//				_retrieveSpotPropertyDlg.setLocationRelativeTo( component);
//			else {
//				_retrieveSpotPropertyDlg.setLocationRelativeTo( null);
//				_retrieveSpotPropertyDlg.setLocation(
//					Integer.parseInt( x),
//					Integer.parseInt( y));
//			}
		}

		_retrieveSpotPropertyDlg.setVisible( true);

		return true;
	}

	/**
	 * Retrieves the specified value backward.
	 * @param transitionManager the scenario data manager
	 * @param entityBase the specified object
	 * @param directory
	 * @param name the specified name
	 * @param value the specified value
	 */
	public void retrieve_backward(TransitionManager transitionManager, EntityBase entityBase, String directory, String name, String value) {
		int position = IntMessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"retrieve_backward", ResourceManager.get_instance().get( "retrieve.show.message"),
			new Object[] { entityBase, directory, name, value}, ( IIntMessageCallback)this, MainFrame.get_instance());
		if ( 0 > position)
			return;

		set_current_position( position, true);
//		MainFrame.get_instance().set_current_position( position, true);
	}

	/**
	 * Retrieves the specified value, which is not less than the specified value, backward.
	 * @param transitionManager the scenario data manager
	 * @param entityBase the specified object
	 * @param directory
	 * @param name the specified name
	 * @param value the specified value
	 */
	public void retrieve_backward_more_than(TransitionManager transitionManager, EntityBase entityBase, String directory, String name, double value) {
		int position = IntMessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"retrieve_backward_more_than", ResourceManager.get_instance().get( "retrieve.show.message"),
			new Object[] { entityBase, directory, name, new Double( value)}, ( IIntMessageCallback)this, MainFrame.get_instance());
		if ( 0 > position)
			return;

		set_current_position( position, true);
//		MainFrame.get_instance().set_current_position( position, true);
	}

	/**
	 * Retrieves the specified value, which is not more than the specified value, backward.
	 * @param transitionManager the scenario data manager
	 * @param entityBase the specified object
	 * @param directory
	 * @param name the specified name
	 * @param value the specified value
	 */
	public void retrieve_backward_less_than(TransitionManager transitionManager, EntityBase entityBase, String directory, String name, double value) {
		int position = IntMessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"retrieve_backward_less_than", ResourceManager.get_instance().get( "retrieve.show.message"),
			new Object[] { entityBase, directory, name, new Double( value)}, ( IIntMessageCallback)this, MainFrame.get_instance());
		if ( 0 > position)
			return;

		set_current_position( position, true);
//		MainFrame.get_instance().set_current_position( position, true);
	}

	/**
	 * Retrieves the specified value, which is not less than the value0 and not more than value1, backward.
	 * @param transitionManager the scenario data manager
	 * @param entityBase the specified object
	 * @param directory
	 * @param name the specified name
	 * @param value0 the specified value
	 * @param value1 the specified value
	 */
	public void retrieve_backward_more_than_less_than(TransitionManager transitionManager, EntityBase entityBase, String directory, String name, double value0, double value1) {
		int position = IntMessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"retrieve_backward_more_than_less_than", ResourceManager.get_instance().get( "retrieve.show.message"),
			new Object[] { entityBase, directory, name, new Double( value0), new Double( value1)}, ( IIntMessageCallback)this, MainFrame.get_instance());
		if ( 0 > position)
			return;

		set_current_position( position, true);
//		MainFrame.get_instance().set_current_position( position, true);
	}

	/**
	 * Retrieves the specified value forward.
	 * @param transitionManager the scenario data manager
	 * @param entityBase the specified object
	 * @param directory
	 * @param name the specified name
	 * @param value the specified value
	 */
	public void retrieve_forward(TransitionManager transitionManager, EntityBase entityBase, String directory, String name, String value) {
		int index = transitionManager.retrieve_forward( entityBase, name, value, _index);
		if ( 0 <= index) {
			set_current_position( get_current_position_internal() + index, true);
//			MainFrame.get_instance().set_current_position( get_current_position_internal() + index, true);
			return;
		}

		int position = IntMessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"retrieve_forward", ResourceManager.get_instance().get( "retrieve.show.message"),
			new Object[] { entityBase, directory, name, value}, ( IIntMessageCallback)this, MainFrame.get_instance());
		if ( 0 > position)
			return;

		set_current_position( position, true);
//		MainFrame.get_instance().set_current_position( position, true);
	}

	/**
	 * Retrieves the specified value, which is not less than the specified value, forward.
	 * @param transitionManager the scenario data manager
	 * @param entityBase the specified object
	 * @param directory
	 * @param name the specified name
	 * @param value the specified value
	 */
	public void retrieve_forward_more_than(TransitionManager transitionManager, EntityBase entityBase, String directory, String name, double value) {
		int index = transitionManager.retrieve_forward_more_than( entityBase, name, value, _index);
		if ( 0 <= index) {
			set_current_position( get_current_position_internal() + index, true);
//			MainFrame.get_instance().set_current_position( get_current_position_internal() + index, true);
			return;
		}

		int position = IntMessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"retrieve_forward_more_than", ResourceManager.get_instance().get( "retrieve.show.message"),
			new Object[] { entityBase, directory, name, new Double( value)}, ( IIntMessageCallback)this, MainFrame.get_instance());
		if ( 0 > position)
			return;

		set_current_position( position, true);
//		MainFrame.get_instance().set_current_position( position, true);
	}

	/**
	 * Retrieves the specified value, which is not more than the specified value, forward.
	 * @param transitionManager the scenario data manager
	 * @param entityBase the specified object
	 * @param directory
	 * @param name the specified name
	 * @param value the specified value
	 */
	public void retrieve_forward_less_than(TransitionManager transitionManager, EntityBase entityBase, String directory, String name, double value) {
		int index = transitionManager.retrieve_forward_less_than( entityBase, name, value, _index);
		if ( 0 <= index) {
			set_current_position( get_current_position_internal() + index, true);
//			MainFrame.get_instance().set_current_position( get_current_position_internal() + index, true);
			return;
		}

		int position = IntMessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"retrieve_forward_less_than", ResourceManager.get_instance().get( "retrieve.show.message"),
			new Object[] { entityBase, directory, name, new Double( value)}, ( IIntMessageCallback)this, MainFrame.get_instance());
		if ( 0 > position)
			return;

		set_current_position( position, true);
//		MainFrame.get_instance().set_current_position( position, true);
	}

	/**
	 * Retrieves the specified value, which is not less than the value0 and not more than value1, forward.
	 * @param transitionManager the scenario data manager
	 * @param entityBase the specified object
	 * @param directory
	 * @param name the specified name
	 * @param value0 the specified value
	 * @param value1 the specified value
	 */
	public void retrieve_forward_more_than_less_than(TransitionManager transitionManager, EntityBase entityBase, String directory, String name, double value0, double value1) {
		int index = transitionManager.retrieve_forward_more_than_less_than( entityBase, name, value0, value1, _index);
		if ( 0 <= index) {
			set_current_position( get_current_position_internal() + index, true);
//			MainFrame.get_instance().set_current_position( get_current_position_internal() + index, true);
			return;
		}

		int position = IntMessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"retrieve_forward_more_than_less_than", ResourceManager.get_instance().get( "retrieve.show.message"),
			new Object[] { entityBase, directory, name, new Double( value0), new Double( value1)}, ( IIntMessageCallback)this, MainFrame.get_instance());
		if ( 0 > position)
			return;

		set_current_position( position, true);
//		MainFrame.get_instance().set_current_position( position, true);
	}

	/**
	 * @param position
	 * @param slider
	 */
	private void set_current_position(int position, boolean slider) {
		set_current_position( position);
		enable_retrieve_property_dialog( true);
		enable_animation_slider_dialog( true);

		if ( slider)
			update_animation_slider_dialog();

		_animatorView.set_title();
		_animatorView.getParent().repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IIntMessageCallback#int_message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.IntMessageDlg)
	 */
	public int int_message_callback(String id, Object[] objects, IntMessageDlg intMessageDlg) {
		if ( id.equals( "retrieve_backward"))
			return _headerObject.retrieve_backward( ( EntityBase)objects[ 0], ( String)objects[ 1], 
				( String)objects[ 2], ( String)objects[ 3], get_current_position_internal() - 1);
		else if ( id.equals( "retrieve_backward_more_than"))
			return _headerObject.retrieve_backward_more_than( ( EntityBase)objects[ 0], ( String)objects[ 1],
				( String)objects[ 2], ( ( Double)objects[ 3]).doubleValue(), get_current_position_internal() - 1);
		else if ( id.equals( "retrieve_backward_less_than"))
			return _headerObject.retrieve_backward_less_than( ( EntityBase)objects[ 0], ( String)objects[ 1],
				( String)objects[ 2], ( ( Double)objects[ 3]).doubleValue(), get_current_position_internal() - 1);
		else if ( id.equals( "retrieve_backward_more_than_less_than"))
			return _headerObject.retrieve_backward_more_than_less_than( ( EntityBase)objects[ 0], ( String)objects[ 1],
				( String)objects[ 2], ( ( Double)objects[ 3]).doubleValue(), ( ( Double)objects[ 4]).doubleValue(), get_current_position_internal() - 1);
		else if ( id.equals( "retrieve_forward"))
			return _headerObject.retrieve_forward( ( EntityBase)objects[ 0], ( String)objects[ 1],
				( String)objects[ 2], ( String)objects[ 3], _counter);
		else if ( id.equals( "retrieve_forward_more_than"))
			return _headerObject.retrieve_forward_more_than( ( EntityBase)objects[ 0], ( String)objects[ 1],
				( String)objects[ 2], ( ( Double)objects[ 3]).doubleValue(), _counter);
		else if ( id.equals( "retrieve_forward_less_than"))
			return _headerObject.retrieve_forward_less_than( ( EntityBase)objects[ 0], ( String)objects[ 1],
				( String)objects[ 2], ( ( Double)objects[ 3]).doubleValue(), _counter);
		else if ( id.equals( "retrieve_forward_more_than_less_than"))
			return _headerObject.retrieve_forward_more_than_less_than( ( EntityBase)objects[ 0], ( String)objects[ 1],
				( String)objects[ 2], ( ( Double)objects[ 3]).doubleValue(), ( ( Double)objects[ 4]).doubleValue(), _counter);

		return -1;
	}

	/**
	 * Display the instance of AnimationSliderDlg.
	 * @param titlePrefix
	 * @param frame the Frame from which the dialog is displayed
	 * @param component
	 */
	public void show_animation_slider(String titlePrefix, Frame frame, Component component) {
		if ( null == _animationSliderDlg) {
			_animationSliderDlg = new AnimationSliderDlg(
				frame,
				titlePrefix + ResourceManager.get_instance().get( "animation.slider.dialog.title"),
				false,
				this);

			if ( !_animationSliderDlg.create())
				return;

			_animationSliderDlg.setLocationRelativeTo( component);
//			String x = Environment.get_instance().get(
//				Environment._animationSliderDialogLocationKey + "x", "");
//			String y = Environment.get_instance().get(
//				Environment._animationSliderDialogLocationKey + "y", "");
//			if ( ( null == x || x.equals( ""))
//				|| ( null == y || y.equals( "")))
//				_animationSliderDlg.setLocationRelativeTo( component);
//			else {
//				_animationSliderDlg.setLocationRelativeTo( null);
//				_animationSliderDlg.setLocation(
//					Integer.parseInt( x),
//					Integer.parseInt( y));
//			}
		}

		_animationSliderDlg.setVisible( true);
	}

	/**
	 * Returns the tool tip text of the specified object.
	 * @param object the specified object
	 * @param transitionManager the scenario data manager
	 * @param selected_properties the array of the visible properties
	 * @return the tool tip text of the specified object
	 */
	public String get_tooltip_text(Object object, TransitionManager transitionManager, Vector selected_properties) {
		return transitionManager.get_tooltip_text( object, selected_properties, _index);
	}

	/**
	 * Returns true for writing the informations of SOARS log files successfully.
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing the informations of SOARS log files successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(Writer writer) throws SAXException {
		return _headerObject.write( writer);
	}
}
