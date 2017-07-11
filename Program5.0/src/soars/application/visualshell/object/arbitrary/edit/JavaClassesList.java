/**
 * 
 */
package soars.application.visualshell.object.arbitrary.edit;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import javax.swing.event.MouseInputAdapter;

import soars.application.visualshell.object.arbitrary.JavaClasses;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.tool.common.Tool;

/**
 * The list component for java class list.
 * @author kurata / SOARS project
 */
public class JavaClassesList extends JList {

	/**
	 * 
	 */
	private DefaultComboBoxModel _defaultComboBoxModel = null;

	/**
	 * 
	 */
	private SelectJavaClassDlg _parent = null;

	/**
	 * Creates this object.
	 * @param parent the parent component of this object
	 */
	public JavaClassesList(SelectJavaClassDlg parent) {
		super();
		_parent = parent;
	}

	/**
	 * @return
	 */
	protected boolean setup() {
		_defaultComboBoxModel = new DefaultComboBoxModel();
		setModel( _defaultComboBoxModel);

		addMouseListener( new MouseInputAdapter() {
			public void mouseClicked( MouseEvent arg0) {
				if ( !SwingTool.is_mouse_left_button_double_click( arg0))
					return;

				on_mouse_left_double_click( arg0);
			}
			public void mouseReleased(MouseEvent arg0) {
				if ( !SwingTool.is_mouse_right_button( arg0))
					return;

				on_mouse_right_up( arg0);
			}
		});

		return true;
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_left_double_click(MouseEvent mouseEvent) {
		int index = getSelectedIndex();
		if ( 0 > index)
			return;

		_parent.on_ok( null);
	}

	/**
	 * @param mouseEvent
	 */
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
	}

	/**
	 * @param word
	 * @param excepted_java_classes
	 */
	protected void update(String word, String[] excepted_java_classes) {
		_defaultComboBoxModel.removeAllElements();

		String[] javaClasses = JavaClasses.get_java_classes( word);
		if ( null == javaClasses)
			return;

		for ( int i = 0; i < javaClasses.length; ++i) {
			if ( 0 > Arrays.binarySearch( excepted_java_classes, javaClasses[ i]))
				_defaultComboBoxModel.addElement( javaClasses[ i]);
		}

		if ( 0 < _defaultComboBoxModel.getSize())
			setSelectedIndex( 0);
	}

	/**
	 * @return
	 */
	protected List<String> on_ok() {
		int[] indices = getSelectedIndices();
		if ( null == indices || 0 == indices.length)
			return null;

		indices = Tool.quick_sort_int( indices, true);

		List<String> list = new ArrayList<String>();
		for ( int i = 0; i < indices.length; ++i)
			list.add( ( String)_defaultComboBoxModel.getElementAt( indices[ i]));

		return list;
	}
}
