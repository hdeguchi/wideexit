/**
 * 
 */
package soars.application.soars.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;

import soars.common.soars.environment.BasicEnvironment;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 *
 */
public class Application {

	/**
	 * @return
	 */
	public boolean init_instance() {
		File currentDirectory = new File( "");
		if ( null == currentDirectory)
			return false;

		File homeDirectory = new File( currentDirectory.getAbsolutePath() + Constant._homeDirectory);
		System.setProperty( Constant._soarsHome, homeDirectory.getAbsolutePath());

		File propertyDirectory = SoarsCommonTool.get_default_property_directory();
		if ( null == propertyDirectory)
			return false;

		if ( !write_test( propertyDirectory))
			return false;

		// ここでBasicEnvironment(soars/user/.soars/basic/environment/environment.properties)を調べる
		// プロジェクトフォルダが指定されていなければModelManager起動時にプロジェクトフォルダ及び動作環境設定ファイルの場所をユーザに指定させる
		// プロジェクトフォルダが指定されている場合は動作環境設定ファイルの場所を取得して環境変数にセット指定する
		String projectFolderName = BasicEnvironment.get_instance().get( BasicEnvironment._projectFolderDirectoryKey, "");
		if ( !projectFolderName.equals( "")) {
			// プロジェクトフォルダが指定されている場合
			File projectFolder = new File( projectFolderName);
			if ( !projectFolder.exists()) {
				if ( !projectFolder.mkdirs())
					return false;
			} else {
				if ( !projectFolder.isDirectory())
					return false;
			}

			// プロジェクトフォルダ内のサブフォルダを作成
			if ( !BasicEnvironment.get_instance().create_projectSubFolers( projectFolder))
				return false;

			// 動作環境設定ファイルの場所を取得して環境変数にセット指定する
			if ( !BasicEnvironment.get_instance().get( BasicEnvironment._usePropertyInProjectFolderKey, "").equals( "false")) {
				propertyDirectory = new File( projectFolder, ".soars");
				if ( !propertyDirectory.exists()) {
					if ( !propertyDirectory.mkdirs())
						return false;
				} else {
					if ( !propertyDirectory.isDirectory())
						return false;
				}

				if ( !write_test( propertyDirectory))
					return false;
			}

			System.setProperty( Constant._soarsProperty, propertyDirectory.getAbsolutePath());
		}

		// こちらはSoarsCommonEnvironment(soars/bin/resource/common/soars.properties)なのでこのままで良い
		File soarsProperties = SoarsCommonEnvironment.get_instance().get();
		if ( null == soarsProperties)
			return false;

		System.setProperty( Constant._soarsProperties, soarsProperties.getAbsolutePath());

		try {
			Process process = ( Process)Runtime.getRuntime().exec( get_cmdarray(), null, homeDirectory);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param propertyDirectory
	 * @return
	 */
	private boolean write_test(File propertyDirectory) {
		try {
			File file = File.createTempFile( "soars", "test", propertyDirectory);
			if ( null == file)
				return false;

			file.delete();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	private static String[] get_cmdarray() {
		String homeDirectoryName = System.getProperty( Constant._soarsHome);
		String propertyDirectoryName = System.getProperty( Constant._soarsProperty, "");
		String soarsProperties = System.getProperty( Constant._soarsProperties);

		String memorySize = SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._initialMemorySizeKey, "0");
		//String memorySize = propertyDirectoryName.equals( "") ? "0" : CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");

		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant.get_mac_java_command());
			//if ( System.getProperty( Constant._systemDefaultFileEncoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
			list.add( "-D" + Constant._systemDefaultFileEncoding + "=" + System.getProperty( Constant._systemDefaultFileEncoding, ""));
			list.add( "-X" + Constant._macScreenMenuName + "=SOARS Model Manager");
			//list.add( "-D" + Constant._macScreenMenuName + "=SOARS Model Manager");
			list.add( "-X" + Constant._macIconFilename + "=../resource/icon/application/manager/soars/icon.png");
			list.add( "-D" + Constant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

		list.add( "-D" + Constant._soarsHome + "=" + homeDirectoryName);
		if ( !propertyDirectoryName.equals( ""))
			list.add( "-D" + Constant._soarsProperty + "=" + propertyDirectoryName);
		list.add( "-D" + Constant._soarsProperties + "=" + soarsProperties);
		if ( !memorySize.equals( "0")) {
//			list.add( "-D" + Constant._soarsMemorySize + "=" + memorySize);
			list.add( "-Xmx" + memorySize + "m");
		}
//		list.add( "-Xmx" + Constant._defaultMemorySize + "m");
		list.add( "-jar");
		list.add( homeDirectoryName + "/" + Constant._soarsManagerJarFilename);
		if ( !propertyDirectoryName.equals( "")) {
			list.add( "-language");
			list.add( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, Locale.getDefault().getLanguage()));
		}

		String[] cmdarray = list.toArray( new String[ 0]);

		debug( "Model Manager", osname, System.getProperty( "os.version"), cmdarray);

		return cmdarray;
	}

	/**
	 * @param type
	 * @param osname
	 * @param osversion
	 * @param cmdarray
	 */
	protected static void debug(String type, String osname, String osversion, String[] cmdarray) {
		String text = ""; 
		text += ( "Type : " + type + Constant._lineSeparator);
		text += ( "OS : " + osname + " [" + osversion + "]" + Constant._lineSeparator);
		for ( int i = 0; i < cmdarray.length; ++i)
			text += ( "Parameter : " + cmdarray[ i] + Constant._lineSeparator);

		Clipboard.set( text);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Application application = new Application();
			if ( !application.init_instance()) {
				JOptionPane.showMessageDialog( null,
					ResourceManager.get_instance().get( "soars.error.messsage"),
					ResourceManager.get_instance().get( "application.title"),
					JOptionPane.ERROR_MESSAGE);
				System.exit( 1);
			}
		} catch (Throwable ex) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter( stringWriter);
			ex.printStackTrace( printWriter);
			JOptionPane.showMessageDialog( null,
				stringWriter.toString(),
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
		}
	}
}
