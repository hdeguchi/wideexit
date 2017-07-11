/**
 * 
 */
package soars.application.visualshell.file.docker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soars.application.visualshell.file.exporter.script.Exporter;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.object.experiment.ExperimentManager;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.common.soars.environment.BasicEnvironment;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.file.ZipUtility;

/**
 * @author kurata
 *
 */
public class DockerFilesetCreator {

	/**
	 * 
	 */
	static private final String _templateFolderName = "../resource/docker";

	/**
	 * 
	 */
	static private final String _buildTemplateFolderName = "../resource/docker/build";

	/**
	 *  
	 */
	private DockerFilesetProperty _dockerFilesetProperty = null;

	/**
	 * 
	 */
	private File _parentFolder = null;

	/**
	 * 
	 */
	private File _rootFolder = null;

	/**
	 * 
	 */
	private File _buildFolder = null;

	/**
	 * 
	 */
	private File _soarsRootFolder = null;

	/**
	 * 
	 */
	private List<String> names = new ArrayList<>();

	/**
	 * @param dockerFilesetProperty 
	 */
	public DockerFilesetCreator(DockerFilesetProperty dockerFilesetProperty) {
		_dockerFilesetProperty = dockerFilesetProperty;
	}

	/**
	 * @return
	 */
	public boolean setup() {
		if ( !prepare())
			return false;

		if ( !create_fileset( null, "", 0, 0)) {
			FileUtility.delete( _parentFolder, true);
			return false;
		}

		return true;
	}

	/**
	 * @param experimentManager
	 * @return
	 */
	public boolean setup(ExperimentManager experimentManager) {
		if ( !prepare())
			return false;

		if ( !create_fileset( experimentManager)) {
			FileUtility.delete( _parentFolder, true);
			return false;
		}

		return true;
	}

	/**
	 * @return
	 */
	private boolean prepare() {
		// parentフォルダ作成
		File parentFolder = SoarsCommonTool.make_parent_directory();
		if ( null == parentFolder)
			return false;

		// rootフォルダ作成
		File rootFolder = new File( parentFolder, "soars"/*Constant._dockerRootFolderName*/);
		if ( !rootFolder.mkdirs()) {
			FileUtility.delete( parentFolder, true);
			return false;
		}

		// buildフォルダ作成
		File buildFolder = new File( rootFolder, "build"/*Constant._dockerBuildFolderName*/);
		if ( !buildFolder.mkdirs()) {
			FileUtility.delete( parentFolder, true);
			return false;
		}

		// soarsルートフォルダ作成
		File soarsRootFolder = new File( rootFolder, "soars"/*Constant._dockerRootFolderName*/);
		if ( !soarsRootFolder.mkdirs()) {
			FileUtility.delete( parentFolder, true);
			return false;
		}

		_parentFolder = parentFolder;
		_rootFolder = rootFolder;
		_buildFolder = buildFolder;
		_soarsRootFolder = soarsRootFolder;

		return true;
	}

	/**
	 * @param experimentManager
	 * @return
	 */
	private boolean create_fileset(ExperimentManager experimentManager) {
		for ( int number = 0; number < experimentManager._numberOfTimes; ++number) {
			Iterator iterator = experimentManager.entrySet().iterator();
			int index = 0;
			while ( iterator.hasNext()) {
				Object object = iterator.next();
				Map.Entry entry = ( Map.Entry)object;
				String experimentName = ( String)entry.getKey();
				InitialValueMap initialValueMap = ( InitialValueMap)entry.getValue();
				if ( !initialValueMap._export)
					continue;

				if ( !create_fileset( initialValueMap, experimentName + ( ( 2 > experimentManager._numberOfTimes) ? "" : ( "(" + String.valueOf( number + 1) + ")")), number, index))
					return false;

				++index;
			}
		}
		return true;
	}

