/**
 * 
 */
package soars.library.auxiliary;

import java.util.HashMap;
import java.util.Map;

import org.soars.exalge.util.ExalgeMath;

import time.DailyTime;
import time.Time;
import exalge2.Exalge;

/**
 * @author kurata
 *
 */
public class ExchangeAlgebraUtility {

	/**
	 * 
	 */
	static private Map<String, String>_hourMap = new HashMap<String, String>();

	/**
	 * 
	 */
	static private Map<String, String>_minuteMap = new HashMap<String, String>();

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
		for ( int i = 0; i < 10; ++i) {
			_hourMap.put( String.format( "%02d", i), String.format( "%d", i));
			_minuteMap.put( String.format( "%d", i), String.format( "%02d", i));
		}
	}

	/**
	 * @param exalge
	 * @param name
	 * @param hat
	 * @param unit
	 * @param time
	 * @param subject
	 * @return
	 */
	static public Exalge projection(Exalge exalge, String name, String hat, String unit, String time, String subject) {
		if ( null == exalge)
			throw new NullPointerException( "Exchange algebra is null!");

		return ExalgeMath.projection( exalge, get_base( name, hat, unit, time, subject));
	}

	/**
	 * @param exalge
	 * @param name
	 * @param hat
	 * @param unit
	 * @param time
	 * @param subject
	 * @return
	 */
	static public Exalge projection(Exalge exalge, String name, String hat, String unit, Time time, String subject) {
		if ( null == exalge)
			throw new NullPointerException( "Exchange algebra is null!");

		return ExalgeMath.projection( exalge, get_base( name, hat, unit, time, subject));
	}

	/**
	 * @param exalge
	 * @param name
	 * @param hat
	 * @param unit
	 * @param dailyTime
	 * @param subject
	 * @return
	 */
	static public Exalge projection(Exalge exalge, String name, String hat, String unit, DailyTime dailyTime, String subject) {
		if ( null == exalge)
			throw new NullPointerException( "Exchange algebra is null!");

		return ExalgeMath.projection( exalge, get_base( name, hat, unit, dailyTime, subject));
	}

	/**
	 * @param exalge
	 * @param value
	 * @param name
	 * @param hat
	 * @param unit
	 * @param time
	 * @param subject
	 * @return
	 */
	static public Exalge plus(Exalge exalge, String value, String name, String hat, String unit, String time, String subject) {
		if ( null == exalge)
			throw new NullPointerException( "Exchange algebra is null!");

		if ( null == value)
			throw new NullPointerException( "Exchange algebra value is null!");

		return ExalgeMath.plus( exalge, value, get_base( name, hat, unit, time, subject));
	}

	/**
	 * @param exalge
	 * @param value
	 * @param name
	 * @param hat
	 * @param unit
	 * @param time
	 * @param subject
	 * @return
	 */
	static public Exalge plus(Exalge exalge, String value, String name, String hat, String unit, Time time, String subject) {
		if ( null == exalge)
			throw new NullPointerException( "Exchange algebra is null!");

		if ( null == value)
			throw new NullPointerException( "Exchange algebra value is null!");

		return ExalgeMath.plus( exalge, value, get_base( name, hat, unit, time, subject));
	}

	/**
	 * @param exalge
	 * @param value
	 * @param name
	 * @param hat
	 * @param unit
	 * @param dailyTime
	 * @param subject
	 * @return
	 */
	static public Exalge plus(Exalge exalge, String value, String name, String hat, String unit, DailyTime dailyTime, String subject) {
		if ( null == exalge)
			throw new NullPointerException( "Exchange algebra is null!");

		if ( null == value)
			throw new NullPointerException( "Exchange algebra value is null!");

		return ExalgeMath.plus( exalge, value, get_base( name, hat, unit, dailyTime, subject));
	}

	/**
	 * @param exalge
	 * @param value
	 * @param name
	 * @param hat
	 * @param unit
	 * @param time
	 * @param subject
	 * @return
	 */
	static public Exalge plus(Exalge exalge, double value, String name, String hat, String unit, String time, String subject) {
		if ( null == exalge)
			throw new NullPointerException( "Exchange algebra is null!");

		return ExalgeMath.plus( exalge, value, get_base( name, hat, unit, time, subject));
	}

	/**
	 * @param exalge
	 * @param value
	 * @param name
	 * @param hat
	 * @param unit
	 * @param time
	 * @param subject
	 * @return
	 */
	static public Exalge plus(Exalge exalge, double value, String name, String hat, String unit, Time time, String subject) {
		if ( null == exalge)
			throw new NullPointerException( "Exchange algebra is null!");

		return ExalgeMath.plus( exalge, value, get_base( name, hat, unit, time, subject));
	}

	/**
	 * @param exalge
	 * @param value
	 * @param name
	 * @param hat
	 * @param unit
	 * @param dailyTime
	 * @param subject
	 * @return
	 */
	static public Exalge plus(Exalge exalge, double value, String name, String hat, String unit, DailyTime dailyTime, String subject) {
		if ( null == exalge)
			throw new NullPointerException( "Exchange algebra is null!");

		return ExalgeMath.plus( exalge, value, get_base( name, hat, unit, dailyTime, subject));
	}

	/**
	 * @param exalge
	 * @param name
	 * @param hat
	 * @param unit
	 * @param time
	 * @param subject
	 * @return
	 */
	static public double getValue(Exalge exalge, String name, String hat, String unit, String time, String subject) {
		if ( null == exalge)
			throw new NullPointerException( "Exchange algebra is null!");

		return ExalgeMath.getValue( exalge, get_base( name, hat, unit, time, subject));
	}

	/**
	 * @param exalge
	 * @param name
	 * @param hat
	 * @param unit
	 * @param time
	 * @param subject
	 * @return
	 */
	static public double getValue(Exalge exalge, String name, String hat, String unit, Time time, String subject) {
		if ( null == exalge)
			throw new NullPointerException( "Exchange algebra is null!");

		return ExalgeMath.getValue( exalge, get_base( name, hat, unit, time, subject));
	}

	/**
	 * @param exalge
	 * @param name
	 * @param hat
	 * @param unit
	 * @param dailyTime
	 * @param subject
	 * @return
	 */
	static public double getValue(Exalge exalge, String name, String hat, String unit, DailyTime dailyTime, String subject) {
		if ( null == exalge)
			throw new NullPointerException( "Exchange algebra is null!");

		return ExalgeMath.getValue( exalge, get_base( name, hat, unit, dailyTime, subject));
	}

	/**
	 * @param name
	 * @param hat
	 * @param unit
	 * @param time
	 * @param subject
	 * @return
	 */
	static private String get_base(Object name, Object hat, Object unit, Object time, Object subject) {
		if ( null == name)
			throw new NullPointerException( "Exchange algebra : name base is null!");

		if ( null == hat)
			throw new NullPointerException( "Exchange algebra : hat is null!");

		if ( null == unit)
			throw new NullPointerException( "Exchange algebra : unit base is null!");

		if ( null == time)
			throw new NullPointerException( "Exchange algebra : time base is null!");

		if ( null == subject)
			throw new NullPointerException( "Exchange algebra : subject base is null!");

		String base = "";
		base += ( name instanceof Time) ? get( name.toString()) : name;
		base += ( "-" + hat);
		base += "-" + ( ( unit instanceof Time) ? get( unit.toString()) : unit);
		base += "-" + ( ( time instanceof Time) ? get( time.toString()) : time);
		base += "-" + ( ( subject instanceof Time) ? get( subject.toString()) : subject);
		return base;
	}

	/**
	 * @param time
	 * @return
	 */
	static private Object get(String time) {
		String[] words = time.split( "/");
		if ( 1 == words.length)
			return get_daily_time( words[ 0]);
		else if ( 2 == words.length)
			return ( words[ 0] + "/" + get_daily_time( words[ 1]));
		else
			return time;
	}

	/**
	 * @param dailyTime
	 * @return
	 */
	static private Object get_daily_time(String dailyTime) {
		String[] words = dailyTime.split( ":");
		if ( 2 != words.length)
			return dailyTime;

		String hour = _hourMap.get( words[ 0]);
		String minute = _minuteMap.get( words[ 1]);
		return ( ( ( null == hour) ? words[ 0] : hour) + ":" + ( ( null == minute) ? words[ 1] : minute));
	}
}
