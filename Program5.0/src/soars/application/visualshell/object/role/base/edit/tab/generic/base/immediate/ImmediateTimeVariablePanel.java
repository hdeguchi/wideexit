/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.base.immediate;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;
import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class ImmediateTimeVariablePanel extends JPanel implements ITextUndoRedoManagerCallBack {

	/**
	 * 
	 */
	protected int _standardControlWidth = 240;

	/**
	 * 
	 */
	protected RulePropertyPanelBase _rulePropertyPanelBase = null;

	/**
	 * 
	 */
	protected List<TextUndoRedoManager> _textUndoRedoManagers = null;

	/**
	 * 
	 */
	private TextField _timeTextField = null;

	/**
	 * 
	 */
	private List<ComboBox> _timeComboBoxes = new ArrayList<ComboBox>();

	/**
	 * @param rulePropertyPanelBase
	 * @param textUndoRedoManagers
	 * @param standardControlWidth
	 */
	public ImmediateTimeVariablePanel(RulePropertyPanelBase rulePropertyPanelBase, int standardControlWidth, List<TextUndoRedoManager> textUndoRedoManagers) {
		super();
		_rulePropertyPanelBase = rulePropertyPanelBase;
		_standardControlWidth = standardControlWidth;
		_textUndoRedoManagers = textUndoRedoManagers;
	}

	/**
	 * @return
	 */
	public boolean setup() {
		setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		if ( !create_day_panel( northPanel))
			return false;

		SwingTool.insert_vertical_strut( northPanel, 5);

		if ( !create_hour_and_minute_panel( northPanel))
			return false;

		add( northPanel, "North");

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean create_day_panel(JPanel parent) {
		_timeTextField = _rulePropertyPanelBase.create_textField( new TextExcluder( Constant._prohibitedCharacters14), _standardControlWidth, false);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _timeTextField, this));
		parent.add( _timeTextField);
		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean create_hour_and_minute_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 1, 2));

		if ( !setup_hour_panel( panel))
			return false;

		if ( !setup_minute_panel( panel))
			return false;

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_hour_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		ComboBox comboBox = new ComboBox( CommonTool.get_hours(), _rulePropertyPanelBase._color, true, new CommonComboBoxRenderer( _rulePropertyPanelBase._color, true));
		_timeComboBoxes.add( comboBox);
		northPanel.add( comboBox);

		panel.add( northPanel, "North");

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_minute_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		ComboBox comboBox = new ComboBox( CommonTool.get_minutes00(), _rulePropertyPanelBase._color, true, new CommonComboBoxRenderer( _rulePropertyPanelBase._color, true));
		_timeComboBoxes.add( comboBox);
		northPanel.add( comboBox);

		panel.add( northPanel, "North");

		parent.add( panel);

		return true;
	}

	/**
	 * @param value
	 */
	public void set(String value) {
		if ( is_time( value)) {
			String[] elements = get_time_elements( value);
			if ( null == elements)
				return;

			_timeTextField.setText( elements[ 0]);
			_timeComboBoxes.get( 0).setSelectedItem( elements[ 1]);
			_timeComboBoxes.get( 1).setSelectedItem( elements[ 2]);
		} else {
			if ( value.startsWith( "$")) {
				_timeTextField.setText( value);
				_timeComboBoxes.get( 0).setSelectedIndex( 0);
				_timeComboBoxes.get( 1).setSelectedIndex( 0);
			}
		}
	}

	/**
	 * @param value
	 * @return
	 */
	private boolean is_time(String value) {
		return ( value.matches( "[0-9]:[0-9][0-9]")
			|| value.matches( "[1-2][0-9]:[0-9][0-9]")
			|| value.matches( "0/[0-9]:[0-9][0-9]")
			|| value.matches( "0/[1-2][0-9]:[0-9][0-9]")
			|| value.matches( "[1-9][0-9]*/[0-9]:[0-9][0-9]")
			|| value.matches( "[1-9][0-9]*/[1-2][0-9]:[0-9][0-9]"));
	}

	/**
	 * @param value
	 * @return
	 */
	private String[] get_time_elements(String value) {
		String elements[] = new String[ 3];

		String[] words = value.split( "/");
		switch ( words.length) {
			case 1:
				if ( words[ 0].equals( ""))
					return null;

				elements[ 0] = "";
				words = words[ 0].split( ":");
				break;
			case 2:
				if ( words[ 0].equals( "") || words[ 1].equals( ""))
					return null;

				elements[ 0] = words[ 0];
				words = words[ 1].split( ":");
				break;
			default:
				return null;
		}

		if ( 2 != words.length)
			return null;

		if ( words[ 0].equals( "") || words[ 1].equals( ""))
			return null;

		elements[ 1] = words[ 0];
		elements[ 2] = words[ 1];

		return elements;
	}

	/**
	 * @return
	 */
	public String get() {
		String value = _timeTextField.getText();
		if ( null == value)
			return null;

		if ( !value.equals( "")) {
			value = Constant.is_correct_time_variable_initial_value( value);
			if ( null == value)
				return null;

			_timeTextField.setText( value);

			if ( value.startsWith( "$"))
				return value;

			value += "/";
		}

		value += ( String)_timeComboBoxes.get( 0).getSelectedItem();
		value += ":";
		value += ( String)_timeComboBoxes.get( 1).getSelectedItem();
		return value;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.undo.UndoManager)
	 */
	@Override
	public void on_changed(UndoManager undoManager) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.text.AbstractDocument.DefaultDocumentEvent, javax.swing.undo.UndoManager)
	 */
	@Override
	public void on_changed(DefaultDocumentEvent defaultDocumentEvent, UndoManager undoManager) {
	}
}
