package graphWiz.model;

public interface Algorithm {


	/**
	 * @return String table describing the algorithm (notations and steps)
	 */
	public abstract String[] getAlgo();

	/**
	 * @return the index of the String describing the current step. (Index in the
	 * description table of the algorithm returned by getAlgo)
	 */
	public abstract int getCurrentStep();

	/**
	 * @return true when the algorithm is finished
	 */
	public abstract boolean isEnd();

	/**
	 * @return true if the algorithm has not performed anything apart from
	 * initializing valuations.
	 */
	public abstract boolean isStart();

	public abstract void nextStep();

	public abstract void previousStep();
	
	public abstract boolean isEligible();
	
	/**
	 * Check that the current graph eligible for this algorithm.
	 * @return a String describing why the graph is 
	 */
	public abstract String checkGraph();

}
