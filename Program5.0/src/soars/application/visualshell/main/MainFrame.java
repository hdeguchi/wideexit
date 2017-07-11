/*
 * 2005/01/27
 */
package soars.application.visualshell.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import soars.application.visualshell.common.menu.basic1.EditAction;
import soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1;
import soars.application.visualshell.common.menu.basic1.RemoveAction;
import soars.application.visualshell.common.menu.basic2.CopyAction;
import soars.application.visualshell.common.menu.basic2.DeselectAllAction;
import soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2;
import soars.application.visualshell.common.menu.basic2.PasteAction;
import soars.application.visualshell.common.menu.basic2.SelectAllAction;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.executor.modelbuilder.ParallelModelBuilder;
import soars.application.visualshell.executor.modelbuilder.SerialModelBuilder;
import soars.application.visualshell.executor.monitor.MonitorFrame;
import soars.application.visualshell.file.common.EditInitialDataSelectionDlg;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.menu.edit.CopyObjectsAction;
import soars.application.visualshell.menu.edit.FlushBottomAction;
import soars.application.visualshell.menu.edit.FlushLeftAction;
import soars.application.visualshell.menu.edit.FlushRightAction;
import soars.application.visualshell.menu.edit.FlushTopAction;
import soars.application.visualshell.menu.edit.HorizontalEqualLayoutAction;
import soars.application.visualshell.menu.edit.IEditMenuHandler;
import soars.application.visualshell.menu.edit.MoveAction;
import soars.application.visualshell.menu.edit.PasteObjectsAction;
import soars.application.visualshell.menu.edit.SwapNamesAction;
import soars.application.visualshell.menu.edit.VerticalEqualLayoutAction;
import soars.application.visualshell.menu.file.CreateDockerFilesetAction;
import soars.application.visualshell.menu.file.ExitAction;
import soars.application.visualshell.menu.file.ExportInitialDataAction;
import soars.application.visualshell.menu.file.ExportToClipboardAction;
import soars.application.visualshell.menu.file.ExportToFileAction;
import soars.application.visualshell.menu.file.ExportToSoarsEngineAction;
import soars.application.visualshell.menu.file.ImportGisDataAction;
import soars.application.visualshell.menu.file.ImportInitialDataAction;
import soars.application.visualshell.menu.file.OpenGisDataAction;
import soars.application.visualshell.menu.file.SaveAction;
import soars.application.visualshell.menu.help.AboutAction;
import soars.application.visualshell.menu.help.ContentsAction;
import soars.application.visualshell.menu.help.DocumentAction;
import soars.application.visualshell.menu.help.ForumAction;
import soars.application.visualshell.menu.layer.AppendLayerAction;
import soars.application.visualshell.menu.layer.InsertLayerAction;
import soars.application.visualshell.menu.layer.RemoveLayerAction;
import soars.application.visualshell.menu.option.ApplicationSettingAction;
import soars.application.visualshell.menu.run.ModelBuilderAction;
import soars.application.visualshell.menu.run.SimulatorAction;
import soars.application.visualshell.menu.setting.CommentAction;
import soars.application.visualshell.menu.setting.ExperimentAction;
import soars.application.visualshell.menu.setting.ExpressionAction;
import soars.application.visualshell.menu.setting.Log1Action;
import soars.application.visualshell.menu.setting.Log2Action;
import soars.application.visualshell.menu.setting.SimulationAction;
import soars.application.visualshell.menu.setting.StageAction;
import soars.application.visualshell.menu.view.ShowMonitorAction;
import soars.application.visualshell.object.base.DrawObject;
import soars.application.visualshell.object.experiment.ExperimentManager;
import soars.application.visualshell.object.experiment.edit.EditExperimentForGeneticAlgorithmDlg;
import soars.application.visualshell.object.experiment.edit.EditExperimentForGridDlg;
import soars.application.visualshell.object.gis.edit.GisDataFrame;
import soars.application.visualshell.object.gis.file.importer.ImportGisDataDlg;
import soars.application.visualshell.object.role.base.edit.inheritance.ConnectObject;
import soars.application.visualshell.observer.WarningDlg1;
import soars.application.visualshell.plugin.PluginManager;
import soars.application.visualshell.state.StateManager;
import soars.application.visualshell.toolbox.ButtonTransferHandler;
import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.dnd.button.DDButton;
import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.mac.IMacScreenMenuHandler;
import soars.common.utility.swing.mac.MacUtility;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Frame;
import soars.common.utility.tool.network.BrowserLauncher;

/**
 * The Visual Shell main window class.
 * @author kurata / SOARS project
 */
public class MainFrame extends Frame implements IMacScreenMenuHandler, IBasicMenuHandler1, IBasicMenuHandler2, IEditMenuHandler {

	/**
	 * Default width.
	 */
	static public final int _minimumWidth = 800;

	/**
	 * Default height.
	 */
	static public final int _minimumHeight = 600;

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private MainFrame _mainFrame = null;

	/**
	 * 
	 */
	private Rectangle _windowRectangle = new Rectangle();

	/**
	 * 
	 */
	private UserInterface _userInterface = null;

//	/**
//	 * 
//	 */
//	private JMenuItem _fileNewMenuItem = null;
//
//	/**
//	 * 
//	 */
//	private JMenuItem _fileOpenMenuItem = null;
//
//	/**
//	 * 
//	 */
//	private JMenuItem _fileCloseMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _fileSaveMenuItem = null;

//	/**
//	 * 
//	 */
//	private JMenuItem _fileSaveAsMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _fileExportToSoarsEngineMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _fileExportToFileMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _fileExportToClipboardMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _fileImportInitialDataMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _fileExportInitialDataMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _fileOpenGisDataMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _fileImportGisDataMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _fileCreateDockerFilesetMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editEditMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editRemoveMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editMoveMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editCopyMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editPasteMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editSelectAllMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editDeselectAllMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editFlushTopMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editFlushBottomMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editFlushLeftMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editFlushRightMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editVerticalEqualLayoutMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editHorizontalEqualLayoutMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editSwapNamesMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editCopyObjectsMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editPasteObjectsMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _settingStageMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _settingSimulationMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _settingExpressionMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _settingLog1MenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _settingLog2MenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _settingCommentMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _settingExperimentMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _viewShowMonitorMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _runModelBuilderMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _runSimulatorMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _optionApplicationSettingMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _helpDocumentMenuItem = null;

	/**
	 * 
	 */
	private Map<String, Boolean> _menuTextMap = new HashMap<String, Boolean>();

//	/**
//	 * 
//	 */
//	private JButton _fileNewButton = null;
//
//	/**
//	 * 
//	 */
//	private JButton _fileOpenButton = null;
//
//	/**
//	 * 
//	 */
//	private JButton _fileCloseButton = null;

	/**
	 * 
	 */
	private JButton _fileSaveButton = null;

//	/**
//	 * 
//	 */
//	private JButton _fileSaveAsButton = null;

//	/**
//	 * 
//	 */
//	private JButton _fileImportInitialDataButton = null;
//
//	/**
//	 * 
//	 */
//	private JButton _fileExportInitialDataButton = null;

	/**
	 * 
	 */
	public JButton _editCopyButton = null;

	/**
	 * 
	 */
	public JButton _editPasteButton = null;

	/**
	 * 
	 */
	public JButton _editRemoveButton = null;

	/**
	 * 
	 */
	private JButton _settingStageButton = null;

	/**
	 * 
	 */
	private JButton _settingSimulationButton = null;

	/**
	 * 
	 */
	private JButton _settingExpressionButton = null;

	/**
	 * 
	 */
	private JButton _settingLog1Button = null;

	/**
	 * 
	 */
	private JButton _settingLog2Button = null;

	/**
	 * 
	 */
	private JButton _settingCommentButton = null;

	/**
	 * 
	 */
	private JButton _settingExperimentButton = null;

	/**
	 * 
	 */
	private JButton _runModelBuilderButton = null;

	/**
	 * 
	 */
	private JButton _runSimulatorButton = null;

	/**
	 * 
	 */
	private JComboBox _layerComboBox = null;

	/**
	 * 
	 */
	private JButton _layerInsertButton = null;

	/**
	 * 
	 */
	private JButton _layerAppendButton = null;

	/**
	 * 
	 */
	private JButton _layerRemoveButton = null;

	/**
	 * 
	 */
	private boolean _synchronize = true;

	/**
	 * 
	 */
	private DDButton _toolboxAgentButton = null;

	/**
	 * 
	 */
	private DDButton _toolboxSpotButton = null;

	/**
	 * 
	 */
	private DDButton _toolboxAgentRoleButton = null;

	/**
	 * 
	 */
	private DDButton _toolboxSpotRoleButton = null;

	/**
	 * 
	 */
	private DDButton _toolboxChartButton = null;

	/**
	 * 
	 */
	private JLabel _messageLabel = null;

	/**
	 * 
	 */
	private JLabel _informationLabel = null;

	/**
	 * 
	 */
	private boolean _direct = false;

	/**
	 * Returns the instance of this class.
	 * @return the instance of this class
	 */
	public static MainFrame get_instance() {
		synchronized( _lock) {
			if ( null == _mainFrame) {
				_mainFrame = new MainFrame( ResourceManager.get_instance().get( "application.title"));
			}
		}
		return _mainFrame;
	}

	/**
	 * Creates the instance of the Visual Shell main window class.
	 * @param arg0 the title to be displayed in the frame's border. A null value is treated as an empty string, ""
	 * @throws HeadlessException Thrown when code that is dependent on a keyboard, display, or mouse is called in an environment that does not support a keyboard, display, or mouse
	 */
	public MainFrame(String arg0) throws HeadlessException {
		super(arg0);
	}

	/**
	 * 
	 */
	private void get_property_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._mainWindowRectangleKey + "x",
			String.valueOf( SwingTool.get_default_window_position( _minimumWidth, _minimumHeight).x));
		_windowRectangle.x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._mainWindowRectangleKey + "y",
			String.valueOf( SwingTool.get_default_window_position( _minimumWidth, _minimumHeight).y));
		_windowRectangle.y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._mainWindowRectangleKey + "width",
			String.valueOf( _minimumWidth));
		_windowRectangle.width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._mainWindowRectangleKey + "height",
			String.valueOf( _minimumHeight));
		_windowRectangle.height = Integer.parseInt( value);
	}

	/**
	 * @return
	 */
	private void optimize_window_rectangle() {
		if ( !GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersects( _windowRectangle)
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( _windowRectangle).width <= 10
			|| _windowRectangle.y <= -getInsets().top
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( _windowRectangle).height <= getInsets().top)
			_windowRectangle.setBounds(
				SwingTool.get_default_window_position( _minimumWidth, _minimumHeight).x,
				SwingTool.get_default_window_position( _minimumWidth, _minimumHeight).y,
				_minimumWidth, _minimumHeight);
	}

	/**
	 * 
	 */
	private void set_property_to_environment_file() throws IOException {
		Environment.get_instance().set(
			Environment._mainWindowRectangleKey + "x", String.valueOf( _windowRectangle.x));
		Environment.get_instance().set(
			Environment._mainWindowRectangleKey + "y", String.valueOf( _windowRectangle.y));
		Environment.get_instance().set(
			Environment._mainWindowRectangleKey + "width", String.valueOf( _windowRectangle.width));
		Environment.get_instance().set(
			Environment._mainWindowRectangleKey + "height", String.valueOf( _windowRectangle.height));

		Environment.get_instance().store();
	}

	/**
	 * 
	 */
	private void setup_menu() {
		JToolBar statusBar = new JToolBar();

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		_messageLabel = new JLabel( "");
		//statusBar.add( _message_label);
		panel.add( _messageLabel);
		statusBar.add( panel);

		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));
		//panel.setLayout( new BorderLayout());

		//_information_label = new JLabel( "");
		_informationLabel = new JLabel( "                                             ");
		_informationLabel.setHorizontalAlignment( Label.RIGHT);
		//statusBar.add( _information_label);
		panel.add( _informationLabel);
		statusBar.add( panel);

		statusBar.setFloatable( false);

		//statusBar.setEnabled( false);

		getContentPane().add( statusBar, BorderLayout.SOUTH);




		JMenuBar menuBar = new JMenuBar();


		JMenu menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "file.menu"),
			true,
			ResourceManager.get_instance().get( "file.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.message"));


