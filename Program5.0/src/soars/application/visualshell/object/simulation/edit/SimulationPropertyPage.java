/*
 * 2005/06/21
 */
package soars.application.visualshell.object.simulation.edit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.simulation.SimulationManager;
import soars.common.utility.swing.border.ComponentTitledBorder;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;
import soars.common.utility.swing.panel.StandardPanel;
import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextLimiter;
import soars.common.utility.swing.text.TextUndoRedoManager;

/**
 * @author kurata
 */
public class SimulationPropertyPage extends StandardPanel implements ITextUndoRedoManagerCallBack {

	/**
	 * 
	 */
	public String _title = ResourceManager.get_instance().get( "edit.simulation.dialog.title");

	/**
	 * 
	 */
	private JPanel[] _panels = new JPanel[] {
		null, null, null, null
	};

	/**
	 * 
	 */
	private JLabel[] _startTimeLabels = new JLabel[ 3];

	/**
	 * 
	 */
	private JLabel[] _stepTimeLabels = new JLabel[ 3];

	/**
	 * 
	 */
	private JLabel[] _endtimeLabels = new JLabel[ 3];

	/**
	 * 
	 */
	private JLabel[] _logStepTimeLabels = new JLabel[ 3];

	/**
	 * 
	 */
	private JComponent[] _startTimeComponents = new JComponent[ 3];

	/**
	 * 
	 */
	private JComponent[] _stepTimeComponents = new JComponent[ 3];

	/**
	 * 
	 */
	private JComponent[] _endTimeComponents = new JComponent[ 3];

	/**
	 * 
	 */
	private JCheckBox _setLogStepTimeCheckBox = null;

	/**
	 * 
	 */
	private JComponent[] _logStepTimeComponents = new JComponent[ 3];

	/**
	 * 
	 */
	private JCheckBox _exportEndTimeCheckBox = null;

	/**
	 * 
	 */
	private JLabel _randomSeedLabel = null;

	/**
	 * 
	 */
	private JTextField _randomSeedTextField = null;

	/**
	 * 
	 */
	private List<TextUndoRedoManager> _textUndoRedoManagers = new ArrayList<TextUndoRedoManager>();

	/**
	 * 
	 */
	protected Frame _owner = null;

	/**
	 * 
	 */
	protected Component _parent = null;

