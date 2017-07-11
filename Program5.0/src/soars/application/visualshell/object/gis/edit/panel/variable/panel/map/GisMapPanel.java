/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.variable.panel.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.SystemColor;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;
import soars.application.visualshell.object.gis.edit.panel.base.GisVariableTableBase;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.GisVariablePanelBase;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisRowHeaderTable;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.map.initial.GisMapInitialValueButtonPanel;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.map.initial.GisMapInitialValueKeyPanel;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.map.initial.GisMapInitialValueValuePanel;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.map.table.GisMapInitialValueTable;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.application.visualshell.object.gis.object.map.GisMapInitialValue;
import soars.application.visualshell.object.gis.object.map.GisMapObject;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class GisMapPanel extends GisVariablePanelBase {

	/**
	 * 
	 */
	protected GisMapInitialValueKeyPanel _gisMapInitialValueKeyPanel = null;

	/**
	 * 
	 */
	protected GisMapInitialValueValuePanel _gisMapInitialValueValuePanel = null;

	/**
	 * 
	 */
	protected GisMapInitialValueButtonPanel _gisMapInitialValueButtonPanel = null;

	/**
	 * 
	 */
	protected GisMapInitialValueTable _gisMapInitialValueTable = null;

	/**
	 * 
	 */
	protected GisRowHeaderTable _gisRowHeaderTable = null;

	/**
	 * @param gisDataManager
	 * @param gisPropertyPanelBaseMap
	 * @param gisVariableTableBase
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public GisMapPanel(GisDataManager gisDataManager, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, GisVariableTableBase gisVariableTableBase, Color color, Frame owner, Component parent) {
		super("map", gisDataManager, gisPropertyPanelBaseMap, gisVariableTableBase, color, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#setup_center_panel(javax.swing.JPanel)
	 */
	@Override
	protected boolean setup_center_panel(JPanel parent) {
		_gisMapInitialValueTable = new GisMapInitialValueTable( _color, _gisDataManager, _gisPropertyPanelBaseMap, this, _owner, _parent);
		_gisRowHeaderTable = new GisRowHeaderTable( _color, _gisDataManager, this, _owner, _parent);

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
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#get_name_label_text()
	 */
	@Override
	protected String get_name_label_text() {
		return ResourceManager.get_instance().get( "edit.object.dialog.name");
	}

	/**
	 * @param parent
	 */
	private void setup_mapInitialValuePanels(JPanel parent) {
		_gisMapInitialValueKeyPanel = new GisMapInitialValueKeyPanel( _color, _gisDataManager, _gisPropertyPanelBaseMap);
		_gisMapInitialValueValuePanel = new GisMapInitialValueValuePanel( _color, _gisDataManager, _gisPropertyPanelBaseMap);
		_gisMapInitialValueButtonPanel = new GisMapInitialValueButtonPanel( _gisMapInitialValueKeyPanel, _gisMapInitialValueValuePanel, _gisMapInitialValueTable);

		_gisMapInitialValueKeyPanel.setup();
		_gisMapInitialValueValuePanel.setup();
		_gisMapInitialValueButtonPanel.setup();

		parent.add( _gisMapInitialValueKeyPanel);
		insert_vertical_strut( parent);
		parent.add( _gisMapInitialValueValuePanel);
		insert_vertical_strut( parent);
		parent.add( _gisMapInitialValueButtonPanel);
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

		if ( !_gisMapInitialValueTable.setup( _gisRowHeaderTable))
			return false;

		if ( !_gisRowHeaderTable.setup( _gisMapInitialValueTable, ( Graphics2D)_parent.getGraphics()))
			return false;

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _gisMapInitialValueTable);
		scrollPane.getViewport().setOpaque( true);
		scrollPane.getViewport().setBackground( SystemColor.text);
		scrollPane.setPreferredSize( new Dimension( scrollPane.getPreferredSize().width, 150));

//		JViewport viewport = new JViewport();
//		viewport.setView( _rowHeaderTable);
//		scrollPane.setRowHeader( viewport);
		scrollPane.setRowHeaderView( _gisRowHeaderTable);
		scrollPane.getRowHeader().setOpaque( true);
		scrollPane.getRowHeader().setBackground( SystemColor.text);
		Dimension dimension = scrollPane.getRowHeader().getPreferredSize();
		scrollPane.getRowHeader().setPreferredSize(
			new Dimension( _gisRowHeaderTable.getColumnModel().getColumn( 0).getWidth(),
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

		scrollPane.setPreferredSize( new Dimension( scrollPane.getPreferredSize().width, 225));

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#get_label_width(int)
	 */
	@Override
	public int get_label_width(int width) {
		for ( JComponent label:_labels)
			width = Math.max( width, label.getPreferredSize().width);

		width = _gisMapInitialValueKeyPanel.get_label_width( width);
		width = _gisMapInitialValueValuePanel.get_label_width( width);

		return width;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#adjust(int)
	 */
	@Override
	public int adjust(int width) {
		for ( JComponent label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));

		_gisMapInitialValueKeyPanel.adjust( width);
		_gisMapInitialValueValuePanel.adjust( width);

		return ( _buttons.get( 0).getPreferredSize().width + 5 + _buttons.get( 1).getPreferredSize().width);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#on_setup_completed()
	 */
	@Override
	public void on_setup_completed() {
		_gisMapInitialValueKeyPanel.on_setup_completed();
		_gisMapInitialValueValuePanel.on_setup_completed();
		_gisMapInitialValueButtonPanel.on_setup_completed();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#contains(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	public boolean contains(GisObjectBase gisObjectBase) {
		return _gisMapInitialValueTable.contains( gisObjectBase);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#update_object_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_object_name(String type, String name, String newName) {
		return _gisMapInitialValueTable.update_object_name( type, name, newName);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#update(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	public void update(GisObjectBase gisObjectBase) {
		_nameTextField.setText( ( null != gisObjectBase && gisObjectBase instanceof GisMapObject) ? gisObjectBase._name : "");
		_commentTextField.setText( ( null != gisObjectBase && gisObjectBase instanceof GisMapObject) ? gisObjectBase._comment : "");
		_gisMapInitialValueTable.update( ( null != gisObjectBase && gisObjectBase instanceof GisMapObject) ? ( GisMapObject)gisObjectBase : null);
		clear_textUndoRedoManagers();
		setup_textUndoRedoManagers();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#clear_textUndoRedoManagers()
	 */
	@Override
	protected void clear_textUndoRedoManagers() {
		super.clear_textUndoRedoManagers();
		_gisMapInitialValueKeyPanel.clear_textUndoRedoManagers();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#setup_textUndoRedoManagers()
	 */
	@Override
	protected void setup_textUndoRedoManagers() {
		if ( !_textUndoRedoManagers.isEmpty())
			return;

		_textUndoRedoManagers.add( new TextUndoRedoManager( _nameTextField, this));
		_textUndoRedoManagers.add( new TextUndoRedoManager( _commentTextField, this));

		_gisMapInitialValueKeyPanel.setup_textUndoRedoManagers();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#update()
	 */
	@Override
	public void update() {
		_gisMapInitialValueKeyPanel.update();
		_gisMapInitialValueValuePanel.update();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#changeSelection(java.lang.Object)
	 */
	@Override
	public void changeSelection(Object object) {
		if ( null == object)
			return;

		if ( !( object instanceof GisMapInitialValue))
			return;

		GisMapInitialValue gisMapInitialValue = ( GisMapInitialValue)object;
		_gisMapInitialValueKeyPanel.set( gisMapInitialValue);
		_gisMapInitialValueValuePanel.set( gisMapInitialValue);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#clear()
	 */
	@Override
	public void clear() {
		super.clear();
		_gisMapInitialValueTable.update( ( GisMapObject)null);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#is_empty()
	 */
	@Override
	protected boolean is_empty() {
		return ( super.is_empty() && ( 0 == _gisMapInitialValueTable.getRowCount()));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#create_and_get()
	 */
	@Override
	protected GisObjectBase create_and_get() {
		GisMapObject gisMapObject = new GisMapObject();
		gisMapObject._name = _nameTextField.getText();
		gisMapObject._comment = _commentTextField.getText();
		_gisMapInitialValueTable.get( gisMapObject);
		return gisMapObject;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#can_get_data(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	protected boolean can_get_data(GisObjectBase gisObjectBase) {
		if ( !( gisObjectBase instanceof GisMapObject))
			return false;

		GisMapObject gisMapObject = ( GisMapObject)gisObjectBase;

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

		if ( !_nameTextField.getText().equals( gisMapObject._name)) {
			if ( _gisVariableTableBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.map"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if ( _gisMapInitialValueTable.contains( gisMapObject)) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.initial.values.contain.self.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.map"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		String[] propertyPanelBases = Constant.get_propertyPanelBases( "variable");

		for ( int i = 0; i < propertyPanelBases.length; ++i) {
			GisPropertyPanelBase gisPropertyPanelBase = _gisPropertyPanelBaseMap.get( propertyPanelBases[ i]);
			if ( null != gisPropertyPanelBase && gisPropertyPanelBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.map"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] kinds = Constant.get_kinds( "map");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], _nameTextField.getText(), new SpotObject()/*_entityBase*/)) {
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
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#get_data(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	protected void get_data(GisObjectBase gisObjectBase) {
		if ( !( gisObjectBase instanceof GisMapObject))
			return;

		GisMapObject gisMapObject = ( GisMapObject)gisObjectBase;

		if ( !gisMapObject._name.equals( "") && !_nameTextField.getText().equals( gisMapObject._name))
			_gisPropertyPanelBaseMap.get( "variable").update_object_name( "map", gisMapObject._name, _nameTextField.getText());

		gisMapObject._name = _nameTextField.getText();
		gisMapObject._comment = _commentTextField.getText();

		_gisMapInitialValueTable.get( gisMapObject);
	}

//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#can_paste(soars.application.visualshell.object.gis.object.base.GisObjectBase, java.util.List)
//	 */
//	@Override
//	public boolean can_paste(GisObjectBase gisObjectBase, List<GisObjectBase> gisObjectBases) {
//		if ( !( gisObjectBase instanceof GisMapObject))
//			return false;
//
//		GisMapObject gisMapObject = ( GisMapObject)gisObjectBase;
//
//		if ( !Constant.is_correct_name( gisMapObject._name))
//			return false;
//
//		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( gisMapObject._name))
//			|| ( null != LayerManager.get_instance().get_spot_has_this_name( gisMapObject._name)))
//			return false;
//
//		if ( _gisVariableTableBase.other_objectBase_has_this_name( gisMapObject._kind, gisMapObject._name))
//			return false;
//
//		if ( _gisMapInitialValueTable.contains( gisMapObject))
//			return false;
//
//		String[] propertyPanelBases = Constant.get_propertyPanelBases( "variable");
//
//		for ( int i = 0; i < propertyPanelBases.length; ++i) {
//			GisPropertyPanelBase gisPropertyPanelBase = _gisPropertyPanelBaseMap.get( propertyPanelBases[ i]);
//			if ( null != gisPropertyPanelBase && gisPropertyPanelBase.contains( gisMapObject._name))
//				return false;
//		}
//
//		String[] kinds = Constant.get_kinds( "map");
//
//		for ( int i = 0; i < kinds.length; ++i) {
//			if ( LayerManager.get_instance().is_object_name( kinds[ i], gisMapObject._name/*, _entityBase*/))
//				return false;
//		}
//
//		if ( null != LayerManager.get_instance().get_chart( gisMapObject._name))
//			return false;
//
//		if ( !gisMapObject.can_paste( _gisPropertyPanelBaseMap, gisObjectBase, gisObjectBases/*, _entityBase*/))
//			return false;
//
//		return true;
//	}
}
