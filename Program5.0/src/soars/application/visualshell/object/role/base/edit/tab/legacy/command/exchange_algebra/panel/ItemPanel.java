/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.command.exchange_algebra.panel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.legacy.command.ExchangeAlgebraCommand;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;

/**
 * @author kurata
 *
 */
public class ItemPanel extends JPanel {

	/**
	 * 
	 */
	private List<JComponent> _components = new ArrayList<JComponent>();

	/**
	 * 
	 */
	private JLabel _label = null;

	/**
	 * 
	 */
	private String _name = "";

	/**
	 * 
	 */
	private String[] _items = null;

	/**
	 * 
	 */
	public ComboBox _typeComboBox = null;

	/**
	 * 
	 */
	public ComboBox _keywordComboBox = null;

	/**
	 * 
	 */
	public ComboBox _timeVariableComboBox = null;

	/**
	 * 
	 */
	public TimePanel _timePanel = null;

	/**
	 * 
	 */
	public TextField _textField = null;

	/**
	 * 
	 */
	public Map<String, JComponent> _componentMap = new HashMap<String, JComponent>();

	/**
	 * 
	 */
	private boolean _margin = false;

	/**
	 * 
	 */
	private RulePropertyPanelBase _rulePropertyPanelBase = null;

	/**
	 * @param margin
	 * @param name
	 * @param items
	 * @param rulePropertyPanelBase
	 */
	public ItemPanel(boolean margin, String name, String[] items, RulePropertyPanelBase rulePropertyPanelBase) {
		super();
		_margin = margin;
		_name = name;
		_items = items;
		_rulePropertyPanelBase = rulePropertyPanelBase;
	}

