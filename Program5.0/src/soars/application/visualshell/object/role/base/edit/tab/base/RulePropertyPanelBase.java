/*
 * 2005/07/28
 */
package soars.application.visualshell.object.role.base.edit.tab.base;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.visualshell.common.selector.IObjectSelectorHandler;
import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.object.class_variable.ClassVariableObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;
import soars.common.utility.swing.panel.StandardPanel;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextLimiter;
import soars.common.utility.tool.expression.Expression;

/**
 * @author kurata
 */
public class RulePropertyPanelBase extends StandardPanel implements IObjectSelectorHandler {

	/**
	 * 
	 */
	public String _title = "";

	/**
	 * 
	 */
	public String _kind = "";

	/**
	 * 
	 */
	public String _type = "";

	/**
	 * 
	 */
	public Color _color = null;

	/**
	 * 
	 */
	public Role _role = null;

	/**
	 * 
	 */
	public int _index = 0;

	/**
	 * 
	 */
	protected Frame _owner = null;

	/**
	 * 
	 */
	protected EditRoleFrame _parent = null;

	/**
	 * 
	 */
	public int _standardSpotNameWidth = 120;				//150;

	/**
	 * 
	 */
	public int _standardNumberSpinnerWidth = 80;		//100

	/**
	 * 
	 */
	public int _standardControlWidth = 200;

//	/**
//	 * 
//	 */
//	protected int _applyButtonWidth = 100;

	/**
	 * 
	 */
	static protected String[][] _agentNames = null;

	/**
	 * 
	 */
	static protected String[][] _spotNames = null;

	/**
	 * 
	 */
	static protected String[][] _agentProbabilityNames = null;

	/**
	 * 
	 */
	static protected String[][] _agentCollectionNames = null;

	/**
	 * 
	 */
	static protected String[][] _agentListNames = null;

	/**
	 * 
	 */
	static protected String[][] _agentMapNames = null;

	/**
	 * 
	 */
	static protected String[][] _agentKeywordNames = null;

	/**
	 * 
	 */
	static protected String[][] _agentNumberObjectNames = null;

	/**
	 * 
	 */
	static protected String[][] _agentIntegerNumberObjectNames = null;

	/**
	 * 
	 */
	static protected String[][] _agentRealNumberObjectNames = null;

	/**
	 * 
	 */
	static protected String[][] _agentRoleVariableNames = null;

	/**
	 * 
	 */
	static protected String[][] _agentTimeVariableNames = null;

	/**
	 * 
	 */
	static protected String[][] _agentAgentVariableNames = null;

	/**
	 * 
	 */
	static protected String[][] _agentSpotVariableNames = null;

	/**
	 * 
	 */
	static protected String[][] _agentClassVariableNames = null;

	/**
	 * 
	 */
	static protected String[][] _agentFileNames = null;

	/**
	 * 
	 */
	static protected String[][] _agentExchangeAlgebraNames = null;

	/**
	 * 
	 */
	static protected String[][] _agentExchangeAlgebraNames2 = null;

	/**
	 * 
	 */
	static protected String[][] _agentExTransferNames = null;

	/**
	 * 
	 */
	static protected Map<String, ClassVariableObject> _agentClassVariableMap = null;

	/**
	 * 
	 */
	static protected String[][] _spotProbabilityNames = null;

	/**
	 * 
	 */
	static protected String[][] _spotCollectionNames = null;

	/**
	 * 
	 */
	static protected String[][] _spotListNames = null;

	/**
	 * 
	 */
	static protected String[][] _spotMapNames = null;

	/**
	 * 
	 */
	static protected String[][] _spotKeywordNames = null;

	/**
	 * 
	 */
	static protected String[][] _spotNumberObjectNames = null;

	/**
	 * 
	 */
	static protected String[][] _spotIntegerNumberObjectNames = null;

	/**
	 * 
	 */
	static protected String[][] _spotRealNumberObjectNames = null;

	/**
	 * 
	 */
	static protected String[][] _spotTimeVariableNames = null;

	/**
	 * 
	 */
	static protected String[][] _spotAgentVariableNames = null;

	/**
	 * 
	 */
	static protected String[][] _spotSpotVariableNames = null;

	/**
	 * 
	 */
	static protected String[][] _spotClassVariableNames = null;

	/**
	 * 
	 */
	static protected String[][] _spotFileNames = null;

	/**
	 * 
	 */
	static protected String[][] _spotExchangeAlgebraNames = null;

	/**
	 * 
	 */
	static protected String[][] _spotExchangeAlgebraNames2 = null;

	/**
	 * 
	 */
	static protected String[][] _spotExTransferNames = null;

	/**
	 * 
	 */
	static protected Map<String, ClassVariableObject> _spotClassVariableMap = null;

	/**
	 * 
	 */
	static protected String[][] _roleNames = null;

	/**
	 * 
	 */
	static protected String[][] _agentRoleNames = null;

	/**
	 * 
	 */
	static protected String[][] _spotRoleNames = null;

