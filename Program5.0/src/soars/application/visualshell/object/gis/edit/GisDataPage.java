/**
 * 
 */
package soars.application.visualshell.object.gis.edit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.GisData;
import soars.application.visualshell.object.gis.edit.table.GisDataRowHeaderTable;
import soars.application.visualshell.object.gis.edit.table.GisDataTable;
import soars.common.utility.swing.panel.StandardPanel;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class GisDataPage extends StandardPanel {

	/**
	 * 
	 */
	private GisData _gisData = null;

	/**
	 * 
	 */
	public JComboBox _spotRoleComboBox = null;

	/**
	 * 
	 */
	public GisDataTable _gisDataTable = null;

	/**
	 * 
	 */
	private GisDataRowHeaderTable _gisDataRowHeaderTable = null;

	/**
	 * 
	 */
	public JTextField _textField = null;

	/**
	 * 
	 */
	protected Frame _owner = null;

	/**
	 * 
	 */
	protected Component _parent = null;

	/**
	 * @param gisData 
	 * @param owner
	 * @param parent
	 */
	public GisDataPage(GisData gisData, Frame owner, Component parent) {
		super();
		_gisData = gisData;
		_owner = owner;
		_parent = parent;
	}

	/**
	 * 
	 */
	public void on_selected() {
		_gisDataTable.requestFocus();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.panel.StandardPanel#on_create()
	 */
	protected boolean on_create() {
		if (!super.on_create())
			return false;

		initialize();

		setLayout( new BorderLayout());

		if ( !setup_north_panel())
			return false;

		if ( !setup_center_panel())
			return false;

		if ( !setup_south_panel())
			return false;

		return true;
	}

	/**
	 * 
	 */
	private void initialize() {
		_spotRoleComboBox = new JComboBox( LayerManager.get_instance().get_spot_role_names( true));
		_textField = new JTextField();
		_gisDataTable = new GisDataTable( _gisData, _textField, _owner, _parent);
		_gisDataRowHeaderTable = new GisDataRowHeaderTable( _owner, _parent);
	}

	/**
	 * @return
	 */
	private boolean setup_north_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		if ( !setup_spotRoleComboBox( panel))
			return false;

		add( panel, "North");

		return true;
	}

	/**
	 * @param parent 
	 * @return
	 */
	private boolean setup_spotRoleComboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.gis.data.dialog.dis.data.page.spot.role"));
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		panel.add( _spotRoleComboBox);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_center_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		if ( !setup_gisTable( panel))
			return false;

		add( panel);

		return true;
	}

	/**
	 * @param panel
	 * @return
	 */
	private boolean setup_gisTable(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		if ( !_gisDataTable.setup( _gisDataRowHeaderTable, true))
			return true;

		if ( !_gisDataRowHeaderTable.setup( _gisData, _gisDataTable, ( Graphics2D)getGraphics()))
			return true;

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _gisDataTable);
		scrollPane.getViewport().setOpaque( true);
		scrollPane.getViewport().setBackground( SystemColor.text);

		scrollPane.setRowHeaderView( _gisDataRowHeaderTable);
		scrollPane.getRowHeader().setOpaque( true);
		scrollPane.getRowHeader().setBackground( SystemColor.text);
		Dimension dimension = scrollPane.getRowHeader().getPreferredSize();
		scrollPane.getRowHeader().setPreferredSize(
			new Dimension( _gisDataRowHeaderTable.getColumnModel().getColumn( 0).getWidth(),
			dimension.height));
		SwingTool.set_table_left_top_corner_column( scrollPane);

		// スクロール時に２つのTableが同期するように以下のhandlerが必要
		scrollPane.getRowHeader().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JViewport viewport = ( JViewport)e.getSource();
				scrollPane.getVerticalScrollBar().setValue( viewport.getViewPosition().y);
			}
		});

		panel.add( scrollPane);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		if ( 0 < _gisDataTable.getRowCount() && 0 < _gisDataTable.getColumnCount())
			_gisDataTable.select( 0, 0);

		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_south_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		if ( !setup_textField( panel))
			return false;

		add( panel, "South");

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		Action enterAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				_gisDataTable.set_text_to_cell( e);
			}
		};
		_textField.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0), "enter");
		_textField.getActionMap().put( "enter", enterAction);

		Action escapeAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				_gisDataTable.restore_text_to_cell( e);
			}
		};
		_textField.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0), "escape");
		_textField.getActionMap().put( "escape", escapeAction);

		panel.add( _textField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}
}
