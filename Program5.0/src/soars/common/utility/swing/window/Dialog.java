/*
 * 2004/10/08
 */
package soars.common.utility.swing.window;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 */
public class Dialog extends JDialog {

	/**
	 * 
	 */
	private boolean _result = false;

	/**
	 * @throws java.awt.HeadlessException
	 */
	public Dialog() throws HeadlessException {
		super();
	}

	/**
	 * @param arg0
	 * @throws java.awt.HeadlessException
	 */
	public Dialog(java.awt.Dialog arg0) throws HeadlessException {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.awt.HeadlessException
	 */
	public Dialog(java.awt.Dialog arg0, boolean arg1)
		throws HeadlessException {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @throws java.awt.HeadlessException
	 */
	public Dialog(Frame arg0) throws HeadlessException {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.awt.HeadlessException
	 */
	public Dialog(Frame arg0, boolean arg1) throws HeadlessException {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.awt.HeadlessException
	 */
	public Dialog(java.awt.Dialog arg0, String arg1) throws HeadlessException {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws java.awt.HeadlessException
	 */
	public Dialog(java.awt.Dialog arg0, String arg1, boolean arg2)
		throws HeadlessException {
		super(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.awt.HeadlessException
	 */
	public Dialog(Frame arg0, String arg1) throws HeadlessException {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws java.awt.HeadlessException
	 */
	public Dialog(Frame arg0, String arg1, boolean arg2)
		throws HeadlessException {
		super(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @throws java.awt.HeadlessException
	 */
	public Dialog(
		java.awt.Dialog arg0,
		String arg1,
		boolean arg2,
		GraphicsConfiguration arg3)
		throws HeadlessException {
		super(arg0, arg1, arg2, arg3);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public Dialog(
		Frame arg0,
		String arg1,
		boolean arg2,
		GraphicsConfiguration arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	/**
	 * @param component
	 * @return
	 */
	public boolean do_modal(Component component) {
		if ( !create())
			return false;

		pack();
		setLocationRelativeTo( component);
		on_setup_completed();
		setVisible( true);

		return _result;
	}

	/**
	 * @param position
	 * @return
	 */
	public boolean do_modal(Point position) {
		return do_modal( position.x, position.y);
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean do_modal(int x, int y) {
		if ( !create())
			return false;

		pack();
		setLocation( x, y);
		on_setup_completed();
		setVisible( true);

		return _result;
	}

	/**
	 * @param component
	 * @param dimension
	 * @return
	 */
	public boolean do_modal(Component component, Dimension dimension) {
		return do_modal( component, dimension.width, dimension.height);
	}

	/**
	 * @param component
	 * @param width
	 * @param height
	 * @return
	 */
	public boolean do_modal(Component component, int width, int height) {
		if ( !create())
			return false;

		pack();
		setSize( width, height);
		setLocationRelativeTo( component);
		on_setup_completed();
		setVisible( true);

		return _result;
	}

	/**
	 * @param rectangle
	 * @return
	 */
	public boolean do_modal(Rectangle rectangle) {
		return do_modal( rectangle.x, rectangle.y, rectangle.width, rectangle.height);
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public boolean do_modal(int x, int y, int width, int height) {
		if ( !create())
			return false;

		pack();
		setBounds( x, y, width, height);
		on_setup_completed();
		setVisible( true);

		return _result;
	}

	/**
	 * @param component
	 * @return
	 */
	public boolean create(Component component) {
		if ( !create())
			return false;

		pack();
		setLocationRelativeTo( component);
		on_setup_completed();
		setVisible( true);

		return true;
	}

	/**
	 * @param position
	 * @return
	 */
	public boolean create(Point position) {
		return create( position.x, position.y);
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean create(int x, int y) {
		if ( !create())
			return false;

		pack();
		setLocation( x, y);
		on_setup_completed();
		setVisible( true);

		return true;
	}

	/**
	 * @param component
	 * @param dimension
	 * @return
	 */
	public boolean create(Component component, Dimension dimension) {
		return create( component, dimension.width, dimension.height);
	}

	/**
	 * @param component
	 * @param width
	 * @param height
	 * @return
	 */
	public boolean create(Component component, int width, int height) {
		if ( !create())
			return false;

		pack();
		setSize( width, height);
		setLocationRelativeTo( component);
		on_setup_completed();
		setVisible( true);

		return true;
	}

	/**
	 * @param rectangle
	 * @return
	 */
	public boolean create(Rectangle rectangle) {
		return create( rectangle.x, rectangle.y, rectangle.width, rectangle.height);
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public boolean create(int x, int y, int width, int height) {
		if ( !create())
			return false;

		pack();
		setBounds( x, y, width, height);
		on_setup_completed();
		setVisible( true);

		return true;
	}

	/**
	 * @return
	 */
	public boolean create() {
		if ( !on_init_dialog())
			return false;

		return true;
	}

	/**
	 * @return
	 */
	protected boolean on_init_dialog() {
		addWindowListener( new WindowListener() {
			public void windowActivated(WindowEvent e) {
			}
			public void windowClosed(WindowEvent e) {
			}
			public void windowClosing(WindowEvent e) {
				on_cancel( null);
			}
			public void windowDeactivated(WindowEvent e) {
			}
			public void windowDeiconified(WindowEvent e) {
			}
			public void windowIconified(WindowEvent e) {
			}
			public void windowOpened(WindowEvent e) {
			}
		});
		return true;
	}

	/**
	 * 
	 */
	protected void on_setup_completed() {
	}

	/**
	 * @param actionEvent
	 */
	protected void on_ok(ActionEvent actionEvent) {
		_result = true;
		dispose();
	}

	/**
	 * @param actionEvent
	 */
	protected void on_cancel(ActionEvent actionEvent) {
		_result = false;
		dispose();
	}

	/**
	 * @param actionEvent
	 */
	protected void on_apply(ActionEvent actionEvent) {
	}

	/**
	 * @param component
	 */
	public void link_to_ok(JComponent component) {
		AbstractAction okAction = new AbstractAction() {
			public void actionPerformed(ActionEvent actionEvent){
				on_ok( actionEvent);
		  }
		};
		component.getInputMap().put(
			KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0), "ok");
		component.getActionMap().put( "ok", okAction);
	}

	/**
	 * @param component
	 */
	public void link_to_cancel(JComponent component) {
		AbstractAction cancelAction = new AbstractAction() {
			public void actionPerformed(ActionEvent actionEvent){
				on_cancel( actionEvent);
		  }
		};
		component.getInputMap().put(
			KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0), "cancel");
		component.getActionMap().put( "cancel", cancelAction);
	}

	/**
	 * 
	 */
	public void insert_horizontal_glue() {
		SwingTool.insert_horizontal_glue( getContentPane());
	}

	/**
	 * @param container
	 */
	public void insert_horizontal_glue(Container container) {
		SwingTool.insert_horizontal_glue( container);
	}

	/**
	 * @param ok
	 * @param cancel
	 * @param enter
	 * @param escape
	 */
	protected void setup_ok_and_cancel_button(String ok, String cancel, boolean enter, boolean escape) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));
		setup_ok_and_cancel_button( panel, ok, cancel, enter, escape);
		getContentPane().add( panel);
	}

	/**
	 * @param panel
	 * @param ok
	 * @param cancel
	 * @param enter
	 * @param escape
	 */
	protected void setup_ok_and_cancel_button(JPanel panel, String ok, String cancel, boolean enter, boolean escape) {
		JButton okButton = new JButton( ok);

		if ( enter)
			getRootPane().setDefaultButton( okButton);

		okButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_ok( arg0);
			}
		});

		if ( escape)
			link_to_cancel( okButton);

		panel.add( okButton);


		JButton cancelButton = new JButton( cancel);
		cancelButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_cancel( arg0);
			}
		});

		if ( escape)
			link_to_cancel( cancelButton);

		panel.add( cancelButton);


		int width = Math.max( okButton.getPreferredSize().width, cancelButton.getPreferredSize().width);

		okButton.setPreferredSize( new Dimension( width, okButton.getPreferredSize().height));
		cancelButton.setPreferredSize( new Dimension( width, cancelButton.getPreferredSize().height));
	}

	/**
	 * @param ok
	 * @param cancel
	 * @param apply
	 * @param enter
	 * @param escape
	 */
	protected void setup_ok_and_cancel_and_apply_button(String ok, String cancel, String apply, boolean enter, boolean escape) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));
		setup_ok_and_cancel_and_apply_button( panel, ok, cancel, apply, enter, escape);
		getContentPane().add( panel);
	}

	/**
	 * @param panel
	 * @param ok
	 * @param cancel
	 * @param apply
	 * @param enter
	 * @param escape
	 */
	protected void setup_ok_and_cancel_and_apply_button(JPanel panel, String ok, String cancel, String apply, boolean enter, boolean escape) {
		JButton okButton = new JButton( ok);

		if ( enter)
			getRootPane().setDefaultButton( okButton);

		okButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_ok( arg0);
			}
		});

