/**
 * 
 */
package soars.plugin.visualshell.ga1.main;

import ga.bitstring.TBitStringIndividual;
import ga.bitstring.TUxMgg;
import ga.core.IIndividual;
import ga.realcode.TRealNumberIndividual;
import ga.realcode.TUndxMgg;

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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import soars.common.utility.swing.window.Dialog;
import soars.common.utility.tool.reflection.Reflection;

/**
 * @author kurata
 *
 */
public class GAMonitorDlg extends Dialog implements Runnable {

	/**
	 * 
	 */
	protected int _minimum_width = 800;

	/**
	 * 
	 */
	protected int _minimum_height = 480;

	/**
	 * 
	 */
	protected String _name = "";

	/**
	 * 
	 */
	protected LocalParameters _localParameters = null;

	/**
	 * 
	 */
	protected Parameters _parameters = null;

	/**
	 * 
	 */
	protected Component _parent = null;

	/**
	 * 
	 */
	protected List<JComponent> _components = new ArrayList<JComponent>();

	/**
	 * 
	 */
	private JTextField _population_size_textField = null;

	/**
	 * 
	 */
	private JTextField _number_of_crossovers_textField = null;

	/**
	 * 
	 */
	private JTextField _number_of_generation_alternations_textField = null;

	/**
	 * 
	 */
	private JTextField _length_textField = null;

	/**
	 * 
	 */
	private JTextField _algorithm_textField = null;

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
	private JTextField _number_variable_textField = null;

	/**
	 * 
	 */
	private JTextField _local_log_directory_textField = null;

	/**
	 * 
	 */
	protected JTextArea _log_textArea = null;

	/**
	 * 
	 */
	protected JProgressBar _ga_progressBar = null;

	/**
	 * 
	 */
	protected JTextField _number_of_scripts_textField = null;

	/**
	 * 
	 */
	protected JTextField _status_textField = null;

	/**
	 * 
	 */
	protected JProgressBar _progressBar = null;

	/**
	 * 
	 */
	protected JButton _stop_button = null;

	/**
	 * 
	 */
	private Thread _thread = null;

	/**
	 * 
	 */
	protected Object _original_experimentManager = null;

	/**
	 * 
	 */
	protected Object _experimentManager = null;

	/**
	 * 
	 */
	protected Environment _environment = null;

	/**
	 * 
	 */
	protected int _generation_alternations = -1;

	/**
	 * 
	 */
	protected Map<String, String> _algorithm_name_map = null;

	/**
	 * 
	 */
	protected Map<String, String> _algorithm_type_map = null;

	/**
	 * 
	 */
	protected TUxMgg _uxMgg = null;

	/**
	 * 
	 */
	protected List<TBitStringIndividual> _bitStringIndividuals = null;

	/**
	 * 
	 */
	protected TUndxMgg _undxMgg = null;

	/**
	 * 
	 */
	protected List<TRealNumberIndividual> _realNumberIndividuals = null;

	/**
	 * 
	 */
	private boolean _first = true;

	/**
	 * 
	 */
	protected boolean _stop = false;

	/**
	 * 
	 */
	protected boolean _termination = false;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws HeadlessException
	 */
	public GAMonitorDlg(Frame arg0, String arg1, boolean arg2, String name, LocalParameters localParameters, Map<String, String> algorithm_name_map, Map<String, String> algorithm_type_map, Object original_experimentManager, Environment environment) throws HeadlessException {
		super(arg0, arg1, arg2);
		_parent = arg0;
		_name = name;
		_localParameters = localParameters;
		_algorithm_name_map = algorithm_name_map;
		_algorithm_type_map = algorithm_type_map;
		_original_experimentManager = original_experimentManager;
		_environment = environment;
	}

