/**
 * 
 */
package soars.tool.image.checker.body.main;

import java.awt.Component;
import java.awt.Frame;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

import soars.common.utility.swing.list.StandardList;
import soars.common.utility.swing.list.StandardListCellRenderer;
import soars.common.utility.swing.message.IObjectsMessageCallback;
import soars.common.utility.swing.message.ObjectsMessageDlg;
import soars.common.utility.swing.progress.IObjectsProgressCallback;
import soars.common.utility.swing.progress.ObjectsProgressDlg;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.resource.Resource;
import soars.tool.image.checker.body.plugin.Plugin;

/**
 * @author kurata
 *
 */
public class MainList extends StandardList implements DropTargetListener, IObjectsMessageCallback, IObjectsProgressCallback {

	/**
	 * 
	 */
	private static MainList _mainList = null;

	/**
	 * 
	 */
	private static Plugin _plugin = null;

	/**
	 * 
	 */
	private boolean _canceled = false;

	/**
	 * Returns the instance of the MainList class.
	 * @return the instance of the MainList class
	 */
	public static MainList get_instance() {
		return _mainList;
	}

	/**
	 * @param owner
	 * @param parent
	 */
	public MainList(Frame owner, Component parent) {
		super(owner, parent);
		_mainList = this;
	}

	/**
	 * @return
	 */
	public boolean setup() {
		if ( !super.setup(false))
			return false;

		setSelectionMode( ListSelectionModel.SINGLE_SELECTION);

		setCellRenderer( new StandardListCellRenderer());

		new DropTarget( this, this);

		return true;
	}

	/**
	 * 
	 */
	public void initialize() {
		_defaultComboBoxModel.removeAllElements();
		MainFrame.get_instance().initialize();
	}

	/**
	 * 
	 */
	private void reset() {
		MainFrame.get_instance().reset();
	}

