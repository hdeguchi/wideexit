/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.class_variable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.common.arbitrary.ClassManager;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.VariableTableBase;
import soars.application.visualshell.object.entity.base.object.base.ObjectBase;
import soars.application.visualshell.object.entity.base.object.class_variable.ClassVariableObject;
import soars.application.visualshell.observer.Observer;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextUndoRedoManager;

/**
 * @author kurata
 *
 */
public class ClassVariablePanel extends PanelBase {

	/**
	 * 
	 */
	private ClassManager _classManager = null;

	/**
	 * 
	 */
	private JTextField _jarFilenameTextField = null;

	/**
	 * 
	 */
	private JTextField _classnameTextField = null;

	/**
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 * @param variableTableBase
	 * @param classManager
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public ClassVariablePanel(EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, VariableTableBase variableTableBase, ClassManager classManager, Color color, Frame owner, Component parent) {
		super("class variable", entityBase, propertyPanelBaseMap, variableTableBase, color, owner, parent);
		_classManager = classManager;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#setup()
	 */
	@Override
	public boolean setup() {
		if ( !super.setup())
			return false;


		setLayout( new BorderLayout());


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		setup_center_panel( centerPanel);

		add( centerPanel);


		JPanel eastPanel = new JPanel();
		eastPanel.setLayout( new BorderLayout());

		setup_buttons( eastPanel);

		add( eastPanel, "East");


		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#setup_center_panel(javax.swing.JPanel)
	 */
	@Override
	protected boolean setup_center_panel(JPanel parent) {
		insert_vertical_strut( parent);
		setup_nameTextField( parent);
		insert_vertical_strut( parent);
		setup_jarFilenameTextField( parent);
		insert_vertical_strut( parent);
		setup_classnameTextField( parent);
		insert_vertical_strut( parent);
		setup_commentTextField( parent);
		insert_vertical_strut( parent);
		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_nameTextField(JPanel parent) {
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
	private void setup_jarFilenameTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.object.dialog.class.variable.table.header.jar.filename"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_jarFilenameTextField = new JTextField();
		_jarFilenameTextField.setEditable( false);
		_jarFilenameTextField.setSelectionColor( _color);
		_jarFilenameTextField.setForeground( _color);
		_components.add( _jarFilenameTextField);
		panel.add( _jarFilenameTextField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_classnameTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.object.dialog.class.variable.table.header.classname"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_classnameTextField = !_entityBase.is_multi() ? new CustomDDTextField( _jarFilenameTextField) : new JTextField();
		_classnameTextField.setEditable( false);
		_classnameTextField.setSelectionColor( _color);
		_classnameTextField.setForeground( _color);
		_components.add( _classnameTextField);
		panel.add( _classnameTextField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_commentTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.object.dialog.class.variable.table.header.comment"));
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

	/**
	 * 
	 */
	public void adjust() {
		int width = 0;
		for ( JLabel label:_labels)
			width = Math.max( width, label.getPreferredSize().width);

		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#on_append(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_append(ActionEvent actionEvent) {
		ObjectBase objectBase = on_append();
		if ( null == objectBase)
			return;

		_variableTableBase.append( objectBase);

		_propertyPanelBaseMap.get( "variable").update();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#on_update(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_update(ActionEvent actionEvent) {
		int[] rows = _variableTableBase.getSelectedRows();
		if ( null == rows || 1 != rows.length)
			return;

		ObjectBase objectBase = ( ObjectBase)_variableTableBase.getValueAt( rows[ 0], 0);
		update( rows[ 0], objectBase, true);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#update(int, soars.application.visualshell.object.entity.base.object.base.ObjectBase, boolean)
	 */
	@Override
	protected void update(int row, ObjectBase objectBase, boolean selection) {
		WarningManager.get_instance().cleanup();

		ObjectBase originalObjectBase = ObjectBase.create( objectBase);
		if ( !on_update( objectBase))
			return;

		_variableTableBase.update( row, originalObjectBase, selection);

		_propertyPanelBaseMap.get( "variable").update();

		if ( !WarningManager.get_instance().isEmpty()) {
			WarningDlg1 warningDlg1 = new WarningDlg1(
				_owner,
				ResourceManager.get_instance().get( "warning.dialog1.title"),
				ResourceManager.get_instance().get( "warning.dialog1.message3"),
				_parent);
			warningDlg1.do_modal();
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#update(soars.application.visualshell.object.entity.base.object.base.ObjectBase)
	 */
	@Override
	public void update(ObjectBase objectBase) {
		if ( null == objectBase) {
			_nameTextField.setText( "");
			//_jarFilenameTextField.setText( "");
			//_classnameTextField.setText( "");
			_commentTextField.setText( "");
		} else {
			ClassVariableObject classVariableObject = ( ClassVariableObject)objectBase;
			_nameTextField.setText( classVariableObject._name);
			_jarFilenameTextField.setText( classVariableObject._jarFilename);
			_classnameTextField.setText( classVariableObject._classname);
			_commentTextField.setText( classVariableObject._comment);
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
		_textUndoRedoManagers.add( new TextUndoRedoManager( _commentTextField, this));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#create_and_get()
	 */
	@Override
	protected ObjectBase create_and_get() {
		ClassVariableObject classVariableObject = ( ClassVariableObject)create();
		classVariableObject._name = _nameTextField.getText();
		classVariableObject._jarFilename = _jarFilenameTextField.getText();
		classVariableObject._classname = _classnameTextField.getText();
		classVariableObject._comment = _commentTextField.getText();
		return classVariableObject;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#can_get_data(soars.application.visualshell.object.entity.base.object.base.ObjectBase)
	 */
	@Override
	protected boolean can_get_data(ObjectBase objectBase) {
		return can_get_data( ( ClassVariableObject)objectBase, _nameTextField.getText(), _jarFilenameTextField.getText(), _classnameTextField.getText(), _commentTextField.getText());
	}

	/**
	 * @param classVariableObject
	 * @param name
	 * @param jarfilename
	 * @param classname
	 * @param comment
	 * @return
	 */
	public boolean can_get_data(ClassVariableObject classVariableObject, String name, String jarfilename, String classname, String comment) {
		if ( !Constant.is_correct_name( name)) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( name))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( name))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( jarfilename.equals( "") || classname.equals( "")) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( _entityBase instanceof AgentObject && ( name.equals( "$Name") || name.equals( "$Role") || name.equals( "$Spot"))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		WarningManager.get_instance().cleanup();

		if ( LayerManager.get_instance().other_uses_this_class_variable_as_different_class( name, jarfilename, classname, _entityBase)) {
			if ( !WarningManager.get_instance().isEmpty()) {
				WarningDlg1 warningDlg1 = new WarningDlg1(
					_owner,
					ResourceManager.get_instance().get( "warning.dialog1.title"),
					ResourceManager.get_instance().get( "warning.dialog1.message1"),
					_parent);
				warningDlg1.do_modal();
			}
			return false;
		}

		if ( !name.equals( classVariableObject._name)
			|| !jarfilename.equals( classVariableObject._jarFilename)
			|| !classname.equals( classVariableObject._classname)) {

			if ( !name.equals( classVariableObject._name) && _variableTableBase.contains( name)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] property_pages = Constant.get_propertyPanelBases( "class variable");

		for ( int i = 0; i < property_pages.length; ++i) {
			PropertyPanelBase propertyPanelBase = _propertyPanelBaseMap.get( property_pages[ i]);
			if ( null != propertyPanelBase && propertyPanelBase.contains( name)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] kinds = Constant.get_kinds( "class variable");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], name, _entityBase)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"),
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
		get_data( ( ClassVariableObject)objectBase, _nameTextField.getText(), _jarFilenameTextField.getText(), _classnameTextField.getText(), _commentTextField.getText());
	}

	/**
	 * @param classVariableObject
	 * @param name
	 * @param jarfilename
	 * @param classname
	 * @param comment
	 */
	public void get_data(ClassVariableObject classVariableObject, String name, String jarfilename, String classname, String comment) {
		if ( !classVariableObject._name.equals( "") && !name.equals( classVariableObject._name)) {
			WarningManager.get_instance().cleanup();

			_propertyPanelBaseMap.get( "variable").update_object_name( "class variable", classVariableObject._name, name);

			boolean result = LayerManager.get_instance().update_object_name( "class variable", classVariableObject._name, name, _entityBase);
			if ( result) {
				if ( _entityBase.update_object_name( "class variable", classVariableObject._name, name)) {
					String[] message = new String[] {
						( ( _entityBase instanceof AgentObject) ? "Agent : " : "Spot : ") + _entityBase._name
					};

					WarningManager.get_instance().add( message);
				}

				Observer.get_instance().on_update_entityBase( true);
				Observer.get_instance().modified();
			}
		}

		classVariableObject._name = name;
		classVariableObject._jarFilename = jarfilename;
		classVariableObject._classname = classname;
		classVariableObject._comment = comment;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#can_paste(soars.application.visualshell.object.entity.base.object.base.ObjectBase, java.util.List)
	 */
	@Override
	public boolean can_paste(ObjectBase objectBase, List<ObjectBase> objectBases) {
		if ( !( objectBase instanceof ClassVariableObject))
			return false;

		ClassVariableObject classVariableObject = ( ClassVariableObject)objectBase;

		if ( !Constant.is_correct_name( classVariableObject._name))
			return false;

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( classVariableObject._name))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( classVariableObject._name)))
			return false;

		if ( classVariableObject._jarFilename.equals( "") || classVariableObject._classname.equals( ""))
			return false;

		if ( _entityBase instanceof AgentObject && ( classVariableObject._name.equals( "$Name")
			|| classVariableObject._name.equals( "$Role") || classVariableObject._name.equals( "$Spot")))
			return false;

		if ( LayerManager.get_instance().other_uses_this_class_variable_as_different_class( classVariableObject._name, classVariableObject._jarFilename, classVariableObject._classname, _entityBase))
			return false;

		if ( _variableTableBase.other_objectBase_has_this_name( classVariableObject._kind, classVariableObject._name))
			return false;

		String[] property_pages = Constant.get_propertyPanelBases( "class variable");

		for ( int i = 0; i < property_pages.length; ++i) {
			PropertyPanelBase propertyPanelBase = _propertyPanelBaseMap.get( property_pages[ i]);
			if ( null != propertyPanelBase && propertyPanelBase.contains( classVariableObject._name))
				return false;
		}

		String[] kinds = Constant.get_kinds( "class variable");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], classVariableObject._name, _entityBase))
				return false;
		}

		return true;
	}
}
