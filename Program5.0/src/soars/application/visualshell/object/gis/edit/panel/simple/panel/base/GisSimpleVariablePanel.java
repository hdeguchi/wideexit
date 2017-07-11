/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.simple.panel.base;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase;
import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;
import soars.application.visualshell.object.gis.edit.panel.base.GisVariableTableBase;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.warning.WarningManager;

/**
 * @author kurata
 *
 */
public class GisSimpleVariablePanel extends GisPanelBase {

	/**
	 * @param kind
	 * @param gisDataManager
	 * @param gisPropertyPanelBaseMap
	 * @param gisVariableTableBase
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public GisSimpleVariablePanel(String kind, GisDataManager gisDataManager, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, GisVariableTableBase gisVariableTableBase, Color color, Frame owner, Component parent) {
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
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#on_append(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_append(ActionEvent actionEvent) {
		GisObjectBase gisObjectBase = on_append();
		if ( null == gisObjectBase)
			return;

		_gisVariableTableBase.append( gisObjectBase);

		_gisPropertyPanelBaseMap.get( "variable").update();
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

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#update(int, soars.application.visualshell.object.gis.object.base.GisObjectBase, boolean)
	 */
	@Override
	protected void update(int row, GisObjectBase gisObjectBase, boolean selection) {
		WarningManager.get_instance().cleanup();

		GisObjectBase originalObjectBase = GisObjectBase.create( gisObjectBase);
		if ( !on_update( gisObjectBase))
			return;

		_gisVariableTableBase.update( row, originalObjectBase, selection);

		_gisPropertyPanelBaseMap.get( "variable").update();

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
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#get_label_width(int)
	 */
	@Override
	public int get_label_width(int width) {
		for ( JComponent label:_labels)
			width = Math.max( width, label.getPreferredSize().width);

		return width;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#adjust(int)
	 */
	@Override
	public int adjust(int width) {
		for ( JComponent label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));

		return ( _buttons.get( 0).getPreferredSize().width + 5 + _buttons.get( 1).getPreferredSize().width);
	}
}
