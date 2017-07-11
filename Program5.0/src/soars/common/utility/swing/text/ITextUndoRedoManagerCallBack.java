/**
 * 
 */
package soars.common.utility.swing.text;

import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

/**
 * @author kurata
 *
 */
public interface ITextUndoRedoManagerCallBack {

	/**
	 * @param undoManager
	 */
	void on_changed(UndoManager undoManager);

	/**
	 * @param defaultDocumentEvent
	 * @param undoManager
	 */
	void on_changed(DefaultDocumentEvent defaultDocumentEvent, UndoManager undoManager);
}
