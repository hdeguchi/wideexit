/*
 * Created on 2006/03/10
 */
package soars.application.visualshell.object.chart.edit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.chart.ChartObject;
import soars.application.visualshell.observer.Observer;
import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Dialog;

/**
 * The dialog box to edit the chart object.
 * @author kurata / SOARS project
 */
public class EditChartObjectDlg extends Dialog implements ITextUndoRedoManagerCallBack {

	/**
	 * Default width.
	 */
	static public final int _minimumWidth = 640;

	/**
	 * Default height.
	 */
	static public final int _minimumHeight = 480;

	/**
	 * 
	 */
	private ChartObject _chartObject = null;

	/**
	 * 
	 */
	private JTextField _nameTextField = null;

	/**
	 * 
	 */
	private JTextField _commentTextField = null;

	/**
	 * 
	 */
	private JTextField _titleTextField = null;

	/**
	 * 
	 */
	private JTextField _horizontalAxisTextField = null;

	/**
	 * 
	 */
	private JTextField _verticalAxisTextField = null;

	/**
	 * 
	 */
	private JCheckBox _connectCheckBox = null;

	/**
	 * 
	 */
	private NumberObjectDataPairTable _numberObjectDataPairTable = null;

	/**
	 * 
	 */
	private JScrollPane _scrollPane = null;

	/**
	 * 
	 */
	private JButton _appendNumberObjectDataPairButton = null;

	/**
	 * 
	 */
	private JLabel[] _labels = new JLabel[] {
		null, null, null, null,
		null
	};

	/**
	 * 
	 */
	protected List<TextUndoRedoManager> _textUndoRedoManagers = new ArrayList<TextUndoRedoManager>();

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog.
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param chartObject the chart object
	 * @throws HeadlessException thrown when code that is dependent on a keyboard, display, or mouse is called in an environment that does not support a keyboard, display, or mouse.
	 */
	public EditChartObjectDlg(Frame arg0, String arg1, boolean arg2, ChartObject chartObject) throws HeadlessException {
		super(arg0, arg1, arg2);
		_chartObject = chartObject;
	}

