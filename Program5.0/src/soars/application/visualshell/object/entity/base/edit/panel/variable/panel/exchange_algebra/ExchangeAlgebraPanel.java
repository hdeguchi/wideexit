/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.exchange_algebra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.VariableTableBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.VariablePanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.RowHeaderTable;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.exchange_algebra.initial.ExchangeAlgebraInitialValueButtonPanel;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.exchange_algebra.initial.HatPanel;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.exchange_algebra.initial.KeywordPanel;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.exchange_algebra.initial.NamePanel;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.exchange_algebra.initial.SubjectPanel;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.exchange_algebra.initial.TimePanel;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.exchange_algebra.initial.UnitPanel;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.exchange_algebra.table.ExchangeAlgebraInitialValueTable;
import soars.application.visualshell.object.entity.base.object.base.ObjectBase;
import soars.application.visualshell.object.entity.base.object.exchange_algebra.ExchangeAlgebraInitialValue;
import soars.application.visualshell.object.entity.base.object.exchange_algebra.ExchangeAlgebraObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.observer.Observer;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class ExchangeAlgebraPanel extends VariablePanelBase {

	/**
	 * 
	 */
	public TextField _valueTextField = null;

	/**
	 * 
	 */
	protected ComboBox _baseTypeComboBox = null;

	/**
	 * 
	 */
	protected KeywordPanel _keywordPanel = null;

	/**
	 * 
	 */
	protected NamePanel _namePanel = null;

	/**
	 * 
	 */
	protected HatPanel _hatPanel = null;

	/**
	 * 
	 */
	protected UnitPanel _unitPanel = null;

	/**
	 * 
	 */
	protected TimePanel _timePanel = null;

	/**
	 * 
	 */
	protected SubjectPanel _subjectPanel = null;

	/**
	 * 
	 */
	protected List<Component> _struts = new ArrayList<Component>();

	/**
	 * 
	 */
	protected ExchangeAlgebraInitialValueButtonPanel _exchangeAlgebraInitialValueButtonPanel = null;

	/**
	 * 
	 */
	protected ExchangeAlgebraInitialValueTable _exchangeAlgebraInitialValueTable = null;

	/**
	 * 
	 */
	protected RowHeaderTable _rowHeaderTable = null;

	/**
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 * @param variableTableBase
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public ExchangeAlgebraPanel(EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, VariableTableBase variableTableBase, Color color, Frame owner, Component parent) {
		super("exchange algebra", entityBase, propertyPanelBaseMap, variableTableBase, color, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.variable.panel.base.panel.VariablePanelBase#setup_center_panel(javax.swing.JPanel)
	 */
	protected boolean setup_center_panel(JPanel parent) {
		_exchangeAlgebraInitialValueTable = new ExchangeAlgebraInitialValueTable( _color, _entityBase, _propertyPanelBaseMap, this, _owner, _parent);
		_rowHeaderTable = new RowHeaderTable( _color, _entityBase, this, _owner, _parent);

		setup_left_panel( parent);

		if ( !setup_right_panel( parent))
			return false;

		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_left_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_vertical_strut( northPanel);
		setup_name_textField( northPanel);
		insert_vertical_strut( northPanel);
		setup_value_textField( northPanel);
		insert_vertical_strut( northPanel);
		setup_base_type_comboBox( northPanel);
		insert_vertical_strut( northPanel);
		setup_exchangeAlgebraInitialValuePanels( northPanel);
		insert_vertical_strut( northPanel);
		setup_comment_textField( northPanel);

		panel.add( northPanel, "North");

		parent.add( panel);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.variable.panel.base.panel.VariablePanelBase#get_name_label_text()
	 */
	protected String get_name_label_text() {
		return ResourceManager.get_instance().get( "edit.object.dialog.name");
	}

	/**
	 * @param parent
	 */
	private void setup_value_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.exchange.algebra.value.dialog.value"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_valueTextField = new TextField();
		_valueTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters5));
		_valueTextField.setHorizontalAlignment( SwingConstants.RIGHT);
		_valueTextField.setSelectionColor( _color);
		_valueTextField.setForeground( _color);
		_components.add( _valueTextField);
		panel.add( _valueTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_base_type_comboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.exchange.algebra.value.dialog.base.type"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		Vector<String> items = new Vector<String>();
		items.add( ResourceManager.get_instance().get( "edit.exchange.algebra.value.dialog.keyword"));
		items.add( ResourceManager.get_instance().get( "edit.exchange.algebra.value.dialog.immediate"));
		_baseTypeComboBox = new ComboBox( items.toArray( new String[ 0]), _color, false, new CommonComboBoxRenderer( _color, false));
		_baseTypeComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String item = ( String)_baseTypeComboBox.getSelectedItem();
				_keywordPanel.setVisible( item.equals( ResourceManager.get_instance().get( "edit.exchange.algebra.value.dialog.keyword")));
				_namePanel.setVisible( item.equals( ResourceManager.get_instance().get( "edit.exchange.algebra.value.dialog.immediate")));
				_hatPanel.setVisible( item.equals( ResourceManager.get_instance().get( "edit.exchange.algebra.value.dialog.immediate")));
				_unitPanel.setVisible( item.equals( ResourceManager.get_instance().get( "edit.exchange.algebra.value.dialog.immediate")));
				_timePanel.setVisible( item.equals( ResourceManager.get_instance().get( "edit.exchange.algebra.value.dialog.immediate")));
				_subjectPanel.setVisible( item.equals( ResourceManager.get_instance().get( "edit.exchange.algebra.value.dialog.immediate")));
				for (Component component:_struts)
					component.setVisible( item.equals( ResourceManager.get_instance().get( "edit.exchange.algebra.value.dialog.immediate")));
			}
		});
		panel.add( _baseTypeComboBox);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_exchangeAlgebraInitialValuePanels(JPanel parent) {
		_keywordPanel = new KeywordPanel( _color, _entityBase, _propertyPanelBaseMap);
		_namePanel = new NamePanel( _color, _entityBase, _propertyPanelBaseMap);
		_hatPanel = new HatPanel( _color, _entityBase, _propertyPanelBaseMap);
		_unitPanel = new UnitPanel( _color, _entityBase, _propertyPanelBaseMap);
		_timePanel = new TimePanel( _color, _entityBase, _propertyPanelBaseMap);
		_subjectPanel = new SubjectPanel( _color, _entityBase, _propertyPanelBaseMap);
		_exchangeAlgebraInitialValueButtonPanel = new ExchangeAlgebraInitialValueButtonPanel(
			_valueTextField, _baseTypeComboBox, _keywordPanel, _namePanel,
			_hatPanel, _unitPanel, _timePanel, _subjectPanel, _exchangeAlgebraInitialValueTable);

		_keywordPanel.setup();
		_namePanel.setup();
		_hatPanel.setup();
		_unitPanel.setup();
		_timePanel.setup();
		_subjectPanel.setup();
		_exchangeAlgebraInitialValueButtonPanel.setup();

		parent.add( _keywordPanel);
		parent.add( _namePanel);
		insert_strut( parent);
		parent.add( _hatPanel);
		insert_strut( parent);
		parent.add( _unitPanel);
		insert_strut( parent);
		parent.add( _timePanel);
		insert_strut( parent);
		parent.add( _subjectPanel);
		insert_vertical_strut( parent);
		parent.add( _exchangeAlgebraInitialValueButtonPanel);
	}

	/**
	 * @param parent
	 */
	private void insert_strut(JPanel parent) {
		Component component = Box.createVerticalStrut( 5);
		_struts.add( component);
		parent.add( component);
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_right_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_vertical_strut( northPanel);

		if ( !_exchangeAlgebraInitialValueTable.setup( _rowHeaderTable))
			return false;

		if ( !_rowHeaderTable.setup( _exchangeAlgebraInitialValueTable, ( Graphics2D)_parent.getGraphics()))
			return false;

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _exchangeAlgebraInitialValueTable);
		scrollPane.getViewport().setOpaque( true);
		scrollPane.getViewport().setBackground( SystemColor.text);

