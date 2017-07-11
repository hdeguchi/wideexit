/**
 * 
 */
package soars.application.visualshell.object.docker.edit.comon;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.swing.border.ComponentTitledBorder;
import soars.common.utility.swing.spinner.CustomNumberSpinner;
import soars.common.utility.swing.spinner.INumberSpinnerHandler;
import soars.common.utility.swing.spinner.NumberSpinner;
import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class DockerPropertyPanel extends JPanel implements INumberSpinnerHandler, ITextUndoRedoManagerCallBack {

	/**
	 * 
	 */
	private JCheckBox _specificDockerImageNameCheckBox = null;

	/**
	 * 
	 */
	private JLabel _dockerImageNameLabel = null;

	/**
	 * 
	 */
	private JTextField _dockerImageNameTextField = null;

	/**
	 * 
	 */
	private JCheckBox _specificUserCheckBox = null;

	/**
	 * 
	 */
	private List<JLabel> _userLabels = new ArrayList<JLabel>();

	/**
	 * 
	 */
	private CustomNumberSpinner _userIdNumberSpinner = null;	// 500 - 60000

	/**
	 * 
	 */
	private JTextField _userNameTextField = null;

	/**
	 * 
	 */
	private JPasswordField _passwordField = null;

	/**
	 * 
	 */
	private JCheckBox _passwordCheckBox = null;

	/**
	 * 
	 */
	private List<JPanel> _borderPanels = new ArrayList<JPanel>();

	/**
	 * 
	 */
	private TextUndoRedoManager _textUndoRedoManager = null;

	/**
	 * 
	 */
	public DockerPropertyPanel() {
	}

	/**
	 * 
	 */
	public void setup() {
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));

		setup_docker_image_name_border_panel( this);

		setup_user_name_border_panel( this);

		SwingTool.insert_horizontal_glue( this);
	}

	/**
	 * @param parent
	 */
	private void setup_docker_image_name_border_panel(JPanel parent) {
		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BoxLayout( basePanel, BoxLayout.X_AXIS));

		basePanel.add( Box.createHorizontalStrut( 5));

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		setup_dockerImageNameTextField( panel);

		SwingTool.insert_horizontal_glue( panel);

		_specificDockerImageNameCheckBox = new JCheckBox(
			ResourceManager.get_instance().get( "create.docker.fileset.dialog.border.docker.image.name"),
			Environment.get_instance().get( Environment._dockerFilesetCreatorSpecificDockerImageNameKey, "false").equals( "true"));
		_specificDockerImageNameCheckBox.addItemListener( new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				update_docker_image_name_border_panel( _specificDockerImageNameCheckBox.isSelected());
			}
		});

		panel.setBorder( new ComponentTitledBorder( _specificDockerImageNameCheckBox,
			panel, BorderFactory.createLineBorder( getForeground())));

		_borderPanels.add( panel);

		basePanel.add( panel);

		basePanel.add( Box.createHorizontalStrut( 5));

		parent.add( basePanel);
	}

	/**
	 * @param selected
	 */
	private void update_docker_image_name_border_panel(boolean selected) {
		_dockerImageNameLabel.setEnabled( selected);
		_dockerImageNameTextField.setEnabled( selected);
	}

	/**
	 * @param parent
	 */
	private void setup_dockerImageNameTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_dockerImageNameLabel = new JLabel( ResourceManager.get_instance().get( "create.docker.fileset.dialog.label.docker.image.name"));
		_dockerImageNameLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _dockerImageNameLabel);

		panel.add( Box.createHorizontalStrut( 5));

		_dockerImageNameTextField = new JTextField( Environment.get_instance().get( Environment._dockerFilesetCreatorDockerImageNameKey, ""));
		_textUndoRedoManager = new TextUndoRedoManager( _dockerImageNameTextField, this);
		panel.add( _dockerImageNameTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_user_name_border_panel(JPanel parent) {
		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BoxLayout( basePanel, BoxLayout.X_AXIS));

		basePanel.add( Box.createHorizontalStrut( 5));

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		setup_userIdNumberSpinner( panel);

		SwingTool.insert_horizontal_glue( panel);

		setup_userNameTextField( panel);

		SwingTool.insert_horizontal_glue( panel);

		setup_passwordField( panel);

		SwingTool.insert_horizontal_glue( panel);

		setup_passwordCheckBox( panel);

		SwingTool.insert_horizontal_glue( panel);

		_specificUserCheckBox = new JCheckBox(
			ResourceManager.get_instance().get( "create.docker.fileset.dialog.border.user"),
			Environment.get_instance().get( Environment._dockerFilesetCreatorSpecificUserKey, "false").equals( "true"));
		_specificUserCheckBox.addItemListener( new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				update_user_name_border_panel( _specificUserCheckBox.isSelected());
			}
		});

		panel.setBorder( new ComponentTitledBorder( _specificUserCheckBox,
			panel, BorderFactory.createLineBorder( getForeground())));

		_borderPanels.add( panel);

		basePanel.add( panel);

		basePanel.add( Box.createHorizontalStrut( 5));

		parent.add( basePanel);
	}

	/**
	 * @param selected
	 */
	private void update_user_name_border_panel(boolean selected) {
		for ( JLabel label:_userLabels)
			label.setEnabled( selected);

		_userIdNumberSpinner.setEnabled( selected);
		_userNameTextField.setEnabled( selected);
		_passwordField.setEnabled( selected);
	}

	/**
	 * @param parent
	 */
	private void setup_userIdNumberSpinner(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "create.docker.fileset.dialog.label.user.id"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_userLabels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_userIdNumberSpinner = new CustomNumberSpinner( this);
		_userIdNumberSpinner.set_minimum( 500);
		_userIdNumberSpinner.set_maximum( 60000);
		_userIdNumberSpinner.set_value( Integer.valueOf( Environment.get_instance().get( Environment._dockerFilesetCreatorUserIdKey, "1000")));
		panel.add( _userIdNumberSpinner);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.spinner.INumberSpinnerHandler#changed(java.lang.String, soars.common.utility.swing.spinner.NumberSpinner)
	 */
	@Override
	public void changed(String number, NumberSpinner numberSpinner) {
		//number = ( number.equals( "") ? "0" : number);
		//if ( numberSpinner.equals( _userIdNumberSpinner))
		//	System.out.println( number);
	}

	/**
	 * @param parent
	 */
	private void setup_userNameTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "create.docker.fileset.dialog.label.username"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_userLabels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_userNameTextField = new JTextField( Environment.get_instance().get( Environment._dockerFilesetCreatorUsernameKey, ""));
		_textUndoRedoManager = new TextUndoRedoManager( _userNameTextField, this);
		panel.add( _userNameTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_passwordField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "create.docker.fileset.dialog.label.password"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_userLabels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_passwordField = new JPasswordField( Environment.get_instance().get( Environment._dockerFilesetCreatorPasswordKey, ""));
		_textUndoRedoManager = new TextUndoRedoManager( _passwordField, this);
		panel.add( _passwordField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_passwordCheckBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( "");
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_userLabels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		JPanel passwordCheckBoxPanel = new JPanel();
		passwordCheckBoxPanel.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0));

		_passwordCheckBox = new JCheckBox( ResourceManager.get_instance().get( "create.docker.fileset.dialog.checkbox.show.password"), false);
		_passwordCheckBox.addItemListener( new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				_passwordField.setEchoChar( _passwordCheckBox.isSelected() ? 0 : '*');
			}
		});
		passwordCheckBoxPanel.add( _passwordCheckBox);
		panel.add( passwordCheckBoxPanel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @return
	 */
	public String get_generate_button_label() {
		return ResourceManager.get_instance().get( "create.docker.fileset.dialog.generate.button");
	}

	/**
	 * @return
	 */
	public String get_close_button_label() {
		return ResourceManager.get_instance().get( "create.docker.fileset.dialog.close.button");
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		int width = _dockerImageNameLabel.getPreferredSize().width;

		for ( JLabel label:_userLabels)
			width = ( label.getPreferredSize().width > width) ? label.getPreferredSize().width : width;

		_dockerImageNameLabel.setPreferredSize( new Dimension( width, _dockerImageNameLabel.getPreferredSize().height));

		for ( JLabel label:_userLabels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));

		update_docker_image_name_border_panel( _specificDockerImageNameCheckBox.isSelected());
		update_user_name_border_panel( _specificUserCheckBox.isSelected());
	}

	/**
	 * 
	 */
	public void store() {
		Environment.get_instance().set( Environment._dockerFilesetCreatorSpecificDockerImageNameKey, _specificDockerImageNameCheckBox.isSelected() ? "true" : "false");
		Environment.get_instance().set( Environment._dockerFilesetCreatorDockerImageNameKey, _dockerImageNameTextField.getText());

		Environment.get_instance().set( Environment._dockerFilesetCreatorSpecificUserKey, _specificUserCheckBox.isSelected() ? "true" : "false");
		Environment.get_instance().set( Environment._dockerFilesetCreatorUserIdKey, String.valueOf( _userIdNumberSpinner.get_value()));
		Environment.get_instance().set( Environment._dockerFilesetCreatorUsernameKey, _userNameTextField.getText());
		Environment.get_instance().set( Environment._dockerFilesetCreatorPasswordKey, String.copyValueOf( _passwordField.getPassword()));
	}

	/**
	 * @param result
	 * @param component
	 */
	public void show_message(boolean result, Component component) {
		JOptionPane.showMessageDialog( component,
			result ? ResourceManager.get_instance().get( "create.docker.fileset.success.message") : ResourceManager.get_instance().get( "create.docker.fileset.error.message"),
			ResourceManager.get_instance().get( "application.title"),
			result ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.undo.UndoManager)
	 */
	@Override
	public void on_changed(UndoManager undoManager) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.text.AbstractDocument.DefaultDocumentEvent, javax.swing.undo.UndoManager)
	 */
	@Override
	public void on_changed(DefaultDocumentEvent defaultDocumentEvent, UndoManager undoManager) {
	}
}
