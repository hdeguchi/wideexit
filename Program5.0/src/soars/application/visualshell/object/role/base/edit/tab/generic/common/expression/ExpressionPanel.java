/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.common.expression;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Subject;
import soars.application.visualshell.object.role.base.object.generic.element.ExpressionRule;
import soars.application.visualshell.object.role.base.object.generic.element.IObject;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.tool.expression.Expression;

/**
 * @author kurata
 *
 */
public class ExpressionPanel extends PanelRoot {

	/**
	 * 
	 */
	protected Map<String, Vector<Variable>> _variablesMap = null;

	/**
	 * 
	 */
	protected ComboBox _functionComboBox = null;

	/**
	 * 
	 */
	protected TextField _formulaTextField = null;

	/**
	 * 
	 */
	protected List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * 
	 */
	protected List<JComponent> _components = new ArrayList<JComponent>();

	/**
	 * 
	 */
	protected VariableValueTable _variableValueTable = null;

	/**
	 * 
	 */
	private Subject _object = null;

	/**
	 * 
	 */
	protected Frame _owner = null;

	/**
	 * 
	 */
	protected Component _parent = null;

	/**
	 * @param object
	 * @param property
	 * @param role 
	 * @param buddiesMap
	 * @param rulePropertyPanelBase
	 * @param owner
	 * @param parent
	 */
	public ExpressionPanel(Subject object, Property property, Role role, Map<String, List<PanelRoot>> buddiesMap, RulePropertyPanelBase rulePropertyPanelBase, Frame owner, Component parent) {
		super(property, role, buddiesMap, rulePropertyPanelBase);
		_object = object;
		_owner = owner;
		_parent = parent;
		_variablesMap = VisualShellExpressionManager.get_instance().get_variablesMap( role);
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
		panel.setLayout( new GridLayout( 1, 2));

		setup_formulaPanel( panel);

		if ( !setup_variableValueTablePanel( panel))
			return false;

		setup_handler();

		northPanel.add( panel);

		basePanel.add( northPanel, "North");

		basePanel.setBorder( BorderFactory.createLineBorder( Color.blue));

		add( basePanel);

		add( Box.createHorizontalStrut( 2));

		adjust();

		return setup_buddiesMap( _object);
	}

	/**
	 * @param parent
	 */
	private void setup_formulaPanel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		setup_nameComboBox( northPanel);

		setup_formulaTextField( northPanel);

		basePanel.add( northPanel, "North");

		panel.add( basePanel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_nameComboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		JLabel label = _rulePropertyPanelBase.create_label(
			ResourceManager.get_instance().get( "generic.gui.rule.expression.function"),
			true);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_functionComboBox = _rulePropertyPanelBase.create_comboBox(
			VisualShellExpressionManager.get_instance().get_functions(),
			_rulePropertyPanelBase._standardControlWidth, false);
		_components.add( _functionComboBox);
		panel.add( _functionComboBox);

		parent.add( panel);

		SwingTool.insert_vertical_strut( parent, 5);
	}

	/**
	 * @param parent
	 */
	private void setup_formulaTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		JLabel label = _rulePropertyPanelBase.create_label(
			ResourceManager.get_instance().get( "generic.gui.rule.expression.expression"),
			true);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_formulaTextField = _rulePropertyPanelBase.create_textField( false);
		_formulaTextField.setEditable( false);
		_components.add( _formulaTextField);
		panel.add( _formulaTextField);

		parent.add( panel);

		SwingTool.insert_vertical_strut( parent, 5);
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_variableValueTablePanel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		if ( !setup_variableValueTable( northPanel))
			return false;

		basePanel.add( northPanel, "North");

		panel.add( basePanel);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_variableValueTable(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		_variableValueTable = new VariableValueTable( _variablesMap, _object, _property, _role, _rulePropertyPanelBase, _owner, _parent);

		if ( !_variableValueTable.setup())
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _variableValueTable);
		scrollPane.setPreferredSize( new Dimension( scrollPane.getPreferredSize().width, 100));

		panel.add( scrollPane);

		parent.add( panel);

		SwingTool.insert_vertical_strut( parent, 5);

		return true;
	}

	/**
	 * 
	 */
	private void setup_handler() {
		_functionComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_formulaTextField.setText( VisualShellExpressionManager.get_instance().get_expression( ( String)_functionComboBox.getSelectedItem()));
				_variableValueTable.set( ( String)_functionComboBox.getSelectedItem());
			}
		});
