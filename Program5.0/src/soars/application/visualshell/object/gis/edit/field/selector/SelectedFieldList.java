/**
 * 
 */
package soars.application.visualshell.object.gis.edit.field.selector;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPopupMenu;

import soars.application.visualshell.common.menu.basic1.RemoveAction;
import soars.application.visualshell.common.swing.ListBase;
import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.swing.gui.UserInterface;

/**
 * @author kurata
 *
 */
public class SelectedFieldList extends ListBase {

	/**
	 * @param owner
	 * @param parent
	 */
	public SelectedFieldList(Frame owner, Component parent) {
		super(owner, parent);
	}

	/**
	 * @return
	 */
	public boolean setup() {
		if ( !super.setup( true))
			return false;

		setCellRenderer( new SelectedFieldListCellRenderer());

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.swing.ListBase#setup_popup_menu()
	 */
	@Override
	protected void setup_popup_menu() {
		_userInterface = new UserInterface();
		_popupMenu = new JPopupMenu();

		_removeMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.remove.menu"),
			new RemoveAction( ResourceManager.get_instance().get( "common.popup.menu.remove.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.remove.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.remove.stroke"));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.swing.ListBase#on_mouse_right_up(java.awt.event.MouseEvent)
	 */
	@Override
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
		if ( null == _userInterface)
			return;

		_removeMenuItem.setEnabled( true);

		if ( 0 == _defaultComboBoxModel.getSize()) {
			_removeMenuItem.setEnabled( false);
		} else {
			int index = locationToIndex( mouseEvent.getPoint());
			if ( 0 <= index && _defaultComboBoxModel.getSize() > index
				&& getCellBounds( index, index).contains( mouseEvent.getPoint())) {
				int[] indices = getSelectedIndices();
				boolean contains = ( 0 <= Arrays.binarySearch( indices, index));
				_removeMenuItem.setEnabled( contains);
			} else {
				_removeMenuItem.setEnabled( false);
			}
		}

		_popupMenu.show( this, mouseEvent.getX(), mouseEvent.getY());
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.swing.ListBase#on_remove(java.awt.event.ActionEvent)
	 */
	@Override
	public void on_remove(ActionEvent actionEvent) {
		int[] indices = getSelectedIndices();
		if ( 1 > indices.length)
			return;

		Arrays.sort( indices);
		for ( int i = indices.length - 1; 0 <= i; --i)
			_defaultComboBoxModel.removeElementAt( indices[ i]);

		if ( 0 < _defaultComboBoxModel.getSize()) {
			int index = ( ( indices[ 0] < _defaultComboBoxModel.getSize()) ? indices[ 0] : _defaultComboBoxModel.getSize() - 1);
			setSelectedIndex( index);
		}
	}

	/**
	 * @param field
	 */
	public void addElement(Field field) {
		_defaultComboBoxModel.addElement( field);
	}

	/**
	 * @param fields
	 */
	public void addElement(Field[] fields) {
		for ( Field field:fields)
			addElement( field);

		int[] indices = new int[ fields.length];
		for ( int i = 0; i < indices.length; ++i)
			indices[ i] = _defaultComboBoxModel.getSize() - indices.length + i;
		clearSelection();
		setSelectedIndices( indices);
	}

	/**
	 * @param field
	 */
	public void insertElement(Field field) {
		int[] indices = getSelectedIndices();
		if ( 1 != indices.length)
			return;

		insertElementAt( field, indices[ 0]);
	}

	/**
	 * @param fields
	 */
	public void insertElement(Field[] fields) {
		int[] indices = getSelectedIndices();
		if ( 1 != indices.length)
			return;

		for ( int i = fields.length - 1; 0 <= i; --i)
			insertElementAt( fields[ i], indices[ 0]);

		int start = indices[ 0];
		indices = new int[ fields.length];
		for ( int i = 0; i < indices.length; ++i)
			indices[ i] = start++;
		clearSelection();
		setSelectedIndices( indices);
	}

	/**
	 * @param object
	 * @param index
	 */
	public void insertElementAt(Object object, int index) {
		_defaultComboBoxModel.insertElementAt( object, index);
	}

	/**
	 * 
	 */
	public void on_up() {
		int[] indices = getSelectedIndices();
		if ( 1 > indices.length)
			return;

		Arrays.sort( indices);
		if ( 0 == indices[ 0])
			return;

		exchange_on_up( indices);

		clearSelection();
		for ( int i = 0; i < indices.length; ++i)
			--indices[ i];
		setSelectedIndices( indices);
	}

	/**
	 * @param indices
	 */
	public void exchange_on_up(int[] indices) {
		for ( int i = 0; i < indices.length; ++i)
			exchange( indices[ i] - 1, indices[ i]);
	}

	/**
	 * 
	 */
	public void on_down() {
		int[] indices = getSelectedIndices();
		if ( 1 > indices.length)
			return;

		Arrays.sort( indices);
		if ( _defaultComboBoxModel.getSize() - 1 == indices[ indices.length - 1])
			return;

		exchange_on_down( indices);

		clearSelection();
		for ( int i = 0; i < indices.length; ++i)
			++indices[ i];
		setSelectedIndices( indices);
	}

	/**
	 * @param indices
	 */
	public void exchange_on_down(int[] indices) {
		for ( int i = indices.length - 1; i >= 0 ; --i)
			exchange( indices[ i], indices[ i] + 1);
	}

	/**
	 * @param i
	 * @param j
	 */
	protected void exchange(int i, int j) {
		Object[] objects = new Object[] { _defaultComboBoxModel.getElementAt( i), _defaultComboBoxModel.getElementAt( j)};
		_defaultComboBoxModel.removeElementAt( j);
		_defaultComboBoxModel.removeElementAt( i);
		_defaultComboBoxModel.insertElementAt( objects[ 1], i);
		_defaultComboBoxModel.insertElementAt( objects[ 0], j);
	}

	/**
	 * @param selectedFields
	 */
	public void on_ok(List<Field> selectedFields) {
		selectedFields.clear();
		for ( int i = 0; i < _defaultComboBoxModel.getSize(); ++i)
			selectedFields.add( ( Field)_defaultComboBoxModel.getElementAt( i));
	}
}
