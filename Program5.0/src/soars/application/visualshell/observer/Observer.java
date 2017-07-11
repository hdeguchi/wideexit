/*
 * 2005/06/09
 */
package soars.application.visualshell.observer;

import java.util.Vector;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.MainFrame;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.EditObjectDlg;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.experiment.ExperimentManager;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.log.LogManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.table.RuleTable;
import soars.application.visualshell.object.stage.StageManager;
import soars.application.visualshell.state.StateManager;

/**
 * @author kurata
 */
public class Observer {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private Observer _observer = null;

	/**
	 * 
	 */
	static {
		startup();
	}

	/**
	 * 
	 */
	private static void startup() {
		synchronized( _lock) {
			if ( null == _observer) {
				_observer = new Observer();
			}
		}
	}

	/**
	 * @return
	 */
	public static Observer get_instance() {
		if ( null == _observer) {
			System.exit( 0);
		}

		return _observer;
	}

	/**
	 * 
	 */
	public Observer() {
		super();
	}

	/**
	 * @param all
	 */
	public void on_update_entityBase(boolean all) {
		StateManager.get_instance().refresh();
		EditRoleFrame.clear();
		if ( all)
			EditObjectDlg.clear();
	}

	/**
	 * 
	 */
	public void on_update_chartObject() {
		StateManager.get_instance().refresh();
		EditObjectDlg.clear();
		EditRoleFrame.clear();
		StageManager.get_instance().update();
	}

	/**
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @param entityBase
	 * @return
	 */
	public boolean on_update_object_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges, EntityBase entityBase) {
		if ( entityBase instanceof AgentObject)
			return LayerManager.get_instance().update_agent_name_and_number( newName, originalName, headName, ranges, newHeadName, newRanges);
		else if ( entityBase instanceof SpotObject)
			return LayerManager.get_instance().update_spot_name_and_number( newName, originalName, headName, ranges, newHeadName, newRanges);
		return false;
	}

	/**
	 * @param originalName
	 * @param role
	 * @return
	 */
	public boolean on_update_role_name(String originalName, Role role) {
		boolean result = LayerManager.get_instance().update_role_name( originalName, role);
		RuleTable.on_update_role_name( originalName, role);
		return result;
	}

	/**
	 * @param kind
	 */
	public void on_update_agent_object(String kind) {
		LogManager.get_instance().update( "agent", kind);
	}

	/**
	 * @param kind
	 */
	public void on_update_spot_object(String kind) {
		LogManager.get_instance().update( "spot", kind);
	}

	/**
	 * @param kind
	 * @param name
	 * @param newName
	 */
	public void on_update_agent_object(String kind, String name, String newName) {
		LogManager.get_instance().update( "agent", kind, name, newName);
	}

	/**
	 * @param kind
	 * @param name
	 * @param newName
	 */
	public void on_update_spot_object(String kind, String name, String newName) {
		LogManager.get_instance().update( "spot", kind, name, newName);
	}

	/**
	 * @param kind
	 */
	public void on_update_object(String kind) {
		if ( kind.equals( "role variable")
			|| kind.equals( "spot variable")
			|| kind.equals( "class variable"))
			return;
		ExperimentManager.get_instance().on_update_object( kind);
		MainFrame.get_instance().update_menu();
	}

	/**
	 * @param all
	 */
	public void on_update_role(boolean all) {
		ExperimentManager.get_instance().on_update_role();
		MainFrame.get_instance().update_menu();
		StateManager.get_instance().refresh();
		EditObjectDlg.clear();
		if ( all)
			EditRoleFrame.clear();
//		else {
//			ConditionTable2.refresh();
//			CommandTable2.refresh();
//		}
	}

	/**
	 * 
	 */
	public void on_update_stage() {
		StateManager.get_instance().refresh();
		EditObjectDlg.clear();
		EditRoleFrame.clear();
	}

	/**
	 * 
	 */
	public void on_update_model() {
		ExperimentManager.get_instance().on_update_simulation();
		MainFrame.get_instance().update_menu();
		StateManager.get_instance().refresh();
		EditObjectDlg.clear();
		EditRoleFrame.clear();
	}

	/**
	 * @return
	 */
	public boolean on_update_expression() {
		return LayerManager.get_instance().update_expression( VisualShellExpressionManager.get_instance());
	}

	/**
	 * 
	 */
	public void on_update_experiment() {
		MainFrame.get_instance().update_menu();
		StateManager.get_instance().refresh();
		EditObjectDlg.clear();
		EditRoleFrame.clear();
	}

	/**
	 * 
	 */
	public void modified() {
		LayerManager.get_instance().modified();
	}
}
