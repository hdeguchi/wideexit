/**
 * 
 */
package soars.application.simulator.main.log;

import java.awt.Component;
import java.awt.Rectangle;
import java.util.List;

import soars.application.simulator.main.ResourceManager;
import soars.application.simulator.main.log.tab.LogPropertyPage;
import soars.application.simulator.main.log.tab.SystemOutPropertyPage;

/**
 * @author kurata
 *
 */
public class StaticLogViewerFrame extends LogViewerFrame {

	/**
	 * 
	 */
	private LogPropertyPage _consoleLogPropertyPage = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 */
	public StaticLogViewerFrame(String arg0, boolean arg1, boolean arg2, boolean arg3, boolean arg4) {
		super(arg0, arg1, arg2, arg3, arg4);
		_terminated = true;
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.main.log.LogViewerFrame#terminated_normally()
	 */
	public boolean terminated_normally() {
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.main.log.LogViewerFrame#get_console_text(java.awt.Component)
	 */
	protected String get_console_text(Component component) {
		return ( component.equals( _consoleLogPropertyPage) ? _consoleLogPropertyPage._textArea.getText() : null);
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.main.log.LogViewerFrame#update(java.lang.String, java.awt.Rectangle, java.lang.String, java.util.List, java.util.List, java.lang.String, java.lang.String)
	 */
	public boolean update(String log_viewer_window_title, Rectangle window_rectangle, String console, List agents, List spots, String stdout, String stderr) {
		if ( null == _consoleLogPropertyPage) {
			_consoleLogPropertyPage = new LogPropertyPage( _tabbedPane);
			if ( !_consoleLogPropertyPage.create( ResourceManager.get_instance().get( "log.viewer.console.title"), console))
				return false;
		} else {
			_tabbedPane.setTitleAt( _tabbedPane.indexOfComponent( _consoleLogPropertyPage), ResourceManager.get_instance().get( "log.viewer.console.title"));
			_consoleLogPropertyPage._textArea.setText( console);
		}

		if ( null == _stdOutPropertyPage) {
			_stdOutPropertyPage = new SystemOutPropertyPage( _tabbedPane, ResourceManager.get_instance().get( "log.viewer.stdout.title"));
			if ( !_stdOutPropertyPage.create())
				return false;
		}

		if ( null == _stdErrOutPropertyPage) {
			_stdErrOutPropertyPage = new SystemOutPropertyPage( _tabbedPane, ResourceManager.get_instance().get( "log.viewer.stderr.title"));
			if ( !_stdErrOutPropertyPage.create())
				return false;
		}

		return super.update(log_viewer_window_title, window_rectangle, console, agents, spots, stdout, stderr);
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.main.log.LogViewerFrame#ignore(java.awt.Component)
	 */
	protected boolean ignore(Component component) {
		return ( component.equals( _consoleLogPropertyPage)
			|| component.equals( _stdOutPropertyPage)
			|| component.equals( _stdErrOutPropertyPage));
	}
}
