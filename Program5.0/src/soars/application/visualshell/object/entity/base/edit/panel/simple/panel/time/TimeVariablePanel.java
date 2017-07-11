/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.simple.panel.time;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.VariableTableBase;
import soars.application.visualshell.object.entity.base.edit.panel.simple.panel.base.SimpleVariablePanel;
import soars.application.visualshell.object.entity.base.object.base.ObjectBase;
import soars.application.visualshell.object.entity.base.object.base.SimpleVariableObject;
import soars.application.visualshell.object.entity.base.object.time.TimeVariableObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.base.object.legacy.common.time.TimeRule;
import soars.application.visualshell.observer.Observer;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextUndoRedoManager;


/**
 * @author kurata
 *
 */
public class TimeVariablePanel extends SimpleVariablePanel {

	/**
	 * 
	 */
	private TextField _timeTextField = null;

	/**
	 * 
	 */
	private List<ComboBox> _timeComboBoxes = new ArrayList<ComboBox>();

	/**
	 * 
	 */
	private List<JLabel> _timeLabels = new ArrayList<JLabel>();

	/**
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 * @param variableTableBase
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public TimeVariablePanel(EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, VariableTableBase variableTableBase, Color color, Frame owner, Component parent) {
		super("time variable", entityBase, propertyPanelBaseMap, variableTableBase, color, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#setup_center_panel(javax.swing.JPanel)
	 */
	@Override
	protected boolean setup_center_panel(JPanel parent) {
		insert_vertical_strut( parent);
		setup_name_textField( parent);
		insert_vertical_strut( parent);
		setup_initial_value_components( parent);
		insert_vertical_strut( parent);
		setup_comment_textField( parent);
		insert_vertical_strut( parent);
		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_name_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.object.dialog.name"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_nameTextField = new TextField();
		_nameTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters13));
		_nameTextField.setSelectionColor( _color);
		_nameTextField.setForeground( _color);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _nameTextField, this));
		_components.add( _nameTextField);
		panel.add( _nameTextField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_initial_value_components(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.time.variable.dialog.initial.value"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		//_time_textField = new TextField( new TextExcluder( Constant._prohibited_characters14), "", 0);
		_timeTextField = new TextField();
		_timeTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters14));
		_timeTextField.setHorizontalAlignment( SwingConstants.RIGHT);
		_timeTextField.setSelectionColor( _color);
		_timeTextField.setForeground( _color);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _timeTextField, this));
		_timeTextField.setPreferredSize( new Dimension( 60, _timeTextField.getPreferredSize().height));
		panel.add( _timeTextField);

		panel.add( Box.createHorizontalStrut( 5));

		label = new JLabel( " / ");
		label.setForeground( _color);
		_timeLabels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		//ComboBox comboBox = new JComboBox( CommonTool.get_hours());
		//comboBox.setRenderer( new CommonComboBoxRenderer( null, true));
		ComboBox comboBox = new ComboBox( CommonTool.get_hours(), _color, true, new CommonComboBoxRenderer( _color, true));
		comboBox.setPreferredSize( new Dimension( 60, comboBox.getPreferredSize().height));
		_timeComboBoxes.add( comboBox);
		panel.add( comboBox);

		panel.add( Box.createHorizontalStrut( 5));

		label = new JLabel( " : ");
		label.setForeground( _color);
		_timeLabels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		//comboBox = new JComboBox( CommonTool.get_minutes00());
		//comboBox.setRenderer( new CommonComboBoxRenderer( null, true));
		comboBox = new ComboBox( CommonTool.get_minutes00(), _color, true, new CommonComboBoxRenderer( _color, true));
		comboBox.setPreferredSize( new Dimension( 60, comboBox.getPreferredSize().height));
		_timeComboBoxes.add( comboBox);
		panel.add( comboBox);

		panel.add( Box.createHorizontalStrut( 5));

		_components.add( panel);
		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_comment_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.time.variable.dialog.comment"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_commentTextField = new TextField();
		_commentTextField.setSelectionColor( _color);
		_commentTextField.setForeground( _color);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _commentTextField, this));
		_components.add( _commentTextField);
		panel.add( _commentTextField);

		parent.add( panel);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.simple.panel.base.SimpleVariablePanel#update(soars.application.visualshell.object.entity.base.object.base.ObjectBase)
	 */
	@Override
	public void update(ObjectBase objectBase) {
		SimpleVariableObject simpleVariableObject = ( SimpleVariableObject)objectBase;
		_nameTextField.setText( ( null != simpleVariableObject && simpleVariableObject instanceof TimeVariableObject) ? simpleVariableObject._name : "");
		update_initial_values( ( null != simpleVariableObject && simpleVariableObject instanceof TimeVariableObject) ? ( TimeVariableObject)simpleVariableObject : null);
		_commentTextField.setText( ( null != simpleVariableObject && simpleVariableObject instanceof TimeVariableObject) ? simpleVariableObject._comment : "");
		// TODO Auto-generated method stub
		_textUndoRedoManagers.clear();
		setup_textUndoRedoManagers();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#setup_textUndoRedoManagers()
	 */
	@Override
	protected void setup_textUndoRedoManagers() {
		// TODO Auto-generated method stub
		if ( !_textUndoRedoManagers.isEmpty())
			return;

		_textUndoRedoManagers.add( new TextUndoRedoManager( _nameTextField, this));
		_textUndoRedoManagers.add( new TextUndoRedoManager( _timeTextField, this));
		_textUndoRedoManagers.add( new TextUndoRedoManager( _commentTextField, this));
	}

	/**
	 * @param timeVariableObject
	 */
	private void update_initial_values(TimeVariableObject timeVariableObject) {
		if ( null == timeVariableObject) {
			_timeTextField.setText( "");
			_timeComboBoxes.get( 0).setSelectedIndex( 0);
			_timeComboBoxes.get( 1).setSelectedIndex( 0);
			return;
		}

		if ( timeVariableObject._initialValue.startsWith( "$"))
			_timeTextField.setText( timeVariableObject._initialValue);
		else {
			String[] words = TimeRule.get_time_elements( timeVariableObject._initialValue);
			if ( null == words)
				return;

			_timeTextField.setText( words[ 0]);
			_timeComboBoxes.get( 0).setSelectedItem( words[ 1]);
			_timeComboBoxes.get( 1).setSelectedItem( words[ 2]);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#clear()
	 */
	@Override
	public void clear() {
		super.clear();
		_timeTextField.setText( "");
		for ( ComboBox comboBox:_timeComboBoxes)
			comboBox.setSelectedIndex( 0);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#is_empty()
	 */
	@Override
	protected boolean is_empty() {
		return ( super.is_empty() && _timeTextField.getText().equals( ""));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#create_and_get()
	 */
	@Override
	protected ObjectBase create_and_get() {
		TimeVariableObject timeVariableObject = new TimeVariableObject();
		timeVariableObject._name = _nameTextField.getText();

		String initialValue = "";
		if ( _timeTextField.getText().startsWith( "$"))
			initialValue = _timeTextField.getText();
		else {
			if ( !_timeTextField.getText().equals( "")) {
				int day;
				try {
					day = Integer.parseInt( _timeTextField.getText());
				} catch (NumberFormatException e) {
					//e.printStackTrace();
					return timeVariableObject;
				}
				initialValue = ( String.valueOf( day) + "/");
			}
			initialValue += ( ( String)_timeComboBoxes.get( 0).getSelectedItem() + ":" + ( String)_timeComboBoxes.get( 1).getSelectedItem());
		}
		timeVariableObject._initialValue = initialValue;

		timeVariableObject._comment = _commentTextField.getText();

		return timeVariableObject;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#can_get_data(soars.application.visualshell.object.entity.base.object.base.ObjectBase)
	 */
	@Override
	protected boolean can_get_data(ObjectBase objectBase) {
		if ( !( objectBase instanceof TimeVariableObject))
			return false;

		TimeVariableObject timeVariableObject = ( TimeVariableObject)objectBase;

		if ( !Constant.is_correct_name( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( _nameTextField.getText()))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( _nameTextField.getText()))
			|| null != LayerManager.get_instance().get_chart( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( !_nameTextField.getText().equals( timeVariableObject._name)) {
			if ( _variableTableBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}


		if ( null == _timeTextField.getText()) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		String initialValue = "";

		if ( _timeTextField.getText().startsWith( "$")) {
			if ( _timeTextField.getText().equals( "$")
				|| _timeTextField.getText().equals( "$Name")
				|| _timeTextField.getText().equals( "$Role")
				|| _timeTextField.getText().equals( "$Spot")
				|| 0 <= _timeTextField.getText().indexOf( Constant._experimentName)
				|| 0 <= _timeTextField.getText().indexOf( Constant._currentTimeName)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if ( 0 < _timeTextField.getText().indexOf( "$", 1)
				|| 0 < _timeTextField.getText().indexOf( ")", 1)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}

			initialValue = _timeTextField.getText();

		} else {
			if ( !_timeTextField.getText().equals( "")) {
				int day;
				try {
					day = Integer.parseInt( _timeTextField.getText());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog( _parent,
						ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
						ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"),
						JOptionPane.ERROR_MESSAGE);
					return false;
				}
				initialValue = ( String.valueOf( day) + "/");
			}
			initialValue += ( ( String)_timeComboBoxes.get( 0).getSelectedItem() + ":" + ( String)_timeComboBoxes.get( 1).getSelectedItem());
		}


		String[] propertyPanelBases = Constant.get_propertyPanelBases( "simple variable");

		for ( int i = 0; i < propertyPanelBases.length; ++i) {
			PropertyPanelBase propertyPanelBase = _propertyPanelBaseMap.get( propertyPanelBases[ i]);
			if ( null != propertyPanelBase && propertyPanelBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] kinds = Constant.get_kinds( "time variable");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], _nameTextField.getText(), _entityBase)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#get_data(soars.application.visualshell.object.entity.base.object.base.ObjectBase)
	 */
	@Override
	protected void get_data(ObjectBase objectBase) {
		if ( !( objectBase instanceof TimeVariableObject))
			return;

		TimeVariableObject timeVariableObject = ( TimeVariableObject)objectBase;

		if ( !timeVariableObject._name.equals( "") && !_nameTextField.getText().equals( timeVariableObject._name)) {
			_propertyPanelBaseMap.get( "variable").update_object_name( "time variable", timeVariableObject._name, _nameTextField.getText());

			WarningManager.get_instance().cleanup();

			boolean result = LayerManager.get_instance().update_object_name( "time variable", timeVariableObject._name, _nameTextField.getText(), _entityBase);
			if ( result) {
				if ( _entityBase.update_object_name( "time variable", timeVariableObject._name, _nameTextField.getText())) {
					String[] message = new String[] {
						( ( _entityBase instanceof AgentObject) ? "Agent : " : "Spot : ") + _entityBase._name
					};

					WarningManager.get_instance().add( message);
				}

				if ( _entityBase instanceof AgentObject)
					Observer.get_instance().on_update_agent_object( "time variable", timeVariableObject._name, _nameTextField.getText());
				else if ( _entityBase instanceof SpotObject)
					Observer.get_instance().on_update_spot_object( "time variable", timeVariableObject._name, _nameTextField.getText());

				Observer.get_instance().on_update_entityBase( true);
				Observer.get_instance().modified();
			}
		}

		timeVariableObject._name = _nameTextField.getText();

		String initialValue = "";
		if ( _timeTextField.getText().startsWith( "$"))
			initialValue = _timeTextField.getText();
		else {
			if ( !_timeTextField.getText().equals( "")) {
				int day;
				try {
					day = Integer.parseInt( _timeTextField.getText());
				} catch (NumberFormatException e) {
					//e.printStackTrace();
					return;
				}
				initialValue = ( String.valueOf( day) + "/");
			}
			initialValue += ( ( String)_timeComboBoxes.get( 0).getSelectedItem() + ":" + ( String)_timeComboBoxes.get( 1).getSelectedItem());
		}
		timeVariableObject._initialValue = initialValue;

		timeVariableObject._comment = _commentTextField.getText();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#can_paste(soars.application.visualshell.object.entity.base.object.base.ObjectBase, java.util.List)
	 */
	@Override
	public boolean can_paste(ObjectBase objectBase, List<ObjectBase> objectBases) {
		if ( !( objectBase instanceof TimeVariableObject))
			return false;

		TimeVariableObject timeVariableObject = ( TimeVariableObject)objectBase;

		if ( !Constant.is_correct_name( timeVariableObject._name))
			return false;

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( timeVariableObject._name))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( timeVariableObject._name))
			|| null != LayerManager.get_instance().get_chart( timeVariableObject._name))
			return false;

		if ( _variableTableBase.other_objectBase_has_this_name( timeVariableObject._kind, timeVariableObject._name))
			return false;


		if ( timeVariableObject._initialValue.startsWith( "$")) {
			if ( timeVariableObject._initialValue.equals( "$")
				|| timeVariableObject._initialValue.equals( "$Name")
				|| timeVariableObject._initialValue.equals( "$Role")
				|| timeVariableObject._initialValue.equals( "$Spot")
				|| 0 <= timeVariableObject._initialValue.indexOf( Constant._experimentName)
				|| 0 <= timeVariableObject._initialValue.indexOf( Constant._currentTimeName))
				return false;

			if ( 0 < timeVariableObject._initialValue.indexOf( "$", 1)
				|| 0 < timeVariableObject._initialValue.indexOf( ")", 1))
				return false;
		} else {
			if ( !timeVariableObject._initialValue.equals( "")) {
				String[] words = timeVariableObject._initialValue.split( "/");
				if ( 2 <= words.length) {
					try {
						int day = Integer.parseInt( words[ 0]);
					} catch (NumberFormatException e) {
						return false;
					}
				}
			}
		}


		String[] propertyPanelBases = Constant.get_propertyPanelBases( "simple variable");

		for ( int i = 0; i < propertyPanelBases.length; ++i) {
			PropertyPanelBase propertyPanelBase = _propertyPanelBaseMap.get( propertyPanelBases[ i]);
			if ( null != propertyPanelBase && propertyPanelBase.contains( timeVariableObject._name))
				return false;
		}

		String[] kinds = Constant.get_kinds( "time variable");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], timeVariableObject._name, _entityBase))
				return false;
		}

		return true;
	}
}
