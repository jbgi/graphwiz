package graphWiz.model;
import graphWiz.model.GWizEdge.Description;

import java.util.Iterator;

/**
 * @author  jbg
 */
public class Dijkstra extends Algorithm {
	
	private String[] algo;
	
	public Dijkstra(GWizGraph graph) {
		super(graph);
		
		algo = new String[8];
		
		algo[0] = "<html><font size=6>Algorithme de Dijkstra<br></font><br><I><font size=3><U>Notations:</U><br>"
			+ "V[x] = valuation du sommet x<br >"
			+ "W(x,y) = poids de l'arc (x,y)</font><br >"
			+ "<br ><br><font size=5><U>Algorithme: </U></font><br ></html>";
		
		algo[1] = "<html><br ><font size=4><U>Pr&eacute;-requis :</U><br >" +
				"Le graphe est orient&eacute; et ses arcs <br>ont des poids positif ou nul.<br></font></html>" +
				"";

		algo[2] = "<html><br><font size=4>Initialiser la valuation du sommet de d&eacute;part &agrave; 0 <br>"
			+ "et celle de tous les autres sommets &agrave; +&#8734 </font><br><br></html>";
		
		algo[3] = "<html><font size=4>Tant que tous les sommets ne sont pas fix&eacute;s<br></font></html>";

		algo[4] = "<html><blockquote><font size=4>S&eacute;lectionner le sommet x non fix&eacute;<br>de plus petite valuation</font></blockquote></html>";

		algo[5] = "<html><blockquote><font size=4>Pour chaque successeur non fix&eacute; y de x</blockquote>"
			+"<blockquote><blockquote><font size=4><b>Si</b>  V[x] + W(x,y)"+" &lt "+" v[y] <br> <b> alors </b> V[y] = V[x] + W(x,y)</blockquote></blockquote></font></html>";

		algo[6] = "<html><blockquote><font size=4>FinPour</blockquote>"
			+ "<blockquote><font size=4>Fixer le sommet x</font></blockquote></html>";

		algo[7] =  "<html><font size=4>Fin Tant Que</html>";
	}
	
	public String checkGraph() {
		// TODO Auto-generated method stub
		
		return null;
	}

	public boolean isEligible() {
		boolean eligible = true;
		Iterator<GWizVertex> i = getGraph().vertexSet().iterator();
		while (i.hasNext() && eligible){
			Iterator<GWizVertex> j = getGraph().vertexSet().iterator();
			while(j.hasNext() && eligible){
				eligible = (getGraph().getEdgeWeight(getGraph().getEdge(i.next(),j.next())) > 0);
			}
		}
		return eligible;
	}
	
	public boolean isEnd() {
		boolean allFixed = true;
		Iterator<GWizVertex> i = getGraph().vertexSet().iterator();
		while (i.hasNext() && allFixed)
			allFixed = allFixed && i.next().isFixed();
		if (allFixed){
			currentStep = 7;
			setComments("Tous les sommet sont fixés, l'algorithme est fini.\n"+
					"Cilquez sur un sommet pour faire apparaître le chemin optimum.");
		}
		
		return allFixed;
	}

	public boolean isStart() {
		return (getStartingVertex()==null || (!getStartingVertex().isFixed() && !getStartingVertex().isFixing()));
	}
	
	public void nextStep(){
		if (getStartingVertex()!=null && getStartingVertex().isStart()){
		if (!isEnd()){
			saveGraph();
			GWizVertex selectedVertex = selectVertex();
			if (selectedVertex.isFixing())
				updateSuccessorOf(selectedVertex);
			else if (!selectedVertex.isFixed())
				selectedVertex.setFixing(true);
		}
		}
	}

	@Override
	public void setEndVertex(GWizVertex endVertex) {
		if (endVertex!=null) {
			if (getEndVertex() == null || !getEndVertex().isEnd())
				saveGraph();
			else
				getEndVertex().setEnd(false);
			this.endVertex=endVertex;
			endVertex.setEnd(true);
			Iterator<GWizEdge> e = getGraph().edgeSet().iterator();
			while (e.hasNext())
				e.next().setDescription(Description.EXPLORED);
			GWizVertex pred = endVertex;
			while (pred.hasPred()){
				getGraph().getEdge(pred.getPred(), pred).setDescription(Description.PATH);
				pred = pred.getPred();
			}
			setComments("Voici le chemin optimum de " + getStartingVertex().getName()+ " à " + getEndVertex().getName()+ "d'après le tableau des prédécesseurs.");
		}
		else if (getEndVertex() != null )
			if (this.getEndVertex().isEnd()){
				restorePreviousGraph();
				this.getEndVertex().setEnd(false);
			}
	}

