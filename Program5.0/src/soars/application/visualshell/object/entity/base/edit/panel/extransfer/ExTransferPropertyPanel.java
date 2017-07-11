/*
 * 2005/06/02
 */
package soars.application.visualshell.object.entity.base.edit.panel.extransfer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Image;
import java.awt.SystemColor;
import java.io.File;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.object.base.ObjectBase;
import soars.application.visualshell.object.entity.base.object.extransfer.ExTransferObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.observer.Observer;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.file.manager.FileManager;
import soars.common.utility.swing.file.manager.IFileManager;
import soars.common.utility.swing.file.manager.IFileManagerCallBack;
import soars.common.utility.swing.file.manager.edit.FileEditorDlg;
import soars.common.utility.tool.resource.Resource;

/**
 * @author kurata
 */
public class ExTransferPropertyPanel extends PropertyPanelBase implements IFileManagerCallBack {

	/**
	 * 
	 */
	private JSplitPane _splitPane = null;

	/**
	 * 
	 */
	private ExTransferTable _exTransferTable = null;

	/**
	 * 
	 */
	private ExTransferPanel _exTransferPanel = null;

	/**
	 * 
	 */
	private FileManager _fileManager = null;

	/**
	 * @param title
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public ExTransferPropertyPanel(String title, EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, int index, Frame owner, Component parent) {
		super(title, entityBase, propertyPanelBaseMap, index, owner, parent);
	}

	/**
	 * 
	 */
	protected void set_property_to_environment_file() {
		Environment.get_instance().set(
			Environment._editObjectDialogExTransferPropertyPanelDividerLocationKey, String.valueOf( _splitPane.getDividerLocation()));
		Environment.get_instance().set(
			Environment._editObjectDialogExTransferFileManagerDividerLocationKey, String.valueOf( _fileManager.getDividerLocation()));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase#contains(java.lang.String)
	 */
	@Override
	public boolean contains(String name) {
		return _exTransferTable.contains( name);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase#contains(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean contains(String name, String number) {
		return _exTransferTable.contains(name, number);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase#get()
	 */
	@Override
	public String[] get() {
		return _exTransferTable.get();
	}

	/**
	 * @param contains_empty
	 * @return
	 */
	public String[] get_agent_file_names(boolean contains_empty) {
		return _exTransferTable.get_agent_file_names( contains_empty);
	}

	/**
	 * @param contains_empty
	 * @return
	 */
	public String[] get_file_names(boolean contains_empty) {
		return _exTransferTable.get_file_names( contains_empty);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase#on_create()
	 */
	@Override
	protected boolean on_create() {
		if ( !super.on_create())
			return false;


		create_fileTable();
		create_fileManager();


		setLayout( new BorderLayout());


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));


		_splitPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT);


		if ( !setup1())
			return false;


		if ( !setup2())
			return false;


		_splitPane.setDividerLocation( Integer.parseInt( Environment.get_instance().get( Environment._editObjectDialogExTransferPropertyPanelDividerLocationKey, "250")));


		centerPanel.add( _splitPane);


		insert_vertical_strut( centerPanel);


		add( centerPanel);


		adjust();


		return true;
	}

	/**
	 * 
	 */
	private void create_fileTable() {
		_exTransferTable = new ExTransferTable( _entityBase, _propertyPanelBaseMap, this, _owner, _parent);
	}

	/**
	 * 
	 */
	private void create_fileManager() {
		_fileManager = new FileManager( this, _owner, _parent);
	}

	/**
	 * @return
	 */
	private boolean setup1() {
		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BorderLayout());


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( centerPanel);

		if ( !setup_fileTable( centerPanel))
			return false;

		basePanel.add( centerPanel);


		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		if ( !setup_components( southPanel))
			return false;

		basePanel.add( southPanel, "South");


