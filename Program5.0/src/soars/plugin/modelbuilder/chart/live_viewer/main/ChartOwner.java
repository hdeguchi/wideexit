/*
 * 2004/11/08
 */
package soars.plugin.modelbuilder.chart.live_viewer.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import soars.common.utility.tool.reflection.Reflection;
import soars.common.utility.tool.timer.ITimerTaskImplementCallback;
import soars.common.utility.tool.timer.TimerTaskImplement;

/**
 * @author kurata
 */
public class ChartOwner implements ITimerTaskImplementCallback {


	/**
	 * 
	 */
	static private int global_id = 0;


	/**
	 * 
	 */
	private Object _chartFrame = null;

	/**
	 * 
	 */
	private Object _chartLogger = null;


	/**
	 * 
	 */
	private int _id;


	/**
	 * 
	 */
	private Timer _timer = null;

	/**
	 * 
	 */
	private TimerTaskImplement _timerTaskImplement = null;

	/**
	 * 
	 */
	private final long _delay = 0;
	//private final long _period = 5;

	/**
	 * 
	 */
	private final long _period = 10;
	//private final long _period = 33;
	//private final long _period = 100;


	/**
	 * 
	 */
	private double _count = 0.0;

	/**
	 * 
	 */
	public ChartOwner() {
		super();
		_id = global_id++;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.tool.timer.ITimerTaskImplementCallback#execute_timer_task(int)
	 */
	public void execute_timer_task(int id) {
		Reflection.execute_class_method( _chartFrame, "append",
			new Class[] { Integer.class, Double.class, Double.class},
			new Object[] { new Integer( 0),
				new Double( Math.PI * _count / 25),
				new Double( 50.0 + ( Math.sin( Math.PI * _count / 25)))});
		Reflection.execute_class_method( _chartFrame, "append",
			new Class[] { Integer.class, Double.class, Double.class},
			new Object[] { new Integer( 1),
				new Double( Math.PI * _count / 25),
				new Double( 50.0 + 10.0 * ( Math.cos( Math.PI * _count / 25)))});

//		_chartFrame.append( 2, Math.PI * _count / 25, 50.0 + 9.0 * ( Math.cos( Math.PI * _count / 25)));
//		_chartFrame.append( 3, Math.PI * _count / 25, 50.0 + 8.0 * ( Math.cos( Math.PI * _count / 25)));
//		_chartFrame.append( 4, Math.PI * _count / 25, 50.0 + 7.0 * ( Math.cos( Math.PI * _count / 25)));
//		_chartFrame.append( 5, Math.PI * _count / 25, 50.0 + 6.0 * ( Math.cos( Math.PI * _count / 25)));
//		_chartFrame.append( 6, Math.PI * _count / 25, 50.0 + 5.0 * ( Math.cos( Math.PI * _count / 25)));
//		_chartFrame.append( 7, Math.PI * _count / 25, 50.0 + 4.0 * ( Math.cos( Math.PI * _count / 25)));
//		_chartFrame.append( 8, Math.PI * _count / 25, 50.0 + 3.0 * ( Math.cos( Math.PI * _count / 25)));
//		_chartFrame.append( 9, Math.PI * _count / 25, 50.0 + 2.0 * ( Math.cos( Math.PI * _count / 25)));
//		_chartFrame.append( 10, Math.PI * _count / 25, 50.0 + 20.0 * ( Math.cos( Math.PI * _count / 25)));
//		_chartFrame.append( 0, _count, Math.pow( _count, 0.5));
//		_chartFrame.append( 2, _count, 100 * Math.pow( _count, 2.0));
//		_chartFrame.append( 2, _count, Math.exp( _count), true);
//		_chartFrame.append( 1, _count, Math.pow( _count, 3.0));
//		_chartFrame.append( 2, _count, Math.pow( _count, 4.0));
//		_chartFrame.append( 3, _count, Math.pow( _count, 5.0));
//		_chartFrame.append( 4, _count, Math.pow( _count, 6.0));
//		_chartFrame.append( Math.sin( Math.PI * _count / 25),
//			Math.cos( Math.PI * _count / 100));
//		_chartFrame.append( Math.sin( Math.PI * _count / 45),
//			Math.cos( Math.PI * _count / 70));
//		_chartFrame.append( Math.sin( Math.PI * _count / 45),
//			Math.cos( Math.PI * _count / 70));
//		_chartFrame.append( Math.sin( Math.PI * _count / 20),
//			Math.cos( Math.PI * _count / 100));
//		_chartFrame.append( Math.sin( Math.PI * _count / 50),
//			Math.cos( Math.PI * _count / 70));

		Reflection.execute_class_method( _chartLogger, "append",
			new Class[] { Integer.class, Double.class, Double.class},
			new Object[] { new Integer( 0),
				new Double( Math.PI * _count / 25),
				new Double( 50.0 + ( Math.sin( Math.PI * _count / 25)))});
		Reflection.execute_class_method( _chartLogger, "append",
			new Class[] { Integer.class, Double.class, Double.class},
			new Object[] { new Integer( 1),
				new Double( Math.PI * _count / 25),
				new Double( 50.0 + 10.0 * ( Math.cos( Math.PI * _count / 25)))});

		_count += 1.0;
	}

	/**
	 * 
	 */
	public void cleanup() {
		stop();
		// _chartFrame. How terminate?
	}

	/**
	 * 
	 */
	public void start() {
		if ( null == _chartFrame) {
			_chartFrame = Reflection.get_class_instance( "soars.plugin.modelbuilder.chart.chart.main.ChartFrame");
			if ( null == _chartFrame)
				return;

			List resultList = new ArrayList();
			if ( !Reflection.execute_class_method(
				_chartFrame, "create", new Class[ 0], new Object[ 0], resultList))
				return;

			if ( resultList.isEmpty() || null == resultList.get( 0)
				|| !( resultList.get( 0) instanceof Boolean))
				return;

			Boolean result = ( Boolean)resultList.get( 0);
			if ( !result.booleanValue())
				return;

			Reflection.execute_class_method( _chartFrame, "setTitle",
				new Class[] { String.class}, new Object[] { "Test"});

			Reflection.execute_class_method( _chartFrame, "setXLabel",
				new Class[] { String.class}, new Object[] { "LiveX"});
			Reflection.execute_class_method( _chartFrame, "setYLabel",
				new Class[] { String.class}, new Object[] { "LiveY"});

			Reflection.execute_class_method( _chartFrame, "addLegend",
				new Class[] { Integer.class, String.class}, new Object[] { new Integer( 0), "Test0"});
			Reflection.execute_class_method( _chartFrame, "addLegend",
				new Class[] { Integer.class, String.class}, new Object[] { new Integer( 1), "Test1"});

//			Refrection.execute_class_method( _chartFrame, "setYRange",
//				new Class[] { Double.class, Double.class},
//				new Object[] { new Double( 50), new Double( 55)});
		}

		if ( null == _chartLogger) {
			_chartLogger = Reflection.get_class_instance( "soars.plugin.modelbuilder.chart.chart.main.ChartLogger");
			if ( null == _chartLogger)
				return;

			Reflection.execute_class_method( _chartLogger, "setTitle",
				new Class[] { String.class}, new Object[] { "Test"});

			Reflection.execute_class_method( _chartLogger, "setXLabel",
				new Class[] { String.class}, new Object[] { "LogX"});
			Reflection.execute_class_method( _chartLogger, "setYLabel",
				new Class[] { String.class}, new Object[] { "LogY"});

//			Refrection.execute_class_method( _chartLogger, "setYRange",
//				new Class[] { Double.class, Double.class},
//				new Object[] { new Double( 50), new Double( 55)});
		}

		start_timer();
	}

	/**
	 * 
	 */
	public void stop() {
		stop_timer();
	}

	/**
	 * 
	 */
	private void start_timer() {
		if ( null == _timer) {
			_timer = new Timer();
			_timerTaskImplement = new TimerTaskImplement( 0, this);
			_timer.schedule( _timerTaskImplement, _delay, _period);
		}
	}

	/**
	 * 
	 */
	private void stop_timer() {
		if ( null != _timer) {
			_timer.cancel();
			_timer = null;
		}
	}

	/**
	 * @return
	 */
	public boolean write() {
		if ( null == _chartFrame || null == _chartLogger)
			return false;

		List resultList = new ArrayList();
		if ( !Reflection.execute_class_method(
			_chartFrame, "write", new Class[] { String.class}, new Object[] { "result1.plt"}, resultList))
			return false;

		if ( resultList.isEmpty() || null == resultList.get( 0)
			|| !( resultList.get( 0) instanceof Boolean))
			return false;

		Boolean result = ( Boolean)resultList.get( 0);
		if ( !result.booleanValue())
			return false;


		resultList.clear();


		if ( !Reflection.execute_class_method(
			_chartLogger, "write", new Class[] { String.class}, new Object[] { "result2.plt"}, resultList))
			return false;

		if ( resultList.isEmpty() || null == resultList.get( 0)
			|| !( resultList.get( 0) instanceof Boolean))
			return false;

		result = ( Boolean)resultList.get( 0);
		if ( !result.booleanValue())
			return false;

		return true;
	}
}
