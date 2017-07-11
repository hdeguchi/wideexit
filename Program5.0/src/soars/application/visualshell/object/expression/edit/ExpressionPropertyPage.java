/**
 * 
 */
package soars.application.visualshell.object.expression.edit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.observer.Observer;
import soars.common.utility.swing.panel.StandardPanel;

/**
 * @author kurata
 *
 */
public class ExpressionPropertyPage extends StandardPanel {

	/**
	 * 
	 */
	public String _title = ResourceManager.get_instance().get( "edit.expressions.dialog.title");

	/**
	 * 
	 */
	private ExpressionTable _expressionTable = null;

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
	public ExpressionPropertyPage(Frame owner, Component parent) {
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

		create_expressionTable();

		if ( !setup_expressionTable( centerPanel))
			return false;

		add( centerPanel);



		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		setup_append_expression_button( southPanel);

		insert_horizontal_glue( southPanel);

		add( southPanel, "South");



		return true;
	}

	/**
	 * 
	 */
	private void create_expressionTable() {
		_expressionTable = new ExpressionTable( null, _owner, this);
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_expressionTable(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel tablePanel = new JPanel();
		tablePanel.setLayout( new GridLayout( 1, 1));

		if ( !_expressionTable.setup())
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _expressionTable);
//		scrollPane.addMouseListener( new MouseInputAdapter() {
//			public void mouseReleased(MouseEvent arg0) {
//				if ( !SwingTool.is_mouse_right_button( arg0))
//					return;
//
//				_expressionTable.on_mouse_right_up( new Point( arg0.getX(),
//					arg0.getY() - _expressionTable.getTableHeader().getHeight()));
//			}
//		});

		tablePanel.add( scrollPane);
		panel.add( tablePanel);
		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_append_expression_button(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridLayout( 1, 1));

		JButton button = new JButton(
			ResourceManager.get_instance().get( "edit.expressions.dialog.append.expression.button.name"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_expressionTable.on_append( arg0);
			}
		});

		buttonPanel.add( button);
		panel.add( buttonPanel);
		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
	}

	/**
	 * @return
	 */
	public boolean on_ok() {
		_expressionTable.on_ok();

		Observer.get_instance().on_update_expression();

		Observer.get_instance().modified();

		return true;
	}
}
