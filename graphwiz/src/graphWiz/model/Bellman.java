package graphWiz.model;

import graphWiz.model.GWizEdge.Description;

import java.util.Iterator;
import java.util.Vector;

public class Bellman extends Algorithm {
	
	private int NbSommetExplore=0;
	private int NbIteration = 0;
	private String[] algo;
	private Vector<GWizVertex> graphe = new Vector<GWizVertex>();
	private Vector<GWizVertex> pred = new Vector<GWizVertex>();
	private boolean temoin = false;
	boolean memeIteration = false;
	
	
	public Bellman(GWizGraph graph) {
		super(graph);
		algo = new String[8];
		algo[0] = "<html><font size=6>Algorithme de Bellman</font><br><br><I><font size=3><U> Notations:</U>"+
				"<br>V[x] = valuation du sommet x</br>" +
				"<br>W(x,y) = poids de l'arc (x,y)</br>" +
				"<br> nbIter = nombre d'it�rations</br>"+
				"<BR></font></I></html>";
		algo[1] =  "<html><font size=5><U> <br>Algorithme: <br></U></font></html>";
		algo[2] = "<html><font size=4><Br> Initialiser la valuation du sommet 0 � 0,</br>"+" <br>celle de tous les autres sommets � + &#8734 et nbIter = 0 </font></blockquote></html>";
		algo[3] = "<html><font size=4><br>Tant que les valuations sont modifi�es d'une it�ration </br>"+"<br>sur l'autre ou que nbIter &lt N-1 "+"<br>Incr�menter nbIter"+"</font></html>";
		algo[4] = "<html><br><blockquote><font size=4>Pour chaque sommet x de 0 � N-1</font></blockquote></html>";
		algo[5] = "<html><br><blockquote><blockquote><font size=4>V[x] = min(V[x],min(V[y]+W(y,x)) </br>"+"<br> <blockquote>y=pred(x)</blockquote>"+"</font></blockquote></blockquote></html>";
		algo[6] = "<html><br><blockquote><font size=4>Fin Pour</font></blockquote></html>";
		algo[7] = "<html><br><font size=4>Fin Tant que</font></html>";
		
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
		boolean isEnd = memeIteration || NbIteration == this.graphe.size()-1;
		if (isEnd) currentStep=7;
		System.out.println(NbIteration);
		return  isEnd;		
	}

	public boolean isStart() {
		boolean isStart = NbIteration == 0 && NbSommetExplore == 0;
		if (isStart) currentStep=2;
		return isStart;
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
				//si v n'a pas encore �t� mis � jour
				if(!v.isFixed()){
					Iterator<GWizEdge> j = graph.incomingEdgesOf(v).iterator();
	    	   	 	GWizEdge e;
	    	   	 	while (j.hasNext()){          	  	
	    	   	 		e = j.next();
	    	   	 		pred.addElement(graph.getEdgeSource(e));
	    	   	 	}
	    	   	 	this.UpdatePredecessorsOf(v);
				}
	    	   	 	
				else{
//	    	   	 	Iterator<GWizEdge> iterator = graph.incomingEdgesOf(v).iterator();
	//    	   	 	GWizEdge edge;
//	    	   	 	while (iterator.hasNext()){          	  	
	//    	   	 		edge = iterator.next();
	  // 	   	 			this.graph.getEdge(graph.getEdgeSource(edge),v).setDescription(Description.REGULAR);
	    //	   	 	}
	    	   	 	
	    	   	 	NbSommetExplore++;
	    	   	 	currentStep=5;
	    	   	 	System.out.println("NbSommet"+NbSommetExplore);
					
	    	   	 	}
	    	   	 	}        
			
			//si on a explor� tous les sommets, l'it�ration est termin�e
			else{
				memeIteration = true;
				for(int i=0;i<this.graphe.size();i++){
					for(int j =0;j<this.graphe.size();j++){
					if(this.graph.containsEdge(this.graphe.get(i),this.graphe.get(j))){
							if(this.graph.getEdge(this.graphe.get(i),this.graphe.get(j)).getDescription()==Description.PATH){
								System.out.println("il y a un changement");
								memeIteration=false;}}
					}
				}
				clearGraphe();
				NbIteration++;
				System.out.println("NbIter"+NbIteration);
				NbSommetExplore =0;
			}
		}
		else
			currentStep = 9;
		}
        saveGraph();    	
	}
	
	
	private void clearGraphe() {
		currentStep = 3;
		Iterator<GWizVertex> i = graph.vertexSet().iterator();
		GWizVertex a = null;
		while (i.hasNext()){
			a = i.next();
			a.setValuated(true);
			a.setFixed(false);
			a.setFixing(false);
			a.setUpdated(false);
			}
		Iterator<GWizEdge> j = graph.edgeSet().iterator();
		GWizEdge b = null;
		while(j.hasNext()){
			b = j.next();
			b.setDescription(Description.REGULAR);
		}
	}

	private GWizVertex selectVertex() {
		currentStep=5;
		return graphe.elementAt(NbSommetExplore);
	}
	
	
	public void UpdatePredecessorsOf(GWizVertex v){
		GWizVertex x =new GWizVertex(null);
		currentStep = 6;
		if(this.graph.containsVertex(v) && !pred.isEmpty()){
			for(int i=0;i<pred.size();i++){
				this.graph.getEdge(pred.get(i),v).setDescription(Description.EXPLORER);
			}
			while(!pred.isEmpty()){
				x=pred.firstElement();
				x.setUpdated(true);
				updatePred(x,v);
				pred.removeElement(x);
			}
		}
		v.setFixed(true);	
        updatePred(x,v);        	
		x.setUpdated(false);
		
	}

	private void updatePred(GWizVertex x, GWizVertex v) {
		//boolean IsPath = false;
		if(this.graph.containsEdge(this.graph.getEdge(x,v))){
		double valX = x.getValuation();
    	double poidsXV = graph.getEdgeWeight(this.graph.getEdge(x, v));
    	
    	if (valX + poidsXV < v.getValuation() ) {
    		this.graph.getEdge(x,v).setDescription(Description.PATH);
    		v.setPreviousValuation(v.getValuation());
    		v.setValuated(true);
  		  	v.setValuation(valX + poidsXV);
  		  	v.setPreviousPred(v.getPred());
  		  	v.setPred(x);
  		  	v.setUpdated(true);
 		  
  		                   
  	  }
    	else{
    		Iterator<GWizEdge> j = graph.incomingEdgesOf(v).iterator();
	   	 	GWizEdge e;
    		while (j.hasNext()){          	  	
	   	 		e = j.next();
	   	 		if(this.graph.getEdge(graph.getEdgeSource(e),v).getDescription()== Description.EXPLORER)
	   	 			this.graph.getEdge(graph.getEdgeSource(e),v).setDescription(Description.REGULAR);
	   	 	}
    		//this.graph.getEdge(x, v).setDescription(Description.EXPLORED);
    		
    	}
		
	}}

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
