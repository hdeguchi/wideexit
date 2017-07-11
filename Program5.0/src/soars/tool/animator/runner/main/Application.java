/**
 * 
 */
package soars.tool.animator.runner.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONException;

import soars.common.soars.constant.CommonConstant;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.soars.tool.SoarsCommonTool;
import soars.tool.animator.runner.executor.launcher.AnimatorLauncher;

/**
 * @author kurata
 *
 */
public class Application {

	/**
	 * 
	 */
	public Application() {
		super();
	}

	/**
	 * @return
	 */
	private String setup() {
		File property_directory = SoarsCommonTool.get_system_property_directory();
		if ( null == property_directory)
			return null;

		if ( !write_test( property_directory))
			return null;

		System.setProperty( CommonConstant._soarsProperty, property_directory.getAbsolutePath());

		File current_directory = new File( "");
		if ( null == current_directory)
			return null;

		File home_directory = new File( current_directory.getAbsolutePath() + "/" + CommonConstant._animatorRunnerHomeDirectory);
		System.setProperty( CommonConstant._soarsHome, home_directory.getAbsolutePath());

		JSONArray jsonArray = load();
		if ( null == jsonArray)
			return null;

		String language = "";
		try {
			language = jsonArray.getString( 0);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		JFrame.setDefaultLookAndFeelDecorated( false);
		Locale.setDefault( new Locale( language));
		CommonEnvironment.get_instance().set( CommonEnvironment._localeKey, language);

		return language;
	}

	/**
	 * @return
	 */
	private JSONArray load() {
		String home_directory = System.getProperty( CommonConstant._soarsHome);
		if ( null == home_directory)
			return null;

		File file = new File( home_directory + "/../" + CommonConstant._animatorRunnerDataDirectory + "/" + CommonConstant._animatorRunnerParameterFilename);
		if ( !file.isFile() || !file.canRead())
			return null;

		try {
			BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( new FileInputStream( file), "UTF-8"));
			String line = bufferedReader.readLine();
			if ( null == line)
				return null;

			bufferedReader.close();

			return new JSONArray( line);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param property_directory
	 * @return
	 */
	private boolean write_test(File property_directory) {
		try {
			File file = File.createTempFile( "soars", "test", property_directory);
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
	 * @param args
	 */
	public static void main(String[] args) {
		Application application = new Application();
		String language = application.setup();
		if ( null == language) {
			JOptionPane.showMessageDialog( null, "Could not setup!", "run.jar", JOptionPane.ERROR_MESSAGE);
			System.exit( 1);
		}

		if ( !AnimatorLauncher.run( language)) {
			JOptionPane.showMessageDialog( null, "Could not launch!", "run.jar", JOptionPane.ERROR_MESSAGE);
			System.exit( 1);
		}

		System.exit( 0);
	}
}
