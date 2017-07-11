/*
 * Created on 2006/06/13
 */
package soars.plugin.visualshell.tsubame2.main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import soars.common.utility.swing.spinner.CustomNumberSpinner;
import soars.common.utility.swing.spinner.INumberSpinnerHandler;
import soars.common.utility.swing.spinner.NumberSpinner;
import soars.common.utility.swing.window.Dialog;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.reflection.Reflection;
import soars.common.utility.tool.ssh.SshTool;

import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;

/**
 * @author kurata
 */
public class GridDlg extends Dialog implements INumberSpinnerHandler {

	/**
	 * 
	 */
	private JFrame _frame = null;

	/**
	 * 
	 */
	private String _name = "";

	/**
	 * 
	 */
	private JTextField _host_textField = null;

	/**
	 * 
	 */
	private JTextField _username_textField = null;

	/**
	 * 
	 */
	private JTextField _script_directory_textField = null;

	/**
	 * 
	 */
	private JButton _script_directory_selector_button = null;

	/**
	 * 
	 */
	private JTextField _log_directory_textField = null;

	/**
	 * 
	 */
	private JButton _log_directory_selector_button = null;

	/**
	 * 
	 */
	private CustomNumberSpinner _model_builder_memory_size_numberSpinner = null;

	/**
	 * 
	 */
	private JCheckBox _simulation_checkBox = null;

	/**
	 * 
	 */
	private NumberSpinner _number_of_times_numberSpinner = null;

	/**
	 * 
	 */
	private JCheckBox _log_analysis_checkBox = null;

	/**
	 * 
	 */
	private JTextField _log_analysis_condition_filename_textField = null;

	/**
	 * 
	 */
	private JButton _log_analysis_condition_file_selector_button = null;

	/**
	 * 
	 */
	private JCheckBox _transfer_log_checkBox = null;

	/**
	 * 
	 */
	private JTextField _local_log_directory_textField = null;

	/**
	 * 
	 */
	private JButton _local_log_directory_selector_button = null;

	/**
	 * 
	 */
	private JTextArea _error_log_textArea = null;

	/**
	 * 
	 */
	private JScrollPane _error_log_textArea_scrollPane = null;

	/**
	 * 
	 */
	private JLabel[] _labels = new JLabel[] {
		null, null, null, null,
		null, null, null, null
	};

	/**
	 * 
	 */
	private String _root_directory = "";

	/**
	 * 
	 */
	private Parameters _parameters = null;

	/**
	 * 
	 */
	private Object _experimentManager = null;

	/**
	 * 
	 */
	private String _model_builder_memory_size = Constant._defaultMemorySize;

	/**
	 * @param name
	 * @param file
	 * @param frame
	 * @param root_directory
	 * @param java_home
	 * @param home_directory
	 * @param sgeout_directory
	 * @param program_directory
	 * @return
	 */
	public static boolean execute(String name, File file, JFrame frame, String root_directory, String java_home, String home_directory, String sgeout_directory, String program_directory) {
		List resultList = get_experimentManager( name, frame);
		if ( null == resultList || resultList.isEmpty()) {
			JOptionPane.showMessageDialog(
				frame,
				ResourceManager.get_instance().get( "plugin.tsubame2.error.message.no.script"),
				ResourceManager.get_instance().get( "plugin.tsubame2.dialog.title"),
				JOptionPane.ERROR_MESSAGE);
			return true;
		}

		Object experimentManager = resultList.get( 0);

		Parameters parameters = new Parameters();
		parameters._java_home = java_home;
		parameters._home_directory_name = home_directory;
		parameters._executor_jar_filename = ( home_directory + Constant._base_executor_jar_filename);
		parameters._submit_shell_script_file_name = ( home_directory + Constant._base_submit_shell_script_file_name);
		parameters._delete_shell_script_file_name = ( home_directory + Constant._base_delete_shell_script_file_name);
		parameters._sgeout_directory_name = sgeout_directory;
		parameters._program_directory_name = program_directory;

		GridLoginDlg gridLoginDlg = new GridLoginDlg(
			frame,
			ResourceManager.get_instance().get( "plugin.tsubame2.login.dialog.title"),
			true,
			root_directory,
			parameters);
		if ( !gridLoginDlg.do_modal( frame))
			return true;

		Environment.get_instance().store();

		patch( root_directory, parameters._local_username);

		GridDlg gridDlg = new GridDlg( frame,
			ResourceManager.get_instance().get( "plugin.tsubame2.dialog.title"),
			true,
			name,
			root_directory,
			parameters,
			experimentManager);
		if ( !gridDlg.do_modal( frame))
			return true;

		Environment.get_instance().store();

		GridFrame gridFrame = new GridFrame(
			ResourceManager.get_instance().get( "plugin.tsubame2.frame.title")
				+ ( ( null == file) ? "" : ( " - " + file.getName())),
			name,
			frame,
			parameters,
			experimentManager,
			Environment.get_instance().get_clone());
		frame.setEnabled( false);
		if ( !gridFrame.create()) {
			frame.setEnabled( true);
			return false;
		}

		Environment.get_instance().store();

		return true;
	}

