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
public class ObjectsProgressDlg extends ProgressDlg {

	/**
	 * 
	 */
	private IObjectsProgressCallback _objectsProgressCallback = null;

	/**
	 * 
	 */
	public Object[] _callbackObjectsResult = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param objectsProgressCallback
	 * @param component
	 * @return
	 */
	public static Object[] execute(Frame arg0, String arg1, boolean arg2, String id, IObjectsProgressCallback objectsProgressCallback, Component component) {
		ObjectsProgressDlg objectsProgressDlg = new ObjectsProgressDlg( arg0, arg1, arg2, id, objectsProgressCallback);
		objectsProgressDlg.do_modal( component);
		return objectsProgressDlg._callbackObjectsResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param objectsProgressCallback
	 * @param component
	 * @return
	 */
	public static Object[] execute(Frame arg0, String arg1, boolean arg2, String id, String cancel, IObjectsProgressCallback objectsProgressCallback, Component component) {
		ObjectsProgressDlg objectsProgressDlg = new ObjectsProgressDlg( arg0, arg1, arg2, id, cancel, objectsProgressCallback);
		objectsProgressDlg.do_modal( component);
		return objectsProgressDlg._callbackObjectsResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param objects
	 * @param objectsProgressCallback
	 * @param component
	 * @return
	 */
	public static Object[] execute(Frame arg0, String arg1, boolean arg2, String id, Object[] objects, IObjectsProgressCallback objectsProgressCallback, Component component) {
		ObjectsProgressDlg objectsProgressDlg = new ObjectsProgressDlg( arg0, arg1, arg2, id, objects, objectsProgressCallback);
		objectsProgressDlg.do_modal( component);
		return objectsProgressDlg._callbackObjectsResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param objects
	 * @param objectsProgressCallback
	 * @param component
	 * @return
	 */
	public static Object[] execute(Frame arg0, String arg1, boolean arg2, String id, String cancel, Object[] objects, IObjectsProgressCallback objectsProgressCallback, Component component) {
		ObjectsProgressDlg objectsProgressDlg = new ObjectsProgressDlg( arg0, arg1, arg2, id, cancel, objects, objectsProgressCallback);
		objectsProgressDlg.do_modal( component);
		return objectsProgressDlg._callbackObjectsResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param objectsProgressCallback
	 */
	public ObjectsProgressDlg(Frame arg0, String arg1, boolean arg2, String id, IObjectsProgressCallback objectsProgressCallback) {
		super(arg0, arg1, arg2, id, null);
		_objectsProgressCallback = objectsProgressCallback;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param objectsProgressCallback
	 */
	public ObjectsProgressDlg(Frame arg0, String arg1, boolean arg2, String id, String cancel, IObjectsProgressCallback objectsProgressCallback) {
		super(arg0, arg1, arg2, id, cancel, null);
		_objectsProgressCallback = objectsProgressCallback;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param objects
	 * @param objectsProgressCallback
	 */
	public ObjectsProgressDlg(Frame arg0, String arg1, boolean arg2, String id, Object[] objects, IObjectsProgressCallback objectsProgressCallback) {
		super(arg0, arg1, arg2, id, objects, null);
		_objectsProgressCallback = objectsProgressCallback;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param objects
	 * @param objectsProgressCallback
	 */
	public ObjectsProgressDlg(Frame arg0, String arg1, boolean arg2, String id, String cancel, Object[] objects, IObjectsProgressCallback objectsProgressCallback) {
		super(arg0, arg1, arg2, id, cancel, objects, null);
		_objectsProgressCallback = objectsProgressCallback;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.progress.ProgressDlg#run()
	 */
	public void run() {
		if ( null != _objectsProgressCallback)
			_callbackObjectsResult = _objectsProgressCallback.objects_message_callback( _id, _objects, this);

		dispose();
	}
}
