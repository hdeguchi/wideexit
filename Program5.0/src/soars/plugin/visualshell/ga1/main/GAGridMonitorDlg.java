/**
 * 
 */
package soars.plugin.visualshell.ga1.main;

import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
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
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.sftp.SftpFile;

import soars.common.utility.swing.border.ComponentTitledBorder;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.reflection.Reflection;
import soars.common.utility.tool.ssh.SshTool;
import soars.common.utility.tool.timer.ITimerTaskImplementCallback;
import soars.common.utility.tool.timer.TimerTaskImplement;

/**
 * @author kurata
 *
 */
public class GAGridMonitorDlg extends GAMonitorDlg implements ITimerTaskImplementCallback {

	/**
	 * 
	 */
	private JTextField _grid_portal_ip_address_textField = null;

	/**
	 * 
	 */
	private JTextField _local_username_textField = null;

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
	private JTextField _number_of_submitted_scripts_textField = null;

	/**
	 * 
	 */
	private int _number = 0;

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
	private boolean _exist_user_data_directory = false;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param name
	 * @param localParameters
	 * @param algorithm_name_map
	 * @param algorithm_type_map
	 * @param parameters
	 * @param experimentManager
	 * @param environment
	 * @throws HeadlessException
	 */
	public GAGridMonitorDlg(Frame arg0, String arg1, boolean arg2, String name, LocalParameters localParameters,
		Map<String, String> algorithmNameMap, Map<String, String> algorithmTypeMap, Parameters parameters, Object originalExperimentManager, Environment environment) throws HeadlessException {
		super(arg0, arg1, arg2, name, localParameters, algorithmNameMap, algorithmTypeMap, originalExperimentManager, environment);
		_parameters = parameters;
	}

	/* (non-Javadoc)
	 * @see soars.plugin.visualshell.ga1.main.GAMonitorDlg#get_rectangle_from_environment_file()
	 */
	protected Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get( Environment._ga_grid_frame_rectangle_key + "x",
			String.valueOf( SwingTool.get_default_window_position( _parent, _minimum_width, _minimum_height).x));
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get( Environment._ga_grid_frame_rectangle_key + "y",
			String.valueOf( SwingTool.get_default_window_position( _parent, _minimum_width, _minimum_height).y));
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get( Environment._ga_grid_frame_rectangle_key + "width",
			String.valueOf( _minimum_width));
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get( Environment._ga_grid_frame_rectangle_key + "height",
			String.valueOf( _minimum_height));
		if ( null == value)
			return null;

		int height = Integer.parseInt( value);

