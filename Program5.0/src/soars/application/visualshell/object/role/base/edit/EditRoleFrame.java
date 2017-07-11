/*
 * 2005/05/05
 */
package soars.application.visualshell.object.role.base.edit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.MainFrame;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.status.StatusFrame;
import soars.application.visualshell.object.role.base.edit.tab.CommandTabbedPane;
import soars.application.visualshell.object.role.base.edit.tab.ConditionTabbedPane;
import soars.application.visualshell.object.role.base.edit.tab.RuleTabbedPane;
import soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.edit.table.RuleRowHeaderTable;
import soars.application.visualshell.object.role.base.edit.table.RuleTable;
import soars.application.visualshell.object.role.base.edit.tree.RuleTree;
import soars.application.visualshell.object.role.spot.SpotRole;
import soars.application.visualshell.object.stage.StageManager;
import soars.application.visualshell.observer.Observer;
import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Frame;
import soars.common.utility.tool.expression.Expression;
import soars.common.utility.tool.resource.Resource;

/**
 * @author kurata
 */
public class EditRoleFrame extends Frame implements ITextUndoRedoManagerCallBack {

	/**
	 * 
	 */
	static public final int _minimumWidth = 800;

	/**
	 * 
	 */
	static public final int _minimumHeight = 600;

	/**
	 * 
	 */
	private Role _role = null;

	/**
	 * 
	 */
	private JTextField _nameTextField = null;

	/**
	 * 
	 */
	private JTextField _roleCommentTextField = null;

	/**
	 * 
	 */
	//private JButton _removeTableColumnButton = null;

	/**
	 * 
	 */
	protected JSplitPane _splitPane1 = null;

	/**
	 * 
	 */
	protected RuleTable _ruleTable = null;

	/**
	 * 
	 */
	protected RuleRowHeaderTable _ruleRowHeaderTable = null;

//	/**
//	 * 
//	 */
//	protected ComboBox _stageComboBox = null;

	/**
	 * 
	 */
	private JTextField _activeCellTextField = null;

	/**
	 * 
	 */
	protected JSplitPane _splitPane2 = null;

	/**
	 * 
	 */
	protected JTabbedPane _ruleTreeTabbedPane = null;

	/**
	 * 
	 */
	protected RuleTree _conditionRuleTree = null;

	/**
	 * 
	 */
	protected RuleTree _commandRuleTree = null;

	/**
	 * 
	 */
	protected JTabbedPane _ruleTabTabbedPane = null;

	/**
	 * 
	 */
	protected ConditionTabbedPane _conditionTabbedPane = null;

	/**
	 * 
	 */
	protected CommandTabbedPane _commandTabbedPane = null;

	/**
	 * 
	 */
	private Map<String, List<PanelRoot>> _buddiesMap = new HashMap<String, List<PanelRoot>>();

	/**
	 * 
	 */
	protected JTextField _ruleCommentTextField = null;

	/**
	 * 
	 */
	private List<JLabel> _components = new ArrayList<JLabel>();

	/**
	 * 
	 */
	private List<JButton> _buttons = new ArrayList<JButton>();

	/**
	 * 
	 */
	private StatusFrame _statusFrame = null;

	/**
	 * 
	 */
	protected List<TextUndoRedoManager> _textUndoRedoManagers = new ArrayList<TextUndoRedoManager>();

	/**
	 * @param arg0
	 * @param role
	 * @throws java.awt.HeadlessException
	 */
	public EditRoleFrame(String arg0, Role role) throws HeadlessException {
		super(arg0);
		_role = role;
	}

	/**
	 * @param srcColumnWidths
	 * @return
	 */
	private Vector<Integer> copy(Vector<Integer> srcColumnWidths) {
		Vector<Integer> dstColumnWidths = new Vector<Integer>();
		for ( int i = 0; i < srcColumnWidths.size(); ++i) {
			Integer integer = srcColumnWidths.get( i);
			dstColumnWidths.add( new Integer( integer.intValue()));
		}
		return dstColumnWidths;
	}

