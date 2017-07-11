/**
 * 
 */
package soars.tool.animator.launcher.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.json.JSONArray;
import org.json.JSONException;

import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;
import soars.common.utility.swing.mac.IMacScreenMenuHandler;
import soars.common.utility.swing.mac.MacUtility;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Frame;
import soars.tool.animator.launcher.executor.animator.Animator;

/**
 * @author kurata
 *
 */
public class MainFrame extends Frame implements IMacScreenMenuHandler {

	/**
	 * 
	 */
	static public final int _minimum_width = 640;

	/**
	 * 
	 */
	static public final int _minimum_height = 480;

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private MainFrame _mainFrame = null;

	/**
	 * 
	 */
	private Rectangle _window_rectangle = new Rectangle();

	/**
	 * 
	 */
	private JLabel _memory_size_label = null;

	/**
	 * 
	 */
	private JLabel _title_label = null;

	/**
	 * 
	 */
	private JLabel _comment_label = null;

	/**
	 * 
	 */
	private ComboBox _memory_size_comboBox = null;

	/**
	 * 
	 */
	private JTextField _title_textField = null;

	/**
	 * 
	 */
	private JTextArea _comment_textArea = null;

	/**
	 * @return
	 */
	public static MainFrame get_instance() {
		synchronized( _lock) {
			if ( null == _mainFrame) {
				_mainFrame = new MainFrame( ResourceManager.get_instance().get( "application.title"));
			}
		}
		return _mainFrame;
	}

	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public MainFrame(String arg0) throws HeadlessException {
		super(arg0);
	}

