package env;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The StepCounter class represents step counter in SOARS.
 * @author H. Tanuma / SOARS project
 */
public class StepCounter implements Serializable {

	private static final long serialVersionUID = 3136852583704075677L;
	int step = 0;
	int stage = 0;
	int next = -1;
	int initial = 0;
	int terminal = 0;
	ArrayList<String> stages = new ArrayList<String>();
	{
		addInitialStageName("");
	}

	/**
	 * Get step number.
	 * @return step number
	 */
	public int getStep() {
		return step;
	}
	/**
	 * Get stage index.
	 * @return stage index
	 */
	public int getStage() {
		return stage;
	}
	/**
	 * Add a terminal stage.
	 * @param name stage name
	 */
	public void addTerminalStageName(String name) {
		if (stages.contains(name)) {
			throw new RuntimeException("Stage Name Collision - " + name);
		}
		stages.add(name);
	}
	/**
	 * Add a main stage.
	 * @param name stage name
	 */
	public void addStageName(String name) {
		if (stages.size() != terminal++) {
			throw new RuntimeException("Must Be Terminal Stage - " + name);
		}
		addTerminalStageName(name);
	}
	/**
	 * Add an initial stage.
	 * @param name stage name
	 */
	public void addInitialStageName(String name) {
		if (stages.size() != initial++) {
			throw new RuntimeException("Illegal Initial Stage - " + name);
		}
		addStageName(name);
	}
	/**
	 * Get list of stage names.
	 * @return list of stage names
	 */
	public List<String> getStageNames() {
		return stages;
	}
	/**
	 * Request next stage transition.
	 * @param next stage index
	 */
	public void setNextStage(int next) {
		if (this.next >= 0 && this.next != next) {
			throw new RuntimeException("Next Stage Inconsistency - " + stages.get(this.next) + " and " + stages.get(next));
		}
		this.next = next;
	}
	/**
	 * Request transition to terminal stage.
	 */
	public void terminate() {
		setNextStage(terminal);
	}
	/**
	 * Check if terminate command is issued.
	 * @return true if if terminate command is issued
	 */
	public boolean isTerminated() {
		return next == stages.size() || next < 0 && stage == stages.size() - 1 && stage >= terminal;
	}
	/**
	 * Check if it is at the last stage.
	 * @return true if it is at the last stage
	 */
	public boolean isLastStage() {
		return next < 0 && stage == terminal - 1;
	}
	/**
	 * Check if it is at initial stage.
	 * @return true if it is at initial stage
	 */
	public boolean isInitialStage() {
		return stage < initial;
	}
	/**
	 * Get number of initial stages.
	 * @return number of initial stages
	 */
	public int sizeInitialStages() {
		return initial;
	}
	/**
	 * Check if this stage is shuffled.
	 * @return true if this stage is shuffled
	 */
	public boolean isShuffledStage() {
		return stage < stages.size() && stages.get(stage).toString().endsWith("*");
	}
	/**
	 * Transit to next stage.
	 */
	public void nextStage() {
		if (next >= 0) {
			stage = next;
			next = -1;
		}
		else if (++stage == terminal) {
			stage = initial;
			++step;
			nextStep();
		}
	}
	protected void nextStep() {
	}
	/**
	 * Install step counter object to current simulation.
	 */
	public void install() {
		install(Environment.getCurrent());
	}
	/**
	 * Install step counter object to specified simulation.
	 * @param environment simulation instance
	 */
	public void install(Environment environment) {
		environment.stepCounter = this;
	}
	/**
	 * Get step string.
	 * @return step string
	 */
	public String toString() {
		return "Step " + step;
	}
}
