/*
 * Created on 2006/06/23
 */
package soars.application.visualshell.plugin.edit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.swing.panel.StandardPanel;

/**
 * @author kurata
 */
public class PluginPropertyPage extends StandardPanel {

	/**
	 * 
	 */
	public String _title = ResourceManager.get_instance().get( "edit.plugin.dialog.title");

	/**
	 * 
	 */
	private PluginTable _pluginTable = null;

	/**
	 * 
	 */
	private JScrollPane _pluginTableScrollPane = null;

	/**
	 * 
	 */
	private boolean _atFirst = true;

	/**
	 * 
	 */
	protected Frame _owner = null;

	/**
	 * 
	 */
	protected Component _parent = null;

	/**
	 * @param owner
	 * @param parent
	 */
	public PluginPropertyPage(Frame owner, Component parent) {
		super();
		_owner = owner;
		_parent = parent;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.panel.StandardPanel#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;



		setLayout( new BorderLayout());



		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		create_plugin_table();

		if ( !setup_plugin_table( centerPanel))
			return false;

		add( centerPanel);



		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				if ( _atFirst) {
					_pluginTable.setup_column_width( _pluginTableScrollPane.getWidth());
					_atFirst = false;
				} else {
					_pluginTable.adjust_column_width( _pluginTableScrollPane.getWidth());
				}
			}
		});



		return true;
	}

	/**
	 * 
	 */
	private void create_plugin_table() {
		_pluginTable = new PluginTable( _owner, _parent);
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_plugin_table(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel tablePanel = new JPanel();
		tablePanel.setLayout( new GridLayout( 1, 1));

		if ( !_pluginTable.setup())
			return false;

		_pluginTableScrollPane = new JScrollPane();
		_pluginTableScrollPane.getViewport().setView( _pluginTable);

		//_pluginTable.adjust_column_width( _pluginTableScrollPane.getPreferredSize().width);

		tablePanel.add( _pluginTableScrollPane);
		panel.add( tablePanel);
		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		_pluginTable.requestFocusInWindow();
	}

	/**
	 * @return
	 */
	public boolean on_ok() {
		_pluginTable.on_ok();
		return true;
	}
}
