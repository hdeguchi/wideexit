/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.class_variable;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.io.IOException;

import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.w3c.dom.Node;

import soars.application.visualshell.main.Constant;
import soars.common.utility.swing.dnd.dom.DomNodeTransferable;
import soars.common.utility.swing.dnd.text.DDTextField;
import soars.common.utility.xml.dom.XmlTool;

/**
 * @author kurata
 *
 */
public class CustomDDTextField extends DDTextField {

	/**
	 * 
	 */
	private JTextField _jarFileNameTextField = null;

	/**
	 * @param jarFileNameTextField
	 */
	public CustomDDTextField(JTextField jarFileNameTextField) {
		super();
		_jarFileNameTextField = jarFileNameTextField;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.dnd.text.DDTextField#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	@Override
	public void dragEnter(DropTargetDragEvent arg0) {
		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.dnd.text.DDTextField#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragExit(DropTargetEvent arg0) {
		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.dnd.text.DDTextField#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	@Override
	public void dragOver(DropTargetDragEvent arg0) {
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
	 * @see soars.common.utility.swing.dnd.text.DDTextField#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	@Override
	public void drop(DropTargetDropEvent arg0) {
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

				arg0.acceptDrop( DnDConstants.ACTION_COPY);

				setText( classname);
				_jarFileNameTextField.setText( jarfilename);
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
	 * @see soars.common.utility.swing.dnd.text.DDTextField#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	@Override
	public void dropActionChanged(DropTargetDragEvent arg0) {
		repaint();
	}
}
