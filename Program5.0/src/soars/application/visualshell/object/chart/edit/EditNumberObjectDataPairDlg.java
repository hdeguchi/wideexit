/*
 * Created on 2006/03/13
 */
package soars.application.visualshell.object.chart.edit;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import soars.application.visualshell.common.selector.IObjectSelectorHandler;
import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.chart.NumberObjectData;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Dialog;

/**
 * The dialog box to edit the numeric data of the chart object.
 * @author kurata / SOARS project
 */
public class EditNumberObjectDataPairDlg extends Dialog implements IObjectSelectorHandler {

	/**
	 * 
	 */
	private int _minimumWidth = -1;

	/**
	 * 
	 */
	private int _minimumHeight = -1;

	/**
	 * 
	 */
	protected NumberObjectData[] _numberObjectDataPair = null;

	/**
	 * 
	 */
	private NumberObjectDataPairTable _numberObjectDataPairTable = null;

	/**
	 * 
	 */
	private JLabel[] _labels = new JLabel[] { null, null};

	/**
	 * 
	 */
	private JLabel[][] _dummies = new JLabel[][] {
		{ null, null},
		{ null, null}
	};

	/**
	 * 
	 */
	private JRadioButton[][] _radioButtons1 = new JRadioButton[][] {
		{ null, null, null},
		{ null, null, null}
	};

	/**
	 * 
	 */
	private ObjectSelector[] _agentSelector = new ObjectSelector[] {
		null, null
	};

	/**
	 * 
	 */
	private ObjectSelector[] _spotSelector = new ObjectSelector[] {
		null, null
	};

	/**
	 * 
	 */
	private JComboBox[] _agentNumberObjectComboBox = new JComboBox[] {
		null, null
	};

	/**
	 * 
	 */
	private JComboBox[] _spotNumberObjectComboBox = new JComboBox[] {
		null, null
	};

	/**
	 * 
	 */
	private Map _numberObjectComboBoxMap = new HashMap();

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog. 
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param numberObjectDataPairTable the table component for the numeric data of the chart object
	 * @throws HeadlessException thrown when code that is dependent on a keyboard, display, or mouse is called in an environment that does not support a keyboard, display, or mouse.
	 */
	public EditNumberObjectDataPairDlg(Frame arg0, String arg1, boolean arg2, NumberObjectDataPairTable numberObjectDataPairTable)
		throws HeadlessException {
		super(arg0, arg1, arg2);
		_numberObjectDataPairTable = numberObjectDataPairTable;
	}

	/**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame. If owner is null, a shared, hidden frame will be set as the owner of the dialog. 
	 * @param arg0 the Frame from which the dialog is displayed
	 * @param arg1 the String to display in the dialog's title bar
	 * @param arg2 true for a modal dialog, false for one that allows other windows to be active at the same time
	 * @param horizontalNumberObjectData the numeric data for the horizontal axis
	 * @param verticalNumberObjectData the numeric data for the vertical axis
	 * @param numberObjectDataPairTable the table component for the numeric data of the chart object
	 * @throws HeadlessException thrown when code that is dependent on a keyboard, display, or mouse is called in an environment that does not support a keyboard, display, or mouse.
	 */
	public EditNumberObjectDataPairDlg(Frame arg0, String arg1, boolean arg2, NumberObjectData horizontalNumberObjectData, NumberObjectData verticalNumberObjectData, NumberObjectDataPairTable numberObjectDataPairTable)
		throws HeadlessException {
		super(arg0, arg1, arg2);
		_numberObjectDataPair = new NumberObjectData[] { horizontalNumberObjectData, verticalNumberObjectData};
		_numberObjectDataPairTable = numberObjectDataPairTable;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		link_to_cancel( getRootPane());


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		insert_horizontal_glue();

		setup( ResourceManager.get_instance().get( "edit.number.object.pair.dialog.horizontal.axis"), 0);

		insert_horizontal_glue();

		setup( ResourceManager.get_instance().get( "edit.number.object.pair.dialog.vertical.axis"), 1);

		insert_horizontal_glue();

		setup_ok_and_cancel_button(
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			true, true);

		insert_horizontal_glue();


		adjust();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		return true;
	}

