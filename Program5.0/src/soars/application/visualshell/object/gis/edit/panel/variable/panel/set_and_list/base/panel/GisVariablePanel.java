/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel;

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
import javax.swing.JComponent;
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
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;
import soars.application.visualshell.object.gis.edit.panel.base.GisVariableTableBase;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.GisVariablePanelBase;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisRowHeaderTable;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.GisVariableInitialValueAgentPanel;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.GisVariableInitialValueFieldPanel;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.GisVariableInitialValueObjectPanel;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.GisVariableInitialValueSpotPanel;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.initial.base.GisVariableInitialValuePanel;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.set_and_list.base.panel.table.GisVariableInitialValueTable;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.application.visualshell.object.gis.object.variable.GisVariableInitialValue;
import soars.application.visualshell.object.gis.object.variable.GisVariableObject;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class GisVariablePanel extends GisVariablePanelBase {

	/**
	 * 
	 */
	protected ComboBox _typeComboBox = null;

	/**
	 * 
	 */
	protected List<GisVariableInitialValuePanel> _gisVariableInitialValuePanels = new ArrayList<GisVariableInitialValuePanel>();

	/**
	 * 
	 */
	protected GisVariableInitialValueTable _gisVariableInitialValueTable = null;

	/**
	 * 
	 */
	protected GisRowHeaderTable _gisRowHeaderTable = null;

	/**
	 * @param kind
	 * @param entityBase
	 * @param gisPropertyPanelBaseMap
	 * @param gisVariableTableBase 
	 * @param color 
	 * @param owner
	 * @param parent
	 */
	public GisVariablePanel(String kind, GisDataManager gisDataManager, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, GisVariableTableBase gisVariableTableBase, Color color, Frame owner, Component parent) {
		super(kind, gisDataManager, gisPropertyPanelBaseMap, gisVariableTableBase, color, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#setup_center_panel(javax.swing.JPanel)
	 */
	@Override
	protected boolean setup_center_panel(JPanel parent) {
		_gisVariableInitialValueTable = new GisVariableInitialValueTable( _color, _gisDataManager, _gisPropertyPanelBaseMap, this, _owner, _parent);
		_gisRowHeaderTable = new GisRowHeaderTable(  _color, _gisDataManager, this, _owner, _parent);

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
		setup_type_comboBox( northPanel);
		insert_vertical_strut( northPanel);
		setup_variableInitialValuePanels( northPanel);
		insert_vertical_strut( northPanel);
		setup_comment_textField( northPanel);

		panel.add( northPanel, "North");

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_type_comboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.variable.dialog.type"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		Vector<String> items = new Vector<String>();
//		items.add( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.agent"));
//		items.add( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.spot"));
		//items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.probability"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"));
//		if ( _entityBase instanceof AgentObject) {
//			items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable"));
//		}
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.collection"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.list"));
		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.map"));
//		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"));
//		items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.file"));
//		if ( Environment.get_instance().is_exchange_algebra_enable()) {
//			items.add( ResourceManager.get_instance().get( "edit.object.dialog.tree.exchange.algebra"));
//		}
		items.add( ResourceManager.get_instance().get( "edit.gis.data.initial.value.field"));
		_typeComboBox = new ComboBox( items.toArray( new String[ 0]), _color, false, new CommonComboBoxRenderer( _color, false));
		_typeComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String item = ( String)_typeComboBox.getSelectedItem();
				_gisVariableInitialValuePanels.get( 0).setVisible( item.equals( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.agent")));
				_gisVariableInitialValuePanels.get( 1).setVisible( item.equals( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.spot")));
				_gisVariableInitialValuePanels.get( 2).setVisible( item.equals( ResourceManager.get_instance().get( "edit.gis.data.initial.value.field")));
				boolean flag = ( !item.equals( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.agent"))
					&& !item.equals( ResourceManager.get_instance().get( "edit.collection.value.dialog.label.spot"))
					&& !item.equals( ResourceManager.get_instance().get( "edit.gis.data.initial.value.field")));
				_gisVariableInitialValuePanels.get( 3).setVisible( flag);
				if ( flag)
					_gisVariableInitialValuePanels.get( 3).update( item);
				revalidate();
				repaint();
			}
		});
		_components.add( _typeComboBox);
		panel.add( _typeComboBox);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_variableInitialValuePanels(JPanel parent) {
		_gisVariableInitialValuePanels.add( new GisVariableInitialValueAgentPanel( _color, _gisDataManager, _gisPropertyPanelBaseMap, _typeComboBox, _gisVariableInitialValueTable));
		_gisVariableInitialValuePanels.add( new GisVariableInitialValueSpotPanel( _color, _gisDataManager, _gisPropertyPanelBaseMap, _typeComboBox, _gisVariableInitialValueTable));
		_gisVariableInitialValuePanels.add( new GisVariableInitialValueFieldPanel( _color, _gisDataManager, _gisPropertyPanelBaseMap, _typeComboBox, _gisVariableInitialValueTable));
		_gisVariableInitialValuePanels.add( new GisVariableInitialValueObjectPanel( _color, _gisDataManager, _gisPropertyPanelBaseMap, _typeComboBox, _gisVariableInitialValueTable));
		for ( GisVariableInitialValuePanel gisVariableInitialValuePanel:_gisVariableInitialValuePanels) {
			gisVariableInitialValuePanel.setup();
			_components.add( gisVariableInitialValuePanel);
			parent.add( gisVariableInitialValuePanel);
		}
	}

	/**
	 * @return
	 * @param parent
	 */
	private boolean setup_right_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_vertical_strut( northPanel);

		if ( !_gisVariableInitialValueTable.setup( _gisRowHeaderTable))
			return false;

		if ( !_gisRowHeaderTable.setup( _gisVariableInitialValueTable, ( Graphics2D)_parent.getGraphics()))
			return false;

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _gisVariableInitialValueTable);
		scrollPane.getViewport().setOpaque( true);
		scrollPane.getViewport().setBackground( SystemColor.text);

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

		for ( GisVariableInitialValuePanel gisVariableInitialValuePanel:_gisVariableInitialValuePanels)
			width = gisVariableInitialValuePanel.get_label_width( width);

		return width;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#adjust(int)
	 */
	@Override
	public int adjust(int width) {
		for ( JComponent label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));

		for ( GisVariableInitialValuePanel gisVariableInitialValuePanel:_gisVariableInitialValuePanels)
			gisVariableInitialValuePanel.adjust( width);

		return ( _buttons.get( 0).getPreferredSize().width + 5 + _buttons.get( 1).getPreferredSize().width);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#on_setup_completed()
	 */
	@Override
	public void on_setup_completed() {
		_gisVariableInitialValuePanels.get( 0).setVisible( false);
		_gisVariableInitialValuePanels.get( 1).setVisible( false);
		_gisVariableInitialValuePanels.get( 2).setVisible( false);
		//_gisVariableInitialValuePanels.get( 3).setVisible( false);
		String item = ( String)_typeComboBox.getSelectedItem();
		_gisVariableInitialValuePanels.get( 3).update( item);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#contains(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	public boolean contains(GisObjectBase gisObjectBase) {
		return _gisVariableInitialValueTable.contains( gisObjectBase);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#update_object_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_object_name(String type, String name, String newName) {
		return _gisVariableInitialValueTable.update_object_name( type, name, newName);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#update(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	public void update(GisObjectBase gisObjectBase) {
		_nameTextField.setText( ( null != gisObjectBase && gisObjectBase instanceof GisVariableObject) ? gisObjectBase._name : "");
		_commentTextField.setText( ( null != gisObjectBase && gisObjectBase instanceof GisVariableObject) ? gisObjectBase._comment : "");
		_gisVariableInitialValueTable.update( ( null != gisObjectBase && gisObjectBase instanceof GisVariableObject) ? ( GisVariableObject)gisObjectBase : null);
		_textUndoRedoManagers.clear();
		setup_textUndoRedoManagers();
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
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#update()
	 */
	@Override
	public void update() {
		String item = ( String)_typeComboBox.getSelectedItem();
		_gisVariableInitialValuePanels.get( 2).update( item);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#changeSelection(java.lang.Object)
	 */
	@Override
	public void changeSelection(Object object) {
		if ( null == object)
			return;

		if ( !( object instanceof GisVariableInitialValue))
			return;

		GisVariableInitialValue gisVariableInitialValue = ( GisVariableInitialValue)object;
		_typeComboBox.setSelectedItem(
			gisVariableInitialValue._type.equals( "field")
				? ResourceManager.get_instance().get( "edit.gis.data.initial.value.field")
				: GisInitialValueTableBase.get_nameMap().get( gisVariableInitialValue._type));

		if ( gisVariableInitialValue._type.equals( "agent"))
			_gisVariableInitialValuePanels.get( 0).set( gisVariableInitialValue);
		else if ( gisVariableInitialValue._type.equals( "spot"))
			_gisVariableInitialValuePanels.get( 1).set( gisVariableInitialValue);
		else if ( gisVariableInitialValue._type.equals( "field"))
			_gisVariableInitialValuePanels.get( 2).set( gisVariableInitialValue);
		else
			_gisVariableInitialValuePanels.get( 3).set( gisVariableInitialValue);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#clear()
	 */
	@Override
	public void clear() {
		super.clear();
		_gisVariableInitialValueTable.update( ( GisVariableObject)null);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#is_empty()
	 */
	@Override
	protected boolean is_empty() {
		return ( super.is_empty() && ( 0 == _gisVariableInitialValueTable.getRowCount()));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#create_and_get()
	 */
	@Override
	protected GisObjectBase create_and_get() {
		GisVariableObject gisVariableObject = new GisVariableObject( _kind);
		gisVariableObject._name = _nameTextField.getText();
		gisVariableObject._comment = _commentTextField.getText();
		_gisVariableInitialValueTable.get( gisVariableObject);
		return gisVariableObject;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#can_get_data(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	protected boolean can_get_data(GisObjectBase gisObjectBase) {
		if ( !( gisObjectBase instanceof GisVariableObject))
			return false;

		GisVariableObject gisVariableObject = ( GisVariableObject)gisObjectBase;

		if ( !gisVariableObject._kind.equals( _kind))
			// これは起こり得ない筈だが念の為
			return false;

		if ( !Constant.is_correct_name( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( _nameTextField.getText()))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( _nameTextField.getText()))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( !_nameTextField.getText().equals( gisVariableObject._name)) {
			if ( _gisVariableTableBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if ( _gisVariableInitialValueTable.contains( gisVariableObject)) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.initial.values.contain.self.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		String[] propertyPanelBases = Constant.get_propertyPanelBases( "variable");

		for ( int i = 0; i < propertyPanelBases.length; ++i) {
			GisPropertyPanelBase gisPropertyPanelBase = _gisPropertyPanelBaseMap.get( propertyPanelBases[ i]);
			if ( null != gisPropertyPanelBase && gisPropertyPanelBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] kinds = Constant.get_kinds( _kind);

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], _nameTextField.getText(), new SpotObject()/*_entityBase*/)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if ( null != LayerManager.get_instance().get_chart( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree." + _kind),
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
		if ( !( gisObjectBase instanceof GisVariableObject))
			return;

		GisVariableObject gisVariableObject = ( GisVariableObject)gisObjectBase;

		if ( !gisVariableObject._name.equals( "") && !_nameTextField.getText().equals( gisVariableObject._name))
			_gisPropertyPanelBaseMap.get( "variable").update_object_name( _kind, gisVariableObject._name, _nameTextField.getText());

		gisVariableObject._name = _nameTextField.getText();
		gisVariableObject._comment = _commentTextField.getText();

		_gisVariableInitialValueTable.get( gisVariableObject);
	}

//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#can_paste(soars.application.visualshell.object.gis.object.base.GisObjectBase, java.util.List)
//	 */
//	@Override
//	public boolean can_paste(GisObjectBase gisObjectBase, List<GisObjectBase> gisObjectBases) {
//		if ( !( gisObjectBase instanceof GisVariableObject))
//			return false;
//
//		GisVariableObject gisVariableObject = ( GisVariableObject)gisObjectBase;
//
//		if ( !gisVariableObject._kind.equals( _kind))
//			// これは起こり得ない筈だが念の為
//			return false;
//
//		if ( !Constant.is_correct_name( gisVariableObject._name))
//			return false;
//
//		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( gisVariableObject._name))
//			|| ( null != LayerManager.get_instance().get_spot_has_this_name( gisVariableObject._name)))
//			return false;
//
//		if ( _gisVariableTableBase.other_objectBase_has_this_name( gisVariableObject._kind, gisVariableObject._name))
//			return false;
//
//
//		if ( _gisVariableInitialValueTable.contains( gisVariableObject))
//			return false;
//
//		String[] propertyPanelBases = Constant.get_propertyPanelBases( "variable");
//
//		for ( int i = 0; i < propertyPanelBases.length; ++i) {
//			GisPropertyPanelBase gisPropertyPanelBase = _gisPropertyPanelBaseMap.get( propertyPanelBases[ i]);
//			if ( null != gisPropertyPanelBase && gisPropertyPanelBase.contains( gisVariableObject._name))
//				return false;
//		}
//
//		String[] kinds = Constant.get_kinds( _kind);
//
//		for ( int i = 0; i < kinds.length; ++i) {
//			if ( LayerManager.get_instance().is_object_name( kinds[ i], gisVariableObject._name/*, _entityBase*/))
//				return false;
//		}
//
//		if ( null != LayerManager.get_instance().get_chart( gisVariableObject._name))
//			return false;
//
//		if ( !gisVariableObject.can_paste( _gisPropertyPanelBaseMap, gisObjectBase, gisObjectBases/*, _entityBase*/))
//			return false;
//
//		return true;
//	}
}