	/**
	 * @return
	 */
	private Rectangle get_rectangle_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._editRoleDialogRectangleKey + "x",
			String.valueOf( SwingTool.get_default_window_position( MainFrame.get_instance(), _minimumWidth, _minimumHeight).x));
		if ( null == value)
			return null;

		int x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editRoleDialogRectangleKey + "y",
			String.valueOf( SwingTool.get_default_window_position( MainFrame.get_instance(), _minimumWidth, _minimumHeight).y));
		if ( null == value)
			return null;

		int y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editRoleDialogRectangleKey + "width",
			String.valueOf( _minimumWidth));
		if ( null == value)
			return null;

		int width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._editRoleDialogRectangleKey + "height",
			String.valueOf( _minimumHeight));
		if ( null == value)
			return null;

		int height = Integer.parseInt( value);

		return new Rectangle( x, y, width, height);
	}

	/**
	 * 
	 */
	private void optimize_window_rectangle() {
		Rectangle rectangle = getBounds();
		if ( !GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersects( rectangle)
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( rectangle).width <= 10
			|| rectangle.y <= -getInsets().top
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( rectangle).height <= getInsets().top) {
			if ( _splitPane1.getDividerLocation() >= ( _minimumHeight - getInsets().top - getInsets().bottom))
				_splitPane1.setDividerLocation( 200);

			if ( _splitPane2.getDividerLocation() >= ( _minimumWidth - getInsets().left - getInsets().right))
				_splitPane2.setDividerLocation( 100);

			setSize( _minimumWidth, _minimumHeight);
			setLocationRelativeTo( MainFrame.get_instance());
		}
	}

	/**
	 * 
	 */
	protected void set_property_to_environment_file() {
		Rectangle rectangle = getBounds();

		Environment.get_instance().set(
			Environment._editRoleDialogRectangleKey + "x", String.valueOf( rectangle.x));
		Environment.get_instance().set(
			Environment._editRoleDialogRectangleKey + "y", String.valueOf( rectangle.y));
		Environment.get_instance().set(
			Environment._editRoleDialogRectangleKey + "width", String.valueOf( rectangle.width));
		Environment.get_instance().set(
			Environment._editRoleDialogRectangleKey + "height", String.valueOf( rectangle.height));

		Environment.get_instance().set(
			Environment._editRuleDialogDividerLocationKey1, String.valueOf( _splitPane1.getDividerLocation()));
		Environment.get_instance().set(
			Environment._editRuleDialogDividerLocationKey2, String.valueOf( _splitPane2.getDividerLocation()));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_create()
	 */
	@Override
	protected boolean on_create() {
		if (!super.on_create())
			return false;

		create_instances();

		setLayout( new BorderLayout());

		setup_north_panel();

		if ( !setup_center_panel())
			return false;

		setup_south_panel();

		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

		Rectangle rectangle = get_rectangle_from_environment_file();
		if ( null == rectangle)
			setBounds( rectangle.x, rectangle.y, _minimumWidth, _minimumHeight);
		else
			setBounds( rectangle);

		adjust();

		on_setup_completed();

		Image image = Resource.load_image_from_resource( Constant._resourceDirectory + "/image/icon/icon.png", getClass());
		if ( null != image)
			setIconImage( image);

		if ( !create_status_window())
			return false;

		_statusFrame.setVisible( true);
		setVisible( true);

		MainFrame.get_instance().enabled( false);


		return true;
	}

	/**
	 * @return
	 */
	private boolean create_status_window() {
		_statusFrame = new StatusFrame( ResourceManager.get_instance().get( "status.frame.title"));
		if ( !_statusFrame.create())
			return false;

		//_statusFrame.setAlwaysOnTop( true);

		return true;
	}

	/**
	 * 
	 */
	private void create_instances() {
		if ( _role instanceof AgentRole)
			_nameTextField = new JTextField( new TextExcluder( Constant._prohibitedCharacters2), _role._name, 0);
		else
			_nameTextField = new JTextField( new TextExcluder( Constant._prohibitedCharacters6), _role._name, 0);

		_nameTextField.setEditable( !_role._global);

		_textUndoRedoManagers.add( new TextUndoRedoManager( _nameTextField, this));


		_roleCommentTextField = new JTextField();
		_roleCommentTextField.setText( _role._comment);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _roleCommentTextField, this));


		_ruleTable = new RuleTable( _role, this, this);
		_ruleRowHeaderTable = new RuleRowHeaderTable( _role, this, this);


		_activeCellTextField = new JTextField();
		_activeCellTextField.setEditable( false);


		_ruleTreeTabbedPane = new JTabbedPane( SwingConstants.TOP, JTabbedPane.WRAP_TAB_LAYOUT);


		_ruleTabTabbedPane = new JTabbedPane( SwingConstants.TOP, JTabbedPane.WRAP_TAB_LAYOUT);


		_conditionRuleTree = new RuleTree( "condition", this, this);
		_conditionTabbedPane = new ConditionTabbedPane( "condition");

		_commandRuleTree = new RuleTree( "command", this, this);
		_commandTabbedPane = new CommandTabbedPane( "command");

		_ruleCommentTextField = new JTextField();
	}

	/**
	 * 
	 */
	private void setup_north_panel() {
		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		JPanel panel = setup_nameTextField();
		//setup_appendTableRowButton( panel);
		//setup_appendTableColumnButton( panel);
		//setup_removeTableColumnButton( panel);
		northPanel.add( panel);

		insert_horizontal_glue( northPanel);

		setup_roleCommentTextField( northPanel);

		insert_horizontal_glue( northPanel);

		add( northPanel, "North");
	}

	/**
	 * @return
	 */
	private JPanel setup_nameTextField() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.role.dialog.name"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		panel.add( _nameTextField);
		panel.add( Box.createHorizontalStrut( 5));

		return panel;
	}