	/**
	 * @param owner
	 * @param parent
	 */
	public SimulationPropertyPage(Frame owner, Component parent) {
		super();
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @param day
	 * @return
	 */
	private String get_day(String day) {
		try {
			int number = Integer.parseInt( day);
			return String.valueOf( number);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return "0";
		}
	}

	/**
	 * @param comboBox
	 */
	private void update_time_comboBox_width(JComboBox comboBox) {
		Dimension dimension = comboBox.getPreferredSize();
		Font font = comboBox.getFont();
		FontMetrics fontMetrics = comboBox.getFontMetrics( font);
		dimension.width += fontMetrics.stringWidth( "88") * 3 / 2;
		comboBox.setPreferredSize( dimension);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.panel.StandardPanel#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;


		setLayout( new BorderLayout());


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		northPanel.add( create1());

		insert_horizontal_glue( northPanel);

		northPanel.add( create2());

		insert_horizontal_glue( northPanel);

		northPanel.add( create3());

		insert_horizontal_glue( northPanel);

		northPanel.add( create4());

		add( northPanel, "North");


		adjust();


		return true;
	}

	/**
	 * @return
	 */
	private JPanel create1() {
		JPanel basicPanel = new JPanel();
		basicPanel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_panels[ 0] = new JPanel();
		_panels[ 0].setLayout( new BoxLayout( _panels[ 0], BoxLayout.Y_AXIS));

		insert_vertical_strut( _panels[ 0]);

		setup_startTimeComponents( _panels[ 0]);

		insert_vertical_strut( _panels[ 0]);

		setup_stepTimeComponents( _panels[ 0]);

		insert_vertical_strut( _panels[ 0]);

		setup_endTimeComponents( _panels[ 0]);

		insert_vertical_strut( _panels[ 0]);

		_panels[ 0].setBorder( BorderFactory.createLineBorder( getForeground(), 1));

		basicPanel.add( _panels[ 0]);
		
		return basicPanel;
	}

	/**
	 * @param parent
	 */
	private void setup_startTimeComponents(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_startTimeLabels[ 0] = new JLabel(
			ResourceManager.get_instance().get( "edit.simulation.dialog.start.time.name"));
		_startTimeLabels[ 0].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _startTimeLabels[ 0]);

		JTextField textField = new JTextField( new TextLimiter( "0123456789"),
			SimulationManager.get_instance()._startTime[ 0], 0);
		textField.setColumns( 5);
		textField.setHorizontalAlignment( SwingConstants.RIGHT);
		_textUndoRedoManagers .add( new TextUndoRedoManager( textField, this));
		panel.add( textField);
		_startTimeComponents[ 0] = textField;

		_startTimeLabels[ 1] = new JLabel( " / ");
		panel.add( _startTimeLabels[ 1]);

		JComboBox comboBox = new JComboBox( CommonTool.get_hours00());
		comboBox.setRenderer( new CommonComboBoxRenderer( null, true));
		comboBox.setSelectedItem( SimulationManager.get_instance()._startTime[ 1]);
		update_time_comboBox_width( comboBox);
		panel.add( comboBox);
		_startTimeComponents[ 1] = comboBox;

		_startTimeLabels[ 2] = new JLabel( " : ");
		panel.add( _startTimeLabels[ 2]);

		comboBox = new JComboBox( CommonTool.get_minutes00());
		comboBox.setRenderer( new CommonComboBoxRenderer( null, true));
		comboBox.setSelectedItem( SimulationManager.get_instance()._startTime[ 2]);
		update_time_comboBox_width( comboBox);
		panel.add( comboBox);
		_startTimeComponents[ 2] = comboBox;

		parent.add( panel);
	}

	/**
	 * @param parent
	 * 
	 */
	private void setup_stepTimeComponents(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_stepTimeLabels[ 0] = new JLabel(
			ResourceManager.get_instance().get( "edit.simulation.dialog.step.time.name"));
		_stepTimeLabels[ 0].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _stepTimeLabels[ 0]);

		JTextField textField = new JTextField( new TextLimiter( "0123456789"),
			SimulationManager.get_instance()._stepTime[ 0], 0);
		textField.setColumns( 5);
		textField.setHorizontalAlignment( SwingConstants.RIGHT);
		_textUndoRedoManagers .add( new TextUndoRedoManager( textField, this));
		panel.add( textField);
		_stepTimeComponents[ 0] = textField;

		_stepTimeLabels[ 1] = new JLabel( " / ");
		panel.add( _stepTimeLabels[ 1]);

		JComboBox comboBox = new JComboBox( CommonTool.get_hours00());
		comboBox.setRenderer( new CommonComboBoxRenderer( null, true));
		comboBox.setSelectedItem( SimulationManager.get_instance()._stepTime[ 1]);
		update_time_comboBox_width( comboBox);
		panel.add( comboBox);
		_stepTimeComponents[ 1] = comboBox;

		_stepTimeLabels[ 2] = new JLabel( " : ");
		panel.add( _stepTimeLabels[ 2]);

		comboBox = new JComboBox( CommonTool.get_minutes00());
		comboBox.setRenderer( new CommonComboBoxRenderer( null, true));
		comboBox.setSelectedItem( SimulationManager.get_instance()._stepTime[ 2]);
		update_time_comboBox_width( comboBox);
		panel.add( comboBox);
		_stepTimeComponents[ 2] = comboBox;

