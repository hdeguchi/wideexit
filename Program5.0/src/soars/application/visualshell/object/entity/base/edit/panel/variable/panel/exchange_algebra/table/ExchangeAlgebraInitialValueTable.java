/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.exchange_algebra.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
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
import soars.application.visualshell.object.entity.base.object.exchange_algebra.ExchangeAlgebraInitialValue;
import soars.application.visualshell.object.entity.base.object.exchange_algebra.ExchangeAlgebraObject;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 *
 */
public class ExchangeAlgebraInitialValueTable extends InitialValueTable {

	/**
	 * @param color
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 * @param variablePanelBase
	 * @param owner
	 * @param parent
	 */
	public ExchangeAlgebraInitialValueTable(Color color, EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, VariablePanelBase variablePanelBase, Frame owner, Component parent) {
		super("exchange algebra", color, entityBase, propertyPanelBaseMap, variablePanelBase, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase#setup(soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.table.InitialValueTableBase)
	 */
	@Override
	public boolean setup(InitialValueTableBase initialValueTableBase) {
		if ( !super.setup(initialValueTableBase))
			return false;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 1);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue( ResourceManager.get_instance().get( "edit.exchange.algebra.dialog.initial.value"));

		defaultTableColumnModel.getColumn( 0).setPreferredWidth( 2000);

		for ( int i = 0; i < 1; ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new ExchangeAlgebraInitialValueTableRowRenderer( _color));
		}

		return true;
	}

	/**
	 * @param exchangeAlgebraObject
	 */
	public void update(ExchangeAlgebraObject exchangeAlgebraObject) {
		cleanup();
		_initialValueTableBase.cleanup();
		if ( null == exchangeAlgebraObject)
			return;

		for ( ExchangeAlgebraInitialValue eaiv:exchangeAlgebraObject._exchangeAlgebraInitialValues) {
			ExchangeAlgebraInitialValue exchangeAlgebraInitialValue = new ExchangeAlgebraInitialValue( eaiv);
			append( new ExchangeAlgebraInitialValue[] { exchangeAlgebraInitialValue});
		}

		if ( 0 < getRowCount()) {
			select( 0);
			_variablePanelBase.changeSelection( getValueAt( 0, 0));
			scroll( 0);
		}
	}

	/**
	 * @param exchangeAlgebraObject
	 */
	public void get(ExchangeAlgebraObject exchangeAlgebraObject) {
		exchangeAlgebraObject._exchangeAlgebraInitialValues.clear();
		for ( int i = 0; i < getRowCount(); ++i)
			exchangeAlgebraObject._exchangeAlgebraInitialValues.add( ( ExchangeAlgebraInitialValue)getValueAt( i, 0));
	}

	/**
	 * @param objectBase
	 * @return
	 */
	public boolean contains(ObjectBase objectBase) {
		for ( int i = 0; i < getRowCount(); ++i) {
			ExchangeAlgebraInitialValue exchangeAlgebraInitialValue = ( ExchangeAlgebraInitialValue)getValueAt( i, 0);
			if ( exchangeAlgebraInitialValue.contains( objectBase))
				return true;
		}
		return false;
	}

	/**
	 * @param kind
	 * @param name
	 * @return
	 */
	public boolean contains(String kind, String name) {
		for ( int i = 0; i < getRowCount(); ++i) {
			ExchangeAlgebraInitialValue exchangeAlgebraInitialValue = ( ExchangeAlgebraInitialValue)getValueAt( i, 0);
			if ( exchangeAlgebraInitialValue.contains( kind, name))
				return true;
		}
		return false;
	}

	/**
	 * @param kind
	 * @param name
	 * @param eaiv
	 * @return
	 */
	public boolean contains(String kind, String name, ExchangeAlgebraInitialValue eaiv) {
		for ( int i = 0; i < getRowCount(); ++i) {
			ExchangeAlgebraInitialValue exchangeAlgebraInitialValue = ( ExchangeAlgebraInitialValue)getValueAt( i, 0);
			if ( exchangeAlgebraInitialValue == eaiv)
				continue;

			if ( exchangeAlgebraInitialValue.contains( kind, name))
				return true;
		}
		return false;
	}

	/**
	 * @param name
	 * @param hat
	 * @param unit
	 * @param time
	 * @param subject
	 * @return
	 */
	public boolean contains(String name, String hat, String unit, String time, String subject) {
		for ( int i = 0; i < getRowCount(); ++i) {
			ExchangeAlgebraInitialValue exchangeAlgebraInitialValue = ( ExchangeAlgebraInitialValue)getValueAt( i, 0);
			if ( exchangeAlgebraInitialValue.contains( name, hat, unit, time, subject))
				return true;
		}
		return false;
	}