	/**
	 * @return
	 */
	private Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._editChartObjectDialogRectangleKey + "x",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimumWidth, _minimumHeight).x));
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editChartObjectDialogRectangleKey + "y",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimumWidth, _minimumHeight).y));
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editChartObjectDialogRectangleKey + "width",
			String.valueOf( _minimumWidth));
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editChartObjectDialogRectangleKey + "height",
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
			Environment._editChartObjectDialogRectangleKey + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._editChartObjectDialogRectangleKey + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._editChartObjectDialogRectangleKey + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._editChartObjectDialogRectangleKey + "height", String.valueOf( rectangle.height));
	}

	/**
	 * Runs this dialog box.
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



		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_nameTextField( northPanel);

		insert_horizontal_glue( northPanel);

		setup_commentTextField( northPanel);

		insert_horizontal_glue( northPanel);

		setup_titleTextField( northPanel);

		insert_horizontal_glue( northPanel);

		setup_horizontalAxisTextField( northPanel);

		insert_horizontal_glue( northPanel);

		setup_verticalAxisTextField( northPanel);

		insert_horizontal_glue( northPanel);

		setup_connectCheckBox( northPanel);

		insert_horizontal_glue( northPanel);

		getContentPane().add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		if ( !setup_numberObjectPairsTable( centerPanel))
			return false;

		getContentPane().add( centerPanel);



		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		setup_appendNumberObjectDataPairButton( southPanel);

		insert_horizontal_glue( southPanel);

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));
		setup_ok_and_cancel_button(
			panel,
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			false, false);
		southPanel.add( panel);

		insert_horizontal_glue( southPanel);

		getContentPane().add( southPanel, "South");



		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		adjust();

		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_nameTextField(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_labels[ 0] = new JLabel( ResourceManager.get_instance().get( "edit.chart.dialog.label.name"));
		_labels[ 0].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _labels[ 0]);

		panel.add( Box.createHorizontalStrut( 5));

		_nameTextField = new JTextField();
		_nameTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters1));
		_nameTextField.setText( _chartObject._name);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _nameTextField, this));
		panel.add( _nameTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_commentTextField(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_labels[ 1] = new JLabel( ResourceManager.get_instance().get( "edit.chart.dialog.label.comment"));
		_labels[ 1].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _labels[ 1]);

		panel.add( Box.createHorizontalStrut( 5));

		_commentTextField = new JTextField();
		//_comment_textField.setDocument( new TextExcluder( ";.?:"));
		_commentTextField.setText( _chartObject._comment);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _commentTextField, this));
		panel.add( _commentTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_titleTextField(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_labels[ 2] = new JLabel( ResourceManager.get_instance().get( "edit.chart.dialog.label.title"));
		_labels[ 2].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _labels[ 2]);

		panel.add( Box.createHorizontalStrut( 5));

		_titleTextField = new JTextField();
		_titleTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters7));
		_titleTextField.setText( _chartObject._title);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _titleTextField, this));
		panel.add( _titleTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_horizontalAxisTextField(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_labels[ 3] = new JLabel( ResourceManager.get_instance().get( "edit.chart.dialog.label.horizontal.axis"));
		_labels[ 3].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _labels[ 3]);

		panel.add( Box.createHorizontalStrut( 5));

		_horizontalAxisTextField = new JTextField();
		_horizontalAxisTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters7));
		_horizontalAxisTextField.setText( _chartObject._horizontalAxis);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _horizontalAxisTextField, this));
		panel.add( _horizontalAxisTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_verticalAxisTextField(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_labels[ 4] = new JLabel( ResourceManager.get_instance().get( "edit.chart.dialog.label.vertical.axis"));
		_labels[ 4].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _labels[ 4]);

		panel.add( Box.createHorizontalStrut( 5));

		_verticalAxisTextField = new JTextField();
		_verticalAxisTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters7));
		_verticalAxisTextField.setText( _chartObject._verticalAxis);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _verticalAxisTextField, this));
		panel.add( _verticalAxisTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_connectCheckBox(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_connectCheckBox = new JCheckBox(
			ResourceManager.get_instance().get( "edit.chart.dialog.check.box.connect"),
			_chartObject._connect);
		panel.add( _connectCheckBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_numberObjectPairsTable(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_numberObjectDataPairTable = new NumberObjectDataPairTable( ( Frame)getOwner(), this);
		if ( !_numberObjectDataPairTable.setup( _chartObject._numberObjectDataPairs))
			return false;

		_scrollPane = new JScrollPane();
		_scrollPane.getViewport().setView( _numberObjectDataPairTable);
		_scrollPane.addMouseListener( new MouseInputAdapter() {
			public void mouseReleased(MouseEvent arg0) {
				if ( !SwingTool.is_mouse_right_button( arg0))
					return;

				_numberObjectDataPairTable.on_mouse_right_up( new Point( arg0.getX(),
					arg0.getY() - _numberObjectDataPairTable.getTableHeader().getHeight()));
			}
		});
		panel.add( _scrollPane);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_appendNumberObjectDataPairButton(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridLayout( 1, 1));

		_appendNumberObjectDataPairButton = new JButton(
			ResourceManager.get_instance().get( "edit.chart.dialog.button.append.number.object.pair"));
		_appendNumberObjectDataPairButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_numberObjectDataPairTable.on_append( arg0);
			}
		});
		buttonPanel.add( _appendNumberObjectDataPairButton);

		panel.add( buttonPanel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;

		for ( int i = 0; i < _labels.length; ++i)
			width = Math.max( width, _labels[ i].getPreferredSize().width);

		for ( int i = 0; i < _labels.length; ++i)
			_labels[ i].setPreferredSize( new Dimension( width,
				_labels[ i].getPreferredSize().height));
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		optimize_window_rectangle();

		_nameTextField.requestFocusInWindow();

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
		if ( _nameTextField.getText().equals( "") || _nameTextField.getText().startsWith( "$")
			|| 0 <= _nameTextField.getText().indexOf( Constant._experimentName)
			|| _nameTextField.getText().equals( Constant._initialDataFileRoleName)
			|| _nameTextField.getText().equals( Constant._initialDataFileSpotName))
			return;

		if ( 0 <= _titleTextField.getText().indexOf( "$")
			|| 0 <= _titleTextField.getText().indexOf( Constant._experimentName))
			return;

		if ( 0 <= _horizontalAxisTextField.getText().indexOf( "$")
			|| 0 <= _horizontalAxisTextField.getText().indexOf( Constant._experimentName))
			return;

		if ( 0 <= _verticalAxisTextField.getText().indexOf( "$")
			|| 0 <= _verticalAxisTextField.getText().indexOf( Constant._experimentName))
			return;

		if ( LayerManager.get_instance().contains( _chartObject, _nameTextField.getText()))
			return;

		for ( int i = 0; i < Constant._kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( Constant._kinds[ i], _nameTextField.getText()))
				return;
		}

		_chartObject.rename( _nameTextField.getText(), ( Graphics2D)getGraphics());

		_chartObject._comment = _commentTextField.getText();

		_chartObject._title = _titleTextField.getText();
		_chartObject._horizontalAxis = _horizontalAxisTextField.getText();
		_chartObject._verticalAxis = _verticalAxisTextField.getText();

		_chartObject._connect = _connectCheckBox.isSelected();

		_numberObjectDataPairTable.get( _chartObject._numberObjectDataPairs);

		Observer.get_instance().on_update_chartObject();

		Observer.get_instance().modified();

		set_property_to_environment_file();

		super.on_ok(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_cancel(java.awt.event.ActionEvent)
	 */
	protected void on_cancel(ActionEvent actionEvent) {
		set_property_to_environment_file();
		super.on_cancel(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.undo.UndoManager)
	 */
	public void on_changed(UndoManager undoManager) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.text.AbstractDocument.DefaultDocumentEvent, javax.swing.undo.UndoManager)
	 */
	public void on_changed(DefaultDocumentEvent defaultDocumentEvent,UndoManager undoManager) {
	}
}