	/**
	 * @param title
	 * @param index
	 */
	private void setup(String title, int index) {
		ButtonGroup buttonGroup = new ButtonGroup();

		setup_agent_section( title, index, buttonGroup);

		insert_horizontal_glue( getContentPane());

		setup_spot_section( index,buttonGroup);

		insert_horizontal_glue( getContentPane());

		setup_step_section( index,buttonGroup);
	}

	/**
	 * @param title
	 * @param index
	 * @param buttonGroup
	 */
	private void setup_agent_section(String title, final int index, ButtonGroup buttonGroup) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_labels[ index] = new JLabel( title);
		panel.add( _labels[ index]);

		panel.add( Box.createHorizontalStrut( 5));

		_radioButtons1[ index][ 0] = new JRadioButton( ResourceManager.get_instance().get( "edit.number.object.pair.dialog.radio.button.agent"));
		buttonGroup.add( _radioButtons1[ index][ 0]);
		_radioButtons1[ index][ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_agentSelector[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				on_update( _agentSelector[ index],
					ItemEvent.SELECTED == arg0.getStateChange());
				_agentNumberObjectComboBox[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});

		link_to_cancel( _radioButtons1[ index][ 0]);

		panel.add( _radioButtons1[ index][ 0]);

		panel.add( Box.createHorizontalStrut( 5));

		_agentSelector[ index] = new ObjectSelector( "agent", false, 120, 80, null, true/*, this*/, null);
		//_agent_selector[ index] = new ObjectSelector( "agent", LayerManager.get_instance().get_agent_names( false), 120, 80, null, true/*, this*/, null);
		panel.add( _agentSelector[ index]);

		link_to_cancel( _agentSelector[ index]._objectNameComboBox);
		link_to_cancel( _agentSelector[ index]._numberSpinner);

		panel.add( Box.createHorizontalStrut( 5));

		_agentNumberObjectComboBox[ index] = new JComboBox();
		_agentNumberObjectComboBox[ index].setPreferredSize( new Dimension( 200,
			_agentNumberObjectComboBox[ index].getPreferredSize().height));
		panel.add( _agentNumberObjectComboBox[ index]);

		link_to_cancel( _agentNumberObjectComboBox[ index]);

		panel.add( Box.createHorizontalStrut( 5));

		_numberObjectComboBoxMap.put( _agentSelector[ index], _agentNumberObjectComboBox[ index]);

		getContentPane().add( panel);
	}

	/**
	 * @param index
	 * @param buttonGroup
	 */
	private void setup_spot_section(final int index, ButtonGroup buttonGroup) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_dummies[ index][ 0] = new JLabel();
		panel.add( _dummies[ index][ 0]);

		panel.add( Box.createHorizontalStrut( 5));

		_radioButtons1[ index][ 1] = new JRadioButton( ResourceManager.get_instance().get( "edit.number.object.pair.dialog.radio.button.spot"));
		buttonGroup.add( _radioButtons1[ index][ 1]);
		_radioButtons1[ index][ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_spotSelector[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				on_update( _spotSelector[ index],
					ItemEvent.SELECTED == arg0.getStateChange());
				_spotNumberObjectComboBox[ index].setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});

		link_to_cancel( _radioButtons1[ index][ 1]);

		panel.add( _radioButtons1[ index][ 1]);

		panel.add( Box.createHorizontalStrut( 5));

		_spotSelector[ index] = new ObjectSelector( "spot", false, 120, 80, null, true/*, this*/, null);
		//_spot_selector[ index] = new ObjectSelector( "spot", LayerManager.get_instance().get_spot_names( false), 120, 80, null, true/*, this*/, null);
		panel.add( _spotSelector[ index]);

		link_to_cancel( _spotSelector[ index]._objectNameComboBox);
		link_to_cancel( _spotSelector[ index]._numberSpinner);

		panel.add( Box.createHorizontalStrut( 5));

		_spotNumberObjectComboBox[ index] = new JComboBox();
		_spotNumberObjectComboBox[ index].setPreferredSize( new Dimension( 200,
			_spotNumberObjectComboBox[ index].getPreferredSize().height));
		panel.add( _spotNumberObjectComboBox[ index]);

