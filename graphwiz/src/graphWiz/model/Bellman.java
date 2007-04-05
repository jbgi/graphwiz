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
	private boolean temoin = false, fin = false;
	boolean memeIteration = true;
	private int[] predecesseurs;
	private GWizVertex endVertex;
	
	public Bellman(GWizGraph graph) {
		super(graph);
		predecesseurs=new int[0];
		algo = new String[8];
		algo[0] = "<html><font size=6>Algorithme de Bellman</font><br><br><I><font size=3><U> Notations:</U>"+
				"<br>V[x] = valuation du sommet x</br>" +
				"<br>W(x,y) = poids de l'arc (x,y)</br>" +
				"<br> nbIter = nombre d'itérations</br>"+
				"<BR></font></I></html>";
		algo[1] =  "<html><font size=5><U> <br>Algorithme: <br></U></font></html>";
		algo[2] = "<html><font size=4><Br> Initialiser la valuation du sommet 0 à 0,</br>"+" <br>celle de tous les autres sommets à + &#8734 et nbIter = 0 </font></blockquote></html>";
		algo[3] = "<html><font size=4><br>Tant que les valuations sont modifiées d'une itération </br>"+"<br>sur l'autre ou que nbIter &lt N-1 "+"<br>Incrémenter nbIter"+"</font></html>";
		algo[4] = "<html><br><blockquote><font size=4>Pour chaque sommet x de 0 à N-1</font></blockquote></html>";
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
		boolean isEnd = fin || NbIteration == this.graphe.size()-1;
		if (isEnd) currentStep=7;
		else currentStep=3;
		System.out.println(NbIteration);
		return  isEnd;		
	}

	public boolean isStart() {
		//this.comm.setCommentaires("On démarre l'algorithme");
		boolean isStart = NbIteration == 0 && NbSommetExplore == 0;
		if (isStart) currentStep=2;
		return isStart;
	}

	public void nextStep() {
		saveGraph(); 
		if(!temoin && NbSommetExplore ==0 && currentStep!=3 && isStart() ){
			//this.comm.setCommentaires("le sommet 0 est selectionné");
			currentStep=4;
			temoin = true;
		}
		else{
		if(!isEnd()){
			if(NbSommetExplore<graphe.size()){
				currentStep=3;
				GWizVertex v = selectVertex();
				//this.comm.setCommentaires("le sommet "+v.getName()+" est selectionné");
				v.setFixing(true);
				//si v n'a pas encore été mis à jour
				if(!v.isFixed()){
					currentStep=4;
					v.isFixing();
					Iterator<GWizEdge> j = graph.incomingEdgesOf(v).iterator();
	    	   	 	GWizEdge e;
	    	   	 	while (j.hasNext()){          	  	
	    	   	 		e = j.next();
	    	   	 		if(e.getDescription() != Description.SELECT)
	    	   	 			e.setDescription(Description.EXPLORED);
	    	   	 		pred.addElement(graph.getEdgeSource(e));
	    	   	 	}
	    	   	 	this.UpdatePredecessorsOf(v);
				}
	    	   	 	
				else{
					NbSommetExplore++;
	    	   	 	//comm.setCommentaires("On sélectionne le sommet suivant"+NbSommetExplore);
	    	   	 	System.out.println("NbSommet"+NbSommetExplore);
				}
   	   	 	}        
			
			//si on a exploré tous les sommets, l'itération est terminée
			else{
				currentStep=6;
				if(memeIteration)
					fin=true;
				memeIteration = true;
				clearGraphe();
				NbIteration++;
				//this.comm.setCommentaires("NbIter = "+NbIteration);
				NbSommetExplore=0;
				System.out.println("NbIter"+NbIteration);
				
			}
		}
		else
			currentStep = 7;
		}
           	
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
			if(b.getDescription()!=Description.SELECT)
				b.setDescription(Description.REGULAR);
		}
		
	}

	private GWizVertex selectVertex() {
		currentStep=4;
		return graphe.elementAt(NbSommetExplore);
	}
	
	
	public void UpdatePredecessorsOf(GWizVertex v){
		GWizVertex x =new GWizVertex(null);
		currentStep = 5;
		if(this.graph.containsVertex(v) && !pred.isEmpty()){
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
		if(this.graph.containsEdge(this.graph.getEdge(x,v))){
			double valX = x.getValuation();
			double poidsXV = graph.getEdgeWeight(this.graph.getEdge(x, v));
			if (valX + poidsXV < v.getValuation() ) {
				Iterator<GWizEdge> j = graph.incomingEdgesOf(v).iterator();
    	   	 	GWizEdge e;
    	   	 	while (j.hasNext()){          	  	
    	   	 		e = j.next();
    	   	 		if(e.getDescription()==Description.SELECT || e.getDescription()==Description.REGULAR)
    	   	 			e.setDescription(Description.EXPLORED);
    	   	 		
    	   	 		pred.addElement(graph.getEdgeSource(e));
    	   	 	}
    	   	 	this.graph.getEdge(x,v).setDescription(Description.SELECT);
    	   	 	memeIteration=false;
				v.setPreviousValuation(v.getValuation());
				v.setValuated(true);
  		  		v.setValuation(valX + poidsXV);
  		  		v.setPreviousPred(v.getPred());
  		  		predecesseurs[Integer.parseInt(v.getName())]=Integer.parseInt(x.getName());
  		  		v.setPred(x);
  		  		v.setUpdated(true);     
			}
			else{
				Iterator<GWizEdge> j = graph.incomingEdgesOf(v).iterator();
	   	 		GWizEdge e;
	   	 		while (j.hasNext()){          	  	
	   	 			e = j.next();
	   	 			if(this.graph.getEdge(graph.getEdgeSource(e),v).getDescription()== Description.EXPLORER)
	   	 				this.graph.getEdge(graph.getEdgeSource(e),v).setDescription(Description.EXPLORED);
	   	 			
	   	 			}
    		   	}
			}
		}

	@Override
	public void setEndVertex(GWizVertex endVertex) {
		if (endVertex!=null) {
			if (this.endVertex == null || !this.endVertex.isEnd())
				saveGraph();
			else
				this.endVertex.setEnd(false);
			this.endVertex=endVertex;
			endVertex.setEnd(true);
			Iterator<GWizEdge> e = graph.edgeSet().iterator();
			while (e.hasNext())
				e.next().setDescription(Description.EXPLORED);
			GWizVertex pred = endVertex;
			while (pred.hasPred()){
				graph.getEdge(pred.getPred(), pred).setDescription(Description.PATH);
				pred = pred.getPred();
			}
		}
		else if (this.endVertex != null )
			if (this.endVertex.isEnd()){
				restorePreviousGraph();
				this.endVertex.setEnd(false);
			}
		
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
		currentStep = 2;
		predecesseurs = new int[graphe.size()];
		predecesseurs[0]=0;
	}

	@Override
	public String[] getAlgo() {
		return algo;
	}

}
