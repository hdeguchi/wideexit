/*
 * Created on 2006/06/13
 */
package soars.plugin.visualshell.ga1.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import soars.common.utility.swing.border.ComponentTitledBorder;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;
import soars.common.utility.swing.spinner.CustomNumberSpinner;
import soars.common.utility.swing.spinner.INumberSpinnerHandler;
import soars.common.utility.swing.spinner.NumberSpinner;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Dialog;
import soars.common.utility.tool.reflection.Reflection;

/**
 * @author kurata
 */
public class GADlg extends Dialog implements INumberSpinnerHandler {

	/**
	 * 
	 */
	private int _minimum_width = 800;

	/**
	 * 
	 */
	private int _minimum_height = 480;

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
	private List<JComponent> _components = new ArrayList<JComponent>();

	/**
	 * 
	 */
	private JTextField _population_size_textField = null;

	/**
	 * 
	 */
	private CustomNumberSpinner _number_of_crossovers_numberSpinner = null;

	/**
	 * 
	 */
	private CustomNumberSpinner _number_of_generation_alternations_numberSpinner = null;

	/**
	 * 
	 */
	private JTextField _length_textField = null;

	/**
	 * 
	 */
	private JComboBox _algorithm_comboBox = null;

	/**
	 * 
	 */
	private JCheckBox _minimization_checkBox = null;

	/**
	 * 
	 */
	private JCheckBox _roulette_selection_checkBox = null;

	/**
	 * 
	 */
	private JCheckBox _spot_checkBox = null;

	/**
	 * 
	 */
	private JComboBox _number_variable_comboBox = null;

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
	private JCheckBox _grid_checkBox = null;

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
	private JLabel _model_builder_memory_size_label = null;

	/**
	 * 
	 */
	private ComboBox _model_builder_memory_size_comboBox = null;

	/**
	 * 
	 */
	private JCheckBox _advanced_memory_setting_checkBox = null;

	/**
	 * 
	 */
	private CustomNumberSpinner _model_builder_memory_size_numberSpinner = null;

	/**
	 * 
	 */
	private List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * 
	 */
	private JTextArea _log_textArea = null;

	/**
	 * 
	 */
	private String _root_directory = "";

	/**
	 * 
	 */
	private LocalParameters _localParameters = new LocalParameters();

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
	 * 
	 */
	private Map<String, String> _algorithm_name_map = null;

	/**
	 * 
	 */
	private Map<String, String> _algorithm_type_map = null;

	/**
	 * @param name
	 * @param file
	 * @param frame
	 * @param username
	 * @param password
	 * @param root_directory
	 * @param java_home
	 * @param home_directory
	 * @param sgeout_directory
	 * @param program_directory
	 * @return
	 */
	public static boolean execute(String name, File file, JFrame frame, String username, String password, String root_directory, String java_home, String home_directory, String sgeout_directory, String program_directory) {
		List resultList = get_experimentManager( name, frame);
		if ( null == resultList || resultList.isEmpty() || null == resultList.get( 0)) {
			JOptionPane.showMessageDialog(
				frame,
				ResourceManager.get_instance().get( "plugin.ga1.error.message.no.script"),
				ResourceManager.get_instance().get( "plugin.ga1.dialog.title"),
				JOptionPane.ERROR_MESSAGE);
			return true;
		}

		Object experimentManager = resultList.get( 0);

		List<String> algorithm = new ArrayList<String>();
		if ( !edit_experimentManager( name, frame, experimentManager, algorithm))
			return true;

		if ( algorithm.isEmpty())
			return true;

		LocalParameters localParameters = new LocalParameters();
		localParameters._algorithm = algorithm.get( 0);

		localParameters._population_size = get_population_size( name, frame, experimentManager);
		if ( 3 > localParameters._population_size)
			return true;

		localParameters._length = get_length( name, frame, experimentManager);
		if ( 0 >= localParameters._length)
			return true;


		Map<String, String> algorithm_name_map = new HashMap<String, String>();
		algorithm_name_map.put( "UxMgg", ResourceManager.get_instance().get( "plugin.ga1.uniform.crossover"));
		algorithm_name_map.put( "UndxMgg", ResourceManager.get_instance().get( "plugin.ga1.unimodal.normal.distribution.crossover"));

		Map<String, String> algorithm_type_map = new HashMap<String, String>();
		algorithm_type_map.put( ResourceManager.get_instance().get( "plugin.ga1.uniform.crossover"), "UxMgg");
		algorithm_type_map.put( ResourceManager.get_instance().get( "plugin.ga1.unimodal.normal.distribution.crossover"), "UndxMgg");


		Parameters parameters = new Parameters();
		parameters._java_home = java_home;
		parameters._home_directory_name = home_directory;
		parameters._executor_jar_filename = ( home_directory + Constant._base_executor_jar_filename);
		parameters._submit_shell_script_file_name = ( home_directory + Constant._base_submit_shell_script_file_name);
		parameters._delete_shell_script_file_name = ( home_directory + Constant._base_delete_shell_script_file_name);
		parameters._sgeout_directory_name = sgeout_directory;
		parameters._program_directory_name = program_directory;

		parameters._username = username;
		parameters._password = password;

		parameters._grid_portal_ip_address = Environment.get_instance().get( Environment._grid_portal_ip_address_key, "");
		parameters._local_username = Environment.get_instance().get( Environment._local_username_key, "");


		GADlg gaDlg = new GADlg(
			frame,
			ResourceManager.get_instance().get( "plugin.ga1.dialog.title"),
			true,
			name,
			root_directory,
			localParameters,
			algorithm_name_map,
			algorithm_type_map,
			parameters,
			experimentManager);
		if ( !gaDlg.do_modal())
			return true;

		Environment.get_instance().store();


		GAMonitorDlg gaMonitorDlg = Environment.get_instance().get( Environment._grid_key).equals( "true")
			? new GAGridMonitorDlg(
					frame,
					ResourceManager.get_instance().get( "plugin.ga1.grid.frame.title")
						+ ( ( null == file) ? "" : ( " - " + file.getName())),
					true,
					name,
					localParameters,
					algorithm_name_map,
					algorithm_type_map,
					parameters,
					experimentManager,
					Environment.get_instance().get_clone())
			: new GALocalMonitorDlg(
					frame,
					ResourceManager.get_instance().get( "plugin.ga1.local.frame.title")
						+ ( ( null == file) ? "" : ( " - " + file.getName())),
					true,
					name,
					localParameters,
					algorithm_name_map,
					algorithm_type_map,
					experimentManager,
					Environment.get_instance().get_clone());
		gaMonitorDlg.do_modal();

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
				ResourceManager.get_instance().get( "plugin.ga1.dialog.title"),
				JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return resultList;
	}