//		NewAction newAction = new NewAction( ResourceManager.get_instance().get( "file.new.menu"));
//		_fileNewMenuItem = _userInterface.append_menuitem(
//			menu,
//			ResourceManager.get_instance().get( "file.new.menu"),
//			newAction,
//			ResourceManager.get_instance().get( "file.new.mnemonic"),
//			ResourceManager.get_instance().get( "file.new.stroke"),
//			_messageLabel,
//			ResourceManager.get_instance().get( "file.new.message"));
//		_menuTextMap.put( _fileNewMenuItem.getText(), new Boolean( true));
//
//
//		OpenAction openAction = new OpenAction( ResourceManager.get_instance().get( "file.open.menu"));
//		_fileOpenMenuItem = _userInterface.append_menuitem(
//			menu,
//			ResourceManager.get_instance().get( "file.open.menu"),
//			openAction,
//			ResourceManager.get_instance().get( "file.open.mnemonic"),
//			ResourceManager.get_instance().get( "file.open.stroke"),
//			_messageLabel,
//			ResourceManager.get_instance().get( "file.open.message"));
//		_menuTextMap.put( _fileOpenMenuItem.getText(), new Boolean( true));
//
//
//		menu.addSeparator();
//
//
//		CloseAction closeAction = new CloseAction( 	ResourceManager.get_instance().get( "file.close.menu"));
//		_fileCloseMenuItem = _userInterface.append_menuitem(
//			menu,
//			ResourceManager.get_instance().get( "file.close.menu"),
//			closeAction,
//			ResourceManager.get_instance().get( "file.close.mnemonic"),
//			ResourceManager.get_instance().get( "file.close.stroke"),
//			_messageLabel,
//			ResourceManager.get_instance().get( "file.close.message"));
//		_fileCloseMenuItem.setEnabled( false);
//		_menuTextMap.put( _fileCloseMenuItem.getText(), new Boolean( true));
//
//
//		menu.addSeparator();


		SaveAction saveAction = new SaveAction( ResourceManager.get_instance().get( "file.save.menu"));
		_fileSaveMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.save.menu"),
			saveAction,
			ResourceManager.get_instance().get( "file.save.mnemonic"),
			ResourceManager.get_instance().get( "file.save.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.save.message"));
		_fileSaveMenuItem.setEnabled( false);
		_menuTextMap.put( _fileSaveMenuItem.getText(), new Boolean( true));


