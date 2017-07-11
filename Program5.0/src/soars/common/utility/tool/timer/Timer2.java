/**
 * 
 */
package soars.common.utility.tool.timer;

import java.lang.reflect.Field;
import java.util.Timer;

import soars.common.utility.tool.reflection.Reflection;

/**
 * @author kurata
 *
 */
public class Timer2 extends Timer {

	/**
	 * 
	 */
	public Timer2() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.util.Timer#cancel()
	 */
	public void cancel() {
		super.cancel();
		try {
			Field field = Timer.class.getDeclaredField( "thread");
			field.setAccessible( true);
			Reflection.execute_class_method( field.get( this), "join", null, null);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
