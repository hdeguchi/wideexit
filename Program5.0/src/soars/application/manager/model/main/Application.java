/**
 * 
 */
package soars.application.manager.model.main;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import soars.application.manager.model.main.startup.ProjectFolderDlg;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.swing.window.SplashWindow;

/**
 * @author kurata
 *
 */
public class Application {

	/**
	 * @return
	 */
	public boolean init_instance() {
		if ( !SoarsCommonTool.setup_look_and_feel())
			return false;

//		JOptionPane.showMessageDialog( null,
//			"test",
//			ResourceManager.get_instance().get( "application.title"),
//			JOptionPane.ERROR_MESSAGE);

		MainFrame mainFrame = MainFrame.get_instance();

		String propertyDirectoryName = System.getProperty( Constant._soarsProperty);
		if ( null != propertyDirectoryName && !propertyDirectoryName.equals( "")) {
			// 動作環境設定ファイルの場所が指定されていたら通常通りの起動
			if ( !mainFrame.create( true))
				return false;
		} else {
			// 動作環境設定ファイルの場所が指定されていない場合、プロジェクトフォルダ、動作環境設定ファイルの場所の選択及びこれらの設定をデフォルトとする(起動時に確認しない)かどうか？を指定させてから起動する
			if ( !SplashWindow.execute( Constant._resourceDirectory + "/image/splash/splash.gif", true))
				return false;

			ProjectFolderDlg projectFolderDlg = new ProjectFolderDlg( null, ResourceManager.get_instance().get( "application.title"), true, ResourceManager.get_instance().get( "project.folder.dialog.exit"), false);
			if ( !projectFolderDlg.do_modal( SplashWindow.get_instance())) {
				SplashWindow.terminate();
				return false;
			}

			if ( !mainFrame.create( false)) {
				SplashWindow.terminate();
				return false;
			}

			SplashWindow.terminate();
		}

		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String homeDirectoryName = System.getProperty( Constant._soarsHome);
			if ( null == homeDirectoryName || homeDirectoryName.equals( "")) {
				File homeDirectory = new File( "");
				if ( null == homeDirectory)
					System.exit( 1);

				System.setProperty( Constant._soarsHome, homeDirectory.getAbsolutePath());
			}

			String propertyDirectoryName = System.getProperty( Constant._soarsProperty);
//			if ( null == propertyDirectoryName || propertyDirectoryName.equals( "")) {
//				File propertyDirectory = SoarsCommonTool.get_default_property_directory();
//				if ( null == propertyDirectory)
//					System.exit( 1);
//
//				System.setProperty( Constant._soarsProperty, propertyDirectory.getAbsolutePath());
//			}

			boolean locale = false;
			String initialData = "";
			boolean removeInitialData = false;
			for ( int i = 0; i < args.length; ++i) {
				if ( args[ i].equals( "-language") && ( i + 1 < args.length) && !args[ i + 1].equals( "")) {
					JFrame.setDefaultLookAndFeelDecorated( false);
					//JFrame.setDefaultLookAndFeelDecorated( true);
					Locale.setDefault( new Locale( args[ i + 1]));
					if ( null != propertyDirectoryName && !propertyDirectoryName.equals( ""))
						CommonEnvironment.get_instance().set( CommonEnvironment._localeKey, args[ i + 1]);
					locale = true;
				}
			}

			if ( !locale) {
				JFrame.setDefaultLookAndFeelDecorated( false);
				//JFrame.setDefaultLookAndFeelDecorated( true);
				if ( null != propertyDirectoryName && !propertyDirectoryName.equals( ""))
					Locale.setDefault( new Locale( CommonEnvironment.get_instance().get( CommonEnvironment._localeKey, Locale.getDefault().getLanguage())));
			}

			Application application = new Application();
			if ( !application.init_instance())
				System.exit( 1);

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
