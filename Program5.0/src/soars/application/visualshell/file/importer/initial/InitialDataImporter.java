/*
 * Created on 2005/11/19
 */
package soars.application.visualshell.file.importer.initial;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.file.importer.initial.comment.CommentDataMap;
import soars.application.visualshell.file.importer.initial.entity.EntityData;
import soars.application.visualshell.file.importer.initial.entity.EntityDataMap;
import soars.application.visualshell.file.importer.initial.expression.VisualShellExpressionDataMap;
import soars.application.visualshell.file.importer.initial.role.RoleData;
import soars.application.visualshell.file.importer.initial.role.RoleDataMap;
import soars.application.visualshell.file.importer.initial.simulation.SimulationDataMap;
import soars.application.visualshell.file.importer.initial.stage.StageDataMap;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.MainFrame;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.arbitrary.JarFileProperties;
import soars.application.visualshell.object.arbitrary.JavaClasses;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.object.file.FileObject;
import soars.application.visualshell.object.entity.base.object.keyword.KeywordObject;
import soars.application.visualshell.object.entity.base.object.number.NumberObject;
import soars.application.visualshell.object.entity.base.object.probability.ProbabilityObject;
import soars.application.visualshell.object.entity.base.object.role.RoleVariableObject;
import soars.application.visualshell.object.entity.base.object.spot.SpotVariableObject;
import soars.application.visualshell.object.entity.base.object.time.TimeVariableObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.file.FileUtility;

/**
 * Imports the initial data from the file in CSV format.
 * @author kurata / SOARS project
 */
public class InitialDataImporter {

	/**
	 * 
	 */
	private EntityDataMap _agentDataMap = new EntityDataMap( "agent");

	/**
	 * 
	 */
	private Point _agentPosition = new Point( 0, 0);

	/**
	 * 
	 */
	private EntityDataMap _spotDataMap = new EntityDataMap( "spot");

	/**
	 * 
	 */
	private Point _spotPosition = new Point( 0, 50);

	/**
	 * 
	 */
	private RoleDataMap _agentRoleDataMap = new RoleDataMap( "agent_role");

	/**
	 * 
	 */
	private Point _agentRolePosition = new Point( 0, 100);

	/**
	 * 
	 */
	private RoleDataMap _spotRoleDataMap = new RoleDataMap( "spot_role");

	/**
	 * 
	 */
	private Point _spotRolePosition = new Point( 0, 150);

	/**
	 * 
	 */
	private Map<RoleData, String[]> _agentRoleConnectionMap = new HashMap<RoleData, String[]>();

	/**
	 * 
	 */
	private Map<RoleData, String[]> _spotRoleConnectionMap = new HashMap<RoleData, String[]>();

	/**
	 * 
	 */
	private StageDataMap _initialStageMap = new StageDataMap( "initial stage");

	/**
	 * 
	 */
	private StageDataMap _mainStageMap = new StageDataMap( "main stage");

	/**
	 * 
	 */
	private StageDataMap _terminalStageMap = new StageDataMap( "terminal stage");

	/**
	 * 
	 */
	private SimulationDataMap _simulationDataMap = new SimulationDataMap();

	/**
	 * 
	 */
	private VisualShellExpressionDataMap _visualShellExpressionDataMap
		= new VisualShellExpressionDataMap( VisualShellExpressionManager.get_instance());

	/**
	 * 
	 */
	private String _otherScripts = "";

	/**
	 * 
	 */
	private CommentDataMap _commentDataMap = new CommentDataMap();

	/**
	 * 
	 */
	private List<String> _appendedJavaClassList = new ArrayList<String>();

	/**
	 * 
	 */
	private boolean _warning = true;

	/**
	 * 
	 */
	private boolean _all = false;

	/**
	 * Returns 1 for importing the initial data from the specified file in CSV format.
	 * @param file the specified file
	 * @param warning whether to display the warning messages
	 * @param all true if all data is imported
	 * @param component the base class for all Swing components
	 * @return 1 for importing the initial data from the specified file in CSV format
	 */
	public static int execute(File file, boolean warning, boolean all, JComponent component) {
		InitialDataImporter initialDataImporter = new InitialDataImporter( warning, all);
		int result = initialDataImporter.load( file, component);
		initialDataImporter.at_end_of_load();
		return result;
	}

	/**
	 * Returns 1 for importing the initial data from the clipboard in CSV format.
	 * @param warning whether to display the warning messages
	 * @param all true if all data is imported
	 * @param component the base class for all Swing components
	 * @return 1 for importing the initial data from the clipboard in CSV format
	 */
	public static int execute(boolean warning, boolean all, JComponent component) {
		InitialDataImporter initialDataImporter = new InitialDataImporter( warning, all);
		int result = initialDataImporter.load( component);
		initialDataImporter.at_end_of_load();
		return result;
	}

	/**
	 * Creates this object with the specified data.
	 * @param warning whether to display the warning messages
	 * @param all true if all data is imported
	 */
	public InitialDataImporter(boolean warning, boolean all) {
		super();
		_warning = warning;
		_all = all;
	}

