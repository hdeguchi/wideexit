/*
 * Created on 2005/09/16
 */
package soars.application.visualshell.object.log.edit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.log.LogManager;
import soars.application.visualshell.object.log.edit.option.LogOptionList;

/**
 * The tab component to edit the log options.
 * @author kurata / SOARS project
 */
public class LogPropertyPage2 extends LogPropertyPageBase {

	/**
	 * 
	 */
	private LogOptionList _agentCollectionList = null; 

	/**
	 * 
	 */
	private LogOptionList _agentListList = null; 

	/**
	 * 
	 */
	private LogOptionList _agentMapList = null; 

	/**
	 * 
	 */
	private LogOptionList _agentSpotVariableList = null; 

	/**
	 * 
	 */
	private LogOptionList _agentExchangeAlgebraList = null;

	/**
	 * 
	 */
	private LogOptionList _spotCollectionList = null; 

	/**
	 * 
	 */
	private LogOptionList _spotListList = null; 

	/**
	 * 
	 */
	private LogOptionList _spotSpotVariableList = null; 

	/**
	 * 
	 */
	private LogOptionList _spotMapList = null; 

	/**
	 * 
	 */
	private LogOptionList _spotExchangeAlgebraList = null;

	/**
	 * Creates this object.
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public LogPropertyPage2(Frame owner, Component parent) {
		super(owner, parent);
		_title = ( ResourceManager.get_instance().get( "edit.log.dialog.title") + 2);
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.panel.StandardPanel#on_create()
	 */
	protected boolean on_create() {
		if ( !super.on_create())
			return false;



		setLayout( new BorderLayout());



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BoxLayout( basePanel, BoxLayout.X_AXIS));

