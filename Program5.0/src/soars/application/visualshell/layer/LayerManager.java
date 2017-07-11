/*
 * 2005/04/25
 */
package soars.application.visualshell.layer;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;

import soars.application.visualshell.common.file.ScriptFile;
import soars.application.visualshell.common.image.VisualShellImageManager;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.file.docker.DockerFilesetCreator;
import soars.application.visualshell.file.docker.DockerFilesetProperty;
import soars.application.visualshell.file.exporter.initial.InitialDataExporter;
import soars.application.visualshell.file.exporter.script.Exporter;
import soars.application.visualshell.file.importer.initial.InitialDataImporter;
import soars.application.visualshell.file.importer.initial.entity.EntityData;
import soars.application.visualshell.file.importer.initial.role.RoleData;
import soars.application.visualshell.file.loader.SaxLoader;
import soars.application.visualshell.file.writer.SaxWriter;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.MainFrame;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.base.DrawObject;
import soars.application.visualshell.object.base.DrawObjectPositionComparator;
import soars.application.visualshell.object.chart.ChartObject;
import soars.application.visualshell.object.comment.CommentManager;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.object.class_variable.ClassVariableObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.experiment.ExperimentManager;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.log.LogManager;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.inheritance.ConnectInObject;
import soars.application.visualshell.object.role.base.edit.tab.legacy.common.functional_object.FunctionalObject;
import soars.application.visualshell.object.role.spot.SpotRole;
import soars.application.visualshell.object.scripts.OtherScriptsManager;
import soars.application.visualshell.object.simulation.SimulationManager;
import soars.application.visualshell.object.stage.Stage;
import soars.application.visualshell.object.stage.StageManager;
import soars.application.visualshell.observer.Observer;
import soars.application.visualshell.state.StateManager;
import soars.common.soars.environment.BasicEnvironment;
import soars.common.soars.tool.DocumentWriter;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.swing.image.ImageProperty;
import soars.common.utility.swing.image.ImagePropertyManager;
import soars.common.utility.swing.window.OptionPane;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.expression.Expression;
import soars.common.utility.tool.file.Entry;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.file.ZipUtility;
import soars.common.utility.tool.sort.QuickSort;
import soars.common.utility.tool.ssh.SshTool;
import soars.common.utility.tool.ssh2.SshTool2;
import soars.common.utility.xml.sax.Writer;

/**
 * Manages the all layers.
 * @author kurata / SOARS project
 */
public class LayerManager extends Vector<Layer> implements ILayerManipulator {

	/**
	 * Name of the data file.
	 */
	static public final String _temporaryFilename = "soars_visualshell.tmp";

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private LayerManager _layerManager = null;

	/**
	 * 
	 */
	private int _currentLayer = 0;

	/**
	 * 
	 */
	private BufferedImage _bufferedImage = null;

	/**
	 * 
	 */
	private Color _backgroundColor = new Color( 255, 255, 255);

	/**
	 * 
	 */
	private Color _rubberbandColor = new Color( 0, 0, 0);

	/**
	 * 
	 */
	private File _currentFile = null;

	/**
	 * 
	 */
	private File _parentDirectory = null;

	/**
	 * 
	 */
	private File _rootDirectory = null;

	/**
	 * 
	 */
	private boolean _modified = false;

	/**
	 * 
	 */
	private String[] _overwriteOptions = new String[] {
		ResourceManager.get_instance().get( "dialog.yes"),
		ResourceManager.get_instance().get( "dialog.no"),
		ResourceManager.get_instance().get( "dialog.yes.to.all"),
		ResourceManager.get_instance().get( "dialog.no.to.all")
	};

