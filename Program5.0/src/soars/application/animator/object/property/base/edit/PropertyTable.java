/*
 * 2005/03/10
 */
package soars.application.animator.object.property.base.edit;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.TreeMap;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import soars.application.animator.common.font.FontSizeListCellRenderer;
import soars.application.animator.common.font.SelectFontFamilyDlg;
import soars.application.animator.common.font.SelectFontSizeDlg;
import soars.application.animator.common.font.SelectFontStyleDlg;
import soars.application.animator.common.menu.basic1.ChangeFontFamilyAction;
import soars.application.animator.common.menu.basic1.ChangeFontSizeAction;
import soars.application.animator.common.menu.basic1.ChangeFontStyleAction;
import soars.application.animator.common.menu.basic1.ChangeImageColorAction;
import soars.application.animator.common.menu.basic1.ChangeImagefileAction;
import soars.application.animator.common.menu.basic1.ChangeTextColorAction;
import soars.application.animator.common.menu.basic1.IBasicMenuHandler1;
import soars.application.animator.common.menu.basic1.InvisibleAction;
import soars.application.animator.common.menu.basic1.RemoveImagefileAction;
import soars.application.animator.common.menu.basic1.VisibleAction;
import soars.application.animator.common.tool.CommonTool;
import soars.application.animator.main.ResourceManager;
import soars.application.animator.object.common.edit.EditSelectedImageDlg;
import soars.application.animator.object.property.base.PropertyBase;
import soars.common.utility.swing.color.ColorDlg;
import soars.common.utility.swing.table.base.StandardTable;

/**
 * The component to edit the properties.
 * @author kurata / SOARS project
 */
public class PropertyTable extends StandardTable implements IBasicMenuHandler1 {

	/**
	 * 
	 */
	private String _openDirectoryKey = "";

