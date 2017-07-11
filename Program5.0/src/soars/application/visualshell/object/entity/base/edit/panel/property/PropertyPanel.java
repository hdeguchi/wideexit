/*
 * 2005/06/02
 */
package soars.application.visualshell.object.entity.base.edit.panel.property;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.util.Map;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.observer.Observer;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextLimiter;
import soars.common.utility.swing.text.TextUndoRedoManager;

/**
 * @author kurata
 */
public class PropertyPanel extends PropertyPanelBase {

	/**
	 * 
	 */
	private JLabel _nameLabel = null;

	/**
	 * 
	 */
	private JLabel _numberLabel = null;

	/**
	 * 
	 */
	private JLabel _initialSpotLabel = null;

	/**
	 * 
	 */
	private JLabel _initialRoleLabel = null;

	/**
	 * 
	 */
	private JLabel _commentLabel = null;

	/**
	 * 
	 */
	private JTextField _nameTextField = null;

	/**
	 * 
	 */
	private JTextField _numberTextField = null;

	/**
	 * 
	 */
	private ObjectSelector _initialSpotSelector = null;

	/**
	 * 
	 */
	private JComboBox _initialRoleComboBox = null;

	/**
	 * 
	 */
	private JTextArea _commentTextArea = null;

	/**
	 * @param title
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public PropertyPanel(String title, EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, int index, Frame owner, Component parent) {
		super(title, entityBase, propertyPanelBaseMap, index, owner, parent);
	}

	/**
	 * @param propertyPanelBaseMap
	 * @return
	 */
	public boolean create(Map<String, PropertyPanelBase> propertyPanelBaseMap) {
		_propertyPanelBaseMap = propertyPanelBaseMap;
		return create();
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase#on_create()
	 */
	@Override
	protected boolean on_create() {
		if ( !super.on_create())
			return false;



		setLayout( new BorderLayout());



		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_name_text_field( northPanel);

		insert_vertical_strut( northPanel);

		setup_number_text_field( northPanel);

		insert_vertical_strut( northPanel);

		if ( _entityBase instanceof AgentObject) {
			setup_initial_spot_selector( northPanel);
			insert_vertical_strut( northPanel);
		}

		if ( !setup_initial_role_combo_box( northPanel))
			return false;

		insert_vertical_strut( northPanel);

		add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		setup_comment_text_area( centerPanel);

		insert_vertical_strut( centerPanel);

		add( centerPanel);



		adjust();



		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_name_text_field(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_nameLabel = new JLabel(
			ResourceManager.get_instance().get( "edit.object.dialog.name"));
		_nameLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _nameLabel);

		panel.add( Box.createHorizontalStrut( 5));

		_nameTextField = new JTextField( new TextExcluder( Constant._prohibitedCharacters2), _entityBase._name, 0);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _nameTextField, this));

		if ( _entityBase._global || _entityBase.is_multi())
			_nameTextField.setEditable( false);