//		JViewport viewport = new JViewport();
//		viewport.setView( _rowHeaderTable);
//		scrollPane.setRowHeader( viewport);
		scrollPane.setRowHeaderView( _rowHeaderTable);
		scrollPane.getRowHeader().setOpaque( true);
		scrollPane.getRowHeader().setBackground( SystemColor.text);
		Dimension dimension = scrollPane.getRowHeader().getPreferredSize();
		scrollPane.getRowHeader().setPreferredSize(
			new Dimension( _rowHeaderTable.getColumnModel().getColumn( 0).getWidth(),
			dimension.height));
		SwingTool.set_table_left_top_corner_column( scrollPane);

		scrollPane.getRowHeader().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JViewport viewport = ( JViewport) e.getSource();
				scrollPane.getVerticalScrollBar().setValue( viewport.getViewPosition().y);
			}
		});

		northPanel.add( scrollPane);

		insert_vertical_strut( northPanel);

		panel.add( northPanel, "North");

		parent.add( panel);

		scrollPane.setPreferredSize( new Dimension( scrollPane.getPreferredSize().width, 300));

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.variable.panel.base.panel.VariablePanelBase#get_label_width(int)
	 */
	public int get_label_width(int width) {
		for ( JLabel label:_labels)
			width = Math.max( width, label.getPreferredSize().width);

		width = _keywordPanel.get_label_width( width);
		width = _namePanel.get_label_width( width);
		width = _hatPanel.get_label_width( width);
		width = _unitPanel.get_label_width( width);
		width = _timePanel.get_label_width( width);
		width = _subjectPanel.get_label_width( width);

		return width;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.variable.panel.base.panel.VariablePanelBase#adjust(int)
	 */
	public int adjust(int width) {
		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));

		_keywordPanel.adjust( width);
		_namePanel.adjust( width);
		_hatPanel.adjust( width);
		_unitPanel.adjust( width);
		_timePanel.adjust( width);
		_subjectPanel.adjust( width);

		return ( _buttons.get( 0).getPreferredSize().width + 5 + _buttons.get( 1).getPreferredSize().width);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.variable.panel.base.panel.VariablePanelBase#on_setup_completed()
	 */
	public void on_setup_completed() {
		_namePanel.setVisible( false);
		_hatPanel.setVisible( false);
		_unitPanel.setVisible( false);
		_timePanel.setVisible( false);
		_subjectPanel.setVisible( false);
		for (Component component:_struts)
			component.setVisible( false);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.base.PanelBase#contains(soars.application.visualshell.object.entiy.base.object.base.ObjectBase)
	 */
	public boolean contains(ObjectBase objectBase) {
		return _exchangeAlgebraInitialValueTable.contains( objectBase);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.variable.panel.base.panel.VariablePanelBase#update_object_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean update_object_name(String type, String name, String newName) {
		return _exchangeAlgebraInitialValueTable.update_object_name( type, name, newName);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.variable.panel.base.panel.VariablePanelBase#update(soars.application.visualshell.object.entiy.base.object.base.ObjectBase)
	 */
	public void update(ObjectBase objectBase) {
		_nameTextField.setText( ( null != objectBase && objectBase instanceof ExchangeAlgebraObject) ? objectBase._name : "");

		if ( null == objectBase)
			_valueTextField.setText( "");

		_commentTextField.setText( ( null != objectBase && objectBase instanceof ExchangeAlgebraObject) ? objectBase._comment : "");
		_exchangeAlgebraInitialValueTable.update( ( null != objectBase && objectBase instanceof ExchangeAlgebraObject) ? ( ExchangeAlgebraObject)objectBase : null);
		// TODO Auto-generated method stub
		clear_textUndoRedoManagers();
		setup_textUndoRedoManagers();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.base.PanelBase#clear_textUndoRedoManagers()
	 */
	protected void clear_textUndoRedoManagers() {
		// TODO Auto-generated method stub
		super.clear_textUndoRedoManagers();
		_namePanel.clear_textUndoRedoManagers();
		_unitPanel.clear_textUndoRedoManagers();
		_timePanel.clear_textUndoRedoManagers();
		_subjectPanel.clear_textUndoRedoManagers();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.base.PanelBase#setup_textUndoRedoManagers()
	 */
	protected void setup_textUndoRedoManagers() {
		// TODO Auto-generated method stub
		if ( !_textUndoRedoManagers.isEmpty())
			return;

		_textUndoRedoManagers.add( new TextUndoRedoManager( _nameTextField, this));
		_textUndoRedoManagers.add( new TextUndoRedoManager( _valueTextField, this));
		_textUndoRedoManagers.add( new TextUndoRedoManager( _commentTextField, this));

		_namePanel.setup_textUndoRedoManagers();
		_unitPanel.setup_textUndoRedoManagers();
		_timePanel.setup_textUndoRedoManagers();
		_subjectPanel.setup_textUndoRedoManagers();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.variable.panel.base.panel.VariablePanelBase#update()
	 */
	public void update() {
		_keywordPanel.update();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.variable.panel.base.panel.VariablePanelBase#changeSelection(java.lang.Object)
	 */
	public void changeSelection(Object object) {
		if ( null == object)
			return;

		if ( !( object instanceof ExchangeAlgebraInitialValue))
			return;

		_exchangeAlgebraInitialValueButtonPanel.set( ( ExchangeAlgebraInitialValue)object);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.variable.panel.base.panel.VariablePanelBase#clear()
	 */
	public void clear() {
		super.clear();
		_valueTextField.setText( "");
		_namePanel.set( "");
		_unitPanel.set( "");
		_timePanel.set( "");
		_subjectPanel.set( "");
		_exchangeAlgebraInitialValueTable.update( ( ExchangeAlgebraObject)null);
	}
	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.base.PanelBase#is_empty()
	 */
	protected boolean is_empty() {
		return ( super.is_empty()
			&& _valueTextField.getText().equals( "")
			&& _namePanel.get().equals( "")
			&& _unitPanel.get().equals( "")
			&& _timePanel.get().equals( "")
			&& _subjectPanel.get().equals( "")
			&& ( 0 == _exchangeAlgebraInitialValueTable.getRowCount()));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.base.PanelBase#create_and_get()
	 */
	protected ObjectBase create_and_get() {
		ExchangeAlgebraObject exchangeAlgebraObject = new ExchangeAlgebraObject();
		exchangeAlgebraObject._name = _nameTextField.getText();
		exchangeAlgebraObject._comment = _commentTextField.getText();
		_exchangeAlgebraInitialValueTable.get( exchangeAlgebraObject);
		return exchangeAlgebraObject;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.base.PanelBase#can_get_data(soars.application.visualshell.object.entiy.base.object.base.ObjectBase)
	 */
	protected boolean can_get_data(ObjectBase objectBase) {
		if ( !( objectBase instanceof ExchangeAlgebraObject))
			return false;

		ExchangeAlgebraObject exchangeAlgebraObject = ( ExchangeAlgebraObject)objectBase;

		if ( !Constant.is_correct_name( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.map"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( _nameTextField.getText()))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( _nameTextField.getText()))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.map"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( !_nameTextField.getText().equals( exchangeAlgebraObject._name)) {
			if ( _variableTableBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.map"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] propertyPanelBases = Constant.get_propertyPanelBases( "variable");

		for ( int i = 0; i < propertyPanelBases.length; ++i) {
			PropertyPanelBase propertyPanelBase = _propertyPanelBaseMap.get( propertyPanelBases[ i]);
			if ( null != propertyPanelBase && propertyPanelBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.map"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] kinds = Constant.get_kinds( "exchange algebra");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], _nameTextField.getText(), _entityBase)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.map"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if ( null != LayerManager.get_instance().get_chart( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.map"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.base.PanelBase#get_data(soars.application.visualshell.object.entiy.base.object.base.ObjectBase)
	 */
	protected void get_data(ObjectBase objectBase) {
		if ( !( objectBase instanceof ExchangeAlgebraObject))
			return;

		ExchangeAlgebraObject exchangeAlgebraObject = ( ExchangeAlgebraObject)objectBase;

		if ( !exchangeAlgebraObject._name.equals( "") && !_nameTextField.getText().equals( exchangeAlgebraObject._name)) {
			_propertyPanelBaseMap.get( "variable").update_object_name( "exchange algebra", exchangeAlgebraObject._name, _nameTextField.getText());

			WarningManager.get_instance().cleanup();

			boolean result = LayerManager.get_instance().update_object_name( "exchange algebra", exchangeAlgebraObject._name, _nameTextField.getText(), _entityBase);
			if ( result) {
				if ( _entityBase.update_object_name( "exchange algebra", exchangeAlgebraObject._name, _nameTextField.getText())) {
					String[] message = new String[] {
						( ( _entityBase instanceof AgentObject) ? "Agent : " : "Spot : ") + _entityBase._name
					};

					WarningManager.get_instance().add( message);
				}

				if ( _entityBase instanceof AgentObject)
					Observer.get_instance().on_update_agent_object( "exchange algebra", exchangeAlgebraObject._name, _nameTextField.getText());
				else if ( _entityBase instanceof SpotObject)
					Observer.get_instance().on_update_spot_object( "exchange algebra", exchangeAlgebraObject._name, _nameTextField.getText());

				Observer.get_instance().on_update_entityBase( true);
				Observer.get_instance().modified();
			}
		}

		exchangeAlgebraObject._name = _nameTextField.getText();
		exchangeAlgebraObject._comment = _commentTextField.getText();

		_exchangeAlgebraInitialValueTable.get( exchangeAlgebraObject);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.base.PanelBase#can_paste(soars.application.visualshell.object.entiy.base.object.base.ObjectBase, java.util.List)
	 */
	public boolean can_paste(ObjectBase objectBase, List<ObjectBase> objectBases) {
		if ( !( objectBase instanceof ExchangeAlgebraObject))
			return false;

		ExchangeAlgebraObject exchangeAlgebraObject = ( ExchangeAlgebraObject)objectBase;

		if ( !Constant.is_correct_name( exchangeAlgebraObject._name))
			return false;

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( exchangeAlgebraObject._name))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( exchangeAlgebraObject._name)))
			return false;

		if ( _variableTableBase.other_objectBase_has_this_name( exchangeAlgebraObject._kind, exchangeAlgebraObject._name))
			return false;

		String[] propertyPanelBases = Constant.get_propertyPanelBases( "variable");

		for ( int i = 0; i < propertyPanelBases.length; ++i) {
			PropertyPanelBase propertyPanelBase = _propertyPanelBaseMap.get( propertyPanelBases[ i]);
			if ( null != propertyPanelBase && propertyPanelBase.contains( exchangeAlgebraObject._name))
				return false;
		}

		String[] kinds = Constant.get_kinds( "exchange algebra");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], exchangeAlgebraObject._name, _entityBase))
				return false;
		}

		if ( null != LayerManager.get_instance().get_chart( exchangeAlgebraObject._name))
			return false;

		if ( !exchangeAlgebraObject.can_paste( _propertyPanelBaseMap))
			return false;

		return true;
	}
}