	/**
	 * @param getStartingVertex()  the getStartingVertex() to set
	 * @uml.property  name="getStartingVertex()"
	 */
	public void setStartingVertex(GWizVertex startingVertex) {
		if (startingVertex!=null){
			if (getStartingVertex()!=null)
				getStartingVertex().reset();
			Iterator<GWizVertex> i = getGraph().vertexSet().iterator();
			while (i.hasNext())
				i.next().setValuated(true);
			this.startingVertex=startingVertex;
			getStartingVertex().setStart(true);
			getStartingVertex().setValuation(0);
			currentStep = 2;
			setComments("Les valuation ont été initialiser, "+getStartingVertex()+" est le sommet de départ (valuation à 0)");
		}
	}

	private void checkAllSuccessorUpdated(GWizVertex vertex){
		Iterator<GWizEdge> i = getGraph().outgoingEdgesOf(vertex).iterator();
		boolean allUpdated = true;
		while (i.hasNext() && allUpdated){
			GWizEdge edge = i.next();
			GWizVertex n = getGraph().getEdgeTarget(edge);
			allUpdated = (n.isUpdated()||n.isFixed()) && allUpdated;
			if (getGraph().getEdgeTarget(edge).isUpdated()||getGraph().getEdgeTarget(edge).isFixed())
				edge.setDescription(Description.EXPLORED);
		}
		if (allUpdated){
			vertex.setFixed(true);
			vertex.setFixing(false);
			currentStep = 6;
			setComments("Tous les successeur de "+vertex.getName()+" ont été mis à jour.");
			Iterator<GWizEdge> j = getGraph().outgoingEdgesOf(vertex).iterator();
			while (j.hasNext())
				getGraph().getEdgeTarget(j.next()).setUpdated(false);
		}
	}

	private GWizVertex selectVertex() {
		GWizVertex selectedVertex = getStartingVertex();
		double minValuation = Double.POSITIVE_INFINITY;
		Iterator<GWizVertex> i = getGraph().vertexSet().iterator();
		GWizVertex nextVertex;
		while (i.hasNext()){
			nextVertex = i.next();
			if (nextVertex.isFixing()){
				checkAllSuccessorUpdated(nextVertex);
				if (nextVertex.isFixed() || nextVertex.isFixing())
					return nextVertex;
			}
			if (nextVertex.getValuation() <= minValuation && !nextVertex.isFixed()){
				selectedVertex = nextVertex;
				minValuation = selectedVertex.getValuation();
				setComments(selectedVertex.getName()+" est le sommet non-fixé de plus petite valuation.");
				currentStep = 4;
			}
		}
		return selectedVertex;
	}

	private void updateSuccessorOf(GWizVertex vertex){
		Iterator<GWizEdge> i = getGraph().outgoingEdgesOf(vertex).iterator();
		GWizVertex succ;
		GWizEdge edge;
		boolean oneUpdate=false;
		while (i.hasNext() && !oneUpdate){
			edge = i.next();
		   	succ = getGraph().getEdgeTarget(edge);
		   	if (!succ.isFixed() && !succ.isUpdated()){
		   		if (vertex.getValuation() + getGraph().getEdgeWeight(edge) < succ.getValuation()){
		   			succ.setValuation(vertex.getValuation()+getGraph().getEdgeWeight(edge));
		   			succ.setPred(vertex);
		   			setComments("La valuation du sommet "+succ.getName()+" a été mis à jour ("+(int) vertex.getValuation()+"+"+ (int) getGraph().getEdgeWeight(edge)+"="+ (int)(vertex.getValuation()+getGraph().getEdgeWeight(edge))+")\n"
		   					+ vertex.getName()+" est maintenant le prédecesseur de "+succ.getName()+"."); 
		   		}
		   		else 
		   			setComments("La valuation du sommet "+succ.getName()+" n'a pas été mis à jour (car "+(int) vertex.getValuation()+"+"+ (int) getGraph().getEdgeWeight(edge)+">="+ succ.getValuation()+")\n"
		   					+ succ.getPred().getName()+" reste le prédecesseur de "+succ.getName()+"."); 
		   		setCurrentVertex(succ);
		   		succ.setUpdated(true);
				edge.setDescription(Description.EXPLORER);
				oneUpdate = true;
				currentStep = 5;
		   	}
		   	else if (succ.isUpdated())
		   		succ.setUpdatedDone(true);
		}
	}

	@Override
	public boolean isRunnable() {
		return (getStartingVertex() != null && getStartingVertex().isStart());
	}

	@Override
	public void initialize() {
		Iterator<GWizVertex> i = getGraph().vertexSet().iterator();
		while (i.hasNext())
			i.next().setValuation(Float.POSITIVE_INFINITY);
		currentStep = 1;
		setComments("Cliquez sur un sommet de départ");
	}
	
	public String[] getAlgo() {
		return algo;
	}

}
