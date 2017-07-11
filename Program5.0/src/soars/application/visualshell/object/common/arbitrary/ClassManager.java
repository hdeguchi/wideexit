/**
 * 
 */
package soars.application.visualshell.object.common.arbitrary;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.w3c.dom.Node;

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.object.entity.base.object.class_variable.ClassVariableObject;
import soars.common.soars.module.Module;

/**
 * @author kurata
 *
 */
public class ClassManager extends JSplitPane implements IClassTreeCallback {

	/**
	 * 
	 */
	private ClassTree _classTree = null;

	/**
	 * 
	 */
	private JEditorPane _propertyEditorPane = null;

	/**
	 * 
	 */
	private String _previousText = "";

	/**
	 * 
	 */
	protected Frame _owner = null;

	/**
	 * 
	 */
	protected Component _parent = null;

	/**
	 * @param owner
	 * @param parent
	 */
	public ClassManager(Frame owner, Component parent) {
		super(JSplitPane.HORIZONTAL_SPLIT);
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @return
	 */
	public boolean setup() {
		_propertyEditorPane = new JEditorPane();
		_propertyEditorPane.setContentType( "text/html");
		_propertyEditorPane.setEditable( false);

		if ( !setup_classTree())
			return false;

		setup_property_editorPane();

		setDividerLocation( Integer.parseInt( Environment.get_instance().get( Environment._editObjectDialogClassManagerDividerLocationKey, "100")));

		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_classTree() {
		_classTree = new ClassTree( _owner, _parent, this);
		if ( !_classTree.setup( true))
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setView( _classTree);

		setLeftComponent( scrollPane);

		return true;
	}

	/**
	 * 
	 */
	private void setup_property_editorPane() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setView( _propertyEditorPane);

		setRightComponent( scrollPane);
	}

	/**
	 * @param classVariableObject
	 * @param update_all
	 */
	public void select(ClassVariableObject classVariableObject, boolean update_all) {
		_classTree.select( new String[][] { { "jarfile", classVariableObject._jarFilename}, { "class", classVariableObject._classname}});
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.common.arbitrary.IClassTreeCallback#selected(org.w3c.dom.Node)
	 */
	public void selected(Node node) {
		if ( null == node)
			return;

		update_property_editorPane( node);
	}

	/**
	 * @param node
	 */
	private void update_property_editorPane(Node node) {
		Module module = _classTree.get_module( node);
		if ( null == module) {
			_propertyEditorPane.setText( "");
			_previousText = "";
		} else {
			String newText = module.get_html();
			if ( newText.equals( _previousText))
				return;

			_propertyEditorPane.setText( newText);
			_propertyEditorPane.setCaretPosition( 0);
			_previousText = newText;
		}
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		_classTree.on_setup_completed();
	}
}
