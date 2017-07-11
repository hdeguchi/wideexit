/**
 * 
 */
package soars.application.visualshell.object.arbitrary.select;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.w3c.dom.Node;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.common.arbitrary.ClassTree;
import soars.application.visualshell.object.common.arbitrary.IClassTreeCallback;
import soars.common.utility.swing.window.Dialog;
import soars.common.utility.xml.dom.XmlTool;

/**
 * @author kurata
 *
 */
public class SelectJarAndClassDlg extends Dialog implements IClassTreeCallback {

	/**
	 * 
	 */
	private int _minimumWidth = -1;

	/**
	 * 
	 */
	private int _minimumHeight = -1;

	/**
	 * 
	 */
	private JTextField _currentJarFilenameTextField = null;

	/**
	 * 
	 */
	private JTextField _currentClassnameTextField = null;

	/**
	 * 
	 */
	private JTextField _jarFilenameTextField = null;

	/**
	 * 
	 */
	private JTextField _classnameTextField = null;

	/**
	 * 
	 */
	private JLabel[] _labels = new JLabel[] {
		null, null, null, null
	};

	/**
	 * 
	 */
	private ClassTree _classTree = null;

	/**
	 * 
	 */
	private JScrollPane _scrollPane = null;

	/**
	 * 
	 */
	private JButton _okButton = null;

	/**
	 * 
	 */
	private JButton _ignoreButton = null;

	/**
	 * 
	 */
	private JButton _abortButton = null;

	/**
	 * 
	 */
	private String _jarFilename = "";

	/**
	 * 
	 */
	private String _classname = "";

	/**
	 * 
	 */
	private JarAndClassMap _jarAndClassMap = null;

	/**
	 * 
	 */
	public JarAndClass _jarAndClass = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param jarFilename
	 * @param classname
	 * @param jarAndClassMap
	 * @throws HeadlessException
	 */
	public SelectJarAndClassDlg(Frame arg0, String arg1, boolean arg2, String jarFilename, String classname, JarAndClassMap jarAndClassMap) throws HeadlessException {
		super(arg0, arg1, arg2);
		_jarFilename = jarFilename;
		_classname = classname;
		_jarAndClassMap = jarAndClassMap;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.common.arbitrary.IClassTreeCallback#selected(org.w3c.dom.Node)
	 */
	public void selected(Node node) {
		if ( null == node || !node.getNodeName().equals( "class")) {
			_jarFilenameTextField.setText( "");
			_classnameTextField.setText( "");
			return;
		}

		String value = XmlTool.get_attribute( node, "name");
		if ( null != value)
			_classnameTextField.setText( value);

		node = node.getParentNode();
		if ( null != node) {
			value = XmlTool.get_attribute( node, "name");
			if ( null != value)
				_jarFilenameTextField.setText( value);
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		link_to_cancel( getRootPane());


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		insert_horizontal_glue();

		setup_current_jar_filename_textField();

		insert_horizontal_glue();

		setup_current_classname_textField();

		insert_horizontal_glue();

		setup_jar_filename_textField();

		insert_horizontal_glue();

		setup_classname_textField();

		insert_horizontal_glue();

		if ( !setup_classTree())
			return false;

		insert_horizontal_glue();

		setup_buttons();

		insert_horizontal_glue();


		adjust();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		return true;
	}

	/**
	 * 
	 */
	private void setup_current_jar_filename_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_labels[ 0] = new JLabel( ResourceManager.get_instance().get( "select.jar.and.class.dialog.current.jar.filename"));
		_labels[ 0].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _labels[ 0]);

		panel.add( Box.createHorizontalStrut( 5));

		_currentJarFilenameTextField = new JTextField( _jarFilename);
		_currentJarFilenameTextField.setEditable( false);
		_currentJarFilenameTextField.setPreferredSize( new Dimension( 400, _currentJarFilenameTextField.getPreferredSize().height));

		link_to_cancel( _currentJarFilenameTextField);

		panel.add( _currentJarFilenameTextField);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_current_classname_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_labels[ 1] = new JLabel( ResourceManager.get_instance().get( "select.jar.and.class.dialog.current.classname"));
		_labels[ 1].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _labels[ 1]);

		panel.add( Box.createHorizontalStrut( 5));

		_currentClassnameTextField = new JTextField( _classname);
		_currentClassnameTextField.setEditable( false);
		_currentClassnameTextField.setPreferredSize( new Dimension( 400, _currentClassnameTextField.getPreferredSize().height));

		link_to_cancel( _currentClassnameTextField);

