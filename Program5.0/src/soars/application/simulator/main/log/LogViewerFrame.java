/**
 * 
 */
package soars.application.simulator.main.log;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.InternalFrameEvent;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.simulator.data.LogData;
import soars.application.simulator.executor.animator.Animator;
import soars.application.simulator.main.Constant;
import soars.application.simulator.main.Environment;
import soars.application.simulator.main.MainFrame;
import soars.application.simulator.main.ResourceManager;
import soars.application.simulator.main.log.tab.LogPropertyPage;
import soars.application.simulator.main.log.tab.SystemOutPropertyPage;
import soars.application.simulator.stream.StdErrOutStreamPumper;
import soars.application.simulator.stream.StdOutStreamPumper;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.swing.window.MDIChildFrame;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class LogViewerFrame extends MDIChildFrame {

	/**
	 * 
	 */
	static public final int _minimumWidth = 600;

	/**
	 * 
	 */
	static public final int _minimumHeight = 450;

	/**
	 * 
	 */
	protected Rectangle _windowRectangle = new Rectangle();

	/**
	 * 
	 */
	protected JTabbedPane _tabbedPane = null;

	/**
	 * 
	 */
	protected SystemOutPropertyPage _stdOutPropertyPage = null;

	/**
	 * 
	 */
	protected SystemOutPropertyPage _stdErrOutPropertyPage = null;

	/**
	 * 
	 */
	protected Map _textAreaMap = new HashMap();

	/**
	 * 
	 */
	public boolean _terminated = false;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 */
	public LogViewerFrame(String arg0, boolean arg1, boolean arg2, boolean arg3, boolean arg4) {
		super(arg0, arg1, arg2, arg3, arg4);
	}

	/**
	 * 
	 */
	private void get_property_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._logViewerWindowRectangleKey + "x",
			"0");
		_windowRectangle.x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._logViewerWindowRectangleKey + "y",
			"0");
		_windowRectangle.y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._logViewerWindowRectangleKey + "width",
			String.valueOf( _minimumWidth));
		_windowRectangle.width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._logViewerWindowRectangleKey + "height",
			String.valueOf( _minimumHeight));
		_windowRectangle.height = Integer.parseInt( value);
	}

	/**
	 * @return
	 */
	public boolean optimize_window_rectangle() {
		if ( !MainFrame.get_instance().get_client_rectangle().intersects( _windowRectangle)
			|| MainFrame.get_instance().get_client_rectangle().intersection( _windowRectangle).width <= 10
			|| _windowRectangle.y <= -MainFrame.get_instance().getInsets().top
			|| MainFrame.get_instance().get_client_rectangle().intersection( _windowRectangle).height <= MainFrame.get_instance().getInsets().top) {
			_windowRectangle.setBounds( 0, 0, _minimumWidth, _minimumHeight);
			setLocation( _windowRectangle.x, _windowRectangle.y);
			setSize( _windowRectangle.width, _windowRectangle.height);
			return true;
		}
		return false;
	}

	/**
	 * 
	 */
	public void set_property_to_environment_file() {
		_windowRectangle = getBounds();

		Environment.get_instance().set(
			Environment._logViewerWindowRectangleKey + "x", String.valueOf( _windowRectangle.x));
		Environment.get_instance().set(
			Environment._logViewerWindowRectangleKey + "y", String.valueOf( _windowRectangle.y));
		Environment.get_instance().set(
			Environment._logViewerWindowRectangleKey + "width", String.valueOf( _windowRectangle.width));
		Environment.get_instance().set(
			Environment._logViewerWindowRectangleKey + "height", String.valueOf( _windowRectangle.height));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		get_property_from_environment_file();

		_tabbedPane = new JTabbedPane();
		getContentPane().setLayout( new BorderLayout());
		getContentPane().add( _tabbedPane);

		//setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

		setLocation( _windowRectangle.x, _windowRectangle.y);
		setSize( _windowRectangle.width, _windowRectangle.height);
//		setSize( 600, 450);

		Toolkit.getDefaultToolkit().setDynamicLayout( true);

		setVisible( true);

//		try {
//			setSelected( true);
//		} catch (PropertyVetoException e) {
//			e.printStackTrace();
//		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_internal_frame_closing(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_closing(InternalFrameEvent internalFrameEvent) {
		super.on_internal_frame_closing(internalFrameEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_internal_frame_activated(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_activated(InternalFrameEvent internalFrameEvent) {
		super.on_internal_frame_activated(internalFrameEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.MDIChildFrame#on_internal_frame_deactivated(javax.swing.event.InternalFrameEvent)
	 */
	protected void on_internal_frame_deactivated(InternalFrameEvent internalFrameEvent) {
		super.on_internal_frame_deactivated(internalFrameEvent);
	}

	/**
	 * @param reader
	 * @param stdOutStreamPumper 
	 * @param stdErrOutStreamPumper 
	 * @return
	 */
	public boolean start(Reader reader, final StdOutStreamPumper stdOutStreamPumper, final StdErrOutStreamPumper stdErrOutStreamPumper) {
		return false;
	}

	/**
	 * @param stdOutStreamPumper
	 * @return
	 */
	protected boolean create_stdOutPropertyPage(StdOutStreamPumper stdOutStreamPumper) {
		_stdOutPropertyPage = new SystemOutPropertyPage(
			_tabbedPane, ResourceManager.get_instance().get( "log.viewer.stdout.title"), stdOutStreamPumper);
		return _stdOutPropertyPage.create();
	}

	/**
	 * @param stdErrOutStreamPumper
	 * @return
	 */
	protected boolean create_stdErrOutPropertyPage( StdErrOutStreamPumper stdErrOutStreamPumper) {
		_stdErrOutPropertyPage  = new SystemOutPropertyPage(
			_tabbedPane, ResourceManager.get_instance().get( "log.viewer.stderr.title"), stdErrOutStreamPumper);
		return _stdErrOutPropertyPage.create();
	}

	/**
	 * 
	 */
	public void flush() {
		_stdOutPropertyPage.flush();
		_stdErrOutPropertyPage.flush();
	}

	/**
	 * @return
	 */
	public boolean terminated_normally() {
		return false;
	}

	/**
	 * 
	 */
	public void stop_simulation() {
	}

	/**
	 * @param graphicProperties
	 * @param chartProperties
	 * @param soarsFile
	 * @param id
	 * @param title
	 * @param visualShellTitle
	 * @return
	 */
	public boolean run_animator(String graphicProperties, String chartProperties, File soarsFile, long id, String title, String visualShellTitle) {
		if ( !dollar_spot_log_exists())
			return false;

		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return false;

		File rootDirectory = new File( parentDirectory, Constant._animatorRootDirectoryName);
		if ( !rootDirectory.mkdir()) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		if ( !setup_log_files( rootDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		if ( !setup_graphic_properties_file( graphicProperties, rootDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		if ( !setup_chart_properties_file( chartProperties, rootDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		if ( !MainFrame.get_instance().setup_chart_data_files( rootDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		if ( !MainFrame.get_instance().setup_image_files( rootDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		if ( !MainFrame.get_instance().setup_thumbnail_image_files( rootDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		if ( !run_animator( parentDirectory, rootDirectory, soarsFile, id, title, visualShellTitle)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		return true;
	}

	/**
	 * @return
	 */
	public boolean dollar_spot_log_exists() {
		for ( int i = 0; i < _tabbedPane.getComponentCount(); ++i) {
			if ( _tabbedPane.getTitleAt( i).equals( "$Spot")) {
				JTextArea textArea = ( JTextArea)_textAreaMap.get( "$Spot");
				if ( null == textArea)
					return false;

				return !textArea.getText().equals( "");
			}
		}
		return false;
	}

	/**
	 * @param rootDirectory
	 * @return
	 */
	private boolean setup_log_files(File rootDirectory) {
		// ログをタブのテキストエリアから取得するのはログファイルが無い場合(初めてVisualShellから起動された時)だけにすること！
		// 一度ファイルへ書き込んだテキストから生成されたテキストエリアの文字列は、初めてテキストエリアへ書き込まれた文字列と異なるのでAnimatorが読み込めなくなる
		File agentsDirectory = new File( rootDirectory, "agents");
		agentsDirectory.mkdir();

		File spotsDirectory = new File( rootDirectory, "spots");
		spotsDirectory.mkdir();

		if ( MainFrame.get_instance().exist_agent_log_directory() && MainFrame.get_instance().exist_spot_log_directory()) {
			if ( !copy( MainFrame.get_instance().get_agent_log_directory(), agentsDirectory))
				return false;
			if ( !copy( MainFrame.get_instance().get_spot_log_directory(), spotsDirectory))
				return false;
		} else {
			for ( int i = 0; i < _tabbedPane.getComponentCount(); ++i) {
				Component component = _tabbedPane.getComponentAt( i);
				if ( ignore( component))
					continue;

				String title = _tabbedPane.getTitleAt( i);
				JTextArea textArea = ( JTextArea)_textAreaMap.get( title);
				if ( null == textArea)
					return false;

				if ( !setup_log_file( title.startsWith( "<>") ? title.substring( "<>".length()) : title,
					textArea, title.startsWith( "<>") ? spotsDirectory : agentsDirectory))
					return false;
			}
		}
		return true;
	}

	/**
	 * @param srcDirectory
	 * @param destDirectory
	 * @return
	 */
	private boolean copy(File srcDirectory, File destDirectory) {
		File[] files = srcDirectory.listFiles();
		for ( File file:files) {
			//JOptionPane.showMessageDialog( MainFrame.get_instance(), file.getName());
			if ( !file.isFile())
				continue;

			if ( !file.getName().endsWith( ".log"))
				continue;

			if ( !FileUtility.copy( file, new File( destDirectory, file.getName())))
				return false;
		}
		return true;
	}

	/**
	 * @param name
	 * @param textArea
	 * @param directory
	 * @return
	 */
	private boolean setup_log_file(String name, JTextArea textArea, File directory) {
		File file = new File( directory, name + ".log");
		return FileUtility.write_text_to_file( file, textArea.getText(), "UTF-8");
	}

	/**
	 * @param graphicProperties
	 * @param rootDirectory
	 * @return
	 */
	private boolean setup_graphic_properties_file(String graphicProperties, File rootDirectory) {
		File file = new File( rootDirectory, Constant._graphicPropertiesFilename);
		return FileUtility.write_text_to_file( file, graphicProperties, "UTF-8");
	}

	/**
	 * @param chartProperties
	 * @param rootDirectory
	 * @return
	 */
	private boolean setup_chart_properties_file(String chartProperties, File rootDirectory) {
		File file = new File( rootDirectory, Constant._chartPropertiesFilename);
		return FileUtility.write_text_to_file( file, chartProperties, "UTF-8");
	}

	/**
	 * @param parentDirectory
	 * @param rootDirectory 
	 * @param soarsFile
	 * @param id
	 * @param title
	 * @param visualShellTitle
	 * @return
	 */
	private boolean run_animator(File parentDirectory, File rootDirectory, File soarsFile, long id, String title, String visualShellTitle) {
		String currentDirectoryName = System.getProperty( Constant._soarsHome);
		File currentDirectory = new File( currentDirectoryName);
		if ( null == currentDirectory)
			return false;

		return Animator.run( currentDirectory, currentDirectoryName, parentDirectory, rootDirectory, soarsFile, id, title, visualShellTitle);
	}

	/**
	 * @param rootDirectory
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(File rootDirectory, Writer writer) throws SAXException {
		String console = "";
		String stdout = "";
		String stderr = "";
		List agents = new ArrayList();
		List spots = new ArrayList();
		for ( int i = 0; i < _tabbedPane.getComponentCount(); ++i) {
			Component component = _tabbedPane.getComponentAt( i);
			String text = get_console_text( component);
			if ( null != text)
				console = text;
			else {
				if ( component.equals( _stdOutPropertyPage))
					stdout = _stdOutPropertyPage.getText();
				else if ( component.equals( _stdErrOutPropertyPage))
					stderr = _stdErrOutPropertyPage.getText();
				else {
					String title = _tabbedPane.getTitleAt( i);
					if ( title.endsWith( " - " + ResourceManager.get_instance().get( "log.viewer.state.logging")))
						title = title.substring( 0, title.length() - ( " - " + ResourceManager.get_instance().get( "log.viewer.state.logging")).length());
						
					JTextArea textArea = ( JTextArea)_textAreaMap.get( title);
					if ( null == textArea)
						return false;

					get_log( title.startsWith( "<>") ? title.substring( "<>".length()) : title,
						textArea, title.startsWith( "<>") ? spots : agents);
				}
			}
		}

		Rectangle rectangle = getBounds();

		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "title", "", Writer.escapeAttributeCharData( getTitle()));
		attributesImpl.addAttribute( null, null, "x", "", String.valueOf( rectangle.x));
		attributesImpl.addAttribute( null, null, "y", "", String.valueOf( rectangle.y));
		attributesImpl.addAttribute( null, null, "width", "", String.valueOf( rectangle.width));
		attributesImpl.addAttribute( null, null, "height", "", String.valueOf( rectangle.height));
		writer.startElement( null, null, "log_viewer_data", attributesImpl);

		if ( !write( console, rootDirectory, Constant._consoleFilename))
			return false;

		if ( !write( stdout, rootDirectory, Constant._standardOutFilename))
			return false;

		if ( !write( stderr, rootDirectory, Constant._standardErrorFilename))
			return false;

		File agents_directory = new File( rootDirectory, "agents");
		if ( !agents_directory.exists() && !agents_directory.mkdir())
			return false;

		File spots_directory = new File( rootDirectory, "spots");
		if ( !spots_directory.exists() && !spots_directory.mkdir())
			return false;

		if ( !write( "agents", agents, agents_directory, writer))
			return false;

		if ( !write( "spots", spots, spots_directory, writer))
			return false;

		writer.endElement( null, null, "log_viewer_data");

		return true;
	}


	/**
	 * @param component
	 * @return
	 */
	protected String get_console_text(Component component) {
		return null;
	}

	/**
	 * @param name
	 * @param textArea
	 * @param list
	 */
	private void get_log(String name, JTextArea textArea, List<LogData> list) {
		list.add( new LogData( name, textArea.getText()));
	}

	/**
	 * @param value
	 * @param rootDirectory
	 * @param filename
	 * @return
	 */
	private boolean write(String value, File rootDirectory, String filename) {
		File file = new File( rootDirectory, filename);
		return FileUtility.write_text_to_file( file, value, "UTF-8");
	}

	/**
	 * @param name
	 * @param list
	 * @param directory
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write(String name, List<LogData> list, File directory, Writer writer) throws SAXException {
		if ( list.isEmpty())
			return true;

		writer.startElement( null, null, name, new AttributesImpl());

		for ( int i = 0; i < list.size(); ++i) {
			if ( !write( i, list.get( i), directory, writer))
				return false;
		}

		writer.endElement( null, null, name);

		return true;
	}

	/**
	 * @param index
	 * @param logData
	 * @param directory
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write(int index, LogData logData, File directory, Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "name", "", logData._name);
		writer.writeElement( null, null, "log" + String.valueOf( index), attributesImpl);

		File file = new File( directory, logData._name + ".log");
		return FileUtility.write_text_to_file( file, logData._value, "UTF-8");
	}

	/**
	 * @param logViewerWindowTitle
	 * @param windowRectangle
	 * @param console
	 * @param agents
	 * @param spots
	 * @param stdout
	 * @param stderr
	 * @return
	 */
	public boolean update(String logViewerWindowTitle, Rectangle windowRectangle, String console, List<LogData> agents, List<LogData> spots, String stdout, String stderr) {
		setTitle( logViewerWindowTitle);

		while ( clear())
			;

		_textAreaMap.clear();

		append( agents, "");
		append( spots, "<>");

		if ( null != _stdOutPropertyPage)
			_stdOutPropertyPage.setText( stdout);

		if ( null != _stdErrOutPropertyPage)
			_stdErrOutPropertyPage.setText( stderr);

		_windowRectangle = windowRectangle;
		setLocation( _windowRectangle.x, _windowRectangle.y);
		setSize( _windowRectangle.width, _windowRectangle.height);

		return true;
	}

	/**
	 * @return
	 */
	private boolean clear() {
		for ( int i = 0; i < _tabbedPane.getComponentCount(); ++i) {
			Component component = _tabbedPane.getComponentAt( i);
			if ( ignore( component))
				continue;

			_tabbedPane.remove( i);
			return true;
		}

		return false;
	}

	/**
	 * @param list
	 * @param prefix
	 */
	private void append(List<LogData> list, String prefix) {
		for ( LogData logData:list) {
			LogPropertyPage logPropertyPage = new LogPropertyPage( _tabbedPane);
			if ( !logPropertyPage.create( prefix, logData, _textAreaMap))
				return;
		}
	}

	/**
	 * @param component
	 * @return
	 */
	protected boolean ignore(Component component) {
		return false;
	}
}
