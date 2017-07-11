/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.SystemColor;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
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
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.map.initial.MapInitialValueButtonPanel;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.map.initial.MapInitialValueKeyPanel;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.map.initial.MapInitialValueValuePanel;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.map.table.MapInitialValueTable;
import soars.application.visualshell.object.entity.base.object.base.ObjectBase;
import soars.application.visualshell.object.entity.base.object.map.MapInitialValue;
import soars.application.visualshell.object.entity.base.object.map.MapObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.observer.Observer;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class MapPanel extends VariablePanelBase {

	/**
	 * 
	 */
	protected MapInitialValueKeyPanel _mapInitialValueKeyPanel = null;

	/**
	 * 
	 */
	protected MapInitialValueValuePanel _mapInitialValueValuePanel = null;

	/**
	 * 
	 */
	protected MapInitialValueButtonPanel _mapInitialValueButtonPanel = null;

	/**
	 * 
	 */
	protected MapInitialValueTable _mapInitialValueTable = null;

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
	public MapPanel(EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, VariableTableBase variableTableBase, Color color, Frame owner, Component parent) {
		super("map", entityBase, propertyPanelBaseMap, variableTableBase, color, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.variable.panel.base.panel.VariablePanelBase#setup_center_panel(javax.swing.JPanel)
	 */
	protected boolean setup_center_panel(JPanel parent) {
		_mapInitialValueTable = new MapInitialValueTable( _color, _entityBase, _propertyPanelBaseMap, this, _owner, _parent);
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
		setup_mapInitialValuePanels( northPanel);
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
	private void setup_mapInitialValuePanels(JPanel parent) {
		_mapInitialValueKeyPanel = new MapInitialValueKeyPanel( _color, _entityBase, _propertyPanelBaseMap);
		_mapInitialValueValuePanel = new MapInitialValueValuePanel( _color, _entityBase, _propertyPanelBaseMap);
		_mapInitialValueButtonPanel = new MapInitialValueButtonPanel( _mapInitialValueKeyPanel, _mapInitialValueValuePanel, _mapInitialValueTable);

		_mapInitialValueKeyPanel.setup();
		_mapInitialValueValuePanel.setup();
		_mapInitialValueButtonPanel.setup();

		parent.add( _mapInitialValueKeyPanel);
		insert_vertical_strut( parent);
		parent.add( _mapInitialValueValuePanel);
		insert_vertical_strut( parent);
		parent.add( _mapInitialValueButtonPanel);
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

		if ( !_mapInitialValueTable.setup( _rowHeaderTable))
			return false;

		if ( !_rowHeaderTable.setup( _mapInitialValueTable, ( Graphics2D)_parent.getGraphics()))
			return false;

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _mapInitialValueTable);
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

		width = _mapInitialValueKeyPanel.get_label_width( width);
		width = _mapInitialValueValuePanel.get_label_width( width);

		return width;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.variable.panel.base.panel.VariablePanelBase#adjust(int)
	 */
	public int adjust(int width) {
		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));

		_mapInitialValueKeyPanel.adjust( width);
		_mapInitialValueValuePanel.adjust( width);

		return ( _buttons.get( 0).getPreferredSize().width + 5 + _buttons.get( 1).getPreferredSize().width);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.variable.panel.base.panel.VariablePanelBase#on_setup_completed()
	 */
	public void on_setup_completed() {
		_mapInitialValueKeyPanel.on_setup_completed();
		_mapInitialValueValuePanel.on_setup_completed();
		_mapInitialValueButtonPanel.on_setup_completed();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.base.PanelBase#contains(soars.application.visualshell.object.entiy.base.object.base.ObjectBase)
	 */
	public boolean contains(ObjectBase objectBase) {
		return _mapInitialValueTable.contains( objectBase);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.variable.panel.base.panel.VariablePanelBase#update_object_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean update_object_name(String type, String name, String newName) {
		return _mapInitialValueTable.update_object_name( type, name, newName);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.variable.panel.base.panel.VariablePanelBase#update(soars.application.visualshell.object.entiy.base.object.base.ObjectBase)
	 */
	public void update(ObjectBase objectBase) {
		_nameTextField.setText( ( null != objectBase && objectBase instanceof MapObject) ? objectBase._name : "");
		_commentTextField.setText( ( null != objectBase && objectBase instanceof MapObject) ? objectBase._comment : "");
		_mapInitialValueTable.update( ( null != objectBase && objectBase instanceof MapObject) ? ( MapObject)objectBase : null);
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
		_mapInitialValueKeyPanel.clear_textUndoRedoManagers();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.base.PanelBase#setup_textUndoRedoManagers()
	 */
	protected void setup_textUndoRedoManagers() {
		// TODO Auto-generated method stub
		if ( !_textUndoRedoManagers.isEmpty())
			return;

		_textUndoRedoManagers.add( new TextUndoRedoManager( _nameTextField, this));
		_textUndoRedoManagers.add( new TextUndoRedoManager( _commentTextField, this));

		_mapInitialValueKeyPanel.setup_textUndoRedoManagers();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.variable.panel.base.panel.VariablePanelBase#update()
	 */
	public void update() {
		_mapInitialValueKeyPanel.update();
		_mapInitialValueValuePanel.update();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.variable.panel.base.panel.VariablePanelBase#changeSelection(java.lang.Object)
	 */
	public void changeSelection(Object object) {
		if ( null == object)
			return;

		if ( !( object instanceof MapInitialValue))
			return;

		MapInitialValue mapInitialValue = ( MapInitialValue)object;
		_mapInitialValueKeyPanel.set( mapInitialValue);
		_mapInitialValueValuePanel.set( mapInitialValue);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.variable.panel.base.panel.VariablePanelBase#clear()
	 */
	public void clear() {
		super.clear();
		_mapInitialValueTable.update( ( MapObject)null);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.base.PanelBase#is_empty()
	 */
	protected boolean is_empty() {
		return ( super.is_empty() && ( 0 == _mapInitialValueTable.getRowCount()));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.base.PanelBase#create_and_get()
	 */
	protected ObjectBase create_and_get() {
		MapObject mapObject = new MapObject();
		mapObject._name = _nameTextField.getText();
		mapObject._comment = _commentTextField.getText();
		_mapInitialValueTable.get( mapObject);
		return mapObject;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.base.PanelBase#can_get_data(soars.application.visualshell.object.entiy.base.object.base.ObjectBase)
	 */
	protected boolean can_get_data(ObjectBase objectBase) {
		if ( !( objectBase instanceof MapObject))
			return false;

		MapObject mapObject = ( MapObject)objectBase;

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

		if ( !_nameTextField.getText().equals( mapObject._name)) {
			if ( _variableTableBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.map"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if ( _mapInitialValueTable.contains( mapObject)) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.initial.values.contain.self.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.map"),
				JOptionPane.ERROR_MESSAGE);
			return false;
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

		String[] kinds = Constant.get_kinds( "map");

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
		if ( !( objectBase instanceof MapObject))
			return;

		MapObject mapObject = ( MapObject)objectBase;

		if ( !mapObject._name.equals( "") && !_nameTextField.getText().equals( mapObject._name)) {
			_propertyPanelBaseMap.get( "variable").update_object_name( "map", mapObject._name, _nameTextField.getText());

			WarningManager.get_instance().cleanup();

			boolean result = LayerManager.get_instance().update_object_name( "map", mapObject._name, _nameTextField.getText(), _entityBase);
			if ( result) {
				if ( _entityBase.update_object_name( "map", mapObject._name, _nameTextField.getText())) {
					String[] message = new String[] {
						( ( _entityBase instanceof AgentObject) ? "Agent : " : "Spot : ") + _entityBase._name
					};

					WarningManager.get_instance().add( message);
				}

				if ( _entityBase instanceof AgentObject)
					Observer.get_instance().on_update_agent_object( "map", mapObject._name, _nameTextField.getText());
				else if ( _entityBase instanceof SpotObject)
					Observer.get_instance().on_update_spot_object( "map", mapObject._name, _nameTextField.getText());

				Observer.get_instance().on_update_entityBase( true);
				Observer.get_instance().modified();
			}
		}

		mapObject._name = _nameTextField.getText();
		mapObject._comment = _commentTextField.getText();

		_mapInitialValueTable.get( mapObject);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.edit.tab.base.PanelBase#can_paste(soars.application.visualshell.object.entiy.base.object.base.ObjectBase, java.util.List)
	 */
	public boolean can_paste(ObjectBase objectBase, List<ObjectBase> objectBases) {
		if ( !( objectBase instanceof MapObject))
			return false;

		MapObject mapObject = ( MapObject)objectBase;

		if ( !Constant.is_correct_name( mapObject._name))
			return false;

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( mapObject._name))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( mapObject._name)))
			return false;

		if ( _variableTableBase.other_objectBase_has_this_name( mapObject._kind, mapObject._name))
			return false;

		if ( _mapInitialValueTable.contains( mapObject))
			return false;

		String[] propertyPanelBases = Constant.get_propertyPanelBases( "variable");

		for ( int i = 0; i < propertyPanelBases.length; ++i) {
			PropertyPanelBase propertyPanelBase = _propertyPanelBaseMap.get( propertyPanelBases[ i]);
			if ( null != propertyPanelBase && propertyPanelBase.contains( mapObject._name))
				return false;
		}

		String[] kinds = Constant.get_kinds( "map");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], mapObject._name, _entityBase))
				return false;
		}

		if ( null != LayerManager.get_instance().get_chart( mapObject._name))
			return false;

		if ( !mapObject.can_paste( _propertyPanelBaseMap, objectBase, objectBases, _entityBase))
			return false;

		return true;
	}
}