		link_to_cancel( _spotNumberObjectComboBox[ index]);

		panel.add( Box.createHorizontalStrut( 5));

		_numberObjectComboBoxMap.put( _spotSelector[ index], _spotNumberObjectComboBox[ index]);

		getContentPane().add( panel);
	}

	/**
	 * @param index
	 * @param buttonGroup
	 */
	private void setup_step_section(final int index, ButtonGroup buttonGroup) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_dummies[ index][ 1] = new JLabel();
		panel.add( _dummies[ index][ 1]);

		panel.add( Box.createHorizontalStrut( 5));

		JPanel partialPanel = new JPanel();
		partialPanel.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0));
		_radioButtons1[ index][ 2] = new JRadioButton( ResourceManager.get_instance().get( "edit.number.object.pair.dialog.radio.button.step"));
		buttonGroup.add( _radioButtons1[ index][ 2]);
		_radioButtons1[ index][ 2].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
			}
		});

		link_to_cancel( _radioButtons1[ index][ 2]);

		partialPanel.add( _radioButtons1[ index][ 2]);

		panel.add( partialPanel);

		panel.add( Box.createHorizontalStrut( 5));

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

		for ( int i = 0; i < _dummies.length; ++i)
			for ( int j = 0; j < _dummies[ i].length; ++j)
				_dummies[ i][ j].setPreferredSize( new Dimension( width,
						_dummies[ i][ j].getPreferredSize().height));


		width = 0;

		for ( int i = 0; i < _radioButtons1.length; ++i)
			for ( int j = 0; j < _radioButtons1[ i].length; ++j)
				width = Math.max( width, _radioButtons1[ i][ j].getPreferredSize().width);

		for ( int i = 0; i < _radioButtons1.length; ++i)
			for ( int j = 0; j < _radioButtons1[ i].length; ++j)
				_radioButtons1[ i][ j].setPreferredSize( new Dimension( width,
					_radioButtons1[ i][ j].getPreferredSize().height));
	}

	/**
	 * @param objectSelector
	 * @param enable
	 */
	protected void on_update(ObjectSelector objectSelector, boolean enable) {
		if ( !enable)
			return;

		update( objectSelector);
	}

	/**
	 * @param objectSelector
	 */
	private void update(ObjectSelector objectSelector) {
		EntityBase entityBase = null;
		String full_name = objectSelector.get();
		if ( objectSelector.equals( _agentSelector[ 0]) || objectSelector.equals( _agentSelector[ 1]))
			entityBase = LayerManager.get_instance().get_agent_has_this_name( full_name);
		else if ( objectSelector.equals( _spotSelector[ 0]) || objectSelector.equals( _spotSelector[ 1]))
			entityBase = LayerManager.get_instance().get_spot_has_this_name( full_name);
		else
			return;

		JComboBox comboBox = ( JComboBox)_numberObjectComboBoxMap.get( objectSelector);
		if ( null == comboBox)
			return;

		if ( null == entityBase || null == full_name || 0 == full_name.length()) {
			comboBox.removeAllItems();
			return;
		}

		CommonTool.update( comboBox, entityBase.get_object_names( "number object", full_name.substring( entityBase._name.length()), false));
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.selector.IObjectSelectorHandler#changed(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector)
	 */
	public void changed(String name, String number, String fullName, ObjectSelector objectSelector) {
		if ( null == fullName || 0 == fullName.length())
			return;

		EntityBase entityBase = null;
		if ( objectSelector.equals( _agentSelector[ 0]) || objectSelector.equals( _agentSelector[ 1]))
			entityBase = LayerManager.get_instance().get_agent_has_this_name( fullName);
		else if ( objectSelector.equals( _spotSelector[ 0]) || objectSelector.equals( _spotSelector[ 1]))
			entityBase = LayerManager.get_instance().get_spot_has_this_name( fullName);
		else
			return;

		JComboBox comboBox = ( JComboBox)_numberObjectComboBoxMap.get( objectSelector);
		if ( null == comboBox)
			return;

		if ( null == entityBase) {
			comboBox.removeAllItems();
			return;
		}

		CommonTool.update( comboBox, entityBase.get_object_names( "number object", number, false));
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		_minimumWidth = getPreferredSize().width;
		_minimumHeight = getPreferredSize().height;

		for ( int i = 0; i < _agentSelector.length; ++i) {
			_agentSelector[ i].set_handler( this);
			_agentSelector[ i].setEnabled( true);
			_agentSelector[ i].selectFirstItem();
			update( _agentSelector[ i]);
		}

		for ( int i = 0; i < _spotSelector.length; ++i) {
			_spotSelector[ i].set_handler( this);
			_spotSelector[ i].setEnabled( true);
			_spotSelector[ i].selectFirstItem();
			update( _spotSelector[ i]);
		}

		if ( null == _numberObjectDataPair) {
			for ( int i = 0; i < _radioButtons1.length; ++i) {
				_radioButtons1[ i][ 0].setSelected( true);
				update_components( i, new boolean[] { true, false});
			}
		} else {
			for ( int i = 0; i < _numberObjectDataPair.length; ++i)
				on_setup_completed( i);
		}
	}

	/**
	 * @param index
	 */
	private void on_setup_completed(int index) {
		if ( _numberObjectDataPair[ index]._type.equals( "agent")) {
			_radioButtons1[ index][ 0].setSelected( true);
			update_components( index, new boolean[] { true, false});
			_agentSelector[ index].set( _numberObjectDataPair[ index]._objectName);
			_agentNumberObjectComboBox[ index].setSelectedItem( _numberObjectDataPair[ index]._numberVariable);
		} else if ( _numberObjectDataPair[ index]._type.equals( "spot")) {
			_radioButtons1[ index][ 1].setSelected( true);
			update_components( index, new boolean[] { false, true});
			_spotSelector[ index].set( _numberObjectDataPair[ index]._objectName);
			_spotNumberObjectComboBox[ index].setSelectedItem( _numberObjectDataPair[ index]._numberVariable);
		} else if ( _numberObjectDataPair[ index]._type.equals( "step")) {
			_radioButtons1[ index][ 2].setSelected( true);
			update_components( index, new boolean[] { false, false});
		}

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				if ( 0 > _minimumWidth || 0 > _minimumHeight)
					return;

				int width = getSize().width;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width, _minimumHeight);
			}
		});
	}

	/**
	 * @param index
	 * @param enables
	 */
	private void update_components(int index, boolean[] enables) {
		_agentSelector[ index].setEnabled( enables[ 0]);
		_agentNumberObjectComboBox[ index].setEnabled( enables[ 0]);

		_spotSelector[ index].setEnabled( enables[ 1]);
		_spotNumberObjectComboBox[ index].setEnabled( enables[ 1]);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		NumberObjectData[] numberObjectDataPair = new NumberObjectData[] {
			new NumberObjectData(), new NumberObjectData()
		};

		for ( int i = 0; i < _radioButtons1.length; ++i) {
			if ( !on_ok( numberObjectDataPair, i))
				return;
		}

		_numberObjectDataPair = numberObjectDataPair;

		super.on_ok(actionEvent);
	}

	/**
	 * @param numberObjectDataPair
	 * @param index
	 * @return
	 */
	private boolean on_ok(NumberObjectData[] numberObjectDataPair, int index) {
		switch ( SwingTool.get_enabled_radioButton( _radioButtons1[ index])) {
			case 0:
				numberObjectDataPair[ index]._type = "agent";
				numberObjectDataPair[ index]._objectName = _agentSelector[ index].get();
				numberObjectDataPair[ index]._numberVariable = ( String)_agentNumberObjectComboBox[ index].getSelectedItem();
				break;
			case 1:
				numberObjectDataPair[ index]._type = "spot";
				numberObjectDataPair[ index]._objectName = _spotSelector[ index].get();
				numberObjectDataPair[ index]._numberVariable = ( String)_spotNumberObjectComboBox[ index].getSelectedItem();
				break;
			case 2:
				return true;
			default:
				return false;
		}

		if ( null == numberObjectDataPair[ index]._objectName || numberObjectDataPair[ index]._objectName.equals( ""))
			return false;

		if ( null == numberObjectDataPair[ index]._numberVariable || numberObjectDataPair[ index]._numberVariable.equals( ""))
			return false;

		return true;
	}
}
