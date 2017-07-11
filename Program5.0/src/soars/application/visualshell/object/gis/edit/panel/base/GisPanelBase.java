/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.base;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class GisPanelBase extends JPanel implements ITextUndoRedoManagerCallBack {

	/**
	 * 
	 */
	protected String _kind = "";

	/**
	 * 
	 */
	protected GisDataManager _gisDataManager = null;

	/**
	 * 
	 */
	protected Map<String, GisPropertyPanelBase> _gisPropertyPanelBaseMap = null;

	/**
	 * 
	 */
	protected GisVariableTableBase _gisVariableTableBase = null;

	/**
	 * 
	 */
	protected List<JComponent> _labels = new ArrayList<JComponent>();

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
	 * @param gisDataManager
	 * @param gisPropertyPanelBaseMap
	 * @param gisVariableTableBase
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public GisPanelBase(String kind, GisDataManager gisDataManager, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, GisVariableTableBase gisVariableTableBase, Color color, Frame owner, Component parent) {
		super();
		_kind = kind;
		_gisDataManager = gisDataManager;
		_gisPropertyPanelBaseMap = gisPropertyPanelBaseMap;
		_gisVariableTableBase = gisVariableTableBase;
		_color = color;
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @return
	 */
	public GisObjectBase create() {
		return GisObjectBase.create( _kind);
	}

	/**
	 * @param gisObjectBase
	 * @return
	 */
	public boolean contains(GisObjectBase gisObjectBase) {
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
	@Override
	public void setEnabled(boolean enabled) {
		for ( JComponent label:_labels)
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

		//insert_vertical_strut( southPanel);

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
	protected GisObjectBase on_append() {
		GisObjectBase gisObjectBase = create();
		return on_update( gisObjectBase) ? gisObjectBase : null;
	}

	/**
	 * @param actionEvent
	 */
	protected void on_update(ActionEvent actionEvent) {
	}

	/**
	 * @param row
	 * @param gisObjectBase
	 * @param selection
	 */
	protected void update(int row, GisObjectBase gisObjectBase, boolean selection) {
	}

	/**
	 * @param gisObjectBase
	 * @return
	 */
	protected boolean on_update(GisObjectBase gisObjectBase) {
		if ( !can_get_data( gisObjectBase))
			return false;

		get_data( gisObjectBase);

		return true;
	}

	/**
	 * @param gisObjectBase
	 */
	public void update(GisObjectBase gisObjectBase) {
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
	protected GisObjectBase create_and_get() {
		return null;
	}

	/**
	 * @param gisObjectBase
	 */
	protected boolean can_get_data(GisObjectBase gisObjectBase) {
		return false;
	}

	/**
	 * @param gisObjectBase
	 */
	protected void get_data(GisObjectBase gisObjectBase) {
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

		int[] rows = _gisVariableTableBase.getSelectedRows();
		GisObjectBase object = create();
		GisObjectBase gisObjectBase = confirm(
			( null == rows || 1 != rows.length) ? -1 : rows[ 0], 
			( null == rows || 1 != rows.length) ? null : ( GisObjectBase)_gisVariableTableBase.getValueAt( rows[ 0], 0), 
			object);
		if ( null == gisObjectBase) {
			// 追加または更新に失敗
			if ( fromTree)
				_reject = true;
			return false;
		} else {
			if ( gisObjectBase == object) {
				// 無視の場合
				if ( null == rows || 1 != rows.length)
					clear();

				return true;
			} else {
				// 追加または更新されたオブジェクトを選択させる
				_gisVariableTableBase.select( gisObjectBase);
				if ( fromTree)
					_reject = true;
				return false;
			}
		}
//		if ( !isVisible())
//			return true;
//
//		int[] rows = _variableTableBase.getSelectedRows();
//		GisObjectBase object = create();
//		GisObjectBase gisObjectBase = confirm(
//			( null == rows || 1 != rows.length) ? -1 : rows[ 0], 
//			( null == rows || 1 != rows.length) ? null : ( GisObjectBase)_variableTableBase.getValueAt( rows[ 0], 0), 
//			object);
//		if ( null == objectBase) {
//			// 追加または更新に失敗
//			return false;
//		} else {
//			if ( objectBase == object) {
//				// 無視の場合
//				if ( null == rows || 1 != rows.length)
//					clear();
//
//				return true;
//			} else {
//				// 追加または更新されたオブジェクトを選択させる
//				_variableTableBase.select( objectBase);
//				return false;
//			}
//		}
	}

	/**
	 * @param row
	 * @param targetGisObjectBase
	 * @param selectedGisObjectBase
	 * @return
	 */
	public GisObjectBase confirm(int row, GisObjectBase targetGisObjectBase, GisObjectBase selectedGisObjectBase) {
		// TODO Auto-generated method stub
		if ( !isVisible())
			return null;

		// 編集状態でない場合
		if ( is_empty())
			return selectedGisObjectBase;	// 何もしない

		// 複数選択されているかまたは選択されているオブジェクトがこのオブジェクトと異なる場合
		if ( ( null == targetGisObjectBase)
			|| !GisObjectBase.is_target( _kind, targetGisObjectBase)) {
			GisObjectBase gisObjectBase = ( GisObjectBase)_gisVariableTableBase.get( _kind, _nameTextField.getText());
			if ( null == gisObjectBase) {
				// 同じ名前のものが無いので追加または無視を選択
				if ( JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.append.variable.confirm.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind.replaceAll(" ", ".")),
					JOptionPane.YES_NO_OPTION))
					return selectedGisObjectBase;	// 無視なら何もしない

				gisObjectBase = create();
				if ( !can_get_data( gisObjectBase))
					return null;		// 追加出来ないので選択を変えさせない

				// オブジェクトを追加
				get_data( gisObjectBase);
				_gisVariableTableBase.append( gisObjectBase);
				update();
				return gisObjectBase;
			} else {
				GisObjectBase temp = create_and_get();
				if ( temp.equals( gisObjectBase))
					return selectedGisObjectBase;	// 同じ名前のものがあっても中身が全く同じなら何もしない

				// 中身が異なる同じ名前のものがあるので更新または無視を選択
				if ( JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.update.variable.confirm.message") + " - " + gisObjectBase._name,
					ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind.replaceAll(" ", ".")),
					JOptionPane.YES_NO_OPTION))
					return selectedGisObjectBase;	// 無視なら何もしない

				if ( !can_get_data( gisObjectBase))
					return null;		// 更新出来ないので選択を変えさせない

				// 更新する行番号を取得
				row = _gisVariableTableBase.get( gisObjectBase);
				if ( 0 > row)		// これは起こり得ない筈だが念の為
					return null;	// 更新出来ないので選択を変えさせない

				// オブジェクトを更新
				get_data( gisObjectBase);
				update( row, gisObjectBase, false);
				update();
				return gisObjectBase;
			}
		}

		GisObjectBase gisObjectBase = create_and_get();
		if ( gisObjectBase.equals( targetGisObjectBase))
			return selectedGisObjectBase;	// 編集されていなければ何もしない


		if ( !targetGisObjectBase._name.equals( _nameTextField.getText())) {
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
					if ( !can_get_data( gisObjectBase))
						return null;		// 追加出来ないので選択を変えさせない

					// オブジェクトを追加
					get_data( gisObjectBase);
					_gisVariableTableBase.append( gisObjectBase);
					update();
					return gisObjectBase;
				case 1:		// 更新の場合
					if ( !can_get_data( targetGisObjectBase))
						return null;		// 更新出来ないので選択を変えさせない

					// オブジェクトを更新
					update( row, targetGisObjectBase, false);
					return targetGisObjectBase;
				default:	// 無視の場合
					return selectedGisObjectBase;	// 無視なら何もしない
			}
		} else {
			// 名前が同じなので更新または無視を選択
			if ( JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.update.variable.confirm.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind.replaceAll(" ", ".")),
				JOptionPane.YES_NO_OPTION))
				return selectedGisObjectBase;	// 無視なら何もしない

			if ( !can_get_data( targetGisObjectBase))
				return null;		// 更新出来ないので選択を変えさせない

			// オブジェクトを更新
			update( row, targetGisObjectBase, false);
			return targetGisObjectBase;
		}
	}

	/**
	 * @param gisObjectBase
	 * @param gisObjectBases
	 * @return
	 */
	public boolean can_paste(GisObjectBase gisObjectBase, List<GisObjectBase> gisObjectBases) {
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
