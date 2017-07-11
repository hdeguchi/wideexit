/**
 * 
 */
package soars.common.utility.swing.message;

import java.awt.Component;
import java.awt.Frame;

/**
 * @author kurata
 *
 */
public class ObjectsMessageDlg extends MessageDlg {

	/**
	 * 
	 */
	private IObjectsMessageCallback _objectsMessageCallback = null;

	/**
	 * 
	 */
	public Object[] _callbackObjectsResult = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param message
	 * @param objectsMessageCallback
	 * @param component
	 * @return
	 */
	public static Object[] execute(Frame arg0, String arg1, boolean arg2, String id, String message, IObjectsMessageCallback objectsMessageCallback, Component component) {
		ObjectsMessageDlg objectsMessageDlg = new ObjectsMessageDlg( arg0, arg1, arg2, id, message, objectsMessageCallback);
		objectsMessageDlg.do_modal( component);
		return objectsMessageDlg._callbackObjectsResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param message
	 * @param objectsMessageCallback
	 * @param component
	 * @return
	 */
	public static Object[] execute(Frame arg0, String arg1, boolean arg2, String id, String cancel, String message, IObjectsMessageCallback objectsMessageCallback, Component component) {
		ObjectsMessageDlg objectsMessageDlg = new ObjectsMessageDlg( arg0, arg1, arg2, id, cancel, message, objectsMessageCallback);
		objectsMessageDlg.do_modal( component);
		return objectsMessageDlg._callbackObjectsResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param message
	 * @param objects
	 * @param objectsMessageCallback
	 * @param component
	 * @return
	 */
	public static Object[] execute(Frame arg0, String arg1, boolean arg2, String id, String message, Object[] objects, IObjectsMessageCallback objectsMessageCallback, Component component) {
		ObjectsMessageDlg objectsMessageDlg = new ObjectsMessageDlg( arg0, arg1, arg2, id, message, objects, objectsMessageCallback);
		objectsMessageDlg.do_modal( component);
		return objectsMessageDlg._callbackObjectsResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param message
	 * @param objects
	 * @param objectsMessageCallback
	 * @param component
	 * @return
	 */
	public static Object[] execute(Frame arg0, String arg1, boolean arg2, String id, String cancel, String message, Object[] objects, IObjectsMessageCallback objectsMessageCallback, Component component) {
		ObjectsMessageDlg objectsMessageDlg = new ObjectsMessageDlg( arg0, arg1, arg2, id, cancel, message, objects, objectsMessageCallback);
		objectsMessageDlg.do_modal( component);
		return objectsMessageDlg._callbackObjectsResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param message
	 * @param objectsMessageCallback
	 */
	public ObjectsMessageDlg(Frame arg0, String arg1, boolean arg2, String id, String message, IObjectsMessageCallback objectsMessageCallback) {
		super(arg0, arg1, arg2, id, message, null);
		_objectsMessageCallback = objectsMessageCallback;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param message
	 * @param objectsMessageCallback
	 */
	public ObjectsMessageDlg(Frame arg0, String arg1, boolean arg2, String id, String cancel, String message, IObjectsMessageCallback objectsMessageCallback) {
		super(arg0, arg1, arg2, id, cancel, message, null);
		_objectsMessageCallback = objectsMessageCallback;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param message
	 * @param objects
	 * @param objectsMessageCallback
	 */
	public ObjectsMessageDlg(Frame arg0, String arg1, boolean arg2, String id, String message, Object[] objects, IObjectsMessageCallback objectsMessageCallback) {
		super(arg0, arg1, arg2, id, message, objects, null);
		_objectsMessageCallback = objectsMessageCallback;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param message
	 * @param objects
	 * @param objectsMessageCallback
	 */
	public ObjectsMessageDlg(Frame arg0, String arg1, boolean arg2, String id, String cancel, String message, Object[] objects, IObjectsMessageCallback objectsMessageCallback) {
		super(arg0, arg1, arg2, id, cancel, message, objects, null);
		_objectsMessageCallback = objectsMessageCallback;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.MessageDlg#run()
	 */
	public void run() {
		if ( null != _objectsMessageCallback)
			_callbackObjectsResult = _objectsMessageCallback.objects_message_callback( _id, _objects, this);

		dispose();
	}
}
