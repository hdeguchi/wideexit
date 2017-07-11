/**
 * 
 */
package soars.application.animator.object.scenario;

/**
 * @author kurata
 *
 */
public interface IScenarioManipulator {

	/**
	 * Returns the number of steps.
	 * @return the number of steps
	 */
	int get_size();

	/**
	 * Returns the current position of the animation.
	 * @return the current position of the animation
	 */
	int get_current_position();

	/**
	 * @param position
	 */
	void on_animationSlider_state_changed(int position);
}