	/**
	 * 
	 */
	private void cleanup() {
		String homeDirectoryName = System.getProperty( Constant._soarsHome);
		String temporaryDirectoryName = SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._tmpKey, "");
		File rootDirectory = new File( homeDirectoryName + "/" + temporaryDirectoryName + Constant._testDirectory );
		FileUtility.delete( rootDirectory, false);
	}

	/**
	 * 
	 */
	private void at_end_of_load() {
		cleanup();

		if ( !_warning)
			return;

		JavaClasses.show_message_dialog( _appendedJavaClassList);
	}

	/**
	 * @param file
	 * @param component
	 * @return
	 */
	private int load(File file, JComponent component) {
		cleanup();

		BufferedReader bufferedReader;
		try {
			if ( 0 <= System.getProperty( "os.name").indexOf( "Mac")
				&& !System.getProperty( Constant._systemDefaultFileEncoding, "").equals( ""))
				bufferedReader = new BufferedReader( new InputStreamReader( new FileInputStream( file),
					System.getProperty( Constant._systemDefaultFileEncoding, "")));
			else
				bufferedReader = new BufferedReader( new InputStreamReader( new FileInputStream( file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return 0;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return 0;
		}

		int number = 1;
		boolean at_first = true;
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
				return 0;
			}

			if ( null == line)
				break;

			if ( 0 == line.length()) {
				++number;
				continue;
			}

			if ( at_first) {
				if ( !line.startsWith( Constant._initialDataIdentifier))
					return 0;

				++number;

				at_first = false;

				continue;
			}

			if ( !read( line, number)) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return 0;
			}

			++number;
		}

		if ( !_agentDataMap.verify( _spotDataMap, _agentRoleDataMap)) {
			warning( MainFrame.get_instance(),
				ResourceManager.get_instance().get( "warning.dialog1.title"),
				ResourceManager.get_instance().get( "warning.dialog1.message1"),
				_warning);
			return 0;
		}

		if ( !_agentDataMap.verify( _agentRoleDataMap, _spotRoleDataMap)) {
			warning( MainFrame.get_instance(),
				ResourceManager.get_instance().get( "warning.dialog1.title"),
				ResourceManager.get_instance().get( "warning.dialog1.message1"),
				_warning);
			return 0;
		}

		if ( !_spotDataMap.verify( _agentDataMap, _spotRoleDataMap)) {
			warning( MainFrame.get_instance(),
				ResourceManager.get_instance().get( "warning.dialog1.title"),
				ResourceManager.get_instance().get( "warning.dialog1.message1"),
				_warning);
			return 0;
		}

		if ( !_agentRoleDataMap.verify( new StageDataMap[] { _initialStageMap, _mainStageMap, _terminalStageMap})) {
			warning( MainFrame.get_instance(),
				ResourceManager.get_instance().get( "warning.dialog1.title"),
				ResourceManager.get_instance().get( "warning.dialog1.message1"),
				_warning);
			return 0;
		}

		if ( !_spotRoleDataMap.verify( new StageDataMap[] { _initialStageMap, _mainStageMap, _terminalStageMap})) {
			warning( MainFrame.get_instance(),
				ResourceManager.get_instance().get( "warning.dialog1.title"),
				ResourceManager.get_instance().get( "warning.dialog1.message1"),
				_warning);
			return 0;
		}

		if ( !LayerManager.get_instance().append(
			_agentDataMap, _spotDataMap,
			_agentRoleDataMap, _spotRoleDataMap,
			_agentRoleConnectionMap, _spotRoleConnectionMap,
			_initialStageMap._stageDataList,
			_mainStageMap._stageDataList,
			_terminalStageMap._stageDataList,
			_simulationDataMap,
			_visualShellExpressionDataMap,
			_otherScripts,
			_commentDataMap,
			component)) {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return -1;
		}

		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

//		_agentDataMap.print();
//		_spotDataMap.print();

//		LayerManager.get_instance().print();

		return 1;
	}

	/**
	 * @param component
	 * @return
	 */
	private int load(JComponent component) {
		cleanup();

		String string = Clipboard.get_text();
		if ( null == string)
			return 0;

		String[] lines = string.split( Constant._lineSeparator);

		boolean at_first = true;
		for ( int i = 0; i < lines.length; ++i) {
			if ( null == lines[ i])
				break;

			if ( 0 == lines[ i].length())
				continue;

			if ( at_first) {
				if ( !lines[ i].startsWith( Constant._initialDataIdentifier))
					return 0;

				at_first = false;

				continue;
			}

			if ( !read( lines[ i], i + 1))
				return 0;
		}

		if ( !_agentDataMap.verify( _spotDataMap, _agentRoleDataMap)) {
			warning( MainFrame.get_instance(),
				ResourceManager.get_instance().get( "warning.dialog1.title"),
				ResourceManager.get_instance().get( "warning.dialog1.message1"),
				_warning);
			return 0;
		}

		if ( !_spotDataMap.verify( _agentDataMap, _spotRoleDataMap)) {
			warning( MainFrame.get_instance(),
				ResourceManager.get_instance().get( "warning.dialog1.title"),
				ResourceManager.get_instance().get( "warning.dialog1.message1"),
				_warning);
			return 0;
		}

		if ( !_agentRoleDataMap.verify( new StageDataMap[] { _initialStageMap, _mainStageMap, _terminalStageMap})) {
			warning( MainFrame.get_instance(),
				ResourceManager.get_instance().get( "warning.dialog1.title"),
				ResourceManager.get_instance().get( "warning.dialog1.message1"),
				_warning);
			return 0;
		}

		if ( !_spotRoleDataMap.verify( new StageDataMap[] { _initialStageMap, _mainStageMap, _terminalStageMap})) {
			warning( MainFrame.get_instance(),
				ResourceManager.get_instance().get( "warning.dialog1.title"),
				ResourceManager.get_instance().get( "warning.dialog1.message1"),
				_warning);
			return 0;
		}

		if ( !LayerManager.get_instance().append(
			_agentDataMap, _spotDataMap,
			_agentRoleDataMap, _spotRoleDataMap,
			_agentRoleConnectionMap, _spotRoleConnectionMap,
			_initialStageMap._stageDataList,
			_mainStageMap._stageDataList,
			_terminalStageMap._stageDataList,
			_simulationDataMap,
			_visualShellExpressionDataMap,
			_otherScripts,
			_commentDataMap,
			component))
			return -1;

		return 1;
	}

	/**
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean read(String line, int number) {
		String[] words = Tool.split( line, '\t');

		if ( words[ 0].equals( ResourceManager.get_instance().get( "initial.data.other.scripts"))) {
			if ( !_all)
				return true;

			if ( 2 > words.length) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			for ( int i = 1; i < words.length; ++i) {
				_otherScripts += ( ( ( 1 == i) ? "" : "\t") + words[ i]);
			}

			_otherScripts += Constant._lineSeparator;
			return true;
		}

		if ( words[ 0].equals( ResourceManager.get_instance().get( "initial.data.agent.role"))) {
			if ( !_all)
				return true;

			if ( 3 > words.length) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			if ( 3 == words.length)
				words = new String[] { words[ 0], words[ 1], words[ 2], ""};

			return read_agent_role( words, line, number);
		} else if ( words[ 0].equals( ResourceManager.get_instance().get( "initial.data.spot.role"))) {
			if ( !_all)
				return true;

			if ( 3 > words.length) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			if ( 3 == words.length)
				words = new String[] { words[ 0], words[ 1], words[ 2], ""};

			return read_spot_role( words, line, number);
		}

		words = line.split( "\t");
		words = trim( words);
		if ( null == words || 0 == words.length)
			return true;

		String kind;
		String name;
		if ( words[ 0].equals( ResourceManager.get_instance().get( "initial.data.agent.number"))) {
			if ( 3 > words.length) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			if ( !read_agent_number( words, line, number))
				return false;
		} else if ( words[ 0].equals( ResourceManager.get_instance().get( "initial.data.spot.number"))) {
			if ( 3 > words.length) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			if ( !read_spot_number( words, line, number))
				return false;
		} else if ( words[ 0].equals( ResourceManager.get_instance().get( "initial.data.initial.stage"))) {
			if ( !_all)
				return true;

			if ( 2 > words.length) {
				on_invalid_line_error( number, _warning);
				return false;
			}
			if ( !read_initial_stage( words, line, number))
				return false;
		} else if ( words[ 0].equals( ResourceManager.get_instance().get( "initial.data.main.stage"))) {
			if ( !_all)
				return true;

			if ( 2 > words.length) {
				on_invalid_line_error( number, _warning);
				return false;
			}
			if ( !read_main_stage( words, line, number))
				return false;
		} else if ( words[ 0].equals( ResourceManager.get_instance().get( "initial.data.terminal.stage"))) {
			if ( !_all)
				return true;

			if ( 2 > words.length) {
				on_invalid_line_error( number, _warning);
				return false;
			}
			if ( !read_terminal_stage( words, line, number))
				return false;
		} else if ( words[ 0].equals( ResourceManager.get_instance().get( "initial.data.simulation"))) {
			if ( !_all)
				return true;

			if ( 3 > words.length) {
				on_invalid_line_error( number, _warning);
				return false;
			}
			if ( !read_simulation( words, line, number))
				return false;
		} else if ( words[ 0].equals( ResourceManager.get_instance().get( "initial.data.expression"))) {
			if ( !_all)
				return true;

			if ( 4 > words.length) {
				on_invalid_line_error( number, _warning);
				return false;
			}
			if ( !read_expression( words, line, number))
				return false;
		} else if ( words[ 0].equals( ResourceManager.get_instance().get( "initial.data.comment"))) {
			if ( !_all)
				return true;

			if ( 3 > words.length) {
				on_invalid_line_error( number, _warning);
				return false;
			}
			if ( !read_comment( words, line, number))
				return false;
		} else {
			if ( !words[ 2].equals( ResourceManager.get_instance().get( "initial.data.number"))) {
				if ( words[ 2].equals( ResourceManager.get_instance().get( "initial.data.probability"))
					|| words[ 2].equals( ResourceManager.get_instance().get( "initial.data.keyword"))
					|| words[ 2].equals( ResourceManager.get_instance().get( "initial.data.integer.number.object"))
					|| words[ 2].equals( ResourceManager.get_instance().get( "initial.data.real.number.object"))
					|| words[ 2].equals( ResourceManager.get_instance().get( "initial.data.role.variable"))
					|| words[ 2].equals( ResourceManager.get_instance().get( "initial.data.time.variable"))
					|| words[ 2].equals( ResourceManager.get_instance().get( "initial.data.spot.variable"))
					|| words[ 2].equals( ResourceManager.get_instance().get( "initial.data.file"))) {
					if ( 4 != words.length && 5 != words.length) {
						on_invalid_line_error( number, _warning);
						return false;
					}
				} else if ( words[ 2].equals( ResourceManager.get_instance().get( "initial.data.initial.role"))
					|| words[ 2].equals( ResourceManager.get_instance().get( "initial.data.collection"))
					|| words[ 2].equals( ResourceManager.get_instance().get( "initial.data.list"))) {
					if ( 4 > words.length) {
						on_invalid_line_error( number, _warning);
						return false;
					}
				} else if ( words[ 2].equals( ResourceManager.get_instance().get( "initial.data.map"))) {
					if ( 4 > words.length || 1 == words.length % 2) {
						on_invalid_line_error( number, _warning);
						return false;
					}
				} else if ( words[ 2].equals( ResourceManager.get_instance().get( "initial.data.class.variable"))) {
					if ( 6 != words.length) {
						on_invalid_line_error( number, _warning);
						return false;
					}
				} else if ( words[ 2].equals( ResourceManager.get_instance().get( "initial.data.exchange.algebra"))) {
					if ( 4 > words.length) {
						on_invalid_line_error( number, _warning);
						return false;
					}
				}

				kind = words[ 2];
				name = words[ 3];

				if ( words[ 0].equals( ResourceManager.get_instance().get( "initial.data.agent"))) {
					if ( !read_agent( words, kind, name, line, number))
						return false;
				} else if ( words[ 0].equals( ResourceManager.get_instance().get( "initial.data.spot"))) {
					if ( !read_spot( words, kind, name, line, number))
						return false;
				}

			} else {
				if ( words[ 4].equals( ResourceManager.get_instance().get( "initial.data.probability"))
					|| words[ 4].equals( ResourceManager.get_instance().get( "initial.data.keyword"))
					|| words[ 4].equals( ResourceManager.get_instance().get( "initial.data.integer.number.object"))
					|| words[ 4].equals( ResourceManager.get_instance().get( "initial.data.real.number.object"))
					|| words[ 4].equals( ResourceManager.get_instance().get( "initial.data.role.variable"))
					|| words[ 4].equals( ResourceManager.get_instance().get( "initial.data.time.variable"))
					|| words[ 4].equals( ResourceManager.get_instance().get( "initial.data.spot.variable"))
					|| words[ 4].equals( ResourceManager.get_instance().get( "initial.data.file"))) {
					if ( 6 != words.length && 7 != words.length) {
						on_invalid_line_error( number, _warning);
						return false;
					}
				} else if (words[ 4].equals( ResourceManager.get_instance().get( "initial.data.collection")) 
					|| words[ 4].equals( ResourceManager.get_instance().get( "initial.data.list"))) {
					if ( 6 > words.length) {
						on_invalid_line_error( number, _warning);
						return false;
					}
				} else if ( words[ 4].equals( ResourceManager.get_instance().get( "initial.data.map"))) {
					if ( 6 > words.length || 1 == words.length % 2) {
						on_invalid_line_error( number, _warning);
						return false;
					}
				} else if ( words[ 4].equals( ResourceManager.get_instance().get( "initial.data.class.variable"))) {
					if ( 8 != words.length) {
						on_invalid_line_error( number, _warning);
						return false;
					}
				} else if ( words[ 4].equals( ResourceManager.get_instance().get( "initial.data.exchange.algebra"))) {
					if ( 6 > words.length) {
						on_invalid_line_error( number, _warning);
						return false;
					}
				}

				Vector<int[]> indices = CommonTool.get_indices( words[ 3], ":", "_");
				if ( null == indices || indices.isEmpty()) {
					on_invalid_line_error( number, _warning);
					return false;
				}

				indices = CommonTool.sort_indices( indices);

				kind = words[ 4];
				name = words[ 5];

				if ( words[ 0].equals( ResourceManager.get_instance().get( "initial.data.agent"))) {
					if ( !read_agent( words, indices, kind, name, line, number))
						return false;
				} else if ( words[ 0].equals( ResourceManager.get_instance().get( "initial.data.spot"))) {
					if ( !read_spot( words, indices, kind, name, line, number))
						return false;
				}
			}
		}

		return true;
	}

	/**
	 * @param words
	 * @return
	 */
	private String[] trim(String[] words) {
		if ( 0 == words.length)
			return null;

		int index = words.length - 1;
		while ( 0 <= index) {
			if ( null != words[ index] && !words[ index].equals( ""))
				break;

			--index;
		}

		if ( 0 > index)
			return null;

		Vector<String> ws = new Vector<String>();
		for ( int i = 0; i <= index; ++i)
			ws.add( words[ i]);

		return ws.toArray( new String[ 0]);
	}

	/**
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean read_agent_number(String[] words, String line, int number) {
		int newNumber;
		try {
			newNumber = Integer.parseInt( words[ 2]);
			if ( newNumber < 1) {
				on_invalid_line_error( number, _warning);
				return false;
			}
			words[ 2] = String.valueOf( newNumber);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			on_invalid_line_error( number, _warning);
			return false;
		}

		if ( !EntityData.is_valid_name( words[ 1], Constant._prohibitedCharacters2, number, _warning))
			return false;

		AgentObject agentObject = LayerManager.get_instance().get_agent( words[ 1]);
		if ( null != agentObject) {
			if ( agentObject.is_multi()) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			if ( !can_adjust_name( words, newNumber, agentObject)) {
				on_invalid_line_error( number, _warning);
				return false;
			}
		} else {
			if ( LayerManager.get_instance().contains( "agent", words[ 1])) {
				on_invalid_line_error( number, _warning);
				return false;
			}
		}

		EntityData agentData = _agentDataMap.get( words[ 1]);
		if ( null != agentData) {
			if ( agentData.is_multi()) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			// TODO
			if ( _agentDataMap.contains( words[ 1], words[ 2], words[ 1]) || _spotDataMap.contains( words[ 1], words[ 2], words[ 1])
				|| LayerManager.get_instance().contains_as_object_name( words[ 1], words[ 2])
				|| _agentDataMap.contains_as_object_name( words[ 1], words[ 2]) || _spotDataMap.contains_as_object_name( words[ 1], words[ 2])) {
				on_invalid_line_error( number, _warning);
				return false;
			}
		} else {
			// TODO
			if ( _agentDataMap.contains( words[ 1]) || _spotDataMap.contains( words[ 1])
				|| LayerManager.get_instance().contains_as_object_name( words[ 1], words[ 2])
				|| _agentDataMap.contains_as_object_name( words[ 1], words[ 2]) || _spotDataMap.contains_as_object_name( words[ 1], words[ 2])) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			if ( null != agentObject)
				agentData = new EntityData( "agent", words[ 1]);
			else {
				_agentPosition.x += 30;
				agentData = new EntityData( "agent", words[ 1], _agentPosition);
			}

			_agentDataMap.put( words[ 1], agentData);
		}

		agentData._number = words[ 2];

		return true;
	}

	/**
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean read_spot_number(String[] words, String line, int number) {
		int newNumber;
		try {
			newNumber = Integer.parseInt( words[ 2]);
			if ( newNumber < 1) {
				on_invalid_line_error( number, _warning);
				return false;
			}
			words[ 2] = String.valueOf( newNumber);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			on_invalid_line_error( number, _warning);
			return false;
		}

		if ( !EntityData.is_valid_name( words[ 1], Constant._prohibitedCharacters2, number, _warning))
			return false;

		SpotObject spotObject = LayerManager.get_instance().get_spot( words[ 1]);
		if ( null != spotObject) {
			if ( spotObject.is_multi()) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			if ( !can_adjust_name( words, newNumber, spotObject)) {
				on_invalid_line_error( number, _warning);
				return false;
			}
		} else {
			if ( LayerManager.get_instance().contains( "spot", words[ 1])) {
				on_invalid_line_error( number, _warning);
				return false;
			}
		}

		EntityData spotData = _spotDataMap.get( words[ 1]);
		if ( null != spotData) {
			if ( spotData.is_multi()) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			// TODO
			if ( _spotDataMap.contains( words[ 1], words[ 2], words[ 1]) || _agentDataMap.contains( words[ 1], words[ 2], words[ 1])
				|| LayerManager.get_instance().contains_as_object_name( words[ 1], words[ 2])
				|| _spotDataMap.contains_as_object_name( words[ 1], words[ 2]) || _agentDataMap.contains_as_object_name( words[ 1], words[ 2])) {
				on_invalid_line_error( number, _warning);
				return false;
			}
		} else {
			// TODO
			if ( _spotDataMap.contains( words[ 1]) || _agentDataMap.contains( words[ 1])
				|| LayerManager.get_instance().contains_as_object_name( words[ 1], words[ 2])
				|| _spotDataMap.contains_as_object_name( words[ 1], words[ 2]) || _agentDataMap.contains_as_object_name( words[ 1], words[ 2])) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			if ( null != spotObject)
				spotData = new EntityData( "spot", words[ 1]);
			else {
				_spotPosition.x += 30;
				spotData = new EntityData( "spot", words[ 1], _spotPosition);
			}

			_spotDataMap.put( words[ 1], spotData);
		}

		spotData._number = words[ 2];

		return true;
	}

	/**
	 * @param words
	 * @param newNumber
	 * @param entityBase
	 * @return
	 */
	private boolean can_adjust_name(String[] words, int newNumber, EntityBase entityBase) {
		String headName = SoarsCommonTool.separate( entityBase._name);
		String headNumber = entityBase._name.substring( headName.length());
		Vector<String[]> ranges = SoarsCommonTool.get_ranges( headNumber, entityBase._number);
		if ( null == ranges)
			return false;

		String newHeadName = SoarsCommonTool.separate( words[ 1]);
		String newHeadNumber = words[ 1].substring( newHeadName.length());
		Vector<String[]> newRanges = SoarsCommonTool.get_ranges( newHeadNumber, words[ 2]);
		if ( null == newRanges)
			return false;

		int originalNumber;
		try {
			originalNumber = Integer.parseInt( entityBase._number);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			originalNumber = 0;
		}

		if ( !newHeadName.equals( headName)) {
			if ( ( 0 < originalNumber && originalNumber <= newNumber))
				return true;

			if ( entityBase instanceof SpotObject)
				return LayerManager.get_instance().can_adjust_spot_name( headName, ranges, false);
			else if ( entityBase instanceof AgentObject)
				return LayerManager.get_instance().can_adjust_agent_name( headName, ranges, false);
			else
				return false;
		} else {
			if ( entityBase instanceof SpotObject)
				return LayerManager.get_instance().can_adjust_spot_name( headName, ranges, newHeadName, newRanges);
			else if ( entityBase instanceof AgentObject)
				return LayerManager.get_instance().can_adjust_agent_name( headName, ranges, newHeadName, newRanges);
			else
				return false;
		}
	}

	/**
	 * @param words
	 * @param kind
	 * @param name
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean read_agent(String[] words, String kind, String name, String line, int number) {
//		boolean exist = false;

		if ( !EntityData.is_valid_name( words[ 1], Constant._prohibitedCharacters2, number, _warning))
			return false;

		AgentObject agentObject = LayerManager.get_instance().get_agent( words[ 1]);
		if ( null != agentObject) {
			if ( agentObject.is_multi()) {
				on_invalid_line_error( number, _warning);
				return false;
			}

//			exist = true;

		} else {
			if ( LayerManager.get_instance().contains( "agent", words[ 1])) {
				on_invalid_line_error( number, _warning);
				return false;
			}
		}

		EntityData agentData = _agentDataMap.get( words[ 1]);
		if ( null != agentData) {
			if ( agentData.is_multi()) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			// TODO
			if ( _agentDataMap.contains( words[ 1], agentData._number, words[ 1]) || _spotDataMap.contains( words[ 1], agentData._number, words[ 1])
				|| LayerManager.get_instance().contains_as_object_name( words[ 1], agentData._number)
				|| _agentDataMap.contains_as_object_name( words[ 1], agentData._number) || _spotDataMap.contains_as_object_name( words[ 1], agentData._number)) {
				on_invalid_line_error( number, _warning);
				return false;
			}
		} else {
			// TODO
			if ( _agentDataMap.contains( words[ 1]) || _spotDataMap.contains( words[ 1])
				|| LayerManager.get_instance().contains_as_object_name( words[ 1], "")
				|| _agentDataMap.contains_as_object_name( words[ 1], "") || _spotDataMap.contains_as_object_name( words[ 1], "")) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			if ( null != agentObject)
				agentData = new EntityData( "agent", words[ 1]);
			else {
				_agentPosition.x += 30;
				agentData = new EntityData( "agent", words[ 1], _agentPosition);
			}

			_agentDataMap.put( words[ 1], agentData);
		}

		if ( !set( agentData, words, kind, line, number))
			return false;

		return true;
	}

	/**
	 * @param words
	 * @param indices
	 * @param kind
	 * @param name
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean read_agent(String[] words, Vector<int[]> indices, String kind, String name, String line, int number) {
//		boolean exist = false;

		if ( !EntityData.is_valid_name( words[ 1], Constant._prohibitedCharacters2, number, _warning))
			return false;

		int max = CommonTool.get_max( indices);

		AgentObject agentObject = LayerManager.get_instance().get_agent( words[ 1]);
		if ( null != agentObject) {
			if ( !agentObject.is_multi()) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			if ( LayerManager.get_instance().contains( "agent", words[ 1], String.valueOf( max), words[ 1])) {
				on_invalid_line_error( number, _warning);
				return false;
			}

//			exist = true;

		} else {
			if ( LayerManager.get_instance().contains( "agent", words[ 1], String.valueOf( max), null)) {
				on_invalid_line_error( number, _warning);
				return false;
			}
		}

		EntityData agentData = _agentDataMap.get( words[ 1]);
		if ( null != agentData) {
			if ( !agentData.is_multi()) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			// TODO
			if ( _agentDataMap.contains( words[ 1], String.valueOf( max), words[ 1]) || _spotDataMap.contains( words[ 1], String.valueOf( max), words[ 1])
				|| LayerManager.get_instance().contains_as_object_name( words[ 1], String.valueOf( max))
				|| _agentDataMap.contains_as_object_name( words[ 1], String.valueOf( max)) || _spotDataMap.contains_as_object_name( words[ 1], String.valueOf( max))) {
				on_invalid_line_error( number, _warning);
				return false;
			}
		} else {
			// TODO
			if ( _agentDataMap.contains( words[ 1], String.valueOf( max), null) || _spotDataMap.contains( words[ 1], String.valueOf( max), null)
				|| LayerManager.get_instance().contains_as_object_name( words[ 1], String.valueOf( max))
				|| _agentDataMap.contains_as_object_name( words[ 1], String.valueOf( max)) || _spotDataMap.contains_as_object_name( words[ 1], String.valueOf( max))) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			if ( null != agentObject)
				agentData = new EntityData( "agent", words[ 1]);
			else {
				_agentPosition.x += 30;
				agentData = new EntityData( "agent", words[ 1], _agentPosition);
			}

			_agentDataMap.put( words[ 1], agentData);
		}

		agentData.set_number( max);

		if ( !set( agentData, words, indices, kind, line, number))
			return false;

		return true;
	}

	/**
	 * @param words
	 * @param kind
	 * @param name
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean read_spot(String[] words, String kind, String name, String line, int number) {
//		boolean exist = false;

		if ( kind.equals( ResourceManager.get_instance().get( "initial.data.role.variable"))) {
			on_invalid_line_error( number, _warning);
			return false;
		}

		if ( !EntityData.is_valid_name( words[ 1], Constant._prohibitedCharacters2, number, _warning))
			return false;

		SpotObject spotObject = LayerManager.get_instance().get_spot( words[ 1]);
		if ( null != spotObject) {
			if ( spotObject.is_multi()) {
				on_invalid_line_error( number, _warning);
				return false;
			}

//			exist = true;

		} else {
			if ( LayerManager.get_instance().contains( "spot", words[ 1])) {
				on_invalid_line_error( number, _warning);
				return false;
			}
		}

		EntityData spotData = _spotDataMap.get( words[ 1]);
		if ( null != spotData) {
			if ( spotData.is_multi()) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			// TODO
			if ( _spotDataMap.contains( words[ 1], spotData._number, words[ 1]) || _agentDataMap.contains( words[ 1], spotData._number, words[ 1])
				|| LayerManager.get_instance().contains_as_object_name( words[ 1], spotData._number)
				|| _spotDataMap.contains_as_object_name( words[ 1], spotData._number) || _agentDataMap.contains_as_object_name( words[ 1], spotData._number)) {
				on_invalid_line_error( number, _warning);
				return false;
			}
		} else {
			// TODO
			if ( _spotDataMap.contains( words[ 1]) || _agentDataMap.contains( words[ 1])
				|| LayerManager.get_instance().contains_as_object_name( words[ 1], "")
				|| _spotDataMap.contains_as_object_name( words[ 1], "") || _agentDataMap.contains_as_object_name( words[ 1], "")) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			if ( null != spotObject)
				spotData = new EntityData( "spot", words[ 1]);
			else {
				_spotPosition.x += 30;
				spotData = new EntityData( "spot", words[ 1], _spotPosition);
			}

			_spotDataMap.put( words[ 1], spotData);
		}

		if ( !set( spotData, words, kind, line, number))
			return false;

		return true;
	}

	/**
	 * @param words
	 * @param indices
	 * @param kind
	 * @param name
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean read_spot(String[] words, Vector<int[]> indices, String kind, String name, String line, int number) {
//		boolean exist = false;

		if ( !EntityData.is_valid_name( words[ 1], Constant._prohibitedCharacters2, number, _warning))
			return false;

		int max = CommonTool.get_max( indices);

		SpotObject spotObject = LayerManager.get_instance().get_spot( words[ 1]);
		if ( null != spotObject) {
			if ( !spotObject.is_multi()) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			if ( LayerManager.get_instance().contains( "spot", words[ 1], String.valueOf( max), words[ 1])) {
				on_invalid_line_error( number, _warning);
				return false;
			}

//			exist = true;

		} else {
			if ( LayerManager.get_instance().contains( "spot", words[ 1], String.valueOf( max), null)) {
				on_invalid_line_error( number, _warning);
				return false;
			}
		}

		EntityData spotData = _spotDataMap.get( words[ 1]);
		if ( null != spotData) {
			if ( !spotData.is_multi()) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			// TODO
			if ( _spotDataMap.contains( words[ 1], String.valueOf( max), words[ 1]) || _agentDataMap.contains( words[ 1], String.valueOf( max), words[ 1])
				|| LayerManager.get_instance().contains_as_object_name( words[ 1], String.valueOf( max))
				|| _spotDataMap.contains_as_object_name( words[ 1], String.valueOf( max)) || _agentDataMap.contains_as_object_name( words[ 1], String.valueOf( max))) {
				on_invalid_line_error( number, _warning);
				return false;
			}
		} else {
			// TODO 
			if ( _spotDataMap.contains( words[ 1], String.valueOf( max), null) || _agentDataMap.contains( words[ 1], String.valueOf( max), null)
				|| LayerManager.get_instance().contains_as_object_name( words[ 1], String.valueOf( max))
				|| _spotDataMap.contains_as_object_name( words[ 1], String.valueOf( max)) || _agentDataMap.contains_as_object_name( words[ 1], String.valueOf( max))) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			if ( null != spotObject)
				spotData = new EntityData( "spot", words[ 1]);
			else {
				_spotPosition.x += 30;
				spotData = new EntityData( "spot", words[ 1], _spotPosition);
			}

			_spotDataMap.put( words[ 1], spotData);
		}

		spotData.set_number( max);

		if ( !set( spotData, words, indices, kind, line, number))
			return false;

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param kind
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean set(EntityData entityData, String[] words, String kind, String line, int number) {
		if ( kind.equals( ResourceManager.get_instance().get( "initial.data.initial.role"))) {
			if ( !set_initial_role( entityData, words, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.probability"))) {
			if ( !append_probability( entityData, words, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.collection"))) {
			if ( !append_collection( entityData, words, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.list"))) {
			if ( !append_list( entityData, words, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.map"))) {
			if ( !append_map( entityData, words, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.keyword"))) {
			if ( !append_keyword( entityData, words, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.integer.number.object"))) {
			if ( !append_number_object( entityData, words, "integer", line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.real.number.object"))) {
			if ( !append_number_object( entityData, words, "real number", line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.role.variable"))) {
			if ( !append_role_variable( entityData, words, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.time.variable"))) {
			if ( !append_time_variable( entityData, words, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.spot.variable"))) {
			if ( !append_spot_variable( entityData, words, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.class.variable"))) {
			if ( !append_class_variable( entityData, words, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.file"))) {
			if ( !append_file( entityData, words, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.exchange.algebra"))) {
			if ( !append_exchange_algebra( entityData, words, line, number))
				return false;
		} else {
			on_invalid_line_error( number, _warning);
			return false;
		}

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean set_initial_role(EntityData entityData, String[] words, String line, int number) {
		if ( !EntityData.is_valid_name( words[ 3],
			entityData._type.equals( "agent") ? Constant._prohibitedCharacters2 : Constant._prohibitedCharacters6,
			number, _warning))
			return false;

		entityData._initialRole = words[ 3];

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_probability(EntityData entityData, String[] words, String line, int number) {
		if ( !Constant.is_correct_name( words[ 3])
			|| !EntityData.is_valid_name( words[ 3], Constant._prohibitedCharacters4, number, _warning))
			return false;

		if ( has_same_name( words[ 3], "Probability", line, number))
			return false;

		if ( 4 < words.length
			&& ( !EntityData.is_valid_value5( words[ 4], number, _warning)
			|| !CommonTool.is_probability_correct( words[ 4]))) {
			on_invalid_value_error( number, _warning);
			return false;
		}

		entityData._objectMapMap.get( "probability").put( words[ 3], new ProbabilityObject( words[ 3], ( ( 4 < words.length) ? words[ 4] : ""), ""));

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_collection(EntityData entityData, String[] words, String line, int number) {
		if ( !Constant.is_correct_name( words[ 3])
			|| !EntityData.is_valid_name( words[ 3], Constant._prohibitedCharacters1, number, _warning))
			return false;

		if ( has_same_name( words[ 3], "Collection", line, number))
			return false;

		if ( !entityData.append_collection( words[ 3], words, number, _warning))
			return false;

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_list(EntityData entityData, String[] words, String line, int number) {
		if ( !Constant.is_correct_name( words[ 3])
			|| !EntityData.is_valid_name( words[ 3], Constant._prohibitedCharacters1, number, _warning))
			return false;

		if ( has_same_name( words[ 3], "List", line, number))
			return false;

		if ( !entityData.append_list( words[ 3], words, number, _warning))
			return false;

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_map(EntityData entityData, String[] words, String line, int number) {
		if ( !Constant.is_correct_name( words[ 3])
			|| !EntityData.is_valid_name( words[ 3], Constant._prohibitedCharacters1, number, _warning))
			return false;

		if ( has_same_name( words[ 3], "Map", line, number))
			return false;

		if ( !entityData.append_map( words[ 3], words, number, _warning))
			return false;

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_keyword(EntityData entityData, String[] words, String line, int number) {
		if ( !Constant.is_correct_name( words[ 3])
			|| !EntityData.is_valid_name( words[ 3], Constant._prohibitedCharacters1, number, _warning))
			return false;

		if ( has_same_name( words[ 3], "Keyword", line, number))
			return false;

		if ( 4 < words.length && !EntityData.is_valid_value3( words[ 4], number, _warning))
			return false;

		entityData._objectMapMap.get( "keyword").put( words[ 3], new KeywordObject( words[ 3], ( ( 4 < words.length) ? words[ 4] : ""), ""));

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param number_object_type
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_number_object(EntityData entityData, String[] words, String number_object_type, String line, int number) {
		if ( !Constant.is_correct_name( words[ 3])
			|| !EntityData.is_valid_name( words[ 3], Constant._prohibitedCharacters4, number, _warning))
			return false;

		if ( !LayerManager.get_instance().is_number_object_correct( entityData._type, words[ 3], number_object_type, null)
			|| !is_number_object_correct( entityData._type, words[ 3], number_object_type)) {
			on_invalid_value_error( number, _warning);
			return false;
		}

		if ( has_same_name( words[ 3], "Number object", line, number))
			return false;

		NumberObject numberObject;
		if ( 4 < words.length) {
			if ( !EntityData.is_valid_value5( words[ 4], number, _warning))
				return false;

			words[ 4] = NumberObject.is_correct( words[ 4], number_object_type);
			if ( null == words[ 4]) {
				on_invalid_value_error( number, _warning);
				return false;
			}

			numberObject = new NumberObject( words[ 3], number_object_type, words[ 4]);
		} else
			numberObject = new NumberObject( words[ 3], number_object_type, "");

		entityData._objectMapMap.get( "number object").put( words[ 3], numberObject);
		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_role_variable(EntityData entityData, String[] words, String line, int number) {
		if ( !Constant.is_correct_name( words[ 3])
			|| !EntityData.is_valid_name( words[ 3], Constant._prohibitedCharacters2, number, _warning))
			return false;

		if ( has_same_name( words[ 3], "Role variable", line, number))
			return false;

		if ( 4 < words.length && !EntityData.is_valid_value6( words[ 4], number, _warning)) {
			on_invalid_value_error( number, _warning);
			return false;
		}

		entityData._objectMapMap.get( "role variable").put( words[ 3], new RoleVariableObject( words[ 3], ( ( 4 < words.length) ? words[ 4] : ""), ""));

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_time_variable(EntityData entityData, String[] words, String line, int number) {
		if ( !Constant.is_correct_name( words[ 3])
			|| !EntityData.is_valid_name( words[ 3], Constant._prohibitedCharacters13, number, _warning))
			return false;

		if ( has_same_name( words[ 3], "Time variable", line, number))
			return false;

		if ( 4 < words.length) {
			String value = EntityData.transform_time_variable_initial_value( words[ 4], number, _warning);
			if ( null == value)
				return false;

			words[ 4] = value;
		}

		entityData._objectMapMap.get( "time variable").put( words[ 3], new TimeVariableObject( words[ 3], ( ( 4 < words.length) ? words[ 4] : "0:00")));

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_spot_variable(EntityData entityData, String[] words, String line, int number) {
		if ( !Constant.is_correct_name( words[ 3])
			|| !EntityData.is_valid_name( words[ 3], Constant._prohibitedCharacters1, number, _warning))
			return false;

		if ( has_same_name( words[ 3], "Spot variable", line, number))
			return false;

		if ( 4 < words.length && !EntityData.is_valid_value1( words[ 4], number, _warning))
			return false;

		entityData._objectMapMap.get( "spot variable").put( words[ 3], new SpotVariableObject( words[ 3], ( ( 4 < words.length) ? words[ 4] : ""), ""));

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_class_variable(EntityData entityData, String[] words, String line, int number) {
		if ( !Environment.get_instance().is_functional_object_enable())
			return true;

		if ( !Constant.is_correct_name( words[ 3])
			|| !EntityData.is_valid_name( words[ 3], Constant._prohibitedCharacters1, number, _warning))
			return false;

		if ( has_same_name( words[ 3], "Class variable", line, number))
			return false;

		String jar_filename = get_initial_data_jar_filename( words[ 4]);
		if ( null == jar_filename || jar_filename.equals( ""))
			return false;

		//if ( !JarFileProperties.get_instance().contains( jar_filename, words[ 5])) {
		if ( !JarFileProperties.get_instance().exist( jar_filename, words[ 5], _appendedJavaClassList)) {
			on_invalid_value_error( number, _warning);
			return false;
		}

		if ( uses_this_class_variable_as_different_class( entityData, words[ 3], jar_filename, words[ 5], number))
			return false;

		if ( !entityData.append_class_variable( words[ 3], jar_filename, words[ 5], number)) {
			on_invalid_value_error( number, _warning);
			return false;
		}

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_file(EntityData entityData, String[] words, String line, int number) {
		if ( !Constant.is_correct_name( words[ 3])
			|| !EntityData.is_valid_name( words[ 3], Constant._prohibitedCharacters1, number, _warning))
			return false;

		if ( has_same_name( words[ 3], "File", line, number))
			return false;

		if ( 4 < words.length) {
			words[ 4] = EntityData.normalize_file_path( words[ 4]);
			if ( !words[ 4].equals( "")
				&& ( !EntityData.is_valid_value17( words[ 4], number, _warning) || !EntityData.is_valid_file( words[ 4], number, _warning)))
				return false;
		}

		entityData._objectMapMap.get( "file").put( words[ 3], new FileObject( words[ 3], ( ( 4 < words.length) ? words[ 4] : ""), ""));

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_exchange_algebra(EntityData entityData, String[] words, String line, int number) {
		if ( !Environment.get_instance().is_exchange_algebra_enable())
			return true;

		if ( !Constant.is_correct_name( words[ 3])
			|| !EntityData.is_valid_name( words[ 3], Constant._prohibitedCharacters1, number, _warning))
			return false;

		if ( has_same_name( words[ 3], "Exchange algebra", line, number))
			return false;

		if ( !entityData.append_exchange_algebra( words[ 3], words, number, _warning)) {
			on_invalid_value_error( number, _warning);
			return false;
		}

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param indices
	 * @param kind
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean set(EntityData entityData, String[] words, Vector<int[]> indices, String kind, String line, int number) {
		if ( kind.equals( ResourceManager.get_instance().get( "initial.data.probability"))) {
			if ( !append_probability( entityData, words, indices, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.collection"))) {
			if ( !append_collection( entityData, words, indices, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.list"))) {
			if ( !append_list( entityData, words, indices, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.map"))) {
			if ( !append_map( entityData, words, indices, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.keyword"))) {
			if ( !append_keyword( entityData, words, indices, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.integer.number.object"))) {
			if ( !append_number_object( entityData, words, "integer", indices, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.real.number.object"))) {
			if ( !append_number_object( entityData, words, "real number", indices, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.role.variable"))) {
			if ( !append_role_variable( entityData, words, indices, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.time.variable"))) {
			if ( !append_time_variable( entityData, words, indices, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.spot.variable"))) {
			if ( !append_spot_variable( entityData, words, indices, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.class.variable"))) {
			if ( !append_class_variable( entityData, words, indices, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.file"))) {
			if ( !append_file( entityData, words, indices, line, number))
				return false;
		} else if ( kind.equals( ResourceManager.get_instance().get( "initial.data.exchange.algebra"))) {
			if ( !append_exchange_algebra( entityData, words, indices, line, number))
				return false;
		} else {
			on_invalid_line_error( number, _warning);
			return false;
		}

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param indices
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_probability(EntityData entityData, String[] words, Vector<int[]> indices, String line, int number) {
		if ( !Constant.is_correct_name( words[ 5])
			|| !EntityData.is_valid_name( words[ 5], Constant._prohibitedCharacters4, number, _warning))
			return false;

		if ( has_same_name( words[ 5], "Probability", line, number))
			return false;

		if ( !entityData.append_probability( words, indices, number, _warning))
			return false;

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param indices
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_collection(EntityData entityData, String[] words, Vector<int[]> indices, String line, int number) {
		if ( !Constant.is_correct_name( words[ 5])
			|| !EntityData.is_valid_name( words[ 5], Constant._prohibitedCharacters1, number, _warning))
			return false;

		if ( has_same_name( words[ 5], "Collection", line, number))
			return false;

		if ( !entityData.append_collection( words, indices, number, _warning))
			return false;

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param indices
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_list(EntityData entityData, String[] words, Vector<int[]> indices, String line, int number) {
		if ( !Constant.is_correct_name( words[ 5])
			|| !EntityData.is_valid_name( words[ 5], Constant._prohibitedCharacters1, number, _warning))
			return false;

		if ( has_same_name( words[ 5], "List", line, number))
			return false;

		if ( !entityData.append_list( words, indices, number, _warning))
			return false;

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param indices
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_map(EntityData entityData, String[] words, Vector<int[]> indices, String line, int number) {
		if ( !Constant.is_correct_name( words[ 5])
			|| !EntityData.is_valid_name( words[ 5], Constant._prohibitedCharacters1, number, _warning))
			return false;

		if ( has_same_name( words[ 5], "Map", line, number))
			return false;

		if ( !entityData.append_map( words, indices, number, _warning))
			return false;

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param indices
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_keyword(EntityData entityData, String[] words, Vector<int[]> indices, String line, int number) {
		if ( !Constant.is_correct_name( words[ 5])
			|| !EntityData.is_valid_name( words[ 5], Constant._prohibitedCharacters1, number, _warning))
			return false;

		if ( has_same_name( words[ 5], "Keyword", line, number))
			return false;

		if ( !entityData.append_keyword( words, indices, number, _warning))
			return false;

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param number_object_type
	 * @param indices
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_number_object(EntityData entityData, String[] words, String number_object_type, Vector<int[]> indices, String line, int number) {
		if ( !Constant.is_correct_name( words[ 5])
			|| !EntityData.is_valid_name( words[ 5], Constant._prohibitedCharacters4, number, _warning))
			return false;

		if ( !LayerManager.get_instance().is_number_object_correct( entityData._type, words[ 5], number_object_type, null)
			|| !is_number_object_correct( entityData._type, words[ 5], number_object_type)) {
			on_invalid_value_error( number, _warning);
			return false;
		}

		if ( has_same_name( words[ 5], "Number object", line, number))
			return false;

		String value;
		if ( 6 < words.length) {
			if ( !EntityData.is_valid_value5( words[ 6], number, _warning))
				return false;

			words[ 6] = NumberObject.is_correct( words[ 6], number_object_type);
			if ( null == words[ 6]) {
				on_invalid_value_error( number, _warning);
				return false;
			}

			value = words[ 6];
		} else
			value = "";

		entityData.append_number_object( words[ 5], number_object_type, value, indices);
		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param indices
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_role_variable(EntityData entityData, String[] words, Vector<int[]> indices, String line, int number) {
		if ( !Constant.is_correct_name( words[ 5])
			|| !EntityData.is_valid_name( words[ 5], Constant._prohibitedCharacters2, number, _warning))
			return false;

		if ( has_same_name( words[ 5], "Role variable", line, number))
			return false;

		if ( !entityData.append_role_variable( words, indices, number, _warning))
			return false;

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param indices
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_time_variable(EntityData entityData, String[] words, Vector<int[]> indices, String line, int number) {
		if ( !Constant.is_correct_name( words[ 5])
			|| !EntityData.is_valid_name( words[ 5], Constant._prohibitedCharacters13, number, _warning))
			return false;

		if ( has_same_name( words[ 5], "Time variable", line, number))
			return false;

		if ( !entityData.append_time_variable( words, indices, number, _warning))
			return false;

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param indices
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_spot_variable(EntityData entityData, String[] words, Vector<int[]> indices, String line, int number) {
		if ( !Constant.is_correct_name( words[ 5])
			|| !EntityData.is_valid_name( words[ 5], Constant._prohibitedCharacters1, number, _warning))
			return false;

		if ( has_same_name( words[ 5], "Spot variable", line, number))
			return false;

		if ( !entityData.append_spot_variable( words, indices, number, _warning))
			return false;

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param indices
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_class_variable(EntityData entityData, String[] words, Vector<int[]> indices, String line, int number) {
		if ( !Environment.get_instance().is_functional_object_enable())
			return true;

		if ( !Constant.is_correct_name( words[ 5])
			|| !EntityData.is_valid_name( words[ 5], Constant._prohibitedCharacters1, number, _warning))
			return false;

		if ( has_same_name( words[ 5], "Class variable", line, number))
			return false;

		String jar_filename = get_initial_data_jar_filename( words[ 6]);
		if ( null == jar_filename || jar_filename.equals( ""))
			return false;

		//if ( !JarFileProperties.get_instance().contains( jar_filename, words[ 7])) {
		if ( !JarFileProperties.get_instance().exist( jar_filename, words[ 7], _appendedJavaClassList)) {
			on_invalid_value_error( number, _warning);
			return false;
		}

		if ( uses_this_class_variable_as_different_class( entityData, words[ 5], jar_filename, words[ 7], number))
			return false;

		if ( !entityData.append_class_variable( words[ 5], jar_filename, words[ 7], indices)) {
			on_invalid_value_error( number, _warning);
			return false;
		}

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param indices
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_file(EntityData entityData, String[] words, Vector<int[]> indices, String line, int number) {
		if ( !Constant.is_correct_name( words[ 5])
			|| !EntityData.is_valid_name( words[ 5], Constant._prohibitedCharacters1, number, _warning))
			return false;

		if ( has_same_name( words[ 5], "File", line, number))
			return false;

		if ( !entityData.append_file( words, indices, number, _warning))
			return false;

		return true;
	}

	/**
	 * @param entityData
	 * @param words
	 * @param indices
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean append_exchange_algebra(EntityData entityData, String[] words, Vector<int[]> indices, String line, int number) {
		if ( !Environment.get_instance().is_exchange_algebra_enable())
			return true;

		if ( !Constant.is_correct_name( words[ 5])
			|| !EntityData.is_valid_name( words[ 5], Constant._prohibitedCharacters1, number, _warning))
			return false;

		if ( has_same_name( words[ 5], "Exchange algebra", line, number))
			return false;

		if ( !entityData.append_exchange_algebra( words[ 5], words, indices, number, _warning)) {
			on_invalid_value_error( number, _warning);
			return false;
		}

		return true;
	}

	/**
	 * @param name
	 * @param type
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean has_same_name(String name, String type, String line, int number) {
		// TODO
		if ( LayerManager.get_instance().contains( "agent", name)
			|| LayerManager.get_instance().contains( "spot", name)
			|| _agentDataMap.contains( name)
			|| _spotDataMap.contains( name))
			return true;

		if ( has_same_name( _agentDataMap, name, type, line, number))
			return true;

		if ( has_same_name( _spotDataMap, name, type, line, number))
			return true;

		return false;
	}

	/**
	 * @param entityDataMap
	 * @param name
	 * @param type
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean has_same_name(EntityDataMap entityDataMap, String name, String type, String line, int number) {
		Iterator iterator = entityDataMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			EntityData entityData = ( EntityData)entry.getValue();

			if ( type.equals( "Probability")
				&& has_same_name( "probability", name, entityData)) {
				on_invalid_duplicated_value_error( number, _warning);
				return true;
			} else if ( type.equals( "Collection")
				&& has_same_name( "collection", name, entityData)) {
				on_invalid_duplicated_value_error( number, _warning);
				return true;
			} else if ( type.equals( "List")
				&& has_same_name( "list", name, entityData)) {
				on_invalid_duplicated_value_error( number, _warning);
				return true;
			} else if ( type.equals( "Map")
				&& has_same_name( "map", name, entityData)) {
				on_invalid_duplicated_value_error( number, _warning);
				return true;
			} else if ( type.equals( "Keyword")
				&& has_same_name( "keyword", name, entityData)) {
				on_invalid_duplicated_value_error( number, _warning);
				return true;
			} else if ( type.equals( "Number object")
				&& has_same_name( "number object", name, entityData)) {
				on_invalid_duplicated_value_error( number, _warning);
				return true;
			} else if ( type.equals( "Role variable")
				&& has_same_name( "role variable", name, entityData)) {
				on_invalid_duplicated_value_error( number, _warning);
				return true;
			} else if ( type.equals( "Time variable")
				&& has_same_name( "time variable", name, entityData)) {
				on_invalid_duplicated_value_error( number, _warning);
				return true;
			} else if ( type.equals( "Class variable")
				&& has_same_name( "class variable", name, entityData)) {
				on_invalid_duplicated_value_error( number, _warning);
				return true;
			} else if ( type.equals( "Spot variable")
				&& has_same_name( "spot variable", name, entityData)) {
				on_invalid_duplicated_value_error( number, _warning);
				return true;
			} else if ( type.equals( "File")
				&& has_same_name( "file", name, entityData)) {
				on_invalid_duplicated_value_error( number, _warning);
				return true;
			} else if ( type.equals( "Exchange algebra")
				&& has_same_name( "exchange algebra", name, entityData)) {
				on_invalid_duplicated_value_error( number, _warning);
				return true;
			} else
				continue;
		}
		return false;
	}

	/**
	 * @param kind
	 * @param name
	 * @param entityData
	 * @return
	 */
	private boolean has_same_name(String kind, String name, EntityData entityData) {
		if ( null != LayerManager.get_instance().get_chart( name))
			return true;

		for ( int i = 0; i < Constant._kinds.length; ++i) {
			if ( Constant._kinds[ i].equals( kind))
				continue;

			if ( LayerManager.get_instance().is_object_name( Constant._kinds[ i], name))
				return true;

			if ( entityData.has_same_object_name( Constant._kinds[ i], name))
				return true;
		}

		return false;
	}

	/**
	 * @param type
	 * @param name
	 * @param numberObjectType
	 * @return
	 */
	private boolean is_number_object_correct(String type, String name, String numberObjectType) {
		if ( type.equals( "agent") && !is_number_object_correct( _agentDataMap, name, numberObjectType))
			return false;

		if ( type.equals( "spot") && !is_number_object_correct( _spotDataMap, name, numberObjectType))
			return false;

		return true;
	}

	/**
	 * @param entityDataMap
	 * @param name
	 * @param numberObjectType
	 * @return
	 */
	private boolean is_number_object_correct(EntityDataMap entityDataMap, String name, String numberObjectType) {
		Iterator iterator = entityDataMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			EntityData entityData = ( EntityData)entry.getValue();
			if ( !entityData.is_number_object_correct( name, numberObjectType))
				return false;
		}
		return true;
	}

	/**
	 * @param jarFilename
	 * @return
	 */
	private String get_initial_data_jar_filename(String jarFilename) {
		if ( jarFilename.equals( Constant._javaClasses))
			return jarFilename;

		if ( jarFilename.equals( "") || jarFilename.equals( "/"))
			return null;

//		return ( Constant._functional_object_directories[ 0]
//			+ ( jar_filename.startsWith( "/") ? "" : "/")
//			+ jar_filename);

		for ( int i = 0; i < Constant._functionalObjectDirectories.length; ++i) {
			String filename = ( Constant._functionalObjectDirectories[ i] + ( jarFilename.startsWith( "/") ? "" : "/") + jarFilename);
			File file = new File( filename);
			if ( !file.exists())
				continue;

			return filename;
		}

		return null;
	}

	/**
	 * @param entityData
	 * @param name
	 * @param jarFilename
	 * @param classname
	 * @param number
	 * @return
	 */
	private boolean uses_this_class_variable_as_different_class(EntityData entityData, String name, String jarFilename, String classname, int number) {
		if ( entityData._type.equals( "agent"))
			return _agentDataMap.uses_this_class_variable_as_different_class( entityData, name, jarFilename, classname, number, _warning);
		else if ( entityData._type.equals( "spot"))
			return _spotDataMap.uses_this_class_variable_as_different_class( entityData, name, jarFilename, classname, number, _warning);

		return false;
	}

	/**
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean read_agent_role(String[] words, String line, int number) {
		if ( !words[ 2].equals( "condition")
			/*|| words[ 3].equals( "")*/) {
			on_invalid_line_error( number, _warning);
			return false;
		}

		if ( !EntityData.is_valid_name( words[ 1], Constant._prohibitedCharacters9, number, _warning))
			return false;

		String[] names = null;
		if ( words[ 1].equals( ""))
			names = new String[] { ""};
		else {
			names = words[ 1].split( ":");
			if ( null == names || 0 == names.length)
				return false;
		}

		for ( int i = 0; i < names.length; ++i) {
			if ( !_agentRoleDataMap.is_correct( names[ i])) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			if ( _spotRoleDataMap.contains( names[ i])) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			Role role = LayerManager.get_instance().get_agent_role( names[ i]);

			RoleData agentRoleData = ( RoleData)_agentRoleDataMap.get( names[ i]);
			if ( null == agentRoleData) {
				if ( null != role)
					agentRoleData = new RoleData( "agent_role", names[ i]);
				else {
					_agentRolePosition.x += 30;
					agentRoleData = new RoleData( "agent_role", names[ i], _agentRolePosition);
				}
			}

			if ( 0 == i && !agentRoleData.append( words)) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			_agentRoleDataMap.put( names[ i], agentRoleData);

			if ( 0 == i)
				_agentRoleConnectionMap.put( agentRoleData, names);
		}

		return true;
	}

	/**
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean read_spot_role(String[] words, String line, int number) {
		if ( !words[ 2].equals( "condition")
			/*|| words[ 3].equals( "")*/) {
			on_invalid_line_error( number, _warning);
			return false;
		}

		if ( !EntityData.is_valid_name( words[ 1], Constant._prohibitedCharacters10, number, _warning))
			return false;

		String[] names = null;
		if ( words[ 1].equals( ""))
			names = new String[] { ""};
		else {
			names = words[ 1].split( ":");
			if ( null == names || 0 == names.length)
				return false;
		}

		for ( int i = 0; i < names.length; ++i) {
			if ( !_spotRoleDataMap.is_correct( names[ i])) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			if ( _agentRoleDataMap.contains( names[ i])) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			Role role = LayerManager.get_instance().get_spot_role( names[ i]);

			RoleData spotRoleData = ( RoleData)_spotRoleDataMap.get( names[ i]);
			if ( null == spotRoleData) {
				if ( null != role)
					spotRoleData = new RoleData( "spot_role", names[ i]);
				else {
					_spotRolePosition.x += 30;
					spotRoleData = new RoleData( "spot_role", names[ i], _spotRolePosition);
				}
			}

			if ( 0 == i && !spotRoleData.append( words)) {
				on_invalid_line_error( number, _warning);
				return false;
			}

			_spotRoleDataMap.put( names[ i], spotRoleData);

			if ( 0 == i)
				_spotRoleConnectionMap.put( spotRoleData, names);
		}

		return true;
	}

	/**
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean read_initial_stage(String[] words, String line, int number) {
		if ( !EntityData.is_valid_name( words[ 1], Constant._prohibitedCharacters1, number, _warning))
			return false;

		if ( _mainStageMap.contains( words[ 1])
			|| _terminalStageMap.contains( words[ 1])) {
			on_invalid_line_error( number, _warning);
			return false;
		}

		return _initialStageMap.append( words, line, number);
	}

	/**
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean read_main_stage(String[] words, String line, int number) {
		if ( !EntityData.is_valid_name( words[ 1], Constant._prohibitedCharacters1, number, _warning))
			return false;

		if ( _initialStageMap.contains( words[ 1])
			|| _terminalStageMap.contains( words[ 1])) {
			on_invalid_line_error( number, _warning);
			return false;
		}

		return _mainStageMap.append( words, line, number);
	}

	/**
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean read_terminal_stage(String[] words, String line, int number) {
		if ( !EntityData.is_valid_name( words[ 1], Constant._prohibitedCharacters1, number, _warning))
			return false;

		if ( _initialStageMap.contains( words[ 1])
			|| _mainStageMap.contains( words[ 1])) {
			on_invalid_line_error( number, _warning);
			return false;
		}

		return _terminalStageMap.append( words, line, number);
	}

	/**
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean read_simulation(String[] words, String line, int number) {
		if ( !_simulationDataMap.append( words, line, number)) {
			on_invalid_line_error( number, _warning);
			return false;
		}

		return true;
	}

	/**
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean read_expression(String[] words, String line, int number) {
		if ( !_visualShellExpressionDataMap.append( words, line, number)) {
			on_invalid_line_error( number, _warning);
			return false;
		}

		return true;
	}

	/**
	 * @param words
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean read_comment(String[] words, String line, int number) {
		if ( !_commentDataMap.append( words, line, number)) {
			on_invalid_line_error( number, _warning);
			return false;
		}

		return true;
	}

	/**
	 * Displays the error message.
	 * @param number the error line number
	 * @param warning whether to display the warning messages
	 */
	public static void on_invalid_line_error(int number, boolean warning) {
		if ( !warning)
			return;

		JOptionPane.showMessageDialog(
			MainFrame.get_instance(),
			ResourceManager.get_instance().get( "initial.data.invalid.line.error.message") + " : line number " + number,
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Displays the error message.
	 * @param number the error line number
	 * @param warning whether to display the warning messages
	 */
	public static void on_invalid_value_error(int number, boolean warning) {
		if ( !warning)
			return;

		JOptionPane.showMessageDialog(
			MainFrame.get_instance(),
			ResourceManager.get_instance().get( "initial.data.invalid.value.error.message") + " : line number " + number,
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Displays the error message.
	 * @param number the error line number
	 * @param warning whether to display the warning messages
	 */
	public static void on_invalid_duplicated_value_error(int number, boolean warning) {
		if ( !warning)
			return;

		JOptionPane.showMessageDialog(
			MainFrame.get_instance(),
			ResourceManager.get_instance().get( "initial.data.duplicated.value.error.message") + " : line number " + number,
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param frame
	 * @param title
	 * @param message
	 * @param warning
	 */
	private static void warning(JFrame frame, String title, String message, boolean warning) {
		if ( !warning)
			return;

		if ( !WarningManager.get_instance().isEmpty()) {
			WarningDlg1 warningDlg1 = new WarningDlg1( frame, title, message, frame);
			warningDlg1.do_modal();
		}
	}
}
