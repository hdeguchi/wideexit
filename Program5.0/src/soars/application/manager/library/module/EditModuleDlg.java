/**
 * 
 */
package soars.application.manager.library.module;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

import soars.application.manager.library.main.Environment;
import soars.application.manager.library.main.ResourceManager;
import soars.common.soars.module.Module;
import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 *
 */
public class EditModuleDlg extends Dialog implements ITextUndoRedoManagerCallBack {

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
	private Module _module = null;

	/**
	 * 
	 */
	private JTextField _nameTextField = null;

	/**
	 * 
	 */
	private JTextArea _commentTextArea = null;

	/**
	 * 
	 */
	private TextUndoRedoManager _textUndoRedoManager = null;

	/**
	 * 
	 */
	private JLabel[] _label = new JLabel[] {
		null, null
	};

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param object
	 */
	public EditModuleDlg(Frame arg0, String arg1, boolean arg2, Module module) {
		super(arg0, arg1, arg2);
		_module = module;
	}

	/**
	 * @return
	 */
	private Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._edit_module_dialog_rectangle_key + "x",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimumWidth, _minimumHeight).x));
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._edit_module_dialog_rectangle_key + "y",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimumWidth, _minimumHeight).y));
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._edit_module_dialog_rectangle_key + "width",
			String.valueOf( _minimumWidth));
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._edit_module_dialog_rectangle_key + "height",
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
			Environment._edit_module_dialog_rectangle_key + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._edit_module_dialog_rectangle_key + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._edit_module_dialog_rectangle_key + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._edit_module_dialog_rectangle_key + "height", String.valueOf( rectangle.height));
	}

	/**
	 * @return
	 */
	public boolean do_modal() {
		Rectangle rectangle = get_rectangle_from_environment_file();
		if ( null == rectangle)
			return do_modal( getOwner(), _minimumWidth, _minimumHeight);
		else
			return do_modal( rectangle);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		//link_to_cancel( getRootPane());


		getContentPane().setLayout( new BorderLayout());


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_name_textField( northPanel);

		insert_horizontal_glue( northPanel);

		getContentPane().add( northPanel, "North");


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		setup_comment_textArea( centerPanel);

		getContentPane().add( centerPanel);


		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

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


		adjust();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_name_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_label[ 0] = new JLabel(
			ResourceManager.get_instance().get( "edit.module.dialog.name"));
		_label[ 0].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _label[ 0]);

		panel.add( Box.createHorizontalStrut( 5));

		_nameTextField = new JTextField( _module.getName());

		//link_to_cancel( _name_textField);

		panel.add( _nameTextField);
		panel.add( Box.createHorizontalStrut( 5));
		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_comment_textArea(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_label[ 1] = new JLabel(
			ResourceManager.get_instance().get( "edit.module.dialog.comment"));
		_label[ 1].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _label[ 1]);


		panel.add( Box.createHorizontalStrut( 5));

		_commentTextArea = new JTextArea( _module.getComment());

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportView( _commentTextArea);

		//link_to_cancel( _comment_textArea);

		_textUndoRedoManager = new TextUndoRedoManager( _commentTextArea, this);

		panel.add( scrollPane);
		panel.add( Box.createHorizontalStrut( 5));
		parent.add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( int i = 0; i < _label.length; ++i)
			width = Math.max( width, _label[ i].getPreferredSize().width);

		for ( int i = 0; i < _label.length; ++i)
			_label[ i].setPreferredSize( new Dimension( width, _label[ i].getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		optimize_window_rectangle();

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

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		if ( _nameTextField.getText().equals( ""))
			return;

		_module.setEnable( "true");
		_module.setName( _nameTextField.getText());
		_module.setComment( _commentTextArea.getText());
		if ( !_module.write())
			return;

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
	public void on_changed(DefaultDocumentEvent defaultDocumentEvent, UndoManager undoManager) {
	}
}
