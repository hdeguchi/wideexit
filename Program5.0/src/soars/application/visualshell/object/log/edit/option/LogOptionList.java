/*
 * 2005/06/01
 */
package soars.application.visualshell.object.log.edit.option;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import soars.application.visualshell.object.log.LogOption;
import soars.application.visualshell.object.log.LogOptionManager;

/**
 * The list component to edit the log options.
 * @author kurata / SOARS project
 */
public class LogOptionList extends JList {

	/**
	 * Creates this object.
	 */
	public LogOptionList() {
		super();
	}

	/**
	 * Returns true if this object is initialized with the specified data successfully.
	 * @param logOptionManager the log option manager
	 * @return true if this object is initialized successfully
	 */
	public boolean setup(LogOptionManager logOptionManager) {
		setSelectionMode( ListSelectionModel.SINGLE_SELECTION);

		DefaultComboBoxModel defaultComboBoxModel = new DefaultComboBoxModel();
		for ( int i = 0; i < logOptionManager.size(); ++i) {
			LogOption logOption = logOptionManager.get( i);
			defaultComboBoxModel.addElement( new JCheckBox( logOption._name, logOption._flag));
		}

		setModel( defaultComboBoxModel);

		setCellRenderer( new LogOptionListCellRenderer());
		addMouseListener( new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
				on_select_changed( arg0);
			}
		});

		return true;
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_select_changed(MouseEvent mouseEvent) {
		int index = locationToIndex( mouseEvent.getPoint());
		if ( 0 > index)
			return;

		JCheckBox checkBox = ( JCheckBox)getModel().getElementAt( index);
		checkBox.setSelected( !checkBox.isSelected());

		repaint();
	}

	/**
	 * Sets this object data to the specified log option manager.
	 * @param logOptionManager the specified log option manager
	 */
	public void on_ok(LogOptionManager logOptionManager) {
		logOptionManager.clear();
		for ( int i = 0; i < getModel().getSize(); ++i) {
			JCheckBox checkBox = ( JCheckBox)getModel().getElementAt( i);
			logOptionManager.add( new LogOption( checkBox.getText(), checkBox.isSelected()));
		}
	}
}
