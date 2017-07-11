/*
 * Created on 2006/06/20
 */
package soars.plugin.visualshell.tsubame2.main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import soars.common.utility.swing.window.Frame;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.reflection.Reflection;
import soars.common.utility.tool.resource.Resource;
import soars.common.utility.tool.ssh.SshTool;
import soars.common.utility.tool.timer.ITimerTaskImplementCallback;
import soars.common.utility.tool.timer.TimerTaskImplement;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.sftp.SftpFile;

/**
 * @author kurata
 */
public class GridFrame extends Frame implements Runnable, ITimerTaskImplementCallback {

	/**
	 * 
	 */
	private String _name = "";

	/**
	 * 
	 */
	private Component _parent = null;

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
	private JTextField _log_directory_textField = null;

	/**
	 * 
	 */
	private JTextField _model_builder_memory_size_textField = null;

	/**
	 * 
	 */
	private JTextField _number_of_times_textField = null;

	/**
	 * 
	 */
	private JTextField _log_analysis_condition_filename_textField = null;

	/**
	 * 
	 */
	private JTextField _local_log_directory_textField = null;

	/**
	 * 
	 */
	private JTextField _number_of_scripts_textField = null;

	/**
	 * 
	 */
	private JTextField _number_of_submitted_scripts_textField = null;

	/**
	 * 
	 */
	private JTextField _status_textField = null;

	/**
	 * 
	 */
	private JProgressBar _progressBar = null;

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
	private JButton _stop_button = null;

	/**
	 * 
	 */
	private JLabel[] _labels = new JLabel[] {
		null, null, null, null,
		null, null, null, null,
		null, null, null, null,
		null
	};

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
	private Environment _environment = null;

	/**
	 * 
	 */
	private Thread _thread = null;

	/**
	 * 
	 */
	private int _number = 0;

	/**
	 * 
	 */
	private TreeMap _analysis_shell_script_directory_map = new TreeMap();

	/**
	 * 
	 */
	private List _id_list = new ArrayList();

	/**
	 * 
	 */
	private int _id = 0;

	/**
	 * 
	 */
	private List<String> _job_id_list = new ArrayList<String>();

	/**
	 * 
	 */
	private Timer _timer = null;

	/**
	 * 
	 */
	private TimerTaskImplement _timerTaskImplement = null;

	/**
	 * 
	 */
	private final long _delay = 0;

	/**
	 * 
	 */
	protected boolean _stop = false;

	/**
	 * 
	 */
	protected boolean _termination = false;

	/**
	 * 
	 */
	private boolean _running = false;

	/**
	 * 
	 */
	private String _status = "none";

	/**
	 * 
	 */
	private String _uuid = "";

	/**
	 * 
	 */
	private long _wait = 10;

	/**
	 * 
	 */
	// TODO Auto-generated method stub
	private boolean _exist_user_data_directory = false;

	/**
	 * @param arg0
	 * @param name
	 * @param parent
	 * @param parameters
	 * @param experimentManager
	 * @param environment 
	 * @throws HeadlessException
	 */
	public GridFrame(String arg0, String name, Component parent, Parameters parameters, Object experimentManager, Environment environment) throws HeadlessException {
		super(arg0);
		_name = name;
		_parent = parent;
		_parameters = parameters;
		_experimentManager = experimentManager;
		_environment = environment;
	}

	/**
	 * @return
	 */
	private boolean get_guid() {
		String osname = System.getProperty( "os.name");
		_uuid = Tool.get_uuid( ( 0 <= osname.indexOf( "Windows"))
			? "../utility/win/uuidgen.exe"
			: "/usr/bin/uuidgen");
		return ( ( null != _uuid) ? true : false);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;


		if ( !get_guid())
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

		setup_model_builder_memory_size_textField();

		insert_horizontal_glue();

		setup_number_of_times_textField();

		insert_horizontal_glue();

		setup_log_analysis_condition_filename_textField();

		insert_horizontal_glue();

		setup_local_log_directory_textField();

		insert_horizontal_glue();

		setup_number_of_scripts_textField();

		insert_horizontal_glue();

		setup_number_of_submitted_scripts_textField();

		insert_horizontal_glue();

		setup_status_textField();

		insert_horizontal_glue();

		setup_progressBar();

		insert_horizontal_glue();

		setup_error_log_textArea();

		insert_horizontal_glue();

		setup_stop_button();

		insert_horizontal_glue();


		adjust();


		pack();

		setLocationRelativeTo( _parent);

		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

		Toolkit.getDefaultToolkit().setDynamicLayout( true);

		setVisible( true);


		Image image = Resource.load_image_from_resource(
			Constant._resource_directory + "/image/icon/icon.png", getClass());
		if ( null != image)
			setIconImage( image);


		_thread = new Thread( this);
		_thread.start();

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_window_closing(java.awt.event.WindowEvent)
	 */
	protected void on_window_closing(WindowEvent windowEvent) {
		if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog( this,
			ResourceManager.get_instance().get( "plugin.tsubame2.frame.exit.confirm.message"), getTitle(), JOptionPane.YES_NO_OPTION))
			return;

		stop_timer();
		_termination = true;
		on_stop( null);
		dispose();
	}

	/**
	 * 
	 */
	private void setup_host_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_labels[ 0] = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.frame.gird.portal.ip.address"), SwingConstants.RIGHT);
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

		_labels[ 1] = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.frame.username"), SwingConstants.RIGHT);
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

		_labels[ 2] = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.frame.script.directory"), SwingConstants.RIGHT);
		_labels[ 2].setEnabled( _environment.get( Environment._simulation_key, "false").equals( "true"));
		panel.add( _labels[ 2]);


		_script_directory_textField = new JTextField(
			_environment.get( Environment._simulation_key, "false").equals( "true")
				? _environment.get( Environment._script_directory_key, "")
				: "");
		_script_directory_textField.setEditable( false);
		_script_directory_textField.setPreferredSize( new Dimension( 400,
			_script_directory_textField.getPreferredSize().height));
		panel.add( _script_directory_textField);


		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_log_directory_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_labels[ 3] = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.frame.log.directory"), SwingConstants.RIGHT);
		panel.add( _labels[ 3]);


		_log_directory_textField = new JTextField(
			_environment.get( Environment._log_directory_key, ""));
		_log_directory_textField.setEditable( false);
		_log_directory_textField.setPreferredSize( new Dimension( 400,
			_log_directory_textField.getPreferredSize().height));
		panel.add( _log_directory_textField);


		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_model_builder_memory_size_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_labels[ 4] = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.frame.model.builder.memory.size"), SwingConstants.RIGHT);
		panel.add( _labels[ 4]);

		_model_builder_memory_size_textField = new JTextField(
			_environment.get( Environment._model_builder_memory_size_key, Constant._defaultMemorySize));
		_model_builder_memory_size_textField.setHorizontalAlignment( SwingConstants.RIGHT);
		_model_builder_memory_size_textField.setEditable( false);
		_model_builder_memory_size_textField.setPreferredSize( new Dimension( 200,
			_model_builder_memory_size_textField.getPreferredSize().height));
		panel.add( _model_builder_memory_size_textField);

		JLabel label = new JLabel( "MB");
		panel.add( label);


		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_number_of_times_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_labels[ 5] = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.frame.number.of.times"), SwingConstants.RIGHT);
		_labels[ 5].setEnabled( _environment.get( Environment._simulation_key, "false").equals( "true"));
		panel.add( _labels[ 5]);


		_number_of_times_textField = new JTextField(
			_environment.get( Environment._simulation_key, "false").equals( "true")
				? _environment.get( Environment._number_of_times_for_grid_key, "1")
				: "");
		_number_of_times_textField.setHorizontalAlignment( SwingConstants.RIGHT);
		_number_of_times_textField.setEditable( false);
		_number_of_times_textField.setPreferredSize( new Dimension( 200,
			_number_of_times_textField.getPreferredSize().height));
		panel.add( _number_of_times_textField);


		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_log_analysis_condition_filename_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_labels[ 6] = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.frame.log.analysis.condition.filename"), SwingConstants.RIGHT);
		_labels[ 6].setEnabled( _environment.get( Environment._log_analysis_key, "false").equals( "true"));
		panel.add( _labels[ 6]);