		parent.add( panel);
	}

	/**
	 * @param parent
	 * 
	 */
	private void setup_endTimeComponents(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_endtimeLabels[ 0] = new JLabel(
			ResourceManager.get_instance().get( "edit.simulation.dialog.end.time.name"));
		_endtimeLabels[ 0].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _endtimeLabels[ 0]);

		JTextField textField = new JTextField( new TextLimiter( "0123456789"),
			SimulationManager.get_instance()._endTime[ 0], 0);
		textField.setColumns( 5);
		textField.setHorizontalAlignment( SwingConstants.RIGHT);
		_textUndoRedoManagers .add( new TextUndoRedoManager( textField, this));
		panel.add( textField);
		_endTimeComponents[ 0] = textField;

		_endtimeLabels[ 1] = new JLabel( " / ");
		panel.add( _endtimeLabels[ 1]);

		JComboBox comboBox = new JComboBox( CommonTool.get_hours00());
		comboBox.setRenderer( new CommonComboBoxRenderer( null, true));
		comboBox.setSelectedItem( SimulationManager.get_instance()._endTime[ 1]);
		update_time_comboBox_width( comboBox);
		panel.add( comboBox);
		_endTimeComponents[ 1] = comboBox;

		_endtimeLabels[ 2] = new JLabel( " : ");
		panel.add( _endtimeLabels[ 2]);

		comboBox = new JComboBox( CommonTool.get_minutes00());
		comboBox.setRenderer( new CommonComboBoxRenderer( null, true));
		comboBox.setSelectedItem( SimulationManager.get_instance()._endTime[ 2]);
		update_time_comboBox_width( comboBox);
		panel.add( comboBox);
		_endTimeComponents[ 2] = comboBox;

		parent.add( panel);
	}

	/**
	 * @return
	 */
	private JPanel create2() {
		JPanel basicPanel = new JPanel();
		basicPanel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_panels[ 1] = new JPanel();
		_panels[ 1].setLayout( new BoxLayout( _panels[ 1], BoxLayout.Y_AXIS));

		insert_vertical_strut( _panels[ 1]);

		setup_logStepTimeComponents( _panels[ 1]);

		insert_vertical_strut( _panels[ 1]);

		_setLogStepTimeCheckBox = new JCheckBox(
			ResourceManager.get_instance().get( "edit.simulation.dialog.set.log.step.time"));
//		_set_log_step_time_checkBox.setSelected( SimulationManager.get_instance()._export_log_step_time);
		_setLogStepTimeCheckBox.setSelected( !SimulationManager.get_instance().log_step_time_equals_step_time());
		_setLogStepTimeCheckBox.addChangeListener( new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				on_changed();
			}
		});
		_panels[ 1].setBorder( new ComponentTitledBorder( _setLogStepTimeCheckBox,
			_panels[ 1], BorderFactory.createLineBorder( getForeground())));

		basicPanel.add( _panels[ 1]);

		return basicPanel;
	}

	/**
	 * 
	 */
	private void on_changed() {
		for ( int i = 0; i < _logStepTimeLabels.length; ++i)
			_logStepTimeLabels[ i].setEnabled( _setLogStepTimeCheckBox.isSelected());
		for ( int i = 0; i < _logStepTimeComponents.length; ++i)
			_logStepTimeComponents[ i].setEnabled( _setLogStepTimeCheckBox.isSelected());
	}

	/**
	 * @param parent
	 * 
	 */
	private void setup_logStepTimeComponents(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_logStepTimeLabels[ 0] = new JLabel(
			ResourceManager.get_instance().get( "edit.simulation.dialog.log.step.time.name"));
		_logStepTimeLabels[ 0].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _logStepTimeLabels[ 0]);

		JTextField textField = new JTextField( new TextLimiter( "0123456789"),
			SimulationManager.get_instance()._logStepTime[ 0], 0);
		textField.setColumns( 5);
		textField.setHorizontalAlignment( SwingConstants.RIGHT);
		_textUndoRedoManagers .add( new TextUndoRedoManager( textField, this));
		panel.add( textField);
		_logStepTimeComponents[ 0] = textField;

		_logStepTimeLabels[ 1] = new JLabel( " / ");
		panel.add( _logStepTimeLabels[ 1]);

		JComboBox comboBox = new JComboBox( CommonTool.get_hours00());
		comboBox.setRenderer( new CommonComboBoxRenderer( null, true));
		comboBox.setSelectedItem( SimulationManager.get_instance()._logStepTime[ 1]);
		update_time_comboBox_width( comboBox);
		panel.add( comboBox);
		_logStepTimeComponents[ 1] = comboBox;

		_logStepTimeLabels[ 2] = new JLabel( " : ");
		panel.add( _logStepTimeLabels[ 2]);

		comboBox = new JComboBox( CommonTool.get_minutes00());
		comboBox.setRenderer( new CommonComboBoxRenderer( null, true));
		comboBox.setSelectedItem( SimulationManager.get_instance()._logStepTime[ 2]);
		update_time_comboBox_width( comboBox);
		panel.add( comboBox);
		_logStepTimeComponents[ 2] = comboBox;

		parent.add( panel);
	}

	/**
	 * @return
	 */
	private JPanel create3() {
		JPanel basicPanel = new JPanel();
		basicPanel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_panels[ 2] = new JPanel();
		_panels[ 2].setLayout( new BoxLayout( _panels[ 2], BoxLayout.Y_AXIS));

		insert_vertical_strut( _panels[ 2]);

		setup_exportEndTimeCheckBox( _panels[ 2]);

		insert_vertical_strut( _panels[ 2]);

		_panels[ 2].setBorder( BorderFactory.createLineBorder( getForeground(), 1));

		basicPanel.add( _panels[ 2]);

		return basicPanel;
	}

	/**
	 * @param parent
	 */
	private void setup_exportEndTimeCheckBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_exportEndTimeCheckBox = new JCheckBox(
			ResourceManager.get_instance().get( "edit.simulation.dialog.export.end.time"));
		_exportEndTimeCheckBox.setSelected( SimulationManager.get_instance()._exportEndTime);

		panel.add( _exportEndTimeCheckBox);
		parent.add( panel);
	}

	/**
	 * @return
	 */
	private JPanel create4() {
		JPanel basicPanel = new JPanel();
		basicPanel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_panels[ 3] = new JPanel();
		_panels[ 3].setLayout( new BoxLayout( _panels[ 3], BoxLayout.Y_AXIS));

		insert_vertical_strut( _panels[ 3]);

		setup_randomSeedTextField( _panels[ 3]);

		insert_vertical_strut( _panels[ 3]);

		_panels[ 3].setBorder( BorderFactory.createLineBorder( getForeground(), 1));

		basicPanel.add( _panels[ 3]);

		return basicPanel;
	}

	/**
	 * @param parent
	 * 
	 */
	private void setup_randomSeedTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_randomSeedLabel = new JLabel(
			ResourceManager.get_instance().get( "edit.simulation.dialog.random.seed"));
		_randomSeedLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _randomSeedLabel);

		_randomSeedTextField  = new JTextField( new TextExcluder( Constant._prohibitedCharacters1), SimulationManager.get_instance()._randomSeed, 0);
		_randomSeedTextField.setHorizontalAlignment( SwingConstants.RIGHT);
		//_randomSeedTextField.setPreferredSize( new Dimension( 100, _random_seed_textField.getPreferredSize().height));
		_textUndoRedoManagers .add( new TextUndoRedoManager( _randomSeedTextField, this));

		panel.add( _randomSeedTextField);
		parent.add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = _startTimeLabels[ 0].getPreferredSize().width;
		width = Math.max( width, _stepTimeLabels[ 0].getPreferredSize().width);
		width = Math.max( width, _endtimeLabels[ 0].getPreferredSize().width);
		width = Math.max( width, _logStepTimeLabels[ 0].getPreferredSize().width);
		width = Math.max( width, _randomSeedLabel.getPreferredSize().width);

		Dimension dimension = new Dimension( width,
			_startTimeLabels[ 0].getPreferredSize().height);
		_startTimeLabels[ 0].setPreferredSize( dimension);
		_stepTimeLabels[ 0].setPreferredSize( dimension);
		_endtimeLabels[ 0].setPreferredSize( dimension);
		_logStepTimeLabels[ 0].setPreferredSize( dimension);
		_randomSeedLabel.setPreferredSize( dimension);

		width = 0;
		for ( int i = 1; i < _logStepTimeLabels.length; ++i)
			width += _startTimeLabels[ i].getPreferredSize().width;
		for ( int i = 0; i < _logStepTimeComponents.length; ++i)
			width += _startTimeComponents[ i].getPreferredSize().width;
		_randomSeedTextField.setPreferredSize( new Dimension( width + 20, _randomSeedTextField.getPreferredSize().height));

		width = 0;
		for ( int i = 0; i < _panels.length; ++i)
			width = Math.max( width, _panels[ i].getPreferredSize().width);
		for ( int i = 0; i < _panels.length; ++i)
			_panels[ i].setPreferredSize( new Dimension( width, _panels[ i].getPreferredSize().height));
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		on_changed();
		_startTimeComponents[ 0].requestFocusInWindow();
	}

	/**
	 * @return
	 */
	public boolean on_ok() {
		if ( !_randomSeedTextField.getText().equals( "")
			&& !SimulationManager.get_instance().is_correct_random_seed( _randomSeedTextField.getText()))
			return false;

		SimulationManager.get_instance()._randomSeed = _randomSeedTextField.getText();

		SimulationManager.get_instance()._startTime[ 0]
			= get_day( ( ( JTextField)_startTimeComponents[ 0]).getText());

		SimulationManager.get_instance()._stepTime[ 0]
			= get_day( ( ( JTextField)_stepTimeComponents[ 0]).getText());

		SimulationManager.get_instance()._endTime[ 0]
			= get_day( ( ( JTextField)_endTimeComponents[ 0]).getText());

//		SimulationManager.get_instance()._log_step_time[ 0]
//			= get_day( ( ( JTextField)_log_step_time_components[ 0]).getText());

		for ( int i = 1; i < 3; ++i) {
			JComboBox comboBox = ( JComboBox)_startTimeComponents[ i];
			SimulationManager.get_instance()._startTime[ i] = ( String)comboBox.getSelectedItem();

			comboBox = ( JComboBox)_stepTimeComponents[ i];
			SimulationManager.get_instance()._stepTime[ i] = ( String)comboBox.getSelectedItem();

			comboBox = ( JComboBox)_endTimeComponents[ i];
			SimulationManager.get_instance()._endTime[ i] = ( String)comboBox.getSelectedItem();

//			comboBox = ( JComboBox)_log_step_time_components[ i];
//			SimulationManager.get_instance()._log_step_time[ i] = ( String)comboBox.getSelectedItem();
		}

		SimulationManager.get_instance()._exportEndTime = _exportEndTimeCheckBox.isSelected();

		if ( !_setLogStepTimeCheckBox.isSelected())
			SimulationManager.get_instance().substitute_step_time_into_log_step_time();
		else {
			SimulationManager.get_instance()._logStepTime[ 0]
				= get_day( ( ( JTextField)_logStepTimeComponents[ 0]).getText());
			for ( int i = 1; i < 3; ++i) {
				JComboBox comboBox = ( JComboBox)_logStepTimeComponents[ i];
				SimulationManager.get_instance()._logStepTime[ i] = ( String)comboBox.getSelectedItem();
			}
		}
//		SimulationManager.get_instance()._export_log_step_time = _set_log_step_time_checkBox.isSelected();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.undo.UndoManager)
	 */
	public void on_changed(UndoManager undoManager) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.text.AbstractDocument.DefaultDocumentEvent, javax.swing.undo.UndoManager)
	 */
	public void on_changed(DefaultDocumentEvent defaultDocumentEvent,UndoManager undoManager) {
	}
}
