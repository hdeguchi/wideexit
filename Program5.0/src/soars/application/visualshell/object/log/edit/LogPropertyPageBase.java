/*
 * Created on 2005/09/16
 */
package soars.application.visualshell.object.log.edit;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import soars.application.visualshell.object.log.LogOptionManager;
import soars.application.visualshell.object.log.edit.option.LogOptionList;
import soars.common.utility.swing.panel.StandardPanel;

/**
 * The base tab component to edit the log options.
 * @author kurata / SOARS project
 */
public class LogPropertyPageBase extends StandardPanel {

	/**
	 * Title for the tab.
	 */
	public String _title = "";

	/**
	 * 
	 */
	protected Frame _owner = null;

	/**
	 * 
	 */
	protected Component _parent = null;

	/**
	 * Creates this object.
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public LogPropertyPageBase(Frame owner, Component parent) {
		super();
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @param text
	 * @param panel
	 * @return
	 */
	protected void setup_label(String text, JPanel panel) {
		JPanel partialPanel = new JPanel();
		partialPanel.setLayout( new BoxLayout( partialPanel, BoxLayout.X_AXIS));

		JPanel labelPanel = new JPanel();
		labelPanel.setLayout( new GridLayout( 1, 1));

		JLabel label = new JLabel( text);

		labelPanel.add( label);
		partialPanel.add( labelPanel);

		partialPanel.add( Box.createHorizontalStrut( 5));

		panel.add( partialPanel);
	}

	/**
	 * @param logOptionManager
	 * @param panel
	 * @return
	 */
	protected LogOptionList setup_list(LogOptionManager logOptionManager, JPanel panel) {
		JPanel partialPanel = new JPanel();
		partialPanel.setLayout( new BoxLayout( partialPanel, BoxLayout.X_AXIS));

		JPanel listPanel = new JPanel();
		listPanel.setLayout( new GridLayout( 1, 1));

		LogOptionList logOptionList = new LogOptionList();
		if ( !logOptionList.setup( logOptionManager))
			return null;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( logOptionList);

		listPanel.add( scrollPane);
		partialPanel.add( listPanel);

		partialPanel.add( Box.createHorizontalStrut( 5));

		panel.add( partialPanel);

		return logOptionList;
	}
}
