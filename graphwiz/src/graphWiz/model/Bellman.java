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
	private boolean temoin = false;
	
	public Bellman(GWizGraph graph) {
		super(graph);
		algo = new String[10];
		algo[0] = "<html><font size=6>Algorithme de Bellman</font><br><br><I><font size=3><U> Notations:</U>"+
				"<br>V[x] = valuation du sommet x</br>" +
				"<br>W(x,y) = poids de l'arc (x,y)</br>" +
				"<br> nbIter = nombre d'itérations</br>"+
				"<BR></font></I></html>";
		algo[1] =  "<html><font size=5><U> <br>Algorithme: <br></U></font></html>";
		algo[2] = "<html><font size=4><Br> Initialiser la valuation du sommet 0 à 0,</br>"+" <br>celle de tous les autres sommets à + &#8734 et nbIter = 0 </font></blockquote></html>";
		algo[3] = "<html><font size=4><br>Tant que les valuations sont modifiées d'une itération </br>"+"<br>sur l'autre ou que nbIter &lt N-1 "+"</font></html>";
		algo[4] = "<html><br><blockquote><font size=4>Incrémenter nbIter </font></blockquote></html>";
		algo[5] = "<html><br><blockquote><font size=4>Pour chaque sommet x de 0 à N-1</font></blockquote></html>";
		algo[6] = "<html><br><blockquote><blockquote><font size=4>V[x] = min(V[x],min(V[y]+W(y,x)) </br>"+"<br> <blockquote>y=pred(x)</blockquote>"+"</font></blockquote></blockquote></html>";
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
		boolean memeIteration = true;
		for(int i=0;i<graphe.size();i++){
			if(graphe.get(i).getValuation()!= graphe.get(i).getPreviousValuation()){
				memeIteration = false;
				currentStep = 8;
			}
		}
		return NbIteration == this.graphe.size()|| memeIteration;		
	}

	public boolean isStart() {
		return NbIteration == 0 && NbSommetExplore == 0;
	}

	public void nextStep() {
		
		if(!temoin && NbSommetExplore ==0 && currentStep!=3 && isStart() ){
			currentStep=2;
			temoin = true;
		}
		else{
		if(!isEnd()){
			if(NbSommetExplore<graphe.size()){
				
				GWizVertex v = selectVertex();
				//si v est en cours de mise à jour
				if(v.isFixing() && !v.isFixed()){
					this.UpdatePredecessorsOf(v);
				}
				//si v n'a pas encore été mis à jour
				else if(!v.isFixing() && !v.isFixed()){
					v.setFixing(true);
					Iterator<GWizEdge> j = graph.incomingEdgesOf(v).iterator();
	    	   	 	GWizEdge e;
	    	   	 	while (j.hasNext()){          	  	
	    	   	 		e = j.next();
	    	   	 		currentStep = 6;
	    	   	 		
	    	   	 		pred.addElement(graph.getEdgeSource(e));
	    	   	 	}
	    	   	 	
	    	   	 	this.UpdatePredecessorsOf(v);
				}
				
				//si v est mis à jour
				else if(v.isFixed()){
					
					Iterator<GWizEdge> j = graph.incomingEdgesOf(v).iterator();
	    	   	 	GWizEdge e;
	    	   	 	while (j.hasNext()){          	  	
	    	   	 		e = j.next();
	    	   	 		if(this.graph.getEdge(graph.getEdgeSource(e),v).getDescription()!= Description.EXPLORER)
	    	   	 			this.graph.getEdge(graph.getEdgeSource(e),v).setDescription(Description.REGULAR);
	    	   	 	}
	    	   	 	NbSommetExplore++;
	    	   	 	currentStep=5;
	    	   	 	System.out.println("NbSommet"+NbSommetExplore);
				}        
			}
			//si on a exploré tous les sommets, l'itération est terminée
			else{
				for(int i=0;i<graphe.size();i++){
					graphe.get(i).setFixed(false);
					graphe.get(i).setFixing(false);
				}
				NbIteration++;
				currentStep = 4;
				System.out.println("NbIter"+NbIteration);
				NbSommetExplore =0;
			}
		}
		else
			currentStep = 9;
		}
        saveGraph();    	
	}
	
	
	private GWizVertex selectVertex() {
		currentStep=5;
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
		this.graph.getEdge(x, v).setDescription(Description.SELECT);
		double valX = x.getValuation();
    	double poidsXV = graph.getEdgeWeight(this.graph.getEdge(x, v));
    	if (valX + poidsXV < v.getValuation() ) {
  		  v.setPreviousValuation(v.getValuation());
    	  v.setValuated(true);
  		  v.setValuation(valX + poidsXV);
  		  
  		  v.setPreviousPred(v.getPred());
  		  v.setPred(x);
  		  v.setUpdated(true);
  		  this.graph.getEdge(x,v).setDescription(Description.EXPLORER);                 
  	  }
    	else
    		this.graph.getEdge(x, v).setDescription(Description.EXPLORED);
		
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
			
			a.setHasPred(true);
			graphe.addElement(a);
			}
		graphe.get(0).setValuation(0);
		graphe.get(0).setPred(graphe.get(0));
		currentStep = 0;
	}

	@Override
	public String[] getAlgo() {
		return algo;
	}

}
