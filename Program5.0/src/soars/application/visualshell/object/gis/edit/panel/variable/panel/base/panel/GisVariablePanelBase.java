/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase;
import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;
import soars.application.visualshell.object.gis.edit.panel.base.GisVariableTableBase;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextUndoRedoManager;

/**
 * @author kurata
 *
 */
public class GisVariablePanelBase extends GisPanelBase {

	/**
	 * @param kind
	 * @param gisDataManager
	 * @param gisPropertyPanelBaseMap
	 * @param gisVariableTableBase 
	 * @param color 
	 * @param owner
	 * @param parent
	 */
	public GisVariablePanelBase(String kind, GisDataManager gisDataManager, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, GisVariableTableBase gisVariableTableBase, Color color, Frame owner, Component parent) {
		super(kind, gisDataManager, gisPropertyPanelBaseMap, gisVariableTableBase, color, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#setup()
	 */
	@Override
	public boolean setup() {
		if ( !super.setup())
			return false;


		setLayout( new BorderLayout());


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new GridLayout( 1, 2));

		setup_center_panel( centerPanel);

		add( centerPanel);


		JPanel eastPanel = new JPanel();
		eastPanel.setLayout( new BorderLayout());

		setup_buttons( eastPanel);

		add( eastPanel, "East");


		return true;
	}

	/**
	 * @param parent
	 */
	protected void setup_name_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( get_name_label_text());
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

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @return
	 */
	protected String get_name_label_text() {
		return null;
	}

	/**
	 * @param parent
	 */
	protected void setup_comment_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.variable.dialog.comment"));
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

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param object
	 */
	public void changeSelection(Object object) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#on_append(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_append(ActionEvent actionEvent) {
		GisObjectBase gisObjectBase = on_append();
		if ( null == gisObjectBase)
			return;

		_gisVariableTableBase.append( gisObjectBase);

		update();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#on_update(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_update(ActionEvent actionEvent) {
		int[] rows = _gisVariableTableBase.getSelectedRows();
		if ( null == rows || 1 != rows.length)
			return;

		GisObjectBase gisObjectBase = ( GisObjectBase)_gisVariableTableBase.getValueAt( rows[ 0], 0);
		update( rows[ 0], gisObjectBase, true);
	}

	/**
	 * @param row
	 * @param gisObjectBase
	 * @param selection
	 */
	protected void update(int row, GisObjectBase gisObjectBase, boolean selection) {
		WarningManager.get_instance().cleanup();

		GisObjectBase originalGisObjectBase = GisObjectBase.create( gisObjectBase);
		if ( !on_update( gisObjectBase))
			return;

		_gisVariableTableBase.update( row, originalGisObjectBase, selection);

		update();

		if ( !WarningManager.get_instance().isEmpty()) {
			WarningDlg1 warningDlg1 = new WarningDlg1(
				_owner,
				ResourceManager.get_instance().get( "warning.dialog1.title"),
				ResourceManager.get_instance().get( "warning.dialog1.message3"),
				_parent);
			warningDlg1.do_modal();
		}
	}
}
