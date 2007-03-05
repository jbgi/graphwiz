package graphWiz.model;

import graphWiz.model.GWizEdge.Description;

import java.util.Iterator;
import java.util.Observable;
import java.util.Stack;
import java.util.Vector;
import org.jgraph.*;
import org.jgrapht.*;

public class Bellman extends Observable implements Algorithm{

	private Stack<Vector<Description>> edgesDescriptionHistory;
	
	private GWizGraph graph;
	
	private Stack<Vector<boolean[]>> verticesFlagHistory;
	
	private Stack<Vector<GWizVertex[]>> verticesPredHistory;
	
	private Stack<Vector<double[]>> verticesValuationHistory;
	
	private int NbIteration=0;
	
	public Bellman() {
		edgesDescriptionHistory = new Stack<Vector<Description>>();
		verticesFlagHistory = new Stack<Vector<boolean[]>>();
		verticesPredHistory = new Stack<Vector<GWizVertex[]>>();
		verticesValuationHistory = new Stack<Vector<double[]>>();
	}
	
	public String checkGraph() {
		// TODO Auto-generated method stub
		// tout graphe est ok!
		return null;
	}

	public String[] getAlgo() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getCurrentStep() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isEligible() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEnd() {
		boolean isEnd = false;
		if(NbIteration== this.verticesValuationHistory.capacity() || 
				verticesValuationHistory.get(verticesValuationHistory.capacity()).equals(verticesValuationHistory.get(verticesValuationHistory.capacity()-1))) isEnd=true;
		return isEnd;
	}

	public boolean isStart() {
		return this.verticesValuationHistory.capacity()==1;
	}

	public void nextStep() {
		if(!isEnd()){
	           NbIteration++;
	           Iterator<GWizVertex> i = graph.vertexSet().iterator();
	           while (i.hasNext()){
	        	   	  GWizVertex v = i.next();
	                  Iterator<GWizEdge> j = graph.incomingEdgesOf(v).iterator();
	                  while (j.hasNext()){
	                	  	GWizEdge e = j.next();
	                	  	GWizVertex x = graph.getEdgeSource(e);
	                        double valX = x.getValuation();
	                        double poidsXV = graph.getEdgeWeight(e);
	                        if (valX + poidsXV < v.getValuation() ) {
	                                 v.setValuation(valX + poidsXV);
	                                 v.setPred(x);
	                                 v.setUpdated(true);
	                                 e.setDescription(Description.EXPLORER);
	                        }
	                  } 
	           }
	   }
	} 
		


	public void previousStep() {
		if(!isStart()){
			restorePreviousGraph();
			setChanged();
			notifyObservers();
		}
		
	}
	
	private void restorePreviousGraph() {
		// TODO Auto-generated method stub
		
	}

}