	/**
	 * 
	 */
	private String _option = "";

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
			if ( null == _layerManager) {
				_layerManager = new LayerManager();
			}
		}
	}

	/**
	 * Returns the instance of this object.
	 * @return the instance of this object
	 */
	public static LayerManager get_instance() {
		if ( null == _layerManager) {
			System.exit( 0);
		}

		return _layerManager;
	}

	/**
	 * Creates this object.
	 */
	public LayerManager() {
		super();
		add( new Layer());
	}

	/**
	 * @return
	 */
	private boolean setup_work_directory() {
		if ( null != _parentDirectory)
			return true;

		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return false;

		File rootDirectory = new File( parentDirectory, Constant._visualShellRootDirectoryName);
		if ( !rootDirectory.mkdirs()) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		_parentDirectory = parentDirectory;
		_rootDirectory = rootDirectory;

		return true;
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
		for ( Layer layer:this)
			layer.cleanup();

		clear();

		add( new Layer());
		_currentLayer = 0;

		VisualShellImageManager.get_instance().cleanup();
		ImagePropertyManager.get_instance().cleanup();

		_currentFile = null;
		_modified = false;

		if ( null != _parentDirectory)
			FileUtility.delete( _parentDirectory, true);

		_rootDirectory = null;
		_parentDirectory = null;
	}

	/**
	 * Returns true if the data file exists.
	 * @return true if the data file exists
	 */
	public boolean exist_datafile() {
		return ( null == _currentFile) ? false : true;
	}

	/**
	 * Returns the current data file.
	 * @return the current data file
	 */
	public File get_current_file() {
		return _currentFile;
	}

	/**
	 * Returns true if the image directory exists.
	 * @return true if the image directory exists
	 */
	public boolean exist_image_directory() {
		if ( null == _parentDirectory || null == _rootDirectory)
			return false;

		return exist_image_directory( _rootDirectory);
	}

	/**
	 * @param rootDirectory
	 * @return
	 */
	private boolean exist_image_directory(File rootDirectory) {
		File directory = new File( rootDirectory, Constant._imageDirectory);
		return ( directory.exists() && directory.isDirectory());
	}

	/**
	 * Returns the image directory.
	 * @return the image directory
	 */
	public File get_image_directory() {
		if ( !setup_work_directory())
			return null;

		return get_image_directory( _rootDirectory);
	}

	/**
	 * @param rootDirectory
	 * @return
	 */
	private File get_image_directory(File rootDirectory) {
		File directory = new File( rootDirectory, Constant._imageDirectory);
		if ( !directory.exists() && !directory.mkdir())
			return null;

		return directory;
	}

	/**
	 * Returns true if the thumbnail directory exists.
	 * @return true if the thumbnail directory exists
	 */
	public boolean exist_thumbnail_image_directory() {
		if ( null == _parentDirectory || null == _rootDirectory)
			return false;

		return exist_thumbnail_image_directory( _rootDirectory);
	}

	/**
	 * @param rootDirectory
	 * @return
	 */
	private boolean exist_thumbnail_image_directory(File rootDirectory) {
		File directory = new File( rootDirectory, Constant._thumbnailImageDirectory);
		return ( directory.exists() && directory.isDirectory());
	}

	/**
	 * Returns the thumbnail directory.
	 * @return the thumbnail directory
	 */
	public File get_thumbnail_image_directory() {
		if ( !setup_work_directory())
			return null;

		return get_thumbnail_image_directory( _rootDirectory);
	}

	/**
	 * @param rootDirectory
	 * @return
	 */
	private File get_thumbnail_image_directory(File rootDirectory) {
		File directory = new File( rootDirectory, Constant._thumbnailImageDirectory);
		if ( !directory.exists() && !directory.mkdir())
			return null;

		return directory;
	}

	/**
	 * Returns true if the user's data directory exists.
	 * @return true if the user's data directory exists
	 */
	public boolean exist_user_data_directory() {
		if ( null == _parentDirectory || null == _rootDirectory)
			return false;

		return exist_user_data_directory( _rootDirectory);
	}

	/**
	 * @param rootDirectory
	 * @return
	 */
	private boolean exist_user_data_directory(File rootDirectory) {
		File directory = new File( rootDirectory, Constant._userDataDirectory);
		return ( directory.exists() && directory.isDirectory());
	}

	/**
	 * Returns the user's data directory.
	 * @return the user's data directory
	 */
	public File get_user_data_directory() {
		if ( !setup_work_directory())
			return null;

		return get_user_data_directory( _rootDirectory);
	}

	/**
	 * @param rootDirectory
	 * @return
	 */
	private File get_user_data_directory(File rootDirectory) {
		File directory = new File( rootDirectory, Constant._userDataDirectory);
		if ( !directory.exists() && !directory.mkdir())
			return null;

		return directory;
	}

	/**
	 * Returns true if the user's rule scripts directory exists.
	 * @return true if the user's rule scripts directory exists
	 */
	public boolean exist_user_rule_scripts_directory() {
		if ( null == _parentDirectory || null == _rootDirectory)
			return false;

		return exist_user_rule_scripts_directory( _rootDirectory);
	}

	/**
	 * @param rootDirectory
	 * @return
	 */
	private boolean exist_user_rule_scripts_directory(File rootDirectory) {
		File directory = new File( rootDirectory, Constant._userRuleScriptsInternalRelativePathName);
		return ( directory.exists() && directory.isDirectory());
	}

	/**
	 * Returns the user's rule scripts directory.
	 * @return the user's rule scripts directory
	 */
	public File get_user_rule_scripts_directory() {
		if ( !setup_work_directory())
			return null;

		return get_user_rule_scripts_directory( _rootDirectory);
	}

	/**
	 * @param rootDirectory
	 * @return
	 */
	private File get_user_rule_scripts_directory(File rootDirectory) {
		File directory = new File( rootDirectory, Constant._userRuleScriptsInternalRelativePathName);
		if ( !directory.exists() || !directory.isDirectory())
			return null;

		return directory;
	}

	/**
	 * Returns true if the user's jar files directory exists.
	 * @return true if the user's jar files directory exists
	 */
	public boolean exist_user_rule_jarFiles_directory() {
		if ( null == _parentDirectory || null == _rootDirectory)
			return false;

		return exist_user_rule_jarFiles_directory( _rootDirectory);
	}

	/**
	 * @param rootDirectory
	 * @return
	 */
	private boolean exist_user_rule_jarFiles_directory(File rootDirectory) {
		File directory = new File( rootDirectory, Constant._userRuleJarFilesInternalRelativePathName);
		return ( directory.exists() && directory.isDirectory());
	}

	/**
	 * Returns the user's jar files directory.
	 * @return the user's jar files directory
	 */
	public File get_user_rule_jarFiles_directory() {
		if ( !setup_work_directory())
			return null;

		return get_user_rule_jarFiles_directory( _rootDirectory);
	}

	/**
	 * @param rootDirectory
	 * @return
	 */
	private File get_user_rule_jarFiles_directory(File rootDirectory) {
		File directory = new File( rootDirectory, Constant._userRuleJarFilesInternalRelativePathName);
		if ( !directory.exists() || !directory.isDirectory())
			return null;

		return directory;
	}

	/**
	 * Returns true if the gaming data directory exists.
	 * @return true if the gaming data directory exists
	 */
	public boolean exist_gaming_data_directory() {
		if ( null == _parentDirectory || null == _rootDirectory)
			return false;

		return exist_gaming_data_directory( _rootDirectory);
	}

	/**
	 * @param rootDirectory
	 * @return
	 */
	private boolean exist_gaming_data_directory(File rootDirectory) {
		File directory = new File( rootDirectory, Constant._gamingDataDirectory);
		return ( directory.exists() && directory.isDirectory());
	}

	/**
	 * Returns the gaming data directory.
	 * @return the gaming data directory
	 */
	public File get_gaming_data_directory() {
		if ( !setup_work_directory())
			return null;

		return get_gaming_data_directory( _rootDirectory);
	}

	/**
	 * @param rootDirectory
	 * @return
	 */
	private File get_gaming_data_directory(File rootDirectory) {
		File directory = new File( rootDirectory, Constant._gamingDataDirectory);
		if ( !directory.exists() && !directory.mkdir())
			return null;

		return directory;
	}

	/**
	 * Returns true if the document file exists.
	 * @return true if the document file exists
	 */
	public boolean exist_document_file() {
		if ( null == _parentDirectory || null == _rootDirectory)
			return false;

		File file = new File( _rootDirectory.getAbsolutePath() + "/" + Constant._documentHtmlFilename);
		return ( file.exists() && file.isFile());
	}

	/**
	 * Returns the document file.
	 * @return the document file
	 */
	public File get_document_file() {
		if ( !setup_work_directory())
			return null;

		File file = new File( _rootDirectory.getAbsolutePath() + "/" + Constant._documentHtmlFilename);
		if ( !file.exists())
			return null;

		return file;
	}

	/**
	 * @param values
	 * @return
	 */
	public boolean is_valid_file(String[] values) {
		if ( !exist_user_data_directory())
			return true;

		File userDataDirectory = get_user_data_directory();
		if ( null == userDataDirectory)
			return false;

		if ( !is_valid_directory( values[ 0], userDataDirectory))
			return false;

		if ( values[ 1].equals( ""))
			return true;

		if ( values[ 1].matches( "[\\.]+.*"))
			return false;

		File directory = values[ 0].equals( "") ? userDataDirectory : new File( userDataDirectory.getAbsolutePath() + "/" + values[ 0]);
		if ( !directory.exists())
			return true;

		File file = new File( directory, values[ 1]);
		if ( !file.exists())
			return true;

		if ( !file.isFile())
			return false;

		return true;
	}

	/**
	 * @param path
	 * @param userDataDirectory
	 * @return
	 */
	private boolean is_valid_directory(String path, File userDataDirectory) {
		if ( path.equals( ""))
			return true;

		String[] words = path.split( "/");
		if ( null == words || 0 == words.length)
			return false;

		File directory = userDataDirectory;
		for ( int i = 0; i < words.length; ++i) {
			if ( words[ i].equals( "") || words[ i].matches( "[\\.]+.*"))
				return false;

			directory = new File( directory, words[ i]);
			if ( !directory.exists())
				return true;

			if ( !directory.isDirectory())
				return false;
		}

		return true;
	}

	/**
	 * Invoked when the data is changed.
	 */
	public void modified() {
		_modified = true;

		MainFrame.get_instance().setTitle(
			ResourceManager.get_instance().get( "application.title")
			+ " - [" + ( CommentManager.get_instance()._title.equals( "") ? ResourceManager.get_instance().get( "application.no.title") : CommentManager.get_instance()._title) + "]"
			+ ( ( null == _currentFile) ? "" : " <" + _currentFile.getName() + ">")
			+ " " + ResourceManager.get_instance().get( "state.edit.modified"));
//		String title = ResourceManager.get_instance().get( "application.title")
//			+ ( ( null == _currentFile) ? "" : ( " - " + _currentFile.getName()))
//			+ ResourceManager.get_instance().get( "state.edit.modified");
//		if ( !MainFrame.get_instance().getTitle().equals( title))
//			MainFrame.get_instance().setTitle( title);
	}

	/**
	 * Returns true if the data is changed.
	 * @return true if the data is changed
	 */
	public boolean isModified() {
		return _modified;
	}

	/**
	 * Returns the current selected layer object.
	 * @return the current selected layer object
	 */
	public Layer get_current_layer() {
		return get( _currentLayer);
	}

	/**
	 * Updates the size of the animation area.
	 * @param component the base class for all Swing components
	 */
	public void update_preferred_size(JComponent component) {
		get( _currentLayer).update_preferred_size( component);
	}

	/**
	 * Updates the size of the editing area.
	 * @param component the base class for all Swing components
	 */
	public void resize(JComponent component) {
		if ( 0 == component.getWidth() || 0 == component.getHeight())
			return;

		_bufferedImage = new BufferedImage( component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_RGB);
	}

	/**
	 * Inserts the new layer.
	 * @param component the base class for all Swing components
	 */
	public void insert(JComponent component) {
		insertElementAt( new Layer(), _currentLayer);
		++_currentLayer;
		change( _currentLayer - 1, component);

		Observer.get_instance().modified();
	}

	/**
	 * Appends the new layer.
	 * @param component the base class for all Swing components
	 */
	public void append(JComponent component) {
		add( new Layer());

		Observer.get_instance().modified();
	}

	/**
	 * Changes the current layer into the specified one.
	 * @param layer the specified one
	 * @param component the base class for all Swing components
	 */
	public void change(int layer, JComponent component) {
		if ( layer == _currentLayer)
			return;

		if ( layer >= size() || _currentLayer >= size())
			return;

		Layer previousLayer = get( _currentLayer);
		previousLayer.on_leave( component);

		_currentLayer = layer;

		Layer newLayer = get( _currentLayer);
		newLayer.on_enter( previousLayer, component);

		resize( component);
		update_preferred_size( component);

		component.repaint();
	}

	/**
	 * Returns true for appending the specified new layer.
	 * @param newLayer the specified new layer
	 * @param expressionMap the expression hashtable
	 * @param imagePropertyMap the image property hashtable
	 * @param rootDirectory source data's root directory
	 * @param component the base class for all Swing components
	 * @return true for appending the specified new layer
	 */
	public boolean append(Layer newLayer, TreeMap<String, Expression> expressionMap, TreeMap<String, ImageProperty> imagePropertyMap, File rootDirectory, JComponent component) {
		Layer drawObjects = new Layer();
		drawObjects.addAll( newLayer);
		get_drawObjects( drawObjects);
		if ( !newLayer.can_paste( drawObjects))
			return false;

		if ( !update_expression_manager( expressionMap, newLayer))
			return false;

		if ( !update_files( imagePropertyMap, rootDirectory))
			return false;

		newLayer.update_stage_manager();

		newLayer._name = "";
		//new_layer.select_all( true);
		add( newLayer);

		Observer.get_instance().modified();
		MainFrame.get_instance().append_layer();

		LogManager.get_instance().update_all();
		StageManager.get_instance().update();
		ExperimentManager.get_instance().update_all();

		resize( component);
		update_preferred_size( component);

		return true;
	}

	/**
	 * @param expressionMap
	 * @param newLayer
	 * @return
	 */
	private boolean update_expression_manager(TreeMap<String, Expression> expressionMap, Layer newLayer) {
		if ( null == expressionMap)
			return true;

		VisualShellExpressionManager usedExpressionMap = new VisualShellExpressionManager();
		newLayer.get_used_expressions( expressionMap, usedExpressionMap);

		Map<String, String> functionNameMap = get_function_name_map( usedExpressionMap);

		update_argument( usedExpressionMap, functionNameMap);

		update_function( usedExpressionMap, functionNameMap);

		update_function( newLayer, functionNameMap);

		newLayer.update_expression( usedExpressionMap);

		VisualShellExpressionManager.get_instance().append( usedExpressionMap);

		return true;
	}

	/**
	 * @param usedExpressionMap
	 * @return
	 */
	private Map<String, String> get_function_name_map(VisualShellExpressionManager usedExpressionMap) {
		Map<String, String> functionNameMap = new HashMap<String, String>();
		Iterator iterator = usedExpressionMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Expression expression = ( Expression)entry.getValue();
			functionNameMap.put( expression._value[ 0], expression._value[ 0]);
		}

		iterator = usedExpressionMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Expression expression = ( Expression)entry.getValue();
			update_function_name_map( expression, functionNameMap);
		}

		return functionNameMap;
	}

	/**
	 * @param expression
	 * @param functionNameMap
	 */
	private void update_function_name_map(Expression expression, Map<String, String> functionNameMap) {
		// TODO 2014.2.13
//		if ( null == VisualShellExpressionManager.get_instance().get( expression._value[ 0]))
//			return;

		Expression originalExpression = VisualShellExpressionManager.get_instance().get( expression._value[ 0]);
		if ( null == originalExpression)
			return;

		if ( expression.same_as_logically( originalExpression))
			return;
	
		Vector<String> functionNames = new Vector<String>( functionNameMap.values());

		int index = 1;
		while ( true) {
			String newFunctionName = ( expression._value[ 0] + index);
			if ( null == VisualShellExpressionManager.get_instance().get( newFunctionName)
				&& !functionNames.contains( newFunctionName)) {
				functionNameMap.put( expression._value[ 0], newFunctionName);
				break;
			}

			++index;
		}
	}

	/**
	 * @param usedExpressionMap
	 * @param functionNameMap
	 */
	private void update_argument(VisualShellExpressionManager usedExpressionMap, Map<String, String> functionNameMap) {
		Iterator iterator = functionNameMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String originalFunctionName = ( String)entry.getKey();
			String newFunctionName = ( String)entry.getValue();
			if ( newFunctionName.equals( originalFunctionName))
				continue;

			Expression expression = usedExpressionMap.get( originalFunctionName);
			if ( null == expression)
				continue;

			expression.update_expression( usedExpressionMap, originalFunctionName, newFunctionName);
		}
	}

	/**
	 * @param usedExpressionMap
	 * @param functionNameMap
	 */
	private void update_function(VisualShellExpressionManager usedExpressionMap, Map<String, String> functionNameMap) {
		Iterator iterator = functionNameMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String originalFunctionName = ( String)entry.getKey();
			String newFunctionName = ( String)entry.getValue();
			if ( newFunctionName.equals( originalFunctionName))
				continue;

			Expression expression = usedExpressionMap.get( originalFunctionName);
			if ( null == expression)
				continue;

			rename_function( usedExpressionMap, originalFunctionName, newFunctionName);

			usedExpressionMap.remove( originalFunctionName);
			expression._value[ 0] = newFunctionName;
			usedExpressionMap.put( newFunctionName, expression);
		}
	}

	/**
	 * @param usedExpressionMap
	 * @param originalFunctionName
	 * @param newFunctionName
	 */
	private void rename_function(VisualShellExpressionManager usedExpressionMap, String originalFunctionName, String newFunctionName) {
		Iterator iterator = usedExpressionMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Expression expression = ( Expression)entry.getValue();
			expression.update_function( usedExpressionMap, originalFunctionName, newFunctionName);
		}
	}

	/**
	 * @param newLayer
	 * @param functionNameMap
	 * @return
	 */
	private boolean update_function(Layer newLayer, Map<String, String> functionNameMap) {
		boolean result = false;
		Iterator iterator = functionNameMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String originalFunctionName = ( String)entry.getKey();
			String newFunctionName = ( String)entry.getValue();
			if ( newFunctionName.equals( originalFunctionName))
				continue;

			if ( newLayer.update_function( originalFunctionName, newFunctionName))
				result = true;
		}
		return result;
	}

	/**
	 * @param rootDirectory 
	 * @param imagePropertyMap 
	 * @return
	 */
	private boolean update_files(TreeMap<String, ImageProperty> imagePropertyMap, File rootDirectory) {
		// ファイルの更新
		// /images、/thumbnailsと/userdataのバックアップを作成
		File images = null;
		File thumbnails = null;
		File userdata = null;

		if ( exist_image_directory()) {
			try {
				images = File.createTempFile( "images", null);
			} catch (IOException e) {
				//e.printStackTrace();
				return false;
			}

			if ( !ZipUtility.compress( images, get_image_directory(), _rootDirectory))
				return false;
		}

		if ( exist_thumbnail_image_directory()) {
			try {
				thumbnails = File.createTempFile( "thumbnails", null);
			} catch (IOException e) {
				//e.printStackTrace();
				if ( null != images)
					images.delete();
				return false;
			}

			if ( !ZipUtility.compress( thumbnails, get_thumbnail_image_directory(), _rootDirectory)) {
				if ( null != images)
					images.delete();
				return false;
			}
		}

		if ( exist_user_data_directory()) {
			try {
				userdata = File.createTempFile( "userdata", null);
			} catch (IOException e) {
				//e.printStackTrace();
				if ( null != images)
					images.delete();
				if ( null != thumbnails)
					thumbnails.delete();
				return false;
			}

			if ( !ZipUtility.compress( userdata, get_user_data_directory(), _rootDirectory)) {
				if ( null != images)
					images.delete();
				if ( null != thumbnails)
					thumbnails.delete();
				return false;
			}
		}

		if ( !update_image( imagePropertyMap, rootDirectory)) {
			// バックアップを利用して/imagesと/thumbnailsをrestore後、バックアップを削除
			if ( null != images) {
				ZipUtility.decompress( images, _rootDirectory);
				images.delete();
			}
			if ( null != thumbnails) {
				ZipUtility.decompress( thumbnails, _rootDirectory);
				thumbnails.delete();
			}
			return false;
		}

		if ( !update_user_data_files( rootDirectory)) {
			// バックアップを利用して/images、/thumbnailsと/userdataをrestore後、バックアップを削除
			if ( null != images) {
				ZipUtility.decompress( images, _rootDirectory);
				images.delete();
			}
			if ( null != thumbnails) {
				ZipUtility.decompress( thumbnails, _rootDirectory);
				thumbnails.delete();
			}
			if ( null != userdata) {
				ZipUtility.decompress( userdata, _rootDirectory);
				userdata.delete();
			}
			return false;
		}

		// バックアップを削除
		if ( null != images)
			images.delete();
		if ( null != thumbnails)
			thumbnails.delete();
		if ( null != userdata)
			userdata.delete();

		return true;
	}

	/**
	 * @param imagePropertyMap
	 * @param rootDirectory
	 * @return
	 */
	private boolean update_image(TreeMap<String, ImageProperty> imagePropertyMap, File rootDirectory) {
		List<String> overwrites = new ArrayList<String>();

		Iterator iterator = imagePropertyMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String filename = ( String)entry.getKey();
			if ( null != ImagePropertyManager.get_instance().get( filename)) {
				String result = ( String)OptionPane.executeOptionDialog(
					filename + " - " + ResourceManager.get_instance().get( "thumbnail.confirm.overwrite.message"),
					JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, _overwriteOptions, _overwriteOptions[ 0],
					MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"));
				if ( result.equals( _overwriteOptions[ 1]))			// No
					continue;
				else if ( result.equals( _overwriteOptions[ 2]))	// Yes to all
					return on_yes_to_all( imagePropertyMap, rootDirectory);
				else if ( result.equals( _overwriteOptions[ 3]))	// No to all
					return on_no_to_all( imagePropertyMap, rootDirectory);
				else
					overwrites.add( filename);
			}
		}

		return append_image( imagePropertyMap, overwrites, rootDirectory);
	}

	/**
	 * @param imagePropertyMap
	 * @param overwrites
	 * @param rootDirectory
	 * @return
	 */
	private boolean append_image(TreeMap<String, ImageProperty> imagePropertyMap, List<String> overwrites, File rootDirectory) {
		Iterator iterator = imagePropertyMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String filename = ( String)entry.getKey();
			if ( null == ImagePropertyManager.get_instance().get( filename)) {
				if ( !append_image( filename, ( ImageProperty)entry.getValue(), rootDirectory))
					return false;
			} else {
				if ( overwrites.contains( filename) && !append_image( filename, ( ImageProperty)entry.getValue(), rootDirectory))
					return false;
			}
		}
		return true;
	}

	/**
	 * @param imagePropertyMap
	 * @param rootDirectory
	 * @return
	 */
	private boolean on_yes_to_all(TreeMap<String, ImageProperty> imagePropertyMap, File rootDirectory) {
		Iterator iterator = imagePropertyMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String filename = ( String)entry.getKey();
			if ( !append_image( filename, ( ImageProperty)entry.getValue(), rootDirectory))
				return false;
		}
		return true;
	}

	/**
	 * @param imagePropertyMap
	 * @param rootDirectory
	 * @return
	 */
	private boolean on_no_to_all(TreeMap<String, ImageProperty> imagePropertyMap, File rootDirectory) {
		Iterator iterator = imagePropertyMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String filename = ( String)entry.getKey();
			if ( null != ImagePropertyManager.get_instance().get( filename))
				continue;

			if ( !append_image( filename, ( ImageProperty)entry.getValue(), rootDirectory))
				return false;
		}
		return true;
	}

	/**
	 * @param filename
	 * @param value
	 * @param rootDirectory
	 * @return
	 */
	private boolean append_image(String filename, ImageProperty imageProperty, File rootDirectory) {
		if ( !FileUtility.copy( new File( get_image_directory( rootDirectory), filename), new File( get_image_directory(), filename)))
			return false;

		if ( !FileUtility.copy( new File( get_thumbnail_image_directory( rootDirectory), filename), new File( get_thumbnail_image_directory(), filename)))
			return false;

		ImagePropertyManager.get_instance().put( filename, imageProperty);

		return true;
	}

	/**
	 * @param rootDirectory
	 * @return
	 */
	private boolean update_user_data_files(File rootDirectory) {
		// ファイル変数及び初期データファイルを取り込む
		_option = "";
		return append_user_data_file( get_user_data_directory( rootDirectory), get_user_data_directory( rootDirectory));
	}

	/**
	 * @param directory
	 * @param userDataDirectory
	 * @return
	 */
	private boolean append_user_data_file(File directory, File userDataDirectory) {
		File[] files = directory.listFiles();
		for ( File file:files) {
			File destPath = new File( get_user_data_directory(),
				( file.getAbsolutePath().substring( userDataDirectory.getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/"));
			if ( file.isDirectory()) {
				if ( !destPath.exists()) {
					if ( !destPath.mkdirs()) {
						JOptionPane.showMessageDialog(
							MainFrame.get_instance(),
							ResourceManager.get_instance().get( "user.data.file.create.directory.error.message")
								+ "\n" + ( destPath.getAbsolutePath().substring( get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/"),
							ResourceManager.get_instance().get( "application.title"),
							JOptionPane.ERROR_MESSAGE );
						return false;
					}
				} else {
					if ( !destPath.isDirectory()) {
						JOptionPane.showMessageDialog(
							MainFrame.get_instance(),
							ResourceManager.get_instance().get( "user.data.file.copy.error.message")
								+ "\n" + ( destPath.getAbsolutePath().substring( get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/"),
							ResourceManager.get_instance().get( "application.title"),
							JOptionPane.ERROR_MESSAGE );
						return false;
					}
				}
				if ( !append_user_data_file( file, userDataDirectory))
					return false;
			} else {
				if ( destPath.getParentFile().exists() && !destPath.getParentFile().isDirectory()) {
					JOptionPane.showMessageDialog(
						MainFrame.get_instance(),
						ResourceManager.get_instance().get( "user.data.file.copy.error.message")
							+ "\n" + ( destPath.getAbsolutePath().substring( get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/"),
						ResourceManager.get_instance().get( "application.title"),
						JOptionPane.ERROR_MESSAGE );
					return false;
				}

				if ( !destPath.getParentFile().exists() && !destPath.getParentFile().mkdirs()) {
					JOptionPane.showMessageDialog(
						MainFrame.get_instance(),
						ResourceManager.get_instance().get( "user.data.file.create.directory.error.message")
							+ "\n" + ( destPath.getParentFile().getAbsolutePath().substring( get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/"),
						ResourceManager.get_instance().get( "application.title"),
						JOptionPane.ERROR_MESSAGE );
					return false;
				}

				if ( !destPath.exists()) {
					if ( !FileUtility.copy( file, destPath)) {
						JOptionPane.showMessageDialog(
							MainFrame.get_instance(),
							ResourceManager.get_instance().get( "user.data.file.copy.error.message")
								+ "\n" + ( destPath.getAbsolutePath().substring( get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/"),
							ResourceManager.get_instance().get( "application.title"),
							JOptionPane.ERROR_MESSAGE );
						return false;
					}
				} else {
					if ( destPath.isDirectory()) {
						JOptionPane.showMessageDialog(
							MainFrame.get_instance(),
							ResourceManager.get_instance().get( "user.data.file.copy.error.message")
								+ "\n" + ( destPath.getAbsolutePath().substring( get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/"),
							ResourceManager.get_instance().get( "application.title"),
							JOptionPane.ERROR_MESSAGE );
						return false;
					} else {
						if ( _option.equals( _overwriteOptions[ 2]))	{	// Yes to all
							if ( !FileUtility.copy( file, destPath)) {
								JOptionPane.showMessageDialog(
									MainFrame.get_instance(),
									ResourceManager.get_instance().get( "user.data.file.copy.error.message")
										+ "\n" + ( destPath.getAbsolutePath().substring( get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/"),
									ResourceManager.get_instance().get( "application.title"),
									JOptionPane.ERROR_MESSAGE );
								return false;
							}
						} else if ( _option.equals( _overwriteOptions[ 3])) {	// No to all
							continue;
						} else {
							_option = ( String)OptionPane.executeOptionDialog(
								( destPath.getAbsolutePath().substring( get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/")
								+ " - " + ResourceManager.get_instance().get( "user.data.file.confirm.overwrite.message"),
								JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, _overwriteOptions, _overwriteOptions[ 0],
								MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"));
							if ( _option.equals( _overwriteOptions[ 1])	// No
								|| _option.equals( _overwriteOptions[ 3]))	// No to all
								continue;
							else {
								if ( !FileUtility.copy( file, destPath)) {
									JOptionPane.showMessageDialog(
										MainFrame.get_instance(),
										ResourceManager.get_instance().get( "user.data.file.copy.error.message")
											+ "\n" + ( destPath.getAbsolutePath().substring( get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/"),
										ResourceManager.get_instance().get( "application.title"),
										JOptionPane.ERROR_MESSAGE );
									return false;
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * @param type
	 * @param name
	 * @param point
	 * @param component
	 * @return
	 */
	public boolean append_object(String type, String name, Point point, JComponent component) {
		if ( !get( _currentLayer).append_object( type, get_unique_id(), name, point, component))
			return false;

		Observer.get_instance().modified();

		return true;
	}

	/**
	 * @param type
	 * @param name
	 * @param point
	 * @param component
	 * @return
	 */
	public DrawObject append_object2(String type, String name, Point point, JComponent component) {
		return get( _currentLayer).append_object2( type, get_unique_id(), name, point, component);
	}

	/**
	 * Returns the unique integer for a object id.
	 * @return the unique integer for a object id
	 */
	public String get_unique_gis_id() {
		int id = 0;
		while ( true) {
			String gis = ( "gis" + String.valueOf( id));
			if ( is_unique_gis_id( gis))
				return gis;

			++id;
		}
	}

	/**
	 * @param gis
	 * @return
	 */
	private boolean is_unique_gis_id(String gis) {
		for ( Layer layer:this) {
			if ( !layer.is_unique_gis_id( gis))
				return false;
		}
		return true;
	}

	/**
	 * Returns the unique integer for a object id.
	 * @return the unique integer for a object id
	 */
	public int get_unique_id() {
		int id = 0;
		while ( !is_unique( id))
			++id;

		return id;
	}

	/**
	 * @param id
	 * @return
	 */
	private boolean is_unique(int id) {
		for ( Layer layer:this) {
			if ( !layer.is_unique( id))
				return false;
		}
		return true;
	}

	/**
	 * Returns true for appending a new object to the current layer.
	 * @param type the object type
	 * @param point the object position
	 * @param component the base class for all Swing components
	 * @return true for appending a new object to the current layer
	 */
	public boolean append_object(String type, Point point, JComponent component) {
		String name = get_new_name( type);
		if ( null == name)
			return false;

		return append_object( type, name, point, component);
	}

	/**
	 * @param type
	 * @return
	 */
	private String get_new_name(String type) {
		int index = 1;
		String name;
		while ( true) {
			name = type + index;
			if ( ( type.equals( "agent") && null != get_agent( name))
				|| ( type.equals( "spot") && null != get_spot( name))
				|| ( type.equals( "agent_role") && null != get_agent_role( name))
				|| ( type.equals( "spot_role") && null != get_spot_role( name))
				|| ( type.equals( "chart") && null != get_chart( name))) {
				++index;
				continue;
			}

			if ( contains( type, name)) {
				++index;
				continue;
			}

			return name;
		}
	}

	/**
	 * Returns the unique name for the specified object.
	 * @param drawObject the specified object
	 * @param pasteCounter the number of the paste
	 * @return the unique name for the specified object
	 */
	public String get_unique_name(DrawObject drawObject, int pasteCounter) {
		String type;
		if ( drawObject instanceof AgentObject)
			type = "agent";
		else if ( drawObject instanceof SpotObject)
			type = "spot";
		else if ( drawObject instanceof AgentRole)
			type = "agent_role";
		else if ( drawObject instanceof SpotRole)
			type = "spot_role";
		else
			type = "chart";

		int index = pasteCounter;
		String name;
		while ( true) {
			name = ( drawObject._name + "(copy" + index + ")");
			if ( ( type.equals( "agent") && null != get_agent( name))
				|| ( type.equals( "spot") && null != get_spot( name))
				|| ( type.equals( "agent_role") && null != get_agent_role( name))
				|| ( type.equals( "spot_role") && null != get_spot_role( name))
				|| ( type.equals( "chart") && null != get_chart( name))) {
				++index;
				continue;
			}

			if ( contains( type, name)) {
				++index;
				continue;
			}

			return name;
		}
	}

	/**
	 * Returns true for appending the specified objects successfully.
	 * @param agentDataMap the specified agent objects hashtable
	 * @param spotDataMap the specified spot objects hashtable
	 * @param agentRoleDataMap the specified agent role objects hashtable
	 * @param spotRoleDataMap the specified spot role objects hashtable
	 * @param agentRoleConnectionMap the specified agent role connection objects hashtable
	 * @param spotRoleConnectionMap the specified spot role connection objects hashtable
	 * @param initialStageDataList the specified initial stage objects hashtable
	 * @param mainStageDataList the specified main stage objects hashtable
	 * @param terminalStageDataList the specified terminal stage objects hashtable
	 * @param simulationDataMap the specified simulation data hashtable
	 * @param visualShellExpressionMap the specified expression objects hashtable
	 * @param otherScripts the specified scripts
	 * @param commentDataMap the specified agent common data hashtable
	 * @param component the base class for all Swing components
	 * @return true for appending the specified objects successfully
	 */
	public boolean append(Map<String, EntityData> agentDataMap, Map<String, EntityData> spotDataMap,
		Map<String, RoleData> agentRoleDataMap, Map<String, RoleData> spotRoleDataMap,
		Map<RoleData, String[]> agentRoleConnectionMap, Map<RoleData, String[]> spotRoleConnectionMap,
		List<Stage> initialStageDataList, List<Stage> mainStageDataList, List<Stage> terminalStageDataList,
		Map<String, String[]> simulationDataMap,
		TreeMap<String, Expression> visualShellExpressionMap,
		String otherScripts,
		Map<String, String> commentDataMap,
		JComponent component) {
		if ( !append_agent( agentDataMap, component))
			return false;

		if ( !append_spot( spotDataMap, component))
			return false;

		if ( !append_agent_role( agentRoleDataMap, component))
			return false;

		if ( !append_spot_role( spotRoleDataMap, component))
			return false;

		if ( !connect( "agent_role", agentRoleConnectionMap, component))
			return false;

		if ( !connect( "spot_role", spotRoleConnectionMap, component))
			return false;

		if ( !append_initial_stage( initialStageDataList))
			return false;

		if ( !append_main_stage( mainStageDataList))
			return false;

		if ( !append_terminal_stage( terminalStageDataList))
			return false;

		if ( !SimulationManager.get_instance().update( simulationDataMap))
			return false;

		if ( !VisualShellExpressionManager.get_instance().replace( visualShellExpressionMap))
			return false;

		if ( !OtherScriptsManager.get_instance().append( otherScripts))
			return false;

		if ( !CommentManager.get_instance().update( commentDataMap))
			return false;

		Observer.get_instance().on_update_object( "probability");
		Observer.get_instance().on_update_object( "collection");
		Observer.get_instance().on_update_object( "list");
		Observer.get_instance().on_update_object( "map");
		Observer.get_instance().on_update_object( "keyword");
		Observer.get_instance().on_update_object( "number object");
		Observer.get_instance().on_update_object( "role variable");
		Observer.get_instance().on_update_object( "time variable");
		Observer.get_instance().on_update_object( "spot variable");
		Observer.get_instance().on_update_object( "class variable");
		Observer.get_instance().on_update_object( "file");
		Observer.get_instance().on_update_object( "exchange algebra");
		Observer.get_instance().on_update_object( "extransfer");
		Observer.get_instance().on_update_entityBase( true);
		Observer.get_instance().on_update_role( true);
		Observer.get_instance().on_update_stage();
		Observer.get_instance().on_update_model();
		Observer.get_instance().on_update_expression();
		Observer.get_instance().modified();

		return true;
	}

	/**
	 * @param agentDataMap
	 * @param component
	 * @return
	 */
	private boolean append_agent(Map<String, EntityData> agentDataMap, JComponent component) {
		Iterator iterator = agentDataMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			EntityData entityData = ( EntityData)entry.getValue();
			AgentObject agentObject = get_agent( name);
			if ( null == agentObject) {
				agentObject = ( AgentObject)get( _currentLayer).append_object( "agent", get_unique_id(), entityData, component);
				if ( null == agentObject)
					return false;

				agentObject._multi = entityData.is_multi();
			}

			if ( !agentObject.update( entityData))
				return false;
		}
		return true;
	}

	/**
	 * @param spotDataMap
	 * @param component
	 * @return
	 */
	private boolean append_spot(Map<String, EntityData> spotDataMap, JComponent component) {
		Iterator iterator = spotDataMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			EntityData entityData = ( EntityData)entry.getValue();
			SpotObject spotObject = get_spot( name);
			if ( null == spotObject) {
				spotObject = ( SpotObject)get( _currentLayer).append_object( "spot", get_unique_id(), entityData, component);
				if ( null == spotObject)
					return false;

				spotObject._multi = entityData.is_multi();
			}

			if ( !spotObject.update( entityData))
				return false;
		}
		return true;
	}

	/**
	 * @param agentRoleDataMap
	 * @param component
	 * @return
	 */
	private boolean append_agent_role(Map<String, RoleData> agentRoleDataMap, JComponent component) {
		Iterator iterator = agentRoleDataMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			RoleData roleData = ( RoleData)entry.getValue();
			AgentRole agentRole = get_agent_role( name);
			if ( null == agentRole) {
				agentRole = ( AgentRole)get( _currentLayer).append_object( "agent_role", get_unique_id(), roleData, component);
				if ( null == agentRole)
					return false;
			}

			if ( !agentRole.update( roleData))
				return false;
		}
		return true;
	}

	/**
	 * @param spotRoleDataMap
	 * @param component
	 * @return
	 */
	private boolean append_spot_role(Map<String, RoleData> spotRoleDataMap, JComponent component) {
		Iterator iterator = spotRoleDataMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			RoleData roleData = ( RoleData)entry.getValue();
			SpotRole spotRole = get_spot_role( name);
			if ( null == spotRole) {
				spotRole = ( SpotRole)get( _currentLayer).append_object( "spot_role", get_unique_id(), roleData, component);
				if ( null == spotRole)
					return false;
			}

			if ( !spotRole.update( roleData))
				return false;
		}
		return true;
	}

	/**
	 * @param type
	 * @param roleConnectionMap
	 * @param component
	 * @return
	 */
	private boolean connect(String type, Map<RoleData, String[]> roleConnectionMap, JComponent component) {
		Map<Role, String[]> newRoleConnectionMap = new HashMap<Role, String[]>();
		Iterator iterator = roleConnectionMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			RoleData roleData = ( RoleData)entry.getKey();
			Role role = get( _currentLayer).get_role( roleData._name);
			if ( null == role)
				return false;

			newRoleConnectionMap.put( role, ( String[])entry.getValue());
		}
		return get( _currentLayer).connect( type, newRoleConnectionMap, ( Graphics2D)component.getGraphics());
	}

	/**
	 * @param initialStageDataList
	 * @return
	 */
	private boolean append_initial_stage(List<Stage> initialStageDataList) {
		for ( Stage stage:initialStageDataList)
			StageManager.get_instance().append_initial_stage( stage);
		return true;
	}

	/**
	 * @param mainStageDataList
	 * @return
	 */
	private boolean append_main_stage(List<Stage> mainStageDataList) {
		for ( Stage stage:mainStageDataList)
			StageManager.get_instance().append_main_stage( stage);
		return true;
	}

	/**
	 * @param terminalStageDataList
	 * @return
	 */
	private boolean append_terminal_stage(List<Stage> terminalStageDataList) {
		for ( Stage stage:terminalStageDataList)
			StageManager.get_instance().append_terminal_stage( stage);
		return true;
	}

	/**
	 * Returns the number of the objects whose type is the specified one.
	 * @param type the specified type
	 * @return the number of the objects whose type is the specified one
	 */
	public int how_many(String type) {
		int counter = 0;
		for ( Layer layer:this)
			counter += ( type.equals( "drawobject") ? layer.size() : layer.how_many( type));
		return counter;
	}

	/**
	 * Returns the object which contains the specified full name.
	 * @param fullName the specified name
	 * @return the object which contains the specified full name
	 */
	public EntityBase get_entityBase_has_this_name(String fullName) {
		for ( Layer layer:this) {
			EntityBase entityBase = layer.get_entityBase_has_this_name( fullName);
			if ( null != entityBase)
				return entityBase;
		}
		return null;
	}

	/**
	 * Returns the agent object whose name is the specified one.
	 * @param name the specified name
	 * @return the agent object whose name is the specified one
	 */
	public AgentObject get_agent(String name) {
		for ( Layer layer:this) {
			AgentObject agentObject = layer.get_agent( name);
			if ( null != agentObject)
				return agentObject;
		}
		return null;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#get_agent_has_this_name(java.lang.String)
	 */
	public AgentObject get_agent_has_this_name(String fullName) {
		for ( Layer layer:this) {
			AgentObject agentObject = layer.get_agent_has_this_name( fullName);
			if ( null != agentObject)
				return agentObject;
		}
		return null;
	}

	/**
	 * Returns the spot object whose name is the specified one.
	 * @param name the specified name
	 * @return the spot object whose name is the specified one
	 */
	public SpotObject get_spot(String name) {
		for ( Layer layer:this) {
			SpotObject spotObject = layer.get_spot( name);
			if ( null != spotObject)
				return spotObject;
		}
		return null;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#get_spot_has_this_name(java.lang.String)
	 */
	public SpotObject get_spot_has_this_name(String fullName) {
		for ( Layer layer:this) {
			SpotObject spotObject = layer.get_spot_has_this_name( fullName);
			if ( null != spotObject)
				return spotObject;
		}
		return null;
	}

	/**
	 * Returns the role object whose name is the specified one.
	 * @param name the specified name
	 * @return the role object whose name is the specified one
	 */
	public Role get_role(String name) {
		for ( Layer layer:this) {
			Role role = layer.get_role( name);
			if ( null != role)
				return role;
		}
		return null;
	}

	/**
	 * Returns the agent role object whose name is the specified one.
	 * @param name the specified name
	 * @return the agent role object whose name is the specified one
	 */
	public AgentRole get_agent_role(String name) {
		for ( Layer layer:this) {
			AgentRole agentRole = layer.get_agent_role( name);
			if ( null != agentRole)
				return agentRole;
		}
		return null;
	}

	/**
	 * Returns the spot role object whose name is the specified one.
	 * @param name the specified name
	 * @return the spot role object whose name is the specified one
	 */
	public SpotRole get_spot_role(String name) {
		for ( Layer layer:this) {
			SpotRole spotRole = layer.get_spot_role( name);
			if ( null != spotRole)
				return spotRole;
		}
		return null;
	}

	/**
	 * Returns the chart object whose name is the specified one.
	 * @param name the specified name
	 * @return the chart object whose name is the specified one
	 */
	public ChartObject get_chart(String name) {
		for ( Layer layer:this) {
			ChartObject chartObject = layer.get_chart( name);
			if ( null != chartObject)
				return chartObject;
		}
		return null;
	}

	/**
	 * @param entity
	 * @param numberObjectName
	 * @param newType
	 * @return
	 */
	public boolean is_number_object_type_correct(String entity, String numberObjectName, String newType) {
		for ( Layer layer:this) {
			if ( !layer.is_number_object_type_correct( entity, numberObjectName, newType))
				return false;
		}
		return true;
	}

	/**
	 * Returns true if the number variable is correct.
	 * @param type the object type("agent" or "spot")
	 * @param numberObjectName the number variable name
	 * @param numberObjectType the number variable type
	 * @param entityBase the object(agent object or spot object)
	 * @return true if the number variable is correct
	 */
	public boolean is_number_object_correct(String type, String numberObjectName, String numberObjectType, EntityBase entityBase) {
		for ( Layer layer:this) {
			if ( !layer.is_number_object_correct( type, numberObjectName, numberObjectType, entityBase))
				return false;
		}
		return true;
	}

	/**
	 * Returns true for updating the number variable type successfully.
	 * @param type the object type("agent" or "spot")
	 * @param numberObjectName the number variable name
	 * @param numberObjectType the number variable type
	 * @param entityBase the object(agent object or spot object)
	 * @return true for updating the number variable type successfully
	 */
	public boolean update_number_object_type(String type, String numberObjectName, String numberObjectType, EntityBase entityBase) {
		boolean result = false;
		for ( Layer layer:this) {
			if ( layer.update_number_object_type( type, numberObjectName, numberObjectType, entityBase))
				result = true;
		}
		return result;
	}

	/**
	 * Returns true if this object contains the specified object.
	 * @param type the specified type
	 * @param fullName the specified name
	 * @return true if this object contains the specified object
	 */
	public boolean contains(String type, String fullName) {
		for ( Layer layer:this) {
			if ( layer.contains( type, fullName))
				return true;
		}

		return false;
	}

	/**
	 * Returns true if this object contains the specified object.
	 * @param type the specified type
	 * @param name the specified name
	 * @param number the specified number
	 * @param exception the exception name
	 * @return true if this object contains the specified object
	 */
	public boolean contains(String type, String name, String number, String exception) {
		for ( Layer layer:this) {
			if ( layer.contains( type, name, number, exception))
				return true;
		}

		return false;
	}

	/**
	 * Returns true if this object contains the object which has the specified data except the specified one.
	 * @param entityBase the specified object
	 * @param name the specified name
	 * @param number the specified number
	 * @return true if this object contains the object which has the specified data except the specified one
	 */
	public boolean contains(EntityBase entityBase, String name, String number) {
		String type;
		if ( entityBase instanceof AgentObject)
			type = "agent";
		else if ( entityBase instanceof SpotObject)
			type = "spot";
		else
			return false;

		for ( Layer layer:this) {
			if ( layer.contains( entityBase, type, name, number)
				|| layer.contains( entityBase, name, number))
				return true;
		}
		return false;
	}

	/**
	 * @param name
	 * @param number
	 * @return
	 */
	public boolean contains_as_object_name(String name, String number) {
		for ( Layer layer:this) {
			if ( layer.contains_as_object_name( name, number))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if this object contains the role object which has the specified data except the specified one.
	 * @param role the specified role object
	 * @param type the specified type
	 * @param name the specified name
	 * @return true if this object contains the role object which has the specified data except the specified one
	 */
	public boolean contains(Role role, String name) {
		String type;
		if ( role instanceof AgentRole)
			type = "agent_role";
		else if ( role instanceof SpotRole)
			type = "spot_role";
		else
			return false;

		for ( Layer layer:this) {
			if ( layer.contains( role, type, name))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if this object contains the role object which has the specified data.
	 * @param type the specified type
	 * @param name the specified name
	 * @return true if this object contains the role object which has the specified data
	 */
	public boolean contains_this_role_name(String type, String name) {
		for ( Layer layer:this) {
			if ( layer.contains_this_role_name( type, name))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if this object contains the object which has the specified data except the specified one.
	 * @param chartObject the specified chart object
	 * @param name the specified name
	 * @return true if this object contains the object which has the specified data except the specified one
	 */
	public boolean contains(ChartObject chartObject, String name) {
		for ( Layer layer:this) {
			if ( layer.contains( chartObject, name))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if this object contains the available chart object.
	 * @return true if this object contains the available chart object
	 */
	public boolean contains_available_chartObject() {
		for ( Layer layer:this) {
			if ( layer.contains_available_chartObject())
				return true;
		}
		return false;
	}

//	/**
//	 * @param name
//	 * @param number
//	 * @return
//	 */
//	public boolean contains_available_chartObject_uses_this_spot_name(String name, String number) {
//		for ( Layer layer:this) {
//			if ( layer.contains_available_chartObject_uses_this_spot_name( name, number))
//				return true;
//		}
//		return false;
//	}

//	/**
//	 * @return
//	 */
//	public boolean contains_spotObject_availabe_chartObject_uses() {
//		Vector<DrawObject> spots = new Vector<DrawObject>();
//		get_spots( spots);
//		for ( DrawObject drawObject:spots) {
//			SpotObject spotObject = ( SpotObject)drawObject;
//			for ( Layer layer:this) {
//				if ( layer.contains_spotObject_availabe_chartObject_uses( spotObject))
//					return true;
//			}
//		}
//		return false;
//	}

	/**
	 * Returns true if this object contains the object which has the specified data.
	 * @param name the specified name
	 * @param number the specified number
	 * @return true if this object contains the object which has the specified data
	 */
	public boolean has_same_name(String name, String number) {
		for ( Layer layer:this) {
			if ( layer.has_same_name( name, number))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if this object contains the agent object which has the specified data.
	 * @param name the specified name
	 * @param number the specified number
	 * @return true if this object contains the agent object which has the specified data
	 */
	public boolean has_same_agent_name(String name, String number) {
		for ( Layer layer:this) {
			if ( layer.has_same_agent_name( name, number))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if this object contains the spot object which has the specified data.
	 * @param name the specified name
	 * @param number the specified number
	 * @return true if this object contains the spot object which has the specified data
	 */
	public boolean has_same_spot_name(String name, String number) {
		for ( Layer layer:this) {
			if ( layer.has_same_spot_name( name, number))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if this object contains the chart object which has the specified data.
	 * @param name the specified name
	 * @param number the specified number
	 * @return true if this object contains the chart object which has the specified data
	 */
	public boolean chartObject_has_same_name(String name, String number) {
		for ( Layer layer:this) {
			if ( layer.chartObject_has_same_name( name, number))
				return true;
		}
		return false;
	}

//	/**
//	 * @param name
//	 * @param number
//	 * @return
//	 */
//	public boolean role_has_same_agent_name(String name, String number) {
//		for ( Layer layer:this) {
//			if ( layer.role_has_same_agent_name( name, number))
//				return true;
//		}
//		return false;
//	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_role_name(java.lang.String)
	 */
	public boolean is_role_name(String name) {
		for ( Layer layer:this) {
			if ( layer.is_role_name( name))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_agent_role_name(java.lang.String)
	 */
	public boolean is_agent_role_name(String name) {
		for ( Layer layer:this) {
			if ( layer.is_agent_role_name( name))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_spot_role_name(java.lang.String)
	 */
	public boolean is_spot_role_name(String name) {
		for ( Layer layer:this) {
			if ( layer.is_spot_role_name( name))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if this object contains the object which has the specified alias.
	 * @param kind the type of the object
	 * @param alias the specified alias
	 * @return true if this object contains the object which has the specified alias
	 */
	public boolean object_contains_this_alias(String kind, String alias) {
		for ( Layer layer:this) {
			if ( layer.object_contains_this_alias( kind, alias))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if this object contains the role object which has the specified alias.
	 * @param alias the specified alias
	 * @return true if this object contains the role object which has the specified alias
	 */
	public boolean role_contains_this_alias(String alias) {
		for ( Layer layer:this) {
			if ( layer.role_contains_this_alias( alias))
				return true;
		}
		return false;
	}

	/**
	 * Sets the selection of all objects.
	 * @param selected whether or not all objects are selected
	 */
	public void select_all(boolean selected) {
		for ( Layer layer:this)
			layer.select_all( selected);
	}

	/**
	 * Selects the all objects in the specified rectangle.
	 * @param rectangle the specified rectangle
	 */
	public void select(Rectangle rectangle) {
		get( _currentLayer).select( rectangle);
	}

	/**
	 * Returns the object which contains the specified position.
	 * @param point the specified position
	 * @return the object which contains the specified position
	 */
	public DrawObject get(Point point) {
		return get( _currentLayer).get( point);
	}

	/**
	 * Returns true if the selected objects exist.
	 * @return true if the selected objects exist
	 */
	public boolean exist_selected_object() {
		return get( _currentLayer).exist_selected_object();
	}

	/**
	 * Gets the selected objects.
	 * @param drawObjects the array for the selected objects
	 */
	public void get_selected(Vector<DrawObject> drawObjects) {
		get( _currentLayer).get_selected( drawObjects);
	}

	/**
	 * @param agents
	 */
	private void get_selected_agents(Vector<DrawObject> agents) {
		get( _currentLayer).get_selected_agents( agents);
	}

	/**
	 * @param spots
	 */
	private void get_selected_spots(Vector<DrawObject> spots) {
		get( _currentLayer).get_selected_spots( spots);
	}

	/**
	 * @param agentRoles
	 */
	private void get_selected_agent_roles(Vector<DrawObject> agentRoles) {
		get( _currentLayer).get_selected_agent_roles( agentRoles);
	}

	/**
	 * @param spotRoles
	 */
	private void get_selected_spot_roles(Vector<DrawObject> spotRoles) {
		get( _currentLayer).get_selected_spot_roles( spotRoles);
	}

	/**
	 * @param charts
	 */
	private void get_selected_charts(Vector<DrawObject> charts) {
		get( _currentLayer).get_selected_charts( charts);
	}

	/**
	 * Gets the clone objects of the selected ones.
	 * @param drawObjects the array for the clone objects
	 */
	public void get_clone_of_selected(Vector<DrawObject> drawObjects) {
		get( _currentLayer).get_clone_of_selected( drawObjects);
	}

	/**
	 * Appends the specified objects to the current layer.
	 * @param drawObjects the array of the specified objects
	 * @param pastePosition the coordinates of the paste position
	 * @param pasteCounter the number of the paste
	 * @param component the base class for all Swing components
	 */
	public void append_clone_of_selected(Vector<DrawObject> drawObjects, Point pastePosition, int pasteCounter, JComponent component) {
		if ( drawObjects.isEmpty())
			return;

		if ( null == pastePosition)
			get( _currentLayer).append_clone_of_selected( drawObjects, pasteCounter, component);
		else {
			Point delta = get_position_delta( drawObjects, pastePosition);
			get( _currentLayer).append_clone_of_selected( drawObjects, delta, pasteCounter, component);
		}
	}

	/**
	 * @param drawObjects
	 * @param pastePosition
	 * @return
	 */
	private Point get_position_delta(Vector<DrawObject> drawObjects, Point pastePosition) {
		DrawObject drawObject = drawObjects.get( 0);
		Point minimum = new Point( drawObject._position);
		for ( int i = 1; i < drawObjects.size(); ++i) {
			drawObject = drawObjects.get( i);
			if ( minimum.x > drawObject._position.x)
				minimum.x = drawObject._position.x;
			if ( minimum.y > drawObject._position.y)
				minimum.y = drawObject._position.y;
		}

		Point delta = new Point( pastePosition.x - minimum.x, pastePosition.y - minimum.y);
		return delta;
	}

	/**
	 * Brings the specified object to top.
	 * @param drawObject the specified object
	 */
	public void bring_to_top(DrawObject drawObject) {
		get( _currentLayer).bring_to_top( drawObject);
	}

	/**
	 * Gets the all objects.
	 * @param drawObjects the array for the all objects
	 */
	public void get_drawObjects(Vector<DrawObject> drawObjects) {
		for ( Layer layer:this)
			layer.get_drawObjects( drawObjects);
	}

	/**
	 * Gets the all agent and spot objects.
	 * @param entityBases the array for the all agent and spot objects
	 */
	public void get_entityBases(Vector<DrawObject> entityBases) {
		for ( Layer layer:this)
			layer.get_entityBases( entityBases);
	}

	/**
	 * Gets the all agent objects.
	 * @param agents the array for the all agent objects
	 */
	public void get_agents(Vector<DrawObject> agents) {
		for ( Layer layer:this)
			layer.get_agents( agents);
	}

	/**
	 * Gets the all spot objects.
	 * @param spots the array for the all spot objects
	 */
	public void get_spots(Vector<DrawObject> spots) {
		for ( Layer layer:this)
			layer.get_spots( spots);
	}

	/**
	 * @param entityBases
	 * @param gis
	 */
	public void get_gis_spots(List<EntityBase> entityBases, String gis) {
		for ( Layer layer:this)
			layer.get_gis_spots( entityBases, gis);
	}

	/**
	 * Gets the all role objects.
	 * @param roles the array for the all role objects
	 */
	public void get_roles(Vector<DrawObject> roles) {
		for ( Layer layer:this)
			layer.get_roles( roles);
	}

	/**
	 * Gets the all agent role objects.
	 * @param agentRoles the array for the all agent role objects
	 */
	public void get_agent_roles(Vector<DrawObject> agentRoles) {
		for ( Layer layer:this)
			layer.get_agent_roles( agentRoles);
	}

	/**
	 * Gets the all spot role objects.
	 * @param spotRoles the array for the all spot role objects
	 */
	public void get_spot_roles(Vector<DrawObject> spotRoles) {
		for ( Layer layer:this)
			layer.get_spot_roles( spotRoles);
	}

	/**
	 * Gets the all chart objects.
	 * @param charts the array for the all chart objects
	 */
	public void get_charts(Vector<DrawObject> charts) {
		for ( Layer layer:this)
			layer.get_charts( charts);
	}

	/**
	 * @return
	 */
	public boolean initial_data_file_exists() {
		for ( Layer layer:this) {
			if ( layer.initial_data_file_exists())
				return true;
		}
		return false;
	}

	/**
	 * @param initialDataFiles
	 */
	public void get_initial_data_files(List<String> initialDataFiles) {
		for ( Layer layer:this)
			layer.get_initial_data_files( initialDataFiles);
	}

	/**
	 * @param exchangeAlgebraInitialDataFiles
	 */
	public void get_exchange_algebra_initial_data_files(List<String> exchangeAlgebraInitialDataFiles) {
		for ( Layer layer:this)
			layer.get_exchange_algebra_initial_data_files( exchangeAlgebraInitialDataFiles);
	}

	/**
	 * Draws the objects on this layer.
	 * @param graphics the graphics object of JAVA
	 * @param component the base class for all Swing components
	 * @param rubberbandStartPoint the start point of the rubberband on editing
	 * @param rubberbandEndPoint the end point of the rubberband on editing
	 * @param connectionStartPoint the start point of the connection line on editing
	 * @param connectionEndPoint the end point of the connection line on editing
	 * @param connectionColor the color of the connection line
	 */
	public void draw(Graphics graphics, JComponent component, Point rubberbandStartPoint, Point rubberbandEndPoint, Point connectionStartPoint, Point connectionEndPoint, Color connectionColor) {
		if ( null == _bufferedImage)
			resize( component);

		Graphics2D graphics2D = ( Graphics2D)_bufferedImage.getGraphics();
		graphics2D.setBackground( _backgroundColor);
		graphics2D.clearRect( 0, 0, _bufferedImage.getWidth(), _bufferedImage.getHeight());

		if ( null != rubberbandStartPoint) {
			graphics2D.setColor( _rubberbandColor);
			graphics2D.drawRect(
				Math.min( rubberbandStartPoint.x, rubberbandEndPoint.x),
				Math.min( rubberbandStartPoint.y, rubberbandEndPoint.y),
				Math.abs( rubberbandStartPoint.x - rubberbandEndPoint.x),
				Math.abs( rubberbandStartPoint.y - rubberbandEndPoint.y));
		}

		get( _currentLayer).draw( graphics2D, component, connectionStartPoint, connectionEndPoint, connectionColor);

		graphics.drawImage( _bufferedImage, 0, 0, component);
		graphics2D.dispose();
	}

	/**
	 * Moves the selected objects to another layer.
	 * @param component the base class for all Swing components
	 * @param frame the Frame from which the dialog is displayed
	 */
	public void move_object(JComponent component, Frame frame) {
		if ( 2 > size())
			return;

		SelectLayerDlg selectLayerDlg = new SelectLayerDlg( frame,
			ResourceManager.get_instance().get( "select.layer.dialog.title"),
			true, _currentLayer, size());
		if ( !selectLayerDlg.do_modal( frame)) {
			select_all( false);
			component.repaint();
			return;
		}

		Vector<DrawObject> drawObjects = new Vector<DrawObject>();
		get_selected( drawObjects);

		Layer layer = get( _currentLayer);
		layer.remove_object( drawObjects);

		layer = get( selectLayerDlg._targetLayer);
		layer.append_object( drawObjects);

		select_all( false);
		component.repaint();

		Observer.get_instance().modified();
	}

	/**
	 * Returns the array of the agent names.
	 * @param containsEmpty whether to contain ""
	 * @return the array of the agent names
	 */
	public String[] get_agent_names(boolean containsEmpty) {
		Vector<String> agentNames = new Vector<String>();
		for ( Layer layer:this)
			layer.get_agent_names( agentNames);

		if ( containsEmpty && !agentNames.contains( ""))
			agentNames.add( "");

		return Tool.quick_sort_string( agentNames, true, false);
	}

	/**
	 * Returns the array of the spot names.
	 * @param containsEmpty whether to contain ""
	 * @return the array of the spot names
	 */
	public String[] get_spot_names(boolean containsEmpty) {
		Vector<String> spotNames = new Vector<String>();
		for ( Layer layer:this)
			layer.get_spot_names( spotNames);

		if ( containsEmpty && !spotNames.contains( ""))
			spotNames.add( "");

		return Tool.quick_sort_string( spotNames, true, false);
	}

	/**
	 * Returns the array of the role names.
	 * @param containsEmpty whether to contain ""
	 * @return the array of the role names
	 */
	public String[] get_role_names(boolean containsEmpty) {
		Vector<String> roleNames = new Vector<String>();
		for ( Layer layer:this)
			layer.get_role_names( roleNames);

		if ( containsEmpty && !roleNames.contains( ""))
			roleNames.add( "");

		return Tool.quick_sort_string( roleNames, true, false);
	}

	/**
	 * Returns the array of the agent role names.
	 * @param containsEmpty whether to contain ""
	 * @return the array of the agent role names
	 */
	public String[] get_agent_role_names(boolean containsEmpty) {
		Vector<String> agentRoleNames = new Vector<String>();
		for ( Layer layer:this)
			layer.get_agent_role_names( agentRoleNames);

		if ( containsEmpty && !agentRoleNames.contains( ""))
			agentRoleNames.add( "");

		return Tool.quick_sort_string( agentRoleNames, true, false);
	}

	/**
	 * Returns the array of the spot role names.
	 * @param containsEmpty whether to contain ""
	 * @return the array of the spot role names
	 */
	public String[] get_spot_role_names(boolean containsEmpty) {
		Vector<String> spotRoleNames = new Vector<String>();
		for ( Layer layer:this)
			layer.get_spot_role_names( spotRoleNames);

		if ( containsEmpty && !spotRoleNames.contains( ""))
			spotRoleNames.add( "");

		return Tool.quick_sort_string( spotRoleNames, true, false);
	}

	/**
	 * Returns the array for the names of the objects which the agent or spot objects have.
	 * @param kind the kind of the object
	 * @param containsEmpty whether to contain ""
	 * @return the array for the names of the objects which the agent or spot objects have
	 */
	public String[] get_object_names(String kind, boolean containsEmpty) {
		Vector<String> objectNames = new Vector<String>();
		for ( Layer layer:this)
			layer.get_object_names( kind, objectNames);

		if ( containsEmpty && !objectNames.contains( ""))
			objectNames.add( "");

		return Tool.quick_sort_string( objectNames, true, false);
	}

	/**
	 * Returns the array for the names of the objects which the agent objects have.
	 * @param kind the kind of the object
	 * @param containsEmpty whether to contain ""
	 * @return the array for the names of the objects which the agent objects have
	 */
	public String[] get_agent_object_names(String kind, boolean containsEmpty) {
		Vector<String> agentObjectNames = new Vector<String>();
		for ( Layer layer:this)
			layer.get_agent_object_names( kind, agentObjectNames);

		if ( containsEmpty && !agentObjectNames.contains( ""))
			agentObjectNames.add( "");

		return Tool.quick_sort_string( agentObjectNames, true, false);
	}

	/**
	 * Returns the array for the names of the objects which the spot objects have.
	 * @param kind the kind of the object
	 * @param containsEmpty whether to contain ""
	 * @return the array for the names of the objects which the spot objects have
	 */
	public String[] get_spot_object_names(String kind, boolean containsEmpty) {
		Vector<String> spotObjectNames = new Vector<String>();
		for ( Layer layer:this)
			layer.get_spot_object_names( kind, spotObjectNames);

		if ( containsEmpty && !spotObjectNames.contains( ""))
			spotObjectNames.add( "");

		return Tool.quick_sort_string( spotObjectNames, true, false);
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public String[] get_agent_exchange_algebra_names(boolean containsEmpty) {
		Vector<String> agentExchangeAlgebraNames = new Vector<String>();
		for ( Layer layer:this)
			layer.get_agent_exchange_algebra_names( agentExchangeAlgebraNames);

		if ( containsEmpty && !agentExchangeAlgebraNames.contains( ""))
			agentExchangeAlgebraNames.add( "");

		return Tool.quick_sort_string( agentExchangeAlgebraNames, true, false);
	}

	/**
	 * @param containsEmpty
	 * @return
	 */
	public String[] get_spot_exchange_algebra_names(boolean containsEmpty) {
		Vector<String> spotExchangeAlgebraNames = new Vector<String>();
		for ( Layer layer:this)
			layer.get_spot_exchange_algebra_names( spotExchangeAlgebraNames);

		if ( containsEmpty && !spotExchangeAlgebraNames.contains( ""))
			spotExchangeAlgebraNames.add( "");

		return Tool.quick_sort_string( spotExchangeAlgebraNames, true, false);
	}

	/**
	 * Returns the array for the names of the objects which the agent or spot objects have.
	 * @param kinds the the array of kind of the object
	 * @param containsEmpty whether to contain ""
	 * @return the array for the names of the objects which the agent or spot objects have
	 */
	public String[] get_object_names(String[] kinds, boolean containsEmpty) {
		Vector<String> objectNames = new Vector<String>();
		for ( Layer layer:this) {
			for ( String kind:kinds)
				layer.get_object_names( kind, objectNames);
		}

		if ( containsEmpty && !objectNames.contains( ""))
			objectNames.add( "");

		return Tool.quick_sort_string( objectNames, true, false);
	}

	/**
	 * Returns the array for the names of the objects which the agent objects have.
	 * @param kinds the the array of kind of the object
	 * @param containsEmpty whether to contain ""
	 * @return the array for the names of the objects which the agent objects have
	 */
	public String[] get_agent_object_names(String[] kinds, boolean containsEmpty) {
		Vector<String> agentObjectNames = new Vector<String>();
		for ( Layer layer:this) {
			for ( String kind:kinds)
				layer.get_agent_object_names( kind, agentObjectNames);
		}

		if ( containsEmpty && !agentObjectNames.contains( ""))
			agentObjectNames.add( "");

		return Tool.quick_sort_string( agentObjectNames, true, false);
	}

	/**
	 * Returns the array for the names of the objects which the spot objects have.
	 * @param kinds the the array of kind of the object
	 * @param containsEmpty whether to contain ""
	 * @return the array for the names of the objects which the spot objects have
	 */
	public String[] get_spot_object_names(String[] kinds, boolean containsEmpty) {
		Vector<String> spotObjectNames = new Vector<String>();
		for ( Layer layer:this) {
			for ( String kind:kinds)
				layer.get_spot_object_names( kind, spotObjectNames);
		}

		if ( containsEmpty && !spotObjectNames.contains( ""))
			spotObjectNames.add( "");

		return Tool.quick_sort_string( spotObjectNames, true, false);
	}

	/**
	 * Returns the array for the names of the number variables which the agent objects have.
	 * @param type the number variable type
	 * @param containsEmpty whether to contain ""
	 * @return the array for the names of the number variables which the agent objects have
	 */
	public String[] get_agent_number_object_names(String type, boolean containsEmpty) {
		Vector<String> agentNumberObjectNames = new Vector<String>();
		for ( Layer layer:this)
			layer.get_agent_number_object_names( type, agentNumberObjectNames);

		if ( containsEmpty && !agentNumberObjectNames.contains( ""))
			agentNumberObjectNames.add( "");

		return Tool.quick_sort_string( agentNumberObjectNames, true, false);
	}

	/**
	 * Returns the array for the names of the number variables which the spot objects have.
	 * @param type the number variable type
	 * @param containsEmpty whether to contain ""
	 * @return the array for the names of the number variables which the spot objects have
	 */
	public String[] get_spot_number_object_names(String type, boolean containsEmpty) {
		Vector<String> spotNumberObjectNames = new Vector<String>();
		for ( Layer layer:this)
			layer.get_spot_number_object_names( type, spotNumberObjectNames);

		if ( containsEmpty && !spotNumberObjectNames.contains( ""))
			spotNumberObjectNames.add( "");

		return Tool.quick_sort_string( spotNumberObjectNames, true, false);
	}

	/**
	 * Gets the class variable hashtable which the agent objects have.
	 * @param classVariableMap the class variable hashtable
	 */
	public void get_agent_class_variable_map(Map<String, ClassVariableObject> classVariableMap) {
		for ( Layer layer:this)
			layer.get_agent_class_variable_map( classVariableMap);
	}

	/**
	 * Gets the class variable hashtable which the spot objects have.
	 * @param classVariableMap the class variable hashtable
	 */
	public void get_spot_class_variable_map(Map<String, ClassVariableObject> classVariableMap) {
		for ( Layer layer:this)
			layer.get_spot_class_variable_map( classVariableMap);
	}

	/**
	 * Returns the class variable whose name is equal to the specified one.
	 * @param functionalObject the specified functional object
	 * @return the class variable whose name is equal to the specified one
	 */
	public ClassVariableObject get_class_variable(FunctionalObject functionalObject) {
		for ( Layer layer:this) {
			ClassVariableObject classVariableObject = layer.get_class_variable( functionalObject);
			if ( null != classVariableObject)
				return classVariableObject;
		}
		return null;
	}

	/**
	 * Returns the class variable whose name is equal to the specified one.
	 * @param prefix the prefix of the object full name
	 * @param classVariableName the specified name
	 * @return the class variable whose name is equal to the specified one
	 */
	public ClassVariableObject get_class_variable(String prefix, String classVariableName) {
		for ( Layer layer:this) {
			ClassVariableObject classVariableObject = layer.get_class_variable( prefix, classVariableName);
			if ( null != classVariableObject)
				return classVariableObject;
		}
		return null;
	}

	/**
	 * Returns the array for the names of the objects which the agent or spot objects except the specified one have.
	 * @param kind the kind of the object
	 * @param objectNames the array for the names
	 * @param entityBase the specified object
	 * @param containsEmpty whether to contain ""
	 * @return the array for the names of the objects which the agent or spot objects except the specified one have
	 */
	public String[] get_object_names(String kind, Vector<String> objectNames, EntityBase entityBase, boolean containsEmpty) {
		for ( Layer layer:this)
			layer.get_object_names( kind, objectNames, entityBase);

		if ( containsEmpty && !objectNames.contains( ""))
			objectNames.add( "");

		return Tool.quick_sort_string( objectNames, true, false);
	}

	/**
	 * Returns the array for the names of the objects which the agent objects except the specified one have.
	 * @param kind the kind of the object
	 * @param agentObjectNames the array for the names
	 * @param entityBase the specified object
	 * @param containsEmpty whether to contain ""
	 * @return the array for the names of the objects which the agent objects except the specified one have
	 */
	public String[] get_agent_object_names(String kind, Vector<String> agentObjectNames, EntityBase entityBase, boolean containsEmpty) {
		for ( Layer layer:this)
			layer.get_agent_object_names( kind, agentObjectNames, entityBase);

		if ( containsEmpty && !agentObjectNames.contains( ""))
			agentObjectNames.add( "");

		return Tool.quick_sort_string( agentObjectNames, true, false);
	}

	/**
	 * Returns the array for the names of the objects which the spot objects except the specified one have.
	 * @param kind the kind of the object
	 * @param spotObjectNames the array for the names
	 * @param entityBase the specified object
	 * @param containsEmpty whether to contain ""
	 * @return the array for the names of the objects which the spot objects except the specified one have
	 */
	public String[] get_spot_object_names(String kind, Vector<String> spotObjectNames, EntityBase entityBase, boolean containsEmpty) {
		for ( Layer layer:this)
			layer.get_spot_object_names( kind, spotObjectNames, entityBase);

		if ( containsEmpty && !spotObjectNames.contains( ""))
			spotObjectNames.add( "");

		return Tool.quick_sort_string( spotObjectNames, true, false);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_object_name(java.lang.String, java.lang.String)
	 */
	public boolean is_object_name(String kind, String name) {
		for ( Layer layer:this) {
			if ( layer.is_object_name( kind, name))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if the object which has the specified name exists except ths specified one.
	 * @param kind the type of the object
	 * @param name the specified name
	 * @param entityBase ths specified object
	 * @return true if the object which has the specified name exists except ths specified one
	 */
	public boolean is_object_name(String kind, String name, EntityBase entityBase) {
		for ( Layer layer:this) {
			if ( layer.is_object_name( kind, name, entityBase))
				return true;
		}
		return false;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_agent_object_name(java.lang.String, java.lang.String)
	 */
	public boolean is_agent_object_name(String kind, String name) {
		for ( Layer layer:this) {
			if ( layer.is_agent_object_name( kind, name))
				return true;
		}
		return false;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_agent_object_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean is_agent_object_name(String kind, String fullName, String name) {
		for ( Layer layer:this) {
			if ( layer.is_agent_object_name( kind, fullName, name))
				return true;
		}
		return false;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_spot_object_name(java.lang.String, java.lang.String)
	 */
	public boolean is_spot_object_name(String kind, String name) {
		for ( Layer layer:this) {
			if ( layer.is_spot_object_name( kind, name))
				return true;
		}
		return false;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_spot_object_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean is_spot_object_name(String kind, String fullName, String name) {
		for ( Layer layer:this) {
			if ( layer.is_spot_object_name( kind, fullName, name))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_agent_number_object_name(java.lang.String, java.lang.String)
	 */
	public boolean is_agent_number_object_name(String numberObjectName, String numberObjectType) {
		for ( Layer layer:this) {
			if ( layer.is_agent_number_object_name( numberObjectName, numberObjectType))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_agent_number_object_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean is_agent_number_object_name(String fullName, String numberObjectName, String numberObjectType) {
		for ( Layer layer:this) {
			if ( layer.is_agent_number_object_name( fullName, numberObjectName, numberObjectType))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_spot_number_object_name(java.lang.String, java.lang.String)
	 */
	public boolean is_spot_number_object_name(String numberObjectName, String numberObjectType) {
		for ( Layer layer:this) {
			if ( layer.is_spot_number_object_name( numberObjectName, numberObjectType))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_spot_number_object_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean is_spot_number_object_name(String fullName, String numberObjectName, String numberObjectType) {
		for ( Layer layer:this) {
			if ( layer.is_spot_number_object_name( fullName, numberObjectName, numberObjectType))
				return true;
		}
		return false;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#get_agent_number_object_type(java.lang.String)
	 */
	public String get_agent_number_object_type(String numberObjectName) {
		for ( Layer layer:this) {
			String type = layer.get_agent_number_object_type( numberObjectName);
			if ( null != type)
				return type;
		}
		return null;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#get_agent_number_object_type(java.lang.String, java.lang.String)
	 */
	public String get_agent_number_object_type(String fullName, String numberObjectName) {
		for ( Layer layer:this) {
			String type = layer.get_agent_number_object_type( fullName, numberObjectName);
			if ( null != type)
				return type;
		}
		return null;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#get_spot_number_object_type(java.lang.String)
	 */
	public String get_spot_number_object_type(String numberObjectName) {
		for ( Layer layer:this) {
			String type = layer.get_spot_number_object_type( numberObjectName);
			if ( null != type)
				return type;
		}
		return null;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#get_spot_number_object_type(java.lang.String, java.lang.String)
	 */
	public String get_spot_number_object_type(String fullName, String numberObjectName) {
		for ( Layer layer:this) {
			String type = layer.get_spot_number_object_type( fullName, numberObjectName);
			if ( null != type)
				return type;
		}
		return null;
	}

	/**
	 * Returns the array for the initial values of the objects which the agent or spot objects have.
	 * @param kind the kind of the object
	 * @return the array for the initial values of the objects which the agent or spot objects have
	 */
	public String[] get_object_initial_values(String kind) {
		Vector<String> initialValues = new Vector<String>();
		for ( Layer layer:this)
			layer.get_object_initial_values( kind, initialValues);

		return Tool.quick_sort_string( initialValues, true, false);
	}

	/**
	 * Returns the array for the initial values of the objects which the agent objects have.
	 * @param kind the kind of the object
	 * @return the array for the initial values of the objects which the agent objects have
	 */
	public String[] get_agent_object_initial_values(String kind) {
		Vector<String> initialValues = new Vector<String>();
		for ( Layer layer:this)
			layer.get_agent_object_initial_values( kind, initialValues);

		return Tool.quick_sort_string( initialValues, true, false);
	}

	/**
	 * Returns the array for the initial values of the objects which the spot objects have.
	 * @param kind the kind of the object
	 * @return the array for the initial values of the objects which the spot objects have
	 */
	public String[] get_spot_object_initial_values(String kind) {
		Vector<String> initialValues = new Vector<String>();
		for ( Layer layer:this)
			layer.get_spot_object_initial_values( kind, initialValues);

		return Tool.quick_sort_string( initialValues, true, false);
	}

	/**
	 * Returns the array for the initial values of the objects which the role objects have.
	 * @return the array for the initial values of the objects which the role objects have
	 */
	public String[] get_role_initial_values() {
		Vector<String> roleInitialValues = new Vector<String>();
		for ( Layer layer:this)
			layer.get_role_initial_values( roleInitialValues, ExperimentManager._suffixes);

		return Tool.quick_sort_string( roleInitialValues, true, false);
	}

	/**
	 * Returns the array for the names of the unremovable objects.
	 * @param kind the specified kind
	 * @param names the array of the specified names
	 * @return the array for the names of the unremovable objects
	 */
	public Vector<String> get_unremovable_object_names(String kind, Vector<String> names) {
		Vector<String> newNames = new Vector<String>();
		for ( String name:names) {
			if ( !other_agents_have_same_object_name( kind, name))
				newNames.add( name);
		}
		return newNames;
	}

	/**
	 * @param kind
	 * @param name
	 * @return
	 */
	public boolean other_agents_have_same_object_name(String kind, String name) {
		for ( Layer layer:this) {
			if ( layer.other_agents_have_same_object_name( kind, name))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if other agent objects have the object which has the specified data except the spceified one.
	 * @param kind the specified kind
	 * @param name the specified name
	 * @param agentObject the spceified agent object
	 * @return true if other agent objects have the object which has the specified data except the spceified one
	 */
	public boolean other_agents_have_same_object_name(String kind, String name, AgentObject agentObject) {
		for ( Layer layer:this) {
			if ( layer.other_agents_have_same_object_name( kind, name, agentObject))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if other spot objects have the object which has the specified data.
	 * @param kind the specified kind
	 * @param name the specified name
	 * @return true if other spot objects have the object which has the specified data
	 */
	public boolean other_spots_have_same_object_name(String kind, String name) {
		for ( Layer layer:this) {
			if ( layer.other_spots_have_same_object_name( kind, name))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if other spot objects have the object which has the specified data except the spceified one.
	 * @param kind the specified kind
	 * @param name the specified name
	 * @param spotObject the spceified spot object
	 * @return true if other spot objects have the object which has the specified data except the spceified one
	 */
	public boolean other_spots_have_same_object_name(String kind, String name, SpotObject spotObject) {
		for ( Layer layer:this) {
			if ( layer.other_spots_have_same_object_name( kind, name, spotObject))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if other spot objects have the object which has the specified data except the spceified one.
	 * @param kind the specified kind
	 * @param name the specified name
	 * @param gis the specified GIS's ID
	 * @param spotObject the spceified spot object
	 * @return true if other spot objects have the object which has the specified data except the spceified one
	 */
	public boolean other_spots_have_same_object_name(String kind, String name, String gis, SpotObject spotObject) {
		for ( Layer layer:this) {
			if ( layer.other_spots_have_same_object_name( kind, name, gis, spotObject))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if the specified class variable name is in use for a different class on other objects except the specified one.
	 * @param classVariable the specified class variable
	 * @param jarFilename the jar file name
	 * @param classname the class name
	 * @param entityBase the specified object
	 * @return true if the specified class variable name is in use for a different class on other objects except the specified one
	 */
	public boolean other_uses_this_class_variable_as_different_class(String classVariable, String jarFilename, String classname, EntityBase entityBase) {
		for ( Layer layer:this) {
			if ( layer.other_uses_this_class_variable_as_different_class( classVariable, jarFilename, classname, entityBase))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if the specified class variable name is in use for a different class on other objects.
	 * @param classVariable the specified class variable
	 * @param jarFilename the jar file name
	 * @param classname the class name
	 * @param type the object type
	 * @return true if the specified class variable name is in use for a different class on other objects
	 */
	public boolean other_uses_this_class_variable_as_different_class(String classVariable, String jarFilename, String classname, String type) {
		for ( Layer layer:this) {
			if ( layer.other_uses_this_class_variable_as_different_class( classVariable, jarFilename, classname, type))
				return true;
		}
		return false;
	}

	/**
	 * Returns true the specified class is in use.
	 * @param jarFilename the specified jar file name
	 * @param classname the specified class name
	 * @return true the specified class is in use
	 */
	public boolean uses_this_class(String jarFilename, String classname) {
		for ( Layer layer:this) {
			if ( layer.uses_this_class( jarFilename, classname))
				return true;
		}
		return false;
	}

//	/**
//	 * @param file
//	 * @param kind
//	 * @return
//	 */
//	public boolean uses_this_file(File file, String kind) {
//		for ( Layer layer:this) {
//			if ( layer.uses_this_file( file, kind))
//				return true;
//		}
//		return false;
//	}

	/**
	 * @param srcPath
	 * @param destPath
	 * @return
	 */
	public boolean move_file(File srcPath, File destPath) {
		boolean result = false;
		for ( Layer layer:this) {
			if ( layer.move_file( srcPath, destPath))
				result = true;
		}
		return result;
	}

	/**
	 * Returns true if the all objects on the current layer can be removed.
	 * @return true if the all objects on the current layer can be removed
	 */
	public boolean can_remove_current_layer() {
		return get( _currentLayer).can_remove();
	}

	/**
	 * Removes the current layer.
	 * @param component the base class for all Swing components
	 */
	public void remove(JComponent component) {
		if ( _currentLayer == size() - 1) {
			change( size() - 2, component);
			Layer layer = get( size() - 1);
			layer.cleanup();
			removeElementAt( size() - 1);
			_currentLayer = size() - 1;
		} else {
			change( _currentLayer + 1, component);
			Layer layer = get( _currentLayer - 1);
			layer.cleanup();
			removeElementAt( _currentLayer - 1);
			--_currentLayer;
		}

		StateManager.get_instance().refresh();

		Observer.get_instance().on_update_agent_object( "probability");
		Observer.get_instance().on_update_agent_object( "collection");
		Observer.get_instance().on_update_agent_object( "list");
		Observer.get_instance().on_update_agent_object( "map");
		Observer.get_instance().on_update_agent_object( "keyword");
		Observer.get_instance().on_update_agent_object( "number object");
		Observer.get_instance().on_update_agent_object( "role variable");
		Observer.get_instance().on_update_agent_object( "time variable");
		Observer.get_instance().on_update_agent_object( "spot variable");
		Observer.get_instance().on_update_agent_object( "class variable");
		Observer.get_instance().on_update_agent_object( "file");
		Observer.get_instance().on_update_agent_object( "exchange algebra");
		Observer.get_instance().on_update_agent_object( "extransfer");

		Observer.get_instance().on_update_spot_object( "probability");
		Observer.get_instance().on_update_spot_object( "collection");
		Observer.get_instance().on_update_spot_object( "list");
		Observer.get_instance().on_update_spot_object( "map");
		Observer.get_instance().on_update_spot_object( "keyword");
		Observer.get_instance().on_update_spot_object( "number object");
		Observer.get_instance().on_update_spot_object( "role variable");
		Observer.get_instance().on_update_spot_object( "time variable");
		Observer.get_instance().on_update_spot_object( "spot variable");
		Observer.get_instance().on_update_spot_object( "class variable");
		Observer.get_instance().on_update_spot_object( "file");
		Observer.get_instance().on_update_spot_object( "exchange algebra");
		Observer.get_instance().on_update_spot_object( "extransfer");

		Observer.get_instance().on_update_object( "probability");
		Observer.get_instance().on_update_object( "collection");
		Observer.get_instance().on_update_object( "list");
		Observer.get_instance().on_update_object( "map");
		Observer.get_instance().on_update_object( "keyword");
		Observer.get_instance().on_update_object( "number object");
		Observer.get_instance().on_update_object( "role variable");
		Observer.get_instance().on_update_object( "time variable");
		Observer.get_instance().on_update_object( "spot variable");
		Observer.get_instance().on_update_object( "class variable");
		Observer.get_instance().on_update_object( "file");
		Observer.get_instance().on_update_object( "exchange algebra");
		Observer.get_instance().on_update_object( "extransfer");

		Observer.get_instance().on_update_entityBase( true);

		Observer.get_instance().on_update_role( true);

		Observer.get_instance().on_update_chartObject();

		Observer.get_instance().modified();
	}

	/**
	 * Returns true if the selected objects can be removed.
	 * @return true if the selected objects can be removed
	 */
	public boolean can_remove_selected() {
		return get( _currentLayer).can_remove_selected();
	}

	/**
	 * Removes the selected objects.
	 */
	public void remove_selected() {
		get( _currentLayer).remove_selected();

		StateManager.get_instance().refresh();

		Observer.get_instance().on_update_agent_object( "probability");
		Observer.get_instance().on_update_agent_object( "collection");
		Observer.get_instance().on_update_agent_object( "list");
		Observer.get_instance().on_update_agent_object( "map");
		Observer.get_instance().on_update_agent_object( "keyword");
		Observer.get_instance().on_update_agent_object( "number object");
		Observer.get_instance().on_update_agent_object( "role variable");
		Observer.get_instance().on_update_agent_object( "time variable");
		Observer.get_instance().on_update_agent_object( "spot variable");
		Observer.get_instance().on_update_agent_object( "class variable");
		Observer.get_instance().on_update_agent_object( "file");
		Observer.get_instance().on_update_agent_object( "exchange algebra");
		Observer.get_instance().on_update_agent_object( "extransfer");

		Observer.get_instance().on_update_spot_object( "probability");
		Observer.get_instance().on_update_spot_object( "collection");
		Observer.get_instance().on_update_spot_object( "list");
		Observer.get_instance().on_update_spot_object( "map");
		Observer.get_instance().on_update_spot_object( "keyword");
		Observer.get_instance().on_update_spot_object( "number object");
		Observer.get_instance().on_update_spot_object( "role variable");
		Observer.get_instance().on_update_spot_object( "time variable");
		Observer.get_instance().on_update_spot_object( "spot variable");
		Observer.get_instance().on_update_spot_object( "class variable");
		Observer.get_instance().on_update_spot_object( "file");
		Observer.get_instance().on_update_spot_object( "exchange algebra");
		Observer.get_instance().on_update_spot_object( "extransfer");

		Observer.get_instance().on_update_object( "probability");
		Observer.get_instance().on_update_object( "collection");
		Observer.get_instance().on_update_object( "list");
		Observer.get_instance().on_update_object( "map");
		Observer.get_instance().on_update_object( "keyword");
		Observer.get_instance().on_update_object( "number object");
		Observer.get_instance().on_update_object( "role variable");
		Observer.get_instance().on_update_object( "time variable");
		Observer.get_instance().on_update_object( "spot variable");
		Observer.get_instance().on_update_object( "class variable");
		Observer.get_instance().on_update_object( "file");
		Observer.get_instance().on_update_object( "exchange algebra");
		Observer.get_instance().on_update_object( "extransfer");

		Observer.get_instance().on_update_entityBase( true);

		Observer.get_instance().on_update_role( true);

		Observer.get_instance().on_update_chartObject();

		Observer.get_instance().modified();
	}

	/**
	 * Updates the sizes of the object names.
	 * @param graphics2D the graphics object of JAVA
	 */
	public void update_name_dimension(Graphics2D graphics2D) {
		for ( Layer layer:this)
			layer.update_name_dimension( graphics2D);
	}

	/**
	 * Returns true if the agent name can be adjusted.
	 * @param headName the prefix of the agent name
	 * @param ranges the ranges for the agent number
	 * @param onRemove true for removing the object
	 * @return true if the agent name can be adjusted
	 */
	public boolean can_adjust_agent_name(String headName, Vector<String[]> ranges, boolean onRemove) {
		boolean result = true;
		for ( Layer layer:this) {
			if ( !layer.can_adjust_agent_name( headName, ranges, onRemove))
				result = false;
		}
		return result;
	}

	/**
	 * Returns true if it is possible to update the specified agent name with the new one.
	 * @param headName the prefix of the specified agent name
	 * @param ranges the ranges for the specified agent number
	 * @param newHeadName the prefix of the new agent name
	 * @param newRanges the ranges for the new agent number
	 * @return true if it is possible to update the specified agent name with the new one
	 */
	public boolean can_adjust_agent_name(String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = true;
		for ( Layer layer:this) {
			if ( !layer.can_adjust_agent_name( headName, ranges, newHeadName, newRanges))
				result = false;
		}
		return result;
	}

	/**
	 * Returns true if the spot name can be adjusted.
	 * @param headName the prefix of the spot name
	 * @param ranges the ranges for the spot number
	 * @param onRemove true for removing the object
	 * @return true if the spot name can be adjusted
	 */
	public boolean can_adjust_spot_name(String headName, Vector<String[]> ranges, boolean onRemove) {
		boolean result = true;
		for ( Layer layer:this) {
			if ( !layer.can_adjust_spot_name( headName, ranges, onRemove))
				result = false;
		}
		return result;
	}

	/**
	 * Returns true if it is possible to update the specified spot name with the new one successfully.
	 * @param headName the prefix of the specified spot name
	 * @param ranges the ranges for the specified spot number
	 * @param newHeadName the prefix of the new spot name
	 * @param newRanges the ranges for the new spot number
	 * @return true if it is possible to update the specified spot name with the new one successfully
	 */
	public boolean can_adjust_spot_name(String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = true;
		for ( Layer layer:this) {
			if ( !layer.can_adjust_spot_name( headName, ranges, newHeadName, newRanges))
				result = false;
		}
		return result;
	}

	/**
	 * Returns true if the specified object can be removed.
	 * @param kind the specified kind of the object
	 * @param objectName the specified name of the object
	 * @param otherSpotsHaveThisObjectName true if other spot objects have the object which has the specified data
	 * @param headName the prefix of the agent or spot name
	 * @param ranges the ranges for the agent or spot number
	 * @param onRemove true for removing the object
	 * @return true if the specified object can be removed
	 */
	public boolean can_remove(String kind, String objectName, boolean otherSpotsHaveThisObjectName, String headName, Vector<String[]> ranges, boolean onRemove) {
		// TODO 従来のもの
		boolean result = true;
		for ( Layer layer:this) {
			if ( !layer.can_remove( kind, objectName, otherSpotsHaveThisObjectName, headName, ranges, onRemove))
				result = false;
		}
		return result;
	}

	/**
	 * @param entity
	 * @param kind
	 * @param objectName
	 * @param otherEntitiesHaveThisObjectName
	 * @param headName
	 * @param ranges
	 * @param onRemove
	 * @return
	 */
	public boolean can_remove(String entity, String kind, String objectName, boolean otherEntitiesHaveThisObjectName, String headName, Vector<String[]> ranges, boolean onRemove) {
		// TODO これからはこちらに移行してゆく
		boolean result = true;
		for ( Layer layer:this) {
			if ( !layer.can_remove( entity, kind, objectName, otherEntitiesHaveThisObjectName, headName, ranges, onRemove))
				result = false;
		}
		return result;
	}

	/**
	 * Returns true if the specified object can be removed.
	 * @param kind the specified kind of the object
	 * @param type the type of the number variable
	 * @param name the specified name of the object
	 * @param headName the prefix of the agent or spot name
	 * @param ranges the ranges for the agent or spot number
	 * @param onRemove true for removing the object
	 * @return true if the specified object can be removed
	 */
	public boolean can_remove(String kind, String type, String name, String headName, Vector<String[]> ranges, boolean onRemove) {
		boolean result = true;
		for ( Layer layer:this) {
			if ( !layer.can_remove( kind, type, name, headName, ranges, onRemove))
				result = false;
		}
		return result;
	}

	/**
	 * Returns true if the specified object can be removed, except the specified agent or spot object.
	 * @param kind the specified kind of the object
	 * @param name the specified name of the object
	 * @param otherSpotsHaveThisObjectName true if other spot objects have the object which has the specified data
	 * @param headName the prefix of the agent or spot name
	 * @param ranges the ranges for the agent or spot number
	 * @param entityBase the specified agent or spot object
	 * @param onRemove true for removing the object
	 * @return true if the specified object can be removed, except the specified agent or spot object
	 */
	public boolean can_remove(String kind, String name, boolean otherSpotsHaveThisObjectName, String headName, Vector<String[]> ranges, EntityBase entityBase, boolean onRemove) {
		// TODO 従来のもの
		boolean result = true;
		for ( Layer layer:this) {
			if ( !layer.can_remove( kind, name, otherSpotsHaveThisObjectName, headName, ranges, entityBase, onRemove))
				result = false;
		}
		return result;
	}

	/**
	 * @param entity
	 * @param kind
	 * @param objectName
	 * @param otherEntitiesHaveThisObjectName
	 * @param headName
	 * @param ranges
	 * @param entityBase
	 * @param onRemove
	 * @return
	 */
	public boolean can_remove(String entity, String kind, String objectName, boolean otherEntitiesHaveThisObjectName, String headName, Vector<String[]> ranges, EntityBase entityBase, boolean onRemove) {
		// TODO これからはこちらに移行してゆく
		boolean result = true;
		for ( Layer layer:this) {
			if ( !layer.can_remove( entity, kind, objectName, otherEntitiesHaveThisObjectName, headName, ranges, entityBase, onRemove))
				result = false;
		}
		return result;
	}

	/**
	 * Returns true for updating the specified objects name with the new one successfully, except the specified agent or spot object.
	 * @param kind the specified kind of the object
	 * @param name the specified name of the object
	 * @param newName the new name of the object
	 * @param entityBase the specified agent or spot object
	 * @return true for updating the specified object name with the new one successfully, except the specified agent or spot object
	 */
	public boolean update_object_name(String kind, String name, String newName, EntityBase entityBase) {
		boolean result = false;
		for ( Layer layer:this) {
			if ( layer.update_object_name( kind, name, newName, entityBase))
				result = true;
		}
		return result;
	}

	/**
	 * Returns true for updating the specified number variables name with the new one successfully, except the specified agent or spot object.
	 * @param entity "agent" or "spot"
	 * @param name the specified name of the number variable
	 * @param newName the new name of the number variable
	 * @param entityBase the specified agent or spot object
	 * @return true for updating the specified number variable name with the new one successfully, except the specified agent or spot object
	 */
	public boolean update_number_object_name(String entity, String name, String newName, EntityBase entityBase) {
		boolean result = false;
		for ( Layer layer:this) {
			if ( layer.update_number_object_name( entity, name, newName, entityBase))
				result = true;
		}
		return result;
	}

	/**
	 * Returns true if the specified role can be removed.
	 * @param name the specified role name
	 * @param onRemove true for removing the object
	 * @param role the specified role
	 * @return true if the specified role can be removed
	 */
	public boolean can_remove_role(String name, boolean onRemove, Role role) {
		boolean result = true;
		for ( Layer layer:this) {
			if ( !layer.can_remove_role( name, onRemove, role))
				result = false;
		}
		return result;
	}

	/**
	 * Returns true if the specified expression can be removed.
	 * @param expression the specified expression
	 * @return true if the specified expression can be removed
	 */
	public boolean can_remove_expression(Expression expression) {
		boolean result = true;
		for ( Layer layer:this) {
			if ( !layer.can_remove_expression( expression))
				result = false;
		}
		return result;
	}

	/**
	 * Returns true for updating the specified stage name with the new one.
	 * @param newName the new stage name
	 * @param originalName the specified stage name
	 * @return true for updating the specified stage name with the new one
	 */
	public boolean update_stage_name(String newName, String originalName) {
		boolean result = false;
		for ( Layer layer:this) {
			if ( layer.update_stage_name( newName, originalName))
				result = true;
		}
		return result;
	}

	/**
	 * Returns true if the specified stage can be removed.
	 * @param stageName the specified stage name
	 * @return true if the specified stage can be removed
	 */
	public boolean can_remove_stage_name(String stageName) {
		boolean result = true;
		for ( Layer layer:this) {
			if ( !layer.can_remove_stage_name( stageName))
				result = false;
		}
		return result;
	}

	/**
	 * Returns true if the specified stage names can be adjusted.
	 * @param stageNames the array of the specified stage names
	 * @return true if the stage names can be adjusted
	 */
	public boolean can_adjust_stage_name(Vector<String> stageNames) {
		boolean result = true;
		for ( Layer layer:this) {
			if ( !layer.can_adjust_stage_name( stageNames))
				result = false;
		}
		return result;
	}

	/**
	 * Invoked when the agent object has been changed.
	 * @param newName the new agent name
	 * @param originalName the original agent name
	 * @param headName the prefix of the agent name
	 * @param ranges the ranges for the agent number
	 * @param newHeadName the new prefix of the agent name
	 * @param newRanges the new ranges for the agent number
	 * @return
	 */
	public boolean update_agent_name_and_number( String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = false;
		for ( Layer layer:this) {
			if ( layer.update_agent_name_and_number( newName, originalName, headName, ranges, newHeadName, newRanges))
				result = true;
		}
		return result;
	}

	/**
	 * Invoked when the spot object has been changed.
	 * @param newName the new spot name
	 * @param originalName the original spot name
	 * @param headName the prefix of the spot name
	 * @param ranges the ranges for the spot number
	 * @param newHeadName the new prefix of the spot name
	 * @param newRanges the new ranges for the spot number
	 * @return
	 */
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = false;
		for ( Layer layer:this) {
			if ( layer.update_spot_name_and_number( newName, originalName, headName, ranges, newHeadName, newRanges))
				result = true;
		}
		return result;
	}

	/**
	 * Invoked when the specified role object has been changed.
	 * @param originalName the role spot name
	 * @param role the specified role object
	 * @return
	 */
	public boolean update_role_name(String originalName, Role role) {
		boolean result = false;
		for ( Layer layer:this) {
			if ( layer.update_role_name( originalName, role))
				result = true;
		}
		return result;
	}

	/**
	 * Invoked when the expressions have been changed.
	 * @param visualShellExpressionManager the expressions manager for Visual Shell
	 * @return
	 */
	public boolean update_expression(VisualShellExpressionManager visualShellExpressionManager) {
		boolean result = false;
		for ( Layer layer:this) {
			if ( layer.update_expression( visualShellExpressionManager))
				result = true;
		}
		return result;
	}

	/**
	 * Invoked when the expression have been changed.
	 * @param newExpression
	 * @param newVariableCount
	 * @param originalExpression
	 * @return
	 */
	public boolean update_expression(Expression newExpression, int newVariableCount, Expression originalExpression) {
		boolean result = false;
		for ( Layer layer:this) {
			if ( layer.update_expression( newExpression, newVariableCount, originalExpression))
				result = true;
		}
		return result;
	}

//	/**
//	 * 
//	 */
//	public void on_remove_object_name_and_number() {
//	}
//
//	/**
//	 * @param drawObjects
//	 * @param spot_names
//	 */
//	private void on_remove_object_name_and_number(Vector<DrawObject> drawObjects, Vector<String> spotNames) {
//	}
//
//	/**
//	 * 
//	 */
//	public void on_remove_role_name() {
//	}
//
//	/**
//	 * @param drawObjects
//	 * @param role_names
//	 */
//	private void on_remove_role_name(Vector<DrawObject> drawObjects, Vector<String> roleNames) {
//	}

	/**
	 * Invoked when the specified stage names have been removed.
	 */
	public void on_remove_stage_name() {
		Vector<String> stageNames = new Vector<String>( Arrays.asList( StageManager.get_instance().get_names( false)));
		for ( Layer layer:this)
			layer.on_remove_stage_name( stageNames);
	}

	/**
	 * Returns true if the specified image file is in use.
	 * @param filename the name of the specified image file
	 * @return true if the specified image file is in use
	 */
	public boolean uses_this_image(String filename) {
		for ( Layer layer:this) {
			if ( layer.uses_this_image( filename))
				return true;
		}
		return false;
	}

	/**
	 * Sets the specified new image if the object uses the specified image.
	 */
	public void update_image() {
		for ( Layer layer:this)
			layer.update_image();
	}

	/**
	 * Sets the specified new image if the object uses the specified image.
	 * @param originalFilename the specified image file name
	 * @param newFilename the specified new image file name
	 */
	public void update_image(String originalFilename, String newFilename) {
		for ( Layer layer:this)
			layer.update_image( originalFilename, newFilename);
	}

	/**
	 * Edits the selected object.
	 * @param mousePosition the mouse position which the selected object contains.
	 * @param component the base class for all Swing components
	 * @param frame the Frame from which the dialog is displayed
	 */
	public void edit_object(Point mousePosition, JComponent component, Frame frame) {
		DrawObject drawObject = get( mousePosition);
		if ( null == drawObject)
			return;

		edit_object( drawObject, component, frame);
	}

	/**
	 * Edits the specified object.
	 * @param drawObject the specified object
	 * @param component the base class for all Swing components
	 * @param frame the Frame from which the dialog is displayed
	 */
	public void edit_object(DrawObject drawObject, JComponent component, Frame frame) {
		drawObject.select( true);
		component.repaint();
		drawObject.edit( component, frame);
		drawObject.select( false);
		component.repaint();
	}

	/**
	 * Returns true if the loading the specified file is completed successfully.
	 * @param file the specified XML file of Visual Shell data
	 * @param graphics2D the graphics object of JAVA
	 * @param component the base class for all Swing components
	 * @return true if the loading the specified file is completed successfully
	 */
	public boolean load(File file, Graphics2D graphics2D, JComponent component) {
		byte[] data = ZipUtility.get_binary( file, Constant._soarsRootDirectoryName + "/" + Constant._visualShellZipFileName);
		if ( null == data)
			return false;

		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return false;

		if ( !ZipUtility.decompress( data, parentDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		cleanup();

		File rootDirectory = new File( parentDirectory, Constant._visualShellRootDirectoryName);
		if ( !rootDirectory.exists() || !rootDirectory.isDirectory()) {
			rootDirectory = new File( parentDirectory, Constant._visualShellRootDirectoryName);
			if ( !rootDirectory.mkdirs()) {
				FileUtility.delete( parentDirectory, true);
				return false;
			}

			_parentDirectory = parentDirectory;
			_rootDirectory = rootDirectory;

			if ( !SaxLoader.execute( file, graphics2D, component)) {
				cleanup();
				return false;
			}
		} else {
			_parentDirectory = parentDirectory;
			_rootDirectory = rootDirectory;

			if ( !SaxLoader.execute( new File( _rootDirectory, Constant._visualShellDataFilename), graphics2D, component)) {
				cleanup();
				return false;
			}

			_currentFile = file;
		}

		if ( !transform_map_objects()) {
			cleanup();
			return false;
		}

		if ( !transform_time_conditions_and_commands()) {
			cleanup();
			return false;
		}

		if ( !transform_keyword_conditions_and_commands()) {
			cleanup();
			return false;
		}

		if ( !transform_numeric_conditions_and_commands()) {
			cleanup();
			return false;
		}

		resize( component);
		update_preferred_size( component);

		// デバッグ用
		//JarFileProperties.get_instance().serialize();
		//JavaClasses.get_instance().serialize();

		return true;
	}

	/**
	 * @return
	 */
	private boolean transform_map_objects() {
		// TODO 要動作確認！
		for ( Layer layer:this) {
			if ( !layer.transform_map_objects())
				return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	private boolean transform_time_conditions_and_commands() {
		for ( Layer layer:this) {
			if ( !layer.transform_time_conditions_and_commands())
				return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	private boolean transform_keyword_conditions_and_commands() {
		for ( Layer layer:this) {
			if ( !layer.transform_keyword_conditions_and_commands())
				return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	private boolean transform_numeric_conditions_and_commands() {
		for ( Layer layer:this) {
			if ( !layer.transform_numeric_conditions_and_commands())
				return false;
		}
		return true;
	}

	/**
	 * Returns true for saving the data to the file successfully.
	 * @return true for saving the data to the file successfully
	 */
	public boolean save() {
		if ( null == _currentFile)
			return false;

		return save_as( _currentFile);
	}

	/**
	 * Returns true for saving the data to the specified file successfully.
	 * @param file the specified file
	 * @return true for saving the data to the specified file successfully
	 */
	public boolean save_as(File file) {
		if ( file.exists() && ( !file.isFile() || !file.canRead() || !file.canWrite()))
			return false;

		if ( !setup_work_directory())
			return false;

		if ( !SaxWriter.execute( new File( _rootDirectory, Constant._visualShellDataFilename)))
			return false;

		if ( !DocumentWriter.execute( _rootDirectory, new File( _rootDirectory, Constant._visualShellDataFilename)))
			return false;

		if ( !file.exists() || !ZipUtility.find( file, Constant._soarsRootDirectoryName + "/")) {
			// ファイルが存在しないかまたは存在していてもSOARSデータファイルでない場合
			if ( null == _currentFile) {
				// 新たに作成する
				File parentDirectory = SoarsCommonTool.make_parent_directory();
				if ( null == parentDirectory)
					return false;

				File rootDirectory = new File( parentDirectory, Constant._soarsRootDirectoryName);
				if ( !rootDirectory.mkdirs()) {
					FileUtility.delete( parentDirectory, true);
					return false;
				}

				if ( !ZipUtility.compress( new File( rootDirectory, Constant._visualShellZipFileName), _rootDirectory, _parentDirectory)) {
					FileUtility.delete( parentDirectory, true);
					return false;
				}

				if ( !ZipUtility.compress( file, rootDirectory, parentDirectory)) {
					FileUtility.delete( parentDirectory, true);
					return false;
				}

				FileUtility.delete( parentDirectory, true);
			} else {
				// もし_currentFileが存在していたら_currentFileからコピーして更新する
				FileUtility.copy( _currentFile, file);
				if ( null == update( file))
					return false;
			}
		} else {
			// ファイルが存在していれば更新する
			if ( null == update( file))
				return false;
		}

		_currentFile = new File( file.getAbsolutePath());
		_modified = false;

		return true;
	}

	/**
	 * @param file
	 * @return
	 */
	private File update(File file) {
		// TODO Auto-generated method stub
		long now = System.currentTimeMillis();
		String filename = String.valueOf( now);

		File tempFile;
		try {
			tempFile = File.createTempFile( filename, null);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		tempFile.deleteOnExit();

		if ( !ZipUtility.compress( tempFile, _rootDirectory, _parentDirectory)) {
			tempFile.delete();
			return null;
		}

		if ( !ZipUtility.find( file, Constant._soarsRootDirectoryName + "/" + Constant._visualShellZipFileName)) {
			// soars/visualshell.zipが含まれていなければ追加する
			Map<String, List<Entry>> entryMap = new HashMap<String, List<Entry>>();

			List<Entry> entryList = new ArrayList<Entry>();

			// soars/visualshell.zipを追加する
			entryList.add( new Entry( Constant._soarsRootDirectoryName + "/" + Constant._visualShellZipFileName, tempFile));

			entryMap.put( Constant._soarsRootDirectoryName + "/", entryList);

			file = ZipUtility.append( file, entryMap);
			if ( null == file) {
				tempFile.delete();
				return null;
			}
		} else {
			// soars/visualshell.zipが含まれていれば置き換える
			Map<String, File> fileMap = new HashMap<String, File>();
			fileMap.put( Constant._soarsRootDirectoryName + "/" + Constant._visualShellZipFileName, tempFile);
			file = ZipUtility.update( file, fileMap);
			if ( null == file) {
				tempFile.delete();
				return null;
			}
		}

		tempFile.delete();

		return file;
	}

	/**
	 * Returns true for importing the data from the specified file successfully.
	 * @param file the specified file
	 * @param graphics2D the graphics object of JAVA
	 * @param component the base class for all Swing components
	 * @return true for importing the data from the specified file successfully
	 */
	public boolean import_data(File file, Graphics2D graphics2D, JComponent component) {
		return true;
	}

	/**
	 * Returns 1 for importing the initial data from the specified file in CSV format successfully.
	 * @param file the specified file
	 * @param warning whether to display the warning messages
	 * @param all true if all data is imported
	 * @param component the base class for all Swing components
	 * @return 1 for importing the initial data from the specified file in CSV format successfully
	 */
	public int import_initial_data(File file, boolean warning, boolean all, JComponent component) {
		//cleanup();
		int result = InitialDataImporter.execute( file, warning, all, component);

		if ( 0 != result) {
			resize( component);
			update_preferred_size( component);
			LogManager.get_instance().update_all();
			ExperimentManager.get_instance().update_all();
			MainFrame.get_instance().update_menu();
		}

		return result;
	}

	/**
	 * Returns 1 for importing the initial data from the clipboard in CSV format successfully.
	 * @param warning whether to display the warning messages
	 * @param all true if all data is imported
	 * @param component the base class for all Swing components
	 * @return 1 for importing the initial data from the clipboard in CSV format successfully
	 */
	public int import_initial_data(boolean warning, boolean all, JComponent component) {
		//cleanup();
		int result = InitialDataImporter.execute( warning, all, component);

		if ( 0 != result) {
			resize( component);
			update_preferred_size( component);
			LogManager.get_instance().update_all();
			ExperimentManager.get_instance().update_all();
			MainFrame.get_instance().update_menu();
		}

		return result;
	}

	/**
	 * Returns true for exporting the initial data to the specified file in CSV format successfully.
	 * @param file the specified file
	 * @param all true if all data is exported
	 * @return true for exporting the initial data to the specified file in CSV format successfully
	 */
	public boolean export_data_initial_data(File file, boolean all) {
		return InitialDataExporter.execute( file, all);
	}

	/**
	 * @return
	 */
	public boolean is_initial_data_file_correct() {
		for ( Layer layer:this) {
			if ( !layer.is_initial_data_file_correct())
				return false;
		}
		return true;
	}

	/**
	 * Returns true for exporting the ModelBuilder script to the specified file in CSV format successfully.
	 * @param file the specified file
	 * @param toDisplay whether to display the logs
	 * @param toFile whether to make the log files
	 * @return true for exporting the ModelBuilder script to the specified file in CSV format successfully
	 */
	public boolean export_data(File file, boolean toDisplay, boolean toFile) {
		if ( !ExperimentManager.get_instance().isEmpty()) {
			if ( 0 == ExperimentManager.get_instance().get_initial_value_count())
				return false;
			else
				return ExperimentManager.get_instance().export( file, toDisplay, toFile);
		} else
			return Exporter.execute_on_model_builder( file, null, null, "", toDisplay, toFile, false);
	}

	/**
	 * Exports the ModelBuilder scripts to the specified files in CSV format, and returns them.
	 * @param toDisplay whether to display the logs
	 * @param toFile whether to make the log files
	 * @return the ModelBuilder scripts
	 */
	public ScriptFile[] export_data(/*boolean animator, */boolean toDisplay, boolean toFile) {
		ScriptFile[] scriptFiles = new ScriptFile[ 1];
//		if ( !animator) {
			String workDirectoryName = CommonTool.get_work_directory_name(
				( null == _currentFile)
					? ( System.getProperty( Constant._soarsHome) + "/../log")
					: _currentFile.getParent());
			if ( null == workDirectoryName)
				return null;

			try {
				scriptFiles[ 0] = new ScriptFile( File.createTempFile( "soars_", ".sor"), workDirectoryName);
			} catch (IOException e) {
				//e.printStackTrace();
				return null;
			}

			if ( !Exporter.execute_on_model_builder( scriptFiles[ 0]._path, null, workDirectoryName, "", toDisplay, toFile, false)) {
				scriptFiles[ 0]._path.delete();
				return null;
			}

			scriptFiles[ 0]._path.deleteOnExit();

			return scriptFiles;
//		} else {
//			scriptFiles[ 0] = new ScriptFile( CommonTool.make_work_directory());
//			File script_file = new File( scriptFiles[ 0]._path.getAbsolutePath() + "/" + Constant._soars_script_file_name);
//
//			if ( !Exporter.execute_on_animator( script_file, null, scriptFiles[ 0]._path, "")) {
//				FileUtility.delete( scriptFiles[ 0]._path, true);
//				return null;
//			}
//
//			return scriptFiles;
//		}
	}

	/**
	 * Exports the ModelBuilder scripts to the specified files in CSV format, and returns them.
	 * @param toDisplay whether to display the logs
	 * @param toFile whether to make the log files
	 * @param experimentManager the experiment support manager
	 * @return the ModelBuilder scripts 
	 */
	public ScriptFile[] export_data(/*boolean animator, */boolean toDisplay, boolean toFile, ExperimentManager experimentManager) {
		if ( 0 == experimentManager.get_initial_value_count())
			return null;
		else
			return experimentManager.export( /*animator, */toDisplay, toFile);
	}

	/**
	 * @param dockerFilesetProperty 
	 * @param file
	 * @return
	 */
	public boolean create_docker_fileset(DockerFilesetProperty dockerFilesetProperty, File file) {
		// TODO Auto-generated method stub
		DockerFilesetCreator dockerFilesetCreator = new DockerFilesetCreator( dockerFilesetProperty);
		if ( !dockerFilesetCreator.setup())
			return false;

		return dockerFilesetCreator.execute( file);
	}

	/**
	 * @param dockerFilesetProperty
	 * @param file
	 * @param experimentManager
	 * @return
	 */
	public boolean create_docker_fileset(DockerFilesetProperty dockerFilesetProperty, File file, ExperimentManager experimentManager) {
		// TODO Auto-generated method stub
		DockerFilesetCreator dockerFilesetCreator = new DockerFilesetCreator( dockerFilesetProperty);
		if ( !dockerFilesetCreator.setup( experimentManager))
			return false;

		return dockerFilesetCreator.execute( file);
	}

	/**
	 * Exports the ModelBuilder script to the clipboard in CSV format.
	 * @param toDisplay whether to display the logs
	 * @param toFile whether to make the log files
	 */
	public void export_data_to_clipboard(boolean toDisplay, boolean toFile) {
		if ( !ExperimentManager.get_instance().isEmpty()
			&& 0 < ExperimentManager.get_instance().get_initial_value_count())
			ExperimentManager.get_instance().export_to_clipboard( toDisplay, toFile);
		else
			Exporter.execute( null, "", toDisplay, toFile);
	}

	/**
	 * Returns true for exporting the ModelBuilder script to the specified file in CSV format for Grid successfully.
	 * @param experimentManager the experiment support manager
	 * @param scriptDirectoryName the name of directory for the script file on Grid
	 * @param logDirectoryName the name of directory for the log files on Grid
	 * @param programDirectory the name of directory for the program file on Grid
	 * @param numberOfTimes the number of the experiment times
	 * @param host the Grid host
	 * @param username the Grid user name
	 * @param password the Grid password
	 * @param intBuffer the couter for progress
	 * @param textField the text field to display progress
	 * @return true for exporting the ModelBuilder script to the specified file in CSV format for Grid successfully
	 */
	public boolean export_data_for_grid(ExperimentManager experimentManager, String scriptDirectoryName, String logDirectoryName, String programDirectory, int numberOfTimes, String host, String username, String password, IntBuffer intBuffer, JTextField textField) {
		// TODO Auto-generated method stub
		if ( !is_initial_data_file_correct())
			return false;

		SshClient sshClient = SshTool.getSshClient( host, username, password);
		if ( null == sshClient)
			return false;

		SftpClient sftpClient = SshTool.getSftpClient( sshClient);
		if ( null == sftpClient) {
			sshClient.disconnect();
			return false;
		}

		if ( exist_user_data_directory() && !export_user_data_zip_file( scriptDirectoryName, sftpClient)) {
			SshTool.close( sftpClient);
			sshClient.disconnect();
			return false;
		}

		if ( !export_functional_object_jar_files( scriptDirectoryName, sftpClient)) {
			SshTool.close( sftpClient);
			sshClient.disconnect();
			return false;
		}

		if ( !export_user_rule_jar_files( scriptDirectoryName, sftpClient)) {
			SshTool.close( sftpClient);
			sshClient.disconnect();
			return false;
		}

		if ( !export_other_jar_files( scriptDirectoryName, sftpClient)) {
			SshTool.close( sftpClient);
			sshClient.disconnect();
			return false;
		}

		if ( null != experimentManager) {
			if ( !experimentManager.isEmpty() && 0 < experimentManager.get_initial_value_count())
				experimentManager.export( scriptDirectoryName, logDirectoryName, programDirectory, numberOfTimes, sftpClient, intBuffer, textField);
		} else {
			for ( int i = 1; i <= numberOfTimes; ++i) {
				if ( !SshTool.directory_exists( sftpClient, scriptDirectoryName + "/" + String.valueOf( i)))
					sftpClient.mkdirs( scriptDirectoryName + "/" + String.valueOf( i));

				if ( !SshTool.directory_exists( sftpClient, scriptDirectoryName + "/" + String.valueOf( i))) {
					SshTool.close( sftpClient);
					sshClient.disconnect();
					return false;
				}

				File scriptFile; 
				try {
					scriptFile = File.createTempFile( "soars_", ".sor");
				} catch (IOException e) {
					SshTool.close( sftpClient);
					sshClient.disconnect();
					return false;
				}

				Exporter.execute_on_grid( scriptFile, null, String.valueOf( i), scriptDirectoryName, logDirectoryName, programDirectory, "", true);

				try {
					sftpClient.put( scriptFile.getAbsolutePath(), scriptDirectoryName + "/" + String.valueOf( i) + "/" + Constant._soarsScriptFilename);
				} catch (IOException e) {
					SshTool.close( sftpClient);
					sshClient.disconnect();
					scriptFile.delete();
					return false;
//				} catch (Throwable ex) {
//					scriptFile.delete();
//					return;
				}

				scriptFile.delete();

				intBuffer.put( 0, intBuffer.get( 0) + 1);
				textField.setText( String.valueOf( intBuffer.get( 0)));
				textField.update( textField.getGraphics());
			}
		}

		SshTool.close( sftpClient);
		sshClient.disconnect();

		return true;
	}

	/**
	 * Returns true for exporting the ModelBuilder script to the specified file in CSV format for Grid successfully.
	 * @param experimentManager the experiment support manager
	 * @param scriptDirectoryName the name of directory for the script file on Grid
	 * @param logDirectoryName the name of directory for the log files on Grid
	 * @param programDirectory the name of directory for the program file on Grid
	 * @param numberOfTimes the number of the experiment times
	 * @param host the Grid host
	 * @param username the Grid user name
	 * @param keyFile the SSH private key file
	 * @param intBuffer the couter for progress
	 * @param textField the text field to display progress
	 * @return true for exporting the ModelBuilder script to the specified file in CSV format for Grid successfully
	 */
	public boolean export_data_for_grid(ExperimentManager experimentManager, String scriptDirectoryName, String logDirectoryName, String programDirectory, int numberOfTimes, String host, String username, File keyFile, IntBuffer intBuffer, JTextField textField) {
		// TODO Auto-generated method stub
		if ( !is_initial_data_file_correct())
			return false;

		SshClient sshClient = SshTool2.getSshClient( host, username, keyFile);
		if ( null == sshClient)
			return false;

		SftpClient sftpClient = SshTool2.getSftpClient( sshClient);
		if ( null == sftpClient) {
			sshClient.disconnect();
			return false;
		}

		if ( exist_user_data_directory() && !export_user_data_zip_file( scriptDirectoryName, sftpClient)) {
			SshTool2.close( sftpClient);
			sshClient.disconnect();
			return false;
		}

		if ( !export_functional_object_jar_files( scriptDirectoryName, sftpClient)) {
			SshTool2.close( sftpClient);
			sshClient.disconnect();
			return false;
		}

		if ( !export_user_rule_jar_files( scriptDirectoryName, sftpClient)) {
			SshTool2.close( sftpClient);
			sshClient.disconnect();
			return false;
		}

		if ( !export_other_jar_files( scriptDirectoryName, sftpClient)) {
			SshTool2.close( sftpClient);
			sshClient.disconnect();
			return false;
		}

		if ( null != experimentManager) {
			if ( !experimentManager.isEmpty() && 0 < experimentManager.get_initial_value_count())
				experimentManager.export( scriptDirectoryName, logDirectoryName, programDirectory, numberOfTimes, sftpClient, intBuffer, textField);
		} else {
			for ( int i = 1; i <= numberOfTimes; ++i) {
				if ( !SshTool2.directory_exists( sftpClient, scriptDirectoryName + "/" + String.valueOf( i)))
					sftpClient.mkdirs( scriptDirectoryName + "/" + String.valueOf( i));

				if ( !SshTool2.directory_exists( sftpClient, scriptDirectoryName + "/" + String.valueOf( i))) {
					SshTool2.close( sftpClient);
					sshClient.disconnect();
					return false;
				}

				File scriptFile; 
				try {
					scriptFile = File.createTempFile( "soars_", ".sor");
				} catch (IOException e) {
					SshTool2.close( sftpClient);
					sshClient.disconnect();
					return false;
				}

				Exporter.execute_on_grid( scriptFile, null, String.valueOf( i), scriptDirectoryName, logDirectoryName, programDirectory, "", true);

				try {
					sftpClient.put( scriptFile.getAbsolutePath(), scriptDirectoryName + "/" + String.valueOf( i) + "/" + Constant._soarsScriptFilename);
				} catch (IOException e) {
					SshTool2.close( sftpClient);
					sshClient.disconnect();
					scriptFile.delete();
					return false;
//				} catch (Throwable ex) {
//					scriptFile.delete();
//					return;
				}

				scriptFile.delete();

				intBuffer.put( 0, intBuffer.get( 0) + 1);
				textField.setText( String.valueOf( intBuffer.get( 0)));
				textField.update( textField.getGraphics());
			}
		}

		SshTool2.close( sftpClient);
		sshClient.disconnect();

		return true;
	}

	/**
	 * 
	 * @param scriptDirectoryName the name of directory for the script file on Grid
	 * @param host the Grid host
	 * @param username the Grid user name
	 * @param password the Grid password
	 * @return 
	 */
	public boolean setup_for_genetic_algorithm_on_grid(String scriptDirectoryName, String host, String username, String password) {
		// TODO Auto-generated method stub
		if ( !is_initial_data_file_correct())
			return false;

		SshClient sshClient = SshTool.getSshClient( host, username, password);
		if ( null == sshClient)
			return false;

		SftpClient sftpClient = SshTool.getSftpClient( sshClient);
		if ( null == sftpClient) {
			sshClient.disconnect();
			return false;
		}

		if ( exist_user_data_directory() && !export_user_data_zip_file( scriptDirectoryName, sftpClient)) {
			SshTool.close( sftpClient);
			sshClient.disconnect();
			return false;
		}

		if ( !export_functional_object_jar_files( scriptDirectoryName, sftpClient)) {
			SshTool.close( sftpClient);
			sshClient.disconnect();
			return false;
		}

		if ( !export_user_rule_jar_files( scriptDirectoryName, sftpClient)) {
			SshTool.close( sftpClient);
			sshClient.disconnect();
			return false;
		}

		if ( !export_other_jar_files( scriptDirectoryName, sftpClient)) {
			SshTool.close( sftpClient);
			sshClient.disconnect();
			return false;
		}

		SshTool.close( sftpClient);
		sshClient.disconnect();

		return true;
	}

	/**
	 * Returns true for exporting the ModelBuilder script to the specified file in CSV format for Grid successfully.
	 * @param experimentManager the experiment support manager
	 * @param scriptDirectoryName the name of directory for the script file on Grid
	 * @param logDirectoryName the name of directory for the log files on Grid
	 * @param programDirectory the name of directory for the program file on Grid
	 * @param numberOfTimes the number of the experiment times
	 * @param host the Grid host
	 * @param username the Grid user name
	 * @param password the Grid password
	 * @param intBuffer the couter for progress
	 * @param textField the text field to display progress
	 * @return true for exporting the ModelBuilder script to the specified file in CSV format for Grid successfully
	 */
	public boolean export_data_for_genetic_algorithm_on_grid(ExperimentManager experimentManager, String scriptDirectoryName, String logDirectoryName, String programDirectory, int numberOfTimes, String host, String username, String password, IntBuffer intBuffer, JTextField textField) {
		// TODO これはいずれ GA plugin側から呼ぶようにする必要がある
		if ( !is_initial_data_file_correct())
			return false;

		SshClient sshClient = SshTool.getSshClient( host, username, password);
		if ( null == sshClient)
			return false;

		SftpClient sftpClient = SshTool.getSftpClient( sshClient);
		if ( null == sftpClient) {
			sshClient.disconnect();
			return false;
		}

		if ( null != experimentManager) {
			if ( !experimentManager.isEmpty() && 0 < experimentManager.get_initial_value_count())
				experimentManager.export( scriptDirectoryName, logDirectoryName, programDirectory, numberOfTimes, sftpClient, intBuffer, textField);
		} else {
			for ( int i = 1; i <= numberOfTimes; ++i) {
				if ( !SshTool.directory_exists( sftpClient, scriptDirectoryName + "/" + String.valueOf( i)))
					sftpClient.mkdirs( scriptDirectoryName + "/" + String.valueOf( i));

				if ( !SshTool.directory_exists( sftpClient, scriptDirectoryName + "/" + String.valueOf( i))) {
					SshTool.close( sftpClient);
					sshClient.disconnect();
					return false;
				}

				File scriptFile; 
				try {
					scriptFile = File.createTempFile( "soars_", ".sor");
				} catch (IOException e) {
					SshTool.close( sftpClient);
					sshClient.disconnect();
					return false;
				}

				Exporter.execute_on_grid( scriptFile, null, String.valueOf( i), scriptDirectoryName, logDirectoryName, programDirectory, "", true);

				try {
					sftpClient.put( scriptFile.getAbsolutePath(), scriptDirectoryName + "/" + String.valueOf( i) + "/" + Constant._soarsScriptFilename);
				} catch (IOException e) {
					SshTool.close( sftpClient);
					sshClient.disconnect();
					scriptFile.delete();
					return false;
//				} catch (Throwable ex) {
//					scriptFile.delete();
//					return;
				}

				scriptFile.delete();

				intBuffer.put( 0, intBuffer.get( 0) + 1);
				textField.setText( String.valueOf( intBuffer.get( 0)));
				textField.update( textField.getGraphics());
			}
		}

		SshTool.close( sftpClient);
		sshClient.disconnect();

		return true;
	}

	/**
	 * @param scriptDirectoryName
	 * @param sftpClient
	 * @return
	 */
	private boolean export_user_data_zip_file(String scriptDirectoryName, SftpClient sftpClient) {
		// userdata.zipを作成してコピー
		if ( null == _rootDirectory || !_rootDirectory.exists())
			return false;

		File userDataDirectory = get_user_data_directory();
		if ( null == userDataDirectory || !userDataDirectory.exists())
			return false;

		File zipFile = null;

		try {
			zipFile = File.createTempFile( "userdata_", ".zip");
		} catch (IOException e1) {
			return false;
		}

		if ( !ZipUtility.compress( zipFile, userDataDirectory, _rootDirectory)) {
			zipFile.delete();
			return false;
		}

		try {
			sftpClient.put( zipFile.getAbsolutePath(), scriptDirectoryName + "/" + Constant._userDataZipFilename);
			zipFile.delete();
		} catch (IOException e) {
			zipFile.delete();
			return false;
		}

		return true;
	}

	/**
	 * @param scriptDirectoryName
	 * @param sftpClient
	 * @return
	 */
	private boolean export_functional_object_jar_files( String scriptDirectoryName, SftpClient sftpClient) {
		// 機能オブジェクト用jarファイルのコピー
		String currentDirectoryName = System.getProperty( Constant._soarsHome);
		if ( null == currentDirectoryName)
			return false;

		String[] jarFilenames = get_jar_filenames();
		for ( int i = 0; i < jarFilenames.length; ++i) {
			int index = Constant.get_index_of_functionalObjectDirectories( jarFilenames[ i]);
			if ( 0 > index)
				continue;

			String jarFilename = jarFilenames[ i].replace( Constant._functionalObjectDirectories[ index], Constant._gridFunctionalObjectDirectories[ index]);
//			String jarFilename = "";
//			if ( jarFilenames[ i].startsWith( Constant._functionalObjectDirectories[ 0]))
//				jarFilename = jarFilenames[ i].replace( Constant._functionalObjectDirectories[ 0], Constant._gridFunctionalObjectDirectories[ 0]);
//			else if ( jarFilenames[ i].startsWith( Constant._functionalObjectDirectories[ 1]))
//				jarFilename = jarFilenames[ i].replace( Constant._functionalObjectDirectories[ 1], Constant._gridFunctionalObjectDirectories[ 1]);
//			else
//				continue;

			int endIndex = jarFilename.lastIndexOf( '/');
			if ( 0 > endIndex)
				return false;

			String jarFileDirectoryName = jarFilename.substring( 0, endIndex);
			//System.out.println( jarFilename);
			//System.out.println( jarFileDirectoryName);
			sftpClient.mkdirs( scriptDirectoryName + jarFileDirectoryName);
			if ( !SshTool.directory_exists( sftpClient, scriptDirectoryName + jarFileDirectoryName))
				return false;

			try {
				sftpClient.put( ( new File( ( ( 1 != index) ? ( currentDirectoryName + "/") : "") + jarFilenames[ i])).getAbsolutePath(), scriptDirectoryName + jarFilename);
			} catch (IOException e) {
				return false;
			}
		}

		return true;
	}

	/**
	 * @param scriptDirectoryName
	 * @param sftpClient
	 * @return
	 */
	private boolean export_user_rule_jar_files( String scriptDirectoryName, SftpClient sftpClient) {
		// TODO Auto-generated method stub
		// ユーザ定義GUI用jarファイルのコピー
		String currentDirectoryName = System.getProperty( Constant._soarsHome);
		if ( null == currentDirectoryName)
			return false;

		File userRuleJarFilesFolder = get_user_rule_jarFiles_directory();
		if ( null == userRuleJarFilesFolder) {
			String projectFoldername = BasicEnvironment.get_instance().get( BasicEnvironment._projectFolderDirectoryKey, "");
			if ( projectFoldername.equals( ""))
				return false;

			userRuleJarFilesFolder = new File( projectFoldername + Constant._userRuleJarFilesExternalRelativePathName);
		}

		List<File> jarFiles = get_user_rule_jarFiles( userRuleJarFilesFolder);

		for ( File jarFile:jarFiles) {

			String jarFilename = jarFile.getAbsolutePath().replaceAll( "\\\\", "/").replace(
				userRuleJarFilesFolder.getAbsolutePath().replaceAll( "\\\\", "/"),
				"/" + Constant._gridFunctionalObjectRootDirectory + Constant._userRuleJarFilesInternalRelativePathName);

			int endIndex = jarFilename.lastIndexOf( '/');
			if ( 0 > endIndex)
				return false;

			String jarFileDirectoryName = jarFilename.substring( 0, endIndex);
			//System.out.println( jarFilename);
			//System.out.println( jarFileDirectoryName);
			sftpClient.mkdirs( scriptDirectoryName + jarFileDirectoryName);
			if ( !SshTool.directory_exists( sftpClient, scriptDirectoryName + jarFileDirectoryName))
				return false;

			try {
				sftpClient.put( jarFile.getAbsolutePath(), scriptDirectoryName + jarFilename);
			} catch (IOException e) {
				return false;
			}
		}

		return true;
	}

	/**
	 * @param userRuleJarFilesFolder
	 * @return
	 */
	public List<File> get_user_rule_jarFiles(File userRuleJarFilesFolder) {
		// TODO Auto-generated method stub
		List<File> jarFiles = new ArrayList<File>();

		if ( !userRuleJarFilesFolder.exists() || !userRuleJarFilesFolder.isDirectory())
			return jarFiles;

		get_user_rule_jarFiles( userRuleJarFilesFolder, jarFiles);

		return jarFiles;
	}

	/**
	 * @param folder
	 * @param jarFiles
	 */
	public void get_user_rule_jarFiles(File folder, List<File> jarFiles) {
		// TODO Auto-generated method stub
		File[] files = folder.listFiles();
		for ( File file:files) {
			if ( file.isDirectory())
				get_user_rule_jarFiles( file, jarFiles);
			else {
				if ( file.isFile() && file.getName().endsWith( ".jar"))
					jarFiles.add( file);
			}
		}
	}

	/**
	 * @param scriptDirectoryName
	 * @param sftpClient
	 * @return
	 */
	private boolean export_other_jar_files(String scriptDirectoryName, SftpClient sftpClient) {
		// クラスURLに記述されているjarファイルのコピー
		String currentDirectoryName = System.getProperty( Constant._soarsHome);
		if ( null == currentDirectoryName)
			return false;

		String[] lines = OtherScriptsManager.get_instance()._otherScripts.split( Constant._lineSeparator);
		if ( null == lines)
			return true;

		for ( int i = 0; i < lines.length; ++i) {
			int index = Constant.get_index_of_functionalObjectDirectories( "file:", lines[ i]);
			if ( 0 > index)
				continue;

			lines[ i] = lines[ i].substring( "file:".length());
			String jarFilename = lines[ i].replace( Constant._functionalObjectDirectories[ index], Constant._gridFunctionalObjectDirectories[ index]);
//			String jarFilename = "";
//			if ( lines[ i].startsWith( "file:" + Constant._functionalObjectDirectories[ 0])) {
//				lines[ i] = lines[ i].substring( "file:".length());
//				jarFilename = lines[ i].replace( Constant._functionalObjectDirectories[ 0], Constant._gridFunctionalObjectDirectories[ 0]);
//			} else if ( lines[ i].startsWith( "file:" + Constant._functionalObjectDirectories[ 1])) {
//				lines[ i] = lines[ i].substring( "file:".length());
//				jarFilename = lines[ i].replace( Constant._functionalObjectDirectories[ 1], Constant._gridFunctionalObjectDirectories[ 1]);
//			} else
//				continue;

			int endIndex = jarFilename.lastIndexOf( '/');
			if ( 0 > endIndex)
				return false;

			String jarFileDirectoryName = jarFilename.substring( 0, endIndex);
			//System.out.println( jarFilename);
			//System.out.println( jarFileDirectoryName);
			sftpClient.mkdirs( scriptDirectoryName + jarFileDirectoryName);
			if ( !SshTool.directory_exists( sftpClient, scriptDirectoryName + jarFileDirectoryName))
				return false;

			try {
				sftpClient.put( ( new File( ( ( 1 != index) ? ( currentDirectoryName + "/") : "") + lines[ i])).getAbsolutePath(), scriptDirectoryName + jarFilename);
			} catch (IOException e) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns true for exporting the ModelBuilder script to the specified file in CSV format for Application Builder successfully.
	 * @param file the specified file
	 * @param toDisplay whether to display the logs
	 * @param toFile whether to make the log files
	 * @return true for exporting the ModelBuilder script to the specified file in CSV format for Application Builder successfully
	 */
	public boolean export_data_on_demo(File file, boolean toDisplay, boolean toFile) {
		if ( !ExperimentManager.get_instance().isEmpty()) {
			if ( 0 == ExperimentManager.get_instance().get_initial_value_count())
				return false;

			if ( !ExperimentManager.get_instance().export_on_demo( file, toDisplay, toFile))
				return false;

			return ExperimentManager.get_instance().export_table_on_demo( file);
		} else
			return Exporter.execute_on_demo( file, null, null, "", toDisplay, toFile);
	}

	/**
	 * @param directories
	 * @return
	 */
	public GisDataManager import_gis_data(File[] directories) {
		GisDataManager gisDataManager = new GisDataManager();
		List<File> shapeFiles = gisDataManager.read( directories);
		if ( null == shapeFiles || shapeFiles.isEmpty())
			return null;

		if ( !gisDataManager.load( shapeFiles))
			return null;

		return gisDataManager;
	}

	/**
	 * 
	 */
	public boolean copy_objects() {
		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return false;

		File rootDirectory = new File( parentDirectory, Constant._visualShellRootDirectoryName);
		if ( !rootDirectory.mkdirs()) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		List<File> imagefiles = new ArrayList<File>();
		List<File> thumbnailImagefiles = new ArrayList<File>();
		if ( !get( _currentLayer).get_imagefiles_selected_objects_use( imagefiles, thumbnailImagefiles)
			|| ( imagefiles.size() != thumbnailImagefiles.size())) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		for ( int i = 0; i < imagefiles.size(); ++i) {
			File imagefile = imagefiles.get( i);
			File thumbnailImagefile = thumbnailImagefiles.get( i);
			if ( !FileUtility.copy( imagefile, new File( get_image_directory( rootDirectory), imagefile.getName()))) {
				FileUtility.delete( parentDirectory, true);
				return false;
			}
			if ( !FileUtility.copy( thumbnailImagefile, new File( get_thumbnail_image_directory( rootDirectory), thumbnailImagefile.getName()))) {
				FileUtility.delete( parentDirectory, true);
				return false;
			}
		}

		// ファイル変数及び初期データファイルも含める！
		List<File> files = new ArrayList<File>();
		if ( !get( _currentLayer).get_user_data_files_selected_objects_use( files)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		for ( File file:files) {
			File destPath = new File( get_user_data_directory( rootDirectory),
				( file.getAbsolutePath().substring( get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/"));
			if ( file.isDirectory()) {
				if ( destPath.exists() && !destPath.isDirectory()) {
					FileUtility.delete( parentDirectory, true);
					return false;
				}

				if ( !destPath.exists())
					destPath.mkdirs();
			} else {
				if ( destPath.getParentFile().exists() && !destPath.getParentFile().isDirectory()) {
					FileUtility.delete( parentDirectory, true);
					return false;
				}

				if ( !destPath.getParentFile().exists() && !destPath.getParentFile().mkdirs()) {
					FileUtility.delete( parentDirectory, true);
					return false;
				}

				if ( !FileUtility.copy( file, destPath)) {
					FileUtility.delete( parentDirectory, true);
					return false;
				}
			}
		}

		if ( !SaxWriter.execute( new File( rootDirectory, Constant._visualShellDataFilename), ( File[])imagefiles.toArray( new File[ 0]))) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		String systemTemporaryDirectory = SoarsCommonTool.get_system_temporary_directory();
		if ( !ZipUtility.compress( new File( systemTemporaryDirectory + _temporaryFilename), rootDirectory, parentDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		FileUtility.delete( parentDirectory, true);

		return true;
	}

	/**
	 * Returns true if the loading is completed successfully.
	 * @param graphics2D the graphics object of JAVA
	 * @param component the base class for all Swing components
	 * @return true if the loading is completed successfully
	 */
	public boolean paste_objects(Graphics2D graphics2D, JComponent component) {
		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return false;

		String systemTemporaryDirectory = SoarsCommonTool.get_system_temporary_directory();
		if ( !ZipUtility.decompress( new File( systemTemporaryDirectory + _temporaryFilename), parentDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		File rootDirectory = new File( parentDirectory, Constant._visualShellRootDirectoryName);
		rootDirectory = new File( parentDirectory, Constant._visualShellRootDirectoryName);
		if ( !rootDirectory.exists() || !rootDirectory.isDirectory()) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		if ( !SaxLoader.execute( new File( rootDirectory, Constant._visualShellDataFilename), rootDirectory, graphics2D, component)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		FileUtility.delete( parentDirectory, true);

		resize( component);
		update_preferred_size( component);
		return true;
	}

	/**
	 * Returns true for writing this object data successfully.
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing this object data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(Writer writer) throws SAXException {
		writer.startElement( null, null, "layer_data", new AttributesImpl());
		for ( int i = 0; i < size(); ++i) {
			if ( !get( i).write( writer, i + 1))
				return false;
		}
		writer.endElement( null, null, "layer_data");
		return true;
	}

	/**
	 * Returns true for writing the selected object data successfully.
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing the selected object data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write_selected_objects(Writer writer) throws SAXException {
		writer.startElement( null, null, "layer_data", new AttributesImpl());

		if ( !get( _currentLayer).write_selected_objects( writer, Constant._reservedLayerName))
			return false;

		writer.endElement( null, null, "layer_data");
		return true;
	}

	/**
	 * Flushes top the selected objects.
	 * @param component the base class for all Swing components
	 */
	public void flush_top(JComponent component) {
		Vector<DrawObject> drawObjects = new Vector<DrawObject>();
		get_selected( drawObjects);
		if ( drawObjects.isEmpty() || 2 > drawObjects.size())
			return;

		int top = 0;
		for ( int i = 0; i < drawObjects.size(); ++i) {
			DrawObject drawObject = drawObjects.get( i);
			if ( 0 == i || drawObject._position.y < top)
				top = drawObject._position.y;
		}

		for ( DrawObject drawObject:drawObjects)
			drawObject.move( 0, top - drawObject._position.y);

		update_preferred_size( component);
		component.repaint();
	}

	/**
	 * Flushes bottom the selected objects.
	 * @param component the base class for all Swing components
	 */
	public void flush_bottom(JComponent component) {
		Vector<DrawObject> drawObjects = new Vector<DrawObject>();
		get_selected( drawObjects);
		if ( drawObjects.isEmpty() || 2 > drawObjects.size())
			return;

		int bottom = 0;
		for ( DrawObject drawObject:drawObjects) {
			if ( ( drawObject._position.y + drawObject._dimension.height + drawObject._nameDimension.height) > bottom)
				bottom = ( drawObject._position.y + drawObject._dimension.height + drawObject._nameDimension.height);
		}

		for ( DrawObject drawObject:drawObjects)
			drawObject.move( 0, bottom - drawObject._nameDimension.height - drawObject._dimension.height - drawObject._position.y);

		update_preferred_size( component);
		component.repaint();
	}

	/**
	 * Flushes left the selected objects.
	 * @param component the base class for all Swing components
	 */
	public void flush_left(JComponent component) {
		Vector<DrawObject> drawObjects = new Vector<DrawObject>();
		get_selected( drawObjects);
		if ( drawObjects.isEmpty() || 2 > drawObjects.size())
			return;

		int left = 0;
		for ( int i = 0; i < drawObjects.size(); ++i) {
			DrawObject drawObject = drawObjects.get( i);
			if ( 0 == i || drawObject._position.x < left)
				left = drawObject._position.x;
		}

		for ( DrawObject drawObject:drawObjects)
			drawObject.move( left - drawObject._position.x, 0);

		update_preferred_size( component);
		component.repaint();
	}

	/**
	 * Flushes the selected objects right.
	 * @param component the base class for all Swing components
	 */
	public void flush_right(JComponent component) {
		Vector<DrawObject> drawObjects = new Vector<DrawObject>();
		get_selected( drawObjects);
		if ( drawObjects.isEmpty() || 2 > drawObjects.size())
			return;

		int right = 0;
		for ( DrawObject drawObject:drawObjects) {
			if ( ( drawObject._position.x + drawObject._dimension.width) > right)
				right = ( drawObject._position.x + drawObject._dimension.width);
		}

		for ( DrawObject drawObject:drawObjects)
			drawObject.move( right - drawObject._dimension.width - drawObject._position.x, 0);

		update_preferred_size( component);
		component.repaint();
	}

	/**
	 * Makes vertical gaps between the selected objects equal.
	 * @param component the base class for all Swing components
	 */
	public void vertical_equal_layout(JComponent component) {
		Vector<DrawObject> dObjects = new Vector<DrawObject>();
		get_selected( dObjects);
		if ( dObjects.isEmpty() || 3 > dObjects.size())
			return;

		DrawObject[] drawObjects = sort_drawObjects( dObjects, true);

		int top = 0, bottom = 0, sum = 0;
		for ( int i = 0; i < drawObjects.length; ++i) {
			sum += ( drawObjects[ i]._dimension.height + drawObjects[ i]._nameDimension.height);
			if ( 0 == i || drawObjects[ i]._position.y < top)
				top = drawObjects[ i]._position.y;
			if ( ( drawObjects[ i]._position.y + drawObjects[ i]._dimension.height + drawObjects[ i]._nameDimension.height) > bottom)
				bottom = ( drawObjects[ i]._position.y + drawObjects[ i]._dimension.height + drawObjects[ i]._nameDimension.height);
		}

		int space = ( ( bottom - top - sum) / ( drawObjects.length - 1));

		for ( int i = 0; i < drawObjects.length; ++i) {
			if ( 0 < i)
				drawObjects[ i].move( 0, top - drawObjects[ i]._position.y);

			top += ( drawObjects[ i]._dimension.height + drawObjects[ i]._nameDimension.height + space);
		}

		update_preferred_size( component);
		component.repaint();
	}

	/**
	 * Makes horizontal gaps between the selected objects equal.
	 * @param component the base class for all Swing components
	 */
	public void horizontal_equal_layout(JComponent component) {
		Vector<DrawObject> dObjects = new Vector<DrawObject>();
		get_selected( dObjects);
		if ( dObjects.isEmpty() || 3 > dObjects.size())
			return;

		DrawObject[] drawObjects = sort_drawObjects( dObjects, false);

		int left = 0, right = 0, sum = 0;
		for ( int i = 0; i < drawObjects.length; ++i) {
			sum += drawObjects[ i]._dimension.width;
			if ( 0 == i || drawObjects[ i]._position.x < left)
				left = drawObjects[ i]._position.x;
			if ( ( drawObjects[ i]._position.x + drawObjects[ i]._dimension.width) > right)
				right = ( drawObjects[ i]._position.x + drawObjects[ i]._dimension.width);
		}

		int space = ( ( right - left - sum) / ( drawObjects.length - 1));

		for ( int i = 0; i < drawObjects.length; ++i) {
			if ( 0 < i)
				drawObjects[ i].move( left - drawObjects[ i]._position.x, 0);

			left += ( drawObjects[ i]._dimension.width + space);
		}

		update_preferred_size( component);
		component.repaint();
	}

	/**
	 * @param dObjects
	 * @param vertical
	 * @return
	 */
	private DrawObject[] sort_drawObjects(Vector<DrawObject> dObjects, boolean vertical) {
		if ( dObjects.isEmpty())
			return null;

		DrawObject[] drawObjects = ( DrawObject[])dObjects.toArray( new DrawObject[ 0]);
		if ( 1 == drawObjects.length)
			return drawObjects;

		QuickSort.sort( drawObjects, new DrawObjectPositionComparator( vertical));
		return drawObjects;
	}

	/**
	 * Swap the names of the selected objects.
	 * @param component the base class for all Swing components
	 */
	public void swap_names(JComponent component) {
		Vector<DrawObject> drawObjects = new Vector<DrawObject>();
		get_selected( drawObjects);
		if ( 2 != drawObjects.size())
			return;

		if ( !( ( drawObjects.get( 0) instanceof AgentRole 	&& drawObjects.get( 1) instanceof AgentRole)
			|| ( drawObjects.get( 0) instanceof SpotRole && drawObjects.get( 1) instanceof SpotRole)))
			return;

		String name0 = drawObjects.get( 0)._name;
		String name1 = drawObjects.get( 1)._name;

		drawObjects.get( 0).rename( name1, ( Graphics2D)component.getGraphics());
		drawObjects.get( 1).rename( name0, ( Graphics2D)component.getGraphics());

		Observer.get_instance().modified();

		update_preferred_size( component);
		component.repaint();
	}

	/**
	 * Returns the array of the jar file names.
	 * @param list the array for the jar file names
	 * @return the array of the jar file names
	 */
	public String[] get_jar_filenames() {
		List<String> list = new ArrayList<String>();
		for ( Layer layer:this)
			layer.get_jar_filenames( list);

		// for initial data file
		String initialDataFileJarFilename = ( Constant._functionalObjectDirectories[ 0] + "/" + Constant._initialDataFileJarFileName);
		if ( initial_data_file_exists() && !list.contains( initialDataFileJarFilename))
			list.add( initialDataFileJarFilename);

		return list.toArray( new String[ 0]);
	}

	/**
	 * Returns true if the connection between the specified role object and one which has the specified connection object contradicts.
	 * @param connectInObject the specified connection object
	 * @param role the specified role object
	 * @return true if the connection between role objects contradicts
	 */
	public boolean contradict(ConnectInObject connectInObject, Role role) {
		return get( _currentLayer).contradict( connectInObject, role);
	}

	/**
	 * @return the graphic properties
	 */
	public String get_graphic_properties() {
		String text = get_image_properties();

		Point originCoordinate = get_origin_coordinate( "spot");
		if ( null == originCoordinate)
			originCoordinate = new Point( 0, 0);

		text += get_graphic_properties( "spot", originCoordinate);
		text += get_graphic_properties( "agent", originCoordinate);

		return text;
	}

	/**
	 * @return
	 */
	private String get_image_properties() {
		String text = "";
		Iterator iterator = ImagePropertyManager.get_instance().entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String filename = ( String)entry.getKey();
			ImageProperty imageProperty = ( ImageProperty)entry.getValue();
			text += ( "image_property\t" + filename + "\t" + String.valueOf( imageProperty._width) + "\t" + String.valueOf( imageProperty._height) + "\n");
		}
		return text;
	}

	/**
	 * Creates the coordinate for the top left corner of objects, and returns it.
	 * @param type the type of the object
	 * @return the coordinates for the top left corner
	 */
	private Point get_origin_coordinate(String type) {
		Point originCoordinate = null;
		for ( Layer layer:this)
			originCoordinate = layer.get_origin_coordinate( type, originCoordinate);
		return originCoordinate;
	}

	/**
	 * Creates the text which contains the relative coordinates of objects, and returns it.
	 * @param type the type of the object
	 * @param originCoordinate the specified coordinate for the top left corner of objects
	 * @return the text which contains the relative coordinatesof objects
	 */
	private String get_graphic_properties(String type, Point originCoordinate) {
		String text = "";
		for ( Layer layer:this)
			text += layer.get_graphic_properties( type, originCoordinate);
		return text;
	}

	/**
	 * @return the chart properties
	 */
	public String get_chart_properties() {
		String text = SimulationManager.get_instance().get_properties();
		for ( Layer layer:this)
			text += layer.get_chart_properties();
		return text;
	}

	/**
	 * @return
	 */
	public boolean exist_global_spot_and_role() {
		// TODO Auto-generated method stub
		for ( Layer layer:this) {
			if ( layer.exist_global_spot_and_role())
				return true;
		}
		return false;
	}

	/**
	 * @return
	 */
	public boolean exist_global_spot() {
		// TODO Auto-generated method stub
		for ( Layer layer:this) {
			if ( layer.exist_global_spot())
				return true;
		}
		return false;
	}

	/**
	 * @return
	 */
	public boolean exist_global_role() {
		// TODO Auto-generated method stub
		for ( Layer layer:this) {
			if ( layer.exist_global_role())
				return true;
		}
		return false;
	}

	/**
	 * @param component
	 * @return
	 */
	public boolean create_global_spot_and_role(JComponent component) {
		// TODO Auto-generated method stub
		return get( 0).create_global_spot_and_role(component);
	}

	/**
	 * @param range
	 */
	public void get_gis_range(double[] range) {
		// TODO Auto-generated method stub
		for ( Layer layer:this)
			layer.get_gis_range( range);
	}

	/**
	 * @param range
	 * @param ratio
	 */
	public void update_gis_coordinates(double[] range, double[] ratio) {
		// TODO Auto-generated method stub
		for ( Layer layer:this)
			layer.update_gis_coordinates( range, ratio);
	}

	/**
	 * For debug print.
	 */
	public void print() {
		print_agents();
		print_spots();
	}

	/**
	 * For debug print.
	 */
	public void print_agents() {
		for ( Layer layer:this)
			layer.print_agents();
	}

	/**
	 * For debug print.
	 */
	public void print_spots() {
		for ( Layer layer:this)
			layer.print_spots();
	}
}
