/**
 * 
 */
package soars.application.visualshell.object.arbitrary.edit;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import soars.application.visualshell.common.menu.basic1.AppendAction;
import soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1;
import soars.application.visualshell.common.menu.basic1.RemoveAction;
import soars.application.visualshell.common.swing.TableBase;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.arbitrary.JarFileProperties;
import soars.application.visualshell.object.arbitrary.JavaClasses;
import soars.common.utility.swing.table.base.StandardTableHeaderRenderer;
import soars.common.utility.swing.table.base.StandardTableRowRenderer;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.xml.dom.XmlTool;

/**
 * The table component to edit the java class list.
 * @author kurata / SOARS project
 */
public class JavaClassesTable extends TableBase implements IBasicMenuHandler1 {

	/**
	 * 
	 */
	private String[] _javaClasses = new String[ 0];

	/**
	 * 
	 */
	protected JMenuItem _appendMenuItem = null;

	/**
	 * 
	 */
	protected JMenuItem _removeMenuItem = null;

	/**
	 * Creates this object.
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public JavaClassesTable(Frame owner, Component parent) {
		super(owner, parent);
	}

	/**
	 * Returns true if this object is initialized successfully.
	 * @return true if this object is initialized successfully
	 */
	public boolean setup() {
		if ( !super.setup( true))
			return false;


		getTableHeader().setReorderingAllowed( false);
		setDefaultEditor( Object.class, null);


		setAutoResizeMode( AUTO_RESIZE_OFF);


		JTableHeader tableHeader = getTableHeader();
		StandardTableHeaderRenderer standardTableHeaderRenderer = new StandardTableHeaderRenderer();

		tableHeader.setDefaultRenderer( standardTableHeaderRenderer);

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 1);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue(
			ResourceManager.get_instance().get( "edit.java.classes.dialog.java.classes.table.header.name"));

