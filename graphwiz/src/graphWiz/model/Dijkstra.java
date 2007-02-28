package graphWiz.model;
import graphWiz.model.GWizEdge.Description;
import java.util.Iterator;
import java.util.Observable;
import java.util.Stack;
import java.util.Vector;

/**
 * @author  jbg
 */
public class Dijkstra extends Observable implements Algorithm {

	private Stack<Vector<Description>> edgesDescriptionHistory;
	
	private GWizGraph graph;
	
	private GWizVertex startingVertex;

	private Stack<Vector<boolean[]>> verticesFlagHistory;
	
	private Stack<Vector<GWizVertex[]>> verticesPredHistory;
	
	private Stack<Vector<double[]>> verticesValuationHistory;
	
	public Dijkstra() {
		edgesDescriptionHistory = new Stack<Vector<Description>>();
		verticesFlagHistory = new Stack<Vector<boolean[]>>();
		verticesPredHistory = new Stack<Vector<GWizVertex[]>>();
		verticesValuationHistory = new Stack<Vector<double[]>>();
	}

	public String checkGraph() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getAlgo() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getCurrentStep() {
		return 0;
	}

	public boolean isEligible() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean isEnd() {
		boolean allFixed = true;
		Iterator<GWizVertex> i = graph.vertexSet().iterator();
		while (i.hasNext() && !allFixed)
			allFixed = allFixed && i.next().isFixed();
		return allFixed;
	}

	public boolean isStart() {
		return !startingVertex.isFixed();
	}
	
	public void nextStep(){
		saveGraph();
		if (!isEnd()){
			GWizVertex selectedVertex = selectVertex();
			if (selectedVertex.isFixing())
				updateSuccessorOf(selectedVertex);
			else
				selectedVertex.setFixing(true);
			setChanged();
			notifyObservers();
		}
	}

	public void previousStep(){
		if(!isStart()){
			restorePreviousGraph();
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * @param graph  the graph to set
	 * @uml.property  name="graph"
	 */
	public void setGraph(GWizGraph gWizGraph) {
		graph = gWizGraph;
		edgesDescriptionHistory.clear();
		verticesFlagHistory.clear();
		verticesPredHistory.clear();
		verticesValuationHistory.clear();
	}

	/**
	 * @param startingVertex  the startingVertex to set
	 * @uml.property  name="startingVertex"
	 */
	public void setStartingVertex (GWizVertex startingVertex) {
		this.startingVertex = startingVertex;
	}

	private void checkAllSuccessorUpdated(GWizVertex vertex){
		Iterator<GWizEdge> i = graph.outgoingEdgesOf(vertex).iterator();
		boolean allUpdated = true;
		while (i.hasNext() && allUpdated){
			allUpdated = graph.getEdgeTarget(i.next()).isUpdated() && allUpdated;
		}
		if (allUpdated){
			vertex.fixeMe();
			Iterator<GWizEdge> j = graph.outgoingEdgesOf(vertex).iterator();
			while (j.hasNext()){
				GWizEdge edge = j.next();
				graph.getEdgeTarget(edge).setUpdated(false);
				edge.setDescription(Description.REGULAR);
			}
		}
	}

	private void restorePreviousGraph() {
		
		if (!verticesFlagHistory.isEmpty()){
			Vector<boolean[]> verticesFlag = verticesFlagHistory.pop();
			Vector<GWizVertex[]> verticesPred = verticesPredHistory.pop();
			Vector<double[]> verticesValuation = verticesValuationHistory.pop();
			Vector<Description> edgesDescription = edgesDescriptionHistory.pop();
			
			Iterator<GWizVertex> i = graph.vertexSet().iterator();
			while (i.hasNext()){
				int j = 0;
				GWizVertex v = i.next();
				v.setFixed(verticesFlag.get(j)[0]);
				v.setFixing(verticesFlag.get(j)[1]);
				v.setUpdated(verticesFlag.get(j)[2]);
				v.setHasPred(verticesFlag.get(j)[3]);
				
				v.setPred(verticesPred.get(j)[0]);
				v.setPreviousPred(verticesPred.get(j)[1]);
				
				v.setValuation(verticesValuation.get(j)[0]);
				v.setPreviousValuation(verticesValuation.get(j)[1]);
				
				j++;
			}
			
			Iterator<GWizEdge> j = graph.edgeSet().iterator();
			while (j.hasNext()){
				int k =0;
				j.next().setDescription(edgesDescription.get(k));
				k++;
			}
		}
	}

	private void saveGraph() {
		
		Vector<boolean[]> verticesFlag = new Vector<boolean[]>();
		Vector<GWizVertex[]> verticesPred = new Vector<GWizVertex[]>();
		Vector<double[]> verticesValuation = new Vector<double[]>();
		Vector<Description> edgesDescription = new Vector<Description>();
		
		Iterator<GWizVertex> i = graph.vertexSet().iterator();
		while (i.hasNext()){
			GWizVertex v = i.next();
			
			boolean[] t1 = {v.isFixed(), v.isFixing(), v.isUpdated(), v.hasPred()};
			verticesFlag.add(t1);
			
			GWizVertex[] t2 = {v.getPred(), v.getPreviousPred()};
			verticesPred.add(t2);
			
			double[] t3 = {v.getValuation(), v.getPreviousValuation()};
			verticesValuation.add(t3);
		}
		
		Iterator<GWizEdge> j = graph.edgeSet().iterator();
		while (j.hasNext())
			edgesDescription.add(j.next().getDescription());
	}

	private GWizVertex selectVertex() {
		GWizVertex selectedVertex = startingVertex;
		double minValuation = Double.POSITIVE_INFINITY;
		Iterator<GWizVertex> i = graph.vertexSet().iterator();
		GWizVertex nextVertex;
		while (i.hasNext()){
			nextVertex = i.next();
			if (nextVertex.isFixing())
				checkAllSuccessorUpdated(nextVertex);
			if (nextVertex.getValuation() < minValuation && !nextVertex.isFixed()){
				selectedVertex = nextVertex;
				minValuation = selectedVertex.getValuation();
			}
		}
		return selectedVertex;
	}

	private void updateSuccessorOf(GWizVertex vertex){
		Iterator<GWizEdge> i = graph.outgoingEdgesOf(vertex).iterator();
		GWizVertex succ;
		GWizEdge edge;
		boolean oneUpdate=false;
		while (i.hasNext() && !oneUpdate){
			edge = i.next();
		   	succ = graph.getEdgeTarget(edge);
		   	if (!succ.isFixed() && !succ.isUpdated() && vertex.getValuation() + graph.getEdgeWeight(edge) <= succ.getValuation()){
		   		succ.setValuation(vertex.getValuation()+graph.getEdgeWeight(edge));
		   		succ.setPred(vertex);
		   		succ.setUpdated(true);
				edge.setDescription(Description.EXPLORER);
		   	}
		}
	}
}