	/**
	 * @param initialValueMap 
	 * @param experimentName 
	 * @param number 
	 * @param index 
	 * @return
	 */
	private boolean create_fileset(InitialValueMap initialValueMap, String experimentName, int number, int index) {
		String name = "soars" + String.valueOf( number) + "-" + String.valueOf( index);
		names.add( name);

		File soarsFolder = new File( _soarsRootFolder, name);
		if ( !soarsFolder.mkdirs())
			return false;

		if ( !make_docker_compose_file( soarsFolder, name))
			return false;

		if ( !make_property_file( soarsFolder, experimentName))
			return false;

		File binFolder = new File( soarsFolder, "bin");
		if ( !binFolder.mkdirs())
			return false;

		File userModuleDirectory = BasicEnvironment.get_instance().get_projectSubFoler( Constant._userModuleDirectoryName);
		if ( null == userModuleDirectory)
			return false;

		File userDataDirectory = LayerManager.get_instance().get_user_data_directory();
		if ( null == userDataDirectory || !userDataDirectory.exists())
			return false;

		File userRuleJarFilesFolder = LayerManager.get_instance().get_user_rule_jarFiles_directory();
		if ( null == userRuleJarFilesFolder) {
			String projectFoldername = BasicEnvironment.get_instance().get( BasicEnvironment._projectFolderDirectoryKey, "");
			if ( projectFoldername.equals( ""))
				return false;

			userRuleJarFilesFolder = new File( projectFoldername + Constant._userRuleJarFilesExternalRelativePathName);
		}

		String[][] paths = new String[][] {
			{ "../function/aadl.editor", "function/aadl.editor"},
			{ "../function/adapter", "function/adapter"},
			{ "../function/org.soars.gaming", "function/org.soars.gaming"},
			{ "../library/adapter", "library/adapter"},
			{ "../library/arbitrary", "library/arbitrary"},
			{ "../library/auxiliary", "library/auxiliary"},
			{ "../library/common", "library/common"},
			{ "../library/exalge", "library/exalge"},
			{ "../library/ga", "library/ga"},
			{ "../library/gis", "library/gis"},
			{ userModuleDirectory.getAbsolutePath(), "library/user"},
			{ userDataDirectory.getAbsolutePath(), "data"},
			{ userRuleJarFilesFolder.getAbsolutePath(), "gui/rule/library"}
		};

		for ( String[] path:paths) {
			if ( !FileUtility.copy_all( new File( path[ 0]), new File( binFolder, path[ 1])))
				return false;
		}

		File programFolder = new File( binFolder, "program");
		if ( !programFolder.mkdirs())
			return false;

		if ( !FileUtility.copy( new File( "ModelBuilder.jar"), new File( programFolder, "ModelBuilder.jar")))
			return false;

		File scriptFile = new File( programFolder, "script.sor");
		if ( !Exporter.execute_on_docker( scriptFile, initialValueMap, null, experimentName, Environment.get_instance().get( Environment._dockerLogToFileKey, "false").equals( "true")))
			return false;

		File shellFile = new File( programFolder, "soars.sh");
		if ( !create_shell_file_for_debug( shellFile))
			return false;

		return true;
	}

	/**
	 * @param soarsFolder
	 * @param string
	 * @return
	 */
	private boolean make_docker_compose_file(File soarsFolder, String name) {
		String text = name + ":\n";
		text += "  image: "+ ( _dockerFilesetProperty._specificDockerImageName ? _dockerFilesetProperty._dockerImageName : "soars") + "\n";
		text += "  volumes:\n";
		text += "    - ./:/srv/docker/soars\n";
		return FileUtility.write_text_to_file( new File( soarsFolder, "docker-compose.yml"/*Constant._dockerComposeFilename*/), text, "UTF-8");
	}

	/**
	 * @param soarsFolder
	 * @param experimentName
	 * @return
	 */
	private boolean make_property_file(File soarsFolder, String experimentName) {
		String text = "{\"name\":\"" + ( ( null == experimentName ) ? "" : experimentName) + "\"}\n";
		return FileUtility.write_text_to_file( new File( soarsFolder, "property.json"/*Constant._dockerComposeFilename*/), text, "UTF-8");
	}

	/**
	 * @param file
	 * @return
	 */
	private boolean create_shell_file_for_debug(File file) {
		String memorySize = Environment.get_instance().get( Environment._dockerMemorySizeKey, CommonEnvironment.get_instance().get_memory_size());
		memorySize = memorySize.equals( "0") ? "64" : memorySize;

		String script = "#!/bin/sh\n\n";
		script += "#For debug\n\n";
		script += "path=`pwd`\n\n";
		script += "java -DSOARS_HOME=$path -DSOARS_MEMORY_SIZE=" + memorySize + " -Xmx" + memorySize + "m -cp $path/ModelBuilder.jar:$path/../function/org.soars.gaming/org.soars.gaming.runner.jar main.MainConsole run=$path/script.sor\n";

		return write( file, script);
	}

	/**
	 * @param file
	 * @return
	 */
	public boolean execute(File file) {
		if ( !setup_buildFolder()) {
			FileUtility.delete( _parentFolder, true);
			return false;
		}

		if ( !setup_rootFolder()) {
			FileUtility.delete( _parentFolder, true);
			return false;
		}

		boolean result = ZipUtility.compress( file, _rootFolder, _parentFolder);
		FileUtility.delete( _parentFolder, true);
		return result;
	}

	/**
	 * @return
	 */
	private boolean setup_buildFolder() {
		File buildTemplateFolder = new File( _buildTemplateFolderName);
		if ( !buildTemplateFolder.exists() || !buildTemplateFolder.isDirectory())
			return false;

		if ( !make_Dockerfile( buildTemplateFolder))
			return false;

		if ( !make_build_shell_file( buildTemplateFolder))
			return false;

		if ( !make_start_shell_file( buildTemplateFolder))
			return false;

		return true;
	}

