/*
 * 2005/05/27
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable;

import java.awt.Component;
import java.awt.Frame;
import java.util.Map;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.VariableTableBase;
import soars.application.visualshell.object.entity.base.object.base.ObjectBase;
import soars.application.visualshell.object.entity.base.object.exchange_algebra.ExchangeAlgebraObject;
import soars.application.visualshell.object.entity.base.object.map.MapObject;
import soars.application.visualshell.object.entity.base.object.variable.VariableObject;
import soars.common.utility.swing.table.base.StandardTableHeaderRenderer;
import soars.common.utility.tool.sort.StringNumberComparator;

/**
 * @author kurata
 */
public class VariableTable extends VariableTableBase {

	/**
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 * @param propertyPanelBase
	 * @param owner
	 * @param parent
	 */
	public VariableTable(EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, PropertyPanelBase propertyPanelBase, Frame owner, Component parent) {
		super("variable", entityBase, propertyPanelBaseMap, propertyPanelBase, 4, owner, parent);
	}

	/**
	 * 
	 */
	public void select_at_first() {
		if ( 0 < getRowCount()) {
			setRowSelectionInterval( 0, 0);
			_propertyPanelBase.changeSelection( ( ObjectBase)getValueAt( 0, 0));
		}
	}

	/**
	 * @return
	 */
	public boolean setup() {
		if ( !super.setup(!_entityBase.is_multi()))
			return false;


		setAutoResizeMode( AUTO_RESIZE_OFF);


		JTableHeader tableHeader = getTableHeader();
		StandardTableHeaderRenderer standardTableHeaderRenderer = new StandardTableHeaderRenderer();
		if ( _entityBase.is_multi())
			standardTableHeaderRenderer.setEnabled( false);

		tableHeader.setDefaultRenderer( standardTableHeaderRenderer);

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( _columns);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.variable.table.header.kind"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.variable.table.header.name"));
		defaultTableColumnModel.getColumn( 2).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.variable.table.header.initial.value"));
		defaultTableColumnModel.getColumn( 3).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.variable.table.header.comment"));

		defaultTableColumnModel.getColumn( 0).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 1).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 2).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 3).setPreferredWidth( 2000);

		for ( int i = 0; i < _columns; ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new VariableTableRowRenderer());
		}


		if ( !_entityBase.is_multi()) {
			if ( !setup( _entityBase._objectMapMap.get( "collection"), defaultTableModel))
				return false;
			if ( !setup( _entityBase._objectMapMap.get( "list"), defaultTableModel))
				return false;
			if ( !setup( _entityBase._objectMapMap.get( "map"), defaultTableModel))
				return false;
			if ( Environment.get_instance().is_exchange_algebra_enable()
				&& !setup( _entityBase._objectMapMap.get( "exchange algebra"), defaultTableModel))
				return false;
		}

		setup_popup_menu();

		return true;
	}

	/**
	 * @param objectBase0
	 * @param objectBase1
	 * @return
	 */
	protected int compare(ObjectBase objectBase0, ObjectBase objectBase1) {
		if ( ( objectBase0 instanceof VariableObject && ( ( VariableObject)objectBase0)._kind.equals( "collection")
				&& objectBase1 instanceof VariableObject && ( ( VariableObject)objectBase1)._kind.equals( "list"))
			|| ( objectBase0 instanceof VariableObject && objectBase1 instanceof MapObject)
			|| ( objectBase0 instanceof VariableObject && objectBase1 instanceof ExchangeAlgebraObject)
			|| ( objectBase0 instanceof MapObject && objectBase1 instanceof ExchangeAlgebraObject))
			return -1;
		else if ( ( objectBase0 instanceof VariableObject && ( ( VariableObject)objectBase0)._kind.equals( "list")
				&& objectBase1 instanceof VariableObject && ( ( VariableObject)objectBase1)._kind.equals( "collection"))
			|| ( objectBase0 instanceof MapObject && objectBase1 instanceof VariableObject)
			|| ( objectBase0 instanceof ExchangeAlgebraObject && objectBase1 instanceof VariableObject)
			|| ( objectBase0 instanceof ExchangeAlgebraObject && objectBase1 instanceof MapObject))
			return 1;
		else
			return StringNumberComparator.compareTo( objectBase0._name, objectBase1._name);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.VariableTableBase#can_remove_selected_objects(soars.application.visualshell.object.entity.base.object.base.ObjectBase)
	 */
	@Override
	protected boolean can_remove_selected_objects(ObjectBase objectBase) {
		if ( contains( objectBase))
			return false;

		return super.can_remove_selected_objects( objectBase);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.VariableTableBase#on_remove()
	 */
	@Override
	protected void on_remove() {
		_propertyPanelBase.update();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.VariableTableBase#on_ok()
	 */
	@Override
	public void on_ok() {
		_entityBase._objectMapMap.get( "collection").clear();
		_entityBase._objectMapMap.get( "list").clear();
		_entityBase._objectMapMap.get( "map").clear();
		_entityBase._objectMapMap.get( "exchange algebra").clear();

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			ObjectBase objectBase = ( ObjectBase)defaultTableModel.getValueAt( i, 0);
			if ( null == objectBase)
				continue;

			if ( objectBase instanceof VariableObject && objectBase._kind.equals( "collection"))
				_entityBase._objectMapMap.get( "collection").put( objectBase._name, objectBase);
			else if ( objectBase instanceof VariableObject && objectBase._kind.equals( "list"))
				_entityBase._objectMapMap.get( "list").put( objectBase._name, objectBase);
			else if ( objectBase instanceof MapObject)
				_entityBase._objectMapMap.get( "map").put( objectBase._name, objectBase);
			else if ( objectBase instanceof ExchangeAlgebraObject)
				_entityBase._objectMapMap.get( "exchange algebra").put( objectBase._name, objectBase);
		}
	}
}
