/**
 * 
 */
package soars.application.simulator.main.log.tab;

import java.awt.BorderLayout;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import soars.application.simulator.data.LogData;
import soars.common.utility.swing.panel.StandardPanel;

/**
 * @author kurata
 *
 */
public class LogPropertyPage extends StandardPanel {

	/**
	 * 
	 */
	protected JTabbedPane _tabbedPane = null;

	/**
	 * 
	 */
	public JTextArea _textArea = null;

	/**
	 * @param tabbedPane
	 */
	public LogPropertyPage(JTabbedPane tabbedPane) {
		super();
		_tabbedPane = tabbedPane;
	}

	/**
	 * @param name
	 * @param value
	 * @return
	 */
	public boolean create(String name, String value) {
		if ( !super.create())
			return false;

		setLayout( new BorderLayout());

		_textArea = new JTextArea( value);
		_textArea.setEditable( false);
		add( new JScrollPane( _textArea));

		_tabbedPane.addTab( name, this);

		return true;
	}

	/**
	 * @param prefix
	 * @param logData
	 * @param textAreaMap
	 * @return
	 */
	public boolean create(String prefix, LogData logData, Map textAreaMap) {
		if ( !super.create())
			return false;

		setLayout( new BorderLayout());

		_textArea = new JTextArea( logData._value);
		_textArea.setEditable( false);
		add( new JScrollPane( _textArea));

		//_tabbedPane.addTab( prefix + logData._name, this);
		if ( 2 > _tabbedPane.getComponentCount())
			return false;

		_tabbedPane.insertTab( prefix + logData._name, null, this, null, _tabbedPane.getComponentCount() - 2);

		textAreaMap.put( prefix + logData._name, _textArea);

		return true;
	}
}