		if ( escape)
			link_to_cancel( okButton);

		panel.add( okButton);


		JButton cancelButton = new JButton( cancel);
		cancelButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_cancel( arg0);
			}
		});

		if ( escape)
			link_to_cancel( cancelButton);

		panel.add( cancelButton);


		JButton applyButton = new JButton( apply);
		applyButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_apply( arg0);
			}
		});

		if ( escape)
			link_to_cancel( applyButton);

		panel.add( applyButton);


		int width = okButton.getPreferredSize().width;
		width = Math.max( width, cancelButton.getPreferredSize().width);
		width = Math.max( width, applyButton.getPreferredSize().width);

		okButton.setPreferredSize( new Dimension( width, okButton.getPreferredSize().height));
		cancelButton.setPreferredSize( new Dimension( width, cancelButton.getPreferredSize().height));
		applyButton.setPreferredSize( new Dimension( width, applyButton.getPreferredSize().height));
	}

	/**
	 * @param panel
	 * @param ok
	 * @param enter
	 * @param escape
	 */
	protected void setup_ok_button(JPanel panel, String ok, boolean enter, boolean escape) {
		JButton okButton = new JButton( ok);

		if ( enter)
			getRootPane().setDefaultButton( okButton);

		okButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_ok( arg0);
			}
		});

		if ( escape)
			link_to_cancel( okButton);

		panel.add( okButton);
	}

	/**
	 * @param panel
	 * @param cancel
	 * @param escape
	 */
	protected void setup_cancel_button(JPanel panel, String cancel, boolean escape) {
		JButton cancelButton = new JButton( cancel);
		cancelButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_cancel( arg0);
			}
		});

		if ( escape)
			link_to_cancel( cancelButton);

		panel.add( cancelButton);
	}
}