	/**
	 * 
	 */
	public void copy_to_clipboard() {
		if ( 0 == _defaultComboBoxModel.getSize())
			return;

		String text = "";
		for ( int i = 0; i < _defaultComboBoxModel.getSize(); ++i)
			text += ( ( String)_defaultComboBoxModel.getElementAt( i) + Constant._lineSeparator);

		Clipboard.set( text);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.list.StandardList#on_mouse_left_double_click(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_double_click(MouseEvent mouseEvent) {
		String filename = ( String)_defaultComboBoxModel.getSelectedItem();
		if ( null == filename)
			return;

//		try {
//			Desktop.getDesktop().open( new File( filename));
//			//Runtime.getRuntime().exec( filename);
//			//Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + filename);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * @param files
	 * @return
	 */
	private boolean execute(File[] files) {
		initialize();

		_canceled = false;

		File[] image_files = ( File[])ObjectsMessageDlg.execute(
			MainFrame.get_instance(),
			ResourceManager.get_instance().get( "application.title"),
			true,
			"image_files",
			ResourceManager.get_instance().get( "dialog.cancel"),
			String.format( ResourceManager.get_instance().get( "image.files.message"), 0),
			files,
			this,
			MainFrame.get_instance());

		if ( _canceled)
			return true;

		if ( null == image_files || 0 == image_files.length) {
			JOptionPane.showMessageDialog(
				MainFrame.get_instance(),
				ResourceManager.get_instance().get( "no.image.files.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.INFORMATION_MESSAGE);
			return true;
		}

		File[] illegal_image_files = ( File[])ObjectsProgressDlg.execute(
			MainFrame.get_instance(),
			ResourceManager.get_instance().get( "application.title"),
			true,
			"illegal_image_files",
			ResourceManager.get_instance().get( "dialog.cancel"),
			image_files,
			this,
			MainFrame.get_instance());

		if ( _canceled)
			return true;

		if ( null == illegal_image_files || 0 == illegal_image_files.length) {
			JOptionPane.showMessageDialog(
				MainFrame.get_instance(),
				ResourceManager.get_instance().get( "no.illegal.image.files.message"),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.INFORMATION_MESSAGE);
			return true;
		}

		Arrays.sort( illegal_image_files);

		for ( int i = 0; i < illegal_image_files.length; ++i)
			_defaultComboBoxModel.addElement( illegal_image_files[ i].getAbsolutePath());

		JOptionPane.showMessageDialog(
			MainFrame.get_instance(),
			String.format( ResourceManager.get_instance().get( "illegal.image.files.message"), illegal_image_files.length),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.ERROR_MESSAGE);

		reset();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IObjectsMessageCallback#objects_message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.ObjectsMessageDlg)
	 */
	public Object[] objects_message_callback(String id, Object[] objects, ObjectsMessageDlg objectsMessageDlg) {
		List list = new ArrayList();
		File[] files = ( File[])objects;
		for ( int i = 0; i < files.length; ++i) {
			if ( !get_image_files( files[ i], list, objectsMessageDlg))
				return null;
		}
		return ( File[])list.toArray( new File[ 0]);
	}

	/**
	 * @param file
	 * @param list
	 * @param objectsMessageDlg
	 * @return
	 */
	private boolean get_image_files(File file, List list, ObjectsMessageDlg objectsMessageDlg) {
		if ( objectsMessageDlg._canceled) {
			_canceled = true;
			return false;
		}

		if ( file.isDirectory()) {
			File[] files = file.listFiles();
			if ( null == files)
				return true;

			for ( int i = 0; i < files.length; ++i) {
				if ( !get_image_files( files[ i], list, objectsMessageDlg))
					return false;
			}
		} else if ( file.isFile()) {
			if ( !file.canRead())
				return true;

			if ( !Plugin.get_instance().contains( file))
				return true;

			list.add( file);

			objectsMessageDlg.update_message( String.format( ResourceManager.get_instance().get( "image.files.message"), list.size()));
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.progress.IObjectsProgressCallback#objects_message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.progress.ObjectsProgressDlg)
	 */
	public Object[] objects_message_callback(String id, Object[] objects, ObjectsProgressDlg objectsProgressDlg) {
		File[] files = ( File[])objects;
		List illegal_image_files = new ArrayList();
		for ( int i = 0; i < files.length; ++i) {
			if ( objectsProgressDlg._canceled) {
				_canceled = true;
				return null;
			}

			if ( null == Resource.load_image( files[ i]))
				illegal_image_files.add( files[ i]);

			objectsProgressDlg.set( 100 * ( i + 1) / files.length);
		}

		return ( File[])illegal_image_files.toArray( new File[ 0]);
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent arg0) {
		arg0.acceptDrag( DnDConstants.ACTION_COPY);
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent arg0) {
		arg0.acceptDrag( DnDConstants.ACTION_COPY_OR_MOVE);
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent arg0) {
		arg0.acceptDrag( DnDConstants.ACTION_COPY);
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent arg0) {
		try {
			Transferable transferable = arg0.getTransferable();
			if ( transferable.isDataFlavorSupported( DataFlavor.javaFileListFlavor)) {
				arg0.acceptDrop( DnDConstants.ACTION_COPY_OR_MOVE);
				List list = ( List)transferable.getTransferData( DataFlavor.javaFileListFlavor);
				if ( list.isEmpty()) {
					arg0.getDropTargetContext().dropComplete( true);
					return;
				}

				execute( ( File[])list.toArray( new File[ 0]));
				arg0.getDropTargetContext().dropComplete( true);
			} else if ( transferable.isDataFlavorSupported( DataFlavor.stringFlavor)) {
				arg0.acceptDrop( DnDConstants.ACTION_COPY_OR_MOVE);
				String string = ( String)transferable.getTransferData( DataFlavor.stringFlavor);
				arg0.getDropTargetContext().dropComplete( true);
				String[] files = string.split( System.getProperty( "line.separator"));
				if ( files.length <= 0)
					arg0.rejectDrop();
				else {
					List list = new ArrayList();
					for ( int i = 0; i < files.length; ++i)
						list.add( new File( new URI( files[ i].replaceAll( "[\r\n]", ""))));

					execute( ( File[])list.toArray( new File[ 0]));
				}
			} else {
				arg0.rejectDrop();
			}
		} catch (IOException ioe) {
			arg0.rejectDrop();
		} catch (UnsupportedFlavorException ufe) {
			arg0.rejectDrop();
		} catch (URISyntaxException e) {
			arg0.rejectDrop();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent arg0) {
	}
}
