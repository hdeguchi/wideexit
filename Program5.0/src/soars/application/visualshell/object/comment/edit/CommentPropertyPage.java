/*
 * 2005/06/21
 */
package soars.application.visualshell.object.comment.edit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
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

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.MainFrame;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.comment.CommentManager;
import soars.common.utility.swing.panel.StandardPanel;
import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextUndoRedoManager;

/**
 * The tab component to edit the comments.
 * @author kurata / SOARS project
 */
public class CommentPropertyPage extends StandardPanel implements ITextUndoRedoManagerCallBack {

	/**
	 * Title for the tab.
	 */
	public String _title = ResourceManager.get_instance().get( "edit.comment.dialog.title");

	/**
	 * 
	 */
	private JTextField _titleTextField = null;

	/**
	 * 
	 */
	private JTextField _dateTextField = null;

	/**
	 * 
	 */
	private JTextField _authorTextField = null;

	/**
	 * 
	 */
	private JTextField _emailTextField = null;

	/**
	 * 
	 */
	private JLabel[] _labels = new JLabel[] {
		null, null, null, null
	};

	/**
	 * 
	 */
	private JTextArea _commentTextArea = null;

	/**
	 * 
	 */
	private JScrollPane _scrollPane = null;

	/**
	 * 
	 */
	private List<TextUndoRedoManager> _textUndoRedoManagers = new ArrayList<TextUndoRedoManager>();

	/**
	 * 
	 */
	protected Frame _owner = null;

	/**
	 * 
	 */
	protected Component _parent = null;

	/**
	 * Creates this object.
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public CommentPropertyPage(Frame owner, Component parent) {
		super();
		_owner = owner;
		_parent = parent;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.panel.StandardPanel#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;



		setLayout( new BorderLayout());



		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_titleTextField( northPanel);

		insert_vertical_strut( northPanel);

		setup_dateTextField( northPanel);

		insert_vertical_strut( northPanel);

		setup_authorTextField( northPanel);

		insert_vertical_strut( northPanel);

		setup_emailTextField( northPanel);

		insert_vertical_strut( northPanel);

		add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		setup_commentTextArea( centerPanel);

		add( centerPanel);



		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

//		insert_vertical_strut( southPanel);
//
//		setup_export_checkBox( southPanel);

		insert_vertical_strut( southPanel);

		add( southPanel, "South");



		adjust();



		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_titleTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_labels[ 0] = new JLabel(
			ResourceManager.get_instance().get( "edit.comment.dialog.label.title"));
		_labels[ 0].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _labels[ 0]);


		panel.add( Box.createHorizontalStrut( 5));


		_titleTextField = new JTextField();
		_titleTextField.setDocument( new TextExcluder( "\t"));
		_titleTextField.setText( CommentManager.get_instance()._title);
		_textUndoRedoManagers .add( new TextUndoRedoManager( _titleTextField, this));

		panel.add( _titleTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_dateTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_labels[ 1] = new JLabel(
			ResourceManager.get_instance().get( "edit.comment.dialog.label.date"));
		_labels[ 1].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _labels[ 1]);


		panel.add( Box.createHorizontalStrut( 5));


		_dateTextField = new JTextField();
		_dateTextField.setDocument( new TextExcluder( "\t"));
		_dateTextField.setText( CommentManager.get_instance()._date);
		_textUndoRedoManagers .add( new TextUndoRedoManager( _dateTextField, this));

		panel.add( _dateTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_authorTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_labels[ 2] = new JLabel(
			ResourceManager.get_instance().get( "edit.comment.dialog.label.author"));
		_labels[ 2].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _labels[ 2]);


		panel.add( Box.createHorizontalStrut( 5));


		_authorTextField = new JTextField();
		_authorTextField.setDocument( new TextExcluder( "\t"));
		_authorTextField.setText( CommentManager.get_instance()._author);
		_textUndoRedoManagers .add( new TextUndoRedoManager( _authorTextField, this));

		panel.add( _authorTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_emailTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_labels[ 3] = new JLabel(
			ResourceManager.get_instance().get( "edit.comment.dialog.label.email"));
		_labels[ 3].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _labels[ 3]);


		panel.add( Box.createHorizontalStrut( 5));


		_emailTextField = new JTextField();
		_emailTextField.setDocument( new TextExcluder( "\t"));
		_emailTextField.setText( CommentManager.get_instance()._email);
		_textUndoRedoManagers .add( new TextUndoRedoManager( _emailTextField, this));

		panel.add( _emailTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 * 
	 */
	private void setup_commentTextArea(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel textAreaPanel = new JPanel();
		textAreaPanel.setLayout( new GridLayout( 1, 1));

		_commentTextArea = new JTextArea( new TextExcluder( "\t"));
		_commentTextArea.setText( CommentManager.get_instance()._comment);
		_commentTextArea.setLineWrap( true);
		_textUndoRedoManagers .add( new TextUndoRedoManager( _commentTextArea, this));

		_scrollPane = new JScrollPane();
		_scrollPane.getViewport().setView( _commentTextArea);

		textAreaPanel.add( _scrollPane);
		panel.add( textAreaPanel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

//	/**
//	 * @param parent
//	 */
//	private void setup_export_checkBox(JPanel parent) {
//		JPanel panel = new JPanel();
//		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0));
//
//		_exportCheckBox = new JCheckBox(
//			ResourceManager.get_instance().get( "edit.comment.dialog.export"));
//		_exportCheckBox.setSelected( CommentManager.get_instance()._export);
//
//		panel.add( _exportCheckBox);
//		parent.add( panel);
//	}

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


//		_title_textField.setPreferredSize( new Dimension(
//			_scrollPane.getPreferredSize().width - width - 5,
//			_title_textField.getPreferredSize().height));
//
//		_date_textField.setPreferredSize( new Dimension(
//			_scrollPane.getPreferredSize().width - width - 5,
//			_date_textField.getPreferredSize().height));
//
//		_author_textField.setPreferredSize( new Dimension(
//			_scrollPane.getPreferredSize().width - width - 5,
//			_author_textField.getPreferredSize().height));
//
//		_email_textField.setPreferredSize( new Dimension(
//			_scrollPane.getPreferredSize().width - width - 5,
//			_email_textField.getPreferredSize().height));
	}

	/**
	 * Invoked when this objet has been initialized.
	 */
	public void on_setup_completed() {
		_titleTextField.requestFocusInWindow();
	}

	/**
	 * Returns true for updating successfully.
	 * @return true for updating successfully
	 */
	public boolean on_ok() {
		CommentManager.get_instance()._title = _titleTextField.getText();
		CommentManager.get_instance()._date = _dateTextField.getText();
		CommentManager.get_instance()._author = _authorTextField.getText();
		CommentManager.get_instance()._email = _emailTextField.getText();
		CommentManager.get_instance()._comment = _commentTextArea.getText();
//		CommentManager.get_instance()._export = _export_checkBox.isSelected();

		MainFrame.get_instance().setTitle(
			ResourceManager.get_instance().get( "application.title")
			+ ( CommentManager.get_instance()._title.equals( "") ? "" : ( " - " + CommentManager.get_instance()._title))
			+ ( ( null == LayerManager.get_instance().get_current_file()) ? "" : " [" + LayerManager.get_instance().get_current_file().getName() + "]"));

		return true;
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
