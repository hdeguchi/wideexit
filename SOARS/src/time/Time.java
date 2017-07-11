package time;

import java.io.Serializable;

/**
 * The Time class represents time in simulation.
 * @author H. Tanuma / SOARS project
 */
public class Time implements Serializable, Cloneable {

	private static final long serialVersionUID = -3537138566271668444L;

	int minutes = 0;

	static final int HOUR = 60;
	static final int DAY = HOUR * 24;

	/**
	 * Parse time expression.
	 * @param time time expression
	 * @return time value
	 */
	public static Time parse(String time) {
		Time timeObj;
		try {
			String[] pair = time.split("/", 2);
			if (pair.length == 2) {
				timeObj = new Time();
				timeObj.minutes = DAY * Integer.parseInt(pair[0]);
				time = pair[1];
			}
			else {
				timeObj = new DailyTime();
			}
			pair = time.split(":", 2);
			if (pair.length != 2) {
				return null;
			}
			timeObj.minutes += HOUR * Integer.parseInt(pair[0]) + Integer.parseInt(pair[1]);
		}
		catch (NumberFormatException e) {
			return null;
		}
		return timeObj;
	}
	/**
	 * Check if specified time has passed.
	 * @param time specified time
	 * @return true if specified time has passed
	 */
	public boolean isTime(Time time) {
		return minutes <= time.minutes;
	}
	/**
	 * Check if specified time has passed.
	 * @return true if specified time has passed
	 */
	public boolean isTime() {
		return isTime(TimeCounter.getCurrentTime());
	}
	/**
	 * Check if specified time has come.
	 * @param time specified time
	 * @return true if specified time has come
	 */
	public boolean isTimeAt(Time time) {
		int m = time.minutes - minutes;
		return m >= 0 && m < TimeCounter.getCurrentStepTime().minutes;
	}
	/**
	 * Check if specified time has come.
	 * @return true if specified time has come
	 */
	public boolean isTimeAt() {
		return isTimeAt(TimeCounter.getCurrentTime());
	}
	/**
	 * Add time value.
	 * @param time time to add
	 * @return time value
	 */
	public Time add(Time time) {
		minutes += time.minutes;
		return this;
	}
	/**
	 * Subtract time value
	 * @param time time to subtract
	 * @return time value
	 */
	public Time sub(Time time) {
		minutes -= time.minutes;
		return this;
	}
	/**
	 * Multiply time value
	 * @param mag magnification
	 * @return time value
	 */
	public Time mul(int mag) {
		minutes *= mag;
		return this;
	}
	/**
	 * Set time value
	 * @param time time to set
	 * @return time value
	 */
	public Time set(Time time) {
		minutes = time.minutes;
		return this;
	}
	/**
	 * Set time as present time.
	 * @return present time
	 */
	public Time set() {
		return set(TimeCounter.getCurrentTime());
	}
	/**
	 * Set time as minutes.
	 * @param minutes minute value
	 * @return time value
	 */
	public Time setMinutes(int minutes) {
		this.minutes = minutes;
		return this;
	}
	/**
	 * Get minute value of the time.
	 * @return minute value
	 */
	public int getMinutes() {
		return minutes;
	}
	/**
	 * Set time as hours.
	 * @param hours hour value
	 * @return time value
	 */
	public Time setHours(int hours) {
		this.minutes = hours * HOUR;
		return this;
	}
	/**
	 * Get hour value of the time.
	 * @return hour value
	 */
	public int getHours() {
		return minutes / HOUR;
	}
	/**
	 * Set time as days.
	 * @param days day value
	 * @return time value
	 */
	public Time setDays(int days) {
		this.minutes = days * DAY;
		return this;
	}
	/**
	 * Get day value of the time.
	 * @return day value
	 */
	public int getDays() {
		return minutes / DAY;
	}
	/**
	 * Create clone of the variable.
	 * @return clone of the variable
	 * @throws CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	protected static String twoDigit(int i) {
		String s = Integer.toString(i);
		if (s.length() == 1) {
			s = '0' + s;
		} 
		return s;
	}
	/**
	 * Get string expression of the time.
	 * @return time string in format DAYS/HH:MM
	 */
	public String toString() {
		int hours = getHours();
		int minutes = getMinutes() - hours * HOUR;
		int days = getDays();
		hours -= days * (DAY / HOUR);
		return days + "/" + twoDigit(hours) + ':' + twoDigit(minutes);
	}
}
