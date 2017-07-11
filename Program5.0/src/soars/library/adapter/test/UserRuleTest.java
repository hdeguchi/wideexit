/**
 * 
 */
package soars.library.adapter.test;

import javax.swing.JOptionPane;

import soars.common.soars.constant.CommonConstant;
import soars.library.adapter.userrules.UserRuleUtility;
import env.EquippedObject;

/**
 * @author kurata
 *
 */
public class UserRuleTest {

	/**
	 * @param equippedObject
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public static void setKeyword(EquippedObject equippedObject, String arg0, String arg1, String arg2) {
		JOptionPane.showMessageDialog( null,
			equippedObject.getEquip( "$Name") + CommonConstant._lineSeparator
				+ arg0 + CommonConstant._lineSeparator
				+ arg1 + CommonConstant._lineSeparator
				+ arg2 + CommonConstant._lineSeparator,
			"setKeyword",
			JOptionPane.INFORMATION_MESSAGE);

		String text = UserRuleUtility.getString( equippedObject, arg1);
		JOptionPane.showMessageDialog( null, ( null != text) ? text : "Invalid argument!", "setKeyword", ( null != text) ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);

		text = UserRuleUtility.getString( equippedObject, arg2);
		JOptionPane.showMessageDialog( null, ( null != text) ? text : "Invalid argument!", "setKeyword", ( null != text) ? JOptionPane.INFORMATION_MESSAGE :JOptionPane.ERROR_MESSAGE);

		//int n = UserRuleUtility.get_int_value( equippedObject, arg1);
		//double d = UserRuleUtility.get_double_value( equippedObject, arg1);

		UserRuleUtility.set( equippedObject, arg1, "hogehoge");
		UserRuleUtility.set( equippedObject, arg2, "hogehoge");

		text = UserRuleUtility.getString( equippedObject, arg1);
		JOptionPane.showMessageDialog( null, ( null != text) ? text : "Invalid argument!", "setKeyword", ( null != text) ? JOptionPane.INFORMATION_MESSAGE :JOptionPane.ERROR_MESSAGE);

		text = UserRuleUtility.getString( equippedObject, arg2);
		JOptionPane.showMessageDialog( null, ( null != text) ? text : "Invalid argument!", "setKeyword", ( null != text) ? JOptionPane.INFORMATION_MESSAGE :JOptionPane.ERROR_MESSAGE);

		//UserRuleUtility.set( equippedObject, arg1, n);
		//UserRuleUtility.set( equippedObject, arg1, d);

		//Object object = UserRuleUtility.get_class_variable( equippedObject, arg1);

		//JOptionPane.showMessageDialog( null, eo.getEquip( "$Name"), "setKeyword", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	public static boolean isNameContains(EquippedObject equippedObject, String arg0, String arg1, String arg2) {
		JOptionPane.showMessageDialog( null,
				equippedObject.getEquip( "$Name") + CommonConstant._lineSeparator
				+ arg0 + CommonConstant._lineSeparator
				+ arg1 + CommonConstant._lineSeparator
				+ arg2 + CommonConstant._lineSeparator,
			"isNameContains",
			JOptionPane.INFORMATION_MESSAGE);
		return true;
	}
}
