/*
 * 2005/01/28
 */
package soars.application.visualshell.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;

import soars.application.visualshell.common.file.ScriptFile;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.executor.common.Parameters;
import soars.application.visualshell.executor.modelbuilder.ModelBuilder;
import soars.application.visualshell.executor.modelbuilder.ParallelModelBuilder;
import soars.application.visualshell.executor.modelbuilder.SerialModelBuilder;
import soars.application.visualshell.executor.monitor.MonitorFrame;
import soars.application.visualshell.executor.monitor.MonitorPropertyPage;
import soars.application.visualshell.executor.simulator.ParallelSimulator;
import soars.application.visualshell.file.docker.DockerFilesetProperty;
import soars.application.visualshell.file.exporter.script.setting.EditExportSettingDlg;
import soars.application.visualshell.file.loader.SaxLoader;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.model.EditModelDlg;
import soars.application.visualshell.object.comment.CommentManager;
import soars.application.visualshell.object.docker.edit.EditDockerPropertyDlg;
import soars.application.visualshell.object.entity.base.edit.panel.base.VariableTableBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTable;
import soars.application.visualshell.object.experiment.ExperimentManager;
import soars.application.visualshell.object.experiment.edit.EditExperimentDlg;
import soars.application.visualshell.object.experiment.edit.EditExperimentForDockerDlg;
import soars.application.visualshell.object.experiment.edit.EditExperimentForModelBuilderDlg;
import soars.application.visualshell.object.experiment.edit.EditExperimentForSimulatorDlg;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.log.LogManager;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.scripts.OtherScriptsManager;
import soars.application.visualshell.object.simulation.SimulationManager;
import soars.application.visualshell.object.stage.StageManager;
import soars.application.visualshell.option.setting.EditApplicationSettingDlg;
import soars.application.visualshell.state.EditState;
import soars.application.visualshell.state.StateManager;
import soars.common.utility.swing.message.IIntMessageCallback;
import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.IObjectsMessageCallback;
import soars.common.utility.swing.message.IntMessageDlg;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.swing.message.ObjectsMessageDlg;
import soars.common.utility.swing.window.View;

/**
 * The Visual Shell view class.
 * @author kurata / SOARS project
 */
public class VisualShellView extends View implements DropTargetListener, IMessageCallback, IIntMessageCallback, IObjectsMessageCallback {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private VisualShellView _visualShellView = null;

	/**
	 * 
	 */
	private final Color _backgroundColor = new Color( 255, 255, 255);

	/**
	 * Returns the instance of the Visual Shell view class.
	 * @return the instance of the Visual Shell view class
	 */
	public static VisualShellView get_instance() {
		return _visualShellView;
	}

	/**
	 * Creates the instance of the Visual Shell view class.
	 * @param arg0 a boolean, true for double-buffering, which uses additional memory space to achieve fast, flicker-free updates
	 */
	public VisualShellView(boolean arg0) {
		super(arg0);
		setBackground( _backgroundColor);
		_visualShellView = this;
	}

	/**
	 * Returns true if the data is changed.
	 * @return true if the data is changed
	 */
	public boolean isModified() {
		return LayerManager.get_instance().isModified();
	}

	/**
	 * Returns true if the data file exists.
	 * @return true if the data file exists
	 */
	public boolean exist_datafile() {
		return LayerManager.get_instance().exist_datafile();
	}

	/**
	 * Cancels all selections of objects.
	 */
	public void cancel() {
		StateManager.get_instance().cancel();
	}

