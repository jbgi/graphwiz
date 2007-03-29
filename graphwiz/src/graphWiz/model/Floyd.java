package graphWiz.model;

import java.util.Iterator;
import java.util.Vector;

public class Floyd extends Algorithm{
	
	private int NbIteration=0;
	public boolean[][] CouplesOfCurrentVertex; 
	public Vector<GWizVertex> predecessors;
	public Vector<GWizVertex> successors;
	private String[] algo;
	public Vector<Vector> Val = new Vector<Vector>();
	
	public Floyd(GWizGraph graph) {
		super(graph);
		successors = new Vector<GWizVertex>();
		predecessors = new Vector<GWizVertex>();
		CouplesOfCurrentVertex = new boolean[1][1];
		CouplesOfCurrentVertex[0][0]= false;
		algo = new String[13];
		algo[0] = "<html><font size=5>Algorithme de Floyd</font><br><br><I><font size=3><U> Notations:</U>"+
				"<br><font size=2>V[x,y] = valuation du plus court chemin pour aller de x à y </br>"+"<br> par les sommets intermédiaires {1,2,..,k} </br>" +
				"<br><font size=2>W(x,y) = poids de l'arc (x,y) (infini s'il n'existe pas)</br>" +
				"<br></font></I></html>";
		algo[1] =  "<html><font size=4><br><U> Algorithme:</U></br></font></html>";
		algo[2] =  "<html><font size= 3 color=#408080> <I> // Initialisation de la matrice V<sup>0</sup> : </I></font>"+"<br> Pour tout sommet i de 0 à N, </br>"+
				"<br><blockquote>Pour tout sommet j de 0 à N </blockquote>"+"<blockquote><blockquote> V<sup>0</sup>[i,j]=W(i,j)</blockquote></blockquote>"+"<blockquote>Fin Pour</blockquote>"+"Fin Pour"+"<br></html>";
		algo[3] = "<html><font size=3 color=#408080><I> //Calcul des matrices successives V<sup>k</sup></font></html>";
		algo[4] = "<html>Pour chaque sommet k de 1 à N </html>";
		algo[5] = "<html><blockquote>Pour chaque sommet i de 0 à N-1</blockquote></html>";
		algo[6] = "<html><blockquote><blockquote>Pour chaque sommet j de 0 à N-1</blockquote></blockquote></html>";
		algo[7] = "<html><blockquote><blockquote><blockquote>Si V<sup>k-1</sup>[i,k]+V<sup>k-1</sup>[k,j] &lt V<sup>k-1</sup>[i,j]"+"<br>alors V<sup>k</sup>[i,j] = V<sup>k-1</sup>[i,k]+V<sup>k-1</sup>[k,j]"+"</blockquote></html>";
		algo[8] = "<html><blockquote><blockquote><blockquote>Fin Si</blockquote></blockquote></blockquote></html>";
		algo[9] = "<html><blockquote><blockquote>Fin Pour</blockquote></blockquote></html>";
		algo[10] = "<html><blockquote>Fin Pour </blockquote></html>";
		algo[11]= "<html>Fin Pour </html>";
		algo[12]= "<html>Fin de l'algorithme </html>";
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
			courant.setFixed(true);
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
		Iterator<GWizVertex> i = graph.vertexSet().iterator();
		GWizVertex a = null;
		
		while (i.hasNext()){
			a = i.next();
			a.setValuated(true);
			a.setValuation(Float.POSITIVE_INFINITY);
			
			a.setHasPred(true);
			predecessors.addElement(a);
			successors.add(a);
			}
		for(int k=0;k<predecessors.size();k++){
			Val.add(new Vector());
			for(int j=0;j<successors.size();j++){
				Val.get(k).add(this.graph.getEdgeWeight(this.graph.getEdge(predecessors.get(k), successors.get(j))));
			}
		}
		currentStep = 0;
		
	}

	@Override
	public String[] getAlgo() {
		// TODO Auto-generated method stub
		return algo;
	}

}
