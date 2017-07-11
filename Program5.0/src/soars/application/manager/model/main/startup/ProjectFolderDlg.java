/**
 * 
 */
package soars.application.manager.model.main.startup;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.manager.model.common.tool.CommonTool;
import soars.application.manager.model.main.Constant;
import soars.application.manager.model.main.Environment;
import soars.application.manager.model.main.ResourceManager;
import soars.common.soars.environment.BasicEnvironment;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.swing.window.Dialog;
import soars.common.utility.tool.resource.Resource;

/**
 * @author kurata
 *
 */
public class ProjectFolderDlg extends Dialog {

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
	private int _numberOfProjectFolder = 10;

	/**
	 * 
	 */
	private String _buttonName = null;

	/**
	 * 
	 */
	private JComboBox _projectFolderComboBox = null;

	/**
	 * 
	 */
	private JButton _selectProjectFolderButton = null;

	/**
	 * 
	 */
	private JCheckBox _usePropertyInProjectFolderCheckBox = null;

	/**
	 * 
	 */
	private boolean _propertyInProjectFolderCheckBox = false;

	/**
	 * 
	 */
	private JCheckBox _confirmCheckBox = null;

	/**
	 * 
	 */
	private List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param buttonName 
	 * @param propertyInProjectFolderCheckBox
	 * @throws HeadlessException
	 */
	public ProjectFolderDlg(Frame arg0, String arg1, boolean arg2, String buttonName, boolean propertyInProjectFolderCheckBox) throws HeadlessException {
		super(arg0, arg1, arg2);
		_buttonName = buttonName;
		_propertyInProjectFolderCheckBox = propertyInProjectFolderCheckBox;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				if ( 0 > _minimumWidth || 0 > _minimumHeight)
					return;

				int width = getSize().width;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width, _minimumHeight);
			}
		});


		link_to_cancel( getRootPane());


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		insert_horizontal_glue();

		setup_projectFolderComboBox();

		insert_horizontal_glue();

		if ( _propertyInProjectFolderCheckBox) {
			setup_usePropertyInProjectFolderCheckBox();
			insert_horizontal_glue();
		}

		setup_confirmCheckBox();

		insert_horizontal_glue();

		setup_ok_and_cancel_button( ResourceManager.get_instance().get( "dialog.ok"), _buttonName, true, true);

		insert_horizontal_glue();


		adjust();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		Image image = Resource.load_image_from_resource( Constant._resourceDirectory + "/image/icon/icon.png", getClass());
		if ( null != image)
			setIconImage( image);

		return true;
	}

	/**
	 * 
	 */
	private void setup_projectFolderComboBox() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "project.folder.dialog.folder"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( label);
		_labels.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_projectFolderComboBox = new JComboBox( get_project_folders());
		_projectFolderComboBox.setEditable( true);
		_projectFolderComboBox.setPreferredSize( new Dimension( 400, _projectFolderComboBox.getPreferredSize().height));

		link_to_cancel( _projectFolderComboBox);

		panel.add( _projectFolderComboBox);

		panel.add( Box.createHorizontalStrut( 5));

		_selectProjectFolderButton = new JButton( ResourceManager.get_instance().get( "project.folder.dialog.folder.button"));
		_selectProjectFolderButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_selectFolderButton( e);
			}
		});

		link_to_cancel( _selectProjectFolderButton);

		panel.add( _selectProjectFolderButton);

		panel.add( Box.createHorizontalStrut( 5));

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_usePropertyInProjectFolderCheckBox() {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_usePropertyInProjectFolderCheckBox = new JCheckBox( ResourceManager.get_instance().get( "project.folder.dialog.use.property.in.project.folder.check.box"),
			BasicEnvironment.get_instance().get( BasicEnvironment._usePropertyInProjectFolderKey, "true").equals( "true"));

		link_to_cancel( _usePropertyInProjectFolderCheckBox);

		panel.add( _usePropertyInProjectFolderCheckBox);

		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void setup_confirmCheckBox() {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_confirmCheckBox = new JCheckBox( ResourceManager.get_instance().get( "project.folder.dialog.confirm.check.box"),
			BasicEnvironment.get_instance().get( BasicEnvironment._defaultProjectFolderKey, "false").equals( "true"));

		link_to_cancel( _confirmCheckBox);

		panel.add( _confirmCheckBox);

		getContentPane().add( panel);
	}

	/**
	 * @return
	 */
	private Vector<String> get_project_folders() {
		Vector<String> projectFolders = new Vector<String>();
		for ( int i = 0; i < _numberOfProjectFolder; ++i) {
			String path = BasicEnvironment.get_instance().get( BasicEnvironment._projectFolderKey + String.valueOf( i), "");
			if ( path.equals( ""))
				break;

			File folder = new File( path);
			if ( !folder.exists() || !folder.isDirectory())
				continue;

			projectFolders.add( path);
		}

		if ( projectFolders.isEmpty()) {
			String path = ( System.getProperty( "user.home") + File.separator + "SOARS" + File.separator + "workspace");
			BasicEnvironment.get_instance().set( BasicEnvironment._projectFolderKey + "0", path);
			projectFolders.add( path);
		}

		return projectFolders;
	}

	/**
	 * @param actionEvent
	 */
	protected void on_selectFolderButton(ActionEvent actionEvent) {
		File folder = CommonTool.get_directory( BasicEnvironment.get_instance(), BasicEnvironment._projectFolderDirectoryKey, ResourceManager.get_instance().get( "project.folder.dialog.title"), this);
		if ( null == folder)
			return;

		_projectFolderComboBox.getEditor().setItem( folder.getAbsolutePath());
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( JLabel label:_labels)
			width = Math.max( label.getPreferredSize().width, width);
		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		_projectFolderComboBox.requestFocusInWindow();

		_minimumWidth = getPreferredSize().width;
		_minimumHeight = getPreferredSize().height;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		File projectFolder = new File( ( String)_projectFolderComboBox.getEditor().getItem());
		if ( projectFolder.exists() && !projectFolder.isDirectory()) {
			JOptionPane.showMessageDialog( this,
				ResourceManager.get_instance().get( "project.folder.dialog.error.new.project.folder.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}

		if ( !projectFolder.exists()) {
			if ( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog( this,
				ResourceManager.get_instance().get( "project.folder.dialog.confirm.new.project.folder.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.YES_NO_OPTION))
				return;

			if ( !projectFolder.mkdirs()) {
				JOptionPane.showMessageDialog( this,
					ResourceManager.get_instance().get( "project.folder.dialog.error.new.project.folder.message"),
					ResourceManager.get_instance().get( "application.title"),
					JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		if ( !BasicEnvironment.get_instance().create_projectSubFolers( projectFolder)) {
			JOptionPane.showMessageDialog( this,
				ResourceManager.get_instance().get( "project.folder.dialog.error.new.project.folder.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}

		append_new_project_folder( projectFolder);

		if ( _propertyInProjectFolderCheckBox)
			set_propertyDirectory( projectFolder, _usePropertyInProjectFolderCheckBox.isSelected());
		else {
			String value = BasicEnvironment.get_instance().get( BasicEnvironment._usePropertyInProjectFolderKey, "");
			set_propertyDirectory( projectFolder, value.equals( "true") || value.equals( ""));
		}

		BasicEnvironment.get_instance().set( BasicEnvironment._defaultProjectFolderKey, String.valueOf( _confirmCheckBox.isSelected()));

		BasicEnvironment.get_instance().set( BasicEnvironment._projectFolderDirectoryKey, projectFolder.getAbsolutePath());

		super.on_ok(actionEvent);
	}

	/**
	 * @param projectFolder
	 * @param usePropertyInProjectFolder
	 */
	private void set_propertyDirectory(File projectFolder, boolean usePropertyInProjectFolder) {
		BasicEnvironment.get_instance().set( BasicEnvironment._usePropertyInProjectFolderKey, String.valueOf( usePropertyInProjectFolder));

		File propertyDirectory = null;
		if ( !usePropertyInProjectFolder) {
			propertyDirectory = SoarsCommonTool.get_default_property_directory();
			if ( null == propertyDirectory) {
				JOptionPane.showMessageDialog( this,
					ResourceManager.get_instance().get( "project.folder.dialog.error.new.project.folder.message"),
					ResourceManager.get_instance().get( "application.title"),
					JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else {
			propertyDirectory = new File( projectFolder, ".soars");
			if ( !propertyDirectory.exists()) {
				if ( !propertyDirectory.mkdirs()) {
					JOptionPane.showMessageDialog( this,
						ResourceManager.get_instance().get( "project.folder.dialog.error.new.project.folder.message"),
						ResourceManager.get_instance().get( "application.title"),
						JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else {
				if ( !propertyDirectory.isDirectory()) {
					JOptionPane.showMessageDialog( this,
						ResourceManager.get_instance().get( "project.folder.dialog.error.new.project.folder.message"),
						ResourceManager.get_instance().get( "application.title"),
						JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		}
		System.setProperty( Constant._soarsProperty, propertyDirectory.getAbsolutePath());
		Environment.get_instance().update();
		CommonEnvironment.get_instance().update();
	}

	/**
	 * @param projectFolder
	 */
	private void append_new_project_folder(File projectFolder) {
		BasicEnvironment.get_instance().set( BasicEnvironment._projectFolderKey + "0", projectFolder.getAbsolutePath());

		int index = 1;
		for ( int i = 0; i < _projectFolderComboBox.getItemCount(); ++i) {
			if ( _numberOfProjectFolder <= index)
				break;

			if ( projectFolder.getAbsolutePath().equals( ( String)_projectFolderComboBox.getItemAt( i)))
				continue;

			BasicEnvironment.get_instance().set( BasicEnvironment._projectFolderKey + String.valueOf( index++), ( String)_projectFolderComboBox.getItemAt( i));
		}
	}
}
