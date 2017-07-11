/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Subject;
import soars.application.visualshell.object.role.base.object.generic.element.ConstantRule;
import soars.application.visualshell.object.role.base.object.generic.element.IObject;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class ConstantPanel extends PanelRoot {

	/**
	 * 
	 */
	private Subject _object = null;

	/**
	 * 
	 */
	private ComboBox _constantComboBox = null;

	/**
	 * 
	 */
	protected List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * @param object
	 * @param property
	 * @param role
	 * @param buddiesMap
	 * @param rulePropertyPanelBase
	 */
	public ConstantPanel(Subject object, Property property, Role role, Map<String, List<PanelRoot>> buddiesMap, RulePropertyPanelBase rulePropertyPanelBase) {
		super(property, role, buddiesMap, rulePropertyPanelBase);
		_object = object;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#setup()
	 */
	@Override
	public boolean setup() {
		if ( !super.setup())
			return false;

		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));

		add( Box.createHorizontalStrut( 2));

		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		SwingTool.insert_vertical_strut( northPanel, 5);

		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 1, 3));
		//panel.setLayout( new GridLayout( 1, ( ( entities.isEmpty() ? 0 : 1) + ( entityVariables.isEmpty() ? 0 : 1) + ( variables.isEmpty() ? 0 : 1))));

		setup_constants_panel( panel);

		//int num = ( ( entities.isEmpty() ? 0 : 1) + ( entityVariables.isEmpty() ? 0 : 1) + ( variables.isEmpty() ? 0 : 1));
		for ( int i = 0; i < 2; ++i)
			panel.add( new JPanel());

		northPanel.add( panel);

		basePanel.add( northPanel, "North");

		//basePanel.setBorder( BorderFactory.createLineBorder( this instanceof ObjectPanel ? Color.blue : getForeground(), 1));
		basePanel.setBorder( BorderFactory.createLineBorder( Color.blue));

		add( basePanel);

		add( Box.createHorizontalStrut( 2));

		//print( entities, entityVariables, variables);

		initialize();

		update_variable_panel();

		return setup_buddiesMap( _object);
	}

	/**
	 * @param parent
	 */
	private void setup_constants_panel(JPanel parent) {
		if ( _object._constants.isEmpty())
			return;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		setup_constantComboBox( northPanel);

		SwingTool.insert_vertical_strut( northPanel, 5);

		basePanel.add( northPanel, "North");

		panel.add( basePanel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_constantComboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = _rulePropertyPanelBase.create_label( _object._name, true);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_constantComboBox = _rulePropertyPanelBase.create_comboBox( _object._constants.toArray( new String[ 0]), _standardControlWidth, false);
		_constantComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update_variable_panel();
			}
		});
		panel.add( _constantComboBox);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#get_max_width(int)
	 */
	@Override
	public int get_max_width(int width) {
		for ( JLabel label:_labels)
			width = Math.max( width, label.getPreferredSize().width);
		return width;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#set_max_width(int)
	 */
	@Override
	public void set_max_width(int width) {
		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( width - 5, label.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#synchronize()
	 */
	@Override
	public void synchronize() {
		synchronize( _object);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#synchronize(soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot)
	 */
	@Override
	public void synchronize(PanelRoot panelRoot) {
		if ( !( panelRoot instanceof ConstantPanel))
				return;

		if ( !same_as( panelRoot))
			return;

		set( panelRoot.get( false), false);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#same_as(soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot)
	 */
	@Override
	protected boolean same_as(PanelRoot panelRoot) {
		if ( !( panelRoot instanceof ConstantPanel))
			return false;

		ConstantPanel constantPanel = ( ConstantPanel)panelRoot;
		return _object.same_as( constantPanel._object);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#set(soars.application.visualshell.object.role.base.object.generic.element.IObject, boolean)
	 */
	@Override
	public boolean set(IObject object, boolean check) {
		if ( !( object instanceof ConstantRule))
			return false;

		ConstantRule constantRule = ( ConstantRule)object;
		String name = _object._constantNameMap.get( constantRule._value);
		if ( null == name || name.equals( ""))
			return false;

		_constantComboBox.setSelectedItem( name);
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#get(boolean)
	 */
	@Override
	public IObject get(boolean check) {
		String name = ( String)_constantComboBox.getSelectedItem();
		if ( null == name || name.equals( ""))
			return null;

		String value = _object._nameConstantMap.get( name);
		if ( null == value || value.equals( ""))
			return null;

		return new ConstantRule( value);
	}
}
