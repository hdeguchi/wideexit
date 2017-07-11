/*
 * 2005/06/21
 */
package soars.application.visualshell.object.log.edit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.log.LogManager;
import soars.application.visualshell.object.log.edit.option.LogOptionList;

/**
 * The tab component to edit the log options.
 * @author kurata / SOARS project
 */
public class LogPropertyPage1 extends LogPropertyPageBase {

	/**
	 * 
	 */
	private LogOptionList _agentKeywordList = null;

	/**
	 * 
	 */
	private LogOptionList _agentNumberObjectList = null;

	/**
	 * 
	 */
	private LogOptionList _agentProbabilityList = null;

	/**
	 * 
	 */
	private LogOptionList _agentTimeVariableList = null;

	/**
	 * 
	 */
	private LogOptionList _agentRoleVariableList = null;

	/**
	 * 
	 */
	private LogOptionList _spotKeywordList = null;

	/**
	 * 
	 */
	private LogOptionList _spotNumberObjectList = null;

	/**
	 * 
	 */
	private LogOptionList _spotProbabilityList = null;

	/**
	 * 
	 */
	private LogOptionList _spotTimeVariableList = null;

	/**
	 * Creates this object.
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public LogPropertyPage1(Frame owner, Component parent) {
		super(owner, parent);
		_title = ( ResourceManager.get_instance().get( "edit.log.dialog.title") + 1);
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
		panel.setLayout( new GridLayout( 1, 3));

		JPanel partialPanel = get_keyword_panel();
		if ( null == partialPanel)
			return false;

		panel.add( partialPanel);


		partialPanel = get_number_object_panel();
		if ( null == partialPanel)
			return false;

		panel.add( partialPanel);


		partialPanel = get_probability_panel();
		if ( null == partialPanel)
			return false;

		panel.add( partialPanel);


		partialPanel = get_time_variable_panel();
		if ( null == partialPanel)
			return false;

		panel.add( partialPanel);


		partialPanel = get_role_variable_panel();
		if ( null == partialPanel)
			return false;

		panel.add( partialPanel);


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
	private JPanel get_keyword_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 2, 1));

		JPanel partialPanel = get_agent_keyword_panel();
		if ( null == partialPanel)
			return null;

		panel.add( partialPanel);

		partialPanel = get_spot_keyword_panel();
		if ( null == partialPanel)
			return null;

		panel.add( partialPanel);

		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_number_object_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 2, 1));

		JPanel partialPanel = get_agent_number_object_panel();
		if ( null == partialPanel)
			return null;

		panel.add( partialPanel);

		partialPanel = get_spot_number_object_panel();
		if ( null == partialPanel)
			return null;

		panel.add( partialPanel);

		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_probability_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 2, 1));

		JPanel partialPanel = get_agent_probability_panel();
		if ( null == partialPanel)
			return null;

		panel.add( partialPanel);

		partialPanel = get_spot_probability_panel();
		if ( null == partialPanel)
			return null;

		panel.add( partialPanel);

		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_time_variable_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 2, 1));

		JPanel partialPanel = get_agent_time_panel();
		if ( null == partialPanel)
			return null;

		panel.add( partialPanel);

		partialPanel = get_spot_time_panel();
		if ( null == partialPanel)
			return null;

		panel.add( partialPanel);

		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_role_variable_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 2, 1));

		JPanel partialPanel = get_agent_role_variable_panel();
		if ( null == partialPanel)
			return null;

		panel.add( partialPanel);

		partialPanel = get_spot_role_variable_panel();
		if ( null == partialPanel)
			return null;

		panel.add( partialPanel);

		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_agent_keyword_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.agent.keyword.name"), northPanel);

		panel.add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		_agentKeywordList = setup_list( LogManager.get_instance().get( "agent").get( "keyword"), centerPanel);
		if ( null == _agentKeywordList)
			return null;

		panel.add( centerPanel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_spot_keyword_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.spot.keyword.name"), northPanel);

		panel.add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		_spotKeywordList = setup_list( LogManager.get_instance().get( "spot").get( "keyword"), centerPanel);
		if ( null == _spotKeywordList)
			return null;

		panel.add( centerPanel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_agent_number_object_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.agent.number.object.name"), northPanel);

		panel.add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		_agentNumberObjectList = setup_list( LogManager.get_instance().get( "agent").get( "number object"), centerPanel);
		if ( null == _agentNumberObjectList)
			return null;

		panel.add( centerPanel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_spot_number_object_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.spot.number.object.name"), northPanel);

		panel.add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		_spotNumberObjectList = setup_list( LogManager.get_instance().get( "spot").get( "number object"), centerPanel);
		if ( null == _spotNumberObjectList)
			return null;

		panel.add( centerPanel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_agent_probability_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.agent.probability.name"), northPanel);

		panel.add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		_agentProbabilityList = setup_list( LogManager.get_instance().get( "agent").get( "probability"), centerPanel);
		if ( null == _agentProbabilityList)
			return null;

		panel.add( centerPanel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_spot_probability_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.spot.probability.name"), northPanel);

		panel.add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		_spotProbabilityList = setup_list( LogManager.get_instance().get( "spot").get( "probability"), centerPanel);
		if ( null == _spotProbabilityList)
			return null;

		panel.add( centerPanel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_agent_time_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.agent.time.variable.name"), northPanel);

		panel.add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		_agentTimeVariableList = setup_list( LogManager.get_instance().get( "agent").get( "time variable"), centerPanel);
		if ( null == _agentTimeVariableList)
			return null;

		panel.add( centerPanel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_spot_time_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.spot.time.variable.name"), northPanel);

		panel.add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		_spotTimeVariableList = setup_list( LogManager.get_instance().get( "spot").get( "time variable"), centerPanel);
		if ( null == _spotTimeVariableList)
			return null;

		panel.add( centerPanel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_agent_role_variable_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_label( ResourceManager.get_instance().get( "edit.log.dialog.agent.role.variable.name"), northPanel);

		panel.add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		_agentRoleVariableList = setup_list( LogManager.get_instance().get( "agent").get( "role variable"), centerPanel);
		if ( null == _agentRoleVariableList)
			return null;

		panel.add( centerPanel);



		return panel;
	}

	/**
	 * @return
	 */
	private JPanel get_spot_role_variable_panel() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_label( "", northPanel);

		panel.add( northPanel, "North");



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		panel.add( centerPanel);



		return panel;
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
		_agentKeywordList.on_ok( LogManager.get_instance().get( "agent").get( "keyword"));
		_agentNumberObjectList.on_ok( LogManager.get_instance().get( "agent").get( "number object"));
		_agentProbabilityList.on_ok( LogManager.get_instance().get( "agent").get( "probability"));
		_agentTimeVariableList.on_ok( LogManager.get_instance().get( "agent").get( "time variable"));
		_agentRoleVariableList.on_ok( LogManager.get_instance().get( "agent").get( "role variable"));
		_spotKeywordList.on_ok( LogManager.get_instance().get( "spot").get( "keyword"));
		_spotNumberObjectList.on_ok( LogManager.get_instance().get( "spot").get( "number object"));
		_spotProbabilityList.on_ok( LogManager.get_instance().get( "spot").get( "probability"));
		_spotTimeVariableList.on_ok( LogManager.get_instance().get( "spot").get( "time variable"));
		return true;
	}
}
