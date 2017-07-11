/**
 * 
 */
package soars.application.visualshell.object.entity.base.object.initial_data_file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.soars.exalge.util.ExalgeFactory;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.MainFrame;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.object.number.NumberObject;
import soars.application.visualshell.object.role.base.object.legacy.command.ExchangeAlgebraCommand;
import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 *
 */
public class InitialDataFileChecker {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private List<String> _commandList = null;

	/**
	 * 
	 */
	private String _name = "";

	/**
	 * 
	 */
	private File _file = null;

	/**
	 * 
	 */
	private String _state = "first";

	/**
	 * 
	 */
	private List<String> _commands = new ArrayList<String>();

	/**
	 * 
	 */
	private List<String> _variables = new ArrayList<String>();

	/**
	 * 
	 */
	// TODO Auto-generated method stub
	private boolean _exchangeAlgebra = false;

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
			if ( null != _commandList)
				return;

			_commandList = new ArrayList<String>();
			_commandList.add( "setProbVal");
			_commandList.add( "setIntVal");
			_commandList.add( "setDoubleVal");
			_commandList.add( "setKeyword");
			_commandList.add( "setRoleVal");
			_commandList.add( "setTimeVal");
			_commandList.add( "setSpotVal");

			_commandList.add( "addAgent");
			_commandList.add( "addSpot");
			_commandList.add( "addKeyword");
			_commandList.add( "addIntVal");
			_commandList.add( "addDoubleVal");
			_commandList.add( "addProbVal");
			_commandList.add( "addRoleVal");
			_commandList.add( "addTimeVal");
			_commandList.add( "addSpotVal");
			_commandList.add( "addMapVal");
			_commandList.add( "addColVal");
			_commandList.add( "addListVal");

			_commandList.add( "addFirstAgent");
			_commandList.add( "addFirstSpot");
			_commandList.add( "addFirstKeyword");
			_commandList.add( "addFirstIntVal");
			_commandList.add( "addFirstDoubleVal");
			_commandList.add( "addFirstProbVal");
			_commandList.add( "addFirstRoleVal");
			_commandList.add( "addFirstTimeVal");
			_commandList.add( "addFirstSpotVal");
			_commandList.add( "addFirstMapVal");
			_commandList.add( "addFirstColVal");
			_commandList.add( "addFirstListVal");

			_commandList.add( "addLastAgent");
			_commandList.add( "addLastSpot");
			_commandList.add( "addLastKeyword");
			_commandList.add( "addLastIntVal");
			_commandList.add( "addLastDoubleVal");
			_commandList.add( "addLastProbVal");
			_commandList.add( "addLastRoleVal");
			_commandList.add( "addLastTimeVal");
			_commandList.add( "addLastSpotVal");
			_commandList.add( "addLastMapVal");
			_commandList.add( "addLastColVal");
			_commandList.add( "addLastListVal");