		basePanel.add( Box.createHorizontalStrut( 5));

		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 1, 4));

		JPanel partialPanel = get_collection_panel();
		if ( null == partialPanel)
			return false;

		panel.add( partialPanel);


		partialPanel = get_list_panel();
		if ( null == partialPanel)
			return false;

		panel.add( partialPanel);


		partialPanel = get_spot_variable_panel();
		if ( null == partialPanel)
			return false;

		panel.add( partialPanel);


		partialPanel = get_map_panel();
		if ( null == partialPanel)
			return false;

		panel.add( partialPanel);


		if ( Environment.get_instance().is_exchange_algebra_enable()) {
			partialPanel = get_exchange_algebra_panel();
			if ( null == partialPanel)
				return false;

			panel.add( partialPanel);
		}


		basePanel.add( panel);
		centerPanel.add( basePanel);

		add( centerPanel);



		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( southPanel);

		add( southPanel, "South");



		return true;
	}

	/**
	 * @return
	 */
	private JPanel get_collection_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 2, 1));

		JPanel partialPanel = get_agent_collection_panel();
		if ( null == partialPanel)
			return null;

		panel.add( partialPanel);

		partialPanel = get_spot_collection_panel();
		if ( null == partialPanel)
			return null;

		panel.add( partialPanel);

		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_list_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 2, 1));

		JPanel partialPanel = get_agent_list_panel();
		if ( null == partialPanel)
			return null;

		panel.add( partialPanel);

		partialPanel = get_spot_list_panel();
		if ( null == partialPanel)
			return null;

		panel.add( partialPanel);

		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_spot_variable_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 2, 1));

		JPanel partialPanel = get_agent_spot_variable_panel();
		if ( null == partialPanel)
			return null;

		panel.add( partialPanel);

		partialPanel = get_spot_spot_variable_panel();
		if ( null == partialPanel)
			return null;

		panel.add( partialPanel);

		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_map_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 2, 1));

		JPanel partialPanel = get_agent_map_panel();
		if ( null == partialPanel)
			return null;

		panel.add( partialPanel);

		partialPanel = get_spot_map_panel();
		if ( null == partialPanel)
			return null;

		panel.add( partialPanel);

		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_exchange_algebra_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 2, 1));

		JPanel partialPanel = get_agent_exchange_algebra_panel();
		if ( null == partialPanel)
			return null;

		panel.add( partialPanel);

		partialPanel = get_spot_exchange_algebra_panel();
		if ( null == partialPanel)
			return null;

		panel.add( partialPanel);

		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_agent_collection_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.agent.collection.name"), northPanel);

		panel.add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		_agentCollectionList = setup_list( LogManager.get_instance().get( "agent").get( "collection"), centerPanel);
		if ( null == _agentCollectionList)
			return null;

		panel.add( centerPanel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_spot_collection_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.spot.collection.name"), northPanel);

		panel.add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		_spotCollectionList = setup_list( LogManager.get_instance().get( "spot").get( "collection"), centerPanel);
		if ( null == _spotCollectionList)
			return null;

		panel.add( centerPanel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_agent_list_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.agent.list.name"), northPanel);

		panel.add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		_agentListList = setup_list( LogManager.get_instance().get( "agent").get( "list"), centerPanel);
		if ( null == _agentListList)
			return null;

		panel.add( centerPanel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_spot_list_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.spot.list.name"), northPanel);

		panel.add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		_spotListList = setup_list( LogManager.get_instance().get( "spot").get( "list"), centerPanel);
		if ( null == _spotListList)
			return null;

		panel.add( centerPanel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_agent_map_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.agent.map.name"), northPanel);

		panel.add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		_agentMapList = setup_list( LogManager.get_instance().get( "agent").get( "map"), centerPanel);
		if ( null == _agentMapList)
			return null;

		panel.add( centerPanel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_spot_map_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.spot.map.name"), northPanel);

		panel.add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		_spotMapList = setup_list( LogManager.get_instance().get( "spot").get( "map"), centerPanel);
		if ( null == _spotMapList)
			return null;

		panel.add( centerPanel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_agent_spot_variable_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.agent.spot.variable.name"), northPanel);

		panel.add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		_agentSpotVariableList = setup_list( LogManager.get_instance().get( "agent").get( "spot variable"), centerPanel);
		if ( null == _agentSpotVariableList)
			return null;

		panel.add( centerPanel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_spot_spot_variable_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.spot.spot.variable.name"), northPanel);

		panel.add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		_spotSpotVariableList = setup_list( LogManager.get_instance().get( "spot").get( "spot variable"), centerPanel);
		if ( null == _spotSpotVariableList)
			return null;

		panel.add( centerPanel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_agent_exchange_algebra_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.agent.exchange.algebra.name"), northPanel);

		panel.add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		_agentExchangeAlgebraList = setup_list( LogManager.get_instance().get( "agent").get( "exchange algebra"), centerPanel);
		if ( null == _agentExchangeAlgebraList)
			return null;

		panel.add( centerPanel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_spot_exchange_algebra_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.spot.exchange.algebra.name"), northPanel);

		panel.add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		_spotExchangeAlgebraList = setup_list( LogManager.get_instance().get( "spot").get( "exchange algebra"), centerPanel);
		if ( null == _spotExchangeAlgebraList)
			return null;

		panel.add( centerPanel);



		return panel;
	}

	/**
	 * Invoked when this objet has been initialized.
	 */
	public void on_setup_completed() {
		_agentCollectionList.requestFocusInWindow();
	}

	/**
	 * Returns true for updating successfully.
	 * @return true for updating successfully
	 */
	public boolean on_ok() {
		_agentCollectionList.on_ok( LogManager.get_instance().get( "agent").get( "collection"));
		_agentListList.on_ok( LogManager.get_instance().get( "agent").get( "list"));
		_agentMapList.on_ok( LogManager.get_instance().get( "agent").get( "map"));
		_agentSpotVariableList.on_ok( LogManager.get_instance().get( "agent").get( "spot variable"));
		_spotCollectionList.on_ok( LogManager.get_instance().get( "spot").get( "collection"));
		_spotListList.on_ok( LogManager.get_instance().get( "spot").get( "list"));
		_spotMapList.on_ok( LogManager.get_instance().get( "spot").get( "map"));
		_spotSpotVariableList.on_ok( LogManager.get_instance().get( "spot").get( "spot variable"));

		if ( Environment.get_instance().is_exchange_algebra_enable()) {
			_agentExchangeAlgebraList.on_ok( LogManager.get_instance().get( "agent").get( "exchange algebra"));
			_spotExchangeAlgebraList.on_ok( LogManager.get_instance().get( "spot").get( "exchange algebra"));
		}

		return true;
	}
}
