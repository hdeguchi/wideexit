/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.property;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import soars.application.visualshell.main.ResourceManager;

/**
 * @author kurata
 *
 */
public class Property extends Vector<Verb> {

	/**
	 * 
	 */
	public String _entity;

	/**
	 * 
	 */
	public String _type;

	/**
	 * 
	 */
	public List<String> _parents = new ArrayList<String>();

	/**
	 * 
	 */
	public String _name;

	/**
	 * 
	 */
	public Color _color = Color.black;

	/**
	 * 
	 */
	public String _title;

	/**
	 * 
	 */
	public Subject _subject = null;

	/**
	 * 
	 */
	public Vector<Subject> _objects = new Vector<Subject>();

	/**
	 * 
	 */
	public boolean _denial = false;

	/**
	 * 
	 */
	public boolean _system = true;

	/**
	 * @param denial
	 * @param system
	 * @param kind
	 */
	public Property(boolean denial, boolean system, String kind) {
		super();
		_denial = denial;
		_system = system;
		if ( !_system)
			_parents.add( ResourceManager.get_instance().get( "generic.user.gui.rule." + kind + ".title"));
	}

	/**
	 * @param red
	 * @param green
	 * @param blue
	 * @return
	 */
	public boolean set_color(String red, String green, String blue) {
		try {
			int r = Integer.parseInt( red);
			int g = Integer.parseInt( green);
			int b = Integer.parseInt( blue);

			if ( 0 > r || 255 < r)
				return false;

			if ( 0 > g || 255 < g)
				return false;

			if ( 0 > b || 255 < b)
				return false;

			_color = new Color( r, g, b);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param line
	 * @return
	 */
	public boolean set_subject(String line) {
		_subject = new Subject();
		return _subject.set( line);
	}

	/**
	 * @param line
	 * @return
	 */
	public boolean set_object(String line) {
//		if ( line.startsWith( "clear")) {
//			_objects.clear();
//			return true;
//		}

		if ( null == _subject)
			set_subject( "");
			//return false;

		Subject object = new Subject();
		if ( !object.set( line))
			return false;

		_objects.add( object);

		return true;
	}

	/**
	 * @param line
	 * @return
	 */
	public boolean set_expression(String line) {
		if ( null == _subject)
			set_subject( "");
			//return false;

		Subject object = new Subject();
		if ( !object.set_expression( line))
			return false;

		_objects.add( object);

		return true;
	}

	/**
	 * @param line
	 * @return
	 */
	public boolean set_variable(String line) {
		if ( null == _subject)
			set_subject( "");
			//return false;

		return ( _objects.isEmpty() ? _subject.set_variable( line) : _objects.lastElement().set_variable( line));
	}

	/**
	 * @param line
	 * @return
	 */
	public boolean set_constant(String line) {
		if ( null == _subject)
			set_subject( "");
			//return false;

		Subject object = new Subject();
		if ( !object.set_constant( line))
			return false;

		_objects.add( object);

		return true;
	}

	/**
	 * @param line
	 * @return
	 */
	public boolean set_value(String line) {
		if ( null == _subject)
			set_subject( "");
			//return false;

		if ( _objects.isEmpty() || !_objects.lastElement()._constant)
			return false;

		return _objects.lastElement().set_value( line);
	}

	/**
	 * @param line
	 * @return
	 */
	public boolean set_sync(String line) {
		if ( _objects.isEmpty())
			_subject._sync = line;
		else
			_objects.lastElement()._sync = line;
		return true;
	}

	/**
	 * @param line
	 * @param denial 
	 * @return
	 */
	public boolean set_verb(String line, boolean denial) {
		if ( null == _subject/* && _objects.isEmpty()*/)
			set_subject( "");
			//return false;

		Verb verb = new Verb( _subject, _objects, denial);
		if ( !verb.set( line))
			return false;

		add( verb);

		_objects.clear();

		return true;
	}

	/**
	 * 
	 */
	public void sort() {
		Collections.sort( this, new VerbComparator());
	}

	/**
	 * @return
	 */
	public String[] get_verb_names() {
		List<String> verbNames = new ArrayList<String>();
		for ( Verb verb:this)
			verbNames.add( verb._methodName);
		return verbNames.toArray( new String[ 0]);
	}

	/**
	 * 
	 */
	public void print() {
		System.out.println( "entity = " + _entity);
		System.out.println( "type = " + _type);

		String text = "";
		for ( String parent:_parents)
			text += ( ( text.equals( "") ? "" : "/") + parent);

		System.out.println( "parent = " + text);

		System.out.println( "name = " + _name);
		System.out.println( "color = " + _color);
		System.out.println( "title = " + _title);
		for ( Verb verb:this)
			verb.print();
		System.out.println( "");
	}
}