	/**
	 * @return
	 */
	private boolean get_experimentManager() {
		List resultList = new ArrayList();
		if ( !Reflection.execute_class_method( _original_experimentManager, "extract_for_genetic_algorithm", new Class[ 0], new Object[ 0], resultList)) {
			JOptionPane.showMessageDialog( _parent,
				"Plugin error : Reflection.execute_class_method( ... )" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator
					+ " Class name : soars.application.visualshell.object.experiment.ExperimentManager" + Constant._lineSeparator
					+ " Method name : extract_for_genetic_algorithm" + Constant._lineSeparator,
				getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		Class cls = null;
		try {
			cls = Class.forName( "soars.application.visualshell.object.experiment.ExperimentManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		if ( null == cls)
			return false;

		if ( !resultList.isEmpty() && null != resultList.get( 0) && cls.isInstance( resultList.get( 0)))
			_experimentManager = resultList.get( 0);
		else {
			JOptionPane.showMessageDialog( _parent,
				"Plugin error : Reflection.execute_class_method( ... )" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator
					+ " Class name : soars.application.visualshell.object.experiment.ExperimentManager" + Constant._lineSeparator
					+ " Method name : extract_for_genetic_algorithm" + Constant._lineSeparator,
					getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	/**
	 * @return
	 */
	protected Rectangle get_rectangle_from_environment_file() {
		return null;
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
		Environment.get_instance().store();
	}

	/**
	 * @return
	 */
	public boolean do_modal() {
		Rectangle rectangle = get_rectangle_from_environment_file();
		if ( null == rectangle)
			return do_modal( getOwner());
		else
			return do_modal( rectangle);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_cancel(java.awt.event.ActionEvent)
	 */
	protected void on_cancel(ActionEvent actionEvent) {
		set_property_to_environment_file();
		super.on_cancel(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !get_experimentManager())
			return false;


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


		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);


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

		setup_local_panel( parent);

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

		setup_number_of_crossovers_textField( parent);

		insert_horizontal_glue( parent);

		setup_number_of_generation_alternations_textField( parent);

		insert_horizontal_glue( parent);

		setup_length_textField( parent);

		insert_horizontal_glue( parent);

		setup_algorithm_textField( parent);

		insert_horizontal_glue( parent);

		setup_minimization_checkBox( parent);

		insert_horizontal_glue( parent);

		setup_roulette_selection_checkBox( parent);

		insert_horizontal_glue( parent);

		setup_number_variable_textField( parent);

		insert_horizontal_glue( parent);

		setup_local_log_directory_textField( parent);

		insert_horizontal_glue( parent);

		setup_ga_progressBar( parent);

		insert_horizontal_glue( parent);
	}

	/**
	 * @param parent
	 */
	private void setup_population_size_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.frame.population.size"));
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
	private void setup_number_of_crossovers_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.frame.number.of.crossovers"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_number_of_crossovers_textField = new JTextField( Environment.get_instance().get( Environment._number_of_crossovers_key, "10"));
		_number_of_crossovers_textField.setHorizontalAlignment( SwingConstants.RIGHT);
		_number_of_crossovers_textField.setEditable( false);
		panel.add( _number_of_crossovers_textField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_number_of_generation_alternations_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.frame.number.of.generation.alternations"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_number_of_generation_alternations_textField = new JTextField( Environment.get_instance().get( Environment._number_of_generation_alternations_key, "10"));
		_number_of_generation_alternations_textField.setHorizontalAlignment( SwingConstants.RIGHT);
		_number_of_generation_alternations_textField.setEditable( false);
		panel.add( _number_of_generation_alternations_textField);

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

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.frame.length"));
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
	private void setup_algorithm_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.frame.algorithm"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_algorithm_textField = new JTextField( _algorithm_name_map.get( _localParameters._algorithm));
		_algorithm_textField.setEditable( false);
		panel.add( _algorithm_textField);

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

		_minimization_checkBox = new JCheckBox( ResourceManager.get_instance().get( "plugin.ga1.frame.minimization"));
		_minimization_checkBox.setSelected( Environment.get_instance().get( Environment._minimization_key, "false").equals( "true"));
		_minimization_checkBox.setEnabled( false);
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

		_roulette_selection_checkBox = new JCheckBox( ResourceManager.get_instance().get( "plugin.ga1.frame.roulette.selection"));
		_roulette_selection_checkBox.setSelected( Environment.get_instance().get( Environment._roulette_selection_key, "true").equals( "true"));
		_roulette_selection_checkBox.setEnabled( false);
		panel.add( _roulette_selection_checkBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_number_variable_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.frame.evaluation.value"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		JPanel partialPanel = new JPanel();
		partialPanel.setLayout( new BoxLayout( partialPanel, BoxLayout.X_AXIS));

		_spot_checkBox = new JCheckBox( ResourceManager.get_instance().get( "plugin.ga1.frame.spot"));
		_spot_checkBox.setSelected( _localParameters._spot);
		_spot_checkBox.setEnabled( false);
		partialPanel.add( _spot_checkBox);

		partialPanel.add( Box.createHorizontalStrut( 5));

		_number_variable_textField = new JTextField( _localParameters._number_variable);
		_number_variable_textField.setEditable( false);
		partialPanel.add( _number_variable_textField);

		panel.add( partialPanel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_local_log_directory_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.frame.local.log.directory"), SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_local_log_directory_textField = new JTextField( Environment.get_instance().get( Environment._local_log_directory_key, ""));
		_local_log_directory_textField.setEditable( false);
		panel.add( _local_log_directory_textField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_ga_progressBar(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.frame.ga.progress"), SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_ga_progressBar = new JProgressBar();
		_ga_progressBar.setStringPainted( true);
		_ga_progressBar.setMinimum( 0);
		_ga_progressBar.setMaximum( Integer.parseInt( _environment.get( Environment._number_of_generation_alternations_key, "10")));
		panel.add( _ga_progressBar);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	protected void setup_local_panel(JPanel parent) {
	}

	/**
	 * @param parent
	 */
	protected void setup_grid_panel(JPanel parent) {
	}

	/**
	 * 
	 */
	protected void setup_number_of_scripts_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.frame.number.of.script"), SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_number_of_scripts_textField = new JTextField( "0");
		_number_of_scripts_textField.setHorizontalAlignment( SwingConstants.RIGHT);
		_number_of_scripts_textField.setEditable( false);
		panel.add( _number_of_scripts_textField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * 
	 */
	protected void setup_status_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.frame.status"), SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_status_textField = new JTextField( "");
		_status_textField.setEditable( false);
		panel.add( _status_textField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * 
	 */
	protected void setup_progressBar(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.frame.local.progress"), SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_progressBar = new JProgressBar();
		_progressBar.setStringPainted( true);
		panel.add( _progressBar);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
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

		JLabel label = new JLabel( ResourceManager.get_instance().get( "plugin.ga1.frame.log"));
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
	 * @param southPanel
	 */
	private void setup_south_panel(JPanel parent) {
		insert_horizontal_glue( parent);

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));
		_stop_button = new JButton( ResourceManager.get_instance().get( "dialog.cancel"));
		_stop_button.setEnabled( false);
		_stop_button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_stop( arg0);
			}
		});
		panel.add( _stop_button);

		parent.add( panel);

		insert_horizontal_glue( parent);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_stop(ActionEvent actionEvent) {
		_stop = true;
		_stop_button.setEnabled( false);
	}

	/**
	 * 
	 */
	protected void on_stop() {
		if ( _termination)
			return;

		JOptionPane.showMessageDialog( this,
			ResourceManager.get_instance().get( "plugin.ga1.frame.canceled"),
			getTitle(),
			JOptionPane.INFORMATION_MESSAGE);
		_status_textField.setText( "Canceled ...");
		_status_textField.update( _status_textField.getGraphics());
		_log_textArea.append( ResourceManager.get_instance().get( "plugin.ga1.frame.canceled"));
		setEnabled( true);
		//_stop_button.setEnabled( false);
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
		optimize_window_rectangle();

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				int width = getSize().width;
				int height = getSize().height;
				setSize( ( _minimum_width > width) ? _minimum_width : width,
					( _minimum_height > height) ? _minimum_height : height);
			}
		});

		_thread = new Thread( this);
		_thread.start();


		_stop_button.setEnabled( true);
	}

	/**
	 * @return
	 */
	protected File get_local_log_directory() {
		File local_log_directory = new File( _environment.get( Environment._local_log_directory_key, ""));
		if ( !make_local_log_directory( local_log_directory))
			return null;

		return local_log_directory;
	}

	/**
	 * @param local_log_directory
	 * @return
	 */
	private boolean make_local_log_directory(File local_log_directory) {
		if ( local_log_directory.exists()) {
//			if ( local_log_directory.isDirectory())
//				_log_textArea.append( "Overwritten! : " + local_log_directory.getAbsolutePath() + "\n");
//			else {
			if ( !local_log_directory.isDirectory()) {
				if ( !local_log_directory.delete()) {
					_log_textArea.append( "Could not make! : " + local_log_directory.getAbsolutePath() + "\n");
					return false;
				}
	
				if ( !local_log_directory.mkdirs()) {
					_log_textArea.append( "Could not make! : " + local_log_directory.getAbsolutePath() + "\n");
					return false;
				}
			}
		} else {
			if ( !local_log_directory.mkdirs()) {
				_log_textArea.append( "Could not make! : " + local_log_directory.getAbsolutePath() + "\n");
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
	}

	/**
	 * @param local_log_directory
	 * @return
	 */
	protected List<Double> prepare_to_restart(File local_log_directory) {
		List<Double> evaluation_values = evaluate( local_log_directory);
		if ( null == evaluation_values)
			return null;

		// 親の選択と子個体生成
		if ( !select_parents_and_make_kids())
			return null;

		return evaluation_values;
	}

	/**
	 * @param local_log_directory
	 * @return
	 */
	protected List<Double> evaluate(File local_log_directory) {
		// 評価値を集める
		List<Double> evaluation_values = get_evaluation_values( local_log_directory);
		if ( null == evaluation_values)
			return null;

		// 集団の各個体に評価値を設定
		if ( !evaluate( evaluation_values))
			return null;

		// ２回目以降ならここで生存選択を行う
		if ( _first)
			_first = false;
		else {
			if ( !do_selection_for_survival())
				return null;
		}

		return evaluation_values;
	}

	/**
	 * @return
	 */
	protected boolean setup() {
		// 前回のログ(グリッド上及びローカル)を消去
		// TODO 不要にしたい
//		if ( !Tool.clear( _parameters, _log_textArea))
//			return false;

		// 初回ならGAのインスタンスを生成し、SOARS側の実験支援テーブル(_experimentManager)のデータ配列をGAライブラリ側に設定
		// ２回目以降ならGAライブラリ側のデータ配列をSOARS側の実験支援テーブル(_experimentManager)に設定
		TreeMap<String, TreeMap<String, String>> experimentManager = ( TreeMap)_experimentManager;
		if ( _localParameters._algorithm.equals( "UxMgg")) {
			if ( null == _uxMgg) {
				_uxMgg = new TUxMgg( _environment.get( Environment._minimization_key, "").equals( "true"),
					_localParameters._length,
					_localParameters._population_size,
					Integer.parseInt( _environment.get( Environment._number_of_crossovers_key, "10")),
					_environment.get( Environment._roulette_selection_key, "").equals( "true"));
				_bitStringIndividuals = _uxMgg.getInitialPopulation();
				if ( experimentManager.size() != _bitStringIndividuals.size())
					return false;

				for ( int i = 0; i < _bitStringIndividuals.size(); ++i) {
					TreeMap<String, String> initialValueMap = experimentManager.get( "name" + String.valueOf( i + 1));
					if ( null == initialValueMap)
						return false;

					List<String> list = get_data_array( initialValueMap);
					//List<String> list = new ArrayList<String>(initialValueMap.values());
					if ( list.size() != _bitStringIndividuals.get( i).getBitString().getLength())
						return false;

					for ( int j = 0; j < list.size(); ++j) {
						if ( list.get( j).equals( ""))
							_bitStringIndividuals.get( i).getBitString().setData( j, 0);
						else {
							try {
								_bitStringIndividuals.get( i).getBitString().setData( j, Integer.parseInt( list.get( j)));
							} catch (NumberFormatException e) {
								//e.printStackTrace();
								return false;
							}
						}
					}
				}
			} else {
				List<List<String>> population = new ArrayList<List<String>>();
				for ( TBitStringIndividual bitStringIndividual:_bitStringIndividuals) {
					List<String> inititial_values = new ArrayList<String>();
					for ( int i = 0; i < bitStringIndividual.getBitString().getLength(); ++i)
						inititial_values.add( String.valueOf( bitStringIndividual.getBitString().getData( i)));
					population.add( inititial_values);
				}
				if ( !update_experimentManager( population))
					return false;
			}
		} else if ( _localParameters._algorithm.equals( "UndxMgg")) {
			if ( null == _undxMgg) {
				_undxMgg = new TUndxMgg( _environment.get( Environment._minimization_key, "").equals( "true"),
					_localParameters._length,
					_localParameters._population_size,
					Integer.parseInt( _environment.get( Environment._number_of_crossovers_key, "10")));
				_realNumberIndividuals = _undxMgg.getInitialPopulation();
				if ( experimentManager.size() != _realNumberIndividuals.size())
					return false;

				for ( int i = 0; i < _realNumberIndividuals.size(); ++i) {
					TreeMap<String, String> initialValueMap = experimentManager.get( "name" + String.valueOf( i + 1));
					if ( null == initialValueMap)
						return false;

					List<String> list = get_data_array( initialValueMap);
					//List<String> list = new ArrayList<String>(initialValueMap.values());
					if ( list.size() != _realNumberIndividuals.get( i).getVector().getLength())
						return false;

					for ( int j = 0; j < list.size(); ++j) {
						if ( list.get( j).equals( ""))
							_realNumberIndividuals.get( i).getVector().setData( j, 0.0);
						else {
							try {
								_realNumberIndividuals.get( i).getVector().setData( j, Double.parseDouble( list.get( j)));
							} catch (NumberFormatException e) {
								//e.printStackTrace();
								return false;
							}
						}
					}
				}
			} else {
				List<List<String>> population = new ArrayList<List<String>>();
				for ( TRealNumberIndividual realNumberIndividual:_realNumberIndividuals) {
					List<String> inititial_values = new ArrayList<String>();
					for ( int i = 0; i < realNumberIndividual.getVector().getLength(); ++i)
						inititial_values.add( String.valueOf( realNumberIndividual.getVector().getData( i)));
					population.add( inititial_values);
				}
				if ( !update_experimentManager( population))
					return false;
			}
		} else
			return false;

		return true;
	}

	/**
	 * @param initialValueMap
	 * @return
	 */
	private List<String> get_data_array(TreeMap<String, String> initialValueMap) {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		for ( String key:initialValueMap.keySet()) {
			if ( !key.startsWith( "$__val"))
				continue;

			list.add( initialValueMap.get( key));
		}
		return list;
	}

	/**
	 * @param population
	 * @return
	 */
	private boolean update_experimentManager(List<List<String>> population) {
		// GAライブラリ側のデータ配列をSOARS側の実験支援テーブル(_experimentManager)に設定
		List resultList = new ArrayList();
		if ( !Reflection.execute_class_method( _experimentManager, "update_for_genetic_algorithm",
			new Class[] { List.class}, new Object[] { population}, resultList)) {
			JOptionPane.showMessageDialog( this,
				"Plugin error : Reflection.execute_class_method( ... )" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator
					+ " Class name : soars.application.visualshell.object.experiment.ExperimentManager" + Constant._lineSeparator
					+ " Method name : update_for_genetic_algorithm" + Constant._lineSeparator,
				getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( resultList.isEmpty() || null == resultList.get( 0) || !Boolean.class.isInstance( resultList.get( 0)))
			return false;

		return ( ( Boolean)resultList.get( 0)).booleanValue();
	}

	/**
	 * @param evaluation_values
	 * @return
	 */
	private boolean evaluate(List<Double> evaluation_values) {
		// GAライブラリ側に評価値を設定
		if ( _localParameters._algorithm.equals( "UxMgg")) {
			if ( evaluation_values.size() != _bitStringIndividuals.size())
				return false;

			for ( int i = 0; i < _bitStringIndividuals.size(); ++i) {
				_bitStringIndividuals.get( i).setEvaluationValue( evaluation_values.get( i));
				_bitStringIndividuals.get( i).setStatus( IIndividual.VALID);
			}
		} else if ( _localParameters._algorithm.equals( "UndxMgg")) {
			if ( evaluation_values.size() != _realNumberIndividuals.size())
				return false;

			for ( int i = 0; i < _realNumberIndividuals.size(); ++i) {
				_realNumberIndividuals.get( i).setEvaluationValue( evaluation_values.get( i));
				_realNumberIndividuals.get( i).setStatus( IIndividual.VALID);
			}
		} else
			return false;

		return true;
	}

	/**
	 * @return
	 */
	private boolean do_selection_for_survival() {
		// GAライブラリの生存選択実行
		if ( _localParameters._algorithm.equals( "UxMgg"))
			_bitStringIndividuals = _uxMgg.doSelectionForSurvival();
		else if ( _localParameters._algorithm.equals( "UndxMgg"))
			_realNumberIndividuals = _undxMgg.doSelectionForSurvival();
		else
			return false;

		return true;
	}

	/**
	 * @return
	 */
	private boolean select_parents_and_make_kids() {
		// 親の選択と子個体生成
		if ( _localParameters._algorithm.equals( "UxMgg"))
			_bitStringIndividuals = _uxMgg.selectParentsAndMakeKids();
		else if ( _localParameters._algorithm.equals( "UndxMgg"))
			_realNumberIndividuals = _undxMgg.selectParentsAndMakeKids();
		else
			return false;

		return true;
	}

	/**
	 * @param local_log_directory
	 * @return
	 */
	private List<Double> get_evaluation_values(File local_log_directory) {
		// SOARSのログファイルから評価値リストを生成
		List<Double> evaluation_values = new ArrayList<Double>();
		int index = 1;
		while ( true) {
			File file = new File( local_log_directory,
				"name" + String.valueOf( index) +"/1/" + ( _localParameters._spot ? "spots/" : "agents/") + _localParameters._number_variable + ".log");
			if ( !file.exists() || !file.canRead())
				break;

			Double evaluation_value = get_evaluation_value( file);
			if ( null == evaluation_value)
				return null;

			evaluation_values.add( evaluation_value);
			++index;
		}
		return evaluation_values;
	}

	/**
	 * @param file
	 * @return
	 */
	private Double get_evaluation_value(File file) {
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader( new InputStreamReader( new FileInputStream( file), "UTF8"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		Double evaluation_value = get_evaluation_value( bufferedReader);

		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return evaluation_value;
	}

	/**
	 * @param bufferedReader
	 * @return
	 */
	private Double get_evaluation_value(BufferedReader bufferedReader) {
		Double evaluation_value = null;

		while ( true) {
			String line = null;
			try {
				line = bufferedReader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

			if ( null == line)
				break;

			if ( line.startsWith( "\t"))
				continue;

			String[] words = line.split( "\t");
			if ( null == words || 3 > words.length)
				continue;

			evaluation_value = new Double( words[ 2]);
		}

		return evaluation_value;
	}

	/**
	 * @param local_log_directory
	 * @return
	 */
	protected boolean output(File local_log_directory) {
		// 最終的なデータ配列でSOARS実験支援テーブル用のファイルを作成
		if ( _localParameters._algorithm.equals( "UxMgg")) {
			List<List<String>> population = new ArrayList<List<String>>();
			for ( TBitStringIndividual bitStringIndividual:_bitStringIndividuals) {
				List<String> inititial_values = new ArrayList<String>();
				for ( int i = 0; i < bitStringIndividual.getBitString().getLength(); ++i)
					inititial_values.add( String.valueOf( bitStringIndividual.getBitString().getData( i)));
				population.add( inititial_values);
			}
			Object experimentManager = get_experimentManager( population);
			if ( null == experimentManager)
				return false;

			_experimentManager = experimentManager;
		} else if ( _localParameters._algorithm.equals( "UndxMgg")) {
			List<List<String>> population = new ArrayList<List<String>>();
			for ( TRealNumberIndividual realNumberIndividual:_realNumberIndividuals) {
				List<String> inititial_values = new ArrayList<String>();
				for ( int i = 0; i < realNumberIndividual.getVector().getLength(); ++i)
					inititial_values.add( String.valueOf( realNumberIndividual.getVector().getData( i)));
				population.add( inititial_values);
			}
			Object experimentManager = get_experimentManager( population);
			if ( null == experimentManager)
				return false;

			_experimentManager = experimentManager;
		} else
			return false;

		return write( local_log_directory);
	}

	/**
	 * @param population
	 * @return
	 */
	private Object get_experimentManager(List<List<String>> population) {
		List resultList = new ArrayList();
		if ( !Reflection.execute_class_method( _original_experimentManager, "get_for_genetic_algorithm",
			new Class[] { List.class}, new Object[] { population}, resultList)) {
			JOptionPane.showMessageDialog( this,
				"Plugin error : Reflection.execute_class_method( ... )" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator
					+ " Class name : soars.application.visualshell.object.experiment.ExperimentManager" + Constant._lineSeparator
					+ " Method name : get_for_genetic_algorithm" + Constant._lineSeparator,
				getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		Class cls = null;
		try {
			cls = Class.forName( "soars.application.visualshell.object.experiment.ExperimentManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		if ( null == cls)
			return false;

		if ( resultList.isEmpty() || null == resultList.get( 0) || !cls.isInstance( resultList.get( 0))) {
			JOptionPane.showMessageDialog( this,
				"Plugin error : Reflection.execute_class_method( ... )" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator
					+ " Class name : soars.application.visualshell.object.experiment.ExperimentManager" + Constant._lineSeparator
					+ " Method name : extract_for_genetic_algorithm" + Constant._lineSeparator,
					getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return resultList.get( 0);
	}

	/**
	 * @param local_log_directory
	 * @return
	 */
	private boolean write(File local_log_directory) {
		List resultList = new ArrayList();
		if ( !Reflection.execute_class_method( _experimentManager, "export_table",
			new Class[] { File.class}, new Object[] { new File( local_log_directory, "result.txt")}, resultList)) {
			JOptionPane.showMessageDialog( this,
				"Plugin error : Reflection.execute_class_method( ... )" + Constant._lineSeparator
					+ " Plugin name : " + _name + Constant._lineSeparator
					+ " Class name : soars.application.visualshell.object.experiment.ExperimentManager" + Constant._lineSeparator
					+ " Method name : export_table" + Constant._lineSeparator,
					getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( resultList.isEmpty() || null == resultList.get( 0) || !Boolean.class.isInstance( resultList.get( 0)))
			return false;

		return ( ( Boolean)resultList.get( 0)).booleanValue();
	}
}
