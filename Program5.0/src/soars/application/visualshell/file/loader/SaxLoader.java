/*
 * 2005/04/22
 */
package soars.application.visualshell.file.loader;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JComponent;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.arbitrary.JarFileProperties;
import soars.application.visualshell.object.arbitrary.JavaClasses;
import soars.application.visualshell.object.arbitrary.select.JarAndClass;
import soars.application.visualshell.object.arbitrary.select.JarAndClassMap;
import soars.application.visualshell.object.base.DrawObject;
import soars.application.visualshell.object.chart.ChartObject;
import soars.application.visualshell.object.chart.NumberObjectData;
import soars.application.visualshell.object.comment.CommentManager;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.object.base.ObjectBase;
import soars.application.visualshell.object.entity.base.object.class_variable.ClassVariableObject;
import soars.application.visualshell.object.entity.base.object.exchange_algebra.ExchangeAlgebraInitialValue;
import soars.application.visualshell.object.entity.base.object.exchange_algebra.ExchangeAlgebraObject;
import soars.application.visualshell.object.entity.base.object.extransfer.ExTransferObject;
import soars.application.visualshell.object.entity.base.object.file.FileObject;
import soars.application.visualshell.object.entity.base.object.initial_data_file.InitialDataFileObject;
import soars.application.visualshell.object.entity.base.object.keyword.KeywordObject;
import soars.application.visualshell.object.entity.base.object.map.MapInitialValue;
import soars.application.visualshell.object.entity.base.object.map.MapObject;
import soars.application.visualshell.object.entity.base.object.number.NumberObject;
import soars.application.visualshell.object.entity.base.object.probability.ProbabilityObject;
import soars.application.visualshell.object.entity.base.object.role.RoleVariableObject;
import soars.application.visualshell.object.entity.base.object.spot.SpotVariableObject;
import soars.application.visualshell.object.entity.base.object.time.TimeVariableObject;
import soars.application.visualshell.object.entity.base.object.variable.VariableInitialValue;
import soars.application.visualshell.object.entity.base.object.variable.VariableObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.experiment.ExperimentManager;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.log.LogManager;
import soars.application.visualshell.object.log.LogOption;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.RuleManager;
import soars.application.visualshell.object.role.base.object.Rules;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.generic.GenericRule;
import soars.application.visualshell.object.role.base.object.generic.element.ConstantRule;
import soars.application.visualshell.object.role.base.object.generic.element.EntityVariableRule;
import soars.application.visualshell.object.role.base.object.generic.element.ExpressionRule;
import soars.application.visualshell.object.role.base.object.legacy.command.CreateObjectCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.MoveCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.RoleCommand;
import soars.application.visualshell.object.role.base.object.legacy.command.SetRoleCommand;
import soars.application.visualshell.object.role.base.object.legacy.condition.RoleCondition;
import soars.application.visualshell.object.role.spot.SpotRole;
import soars.application.visualshell.object.scripts.OtherScriptsManager;
import soars.application.visualshell.object.simulation.SimulationManager;
import soars.application.visualshell.object.stage.Stage;
import soars.application.visualshell.object.stage.StageManager;
import soars.common.utility.swing.image.ImageProperty;
import soars.common.utility.swing.image.ImagePropertyManager;
import soars.common.utility.tool.expression.Expression;
import soars.common.utility.tool.resource.Resource;

/**
 * The XML SAX loader for Visual Shell data.
 * @author kurata / SOARS project
 */
public class SaxLoader extends DefaultHandler {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static public Map<String, String> _typeKindMap = null;

	/**
	 * 
	 */
	static public Map<String, String> _kindTypeMap = null;

	/**
	 * 
	 */
	static public Map<String, String> _kindTagMap = null;

	/**
	 * 
	 */
	static public Map<String, String[]> _tagKindMap = null;

	/**
	 * 
	 */
	private Graphics2D _graphics2D = null;

	/**
	 * 
	 */
	private String _version = "1.0";

	/**
	 * 
	 */
	private Stage _stage = null;

	/**
	 * 
	 */
	private Layer _layer = null;

	/**
	 * 
	 */
	private int _layerCounter = 0;

	/**
	 * 
	 */
	private DrawObject _drawObject = null;

	/**
	 * 
	 */
	private boolean _drawObjectComment = false;

	/**
	 * 
	 */
	private boolean _entityBaseOthers = false;

	/**
	 * 
	 */
	private boolean _initialData = false;

	/**
	 * 
	 */
	private int _row = -1;

	/**
	 * 
	 */
	private Map<String, Integer> _columnWidthsMap = new HashMap<String, Integer>();

	/**
	 * 
	 */
	private SimulationManager _simulationManager = null;

	/**
	 * 
	 */
	private LogManager _logManager = null;

	/**
	 * 
	 */
	private VisualShellExpressionManager _visualShellExpressionManager = null;

	/**
	 * 
	 */
	private TreeMap<String, Expression> _expressionMap = null;

	/**
	 * 
	 */
	private OtherScriptsManager _otherScriptsManager = null;

	/**
	 * 
	 */
	private CommentManager _commentManager = null;

	/**
	 * 
	 */
	private ExperimentManager _experimentManager = null;

	/**
	 * 
	 */
	private InitialValueMap _initialValueMap = null;

	/**
	 * 
	 */
	private ObjectBase _objectBase = null;

	/**
	 * 
	 */
	private String _type = null;

	/**
	 * 
	 */
	private String _name = null;

	/**
	 * 
	 */
	private String _comment = "";

	/**
	 * 
	 */
	private List<String> _appendedJavaClassList = new ArrayList<String>();

	/**
	 * 
	 */
	private JarAndClassMap _jarAndClassMap = new JarAndClassMap();

	/**
	 * 
	 */
	private Map<Role, String[]> _agentRoleConnectionMap = null;

	/**
	 * 
	 */
	private Map<Role, String[]> _spotRoleConnectionMap = null;

	/**
	 * 
	 */
	static private boolean _result;

	/**
	 * 
	 */
	private String _chartObjectType = null;

	/**
	 * 
	 */
	private String _chartObjectObjectName = null;

	/**
	 *
	 */
	private TreeMap<String, ImageProperty> _imagePropertyMap = new TreeMap<String, ImageProperty>();

	/**
	 *
	 */
	private boolean _image = false;

	/**
	 *
	 */
	private int _conditionColumnMax = 0;

	/**
	 *
	 */
	// TODO 2012.9.20
	private GenericRule _genericRule = null;

	/**
	 *
	 */
	// TODO 2014.2.5
	private ExpressionRule _expressionRule = null;

