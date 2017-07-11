/**
 * 
 */
package soars.library.auxiliary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.soars.exalge.util.ExalgeFactory;
import org.soars.exalge.util.ExalgeMath;

import soars.common.utility.tool.common.Tool;
import util.Invoker;
import env.Agent;
import env.EquippedObject;
import env.Spot;
import exalge2.ExBase;
import exalge2.Exalge;

/**
 * @author kurata
 *
 */
public class CSVReader {

	/**
	 * 
	 */
	private String _state = "first";

	/**
	 * 
	 */
	private List<String> _commands = new ArrayList<String>();

	/**
	 * 
	 */
	private List<String> _variables = new ArrayList<String>();

	/**
	 * @param path
	 * @return
	 */
	public static boolean read(String path) {
		CSVReader csvReader = new CSVReader();
		return csvReader.read( new File( path));
	}

	/**
	 * 
	 */
	public CSVReader() {
		super();
	}

	/**
	 * @param file
	 * @return
	 */
	private boolean read(File file) {
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader( new InputStreamReader( new FileInputStream( file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		int row = 1;
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
				++row;
				continue;
			}

			if ( !read( line, row)) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}
		}

		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * @param line
	 * @param row
	 * @return
	 */
	private boolean read(String line, int row) {
		String[] words = Tool.split( line, '\t');
		if ( null == words)
			return false;

		if ( _state.equals( "first")) {
			if ( !read_command( words, row)) {
				on_error( "read_command");
				return false;
			}
		} else if ( _state.equals( "second")) {
			if ( !read_variable( words, row)) {
				on_error( "read_variable");
				return false;
			}
		} else if ( _state.equals( "third")) {
			if ( !read_value( words, row))
				return false;
		} else
			return false;

		return true;
	}

	/**
	 * @param words
	 * @param row
	 * @return
	 */
	private boolean read_command(String[] words, int row) {
		if ( 2 > words.length)
			return false;

		for ( int i = 1; i < words.length; ++i) {
			if ( !words[ i].equals( "addExalge"))
				return false;

			_commands.add( words[ i]);
		}

		_state = "second";

		return true;
	}

	/**
	 * @param words
	 * @param row
	 * @return
	 */
	private boolean read_variable(String[] words, int row) {
		if ( _commands.size() != words.length - 1)
			return false;

		for ( int i = 1; i < words.length; ++i)
			_variables.add( words[ i]);

		_state = "third";

		return true;
	}

	/**
	 * @param words
	 * @param row
	 * @return
	 */
	private boolean read_value(String[] words, int row) {
		if ( _commands.size() < words.length - 1) {
			on_error( "read_variable : 1");
			return false;
		}

		EquippedObject equippedObject = null;
		for ( int i = 0; i < words.length; ++i) {
			if ( 0 == i) {
				Agent agent = Agent.forName( words[ 0]);
				if ( null != agent)
					equippedObject = agent;
				else {
					Spot spot = Spot.forName( words[ 0]);
					if ( null != spot)
						equippedObject = spot;
					else {
						on_error( "read_variable : 2");
						return false;
					}
				}
			} else {
				if ( words[ i].equals( ""))
					continue;

				if ( null == equippedObject) {
					on_error( "read_variable : 3");
					return false;
				}

				Object object = equippedObject.getEquip( _variables.get( i - 1));
				if ( null == object || !( object instanceof Invoker)) {
					on_error( "read_variable : 4");
					return false;
				}

				Invoker invoker = ( Invoker)object;

				object = invoker.getInstance();
				if ( null == object)
					object = ExalgeFactory.newExalge();
				else if ( !( object instanceof Exalge)) {
					on_error( "read_variable : 5");
					return false;
				}

				Exalge exalge = ( Exalge)object;

				String[] parameters = get_parameters( words[ i]);
				if ( null == parameters) {
					on_error( "read_variable : 6");
					return false;
				}

				exalge = ExalgeMath.plus( exalge, parameters[ 0], toBaseOneKeyString( parameters[ 1]));
				if ( null == exalge) {
					on_error( "read_variable : 7");
					return false;
				}

				invoker.setInstance( exalge);
			}
		}

		return true;
	}

	/**
	 * @param value
	 * @return
	 */
	private String[] get_parameters(String value) {
		boolean hat = ( 0 <= value.indexOf( '^'));
		String[] parameters = value.split( hat ? "\\^" : "<");
		if ( null == parameters || 2 != parameters.length)
			return null;

		parameters[ 1] = ( ( hat ? "^" : "<") + parameters[ 1]);

		return parameters;
	}

	/**
	 * @param text
	 * @return
	 */
	private String toBaseOneKeyString(String text) {
		ExBase exBase = ExalgeFactory.newExBaseByLiteral( text);
		if ( null == exBase) {
			exBase = ExalgeFactory.newExBaseByOneKeyString( text);
			if ( null == exBase)
				return null;
		}

		String baseOneKeyString = exBase.key();
		if ( null == baseOneKeyString)
			return null;

		while ( 0 <= baseOneKeyString.indexOf( "-#-"))
			baseOneKeyString = baseOneKeyString.replace( "-#-", "--");

		return baseOneKeyString;
	}

	/**
	 * @param message
	 */
	private void on_error(String message) {
		JOptionPane.showMessageDialog( null, message, "Exchange Algebra CSV Reader", JOptionPane.ERROR_MESSAGE);
	}
}
