/*
 * 2005/05/01
 */
package soars.application.visualshell.object.stage.edit;

import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import soars.application.visualshell.main.Constant;

/**
 * @author kurata
 */
public class StageListCellRenderer extends JCheckBox implements ListCellRenderer {

	/**
	 * 
	 */
	public StageListCellRenderer() {
		super();
	}

	/* (Non Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3, boolean arg4) {
		setOpaque( true);
		setForeground( arg3 ? SystemColor.textHighlightText : SystemColor.textText);
		setBackground( arg3 ? SystemColor.textHighlight : SystemColor.text);
		if ( null != arg1 && arg1 instanceof StageCheckBox) {
			StageCheckBox stageCheckBox = ( StageCheckBox)arg1;
			setText( stageCheckBox._stage._name);
			setSelected( stageCheckBox._stage._random);
			setEnabled( !stageCheckBox._stage._name.equals( Constant._initializeChartStageName)
				&& !stageCheckBox._stage._name.equals( Constant._updateChartStageName));
		}
		return this;
	}
}
