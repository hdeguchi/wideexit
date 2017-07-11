/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.simple.panel.number;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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
import soars.application.visualshell.object.entity.base.object.number.NumberObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
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
public class NumberVariablePanel extends SimpleVariablePanel {

	/**
	 * 
	 */
	private ComboBox _typeComboBox = null;

	/**
	 * 
	 */
	private TextField _initialValueTextField = null;

	/**
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 * @param variableTableBase
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public NumberVariablePanel(EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, VariableTableBase variableTableBase, Color color, Frame owner, Component parent) {
		super("number object", entityBase, propertyPanelBaseMap, variableTableBase, color, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#setup_center_panel(javax.swing.JPanel)
	 */
	@Override
	protected boolean setup_center_panel(JPanel parent) {
		insert_vertical_strut( parent);
		setup_name_textField( parent);
		insert_vertical_strut( parent);
		setup_type_comboBox( parent);
		insert_vertical_strut( parent);
		setup_initial_value_textField( parent);
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
		_nameTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters4));
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
	private void setup_type_comboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.number.object.dialog.type"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_typeComboBox = new ComboBox( new String[] {
				ResourceManager.get_instance().get( "number.object.integer"),
				ResourceManager.get_instance().get( "number.object.real.number")},
			_color, false, new CommonComboBoxRenderer( _color, false));
		_components.add( _typeComboBox);
		panel.add( _typeComboBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_initial_value_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.number.object.dialog.initial.value"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_initialValueTextField = new TextField();
		_initialValueTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters5));
		_initialValueTextField.setSelectionColor( _color);
		_initialValueTextField.setForeground( _color);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _initialValueTextField, this));
		_components.add( _initialValueTextField);
		panel.add( _initialValueTextField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_comment_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.number.object.dialog.comment"));
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
		if ( null == simpleVariableObject || !(simpleVariableObject instanceof NumberObject)) {
			_nameTextField.setText( "");
			_initialValueTextField.setText( "");
			_commentTextField.setText( "");
		} else {
			NumberObject numberObject = ( NumberObject)simpleVariableObject;
			_nameTextField.setText( numberObject._name);
			_typeComboBox.setSelectedItem( NumberObject.get_type_name( numberObject._type));
			_initialValueTextField.setText( numberObject._initialValue);
			_commentTextField.setText( numberObject._comment);
		}

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
		_textUndoRedoManagers.add( new TextUndoRedoManager( _initialValueTextField, this));
		_textUndoRedoManagers.add( new TextUndoRedoManager( _commentTextField, this));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#clear()
	 */
	@Override
	public void clear() {
		super.clear();
		_typeComboBox.setSelectedIndex( 0);
		_initialValueTextField.setText( "");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#is_empty()
	 */
	@Override
	protected boolean is_empty() {
		return ( super.is_empty() && _initialValueTextField.getText().equals( ""));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#create_and_get()
	 */
	@Override
	protected ObjectBase create_and_get() {
		NumberObject numberObject = new NumberObject();
		numberObject._name = _nameTextField.getText();
		numberObject._type = NumberObject.get_type( ( String)_typeComboBox.getSelectedItem());
		numberObject._initialValue = _initialValueTextField.getText();
		numberObject._comment = _commentTextField.getText();
		return numberObject;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#can_get_data(soars.application.visualshell.object.entity.base.object.base.ObjectBase)
	 */
	@Override
	protected boolean can_get_data(ObjectBase objectBase) {
		if ( !( objectBase instanceof NumberObject))
			return false;

		NumberObject numberObject = ( NumberObject)objectBase;

		if ( !Constant.is_correct_name( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( _nameTextField.getText()))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( _nameTextField.getText()))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		String type = ( String)_typeComboBox.getSelectedItem();
		if ( null == type || type.equals( ""))
			return false;

		if ( !_nameTextField.getText().equals( numberObject._name)) {
			if ( _variableTableBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String initialValue = _initialValueTextField.getText();

		if ( !initialValue.equals( "")) {
			if ( initialValue.equals( "$") || 0 < initialValue.indexOf( '$')) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if ( initialValue.startsWith( "$") && 0 < initialValue.indexOf( "$", 1)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if ( initialValue.startsWith( "$") && 0 < initialValue.indexOf( ")", 1)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if ( initialValue.equals( "$Name")
				|| initialValue.equals( "$Role")
				|| initialValue.equals( "$Spot")
				|| 0 <= initialValue.indexOf( Constant._experimentName)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}

			initialValue = NumberObject.is_correct( initialValue, NumberObject.get_type( type));
			if ( null == initialValue) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] propertyPanelBases = Constant.get_propertyPanelBases( "simple variable");

		for ( int i = 0; i < propertyPanelBases.length; ++i) {
			PropertyPanelBase propertyPanelBase = _propertyPanelBaseMap.get( propertyPanelBases[ i]);
			if ( null != propertyPanelBase && propertyPanelBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] kinds = Constant.get_kinds( "number object");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], _nameTextField.getText(), _entityBase)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		// TODO 型チェック
		if ( !NumberObject.get_type_name( numberObject._type).equals( ( String)_typeComboBox.getSelectedItem())) {
			if ( !LayerManager.get_instance().is_number_object_type_correct( ( _entityBase instanceof AgentObject) ? "agent" : "spot", numberObject._name, NumberObject.get_type( ( String)_typeComboBox.getSelectedItem()))) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.invalid.number.object.type.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if ( null != LayerManager.get_instance().get_chart( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#get_data(soars.application.visualshell.object.entity.base.object.base.ObjectBase)
	 */
	@Override
	protected void get_data(ObjectBase objectBase) {
		if ( !( objectBase instanceof NumberObject))
			return;

		NumberObject numberObject = ( NumberObject)objectBase;

		String type = ( String)_typeComboBox.getSelectedItem();

		if ( !numberObject._name.equals( "") && !_nameTextField.getText().equals( numberObject._name))
			_propertyPanelBaseMap.get( "variable").update_object_name( "number object", numberObject._name, _nameTextField.getText());

		WarningManager.get_instance().cleanup();

		boolean result1 = false;
		if ( !numberObject._name.equals( "") && !_nameTextField.getText().equals( numberObject._name))
			result1 = LayerManager.get_instance().update_number_object_name(
				( _entityBase instanceof AgentObject) ? "agent" : "spot",
					numberObject._name, _nameTextField.getText(), _entityBase);
		boolean result2 = false;
		if ( !numberObject._type.equals( "") && !numberObject._type.equals( NumberObject.get_type( type)))
			result2 = LayerManager.get_instance().update_number_object_type(
				( _entityBase instanceof AgentObject) ? "agent" : "spot",
				_nameTextField.getText(), NumberObject.get_type( type), _entityBase);
		if ( result1 || result2) {
			boolean result3 = false;
			if ( !numberObject._name.equals( "") && !_nameTextField.getText().equals( numberObject._name))
				result3 = _entityBase.update_object_name( "number object", numberObject._name, _nameTextField.getText());
			boolean result4 = false;
			if ( !numberObject._type.equals( "") && !numberObject._type.equals( NumberObject.get_type( type)))
				result4 = _entityBase.update_number_object_type(
					( _entityBase instanceof AgentObject) ? "agent" : "spot",
					_nameTextField.getText(), NumberObject.get_type( type));
			if ( result3 || result4) {
				String[] message = new String[] {
					( ( _entityBase instanceof AgentObject) ? "Agent : " : "Spot : ") + _entityBase._name
				};

				WarningManager.get_instance().add( message);
			}

			if ( _entityBase instanceof AgentObject)
				Observer.get_instance().on_update_agent_object( "number object", numberObject._name, _nameTextField.getText());
			else if ( _entityBase instanceof SpotObject)
				Observer.get_instance().on_update_spot_object( "number object", numberObject._name, _nameTextField.getText());

			Observer.get_instance().on_update_entityBase( true);
			Observer.get_instance().modified();
		}

		numberObject._name = _nameTextField.getText();
		numberObject._type = NumberObject.get_type( type);
		numberObject._initialValue = _initialValueTextField.getText();
		numberObject._comment = _commentTextField.getText();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#can_paste(soars.application.visualshell.object.entity.base.object.base.ObjectBase, java.util.List)
	 */
	@Override
	public boolean can_paste(ObjectBase objectBase, List<ObjectBase> objectBases) {
		if ( !( objectBase instanceof NumberObject))
			return false;

		NumberObject numberObject = ( NumberObject)objectBase;

		if ( !Constant.is_correct_name( numberObject._name))
			return false;

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( numberObject._name))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( numberObject._name)))
			return false;

		if ( _variableTableBase.other_objectBase_has_this_name( numberObject._kind, numberObject._name))
			return false;

		if ( !numberObject._initialValue.equals( "")) {
			if ( numberObject._initialValue.equals( "$") || 0 < numberObject._initialValue.indexOf( '$'))
				return false;

			if ( numberObject._initialValue.startsWith( "$") && 0 < numberObject._initialValue.indexOf( "$", 1))
				return false;

			if ( numberObject._initialValue.startsWith( "$") && 0 < numberObject._initialValue.indexOf( ")", 1))
				return false;

			if ( numberObject._initialValue.equals( "$Name")
				|| numberObject._initialValue.equals( "$Role")
				|| numberObject._initialValue.equals( "$Spot")
				|| 0 <= numberObject._initialValue.indexOf( Constant._experimentName))
				return false;

			if ( null == NumberObject.is_correct( numberObject._initialValue, numberObject._type))
				return false;
		}

		String[] propertyPanelBases = Constant.get_propertyPanelBases( "simple variable");

		for ( int i = 0; i < propertyPanelBases.length; ++i) {
			PropertyPanelBase propertyPanelBase = _propertyPanelBaseMap.get( propertyPanelBases[ i]);
			if ( null != propertyPanelBase && propertyPanelBase.contains( numberObject._name))
				return false;
		}

		String[] kinds = Constant.get_kinds( "number object");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], numberObject._name, _entityBase))
				return false;
		}

		if ( null != LayerManager.get_instance().get_chart( numberObject._name))
			return false;

		return true;
	}
}
