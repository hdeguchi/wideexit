/**
 * 
 */
package soars.application.visualshell.observer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.common.soars.warning.WarningList;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 *
 */
public class WarningDlg3 extends Dialog {

	/**
	 * 
	 */
	static public final int _minimumWidth = 640;

	/**
	 * 
	 */
	static public final int _minimumHeight = 480;

	/**
	 * 
	 */
	private String _message = "";

	/**
	 * 
	 */
	private String _forcedExecutionMessage = "";

	/**
	 * 
	 */
	private WarningList _warningList = null;

	/**
	 * 
	 */
	private JButton _copyToClipboardButton = null;

	/**
	 * 
	 */
	public boolean _forcedExecution = false;

	/**
	 * 
	 */
	private Component _parent = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param message
	 * @param forced_execution_message
	 * @param parent
	 */
	public WarningDlg3(Frame arg0, String arg1, String message, String forced_execution_message, Component parent) {
		super(arg0, arg1, true);
		_message = message;
		_forcedExecutionMessage = forced_execution_message;
		_parent = parent;
	}

	/**
	 * @return
	 */
	private Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._warningDialogRectangleKey3 + "x",
			String.valueOf( SwingTool.get_default_window_position(
				( null != _parent) ? _parent : getOwner(),
				_minimumWidth, _minimumHeight).x));
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._warningDialogRectangleKey3 + "y",
			String.valueOf( SwingTool.get_default_window_position(
				( null != _parent) ? _parent : getOwner(),
				_minimumWidth, _minimumHeight).y));
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._warningDialogRectangleKey3 + "width",
			String.valueOf( _minimumWidth));
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._warningDialogRectangleKey3 + "height",
			String.valueOf( _minimumHeight));
		if ( null == value)
			return null;

		int height = Integer.parseInt( value);

		return new Rectangle( x, y, width, height);
	}

	/**
	 * 
	 */
	private void optimize_window_rectangle() {
		Rectangle rectangle = getBounds();
		if ( !GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersects( rectangle)
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( rectangle).width <= 10
			|| rectangle.y <= -getInsets().top
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( rectangle).height <= getInsets().top) {
			setSize( _minimumWidth, _minimumHeight);
			setLocationRelativeTo( ( null != _parent) ? _parent : getOwner());
		}
	}

	/**
	 * 
	 */
	protected void set_property_to_environment_file() {
		Rectangle rectangle = getBounds();

		Environment.get_instance().set(
			Environment._warningDialogRectangleKey3 + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._warningDialogRectangleKey3 + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._warningDialogRectangleKey3 + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._warningDialogRectangleKey3 + "height", String.valueOf( rectangle.height));
	}

	/**
	 * 
	 */
	public void do_modal() {
		Rectangle rectangle = get_rectangle_from_environment_file();
		if ( null == rectangle)
			do_modal( getOwner(), _minimumWidth, _minimumHeight);
		else
			do_modal( rectangle);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;



		getContentPane().setLayout( new BorderLayout());



		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		setup_message_label( northPanel);

		getContentPane().add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		if ( !setup_warningList( centerPanel))
			return false;

		getContentPane().add( centerPanel);



		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( southPanel);

		setup_copy_to_clipboard_button( southPanel);

		insert_horizontal_glue( southPanel);

		setup_forced_execution_button( southPanel);

		insert_horizontal_glue( southPanel);

		setup_close_button( southPanel);

		insert_horizontal_glue( southPanel);

		getContentPane().add( southPanel, "South");



		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		return true;
	}

	/**
	 * @param parent
	 * 
	 */
	private void setup_message_label(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JLabel label = new JLabel( _message);
		panel.add( label);

		parent.add( panel);
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_warningList(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_warningList = new WarningList( ( Frame)getOwner(), this);
		if ( !_warningList.setup())
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _warningList);

		panel.add( scrollPane);
		panel.add( Box.createHorizontalStrut( 5));
		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 * 
	 */
	private void setup_copy_to_clipboard_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridLayout( 1, 1));

		_copyToClipboardButton = new JButton(
			ResourceManager.get_instance().get( "warning.dialog3.copy.to.clipboard.button.name"));
		_copyToClipboardButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WarningManager.get_instance().copy_to_clipboard();
			}
		});

		buttonPanel.add( _copyToClipboardButton);
		panel.add( buttonPanel);
		panel.add( Box.createHorizontalStrut( 5));
		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_forced_execution_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridLayout( 1, 1));

		JButton button = new JButton(
			ResourceManager.get_instance().get( "warning.dialog3.forced.execution.button.name"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_forcedExecution = true;
				set_property_to_environment_file();
				dispose();
			}
		});

		buttonPanel.add( button);
		panel.add( buttonPanel);
		panel.add( Box.createHorizontalStrut( 5));
		parent.add( panel);
	}

	/**
	 * @param parent
	 * 
	 */
	private void setup_close_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridLayout( 1, 1));

		JButton button = new JButton(
			ResourceManager.get_instance().get( "warning.dialog3.close.button.name"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				set_property_to_environment_file();
				dispose();
			}
		});

		buttonPanel.add( button);
		panel.add( buttonPanel);
		panel.add( Box.createHorizontalStrut( 5));
		parent.add( panel);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		optimize_window_rectangle();

		if ( 0 == _warningList.getModel().getSize())
			_copyToClipboardButton.setEnabled( false);

		_warningList.requestFocusInWindow();

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				int width = getSize().width;
				int height = getSize().height;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width,
					( _minimumHeight > height) ? _minimumHeight : height);
			}
		});

		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				set_property_to_environment_file();
			}
		});
	}
}