		for ( int i = 0; i < defaultTableColumnModel.getColumnCount(); ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new StandardTableRowRenderer());
		}

		TableColumn tableColumn = defaultTableColumnModel.getColumn( 0);
		tableColumn.setPreferredWidth( 638);


		if ( !setup( defaultTableModel))
			return false;

		return true;
	}

	/**
	 * @param defaultTableModel
	 * @return
	 */
	private boolean setup(DefaultTableModel defaultTableModel) {
		Node node = JarFileProperties.get_instance().get_jarfile_node( Constant._javaClasses);
		if ( null == node)
			return false;

		NodeList nodeList = XmlTool.get_node_list( node, "class");
		if ( null == nodeList || 0 == nodeList.getLength())
			return true;

		List<String> list = new ArrayList<String>();
		for ( int i = 0; i < nodeList.getLength(); ++i) {
			Node childNode = nodeList.item( i);
			String name = XmlTool.get_attribute( childNode, "name");
			if ( null != name)
				list.add( name);
		}

		_javaClasses = ( String[])list.toArray( new String[ 0]);
		Arrays.sort( _javaClasses);

		Object[] objects = new Object[ 1]; 
		for ( int i = 0; i < _javaClasses.length; ++i) {
			objects[ 0] = _javaClasses[ i];
			defaultTableModel.addRow( objects);
		}

		if ( 0 < defaultTableModel.getRowCount())
			setRowSelectionInterval( 0, 0);

		return true;
	}

	/**
	 * @param width
	 */
	protected void setup_column_width(int width) {
		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		TableColumn tableColumn = defaultTableColumnModel.getColumn( 0);
		tableColumn.setPreferredWidth( width);
	}

	/**
	 * @param width
	 */
	protected void adjust_column_width(int width) {
		if ( getPreferredSize().width >= width)
			return;

		setup_column_width( width);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#setup_popup_menu()
	 */
	protected void setup_popup_menu() {
		super.setup_popup_menu();

		_appendMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.append.menu"),
			new AppendAction( ResourceManager.get_instance().get( "common.popup.menu.append.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.append.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.append.stroke"));
		_removeMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.remove.menu"),
			new RemoveAction( ResourceManager.get_instance().get( "common.popup.menu.remove.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.remove.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.remove.stroke"));
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.swing.TableBase#on_mouse_right_up(java.awt.Point)
	 */
	public void on_mouse_right_up(Point point) {
		if ( null == _userInterface)
			return;

		_removeMenuItem.setEnabled( true);

		//int index = getSelectedRow();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount() /*|| -1 == index*/) {
			_removeMenuItem.setEnabled( false);
		} else {
			int row = rowAtPoint( point);
			int column = columnAtPoint( point);
			if ( ( 0 <= row && getRowCount() > row)
				&& ( 0 <= column && getColumnCount() > column)) {
				//setRowSelectionInterval( row, row);
				//setColumnSelectionInterval( column, column);
				int[] rows = getSelectedRows();
				boolean contains = ( 0 <= Arrays.binarySearch( rows, row));
				_removeMenuItem.setEnabled( contains);
			} else {
				_removeMenuItem.setEnabled( false);
			}
		}

		_popupMenu.show( this, point.x, point.y);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#on_key_pressed(java.awt.event.KeyEvent)
	 */
	protected void on_key_pressed(KeyEvent keyEvent) {
		int row = getSelectedRow();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount() || -1 == row)
			return;

		switch ( keyEvent.getKeyCode()) {
			case KeyEvent.VK_DELETE:
			case KeyEvent.VK_BACK_SPACE:
				on_remove( null);
				break;
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_append(java.awt.event.ActionEvent)
	 */
	public void on_append(ActionEvent actionEvent) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();

		List<String> javaClassList = new ArrayList();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i)
			javaClassList.add( ( String)defaultTableModel.getValueAt( i, 0));

		SelectJavaClassDlg selectJavaClassDlg = new SelectJavaClassDlg(
			_owner,
			ResourceManager.get_instance().get( "select.java.class.dialog.title"),
			true,
			javaClassList.toArray( new String[ 0]));

		if ( !selectJavaClassDlg.do_modal( _parent))
			return;

		if( null == selectJavaClassDlg._selectedJavaClasses
			|| selectJavaClassDlg._selectedJavaClasses.isEmpty())
			return;

		while ( 0 < defaultTableModel.getRowCount())
			defaultTableModel.removeRow( 0);

		String selectedJavaClass = selectJavaClassDlg._selectedJavaClasses.get( 0);

		javaClassList.addAll( selectJavaClassDlg._selectedJavaClasses);

		String[] javaClasses = javaClassList.toArray( new String[ 0]);

		Arrays.sort( javaClasses);

		int row = -1;
		Object[] objects = new Object[ 1]; 
		for ( int i = 0; i < javaClasses.length; ++i) {
			objects[ 0] = javaClasses[ i];
			defaultTableModel.addRow( objects);
			//if ( selectJavaClassDlg._selected_java_classes.contains( java_classes[ i]))
			if ( javaClasses[ i].equals( selectedJavaClass))
				row = i;
		}

		if ( 0 <= row) {
			setRowSelectionInterval( row, row);
			Rectangle rect = getCellRect( row, 0, true);
			scrollRectToVisible( rect);
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_edit(java.awt.event.ActionEvent)
	 */
	public void on_edit(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_insert(java.awt.event.ActionEvent)
	 */
	public void on_insert(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_remove(java.awt.event.ActionEvent)
	 */
	public void on_remove(ActionEvent actionEvent) {
		int[] rows = getSelectedRows();
		if ( 1 > rows.length)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		if ( 0 == defaultTableModel.getRowCount())
			return;

		if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
			_parent,
			ResourceManager.get_instance().get( "edit.java.classes.dialog.java.classes.table.confirm.remove.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION)) {

			rows = Tool.quick_sort_int( rows, true);
			for ( int i = rows.length - 1; i >= 0; --i) {
				String classname = ( String)defaultTableModel.getValueAt( rows[ i], 0);
				if ( LayerManager.get_instance().uses_this_class( Constant._javaClasses, classname)
					&& JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(
						_parent,
						classname
							+ "\n"
							+ ResourceManager.get_instance().get( "edit.java.classes.dialog.java.classes.table.is.now.used.message")
							+ "\n"
							+ ResourceManager.get_instance().get( "edit.java.classes.dialog.java.classes.table.confirm.remove.message"),
						ResourceManager.get_instance().get( "application.title"),
						JOptionPane.YES_NO_OPTION))
					continue;

				defaultTableModel.removeRow( rows[ i]);
			}

			if ( 0 < defaultTableModel.getRowCount()) {
				if ( rows[ 0] < defaultTableModel.getRowCount())
					setRowSelectionInterval( rows[ 0], rows[ 0]);
				else
					setRowSelectionInterval( defaultTableModel.getRowCount() - 1, defaultTableModel.getRowCount() - 1);
			}
		}
	}

	/**
	 * 
	 */
	protected void on_ok() {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();

		List<String> javaClassList = new ArrayList<String>();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i)
			javaClassList.add( ( String)defaultTableModel.getValueAt( i, 0));

		String[] javaClasses = javaClassList.toArray( new String[ 0]);

		if ( !changed( javaClasses))
			return;

		// update java_classes.xml
		if ( !JavaClasses.create_java_classes_xml_file( javaClasses))
			return;

		// update DOM
		JarFileProperties.get_instance().update_java_classes();
	}

	/**
	 * @param javaClasses
	 * @return
	 */
	private boolean changed(String[] javaClasses) {
		if ( javaClasses.length != _javaClasses.length)
			return true;

		for ( int i = 0; i < javaClasses.length; ++i) {
			if ( !javaClasses[ i].equals( _javaClasses[ i]))
				return true;
		}

		return false;
	}
}
