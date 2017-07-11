/**
 * 
 */
package soars.application.manager.model.executor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import soars.application.manager.model.main.Constant;
import soars.application.manager.model.main.MainFrame;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.stream.StreamPumper;

/**
 * @author kurata
 *
 */
public class ApplicationBuilder extends Executor {

	/**
	 * @param file
	 * @return
	 */
	public static boolean start(File file) {
		MainFrame.get_instance().store();

		String homeDirectoryName = System.getProperty( Constant._soarsHome);
		String propertyDirectoryName = System.getProperty( Constant._soarsProperty);
		String soars_properties = System.getProperty( Constant._soarsProperties);

		String memorySize = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");

		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant._macJava);
			//if ( System.getProperty( Constant._systemDefaultFileEncoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
			list.add( "-D" + Constant._systemDefaultFileEncoding + "=" + System.getProperty( Constant._systemDefaultFileEncoding, ""));
			list.add( "-X" + Constant._macScreenMenuName + "=SOARS Application Builder");
			list.add( "-X" + Constant._macIconFilename + "=../resource/icon/application/builder/application/icon.png");
			list.add( "-D" + Constant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

		list.add( "-D" + Constant._soarsHome + "=" + homeDirectoryName);
		list.add( "-D" + Constant._soarsProperty + "=" + propertyDirectoryName);
		list.add( "-D" + Constant._soarsProperties + "=" + soars_properties);
		if ( !memorySize.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memorySize);
			list.add( "-Xmx" + memorySize + "m");
		}
		list.add( "-cp");
		list.add( homeDirectoryName + "/../" + ( is_visualShell4( file) ? Constant._applicationBuilder4JarFilename : Constant._applicationBuilderJarFilename));
		list.add( Constant._applicationBuilderMainClassname);
		list.add( "-language");
		list.add( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, Locale.getDefault().getLanguage()));
		list.add( "-soars");
		list.add( file.getAbsolutePath());

		String[] cmdarray = list.toArray( new String[ 0]);

		debug( "Application Builder", osname, System.getProperty( "os.version"), cmdarray);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec(
				cmdarray,
				null,
				new File( homeDirectoryName));
			new StreamPumper( process.getInputStream(), false).start();
			new StreamPumper( process.getErrorStream(), false).start();
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		}

		return true;
	}
}
