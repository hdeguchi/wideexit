/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.variable.panel.map.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import soars.application.visualshell.main.MainFrame;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.GisVariablePanelBase;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTable;
import soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase;
import soars.application.visualshell.object.gis.object.base.GisInitialValueBase;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.application.visualshell.object.gis.object.map.GisMapInitialValue;
import soars.application.visualshell.object.gis.object.map.GisMapObject;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.sort.StringNumberComparator;

/**
 * @author kurata
 *
 */
public class GisMapInitialValueTable extends GisInitialValueTable {

	/**
	 * @param color
	 * @param gisDataManager
	 * @param gisPropertyPanelBaseMap
	 * @param gisVariablePanelBase
	 * @param owner
	 * @param parent
	 */
	public GisMapInitialValueTable(Color color, GisDataManager gisDataManager, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, GisVariablePanelBase gisVariablePanelBase, Frame owner, Component parent) {
		super("map", color, gisDataManager, gisPropertyPanelBaseMap, gisVariablePanelBase, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#setup(soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase)
	 */
	@Override
	public boolean setup(GisInitialValueTableBase gisInitialValueTableBase) {
		if ( !super.setup(gisInitialValueTableBase))
			return false;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 3);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue( ResourceManager.get_instance().get( "edit.object.dialog.map.initial.value.table.header.key"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue( ResourceManager.get_instance().get( "edit.object.dialog.map.initial.value.table.header.initial.value.type"));
		defaultTableColumnModel.getColumn( 2).setHeaderValue( ResourceManager.get_instance().get( "edit.object.dialog.map.initial.value.table.header.initial.value"));

		defaultTableColumnModel.getColumn( 0).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 1).setPreferredWidth( 130);
		defaultTableColumnModel.getColumn( 2).setPreferredWidth( 2000);

		for ( int i = 0; i < 3; ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new GisMapInitialValueTableRowRenderer( _color));
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#append(java.lang.Object[])
	 */
	@Override
	public void append(Object[] objects) {
		if ( 0 <= has_same_key_and_value( ( GisMapInitialValue)objects[ 0], -1)) {
			JOptionPane.showMessageDialog(
				MainFrame.get_instance(),
				ResourceManager.get_instance().get( "edit.object.dialog.map.initial.value.table.already.exists.information.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		int row = has_same_key( ( GisMapInitialValue)objects[ 0], -1);
		if ( 0 <= row) {
			if ( JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(
				MainFrame.get_instance(),
				ResourceManager.get_instance().get( "edit.object.dialog.map.initial.value.table.duplicated.key.confirm.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.YES_NO_OPTION))
				return;

			DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
			defaultTableModel.removeRow( row);
			_gisInitialValueTableBase.removeRow( row);
			insert( objects);
			return;
		}

		insert( objects);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#insert(java.lang.Object[])
	 */
	@Override
	public void insert(Object[] objects) {
		int index = 0;
		boolean insert = false;
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount()) {
			_gisInitialValueTableBase.insert( 0);
			defaultTableModel.addRow( objects);
			insert = true;
		} else {
			for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
				GisMapInitialValue gmiv = ( GisMapInitialValue)defaultTableModel.getValueAt( i, 0);
				if ( null == gmiv)
					return;

				if ( 0 > compare( ( GisMapInitialValue)objects[ 0], gmiv)) {
					_gisInitialValueTableBase.insert( i);
					defaultTableModel.insertRow( i, objects);
					index = i;
					insert = true;
					break;
				}
			}
		}

		if ( !insert) {
			index = defaultTableModel.getRowCount();
			_gisInitialValueTableBase.insert( index);
			defaultTableModel.addRow( objects);
			//index = defaultTableModel.getRowCount() - 1;
		}

		//_gisInitialValueTableBase.insert( index);
		select( index);
		scroll( index);
	}

	/**
	 * @param gisMapInitialValue0
	 * @param gisMapInitialValue1
	 * @return
	 */
	private int compare(GisMapInitialValue gisMapInitialValue0, GisMapInitialValue gisMapInitialValue1) {
		if ( !gisMapInitialValue0._key[ 0].equals( gisMapInitialValue1._key[ 0]))
			return compare1( gisMapInitialValue0._key[ 0], gisMapInitialValue1._key[ 0]);
		else {
			if ( gisMapInitialValue0._key[ 0].equals( "field")) {
				if ( !gisMapInitialValue0._key[ 2].equals( gisMapInitialValue1._key[ 2]))
					return compare3( gisMapInitialValue0._key[ 2], gisMapInitialValue1._key[ 2]);
				else
					return StringNumberComparator.compareTo( gisMapInitialValue0._key[ 1], gisMapInitialValue1._key[ 1]);
			} else {
				if ( !gisMapInitialValue0._key[ 1].equals( gisMapInitialValue1._key[ 1]))
					return StringNumberComparator.compareTo( gisMapInitialValue0._key[ 1], gisMapInitialValue1._key[ 1]);
				else {
					if ( !gisMapInitialValue0._value[ 0].equals( gisMapInitialValue1._value[ 0]))
						return compare2( gisMapInitialValue0._value[ 0], gisMapInitialValue1._value[ 0]);
					else {
						if ( !gisMapInitialValue0._value[ 0].equals( "field"))
							return StringNumberComparator.compareTo( gisMapInitialValue0._value[ 1], gisMapInitialValue1._value[ 1]);
						else {
							if ( !gisMapInitialValue0._value[ 2].equals( gisMapInitialValue1._value[ 2]))
								return compare3( gisMapInitialValue0._value[ 2], gisMapInitialValue1._value[ 2]);
							else
								return StringNumberComparator.compareTo( gisMapInitialValue0._value[ 1], gisMapInitialValue1._value[ 1]);
						}
					}
				}
			}
		}
	}

	/**
	 * @param value0
	 * @param value1
	 * @return
	 */
	private int compare1(String value0, String value1) {
		if ( ( value0.equals( "immediate")
				&& ( value1.equals( "keyword") || value1.equals( "field")))
			|| ( value0.equals( "integer")
				&& ( value1.equals( "field"))))
			return -1;
		else if ( ( value0.equals( "keyword")
				&& ( value1.equals( "immediate")))
			|| ( value0.equals( "field")
				&& ( value1.equals( "immediate") || value1.equals( "keyword"))))
			return 1;
		else
			return StringNumberComparator.compareTo( value0, value1);
	}

	/**
	 * @param value0
	 * @param value1
	 * @return
	 */
	private int compare3(String value0, String value1) {
		if ( ( value0.equals( "string")
				&& ( value1.equals( "integer") || value1.equals( "real number")))
			|| ( value0.equals( "integer")
				&& ( value1.equals( "real number"))))
			return -1;
		else if ( ( value0.equals( "integer")
				&& ( value1.equals( "string")))
			|| ( value0.equals( "real number")
				&& ( value1.equals( "string") || value1.equals( "integer"))))
			return 1;
		else
			return StringNumberComparator.compareTo( value0, value1);
	}

	/**
	 * @param value0
	 * @param value1
	 * @return
	 */
	private int compare2(String value0, String value1) {
		if ( ( value0.equals( "agent")
				&& ( value1.equals( "spot") || value1.equals( "probability") || value1.equals( "keyword") || value1.equals( "number object") || value1.equals( "role variable") || value1.equals( "time variable") || value1.equals( "spot variable") || value1.equals( "collection") || value1.equals( "list") || value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra") || value1.equals( "field")))
			|| ( value0.equals( "spot")
				&& ( value1.equals( "probability") || value1.equals( "keyword") || value1.equals( "number object") || value1.equals( "role variable") || value1.equals( "time variable") || value1.equals( "spot variable") || value1.equals( "collection") || value1.equals( "list") || value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra") || value1.equals( "field")))
			|| ( value0.equals( "probability")
				&& ( value1.equals( "keyword") || value1.equals( "number object") || value1.equals( "role variable") || value1.equals( "time variable") || value1.equals( "spot variable") || value1.equals( "collection") || value1.equals( "list") || value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra") || value1.equals( "field")))
			|| ( value0.equals( "keyword")
				&& ( value1.equals( "number object") || value1.equals( "role variable") || value1.equals( "time variable") || value1.equals( "spot variable") || value1.equals( "collection") || value1.equals( "list") || value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra") || value1.equals( "field")))
			|| ( value0.equals( "number object")
				&& ( value1.equals( "role variable") || value1.equals( "time variable") || value1.equals( "spot variable") || value1.equals( "collection") || value1.equals( "list") || value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra") || value1.equals( "field")))
			|| ( value0.equals( "role variable")
				&& ( value1.equals( "time variable") || value1.equals( "spot variable") || value1.equals( "collection") || value1.equals( "list") || value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra") || value1.equals( "field")))
			|| ( value0.equals( "time variable")
				&& ( value1.equals( "spot variable") || value1.equals( "collection") || value1.equals( "list") || value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra") || value1.equals( "field")))
			|| ( value0.equals( "spot variable")
				&& ( value1.equals( "collection") || value1.equals( "list") || value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra") || value1.equals( "field")))
			|| ( value0.equals( "collection")
				&& ( value1.equals( "list") || value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra") || value1.equals( "field")))
			|| ( value0.equals( "list")
				&& ( value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra") || value1.equals( "field")))
			|| ( value0.equals( "map")
				&& ( value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra") || value1.equals( "field")))
			|| ( value0.equals( "class variable")
				&& ( value1.equals( "file") || value1.equals( "exchange algebra") || value1.equals( "field")))
			|| ( value0.equals( "file")
				&& ( value1.equals( "exchange algebra") || value1.equals( "field")))
			|| ( value0.equals( "exchange algebra")
				&& ( value1.equals( "field"))))
			return -1;
		else if ( ( value0.equals( "spot")
				&& ( value1.equals( "agent")))
			|| ( value0.equals( "probability")
				&& ( value1.equals( "agent") || value1.equals( "spot")))
			|| ( value0.equals( "keyword")
				&& ( value1.equals( "agent") || value1.equals( "spot") || value1.equals( "probability")))
			|| ( value0.equals( "number object")
				&& ( value1.equals( "agent") || value1.equals( "spot") || value1.equals( "probability") || value1.equals( "keyword")))
			|| ( value0.equals( "role variable")
				&& ( value1.equals( "agent") || value1.equals( "spot") || value1.equals( "probability") || value1.equals( "keyword") || value1.equals( "number object")))
			|| ( value0.equals( "time variable")
				&& ( value1.equals( "agent") || value1.equals( "spot") || value1.equals( "probability") || value1.equals( "keyword") || value1.equals( "number object") || value1.equals( "role variable")))
			|| ( value0.equals( "spot variable")
				&& ( value1.equals( "agent") || value1.equals( "spot") || value1.equals( "probability") || value1.equals( "keyword") || value1.equals( "number object") || value1.equals( "role variable") || value1.equals( "time variable")))
			|| ( value0.equals( "collection")
				&& ( value1.equals( "agent") || value1.equals( "spot") || value1.equals( "probability") || value1.equals( "keyword") || value1.equals( "number object") || value1.equals( "role variable") || value1.equals( "time variable") || value1.equals( "spot variable")))
			|| ( value0.equals( "list")
				&& ( value1.equals( "agent") || value1.equals( "spot") || value1.equals( "probability") || value1.equals( "keyword") || value1.equals( "number object") || value1.equals( "role variable") || value1.equals( "time variable") || value1.equals( "spot variable") || value1.equals( "collection")))
			|| ( value0.equals( "map")
				&& ( value1.equals( "agent") || value1.equals( "spot") || value1.equals( "probability") || value1.equals( "keyword") || value1.equals( "number object") || value1.equals( "role variable") || value1.equals( "time variable") || value1.equals( "spot variable") || value1.equals( "collection") || value1.equals( "list")))
			|| ( value0.equals( "class variable")
				&& ( value1.equals( "agent") || value1.equals( "spot") || value1.equals( "probability") || value1.equals( "keyword") || value1.equals( "number object") || value1.equals( "role variable") || value1.equals( "time variable") || value1.equals( "spot variable") || value1.equals( "collection") || value1.equals( "list") || value1.equals( "map")))
			|| ( value0.equals( "file")
				&& ( value1.equals( "agent") || value1.equals( "spot") || value1.equals( "probability") || value1.equals( "keyword") || value1.equals( "number object") || value1.equals( "role variable") || value1.equals( "time variable") || value1.equals( "spot variable") || value1.equals( "collection") || value1.equals( "list") || value1.equals( "map") || value1.equals( "class variable")))
			|| ( value0.equals( "exchange algebra")
				&& ( value1.equals( "agent") || value1.equals( "spot") || value1.equals( "probability") || value1.equals( "keyword") || value1.equals( "number object") || value1.equals( "role variable") || value1.equals( "time variable") || value1.equals( "spot variable") || value1.equals( "collection") || value1.equals( "list") || value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file")))
			|| ( value0.equals( "field")
				&& ( value1.equals( "agent") || value1.equals( "spot") || value1.equals( "probability") || value1.equals( "keyword") || value1.equals( "number object") || value1.equals( "role variable") || value1.equals( "time variable") || value1.equals( "spot variable") || value1.equals( "collection") || value1.equals( "list") || value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra"))))
			return 1;
		else
			return StringNumberComparator.compareTo( value0, value1);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#update(java.lang.Object[])
	 */
	@Override
	public void update(Object[] objects) {
		int[] rows = _gisInitialValueTableBase.getSelectedRows();
		if ( 1 != rows.length)
			return;

		if ( 0 <= has_same_key( ( GisMapInitialValue)objects[ 0], rows[ 0])) {
			JOptionPane.showMessageDialog(
				MainFrame.get_instance(),
				ResourceManager.get_instance().get( "edit.object.dialog.map.initial.value.table.duplicated.key.error.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.removeRow( rows[ 0]);
		_gisInitialValueTableBase.removeRow( rows[ 0]);
		insert( objects);
	}

	/**
	 * @param gisMapInitialValue
	 * @param row
	 * @return
	 */
	private int has_same_key_and_value(GisMapInitialValue gisMapInitialValue, int row) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			GisMapInitialValue gmiv = ( GisMapInitialValue)defaultTableModel.getValueAt( i, 0);
			if ( null == gmiv || row == i)
				continue;

			if ( gmiv.equals( gisMapInitialValue))
				return i;
		}
		return -1;
	}

	/**
	 * @param gisMapInitialValue
	 * @param row
	 * @return
	 */
	private int has_same_key(GisMapInitialValue gisMapInitialValue, int row) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			GisMapInitialValue gmiv = ( GisMapInitialValue)defaultTableModel.getValueAt( i, 0);
			if ( null == gmiv || row == i)
				continue;

			if ( gmiv.is_same_key( gisMapInitialValue))
				return i;
		}
		return -1;
	}

	/**
	 * @param gisMapObject
	 */
	public void update(GisMapObject gisMapObject) {
		cleanup();
		_gisInitialValueTableBase.cleanup();
		if ( null == gisMapObject)
			return;

		for ( GisMapInitialValue gmiv:gisMapObject._gisMapInitialValues) {
			GisMapInitialValue mapInitialValue = new GisMapInitialValue( gmiv);
			insert( new GisMapInitialValue[] { mapInitialValue, mapInitialValue, mapInitialValue});
		}

		if ( 0 < getRowCount()) {
			select( 0);
			_gisVariablePanelBase.changeSelection( getValueAt( 0, 0));
			scroll( 0);
		}
	}

	/**
	 * @param gisMapObject
	 */
	public void get(GisMapObject gisMapObject) {
		gisMapObject._gisMapInitialValues.clear();
		for ( int i = 0; i < getRowCount(); ++i)
			gisMapObject._gisMapInitialValues.add( ( GisMapInitialValue)getValueAt( i, 0));
	}

	/**
	 * @param gisObjectBase
	 * @return
	 */
	public boolean contains(GisObjectBase gisObjectBase) {
		for ( int i = 0; i < getRowCount(); ++i) {
			GisMapInitialValue gisMapInitialValue = ( GisMapInitialValue)getValueAt( i, 0);
			if ( gisMapInitialValue.contains( gisObjectBase))
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
			GisMapInitialValue gisMapInitialValue = ( GisMapInitialValue)getValueAt( i, 0);
			if ( gisMapInitialValue.update_object_name( type, name, newName))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#can_paste(java.awt.Point)
	 */
	@Override
	protected boolean can_paste(Point point) {
		return !__gisInitialValueBasesMap.get( _kind).isEmpty();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel.table.GisInitialValueTableBase#paste(int[])
	 */
	@Override
	public void paste(int[] rows) {
		if ( __gisInitialValueBasesMap.get( _kind).isEmpty())
			return;

		WarningManager.get_instance().cleanup();

		// 自分自身のを含む場合はとりあえず放置しておく
		int option = -1;
		List<GisMapInitialValue> gisMapInitialValues = new ArrayList<GisMapInitialValue>();
		for ( int i = 0; i < __gisInitialValueBasesMap.get( _kind).size(); ++i) {
			GisMapInitialValue gisMapInitialValue = ( GisMapInitialValue)GisInitialValueBase.create( __gisInitialValueBasesMap.get( _kind).get( i));
			if ( !gisMapInitialValue.can_paste( _gisPropertyPanelBaseMap, null, null/*, _entityBase*/)) {
				String key = "";
				String[] message = null;
				if ( gisMapInitialValue._key[ 0].equals( "immediate")) {
					key = ( "\"" + gisMapInitialValue._key[ 1] + "\"");
					message = new String[] {
						"key : " + key,
						"value : "
							+ ( gisMapInitialValue._value[ 0].equals( "field") ? GisInitialValueBase.__nameMap.get( gisMapInitialValue._value[ 2]) : GisPropertyPanelBase.get_nameMap().get( gisMapInitialValue._value[ 0]))
							+ ", name = " + gisMapInitialValue._value[ 1]
					};
				} else if ( gisMapInitialValue._key[ 0].equals( "keyword")) {
					key = ( GisPropertyPanelBase.get_nameMap().get( gisMapInitialValue._key[ 0]) + ", name = " + gisMapInitialValue._key[ 1]);
					message = new String[] {
						"key : " + key,
						"value : "
							+ ( gisMapInitialValue._value[ 0].equals( "field") ? GisInitialValueBase.__nameMap.get( gisMapInitialValue._value[ 2]) : GisPropertyPanelBase.get_nameMap().get( gisMapInitialValue._value[ 0]))
							+ ", name = " + gisMapInitialValue._value[ 1]
					};
				} else if ( gisMapInitialValue._key[ 0].equals( "field")) {
					key = ( GisInitialValueBase.__nameMap.get( gisMapInitialValue._key[ 2]) + ", name = " + gisMapInitialValue._key[ 1]);
					message = new String[] {
						"key : " + key,
						"value : "
							+ ( gisMapInitialValue._value[ 0].equals( "field") ? GisInitialValueBase.__nameMap.get( gisMapInitialValue._value[ 2]) : GisPropertyPanelBase.get_nameMap().get( gisMapInitialValue._value[ 0]))
							+ ", name = " + gisMapInitialValue._value[ 1]
					};
				} else
					continue;


				WarningManager.get_instance().add( message);
				continue;
			}

			GisMapInitialValue gmiv = get_gisMapInitialValue_has_same_key_and_value( gisMapInitialValue);
			// キーと値のペアが既に存在していたら、、、
			if ( null != gmiv) {
				gisMapInitialValues.add( gmiv);
				continue;
			}

			gmiv = get_mapInitialValue_has_same_key( gisMapInitialValue);
			// キーが既に存在していたら、、、
			if ( null != gmiv) {
				if ( 3 == option)		// 全ていいえ
					continue;
				else if ( 2 == option) {		// 全てはい
					gmiv.copy( gisMapInitialValue);
					gisMapInitialValues.add( gmiv);
					continue;
				} else {
					option = showOptionDialog( gisMapInitialValue._key);
					if ( 0 == option || 2 == option) {
						gmiv.copy( gisMapInitialValue);
						gisMapInitialValues.add( gmiv);
					}
					continue;
				}
			}

			append( new GisMapInitialValue[] { gisMapInitialValue, gisMapInitialValue, gisMapInitialValue});
			gisMapInitialValues.add( gisMapInitialValue);
		}

		List<Integer> list = new ArrayList<Integer>();
		for ( GisMapInitialValue gisMapInitialValue:gisMapInitialValues) {
			for ( int i = 0; i < getRowCount(); ++i) {
				if ( gisMapInitialValue == getValueAt( i, 0)) {
					list.add( i);
					break;
				}
			}
		}
		_gisInitialValueTableBase.select( Tool.get_array( list));
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

	/**
	 * @param gisMapInitialValues
	 * @return
	 */
	private GisMapInitialValue get_gisMapInitialValue_has_same_key_and_value(GisMapInitialValue gisMapInitialValues) {
		for ( int i = 0; i < getRowCount(); ++i) {
			GisMapInitialValue gmiv = ( GisMapInitialValue)getValueAt( i, 0);
			if ( null == gmiv)
				continue;

			if ( gmiv.is_same_key_and_value( gisMapInitialValues))
				return gmiv;
		}
		return null;
	}

	/**
	 * @param gisMapInitialValues
	 * @return
	 */
	private GisMapInitialValue get_mapInitialValue_has_same_key(GisMapInitialValue gisMapInitialValues) {
		for ( int i = 0; i < getRowCount(); ++i) {
			GisMapInitialValue gmiv = ( GisMapInitialValue)getValueAt( i, 0);
			if ( null == gmiv)
				continue;

			if ( gmiv.is_same_key( gisMapInitialValues))
				return gmiv;
		}
		return null;
	}

	/**
	 * @param key
	 * @return
	 */
	private int showOptionDialog(String[] key) {
		String[] overwrite_options = new String[] {
			ResourceManager.get_instance().get( "dialog.yes"),
			ResourceManager.get_instance().get( "dialog.no"),
			ResourceManager.get_instance().get( "dialog.yes.to.all"),
			ResourceManager.get_instance().get( "dialog.no.to.all")};
		String message = ResourceManager.get_instance().get( "edit.object.dialog.map.initial.value.table.header.key") + " : ";
		if ( key[ 0].equals( "immediate"))
			message += ( "\"" + key[ 1] + "\"");
		else if ( key[ 0].equals( "keyword"))
			message += key[ 1];
		else if ( key[ 0].equals( "field"))
			message += ( GisInitialValueBase.__nameMap.get( key[ 2]) + " : " + key[ 1]);
		return JOptionPane.showOptionDialog(
			getParent(),
			message + ResourceManager.get_instance().get( "edit.object.dialog.map.initial.value.table.confirm.overwrite.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.INFORMATION_MESSAGE,
			null,
			overwrite_options,
			overwrite_options[ 0]);
	}
}
