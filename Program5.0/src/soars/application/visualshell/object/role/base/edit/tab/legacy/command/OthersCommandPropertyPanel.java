/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.command;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.edit.table.data.CommonRuleData;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.stage.StageManager;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class OthersCommandPropertyPanel extends RulePropertyPanelBase {

	/**
	 * 
	 */
	protected RadioButton[] _radioButtons1 = new RadioButton[] {
		null, null, null, null
	};

	/**
	 * 
	 */
	protected ComboBox _nextStageComboBox = null;

	/**
	 * 
	 */
	protected TextField _othersTextField = null;

	/**
	 * @param title
	 * @param kind
	 * @param type
	 * @param role
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public OthersCommandPropertyPanel(String title, String kind, String type, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, role, index, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase#update_stage_name(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_stage_name(String newName, String originalName) {
		// TODO 2012.9.20
		String name = ( String)_nextStageComboBox.getSelectedItem();
		if ( null != name && name.equals( originalName))
			name = newName;

		CommonTool.update( _nextStageComboBox, StageManager.get_instance().get_names( false));

		_nextStageComboBox.setSelectedItem( name);

		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase#on_update_stage()
	 */
	@Override
	public void on_update_stage() {
		// TODO 2012.9.20
		String name = ( String)_nextStageComboBox.getSelectedItem();
		CommonTool.update( _nextStageComboBox, StageManager.get_instance().get_names( false));
		if ( null != name)
			_nextStageComboBox.setSelectedItem( name);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.panel.StandardPanel#on_create()
	 */
	@Override
	protected boolean on_create() {
		if ( !super.on_create())
			return false;


		setLayout( new BorderLayout());


		JPanel basicPanel = new JPanel();
		basicPanel.setLayout( new BorderLayout());


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		ButtonGroup buttonGroup1 = new ButtonGroup();

		setup_next_stage( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_terminate( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_trace( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		setup_others( buttonGroup1, northPanel);

		insert_vertical_strut( northPanel);

		basicPanel.add( northPanel, "North");


		add( basicPanel);


		setup_apply_button();


		adjust();


		return true;
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_next_stage(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 0] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.next.stage"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.next.stage"), "command"),
			buttonGroup1, true, false);
		_radioButtons1[ 0].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_nextStageComboBox.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 0]);

		_nextStageComboBox = create_comboBox( StageManager.get_instance().get_names( false), _standardControlWidth,
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.next.stage"), "command"),
			false);
		panel.add( _nextStageComboBox);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_terminate(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 1] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.terminate"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.terminate"), "command"),
			buttonGroup1, true, false);
		panel.add( _radioButtons1[ 1]);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_trace(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 2] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.trace"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.trace"), "command"),
			buttonGroup1, true, false);
		panel.add( _radioButtons1[ 2]);

		parent.add( panel);
	}

	/**
	 * @param buttonGroup1
	 * @param parent
	 */
	private void setup_others(ButtonGroup buttonGroup1, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_radioButtons1[ 3] = create_radioButton(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.others"),
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.others"), "command"),
			buttonGroup1, true, false);
		_radioButtons1[ 3].addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				_othersTextField.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
			}
		});
		panel.add( _radioButtons1[ 3]);

		_othersTextField = create_textField( 400,
			CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.others"), "command"),
			false);
		panel.add( _othersTextField);

		parent.add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( int i = 0; i < _radioButtons1.length; ++i)
			width = Math.max( width, _radioButtons1[ i].getPreferredSize().width);

		Dimension dimension = new Dimension( width,
			_radioButtons1[ 0].getPreferredSize().height);
		for ( int i = 0; i < _radioButtons1.length; ++i)
			_radioButtons1[ i].setPreferredSize( dimension);
	}

	/**
	 * 
	 */
	private void initialize() {
		_radioButtons1[ 0].setSelected( true);
		update_components( new boolean[] {
			true, false, false, false
		});
	}

	/**
	 * @param enables
	 */
	private void update_components(boolean[] enables) {
		_nextStageComboBox.setEnabled( enables[ 0]);
		_othersTextField.setEnabled( enables[ 3]);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#on_setup_completed()
	 */
	@Override
	public void on_setup_completed() {
		super.on_setup_completed();
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.panel.StandardPanel#on_ok(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_ok(ActionEvent actionEvent) {
		_parent.on_apply( this, actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#set(soars.application.visualshell.object.role.base.rule.base.Rule)
	 */
	@Override
	public boolean set(Rule rule) {
		initialize();

		if ( null == rule)
			return false;

		if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.command.next.stage")))
			return set_next_stage( rule._value);
		else if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.command.terminate")))
			return set_terminate( rule._value);
		else if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.command.trace")))
			return set_trace( rule._value);
		else if ( rule._type.equals( ResourceManager.get_instance().get( "rule.type.command.others")))
			return set_others( rule._value);
		// TODO 2012.9.20
		else
			set_others( rule._value);

		return false;
	}

	/**
	 * @param value
	 * @return
	 */
	private boolean set_next_stage(String value) {
		_radioButtons1[ 0].setSelected( true);
		_nextStageComboBox.setSelectedItem( value);
		return true;
	}

	/**
	 * @param value
	 * @return
	 */
	private boolean set_terminate(String value) {
		_radioButtons1[ 1].setSelected( true);
		return true;
	}

	/**
	 * @param value
	 * @return
	 */
	private boolean set_trace(String value) {
		_radioButtons1[ 2].setSelected( true);
		return true;
	}

	/**
	 * @param value
	 * @return
	 */
	private boolean set_others(String value) {
		_radioButtons1[ 3].setSelected( true);
		_othersTextField.setText( value);
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get()
	 */
	@Override
	public Rule get() {
		String type = null;
		String value = null;
		int kind = SwingTool.get_enabled_radioButton( _radioButtons1);

		switch ( kind) {
			case 0:
				type = ResourceManager.get_instance().get( "rule.type.command.next.stage");
				value = get1( _nextStageComboBox);
				break;
			case 1:
				type = ResourceManager.get_instance().get( "rule.type.command.terminate");
				value = "";
				break;
			case 2:
				type = ResourceManager.get_instance().get( "rule.type.command.trace");
				value = "";
				break;
			case 3:
				type = ResourceManager.get_instance().get( "rule.type.command.others");
				value = get1( _othersTextField, true);
				break;
		}

		if ( null == type || null == value)
			return null;

		return Rule.create( _kind, type, value);
	}
}
