/**
 * 
 */
package soars.common.utility.swing.text;

import java.awt.Event;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

/**
 * @author kurata
 *
 */
public class TextUndoRedoManager {

	/**
	 * 
	 */
	public static final String _ACTION_KEY_UNDO = "undo";

	/**
	 * 
	 */
	public static final String _ACTION_KEY_REDO = "redo";

	/**
	 * 
	 */
	private UndoManager _undoManager = new UndoManager();

	/**
	 * 
	 */
	private ITextUndoRedoManagerCallBack _textUndoRedoManagerCallBack = null;

	/**
	 * @param textComponent
	 * @param textUndoRedoManagerCallBack
	 */
	public TextUndoRedoManager(JTextComponent textComponent, ITextUndoRedoManagerCallBack textUndoRedoManagerCallBack) {
		super();
		_textUndoRedoManagerCallBack = textUndoRedoManagerCallBack;

		Action undoAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_undo();
			}
		};

		textComponent.getInputMap().put( KeyStroke.getKeyStroke( 'Z', ( 0 <= System.getProperty( "os.name").indexOf( "Mac")) ? Event.META_MASK : Event.CTRL_MASK), _ACTION_KEY_UNDO);
		textComponent.getActionMap().put( _ACTION_KEY_UNDO, undoAction);


		Action redoAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				on_redo();
			}
		};

		textComponent.getInputMap().put( KeyStroke.getKeyStroke( 'Y', ( 0 <= System.getProperty( "os.name").indexOf( "Mac")) ? Event.META_MASK : Event.CTRL_MASK), _ACTION_KEY_REDO);
		textComponent.getActionMap().put( _ACTION_KEY_REDO, redoAction);


		textComponent.getDocument().addDocumentListener( new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
			}
			public void insertUpdate(DocumentEvent e) {
				if ( e instanceof DefaultDocumentEvent) {
					DefaultDocumentEvent defaultDocumentEvent = ( DefaultDocumentEvent)e;
					_undoManager.addEdit( defaultDocumentEvent);

					if ( null != _textUndoRedoManagerCallBack)
						_textUndoRedoManagerCallBack.on_changed( defaultDocumentEvent, _undoManager);
				}
			}
			public void removeUpdate(DocumentEvent e) {
				if ( e instanceof DefaultDocumentEvent) {
					DefaultDocumentEvent defaultDocumentEvent = ( DefaultDocumentEvent)e;
					_undoManager.addEdit( defaultDocumentEvent);

					if ( null != _textUndoRedoManagerCallBack)
						_textUndoRedoManagerCallBack.on_changed( defaultDocumentEvent, _undoManager);
				}
			}
		});
	}

	/**
	 * 
	 */
	public void on_undo() {
		if ( !_undoManager.canUndo())
			return;

		_undoManager.undo();

		if ( null != _textUndoRedoManagerCallBack)
			_textUndoRedoManagerCallBack.on_changed( _undoManager);
	}

	/**
	 * 
	 */
	public void on_redo() {
		if ( !_undoManager.canRedo())
			return;

		_undoManager.redo();

		if ( null != _textUndoRedoManagerCallBack)
			_textUndoRedoManagerCallBack.on_changed( _undoManager);
	}

	/**
	 * 
	 */
	public void clear() {
		_undoManager.discardAllEdits();
	}

	/**
	 * @return
	 */
	public boolean can_undo() {
		return _undoManager.canUndo();
	}
}
