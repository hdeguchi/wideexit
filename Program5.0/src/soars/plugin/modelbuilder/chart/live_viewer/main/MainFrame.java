/*
 * 2004/10/01
 */
package soars.plugin.modelbuilder.chart.live_viewer.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;

import soars.plugin.modelbuilder.chart.live_viewer.menu.StartAction;
import soars.plugin.modelbuilder.chart.live_viewer.menu.WriteAction;
import soars.plugin.modelbuilder.chart.live_viewer.menu.ExitAction;
import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.window.Frame;

/**
 * @author kurata
 */
public class MainFrame extends Frame {


	/**
	 * 
	 */
	public UserInterface _userInterface = null;

	/**
	 * 
	 */
	private JScrollPane _scrollPane = null;

	/**
	 * 
	 */
	private LiveViewerView _liveViewerView = null;

	/**
	 * @throws java.awt.HeadlessException
	 */
	public MainFrame() throws HeadlessException {
		super();
	}

	/**
	 * @param arg0
	 */
	public MainFrame(GraphicsConfiguration arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @throws java.awt.HeadlessException
	 */
	public MainFrame(String arg0) throws HeadlessException {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public MainFrame(String arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
	}

	/**
	 * 
	 */
	private void setup_menu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu menu = new JMenu( "File", true);
		menuBar.add( menu);

		menu.addSeparator();

		_userInterface.append_menuitem( menu, "Exit", new ExitAction( "Exit", this));

		menu = new JMenu( "Chart", false);
		menuBar.add( menu);

		_userInterface.append_menuitem( menu, "Start", new StartAction( "Start", _liveViewerView));

		_userInterface.append_menuitem( menu, "Write", new WriteAction( "Write", _liveViewerView));

		setJMenuBar( menuBar);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		setBackground( new Color( 255, 255, 255));

		_userInterface = new UserInterface();

		_liveViewerView = new LiveViewerView( true, this);
		if ( !_liveViewerView.create())
			return false;

		_scrollPane = new JScrollPane();
		_scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		_scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		_scrollPane.setViewportView( _liveViewerView);
		_scrollPane.setBackground( new Color( 255, 255, 255));
		getContentPane().setLayout( new BorderLayout());
		getContentPane().add( _scrollPane);

		setup_menu();

		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);

		pack();

		int width = 400;
		int height = 300;
//		setSize( width, height);

		// Get screen size
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		// show at the center of display
		setLocation( ( int)( dim.getWidth() - width) / 2 , ( int)( dim.getHeight() - height) / 2 );

		setSize( width, height);

		Toolkit.getDefaultToolkit().setDynamicLayout( true);

		setVisible( true);

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_window_closing(java.awt.event.WindowEvent)
	 */
	protected void on_window_closing(WindowEvent windowEvent) {
		_liveViewerView.cleanup();
	}

	/**
	 * @param actionEvent
	 */
	public void on_exit(ActionEvent actionEvent) {
		_liveViewerView.cleanup();
		System.exit( 0);
	}
}
