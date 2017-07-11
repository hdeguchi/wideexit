/*
 * Created on 2006/06/20
 */
package soars.plugin.visualshell.grid3.main;

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
	private JTextField _gridPortalIpAddressTextField = null;

	/**
	 * 
	 */
	private JTextField _usernameTextField = null;

	/**
	 * 
	 */
	private JTextField _scriptDirectoryTextField = null;

	/**
	 * 
	 */
	private JTextField _logDirectoryTextField = null;

	/**
	 * 
	 */
	private JTextField _modelBuilderMemorySizeTextField = null;

	/**
	 * 
	 */
	private JTextField _numberOfTimesTextField = null;

	/**
	 * 
	 */
	private JTextField _logAnalysisConditionFilenameTextField = null;

	/**
	 * 
	 */
	private JTextField _localLogDirectoryTextField = null;

	/**
	 * 
	 */
	private JTextField _numberOfScriptsTextField = null;

	/**
	 * 
	 */
	private JTextField _numberOfSubmittedScriptsTextField = null;

	/**
	 * 
	 */
	private JTextField _statusTextField = null;

	/**
	 * 
	 */
	private JProgressBar _progressBar = null;

	/**
	 * 
	 */
	private JTextArea _errorLogTextArea = null;

	/**
	 * 
	 */
	private JScrollPane _errorLogTextAreaScrollPane = null;

	/**
	 * 
	 */
	private JButton _stopButton = null;

	/**
	 * 
	 */
	private List<JLabel> _labels = new ArrayList<JLabel>();

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
	private TreeMap<String, String> _analysisShellScriptDirectoryMap = new TreeMap<String, String>();

	/**
	 * 
	 */
	private List<String> _idList = new ArrayList<String>();

	/**
	 * 
	 */
	private int _id = 0;

	/**
	 * 
	 */
	private List<String> _jobIdList = new ArrayList<String>();

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
	private boolean _existUserDataDirectory = false;

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

		setup_gridPortalIpAddressTextField();

		insert_horizontal_glue();

		setup_usernameTextField();

		insert_horizontal_glue();

		setup_scriptDirectoryTextField();

		insert_horizontal_glue();

		setup_logDirectoryTextField();

		insert_horizontal_glue();

		setup_modelBuilderMemorySizeTextField();

		insert_horizontal_glue();

		setup_numberOfTimesTextField();

		insert_horizontal_glue();

		setup_logAnalysisConditionFilenameTextField();

		insert_horizontal_glue();

		setup_localLogDirectoryTextField();

		insert_horizontal_glue();

		setup_numberOfScriptsTextField();

		insert_horizontal_glue();

		setup_numberOfSubmittedScriptsTextField();

		insert_horizontal_glue();

		setup_statusTextField();

		insert_horizontal_glue();

		setup_progressBar();

		insert_horizontal_glue();

		setup_errorLogTextArea();

		insert_horizontal_glue();

		setup_stopButton();

		insert_horizontal_glue();


		adjust();


		pack();

		setLocationRelativeTo( _parent);

		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

		Toolkit.getDefaultToolkit().setDynamicLayout( true);

		setVisible( true);


		Image image = Resource.load_image_from_resource(
			Constant._resourceDirectory + "/image/icon/icon.png", getClass());
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
			ResourceManager.get_instance().get( "plugin.grid3.frame.exit.confirm.message"), getTitle(), JOptionPane.YES_NO_OPTION))
			return;

		stop_timer();
		_termination = true;
		on_stop( null);
		dispose();
	}

	/**
	 * 
	 */
	private void setup_gridPortalIpAddressTextField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.grid3.frame.gird.portal.ip.address"), SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);

		_gridPortalIpAddressTextField = new JTextField( _parameters._gridPortalIpAddress);
		_gridPortalIpAddressTextField.setEditable( false);
		_gridPortalIpAddressTextField.setPreferredSize( new Dimension( 400,
			_gridPortalIpAddressTextField.getPreferredSize().height));
		panel.add( _gridPortalIpAddressTextField);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_usernameTextField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.grid3.frame.username"), SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);

		_usernameTextField = new JTextField( _parameters._localUsername);
		_usernameTextField.setEditable( false);
		_usernameTextField.setPreferredSize( new Dimension( 400,
			_usernameTextField.getPreferredSize().height));
		panel.add( _usernameTextField);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_scriptDirectoryTextField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.grid3.frame.script.directory"), SwingConstants.RIGHT);
		label.setEnabled( _environment.get( Environment._simulationKey, "false").equals( "true"));
		_labels.add( label);
		panel.add( label);


		_scriptDirectoryTextField = new JTextField(
			_environment.get( Environment._simulationKey, "false").equals( "true")
				? _environment.get( Environment._scriptDirectoryKey, "")
				: "");
		_scriptDirectoryTextField.setEditable( false);
		_scriptDirectoryTextField.setPreferredSize( new Dimension( 400,
			_scriptDirectoryTextField.getPreferredSize().height));
		panel.add( _scriptDirectoryTextField);


		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_logDirectoryTextField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.grid3.frame.log.directory"), SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);


		_logDirectoryTextField = new JTextField(
			_environment.get( Environment._logDirectoryKey, ""));
		_logDirectoryTextField.setEditable( false);
		_logDirectoryTextField.setPreferredSize( new Dimension( 400,
			_logDirectoryTextField.getPreferredSize().height));
		panel.add( _logDirectoryTextField);


		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_modelBuilderMemorySizeTextField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.grid3.frame.model.builder.memory.size"), SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);

		_modelBuilderMemorySizeTextField = new JTextField(
			_environment.get( Environment._modelBuilderMemorySizeKey, Constant._defaultMemorySize));
		_modelBuilderMemorySizeTextField.setHorizontalAlignment( SwingConstants.RIGHT);
		_modelBuilderMemorySizeTextField.setEditable( false);
		_modelBuilderMemorySizeTextField.setPreferredSize( new Dimension( 200,
			_modelBuilderMemorySizeTextField.getPreferredSize().height));
		panel.add( _modelBuilderMemorySizeTextField);

		label = new JLabel( "MB");
		panel.add( label);


		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_numberOfTimesTextField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.grid3.frame.number.of.times"), SwingConstants.RIGHT);
		label.setEnabled( _environment.get( Environment._simulationKey, "false").equals( "true"));
		_labels.add( label);
		panel.add( label);


		_numberOfTimesTextField = new JTextField(
			_environment.get( Environment._simulationKey, "false").equals( "true")
				? _environment.get( Environment._numberOfTimesForGridKey, "1")
				: "");
		_numberOfTimesTextField.setHorizontalAlignment( SwingConstants.RIGHT);
		_numberOfTimesTextField.setEditable( false);
		_numberOfTimesTextField.setPreferredSize( new Dimension( 200,
			_numberOfTimesTextField.getPreferredSize().height));
		panel.add( _numberOfTimesTextField);


		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_logAnalysisConditionFilenameTextField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.grid3.frame.log.analysis.condition.filename"), SwingConstants.RIGHT);
		label.setEnabled( _environment.get( Environment._logAnalysisKey, "false").equals( "true"));
		_labels.add( label);
		panel.add( label);


		_logAnalysisConditionFilenameTextField = new JTextField(
			_environment.get( Environment._logAnalysisKey, "false").equals( "true")
				? _environment.get( Environment._logAnalysisConditionFilenameKey, "")
				: "");
		_logAnalysisConditionFilenameTextField.setEditable( false);
		_logAnalysisConditionFilenameTextField.setPreferredSize( new Dimension( 400,
			_logAnalysisConditionFilenameTextField.getPreferredSize().height));
		panel.add( _logAnalysisConditionFilenameTextField);


		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_localLogDirectoryTextField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.grid3.frame.local.log.directory"), SwingConstants.RIGHT);
		label.setEnabled( _environment.get( Environment._logTransferKey, "false").equals( "true"));
		_labels.add( label);
		panel.add( label);


		_localLogDirectoryTextField = new JTextField(
			_environment.get( Environment._logTransferKey, "false").equals( "true")
				? _environment.get( Environment._localLogDirectoryKey, "")
				: "");
		_localLogDirectoryTextField.setEditable( false);
		_localLogDirectoryTextField.setPreferredSize( new Dimension( 400,
			_localLogDirectoryTextField.getPreferredSize().height));
		panel.add( _localLogDirectoryTextField);


		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_numberOfScriptsTextField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.grid3.frame.number.of.script"), SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);


		_numberOfScriptsTextField = new JTextField( "0");
		_numberOfScriptsTextField.setHorizontalAlignment( SwingConstants.RIGHT);
		_numberOfScriptsTextField.setEditable( false);
		_numberOfScriptsTextField.setPreferredSize( new Dimension( 200,
			_numberOfScriptsTextField.getPreferredSize().height));
		panel.add( _numberOfScriptsTextField);


		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_numberOfSubmittedScriptsTextField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.grid3.frame.number.of.submitted.script"), SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);


		_numberOfSubmittedScriptsTextField = new JTextField( "0");
		_numberOfSubmittedScriptsTextField.setHorizontalAlignment( SwingConstants.RIGHT);
		_numberOfSubmittedScriptsTextField.setEditable( false);
		_numberOfSubmittedScriptsTextField.setPreferredSize( new Dimension( 200,
			_numberOfSubmittedScriptsTextField.getPreferredSize().height));
		panel.add( _numberOfSubmittedScriptsTextField);


		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_statusTextField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.grid3.frame.status"), SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);


		_statusTextField = new JTextField( "");
		_statusTextField.setEditable( false);
		_statusTextField.setPreferredSize( new Dimension( 400,
			_statusTextField.getPreferredSize().height));
		panel.add( _statusTextField);


		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_progressBar() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.grid3.frame.progress"), SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);


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
	private void setup_errorLogTextArea() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.grid3.frame.error.log"));
		panel.add( label);

		getContentPane().add( panel);


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_errorLogTextArea = new JTextArea();
		_errorLogTextArea.setEditable( false);
		_errorLogTextArea.setTabSize( 2);

		_errorLogTextAreaScrollPane = new JScrollPane();
		_errorLogTextAreaScrollPane.getViewport().setView( _errorLogTextArea);
		panel.add( _errorLogTextAreaScrollPane);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_stopButton() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, pad, 0));

		JLabel label = new JLabel( "");
		_labels.add( label);
		panel.add( label);


		_stopButton = new JButton( ResourceManager.get_instance().get( "dialog.cancel"));
		_stopButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_stop( arg0);
			}
		});
		panel.add( _stopButton);


		getContentPane().add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_stop(ActionEvent actionEvent) {
		if ( !_termination
			&& JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog( this,
				ResourceManager.get_instance().get( "plugin.grid3.frame.cancel.confirm.message"), getTitle(), JOptionPane.YES_NO_OPTION))
			return;

		_stop = true;
		_stopButton.setEnabled( false);
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
			ResourceManager.get_instance().get( "plugin.grid3.frame.canceled"),
			getTitle(),
			JOptionPane.INFORMATION_MESSAGE);
		_statusTextField.setText( "Canceled ...");
		_statusTextField.update( _statusTextField.getGraphics());
		_errorLogTextArea.append( ResourceManager.get_instance().get( "plugin.grid3.frame.canceled"));
		setEnabled( true);
		_parent.setEnabled( true);
		//_stop_button.setEnabled( false);
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

		_errorLogTextAreaScrollPane.setPreferredSize( new Dimension( width + 405, 100));
	}

	/**
	 * @param sshClient
	 * @param sftpClient
	 * @param directory
	 * @param connection
	 * @param shellScriptFilename
	 * @return
	 */
	private String execute(SshClient sshClient, SftpClient sftpClient, SftpFile directory, Connection connection, String shellScriptFilename) {
		synchronized ( _jobIdList) {
			if ( _termination || _stop)
				return null;

			if ( !SshTool.directory_exists( sftpClient, _parameters._sgeoutDirectoryName + "/" + _uuid))
				sftpClient.mkdirs( _parameters._sgeoutDirectoryName + "/" + _uuid);

			if ( !SshTool.directory_exists( sftpClient, _parameters._sgeoutDirectoryName + "/" + _uuid))
				return null;

			String command = SshTool.execute1( sshClient,
				_parameters._submitShellScriptFilename + " "
				+ "'" + Constant._jobName + "' "
				+ "'" + _parameters._sgeoutDirectoryName + "/" + _uuid + "' "
				+ "'" + _parameters._sgeoutDirectoryName + "/" + _uuid + "' "
				+ "'" + directory.getAbsolutePath() + "/" + shellScriptFilename + "'");

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
					_errorLogTextArea.append( text);
				else {
					int index = 0;
					String[] words = result.split( " ");
					if ( words[ 0].toLowerCase().equals( "your") && words[ 1].toLowerCase().equals( "job"))
						index = 2;
					else if ( words[ 1].toLowerCase().equals( "your") && words[ 2].toLowerCase().equals( "job"))
						index = 3;
					else {
						_errorLogTextArea.append( text);
						return null;
					}

					double d;
					try {
						d = Double.parseDouble( words[ index]);
					} catch (NumberFormatException e) {
						//e.printStackTrace();
						_errorLogTextArea.append( text);
						return null;
					}
					_jobIdList.add( words[ index]);
				}

				return result;
			} catch (IOException e) {
				return null;
			}
		}

