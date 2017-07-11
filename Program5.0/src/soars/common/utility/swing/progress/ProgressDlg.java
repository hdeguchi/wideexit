/**
 * 
 */
package soars.common.utility.swing.progress;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 *
 */
public class ProgressDlg extends Dialog implements Runnable {

	/**
	 * 
	 */
	protected String _id = "";

	/**
	 * 
	 */
	protected String _cancel = null;

	/**
	 * 
	 */
	protected Object[] _objects = null;

	/**
	 * 
	 */
	private IProgressCallback _progressCallback = null;

	/**
	 * 
	 */
	public boolean _callbackResult = false;

	/**
	 * 
	 */
	public boolean _canceled = false;

	/**
	 * 
	 */
	private JProgressBar _progressBar = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param progressCallback
	 * @param component
	 * @return
	 */
	public static boolean execute(Frame arg0, String arg1, boolean arg2, String id, IProgressCallback progressCallback, Component component) {
		ProgressDlg progressDlg = new ProgressDlg( arg0, arg1, arg2, id, progressCallback);
		progressDlg.do_modal( component);
		return progressDlg._callbackResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param progressCallback
	 * @param component
	 * @return
	 */
	public static boolean execute(Frame arg0, String arg1, boolean arg2, String id, String cancel, IProgressCallback progressCallback, Component component) {
		ProgressDlg progressDlg = new ProgressDlg( arg0, arg1, arg2, id, cancel, progressCallback);
		progressDlg.do_modal( component);
		return progressDlg._callbackResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param objects
	 * @param progressCallback
	 * @param component
	 * @return
	 */
	public static boolean execute(Frame arg0, String arg1, boolean arg2, String id, Object[] objects, IProgressCallback progressCallback, Component component) {
		ProgressDlg progressDlg = new ProgressDlg( arg0, arg1, arg2, id, objects, progressCallback);
		progressDlg.do_modal( component);
		return progressDlg._callbackResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param objects
	 * @param progressCallback
	 * @param component
	 * @return
	 */
	public static boolean execute(Frame arg0, String arg1, boolean arg2, String id, String cancel, Object[] objects, IProgressCallback progressCallback, Component component) {
		ProgressDlg progressDlg = new ProgressDlg( arg0, arg1, arg2, id, cancel, objects, progressCallback);
		progressDlg.do_modal( component);
		return progressDlg._callbackResult;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param progressCallback
	 */
	public ProgressDlg(Frame arg0, String arg1, boolean arg2, String id, IProgressCallback progressCallback) {
		super(arg0, arg1, arg2);
		_id = id;
		_progressCallback = progressCallback;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param progressCallback
	 */
	public ProgressDlg(Frame arg0, String arg1, boolean arg2, String id, String cancel, IProgressCallback progressCallback) {
		super(arg0, arg1, arg2);
		_id = id;
		_cancel = cancel;
		_progressCallback = progressCallback;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param objects
	 * @param progressCallback
	 */
	public ProgressDlg(Frame arg0, String arg1, boolean arg2, String id, Object[] objects, IProgressCallback progressCallback) {
		super(arg0, arg1, arg2);
		_id = id;
		_objects = objects;
		_progressCallback = progressCallback;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param id
	 * @param cancel
	 * @param objects
	 * @param progressCallback
	 */
	public ProgressDlg(Frame arg0, String arg1, boolean arg2, String id, String cancel, Object[] objects, IProgressCallback progressCallback) {
		super(arg0, arg1, arg2);
		_id = id;
		_cancel = cancel;
		_objects = objects;
		_progressCallback = progressCallback;
	}

	/**
	 * @param value
	 */
	public void set(int value) {
		_progressBar.setValue( value);
		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;



		setResizable( false);



		getContentPane().setLayout( new BorderLayout());



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( centerPanel);

		if ( !setup( centerPanel))
			return false;

		insert_horizontal_glue( centerPanel);

		getContentPane().add( centerPanel);



		setup_cancel_button();



		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);



		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_progressBar = new JProgressBar( 0, 100);
		_progressBar.setValue( 0);
		_progressBar.setStringPainted( true);
		_progressBar.setPreferredSize( new Dimension( 400,
			_progressBar.getPreferredSize().height));
		panel.add( _progressBar);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * 
	 */
	private void setup_cancel_button() {
		if ( null == _cancel)
			return;

		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		setup_cancel_button( panel, _cancel, false);
		southPanel.add( panel);

		insert_horizontal_glue( southPanel);

		getContentPane().add( southPanel, "South");
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		Thread thread = new Thread( this);
		thread.start();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_cancel(java.awt.event.ActionEvent)
	 */
	protected void on_cancel(ActionEvent actionEvent) {
		if ( null == _cancel)
			return;

		_canceled = true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		_callbackResult = _progressCallback.progress_callback( _id, _objects, this);
		dispose();
	}
}