	/**
	 * @param name
	 * @param frame
	 * @param experimentManager
	 * @param algorithm
	 * @return
	 */
	private static boolean edit_experimentManager(String name, JFrame frame, Object experimentManager, List<String> algorithm) {
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
		if ( !Reflection.execute_class_method( frame, "edit_experimentManager_for_genetic_algorithm",
			new Class[] { cls, String.class, Component.class, List.class},
			new Object[] { experimentManager, ResourceManager.get_instance().get( "plugin.ga1.dialog.title"), frame, algorithm},
			resultList)) {
			JOptionPane.showMessageDialog( frame,
				"Plugin error : Reflection.execute_class_method( ... )" + Constant._lineSeparator
				+ " Plugin name : " + name + Constant._lineSeparator
				+ " Class name : soars.application.visualshell.main.MainFrame" + Constant._lineSeparator
				+ " Method name : edit_experimentManager_for_genetic_algorithm" + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.ga1.dialog.title"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( resultList.isEmpty() || null == resultList.get( 0) || !Boolean.class.isInstance( resultList.get( 0)))
			return false;

		return ( ( Boolean)resultList.get( 0)).booleanValue();
	}

	/**
	 * @param name
	 * @param frame
	 * @param experimentManager
	 * @return
	 */
	private static int get_population_size(String name, JFrame frame, Object experimentManager) {
		List resultList = new ArrayList();
		if ( !Reflection.execute_class_method( experimentManager, "get_export_count", new Class[ 0], new Object[ 0], resultList)) {
			JOptionPane.showMessageDialog( frame,
				"Plugin error : Reflection.execute_class_method( ... )" + Constant._lineSeparator
					+ " Plugin name : " + name + Constant._lineSeparator
					+ " Class name : soars.application.visualshell.object.experiment.ExperimentManager" + Constant._lineSeparator
					+ " Method name : get_export_count" + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.ga1.dialog.title"),
				JOptionPane.ERROR_MESSAGE);
			return -1;
		}

		if ( resultList.isEmpty() || null == resultList.get( 0) || !Integer.class.isInstance( resultList.get( 0)))
			return -1;

		return ( ( Integer)resultList.get( 0)).intValue();
	}

	/**
	 * @param name
	 * @param frame
	 * @param experimentManager
	 * @return
	 */
	private static int get_length(String name, JFrame frame, Object experimentManager) {
		List resultList = new ArrayList();
		if ( !Reflection.execute_class_method( experimentManager, "get_initial_value_count_for_genetic_algorithm", new Class[ 0], new Object[ 0], resultList)) {
			JOptionPane.showMessageDialog( frame,
				"Plugin error : Reflection.execute_class_method( ... )" + Constant._lineSeparator
					+ " Plugin name : " + name + Constant._lineSeparator
					+ " Class name : soars.application.visualshell.object.experiment.ExperimentManager" + Constant._lineSeparator
					+ " Method name : get_initial_value_count_for_genetic_algorithm" + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.ga1.dialog.title"),
				JOptionPane.ERROR_MESSAGE);
			return -1;
		}

		if ( resultList.isEmpty() || null == resultList.get( 0) || !Integer.class.isInstance( resultList.get( 0)))
			return -1;

		return ( ( Integer)resultList.get( 0)).intValue();
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param name
	 * @param root_directory
	 * @param localParameters
	 * @param algorithm_name_map
	 * @param algorithm_type_map
	 * @param parameters
	 * @param experimentManager
	 * @throws HeadlessException
	 */
	public GADlg(JFrame arg0, String arg1, boolean arg2, String name, String root_directory, LocalParameters localParameters, Map<String, String> algorithm_name_map, Map<String, String> algorithm_type_map, Parameters parameters, Object experimentManager) throws HeadlessException {
		super(arg0, arg1, arg2);
		_frame = arg0;
		_name = name;
		_root_directory = root_directory;
		_localParameters = localParameters;
		_algorithm_name_map = algorithm_name_map;
		_algorithm_type_map = algorithm_type_map;
		_parameters = parameters;
		_experimentManager = experimentManager;
	}

	/**
	 * @return
	 */
	private Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get( Environment._ga_dialog_rectangle_key + "x",
			String.valueOf( SwingTool.get_default_window_position( _frame, _minimum_width, _minimum_height).x));
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get( Environment._ga_dialog_rectangle_key + "y",
			String.valueOf( SwingTool.get_default_window_position( _frame, _minimum_width, _minimum_height).y));
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get( Environment._ga_dialog_rectangle_key + "width",
			String.valueOf( _minimum_width));
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get( Environment._ga_dialog_rectangle_key + "height",
			String.valueOf( _minimum_height));
		if ( null == value)
			return null;

		int height = Integer.parseInt( value);

		return new Rectangle( x, y, width, height);
	}

	/**
	 * 
	 */
	private void optimize_window_rectangle() {
		Rectangle rectangle = getBounds();
		if ( !GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersects( rectangle)
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( rectangle).width <= 10
			|| rectangle.y <= -getInsets().top
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( rectangle).height <= getInsets().top) {
			setSize( _minimum_width, _minimum_height);
			setLocationRelativeTo( getOwner());
		}
	}

	/**
	 * 
	 */
	protected void set_property_to_environment_file() {
		Rectangle rectangle = getBounds();

		Environment.get_instance().set(
			Environment._ga_dialog_rectangle_key + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._ga_dialog_rectangle_key + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._ga_dialog_rectangle_key + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._ga_dialog_rectangle_key + "height", String.valueOf( rectangle.height));
	}

	/**
	 * @return
	 */
	private boolean do_modal() {
		Rectangle rectangle = get_rectangle_from_environment_file();
		if ( null == rectangle)
			return do_modal( getOwner());
		else
			return do_modal( rectangle);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		setLayout( new BorderLayout());


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new GridLayout( 1, 2));

		setup_center_panel( centerPanel);

		add( centerPanel);


		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		setup_south_panel( southPanel);

		add( southPanel, "South");


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);


		adjust();


		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_center_panel(JPanel parent) {
		setup_left_panel( parent);
		setup_right_panel( parent);
	}

	/**
	 * @param parent
	 */
	private void setup_left_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));
		setup_left_north_panel( northPanel);
		panel.add( northPanel, "North");


		//panel.setPreferredSize( new Dimension( 450, panel.getPreferredSize().height));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_left_north_panel(JPanel parent) {
		insert_horizontal_glue( parent);

		setup_basic_panel( parent);

		setup_grid_panel( parent);
	}

	/**
	 * @param parent
	 */
	private void setup_basic_panel(JPanel parent) {
		JPanel basicPanel = new JPanel();
		basicPanel.setLayout( new BoxLayout( basicPanel, BoxLayout.X_AXIS));

		basicPanel.add( Box.createHorizontalStrut( 10));

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		setup_basic_components( panel);

		panel.setBorder( BorderFactory.createLineBorder( getForeground(), 1));

		basicPanel.add( panel);

		basicPanel.add( Box.createHorizontalStrut( 5));

		parent.add( basicPanel);
	}

	/**
	 * @param parent
	 */
	private void setup_basic_components(JPanel parent) {
		insert_horizontal_glue( parent);

		setup_population_size_textField( parent);

		insert_horizontal_glue( parent);

		setup_number_of_crossovers_numberSpinner( parent);

		insert_horizontal_glue( parent);

		setup_number_of_generation_alternations_numberSpinner( parent);

		insert_horizontal_glue( parent);

		setup_length_textField( parent);

		insert_horizontal_glue( parent);

		setup_algorithm_comboBox( parent);

		insert_horizontal_glue( parent);

		setup_minimization_checkBox( parent);

		insert_horizontal_glue( parent);

		setup_roulette_selection_checkBox( parent);

		insert_horizontal_glue( parent);

		setup_number_variable_comboBox( parent);

		insert_horizontal_glue( parent);

		setup_local_log_directory_textField( parent);

		insert_horizontal_glue( parent);
	}

	/**
	 * @param parent
	 */
	private void setup_population_size_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.dialog.population.size"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_population_size_textField = new JTextField( String.valueOf( _localParameters._population_size));
		_population_size_textField.setHorizontalAlignment( SwingConstants.RIGHT);
		_population_size_textField.setEditable( false);
		panel.add( _population_size_textField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_number_of_crossovers_numberSpinner(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.dialog.number.of.crossovers"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		int minimum = Math.max( 1, ( int)( ( ( double)( _localParameters._population_size - 3) / 2.0) + 1.0));
		_number_of_crossovers_numberSpinner = new CustomNumberSpinner( this);
		_number_of_crossovers_numberSpinner.set_minimum( minimum);
		_number_of_crossovers_numberSpinner.set_maximum( 1000000);
		int number_of_crossovers = Integer.parseInt( Environment.get_instance().get( Environment._number_of_crossovers_key, "10"));
		_number_of_crossovers_numberSpinner.set_value( number_of_crossovers < minimum ? minimum : number_of_crossovers);
		panel.add( _number_of_crossovers_numberSpinner);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_number_of_generation_alternations_numberSpinner(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.dialog.number.of.generation.alternations"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_number_of_generation_alternations_numberSpinner = new CustomNumberSpinner( this);
		_number_of_generation_alternations_numberSpinner.set_minimum( 1);
		_number_of_generation_alternations_numberSpinner.set_maximum( 1000000);
		_number_of_generation_alternations_numberSpinner.set_value( Integer.parseInt( Environment.get_instance().get( Environment._number_of_generation_alternations_key, "10")));
		panel.add( _number_of_generation_alternations_numberSpinner);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_length_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.dialog.length"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_length_textField = new JTextField( String.valueOf( _localParameters._length));
		_length_textField.setHorizontalAlignment( SwingConstants.RIGHT);
		_length_textField.setEditable( false);
		panel.add( _length_textField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_algorithm_comboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.dialog.algorithm"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_algorithm_comboBox = new JComboBox( new String[] {
			ResourceManager.get_instance().get( "plugin.ga1.uniform.crossover"),
			ResourceManager.get_instance().get( "plugin.ga1.unimodal.normal.distribution.crossover")});
		_algorithm_comboBox.setSelectedItem( _algorithm_name_map.get( _localParameters._algorithm));
		panel.add( _algorithm_comboBox);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_minimization_checkBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JPanel dummy = new JPanel();
		_components.add( dummy);
		panel.add( dummy);

		_minimization_checkBox = new JCheckBox( ResourceManager.get_instance().get( "plugin.ga1.dialog.minimization"));
		_minimization_checkBox.setSelected( Environment.get_instance().get( Environment._minimization_key, "false").equals( "true"));
		panel.add( _minimization_checkBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_roulette_selection_checkBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JPanel dummy = new JPanel();
		_components.add( dummy);
		panel.add( dummy);

		_roulette_selection_checkBox = new JCheckBox( ResourceManager.get_instance().get( "plugin.ga1.dialog.roulette.selection"));
		_roulette_selection_checkBox.setSelected( Environment.get_instance().get( Environment._roulette_selection_key, "true").equals( "true"));
		panel.add( _roulette_selection_checkBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_number_variable_comboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.dialog.evaluation.value"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		JPanel partialPanel = new JPanel();
		partialPanel.setLayout( new BoxLayout( partialPanel, BoxLayout.X_AXIS));

		_spot_checkBox = new JCheckBox( ResourceManager.get_instance().get( "plugin.ga1.dialog.spot"));
		_spot_checkBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				on_spot_checkBox_state_changed( ItemEvent.SELECTED == e.getStateChange());
			}
		});
		partialPanel.add( _spot_checkBox);

		partialPanel.add( Box.createHorizontalStrut( 5));

		_number_variable_comboBox = new JComboBox();
		partialPanel.add( _number_variable_comboBox);

		panel.add( partialPanel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param selected
	 */
	protected void on_spot_checkBox_state_changed(boolean selected) {
		List resultList = new ArrayList();
		if ( !Reflection.execute_static_method( "soars.application.visualshell.layer.LayerManager", "get_instance", new Class[ 0], new Object[ 0], resultList)) {
			JOptionPane.showMessageDialog( _frame,
				"Plugin error : Reflection.execute_static_method( ... )" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator
				+ " Class name : soars.application.visualshell.layer.LayerManager" + Constant._lineSeparator
				+ " Method name : get_instance" + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.ga1.dialog.title"),
			JOptionPane.ERROR_MESSAGE);
			return;
		}

		Class cls = null;
		try {
			cls = Class.forName( "soars.application.visualshell.layer.LayerManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}

		if ( null == cls)
			return;

		Object object = null;
		if ( !resultList.isEmpty() && null != resultList.get( 0) && cls.isInstance( resultList.get( 0)))
			object = resultList.get( 0);
		else {
			JOptionPane.showMessageDialog( _frame,
				"Plugin error : Reflection.execute_static_method( ... )" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator
				+ " Class name : soars.application.visualshell.layer.LayerManager" + Constant._lineSeparator
				+ " Method name : get_instance" + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.ga1.dialog.title"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}



		resultList.clear();



		if ( !Reflection.execute_class_method( object, ( selected ? "get_spot_number_object_names" : "get_agent_number_object_names"),
				new Class[] { String.class, boolean.class}, new Object[] { "real number", false}, resultList)
			|| resultList.isEmpty()) {
			JOptionPane.showMessageDialog( _frame,
				"Plugin error : Reflection.execute_class_method( ... )" + Constant._lineSeparator
				+ " Plugin name : " + _name + Constant._lineSeparator
				+ " Class name : soars.application.visualshell.layer.LayerManager" + Constant._lineSeparator
				+ " Method name : " + ( selected ? "get_spot_number_object_names" : "get_agent_number_object_names") + Constant._lineSeparator,
				ResourceManager.get_instance().get( "plugin.ga1.dialog.title"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}

		_number_variable_comboBox.removeAllItems();
		if ( null == resultList.get( 0))
			return;

		String[] items = ( String[])resultList.get( 0);
		for ( int i = 0; i < items.length; ++i)
			_number_variable_comboBox.addItem( items[ i]);
	}

	/**
	 * @param parent
	 */
	private void setup_local_log_directory_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.dialog.local.log.directory"), SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_local_log_directory_textField = new JTextField(
			Environment.get_instance().get( Environment._local_log_directory_key, ""));
		_local_log_directory_textField.setEditable( false);
		panel.add( _local_log_directory_textField);

		panel.add( Box.createHorizontalStrut( 5));

		_local_log_directory_selector_button = new JButton( ResourceManager.get_instance().get( "plugin.ga1.dialog.local.log.reference"));
		_local_log_directory_selector_button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_local_log_directory_selector_button_actionPerformed( arg0);
			}
		});
		panel.add( _local_log_directory_selector_button);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_local_log_directory_selector_button_actionPerformed(ActionEvent actionEvent) {
		File directory = Tool.get_directory( _local_log_directory_textField.getText(),
			ResourceManager.get_instance().get( "plugin.ga1.local.log.directory.selector.dialog.title"), this);
		if ( null == directory)
			return;

		_local_log_directory_textField.setText( directory.getAbsolutePath());
	}

	/**
	 * @param parent
	 */
	private void setup_grid_panel(JPanel parent) {
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout( new BoxLayout( gridPanel, BoxLayout.X_AXIS));

		gridPanel.add( Box.createHorizontalStrut( 10));

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		setup_grid_components( panel);

		_grid_checkBox = new JCheckBox( ResourceManager.get_instance().get( "plugin.ga1.dialog.grid"));
		_grid_checkBox.setSelected( Environment.get_instance().get( Environment._grid_key, "false").equals( "true"));
		_grid_checkBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				on_grid_checkBox_state_changed( ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.setBorder( new ComponentTitledBorder( _grid_checkBox,
			panel, BorderFactory.createLineBorder( getForeground())));

		gridPanel.add( panel);

		gridPanel.add( Box.createHorizontalStrut( 5));

		parent.add( gridPanel);
	}

	/**
	 * @param selected
	 * 
	 */
	protected void on_grid_checkBox_state_changed(boolean selected) {
		if ( selected) {
			GridLoginDlg gridLoginDlg = new GridLoginDlg(
				_frame,
				ResourceManager.get_instance().get( "plugin.ga1.login.dialog.title"),
				true,
				_root_directory,
				_parameters);
			if ( !gridLoginDlg.do_modal( _frame)) {
				_grid_checkBox.setSelected( false);
				return;
			}

			patch();

			_grid_portal_ip_address_textField.setText( _parameters._grid_portal_ip_address);
			_local_username_textField.setText( _parameters._local_username);
		}

		for ( int i = 9; i < 15; ++i)
			_components.get( i).setEnabled( selected);

		_grid_portal_ip_address_textField.setEnabled( selected);
		_local_username_textField.setEnabled( selected);
		_script_directory_textField.setEnabled( selected);
		_script_directory_selector_button.setEnabled( selected);
		_log_directory_textField.setEnabled( selected);
		_log_directory_selector_button.setEnabled( selected);

		_model_builder_memory_size_label.setEnabled( selected && !_advanced_memory_setting_checkBox.isSelected());
		_model_builder_memory_size_comboBox.setEnabled( selected && !_advanced_memory_setting_checkBox.isSelected());
		_labels.get( 0).setEnabled( selected && !_advanced_memory_setting_checkBox.isSelected());

		_advanced_memory_setting_checkBox.setEnabled( selected);
		_model_builder_memory_size_numberSpinner.setEnabled( selected && _advanced_memory_setting_checkBox.isSelected());
		_labels.get( 1).setEnabled( selected && _advanced_memory_setting_checkBox.isSelected());

		if ( selected) {
			if ( _advanced_memory_setting_checkBox.isSelected()) {
				_model_builder_memory_size = String.valueOf( _model_builder_memory_size_numberSpinner.get_value());
			} else {
				String memory_size = ( String)_model_builder_memory_size_comboBox.getSelectedItem();
				_model_builder_memory_size = ( memory_size.equals( ResourceManager.get_instance().get( "plugin.ga1.dialog.model.builder.memory.non.use")) ? "0" : memory_size);
			}
		}
	}

	/**
	 * 
	 */
	private void patch() {
		String directory = ( _root_directory + "/" + _parameters._local_username);

		if ( !Environment.get_instance().get( Environment._script_directory_key, "").startsWith( directory)) {
			Environment.get_instance().set( Environment._script_directory_key, directory);
			_script_directory_textField.setText( directory);
		}

		if ( !Environment.get_instance().get( Environment._log_directory_key, "").startsWith( directory)) {
			Environment.get_instance().set( Environment._log_directory_key, directory);
			_log_directory_textField.setText( directory);
		}
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

		setup_model_builder_memory_size_comboBox( parent);

		insert_horizontal_glue( parent);

		setup_model_builder_memory_size_numberSpinner( parent);

		insert_horizontal_glue( parent);
	}

	/**
	 * @param parent
	 */
	private void setup_grid_portal_ip_address_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.dialog.gird.portal.ip.address"), SwingConstants.RIGHT);
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

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.dialog.username"), SwingConstants.RIGHT);
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

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.dialog.script.directory"), SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_script_directory_textField = new JTextField(
			Environment.get_instance().get( Environment._script_directory_key, ""));
		_script_directory_textField.setEditable( false);
		panel.add( _script_directory_textField);

		panel.add( Box.createHorizontalStrut( 5));

		_script_directory_selector_button = new JButton( ResourceManager.get_instance().get( "plugin.ga1.dialog.script.reference"));
		_script_directory_selector_button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_script_directory_selector_button_actionPerformed( arg0);
			}
		});
		panel.add( _script_directory_selector_button);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_script_directory_selector_button_actionPerformed(ActionEvent actionEvent) {
		String directory = Tool.get_remote_directory( _script_directory_textField.getText(),
			ResourceManager.get_instance().get( "plugin.ga1.script.directory.selector.dialog.title"),
			( Frame)getOwner(), this, _parameters._grid_portal_ip_address, _parameters._username, _parameters._password,
			_root_directory, _parameters._local_username);
		if ( null == directory)
			return;

		_script_directory_textField.setText( directory);
	}

	/**
	 * @param parent
	 */
	private void setup_log_directory_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.dialog.log.directory"), SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_log_directory_textField = new JTextField(
			Environment.get_instance().get( Environment._log_directory_key, ""));
		_log_directory_textField.setEditable( false);
		panel.add( _log_directory_textField);

		panel.add( Box.createHorizontalStrut( 5));

		_log_directory_selector_button = new JButton( ResourceManager.get_instance().get( "plugin.ga1.dialog.log.reference"));
		_log_directory_selector_button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_log_directory_selector_button_actionPerformed( arg0);
			}
		});
		panel.add( _log_directory_selector_button);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_log_directory_selector_button_actionPerformed(ActionEvent actionEvent) {
		String directory = Tool.get_remote_directory( _log_directory_textField.getText(),
			ResourceManager.get_instance().get( "plugin.ga1.log.directory.selector.dialog.title"),
			( Frame)getOwner(), this, _parameters._grid_portal_ip_address, _parameters._username, _parameters._password,
			_root_directory, _parameters._local_username);
		if ( null == directory)
			return;

		_log_directory_textField.setText( directory);
	}

	/**
	 * @param parent
	 */
	private void setup_model_builder_memory_size_comboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_model_builder_memory_size_label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.dialog.model.builder.memory.size"));
		_model_builder_memory_size_label.setHorizontalAlignment( SwingConstants.RIGHT);
		_components.add( _model_builder_memory_size_label);
		panel.add( _model_builder_memory_size_label);

		panel.add( Box.createHorizontalStrut( 5));

		String[] memory_sizes = new String[ 1 + Constant._memory_sizes.length];
		memory_sizes[ 0] = ResourceManager.get_instance().get( "plugin.ga1.dialog.model.builder.memory.non.use");
		System.arraycopy( Constant._memory_sizes, 0, memory_sizes, 1, Constant._memory_sizes.length);
		_model_builder_memory_size_comboBox = ComboBox.create( memory_sizes, 100, true, new CommonComboBoxRenderer( null, true));
		_model_builder_memory_size_comboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String memory_size = ( String)_model_builder_memory_size_comboBox.getSelectedItem();
				_model_builder_memory_size = ( memory_size.equals( ResourceManager.get_instance().get( "plugin.ga1.dialog.model.builder.memory.non.use")) ? "0" : memory_size);
			}
		});
		panel.add( _model_builder_memory_size_comboBox);

		JLabel label = new JLabel( "MB");
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_model_builder_memory_size_numberSpinner(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_advanced_memory_setting_checkBox = new JCheckBox( ResourceManager.get_instance().get( "plugin.ga1.dialog.model.builder.advanced.memory.size"));
		_advanced_memory_setting_checkBox.setHorizontalAlignment( SwingConstants.RIGHT);
		_advanced_memory_setting_checkBox.setSelected( Environment.get_instance().get(
			Environment._advanced_memory_setting_key, Constant._default_advanced_memory_setting).equals( "true"));
		_advanced_memory_setting_checkBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				on_advanced_memory_setting_checkBox_state_changed( ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		_components.add( _advanced_memory_setting_checkBox);
		panel.add( _advanced_memory_setting_checkBox);

		panel.add( Box.createHorizontalStrut( 5));

		_model_builder_memory_size_numberSpinner = new CustomNumberSpinner( this);
		_model_builder_memory_size_numberSpinner.set_minimum( 0);
		_model_builder_memory_size_numberSpinner.set_maximum( 1000000);
		panel.add( _model_builder_memory_size_numberSpinner);

		JLabel label = new JLabel( "MB");
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param selected
	 */
	private void on_advanced_memory_setting_checkBox_state_changed(boolean selected) {
		_model_builder_memory_size_label.setEnabled( !selected);
		_model_builder_memory_size_comboBox.setEnabled( !selected);
		_labels.get( 0).setEnabled( !selected);
		_model_builder_memory_size_numberSpinner.setEnabled( selected);
		_labels.get( 1).setEnabled( selected);
		if ( selected)
			_model_builder_memory_size = String.valueOf( _model_builder_memory_size_numberSpinner.get_value());
		else {
			String memory_size = ( String)_model_builder_memory_size_comboBox.getSelectedItem();
			_model_builder_memory_size = ( memory_size.equals( ResourceManager.get_instance().get( "plugin.ga1.dialog.model.builder.memory.non.use")) ? "0" : memory_size);
		}
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
	 * @param parent
	 */
	private void setup_right_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));
		setup_right_north_panel( northPanel);
		panel.add( northPanel, "North");

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));
		setup_right_center_panel( centerPanel);
		panel.add( centerPanel);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_right_north_panel(JPanel parent) {
		insert_horizontal_glue( parent);
		setup_error_log_label( parent);
	}

	/**
	 * @param parent
	 */
	private void setup_error_log_label(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.dialog.error.log"));
		panel.add( label);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_right_center_panel(JPanel parent) {
		setup_log_textArea( parent);
	}

	/**
	 * @param parent
	 */
	private void setup_log_textArea(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_log_textArea = new JTextArea();
		_log_textArea.setEditable( false);
		_log_textArea.setTabSize( 2);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _log_textArea);
		panel.add( scrollPane);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_south_panel(JPanel parent) {
		insert_horizontal_glue( parent);

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));
		setup_ok_and_cancel_button( panel,
			ResourceManager.get_instance().get( "dialog.start"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			false, false);
		parent.add( panel);

		insert_horizontal_glue( parent);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( JComponent label:_components)
			width = Math.max( width, label.getPreferredSize().width);

		for ( JComponent label:_components)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		_model_builder_memory_size = Environment.get_instance().get( Environment._model_builder_memory_size_key, Constant._defaultMemorySize);

		_model_builder_memory_size_label.setEnabled( !_advanced_memory_setting_checkBox.isSelected());
		_model_builder_memory_size_comboBox.setEnabled( !_advanced_memory_setting_checkBox.isSelected());
		_labels.get( 0).setEnabled( !_advanced_memory_setting_checkBox.isSelected());
		_model_builder_memory_size_numberSpinner.setEnabled( _advanced_memory_setting_checkBox.isSelected());
		_labels.get( 1).setEnabled( _advanced_memory_setting_checkBox.isSelected());

		_model_builder_memory_size_numberSpinner.set_value( new Integer( _model_builder_memory_size).intValue());
		if ( !_advanced_memory_setting_checkBox.isSelected()) {
			if ( _model_builder_memory_size.equals( "0") || Constant.contained( _model_builder_memory_size))
				_model_builder_memory_size_comboBox.setSelectedItem( _model_builder_memory_size.equals( ResourceManager.get_instance().get( "plugin.ga1.dialog.model.builder.memory.non.use")) ? "0" : _model_builder_memory_size);
			else {
				_advanced_memory_setting_checkBox.setSelected( true);
				_model_builder_memory_size_numberSpinner.set_value( new Integer( _model_builder_memory_size).intValue());
			}
		}

		on_spot_checkBox_state_changed( false);

		on_grid_checkBox_state_changed( _grid_checkBox.isSelected());


		optimize_window_rectangle();

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				int width = getSize().width;
				int height = getSize().height;
				setSize( ( _minimum_width > width) ? _minimum_width : width,
					( _minimum_height > height) ? _minimum_height : height);
			}
		});
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		_localParameters._population_size = Integer.parseInt( _population_size_textField.getText());
		_localParameters._length = Integer.parseInt( _length_textField.getText());
		_localParameters._algorithm = _algorithm_type_map.get( ( String)_algorithm_comboBox.getSelectedItem());
		_localParameters._spot = _spot_checkBox.isSelected();
		_localParameters._number_variable = ( String)_number_variable_comboBox.getSelectedItem();

		int number_of_crossovers = _number_of_crossovers_numberSpinner.get_value();
		Environment.get_instance().set( Environment._number_of_crossovers_key, String.valueOf( number_of_crossovers));


		int number_of_generation_alternations = _number_of_generation_alternations_numberSpinner.get_value();
		Environment.get_instance().set( Environment._number_of_generation_alternations_key, String.valueOf( number_of_generation_alternations));


		Environment.get_instance().set( Environment._minimization_key, _minimization_checkBox.isSelected() ? "true" : "false");


		Environment.get_instance().set( Environment._roulette_selection_key, _roulette_selection_checkBox.isSelected() ? "true" : "false");


		String local_log_directory = _local_log_directory_textField.getText();
		if ( null == local_log_directory || local_log_directory.equals( ""))
			return;

		Environment.get_instance().set( Environment._local_log_directory_key, local_log_directory);


		Environment.get_instance().set( Environment._grid_key, _grid_checkBox.isSelected() ? "true" : "false");


		if ( _grid_checkBox.isSelected()) {
			Environment.get_instance().set( Environment._grid_portal_ip_address_key, _parameters._grid_portal_ip_address);


			Environment.get_instance().set( Environment._local_username_key, _parameters._local_username);


			String script_directory = _script_directory_textField.getText();
			if ( null == script_directory || script_directory.equals( ""))
				return;

			Environment.get_instance().set( Environment._script_directory_key, script_directory);


			String log_directory = _log_directory_textField.getText();
			if ( null == log_directory || log_directory.equals( ""))
				return;

			Environment.get_instance().set( Environment._log_directory_key, log_directory);


			Environment.get_instance().set( Environment._advanced_memory_setting_key, _advanced_memory_setting_checkBox.isSelected() ? "true" : "false");


			Environment.get_instance().set( Environment._model_builder_memory_size_key, _model_builder_memory_size);
		}

		set_property_to_environment_file();

		setEnabled( false);
		if ( !Tool.clear( _parameters, _log_textArea)) {
			setEnabled( true);
			return;
		}

		super.on_ok(actionEvent);
	}
}
