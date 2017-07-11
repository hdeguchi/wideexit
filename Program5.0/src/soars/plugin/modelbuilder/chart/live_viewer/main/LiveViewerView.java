/*
 * 2004/10/01
 */
package soars.plugin.modelbuilder.chart.live_viewer.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.util.Vector;

import soars.common.utility.swing.window.View;

/**
 * @author kurata
 */
public class LiveViewerView extends View {


	/**
	 * 
	 */
	private MainFrame _mainFrame = null;


	/**
	 * 
	 */
	private final Color _background_color = new Color( 255, 255, 255);


	/**
	 * 
	 */
	private Vector _chartOwnerVector = new Vector();

	/**
	 * 
	 */
	public LiveViewerView(MainFrame mainFrame) {
		super();
		_mainFrame = mainFrame;
		setBackground( _background_color);
	}

	/**
	 * @param arg0
	 */
	public LiveViewerView(boolean arg0, MainFrame mainFrame) {
		super(arg0);
		_mainFrame = mainFrame;
		setBackground( _background_color);
	}

	/**
	 * @param arg0
	 */
	public LiveViewerView(LayoutManager arg0, MainFrame mainFrame) {
		super(arg0);
		_mainFrame = mainFrame;
		setBackground( _background_color);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public LiveViewerView(LayoutManager arg0, boolean arg1, MainFrame mainFrame) {
		super(arg0, arg1);
		_mainFrame = mainFrame;
		setBackground( _background_color);
	}

    /**
     * 
     */
    public void cleanup() {
 		for ( int i = 0; i < _chartOwnerVector.size(); ++i) {
			ChartOwner chartOwner = ( ChartOwner)_chartOwnerVector.get( i);
			chartOwner.cleanup();
		}
    }

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		setPreferredSize( new Dimension( 150, 150));

		//start_timer();

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_component_resized(java.awt.event.ComponentEvent)
	 */
	protected void on_component_resized(ComponentEvent componentEvent) {
		Graphics graphics = getGraphics();
		if ( null == graphics) {
			System.out.println( "on_component_resized : getGraphics() returned null!");
			return;
		}
	}

	/* (Non Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
	}

	/**
	 * @param actionEvent
	 */
	public void on_start(ActionEvent actionEvent) {
		ChartOwner chartOwner = new ChartOwner();
		chartOwner.start();
		_chartOwnerVector.add( chartOwner);
	}

	/**
	 * @param actionEvent
	 */
	public void on_write(ActionEvent actionEvent) {
		if ( _chartOwnerVector.isEmpty())
			return;

		ChartOwner chartOwner = ( ChartOwner)_chartOwnerVector.get( 0);
		chartOwner.write();
	}
}
