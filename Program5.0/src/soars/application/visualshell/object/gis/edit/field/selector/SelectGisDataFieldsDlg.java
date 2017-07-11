/**
 * 
 */
package soars.application.visualshell.object.gis.edit.field.selector;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 *
 */
public class SelectGisDataFieldsDlg extends Dialog {

	/**
	 * 
	 */
	static public final int _minimumWidth = 800;

	/**
	 * 
	 */
	static public final int _minimumHeight = 600;

	/**
	 * 
	 */
	public String[] _availableFields = null;

	/**
	 * 
	 */
	public List<Field> _selectedFields = new ArrayList<Field>();

	/**
	 * 
	 */
	private GisDataManager _gisDataManager = null;

	/**
	 * 
	 */
	private String _prohibitedCharacters = null;

	/**
	 * 
	 */
	private SelectedFieldList _selectedFieldList = null;

	/**
	 * 
	 */
	private JTextField _anyStringTextField = null;

	/**
	 * 
	 */
	private AvailableFieldList _availableFieldList = null;

	/**
	 * 
	 */
	private List<JButton> _buttons = new ArrayList<JButton>();

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param availableFields
	 * @param selectedFields
	 * @param gisDataManager
	 * @throws HeadlessException
	 */
	public SelectGisDataFieldsDlg(Frame arg0, String arg1, boolean arg2, String[] availableFields, List<Field> selectedFields, GisDataManager gisDataManager, String prohibitedCharacters) throws HeadlessException {
		super(arg0, arg1, arg2);
		_availableFields = availableFields;
		_selectedFields = selectedFields;
		_gisDataManager = gisDataManager;
		_prohibitedCharacters = prohibitedCharacters;
	}

