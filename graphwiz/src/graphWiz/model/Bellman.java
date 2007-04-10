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
		comments = "vous avez choisi l'algorithme de Bellman, SELECTIONNEZ 0 AVANT DE COMMENCER";
		predecesseurs=new int[0];
		algo = new String[8];
		algo[0] = "<html><font size=6>Algorithme de Bellman</font><br><br><I><font size=3><U> Notations:</U>"+
				"<br>V[x] = valuation du sommet x</br>" +
				"<br>W(x,y) = poids de l'arc (x,y)</br>" +
				"<br> nbIter = nombre d'it&eacute;rations</br>"+
				"<BR></font></I></html>";
		algo[1] =  "<html><font size=5><U> <br>Algorithme: <br></U></font></html>";
		algo[2] = "<html><font size=4><Br> Initialiser la valuation du sommet 0 &agrave; 0,</br>"+" <br>celle de tous les autres sommets &agrave; +&#8734 et nbIter = 0 </font></blockquote></html>";
		algo[3] = "<html><font size=4><br>Tant que les valuations sont modifi&eacute;es d'une it&eacute;ration </br>"+"<br>sur l'autre ou que nbIter &lt N-1 "+"<br>Incr&eacute;menter nbIter"+"</font></html>";
		algo[4] = "<html><br><blockquote><font size=4>Pour chaque sommet x de 0 &agrave; N-1</font></blockquote></html>";
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
		//this.comm.setcomments("On démarre l'algorithme");
		boolean isStart = NbIteration == 0 && NbSommetExplore == 0;
		if (isStart) currentStep=2;
		return isStart;
	}

	public void nextStep() {
		saveGraph(); 
		if(!temoin && NbSommetExplore ==0 && currentStep!=3 && isStart() ){
			//this.comm.setcomments("le sommet 0 est selectionné");
			currentStep=4;
			temoin = true;
			comments = "On démarre l'algorithme";
		}
		else{
		if(!isEnd()){
			if(NbSommetExplore<graphe.size()){
				currentStep=3;
				GWizVertex v = selectVertex();
				comments = "le sommet "+v.getName()+" est sélectionné";
				v.setFixing(true);
				//si v n'a pas encore été mis à jour
				if(!v.isFixed()){
					currentStep=4;
					v.isFixing();
					Iterator<GWizEdge> j = getGraph().incomingEdgesOf(v).iterator();
	    	   	 	GWizEdge e;
	    	   	 	comments = "On sélectionne les prédecesseurs de "+v.getName();
	    	   	 	while (j.hasNext()){          	  	
	    	   	 		e = j.next();
	    	   	 		if(e.getDescription() != Description.SELECT)
	    	   	 			e.setDescription(Description.EXPLORED);
	    	   	 		pred.addElement(getGraph().getEdgeSource(e));
	    	   	 	}
	    	   	 	this.UpdatePredecessorsOf(v);
				}
	    	   	 	
				else{
					NbSommetExplore++;
	    	   	 	comments="On s&eacute;lectionne le sommet suivant      "+NbSommetExplore;
	    	   	 	
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
				comments="On a NbIter = "+NbIteration;
				NbSommetExplore=0;
				
			}
		}
		else{
			currentStep = 7;
			comments="La simulation est terminée,"+"\n"+" vous pouvez sélectionner un sommet pour connaitre le chemin optimal";
		}
		}
           	
	}
	
	
	private void clearGraphe() {
		currentStep = 3;
		Iterator<GWizVertex> i = getGraph().vertexSet().iterator();
		GWizVertex a = null;
		while (i.hasNext()){
			a = i.next();
			a.setValuated(true);
			a.setFixed(false);
			a.setFixing(false);
			a.setUpdated(false);
		}
		Iterator<GWizEdge> j = getGraph().edgeSet().iterator();
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
		if(this.getGraph().containsVertex(v) && !pred.isEmpty()){
			while(!pred.isEmpty()){
				x=pred.firstElement();
				comments = "on sélectionne le sommet "+x.getName();
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
		if(this.getGraph().containsEdge(this.getGraph().getEdge(x,v))){
			double valX = x.getValuation();
			double poidsXV = getGraph().getEdgeWeight(this.getGraph().getEdge(x, v));
			if (valX + poidsXV < v.getValuation() ) {
				comments="On a : "+valX +"+"+ poidsXV +"<"+ v.getValuation();
				Iterator<GWizEdge> j = getGraph().incomingEdgesOf(v).iterator();
    	   	 	GWizEdge e;
    	   	 	while (j.hasNext()){          	  	
    	   	 		e = j.next();
    	   	 		if(e.getDescription()==Description.SELECT || e.getDescription()==Description.REGULAR)
    	   	 			e.setDescription(Description.EXPLORED);
    	   	 		
    	   	 		pred.addElement(getGraph().getEdgeSource(e));
    	   	 	}
    	   	 	this.getGraph().getEdge(x,v).setDescription(Description.SELECT);
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
				Iterator<GWizEdge> j = getGraph().incomingEdgesOf(v).iterator();
	   	 		
				GWizEdge e;
	   	 		while (j.hasNext()){          	  	
	   	 			e = j.next();
	   	 			if(this.getGraph().getEdge(getGraph().getEdgeSource(e),v).getDescription()== Description.EXPLORER)
	   	 				this.getGraph().getEdge(getGraph().getEdgeSource(e),v).setDescription(Description.EXPLORED);
	   	 			
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
			Iterator<GWizEdge> e = getGraph().edgeSet().iterator();
			while (e.hasNext())
				e.next().setDescription(Description.EXPLORED);
			GWizVertex pred = endVertex;
			while (pred.hasPred()){
				getGraph().getEdge(pred.getPred(),pred).setDescription(Description.PATH);
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
		
		this.startingVertex = startingVertex;
		Iterator<GWizVertex> i = getGraph().vertexSet().iterator();
		GWizVertex a;
		graphe.clear();
		graphe.add(startingVertex);
		while (i.hasNext()){
			a = i.next();
			a.setValuated(true);
			a.setStart(false);
			a.setValuation(Float.POSITIVE_INFINITY);
			if (startingVertex!=a)
				graphe.addElement(a);
			}
		startingVertex.setValuation(0);
		startingVertex.setStart(true);
		currentStep = 2;
		predecesseurs = new int[graphe.size()];
		predecesseurs[0]=0;
		comments = "Vous pouvez démarrer la simulation";
	}

	@Override
	public boolean isRunnable() {
		return true;
	}

	@Override
	public void initialize() {
		comments="Initialisation : Cliquez sur le sommet 0 \n puis lancez la simulation!";
		currentStep=1;
		}

	@Override
	public String[] getAlgo() {
		return algo;
	}

}
