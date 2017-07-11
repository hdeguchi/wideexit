/**
 * 
 */
package soars.application.manager.model.main.panel.tab.contents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.SystemColor;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import soars.application.manager.model.main.Environment;
import soars.application.manager.model.main.MainFrame;
import soars.application.manager.model.main.ResourceManager;
import soars.application.manager.model.main.panel.tree.ModelTree;
import soars.application.manager.model.main.panel.tree.property.TableSelection;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class SoarsContentsPage extends JSplitPane {

	/**
	 * 
	 */
	private ModelTree _modelTree = null;

	/**
	 * 
	 */
	private SoarsContentsTable _soarsContentsTable = null;

	/**
	 * 
	 */
	private SoarsContentsRowHeaderTable _soarsContentsRowHeaderTable = null;

	/**
	 * 
	 */
	private JTextField _titleTextField = null;

	/**
	 * 
	 */
	private JTextArea _commentTextArea = null;

	/**
	 * @param modelTree 
	 */
	public SoarsContentsPage(ModelTree modelTree) {
		super(JSplitPane.VERTICAL_SPLIT);
		_modelTree = modelTree;
	}

	/**
	 * @return
	 */
	public boolean setup() {
		_titleTextField = new JTextField();
		_commentTextArea = new JTextArea();

		_soarsContentsTable = new SoarsContentsTable( _modelTree, _titleTextField, _commentTextArea, MainFrame.get_instance(), this);
		_soarsContentsRowHeaderTable = new SoarsContentsRowHeaderTable( MainFrame.get_instance(), this);

		if ( !setup_soarsContentsTablePane())
			return false;

		if ( !setup_propertyPane())
			return false;

		setDividerLocation( Integer.parseInt( Environment.get_instance().get( Environment._soarsContentsPageDividerLocationKey, "400")));

		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_soarsContentsTablePane() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		if ( !setup_soarsContentsTable( centerPanel))
			return false;

		panel.add( centerPanel);

		setTopComponent( panel);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_soarsContentsTable(JPanel parent) {
		if ( !_soarsContentsTable.setup( _soarsContentsRowHeaderTable))
			return false;

		if ( !_soarsContentsRowHeaderTable.setup( _soarsContentsTable, false, ( Graphics2D)getGraphics()))
			return false;

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _soarsContentsTable);
		scrollPane.getViewport().setOpaque( true);
		scrollPane.getViewport().setBackground( SystemColor.text);

		scrollPane.setRowHeaderView( _soarsContentsRowHeaderTable);
		scrollPane.getRowHeader().setOpaque( true);
		scrollPane.getRowHeader().setBackground( SystemColor.text);
		Dimension dimension = scrollPane.getRowHeader().getPreferredSize();
		scrollPane.getRowHeader().setPreferredSize( new Dimension( _soarsContentsRowHeaderTable.getColumnModel().getColumn( 0).getWidth(), dimension.height));
		SwingTool.set_table_left_top_corner_column( scrollPane);

		// スクロール時に２つのTableが同期するように以下のhandlerが必要
		scrollPane.getRowHeader().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JViewport viewport = ( JViewport)e.getSource();
				scrollPane.getVerticalScrollBar().setValue( viewport.getViewPosition().y);
			}
		});

		parent.add( scrollPane);

		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_propertyPane() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.X_AXIS));

		setup_titleTextField( northPanel);

		panel.add( northPanel, "North");


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		setup_commentTextArea( centerPanel);

		panel.add( centerPanel);


		setBottomComponent( panel);

		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_titleTextField(JPanel parent) {
		parent.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "soars.contents.property.pane.label.title"));
		parent.add( label);

		parent.add( Box.createHorizontalStrut( 5));

		parent.add( _titleTextField);
	}

	/**
	 * @param parent
	 */
	private void setup_commentTextArea(JPanel parent) {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBackground( new Color( 255, 255, 255));
		scrollPane.getViewport().setView( _commentTextArea);
		parent.add( scrollPane);
	}

	/**
	 * 
	 */
	public void set_property_to_environment_file() {
		Environment.get_instance().set( Environment._soarsContentsPageDividerLocationKey, String.valueOf( getDividerLocation()));
	}

	/**
	 * @param tableSelection
	 */
	public void get(TableSelection tableSelection) {
		_soarsContentsTable.get( tableSelection);
	}

	/**
	 * @param file
	 * @param tableSelection
	 */
	public void update(File file, TableSelection tableSelection) {
		_soarsContentsTable.update( file, tableSelection);
	}

	/**
	 * 
	 */
	public void refresh() {
		_soarsContentsTable.refresh();
		_soarsContentsRowHeaderTable.refresh();
		_commentTextArea.updateUI();
		_commentTextArea.repaint();
	}

	/**
	 * 
	 */
	public void on_exit() {
		_soarsContentsTable.update( null, null);
	}
}
