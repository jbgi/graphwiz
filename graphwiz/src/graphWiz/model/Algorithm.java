package graphWiz.model;

import graphWiz.model.GWizEdge.Description;

import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

public abstract class Algorithm {
	
	GWizVertex startingVertex, endVertex, currentVertex;
	
	private Stack<Vector<Description>> edgesDescriptionHistory;
	
	private GWizGraph graph;

	private Stack<Vector<boolean[]>> verticesFlagHistory;
	
	private Stack<Vector<GWizVertex[]>> verticesPredHistory;
	
	private Stack<Vector<double[]>> verticesValuationHistory;
	
	private Stack<Integer> currentStepHistory;
	
	private Stack<String> commentsHistory;
	
	//public Commentaires comm = new Commentaires();
	
	int currentStep = 1;
	
	String comments = "";
	
	public Algorithm(GWizGraph graph) {
		
		this.graph=graph;
		edgesDescriptionHistory = new Stack<Vector<Description>>();
		verticesFlagHistory = new Stack<Vector<boolean[]>>();
		verticesPredHistory = new Stack<Vector<GWizVertex[]>>();
		verticesValuationHistory = new Stack<Vector<double[]>>();
		currentStepHistory = new Stack<Integer>();
		commentsHistory = new Stack<String>();
	}
	
	/**
	 * Check that the current graph eligible for this algorithm.
	 * @return a String describing why the graph is 
	 */
	public abstract String checkGraph();
	
	private void clearHistories(){
		edgesDescriptionHistory.clear();
		verticesFlagHistory.clear();
		verticesPredHistory.clear();
		verticesValuationHistory.clear();
		currentStepHistory.clear();
		commentsHistory.clear();
	}
	
	public final void retoreInitialState(){
		Iterator<GWizVertex> v = graph.vertexSet().iterator();
		while (v.hasNext())
			v.next().reset();
		
		Iterator<GWizEdge> e = graph.edgeSet().iterator();
		while (e.hasNext())
			e.next().reset();
		if (!currentStepHistory.isEmpty())
			currentStep = currentStepHistory.firstElement();
		clearHistories();
	}
	
	public final void previousStep(){
		restorePreviousGraph();
	}
	
	public abstract void initialize(); 


	/**
	 * @return String table describing the algorithm (notations and steps)
	 */
	public abstract String[] getAlgo();

	/**
	 * @return the index of the String describing the current step. (Index in the
	 * description table of the algorithm returned by getAlgo)
	 */
	public final int getCurrentStep() {
		return currentStep;
	}

	public final GWizGraph getGraph() {
		return graph;
	}

	public abstract boolean isEligible();

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
	
	public abstract void setEndVertex (GWizVertex endVertex);
	
	/**
	 * @param graph  the graph to set
	 * @uml.property  name="graph"
	 */
	public final void setGraph(GWizGraph gWizGraph) {
		graph = gWizGraph;
		edgesDescriptionHistory.clear();
		verticesFlagHistory.clear();
		verticesPredHistory.clear();
		verticesValuationHistory.clear();
		currentStepHistory.clear();
		commentsHistory.clear();
	}
	
	public abstract void setStartingVertex (GWizVertex startingVertex);
	
	protected void restorePreviousGraph() {	
		if (!verticesFlagHistory.isEmpty()){
			Vector<boolean[]> verticesFlag = verticesFlagHistory.pop();
			Vector<GWizVertex[]> verticesPred = verticesPredHistory.pop();
			Vector<double[]> verticesValuation = verticesValuationHistory.pop();
			Vector<Description> edgesDescription = edgesDescriptionHistory.pop();
			
			Iterator<GWizVertex> i = graph.vertexSet().iterator();
			int j = 0;
			while (i.hasNext()){
				GWizVertex v = i.next();
				v.setFixed(verticesFlag.get(j)[0]);
				v.setFixing(verticesFlag.get(j)[1]);
				v.setUpdated(verticesFlag.get(j)[2]);
				v.setHasPred(verticesFlag.get(j)[3]);
				v.setUpdatedDone(verticesFlag.get(j)[4]);
				v.setPred(verticesPred.get(j)[0]);
				v.setPreviousPred(verticesPred.get(j)[1]);
				
				v.setValuation(verticesValuation.get(j)[0]);
				v.setPreviousValuation(verticesValuation.get(j)[1]);
				
				j++;
			}
			
			Iterator<GWizEdge> e = graph.edgeSet().iterator();
			int k =0;
			while (e.hasNext()){
				e.next().setDescription(edgesDescription.get(k));
				k++;
			}
			currentStep = currentStepHistory.pop();
			comments = commentsHistory.pop();
		}
	}
	
	protected void saveGraph() {
		
		Vector<boolean[]> verticesFlag = new Vector<boolean[]>();
		Vector<GWizVertex[]> verticesPred = new Vector<GWizVertex[]>();
		Vector<double[]> verticesValuation = new Vector<double[]>();
		Vector<Description> edgesDescription = new Vector<Description>();
		
		Iterator<GWizVertex> i = graph.vertexSet().iterator();
		while (i.hasNext()){
			GWizVertex v = i.next();
			
			boolean[] t1 = new boolean[] {v.isFixed(), v.isFixing(), v.isUpdated(), v.hasPred(), v.isUpdatedDone()};
			verticesFlag.add(t1);
			
			GWizVertex[] t2 = new GWizVertex[] {v.getPred(), v.getPreviousPred()};
			verticesPred.add(t2);
			
			double[] t3 = new double[] {v.getValuation(), v.getPreviousValuation()};
			verticesValuation.add(t3);
		}
		
		verticesFlagHistory.add(verticesFlag);
		verticesPredHistory.add(verticesPred);
		verticesValuationHistory.add(verticesValuation);
		
		Iterator<GWizEdge> j = graph.edgeSet().iterator();
		while (j.hasNext())
			edgesDescription.add(j.next().getDescription());
		
		edgesDescriptionHistory.add(edgesDescription);
		
		currentStepHistory.add(Integer.valueOf(currentStep));
		commentsHistory.add(comments.toString());
	}

	public abstract boolean isRunnable();
	
	public String getComments(){
		return comments;
	}

	public GWizVertex getCurrentVertex() {
		return currentVertex;
	}

	protected void setCurrentVertex(GWizVertex currentVertex) {
		this.currentVertex = currentVertex;
	}

	protected GWizVertex getStartingVertex() {
		return startingVertex;
	}

	protected GWizVertex getEndVertex() {
		return endVertex;
	}

	protected void setComments(String comments) {
		this.comments = comments;
	}

}
