/**
 * 
 */
package soars.application.manager.model.main.panel.tab.contents;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import soars.application.manager.model.main.ResourceManager;
import soars.common.soars.property.Property;

/**
 * @author kurata
 *
 */
public class SoarsContentsTableCellRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	static private Color _simulationBackgroundColor = new Color( /*255, 255, 108*/255, 240, 240);

	/**
	 * 
	 */
	static private Color _animatorBackgroundColor = new Color( 255, 255, 108);

	/**
	 * 
	 */
	public SoarsContentsTableCellRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
		Property property = ( Property)arg1;

		String text = "";
		if ( null != property)
			text = (  !property._title.equals( "")
				? property._title/* + " - [" + new Timestamp( Long.parseLong( property._id)).toString() + "]"*/
				: ResourceManager.get_instance().get( "soars.contents.table.no.title")/* + " - [" + new Timestamp( Long.parseLong( property._id)).toString() + "]"*/);

		setOpaque( true);

		if ( arg0.hasFocus()) {
			if ( arg2) {
				setForeground( Color.white);
				setBackground( arg0.getSelectionBackground());
				//setBackground( ( null == cellDataBase) ? arg0.getSelectionBackground() : get_color( rule, _role));
			} else {
				setForeground( arg0.getForeground());
				//setForeground( ( null == cellDataBase) ? arg0.getForeground() : get_color( rule, _role));
				setBackground( get_background_color( arg0, property));
			}
		} else {
			if ( arg2) {
				setForeground( Color.white);
				setBackground( arg0.getSelectionBackground());
				//setBackground( ( null == cellDataBase) ? arg0.getSelectionBackground() : get_color( rule, _role));
			} else {
				setForeground( arg0.getForeground());
				//setForeground( ( null == cellDataBase) ? arg0.getForeground() : ( get_color( rule, _role)));
				setBackground( get_background_color( arg0, property));
			}
		}

		setText( text);

		return this;
	}

	/**
	 * @param table
	 * @param property
	 * @return
	 */
	private Color get_background_color(JTable table, Property property) {
		if ( null == property)
			return table.getBackground();

		return ( ( property._type.equals( "simulation")) ? _simulationBackgroundColor : _animatorBackgroundColor);
	}
}
