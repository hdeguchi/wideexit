/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.class_variable;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Map;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.w3c.dom.Node;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.VariableTableBase;
import soars.application.visualshell.object.entity.base.object.base.ObjectBase;
import soars.application.visualshell.object.entity.base.object.class_variable.ClassVariableObject;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.dnd.dom.DomNodeTransferable;
import soars.common.utility.swing.table.base.StandardTableHeaderRenderer;
import soars.common.utility.tool.sort.StringNumberComparator;
import soars.common.utility.xml.dom.XmlTool;

/**
 * @author kurata
 *
 */
public class ClassVariableTable extends VariableTableBase implements DropTargetListener {

	/**
	 * 
	 */
	public ClassVariablePanel _classVariablePanel = null; 

	/**
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 * @param propertyPanelBase
	 * @param owner
	 * @param parent
	 */
	public ClassVariableTable(EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, PropertyPanelBase propertyPanelBase, Frame owner, Component parent) {
		super("class variable", entityBase, propertyPanelBaseMap, propertyPanelBase, 4, owner, parent);
	}

	/**
	 * @return
	 */
	public boolean setup() {
		if ( !super.setup(!_entityBase.is_multi()))
			return false;


		setAutoResizeMode( AUTO_RESIZE_OFF);


		JTableHeader tableHeader = getTableHeader();
		StandardTableHeaderRenderer standardTableHeaderRenderer = new StandardTableHeaderRenderer();
		if ( _entityBase.is_multi())
			standardTableHeaderRenderer.setEnabled( false);

		tableHeader.setDefaultRenderer( standardTableHeaderRenderer);

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		defaultTableModel.setColumnCount( _columns);

		DefaultTableColumnModel defaultTableColumnModel = ( DefaultTableColumnModel)getColumnModel();
		defaultTableColumnModel.getColumn( 0).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.class.variable.table.header.name"));
		defaultTableColumnModel.getColumn( 1).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.class.variable.table.header.jar.filename"));
		defaultTableColumnModel.getColumn( 2).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.class.variable.table.header.classname"));
		defaultTableColumnModel.getColumn( 3).setHeaderValue(
			ResourceManager.get_instance().get( "edit.object.dialog.class.variable.table.header.comment"));

		defaultTableColumnModel.getColumn( 0).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 1).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 2).setPreferredWidth( 200);
		defaultTableColumnModel.getColumn( 3).setPreferredWidth( 2000);

		for ( int i = 0; i < _columns; ++i) {
			TableColumn tableColumn = defaultTableColumnModel.getColumn( i);
			tableColumn.setCellRenderer( new ClassVariableTableRowRenderer());
		}

		if ( !_entityBase.is_multi()) {
			if ( !setup( _entityBase._objectMapMap.get( "class variable"), defaultTableModel))
				return false;
		}

		setup_popup_menu();

		new DropTarget( this, this);

		setToolTipText( "ClassVariableTable");

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.VariableTableBase#compare(soars.application.visualshell.object.entity.base.object.base.ObjectBase, soars.application.visualshell.object.entity.base.object.base.ObjectBase)
	 */
	@Override
	protected int compare(ObjectBase objectBase0, ObjectBase objectBase1) {
		return StringNumberComparator.compareTo( objectBase0._name, objectBase1._name);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.VariableTableBase#can_remove_selected_objects(soars.application.visualshell.object.entity.base.object.base.ObjectBase)
	 */
	@Override
	protected boolean can_remove_selected_objects(ObjectBase objectBase) {
		if ( _propertyPanelBaseMap.get( "variable").contains( objectBase))
			return false;

		return super.can_remove_selected_objects( objectBase);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.VariableTableBase#on_remove()
	 */
	@Override
	protected void on_remove() {
		_propertyPanelBaseMap.get( "variable").update();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#getToolTipText(java.awt.event.MouseEvent)
	 */
	@Override
	public String getToolTipText(MouseEvent mouseEvent) {
		int row = rowAtPoint( mouseEvent.getPoint());
		if ( 0 > row || getRowCount() <= row)
			return null;

		int column = columnAtPoint( mouseEvent.getPoint());
		if ( 0 > column || getColumnCount() <= column)
			return null;

		if ( 1 == convertColumnIndexToModel( column)) {
			DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
			ClassVariableObject classVariableObject = ( ClassVariableObject)defaultTableModel.getValueAt( row, column);
			if ( null == classVariableObject)
				return null;

			return classVariableObject._jarFilename;
		} else if ( 2 == convertColumnIndexToModel( column)) {
			DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
			ClassVariableObject classVariableObject = ( ClassVariableObject)defaultTableModel.getValueAt( row, column);
			if ( null == classVariableObject)
				return null;

			return classVariableObject._classname;
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.VariableTableBase#on_ok()
	 */
	@Override
	public void on_ok() {
		_entityBase._objectMapMap.get( "class variable").clear();

		DefaultTableModel defaultTableModel = ( DefaultTableModel)getModel();
		for ( int i = 0; i < defaultTableModel.getRowCount(); ++i) {
			ClassVariableObject classVariableObject = ( ClassVariableObject)defaultTableModel.getValueAt( i,  0);
			_entityBase._objectMapMap.get( "class variable").put( classVariableObject._name, classVariableObject);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	@Override
	public void dragEnter(DropTargetDragEvent arg0) {
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragExit(DropTargetEvent arg0) {
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	@Override
	public void dragOver(DropTargetDragEvent arg0) {
		Point position = getMousePosition();
		if ( null == position) {
			arg0.rejectDrag();
			repaint();
			return;
		}

		if ( 2 != columnAtPoint( position)) {
			arg0.rejectDrag();
			repaint();
			return;
		}

		int row = rowAtPoint( position);
		if ( 0 > row || getRowCount() <= row) {
			arg0.rejectDrag();
			repaint();
			return;
		}

    DataFlavor[] dataFlavors = arg0.getCurrentDataFlavors();
		if ( dataFlavors[ 0].getHumanPresentableName().equals( DomNodeTransferable._name)) {
			try {
				TreeNode draggedTreeNode = ( TreeNode)arg0.getTransferable().getTransferData( DomNodeTransferable._localObjectFlavor);
				Node node = ( Node)( ( DefaultMutableTreeNode)draggedTreeNode).getUserObject();
				if ( null == node) {
					arg0.rejectDrag();
					repaint();
					return;
				}

				if ( node.getNodeName().equals( "class"))
					arg0.acceptDrag( DnDConstants.ACTION_COPY);
				else
					arg0.rejectDrag();

				setRowSelectionInterval( row, row);
				( ( ClassVariablePropertyPanel)_propertyPanelBase).changeSelection( ( ClassVariableObject)getValueAt( row, 0), false);
			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
				arg0.rejectDrag();
			} catch (IOException e) {
				e.printStackTrace();
				arg0.rejectDrag();
			}
		} else
			arg0.rejectDrag();

		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	@Override
	public void drop(DropTargetDropEvent arg0) {
		Point position = getMousePosition();
		if ( null == position) {
			arg0.rejectDrop();
			repaint();
			return;
		}

		if ( 2 != columnAtPoint( position)) {
			arg0.rejectDrop();
			repaint();
			return;
		}

		int row = rowAtPoint( position);
		if ( 0 > row || getRowCount() <= row) {
			arg0.rejectDrop();
			repaint();
			return;
		}

		try {
			DataFlavor[] dataFlavors = arg0.getCurrentDataFlavors();
			if ( dataFlavors[ 0].getHumanPresentableName().equals( DomNodeTransferable._name)) {
				TreeNode draggedTreeNode = ( TreeNode)arg0.getTransferable().getTransferData( DomNodeTransferable._localObjectFlavor);
				arg0.getDropTargetContext().dropComplete( true);
				if ( null == draggedTreeNode) {
					arg0.rejectDrop();
					repaint();
					return;
				}

				Node node = ( Node)( ( DefaultMutableTreeNode)draggedTreeNode).getUserObject();
				if ( null == node) {
					arg0.rejectDrop();
					repaint();
					return;
				}

				if ( !node.getNodeName().equals( "class")) {
					arg0.rejectDrop();
					repaint();
					return;
				}

				String classname = XmlTool.get_attribute( node, "name");
				if ( null == classname) {
					arg0.rejectDrop();
					repaint();
					return;
				}

				node = node.getParentNode();
				//if ( !node.getNodeName().equals( "jarfile")) {
				if ( null == node) {
					arg0.rejectDrop();
					repaint();
					return;
				}

				String nodename = node.getNodeName();
				if ( !nodename.equals( "root") && !nodename.equals( "jarfile")) {
					arg0.rejectDrop();
					repaint();
					return;
				}

				String jarfilename = XmlTool.get_attribute( node, "name");
				if ( null == jarfilename) {
					arg0.rejectDrop();
					repaint();
					return;
				}

				if ( nodename.equals( "root") && !jarfilename.equals( Constant._javaClasses)) {
					arg0.rejectDrop();
					repaint();
					return;
				}

				if ( !update( row, jarfilename, classname)) {
					arg0.rejectDrop();
					repaint();
					return;
				}

				arg0.acceptDrop( DnDConstants.ACTION_COPY);
			} else {
				arg0.rejectDrop();
			}
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
			arg0.rejectDrop();
		} catch (IOException e) {
			e.printStackTrace();
			arg0.rejectDrop();
		}

		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	@Override
	public void dropActionChanged(DropTargetDragEvent arg0) {
		repaint();
	}

	/**
	 * @param row
	 * @param jarfilename
	 * @param classname
	 * @return
	 */
	private boolean update(int row, String jarfilename, String classname) {
		ClassVariableObject classVariableObject = ( ClassVariableObject)getValueAt( row, 0);
		if ( !_classVariablePanel.can_get_data( classVariableObject, classVariableObject._name, jarfilename, classname, classVariableObject._comment))
			return false;

		_classVariablePanel.get_data( classVariableObject, classVariableObject._name, jarfilename, classname, classVariableObject._comment);

		repaint();

		if ( !WarningManager.get_instance().isEmpty()) {
			WarningDlg1 warningDlg1 = new WarningDlg1( _owner,
				ResourceManager.get_instance().get( "warning.dialog1.title"),
				ResourceManager.get_instance().get( "warning.dialog1.message3"),
				_parent);
			warningDlg1.do_modal();
		}

		setRowSelectionInterval( row, row);
		( ( ClassVariablePropertyPanel)_propertyPanelBase).changeSelection( ( ClassVariableObject)getValueAt( row, 0), true);

		return true;
	}
}
