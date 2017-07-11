/*
 * 2005/05/23
 */
package soars.application.visualshell.object.role.base.edit.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.CommandTabbedPane;
import soars.application.visualshell.object.role.base.edit.tab.ConditionTabbedPane;
import soars.application.visualshell.object.role.base.edit.table.data.CommonRuleData;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.generic.GenericRule;

/**
 * @author kurata
 */
public class RuleTableCellRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	static private Color _commandBackgroundColor = null;

	/**
	 * 
	 */
	static private Color _orBackgroundColor = null;

	/**
	 * 
	 */
	static private ImageIcon _icon = null;

	/**
	 * 
	 */
	private Role _role = null;

	/**
	 * 
	 */
	static {
		startup();
	}

	/**
	 * 
	 */
	private static void startup() {
		_commandBackgroundColor = new Color( 255, 240, 240);
		_orBackgroundColor = new Color( 255, 255, 108);
		_icon = new ImageIcon( RuleTableCellRenderer.class.getResource( Constant._resourceDirectory + "/image/icon/cell.png"));
	}

	/**
	 * @param role 
	 * 
	 */
	public RuleTableCellRenderer(Role role) {
		super();
		_role = role;
	}

	/* (Non Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
		Rule rule = null;
		if ( arg1 instanceof Rule)
			rule = ( Rule)arg1;
		else if ( arg1 instanceof String)
			rule = Rule.create( "condition", 0, ResourceManager.get_instance().get( "rule.type.condition.stage"), ( String)arg1, false);

//		String text = ( null == rule) ? ""
//			: ( ( rule._or ? " || " : "") + get_name( rule._type, arg0) + " : " + rule._value);
		String text = "";
		if ( null != rule) {
			// TODO 2012.9.20
			if ( rule instanceof GenericRule)
				text = rule.get_cell_text( _role);
				//GenericRule genericRule = ( GenericRule)rule;
				//text = ( ( rule._or ? " || " : "") + "GenericRule : " + genericRule._id);
			else {
				String value = rule.get_cell_text( _role);

				// TODO どっちか？
				//if ( rule._kind.equals( "command") && rule._type.equals( ResourceManager.get_instance().get( "rule.type.command.move")))
				//	text = ( ( rule._or ? " || " : "") + value);
				//else
					text = ( ( rule._or ? " || " : "") + CommonRuleData.get_name( rule) + " : " + value);
			}
		}

		setOpaque( true);

		if ( arg0.hasFocus()) {
			if ( arg2) {
				setForeground( Color.white);
				// TODO 2012.9.20
				setBackground( ( null == rule) ? arg0.getSelectionBackground() : get_color( rule, _role));
//				set_icon( arg0, rule, arg4, arg5);
			} else {
				// TODO 2012.9.20
				setForeground( ( null == rule) ? arg0.getForeground() : get_color( rule, _role));
				setBackground( get_background_color( arg0, rule, arg4, arg5));
			}
		} else {
			if ( arg2) {
				setForeground( Color.white);
				// TODO 2012.9.20
				setBackground( ( null == rule) ? arg0.getSelectionBackground() : get_color( rule, _role));
//				set_icon( arg0, rule, arg4, arg5);
			} else {
				// TODO 2012.9.20
				setForeground( ( null == rule) ? arg0.getForeground() : ( get_color( rule, _role)));
				setBackground( get_background_color( arg0, rule, arg4, arg5));
			}
		}

		set_icon( arg0, rule, arg4, arg5);

		setText( text);

		return this;
	}

	/**
	 * @param rule
	 * @param role
	 * @return
	 */
	static public Color get_color(Rule rule, Role role) {
		// TODO 2012.9.20
		if ( rule instanceof GenericRule)
			return ( rule._kind.equals( "condition") ? ConditionTabbedPane.get_color( ( GenericRule)rule, role) : CommandTabbedPane.get_color( ( GenericRule)rule, role));
		else 
			return CommonRuleData.get_color( rule);
	}

	/**
	 * @param table
	 * @param rule
	 * @param row
	 * @param column
	 * @return
	 */
	private void set_icon(JTable table, Rule rule, int row, int column) {
		if ( null != rule && rule._or)
			setIcon( null);
		else {
			DefaultTableModel defaultTableModel = ( DefaultTableModel)table.getModel();
			for ( int i = column + 1; i < defaultTableModel.getColumnCount(); ++i) {
				Rule r = ( Rule)defaultTableModel.getValueAt( row, i);
				if ( null == r)
					continue;

				setIcon( ( null != rule && r._or) ? _icon : null);
				return;
			}

			setIcon( null);
		}
	}

	/**
	 * @param table
	 * @param rule
	 * @param row
	 * @param column
	 * @return
	 */
	private Color get_background_color(JTable table, Rule rule, int row, int column) {
		if ( null != rule && rule._kind.equals( "command"))
			return _commandBackgroundColor;

		if ( null != rule && rule._or) {
			setIcon( null);
			return _orBackgroundColor;
		} else {
			DefaultTableModel defaultTableModel = ( DefaultTableModel)table.getModel();
			for ( int i = column + 1; i < defaultTableModel.getColumnCount(); ++i) {
				Rule r = ( Rule)defaultTableModel.getValueAt( row, i);
				if ( null == r)
					continue;

				if ( !r._or) {
					setIcon( null);
					return table.getBackground();
				} else {
					setIcon( ( null == rule) ? null : _icon);
					return _orBackgroundColor;
				}
			}

			setIcon( null);
			return table.getBackground();
		}
	}
}
