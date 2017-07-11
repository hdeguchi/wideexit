/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.exchange_algebra.initial;

import java.awt.Color;
import java.util.Map;

import javax.swing.JLabel;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.exchange_algebra.initial.base.ExchangeAlgebraInitialValueTextPanelBase;

/**
 * @author kurata
 *
 */
public class SubjectPanel extends ExchangeAlgebraInitialValueTextPanelBase {

	/**
	 * @param color
	 * @param propertyPanelBaseMap
	 * @param entityBase
	 */
	public SubjectPanel(Color color, EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap) {
		super(color, entityBase, propertyPanelBaseMap);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.variable.panel.exchange_algebra.initial.base.ExchangeAlgebraInitialValueTextPanelBase#create_label()
	 */
	@Override
	protected JLabel create_label() {
		return new JLabel( ResourceManager.get_instance().get( "edit.exchange.algebra.value.dialog.edit.subject"));
	}
}
