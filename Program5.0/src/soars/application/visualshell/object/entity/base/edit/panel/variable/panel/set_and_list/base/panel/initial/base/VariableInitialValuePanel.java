/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.set_and_list.base.panel.initial.base;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class VariableInitialValuePanel extends JPanel {

	/**
	 * 
	 */
	protected Color _color = null;

	/**
	 * 
	 */
	protected EntityBase _entityBase = null;

	/**
	 * 
	 */
	protected Map<String, PropertyPanelBase> _propertyPanelBaseMap = null;

	/**
	 * 
	 */
	private JLabel _label = null;

	/**
	 * 
	 */
	protected List<JButton> _buttons = new ArrayList<JButton>();

	/**
	 * 
	 */
	protected ComboBox _typeComboBox = null;

	/**
	 * 
	 */
	protected InitialValueTableBase _initialValueTableBase = null;

	/**
	 * @param color
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 * @param typeComboBox
	 * @param initialValueTableBase
	 */
	public VariableInitialValuePanel(Color color, EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, ComboBox typeComboBox, InitialValueTableBase initialValueTableBase) {
		super();
		_color = color;
		_entityBase = entityBase;
		_propertyPanelBaseMap = propertyPanelBaseMap;
		_typeComboBox = typeComboBox;
		_initialValueTableBase = initialValueTableBase;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		_label.setEnabled( enabled);

		for ( JButton button:_buttons)
			button.setEnabled( enabled);

		super.setEnabled(enabled);
	}

	/**
	 * @param container
	 */
	protected void insert_vertical_strut(Container container) {
		SwingTool.insert_vertical_strut( container, 5);
	}

	/**
	 * 
	 */
	public void setup() {
		setLayout( new BorderLayout());


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		setup_components( northPanel);

		insert_vertical_strut( northPanel);

		add( northPanel, "North");


		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		setup_buttons( southPanel);

		add( southPanel, "South");
	}

	/**
	 * @param parent
	 */
	private void setup_components(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_label = new JLabel( ResourceManager.get_instance().get( "edit.variable.dialog.initial.value"));
		_label.setHorizontalAlignment( SwingConstants.RIGHT);
		_label.setForeground( _color);
		panel.add( _label);

		panel.add( Box.createHorizontalStrut( 5));

		create( panel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 * 
	 */
	protected void create(JPanel parent) {
	}

	/**
	 * @param parent
	 */
	private void setup_buttons(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		ImageIcon imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/append.png"));
		JButton button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "edit.object.dialog.variable.initial.value.append.button"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_append( e);
			}
		});
		_buttons.add( button);
		panel.add( button);

		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/insert.png"));
		button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "edit.object.dialog.variable.initial.value.insert.button"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_insert( e);
			}
		});
		_buttons.add( button);
		panel.add( button);

		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/update.png"));
		button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "edit.object.dialog.variable.initial.value.update.button"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_update( e);
			}
		});
		_buttons.add( button);
		panel.add( button);

		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/remove.png"));
		button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "edit.object.dialog.variable.initial.value.remove.button"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_remove( e);
			}
		});
		_buttons.add( button);
		panel.add( button);

		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/up_arrow.png"));
		button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "edit.object.dialog.variable.initial.value.up.button"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_up( e);
			}
		});
		_buttons.add( button);
		panel.add( button);

		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/down_arrow.png"));
		button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "edit.object.dialog.variable.initial.value.down.button"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_down( e);
			}
		});
		_buttons.add( button);
		panel.add( button);

		parent.add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_append(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	protected void on_insert(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	protected void on_update(ActionEvent actionEvent) {
	}

	/**
	 * @param actionEvent
	 */
	protected void on_remove(ActionEvent actionEvent) {
		_initialValueTableBase.on_remove();
	}

	/**
	 * @param actionEvent
	 */
	protected void on_up(ActionEvent actionEvent) {
		_initialValueTableBase.up();
	}

	/**
	 * @param actionEvent
	 */
	protected void on_down(ActionEvent actionEvent) {
		_initialValueTableBase.down();
	}

	/**
	 * @param width
	 * @return
	 */
	public int get_label_width(int width) {
		return Math.max( width, _label.getPreferredSize().width);
	}

	/**
	 * @param width
	 */
	public void adjust(int width) {
		_label.setPreferredSize( new Dimension( width, _label.getPreferredSize().height));
	}

	/**
	 * @param item
	 */
	public void update(String item) {
	}

	/**
	 * @param value
	 */
	public void set(String value) {
	}
}
