/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.base;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.generic.GenericRule;
import soars.common.utility.tool.expression.Expression;

/**
 * @author kurata
 *
 */
public class GenericPropertyPanel extends RulePropertyPanelBase {

	/**
	 * 
	 */
	public Property _property = null;

	/**
	 * 
	 */
	public Map<String, List<PanelRoot>> _buddiesMap = null;

	/**
	 * 
	 */
	protected VerbPanel _verbPanel = null;

	/**
	 * @param kind
	 * @param property
	 * @param role
	 * @param buddiesMap
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public GenericPropertyPanel(String kind, Property property, Role role, Map<String, List<PanelRoot>> buddiesMap, int index, Frame owner, EditRoleFrame parent) {
		super(property._title, kind, property._type, property._color, role, index, owner, parent);
		_property = property;
		_buddiesMap = buddiesMap;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase#on_create()
	 */
	@Override
	protected boolean on_create() {
		if (!super.on_create())
			return false;

		setLayout( new BorderLayout());

		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		_verbPanel = create_VerbPanel();

		if ( !_verbPanel.setup( _role, _buddiesMap, northPanel))
			return false;

		basePanel.add( northPanel, "North");

		add( basePanel);

		setup_apply_button();

		initialize();

		adjust();

		return true;
	}

	/**
	 * @return
	 */
	protected VerbPanel create_VerbPanel() {
		return null;
	}

	/**
	 * 
	 */
	private void initialize() {
		_verbPanel.initialize();
	}

	/**
	 * 
	 */
	private void adjust() {
		_verbPanel.adjust();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.panel.StandardPanel#on_ok(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_ok(ActionEvent actionEvent) {
		_parent.on_apply( this, actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase#set(soars.application.visualshell.object.role.base.object.base.Rule)
	 */
	@Override
	public boolean set(Rule rule) {
		if ( null == rule || !( rule instanceof GenericRule) || !_type.equals( rule._type))
			return false;

		//( ( GenericRule)rule).print();

		if ( !_verbPanel.set( ( GenericRule)rule))
			return false;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase#get()
	 */
	@Override
	public Rule get() {
		GenericRule genericRule = GenericRule.create( _kind, _property._type, _property._system);
		if ( !_verbPanel.get( genericRule))
			return null;

		//genericRule.print();

		return genericRule;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase#update_stage_name(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_stage_name(String newName, String originalName) {
		return _verbPanel.update_stage_name( newName, originalName);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase#on_update_stage()
	 */
	@Override
	public void on_update_stage() {
		_verbPanel.on_update_stage();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase#on_update_expression(soars.common.utility.tool.expression.Expression, int, soars.common.utility.tool.expression.Expression)
	 */
	@Override
	public void on_update_expression(Expression newExpression, int newVariableCount, Expression originalExpression) {
		// TODO 2014.2.14
		_verbPanel.on_update_expression( newExpression, newVariableCount, originalExpression);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase#on_update_expression()
	 */
	@Override
	public void on_update_expression() {
		// TODO 2014.2.14
		_verbPanel.on_update_expression();
	}
}
