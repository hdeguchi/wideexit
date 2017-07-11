/*
 * 2005/05/01
 */
package soars.application.visualshell.object.stage.edit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.stage.StageManager;
import soars.application.visualshell.observer.Observer;
import soars.common.utility.swing.panel.StandardPanel;

/**
 * @author kurata
 */
public class StagePropertyPage extends StandardPanel {

	/**
	 * 
	 */
	public String _title = ResourceManager.get_instance().get( "edit.stage.dialog.title");

	/**
	 * 
	 */
	private StageManager _stageManager = null;

	/**
	 * 
	 */
	private StageList _mainStageList = null;

	/**
	 * 
	 */
	private StageList _initialStageList = null;

	/**
	 * 
	 */
	private StageList _terminalStageList = null;

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
	public StagePropertyPage(Frame owner, Component parent) {
		super();
		_stageManager = new StageManager( StageManager.get_instance());
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

		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BoxLayout( basePanel, BoxLayout.X_AXIS));

		basePanel.add( Box.createHorizontalStrut( 5));

		JPanel panel = setup_initial_stage_label();
		if ( null == panel)
			return false;

		setup_main_stage_label( panel);
		setup_terminal_stage_label( panel);

		basePanel.add( panel);
		northPanel.add( basePanel);
		add( northPanel, BorderLayout.NORTH);



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		basePanel = new JPanel();
		basePanel.setLayout( new BoxLayout( basePanel, BoxLayout.X_AXIS));

		basePanel.add( Box.createHorizontalStrut( 5));

		_initialStageList = new StageList(
			new String[] { ResourceManager.get_instance().get( "append.initial.stage.property.dialog.title"),
				ResourceManager.get_instance().get( "edit.initial.stage.property.dialog.title"),
				ResourceManager.get_instance().get( "edit.initial.stage.list.confirm.remove.message")},
			null, _owner, this);
		_mainStageList = new StageList(
			new String[] { ResourceManager.get_instance().get( "append.main.stage.property.dialog.title"),
				ResourceManager.get_instance().get( "edit.main.stage.property.dialog.title"),
				ResourceManager.get_instance().get( "edit.main.stage.list.confirm.remove.message")},
			null, _owner, this);
		_terminalStageList = new StageList(
			new String[] { ResourceManager.get_instance().get( "append.terminal.stage.property.dialog.title"),
				ResourceManager.get_instance().get( "edit.terminal.stage.property.dialog.title"),
				ResourceManager.get_instance().get( "edit.terminal.stage.list.confirm.remove.message")},
			null, _owner, this);
		StageListTransferHandler stageListTransferHandler = new StageListTransferHandler( _mainStageList, _initialStageList, _terminalStageList);

		panel = setup_initial_stage_list( stageListTransferHandler);
		if ( null == panel)
			return false;

		if ( !setup_main_stage_list( stageListTransferHandler, panel))
			return false;

		if ( !setup_terminal_stage_list( stageListTransferHandler, panel))
			return false;

		basePanel.add( panel);
		centerPanel.add( basePanel);
		add( centerPanel);



		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		basePanel = new JPanel();
		basePanel.setLayout( new BoxLayout( basePanel, BoxLayout.X_AXIS));

		basePanel.add( Box.createHorizontalStrut( 5));

		panel = setup_append_initial_stage_button();
		if ( null == panel)
			return false;

		setup_append_main_stage_button( panel);
		setup_append_terminal_stage_button( panel);

		basePanel.add( panel);
		southPanel.add( basePanel);


		insert_horizontal_glue( southPanel);

		add( southPanel, "South");



