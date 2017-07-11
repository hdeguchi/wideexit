/**
 * 
 */
package soars.plugin.visualshell.ga1.main;

import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import soars.common.utility.swing.border.ComponentTitledBorder;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.tool.reflection.Reflection;

/**
 * @author kurata
 *
 */
public class GALocalMonitorDlg extends GAMonitorDlg {

	/**
	 * Synchronized object.
	 */
	private Object _lock_process = new Object();

	/**
	 * 
	 */
	private List<Process> _process = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param name
	 * @param localParameters
	 * @param algorithmNameMap
	 * @param algorithmTypeMap
	 * @param originalExperimentManager
	 * @param environment
	 * @throws HeadlessException
	 */
	public GALocalMonitorDlg(Frame arg0, String arg1, boolean arg2, String name, LocalParameters localParameters,
		Map<String, String> algorithmNameMap, Map<String, String> algorithmTypeMap, Object originalExperimentManager, Environment environment) throws HeadlessException {
		super(arg0, arg1, arg2, name, localParameters, algorithmNameMap, algorithmTypeMap, originalExperimentManager, environment);
	}

	/* (non-Javadoc)
	 * @see soars.plugin.visualshell.ga1.main.GAMonitorDlg#get_rectangle_from_environment_file()
	 */
	protected Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get( Environment._ga_local_frame_rectangle_key + "x",
			String.valueOf( SwingTool.get_default_window_position( _parent, _minimum_width, _minimum_height).x));
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get( Environment._ga_local_frame_rectangle_key + "y",
			String.valueOf( SwingTool.get_default_window_position( _parent, _minimum_width, _minimum_height).y));
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get( Environment._ga_local_frame_rectangle_key + "width",
			String.valueOf( _minimum_width));
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get( Environment._ga_local_frame_rectangle_key + "height",
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
			Environment._ga_local_frame_rectangle_key + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._ga_local_frame_rectangle_key + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._ga_local_frame_rectangle_key + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._ga_local_frame_rectangle_key + "height", String.valueOf( rectangle.height));

