/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.set_and_list.base.panel.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.VariablePanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTable;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase;
import soars.application.visualshell.object.entity.base.object.base.InitialValueBase;
import soars.application.visualshell.object.entity.base.object.base.ObjectBase;
import soars.application.visualshell.object.entity.base.object.variable.VariableInitialValue;
import soars.application.visualshell.object.entity.base.object.variable.VariableObject;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 *
 */
public class VariableInitialValueTable extends InitialValueTable {

	/**
	 * @param color
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 * @param variablePanelBase
	 * @param owner
	 * @param parent
	 */
	public VariableInitialValueTable(Color color, EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, VariablePanelBase variablePanelBase, Frame owner, Component parent) {
		super("variable", color, entityBase, propertyPanelBaseMap, variablePanelBase, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#setup(soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase)
	 */
	@Override
	public boolean setup(InitialValueTableBase initialValueTableBase) {
		if ( !super.setup(initialValueTableBase))
			return false;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 2);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue( ResourceManager.get_instance().get( "edit.object.dialog.variable.initial.value.table.header.kind"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue( ResourceManager.get_instance().get( "edit.object.dialog.variable.initial.value.table.header.initial.value"));

		defaultTableColumnModel.getColumn( 0).setPreferredWidth( 100);
		defaultTableColumnModel.getColumn( 1).setPreferredWidth( 2000);

		for ( int i = 0; i < 2; ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new VariableInitialValueTableRowRenderer( _color));
		}

		return true;
	}

	/**
	 * @param variableObject
	 */
	public void update(VariableObject variableObject) {
		cleanup();
		_initialValueTableBase.cleanup();
		if ( null == variableObject)
			return;

		for ( VariableInitialValue viv:variableObject._variableInitialValues) {
			VariableInitialValue variableInitialValue = new VariableInitialValue( viv);
			append( new VariableInitialValue[] { variableInitialValue, variableInitialValue});
		}

		if ( 0 < getRowCount()) {
			select( 0);
			_variablePanelBase.changeSelection( getValueAt( 0, 0));
			scroll( 0);
		}
	}

	/**
	 * @param variableObject
	 */
	public void get(VariableObject variableObject) {
		variableObject._variableInitialValues.clear();
		for ( int i = 0; i < getRowCount(); ++i)
			variableObject._variableInitialValues.add( ( VariableInitialValue)getValueAt( i, 0));
	}

	/**
	 * @param objectBase
	 * @return
	 */
	public boolean contains(ObjectBase objectBase) {
		for ( int i = 0; i < getRowCount(); ++i) {
			VariableInitialValue variableInitialValue = ( VariableInitialValue)getValueAt( i, 0);
			if ( variableInitialValue.contains( objectBase))
				return true;
		}
		return false;
	}

	/**
	 * @param type
	 * @param name
	 * @param newName
	 * @return
	 */
	public boolean update_object_name(String type, String name, String newName) {
		boolean result = false;
		for ( int i = 0; i < getRowCount(); ++i) {
			VariableInitialValue variableInitialValue = ( VariableInitialValue)getValueAt( i, 0);
			if ( variableInitialValue.update_object_name( type, name, newName))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#paste(int[])
	 */
	@Override
	public void paste(int[] rows) {
		if ( __initialValueBasesMap.get( _kind).isEmpty())
			return;

		if ( 0 < getRowCount() && 1 != rows.length)
			return;

		WarningManager.get_instance().cleanup();

		List<VariableInitialValue> variableInitialValues = new ArrayList<VariableInitialValue>();
		if ( 0 == getRowCount()) {
			for ( int i = 0; i < __initialValueBasesMap.get( _kind).size(); ++i) {
				VariableInitialValue variableInitialValue = ( VariableInitialValue)InitialValueBase.create( __initialValueBasesMap.get( _kind).get( i));
				if ( !variableInitialValue.can_paste( _propertyPanelBaseMap, null, null, _entityBase)) {
					String[] message = new String[] {
						PropertyPanelBase._nameMap.get( variableInitialValue._type),
						"name = " + variableInitialValue._value
					};

					WarningManager.get_instance().add( message);
					continue;
				}

				append( new VariableInitialValue[] { variableInitialValue, variableInitialValue});
				variableInitialValues.add( variableInitialValue);
			}
		} else {
			// 自分自身のを含む場合はとりあえず放置しておく
			for ( int i = 0; i < __initialValueBasesMap.get( _kind).size(); ++i) {
				VariableInitialValue variableInitialValue = ( VariableInitialValue)InitialValueBase.create( __initialValueBasesMap.get( _kind).get( i));
				if ( !variableInitialValue.can_paste( _propertyPanelBaseMap, null, null, _entityBase)) {
					String[] message = new String[] {
						PropertyPanelBase._nameMap.get( variableInitialValue._type),
						"name = " + variableInitialValue._value
					};

					WarningManager.get_instance().add( message);
					continue;
				}

				int index = ( rows[ 0] + __initialValueBasesMap.get( _kind).get( i)._row);
				if ( getRowCount() > index)
					insert( new VariableInitialValue[] { variableInitialValue, variableInitialValue}, index);
				else
					append( new VariableInitialValue[] { variableInitialValue, variableInitialValue});
				variableInitialValues.add( variableInitialValue);
			}
		}

		List<Integer> list = new ArrayList<Integer>();
		for ( VariableInitialValue variableInitialValue:variableInitialValues) {
			for ( int i = 0; i < getRowCount(); ++i) {
				if ( variableInitialValue == getValueAt( i, 0)) {
					list.add( i);
					break;
				}
			}
		}
		_initialValueTableBase.select( Tool.get_array( list));
		changeSelection();

		if ( !WarningManager.get_instance().isEmpty()) {
			WarningDlg1 warningDlg1 = new WarningDlg1(
				_owner,
				ResourceManager.get_instance().get( "warning.dialog1.title"),
				ResourceManager.get_instance().get( "warning.dialog1.message6"),
				getParent());
			warningDlg1.do_modal();
		}
	}
}