	/**
	 * @param buildTemplateFolder
	 * @return
	 */
	private boolean make_Dockerfile(File buildTemplateFolder) {
		File file = new File( buildTemplateFolder, "Dockerfile1");
		if ( !file.exists() || !file.isFile())
			return false;

		String script = FileUtility.read_text_from_file( file);
		if ( null == script)
			return false;

		file = new File( buildTemplateFolder, _dockerFilesetProperty._specificUser ? "Dockerfile2-user" : "Dockerfile2-root");
		if ( !file.exists() || !file.isFile())
			return false;

		String text = FileUtility.read_text_from_file( file);
		if ( null == text)
			return false;

		if ( _dockerFilesetProperty._specificUser) {
			text = text.replaceAll( "\\$id", _dockerFilesetProperty._userId);
			text = text.replaceAll( "\\$username", _dockerFilesetProperty._username);
			text = text.replaceAll( "\\$password", _dockerFilesetProperty._password);
		}

		script += text;

		return FileUtility.write_text_to_file( new File( _buildFolder, "Dockerfile"), script, "UTF-8");
	}

	/**
	 * @param buildTemplateFolder
	 * @return
	 */
	private boolean make_build_shell_file(File buildTemplateFolder) {
		File file = new File( buildTemplateFolder, "make.sh");
		if ( !file.exists() || !file.isFile())
			return false;

		String script = FileUtility.read_text_from_file( file);
		if ( null == script)
			return false;

		script = script.replaceAll( "\\$docker-name", _dockerFilesetProperty._specificDockerImageName ? _dockerFilesetProperty._dockerImageName : "soars");

		return write( new File( _buildFolder, "make.sh"), script);
	}

	/**
	 * @param buildTemplateFolder
	 * @return
	 */
	private boolean make_start_shell_file(File buildTemplateFolder) {
		File file = new File( buildTemplateFolder, "start");
		if ( !file.exists() || !file.isFile())
			return false;

		String script = FileUtility.read_text_from_file( file);
		if ( null == script)
			return false;

		String memorySize = Environment.get_instance().get( Environment._dockerMemorySizeKey, CommonEnvironment.get_instance().get_memory_size());
		memorySize = memorySize.equals( "0") ? "64" : memorySize;
		script = script.replaceAll( "\\$memorySize", memorySize);

		return FileUtility.write_text_to_file( new File( _buildFolder, "start"), script, "UTF-8");
	}

	/**
	 * @return
	 */
	private boolean setup_rootFolder() {
		File templateFolder = new File( _templateFolderName);
		String[] filenames = new String[] { "setup.py", "generator.py"};
		for ( String filename:filenames) {
			File src = new File( templateFolder, filename);
			File dest = new File( _rootFolder, filename);
			try {
				Files.copy( src.toPath(), dest.toPath());
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		if ( !make_setup_python_file())
			return false;

		if ( !make_start_docker_shell_file())
			return false;

		if ( !make_terminate_docker_shell_file())
			return false;

		return true;
	}

	/**
	 * @return
	 */
	private boolean make_setup_python_file() {
		String text = "#!/usr/bin/env python\n";
		text += "# -*- coding: utf-8 -*-\n\n";

		text += "import os\n\n";

		text += "os.system( 'chmod +x build/make.sh')\n";
		text += "os.system( 'chmod +x *.sh')\n";

		return FileUtility.write_text_to_file( new File( _rootFolder, "setup.py"/*Constant._dockerSetupPythonFilename*/), text, "UTF-8");
	}

	/**
	 * @return
	 */
	private boolean make_start_docker_shell_file() {
		String text = "#!/bin/sh\n\n";

		text += "cd build\n";
		text += "./make.sh\n";
		text += "cd ..\n\n";

		for ( int i = 0; i < names.size(); ++i)
			text += "sudo docker-compose -f soars/" + names.get( i) + "/docker-compose.yml up -d " + ( ( 0 == i) ? "2>" : "2>>") + " temp.txt\n";

		text += "\npython generator.py temp.txt remove.sh\n\n";

		text += "rm temp.txt\n\n";

		text += "chmod +x remove.sh\n";

		return write( new File( _rootFolder, "start.sh"/*Constant._dockerStartDockerShellFilename*/), text);
	}

	/**
	 * @return
	 */
	private boolean make_terminate_docker_shell_file() {
		String text = "#!/bin/sh\n\n";

		for ( int i = 0; i < names.size(); ++i)
			text += "sudo docker-compose -f soars/" + names.get( i) + "/docker-compose.yml stop\n";

		text += "\n./remove.sh\n";

		return write( new File( _rootFolder, "terminate.sh"/*Constant._dockerTerminateDockerShellFilename*/), text);
	}

	/**
	 * @param file
	 * @param text
	 * @return
	 */
	private boolean write(File file, String text) {
		if ( !FileUtility.write_text_to_file( file, text, "UTF-8"))
			return false;

		if  ( 0 <= System.getProperty( "os.name").toLowerCase().indexOf( "windows"))
			return true;

		try {
			Files.setPosixFilePermissions( file.toPath(), PosixFilePermissions.fromString( "rwxrwxr-x"));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
