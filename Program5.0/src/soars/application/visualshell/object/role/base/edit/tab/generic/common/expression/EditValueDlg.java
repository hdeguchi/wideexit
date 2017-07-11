/*
 * Created on 2005/11/02
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.common.expression;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Subject;
import soars.application.visualshell.object.role.base.object.generic.element.EntityVariableRule;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 */
public class EditValueDlg extends Dialog {

	/**
	 * 
	 */
	private Variable _variable = null;

	/**
	 * 
	 */
	private Subject _object = null;

	/**
	 * 
	 */
	protected Property _property = null;

	/**
	 * 
	 */
	protected Role _role = null;

	/**
	 * 
	 */
	protected RulePropertyPanelBase _rulePropertyPanelBase = null;

	/**
	 * 
	 */
	protected List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * 
	 */
	private TextField _variableTextField = null;

	/**
	 * 
	 */
	private ObjectPanel _objectPanel = null;

	/**
	 * 
	 */
	private int _minimumHeight;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param variable
	 * @param object
	 * @param property
	 * @param role
	 * @param rulePropertyPanelBase
	 */
	public EditValueDlg(Frame arg0, String arg1, boolean arg2, Variable variable, Subject object, Property property, Role role, RulePropertyPanelBase rulePropertyPanelBase) {
		super(arg0, arg1, arg2);
		_variable = variable;
		_object = object;
		_property = property;
		_role = role;
		_rulePropertyPanelBase = rulePropertyPanelBase;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	@Override
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		getContentPane().setLayout( new BorderLayout());


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		SwingTool.insert_vertical_strut( northPanel, 5);

		setup_variablePanel( northPanel);

		if ( !setup_objectPanel( northPanel))
			return false;

		getContentPane().add( northPanel, "North");


		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		setup_ok_and_cancel_button(
			panel,
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			false, false);
		southPanel.add( panel);

		SwingTool.insert_vertical_strut( southPanel, 5);

		getContentPane().add( southPanel, "South");


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);


		link_to_cancel( getRootPane());


		adjust();


		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_variablePanel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 2));

		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		SwingTool.insert_vertical_strut( northPanel, 5);

		setup_variableTextField( northPanel);

		SwingTool.insert_vertical_strut( northPanel, 5);

		basePanel.add( northPanel, "North");

		basePanel.setBorder( BorderFactory.createLineBorder( Color.blue));

		panel.add( basePanel);

		panel.add( Box.createHorizontalStrut( 2));

		parent.add( panel);

		SwingTool.insert_vertical_strut( parent, 5);
	}

	/**
	 * @param parent
	 */
	private void setup_variableTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = _rulePropertyPanelBase.create_label(
			ResourceManager.get_instance().get( "generic.gui.rule.expression.dialog.variable"),
			true);
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_variableTextField = _rulePropertyPanelBase.create_textField( false);
		_variableTextField.setText( _variable._name);
		_variableTextField.setEditable( false);
		panel.add( _variableTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_objectPanel(JPanel parent) {
		_objectPanel = new ObjectPanel( _object, _property, _role, _rulePropertyPanelBase);
		if ( !_objectPanel.setup())
			return false;

		if ( !_objectPanel.set( _variable._entityVariableRule, true))
			return false;

		parent.add( _objectPanel);

		SwingTool.insert_vertical_strut( parent, 5);

		return true;
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( JLabel label:_labels)
			width = Math.max( width, label.getPreferredSize().width);
		width = _objectPanel.get_max_width( width);

		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension(width, label.getPreferredSize().height));
		_objectPanel.set_max_width( width);
}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	@Override
	protected void on_setup_completed() {
		_minimumHeight = getPreferredSize().height;

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				int width = getSize().width;
				setSize( width, _minimumHeight);
			}
		});
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_ok(ActionEvent actionEvent) {
		EntityVariableRule entityVariableRule = ( EntityVariableRule)_objectPanel.get( true);
		if ( null == entityVariableRule)
			return;

		_variable._entityVariableRule.copy( entityVariableRule);

		super.on_ok(actionEvent);
	}
}
