/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.simple;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.gis.edit.field.selector.Field;
import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;
import soars.application.visualshell.object.gis.object.base.GisSimpleVariableObject;
import soars.application.visualshell.object.gis.object.keyword.GisKeywordObject;
import soars.application.visualshell.object.gis.object.number.GisNumberObject;
import soars.application.visualshell.object.gis.object.spot.GisSpotVariableObject;
import soars.application.visualshell.object.gis.object.time.GisTimeVariableObject;

/**
 * @author kurata
 *
 */
public class GisSimpleVariableTableRowRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	public GisSimpleVariableTableRowRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {

		setOpaque( true);

		String text = "";
		if ( null != arg1 && arg1 instanceof GisSimpleVariableObject) {
			GisSimpleVariableObject gisSimpleVariableObject = ( GisSimpleVariableObject)arg1;
			switch ( arg0.convertColumnIndexToModel( arg5)) {
				case 0:
//					if ( arg1 instanceof GisProbabilityObject)
//						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.probability");
					if ( arg1 instanceof GisKeywordObject)
						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword");
					else if ( arg1 instanceof GisNumberObject)
						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object");
//					else if ( arg1 instanceof GisRoleVariableObject)
//						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable");
					else if ( arg1 instanceof GisTimeVariableObject)
						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable");
					else if ( arg1 instanceof GisSpotVariableObject)
						text = ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable");
					break;
				case 1:
					text = gisSimpleVariableObject._name;
					break;
				case 2:
					if ( gisSimpleVariableObject instanceof GisSpotVariableObject) {
						GisSpotVariableObject gisSpotVariableObject = ( GisSpotVariableObject)gisSimpleVariableObject;
						text = gisSpotVariableObject._initialValue;
					} else
						text = Field.get( gisSimpleVariableObject._fields);
					break;
				case 3:
					if ( gisSimpleVariableObject instanceof GisNumberObject) {
						GisNumberObject gisNumberObject = ( GisNumberObject)gisSimpleVariableObject;
						text = GisNumberObject.get_type_name( gisNumberObject._type);
					}
					break;
				case 4:
					text = gisSimpleVariableObject._comment;
			}

//			if ( arg1 instanceof GisProbabilityObject) {
//				setForeground( arg2 ? Color.white : GisPropertyPanelBase.get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.probability")));
//				setBackground( arg2 ? GisPropertyPanelBase.get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.probability")) : table.getBackground());
			if ( arg1 instanceof GisKeywordObject) {
				setForeground( arg2 ? Color.white : GisPropertyPanelBase.get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword")));
				setBackground( arg2 ? GisPropertyPanelBase.get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword")) : arg0.getBackground());
			} else if ( arg1 instanceof GisNumberObject) {
				setForeground( arg2 ? Color.white : GisPropertyPanelBase.get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object")));
				setBackground( arg2 ? GisPropertyPanelBase.get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object")) : arg0.getBackground());
//			} else if ( arg1 instanceof GisRoleVariableObject) {
//				setForeground( arg2 ? Color.white : GisPropertyPanelBase.get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable")));
//				setBackground( arg2 ? GisPropertyPanelBase.get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.role.variable")) : table.getBackground());
			} else if ( arg1 instanceof GisTimeVariableObject) {
				setForeground( arg2 ? Color.white : GisPropertyPanelBase.get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable")));
				setBackground( arg2 ? GisPropertyPanelBase.get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.time.variable")) : arg0.getBackground());
			} else if ( arg1 instanceof GisSpotVariableObject) {
				setForeground( arg2 ? Color.white : GisPropertyPanelBase.get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable")));
				setBackground( arg2 ? GisPropertyPanelBase.get_colorMap().get( ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable")) : arg0.getBackground());
			}
//			setForeground( arg2 ? SystemColor.textHighlightText : SystemColor.textText);
//			setBackground( arg2 ? SystemColor.textHighlight : SystemColor.text);
		}

		setText( text);

		return this;
	}
}
