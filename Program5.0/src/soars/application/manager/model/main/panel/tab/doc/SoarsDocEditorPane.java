/**
 * 
 */
package soars.application.manager.model.main.panel.tab.doc;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

import soars.application.manager.model.main.Constant;
import soars.application.manager.model.main.ResourceManager;
import soars.common.utility.tool.file.FileUtility;

/**
 * @author kurata
 *
 */
public class SoarsDocEditorPane extends JEditorPane {

	/**
	 * 
	 */
	private List<URL> _history = new ArrayList<URL>();

	/**
	 * 
	 */
	private int _index = 0;

	/**
	 * 
	 */
	private File _emptyDocumentFile = null;

	/**
	 * 
	 */
	private File _noDocumentFile = null;

	/**
	 * 
	 */
	public JButton _backButton = null;

	/**
	 * 
	 */
	public JButton _forwardButton = null;

	/**
	 * @param backButton
	 * @param forwardButton
	 */
	public SoarsDocEditorPane(JButton backButton, JButton forwardButton) {
		super();
		_backButton = backButton;
		_forwardButton = forwardButton;
	}

	/**
	 * @return
	 */
	private boolean create_empty_document_file() {
		try {
			_emptyDocumentFile = File.createTempFile( "soars", "html");
			_emptyDocumentFile.deleteOnExit();
			if ( !FileUtility.write_text_to_file( _emptyDocumentFile,
				"<html>\n<head><title></title>\n<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\"></head>\n<body></body>\n</html>",
				"UTF8"))
				return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	private boolean create_no_document_file() {
		try {
			_noDocumentFile = File.createTempFile( "soars", "html");
			_noDocumentFile.deleteOnExit();
			if ( !FileUtility.write_text_to_file( _noDocumentFile,
				"<html>\n<head><title></title>\n<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\"></head>\n<body><h1><b><font color=\"#ff0000\">"
					+ ResourceManager.get_instance().get( "soars.doc.page.editor.pane.no.soars.document")
					+ "</font></b></h1></body>\n</html>",
				"UTF8"))
				return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 
	 */
	private void clear_history() {
		_history.clear();
		_index = 0;
		_backButton.setEnabled( false);
		_forwardButton.setEnabled( false);
	}

	/**
	 * @return
	 */
	protected boolean can_back() {
		return ( 0 < _index);
	}

	/**
	 * @return
	 */
	protected boolean can_forward() {
		return ( _history.size() - 1 > _index);
	}

	/**
	 * @return
	 */
	public boolean setup() {
		if ( !create_empty_document_file())
			return false;

		if ( !create_no_document_file())
			return false;

		addHyperlinkListener( new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if ( HyperlinkEvent.EventType.ACTIVATED == e.getEventType()) {
					if ( e instanceof HTMLFrameHyperlinkEvent) {
						HTMLFrameHyperlinkEvent evt = ( HTMLFrameHyperlinkEvent)e;
						HTMLDocument doc = ( HTMLDocument)getDocument();
						doc.processHTMLFrameHyperlinkEvent( evt);
					} else {
						try {
							URL url = e.getURL();
							setPage( url);
							update_history();
							_history.add( url);
							_index = _history.size() - 1;
							_backButton.setEnabled( can_back());
							_forwardButton.setEnabled( can_forward());
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		return true;
	}

	/**
	 * 
	 */
	private void update_history() {
		while ( _index + 1 < _history.size())
			_history.remove( _index + 1);
	}

	/**
	 * @param parentDirectory
	 */
	public void update(File parentDirectory) {
		_history.clear();

		File file = new File( parentDirectory, "visualshell/" + Constant._documentHtmlFilename);
		if ( !file.exists() || !file.isFile() || !file.canRead()) {
			set_no_document_page();
			return;
		}

		try {
			URL url = file.toURI().toURL();
			setPage( url);
			_history.add( url);
			_index = 0;
			_backButton.setEnabled( false);
			_forwardButton.setEnabled( false);
		} catch (MalformedURLException e) {
			set_no_document_page();
			e.printStackTrace();
			return;
		} catch (IOException e) {
			set_no_document_page();
			e.printStackTrace();
			return;
		}

		refresh();
	}

	/**
	 * 
	 */
	public void back() {
		if ( 0 >= _index)
			return;

		try {
			setPage( _history.get( --_index));
		} catch (IOException e) {
			e.printStackTrace();
		}

		_backButton.setEnabled( can_back());
		_forwardButton.setEnabled( can_forward());
	}

	/**
	 * 
	 */
	public void forward() {
		if ( _history.size() - 1 <= _index)
			return;

		try {
			setPage( _history.get( ++_index));
		} catch (IOException e) {
			e.printStackTrace();
		}

		_backButton.setEnabled( can_back());
		_forwardButton.setEnabled( can_forward());
	}

	/**
	 * 
	 */
	public void set_empty_page() {
		try {
			setPage( _emptyDocumentFile.toURI().toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		clear_history();
	}

	/**
	 * 
	 */
	public void set_no_document_page() {
		try {
			setPage( _noDocumentFile.toURI().toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		clear_history();
	}

	/**
	 * 
	 */
	public void refresh() {
		//updateUI();
		repaint();
	}
}
