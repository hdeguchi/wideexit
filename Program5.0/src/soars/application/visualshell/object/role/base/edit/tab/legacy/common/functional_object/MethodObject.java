/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.common.functional_object;

import java.util.List;

/**
 * @author kurata
 *
 */
public class MethodObject {

	/**
	 * 
	 */
	public String _name = "";

	/**
	 * 
	 */
	public String _returnType = "";

	/**
	 * 
	 */
	public String[] _parameterTypes = null;

	/**
	 * @param name
	 * @param returnType
	 * @param parameterTypes
	 */
	public MethodObject(String name, String returnType, List<String> parameterTypes) {
		super();
		_name = name;
		_returnType = returnType;
		_parameterTypes = parameterTypes.toArray( new String[ 0]);
	}

	/**
	 * @return
	 */
	public String get_full_method_name() {
		// TODO 2012.8.1
		String name = ( _name + "(");
		for ( int i = 0; i < _parameterTypes.length; ++i) {
			name += ( ( ( 0 == i) ? "" : ", ") + _parameterTypes[ i]);
		}
		name += ")";
		return name;
	}

	/**
	 * @param methodName
	 * @param functionalObject
	 * @return
	 */
	public boolean equals(String methodName, FunctionalObject functionalObject) {
		// TODO 2012.8.1
		String name = ( functionalObject._method + "(");
		for ( int i = 0; i < functionalObject._parameters.length; ++i)
			name += ( ( ( 0 == i) ? "" : ", ") + functionalObject._parameters[ i][ 1]);
		name += ")";
		return name.equals( methodName);
	}
}
