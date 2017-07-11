package time;

/**
 * The DailyTime class represents time in a day.
 * @author H. Tanuma / SOARS project
 */
public class DailyTime extends Time {

	private static final long serialVersionUID = -4390702134809817350L;

	/**
	 * Check if specified time has passed.
	 * @param time specified time
	 * @return true if specified time has passed
	 */
	public boolean isTime(Time time) {
		return minutes <= time.minutes % DAY;
	}
	/**
	 * Check if specified time has come.
	 * @param time specified time
	 * @return true if specified time has come
	 */
	public boolean isTimeAt(Time time) {
		int m = time.minutes % DAY - minutes;
		return m >= 0 && m < TimeCounter.getCurrentStepTime().minutes;
	}
	/**
	 * Get string expression of the time.
	 * @return time string in format HH:MM
	 */
	public String toString() {
		int hours = getHours();
		return twoDigit(hours) + ":" + twoDigit(getMinutes() - hours * HOUR);
	}
}
