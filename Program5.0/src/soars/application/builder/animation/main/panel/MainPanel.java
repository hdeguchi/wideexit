/**
 * 
 */
package soars.application.builder.animation.main.panel;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import soars.application.builder.animation.document.Document;
import soars.application.builder.animation.document.DocumentManager;
import soars.application.builder.animation.main.ResourceManager;

/**
 * @author kurata
 *
 */
public class MainPanel extends JPanel {


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
	private boolean _ignore = false;

	/**
	 * 
	 */
	public MainPanel() {
		super();
	}

	/**
	 * @return
	 */
	public boolean setup() {
		setLayout( new BorderLayout());


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		//SwingTool.insert_horizontal_glue( north_panel);

		setup_title_textField( northPanel);

		//SwingTool.insert_horizontal_glue( north_panel);

		add( northPanel, "North");


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		setup_comment_textArea( centerPanel);

		add( centerPanel);


		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		//SwingTool.insert_horizontal_glue( south_panel);

		add( southPanel, "South");


		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_title_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel titlePanel = new JPanel();
		titlePanel.setLayout( new BoxLayout( titlePanel, BoxLayout.Y_AXIS));

		titlePanel.setBorder( BorderFactory.createTitledBorder(
			ResourceManager.get_instance().get( "main.panel.title.label")));

		_titleTextField = new JTextField();
		_titleTextField.getDocument().addDocumentListener( new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				on_update();
			}
			public void insertUpdate(DocumentEvent e) {
				on_update();
			}
			public void removeUpdate(DocumentEvent e) {
				on_update();
			}
		});
		titlePanel.add( _titleTextField);

		panel.add( titlePanel);

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

		JPanel commentPanel = new JPanel();
		commentPanel.setLayout( new BoxLayout( commentPanel, BoxLayout.Y_AXIS));

		commentPanel.setBorder( BorderFactory.createTitledBorder(
			ResourceManager.get_instance().get( "main.panel.comment.label")));

		_commentTextArea = new JTextArea();
		_commentTextArea.getDocument().addDocumentListener( new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				on_update();
			}
			public void insertUpdate(DocumentEvent e) {
				on_update();
			}
			public void removeUpdate(DocumentEvent e) {
				on_update();
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _commentTextArea);

		commentPanel.add( scrollPane);

		panel.add( commentPanel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * 
	 */
	protected void on_update() {
		if ( _ignore)
			return;

		DocumentManager.get_instance().modified();
	}

	/**
	 * @param previous
	 * @param next 
	 */
	public void update(Document previous, Document next) {
		_ignore = true;

		if ( null != previous)
			get( previous);

		set( next);

		_ignore = false;
	}

	/**
	 * @param document
	 */
	private void get(Document document) {
		document._title = _titleTextField.getText();
		document._comment = _commentTextArea.getText();
	}

	/**
	 * @param document
	 */
	private void set(Document document) {
		_titleTextField.setText( ( null == document) ? "" : document._title);
		_commentTextArea.setText( ( null == document) ? "" : document._comment);
	}

	/**
	 * 
	 */
	public void reset() {
		_ignore = true;
		_titleTextField.setText( "");
		_commentTextArea.setText( "");
		_ignore = false;
	}

	/**
	 * @param document
	 */
	public void load(Document document) {
		if ( null == document)
			return;

		_ignore = true;
		set( document);
		_ignore = false;
	}

	/**
	 * @param document
	 */
	public void store(Document document) {
		if ( null == document)
			return;

		get( document);
	}
}
