/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.status;

import java.awt.Component;
import java.awt.Frame;
import java.util.Vector;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import soars.application.visualshell.common.swing.TableBase;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.common.utility.swing.table.base.StandardTableHeaderRenderer;
import soars.common.utility.swing.table.base.StandardTableRowRenderer;
import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 *
 */
public class StatusTable extends TableBase {

	/**
	 * @param owner
	 * @param parent
	 */
	public StatusTable(Frame owner, Component parent) {
		super(owner, parent);
	}

	/**
	 * @return
	 */
	public boolean setup() {
		if ( !super.setup( false))
			return false;

		setAutoResizeMode( AUTO_RESIZE_OFF);
		getTableHeader().setReorderingAllowed( false);
		setDefaultEditor( Object.class, null);

		getTableHeader().setDefaultRenderer( new StandardTableHeaderRenderer());

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 2);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue(
			ResourceManager.get_instance().get( "edit.rule.dialog.status.table.header.name"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue(
			ResourceManager.get_instance().get( "edit.rule.dialog.status.table.header.initial.value"));

		defaultTableColumnModel.getColumn( 0).setPreferredWidth( 100);
		defaultTableColumnModel.getColumn( 1).setPreferredWidth( 2000);

		for ( int i = 0; i < defaultTableColumnModel.getColumnCount(); ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new StandardTableRowRenderer());
		}

		return true;
	}

	/**
	 * @param type
	 * @param name
	 * @param object
	 */
	public void update(String type, String name, String object) {
		clear();

		if ( null == name)
			return;

		EntityBase entityBase = null;
		if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.type.agent")))
			entityBase = LayerManager.get_instance().get_agent( name);
		else if ( type.equals( ResourceManager.get_instance().get( "edit.rule.dialog.type.spot")))
			entityBase = LayerManager.get_instance().get_spot( name);
		else
			return;

		if ( null == entityBase)
			return;

		Vector<String> objectNameVector = new Vector<String>();
		if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.collection.variable")))
			entityBase.get_object_names( "collection", objectNameVector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.list.variable")))
			entityBase.get_object_names( "list", objectNameVector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.map.variable")))
			entityBase.get_object_names( "map", objectNameVector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.keyword")))
			entityBase.get_object_names( "keyword", objectNameVector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.integer.number.variable")))
			entityBase.get_number_object_names( "integer", objectNameVector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.real.number.variable")))
			entityBase.get_number_object_names( "real number", objectNameVector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.role.variable")))
			entityBase.get_object_names( "role variable", objectNameVector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.time.variable")))
			entityBase.get_object_names( "time variable", objectNameVector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.spot.variable")))
			entityBase.get_object_names( "spot variable", objectNameVector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.class.variable")))
			entityBase.get_object_names( "class variable", objectNameVector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.file")))
			entityBase.get_object_names( "file", objectNameVector);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.exchange.algebra")))
			entityBase.get_object_names( "exchange algebra", objectNameVector);
		else
			return;

		if ( objectNameVector.isEmpty())
			return;

		String[] objectNames = Tool.quick_sort_string( objectNameVector, true, false);
		if ( null == objectNames)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();

		Object[] objects = new Object[ 2];
		for ( int i = 0; i < objectNames.length; ++i) {
			objects[ 0] = objectNames[ i];
			objects[ 1] = get_initial_values( entityBase, objectNames[ i], object);
			defaultTableModel.addRow( objects);
		}
	}

	/**
	 * @param entityBase
	 * @param name
	 * @param object
	 * @return
	 */
	private String get_initial_values(EntityBase entityBase, String name, String object) {
		Vector<String> initialValues = new Vector<String>();
		if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.collection.variable")))
			entityBase.get_object_initial_values( "collection", name, initialValues);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.list.variable")))
			entityBase.get_object_initial_values( "list", name, initialValues);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.map.variable")))
			entityBase.get_object_initial_values( "map", name, initialValues);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.keyword")))
			entityBase.get_object_initial_values( "keyword", name, initialValues);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.integer.number.variable")))
			entityBase.get_object_initial_values( "integer", name, initialValues);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.real.number.variable")))
			entityBase.get_object_initial_values( "real number", name, initialValues);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.role.variable")))
			entityBase.get_object_initial_values( "role variable", name, initialValues);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.time.variable")))
			entityBase.get_object_initial_values( "time variable", name, initialValues);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.spot.variable")))
			entityBase.get_object_initial_values( "spot variable", name, initialValues);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.file")))
			entityBase.get_object_initial_values( "file", name, initialValues);
		else if ( object.equals( ResourceManager.get_instance().get( "edit.rule.dialog.object.exchange.algebra")))
			entityBase.get_object_initial_values( "exchange algebra", name, initialValues);
		else
			return "";

		if ( initialValues.isEmpty())
			return "";

		String result = "";
		for ( int i = 0; i < initialValues.size(); ++i) {
			String initialValue = initialValues.get( i);
			result += ( ( ( 0 == i) ? "" : ", ") + initialValue);
		}

		return result;
	}

	/**
	 * 
	 */
	private void clear() {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		while ( 0 < defaultTableModel.getRowCount())
			defaultTableModel.removeRow( 0);
	}
}
