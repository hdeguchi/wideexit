/**
 * 
 */
package soars.common.utility.swing.progress;

import java.awt.Component;
import java.awt.Frame;

/**
 * @author kurata
 *
 */
public class IntProgressDlg extends ProgressDlg {

	/**
	 * 
	 */
	private IIntProgressCallback _intProgressCallback = null;

	/**
	 * 
	 */
	public int _callbackIntResult = 0;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param intProgressCallback
	 * @param component
	 * @return
	 */
	public static int execute(Frame arg0, String arg1, boolean arg2, String id, IIntProgressCallback intProgressCallback, Component component) {
		IntProgressDlg intProgressDlg = new IntProgressDlg( arg0, arg1, arg2, id, intProgressCallback);
		intProgressDlg.do_modal( component);
		return intProgressDlg._callbackIntResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param intProgressCallback
	 * @param component
	 * @return
	 */
	public static int execute(Frame arg0, String arg1, boolean arg2, String id, String cancel, IIntProgressCallback intProgressCallback, Component component) {
		IntProgressDlg intProgressDlg = new IntProgressDlg( arg0, arg1, arg2, id, cancel, intProgressCallback);
		intProgressDlg.do_modal( component);
		return intProgressDlg._callbackIntResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param objects
	 * @param intProgressCallback
	 * @param component
	 * @return
	 */
	public static int execute(Frame arg0, String arg1, boolean arg2, String id, Object[] objects, IIntProgressCallback intProgressCallback, Component component) {
		IntProgressDlg intProgressDlg = new IntProgressDlg( arg0, arg1, arg2, id, objects, intProgressCallback);
		intProgressDlg.do_modal( component);
		return intProgressDlg._callbackIntResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param objects
	 * @param intProgressCallback
	 * @param component
	 * @return
	 */
	public static int execute(Frame arg0, String arg1, boolean arg2, String id, String cancel, Object[] objects, IIntProgressCallback intProgressCallback, Component component) {
		IntProgressDlg intProgressDlg = new IntProgressDlg( arg0, arg1, arg2, id, cancel, objects, intProgressCallback);
		intProgressDlg.do_modal( component);
		return intProgressDlg._callbackIntResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param intProgressCallback
	 */
	public IntProgressDlg(Frame arg0, String arg1, boolean arg2, String id, IIntProgressCallback intProgressCallback) {
		super(arg0, arg1, arg2, id, null);
		_intProgressCallback = intProgressCallback;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param intProgressCallback
	 */
	public IntProgressDlg(Frame arg0, String arg1, boolean arg2, String id, String cancel, IIntProgressCallback intProgressCallback) {
		super(arg0, arg1, arg2, id, cancel, null);
		_intProgressCallback = intProgressCallback;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param objects
	 * @param intProgressCallback
	 */
	public IntProgressDlg(Frame arg0, String arg1, boolean arg2, String id, Object[] objects, IIntProgressCallback intProgressCallback) {
		super(arg0, arg1, arg2, id, objects, null);
		_intProgressCallback = intProgressCallback;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param objects
	 * @param intProgressCallback
	 */
	public IntProgressDlg(Frame arg0, String arg1, boolean arg2, String id, String cancel, Object[] objects, IIntProgressCallback intProgressCallback) {
		super(arg0, arg1, arg2, id, cancel, objects, null);
		_intProgressCallback = intProgressCallback;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.progress.ProgressDlg#run()
	 */
	public void run() {
		if ( null != _intProgressCallback)
			_callbackIntResult = _intProgressCallback.int_message_callback( _id, _objects, this);

		dispose();
	}
}
