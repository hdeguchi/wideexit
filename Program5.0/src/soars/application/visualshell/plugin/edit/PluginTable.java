/*
 * Created on 2006/06/26
 */
package soars.application.visualshell.plugin.edit;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import soars.application.visualshell.common.swing.TableBase;
import soars.application.visualshell.file.common.InitialDataPlugin;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.arbitrary.FunctionalObjectPlugin;
import soars.application.visualshell.object.entity.base.object.exchange_algebra.ExchangeAlgebraPlugin;
import soars.application.visualshell.object.gis.GisDataPlugin;
import soars.application.visualshell.plugin.Plugin;
import soars.application.visualshell.plugin.PluginManager;
import soars.common.utility.swing.table.base.StandardTableHeaderRenderer;

/**
 * @author kurata
 */
public class PluginTable extends TableBase {

	/**
	 * @param owner
	 * @param parent
	 */
	public PluginTable(Frame owner, Component parent) {
		super(owner, parent);
	}

	/**
	 * @return
	 */
	public boolean setup() {
		if ( !super.setup(false))
			return false;

		getTableHeader().setReorderingAllowed( false);
		setDefaultEditor( Object.class, null);
		setSelectionMode( DefaultListSelectionModel.SINGLE_SELECTION);


		setAutoResizeMode( AUTO_RESIZE_OFF);


		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 3);

