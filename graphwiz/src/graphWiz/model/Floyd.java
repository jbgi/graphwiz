package graphWiz.model;

import graphWiz.model.GWizEdge.Description;

import java.util.Iterator;
import java.util.Vector;

public class Floyd extends Algorithm{
	
	private int NbIteration=0, SommetDepart=0, SommetArrivee=0;
	private String[] algo;
	private Vector<GWizVertex> graphe = new Vector<GWizVertex>();
	public double[][] Val, PreviousVal;
	public String[][] Pred;
	private GWizVertex endVertex;
	private GWizVertex startingVertex;
	
	
	public Floyd(GWizGraph graph) {
		super(graph);
		commentaires="Vous avez choisi l'algorithme de Floyd";
		Val = new double[this.graph.vertexSet().size()][this.graph.vertexSet().size()];
		PreviousVal = new double[this.graph.vertexSet().size()][this.graph.vertexSet().size()];
		Pred = new String[this.graph.vertexSet().size()][this.graph.vertexSet().size()];
		algo = new String[11];
		algo[0] = "<html><font size=5>Algorithme de Floyd</font><br><br><I><font size=3><U> Notations:</U>"+
				"<br><font size=2>V[x,y] = valuation du plus court chemin pour aller de x à y </br>"+"<br> par les sommets intermédiaires {1,2,..,k} </br>" +
				"<br><font size=2>W(x,y) = poids de l'arc (x,y) (infini s'il n'existe pas)</br>" +
				"<br></font></I></html>";
		algo[1] =  "<html><font size=4><br><U> Algorithme:</U></br></font></html>";
		algo[2] =  "<html><font size= 3 color=#408080> <I> // Initialisation de la matrice V<sup>0</sup> : </I></font>"+"<br> Pour tout sommet i de 0 à N-1, </br>"+
				"<br><blockquote>Pour tout sommet j de 0 à N-1 </blockquote>"+"<blockquote><blockquote> V<sup>0</sup>[i,j]=W(i,j)</blockquote></blockquote>"+"<blockquote>Fin Pour</blockquote>"+"Fin Pour"+"<br></html>";
		algo[3] = "<html><font size=3 color=#408080><I> //Calcul des matrices successives V<sup>k</sup></font></html>";
		algo[4] = "<html>Pour chaque itération k de 1 à N </html>";
		algo[5] = "<html><blockquote>Pour chaque sommet i de 0 à N-1</blockquote></html>";
		algo[6] = "<html><blockquote><blockquote>Pour chaque sommet j de 0 à N-1</blockquote></blockquote></html>";
		algo[7] = "<html><blockquote><blockquote><blockquote>Si V<sup>k-1</sup>[i,k]+V<sup>k-1</sup>[k,j] &lt V<sup>k-1</sup>[i,j]"+"<br>alors V<sup>k</sup>[i,j] = V<sup>k-1</sup>[i,k]+V<sup>k-1</sup>[k,j]"+"</blockquote></html>";
		algo[8] = "<html><blockquote><blockquote>Fin Pour</blockquote></blockquote></html>";
		algo[9] = "<html><blockquote>Fin Pour </blockquote></html>";
		algo[10]= "<html>Fin Pour </html>";
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
		return NbIteration == graphe.size() && SommetArrivee==graphe.size()-1 && SommetDepart==graphe.size()-1;
	}

	public boolean isStart() {
		return NbIteration==0 && SommetDepart==0 && SommetArrivee==0;
	}

	public void nextStep() {
		saveGraph();
		if(NbIteration < this.graph.nbVertex())
			this.graphe.get(NbIteration).setUpdated(true);
		commentaires="NbIter = "+NbIteration;
		currentStep=4;
		if(!isEnd()){
			if(SommetDepart<this.graph.nbVertex()){
					currentStep=5;
						if(SommetArrivee<this.graph.nbVertex() && SommetDepart!=NbIteration){
							if(!this.graphe.get(SommetArrivee).isFixing()){
								this.graphe.get(SommetDepart).setFixing(true);
								this.graphe.get(SommetArrivee).setFixing(true);
								if (this.graph.containsEdge(this.graphe.get(SommetDepart),this.graphe.get(SommetArrivee)))
									this.graph.getEdge(this.graphe.get(SommetDepart),this.graphe.get(SommetArrivee)).setDescription(Description.REGULAR);	
							
								currentStep = 6;
							}
							else{
							currentStep=7;
							if(SommetArrivee != SommetDepart && SommetArrivee!=NbIteration){
								commentaires="On cherche un chemin entre "+SommetDepart+" et"+SommetArrivee+" passant par "+NbIteration;
								update(SommetDepart, NbIteration, SommetArrivee);
								}
							for(int i=0;i<graphe.size();i++)
								this.graphe.get(i).setFixing(false);
							SommetArrivee++;
							afficherValPred();
							}
						}
						else{
							currentStep = 8;
							SommetArrivee=0;
							SommetDepart++;
							for(int i=0;i<graphe.size();i++)
								this.graphe.get(i).setFixing(false);
						}
				
				
			}
			else{
				commentaires="On a exploré toutes les paires de sommets";
				currentStep = 9;
				SommetDepart=0;
				afficherValPred();
				PreviousVal = Val;
				this.graphe.get(NbIteration).setUpdated(false);
				NbIteration++;
				clearGraphe();
			}
		}	
		else currentStep=10;
	}
	

