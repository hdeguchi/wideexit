/*
 * Created on 2006/07/31
 */
package soars.application.visualshell.file.importer.experiment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.MainFrame;
import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.tool.common.Tool;

/**
 * Imports the experiment table data from the file in CSV format.
 * @author kurata / SOARS project
 */
public class ExperimentTableImporter {

	/**
	 * 
	 */
	private List<String> _aliases = null;

	/**
	 * 
	 */
	private List<String> _comments = null;

	/**
	 * 
	 */
	private List<List<String>> _tableData = null;

	/**
	 * 
	 */
	private boolean _exportExists = true;

	/**
	 * Creates this object.
	 */
	public ExperimentTableImporter() {
		super();
	}

	/**
	 * Returns true for importing the experiment table data from the specified file in CSV format successfully.
	 * @param file the specified file
	 * @return true for importing the experiment table data from the specified file in CSV format successfully
	 */
	public boolean load(File file) {
		BufferedReader bufferedReader;
		try {
			if ( 0 <= System.getProperty( "os.name").indexOf( "Mac")
				&& !System.getProperty( Constant._systemDefaultFileEncoding, "").equals( ""))
				bufferedReader = new BufferedReader( new InputStreamReader( new FileInputStream( file),
					System.getProperty( Constant._systemDefaultFileEncoding, "")));
			else
				bufferedReader = new BufferedReader( new InputStreamReader( new FileInputStream( file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}

		int number = 1;
		int status = 0;
		while ( true) {
			String line = null;
			try {
				line = bufferedReader.readLine();
			} catch (IOException e1) {
				try {
					bufferedReader.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				e1.printStackTrace();
				return false;
			}

			if ( null == line)
				break;

			if ( 0 == line.length()) {
				++number;
				continue;
			}

			switch ( status) {
				case 0:
					if ( !line.startsWith( Constant._experimentTableDataIdentifier)) {
						try {
							bufferedReader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}

						on_invalid_line_error( number);
						return false;
					}

					++number;
					status = 1;
					break;
				case 1:
					if ( !get_aliases( line, number)) {
						try {
							bufferedReader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}

						on_invalid_line_error( number);
						return false;
					}

					++number;
					status = 2;
					break;
				case 2:
					if ( !get_comments( line, number)) {
						try {
							bufferedReader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}

						on_invalid_line_error( number);
						return false;
					}

					status = 3;

					if ( is_comments( line)) {
						++number;
						break;
					}
				case 3:
					if ( !get_table_data( line, number)) {
						try {
							bufferedReader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}

						on_invalid_line_error( number);
						return false;
					}

					++number;
					break;
			}
		}

		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		if ( 3 != status || _tableData.isEmpty())
			return false;

		return true;
	}

	/**
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean get_aliases(String line, int number) {
		_exportExists = line.startsWith( "\t\t\t");

		String[] words = line.split( "\t");
		if ( null == words || ( !_exportExists && 3 > words.length)
			|| ( _exportExists && 4 > words.length))
			return false;

		_aliases = new ArrayList<String>();

		int start = ( !_exportExists ? 2 : 3);
		for ( int i = start; i < words.length; ++i) {
			if ( !words[ i].startsWith( "$"))
				return false;

			_aliases.add( words[ i]);
		}

		return true;
	}

	/**
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean get_comments(String line, int number) {
		_comments = new ArrayList<String>();
		int size = ( _aliases.size() + ( _exportExists ? 3 : 2));
		if ( !is_comments( line)) {
			for ( int i = 0; i < size; ++i)
				_comments.add( "");

			return true;
		}

		String[] words = Tool.split( line, '\t');
		if ( null == words || 1 > words.length)
			return false;

		for ( int i = 0; i < size; ++i)
			_comments.add( i < words.length ? words[ i] : "");

		return true;
	}

	/**
	 * @param line
	 * @return
	 */
	private boolean is_comments(String line) {
		return ( ( !_exportExists && line.startsWith( "\t\t"))
			|| ( _exportExists && line.startsWith( "\t\t\t")));
	}

	/**
	 * @param line
	 * @param number
	 * @return
	 */
	private boolean get_table_data(String line, int number) {
		String[] words = line.split( "\t");
		if ( null == words || 1 > words.length)
			return false;

		if ( _exportExists)
			words[ 0] = words[ 0].toLowerCase();

		int size = ( _aliases.size() + ( _exportExists ? 3 : 2));
		List<String> row = new ArrayList<String>();
		for ( int i = 0; i < size; ++i)
			row.add( i < words.length ? words[ i] : "");

		if ( null == _tableData)
			_tableData = new ArrayList<List<String>>();

		_tableData.add( row);

		return true;
	}

	/**
	 * Returns true if the flag which indicates whether to export the script for the experiment exists.
	 * @return true if the flag which indicates whether to export the script for the experiment exists
	 */
	public boolean export_exists() {
		return _exportExists;
	}

	/**
	 * Returns the array of the words which start with '$'
	 * @return the array of the words which start with '$'
	 */
	public String[] get_aliases() {
		if ( null == _aliases || _aliases.isEmpty())
			return null;

		String[] aliases = new String[ _aliases.size()];
		for ( int i = 0; i < _aliases.size(); ++i)
			aliases[ i] = ( String)_aliases.get( i);

		return aliases;
	}

	/**
	 * Returns the array of the comments explaining about the experiments.
	 * @return the array of the comments explaining about the experiments
	 */
	public String[] get_comments() {
		if ( null == _comments || _comments.isEmpty())
			return null;

		String[] comments = new String[ _comments.size()];
		for ( int i = 0; i < _comments.size(); ++i)
			comments[ i] = ( String)_comments.get( i);

		return comments;
	}

	/**
	 * Returns the array of the values for the experiments.
	 * @return the array of the values for the experiments
	 */
	public String[][] get_table_data() {
		if ( null == _tableData || _tableData.isEmpty())
			return null;

		String[][] tableData = new String[ _tableData.size()][ _aliases.size() + ( !_exportExists ? 2 : 3)];
		for ( int i = 0; i < tableData.length; ++i) {
			for ( int j = 0; j < tableData[ i].length; ++j)
				tableData[ i][ j] = "";
		}

		for ( int i = 0; i < _tableData.size(); ++i) {
			List<String> row = _tableData.get( i);
			for ( int j = 0; j < row.size(); ++j)
				tableData[ i][ j] = row.get( j);
		}

		return tableData;
	}

	/**
	 * @param number
	 */
	private void on_invalid_line_error(int number) {
		JOptionPane.showMessageDialog(
			MainFrame.get_instance(),
			ResourceManager.get_instance().get( "experiment.data.invalid.line.error.message") + " : line number " + number,
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.ERROR_MESSAGE);
	}
}