		JTableHeader tableHeader = getTableHeader();
		tableHeader.setDefaultRenderer( new StandardTableHeaderRenderer());

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue(
			ResourceManager.get_instance().get( "edit.plugin.dialog.plugin.table.header.name"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue(
			ResourceManager.get_instance().get( "edit.plugin.dialog.plugin.table.header.version"));
		defaultTableColumnModel.getColumn( 2).setHeaderValue(
			ResourceManager.get_instance().get( "edit.plugin.dialog.plugin.table.header.comment"));

		for ( int i = 0; i < defaultTableColumnModel.getColumnCount(); ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			if ( 0 == i)
				tableColumn.setCellRenderer( new PluginTableChekBoxCellRenderer());
			else
				tableColumn.setCellRenderer( new PluginTableCellRenderer());
		}

		addMouseListener( new MouseInputAdapter() {
			public void mouseReleased(MouseEvent arg0) {
				if ( 1 == arg0.getButton())
					on_mouse_left_up( arg0);
			}
		});

		Object[] objects = new Object[ 3];

		FunctionalObjectPlugin functionalObjectPlugin = new FunctionalObjectPlugin();
		objects[ 0] = new JCheckBox( functionalObjectPlugin.getName(), functionalObjectPlugin._enable);
		objects[ 1] = functionalObjectPlugin;
		objects[ 2] = functionalObjectPlugin;
		defaultTableModel.addRow( objects);

		ExchangeAlgebraPlugin exchangeAlgebraPlugin = new ExchangeAlgebraPlugin();
		objects[ 0] = new JCheckBox( exchangeAlgebraPlugin.getName(), exchangeAlgebraPlugin._enable);
		objects[ 1] = exchangeAlgebraPlugin;
		objects[ 2] = exchangeAlgebraPlugin;
		defaultTableModel.addRow( objects);

		InitialDataPlugin initialDataPlugin = new InitialDataPlugin();
		objects[ 0] = new JCheckBox( initialDataPlugin.getName(), initialDataPlugin._enable);
		objects[ 1] = initialDataPlugin;
		objects[ 2] = initialDataPlugin;
		defaultTableModel.addRow( objects);

		GisDataPlugin gisDataPlugin = new GisDataPlugin();
		objects[ 0] = new JCheckBox( gisDataPlugin.getName(), gisDataPlugin._enable);
		objects[ 1] = gisDataPlugin;
		objects[ 2] = gisDataPlugin;
		defaultTableModel.addRow( objects);

		for ( int i = 0; i < PluginManager.get_instance().size(); ++i) {
			Plugin plugin = ( Plugin)PluginManager.get_instance().get( i);
			objects[ 0] = new JCheckBox( plugin.get_title(), plugin._enable);
			objects[ 1] = plugin;
			objects[ 2] = plugin;
			defaultTableModel.addRow( objects);
		}

		if ( 0 < defaultTableModel.getRowCount())
			setRowSelectionInterval( 0, 0);

		return true;
	}

	/**
	 * @param width
	 */
	public void setup_column_width(int width) {
		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();

		int width0 = width / 4;

		TableColumn tableColumn = defaultTableColumnModel.getColumn( 0);
		tableColumn.setPreferredWidth( width0);

		tableColumn = defaultTableColumnModel.getColumn( 1);
		tableColumn.setPreferredWidth( width0);

		tableColumn = defaultTableColumnModel.getColumn( 2);
		tableColumn.setPreferredWidth( width - ( 2 * width0));
	}

	/**
	 * @param width
	 */
	public void adjust_column_width(int width) {
		if ( getPreferredSize().width >= width)
			return;

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();

		TableColumn tableColumn = defaultTableColumnModel.getColumn( 0);
		int width0 = width * tableColumn.getPreferredWidth() / getPreferredSize().width;
		tableColumn.setPreferredWidth( width0);

		tableColumn = defaultTableColumnModel.getColumn( 1);
		int width1 = width * tableColumn.getPreferredWidth() / getPreferredSize().width;
		tableColumn.setPreferredWidth( width1);

		tableColumn = defaultTableColumnModel.getColumn( 2);
		tableColumn.setPreferredWidth( width - width0 - width1);
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_left_up(MouseEvent mouseEvent) {
		int row = rowAtPoint( mouseEvent.getPoint());
		if ( 0 > row || getRowCount() <= row)
			return;

		int column = columnAtPoint( mouseEvent.getPoint());
		if ( 0 > column || getColumnCount() <= column)
			return;

		column = convertColumnIndexToModel( column);

		switch ( column) {
			case 0:
				JCheckBox checkBox = ( JCheckBox)getModel().getValueAt( row, column);
				checkBox.setSelected( !checkBox.isSelected());
				break;
		}

		repaint();
	}

	/**
	 * 
	 */
	public void on_ok() {
		FunctionalObjectPlugin functionalObjectPlugin = null;
		ExchangeAlgebraPlugin exchangeAlgebraPlugin = null;
		InitialDataPlugin initialDataPlugin = null;
		GisDataPlugin gisDataPlugin = null;
		String enable_plugins = "";
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			JCheckBox checkbox = ( JCheckBox)defaultTableModel.getValueAt( i, 0);
			Plugin plugin = ( Plugin)defaultTableModel.getValueAt( i, 1);
			plugin._enable = checkbox.isSelected();

			if ( plugin instanceof FunctionalObjectPlugin) {
				functionalObjectPlugin = ( FunctionalObjectPlugin)plugin;
				continue;
			}

			if ( plugin instanceof ExchangeAlgebraPlugin) {
				exchangeAlgebraPlugin = ( ExchangeAlgebraPlugin)plugin;
				continue;
			}

			if ( plugin instanceof InitialDataPlugin) {
				initialDataPlugin = ( InitialDataPlugin)plugin;
				continue;
			}

			if ( plugin instanceof GisDataPlugin) {
				gisDataPlugin = ( GisDataPlugin)plugin;
				continue;
			}

			if ( !plugin._enable)
				continue;

			enable_plugins += ( ( enable_plugins.equals( "") ? "" : ";") + plugin.getName());
		}

		if ( null == functionalObjectPlugin)
			return;

		if ( functionalObjectPlugin._enable == functionalObjectPlugin._originalEnable
			&& exchangeAlgebraPlugin._enable == exchangeAlgebraPlugin._originalEnable
			&& initialDataPlugin._enable == initialDataPlugin._originalEnable
			&& gisDataPlugin._enable == gisDataPlugin._originalEnable
			&& Environment.get_instance().get( Environment._enablePluginsKey, "").equals( enable_plugins))
			return;

		Environment.get_instance().set( Environment._enableFunctionalObjectKey, functionalObjectPlugin._enable ? "true" : "false");
		Environment.get_instance().set( Environment._enableExchangeAlgebraKey, exchangeAlgebraPlugin._enable ? "true" : "false");
		Environment.get_instance().set( Environment._enableInitialDataKey, initialDataPlugin._enable ? "true" : "false");
		Environment.get_instance().set( Environment._enableGisDataKey, gisDataPlugin._enable ? "true" : "false");
		Environment.get_instance().set( Environment._enablePluginsKey, enable_plugins);

		JOptionPane.showMessageDialog( _parent,
			ResourceManager.get_instance().get( "edit.plugin.dialog.plugin.table.restart.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.INFORMATION_MESSAGE);
	}
}