		panel.add( _currentClassnameTextField);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_jar_filename_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_labels[ 2] = new JLabel( ResourceManager.get_instance().get( "select.jar.and.class.dialog.jar.filename"));
		_labels[ 2].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _labels[ 2]);

		panel.add( Box.createHorizontalStrut( 5));

		_jarFilenameTextField = new JTextField();
		_jarFilenameTextField.setEditable( false);
		_jarFilenameTextField.setPreferredSize( new Dimension( 400, _jarFilenameTextField.getPreferredSize().height));

		link_to_cancel( _jarFilenameTextField);

		panel.add( _jarFilenameTextField);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_classname_textField() {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_labels[ 3] = new JLabel( ResourceManager.get_instance().get( "select.jar.and.class.dialog.classname"));
		_labels[ 3].setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( _labels[ 3]);

		panel.add( Box.createHorizontalStrut( 5));

		_classnameTextField = new JTextField();
		_classnameTextField.setEditable( false);
		_classnameTextField.setPreferredSize( new Dimension( 400, _classnameTextField.getPreferredSize().height));

		link_to_cancel( _classnameTextField);

		panel.add( _classnameTextField);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * @return
	 */
	private boolean setup_classTree() {
		_classTree = new ClassTree( ( Frame)getOwner(), this, this);
		if ( !_classTree.setup( false))
			return false;

		link_to_cancel( _classTree);

		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_scrollPane = new JScrollPane();
		_scrollPane.getViewport().setView( _classTree);

		link_to_cancel( _scrollPane);

		panel.add( _scrollPane);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);

		return true;
	}

	/**
	 * 
	 */
	private void setup_buttons() {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		_okButton = new JButton( ResourceManager.get_instance().get( "dialog.ok"));

		getRootPane().setDefaultButton( _okButton);

		_okButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_ok( arg0);
			}
		});

		link_to_cancel( _okButton);

		panel.add( _okButton);


		_ignoreButton = new JButton( ResourceManager.get_instance().get( "dialog.ignore"));
		_ignoreButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_ignore( arg0);
			}
		});

		link_to_cancel( _ignoreButton);

		panel.add( _ignoreButton);


		_abortButton = new JButton( ResourceManager.get_instance().get( "dialog.abort"));
		_abortButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_abort( arg0);
			}
		});

		link_to_cancel( _abortButton);

		panel.add( _abortButton);


		int width = _okButton.getPreferredSize().width;
		width = Math.max( width, _ignoreButton.getPreferredSize().width);
		width = Math.max( width, _abortButton.getPreferredSize().width);

		_okButton.setPreferredSize( new Dimension( width, _okButton.getPreferredSize().height));
		_ignoreButton.setPreferredSize( new Dimension( width, _ignoreButton.getPreferredSize().height));
		_abortButton.setPreferredSize( new Dimension( width, _abortButton.getPreferredSize().height));

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;

		for ( int i = 0; i < _labels.length; ++i)
			width = Math.max( width, _labels[ i].getPreferredSize().width);

		for ( int i = 0; i < _labels.length; ++i)
			_labels[ i].setPreferredSize( new Dimension(
				width, _labels[ i].getPreferredSize().height));

		_scrollPane.setPreferredSize( new Dimension( width + 5 + _currentJarFilenameTextField.getPreferredSize().width, 350));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		_classTree.requestFocusInWindow();

		if ( null == _classTree)
			return;

		_minimumWidth = getPreferredSize().width;
		_minimumHeight = getPreferredSize().height;

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				if ( 0 > _minimumWidth || 0 > _minimumHeight)
					return;

				int width = getSize().width;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width, _minimumHeight);
			}
		});
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		// 新たなjarFilenameとclassnameが選択された場合は、新たなJarAndClassを作って_jarAndClassへセットしマップへ追加する
		String jarFilename = _jarFilenameTextField.getText();
		if ( null == jarFilename || jarFilename.equals( ""))
			return;

		String classname = _classnameTextField.getText();
		if ( null == classname || classname.equals( ""))
			return;

		_jarAndClass = new JarAndClass( jarFilename, classname);
		_jarAndClassMap.put( new JarAndClass( _jarFilename, _classname), _jarAndClass);

		super.on_ok(actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_ignore(ActionEvent actionEvent) {
		// 無視する場合はそのままの_jarFilenameと_classnameでJarAndClassを作って_jarAndClassへセットする
		_jarAndClass = new JarAndClass( _jarFilename, _classname);
		super.on_ok(actionEvent);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_abort(ActionEvent actionEvent) {
		// 読み込みを中止する場合は_jarAndClassへnullをセットする
		_jarAndClass = null;
		super.on_cancel(actionEvent);
	}
}
