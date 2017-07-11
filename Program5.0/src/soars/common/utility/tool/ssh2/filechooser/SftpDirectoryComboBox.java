/**
 * 
 */
package soars.common.utility.tool.ssh2.filechooser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.plaf.basic.BasicComboPopup;

/**
 * @author kurata
 *
 */
public class SftpDirectoryComboBox extends JComboBox {

	/**
	 * 
	 */
	private DefaultComboBoxModel _defaultComboBoxModel = null;

	/**
	 * 
	 */
	private SftpFileChooser _sftpFileChooser = null;

	/**
	 * @param sftpFileChooser
	 * 
	 */
	public SftpDirectoryComboBox(SftpFileChooser sftpFileChooser) {
		super();
		_sftpFileChooser = sftpFileChooser;
	}

	/**
	 * @return
	 */
	public boolean setup() {
		_defaultComboBoxModel = new DefaultComboBoxModel();
		setModel( _defaultComboBoxModel);

		BasicComboPopup basicComboPopup = new BasicComboPopup( this) {
			public boolean isFocusable() {
				return true;
			}
		};

		addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_selected( e);
			}
		});

		setRenderer( new SftpDirectoryComboBoxCellRenderer());

		return true;
	}

	/**
	 * @param actionEvent
	 */
	protected void on_selected(ActionEvent actionEvent) {
		int index = getSelectedIndex();
		if ( 0 > index || getItemCount() <= index)
			return;

		_sftpFileChooser.update( ( String)getItemAt( index));
	}

	/**
	 * @param directory
	 * @param rootDirectory
	 * @return
	 */
	public boolean update(String directory, String rootDirectory) {
		if ( !directory.startsWith( rootDirectory))
			return false;

		removeAllItems();

		if ( directory.equals( rootDirectory)) {
			addItem( directory);
			setSelectedIndex( 0);
			return true;
		}

		String partial_directory = rootDirectory.equals( "/") ? directory : directory.substring( rootDirectory.length());
		String[] names = partial_directory.split( "/");
		String name = rootDirectory;
		for ( int i = 0; i < names.length; ++i) {
			if ( 0 == i)
				addItem( name);
			else {
				name += ( ( name.endsWith( "/") ? "" : "/") + names[ i]);
				addItem( name);
			}
		}

		setSelectedIndex( getItemCount() - 1);

		return true;
	}

	/**
	 * 
	 */
	public void up_one_level() {
		int index = getSelectedIndex();
		if ( 0 >= index || getItemCount() <= index)
			return;

		setSelectedIndex( index - 1);
	}
}
