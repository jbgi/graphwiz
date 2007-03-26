package graphWiz.model;

import graphWiz.model.GWizEdge.Description;

import java.util.Iterator;
import java.util.Observable;
import java.util.Stack;
import java.util.Vector;
import org.jgraph.*;
import org.jgrapht.*;

public class Bellman extends Algorithm {
	
	private int NbIteration=0;
	
	public Bellman(GWizGraph graph) {
		super(graph);
	}
	
	public String checkGraph() {
		// TODO Auto-generated method stub
		// tout graphe est ok!
		return null;
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
		return this.verticesValuationHistory.lastIndexOf(this.verticesValuationHistory.lastElement())==0;
	}

	public void nextStep() {
		if(!isEnd()){
	            GWizVertex v = selectVertex();
	            if (v != null){
	            	v.setFixed(true);
	        		}
	            else{
	            	NbIteration++;
	            	Iterator<GWizVertex> i = graph.vertexSet().iterator();
	            	while (i.hasNext()){
        			GWizVertex vertex = i.next();
        			vertex.setFixed(false);
	            	}
	            }
	            saveGraph();    
		}
	}
	
	
	private GWizVertex selectVertex() {
		Iterator<GWizVertex> i = graph.vertexSet().iterator();
		GWizVertex selectedVertex = null, nextVertex;
		while (i.hasNext()){
			nextVertex = i.next();
			if (!nextVertex.isFixed()){
				selectedVertex = nextVertex;
			}
		}
		return selectedVertex;
	}
	
	
	public void UpdatePredecessorsOf(GWizVertex v){
		if (this.graph.containsVertex(v)){
  	   	  Iterator<GWizEdge> j = graph.incomingEdgesOf(v).iterator();
  	   	  GWizEdge e;
  	   	  GWizVertex x;
          while (j.hasNext()){          	  	
        	  e = j.next();
        	  x = graph.getEdgeSource(e);
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
		v.setFixed(true);
	}

	@Override
	public void setEndVertex(GWizVertex endVertex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStartingVertex(GWizVertex startingVertex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isRunnable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getAlgo() {
		// TODO Auto-generated method stub
		return null;
	}

}
