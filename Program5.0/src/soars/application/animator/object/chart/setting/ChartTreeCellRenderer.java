/**
 * 
 */
package soars.application.animator.object.chart.setting;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import soars.application.animator.object.chart.ChartDataPair;
import soars.application.animator.object.chart.ChartObject;

/**
 * @author kurata
 *
 */
public class ChartTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	private JCheckBox _checkBox = null;

	/**
	 * 
	 */
	public ChartTreeCellRenderer() {
		super();
		add( _checkBox = new JCheckBox());
		_checkBox.setBackground( UIManager.getColor( "Tree.textBackground"));
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	public Component getTreeCellRendererComponent(JTree arg0, Object arg1, boolean arg2, boolean arg3, boolean arg4, int arg5, boolean arg6) {
		super.getTreeCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5, arg6);

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)arg1;
		Object object = defaultMutableTreeNode.getUserObject();
		if ( null == object)
			setText( "unknown");
		else if ( object instanceof String)
			setText( ( String)object);
		else if ( object instanceof ChartObject) {
			ChartObject chartObject = ( ChartObject)object;
			_checkBox.setText( chartObject._title + " - " + chartObject._name);
			_checkBox.setSelected( chartObject.visible());
			_checkBox.setVisible( true);
			return _checkBox;
		} else if ( object instanceof ChartDataPair) {
			ChartDataPair chartDataPair = ( ChartDataPair)object;
			_checkBox.setText( chartDataPair.getLegend());
			_checkBox.setSelected( chartDataPair._visible);
			_checkBox.setVisible( true);
			return _checkBox;
		} else
			setText( "unknown");

		return this;
	}
}
