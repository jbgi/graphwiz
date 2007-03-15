package graphWiz.model;

import java.util.Iterator;
import java.util.Vector;

public class Floyd extends Algorithm{
	
	private int NbIteration=0;
	public boolean[][] CouplesOfCurrentVertex; 
	public Vector<GWizVertex> predecessors;
	public Vector<GWizVertex> successors;
	
	public Floyd() {
		super();
		successors = new Vector<GWizVertex>();
		predecessors = new Vector<GWizVertex>();
		CouplesOfCurrentVertex = new boolean[0][0];
		CouplesOfCurrentVertex[0][0]= false;
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
		return NbIteration == this.verticesValuationHistory.capacity();
		
	}

	public boolean isStart() {
		return NbIteration == 0;
	}

	public void nextStep() {
		if(!isEnd()){
			GWizVertex courant = selectVertex();
			if(courant.isFixing()){
				UpdateOneCouple(courant);
			}
			else{
			NbIteration++;
			UpdateOneCouple(courant);
			}
			saveGraph();
		}
	}
	
	
	private void UpdateOneCouple(GWizVertex courant) {
		if(!courant.isFixing()){
			//création des vecteurs de prédécesseurs x et successeurs y, ainsi que le tableau donnant
			//l'existence de V(x,y) :
			Iterator<GWizEdge> entrants = this.graph.incomingEdgesOf(courant).iterator();
			Iterator<GWizEdge> sortants = this.graph.outgoingEdgesOf(courant).iterator();
			while(entrants.hasNext()){
				predecessors.add(this.graph.getEdgeSource(entrants.next()));
			}
			while(sortants.hasNext()){
				successors.add(this.graph.getEdgeTarget(sortants.next()));
			}
			
			CouplesOfCurrentVertex = new boolean[predecessors.capacity()][successors.capacity()];
			for(int pred=0;pred<CouplesOfCurrentVertex.length;pred++){
				for(int succ=0;succ<CouplesOfCurrentVertex[0].length;succ++){
					if(this.graph.containsEdge(predecessors.get(pred),successors.get(succ))){
						CouplesOfCurrentVertex[pred][succ]= true;
					}
					else CouplesOfCurrentVertex[pred][succ]= false;
				}
			}
			
			courant.setFixing(true);
		}
		
		int pred = 0, succ = 0,p=0,s=0;
		//recherche du couple
		while(!CouplesOfCurrentVertex[pred][succ] || p<CouplesOfCurrentVertex.length){
			while(!CouplesOfCurrentVertex[pred][succ] || s<CouplesOfCurrentVertex[0].length){
				s++;
			}
			pred = p;
			succ = s;
			p++;
			s=0;
		}
		double PoidsPred = this.graph.getEdgeWeight(this.graph.getEdge(predecessors.get(pred), courant));
		double PoidsSucc = this.graph.getEdgeWeight(this.graph.getEdge(courant,successors.get(succ)));
		double PoidsV = this.graph.getEdgeWeight(this.graph.getEdge(predecessors.get(pred), successors.get(succ)));
		if(PoidsPred+PoidsSucc<PoidsV){
			this.graph.setEdgeWeight(this.graph.getEdge(predecessors.get(pred), successors.get(succ)), PoidsSucc+PoidsPred);
		}
		//suppression du couple
		CouplesOfCurrentVertex[pred][succ]=false;
		
		//fixation du sommet s'il n'y a plus de mise à jour possible
		if(CheckPasMiseAJour(CouplesOfCurrentVertex)){
			courant.fixeMe();
		}
					
	}

	private static boolean CheckPasMiseAJour(boolean[][] u) {
		boolean check = true;
		
		for(int pred=0;pred<u.length;pred++){
			for(int succ=0;succ<u[0].length;succ++){
				if(u[pred][succ]) check=false;
			}	
		}
		return check;
	}

	//faux: à corriger
	private GWizVertex selectVertex() {
		Iterator<GWizVertex> i =this.graph.vertexSet().iterator();
		GWizVertex current = new GWizVertex(null);
		while(i.hasNext() || !current.isFixing()){
			current =i.next();
		}
		if(!current.isFixing()){
			Iterator<GWizVertex> j =this.graph.vertexSet().iterator();
			while(j.hasNext() || current.isFixed()){
				current =j.next();
			}
		}
		return current;
	}
	
	
	public void UpdatePredecessorsOf(GWizVertex v){
		
	}

	public void previousStep() {
		if(!isStart()){
			restorePreviousGraph();
		}
		
	}

}
