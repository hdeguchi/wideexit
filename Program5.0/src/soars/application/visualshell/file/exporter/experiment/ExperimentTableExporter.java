/*
 * Created on 2006/07/31
 */
package soars.application.visualshell.file.exporter.experiment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import soars.application.visualshell.main.Constant;

/**
 * Exports the experiment table data to the file in CSV format.
 * @author kurata / SOARS project
 */
public class ExperimentTableExporter {

	/**
	 * Returns true for exporting the experiment table data to the specified file in CSV format successfully.
	 * @param file the specified file
	 * @param aliases the array of the words which start with '$'
	 * @param comments the array of the comments explaining about the experiments
	 * @param tableData the array of the values for the experiments
	 * @return true for exporting the experiment table data to the specified file in CSV format successfully
	 */
	public static boolean execute(File file, String[] aliases, String[] comments, String[][] tableData) {
		String script = get_script( aliases, comments, tableData);

		try {
			OutputStreamWriter outputStreamWriter;
			if ( 0 <= System.getProperty( "os.name").indexOf( "Mac")
				&& !System.getProperty( Constant._systemDefaultFileEncoding, "").equals( ""))
				outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file),
					System.getProperty( Constant._systemDefaultFileEncoding, ""));
			else
				outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file));

			outputStreamWriter.write( script);
			outputStreamWriter.flush();
			outputStreamWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param aliases
	 * @param comments
	 * @param tableData
	 * @return
	 */
	private static String get_script(String[] aliases, String[] comments, String[][] tableData) {
		String script = ( Constant._experimentTableDataIdentifier + Constant._lineSeparator);


		script += "\t\t";

		for ( int i = 0; i < aliases.length; ++i)
			script += ( "\t" + aliases[ i]);

		script += Constant._lineSeparator;


		for ( int i = 0; i < comments.length; ++i)
			script += ( ( ( 0 == i) ? "" : "\t") + comments[ i]);

		script += Constant._lineSeparator;


		for ( int i = 0; i < tableData.length; ++i) {
			for ( int j = 0; j < tableData[ i].length; ++j)
				script += ( ( ( 0 == j) ? "" : "\t") + tableData[ i][ j]);

			script += Constant._lineSeparator;
		}

		return script;
	}
}
