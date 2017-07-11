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
import soars.application.visualshell.object.entity.base.object.number.NumberObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.object.legacy.command.ExchangeAlgebraCommand;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;

/**
 * @author kurata
 *
 */
public class ValuePanel extends JPanel {

	/**
	 * 
	 */
	private List<JComponent> _components = new ArrayList<JComponent>();

	/**
	 * 
	 */
	private ComboBox _valueTypeComboBox = null;

	/**
	 * 
	 */
	private JLabel _label = null;

	/**
	 * 
	 */
	private TextField _textField = null;

	/**
	 * 
	 */
	private ComboBox _numberObjectComboBox = null;

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
	 * @param rulePropertyPanelBase
	 */
	public ValuePanel(boolean margin, RulePropertyPanelBase rulePropertyPanelBase) {
		super();
		_margin = margin;
		_rulePropertyPanelBase = rulePropertyPanelBase;
	}

	/**
	 * @param radioButton
	 * @return
	 */
	public boolean create(RadioButton radioButton) {
		int pad = 5;

		setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		if ( null != radioButton)
			add( radioButton);
		else {
			if ( _margin) {
				JLabel dummy = new JLabel();
				add( dummy);
				_components.add( dummy);
			}
		}

		_label = _rulePropertyPanelBase.create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.value"),
			true);
		add( _label);

		_valueTypeComboBox = _rulePropertyPanelBase.create_comboBox(
			new String[] {
				ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.immediate"),
				ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.number.variable")
			}, false);
		_valueTypeComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Set<String> types = _componentMap.keySet();
				for ( String type:types)
					_componentMap.get( type).setVisible( type.equals( ( String)_valueTypeComboBox.getSelectedItem()));
				revalidate();
				repaint();
			}
		});
		add( _valueTypeComboBox);

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0));

		_textField = _rulePropertyPanelBase.create_textField( new TextExcluder( Constant._prohibitedCharacters4), true);
		panel.add( _textField);
		_componentMap.put( ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.immediate"), _textField);

		_numberObjectComboBox = _rulePropertyPanelBase.create_comboBox( null, false);
		panel.add( _numberObjectComboBox);
		_componentMap.put( ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.number.variable"), _numberObjectComboBox);

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
		_valueTypeComboBox.setSelectedIndex( 0);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		_valueTypeComboBox.setEnabled( enabled);
		_label.setEnabled( enabled);
		_textField.setEnabled( enabled);
		_numberObjectComboBox.setEnabled( enabled);
		super.setEnabled(enabled);
	}

	/**
	 * @param spotCheckBox
	 * @param spotVariableCheckBox
	 */
	public void reset(CheckBox spotCheckBox, CheckBox spotVariableCheckBox) {
		CommonTool.update( _numberObjectComboBox, ( !spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? RulePropertyPanelBase.get_agent_number_object_names( false) : RulePropertyPanelBase.get_spot_number_object_names( false));
	}

	/**
	 * 
	 */
	public void update() {
		CommonTool.update( _numberObjectComboBox, RulePropertyPanelBase.get_spot_number_object_names( false));
	}

	/**
	 * @param spotObject
	 * @param number
	 * @param spotVariableCheckBox
	 */
	public void update(SpotObject spotObject, String number, CheckBox spotVariableCheckBox) {
		CommonTool.update( _numberObjectComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "number object", number, false) : RulePropertyPanelBase.get_spot_number_object_names( false));
	}

	/**
	 * @param kind
	 * @param prefix
	 * @param numeric
	 * @return
	 */
	public boolean set(int kind, String prefix, String numeric) {
		_valueTypeComboBox.setSelectedIndex( kind);
		switch ( kind) {
			case 0:
				_textField.setText( numeric);
				break;
			case 1:
				_numberObjectComboBox.setSelectedItem( numeric);
				break;
		default:
			return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	public String get() {
		switch ( _valueTypeComboBox.getSelectedIndex()) {
			case 0:
				String value = _textField.getText();
				if ( !ExchangeAlgebraCommand.is_value_correct( value))
					return null;

				return NumberObject.is_correct( value, "real number");
			case 1:
				value = ( String)_numberObjectComboBox.getSelectedItem();
				if ( null == value || value.equals( ""))
					return null;

				return value;
		}
		return null;
	}
}
