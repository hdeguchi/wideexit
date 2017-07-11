/**
 * 
 */
package soars.common.soars.tool;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Timer;

import soars.common.utility.tool.timer.ITimerTaskImplementCallback;
import soars.common.utility.tool.timer.TimerTaskImplement;

/**
 * @author kurata
 *
 */
public class Receiver extends DatagramSocket implements ITimerTaskImplementCallback {

	/**
	 * 
	 */
	private int _timeout = 5000;

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
	private final int _id = 1;

	/**
	 * 
	 */
	private final long _delay = 0;

	/**
	 * 
	 */
	private final long _period = 1000;

	/**
	 * 
	 */
	private boolean _running = false;

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static public final String _noPortal = "No portal!";

	/**
	 * 
	 */
	private String _portalIpAddress = _noPortal;

	/**
	 * @param port
	 * @throws SocketException
	 */
	public Receiver(int port) throws SocketException {
		super(port);
		setSoTimeout( _timeout);
	}

	/**
	 * @return
	 */
	public String get() {
		synchronized( _lock) {
			return _portalIpAddress;
		}
	}

	/**
	 * @param portalIpAddress
	 */
	private void set(String portalIpAddress) {
		synchronized( _lock) {
			_portalIpAddress = portalIpAddress;
		}
	}

	/**
	 * 
	 */
	public void start() {
		if ( null == _timer) {
			_running = false;
			_timer = new Timer();
			_timerTaskImplement = new TimerTaskImplement( _id, this);
			_timer.schedule( _timerTaskImplement, _delay, _period);
		}
	}

	/**
	 * 
	 */
	public void stop() {
		close();

		if ( null != _timer) {
			_timer.cancel();
			_timer = null;
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.tool.timer.ITimerTaskImplementCallback#execute_timer_task(int)
	 */
	public void execute_timer_task(int id) {
		if ( id != _id || _running)
			return;

		_running = true;

		try {
			byte buf[] = new byte[ 1024];
			DatagramPacket datagramPacket = new DatagramPacket( buf, buf.length);
			try {
				receive( datagramPacket);
				String text = new String( buf);
				set( text.startsWith( datagramPacket.getAddress().getHostAddress())
					? datagramPacket.getAddress().getHostAddress() : _noPortal);
			} catch (SocketTimeoutException e) {
				//e.printStackTrace();
				set( _noPortal);
				_running = false;
				return;
			}
		} catch (IOException e) {
			//e.printStackTrace();
			_running = false;
			return;
		}

		_running = false;
	}
}
