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
		return this.verticesValuationHistory.lastIndexOf(this.verticesValuationHistory.lastElement())==0;
	}

	public void nextStep() {
		if(!isEnd()){
	            GWizVertex v = selectVertex();
	            if (v != null){
	            	v.fixeMe();
	        		}
	            else{
	            	NbIteration++;
	            	Iterator<GWizVertex> i = graph.vertexSet().iterator();
	            	while (i.hasNext()){
        			GWizVertex vertex = i.next();
        			vertex.setFixed(false);
	            	}
	            }
	            setChanged();
	            notifyObservers();
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
		v.fixeMe();
	}

	public void previousStep() {
		if(!isStart()){
			restorePreviousGraph();
			setChanged();
			notifyObservers();
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

}
