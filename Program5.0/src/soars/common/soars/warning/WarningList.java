/*
 * 2005/06/22
 */
package soars.common.soars.warning;

import java.awt.Component;
import java.awt.Frame;
import java.util.Vector;

import javax.swing.ListSelectionModel;

import soars.common.utility.swing.list.StandardList;
import soars.common.utility.swing.list.StandardListCellRenderer;

/**
 * @author kurata
 */
public class WarningList extends StandardList {

	/**
	 * @param owner
	 * @param parent
	 */
	public WarningList(Frame owner, Component parent) {
		super(owner, parent);
	}

	/**
	 * @return
	 */
	public boolean setup() {
		if ( !super.setup(false))
			return false;

		setSelectionMode( ListSelectionModel.SINGLE_SELECTION);

		Vector<String> messages = WarningManager.get_instance().get( ", ");
		for ( int i = 0; i < messages.size(); ++i) {
			String message = messages.get( i);
			_defaultComboBoxModel.addElement( message);
		}

		setCellRenderer( new StandardListCellRenderer());

		setSelectedIndex( 0);

		return true;
	}
}