		return new Rectangle( x, y, width, height);
	}

	/* (non-Javadoc)
	 * @see soars.plugin.visualshell.ga1.main.GAMonitorDlg#set_property_to_environment_file()
	 */
	protected void set_property_to_environment_file() {
		Rectangle rectangle = getBounds();

		Environment.get_instance().set(
			Environment._ga_grid_frame_rectangle_key + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._ga_grid_frame_rectangle_key + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._ga_grid_frame_rectangle_key + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._ga_grid_frame_rectangle_key + "height", String.valueOf( rectangle.height));

		super.set_property_to_environment_file();
	}

	/* (non-Javadoc)
	 * @see soars.plugin.visualshell.ga1.main.GAMonitorDlg#on_cancel(java.awt.event.ActionEvent)
	 */
	protected void on_cancel(ActionEvent actionEvent) {
		//System.out.println( "cancel");
		if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog( _parent,
			ResourceManager.get_instance().get( "plugin.ga1.frame.exit.confirm.message"), getTitle(), JOptionPane.YES_NO_OPTION))
			return;

		stop_timer();
		_termination = true;
		on_stop( null);
		super.on_cancel(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.plugin.visualshell.ga1.main.GAMonitorDlg#setup_grid_panel(javax.swing.JPanel)
	 */
	protected void setup_grid_panel(JPanel parent) {
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout( new BoxLayout( gridPanel, BoxLayout.X_AXIS));

		gridPanel.add( Box.createHorizontalStrut( 10));

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		setup_grid_components( panel);

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.grid.frame"));
		panel.setBorder( new ComponentTitledBorder( label, panel, BorderFactory.createLineBorder( getForeground())));

		gridPanel.add( panel);

		gridPanel.add( Box.createHorizontalStrut( 5));

		parent.add( gridPanel);
	}

	/**
	 * @param parent
	 */
	private void setup_grid_components(JPanel parent) {
		setup_grid_portal_ip_address_textField( parent);

		insert_horizontal_glue( parent);

		setup_local_username_textField( parent);

		insert_horizontal_glue( parent);

		setup_script_directory_textField( parent);

		insert_horizontal_glue( parent);

		setup_log_directory_textField( parent);

		insert_horizontal_glue( parent);

		setup_model_builder_memory_size_textField( parent);

		insert_horizontal_glue( parent);

		setup_number_of_scripts_textField( parent);

		insert_horizontal_glue( parent);

		setup_number_of_submitted_scripts_textField( parent);

		insert_horizontal_glue( parent);

		setup_status_textField( parent);

		insert_horizontal_glue( parent);

		setup_progressBar( parent);

		insert_horizontal_glue( parent);
	}

	/**
	 * @param parent
	 */
	private void setup_grid_portal_ip_address_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.grid.frame.gird.portal.ip.address"), SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_grid_portal_ip_address_textField = new JTextField( _parameters._grid_portal_ip_address);
		_grid_portal_ip_address_textField.setEditable( false);
		panel.add( _grid_portal_ip_address_textField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_local_username_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.grid.frame.username"), SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_local_username_textField = new JTextField( _parameters._local_username);
		_local_username_textField.setEditable( false);
		panel.add( _local_username_textField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_script_directory_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.grid.frame.script.directory"), SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_script_directory_textField = new JTextField(
			Environment.get_instance().get( Environment._script_directory_key, ""));
		_script_directory_textField.setEditable( false);
		panel.add( _script_directory_textField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_log_directory_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.grid.frame.log.directory"), SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_log_directory_textField = new JTextField(
			Environment.get_instance().get( Environment._log_directory_key, ""));
		_log_directory_textField.setEditable( false);
		panel.add( _log_directory_textField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_model_builder_memory_size_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.grid.frame.model.builder.memory.size"), SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_model_builder_memory_size_textField = new JTextField(
			_environment.get( Environment._model_builder_memory_size_key, Constant._defaultMemorySize));
		_model_builder_memory_size_textField.setHorizontalAlignment( SwingConstants.RIGHT);
		_model_builder_memory_size_textField.setEditable( false);
		//_model_builder_memory_size_textField.setPreferredSize( new Dimension( 200,
		//	_model_builder_memory_size_textField.getPreferredSize().height));
		panel.add( _model_builder_memory_size_textField);

		label = new JLabel( "MB");
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void setup_number_of_submitted_scripts_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.grid.frame.number.of.submitted.script"), SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_number_of_submitted_scripts_textField = new JTextField( "0");
		_number_of_submitted_scripts_textField.setHorizontalAlignment( SwingConstants.RIGHT);
		_number_of_submitted_scripts_textField.setEditable( false);
		panel.add( _number_of_submitted_scripts_textField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
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

				session.close();

				if ( null == result)
					_log_textArea.append( text);
				else {
					int index = 0;
					String[] words = result.split( " ");
					if ( words[ 0].toLowerCase().equals( "your") && words[ 1].toLowerCase().equals( "job"))
						index = 2;
					else if ( words[ 1].toLowerCase().equals( "your") && words[ 2].toLowerCase().equals( "job"))
						index = 3;
					else {
						_log_textArea.append( text);
						return null;
					}

					double d;
					try {
						d = Double.parseDouble( words[ index]);
					} catch (NumberFormatException e) {
						//e.printStackTrace();
						_log_textArea.append( text);
						return null;
					}
					_job_id_list.add( words[ index]);
				}

				return result;
			} catch (IOException e) {
				return null;
			}
		}

//		String output = SshTool.execute1( sshClient,
//				_parameters._submit_shell_script_file_name + " "
//				+ "'" + Constant._job_name + "' "
//				+ "'" + _parameters._sgeout_directory_name + "/" + _uuid + "' "
//				+ "'" + _parameters._sgeout_directory_name + "/" + _uuid + "' "
//				+ "'" + directory.getAbsolutePath() + "/" + shell_script_file_name + "'");
//
//		if ( null == output)
//			return null;
//
//		System.out.println( output);
//
//		boolean result = ( ( output.toLowerCase().startsWith( "your job") && output.trim().endsWith( "has been submitted")));
//		if ( !result) {
//			_log_textArea.append( "execute() : " + output + "\n");
//			_log_textArea.update( _log_textArea.getGraphics());
//		}
//
//		return output;
//		//return ( result ? output : null);
//		//return ( ( output.toLowerCase().startsWith( "your job") && output.trim().endsWith( "has been submitted")) ? output : null);

//		String result = SshTool.execute2( sshClient,
//			_parameters._submit_shell_script_file_name + " "
//				+ "'" + Constant._job_name + "' "
//				+ "'" + _parameters._sgeout_directory_name + "/" + _uuid + "' "
//				+ "'" + _parameters._sgeout_directory_name + "/" + _uuid + "' "
//				+ "'" + directory.getAbsolutePath() + "/" + shell_script_file_name + "'\n");
//
//		if ( null == result)
//			return null;
//
//		String[] lines = result.split( Constant._line_separator);
//		if ( null == lines || 0 == lines.length)
//			return null;
//
//		for ( int i = 0; i < lines.length; ++i) {
//			//System.out.println( lines[ i]);
//			if ( lines[ i].toLowerCase().startsWith( "your job") && lines[ i].trim().endsWith( "has been submitted"))
//				return lines[ i];
//		}
//
//		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		if ( !initialize())
			return;

		if ( !start())
			return;

		if ( _stop) {
			//on_stop();
			return;
		}

		start_timer();
	}

	/**
	 * @return
	 */
	private boolean initialize() {
		List resultList = new ArrayList();
		if ( !Reflection.execute_static_method( "soars.application.visualshell.layer.LayerManager", "get_instance", new Class[ 0], new Object[ 0], resultList)) {
			JOptionPane.showMessageDialog( _parent,
				"Plugin error : Reflection.execute_static_method( ... )" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator
				+ " Class name : soars.application.visualshell.layer.LayerManager" + Constant._lineSeparator
				+ " Method name : get_instance" + Constant._lineSeparator,
			getTitle(),
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
			JOptionPane.showMessageDialog( _parent,
				"Plugin error : Reflection.execute_static_method( ... )" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator
				+ " Class name : soars.application.visualshell.layer.LayerManager" + Constant._lineSeparator
				+ " Method name : get_instance" + Constant._lineSeparator,
				getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}


		resultList.clear();


		if ( !Reflection.execute_class_method( object, "exist_user_data_directory", new Class[ 0], new Object[ 0], resultList)
			|| resultList.isEmpty() || null == resultList.get( 0) || !( resultList.get( 0) instanceof Boolean)) {
			JOptionPane.showMessageDialog( _parent,
				"Plugin error : Reflection.execute_class_method( ... )" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator
				+ " Class name : soars.application.visualshell.layer.LayerManager" + Constant._lineSeparator
				+ " Method name : exist_user_data_directory" + Constant._lineSeparator,
				getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		Boolean result = ( Boolean)resultList.get( 0);
		_exist_user_data_directory = result.booleanValue();


		resultList.clear();


		if ( !Reflection.execute_class_method( object, "setup_for_genetic_algorithm_on_grid",
			new Class[] { String.class, String.class, String.class, String.class},
			new Object[] {
				_environment.get( Environment._script_directory_key, ""),
				_parameters._grid_portal_ip_address,
				_parameters._username,
				_parameters._password},
			resultList)
			|| resultList.isEmpty() || null == resultList.get( 0) || !( resultList.get( 0) instanceof Boolean) || !(( Boolean)resultList.get( 0)).booleanValue()) {
			JOptionPane.showMessageDialog( _parent,
				"Plugin error : Reflection.execute_class_method( ... )" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator
				+ " Class name : soars.application.visualshell.layer.LayerManager" + Constant._lineSeparator
				+ " Method name : setup_for_genetic_algorithm_on_grid" + Constant._lineSeparator,
				getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
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

	/**
	 * @return
	 */
	protected boolean start() {
		if ( !get_guid()) {
			on_failed( "get_guid() at start()\n", "get_guid() at start()");
			return false;
		}

		if ( _stop)
			return true;

		if ( !setup()) {
			on_failed( "setup() at start()\n", "setup() at start()");
			return false;
		}

		if ( _stop)
			return true;

		try {
			if ( !simulate())
				return false;
		} catch (Throwable ex) {
			on_failed( "simulate() at start()\n", "simulate() at start()");
			return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.plugin.visualshell.ga1.main.GAMonitorDlg#setup()
	 */
	protected boolean setup() {
		// TODO Auto-generated method stub
		String local_log_directory = Environment.get_instance().get( Environment._local_log_directory_key, "");
		if ( !FileUtility.delete( new File( local_log_directory), false)) {
			_log_textArea.append( "Could not clear! : " + local_log_directory + "\n");
			return false;
		}

		return super.setup();
	}

	/**
	 * @return
	 */
	private boolean simulate() {
		setEnabled( false);

		_number = 0;
		_id_list.clear();
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
		List resultList = new ArrayList();
		if ( !Reflection.execute_static_method( "soars.application.visualshell.layer.LayerManager", "get_instance", new Class[ 0], new Object[ 0], resultList)) {
			JOptionPane.showMessageDialog( _parent,
				"Plugin error : Reflection.execute_static_method( ... )" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator
				+ " Class name : soars.application.visualshell.layer.LayerManager" + Constant._lineSeparator
				+ " Method name : get_instance" + Constant._lineSeparator,
			getTitle(),
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
			JOptionPane.showMessageDialog( _parent,
				"Plugin error : Reflection.execute_static_method( ... )" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator
				+ " Class name : soars.application.visualshell.layer.LayerManager" + Constant._lineSeparator
				+ " Method name : get_instance" + Constant._lineSeparator,
				getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}


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

		if ( !Reflection.execute_class_method( object, "export_data_for_genetic_algorithm_on_grid",
			new Class[] { cls, String.class, String.class, String.class, int.class, String.class, String.class, String.class, IntBuffer.class, JTextField.class},
			new Object[] { _experimentManager,
				_environment.get( Environment._script_directory_key, ""),
				_environment.get( Environment._log_directory_key, ""),
				_parameters._program_directory_name,
				1/*new Integer( _environment.get( Environment._number_of_times_for_grid_key, "1"))*/,
				_parameters._grid_portal_ip_address,
				_parameters._username,
				_parameters._password,
				intBuffer,
				_number_of_scripts_textField},
			resultList)
			|| resultList.isEmpty() || null == resultList.get( 0) || !( resultList.get( 0) instanceof Boolean) || !(( Boolean)resultList.get( 0)).booleanValue()) {
			JOptionPane.showMessageDialog( _parent,
				"Plugin error : Reflection.execute_class_method( ... )" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator
				+ " Class name : soars.application.visualshell.layer.LayerManager" + Constant._lineSeparator
				+ " Method name : export_data_for_genetic_algorithm_on_grid" + Constant._lineSeparator,
				getTitle(),
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
		SshClient sshClient = SshTool.getSshClient( _parameters._grid_portal_ip_address, _parameters._username, _parameters._password);
		if ( null == sshClient) {
			JOptionPane.showMessageDialog( _parent,
				"Plugin error : submit" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator,
				getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		SftpClient sftpClient = SshTool.getSftpClient( sshClient);
		if ( null == sftpClient) {
			sshClient.disconnect();
			JOptionPane.showMessageDialog( _parent,
				"Plugin error : submit" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator,
				getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( !SshTool.directory_exists( sftpClient, _environment.get( Environment._script_directory_key, ""))) {
			SshTool.close( sftpClient);
			sshClient.disconnect();
			JOptionPane.showMessageDialog( _parent,
				"Plugin error : submit" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator,
				getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		Connection connection = new Connection( _parameters._grid_portal_ip_address, 22);
		try {
			connection.connect();
		} catch (IOException e) {
			SshTool.close( sftpClient);
			sshClient.disconnect();
			JOptionPane.showMessageDialog( _parent,
				"Plugin error : submit" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator,
				getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		try {
			if ( !connection.authenticateWithPassword( _parameters._username, _parameters._password)) {
				connection.close();
				SshTool.close( sftpClient);
				sshClient.disconnect();
				JOptionPane.showMessageDialog( _parent,
					"Plugin error : submit" + Constant._lineSeparator
						+ " Plugin name : " + _name + Constant._lineSeparator,
					getTitle(),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		} catch (IOException e) {
			connection.close();
			SshTool.close( sftpClient);
			sshClient.disconnect();
			JOptionPane.showMessageDialog( _parent,
				"Plugin error : submit" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator,
				getTitle(),
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

			String model_builder_memory_size = _environment.get( Environment._model_builder_memory_size_key, Constant._defaultMemorySize);
			model_builder_memory_size = ( model_builder_memory_size.equals( "") ? "0" : model_builder_memory_size);

			if ( _exist_user_data_directory) {
				String script_directory_name = _environment.get( Environment._script_directory_key, "");
				String parent_directory_name = _environment.get( Environment._log_directory_key, "") + directory.getAbsolutePath().substring( script_directory_name.length());
				String user_data_directory_name = parent_directory_name + "/" + Constant._userDataDirectory;

				fileWriter.write( 
					"if [ -d \"" + user_data_directory_name + "\" ];\n"
					+ "then\n"
					+ "\techo 'user data directory already exists!'\n"
					+ "else\n"
					+ "\t$JAVA_HOME/bin/java -jar "
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
					+ "\tif [ -d \"" + user_data_directory_name + "\" ];\n"
					+ "\tthen\n"
					+ "\t\techo 'decompression completed!'\n"
					//+ "\t\techo 'completed!' > \"" + _parameters._sgeout_directory_name + "/" + _uuid + "/" + Constant._decompression_name + "." + String.valueOf( _id) + "\"\n"
					+ "\telse\n"
					+ "\t\techo 'error!' > \"" + _parameters._sgeout_directory_name + "/" + _uuid + "/" + Constant._decompression_name + "." + String.valueOf( _id) + "\"\n"
					+ "\t\texit 0\n"
					+ "\tfi\n"
					+ "fi\n\n");

				fileWriter.write( "$JAVA_HOME/bin/java -jar "
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

//	/**
//	 * @return
//	 */
//	private boolean compress() {
//		synchronized ( _job_id_list) {
//			_job_id_list.clear();
//		}
//
//		SshClient sshClient = SshTool.getSshClient( _parameters._grid_portal_ip_address, _parameters._username, _parameters._password);
//		if ( null == sshClient) {
//			on_failed( "SshTool.getSshClient( ... ) at compress()\n",
//				"Plugin error : compress" + Constant._line_separator
//					+ " Plugin name : " + _name + Constant._line_separator);
//			return false;
//		}
//
//		SftpClient sftpClient = SshTool.getSftpClient( sshClient);
//		if ( null == sftpClient) {
//			sshClient.disconnect();
//			on_failed( "SshTool.getSftpClient( ... ) at compress()\n",
//				"Plugin error : compress" + Constant._line_separator
//					+ " Plugin name : " + _name + Constant._line_separator);
//			return false;
//		}
//
//		if ( !SshTool.directory_exists( sftpClient, _environment.get( Environment._log_directory_key, ""))) {
//			SshTool.close( sftpClient);
//			sshClient.disconnect();
//			on_failed( "SshTool.directory_exists( ... ) at compress()\n", 
//				"Plugin error : compress" + Constant._line_separator
//					+ " Plugin name : " + _name + Constant._line_separator);
//			return false;
//		}
//
//		Connection connection = new Connection( _parameters._grid_portal_ip_address, 22);
//		try {
//			connection.connect();
//		} catch (IOException e) {
//			SshTool.close( sftpClient);
//			sshClient.disconnect();
//			on_failed( "connection.connect() at compress()\n",
//				"Plugin error : compress" + Constant._line_separator
//					+ " Plugin name : " + _name + Constant._line_separator);
//			return false;
//		}
//
//		try {
//			if ( !connection.authenticateWithPassword( _parameters._username, _parameters._password)) {
//				connection.close();
//				SshTool.close( sftpClient);
//				sshClient.disconnect();
//				on_failed( "connection.authenticateWithPassword( ... ) at compress()\n",
//					"Plugin error : compress" + Constant._line_separator
//						+ " Plugin name : " + _name + Constant._line_separator);
//				return false;
//			}
//		} catch (IOException e) {
//			connection.close();
//			SshTool.close( sftpClient);
//			sshClient.disconnect();
//			on_failed( "connection.authenticateWithPassword( ... ) at compress()\n",
//				"Plugin error : compress" + Constant._line_separator
//					+ " Plugin name : " + _name + Constant._line_separator);
//			return false;
//		}
//
//		if ( !compress( sshClient, sftpClient, connection)) {
//			connection.close();
//			SshTool.close( sftpClient);
//			sshClient.disconnect();
//			return false;
//		}
//
//		connection.close();
//
//		SshTool.close( sftpClient);
//		sshClient.disconnect();
//
//		return true;
//	}

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

		setEnabled( true);

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

			String model_builder_memory_size = _environment.get( Environment._model_builder_memory_size_key, Constant._defaultMemorySize);
			model_builder_memory_size = ( model_builder_memory_size.equals( "") ? "0" : model_builder_memory_size);
			fileWriter.write( "$JAVA_HOME/bin/java -jar "
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

		setEnabled( false);

		_status_textField.setText( "Transfer : Running ...");
		_status_textField.update( _status_textField.getGraphics());

		_progressBar.setMinimum( 0);
		_progressBar.setMaximum( 1);
		_progressBar.setValue( 0);
		_progressBar.update( _progressBar.getGraphics());

		File local_log_directory = get_local_log_directory();
		if ( null == local_log_directory){
			on_failed( "get_local_log_directory() at transfer( ... )\n", "get_local_log_directory() at transfer( ... )");
			return false;
		}

		try {
			sftpClient.get( _parameters._sgeout_directory_name + "/" + _uuid + ".zip",
				local_log_directory.getAbsolutePath() + "/" + _uuid + ".zip");
		} catch (IOException e) {
			//e.printStackTrace();
			//SshTool.rm( sftpClient, _parameters._sgeout_directory_name + "/" + _uuid + ".zip");
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

		if ( _generation_alternations < Integer.parseInt( _environment.get( Environment._number_of_generation_alternations_key, "10")) - 1) {
			List<Double> evaluation_values = prepare_to_restart( local_log_directory);
			if ( null == evaluation_values) {
				on_failed( "prepare_to_restart( ... ) at transfer( ... )\n", "prepare_to_restart( ... ) at transfer( ... )");
				return false;
			}

			// TODO デバッグ用
			//System.out.println( evaluation_values.toString());

			_ga_progressBar.setValue( ++_generation_alternations);
			_ga_progressBar.update( _ga_progressBar.getGraphics());
	
			if ( !start()) {
				on_failed( "start() at transfer( ... )\n", "start() at taransfer( ... )");
				return false;
			}

			setEnabled( true);

		} else {
			List<Double> evaluation_values = evaluate( local_log_directory);
			if ( null == evaluation_values) {
				on_failed( "evaluate( ... ) at transfer( ... )\n", "evaluate( ... ) at taransfer( ... )");
				return false;
			}

			// TODO デバッグ用
			//System.out.println( evaluation_values.toString());

			// 正常終了時の処理を行う
			if ( !output( local_log_directory)) {
				on_failed( "output( ... ) at transfer( ... )\n", "output( ... ) at taransfer( ... )");
				return false;
			}

			_ga_progressBar.setValue( ++_generation_alternations);
			_ga_progressBar.update( _ga_progressBar.getGraphics());
	
			setEnabled( true);

			_progressBar.setValue( 1);
			_progressBar.update( _progressBar.getGraphics());

			_status = "none";

			stop_timer();
			_status_textField.setText( "Done");
			_status_textField.update( _status_textField.getGraphics());
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
	protected void start_timer() {
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
	protected void stop_timer() {
		if ( null != _timer) {
			_timer.cancel();
			_timer = null;
			_running = false;
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.tool.timer.ITimerTaskImplementCallback#execute_timer_task(int)
	 */
	public void execute_timer_task(int id) {
		if ( _termination || _stop) {
			//stop_timer();
			//on_stop();
			return;
		}

		if ( _running)
			return;

		_running = true;

		SshClient sshClient = null;
		try {
			sshClient = SshTool.getSshClient( _parameters._grid_portal_ip_address, _parameters._username, _parameters._password);
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
			connection = new Connection( _parameters._grid_portal_ip_address, 22);
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
						on_failed( "transfer( ... ) at execute_timer_task( ... )\n", "transfer( ... ) at execute_timer_task( ... )", connection);
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

	/* (non-Javadoc)
	 * @see soars.plugin.visualshell.ga1.main.GAMonitorDlg#on_stop(java.awt.event.ActionEvent)
	 */
	protected void on_stop(ActionEvent actionEvent) {
		if ( !_termination
			&& JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog( _parent,
				ResourceManager.get_instance().get( "plugin.ga1.frame.cancel.confirm.message"), getTitle(), JOptionPane.YES_NO_OPTION))
			return;

		super.on_stop(actionEvent);
		stop_timer();
		setEnabled( false);
		delete();
		setEnabled( true);
		on_stop();
	}

	/**
	 * @param log
	 * @param message 
	 */
	private void on_failed(String log, String message) {
		if ( _termination)
			return;

		_status_textField.setText( "Failed ...");
		_status_textField.update( _status_textField.getGraphics());
		_log_textArea.append( log);
		_log_textArea.update( _log_textArea.getGraphics());
		_stop_button.setEnabled( false);
		setEnabled( false);
		delete();
		setEnabled( true);
		JOptionPane.showMessageDialog( _parent, message, getTitle(), JOptionPane.ERROR_MESSAGE);
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
		_log_textArea.append( log);
		_log_textArea.update( _log_textArea.getGraphics());
		_stop_button.setEnabled( false);
		setEnabled( false);
		delete( connection);
		setEnabled( true);
		JOptionPane.showMessageDialog( _parent, message, getTitle(), JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * 
	 */
	private void delete() {
		Connection connection = new Connection( _parameters._grid_portal_ip_address, 22);
		try {
			connection.connect();
		} catch (IOException e) {
			JOptionPane.showMessageDialog( _parent,
				"Plugin error : delete" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator,
				getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			if ( !connection.authenticateWithPassword( _parameters._username, _parameters._password)) {
				connection.close();
				JOptionPane.showMessageDialog( _parent,
					"Plugin error : delete" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator,
					getTitle(),
					JOptionPane.ERROR_MESSAGE);
				return;
			}
		} catch (IOException e) {
			connection.close();
			JOptionPane.showMessageDialog( _parent,
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

				_log_textArea.append( result);
				System.out.println( result);

				_job_id_list.clear();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
