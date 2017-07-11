/**
 * 
 */
package soars.application.manager.library.main.tab.tab.annotaion.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
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

import soars.application.manager.library.main.ResourceManager;
import soars.application.manager.library.main.tab.tab.annotaion.tree.AnnotationData;
import soars.common.utility.swing.border.ComponentTitledBorder;
import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class Components extends JPanel implements ITextUndoRedoManagerCallBack {

	/**
	 * 
	 */
	private String _language = "";

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
	private List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * 
	 */
	private List<TextUndoRedoManager> _textUndoRedoManagers = new ArrayList<TextUndoRedoManager>();

	/**
	 * @param language
	 */
	public Components(String language) {
		super();
		_language = language;
	}

	/**
	 * @return
	 */
	public boolean create() {
		setLayout( new BorderLayout());


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));
		SwingTool.insert_vertical_strut( northPanel, 3);
		add( northPanel, "North");


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.X_AXIS));

		centerPanel.add( Box.createHorizontalStrut( 5));

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		setup_center_panel( panel);
		centerPanel.add( panel);

		centerPanel.add( Box.createHorizontalStrut( 5));


		add( centerPanel);


		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_center_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.X_AXIS));

		northPanel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "annotation.components.name"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_labels.add( label);
		northPanel.add( label);

		northPanel.add( Box.createHorizontalStrut( 5));

		_nameTextField = new JTextField();
		_textUndoRedoManagers .add( new TextUndoRedoManager( _nameTextField, this));
		northPanel.add( _nameTextField);

		northPanel.add( Box.createHorizontalStrut( 5));

		panel.add( northPanel, "North");


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.X_AXIS));

		centerPanel.add( Box.createHorizontalStrut( 5));

		label = new JLabel( ResourceManager.get_instance().get( "annotation.components.comment"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_labels.add( label);
		centerPanel.add( label);

		centerPanel.add( Box.createHorizontalStrut( 5));

		_commentTextArea = new JTextArea();
		_commentTextArea.setLineWrap( true);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setView( _commentTextArea);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _commentTextArea, this));
		centerPanel.add( scrollPane);

		centerPanel.add( Box.createHorizontalStrut( 5));

		panel.add( centerPanel);


		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));
		SwingTool.insert_vertical_strut( southPanel, 5);
		panel.add( southPanel, "South");


		parent.add( panel);


		parent.setBorder( new ComponentTitledBorder( new JLabel( ResourceManager.get_instance().get( "annotation.components.border." + _language)), this, BorderFactory.createLineBorder( getForeground())));
	}

	/**
	 * @param width
	 * @return
	 */
	public int get_max_width(int width) {
		for ( JLabel label:_labels)
			width = Math.max( width, label.getPreferredSize().width);
		return width;
	}

	/**
	 * @param width
	 */
	public void adjust(int width) {
		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));
	}

	/**
	 * @param annotationData
	 */
	public void readFrom(AnnotationData annotationData) {
		if ( _language.equals( "en")) {
			_nameTextField.setText( annotationData._en);
			_commentTextArea.setText( annotationData._enComment);
		} else if ( _language.equals( "ja")) {
			_nameTextField.setText( annotationData._ja);
			_commentTextArea.setText( annotationData._jaComment);
		}
		_nameTextField.setCaretPosition( 0);
		_commentTextArea.setCaretPosition( 0);
		clear_textUndoRedoManagers();
	}

	/**
	 * @param annotationData
	 */
	public void writeTo(AnnotationData annotationData) {
		if ( _language.equals( "en")) {
			annotationData._en = _nameTextField.getText();
			annotationData._enComment = _commentTextArea.getText();
		} else if ( _language.equals( "ja")) {
			annotationData._ja = _nameTextField.getText();
			annotationData._jaComment = _commentTextArea.getText();
		}
	}

	/**
	 * 
	 */
	public void clear() {
		_nameTextField.setText( "");
		_commentTextArea.setText( "");
		clear_textUndoRedoManagers();
	}

	/**
	 * 
	 */
	private void clear_textUndoRedoManagers() {
		for ( TextUndoRedoManager textUndoRedoManager:_textUndoRedoManagers)
			textUndoRedoManager.clear();
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
