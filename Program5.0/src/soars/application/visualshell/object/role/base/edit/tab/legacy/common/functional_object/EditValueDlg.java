/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.common.functional_object;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.edit.tab.legacy.base.EditRuleSubDlg;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class EditValueDlg extends EditRuleSubDlg {

	/**
	 * 
	 */
	protected String _spot = "";

	/**
	 * 
	 */
	protected String[] _probabilityNames = null;

	/**
	 * 
	 */
	protected String[] _keywordNames = null;

	/**
	 * 
	 */
	protected String[] _numberObjectNames = null;

	/**
	 * 
	 */
	protected String[] _integerNumberObjectNames = null;

	/**
	 * 
	 */
	protected String[] _realNumberObjectNames = null;

	/**
	 * 
	 */
	protected String[] _collectionNames = null;

	/**
	 * 
	 */
	protected String[] _listNames = null;

	/**
	 * 
	 */
	protected String[] _mapNames = null;

	/**
	 * 
	 */
	protected String[] _classVariableNames = null;

	/**
	 * 
	 */
	protected String[] _fileVariableNames = null;

	/**
	 * 
	 */
	private Role _role = null;

	/**
	 * 
	 */
	protected List<RadioButton> _radioButtons = new ArrayList<RadioButton>();

	/**
	 * 
	 */
	protected String[] _kinds = null;

	/**
	 * 
	 */
	protected List<JComponent> _components = new ArrayList<JComponent>();

	/**
	 * 
	 */
	protected String _parameterType = "";

	/**
	 * 
	 */
	protected String _originalValue = "";

	/**
	 * 
	 */
	public String _value = "";

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param color
	 * @param parameterType
	 * @param originalValue
	 * @param spot
	 * @param probabilityNames
	 * @param keywordNames
	 * @param numberObjectNames
	 * @param integerNumberObjectNames
	 * @param realNumberObjectNames
	 * @param collectionNames
	 * @param listNames
	 * @param mapNames
	 * @param classVariableNames
	 * @param fileVariableNames
	 * @param role
	 */
	public EditValueDlg(Frame arg0, String arg1, boolean arg2, Color color, String parameterType, String originalValue, String spot,
		String[] probabilityNames, String[] keywordNames, String[] numberObjectNames, String[] integerNumberObjectNames,
		String[] realNumberObjectNames, String[] collectionNames, String[] listNames, String[] mapNames,
		String[] classVariableNames, String[] fileVariableNames, Role role) {
		super(arg0, arg1, arg2, color);
		_spot = spot;
		_parameterType = parameterType;
		_originalValue = originalValue;
		_probabilityNames = probabilityNames;
		_keywordNames = keywordNames;
		_numberObjectNames = numberObjectNames;
		_integerNumberObjectNames = integerNumberObjectNames;
		_realNumberObjectNames = realNumberObjectNames;
		_collectionNames = collectionNames;
		_listNames = listNames;
		_mapNames = mapNames;
		_classVariableNames = classVariableNames;
		_fileVariableNames = fileVariableNames;
		_role = role;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.EditRuleSubDlg#on_init_dialog()
	 */
	@Override
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));


		insert_horizontal_glue();

		ButtonGroup buttonGroup = new ButtonGroup();

		if ( !setup( buttonGroup))
			return false;


		setup_ok_and_cancel_button(
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			true, true);

		insert_horizontal_glue();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);


		adjust();


		return true;
	}

	/**
	 * @param buttonGroup 
	 * @return
	 */
	private boolean setup(ButtonGroup buttonGroup) {
		for ( String kind:_kinds) {
			RadioButton radioButton = create_radioButton( kind, buttonGroup, true, false);
			_radioButtons.add( radioButton);

			JComponent component = null;
			if ( kind.equals( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.literal")))
				 component = create_textField( new TextExcluder( Constant._prohibitedCharacters3), _standardControlWidth, false);
			else if ( kind.equals( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.object")))
				 component = create_textField( new TextExcluder( Constant._prohibitedCharacters1), _standardControlWidth, false);
			else if ( kind.equals( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.agent")))
				 component = new ObjectSelector( "agent", _role instanceof AgentRole, 120, 80, null, true, null);
			else if ( kind.equals( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.spot")))
				 component = new ObjectSelector( "spot", true/*false*/, 120, 80, null, true, null);
			else
				 component = create_comboBox( null, _standardControlWidth, false);

			_components.add( component);

			setup( kind, radioButton, component);

			insert_horizontal_glue();
		}
		return true;
	}

	/**
	 * @param radioButton
	 * @param kind
	 */
	private void setup(String kind, RadioButton radioButton, final JComponent component) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		radioButton.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				component.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( radioButton);

		panel.add( component);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( RadioButton radioButton:_radioButtons)
			width = Math.max( width, radioButton.getPreferredSize().width);

		for ( RadioButton radioButton:_radioButtons)
			radioButton.setPreferredSize( new Dimension( width, radioButton.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	@Override
	protected void on_setup_completed() {
		for ( int i = 0; i < _kinds.length; ++i) {
			JComponent component = _components.get( i);

			_radioButtons.get( i).setSelected( 0 == i);
			component.setEnabled( 0 == i);

			if ( 0 == i)
				component.requestFocusInWindow();

			if ( _kinds[ i].equals( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.literal"))
				|| _kinds[ i].equals( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.object"))
				|| _kinds[ i].equals( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.agent"))
				|| _kinds[ i].equals( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.spot")))
				continue;

			ComboBox comboBox = ( ComboBox)component;
			if ( _kinds[ i].equals( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.probability")))
				CommonTool.update(comboBox, _probabilityNames);
			else if ( _kinds[ i].equals( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.keyword")))
				CommonTool.update(comboBox, _keywordNames);
			else if ( _kinds[ i].equals( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.number.object")))
				CommonTool.update(comboBox, _numberObjectNames);
			else if ( _kinds[ i].equals( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.integer.number.object")))
				CommonTool.update(comboBox, _integerNumberObjectNames);
			else if ( _kinds[ i].equals( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.real.number.object")))
				CommonTool.update(comboBox, _realNumberObjectNames);
			else if ( _kinds[ i].equals( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.collection")))
				CommonTool.update(comboBox, _collectionNames);
			else if ( _kinds[ i].equals( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.list")))
				CommonTool.update(comboBox, _listNames);
			else if ( _kinds[ i].equals( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.map")))
				CommonTool.update(comboBox, _mapNames);
			else if ( _kinds[ i].equals( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.class.variable")))
				CommonTool.update(comboBox, _classVariableNames);
			else if ( _kinds[ i].equals( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.file")))
				CommonTool.update(comboBox, _fileVariableNames);
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_ok(ActionEvent actionEvent) {
		int index = SwingTool.get_enabled_radioButton( _radioButtons);
		JComponent component = _components.get( index);
		if ( component instanceof ComboBox) {
			ComboBox comboBox = ( ComboBox)component;
			if ( 0 == comboBox.getItemCount())
				return;

			_value = ( String)comboBox.getSelectedItem();
		} else if ( component instanceof TextField) {
			TextField textField = ( TextField)component;
			if ( null == textField.getText()
				|| textField.getText().equals( "$")
				|| 0 < textField.getText().indexOf( '$')
				|| textField.getText().equals( "$Name")
				|| textField.getText().equals( "$Role")
				|| textField.getText().equals( "$Spot")
				|| 0 <= textField.getText().indexOf( Constant._experimentName))
				return;

			if ( textField.getText().startsWith( "$")
				&& ( 0 < textField.getText().indexOf( "$", 1)
				|| 0 < textField.getText().indexOf( ")", 1)))
				return;

			if ( !textField.getText().startsWith( "$")
				&& !_kinds[ index].equals( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.literal"))
				&& !RulePropertyPanelBase.is_object( _spot, textField.getText()))
				return;

			_value = _kinds[ index].equals( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.literal"))
				? ( "\"" + textField.getText() + "\"") : textField.getText();
		} else if ( component instanceof ObjectSelector) {
			ObjectSelector objectSelector = ( ObjectSelector)component;
			String value = objectSelector.get();
			if ( null == value)
				return;

			_value = value;
		} else
			return;

		super.on_ok(actionEvent);
	}
}