	/**
	 * @param name
	 * @param hat
	 * @param unit
	 * @param time
	 * @param subject
	 * @param eaiv
	 * @return
	 */
	public boolean contains(String name, String hat, String unit, String time, String subject, ExchangeAlgebraInitialValue eaiv) {
		for ( int i = 0; i < getRowCount(); ++i) {
			ExchangeAlgebraInitialValue exchangeAlgebraInitialValue = ( ExchangeAlgebraInitialValue)getValueAt( i, 0);
			if ( exchangeAlgebraInitialValue == eaiv)
				continue;

			if ( exchangeAlgebraInitialValue.contains( name, hat, unit, time, subject))
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
			ExchangeAlgebraInitialValue exchangeAlgebraInitialValue = ( ExchangeAlgebraInitialValue)getValueAt( i, 0);
			if ( exchangeAlgebraInitialValue.update_object_name( type, name, newName))
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

		List<ExchangeAlgebraInitialValue> exchangeAlgebraInitialValues = new ArrayList<ExchangeAlgebraInitialValue>();
		if ( 0 == getRowCount()) {
			for ( int i = 0; i < __initialValueBasesMap.get( _kind).size(); ++i) {
				ExchangeAlgebraInitialValue exchangeAlgebraInitialValue = ( ExchangeAlgebraInitialValue)InitialValueBase.create( __initialValueBasesMap.get( _kind).get( i));
				if ( !exchangeAlgebraInitialValue.can_paste( _propertyPanelBaseMap)) {
					String[] message = new String[] {
						"value = " + exchangeAlgebraInitialValue._value,
						"base = " + exchangeAlgebraInitialValue.get_base()
					};

					WarningManager.get_instance().add( message);
					continue;
				}

				append( new ExchangeAlgebraInitialValue[] { exchangeAlgebraInitialValue, exchangeAlgebraInitialValue});
				exchangeAlgebraInitialValues.add( exchangeAlgebraInitialValue);
			}
		} else {
			int option = -1;
			for ( int i = 0; i < __initialValueBasesMap.get( _kind).size(); ++i) {
				ExchangeAlgebraInitialValue exchangeAlgebraInitialValue = ( ExchangeAlgebraInitialValue)InitialValueBase.create( __initialValueBasesMap.get( _kind).get( i));
				if ( !exchangeAlgebraInitialValue.can_paste( _propertyPanelBaseMap)) {
					String[] message = new String[] {
						"value = " + exchangeAlgebraInitialValue._value,
						"base = " + exchangeAlgebraInitialValue.get_base()
					};

					WarningManager.get_instance().add( message);
					continue;
				}

				ExchangeAlgebraInitialValue eaiv = get_exchangeAlgebraInitialValue_has_same_base_and_value( exchangeAlgebraInitialValue);
				// 基底と値のペアが既に存在していたら、、、
				if ( null != eaiv) {
					exchangeAlgebraInitialValues.add( eaiv);
					continue;
				}

				eaiv = get_exchangeAlgebraInitialValue_has_same_base( exchangeAlgebraInitialValue);
				// 基底が既に存在していたら、、、
				if ( null != eaiv) {
					if ( 3 == option)		// 全ていいえ
						continue;
					else if ( 2 == option) {		// 全てはい
						eaiv.copy( exchangeAlgebraInitialValue);
						exchangeAlgebraInitialValues.add( eaiv);
						continue;
					} else {
						option = showOptionDialog( exchangeAlgebraInitialValue.get_base());
						if ( 0 == option || 2 == option) {
							eaiv.copy( exchangeAlgebraInitialValue);
							exchangeAlgebraInitialValues.add( eaiv);
						}
						continue;
					}
				}

				int index = ( rows[ 0] + __initialValueBasesMap.get( _kind).get( i)._row);
				if ( getRowCount() > index)
					insert( new ExchangeAlgebraInitialValue[] { exchangeAlgebraInitialValue, exchangeAlgebraInitialValue}, index);
				else
					append( new ExchangeAlgebraInitialValue[] { exchangeAlgebraInitialValue, exchangeAlgebraInitialValue});
				exchangeAlgebraInitialValues.add( exchangeAlgebraInitialValue);
			}
		}

		List<Integer> list = new ArrayList<Integer>();
		for ( ExchangeAlgebraInitialValue exchangeAlgebraInitialValue:exchangeAlgebraInitialValues) {
			for ( int i = 0; i < getRowCount(); ++i) {
				if ( exchangeAlgebraInitialValue == getValueAt( i, 0)) {
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
	 * @param exchangeAlgebraInitialValue
	 * @return
	 */
	private ExchangeAlgebraInitialValue get_exchangeAlgebraInitialValue_has_same_base_and_value(ExchangeAlgebraInitialValue exchangeAlgebraInitialValue) {
		for ( int i = 0; i < getRowCount(); ++i) {
			ExchangeAlgebraInitialValue eaiv = ( ExchangeAlgebraInitialValue)getValueAt( i, 0);
			if ( null == eaiv)
				continue;

			if ( eaiv.equals( exchangeAlgebraInitialValue))
				return eaiv;
		}
		return null;
	}

	/**
	 * @param exchangeAlgebraInitialValue
	 * @return
	 */
	private ExchangeAlgebraInitialValue get_exchangeAlgebraInitialValue_has_same_base(ExchangeAlgebraInitialValue exchangeAlgebraInitialValue) {
		for ( int i = 0; i < getRowCount(); ++i) {
			ExchangeAlgebraInitialValue eaiv = ( ExchangeAlgebraInitialValue)getValueAt( i, 0);
			if ( null == eaiv)
				continue;

			if ( eaiv.is_same_base( exchangeAlgebraInitialValue))
				return eaiv;
		}
		return null;
	}

	/**
	 * @param base
	 * @return
	 */
	private int showOptionDialog(String base) {
		String[] overwrite_options = new String[] {
			ResourceManager.get_instance().get( "dialog.yes"),
			ResourceManager.get_instance().get( "dialog.no"),
			ResourceManager.get_instance().get( "dialog.yes.to.all"),
			ResourceManager.get_instance().get( "dialog.no.to.all")};
		return JOptionPane.showOptionDialog(
			getParent(),
			ResourceManager.get_instance().get( "edit.object.dialog.exchange.algebra.initial.value.table.confirm.overwrite.subtitle") + " : "
				+ base + "\n"
				+ ResourceManager.get_instance().get( "edit.object.dialog.exchange.algebra.initial.value.table.confirm.overwrite.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.INFORMATION_MESSAGE,
			null,
			overwrite_options,
			overwrite_options[ 0]);
	}
}
