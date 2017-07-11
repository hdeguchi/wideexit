/*
 * 2004/12/15
 */
package soars.plugin.modelbuilder.chart.chart.main;

import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;

import org.xml.sax.SAXException;

import ptolemy.plot.Plot;
import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 */
public class LogViewer extends Plot implements IMessageCallback {

	/**
	 * 
	 */
	public LogViewer() {
		super();
	}

	/**
	 * @return
	 */
	public boolean setup() {
		removeMouseListener();
		removeMouseMotionListener();
		removeMouseWheelListener();

		return true;
	}

	/**
	 * 
	 */
	private void removeMouseListener() {
		MouseListener[] mouseListeners = getMouseListeners();
		for ( int i = 0; i < mouseListeners.length; ++i)
			removeMouseListener( mouseListeners[ i]);
	}

	/**
	 * 
	 */
	private void removeMouseMotionListener() {
		MouseMotionListener[] mouseMotionListeners = getMouseMotionListeners();
		for ( int i = 0; i < mouseMotionListeners.length; ++i)
			removeMouseMotionListener( mouseMotionListeners[ i]);
	}

	/**
	 * 
	 */
	private void removeMouseWheelListener() {
		MouseWheelListener[] mouseWheelListeners = getMouseWheelListeners();
		for ( int i = 0; i < mouseWheelListeners.length; ++i)
			removeMouseWheelListener( mouseWheelListeners[ i]);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IMessageCallback#message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.MessageDlg)
	 */
	public boolean message_callback(String id, Object[] objects, MessageDlg messageDlg) {
		if ( id.equals( "on_file_open"))
			return Loader.load( ( File)objects[ 0], ( LogViewer)objects[ 1]);

		return true;
	}

	/**
	 * @param filename
	 * @return
	 */
	public boolean write_data(String filename) throws IOException {
		LogWriter logWriter = new LogWriter();
		return logWriter.write_data( this, true, true, _points, filename);
	}

	/**
	 * @param writer
	 * @param name
	 * @param rectangle
	 * @return
	 * @throws SAXException
	 */
	public boolean write(Writer writer, String name, Rectangle rectangle) throws SAXException {
		LogWriter logWriter = new LogWriter();
		return logWriter.write( writer, this, true, true, name, _points, rectangle);
	}

	/**
	 * @param dataset
	 * @param file
	 * @param connect
	 * @param writer
	 * @param name
	 * @param rectangle
	 * @return
	 * @throws SAXException
	 */
	public boolean write(int dataset, File file, boolean connect, Writer writer, String name, Rectangle rectangle) throws SAXException {
		LogWriter logWriter = new LogWriter();
		return logWriter.write( dataset, file, connect, writer, this, true, true, name, _points, rectangle);
	}

	/**
	 * @param dataset
	 * @param file
	 * @return
	 */
	public boolean write(int dataset, File file) {
		LogWriter logWriter = new LogWriter();
		return logWriter.write_data( this, _points, dataset, file);
	}
}