	/**
	 * @return
	 */
	private Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._selectGisDataFieldsDialogRectangleKey + "x",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimumWidth, _minimumHeight).x));
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._selectGisDataFieldsDialogRectangleKey + "y",
			String.valueOf( SwingTool.get_default_window_position( getOwner(), _minimumWidth, _minimumHeight).y));
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._selectGisDataFieldsDialogRectangleKey + "width",
			String.valueOf( _minimumWidth));
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._selectGisDataFieldsDialogRectangleKey + "height",
			String.valueOf( _minimumHeight));
		if ( null == value)
			return null;

		int height = Integer.parseInt( value);

		return new Rectangle( x, y, width, height);
	}

	/**
	 * 
	 */
	private void optimize_window_rectangle() {
		Rectangle rectangle = getBounds();
		if ( !GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersects( rectangle)
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( rectangle).width <= 10
			|| rectangle.y <= -getInsets().top
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( rectangle).height <= getInsets().top) {
			setSize( _minimumWidth, _minimumHeight);
			setLocationRelativeTo( getOwner());
		}
	}

	/**
	 * 
	 */
	protected void set_property_to_environment_file() {
		Rectangle rectangle = getBounds();

		Environment.get_instance().set(
			Environment._selectGisDataFieldsDialogRectangleKey + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._selectGisDataFieldsDialogRectangleKey + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._selectGisDataFieldsDialogRectangleKey + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._selectGisDataFieldsDialogRectangleKey + "height", String.valueOf( rectangle.height));
	}

	/**
	 * @return
	 */
	public boolean do_modal() {
		Rectangle rectangle = get_rectangle_from_environment_file();
		if ( null == rectangle)
			return do_modal( getOwner(), _minimumWidth, _minimumHeight);
		else
			return do_modal( rectangle);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	@Override
	protected boolean on_init_dialog() {
		if (!super.on_init_dialog())
			return false;

		setLayout( new BorderLayout());

		create_fieldLists();

		if ( !setup_north_panel())
			return false;

		if ( !setup_center_panel())
			return false;

		if ( !setup_south_panel())
			return false;

		adjust();

		return true;
	}

	/**
	 * 
	 */
	private void create_fieldLists() {
		_selectedFieldList = new SelectedFieldList( ( Frame)getOwner(), this);
		_availableFieldList = new AvailableFieldList( _selectedFieldList, ( Frame)getOwner(), this);
	}

	/**
	 * @return
	 */
	private boolean setup_north_panel() {
		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));
		insert_horizontal_glue( northPanel);
		getContentPane().add( northPanel, "North");
		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_center_panel() {
		JPanel panel = new JPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();
		panel.setLayout( gridBagLayout);

		GridBagConstraints gridBagConstraints = new GridBagConstraints();

		if ( !setup_availableFieldList( panel, gridBagLayout, gridBagConstraints))
			return false;

		setup_buttons( panel, gridBagLayout, gridBagConstraints);
		
		if ( !setup_selectedFieldList( panel, gridBagLayout, gridBagConstraints))
			return false;

		getContentPane().add( panel);

		return true;
	}

	/**
	 * @param parent
	 * @param gridBagLayout
	 * @param gridBagConstraints
	 * @return
	 */
	private boolean setup_availableFieldList(JPanel parent, GridBagLayout gridBagLayout, GridBagConstraints gridBagConstraints) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		setup_availableFieldList_north_panel( panel);

		if ( !setup_availableFieldList_center_panel( panel))
			return false;

		setup_availableFieldList_south_panel( panel);

		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagLayout.setConstraints( panel, gridBagConstraints);

		parent.add( panel);

		panel.setPreferredSize( new Dimension( 1, 1));

		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_availableFieldList_north_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		setup_label( ResourceManager.get_instance().get( "select.field.dialog.available.fields"), panel);

		parent.add( panel, "North");
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_availableFieldList_center_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		if ( !_availableFieldList.setup( false))
			return false;

		for ( String field:_availableFields)
			( ( DefaultComboBoxModel)_availableFieldList.getModel()).addElement( field);

		if ( 0 < _availableFieldList.getModel().getSize())
			_availableFieldList.setSelectedIndex( 0);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _availableFieldList);

		panel.add( scrollPane);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_availableFieldList_south_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( panel);

		setup_any_string_component( panel);

		parent.add( panel, "South");
	}

	/**
	 * @param parent
	 */
	private void setup_any_string_component(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "select.field.dialog.any.string"));
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_anyStringTextField = new JTextField( new TextExcluder( _prohibitedCharacters), "", 0);
		panel.add( _anyStringTextField);

		panel.add( Box.createHorizontalStrut( 5));

		ImageIcon imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/insert.png"));
		JButton button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "select.field.dialog.insert.field.button"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_insert_any_string();
			}
		});
		panel.add( button);

		panel.add( Box.createHorizontalStrut( 5));

		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/append.png"));
		button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "select.field.dialog.append.field.button"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_append_any_string();
			}
		});
		panel.add( button);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * 
	 */
	protected void on_insert_any_string() {
		String value = _anyStringTextField.getText();
		if ( null == value || value.equals( ""))
			return;

		int[] indices = _selectedFieldList.getSelectedIndices();
		if ( 0 == indices.length) {
			_selectedFieldList.addElement( new Field( false, value));
			_selectedFieldList.setSelectedIndex( _selectedFieldList.getModel().getSize() - 1);
		} else if ( 1 == indices.length) {
			_selectedFieldList.insertElementAt( new Field( false, value), indices[ 0]);
			_selectedFieldList.setSelectedIndex( indices[ 0]);
		} else
			return;
	}

	/**
	 * 
	 */
	protected void on_append_any_string() {
		String value = _anyStringTextField.getText();
		if ( null == value || value.equals( ""))
			return;

		_selectedFieldList.addElement( new Field( false, value));
		_selectedFieldList.setSelectedIndex( _selectedFieldList.getModel().getSize() - 1);
	}

	/**
	 * @param parent
	 * @param gridBagLayout
	 * @param gridBagConstraints
	 */
	private void setup_buttons(JPanel parent, GridBagLayout gridBagLayout, GridBagConstraints gridBagConstraints) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		setup_buttons_north_panel( panel);

		setup_buttons_center_panel( panel);

		setup_buttons_south_panel( panel);

		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.weighty = 1.0;
		gridBagLayout.setConstraints( panel, gridBagConstraints);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_buttons_north_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		setup_label( " ", panel);

		setup_insert_button( panel);

		insert_horizontal_glue( panel);

		setup_append_button( panel);

		parent.add( panel, "North");
	}

	/**
	 * @param parent
	 */
	private void setup_insert_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		ImageIcon imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/insert.png"));
		JButton button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "select.field.dialog.insert.field.button"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_insert();
			}
		});
		_buttons.add( button);
		panel.add( button);

		parent.add( panel);
	}

	/**
	 * 
	 */
	protected void on_insert() {
		int[] indices = _selectedFieldList.getSelectedIndices();
		if ( 0 == indices.length)
			_selectedFieldList.addElement( _availableFieldList.get_fields());
		else if ( 1 == indices.length)
			_selectedFieldList.insertElement( _availableFieldList.get_fields());
		else
			return;
	}

	/**
	 * @param parent
	 */
	private void setup_append_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		ImageIcon imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/append.png"));
		JButton button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "select.field.dialog.append.field.button"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_append();
			}
		});
		_buttons.add( button);
		panel.add( button);

		parent.add( panel);
	}

	/**
	 * 
	 */
	protected void on_append() {
		_selectedFieldList.addElement( _availableFieldList.get_fields());
	}

	/**
	 * @param parent
	 */
	private void setup_buttons_center_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		panel.add( new JPanel());

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_buttons_south_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		setup_remove_button( panel);

		insert_horizontal_glue( panel);

		setup_up_button( panel);

		insert_horizontal_glue( panel);

		setup_down_button( panel);

		parent.add( panel, "South");
	}

	/**
	 * @param parent
	 */
	private void setup_remove_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		ImageIcon imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/left_arrow.png"));
		JButton button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "select.field.dialog.remove.field.button"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_selectedFieldList.on_remove( e);
			}
		});
		_buttons.add( button);
		panel.add( button);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_up_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		ImageIcon imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/up_arrow.png"));
		JButton button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "select.field.dialog.up.field.button"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_selectedFieldList.on_up();
			}
		});
		_buttons.add( button);
		panel.add( button);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_down_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		ImageIcon imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/down_arrow.png"));
		JButton button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "select.field.dialog.down.field.button"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_selectedFieldList.on_down();
			}
		});
		_buttons.add( button);
		panel.add( button);

		parent.add( panel);
	}

	/**
	 * @param parent
	 * @param gridBagLayout
	 * @param gridBagConstraints
	 * @return
	 */
	private boolean setup_selectedFieldList(JPanel parent, GridBagLayout gridBagLayout, GridBagConstraints gridBagConstraints) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		setup_selectedFieldList_north_panel( panel);

		if ( !setup_selectedFieldList_center_panel( panel))
			return false;

		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagLayout.setConstraints( panel, gridBagConstraints);

		parent.add( panel);

		panel.setPreferredSize( new Dimension( 1, 1));

		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_selectedFieldList_north_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS));

		setup_label( ResourceManager.get_instance().get( "select.field.dialog.selected.fields"), panel);

		parent.add( panel, "North");
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_selectedFieldList_center_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		if ( !_selectedFieldList.setup())
			return false;

		for ( Field selectedField:_selectedFields)
			_selectedFieldList.addElement( selectedField);

		if ( 0 < _selectedFieldList.getModel().getSize())
			_selectedFieldList.setSelectedIndex( 0);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _selectedFieldList);

		panel.add( scrollPane);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param text
	 * @param parent
	 */
	private void setup_label(String text, JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		JLabel label = new JLabel( text);
		panel.add( label);

		parent.add( panel);
	}

	/**
	 * @return
	 */
	private boolean setup_south_panel() {
		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( southPanel);

		setup_ok_and_cancel_button( southPanel);

		insert_horizontal_glue( southPanel);

		getContentPane().add( southPanel, "South");

		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_ok_and_cancel_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		setup_ok_and_cancel_button(
			panel,
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			true, true);
		parent.add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( JButton button:_buttons)
			width = Math.max( width, button.getPreferredSize().width);
		for ( JButton button:_buttons)
			button.setPreferredSize( new Dimension( width, button.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	@Override
	protected void on_setup_completed() {
		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				int width = getSize().width;
				int height = getSize().height;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width,
					( _minimumHeight > height) ? _minimumHeight : height);
			}
		});

		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				set_property_to_environment_file();
			}
		});
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_ok(ActionEvent actionEvent) {
		_selectedFieldList.on_ok( _selectedFields);

		set_property_to_environment_file();

		super.on_ok(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_cancel(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_cancel(ActionEvent actionEvent) {
		set_property_to_environment_file();
		super.on_cancel(actionEvent);
	}
}