		panel.add( _nameTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_number_text_field(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_numberLabel = new JLabel(
			ResourceManager.get_instance().get( "edit.object.dialog.number"));
		_numberLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _numberLabel);

		panel.add( Box.createHorizontalStrut( 5));

//		if ( !_entityBase.is_multi())
		_numberTextField = new JTextField( new TextLimiter( "0123456789"), _entityBase._number, 0);
//		else {
//			_numberTextField = new JTextField( new TextLimiter( "0123456789"),
//				String.valueOf( _entityBase._entityBases.size()), 0);
//			_numberTextField.setEditable( false);
//			//_numberTextField.setEnabled( false);
//		}
		_textUndoRedoManagers.add( new TextUndoRedoManager( _numberTextField, this));

		if ( _entityBase._global || _entityBase.is_multi())
			_numberTextField.setEditable( false);

		_numberTextField.setHorizontalAlignment( SwingConstants.RIGHT);

		panel.add( _numberTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_initial_spot_selector(JPanel parent) {
		JPanel panel = new JPanel();
		//panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_initialSpotLabel = new JLabel(
			ResourceManager.get_instance().get( "edit.object.dialog.initial.spot"));
		_initialSpotLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _initialSpotLabel);

		if ( _entityBase.is_multi())
			_initialSpotLabel.setEnabled( false);

		panel.add( Box.createHorizontalStrut( 5));

		if ( !_entityBase.is_multi()) {
			AgentObject agentObject = ( AgentObject)_entityBase;
			_initialSpotSelector = new ObjectSelector(
				"spot", agentObject._initialSpot, true, 500, 100, null, true, null);
		} else {
			_initialSpotSelector = new ObjectSelector( null);
		}

		panel.add( _initialSpotSelector);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_initial_role_combo_box(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_initialRoleLabel = new JLabel(
			ResourceManager.get_instance().get( "edit.object.dialog.initial.role"));
		_initialRoleLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _initialRoleLabel);

		if ( _entityBase.is_multi())
			_initialRoleLabel.setEnabled( false);

		panel.add( Box.createHorizontalStrut( 5));

		if ( !_entityBase.is_multi()) {
			String[] role_names;
			if ( _entityBase instanceof AgentObject)
				role_names = LayerManager.get_instance().get_agent_role_names( true);
			else if ( _entityBase instanceof SpotObject)
				role_names = LayerManager.get_instance().get_spot_role_names( true);
			else
				return false;

			if ( null == role_names)
				_initialRoleComboBox = new JComboBox();
			else
				_initialRoleComboBox = new JComboBox( role_names);

			_initialRoleComboBox.setSelectedItem( _entityBase._initialRole);
			_initialRoleComboBox.setEnabled( !_entityBase._global);
		} else {
			_initialRoleComboBox = new JComboBox();
			_initialRoleComboBox.setEnabled( false);
		}

		panel.add( _initialRoleComboBox);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_comment_text_area(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_commentLabel = new JLabel(
			ResourceManager.get_instance().get( "edit.object.dialog.comment"));
		_commentLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _commentLabel);

		panel.add( Box.createHorizontalStrut( 5));

		_commentTextArea = new JTextArea( _entityBase._comment);
		_commentTextArea.setLineWrap( true);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _commentTextArea, this));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _commentTextArea);

		panel.add( scrollPane);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase#adjust()
	 */
	@Override
	protected void adjust() {
		int width = _nameLabel.getPreferredSize().width;
		width = Math.max( width, _numberLabel.getPreferredSize().width);

		if ( _entityBase instanceof AgentObject)
			width = Math.max( width, _initialSpotLabel.getPreferredSize().width);

		width = Math.max( width, _initialRoleLabel.getPreferredSize().width);
		width = Math.max( width, _commentLabel.getPreferredSize().width);

		Dimension dimension = new Dimension( width,
			_nameLabel.getPreferredSize().height);
		_nameLabel.setPreferredSize( dimension);
		_numberLabel.setPreferredSize( dimension);

		if ( _entityBase instanceof AgentObject)
			_initialSpotLabel.setPreferredSize( dimension);

		_initialRoleLabel.setPreferredSize( dimension);
		_commentLabel.setPreferredSize( dimension);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase#on_setup_completed()
	 */
	@Override
	public void on_setup_completed() {
		_nameTextField.requestFocusInWindow();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase#on_ok()
	 */
	@Override
	public boolean on_ok() {
		if ( _entityBase.is_multi()) {
			_entityBase._comment = _commentTextArea.getText();
			return true;
		}

		if ( !Constant.is_correct_agent_or_spot_name( _nameTextField.getText()))
			return false;

		for ( int i = 0; i < Constant._propertyPanelBases.length; ++i) {
			PropertyPanelBase propertyPanelBase = _propertyPanelBaseMap.get( Constant._propertyPanelBases[ i]);
			if ( null != propertyPanelBase && propertyPanelBase.contains( _nameTextField.getText(), _numberTextField.getText()))
				return false;
		}

		if ( LayerManager.get_instance().contains( _entityBase, _nameTextField.getText(), _numberTextField.getText()))
			return false;

//		if ( _entityBase instanceof AgentObject) {
//			if ( LayerManager.get_instance().role_has_same_agent_name( _nameTextField.getText(), _numberTextField.getText()))
//				return false;
//		}

		if ( LayerManager.get_instance().chartObject_has_same_name( _nameTextField.getText(), _numberTextField.getText()))
			return false;

		if ( !can_adjust_name())
			return false;

		String originalName = _entityBase._name;
		String originalNumber = _entityBase._number;

		String newName = _nameTextField.getText();
		String newNumber = _numberTextField.getText();

		if ( !newNumber.equals( "")) {
			try {
				int number = Integer.parseInt( newNumber);
				if ( 0 == number)
					return false;

				newNumber = String.valueOf( number);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				//_entityBase._number = "";
				return false;
			}
		}

		if ( !newName.equals( originalName) || !newNumber.equals( originalNumber)) {
			String headName = SoarsCommonTool.separate( originalName);
			String headNumber = originalName.substring( headName.length());
			Vector<String[]> ranges = SoarsCommonTool.get_ranges( headNumber, originalNumber);

			String newHeadName = SoarsCommonTool.separate( newName);
			String newHeadNumber = newName.substring( newHeadName.length());
			Vector<String[]> newRanges = SoarsCommonTool.get_ranges( newHeadNumber, newNumber);

			if ( _entityBase instanceof SpotObject) {
				_propertyPanelBaseMap.get( "variable").update_name_and_number( "spot", newName, originalName, headName, ranges, newHeadName, newRanges);
				_propertyPanelBaseMap.get( "simple variable").update_name_and_number( "spot", newName, originalName, headName, ranges, newHeadName, newRanges);
			} else if ( _entityBase instanceof AgentObject) {
				_propertyPanelBaseMap.get( "variable").update_name_and_number( "agent", newName, originalName, headName, ranges, newHeadName, newRanges);
			}

			Observer.get_instance().on_update_object_name_and_number( newName, originalName, headName, ranges, newHeadName, newRanges, _entityBase);
		}

		_entityBase._name = newName;
		_entityBase._number = newNumber;

		if ( _entityBase instanceof AgentObject) {
			AgentObject agentObject = ( AgentObject)_entityBase;
			agentObject._initialSpot = _initialSpotSelector.get();
		}

		String initialRole = ( String)_initialRoleComboBox.getSelectedItem();
		_entityBase._initialRole = ( null == initialRole) ? "" : initialRole;

		_entityBase._comment = _commentTextArea.getText();

		_entityBase.setup_name_dimension( ( Graphics2D)getGraphics());

		return true;
	}

	/**
	 * @return
	 */
	private boolean can_adjust_name() {
		int originalNumber, number;
		try {
			originalNumber = Integer.parseInt( _entityBase._number);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			originalNumber = 0;
		}
		try {
			number = Integer.parseInt( _numberTextField.getText());
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			number = 0;
		}

		String headName = SoarsCommonTool.separate( _entityBase._name);
		String headNumber = _entityBase._name.substring( headName.length());
		Vector<String[]> ranges = SoarsCommonTool.get_ranges( headNumber, _entityBase._number);
		if ( null == ranges)
			return false;

		String newHeadName = SoarsCommonTool.separate( _nameTextField.getText());
		String newHeadNumber = _nameTextField.getText().substring( newHeadName.length());
		Vector<String[]> newRanges = SoarsCommonTool.get_ranges( newHeadNumber, _numberTextField.getText());
		if ( null == newRanges)
			return false;

		if ( !newHeadName.equals( headName)) {
			if ( ( 0 < originalNumber && originalNumber <= number)
				|| ( 0 == originalNumber && 0 == number))
				return true;

			WarningManager.get_instance().cleanup();
			if ( _entityBase instanceof SpotObject) {
				if ( _propertyPanelBaseMap.get( "variable").can_adjust_name( "spot", headName, ranges)
					&& _propertyPanelBaseMap.get( "simple variable").can_adjust_name( "spot", headName, ranges)	
					&& LayerManager.get_instance().can_adjust_spot_name( headName, ranges, false))	
					return true;
			} else if ( _entityBase instanceof AgentObject) {
				if ( _propertyPanelBaseMap.get( "variable").can_adjust_name( "agent", headName, ranges)
					&& LayerManager.get_instance().can_adjust_agent_name( headName, ranges, false))
					return true;
			} else
				return false;

			if ( !WarningManager.get_instance().isEmpty()) {
				WarningDlg1 warningDlg1 = new WarningDlg1(
					_owner,
					ResourceManager.get_instance().get( "warning.dialog1.title"),
					ResourceManager.get_instance().get( "warning.dialog1.message1"),
					_parent);
				warningDlg1.do_modal();
			}
		} else {
			WarningManager.get_instance().cleanup();
			if ( _entityBase instanceof SpotObject) {
				if ( _propertyPanelBaseMap.get( "variable").can_adjust_name( "spot", headName, ranges, newHeadName, newRanges)
					&& _propertyPanelBaseMap.get( "simple variable").can_adjust_name( "spot", headName, ranges, newHeadName, newRanges)
					&& LayerManager.get_instance().can_adjust_spot_name( headName, ranges, newHeadName, newRanges))
					return true;
			} else if ( _entityBase instanceof AgentObject) {
				if ( _propertyPanelBaseMap.get( "variable").can_adjust_name( "agent", headName, ranges, newHeadName, newRanges)
					&& LayerManager.get_instance().can_adjust_agent_name( headName, ranges, newHeadName, newRanges))
					return true;
			} else
				return false;

			if ( !WarningManager.get_instance().isEmpty()) {
				WarningDlg1 warningDlg1 = new WarningDlg1(
					_owner,
					ResourceManager.get_instance().get( "warning.dialog1.title"),
					ResourceManager.get_instance().get( "warning.dialog1.message1"),
					_parent);
				warningDlg1.do_modal();
			}
		}

		return false;
	}

	/**
	 * @param headNumber
	 * @param number
	 * @param originalNumber
	 * @return
	 */
	private Vector<String[]> get_ranges(String headNumber, int number, int originalNumber) {
		Vector<String[]> ranges;
		if ( 0 < number && number < originalNumber) {
			ranges = SoarsCommonTool.get_ranges( headNumber, number + 1, originalNumber);
			//print( ranges);
		} else if ( 0 == number) {
			ranges = SoarsCommonTool.get_ranges( headNumber, _entityBase._number);
			//print( ranges);
		} else if ( 0 == originalNumber) {
			ranges = SoarsCommonTool.get_ranges( headNumber, "");
			//print( ranges);
		} else
			return null;

		return ranges;
	}

	/**
	 * @param ranges
	 */
	private void print(Vector<String[]> ranges) {
		for ( int i = 0; i < ranges.size(); ++i) {
			String[] range = ranges.get( i);
			System.out.println( range[ 0] + "-" + range[ 1]);
		}
	}
}
