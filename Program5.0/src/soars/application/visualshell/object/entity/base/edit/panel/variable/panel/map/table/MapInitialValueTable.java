/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.map.table;

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
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.VariablePanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTable;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase;
import soars.application.visualshell.object.entity.base.object.base.InitialValueBase;
import soars.application.visualshell.object.entity.base.object.base.ObjectBase;
import soars.application.visualshell.object.entity.base.object.map.MapInitialValue;
import soars.application.visualshell.object.entity.base.object.map.MapObject;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.sort.StringNumberComparator;

/**
 * @author kurata
 *
 */
public class MapInitialValueTable extends InitialValueTable {

	/**
	 * @param color
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 * @param variablePanelBase
	 * @param owner
	 * @param parent
	 */
	public MapInitialValueTable(Color color, EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, VariablePanelBase variablePanelBase, Frame owner, Component parent) {
		super("map", color, entityBase, propertyPanelBaseMap, variablePanelBase, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#setup(soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase)
	 */
	@Override
	public boolean setup(InitialValueTableBase initialValueTableBase) {
		if ( !super.setup(initialValueTableBase))
			return false;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 3);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue( ResourceManager.get_instance().get( "edit.object.dialog.map.initial.value.table.header.key"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue( ResourceManager.get_instance().get( "edit.object.dialog.map.initial.value.table.header.initial.value.type"));
		defaultTableColumnModel.getColumn( 2).setHeaderValue( ResourceManager.get_instance().get( "edit.object.dialog.map.initial.value.table.header.initial.value"));

		defaultTableColumnModel.getColumn( 0).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 1).setPreferredWidth( 100);
		defaultTableColumnModel.getColumn( 2).setPreferredWidth( 2000);

		for ( int i = 0; i < 3; ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new MapInitialValueTableRowRenderer( _color));
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTable#append(java.lang.Object[])
	 */
	@Override
	public void append(Object[] objects) {
		if ( 0 <= has_same_key_and_value( ( MapInitialValue)objects[ 0], -1)) {
			JOptionPane.showMessageDialog(
				MainFrame.get_instance(),
				ResourceManager.get_instance().get( "edit.object.dialog.map.initial.value.table.already.exists.information.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		int row = has_same_key( ( MapInitialValue)objects[ 0], -1);
		if ( 0 <= row) {
			if ( JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(
				MainFrame.get_instance(),
				ResourceManager.get_instance().get( "edit.object.dialog.map.initial.value.table.duplicated.key.confirm.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.YES_NO_OPTION))
				return;

			DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
			defaultTableModel.removeRow( row);
			_initialValueTableBase.removeRow( row);
			insert( objects);
			return;
		}

		insert( objects);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTable#insert(java.lang.Object[])
	 */
	@Override
	public void insert(Object[] objects) {
		int index = 0;
		boolean insert = false;
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount()) {
			_initialValueTableBase.insert( 0);
			defaultTableModel.addRow( objects);
			insert = true;
		} else {
			for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
				MapInitialValue miv = ( MapInitialValue)defaultTableModel.getValueAt( i, 0);
				if ( null == miv)
					return;

				if ( 0 > compare( ( MapInitialValue)objects[ 0], miv)) {
					_initialValueTableBase.insert( i);
					defaultTableModel.insertRow( i, objects);
					index = i;
					insert = true;
					break;
				}
			}
		}

		if ( !insert) {
			index = defaultTableModel.getRowCount();
			_initialValueTableBase.insert( index);
			defaultTableModel.addRow( objects);
			//index = defaultTableModel.getRowCount() - 1;
		}

		//_initialValueTableBase.insert( index);
		select( index);
		scroll( index);
	}

	/**
	 * @param mapInitialValue0
	 * @param mapInitialValue1
	 * @return
	 */
	private int compare(MapInitialValue mapInitialValue0, MapInitialValue mapInitialValue1) {
		if ( mapInitialValue0._key[ 0].equals( "immediate") && mapInitialValue1._key[ 0].equals( "keyword"))
			return -1;
		else if ( mapInitialValue0._key[ 0].equals( "keyword") && mapInitialValue1._key[ 0].equals( "immediate"))
			return 1;
		else {
			if ( !mapInitialValue0._key[ 1].equals( mapInitialValue1._key[ 1]))
				return StringNumberComparator.compareTo( mapInitialValue0._key[ 1], mapInitialValue1._key[ 1]);
			else {
				if ( mapInitialValue0._value[ 0].equals( mapInitialValue1._value[ 0]))
					return StringNumberComparator.compareTo( mapInitialValue0._value[ 1], mapInitialValue1._value[ 1]);
				else
					return compare( mapInitialValue0._value[ 0], mapInitialValue1._value[ 0]);
			}
		}
	}

	/**
	 * @param value0
	 * @param value1
	 * @return
	 */
	private int compare(String value0, String value1) {
		if ( ( value0.equals( "agent")
				&& ( value1.equals( "spot") || value1.equals( "probability") || value1.equals( "keyword") || value1.equals( "number object") || value1.equals( "role variable") || value1.equals( "time variable") || value1.equals( "spot variable") || value1.equals( "collection") || value1.equals( "list") || value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra")))
			|| ( value0.equals( "spot")
				&& ( value1.equals( "probability") || value1.equals( "keyword") || value1.equals( "number object") || value1.equals( "role variable") || value1.equals( "time variable") || value1.equals( "spot variable") || value1.equals( "collection") || value1.equals( "list") || value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra")))
			|| ( value0.equals( "probability")
				&& ( value1.equals( "keyword") || value1.equals( "number object") || value1.equals( "role variable") || value1.equals( "time variable") || value1.equals( "spot variable") || value1.equals( "collection") || value1.equals( "list") || value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra")))
			|| ( value0.equals( "keyword")
				&& ( value1.equals( "number object") || value1.equals( "role variable") || value1.equals( "time variable") || value1.equals( "spot variable") || value1.equals( "collection") || value1.equals( "list") || value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra")))
			|| ( value0.equals( "number object")
				&& ( value1.equals( "role variable") || value1.equals( "time variable") || value1.equals( "spot variable") || value1.equals( "collection") || value1.equals( "list") || value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra")))
			|| ( value0.equals( "role variable")
				&& ( value1.equals( "time variable") || value1.equals( "spot variable") || value1.equals( "collection") || value1.equals( "list") || value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra")))
			|| ( value0.equals( "time variable")
				&& ( value1.equals( "spot variable") || value1.equals( "collection") || value1.equals( "list") || value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra")))
			|| ( value0.equals( "spot variable")
				&& ( value1.equals( "collection") || value1.equals( "list") || value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra")))
			|| ( value0.equals( "collection")
				&& ( value1.equals( "list") || value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra")))
			|| ( value0.equals( "list")
				&& ( value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra")))
			|| ( value0.equals( "map")
				&& ( value1.equals( "class variable") || value1.equals( "file") || value1.equals( "exchange algebra")))
			|| ( value0.equals( "class variable")
				&& ( value1.equals( "file") || value1.equals( "exchange algebra")))
			|| ( value0.equals( "file")
				&& ( value1.equals( "exchange algebra"))))
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
				&& ( value1.equals( "agent") || value1.equals( "spot") || value1.equals( "probability") || value1.equals( "keyword") || value1.equals( "number object") || value1.equals( "role variable") || value1.equals( "time variable") || value1.equals( "spot variable") || value1.equals( "collection") || value1.equals( "list") || value1.equals( "map") || value1.equals( "class variable") || value1.equals( "file"))))
			return 1;
		else
			return StringNumberComparator.compareTo( value0, value1);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTable#update(java.lang.Object[])
	 */
	@Override
	public void update(Object[] objects) {
		int[] rows = _initialValueTableBase.getSelectedRows();
		if ( 1 != rows.length)
			return;

		if ( 0 <= has_same_key( ( MapInitialValue)objects[ 0], rows[ 0])) {
			JOptionPane.showMessageDialog(
				MainFrame.get_instance(),
				ResourceManager.get_instance().get( "edit.object.dialog.map.initial.value.table.duplicated.key.error.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.removeRow( rows[ 0]);
		_initialValueTableBase.removeRow( rows[ 0]);
		insert( objects);
	}

	/**
	 * @param mapInitialValue
	 * @param row
	 * @return
	 */
	private int has_same_key_and_value(MapInitialValue mapInitialValue, int row) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			MapInitialValue miv = ( MapInitialValue)defaultTableModel.getValueAt( i, 0);
			if ( null == miv || row == i)
				continue;

			if ( miv.equals( mapInitialValue))
				return i;
		}
		return -1;
	}

	/**
	 * @param mapInitialValue
	 * @param row
	 * @return
	 */
	private int has_same_key(MapInitialValue mapInitialValue, int row) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			MapInitialValue miv = ( MapInitialValue)defaultTableModel.getValueAt( i, 0);
			if ( null == miv || row == i)
				continue;

			if ( miv.is_same_key( mapInitialValue))
				return i;
		}
		return -1;
	}

	/**
	 * @param mapObject
	 */
	public void update(MapObject mapObject) {
		cleanup();
		_initialValueTableBase.cleanup();
		if ( null == mapObject)
			return;

		for ( MapInitialValue miv:mapObject._mapInitialValues) {
			MapInitialValue mapInitialValue = new MapInitialValue( miv);
			insert( new MapInitialValue[] { mapInitialValue, mapInitialValue, mapInitialValue});
		}

		if ( 0 < getRowCount()) {
			select( 0);
			_variablePanelBase.changeSelection( getValueAt( 0, 0));
			scroll( 0);
		}
	}

	/**
	 * @param mapObject
	 */
	public void get(MapObject mapObject) {
		mapObject._mapInitialValues.clear();
		for ( int i = 0; i < getRowCount(); ++i)
			mapObject._mapInitialValues.add( ( MapInitialValue)getValueAt( i, 0));
	}

	/**
	 * @param objectBase
	 * @return
	 */
	public boolean contains(ObjectBase objectBase) {
		for ( int i = 0; i < getRowCount(); ++i) {
			MapInitialValue mapInitialValue = ( MapInitialValue)getValueAt( i, 0);
			if ( mapInitialValue.contains( objectBase))
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
			MapInitialValue mapInitialValue = ( MapInitialValue)getValueAt( i, 0);
			if ( mapInitialValue.update_object_name( type, name, newName))
				result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#can_paste(java.awt.Point)
	 */
	@Override
	protected boolean can_paste(Point point) {
		return !__initialValueBasesMap.get( _kind).isEmpty();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#paste(int[])
	 */
	@Override
	public void paste(int[] rows) {
		if ( __initialValueBasesMap.get( _kind).isEmpty())
			return;

		WarningManager.get_instance().cleanup();

		// 自分自身のを含む場合はとりあえず放置しておく
		int option = -1;
		List<MapInitialValue> mapInitialValues = new ArrayList<MapInitialValue>();
		for ( int i = 0; i < __initialValueBasesMap.get( _kind).size(); ++i) {
			MapInitialValue mapInitialValue = ( MapInitialValue)InitialValueBase.create( __initialValueBasesMap.get( _kind).get( i));
			if ( !mapInitialValue.can_paste( _propertyPanelBaseMap, null, null, _entityBase)) {
				String key = ( mapInitialValue._key[ 0].equals( "immediate")
					? ( "\"" + mapInitialValue._key[ 1] + "\"")
					: ( PropertyPanelBase._nameMap.get( mapInitialValue._key[ 0]) + ", name = " + mapInitialValue._key[ 1]));
				String[] message = new String[] {
					"key : " + key,
					"value : " + PropertyPanelBase._nameMap.get( mapInitialValue._value[ 0]) + ", name = " + mapInitialValue._value[ 1]
				};

				WarningManager.get_instance().add( message);
				continue;
			}

			MapInitialValue miv = get_mapInitialValue_has_same_key_and_value( mapInitialValue);
			// キーと値のペアが既に存在していたら、、、
			if ( null != miv) {
				mapInitialValues.add( miv);
				continue;
			}

			miv = get_mapInitialValue_has_same_key( mapInitialValue);
			// キーが既に存在していたら、、、
			if ( null != miv) {
				if ( 3 == option)		// 全ていいえ
					continue;
				else if ( 2 == option) {		// 全てはい
					miv.copy( mapInitialValue);
					mapInitialValues.add( miv);
					continue;
				} else {
					option = showOptionDialog( mapInitialValue._key);
					if ( 0 == option || 2 == option) {
						miv.copy( mapInitialValue);
						mapInitialValues.add( miv);
					}
					continue;
				}
			}

			append( new MapInitialValue[] { mapInitialValue, mapInitialValue, mapInitialValue});
			mapInitialValues.add( mapInitialValue);
		}

		List<Integer> list = new ArrayList<Integer>();
		for ( MapInitialValue mapInitialValue:mapInitialValues) {
			for ( int i = 0; i < getRowCount(); ++i) {
				if ( mapInitialValue == getValueAt( i, 0)) {
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

	/**
	 * @param mapInitialValue
	 * @return
	 */
	private MapInitialValue get_mapInitialValue_has_same_key_and_value(MapInitialValue mapInitialValue) {
		for ( int i = 0; i < getRowCount(); ++i) {
			MapInitialValue miv = ( MapInitialValue)getValueAt( i, 0);
			if ( null == miv)
				continue;

			if ( miv.equals( mapInitialValue))
				return miv;
		}
		return null;
	}

	/**
	 * @param mapInitialValue
	 * @return
	 */
	private MapInitialValue get_mapInitialValue_has_same_key(MapInitialValue mapInitialValue) {
		for ( int i = 0; i < getRowCount(); ++i) {
			MapInitialValue miv = ( MapInitialValue)getValueAt( i, 0);
			if ( null == miv)
				continue;

			if ( miv.is_same_key( mapInitialValue))
				return miv;
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
		return JOptionPane.showOptionDialog(
			getParent(),
			ResourceManager.get_instance().get( "edit.object.dialog.map.initial.value.table.header.key") + " : "
				+ ( ( key[ 0].equals( "immediate") ? "\"" : "") + key[ 1] + ( key[ 0].equals( "immediate") ? "\"" : "")) + "\n"
				+ ResourceManager.get_instance().get( "edit.object.dialog.map.initial.value.table.confirm.overwrite.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.INFORMATION_MESSAGE,
			null,
			overwrite_options,
			overwrite_options[ 0]);
	}
}
