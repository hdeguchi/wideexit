/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.command;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.legacy.common.functional_object.EditReturnValueDlg;
import soars.application.visualshell.object.role.base.edit.tab.legacy.common.functional_object.FunctionalObjectPropertyPanelBase;

/**
 * @author kurata
 *
 */
public class FunctionalObjectCommandPropertyPanel extends FunctionalObjectPropertyPanelBase {

	/**
	 * @param title
	 * @param kind
	 * @param type
	 * @param color
	 * @param role
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public FunctionalObjectCommandPropertyPanel(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.functional_object.FunctionalObjectPropertyPanelBase#get_return_value()
	 */
	@Override
	protected String get_return_value() {
		return _returnValueTextField.getText();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.functional_object.FunctionalObjectPropertyPanelBase#set_return_value(java.lang.String)
	 */
	@Override
	protected void set_return_value(String text) {
		_returnValueTextField.setText( text);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.functional_object.FunctionalObjectPropertyPanelBase#set_enable_return_value_button(boolean)
	 */
	@Override
	protected void set_enable_return_value_button(boolean enable) {
		_returnValueButton.setEnabled( enable);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.common.functional_object.FunctionalObjectPropertyPanelBase#setup_return_value_textField(javax.swing.JPanel)
	 */
	@Override
	protected void setup_returnValueTextField(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = create_label( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.label.return.value"), false);
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( label);
		_labels.get( 0).add( label);

		_returnValueTextField = create_textField( _standardControlWidth, false);
		_returnValueTextField.setEditable( false);
		panel.add( _returnValueTextField);


		_returnValueButton = create_button(
			ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.button.return.value"));
		_returnValueButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_edit_return_value( arg0);
			}
		});
		_returnValueButton.setEnabled( false);
		panel.add( _returnValueButton);


		parent.add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_edit_return_value(ActionEvent actionEvent) {
		EditReturnValueDlg editReturnValueDlg = new EditReturnValueDlg(
			_owner,
			ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.return.value.dialog.title"),
			true,
			_color,
			_returnType,
			_returnValueTextField.getText(),
			get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox),
			_probabilityNames,
			_keywordNames,
			_numberObjectNames,
			_integerNumberObjectNames,
			_realNumberObjectNames,
			_collectionNames,
			_listNames,
			_mapNames,
			_classVariableNames,
			_fileVariableNames,
			_role);

		if ( !editReturnValueDlg.do_modal( _parent))
			return;

		_returnValueTextField.setText( editReturnValueDlg._value);
	}
}
