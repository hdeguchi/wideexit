/**
 * 
 */
package soars.application.builder.animation.document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.builder.animation.executor.launcher.AnimatorLauncher;
import soars.application.builder.animation.file.loader.SaxLoader;
import soars.application.builder.animation.file.writer.SaxWriter;
import soars.application.builder.animation.main.Constant;
import soars.application.builder.animation.main.MainFrame;
import soars.application.builder.animation.main.ResourceManager;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.file.ZipUtility;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class DocumentManager extends HashMap {


	/**
	 * 
	 */
	static public final String _root_directory = "animation";


	/**
	 * 
	 */
	static private Object _lock = new Object();


	/**
	 * 
	 */
	static private DocumentManager _documentManager = null;


	/**
	 * 
	 */
	static public String[][] _languages = null;


	/**
	 * 
	 */
	private File _current_file = null;


	/**
	 * 
	 */
	private boolean _modified = false;


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
			if ( null == _documentManager) {
				_documentManager = new DocumentManager();
				_documentManager.initialize();
			}
		}
	}

	/**
	 * @return
	 */
	public static DocumentManager get_instance() {
		if ( null == _documentManager) {
			System.exit( 0);
		}

		return _documentManager;
	}

	/**
	 * 
	 */
	public DocumentManager() {
		super();
	}

	/**
	 * 
	 */
	private void initialize() {
		TreeMap languageMap = new TreeMap();
		languageMap.put( "ja", new String[] { "ja", ResourceManager.get_instance().get( "language.tree.japanese")});
		languageMap.put( "en", new String[] { "en", ResourceManager.get_instance().get( "language.tree.english")});

		String default_language = Locale.getDefault().getLanguage();
		String[] words = ( String[])languageMap.get( default_language);
		if ( null == words) {
			default_language = "en";
			words = ( String[])languageMap.get( "en");
		}

		List list = new ArrayList();
		list.add( words);

		Iterator iterator = languageMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String language = ( String)entry.getKey();
			if ( language.equals( default_language))
				continue;

			words = ( String[])entry.getValue();
			list.add( words);
		}

		_languages = ( String[][])list.toArray( new String[ 0][]);

		for ( int i = 0; i < _languages.length; ++i)
			put( _languages[ i][ 1], new Document( _languages[ i]));
	}

	/**
	 * @param language
	 * @return
	 */
	public Document get_document(String language) {
		for ( int i = 0; i < _languages.length; ++i) {
			Document document = ( Document)get( _languages[ i][ 1]);
			if ( document._language.equals( language))
				return document;
		}
		return null;
	}

	/**
	 * @return
	 */
	public String[] get_language_names() {
		List list = new ArrayList();
		for ( int i = 0; i < _languages.length; ++i)
			list.add( _languages[ i][ 1]);

		return ( String[])list.toArray( new String[ 0]);
	}

	/**
	 * @param language
	 * @return
	 */
	public String get_language_name(String language) {
		for ( int i = 0; i < _languages.length; ++i) {
			if ( _languages[ i][ 0].equals( language))
				return _languages[ i][ 1];
		}
		return ResourceManager.get_instance().get( "language.tree.english");
	}

	/**
	 * @param language_name
	 * @return
	 */
	public String get_language(String language_name) {
		for ( int i = 0; i < _languages.length; ++i) {
			if ( _languages[ i][ 1].equals( language_name))
				return _languages[ i][ 0];
		}
		return "en";
	}

	/**
	 * @return
	 */
	public boolean exist_datafile() {
		return ( null == _current_file) ? false : true;
	}

	/**
	 * @return
	 */
	public File get_current_file() {
		return _current_file;
	}

	/**
	 * 
	 */
	public void modified() {
		_modified = true;

		String title = ResourceManager.get_instance().get( "application.title")
			+ ( ( null == _current_file) ? "" : ( " - " + _current_file.getName()))
			+ ResourceManager.get_instance().get( "state.edit.modified");
		if ( !MainFrame.get_instance().getTitle().equals( title))
			MainFrame.get_instance().setTitle( title);
	}

	/**
	 * @return
	 */
	public boolean isModified() {
		return _modified;
	}

	/**
	 * 
	 */
	public void reset() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Document document = ( Document)entry.getValue();
			document.reset();
		}
		_current_file = null;
		_modified = false;
	}

	/**
	 * @param file
	 * @return
	 */
	public boolean load(File file) {
		if ( !SaxLoader.execute( file))
			return false;

		_current_file = file;

		return true;
	}

	/**
	 * @return
	 */
	public boolean save() {
		if ( null == _current_file)
			return false;

		return save_as( _current_file);
	}

	/**
	 * @param file
	 * @return
	 */
	public boolean save_as(File file) {
		if ( !SaxWriter.execute( file))
			return false;

		_current_file = file;
		_modified = false;
		return true;
	}

	/**
	 * @param file
	 * @param data_filename
	 * @param language_name
	 * @return
	 */
	public boolean export_archive(File file, String data_filename, String language_name) {
		File animator_runner_parent_directory = make( data_filename, language_name);
		if ( null == animator_runner_parent_directory)
			return false;

		File animator_runner_root_directory = new File( animator_runner_parent_directory, _root_directory);
		if ( !ZipUtility.compress( file,
			new File( animator_runner_parent_directory, _root_directory), animator_runner_parent_directory)) {
			FileUtility.delete( animator_runner_parent_directory, true);
			return false;
		}

		FileUtility.delete( animator_runner_parent_directory, true);

		return true;
	}

	/**
	 * @param anm_filename
	 * @param language_name
	 * @return
	 */
	public boolean run_application(String anm_filename, String language_name) {
		try {
			File animator_runner_parameter_file = File.createTempFile( "parameter", ".js");
			if ( !setup_parameter_file( animator_runner_parameter_file, language_name)) {
				animator_runner_parameter_file.delete();
				return false;
			}

			if ( !AnimatorLauncher.run( get_language( language_name), anm_filename, animator_runner_parameter_file.getAbsolutePath())) {
				animator_runner_parameter_file.delete();
				return false;
			}

			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param data_filename
	 * @param language_name
	 * @return
	 */
	private File make(String data_filename, String language_name) {
		File animator_runner_parent_directory = SoarsCommonTool.make_parent_directory();
		if ( null == animator_runner_parent_directory)
			return null;

		File animator_runner_root_directory = new File( animator_runner_parent_directory, _root_directory);
		if ( !animator_runner_root_directory.mkdirs()) {
			FileUtility.delete( animator_runner_parent_directory, true);
			return null;
		}

		File animator_runner_home_directory = new File( animator_runner_root_directory, Constant._animatorRunnerHomeDirectory);
		if ( !animator_runner_home_directory.mkdirs()) {
			FileUtility.delete( animator_runner_parent_directory, true);
			return null;
		}

		File animator_runner_data_directory = new File( animator_runner_root_directory, Constant._animatorRunnerDataDirectory);
		if ( !animator_runner_data_directory.mkdirs()) {
			FileUtility.delete( animator_runner_parent_directory, true);
			return null;
		}

		String current_directory_name = System.getProperty( Constant._soarsHome);
		File current_directory = new File( current_directory_name);
		if ( null == current_directory) {
			FileUtility.delete( animator_runner_parent_directory, true);
			return null;
		}

		File library_directory = new File( current_directory_name + Constant._libraryDirectory);
		if ( null == library_directory) {
			FileUtility.delete( animator_runner_parent_directory, true);
			return null;
		}

		File json_library_directory = new File( current_directory_name + Constant._jsonLibraryDirectory);
		if ( null == json_library_directory) {
			FileUtility.delete( animator_runner_parent_directory, true);
			return null;
		}

		File chart_library_directory = new File( current_directory_name + Constant._chartLibraryDirectory);
		if ( null == chart_library_directory) {
			FileUtility.delete( animator_runner_parent_directory, true);
			return null;
		}

		File plot_library_directory = new File( current_directory_name + Constant._plotLibraryDirectory);
		if ( null == plot_library_directory) {
			FileUtility.delete( animator_runner_parent_directory, true);
			return null;
		}


		if ( !FileUtility.copy( new File( library_directory, Constant._animatorRunnerJarFilename),
			new File( animator_runner_root_directory, Constant._animatorRunnerJarFilename))) {
			FileUtility.delete( animator_runner_parent_directory, true);
			return null;
		}

		if ( !FileUtility.copy( new File( current_directory, Constant._animatorJarFilename),
			new File( animator_runner_home_directory, Constant._animatorJarFilename))) {
			FileUtility.delete( animator_runner_parent_directory, true);
			return null;
		}

		if ( !FileUtility.copy( new File( library_directory, Constant._animatorRunnerLauncherJarFilename),
			new File( animator_runner_home_directory, Constant._animatorRunnerLauncherJarFilename))) {
			FileUtility.delete( animator_runner_parent_directory, true);
			return null;
		}

		if ( !FileUtility.copy( new File( json_library_directory, Constant._jsonJarFilename),
			new File( animator_runner_home_directory, Constant._jsonJarFilename))) {
			FileUtility.delete( animator_runner_parent_directory, true);
			return null;
		}

		if ( !FileUtility.copy( new File( chart_library_directory, Constant._chartJarFilename),
			new File( animator_runner_home_directory, Constant._chartJarFilename))) {
			FileUtility.delete( animator_runner_parent_directory, true);
			return null;
		}

		if ( !FileUtility.copy( new File( plot_library_directory, Constant._plotJarFilename),
			new File( animator_runner_home_directory, Constant._plotJarFilename))) {
			FileUtility.delete( animator_runner_parent_directory, true);
			return null;
		}

		if ( !FileUtility.copy( new File( library_directory, Constant._iconFilename),
			new File( animator_runner_home_directory, Constant._iconFilename))) {
			FileUtility.delete( animator_runner_parent_directory, true);
			return null;
		}

		if ( !FileUtility.copy( new File( data_filename),
			new File( animator_runner_data_directory, Constant._animatorRunnerDataFilename))) {
			FileUtility.delete( animator_runner_parent_directory, true);
			return null;
		}

		if ( !setup_parameter_file( new File( animator_runner_data_directory, Constant._animatorRunnerParameterFilename), language_name)) {
			FileUtility.delete( animator_runner_parent_directory, true);
			return null;
		}

		return animator_runner_parent_directory;
	}

	/**
	 * @param animator_runner_parameter_file
	 * @param language_name
	 * @return
	 */
	private boolean setup_parameter_file(File animator_runner_parameter_file, String language_name) {
		JSONArray jsonArray = get_parameter( language_name);
		if ( null == jsonArray)
			return false;

		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( animator_runner_parameter_file), "UTF-8");
			jsonArray.write( outputStreamWriter);
			outputStreamWriter.flush();
			outputStreamWriter.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param language_name
	 * @return
	 */
	public JSONArray get_parameter(String language_name) {
		JSONArray jsonArray = new JSONArray();
		Document document = ( Document)get( language_name);
		if ( !document.write( jsonArray))
			return null;

		return jsonArray;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException 
	 */
	public boolean write(Writer writer) throws SAXException {
		writer.startElement( null, null, "language_data", new AttributesImpl());

		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Document document = ( Document)entry.getValue();
			if ( !document.write( writer))
				return false;
		}

		writer.endElement( null, null, "language_data");

		return true;
	}
}
