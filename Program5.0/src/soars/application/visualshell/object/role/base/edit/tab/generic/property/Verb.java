/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.property;

import java.util.List;
import java.util.Vector;

import soars.application.visualshell.object.role.base.object.generic.GenericRule;
import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 *
 */
public class Verb {

	/**
	 * 
	 */
	public Subject _subject = null;

	/**
	 * 
	 */
	public String _id = null;

	/**
	 * TODO Added 2013.7.4
	 */
	public int _index = 0;

	/**
	 * 
	 */
	public String _method = null;

	/**
	 * 
	 */
	public String _methodName = "";

	/**
	 * 
	 */
	public Vector<Subject> _objects = new Vector<Subject>();

	/**
	 * 
	 */
	public boolean _denial = false;

	/**
	 * @param subject
	 * @param objects
	 * @param denial 
	 * 
	 */
	public Verb(Subject subject, Vector<Subject> objects, boolean denial) {
		super();
		_subject = new Subject( subject);
		_denial = denial;
		for ( Subject object:objects)
			_objects.add( new Subject( object));
	}

	/**
	 * @param line
	 * @return
	 */
	public boolean set(String line) {
		if ( line.equals( ""))
			return false;

		String[] words = Tool.split( line, ',');
		if ( null == words || 4 > words.length)
			return false;

		// TODO modified 2013.7.4
		int n;
		try {
			n = Integer.parseInt( words[ 1]);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}

		_id = words[ 0];
		_index = n;
		_method = words[ 2];
		_methodName = words[ 3];

		// TODO modified 2014.2.13
		for ( int i = 4; i < words.length; ++i) {
			if ( words[ i].equals( "denial=no"))
				_denial = false;
		}

		return true;
	}

	/**
	 * @param variables
	 * @param kind
	 */
	public void get_variables(List<Variable> variables, String kind) {
		if ( kind.equals( "subject"))
			_subject.get_variables( variables);
		else if ( kind.equals( "object")) {
			for ( Subject object:_objects)
				object.get_variables( variables);
		}
	}

	/**
	 * @param genericRule
	 */
	public void get(GenericRule genericRule) {
		genericRule._id = _id;
	}

	/**
	 * 
	 */
	public void print() {
		System.out.println( "Verb : " + _method + ", " + _methodName);
		_subject.print( "Subject");
		for ( Subject object:_objects)
			object.print( "Object");
	}
}
