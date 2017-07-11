/**
 * 
 */
package soars.application.manager.model.main.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import soars.application.manager.model.main.Environment;
import soars.application.manager.model.main.ResourceManager;
import soars.application.manager.model.main.panel.image.ImagePanel;
import soars.application.manager.model.main.panel.tab.contents.SoarsContentsPage;
import soars.application.manager.model.main.panel.tab.doc.SoarsDocPage;
import soars.application.manager.model.main.panel.tree.ModelTree;

/**
 * @author kurata
 *
 */
public class MainPanel extends JSplitPane {

	/**
	 * 
	 */
	private ModelTree _modelTree = null;

	/**
	 * 
	 */
	private JSplitPane _splitPane = null;

	/**
	 * 
	 */
	private ImagePanel _imagePanel = null;

	/**
	 * 
	 */
	private JTabbedPane _tabbedPane = null;

	/**
	 * 
	 */
	private SoarsDocPage _soarsDocPage = null;

	/**
	 * 
	 */
	private SoarsContentsPage _soarsContentsPage = null;

	/**
	 * 
	 */
	private Frame _owner = null;

	/**
	 * 
	 */
	private Component _parent = null;

	/**
	 * @param owner
	 * @param parent
	 */
	public MainPanel(Frame owner, Component parent) {
		super(JSplitPane.HORIZONTAL_SPLIT);
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @param buttonMap
	 * @return
	 */
	public boolean setup(Map<String, JButton> buttonMap) {
		_splitPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT);

		_modelTree = new ModelTree( buttonMap, this, _owner, _parent);

		_imagePanel = new ImagePanel( buttonMap);

		if ( !setup_tabbedPane())
			return false;

		if ( !_modelTree.setup( _imagePanel, _soarsDocPage._soarsDocEditorPane, _soarsContentsPage))
//		if ( !_modelTree.setup( _imagePanel, _soarsDocPage._filenameTextField, _soarsDocPage._soarsDocEditorPane))
			return false;

		if ( !_imagePanel.create( _modelTree))
			return false;

		setup_modelTree();
		setup_imagePanel();
		setup_tabbedPanePanel();

		setRightComponent( _splitPane);

		setDividerLocation( Integer.parseInt( Environment.get_instance().get( Environment._mainPanelDividerLocation1key, "100")));

		_splitPane.setDividerLocation( Integer.parseInt( Environment.get_instance().get( Environment._mainPanelDividerLocation2key, "100")));

		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_tabbedPane() {
		_tabbedPane = new JTabbedPane();

		_soarsDocPage = new SoarsDocPage();
		if ( !_soarsDocPage.setup())
			return false;

		_tabbedPane.add( ResourceManager.get_instance().get( "soars.doc.page.name"), _soarsDocPage);

		_soarsContentsPage = new SoarsContentsPage( _modelTree);
		if ( !_soarsContentsPage.setup())
			return false;

		_tabbedPane.add( ResourceManager.get_instance().get( "soars.contents.page.name"), _soarsContentsPage);

		return true;
	}

	/**
	 * 
	 */
	private void setup_modelTree() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setView( _modelTree);

		setLeftComponent( scrollPane);
	}

	/**
	 * 
	 */
	private void setup_imagePanel() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBackground( new Color( 255, 255, 255));
		scrollPane.getViewport().setView( _imagePanel);

		_splitPane.setTopComponent( scrollPane);
	}

	/**
	 * 
	 */
	private void setup_tabbedPanePanel() {
		_splitPane.setBottomComponent( _tabbedPane);
	}

	/**
	 * 
	 */
	public void optimize_divider_location() {
		setDividerLocation( 100);
		_splitPane.setDividerLocation( 100);
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		_modelTree.on_setup_completed();
	}

	/**
	 * 
	 */
	public void set_property_to_environment_file() {
		Environment.get_instance().set( Environment._mainPanelDividerLocation1key, String.valueOf( getDividerLocation()));
		Environment.get_instance().set( Environment._mainPanelDividerLocation2key, String.valueOf( _splitPane.getDividerLocation()));
		_soarsContentsPage.set_property_to_environment_file();
	}

	/**
	 * 
	 */
	public void update() {
		_modelTree.update();
	}

	/**
	 * 
	 */
	public void on_exit() {
		_modelTree.on_exit();
		_soarsContentsPage.on_exit();
	}
	/**
	 * 
	 */
	public void refresh() {
		_modelTree.refresh();
		_imagePanel.refresh();
		_soarsDocPage.refresh();
		_soarsContentsPage.refresh();
	}

	/**
	 * @param menuItemMap 
	 */
	public void editMenuSelected(Map<String, JMenuItem> menuItemMap) {
		_modelTree.editMenuSelected( menuItemMap);
		_imagePanel.editMenuSelected( menuItemMap);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_copy(ActionEvent actionEvent) {
		_modelTree.on_edit_copy( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_paste(ActionEvent actionEvent) {
		_modelTree.on_edit_paste( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_duplicate(ActionEvent actionEvent) {
		_modelTree.on_edit_duplicate( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_export(ActionEvent actionEvent) {
		_modelTree.on_edit_export( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_remove(ActionEvent actionEvent) {
		_modelTree.on_edit_remove( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_rename(ActionEvent actionEvent) {
		_modelTree.on_edit_rename( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_new_directory(ActionEvent actionEvent) {
		_modelTree.on_edit_new_directory( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_new_simulation_model(ActionEvent actionEvent) {
		_modelTree.on_edit_new_simulation_model( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_model_information(ActionEvent actionEvent) {
		_modelTree.on_edit_model_information( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_import_user_defined_rule(ActionEvent actionEvent) {
		_modelTree.on_edit_import_user_defined_rule( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_update_user_defined_rule(ActionEvent actionEvent) {
		_modelTree.on_edit_update_user_defined_rule( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_export_user_defined_rule(ActionEvent actionEvent) {
		_modelTree.on_edit_export_user_defined_rule( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_remove_user_defined_rule(ActionEvent actionEvent) {
		_modelTree.on_edit_remove_user_defined_rule( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_edit_clear_image(ActionEvent actionEvent) {
		_imagePanel.on_edit_clear_image( actionEvent);
	}

	/**
	 * @param menuItemMap
	 */
	public void runMenuSelected(Map<String, JMenuItem> menuItemMap) {
		_modelTree.runMenuSelected( menuItemMap);
	}

	/**
	 * @param actionEvent
	 */
	public void on_run_start_visual_shell(ActionEvent actionEvent) {
		_modelTree.on_run_start_visual_shell( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_run_start_simulator(ActionEvent actionEvent) {
		_modelTree.on_run_start_simulator( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	public void on_run_start_application_builder(ActionEvent actionEvent) {
		_modelTree.on_run_start_application_builder( actionEvent);
	}

	/**
	 * 
	 */
	public void back() {
		_soarsDocPage.back();
	}

	/**
	 * 
	 */
	public void forward() {
		_soarsDocPage.forward();
	}
}
