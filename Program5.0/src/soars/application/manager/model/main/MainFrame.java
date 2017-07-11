/**
 * 
 */
package soars.application.manager.model.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import soars.application.manager.model.executor.LibraryManager;
import soars.application.manager.model.main.panel.MainPanel;
import soars.application.manager.model.main.startup.ProjectFolderDlg;
import soars.application.manager.model.menu.edit.ClearImageAction;
import soars.application.manager.model.menu.edit.CopyAction;
import soars.application.manager.model.menu.edit.DuplicateAction;
import soars.application.manager.model.menu.edit.ExportAction;
import soars.application.manager.model.menu.edit.ExportUserDefinedRuleAction;
import soars.application.manager.model.menu.edit.IEditMenuHandler;
import soars.application.manager.model.menu.edit.ImportUserDefinedRuleAction;
import soars.application.manager.model.menu.edit.ModelInformationAction;
import soars.application.manager.model.menu.edit.NewDirectoryAction;
import soars.application.manager.model.menu.edit.NewSimulationModelAction;
import soars.application.manager.model.menu.edit.PasteAction;
import soars.application.manager.model.menu.edit.RemoveAction;
import soars.application.manager.model.menu.edit.RemoveUserDefinedRuleAction;
import soars.application.manager.model.menu.edit.RenameAction;
import soars.application.manager.model.menu.edit.UpdateUserDefinedRuleAction;
import soars.application.manager.model.menu.file.ChangeProjectFolderAction;
import soars.application.manager.model.menu.file.ExitAction;
import soars.application.manager.model.menu.help.AboutAction;
import soars.application.manager.model.menu.help.ForumAction;
import soars.application.manager.model.menu.run.IRunMenuHandler;
import soars.application.manager.model.menu.run.StartApplicationBuilderAction;
import soars.application.manager.model.menu.run.StartLibraryManagerAction;
import soars.application.manager.model.menu.run.StartSimulatorAction;
import soars.application.manager.model.menu.run.StartVisualShellAction;
import soars.application.manager.model.menu.view.RefreshAction;
import soars.common.soars.environment.BasicEnvironment;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;
import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.mac.IMacScreenMenuHandler;
import soars.common.utility.swing.mac.MacUtility;
import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.swing.spinner.CustomNumberSpinner;
import soars.common.utility.swing.spinner.INumberSpinnerHandler;
import soars.common.utility.swing.spinner.NumberSpinner;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Frame;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.network.BrowserLauncher;
import soars.common.utility.tool.resource.Resource;

/**
 * @author kurata
 *
 */
public class MainFrame extends Frame implements IMacScreenMenuHandler, IEditMenuHandler, IRunMenuHandler, INumberSpinnerHandler, IMessageCallback {

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
	private JMenuItem _editDuplicateMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editExportMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editRemoveMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editRenameMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editNewDirectoryMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editNewSimulationModelMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editModelInformationMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editImportUserDefinedRuleMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editUpdateUserDefinedRuleMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editExportUserDefinedRuleMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editRemoveUserDefinedRuleMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editClearImageMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _runStartVisualShellMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _runStartSimulatorMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _runStartApplicationBuilderMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _runStartLibraryManagerMenuItem = null;

	/**
	 * 
	 */
	private Map<String, JMenuItem> _menuItemMap = new HashMap<String, JMenuItem>();

	/**
	 * 
	 */
	private JButton _editCopyButton = null;

	/**
	 * 
	 */
	private JButton _editPasteButton = null;

	/**
	 * 
	 */
	private JButton _editRemoveButton = null;

	/**
	 * 
	 */
	private JButton _runStartVisualShellButton = null;

	/**
	 * 
	 */
	private JButton _runStartSimulatorButton = null;

	/**
	 * 
	 */
	private Map<String, JButton> _buttonMap = new HashMap<String, JButton>();

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
	private MainPanel _mainPanel = null;

	/**
	 * 
	 */
	private JLabel _projectFolderLabel = null;

	/**
	 * 
	 */
	private JTextField _projectFolderTextField = null;

	/**
	 * 
	 */
	private ComboBox _memorySizeComboBox = null;

	/**
	 * 
	 */
	private String _memorySize = null;

	/**
	 * 
	 */
	private JCheckBox _memorySizeCheckBox = null;

	/**
	 * 
	 */
	private CustomNumberSpinner _memorySizeNumberSpinner = null;

	/**
	 * 
	 */
	private JLabel _memorySizeLabel = null;