			_commandList.add( "addExalge");
		}
	}

	/**
	 * @param file
	 * @return
	 */
	public static boolean execute(File file) {
		InitialDataFileChecker initialDataFileChecker = new InitialDataFileChecker( file);
		return initialDataFileChecker.execute();
	}

	/**
	 * @param name
	 * @param file
	 * @return
	 */
	public static boolean execute(String name, File file) {
		InitialDataFileChecker initialDataFileChecker = new InitialDataFileChecker( name, file);
		return initialDataFileChecker.execute();
	}

	/**
	 * @param file
	 */
	public InitialDataFileChecker(File file) {
		super();
		_file = file;
	}

	/**
	 * @param name
	 * @param file
	 */
	public InitialDataFileChecker(String name, File file) {
		super();
		_name = name;
		_file = file;
	}

	/**
	 * @return
	 */
	private boolean execute() {
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader( new InputStreamReader( new FileInputStream( _file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		int row = 1;
		while ( true) {
			String line = null;
			try {
				line = bufferedReader.readLine();
			} catch (IOException e1) {
				try {
					bufferedReader.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				e1.printStackTrace();
				return false;
			}

			if ( null == line)
				break;

			if ( 0 == line.length()) {
				++row;
				continue;
			}

			if ( !read( line, row)) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}
		}

		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * @param line
	 * @param row
	 * @return
	 */
	private boolean read(String line, int row) {
		String[] words = Tool.split( line, '\t');
		if ( null == words) {
			on_error( row);
			return false;
		}

		if ( _state.equals( "first")) {
			if ( !read_command( words, row))
				return false;
		} else if ( _state.equals( "second")) {
			if ( !read_variable( words, row))
				return false;
		} else if ( _state.equals( "third")) {
			if ( !read_value( words, row))
				return false;
		} else {
			on_error( row);
			return false;
		}

		return true;
	}

	/**
	 * @param words
	 * @param row
	 * @return
	 */
	private boolean read_command(String[] words, int row) {
		if ( 2 > words.length) {
			on_error( row);
			return false;
		}

		for ( int i = 1; i < words.length; ++i) {
			if ( !_commandList.contains( words[ i])) {
				on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.command.error.message"), "", "", "", "");
				return false;
			}

			_commands.add( words[ i]);
		}

		// TODO Auto-generated method stub
		int counter = 0;
		for ( String command:_commands) {
			if ( command.equals( "addExalge"))
				++counter;
		}

		if ( _commands.size() == counter)
			_exchangeAlgebra = true;
		else if ( 0 < counter)
			return false;

		_state = "second";

		return true;
	}

	/**
	 * @param words
	 * @param row
	 * @return
	 */
	private boolean read_variable(String[] words, int row) {
		if ( _commands.size() != words.length - 1) {
			on_error( row);
			return false;
		}

		for ( int i = 1; i < words.length; ++i)
			_variables.add( words[ i]);

		_state = "third";

		return true;
	}

	/**
	 * @param words
	 * @param row
	 * @return
	 */
	private boolean read_value(String[] words, int row) {
		// TODO いずれ編集時にチェックするようになれば、編集中のテーブルについてもチェックする必要がある

		// TODO Auto-generated method stub
		if ( _exchangeAlgebra)
			return read_echange_algebra_value( words, row);

		if ( _commands.size() != words.length - 1) {
			on_error( row);
			return false;
		}

		EntityBase entityBase = null;
		String entityType = "";
		String entityName = "";
		for ( int i = 0; i < words.length; ++i) {
			if ( 0 == i) {
				if ( words[ 0].equals( "")) {
					on_error( row, i + 1, words[ 0], ResourceManager.get_instance().get( "initial.data.file.checker.agent.or.spot.error.message"), "", "", "", "");
					return false;
				}
				entityBase = LayerManager.get_instance().get_entityBase_has_this_name( words[ 0]);
				if ( null == entityBase) {
					on_error( row, i + 1, words[ 0], ResourceManager.get_instance().get( "initial.data.file.checker.agent.or.spot.error.message"), "", "", "", "");
					return false;
				}
				entityType = ( entityBase instanceof AgentObject) ? "agent" : "spot";
				entityName = words[ 0];
			} else {
				if ( words[ i].equals( ""))
					continue;

				if ( _commands.get( i - 1).equals( "setProbVal")) {
					if ( !entityBase.has_same_object_name( "probability", _variables.get( i - 1))) {
						on_error( row, i + 1, _variables.get( i - 1), ResourceManager.get_instance().get( "initial.data.file.checker.probability.error.message"), entityType, entityName, _commands.get( i - 1), "");
						return false;
					}
					if ( !is_probability_correct( words[ i])) {
						on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.value.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
						return false;
					}
				} else if ( _commands.get( i - 1).equals( "setIntVal")) {
					if ( !entityBase.has_same_object_name( "number object", _variables.get( i - 1))) {
						on_error( row, i + 1, _variables.get( i - 1), ResourceManager.get_instance().get( "initial.data.file.checker.number.object.error.message"), entityType, entityName, _commands.get( i - 1), "");
						return false;
					}
					if ( !entityBase.is_number_object_correct( _variables.get( i - 1), "integer")) {
						on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.value.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
						return false;
					}
					if ( !is_number_object_correct( words[ i], "integer")) {
						on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.value.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
						return false;
					}
				} else if ( _commands.get( i - 1).equals( "setDoubleVal")) {
					if ( !entityBase.has_same_object_name( "number object", _variables.get( i - 1))) {
						on_error( row, i + 1, _variables.get( i - 1), ResourceManager.get_instance().get( "initial.data.file.checker.number.object.error.message"), entityType, entityName, _commands.get( i - 1), "");
						return false;
					}
					if ( !entityBase.is_number_object_correct( _variables.get( i - 1), "real number")) {
						on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.value.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
						return false;
					}
					if ( !is_number_object_correct( words[ i], "real number")) {
						on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.value.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
						return false;
					}
				} else if ( _commands.get( i - 1).equals( "setKeyword")) {
					if ( !entityBase.has_same_object_name( "keyword", _variables.get( i - 1))) {
						on_error( row, i + 1, _variables.get( i - 1), ResourceManager.get_instance().get( "initial.data.file.checker.keyword.error.message"), entityType, entityName, _commands.get( i - 1), "");
						return false;
					}
					if ( !is_keyword_correct( words[ i])) {
						on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.value.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
						return false;
					}
				} else if ( _commands.get( i - 1).equals( "setRoleVal")) {
					if ( !entityBase.has_same_object_name( "role variable", _variables.get( i - 1))) {
						on_error( row, i + 1, _variables.get( i - 1), ResourceManager.get_instance().get( "initial.data.file.checker.role.variable.error.message"), entityType, entityName, _commands.get( i - 1), "");
						return false;
					}
					if ( !is_role_variable_correct( words[ i])) {
						on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.value.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
						return false;
					}
				} else if ( _commands.get( i - 1).equals( "setTimeVal")) {
					if ( !entityBase.has_same_object_name( "time variable", _variables.get( i - 1))) {
						on_error( row, i + 1, _variables.get( i - 1), ResourceManager.get_instance().get( "initial.data.file.checker.time.variable.error.message"), entityType, entityName, _commands.get( i - 1), "");
						return false;
					}
					if ( !is_time_variable_correct( words[ i])) {
						on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.value.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
						return false;
					}
				} else if ( _commands.get( i - 1).equals( "setSpotVal")) {
					if ( !entityBase.has_same_object_name( "spot variable", _variables.get( i - 1))) {
						on_error( row, i + 1, _variables.get( i - 1), ResourceManager.get_instance().get( "initial.data.file.checker.spot.variable.error.message"), entityType, entityName, _commands.get( i - 1), "");
						return false;
					}
					if ( !is_spot_variable_correct( words[ i])) {
						on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.value.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
						return false;
					}
				} else {
					if ( !entityBase.has_same_object_name( "collection", _variables.get( i - 1))
						&& !entityBase.has_same_object_name( "list", _variables.get( i - 1))) {
						on_error( 1, i + 1, _variables.get( i - 1), ResourceManager.get_instance().get( "initial.data.file.checker.collection.or.list.error.message"), entityType, entityName, _commands.get( i - 1), "");
						return false;
					}
					if ( !entityBase.has_same_object_name( "list", _variables.get( i - 1))
						&& ( _commands.get( i - 1).equals( "addFirstAgent") || _commands.get( i - 1).equals( "addLastAgent")
						|| _commands.get( i - 1).equals( "addFirstSpot") || _commands.get( i - 1).equals( "addLastSpot")
						|| _commands.get( i - 1).equals( "addFirstKeyword") || _commands.get( i - 1).equals( "addLastKeyword")
						|| _commands.get( i - 1).equals( "addFirstIntVal") || _commands.get( i - 1).equals( "addLastIntVal")
						|| _commands.get( i - 1).equals( "addFirstDoubleVal") || _commands.get( i - 1).equals( "addLastDoubleVal")
						|| _commands.get( i - 1).equals( "addFirstProbVal") || _commands.get( i - 1).equals( "addLastProbVal")
						|| _commands.get( i - 1).equals( "addFirstRoleVal") || _commands.get( i - 1).equals( "addLastRoleVal")
						|| _commands.get( i - 1).equals( "addFirstTimeVal") || _commands.get( i - 1).equals( "addLastTimeVal")
						|| _commands.get( i - 1).equals( "addFirstSpotVal") || _commands.get( i - 1).equals( "addLastSpotVal")
						|| _commands.get( i - 1).equals( "addFirstMapVal") || _commands.get( i - 1).equals( "addLastMapVal")
						|| _commands.get( i - 1).equals( "addFirstColVal") || _commands.get( i - 1).equals( "addLastColVal")
						|| _commands.get( i - 1).equals( "addFirstListVal") || _commands.get( i - 1).equals( "addLastListVal"))) {
						on_error( 1, i + 1, _variables.get( i - 1), ResourceManager.get_instance().get( "initial.data.file.checker.command.error.message"), entityType, entityName, _commands.get( i - 1), "");
						return false;
					}
					if ( _commands.get( i - 1).equals( "addAgent") || _commands.get( i - 1).equals( "addFirstAgent") || _commands.get( i - 1).equals( "addLastAgent")) {
						if ( null == LayerManager.get_instance().get_agent_has_this_name( words[ i])) {
							on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.agent.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
							return false;
						}
					} else if ( _commands.get( i - 1).equals( "addSpot") || _commands.get( i - 1).equals( "addFirstSpot") || _commands.get( i - 1).equals( "addLastSpot")) {
						if ( null == LayerManager.get_instance().get_spot_has_this_name( words[ i])) {
							on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.spot.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
							return false;
						}
					} else if ( _commands.get( i - 1).equals( "addKeyword") || _commands.get( i - 1).equals( "addFirstKeyword") || _commands.get( i - 1).equals( "addLastKeyword")) {
						if ( !entityBase.has_same_object_name( "keyword", words[ i])) {
							on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.keyword.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
							return false;
						}
					} else if ( _commands.get( i - 1).equals( "addIntVal") || _commands.get( i - 1).equals( "addFirstIntVal") || _commands.get( i - 1).equals( "addLastIntVal")) {
						if ( !entityBase.has_same_object_name( "number object", words[ i])) {
							on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.number.object.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
							return false;
						}
						if ( !entityBase.is_number_object_correct( words[ i], "integer")) {
							on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.value.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
							return false;
						}
					} else if ( _commands.get( i - 1).equals( "addDoubleVal") || _commands.get( i - 1).equals( "addFirstDoubleVal") || _commands.get( i - 1).equals( "addLastDoubleVal")) {
						if ( !entityBase.has_same_object_name( "number object", words[ i])) {
							on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.number.object.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
							return false;
						}
						if ( !entityBase.is_number_object_correct( words[ i], "real number")) {
							on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.value.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
							return false;
						}
					} else if ( _commands.get( i - 1).equals( "addProbVal") || _commands.get( i - 1).equals( "addFirstProbVal") || _commands.get( i - 1).equals( "addLastProbVal")) {
						if ( !entityBase.has_same_object_name( "probability", words[ i])) {
							on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.probability.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
							return false;
						}
					} else if ( _commands.get( i - 1).equals( "addRoleVal") || _commands.get( i - 1).equals( "addFirstRoleVal") || _commands.get( i - 1).equals( "addLastRoleVal")) {
						if ( !entityBase.has_same_object_name( "role variable", words[ i])) {
							on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.role.variable.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
							return false;
						}
					} else if ( _commands.get( i - 1).equals( "addTimeVal") || _commands.get( i - 1).equals( "addFirstTimeVal") || _commands.get( i - 1).equals( "addLastTimeVal")) {
						if ( !entityBase.has_same_object_name( "time variable", words[ i])) {
							on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.time.variable.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
							return false;
						}
					} else if ( _commands.get( i - 1).equals( "addSpotVal") || _commands.get( i - 1).equals( "addFirstSpotVal") || _commands.get( i - 1).equals( "addLastSpotVal")) {
						if ( !entityBase.has_same_object_name( "spot variable", words[ i])) {
							on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.spot.variable.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
							return false;
						}
					} else if ( _commands.get( i - 1).equals( "addMapVal") || _commands.get( i - 1).equals( "addFirstMapVal") || _commands.get( i - 1).equals( "addLastMapVal")) {
						if ( !entityBase.has_same_object_name( "map", words[ i])) {
							on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.map.variable.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
							return false;
						}
					} else if ( _commands.get( i - 1).equals( "addColVal") || _commands.get( i - 1).equals( "addFirstColVal") || _commands.get( i - 1).equals( "addLastColVal")) {
						if ( !entityBase.has_same_object_name( "collection", words[ i])) {
							on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.collection.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
							return false;
						}
						if ( _variables.get( i - 1).equals( words[ i])) {
							on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.self.collection.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
							return false;
						}
					} else if ( _commands.get( i - 1).equals( "addListVal") || _commands.get( i - 1).equals( "addFirstListVal") || _commands.get( i - 1).equals( "addLastListVal")) {
						if ( !entityBase.has_same_object_name( "list", words[ i])) {
							on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.list.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
							return false;
						}
						if ( _variables.get( i - 1).equals( words[ i])) {
							on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.self.list.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	/**
	 * @param value
	 * @return
	 */
	private boolean is_probability_correct(String value) {
		// valueが確率変数値として正しいかどうか？をチェック
		if ( !value.equals( "")) {
			if ( value.equals( "$") || 0 < value.indexOf( '$'))
				return false;

			if ( value.startsWith( "$") && 0 < value.indexOf( "$", 1))
				return false;

			if ( value.startsWith( "$") && 0 < value.indexOf( ")", 1))
				return false;

			if ( value.equals( "$Name")
				|| value.equals( "$Role")
				|| value.equals( "$Spot")
				|| 0 <= value.indexOf( Constant._experimentName))
				return false;

			if ( !CommonTool.is_probability_correct( value))
				return false;

			if ( null == NumberObject.is_correct( value, "real number"))
				return false;
		}
		return true;
	}

	/**
	 * @param value
	 * @param type
	 * @return
	 */
	private boolean is_number_object_correct(String value, String type) {
		// valueが数値変数値として正しいかどうか？をチェック
		if ( !value.equals( "")) {
			if ( value.equals( "$") || 0 < value.indexOf( '$'))
				return false;

			if ( value.startsWith( "$") && 0 < value.indexOf( "$", 1))
				return false;

			if ( value.startsWith( "$") && 0 < value.indexOf( ")", 1))
				return false;

			if ( value.equals( "$Name")
				|| value.equals( "$Role")
				|| value.equals( "$Spot")
				|| 0 <= value.indexOf( Constant._experimentName))
				return false;

			if ( null == NumberObject.is_correct( value, type))
				return false;
		}
		return true;
	}

	/**
	 * @param value
	 * @return
	 */
	private boolean is_keyword_correct(String value) {
		// valueがキーワード値として正しいかどうか？をチェック
		if ( 0 < value.indexOf( '$')
			|| value.equals( "$")
			|| value.startsWith( " ")
			|| value.endsWith( " ")
			|| value.equals( "$Name")
			|| value.equals( "$Role")
			|| value.equals( "$Spot")
			|| 0 <= value.indexOf( Constant._experimentName))
			return false;

		if ( value.startsWith( "$")
			&& ( 0 <= value.indexOf( " ")
			|| 0 < value.indexOf( "$", 1)
			|| 0 < value.indexOf( ")", 1)))
			return false;

		return true;
	}

	/**
	 * @param value
	 * @return
	 */
	private boolean is_role_variable_correct(String value) {
		// valueがエージェントロール名かどうか？をチェック
		return LayerManager.get_instance().is_agent_role_name( value);
	}

	/**
	 * @param value
	 * @return
	 */
	private boolean is_time_variable_correct(String value) {
		// valueが時間変数値として正しいかどうか？をチェック
		if ( value.startsWith( "$")) {
			if ( value.equals( "$")
				|| value.equals( "$Name")
				|| value.equals( "$Role")
				|| value.equals( "$Spot")
				|| 0 <= value.indexOf( Constant._experimentName)
				|| 0 <= value.indexOf( Constant._currentTimeName))
				return false;

			if ( 0 < value.indexOf( "$", 1)
				|| 0 < value.indexOf( ")", 1))
				return false;
		} else {
			if ( value.matches( "[0-9][/][0-9][:][0-5][0-9]")
				|| value.matches( "[0-9][/][1-2][0-9][:][0-5][0-9]")
				|| value.matches( "[1-9][0-9]+[/][0-9][:][0-5][0-9]")
				|| value.matches( "[1-9][0-9]+[/][1-2][0-9][:][0-5][0-9]")) {
				String[] words = value.split( "/");
				if ( null == words || 2 != words.length)
					return false;

				if ( !is_time_correct( words[ 1]))
					return false;
			} else if ( value.matches( "[0-9][:][0-5][0-9]")
				|| value.matches( "[1-2][0-9][:][0-5][0-9]")) {
				if ( !is_time_correct( value))
					return false;

			} else
				return false;
		}
		return true;
	}

	/**
	 * @param value
	 * @return
	 */
	private boolean is_time_correct(String value) {
		String[] words = value.split( ":");
		if ( null == words || 2 != words.length)
			return false;

		int hour;
		try {
			hour = Integer.parseInt( words[ 0]);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}

		if ( 0 > hour || 23 < hour)
			return false;

		int minute;
		try {
			minute = Integer.parseInt( words[ 1]);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}

		if ( 0 > minute || 59 < minute)
			return false;

		return true;
	}

	/**
	 * @param value
	 * @return
	 */
	private boolean is_spot_variable_correct(String value) {
		// valueがスポット名かどうか？をチェック
		return ( null != LayerManager.get_instance().get_spot_has_this_name( value));
	}

	/**
	 * @param words
	 * @param row
	 * @return
	 */
	private boolean read_echange_algebra_value(String[] words, int row) {
		// TODO Auto-generated method stub
		if ( _commands.size() < words.length - 1) {
			on_error( row);
			return false;
		}

		EntityBase entityBase = null;
		String entityType = "";
		String entityName = "";
		for ( int i = 0; i < words.length; ++i) {
			if ( 0 == i) {
				if ( words[ 0].equals( "")) {
					on_error( row, i + 1, words[ 0], ResourceManager.get_instance().get( "initial.data.file.checker.agent.or.spot.error.message"), "", "", "", "");
					return false;
				}
				entityBase = LayerManager.get_instance().get_entityBase_has_this_name( words[ 0]);
				if ( null == entityBase) {
					on_error( row, i + 1, words[ 0], ResourceManager.get_instance().get( "initial.data.file.checker.agent.or.spot.error.message"), "", "", "", "");
					return false;
				}
				entityType = ( entityBase instanceof AgentObject) ? "agent" : "spot";
				entityName = words[ 0];
			} else {
				if ( words[ i].equals( ""))
					continue;

				if ( _commands.get( i - 1).equals( "addExalge")) {
					if ( !entityBase.has_same_object_name( "exchange algebra", _variables.get( i - 1)) && !entityBase.is_exchange_algebra( _variables.get( i - 1))) {
						on_error( row, i + 1, _variables.get( i - 1), ResourceManager.get_instance().get( "initial.data.file.checker.exchange.algebra.error.message"), entityType, entityName, _commands.get( i - 1), "");
						return false;
					}
					if ( !is_exchange_algebra_correct( words[ i])) {
						on_error( row, i + 1, words[ i], ResourceManager.get_instance().get( "initial.data.file.checker.value.error.message"), entityType, entityName, _commands.get( i - 1), _variables.get( i - 1));
						return false;
					}
				}
			}
		}

		return true;
	}

	/**
	 * @param value
	 * @return
	 */
	private boolean is_exchange_algebra_correct(String value) {
		// TODO Auto-generated method stub
		boolean hat = ( 0 <= value.indexOf( '^'));
		String[] parameters = value.split( hat ? "\\^" : "<");
		if ( null == parameters || 2 != parameters.length)
			return false;

		parameters[ 1] = ( ( hat ? "^" : "<") + parameters[ 1]);

		try {
			ExalgeFactory.add( parameters[ 0], ExchangeAlgebraCommand.toBaseOneKeyString( parameters[ 1]));
		} catch (Throwable ex) {
			return false;
		}

		return ( null != ExalgeFactory.toExalge());
	}

	/**
	 * @param row
	 */
	private void on_error(int row) {
		// エラーメッセージ
		String message = ( ResourceManager.get_instance().get( "initial.data.file.checker.row.error.message") + Constant._lineSeparator);
		message += ( _name.equals( "") ? "" : ( String.format( ResourceManager.get_instance().get( "initial.data.file.checker.error.message.file"), _name) + Constant._lineSeparator));
		message += ( String.format( ResourceManager.get_instance().get( "initial.data.file.checker.error.message.row"), row) + Constant._lineSeparator);
		JOptionPane.showMessageDialog( MainFrame.get_instance(),
			message,
			ResourceManager.get_instance().get( "initial.data.file.checker.title"),
			JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param row
	 * @param column
	 * @param value
	 * @param message
	 * @param entityType
	 * @param entityName
	 * @param command
	 * @param variable
	 */
	private void on_error(int row, int column, String value, String message, String entityType, String entityName, String command, String variable) {
		// エラーメッセージ
		message += Constant._lineSeparator;
		message += ( _name.equals( "") ? "" : ( String.format( ResourceManager.get_instance().get( "initial.data.file.checker.error.message.file"), _name) + Constant._lineSeparator));
		message += ( String.format( ResourceManager.get_instance().get( "initial.data.file.checker.error.message.row.and.column"), row, column) + Constant._lineSeparator);
		message += ( entityType.equals( "") ? "" : String.format( ResourceManager.get_instance().get( "initial.data.file.checker.error.message.entity"),
			entityType.equals( "agent") ? ResourceManager.get_instance().get( "initial.data.file.checker.error.message.agent") : ResourceManager.get_instance().get( "initial.data.file.checker.error.message.spot"),
			entityName) + Constant._lineSeparator);
		message += ( command.equals( "") ? "" : ( String.format( ResourceManager.get_instance().get( "initial.data.file.checker.error.message.command"), command) + Constant._lineSeparator));
		message += ( variable.equals( "") ? "" : ( String.format( ResourceManager.get_instance().get( "initial.data.file.checker.error.message.variable"), variable) + Constant._lineSeparator));
		message += ( String.format( ResourceManager.get_instance().get( "initial.data.file.checker.error.message.value"), value) + Constant._lineSeparator);
		JOptionPane.showMessageDialog( MainFrame.get_instance(),
			message,
			ResourceManager.get_instance().get( "initial.data.file.checker.title"),
			JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param name
	 * @return
	 */
	public static String get_initial_data_file_type(String name) {
		// TODO Auto-generated method stub
		File file = new File( LayerManager.get_instance().get_user_data_directory(), name);
		if ( !file.exists() || !file.isFile() || !file.canRead())
			return null;

		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader( new InputStreamReader( new FileInputStream( file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		String type = null;
		while ( true) {
			String line = null;
			try {
				line = bufferedReader.readLine();
			} catch (IOException e1) {
				try {
					bufferedReader.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				e1.printStackTrace();
				return null;
			}

			if ( null == line)
				break;

			if ( 0 == line.length())
				continue;

			type = read( line);

			break;
		}

		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return type;
	}

	/**
	 * @param line
	 * @return
	 */
	private static String read(String line) {
		// TODO Auto-generated method stub
		String[] words = Tool.split( line, '\t');
		if ( null == words)
			return null;

		if ( 2 > words.length)
			return null;

		List<String> commands = new ArrayList<String>();

		for ( int i = 1; i < words.length; ++i) {
			if ( !_commandList.contains( words[ i]))
				return null;

			commands.add( words[ i]);
		}

		// TODO Auto-generated method stub
		int counter = 0;
		for ( String command:commands) {
			if ( command.equals( "addExalge"))
				++counter;
		}

		if ( commands.size() == counter)
			return "exchange algebra";
		else if ( 0 < counter)
			return null;
		else
			return "common";
	}
}
