/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.simple.panel.spot;

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

import soars.application.visualshell.common.selector.ObjectSelector;
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
import soars.application.visualshell.object.entity.base.object.spot.SpotVariableObject;
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
public class SpotVariablePanel extends SimpleVariablePanel {

	/**
	 * 
	 */
	private ObjectSelector _spotSelector = null;

	/**
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 * @param variableTableBase
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public SpotVariablePanel(EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, VariableTableBase variableTableBase, Color color, Frame owner, Component parent) {
		super("spot variable", entityBase, propertyPanelBaseMap, variableTableBase, color, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#setup_center_panel(javax.swing.JPanel)
	 */
	@Override
	protected boolean setup_center_panel(JPanel parent) {
		insert_vertical_strut( parent);
		setup_name_textField( parent);
		insert_vertical_strut( parent);
		setup_spot_selector( parent);
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
	private void setup_spot_selector(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.spot.variable.dialog.initial.value"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_spotSelector = new ObjectSelector( "spot", "", true, 500, 100, _color, true, null);
		_components.add( _spotSelector);
		panel.add( _spotSelector);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_comment_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.spot.variable.dialog.comment"));
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
		_nameTextField.setText( ( null != simpleVariableObject && simpleVariableObject instanceof SpotVariableObject) ? simpleVariableObject._name : "");
		_spotSelector.set( ( null != simpleVariableObject && simpleVariableObject instanceof SpotVariableObject) ? simpleVariableObject._initialValue : "");
		_commentTextField.setText( ( null != simpleVariableObject && simpleVariableObject instanceof SpotVariableObject) ? simpleVariableObject._comment : "");
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
		_textUndoRedoManagers.add( new TextUndoRedoManager( _commentTextField, this));
	}

//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#clear()
//	 */
//	@Override
//	public void clear() {
//		super.clear();
//		_spot_selector.set( "");
//	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#create_and_get()
	 */
	@Override
	protected ObjectBase create_and_get() {
		SpotVariableObject spotVariableObject = new SpotVariableObject();
		spotVariableObject._name = _nameTextField.getText();
		spotVariableObject._initialValue = _spotSelector.get();
		spotVariableObject._comment = _commentTextField.getText();
		return spotVariableObject;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#can_get_data(soars.application.visualshell.object.entity.base.object.base.ObjectBase)
	 */
	@Override
	protected boolean can_get_data(ObjectBase objectBase) {
		if ( !( objectBase instanceof SpotVariableObject))
			return false;

		SpotVariableObject spotVariableObject = ( SpotVariableObject)objectBase;

		if ( !Constant.is_correct_name( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( _entityBase instanceof AgentObject && ( _nameTextField.getText().equals( "$Name")
			|| _nameTextField.getText().equals( "$Role") || _nameTextField.getText().equals( "$Spot"))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( !_nameTextField.getText().equals( spotVariableObject._name)) {
			if ( _variableTableBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( _nameTextField.getText()))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( _nameTextField.getText()))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		String[] propertyPanelBases = Constant.get_propertyPanelBases( "simple variable");

		for ( int i = 0; i < propertyPanelBases.length; ++i) {
			PropertyPanelBase propertyPanelBase = _propertyPanelBaseMap.get( propertyPanelBases[ i]);
			if ( null != propertyPanelBase && propertyPanelBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] kinds = Constant.get_kinds( "spot variable");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], _nameTextField.getText(), _entityBase)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if ( null != LayerManager.get_instance().get_chart( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"),
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
		if ( !( objectBase instanceof SpotVariableObject))
			return;

		SpotVariableObject spotVariableObject = ( SpotVariableObject)objectBase;

		if ( !spotVariableObject._name.equals( "") && !_nameTextField.getText().equals( spotVariableObject._name)) {
			_propertyPanelBaseMap.get( "variable").update_object_name( "spot variable", spotVariableObject._name, _nameTextField.getText());

			WarningManager.get_instance().cleanup();

			boolean result = LayerManager.get_instance().update_object_name( "spot variable", spotVariableObject._name, _nameTextField.getText(), _entityBase);
			if ( result) {
				if ( _entityBase.update_object_name( "spot variable", spotVariableObject._name, _nameTextField.getText())) {
					String[] message = new String[] {
						( ( _entityBase instanceof AgentObject) ? "Agent : " : "Spot : ") + _entityBase._name
					};

					WarningManager.get_instance().add( message);
				}

				if ( _entityBase instanceof AgentObject)
					Observer.get_instance().on_update_agent_object( "spot variable", spotVariableObject._name, _nameTextField.getText());
				else if ( _entityBase instanceof SpotObject)
					Observer.get_instance().on_update_spot_object( "spot variable", spotVariableObject._name, _nameTextField.getText());

				Observer.get_instance().on_update_entityBase( true);
				Observer.get_instance().modified();
			}
		}

		spotVariableObject._name = _nameTextField.getText();
		spotVariableObject._initialValue = _spotSelector.get();
		spotVariableObject._comment = _commentTextField.getText();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#can_paste(soars.application.visualshell.object.entity.base.object.base.ObjectBase, java.util.List)
	 */
	@Override
	public boolean can_paste(ObjectBase objectBase, List<ObjectBase> objectBases) {
		if ( !( objectBase instanceof SpotVariableObject))
			return false;

		SpotVariableObject spotVariableObject = ( SpotVariableObject)objectBase;

		if ( !Constant.is_correct_name( spotVariableObject._name))
			return false;

		if ( _entityBase instanceof AgentObject && ( spotVariableObject._name.equals( "$Name")
			|| spotVariableObject._name.equals( "$Role") || spotVariableObject._name.equals( "$Spot")))
			return false;

		if ( _variableTableBase.other_objectBase_has_this_name( spotVariableObject._kind, spotVariableObject._name))
			return false;

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( spotVariableObject._name))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( spotVariableObject._name)))
			return false;

		if ( !spotVariableObject._initialValue.equals( "")) {
			if ( ( null == LayerManager.get_instance().get_spot_has_this_name( spotVariableObject._initialValue)))
				return false;
		}

		String[] propertyPanelBases = Constant.get_propertyPanelBases( "simple variable");

		for ( int i = 0; i < propertyPanelBases.length; ++i) {
			PropertyPanelBase propertyPanelBase = _propertyPanelBaseMap.get( propertyPanelBases[ i]);
			if ( null != propertyPanelBase && propertyPanelBase.contains( spotVariableObject._name))
				return false;
		}

		String[] kinds = Constant.get_kinds( "spot variable");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], spotVariableObject._name, _entityBase))
				return false;
		}

		if ( null != LayerManager.get_instance().get_chart( spotVariableObject._name))
			return false;

		return true;
	}
}
