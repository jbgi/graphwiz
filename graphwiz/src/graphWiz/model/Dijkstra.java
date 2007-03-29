package graphWiz.model;
import graphWiz.model.GWizEdge.Description;
import java.util.Iterator;

/**
 * @author  jbg
 */
public class Dijkstra extends Algorithm {
	
	private GWizVertex startingVertex;
	private GWizVertex endVertex;
	
	private String[] algo;
	
	public Dijkstra(GWizGraph graph) {
		super(graph);
		
		algo = new String[8];
		
		algo[0] = "<html><font size=6>Algorithme de Dijkstra<br></font><br><I><font size=3><U>Notations:</U><br>"
			+ "V[x] = valuation du sommet x<br >"
			+ "W(x,y) = poids de l'arc (x,y)</font><br >"
			+ "<br ><br><font size=5><U> Algorithme: </U></font><br ></html>";
		
		algo[1] = "<html><br ><font size=4><U>Pré-requis :</U><br >" +
				"Le graphe est orienté et ses arcs <br>ont des poids négatifs ou nul.<br></font></html>" +
				"";

		algo[2] = "<html><br><font size=4>Initialiser la valuation du sommet de départ à 0 <br>"
			+ "et celle de tous les autres sommets à +&#8734 </font><br><br></html>";
		
		algo[3] = "<html><font size=4>Tant que tous les sommets ne sont pas fixés<br></font></html>";

		algo[4] = "<html><blockquote><font size=4>Sélectionner le sommet x non fixé<br>de plus petite valuation</font></blockquote></html>";

		algo[5] = "<html><blockquote><font size=4>Pour chaque successeur non fixé y de x</blockquote>"
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
		if (allFixed)
			currentStep = 7;
		return allFixed;
	}

	public boolean isStart() {
		return (startingVertex==null || (!startingVertex.isFixed() && !startingVertex.isFixing()));
	}
	
	public void nextStep(){
		if (startingVertex!=null && startingVertex.isStart()){
		//	setStartingVertex(graph.vertexSet().iterator().next());
		if (!isEnd()){
			saveGraph();
			GWizVertex selectedVertex = selectVertex();
			if (selectedVertex.isFixing())
				updateSuccessorOf(selectedVertex);
			else if (!selectedVertex.isFixed())
				selectedVertex.setFixing(true);
		}
		else
			currentStep = 7;
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

	/**
	 * @param startingVertex  the startingVertex to set
	 * @uml.property  name="startingVertex"
	 */
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

	private void checkAllSuccessorUpdated(GWizVertex vertex){
		Iterator<GWizEdge> i = graph.outgoingEdgesOf(vertex).iterator();
		boolean allUpdated = true;
		while (i.hasNext() && allUpdated){
			GWizEdge edge = i.next();
			GWizVertex n = graph.getEdgeTarget(edge);
			allUpdated = (n.isUpdated()||n.isFixed()) && allUpdated;
			if (graph.getEdgeSource(edge)==graph.getEdgeTarget(edge).getPred()) {
				Iterator<GWizEdge> i2 = graph.incomingEdgesOf(graph.getEdgeTarget(edge)).iterator();
				while (i2.hasNext()){
					GWizEdge e = i2.next();
					if (e.getDescription()==Description.PATH)
						e.setDescription(Description.EXPLORED);
				}
				edge.setDescription(Description.PATH);
			}
			else if (graph.getEdgeTarget(edge).isUpdated()||graph.getEdgeTarget(edge).isFixed())
				edge.setDescription(Description.EXPLORED);
		}
		if (allUpdated){
			vertex.setFixed(true);
			vertex.setFixing(false);
			currentStep = 6;
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
				currentStep = 4;
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
				currentStep = 5;
		   	}
		   	else if (succ.isUpdated())
		   		succ.setUpdatedDone(true);
		}
	}

	@Override
	public boolean isRunnable() {
		return (startingVertex != null && startingVertex.isStart());
	}

	@Override
	public void initialize() {
		Iterator<GWizVertex> i = graph.vertexSet().iterator();
		while (i.hasNext())
			i.next().setValuation(Float.POSITIVE_INFINITY);
		currentStep = 1;
	}
	
	public String[] getAlgo() {
		return algo;
	}

}
