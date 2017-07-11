/*
 * 2005/03/28
 */
package soars.application.animator.object.scenario.retrieve;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import soars.application.animator.main.ResourceManager;
import soars.application.animator.object.entity.base.EntityBase;
import soars.application.animator.object.property.base.PropertyManager;
import soars.application.animator.object.scenario.ScenarioManager;
import soars.application.animator.object.transition.base.TransitionManager;
import soars.common.utility.swing.window.Dialog;

/**
 * The dialog box to retrieve the value of the property.
 * @author kurata / SOARS project
 */
public class RetrievePropertyDlg extends Dialog {

	/**
	 * Object(AgentObject or SpotObject).
	 */
	public EntityBase _entityBase = null;

	/**
	 * Name of the property.
	 */
	public String _propertyName = "";

	/**
	 * Value of the property.
	 */
	public String _propertyValue = "";

	/**
	 * 
	 */
	private EntityBase[] _visibleObjects = null;

	/**
	 * 
	 */
	public String _directory = "";

	/**
	 * 
	 */
	private PropertyManager _propertyManager = null;

	/**
	 * 
	 */
	private TransitionManager _transitionManager = null;

	/**
	 * 
	 */
	private ScenarioManager _scenarioManager = null;

	/**
	 * 
	 */
	private JComboBox _objectComboBox = null;

	/**
	 * 
	 */
	private JComboBox _propertyNameComboBox = null;

	/**
	 * 
	 */
	private JComboBox _retrieveTypeComboBox = null;

	/**
	 * 
	 */
	private JComboBox[] _propertyValueComboBoxes = new JComboBox[ 2];

	/**
	 * 
	 */
	private JButton _backwardButton = null;

	/**
	 * 
	 */
	private JButton _forwardButton = null;

	/**
	 * 
	 */
	private String _currentPropertyName = "";

	/**
	 * 
	 */
	private int _minimumWidth;

	/**
	 * 
	 */
	private int _minimumHeight;

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog. 
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param visibleObjects the array of the visible objects
	 * @param directory
	 * @param propertyManager the Property hashtable(name(String) - value(String) - PropertyBase)
	 * @param transitionManager the scenario data manager
	 * @param scenarioManager
	 */
	public RetrievePropertyDlg(Frame arg0, String arg1, boolean arg2, EntityBase[] visibleObjects, String directory, PropertyManager propertyManager, TransitionManager transitionManager, ScenarioManager scenarioManager) {
		super(arg0, arg1, arg2);
		_visibleObjects = visibleObjects;
		_directory = directory;
		_propertyManager = propertyManager;
		_transitionManager = transitionManager;
		_scenarioManager = scenarioManager;
	}

