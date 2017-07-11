/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.variable.panel.base.panel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextUndoRedoManager;

/**
 * @author kurata
 *
 */
public class GisInitialValueTextPanelBase extends JPanel implements ITextUndoRedoManagerCallBack {

	/**
	 * 
	 */
	public TextField _textField = null;

	/**
	 * 
	 */
	protected List<TextUndoRedoManager> _textUndoRedoManagers = new ArrayList<TextUndoRedoManager>();

	/**
	 * 
	 */
	public GisInitialValueTextPanelBase() {
		super();
	}

	/**
	 * @param value
	 */
	public void set(String value) {
		_textField.setText( value);
	}

	/**
	 * @return
	 */
	public String get() {
		return _textField.getText();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.undo.UndoManager)
	 */
	@Override
	public void on_changed(UndoManager undoManager) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.text.AbstractDocument.DefaultDocumentEvent, javax.swing.undo.UndoManager)
	 */
	@Override
	public void on_changed(DefaultDocumentEvent defaultDocumentEvent,UndoManager undoManager) {
	}

	/**
	 * 
	 */
	public void clear_textUndoRedoManagers() {
		_textUndoRedoManagers.clear();
	}

	/**
	 * 
	 */
	public void setup_textUndoRedoManagers() {
		if ( !_textUndoRedoManagers.isEmpty())
			return;

		_textUndoRedoManagers.add( new TextUndoRedoManager( _textField, this));
	}
}