		_splitPane.setTopComponent( basePanel);


		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_fileTable(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		if ( !_exTransferTable.setup())
			return false;

		if ( _entityBase.is_multi())
			_exTransferTable.setEnabled( false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _exTransferTable);
		scrollPane.getViewport().setOpaque( true);
		scrollPane.getViewport().setBackground( SystemColor.text);

		panel.add( scrollPane);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_components(JPanel parent) {
		_exTransferPanel = new ExTransferPanel( _entityBase, _propertyPanelBaseMap, _exTransferTable, _fileManager, SystemColor.textText, _owner, _parent);
		if ( !_exTransferPanel.setup())
			return false;

		_panelBaseMap.put( ResourceManager.get_instance().get( "edit.object.dialog.tree.extransfer"), _exTransferPanel);
		parent.add( _exTransferPanel);

		return true;
	}


	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase#changeSelection(soars.application.visualshell.object.entity.base.object.base.ObjectBase)
	 */
	@Override
	public void changeSelection(ObjectBase objectBase) {
		changeSelection( ( ExTransferObject)objectBase, true);
	}

	/**
	 * @param exTransferObject
	 * @param update_all
	 */
	public void changeSelection(ExTransferObject exTransferObject, boolean update_all) {
		_exTransferPanel.update( exTransferObject);

		if ( !update_all)
			return;

		if ( null == exTransferObject)
			return;

		// FileManagerの表示も更新する必要がある
		_fileManager.select( exTransferObject._initialValue.startsWith( "$") ? "" : exTransferObject._initialValue);
	}

	/**
	 * @return
	 */
	private boolean setup2() {
		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BorderLayout());


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		if ( !setup_fileManager( centerPanel))
			return false;

		basePanel.add( centerPanel);


		_splitPane.setBottomComponent( basePanel);


		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_fileManager(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		if ( !_fileManager.setup( false, LayerManager.get_instance().get_user_data_directory()))
			return false;

		if ( _entityBase.is_multi())
			_fileManager.select( "");

		_fileManager.setDividerLocation( Integer.parseInt( Environment.get_instance().get( Environment._editObjectDialogExTransferFileManagerDividerLocationKey, "100")));

		panel.add( _fileManager);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_start_paste()
	 */
	@Override
	public void on_start_paste() {
		WarningManager.get_instance().cleanup();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_start_paste_and_remove()
	 */
	@Override
	public void on_start_paste_and_remove() {
		WarningManager.get_instance().cleanup();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#can_copy(java.io.File)
	 */
	@Override
	public boolean can_copy(File file) {
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#can_paste(java.io.File)
	 */
	@Override
	public boolean can_paste(File file) {
		if ( 0 > file.getAbsolutePath().indexOf( "$"))
			return true;

		if ( WarningManager.get_instance().size() < 100)
			WarningManager.get_instance().add( new String[] { file.getAbsolutePath()});

		return false;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_paste_completed()
	 */
	@Override
	public void on_paste_completed() {
		warn( ResourceManager.get_instance().get( "warning.dialog1.message4"));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_paste_and_remove_completed()
	 */
	@Override
	public void on_paste_and_remove_completed() {
		warn( ResourceManager.get_instance().get( "warning.dialog1.message5"));
	}

	/**
	 * @param message
	 */
	private void warn(String message) {
		if ( WarningManager.get_instance().isEmpty())
			return;

		WarningDlg1 warningDlg1 = new WarningDlg1(
			_owner,
			ResourceManager.get_instance().get( "warning.dialog1.title"),
			message,
			_parent);
		warningDlg1.do_modal();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#modified(soars.common.utility.swing.file.manager.IFileManager)
	 */
	public void modified(IFileManager fileManager) {
		// 対象のファイルシステム内で変更があった場合に呼び出される
		_propertyPanelBaseMap.get( "file").refresh();							// 他のFileManagerを更新
		_propertyPanelBaseMap.get( "initial data file").refresh();	// 他のFileManagerを更新
		Observer.get_instance().modified();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase#refresh()
	 */
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		_fileManager.refresh();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#select_changed(soars.common.utility.swing.file.manager.IFileManager)
	 */
	@Override
	public void select_changed(IFileManager fileManager) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#can_remove(java.io.File)
	 */
	@Override
	public boolean can_remove(File file) {
		// TODO Auto-generated method stub
		// ファイルを削除する際に呼び出される
		// 削除可能ならtrueを、そうでなければfalseを返す必要がある
		return ( !_exTransferTable.uses_this_file( file)
			&& !_propertyPanelBaseMap.get( "file").uses_this_file( file)
			&& !_propertyPanelBaseMap.get( "initial data file").uses_this_file( file));
			//&& !LayerManager.get_instance().uses_this_file( file, "extransfer"));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase#uses_this_file(java.io.File)
	 */
	@Override
	public boolean uses_this_file(File file) {
		// TODO Auto-generated method stub
		return _exTransferTable.uses_this_file( file);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_move(java.io.File, java.io.File)
	 */
	@Override
	public void on_move(File srcPath, File destPath) {
		// TODO Auto-generated method stub
		// ファイルが移動された時に元のファイルと移動されたファイルを受け取る
		_exTransferPanel.update_initial_value( srcPath, destPath);
		_propertyPanelBaseMap.get( "file").update_file( srcPath, destPath);
		_propertyPanelBaseMap.get( "initial data file").update_file( srcPath, destPath);
		_exTransferTable.move_file( srcPath, destPath);
		_propertyPanelBaseMap.get( "file").move_file( srcPath, destPath);
		_propertyPanelBaseMap.get( "initial data file").move_file( srcPath, destPath);
		if ( LayerManager.get_instance().move_file( srcPath, destPath))
			Observer.get_instance().modified();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase#move_file(java.io.File, java.io.File)
	 */
	@Override
	public void move_file(File srcPath, File destPath) {
		// TODO Auto-generated method stub
		_exTransferTable.move_file( srcPath, destPath);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase#update_file(java.io.File, java.io.File)
	 */
	@Override
	public void update_file(File srcPath, File destPath) {
		_exTransferPanel.update_initial_value( srcPath, destPath);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_select(java.io.File, java.lang.String)
	 */
	@Override
	public void on_select(File file, String encoding) {
		if ( !file.exists() || !file.isFile() || !file.canRead() || !file.canWrite())
			return;

		Image image = Resource.load_image_from_resource( Constant._resourceDirectory + "/image/icon/icon.png", getClass());
		if ( null == image)
			return;

		FileEditorDlg fileEditorDlg = new FileEditorDlg( _owner, file.getName(), true, image, file, Environment.get_instance(), Environment._fileEditorWindowRectangleKey, encoding, _fileManager, this);
		fileEditorDlg.do_modal( _fileManager);
//			fileEditorFrame.setIconImage( image);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#on_rename(java.io.File, java.io.File)
	 */
	@Override
	public void on_rename(File originalFile, File newFile) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#get_export_directory()
	 */
	@Override
	public File get_export_directory() {
		String exportDirectory = "";
		File directory = null;
		
		String value = Environment.get_instance().get( Environment._exportFilesDirectoryKey, "");
		if ( null != value && !value.equals( "")) {
			directory = new File( value);
			if ( directory.exists())
				exportDirectory = value;
		}
	
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle( ResourceManager.get_instance().get( "edit.object.dialog.export.files.title"));
		fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);

		if ( !exportDirectory.equals( "")) {
			fileChooser.setCurrentDirectory( new File( exportDirectory + "/../"));
			fileChooser.setSelectedFile( directory);
		}

		if ( JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog( _fileManager)) {
			directory = fileChooser.getSelectedFile();
			exportDirectory = directory.getAbsolutePath();
			Environment.get_instance().set( Environment._exportFilesDirectoryKey, exportDirectory);
			return directory;
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.file.manager.IFileManagerCallBack#visible(java.io.File)
	 */
	@Override
	public boolean visible(File file) {
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase#adjust()
	 */
	@Override
	protected void adjust() {
		_exTransferPanel.adjust();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase#on_setup_completed()
	 */
	@Override
	public void on_setup_completed() {
		if ( _entityBase.is_multi()) {
			_exTransferTable.setEnabled( false);
			_exTransferPanel.setEnabled( false);
		} else {
			_fileManager.on_setup_completed();
			_exTransferTable.on_setup_completed();
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase#on_ok()
	 */
	@Override
	public boolean on_ok() {
		if ( isVisible() && !confirm( false))
			return false;;

		_exTransferTable.on_ok();

		if ( _entityBase instanceof AgentObject)
			Observer.get_instance().on_update_agent_object( "extransfer");
		else if ( _entityBase instanceof SpotObject)
			Observer.get_instance().on_update_spot_object( "extransfer");

		set_property_to_environment_file();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase#on_cancel()
	 */
	@Override
	public void on_cancel() {
		set_property_to_environment_file();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase#confirm(boolean)
	 */
	@Override
	public boolean confirm(boolean fromTree) {
		if ( !isVisible())
			return true;

		//if ( 0 == _fileTable.getRowCount())
		//	return true;

		if ( !_exTransferPanel.confirm( fromTree))
			return false;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase#confirm(int, soars.application.visualshell.object.entity.base.object.base.ObjectBase, soars.application.visualshell.object.entity.base.object.base.ObjectBase)
	 */
	@Override
	public ObjectBase confirm(int row, ObjectBase targetObjectBase, ObjectBase selectedObjectBase) {
		return _exTransferPanel.confirm( row, targetObjectBase, selectedObjectBase);
	}
}
