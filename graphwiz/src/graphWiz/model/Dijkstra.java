package graphWiz.model;
import graphWiz.model.GWizEdge.Description;

import java.util.Iterator;

public class Dijkstra extends GraphManipulator implements Algorithm {

	public GWizVertex startingVertex;
	
	public Dijkstra() {
	}

	private void checkAllSuccessorUpdated(GWizVertex vertex){
		Iterator<GWizEdge> i = getGraph().outgoingEdgesOf(vertex).iterator();
		boolean allUpdated = true;
		while (i.hasNext() && allUpdated){
			allUpdated = getGraph().getEdgeTarget(i.next()).isUpdated() && allUpdated;
		}
		if (allUpdated){
			vertex.fixeMe();
			Iterator<GWizEdge> j = getGraph().outgoingEdgesOf(vertex).iterator();
			while (j.hasNext()){
				GWizEdge edge = j.next();
				getGraph().getEdgeTarget(edge).setUpdated(false);
				edge.setDescription(Description.REGULAR);
			}
		}
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
	
	public GWizVertex getStartingVertex() {
		return startingVertex;
	}

	public boolean isEligible() {
		// TODO Auto-generated method stub
		return false;
	}  

	public boolean isEnd() {
		boolean allFixed = true;
		Iterator<GWizVertex> i = getGraph().vertexSet().iterator();
		while (i.hasNext() && !allFixed)
			allFixed = allFixed && i.next().isFixed();
		return allFixed;
	}

	public boolean isStart() {
		return !getStartingVertex().isFixed();
	}
	
	public void nextStep(){
		saveGraph();
		if (!isEnd()){
			GWizVertex selectedVertex = selectVertex();
			if (selectedVertex.isFixing())
				updateSuccessorOf(selectedVertex);
			else
				selectedVertex.setFixing(true);
		}
		//this.
	}

	public void previousStep(){
		if(!isStart())
			restorePreviousGraph();
		}

	private GWizVertex selectVertex() {
		GWizVertex selectedVertex = startingVertex;
		double minValuation = Double.POSITIVE_INFINITY;
		Iterator<GWizVertex> i = getGraph().vertexSet().iterator();
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

	public void setStartingVertex (GWizVertex startingVertex) {
		this.startingVertex = startingVertex;
	}

	private void updateSuccessorOf(GWizVertex vertex){
		Iterator<GWizEdge> i = getGraph().outgoingEdgesOf(vertex).iterator();
		GWizVertex succ;
		GWizEdge edge;
		boolean oneUpdate=false;
		while (i.hasNext() && !oneUpdate){
			edge = i.next();
		   	succ = getGraph().getEdgeTarget(edge);
		   	if (!succ.isFixed() && !succ.isUpdated() && vertex.getValuation() + getGraph().getEdgeWeight(edge) <= succ.getValuation()){
		   		succ.setValuation(vertex.getValuation()+getGraph().getEdgeWeight(edge));
		   		succ.setPred(vertex);
		   		succ.setUpdated(true);
				edge.setDescription(Description.EXPLORER);
		   	}
		}
	}
}
