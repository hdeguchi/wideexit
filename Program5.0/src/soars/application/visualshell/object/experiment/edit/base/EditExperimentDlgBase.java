/*
 * 2005/07/12
 */
package soars.application.visualshell.object.experiment.edit.base;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.experiment.ExperimentManager;
import soars.application.visualshell.object.experiment.edit.table.ExperimentRowHeaderTable;
import soars.application.visualshell.object.experiment.edit.table.ExperimentTable;
import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.swing.spinner.NumberSpinner;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Dialog;

/**
 * The base dialog box class to edit the experiment conditions.
 * @author kurata / SOARS project
 */
public class EditExperimentDlgBase extends Dialog implements IMessageCallback {

	/**
	 * Default width.
	 */
	static public final int _minimumWidth = 800;

	/**
	 * Default height.
	 */
	static public final int _minimumHeight = 480;

	/**
	 * 
	 */
	protected ExperimentManager _experimentManager = null;

	/**
	 * 
	 */
	protected ExperimentTable _experimentTable = null;

	/**
	 * 
	 */
	protected ExperimentRowHeaderTable _experimentRowHeaderTable = null;

	/**
	 * 
	 */
	private JButton _exportButton = null;

	/**
	 * 
	 */
	private JButton _exportTableButton = null;

	/**
	 * 
	 */
	private JButton _importTableButton = null;

	/**
	 * 
	 */
	protected NumberSpinner _numberOfTimesNumberSpinner = null;

	/**
	 * 
	 */
	protected JCheckBox _toLogFileCheckBox = null;

	/**
	 * 
	 */
	private Component _parent = null;

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog.
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param experimentManager the experiment support manager
	 */
	public EditExperimentDlgBase(Frame arg0, String arg1, boolean arg2, ExperimentManager experimentManager) {
		super(arg0, arg1, arg2);
		_experimentManager = experimentManager;
	}

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog.
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param parent the parent container of this component
	 * @param experimentManager the experiment support manager
	 */
	public EditExperimentDlgBase(Frame arg0, String arg1, boolean arg2, Component parent, ExperimentManager experimentManager) {
		super(arg0, arg1, arg2);
		_parent = parent;
		_experimentManager = experimentManager;
	}

