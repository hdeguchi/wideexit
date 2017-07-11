/*
 * Created on 2005/11/15
 */
package soars.application.visualshell.object.scripts;

import java.io.File;
import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.common.soars.environment.BasicEnvironment;
import soars.common.utility.xml.sax.Writer;


/**
 * @author kurata
 */
public class OtherScriptsManager {

	/**
	 * 
	 */
	public String _otherScripts = "";

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private OtherScriptsManager _otherScriptsManager = null;

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
			if ( null == _otherScriptsManager) {
				_otherScriptsManager = new OtherScriptsManager();
			}
		}
	}

	/**
	 * 
	 */
	public static OtherScriptsManager get_instance() {
		if ( null == _otherScriptsManager) {
			System.exit( 0);
		}

		return _otherScriptsManager;
	}

	/**
	 * 
	 */
	public OtherScriptsManager() {
		super();
	}

	/**
	 * 
	 */
	public void cleanup() {
		_otherScripts = "";
	}

	/**
	 * @return
	 */
	public String get_initial_data() {
		String[] lines = _otherScripts.split( Constant._lineSeparator);
		if ( null == lines)
			return "";

		String script = "";
		for ( int i = 0; i < lines.length; ++i)
			script += ( ResourceManager.get_instance().get( "initial.data.other.scripts") + "\t" + lines[ i] + Constant._lineSeparator);

		return ( script + Constant._lineSeparator);
	}

	/**
	 * @param scriptDirectory
	 * @param programDirectory 
	 * @param grid
	 * @param ga
	 * @return
	 */
	public String get_script(String scriptDirectory, String programDirectory, boolean grid, boolean ga) {
		String script = "";

		if ( !grid && !ga && LayerManager.get_instance().contains_available_chartObject()) {
			for ( int i = 0; i < Constant._chartManagerJarScript.length; ++i) {
				if ( 0 > _otherScripts.indexOf( Constant._chartManagerJarScript[ i]))
					script += Constant._chartManagerJarScript[ i];
			}
		}

		String[] jarFilenames = LayerManager.get_instance().get_jar_filenames();
		for ( int i = 0; i < jarFilenames.length; ++i) {
			// TODO Auto-generated method stub
			//if ( jarFilenames[ i].startsWith( Constant._functionalObjectDirectories[ 0]))
			//	System.out.println( jarFilenames[ i].replace( Constant._functionalObjectDirectories[ 0], Constant._gridFunctionalObjectDirectories[ 0]));
			//else if ( jarFilenames[ i].startsWith( Constant._functionalObjectDirectories[ 1]))
			//	System.out.println( jarFilenames[ i].replace( Constant._functionalObjectDirectories[ 1], Constant._gridFunctionalObjectDirectories[ 1]));
			String line = "file:";
			if ( scriptDirectory.equals( ""))
				line += ( jarFilenames[ i] + Constant._lineSeparator);
			else {
				// TODO Auto-generated method stub
				int index = Constant.get_index_of_functionalObjectDirectories( jarFilenames[ i]);
				if ( 0 > index)
					line += ( ( programDirectory.equals( "") ? "" : ( programDirectory + "/")) + jarFilenames[ i] + Constant._lineSeparator);
				else
					line += ( scriptDirectory + jarFilenames[ i].replace( Constant._functionalObjectDirectories[ index], Constant._gridFunctionalObjectDirectories[ index]) + Constant._lineSeparator);
//				if ( jarFilenames[ i].startsWith( Constant._functionalObjectDirectories[ 0]))
//					line += ( scriptDirectory + jarFilenames[ i].replace( Constant._functionalObjectDirectories[ 0], Constant._gridFunctionalObjectDirectories[ 0]) + Constant._lineSeparator);
//				else if ( jarFilenames[ i].startsWith( Constant._functionalObjectDirectories[ 1]))
//					line += ( scriptDirectory + jarFilenames[ i].replace( Constant._functionalObjectDirectories[ 1], Constant._gridFunctionalObjectDirectories[ 1]) + Constant._lineSeparator);
//				else
//					line += ( ( programDirectory.equals( "") ? "" : ( programDirectory + "/")) + jarFilenames[ i] + Constant._lineSeparator);
			}
			if ( 0 > _otherScripts.indexOf( line))
				script += line;
		}

		if ( Environment.get_instance().is_exchange_algebra_enable()) {
			for ( int i = 0; i < Constant._exchangeAlgebraJar.length; ++i) {
				String line = "file:"
					+ ( programDirectory.equals( "") ? "" : ( programDirectory + "/"))
					+ Constant._exchangeAlgebraJar[ i] + Constant._lineSeparator;
				if ( 0 > _otherScripts.indexOf( line))
					script += line;
			}
		}

		// TODO for Mr.Ichikawa
		script += "file:"
			+ ( programDirectory.equals( "") ? "" : ( programDirectory + "/"))
			+ Constant._rulesJar + Constant._lineSeparator;

		// TODO for User GUI
		script += "file:"
			+ ( programDirectory.equals( "") ? "" : ( programDirectory + "/"))
			+ Constant._userrulesJar + Constant._lineSeparator;

		// TODO for User GUI
		File userRuleJarFilesFolder = LayerManager.get_instance().get_user_rule_jarFiles_directory();
		if ( null == userRuleJarFilesFolder) {
			String projectFoldername = BasicEnvironment.get_instance().get( BasicEnvironment._projectFolderDirectoryKey, "");
			if ( projectFoldername.equals( ""))
				return "";

			userRuleJarFilesFolder = new File( projectFoldername + Constant._userRuleJarFilesExternalRelativePathName);
		}

		List<File> jarFiles = LayerManager.get_instance().get_user_rule_jarFiles( userRuleJarFilesFolder);
		if ( !grid) {
			if ( !LayerManager.get_instance().exist_user_rule_jarFiles_directory()) {
				for ( File jarFile:jarFiles) {
					script += jarFile.toURI() + Constant._lineSeparator;
					//script += "file:" + jarFile.getAbsolutePath().replaceAll( "\\\\", "/") + Constant._lineSeparator;
//					String path = jarFile.getAbsolutePath().replaceAll( "\\\\", "/");
//					path = path.replaceAll( " ", "%20");
//					script += "file:/" + path + Constant._lineSeparator;
				}
			} else {
				for ( File jarFile:jarFiles)
					script += "file:" + Constant._reservedUserRuleJarFilesInternalDirectory
						+ jarFile.getAbsolutePath().substring( userRuleJarFilesFolder.getAbsolutePath().length()).replaceAll( "\\\\", "/") + Constant._lineSeparator;
			}
		} else {
			for ( File jarFile:jarFiles)
				script += "file:" + scriptDirectory
					+ jarFile.getAbsolutePath().replaceAll( "\\\\", "/").replace(
							userRuleJarFilesFolder.getAbsolutePath().replaceAll( "\\\\", "/"),
							"/" + Constant._gridFunctionalObjectRootDirectory + Constant._userRuleJarFilesInternalRelativePathName) + Constant._lineSeparator;
		}

		if ( !grid)
			script += _otherScripts;
		else {
			String[] lines = _otherScripts.split( Constant._lineSeparator);
			if ( null == lines)
				return "";

			for ( int i = 0; i < lines.length; ++i) {
				// TODO Auto-generated method stub
				int index = Constant.get_index_of_functionalObjectDirectories( "file:", lines[ i]);
				if ( 0 <= index)
					lines[ i] = lines[ i].replace( Constant._functionalObjectDirectories[ index], scriptDirectory + Constant._gridFunctionalObjectDirectories[ index]);

				script += ( lines[ i] + Constant._lineSeparator);
//				if ( lines[ i].startsWith( "file:" + Constant._functionalObjectDirectories[ 0]))
//					lines[ i] = lines[ i].replace( Constant._functionalObjectDirectories[ 0], script_directory + Constant._gridFunctionalObjectDirectories[ 0]);
//				else if ( lines[ i].startsWith( "file:" + Constant._functionalObjectDirectories[ 1]))
//					lines[ i] = lines[ i].replace( Constant._functionalObjectDirectories[ 1], script_directory + Constant._gridFunctionalObjectDirectories[ 1]);
//
//				script += ( lines[ i] + Constant._lineSeparator);
			}
		}

		if ( script.equals( ""))
			return "";

		return ( "classURL" + Constant._lineSeparator + script + Constant._lineSeparator
			+ ( script.endsWith( Constant._lineSeparator) ? "" : Constant._lineSeparator));
	}

	/**
	 * @return
	 */
	public String get_script_on_docker() {
		// TODO Auto-generated method stub
		String script = "";

		File userModuleDirectory = BasicEnvironment.get_instance().get_projectSubFoler( Constant._userModuleDirectoryName);
		if ( null == userModuleDirectory)
			return "";

		String[] jarFilenames = LayerManager.get_instance().get_jar_filenames();
		for ( int i = 0; i < jarFilenames.length; ++i) {
			String line = "file:";
			line += ( !jarFilenames[ i].startsWith( userModuleDirectory.getAbsolutePath().replaceAll( "\\\\", "/")))
				? jarFilenames[ i] + Constant._lineSeparator
				: jarFilenames[ i].replace( userModuleDirectory.getAbsolutePath().replaceAll( "\\\\", "/"), Constant._userModuleDirectoryNameForDocker) + Constant._lineSeparator;
			if ( 0 > _otherScripts.indexOf( line))
				script += line;
		}

		if ( Environment.get_instance().is_exchange_algebra_enable()) {
			for ( int i = 0; i < Constant._exchangeAlgebraJar.length; ++i) {
				String line = "file:"
					+ Constant._exchangeAlgebraJar[ i] + Constant._lineSeparator;
				if ( 0 > _otherScripts.indexOf( line))
					script += line;
			}
		}

		// TODO for Mr.Ichikawa
		script += "file:"
			+ Constant._rulesJar + Constant._lineSeparator;

		// TODO for User GUI
		script += "file:"
			+ Constant._userrulesJar + Constant._lineSeparator;

		// TODO for User GUI
		File userRuleJarFilesFolder = LayerManager.get_instance().get_user_rule_jarFiles_directory();
		if ( null == userRuleJarFilesFolder) {
			String projectFoldername = BasicEnvironment.get_instance().get( BasicEnvironment._projectFolderDirectoryKey, "");
			if ( projectFoldername.equals( ""))
				return "";

			userRuleJarFilesFolder = new File( projectFoldername + Constant._userRuleJarFilesExternalRelativePathName);
		}

		List<File> jarFiles = LayerManager.get_instance().get_user_rule_jarFiles( userRuleJarFilesFolder);
		for ( File jarFile:jarFiles)
			script += "file:"
				+ jarFile.getAbsolutePath().replaceAll( "\\\\", "/").replace(
						userRuleJarFilesFolder.getAbsolutePath().replaceAll( "\\\\", "/"),
						".." + Constant._userRuleJarFilesInternalRelativePathName) + Constant._lineSeparator;

		if ( script.equals( ""))
			return "";

		return ( "classURL" + Constant._lineSeparator + script + Constant._lineSeparator
			+ ( script.endsWith( Constant._lineSeparator) ? "" : Constant._lineSeparator));
	}

	/**
	 * @param otherScripts
	 * @return
	 */
	public boolean append(String otherScripts) {
		_otherScripts += otherScripts;
		return true;
	}

	/**
	 * @param writer
	 * @return
	 */
	public boolean write(Writer writer) throws SAXException {
		if ( _otherScripts.equals( ""))
			return true;

		writer.startElement( null, null, "other_scripts_data", new AttributesImpl());

		writer.characters( _otherScripts.toCharArray(), 0, _otherScripts.length());

		writer.endElement( null, null, "other_scripts_data");

		return true;
	}
}
