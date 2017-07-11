/**
 * 
 */
package soars.tool.visualshell.exporter.test;

import java.io.File;

import soars.application.visualshell.main.Constant;
import soars.common.soars.tool.SoarsCommonTool;
import soars.tool.visualshell.exporter.export.Exporter;

/**
 * @author kurata
 *
 */
public class Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String homeDirectoryName = System.getProperty( Constant._soarsHome);
		if ( null == homeDirectoryName || homeDirectoryName.equals( "")) {
			File homeDirectory = new File( "");
			if ( null == homeDirectory)
				System.exit( 1);

			System.setProperty( Constant._soarsHome, homeDirectory.getAbsolutePath());
		}

		String propertyDirectoryName = System.getProperty( Constant._soarsProperty);
		if ( null == propertyDirectoryName || propertyDirectoryName.equals( "")) {
			File propertyDirectory = SoarsCommonTool.get_default_property_directory();
			if ( null == propertyDirectory)
				System.exit( 1);

			System.setProperty( Constant._soarsProperty, propertyDirectory.getAbsolutePath());
		}

		switch ( args.length) {
			case 0:
				return;
			case 1:
				if ( !Exporter.run( args[ 0]))
					System.exit( 1);

				break;
			default:
				if ( !Exporter.run( args[ 0], args[ 1]))
					System.exit( 1);

			break;
		}
	}
}