//		_functionComboBox.addItemListener( new ItemListener() {
//			public void itemStateChanged(ItemEvent arg0) {
//				if ( ItemEvent.DESELECTED == arg0.getStateChange())
//					_variableValueTable.update_variable_map( ( String)arg0.getItem());
//			}
//		});
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( JComponent component:_components)
			width = Math.max( width, component.getPreferredSize().width);
		for ( JComponent component:_components)
			component.setPreferredSize( new Dimension(width, component.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#initialize()
	 */
	@Override
	public void initialize() {
		if ( 0 < _functionComboBox.getItemCount())
			_functionComboBox.setSelectedIndex( 0);
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
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#on_update_expression(soars.common.utility.tool.expression.Expression, int, soars.common.utility.tool.expression.Expression)
	 */
	@Override
	public void on_update_expression(Expression newExpression, int newVariableCount, Expression originalExpression) {
		// TODO 2014.2.14 ロール編集から数式編集が行われた際に、数式名または引数の数が変えられた時に呼ばれる
		// このメソッドが呼ばれる時、VisualShellExpressionManagerは既に更新されている
		String function = ( String)_functionComboBox.getSelectedItem();
		if ( null == function || function.equals( ""))
			return;

		if ( function.equals( originalExpression.get_function()))
			function = newExpression.get_function();

		Iterator iterator = _variablesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String key = ( String)entry.getKey();
			if ( key.equals( originalExpression.get_function())) {
				Vector<Variable> originalVariables = ( Vector<Variable>)entry.getValue();
				Vector<Variable> newVariables = new Vector<Variable>();
				Vector<String> variables = newExpression.get_variables();
				for ( int i = 0; i < variables.size(); ++i) {
					Variable variable;
					if ( originalVariables.isEmpty())
						variable = new Variable( variables.get( i), _role);
					else {
						variable = new Variable( ( originalVariables.size() > i) ? originalVariables.get( i) : originalVariables.lastElement());
						variable._name = variables.get( i);
//						if ( originalVariables.size() > i) {
//							variable = new Variable( originalVariables.get( i));
//							variable._name = variables.get( i);
//						} else
//							variable = new Variable( variables.get( i), _role);
					}

					newVariables.add( variable);
				}

				_variablesMap.put( newExpression.get_function(), newVariables);
				_variablesMap.remove( key);
				break;
			}
		}

		CommonTool.update( _functionComboBox, VisualShellExpressionManager.get_instance().get_functions());
		_functionComboBox.setSelectedItem( function);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#on_update_expression()
	 */
	@Override
	public void on_update_expression() {
		// TODO 2014.2.20 ロール編集から数式編集が行われてOKボタンが押された時に呼ばれる
		// このメソッドが呼ばれる時、VisualShellExpressionManagerは既に更新されている

		// 現在の数式情報を取得
		Map<String, Vector<Variable>> variablesMap = VisualShellExpressionManager.get_instance().get_variablesMap( _role);

		if ( variablesMap.isEmpty()) {
			_variablesMap.clear();
			_functionComboBox.removeAllItems();
			_formulaTextField.setText( "");
			while ( 0 < _variableValueTable.getRowCount())
				_variableValueTable.removeRow( 0);
			return;
		}

		// 新たに追加されている数式を追加
		Iterator iterator = variablesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String key = ( String)entry.getKey();
			if ( _variablesMap.containsKey( key))
				continue;

			_variablesMap.put( key, ( Vector<Variable>)entry.getValue());
		}

		// 削除されている数式を追加
		String[] functions = VisualShellExpressionManager.get_instance().get_functions();
		for ( String function:functions) {
			if ( variablesMap.containsKey( function))
				continue;

			_variablesMap.remove( function);
		}

		int index = _functionComboBox.getSelectedIndex();

		CommonTool.update( _functionComboBox, functions);
		if ( 0 >= _functionComboBox.getItemCount())
			return;

		if ( 0 > index)
			_functionComboBox.setSelectedIndex( 0);
		else if ( 0 <= index && _functionComboBox.getItemCount() > index)
			_functionComboBox.setSelectedIndex( index);
		else
			_functionComboBox.setSelectedIndex( _functionComboBox.getItemCount() - 1);
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
		if ( !( panelRoot instanceof ExpressionPanel))
			return;

		if ( !same_as( panelRoot))
			return;

		ExpressionPanel expressionPanel = ( ExpressionPanel)panelRoot;

		_variablesMap.clear();
		Iterator iterator = expressionPanel._variablesMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String key = ( String)entry.getKey();
			Vector<Variable> srcVariables = ( Vector<Variable>)entry.getValue();
			Vector<Variable> newVariables = new Vector<Variable>();
			for ( Variable variable:srcVariables)
				newVariables.add( new Variable( variable));
			_variablesMap.put( key, newVariables);
		}

		set( expressionPanel.get( false), false);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#same_as(soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot)
	 */
	@Override
	protected boolean same_as(PanelRoot panelRoot) {
		if ( !( panelRoot instanceof ExpressionPanel))
			return false;

		ExpressionPanel expressionPanel = ( ExpressionPanel)panelRoot;
		return _object.same_as( expressionPanel._object);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#set(soars.application.visualshell.object.role.base.object.generic.element.IObject, boolean)
	 */
	@Override
	public boolean set(IObject object, boolean check) {
		if ( !( object instanceof ExpressionRule))
			return false;

		ExpressionRule expressionRule = ( ExpressionRule)object;
		String function = VisualShellExpressionManager.get_instance().get_function( expressionRule._name);
		if ( null == function || function.equals( ""))
			return false;

		_functionComboBox.setSelectedItem( function);
		return _variableValueTable.set( expressionRule, function);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot#get(boolean)
	 */
	@Override
	public IObject get(boolean check) {
		String function = ( String)_functionComboBox.getSelectedItem();
		if ( null == function || function.equals( ""))
			return null;

		String name = VisualShellExpressionManager.get_name( function);
		if ( null == name || name.equals( ""))
			return null;

		ExpressionRule expressionRule = new ExpressionRule( name, _role);

		if ( !_variableValueTable.get( expressionRule))
			return null;

		return expressionRule;
	}
}