	/**
	 * Sets whether or not this dialog is enabled.
	 * @param enable true if this dialog should be enabled, false otherwise
	 */
	public void enable_user_interface(boolean enable) {
		_objectComboBox.setEnabled( enable);
		_propertyNameComboBox.setEnabled( enable);

		_propertyValueComboBoxes[ 0].setEnabled( enable);

		if ( enable)
			enable_property_value_comboBox();

		_backwardButton.setEnabled( enable);
		_forwardButton.setEnabled( enable);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;

		//setResizable( false);


		getContentPane().setLayout( new BorderLayout());


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_northPanel( northPanel);

		getContentPane().add( northPanel, "North");


		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( southPanel);

		setup_buttons( southPanel);

		insert_horizontal_glue( southPanel);

		getContentPane().add( southPanel, "South");


		adjust();


		pack();


		_minimumWidth = getPreferredSize().width;
		_minimumHeight = getPreferredSize().height;


		int width = getSize().width;
		setSize( ( _minimumWidth > width) ? _minimumWidth : width, _minimumHeight);


		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				int width = getSize().width;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width, _minimumHeight);
			}
		});


		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_northPanel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		setup_objectComboBox( panel);

		panel.add( Box.createHorizontalStrut( 5));

		setup_propertyNameComboBox( panel);

		panel.add( Box.createHorizontalStrut( 5));

		_currentPropertyName = ( String)_propertyManager.get_selected_properties().get( 0);

		setup_retrieveTypeComboBox( panel);

		panel.add( Box.createHorizontalStrut( 5));

		setup_propertyValueComboBoxes( panel);

		parent.add( panel);
	}

	/**
	 * @param parent 
	 */
	private void setup_objectComboBox(JPanel parent) {
		_objectComboBox = new JComboBox( _visibleObjects);
		_objectComboBox.setRenderer( new EntityNameCellRenderer());
		parent.add( _objectComboBox);
	}

	/**
	 * @param parent
	 */
	private void setup_propertyNameComboBox(JPanel parent) {
		_propertyNameComboBox = new JComboBox( _propertyManager.get_selected_properties());
		_propertyNameComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_property_name_changed( arg0);
			}
		});
		parent.add( _propertyNameComboBox);
	}

	/**
	 * @param parent
	 */
	private void setup_retrieveTypeComboBox(JPanel parent) {
		_retrieveTypeComboBox = new JComboBox();
		_retrieveTypeComboBox.addItem(
			ResourceManager.get_instance().get( "retrieve.property.dialog.equal"));
		_retrieveTypeComboBox.addItem(
			ResourceManager.get_instance().get( "retrieve.property.dialog.more.than"));
		_retrieveTypeComboBox.addItem(
			ResourceManager.get_instance().get( "retrieve.property.dialog.less.than"));
		_retrieveTypeComboBox.addItem(
			ResourceManager.get_instance().get( "retrieve.property.dialog.more.than.less.than"));
		_retrieveTypeComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_retrieve_type_changed( arg0);
			}
		});
		parent.add( _retrieveTypeComboBox);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_retrieve_type_changed(ActionEvent actionEvent) {
		enable_property_value_comboBox();
	}

	/**
	 * 
	 */
	private void enable_property_value_comboBox() {
		String retrieveType = ( String)_retrieveTypeComboBox.getSelectedItem();
		_propertyValueComboBoxes[ 1].setEnabled(
			retrieveType.equals( ResourceManager.get_instance().get(
				"retrieve.property.dialog.more.than.less.than")) ? true : false);
	}

	/**
	 * @param parent
	 */
	private void setup_propertyValueComboBoxes(JPanel parent) {
		for ( int i = 0; i < _propertyValueComboBoxes.length; ++i) {
			_propertyValueComboBoxes[ i] = new JComboBox();
			update_property_value_comboBox( _propertyValueComboBoxes[ i], _currentPropertyName);

			parent.add( _propertyValueComboBoxes[ i]);

			parent.add( Box.createHorizontalStrut( 5));
		}
		_propertyValueComboBoxes[ 1].setEnabled( false);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_property_name_changed(ActionEvent actionEvent) {
		String propertyName = ( String)_propertyNameComboBox.getSelectedItem();

		if ( propertyName.equals( _currentPropertyName))
			return;

		for ( int i = 0; i < _propertyValueComboBoxes.length; ++i)
			update_property_value_comboBox( _propertyValueComboBoxes[ i], propertyName);

		_currentPropertyName = propertyName;
	}

	/**
	 * @param propertyValueComboBox
	 * @param propertyName
	 */
	private void update_property_value_comboBox(JComboBox propertyValueComboBox, String propertyName) {
		propertyValueComboBox.removeAllItems();

		TreeMap propertyMap = _propertyManager.get( propertyName);
		Iterator iterator = propertyMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String value = ( String)entry.getKey();
			propertyValueComboBox.addItem( value);
		}
	}

	/**
	 * @param parent
	 */
	private void setup_buttons(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		setup_backwardButton( panel);

		setup_forwardButton( panel);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_backwardButton(JPanel parent) {
		_backwardButton = new JButton(
			ResourceManager.get_instance().get( "retrieve.property.dialog.backward.button"));
		_backwardButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_backward( arg0);
			}
		});
		parent.add( _backwardButton);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_backward(ActionEvent actionEvent) {
		String retrieveType = ( String)_retrieveTypeComboBox.getSelectedItem();
		if ( retrieveType.equals( ResourceManager.get_instance().get( "retrieve.property.dialog.equal")))
			_scenarioManager.retrieve_backward(
				_transitionManager,
				( EntityBase)_objectComboBox.getSelectedItem(),
				_directory,
				( String)_propertyNameComboBox.getSelectedItem(),
				( String)_propertyValueComboBoxes[ 0].getSelectedItem());
		else {
			String text0 = ( String)_propertyValueComboBoxes[ 0].getSelectedItem();
			double value0;
			try {
				value0 = Double.parseDouble( text0);
			} catch (NumberFormatException e) {
				return;
			}
			if ( retrieveType.equals( ResourceManager.get_instance().get( "retrieve.property.dialog.more.than")))
				_scenarioManager.retrieve_backward_more_than(
					_transitionManager,
					( EntityBase)_objectComboBox.getSelectedItem(),
					_directory,
					( String)_propertyNameComboBox.getSelectedItem(),
					value0);
			else if ( retrieveType.equals( ResourceManager.get_instance().get( "retrieve.property.dialog.less.than")))
				_scenarioManager.retrieve_backward_less_than(
					_transitionManager,
					( EntityBase)_objectComboBox.getSelectedItem(),
					_directory,
					( String)_propertyNameComboBox.getSelectedItem(),
					value0);
			else if ( retrieveType.equals( ResourceManager.get_instance().get( "retrieve.property.dialog.more.than.less.than"))) {
				String text1 = ( String)_propertyValueComboBoxes[ 1].getSelectedItem();
				double value1;
				try {
					value1 = Double.parseDouble( text1);
				} catch (NumberFormatException e) {
					return;
				}
				_scenarioManager.retrieve_backward_more_than_less_than(
					_transitionManager,
					( EntityBase)_objectComboBox.getSelectedItem(),
					_directory,
					( String)_propertyNameComboBox.getSelectedItem(),
					value0,
					value1);
			}
		}
	}

	/**
	 * @param parent
	 */
	private void setup_forwardButton(JPanel parent) {
		_forwardButton = new JButton(
			ResourceManager.get_instance().get( "retrieve.property.dialog.forward.button"));
		_forwardButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_forward( arg0);
			}
		});
		parent.add( _forwardButton);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_forward(ActionEvent actionEvent) {
		String retrieveType = ( String)_retrieveTypeComboBox.getSelectedItem();
		if ( retrieveType.equals( ResourceManager.get_instance().get( "retrieve.property.dialog.equal")))
			_scenarioManager.retrieve_forward(
				_transitionManager,
				( EntityBase)_objectComboBox.getSelectedItem(),
				_directory,
				( String)_propertyNameComboBox.getSelectedItem(),
				( String)_propertyValueComboBoxes[ 0].getSelectedItem());
		else {
			String text0 = ( String)_propertyValueComboBoxes[ 0].getSelectedItem();
			double value0;
			try {
				value0 = Double.parseDouble( text0);
			} catch (NumberFormatException e) {
				return;
			}
			if ( retrieveType.equals( ResourceManager.get_instance().get( "retrieve.property.dialog.more.than")))
				_scenarioManager.retrieve_forward_more_than(
					_transitionManager,
					( EntityBase)_objectComboBox.getSelectedItem(),
					_directory,
					( String)_propertyNameComboBox.getSelectedItem(),
					value0);
			else if ( retrieveType.equals( ResourceManager.get_instance().get( "retrieve.property.dialog.less.than")))
				_scenarioManager.retrieve_forward_less_than(
					_transitionManager,
					( EntityBase)_objectComboBox.getSelectedItem(),
					_directory,
					( String)_propertyNameComboBox.getSelectedItem(),
					value0);
			else if ( retrieveType.equals( ResourceManager.get_instance().get( "retrieve.property.dialog.more.than.less.than"))) {
				String text1 = ( String)_propertyValueComboBoxes[ 1].getSelectedItem();
				double value1;
				try {
					value1 = Double.parseDouble( text1);
				} catch (NumberFormatException e) {
					return;
				}
				_scenarioManager.retrieve_forward_more_than_less_than(
					_transitionManager,
					( EntityBase)_objectComboBox.getSelectedItem(),
					_directory,
					( String)_propertyNameComboBox.getSelectedItem(),
					value0,
					value1);
			}
		}
	}

	/**
	 * 
	 */
	private void adjust() {
		_objectComboBox.setPreferredSize(
			new Dimension( _objectComboBox.getPreferredSize().width + 15,
				_objectComboBox.getPreferredSize().height));
		_propertyNameComboBox.setPreferredSize(
			new Dimension( _propertyNameComboBox.getPreferredSize().width + 15,
				_propertyNameComboBox.getPreferredSize().height));
		_retrieveTypeComboBox.setPreferredSize(
			new Dimension( _retrieveTypeComboBox.getPreferredSize().width + 15,
				_retrieveTypeComboBox.getPreferredSize().height));
		for ( int i = 0; i < _propertyValueComboBoxes.length; ++i)
			_propertyValueComboBoxes[ i].setPreferredSize(
				new Dimension( _propertyValueComboBoxes[ i].getPreferredSize().width + 15,
					_propertyValueComboBoxes[ i].getPreferredSize().height));

		int width = _backwardButton.getPreferredSize().width;
		width = Math.max( width, _forwardButton.getPreferredSize().width);

		Dimension dimension = new Dimension( width,
			_backwardButton.getPreferredSize().height);
		_backwardButton.setPreferredSize( dimension);
		_forwardButton.setPreferredSize( dimension);
	}
}