	/**
	 *
	 */
	static public boolean _modified;

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
			if ( null == _typeKindMap) {
				_typeKindMap = new HashMap<String, String>();
				_typeKindMap.put( "probability", "probability");
				_typeKindMap.put( "collection", "collection");
				_typeKindMap.put( "list", "list");
				_typeKindMap.put( "map", "map");
				_typeKindMap.put( "keyword", "keyword");
				_typeKindMap.put( "number_object", "number object");
				_typeKindMap.put( "role_variable", "role variable");
				_typeKindMap.put( "time_variable", "time variable");
				_typeKindMap.put( "spot_variable", "spot variable");
				_typeKindMap.put( "class_variable", "class variable");
				_typeKindMap.put( "file", "file");
				_typeKindMap.put( "exchange_algebra", "exchange algebra");
				_typeKindMap.put( "extransfer", "extransfer");
				_typeKindMap.put( "initial_data_file", "initial data file");
			}
			if ( null == _kindTypeMap) {
				_kindTypeMap = new HashMap<String, String>();
				_kindTypeMap.put( "probability", "probability");
				_kindTypeMap.put( "collection", "collection");
				_kindTypeMap.put( "list", "list");
				_kindTypeMap.put( "map", "map");
				_kindTypeMap.put( "keyword", "keyword");
				_kindTypeMap.put( "number object", "number_object");
				_kindTypeMap.put( "role variable", "role_variable");
				_kindTypeMap.put( "time variable", "time_variable");
				_kindTypeMap.put( "spot variable", "spot_variable");
				_kindTypeMap.put( "class variable", "class_variable");
				_kindTypeMap.put( "file", "file");
				_kindTypeMap.put( "exchange algebra", "exchange_algebra");
				_kindTypeMap.put( "extransfer", "extransfer");
				_kindTypeMap.put( "initial data file", "initial_data_file");
			}
			if ( null == _kindTagMap) {
				_kindTagMap = new HashMap<String, String>();
				_kindTagMap.put( "probability", "probability_data");
				_kindTagMap.put( "collection", "collection_data");
				_kindTagMap.put( "list", "list_data");
				_kindTagMap.put( "map", "map_data");
				_kindTagMap.put( "keyword", "keyword_data");
				_kindTagMap.put( "number object", "number_object_data");
				_kindTagMap.put( "role variable", "role_variable_data");
				_kindTagMap.put( "time variable", "time_variable_data");
				_kindTagMap.put( "spot variable", "spot_variable_data");
				_kindTagMap.put( "class variable", "class_variable_data");
				_kindTagMap.put( "file", "file_data");
				_kindTagMap.put( "exchange algebra", "exchange_algebra_data");
				_kindTagMap.put( "extransfer", "extransfer_data");
				_kindTagMap.put( "initial data file", "initial_data_file_data");
			}
			if ( null == _tagKindMap) {
				_tagKindMap = new HashMap<String, String[]>();
				_tagKindMap.put( "agent_probability", new String[] { "agent", "probability"});
				_tagKindMap.put( "agent_collection", new String[] { "agent", "collection"});
				_tagKindMap.put( "agent_list", new String[] { "agent", "list"});
				_tagKindMap.put( "agent_map", new String[] { "agent", "map"});
				_tagKindMap.put( "agent_keyword", new String[] { "agent", "keyword"});
				_tagKindMap.put( "agent_number_object", new String[] { "agent", "number object"});
				_tagKindMap.put( "agent_role_variable", new String[] { "agent", "role variable"});
				_tagKindMap.put( "agent_time_variable", new String[] { "agent", "time variable"});
				_tagKindMap.put( "agent_spot_variable", new String[] { "agent", "spot variable"});
				_tagKindMap.put( "agent_class_variable", new String[] { "agent", "class variable"});
				_tagKindMap.put( "agent_file", new String[] { "agent", "file"});
				_tagKindMap.put( "agent_exchange_algebra", new String[] { "agent", "exchange algebra"});
				_tagKindMap.put( "agent_extransfer", new String[] { "agent", "extransfer"});
				_tagKindMap.put( "spot_probability", new String[] { "spot", "probability"});
				_tagKindMap.put( "spot_collection", new String[] { "spot", "collection"});
				_tagKindMap.put( "spot_list", new String[] { "spot", "list"});
				_tagKindMap.put( "spot_map", new String[] { "spot", "map"});
				_tagKindMap.put( "spot_keyword", new String[] { "spot", "keyword"});
				_tagKindMap.put( "spot_number_object", new String[] { "spot", "number object"});
				_tagKindMap.put( "spot_role_variable", new String[] { "spot", "role variable"});
				_tagKindMap.put( "spot_time_variable", new String[] { "spot", "time variable"});
				_tagKindMap.put( "spot_spot_variable", new String[] { "spot", "spot variable"});
				_tagKindMap.put( "spot_class_variable", new String[] { "spot", "class variable"});
				_tagKindMap.put( "spot_file", new String[] { "spot", "file"});
				_tagKindMap.put( "spot_exchange_algebra", new String[] { "spot", "exchange algebra"});
				_tagKindMap.put( "spot_extransfer", new String[] { "spot", "extransfer"});
			}
		}
	}

	/**
	 * Returns true if loading the specified file is completed successfully.
	 * @param file the specified XML file of Visual Shell data
	 * @param graphics2D the graphics object of JAVA
	 * @param component the base class for all Swing components
	 * @return true if loading the specified file is completed successfully
	 */
	public static boolean execute(File file, Graphics2D graphics2D, JComponent component) {
		// 通常読み込みの場合
		_result = false;
		_modified = false;

		SaxLoader saxLoader = new SaxLoader( graphics2D);

		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			saxParser.parse( file, saxLoader);
			saxLoader.at_end_of_load( component);
		} catch (Exception e) {
			e.printStackTrace();
			saxLoader.at_end_of_load( component);
			return false;
		}
		return _result;
	}

	/**
	 * @param component
	 */
	private void at_end_of_load(JComponent component) {
		if ( !_result)
			return;

		if ( !setup_global_spot_and_role( component)) {
			_result = false;
			return;
		}

		if ( !setup_imagePropertyManager()) {
			_result = false;
			return;
		}

		LayerManager.get_instance().update_image();
		JavaClasses.show_message_dialog( _appendedJavaClassList);
	}

	/**
	 * @param component
	 * @return
	 */
	private boolean setup_global_spot_and_role(JComponent component) {
		if ( LayerManager.get_instance().exist_global_spot_and_role())
			return true;

		if ( !LayerManager.get_instance().create_global_spot_and_role( component))
			return false;

		_modified = true;
		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_imagePropertyManager() {
		if ( !LayerManager.get_instance().exist_image_directory())
			return true;

		File imageDirectory = LayerManager.get_instance().get_image_directory();
		if ( null == imageDirectory)
			return false;

		File[] files = imageDirectory.listFiles();
		if ( null == files)
			return false;

		Map<String, File> fileMap = new HashMap<String, File>();

		// ファイルが存在していてImagePropertyが存在していない場合は新たにImagePropertyを作成して_imagePropertyMapへ追加
		for ( File file:files) {
			if ( file.getName().startsWith( "."))
				continue;

			fileMap.put( file.getName(), file);

			ImageProperty imageProperty = _imagePropertyMap.get( file.getName());
			if ( null != imageProperty)
				continue;

			BufferedImage bufferedImage = Resource.load_image( file);
			if ( null == bufferedImage)
				continue;

			_imagePropertyMap.put( file.getName(), new ImageProperty( bufferedImage.getWidth(), bufferedImage.getHeight()));
		}

		// ImagePropertyが存在しているのにファイルが存在していない場合はImagePropertyを_imagePropertyMapから削除
		Set<String> keys = _imagePropertyMap.keySet();
		String[] filenames = keys.toArray( new String[ 0]);
		for ( String filename:filenames) {
			File file = fileMap.get( filename);
			if ( null != file)
				continue;

			_imagePropertyMap.remove( filename);
		}

		ImagePropertyManager.get_instance().putAll( _imagePropertyMap);

		return true;
	}

	/**
	 * Returns true if loading the specified file is completed successfully.
	 * @param file the specified XML file of Visual Shell data
	 * @param rootDirectory data's root directory
	 * @param graphics2D the graphics object of JAVA
	 * @param component the base class for all Swing components
	 * @return true if loading the specified file is completed successfully
	 * @return
	 */
	public static boolean execute(File file, File rootDirectory, Graphics2D graphics2D, JComponent component) {
		// 「オブジェクトのコピー」の場合
		_result = false;
		_modified = false;

		SaxLoader saxLoader = new SaxLoader( graphics2D);

		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			saxParser.parse( file, saxLoader);
			saxLoader.at_end_of_load( rootDirectory, component);
		} catch (Exception e) {
			e.printStackTrace();
			saxLoader.at_end_of_load( rootDirectory, component);
			return false;
		}
		return _result;
	}

	/**
	 * @param rootDirectory
	 * @param component
	 */
	private void at_end_of_load(File rootDirectory, JComponent component) {
		if ( !_result)
			return;

		if ( null == _layer || !_layer._name.equals( Constant._reservedLayerName)) {
			_result = false;
			return;
		}

		if ( !LayerManager.get_instance().append( _layer, _expressionMap, _imagePropertyMap, rootDirectory, component)) {
			_result = false;
			return;
		}

		LayerManager.get_instance().update_image();
		JavaClasses.show_message_dialog( _appendedJavaClassList);
	}

	/**
	 * Creates the XML SAX loader for Visual Shell data.
	 * @param graphics2D the graphics object of JAVA
	 */
	public SaxLoader(Graphics2D graphics2D) {
		super();
		_graphics2D = graphics2D;
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement( String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {

		super.startElement(arg0, arg1, arg2, arg3);

		if ( arg2.equals( "visual_shell_data")) {
			get_version( arg3);
			_result = true;
			return;
		}

		if ( !_result)
			return;

		if ( arg2.equals( "stage")) {
			on_stage( arg3);
		} else if ( arg2.equals( "initial_stage")) {
			on_initial_stage( arg3);
		} else if ( arg2.equals( "terminal_stage")) {
			on_terminal_stage( arg3);
		} else if ( arg2.equals( "layer")) {
			on_layer( arg3);
		} else if ( arg2.equals( "agent") || arg2.equals( "spot")) {
			on_entity_base( arg3, arg2);
		} else if ( arg2.equals( "initial_data")) {
			on_initial_data( arg3);
		} else if ( arg2.equals( "collection") || arg2.equals( "list")) {
			on_variable( arg3, arg2);
		} else if ( arg2.equals( "map")) {
			on_map( arg3, arg2);
		} else if ( arg2.equals( "probability") || arg2.equals( "keyword")
			|| arg2.equals( "number_object") || arg2.equals( "role_variable")
			|| arg2.equals( "time_variable") || arg2.equals( "spot_variable")
			|| arg2.equals( "file") || arg2.equals( "extransfer")) {
			on_object_standard_variable( arg3, arg2);
		} else if ( arg2.equals( "class_variable")) {
			on_class_variable( arg3, arg2);
		} else if ( arg2.equals( "exchange_algebra")) {
			on_exchange_algebra( arg3, arg2);
		} else if ( arg2.equals( "initial_data_file")) {
			on_initial_data_file( arg3, arg2);
		} else if ( arg2.equals( "agent_role") || arg2.equals( "spot_role")) {
			on_role( arg3, arg2);
		} else if ( arg2.equals( "rule_column")) {
			on_rule_column_widths( arg3);
		} else if ( arg2.equals( "rule_data")) {
			on_rule_data( arg3);
		} else if ( arg2.equals( "rule")) {
			on_rule( arg3);
		} else if ( arg2.equals( "condition") || arg2.equals( "command")) {
			on_rule_element( arg3, arg2);
			// 2014.2.5
		} else if ( arg2.equals( "subject") || arg2.equals( "object") || arg2.equals( "constant")) {
			on_genericRule_element( arg3, arg2);
		} else if ( arg2.equals( "chart")) {
			on_chart( arg3);
		} else if ( arg2.equals( "pair")) {
			on_pair( arg3);
		} else if ( arg2.equals( "others")) {
			on_others( arg3);
		} else if ( arg2.equals( "comment")) {
			on_comment( arg3);
		} else if ( arg2.equals( "simulation_data")) {
			on_simulation_data( arg3);
		} else if ( arg2.equals( "start")) {
			on_start( arg3);
		} else if ( arg2.equals( "step")) {
			on_step( arg3);
		} else if ( arg2.equals( "end")) {
			on_end( arg3);
		} else if ( arg2.equals( "log_step")) {
			on_log_step( arg3);
		} else if ( arg2.equals( "log_data")) {
			on_log_data( arg3);
		} else if ( arg2.equals( "agent_probability") || arg2.equals( "agent_collection")
			|| arg2.equals( "agent_list") || arg2.equals( "agent_map")
			|| arg2.equals( "agent_keyword") || arg2.equals( "agent_number_object")
			|| arg2.equals( "agent_role_variable") || arg2.equals( "agent_time_variable")
			|| arg2.equals( "agent_spot_variable") || arg2.equals( "agent_exchange_algebra")
			|| arg2.equals( "spot_probability") || arg2.equals( "spot_collection")
			|| arg2.equals( "spot_list") || arg2.equals( "spot_map")
			|| arg2.equals( "spot_keyword") || arg2.equals( "spot_number_object")
			|| arg2.equals( "spot_role_variable") || arg2.equals( "spot_time_variable")
			|| arg2.equals( "spot_spot_variable") || arg2.equals( "spot_exchange_algebra")) {
			on_log_object( arg3, arg2);
		} else if ( arg2.equals( "expression_data")) {
			on_expression_data( arg3);
		} else if ( arg2.equals( "expression")) {
			on_expression( arg3);
		} else if ( arg2.equals( "other_scripts_data")) {
			on_other_scripts_data( arg3);
		} else if ( arg2.equals( "comment_data")) {
			on_comment_data( arg3);
		} else if ( arg2.equals( "experiment_data")) {
			on_experiment_data( arg3);
		} else if ( arg2.equals( "experiment")) {
			on_experiment( arg3);
		} else if ( arg2.equals( "initial_value")) {
			on_initial_value( arg3);
		} else if ( arg2.equals( "initial_value_comment")) {
			on_initial_value_comment( arg3);
		} else if ( arg2.equals( "image_data")) {
			on_image_data( arg3);
		} else if ( arg2.equals( "data")) {
			on_data( arg3);
		}
	}

	/**
	 * @param attributes
	 */
	private void get_version(Attributes attributes) {
		String version = attributes.getValue( "version");
		if ( null == version || version.equals( ""))
			return;

		_version = version;
	}

	/**
	 * @param attributes
	 */
	private void on_stage(Attributes attributes) {
		create_stage( attributes);
	}

	/**
	 * @param attributes
	 */
	private void on_initial_stage(Attributes attributes) {
		create_stage( attributes);
	}

	/**
	 * @param attributes
	 */
	private void on_terminal_stage(Attributes attributes) {
		create_stage( attributes);
	}

	/**
	 * @param attributes
	 */
	private void create_stage(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "random");

		_stage = new Stage( name, ( ( null != value) && value.equals( "true")));
	}

	/**
	 * @param attributes
	 */
	private void on_layer(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		if ( name.equals( Constant._reservedLayerName))
			_layer = new Layer( name);
		else {
			if ( 0 == _layerCounter)
				_layer = LayerManager.get_instance().get( 0);
			else
				_layer = new Layer();
		}

		_agentRoleConnectionMap = new HashMap<Role, String[]>();
		_spotRoleConnectionMap = new HashMap<Role, String[]>();
	}

	/**
	 * @param attributes
	 * @param type
	 */
	private void on_entity_base(Attributes attributes, String type) {
		if ( null == _layer) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "id");
		int id = ( null == value || value.equals( ""))
			? LayerManager.get_instance().get_unique_id()
			: Integer.parseInt( value);

		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		boolean global = false;
		String attribute = attributes.getValue( "global");
		if ( null != attribute) {
			if ( attribute.equals( "true"))
				global = true;
			else if ( attribute.equals( "false"))
				global = false;
			else {
				_result = false;
				return;
			}
		}

		value = attributes.getValue( "x");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}

		int x = Integer.parseInt( value);

		value = attributes.getValue( "y");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}

		int y = Integer.parseInt( value);

		String number = attributes.getValue( "number");
		if ( null == number)
			number = "";

		String initialRole = attributes.getValue( "initial_role");
		if ( null == initialRole)
			initialRole = "";

		if ( !initialRole.equals( "")) {
			String[] roles = initialRole.split( ":");
			if ( null == roles || 0 == roles.length) {
				_result = false;
				return;
			}

			initialRole = roles[ 0];
		}

		String imageFilename = attributes.getValue( "image");
		if ( null == imageFilename)
			imageFilename = "";

		String gis = attributes.getValue( "gis");
		if ( null == gis)
			gis = "";

		String[] gisCoordinates = new String[] { "", ""};
		gisCoordinates[ 0] = attributes.getValue( "gis_coordinates_x");
		if ( null == gisCoordinates[ 0])
			gisCoordinates[ 0] = "";

		gisCoordinates[ 1] = attributes.getValue( "gis_coordinates_y");
		if ( null == gisCoordinates[ 1])
			gisCoordinates[ 1] = "";

		if ( type.equals( "spot"))
			_drawObject = new SpotObject( global, id, name, new Point( x, y), number, initialRole, imageFilename, gis, gisCoordinates, _graphics2D);
		else if ( type.equals( "agent")) {
			String initialSpot = attributes.getValue( "initial_spot");
			if ( null == initialSpot)
				initialSpot = "";

			_drawObject = new AgentObject( global, id, name, new Point( x, y), number, initialRole, initialSpot, imageFilename, gis, gisCoordinates, _graphics2D);
		} else {
			_result = false;
			return;
		}

		if ( null == _drawObject)
			_result = false;
	}

	/**
	 * @param attributes
	 */
	private void on_initial_data(Attributes arg3) {
		_initialData = true;
	}

	/**
	 * @param attributes
	 * @param type
	 */
	private void on_variable(Attributes attributes, String type) {
		if ( null == _drawObject || !( _drawObject instanceof EntityBase)) {
			_result = false;
			return;
		}

		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		EntityBase entityBase = ( EntityBase)_drawObject;

		VariableObject variableObject = new VariableObject( type, name);
		get_variable_initial_values( variableObject._variableInitialValues, attributes);

		if ( !_initialData)
			entityBase.append_object( variableObject);
		else {
			String number = attributes.getValue( "number");
			if ( null == name || name.equals( "")) {
				_result = false;
				return;
			}

			_objectBase = variableObject;

			entityBase.append_object( variableObject, number);
		}

		_type = type;
		_name = name;
	}

	/**
	 * @param variableInitialValues 
	 * @param attributes
	 */
	private void get_variable_initial_values(List<VariableInitialValue> variableInitialValues, Attributes attributes) {
		int index = 0;
		while ( true) {
			String type = attributes.getValue( "type" + index);
			if ( null == type)
				break;

			String value = attributes.getValue( "value" + index);
			if ( null == value)
				break;

			type = ( type.equals( "addAgent") ? "agent" : type);
			type = ( type.equals( "addSpot") ? "spot" : type);
			variableInitialValues.add( new VariableInitialValue( type, value));

			++index;
		}
	}

	/**
	 * @param attributes
	 * @param type
	 */
	private void on_map(Attributes attributes, String type) {
		if ( null == _drawObject || !( _drawObject instanceof EntityBase)) {
			_result = false;
			return;
		}

		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		EntityBase entityBase = ( EntityBase)_drawObject;

		MapObject mapObject = new MapObject( name);
		get_map_initial_values( mapObject._mapInitialValues, attributes);

		if ( !_initialData)
			entityBase.append_object( mapObject);
		else {
			String number = attributes.getValue( "number");
			if ( null == name || name.equals( "")) {
				_result = false;
				return;
			}

			_objectBase = mapObject;

			entityBase.append_object( mapObject, number);
		}

		_type = type;
		_name = name;
	}

	/**
	 * @param initialValueMap
	 * @param attributes
	 */

	private void get_map_initial_values(List<MapInitialValue> mapInitialValues, Attributes attributes) {
		int index = 0;
		while ( true) {
			String keyType = attributes.getValue( "key_type" + index);
			if ( null == keyType)
				keyType = "";

			String key = attributes.getValue( "key" + index);
			if ( null == key)
				break;

			if ( key.startsWith( "\"") && key.endsWith( "\"")) {
				key = key.substring( 1, key.length() - 1);
				if ( keyType.equals( ""))
					keyType = "immediate";
			}

			String valueType = attributes.getValue( "value_type" + index);
			if ( null == valueType)
				valueType = "";

			String value = attributes.getValue( "value" + index);
			if ( null == value)
				break;

			mapInitialValues.add( new MapInitialValue( keyType, key, valueType, value));

			++index;
		}
	}

	/**
	 * @param attributes
	 * @param type
	 */
	private void on_object_standard_variable(Attributes attributes, String type) {
		if ( null == _drawObject && !( _drawObject instanceof EntityBase)) {
			//_result = false;
			return;
		}

		if ( type.equals( "probability"))
			on_object_probability( attributes);
		else if ( type.equals( "keyword"))
			on_object_keyword( attributes);
		else if ( type.equals( "number_object"))
			on_object_number_object( attributes);
		else if ( type.equals( "role_variable"))
			on_object_role_variable( attributes);
		else if ( type.equals( "time_variable"))
			on_object_time_variable( attributes);
		else if ( type.equals( "spot_variable"))
			on_object_spot_variable( attributes);
		else if ( type.equals( "file"))
			on_object_file( attributes);
		else if ( type.equals( "extransfer"))
			on_object_extransfer( attributes);
		else {
			_result = false;
			return;
		}

		_type = type;
	}

	/**
	 * @param attributes
	 */
	private void on_object_probability(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "value");
		if ( null == value)
			value = "";

		EntityBase entityBase = ( EntityBase)_drawObject;

		ProbabilityObject probabilityObject = new ProbabilityObject( name, value);

		if ( !_initialData)
			entityBase.append_object( probabilityObject);
		else {
			String number = attributes.getValue( "number");
			if ( null == name || name.equals( "")) {
				_result = false;
				return;
			}

			_objectBase = probabilityObject;

			entityBase.append_object( probabilityObject, number);
		}

		_name = name;
	}

	/**
	 * @param attributes
	 */
	private void on_object_keyword(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "value");
		if ( null == value)
			value = "";

		EntityBase entityBase = ( EntityBase)_drawObject;

		KeywordObject keywordObject = new KeywordObject( name, value);

		if ( !_initialData)
			entityBase.append_object( keywordObject);
		else {
			String number = attributes.getValue( "number");
			if ( null == name || name.equals( "")) {
				_result = false;
				return;
			}

			_objectBase = keywordObject;

			entityBase.append_object( keywordObject, number);
		}

		_name = name;
	}

	/**
	 * @param attributes
	 */
	private void on_object_number_object(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		String type = attributes.getValue( "type");
		if ( null == type || type.equals( "")) {
			_result = false;
			return;
		}

		String initialValue = attributes.getValue( "initial_value");
		if ( null == initialValue)
			initialValue = "";

		EntityBase entityBase = ( EntityBase)_drawObject;

		NumberObject numberObject = new NumberObject( name, type, initialValue);

		if ( !_initialData)
			entityBase.append_object( numberObject);
		else {
			String number = attributes.getValue( "number");
			if ( null == name || name.equals( "")) {
				_result = false;
				return;
			}

			_objectBase = numberObject;

			entityBase.append_object( numberObject, number);
		}

		_name = name;
	}

	/**
	 * @param attributes
	 */
	private void on_object_role_variable(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "value");
		if ( null == value)
			value = "";

		if ( !value.equals( "")) {
			String[] roles = value.split( ":");
			if ( null == roles || 0 == roles.length) {
				_result = false;
				return;
			}

			value = roles[ 0];
		}

		EntityBase entityBase = ( EntityBase)_drawObject;

		RoleVariableObject roleVariableObject = new RoleVariableObject( name, value);

		if ( !_initialData)
			entityBase.append_object( roleVariableObject);
		else {
			String number = attributes.getValue( "number");
			if ( null == name || name.equals( "")) {
				_result = false;
				return;
			}

			_objectBase = roleVariableObject;

			entityBase.append_object( roleVariableObject, number);
		}

		_name = name;
	}

	/**
	 * @param attributes
	 */
	private void on_object_time_variable(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "value");
		if ( null == value || value.equals( ""))
			value = "0:00";

		EntityBase entityBase = ( EntityBase)_drawObject;

		TimeVariableObject timeVariableObject = new TimeVariableObject( name, value);

		if ( !_initialData)
			entityBase.append_object( timeVariableObject);
		else {
			String number = attributes.getValue( "number");
			if ( null == name || name.equals( "")) {
				_result = false;
				return;
			}

			_objectBase = timeVariableObject;

			entityBase.append_object( timeVariableObject, number);
		}

		_name = name;
	}

	/**
	 * @param attributes
	 */
	private void on_object_spot_variable(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "value");
		if ( null == value)
			value = "";

		EntityBase entityBase = ( EntityBase)_drawObject;

		SpotVariableObject spotVariableObject = new SpotVariableObject( name, value);

		if ( !_initialData)
			entityBase.append_object( spotVariableObject);
		else {
			String number = attributes.getValue( "number");
			if ( null == name || name.equals( "")) {
				_result = false;
				return;
			}

			_objectBase = spotVariableObject;

			entityBase.append_object( spotVariableObject, number);
		}

		_name = name;
	}

	/**
	 * @param attributes
	 */
	private void on_object_file(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "value");
		if ( null == value)
			value = "";

		EntityBase entityBase = ( EntityBase)_drawObject;

		FileObject fileObject = new FileObject( name, value);

		if ( !_initialData)
			entityBase.append_object( fileObject);
		else {
			String number = attributes.getValue( "number");
			if ( null == name || name.equals( "")) {
				_result = false;
				return;
			}

			_objectBase = fileObject;

			entityBase.append_object( fileObject, number);
		}

		_name = name;
	}

	/**
	 * @param attributes
	 */
	private void on_object_extransfer(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "value");
		if ( null == value)
			value = "";

		EntityBase entityBase = ( EntityBase)_drawObject;

		ExTransferObject exTransferObject = new ExTransferObject( name, value);

		if ( !_initialData)
			entityBase.append_object( exTransferObject);
		else {
			String number = attributes.getValue( "number");
			if ( null == name || name.equals( "")) {
				_result = false;
				return;
			}

			_objectBase = exTransferObject;

			entityBase.append_object( exTransferObject, number);
		}

		_name = name;
	}

	/**
	 * @param attributes
	 * @param type
	 */
	private void on_class_variable(Attributes attributes, String type) {
		if ( null == _drawObject || !( _drawObject instanceof EntityBase)) {
			_result = false;
			return;
		}

		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		String jarFilename = attributes.getValue( "jar_filename");
		if ( null == jarFilename || jarFilename.equals( "")) {
			_result = false;
			return;
		}

		// TODO
		if ( jarFilename.startsWith( Constant._userModuleDirectoryNameSymbol)) {
			String userModuleDirectoryName = Constant._functionalObjectDirectories[ 1].endsWith( "/")
				? Constant._functionalObjectDirectories[ 1].substring( 0, Constant._functionalObjectDirectories[ 1].length() - 1)
				: Constant._functionalObjectDirectories[ 1];
			jarFilename = userModuleDirectoryName + jarFilename.substring( Constant._userModuleDirectoryNameSymbol.length());
		}

		String classname = attributes.getValue( "classname");
		if ( null == classname || classname.equals( "")) {
			_result = false;
			return;
		}

		if ( Environment.get_instance().is_functional_object_enable()) {
			if ( !JarFileProperties.get_instance().exist( jarFilename, classname, _appendedJavaClassList)) {
				JarAndClass jarAndClass = _jarAndClassMap.get( jarFilename, classname);
				if ( null == jarAndClass) {
					jarAndClass = _jarAndClassMap.select( jarFilename, classname);
					if ( null == jarAndClass) {
						_result = false;
						return;
					}
				}
				if ( !jarAndClass._jarFilename.equals( jarFilename) || !jarAndClass._classname.equals( classname)) {
					jarFilename = jarAndClass._jarFilename;
					classname = jarAndClass._classname;
					_modified = true;
				}
			}
		}

		EntityBase entityBase = ( EntityBase)_drawObject;

		ClassVariableObject classVariableObject = new ClassVariableObject( name, jarFilename, classname);

		if ( !_initialData)
			entityBase.append_object( classVariableObject);
		else {
			String number = attributes.getValue( "number");
			if ( null == name || name.equals( "")) {
				_result = false;
				return;
			}

			_objectBase = classVariableObject;

			entityBase.append_object( classVariableObject, number);
		}

		_type = type;
		_name = name;
	}

	/**
	 * @param attributes
	 * @param type
	 */
	private void on_exchange_algebra(Attributes attributes, String type) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		EntityBase entityBase = ( EntityBase)_drawObject;

		ExchangeAlgebraObject exchangeAlgebraObject = new ExchangeAlgebraObject( name);
		if ( !get_exchange_algebra_initial_values( exchangeAlgebraObject, attributes)) {
			_result = false;
			return;
		}

		if ( !_initialData)
			entityBase.append_object( exchangeAlgebraObject);
		else {
			String number = attributes.getValue( "number");
			if ( null == name || name.equals( "")) {
				_result = false;
				return;
			}

			_objectBase = exchangeAlgebraObject;

			entityBase.append_object( exchangeAlgebraObject, number);
		}

		_type = type;
		_name = name;
	}

	/**
	 * @param exchangeAlgebraObject
	 * @param attributes
	 * @return
	 */
	private boolean get_exchange_algebra_initial_values(ExchangeAlgebraObject exchangeAlgebraObject, Attributes attributes) {
		int counter = 0;
		while ( true) {
			String value = attributes.getValue( "value" + counter);
			String keyword = attributes.getValue( "keyword" + counter);
			String name = attributes.getValue( "name" + counter);
			String hat = attributes.getValue( "hat" + counter);
			String unit = attributes.getValue( "unit" + counter);
			String time = attributes.getValue( "time" + counter);
			String subject = attributes.getValue( "subject" + counter);
			if ( null == value || null == keyword || null == name || null == hat || null == unit || null == time || null == subject)
				break;

			if ( value.equals( ""))
				return false;

			if ( ( !keyword.equals( "") && ( !name.equals( "") || !hat.equals( "") || !unit.equals( "") || !time.equals( "") || !subject.equals( "")))
				|| ( keyword.equals( "") && ( name.equals( "") || hat.equals( ""))))
				return false;

			exchangeAlgebraObject._exchangeAlgebraInitialValues.add( new ExchangeAlgebraInitialValue( value, keyword, name, hat, unit, time, subject));

			++counter;
		}

		return true;
	}

	/**
	 * @param attributes
	 * @param type
	 */
	private void on_initial_data_file(Attributes attributes, String type) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		EntityBase entityBase = ( EntityBase)_drawObject;

		InitialDataFileObject initialDataFileObject = new InitialDataFileObject( name);

		if ( _initialData) {
			_result = false;
			return;
		}

		entityBase.append_object( initialDataFileObject);

		_type = type;
		_name = name;
	}

	/**
	 * @param attributes
	 * @param type
	 */
	private void on_role(Attributes attributes, String type) {
		if ( null == _layer) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "id");
		int id = ( null == value || value.equals( ""))
			? LayerManager.get_instance().get_unique_id()
			: Integer.parseInt( value);

		String name = attributes.getValue( "name");
		if ( null == name) {
			_result = false;
			return;
		}

		String[] names = null;
		if ( name.equals( ""))
			names = new String[] { ""};
		else {
			names = name.split( ":");
			if ( null == names || 0 == names.length) {
				_result = false;
				return;
			}
		}
			
		boolean global = false;
		String attribute = attributes.getValue( "global");
		if ( null != attribute) {
			if ( attribute.equals( "true"))
				global = true;
			else if ( attribute.equals( "false"))
				global = false;
			else {
				_result = false;
				return;
			}
		}

		value = attributes.getValue( "x");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}

		int x = Integer.parseInt( value);

		value = attributes.getValue( "y");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}

		int y = Integer.parseInt( value);

		if ( type.equals( "agent_role")) {
			_drawObject = new AgentRole( global, id, names[ 0], new Point( x, y), _graphics2D);
			_agentRoleConnectionMap.put( ( Role)_drawObject, names);
		} else if ( type.equals( "spot_role")) {
			_drawObject = new SpotRole( global, id, names[ 0], new Point( x, y), _graphics2D);
			_spotRoleConnectionMap.put( ( Role)_drawObject, names);
		} else {
			_result = false;
			return;
		}

		if (null == _drawObject)
			_result = false;
	}

	/**
	 * @param attributes
	 */
	private void on_rule_column_widths(Attributes attributes) {
		if ( null == _drawObject || !( _drawObject instanceof Role)) {
			_result = false;
			return;
		}

		for ( int i = 0; i < attributes.getLength(); ++i) {
			if ( null == attributes.getQName( i))
				continue;

			if ( attributes.getQName( i).matches( "^width[0-9]+"))
				_columnWidthsMap.put( attributes.getQName( i).substring( "width".length()), new Integer( attributes.getValue( i)));
		}
	}

	/**
	 * @param attributes
	 */
	private void on_rule_data(Attributes attributes) {
		if ( null == _drawObject || !( _drawObject instanceof Role)) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "row");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}

		int row = Math.max( Integer.parseInt( value), RuleManager.get_row_minimum_count());

		value = attributes.getValue( "column");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}

		int column = Math.max( Integer.parseInt( value), RuleManager.get_column_minimum_count());

		Role role = ( Role)_drawObject;
		role._ruleManager.setup( row, column, _columnWidthsMap);
	}

	/**
	 * @param attributes
	 */
	private void on_rule(Attributes attributes) {
		if ( null == _drawObject || !( _drawObject instanceof Role)) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "row");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}

		_row = Integer.parseInt( value);
		_conditionColumnMax = 0;
	}

	/**
	 * @param attributes
	 * @param name
	 */
	private void on_rule_element(Attributes attributes, String name) {
		// TODO 2012.9.20
		if ( null == _drawObject || !( _drawObject instanceof Role) || 0 > _row) {
			_result = false;
			return;
		}

		if ( !name.equals( "condition") && !name.equals( "command")) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "column");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}

		int column = Integer.parseInt( value);

		String type = attributes.getValue( "type");
		if ( null == type || type.equals( "")) {
			_result = false;
			return;
		}

		boolean or = false;
		String attribute = attributes.getValue( "or");
		if ( null != attribute) {
			if ( attribute.equals( "true"))
				or = true;
			else if ( attribute.equals( "false"))
				or = false;
			else {
				_result = false;
				return;
			}
		}

		value = attributes.getValue( "generic");
		if ( null == value || value.equals( ""))
			on_rule( attributes, name, column, type, or);
		else
			on_genericRule( attributes, name, column, type, or);
	}

	/**
	 * @param attributes
	 * @param name
	 * @param column
	 * @param type
	 * @param or
	 */
	private void on_rule(Attributes attributes, String name, int column, String type, boolean or) {
		// TODO 2014.2.10
		if ( type.equals( "Expression"))
			type = ResourceManager.get_instance().get( "rule.type.command.substitution");

		String value = attributes.getValue( "value");
		if ( null == value)
			value = "";

		if ( name.equals( "condition")) {
			if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.stage")) && value.equals( ""))
				return;

			if ( type.equals( ResourceManager.get_instance().get( "rule.type.condition.role")) && !value.equals( ""))
				value = RoleCondition.update_role_name( value);

		} else if ( name.equals( "command")) {
			if ( ( type.equals( ResourceManager.get_instance().get( "rule.type.command.get.equip"))
				|| type.equals( ResourceManager.get_instance().get( "rule.type.command.put.equip")))
				&& value.startsWith( "<"))
				value = ( ( value.matches( "<[^>]*>.+")) ? value.replaceFirst( "<[^>]*>", "") : value);
				//value = transform( value, type);

			if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.role")) && !value.equals( ""))
				value = RoleCommand.update_role_name( value);

			if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.set.role")) && !value.equals( ""))
				value = SetRoleCommand.update_role_name( value);

			if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.create.object")) && !value.equals( ""))
				value = CreateObjectCommand.update_role_name( value);

			if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.time")) && !value.equals( "")) {
				if ( 0 <= value.indexOf( "=+") || 0 <= value.indexOf( "+=")) {
					type = ResourceManager.get_instance().get( "rule.type.command.others");
					if ( _drawObject instanceof SpotRole)
						value = ( "<>" + value);
				}
			}

			if ( type.equals( ResourceManager.get_instance().get( "rule.type.command.move")) && !value.equals( ""))
				value = MoveCommand.update( value);

		}

		Rule rule = Rule.create( name, column, type, value, or);

		Role role = ( Role)_drawObject;
		Rules rules = role._ruleManager.get( _row);
		if ( null == rules) {
			_result = false;
			return;
		}

		if ( _version.equals( "1.0")) {
			if ( name.equals( "condition"))
				_conditionColumnMax = Math.max( column, _conditionColumnMax);
			else
				rule._column += ( _conditionColumnMax + 1);
		}

		rules.add( rule);
	}

	/**
	 * @param attributes
	 * @param name
	 * @param column
	 * @param type
	 * @param or
	 */
	private void on_genericRule(Attributes attributes, String name, int column, String type, boolean or) {
		// TODO 2015.7.29
		if ( null != _genericRule) {
			_result = false;
			return;
		}

		String id = attributes.getValue( "id");
		if ( null == id || id.equals( "")) {
			_result = false;
			return;
		}

		boolean denial = false;
		String attribute = attributes.getValue( "denial");
		if ( null != attribute) {
			if ( attribute.equals( "true"))
				denial = true;
			else if ( attribute.equals( "false"))
				denial = false;
			else {
				_result = false;
				return;
			}
		}

		boolean system = true;
		attribute = attributes.getValue( "system");
		if ( null != attribute) {
			if ( attribute.equals( "true"))
				system = true;
			else if ( attribute.equals( "false"))
				system = false;
			else {
				_result = false;
				return;
			}
		}

		_genericRule = new GenericRule( name, column, type, or, id, denial, system);

		Role role = ( Role)_drawObject;
		Rules rules = role._ruleManager.get( _row);
		if ( null == rules) {
			_result = false;
			return;
		}

		rules.add( _genericRule);
	}

	/**
	 * @param attributes
	 * @param name
	 */
	private void on_genericRule_element(Attributes attributes, String name) {
		// TODO 2014.2.5 "subject"、"object"または"constant"の場合
		if ( name.equals( "constant"))
			on_genericRule_constant( attributes);
		else
			on_genericRule_entityVariable( attributes, name);
	}

	/**
	 * @param attributes
	 */
	private void on_genericRule_entityVariable(Attributes attributes, String name) {
		// TODO 2014.2.5 "subject"または"object"の場合
		if ( null == _genericRule) {
			_result = false;
			return;
		}

		String entity = attributes.getValue( "entity");
		if ( null == entity) {
			_result = false;
			return;
		}

		String entityName = attributes.getValue( "entityName");
		if ( null == entityName) {
			_result = false;
			return;
		}

		String agentVariable = attributes.getValue( "agentVariable");
		if ( null == agentVariable) {
			_result = false;
			return;
		}

		String spotVariable = attributes.getValue( "spotVariable");
		if ( null == spotVariable) {
			_result = false;
			return;
		}

		String variableType = attributes.getValue( "variableType");
		if ( null == variableType) {
			_result = false;
			return;
		}

		String variableValue = attributes.getValue( "variableValue");
		if ( null == variableValue) {
			_result = false;
			return;
		}

		if ( null != _expressionRule)
			// 数式内変数の場合
			_expressionRule._entityVariableRules.add( new EntityVariableRule( entity, entityName, agentVariable, spotVariable, variableType, variableValue));
		else {
			// エンティティ変数の場合
			if ( name.equals( "subject"))
				_genericRule._subject.set( entity, entityName, agentVariable, spotVariable, variableType, variableValue);
			else
				_genericRule._objects.add( new EntityVariableRule( entity, entityName, agentVariable, spotVariable, variableType, variableValue));
		}
	}

	/**
	 * @param attributes
	 * @param name
	 */
	private void on_genericRule_constant(Attributes attributes) {
		// TODO 2014.2.5 "constant"の場合
		if ( null == _genericRule) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "value");
		if ( null == value) {
			_result = false;
			return;
		}

		_genericRule._objects.add( new ConstantRule( value));
	}

	/**
	 * @param attributes
	 */
	private void on_genericRule_expression(Attributes attributes) {
		// TODO 2014.2.5 "expression"の場合
		if ( null == _genericRule) {
			_result = false;
			return;
		}

		String name = attributes.getValue( "name");
		if ( null == name) {
			_result = false;
			return;
		}

		_expressionRule = new ExpressionRule( name, ( Role)_drawObject);
		_genericRule._objects.add( _expressionRule);
	}

	/**
	 * @param attributes
	 */
	private void on_chart(Attributes attributes) {
		if ( null == _layer) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "id");
		int id = ( null == value || value.equals( ""))
			? LayerManager.get_instance().get_unique_id()
			: Integer.parseInt( value);

		String name = attributes.getValue( "name");
		if ( null == name) {
			_result = false;
			return;
		}

		value = attributes.getValue( "x");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}

		int x = Integer.parseInt( value);

		value = attributes.getValue( "y");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}

		int y = Integer.parseInt( value);

		_drawObject = new ChartObject( id, name, new Point( x, y), _graphics2D);

		if (null == _drawObject) {
			_result = false;
			return;
		}

		ChartObject chartObject = ( ChartObject)_drawObject;

		value = attributes.getValue( "type");
		//chartObject._type = ( ( null == value) ? "" : value);
		if ( null != value)
			_chartObjectType = value;

		value = attributes.getValue( "object_name");
		//chartObject._object_name = ( ( null == value) ? "" : value);
		if ( null != value)
			_chartObjectObjectName = value;

		value = attributes.getValue( "title");
		chartObject._title = ( ( null == value) ? "" : value);

		value = attributes.getValue( "horizontal_axis");
		chartObject._horizontalAxis = ( ( null == value) ? "" : value);

		value = attributes.getValue( "vertical_axis");
		chartObject._verticalAxis = ( ( null == value) ? "" : value);

		value = attributes.getValue( "connect");
		if ( null == value)
			value = "true";

		if ( value.equals( "true"))
			chartObject._connect = true;
		else if ( value.equals( "false"))
			chartObject._connect = false;
		else {
			_result = false;
			return;
		}
	}

	/**
	 * @param attributes
	 */
	private void on_pair(Attributes attributes) {
		if ( null == _drawObject || !( _drawObject instanceof ChartObject)) {
			_result = false;
			return;
		}

		ChartObject chartObject = ( ChartObject)_drawObject;

		if ( null == _chartObjectType && null == _chartObjectObjectName) {
			NumberObjectData[] numberObjectDataPair = new NumberObjectData[ 2];
			String[] prifix = new String[] { "horizontal_", "vertical_"};
			for ( int i = 0; i < numberObjectDataPair.length; ++i) {
				if ( !on_pair( attributes, numberObjectDataPair, prifix, i)) {
					_result = false;
					return;
				}
			}

			chartObject._numberObjectDataPairs.add( numberObjectDataPair);
		} else if ( null != _chartObjectType && null != _chartObjectObjectName) {
			String horizontal = attributes.getValue( "horizontal");
			if ( null == horizontal || horizontal.equals( "")) {
				_result = false;
				return;
			}

			String vertical = attributes.getValue( "vertical");
			if ( null == vertical || vertical.equals( "")) {
				_result = false;
				return;
			}

			chartObject._numberObjectDataPairs.add(
				new NumberObjectData[] {
					new NumberObjectData( _chartObjectType, _chartObjectObjectName, horizontal),
					new NumberObjectData( _chartObjectType, _chartObjectObjectName, vertical)});
		} else {
			_result = false;
			return;
		}
	}

	/**
	 * @param attributes
	 * @param numberObjectDataPair
	 * @param prifix
	 * @param index
	 * @return
	 */
	private boolean on_pair(Attributes attributes, NumberObjectData[] numberObjectDataPair, String[] prifix, int index) {
		String type = attributes.getValue( prifix[ index] + "type");
		if ( null == type || type.equals( ""))
			return false;

		if ( type.equals( "step")) {
			numberObjectDataPair[ index] = new NumberObjectData();
			return true;
		}

		String objectName = attributes.getValue( prifix[ index] + "object_name");
		if ( null == objectName || objectName.equals( "")) {
			return false;
		}

		String numberVariable = attributes.getValue( prifix[ index] + "number_variable");
		if ( null == numberVariable || numberVariable.equals( "")) {
			return false;
		}

		numberObjectDataPair[ index] = new NumberObjectData( type, objectName, numberVariable);

		return true;
	}

	/**
	 * @param attributes
	 */
	private void on_others(Attributes attributes) {
		if ( null == _drawObject) {
			_result = false;
			return;
		}
		_entityBaseOthers = true;
	}

	/**
	 * @param attributes
	 */
	private void on_comment(Attributes attributes) {
		if ( null != _drawObject)
			_drawObjectComment = ( 0 > _row);
		else if ( null != _initialValueMap)
			return;
		else {
			_result = false;
			return;
		}
	}

	/**
	 * @param attributes
	 */
	private void on_simulation_data(Attributes attributes) {
		_simulationManager = SimulationManager.get_instance();

		String value = attributes.getValue( "export_end_time");
		if ( null == value)
			value = "true";

		boolean exportEndTime;
		if ( value.equals( "true"))
			exportEndTime = true;
		else if ( value.equals( "false"))
			exportEndTime = false;
		else {
			_result = false;
			return;
		}

		_simulationManager._exportEndTime = exportEndTime;

		value = attributes.getValue( "random_seed");
		_simulationManager._randomSeed = ( null != value) ? value : "";
	}

	/**
	 * @param attributes
	 */
	private void on_start(Attributes attributes) {
		if ( null == _simulationManager) {
			_result = false;
			return;
		}

		get_time( attributes, SimulationManager.get_instance()._startTime);
	}

	/**
	 * @param attributes
	 */
	private void on_step(Attributes attributes) {
		if ( null == _simulationManager) {
			_result = false;
			return;
		}

		get_time( attributes, SimulationManager.get_instance()._stepTime);
	}

	/**
	 * @param attributes
	 */
	private void on_end(Attributes attributes) {
		if ( null == _simulationManager) {
			_result = false;
			return;
		}

		get_time( attributes, SimulationManager.get_instance()._endTime);
	}

	/**
	 * @param attributes
	 */
	private void on_log_step(Attributes attributes) {
		if ( null == _simulationManager) {
			_result = false;
			return;
		}

		get_time( attributes, SimulationManager.get_instance()._logStepTime);
	}

	/**
	 * @param attributes
	 * @param time
	 */
	private void get_time(Attributes attributes, String[] time) {
		String day = attributes.getValue( "day");
		if ( null == day || day.equals( "")) {
			_result = false;
			return;
		}

		String hour = attributes.getValue( "hour");
		if ( null == hour || hour.equals( "")) {
			_result = false;
			return;
		}

		String minute = attributes.getValue( "minute");
		if ( null == minute || minute.equals( "")) {
			_result = false;
			return;
		}

		time[ 0] = day;
		time[ 1] = hour;
		time[ 2] = minute;
	}

	/**
	 * @param attributes
	 */
	private void on_log_data(Attributes attributes) {
		_logManager = LogManager.get_instance();
	}

	/**
	 * @param attributes
	 * @param type
	 */
	private void on_log_object(Attributes attributes, String type) {
		if ( null == _logManager) {
			_result = false;
			return;
		}

		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "flag");
		if ( null == value)
			value = "false";

		boolean flag;
		if ( value.equals( "true"))
			flag = true;
		else if ( value.equals( "false"))
			flag = false;
		else {
			_result = false;
			return;
		}

		_logManager.append( new LogOption( name, flag), type);
	}

	/**
	 * @param attributes
	 */
	private void on_expression_data(Attributes attributes) {
		if ( null != _layer && _layer._name.equals( Constant._reservedLayerName))
			_expressionMap = new TreeMap<String, Expression>();
		else
			_visualShellExpressionManager = VisualShellExpressionManager.get_instance();
	}

	/**
	 * @param attributes
	 */
	private void on_expression(Attributes attributes) {
		// TODO 2014.2.5
		if ( null != _genericRule)
			// GenericRulleの数式
			on_genericRule_expression( attributes);
		else {
			// 通常の数式
			if ( null != _layer && _layer._name.equals( Constant._reservedLayerName)) {
				if ( null == _expressionMap) {
					_result = false;
					return;
				}
			} else {
				if ( null == _visualShellExpressionManager) {
					_result = false;
					return;
				}
			}

			String[] value = new String[ 3];
			for ( int i = 0; i < value.length; ++i) {
				value[ i] = attributes.getValue( "value" + i);
				if ( 1 == i) {
					if ( null == value[ i]) {
						_result = false;
						return;
					}
				} else {
					if ( null == value[ i] || value[ i].equals( "")) {
						_result = false;
						return;
					}
				}
			}

			if ( null != _layer && _layer._name.equals( Constant._reservedLayerName))
				_expressionMap.put( value[ 0], new Expression( value));
			else
				_visualShellExpressionManager.put( value[ 0], new Expression( value));
		}
	}

	/**
	 * @param attributes
	 */
	private void on_other_scripts_data(Attributes attributes) {
		_otherScriptsManager = OtherScriptsManager.get_instance();
	}

	/**
	 * @param attributes
	 */
	private void on_comment_data(Attributes attributes) {
		_commentManager = CommentManager.get_instance();

		String value = attributes.getValue( "title");
		if ( null != value)
			_commentManager._title = value;

		value = attributes.getValue( "date");
		if ( null != value)
			_commentManager._date = value;

		value = attributes.getValue( "author");
		if ( null != value)
			_commentManager._author = value;

		value = attributes.getValue( "email");
		if ( null != value)
			_commentManager._email = value;
	}

	/**
	 * @param attributes
	 */
	private void on_experiment_data(Attributes attributes) {
		_experimentManager = ExperimentManager.get_instance();

		setup_column_widths_on_experiment_data( attributes);
		setup_number_of_times_on_experiment_data( attributes);
		setup_parallel_on_experiment_data( attributes);
	}

	/**
	 * @param attributes
	 */
	private void setup_column_widths_on_experiment_data(Attributes attributes) {
		String value = attributes.getValue( "column");
		if ( null == value || value.equals( ""))
			return;

		int column;
		try {
			column = Integer.parseInt( value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return;
		}

		Vector<Integer> columnWidths = new Vector<Integer>();

		String name = "width";
		for ( int i = 0; i < column; ++i) {
			value = attributes.getValue( name + i);
			if ( null == value || value.equals( "")) {
				columnWidths.add( new Integer( ( 0 == i)
					? ExperimentManager._defaultExperimentTableCheckBoxColumnWidth
					: ExperimentManager._defaultExperimentTableColumnWidth));
				continue;
			}

			columnWidths.add( new Integer( value));
		}

		_experimentManager.setup_column_widths( columnWidths);
	}

	/**
	 * @param attributes
	 */
	private void setup_number_of_times_on_experiment_data(Attributes attributes) {
		String value = attributes.getValue( "number_of_times");
		if ( null == value || value.equals( ""))
			return;

		_experimentManager._numberOfTimes = Integer.parseInt( value);
	}

	/**
	 * @param attributes
	 */
	private void setup_parallel_on_experiment_data(Attributes attributes) {
		String value = attributes.getValue( "parallel");
		if ( null == value || value.equals( ""))
			return;

		boolean parallel;
		if ( value.equals( "true"))
			parallel = true;
		else
			parallel = false;

		_experimentManager._parallel = parallel;
	}

	/**
	 * @param attributes
	 */
	private void on_experiment(Attributes attributes) {
		if ( null == _experimentManager) {
			_result = false;
			return;
		}

		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "export");
		if ( null == value) {
			_result = false;
			return;
		}

		boolean export;
		if ( value.equals( "true"))
			export = true;
		else if ( value.equals( "false"))
			export = false;
		else {
			_result = false;
			return;
		}

		_initialValueMap = new InitialValueMap();
		_initialValueMap._export = export;
		_experimentManager.put( name, _initialValueMap);
	}

	/**
	 * @param attributes
	 */
	private void on_initial_value(Attributes attributes) {
		if ( null == _experimentManager || null == _initialValueMap) {
			_result = false;
			return;
		}

		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		String value = attributes.getValue( "value");
		if ( null == value) {
			_result = false;
			return;
		}

		_initialValueMap.put( name, value);
	}

	/**
	 * @param attributes
	 */
	private void on_initial_value_comment(Attributes attributes) {
		if ( null == _experimentManager) {
			_result = false;
			return;
		}

		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		_type = "initial_value_comment";
		_name = name;
	}

	/**
	 * @param attributes
	 */
	private void on_image_data(Attributes attributes) {
		_image = true;
	}

	/**
	 * @param attributes
	 */
	private void on_data(Attributes attributes) {
		if ( _image)
			get_image_property( attributes);
	}

	/**
	 * @param attributes
	 */
	private void get_image_property(Attributes attributes) {
		String filename = attributes.getValue( "filename");
		if ( null == filename || filename.equals( "")) {
			_result = false;
			return;
		}

		int width = 0;
		String attribute = attributes.getValue( "width");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				width = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		int height = 0;
		attribute = attributes.getValue( "height");
		if ( null != attribute && !attribute.equals( "")) {
			try {
				height = Integer.parseInt( attribute);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				_result = false;
				return;
			}
		}

		_imagePropertyMap.put( filename, new ImageProperty( width, height));
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		if ( !_result)
			return;

		if ( null != _commentManager) {
			String comment = new String( arg0, arg1, arg2);
			_commentManager._comment += comment;
		}

		if ( null != _stage) {
			String comment = new String( arg0, arg1, arg2);
			_stage._comment += comment;
		}

		if ( null != _drawObject) {
			if ( _entityBaseOthers) {
				String others = new String( arg0, arg1, arg2);
				EntityBase entityBase = ( EntityBase)_drawObject;
				entityBase._others += others;
			}
			if ( _drawObjectComment) {
				String comment = new String( arg0, arg1, arg2);
				_drawObject._comment += comment;
			}
			if ( 0 <= _row) {
				String comment = new String( arg0, arg1, arg2);
				_comment += comment;
			}
		}

		if ( null != _otherScriptsManager) {
			String otherScripts = new String( arg0, arg1, arg2);
			_otherScriptsManager._otherScripts += otherScripts;
		}

		if ( null != _initialValueMap) {
			String comment = new String( arg0, arg1, arg2);
			_initialValueMap._comment += comment;
		}

		if ( null != _type && null != _name) {
			String comment = new String( arg0, arg1, arg2);
			_comment += comment;
		}
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String arg0, String arg1, String arg2) throws SAXException {

		if ( !_result) {
			super.endElement(arg0, arg1, arg2);
			return;
		}

		if ( arg2.equals( "stage")) {
			on_stage();
		} else if ( arg2.equals( "initial_stage")) {
			on_initial_stage();
		} else if ( arg2.equals( "terminal_stage")) {
			on_terminal_stage();
		} else if ( arg2.equals( "layer")) {
			on_layer();
		} else if ( arg2.equals( "agent") || arg2.equals( "spot")) {
			on_entity_base();
		} else if ( arg2.equals( "initial_data")) {
			on_initial_data();
		} else if ( arg2.equals( "collection") || arg2.equals( "list")) {
			on_variable();
		} else if ( arg2.equals( "map")) {
			on_map();
		} else if ( arg2.equals( "probability") || arg2.equals( "keyword")
			|| arg2.equals( "number_object") || arg2.equals( "role_variable")
			|| arg2.equals( "time_variable") || arg2.equals( "spot_variable")
			|| arg2.equals( "file") || arg2.equals( "extransfer")) {
			on_object_standard_variable();
		} else if ( arg2.equals( "class_variable")) {
			on_class_variable();
		} else if ( arg2.equals( "exchange_algebra")) {
			on_exchange_algebra();
		} else if ( arg2.equals( "initial_data_file")) {
			on_initial_data_file();
		} else if ( arg2.equals( "agent_role") || arg2.equals( "spot_role")) {
			on_role();
		} else if ( arg2.equals( "rule_column")) {
			on_rule_column_widths();
		} else if ( arg2.equals( "rule_data")) {
			on_rule_data();
		} else if ( arg2.equals( "rule")) {
			on_rule();
		} else if ( arg2.equals( "condition") || arg2.equals( "command")) {
			on_rule_element();
		} else if ( arg2.equals( "chart")) {
			on_chart();
		} else if ( arg2.equals( "pair")) {
			on_pair();
		} else if ( arg2.equals( "others")) {
			on_others();
		} else if ( arg2.equals( "comment")) {
			on_comment();
		} else if ( arg2.equals( "simulation_data")) {
			on_simulation_data();
		} else if ( arg2.equals( "start")) {
			on_start();
		} else if ( arg2.equals( "step")) {
			on_step();
		} else if ( arg2.equals( "end")) {
			on_end();
		} else if ( arg2.equals( "log_step")) {
			on_log_step();
		} else if ( arg2.equals( "log_data")) {
			on_log_data();
		} else if ( arg2.equals( "agent_probability") || arg2.equals( "agent_collection")
			|| arg2.equals( "agent_keyword") || arg2.equals( "agent_number_object")
			|| arg2.equals( "agent_list") || arg2.equals( "agent_map")
			|| arg2.equals( "agent_role_variable") || arg2.equals( "agent_time_variable")
			|| arg2.equals( "agent_spot_variable") || arg2.equals( "agent_exchange_algebra")
			|| arg2.equals( "spot_probability") || arg2.equals( "spot_collection")
			|| arg2.equals( "spot_list") || arg2.equals( "spot_map")
			|| arg2.equals( "spot_keyword") || arg2.equals( "spot_number_object")
			|| arg2.equals( "spot_role_variable") || arg2.equals( "spot_time_variable")
			|| arg2.equals( "spot_spot_variable")|| arg2.equals( "spot_exchange_algebra")) {
			on_log_object();
		} else if ( arg2.equals( "expression_data")) {
			on_expression_data();
		} else if ( arg2.equals( "expression")) {
			on_expression();
		} else if ( arg2.equals( "other_scripts_data")) {
			on_other_scripts_data();
		} else if ( arg2.equals( "comment_data")) {
			on_comment_data();
		} else if ( arg2.equals( "experiment_data")) {
			on_experiment_data();
		} else if ( arg2.equals( "experiment")) {
			on_experiment();
		} else if ( arg2.equals( "initial_value")) {
			on_initial_value();
		} else if ( arg2.equals( "initial_value_comment")) {
			on_initial_value_comment();
		} else if ( arg2.equals( "image_data")) {
			on_image_data();
		} else if ( arg2.equals( "visual_shell_data")) {
			on_visual_shell_data();
		}

		super.endElement(arg0, arg1, arg2);
	}

	/**
	 * 
	 */
	private void on_stage() {
		StageManager.get_instance().append_main_stage( _stage);
		_stage = null;
	}

	/**
	 * 
	 */
	private void on_initial_stage() {
		StageManager.get_instance().append_initial_stage( _stage);
		_stage = null;
	}

	/**
	 * 
	 */
	private void on_terminal_stage() {
		StageManager.get_instance().append_terminal_stage( _stage);
		_stage = null;
	}

	/**
	 * 
	 */
	private void on_layer() {
		if ( !_layer.connect( "agent_role", _agentRoleConnectionMap, _graphics2D)) {
			_result = false;
			return;
		}

		if ( !_layer.connect( "spot_role", _spotRoleConnectionMap, _graphics2D)) {
			_result = false;
			return;
		}

		_agentRoleConnectionMap = null;
		_spotRoleConnectionMap = null;

		if ( !_layer._name.equals( Constant._reservedLayerName)) {
			if ( 0 < _layerCounter)
				LayerManager.get_instance().add( _layer);

			++_layerCounter;
			_layer = null;
		}
	}

	/**
	 * 
	 */
	private void on_entity_base() {
		if ( null == _layer || null == _drawObject
			|| !( _drawObject instanceof EntityBase)) {
			_result = false;
			return;
		}

		_layer.append_object( _drawObject);
		_drawObject = null;
	}

	/**
	 * 
	 */
	private void on_initial_data() {
		_initialData = false;
	}

	/**
	 * 
	 */
	private void on_variable() {
		set_comment();
	}

	/**
	 * 
	 */
	private void on_map() {
		set_comment();
	}

	/**
	 * 
	 */
	private void on_object_standard_variable() {
		set_comment();
	}

	/**
	 * 
	 */
	private void on_class_variable() {
		set_comment();
	}

	/**
	 * 
	 */
	private void on_exchange_algebra() {
		set_comment();
	}

	/**
	 * 
	 */
	private void on_initial_data_file() {
		set_comment();
	}

	/**
	 * 
	 */
	private void set_comment() {
		if ( null == _drawObject || !( _drawObject instanceof EntityBase) || null == _type || null == _name) {
			_result = false;
			return;
		}

		EntityBase entityBase = ( EntityBase)_drawObject;

		boolean result = false;
		if ( !entityBase.is_multi())
			result = entityBase.set_comment( _typeKindMap.get( _type), _name, _comment);
		else
			result = entityBase.set_comment( _typeKindMap.get( _type), _name, _objectBase, _comment);

		_type = null;
		_name = null;
		_comment = "";

		if ( !result) {
			_result = false;
			return;
		}
	}

	/**
	 * 
	 */
	private void on_role() {
		if ( null == _layer || null == _drawObject
			|| !( _drawObject instanceof Role)) {
			_result = false;
			return;
		}

		_layer.append_object( _drawObject);

		_drawObject = null;
	}

	/**
	 * 
	 */
	private void on_rule_data() {
		_columnWidthsMap.clear();
		if ( _version.equals( "1.0")) {
			Role role = ( Role)_drawObject;
			role._ruleManager.adjust_column_count();
		}
	}

	/**
	 * 
	 */
	private void on_rule() {
		_row = -1;
	}

	/**
	 * 
	 */
	private void on_rule_element() {
		// TODO 2012.9.20
		_genericRule = null;
	}

	/**
	 * 
	 */
	private void on_rule_column_widths() {
	}

	/**
	 * 
	 */
	private void on_chart() {
		if ( null == _layer || null == _drawObject
			|| !( _drawObject instanceof ChartObject)) {
			_result = false;
			return;
		}

		_layer.append_object( _drawObject);

		_drawObject = null;

		_chartObjectType = null;
		_chartObjectObjectName = null;
	}

	/**
	 * 
	 */
	private void on_pair() {
	}

	/**
	 * 
	 */
	private void on_others() {
		_entityBaseOthers = false;
	}

	/**
	 * 
	 */
	private void on_comment() {
		if ( 0 > _row)
			_drawObjectComment = false;
		else {
			if ( null == _drawObject || !( _drawObject instanceof Role)) {
				_result = false;
				return;
			}

			Role role = ( Role)_drawObject;
			Rules rules = role._ruleManager.get( _row);
			if ( null == rules) {
				_result = false;
				return;
			}

			rules._comment = _comment;

			_type = null;
			_name = null;
			_comment = "";
		}
	}

	/**
	 * 
	 */
	private void on_simulation_data() {
		if ( _simulationManager.log_step_time_equals_zero())
			_simulationManager.substitute_step_time_into_log_step_time();

		_simulationManager = null;
	}

	/**
	 * 
	 */
	private void on_start() {
	}

	/**
	 * 
	 */
	private void on_step() {
	}

	/**
	 * 
	 */
	private void on_end() {
	}

	/**
	 * 
	 */
	private void on_log_step() {
	}

	/**
	 * 
	 */
	private void on_log_data() {
		if ( null == _logManager) {
			_result = false;
			return;
		}
		_logManager.update_all();
		_logManager = null;
	}

	/**
	 * 
	 */
	private void on_log_object() {
	}

	/**
	 * 
	 */
	private void on_expression_data() {
		if ( null != _layer && _layer._name.equals( Constant._reservedLayerName)) {
			if ( null == _expressionMap) {
				_result = false;
				return;
			}
		} else {
			if ( null == _visualShellExpressionManager) {
				_result = false;
				return;
			}
			_visualShellExpressionManager = null;
		}
	}

	/**
	 * 
	 */
	private void on_expression() {
		// TODO 2014.2.5
		if ( null != _genericRule)
			// GenericRulleの数式
			_expressionRule = null;
	}

	/**
	 * 
	 */
	private void on_other_scripts_data() {
		_otherScriptsManager = null;
	}

	/**
	 * 
	 */
	private void on_comment_data() {
		_commentManager = null;
	}

	/**
	 * 
	 */
	private void on_experiment_data() {
		if ( null == _experimentManager) {
			_result = false;
			return;
		}
		_experimentManager.update_all();
		_experimentManager = null;
	}

	/**
	 * 
	 */
	private void on_experiment() {
		_initialValueMap = null;
	}

	/**
	 * 
	 */
	private void on_initial_value() {
	}

	/**
	 * 
	 */
	private void on_initial_value_comment() {
		if ( null == _experimentManager || null == _type || null == _name) {
			_result = false;
			return;
		}

		if ( !_type.equals( "initial_value_comment")) {
			_result = false;
			return;
		}

		_experimentManager._commentMap.put( _name, _comment);

		_type = null;
		_name = null;
		_comment = "";
	}

	/**
	 * 
	 */
	private void on_image_data() {
//		if ( null == _layer || !_layer._name.equals( Constant._reservedLayerName))
//			ImagePropertyManager.get_instance().putAll( _imagePropertyMap);

		_image = false;
	}

	/**
	 * 
	 */
	private void on_visual_shell_data() {
	}
}
