/**
 * 
 */
package soars.application.manager.library.main.tab.tab;

import java.awt.Component;
import java.awt.Frame;
import java.io.File;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import soars.application.manager.library.main.ResourceManager;
import soars.application.manager.library.main.tab.common.table.FileTable;
import soars.application.manager.library.main.tab.tab.annotaion.AnnotationPane;

/**
 * @author kurata
 *
 */
public class InternalTabbedPane extends JTabbedPane {

	/**
	 * 
	 */
	private FileTable _fileTable = null;

	/**
	 * 
	 */
	public JEditorPane _propertyEditorPane = null;

	/**
	 * 
	 */
	private JScrollPane _scrollPane = null;

	/**
	 * 
	 */
	public AnnotationPane _annotationPane = null;

	/**
	 * 
	 */
	private Frame _owner = null;

	/**
	 * 
	 */
	private Component _parent = null;

	/**
	 * @param tabPlacement
	 * @param owner
	 * @param parent
	 */
	public InternalTabbedPane(int tabPlacement, Frame owner, Component parent) {
		super(tabPlacement);
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @param dividerLocationKey
	 * @param fileTable 
	 * @return
	 */
	public boolean setup(String dividerLocationKey, FileTable fileTable) {
		_fileTable = fileTable;

		if ( !setup_propertyEditorPane())
			return false;

		if ( !setup_annotationPanel( dividerLocationKey))
			return false;

		addChangeListener( new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Component component = getSelectedComponent();
				if ( component instanceof JScrollPane)
					_annotationPane.store();
//				else if ( component instanceof AnnotationPanel)
//					_annotationPanel.refresh();
			}
		});

		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_propertyEditorPane() {
		_propertyEditorPane = new JEditorPane();
		_propertyEditorPane.setContentType( "text/html");
		_propertyEditorPane.setEditable( false);

		_scrollPane = new JScrollPane();
		_scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		_scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		_scrollPane.getViewport().setView( _propertyEditorPane);

		add( _scrollPane, ResourceManager.get_instance().get( "internal.tab.property"));

		return true;
	}

	/**
	 * 
	 */
	public void optimize_divider_location() {
		_annotationPane.optimize_divider_location();
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		_annotationPane.on_setup_completed();
	}

	/**
	 * @param dividerLocationKey
	 * @return
	 */
	private boolean setup_annotationPanel(String dividerLocationKey) {
		_annotationPane = new AnnotationPane( dividerLocationKey, _owner, _parent);
		if ( !_annotationPane.setup())
			return false;

		add( _annotationPane, ResourceManager.get_instance().get( "internal.tab.annotation"));

		return true;
	}

	/**
	 * 
	 */
	public void set_property_to_environment_file() {
		_annotationPane.set_property_to_environment_file();
	}

	/**
	 * @param save
	 */
	public void refresh(boolean save) {
		int[] rows = _fileTable.getSelectedRows();
		if ( null == rows)
			return;

//		int[] columns = getSelectedColumns();
//		if ( null == columns || 1 != columns.length || 0 != columns[ 0])
//			return;

		if ( 1 != rows.length)
			update( ( File)null, save);
		else {
			try {
				File file = ( File)_fileTable.getValueAt( rows[ 0], 0);
				if ( null == file)
					return;

				update( file, save);

			} catch (ArrayIndexOutOfBoundsException e) {
				return;
			}
		}
	}

	/**
	 * 
	 */
	public void store() {
		_annotationPane.store();
	}

	/**
	 * @param file
	 * @param save
	 * @return
	 */
	public boolean update(File file, boolean save) {
		return _annotationPane.update( file, save);
	}

	/**
	 * @param files
	 * @return
	 */
	public boolean update(File[] files) {
		return _annotationPane.update( files);
	}
}
