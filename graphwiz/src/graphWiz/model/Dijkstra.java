package graphWiz.model;
import graphWiz.model.GWizEdge.Description;
import java.util.Iterator;

/**
 * @author  jbg
 */
public class Dijkstra extends Algorithm {
	
	private GWizVertex startingVertex;
	private GWizVertex endVertex;
	
	public Dijkstra() {
		super();
		
		algo = new String[6];
		
		algo[0] = "<html><I><font size=3><U> Notations:</U><br />"
			+ "V[x] = valuation du sommet x<br />"
			+ "W(x,y) = poids de l'arc (x,y)</font><br />"
			+ "<br /><font size=4><U> Algorithme: </U></font><br /></html>";

		algo[1] = "<html><br /><font size=4>Initialiser la valuation du sommet de départ à 0 <br/>"
			+ "et celle de tous les autres sommets à +&#8734 </font></html>";

		algo[2] = "<html><br /><font size=4>Tant que tous les sommets ne sont pas fixés<br />"
			+ "<blockquote>Sélectionner le sommet x non fixé de plus petite valuation</blockquote></font></html>";

		algo[3] = "<html><br /><font size=4><blockquote>Pour chaque successeur non fixé y de x</blockquote><br />"
			+"<blockquote><blockquote>Si  &quot V[x] + W(x,y)"+" &lt "+" v[y]&quot  alors &quot V[y] = V[x] + W(x,y)&quot</blockquote></blockquote></font></html>";

		algo[4] = "<html><br /><font size=4><blockquote>FinPour</blockquote><br />"
			+ "<blockquote>Fixer le sommet x</blockquote></font></html>";

		algo[5] =  "<html><br /><font size=4>Fin Tant Que<br /></html>";
	}
	
	public Dijkstra(GWizGraph graph) {
		this();
		this.graph=graph;
	}
	
	public String checkGraph() {
		// TODO Auto-generated method stub
		
		return null;
	}

	public boolean isEligible() {
		boolean eligible = true;
		Iterator<GWizVertex> i = graph.vertexSet().iterator();
		while (i.hasNext() && eligible){
			Iterator<GWizVertex> j = graph.vertexSet().iterator();
			while(j.hasNext() && eligible){
				eligible = (graph.getEdgeWeight(graph.getEdge(i.next(),j.next())) > 0);
			}
		}
		return eligible;
	}
	
	public boolean isEnd() {
		boolean allFixed = true;
		Iterator<GWizVertex> i = graph.vertexSet().iterator();
		while (i.hasNext() && allFixed)
			allFixed = allFixed && i.next().isFixed();
		return allFixed;
	}

	public boolean isStart() {
		return !startingVertex.isFixed();
	}
	
	public void nextStep(){
		System.out.println("nextStep");
		if (startingVertex==null)
			setStartingVertex(graph.vertexSet().iterator().next());
		if (!isEnd()){
			saveGraph();
			GWizVertex selectedVertex = selectVertex();
			if (selectedVertex.isFixing())
				updateSuccessorOf(selectedVertex);
			else if (!selectedVertex.isFixed())
				selectedVertex.setFixing(true);
		}
		else
			currentStep = 5;
	}

	public void previousStep(){
		restorePreviousGraph();
	}


	@Override
	public void setEndVertex(GWizVertex endVertex) {
		this.endVertex=endVertex;
	}

	/**
	 * @param startingVertex  the startingVertex to set
	 * @uml.property  name="startingVertex"
	 */
	public void setStartingVertex (GWizVertex startingVertex) {
		this.startingVertex = startingVertex;
		startingVertex.setValuation(0);
	}

	private void checkAllSuccessorUpdated(GWizVertex vertex){
		Iterator<GWizEdge> i = graph.outgoingEdgesOf(vertex).iterator();
		boolean allUpdated = true;
		while (i.hasNext() && allUpdated){
			GWizEdge edge = i.next();
			GWizVertex n = graph.getEdgeTarget(edge);
			allUpdated = (n.isUpdated()||n.isFixed()) && allUpdated;
			if (graph.getEdgeSource(edge)==graph.getEdgeTarget(edge).getPred()) {
				Iterator<GWizEdge> i2 = graph.incomingEdgesOf(graph.getEdgeTarget(edge)).iterator();
				while (i2.hasNext())
					i2.next().setDescription(Description.REGULAR);
				edge.setDescription(Description.PATH);
			}
			else if (graph.getEdgeTarget(edge).isUpdated()||graph.getEdgeTarget(edge).isFixed())
				edge.setDescription(Description.REGULAR);
		}
		if (allUpdated){
			vertex.fixeMe();
			vertex.setFixing(false);
			currentStep = 4;
			Iterator<GWizEdge> j = graph.outgoingEdgesOf(vertex).iterator();
			while (j.hasNext())
				graph.getEdgeTarget(j.next()).setUpdated(false);
		}
	}

	private GWizVertex selectVertex() {
		GWizVertex selectedVertex = startingVertex;
		double minValuation = Double.POSITIVE_INFINITY;
		Iterator<GWizVertex> i = graph.vertexSet().iterator();
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
				currentStep = 2;
			}
		}
		if (!selectedVertex.isFixed()){
			Iterator<GWizEdge> e = graph.outgoingEdgesOf(selectedVertex).iterator();
			while (e.hasNext()){
				GWizEdge edge = e.next();
				if (!graph.getEdgeTarget(edge).isFixed() && !graph.getEdgeTarget(edge).isUpdated())
					edge.setDescription(Description.SELECT);
			}
		}
		return selectedVertex;
	}

	private void updateSuccessorOf(GWizVertex vertex){
		Iterator<GWizEdge> i = graph.outgoingEdgesOf(vertex).iterator();
		GWizVertex succ;
		GWizEdge edge;
		boolean oneUpdate=false;
		while (i.hasNext() && !oneUpdate){
			edge = i.next();
		   	succ = graph.getEdgeTarget(edge);
		   	if (!succ.isFixed() && !succ.isUpdated()){
		   		if (vertex.getValuation() + graph.getEdgeWeight(edge) < succ.getValuation()){
		   			succ.setValuation(vertex.getValuation()+graph.getEdgeWeight(edge));
		   			succ.setPred(vertex);
		   		}
		   		succ.setUpdated(true);
				edge.setDescription(Description.EXPLORER);
				oneUpdate = true;
				currentStep = 3;
		   	}
		   	else if (succ.isUpdated())
		   		succ.setUpdatedDone(true);
		}
	}

}
