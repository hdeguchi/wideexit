/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.common.functional_object;

import java.awt.Color;
import java.awt.Frame;
import java.util.Arrays;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextField;

/**
 * @author kurata
 *
 */
public class EditParameterDlg extends EditValueDlg {

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param color
	 * @param parameterType
	 * @param value
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
	public EditParameterDlg(Frame arg0, String arg1, boolean arg2, Color color,
		String parameterType, String value, String spot, String[] probabilityNames, String[] keywordNames,
		String[] numberObjectNames, String[] integerNumberObjectNames, String[] realNumberObjectNames,
		String[] collectionNames, String[] listNames, String[] mapNames, String[] classVariableNames, String[] fileVariableNames, Role role) {
		super(arg0, arg1, arg2, color, parameterType, value, spot, probabilityNames, keywordNames,
			numberObjectNames, integerNumberObjectNames, realNumberObjectNames,
			collectionNames, listNames, mapNames, classVariableNames, fileVariableNames, role);
		if ( parameterType.equals( "boolean")) {
			_kinds = new String[] {
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.keyword")};
		} else if ( parameterType.equals( "java.lang.String")) {
			_kinds = new String[] {
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.keyword"),
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.file"),
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.literal")};
		} else if ( parameterType.equals( "int")
			|| parameterType.equals( "byte")
			|| parameterType.equals( "short")
			|| parameterType.equals( "long")) {
			_kinds = new String[] {
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.integer.number.object")};
		} else if ( parameterType.equals( "double")
			|| parameterType.equals( "float")) {
			_kinds = new String[] {
				//ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.probability"),
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.number.object")};
		} else if ( parameterType.equals( "java.util.Collection")) {
			_kinds = new String[] {
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.collection"),
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.list"),
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.object")};
		} else if ( parameterType.equals( "java.util.HashSet")
			|| parameterType.equals( "java.util.Set")) {
			_kinds = new String[] {
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.collection"),
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.object")};
		} else if ( parameterType.equals( "java.util.LinkedList")
			|| parameterType.equals( "java.util.List")) {
			_kinds = new String[] {
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.list"),
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.object")};
		} else if ( parameterType.equals( "java.util.HashMap")
			|| parameterType.equals( "java.util.Map")) {
			_kinds = new String[] {
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.map"),
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.object")};
		} else if ( parameterType.equals( "env.Agent")) {
			_kinds = new String[] {
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.agent")};
		} else if ( parameterType.equals( "env.Spot")) {
			_kinds = new String[] {
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.spot")};
		} else {
			_kinds = new String[] {
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.class.variable"),
				ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.edit.value.dialog.object")};
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.command.functional_object.EditValueDlg#on_setup_completed()
	 */
	@Override
	protected void on_setup_completed() {
		super.on_setup_completed();

		if ( _parameterType.equals( "boolean")) {
			ComboBox comboBox = ( ComboBox)_components.get( 0);
			comboBox.setSelectedItem( _originalValue);
		} else if ( _parameterType.equals( "java.lang.String")) {
			int index = 2;
			if ( null != _keywordNames && ( 0 <= Arrays.binarySearch( _keywordNames, _originalValue))) {
				index = 0;
				ComboBox comboBox = ( ComboBox)_components.get( index);
				comboBox.setSelectedItem( _originalValue);
			} else if ( null != _fileVariableNames && ( 0 <= Arrays.binarySearch( _fileVariableNames, _originalValue))) {
				index = 1;
				ComboBox comboBox = ( ComboBox)_components.get( index);
				comboBox.setSelectedItem( _originalValue);
			} else {
				TextField textField = ( TextField)_components.get( index);
				textField.setText( ( _originalValue.startsWith( "\"") && _originalValue.endsWith( "\""))
					? _originalValue.substring( 1, _originalValue.length() - 1) : _originalValue);
			}
			_radioButtons.get( index).setSelected( true);
		} else if ( _parameterType.equals( "int")
			|| _parameterType.equals( "byte")
			|| _parameterType.equals( "short")
			|| _parameterType.equals( "long")) {
			ComboBox comboBox = ( ComboBox)_components.get( 0);
			comboBox.setSelectedItem( _originalValue);
		} else if ( _parameterType.equals( "double")
			|| _parameterType.equals( "float")) {
			int index = 0;
//			if ( null != _probabilityNames && ( 0 <= Arrays.binarySearch( _probabilityNames, _originalValue))) {
//				ComboBox comboBox = ( ComboBox)_components.get( index);
//				comboBox.setSelectedItem( _originalValue);
//			}
			if ( null != _numberObjectNames && ( 0 <= Arrays.binarySearch( _numberObjectNames, _originalValue))) {
//				index = 1;
				ComboBox comboBox = ( ComboBox)_components.get( index);
				comboBox.setSelectedItem( _originalValue);
			}
			_radioButtons.get( index).setSelected( true);
		} else if ( _parameterType.equals( "java.util.Collection")) {
			int index = 2;
			if ( null != _collectionNames && ( 0 <= Arrays.binarySearch( _collectionNames, _originalValue))) {
				index = 0;
				ComboBox comboBox = ( ComboBox)_components.get( index);
				comboBox.setSelectedItem( _originalValue);
			} else if ( null != _listNames && ( 0 <= Arrays.binarySearch( _listNames, _originalValue))) {
				index = 1;
				ComboBox comboBox = ( ComboBox)_components.get( index);
				comboBox.setSelectedItem( _originalValue);
			} else {
				TextField textField = ( TextField)_components.get( index);
				textField.setText( _originalValue);
			}
			_radioButtons.get( index).setSelected( true);
		} else if ( _parameterType.equals( "java.util.HashSet")
			|| _parameterType.equals( "java.util.Set")) {
			int index = 1;
			if ( null != _collectionNames && ( 0 <= Arrays.binarySearch( _collectionNames, _originalValue))) {
				index = 0;
				ComboBox comboBox = ( ComboBox)_components.get( index);
				comboBox.setSelectedItem( _originalValue);
			} else {
				TextField textField = ( TextField)_components.get( index);
				textField.setText( _originalValue);
			}
			_radioButtons.get( index).setSelected( true);
		} else if ( _parameterType.equals( "java.util.LinkedList")
			|| _parameterType.equals( "java.util.List")) {
			int index = 1;
			if ( null != _listNames && ( 0 <= Arrays.binarySearch( _listNames, _originalValue))) {
				index = 0;
				ComboBox comboBox = ( ComboBox)_components.get( index);
				comboBox.setSelectedItem( _originalValue);
			} else {
				TextField textField = ( TextField)_components.get( index);
				textField.setText( _originalValue);
			}
			_radioButtons.get( index).setSelected( true);
		} else if ( _parameterType.equals( "java.util.HashMap")
			|| _parameterType.equals( "java.util.Map")) {
			int index = 1;
			if ( null != _mapNames && ( 0 <= Arrays.binarySearch( _mapNames, _originalValue))) {
				index = 0;
				ComboBox comboBox = ( ComboBox)_components.get( index);
				comboBox.setSelectedItem( _originalValue);
			} else {
				TextField textField = ( TextField)_components.get( index);
				textField.setText( _originalValue);
			}
			_radioButtons.get( index).setSelected( true);
		} else if ( _parameterType.equals( "env.Agent")) {
			ObjectSelector objectSelector = ( ObjectSelector)_components.get( 0);
			objectSelector.set( _originalValue);
		} else if ( _parameterType.equals( "env.Spot")) {
			ObjectSelector objectSelector = ( ObjectSelector)_components.get( 0);
			objectSelector.set( _originalValue);
		} else {
			int index = 1;
			if ( null != _classVariableNames && ( 0 <= Arrays.binarySearch( _classVariableNames, _originalValue))) {
				index = 0;
				ComboBox comboBox = ( ComboBox)_components.get( index);
				comboBox.setSelectedItem( _originalValue);
			} else {
				TextField textField = ( TextField)_components.get( index);
				textField.setText( _originalValue);
			}
			_radioButtons.get( index).setSelected( true);
		}
	}
}
