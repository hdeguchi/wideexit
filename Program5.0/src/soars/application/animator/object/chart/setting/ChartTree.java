/**
 * 
 */
package soars.application.animator.object.chart.setting;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import soars.application.animator.main.ResourceManager;
import soars.application.animator.object.chart.ChartDataPair;
import soars.application.animator.object.chart.ChartObject;
import soars.application.animator.object.chart.ChartObjectComparator;
import soars.application.animator.object.chart.ChartObjectMap;
import soars.common.utility.swing.tree.StandardTree;

/**
 * @author kurata
 *
 */
public class ChartTree extends StandardTree {

	/**
	 * 
	 */
	private ChartObjectMap _chartObjectMap = null;

	/**
	 * @param owner
	 * @param parent
	 */
	public ChartTree(Frame owner, Component parent) {
		super(owner, parent);
		_chartObjectMap = new ChartObjectMap( ChartObjectMap.get_instance());
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.swing.TreeBase#setup(boolean)
	 */
	public boolean setup(boolean popupMenu) {
		if ( !super.setup(popupMenu))
			return false;

		DefaultTreeModel defaultTreeModel = ( DefaultTreeModel)getModel();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode( ResourceManager.get_instance().get( "chart.display.setting.dialog.chart.tree.root.text"));
		defaultTreeModel.setRoot( root);

		ChartObject[] chartObjects = ( ChartObject[])_chartObjectMap.values().toArray( new ChartObject[ 0]);
		Arrays.sort( chartObjects, new ChartObjectComparator());
		for ( int i = 0; i < chartObjects.length; ++i) {
			DefaultMutableTreeNode child = new DefaultMutableTreeNode( chartObjects[ i]);
			root.add( child);
			append( chartObjects[ i]._chartDataPairs, child);
		}

		setCellRenderer( new ChartTreeCellRenderer());

		expandPath( root);

		return true;
	}

	/**
	 * @param defaultMutableTreeNode
	 */
	private void expandPath(DefaultMutableTreeNode defaultMutableTreeNode) {
		for ( int i = 0; i < defaultMutableTreeNode.getChildCount(); ++i) {
			DefaultMutableTreeNode child = ( DefaultMutableTreeNode)defaultMutableTreeNode.getChildAt( i);
			if ( 0 < child.getChildCount()) {
				expandPath( new TreePath( child.getPath()));
				expandPath( child);
			}
		}
	}

	/**
	 * @param chartDataPairs
	 * @param parent
	 */
	private void append(List chartDataPairs, DefaultMutableTreeNode parent) {
		for ( int i = 0; i < chartDataPairs.size(); ++i) {
			ChartDataPair chartDataPair = ( ChartDataPair)chartDataPairs.get( i);
			DefaultMutableTreeNode child = new DefaultMutableTreeNode( chartDataPair);
			parent.add( child);
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.tree.StandardTree#on_mouse_pressed(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_pressed(MouseEvent mouseEvent) {
		TreePath treePath = getPathForLocation( mouseEvent.getX(), mouseEvent.getY());
		if ( null == treePath)
			return;

		setSelectionPath( treePath);

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)treePath.getLastPathComponent();
		Object object = defaultMutableTreeNode.getUserObject();
		if ( 0 < defaultMutableTreeNode.getChildCount() && !isExpanded( treePath))
			expandPath( treePath);
		else {
			if ( object instanceof ChartObject) {
				ChartObject chartObject = ( ChartObject)object;
				chartObject.visible( !chartObject.visible());
			} else if ( object instanceof ChartDataPair) {
				ChartDataPair chartDataPair = ( ChartDataPair)object;
				chartDataPair._visible = !chartDataPair._visible;
				if ( !chartDataPair._parent.visible())
					chartDataPair._parent.visible( false);
			}
		}

		repaint();
		requestFocus();
	}

	/**
	 * 
	 */
	public void on_ok() {
		ChartObjectMap.get_instance().update( _chartObjectMap);
	}
}
