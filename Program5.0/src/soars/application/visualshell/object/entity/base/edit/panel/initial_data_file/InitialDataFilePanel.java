/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.initial_data_file;

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
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.VariableTableBase;
import soars.application.visualshell.object.entity.base.object.base.ObjectBase;
import soars.application.visualshell.object.entity.base.object.initial_data_file.InitialDataFileObject;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.file.manager.FileManager;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextUndoRedoManager;

/**
 * @author kurata
 *
 */
public class InitialDataFilePanel extends PanelBase {

	/**
	 * 
	 */
	private FileManager _fileManager = null;

	/**
	 * 
	 */
	private JTextField _fileTextField = null;

	/**
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 * @param variableTableBase
	 * @param fileManager
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public InitialDataFilePanel(EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, VariableTableBase variableTableBase, FileManager fileManager, Color color, Frame owner, Component parent) {
		super("initial data file", entityBase, propertyPanelBaseMap, variableTableBase, color, owner, parent);
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
		setup_initialValueTextField( parent);
		insert_vertical_strut( parent);
		setup_commentTextField( parent);
		insert_vertical_strut( parent);
		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_initialValueTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.object.dialog.initial.data.file.table.header.file"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_fileTextField = !_entityBase.is_multi() ? new FileDDTextField() : new JTextField();
		_fileTextField.setEditable( false);
		//_fileTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters16));
		_fileTextField.setSelectionColor( _color);
		_fileTextField.setForeground( _color);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _fileTextField, this));
		_components.add( _fileTextField);
		panel.add( _fileTextField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_commentTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.object.dialog.initial.data.file.table.header.comment"));
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

//		_propertyPanelBaseMap.get( "variable").update();
	
		_fileManager.select( _fileTextField.getText());
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

//		_propertyPanelBaseMap.get( "variable").update();

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
			_fileTextField.setText( "");
			_commentTextField.setText( "");
		} else {
			InitialDataFileObject initialDataFileObject = ( InitialDataFileObject)objectBase;
			_fileTextField.setText( initialDataFileObject._name);
			_commentTextField.setText( initialDataFileObject._comment);
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

		_textUndoRedoManagers.add( new TextUndoRedoManager( _fileTextField, this));
		_textUndoRedoManagers.add( new TextUndoRedoManager( _commentTextField, this));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#clear()
	 */
	@Override
	public void clear() {
		_fileTextField.setText( "");
		_commentTextField.setText( "");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#is_empty()
	 */
	@Override
	protected boolean is_empty() {
		return ( _fileTextField.getText().equals( "") && _commentTextField.getText().equals( ""));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#create_and_get()
	 */
	@Override
	protected ObjectBase create_and_get() {
		InitialDataFileObject initialDataFileObject = new InitialDataFileObject();
		initialDataFileObject._name = _fileTextField.getText();
		initialDataFileObject._comment = _commentTextField.getText();
		return initialDataFileObject;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#can_get_data(soars.application.visualshell.object.entity.base.object.base.ObjectBase)
	 */
	@Override
	protected boolean can_get_data(ObjectBase objectBase) {
		// TODO
		InitialDataFileObject initialDataFileObject = ( InitialDataFileObject)objectBase;

//		if ( !Constant.is_correct_name( _nameTextField.getText())) {
//			JOptionPane.showMessageDialog( _parent,
//				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
//				ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
//				JOptionPane.ERROR_MESSAGE);
//			return false;
//		}
//
//		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( _nameTextField.getText()))
//			|| ( null != LayerManager.get_instance().get_spot_has_this_name( _nameTextField.getText()))) {
//			JOptionPane.showMessageDialog( _parent,
//				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
//				ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
//				JOptionPane.ERROR_MESSAGE);
//			return false;
//		}
//
//		if ( _entityBase instanceof AgentObject && ( _nameTextField.getText().equals( "$Name")
//			|| _nameTextField.getText().equals( "$Role") || _nameTextField.getText().equals( "$Spot"))) {
//			JOptionPane.showMessageDialog( _parent,
//				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
//				ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
//				JOptionPane.ERROR_MESSAGE);
//			return false;
//		}
//
//		if ( null == _fileTextField.getText()
//			|| 0 < _fileTextField.getText().indexOf( '$')
//			|| _fileTextField.getText().equals( "$")
//			|| _fileTextField.getText().startsWith( " ")
//			|| _fileTextField.getText().endsWith( " ")
//			|| _fileTextField.getText().equals( "$Name")
//			|| _fileTextField.getText().equals( "$Role")
//			|| _fileTextField.getText().equals( "$Spot")
//			|| 0 <= _fileTextField.getText().indexOf( Constant._experimentName)) {
//			JOptionPane.showMessageDialog( _parent,
//				ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
//				ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
//				JOptionPane.ERROR_MESSAGE);
//			return false;
//		}
//
//		if ( _fileTextField.getText().startsWith( "$")
//			&& ( 0 <= _fileTextField.getText().indexOf( " ")
//			|| 0 < _fileTextField.getText().indexOf( "$", 1)
//			|| 0 < _fileTextField.getText().indexOf( ")", 1))) {
//			JOptionPane.showMessageDialog( _parent,
//				ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
//				ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
//				JOptionPane.ERROR_MESSAGE);
//			return false;
//		}
//
//		// ファイルとしての名前チェックが必要！
//		if ( !_fileTextField.getText().startsWith( "$")) {
//			if ( _fileTextField.getText().startsWith( "/") || _fileTextField.getText().matches( ".*[/]{2,}.*")) {
//				JOptionPane.showMessageDialog( _parent,
//					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
//					ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
//					JOptionPane.ERROR_MESSAGE);
//				return false;
//			}
//
//			File user_data_directory = LayerManager.get_instance().get_user_data_directory();
//			if ( null == user_data_directory) {
//				JOptionPane.showMessageDialog( _parent,
//					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
//					ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
//					JOptionPane.ERROR_MESSAGE);
//				return false;
//			}
//
//			File file = new File( user_data_directory.getAbsolutePath() + "/" + _fileTextField.getText());
//			//if ( !file.exists())
//			//	return false;
//
//			if ( _fileTextField.getText().endsWith( "/") && !file.isDirectory()) {
//				JOptionPane.showMessageDialog( _parent,
//					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
//					ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
//					JOptionPane.ERROR_MESSAGE);
//				return false;
//			}
//		}
//
//
		if ( !_fileTextField.getText().equals( initialDataFileObject._name)) {
			if ( _variableTableBase.contains( _fileTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.initial.data.file.already.exists.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.initial.data.file"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
//
//		String[] propertyPanelBases = Constant.get_propertyPanelBases( "file");
//
//		for ( int i = 0; i < propertyPanelBases.length; ++i) {
//			PropertyPanelBase propertyPanelBase = _propertyPanelBaseMap.get( propertyPanelBases[ i]);
//			if ( null != propertyPanelBase && propertyPanelBase.contains( _nameTextField.getText())) {
//				JOptionPane.showMessageDialog( _parent,
//					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
//					ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
//					JOptionPane.ERROR_MESSAGE);
//				return false;
//			}
//		}
//
//		String[] kinds = Constant.get_kinds( "file");
//
//		for ( int i = 0; i < kinds.length; ++i) {
//			if ( LayerManager.get_instance().is_object_name( kinds[ i], _nameTextField.getText(), _entityBase)) {
//				JOptionPane.showMessageDialog( _parent,
//					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
//					ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
//					JOptionPane.ERROR_MESSAGE);
//				return false;
//			}
//		}
//
//		if ( null != LayerManager.get_instance().get_chart( _nameTextField.getText())) {
//			JOptionPane.showMessageDialog( _parent,
//				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
//				ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
//				JOptionPane.ERROR_MESSAGE);
//			return false;
//		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#get_data(soars.application.visualshell.object.entity.base.object.base.ObjectBase)
	 */
	@Override
	protected void get_data(ObjectBase objectBase) {
		// TODO
		InitialDataFileObject initialDataFileObject = ( InitialDataFileObject)objectBase;

//		if ( !fileObject._name.equals( "") && !_nameTextField.getText().equals( fileObject._name)) {
//
//			WarningManager.get_instance().cleanup();
//
//			_propertyPanelBaseMap.get( "variable").update_object_name( "file", fileObject._name, _nameTextField.getText());
//
//			boolean result = LayerManager.get_instance().update_object_name( "file", fileObject._name, _nameTextField.getText(), _entityBase);
//			if ( result) {
//				if ( _entityBase.update_object_name( "file", fileObject._name, _nameTextField.getText())) {
//					String[] message = new String[] {
//						( ( _entityBase instanceof AgentObject) ? "Agent : " : "Spot : ") + _entityBase._name
//					};
//
//					WarningManager.get_instance().add( message);
//				}
//
//				if ( _entityBase instanceof AgentObject)
//					Observer.get_instance().on_update_agent_object( "file", fileObject._name, _nameTextField.getText());
//				else if ( _entityBase instanceof SpotObject)
//					Observer.get_instance().on_update_spot_object( "file", fileObject._name, _nameTextField.getText());
//
//				Observer.get_instance().on_update_entityBase( true);
//				Observer.get_instance().modified();
//			}
//		}

		initialDataFileObject._name = _fileTextField.getText();
		initialDataFileObject._comment = _commentTextField.getText();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#can_paste(soars.application.visualshell.object.entity.base.object.base.ObjectBase, java.util.List)
	 */
	@Override
	public boolean can_paste(ObjectBase objectBase, List<ObjectBase> objectBases) {
		if ( !( objectBase instanceof InitialDataFileObject))
			return false;

		// TODO
		InitialDataFileObject initialDataFileObject = ( InitialDataFileObject)objectBase;
//
//		if ( !Constant.is_correct_name( fileObject._name))
//			return false;
//
//		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( fileObject._name))
//			|| ( null != LayerManager.get_instance().get_spot_has_this_name( fileObject._name)))
//			return false;
//
//		if ( _entityBase instanceof AgentObject && ( fileObject._name.equals( "$Name")
//			|| fileObject._name.equals( "$Role") || fileObject._name.equals( "$Spot")))
//			return false;
//
//		if ( null == fileObject._initialValue
//			|| 0 < fileObject._initialValue.indexOf( '$')
//			|| fileObject._initialValue.equals( "$")
//			|| fileObject._initialValue.startsWith( " ")
//			|| fileObject._initialValue.endsWith( " ")
//			|| fileObject._initialValue.equals( "$Name")
//			|| fileObject._initialValue.equals( "$Role")
//			|| fileObject._initialValue.equals( "$Spot")
//			|| 0 <= fileObject._initialValue.indexOf( Constant._experimentName))
//			return false;
//
//		if ( fileObject._initialValue.startsWith( "$")
//			&& ( 0 <= fileObject._initialValue.indexOf( " ")
//			|| 0 < fileObject._initialValue.indexOf( "$", 1)
//			|| 0 < fileObject._initialValue.indexOf( ")", 1)))
//			return false;
//
//		// ファイルとしての名前チェックが必要！
//		if ( !fileObject._initialValue.startsWith( "$")) {
//			if ( fileObject._initialValue.startsWith( "/") || fileObject._initialValue.matches( ".*[/]{2,}.*"))
//				return false;
//
		File user_data_directory = LayerManager.get_instance().get_user_data_directory();
		if ( null == user_data_directory)
			return false;

		File file = new File( user_data_directory.getAbsolutePath() + "/" + initialDataFileObject._name);
		if ( !file.exists())
			return false;
//
//			if ( fileObject._initialValue.endsWith( "/") && !file.isDirectory())
//				return false;
//		}
//
//
//		if ( _variableTableBase.other_objectBase_has_this_name( fileObject._kind, fileObject._name))
//			return false;
//
//		String[] propertyPanelBases = Constant.get_propertyPanelBases( "file");
//
//		for ( int i = 0; i < propertyPanelBases.length; ++i) {
//			PropertyPanelBase propertyPanelBase = _propertyPanelBaseMap.get( propertyPanelBases[ i]);
//			if ( null != propertyPanelBase && propertyPanelBase.contains( fileObject._name))
//				return false;
//		}
//
//		String[] kinds = Constant.get_kinds( "file");
//
//		for ( int i = 0; i < kinds.length; ++i) {
//			if ( LayerManager.get_instance().is_object_name( kinds[ i], fileObject._name, _entityBase))
//				return false;
//		}
//
//		if ( null != LayerManager.get_instance().get_chart( fileObject._name))
//			return false;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PanelBase#confirm(int, soars.application.visualshell.object.entity.base.object.base.ObjectBase, soars.application.visualshell.object.entity.base.object.base.ObjectBase)
	 */
	@Override
	public ObjectBase confirm(int row, ObjectBase targetObjectBase, ObjectBase selectedObjectBase) {
		// TODO
		if ( !isVisible())
			return null;

		// 編集状態でない場合
		if ( is_empty())
			return selectedObjectBase;	// 何もしない

		// 複数選択されているかまたは選択されているオブジェクトがこのオブジェクトと異なる場合
		if ( ( null == targetObjectBase)
			|| !ObjectBase.is_target( _kind, targetObjectBase)) {
			ObjectBase objectBase = ( ObjectBase)_variableTableBase.get( _kind, _fileTextField.getText());
			if ( null == objectBase) {
				// 同じ名前のものが無いので追加または無視を選択
				if ( JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.append.initial.data.file.confirm.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind.replaceAll(" ", ".")),
					JOptionPane.YES_NO_OPTION))
					return selectedObjectBase;	// 無視なら何もしない

				objectBase = create();
				if ( !can_get_data( objectBase))
					return null;		// 追加出来ないので選択を変えさせない

				// オブジェクトを追加
				get_data( objectBase);
				_variableTableBase.append( objectBase);
				update();
				return objectBase;
			} else {
				// 同じ名前のものがあるので更新または無視を選択
				if ( JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.update.initial.data.file.confirm.message") + " - " + objectBase._name,
					ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind.replaceAll(" ", ".")),
					JOptionPane.YES_NO_OPTION))
					return selectedObjectBase;	// 無視なら何もしない

				if ( !can_get_data( objectBase))
					return null;		// 更新出来ないので選択を変えさせない

				// 更新する行番号を取得
				row = _variableTableBase.get( objectBase);
				if ( 0 > row)		// これは起こり得ない筈だが念の為
					return null;	// 更新出来ないので選択を変えさせない

				// オブジェクトを更新
				get_data( objectBase);
				update( row, objectBase, false);
				update();
				return objectBase;
			}
		}

		ObjectBase objectBase = create_and_get();
		if ( objectBase.equals( targetObjectBase))
			return selectedObjectBase;	// 編集されていなければ何もしない


		if ( !targetObjectBase._name.equals( _fileTextField.getText())) {
			// 名前が異なるので追加、更新または無視を選択
			String[] options = new String[] {
				ResourceManager.get_instance().get( "edit.object.dialog.append.option"),
				ResourceManager.get_instance().get( "edit.object.dialog.update.option"),
				ResourceManager.get_instance().get( "edit.object.dialog.ignore.option")
			};
			int result = JOptionPane.showOptionDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.append.or.update.initial.data.file.confirm.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind.replaceAll(" ", ".")),
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE,
				null,
				options,
				options[ 0]);
			switch ( result) {
				case 0:		// 追加の場合
					if ( !can_get_data( objectBase))
						return null;		// 追加出来ないので選択を変えさせない

					// オブジェクトを追加
					get_data( objectBase);
					_variableTableBase.append( objectBase);
					update();
					return objectBase;
				case 1:		// 更新の場合
					if ( !can_get_data( targetObjectBase))
						return null;		// 更新出来ないので選択を変えさせない

					// オブジェクトを更新
					update( row, targetObjectBase, false);
					return targetObjectBase;
				default:	// 無視の場合
					return selectedObjectBase;	// 無視なら何もしない
			}
		} else {
			// 名前が同じなので更新または無視を選択
			if ( JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.update.initial.data.file.confirm.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind.replaceAll(" ", ".")),
				JOptionPane.YES_NO_OPTION))
				return selectedObjectBase;	// 無視なら何もしない

			if ( !can_get_data( targetObjectBase))
				return null;		// 更新出来ないので選択を変えさせない

			// オブジェクトを更新
			update( row, targetObjectBase, false);
			return targetObjectBase;
		}
	}

	/**
	 * @param srcPath
	 * @param destPath
	 */
	public void update_initial_value(File srcPath, File destPath) {
		// TODO
		String value = _fileTextField.getText();
		if ( value.equals( "") || value.startsWith( "$"))
			return;

		String srcValue = ( srcPath.getAbsolutePath().substring( LayerManager.get_instance().get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/");
		String destValue = ( destPath.getAbsolutePath().substring( LayerManager.get_instance().get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/");
		if ( !value.startsWith( srcValue))
			return;

		_fileTextField.setText( destValue + value.substring( srcValue.length()));
	}
}
