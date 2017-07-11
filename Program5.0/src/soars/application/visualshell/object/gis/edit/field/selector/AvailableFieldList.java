/**
 * 
 */
package soars.application.visualshell.object.gis.edit.field.selector;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import soars.application.visualshell.common.swing.ListBase;

/**
 * @author kurata
 *
 */
public class AvailableFieldList extends ListBase {

	/**
	 * 
	 */
	private SelectedFieldList _selectedFieldList = null;

	/**
	 * @param selectedFieldList 
	 * @param owner
	 * @param parent
	 */
	public AvailableFieldList(SelectedFieldList selectedFieldList, Frame owner, Component parent) {
		super(owner, parent);
		_selectedFieldList = selectedFieldList;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.swing.ListBase#on_edit(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_edit(ActionEvent actionEvent) {
		int[] indices = getSelectedIndices();
		if ( 1 != indices.length)
			return;

		_selectedFieldList.addElement( get_fields());
	}

	/**
	 * @return
	 */
	public Field[] get_fields() {
    List<Object> values = getSelectedValuesList();
		List<Field> fields = new ArrayList<Field>();
		for ( Object value:values)
			fields.add( new Field( ( String)value));

		return fields.toArray( new Field[ 0]);
	}
}