	/**
	 * 
	 */
	public static void refresh() {
		_agentNames = null;
		_spotNames = null;

		_agentProbabilityNames = null;
		_agentCollectionNames = null;
		_agentListNames = null;
		_agentMapNames = null;
		_agentKeywordNames = null;
		_agentNumberObjectNames = null;
		_agentIntegerNumberObjectNames = null;
		_agentRealNumberObjectNames = null;
		_agentRoleVariableNames = null;
		_agentTimeVariableNames = null;
		_agentAgentVariableNames = null;
		_agentSpotVariableNames = null;
		_agentClassVariableNames = null;
		_agentFileNames = null;
		_agentExchangeAlgebraNames = null;
		_agentExchangeAlgebraNames2 = null;
		_agentExTransferNames = null;

		_agentClassVariableMap = null;

		_spotProbabilityNames = null;
		_spotCollectionNames = null;
		_spotListNames = null;
		_spotMapNames = null;
		_spotKeywordNames = null;
		_spotNumberObjectNames = null;
		_spotIntegerNumberObjectNames = null;
		_spotRealNumberObjectNames = null;
		_spotTimeVariableNames = null;
		_spotAgentVariableNames = null;
		_spotSpotVariableNames = null;
		_spotClassVariableNames = null;
		_spotFileNames = null;
		_spotExchangeAlgebraNames = null;
		_spotExchangeAlgebraNames2 = null;
		_spotExTransferNames = null;

		_spotClassVariableMap = null;

		_roleNames = null;
		_agentRoleNames = null;
		_spotRoleNames = null;
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_agent_names(boolean containsEmpty) {
		if ( null == _agentNames) {
			_agentNames = new String[ 2][];
			_agentNames[ 0] = LayerManager.get_instance().get_agent_names( false);
			_agentNames[ 1] = LayerManager.get_instance().get_agent_names( true);
		}

		return _agentNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_spot_names(boolean containsEmpty) {
		if ( null == _spotNames) {
			_spotNames = new String[ 2][];
			_spotNames[ 0] = LayerManager.get_instance().get_spot_names( false);
			_spotNames[ 1] = LayerManager.get_instance().get_spot_names( true);
		}

		return _spotNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_agent_probability_names(boolean containsEmpty) {
		if ( null == _agentProbabilityNames) {
			_agentProbabilityNames = new String[ 2][];
			_agentProbabilityNames[ 0] = LayerManager.get_instance().get_agent_object_names( "probability", false);
			_agentProbabilityNames[ 1] = LayerManager.get_instance().get_agent_object_names( "probability", true);
		}

		return _agentProbabilityNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_agent_collection_names(boolean containsEmpty) {
		if ( null == _agentCollectionNames) {
			_agentCollectionNames = new String[ 2][];
			_agentCollectionNames[ 0] = LayerManager.get_instance().get_agent_object_names( "collection", false);
			_agentCollectionNames[ 1] = LayerManager.get_instance().get_agent_object_names( "collection", true);
		}

		return _agentCollectionNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_agent_list_names(boolean containsEmpty) {
		if ( null == _agentListNames) {
			_agentListNames = new String[ 2][];
			_agentListNames[ 0] = LayerManager.get_instance().get_agent_object_names( "list", false);
			_agentListNames[ 1] = LayerManager.get_instance().get_agent_object_names( "list", true);
		}

		return _agentListNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_agent_map_names(boolean containsEmpty) {
		if ( null == _agentMapNames) {
			_agentMapNames = new String[ 2][];
			_agentMapNames[ 0] = LayerManager.get_instance().get_agent_object_names( "map", false);
			_agentMapNames[ 1] = LayerManager.get_instance().get_agent_object_names( "map", true);
		}

		return _agentMapNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_agent_keyword_names(boolean containsEmpty) {
		if ( null == _agentKeywordNames) {
			_agentKeywordNames = new String[ 2][];
			_agentKeywordNames[ 0] = LayerManager.get_instance().get_agent_object_names( "keyword", false);
			_agentKeywordNames[ 1] = LayerManager.get_instance().get_agent_object_names( "keyword", true);
		}

		return _agentKeywordNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_agent_number_object_names(boolean containsEmpty) {
		if ( null == _agentNumberObjectNames) {
			_agentNumberObjectNames = new String[ 2][];
			_agentNumberObjectNames[ 0] = LayerManager.get_instance().get_agent_object_names( "number object", false);
			_agentNumberObjectNames[ 1] = LayerManager.get_instance().get_agent_object_names( "number object", true);
		}

		return _agentNumberObjectNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_agent_integer_number_object_names(boolean containsEmpty) {
		if ( null == _agentIntegerNumberObjectNames) {
			_agentIntegerNumberObjectNames = new String[ 2][];
			_agentIntegerNumberObjectNames[ 0] = LayerManager.get_instance().get_agent_number_object_names( "integer", false);
			_agentIntegerNumberObjectNames[ 1] = LayerManager.get_instance().get_agent_number_object_names( "integer", true);
		}

		return _agentIntegerNumberObjectNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_agent_real_number_object_names(boolean containsEmpty) {
		if ( null == _agentRealNumberObjectNames) {
			_agentRealNumberObjectNames = new String[ 2][];
			_agentRealNumberObjectNames[ 0] = LayerManager.get_instance().get_agent_number_object_names( "real number", false);
			_agentRealNumberObjectNames[ 1] = LayerManager.get_instance().get_agent_number_object_names( "real number", true);
		}

		return _agentRealNumberObjectNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_agent_role_variable_names(boolean containsEmpty) {
		if ( null == _agentRoleVariableNames) {
			_agentRoleVariableNames = new String[ 2][];
			_agentRoleVariableNames[ 0] = LayerManager.get_instance().get_agent_object_names( "role variable", false);
			_agentRoleVariableNames[ 1] = LayerManager.get_instance().get_agent_object_names( "role variable", true);
		}

		return _agentRoleVariableNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_agent_time_variable_names(boolean containsEmpty) {
		if ( null == _agentTimeVariableNames) {
			_agentTimeVariableNames = new String[ 2][];
			_agentTimeVariableNames[ 0] = LayerManager.get_instance().get_agent_object_names( "time variable", false);
			_agentTimeVariableNames[ 1] = LayerManager.get_instance().get_agent_object_names( "time variable", true);
		}

		return _agentTimeVariableNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_agent_agent_variable_names(boolean containsEmpty) {
		// TODO 2012.9.20
		if ( null == _agentAgentVariableNames) {
			_agentAgentVariableNames = new String[ 2][];
			_agentAgentVariableNames[ 0] = LayerManager.get_instance().get_agent_object_names( "agent variable", false);
			_agentAgentVariableNames[ 1] = LayerManager.get_instance().get_agent_object_names( "agent variable", true);
		}

		return _agentAgentVariableNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_agent_spot_variable_names(boolean containsEmpty) {
		if ( null == _agentSpotVariableNames) {
			_agentSpotVariableNames = new String[ 2][];
			_agentSpotVariableNames[ 0] = LayerManager.get_instance().get_agent_object_names( "spot variable", false);
			_agentSpotVariableNames[ 1] = LayerManager.get_instance().get_agent_object_names( "spot variable", true);
		}

		return _agentSpotVariableNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_agent_class_variable_names(boolean containsEmpty) {
		if ( null == _agentClassVariableNames) {
			_agentClassVariableNames = new String[ 2][];
			_agentClassVariableNames[ 0] = LayerManager.get_instance().get_agent_object_names( "class variable", false);
			_agentClassVariableNames[ 1] = LayerManager.get_instance().get_agent_object_names( "class variable", true);
		}

		return _agentClassVariableNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param classVariableName
	 * @return
	 */
	public static ClassVariableObject get_agent_class_variable(String classVariableName) {
		if ( null == _agentClassVariableMap) {
			_agentClassVariableMap = new HashMap<String, ClassVariableObject>();
			LayerManager.get_instance().get_agent_class_variable_map( _agentClassVariableMap);
		}
		return _agentClassVariableMap.get( classVariableName);
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_agent_file_names(boolean containsEmpty) {
		if ( null == _agentFileNames) {
			_agentFileNames = new String[ 2][];
			_agentFileNames[ 0] = LayerManager.get_instance().get_agent_object_names( "file", false);
			_agentFileNames[ 1] = LayerManager.get_instance().get_agent_object_names( "file", true);
		}

		return _agentFileNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_agent_exchange_algebra_names(boolean containsEmpty) {
		if ( null == _agentExchangeAlgebraNames) {
			_agentExchangeAlgebraNames = new String[ 2][];
			_agentExchangeAlgebraNames[ 0] = LayerManager.get_instance().get_agent_object_names( "exchange algebra", false);
			_agentExchangeAlgebraNames[ 1] = LayerManager.get_instance().get_agent_object_names( "exchange algebra", true);
		}

		return _agentExchangeAlgebraNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_agent_exchange_algebra_names2(boolean containsEmpty) {
		if ( null == _agentExchangeAlgebraNames2) {
			_agentExchangeAlgebraNames2 = new String[ 2][];
			_agentExchangeAlgebraNames2[ 0] = LayerManager.get_instance().get_agent_exchange_algebra_names( false);
			_agentExchangeAlgebraNames2[ 1] = LayerManager.get_instance().get_agent_exchange_algebra_names( true);
		}

		return _agentExchangeAlgebraNames2[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_agent_extransfer_names(boolean containsEmpty) {
		if ( null == _agentExTransferNames) {
			_agentExTransferNames = new String[ 2][];
			_agentExTransferNames[ 0] = LayerManager.get_instance().get_agent_object_names( "extransfer", false);
			_agentExTransferNames[ 1] = LayerManager.get_instance().get_agent_object_names( "extransfer", true);
		}

		return _agentExTransferNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_spot_probability_names(boolean containsEmpty) {
		if ( null == _spotProbabilityNames) {
			_spotProbabilityNames = new String[ 2][];
			_spotProbabilityNames[ 0] = LayerManager.get_instance().get_spot_object_names( "probability", false);
			_spotProbabilityNames[ 1] = LayerManager.get_instance().get_spot_object_names( "probability", true);
		}

		return _spotProbabilityNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_spot_collection_names(boolean containsEmpty) {
		if ( null == _spotCollectionNames) {
			_spotCollectionNames = new String[ 2][];
			_spotCollectionNames[ 0] = LayerManager.get_instance().get_spot_object_names( "collection", false);
			_spotCollectionNames[ 1] = LayerManager.get_instance().get_spot_object_names( "collection", true);
		}

		return _spotCollectionNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_spot_list_names(boolean containsEmpty) {
		if ( null == _spotListNames) {
			_spotListNames = new String[ 2][];
			_spotListNames[ 0] = LayerManager.get_instance().get_spot_object_names( "list", false);
			_spotListNames[ 1] = LayerManager.get_instance().get_spot_object_names( "list", true);
		}

		return _spotListNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_spot_map_names(boolean containsEmpty) {
		if ( null == _spotMapNames) {
			_spotMapNames = new String[ 2][];
			_spotMapNames[ 0] = LayerManager.get_instance().get_spot_object_names( "map", false);
			_spotMapNames[ 1] = LayerManager.get_instance().get_spot_object_names( "map", true);
		}

		return _spotMapNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_spot_keyword_names(boolean containsEmpty) {
		if ( null == _spotKeywordNames) {
			_spotKeywordNames = new String[ 2][];
			_spotKeywordNames[ 0] = LayerManager.get_instance().get_spot_object_names( "keyword", false);
			_spotKeywordNames[ 1] = LayerManager.get_instance().get_spot_object_names( "keyword", true);
		}

		return _spotKeywordNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_spot_number_object_names(boolean containsEmpty) {
		if ( null == _spotNumberObjectNames) {
			_spotNumberObjectNames = new String[ 2][];
			_spotNumberObjectNames[ 0] = LayerManager.get_instance().get_spot_object_names( "number object", false);
			_spotNumberObjectNames[ 1] = LayerManager.get_instance().get_spot_object_names( "number object", true);
		}

		return _spotNumberObjectNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_spot_integer_number_object_names(boolean containsEmpty) {
		if ( null == _spotIntegerNumberObjectNames) {
			_spotIntegerNumberObjectNames = new String[ 2][];
			_spotIntegerNumberObjectNames[ 0] = LayerManager.get_instance().get_spot_number_object_names( "integer", false);
			_spotIntegerNumberObjectNames[ 1] = LayerManager.get_instance().get_spot_number_object_names( "integer", true);
		}

		return _spotIntegerNumberObjectNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_spot_real_number_object_names(boolean containsEmpty) {
		if ( null == _spotRealNumberObjectNames) {
			_spotRealNumberObjectNames = new String[ 2][];
			_spotRealNumberObjectNames[ 0] = LayerManager.get_instance().get_spot_number_object_names( "real number", false);
			_spotRealNumberObjectNames[ 1] = LayerManager.get_instance().get_spot_number_object_names( "real number", true);
		}

		return _spotRealNumberObjectNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_spot_role_variable_names(boolean containsEmpty) {
		// TODO 2015.7.13
		return null;
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_spot_time_variable_names(boolean containsEmpty) {
		if ( null == _spotTimeVariableNames) {
			_spotTimeVariableNames = new String[ 2][];
			_spotTimeVariableNames[ 0] = LayerManager.get_instance().get_spot_object_names( "time variable", false);
			_spotTimeVariableNames[ 1] = LayerManager.get_instance().get_spot_object_names( "time variable", true);
		}

		return _spotTimeVariableNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_spot_agent_variable_names(boolean containsEmpty) {
		// TODO 2012.9.20
		if ( null == _spotAgentVariableNames) {
			_spotAgentVariableNames = new String[ 2][];
			_spotAgentVariableNames[ 0] = LayerManager.get_instance().get_spot_object_names( "agent variable", false);
			_spotAgentVariableNames[ 1] = LayerManager.get_instance().get_spot_object_names( "agent variable", true);
		}

		return _spotAgentVariableNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_spot_spot_variable_names(boolean containsEmpty) {
		if ( null == _spotSpotVariableNames) {
			_spotSpotVariableNames = new String[ 2][];
			_spotSpotVariableNames[ 0] = LayerManager.get_instance().get_spot_object_names( "spot variable", false);
			_spotSpotVariableNames[ 1] = LayerManager.get_instance().get_spot_object_names( "spot variable", true);
		}

		return _spotSpotVariableNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_spot_class_variable_names(boolean containsEmpty) {
		if ( null == _spotClassVariableNames) {
			_spotClassVariableNames = new String[ 2][];
			_spotClassVariableNames[ 0] = LayerManager.get_instance().get_spot_object_names( "class variable", false);
			_spotClassVariableNames[ 1] = LayerManager.get_instance().get_spot_object_names( "class variable", true);
		}

		return _spotClassVariableNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param classVariableName
	 * @return
	 */
	public static ClassVariableObject get_spot_class_variable(String classVariableName) {
		if ( null == _spotClassVariableMap) {
			_spotClassVariableMap = new HashMap<String, ClassVariableObject>();
			LayerManager.get_instance().get_spot_class_variable_map( _spotClassVariableMap);
		}
		return _spotClassVariableMap.get( classVariableName);
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_spot_file_names(boolean containsEmpty) {
		if ( null == _spotFileNames) {
			_spotFileNames = new String[ 2][];
			_spotFileNames[ 0] = LayerManager.get_instance().get_spot_object_names( "file", false);
			_spotFileNames[ 1] = LayerManager.get_instance().get_spot_object_names( "file", true);
		}

		return _spotFileNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_spot_exchange_algebra_names(boolean containsEmpty) {
		if ( null == _spotExchangeAlgebraNames) {
			_spotExchangeAlgebraNames = new String[ 2][];
			_spotExchangeAlgebraNames[ 0] = LayerManager.get_instance().get_spot_object_names( "exchange algebra", false);
			_spotExchangeAlgebraNames[ 1] = LayerManager.get_instance().get_spot_object_names( "exchange algebra", true);
		}

		return _spotExchangeAlgebraNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_spot_exchange_algebra_names2(boolean containsEmpty) {
		if ( null == _spotExchangeAlgebraNames2) {
			_spotExchangeAlgebraNames2 = new String[ 2][];
			_spotExchangeAlgebraNames2[ 0] = LayerManager.get_instance().get_spot_exchange_algebra_names( false);
			_spotExchangeAlgebraNames2[ 1] = LayerManager.get_instance().get_spot_exchange_algebra_names( true);
		}

		return _spotExchangeAlgebraNames2[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_spot_extransfer_names(boolean containsEmpty) {
		if ( null == _spotExTransferNames) {
			_spotExTransferNames = new String[ 2][];
			_spotExTransferNames[ 0] = LayerManager.get_instance().get_spot_object_names( "extransfer", false);
			_spotExTransferNames[ 1] = LayerManager.get_instance().get_spot_object_names( "extransfer", true);
		}

		return _spotExTransferNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_role_names(boolean containsEmpty) {
		if ( null == _roleNames) {
			_roleNames = new String[ 2][];
			_roleNames[ 0] = LayerManager.get_instance().get_role_names( false);
			_roleNames[ 1] = LayerManager.get_instance().get_role_names( true);
		}

		return _roleNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_agent_role_names(boolean containsEmpty) {
		if ( null == _agentRoleNames) {
			_agentRoleNames = new String[ 2][];
			_agentRoleNames[ 0] = LayerManager.get_instance().get_agent_role_names( false);
			_agentRoleNames[ 1] = LayerManager.get_instance().get_agent_role_names( true);
		}

		return _agentRoleNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public static String[] get_spot_role_names(boolean containsEmpty) {
		if ( null == _spotRoleNames) {
			_spotRoleNames = new String[ 2][];
			_spotRoleNames[ 0] = LayerManager.get_instance().get_spot_role_names( false);
			_spotRoleNames[ 1] = LayerManager.get_instance().get_spot_role_names( true);
		}

		return _spotRoleNames[ !containsEmpty ? 0 : 1];
	}

	/**
	 * @param title
	 * @param kind
	 * @param type
	 * @param color
	 * @param role
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public RulePropertyPanelBase(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super();
		_title = title;
		_kind = kind;
		_type = type;
		_color = color;
		_role = role;
		_index = index;
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @param title
	 * @param kind
	 * @param type
	 * @param role
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public RulePropertyPanelBase(String title, String kind, String type, Role role, int index, Frame owner, EditRoleFrame parent) {
		super();
		_title = title;
		_kind = kind;
		_type = type;
		_role = role;
		_index = index;
		_owner = owner;
		_parent = parent;
		_color = new Color( 0, 0, 0);
	}

	/**
	 * @param title
	 * @param kind
	 * @param color
	 * @param role
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public RulePropertyPanelBase(String title, String kind, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super();
		_title = title;
		_kind = kind;
		_color = color;
		_role = role;
		_index = index;
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @param title
	 * @param kind
	 * @param role
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public RulePropertyPanelBase(String title, String kind, Role role, int index, Frame owner, EditRoleFrame parent) {
		super();
		_title = title;
		_kind = kind;
		_role = role;
		_index = index;
		_owner = owner;
		_parent = parent;
		_color = new Color( 0, 0, 0);
	}

	/**
	 * @param rule
	 * @return
	 */
	public boolean set(Rule rule) {
		return false;
	}

//	/**
//	 * @param type
//	 * @param value
//	 * @return
//	 */
//	public boolean set(String type, String value) {
//		return false;
//	}

	/**
	 * @return
	 */
	public Rule get() {
		return null;
	}

//	/**
//	 * @return
//	 */
//	public Rule get2() {
//		return get();
//	}

//	/**
//	 * @param rule
//	 * @return
//	 */
//	public boolean get(Rule rule) {
//		return true;
//	}
//
//	/**
//	 * @param rule
//	 * @return
//	 */
//	public boolean get2(Rule rule) {
//		return get( rule);
//	}

	/**
	 * 
	 */
	public void confirm() {
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.panel.StandardPanel#on_create()
	 */
	@Override
	protected boolean on_create() {
		if ( !super.on_create())
			return false;

		link_to_ok( this);
		link_to_cancel( this);

		return true;
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
	}

	/**
	 * @param containsEmpty
	 * @param right
	 * @param panel
	 * @return
	 */
	public ObjectSelector create_agent_selector(boolean containsEmpty, boolean right, JPanel panel) {
		return create_agent_selector( containsEmpty, _color, right, panel);
	}

	/**
	 * @param containsEmpty
	 * @param color
	 * @param right
	 * @param panel
	 * @return
	 */
	public ObjectSelector create_agent_selector(boolean containsEmpty, Color color, boolean right, JPanel panel) {
		return create_objectSelector( "agent", get_agent_names( containsEmpty), color, right, panel);
	}

	/**
	 * @param containsEmpty
	 * @param width
	 * @param right
	 * @param panel
	 * @return
	 */
	public ObjectSelector create_agent_selector(boolean containsEmpty, int width, boolean right, JPanel panel) {
		return create_agent_selector( containsEmpty, width, _color, right, panel);
	}

	/**
	 * @param containsEmpty
	 * @param width
	 * @param color
	 * @param right
	 * @param panel
	 * @return
	 */
	public ObjectSelector create_agent_selector(boolean containsEmpty, int width, Color color, boolean right, JPanel panel) {
		return create_objectSelector( "agent", get_agent_names( containsEmpty), width, color, right, panel);
	}

	/**
	 * @param containsEmpty
	 * @param right
	 * @param panel
	 * @return
	 */
	public ObjectSelector create_spot_selector(boolean containsEmpty, boolean right, JPanel panel) {
		return create_spot_selector( containsEmpty, _color, right, panel);
	}

	/**
	 * @param containsEmpty
	 * @param color
	 * @param right
	 * @param panel
	 * @return
	 */
	public ObjectSelector create_spot_selector(boolean containsEmpty, Color color, boolean right, JPanel panel) {
		return create_objectSelector( "spot", get_spot_names( containsEmpty), color, right, panel);
	}

	/**
	 * @param containsEmpty
	 * @param width
	 * @param right
	 * @param panel
	 * @return
	 */
	public ObjectSelector create_spot_selector(boolean containsEmpty, int width, boolean right, JPanel panel) {
		return create_spot_selector( containsEmpty, width, _color, right, panel);
	}

	/**
	 * @param containsEmpty
	 * @param width
	 * @param color
	 * @param right
	 * @param panel
	 * @return
	 */
	public ObjectSelector create_spot_selector(boolean containsEmpty, int width, Color color, boolean right, JPanel panel) {
		return create_objectSelector( "spot", get_spot_names( containsEmpty), width, color, right, panel);
	}

	/**
	 * @param kind
	 * @param names
	 * @param color
	 * @param right
	 * @param panel
	 * @return
	 */
	public ObjectSelector create_objectSelector(String kind, String[] names, Color color, boolean right, JPanel panel) {
		return create_objectSelector( kind, names, _standardSpotNameWidth, color, right, panel);
	}

	/**
	 * @param kind
	 * @param names
	 * @param width
	 * @param color
	 * @param right
	 * @param panel
	 * @return
	 */
	public ObjectSelector create_objectSelector(String kind, String[] names, int width, Color color, boolean right, JPanel panel) {
		ObjectSelector objectSelector = new ObjectSelector( kind, names, width, _standardNumberSpinnerWidth, color, right/*, this*/, new FlowLayout( FlowLayout.LEFT, 0, 0), this);
		panel.add( objectSelector);
		return objectSelector;
	}

	/**
	 * @param objectSelector
	 * @param spotVariableCheckBox
	 * @param spotVariableComboBox
	 */
	protected void on_update(ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		on_update( true, objectSelector, spotVariableCheckBox, spotVariableComboBox);
	}

	/**
	 * @param spot
	 * @param objectSelector
	 * @param spotVariableCheckBox
	 * @param spotVariableComboBox
	 */
	protected void on_update(boolean spot, ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !spot)
			reset( objectSelector, spotVariableCheckBox, spotVariableComboBox);
		else
			update( objectSelector.get_name(), objectSelector.get_number(), objectSelector, spotVariableCheckBox, spotVariableComboBox);
	}

	/**
	 * @param objectSelector
	 * @param spotVariableCheckBox
	 * @param spotVariableComboBox
	 */
	protected void reset(ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		// TODO Auto-generated method stub
		//CommonTool.update( spotVariableComboBox, get_agent_spot_variable_names( false));
		update( spotVariableCheckBox, spotVariableComboBox);
	}

//	/**
//	 * @param objectSelector
//	 * @param spotCheckBox
//	 * @param spotVariableCheckBox
//	 * @param spotVariableComboBox
//	 */
//	protected void reset(ObjectSelector objectSelector, CheckBox spotCheckBox, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
//		// TODO Auto-generated method stub
//		spotCheckBox.setSelected( false);
//		objectSelector.setEnabled( false);
//		reset( objectSelector, spotVariableCheckBox, spotVariableComboBox);
//	}

	/**
	 * @param name
	 * @param number
	 * @param objectSelector
	 * @param selected
	 * @param spotVariableComboBox
	 */
	private void update(String name, String number, ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( null == name || name.equals( ""))
			update( objectSelector, spotVariableCheckBox, spotVariableComboBox);
		else {
			SpotObject spotObject = LayerManager.get_instance().get_spot( name);
			if ( null == spotObject)
				return;

			update( spotObject, number, objectSelector, spotVariableCheckBox, spotVariableComboBox);
		}
	}

	/**
	 * @param objectSelector
	 * @param spotVariableCheckBox
	 * @param spotVariableComboBox
	 */
	protected void update(ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		CommonTool.update( spotVariableComboBox, get_spot_spot_variable_names( false));
		update( spotVariableCheckBox, spotVariableComboBox);
	}

	/**
	 * @param spotObject
	 * @param number
	 * @param objectSelector
	 * @param spotVariableCheckBox
	 * @param spotVariableComboBox
	 */
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		CommonTool.update( spotVariableComboBox, spotObject.get_object_names( "spot variable", number, false));
		update( spotVariableCheckBox, spotVariableComboBox);
	}

	/**
	 * @param spotVariableCheckBox
	 * @param spotVariableComboBox
	 */
	private void update(CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		spotVariableCheckBox.setEnabled( 0 < spotVariableComboBox.getItemCount());
		spotVariableCheckBox.setSelected( spotVariableCheckBox.isSelected() && 0 < spotVariableComboBox.getItemCount());
		spotVariableComboBox.setEnabled( spotVariableCheckBox.isSelected() && 0 < spotVariableComboBox.getItemCount());
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.selector.IObjectSelectorHandler#changed(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector)
	 */
	@Override
	public void changed(String name, String number, String fullName, ObjectSelector objectSelector) {
		update( name, number, objectSelector);
	}

	/**
	 * @param name
	 * @param number
	 * @param objectSelector
	 */
	protected void update(String name, String number, ObjectSelector objectSelector) {
		if ( null == name || name.equals( ""))
			update( objectSelector);
		else {
			SpotObject spotObject = LayerManager.get_instance().get_spot( name);
			if ( null == spotObject)
				return;

			update( spotObject, number, objectSelector);
		}
	}

	/**
	 * @param objectSelector
	 */
	protected void update(ObjectSelector objectSelector) {
	}

	/**
	 * @param spotObject
	 * @param number
	 * @param objectSelector
	 */
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector) {
	}

	/**
	 * @param fullName
	 * @param spotVariable
	 * @param objectSelector
	 * @param spotVariableCheckBox
	 * @param spotVariableComboBox
	 * @return
	 */
	protected boolean set(String fullName, String spotVariable, ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		return set( fullName, spotVariable, null, objectSelector, spotVariableCheckBox, spotVariableComboBox);
	}

	/**
	 * @param fullName
	 * @param spotVariable
	 * @param spotCheckBox
	 * @param objectSelector
	 * @param spotVariableCheckBox
	 * @param spotVariableComboBox
	 * @return
	 */
	protected boolean set(String fullName, String spotVariable, CheckBox spotCheckBox, ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		objectSelector.set( fullName);

		if ( null == fullName) {
			reset( objectSelector, spotVariableCheckBox, spotVariableComboBox);

			set( spotCheckBox, objectSelector, false);

			spotVariableCheckBox.setSelected( !spotVariable.equals( ""));

			if ( !spotVariable.equals( "")
				&& LayerManager.get_instance().is_agent_object_name( "spot variable", spotVariable))
				spotVariableComboBox.setSelectedItem( spotVariable);

//			set( spotCheckBox, objectSelector, false);
		} else if ( fullName.equals( "")) {
			update( objectSelector, spotVariableCheckBox, spotVariableComboBox);

			set( spotCheckBox, objectSelector, true);

			spotVariableCheckBox.setSelected( !spotVariable.equals( ""));

			if ( !spotVariable.equals( "")
				&& LayerManager.get_instance().is_spot_object_name( "spot variable", spotVariable))
				spotVariableComboBox.setSelectedItem( spotVariable);

//			set( spotCheckBox, objectSelector, true);
		} else {
			SpotObject spotObject = LayerManager.get_instance().get_spot_has_this_name( fullName);
			if ( null == spotObject)
				return false;

			update( spotObject, fullName.substring( spotObject._name.length()), objectSelector, spotVariableCheckBox, spotVariableComboBox);

			set( spotCheckBox, objectSelector, true);

			spotVariableCheckBox.setSelected( !spotVariable.equals( ""));

			if ( !spotVariable.equals( "")
				&& LayerManager.get_instance().is_spot_object_name( "spot variable", fullName, spotVariable))
				spotVariableComboBox.setSelectedItem( spotVariable);

//			set( spotCheckBox, objectSelector, true);
		}

		return true;
	}

	/**
	 * @param spotCheckBox
	 * @param objectSelector
	 * @param enable
	 */
	private void set(CheckBox spotCheckBox, ObjectSelector objectSelector, boolean enable) {
		if ( null != spotCheckBox)
			spotCheckBox.setSelected( enable);

		objectSelector.setEnabled( enable);
	}

	/**
	 * @param index
	 * @param objectSelector
	 * @return
	 */
	protected boolean update(int index, ObjectSelector objectSelector) {
		if ( objectSelector.is_empty())
			return false;

		if ( 0 > index || objectSelector._objectNameComboBox.getItemCount() <= index)
			return false;

		objectSelector._objectNameComboBox.setSelectedIndex( index);
		String fullName = objectSelector.get();
		if ( null == fullName || fullName.equals( ""))
			update( objectSelector);
		else {
			SpotObject spotObject = LayerManager.get_instance().get_spot_has_this_name( fullName);
			if ( null == spotObject)
				return false;

			update( spotObject, fullName.substring( spotObject._name.length()), objectSelector);
		}

		return true;
	}

//	/** obsolete method
//	 * @param objectSelector
//	 * @param enable
//	 */
//	protected void on_update(ObjectSelector objectSelector, boolean enable) {
//		if ( !enable)
//			reset( objectSelector);
//		else
//			update( objectSelector.get_name(), objectSelector.get_number(), objectSelector);
//	}
//
//	/** obsolete method
//	 * @param fullName
//	 * @param objectSelector
//	 * @param checkBox
//	 * @return
//	 */
//	protected boolean update(String fullName, ObjectSelector objectSelector, CheckBox checkBox) {
//		objectSelector.set( fullName);
//
//		if ( null == fullName || fullName.equals( ""))
//			update( objectSelector);
//		else {
//			//objectSelector.set( fullName);
//			SpotObject spotObject = LayerManager.get_instance().get_spot_has_this_name( fullName);
//			if ( null == spotObject)
//				return false;
//
//			update( spotObject, fullName.substring( spotObject._name.length()), objectSelector);
//		}
//
//		checkBox.setSelected( true);
//		objectSelector.setEnabled( true);
//
//		return true;
//	}
//
//	/** obsolete method
//	 * @param fullName
//	 * @param objectSelector
//	 * @return
//	 */
//	protected boolean update(String fullName, ObjectSelector objectSelector) {
//		objectSelector.set( fullName);
//
//		if ( null == fullName || fullName.equals( ""))
//			update( objectSelector);
//		else {
//			//objectSelector.set( fullName);
//			SpotObject spotObject = LayerManager.get_instance().get_spot_has_this_name( fullName);
//			if ( null == spotObject)
//				return false;
//
//			update( spotObject, fullName.substring( spotObject._name.length()), objectSelector);
//		}
//
//		objectSelector.setEnabled( true);
//
//		return true;
//	}
//
//	/** obsolete method
//	 * @param objectSelector
//	 */
//	protected void reset(ObjectSelector objectSelector) {
//	}
//
//	/** obsolete method
//	 * @param objectSelector
//	 * @param checkBox
//	 */
//	protected void reset(ObjectSelector objectSelector, CheckBox checkBox) {
//		checkBox.setSelected( false);
//		objectSelector.setEnabled( false);
//		reset( objectSelector);
//	}

	/**
	 * @param items
	 * @param right
	 * @return
	 */
	public ComboBox create_comboBox(Object[] items, boolean right) {
		return create_comboBox( items, _color, right);
	}

	/**
	 * @param items
	 * @param color
	 * @param right
	 * @return
	 */
	public ComboBox create_comboBox(Object[] items, Color color, boolean right) {
		return ComboBox.create( items, _standardControlWidth, color, right, new CommonComboBoxRenderer( color, right), this);
	}

	/**
	 * @param items
	 * @param width
	 * @param right
	 * @return
	 */
	public ComboBox create_comboBox(Object[] items, int width, boolean right) {
		return create_comboBox( items, width, _color, right);
	}

	/**
	 * @param items
	 * @param width
	 * @param color
	 * @param right
	 * @return
	 */
	public ComboBox create_comboBox(Object[] items, int width, Color color, boolean right) {
		return ComboBox.create( items, width, color, right, new CommonComboBoxRenderer( color, right), this);
	}

	/**
	 * @param right
	 * @return
	 */
	public TextField create_textField(boolean right) {
		return create_textField( _standardControlWidth, right);
	}

	/**
	 * @param width
	 * @param right
	 * @return
	 */
	public TextField create_textField(int width, boolean right) {
		return create_textField( width, _color, right);
	}

	/**
	 * @param width
	 * @param color
	 * @param right
	 * @return
	 */
	public TextField create_textField(int width, Color color, boolean right) {
		return TextField.create( width, color, right, this);
	}

	/**
	 * @param textLimiter
	 * @param right
	 * @return
	 */
	public TextField create_textField(TextLimiter textLimiter, boolean right) {
		return create_textField( textLimiter, _standardControlWidth, right);
	}

	/**
	 * @param textLimiter
	 * @param width
	 * @param right
	 * @return
	 */
	public TextField create_textField(TextLimiter textLimiter, int width, boolean right) {
		return create_textField( textLimiter, width, _color, right);
	}

	/**
	 * @param textLimiter
	 * @param width
	 * @param color
	 * @param right
	 * @return
	 */
	public TextField create_textField(TextLimiter textLimiter, int width, Color color, boolean right) {
		return TextField.create( textLimiter, width, color, right, this);
	}

	/**
	 * @param textLimiter
	 * @param text
	 * @param width
	 * @param right
	 * @return
	 */
	public TextField create_textField(TextLimiter textLimiter, String text, int width, boolean right) {
		return create_textField( textLimiter, text, width, _color, right);
	}

	/**
	 * @param textLimiter
	 * @param text
	 * @param width
	 * @param color
	 * @param right
	 * @return
	 */
	public TextField create_textField(TextLimiter textLimiter, String text, int width, Color color, boolean right) {
		return TextField.create( textLimiter, text, width, color, right, this);
	}

	/**
	 * @param textExcluder
	 * @param right
	 * @return
	 */
	public TextField create_textField(TextExcluder textExcluder, boolean right) {
		return create_textField( textExcluder, _standardControlWidth, right);
	}

	/**
	 * @param textExcluder
	 * @param width
	 * @param right
	 * @return
	 */
	public TextField create_textField(TextExcluder textExcluder, int width, boolean right) {
		return create_textField( textExcluder, width, _color, right);
	}

	/**
	 * @param textExcluder
	 * @param width
	 * @param color
	 * @param right
	 * @return
	 */
	public TextField create_textField(TextExcluder textExcluder, int width, Color color, boolean right) {
		return TextField.create( textExcluder, width, color, right, this);
	}

	/**
	 * @param textExcluder
	 * @param text
	 * @param width
	 * @param right
	 * @return
	 */
	public TextField create_textField(TextExcluder textExcluder, String text, int width, boolean right) {
		return create_textField( textExcluder, text, width, _color, right);
	}

	/**
	 * @param textExcluder
	 * @param text
	 * @param width
	 * @param color
	 * @param right
	 * @return
	 */
	public TextField create_textField(TextExcluder textExcluder, String text, int width, Color color, boolean right) {
		return TextField.create( textExcluder, text, width, color, right, this);
	}

	/**
	 * @param name
	 * @param buttonGroup
	 * @param synchronize
	 * @param right
	 * @return
	 */
	public RadioButton create_radioButton(String name, ButtonGroup buttonGroup, boolean synchronize, boolean right) {
		return create_radioButton( name, _color, buttonGroup, synchronize, right);
	}

	/**
	 * @param name
	 * @param color
	 * @param buttonGroup
	 * @return
	 */
	public RadioButton create_radioButton(String name, Color color, ButtonGroup buttonGroup, boolean synchronize, boolean right) {
		RadioButton radioButton = ( ( null == name)
			? ( new RadioButton( synchronize, right))
			: ( new RadioButton( name, synchronize, right)));

		radioButton.setForeground( color);

		link_to_ok( radioButton._radioButton);
		link_to_ok( radioButton._label);

		link_to_cancel( radioButton._radioButton);
		link_to_cancel( radioButton._label);

		buttonGroup.add( radioButton._radioButton);

		return radioButton;
	}

	/**
	 * @param text
	 * @param synchronize
	 * @param right
	 * @return
	 */
	public CheckBox create_checkBox(String text, boolean synchronize, boolean right) {
		return create_checkBox( text, _color, synchronize, right);
	}

	/**
	 * @param text
	 * @param color
	 * @param synchronize
	 * @param right
	 * @return
	 */
	public CheckBox create_checkBox(String text, Color color, boolean synchronize, boolean right) {
		CheckBox checkBox = new CheckBox( text, synchronize, right);

		checkBox.setForeground( color);

		link_to_ok( checkBox._checkBox);
		link_to_ok( checkBox._label);

		link_to_cancel( checkBox._checkBox);
		link_to_cancel( checkBox._label);

		return checkBox;
	}

	/**
	 * @param text
	 * @param right
	 * @return
	 */
	public JLabel create_label(String text, boolean right) {
		return create_label( text, _color, right);
	}

	/**
	 * @param text
	 * @param color
	 * @param right
	 * @return
	 */
	public JLabel create_label(String text, Color color, boolean right) {
		JLabel label = ( right ? ( new JLabel( text, SwingConstants.RIGHT)) : ( new JLabel( text)));
		label.setForeground( color);
		return label;
	}

	/**
	 * @param text
	 * @return
	 */
	public JButton create_button(String text) {
		return create_button( text, _color);
	}

	/**
	 * @param text
	 * @param color
	 * @return
	 */
	public JButton create_button(String text, Color color) {
		JButton button = new JButton( text);
		button.setForeground( color);
		return button;
	}

	/**
	 * 
	 */
	protected void setup_apply_button() {
		// TODO とりあえず
		//setup_apply_button( "South");
		setup_apply_button( "West");
		//setup_apply_button( "East");
	}

	/**
	 * @param constraints
	 */
	protected void setup_apply_button(Object constraints) {
		JButton button = new JButton( ResourceManager.get_instance().get( "dialog.apply"));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_ok( arg0);
			}
		});

		add( button, constraints);
	}

//	/**
//	 * 
//	 */
//	protected void setup_apply_button() {
//		JPanel southPanel = new JPanel();
//		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));
//
//		insert_horizontal_glue( southPanel);
//
//		JPanel panel = new JPanel();
//		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));
//		setup_ok_button(
//			panel,
//			ResourceManager.get_instance().get( "dialog.apply"),
//			false, false, _apply_button_width);
//		southPanel.add( panel);
//
//		insert_horizontal_glue( southPanel);
//
//		add( southPanel, "East");
//	}

//	/**
//	 * @param comboBox0
//	 * @param comboBox1
//	 * @param elements
//	 * @return
//	 */
//	protected boolean set1(ComboBox comboBox0, ComboBox comboBox1, String[] elements) {
//		if ( 2 != elements.length)
//			return false;
//
//		set1( comboBox0, comboBox1, elements[ 0], elements[ 1]);
//		return true;
//	}

	/**
	 * @param comboBox0
	 * @param comboBox1
	 * @param element0
	 * @param element1
	 */
	protected void set1(ComboBox comboBox0, ComboBox comboBox1, String element0, String element1) {
		comboBox0.setSelectedItem( element0);
		comboBox1.setSelectedItem( element1);
	}

	/**
	 * @param comboBox
	 * @param ObjectSelector
	 * @param element0
	 * @param element1
	 * @return
	 */
	protected void set2(ComboBox comboBox, ObjectSelector objectSelector, String element0, String element1) {
		comboBox.setSelectedItem( element0);
		objectSelector.set( element1);
	}

	/**
	 * @param comboBox0
	 * @param textField
	 * @param element0
	 * @param element1
	 */
	protected void set3(ComboBox comboBox0, TextField textField, String element0, String element1) {
		comboBox0.setSelectedItem( element0);
		textField.setText( element1);
	}

	/**
	 * @param comboBox
	 * @param textField1
	 * @param textField2
	 * @param element0
	 * @param element1
	 * @param element2
	 * @return
	 */
	protected boolean set4(ComboBox comboBox, TextField textField1, TextField textField2, String element0, String element1, String element2) {
		comboBox.setSelectedItem( element0);
		textField1.setText( element1);
		textField2.setText( element2);
		return true;
	}

	/**
	 * @param objectSelector
	 * @param spotVariableCheckBox
	 * @param spotVariableComboBox
	 * @return
	 */
	protected String get(ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		return get( true, objectSelector, spotVariableCheckBox, spotVariableComboBox);
	}

	/**
	 * @param spotCheckBox
	 * @param objectSelector
	 * @param spotVariableCheckBox
	 * @param spotVariableComboBox
	 * @return
	 */
	protected String get(CheckBox spotCheckBox, ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		return get( spotCheckBox.isSelected(), objectSelector, spotVariableCheckBox, spotVariableComboBox);
	}

	/**
	 * @param selected
	 * @param objectSelector
	 * @param spotVariableCheckBox
	 * @param spotVariableComboBox
	 * @return
	 */
	private String get(boolean selected, ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !selected)
			return ( !spotVariableCheckBox.isSelected() ? "" : ( "<" + ( String)spotVariableComboBox.getSelectedItem() + ">"));
		else
			return ( "<" + objectSelector.get() + ( !spotVariableCheckBox.isSelected() ? "" : ( ":" + ( String)spotVariableComboBox.getSelectedItem())) + ">"); 
	}

	/**
	 * @param comboBox
	 * @return
	 */
	protected String get1(ComboBox comboBox) {
		String value = ( String)comboBox.getSelectedItem();
		if ( null == value || value.equals( ""))
			return null;

		return value;
	}

	/**
	 * @param field
	 * @param containsEmpty
	 * @return
	 */
	protected String get1(TextField textField, boolean containsEmpty) {
		String value = textField.getText();
		if ( null == value || value.equals( "")) {
			if ( containsEmpty)
				value = "";
			else
				return null;
		}

		return value;
	}

	/**
	 * @param comboBox0
	 * @param comboBox1
	 * @return
	 */
	protected String get2(ComboBox comboBox0, ComboBox comboBox1) {
		String text0 = ( String)comboBox0.getSelectedItem();
		if ( null == text0 || text0.equals( ""))
			return null;

		String text1 = ( String)comboBox1.getSelectedItem();
		if ( null == text1 || text1.equals( ""))
			return null;

		return ( text0 + "=" + text1);
	}

	/**
	 * @param comboBox0
	 * @param comboBox1
	 * @param containsEmpty
	 * @return
	 */
	protected String get2(ComboBox comboBox0, ComboBox comboBox1, boolean containsEmpty) {
		String text0 = ( String)comboBox0.getSelectedItem();
		if ( null == text0 || text0.equals( ""))
			return null;

		String text1 = ( String)comboBox1.getSelectedItem();
		if ( !containsEmpty && ( null == text1 || text1.equals( "")))
			return null;

		if ( !containsEmpty)
			return ( text0 + "=" + text1);
		else
			return ( text0 + ( ( null == text1 || text1.equals( "")) ? "" : "=" + text1));
	}

	/**
	 * @param comboBox0
	 * @param comboBox1
	 * @param containsEmpty
	 * @param prefix
	 * @return
	 */
	protected String get2(ComboBox comboBox0, ComboBox comboBox1, boolean containsEmpty, String prefix) {
		String text0 = ( String)comboBox0.getSelectedItem();
		if ( null == text0 || text0.equals( ""))
			return null;

		String text1 = ( String)comboBox1.getSelectedItem();
		if ( !containsEmpty && ( null == text1 || text1.equals( "")))
			return null;

		if ( !containsEmpty)
			return ( text0 + "=" + prefix + text1);
		else
			return ( text0 + ( ( null == text1 || text1.equals( "")) ? "" : "=" + prefix + text1));
	}

	/**
	 * @param spot0
	 * @param comboBox0
	 * @param spot1
	 * @param comboBox1
	 * @return
	 */
	protected String get2(String spot0, ComboBox comboBox0, String spot1, ComboBox comboBox1) {
		String text0 = ( String)comboBox0.getSelectedItem();
		if ( null == text0 || text0.equals( ""))
			return null;

		String text1 = ( String)comboBox1.getSelectedItem();
		if ( null == text1 || text1.equals( ""))
			return null;

		return ( spot0 + text0 + "=" + spot1 + text1);
	}

	/**
	 * @param comboBox
	 * @param objectSelector
	 * @return
	 */
	protected String get3(ComboBox comboBox, ObjectSelector objectSelector) {
		String text0 = ( String)comboBox.getSelectedItem();
		if ( null == text0 || text0.equals( ""))
			return null;

		String text1 = objectSelector.get();
		return ( text0 	+ ( ( null == text1 || text1.equals( "")) ? "" : ( "=" + text1)));
	}

	/**
	 * @param comboBox
	 * @param textField
	 * @param containsEmpty
	 * @return
	 */
	protected String get4(ComboBox comboBox, TextField textField, boolean containsEmpty) {
		String text0 = ( String)comboBox.getSelectedItem();
		if ( null == text0 || text0.equals( ""))
			return null;

		String text1 = textField.getText();
		if ( null == text1 || text1.equals( "")) {
			if ( containsEmpty)
				text1 = "";
			else
				return null;
		}

		return ( text0 + "=" + text1);
	}

	/**
	 * @param comboBox
	 * @param textField
	 * @return
	 */
	protected String get_keyword(ComboBox comboBox, TextField textField) {
		// TODO
		String text0 = ( String)comboBox.getSelectedItem();
		if ( null == text0 || text0.equals( ""))
			return null;

		String text1 = textField.getText();
		if ( null == text1)
				text1 = "";

		return ( text0 + "=\"" + text1 + "\"");
	}

	/**
	 * @param textField
	 * @param comboBox
	 * @param containsEmpty
	 * @return
	 */
	protected String get4(TextField textField, ComboBox comboBox, boolean containsEmpty) {
		String text0 = textField.getText();
		if ( null == text0 || text0.equals( "")) {
			if ( containsEmpty)
				text0 = "";
			else
				return null;
		}

		String text1 = ( String)comboBox.getSelectedItem();
		if ( null == text1 || text1.equals( ""))
			return null;

		return ( text0 + "=" + text1);
	}

	/**
	 * @param comboBox
	 * @param textField1
	 * @param textField2
	 * @return
	 */
	protected String get5(ComboBox comboBox, TextField textField1, TextField textField2) {
		String text0 = ( String)comboBox.getSelectedItem();
		if ( null == text0 || text0.equals( ""))
			return null;

		String text1 = textField1.getText();
		if ( null == text1 || text1.equals( ""))
			return null;

		String text2 = textField2.getText();
		if ( null == text2 || text2.equals( ""))
			return null;

		return ( text0 + "=" + text1 + "=" + text2);
	}

	/**
	 * @param rulePropertyPanelBase
	 */
	public void update(RulePropertyPanelBase rulePropertyPanelBase) {
	}

	/**
	 * @param spot
	 * @param name
	 * @return
	 */
	static public boolean is_object(String spot, String name) {
		return is_object( spot, name, Constant._kinds);
	}

	/**
	 * @param spot
	 * @param name
	 * @param kinds
	 * @return
	 */
	static public boolean is_object(String spot, String name, String[] kinds) {
		for ( String kind:kinds) {
			if ( CommonRuleManipulator.is_object( kind, spot + name))
				return true;
		}
		return false;
	}

	/**
	 * 
	 */
	public void on_update_stage() {
		// TODO 2012.9.20
	}

	/**
	 * @param newName
	 * @param originalName
	 * @return 
	 */
	public boolean update_stage_name(String newName, String originalName) {
		// TODO 2012.9.20
		return false;
	}

	/**
	 * @param newExpression
	 * @param newVariableCount
	 * @param originalExpression
	 */
	public void on_update_expression(Expression newExpression, int newVariableCount, Expression originalExpression) {
		// TODO 2014.2.14
	}

	/**
	 * 
	 */
	public void on_update_expression() {
		// TODO 2014.2.14
	}
}
