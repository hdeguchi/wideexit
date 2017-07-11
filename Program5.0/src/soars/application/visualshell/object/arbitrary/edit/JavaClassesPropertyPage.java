/**
 * 
 */
package soars.application.visualshell.object.arbitrary.edit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.MouseInputAdapter;

import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.swing.panel.StandardPanel;
import soars.common.utility.swing.tool.SwingTool;

/**
 * The tab component for java class list.
 * @author kurata / SOARS project
 */
public class JavaClassesPropertyPage extends StandardPanel {

	/**
	 * Title for the tab.
	 */
	public String _title = ResourceManager.get_instance().get( "edit.java.classes.dialog.title");

	/**
	 * 
	 */
	private JavaClassesTable _javaClassesTable = null;

	/**
	 * 
	 */
	private JScrollPane _javaClassesTableScrollPane = null;

	/**
	 * 
	 */
	private boolean _atFirst = true;

	/**
	 * 
	 */
	protected Frame _owner = null;

	/**
	 * 
	 */
	protected Component _parent = null;

	/**
	 * Creates this object.
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public JavaClassesPropertyPage(Frame owner, Component parent) {
		super();
		_owner = owner;
		_parent = parent;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.panel.StandardPanel#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;



		setLayout( new BorderLayout());



		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		if ( !setup_javaClassesTable( centerPanel))
			return false;

		add( centerPanel);



		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		setup_append_java_class_button( southPanel);

		insert_horizontal_glue( southPanel);

		add( southPanel, "South");



		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				if ( _atFirst) {
					_javaClassesTable.setup_column_width( _javaClassesTableScrollPane.getWidth());
					_atFirst = false;
				} else {
					_javaClassesTable.adjust_column_width( _javaClassesTableScrollPane.getWidth());
				}
			}
		});



		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_javaClassesTable(JPanel parent) {
		_javaClassesTable = new JavaClassesTable( _owner, _parent);
		if ( !_javaClassesTable.setup())
			return false;

		//link_to_cancel( _javaClassesTable);

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel tablePanel = new JPanel();
		tablePanel.setLayout( new GridLayout( 1, 1));

		_javaClassesTableScrollPane = new JScrollPane();
		_javaClassesTableScrollPane.getViewport().setView( _javaClassesTable);
		_javaClassesTableScrollPane.addMouseListener( new MouseInputAdapter() {
			public void mouseReleased(MouseEvent arg0) {
				if ( !SwingTool.is_mouse_right_button( arg0))
					return;

				_javaClassesTable.on_mouse_right_up( new Point( arg0.getX(),
					arg0.getY() - _javaClassesTable.getTableHeader().getHeight()));
			}
		});

		//link_to_cancel( _javaClassesTableScrollPane);

		tablePanel.add( _javaClassesTableScrollPane);
		panel.add( tablePanel);
		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_append_java_class_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridLayout( 1, 1));

		JButton button = new JButton(
			ResourceManager.get_instance().get( "edit.java.classes.dialog.append.new.class.button.name"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_javaClassesTable.on_append( arg0);
			}
		});

		//link_to_cancel( button);

		buttonPanel.add( button);
		panel.add( buttonPanel);
		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * Invoked when this objet has been initialized.
	 */
	public void on_setup_completed() {
	}

	/**
	 * Returns true for updating successfully.
	 * @return true for updating successfully
	 */
	public boolean on_ok() {
		_javaClassesTable.on_ok();
		return true;
	}
}
