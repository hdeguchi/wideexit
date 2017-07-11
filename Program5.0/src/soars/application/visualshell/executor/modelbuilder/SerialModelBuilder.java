/*
 * Created on 2006/05/10
 */
package soars.application.visualshell.executor.modelbuilder;

import java.io.File;
import java.util.Vector;

import soars.application.visualshell.common.file.ScriptFile;
import soars.application.visualshell.executor.monitor.MonitorPropertyPage;
import soars.application.visualshell.main.Constant;

/**
 * The class which runs the ModelBuilders in serial.
 * @author kurata / SOARS project
 */
public class SerialModelBuilder implements Runnable {

	/**
	 * 
	 */
	private ScriptFile[] _scriptFiles = null;

	/**
	 * 
	 */
	private MonitorPropertyPage _monitorPropertyPage = null;

	/**
	 * 
	 */
	static protected Vector<Thread> _threads = new Vector<Thread>();

	/**
	 * Returns true if this thread is alive.
	 * @return true if this thread is alive
	 */
	public static boolean isAlive() {
		for ( int i = 0; i < _threads.size(); ++i) {
			Thread thread = ( Thread)_threads.get( i);
			if ( thread.isAlive())
				return true;
		}
		return false;
	}

	/**
	 * Runs ModelBuilders in serial with the specified data.
	 * @param scriptFiles the specified script files
	 * @param monitorPropertyPage the specified monitor component to display the log output of the ModelBuilder
	 */
	public static void execute(ScriptFile[] scriptFiles, MonitorPropertyPage monitorPropertyPage) {
		SerialModelBuilder serialModelBuilder = new SerialModelBuilder( scriptFiles, monitorPropertyPage);
		Thread thread = new Thread( serialModelBuilder);
		monitorPropertyPage._thread = thread;
		//thread.setName();
		thread.start();
		_threads.add( thread);
	}

	/**
	 * Creates the instance of SerialModelBuilder class with the specified data.
	 * @param script_file the specified script files
	 * @param monitorPropertyPage the specified monitor component to display the log output of the ModelBuilder
	 */
	public SerialModelBuilder(ScriptFile[] scriptFiles, MonitorPropertyPage monitorPropertyPage) {
		super();
		_scriptFiles = scriptFiles;
		_monitorPropertyPage = monitorPropertyPage;
	}

	/* (Non Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		String currentDirectoryName = System.getProperty( Constant._soarsHome);
		File currentDirectory = new File( currentDirectoryName);
		if ( null == currentDirectory)
			return;

		ModelBuilder.run( _scriptFiles, _monitorPropertyPage, currentDirectory, currentDirectoryName);
		synchronized( _monitorPropertyPage._lockProcess) {
			_monitorPropertyPage._process = null;
		}
		for ( ScriptFile scriptFile:_scriptFiles)
			scriptFile.clear_work_directory();
	}
}
