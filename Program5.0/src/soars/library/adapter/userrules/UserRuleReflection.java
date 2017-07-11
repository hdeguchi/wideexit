/**
 * 
 */
package soars.library.adapter.userrules;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import soars.common.soars.constant.CommonConstant;
import soars.common.soars.tool.SoarsReflection;
import soars.common.utility.tool.common.Tool;
import env.EquippedObject;

/**
 * @author kurata
 *
 */
public class UserRuleReflection {

	/**
	 * @param linkedList
	 * @return
	 */
	public static boolean execute_condition(LinkedList<Object> linkedList) {
		return execute( linkedList, true);
	}

	/**
	 * @param linkedList
	 */
	public static void execute_command(LinkedList<Object> linkedList) {
		execute( linkedList, false);
	}

	/**
	 * @param linkedList
	 * @param condition
	 * @return
	 */
	private static boolean execute(LinkedList<Object> linkedList, boolean condition) {
		if ( null == linkedList || 2 > linkedList.size()) {
			JOptionPane.showMessageDialog( null,
				"User rule error : Invalid LinkedList",
				get_title( condition),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		EquippedObject equippedObject = ( EquippedObject)linkedList.get( 0);
		if ( null == equippedObject) {
			JOptionPane.showMessageDialog( null,
				"User rule error : Invalid LinkedList",
				get_title( condition),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		String[] words = Tool.split( ( String)linkedList.get( 1), ':');
		if ( 2 != words.length) {
			JOptionPane.showMessageDialog( null,
				"User rule error : Invalid LinkedList",
				get_title( condition),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		Class cls = SoarsReflection.get_class( words[ 0]);
		if ( null == cls) {
			JOptionPane.showMessageDialog( null,
				"User rule error : Could not get class!",
				get_title( condition),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		List<Class> classes = new ArrayList<Class>();
		classes.add( EquippedObject.class);
		for ( int i = 2; i < linkedList.size(); ++i)
			classes.add( String.class);

		List<Object> arguments = new ArrayList<Object>();
		arguments.add( equippedObject);
		for ( int i = 2; i < linkedList.size(); ++i)
			arguments.add( linkedList.get( i));

		if ( condition) {
			List<Object> resultList = new ArrayList<Object>();
			if ( !SoarsReflection.execute_static_method( cls, words[ 1], classes.toArray( new Class[ 0]), arguments.toArray( new Object[ 0]), resultList)) {
				JOptionPane.showMessageDialog( null,
					"User rule error : SoarsReflection.execute_static_method( ... )" + CommonConstant._lineSeparator
						+ " Class name : " + words[ 0] + CommonConstant._lineSeparator
						+ " Method name : " + words[ 1] + CommonConstant._lineSeparator,
					get_title( condition),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if ( resultList.isEmpty() || null == resultList.get( 0) || !Boolean.class.isInstance( resultList.get( 0))) {
				JOptionPane.showMessageDialog( null,
					"User rule error : SoarsReflection.execute_static_method( ... )" + CommonConstant._lineSeparator
						+ " Class name : " + words[ 0] + CommonConstant._lineSeparator
						+ " Method name : " + words[ 1] + CommonConstant._lineSeparator,
					get_title( condition),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}

			return ( ( Boolean)resultList.get( 0)).booleanValue();
		} else {
			if ( !SoarsReflection.execute_static_method( cls, words[ 1], classes.toArray( new Class[ 0]), arguments.toArray( new Object[ 0]))) {
				JOptionPane.showMessageDialog( null,
					"User rule error : SoarsReflection.execute_static_method( ... )" + CommonConstant._lineSeparator
						+ " Class name : " + words[ 0] + CommonConstant._lineSeparator
						+ " Method name : " + words[ 1] + CommonConstant._lineSeparator,
					get_title( condition),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		return true;
	}

	/**
	 * @param condition
	 * @return
	 */
	private static String get_title(boolean condition) {
		return condition ? "execute_condition" : "execute_command";
	}
}
