/*
 * Created on 2004/10/06
 */
package soars.common.utility.tool.timer;

import java.util.TimerTask;

/**
 * @author kurata
 */
public class TimerTaskImplement extends TimerTask {

	/**
	 * 
	 */
	private int _id;

	/**
	 * 
	 */
	private ITimerTaskImplementCallback _timerTaskImplementCallback = null;
 
	/**
	 * @param timerTaskImplementCallback
	 */
	public TimerTaskImplement(int id, ITimerTaskImplementCallback timerTaskImplementCallback) {
		_id = id;
		_timerTaskImplementCallback = timerTaskImplementCallback;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		_timerTaskImplementCallback.execute_timer_task( _id);
	}
}