//		SaveAsAction saveAsAction = new SaveAsAction( ResourceManager.get_instance().get( "file.saveas.menu"));
//		_fileSaveAsMenuItem = _userInterface.append_menuitem(
//			menu,
//			ResourceManager.get_instance().get( "file.saveas.menu"),
//			saveAsAction,
//			ResourceManager.get_instance().get( "file.saveas.mnemonic"),
//			ResourceManager.get_instance().get( "file.saveas.stroke"),
//			_messageLabel,
//			ResourceManager.get_instance().get( "file.saveas.message"));
//		//_file_save_as_menuItem.setEnabled( false);
//		_menuTextMap.put( _fileSaveAsMenuItem.getText(), new Boolean( true));


		menu.addSeparator();


		JMenu subMenu = _userInterface.append_menu(
			menu,
			ResourceManager.get_instance().get( "file.soars.engine.menu"),
			true,
			ResourceManager.get_instance().get( "file.soars.engine.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.soars.engine.message"));


		ExportToSoarsEngineAction exportToSosrsEngineAction = new ExportToSoarsEngineAction( ResourceManager.get_instance().get( "file.export.to.soars.engine.menu"));
		_fileExportToSoarsEngineMenuItem = _userInterface.append_menuitem(
			subMenu,
			ResourceManager.get_instance().get( "file.export.to.soars.engine.menu"),
			exportToSosrsEngineAction,
			ResourceManager.get_instance().get( "file.export.to.soars.engine.mnemonic"),
			ResourceManager.get_instance().get( "file.export.to.soars.engine.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.export.to.soars.engine.message"));
		_menuTextMap.put( _fileExportToSoarsEngineMenuItem.getText(), new Boolean( true));


		ExportToFileAction exportToFileAction = new ExportToFileAction( ResourceManager.get_instance().get( "file.export.to.file.menu"));
		_fileExportToFileMenuItem = _userInterface.append_menuitem(
			subMenu,
			ResourceManager.get_instance().get( "file.export.to.file.menu"),
			exportToFileAction,
			ResourceManager.get_instance().get( "file.export.to.file.mnemonic"),
			ResourceManager.get_instance().get( "file.export.to.file.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.export.to.file.message"));
		_menuTextMap.put( _fileExportToFileMenuItem.getText(), new Boolean( true));


		ExportToClipboardAction exportToClipboardAction = new ExportToClipboardAction( ResourceManager.get_instance().get( "file.export.to.clipboard.menu"));
		_fileExportToClipboardMenuItem = _userInterface.append_menuitem(
			subMenu,
			ResourceManager.get_instance().get( "file.export.to.clipboard.menu"),
			exportToClipboardAction,
			ResourceManager.get_instance().get( "file.export.to.clipboard.mnemonic"),
			ResourceManager.get_instance().get( "file.export.to.clipboard.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.export.to.clipboard.message"));
		_menuTextMap.put( _fileExportToClipboardMenuItem.getText(), new Boolean( true));


		menu.addSeparator();


		if ( Environment.get_instance().is_initial_data_enable()) {
			subMenu = _userInterface.append_menu(
				menu,
				ResourceManager.get_instance().get( "file.initial.data.menu"),
				true,
				ResourceManager.get_instance().get( "file.initial.data.mnemonic"),
				_messageLabel,
				ResourceManager.get_instance().get( "file.initial.data.message"));


			ImportInitialDataAction importInitialDataAction = new ImportInitialDataAction( ResourceManager.get_instance().get( "file.import.initial.data.menu"));
			_fileImportInitialDataMenuItem = _userInterface.append_menuitem(
				subMenu,
				ResourceManager.get_instance().get( "file.import.initial.data.menu"),
				importInitialDataAction,
				ResourceManager.get_instance().get( "file.import.initial.data.mnemonic"),
				ResourceManager.get_instance().get( "file.import.initial.data.stroke"),
				_messageLabel,
				ResourceManager.get_instance().get( "file.import.initial.data.message"));
			_menuTextMap.put( _fileImportInitialDataMenuItem.getText(), new Boolean( true));


			ExportInitialDataAction exportInitialDataAction = new ExportInitialDataAction( ResourceManager.get_instance().get( "file.export.initial.data.menu"));
			_fileExportInitialDataMenuItem = _userInterface.append_menuitem(
				subMenu,
				ResourceManager.get_instance().get( "file.export.initial.data.menu"),
				exportInitialDataAction,
				ResourceManager.get_instance().get( "file.export.initial.data.mnemonic"),
				ResourceManager.get_instance().get( "file.export.initial.data.stroke"),
				_messageLabel,
				ResourceManager.get_instance().get( "file.export.initial.data.message"));
			_menuTextMap.put( _fileExportInitialDataMenuItem.getText(), new Boolean( true));


			menu.addSeparator();
		}


		if ( Environment.get_instance().is_gis_enable()) {
			subMenu = _userInterface.append_menu(
				menu,
				ResourceManager.get_instance().get( "file.gis.menu"),
				true,
				ResourceManager.get_instance().get( "file.gis.mnemonic"),
				_messageLabel,
				ResourceManager.get_instance().get( "file.gis.message"));


			OpenGisDataAction openGisDataAction = new OpenGisDataAction( ResourceManager.get_instance().get( "file.open.gis.data.menu"));
			_fileOpenGisDataMenuItem = _userInterface.append_menuitem(
				subMenu,
				ResourceManager.get_instance().get( "file.open.gis.data.menu"),
				openGisDataAction,
				ResourceManager.get_instance().get( "file.open.gis.data.mnemonic"),
				ResourceManager.get_instance().get( "file.open.gis.data.stroke"),
				_messageLabel,
				ResourceManager.get_instance().get( "file.open.gis.data.message"));
			_menuTextMap.put( _fileOpenGisDataMenuItem.getText(), new Boolean( true));


			ImportGisDataAction importGisDataAction = new ImportGisDataAction( ResourceManager.get_instance().get( "file.import.gis.data.menu"));
			_fileImportGisDataMenuItem = _userInterface.append_menuitem(
				subMenu,
				ResourceManager.get_instance().get( "file.import.gis.data.menu"),
				importGisDataAction,
				ResourceManager.get_instance().get( "file.import.gis.data.mnemonic"),
				ResourceManager.get_instance().get( "file.import.gis.data.stroke"),
				_messageLabel,
				ResourceManager.get_instance().get( "file.import.gis.data.message"));
			_menuTextMap.put( _fileImportGisDataMenuItem.getText(), new Boolean( true));


			menu.addSeparator();
		}


		CreateDockerFilesetAction createDockerFilesetAction = new CreateDockerFilesetAction( ResourceManager.get_instance().get( "file.create.docker.fileset.menu"));
		_fileCreateDockerFilesetMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.create.docker.fileset.menu"),
			createDockerFilesetAction,
			ResourceManager.get_instance().get( "file.create.docker.fileset.mnemonic"),
			ResourceManager.get_instance().get( "file.create.docker.fileset.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.create.docker.fileset.message"));
		_menuTextMap.put( _fileCreateDockerFilesetMenuItem.getText(), new Boolean( true));


		menu.addSeparator();


		ExitAction exitAction = new ExitAction( ResourceManager.get_instance().get( "file.exit.menu"));
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.exit.menu"),
			exitAction,
			ResourceManager.get_instance().get( "file.exit.mnemonic"),
			ResourceManager.get_instance().get( "file.exit.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.exit.message"));
		_menuTextMap.put( ResourceManager.get_instance().get( "file.exit.menu"), new Boolean( true));




		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "edit.menu"),
			true,
			ResourceManager.get_instance().get( "edit.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "edit.message"));
		menu.addMenuListener( new MenuListener() {
			public void menuSelected(MenuEvent e) {
				_editEditMenuItem.setEnabled( true);
				_editRemoveMenuItem.setEnabled( true);
				_editMoveMenuItem.setEnabled( true);
				_editCopyMenuItem.setEnabled( true);

				_editPasteMenuItem.setEnabled( !StateManager.get_instance().get_selected_drawobjects().isEmpty());
				//_edit_paste_menuItem.setEnabled( !_selected_drawobjects.isEmpty());

				_editSelectAllMenuItem.setEnabled(
					!LayerManager.get_instance().get_current_layer().isEmpty());

				_editFlushTopMenuItem.setEnabled( true);
				_editFlushBottomMenuItem.setEnabled( true);
				_editFlushLeftMenuItem.setEnabled( true);
				_editFlushRightMenuItem.setEnabled( true);
				_editVerticalEqualLayoutMenuItem.setEnabled( true);
				_editHorizontalEqualLayoutMenuItem.setEnabled( true);

				_editSwapNamesMenuItem.setEnabled( true);

				_editCopyObjectsMenuItem.setEnabled( true);
				_editPasteObjectsMenuItem.setEnabled( StateManager.get_instance().can_paste_objects());

				Vector<DrawObject> drawObjects = new Vector<DrawObject>();
				LayerManager.get_instance().get_selected( drawObjects);
				if ( drawObjects.isEmpty()) {
					_editEditMenuItem.setEnabled( false);
					_editRemoveMenuItem.setEnabled( false);
					_editMoveMenuItem.setEnabled( false);
					//_edit_move_menuItem.setEnabled( 1 < LayerManager.get_instance().size() && is_closure_connection( drawObjects));
					//_edit_move_menuItem.setEnabled( 1 < LayerManager.get_instance().size() && is_closure_connection( drawObject, drawObjects));
					_editCopyMenuItem.setEnabled( false);

					_editDeselectAllMenuItem.setEnabled( false);

					_editFlushTopMenuItem.setEnabled( false);
					_editFlushBottomMenuItem.setEnabled( false);
					_editFlushLeftMenuItem.setEnabled( false);
					_editFlushRightMenuItem.setEnabled( false);
					_editVerticalEqualLayoutMenuItem.setEnabled( false);
					_editHorizontalEqualLayoutMenuItem.setEnabled( false);

					_editSwapNamesMenuItem.setEnabled( false);

					_editCopyObjectsMenuItem.setEnabled( false);
					//_edit_copy_objects_menuItem.setEnabled( is_closure_connection( drawObjects));
					//_edit_copy_objects_menuItem.setEnabled( is_closure_connection( drawObject, drawObjects));

				} else {
					_editEditMenuItem.setEnabled( 1 == drawObjects.size());
					_editMoveMenuItem.setEnabled( 1 < LayerManager.get_instance().size() && StateManager.get_instance().is_closure_connection( drawObjects));

					_editDeselectAllMenuItem.setEnabled( true);

					_editFlushTopMenuItem.setEnabled( 1 < drawObjects.size());
					_editFlushBottomMenuItem.setEnabled( 1 < drawObjects.size());
					_editFlushLeftMenuItem.setEnabled( 1 < drawObjects.size());
					_editFlushRightMenuItem.setEnabled( 1 < drawObjects.size());
					_editVerticalEqualLayoutMenuItem.setEnabled( 2 < drawObjects.size());
					_editHorizontalEqualLayoutMenuItem.setEnabled( 2 < drawObjects.size());

					_editSwapNamesMenuItem.setEnabled( StateManager.get_instance().can_swap_names( drawObjects));

					_editCopyObjectsMenuItem.setEnabled( StateManager.get_instance().is_closure_connection( drawObjects) && StateManager.get_instance().can_copy_objects( drawObjects));
				}
			}
			public void menuDeselected(MenuEvent e) {
			}
			public void menuCanceled(MenuEvent e) {
			}
		});


		_editEditMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "common.popup.menu.edit.menu"),
			new EditAction( ResourceManager.get_instance().get( "common.popup.menu.edit.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.edit.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.edit.stroke"));
		_menuTextMap.put( _editEditMenuItem.getText(), new Boolean( true));


		RemoveAction removeAction = new RemoveAction( ResourceManager.get_instance().get( "common.popup.menu.remove.menu"), this);
		_editRemoveMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "common.popup.menu.remove.menu"),
			removeAction,
			ResourceManager.get_instance().get( "common.popup.menu.remove.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.remove.stroke"));
		_menuTextMap.put( _editRemoveMenuItem.getText(), new Boolean( true));


		_editMoveMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.popup.menu.move.menu"),
			new MoveAction( ResourceManager.get_instance().get( "edit.popup.menu.move.menu"), this),
			ResourceManager.get_instance().get( "edit.popup.menu.move.mnemonic"),
			ResourceManager.get_instance().get( "edit.popup.menu.move.stroke"));
		_menuTextMap.put( _editMoveMenuItem.getText(), new Boolean( true));


		menu.addSeparator();


		CopyAction copyAction = new CopyAction( ResourceManager.get_instance().get( "common.popup.menu.copy.menu"), this);
		_editCopyMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "common.popup.menu.copy.menu"),
			copyAction,
			ResourceManager.get_instance().get( "common.popup.menu.copy.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.copy.stroke"));
		_menuTextMap.put( _editCopyMenuItem.getText(), new Boolean( true));


		PasteAction pasteAction = new PasteAction( ResourceManager.get_instance().get( "common.popup.menu.paste.menu"), this);
		_editPasteMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "common.popup.menu.paste.menu"),
			pasteAction,
			ResourceManager.get_instance().get( "common.popup.menu.paste.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.paste.stroke"));
		_menuTextMap.put( _editPasteMenuItem.getText(), new Boolean( true));


		menu.addSeparator();


		_editSelectAllMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "common.popup.menu.select.all.menu"),
			new SelectAllAction( ResourceManager.get_instance().get( "common.popup.menu.select.all.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.select.all.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.select.all.stroke"));
		_menuTextMap.put( _editSelectAllMenuItem.getText(), new Boolean( true));


		_editDeselectAllMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "common.popup.menu.deselect.all.menu"),
			new DeselectAllAction( ResourceManager.get_instance().get( "common.popup.menu.deselect.all.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.deselect.all.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.deselect.all.stroke"));
		_menuTextMap.put( _editDeselectAllMenuItem.getText(), new Boolean( true));


		menu.addSeparator();


		_editFlushTopMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.popup.menu.flush.top.menu"),
			new FlushTopAction( ResourceManager.get_instance().get( "edit.popup.menu.flush.top.menu"), this),
			ResourceManager.get_instance().get( "edit.popup.menu.flush.top.mnemonic"),
			ResourceManager.get_instance().get( "edit.popup.menu.flush.top.stroke"));
		_menuTextMap.put( _editFlushTopMenuItem.getText(), new Boolean( true));


		_editFlushBottomMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.popup.menu.flush.bottom.menu"),
			new FlushBottomAction( ResourceManager.get_instance().get( "edit.popup.menu.flush.bottom.menu"), this),
			ResourceManager.get_instance().get( "edit.popup.menu.flush.bottom.mnemonic"),
			ResourceManager.get_instance().get( "edit.popup.menu.flush.bottom.stroke"));
		_menuTextMap.put( _editFlushBottomMenuItem.getText(), new Boolean( true));


		_editFlushLeftMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.popup.menu.flush.left.menu"),
			new FlushLeftAction( ResourceManager.get_instance().get( "edit.popup.menu.flush.left.menu"), this),
			ResourceManager.get_instance().get( "edit.popup.menu.flush.left.mnemonic"),
			ResourceManager.get_instance().get( "edit.popup.menu.flush.left.stroke"));
		_menuTextMap.put( _editFlushLeftMenuItem.getText(), new Boolean( true));


		_editFlushRightMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.popup.menu.flush.right.menu"),
			new FlushRightAction( ResourceManager.get_instance().get( "edit.popup.menu.flush.right.menu"), this),
			ResourceManager.get_instance().get( "edit.popup.menu.flush.right.mnemonic"),
			ResourceManager.get_instance().get( "edit.popup.menu.flush.right.stroke"));
		_menuTextMap.put( _editFlushRightMenuItem.getText(), new Boolean( true));


		menu.addSeparator();


		_editVerticalEqualLayoutMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.popup.menu.vertical.equal.layout.menu"),
			new VerticalEqualLayoutAction( ResourceManager.get_instance().get( "edit.popup.menu.vertical.equal.layout.menu"), this),
			ResourceManager.get_instance().get( "edit.popup.menu.vertical.equal.layout.mnemonic"),
			ResourceManager.get_instance().get( "edit.popup.menu.vertical.equal.layout.stroke"));
		_menuTextMap.put( _editVerticalEqualLayoutMenuItem.getText(), new Boolean( true));


		_editHorizontalEqualLayoutMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.popup.menu.horizontal.equal.layout.menu"),
			new HorizontalEqualLayoutAction( ResourceManager.get_instance().get( "edit.popup.menu.horizontal.equal.layout.menu"), this),
			ResourceManager.get_instance().get( "edit.popup.menu.horizontal.equal.layout.mnemonic"),
			ResourceManager.get_instance().get( "edit.popup.menu.horizontal.equal.layout.stroke"));
		_menuTextMap.put( _editHorizontalEqualLayoutMenuItem.getText(), new Boolean( true));


		menu.addSeparator();


		_editSwapNamesMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.popup.menu.swap.names.menu"),
			new SwapNamesAction( ResourceManager.get_instance().get( "edit.popup.menu.swap.names.menu"), this),
			ResourceManager.get_instance().get( "edit.popup.menu.swap.names.mnemonic"),
			ResourceManager.get_instance().get( "edit.popup.menu.swap.names.stroke"));
		_menuTextMap.put( _editSwapNamesMenuItem.getText(), new Boolean( true));


		menu.addSeparator();


		_editCopyObjectsMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.popup.menu.copy.objects.menu"),
			new CopyObjectsAction( ResourceManager.get_instance().get( "edit.popup.menu.copy.objects.menu"), this),
			ResourceManager.get_instance().get( "edit.popup.menu.copy.objects.mnemonic"),
			ResourceManager.get_instance().get( "edit.popup.menu.copy.objects.stroke"));
		_menuTextMap.put( _editCopyObjectsMenuItem.getText(), new Boolean( true));


		_editPasteObjectsMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.popup.menu.paste.objects.menu"),
			new PasteObjectsAction( ResourceManager.get_instance().get( "edit.popup.menu.paste.objects.menu"), this),
			ResourceManager.get_instance().get( "edit.popup.menu.paste.objects.mnemonic"),
			ResourceManager.get_instance().get( "edit.popup.menu.paste.objects.stroke"));
		_menuTextMap.put( _editPasteObjectsMenuItem.getText(), new Boolean( true));




		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "setting.menu"),
			true,
			ResourceManager.get_instance().get( "setting.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "setting.message"));


		StageAction stageAction = new StageAction( ResourceManager.get_instance().get( "setting.stage.menu"));
		_settingStageMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "setting.stage.menu"),
			stageAction,
			ResourceManager.get_instance().get( "setting.stage.mnemonic"),
			ResourceManager.get_instance().get( "setting.stage.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "setting.stage.message"));
		_menuTextMap.put( _settingStageMenuItem.getText(), new Boolean( true));


		menu.addSeparator();


		SimulationAction simulationAction = new SimulationAction( ResourceManager.get_instance().get( "setting.simulation.menu"));
		_settingSimulationMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "setting.simulation.menu"),
			simulationAction,
			ResourceManager.get_instance().get( "setting.simulation.mnemonic"),
			ResourceManager.get_instance().get( "setting.simulation.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "setting.simulation.message"));
		_menuTextMap.put( _settingSimulationMenuItem.getText(), new Boolean( true));


		menu.addSeparator();


		ExpressionAction expressionAction = new ExpressionAction( ResourceManager.get_instance().get( "setting.expression.menu"));
		_settingExpressionMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "setting.expression.menu"),
			expressionAction,
			ResourceManager.get_instance().get( "setting.expression.mnemonic"),
			ResourceManager.get_instance().get( "setting.expression.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "setting.expression.message"));
		_menuTextMap.put( _settingExpressionMenuItem.getText(), new Boolean( true));


		menu.addSeparator();


		Log1Action log1Action = new Log1Action( ResourceManager.get_instance().get( "setting.log1.menu"));
		_settingLog1MenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "setting.log1.menu"),
			log1Action,
			ResourceManager.get_instance().get( "setting.log1.mnemonic"),
			ResourceManager.get_instance().get( "setting.log1.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "setting.log1.message"));
		_menuTextMap.put( _settingLog1MenuItem.getText(), new Boolean( true));


		Log2Action log2Action = new Log2Action( ResourceManager.get_instance().get( "setting.log2.menu"));
		_settingLog2MenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "setting.log2.menu"),
			log2Action,
			ResourceManager.get_instance().get( "setting.log2.mnemonic"),
			ResourceManager.get_instance().get( "setting.log2.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "setting.log2.message"));
		_menuTextMap.put( _settingLog2MenuItem.getText(), new Boolean( true));


		menu.addSeparator();


		CommentAction commentAction = new CommentAction( ResourceManager.get_instance().get( "setting.comment.menu"));
		_settingCommentMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "setting.comment.menu"),
			commentAction,
			ResourceManager.get_instance().get( "setting.comment.mnemonic"),
			ResourceManager.get_instance().get( "setting.comment.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "setting.comment.message"));
		_menuTextMap.put( _settingCommentMenuItem.getText(), new Boolean( true));


		menu.addSeparator();


		ExperimentAction experimentAction = new ExperimentAction( ResourceManager.get_instance().get( "setting.experiment.menu"));
		_settingExperimentMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "setting.experiment.menu"),
			experimentAction,
			ResourceManager.get_instance().get( "setting.experiment.mnemonic"),
			ResourceManager.get_instance().get( "setting.experiment.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "setting.experiment.message"));
		_settingExperimentMenuItem.setEnabled( false);
		_menuTextMap.put( _settingExperimentMenuItem.getText(), new Boolean( true));




		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "view.menu"),
			true,
			ResourceManager.get_instance().get( "view.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "view.message"));


		ShowMonitorAction showMonitorAction = new ShowMonitorAction( ResourceManager.get_instance().get( "view.show.monitor.menu"));
		_viewShowMonitorMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "view.show.monitor.menu"),
			showMonitorAction,
			ResourceManager.get_instance().get( "view.show.monitor.mnemonic"),
			ResourceManager.get_instance().get( "view.show.monitor.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "view.show.monitor.message"));
		_viewShowMonitorMenuItem.setEnabled( false);




		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "run.menu"),
			true,
			ResourceManager.get_instance().get( "run.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.message"));


		ModelBuilderAction modelBuilderAction = new ModelBuilderAction( ResourceManager.get_instance().get( "run.model.builder.menu"));
		_runModelBuilderMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "run.model.builder.menu"),
			modelBuilderAction,
			ResourceManager.get_instance().get( "run.model.builder.mnemonic"),
			ResourceManager.get_instance().get( "run.model.builder.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.model.builder.message"));
		//_run_model_builder_menuItem.setEnabled( false);
		_menuTextMap.put( _runModelBuilderMenuItem.getText(), new Boolean( true));


		SimulatorAction simulatorAction = new SimulatorAction( ResourceManager.get_instance().get( "run.simulator.menu"));
		_runSimulatorMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "run.simulator.menu"),
			simulatorAction,
			ResourceManager.get_instance().get( "run.simulator.mnemonic"),
			ResourceManager.get_instance().get( "run.simulator.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.simulator.message"));
		_menuTextMap.put( _runSimulatorMenuItem.getText(), new Boolean( true));


