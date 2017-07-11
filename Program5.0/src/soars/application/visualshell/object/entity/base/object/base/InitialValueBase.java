/**
 * 
 */
package soars.application.visualshell.object.entity.base.object.base;

import soars.application.visualshell.object.entity.base.object.exchange_algebra.ExchangeAlgebraInitialValue;
import soars.application.visualshell.object.entity.base.object.map.MapInitialValue;
import soars.application.visualshell.object.entity.base.object.variable.VariableInitialValue;


/**
 * @author kurata
 *
 */
public class InitialValueBase {

	/**
	 * 
	 */
	public int _row = 0;	// for copy, cut and paste only.

	/**
	 * 
	 */
	public InitialValueBase() {
		super();
	}

	/**
	 * @param row
	 */
	public InitialValueBase(int row) {
		super();
		_row = row;
	}

	/**
	 * @param initialValueBase
	 * @return
	 */
	public static InitialValueBase create(InitialValueBase initialValueBase) {
		if ( initialValueBase instanceof VariableInitialValue)
			return new VariableInitialValue( ( VariableInitialValue)initialValueBase);
		else if ( initialValueBase instanceof MapInitialValue)
			return new MapInitialValue( ( MapInitialValue)initialValueBase);
		else if ( initialValueBase instanceof ExchangeAlgebraInitialValue)
			return new ExchangeAlgebraInitialValue( ( ExchangeAlgebraInitialValue)initialValueBase);
		else
			return null;
	}

	/**
	 * @param initialValueBase
	 * @param row
	 * @return
	 */
	public static InitialValueBase create(InitialValueBase initialValueBase, int row) {
		if ( initialValueBase instanceof VariableInitialValue)
			return new VariableInitialValue( ( VariableInitialValue)initialValueBase, row);
		else if ( initialValueBase instanceof MapInitialValue)
			return new MapInitialValue( ( MapInitialValue)initialValueBase, row);
		else if ( initialValueBase instanceof ExchangeAlgebraInitialValue)
			return new ExchangeAlgebraInitialValue( ( ExchangeAlgebraInitialValue)initialValueBase, row);
		else
			return null;
	}
}
