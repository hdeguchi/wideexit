/**
 * 
 */
package soars.common.utility.swing.file.manager.edit;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

import soars.common.utility.swing.file.manager.ResourceManager;
import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 *
 */
public class EditNameDlg extends Dialog implements ITextUndoRedoManagerCallBack {

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
	private String _type = "";

	/**
	 * 
	 */
	public File _file = null;

	/**
	 * 
	 */
	private File _parent = null;

	/**
	 * 
	 */
	private String _extension = "";

	/**
	 * 
	 */
	private JTextField _nameTextField = null;

	/**
	 * 
	 */
	private TextUndoRedoManager _textUndoRedoManager = null;

	/**
	 * 
	 */
	private String _prohibitedCharacters = "$\t=<>&|:;,?\"*\\/";

	/**
	 * 
	 */
	public boolean _modified = false;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param type
	 * @param parent
	 */
	public EditNameDlg(Frame arg0, String arg1, boolean arg2, String type, File parent) {
		super(arg0, arg1, arg2);
		_type = type;
		_parent = parent;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param type
	 * @param file
	 * @param parent
	 */
	public EditNameDlg(Frame arg0, String arg1, boolean arg2, String type, File file, File parent) {
		super(arg0, arg1, arg2);
		_type = type;
		_file = file;
		_parent = parent;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param type
	 * @param file
	 * @param extension
	 * @param 
	 */
	public EditNameDlg(Frame arg0, String arg1, boolean arg2, String type, File file, File parent, String extension) {
		super(arg0, arg1, arg2);
		_type = type;
		_file = file;
		_parent = parent;
		_extension = extension;
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

		setup_name_text_field();

		insert_horizontal_glue();

		setup_ok_and_cancel_button(
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			true, true);

		insert_horizontal_glue();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);


		return true;
	}

	/**
	 * 
	 */
	private void setup_name_text_field() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "name.dialog.name.label"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_nameTextField = new JTextField( new TextExcluder( _prohibitedCharacters), ( null == _file) ? "" : _file.getName(), 0);
		_nameTextField.setPreferredSize( new Dimension( 200, _nameTextField.getPreferredSize().height));

		link_to_cancel( _nameTextField);

		_textUndoRedoManager = new TextUndoRedoManager( _nameTextField, this);

		panel.add( _nameTextField);
		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		_nameTextField.requestFocusInWindow();

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
		String name = _nameTextField.getText();
		if ( name.equals( ""))
			return;

		if ( _type.equals( "file") && ( null != _extension) && !_extension.equals( "") && !name.endsWith( _extension))
			name += _extension;

		File file = new File( _parent, name);
		if ( null == _file) {
			if ( file.exists())
				return;

			if ( _type.equals( "file")) {
				try {
					if ( !file.createNewFile())
						return;
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			} else if ( _type.equals( "directory")) {
				if ( !file.mkdirs())
					return;
			}

			_file = file;
			_modified = true;
		} else {
			if ( !_file.getName().equals( name)) {
				if ( file.exists())
					return;

				if ( !_file.renameTo( file))
					return;

				_file = file;
				_modified = true;
			}
		}
		
		super.on_ok(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.undo.UndoManager)
	 */
	public void on_changed(UndoManager undoManager) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.text.AbstractDocument.DefaultDocumentEvent, javax.swing.undo.UndoManager)
	 */
	public void on_changed(DefaultDocumentEvent defaultDocumentEvent,UndoManager undoManager) {
	}
}
