/**
 * 
 */
package soars.common.soars.property;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

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

import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 *
 */
public class EditPropertyDlg extends Dialog implements ITextUndoRedoManagerCallBack {

	/**
	 * 
	 */
	private List<TextUndoRedoManager> _textUndoRedoManagers = new ArrayList<TextUndoRedoManager>();

	/**
	 * 
	 */
	private Property _property = null;

	/**
	 * 
	 */
	public boolean _changed = false;

	/**
	 * 
	 */
	private JTextField _titleTextField = null;

	/**
	 * 
	 */
	private JTextArea _commentTextArea = null;

	/**
	 * 
	 */
	private List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * 
	 */
	private int _width = 400;

	/**
	 * 
	 */
	private int _height = 400;

	/**
	 * @param arg0
	 * @param arg1
	 * @param property
	 */
	public EditPropertyDlg(Frame arg0, boolean arg1, Property property) {
		super(arg0, ResourceManager.get_instance().get( "edit.property.dialog.title"), arg1);
		_property = property;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param property
	 * @param width
	 * @param height
	 */
	public EditPropertyDlg(Frame arg0, boolean arg1, Property property, int width, int height) {
		super(arg0, ResourceManager.get_instance().get( "edit.property.dialog.title"), arg1);
		_property = property;
		_width = width;
		_height = height;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if (!super.on_init_dialog())
			return false;


		//setResizable( false);


		getContentPane().setLayout( new BorderLayout());


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		if ( !setup_north_panel( northPanel))
			return false;

		getContentPane().add( northPanel, "North");


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		if ( !setup_center_panel( centerPanel))
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


		adjust();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);


		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_north_panel(JPanel parent) {
		insert_horizontal_glue( parent);

		if ( !setup_titleTextField( parent))
			return false;

		insert_horizontal_glue( parent);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_titleTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.property.dialog.title.label"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( label);
		_labels.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_titleTextField = new JTextField();
		_titleTextField.setText( _property._title);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _titleTextField, this));
		panel.add( _titleTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_center_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.property.dialog.comment.label"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( label);
		_labels.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_commentTextArea = new JTextArea( _property._comment);
		_commentTextArea.setLineWrap( true);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _commentTextArea, this));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _commentTextArea);

		panel.add( scrollPane);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( JLabel label:_labels)
			width = Math.max( width, label.getPreferredSize().width);

		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));

		_titleTextField.setPreferredSize( new Dimension( _width, _titleTextField.getPreferredSize().height));
		_commentTextArea.setPreferredSize( new Dimension( _width, _height));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		String title = _titleTextField.getText();
		if ( null == title)
			return;

		String comment = _commentTextArea.getText();
		if ( null == comment)
			return;

		_changed = ( !title.equals( _property._title) || !comment.equals( _property._comment));

		_property._title = title;
		_property._comment = comment;

		super.on_ok(actionEvent);
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