	private void clearGraphe() {
		Iterator<GWizVertex> i = graph.vertexSet().iterator();
		GWizVertex a = null;
		while (i.hasNext()){
			a = i.next();
			a.setFixed(false);
			a.setFixing(false);
		}
		Iterator<GWizEdge> j = graph.edgeSet().iterator();
		GWizEdge b = null;
		while(j.hasNext()){
			b = j.next();
			if(b.getDescription()!=Description.SELECT)
				b.setDescription(Description.REGULAR);
		}
		currentStep=4;
	}

	

	private void update(int i, int x, int j) {
		if(PreviousVal[i][x]+PreviousVal[x][j]<Val[i][j]){
			Val[i][j]=PreviousVal[i][x]+PreviousVal[x][j];
			Pred[i][j]=""+x;
			commentaires="Le chemin passant par "+NbIteration+" est plus court que le chemin courant...";
		}
		else
			commentaires="Le chemin passant par"+NbIteration+" n'est pas plus court que le chemin courant...";
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
	public void setStartingVertex (GWizVertex startingVertex) {
		if (startingVertex!=null){
			if (this.startingVertex!=null)
				this.startingVertex.reset();
			Iterator<GWizVertex> i = graph.vertexSet().iterator();
			while (i.hasNext())
				i.next().setValuated(true);
			this.startingVertex = startingVertex;
			this.startingVertex.setStart(true);
			this.startingVertex.setValuation(0);
			currentStep = 2;
		}
	}


	@Override
	public boolean isRunnable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void initialize() {
		commentaires="On initialise les matrices VAL et PRED";
		Iterator<GWizVertex> i = graph.vertexSet().iterator();
		GWizVertex a = null;
		while (i.hasNext()){
			a = i.next();
			a.setValuated(false);
			graphe.addElement(a);
			}
		Val = new double[graphe.size()][graphe.size()];
		Pred = new String[graphe.size()][graphe.size()];
		for(int j=0;j<graphe.size();j++){
			for(int k=0;k<graphe.size();k++){
				if(this.graph.containsEdge(this.graphe.get(j),this.graphe.get(k))){
					Val[j][k] = this.graph.getEdgeWeight(this.graph.getEdge(this.graphe.get(j), this.graphe.get(k)));
					this.graphe.get(k).setPred(this.graphe.get(j));
					Pred[j][k]=""+j;
				}
				else if(k!=j){
					Pred[j][k]=""+0;
					Val[j][k] = Double.POSITIVE_INFINITY;
				}
				else {
					Pred[j][k]=" -";
					Val[j][k]=0;
				}
			}
		}
		PreviousVal = Val;
		afficherValPred();
		currentStep = 2;
		graphe.get(0).setFixing(true);
		System.out.println("Fin de l'initialisation");
	}

	private void afficherValPred() {
		for(int i=0;i<Val.length;i++){
			System.out.print(" |");
			for(int j=0;j<Val[0].length;j++){
				System.out.print(" "+Val[i][j]+" ");
			}
			System.out.println("|");
		}
		
		for(int i=0;i<Pred.length;i++){
			System.out.print(" |");
			for(int j=0;j<Pred[0].length;j++){
				System.out.print(" "+Pred[i][j]+" ");
			}
			System.out.println("|");
		}
	}

	public double[][] getVal(){
		return PreviousVal;
	}
	
	public String[][] getPred(){
		return Pred;
	}
	
	public int getIteration(){
		return NbIteration;
	}
	public int getDepart(){
		return this.SommetDepart;
	}
	
	public int getArrivee(){
		return this.SommetArrivee;
	}
	@Override
	public String[] getAlgo() {
		// TODO Auto-generated method stub
		return algo;
	}

}