		super.set_property_to_environment_file();
	}

	/* (non-Javadoc)
	 * @see soars.plugin.visualshell.ga1.main.GAMonitorDlg#on_cancel(java.awt.event.ActionEvent)
	 */
	protected void on_cancel(ActionEvent actionEvent) {
		if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog( _parent,
			ResourceManager.get_instance().get( "plugin.ga1.frame.exit.confirm.message"), getTitle(), JOptionPane.YES_NO_OPTION))
			return;

		_termination = true;
		on_stop( null);
		super.on_cancel(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.plugin.visualshell.ga1.main.GAMonitorDlg#setup_local_panel(javax.swing.JPanel)
	 */
	protected void setup_local_panel(JPanel parent) {
		JPanel localPanel = new JPanel();
		localPanel.setLayout( new BoxLayout( localPanel, BoxLayout.X_AXIS));

		localPanel.add( Box.createHorizontalStrut( 10));

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		setup_local_components( panel);

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.local.frame"));
		panel.setBorder( new ComponentTitledBorder( label, panel, BorderFactory.createLineBorder( getForeground())));

		localPanel.add( panel);

		localPanel.add( Box.createHorizontalStrut( 5));

		parent.add( localPanel);
	}

	/**
	 * @param parent
	 */
	private void setup_local_components(JPanel parent) {
		setup_number_of_scripts_textField( parent);

		insert_horizontal_glue( parent);

		setup_status_textField( parent);

		insert_horizontal_glue( parent);

		setup_progressBar( parent);

		insert_horizontal_glue( parent);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		while ( true) {
			if ( !setup()) {
				on_failed( "setup() at run()\n", "setup() at run()");
				break;
			}

			if ( _stop) {
				on_stop();
				break;
			}

			File local_log_directory = get_local_log_directory();
			if ( null == local_log_directory){
				on_failed( "get_local_log_directory() at run()\n", "get_local_log_directory() at run()");
				break;
			}

			if ( _stop) {
				on_stop();
				break;
			}

			List scriptFiles = export_scripts( local_log_directory);
			if ( null == scriptFiles) {
				on_failed( "export_scripts() at run()\n", "export_scripts() at run()");
				break;
			}

			if ( _stop) {
				on_stop();
				break;
			}

			if ( !simulate( scriptFiles, local_log_directory)) {
				on_failed( "simulate() at run()\n", "simulate() at run()");
				break;
			}

			if ( _stop) {
				on_stop();
				break;
			}

			if ( _generation_alternations < Integer.parseInt( _environment.get( Environment._number_of_generation_alternations_key, "10")) - 1) {
				List<Double> evaluation_values = prepare_to_restart( local_log_directory);
				if ( null == evaluation_values) {
					on_failed( "prepare_to_restart() at run()\n", "prepare_to_restart() at run()");
					break;
				}

				// TODO デバッグ用
				//System.out.println( evaluation_values.toString());

				_ga_progressBar.setValue( ++_generation_alternations);
				_ga_progressBar.update( _ga_progressBar.getGraphics());

				if ( _stop) {
					on_stop();
					break;
				}

			} else {
				List<Double> evaluation_values = evaluate( local_log_directory);
				if ( null == evaluation_values) {
					on_failed( "evaluate() at run()\n", "evaluate() at run()");
					break;
				}

				// TODO デバッグ用
				//System.out.println( evaluation_values.toString());

				// 正常終了時の処理を行う
				if ( !output( local_log_directory)) {
					on_failed( "output() at run()\n", "output() at run()");
					break;
				}

				_ga_progressBar.setValue( ++_generation_alternations);
				_ga_progressBar.update( _ga_progressBar.getGraphics());
		
				_stop_button.setEnabled( false);
				setEnabled( true);

				_status_textField.setText( "Done");
				_status_textField.update( _status_textField.getGraphics());

				break;
			}
		}
	}

	/**
	 * @param local_log_directory
	 * @return
	 */
	private List export_scripts(File local_log_directory) {
		_status_textField.setText( "Simulation : Exporting ...");
		_status_textField.update( _status_textField.getGraphics());

		_number_of_scripts_textField.setText( "0");
		_number_of_scripts_textField.update( _number_of_scripts_textField.getGraphics());

		List resultList = new ArrayList();
		if ( !Reflection.execute_class_method( _experimentManager, "export",
			new Class[] { File.class, JTextField.class, JProgressBar.class}, new Object[] { local_log_directory, _number_of_scripts_textField, _progressBar}, resultList)) {
			JOptionPane.showMessageDialog( _parent,
				"Plugin error : Reflection.execute_class_method( ... )" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator
					+ " Class name : soars.application.visualshell.object.experiment.ExperimentManager" + Constant._lineSeparator
					+ " Method name : export" + Constant._lineSeparator,
				getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return null;
		}

		if ( resultList.isEmpty() || null == resultList.get( 0) || !List.class.isInstance( resultList.get( 0))) {
			JOptionPane.showMessageDialog( _parent,
				"Plugin error : Reflection.execute_class_method( ... )" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator
					+ " Class name : soars.application.visualshell.object.experiment.ExperimentManager" + Constant._lineSeparator
					+ " Method name : extract_for_genetic_algorithm" + Constant._lineSeparator,
				getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return null;
		}

		List scriptFiles = ( List)resultList.get( 0);

		return scriptFiles;
	}

	/**
	 * @param scriptFiles
	 * @param local_log_directory
	 * @return
	 */
	private boolean simulate(List scriptFiles, File local_log_directory) {
		_status_textField.setText( "Simulation : Running ...");
		_status_textField.update( _status_textField.getGraphics());

		Class cls = null;
		try {
			cls = Class.forName( "soars.application.visualshell.common.file.ScriptFile");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		if ( null == cls)
			return false;

		_progressBar.setMinimum( 0);
		_progressBar.setMaximum( 1);
		_progressBar.setValue( 0);
		_progressBar.update( _progressBar.getGraphics());

		_log_textArea.setText( "");

		List resultList = new ArrayList();
		synchronized( _lock_process) {
			_process = new ArrayList<Process>();
		}

		if ( !Reflection.execute_static_method( "soars.application.visualshell.executor.modelbuilder.ModelBuilder",
			"run_on_genetic_algorithm", new Class[] { List.class, JTextArea.class, List.class, Object.class}, new Object[] { scriptFiles, _log_textArea, _process, _lock_process}, resultList)) {
			JOptionPane.showMessageDialog( _parent,
				"Plugin error : Reflection.execute_static_method( ... )" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator
					+ " Class name : soars.application.visualshell.executor.modelbuilder.ModelBuilder" + Constant._lineSeparator
					+ " Method name : run_on_genetic_algorithm" + Constant._lineSeparator,
				getTitle(),
				JOptionPane.ERROR_MESSAGE);
			cleanup( cls, scriptFiles);
			return false;
		}

		synchronized( _lock_process) {
			_process = null;
		}

		if ( resultList.isEmpty() || null == resultList.get( 0) || !Boolean.class.isInstance( resultList.get( 0))) {
			JOptionPane.showMessageDialog( _parent,
				"Plugin error : Reflection.execute_static_method( ... )" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator
					+ " Class name : soars.application.visualshell.executor.modelbuilder.ModelBuilder" + Constant._lineSeparator
					+ " Method name : run_on_genetic_algorithm" + Constant._lineSeparator,
				getTitle(),
				JOptionPane.ERROR_MESSAGE);
			cleanup( cls, scriptFiles);
			return false;
		}

		_progressBar.setValue( 1);
		_progressBar.update( _progressBar.getGraphics());

//		wait_for( local_log_directory, scriptFiles);

		cleanup( cls, scriptFiles);

		return true;

//		_progressBar.setMinimum( 0);
//		_progressBar.setMaximum( scriptFiles.size());
//		_progressBar.setValue( 0);
//		_progressBar.update( _progressBar.getGraphics());
//
//		for ( int i = 0; i < scriptFiles.size(); ++i) {
//			_log_textArea.setText( "");
//
//			List resultList = new ArrayList();
//			synchronized( _lock_process) {
//				_process = new ArrayList<Process>();
//			}
//			if ( !Reflection.execute_static_method( "soars.application.visualshell.executor.modelbuilder.ModelBuilder",
//				"run_on_genetic_algorithm", new Class[] { cls, JTextArea.class, List.class, Object.class}, new Object[] { scriptFiles.get( i), _log_textArea, _process, _lock_process}, resultList)) {
//				JOptionPane.showMessageDialog( _parent,
//					"Plugin error : Reflection.execute_static_method( ... )" + Constant._line_separator
//						+ " Plugin name : " + _name + Constant._line_separator
//						+ " Class name : soars.application.visualshell.executor.modelbuilder.ModelBuilder" + Constant._line_separator
//						+ " Method name : run_on_genetic_algorithm" + Constant._line_separator,
//					getTitle(),
//					JOptionPane.ERROR_MESSAGE);
//				cleanup( cls, scriptFiles);
//				return false;
//			}
//
//			synchronized( _lock_process) {
//				_process = null;
//			}
//
//			if ( resultList.isEmpty() || null == resultList.get( 0) || !Boolean.class.isInstance( resultList.get( 0))) {
//				JOptionPane.showMessageDialog( _parent,
//					"Plugin error : Reflection.execute_static_method( ... )" + Constant._line_separator
//						+ " Plugin name : " + _name + Constant._line_separator
//						+ " Class name : soars.application.visualshell.executor.modelbuilder.ModelBuilder" + Constant._line_separator
//						+ " Method name : run_on_genetic_algorithm" + Constant._line_separator,
//					getTitle(),
//					JOptionPane.ERROR_MESSAGE);
//				cleanup( cls, scriptFiles);
//				return false;
//			}
//
//			_progressBar.setValue( i + 1);
//			_progressBar.update( _progressBar.getGraphics());
//
//			if ( _stop)
//				break;
//		}
//
//		cleanup( cls, scriptFiles);
//
//		return true;
	}

//	/**
//	 * @param local_log_directory
//	 * @param scriptFiles
//	 * 
//	 */
//	private void wait_for(File local_log_directory, List scriptFiles) {
//		// TODO Auto-generated method stub
//		int index = 0;
//		while ( scriptFiles.size() > index) {
//			if ( _stop) {
//				on_stop();
//				break;
//			}
//
//			File file = new File( local_log_directory,
//				"name" + String.valueOf( index + 1) +"/1/" + ( _localParameters._spot ? "spots/" : "agents/") + _localParameters._number_variable + ".log");
//			if ( !file.exists() || !file.canRead())
//				continue;
//
//			++index;
//		}
//	}

	/**
	 * @param cls
	 * @param scriptFiles
	 * @return
	 */
	private boolean cleanup(Class cls, List scriptFiles) {
		for ( int i = 0; i < scriptFiles.size(); ++i) {
			List resultList = new ArrayList();
			Reflection.execute_class_method( scriptFiles.get( i), "cleanup", new Class[ 0], new Object[ 0], resultList);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.plugin.visualshell.ga1.main.GAMonitorDlg#on_stop(java.awt.event.ActionEvent)
	 */
	protected void on_stop(ActionEvent actionEvent) {
		if ( !_termination
			&& JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog( _parent,
				ResourceManager.get_instance().get( "plugin.ga1.frame.cancel.confirm.message"), getTitle(), JOptionPane.YES_NO_OPTION))
			return;

		synchronized( _lock_process) {
			if ( null != _process && !_process.isEmpty()) {
				_process.get( 0).destroy();
			}
		}
		super.on_stop(actionEvent);
	}

	/**
	 * @param log
	 * @param message 
	 */
	private void on_failed(String log, String message) {
		_status_textField.setText( "Failed ...");
		_status_textField.update( _status_textField.getGraphics());
		_log_textArea.append( log);
		_log_textArea.update( _log_textArea.getGraphics());
		_stop_button.setEnabled( false);
		setEnabled( true);
		JOptionPane.showMessageDialog( _parent, message, getTitle(), JOptionPane.ERROR_MESSAGE);
	}
}
