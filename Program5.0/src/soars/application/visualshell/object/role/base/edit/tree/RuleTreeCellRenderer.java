/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tree;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;

/**
 * @author kurata
 *
 */
public class RuleTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	public RuleTreeCellRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree arg0, Object arg1,
		boolean arg2, boolean arg3, boolean arg4, int arg5, boolean arg6) {

		super.getTreeCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5, 	arg6);

		DefaultMutableTreeNode defaultMutableTreeNode = ( DefaultMutableTreeNode)arg1;
		Object object = defaultMutableTreeNode.getUserObject();
		if ( null == object)
			setText( "unknown");
		else {
			if ( object instanceof String) {
				String text = ( String)object;

				Color color = null;

				// TODO 2012.9.20 このノードの子のインスタンスを調べてその色と同じにする
				for ( int i = 0; i < defaultMutableTreeNode.getChildCount(); ++i) {
					DefaultMutableTreeNode child = ( DefaultMutableTreeNode)defaultMutableTreeNode.getChildAt( i);
					object = child.getUserObject();
					if ( null == object)
						continue;

					if ( object instanceof RulePropertyPanelBase) {
						RulePropertyPanelBase rulePropertyPanelBase = ( RulePropertyPanelBase)object;
						color = rulePropertyPanelBase._color;
						break;
					}
				}
//				if ( text.equals( ResourceManager.get( "edit.rule.dialog.condition.list.inclusion.condition")))
//					color = CommonRuleData.get_color( ResourceManager.get( "rule.type.condition.list"), "condition");
//				else if ( text.equals( ResourceManager.get( "edit.rule.dialog.condition.list.specified.condition")))
//					color = CommonRuleData.get_color( ResourceManager.get( "rule.type.condition.list"), "condition");
//				else if ( text.equals( ResourceManager.get( "edit.rule.dialog.command.collection")))
//					color = CommonRuleData.get_color( ResourceManager.get( "rule.type.command.collection"), "command");
//				else if ( text.equals( ResourceManager.get( "edit.rule.dialog.command.list")))
//					color = CommonRuleData.get_color( ResourceManager.get( "rule.type.command.list"), "command");
//				else if ( text.equals( ResourceManager.get( "edit.rule.dialog.command.dynamic.creation")))
//					color = CommonRuleData.get_color( ResourceManager.get( "rule.type.command.create.agent"), "command");
//				else if ( text.equals( ResourceManager.get( "edit.rule.dialog.command.exchange.algebra")))
//					color = CommonRuleData.get_color( ResourceManager.get( "rule.type.command.exchange.algebra"), "command");

				if ( null != color) {
					if ( arg2 || arg6) {
						setForeground( Color.white);
						setBackground( color);
					} else {
						setForeground( color);
						//setBackground( arg0.getBackground());
					}
				}

				setText( text);

				if ( !arg3)
					setIcon( getClosedIcon());

			} else if ( object instanceof RulePropertyPanelBase) {
				RulePropertyPanelBase rulePropertyPanelBase = ( RulePropertyPanelBase)object;
				if ( null != rulePropertyPanelBase._color) {
					if ( arg2 || arg6) {
						setForeground( Color.white);
						setBackground( rulePropertyPanelBase._color);
					} else {
						setForeground( rulePropertyPanelBase._color);
						//setBackground( arg0.getBackground());
					}
				}

				setText( String.valueOf( rulePropertyPanelBase._index + 1) + ". " + rulePropertyPanelBase._title);
			} else
				setText( "unknown");
		}

		return this;
	}
}
