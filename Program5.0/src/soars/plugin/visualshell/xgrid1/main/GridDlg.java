/*
 * Created on 2006/06/13
 */
package soars.plugin.visualshell.xgrid1.main;

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
import soars.common.utility.tool.ssh2.SshTool2;

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
	private JButton _scriptDirectorySelectorButton = null;

	/**
	 * 
	 */
	private JTextField _logDirectoryTextField = null;

	/**
	 * 
	 */
	private JButton _logDirectorySelectorButton = null;

	/**
	 * 
	 */
	private CustomNumberSpinner _modelBuilderMemorySizeNumberSpinner = null;

	/**
	 * 
	 */
	private JCheckBox _simulationCheckBox = null;

	/**
	 * 
	 */
	private NumberSpinner _numberOfTimesNumberSpinner = null;

	/**
	 * 
	 */
	private JCheckBox _transferLogCheckBox = null;

	/**
	 * 
	 */
	private JTextField _localLogDirectoryTextField = null;

	/**
	 * 
	 */
	private JButton _localLogDirectorySelectorButton = null;

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
	private String _modelBuilderMemorySize = Constant._defaultMemorySize;

	/**
	 * @param name
	 * @param file
	 * @param frame
	 * @param hostname
	 * @param usersDirectory
	 * @param javaHome
	 * @param applicationDirectory
	 * @return
	 */
	public static boolean execute(String name, File file, JFrame frame, String hostname, String usersDirectory, String javaHome, String applicationDirectory) {
		List resultList = get_experimentManager( name, frame);
		if ( null == resultList || resultList.isEmpty()) {
			JOptionPane.showMessageDialog(
				frame,
				ResourceManager.get_instance().get( "plugin.xgrid1.error.message.no.script"),
				ResourceManager.get_instance().get( "plugin.xgrid1.dialog.title"),
				JOptionPane.ERROR_MESSAGE);
			return true;
		}

		Object experimentManager = resultList.get( 0);

		Parameters parameters = new Parameters();

		parameters._hostname = hostname;
		parameters._javaHome = javaHome;
		//parameters._directoryPrefix = ( Constant._baseDirectoryPrefix + hostname);
		parameters._directoryPrefix = Constant._baseDirectoryPrefix;
		parameters._applicationDirectoryName = ( parameters._directoryPrefix + applicationDirectory);
		parameters._executorJarFilename = ( parameters._applicationDirectoryName + Constant._baseExecutorJarFilename);
		parameters._submitShellScriptFilename = ( parameters._applicationDirectoryName + Constant._baseSubmitShellScriptFilename);
		parameters._deleteShellScriptFilename = ( parameters._applicationDirectoryName + Constant._baseDeleteShellScriptFilename);
		parameters._programDirectoryName = ( parameters._applicationDirectoryName + Constant._baseProgramDirectoryName);


		GridLoginDlg gridLoginDlg = new GridLoginDlg(
			frame,
			ResourceManager.get_instance().get( "plugin.xgrid1.login.dialog.title"),
			true,
			usersDirectory,
			parameters);
		if ( !gridLoginDlg.do_modal( frame))
			return true;

		Environment.get_instance().store();

//		patch( rootDirectory, parameters._username);

		GridDlg gridDlg = new GridDlg( frame,
			ResourceManager.get_instance().get( "plugin.xgrid1.dialog.title"),
			true,
			name,
			parameters,
			experimentManager);
		if ( !gridDlg.do_modal( frame))
			return true;

		Environment.get_instance().store();

		GridFrame gridFrame = new GridFrame(
			ResourceManager.get_instance().get( "plugin.xgrid1.frame.title")
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
				ResourceManager.get_instance().get( "plugin.xgrid1.frame.title"),
				JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return resultList;
	}

//	/**
//	 * @param rootDirectory 
//	 * @param username
//	 */
//	private static void patch(String rootDirectory, String username) {
//		if ( !Environment.get_instance().get( Environment._scriptDirectoryKey, "").startsWith( rootDirectory + "/" + username))
//			Environment.get_instance().set( Environment._scriptDirectoryKey, rootDirectory + "/" + username);
//
//		if ( !Environment.get_instance().get( Environment._logDirectoryKey, "").startsWith( rootDirectory + "/" + username))
//			Environment.get_instance().set( Environment._logDirectoryKey, rootDirectory + "/" + username);
//	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param name
	 * @param parameters
	 * @param experimentManager
	 * @throws HeadlessException
	 */
	public GridDlg(JFrame arg0, String arg1, boolean arg2, String name, Parameters parameters, Object experimentManager) throws HeadlessException {
		super(arg0, arg1, arg2);
		_frame = arg0;
		_name = name;
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

		setup_gridPortalIpAddressTextField();

		insert_horizontal_glue();

		setup_localUsernameTextField();

		insert_horizontal_glue();

		setup_scriptDirectoryTextField();

		insert_horizontal_glue();

		setup_logDirectoryTextField();

		insert_horizontal_glue();

		setup_modelBuilderMemorySizeNumberSpinner();

		insert_horizontal_glue();

		setup_simulationCheckBox();

		setup_numberOfTimesNumberSpinner();

		insert_horizontal_glue();

		setup_transferLogCheckBox();

		setup_localLogDirectoryTextField();

		insert_horizontal_glue();

		setup_errorLogTextArea();

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
	private void setup_gridPortalIpAddressTextField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.xgrid1.dialog.gird.portal.ip.address"), SwingConstants.RIGHT);
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
	private void setup_localUsernameTextField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.xgrid1.dialog.username"), SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);

		_usernameTextField = new JTextField( _parameters._username);
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

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.xgrid1.dialog.script.directory"), SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);


		String scriptDirectory = Environment.get_instance().get( Environment._scriptDirectoryKey, _parameters._dataDirectoryName);
		scriptDirectory = ( scriptDirectory.startsWith( _parameters._dataDirectoryName) ? scriptDirectory : _parameters._dataDirectoryName);
		_scriptDirectoryTextField = new JTextField( scriptDirectory);
		_scriptDirectoryTextField.setEditable( false);
		_scriptDirectoryTextField.setPreferredSize( new Dimension( 400,
			_scriptDirectoryTextField.getPreferredSize().height));
		panel.add( _scriptDirectoryTextField);


		_scriptDirectorySelectorButton = new JButton( ResourceManager.get_instance().get( "plugin.xgrid1.dialog.script.reference"));
		_scriptDirectorySelectorButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_scriptDirectorySelectorButton_actionPerformed( arg0);
			}
		});
		panel.add( _scriptDirectorySelectorButton);


		getContentPane().add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_scriptDirectorySelectorButton_actionPerformed(ActionEvent actionEvent) {
		String directory = Tool.get_remote_directory( _scriptDirectoryTextField.getText(),
			ResourceManager.get_instance().get( "plugin.xgrid1.script.directory.selector.dialog.title"),
			( Frame)getOwner(), this, _parameters._gridPortalIpAddress, _parameters._username, _parameters._privateKeyFilename,
			_parameters._dataDirectoryName);
		if ( null == directory)
			return;

		_scriptDirectoryTextField.setText( directory);
	}

	/**
	 * 
	 */
	private void setup_logDirectoryTextField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.xgrid1.dialog.log.directory"), SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);


		String logDirectory = Environment.get_instance().get( Environment._logDirectoryKey, _parameters._dataDirectoryName);
		logDirectory = ( logDirectory.startsWith( _parameters._dataDirectoryName) ? logDirectory : _parameters._dataDirectoryName);
		_logDirectoryTextField = new JTextField( logDirectory);
		_logDirectoryTextField.setEditable( false);
		_logDirectoryTextField.setPreferredSize( new Dimension( 400,
			_logDirectoryTextField.getPreferredSize().height));
		panel.add( _logDirectoryTextField);


		_logDirectorySelectorButton = new JButton( ResourceManager.get_instance().get( "plugin.xgrid1.dialog.log.reference"));
		_logDirectorySelectorButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_logDirectorySelectorButton_actionPerformed( arg0);
			}
		});
		panel.add( _logDirectorySelectorButton);


		getContentPane().add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_logDirectorySelectorButton_actionPerformed(ActionEvent actionEvent) {
		String directory = Tool.get_remote_directory( _logDirectoryTextField.getText(),
			ResourceManager.get_instance().get( "plugin.xgrid1.log.directory.selector.dialog.title"),
			( Frame)getOwner(), this, _parameters._gridPortalIpAddress, _parameters._username, _parameters._privateKeyFilename,
			_parameters._dataDirectoryName);
		if ( null == directory)
			return;

		_logDirectoryTextField.setText( directory);
	}

	/**
	 * 
	 */
	private void setup_modelBuilderMemorySizeNumberSpinner() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.xgrid1.dialog.model.builder.memory.size"), SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);

		_modelBuilderMemorySizeNumberSpinner = new CustomNumberSpinner( this);
		_modelBuilderMemorySizeNumberSpinner.set_minimum( 0);
		_modelBuilderMemorySizeNumberSpinner.set_maximum( 10000);
		_modelBuilderMemorySizeNumberSpinner.setPreferredSize( new Dimension( 200,
			_modelBuilderMemorySizeNumberSpinner.getPreferredSize().height));
		_modelBuilderMemorySize = Environment.get_instance().get(
			Environment._modelBuilderMemorySizeKey, Constant._defaultMemorySize);
		_modelBuilderMemorySizeNumberSpinner.set_value( new Integer( _modelBuilderMemorySize).intValue());
		panel.add( _modelBuilderMemorySizeNumberSpinner);

		label = new JLabel( "MB");
		panel.add( label);


		getContentPane().add( panel);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.spinner.INumberSpinnerHandler#changed(java.lang.String, soars.common.utility.swing.spinner.NumberSpinner)
	 */
	public void changed(String number, NumberSpinner numberSpinner) {
		number = ( number.equals( "") ? "0" : number);
		if ( numberSpinner.equals( _modelBuilderMemorySizeNumberSpinner))
			_modelBuilderMemorySize = number;
	}

	/**
	 * 
	 */
	private void setup_simulationCheckBox() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_simulationCheckBox = new JCheckBox(
			ResourceManager.get_instance().get( "plugin.xgrid1.dialog.simulation"),
			Environment.get_instance().get( Environment._simulationKey, "true").equals( "true"));
		_simulationCheckBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_labels.get( 2).setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_scriptDirectoryTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_scriptDirectorySelectorButton.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_labels.get( 5).setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_numberOfTimesNumberSpinner.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _simulationCheckBox);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_numberOfTimesNumberSpinner() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.xgrid1.dialog.number.of.times"), SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);


		_numberOfTimesNumberSpinner = new NumberSpinner();
		_numberOfTimesNumberSpinner.set_minimum( 1);
		_numberOfTimesNumberSpinner.set_maximum( Constant._maxNumberOfTimes);
		Integer number_of_times = new Integer(
			Environment.get_instance().get( Environment._numberOfTimesForGridKey, "1"));
		_numberOfTimesNumberSpinner.set_value( number_of_times.intValue());
		_numberOfTimesNumberSpinner.setPreferredSize( new Dimension( 200,
			_numberOfTimesNumberSpinner.getPreferredSize().height));
		panel.add( _numberOfTimesNumberSpinner);


		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_transferLogCheckBox() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_transferLogCheckBox = new JCheckBox(
			ResourceManager.get_instance().get( "plugin.xgrid1.dialog.log.transfer"),
			Environment.get_instance().get( Environment._logTransferKey, "false").equals( "true"));
		_transferLogCheckBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_labels.get( 6).setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_localLogDirectoryTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				_localLogDirectorySelectorButton.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _transferLogCheckBox);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_localLogDirectoryTextField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.xgrid1.dialog.local.log.directory"), SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);


		_localLogDirectoryTextField = new JTextField(
			Environment.get_instance().get( Environment._localLogDirectoryKey, ""));
		_localLogDirectoryTextField.setEditable( false);
		_localLogDirectoryTextField.setPreferredSize( new Dimension( 400,
			_localLogDirectoryTextField.getPreferredSize().height));
		panel.add( _localLogDirectoryTextField);


		_localLogDirectorySelectorButton = new JButton( ResourceManager.get_instance().get( "plugin.xgrid1.dialog.local.log.reference"));
		_localLogDirectorySelectorButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_localLogDirectorySelectorButton_actionPerformed( arg0);
			}
		});
		panel.add( _localLogDirectorySelectorButton);


		getContentPane().add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_localLogDirectorySelectorButton_actionPerformed(ActionEvent actionEvent) {
		File directory = Tool.get_directory( _localLogDirectoryTextField.getText(),
			ResourceManager.get_instance().get( "plugin.xgrid1.local.log.directory.selector.dialog.title"), this);
		if ( null == directory)
			return;

		_localLogDirectoryTextField.setText( directory.getAbsolutePath());
	}

	/**
	 * 
	 */
	private void setup_errorLogTextArea() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.xgrid1.dialog.error.log"));
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
	private void adjust() {
		int width = 0;
		for ( JLabel label:_labels)
			width = Math.max( width, label.getPreferredSize().width);

		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));

		_labels.get( 2).setEnabled( _simulationCheckBox.isSelected());
		_scriptDirectoryTextField.setEnabled( _simulationCheckBox.isSelected());
		_scriptDirectorySelectorButton.setEnabled( _simulationCheckBox.isSelected());
		_labels.get( 5).setEnabled( _simulationCheckBox.isSelected());
		_numberOfTimesNumberSpinner.setEnabled( _simulationCheckBox.isSelected());

		_labels.get( 6).setEnabled( _transferLogCheckBox.isSelected());
		_localLogDirectoryTextField.setEnabled( _transferLogCheckBox.isSelected());
		_localLogDirectorySelectorButton.setEnabled( _transferLogCheckBox.isSelected());

		_errorLogTextAreaScrollPane.setPreferredSize( new Dimension(
			width + 410 + _localLogDirectorySelectorButton.getPreferredSize().width, 100));
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		if ( null != _experimentManager
			&& _simulationCheckBox.isSelected()
			&& !edit_experimentManager())
			return;


		String scriptDirectory = _scriptDirectoryTextField.getText();
		if ( null == scriptDirectory || scriptDirectory.equals( ""))
			return;

		Environment.get_instance().set( Environment._scriptDirectoryKey, scriptDirectory);


		String logDirectory = _logDirectoryTextField.getText();
		if ( null == logDirectory || logDirectory.equals( ""))
			return;

		Environment.get_instance().set( Environment._logDirectoryKey, logDirectory);


		Environment.get_instance().set(
			Environment._modelBuilderMemorySizeKey, _modelBuilderMemorySize);


		Environment.get_instance().set( Environment._simulationKey,
			_simulationCheckBox.isSelected() ? "true" : "false");

		int numberOfTimes = _numberOfTimesNumberSpinner.get_value();
		Environment.get_instance().set(
			Environment._numberOfTimesForGridKey, String.valueOf( numberOfTimes));


		String localLogDirectory = _localLogDirectoryTextField.getText();
		if ( _transferLogCheckBox.isSelected()
			&& ( null == localLogDirectory || localLogDirectory.equals( "")))
			return;

		//localLogDirectory = localLogDirectory.replaceAll( "\\\\", "/");

		Environment.get_instance().set( Environment._logTransferKey,
			_transferLogCheckBox.isSelected() ? "true" : "false");
		Environment.get_instance().set( Environment._localLogDirectoryKey, localLogDirectory);


		if ( Environment.get_instance().get( Environment._simulationKey, "false").equals( "false")
			//&& Environment.get_instance().get( Environment._logAnalysisKey, "false").equals( "false")
			&& Environment.get_instance().get( Environment._logTransferKey, "false").equals( "false"))
			return;

		setEnabled( false);
		if ( !clear( scriptDirectory, logDirectory, _transferLogCheckBox.isSelected() ? localLogDirectory : null)) {
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
			new Object[] { _experimentManager, ResourceManager.get_instance().get( "plugin.xgrid1.dialog.title"), this},
			resultList)) {
			JOptionPane.showMessageDialog( _frame,
				"Plugin error : Reflection.execute_class_method( ... )" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator
				+ " Class name : soars.application.visualshell.main.MainFrame" + Constant._lineSeparator
				+ " Method name : edit_experimentManager" + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.xgrid1.frame.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( resultList.isEmpty() || null == resultList.get( 0) || !Boolean.class.isInstance( resultList.get( 0)))
			return false;

		return ( ( Boolean)resultList.get( 0)).booleanValue();
	}

	/**
	 * @param scriptDirectory
	 * @param logDirectory
	 * @param localLogDirectory
	 * @return
	 */
	private boolean clear(String scriptDirectory, String logDirectory, String localLogDirectory) {
		if ( Environment.get_instance().get( Environment._simulationKey, "false").equals( "true")) {
			SshClient sshClient = SshTool2.getSshClient( _parameters._gridPortalIpAddress, _parameters._username, new File( _parameters._privateKeyFilename));
			if ( null == sshClient)
				return false;

			SftpClient sftpClient = SshTool2.getSftpClient( sshClient);
			if ( null == sftpClient) {
				sshClient.disconnect();
				return false;
			}

			if ( !SshTool2.delete( sftpClient, scriptDirectory, false)) {
				SshTool2.close( sftpClient);
				sshClient.disconnect();
				_errorLogTextArea.append( "Could not clear! : " + scriptDirectory + "\n");
				return false;
			}

			if ( !SshTool2.delete( sftpClient, logDirectory, false)) {
				SshTool2.close( sftpClient);
				sshClient.disconnect();
				_errorLogTextArea.append( "Could not clear! : " + logDirectory + "\n");
				return false;
			}

			SshTool2.close( sftpClient);
			sshClient.disconnect();
		}

		if ( Environment.get_instance().get( Environment._logTransferKey, "false").equals( "true")
			&& null != localLogDirectory) {
			if ( !FileUtility.delete( new File( localLogDirectory), false)) {
				_errorLogTextArea.append( "Could not clear! : " + localLogDirectory + "\n");
				return false;
			}
		}

		return true;
	}
}