//	/**
//	 * @param parent
//	 */
//	private void setup_appendTableRowButton(JPanel parent) {
//		JButton button = new JButton( ResourceManager.get_instance().get( "edit.role.dialog.append.table.row.button.name"));
//		button.addActionListener( new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				_ruleTable.insertRows( new int[] { _ruleTable.getRowCount()});
//			}
//		});
//		_buttons.add( button);
//		parent.add( button);
//		parent.add( Box.createHorizontalStrut( 5));
//	}
//
//	/**
//	 * @param parent
//	 */
//	private void setup_appendTableColumnButton(JPanel parent) {
//		JButton button = new JButton( ResourceManager.get_instance().get( "edit.role.dialog.append.table.column.button.name"));
//		button.addActionListener( new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				_ruleTable.insertColumns( new int[] { _ruleTable.getColumnCount()}, RuleManager._defaultRuleTableColumnWidth);
//			}
//		});
//		_buttons.add( button);
//		parent.add( button);
//		parent.add( Box.createHorizontalStrut( 5));
//	}
//
//	/**
//	 * @param parent
//	 */
//	private void setup_removeTableColumnButton(JPanel parent) {
//		_removeTableColumnButton = new JButton( ResourceManager.get_instance().get( "edit.role.dialog.remove.table.column.button.name"));
//		_removeTableColumnButton.addActionListener( new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				_ruleTable.removeColumns( new int[] { _ruleTable.getColumnCount() - 1}, RuleManager._default_rule_table_column_width);
//			}
//		});
//		_buttons.add( _removeTableColumnButton);
//		parent.add( _removeTableColumnButton);
//		parent.add( Box.createHorizontalStrut( 5));
//	}

	/**
	 * @param parent
	 */
	private void setup_roleCommentTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.role.dialog.comment"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		_components.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		panel.add( _roleCommentTextField);
		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @return
	 */
	private boolean setup_center_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_splitPane1 = new JSplitPane( JSplitPane.VERTICAL_SPLIT);

		if ( !setup_table_panel())
			return false;

		if ( !setup_editor_panel())
			return false;

		_splitPane1.setDividerLocation( Integer.parseInt( Environment.get_instance().get( Environment._editRuleDialogDividerLocationKey1, "200")));
		panel.add( _splitPane1);

		panel.add( Box.createHorizontalStrut( 5));

		add( panel);

		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_table_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		if ( !setup_table_panel( panel))
			return false;

		setup_activeCellTextField( panel);

		_splitPane1.setTopComponent( panel);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_table_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		if ( !_ruleTable.setup( _role._ruleManager, _activeCellTextField, _ruleCommentTextField, _ruleRowHeaderTable, _ruleTreeTabbedPane, _conditionRuleTree, _commandRuleTree, _ruleTabTabbedPane, _conditionTabbedPane, _commandTabbedPane))
			return false;

		if ( !_ruleRowHeaderTable.setup( _role._ruleManager, _ruleTable, ( Graphics2D)getGraphics()))
			return false;

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _ruleTable);
		scrollPane.getViewport().setOpaque( true);
		scrollPane.getViewport().setBackground( SystemColor.text);

		scrollPane.setRowHeaderView( _ruleRowHeaderTable);
		scrollPane.getRowHeader().setOpaque( true);
		scrollPane.getRowHeader().setBackground( SystemColor.text);
		Dimension dimension = scrollPane.getRowHeader().getPreferredSize();
		scrollPane.getRowHeader().setPreferredSize(
			new Dimension( _ruleRowHeaderTable.getColumnModel().getColumn( 0).getWidth(),
			dimension.height));
		SwingTool.set_table_left_top_corner_column( scrollPane);

		// スクロール時に２つのTableが同期するように以下のhandlerが必要
		scrollPane.getRowHeader().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JViewport viewport = ( JViewport)e.getSource();
				scrollPane.getVerticalScrollBar().setValue( viewport.getViewPosition().y);
			}
		});

