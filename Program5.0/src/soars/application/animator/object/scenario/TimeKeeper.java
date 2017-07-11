/**
 * 
 */
package soars.application.animator.object.scenario;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.util.Vector;

import soars.application.animator.common.tool.CommonTool;
import soars.application.animator.main.Administrator;
import soars.application.animator.main.Constant;
import soars.application.animator.main.Environment;
import soars.application.animator.main.MainFrame;
import soars.application.animator.main.ResourceManager;
import soars.application.animator.object.file.HeaderObject;
import soars.application.animator.object.file.TimeKeeperHeaderObject;
import soars.application.animator.object.scenario.slider.AnimationSliderDlg;
import soars.application.animator.setting.common.CommonProperty;
import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.MessageDlg;

/**
 * @author kurata
 *
 */
public class TimeKeeper implements IScenarioManipulator, IMessageCallback {

	/**
	 * 
	 */
	private Object _lock = new Object();

	/**
	 * 
	 */
	public TimeKeeperHeaderObject _timeKeeperHeaderObject = null;

	/**
	 * 
	 */
	private Vector<String> _times = new Vector<String>();

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
	 * 
	 */
	private AnimationSliderDlg _animationSliderDlg = null;

	/**
	 * @param headerObject
	 * 
	 */
	public TimeKeeper(HeaderObject headerObject) {
		super();
		_timeKeeperHeaderObject = new TimeKeeperHeaderObject( headerObject);
	}

	/* (non-Javadoc)
	 * @see soars.application.animator.object.scenario.IScenarioManipulator#get_size()
	 */
	public int get_size() {
		synchronized( _lock) {
			return _timeKeeperHeaderObject._size;
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
		MainFrame.get_instance().update_current_position( position, true);
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
			return ( ( _timeKeeperHeaderObject._size == _counter) && ( 1 == _times.size()));
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
	 * 
	 */
	public void load_on_timer_task() {
		read();
	}

	/**
	 * 
	 */
	public boolean read() {
		if ( _timeKeeperHeaderObject._size == _counter)
			return true;

		if ( Constant._bufferingSize <= _times.size())
			return true;

		return read( Math.min( Constant._bufferingSize - _times.size(), _timeKeeperHeaderObject._size - _counter));
	}

	/**
	 * @param size
	 * @return
	 */
	private boolean read(int size) {
		Vector<String> times = new Vector<String>();

		if ( !_timeKeeperHeaderObject.read( _counter, size, times))
			return false;

		synchronized( _lock) {
			_times.addAll( times);

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
		int currentPosition = get_current_position_internal();
		if ( currentPosition == position)
			return;

		Vector<String> times = new Vector<String>();

//		int type = -1;

		int size = Math.abs( position - currentPosition);
		if ( _times.size() > size) {
			if ( position < currentPosition) {
//				type = 1;
				if ( !_timeKeeperHeaderObject.read( position, size, times))
					return;

				_times.addAll( 0, times);

				while ( Constant._bufferingSize < _times.size())
					_times.removeElementAt( _times.size() - 1);

			} else {
//				type = 2;
				if ( _timeKeeperHeaderObject._size > _counter) {
					size = Math.min( size, _timeKeeperHeaderObject._size - _counter);
					if ( !_timeKeeperHeaderObject.read( _counter, size, times))
						return;

					_times.addAll( times);
				}

				for ( int i = 0; i < size; ++i)
					_times.removeElementAt( 0);

			}
		} else {
//			type = 3;
			size = Math.min( Constant._bufferingSize, _timeKeeperHeaderObject._size - position);

			if ( !_timeKeeperHeaderObject.read( position, size, times))
				return;

			_times.clear();

			_times.addAll( times);
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
		dispose_animation_slider_dialog();

		_timeKeeperHeaderObject = null;

		_times.clear();

		_counter = 0;

		reset();
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
	 * 
	 */
	public void next() {
		if ( !is_tail() && !is_waiting()) {
			synchronized( _lock) {
				_times.removeElementAt( 0);
			}
		}
	}

	/**
	 * 
	 */
	public void animate_on_timer_task() {
		if ( is_tail() && CommonProperty.get_instance()._repeat)
			rewind();

		update_animation_slider_dialog();
	}

	/**
	 * Goes backward.
	 */
	public void backward() {
		synchronized( _lock) {
			if ( _timeKeeperHeaderObject._size < _divide) {
				set_current_position_internal( 0);
				return;
			}

			for ( int i = _divide - 1; i >= 0; --i) {
				int position = ( int)Math.floor( i * _timeKeeperHeaderObject._size / _divide);
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
			int position = get_current_position_internal() - 1;
			set_current_position_internal( ( 0 <= position) ? position : 0);
		}
	}

	/**
	 * Goes forward step.
	 */
	public void forward_step() {
		synchronized( _lock) {
			int position = get_current_position_internal() + 1;
			set_current_position_internal( ( 0 <= position) ? position : 0);
		}
	}

	/**
	 * Goes forward.
	 */
	public void forward() {
		synchronized( _lock) {
			if ( _timeKeeperHeaderObject._size < _divide) {
				forward_tail();
				return;
			}

			for ( int i = 0; i < _divide; ++i) {
				int position = ( int)Math.ceil( i * _timeKeeperHeaderObject._size / _divide);
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
			set_current_position_internal( _timeKeeperHeaderObject._size - 1);
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
				time = "[ " + ( String)_times.get( _index) + " / " + _timeKeeperHeaderObject._last + " ]";

			return ( time.equals( "") ? "" : ( time + " ")) + "[ "
				+ ( get_current_position_internal() + 1) + " / " + _timeKeeperHeaderObject._size + " ]";
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
	 * Display the instance of AnimationSliderDlg.
	 * @param frame the Frame from which the dialog is displayed
	 * @param component
	 */
	public void show_animation_slider(Frame frame, Component component) {
		if ( null == _animationSliderDlg) {
			_animationSliderDlg = new AnimationSliderDlg(
				frame,
				ResourceManager.get_instance().get( "animation.slider.dialog.title"),
				false,
				this);

			if ( !_animationSliderDlg.create())
				return;

			//_animationSliderDlg.pack();

			String x = Environment.get_instance().get(
				Environment._animationSliderDialogLocationKey + "x", "");
			String y = Environment.get_instance().get(
				Environment._animationSliderDialogLocationKey + "y", "");
			if ( ( null == x || x.equals( ""))
				|| ( null == y || y.equals( "")))
				_animationSliderDlg.setLocationRelativeTo( component);
			else {
				_animationSliderDlg.setLocationRelativeTo( null);
				_animationSliderDlg.setLocation(
					Integer.parseInt( x),
					Integer.parseInt( y));
			}
		}

		_animationSliderDlg.setVisible( true);
	}
}
