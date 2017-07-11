/*
 * Created on 2006/04/10
 */
package soars.application.visualshell.object.role.base.object.legacy.common.expression;

import soars.common.utility.swing.combo.ComboBox;

/**
 * @author kurata
 */
public class SubType {

	/**
	 * 
	 */
	public int[] _kind = new int[] { -1, -1};

	/**
	 * 
	 */
	public String _operator = "";

	/**
	 * @param value
	 * @param subTypes
	 * @return
	 */
	public static SubType get(String value, String[] subTypes) {
		String[] elements = value.split( ": ");
		if ( null == elements || 2 > elements.length)
			return ( new SubType( 2, "", -1));

		elements = elements[ 0].split( " ");
		if ( 2 == elements.length) {
			if ( !elements[ 1].equals( "none"))
				return null;

			int kind1 = get_kind( elements[ 0], subTypes);
			if ( 0 > kind1)
				return null;

			return ( new SubType( kind1, "", -1));
		} else if ( 3 == elements.length) {
			int kind1 = get_kind( elements[ 0], subTypes);
			if ( 0 > kind1)
				return null;

			if ( 1 != elements[ 1].length() || 0 > "+-*/%".indexOf( elements[ 1]))
				return null;

			int kind2 = get_kind( elements[ 2], subTypes);
			if ( 0 > kind2)
				return null;

			return ( new SubType( kind1, elements[ 1], kind2));
		}

		return null;
	}

	/**
	 * @param value
	 * @param subTypes
	 * @return
	 */
	private static int get_kind(String value, String[] subTypes) {
		for ( int i = 0; i < subTypes.length; ++i) {
			if ( subTypes[ i].equals( value))
				return i;
		}
		return -1;
	}

	/**
	 * @param kind1
	 * @param comboBox
	 * @param kind2
	 * @return
	 */
	public static SubType get(int kind1, ComboBox comboBox, int kind2) {
		String operator = ( String)comboBox.getSelectedItem();
		if ( null == operator)
			return null;

		return new SubType( kind1, operator, kind2);
	}

	/**
	 * @param kind1
	 * @param operator
	 * @param kind2
	 */
	public SubType(int kind1, String operator, int kind2) {
		super();
		_kind[ 0] = kind1;
		_operator = operator;
		_kind[ 1] = kind2;
	}

	/**
	 * @param subTypes
	 */
	public String get(String[] subTypes) {
		if ( null == _operator || _operator.equals( ""))
			return ( subTypes[ _kind[ 0]] + " none: ");
		else
			return ( subTypes[ _kind[ 0]] + " " + _operator + " " + subTypes[ _kind[ 1]] + ": ");
	}

	/**
	 * @param value
	 * @return
	 */
	public String get_value(String value) {
		String[] elements = value.split( ": ");
		if ( null == elements || 2 > elements.length) {
			if ( 2 == _kind[ 0] && _operator.equals( ""))
				return value;

			return null;
		}

		return elements[ 1];
	}
}