//		ModelBuilderWithoutAnimatorAction modelBuilderWithoutAnimatorAction = new ModelBuilderWithoutAnimatorAction( ResourceManager.get_instance().get( "run.model.builder.without.animator.menu"));
//		_run_model_builder_without_animator_menuItem = _userInterface.append_menuitem(
//			menu,
//			ResourceManager.get_instance().get( "run.model.builder.without.animator.menu"),
//			modelBuilderWithoutAnimatorAction,
//			ResourceManager.get_instance().get( "run.model.builder.without.animator.mnemonic"),
//			ResourceManager.get_instance().get( "run.model.builder.without.animator.stroke"),
//			_message_label,
//			ResourceManager.get_instance().get( "run.model.builder.without.animator.message"));
//		_menuTextMap.put( _run_model_builder_without_animator_menuItem.getText(), new Boolean( true));
//
//
//		AnimatorAction animatorAction = new AnimatorAction( ResourceManager.get_instance().get( "run.animator.menu"));
//		_run_animator_menuItem = _userInterface.append_menuitem(
//			menu,
//			ResourceManager.get_instance().get( "run.animator.menu"),
//			animatorAction,
//			ResourceManager.get_instance().get( "run.animator.mnemonic"),
//			ResourceManager.get_instance().get( "run.animator.stroke"),
//			_message_label,
//			ResourceManager.get_instance().get( "run.animator.message"));
//		_menuTextMap.put( _run_animator_menuItem.getText(), new Boolean( true));




		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "option.menu"),
			true,
			ResourceManager.get_instance().get( "option.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "option.message"));


		ApplicationSettingAction applicationSettingAction = new ApplicationSettingAction( ResourceManager.get_instance().get( "option.application.setting.menu"));
		_optionApplicationSettingMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "option.application.setting.menu"),
			applicationSettingAction,
			ResourceManager.get_instance().get( "option.application.setting.mnemonic"),
			ResourceManager.get_instance().get( "option.application.setting.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "option.application.setting.message"));
		_menuTextMap.put( _optionApplicationSettingMenuItem.getText(), new Boolean( true));


		
		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "help.menu"),
			true,
			ResourceManager.get_instance().get( "help.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "help.message"));


		ContentsAction contentsAction = new ContentsAction(
			ResourceManager.get_instance().get( "help.contents.menu"));
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "help.contents.menu"),
			contentsAction,
			ResourceManager.get_instance().get( "help.contents.mnemonic"),
			ResourceManager.get_instance().get( "help.contents.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "help.contents.message"));


		menu.addSeparator();


		DocumentAction documentAction = new DocumentAction(
			ResourceManager.get_instance().get( "help.document.menu"));
		_helpDocumentMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "help.document.menu"),
			documentAction,
			ResourceManager.get_instance().get( "help.document.mnemonic"),
			ResourceManager.get_instance().get( "help.document.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "help.document.message"));
		_helpDocumentMenuItem.setEnabled( false);


		menu.addSeparator();


		ForumAction forumAction = new ForumAction( ResourceManager.get_instance().get( "help.forum.menu"));
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "help.forum.menu"),
			forumAction,
			ResourceManager.get_instance().get( "help.forum.mnemonic"),
			ResourceManager.get_instance().get( "help.forum.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "help.forum.message"));
		_menuTextMap.put( ResourceManager.get_instance().get( "help.forum.menu"), new Boolean( true));


		menu.addSeparator();


		AboutAction aboutAction = new AboutAction( ResourceManager.get_instance().get( "help.about.menu"));
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "help.about.menu"),
			aboutAction,
			ResourceManager.get_instance().get( "help.about.mnemonic"),
			ResourceManager.get_instance().get( "help.about.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "help.about.message"));
		_menuTextMap.put( ResourceManager.get_instance().get( "help.about.menu"), new Boolean( true));


		//menuBar.setEnabled( false);

		setJMenuBar( menuBar);




		JToolBar toolBar = new JToolBar();
		toolBar.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0));


		ImageIcon imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/app_exit.png"));
		JButton button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "file.exit.menu"),
			exitAction,
			ResourceManager.get_instance().get( "file.exit.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.exit.message"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));


		toolBar.addSeparator();


//		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/new.png"));
//		_fileNewButton = _userInterface.append_tool_button(
//			toolBar,
//			imageIcon,
//			ResourceManager.get_instance().get( "file.new.menu"),
//			newAction,
//			ResourceManager.get_instance().get( "file.new.tooltip"),
//			_messageLabel,
//			ResourceManager.get_instance().get( "file.new.message"));
//		_fileNewButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileNewButton.getPreferredSize().height));
//
//
//		toolBar.addSeparator();
//
//
//		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/open.png"));
//		_fileOpenButton = _userInterface.append_tool_button(
//			toolBar,
//			imageIcon,
//			ResourceManager.get_instance().get( "file.open.menu"),
//			openAction,
//			ResourceManager.get_instance().get( "file.open.tooltip"),
//			_messageLabel,
//			ResourceManager.get_instance().get( "file.open.message"));
//		_fileOpenButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileOpenButton.getPreferredSize().height));
//
//
//		toolBar.addSeparator();
//
//
//		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/close.png"));
//		_fileCloseButton = _userInterface.append_tool_button(
//			toolBar,
//			imageIcon,
//			ResourceManager.get_instance().get( "file.close.menu"),
//			closeAction,
//			ResourceManager.get_instance().get( "file.close.tooltip"),
//			_messageLabel,
//			ResourceManager.get_instance().get( "file.close.message"));
//		_fileCloseButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileCloseButton.getPreferredSize().height));
//		_fileCloseButton.setEnabled( false);
//
//
//		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/save.png"));
		_fileSaveButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "file.save.menu"),
			saveAction,
			ResourceManager.get_instance().get( "file.save.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.save.message"));
		_fileSaveButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileSaveButton.getPreferredSize().height));
		_fileSaveButton.setEnabled( false);


//		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/save_as.png"));
//		_fileSaveAsButton = _userInterface.append_tool_button(
//			toolBar,
//			imageIcon,
//			ResourceManager.get_instance().get( "file.saveas.menu"),
//			saveAsAction,
//			ResourceManager.get_instance().get( "file.saveas.tooltip"),
//			_messageLabel,
//			ResourceManager.get_instance().get( "file.saveas.message"));
//		_fileSaveAsButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileSaveAsButton.getPreferredSize().height));
//		//_file_save_as_button.setEnabled( false);


//		toolBar.addSeparator();
//
//
//		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/import_initial_data.png"));
//		_fileImportInitialDataButton = _userInterface.append_tool_button(
//			toolBar,
//			imageIcon,
//			ResourceManager.get_instance().get( "file.import.initial.data.menu"),
//			importInitialDataAction,
//			ResourceManager.get_instance().get( "file.import.initial.data.tooltip"),
//			_message_label,
//			ResourceManager.get_instance().get( "file.import.initial.data.message"));
//		_fileImportInitialDataButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileImportInitialDataButton.getPreferredSize().height));
//
//
//		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/file/export_initial_data.png"));
//		_fileExportInitialDataButton = _userInterface.append_tool_button(
//			toolBar,
//			imageIcon,
//			ResourceManager.get_instance().get( "file.export.initial.data.menu"),
//			exportInitialDataAction,
//			ResourceManager.get_instance().get( "file.export.initial.data.tooltip"),
//			_message_label,
//			ResourceManager.get_instance().get( "file.export.initial.data.message"));
//		_fileExportInitialDataButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileExportInitialDataButton.getPreferredSize().height));
//
//
//		toolBar.addSeparator();


//		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/toolbar/menu/file/export.png"));
//		_fileExportButton = _userInterface.append_tool_button(
//			toolBar,
//			imageIcon,
//			ResourceManager.get_instance().get( "file.export.menu"),
//			exportAction,
//			ResourceManager.get_instance().get( "file.export.tooltip"),
//			_message_label,
//			ResourceManager.get_instance().get( "file.export.message"));
//		_fileExportButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileExportButton.getPreferredSize().height));
//
//
//		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/toolbar/menu/file/export_to_clipboard.png"));
//		_fileExportToClipboardButton = _userInterface.append_tool_button(
//			toolBar,
//			imageIcon,
//			ResourceManager.get_instance().get( "file.export.to.clipboard.menu"),
//			exportToClipboardAction,
//			ResourceManager.get_instance().get( "file.export.to.clipboard.tooltip"),
//			_message_label,
//			ResourceManager.get_instance().get( "file.export.to.clipboard.message"));
//		_fileExportToClipboardButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _fileExportToClipboardButton.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/copy.png"));
		_editCopyButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "common.popup.menu.copy.menu"),
			copyAction,
			ResourceManager.get_instance().get( "common.popup.menu.copy.menu"),
			_messageLabel,
			ResourceManager.get_instance().get( "common.popup.menu.copy.menu"));
		_editCopyButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _editCopyButton.getPreferredSize().height));
		_editCopyButton.setEnabled( false);


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/paste.png"));
		_editPasteButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "common.popup.menu.paste.menu"),
			pasteAction,
			ResourceManager.get_instance().get( "common.popup.menu.paste.menu"),
			_messageLabel,
			ResourceManager.get_instance().get( "common.popup.menu.paste.menu"));
		_editPasteButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _editPasteButton.getPreferredSize().height));
		_editPasteButton.setEnabled( false);


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/delete.png"));
		_editRemoveButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "common.popup.menu.remove.menu"),
			removeAction,
			ResourceManager.get_instance().get( "common.popup.menu.remove.menu"),
			_messageLabel,
			ResourceManager.get_instance().get( "common.popup.menu.remove.menu"));
		_editRemoveButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _editRemoveButton.getPreferredSize().height));
		_editRemoveButton.setEnabled( false);


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/setting/stage.png"));
		_settingStageButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "setting.stage.menu"),
			stageAction,
			ResourceManager.get_instance().get( "setting.stage.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "setting.stage.message"));
		_settingStageButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _settingStageButton.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/setting/simulation.png"));
		_settingSimulationButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "setting.simulation.menu"),
			simulationAction,
			ResourceManager.get_instance().get( "setting.simulation.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "setting.simulation.message"));
		_settingSimulationButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _settingSimulationButton.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/setting/expression.png"));
		_settingExpressionButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "setting.expression.menu"),
			expressionAction,
			ResourceManager.get_instance().get( "setting.expression.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "setting.expression.message"));
		_settingExpressionButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _settingExpressionButton.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/setting/log.png"));
		_settingLog1Button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "setting.log1.menu"),
			log1Action,
			ResourceManager.get_instance().get( "setting.log1.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "setting.log1.message"));
		_settingLog1Button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _settingLog1Button.getPreferredSize().height));


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/setting/log.png"));
		_settingLog2Button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "setting.log2.menu"),
			log2Action,
			ResourceManager.get_instance().get( "setting.log2.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "setting.log2.message"));
		_settingLog2Button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _settingLog2Button.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/setting/comment.png"));
		_settingCommentButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "setting.comment.menu"),
			commentAction,
			ResourceManager.get_instance().get( "setting.comment.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "setting.comment.message"));
		_settingCommentButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _settingCommentButton.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/setting/experiment.png"));
		_settingExperimentButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "setting.experiment.menu"),
			experimentAction,
			ResourceManager.get_instance().get( "setting.experiment.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "setting.experiment.message"));
		_settingExperimentButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _settingExperimentButton.getPreferredSize().height));
		_settingExperimentButton.setEnabled( false);


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/run/modelbuilder.png"));
		_runModelBuilderButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "run.model.builder.menu"),
			modelBuilderAction,
			ResourceManager.get_instance().get( "run.model.builder.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.model.builder.message"));
		_runModelBuilderButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _runModelBuilderButton.getPreferredSize().height));
		//_run_model_builder_button.setEnabled( false);


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/run/simulator.png"));
		_runSimulatorButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "run.simulator.menu"),
			simulatorAction,
			ResourceManager.get_instance().get( "run.simulator.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.simulator.message"));
		_runSimulatorButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _runSimulatorButton.getPreferredSize().height));
		//_run_simulator_button.setEnabled( false);


