/**
 * 
 */
package soars.common.utility.swing.message;

import java.awt.Component;
import java.awt.Frame;

import soars.common.utility.swing.message.MessageDlg;

/**
 * @author kurata
 *
 */
public class IntMessageDlg extends MessageDlg {

	/**
	 * 
	 */
	private IIntMessageCallback _intMessageCallback = null;

	/**
	 * 
	 */
	public int _callbackIntResult = 0;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param message
	 * @param intMessageCallback
	 * @param component
	 * @return
	 */
	public static int execute(Frame arg0, String arg1, boolean arg2, String id, String message, IIntMessageCallback intMessageCallback, Component component) {
		IntMessageDlg intMessageDlg = new IntMessageDlg( arg0, arg1, arg2, id, message, intMessageCallback);
		intMessageDlg.do_modal( component);
		return intMessageDlg._callbackIntResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param message
	 * @param intMessageCallback
	 * @param component
	 * @return
	 */
	public static int execute(Frame arg0, String arg1, boolean arg2, String id, String cancel, String message, IIntMessageCallback intMessageCallback, Component component) {
		IntMessageDlg intMessageDlg = new IntMessageDlg( arg0, arg1, arg2, id, cancel, message, intMessageCallback);
		intMessageDlg.do_modal( component);
		return intMessageDlg._callbackIntResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param message
	 * @param objects
	 * @param intMessageCallback
	 * @param component
	 * @return
	 */
	public static int execute(Frame arg0, String arg1, boolean arg2, String id, String message, Object[] objects, IIntMessageCallback intMessageCallback, Component component) {
		IntMessageDlg intMessageDlg = new IntMessageDlg( arg0, arg1, arg2, id, message, objects, intMessageCallback);
		intMessageDlg.do_modal( component);
		return intMessageDlg._callbackIntResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param message
	 * @param objects
	 * @param intMessageCallback
	 * @param component
	 * @return
	 */
	public static int execute(Frame arg0, String arg1, boolean arg2, String id, String cancel, String message, Object[] objects, IIntMessageCallback intMessageCallback, Component component) {
		IntMessageDlg intMessageDlg = new IntMessageDlg( arg0, arg1, arg2, id, cancel, message, objects, intMessageCallback);
		intMessageDlg.do_modal( component);
		return intMessageDlg._callbackIntResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param message
	 * @param intMessageCallback
	 */
	public IntMessageDlg(Frame arg0, String arg1, boolean arg2, String id, String message, IIntMessageCallback intMessageCallback) {
		super(arg0, arg1, arg2, id, message, null);
		_intMessageCallback = intMessageCallback;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param message
	 * @param intMessageCallback
	 */
	public IntMessageDlg(Frame arg0, String arg1, boolean arg2, String id, String cancel, String message, IIntMessageCallback intMessageCallback) {
		super(arg0, arg1, arg2, id, cancel, message, null);
		_intMessageCallback = intMessageCallback;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param message
	 * @param objects
	 * @param intMessageCallback
	 */
	public IntMessageDlg(Frame arg0, String arg1, boolean arg2, String id, String message, Object[] objects, IIntMessageCallback intMessageCallback) {
		super(arg0, arg1, arg2, id, message, objects, null);
		_intMessageCallback = intMessageCallback;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param message
	 * @param objects
	 * @param intMessageCallback
	 */
	public IntMessageDlg(Frame arg0, String arg1, boolean arg2, String id, String cancel, String message, Object[] objects, IIntMessageCallback intMessageCallback) {
		super(arg0, arg1, arg2, id, cancel, message, objects, null);
		_intMessageCallback = intMessageCallback;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.MessageDlg#run()
	 */
	public void run() {
		if ( null != _intMessageCallback)
			_callbackIntResult = _intMessageCallback.int_message_callback( _id, _objects, this);

		dispose();
	}
}
