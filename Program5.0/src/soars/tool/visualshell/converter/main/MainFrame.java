/**
 * 
 */
package soars.tool.visualshell.converter.main;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import soars.common.soars.tool.Converter;
import soars.common.utility.swing.mac.IMacScreenMenuHandler;
import soars.common.utility.swing.mac.MacUtility;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.swing.window.Frame;

/**
 * @author kurata
 *
 */
public class MainFrame extends Frame implements DropTargetListener, IMacScreenMenuHandler {

	/**
	 * 
	 */
	static public int _minimumWidth = 0;

	/**
	 * 
	 */
	static public int _minimumHeight = 0;

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private MainFrame _mainFrame = null;

	/**
	 * @return
	 */
	public static MainFrame get_instance() {
		synchronized( _lock) {
			if ( null == _mainFrame) {
				_mainFrame = new MainFrame( "SOARS VidualShell converter");
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

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		if ( !MacUtility.setup_screen_menu_handler( this, this, "SOARS VidualShell converter"))
			return false;

		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		insert_horizontal_glue();

		setup();

		insert_horizontal_glue();


		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);

		pack();

		Toolkit.getDefaultToolkit().setDynamicLayout( true);

		_minimumWidth = getSize().width;
		_minimumHeight = getSize().height;

		Rectangle rectangle = new Rectangle();
		rectangle.setBounds(
			SwingTool.get_default_window_position( _minimumWidth, _minimumHeight).x,
			SwingTool.get_default_window_position( _minimumWidth, _minimumHeight).y,
			_minimumWidth, _minimumHeight);
		setLocation( rectangle.x, rectangle.y);
		setSize( rectangle.width, rectangle.height);

		setVisible( true);

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				int width = getSize().width;
				int height = getSize().height;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width,
					( _minimumHeight > height) ? _minimumHeight : height);
			}
		});

		new DropTarget( this, this);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.mac.IMacScreenMenuHandler#on_mac_about()
	 */
	public void on_mac_about() {
		JOptionPane.showMessageDialog( this,
			"SOARS VidualShell converter",
			"SOARS",
			JOptionPane.INFORMATION_MESSAGE);

		requestFocus();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.mac.IMacScreenMenuHandler#on_mac_quit()
	 */
	public void on_mac_quit() {
		on_window_closing( null);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Frame#on_window_closing(java.awt.event.WindowEvent)
	 */
	protected void on_window_closing(WindowEvent windowEvent) {
		System.exit( 0);
	}

	/**
	 * 
	 */
	private void setup() {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		String text = "Drag & Drop here!";
		JLabel label = new JLabel( text);
		Font font = label.getFont();
		label.setFont( new Font( font.getFamily(), font.getStyle(), 32));
		label.setPreferredSize( new Dimension( ( font.getSize() + 3) * text.length(), label.getPreferredSize().height));
		panel.add( label);

		getContentPane().add( panel);
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
		arg0.acceptDrag( DnDConstants.ACTION_COPY);
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
				arg0.acceptDrop( DnDConstants.ACTION_COPY);
				List list = ( List)transferable.getTransferData( DataFlavor.javaFileListFlavor);
				if ( list.isEmpty()) {
					arg0.getDropTargetContext().dropComplete( true);
					return;
				}

				List<File> files = new ArrayList<File>();
				for ( Object object:list) {
					if ( !( object instanceof File))
						continue;

					File file = ( File)object;
					if ( !file.isDirectory() && ( !file.isFile() || !file.canRead()))
						continue;

					files.add( file);
				}

				arg0.getDropTargetContext().dropComplete( true);

				on_file_open_by_drag_and_drop( files.toArray( new File[ 0]));
			} else {
				arg0.rejectDrop();
			}
		} catch (IOException ioe) {
			arg0.rejectDrop();
		} catch (UnsupportedFlavorException ufe) {
			arg0.rejectDrop();
		}
	}

	/**
	 * @param files
	 */
	private void on_file_open_by_drag_and_drop(File[] files) {
		for ( File file:files) {
			if ( file.isFile())
				on_file_open_by_drag_and_drop( file);
			else if ( file.isDirectory()) {
				File[] list = file.listFiles();
				if ( null == list)
					continue;

				on_file_open_by_drag_and_drop( list);
			} 
		}
	}

	/**
	 * @param file
	 */
	private void on_file_open_by_drag_and_drop(File file) {
		Converter.vsl2soars( file);
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent arg0) {
	}
}
