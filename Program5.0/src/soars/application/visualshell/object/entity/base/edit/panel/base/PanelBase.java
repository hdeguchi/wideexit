/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.base;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.object.base.ObjectBase;
import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class PanelBase extends JPanel implements ITextUndoRedoManagerCallBack {

	/**
	 * 
	 */
	protected String _kind = "";

	/**
	 * 
	 */
	protected EntityBase _entityBase = null;

	/**
	 * 
	 */
	protected Map<String, PropertyPanelBase> _propertyPanelBaseMap = null;

	/**
	 * 
	 */
	protected VariableTableBase _variableTableBase = null;

	/**
	 * 
	 */
	protected List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * 
	 */
	protected List<JComponent> _components = new ArrayList<JComponent>();

	/**
	 * 
	 */
	protected List<TextUndoRedoManager> _textUndoRedoManagers = new ArrayList<TextUndoRedoManager>();

	/**
	 * 
	 */
	protected Frame _owner = null;

	/**
	 * 
	 */
	protected Component _parent = null;

	/**
	 * 
	 */
	protected Color _color = null;

	/**
	 * 
	 */
	protected List<JButton> _buttons = new ArrayList<JButton>();

	/**
	 * 
	 */
	protected JButton _clearButton = null;

	/**
	 * 
	 */
	protected TextField _nameTextField = null;

	/**
	 * 
	 */
	protected TextField _commentTextField = null;

	/**
	 * 
	 */
	protected boolean _reject = false;

	/**
	 * @param kind
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 * @param variableTableBase
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public PanelBase(String kind, EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, VariableTableBase variableTableBase, Color color, Frame owner, Component parent) {
		super();
		_kind = kind;
		_entityBase = entityBase;
		_propertyPanelBaseMap = propertyPanelBaseMap;
		_variableTableBase = variableTableBase;
		_color = color;
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @return
	 */
	public ObjectBase create() {
		return ObjectBase.create( _kind);
	}

	/**
	 * @param objectBase
	 * @return
	 */
	public boolean contains(ObjectBase objectBase) {
		return false;
	}

	/**
	 * @param type
	 * @param name
	 * @param newName
	 * @return
	 */
	public boolean update_object_name(String type, String name, String newName) {
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		for ( JLabel label:_labels)
			label.setEnabled( enabled);

		for ( JComponent component:_components)
			component.setEnabled( enabled);

		for ( JButton button:_buttons)
			button.setEnabled( enabled);

		_clearButton.setEnabled( enabled);

		super.setEnabled(enabled);
	}

	/**
	 * @param container
	 */
	protected void insert_vertical_strut(Container container) {
		SwingTool.insert_vertical_strut( container, 5);
	}

	/**
	 * @return
	 */
	public boolean setup() {
		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	protected boolean setup_center_panel(JPanel parent) {
		return true;
	}

	/**
	 * @param parent
	 */
	protected void setup_buttons(JPanel parent) {
		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_vertical_strut( northPanel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		ImageIcon imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/file/new.png"));
		JButton button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "edit.object.dialog.file.append.button"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_append( e);
			}
		});
		_buttons.add( button);
		buttonPanel.add( button);

		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/file/update.png"));
		button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "edit.object.dialog.file.update.button"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_update( e);
			}
		});
		_buttons.add( button);
		buttonPanel.add( button);

		northPanel.add( buttonPanel);

		parent.add( northPanel, "North");


		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		buttonPanel = new JPanel();
		buttonPanel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/file/clear.png"));
		_clearButton = new JButton( imageIcon);
		_clearButton.setToolTipText( ResourceManager.get_instance().get( "edit.object.dialog.file.clear.button"));
		_clearButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_clear( e);
			}
		});
		buttonPanel.add( _clearButton);

		southPanel.add( buttonPanel);

		insert_vertical_strut( southPanel);

		parent.add( southPanel, "South");
	}

	/**
	 * @param actionEvent
	 */
	protected void on_append(ActionEvent actionEvent) {
	}

	/**
	 * @return
	 */
	protected ObjectBase on_append() {
		ObjectBase objectBase = create();
		return on_update( objectBase) ? objectBase : null;
	}

	/**
	 * @param actionEvent
	 */
	protected void on_update(ActionEvent actionEvent) {
	}

	/**
	 * @param row
	 * @param objectBase
	 * @param selection
	 */
	protected void update(int row, ObjectBase objectBase, boolean selection) {
	}

	/**
	 * @param objectBase
	 * @return
	 */
	protected boolean on_update(ObjectBase objectBase) {
		if ( !can_get_data( objectBase))
			return false;

		get_data( objectBase);

		return true;
	}

	/**
	 * @param objectBase
	 */
	public void update(ObjectBase objectBase) {
	}

	/**
	 * 
	 */
	public void update() {
	}

	/**
	 * @param actionEvent
	 */
	protected void on_clear(ActionEvent actionEvent) {
		clear();
	}

	/**
	 * 
	 */
	public void clear() {
		_nameTextField.setText( "");
		_commentTextField.setText( "");
	}

	/**
	 * @param width
	 * @return
	 */
	public int get_label_width(int width) {
		return 0;
	}

	/**
	 * @param width 
	 * @return
	 */
	public int adjust(int width) {
		return 0;
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
	}

	/**
	 * @return
	 */
	protected boolean is_empty() {
		return ( _nameTextField.getText().equals( "")
			&& _commentTextField.getText().equals( ""));
	}

	/**
	 * @return
	 */
	protected ObjectBase create_and_get() {
		return null;
	}

	/**
	 * @param objectBase
	 */
	protected boolean can_get_data(ObjectBase objectBase) {
		return false;
	}

	/**
	 * @param objectBase
	 */
	protected void get_data(ObjectBase objectBase) {
	}

	/**
	 * @param fromTree
	 * @return
	 */
	public boolean confirm(boolean fromTree) {
		// JTreeの仕様によりfalseを返すともう1度呼ばれるので、２回目に実行しないようにする(苦肉の策)
		if ( fromTree && _reject) {
			_reject = false;
			return false;
		}

		if ( !isVisible())
			return true;

		int[] rows = _variableTableBase.getSelectedRows();
		ObjectBase object = create();
		ObjectBase objectBase = confirm(
			( null == rows || 1 != rows.length) ? -1 : rows[ 0], 
			( null == rows || 1 != rows.length) ? null : ( ObjectBase)_variableTableBase.getValueAt( rows[ 0], 0), 
			object);
		if ( null == objectBase) {
			// 追加または更新に失敗
			if ( fromTree)
				_reject = true;
			return false;
		} else {
			if ( objectBase == object) {
				// 無視の場合
				if ( null == rows || 1 != rows.length)
					clear();

				return true;
			} else {
				// 追加または更新されたオブジェクトを選択させる
				_variableTableBase.select( objectBase);
				if ( fromTree)
					_reject = true;
				return false;
			}
		}
	}

	/**
	 * @param row
	 * @param targetObjectBase
	 * @param selectedObjectBase
	 * @return
	 */
	public ObjectBase confirm(int row, ObjectBase targetObjectBase, ObjectBase selectedObjectBase) {
		if ( !isVisible())
			return null;

		// 編集状態でない場合
		if ( is_empty())
			return selectedObjectBase;	// 何もしない

		// 複数選択されているかまたは選択されているオブジェクトがこのオブジェクトと異なる場合
		if ( ( null == targetObjectBase)
			|| !ObjectBase.is_target( _kind, targetObjectBase)) {
			ObjectBase objectBase = ( ObjectBase)_variableTableBase.get( _kind, _nameTextField.getText());
			if ( null == objectBase) {
				// 同じ名前のものが無いので追加または無視を選択
				if ( JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.append.variable.confirm.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind.replaceAll(" ", ".")),
					JOptionPane.YES_NO_OPTION))
					return selectedObjectBase;	// 無視なら何もしない

				objectBase = create();
				if ( !can_get_data( objectBase))
					return null;		// 追加出来ないので選択を変えさせない

				// オブジェクトを追加
				get_data( objectBase);
				_variableTableBase.append( objectBase);
				update();
				return objectBase;
			} else {
				ObjectBase temp = create_and_get();
				if ( temp.equals( objectBase))
					return selectedObjectBase;	// 同じ名前のものがあっても中身が全く同じなら何もしない

				// 中身が異なる同じ名前のものがあるので更新または無視を選択
				if ( JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.update.variable.confirm.message") + " - " + objectBase._name,
					ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind.replaceAll(" ", ".")),
					JOptionPane.YES_NO_OPTION))
					return selectedObjectBase;	// 無視なら何もしない

				if ( !can_get_data( objectBase))
					return null;		// 更新出来ないので選択を変えさせない

				// 更新する行番号を取得
				row = _variableTableBase.get( objectBase);
				if ( 0 > row)		// これは起こり得ない筈だが念の為
					return null;	// 更新出来ないので選択を変えさせない

				// オブジェクトを更新
				get_data( objectBase);
				update( row, objectBase, false);
				update();
				return objectBase;
			}
		}

		ObjectBase objectBase = create_and_get();
		if ( objectBase.equals( targetObjectBase))
			return selectedObjectBase;	// 編集されていなければ何もしない


		if ( !targetObjectBase._name.equals( _nameTextField.getText())) {
			// 名前が異なるので追加、更新または無視を選択
			String[] options = new String[] {
				ResourceManager.get_instance().get( "edit.object.dialog.append.option"),
				ResourceManager.get_instance().get( "edit.object.dialog.update.option"),
				ResourceManager.get_instance().get( "edit.object.dialog.ignore.option")
			};
			int result = JOptionPane.showOptionDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.append.or.update.variable.confirm.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind.replaceAll(" ", ".")),
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE,
				null,
				options,
				options[ 0]);
			switch ( result) {
				case 0:		// 追加の場合
					if ( !can_get_data( objectBase))
						return null;		// 追加出来ないので選択を変えさせない

					// オブジェクトを追加
					get_data( objectBase);
					_variableTableBase.append( objectBase);
					update();
					return objectBase;
				case 1:		// 更新の場合
					if ( !can_get_data( targetObjectBase))
						return null;		// 更新出来ないので選択を変えさせない

					// オブジェクトを更新
					update( row, targetObjectBase, false);
					return targetObjectBase;
				default:	// 無視の場合
					return selectedObjectBase;	// 無視なら何もしない
			}
		} else {
			// 名前が同じなので更新または無視を選択
			if ( JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.update.variable.confirm.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind.replaceAll(" ", ".")),
				JOptionPane.YES_NO_OPTION))
				return selectedObjectBase;	// 無視なら何もしない

			if ( !can_get_data( targetObjectBase))
				return null;		// 更新出来ないので選択を変えさせない

			// オブジェクトを更新
			update( row, targetObjectBase, false);
			return targetObjectBase;
		}
	}

	/**
	 * @param objectBase
	 * @param objectBases
	 * @return
	 */
	public boolean can_paste(ObjectBase objectBase, List<ObjectBase> objectBases) {
		return true;
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

	/**
	 * @param active
	 */
	public void activate(boolean active) {
		setVisible( active);

		if ( !active)
			return;

		clear_textUndoRedoManagers();
		setup_textUndoRedoManagers();
	}

	/**
	 * 
	 */
	protected void clear_textUndoRedoManagers() {
		_textUndoRedoManagers.clear();
	}

	/**
	 * 
	 */
	protected void setup_textUndoRedoManagers() {
	}
}
