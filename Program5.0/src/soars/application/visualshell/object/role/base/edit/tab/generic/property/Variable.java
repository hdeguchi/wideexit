/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import soars.application.visualshell.main.Constant;

/**
 * @author kurata
 *
 */
public class Variable {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static public List<String> _allTypes = new ArrayList<String>();

	/**
	 * 
	 */
	static public List<String> _variableTypes = new ArrayList<String>( Arrays.asList( Constant._kinds));

	/**
	 * 
	 */
	static public List<String> _independentVariableTypes = new ArrayList<String>();

	/**
	 * 
	 */
	static public List<String> _immediateTypes = new ArrayList<String>();

	/**
	 * 
	 */
	public String _type;

	/**
	 * 
	 */
	public String _name;

	/**
	 * 
	 */
	public boolean _empty = false;

	/**
	 * 
	 */
	static {
		startup();
	}

	/**
	 * 
	 */
	private static void startup() {
		synchronized( _lock) {
			_variableTypes.add( "agent variable");	// TODO Constant._kindsに含まれるようになったら不要
			_variableTypes.add( "integer number object");
			_variableTypes.add( "real number object");

			_independentVariableTypes.add( "agent");
			_independentVariableTypes.add( "spot");
			_independentVariableTypes.add( "role");
			_independentVariableTypes.add( "agent role");
			_independentVariableTypes.add( "spot role");
			_independentVariableTypes.add( "stage");

			_variableTypes.addAll( _independentVariableTypes);

			_immediateTypes.add( "immediate probability");
			_immediateTypes.add( "immediate keyword");
			_immediateTypes.add( "immediate integer");
			_immediateTypes.add( "immediate real number");
			_immediateTypes.add( "immediate time variable");
			_immediateTypes.add( "immediate exchange algebra base");
			_immediateTypes.add( "immediate data");

			_allTypes.addAll( _variableTypes);
			_allTypes.addAll( _immediateTypes);
		}
	}

	/**
	 * @param type
	 * @param name
	 */
	public Variable(String type, String name) {
		super();
		_type = type;
		_name = name;
	}

	/**
	 * @param variable
	 */
	public Variable(Variable variable) {
		super();
		_type = variable._type;
		_name = variable._name;
		_empty = variable._empty;
	}

	/**
	 * @param type
	 * @param name
	 * @param empty
	 */
	public Variable(String type, String name, boolean empty) {
		super();
		_type = type;
		_name = name;
		_empty = empty;
	}

	/**
	 * @param variable
	 * @return
	 */
	public boolean same_as(Variable variable) {
		return ( _type.equals( variable._type) && _name.equals( variable._name) && _empty == variable._empty);
	}
}