//		toolBar.addSeparator();
//
//
//		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/toolbar/menu/run/modelbuilder_without_animator.png"));
//		_run_model_builder_without_animator_button = _userInterface.append_tool_button(
//			toolBar,
//			imageIcon,
//			ResourceManager.get_instance().get( "run.model.builder.without.animator.menu"),
//			modelBuilderWithoutAnimatorAction,
//			ResourceManager.get_instance().get( "run.model.builder.without.animator.tooltip"),
//			_message_label,
//			ResourceManager.get_instance().get( "run.model.builder.without.animator.message"));
//		_run_model_builder_without_animator_button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _run_model_builder_without_animator_button.getPreferredSize().height));
//		//_run_model_builder_without_animator_button.setEnabled( false);
//
//
//		toolBar.addSeparator();
//
//
//		imageIcon = new ImageIcon( getClass().getResource( Constant._resource_directory + "/image/toolbar/menu/run/animator.png"));
//		_run_animator_button = _userInterface.append_tool_button(
//			toolBar,
//			imageIcon,
//			ResourceManager.get_instance().get( "run.animator.menu"),
//			animatorAction,
//			ResourceManager.get_instance().get( "run.animator.tooltip"),
//			_message_label,
//			ResourceManager.get_instance().get( "run.animator.message"));
//		_run_animator_button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _run_animator_button.getPreferredSize().height));
//		//_run_animator_button.setEnabled( false);



		_layerComboBox = new JComboBox();
		_layerComboBox.addItem( ResourceManager.get_instance().get( "layer.combobox.name") + "1");
		_layerComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if ( _synchronize) {
					VisualShellView.get_instance().on_change_layer( _layerComboBox.getSelectedIndex());
					update( ResourceManager.get_instance().get( "layer.combobox.name")
						+ " [ " + ( _layerComboBox.getSelectedIndex() + 1) + " / "
						+ _layerComboBox.getItemCount() + " ]");
					requestFocus();
				}
			}
		});
		toolBar.add( _layerComboBox);
		_layerComboBox.setPreferredSize( new Dimension( 200, _layerComboBox.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/layer/insert.png"));
		InsertLayerAction insertLayerAction = new InsertLayerAction( ResourceManager.get_instance().get( "layer.insert.menu"));
		_layerInsertButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "layer.insert.menu"),
			insertLayerAction,
			ResourceManager.get_instance().get( "layer.insert.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "layer.insert.message"));
		_layerInsertButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _layerInsertButton.getPreferredSize().height));


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/layer/append.png"));
		AppendLayerAction appendLayerAction = new AppendLayerAction( ResourceManager.get_instance().get( "layer.append.menu"));
		_layerAppendButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "layer.append.menu"),
			appendLayerAction,
			ResourceManager.get_instance().get( "layer.append.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "layer.append.message"));
		_layerAppendButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _layerAppendButton.getPreferredSize().height));


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/layer/remove.png"));
		RemoveLayerAction removeLayerAction = new RemoveLayerAction( ResourceManager.get_instance().get( "layer.remove.menu"));
		_layerRemoveButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "layer.remove.menu"),
			removeLayerAction,
			ResourceManager.get_instance().get( "layer.remove.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "layer.remove.message"));
		_layerRemoveButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _layerRemoveButton.getPreferredSize().height));
		_layerRemoveButton.setEnabled( false);



		toolBar.setFloatable( false);

		//toolBar.setEnabled( false);

		getContentPane().add( toolBar, BorderLayout.NORTH);




		toolBar = new JToolBar( JToolBar.VERTICAL);


		Map<JComponent, String> buttonKeywordMap = new HashMap<JComponent, String>();

		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/toolbox/agent.png"));
		_toolboxAgentButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			new ButtonTransferHandler( "text", buttonKeywordMap),
			ResourceManager.get_instance().get( "toolbox.agent.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "toolbox.agent.message"));
		_toolboxAgentButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _toolboxAgentButton.getPreferredSize().height));
		buttonKeywordMap.put( _toolboxAgentButton, "agent");


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/toolbox/spot.png"));
		_toolboxSpotButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			new ButtonTransferHandler( "text", buttonKeywordMap),
			ResourceManager.get_instance().get( "toolbox.spot.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "toolbox.spot.message"));
		_toolboxSpotButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _toolboxSpotButton.getPreferredSize().height));
		buttonKeywordMap.put( _toolboxSpotButton, "spot");


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/toolbox/agent_role.png"));
		_toolboxAgentRoleButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			new ButtonTransferHandler( "text", buttonKeywordMap),
			ResourceManager.get_instance().get( "toolbox.agent.role.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "toolbox.agent.role.message"));
		_toolboxAgentRoleButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _toolboxAgentRoleButton.getPreferredSize().height));
		buttonKeywordMap.put( _toolboxAgentRoleButton, "agent_role");


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/toolbox/spot_role.png"));
		_toolboxSpotRoleButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			new ButtonTransferHandler( "text", buttonKeywordMap),
			ResourceManager.get_instance().get( "toolbox.spot.role.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "toolbox.spot.role.message"));
		_toolboxSpotRoleButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _toolboxSpotRoleButton.getPreferredSize().height));
		buttonKeywordMap.put( _toolboxSpotRoleButton, "spot_role");


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/toolbox/chart.png"));
		_toolboxChartButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			new ButtonTransferHandler( "text", buttonKeywordMap),
			ResourceManager.get_instance().get( "toolbox.chart.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "toolbox.chart.message"));
		_toolboxChartButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _toolboxChartButton.getPreferredSize().height));
		buttonKeywordMap.put( _toolboxChartButton, "chart");


		toolBar.setFloatable( false);

		//toolBar.setEnabled( false);

		getContentPane().add( toolBar, BorderLayout.EAST);
	}

	/**
	 * @param menuTextMap
	 * @return
	 */
	private boolean setup_plugin_menu(Map<String, Boolean> menuTextMap) {
		return PluginManager.get_instance().setup_menu( _userInterface, _messageLabel, this, menuTextMap);
	}

	/**
	 * Enables the specified plugins.
	 * @param executePlugins the array of the specified plugins
	 */
	public void execute_plugin(List<String> executePlugins) {
		for ( String name:executePlugins)
			PluginManager.get_instance().execute( name, this);
	}

	/**
	 * 
	 */
	private void initialize_user_interface() {
		setTitle( ResourceManager.get_instance().get( "application.title"));
		update_user_interface(
			true, true, false, false, true, true, true,
			ExperimentManager.get_instance().can_export(),
			ExperimentManager.get_instance().can_export_to_clipboard(),
			true, false,
			true, true, true, true, true, false,
			true, true, false);

		_synchronize = false;
		_layerComboBox.removeAllItems();
		_layerComboBox.addItem( ResourceManager.get_instance().get( "layer.combobox.name") + "1");
		_synchronize = true;

		//update( "                                             ");
		update( ResourceManager.get_instance().get( "layer.combobox.name")
			+ " [ 1 / " + _layerComboBox.getItemCount() + " ]");
	}

	/**
	 * @param fileNew
	 * @param fileOpen
	 * @param fileClose
	 * @param fileSave
	 * @param fileSaveAs
	 * @param fileImportInitialData
	 * @param fileExportInitialData
	 * @param fileExport
	 * @param fileExportToClipboard
	 * @param editSetting
	 * @param editExperiment
	 * @param startModelBuilder
	 * @param runModelBuilder
	 * @param runSimulator
	 * @param runModelBuilderWithoutAnimator
	 * @param runAnimator
	 * @param helpDocument
	 * @param layerInsert
	 * @param layerAppend
	 * @param layerRemove
	 */
	private void update_user_interface(boolean fileNew, boolean fileOpen,
		boolean fileClose, boolean fileSave, boolean fileSaveAs,
		boolean fileImportInitialData, boolean fileExportInitialData,
		boolean fileExport, boolean fileExportToClipboard,
		boolean editSetting, boolean editExperiment,
		boolean startModelBuilder, boolean runModelBuilder, boolean runSimulator,
		boolean runModelBuilderWithoutAnimator, boolean runAnimator, boolean helpDocument,
		boolean layerInsert, boolean layerAppend, boolean layerRemove) {
//		_fileNewMenuItem.setEnabled( fileNew);
//		_fileOpenMenuItem.setEnabled( fileOpen);
//		_fileCloseMenuItem.setEnabled( fileClose);
		_fileSaveMenuItem.setEnabled( fileSave);
//		_fileSaveAsMenuItem.setEnabled( fileSaveAs);
		if ( Environment.get_instance().is_initial_data_enable()) {
			_fileImportInitialDataMenuItem.setEnabled( fileImportInitialData);
			_fileExportInitialDataMenuItem.setEnabled( fileExportInitialData);
		}
		_fileExportToFileMenuItem.setEnabled( fileExport);
		_fileExportToClipboardMenuItem.setEnabled( fileExportToClipboard);

		_settingStageMenuItem.setEnabled( editSetting);
		_settingSimulationMenuItem.setEnabled( editSetting);
		_settingExpressionMenuItem.setEnabled( editSetting);
		_settingLog1MenuItem.setEnabled( editSetting);
		_settingLog2MenuItem.setEnabled( editSetting);
		_settingCommentMenuItem.setEnabled( editSetting);
		_settingExperimentMenuItem.setEnabled( editExperiment);

		_fileExportToSoarsEngineMenuItem.setEnabled( startModelBuilder);
		_runModelBuilderMenuItem.setEnabled( runModelBuilder);
		_runSimulatorMenuItem.setEnabled( runSimulator);

		_helpDocumentMenuItem.setEnabled( helpDocument);

//		_fileNewButton.setEnabled( fileNew);
//		_fileOpenButton.setEnabled( fileOpen);
//		_fileCloseButton.setEnabled( fileClose);
		_fileSaveButton.setEnabled( fileSave);
//		_fileSaveAsButton.setEnabled( fileSaveAs);
//		_fileImportInitialDataButton.setEnabled( file_import_initial_data);
//		_fileExportInitialDataButton.setEnabled( file_export_initial_data);
//		_fileExportButton.setEnabled( file_export);
//		_fileExportToClipboard_button.setEnabled( file_export_to_clipboard);

		_settingStageButton.setEnabled( editSetting);
		_settingSimulationButton.setEnabled( editSetting);
		_settingExpressionButton.setEnabled( editSetting);
		_settingLog1Button.setEnabled( editSetting);
		_settingLog2Button.setEnabled( editSetting);
		_settingCommentButton.setEnabled( editSetting);
		_settingExperimentButton.setEnabled( editExperiment);

		_runModelBuilderButton.setEnabled( runModelBuilder);
		_runSimulatorButton.setEnabled( runSimulator);

		_layerAppendButton.setEnabled( layerInsert);
		_layerAppendButton.setEnabled( layerAppend);
		_layerRemoveButton.setEnabled( layerRemove);
	}

	/**
	 * Loads the data from the specified file.
	 * @param file the specified file
	 * @return
	 */
	public boolean open(File file) {
		if ( !VisualShellView.get_instance().on_file_open( file)) {
			initialize_user_interface();
			return false;
		}

		update_user_interface(
			true, true,
			LayerManager.get_instance().exist_datafile(),
			LayerManager.get_instance().exist_datafile(),
			true, true, true,
			ExperimentManager.get_instance().can_export(),
			ExperimentManager.get_instance().can_export_to_clipboard(),
			true, ( 0 < ExperimentManager.get_instance().get_initial_value_count()),
			can_export(), can_export(), can_export(), can_export(), can_export(),
			LayerManager.get_instance().exist_document_file(),
			true, true, ( 1 < _layerComboBox.getItemCount()));

		return true;
	}

	/**
	 * @return
	 */
	private int confirm() {
		int result = JOptionPane.showConfirmDialog(
			this,
			ResourceManager.get_instance().get( "file.close.confirm.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_CANCEL_OPTION);
		switch ( result) {
			case JOptionPane.YES_OPTION:
				if ( VisualShellView.get_instance().exist_datafile())
					VisualShellView.get_instance().on_file_save();
				else {
					if ( !on_file_save_as( null))
						result = JOptionPane.CANCEL_OPTION;
				}
				break;
			case JOptionPane.CANCEL_OPTION:
			case JOptionPane.CLOSED_OPTION:
				requestFocus();
				break;
		}
		return result;
	}

	/**
	 * @param direct
	 * @return
	 */
	public boolean create(boolean direct) {
		// TODO Auto-generated method stub
		_direct = direct;

		if (!super.create())
			return false;

		return true;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		if ( !MacUtility.setup_screen_menu_handler( this, this, ResourceManager.get_instance().get( "application.title")))
			return false;

		get_property_from_environment_file();

		_userInterface = new UserInterface();

		VisualShellView visualShellView = new VisualShellView( true);
		if ( !visualShellView.create())
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportView( visualShellView);
		scrollPane.setBackground( new Color( 255, 255, 255));
		getContentPane().setLayout( new BorderLayout());
		getContentPane().add( scrollPane);

		setup_menu();

		if ( !setup_plugin_menu( _menuTextMap))
			return false;

		update( ResourceManager.get_instance().get( "layer.combobox.name")
			+ " [ " + ( _layerComboBox.getSelectedIndex() + 1) + " / "
			+ _layerComboBox.getItemCount() + " ]");

		//setup_key_action();

		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

//		new DropTarget( this, this);

		pack();

		optimize_window_rectangle();
		setLocation( _windowRectangle.x, _windowRectangle.y);
		setSize( _windowRectangle.width, _windowRectangle.height);

		Toolkit.getDefaultToolkit().setDynamicLayout( true);

		// TODO Auto-generated method stub
		setVisible( !_direct);

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				int width = getSize().width;
				int height = getSize().height;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width,
					( _minimumHeight > height) ? _minimumHeight : height);
			}
		});

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.mac.IMacScreenMenuHandler#on_mac_about()
	 */
	public void on_mac_about() {
		if ( !isEnabled())
			return;

		on_help_about( null);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.mac.IMacScreenMenuHandler#on_mac_quit()
	 */
	public void on_mac_quit() {
		if ( !isEnabled())
			return;

		on_file_exit( null);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_window_closing(java.awt.event.WindowEvent)
	 */
	protected void on_window_closing(WindowEvent windowEvent) {
		if ( !isEnabled())
			return;

		if ( SerialModelBuilder.isAlive() || ParallelModelBuilder.isAlive())
			return;

		if ( VisualShellView.get_instance().isModified()) {
			int result = confirm();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return;
		} else {
			if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
				this,
				ResourceManager.get_instance().get( "file.exit.confirm.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.YES_NO_OPTION)) {
				requestFocus();
				return;
			}
		}

		MonitorFrame.on_closing();

		VisualShellView.get_instance().on_closing();
		_windowRectangle = getBounds();
		try {
			set_property_to_environment_file();
		}
		catch (IOException e) {
			throw new RuntimeException( e);
		}

		System.exit( 0);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_key_pressed(java.awt.event.KeyEvent)
	 */
	protected void on_key_pressed(KeyEvent keyEvent) {
		VisualShellView.get_instance().on_key_pressed( keyEvent);
		super.on_key_pressed(keyEvent);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_key_released(java.awt.event.KeyEvent)
	 */
	protected void on_key_released(KeyEvent keyEvent) {
		VisualShellView.get_instance().on_key_released( keyEvent);
		super.on_key_released(keyEvent);
	}

	/**
	 * Invoked when the "New" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_new(ActionEvent actionEvent) {
//		if ( VisualShellView.get_instance().isModified()) {
//			int result = confirm();
//			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
//				return;
//		}
//
//		VisualShellView.get_instance().on_file_new();
//		initialize_user_interface();
		requestFocus();
	}

	/**
	 * Invoked when the "Open" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_open(ActionEvent actionEvent) {
//		if ( VisualShellView.get_instance().isModified()) {
//			int result = confirm();
//			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
//				return;
//		}
//
//		VisualShellView.get_instance().cancel();
//
//		File file = CommonTool.get_open_file(
//			Environment._openDirectoryKey,
//			ResourceManager.get_instance().get( "file.open.dialog"),
//			new String[] { Constant._soarsExtension/*"vsl", "vml", "gvml"*/},
//			"soars data"/*"visual shell data"*/,
//			this);
//
		requestFocus();
//
//		if ( null == file)
//			return;
//
//		open( file);
	}

	/**
	 * Invoked when the specified file is dropped.
	 * @param the specified file
	 */
	public void on_file_open_by_drag_and_drop(File file) {
//		if ( VisualShellView.get_instance().isModified()) {
//			int result = confirm();
//			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
//				return;
//		}
//
//		VisualShellView.get_instance().cancel();
//
		requestFocus();
//
//		if ( null == file)
//			return;
//
//		open( file);
	}

	/**
	 * Returns true for copying the objects successfully.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 * @return true for copying the objects successfully
	 */
	public boolean copy_objects(ActionEvent actionEvent) {
		return VisualShellView.get_instance().copy_objects();
	}

	/**
	 * Returns true for pasting the objects successfully.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 * @return true for pasting the objects successfully
	 */
	public boolean paste_objects(ActionEvent actionEvent) {
		VisualShellView.get_instance().cancel();

		if ( !VisualShellView.get_instance().paste_objects())
			return false;

		update_user_interface(
			true, true,
			LayerManager.get_instance().exist_datafile(),
			LayerManager.get_instance().exist_datafile(),
			true, true, true,
			ExperimentManager.get_instance().can_export(),
			ExperimentManager.get_instance().can_export_to_clipboard(),
			true, ( 0 < ExperimentManager.get_instance().get_initial_value_count()),
			can_export(), can_export(), can_export(), can_export(), can_export(),
			LayerManager.get_instance().exist_document_file(),
			true, true, ( 1 < _layerComboBox.getItemCount()));

		return true;
	}

	/**
	 * Invoked when the "Close" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_close(ActionEvent actionEvent) {
//		if ( VisualShellView.get_instance().isModified()) {
//			int result = confirm();
//			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
//				return;
//		}
//
//		VisualShellView.get_instance().on_file_close();
////		initialize_user_interface();
		requestFocus();
	}

	/**
	 * Invoked when the "Save" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_save(ActionEvent actionEvent) {
		VisualShellView.get_instance().on_file_save();
		_helpDocumentMenuItem.setEnabled( LayerManager.get_instance().exist_document_file());
		requestFocus();
	}

	/**
	 * Returns true for saving the data to the file successfullly.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 * @return true for saving the data to the file successfullly
	 */
	public boolean on_file_save_as(ActionEvent actionEvent) {
//		VisualShellView.get_instance().cancel();
//
//		File file = CommonTool.get_save_file(
//			Environment._saveAsDirectoryKey,
//			ResourceManager.get_instance().get( "file.saveas.dialog"),
//			new String[] { Constant._soarsExtension/*"vsl"*/},
//			"soars data"/*"visual shell data"*/,
//			this);
//
//		requestFocus();
//
//		if ( null == file)
//			return false;
//
//		String absolutePath = file.getAbsolutePath();
//		String name = file.getName();
//		int index = name.lastIndexOf( '.');
//		if ( -1 == index)
//			file = new File( absolutePath + "." + Constant._soarsExtension/*".vsl"*/);
//		else if ( name.length() - 1 == index)
//			file = new File( absolutePath + Constant._soarsExtension/*"vsl"*/);
//
//		if ( VisualShellView.get_instance().on_file_save_as( file)) {
//			_fileCloseMenuItem.setEnabled( true);
//			_fileSaveMenuItem.setEnabled( true);
//			_fileCloseButton.setEnabled( true);
//			_fileSaveButton.setEnabled( true);
//		}
//
//		_helpDocumentMenuItem.setEnabled( LayerManager.get_instance().exist_document_file());

		return true;
	}

	/**
	 * Invoked when the "Import" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_import(ActionEvent actionEvent) {
//		if ( VisualShellView.get_instance().isModified()) {
//			int result = confirm();
//			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
//				return;
//		}
//
//		VisualShellView.get_instance().cancel();
//
//		File file = CommonTool.get_open_file(
//			Environment._import_directory_key,
//			ResourceManager.get_instance().get( "file.import.dialog"),
//			new String[] { "sor", "txt"},
//			"SOARS script data",
//			this);
//
		requestFocus();
//
//		if ( null == file)
//			return;
//
//		if ( !VisualShellView.get_instance().on_file_import( file))
//			initialize_user_interface();
//		else
//			update_user_interface(
//				true, true, true, false, true, true,
//				ExperimentManager.get_instance().can_export(),
//				ExperimentManager.get_instance().can_export_to_clipboard(),
//				true, true, ( 0 < ExperimentManager.get_instance().get_initial_value_count()),
//				true, true, ( 1 < _layer_comboBox.getItemCount()));
	}

	/**
	 * Invoked when the "Import initial data" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_import_initial_data(ActionEvent actionEvent) {
		if ( VisualShellView.get_instance().isModified()) {
			int result = confirm();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return;
		}

		VisualShellView.get_instance().cancel();
		
		File file = CommonTool.get_open_file(
			Environment._importInitialDataDirectoryKey,
			ResourceManager.get_instance().get( "file.import.initial.data.dialog"),
			new String[] { "csv", "txt"},
			"SOARS initial data",
			this);

		requestFocus();

		if ( null == file)
			return;

		EditInitialDataSelectionDlg editInitialDataSelectionDlg = new EditInitialDataSelectionDlg(
			this,
			ResourceManager.get_instance().get( "edit.import.initial.data.selection.dialog.title"),
			true,
			Environment._importInitialDataAllKey);
		if ( !editInitialDataSelectionDlg.do_modal( this))
			return;

		import_initial_data( file, 
			Environment.get_instance().get( Environment._importInitialDataAllKey, "false").equals( "true"));
	}

	/**
	 * @param file
	 * @param all
	 * @return
	 */
	protected int import_initial_data(File file, boolean all) {
		WarningManager.get_instance().cleanup();

		int result = VisualShellView.get_instance().on_file_import_initial_data( file, true, all);

		requestFocus();

		switch ( result) {
			case -1:
				initialize_user_interface();
				break;
			case 1:
				update_user_interface(
					true, true,
					LayerManager.get_instance().exist_datafile(),
					LayerManager.get_instance().exist_datafile(),
					true, true, true,
					ExperimentManager.get_instance().can_export(),
					ExperimentManager.get_instance().can_export_to_clipboard(),
					true, ( 0 < ExperimentManager.get_instance().get_initial_value_count()),
					can_export(), can_export(), can_export(), can_export(), can_export(),
					LayerManager.get_instance().exist_document_file(),
					true, true, ( 1 < _layerComboBox.getItemCount()));
				break;
		}

		if ( -1 == result)
			JOptionPane.showMessageDialog(
				this,
				"Could not read initial data!"
					+ "\n" + file.getAbsolutePath(),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE );

		return result;
	}

	/**
	 * Returns true for preparing the plugins successfully.
	 * @return true for preparing the plugins successfully
	 */
	public boolean prepare_for_plugin() {
		if ( VisualShellView.get_instance().isModified()) {
			int result = confirm();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return false;
		}

		VisualShellView.get_instance().on_file_new();
		initialize_user_interface();
		return true;
	}

	/**
	 * Returns 1 for importing the initial data successfully.
	 * @return 1 for importing the initial data successfully
	 */
	public int import_initial_data() {
		VisualShellView.get_instance().on_file_new();
		initialize_user_interface();

		int result = VisualShellView.get_instance().on_import_initial_data( false, true);

		switch ( result) {
			case -1:
				initialize_user_interface();
				break;
			case 1:
				update_user_interface(
					true, true,
					LayerManager.get_instance().exist_datafile(),
					LayerManager.get_instance().exist_datafile(),
					true, true, true,
					ExperimentManager.get_instance().can_export(),
					ExperimentManager.get_instance().can_export_to_clipboard(),
					true, ( 0 < ExperimentManager.get_instance().get_initial_value_count()),
					can_export(), can_export(), can_export(), can_export(), can_export(),
					LayerManager.get_instance().exist_document_file(),
					true, true, ( 1 < _layerComboBox.getItemCount()));
				break;
		}

		return result;
	}

	/**
	 * Invoked when the "Export initial data" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_export_initial_data(ActionEvent actionEvent) {
		VisualShellView.get_instance().cancel();

		File file = CommonTool.get_save_file(
			Environment._importInitialDataDirectoryKey,
			ResourceManager.get_instance().get( "file.export.initial.data.dialog"),
			new String[] { "csv", "txt"},
			"SOARS initial data",
			this);

		requestFocus();

		if ( null == file)
			return;

		EditInitialDataSelectionDlg editInitialDataSelectionDlg = new EditInitialDataSelectionDlg(
			this,
			ResourceManager.get_instance().get( "edit.export.initial.data.selection.dialog.title"),
			true,
			Environment._importInitialDataAllKey);
		if ( !editInitialDataSelectionDlg.do_modal( this))
			return;

		String absoluteName = file.getAbsolutePath();
		String name = file.getName();
		int index = name.lastIndexOf( '.');
		if ( -1 == index)
			file = new File( absoluteName + ".txt");
		else if ( name.length() - 1 == index)
			file = new File( absoluteName + "txt");

		VisualShellView.get_instance().on_file_export_initial_data( file,
			Environment.get_instance().get( Environment._importInitialDataAllKey, "false").equals( "true"));
	}

	/**
	 * Invoked when the "Export SOARS Engine script to file" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_export_to_file(ActionEvent actionEvent) {
		VisualShellView.get_instance().cancel();

		File file = CommonTool.get_save_file(
			Environment._exportDirectoryKey,
			ResourceManager.get_instance().get( "file.export.to.file.dialog"),
			new String[] { "sor", "txt"},
			"SOARS script data",
			this);

		requestFocus();

		if ( null == file)
			return;

		String absoluteName = file.getAbsolutePath();
		String name = file.getName();
		int index = name.lastIndexOf( '.');
		if ( -1 == index)
			file = new File( absoluteName + ".sor");
		else if ( name.length() - 1 == index)
			file = new File( absoluteName + "sor");

		VisualShellView.get_instance().on_file_export( file, true, false);
	}

	/**
	 * Invoked when the "Export script to clipboard" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_export_to_clipboard(ActionEvent actionEvent) {
		VisualShellView.get_instance().cancel();
		requestFocus();
		VisualShellView.get_instance().on_file_export_to_clipboard( true, false);
	}

	/**
	 * @param actionEvent
	 */
	public void on_file_open_gis_data(ActionEvent actionEvent) {
		//if ( VisualShellView.get_instance().isModified()) {
		//	int result = confirm();
		//	if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
		//		return;
		//}

		VisualShellView.get_instance().cancel();

		File file = CommonTool.get_open_file(
			Environment._openGisDataDirectoryKey,
			ResourceManager.get_instance().get( "file.open.dialog"),
			new String[] { "gis"},
			"GIS data for VisualShell",
			this);

		requestFocus();

		if ( null == file)
			return;

		GisDataFrame gisDataFrame = new GisDataFrame( ResourceManager.get_instance().get( "edit.gis.data.dialog.title"));
		if ( !gisDataFrame.create( file))
			return;
	}

	/**
	 * @param actionEvent
	 */
	public void on_file_import_gis_data(ActionEvent actionEvent) {
		VisualShellView.get_instance().cancel();
		requestFocus();
		ImportGisDataDlg importGisDataDlg = new ImportGisDataDlg( this, ResourceManager.get_instance().get( "import.gis.data.dialog.title"), true);
		if ( !importGisDataDlg.do_modal())
			return;

		GisDataFrame gisDataFrame = new GisDataFrame( ResourceManager.get_instance().get( "edit.gis.data.dialog.title"), importGisDataDlg._gisDataManager);
		if ( !gisDataFrame.create())
			return;
	}

	/**
	 * @param actionEvent
	 */
	public void on_file_create_docker_fileset(ActionEvent actionEvent) {
		// TODO Auto-generated method stub
		VisualShellView.get_instance().cancel();
		requestFocus();
		VisualShellView.get_instance().on_file_create_docker_fileset( _direct);
	}

	/**
	 * Invoked when the "Exit" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_exit(ActionEvent actionEvent) {
		if ( !isEnabled())
			return;

		if ( SerialModelBuilder.isAlive() || ParallelModelBuilder.isAlive())
			return;

		if ( VisualShellView.get_instance().isModified()) {
			int result = confirm();
			if ( JOptionPane.YES_OPTION != result && JOptionPane.NO_OPTION != result)
				return;
		} else {
			if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
				this,
				ResourceManager.get_instance().get( "file.exit.confirm.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.YES_NO_OPTION)) {
				requestFocus();
				return;
			}
		}

		MonitorFrame.on_closing();

		VisualShellView.get_instance().cleanup();
		_windowRectangle = getBounds();
		try {
			set_property_to_environment_file();
		}
		catch (IOException e) {
			throw new RuntimeException( e);
		}

		System.exit( 0);
	}

	// TODO Auto-generated method stub
	public void terminate() {
		VisualShellView.get_instance().cleanup();
		System.exit( 0);
	}

	/**
	 * Invoked when the "Stage" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_setting_stage(ActionEvent actionEvent) {
		VisualShellView.get_instance().on_setting_stage();
		requestFocus();
	}

	/**
	 * Invoked when the "Simulation" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_setting_simulation(ActionEvent actionEvent) {
		VisualShellView.get_instance().on_setting_simulation();
		requestFocus();
	}

	/**
	 * Invoked when the "Expression" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_setting_expression(ActionEvent actionEvent) {
		VisualShellView.get_instance().on_setting_expression();
		requestFocus();
	}

	/**
	 * Invoked when the "Log1" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_setting_log1(ActionEvent actionEvent) {
		VisualShellView.get_instance().on_setting_log1();
		requestFocus();
	}

	/**
	 * Invoked when the "Log2" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_setting_log2(ActionEvent actionEvent) {
		VisualShellView.get_instance().on_setting_log2();
		requestFocus();
	}

	/**
	 * Invoked when the "Comment" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_setting_comment(ActionEvent actionEvent) {
		VisualShellView.get_instance().on_setting_comment();
		requestFocus();
	}

	/**
	 * Invoked when the "Experimental support" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_setting_experiment(ActionEvent actionEvent) {
		VisualShellView.get_instance().on_setting_experiment();
		requestFocus();
	}

	/**
	 * Invoked when the "Show monitor" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_show_monitor(ActionEvent actionEvent) {
		MonitorFrame.get_instance().set_visible();
		_viewShowMonitorMenuItem.setEnabled( false);
		requestFocus();
	}

	/**
	 * Invoked when the "Export to SOARS Engine" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_file_export_to_soars_engine(ActionEvent actionEvent) {
		VisualShellView.get_instance().cancel();
		requestFocus();
		VisualShellView.get_instance().on_start_model_builder();
	}

	/**
	 * Invoked when the "Run(Model Builder)" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_run_model_builder(ActionEvent actionEvent) {
		VisualShellView.get_instance().cancel();
		requestFocus();
		VisualShellView.get_instance().on_run_model_builder();
	}

	/**
	 * Invoked when the "Run(Simulator)" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_run_simulator(ActionEvent actionEvent) {
		// TODO Auto-generated method stub
		VisualShellView.get_instance().cancel();
		requestFocus();
		VisualShellView.get_instance().on_run_simulator( _direct);
	}

//	/**
//	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
//	 */
//	public void on_run_model_builder_without_animator(ActionEvent actionEvent) {
//		VisualShellView.get_instance().cancel();
//		requestFocus();
////		VisualShellView.get_instance().on_run_model_builder_without_animator();
//	}
//
//	/**
//	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
//	 */
//	public void on_run_animator(ActionEvent actionEvent) {
//		VisualShellView.get_instance().cancel();
//		requestFocus();
////		VisualShellView.get_instance().on_run_animator();
//	}

	/**
	 * Invoked when the "Application setting" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_application_setting(ActionEvent actionEvent) {
		VisualShellView.get_instance().on_application_setting();
		requestFocus();
	}

	/**
	 * Invoked when the "SOARS Visual Shell Help" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_help_contents(ActionEvent actionEvent) {
		String currentDirectoryName = System.getProperty( Constant._soarsHome);
		if ( null == currentDirectoryName)
			return;

		File file = new File( currentDirectoryName + "/"
			+ ResourceManager.get_instance().get( "help.contents.url"));
		if ( !file.exists() || !file.canRead())
			return;

		try {
			BrowserLauncher.openURL( file.toURI().toURL().toString().replaceAll( "\\\\", "/"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		//BrowserLauncher.openURL( "file:///" + file.getAbsolutePath().replaceAll( "\\\\", "/"));
	}

	/**
	 * Invoked when the "SOARS document" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_help_document(ActionEvent actionEvent) {
		if ( !LayerManager.get_instance().exist_document_file())
			return;

		File file = LayerManager.get_instance().get_document_file();
		if ( null == file)
			return;

		try {
			BrowserLauncher.openURL( file.toURI().toURL().toString().replaceAll( "\\\\", "/"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Invoked when the "SOARS forum" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_help_forum(ActionEvent actionEvent) {
		String language = Locale.getDefault().getLanguage();
		if ( null == language)
			return;

		String url = SoarsCommonEnvironment.get_instance().getProperty(
			language.equals( "ja") ? SoarsCommonEnvironment._forumJaUrlKey : SoarsCommonEnvironment._forumEnUrlKey);
		if ( null == url)
			return;

		BrowserLauncher.openURL( url);
	}

	/**
	 * Invoked when the "About SOARS Visual Shell" menu is selected.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_help_about(ActionEvent actionEvent) {
		VisualShellView.get_instance().on_help_about( actionEvent);

		JOptionPane.showMessageDialog( this,
			Constant.get_version_message(),
			ResourceManager.get_instance().get( "application.title"),
//			JOptionPane.INFORMATION_MESSAGE,
//			new ImageIcon( Resource.load_image_from_resource( Constant._resource_directory + "/image/picture/picture.jpg", getClass())));
			JOptionPane.INFORMATION_MESSAGE);

		requestFocus();
	}

	/**
	 * Inserts the new layer.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_insert_layer(ActionEvent actionEvent) {
		VisualShellView.get_instance().on_insert_layer();

		_layerComboBox.addItem( ResourceManager.get_instance().get( "layer.combobox.name")
			+ ( _layerComboBox.getItemCount() + 1));

		update_user_interface(
			true, true,
			VisualShellView.get_instance().exist_datafile(),
			VisualShellView.get_instance().exist_datafile(),
			true, true, true,
			ExperimentManager.get_instance().can_export(),
			ExperimentManager.get_instance().can_export_to_clipboard(),
			true, ( 0 < ExperimentManager.get_instance().get_initial_value_count()),
			can_export(), can_export(), can_export(), can_export(), can_export(),
			LayerManager.get_instance().exist_document_file(),
			true, true, true);

		update( ResourceManager.get_instance().get( "layer.combobox.name")
			+ " [ " + ( _layerComboBox.getSelectedIndex() + 1) + " / "
			+ _layerComboBox.getItemCount() + " ]");

		requestFocus();
	}

	/**
	 * Appends the new layer.
	 * @param actionEvent a semantic event which indicates that a component-defined action occurred
	 */
	public void on_append_layer(ActionEvent actionEvent) {
		VisualShellView.get_instance().on_append_layer();
		append_layer();
	}

	/**
	 * Appends the new layer.
	 */
	public void append_layer() {
		_layerComboBox.addItem( ResourceManager.get_instance().get( "layer.combobox.name")
			+ ( _layerComboBox.getItemCount() + 1));
		_layerComboBox.setSelectedIndex( _layerComboBox.getItemCount() - 1);

		update_user_interface(
			true, true,
			VisualShellView.get_instance().exist_datafile(),
			VisualShellView.get_instance().exist_datafile(),
			true, true, true,
			ExperimentManager.get_instance().can_export(),
			ExperimentManager.get_instance().can_export_to_clipboard(),
			true, ( 0 < ExperimentManager.get_instance().get_initial_value_count()),
			can_export(), can_export(), can_export(), can_export(), can_export(),
			LayerManager.get_instance().exist_document_file(),
			true, true, true);

		update( ResourceManager.get_instance().get( "layer.combobox.name")
			+ " [ " + ( _layerComboBox.getSelectedIndex() + 1) + " / "
			+ _layerComboBox.getItemCount() + " ]");

		requestFocus();
	}

	/**
	 * Removes the current layer.
	 * @param actionEvent
	 */
	public void on_remove_layer(ActionEvent actionEvent) {
		if ( 2 > _layerComboBox.getItemCount())
			return;

		if ( !LayerManager.get_instance().can_remove_current_layer()) {
			if ( !WarningManager.get_instance().isEmpty()) {
				WarningDlg1 warningDlg1 = new WarningDlg1(
					this,
					ResourceManager.get_instance().get( "warning.dialog1.title"),
					ResourceManager.get_instance().get( "warning.dialog1.message2"),
					this);
				warningDlg1.do_modal();
			}
			requestFocus();
			return;
		}

		if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
			this,
			ResourceManager.get_instance().get( "layer.confirm.remove.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION)) {

			VisualShellView.get_instance().on_remove_layer();

			_layerComboBox.removeItemAt( _layerComboBox.getItemCount() - 1);

			update_user_interface(
				true, true,
				VisualShellView.get_instance().exist_datafile(),
				VisualShellView.get_instance().exist_datafile(),
				true, true, true,
				ExperimentManager.get_instance().can_export(),
				ExperimentManager.get_instance().can_export_to_clipboard(),
				true, ( 0 < ExperimentManager.get_instance().get_initial_value_count()),
				can_export(), can_export(), can_export(), can_export(), can_export(),
				LayerManager.get_instance().exist_document_file(),
				true, true, ( 1 < _layerComboBox.getItemCount()));

			update( ResourceManager.get_instance().get( "layer.combobox.name")
				+ " [ " + ( _layerComboBox.getSelectedIndex() + 1) + " / "
				+ _layerComboBox.getItemCount() + " ]");
		}

		requestFocus();
	}

	/**
	 * @param size
	 */
	protected void adjust_layer(int size) {
		_synchronize = false;
		_layerComboBox.removeAllItems();
		_synchronize = true;

		for ( int i = 0; i < size; ++i)
			_layerComboBox.addItem( ResourceManager.get_instance().get( "layer.combobox.name")
				+ ( i + 1));

		_layerComboBox.setSelectedIndex( 0);

		update_user_interface(
			true, true,
			VisualShellView.get_instance().exist_datafile(),
			VisualShellView.get_instance().exist_datafile(),
			true, true, true,
			ExperimentManager.get_instance().can_export(),
			ExperimentManager.get_instance().can_export_to_clipboard(),
			true, ( 0 < ExperimentManager.get_instance().get_initial_value_count()),
			can_export(), can_export(), can_export(), can_export(), can_export(),
			LayerManager.get_instance().exist_document_file(),
			true, true, ( 1 < _layerComboBox.getItemCount()));

		update( ResourceManager.get_instance().get( "layer.combobox.name")
			+ " [ " + ( _layerComboBox.getSelectedIndex() + 1) + " / "
			+ _layerComboBox.getItemCount() + " ]");

		requestFocus();
	}

	/**
	 * Invoked on the end of mouse dragging.
	 */
	public void on_end_of_drag() {
		_messageLabel.setText( "");
		requestFocus();
	}

	/**
	 * Updates the status bar with the specified text.
	 * @param information the specified text
	 */
	public void update(String information) {
		_informationLabel.setText( information);
	}

	/**
	 * Updates the statuses of the menus.
	 */
	public void update_menu() {
		_settingExperimentMenuItem.setEnabled( 0 < ExperimentManager.get_instance().get_initial_value_count());
		_settingExperimentButton.setEnabled( 0 < ExperimentManager.get_instance().get_initial_value_count());

		_fileExportToSoarsEngineMenuItem.setEnabled( can_export());

		_runModelBuilderMenuItem.setEnabled( can_export());
		_runModelBuilderButton.setEnabled( can_export());

		_runSimulatorMenuItem.setEnabled( can_export());
		_runSimulatorButton.setEnabled( can_export());

//		_run_model_builder_without_animator_menuItem.setEnabled( can_export());
//		_run_model_builder_without_animator_button.setEnabled( can_export());
//
//		_run_animator_menuItem.setEnabled( can_export());
//		_run_animator_button.setEnabled( can_export());

		_fileExportToFileMenuItem.setEnabled( ExperimentManager.get_instance().can_export());
//		_file_export_button.setEnabled( ExperimentManager.get_instance().can_export());

		_fileExportToClipboardMenuItem.setEnabled( ExperimentManager.get_instance().can_export_to_clipboard());
//		_file_export_to_clipboard_button.setEnabled( ExperimentManager.get_instance().can_export_to_clipboard());
	}

	/**
	 * Returns whether to export the experiment data.
	 * @return whether to export the experiment data
	 */
	public boolean can_export() {
		return ( ( 0 == ExperimentManager.get_instance().get_initial_value_count())
			|| ( ( 0 < ExperimentManager.get_instance().get_initial_value_count())/* && ExperimentManager.get_instance().can_export()*/));
	}

	/**
	 * Returns the instance of the ExperimentManager class.
	 * @return the instance of the ExperimentManager class
	 */
	public ExperimentManager get_experimentManager() {
		return ( ExperimentManager.get_instance().isEmpty() ? null : new ExperimentManager( ExperimentManager.get_instance()));
	}

	/**
	 * Returns true for editing the experiment data successfully.
	 * @param experimentManager the instance of the ExperimentManager class
	 * @param title the title for the dialog box to edit the experiment data
	 * @param parent the parent container of this component
	 * @return true for editing the experiment data successfully
	 */
	public boolean edit_experimentManager(ExperimentManager experimentManager, String title, Component parent) {
		EditExperimentForGridDlg editExperimentForGridDlg = new EditExperimentForGridDlg( this, title, true, parent, experimentManager);
		return editExperimentForGridDlg.do_modal();
	}

	/**
	 * Returns true for editing the experiment data successfully.
	 * @param experimentManager the instance of the ExperimentManager class
	 * @param title the title for the dialog box to edit the experiment data
	 * @param parent the parent container of this component
	 * @param algorithm the algorithm
	 * @return true for editing the experiment data successfully
	 */
	public boolean edit_experimentManager_for_genetic_algorithm(ExperimentManager experimentManager, String title, Component parent, List<String> algorithm) {
		EditExperimentForGeneticAlgorithmDlg editExperimentForGeneticAlgorithmDlg = new EditExperimentForGeneticAlgorithmDlg( this, title, true, parent, experimentManager, algorithm);
		return editExperimentForGeneticAlgorithmDlg.do_modal();
	}

	/**
	 * Sets whether or not this the "Show monitor" menu is enabled.
	 * @param enable true if the "Show monitor" menu should be enabled, false otherwise
	 */
	public void enable_view_show_monitor_menuItem(boolean enable) {
		_viewShowMonitorMenuItem.setEnabled( enable);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_disconnect(java.awt.event.ActionEvent)
	 */
	public void on_disconnect(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_disconnect_one(soars.application.visualshell.object.role.base.inheritance.ConnectObject, java.awt.event.ActionEvent)
	 */
	public void on_disconnect_one(ConnectObject connectObject, ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_edit(java.awt.event.ActionEvent)
	 */
	public void on_edit(ActionEvent actionEvent) {
		StateManager.get_instance().on_edit( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_append(java.awt.event.ActionEvent)
	 */
	public void on_append(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_insert(java.awt.event.ActionEvent)
	 */
	public void on_insert(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_remove(java.awt.event.ActionEvent)
	 */
	public void on_remove(ActionEvent actionEvent) {
		StateManager.get_instance().on_remove( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_move(java.awt.event.ActionEvent)
	 */
	public void on_move(ActionEvent actionEvent) {
		StateManager.get_instance().on_move( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_redo(java.awt.event.ActionEvent)
	 */
	public void on_redo(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_undo(java.awt.event.ActionEvent)
	 */
	public void on_undo(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_copy(java.awt.event.ActionEvent)
	 */
	public void on_copy(ActionEvent actionEvent) {
		StateManager.get_instance().on_copy( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_cut(java.awt.event.ActionEvent)
	 */
	public void on_cut(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_paste(java.awt.event.ActionEvent)
	 */
	public void on_paste(ActionEvent actionEvent) {
		StateManager.get_instance().on_paste( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_select_all(java.awt.event.ActionEvent)
	 */
	public void on_select_all(ActionEvent actionEvent) {
		StateManager.get_instance().on_select_all( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_deselect_all(java.awt.event.ActionEvent)
	 */
	public void on_deselect_all(ActionEvent actionEvent) {
		StateManager.get_instance().on_deselect_all( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_flush_top(java.awt.event.ActionEvent)
	 */
	public void on_flush_top(ActionEvent actionEvent) {
		StateManager.get_instance().on_flush_top( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_flush_bottom(java.awt.event.ActionEvent)
	 */
	public void on_flush_bottom(ActionEvent actionEvent) {
		StateManager.get_instance().on_flush_bottom( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_flush_left(java.awt.event.ActionEvent)
	 */
	public void on_flush_left(ActionEvent actionEvent) {
		StateManager.get_instance().on_flush_left( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_flush_right(java.awt.event.ActionEvent)
	 */
	public void on_flush_right(ActionEvent actionEvent) {
		StateManager.get_instance().on_flush_right( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_vertical_equal_layout(java.awt.event.ActionEvent)
	 */
	public void on_vertical_equal_layout(ActionEvent actionEvent) {
		StateManager.get_instance().on_vertical_equal_layout( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_horizontal_equal_layout(java.awt.event.ActionEvent)
	 */
	public void on_horizontal_equal_layout(ActionEvent actionEvent) {
		StateManager.get_instance().on_horizontal_equal_layout( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_swap_names(java.awt.event.ActionEvent)
	 */
	public void on_swap_names(ActionEvent actionEvent) {
		StateManager.get_instance().on_swap_names( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_copy_objects(java.awt.event.ActionEvent)
	 */
	public void on_copy_objects(ActionEvent actionEvent) {
		StateManager.get_instance().on_copy_objects( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_paste_objects(java.awt.event.ActionEvent)
	 */
	public void on_paste_objects(ActionEvent actionEvent) {
		StateManager.get_instance().on_paste_objects( actionEvent);
	}

	/**
	 * @param enable
	 */
	public void enabled(boolean enable) {
		setEnabled( enable);
		if ( 0 <= System.getProperty( "os.name").indexOf( "Mac")) {
			JMenuBar menuBar = getJMenuBar();
			if ( null != menuBar) {
				for ( int i = 0; i < menuBar.getMenuCount(); ++i) {
					JMenu menu = menuBar.getMenu( i);
					if ( null == menu)
						continue;

					menu.setEnabled( enable);
				}
			}
		}
	}
}
