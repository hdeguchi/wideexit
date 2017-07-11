/*
 * 2005/06/21
 */
package soars.application.visualshell.model;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.comment.edit.CommentPropertyPage;
import soars.application.visualshell.object.expression.edit.ExpressionPropertyPage;
import soars.application.visualshell.object.log.edit.LogPropertyPage1;
import soars.application.visualshell.object.log.edit.LogPropertyPage2;
import soars.application.visualshell.object.simulation.edit.SimulationPropertyPage;
import soars.application.visualshell.object.stage.edit.StagePropertyPage;
import soars.application.visualshell.observer.Observer;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 */
public class EditModelDlg extends Dialog {

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
	private JTabbedPane _tabbedPane = null;

	/**
	 * 
	 */
	private StagePropertyPage _stagePropertyPage = null;

	/**
	 * 
	 */
	private SimulationPropertyPage _simulationPropertyPage = null;

	/**
	 * 
	 */
	private ExpressionPropertyPage _expressionPropertyPage = null;

	/**
	 * 
	 */
	private LogPropertyPage1 _logPropertyPage1 = null;

	/**
	 * 
	 */
	private LogPropertyPage2 _logPropertyPage2 = null;

//	/**
//	 * 
//	 */
//	private OtherScriptsPropertyPage _otherScriptsPropertyPage = null;

	/**
	 * 
	 */
	private CommentPropertyPage _commentPropertyPage = null;

	/**
	 * 
	 */
	private int _selectedIndex = 0;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param selectedIndex
	 */
	public EditModelDlg(Frame arg0, String arg1, boolean arg2, int selectedIndex) {
		super(arg0, arg1, arg2);
		_selectedIndex = selectedIndex;
	}

	/**
	 * @return
	 */
	private Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._editSettingDialogRectangleKey + "x",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimumWidth, _minimumHeight).x));
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editSettingDialogRectangleKey + "y",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimumWidth, _minimumHeight).y));
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editSettingDialogRectangleKey + "width",
			String.valueOf( _minimumWidth));
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editSettingDialogRectangleKey + "height",
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
			setLocationRelativeTo( getOwner());
		}
	}

	/**
	 * 
	 */
	protected void set_property_to_environment_file() {
		Rectangle rectangle = getBounds();

		Environment.get_instance().set(
			Environment._editSettingDialogRectangleKey + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._editSettingDialogRectangleKey + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._editSettingDialogRectangleKey + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._editSettingDialogRectangleKey + "height", String.valueOf( rectangle.height));
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

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;



		getContentPane().setLayout( new BorderLayout());



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		if ( !setup_tabbed_pane( centerPanel))
			return false;

		getContentPane().add( centerPanel);



		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		insert_horizontal_glue( southPanel);

		setup_ok_and_cancel_button(
			panel,
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			false, false);
		southPanel.add( panel);

		insert_horizontal_glue( southPanel);

		getContentPane().add( southPanel, "South");



		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_tabbed_pane(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_tabbedPane = new JTabbedPane( SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

		_stagePropertyPage = new StagePropertyPage( ( Frame)getOwner(), this);
		if ( !_stagePropertyPage.create())
			return false;

		_tabbedPane.add( _stagePropertyPage, _stagePropertyPage._title);

		_simulationPropertyPage = new SimulationPropertyPage( ( Frame)getOwner(), this);
		if ( !_simulationPropertyPage.create())
			return false;

		_tabbedPane.add( _simulationPropertyPage, _simulationPropertyPage._title);

		_expressionPropertyPage = new ExpressionPropertyPage( ( Frame)getOwner(), this);
		if ( !_expressionPropertyPage.create())
			return false;

		_tabbedPane.add( _expressionPropertyPage, _expressionPropertyPage._title);

		_logPropertyPage1 = new LogPropertyPage1( ( Frame)getOwner(), this);
		if ( !_logPropertyPage1.create())
			return false;

		_tabbedPane.add( _logPropertyPage1, _logPropertyPage1._title);

		_logPropertyPage2 = new LogPropertyPage2( ( Frame)getOwner(), this);
		if ( !_logPropertyPage2.create())
			return false;

		_tabbedPane.add( _logPropertyPage2, _logPropertyPage2._title);

//		_otherScriptsPropertyPage = new OtherScriptsPropertyPage( ( Frame)getOwner(), this);
//		if ( !_otherScriptsPropertyPage.create())
//			return false;
//
//		_tabbedPane.add( _otherScriptsPropertyPage, _otherScriptsPropertyPage._title);

		_commentPropertyPage = new CommentPropertyPage( ( Frame)getOwner(), this);
		if ( !_commentPropertyPage.create())
			return false;

		_tabbedPane.add( _commentPropertyPage, _commentPropertyPage._title);

		panel.add( _tabbedPane);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		optimize_window_rectangle();

		_tabbedPane.setSelectedIndex( _selectedIndex);
		_stagePropertyPage.on_setup_completed();
		_simulationPropertyPage.on_setup_completed();
		_expressionPropertyPage.on_setup_completed();
		_logPropertyPage1.on_setup_completed();
		_logPropertyPage2.on_setup_completed();
//		_otherScriptsPropertyPage.on_setup_completed();
		_commentPropertyPage.on_setup_completed();

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

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		if ( !_stagePropertyPage.on_ok())
			return;

		if ( !_simulationPropertyPage.on_ok())
			return;

		if ( !_expressionPropertyPage.on_ok())
			return;

		if ( !_logPropertyPage1.on_ok())
			return;

		if ( !_logPropertyPage2.on_ok())
			return;

//		if ( !_otherScriptsPropertyPage.on_ok())
//			return;

		if ( !_commentPropertyPage.on_ok())
			return;

		Observer.get_instance().on_update_model();

		Observer.get_instance().modified();

		set_property_to_environment_file();

		super.on_ok(actionEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_cancel(java.awt.event.ActionEvent)
	 */
	protected void on_cancel(ActionEvent actionEvent) {
		set_property_to_environment_file();
		super.on_cancel(actionEvent);
	}
}
