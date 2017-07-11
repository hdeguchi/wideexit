/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.status;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.MainFrame;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Frame;
import soars.common.utility.tool.resource.Resource;

/**
 * @author kurata
 *
 */
public class StatusFrame extends Frame {

	/**
	 * 
	 */
	static public final int _minimumWidth = 400;

	/**
	 * 
	 */
	static public final int _minimumHeight = 300;

	/**
	 * 
	 */
	private JComboBox _typeComboBox = null;

	/**
	 * 
	 */
	private JComboBox _nameComboBox = null;

	/**
	 * 
	 */
	private JComboBox _objectComboBox = null;

	/**
	 * 
	 */
	private StatusTable _statusTable = null;

	/**
	 * 
	 */
	private String[] agents = new String[] { null, null};

	/**
	 * 
	 */
	private String[] spots = new String[] { null, null};

	/**
	 * 
	 */
	private List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public StatusFrame(String arg0) throws HeadlessException {
		super(arg0);
	}

	/**
	 * @return
	 */
	private Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._statusFrameRectangleKey + "x",
			String.valueOf( SwingTool.get_default_window_position( MainFrame.get_instance(), _minimumWidth, _minimumHeight).x));
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._statusFrameRectangleKey + "y",
			String.valueOf( SwingTool.get_default_window_position( MainFrame.get_instance(), _minimumWidth, _minimumHeight).y));
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._statusFrameRectangleKey + "width",
			String.valueOf( _minimumWidth));
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._statusFrameRectangleKey + "height",
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
			setLocationRelativeTo( MainFrame.get_instance());
		}
	}

	/**
	 * 
	 */
	private void set_property_to_environment_file() {
		Rectangle rectangle = getBounds();

		Environment.get_instance().set(
			Environment._statusFrameRectangleKey + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._statusFrameRectangleKey + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._statusFrameRectangleKey + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._statusFrameRectangleKey + "height", String.valueOf( rectangle.height));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_create()
	 */
	protected boolean on_create() {
		if (!super.on_create())
			return false;

		if ( !setup_statusTable())
			return false;

		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

		Rectangle rectangle = get_rectangle_from_environment_file();
		if ( null == rectangle)
			setBounds( rectangle.x, rectangle.y, _minimumWidth, _minimumHeight);
		else
			setBounds( rectangle);

		adjust();

		Image image = Resource.load_image_from_resource( Constant._resourceDirectory + "/image/icon/icon.png", getClass());
		if ( null != image)
			setIconImage( image);

		_typeComboBox.setSelectedIndex( 0);

		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_statusTable() {
		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BorderLayout());

		JPanel notrhPanel = new JPanel();
		notrhPanel.setLayout( new BoxLayout( notrhPanel, BoxLayout.Y_AXIS));


		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.rule.dialog.type.label"), SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_typeComboBox = new JComboBox(
			new String[] {
				ResourceManager.get_instance().get( "edit.rule.dialog.type.agent"),
				ResourceManager.get_instance().get( "edit.rule.dialog.type.spot")
			});
		_typeComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_typeComboBox_actionPerformed( arg0);
			}
		});
		panel.add( _typeComboBox);

		panel.add( Box.createHorizontalStrut( 5));

		notrhPanel.add( panel);


		panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		label = new JLabel( ResourceManager.get_instance().get( "edit.rule.dialog.name.label"), SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_nameComboBox = new JComboBox();
		_nameComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_nameComboBox_actionPerformed( arg0);
			}
		});
		panel.add( _nameComboBox);

		panel.add( Box.createHorizontalStrut( 5));

		notrhPanel.add( panel);


		panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		label = new JLabel( ResourceManager.get_instance().get( "edit.rule.dialog.object.label"), SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_objectComboBox = new JComboBox(
			Environment.get_instance().is_functional_object_enable()
				? new String[] {
					ResourceManager.get_instance().get( "edit.rule.dialog.object.collection.variable"),
					ResourceManager.get_instance().get( "edit.rule.dialog.object.list.variable"),
					ResourceManager.get_instance().get( "edit.rule.dialog.object.map.variable"),
					ResourceManager.get_instance().get( "edit.rule.dialog.object.keyword"),
					ResourceManager.get_instance().get( "edit.rule.dialog.object.integer.number.variable"),
					ResourceManager.get_instance().get( "edit.rule.dialog.object.real.number.variable"),
					ResourceManager.get_instance().get( "edit.rule.dialog.object.role.variable"),
					ResourceManager.get_instance().get( "edit.rule.dialog.object.time.variable"),
					ResourceManager.get_instance().get( "edit.rule.dialog.object.spot.variable"),
					ResourceManager.get_instance().get( "edit.rule.dialog.object.file"),
					ResourceManager.get_instance().get( "edit.rule.dialog.object.class.variable"),
					ResourceManager.get_instance().get( "edit.rule.dialog.object.exchange.algebra")}
				: new String[] {
					ResourceManager.get_instance().get( "edit.rule.dialog.object.collection.variable"),
					ResourceManager.get_instance().get( "edit.rule.dialog.object.list.variable"),
					ResourceManager.get_instance().get( "edit.rule.dialog.object.map.variable"),
					ResourceManager.get_instance().get( "edit.rule.dialog.object.keyword"),
					ResourceManager.get_instance().get( "edit.rule.dialog.object.integer.number.variable"),
					ResourceManager.get_instance().get( "edit.rule.dialog.object.real.number.variable"),
					ResourceManager.get_instance().get( "edit.rule.dialog.object.role.variable"),
					ResourceManager.get_instance().get( "edit.rule.dialog.object.time.variable"),
					ResourceManager.get_instance().get( "edit.rule.dialog.object.spot.variable"),
					ResourceManager.get_instance().get( "edit.rule.dialog.object.file"),
					ResourceManager.get_instance().get( "edit.rule.dialog.object.exchange.algebra")});
		_objectComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_objectComboBox_actionPerformed( arg0);
			}
		});
		panel.add( _objectComboBox);

		panel.add( Box.createHorizontalStrut( 5));

		notrhPanel.add( panel);


		basePanel.add( notrhPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.X_AXIS));

		centerPanel.add( Box.createHorizontalStrut( 5));

		_statusTable = new StatusTable( ( Frame)getOwner(), this);
		if ( !_statusTable.setup())
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _statusTable);
		scrollPane.getViewport().setOpaque( true);
		scrollPane.getViewport().setBackground( SystemColor.text);

		centerPanel.add( scrollPane);
		centerPanel.add( Box.createHorizontalStrut( 5));
		basePanel.add( centerPanel);
		add( basePanel);

		return true;
	}

	/**
	 * @param arg0
	 */
	protected void on_typeComboBox_actionPerformed(ActionEvent arg0) {
		String type = ( String)_typeComboBox.getSelectedItem();
		if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.type.agent"))) {
			String agent = agents[ 0];
			CommonTool.update( _nameComboBox, RulePropertyPanelBase.get_agent_names( false));
			if ( null != agent)
				_nameComboBox.setSelectedItem( agent);
			else {
				if ( 0 < _nameComboBox.getItemCount())
					_nameComboBox.setSelectedIndex( 0);
			}

			if ( null != agents[ 1])
				_objectComboBox.setSelectedItem( agents[ 1]);
			else {
				if ( 0 < _objectComboBox.getItemCount())
					_objectComboBox.setSelectedIndex( 0);
			}

		} else if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.type.spot"))) {
			String spot = spots[ 0];
			CommonTool.update( _nameComboBox, RulePropertyPanelBase.get_spot_names( false));
			if ( null != spot)
				_nameComboBox.setSelectedItem( spot);
			else {
				if ( 0 < _nameComboBox.getItemCount())
					_nameComboBox.setSelectedIndex( 0);
			}

			if ( null != spots[ 1])
				_objectComboBox.setSelectedItem( spots[ 1]);
			else {
				if ( 0 < _objectComboBox.getItemCount())
					_objectComboBox.setSelectedIndex( 0);
			}

		} else
			return;

		on_actionPerformed( arg0);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_nameComboBox_actionPerformed(ActionEvent actionEvent) {
		String type = ( String)_typeComboBox.getSelectedItem();
		if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.type.agent")))
			agents[ 0] = ( String)_nameComboBox.getSelectedItem();
		else if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.type.spot")))
			spots[ 0] = ( String)_nameComboBox.getSelectedItem();
		else
			return;

		on_actionPerformed( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_objectComboBox_actionPerformed(ActionEvent actionEvent) {
		String type = ( String)_typeComboBox.getSelectedItem();
		if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.type.agent")))
			agents[ 1] = ( String)_objectComboBox.getSelectedItem();
		else if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.type.spot")))
			spots[ 1] = ( String)_objectComboBox.getSelectedItem();
		else
			return;

		on_actionPerformed( actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_actionPerformed(ActionEvent actionEvent) {
		_statusTable.update( ( String)_typeComboBox.getSelectedItem(),
			( String)_nameComboBox.getSelectedItem(), ( String)_objectComboBox.getSelectedItem());
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( JLabel label:_labels)
			width = Math.max( label.getPreferredSize().width, width);
		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));
	}

	/**
	 * 
	 */
	public void close() {
		set_property_to_environment_file();
		dispose();
	}
}