	/**
	 * @return
	 */
	private boolean setup() {
		if ( !setup_state_manager())
			return false;

		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_state_manager() {
		if ( !StateManager.get_instance().setup( ""))
			return false;

		EditState editState = new EditState();
		if ( !editState.setup())
			return false;

		StateManager.get_instance().put( ResourceManager.get_instance().get( "state.edit"), editState);

		StateManager.get_instance().change( ResourceManager.get_instance().get( "state.edit"));

		return true;
	}

	/**
	 * Resets all.
	 */
	public void reset() {
		StageManager.get_instance().cleanup();
		LayerManager.get_instance().cleanup();
		SimulationManager.get_instance().cleanup();
		LogManager.get_instance().cleanup();
		VisualShellExpressionManager.get_instance().cleanup();
		OtherScriptsManager.get_instance().cleanup();
		CommentManager.get_instance().cleanup();
		ExperimentManager.get_instance().cleanup();
		VariableTableBase.clear();
		InitialValueTable.clear();
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
		StateManager.get_instance().cleanup();
		reset();
	}

	/**
	 * 
	 */
	private void refresh() {
		StateManager.get_instance().refresh();
		EditRoleFrame.clear();
	}

	/**
	 * Invoked just before this component is destroyed.
	 */
	public void on_closing() {
		cleanup();
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		if ( !setup())
			return false;

		new DropTarget( this, this);

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_component_resized(java.awt.event.ComponentEvent)
	 */
	protected void on_component_resized(ComponentEvent componentEvent) {
		StateManager.get_instance().on_resized( componentEvent);
		super.on_component_resized(componentEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_mouse_left_double_click(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_double_click(MouseEvent mouseEvent) {
		StateManager.get_instance().on_mouse_left_double_click(mouseEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_mouse_left_down(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_down(MouseEvent mouseEvent) {
		StateManager.get_instance().on_mouse_left_down(mouseEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_mouse_left_up(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_up(MouseEvent mouseEvent) {
		StateManager.get_instance().on_mouse_left_up(mouseEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_mouse_dragged(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_dragged(MouseEvent mouseEvent) {
		StateManager.get_instance().on_mouse_dragged(mouseEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_mouse_right_down(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_right_down(MouseEvent mouseEvent) {
		StateManager.get_instance().on_mouse_right_down(mouseEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_mouse_right_up(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
		StateManager.get_instance().on_mouse_right_up(mouseEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.View#on_mouse_moved(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_moved(MouseEvent mouseEvent) {
		StateManager.get_instance().on_mouse_moved(mouseEvent);
	}

	/* (Non Javadoc)
	 * @see javax.swing.JComponent#getToolTipText(java.awt.event.MouseEvent)
	 */
	public String getToolTipText(MouseEvent arg0) {
		return StateManager.get_instance().get_tooltip_text( arg0);
	}

	/* (Non Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics arg0) {
		super.paintComponent( arg0);
		StateManager.get_instance().paint( arg0);
	}

	/**
	 * Invoked when the key is pressed.
	 * @param keyEvent An event which indicates that a keystroke occurred in a component
	 */
	public void on_key_pressed(KeyEvent keyEvent) {
		StateManager.get_instance().on_key_pressed( keyEvent);
	}

	/**
	 * Invoked when the key is released.
	 * @param keyEvent An event which indicates that a keystroke occurred in a component
	 */
	public void on_key_released(KeyEvent keyEvent) {
		StateManager.get_instance().on_key_released( keyEvent);
	}

	/**
	 * Inserts the new layer.
	 */
	public void on_insert_layer() {
		LayerManager.get_instance().insert( this);
	}

	/**
	 * Appends the new layer.
	 */
	public void on_append_layer() {
		LayerManager.get_instance().append( this);
	}

	/**
	 * Removes the current layer.
	 */
	public void on_remove_layer() {
		LayerManager.get_instance().remove( this);
		LayerManager.get_instance().update_name_dimension( ( Graphics2D)getGraphics());
	}

	/**
	 * @param layer
	 */
	protected void on_change_layer(int layer) {
		LayerManager.get_instance().change( layer, this);
	}

	/**
	 * Resets all.
	 */
	public void on_file_new() {
		reset();
		refresh();
		MainFrame.get_instance().adjust_layer( LayerManager.get_instance().size());
		setPreferredSize( new Dimension());
		updateUI();
		MainFrame.get_instance().setTitle( ResourceManager.get_instance().get( "application.title"));
	}

	/**
	 * Returns true if the data are loaded from the specified file successfully.
	 * @param file the specified file
	 * @return true if the data are loaded from the specified file successfully
	 */
	public boolean on_file_open(File file) {
		reset();
		refresh();
		MainFrame.get_instance().adjust_layer( 1);
		setPreferredSize( new Dimension());
		updateUI();

		Graphics2D graphics2D = ( Graphics2D)getGraphics();

		if ( !MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"on_file_open", ResourceManager.get_instance().get( "file.open.show.message"),
			new Object[] { file, graphics2D, this}, this, MainFrame.get_instance())) {
			graphics2D.dispose();
			reset();
			setPreferredSize( new Dimension());
			updateUI();
			return false;
		}

		graphics2D.dispose();

		StateManager.get_instance().reset();
		StateManager.get_instance().change( ResourceManager.get_instance().get( "state.edit"));

		MainFrame.get_instance().adjust_layer( LayerManager.get_instance().size());

		if ( SaxLoader._modified)
			LayerManager.get_instance().modified();
		else
			MainFrame.get_instance().setTitle(
				ResourceManager.get_instance().get( "application.title")
				+ " - [" + ( CommentManager.get_instance()._title.equals( "") ? ResourceManager.get_instance().get( "application.no.title") : CommentManager.get_instance()._title) + "]"
				+ ( ( null == file) ? "" : " <" + file.getName() + ">"));

		repaint();

		return true;
	}

	/**
	 * Closes the current opened file.
	 */
	public void on_file_close() {
		reset();
		refresh();
		MainFrame.get_instance().adjust_layer( LayerManager.get_instance().size());
		setPreferredSize( new Dimension());
		updateUI();
		MainFrame.get_instance().setTitle( ResourceManager.get_instance().get( "application.title"));
	}

	/**
	 * Returns true if the current data are stored to the current selected file successfully.
	 * @return true if the current data are stored to the current selected file successfully
	 */
	public boolean on_file_save() {
		StateManager.get_instance().cancel();

		boolean result = MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"on_file_save", ResourceManager.get_instance().get( "file.save.show.message"), this, MainFrame.get_instance());

		MainFrame.get_instance().setTitle(
			ResourceManager.get_instance().get( "application.title")
			+ " - [" + ( CommentManager.get_instance()._title.equals( "") ? ResourceManager.get_instance().get( "application.no.title") : CommentManager.get_instance()._title) + "]"
			+ ( ( null == LayerManager.get_instance().get_current_file()) ? "" : " <" + LayerManager.get_instance().get_current_file().getName() + ">"));

		return result;
	}

	/**
	 * Returns true if the current data are stored to the specified file successfully.
	 * @param file the specified file
	 * @return true if the current data are stored to the specified file successfully
	 */
	public boolean on_file_save_as(File file) {
		boolean result = MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"on_file_save_as", ResourceManager.get_instance().get( "file.saveas.show.message"), new Object[] { file}, this, MainFrame.get_instance());

		if ( result)
			MainFrame.get_instance().setTitle(
				ResourceManager.get_instance().get( "application.title")
				+ " - [" + ( CommentManager.get_instance()._title.equals( "") ? ResourceManager.get_instance().get( "application.no.title") : CommentManager.get_instance()._title) + "]"
				+ ( ( null == file) ? "" : " <" + file.getName() + ">"));

		return result;
	}

	/**
	 * Returns true if the data are loaded from the specified script file successfully.
	 * @param file the specified script file
	 * @return true if the data are loaded from the specified script file successfully
	 */
	public boolean on_file_import(File file) {
		reset();
		refresh();
		MainFrame.get_instance().adjust_layer( 1);
		setPreferredSize( new Dimension());
		updateUI();

		Graphics2D graphics2D = ( Graphics2D)getGraphics();

		if ( !MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"on_file_import", ResourceManager.get_instance().get( "file.import.show.message"), 
			new Object[] { file, graphics2D, this}, this, MainFrame.get_instance())) {
			graphics2D.dispose();
			reset();
			setPreferredSize( new Dimension());
			updateUI();
			return false;
		}

		graphics2D.dispose();

		MainFrame.get_instance().setTitle( ResourceManager.get_instance().get( "application.title"));

		StateManager.get_instance().reset();
		StateManager.get_instance().change( ResourceManager.get_instance().get( "state.edit"));

		repaint();

		return true;
	}

	/**
	 * Returns 1 if the data are loaded from the specified initial data file successfully.
	 * @param file the specified initial data file
	 * @param warning whether to show the warning messsages
	 * @param all whether to load all of the data
	 * @return 1 if the data are loaded from the specified initial data file successfully
	 */
	public int on_file_import_initial_data(File file, boolean warning, boolean all) {
		int result = IntMessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"on_file_import_initial_data", ResourceManager.get_instance().get( "file.import.initial.data.show.message"),
			new Object[] { file, Boolean.valueOf( warning), Boolean.valueOf( all), this}, ( IIntMessageCallback)this, MainFrame.get_instance());

		if ( -1 == result) {
			reset();
			setPreferredSize( new Dimension());
			updateUI();
			return result;
		}

		StateManager.get_instance().reset();
		StateManager.get_instance().change( ResourceManager.get_instance().get( "state.edit"));

		if ( 1 == result)
			LayerManager.get_instance().modified();

		repaint();

		return result;
	}

	/**
	 * Returns 1 if the data are loaded from the clipboard successfully.
	 * @param warning whether to show the warning messsages
	 * @param all whether to load all of the data
	 * @return 1 if the data are loaded from the clipboard successfully
	 */
	public int on_import_initial_data(boolean warning, boolean all) {
		int result = LayerManager.get_instance().import_initial_data( warning, all, this);

		if ( -1 == result) {
			reset();
			setPreferredSize( new Dimension());
			updateUI();
			return result;
		}

		StateManager.get_instance().reset();
		StateManager.get_instance().change( ResourceManager.get_instance().get( "state.edit"));

		if ( 1 == result)
			LayerManager.get_instance().modified();

		repaint();

		return result;
	}

	/**
	 * Returns true if the data are stored to the specified initial data file successfully.
	 * @param file the specified initial data file
	 * @param all whether to store all of the data
	 * @return true if the data are stored to the specified initial data file successfully
	 */
	public boolean on_file_export_initial_data(File file, boolean all) {
		return MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"on_file_export_initial_data", ResourceManager.get_instance().get( "file.export.initial.data.show.message"),
			new Object[] { file, Boolean.valueOf( all)}, this, MainFrame.get_instance());
	}

	/**
	 * Returns true for exporting the ModelBuilder script to the specified file in CSV format successfully.
	 * @param file the specified file
	 * @param toDisplay whether to display the logs
	 * @param toFile whether to make the log files
	 * @return true for exporting the ModelBuilder script to the specified file in CSV format successfully
	 */
	public boolean on_file_export(File file, boolean toDisplay, boolean toFile) {
		return MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"on_file_export", ResourceManager.get_instance().get( "file.export.show.message"),
			new Object[] { file, Boolean.valueOf( toDisplay), Boolean.valueOf( toFile)}, this, MainFrame.get_instance());
	}

	/**
	 * Exports the ModelBuilder scripts to the specified files in CSV format, and returns them.
	 * @param toDisplay whether to display the logs
	 * @param toFile whether to make the log files
	 * @return the ModelBuilder scripts
	 */
	public ScriptFile[] on_file_export(/*boolean animator, */boolean toDisplay, boolean toFile) {
		return ( ScriptFile[])ObjectsMessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"on_file_export", ResourceManager.get_instance().get( "file.export.show.message"),
			new Object[] { /*Boolean.valueOf( animator), */Boolean.valueOf( toDisplay), Boolean.valueOf( toFile)}, ( IObjectsMessageCallback)this, MainFrame.get_instance());
	}

	/**
	 * Exports the ModelBuilder scripts to the specified files in CSV format, and returns them.
	 * @param toDisplay whether to display the logs
	 * @param toFile whether to make the log files
	 * @param experimentManager the experiment support manager
	 * @return the ModelBuilder scripts 
	 */
	public ScriptFile[] on_file_export(/*boolean animator, */boolean toDisplay, boolean toFile, ExperimentManager experimentManager) {
		return ( ScriptFile[])ObjectsMessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"on_experiment_file_export", ResourceManager.get_instance().get( "file.export.show.message"),
			new Object[] { /*Boolean.valueOf( animator), */Boolean.valueOf( toDisplay), Boolean.valueOf( toFile), experimentManager}, ( IObjectsMessageCallback)this, MainFrame.get_instance());
	}

	/**
	 * @param dockerFilesetProperty 
	 * @param dockerFilesetFile
	 * @param parent 
	 * @return
	 */
	public boolean on_file_create_docker_fileset(DockerFilesetProperty dockerFilesetProperty, File dockerFilesetFile, Component parent) {
		// TODO Auto-generated method stub
		return MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"on_file_create_docker_fileset1", ResourceManager.get_instance().get( "file.create.docker.fileset.show.message"),
			new Object[] { dockerFilesetProperty, dockerFilesetFile}, this, parent);
	}

	/**
	 * @param dockerFilesetProperty 
	 * @param dockerFilesetFile
	 * @param experimentManager
	 * @param parent 
	 * @return
	 */
	public boolean on_file_create_docker_fileset(DockerFilesetProperty dockerFilesetProperty, File dockerFilesetFile, ExperimentManager experimentManager, Component parent) {
		// TODO Auto-generated method stub
		return MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"on_file_create_docker_fileset2", ResourceManager.get_instance().get( "file.create.docker.fileset.show.message"),
			new Object[] { dockerFilesetProperty, dockerFilesetFile, experimentManager}, this, parent);
	}

	/**
	 * Exports the ModelBuilder script to the clipboard in CSV format.
	 * @param toDisplay whether to display the logs
	 * @param toFile whether to make the log files
	 */
	public void on_file_export_to_clipboard(boolean toDisplay, boolean toFile) {
		MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"on_file_export_to_clipboard", ResourceManager.get_instance().get( "file.export.to.clipboard.show.message"),
			new Object[] { Boolean.valueOf( toDisplay), Boolean.valueOf( toFile)}, this, MainFrame.get_instance());
	}

	/**
	 * @return
	 */
	public boolean copy_objects() {
		return MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"on_copy_objects", ResourceManager.get_instance().get( "edit.popup.menu.copy.objects.show.message"), this, MainFrame.get_instance());
	}

	/**
	 * Returns true for pasting the objects successfully.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 * @return true for pasting the objects successfully
	 */
	public boolean paste_objects() {
		return MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"on_paste_objects", ResourceManager.get_instance().get( "edit.popup.menu.paste.objects.show.message"),
			new Object[] { ( Graphics2D)getGraphics(), this}, this, MainFrame.get_instance());
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IMessageCallback#message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.MessageDlg)
	 */
	public boolean message_callback(String id, Object[] objects, MessageDlg messageDlg) {
		if ( id.equals( "on_file_open"))
			return LayerManager.get_instance().load(
				( File)objects[ 0],
				( Graphics2D)objects[ 1],
				( JComponent)objects[ 2]);
		else if ( id.equals( "on_file_save"))
			return LayerManager.get_instance().save();
		else if ( id.equals( "on_file_save_as"))
			return LayerManager.get_instance().save_as(
				( File)objects[ 0]);
		else if ( id.equals( "on_file_import"))
			return LayerManager.get_instance().import_data(
				( File)objects[ 0],
				( Graphics2D)objects[ 1],
				( JComponent)objects[ 2]);
		else if ( id.equals( "on_file_export_initial_data"))
			return LayerManager.get_instance().export_data_initial_data(
				( File)objects[ 0],
				( ( Boolean)objects[ 1]).booleanValue());
		else if ( id.equals( "on_file_export"))
			return LayerManager.get_instance().export_data(
				( File)objects[ 0],
				( ( Boolean)objects[ 1]).booleanValue(),
				( ( Boolean)objects[ 2]).booleanValue());
		else if ( id.equals( "on_file_export_to_clipboard"))
			LayerManager.get_instance().export_data_to_clipboard(
				( ( Boolean)objects[ 0]).booleanValue(),
				( ( Boolean)objects[ 1]).booleanValue());
		else if ( id.equals( "on_copy_objects"))
			return LayerManager.get_instance().copy_objects();
		else if ( id.equals( "on_paste_objects"))
			return LayerManager.get_instance().paste_objects(
				( Graphics2D)objects[ 0], ( JComponent)objects[ 1]);
		// TODO Auto-generated method stub
		else if ( id.equals( "on_file_create_docker_fileset1"))
			return LayerManager.get_instance().create_docker_fileset(
				( DockerFilesetProperty)objects[ 0],
				( File)objects[ 1]);
		// TODO Auto-generated method stub
		else if ( id.equals( "on_file_create_docker_fileset2"))
			return LayerManager.get_instance().create_docker_fileset(
				( DockerFilesetProperty)objects[ 0],
				( File)objects[ 1],
				( ExperimentManager)objects[ 2]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IIntMessageCallback#int_message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.IntMessageDlg)
	 */
	public int int_message_callback(String id, Object[] objects, IntMessageDlg intMessageDlg) {
		if ( id.equals( "on_file_import_initial_data"))
			return LayerManager.get_instance().import_initial_data(
				( File)objects[ 0],
				( ( Boolean)objects[ 1]).booleanValue(),
				( ( Boolean)objects[ 2]).booleanValue(),
				( JComponent)objects[ 3]);

		return 0;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IObjectsMessageCallback#objects_message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.ObjectsMessageDlg)
	 */
	public Object[] objects_message_callback(String id, Object[] objects, ObjectsMessageDlg objectsMessageDlg) {
		if ( id.equals( "on_file_export"))
			return LayerManager.get_instance().export_data(
				( ( Boolean)objects[ 0]).booleanValue(),
				( ( Boolean)objects[ 1]).booleanValue());
//				( ( Boolean)objects[ 0]).booleanValue(),
//				( ( Boolean)objects[ 1]).booleanValue(),
//				( ( Boolean)objects[ 2]).booleanValue());
		else if ( id.equals( "on_experiment_file_export"))
			return LayerManager.get_instance().export_data(
				( ( Boolean)objects[ 0]).booleanValue(),
				( ( Boolean)objects[ 1]).booleanValue(),
				( ExperimentManager)objects[ 2]);
//				( ( Boolean)objects[ 0]).booleanValue(),
//				( ( Boolean)objects[ 1]).booleanValue(),
//				( ( Boolean)objects[ 2]).booleanValue(),
//				( ExperimentManager)objects[ 3]);

		return null;
	}

	/**
	 * Returns true for appending a new object to the current layer.
	 * @param type the object type
	 * @param point the object position
	 * @return true for appending a new object to the current layer
	 */
	public boolean append_object(String type, Point point) {
		return LayerManager.get_instance().append_object( type, point, this);
	}

	/**
	 * Edits the stages.
	 */
	public void on_setting_stage() {
		StateManager.get_instance().cancel();
		EditModelDlg editModelDlg = new EditModelDlg( MainFrame.get_instance(),
			ResourceManager.get_instance().get( "edit.setting.dialog.title"),
			true, 0);
		editModelDlg.do_modal();
	}

	/**
	 * Edits the simulation settings.
	 */
	public void on_setting_simulation() {
		StateManager.get_instance().cancel();
		EditModelDlg editModelDlg = new EditModelDlg( MainFrame.get_instance(),
			ResourceManager.get_instance().get( "edit.setting.dialog.title"),
			true, 1);
		editModelDlg.do_modal();
	}

	/**
	 * 
	 */
	public void on_setting_expression() {
		StateManager.get_instance().cancel();
		EditModelDlg editModelDlg = new EditModelDlg( MainFrame.get_instance(),
			ResourceManager.get_instance().get( "edit.setting.dialog.title"),
			true, 2);
		editModelDlg.do_modal();
	}

	/**
	 * Edits the log1 settings.
	 */
	public void on_setting_log1() {
		StateManager.get_instance().cancel();
		EditModelDlg editModelDlg = new EditModelDlg( MainFrame.get_instance(),
			ResourceManager.get_instance().get( "edit.setting.dialog.title"),
			true, 3);
		editModelDlg.do_modal();
	}

	/**
	 * Edits the log2 settings.
	 */
	public void on_setting_log2() {
		StateManager.get_instance().cancel();
		EditModelDlg editModelDlg = new EditModelDlg( MainFrame.get_instance(),
			ResourceManager.get_instance().get( "edit.setting.dialog.title"),
			true, 4);
		editModelDlg.do_modal();
	}

	/**
	 * Edits the comment settings.
	 */
	public void on_setting_comment() {
		StateManager.get_instance().cancel();
		EditModelDlg editModelDlg = new EditModelDlg( MainFrame.get_instance(),
			ResourceManager.get_instance().get( "edit.setting.dialog.title"),
			true, 5);
		editModelDlg.do_modal();
	}

	/**
	 * Edits the experiment data.
	 */
	public void on_setting_experiment() {
		StateManager.get_instance().cancel();
		EditExperimentDlg editExperimentDlg = new EditExperimentDlg( MainFrame.get_instance(),
			ResourceManager.get_instance().get( "edit.experiment.dialog.title"),
			true,
			ExperimentManager.get_instance());
		editExperimentDlg.do_modal();
	}

	/**
	 * Starts the Model Builder.
	 */
	public void on_start_model_builder() {
		if ( !LayerManager.get_instance().is_initial_data_file_correct())
			return;

		ScriptFile[] scriptFiles = null;
//		if ( ExperimentManager.get_instance().isEmpty()) {
//		if ( 0 == ExperimentManager.get_instance().get_initial_value_count()) {
		if ( !ExperimentManager.get_instance().initial_value_exists()) {
			EditExportSettingDlg editExportSettingDlg = new EditExportSettingDlg( MainFrame.get_instance(),
				ResourceManager.get_instance().get( "file.export.to.soars.engine.menu"), true, false);
			if ( !editExportSettingDlg.do_modal( MainFrame.get_instance()))
				return;

			scriptFiles = on_file_export( /*false, */true,
				Environment.get_instance().get( Environment._editExportSettingDialogToFileKey, "false").equals( "true"));
			if ( null == scriptFiles || 0 == scriptFiles.length)
				return;

		} else {
			ExperimentManager experimentManager = new ExperimentManager( ExperimentManager.get_instance());
			EditExperimentForSimulatorDlg editExperimentForSimulatorDlg = new EditExperimentForSimulatorDlg( MainFrame.get_instance(),
				ResourceManager.get_instance().get( "file.export.to.soars.engine.menu"),
				true,
				experimentManager);
			if ( !editExperimentForSimulatorDlg.do_modal())
				return;

			scriptFiles = on_file_export( /*false, */true,
				Environment.get_instance().get( Environment._editExportSettingDialogToFileKey, "false").equals( "true"),
				experimentManager);
			if ( null == scriptFiles || 0 == scriptFiles.length)
				return;

		}

		ModelBuilder.start( scriptFiles);
	}

	/**
	 * Runs the Model Builder.
	 */
	public void on_run_model_builder() {
		if ( !LayerManager.get_instance().is_initial_data_file_correct())
			return;

		ExperimentManager experimentManager = null;
		ScriptFile[] scriptFiles = null;
//		if ( ExperimentManager.get_instance().isEmpty()) {
//		if ( 0 == ExperimentManager.get_instance().get_initial_value_count()) {
		if ( !ExperimentManager.get_instance().initial_value_exists()) {
			experimentManager = ExperimentManager.get_instance();
			EditExportSettingDlg editExportSettingDlg = new EditExportSettingDlg( MainFrame.get_instance(),
				ResourceManager.get_instance().get( "run.model.builder.menu"), true, true);
			if ( !editExportSettingDlg.do_modal( MainFrame.get_instance()))
				return;

			scriptFiles = on_file_export( /*false, */false,
				Environment.get_instance().get( Environment._editExportSettingDialogToFileKey, "false").equals( "true"));
			if ( null == scriptFiles || 0 == scriptFiles.length)
				return;

		} else {
			experimentManager = new ExperimentManager( ExperimentManager.get_instance());
			EditExperimentForModelBuilderDlg editExperimentForModelBuilderDlg = new EditExperimentForModelBuilderDlg( MainFrame.get_instance(),
				ResourceManager.get_instance().get( "run.model.builder.menu"),
				//ResourceManager.get_instance().get( "edit.experiment.dialog.title"),
				true,
				experimentManager);
			if ( !editExperimentForModelBuilderDlg.do_modal())
				return;

			scriptFiles = on_file_export( /*false, */false,
				Environment.get_instance().get( Environment._editExportSettingDialogToFileKey, "false").equals( "true"),
				experimentManager);
			if ( null == scriptFiles || 0 == scriptFiles.length)
				return;

		}

		MonitorFrame monitorFrame = MonitorFrame.get_instance();
		File currentFile = LayerManager.get_instance().get_current_file();

		if ( 1 < scriptFiles.length && experimentManager._parallel) {
			for ( int i = 0; i < scriptFiles.length; ++i) {
				MonitorPropertyPage monitorPropertyPage = monitorFrame.append( get_title( scriptFiles, i), scriptFiles[ i]._logFolderPath);
				ParallelModelBuilder.execute( scriptFiles[ i], monitorPropertyPage);
			}
		} else {
			MonitorPropertyPage monitorPropertyPage = monitorFrame.append( get_title(), "");
			SerialModelBuilder.execute( scriptFiles, monitorPropertyPage);
//			MonitorPropertyPage[] monitorPropertyPages = new MonitorPropertyPage[ scriptFiles.length];
//			for ( int i = 0; i < scriptFiles.length; ++i) {
//				monitorPropertyPages[ i] = monitorFrame.append( get_title( scriptFiles, i), scriptFiles[ i]._log_folder_path);
//			}
//			SerialModelBuilder.execute( scriptFiles, monitorPropertyPages);
		}
	}

	/**
	 * @param scriptFiles
	 * @param index
	 * @return
	 */
	private String get_title(ScriptFile[] scriptFiles, int index) {
		String title = ( scriptFiles[ index]._experimentName.equals( "") ? "" : ( scriptFiles[ index]._experimentName + " - "));

		File currentFile = LayerManager.get_instance().get_current_file();
		title += ( ( null == currentFile) 	? ResourceManager.get_instance().get( "run.monitor.tab.title.no.name")
			: currentFile.getName()) + ( ( 2 > scriptFiles.length) ? "" : ( "[" + String.valueOf( index + 1) + "]"));

		return title;
	}

	/**
	 * @return
	 */
	private String get_title() {
		File currentFile = LayerManager.get_instance().get_current_file();
		return ( ( null == currentFile) 	? ResourceManager.get_instance().get( "run.monitor.tab.title.no.name") : currentFile.getName());
	}

	/**
	 * Runs the Simulator.
	 * @param direct
	 */
	public void on_run_simulator(boolean direct) {
		if ( !LayerManager.get_instance().is_initial_data_file_correct())
			return;

		ScriptFile[] scriptFiles = null;
//		if ( ExperimentManager.get_instance().isEmpty()) {
//		if ( 0 == ExperimentManager.get_instance().get_initial_value_count()) {
		if ( !ExperimentManager.get_instance().initial_value_exists()) {
			EditExportSettingDlg editExportSettingDlg = new EditExportSettingDlg( MainFrame.get_instance(),
				ResourceManager.get_instance().get( "run.simulator.menu"), true, false);
			if ( !editExportSettingDlg.do_modal( MainFrame.get_instance()))
				return;

			scriptFiles = on_file_export( /*false, */true,
				Environment.get_instance().get( Environment._editExportSettingDialogToFileKey, "false").equals( "true"));
			if ( null == scriptFiles || 0 == scriptFiles.length)
				return;
		} else {
			ExperimentManager experimentManager = new ExperimentManager( ExperimentManager.get_instance());
			EditExperimentForSimulatorDlg editExperimentForSimulatorDlg = new EditExperimentForSimulatorDlg( MainFrame.get_instance(),
				ResourceManager.get_instance().get( "run.simulator.menu"),
				//ResourceManager.get_instance().get( "edit.experiment.dialog.title"),
				true,
				experimentManager);
			if ( !editExperimentForSimulatorDlg.do_modal())
				return;

			scriptFiles = on_file_export( /*false, */true,
				Environment.get_instance().get( Environment._editExportSettingDialogToFileKey, "false").equals( "true"),
				experimentManager);
			if ( null == scriptFiles || 0 == scriptFiles.length)
				return;
		}

		String graphicProperties = LayerManager.get_instance().get_graphic_properties();

		String chartProperties = LayerManager.get_instance().get_chart_properties(); 

		for ( int i = 0; i < scriptFiles.length; ++i) {
			Parameters parameters = new Parameters();
			if ( !parameters.setup( scriptFiles[ i], graphicProperties, chartProperties))
				continue;

			ParallelSimulator.execute( scriptFiles[ i], parameters, direct);
		}
	}

	/**
	 * @param direct
	 */
	public void on_file_create_docker_fileset(boolean direct) {
		// TODO Auto-generated method stub
		if ( !LayerManager.get_instance().is_initial_data_file_correct())
			return;

//		if ( ExperimentManager.get_instance().isEmpty()) {
//		if ( 0 == ExperimentManager.get_instance().get_initial_value_count()) {
		if ( !ExperimentManager.get_instance().initial_value_exists()) {
			EditDockerPropertyDlg editDockerPropertiesDlg = new EditDockerPropertyDlg(
				MainFrame.get_instance(),
				ResourceManager.get_instance().get( "create.docker.fileset.dialog.title"),
				true);
			editDockerPropertiesDlg.do_modal( this);
		} else {
			ExperimentManager em = ExperimentManager.get_instance();
			ExperimentManager experimentManager = new ExperimentManager( ExperimentManager.get_instance());
			EditExperimentForDockerDlg editExperimentForDockerDlg = new EditExperimentForDockerDlg(
				MainFrame.get_instance(),
				ResourceManager.get_instance().get( "create.docker.fileset.dialog.title"),
				true,
				experimentManager);
			editExperimentForDockerDlg.do_modal();
		}
	}

	/**
	 * @return
	 */
	public File get_docker_fileset_file() {
		// TODO Auto-generated method stub
		File file = CommonTool.get_save_file( Environment._dockerFilesetCreatorFolderKey, ResourceManager.get_instance().get( "create.docker.fileset.file.dialog.title"), new String[] { "zip"}, "Docker fileset file", this);
		if ( null == file)
			return null;

		String filePath = file.getAbsolutePath();
		String filename = file.getName();
		int index = filename.lastIndexOf( '.');
		if ( 0 > index)
			file = new File( filePath + ".zip");
		else if ( filename.length() - 1 == index)
			file = new File( filePath + "zip");

		return file;
	}

	/**
	 * Edits the application settings.
	 */
	public void on_application_setting() {
		StateManager.get_instance().cancel();
		EditApplicationSettingDlg editApplicationSettingDlg = new EditApplicationSettingDlg( MainFrame.get_instance(),
			ResourceManager.get_instance().get( "edit.application.setting.dialog.title"),
			true);
		editApplicationSettingDlg.do_modal();
	}

	/**
	 * Invoked when the "About SOARS Visual Shell" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_help_about(ActionEvent actionEvent) {
		StateManager.get_instance().cancel();
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent arg0) {
		arg0.acceptDrag( DnDConstants.ACTION_COPY);
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent arg0) {
		arg0.acceptDrag( DnDConstants.ACTION_COPY_OR_MOVE);
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent arg0) {
		arg0.acceptDrag( DnDConstants.ACTION_COPY);
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent arg0) {
		try {
			Transferable transferable = arg0.getTransferable();
//			if ( transferable.isDataFlavorSupported( DataFlavor.javaFileListFlavor)) {
//				arg0.acceptDrop( DnDConstants.ACTION_COPY_OR_MOVE);
//				List list = ( List)transferable.getTransferData( DataFlavor.javaFileListFlavor);
//				if ( list.isEmpty()) {
//					arg0.getDropTargetContext().dropComplete( true);
//					return;
//				}
//
//				File file = ( File)list.get( 0);
//				arg0.getDropTargetContext().dropComplete( true);
//				MainFrame.get_instance().on_file_open_by_drag_and_drop( file);
//			} else if ( transferable.isDataFlavorSupported( DataFlavor.stringFlavor)) {
			if ( transferable.isDataFlavorSupported( DataFlavor.stringFlavor)) {
				arg0.acceptDrop( DnDConstants.ACTION_COPY_OR_MOVE);
				String string = ( String)transferable.getTransferData( DataFlavor.stringFlavor);
				arg0.getDropTargetContext().dropComplete( true);
				if ( string.equals( "agent")
					|| string.equals( "spot")
					|| string.equals( "agent_role")
					|| string.equals( "spot_role")
					|| string.equals( "chart")) {
					append_object( string, arg0.getLocation());
					repaint();
//				} else {
//					String[] files = string.split( System.getProperty( "line.separator"));
//					if ( files.length <= 0)
//						arg0.rejectDrop();
//					else
//						MainFrame.get_instance().on_file_open_by_drag_and_drop( new File( new URI( files[ 0].replaceAll( "[\r\n]", ""))));
				}
			} else {
				arg0.rejectDrop();
			}
		} catch (IOException ioe) {
			arg0.rejectDrop();
		} catch (UnsupportedFlavorException ufe) {
			arg0.rejectDrop();
//		} catch (URISyntaxException e) {
//			arg0.rejectDrop();
		}
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent arg0) {
	}
}
