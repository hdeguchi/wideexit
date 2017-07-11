/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.extransfer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
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
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.VariableTableBase;
import soars.application.visualshell.object.entity.base.object.base.ObjectBase;
import soars.application.visualshell.object.entity.base.object.extransfer.ExTransferObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.observer.Observer;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.file.manager.FileManager;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextUndoRedoManager;

/**
 * @author kurata
 *
 */
public class ExTransferPanel extends PanelBase {

	/**
	 * 
	 */
	private FileManager _fileManager = null;

	/**
	 * 
	 */
	private JTextField _initialValueTextField = null;

	/**
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 * @param variableTableBase
	 * @param fileManager
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public ExTransferPanel(EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, VariableTableBase variableTableBase, FileManager fileManager, Color color, Frame owner, Component parent) {
		super("extransfer", entityBase, propertyPanelBaseMap, variableTableBase, color, owner, parent);
		_fileManager = fileManager;
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
		setup_initialValueTextField( parent);
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
	private void setup_initialValueTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.object.dialog.extransfer.table.header.initial.value"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_initialValueTextField = !_entityBase.is_multi() ? new FileDDTextField() : new JTextField();
		_initialValueTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters16));
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
	private void setup_commentTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.object.dialog.extransfer.table.header.comment"));
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
	
		_fileManager.select( _initialValueTextField.getText());
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
			_initialValueTextField.setText( "");
			_commentTextField.setText( "");
		} else {
			ExTransferObject exTransferObject = ( ExTransferObject)objectBase;
			_nameTextField.setText( exTransferObject._name);
			_initialValueTextField.setText( exTransferObject._initialValue);
			_commentTextField.setText( exTransferObject._comment);
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
		ExTransferObject exTransferObject = new ExTransferObject();
		exTransferObject._name = _nameTextField.getText();
		exTransferObject._initialValue = _initialValueTextField.getText();
		exTransferObject._comment = _commentTextField.getText();
		return exTransferObject;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#can_get_data(soars.application.visualshell.object.entity.base.object.base.ObjectBase)
	 */
	@Override
	protected boolean can_get_data(ObjectBase objectBase) {
		ExTransferObject exTransferObject = ( ExTransferObject)objectBase;

		if ( !Constant.is_correct_name( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.extransfer"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( _nameTextField.getText()))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( _nameTextField.getText()))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.extransfer"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( _entityBase instanceof AgentObject && ( _nameTextField.getText().equals( "$Name")
			|| _nameTextField.getText().equals( "$Role") || _nameTextField.getText().equals( "$Spot"))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.extransfer"),
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
				ResourceManager.get_instance().get( "edit.object.dialog.tree.extransfer"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( _initialValueTextField.getText().startsWith( "$")
			&& ( 0 <= _initialValueTextField.getText().indexOf( " ")
			|| 0 < _initialValueTextField.getText().indexOf( "$", 1)
			|| 0 < _initialValueTextField.getText().indexOf( ")", 1))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.extransfer"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		// ファイルとしての名前チェックが必要！
		if ( !_initialValueTextField.getText().startsWith( "$")) {
			if ( _initialValueTextField.getText().startsWith( "/") || _initialValueTextField.getText().matches( ".*[/]{2,}.*")) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.extransfer"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}

			File user_data_directory = LayerManager.get_instance().get_user_data_directory();
			if ( null == user_data_directory) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.extransfer"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}

			File file = new File( user_data_directory.getAbsolutePath() + "/" + _initialValueTextField.getText());
			//if ( !file.exists())
			//	return false;

			if ( _initialValueTextField.getText().endsWith( "/") && !file.isDirectory()) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.extransfer"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}


		if ( !_nameTextField.getText().equals( exTransferObject._name)) {
			if ( _variableTableBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.extransfer"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] property_pages = Constant.get_propertyPanelBases( "extransfer");

		for ( int i = 0; i < property_pages.length; ++i) {
			PropertyPanelBase propertyPanelBase = _propertyPanelBaseMap.get( property_pages[ i]);
			if ( null != propertyPanelBase && propertyPanelBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.extransfer"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] kinds = Constant.get_kinds( "extransfer");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], _nameTextField.getText(), _entityBase)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.extransfer"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if ( null != LayerManager.get_instance().get_chart( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.extransfer"),
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
		ExTransferObject exTransferObject = ( ExTransferObject)objectBase;

		if ( !exTransferObject._name.equals( "") && !_nameTextField.getText().equals( exTransferObject._name)) {

			WarningManager.get_instance().cleanup();

			_propertyPanelBaseMap.get( "variable").update_object_name( "extransfer", exTransferObject._name, _nameTextField.getText());

			boolean result = LayerManager.get_instance().update_object_name( "extransfer", exTransferObject._name, _nameTextField.getText(), _entityBase);
			if ( result) {
				if ( _entityBase.update_object_name( "extransfer", exTransferObject._name, _nameTextField.getText())) {
					String[] message = new String[] {
						( ( _entityBase instanceof AgentObject) ? "Agent : " : "Spot : ") + _entityBase._name
					};

					WarningManager.get_instance().add( message);
				}

				if ( _entityBase instanceof AgentObject)
					Observer.get_instance().on_update_agent_object( "extransfer", exTransferObject._name, _nameTextField.getText());
				else if ( _entityBase instanceof SpotObject)
					Observer.get_instance().on_update_spot_object( "extransfer", exTransferObject._name, _nameTextField.getText());

				Observer.get_instance().on_update_entityBase( true);
				Observer.get_instance().modified();
			}
		}

		exTransferObject._name = _nameTextField.getText();
		exTransferObject._initialValue = _initialValueTextField.getText();
		exTransferObject._comment = _commentTextField.getText();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#can_paste(soars.application.visualshell.object.entity.base.object.base.ObjectBase, java.util.List)
	 */
	@Override
	public boolean can_paste(ObjectBase objectBase, List<ObjectBase> objectBases) {
		if ( !( objectBase instanceof ExTransferObject))
			return false;

		ExTransferObject exTransferObject = ( ExTransferObject)objectBase;

		if ( !Constant.is_correct_name( exTransferObject._name))
			return false;

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( exTransferObject._name))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( exTransferObject._name)))
			return false;

		if ( _entityBase instanceof AgentObject && ( exTransferObject._name.equals( "$Name")
			|| exTransferObject._name.equals( "$Role") || exTransferObject._name.equals( "$Spot")))
			return false;

		if ( null == exTransferObject._initialValue
			|| 0 < exTransferObject._initialValue.indexOf( '$')
			|| exTransferObject._initialValue.equals( "$")
			|| exTransferObject._initialValue.startsWith( " ")
			|| exTransferObject._initialValue.endsWith( " ")
			|| exTransferObject._initialValue.equals( "$Name")
			|| exTransferObject._initialValue.equals( "$Role")
			|| exTransferObject._initialValue.equals( "$Spot")
			|| 0 <= exTransferObject._initialValue.indexOf( Constant._experimentName))
			return false;

		if ( exTransferObject._initialValue.startsWith( "$")
			&& ( 0 <= exTransferObject._initialValue.indexOf( " ")
			|| 0 < exTransferObject._initialValue.indexOf( "$", 1)
			|| 0 < exTransferObject._initialValue.indexOf( ")", 1)))
			return false;

		// ファイルとしての名前チェックが必要！
		if ( !exTransferObject._initialValue.startsWith( "$")) {
			if ( exTransferObject._initialValue.startsWith( "/") || exTransferObject._initialValue.matches( ".*[/]{2,}.*"))
				return false;

			File user_data_directory = LayerManager.get_instance().get_user_data_directory();
			if ( null == user_data_directory)
				return false;

			File file = new File( user_data_directory.getAbsolutePath() + "/" + exTransferObject._initialValue);
			//if ( !file.exists())
			//	return false;

			if ( exTransferObject._initialValue.endsWith( "/") && !file.isDirectory())
				return false;
		}


		if ( _variableTableBase.other_objectBase_has_this_name( exTransferObject._kind, exTransferObject._name))
			return false;

		String[] property_pages = Constant.get_propertyPanelBases( "extransfer");

		for ( int i = 0; i < property_pages.length; ++i) {
			PropertyPanelBase propertyPanelBase = _propertyPanelBaseMap.get( property_pages[ i]);
			if ( null != propertyPanelBase && propertyPanelBase.contains( exTransferObject._name))
				return false;
		}

		String[] kinds = Constant.get_kinds( "extransfer");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], exTransferObject._name, _entityBase))
				return false;
		}

		if ( null != LayerManager.get_instance().get_chart( exTransferObject._name))
			return false;

		return true;
	}

	/**
	 * @param srcPath
	 * @param destPath
	 */
	public void update_initial_value(File srcPath, File destPath) {
		// TODO Auto-generated method stub
		String value = _initialValueTextField.getText();
		if ( value.equals( "") || value.startsWith( "$"))
			return;

		String srcValue = ( srcPath.getAbsolutePath().substring( LayerManager.get_instance().get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/");
		String destValue = ( destPath.getAbsolutePath().substring( LayerManager.get_instance().get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/");
		if ( !value.startsWith( srcValue))
			return;

		_initialValueTextField.setText( destValue + value.substring( srcValue.length()));
	}
}