	/**
	 * @return
	 */
	private Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._editExperimentDialogRectangleKey + "x",
			String.valueOf( SwingTool.get_default_window_position(
				( null != _parent) ? _parent : getOwner(),
				_minimumWidth, _minimumHeight).x));
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editExperimentDialogRectangleKey + "y",
			String.valueOf( SwingTool.get_default_window_position(
				( null != _parent) ? _parent : getOwner(),
				_minimumWidth, _minimumHeight).y));
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editExperimentDialogRectangleKey + "width",
			String.valueOf( _minimumWidth));
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editExperimentDialogRectangleKey + "height",
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
			Environment._editExperimentDialogRectangleKey + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._editExperimentDialogRectangleKey + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._editExperimentDialogRectangleKey + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._editExperimentDialogRectangleKey + "height", String.valueOf( rectangle.height));
	}

	/**
	 * Runs this dialog box and returns the result.
	 * @return the result
	 */
	public boolean do_modal() {
		Rectangle rectangle = get_rectangle_from_environment_file();
		if ( null == rectangle)
			return do_modal( getOwner(), _minimumWidth, _minimumHeight);
		else
			return do_modal( rectangle);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		getContentPane().setLayout( new BorderLayout());


		create_export_button();
		create_export_table_button();
		create_import_table_button();
		create_experiment_table();


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		getContentPane().add( northPanel, "North");


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));


		if ( !setup_experiment_table( centerPanel))
			return false;

		getContentPane().add( centerPanel);


		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( southPanel);

		on_setup_south_panel( southPanel);

		insert_horizontal_glue( southPanel);

		getContentPane().add( southPanel, "South");


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		return true;
	}

	/**
	 * 
	 */
	private void create_export_button() {
		_exportButton = new JButton( ResourceManager.get_instance().get( "edit.experiment.dialog.export.button.name"));
	}

	/**
	 * 
	 */
	private void create_export_table_button() {
		_exportTableButton = new JButton( ResourceManager.get_instance().get( "edit.experiment.dialog.export.table.button.name"));
	}

	/**
	 * 
	 */
	private void create_import_table_button() {
		_importTableButton = new JButton( ResourceManager.get_instance().get( "edit.experiment.dialog.import.table.button.name"));
	}

	/**
	 * 
	 */
	private void create_experiment_table() {
		_experimentTable = new ExperimentTable( _exportButton, _experimentManager, ( Frame)getOwner(), this);
		_experimentRowHeaderTable = new ExperimentRowHeaderTable( ( Frame)getOwner(), this);
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_experiment_table(JPanel parent) {
		if ( !_experimentTable.setup( _experimentRowHeaderTable))
				return false;

		if ( !_experimentRowHeaderTable.setup( _experimentManager, _experimentTable, ( Graphics2D)getParent().getGraphics()))
			return false;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _experimentTable);
		scrollPane.getViewport().setOpaque( true);
		scrollPane.getViewport().setBackground( SystemColor.text);

		scrollPane.setRowHeaderView( _experimentRowHeaderTable);
		scrollPane.getRowHeader().setOpaque( true);
		scrollPane.getRowHeader().setBackground( SystemColor.text);

		Dimension dimension = scrollPane.getRowHeader().getPreferredSize();
		scrollPane.getRowHeader().setPreferredSize( new Dimension( _experimentRowHeaderTable.getColumnModel().getColumn( 0).getWidth(), dimension.height));
		SwingTool.set_table_left_top_corner_column( scrollPane);

		// スクロール時に２つのTableが同期するように以下のhandlerが必要
		scrollPane.getRowHeader().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JViewport viewport = ( JViewport)e.getSource();
				scrollPane.getVerticalScrollBar().setValue( viewport.getViewPosition().y);
			}
		});

		scrollPane.addMouseListener( new MouseInputAdapter() {
			public void mouseReleased(MouseEvent arg0) {
				if ( !SwingTool.is_mouse_right_button( arg0))
					return;

				_experimentRowHeaderTable.on_mouse_right_up( new Point( arg0.getX(), arg0.getY() - _experimentTable.getTableHeader().getHeight()));
			}
		});

		panel.add( scrollPane);
		panel.add( Box.createHorizontalStrut( 5));
		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 */
	protected void setup_append_experiment_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridLayout( 1, 1));

		JButton button = new JButton(
			ResourceManager.get_instance().get( "edit.experiment.dialog.append.experiment.button.name"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_experimentRowHeaderTable.on_append_row( arg0);
			}
		});

		buttonPanel.add( button);
		panel.add( buttonPanel);
		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	protected void setup_remove_all_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridLayout( 1, 1));

		JButton button = new JButton(
			ResourceManager.get_instance().get( "edit.experiment.dialog.clear.all.button.name"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_experimentRowHeaderTable.remove_all();
			}
		});

		buttonPanel.add( button);
		panel.add( buttonPanel);
		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	protected void setup_export_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridLayout( 1, 1));

		_exportButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_export( arg0);
			}
		});

		buttonPanel.add( _exportButton);
		panel.add( buttonPanel);
		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_export(ActionEvent actionEvent) {
		File file = CommonTool.get_save_file(
			Environment._exportDirectoryKey,
			ResourceManager.get_instance().get( "file.export.dialog"),
			new String[] { "sor", "txt"},
			"SOARS script data",
			this);

		if ( null == file)
			return;

		MessageDlg.execute( ( Frame)getOwner(), ResourceManager.get_instance().get( "application.title"), true,
			"on_export", ResourceManager.get_instance().get( "file.export.show.message"),
			new Object[] { file}, this, this);
	}

	/**
	 * @param parent
	 */
	protected void setup_import_table_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridLayout( 1, 1));

		_importTableButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_experimentTable.import_table();
			}
		});

		buttonPanel.add( _importTableButton);
		panel.add( buttonPanel);
		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	protected void setup_export_table_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridLayout( 1, 1));

		_exportTableButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_experimentTable.export_table();
			}
		});

		buttonPanel.add( _exportTableButton);
		panel.add( buttonPanel);
		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	protected void setup_number_of_times_numberSpinner(JPanel parent) {
		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.experiment.dialog.number.of.times"));
		parent.add( label);

		_numberOfTimesNumberSpinner = new NumberSpinner();
		_numberOfTimesNumberSpinner.set_minimum( 1);
		_numberOfTimesNumberSpinner.set_maximum( Constant._maxNumberOfTimes);
		_numberOfTimesNumberSpinner.set_value( _experimentManager._numberOfTimes);
		_numberOfTimesNumberSpinner.setPreferredSize( new Dimension( 100,
			_numberOfTimesNumberSpinner.getPreferredSize().height));
		parent.add( _numberOfTimesNumberSpinner);

		parent.add( Box.createHorizontalStrut( 5));
	}

	/**
	 * @param selected
	 * @param parent
	 */
	protected void setup_to_log_file_checkBox(boolean selected, JPanel parent) {
		_toLogFileCheckBox = new JCheckBox(
			ResourceManager.get_instance().get( "edit.export.setting.dialog.check.box.to.file.name"));
		_toLogFileCheckBox.setSelected( selected);
		parent.add( _toLogFileCheckBox);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IMessageCallback#message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.MessageDlg)
	 */
	public boolean message_callback(String id, Object[] objects, MessageDlg messageDlg) {
		if ( id.equals( "on_export"))
			return _experimentTable.export( ( File)objects[ 0]);

		return true;
	}

	/**
	 * @param parent
	 */
	protected void on_setup_south_panel(JPanel parent) {
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		optimize_window_rectangle();

		_exportButton.setEnabled( _experimentTable.can_export());
		_experimentTable.requestFocusInWindow();

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
				_experimentTable.get_column_widths();
				set_property_to_environment_file();
			}
		});
	}

	/**
	 * @return
	 */
	protected boolean get() {
		if ( !_experimentTable.on_ok())
			return false;

		_experimentTable.get_column_widths();

		return true;
	}

	/**
	 * 
	 */
	protected void save() {
		_experimentTable.save();
		_experimentTable.get_column_widths();
	}
}
