/*
 * 2005/05/06
 */
package soars.application.visualshell.object.stage.edit;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.stage.Stage;
import soars.application.visualshell.object.stage.StageManager;
import soars.application.visualshell.observer.Observer;
import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 */
public class EditStagePropertyDlg extends Dialog implements ITextUndoRedoManagerCallBack {

	/**
	 * 
	 */
	private int _minimumWidth = -1;

	/**
	 * 
	 */
	private int _minimumHeight = -1;

	/**
	 * 
	 */
	private StageList _stageList = null;

	/**
	 * 
	 */
	private StageList[] _stageLists = null;

	/**
	 * 
	 */
	private JLabel[] _labels = {
		null, null
	};

	/**
	 * 
	 */
	private JTextField _nameTextField = null;

	/**
	 * 
	 */
	private JTextField _commentTextField = null;

	/**
	 * 
	 */
	private JCheckBox _randomCheckBox = null;

	/**
	 * 
	 */
	public Stage _stage = null;

	/**
	 * 
	 */
	private String _originalName = "";

	/**
	 * 
	 */
	private EditRoleFrame _editRoleFrame = null;

	/**
	 * 
	 */
	private List<TextUndoRedoManager> _textUndoRedoManagers = new ArrayList<TextUndoRedoManager>();

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param stageList
	 * @param stageLists
	 * @param editRoleFrame
	 */
	public EditStagePropertyDlg(Frame arg0, String arg1, boolean arg2, StageList stageList, StageList[] stageLists, EditRoleFrame editRoleFrame) {
		super(arg0, arg1, arg2);
		_stageList = stageList;
		_stageLists = stageLists;
		_editRoleFrame = editRoleFrame;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param stage
	 * @param stageList
	 * @param stageLists
	 * @param editRoleFrame
	 */
	public EditStagePropertyDlg(Frame arg0, String arg1, boolean arg2, Stage stage, StageList stageList, StageList[] stageLists, EditRoleFrame editRoleFrame) {
		super(arg0, arg1, arg2);
		_stage = stage;
		_stageList = stageList;
		_stageLists = stageLists;
		_originalName = _stage._name;
		_editRoleFrame = editRoleFrame;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		link_to_cancel( getRootPane());


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		insert_horizontal_glue();

		setup_nameTextField();

		insert_horizontal_glue();

		setup_commentTextField();

		insert_horizontal_glue();

		setup_randomCheckBox();

		insert_horizontal_glue();

		setup_ok_and_cancel_button(
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			true, true);

		insert_horizontal_glue();

		adjust();

		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		return true;
	}

	/**
	 * 
	 */
	private void setup_nameTextField() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_labels[ 0] = new JLabel( ResourceManager.get_instance().get( "edit.stage.property.dialog.label.name"));
		_labels[ 0].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _labels[ 0]);

		panel.add( Box.createHorizontalStrut( 5));

		_nameTextField = new JTextField( new TextExcluder( Constant._prohibitedCharacters1), "", 0);
		_nameTextField.setPreferredSize( new Dimension( 200, _nameTextField.getPreferredSize().height));

		if ( null != _stage)
			_nameTextField.setText( _stage._name);

		_textUndoRedoManagers .add( new TextUndoRedoManager( _nameTextField, this));

		link_to_cancel( _nameTextField);

		panel.add( _nameTextField);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_commentTextField() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_labels[ 1] = new JLabel( ResourceManager.get_instance().get( "edit.stage.property.dialog.label.comment"));
		_labels[ 1].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _labels[ 1]);

		panel.add( Box.createHorizontalStrut( 5));

		_commentTextField = new JTextField();
		_commentTextField.setPreferredSize( new Dimension( 200, _commentTextField.getPreferredSize().height));

		if ( null != _stage)
			_commentTextField.setText( _stage._comment);

		_textUndoRedoManagers .add( new TextUndoRedoManager( _commentTextField, this));

		link_to_cancel( _commentTextField);

		panel.add( _commentTextField);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_randomCheckBox() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_randomCheckBox = new JCheckBox( ResourceManager.get_instance().get( "edit.stage.property.dialog.check.box.random"), false);

		if ( null != _stage)
			_randomCheckBox.setSelected( _stage._random);

		link_to_cancel( _randomCheckBox);

		panel.add( _randomCheckBox);
		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;

		for ( int i = 0; i < _labels.length; ++i)
			width = Math.max( width, _labels[ i].getPreferredSize().width);

		for ( int i = 0; i < _labels.length; ++i)
			_labels[ i].setPreferredSize( new Dimension(
				width, _labels[ i].getPreferredSize().height));
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		_nameTextField.requestFocusInWindow();

		_minimumWidth = getPreferredSize().width;
		_minimumHeight = getPreferredSize().height;

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				if ( 0 > _minimumWidth || 0 > _minimumHeight)
					return;

				int width = getSize().width;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width, _minimumHeight);
			}
		});
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		if ( _nameTextField.getText().equals( "")
			|| _nameTextField.getText().startsWith( "$")
			|| _nameTextField.getText().equals( Constant._initializeChartStageName)
			|| _nameTextField.getText().equals( Constant._updateChartStageName)
			|| _nameTextField.getText().equals( Constant._initialDataFileStageName))
			return;

		if ( !_nameTextField.getText().equals( _originalName)) {
			if ( _stageList.contains( _nameTextField.getText()))
				return;

			for ( int i = 0; i < _stageLists.length; ++i) {
				if ( _stageLists[ i].contains( _nameTextField.getText()))
					return;
			}
		}

		if ( !_originalName.equals( "") && !_originalName.equals( _nameTextField.getText())) {
			boolean result1 = LayerManager.get_instance().update_stage_name( _nameTextField.getText(), _originalName);
			if ( null ==_editRoleFrame) {
				if ( result1) {
					StageManager.get_instance().rename( _nameTextField.getText(), _originalName);
					Observer.get_instance().on_update_stage();
					Observer.get_instance().modified();
				}
			} else {
				boolean result2 = StageManager.get_instance().rename( _nameTextField.getText(), _originalName);
				boolean result3 = _editRoleFrame.update_stage_name( _nameTextField.getText(), _originalName);
				if ( result1 || result2 || result3) {
					Observer.get_instance().on_update_stage();
					Observer.get_instance().modified();
				}
			}
//			boolean result1 = LayerManager.get_instance().update_stage_name( _nameTextField.getText(), _originalName);
//			boolean result2 = StageManager.get_instance().rename( _nameTextField.getText(), _originalName);
//			boolean result3 = ( null == _editRoleFrame) ? false : _editRoleFrame.update_stage_name( _nameTextField.getText(), _originalName);
//			if ( result1 || result2 || result3) {
//				Observer.get_instance().on_update_stage();
//				Observer.get_instance().modified();
//			}
		}

		if ( null == _stage)
			_stage = new Stage( _nameTextField.getText(), _randomCheckBox.isSelected(), _commentTextField.getText());
		else {
			_stage._name = _nameTextField.getText();
			_stage._random = _randomCheckBox.isSelected();
			_stage._comment = _commentTextField.getText();
		}

		super.on_ok(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.undo.UndoManager)
	 */
	public void on_changed(UndoManager undoManager) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.text.AbstractDocument.DefaultDocumentEvent, javax.swing.undo.UndoManager)
	 */
	public void on_changed(DefaultDocumentEvent defaultDocumentEvent,UndoManager undoManager) {
	}
}
