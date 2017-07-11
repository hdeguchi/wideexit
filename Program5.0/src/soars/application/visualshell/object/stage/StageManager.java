/*
 * 2005/05/01
 */
package soars.application.visualshell.object.stage;

import java.awt.Frame;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.stage.edit.EditStageDlg;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 */
public class StageManager {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private StageManager _stageManager = null;

	/**
	 * 
	 */
	public Vector<Stage> _mainStages = new Vector<Stage>();

	/**
	 * 
	 */
	public Vector<Stage> _initialStages = new Vector<Stage>();

	/**
	 * 
	 */
	public Vector<Stage> _terminalStages = new Vector<Stage>();

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
			if ( null == _stageManager) {
				_stageManager = new StageManager();
			}
		}
	}

	/**
	 * @return
	 */
	public static StageManager get_instance() {
		if ( null == _stageManager) {
			System.exit( 0);
		}

		return _stageManager;
	}

	/**
	 * 
	 */
	public StageManager() {
		super();
	}

	/**
	 * @param stageManager
	 */
	public StageManager(StageManager stageManager) {
		super();
		copy( stageManager);
	}

	/**
	 * @param stageManager
	 */
	private void copy(StageManager stageManager) {
		for ( Stage stage:stageManager._mainStages)
			_mainStages.add( new Stage( stage));
		for ( Stage stage:stageManager._initialStages)
			_initialStages.add( new Stage( stage));
		for ( Stage stage:stageManager._terminalStages)
			_terminalStages.add( new Stage( stage));
	}

	/**
	 * @param name
	 * @return
	 */
	public Stage get(String name) {
		for ( Stage stage:_mainStages) {
			if ( stage._name.equals( name))
				return stage;
		}
		for ( Stage stage:_initialStages) {
			if ( stage._name.equals( name))
				return stage;
		}
		for ( Stage stage:_terminalStages) {
			if ( stage._name.equals( name))
				return stage;
		}
		return null;
	}

	/**
	 * @param name
	 */
	public void append_main_stage(String name) {
		if ( contains_this_name( name))
			return;

		append_main_stage( new Stage( name));
	}

	/**
	 * @param stage
	 */
	public void append_main_stage(Stage stage) {
		if ( !contains( "main stage", stage._name))
			_mainStages.add( stage);
		while ( remove_this_name( _initialStages, stage._name))
			;
		while ( remove_this_name( _terminalStages, stage._name))
			;
	}

	/**
	 * @param stage
	 */
	public void append_initial_stage(Stage stage) {
		if ( !contains( "initial stage", stage._name))
			_initialStages.add( stage);
		while ( remove_this_name( _mainStages, stage._name))
			;
		while ( remove_this_name( _terminalStages, stage._name))
			;
	}

	/**
	 * @param stage
	 */
	public void append_terminal_stage(Stage stage) {
		if ( !contains( "terminal stage", stage._name))
			_terminalStages.add( stage);
		while ( remove_this_name( _initialStages, stage._name))
			;
		while ( remove_this_name( _mainStages, stage._name))
			;
	}

	/**
	 * @param stageManager
	 */
	public void set(StageManager stageManager) {
		cleanup();

		for ( Stage stage:stageManager._mainStages)
			_mainStages.add( stage);

		for ( Stage stage:stageManager._initialStages)
			_initialStages.add( stage);

		for ( Stage stage:stageManager._terminalStages)
			_terminalStages.add( stage);
	}

	/**
	 * 
	 */
	public void cleanup() {
		for ( Stage stage:_mainStages)
			stage.cleanup();
		_mainStages.clear();

		for ( Stage stage:_initialStages)
			stage.cleanup();
		_initialStages.clear();

		for ( Stage stage:_terminalStages)
			stage.cleanup();
		_terminalStages.clear();
	}

	/**
	 * 
	 */
	public void update() {
		if ( LayerManager.get_instance().contains_available_chartObject()) {
			if ( !contains_this_name( Constant._initializeChartStageName)) {
				Stage stage = new Stage( Constant._initializeChartStageName);
				_initialStages.insertElementAt( stage, 0);
			}
			if ( !contains_this_name( Constant._updateChartStageName))
				_mainStages.add( new Stage( Constant._updateChartStageName));
		} else {
			if ( contains_this_name( Constant._initializeChartStageName))
				remove_this_name( Constant._initializeChartStageName);
			if ( contains_this_name( Constant._updateChartStageName))
				remove_this_name( Constant._updateChartStageName);
		}
	}

	/**
	 * @param type
	 * @param name
	 * @return
	 */
	public boolean contains(String type, String name) {
		if ( type.equals( "initial stage"))
			return contains( name, _initialStages);
		else if ( type.equals( "main stage"))
			return contains( name, _mainStages);
		else if ( type.equals( "terminal stage"))
			return contains( name, _terminalStages);
		else
			return false;
	}

	/**
	 * @param name
	 * @return
	 */
	private boolean contains_this_name(String name) {
		if ( contains( name, _initialStages))
			return true;
		if ( contains( name, _mainStages))
			return true;
		if ( contains( name, _terminalStages))
			return true;
		return false;
	}

	/**
	 * @param name
	 * @param stages
	 * @return
	 */
	private boolean contains(String name, Vector<Stage> stages) {
		for ( Stage stage:stages) {
			if ( stage._name.equals( name))
				return true;
		}
		return false;
	}

	/**
	 * @param name
	 */
	private void remove_this_name(String name) {
		while ( remove_this_name( _initialStages, name))
			;
		while ( remove_this_name( _terminalStages, name))
			;
		while ( remove_this_name( _mainStages, name))
			;
	}

	/**
	 * @param stages
	 * @param name
	 * @return
	 */
	private boolean remove_this_name(Vector<Stage> stages, String name) {
		for ( int i = 0; i < stages.size(); ++i) {
			if ( stages.get( i)._name.equals( name)) {
				stages.removeElementAt( i);
				return true;
			}
		}
		return false;
	}

	/**
	 * @param newName
	 * @param originalName
	 * @return
	 */
	public boolean rename(String newName, String originalName) {
		for ( Stage stage:_mainStages) {
			if ( stage._name.equals( originalName)) {
				stage._name = newName;
				return true;
			}
		}

		for ( Stage stage:_initialStages) {
			if ( stage._name.equals( originalName)) {
				stage._name = newName;
				return true;
			}
		}

		for ( Stage stage:_terminalStages) {
			if ( stage._name.equals( originalName)) {
				stage._name = newName;
				return true;
			}
		}

		return false;
	}

	/**
	 * @param name
	 * @return
	 */
	public boolean remove(String name) {
		for ( int i = 0; i < _mainStages.size(); ++i) {
			if ( _mainStages.get( i)._name.equals( name)) {
				_mainStages.removeElementAt( i);
				return true;
			}
		}

		for ( int i = 0; i < _initialStages.size(); ++i) {
			if ( _initialStages.get( i)._name.equals( name)) {
				_initialStages.removeElementAt( i);
				return true;
			}
		}

		for ( int i = 0; i < _terminalStages.size(); ++i) {
			if ( _terminalStages.get( i)._name.equals( name)) {
				_terminalStages.removeElementAt( i);
				return true;
			}
		}
		return false;
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public String[] get_names(boolean containsEmpty) {
		Vector<String> names = new Vector<String>();
		for ( Stage stage:_initialStages) {
			if ( stage._name.equals( Constant._initializeChartStageName)
				|| stage._name.equals( Constant._updateChartStageName))
				continue;

			names.add( stage._name);
		}
		for ( Stage stage:_mainStages) {
			if ( stage._name.equals( Constant._initializeChartStageName)
				|| stage._name.equals( Constant._updateChartStageName))
				continue;

			names.add( stage._name);
		}
		for ( Stage stage:_terminalStages) {
			if ( stage._name.equals( Constant._initializeChartStageName)
				|| stage._name.equals( Constant._updateChartStageName))
				continue;

			names.add( stage._name);
		}

		if ( containsEmpty && !names.contains( ""))
			names.insertElementAt( "", 0);

		return ( String[])names.toArray( new String[ 0]);
		//return Tool.quick_sort_string( stages, true, false);
	}

	/**
	 * @param editRoleFrame
	 * @param frame
	 */
	public void edit(EditRoleFrame editRoleFrame, Frame frame) {
		EditStageDlg editStageDlg = new EditStageDlg( frame,
			ResourceManager.get_instance().get( "edit.stage.dialog.title"),
			true,
			this,
			editRoleFrame);
		if ( !editStageDlg.do_modal())
			return;
	}

	/**
	 * @return
	 */
	public String get_initial_data() {
		if ( _mainStages.isEmpty() && _initialStages.isEmpty() && _terminalStages.isEmpty())
			return "";

		String script1 = "";
		for ( Stage stage:_initialStages) {
			if ( stage._name.equals( Constant._initializeChartStageName)
				|| stage._name.equals( Constant._updateChartStageName))
				continue;

			script1 += stage.get_initial_data( ResourceManager.get_instance().get( "initial.data.initial.stage"));
		}

		String script2 = "";
		for ( Stage stage:_mainStages) {
			if ( stage._name.equals( Constant._initializeChartStageName)
				|| stage._name.equals( Constant._updateChartStageName))
				continue;

			script2 += stage.get_initial_data( ResourceManager.get_instance().get( "initial.data.main.stage"));
		}

		String script3 = "";
		for ( Stage stage:_terminalStages) {
			if ( stage._name.equals( Constant._initializeChartStageName)
				|| stage._name.equals( Constant._updateChartStageName))
				continue;

			script3 += stage.get_initial_data( ResourceManager.get_instance().get( "initial.data.terminal.stage"));
		}

		if ( script1.equals( "") && script2.equals( "") && script3.equals( ""))
			return "";

		return ( script1 + script2 + script3 + Constant._lineSeparator);
	}

	/**
	 * @param grid
	 * @param ga
	 * @return
	 */
	public String get_script(boolean grid, boolean ga) {
		// TODO
		if ( _mainStages.isEmpty() && _initialStages.isEmpty() && _terminalStages.isEmpty()
			&& !LayerManager.get_instance().initial_data_file_exists())
			return "";

		String script1 = ( LayerManager.get_instance().initial_data_file_exists() ? ( Constant._initialDataFileStageName + Constant._lineSeparator) : "");
		for ( Stage stage:_initialStages) {
			if ( ( grid || ga) && ( stage._name.equals( Constant._initializeChartStageName)
				|| stage._name.equals( Constant._updateChartStageName)))
				continue;

			script1 += stage.get_script();
		}

		String script2 = "";
		for ( Stage stage:_mainStages) {
			if ( ( grid || ga) && ( stage._name.equals( Constant._initializeChartStageName)
				|| stage._name.equals( Constant._updateChartStageName)))
				continue;

			script2 += stage.get_script();
		}

		String script3 = "";
		for ( Stage stage:_terminalStages) {
			if ( ( grid || ga) && ( stage._name.equals( Constant._initializeChartStageName)
				|| stage._name.equals( Constant._updateChartStageName)))
				continue;

			script3 += stage.get_script();
		}

		String script = "";

		if ( !script1.equals( "")) {
			script += "initialStage" + Constant._lineSeparator;
			script += ( script1 + Constant._lineSeparator);
		}

		if ( !script2.equals( "")) {
			script += "stage" + Constant._lineSeparator;
			script += ( script2 + Constant._lineSeparator);
		}

		if ( !script3.equals( "")) {
			script += "terminalStage" + Constant._lineSeparator;
			script += ( script3 + Constant._lineSeparator);
		}

		return script;
	}

	/**
	 * @param writer
	 * @return
	 */
	public boolean write(Writer writer) throws SAXException {
		if ( _mainStages.isEmpty() && _initialStages.isEmpty() && _terminalStages.isEmpty())
			return true;

		writer.startElement( null, null, "stage_data", new AttributesImpl());

		for ( Stage stage:_mainStages)
			stage.write( "stage", writer);

		for ( Stage stage:_initialStages)
			stage.write( "initial_stage", writer);

		for ( Stage stage:_terminalStages)
			stage.write( "terminal_stage", writer);

		writer.endElement( null, null, "stage_data");

		return true;
	}
}
