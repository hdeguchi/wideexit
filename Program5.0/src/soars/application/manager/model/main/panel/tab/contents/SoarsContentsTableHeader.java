/**
 * 
 */
package soars.application.manager.model.main.panel.tab.contents;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.table.TableColumnModel;

import soars.common.utility.swing.table.base.StandardTableHeader;

/**
 * @author kurata
 *
 */
public class SoarsContentsTableHeader extends StandardTableHeader {

	/**
	 * @param tableColumnModel
	 * @param owner
	 * @param parent
	 */
	public SoarsContentsTableHeader(TableColumnModel tableColumnModel, Frame owner, Component parent) {
		super(tableColumnModel, owner, parent);
	}
}