//		String result = SshTool.execute1( sshClient,
//				_parameters._submit_shell_script_file_name + " "
//				+ "'" + Constant._job_name + "' "
//				+ "'" + _parameters._sgeout_directory_name + "/" + _uuid + "' "
//				+ "'" + _parameters._sgeout_directory_name + "/" + _uuid + "' "
//				+ "'" + directory.getAbsolutePath() + "/" + shell_script_file_name + "'");
//
//		if ( null == result)
//			return null;
//
//		return ( ( result.toLowerCase().startsWith( "your job") && result.trim().endsWith( "has been submitted")) ? result : null);

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

	/* (Non Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		if ( _environment.get( Environment._simulationKey, "false").equals( "true")) {
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

		if ( _environment.get( Environment._logAnalysisKey, "false").equals( "true")) {
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

		if ( _environment.get( Environment._logTransferKey, "false").equals( "true")) {
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
		synchronized ( _jobIdList) {
			_jobIdList.clear();
		}

		_statusTextField.setText( "Simulation : Exporting ...");
		_statusTextField.update( _statusTextField.getGraphics());

		_numberOfScriptsTextField.setText( "0");
		_numberOfScriptsTextField.update( _numberOfScriptsTextField.getGraphics());

		_numberOfSubmittedScriptsTextField.setText( "0");
		_numberOfSubmittedScriptsTextField.update( _numberOfSubmittedScriptsTextField.getGraphics());

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
		_parent.setEnabled( true);

		_statusTextField.setText( "Simulation : Submitting ...");
		_statusTextField.update( _statusTextField.getGraphics());

		_progressBar.setMinimum( 0);
		_progressBar.setMaximum( _number);
		_progressBar.setValue( 0);
		_progressBar.update( _progressBar.getGraphics());

		if ( !submit()) {
			on_failed( "submit() at simulate()\n", "submit() at simulate()");
			return false;
		}

		_number = _idList.size();
		_progressBar.setMinimum( 0);
		_progressBar.setMaximum( _number);
		_progressBar.setValue( 0);
		_progressBar.update( _progressBar.getGraphics());

		_statusTextField.setText( "Simulation : Running ...");
		_statusTextField.update( _statusTextField.getGraphics());

		_status = "simulation";

		return true;
	}

	/**
	 * @return
	 */
	private boolean export_soars_scripts() {
		// TODO Auto-generated method stub
		List<Object> resultList = new ArrayList<Object>();
		if ( !Reflection.execute_static_method( "soars.application.visualshell.layer.LayerManager", "get_instance", new Class[ 0], new Object[ 0], resultList)) {
			JOptionPane.showMessageDialog( this,
				"Plugin error : Reflection.execute_static_method( ... )" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator
				+ " Class name : soars.application.visualshell.layer.LayerManager" + Constant._lineSeparator
				+ " Method name : get_instance" + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.grid3.frame.title"),
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
				ResourceManager.get_instance().get( "plugin.grid3.frame.title"),
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
				ResourceManager.get_instance().get( "plugin.grid3.frame.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		Boolean result = ( Boolean)resultList.get( 0);
		_existUserDataDirectory = result.booleanValue();



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
				_environment.get( Environment._scriptDirectoryKey, ""),
				_environment.get( Environment._logDirectoryKey, ""),
				_parameters._programDirectoryName,
				new Integer( _environment.get( Environment._numberOfTimesForGridKey, "1")),
				_parameters._gridPortalIpAddress,
				_parameters._username,
				_parameters._password,
				intBuffer,
				_numberOfScriptsTextField},
			resultList)
			|| resultList.isEmpty() || null == resultList.get( 0) || !( resultList.get( 0) instanceof Boolean) || !(( Boolean)resultList.get( 0)).booleanValue()) {
			JOptionPane.showMessageDialog( this,
				"Plugin error : Reflection.execute_class_method( ... )" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator
				+ " Class name : soars.application.visualshell.layer.LayerManager" + Constant._lineSeparator
				+ " Method name : export_data_for_grid" + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.grid3.frame.title"),
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
		SshClient sshClient = SshTool.getSshClient( _parameters._gridPortalIpAddress, _parameters._username, _parameters._password);
		if ( null == sshClient) {
			JOptionPane.showMessageDialog( this,
				"Plugin error : submit" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.grid3.frame.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		SftpClient sftpClient = SshTool.getSftpClient( sshClient);
		if ( null == sftpClient) {
			sshClient.disconnect();
			JOptionPane.showMessageDialog( this,
				"Plugin error : submit" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.grid3.frame.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( !SshTool.directory_exists( sftpClient, _environment.get( Environment._scriptDirectoryKey, ""))) {
			SshTool.close( sftpClient);
			sshClient.disconnect();
			JOptionPane.showMessageDialog( this,
				"Plugin error : submit" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.grid3.frame.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		Connection connection = new Connection( _parameters._gridPortalIpAddress, 22);
		try {
			connection.connect();
		} catch (IOException e) {
			SshTool.close( sftpClient);
			sshClient.disconnect();
			JOptionPane.showMessageDialog( this,
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
				JOptionPane.showMessageDialog( this,
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
			JOptionPane.showMessageDialog( this,
				"Plugin error : submit" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator,
				getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( !submit( sshClient, sftpClient,
			new SftpFile( _environment.get( Environment._scriptDirectoryKey, "")), connection)) {
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

		_idList.add( String.valueOf( _id++));

		String result = execute( sshClient, sftpClient, directory, connection, Constant._simulationShellScriptFilename);
		if ( null == result)
			return false;

		_numberOfSubmittedScriptsTextField.setText( String.valueOf( _idList.size()));
		_numberOfSubmittedScriptsTextField.update( _numberOfSubmittedScriptsTextField.getGraphics());

		_progressBar.setValue( _idList.size());
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
		File simulationShellScriptFile;
		try {
			simulationShellScriptFile = File.createTempFile( "simulation_", ".sh");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter( simulationShellScriptFile);
		} catch (IOException e) {
			e.printStackTrace();
			simulationShellScriptFile.delete();
			return false;
		}

		try {
			fileWriter.write( "#!/bin/sh\n\n");
			fileWriter.write( "JAVA_HOME=" + _parameters._javaHome + "\n\n");

			String modelBuilderMemorySize = _environment.get( Environment._modelBuilderMemorySizeKey, Constant._defaultMemorySize);
			modelBuilderMemorySize = ( modelBuilderMemorySize.equals( "") ? "0" : modelBuilderMemorySize);

			if ( _existUserDataDirectory) {
				String scriptDirectoryName = _environment.get( Environment._scriptDirectoryKey, "");
				String parentDirectoryName = _environment.get( Environment._logDirectoryKey, "") + directory.getAbsolutePath().substring( scriptDirectoryName.length());
				String userDataDirectoryName = parentDirectoryName + "/" + Constant._userDataDirectory;

				fileWriter.write( "$JAVA_HOME/bin/java -jar "
					+ "'" + _parameters._executorJarFilename + "' "
					+ "decompression "
					+ "'" + _parameters._homeDirectoryName + "' "
					+ "'" + _parameters._sgeoutDirectoryName + "/" + _uuid + "/" + Constant._decompressionName + "' "
					+ String.valueOf( _id) + " "
					+ "'" + _parameters._javaHome + "' "
					+ modelBuilderMemorySize + " "
					+ "'" + scriptDirectoryName + "/" + Constant._userDataZipFilename + "' "
					+ "'" + parentDirectoryName + "' "
					+ "$@\n\n"
					+ "if [ -d \"" + userDataDirectoryName + "\" ];\n"
					+ "then\n"
					+ "\techo 'decompression completed!'\n"
					//+ "\techo 'completed!' > \"" + _parameters._sgeout_directory_name + "/" + _uuid + "/" + Constant._decompression_name + "." + String.valueOf( _id) + "\"\n"
					+ "else\n"
					+ "\techo 'error!' > \"" + _parameters._sgeoutDirectoryName + "/" + _uuid + "/" + Constant._decompressionName + "." + String.valueOf( _id) + "\"\n"
					+ "\texit 0\n"
					+ "fi\n\n");

				fileWriter.write( "$JAVA_HOME/bin/java -jar "
					+ "'" + _parameters._executorJarFilename + "' "
					+ "simulation "
					+ "'" + _parameters._homeDirectoryName + "' "
					+ "'" + _parameters._sgeoutDirectoryName + "/" + _uuid + "/" + Constant._terminationName + "' "
					+ String.valueOf( _id) + " "
					+ "'" + _parameters._javaHome + "' "
					+ modelBuilderMemorySize + " "
					+ "'" + filename + "' "
					+ "'" + userDataDirectoryName + "' "
					+ "$@\n\n"
					+ "if [ -f \"" + _parameters._sgeoutDirectoryName + "/" + _uuid + "/" + Constant._terminationName + "." + String.valueOf( _id) + "\" ];\n"
					+ "then\n"
					+ "\texit 0\n"
					+ "else\n"
					+ "\techo 'error!' > \"" + _parameters._sgeoutDirectoryName + "/" + _uuid + "/" + Constant._terminationName + "." + String.valueOf( _id) + "\"\n"
					+ "fi\n\n");
			} else {
				fileWriter.write( "$JAVA_HOME/bin/java -jar "
					+ "'" + _parameters._executorJarFilename + "' "
					+ "simulation "
					+ "'" + _parameters._homeDirectoryName + "' "
					+ "'" + _parameters._sgeoutDirectoryName + "/" + _uuid + "/" + Constant._terminationName + "' "
					+ String.valueOf( _id) + " "
					+ "'" + _parameters._javaHome + "' "
					+ modelBuilderMemorySize + " "
					+ "'" + filename + "' "
					+ "$@\n\n"
					+ "if [ -f \"" + _parameters._sgeoutDirectoryName + "/" + _uuid + "/" + Constant._terminationName + "." + String.valueOf( _id) + "\" ];\n"
					+ "then\n"
					+ "\texit 0\n"
					+ "else\n"
					+ "\techo 'error!' > \"" + _parameters._sgeoutDirectoryName + "/" + _uuid + "/" + Constant._terminationName + "." + String.valueOf( _id) + "\"\n"
					+ "fi\n\n");
			}

			fileWriter.write( "exit 0\n");
		} catch (IOException e) {
			e.printStackTrace();
			simulationShellScriptFile.delete();
			return false;
		}

		try {
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			simulationShellScriptFile.delete();
			return false;
		}

		try {
			sftpClient.put( simulationShellScriptFile.getAbsolutePath(), directory.getAbsolutePath() + "/" + Constant._simulationShellScriptFilename);
		} catch (IOException e) {
			e.printStackTrace();
			simulationShellScriptFile.delete();
			return false;
		}

		simulationShellScriptFile.delete();

		try {
			sftpClient.chmod( 0744, directory.getAbsolutePath() + "/" + Constant._simulationShellScriptFilename);
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
		synchronized ( _jobIdList) {
			_jobIdList.clear();
		}

		SshClient sshClient = SshTool.getSshClient( _parameters._gridPortalIpAddress, _parameters._username, _parameters._password);
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

		if ( !SshTool.directory_exists( sftpClient, _environment.get( Environment._logDirectoryKey, ""))) {
			SshTool.close( sftpClient);
			sshClient.disconnect();
			on_failed( "SshTool.directory_exists( ... ) at analyze()\n",
				"Plugin error : analyze" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator);
			return false;
		}

		Connection connection = new Connection( _parameters._gridPortalIpAddress, 22);
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
		synchronized ( _jobIdList) {
			_jobIdList.clear();
		}

		_parent.setEnabled( false);
		setEnabled( false);

		_number = 0;

		_statusTextField.setText( "Analysis : Exporting ...");
		_statusTextField.update( _statusTextField.getGraphics());

		_numberOfScriptsTextField.setText( "0");
		_numberOfScriptsTextField.update( _numberOfScriptsTextField.getGraphics());

		_numberOfSubmittedScriptsTextField.setText( "0");
		_numberOfSubmittedScriptsTextField.update( _numberOfSubmittedScriptsTextField.getGraphics());

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

		_statusTextField.setText( "Analysis : Submitting ...");
		_statusTextField.update( _statusTextField.getGraphics());

		_progressBar.setMinimum( 0);
		_progressBar.setMaximum( _number);
		_progressBar.setValue( 0);
		_progressBar.update( _progressBar.getGraphics());

		Iterator iterator = _analysisShellScriptDirectoryMap.entrySet().iterator();

		while ( iterator.hasNext()) {
			if ( _stop)
				break;

			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String id = ( String)entry.getKey();
			String analysisShellScriptDirectory = ( String)entry.getValue();

			String result = execute( sshClient, sftpClient,
				new SftpFile( analysisShellScriptDirectory), connection, Constant._analysisShellScriptFilename);
			if ( null == result) {
				on_failed( "execute( ... ) at analyze( ... )\n", "execute( ... ) at analyze( ... )", connection);
				return false;
			}

			_idList.add( id);

			_numberOfSubmittedScriptsTextField.setText( String.valueOf( _idList.size()));
			_numberOfSubmittedScriptsTextField.update( _numberOfSubmittedScriptsTextField.getGraphics());

			_progressBar.setValue( _idList.size());
			_progressBar.update( _progressBar.getGraphics());
		}

		_number = _idList.size();
		_progressBar.setMinimum( 0);
		_progressBar.setMaximum( _number);
		_progressBar.setValue( 0);
		_progressBar.update( _progressBar.getGraphics());

		_statusTextField.setText( "Analysis : Running ...");
		_statusTextField.update( _statusTextField.getGraphics());

		_status = "analysis";

		return true;
	}

	/**
	 * @param sftpClient
	 * @return
	 */
	private boolean export_for_analysis(SftpClient sftpClient) {
		if ( !SshTool.directory_exists( sftpClient, _environment.get( Environment._logDirectoryKey, "")))
			return false;

		File logAnalysisConditionFile = new File( _environment.get( Environment._logAnalysisConditionFilenameKey, ""));
		if ( null == logAnalysisConditionFile || !logAnalysisConditionFile.exists() || !logAnalysisConditionFile.isFile())
			return false;

		if ( !export_for_analysis( sftpClient, new SftpFile( _environment.get( Environment._logDirectoryKey, "")), logAnalysisConditionFile))
			return false;

		_number = _analysisShellScriptDirectoryMap.size();

		return true;
	}

	/**
	 * @param sftpClient
	 * @param directory
	 * @param logAnalysisConditionFile
	 * @return
	 */
	private boolean export_for_analysis(SftpClient sftpClient, SftpFile directory, File logAnalysisConditionFile) {
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
					if ( !export_for_analysis( sftpClient, directory.getAbsolutePath(), logAnalysisConditionFile))
						return false;

					break;
				} else
					export_for_analysis( sftpClient, sftpFile, logAnalysisConditionFile);
			}
		}
	
		return true;
	}

	/**
	 * @param sftpClient
	 * @param directory
	 * @param logAnalysisConditionFile
	 * @return
	 */
	private boolean export_for_analysis(SftpClient sftpClient, String directory, File logAnalysisConditionFile) {
		try {
			sftpClient.put( logAnalysisConditionFile.getAbsolutePath(), directory + "/" + Constant._analysisConditionScriptFilename);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}


		File analysisShellScriptFile;
		try {
			analysisShellScriptFile = File.createTempFile( "analysis_", ".sh");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter( analysisShellScriptFile);
		} catch (IOException e) {
			e.printStackTrace();
			analysisShellScriptFile.delete();
			return false;
		}

		try {
			fileWriter.write( "#!/bin/sh\n\n");
			fileWriter.write( "JAVA_HOME=" + _parameters._javaHome + "\n\n");

			String modelBuilderMemorySize = _environment.get( Environment._modelBuilderMemorySizeKey, Constant._defaultMemorySize);
			modelBuilderMemorySize = ( modelBuilderMemorySize.equals( "") ? "0" : modelBuilderMemorySize);
			fileWriter.write( "$JAVA_HOME/bin/java -jar "
				+ "'" + _parameters._executorJarFilename + "' "
				+ "analysis "
				+ "'" + _parameters._homeDirectoryName + "' "
				+ "'" + _parameters._sgeoutDirectoryName + "/" + _uuid + "/" + Constant._terminationName + "' "
				+ String.valueOf( _id) + " "
				+ "'" + _parameters._javaHome + "' "
				+ modelBuilderMemorySize + " "
				+ "'" + directory + "' "
				+ "'" + directory + "' "
				+ "$@\n\n"
				+ "if [ -f \"" + _parameters._sgeoutDirectoryName + "/" + _uuid + "/" + Constant._terminationName + "." + String.valueOf( _id) + "\" ];\n"
				+ "then\n"
				+ "\texit 0\n"
				+ "else\n"
				+ "\techo 'error!' > \"" + _parameters._sgeoutDirectoryName + "/" + _uuid + "/" + Constant._terminationName + "." + String.valueOf( _id) + "\"\n"
				+ "fi\n\n");

			fileWriter.write( "exit 0\n");
		} catch (IOException e) {
			e.printStackTrace();
			analysisShellScriptFile.delete();
			return false;
		}

		try {
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			analysisShellScriptFile.delete();
			return false;
		}

		try {
			sftpClient.put( analysisShellScriptFile.getAbsolutePath(), directory + "/" + Constant._analysisShellScriptFilename);
		} catch (IOException e) {
			e.printStackTrace();
			analysisShellScriptFile.delete();
			return false;
		}

		analysisShellScriptFile.delete();

		try {
			sftpClient.chmod( 0744, directory + "/" + Constant._analysisShellScriptFilename);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		_analysisShellScriptDirectoryMap.put( String.valueOf( _id++), directory);

		_numberOfScriptsTextField.setText( String.valueOf( _analysisShellScriptDirectoryMap.size()));
		_numberOfScriptsTextField.update( _numberOfScriptsTextField.getGraphics());

		return true;
	}

	/**
	 * @return
	 */
	private boolean compress() {
		synchronized ( _jobIdList) {
			_jobIdList.clear();
		}

		SshClient sshClient = SshTool.getSshClient( _parameters._gridPortalIpAddress, _parameters._username, _parameters._password);
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

		if ( !SshTool.directory_exists( sftpClient, _environment.get( Environment._logDirectoryKey, ""))) {
			SshTool.close( sftpClient);
			sshClient.disconnect();
			on_failed( "SshTool.directory_exists( ... ) at compress()\n",
				"Plugin error : compress" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator);
			return false;
		}

		Connection connection = new Connection( _parameters._gridPortalIpAddress, 22);
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
		synchronized ( _jobIdList) {
			_jobIdList.clear();
		}

		_parent.setEnabled( false);
		setEnabled( false);

		_number = 0;

		_statusTextField.setText( "Compress : Exporting ...");
		_statusTextField.update( _statusTextField.getGraphics());

		_numberOfScriptsTextField.setText( "0");
		_numberOfScriptsTextField.update( _numberOfScriptsTextField.getGraphics());

		_numberOfSubmittedScriptsTextField.setText( "0");
		_numberOfSubmittedScriptsTextField.update( _numberOfSubmittedScriptsTextField.getGraphics());

		_progressBar.setMinimum( 0);
		_progressBar.setMaximum( 1);
		_progressBar.setValue( 0);
		_progressBar.update( _progressBar.getGraphics());

		SftpFile directory = new SftpFile( _environment.get( Environment._scriptDirectoryKey, ""));
		if ( !export_for_compression( sftpClient, directory)) {
			on_failed( "export_for_compression( ... ) at compress( ... )\n", "export_for_compression( ... ) at compress( ... )", connection);
			return false;
		}

		setEnabled( true);
		_parent.setEnabled( true);

		_numberOfScriptsTextField.setText( "1");
		_numberOfScriptsTextField.update( _numberOfScriptsTextField.getGraphics());

		_statusTextField.setText( "Compress : Submitting ...");
		_statusTextField.update( _statusTextField.getGraphics());

		//String result = execute( sshClient, sftpClient, directory, connection, Constant._compressionShellScriptFilename);
		String result = execute_compression( sshClient, sftpClient, directory, connection, Constant._compressionShellScriptFilename);
		if ( null == result) {
			on_failed( "execute( ... ) at compress( ... )\n", "execute( ... ) at compress( ... )", connection);
			return false;
		}

		_idList.add( String.valueOf( _id++));

		_numberOfSubmittedScriptsTextField.setText( String.valueOf( _idList.size()));
		_numberOfSubmittedScriptsTextField.update( _numberOfSubmittedScriptsTextField.getGraphics());

		_number = _idList.size();
		_progressBar.setMinimum( 0);
		_progressBar.setMaximum( _number);
		_progressBar.setValue( 0);
		_progressBar.update( _progressBar.getGraphics());

		_statusTextField.setText( "Compress : Running ...");
		_statusTextField.update( _statusTextField.getGraphics());

		_status = "compression";

		return true;
	}

	/**
	 * @param sftpClient
	 * @param directory
	 * @return
	 */
	private boolean export_for_compression(SftpClient sftpClient, SftpFile directory) {
		File compressionShellScriptFile;
		try {
			compressionShellScriptFile = File.createTempFile( "compression_", ".sh");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter( compressionShellScriptFile);
		} catch (IOException e) {
			e.printStackTrace();
			compressionShellScriptFile.delete();
			return false;
		}

		try {
			fileWriter.write( "#!/bin/sh\n\n");
			fileWriter.write( "JAVA_HOME=" + _parameters._javaHome + "\n\n");

			String modelBuilderMemorySize = _environment.get( Environment._modelBuilderMemorySizeKey, Constant._defaultMemorySize);
			modelBuilderMemorySize = ( modelBuilderMemorySize.equals( "") ? "0" : modelBuilderMemorySize);
			fileWriter.write( "$JAVA_HOME/bin/java -jar "
				+ "'" + _parameters._executorJarFilename + "' "
				+ "compression "
				+ "'" + _parameters._homeDirectoryName + "' "
				+ "'" + _parameters._sgeoutDirectoryName + "/" + _uuid + "/" + Constant._terminationName + "' "
				+ String.valueOf( _id) + " "
				+ "'" + _parameters._javaHome + "' "
				+ modelBuilderMemorySize + " "
				+ "'" + _parameters._sgeoutDirectoryName + "/" + _uuid + ".zip" + "' "
				+ "'" + _environment.get( Environment._logDirectoryKey, "") + "' "
				+ "'" + _parameters._sgeoutDirectoryName + "/" + _uuid + "' "
				+ "$@\n\n"
				+ "if [ -f \"" + _parameters._sgeoutDirectoryName + "/" + _uuid + "/" + Constant._terminationName + "." + String.valueOf( _id) + "\" ];\n"
				+ "then\n"
				+ "\texit 0\n"
				+ "else\n"
				+ "\techo 'error!' > \"" + _parameters._sgeoutDirectoryName + "/" + _uuid + "/" + Constant._terminationName + "." + String.valueOf( _id) + "\"\n"
				+ "fi\n\n");

			fileWriter.write( "exit 0\n");
		} catch (IOException e) {
			e.printStackTrace();
			compressionShellScriptFile.delete();
			return false;
		}

		try {
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			compressionShellScriptFile.delete();
			return false;
		}

		try {
			sftpClient.put( compressionShellScriptFile.getAbsolutePath(), directory.getAbsolutePath() + "/" + Constant._compressionShellScriptFilename);
		} catch (IOException e) {
			e.printStackTrace();
			compressionShellScriptFile.delete();
			return false;
		}

		compressionShellScriptFile.delete();

		try {
			sftpClient.chmod( 0744, directory.getAbsolutePath() + "/" + Constant._compressionShellScriptFilename);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param sshClient
	 * @param sftpClient
	 * @param directory
	 * @param connection
	 * @param shellScriptFilename
	 * @return
	 */
	private String execute_compression(SshClient sshClient, SftpClient sftpClient, SftpFile directory, Connection connection, String shellScriptFilename) {
		synchronized ( _jobIdList) {
			if ( _termination || _stop)
				return null;

			if ( !SshTool.directory_exists( sftpClient, _parameters._sgeoutDirectoryName + "/" + _uuid))
				sftpClient.mkdirs( _parameters._sgeoutDirectoryName + "/" + _uuid);

			if ( !SshTool.directory_exists( sftpClient, _parameters._sgeoutDirectoryName + "/" + _uuid))
				return null;

			String command = SshTool.execute1( sshClient, "nohup " + "'" + directory.getAbsolutePath() + "/" + shellScriptFilename + "' &");

			//System.out.println( command);

			String text = "";
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

					text += ( line + Constant._lineSeparator);
				}

				bufferedReader.close();

				session.close();

				return "success!";
			} catch (IOException e) {
				return null;
			}
		}
	}

	/**
	 * @param sftpClient
	 */
	private boolean transfer(SftpClient sftpClient) {
		synchronized ( _jobIdList) {
			_jobIdList.clear();
		}

		_parent.setEnabled( false);
		setEnabled( false);

		_statusTextField.setText( "Transfer : Running ...");
		_statusTextField.update( _statusTextField.getGraphics());

		_progressBar.setMinimum( 0);
		_progressBar.setMaximum( 1);
		_progressBar.setValue( 0);
		_progressBar.update( _progressBar.getGraphics());

		File localLogDirectory = new File( _environment.get( Environment._localLogDirectoryKey, ""));
		if ( !make_local_directory( localLogDirectory)) {
			on_failed( "make_local_directory( ... ) at transfer( ... )\n", "make_local_directory( ... ) at transfer( ... )");
			return false;
		}

		// TODO Auto-generated method stub
		try {
			sftpClient.get( _parameters._sgeoutDirectoryName + "/" + _uuid + ".zip",
				localLogDirectory.getAbsolutePath() + "/" + _uuid + ".zip");
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

		_statusTextField.setText( "Unpack : Running ...");
		_statusTextField.update( _statusTextField.getGraphics());

		if ( !unpack( localLogDirectory.getAbsolutePath() + "/" + _uuid + ".zip",
			localLogDirectory.getAbsolutePath(),
			_environment.get( Environment._logDirectoryKey, ""),
			_parameters._sgeoutDirectoryName)) {
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
	 * @param localDirectory
	 * @return
	 */
	private boolean make_local_directory(File localDirectory) {
		if ( localDirectory.exists()) {
//			if ( localDirectory.isDirectory())
//				_errorLogTextArea.append( "Overwritten! : " + localDirectory.getAbsolutePath() + "\n");
//			else {
			if ( !localDirectory.isDirectory()) {
				if ( !localDirectory.delete()) {
					_errorLogTextArea.append( "Could not make! : " + localDirectory.getAbsolutePath() + "\n");
					return false;
				}
	
				if ( !localDirectory.mkdirs()) {
					_errorLogTextArea.append( "Could not make! : " + localDirectory.getAbsolutePath() + "\n");
					return false;
				}
			}
		} else {
			if ( !localDirectory.mkdirs()) {
				_errorLogTextArea.append( "Could not make! : " + localDirectory.getAbsolutePath() + "\n");
				return false;
			}
		}
		return true;
	}
	/**
	 * @param path
	 * @param localLogDirectory
	 * @param logDirectory
	 * @param sgeoutDirectory
	 * @return
	 */
	private boolean unpack(String path, String localLogDirectory, String logDirectory, String sgeoutDirectory) {
		ZipInputStream zipInputStream = null;
		try {
			zipInputStream = new ZipInputStream( new FileInputStream( path));
			ZipEntry zipEntry = null;
			while ( null != ( zipEntry = zipInputStream.getNextEntry())) {
				String filename = zipEntry.getName();
				String name = "";
				if ( filename.startsWith( logDirectory) && filename.length() > logDirectory.length() + 1)
					name = filename.substring( logDirectory.length());
				else if ( filename.startsWith( sgeoutDirectory) && filename.length() > sgeoutDirectory.length() + 1)
					name = filename.substring( sgeoutDirectory.length());
				else {
					zipInputStream.closeEntry();
					continue;
				}

				if ( name.endsWith( "/")) {
					File directory = new File( localLogDirectory + name);
					if ( !directory.mkdirs()) {
						zipInputStream.closeEntry();
						return false;
					}

					zipInputStream.closeEntry();
					continue;
				} else {
					File file = new File( localLogDirectory + name);
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

				File file = new File( localLogDirectory + name);
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
			sshClient = SshTool.getSshClient( _parameters._gridPortalIpAddress, _parameters._username, _parameters._password);
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
			connection = new Connection( _parameters._gridPortalIpAddress, 22);
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

		String termination = _parameters._sgeoutDirectoryName + "/" + _uuid + "/" + Constant._terminationName + ".";
		List<String> finishedIdList = new ArrayList<String>();
		for ( String jobId:_idList) {
			try {
				if ( SshTool.file_exists( sftpClient, termination + jobId))
					finishedIdList.add( jobId);
			} catch (Throwable ex) {
				SshTool.close( connection);
				SshTool.close_and_catch( sftpClient);
				SshTool.disconnect( sshClient);
				_running = false;
				return;
			}
		}

		for ( String jobId:finishedIdList)
			_idList.remove( jobId);

		_progressBar.setValue( _number - _idList.size());
		_progressBar.update( _progressBar.getGraphics());

		if ( _idList.isEmpty()) {
			if ( _status.equals( "simulation")) {
				if ( _environment.get( Environment._logAnalysisKey, "false").equals( "true")) {
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
					if ( _environment.get( Environment._logTransferKey, "false").equals( "true")) {
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
						_statusTextField.setText( "Done");
						_statusTextField.update( _statusTextField.getGraphics());
					}
				}
			} else if ( _status.equals( "analysis")) {
				if ( _environment.get( Environment._logTransferKey, "false").equals( "true")) {
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
					_statusTextField.setText( "Done");
					_statusTextField.update( _statusTextField.getGraphics());
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
						_statusTextField.setText( "Done");
						_statusTextField.update( _statusTextField.getGraphics());
					}
				} catch (Throwable ex) {
					_status = "none";
					stop_timer();
					on_failed( "transfer( ... ) at execute_timer_task( ... )\n", "transfer( ... ) at execute_timer_task( ... )", connection);
				}
			} else {
				_status = "none";
				stop_timer();
				_statusTextField.setText( "Done");
				_statusTextField.update( _statusTextField.getGraphics());
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

		_statusTextField.setText( "Failed ...");
		_statusTextField.update( _statusTextField.getGraphics());
		_errorLogTextArea.append( log);
		_errorLogTextArea.update( _errorLogTextArea.getGraphics());
		_stopButton.setEnabled( false);
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

		_statusTextField.setText( "Failed ...");
		_statusTextField.update( _statusTextField.getGraphics());
		_errorLogTextArea.append( log);
		_errorLogTextArea.update( _errorLogTextArea.getGraphics());
		_stopButton.setEnabled( false);
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
		Connection connection = new Connection( _parameters._gridPortalIpAddress, 22);
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
		synchronized ( _jobIdList) {
			if ( _jobIdList.isEmpty())
				return;

			String command = _parameters._deleteShellScriptFilename;
			for ( String id:_jobIdList)
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

				_errorLogTextArea.append( result);
				System.out.println( result);

				_jobIdList.clear();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
