package graphWiz.model;

import graphWiz.model.GWizEdge.Description;

import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public class Floyd extends Algorithm{
	
	private int NbIteration=0;
	private boolean[][] CouplesOfCurrentVertex; 
	public Vector<GWizVertex> succ= new Vector<GWizVertex>();
	private String[] algo;
	private Vector<GWizVertex> graphe = new Vector<GWizVertex>();
	private Vector<GWizVertex> pred = new Vector<GWizVertex>();
	public double[][] Val;
	
	
	public Floyd(GWizGraph graph) {
		super(graph);
		Val = new double[this.graph.vertexSet().size()][this.graph.vertexSet().size()];
		CouplesOfCurrentVertex = new boolean[1][1];
		CouplesOfCurrentVertex[0][0]= false;
		algo = new String[12];
		algo[0] = "<html><font size=5>Algorithme de Floyd</font><br><br><I><font size=3><U> Notations:</U>"+
				"<br><font size=2>V[x,y] = valuation du plus court chemin pour aller de x à y </br>"+"<br> par les sommets intermédiaires {1,2,..,k} </br>" +
				"<br><font size=2>W(x,y) = poids de l'arc (x,y) (infini s'il n'existe pas)</br>" +
				"<br></font></I></html>";
		algo[1] =  "<html><font size=4><br><U> Algorithme:</U></br></font></html>";
		algo[2] =  "<html><font size= 3 color=#408080> <I> // Initialisation de la matrice V<sup>0</sup> : </I></font>"+"<br> Pour tout sommet i de 0 à N-1, </br>"+
				"<br><blockquote>Pour tout sommet j de 0 à N-1 </blockquote>"+"<blockquote><blockquote> V<sup>0</sup>[i,j]=W(i,j)</blockquote></blockquote>"+"<blockquote>Fin Pour</blockquote>"+"Fin Pour"+"<br></html>";
		algo[3] = "<html><font size=3 color=#408080><I> //Calcul des matrices successives V<sup>k</sup></font></html>";
		algo[4] = "<html>Pour chaque sommet k de 1 à N </html>";
		algo[5] = "<html><blockquote>Pour chaque sommet i de 0 à N-1</blockquote></html>";
		algo[6] = "<html><blockquote><blockquote>Pour chaque sommet j de 0 à N-1</blockquote></blockquote></html>";
		algo[7] = "<html><blockquote><blockquote><blockquote>Si V<sup>k-1</sup>[i,k]+V<sup>k-1</sup>[k,j] &lt V<sup>k-1</sup>[i,j]"+"<br>alors V<sup>k</sup>[i,j] = V<sup>k-1</sup>[i,k]+V<sup>k-1</sup>[k,j]"+"</blockquote></html>";
		algo[8] = "<html><blockquote><blockquote><blockquote>Fin Si</blockquote></blockquote></blockquote></html>";
		algo[9] = "<html><blockquote><blockquote>Fin Pour</blockquote></blockquote></html>";
		algo[10] = "<html><blockquote>Fin Pour </blockquote></html>";
		algo[11]= "<html>Fin Pour </html>";
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
		return NbIteration == graphe.size();
		
	}

	public boolean isStart() {
		boolean isStart = true;
		for(int i=0;i<this.graphe.size();i++){
			if(graphe.get(i).isFixing() || graphe.get(i).isFixed())
				isStart = false;
		}
		if (NbIteration == 0) isStart = false;
		return isStart;
	}

	public void nextStep() {
		System.out.println("nextStep()");
		if(!isEnd()){
			GWizVertex courant = selectVertex();
			if(PlusDeCouple()){
				currentStep=4;
				pred.clear();
				succ.clear();
				System.out.println("Fin de l'iteration "+NbIteration);
				NbIteration++;
			}
			else if(!PlusDeCouple()){
			UpdateOneCouple(courant);
			}
			afficherVal();
			//saveGraph();
		}
		else currentStep=11;
	}
	
	
	private void UpdateOneCouple(GWizVertex courant) {
		System.out.println("UpdateOneCouple");
		currentStep = 7;
		if(!PlusDeCouple()){
		int[] couple = selectCouple(courant);	
		int j=couple[1],i=couple[0];
		System.out.println("couple selectionne : ("+pred.get(i).getName()+","+succ.get(j).getName()+")...");
		
		double Vx = this.graph.getEdgeWeight(this.graph.getEdge(pred.get(i),courant));
		double Vy = this.graph.getEdgeWeight(this.graph.getEdge(courant, succ.get(j)));
		
		this.graph.getEdge(pred.get(i),courant).setDescription(Description.EXPLORER);
		this.graph.getEdge(courant, succ.get(j)).setDescription(Description.EXPLORER);
		
		miseAjour(courant,Vx,Vy,Integer.parseInt(pred.get(i).getName()),Integer.parseInt(succ.get(j).getName()),i,j);
		
		this.graph.getEdge(pred.get(i),courant).setDescription(Description.REGULAR);
		this.graph.getEdge(courant, succ.get(j)).setDescription(Description.REGULAR);
		CouplesOfCurrentVertex[i][j]=false;
		}
		else courant.isFixed();
	}

	private void miseAjour(GWizVertex courant,double vx, double vy, int i, int j,int a, int b) {
		if(vx+vy<Val[i][j]){
			Val[i][j]=vx+vy;
			courant.setPred(pred.get(a));
			succ.get(b).setPred(courant);
			this.graph.getEdge(pred.get(a),succ.get(b)).setDescription(Description.PATH);
			System.out.println("Couple Ameliore : ("+Integer.parseInt(pred.get(a).getName())+","+Integer.parseInt(succ.get(b).getName())+")...");
		}
		
	}

	private int[] selectCouple(GWizVertex courant) {
		System.out.println("SelectCouple");
		int[] couple = new int[2];
		boolean trouve = false;
		if(!PlusDeCouple()){
			for(int a=0;a<CouplesOfCurrentVertex.length;a++){
				for(int b=0;b<CouplesOfCurrentVertex[0].length;b++){
					if(CouplesOfCurrentVertex[a][b]){
						if(!trouve){
							couple[0]=a;couple[1]=b;
							trouve = true;
						}
					}
				}
			}
		}
		System.out.println("couple selectionne");
		return couple;
	}

	private GWizVertex selectVertex() {
		System.out.println("selectVertex");
		GWizVertex selectedVertex = graphe.get(NbIteration);
		if(!selectedVertex.isFixing()){
			UpdatepredsuccOf(selectedVertex);	
		}
		selectedVertex.setFixing(true);
		return selectedVertex;
	}
	
	
	public void UpdatepredsuccOf(GWizVertex v){
		pred.clear();
		succ.clear();
		System.out.println("UpdatepredsuccOf");
		//on remplit pred et succ
		Iterator<GWizEdge> incoming = this.graph.incomingEdgesOf(v).iterator();
		Iterator<GWizEdge> outgoing = this.graph.outgoingEdgesOf(v).iterator();
		GWizEdge courant = new GWizEdge();
		System.out.println("pred de "+v.getName());
		while(incoming.hasNext()){
			courant = incoming.next();
			pred.addElement(this.graph.getEdgeSource(courant));
			System.out.print(this.graph.getEdgeSource(courant).getName()+" - ");
		}
		System.out.println("");
		System.out.println("il y a"+pred.size()+"predecesseurs");
		System.out.println("succ de "+v.getName());
		while(outgoing.hasNext()){
			courant = outgoing.next();
			succ.addElement(this.graph.getEdgeTarget(courant));
			System.out.print(this.graph.getEdgeTarget(courant).getName()+" - ");
		}
		CouplesOfCurrentVertex = new boolean[pred.size()][succ.size()];
		System.out.println("");
		System.out.println("il y a"+succ.size()+"successeurs");
		//on ne garde que les couples susceptibles d'être mis à jour
		TrierPredSuccOf(v);
		
	}

	private void TrierPredSuccOf(GWizVertex v) {
		//CouplesOfCurrentVertex = new boolean[pred.size()][succ.size()];
		System.out.println("");
		for(int i=0; i<pred.size();i++){
			for(int j=0; j<succ.size();j++){
				if(this.graph.containsEdge(pred.get(i), succ.get(j))){
					CouplesOfCurrentVertex[i][j]= true;
					//this.graph.getEdge(pred.get(i),succ.get(j)).setDescription(Description.EXPLORER);
				}
				else CouplesOfCurrentVertex[i][j]= false;
				System.out.print(CouplesOfCurrentVertex[i][j]);
			}	
			System.out.println("");
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
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void initialize() {
		Iterator<GWizVertex> i = graph.vertexSet().iterator();
		GWizVertex a = null;
		while (i.hasNext()){
			a = i.next();
			a.setValuated(false);
			a.setHasPred(true);
			graphe.addElement(a);
			}
		Val = new double[graphe.size()][graphe.size()];
		for(int j=0;j<graphe.size();j++){
			for(int k=0;k<graphe.size();k++){
				if(this.graph.containsEdge(this.graphe.get(j),	this.graphe.get(k)))
					Val[j][k] = this.graph.getEdgeWeight(this.graph.getEdge(this.graphe.get(j), this.graphe.get(k)));
				else Val[j][k] = Double.POSITIVE_INFINITY;
			}
		}
		afficherVal();
		currentStep = 0;
		System.out.println("Fin de l'initialisation");
	}

	private void afficherVal() {
		for(int i=0;i<Val.length;i++){
			System.out.print(" |");
			for(int j=0;j<Val[0].length;j++){
				System.out.print(" "+Val[i][j]+" ");
			}
			System.out.println("|");
		}
		
	}

	@Override
	public String[] getAlgo() {
		// TODO Auto-generated method stub
		return algo;
	}
	
	private boolean PlusDeCouple(){
		boolean plus = true;
		for(int i =0; i<this.CouplesOfCurrentVertex.length;i++){
			for(int j=0; j<this.CouplesOfCurrentVertex[0].length;j++){
				if(this.CouplesOfCurrentVertex[i][j])
					plus = false;
			}
		}
		System.out.println("Il n'y a plus de couples : "+plus);
		return plus;
	}

}
