/*
 * Created on 2005/11/02
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.base;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.visualshell.common.selector.IObjectSelectorHandler;
import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextLimiter;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 */
public class EditRuleSubDlg extends Dialog implements IObjectSelectorHandler {

	/**
	 * 
	 */
	protected Color _color = null;

	/**
	 * 
	 */
	protected int _standardSpotNameWidth = 120;				//150;

	/**
	 * 
	 */
	protected int _standardNumberSpinnerWidth = 80;		//100

	/**
	 * 
	 */
	protected int _standardControlWidth = 200;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param color
	 */
	public EditRuleSubDlg(Frame arg0, String arg1, boolean arg2, Color color) {
		super(arg0, arg1, arg2);
		_color = color;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	@Override
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;

		setResizable( false);

		link_to_cancel( getRootPane());

		return true;
	}

	/**
	 * @param items
	 * @param right
	 * @return
	 */
	protected ComboBox create_comboBox(Object[] items, boolean right) {
		return ComboBox.create( items, _standardControlWidth, _color, right, new CommonComboBoxRenderer( _color, right), this);
	}

	/**
	 * @param items
	 * @param width
	 * @param right
	 * @return
	 */
	protected ComboBox create_comboBox(Object[] items, int width, boolean right) {
		return ComboBox.create( items, width, _color, right, new CommonComboBoxRenderer( _color, right), this);
	}

	/**
	 * @param right
	 * @return
	 */
	protected TextField create_textField(boolean right) {
		return TextField.create( _standardControlWidth, _color, right, this);
	}

	/**
	 * @param width
	 * @param right
	 * @return
	 */
	protected TextField create_textField(int width, boolean right) {
		return TextField.create( width, _color, right, this);
	}

	/**
	 * @param textLimiter
	 * @param right
	 * @return
	 */
	protected TextField create_textField(TextLimiter textLimiter, boolean right) {
		return TextField.create( textLimiter, _standardControlWidth, _color, right, this);
	}

	/**
	 * @param textLimiter
	 * @param width
	 * @param right
	 * @return
	 */
	protected TextField create_textField(TextLimiter textLimiter, int width, boolean right) {
		return TextField.create( textLimiter, width, _color, right, this);
	}

	/**
	 * @param textLimiter
	 * @param text
	 * @param width
	 * @param right
	 * @return
	 */
	protected TextField create_textField(TextLimiter textLimiter, String text, int width, boolean right) {
		return TextField.create( textLimiter, text, width, _color, right, this);
	}

	/**
	 * @param textExcluder
	 * @param right
	 * @return
	 */
	protected TextField create_textField(TextExcluder textExcluder, boolean right) {
		return TextField.create( textExcluder, _standardControlWidth, _color, right, this);
	}

	/**
	 * @param textExcluder
	 * @param width
	 * @param right
	 * @return
	 */
	protected TextField create_textField(TextExcluder textExcluder, int width, boolean right) {
		return TextField.create( textExcluder, width, _color, right, this);
	}

	/**
	 * @param textExcluder
	 * @param text
	 * @param width
	 * @param right
	 * @return
	 */
	protected TextField create_textField(TextExcluder textExcluder, String text, int width, boolean right) {
		return TextField.create( textExcluder, text, width, _color, right, this);
	}

	/**
	 * @param name
	 * @param buttonGroup
	 * @param synchronize
	 * @param right
	 * @return
	 */
	protected RadioButton create_radioButton(String name, ButtonGroup buttonGroup, boolean synchronize, boolean right) {
		RadioButton radioButton = ( ( null == name)
			? ( new RadioButton( synchronize, right))
			: ( new RadioButton( name, synchronize, right)));

		radioButton.setForeground( _color);

		link_to_ok( radioButton._radioButton);
		link_to_ok( radioButton._label);

		link_to_cancel( radioButton._radioButton);
		link_to_cancel( radioButton._label);

		buttonGroup.add( radioButton._radioButton);

		return radioButton;
	}

	/**
	 * @param text
	 * @param synchronize
	 * @param right
	 * @return
	 */
	protected CheckBox create_checkBox(String text, boolean synchronize, boolean right) {
		CheckBox checkBox = new CheckBox( text, synchronize, right);

		checkBox.setForeground( _color);

		link_to_cancel( checkBox._checkBox);
		link_to_cancel( checkBox._label);

		return checkBox;
	}

	/**
	 * @param text
	 * @param right
	 * @return
	 */
	protected JLabel create_label(String text, boolean right) {
		JLabel label = ( right ? ( new JLabel( text, SwingConstants.RIGHT)) : ( new JLabel( text)));
		label.setForeground( _color);
		return label;
	}

	/**
	 * @param containsEmpty
	 * @param right
	 * @param panel
	 * @return
	 */
	protected ObjectSelector create_agent_selector(boolean containsEmpty, boolean right, JPanel panel) {
		return create_objectSelector( "agent", RulePropertyPanelBase.get_agent_names( containsEmpty), right, panel);
	}