		_log_analysis_condition_filename_textField = new JTextField(
			_environment.get( Environment._log_analysis_key, "false").equals( "true")
				? _environment.get( Environment._log_analysis_condition_filename_key, "")
				: "");
		_log_analysis_condition_filename_textField.setEditable( false);
		_log_analysis_condition_filename_textField.setPreferredSize( new Dimension( 400,
			_log_analysis_condition_filename_textField.getPreferredSize().height));
		panel.add( _log_analysis_condition_filename_textField);


		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_local_log_directory_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_labels[ 7] = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.frame.local.log.directory"), SwingConstants.RIGHT);
		_labels[ 7].setEnabled( _environment.get( Environment._log_transfer_key, "false").equals( "true"));
		panel.add( _labels[ 7]);


		_local_log_directory_textField = new JTextField(
			_environment.get( Environment._log_transfer_key, "false").equals( "true")
				? _environment.get( Environment._local_log_directory_key, "")
				: "");
		_local_log_directory_textField.setEditable( false);
		_local_log_directory_textField.setPreferredSize( new Dimension( 400,
			_local_log_directory_textField.getPreferredSize().height));
		panel.add( _local_log_directory_textField);


		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_number_of_scripts_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_labels[ 8] = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.frame.number.of.script"), SwingConstants.RIGHT);
		panel.add( _labels[ 8]);


		_number_of_scripts_textField = new JTextField( "0");
		_number_of_scripts_textField.setHorizontalAlignment( SwingConstants.RIGHT);
		_number_of_scripts_textField.setEditable( false);
		_number_of_scripts_textField.setPreferredSize( new Dimension( 200,
			_number_of_scripts_textField.getPreferredSize().height));
		panel.add( _number_of_scripts_textField);


		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_number_of_submitted_scripts_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_labels[ 9] = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.frame.number.of.submitted.script"), SwingConstants.RIGHT);
		panel.add( _labels[ 9]);


		_number_of_submitted_scripts_textField = new JTextField( "0");
		_number_of_submitted_scripts_textField.setHorizontalAlignment( SwingConstants.RIGHT);
		_number_of_submitted_scripts_textField.setEditable( false);
		_number_of_submitted_scripts_textField.setPreferredSize( new Dimension( 200,
			_number_of_submitted_scripts_textField.getPreferredSize().height));
		panel.add( _number_of_submitted_scripts_textField);


		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_status_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_labels[ 10] = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.frame.status"), SwingConstants.RIGHT);
		panel.add( _labels[ 10]);


		_status_textField = new JTextField( "");
		_status_textField.setEditable( false);
		_status_textField.setPreferredSize( new Dimension( 400,
			_status_textField.getPreferredSize().height));
		panel.add( _status_textField);


		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_progressBar() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_labels[ 11] = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.frame.progress"), SwingConstants.RIGHT);
		panel.add( _labels[ 11]);