	/**
	 * 
	 */
	private void get_property_from_environment_file() {
		String value = Environment.get_instance().get(
			Environment._main_window_rectangle_key + "x",
			String.valueOf( SwingTool.get_default_window_position( _minimum_width, _minimum_height).x));
		_window_rectangle.x = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._main_window_rectangle_key + "y",
			String.valueOf( SwingTool.get_default_window_position( _minimum_width, _minimum_height).y));
		_window_rectangle.y = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._main_window_rectangle_key + "width",
			String.valueOf( _minimum_width));
		_window_rectangle.width = Integer.parseInt( value);

		value = Environment.get_instance().get(
			Environment._main_window_rectangle_key + "height",
			String.valueOf( _minimum_height));
		_window_rectangle.height = Integer.parseInt( value);
	}

	/**
	 * @return
	 */
	private Rectangle get_optimized_window_rectangle() {
		if ( !GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersects( _window_rectangle)
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( _window_rectangle).width <= 10
			|| _window_rectangle.y <= -getInsets().top
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( _window_rectangle).height <= getInsets().top)
			_window_rectangle.setBounds(
				SwingTool.get_default_window_position( _minimum_width, _minimum_height).x,
				SwingTool.get_default_window_position( _minimum_width, _minimum_height).y,
				_minimum_width, _minimum_height);
		return _window_rectangle;
	}

	/**
	 * 
	 */
	private void set_property_to_environment_file() {
		Environment.get_instance().set(
			Environment._main_window_rectangle_key + "x", String.valueOf( _window_rectangle.x));
		Environment.get_instance().set(
			Environment._main_window_rectangle_key + "y", String.valueOf( _window_rectangle.y));
		Environment.get_instance().set(
			Environment._main_window_rectangle_key + "width", String.valueOf( _window_rectangle.width));
		Environment.get_instance().set(
			Environment._main_window_rectangle_key + "height", String.valueOf( _window_rectangle.height));

		Environment.get_instance().set(
			Environment._memory_size_key, get_memory_size());

		Environment.get_instance().store();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;


		if ( !MacUtility.setup_screen_menu_handler( this, this, ResourceManager.get_instance().get( "application.title")))
			return false;


		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				int width = getSize().width;
				int height = getSize().height;
				setSize( ( _minimum_width > width) ? _minimum_width : width,
					( _minimum_height > height) ? _minimum_height : height);
			}
		});


		JSONArray jsonArray = load();
		if ( null == jsonArray)
			return false;


		if ( !setup( jsonArray))
			return false;


		get_property_from_environment_file();

		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

		pack();

		setBounds( get_optimized_window_rectangle());

		Toolkit.getDefaultToolkit().setDynamicLayout( true);

		setVisible( true);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.mac.IMacScreenMenuHandler#on_mac_about()
	 */
	public void on_mac_about() {
		on_help_about( null);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.mac.IMacScreenMenuHandler#on_mac_quit()
	 */
	public void on_mac_quit() {
		on_window_closing( null);
	}

	/**
	 * @return
	 */
	private JSONArray load() {
		if ( null != Application._parameter_file) {
			JSONArray jsonArray = load( Application._parameter_file);
			Application._parameter_file.delete();
			Application._parameter_file = null;
			return jsonArray;
		} else {
			String home_directory = System.getProperty( Constant._soarsHome);
			if ( null == home_directory)
				return null;

			File file = new File( home_directory + "/../" + Constant._animatorRunnerDataDirectory + "/" + Constant._animatorRunnerParameterFilename);

			JSONArray jsonArray = load( file);
			return jsonArray;
		}
	}

	/**
	 * @param file
	 * @return
	 */
	private JSONArray load(File file) {
		if ( null == file || !file.exists() || !file.isFile() || !file.canRead())
			return null;

		try {
			BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( new FileInputStream( file), "UTF-8"));
			String line = bufferedReader.readLine();
			bufferedReader.close();
			if ( null == line)
				return null;

			return new JSONArray( line);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param jsonArray
	 * @return
	 */
	private boolean setup(JSONArray jsonArray) {
		if ( !Constant.setup( jsonArray))
			return false;


		setLayout( new BorderLayout());


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_memory_size_comboBox( northPanel);

		insert_horizontal_glue( northPanel);

		if ( !setup_title_textField( jsonArray, northPanel))
			return false;

		insert_horizontal_glue( northPanel);

		add( northPanel, "North");


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		if ( !setup_comment_textArea( jsonArray, centerPanel))
			return false;

		add( centerPanel);


		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( southPanel);

		setup_launch_button( southPanel);

		insert_horizontal_glue( southPanel);

		add( southPanel, "South");


		adjust();


		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_memory_size_comboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_memory_size_label = new JLabel( ResourceManager.get_instance().get( "main.frame.memory.size.label"));
		_memory_size_label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _memory_size_label);

		_memory_size_comboBox = ComboBox.create(
			new String[] { ResourceManager.get_instance().get( "main.frame.memory.non.use"),
				"64", "128", "256", "512", "1024", "2048", "4096"},
			150, true, new CommonComboBoxRenderer( null, true));
		_memory_size_comboBox.setSelectedItem( Environment.get_instance().get(
			Environment._memory_size_key, ResourceManager.get_instance().get( "main.frame.memory.non.use")));
		panel.add( _memory_size_comboBox);

		JLabel label = new JLabel( "MB");
		panel.add( label);

		parent.add( panel);
	}

	/**
	 * @param jsonArray
	 * @param parent
	 * @return
	 */
	private boolean setup_title_textField(JSONArray jsonArray, JPanel parent) {
		String title = "";
		try {
			title = jsonArray.getString( 1);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_title_label = new JLabel( ResourceManager.get_instance().get( "main.frame.title.label"));
		_title_label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _title_label);

		panel.add( Box.createHorizontalStrut( 5));

		_title_textField = new JTextField( title);
		_title_textField.setEditable( false);
		panel.add( _title_textField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param jsonArray
	 * @param parent
	 * @return
	 */
	private boolean setup_comment_textArea(JSONArray jsonArray, JPanel parent) {
		String comment = "";
		try {
			comment = jsonArray.getString( 2);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_comment_label = new JLabel( ResourceManager.get_instance().get( "main.frame.comment.label"));
		_comment_label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _comment_label);

		panel.add( Box.createHorizontalStrut( 5));

		_comment_textArea = new JTextArea( comment);
		_comment_textArea.setEditable( false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _comment_textArea);

		panel.add( scrollPane);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_launch_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		JButton button = new JButton( ResourceManager.get_instance().get( "main.frame.launch.button"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_launch_animator( arg0);
			}
		});
		panel.add( button);

		parent.add( panel);
	}

	/**
	 * @param ActionEvent
	 */
	protected void on_launch_animator(ActionEvent actionEvent) {
		if ( !Animator.run( get_memory_size(), get_title()))
			JOptionPane.showMessageDialog(
				this,
				ResourceManager.get_instance().get( "main.frame.could.not.launch.animator.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @return
	 */
	private String get_title() {
		String title = _title_textField.getText();
		return ( title.equals( ResourceManager.get_instance().get( "animator.title.no.name")) ? "" : title);
	}

	/**
	 * @return
	 */
	private String get_memory_size() {
		String memory_size = ( String)_memory_size_comboBox.getSelectedItem();
		return ( memory_size.equals( ResourceManager.get_instance().get( "main.frame.memory.non.use")) ? "0" : memory_size);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = _memory_size_label.getPreferredSize().width;
		width = Math.max( width, _title_label.getPreferredSize().width);
		width = Math.max( width, _comment_label.getPreferredSize().width);

		Dimension dimension = new Dimension( width,
			_memory_size_label.getPreferredSize().height);

		_memory_size_label.setPreferredSize( dimension);
		_title_label.setPreferredSize( dimension);
		_comment_label.setPreferredSize( dimension);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_window_closing(java.awt.event.WindowEvent)
	 */
	protected void on_window_closing(WindowEvent windowEvent) {
		if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
			this,
			ResourceManager.get_instance().get( "file.exit.confirm.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION)) {
			requestFocus();
			return;
		}

		_window_rectangle = getBounds();
		set_property_to_environment_file();
		System.exit( 0);
	}

	/**
	 * @param actionEvent
	 */
	public void on_help_about(ActionEvent actionEvent) {
		JOptionPane.showMessageDialog( this,
			Constant.get_version_message(),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.INFORMATION_MESSAGE);

		requestFocus();
	}
}