	/**
	 * 
	 */
	private List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * 
	 */
	private JButton _languageButton = null;

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
	 * @param arg0
	 * @throws HeadlessException
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
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( _windowRectangle).height <= getInsets().top) {
			_windowRectangle.setBounds(
				SwingTool.get_default_window_position( _minimumWidth, _minimumHeight).x,
				SwingTool.get_default_window_position( _minimumWidth, _minimumHeight).y,
				_minimumWidth, _minimumHeight);
			_mainPanel.optimize_divider_location();
		}
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
	public void store() {
		set_properties();
		CommonEnvironment.get_instance().store();
		BasicEnvironment.get_instance().store();
	}

	/**
	 * 
	 */
	private void set_properties() {
		CommonEnvironment.get_instance().set_memory_size( _memorySize);
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


		ChangeProjectFolderAction changeProjectFolderAction = new ChangeProjectFolderAction( ResourceManager.get_instance().get( "file.change.project.folder.menu"));
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "file.change.project.folder.menu"),
			changeProjectFolderAction,
			ResourceManager.get_instance().get( "file.change.project.folder.mnemonic"),
			ResourceManager.get_instance().get( "file.change.project.folder.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "file.change.project.folder.message"));


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



		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "edit.menu"),
			true,
			ResourceManager.get_instance().get( "edit.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "edit.message"));
		menu.addMenuListener( new MenuListener() {
			public void menuSelected(MenuEvent e) {
				_mainPanel.editMenuSelected( _menuItemMap);
			}
			public void menuDeselected(MenuEvent e) {
			}
			public void menuCanceled(MenuEvent e) {
			}
		});


		CopyAction copyAction = new CopyAction( ResourceManager.get_instance().get( "edit.copy.menu"), this);
		_editCopyMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.copy.menu"),
			copyAction,
			ResourceManager.get_instance().get( "edit.copy.mnemonic"),
			ResourceManager.get_instance().get( "edit.copy.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "edit.copy.message"));
		_menuItemMap.put( ResourceManager.get_instance().get( "edit.copy.menu"), _editCopyMenuItem);


		PasteAction pasteAction = new PasteAction( ResourceManager.get_instance().get( "edit.paste.menu"), this);
		_editPasteMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.paste.menu"),
			pasteAction,
			ResourceManager.get_instance().get( "edit.paste.mnemonic"),
			ResourceManager.get_instance().get( "edit.paste.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "edit.paste.message"));
		_menuItemMap.put( ResourceManager.get_instance().get( "edit.paste.menu"), _editPasteMenuItem);


		DuplicateAction duplicateAction = new DuplicateAction( ResourceManager.get_instance().get( "edit.duplicate.menu"), this);
		_editDuplicateMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.duplicate.menu"),
			duplicateAction,
			ResourceManager.get_instance().get( "edit.duplicate.mnemonic"),
			ResourceManager.get_instance().get( "edit.duplicate.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "edit.duplicate.message"));
		_menuItemMap.put( ResourceManager.get_instance().get( "edit.duplicate.menu"), _editDuplicateMenuItem);

		ExportAction exportAction = new ExportAction( ResourceManager.get_instance().get( "edit.export.menu"), this);
		_editExportMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.export.menu"),
			exportAction,
			ResourceManager.get_instance().get( "edit.export.mnemonic"),
			ResourceManager.get_instance().get( "edit.export.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "edit.export.message"));
		_menuItemMap.put( ResourceManager.get_instance().get( "edit.export.menu"), _editExportMenuItem);


		menu.addSeparator();


		RemoveAction removeAction = new RemoveAction( ResourceManager.get_instance().get( "edit.remove.menu"), this);
		_editRemoveMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.remove.menu"),
			removeAction,
			ResourceManager.get_instance().get( "edit.remove.mnemonic"),
			ResourceManager.get_instance().get( "edit.remove.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "edit.remove.message"));
		_menuItemMap.put( ResourceManager.get_instance().get( "edit.remove.menu"), _editRemoveMenuItem);


		menu.addSeparator();


		RenameAction renameAction = new RenameAction( ResourceManager.get_instance().get( "edit.rename.menu"), this);
		_editRenameMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.rename.menu"),
			renameAction,
			ResourceManager.get_instance().get( "edit.rename.mnemonic"),
			ResourceManager.get_instance().get( "edit.rename.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "edit.rename.message"));
		_menuItemMap.put( ResourceManager.get_instance().get( "edit.rename.menu"), _editRenameMenuItem);


		menu.addSeparator();


		NewDirectoryAction newDirectoryAction = new NewDirectoryAction( ResourceManager.get_instance().get( "edit.new.directory.menu"), this);
		_editNewDirectoryMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.new.directory.menu"),
			newDirectoryAction,
			ResourceManager.get_instance().get( "edit.new.directory.mnemonic"),
			ResourceManager.get_instance().get( "edit.new.directory.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "edit.new.directory.message"));
		_menuItemMap.put( ResourceManager.get_instance().get( "edit.new.directory.menu"), _editNewDirectoryMenuItem);


		menu.addSeparator();


		NewSimulationModelAction newSimulationModelAction = new NewSimulationModelAction( ResourceManager.get_instance().get( "edit.new.simulation.model.menu"), this);
		_editNewSimulationModelMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.new.simulation.model.menu"),
			newSimulationModelAction,
			ResourceManager.get_instance().get( "edit.new.simulation.model.mnemonic"),
			ResourceManager.get_instance().get( "edit.new.simulation.model.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "edit.new.simulation.model.message"));
		_menuItemMap.put( ResourceManager.get_instance().get( "edit.new.simulation.model.menu"), _editNewSimulationModelMenuItem);


		ModelInformationAction modelInformationAction = new ModelInformationAction( ResourceManager.get_instance().get( "edit.model.information.menu"), this);
		_editModelInformationMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.model.information.menu"),
			modelInformationAction,
			ResourceManager.get_instance().get( "edit.model.information.mnemonic"),
			ResourceManager.get_instance().get( "edit.model.information.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "edit.model.information.message"));
		_menuItemMap.put( ResourceManager.get_instance().get( "edit.model.information.menu"), _editModelInformationMenuItem);


		menu.addSeparator();


		ImportUserDefinedRuleAction importUserDefinedRuleAction = new ImportUserDefinedRuleAction( ResourceManager.get_instance().get( "edit.import.user.defined.rule.menu"), this);
		_editImportUserDefinedRuleMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.import.user.defined.rule.menu"),
			importUserDefinedRuleAction,
			ResourceManager.get_instance().get( "edit.import.user.defined.rule.mnemonic"),
			ResourceManager.get_instance().get( "edit.import.user.defined.rule.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "edit.import.user.defined.rule.message"));
		_menuItemMap.put( ResourceManager.get_instance().get( "edit.import.user.defined.rule.menu"), _editImportUserDefinedRuleMenuItem);

		UpdateUserDefinedRuleAction updateUserDefinedRuleAction = new UpdateUserDefinedRuleAction( ResourceManager.get_instance().get( "edit.update.user.defined.rule.menu"), this);
		_editUpdateUserDefinedRuleMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.update.user.defined.rule.menu"),
			updateUserDefinedRuleAction,
			ResourceManager.get_instance().get( "edit.update.user.defined.rule.mnemonic"),
			ResourceManager.get_instance().get( "edit.update.user.defined.rule.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "edit.update.user.defined.rule.message"));
		_menuItemMap.put( ResourceManager.get_instance().get( "edit.update.user.defined.rule.menu"), _editUpdateUserDefinedRuleMenuItem);

		ExportUserDefinedRuleAction exportUserDefinedRuleAction = new ExportUserDefinedRuleAction( ResourceManager.get_instance().get( "edit.export.user.defined.rule.menu"), this);
		_editExportUserDefinedRuleMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.export.user.defined.rule.menu"),
			exportUserDefinedRuleAction,
			ResourceManager.get_instance().get( "edit.export.user.defined.rule.mnemonic"),
			ResourceManager.get_instance().get( "edit.export.user.defined.rule.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "edit.export.user.defined.rule.message"));
		_menuItemMap.put( ResourceManager.get_instance().get( "edit.export.user.defined.rule.menu"), _editExportUserDefinedRuleMenuItem);

		RemoveUserDefinedRuleAction removeUserDefinedRuleAction = new RemoveUserDefinedRuleAction( ResourceManager.get_instance().get( "edit.remove.user.defined.rule.menu"), this);
		_editRemoveUserDefinedRuleMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.remove.user.defined.rule.menu"),
			removeUserDefinedRuleAction,
			ResourceManager.get_instance().get( "edit.remove.user.defined.rule.mnemonic"),
			ResourceManager.get_instance().get( "edit.remove.user.defined.rule.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "edit.remove.user.defined.rule.message"));
		_menuItemMap.put( ResourceManager.get_instance().get( "edit.remove.user.defined.rule.menu"), _editRemoveUserDefinedRuleMenuItem);


		menu.addSeparator();


		ClearImageAction clearImageAction = new ClearImageAction( ResourceManager.get_instance().get( "edit.clear.image.menu"), this);
		_editClearImageMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "edit.clear.image.menu"),
			clearImageAction,
			ResourceManager.get_instance().get( "edit.clear.image.mnemonic"),
			ResourceManager.get_instance().get( "edit.clear.image.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "edit.clear.image.message"));
		_menuItemMap.put( ResourceManager.get_instance().get( "edit.clear.image.menu"), _editClearImageMenuItem);



		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "view.menu"),
			true,
			ResourceManager.get_instance().get( "view.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "view.message"));


		RefreshAction refreshAction = new RefreshAction( ResourceManager.get_instance().get( "view.refresh.menu"));
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "view.refresh.menu"),
			refreshAction,
			ResourceManager.get_instance().get( "view.refresh.mnemonic"),
			ResourceManager.get_instance().get( "view.refresh.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "view.refresh.message"));



//		menu = _userInterface.append_menu(
//			menuBar,
//			ResourceManager.get_instance().get( "history.menu"),
//			true,
//			ResourceManager.get_instance().get( "history.mnemonic"),
//			_message_label,
//			ResourceManager.get_instance().get( "history.message"));
//
//
//		BackAction backAction = new BackAction( ResourceManager.get_instance().get( "history.back.menu"));
//		_userInterface.append_menuitem(
//			menu,
//			ResourceManager.get_instance().get( "history.back.menu"),
//			backAction,
//			ResourceManager.get_instance().get( "history.back.mnemonic"),
//			ResourceManager.get_instance().get( "history.back.stroke"),
//			_message_label,
//			ResourceManager.get_instance().get( "history.back.message"));
//
//
//		ForwardAction forwardAction = new ForwardAction( ResourceManager.get_instance().get( "history.forward.menu"));
//		_userInterface.append_menuitem(
//			menu,
//			ResourceManager.get_instance().get( "history.forward.menu"),
//			forwardAction,
//			ResourceManager.get_instance().get( "history.forward.mnemonic"),
//			ResourceManager.get_instance().get( "history.forward.stroke"),
//			_message_label,
//			ResourceManager.get_instance().get( "history.forward.message"));



		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "run.menu"),
			true,
			ResourceManager.get_instance().get( "run.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.message"));
		menu.addMenuListener( new MenuListener() {
			public void menuSelected(MenuEvent e) {
				_mainPanel.runMenuSelected( _menuItemMap);
			}
			public void menuDeselected(MenuEvent e) {
			}
			public void menuCanceled(MenuEvent e) {
			}
		});


		StartVisualShellAction startVisualShellAction = new StartVisualShellAction( ResourceManager.get_instance().get( "run.start.visual.shell.menu"), this);
		_runStartVisualShellMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "run.start.visual.shell.menu"),
			startVisualShellAction,
			ResourceManager.get_instance().get( "run.start.visual.shell.mnemonic"),
			ResourceManager.get_instance().get( "run.start.visual.shell.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.start.visual.shell.message"));
		_menuItemMap.put( ResourceManager.get_instance().get( "run.start.visual.shell.menu"), _runStartVisualShellMenuItem);


		StartSimulatorAction startSimulatorAction = new StartSimulatorAction( ResourceManager.get_instance().get( "run.start.simulator.menu"), this);
		_runStartSimulatorMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "run.start.simulator.menu"),
			startSimulatorAction,
			ResourceManager.get_instance().get( "run.start.simulator.mnemonic"),
			ResourceManager.get_instance().get( "run.start.simulator.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.start.simulator.message"));
		_menuItemMap.put( ResourceManager.get_instance().get( "run.start.simulator.menu"), _runStartSimulatorMenuItem);


		menu.addSeparator();


		StartApplicationBuilderAction startApplicationBuilderAction = new StartApplicationBuilderAction( ResourceManager.get_instance().get( "run.start.application.builder.menu"), this);
		_runStartApplicationBuilderMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "run.start.application.builder.menu"),
			startApplicationBuilderAction,
			ResourceManager.get_instance().get( "run.start.application.builder.mnemonic"),
			ResourceManager.get_instance().get( "run.start.application.builder.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.start.application.builder.message"));
		_menuItemMap.put( ResourceManager.get_instance().get( "run.start.application.builder.menu"), _runStartApplicationBuilderMenuItem);


		menu.addSeparator();


		StartLibraryManagerAction startLibraryManagerAction = new StartLibraryManagerAction( ResourceManager.get_instance().get( "run.start.library.manager.menu"), this);
		_runStartLibraryManagerMenuItem = _userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "run.start.library.manager.menu"),
			startLibraryManagerAction,
			ResourceManager.get_instance().get( "run.start.library.manager.mnemonic"),
			ResourceManager.get_instance().get( "run.start.library.manager.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.start.library.manager.message"));
		_menuItemMap.put( ResourceManager.get_instance().get( "run.start.library.manager.menu"), _runStartLibraryManagerMenuItem);



		menu = _userInterface.append_menu(
			menuBar,
			ResourceManager.get_instance().get( "help.menu"),
			true,
			ResourceManager.get_instance().get( "help.mnemonic"),
			_messageLabel,
			ResourceManager.get_instance().get( "help.message"));


		ForumAction forumAction = new ForumAction( ResourceManager.get_instance().get( "help.forum.menu"));
		_userInterface.append_menuitem(
			menu,
			ResourceManager.get_instance().get( "help.forum.menu"),
			forumAction,
			ResourceManager.get_instance().get( "help.forum.mnemonic"),
			ResourceManager.get_instance().get( "help.forum.stroke"),
			_messageLabel,
			ResourceManager.get_instance().get( "help.forum.message"));


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


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/copy.png"));
		_editCopyButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "edit.copy.menu"),
			copyAction,
			ResourceManager.get_instance().get( "edit.copy.menu"),
			_messageLabel,
			ResourceManager.get_instance().get( "edit.copy.menu"));
		_editCopyButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _editCopyButton.getPreferredSize().height));
		_buttonMap.put( ResourceManager.get_instance().get( "edit.copy.menu"), _editCopyButton);


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/paste.png"));
		_editPasteButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "edit.paste.menu"),
			pasteAction,
			ResourceManager.get_instance().get( "edit.paste.menu"),
			_messageLabel,
			ResourceManager.get_instance().get( "edit.paste.menu"));
		_editPasteButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _editPasteButton.getPreferredSize().height));
		_buttonMap.put( ResourceManager.get_instance().get( "edit.paste.menu"), _editPasteButton);


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/delete.png"));
		_editRemoveButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "edit.remove.menu"),
			removeAction,
			ResourceManager.get_instance().get( "edit.remove.menu"),
			_messageLabel,
			ResourceManager.get_instance().get( "edit.remove.menu"));
		_editRemoveButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _editRemoveButton.getPreferredSize().height));
		_buttonMap.put( ResourceManager.get_instance().get( "edit.remove.menu"), _editRemoveButton);


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/view/refresh.png"));
		button = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "view.refresh.menu"),
			refreshAction,
			ResourceManager.get_instance().get( "view.refresh.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "view.refresh.message"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/run/start_visual_shell.png"));
		_runStartVisualShellButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "run.start.visual.shell.menu"),
			startVisualShellAction,
			ResourceManager.get_instance().get( "run.start.visual.shell.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.start.visual.shell.message"));
		_runStartVisualShellButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));
		_buttonMap.put( ResourceManager.get_instance().get( "run.start.visual.shell.menu"), _runStartVisualShellButton);


		toolBar.addSeparator();


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/run/start_simulator.png"));
		_runStartSimulatorButton = _userInterface.append_tool_button(
			toolBar,
			imageIcon,
			ResourceManager.get_instance().get( "run.start.simulator.menu"),
			startSimulatorAction,
			ResourceManager.get_instance().get( "run.start.simulator.tooltip"),
			_messageLabel,
			ResourceManager.get_instance().get( "run.start.simulator.message"));
		_runStartSimulatorButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));
		_buttonMap.put( ResourceManager.get_instance().get( "run.start.simulator.menu"), _runStartSimulatorButton);


		toolBar.setFloatable( false);

		//toolBar.setEnabled( false);

		getContentPane().add( toolBar, BorderLayout.NORTH);
	}


	/**
	 * @param startup
	 * @return
	 */
	public boolean create(boolean startup) {
		if (!super.create())
			return false;

		if ( startup) {
			if ( !startup())
				return false;
		} else {
			_mainPanel.update();
			_projectFolderTextField.setText( BasicEnvironment.get_instance().get( BasicEnvironment._projectFolderKey + "0", ""));
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		if ( !MacUtility.setup_screen_menu_handler( this, this, ResourceManager.get_instance().get( "application.title")))
			return false;

		get_property_from_environment_file();

		_userInterface = new UserInterface();

		setup_menu();

		if ( !setup())
			return false;

		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

//		new DropTarget( this, this);

		pack();

		optimize_window_rectangle();
		setLocation( _windowRectangle.x, _windowRectangle.y);
		setSize( _windowRectangle.width, _windowRectangle.height);

		Toolkit.getDefaultToolkit().setDynamicLayout( true);

		_mainPanel.on_setup_completed();

		Image image = Resource.load_image_from_resource( Constant._resourceDirectory + "/image/icon/icon.png", getClass());
		if ( null != image)
			setIconImage( image);

		setVisible( true);

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

	/**
	 * @return
	 */
	private boolean setup() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.X_AXIS));

		setup_north_panel( northPanel);

		panel.add( northPanel, "North");


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		if ( !setup_center_panel( centerPanel))
			return false;

		panel.add( centerPanel);

		initialize();

		adjust();


		getContentPane().add( panel);

		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_north_panel(JPanel parent) {
		setup_projectFolderTextField( parent);
		setup_default_memory_panel( parent);
		setup_advanced_memory_panel( parent);
		setup_language_panel( parent);
	}

	/**
	 * @param parent
	 */
	private void setup_projectFolderTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_projectFolderLabel = new JLabel( ResourceManager.get_instance().get( "project.folder.label"));
		panel.add( _projectFolderLabel);

		panel.add( Box.createHorizontalStrut( 5));

		_projectFolderTextField = new JTextField();
		_projectFolderTextField.setEditable( false);
		panel.add( _projectFolderTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_default_memory_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_memorySizeLabel = new JLabel( ResourceManager.get_instance().get( "soars.manager.main.frame.memory.size.label"));
		panel.add( _memorySizeLabel);

		panel.add( Box.createHorizontalStrut( 5));

		String[] memorySizes = new String[ 1 + Constant._memorySizes.length];
		memorySizes[ 0] = ResourceManager.get_instance().get( "soars.manager.main.frame.memory.non.use");
		System.arraycopy( Constant._memorySizes, 0, memorySizes, 1, Constant._memorySizes.length);
		_memorySizeComboBox = ComboBox.create( memorySizes, 100, true, new CommonComboBoxRenderer( null, true));
		_memorySizeComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String memorySize = ( String)_memorySizeComboBox.getSelectedItem();
				_memorySize = ( memorySize.equals( ResourceManager.get_instance().get( "soars.manager.main.frame.memory.non.use")) ? "0" : memorySize);
			}
		});
		panel.add( _memorySizeComboBox);

		JLabel label = new JLabel( "MB");
		_labels.add( label);
		panel.add( label);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_advanced_memory_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_memorySizeCheckBox = new JCheckBox( ResourceManager.get_instance().get( "soars.manager.main.frame.advanced.setting.label"));
		_memorySizeCheckBox.setSelected( CommonEnvironment.get_instance().get( CommonEnvironment._advancedMemorySettingKey, Constant._defaultAdvancedMemorySetting).equals( "true"));
		_memorySizeCheckBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				on_state_changed( ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _memorySizeCheckBox);

		panel.add( Box.createHorizontalStrut( 5));

		_memorySizeNumberSpinner = new CustomNumberSpinner( this);
		_memorySizeNumberSpinner.set_minimum( 0);
		_memorySizeNumberSpinner.set_maximum( 1000000);
		_memorySizeNumberSpinner.setPreferredSize( new Dimension( 100,
			_memorySizeNumberSpinner.getPreferredSize().height));
		panel.add( _memorySizeNumberSpinner);

		JLabel label = new JLabel( "MB");
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param selected
	 */
	protected void on_state_changed(boolean selected) {
		_memorySizeLabel.setEnabled( !selected);
		_memorySizeComboBox.setEnabled( !selected);
		_labels.get( 0).setEnabled( !selected);
		_memorySizeNumberSpinner.setEnabled( selected);
		_labels.get( 1).setEnabled( selected);
		if ( selected)
			_memorySize = String.valueOf( _memorySizeNumberSpinner.get_value());
		else {
			String memorySize = ( String)_memorySizeComboBox.getSelectedItem();
			_memorySize = ( memorySize.equals( ResourceManager.get_instance().get( "soars.manager.main.frame.memory.non.use")) ? "0" : memorySize);
		}
		CommonEnvironment.get_instance().set( CommonEnvironment._advancedMemorySettingKey, String.valueOf( selected));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.spinner.INumberSpinnerHandler#changed(java.lang.String, soars.common.utility.swing.spinner.NumberSpinner)
	 */
	public void changed(String number, NumberSpinner numberSpinner) {
		number = ( number.equals( "") ? "0" : number);
		if ( numberSpinner.equals( _memorySizeNumberSpinner))
			_memorySize = number;
	}

	/**
	 * @param parent
	 */
	private void setup_language_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_languageButton = new JButton( ResourceManager.get_instance().get( "soars.manager.main.frame.language"));
		_languageButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_language( arg0);
			}
		});
		panel.add( _languageButton);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_language(ActionEvent actionEvent) {
		String language = CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, "en");
		LanguageSelectorDlg languageSelectorDlg = new LanguageSelectorDlg( this, ResourceManager.get_instance().get( "soars.manager.main.frame.language"), true);
		if ( !languageSelectorDlg.do_modal( this))
			return;

		if ( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, "en").equals( language))
			return;

		try {
			set_property_to_environment_file();
		}
		catch (IOException e) {
			throw new RuntimeException( e);
		}

		store();

		if ( !MessageDlg.execute( this, ResourceManager.get_instance().get( "application.title"), true,
			"reboot", ResourceManager.get_instance().get( "soars.manager.main.frame.soars.reboot.messsage"), this, this)) {
			JOptionPane.showMessageDialog( this,
				ResourceManager.get_instance().get( "soars.manager.main.frame.soars.reboot.error.messsage"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}

		dispose();
		System.exit( 0);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IMessageCallback#message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.MessageDlg)
	 */
	public boolean message_callback(String id, Object[] objects, MessageDlg messageDlg) {
		if ( id.equals( "reboot"))
			return reboot();

		return true;
	}

	/**
	 * @return
	 */
	private boolean reboot() {
		File current_directory = new File( "");
		if ( null == current_directory)
			return false;

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec( get_cmdarray( current_directory));
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param current_directory
	 * @return
	 */
	private String[] get_cmdarray(File current_directory) {
		String homeDirectoryName = System.getProperty( Constant._soarsHome);
		String propertyDirectoryName = System.getProperty( Constant._soarsProperty);
		String soarsProperties = System.getProperty( Constant._soarsProperties);

		String memorySize = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");

		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant.get_mac_java_command());
			//if ( System.getProperty( Constant._systemDefaultFileEncoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
			list.add( "-D" + Constant._systemDefaultFileEncoding + "=" + System.getProperty( Constant._systemDefaultFileEncoding, ""));
			list.add( "-X" + Constant._macScreenMenuName + "=SOARS Model Manager");
			//list.add( "-D" + Constant._macScreenMenuName + "=SOARS Model Manager");
			list.add( "-X" + Constant._macIconFilename + "=../resource/icon/application/manager/soars/icon.png");
			list.add( "-D" + Constant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

		list.add( "-D" + Constant._soarsHome + "=" + homeDirectoryName);
		list.add( "-D" + Constant._soarsProperty + "=" + propertyDirectoryName);
		list.add( "-D" + Constant._soarsProperties + "=" + soarsProperties);
		if ( !memorySize.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memorySize);
//			list.add( "-Xmx" + memorySize + "m");
		}
		list.add( "-Xmx" + Constant._defaultMemorySize + "m");
		list.add( "-jar");
		list.add( homeDirectoryName + "/" + Constant._soarsManagerJarFilename);
		list.add( "-language");
		list.add( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, Locale.getDefault().getLanguage()));

		return list.toArray( new String[ 0]);
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_center_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_mainPanel = new MainPanel( this, this);
		if ( !_mainPanel.setup( _buttonMap))
			return false;

		panel.add( _mainPanel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
		return true;
	}

	/**
	 * 
	 */
	private void initialize() {
		_memorySize = CommonEnvironment.get_instance().get_memory_size();

		_memorySizeLabel.setEnabled( !_memorySizeCheckBox.isSelected());
		_memorySizeComboBox.setEnabled( !_memorySizeCheckBox.isSelected());
		_labels.get( 0).setEnabled( !_memorySizeCheckBox.isSelected());
		_memorySizeNumberSpinner.setEnabled( _memorySizeCheckBox.isSelected());
		_labels.get( 1).setEnabled( _memorySizeCheckBox.isSelected());

		_memorySizeNumberSpinner.set_value( new Integer( _memorySize).intValue());
		if ( !_memorySizeCheckBox.isSelected()) {
			if ( _memorySize.equals( "0") || Constant.contained( _memorySize))
				_memorySizeComboBox.setSelectedItem( _memorySize.equals( ResourceManager.get_instance().get( "launcher.dialog.memory.non.use")) ? "0" : _memorySize);
			else {
				_memorySizeCheckBox.setSelected( true);
				_memorySizeNumberSpinner.set_value( new Integer( _memorySize).intValue());
			}
		}
	}

	/**
	 * 
	 */
	private void adjust() {
	}

	/**
	 * 
	 */
	private boolean startup() {
		if ( BasicEnvironment.get_instance().get( BasicEnvironment._defaultProjectFolderKey, "false").equals( "true")
			&& !BasicEnvironment.get_instance().get( BasicEnvironment._projectFolderKey + "0", "").equals( "")) {
			File projectFolder = new File( BasicEnvironment.get_instance().get( BasicEnvironment._projectFolderKey + "0", ""));
			if ( ( projectFolder.exists() && projectFolder.isDirectory()) || ( !projectFolder.exists() && projectFolder.mkdirs())) {
				if ( !BasicEnvironment.get_instance().create_projectSubFolers( projectFolder))
					return false;

				_mainPanel.update();
				_projectFolderTextField.setText( BasicEnvironment.get_instance().get( BasicEnvironment._projectFolderKey + "0", ""));
				return true;
			}
		} else {
			ProjectFolderDlg projectFolderDlg = new ProjectFolderDlg( this, ResourceManager.get_instance().get( "application.title"), true, ResourceManager.get_instance().get( "project.folder.dialog.exit"), false);
			if ( !projectFolderDlg.do_modal( this))
				System.exit( 0);
		}

		_mainPanel.update();
		_projectFolderTextField.setText( BasicEnvironment.get_instance().get( BasicEnvironment._projectFolderKey + "0", ""));

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.mac.IMacScreenMenuHandler#on_mac_about()
	 */
	public void on_mac_about() {
		on_help_about( null);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.mac.IMacScreenMenuHandler#on_mac_quit()
	 */
	public void on_mac_quit() {
		on_file_exit( null);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_window_closing(java.awt.event.WindowEvent)
	 */
	protected void on_window_closing(WindowEvent windowEvent) {
		if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
			this,
			ResourceManager.get_instance().get( "file.exit.confirm.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION)) {
			requestFocus();
			return;
		}

		_mainPanel.set_property_to_environment_file();
		_mainPanel.on_exit();

		_windowRectangle = getBounds();
		try {
			set_property_to_environment_file();
		}
		catch (IOException e) {
			throw new RuntimeException( e);
		}

		store();

		System.exit( 0);
	}

	/**
	 * @param actionEvent
	 */
	public void on_file_exit(ActionEvent actionEvent) {
		if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
			this,
			ResourceManager.get_instance().get( "file.exit.confirm.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION)) {
			requestFocus();
			return;
		}

		_mainPanel.set_property_to_environment_file();
		_mainPanel.on_exit();

		_windowRectangle = getBounds();
		try {
			set_property_to_environment_file();
		}
		catch (IOException e) {
			throw new RuntimeException( e);
		}

		store();

		System.exit( 0);
	}

	/**
	 * @param actionEvent
	 */
	public void on_file_change_project_folder(ActionEvent actionEvent) {
		ProjectFolderDlg projectFolderDlg = new ProjectFolderDlg( this, ResourceManager.get_instance().get( "application.title"), true, ResourceManager.get_instance().get( "dialog.cancel"), true);
		if ( !projectFolderDlg.do_modal( this))
			return;

		_mainPanel.update();
		_projectFolderTextField.setText( BasicEnvironment.get_instance().get( BasicEnvironment._projectFolderKey + "0", ""));
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_copy(java.awt.event.ActionEvent)
	 */
	public void on_edit_copy(ActionEvent actionEvent) {
		_mainPanel.on_edit_copy( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_duplicate(java.awt.event.ActionEvent)
	 */
	public void on_edit_duplicate(ActionEvent actionEvent) {
		_mainPanel.on_edit_duplicate( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_paste(java.awt.event.ActionEvent)
	 */
	public void on_edit_paste(ActionEvent actionEvent) {
		_mainPanel.on_edit_paste( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_export(java.awt.event.ActionEvent)
	 */
	public void on_edit_export(ActionEvent actionEvent) {
		_mainPanel.on_edit_export( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_remove(java.awt.event.ActionEvent)
	 */
	public void on_edit_remove(ActionEvent actionEvent) {
		_mainPanel.on_edit_remove( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_rename(java.awt.event.ActionEvent)
	 */
	public void on_edit_rename(ActionEvent actionEvent) {
		_mainPanel.on_edit_rename( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_new_directory(java.awt.event.ActionEvent)
	 */
	public void on_edit_new_directory(ActionEvent actionEvent) {
		_mainPanel.on_edit_new_directory( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_new_simulation_model(java.awt.event.ActionEvent)
	 */
	public void on_edit_new_simulation_model(ActionEvent actionEvent) {
		_mainPanel.on_edit_new_simulation_model( actionEvent);
	}
	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_model_information(java.awt.event.ActionEvent)
	 */
	public void on_edit_model_information(ActionEvent actionEvent) {
		_mainPanel.on_edit_model_information( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_import_user_defined_rule(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_edit_import_user_defined_rule(ActionEvent actionEvent) {
		_mainPanel.on_edit_import_user_defined_rule( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_update_user_defined_rule(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_edit_update_user_defined_rule(ActionEvent actionEvent) {
		_mainPanel.on_edit_update_user_defined_rule( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_export_user_defined_rule(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_edit_export_user_defined_rule(ActionEvent actionEvent) {
		_mainPanel.on_edit_export_user_defined_rule( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_remove_user_defined_rule(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_edit_remove_user_defined_rule(ActionEvent actionEvent) {
		_mainPanel.on_edit_remove_user_defined_rule( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.edit.IEditMenuHandler#on_edit_clear_image(java.awt.event.ActionEvent)
	 */
	public void on_edit_clear_image(ActionEvent actionEvent) {
		_mainPanel.on_edit_clear_image( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_view_refresh(ActionEvent actionEvent) {
		_mainPanel.refresh();
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.run.IRunMenuHandler#on_run_start_visual_shell(java.awt.event.ActionEvent)
	 */
	public void on_run_start_visual_shell(ActionEvent actionEvent) {
		_mainPanel.on_run_start_visual_shell( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.run.IRunMenuHandler#on_run_start_simulator(java.awt.event.ActionEvent)
	 */
	public void on_run_start_simulator(ActionEvent actionEvent) {
		_mainPanel.on_run_start_simulator( actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.run.IRunMenuHandler#on_run_start_library_manager(java.awt.event.ActionEvent)
	 */
	public void on_run_start_library_manager(ActionEvent actionEvent) {
		store();
		LibraryManager.start();
	}

	/* (non-Javadoc)
	 * @see soars.application.manager.model.menu.run.IRunMenuHandler#on_run_start_application_builder(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_run_start_application_builder(ActionEvent actionEvent) {
		store();
		_mainPanel.on_run_start_application_builder( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_history_back(ActionEvent actionEvent) {
		_mainPanel.back();
	}

	/**
	 * @param actionEvent
	 */
	public void on_history_forward(ActionEvent actionEvent) {
		_mainPanel.forward();
	}

	/**
	 * @param actionEvent
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
	 * @param actionEvent
	 */
	public void on_help_about(ActionEvent actionEvent) {
		JOptionPane.showMessageDialog( this,
			Constant.get_version_message(),
			ResourceManager.get_instance().get( "application.title"),
//			JOptionPane.INFORMATION_MESSAGE,
//			new ImageIcon( Resource.load_image_from_resource( Constant._resource_directory + "/image/picture/picture.jpg", getClass())));
			JOptionPane.INFORMATION_MESSAGE);

		requestFocus();
	}

	/**
	 * @param title
	 * @param filename
	 */
	public void update_title(String title, String filename) {
		if ( filename.equals( ""))
			setTitle( ResourceManager.get_instance().get( "application.title"));
		else
			setTitle( ResourceManager.get_instance().get( "application.title")
				+ " - [" + ( ( null == title || title.equals( "")) ? ResourceManager.get_instance().get( "model.tree.no.title") : title) + "]"
				+ ( filename.equals( "") ? "" : ( " <" + filename + ">")));
	}
}