	/**
	 * @param name
	 * @param frame
	 * @return
	 */
	private static List get_experimentManager(String name, JFrame frame) {
		List resultList = new ArrayList();
		if ( !Reflection.execute_class_method( frame, "get_experimentManager", new Class[ 0], new Object[ 0], resultList)) {
			JOptionPane.showMessageDialog( frame,
				"Plugin error : Reflection.execute_class_method( ... )" + Constant._lineSeparator
				+ " Plugin name : " + name + Constant._lineSeparator
				+ " Class name : soars.application.visualshell.main.MainFrame" + Constant._lineSeparator
				+ " Method name : get_experimentManager" + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.tsubame2.frame.title"),
				JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return resultList;
	}

	/**
	 * @param root_directory 
	 * @param username
	 */
	private static void patch(String root_directory, String username) {
		if ( !Environment.get_instance().get( Environment._script_directory_key, "").startsWith( root_directory + "/" + username))
			Environment.get_instance().set( Environment._script_directory_key, root_directory + "/" + username);

		if ( !Environment.get_instance().get( Environment._log_directory_key, "").startsWith( root_directory + "/" + username))
			Environment.get_instance().set( Environment._log_directory_key, root_directory + "/" + username);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param name
	 * @param root_directory
	 * @param parameters
	 * @param experimentManager
	 * @throws HeadlessException
	 */
	public GridDlg(JFrame arg0, String arg1, boolean arg2, String name, String root_directory, Parameters parameters, Object experimentManager) throws HeadlessException {
		super(arg0, arg1, arg2);
		_frame = arg0;
		_name = name;
		_root_directory = root_directory;
		_parameters = parameters;
		_experimentManager = experimentManager;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		setResizable( false);


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));


		insert_horizontal_glue();

		setup_host_textField();

		insert_horizontal_glue();

		setup_username_textField();

		insert_horizontal_glue();

		setup_script_directory_textField();

		insert_horizontal_glue();

		setup_log_directory_textField();

		insert_horizontal_glue();

		setup_model_builder_memory_size_numberSpinner();

		insert_horizontal_glue();

		setup_simulation_checkBox();

		setup_number_of_times_numberSpinner();

		insert_horizontal_glue();

		setup_log_analysis_checkBox();

		setup_log_analysis_condition_filename_textField();

		insert_horizontal_glue();

		setup_transfer_log_checkBox();

		setup_local_log_directory_textField();

		insert_horizontal_glue();

		setup_error_log_textArea();

		insert_horizontal_glue();

		setup_ok_and_cancel_button(
			ResourceManager.get_instance().get( "dialog.start"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			false, false);

		insert_horizontal_glue();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);


		adjust();


		return true;
	}

	/**
	 * 
	 */
	private void setup_host_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_labels[ 0] = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.dialog.gird.portal.ip.address"), SwingConstants.RIGHT);
		panel.add( _labels[ 0]);

		_host_textField = new JTextField( _parameters._host);
		_host_textField.setEditable( false);
		_host_textField.setPreferredSize( new Dimension( 400,
			_host_textField.getPreferredSize().height));
		panel.add( _host_textField);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_username_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_labels[ 1] = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.dialog.username"), SwingConstants.RIGHT);
		panel.add( _labels[ 1]);

		_username_textField = new JTextField( _parameters._local_username);
		_username_textField.setEditable( false);
		_username_textField.setPreferredSize( new Dimension( 400,
			_username_textField.getPreferredSize().height));
		panel.add( _username_textField);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_script_directory_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_labels[ 2] = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.dialog.script.directory"), SwingConstants.RIGHT);
		panel.add( _labels[ 2]);


		_script_directory_textField = new JTextField(
			Environment.get_instance().get( Environment._script_directory_key, ""));
		_script_directory_textField.setEditable( false);
		_script_directory_textField.setPreferredSize( new Dimension( 400,
			_script_directory_textField.getPreferredSize().height));
		panel.add( _script_directory_textField);


		_script_directory_selector_button = new JButton( ResourceManager.get_instance().get( "plugin.tsubame2.dialog.script.reference"));
		_script_directory_selector_button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_script_directory_selector_button_actionPerformed( arg0);
			}
		});
		panel.add( _script_directory_selector_button);


		getContentPane().add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_script_directory_selector_button_actionPerformed(ActionEvent actionEvent) {
		String directory = Tool.get_remote_directory( _script_directory_textField.getText(),
			ResourceManager.get_instance().get( "plugin.tsubame2.script.directory.selector.dialog.title"),
			( Frame)getOwner(), this, _parameters._host, _parameters._username, _parameters._password,
			_root_directory, _parameters._local_username);
		if ( null == directory)
			return;

		_script_directory_textField.setText( directory);
	}

	/**
	 * 
	 */
	private void setup_log_directory_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_labels[ 3] = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.dialog.log.directory"), SwingConstants.RIGHT);
		panel.add( _labels[ 3]);


		_log_directory_textField = new JTextField(
			Environment.get_instance().get( Environment._log_directory_key, ""));
		_log_directory_textField.setEditable( false);
		_log_directory_textField.setPreferredSize( new Dimension( 400,
			_log_directory_textField.getPreferredSize().height));
		panel.add( _log_directory_textField);


		_log_directory_selector_button = new JButton( ResourceManager.get_instance().get( "plugin.tsubame2.dialog.log.reference"));
		_log_directory_selector_button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_log_directory_selector_button_actionPerformed( arg0);
			}
		});
		panel.add( _log_directory_selector_button);


		getContentPane().add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_log_directory_selector_button_actionPerformed(ActionEvent actionEvent) {
		String directory = Tool.get_remote_directory( _log_directory_textField.getText(),
			ResourceManager.get_instance().get( "plugin.tsubame2.log.directory.selector.dialog.title"),
			( Frame)getOwner(), this, _parameters._host, _parameters._username, _parameters._password,
			_root_directory, _parameters._local_username);
		if ( null == directory)
			return;

		_log_directory_textField.setText( directory);
	}

	/**
	 * 
	 */
	private void setup_model_builder_memory_size_numberSpinner() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_labels[ 4] = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.dialog.model.builder.memory.size"), SwingConstants.RIGHT);
		panel.add( _labels[ 4]);

		_model_builder_memory_size_numberSpinner = new CustomNumberSpinner( this);
		_model_builder_memory_size_numberSpinner.set_minimum( 0);
		_model_builder_memory_size_numberSpinner.set_maximum( 10000);
		_model_builder_memory_size_numberSpinner.setPreferredSize( new Dimension( 200,
			_model_builder_memory_size_numberSpinner.getPreferredSize().height));
		_model_builder_memory_size = Environment.get_instance().get(
			Environment._model_builder_memory_size_key, Constant._defaultMemorySize);
		_model_builder_memory_size_numberSpinner.set_value( new Integer( _model_builder_memory_size).intValue());
		panel.add( _model_builder_memory_size_numberSpinner);

		JLabel label = new JLabel( "MB");
		panel.add( label);


		getContentPane().add( panel);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.spinner.INumberSpinnerHandler#changed(java.lang.String, soars.common.utility.swing.spinner.NumberSpinner)
	 */
	public void changed(String number, NumberSpinner numberSpinner) {
		number = ( number.equals( "") ? "0" : number);
		if ( numberSpinner.equals( _model_builder_memory_size_numberSpinner))
			_model_builder_memory_size = number;
	}

	/**
	 * 
	 */
	private void setup_simulation_checkBox() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_simulation_checkBox = new JCheckBox(
			ResourceManager.get_instance().get( "plugin.tsubame2.dialog.simulation"),
			Environment.get_instance().get( Environment._simulation_key, "true").equals( "true"));
		_simulation_checkBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_labels[ 2].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_script_directory_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_script_directory_selector_button.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_labels[ 5].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_number_of_times_numberSpinner.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _simulation_checkBox);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_number_of_times_numberSpinner() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_labels[ 5] = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.dialog.number.of.times"), SwingConstants.RIGHT);
		panel.add( _labels[ 5]);


		_number_of_times_numberSpinner = new NumberSpinner();
		_number_of_times_numberSpinner.set_minimum( 1);
		_number_of_times_numberSpinner.set_maximum( Constant._max_number_of_times);
		Integer number_of_times = new Integer(
			Environment.get_instance().get( Environment._number_of_times_for_grid_key, "1"));
		_number_of_times_numberSpinner.set_value( number_of_times.intValue());
		_number_of_times_numberSpinner.setPreferredSize( new Dimension( 200,
			_number_of_times_numberSpinner.getPreferredSize().height));
		panel.add( _number_of_times_numberSpinner);


		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_log_analysis_checkBox() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_log_analysis_checkBox = new JCheckBox(
			ResourceManager.get_instance().get( "plugin.tsubame2.dialog.log.analysis"),
			Environment.get_instance().get( Environment._log_analysis_key, "false").equals( "true"));
		_log_analysis_checkBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_labels[ 6].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_log_analysis_condition_filename_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_log_analysis_condition_file_selector_button.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _log_analysis_checkBox);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_log_analysis_condition_filename_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_labels[ 6] = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.dialog.log.analysis.condition.filename"), SwingConstants.RIGHT);
		//_labels[ 6].setVerticalAlignment( SwingConstants.TOP);
		panel.add( _labels[ 6]);


		_log_analysis_condition_filename_textField = new JTextField(
			Environment.get_instance().get( Environment._log_analysis_condition_filename_key, ""));
		_log_analysis_condition_filename_textField.setPreferredSize( new Dimension( 400,
			_log_analysis_condition_filename_textField.getPreferredSize().height));
		panel.add( _log_analysis_condition_filename_textField);


		_log_analysis_condition_file_selector_button = new JButton( ResourceManager.get_instance().get( "plugin.tsubame2.dialog.log.analysis.condition.filename.reference"));
		_log_analysis_condition_file_selector_button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_log_analysis_condition_file_selector_button_actionPerformed( arg0);
			}
		});
		panel.add( _log_analysis_condition_file_selector_button);


		getContentPane().add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_log_analysis_condition_file_selector_button_actionPerformed(ActionEvent actionEvent) {
		File file = Tool.get_file( _log_analysis_condition_filename_textField.getText(),
			ResourceManager.get_instance().get( "plugin.tsubame2.log.analysis.condition.file.selector.dialog.title"),
			new String[] { "groovy"},
			"Log analysis condition file",
			this);
		if ( null == file)
			return;

		_log_analysis_condition_filename_textField.setText( file.getAbsolutePath());
	}

	/**
	 * 
	 */
	private void setup_transfer_log_checkBox() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_transfer_log_checkBox = new JCheckBox(
			ResourceManager.get_instance().get( "plugin.tsubame2.dialog.log.transfer"),
			Environment.get_instance().get( Environment._log_transfer_key, "false").equals( "true"));
		_transfer_log_checkBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_labels[ 7].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_local_log_directory_textField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_local_log_directory_selector_button.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _transfer_log_checkBox);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_local_log_directory_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_labels[ 7] = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.dialog.local.log.directory"), SwingConstants.RIGHT);
		panel.add( _labels[ 7]);


		_local_log_directory_textField = new JTextField(
			Environment.get_instance().get( Environment._local_log_directory_key, ""));
		_local_log_directory_textField.setEditable( false);
		_local_log_directory_textField.setPreferredSize( new Dimension( 400,
			_local_log_directory_textField.getPreferredSize().height));
		panel.add( _local_log_directory_textField);


		_local_log_directory_selector_button = new JButton( ResourceManager.get_instance().get( "plugin.tsubame2.dialog.local.log.reference"));
		_local_log_directory_selector_button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_local_log_directory_selector_button_actionPerformed( arg0);
			}
		});
		panel.add( _local_log_directory_selector_button);


		getContentPane().add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_local_log_directory_selector_button_actionPerformed(ActionEvent actionEvent) {
		File directory = Tool.get_directory( _local_log_directory_textField.getText(),
			ResourceManager.get_instance().get( "plugin.tsubame2.local.log.directory.selector.dialog.title"), this);
		if ( null == directory)
			return;

		_local_log_directory_textField.setText( directory.getAbsolutePath());
	}

	/**
	 * 
	 */
	private void setup_error_log_textArea() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.dialog.error.log"));
		panel.add( label);

		getContentPane().add( panel);


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_error_log_textArea = new JTextArea();
		_error_log_textArea.setEditable( false);
		_error_log_textArea.setTabSize( 2);

		_error_log_textArea_scrollPane = new JScrollPane();
		_error_log_textArea_scrollPane.getViewport().setView( _error_log_textArea);
		panel.add( _error_log_textArea_scrollPane);

		getContentPane().add( panel);
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

		_labels[ 2].setEnabled( _simulation_checkBox.isSelected());
		_script_directory_textField.setEnabled( _simulation_checkBox.isSelected());
		_script_directory_selector_button.setEnabled( _simulation_checkBox.isSelected());
		_labels[ 5].setEnabled( _simulation_checkBox.isSelected());
		_number_of_times_numberSpinner.setEnabled( _simulation_checkBox.isSelected());

		_labels[ 6].setEnabled( _log_analysis_checkBox.isSelected());
		_log_analysis_condition_filename_textField.setEnabled( _log_analysis_checkBox.isSelected());
		_log_analysis_condition_file_selector_button.setEnabled( _log_analysis_checkBox.isSelected());

		_labels[ 7].setEnabled( _transfer_log_checkBox.isSelected());
		_local_log_directory_textField.setEnabled( _transfer_log_checkBox.isSelected());
		_local_log_directory_selector_button.setEnabled( _transfer_log_checkBox.isSelected());

		_error_log_textArea_scrollPane.setPreferredSize( new Dimension(
			width + 410 + _local_log_directory_selector_button.getPreferredSize().width, 100));
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		if ( null != _experimentManager
			&& _simulation_checkBox.isSelected()
			&& !edit_experimentManager())
			return;


		String script_directory = _script_directory_textField.getText();
		if ( null == script_directory || script_directory.equals( ""))
			return;

		Environment.get_instance().set( Environment._script_directory_key, script_directory);


		String log_directory = _log_directory_textField.getText();
		if ( null == log_directory || log_directory.equals( ""))
			return;

		Environment.get_instance().set( Environment._log_directory_key, log_directory);


		Environment.get_instance().set(
			Environment._model_builder_memory_size_key, _model_builder_memory_size);


		Environment.get_instance().set( Environment._simulation_key,
			_simulation_checkBox.isSelected() ? "true" : "false");

		int number_of_times = _number_of_times_numberSpinner.get_value();
		Environment.get_instance().set(
			Environment._number_of_times_for_grid_key, String.valueOf( number_of_times));


		Environment.get_instance().set( Environment._log_analysis_key,
			_log_analysis_checkBox.isSelected() ? "true" : "false");

		String log_analysis_condition_filename = _log_analysis_condition_filename_textField.getText();
		if ( _log_analysis_checkBox.isSelected()
			&& ( null == log_analysis_condition_filename || log_analysis_condition_filename.equals( "")))
			return;

		Environment.get_instance().set(
			Environment._log_analysis_condition_filename_key, log_analysis_condition_filename);


		String local_log_directory = _local_log_directory_textField.getText();
		if ( _transfer_log_checkBox.isSelected()
			&& ( null == local_log_directory || local_log_directory.equals( "")))
			return;

		//local_log_directory = local_log_directory.replaceAll( "\\\\", "/");

		Environment.get_instance().set( Environment._log_transfer_key,
			_transfer_log_checkBox.isSelected() ? "true" : "false");
		Environment.get_instance().set( Environment._local_log_directory_key, local_log_directory);


		if ( Environment.get_instance().get( Environment._simulation_key, "false").equals( "false")
			&& Environment.get_instance().get( Environment._log_analysis_key, "false").equals( "false")
			&& Environment.get_instance().get( Environment._log_transfer_key, "false").equals( "false"))
			return;

		setEnabled( false);
		if ( !clear( script_directory, log_directory, _transfer_log_checkBox.isSelected() ? local_log_directory : null)) {
			setEnabled( true);
			return;
		}

		super.on_ok(actionEvent);
	}

	/**
	 * @return
	 */
	private boolean edit_experimentManager() {
		Class cls = null;
		try {
			cls = Class.forName( "soars.application.visualshell.object.experiment.ExperimentManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		if ( null == cls)
			return false;

		List resultList = new ArrayList();
		if ( !Reflection.execute_class_method( _frame, "edit_experimentManager",
			new Class[] { cls, String.class, Component.class},
			new Object[] { _experimentManager, ResourceManager.get_instance().get( "plugin.tsubame2.dialog.title"), this},
			resultList)) {
			JOptionPane.showMessageDialog( _frame,
				"Plugin error : Reflection.execute_class_method( ... )" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator
				+ " Class name : soars.application.visualshell.main.MainFrame" + Constant._lineSeparator
				+ " Method name : edit_experimentManager" + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.tsubame2.frame.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( resultList.isEmpty() || null == resultList.get( 0) || !Boolean.class.isInstance( resultList.get( 0)))
			return false;

		return ( ( Boolean)resultList.get( 0)).booleanValue();
	}

	/**
	 * @param script_directory
	 * @param log_directory
	 * @param local_log_directory
	 * @return
	 */
	private boolean clear(String script_directory, String log_directory, String local_log_directory) {
		if ( Environment.get_instance().get( Environment._simulation_key, "false").equals( "true")) {
			SshClient sshClient = SshTool.getSshClient( _parameters._host, _parameters._username, _parameters._password);
			if ( null == sshClient)
				return false;

			SftpClient sftpClient = SshTool.getSftpClient( sshClient);
			if ( null == sftpClient) {
				sshClient.disconnect();
				return false;
			}

			if ( !SshTool.delete( sftpClient, script_directory, false)) {
				SshTool.close( sftpClient);
				sshClient.disconnect();
				_error_log_textArea.append( "Could not clear! : " + script_directory + "\n");
				return false;
			}

			if ( !SshTool.delete( sftpClient, log_directory, false)) {
				SshTool.close( sftpClient);
				sshClient.disconnect();
				_error_log_textArea.append( "Could not clear! : " + log_directory + "\n");
				return false;
			}

			SshTool.close( sftpClient);
			sshClient.disconnect();
		}

		if ( Environment.get_instance().get( Environment._log_transfer_key, "false").equals( "true")
			&& null != local_log_directory) {
			if ( !FileUtility.delete( new File( local_log_directory), false)) {
				_error_log_textArea.append( "Could not clear! : " + local_log_directory + "\n");
				return false;
			}
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_cancel(java.awt.event.ActionEvent)
	 */
	protected void on_cancel(ActionEvent actionEvent) {
		super.on_cancel(actionEvent);
	}
}
