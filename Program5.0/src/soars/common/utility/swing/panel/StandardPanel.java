/*
 * 2005/03/07
 */
package soars.common.utility.swing.panel;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 */
public class StandardPanel extends JPanel {

	/**
	 * 
	 */
	public StandardPanel() {
		super();
	}

	/**
	 * @param arg0
	 */
	public StandardPanel(boolean arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public StandardPanel(LayoutManager arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public StandardPanel(LayoutManager arg0, boolean arg1) {
		super(arg0, arg1);
	}

	/**
	 * @return
	 */
	public boolean create() {
		if ( !on_create())
			return false;

		return true;
	}

	/**
	 * @return
	 */
	protected boolean on_create() {
		return true;
	}

	/**
	 * @param actionEvent
	 */
	protected void on_ok(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	protected void on_cancel(ActionEvent actionEvent) {
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
	protected void insert_horizontal_glue() {
		insert_vertical_strut( this);
	}

	/**
	 * @param container
	 */
	protected void insert_horizontal_glue(Container container) {
		SwingTool.insert_horizontal_glue( container);
	}

	/**
	 * 
	 */
	protected void insert_vertical_strut() {
		insert_vertical_strut( this);
	}

	/**
	 * @param container
	 */
	protected void insert_vertical_strut(Container container) {
		SwingTool.insert_vertical_strut( container, 5);
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
		add( panel);
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
			link_to_ok( okButton);

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

		Dimension okButtonDimension = okButton.getPreferredSize();
		Dimension cancelButtonDimension = cancelButton.getPreferredSize();

		okButton.setPreferredSize( new Dimension( Math.max( okButtonDimension.width, cancelButtonDimension.width),
			okButtonDimension.height));
		cancelButton.setPreferredSize( new Dimension( Math.max( okButtonDimension.width, cancelButtonDimension.width),
			cancelButtonDimension.height));
	}

	/**
	 * @param ok
	 * @param enter
	 * @param escape
	 */
	protected void setup_ok_button(String ok, boolean enter, boolean escape) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));
		setup_ok_button( panel, ok, enter, escape);
		add( panel);
	}

	/**
	 * @param ok
	 * @param enter
	 * @param escape
	 * @param width
	 */
	protected void setup_ok_button(String ok, boolean enter, boolean escape, int width) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));
		setup_ok_button( panel, ok, enter, escape, width);
		add( panel);
	}

	/**
	 * @param panel
	 * @param ok
	 * @param enter
	 * @param escape
	 */
	protected void setup_ok_button(JPanel panel, String ok, boolean enter, boolean escape) {
		setup_ok_button( panel, ok, enter, escape, -1);
	}

	/**
	 * @param panel
	 * @param ok
	 * @param enter
	 * @param escape
	 * @param width
	 */
	protected void setup_ok_button(JPanel panel, String ok, boolean enter, boolean escape, int width) {
		JButton okButton = new JButton( ok);

		if ( enter)
			link_to_ok( okButton);

		okButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_ok( arg0);
			}
		});

		if ( escape)
			link_to_cancel( okButton);

		if ( 0 < width)
			okButton.setPreferredSize( new Dimension( width, okButton.getPreferredSize().height));

		panel.add( okButton);
	}
}
