/**
 * 
 */
package soars.application.visualshell.object.arbitrary.edit;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.swing.window.Dialog;

/**
 * The dialog box to select java class.
 * @author kurata / SOARS project
 */
public class SelectJavaClassDlg extends Dialog {

	/**
	 * 
	 */
	private String[] _exceptedJavaClasses = null;

	/**
	 * 
	 */
	private JLabel _label = null;

	/**
	 * 
	 */
	private JTextField _wordTextField = null;

	/**
	 * 
	 */
	private JavaClassesList _javaClassesList = null;

	/**
	 * 
	 */
	private JScrollPane _scrollPane = null;

	/**
	 * 
	 */
	protected List<String> _selectedJavaClasses = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param exceptedJavaClasses
	 * @throws HeadlessException
	 */
	public SelectJavaClassDlg(Frame arg0, String arg1, boolean arg2, String[] exceptedJavaClasses) {
		super(arg0, arg1, arg2);
		_exceptedJavaClasses = exceptedJavaClasses;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		setResizable( false);


		link_to_cancel( getRootPane());


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		insert_horizontal_glue();

		setup_wordTextField();

		insert_horizontal_glue();

		if ( !setup_javaClassesList())
			return false;

		insert_horizontal_glue();

		setup_ok_and_cancel_button(
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			true, true);

		insert_horizontal_glue();


		adjust();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		return true;
	}

	/**
	 * 
	 */
	private void setup_wordTextField() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_label = new JLabel(
			ResourceManager.get_instance().get( "select.java.class.dialog.name"));
		panel.add( _label);

		panel.add( Box.createHorizontalStrut( 5));

		_wordTextField = new JTextField();
		_wordTextField.addKeyListener( new KeyListener() {
			public void keyPressed(KeyEvent arg0) {
			}
			public void keyReleased(KeyEvent arg0) {
				if ( KeyEvent.VK_SHIFT == arg0.getKeyCode())
					return;

				_javaClassesList.update( _wordTextField.getText(), _exceptedJavaClasses);
			}
			public void keyTyped(KeyEvent arg0) {
			}
		});

		link_to_cancel( _wordTextField);

		panel.add( _wordTextField);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * @return
	 */
	private boolean setup_javaClassesList() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_javaClassesList = new JavaClassesList( this);
		if ( !_javaClassesList.setup())
			return false;

		link_to_cancel( _javaClassesList);

		_scrollPane = new JScrollPane();
		_scrollPane.getViewport().setView( _javaClassesList);
		_scrollPane.setPreferredSize( new Dimension( 600, 320));

		link_to_cancel( _scrollPane);

		panel.add( _scrollPane);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);

		return true;
	}

	/**
	 * 
	 */
	private void adjust() {
		_wordTextField.setPreferredSize( new Dimension(
			_scrollPane.getPreferredSize().width - _label.getPreferredSize().width - 5,
			_wordTextField.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		_wordTextField.requestFocusInWindow();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		_selectedJavaClasses = _javaClassesList.on_ok();
		if ( null == _selectedJavaClasses || _selectedJavaClasses.isEmpty())
			return;

		super.on_ok(actionEvent);
	}
}