		_progressBar = new JProgressBar();
		_progressBar.setStringPainted( true);
		_progressBar.setPreferredSize( new Dimension( 400,
			_progressBar.getPreferredSize().height));
		panel.add( _progressBar);


		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_error_log_textArea() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.tsubame2.frame.error.log"));
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
	private void setup_stop_button() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, pad, 0));

		_labels[ 12] = new JLabel( "");
		panel.add( _labels[ 12]);


		_stop_button = new JButton( ResourceManager.get_instance().get( "dialog.cancel"));
		_stop_button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_stop( arg0);
			}
		});
		panel.add( _stop_button);


		getContentPane().add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_stop(ActionEvent actionEvent) {
		if ( !_termination
			&& JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog( this,
				ResourceManager.get_instance().get( "plugin.tsubame2.frame.cancel.confirm.message"), getTitle(), JOptionPane.YES_NO_OPTION))
			return;

		_stop = true;
		_stop_button.setEnabled( false);
		stop_timer();
		setEnabled( false);
		delete();
		setEnabled( true);
		on_stop();
	}

	/**
	 * 
	 */
	private void on_stop() {
		if ( _termination)
			return;

		JOptionPane.showMessageDialog( this,
			ResourceManager.get_instance().get( "plugin.tsubame2.frame.canceled"),
			getTitle(),
			JOptionPane.INFORMATION_MESSAGE);
		_status_textField.setText( "Canceled ...");
		_status_textField.update( _status_textField.getGraphics());
		_error_log_textArea.append( ResourceManager.get_instance().get( "plugin.tsubame2.frame.canceled"));
		setEnabled( true);
		_parent.setEnabled( true);
		//_stop_button.setEnabled( false);
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

		_error_log_textArea_scrollPane.setPreferredSize( new Dimension( width + 405, 100));
	}

	/**
	 * @param sshClient
	 * @param sftpClient
	 * @param directory
	 * @param connection
	 * @param shell_script_file_name
	 * @return
	 */
	private String execute(SshClient sshClient, SftpClient sftpClient, SftpFile directory, Connection connection, String shell_script_file_name) {
		synchronized ( _job_id_list) {
			if ( _termination || _stop)
				return null;

			if ( !SshTool.directory_exists( sftpClient, _parameters._sgeout_directory_name + "/" + _uuid))
				sftpClient.mkdirs( _parameters._sgeout_directory_name + "/" + _uuid);

			if ( !SshTool.directory_exists( sftpClient, _parameters._sgeout_directory_name + "/" + _uuid))
				return null;

			String command = ( _parameters._submit_shell_script_file_name + " "
				+ "'" + Constant._job_name + "' "
				+ "'" + _parameters._sgeout_directory_name + "/" + _uuid + "' "
				+ "'" + directory.getAbsolutePath() + "/" + shell_script_file_name + "'");

			//System.out.println( command);

			String text = "";
			String result = null;
			Session session;
			try {
				session = connection.openSession();
				session.requestPTY( "vt100", 80, 24, 0, 0, null);
				session.startShell();
				try {
					Thread.sleep( 5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( new StreamGobbler( session.getStdout())));
				//TsubameStreamPumper tsubameStreamPumper = new TsubameStreamPumper(
				//	new BufferedReader( new InputStreamReader( new StreamGobbler( session.getStdout()))));
				//tsubameStreamPumper.start();
				PrintWriter printWriter = new PrintWriter( session.getStdin());
				printWriter.println();
				printWriter.flush();
				printWriter.println( command);
				printWriter.flush();
				printWriter.println( "exit");
				printWriter.flush();
				while ( true) {
					String line = null;
					try {
						line = bufferedReader.readLine();
					} catch (IOException e) {
						e.printStackTrace();
						break;
					}

					if ( null == line)
						break;

					//System.out.println( line);
					if ( line.trim().endsWith( "has been submitted"))
					//if ( line.toLowerCase().startsWith( "your job") && line.trim().endsWith( "has been submitted"))
						result = line;

					//System.out.println( line);
					text += ( line + Constant._lineSeparator);
				}

				bufferedReader.close();

				//session.waitForCondition( ChannelCondition.EXIT_SIGNAL, 5000);
				session.close();
				//System.out.println( tsubameStreamPumper._result);
				//return tsubameStreamPumper._result;

				if ( null == result)
					_error_log_textArea.append( text);
				else {
					int index = 0;
					String[] words = result.split( " ");
					if ( words[ 0].toLowerCase().equals( "your") && words[ 1].toLowerCase().equals( "job"))
						index = 2;
					else if ( words[ 1].toLowerCase().equals( "your") && words[ 2].toLowerCase().equals( "job"))
						index = 3;
					else {
						_error_log_textArea.append( text);
						return null;
					}

					double d;
					try {
						d = Double.parseDouble( words[ index]);
					} catch (NumberFormatException e) {
						//e.printStackTrace();
						_error_log_textArea.append( text);
						return null;
					}
					_job_id_list.add( words[ index]);
				}

				return result;
			} catch (IOException e) {
				return null;
			}
		}
	}

	/* (Non Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		if ( _environment.get( Environment._simulation_key, "false").equals( "true")) {
			try {
				if ( !simulate())
					return;
			} catch (Throwable ex) {
				on_failed( "simulate() at run()\n", "simulate() at run()");
				return;
			}

			start_timer();
			return;
		}

		if ( _environment.get( Environment._log_analysis_key, "false").equals( "true")) {
			try {
				if ( !analyze())
					return;
			} catch (Throwable ex) {
				on_failed( "analyze() at run()\n", "analyze() at run()");
				return;
			}

			start_timer();
			return;
		}

		if ( _environment.get( Environment._log_transfer_key, "false").equals( "true")) {
			try {
				if ( !compress())
					return;
			} catch (Throwable ex) {
				on_failed( "compress() at run()\n", "compress() at run()");
				return;
			}

			start_timer();
			return;
		}
	}

	/**
	 * @return
	 */
	private boolean simulate() {
		setEnabled( false);
		_parent.setEnabled( false);

		_number = 0;
		_id = 0;
		synchronized ( _job_id_list) {
			_job_id_list.clear();
		}

		_status_textField.setText( "Simulation : Exporting ...");
		_status_textField.update( _status_textField.getGraphics());

		_number_of_scripts_textField.setText( "0");
		_number_of_scripts_textField.update( _number_of_scripts_textField.getGraphics());

		_number_of_submitted_scripts_textField.setText( "0");
		_number_of_submitted_scripts_textField.update( _number_of_submitted_scripts_textField.getGraphics());

		if ( !export_soars_scripts()) {
			on_failed( "export_soars_scripts() at simulate()\n", "export_soars_scripts() at simulate()");
			return false;
		}

		try {
			Thread.sleep( 1000 * _wait);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		_parent.setEnabled( true);
		setEnabled( true);

		_status_textField.setText( "Simulation : Submitting ...");
		_status_textField.update( _status_textField.getGraphics());

		_progressBar.setMinimum( 0);
		_progressBar.setMaximum( _number);
		_progressBar.setValue( 0);
		_progressBar.update( _progressBar.getGraphics());

		if ( !submit()) {
			on_failed( "submit() at simulate()\n", "submit() at simulate()");
			return false;
		}

		_number = _id_list.size();
		_progressBar.setMinimum( 0);
		_progressBar.setMaximum( _number);
		_progressBar.setValue( 0);
		_progressBar.update( _progressBar.getGraphics());

		_status_textField.setText( "Simulation : Running ...");
		_status_textField.update( _status_textField.getGraphics());

		_status = "simulation";

		return true;
	}

	/**
	 * @return
	 */
	private boolean export_soars_scripts() {
		// TODO Auto-generated method stub
		List resultList = new ArrayList();
		if ( !Reflection.execute_static_method( "soars.application.visualshell.layer.LayerManager", "get_instance", new Class[ 0], new Object[ 0], resultList)) {
			JOptionPane.showMessageDialog( this,
				"Plugin error : Reflection.execute_static_method( ... )" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator
				+ " Class name : soars.application.visualshell.layer.LayerManager" + Constant._lineSeparator
				+ " Method name : get_instance" + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.tsubame2.frame.title"),
			JOptionPane.ERROR_MESSAGE);
			return false;
		}

		Class cls = null;
		try {
			cls = Class.forName( "soars.application.visualshell.layer.LayerManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		if ( null == cls)
			return false;

		Object object = null;
		if ( !resultList.isEmpty() && null != resultList.get( 0) && cls.isInstance( resultList.get( 0)))
			object = resultList.get( 0);
		else {
			JOptionPane.showMessageDialog( this,
				"Plugin error : Reflection.execute_static_method( ... )" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator
				+ " Class name : soars.application.visualshell.layer.LayerManager" + Constant._lineSeparator
				+ " Method name : get_instance" + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.tsubame2.frame.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}



		resultList.clear();



		if ( !Reflection.execute_class_method( object, "exist_user_data_directory", new Class[ 0], new Object[ 0], resultList)
			|| resultList.isEmpty() || null == resultList.get( 0) || !( resultList.get( 0) instanceof Boolean)) {
			JOptionPane.showMessageDialog( this,
				"Plugin error : Reflection.execute_class_method( ... )" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator
				+ " Class name : soars.application.visualshell.layer.LayerManager" + Constant._lineSeparator
				+ " Method name : exist_user_data_directory" + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.tsubame2.frame.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		Boolean result = ( Boolean)resultList.get( 0);
		_exist_user_data_directory = result.booleanValue();



		resultList.clear();



		try {
			cls = Class.forName( "soars.application.visualshell.object.experiment.ExperimentManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		if ( null == cls)
			return false;

		IntBuffer intBuffer = IntBuffer.allocate( 1);
		intBuffer.put( 0, 0);

		if ( !Reflection.execute_class_method( object, "export_data_for_grid",
			new Class[] { cls, String.class, String.class, String.class, int.class, String.class, String.class, String.class, IntBuffer.class, JTextField.class},
			new Object[] { _experimentManager,
				_environment.get( Environment._script_directory_key, ""),
				_environment.get( Environment._log_directory_key, ""),
				_parameters._program_directory_name,
				new Integer( _environment.get( Environment._number_of_times_for_grid_key, "1")),
				_parameters._host,
				_parameters._username,
				_parameters._password,
				intBuffer,
				_number_of_scripts_textField},
			resultList)
			|| resultList.isEmpty() || null == resultList.get( 0) || !( resultList.get( 0) instanceof Boolean) || !(( Boolean)resultList.get( 0)).booleanValue()) {
			JOptionPane.showMessageDialog( this,
				"Plugin error : Reflection.execute_class_method( ... )" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator
				+ " Class name : soars.application.visualshell.layer.LayerManager" + Constant._lineSeparator
				+ " Method name : export_data_for_grid" + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.tsubame2.frame.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		_number = intBuffer.get( 0);

		return true;
	}

	/**
	 * @return
	 */
	private boolean submit() {
		SshClient sshClient = SshTool.getSshClient( _parameters._host, _parameters._username, _parameters._password);
		if ( null == sshClient) {
			JOptionPane.showMessageDialog( this,
				"Plugin error : submit" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.tsubame2.frame.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		SftpClient sftpClient = SshTool.getSftpClient( sshClient);
		if ( null == sftpClient) {
			sshClient.disconnect();
			JOptionPane.showMessageDialog( this,
				"Plugin error : submit" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.tsubame2.frame.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( !SshTool.directory_exists( sftpClient, _environment.get( Environment._script_directory_key, ""))) {
			SshTool.close( sftpClient);
			sshClient.disconnect();
			JOptionPane.showMessageDialog( this,
				"Plugin error : submit" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.tsubame2.frame.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		Connection connection = new Connection( _parameters._host, 22);
		try {
			connection.connect();
		} catch (IOException e) {
			SshTool.close( sftpClient);
			sshClient.disconnect();
			JOptionPane.showMessageDialog( this,
				"Plugin error : submit" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.tsubame2.frame.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		try {
			if ( !connection.authenticateWithPassword( _parameters._username, _parameters._password)) {
				connection.close();
				SshTool.close( sftpClient);
				sshClient.disconnect();
				JOptionPane.showMessageDialog( this,
					"Plugin error : submit" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator,
					ResourceManager.get_instance().get( "plugin.tsubame2.frame.title"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		} catch (IOException e) {
			connection.close();
			SshTool.close( sftpClient);
			sshClient.disconnect();
			JOptionPane.showMessageDialog( this,
				"Plugin error : submit" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.tsubame2.frame.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( !submit( sshClient, sftpClient,
			new SftpFile( _environment.get( Environment._script_directory_key, "")), connection)) {
			connection.close();
			SshTool.close( sftpClient);
			sshClient.disconnect();
			return false;
		}

		connection.close();

		SshTool.close( sftpClient);
		sshClient.disconnect();

		return true;
	}

	/**
	 * @param sshClient
	 * @param sftpClient
	 * @param directory
	 * @param connection
	 * @return
	 */
	private boolean submit(SshClient sshClient, SftpClient sftpClient, SftpFile directory, Connection connection) {
		List files = SshTool.ls( sftpClient, directory.getAbsolutePath());
		if ( null == files)
			return false;

		for ( int i = 0; i < files.size(); ++i) {
			if ( _stop)
				break;

			SftpFile sftpFile = ( SftpFile)files.get( i);
			if ( sftpFile.getAbsolutePath().endsWith( "."))
				continue;

			if ( sftpFile.isDirectory()) {
				if ( !submit( sshClient, sftpClient, sftpFile, connection))
					return false;
			} else if ( sftpFile.isFile() && sftpFile.getFilename().equals( Constant._soarsScriptFilename)) {
				if ( !submit( sshClient, sftpClient, directory, sftpFile.getAbsolutePath(), connection))
					return false;
			}
		}

		return true;
	}

	/**
	 * @param sshClient
	 * @param sftpClient
	 * @param directory
	 * @param filename
	 * @param connection
	 * @return
	 */
	private boolean submit(SshClient sshClient, SftpClient sftpClient, SftpFile directory, String filename, Connection connection) {
		if ( !export_for_simulation( sftpClient, directory, filename))
			return false;

		_id_list.add( String.valueOf( _id++));

		String result = execute( sshClient, sftpClient, directory, connection, Constant._simulation_shell_script_file_name);
		if ( null == result)
			return false;

		_number_of_submitted_scripts_textField.setText( String.valueOf( _id_list.size()));
		_number_of_submitted_scripts_textField.update( _number_of_submitted_scripts_textField.getGraphics());

		_progressBar.setValue( _id_list.size());
		_progressBar.update( _progressBar.getGraphics());

		return true;
	}

	/**
	 * @param sftpClient
	 * @param directory
	 * @param filename
	 * @return
	 */
	private boolean export_for_simulation(SftpClient sftpClient, SftpFile directory, String filename) {
		// TODO Auto-generated method stub
		File simulation_shell_script_file;
		try {
			simulation_shell_script_file = File.createTempFile( "simulation_", ".sh");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter( simulation_shell_script_file);
		} catch (IOException e) {
			e.printStackTrace();
			simulation_shell_script_file.delete();
			return false;
		}

		try {
			fileWriter.write( "#!/bin/sh\n\n");
			fileWriter.write( "JAVA_HOME=" + _parameters._java_home + "\n\n");

//			fileWriter.write( "export PATH=" + _parameters._java_home + "/bin:$PATH\n");
//			fileWriter.write( "export JRE_HOME=" + _parameters._java_home + "\n");
//			fileWriter.write( "export JAVA_BINDER=" + _parameters._java_home + "/bin\n");
//			fileWriter.write( "export JAVA_HOME=" + _parameters._java_home + "\n");
//			fileWriter.write( "export JAVA_ROOT=" + _parameters._home_directory_name + "/program/java\n");

			String model_builder_memory_size = _environment.get( Environment._model_builder_memory_size_key, Constant._defaultMemorySize);
			model_builder_memory_size = ( model_builder_memory_size.equals( "") ? "0" : model_builder_memory_size);

			if ( _exist_user_data_directory) {
				String script_directory_name = _environment.get( Environment._script_directory_key, "");
				String parent_directory_name = _environment.get( Environment._log_directory_key, "") + directory.getAbsolutePath().substring( script_directory_name.length());
				String user_data_directory_name = parent_directory_name + "/" + Constant._userDataDirectory;

				fileWriter.write( "$JAVA_HOME/bin/java -jar "
					+ "'" + _parameters._executor_jar_filename + "' "
					+ "decompression "
					+ "'" + _parameters._home_directory_name + "' "
					+ "'" + _parameters._sgeout_directory_name + "/" + _uuid + "/" + Constant._decompression_name + "' "
					+ String.valueOf( _id) + " "
					+ "'" + _parameters._java_home + "' "
					+ model_builder_memory_size + " "
					+ "'" + script_directory_name + "/" + Constant._userDataZipFilename + "' "
					+ "'" + parent_directory_name + "' "
					+ "$@\n\n"
					+ "if [ -d \"" + user_data_directory_name + "\" ];\n"
					+ "then\n"
					+ "\techo 'decompression completed!'\n"
					//+ "\techo 'completed!' > \"" + _parameters._sgeout_directory_name + "/" + _uuid + "/" + Constant._decompression_name + "." + String.valueOf( _id) + "\"\n"
					+ "else\n"
					+ "\techo 'error!' > \"" + _parameters._sgeout_directory_name + "/" + _uuid + "/" + Constant._decompression_name + "." + String.valueOf( _id) + "\"\n"
					+ "\texit 0\n"
					+ "fi\n\n");

				fileWriter.write( "$JAVA_HOME/bin/java -jar "
//					fileWriter.write( _parameters._java_home + "/bin/java -jar "
					+ "'" + _parameters._executor_jar_filename + "' "
					+ "simulation "
					+ "'" + _parameters._home_directory_name + "' "
					+ "'" + _parameters._sgeout_directory_name + "/" + _uuid + "/" + Constant._termination_name + "' "
					+ String.valueOf( _id) + " "
					+ "'" + _parameters._java_home + "' "
					+ model_builder_memory_size + " "
					+ "'" + filename + "' "
					+ "'" + user_data_directory_name + "' "
					+ "$@\n\n"
					+ "if [ -f \"" + _parameters._sgeout_directory_name + "/" + _uuid + "/" + Constant._termination_name + "." + String.valueOf( _id) + "\" ];\n"
					+ "then\n"
					+ "\texit 0\n"
					+ "else\n"
					+ "\techo 'error!' > \"" + _parameters._sgeout_directory_name + "/" + _uuid + "/" + Constant._termination_name + "." + String.valueOf( _id) + "\"\n"
					+ "fi\n\n");
			} else {
				fileWriter.write( "$JAVA_HOME/bin/java -jar "
//					fileWriter.write( _parameters._java_home + "/bin/java -jar "
					+ "'" + _parameters._executor_jar_filename + "' "
					+ "simulation "
					+ "'" + _parameters._home_directory_name + "' "
					+ "'" + _parameters._sgeout_directory_name + "/" + _uuid + "/" + Constant._termination_name + "' "
					+ String.valueOf( _id) + " "
					+ "'" + _parameters._java_home + "' "
					+ model_builder_memory_size + " "
					+ "'" + filename + "' "
					+ "$@\n\n"
					+ "if [ -f \"" + _parameters._sgeout_directory_name + "/" + _uuid + "/" + Constant._termination_name + "." + String.valueOf( _id) + "\" ];\n"
					+ "then\n"
					+ "\texit 0\n"
					+ "else\n"
					+ "\techo 'error!' > \"" + _parameters._sgeout_directory_name + "/" + _uuid + "/" + Constant._termination_name + "." + String.valueOf( _id) + "\"\n"
					+ "fi\n\n");
			}

			fileWriter.write( "exit 0\n");
		} catch (IOException e) {
			e.printStackTrace();
			simulation_shell_script_file.delete();
			return false;
		}

		try {
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			simulation_shell_script_file.delete();
			return false;
		}

		try {
			sftpClient.put( simulation_shell_script_file.getAbsolutePath(), directory.getAbsolutePath() + "/" + Constant._simulation_shell_script_file_name);
		} catch (IOException e) {
			e.printStackTrace();
			simulation_shell_script_file.delete();
			return false;
		}

		simulation_shell_script_file.delete();

		try {
			sftpClient.chmod( 0744, directory.getAbsolutePath() + "/" + Constant._simulation_shell_script_file_name);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @return
	 */
	private boolean analyze() {
		synchronized ( _job_id_list) {
			_job_id_list.clear();
		}

		SshClient sshClient = SshTool.getSshClient( _parameters._host, _parameters._username, _parameters._password);
		if ( null == sshClient) {
			on_failed( "SshTool.getSshClient( ... ) at analyze()\n",
				"Plugin error : analyze" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator);
			return false;
		}

		SftpClient sftpClient = SshTool.getSftpClient( sshClient);
		if ( null == sftpClient) {
			sshClient.disconnect();
			on_failed( "SshTool.getSftpClient( ... ) at analyze()\n",
				"Plugin error : analyze" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator);
			return false;
		}

		if ( !SshTool.directory_exists( sftpClient, _environment.get( Environment._log_directory_key, ""))) {
			SshTool.close( sftpClient);
			sshClient.disconnect();
			on_failed( "SshTool.directory_exists( ... ) at analyze()\n",
				"Plugin error : analyze" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator);
			return false;
		}

		Connection connection = new Connection( _parameters._host, 22);
		try {
			connection.connect();
		} catch (IOException e) {
			SshTool.close( sftpClient);
			sshClient.disconnect();
			on_failed( "connection.connect() at analyze()\n",
				"Plugin error : analyze" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator);
			return false;
		}

		try {
			if ( !connection.authenticateWithPassword( _parameters._username, _parameters._password)) {
				connection.close();
				SshTool.close( sftpClient);
				sshClient.disconnect();
				on_failed( "connection.authenticateWithPassword( ... ) at analyze()\n",
					"Plugin error : analyze" + Constant._lineSeparator
						+ " Plugin name : " + _name + Constant._lineSeparator);
				return false;
			}
		} catch (IOException e) {
			connection.close();
			SshTool.close( sftpClient);
			sshClient.disconnect();
			on_failed( "connection.authenticateWithPassword( ... ) at analyze()\n",
				"Plugin error : analyze" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator);
			return false;
		}

		if ( !analyze( sshClient, sftpClient, connection)) {
			connection.close();
			SshTool.close( sftpClient);
			sshClient.disconnect();
			return false;
		}

		connection.close();

		SshTool.close( sftpClient);
		sshClient.disconnect();

		return true;
	}

	/**
	 * @param sshClient
	 * @param sftpClient
	 * @param connection
	 * @return
	 */
	private boolean analyze(SshClient sshClient, SftpClient sftpClient, Connection connection) {
		synchronized ( _job_id_list) {
			_job_id_list.clear();
		}

		_parent.setEnabled( false);
		setEnabled( false);

		_number = 0;

		_status_textField.setText( "Analysis : Exporting ...");
		_status_textField.update( _status_textField.getGraphics());

		_number_of_scripts_textField.setText( "0");
		_number_of_scripts_textField.update( _number_of_scripts_textField.getGraphics());

		_number_of_submitted_scripts_textField.setText( "0");
		_number_of_submitted_scripts_textField.update( _number_of_submitted_scripts_textField.getGraphics());

		if ( !export_for_analysis( sftpClient)) {
			on_failed( "export_for_analysis( ... ) at analyze( ... )\n", "execute( ... ) at analyze( ... )", connection);
			return false;
		}

		try {
			Thread.sleep( 1000 * _wait);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		setEnabled( true);
		_parent.setEnabled( true);

		_status_textField.setText( "Analysis : Submitting ...");
		_status_textField.update( _status_textField.getGraphics());

		_progressBar.setMinimum( 0);
		_progressBar.setMaximum( _number);
		_progressBar.setValue( 0);
		_progressBar.update( _progressBar.getGraphics());

		Iterator iterator = _analysis_shell_script_directory_map.entrySet().iterator();

		while ( iterator.hasNext()) {
			if ( _stop)
				break;

			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String id = ( String)entry.getKey();
			String analysis_shell_script_directory = ( String)entry.getValue();

			String result = execute( sshClient, sftpClient,
				new SftpFile( analysis_shell_script_directory),
				connection,
				Constant._analysis_shell_script_file_name);
			if ( null == result) {
				on_failed( "execute( ... ) at analyze( ... )\n", "execute( ... ) at analyze( ... )", connection);
				return false;
			}

			_id_list.add( id);

			_number_of_submitted_scripts_textField.setText( String.valueOf( _id_list.size()));
			_number_of_submitted_scripts_textField.update( _number_of_submitted_scripts_textField.getGraphics());

			_progressBar.setValue( _id_list.size());
			_progressBar.update( _progressBar.getGraphics());
		}

		_number = _id_list.size();
		_progressBar.setMinimum( 0);
		_progressBar.setMaximum( _number);
		_progressBar.setValue( 0);
		_progressBar.update( _progressBar.getGraphics());

		_status_textField.setText( "Analysis : Running ...");
		_status_textField.update( _status_textField.getGraphics());

		_status = "analysis";

		return true;
	}

	/**
	 * @param sftpClient
	 * @return
	 */
	private boolean export_for_analysis(SftpClient sftpClient) {
		if ( !SshTool.directory_exists( sftpClient, _environment.get( Environment._log_directory_key, "")))
			return false;

		File log_analysis_condition_file = new File( _environment.get( Environment._log_analysis_condition_filename_key, ""));
		if ( null == log_analysis_condition_file || !log_analysis_condition_file.exists() || !log_analysis_condition_file.isFile())
			return false;

		if ( !export_for_analysis( sftpClient, new SftpFile( _environment.get( Environment._log_directory_key, "")), log_analysis_condition_file))
			return false;

		_number = _analysis_shell_script_directory_map.size();

		return true;
	}

	/**
	 * @param sftpClient
	 * @param directory
	 * @param log_analysis_condition_file
	 * @return
	 */
	private boolean export_for_analysis(SftpClient sftpClient, SftpFile directory, File log_analysis_condition_file) {
		List files = SshTool.ls( sftpClient, directory.getAbsolutePath());
		if ( null == files)
			return false;

		for ( int i = 0; i < files.size(); ++i) {
			SftpFile sftpFile = ( SftpFile)files.get( i);
			if ( sftpFile.getAbsolutePath().endsWith( "."))
				continue;

			if ( sftpFile.isDirectory()) {
				if ( directory.getFilename().matches( "[1-9][0-9]*")
					&& ( sftpFile.getFilename().equals( "agents") || sftpFile.getFilename().equals( "spots"))) {
					if ( !export_for_analysis( sftpClient, directory.getAbsolutePath(), log_analysis_condition_file))
						return false;

					break;
				} else
					export_for_analysis( sftpClient, sftpFile, log_analysis_condition_file);
			}
		}
	
		return true;
	}

	/**
	 * @param sftpClient
	 * @param directory
	 * @param log_analysis_condition_file
	 * @return
	 */
	private boolean export_for_analysis(SftpClient sftpClient, String directory, File log_analysis_condition_file) {
		try {
			sftpClient.put( log_analysis_condition_file.getAbsolutePath(), directory + "/" + Constant._analysis_condition_script_file_name);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}


		File analysis_shell_script_file;
		try {
			analysis_shell_script_file = File.createTempFile( "analysis_", ".sh");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter( analysis_shell_script_file);
		} catch (IOException e) {
			e.printStackTrace();
			analysis_shell_script_file.delete();
			return false;
		}

		try {
			fileWriter.write( "#!/bin/sh\n\n");
			fileWriter.write( "JAVA_HOME=" + _parameters._java_home + "\n\n");

//			fileWriter.write( "export PATH=" + _parameters._java_home + "/bin:$PATH\n");
//			fileWriter.write( "export JRE_HOME=" + _parameters._java_home + "\n");
//			fileWriter.write( "export JAVA_BINDER=" + _parameters._java_home + "/bin\n");
//			fileWriter.write( "export JAVA_HOME=" + _parameters._java_home + "\n");
//			fileWriter.write( "export JAVA_ROOT=" + _parameters._home_directory_name + "/program/java\n");

			String model_builder_memory_size = _environment.get( Environment._model_builder_memory_size_key, Constant._defaultMemorySize);
			model_builder_memory_size = ( model_builder_memory_size.equals( "") ? "0" : model_builder_memory_size);
			fileWriter.write( "$JAVA_HOME/bin/java -jar "
//			fileWriter.write( _parameters._java_home + "/bin/java -jar "
				+ "'" + _parameters._executor_jar_filename + "' "
				+ "analysis "
				+ "'" + _parameters._home_directory_name + "' "
				+ "'" + _parameters._sgeout_directory_name + "/" + _uuid + "/" + Constant._termination_name + "' "
				+ String.valueOf( _id) + " "
				+ "'" + _parameters._java_home + "' "
				+ model_builder_memory_size + " "
				+ "'" + directory + "' "
				+ "'" + directory + "' "
				+ "$@\n\n"
				+ "if [ -f \"" + _parameters._sgeout_directory_name + "/" + _uuid + "/" + Constant._termination_name + "." + String.valueOf( _id) + "\" ];\n"
				+ "then\n"
				+ "\texit 0\n"
				+ "else\n"
				+ "\techo 'error!' > \"" + _parameters._sgeout_directory_name + "/" + _uuid + "/" + Constant._termination_name + "." + String.valueOf( _id) + "\"\n"
				+ "fi\n\n");

			fileWriter.write( "exit 0\n");
		} catch (IOException e) {
			e.printStackTrace();
			analysis_shell_script_file.delete();
			return false;
		}

		try {
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			analysis_shell_script_file.delete();
			return false;
		}

		try {
			sftpClient.put( analysis_shell_script_file.getAbsolutePath(), directory + "/" + Constant._analysis_shell_script_file_name);
		} catch (IOException e) {
			e.printStackTrace();
			analysis_shell_script_file.delete();
			return false;
		}

		analysis_shell_script_file.delete();

		try {
			sftpClient.chmod( 0744, directory + "/" + Constant._analysis_shell_script_file_name);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		_analysis_shell_script_directory_map.put( String.valueOf( _id++), directory);

		_number_of_scripts_textField.setText( String.valueOf( _analysis_shell_script_directory_map.size()));
		_number_of_scripts_textField.update( _number_of_scripts_textField.getGraphics());

		return true;
	}

	/**
	 * @return
	 */
	private boolean compress() {
		synchronized ( _job_id_list) {
			_job_id_list.clear();
		}

		SshClient sshClient = SshTool.getSshClient( _parameters._host, _parameters._username, _parameters._password);
		if ( null == sshClient) {
			on_failed( "SshTool.getSshClient( ... ) at compress()\n",
				"Plugin error : compress" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator);
			return false;
		}

		SftpClient sftpClient = SshTool.getSftpClient( sshClient);
		if ( null == sftpClient) {
			sshClient.disconnect();
			on_failed( "SshTool.getSftpClient( ... ) at compress()\n",
				"Plugin error : compress" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator);
			return false;
		}

		if ( !SshTool.directory_exists( sftpClient, _environment.get( Environment._log_directory_key, ""))) {
			SshTool.close( sftpClient);
			sshClient.disconnect();
			on_failed( "SshTool.directory_exists( ... ) at compress()\n",
				"Plugin error : compress" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator);
			return false;
		}

		Connection connection = new Connection( _parameters._host, 22);
		try {
			connection.connect();
		} catch (IOException e) {
			SshTool.close( sftpClient);
			sshClient.disconnect();
			on_failed( "connection.connect() at compress()\n",
				"Plugin error : compress" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator);
			return false;
		}

		try {
			if ( !connection.authenticateWithPassword( _parameters._username, _parameters._password)) {
				connection.close();
				SshTool.close( sftpClient);
				sshClient.disconnect();
				on_failed( "connection.authenticateWithPassword( ... ) at compress()\n",
					"Plugin error : compress" + Constant._lineSeparator
						+ " Plugin name : " + _name + Constant._lineSeparator);
				return false;
			}
		} catch (IOException e) {
			connection.close();
			SshTool.close( sftpClient);
			sshClient.disconnect();
			on_failed( "connection.authenticateWithPassword( ... ) at compress()\n",
				"Plugin error : compress" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator);
			return false;
		}

		if ( !compress( sshClient, sftpClient, connection)) {
			connection.close();
			SshTool.close( sftpClient);
			sshClient.disconnect();
			return false;
		}

		connection.close();

		SshTool.close( sftpClient);
		sshClient.disconnect();

		return true;
	}

	/**
	 * @param sshClient
	 * @param sftpClient
	 * @param connection 
	 * @return
	 */
	private boolean compress(SshClient sshClient, SftpClient sftpClient, Connection connection) {
		synchronized ( _job_id_list) {
			_job_id_list.clear();
		}

		_parent.setEnabled( false);
		setEnabled( false);

		_number = 0;

		_status_textField.setText( "Compress : Exporting ...");
		_status_textField.update( _status_textField.getGraphics());

		_number_of_scripts_textField.setText( "0");
		_number_of_scripts_textField.update( _number_of_scripts_textField.getGraphics());

		_number_of_submitted_scripts_textField.setText( "0");
		_number_of_submitted_scripts_textField.update( _number_of_submitted_scripts_textField.getGraphics());

		_progressBar.setMinimum( 0);
		_progressBar.setMaximum( 1);
		_progressBar.setValue( 0);
		_progressBar.update( _progressBar.getGraphics());

		SftpFile directory = new SftpFile( _environment.get( Environment._script_directory_key, ""));
		if ( !export_for_compression( sftpClient, directory)) {
			on_failed( "export_for_compression( ... ) at compress( ... )\n", "export_for_compression( ... ) at compress( ... )", connection);
			return false;
		}

		setEnabled( true);
		_parent.setEnabled( true);

		_number_of_scripts_textField.setText( "1");
		_number_of_scripts_textField.update( _number_of_scripts_textField.getGraphics());

		_status_textField.setText( "Compress : Submitting ...");
		_status_textField.update( _status_textField.getGraphics());

		String result = execute( sshClient, sftpClient, directory, connection, Constant._compression_shell_script_file_name);
		if ( null == result) {
			on_failed( "execute( ... ) at compress( ... )\n", "execute( ... ) at compress( ... )", connection);
			return false;
		}

		_id_list.add( String.valueOf( _id++));

		_number_of_submitted_scripts_textField.setText( String.valueOf( _id_list.size()));
		_number_of_submitted_scripts_textField.update( _number_of_submitted_scripts_textField.getGraphics());

		_number = _id_list.size();
		_progressBar.setMinimum( 0);
		_progressBar.setMaximum( _number);
		_progressBar.setValue( 0);
		_progressBar.update( _progressBar.getGraphics());

		_status_textField.setText( "Compress : Running ...");
		_status_textField.update( _status_textField.getGraphics());

		_status = "compression";

		return true;
	}

	/**
	 * @param sftpClient
	 * @param directory
	 * @return
	 */
	private boolean export_for_compression(SftpClient sftpClient, SftpFile directory) {
		File compression_shell_script_file;
		try {
			compression_shell_script_file = File.createTempFile( "compression_", ".sh");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter( compression_shell_script_file);
		} catch (IOException e) {
			e.printStackTrace();
			compression_shell_script_file.delete();
			return false;
		}

		try {
			fileWriter.write( "#!/bin/sh\n\n");
			fileWriter.write( "JAVA_HOME=" + _parameters._java_home + "\n\n");

//			fileWriter.write( "export PATH=" + _parameters._java_home + "/bin:$PATH\n");
//			fileWriter.write( "export JRE_HOME=" + _parameters._java_home + "\n");
//			fileWriter.write( "export JAVA_BINDER=" + _parameters._java_home + "/bin\n");
//			fileWriter.write( "export JAVA_HOME=" + _parameters._java_home + "\n");
//			fileWriter.write( "export JAVA_ROOT=" + _parameters._home_directory_name + "/program/java\n");

			String model_builder_memory_size = _environment.get( Environment._model_builder_memory_size_key, Constant._defaultMemorySize);
			model_builder_memory_size = ( model_builder_memory_size.equals( "") ? "0" : model_builder_memory_size);
			fileWriter.write( "$JAVA_HOME/bin/java -jar "
//			fileWriter.write( _parameters._java_home + "/bin/java -jar "
				+ "'" + _parameters._executor_jar_filename + "' "
				+ "compression "
				+ "'" + _parameters._home_directory_name + "' "
				+ "'" + _parameters._sgeout_directory_name + "/" + _uuid + "/" + Constant._termination_name + "' "
				+ String.valueOf( _id) + " "
				+ "'" + _parameters._java_home + "' "
				+ model_builder_memory_size + " "
				+ "'" + _parameters._sgeout_directory_name + "/" + _uuid + ".zip" + "' "
				+ "'" + _environment.get( Environment._log_directory_key, "") + "' "
				+ "'" + _parameters._sgeout_directory_name + "/" + _uuid + "' "
				+ "$@\n\n"
				+ "if [ -f \"" + _parameters._sgeout_directory_name + "/" + _uuid + "/" + Constant._termination_name + "." + String.valueOf( _id) + "\" ];\n"
				+ "then\n"
				+ "\texit 0\n"
				+ "else\n"
				+ "\techo 'error!' > \"" + _parameters._sgeout_directory_name + "/" + _uuid + "/" + Constant._termination_name + "." + String.valueOf( _id) + "\"\n"
				+ "fi\n\n");

			fileWriter.write( "exit 0\n");
		} catch (IOException e) {
			e.printStackTrace();
			compression_shell_script_file.delete();
			return false;
		}

		try {
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			compression_shell_script_file.delete();
			return false;
		}

		try {
			sftpClient.put( compression_shell_script_file.getAbsolutePath(), directory.getAbsolutePath() + "/" + Constant._compression_shell_script_file_name);
		} catch (IOException e) {
			e.printStackTrace();
			compression_shell_script_file.delete();
			return false;
		}

		compression_shell_script_file.delete();

		try {
			sftpClient.chmod( 0744, directory.getAbsolutePath() + "/" + Constant._compression_shell_script_file_name);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param sftpClient
	 */
	private boolean transfer(SftpClient sftpClient) {
		synchronized ( _job_id_list) {
			_job_id_list.clear();
		}

		_parent.setEnabled( false);
		setEnabled( false);

		_status_textField.setText( "Transfer : Running ...");
		_status_textField.update( _status_textField.getGraphics());

		_progressBar.setMinimum( 0);
		_progressBar.setMaximum( 1);
		_progressBar.setValue( 0);
		_progressBar.update( _progressBar.getGraphics());

		File local_log_directory = new File( _environment.get( Environment._local_log_directory_key, ""));
		if ( !make_local_directory( local_log_directory)) {
			on_failed( "make_local_directory( ... ) at transfer( ... )\n", "make_local_directory( ... ) at transfer( ... )");
			return false;
		}

		// TODO Auto-generated method stub
		try {
			sftpClient.get( _parameters._sgeout_directory_name + "/" + _uuid + ".zip",
				local_log_directory.getAbsolutePath() + "/" + _uuid + ".zip");
		} catch (IOException e) {
			//e.printStackTrace();
			//SshTool.rm( sftpClient, _parameters._sgeout_directory_name + "/" + _uuid + ".zip");
			on_failed( "sftpClient.get( ... ) at transfer( ... )\n", "sftpClient.get( ... ) at transfer( ... )");
			return false;
		} catch (Throwable ex) {
			//SshTool.rm( sftpClient, _parameters._sgeout_directory_name + "/" + _uuid + ".zip");
			on_failed( "sftpClient.get( ... ) at transfer( ... )\n", "sftpClient.get( ... ) at transfer( ... )");
			return false;
		}

		//SshTool.rm( sftpClient, _parameters._sgeout_directory_name + "/" + _uuid + ".zip");

		_status_textField.setText( "Unpack : Running ...");
		_status_textField.update( _status_textField.getGraphics());

		if ( !unpack( local_log_directory.getAbsolutePath() + "/" + _uuid + ".zip",
			local_log_directory.getAbsolutePath(),
			_environment.get( Environment._log_directory_key, ""),
			_parameters._sgeout_directory_name)) {
			on_failed( "unpack( ... ) at transfer( ... )\n", "unpack( ... ) at transfer( ... )");
			return false;
		}

		setEnabled( true);
		_parent.setEnabled( true);

		_progressBar.setValue( 1);
		_progressBar.update( _progressBar.getGraphics());

		_status = "none";

		return true;
	}

	/**
	 * @param local_directory
	 * @return
	 */
	private boolean make_local_directory(File local_directory) {
		if ( local_directory.exists()) {
//			if ( local_directory.isDirectory())
//				_error_log_textArea.append( "Overwritten! : " + local_directory.getAbsolutePath() + "\n");
//			else {
			if ( !local_directory.isDirectory()) {
				if ( !local_directory.delete()) {
					_error_log_textArea.append( "Could not make! : " + local_directory.getAbsolutePath() + "\n");
					return false;
				}
	
				if ( !local_directory.mkdirs()) {
					_error_log_textArea.append( "Could not make! : " + local_directory.getAbsolutePath() + "\n");
					return false;
				}
			}
		} else {
			if ( !local_directory.mkdirs()) {
				_error_log_textArea.append( "Could not make! : " + local_directory.getAbsolutePath() + "\n");
				return false;
			}
		}
		return true;
	}
	/**
	 * @param path
	 * @param sgeout_directory
	 * @param absolutePath
	 * @param string2
	 * @return
	 */
	private boolean unpack(String path, String local_log_directory, String log_directory, String sgeout_directory) {
		ZipInputStream zipInputStream = null;
		try {
			zipInputStream = new ZipInputStream( new FileInputStream( path));
			ZipEntry zipEntry = null;
			while ( null != ( zipEntry = zipInputStream.getNextEntry())) {
				String filename = zipEntry.getName();
				String name = "";
				if ( filename.startsWith( log_directory) && filename.length() > log_directory.length() + 1)
					name = filename.substring( log_directory.length());
				else if ( filename.startsWith( sgeout_directory) && filename.length() > sgeout_directory.length() + 1)
					name = filename.substring( sgeout_directory.length());
				else {
					zipInputStream.closeEntry();
					continue;
				}

				if ( name.endsWith( "/")) {
					File directory = new File( local_log_directory + name);
					if ( !directory.mkdirs()) {
						zipInputStream.closeEntry();
						return false;
					}

					zipInputStream.closeEntry();
					continue;
				} else {
					File file = new File( local_log_directory + name);
					File directory = new File( file.getParent());
					if ( !directory.exists() && !directory.mkdirs()) {
						zipInputStream.closeEntry();
						return false;
					}
				}

				if ( name.endsWith( "$Name.log")) {
					zipInputStream.closeEntry();
					continue;
				}

				File file = new File( local_log_directory + name);
				FileOutputStream fileOutputStream = new FileOutputStream( file);

				byte[] buf = new byte[ 1024000];
				BufferedInputStream bufferedInputStream = new BufferedInputStream( zipInputStream);

				int count;
				while ( -1 != ( count = bufferedInputStream.read( buf, 0, 1024000)))
					fileOutputStream.write( buf, 0, count);

				fileOutputStream.close();
				zipInputStream.closeEntry();
			}

			try {
				zipInputStream.close();
				//if ( fileOutputStream != null)
				//	fileOutputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 
	 */
	private void start_timer() {
		if ( null == _timer) {
			_timer = new Timer();
			_timerTaskImplement = new TimerTaskImplement( 0, this);

			long period = ( 1 * 60 * 1000);

//			long period = ( _id_list.size() * 100);
//			period = ( ( 6000 > period) ? 6000 : period);

			_running = false;
			_timer.schedule( _timerTaskImplement, _delay, period);
		}
	}

	/**
	 * 
	 */
	private void stop_timer() {
		if ( null != _timer) {
			_timer.cancel();
			_timer = null;
			_running = false;
		}
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.tool.timer.ITimerTaskImplementCallback#execute_timer_task(int)
	 */
	public void execute_timer_task(int id) {
		if ( _stop) {
			//stop_timer();
			//on_stop();
			return;
		}

		if ( _running)
			return;

		_running = true;

		SshClient sshClient = null;
		try {
			sshClient = SshTool.getSshClient( _parameters._host, _parameters._username, _parameters._password);
		} catch (Throwable ex) {
			_running = false;
			return;
		}

		if ( null == sshClient) {
			_running = false;
			return;
		}

		SftpClient sftpClient = null;
		try {
			sftpClient = SshTool.getSftpClient( sshClient);
		} catch (Throwable ex) {
			SshTool.disconnect( sshClient);
			_running = false;
			return;
		}

		if ( null == sftpClient) {
			SshTool.disconnect( sshClient);
			_running = false;
			return;
		}

		Connection connection = null;
		try {
			connection = new Connection( _parameters._host, 22);
		} catch (Throwable ex) {
			SshTool.close_and_catch( sftpClient);
			SshTool.disconnect( sshClient);
			_running = false;
			return;
		}

		try {
			connection.connect();
		} catch (IOException e) {
			SshTool.close_and_catch( sftpClient);
			SshTool.disconnect( sshClient);
			_running = false;
			return;
		} catch (Throwable ex) {
			SshTool.close_and_catch( sftpClient);
			SshTool.disconnect( sshClient);
			_running = false;
			return;
		}

		try {
			if ( !connection.authenticateWithPassword( _parameters._username, _parameters._password)) {
				SshTool.close( connection);
				SshTool.close_and_catch( sftpClient);
				SshTool.disconnect( sshClient);
				_running = false;
				return;
			}
		} catch (IOException e) {
			SshTool.close( connection);
			SshTool.close_and_catch( sftpClient);
			SshTool.disconnect( sshClient);
			_running = false;
			return;
		} catch (Throwable ex) {
			SshTool.close( connection);
			SshTool.close_and_catch( sftpClient);
			SshTool.disconnect( sshClient);
			_running = false;
			return;
		}

		String termination = _parameters._sgeout_directory_name + "/" + _uuid + "/" + Constant._termination_name + ".";
		List finished_id_list = new ArrayList();
		for ( int i = 0; i < _id_list.size(); ++i) {
			String job_id = ( String)_id_list.get( i);
			try {
				if ( SshTool.file_exists( sftpClient, termination + job_id))
					finished_id_list.add( job_id);
			} catch (Throwable ex) {
				SshTool.close( connection);
				SshTool.close_and_catch( sftpClient);
				SshTool.disconnect( sshClient);
				_running = false;
				return;
			}
		}

		for ( int i = 0; i < finished_id_list.size(); ++i) {
			String job_id = ( String)finished_id_list.get( i);
			_id_list.remove( job_id);
		}

		_progressBar.setValue( _number - _id_list.size());
		_progressBar.update( _progressBar.getGraphics());

		if ( _id_list.isEmpty()) {
			if ( _status.equals( "simulation")) {
				if ( _environment.get( Environment._log_analysis_key, "false").equals( "true")) {
					try {
						Thread.sleep( 1000 * _wait);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					try {
						if ( !analyze( sshClient, sftpClient, connection)) {
							_status = "none";
							stop_timer();
						}
					} catch (Throwable ex) {
						_status = "none";
						stop_timer();
						on_failed( "analyze( ... ) at execute_timer_task( ... )\n", "analyze( ... ) at execute_timer_task( ... )", connection);
					}
				} else {
					if ( _environment.get( Environment._log_transfer_key, "false").equals( "true")) {
						try {
							Thread.sleep( 1000 * _wait);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						try {
							if ( !compress( sshClient, sftpClient, connection)) {
								_status = "none";
								stop_timer();
							}
						} catch (Throwable ex) {
							_status = "none";
							stop_timer();
							on_failed( "compress( ... ) at execute_timer_task( ... )\n", "compress( ... ) at execute_timer_task( ... )", connection);
						}
					} else {
						_status = "none";
						stop_timer();
						_status_textField.setText( "Done");
						_status_textField.update( _status_textField.getGraphics());
					}
				}
			} else if ( _status.equals( "analysis")) {
				if ( _environment.get( Environment._log_transfer_key, "false").equals( "true")) {
					try {
						Thread.sleep( 1000 * _wait);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					try {
						if ( !compress( sshClient, sftpClient, connection)) {
							_status = "none";
							stop_timer();
						}
					} catch (Throwable ex) {
						_status = "none";
						stop_timer();
						on_failed( "compress( ... ) at execute_timer_task( ... )\n", "compress( ... ) at execute_timer_task( ... )", connection);
					}
				} else {
					_status = "none";
					stop_timer();
					_status_textField.setText( "Done");
					_status_textField.update( _status_textField.getGraphics());
				}
			} else if ( _status.equals( "compression")) {
				try {
					Thread.sleep( 1000 * _wait);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					if ( !transfer( sftpClient)) {
						_status = "none";
						stop_timer();
					} else {
						_status = "none";
						stop_timer();
						_status_textField.setText( "Done");
						_status_textField.update( _status_textField.getGraphics());
					}
				} catch (Throwable ex) {
					_status = "none";
					stop_timer();
					on_failed( "transfer( ... ) at execute_timer_task( ... )\n", "transfer( ... ) at execute_timer_task( ... )", connection);
				}
			} else {
				_status = "none";
				stop_timer();
				_status_textField.setText( "Done");
				_status_textField.update( _status_textField.getGraphics());
			}
		}

		if ( _termination || _stop) {
			setEnabled( false);
			delete( connection);
			setEnabled( true);
		}

		SshTool.close( connection);
		SshTool.close_and_catch( sftpClient);
		SshTool.disconnect( sshClient);

		_running = false;
	}

	/**
	 * @param log
	 * @param message
	 */
	private void on_failed(String log, String message) {
		if ( _termination || _stop)
			return;

		_status_textField.setText( "Failed ...");
		_status_textField.update( _status_textField.getGraphics());
		_error_log_textArea.append( log);
		_error_log_textArea.update( _error_log_textArea.getGraphics());
		_stop_button.setEnabled( false);
		setEnabled( false);
		delete();
		setEnabled( true);
		_parent.setEnabled( true);
		JOptionPane.showMessageDialog( this, message, getTitle(), JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param log
	 * @param message
	 * @param connection
	 */
	private void on_failed(String log, String message, Connection connection) {
		if ( _termination || _stop)
			return;

		_status_textField.setText( "Failed ...");
		_status_textField.update( _status_textField.getGraphics());
		_error_log_textArea.append( log);
		_error_log_textArea.update( _error_log_textArea.getGraphics());
		_stop_button.setEnabled( false);
		setEnabled( false);
		delete( connection);
		setEnabled( true);
		_parent.setEnabled( true);
		JOptionPane.showMessageDialog( this, message, getTitle(), JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * 
	 */
	private void delete() {
		Connection connection = new Connection( _parameters._host, 22);
		try {
			connection.connect();
		} catch (IOException e) {
			JOptionPane.showMessageDialog( this,
				"Plugin error : delete" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator,
				getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			if ( !connection.authenticateWithPassword( _parameters._username, _parameters._password)) {
				connection.close();
				JOptionPane.showMessageDialog( this,
					"Plugin error : delete" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator,
					getTitle(),
					JOptionPane.ERROR_MESSAGE);
				return;
			}
		} catch (IOException e) {
			connection.close();
			JOptionPane.showMessageDialog( this,
				"Plugin error : delete" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator,
				getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return;
		}

		delete( connection);

		connection.close();
	}

	/**
	 * @param connection
	 */
	private void delete(Connection connection) {
		synchronized ( _job_id_list) {
			if ( _job_id_list.isEmpty())
				return;

			String command = _parameters._delete_shell_script_file_name;
			for ( String id:_job_id_list)
				command += ( " " + id);

			//System.out.println( command);

			String result = "";
			Session session;
			try {
				session = connection.openSession();
				session.requestPTY( "vt100", 80, 24, 0, 0, null);
				session.startShell();
				try {
					Thread.sleep( 5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( new StreamGobbler( session.getStdout())));
				PrintWriter printWriter = new PrintWriter( session.getStdin());
				printWriter.println();
				printWriter.flush();
				printWriter.println( command);
				printWriter.flush();
				printWriter.println( "exit");
				printWriter.flush();
				while ( true) {
					String line = null;
					try {
						line = bufferedReader.readLine();
					} catch (IOException e) {
						e.printStackTrace();
						break;
					}

					if ( null == line)
						break;

					//System.out.println( line);
					result += ( line + Constant._lineSeparator);
				}

				bufferedReader.close();

				session.close();

				_error_log_textArea.append( result);
				System.out.println( result);

				_job_id_list.clear();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
