/**
 * 
 */
package soars.application.simulator.main.log.tab;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import soars.application.simulator.stream.SystemOutStreamPumper;
import soars.common.utility.swing.panel.StandardPanel;

/**
 * @author kurata
 *
 */
public class SystemOutPropertyPage extends StandardPanel {

	/**
	 * 
	 */
	private JTabbedPane _tabbedPane = null;

	/**
	 * 
	 */
	private String _title = "";

	/**
	 * 
	 */
	private SystemOutStreamPumper _systemOutStreamPumper = null;

	/**
	 * 
	 */
	private JTextArea _textArea = null;

	/**
	 * @param tabbedPane
	 * @param title
	 */
	public SystemOutPropertyPage(JTabbedPane tabbedPane, String title) {
		super();
		_tabbedPane = tabbedPane;
		_title = title;
	}

	/**
	 * @param tabbedPane
	 * @param title
	 * @param systemOutStreamPumper 
	 */
	public SystemOutPropertyPage(JTabbedPane tabbedPane, String title, SystemOutStreamPumper systemOutStreamPumper) {
		super();
		_tabbedPane = tabbedPane;
		_title = title;
		_systemOutStreamPumper = systemOutStreamPumper;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.panel.StandardPanel#create()
	 */
	public boolean create() {
		if ( !super.create())
			return false;

		setLayout( new BorderLayout());

		_textArea = new JTextArea();
		_textArea.setEditable( false);
		add( new JScrollPane( _textArea));

		_tabbedPane.addTab( _title, this);

		return true;
	}

	/**
	 * 
	 */
	public void flush() {
		if ( null == _systemOutStreamPumper)
			return;

		_textArea.append( _systemOutStreamPumper._text);
	}

	/**
	 * @return
	 */
	public String getText() {
		return _textArea.getText();
	}

	/**
	 * @param stdout
	 */
	public void setText(String stdout) {
		_textArea.setText( stdout);
	}
}