	/**
	 * @param containsEmpty
	 * @param width
	 * @param right
	 * @param panel
	 * @return
	 */
	protected ObjectSelector create_agent_selector(boolean containsEmpty, int width, boolean right, JPanel panel) {
		return create_objectSelector( "agent", RulePropertyPanelBase.get_agent_names( containsEmpty), width, right, panel);
	}

	/**
	 * @param containsEmpty
	 * @param right
	 * @param panel
	 * @return
	 */
	protected ObjectSelector create_spot_selector(boolean containsEmpty, boolean right, JPanel panel) {
		return create_objectSelector( "spot", RulePropertyPanelBase.get_spot_names( containsEmpty), right, panel);
	}

	/**
	 * @param containsEmpty
	 * @param width
	 * @param right
	 * @param panel
	 * @return
	 */
	protected ObjectSelector create_spot_selector(boolean containsEmpty, int width, boolean right, JPanel panel) {
		return create_objectSelector( "spot", RulePropertyPanelBase.get_spot_names( containsEmpty), width, right, panel);
	}

	/**
	 * @param type
	 * @param names
	 * @param right
	 * @param panel
	 * @return
	 */
	protected ObjectSelector create_objectSelector(String type, String[] names, boolean right, JPanel panel) {
		return create_objectSelector( type, names, _standardSpotNameWidth, right, panel);
	}

	/**
	 * @param type
	 * @param names
	 * @param width
	 * @param right
	 * @param panel
	 * @return
	 */
	protected ObjectSelector create_objectSelector(String type, String[] names, int width, boolean right, JPanel panel) {
		ObjectSelector objectSelector = new ObjectSelector( type, names, width, _standardNumberSpinnerWidth, _color, right/*, this*/, new FlowLayout( FlowLayout.LEFT, 0, 0), this);
		panel.add( objectSelector);
		return objectSelector;
	}

	/**
	 * @param objectSelector
	 * @param enable
	 */
	protected void on_update(ObjectSelector objectSelector, boolean enable) {
		if ( !enable)
			reset( objectSelector);
		else
			update( objectSelector.get_name(), objectSelector.get_number(), objectSelector);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.selector.IObjectSelectorHandler#changed(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector)
	 */
	@Override
	public void changed(String name, String number, String fullName, ObjectSelector objectSelector) {
		update( name, number, objectSelector);
	}

	/**
	 * @param name
	 * @param number
	 * @param objectSelector
	 */
	private void update(String name, String number, ObjectSelector objectSelector) {
		if ( null == name || name.equals( ""))
			update( objectSelector);
		else {
			SpotObject spotObject = LayerManager.get_instance().get_spot( name);
			if ( null == spotObject)
				return;

			update( spotObject, number, objectSelector);
		}
	}

	/**
	 * @param fullName
	 * @param objectSelector
	 * @param checkBox
	 */
	protected boolean update(String fullName, ObjectSelector objectSelector, CheckBox checkBox) {
		if ( null == fullName || fullName.equals( ""))
			update( objectSelector);
		else {
			objectSelector.set( fullName);
			SpotObject spotObject = LayerManager.get_instance().get_spot_has_this_name( fullName);
			if ( null == spotObject)
				return false;

			update( spotObject, fullName.substring( spotObject._name.length()), objectSelector);
		}

		checkBox.setSelected( true);
		objectSelector.setEnabled( true);

		return true;
	}

	/**
	 * @param fullName
	 * @param objectSelector
	 * @param checkBox
	 */
	protected boolean update(String fullName, ObjectSelector objectSelector) {
		if ( null == fullName || fullName.equals( ""))
			update( objectSelector);
		else {
			objectSelector.set( fullName);
			SpotObject spotObject = LayerManager.get_instance().get_spot_has_this_name( fullName);
			if ( null == spotObject)
				return false;

			update( spotObject, fullName.substring( spotObject._name.length()), objectSelector);
		}

		objectSelector.setEnabled( true);

		return true;
	}

//	/**
//	 * @param fullName
//	 * @param objectSelector
//	 */
//	protected boolean update(String fullName, ObjectSelector objectSelector) {
//		if ( null == fullName || fullName.equals( ""))
//			return false;
//
//		objectSelector.set( fullName);
//		SpotObject spotObject = LayerManager.get_instance().get_spot_has_this_name( fullName);
//		if ( null == spotObject)
//			return false;
//
//		update( spotObject, fullName.substring( spotObject._name.length()), objectSelector);
//
//		objectSelector.setEnabled( true);
//
//		return true;
//	}

	/**
	 * @param objectSelector
	 */
	protected void reset(ObjectSelector objectSelector) {
	}

	/**
	 * @param objectSelector
	 */
	protected void update(ObjectSelector objectSelector) {
	}

	/**
	 * @param spotObject
	 * @param number
	 * @param objectSelector
	 */
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector) {
	}

	/**
	 * @param objectSelector
	 * @param checkBox
	 */
	protected void reset(ObjectSelector objectSelector, CheckBox checkBox) {
		checkBox.setSelected( false);
		objectSelector.setEnabled( false);
		reset( objectSelector);
	}
}