//		scrollPane.addMouseListener( new MouseInputAdapter() {
//			public void mouseReleased(MouseEvent arg0) {
//				if ( !SwingTool.is_mouse_right_button( arg0))
//					return;
//
//				_ruleRowHeaderTable.on_mouse_right_up( new Point( arg0.getX(),
//					arg0.getY() - _ruleTable.getTableHeader().getHeight()));
//			}
//		});

		scrollPane.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( Color.red, 2), BorderFactory.createEmptyBorder( 2, 2, 2, 2)));
		panel.add( scrollPane);
		panel.add( Box.createHorizontalStrut( 5));
		parent.add( panel);
		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_activeCellTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		panel.add( _activeCellTextField);
		panel.add( Box.createHorizontalStrut( 5));
		parent.add( panel, "South");
	}

	/**
	 * @return
	 */
	private boolean setup_editor_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		if ( !setup_editor_panel( panel))
			return false;

		setup_ruleCommentTextField( panel);

		_splitPane1.setBottomComponent( panel);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_editor_panel(JPanel parent) {
		if ( !setup_tree( parent))
			return false;

		if ( !setup_condition())
			return false;

		if ( !setup_command())
			return false;

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_tree(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));


		_splitPane2 = new JSplitPane();


		_ruleTreeTabbedPane.addChangeListener( new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				// 条件タブとコマンドタブ切り替え時の処理
				if ( 0 < _ruleTreeTabbedPane.getTabCount() && 0 < _ruleTabTabbedPane.getTabCount()) {
					RuleTree ruleTree = ( RuleTree)_ruleTreeTabbedPane.getSelectedComponent();
					if ( !_ruleTabTabbedPane.getSelectedComponent().equals( ruleTree._ruleTabbedPane))
						_ruleTabTabbedPane.setSelectedComponent( ruleTree._ruleTabbedPane);
				}
			}
		});
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setView( _ruleTreeTabbedPane);
		scrollPane.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( Color.blue, 2), BorderFactory.createEmptyBorder( 2, 2, 2, 2)));
		_splitPane2.setLeftComponent( scrollPane);


		_ruleTabTabbedPane.addChangeListener( new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				// 条件タブとコマンドタブ切り替え時の処理
				if ( 0 < _ruleTabTabbedPane.getTabCount() && 0 < _ruleTabTabbedPane.getTabCount()) {
					RuleTabbedPane ruleTabbedPane = ( RuleTabbedPane)_ruleTabTabbedPane.getSelectedComponent();
					if ( !_ruleTreeTabbedPane.getSelectedComponent().equals( ruleTabbedPane._ruleTree))
						_ruleTreeTabbedPane.setSelectedComponent( ruleTabbedPane._ruleTree);
				}
			}
		});
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setView( _ruleTabTabbedPane);
		scrollPane.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( Color.green, 2), BorderFactory.createEmptyBorder( 2, 2, 2, 2)));
		_splitPane2.setRightComponent( scrollPane);

		_splitPane2.setDividerLocation( Integer.parseInt( Environment.get_instance().get( Environment._editRuleDialogDividerLocationKey2, "100")));


		panel.add( _splitPane2);


		panel.add( Box.createHorizontalStrut( 5));


		parent.add( panel);


		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_condition() {
		if ( !_conditionRuleTree.setup( _conditionTabbedPane, ResourceManager.get_instance().get( "edit.rule.dialog.condition.rule.tree.root.text")))
			return false;

		_ruleTreeTabbedPane.add( _conditionRuleTree, ResourceManager.get_instance().get( "edit.rule.dialog.condition.rule.tree.title"));



		if ( !_conditionTabbedPane.setup( _conditionRuleTree, _role, _buddiesMap, this, this))
			return false;

		_ruleTabTabbedPane.add( _conditionTabbedPane, ResourceManager.get_instance().get( "edit.rule.dialog.condition.tabbed.pane.title"));



		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_command() {
		if ( !_commandRuleTree.setup( _commandTabbedPane,  ResourceManager.get_instance().get( "edit.rule.dialog.command.rule.tree.root.text")))
			return false;

		_ruleTreeTabbedPane.add( _commandRuleTree, ResourceManager.get_instance().get( "edit.rule.dialog.command.rule.tree.title"));



		if ( !_commandTabbedPane.setup( _commandRuleTree, _role, _buddiesMap, this, this))
			return false;

		_ruleTabTabbedPane.add( _commandTabbedPane, ResourceManager.get_instance().get( "edit.rule.dialog.command.tabbed.pane.title"));



		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_ruleCommentTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JButton button = new JButton( ResourceManager.get_instance().get( "edit.rule.dialog.comment"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_ruleTable.update_ruleComment();
			}
		});
		//button.setHorizontalAlignment( SwingConstants.RIGHT);
		_buttons.add( button);
		panel.add( button);

		panel.add( Box.createHorizontalStrut( 5));

		panel.add( _ruleCommentTextField);

		panel.add( Box.createHorizontalStrut( 5));
		parent.add( panel, "South");
	}

	/**
	 * 
	 */
	private void setup_south_panel() {
		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( southPanel);

		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BoxLayout( basePanel, BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));
		setup_editStageButton( panel);
		setup_editExpressionButton( panel);
		basePanel.add( panel);

		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));
		setup_ok_and_cancel_button( panel);
		basePanel.add( panel);

		southPanel.add( basePanel);

		insert_horizontal_glue( southPanel);

		add( southPanel, "South");
	}

	/**
	 * @param parent
	 */
	private void setup_editStageButton(JPanel parent) {
		JButton button = new JButton( ResourceManager.get_instance().get( "setting.stage.menu"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_edit_stage( arg0);
			}
		});
		_buttons.add( button);
		parent.add( button);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_edit_stage(ActionEvent actionEvent) {
		_ruleTable.stop_cell_editing();
		StageManager.get_instance().edit( this, this);
	}

	/**
	 * @param parent
	 */
	private void setup_editExpressionButton(JPanel parent) {
		JButton button = new JButton( ResourceManager.get_instance().get( "setting.expression.menu"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_edit_expression( arg0);
			}
		});
		_buttons.add( button);
		parent.add( button);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_edit_expression(ActionEvent actionEvent) {
		// TODO 2014.2.14
		_ruleTable.stop_cell_editing();
		VisualShellExpressionManager.get_instance().edit( this, this);
	}

	/**
	 * @param panel
	 */
	protected void setup_ok_and_cancel_button(JPanel panel) {
		JButton okButton = new JButton( ResourceManager.get_instance().get( "dialog.ok"));

		okButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_ok( arg0);
			}
		});

		panel.add( okButton);


		JButton cancelButton = new JButton( ResourceManager.get_instance().get( "dialog.cancel"));
		cancelButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_cancel( arg0);
			}
		});

		panel.add( cancelButton);


		int width = Math.max( okButton.getPreferredSize().width, cancelButton.getPreferredSize().width);

		okButton.setPreferredSize( new Dimension( width, okButton.getPreferredSize().height));
		cancelButton.setPreferredSize( new Dimension( width, cancelButton.getPreferredSize().height));
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( JComponent component:_components)
			width = Math.max( width, component.getPreferredSize().width);

		for ( JComponent component:_components)
			component.setPreferredSize( new Dimension( width, component.getPreferredSize().height));

		width = 0;
		for ( JButton button:_buttons)
			width = Math.max( width, button.getPreferredSize().width);

		for ( JButton button:_buttons)
			button.setPreferredSize( new Dimension( width, button.getPreferredSize().height));
	}

	/**
	 * 
	 */
	protected void on_setup_completed() {
		select_default();	// タブではなくなったからここで１回初期化が必要！

		optimize_window_rectangle();

//		_removeTableColumnButton.setEnabled(
//			_conditionTable1.getColumnCount() > RuleManager.get_column_minimum_size());

		initialize( _conditionTabbedPane);
		select_default();
		initialize( _commandTabbedPane);

		_nameTextField.requestFocusInWindow();

		_ruleTable.select( 0, 0);

		_ruleTable.on_setup_completed();

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				int width = getSize().width;
				int height = getSize().height;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width,
					( _minimumHeight > height) ? _minimumHeight : height);
			}
		});
	}

	/**
	 * 
	 */
	private void select_default() {
		_conditionTabbedPane.select_default();
		_commandTabbedPane.select_default();
	}

	/**
	 * @param ruleTabbedPane
	 */
	private void initialize(RuleTabbedPane ruleTabbedPane) {
		for ( RulePropertyPanelBase rulePropertyPanelBase:ruleTabbedPane._rulePropertyPanelBases) {
			rulePropertyPanelBase.on_setup_completed();
			rulePropertyPanelBase.set( null);
		}

		_conditionTabbedPane.revalidate();
		_commandTabbedPane.revalidate();

		_conditionTabbedPane.repaint();
		_commandTabbedPane.repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_window_closing(java.awt.event.WindowEvent)
	 */
	@Override
	protected void on_window_closing(WindowEvent windowEvent) {
		on_close();
	}

	/**
	 * 
	 */
	private void on_close() {
		_statusFrame.close();

		set_property_to_environment_file();
		RulePropertyPanelBase.refresh();

		dispose();

		Role._editRoleFrame = null;

		MainFrame.get_instance().enabled( true);
		MainFrame.get_instance().toFront();

		System.gc();
	}

	/**
	 * 
	 */
	public void on_update_stage() {
		_ruleTable.on_update_stage();

		// TODO 2012.9.20
		_conditionTabbedPane.on_update_stage();
		_commandTabbedPane.on_update_stage();
//		RulePropertyPanelBase rulePropertyPanelBase = _commandTabbedPane.get( ResourceManager.get_instance().get( "rule.type.command.others"));
//		if ( null != rulePropertyPanelBase) {
//			OthersCommandPropertyPanel othersCommandPropertyPanel = ( OthersCommandPropertyPanel)rulePropertyPanelBase;
//			othersCommandPropertyPanel.on_update_stage();
//		}
	}

	/**
	 * @param newName
	 * @param originalName
	 * @return
	 */
	public boolean update_stage_name(String newName, String originalName) {
		boolean result = _ruleTable.update_stage_name( newName, originalName);

		// TODO 2012.9.20
		if ( _conditionTabbedPane.update_stage_name( newName, originalName))
			result = true;

		if ( _commandTabbedPane.update_stage_name( newName, originalName))
			result = true;
//		RulePropertyPanelBase rulePropertyPanelBase = _commandTabbedPane.get( ResourceManager.get_instance().get( "rule.type.command.others"));
//		if ( null != rulePropertyPanelBase) {
//			OthersCommandPropertyPanel othersCommandPropertyPanel = ( OthersCommandPropertyPanel)rulePropertyPanelBase;
//			othersCommandPropertyPanel.update_stage_name( newName, originalName);
//		}
	
		return result;
	}

	/**
	 * @param name
	 * @return
	 */
	public boolean can_remove_stage_name(String name) {
		return _ruleTable.can_remove_stage_name( name);
	}

	/**
	 * @param newExpression
	 * @param newVariableCount
	 * @param originalExpression
	 * @return
	 */
	public boolean update_expression(Expression newExpression, int newVariableCount, Expression originalExpression) {
		// TODO 2014.2.14
		return _ruleTable.update_expression( newExpression, newVariableCount, originalExpression);
	}

	/**
	 * @param newExpression
	 * @param newVariableCount
	 * @param originalExpression
	 */
	public void on_update_expression(Expression newExpression, int newVariableCount, Expression originalExpression) {
		// TODO 2014.2.14
		_conditionTabbedPane.on_update_expression( newExpression, newVariableCount, originalExpression);
		_commandTabbedPane.on_update_expression( newExpression, newVariableCount, originalExpression);
	}

	/**
	 * 
	 */
	public void on_update_expression() {
		// TODO 2014.2.14
		_conditionTabbedPane.on_update_expression();
		_commandTabbedPane.on_update_expression();
	}

	/**
	 * @param expression
	 * @return
	 */
	public boolean can_remove_expression(Expression expression) {
		// TODO 2014.2.14
		return _ruleTable.can_remove_expression( expression);
	}

	/**
	 * @param RulePropertyPanelBase
	 * @param actionEvent
	 */
	public void on_apply(RulePropertyPanelBase rulePropertyPanelBase, ActionEvent actionEvent) {
		boolean updated = false;
		int index = _conditionTabbedPane.get_index( rulePropertyPanelBase);
		if ( 0 <= index)
			updated = _ruleTable.update( rulePropertyPanelBase);
		else {
			index = _commandTabbedPane.get_index( rulePropertyPanelBase);
			if ( 0 <= index)
				updated = _ruleTable.update( rulePropertyPanelBase);
		}

		if ( !updated)
			return;

		_conditionTabbedPane.update( rulePropertyPanelBase);
		_commandTabbedPane.update( rulePropertyPanelBase);
		_ruleTable.set_text_to_activeCellTextField();
	}

	/**
	 * @param actionEvent
	 */
	protected void on_ok(ActionEvent actionEvent) {
		if ( 0 <= _nameTextField.getText().indexOf( '$'))
			return;
		//if ( _name_textField.getText().equals( Constant._spot_chart_role_name)
		//	|| 0 <= _name_textField.getText().indexOf( '$'))
		//	return;

		if ( _role instanceof SpotRole) {
			if ( _nameTextField.getText().equals( ""))
				return;
		}

		if ( LayerManager.get_instance().contains( _role, _nameTextField.getText())
			|| LayerManager.get_instance().is_agent_object_name( "role variable", _nameTextField.getText())
			//|| _commandTable.contains_this_agent_role_variable( _name_textField.getText(), null)
			|| LayerManager.get_instance().chartObject_has_same_name( _nameTextField.getText(), "")
			|| _nameTextField.getText().equals( Constant._initialDataFileRoleName)
			|| _nameTextField.getText().equals( Constant._initialDataFileSpotName))
			return;

		int[] rows = _ruleTable.getSelectedRows();
		int[] columns = _ruleTable.getSelectedColumns();
		int result1 = JOptionPane.NO_OPTION;
		int result2 = JOptionPane.NO_OPTION;
		if ( ( null != rows) && ( 1 == rows.length) && ( 0 <= rows[ 0]) && ( _ruleTable.getRowCount() > rows[ 0])
			&& ( null != columns) && ( 1 == columns.length) && ( 0 < columns[ 0]) && ( _ruleTable.getRowCount() > rows[ 0]))
			result1 = _ruleTable.on_change_selection( rows[ 0], columns[ 0], JOptionPane.YES_NO_CANCEL_OPTION);
		if ( ( null != rows) && ( 1 == rows.length) && ( 0 <= rows[ 0]) && ( _ruleTable.getRowCount() > rows[ 0]))
			result2 = _ruleTable.on_change_selection( rows[ 0], JOptionPane.YES_NO_CANCEL_OPTION);
		if ( JOptionPane.CANCEL_OPTION == result1 || JOptionPane.CLOSED_OPTION  == result1
			|| JOptionPane.CANCEL_OPTION == result2 || JOptionPane.CLOSED_OPTION == result2)
			return;

		String originalName = _role._name;

		_role.rename( _nameTextField.getText(), ( Graphics2D)getGraphics());

		_role._comment = _roleCommentTextField.getText();

		_ruleTable.on_ok( _role._ruleManager);

		if ( !_role._name.equals( originalName))
			Observer.get_instance().on_update_role_name( originalName, _role);

		Observer.get_instance().on_update_role( false);

		Observer.get_instance().modified();

		_statusFrame.close();

		set_property_to_environment_file();

		RulePropertyPanelBase.refresh();

		dispose();

		Role._editRoleFrame = null;

		MainFrame.get_instance().enabled( true);
		MainFrame.get_instance().toFront();
		MainFrame.get_instance().repaint();

		System.gc();
	}

	/**
	 * @param actionEvent
	 */
	protected void on_cancel(ActionEvent actionEvent) {
		on_close();
	}

	/**
	 * Clear buffers for copy and paste.
	 */
	public static void clear() {
		RuleTable.refresh();
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
	public void on_changed(DefaultDocumentEvent defaultDocumentEvent,UndoManager undoManager) {
	}
}