		return true;
	}

	/**
	 * @return
	 */
	private JPanel setup_initial_stage_label() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 1, 3));

		JPanel partialPanel = new JPanel();
		partialPanel.setLayout( new BoxLayout( partialPanel, BoxLayout.X_AXIS));

		JPanel labelPanel = new JPanel();
		labelPanel.setLayout( new GridLayout( 1, 1));

		JLabel label = new JLabel(
			ResourceManager.get_instance().get( "edit.stage.dialog.initial.stage"));

		labelPanel.add( label);
		partialPanel.add( labelPanel);

		partialPanel.add( Box.createHorizontalStrut( 5));

		panel.add( partialPanel);

		return panel;
	}

	/**
	 * @param parent
	 */
	private void setup_main_stage_label(JPanel parent) {
		JPanel partialPanel = new JPanel();
		partialPanel.setLayout( new BoxLayout( partialPanel, BoxLayout.X_AXIS));

		JPanel labelPanel = new JPanel();
		labelPanel.setLayout( new GridLayout( 1, 1));

		JLabel label = new JLabel(
			ResourceManager.get_instance().get( "edit.stage.dialog.main.stage"));

		labelPanel.add( label);
		partialPanel.add( labelPanel);

		partialPanel.add( Box.createHorizontalStrut( 5));

		parent.add( partialPanel);
	}

	/**
	 * @param parent
	 */
	private void setup_terminal_stage_label(JPanel parent) {
		JPanel partialPanel = new JPanel();
		partialPanel.setLayout( new BoxLayout( partialPanel, BoxLayout.X_AXIS));

		JPanel labelPanel = new JPanel();
		labelPanel.setLayout( new GridLayout( 1, 1));

		JLabel label = new JLabel(
			ResourceManager.get_instance().get( "edit.stage.dialog.terminal.stage"));

		labelPanel.add( label);
		partialPanel.add( labelPanel);

		partialPanel.add( Box.createHorizontalStrut( 5));

		parent.add( partialPanel);
	}

	/**
	 * @param stageListTransferHandler
	 * @return
	 */
	private JPanel setup_initial_stage_list(StageListTransferHandler stageListTransferHandler) {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 1, 3));

		JPanel partialPanel = new JPanel();
		partialPanel.setLayout( new BoxLayout( partialPanel, BoxLayout.X_AXIS));

		JPanel listPanel = new JPanel();
		listPanel.setLayout( new GridLayout( 1, 1));

		if ( !_initialStageList.setup( _stageManager._initialStages,
			new StageList[] { _mainStageList, _terminalStageList},
			stageListTransferHandler))
			return null;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _initialStageList);

		listPanel.add( scrollPane);
		partialPanel.add( listPanel);

		partialPanel.add( Box.createHorizontalStrut( 5));

		panel.add( partialPanel);

		return panel;
	}

	/**
	 * @param stageListTransferHandler
	 * @param parent
	 * @return
	 */
	private boolean setup_main_stage_list(StageListTransferHandler stageListTransferHandler, JPanel parent) {
		JPanel partialPanel = new JPanel();
		partialPanel.setLayout( new BoxLayout( partialPanel, BoxLayout.X_AXIS));

		JPanel listPanel = new JPanel();
		listPanel.setLayout( new GridLayout( 1, 1));

		if ( !_mainStageList.setup( _stageManager._mainStages,
			new StageList[] { _initialStageList, _terminalStageList},
			stageListTransferHandler))
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _mainStageList);

		listPanel.add( scrollPane);
		partialPanel.add( listPanel);

		partialPanel.add( Box.createHorizontalStrut( 5));

		parent.add( partialPanel);

		return true;
	}

	/**
	 * @param stageListTransferHandler
	 * @param parent
	 * @return
	 */
	private boolean setup_terminal_stage_list(StageListTransferHandler stageListTransferHandler, JPanel parent) {
		JPanel partialPanel = new JPanel();
		partialPanel.setLayout( new BoxLayout( partialPanel, BoxLayout.X_AXIS));

		JPanel listPanel = new JPanel();
		listPanel.setLayout( new GridLayout( 1, 1));

		if ( !_terminalStageList.setup( _stageManager._terminalStages,
			new StageList[] { _mainStageList, _initialStageList},
			stageListTransferHandler))
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _terminalStageList);

		listPanel.add( scrollPane);
		partialPanel.add( listPanel);

		partialPanel.add( Box.createHorizontalStrut( 5));

		parent.add( partialPanel);

		return true;
	}

	/**
	 * @return
	 */
	private JPanel setup_append_initial_stage_button() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 1, 3));

		JPanel partialPanel = new JPanel();
		partialPanel.setLayout( new BoxLayout( partialPanel, BoxLayout.X_AXIS));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridLayout( 1, 1));

		JButton button = new JButton(
			ResourceManager.get_instance().get( "edit.stage.dialog.append.initial.stage.button.name"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_initialStageList.on_append( arg0);
			}
		});

		buttonPanel.add( button);
		partialPanel.add( buttonPanel);

		partialPanel.add( Box.createHorizontalStrut( 5));

		panel.add( partialPanel);

		return panel;
	}

	/**
	 * @param parent
	 */
	private void setup_append_main_stage_button(JPanel parent) {
		JPanel partialPanel = new JPanel();
		partialPanel.setLayout( new BoxLayout( partialPanel, BoxLayout.X_AXIS));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridLayout( 1, 1));

		JButton button = new JButton(
			ResourceManager.get_instance().get( "edit.stage.dialog.append.main.stage.button.name"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_mainStageList.on_append( arg0);
			}
		});

		buttonPanel.add( button);
		partialPanel.add( buttonPanel);

		partialPanel.add( Box.createHorizontalStrut( 5));

		parent.add( partialPanel);
	}

	/**
	 * @param parent
	 */
	private void setup_append_terminal_stage_button(JPanel parent) {
		JPanel partialPanel = new JPanel();
		partialPanel.setLayout( new BoxLayout( partialPanel, BoxLayout.X_AXIS));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridLayout( 1, 1));

		JButton button = new JButton(
			ResourceManager.get_instance().get( "edit.stage.dialog.append.terminal.stage.button.name"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_terminalStageList.on_append( arg0);
			}
		});

		buttonPanel.add( button);
		partialPanel.add( buttonPanel);

		partialPanel.add( Box.createHorizontalStrut( 5));

		parent.add( partialPanel);
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
//		Vector<String> stageNames = _stageList.get_stage_names();
//		if ( !Observer.get_instance().can_adjust_stage_name( stageNames)) {
//			WarningDlg warningDlg = new WarningDlg( _owner);
//			warningDlg.do_modal( this);
//			return false;
//		}

		_mainStageList.on_ok( _stageManager._mainStages);
		_initialStageList.on_ok( _stageManager._initialStages);
		_terminalStageList.on_ok( _stageManager._terminalStages);

		StageManager.get_instance().set( _stageManager);

		Observer.get_instance().on_update_stage();

		Observer.get_instance().modified();

		return true;
	}
}
