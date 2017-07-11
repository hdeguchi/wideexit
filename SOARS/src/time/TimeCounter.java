package time;

import env.Environment;
import env.StepCounter;

/**
 * The TimeCounter class represents time counter in SOARS.
 * @author H. Tanuma / SOARS project
 */
public class TimeCounter extends StepCounter {

	private static final long serialVersionUID = 8412044402247765454L;
	Time time = new Time();
	Time stepTime = new Time();

	/**
	 * Get current time of simulation.
	 * @return current time
	 */
	public static Time getCurrentTime() {
		return ((TimeCounter) Environment.getCurrent().getStepCounter()).getTime();
	}
	/**
	 * Get time interval of simulation.
	 * @return time interval
	 */
	public static Time getCurrentStepTime() {
		return ((TimeCounter) Environment.getCurrent().getStepCounter()).getStepTime();
	}
	/**
	 * Get time of the time counter.
	 * @return time of the time counter
	 */
	public Time getTime() {
		return time;
	}
	/**
	 * Get time interval of the time counter.
	 * @return time interval of the time counter
	 */
	public Time getStepTime() {
		return stepTime;
	}
	protected void nextStep() {
		time.add(stepTime);
	}
	/**
	 * Get time string.
	 * @return time string
	 */
	public String toString() {
		return time.toString();
	}
}