	/**
	 * Creates this component with the specified data.
	 * @param openDirectoryKey the key mapped to the default directory for the file chooser dialog
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public PropertyTable(String openDirectoryKey, Frame owner, Component parent) {
		super(owner, parent);
		_openDirectoryKey = openDirectoryKey;
	}

	/**
	 * Returns true if this component is initialized successfully.
	 * @param propertyMap the PropertyBase hashtable(value(String) - PropertyBase)
	 * @return true if this component is initialized successfully
	 */
	public boolean setup(TreeMap<String, PropertyBase> propertyMap) {
		if ( !super.setup( true))
			return false;


		getTableHeader().setReorderingAllowed( false);
		setDefaultEditor( Object.class, null);


		JTableHeader tableHeader = getTableHeader();
		tableHeader.setDefaultRenderer( new PropertyTableHeaderRenderer());


		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( 8);


		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue(
			ResourceManager.get_instance().get( "property.table.header.value"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue(
			ResourceManager.get_instance().get( "property.table.header.visible"));
		defaultTableColumnModel.getColumn( 2).setHeaderValue(
			ResourceManager.get_instance().get( "property.table.header.image.color"));
		defaultTableColumnModel.getColumn( 3).setHeaderValue(
			ResourceManager.get_instance().get( "property.table.header.text.color"));
		defaultTableColumnModel.getColumn( 4).setHeaderValue(
			ResourceManager.get_instance().get( "property.table.header.font.family"));
		defaultTableColumnModel.getColumn( 5).setHeaderValue(
			ResourceManager.get_instance().get( "property.table.header.font.style"));
		defaultTableColumnModel.getColumn( 6).setHeaderValue(
			ResourceManager.get_instance().get( "property.table.header.font.size"));
		defaultTableColumnModel.getColumn( 7).setHeaderValue(
			ResourceManager.get_instance().get( "property.table.header.imagefile.name"));


		for ( int i = 0; i < 8; ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			if ( 1 == i)
				tableColumn.setCellRenderer( new PropertyCheckBoxTableCellRenderer());
			else
				tableColumn.setCellRenderer( new PropertyTableCellRenderer());
		}


		JComboBox comboBox = CommonTool.get_font_family_combo_box();
		TableColumn tableColumn = defaultTableColumnModel.getColumn( 4);
		DefaultCellEditor defaultCellEditor = new DefaultCellEditor( comboBox);
		tableColumn.setCellEditor( defaultCellEditor);


		comboBox = CommonTool.get_font_style_combo_box();
		tableColumn = defaultTableColumnModel.getColumn( 5);
		defaultCellEditor = new DefaultCellEditor( comboBox);
		tableColumn.setCellEditor( defaultCellEditor);


		comboBox = CommonTool.get_font_size_combo_box();
		comboBox.setRenderer( new FontSizeListCellRenderer());
		tableColumn = defaultTableColumnModel.getColumn( 6);
		defaultCellEditor = new DefaultCellEditor( comboBox);
		tableColumn.setCellEditor( defaultCellEditor);


		PropertyBase[] propertyBases = propertyMap.values().toArray( new PropertyBase[ 0]);
		Arrays.sort( propertyBases, new PropertyComparator( true, false));

		Object[] objects = new Object[ 8];
		for ( int i = 0; i < propertyBases.length; ++i) {
			objects[ 0] = propertyBases[ i]._value;
			objects[ 1] = new JCheckBox( "", propertyBases[ i]._visible);
			objects[ 2] = new Color( propertyBases[ i]._imageR, propertyBases[ i]._imageG, propertyBases[ i]._imageB);
			objects[ 3] = new Color( propertyBases[ i]._textR, propertyBases[ i]._textG, propertyBases[ i]._textB);
			objects[ 4] = propertyBases[ i]._font.getFamily();
			objects[ 5] = CommonTool.get_font_style( propertyBases[ i]._font.getStyle());
			objects[ 6] = String.valueOf( propertyBases[ i]._font.getSize());
			objects[ 7] = propertyBases[ i]._imageFilename;
			defaultTableModel.addRow( objects);
		}


		setRowSelectionInterval( 0, 0);


		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#setup_popup_menu()
	 */
	protected void setup_popup_menu() {
		super.setup_popup_menu();

		JMenuItem item = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "property.table.visible.menu"),
			new VisibleAction( ResourceManager.get_instance().get( "object.table.visible.menu"), this),
			ResourceManager.get_instance().get( "property.table.visible.mnemonic"),
			ResourceManager.get_instance().get( "property.table.visible.stroke"));
		item = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "property.table.invisible.menu"),
			new InvisibleAction( ResourceManager.get_instance().get( "object.table.invisible.menu"), this),
			ResourceManager.get_instance().get( "property.table.invisible.mnemonic"),
			ResourceManager.get_instance().get( "property.table.invisible.stroke"));

		_popupMenu.addSeparator();

