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
	}
	
	public Dijkstra(GWizGraph graph) {
		this();
		this.graph=graph;
	}
	
	public String checkGraph() {
		// TODO Auto-generated method stub
		
		return null;
	}

	public String[] getAlgo() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getCurrentStep() {
		return 0;
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
		if (startingVertex==null){
			setStartingVertex((GWizVertex) graph.vertexSet().toArray()[0]);
			System.out.println("startingVertex==null");
		}
		if (!isEnd()){
			saveGraph();
			System.out.println("not End");
			GWizVertex selectedVertex = selectVertex();
			if (selectedVertex.isFixing()){
				System.out.println(selectedVertex + " isFixing");
				updateSuccessorOf(selectedVertex);
			}
			else {
				selectedVertex.setFixing(true);
				System.out.println(selectedVertex + " setFixing");
			}
		}
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
			allUpdated = graph.getEdgeTarget(i.next()).isUpdated() && allUpdated;
		}
		if (allUpdated){
			vertex.fixeMe();
			System.out.println(vertex + " isFixed");
			Iterator<GWizEdge> j = graph.outgoingEdgesOf(vertex).iterator();
			while (j.hasNext()){
				GWizEdge edge = j.next();
				graph.getEdgeTarget(edge).setUpdated(false);
				edge.setDescription(Description.REGULAR);
			}
		}
	}

	private GWizVertex selectVertex() {
		GWizVertex selectedVertex = startingVertex;
		double minValuation = Double.POSITIVE_INFINITY;
		Iterator<GWizVertex> i = graph.vertexSet().iterator();
		GWizVertex nextVertex;
		while (i.hasNext()){
			nextVertex = i.next();
			if (nextVertex.isFixing())
				checkAllSuccessorUpdated(nextVertex);
			if (nextVertex.getValuation() <= minValuation && !nextVertex.isFixed()){
				selectedVertex = nextVertex;
				minValuation = selectedVertex.getValuation();
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
		   		if (vertex.getValuation() + graph.getEdgeWeight(edge) <= succ.getValuation()){
		   			succ.setValuation(vertex.getValuation()+graph.getEdgeWeight(edge));
		   			succ.setPred(vertex);
		   		}
		   		succ.setUpdated(true);
				edge.setDescription(Description.EXPLORER);
				oneUpdate = true;
		   	}
		}
	}

}
