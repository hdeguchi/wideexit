/*
 * Created on 2005/11/02
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.common.number;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.object.number.NumberObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.base.edit.tab.legacy.base.EditRuleSubDlg;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 */
public class EditValueDlg extends EditRuleSubDlg {

	/**
	 * 
	 */
	private String _variable = "";

	/**
	 * 
	 */
	public String _value = "";

	/**
	 * 
	 */
	private String[] _numberObjectNames = null;

	/**
	 * 
	 */
	private RadioButton[] _radioButtons1 = new RadioButton[] {
		null, null
	};

	/**
	 * 
	 */
	private JLabel _variableLabel = null;

	/**
	 * 
	 */
	private TextField _variableTextField = null;

	/**
	 * 
	 */
	private ComboBox _numberObjectComboBox = null;

	/**
	 * 
	 */
	private TextField _valueTextField = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param color
	 * @param variable
	 * @param value
	 * @param numberObjectNames
	 */
	public EditValueDlg(Frame arg0, String arg1, boolean arg2, Color color, String variable, String value, String[] numberObjectNames) {
		super(arg0, arg1, arg2, color);
		_variable = variable;
		_value = value;
		_numberObjectNames = numberObjectNames;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	@Override
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));


		insert_horizontal_glue();

		setup_variable_text_field();

		insert_horizontal_glue();

		ButtonGroup buttonGroup1 = new ButtonGroup();

		setup_number_object_comboBox( buttonGroup1);

		insert_horizontal_glue();

		setup_value_textField( buttonGroup1);

		insert_horizontal_glue();

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
	 * 
	 */
	private void setup_variable_text_field() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_variableLabel = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.common.number.object.value.dialog.label.variable"),
			false);
		_variableLabel.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _variableLabel);

		panel.add( Box.createHorizontalStrut( 5));

		_variableTextField = create_textField( true);
		_variableTextField.setText( _variable);
		_variableTextField.setEditable( false);

		panel.add( _variableTextField);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * @param buttonGroup1
	 */
	private void setup_number_object_comboBox(ButtonGroup buttonGroup1) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_radioButtons1[ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.common.number.object.value.dialog.number.object"),
			buttonGroup1, true, false);
		_radioButtons1[ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_numberObjectComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 0]);

		panel.add( Box.createHorizontalStrut( 5));

		_numberObjectComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _numberObjectComboBox);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * @param buttonGroup1
	 * 
	 */
	private void setup_value_textField(ButtonGroup buttonGroup1) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_radioButtons1[ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.common.number.object.value.dialog.value"),
			buttonGroup1, true, false);
		_radioButtons1[ 1].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_valueTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 1]);

		panel.add( Box.createHorizontalStrut( 5));

		_valueTextField = create_textField( new TextExcluder( Constant._prohibitedCharacters5), true);
		panel.add( _valueTextField);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = _variableLabel.getPreferredSize().width;
		for ( int i = 0; i < _radioButtons1.length; ++i)
			width = Math.max( width, _radioButtons1[ i].getPreferredSize().width);

		_variableLabel.setPreferredSize( new Dimension( width, _variableLabel.getPreferredSize().height));
		for ( int i = 0; i < _radioButtons1.length; ++i)
			_radioButtons1[ i].setPreferredSize( new Dimension( width, _radioButtons1[ i].getPreferredSize().height));


		_variableTextField.setPreferredSize( new Dimension( _standardControlWidth, _variableTextField.getPreferredSize().height));
		_numberObjectComboBox.setPreferredSize( new Dimension( _standardControlWidth, _numberObjectComboBox.getPreferredSize().height));
		_valueTextField.setPreferredSize( new Dimension( _standardControlWidth, _valueTextField.getPreferredSize().height));
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.EditRuleSubDlg#reset(soars.application.visualshell.common.selector.ObjectSelector)
	 */
	@Override
	protected void reset(ObjectSelector objectSelector) {
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.EditRuleSubDlg#update(soars.application.visualshell.common.selector.ObjectSelector)
	 */
	@Override
	protected void update(ObjectSelector objectSelector) {
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.EditRuleSubDlg#update(soars.application.visualshell.object.entiy.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector)
	 */
	@Override
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector) {
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	@Override
	protected void on_setup_completed() {
		CommonTool.update( _numberObjectComboBox, _numberObjectNames);
		if ( CommonTool.is_number_correct( _value)) {
			_radioButtons1[ 1].setSelected( true);
			_valueTextField.setText( _value);
			update_components( new boolean[] { false, true});
		} else {
			_numberObjectComboBox.setSelectedItem( _value);
			_radioButtons1[ 0].setSelected( true);
			update_components( new boolean[] { true, false});
		}

		super.on_setup_completed();
	}

	/**
	 * @param enables
	 */
	private void update_components(boolean[] enables) {
		_numberObjectComboBox.setEnabled( enables[ 0]);
		_valueTextField.setEnabled( enables[ 1]);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_ok(ActionEvent actionEvent) {
		String value = null;
		int kind = SwingTool.get_enabled_radioButton( _radioButtons1);
		switch ( kind) {
			case 0:
				value = ( String)_numberObjectComboBox.getSelectedItem();
				if ( null == value || value.equals( ""))
					return;

				break;
			case 1:
				value = _valueTextField.getText();
				if ( null == value || value.equals( "")
					|| value.equals( "$") || 0 < value.indexOf( '$')
					|| value.equals( "$Name") || value.equals( "$Role") || value.equals( "$Spot")
					|| 0 <= value.indexOf( Constant._experimentName))
					return;

				if ( value.startsWith( "$")
					&& ( 0 < value.indexOf( "$", 1) || 0 < value.indexOf( ")", 1)))
					return;

				String temp = NumberObject.is_correct( value, "integer");
				if ( null == temp || temp.equals( "")) {
					temp = NumberObject.is_correct( value, "real number");
					if ( null == temp || temp.equals( ""))
						return;
				}

				value = temp;

				break;
		}

		if ( null == value)
			return;

		_value = value;

		super.on_ok(actionEvent);
	}
}
