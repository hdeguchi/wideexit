/*
 * Created on 2006/01/30
 */
package soars.application.visualshell.common.selector;

/**
 * The interface for the callback of ObjectSelector.
 * @author kurata / SOARS project
 */
public interface IObjectSelectorHandler {

	/**
	 * Invoked when the selected object is changed.
	 * @param name the name of object
	 * @param number the object number 
	 * @param fullName the full name of object
	 * @param objectSelector the instance of ObjectSelector class
	 */
	void changed(String name, String number, String fullName, ObjectSelector objectSelector);
}