	/**
	 * @return
	 */
	public boolean create() {
		int pad = 5;

		setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		if ( _margin) {
			JLabel dummy = new JLabel();
			add( dummy);
			_components.add( dummy);
		}

		_label = _rulePropertyPanelBase.create_label( _name, true);
		add( _label);

		_typeComboBox = _rulePropertyPanelBase.create_comboBox( _items, false);
		_typeComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Set<String> types = _componentMap.keySet();
				for ( String type:types)
					_componentMap.get( type).setVisible( type.equals( ( String)_typeComboBox.getSelectedItem()));
				revalidate();
				repaint();
			}
		});
		add( _typeComboBox);

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0));

		for ( String item:_items) {
			if ( item.equals( ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.keyword"))) {
				_keywordComboBox = _rulePropertyPanelBase.create_comboBox( null, false);
				panel.add( _keywordComboBox);
				_componentMap.put( item, _keywordComboBox);
			} else if ( item.equals( ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.time.variable"))) {
				_timeVariableComboBox = _rulePropertyPanelBase.create_comboBox( null, false);
				panel.add( _timeVariableComboBox);
				_componentMap.put( item, _timeVariableComboBox);
			} else if ( item.equals( ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.immediate.time"))) {
				_timePanel = new TimePanel( _rulePropertyPanelBase);
				if ( !_timePanel.create())
					return false;

				panel.add( _timePanel);
				_componentMap.put( item, _timePanel);
			} else if ( item.equals( ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.immediate"))) {
				_textField = _rulePropertyPanelBase.create_textField( new TextExcluder( Constant._prohibitedCharacters12), false);
				panel.add( _textField);
				_componentMap.put( item, _textField);
			}
		}

		add( panel);

		return true;
	}

	/**
	 * @param width
	 * @return
	 */
	public int get_width(int width) {
		return Math.max( width, _label.getPreferredSize().width);
	}

	/**
	 * @param width
	 */
	public void adjust(int width) {
		_label.setPreferredSize( new Dimension( width, _label.getPreferredSize().height));
	}

	/**
	 * @param width
	 * @return
	 */
	public int get_margin_width(int width) {
		for ( JComponent component:_components)
			width = Math.max( width, component.getPreferredSize().width);
		return width;
	}

	/**
	 * @param width
	 */
	public void adjust_margin_width(int width) {
		for ( JComponent component:_components)
			component.setPreferredSize( new Dimension( width, component.getPreferredSize().height));
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		_typeComboBox.setSelectedIndex( 0);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		_label.setEnabled( enabled);
		_typeComboBox.setEnabled( enabled);
		if ( null != _keywordComboBox)
			_keywordComboBox.setEnabled( enabled);
		if ( null != _timeVariableComboBox)
			_timeVariableComboBox.setEnabled( enabled);
		if ( null != _timePanel)
			_timePanel.setEnabled( enabled);
		if ( null != _textField)
			_textField.setEnabled( enabled);
		super.setEnabled(enabled);
	}

	/**
	 * @param spotCheckBox
	 * @param spotVariableCheckBox
	 */
	public void reset(CheckBox spotCheckBox, CheckBox spotVariableCheckBox) {
		if ( null != _keywordComboBox)
			CommonTool.update( _keywordComboBox, ( !spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? RulePropertyPanelBase.get_agent_keyword_names( false) : RulePropertyPanelBase.get_spot_keyword_names( false));
		if ( null != _timeVariableComboBox)
			CommonTool.update( _timeVariableComboBox, ( !spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? RulePropertyPanelBase.get_agent_time_variable_names( false) : RulePropertyPanelBase.get_spot_time_variable_names( false));
	}

	/**
	 * 
	 */
	public void update() {
		if ( null != _keywordComboBox)
			CommonTool.update( _keywordComboBox, RulePropertyPanelBase.get_spot_keyword_names( false));
		if ( null != _timeVariableComboBox)
			CommonTool.update( _timeVariableComboBox, RulePropertyPanelBase.get_spot_time_variable_names( false));
	}

	/**
	 * @param spotObject
	 * @param number
	 * @param spotVariableCheckBox
	 */
	public void update(SpotObject spotObject, String number, CheckBox spotVariableCheckBox) {
		if ( null != _keywordComboBox)
			CommonTool.update( _keywordComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "keyword", number, false) : RulePropertyPanelBase.get_spot_keyword_names( false));
		if ( null != _timeVariableComboBox)
			CommonTool.update( _timeVariableComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "time variable", number, false) : RulePropertyPanelBase.get_spot_time_variable_names( false));
	}

	/**
	 * @param prefix
	 * @param key
	 * @return
	 */
	public boolean set(String prefix, String key) {
		if ( key.startsWith( "\"") && key.endsWith( "\"")) {
			String value = key.substring( 1, key.length() - 1);
			if ( null != _timePanel && _timePanel.set( value))
				_typeComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.immediate.time"));
			else {
				if ( null == _textField)
					return false;

				_typeComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.immediate"));
				_textField.setText( value);
			}
		} else {
			if ( CommonRuleManipulator.is_object( "keyword", prefix + key)) {
				if ( null == _keywordComboBox)
					return false;

				_typeComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.keyword"));
				_keywordComboBox.setSelectedItem( key);
			} else if ( CommonRuleManipulator.is_object( "time variable", prefix + key)) {
				if ( null == _timeVariableComboBox)
					return false;

				_typeComboBox.setSelectedItem( ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.time.variable"));
				_timeVariableComboBox.setSelectedItem( key);
			} else
				return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	public String get() {
		String item = ( String)_typeComboBox.getSelectedItem();
		if ( item.equals( ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.keyword")))
			return ( String)_keywordComboBox.getSelectedItem();
		else if ( item.equals( ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.time.variable")))
			return ( String)_timeVariableComboBox.getSelectedItem();
		else if ( item.equals( ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.immediate.time")))
			return _timePanel.get();
		else if ( item.equals( ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.immediate"))) {
			if ( !ExchangeAlgebraCommand.is_value_correct( _textField.getText())) {
				//JOptionPane.showMessageDialog( _parent,
				//	ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
				//	ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
				//	JOptionPane.ERROR_MESSAGE);
				return null;
			}

			return ( "\"" + _textField.getText() + "\"");
		} else
			return null;
	}
}
