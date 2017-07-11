/**
 * 
 */
package soars.application.simulator.main.log;

import java.awt.Component;
import java.awt.Rectangle;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.List;

import script.ScriptParser;
import script.ScriptReader;
import soars.application.simulator.main.MainFrame;
import soars.application.simulator.main.ResourceManager;
import soars.application.simulator.modelbuilder.ModelBuilder;
import soars.application.simulator.stream.StdErrOutStreamPumper;
import soars.application.simulator.stream.StdOutStreamPumper;
import view.JTabbedConsole;

/**
 * @author kurata
 *
 */
public class LiveLogViewerFrame extends LogViewerFrame {

	/**
	 * 
	 */
	private JTabbedConsole _tabbedConsole = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 */
	public LiveLogViewerFrame(String arg0, boolean arg1, boolean arg2, boolean arg3, boolean arg4) {
		super(arg0, arg1, arg2, arg3, arg4);
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.main.log.LogViewerFrame#start(java.io.Reader, soars.application.simulator.stream.StdOutStreamPumper, soars.application.simulator.stream.StdErrOutStreamPumper)
	 */
	public boolean start(Reader reader, final StdOutStreamPumper stdOutStreamPumper, final StdErrOutStreamPumper stdErrOutStreamPumper) {
		final ScriptReader scriptReader = new ScriptReader( reader, "");

		_tabbedConsole = new JTabbedConsole( _tabbedPane,
			ResourceManager.get_instance().get( "log.viewer.console.title") + " - " + ResourceManager.get_instance().get( "log.viewer.state.waiting"),
			ResourceManager.get_instance().get( "log.viewer.console.title") + " - " + ResourceManager.get_instance().get( "log.viewer.state.finished"));
		_tabbedConsole.panel.remove( _tabbedConsole.toolBar);
		_tabbedConsole.textArea.setEditable( false);
		final PrintWriter printWriter = new PrintWriter( _tabbedConsole.setSelected());
		printWriter.println( scriptReader.getFileName());
		printWriter.println();
		_tabbedConsole.thread( new Runnable() {
			@SuppressWarnings("deprecation")
			public void run() {
				try {
					env.Environment.setConsole( printWriter);
					MainFrame.get_instance().on_start_simulation();
					synchronized (MainFrame.class) {
//						if ( enqueue) {
//							JMainFrame.class.wait();
//						}
						_tabbedConsole.setTitle( ResourceManager.get_instance().get( "log.viewer.console.title") + " - " + ResourceManager.get_instance().get( "log.viewer.state.running"));

						ScriptParser scriptParser = new ModelBuilder( _tabbedPane, _textAreaMap);
						scriptParser.parseAll( scriptReader);

						create_stdOutPropertyPage( stdOutStreamPumper);
						create_stdErrOutPropertyPage( stdErrOutStreamPumper);
					}
					printWriter.close();
				} catch (Exception e) {
					e.printStackTrace(printWriter);
					//e.printStackTrace();
					_tabbedConsole.setTitle( ResourceManager.get_instance().get( "log.viewer.console.title") + " - " + ResourceManager.get_instance().get( "log.viewer.state.aborted"));
//					System.out.println( "start error!");
					create_stdOutPropertyPage( stdOutStreamPumper);
					stdOutStreamPumper.cleanup();
					_stdOutPropertyPage.flush();

					create_stdErrOutPropertyPage( stdErrOutStreamPumper);
					stdErrOutStreamPumper.cleanup();
					_stdErrOutPropertyPage.flush();
				}
				_terminated = true;
//				MainFrame.get_instance().on_terminate_simulation();
//				JOptionPane.showMessageDialog( MainFrame.get_instance(), "MainFrame.get_instance().on_terminate()", ResourceManager.get_instance().get( "application.title"), JOptionPane.INFORMATION_MESSAGE);
			}
		}).start();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.main.log.LogViewerFrame#terminated_normally()
	 */
	public boolean terminated_normally() {
		return ( 0 <= _tabbedConsole.textArea.getText().indexOf( "Execution Time:"));
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.main.log.LogViewerFrame#stop_simulation()
	 */
	public void stop_simulation() {
		if ( null == _tabbedConsole)
			return;

		_tabbedConsole.interrupt();
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.main.log.LogViewerFrame#get_console_text(java.awt.Component)
	 */
	protected String get_console_text(Component component) {
		return ( component.equals( _tabbedConsole.panel) ? _tabbedConsole.textArea.getText() : null);
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.main.log.LogViewerFrame#update(java.lang.String, java.awt.Rectangle, java.lang.String, java.util.List, java.util.List, java.lang.String, java.lang.String)
	 */
	public boolean update(String log_viewer_window_title, Rectangle window_rectangle, String console, List agents, List spots, String stdout, String stderr) {
		_tabbedConsole.setTitle( ResourceManager.get_instance().get( "log.viewer.console.title"));
		_tabbedConsole.textArea.setText( console);
		return super.update(log_viewer_window_title, window_rectangle, console, agents, spots, stdout, stderr);
	}

	/* (non-Javadoc)
	 * @see soars.application.simulator.main.log.LogViewerFrame#ignore(java.awt.Component)
	 */
	protected boolean ignore(Component component) {
		return ( component.equals( _tabbedConsole.panel)
			|| component.equals( _stdOutPropertyPage)
			|| component.equals( _stdErrOutPropertyPage));
	}
}