		item = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "property.table.change.image.color.menu"),
			new ChangeImageColorAction( ResourceManager.get_instance().get( "property.table.change.image.color.menu"), this),
			ResourceManager.get_instance().get( "property.table.change.image.color.mnemonic"),
			ResourceManager.get_instance().get( "property.table.change.image.color.stroke"));
		item = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "property.table.change.text.color.menu"),
			new ChangeTextColorAction( ResourceManager.get_instance().get( "property.table.change.text.color.menu"), this),
			ResourceManager.get_instance().get( "property.table.change.text.color.mnemonic"),
			ResourceManager.get_instance().get( "property.table.change.text.color.stroke"));

		_popupMenu.addSeparator();

		item = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "property.table.change.font.family.menu"),
			new ChangeFontFamilyAction( ResourceManager.get_instance().get( "property.table.change.font.family.menu"), this),
			ResourceManager.get_instance().get( "property.table.change.font.family.mnemonic"),
			ResourceManager.get_instance().get( "property.table.change.font.family.stroke"));
		item = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "property.table.change.font.style.menu"),
			new ChangeFontStyleAction( ResourceManager.get_instance().get( "property.table.change.font.style.menu"), this),
			ResourceManager.get_instance().get( "property.table.change.font.style.mnemonic"),
			ResourceManager.get_instance().get( "property.table.change.font.style.stroke"));
		item = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "property.table.change.font.size.menu"),
			new ChangeFontSizeAction( ResourceManager.get_instance().get( "property.table.change.font.size.menu"), this),
			ResourceManager.get_instance().get( "property.table.change.font.size.mnemonic"),
			ResourceManager.get_instance().get( "property.table.change.font.size.stroke"));

		_popupMenu.addSeparator();

		item = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "property.table.change.imagefile.menu"),
			new ChangeImagefileAction( ResourceManager.get_instance().get( "property.table.change.imagefile.menu"), this),
			ResourceManager.get_instance().get( "property.table.change.imagefile.mnemonic"),
			ResourceManager.get_instance().get( "property.table.change.imagefile.stroke"));
		item = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "property.table.remove.imagefile.menu"),
			new RemoveImagefileAction( ResourceManager.get_instance().get( "property.table.remove.imagefile.menu"), this),
			ResourceManager.get_instance().get( "property.table.remove.imagefile.mnemonic"),
			ResourceManager.get_instance().get( "property.table.remove.imagefile.stroke"));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#on_mouse_pressed(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_pressed(MouseEvent mouseEvent) {
		int row = rowAtPoint( mouseEvent.getPoint());
		if ( 0 > row || getRowCount() <= row)
			return;

		int column = columnAtPoint( mouseEvent.getPoint());
		if ( 0 > column || getColumnCount() <= column)
			return;

		column = convertColumnIndexToModel( column);

		switch ( column) {
			case 1:
				JCheckBox checkBox = ( JCheckBox)getModel().getValueAt( row, column);
				checkBox.setSelected( !checkBox.isSelected());
				break;
		}

		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#on_mouse_left_double_click(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_double_click(MouseEvent mouseEvent) {
		int row = rowAtPoint( mouseEvent.getPoint());
		if ( 0 > row || getRowCount() <= row)
			return;

		int column = columnAtPoint( mouseEvent.getPoint());
		if ( 0 > column || getColumnCount() <= column)
			return;

		column = convertColumnIndexToModel( column);

		switch ( column) {
			case 2:
				on_change_color( row, column, "property.table.image.color.dialog.title");
				break;
			case 3:
				on_change_color( row, column, "property.table.text.color.dialog.title");
				break;
			case 7:
				on_change_imagefile( row, column);
				break;
		}
	}

	/**
	 * @param row
	 * @param column
	 * @param titleKey
	 */
	private void on_change_color(int row, int column, String titleKey) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		String value = ( String)defaultTableModel.getValueAt( row, 0);
		Color color = ( Color)defaultTableModel.getValueAt( row, column);
		color = ColorDlg.showDialog( _owner,
			ResourceManager.get_instance().get( titleKey)
				+ " - " + value,
			color,
			getParent(),
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			ResourceManager.get_instance().get( "make.color"));
//		color = JColorChooser.showDialog( getParent(),
//			ResourceManager.get_instance().get( title_key)
//				+ " - " + value,
//			color);
		if ( null == color)
			return;
	
		defaultTableModel.setValueAt( color, row, column);
		repaint();
	}

	/**
	 * @param row
	 * @param column
	 */
	private void on_change_imagefile(int row, int column) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		String value = ( String)defaultTableModel.getValueAt( row, 0);
		EditSelectedImageDlg editSelectedImageDlg = new EditSelectedImageDlg( _owner,
			ResourceManager.get_instance().get( "select.imagefile.dialog.title") + " - " + value,
			true, _openDirectoryKey, ( String)defaultTableModel.getValueAt( row, column));
		if ( !editSelectedImageDlg.do_modal())
			return;

		defaultTableModel.setValueAt( editSelectedImageDlg._newImageFilename, row, column);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.table.StandardTable#on_mouse_right_up(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
		int[] indices = getSelectedRows();
		if ( 0 == indices.length)
			return;

		int row = rowAtPoint( mouseEvent.getPoint());
		if ( !isRowSelected( row))
			return;

		_popupMenu.show( this, mouseEvent.getX(), mouseEvent.getY());
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_visible(java.awt.event.ActionEvent)
	 */
	public void on_visible(ActionEvent actionEvent) {
		int[] indices = getSelectedRows();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < indices.length; ++i) {
			JCheckBox checkbox = ( JCheckBox)defaultTableModel.getValueAt( indices[ i], 1);
			checkbox.setSelected( true);
		}

		repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_invisible(java.awt.event.ActionEvent)
	 */
	public void on_invisible(ActionEvent actionEvent) {
		int[] indices = getSelectedRows();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < indices.length; ++i) {
			JCheckBox checkbox = ( JCheckBox)defaultTableModel.getValueAt( indices[ i], 1);
			checkbox.setSelected( false);
		}

		repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_visible_name(java.awt.event.ActionEvent)
	 */
	public void on_visible_name(ActionEvent actionEvent) {
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_invisible_name(java.awt.event.ActionEvent)
	 */
	public void on_invisible_name(ActionEvent actionEvent) {
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_change_image_color(java.awt.event.ActionEvent)
	 */
	public void on_change_image_color(ActionEvent actionEvent) {
		int[] indices = getSelectedRows();
		if ( 0 == indices.length)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();

		Color color = ColorDlg.showDialog( _owner,
			ResourceManager.get_instance().get( "property.table.image.color.dialog.title"),
			( Color)defaultTableModel.getValueAt( indices[ 0], 2),
			getParent(),
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			ResourceManager.get_instance().get( "make.color"));
//		Color color = JColorChooser.showDialog( getParent(),
//			ResourceManager.get_instance().get( "property.table.image.color.dialog.title"),
//			( Color)defaultTableModel.getValueAt( indices[ 0], 2));
		if ( null == color)
			return;
	
		for ( int i = 0; i < indices.length; ++i)
			defaultTableModel.setValueAt( color, indices[ i], 2);

		repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_change_text_color(java.awt.event.ActionEvent)
	 */
	public void on_change_text_color(ActionEvent actionEvent) {
		int[] indices = getSelectedRows();
		if ( 0 == indices.length)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();

		Color color = ColorDlg.showDialog( _owner,
			ResourceManager.get_instance().get( "property.table.text.color.dialog.title"),
			( Color)defaultTableModel.getValueAt( indices[ 0], 3),
			getParent(),
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			ResourceManager.get_instance().get( "make.color"));
//		Color color = JColorChooser.showDialog( getParent(),
//			ResourceManager.get_instance().get( "property.table.text.color.dialog.title"),
//			( Color)defaultTableModel.getValueAt( indices[ 0], 3));
		if ( null == color)
			return;
	
		for ( int i = 0; i < indices.length; ++i)
			defaultTableModel.setValueAt( color, indices[ i], 3);

		repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_change_font_family(java.awt.event.ActionEvent)
	 */
	public void on_change_font_family(ActionEvent actionEvent) {
		int[] indices = getSelectedRows();
		if ( 0 == indices.length)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();

		SelectFontFamilyDlg selectFontFamilyDlg
			= new SelectFontFamilyDlg( _owner, true,
				( String)defaultTableModel.getValueAt( indices[ 0], 4));
		if ( !selectFontFamilyDlg.do_modal( getParent()))
			return;

		for ( int i = 0; i < indices.length; ++i)
			defaultTableModel.setValueAt( selectFontFamilyDlg._familyName, indices[ i], 4);

		repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_change_font_style(java.awt.event.ActionEvent)
	 */
	public void on_change_font_style(ActionEvent actionEvent) {
		int[] indices = getSelectedRows();
		if ( 0 == indices.length)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();

		SelectFontStyleDlg selectFontStyleDlg
			= new SelectFontStyleDlg( _owner, true,
			( String)defaultTableModel.getValueAt( indices[ 0], 5));
		if ( !selectFontStyleDlg.do_modal( getParent()))
			return;

		for ( int i = 0; i < indices.length; ++i)
			defaultTableModel.setValueAt( selectFontStyleDlg._style, indices[ i], 5);

		repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_change_font_size(java.awt.event.ActionEvent)
	 */
	public void on_change_font_size(ActionEvent actionEvent) {
		int[] indices = getSelectedRows();
		if ( 0 == indices.length)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();

		SelectFontSizeDlg selectFontSizeDlg
			= new SelectFontSizeDlg( _owner, true,
				( String)defaultTableModel.getValueAt( indices[ 0], 6));
		if ( !selectFontSizeDlg.do_modal( getParent()))
			return;

		for ( int i = 0; i < indices.length; ++i)
			defaultTableModel.setValueAt( selectFontSizeDlg._size, indices[ i], 6);

		repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_change_imagefile(java.awt.event.ActionEvent)
	 */
	public void on_change_imagefile(ActionEvent actionEvent) {
		int[] indices = getSelectedRows();
		if ( 0 == indices.length)
			return;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();

		EditSelectedImageDlg editSelectedImageDlg = new EditSelectedImageDlg( _owner,
			ResourceManager.get_instance().get( "select.imagefile.dialog.title"),
			true, _openDirectoryKey, ( String)defaultTableModel.getValueAt( indices[ 0], 7));
		if ( !editSelectedImageDlg.do_modal())
			return;

		for ( int i = 0; i < indices.length; ++i)
			defaultTableModel.setValueAt( editSelectedImageDlg._newImageFilename, indices[ i], 7);

		repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_remove_imagefile(java.awt.event.ActionEvent)
	 */
	public void on_remove_imagefile(ActionEvent actionEvent) {
		int[] indices = getSelectedRows();
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < indices.length; ++i)
			defaultTableModel.setValueAt( "", indices[ i], 7);
	}

	/* (Non Javadoc)
	 * @see soars.application.animator.common.menu.basic1.IBasicMenuHandler1#on_arrange_spots(java.awt.event.ActionEvent)
	 */
	public void on_arrange_spots(ActionEvent actionEvent) {
	}

	/* (Non Javadoc)
	 * @see javax.swing.JComponent#getToolTipText(java.awt.event.MouseEvent)
	 */
	public String getToolTipText(MouseEvent mouseEvent) {
		int row = rowAtPoint( mouseEvent.getPoint());
		if ( 0 > row || getRowCount() <= row)
			return null;

		int column = columnAtPoint( mouseEvent.getPoint());
		if ( 0 > column || getColumnCount() <= column)
			return null;

		if ( 7 != convertColumnIndexToModel( column))
			return null;

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		String text = ( String)defaultTableModel.getValueAt( row, 7);
		if ( null == text || text.equals( ""))
			return null;

		return text;
	}

	/**
	 * @param propertyMap
	 * @param graphics2D
	 */
	protected void on_ok(TreeMap<String, PropertyBase> propertyMap, Graphics2D graphics2D) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			String value = ( String)defaultTableModel.getValueAt( i, 0);
			PropertyBase propertyBase = propertyMap.get( value);

			JCheckBox checkbox = ( JCheckBox)defaultTableModel.getValueAt( i, 1);
			boolean visible = checkbox.isSelected();
			Color imageColor = ( Color)defaultTableModel.getValueAt( i, 2);
			Color textColor = ( Color)defaultTableModel.getValueAt( i, 3);
			String fontFamily = ( String)defaultTableModel.getValueAt( i, 4);
			int fontStyle = CommonTool.get_font_style( ( String)defaultTableModel.getValueAt( i, 5));
			int fontSize = Integer.parseInt( ( String)defaultTableModel.getValueAt( i, 6));
			String imageFilename = ( String)defaultTableModel.getValueAt( i, 7);

			propertyBase.update( visible,
				imageColor.getRed(), imageColor.getGreen(), imageColor.getBlue(),
				textColor.getRed(), textColor.getGreen(), textColor.getBlue(),
				fontFamily, fontStyle, fontSize, imageFilename,
				( Graphics2D)getGraphics());
		}
	}
}
