/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.simple.panel.keyword;

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
import soars.application.visualshell.object.entity.base.object.keyword.KeywordObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.observer.Observer;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextUndoRedoManager;


/**
 * @author kurata
 *
 */
public class KeywordPanel extends SimpleVariablePanel {

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
	public KeywordPanel(EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, VariableTableBase variableTableBase, Color color, Frame owner, Component parent) {
		super("keyword", entityBase, propertyPanelBaseMap, variableTableBase, color, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#setup_center_panel(javax.swing.JPanel)
	 */
	@Override
	protected boolean setup_center_panel(JPanel parent) {
		insert_vertical_strut( parent);
		setup_name_textField( parent);
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
		_nameTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters1));
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
	private void setup_initial_value_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.keyword.dialog.initial.value"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_initialValueTextField = new TextField();
		_initialValueTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters3));
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

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.keyword.dialog.comment"));
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
		_nameTextField.setText( ( null != objectBase && objectBase instanceof KeywordObject) ? simpleVariableObject._name : "");
		_initialValueTextField.setText( ( null != simpleVariableObject && simpleVariableObject instanceof KeywordObject) ? simpleVariableObject._initialValue : "");
		_commentTextField.setText( ( null != simpleVariableObject && simpleVariableObject instanceof KeywordObject) ? simpleVariableObject._comment : "");
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
		KeywordObject keywordObject = new KeywordObject();
		keywordObject._name = _nameTextField.getText();
		keywordObject._initialValue = _initialValueTextField.getText();
		keywordObject._comment = _commentTextField.getText();
		return keywordObject;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#can_get_data(soars.application.visualshell.object.entity.base.object.base.ObjectBase)
	 */
	@Override
	protected boolean can_get_data(ObjectBase objectBase) {
		if ( !( objectBase instanceof KeywordObject))
			return false;

		KeywordObject keywordObject = ( KeywordObject)objectBase;

		if ( !Constant.is_correct_name( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( _nameTextField.getText()))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( _nameTextField.getText()))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( _entityBase instanceof AgentObject && ( _nameTextField.getText().equals( "$Name")
			|| _nameTextField.getText().equals( "$Role") || _nameTextField.getText().equals( "$Spot"))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( null == _initialValueTextField.getText()
			|| 0 < _initialValueTextField.getText().indexOf( '$')
			|| _initialValueTextField.getText().equals( "$")
			|| _initialValueTextField.getText().startsWith( " ")
			|| _initialValueTextField.getText().endsWith( " ")
			|| _initialValueTextField.getText().equals( "$Name")
			|| _initialValueTextField.getText().equals( "$Role")
			|| _initialValueTextField.getText().equals( "$Spot")
			|| 0 <= _initialValueTextField.getText().indexOf( Constant._experimentName)) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( _initialValueTextField.getText().startsWith( "$")
			&& ( 0 <= _initialValueTextField.getText().indexOf( " ")
			|| 0 < _initialValueTextField.getText().indexOf( "$", 1)
			|| 0 < _initialValueTextField.getText().indexOf( ")", 1))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( !_nameTextField.getText().equals( keywordObject._name)) {
			if ( _variableTableBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
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
					ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] kinds = Constant.get_kinds( "keyword");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], _nameTextField.getText(), _entityBase)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if ( null != LayerManager.get_instance().get_chart( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
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
		if ( !( objectBase instanceof KeywordObject))
			return;

		KeywordObject keywordObject = ( KeywordObject)objectBase;

		if ( !keywordObject._name.equals( "") && !_nameTextField.getText().equals( keywordObject._name)) {
			_propertyPanelBaseMap.get( "variable").update_object_name( "keyword", keywordObject._name, _nameTextField.getText());

			WarningManager.get_instance().cleanup();

			boolean result = LayerManager.get_instance().update_object_name( "keyword", keywordObject._name, _nameTextField.getText(), _entityBase);
			if ( result) {
				if ( _entityBase.update_object_name( "keyword", keywordObject._name, _nameTextField.getText())) {
					String[] message = new String[] {
						( ( _entityBase instanceof AgentObject) ? "Agent : " : "Spot : ") + _entityBase._name
					};

					WarningManager.get_instance().add( message);
				}

				if ( _entityBase instanceof AgentObject)
					Observer.get_instance().on_update_agent_object( "keyword", keywordObject._name, _nameTextField.getText());
				else if ( _entityBase instanceof SpotObject)
					Observer.get_instance().on_update_spot_object( "keyword", keywordObject._name, _nameTextField.getText());

				Observer.get_instance().on_update_entityBase( true);
				Observer.get_instance().modified();
			}
		}

		keywordObject._name = _nameTextField.getText();
		keywordObject._initialValue = _initialValueTextField.getText();
		keywordObject._comment = _commentTextField.getText();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#can_paste(soars.application.visualshell.object.entity.base.object.base.ObjectBase, java.util.List)
	 */
	@Override
	public boolean can_paste(ObjectBase objectBase, List<ObjectBase> objectBases) {
		if ( !( objectBase instanceof KeywordObject))
			return false;

		KeywordObject keywordObject = ( KeywordObject)objectBase;

		if ( !Constant.is_correct_name( keywordObject._name))
			return false;

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( keywordObject._name))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( keywordObject._name)))
			return false;

		if ( _entityBase instanceof AgentObject && ( keywordObject._name.equals( "$Name")
			|| keywordObject._name.equals( "$Role") || keywordObject._name.equals( "$Spot")))
			return false;

		if ( null == keywordObject._initialValue
			|| 0 < keywordObject._initialValue.indexOf( '$')
			|| keywordObject._initialValue.equals( "$")
			|| keywordObject._initialValue.startsWith( " ")
			|| keywordObject._initialValue.endsWith( " ")
			|| keywordObject._initialValue.equals( "$Name")
			|| keywordObject._initialValue.equals( "$Role")
			|| keywordObject._initialValue.equals( "$Spot")
			|| 0 <= keywordObject._initialValue.indexOf( Constant._experimentName))
			return false;

		if ( keywordObject._initialValue.startsWith( "$")
			&& ( 0 <= keywordObject._initialValue.indexOf( " ")
			|| 0 < keywordObject._initialValue.indexOf( "$", 1)
			|| 0 < keywordObject._initialValue.indexOf( ")", 1)))
			return false;

		if ( _variableTableBase.other_objectBase_has_this_name( keywordObject._kind, keywordObject._name))
			return false;

		String[] propertyPanelBases = Constant.get_propertyPanelBases( "simple variable");

		for ( int i = 0; i < propertyPanelBases.length; ++i) {
			PropertyPanelBase propertyPanelBase = _propertyPanelBaseMap.get( propertyPanelBases[ i]);
			if ( null != propertyPanelBase && propertyPanelBase.contains( keywordObject._name))
				return false;
		}

		String[] kinds = Constant.get_kinds( "keyword");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], keywordObject._name, _entityBase))
				return false;
		}

		if ( null != LayerManager.get_instance().get_chart( keywordObject._name))
			return false;

		return true;
	}
}
