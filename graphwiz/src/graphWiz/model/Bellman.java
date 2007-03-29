package graphWiz.model;

import graphWiz.model.GWizEdge.Description;

import java.util.Iterator;
import java.util.Observable;
import java.util.Stack;
import java.util.Vector;
import org.jgraph.*;
import org.jgrapht.*;

public class Bellman extends Algorithm {
	
	private int NbSommetExplore=0;
	private int NbIteration = 0;
	private String[] algo;
	private Vector<GWizVertex> graphe = new Vector<GWizVertex>();
	private Vector<GWizVertex> pred = new Vector<GWizVertex>();
	
	public Bellman(GWizGraph graph) {
		super(graph);
		algo = new String[10];
		algo[0] = "<html><font size=6>Algorithme de Bellman</font><br><br><I><font size=3><U> Notations:</U>"+
				"<br>V[x] = valuation du sommet x</br>" +
				"<br>W(x,y) = poids de l'arc (x,y)</br>" +
				"<br> nbIter = nombre d'itérations</br>"+
				"<BR></font></I></html>";
		algo[1] =  "<html><font size=5><U> <br>Algorithme: <br></U></font></html>";
		algo[2] = "<html><font size=4><Br> Initialiser la valuation du sommet 0 à 0, <br>celle de tous les autres sommets à + &#8734 et nbIter = 0 </font></blockquote></html>";
		algo[3] = "<html><font size=4><br>Tant que les valuations sont modifiées d'une itération sur l'autre ou que nbIter&ne;N-1 </font></html>";
		algo[4] = "<html><br><blockquote><font size=4>Incrémenter nbIter </font></blockquote></html>";
		algo[5] = "<html><br><blockquote><font size=4>Pour chaque sommet x de 0 à N-1</font></blockquote></html>";
		algo[6] = "<html><br><blockquote><blockquote><font size=4>V[x] = min(V[x],min(V[y]+W(y,x), y=pred(x)))</font></blockquote></blockquote></html>";
		algo[7] = "<html><br><blockquote><font size=4>Fin Pour</font></blockquote></html>";
		algo[8] = "<html><br><font size=4>Fin Tant que</font></html>";
		algo[9] = "<html><br><font size=4>Fin de l'algorithme</font></html>";
		
	}
	
	public String checkGraph() {
		// TODO Auto-generated method stub
		// tout graphe est ok!
		return null;
	}

	public boolean isEligible() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isEnd() {
		
		return NbIteration == this.graphe.size();
		
	}

	public boolean isStart() {
		return this.verticesValuationHistory.lastIndexOf(this.verticesValuationHistory.lastElement())==0;
	}

	public void nextStep() {
		if(!isEnd()){
			if(NbSommetExplore<graphe.size()){
				saveGraph();
				GWizVertex v = selectVertex();
				if(v.isFixing() && !v.isFixed()){
					this.UpdatePredecessorsOf(v);
				}
				else if(!v.isFixing() && !v.isFixed()){
					v.setFixing(true);
					Iterator<GWizEdge> j = graph.incomingEdgesOf(v).iterator();
	    	   	 	GWizEdge e;
	    	   	 	GWizVertex x;
	    	   	 	while (j.hasNext()){          	  	
	    	   	 		e = j.next();
	    	   	 		e.setDescription(Description.SELECT);
	    	   	 		pred.addElement(graph.getEdgeSource(e));
	    	   	 	}
	    	   	 	this.UpdatePredecessorsOf(v);
				}
				else if(v.isFixed()){
					Iterator<GWizEdge> j = graph.incomingEdgesOf(v).iterator();
	    	   	 	GWizEdge e;
	    	   	 	GWizVertex x;
	    	   	 	while (j.hasNext()){          	  	
	    	   	 		e = j.next();
	    	   	 		if(this.graph.getEdge(graph.getEdgeSource(e),v).getDescription()!= Description.EXPLORER)
	          	  		this.graph.getEdge(graph.getEdgeSource(e),v).setDescription(Description.REGULAR);
	    	   	 	}
	    	   	 	currentStep = 5;
	    	   	 	NbSommetExplore++;
	    	   	 	System.out.println("NbSommet"+NbSommetExplore);
				}        
			}
			else{
				currentStep = 4;
				NbIteration++;
				System.out.println("NbIter"+NbIteration);
				NbSommetExplore =0;
				for(int i=0;i<graphe.size();i++){
					graphe.get(i).setFixed(false);
					graphe.get(i).setFixing(false);
				}
			}
		}
		
        saveGraph();    
		
	}
	
	
	private GWizVertex selectVertex() {
		currentStep=4;
		return graphe.elementAt(NbSommetExplore);
	}
	
	
	public void UpdatePredecessorsOf(GWizVertex v){
		GWizVertex x =new GWizVertex(null);
		if (this.graph.containsVertex(v) && !pred.isEmpty()){
			x = pred.firstElement();
			x.setUpdated(true);
        	this.graph.getEdge(x,v).setDescription(Description.SELECT);
        	currentStep = 6;
        	updatePred(x,v);        	
		}  
		else
			v.setFixed(true);
		pred.removeElement(x);
		x.setUpdated(false);
		
	}

	private void updatePred(GWizVertex x, GWizVertex v) {
		double valX = x.getValuation();
    	double poidsXV = graph.getEdgeWeight(this.graph.getEdge(x, v));
    	if (valX + poidsXV < v.getValuation() ) {
  		  v.setValuated(true);
  		  v.setValuation(valX + poidsXV);
  		  v.setHasPred(true);
  		  v.setPred(x);
  		  v.setUpdated(true);
  		  this.graph.getEdge(x,v).setDescription(Description.EXPLORER);                 
  	  }
		
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
		return true;
	}

	@Override
	public void initialize() {
		Iterator<GWizVertex> i = graph.vertexSet().iterator();
		GWizVertex a = null;
		while (i.hasNext()){
			a = i.next();
			a.setValuated(true);
			a.setValuation(Float.POSITIVE_INFINITY);
			graphe.addElement(a);
			}
		graphe.get(0).setValuation(0);
		currentStep = 0;
	}

	@Override
	public String[] getAlgo() {
		return algo;
	}

}
